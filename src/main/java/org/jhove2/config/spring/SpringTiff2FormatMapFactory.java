/**
 * JHOVE2 - Next-generation architecture for format-aware characterization
 * <p>
 * Copyright (c) 2010 by The Regents of the University of California. All rights reserved.
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
package org.jhove2.config.spring;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.format.Format;
import org.jhove2.module.format.tiff.Tiff2FormatMapFactory;
import org.springframework.context.ApplicationContext;

import com.sleepycat.persist.model.NotPersistent;
import com.sleepycat.persist.model.Persistent;

/**
 * @author smorrissey
 *
 */
@Persistent
public class SpringTiff2FormatMapFactory implements Tiff2FormatMapFactory {

    /** Map from tags to formats for the content of the tags. */
    @NotPersistent
	private static ConcurrentMap<String, Format> tagToFormatMap;
    private static String tiff2FormatBeanName = "TiffTagToFormatMap";
	/**
	 * 
	 */
	public SpringTiff2FormatMapFactory() {
		super();
	}


	public synchronized ConcurrentMap<String, Format> getTagtoFormatMap()
			throws JHOVE2Exception {
		if (tagToFormatMap==null){
			ConcurrentHashMap<String, Format> tfMap = null;
			Class<ConcurrentHashMap<String, Format>> mClass;
			try {
				mClass = (Class<ConcurrentHashMap<String, Format>>) Class.forName("java.util.concurrent.ConcurrentHashMap");
			} catch (ClassNotFoundException e1) {
				throw new JHOVE2Exception("Unable to create bean " + tiff2FormatBeanName, e1);
			}
			ApplicationContext context = SpringConfigInfo.getContext();
			try {
				tfMap = (ConcurrentHashMap<String, Format>)context.getBean(tiff2FormatBeanName, mClass);
				tagToFormatMap = tfMap;
			}
			catch (Exception e){
				throw new JHOVE2Exception("Unable to create bean " + tiff2FormatBeanName, e);
			}
		}
		return tagToFormatMap;
	}

	@Override
	public Format getFormat(int tag) throws JHOVE2Exception {
		Format format = null;
		String formatInt = String.valueOf(tag);
		format = this.getTagtoFormatMap().get(formatInt);
		return format;
	}

}
