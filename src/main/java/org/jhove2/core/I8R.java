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

package org.jhove2.core;

/** A JHOVE2 identifier.  Note that this class is named "I8R", not "Identifier"
 * to avoid confusion between "identifier" as a label and "identifier" as a
 * process that determines a format.
 * 
 * @author mstrong, slabrams
 */
public class I8R
	implements Comparable<I8R>
{
	/** JHOVE2 namespace identifier prefix. */
	public static final String JHOVE2_PREFIX = "info:jhove2/";
	
	/** JHOVE2 namespace identifier format infix. */
	public static final String JHOVE2_FORMAT_INFIX = "format/";
	
	/** JHOVE2 namespace identifier
	 * {@link org.jhove2.annotation.ReportableProperty) infix.
	 */
	public static final String JHOVE2_PROPERTY_INFIX = "property/";
	
	/** JHOVE2 namespace identifier
	 * {@link org.jhove2.annotation.Reportable} infix.
	 */
	public static final String JHOVE2_REPORTABLE_INFIX = "reportable/";
	
	/** Identifier types, or namespaces. */
	public enum Namespace {
		AFNOR,      /* AFNOR standard */
		AIIM,       /* AIIM standard */
		ANSI,       /* ANSI standard */
		ARK,        /* ARK identifier */
		BCP,        /* IETF Best Community Practice */
		BSI,        /* BSI standard */
		CallNumber, /* Call number */
		CCITT,      /* CCITT standard */
		Charset,    /* IANA charset */
		DDC,        /* Dewey Decimal Classification */
		DOI,        /* Digital Object Identifier */
		ECMA,       /* ECMA standard */
		FDD,        /* Library of Congress FDD identifier */
		FIPS,       /* FIPS standard */
		FourCC,     /* 4CC Standard */
		GUID,       /* Globally Unique Identifier */
		Handle,     /* Handle */
		I3A,        /* I3A standard */
		IEC,        /* IEC standard */
		ISBN,       /* International Standard Book Number */
		ISO,        /* ISO standard */
		ISSN,       /* International Standard Serial Nummber */
		ITU,        /* ITU standard */
		JEITA,      /* JEITA standard */
		JHOVE2,     /* JHOVE2 identifier */
		LCC,        /* Library of Congress Classification */
		LCCN,       /* Library of Congress Control Number */
		MIME,       /* MIME media type */
		NISO,       /* NISO standard */
		OCLC,       /* OCLC number */
		PII,        /* Publisher Item Identifier */
		PUID,       /* PRONOM Unique Identifier */
		PURL,       /* Persistent URL */
		RFC,        /* IETF Request for Comments */
		Shelfmark,  /* Shelfmark */
		SICI,       /* Serial Item and Contribution Identifier */
		SMPTE,      /* SMPTE standard */
		SN,         /* Serial number */
		STD,        /* IETF standard */
		TOM,        /* TOM identifier */
		UUID,       /* Universally Unique Identifier */
		URI,        /* W3C Uniform Resource Identifier */
		URL,        /* W3C Uniform Resource Locator */
		URN,        /* W3C Uniform Resource Name */
		UTI,        /* Apple Uniform Type Identifier */
		Other
	}

	/** Identifier namespace. */
	protected Namespace namespace;
	
	/** Identifier value. */
	protected String value;

	/** Instantiate a <code>I8R</code> identifier in the JHOVE2 namespace. 
	 * @param value Identifier value
	 */
	public I8R(String value)
	{
		this(value, Namespace.JHOVE2);
	}
	
	/** Instantiate a new <code>I8R</code>. 
	 * @param value     Identifier value
	 * @param namespace Identifier namespace
	 */
	public I8R(String value, Namespace namespace)
	{
		this.value     = value;
		this.namespace = namespace;
	}

	/** Get the identifier namespace.
	 * @return Identifier namespace
	 */
	public Namespace getNamespace ()
	{
		return this.namespace;
	}

	/** Get the identifier value.
	 * @return Identifier value
	 */
	public String getValue ()
	{
		return this.value;
	}
	
	/** Get a String representation of the identifier, in the form
	 * "namespace:identifier".
	 * @return String representation of the identifier
	 */
	@Override
	public String toString()
	{
		return this.namespace.toString() + ":" + value;
	}

	/** Lexically compare identifier.
	 * @param identifier Identifier to be compared
	 * @return -1, 0, or 1 if this identifier value is less than, equal
	 *         to, or greater than the second
	 * @see java.lang.comparable#compareTo(Object)
	 */
	@Override
	public int compareTo(I8R identifier) {
		int ret = this.namespace.toString().compareToIgnoreCase(identifier.getNamespace().toString());
		if (ret == 0) {
			ret = this.value.compareToIgnoreCase(identifier.getValue());
		}
		return ret;
	}
}
