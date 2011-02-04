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

package org.jhove2.core.format;

import java.util.ArrayList;
import java.util.List;

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.I8R;
import org.jhove2.core.Message;
import org.jhove2.core.reportable.AbstractReportable;

import com.sleepycat.persist.model.Persistent;

/**
 * JHOVE2 presumptive format identifier.
 * 
 * @author mstrong, slabrams, smorrissey
 */
@Persistent
public class FormatIdentification
    extends AbstractReportable
    implements Comparable<FormatIdentification>
{
	/** Identification confidence levels. */
	public enum Confidence {
          /**
           * TODO: define.
           */
          Negative        (6),
          /**
           * TODO: define.
           */
          Tentative       (5),
          /**
           * TODO: define.
           */
          Heuristic       (4),
          /**
           * TODO: define.
           */
          PositiveGeneric(3),
          /**
           * TODO: define.
           */
          PositiveSpecific(2),
          /**
           * TODO: define.
           */
          Validated       (1);

          /** Priority order. */
          private int order;

          /**
           * Instantiate a new <code>Confidence</code> enum.
           * @param order
           *            Priority order
           */
          private Confidence(int order) {
                  this.order = order;
          }

          /**
           * Get priority order.
           * @return Priority order
           */
          public int getOrder() {
                  return this.order;
          }
	}
	/** Format identifier native to the identification process, which may not
	 *  be in the JHOVE2 namespace.
	 */
	protected I8R identifier;
	
	/** Canonical JHOVE2 identifier in the JHOVE2 namespace. */
	protected I8R jhove2Identifier;
	
	/** Identification confidence level. */
	protected Confidence confidence;

	/** Identifier for the product performing the identification process. */
	protected I8R identifierProduct;

	/** Messages produced by the identification process. */
	protected List<Message> messages;
	
	@SuppressWarnings("unused")
	private FormatIdentification(){
		this(null, null);
	}
	/**
	 * Instantiate a new <code>FormatIdentification</code>.
	 * 
	 * @param canonical
	 *            Canonical format identifier in the JHOVE2 namespace
	 * @param confidence
	 *            Identification confidence level
	 */
	public FormatIdentification(I8R canonical, Confidence confidence) {
		this(canonical, confidence, null, canonical, null);
	}
	
	/**
	 * Instantiate a new <code>FormatIdentification</code>.
	 * 
	 * @param canonical
	 *            Canonical format identifier in the JHOVE2 namespace
	 * @param confidence
	 *            Identification confidence level
	 * @param product
	 *            Identifier for the product performing the identification process
	 */
	public FormatIdentification(I8R canonical, Confidence confidence, I8R product) {
		this(canonical, confidence, product, canonical, null);
	}
	
	/**
	 * Instantiate a new <code>FormatIdentification</code>.
	 * 
	 * @param canonical
	 *            Canonical format identifier in the JHOVE2 namespace
	 * @param confidence
	 *            Identification confidence level
	 * @param product
	 *            Identifier of product performing the identification process
	 * @param identifier
	 *            Identifier native to the identification process
	 * @param messages
	 *            Messages produced by the identification process
	 */
	public FormatIdentification(I8R canonical, Confidence confidence,
			I8R product, I8R identifier, List<Message> messages) {
		this.jhove2Identifier = canonical;
		this.confidence = confidence;
		this.identifierProduct = product;
		this.identifier = identifier;
		if (messages==null) {
			this.messages = new ArrayList<Message>();
		}
		else {
			this.messages = messages;
		}
	}
	
	/** Get format identifier native to the identification process.
	 * @return Format identifier native to the identification process
	 */
	@ReportableProperty(order = 1, value = "Format identifier native to the " +
	        "identification process.")
	public I8R getNativeIdentifier() {
		return identifier;
	}
	
	/**
	 * Get identifier for product performing the identification process.
	 * @return product identifier
	 */
	@ReportableProperty(order = 3, value = "Identifier for the product " +
	        "performing the identification process.")
	public I8R getIdentificationProduct() {
		return this.identifierProduct;
	}

	/**
	 * Get identification confidence level.
	 * 
	 * @return Identification confidence level
	 */
	@ReportableProperty(order = 4, value = "Identification confidence level.")
	public Confidence getConfidence() {
		return this.confidence;
	}

    /** Get canonical JHOVE2 format identifier in the JHOVE2 namespace.
     * @return the jhove2FormatIdentification
     */
    @ReportableProperty(order = 2, value = "Canonical format identifier in " +
            "the JHOVE2 namespace.")
    public I8R getJHOVE2Identifier() {
        return this.jhove2Identifier;
    }
    
    /**
     *
     * @return
     */
    @ReportableProperty(order = 5, value = "Messages returned by identifier.")
	public List<Message> getMessages() {
		return messages;
	}
    
    /** Set identification confidence level.
     * @param confidence Identification confidence level
     */
    public void setConfidence(Confidence confidence) {
        this.confidence = confidence;
    }

    /** Set identifier for the product performing the identification process.
     * @param product Product identifier
     */
    public void setIdentificationProduct(I8R product) {
        this.identifierProduct = product;
    }

    /** Set format identifier native to the identification process, which may
     * be in the JHOVE2 namespace.
     * @param identifier Format identifier native to the identification process
     */
    public void setIdentifier(I8R identifier) {
        this.identifier = identifier;
    }

    /** Set canonical JHOVE2 identifier in the JHOVE2 namespace.
     * @param identifier the jhove2FormatIdentification to set
     */
    public void setJhove2Identification(I8R identifier) {
        this.jhove2Identifier = identifier;
    }

    /** Set messages produced by the identification process.
     * @param messages Messages produced by the identification process
     */
    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
    
	/**
	 * Lexically compare format identifications.
	 * 
	 * @param id
	 *            IdentifierModule to be compared
	 * @return -1, 0, or 1 if this identifier value is less than, equal to, or
	 *         greater than the second
	 * @see java.lang.Comparable#compareTo(Object)
	 */
	@Override
	public int compareTo(FormatIdentification id) {
		if (id==null){
			return 1;
		}
		if (this==id){
			return 0;
		}
		if (this.getNativeIdentifier()==null){
			if (id.getNativeIdentifier()!=null){
				return -1;
			}
		}
		else if (id.getNativeIdentifier()==null){
			return 1;
		}
		else {
			int idComp = this.getNativeIdentifier().compareTo(id.getNativeIdentifier());
			if (idComp != 0){
				return idComp;
			}
		}
		int compareFormat = this.getJHOVE2Identifier().compareTo(
				id.getJHOVE2Identifier());
		if (compareFormat != 0){
			return compareFormat;
		}
		if (this.getIdentificationProduct()==null){
			if (id.getIdentificationProduct()!= null){
				return -1;
			}
		}
		else if (id.getIdentificationProduct()==null){
			return 1;
		}
		else {
			int compareProduct = this.getIdentificationProduct().compareTo
						(id.getIdentificationProduct());
			if (compareProduct != 0){
				return compareProduct;
			}
		}
		int order1 = this.confidence.getOrder();
		int order2 = id.getConfidence().getOrder();
		if (order1 < order2) {
			return -1;
		} 
		else if (order1 > order2) {
			return 1;
		}
		else{ 
			return 0;
		}
	}
	
	/** Determine equality with another format identification.
	 * @param obj Format identification to be compared
	 */
	@Override
	public boolean equals (Object obj){
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (! (obj instanceof FormatIdentification)){
			return false;
		}
		FormatIdentification fiObj = (FormatIdentification) obj;
		if (this.getNativeIdentifier()==null){
			if (fiObj.getNativeIdentifier()!=null){
				return false;
			}
		}
		boolean equals = (this.getNativeIdentifier().equals(fiObj.getNativeIdentifier()));
		if (!equals){
			return equals;
		}			
		equals = (this.getJHOVE2Identifier().equals
				            (fiObj.getJHOVE2Identifier()));
		if (!equals){
			return equals;
		}		
		if (this.getIdentificationProduct()==null){
			if (fiObj.getIdentificationProduct() != null){
				return false;
			}
		}
		else if (fiObj.getIdentificationProduct()==null){
			return false;
		}
		else {
			equals = this.getIdentificationProduct().equals(fiObj.getIdentificationProduct());
			if (!equals){
				return equals;
			}
		}			
		equals = this.getConfidence().getOrder()== fiObj.getConfidence().getOrder();
		return equals;
	}

    /** Generate a unique has code for the format identification.
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((confidence == null) ? 0 : confidence.hashCode());
        result = prime * result
                + ((identifier == null) ? 0 : identifier.hashCode());
        result = prime
                * result
                + ((identifierProduct == null) ? 0 : identifierProduct
                        .hashCode());
        result = prime
                * result
                + ((jhove2Identifier == null) ? 0 : jhove2Identifier.hashCode());
        return result;
    }
 }
