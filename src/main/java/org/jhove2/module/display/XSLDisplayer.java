/**
 * JHOVE2 - Next-generation architecture for format-aware characterization
 *
 * Copyright (c) 2009 by The Regents of the University of California,
 * Ithaka Harbors, Inc., and The Board of Trustees of the Leland Stanford
 * Junior University.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * o Redistributions of source code must retain the above copyright notice,
 *   this list of conditions and the following disclaimer.
 *
 * o Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * o Neither the name of the University of California/California Digital
 *   Library, Ithaka Harbors/Portico, or Stanford University, nor the names of
 *   its contributors may be used to endorse or promote products derived from
 *   this software without specific prior written permission.
 *
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
 */

package org.jhove2.module.display;

import java.io.File;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.transform.Templates;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.persist.ModuleAccessor;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import com.sleepycat.persist.model.Persistent;

/**
 * An extension of the {@link XMLDisplayer} that directly pipelines
 * the XML output to an XSLT stylesheet.
 * <p>
 * This displayer shall be configured (in JHOVE2 Spring configuration)
 * with scope <code>prototype</code> as a new TrAX Transformer object
 * shall be allocated for each new Reportable to display.</p>
 *
 * @author lbihanic
 */
@Persistent
public class XSLDisplayer extends XMLDisplayer {

	/** The SAX transformer factory. */
    private static SAXTransformerFactory stf;
    /** The cache of compiled XLST stylesheets. */
    private static ConcurrentMap<String,Templates> stylesheets =
                                    new ConcurrentHashMap<String, Templates>();

    private static Logger log = Logger.getLogger(XSLDisplayer.class.getName());

    File styleSheetFile;
    /** The XSLT stylesheet to apply, as a compile TrAX Templates object. */
    transient Templates stylesheet = null;
    /** The XSLT processor as a SAX ContentHandler. */
    transient ContentHandler out = null;

	/**
	 * Instantiate a new <code>XSLDisplayer</code>.
	 */
    public XSLDisplayer() {
        this(null);
    }

	/**
	 * Instantiate a new <code>XMLDisplayer</code>.
     * @param moduleAccessor 
	 * 		     Displayer persistence manager 
	 */
	public XSLDisplayer(ModuleAccessor moduleAccessor) {
		super(moduleAccessor);
	}

	/** {@inheritDoc} */
    @Override
    public void startDisplay(PrintStream out, int level) {
        this.out = this.newTransformer(out);
        this.declaration(out);
        this.startTag(out, level, ELEROOT);
    }

    /** {@inheritDoc} */
    @Override
    public void endDisplay(PrintStream out, int level) {
        this.endTag(out, level, ELEROOT);
        try {
            this.out.endPrefixMapping(XSI);
            this.out.endDocument();
        }
        catch (SAXException e) {
            throw new RuntimeException(e);
        }
        finally {
            this.out = null;
        }
    }

    /** {@inheritDoc} */
    @Override
    public void declaration(PrintStream out) {
        try {
            this.out.startDocument();
            this.out.startPrefixMapping(XSI, XSI_URI);
        }
        catch (SAXException e) {
            throw new RuntimeException(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void startTag(PrintStream out, int level, String name) {
        try {
            // Use String interning to limit memory consumption.
            name = name.intern();
            if (log.isLoggable(Level.FINE)) {
                log.fine("</" + name + '>');
            }

            this.out.startElement(this.uri, name, name, new AttributesImpl());
        }
        catch (SAXException e) {
            throw new RuntimeException(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void startTag(PrintStream out, int level, String name,
                                                     String... attrs) {
        try {
            // Use String interning to limit memory consumption.
            name = name.intern();

            AttributesImpl atts = new AttributesImpl();
            for (int i = 0; i < attrs.length; i += 2) {
                String attrName = attrs[i].intern();
                atts.addAttribute("", attrName, attrName,
                                                "CDATA", attrs[i+1].intern());
            }
            if (log.isLoggable(Level.FINE)) {
                StringBuilder buf = new StringBuilder(256);
                buf.append('<').append(name);
                if (attrs.length != 0) {
                    for (int i = 0; i < attrs.length; i += 2) {
                        buf.append(' ').append(attrs[i])
                           .append("=\"").append(attrs[i+1]).append('"');
                    }
                }
                log.fine(buf.append('>').toString());
            }
            this.out.startElement(this.uri, name, name, atts);
        }
        catch (SAXException e) {
            throw new RuntimeException(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void endTag(PrintStream out, int level, String name) {
        String iname = name.intern();
        try {
            // Use String interning to limit memory footprint.
            // name = name.intern();
            if (log.isLoggable(Level.FINE)) {
                log.fine("</" + iname + '>');
            }
            this.out.endElement(this.uri, iname, iname);
        }
        catch (SAXException e) {
            throw new RuntimeException("Error end tag <" + iname + ">.", e);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void tag(PrintStream out, int level, String name, String content) {
        try {
            // Use String interning to limit memory footprint.
            name = name.intern();
            if (log.isLoggable(Level.FINE)) {
                log.fine("<" + name + '>' + content + "</" + name + '>');
            }
            this.out.startElement(this.uri, name, name, new AttributesImpl());

            if ((content != null) && (content.length() != 0)) {
                char[] ch = content.toCharArray();
                this.out.characters(ch, 0, ch.length);
            }
            this.out.endElement(this.uri, name, name);
        }
        catch (SAXException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Create a new TrAX Transformer object to apply the
     * {@link #setStylesheet specified XSLT stylesheet} to the XML
     * output of this displayer.
     * @param  out   the stream where to output the result of the
     *               transformation.
     * @return the SAX ContentHandler object of the XSLT processor
     *         to which direct the SAX events of the XML output.
     */
    protected ContentHandler newTransformer(OutputStream out) {
        // Ensure the transformer factory has been initialized.
        getTransformerFactory(); 
        try {
            if (this.stylesheet == null && this.styleSheetFile != null) {
                loadStyleSheet();
            }
            
            // Get a new transformer, using the identity stylesheet
            // if none was installed.
            TransformerHandler h = (this.stylesheet != null)?
                                    stf.newTransformerHandler(this.stylesheet):
                                    stf.newTransformerHandler();
            h.setResult(new StreamResult(out));
            return h;
        }
        catch (TransformerException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * <i>[Dependency injection]</i> Sets the XSLT stylesheet to apply
     * when outputting XML.
     * @param  f   the XSLT stylesheet file or <code>null</code> to use
     *             the identity transformation.
     *
     * @throws TransformerException if any error occurred while parsing
     *         the stylesheet.
     */
    public void setStylesheet(File f) throws TransformerException {
        if (f == null) {
            throw new IllegalArgumentException("f");
        }
        this.styleSheetFile = f;
        loadStyleSheet();
    }

    public void loadStyleSheet() throws TransformerException {
        if (this.stylesheet != null) return;
        if (this.styleSheetFile == null) return;
        
        String styleSheetPath = this.styleSheetFile.getAbsolutePath();
        Templates t = stylesheets.get(styleSheetPath);
        if (t == null) {
            t = getTransformerFactory().newTemplates(new StreamSource(this.styleSheetFile));
            stylesheets.put(styleSheetPath, t);
        }
        this.stylesheet = t;
    }
    
    /**
     * Returns the singleton instance of {@link SAXTransformerFactory}.
     * @return the singleton instance of the SAX transformer factory
     *         configured at the JVM level.
     */
    private static SAXTransformerFactory getTransformerFactory() {
        if (stf == null) {
            stf = (SAXTransformerFactory)(TransformerFactory.newInstance());
        }
        return stf;
    }
    
    @ReportableProperty(order = 1, value = "StyleSheet file")
	public String getStylesheetFile() {
		return this.styleSheetFile.getAbsolutePath() + ((this.stylesheet == null)?" null stylesheet":"stylesheet");
	}

}
