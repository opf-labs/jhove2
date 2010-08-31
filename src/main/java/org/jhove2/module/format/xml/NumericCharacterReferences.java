/**
 * JHOVE2 - Next-generation architecture for format-aware characterization
 * <p>
 * Copyright (c) 2009 by The Regents of the University of California, Ithaka
 * Harbors, Inc., and The Board of Trustees of the Leland Stanford Junior
 * University. All rights reserved.
 * </p>
 * <p>
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * </p>
 * <ul>
 * <li>Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.</li>
 * <li>Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.</li>
 * <li>Neither the name of the University of California/California Digital
 * Library, Ithaka Harbors/Portico, or Stanford University, nor the names of its
 * contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.</li>
 * </ul>
 * <p>
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * </p>
 */
package org.jhove2.module.format.xml;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.Message;
import org.jhove2.core.Message.Context;
import org.jhove2.core.Message.Severity;
import org.jhove2.core.io.Input;
import org.jhove2.core.reportable.AbstractReportable;

/**
 * A class to hold a sorted set of numeric character references (NCRs) that are
 * used to represent Unicode characters, and the counts of how many times each
 * NCR reference was found in the XML document.
 * 
 * @see http://www.w3.org/International/questions/qa-escapes
 * @see http://unicode.org/standard/principles.html#Assigning_Codes
 */
public class NumericCharacterReferences extends AbstractReportable {

    /**
     * The regular expression that would match a numeric character reference.
     * The capture group allows extraction of just the numeric code portion.
     */
    private static final String NCR_REGEX = "&#([0-9]+|[xX][0-9a-fA-F]+);";

    /**
     * The compiled regular expression for the NCR.
     */
    private static final Pattern NCR_PATTERN = Pattern.compile(NCR_REGEX);

    /** The de-duplicated set of NCRs found in the XML document. */
    TreeMap<Integer, NumericCharacterReference> numericCharacterReferenceMap 
        = new TreeMap<Integer, NumericCharacterReference>();

    /** Invalid character for encoding message. */
    protected Message invalidCharacterForEncodingMessage;

    /**
     * Get the NCRs found during XML parsing.
     * 
     * @return the numeric character references
     */
    @ReportableProperty(order = 1, value = "numeric character references found during XML parsing")
    public ArrayList<NumericCharacterReference> getNumericCharacterReferenceList() {
        return new ArrayList<NumericCharacterReference>(
                numericCharacterReferenceMap.values());
    }

    /**
     * Increment the instance count for this numeric character reference.
     * 
     * @param code
     *            the string representation of a character's unicode code point
     */
    public void tally(String code) {
        Integer codePoint = Integer.decode(code.replace("x", "0x"));
        NumericCharacterReference reference = numericCharacterReferenceMap
                .get(codePoint);
        if (reference != null) {
            reference.count++;
        }
        else {
            numericCharacterReferenceMap.put(codePoint,
                    new NumericCharacterReference(codePoint));
        }
    }

    /**
     * In order to locate numeric character references (like the code for double
     * dagger = &#x2021; ), we need to do a separate parse of the source object.
     * The SAX2 parser does not provide a mechanism for getting at these markup
     * constructs, which are not considered XML entities. The characters()
     * method of the ContentHandler interface, translates these codes into
     * Unicode characters before placing the data in the buffer.
     * @param jhove2 TODO
     * @param jhove2
     *            the JHOVE2 framework
     * @param source
     *            the source object
     * 
     * @throws IOException
     * @throws JHOVE2Exception
     */
    protected void parse(Input input, String encodingFromSAX2, JHOVE2 jhove2)
            throws IOException, JHOVE2Exception {
        try {
            /* Get a CharSequence object that can be analyzed */
            ByteBuffer bbuf = input.getBuffer();
            CharBuffer cbuf = Charset.forName(encodingFromSAX2).newDecoder()
                    .decode(bbuf);
            /* Look for numeric character references */
            Matcher ncrMatcher = NCR_PATTERN.matcher(cbuf);
            while (ncrMatcher.find()) {
                /*
                 * Found one, record the occurrence of the NCR code (pattern
                 * capture group 1)
                 */
                tally(ncrMatcher.group(1));
            }
        }
        catch (CharacterCodingException e) {
            this.invalidCharacterForEncodingMessage = new Message(
                    Severity.ERROR, Context.OBJECT,
                    "org.jhove2.module.format.xml.XmlModule.invalidCharacterForEncodingMessage",
                    jhove2.getConfigInfo());
        }
    }

}
