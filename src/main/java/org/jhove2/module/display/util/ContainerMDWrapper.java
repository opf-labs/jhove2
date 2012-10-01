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

package org.jhove2.module.display.util;

import java.io.IOException;
import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jdom.JDOMException;

import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * A wrapper for the data needed to build the containerMD section
 * of the manifest. Data are gathered during the parse of the JHove2
 * characterization result analysis and aggregated.
 */
public final class ContainerMDWrapper
{	
	public static final  String CONTAINER_PREFIX = "containerMD";

	public static final  String CONTAINER_URI = "http://bibnum.bnf.fr/ns/containerMD-v1";

	public static final DateFormat rawDateFormat = new MtSafeDateFormat("yyyyMMddHHmmss");

	public static final DateFormat dateFormat = new MtSafeDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

    private static Logger log = Logger.getLogger(ContainerMDWrapper.class.getName());

	private final static Pattern hostExtractor = Pattern.compile("^([a-zA-Z]*):/{0,3}([.[^/#?:]]*)(?:.*)");
	private final static long MIN_VALID_DATE = 19700101000000L;

	/** ArcRecordSource handlers */
	public Map<String,ContainerElement> encodings;
	public Map<String,ContainerElement> formats;
	public Map<String,ContainerElement> declaredMimeTypes;
	public Map<String,ContainerElement> hosts;
	public Map<String,ContainerElement> responses;

	private long firstDateTime = -1L;
	private long lastDateTime  = -1L;

	private long minimumSize   = Long.MAX_VALUE;
	private long maximumSize   = 0L;
	private long globalSize	   = 0L;

	/** Permitted attributes for the containerMD elements */
	protected enum AttributeName
	{
		NUMBER("number"), 
		NAME("name"),
		SIZE("size"), 
		TYPE("type"),
		METHOD("method"),
		ORDER("order"),
		PROTOCOL_NAME("protocolName"),
		PROTOCOL_VERSION("protocolVersion"), 
		GLOBALSIZE("globalSize") ;
		
		public final String key;
		
		private AttributeName(String key) {
			this.key = key;
		}

		@Override
		public String toString() {
			return this.key;
		}
	}

	/**
	 * Creates a new ContainerMDWrapper instance.
	 */
	public ContainerMDWrapper()
	{
		/** Initialization */
		this.formats = new HashMap<String,ContainerElement>();
		this.declaredMimeTypes = new HashMap<String,ContainerElement>();
		this.encodings = new HashMap<String,ContainerElement>();
		this.hosts     = new HashMap<String,ContainerElement>();
		this.responses = new HashMap<String,ContainerElement>();
		log.log(Level.FINE, "{}|ContainerMDWrapper 0x{}: new",
		          new Object[] { 
                    Thread.currentThread().getName(),
                    Integer.toHexString(System.identityHashCode(this)) 
                  });
	}
	
	public void addEntry(String sourceName, long size, String dateTime,
			             String format, String mimeType,
			             String protocolVersion, String codeResponse) {
	        log.log(Level.FINEST, "addEntry: {} ({}, {}, {})",
	                  new Object[] { sourceName, Long.valueOf(size),
	                                 dateTime, mimeType });
		if (size >= 0L) {
		    this.setMaximumSize(size);
		    this.setMinimumSize(size);
		    this.setGlobalSize(size);
		}
		if (! isBlank(dateTime)) {
		    try {
		        long l = Long.parseLong(dateTime);
		        this.setFirstDateTime(l);
		        this.setLastDateTime(l);
		    }
		    catch (Exception e) {
		        log.log(Level.WARNING, "Invalid ARC entry date ({}) for {}",
		                 new Object[] { dateTime, sourceName });
		        /* Ignore... */
		    }
		}
		Matcher m = hostExtractor.matcher(sourceName);
		if (m.matches()) {
			String protocol = m.group(1);
			String hostName = m.group(2);

			this.handleHost(hostName, size);
			this.handleResponse(protocolVersion, protocol, codeResponse,size);
		}
		if ((mimeType != null) && (mimeType.length() != 0)) {
			this.handleDeclaredMimeType(mimeType,size);
		}
		if ((format != null) && (format.length() != 0)) {
			this.handleFormat(format, size);
		}
	}

	/**
	 * Checks whether hosts have been handled.
	 * @return true | false
	 */
	public boolean hasHosts()
	{
		return !hosts.isEmpty();
	}
	
	/**
	 * Checks whether responses have been handled.
	 * @return true | false
	 */
	public boolean hasResponses()
	{
		return !responses.isEmpty();
	}
	
	/**
	 * Checks whether encodings have been handled.
	 * @return true | false
	 */
	public boolean hasEncodings()
	{
		return !encodings.isEmpty();
	}
	
	/**
	 * Checks whether formats have been handled.
	 * @return true | false
	 */
	public boolean hasFormats() {
		return !formats.isEmpty();
	}
	
	/**
	 * Checks whether declared mimeTypes have been handled.
	 * @return true | false
	 */
	public boolean hasDeclaredMimeTypes() {
		return !declaredMimeTypes.isEmpty();
	}
	
	/**
	 * Sets maximum size
	 * @param size
	 */
	public void setMaximumSize(long size) {
		if (this.maximumSize < size) { 
			this.maximumSize = size;
		}
	}

	/**
	 * Gets maximumSize
	 * @return <code>String</code>
	 */
	public String getMaximumSize() {
		return Long.toString(this.maximumSize);
	}

	/**
	 * Sets minimum size
	 * @param size
	 */
	public void setMinimumSize(long size) {
		if ((size >= 0L) && (this.minimumSize > size)) { 
			this.minimumSize = size ;
		}
	}

	/**
	 * Sets the global size
	 * @param size
	 */
	public void setGlobalSize(long size) {
	    if (size >= 0L) {
	        this.globalSize += size;
	    }
	}
	/**
	 * Gets the global size
	 * @return <code>long</code>
	 */
	public long getGlobalSize() {
		return this.globalSize;
	}

	/**
	 * Gets minimumSize
	 * @return <code>String</code>
	 */
	public String getMinimumSize() {
	    log.log(Level.FINE, "{}|{}", 
            new Object[] { Thread.currentThread().getName(), this });
		return Long.toString(
		    (this.minimumSize == Long.MAX_VALUE)? 0L: this.minimumSize);
	}

	/**
	 * Sets firstLastTime
	 * @param dateTime
	 */
	public void setFirstDateTime(long dateTime) {
		if ((firstDateTime == -1L) || (firstDateTime > dateTime)) {
			firstDateTime = dateTime;
		}
	}

	/**
	 * Gets firstDateTime
	 * @return <code>String</code>
	 * @throws ParseException
	 */
	public String getFirstDateTime() throws ParseException {
		return formatDateTime(longToDate(this.firstDateTime));
	}

	/**
	 * Sets lastDateTime
	 * @param dateTime
	 */
	public void setLastDateTime(long dateTime) {
		if (lastDateTime < dateTime) {
			lastDateTime = dateTime;
		}
	}

	/**
	 * Gets lastDateTime
	 * @return <code>String</code>
	 * @throws ParseException
	 */
	public String getLastDateTime() throws ParseException {
		return formatDateTime(longToDate(this.lastDateTime));
	}

	/**
	 * Returns containerMD encoding elements formatted into XML.
	 * @return <code>String</code>
	 */
	public String getEncodings() throws JDOMException, IOException
	{
		return toXml(this.encodings.values());
	}
	
	/**
	 * Returns containerMD declared mimeTypes elements formatted into XML.
	 * @return <code>String</code>
	 */
	public String getDeclaredMimeTypes() throws JDOMException, IOException
	{
		return toXml(this.declaredMimeTypes.values());
	}
	
	/**
	 * Returns containerMD format elements formatted into XML.
	 * @return <code>String</code>
	 */
	public String getFormats() throws JDOMException, IOException {
		return toXml(this.formats.values());
	}
	
	/**
	 * Returns containerMD host elements formatted into XML.
	 * @return <code>String</code>
	 */
	public String getHosts() throws JDOMException, IOException
	{
		return toXml(this.hosts.values());
	}
	
	/**
	 * Returns containerMD response elements formatted into XML.
	 * @return <code>String</code>
	 */
	public String getResponses() throws JDOMException, IOException
	{
		return toXml(this.responses.values());
	}
	
	/**
	 * Formats a given long "yyyyMMddHHmmss" into a date 
	 * @param date
	 * @return Long
	 */
	protected Date longToDate(long date) throws ParseException {
	    Date d = null;
	    if (date >= MIN_VALID_DATE) {
		d = rawDateFormat.parse(String.valueOf(date));
	    }
	    return d;
	}
	
	/**
	 * Formats a given date into a long "yyyyMMddHHmmss"
	 * @param date
	 * @return Long
	 */
	protected Long dateToLong(Date date) throws ParseException
	{
		String stringDate = rawDateFormat.format( date );
		return Long.valueOf(stringDate);
	}

	/**
	 * Formats a given date into "yyyy-MM-dd'T'HH:mm:ss'Z'".
	 * 
	 * @param DateTime
	 * @return <code>String</code>
	 * @throws ParseException
	 */
	protected String formatDateTime(Date date) throws ParseException {
	    if (date == null) {
	        throw new IllegalArgumentException("Invalid date: " + date);
	    }
	    return dateFormat.format(date);
	}

	public String toXml(Collection<ContainerElement> elts) 
	                                    throws JDOMException, IOException {
		StringBuilder stringElements = new StringBuilder();
		for (ContainerElement e : elts) {
			stringElements.append( e.toString(CONTAINER_PREFIX) );
		}
		return stringElements.toString();
	}

	/**
	 * Handles distinct encodings
	 * @param encoding
	 */
	public void handleEncoding(String type, String method)
	{
		if( !encodings.containsKey( method ))
		{
			ContainerElement container = new ContainerElement("encoding");

			container.getAttributes().put(AttributeName.TYPE, type);
			container.getAttributes().put(AttributeName.METHOD, method);
			container.getAttributes().put(AttributeName.ORDER,
			                              Integer.valueOf(encodings.size()+1));
			encodings.put(method,container);
		}
	}
	
	/**
	 * Handles distinct formats
	 * @param format
	 * @param size
	 */
	public void handleFormat(String format, long size)
	{
		ContainerElement container = formats.get( format );
		if( container != null )
		{
			Map<AttributeName,Object> attrs = container.getAttributes();
			((AtomicInteger)attrs.get(AttributeName.NUMBER)).incrementAndGet();
			((AtomicLong)attrs.get(AttributeName.GLOBALSIZE)).addAndGet(size);
		}else {
			container =  new ContainerElement("format");

			container.getAttributes().put(AttributeName.NAME, format);
			container.getAttributes().put(AttributeName.NUMBER, new AtomicInteger(1));
			container.getAttributes().put(AttributeName.GLOBALSIZE, new AtomicLong(size));

			formats.put(format, container );
		}
	}
	
	/**
	 * Handles distinct declared mimeTypes
	 * @param mimeType
	 */
	public void handleDeclaredMimeType(String mimeType,long size)
	{
		ContainerElement container = declaredMimeTypes.get(mimeType);
		if( container != null )
		{
			Map<AttributeName,Object> attrs = container.getAttributes();
			((AtomicInteger)attrs.get(AttributeName.NUMBER)).incrementAndGet();
			((AtomicLong)attrs.get(AttributeName.GLOBALSIZE)).addAndGet(size);
		}else{
			container =  new ContainerElement("declaredMimeType", mimeType);
			
			container.getAttributes().put(AttributeName.NUMBER, new AtomicInteger(1));
			container.getAttributes().put(AttributeName.GLOBALSIZE, new AtomicLong(size));
			declaredMimeTypes.put(mimeType, container );
		}		
	}
	
	/**
	 * Handles distinct hosts
	 * @param host
	 * @param size
	 */
	public void handleHost(String host, long size)
	{
		ContainerElement container = hosts.get(host);
		if( container != null )
		{
			Map<AttributeName,Object> attrs = container.getAttributes();
			((AtomicInteger)attrs.get(AttributeName.NUMBER)).incrementAndGet();
			((AtomicLong)attrs.get(AttributeName.GLOBALSIZE)).addAndGet(size);
		}else{
			container =  new ContainerElement("host", host);
			
			container.getAttributes().put(AttributeName.NUMBER, new AtomicInteger(1));
			container.getAttributes().put(AttributeName.GLOBALSIZE, new AtomicLong(size));
			hosts.put(host, container );
		}
	}
	
	/**
	 * Handles distinct response
	 * @param protocolVersion
	 * @param protocolName
	 * @param codeResponse
	 */
	public void handleResponse(String protocolVersion, String protocolName, String codeResponse, long size)
	{
		String key = protocolName + '|' + protocolVersion + '|' + codeResponse;
		ContainerElement container = responses.get(key);
		if( container != null )
		{
			Map<AttributeName,Object> attrs = container.getAttributes();
			((AtomicInteger)attrs.get(AttributeName.NUMBER)).incrementAndGet();
			((AtomicLong)attrs.get(AttributeName.GLOBALSIZE)).addAndGet(size);
		}else{
			container =  new ContainerElement("response", codeResponse);		
			container.getAttributes().put(AttributeName.NUMBER, new AtomicInteger(1));
			container.getAttributes().put(AttributeName.PROTOCOL_NAME, protocolName );
			container.getAttributes().put(AttributeName.PROTOCOL_VERSION, protocolVersion );		
			container.getAttributes().put(AttributeName.GLOBALSIZE, new AtomicLong(size));
			responses.put(key, container );
		}
	}

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder(512);
        buf.append("ContainerMDWrapper 0x")
           .append(Integer.toHexString(System.identityHashCode(this)))
           .append(" { ");
        buf.append("minimumSize=").append(this.minimumSize).append(", ");
        buf.append("maximumSize=").append(this.maximumSize).append(", ");
        buf.append("globalSize=").append(this.globalSize).append(", ");
        buf.append("firstDateTime=").append(this.firstDateTime).append(", ");
        buf.append("lastDateTime=").append(this.lastDateTime).append(", ");
        buf.append("encodings=").append(this.encodings).append(", ");
        buf.append("MIME types=").append(this.declaredMimeTypes).append(", ");
        buf.append("formats=").append(this.formats).append(", ");
        buf.append("hosts=").append(this.hosts).append(", ");
        buf.append("responses=").append(this.responses);
        return buf.append(" }").toString();
    }

    /**
     * Check if a string is <code>null</code>, empty or contains only
     * whitespace characters.
     *
     * @param  s   the string to check, may be <code>null</code>.
     *
     * @return <code>true</code> if the string is <code>null<code>,
     *         empty ("") or contains only whitespaces characters.
     */
    public static boolean isBlank(String s) {
        return ((s == null) || (s.trim().length() == 0));
    }

    /*
     * Thread safe date formatter.
     */
    private final static class MtSafeDateFormat extends SimpleDateFormat {
        /**
		 * UID.
		 */
		private static final long serialVersionUID = -8797209035403605920L;

		public MtSafeDateFormat(String pattern) {
            super(pattern);
            this.setLenient(false);
            this.setTimeZone(TimeZone.getTimeZone("UTC"));
        }

        @Override
        public synchronized Date parse(String source) throws ParseException {
            return super.parse(source);
        }

        @Override
        public synchronized StringBuffer format(Date date,
                                                StringBuffer toAppendTo,
                                                FieldPosition pos) {
            return super.format(date, toAppendTo, pos);
        }
    }
}
