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



import java.util.ArrayList;
import java.util.List;

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.reportable.AbstractReportable;

/**
 * JHOVE2 presumptive format identification.
 * 
 * @author mstrong, slabrams, smorrissey
 */
public class FormatIdentification extends AbstractReportable implements 
		Comparable<FormatIdentification> {
	/** Identification confidence levels. */
	public enum Confidence {
		Negative(6), Tentative(5), Heuristic(4), PositiveGeneric(3), PositiveSpecific(
				2), Validated(1);

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
	/** format identification returned by identifying module (might not be JHOVE2 namespace) */
	protected I8R identification;
	
	protected I8R jhove2Identification;
	
	/** Identification confidence level. */
	protected Confidence confidence;


	/** Identification product. 
	 *  We'll need a instance with every invocation of this class, because these
	 * identifiers are potentially stateful, unlike this class, which is effectively stateless */
	protected I8R identifierProduct;

	
	/**
	 * Messages associated with this identification
	 */
	protected List<Message> messages;
	
	
	/**
	 * Instantiate a new <code>FormatIdentification</code>.
	 * 
	 * @param jhove2FormatId
	 *            Presumptively identified format id (JHOVE2 namespace)
	 * @param confidence
	 *            Identification confidence level
	 */
	public FormatIdentification(I8R jhove2FormatId, Confidence confidence) {
		this(jhove2FormatId, confidence, null, jhove2FormatId, null);
	}

	
	/**
	 * Instantiate a new <code>FormatIdentification</code>.
	 * 
	 * @param jhove2FormatId
	 *            Presumptively identified format id (JHOVE2 namespace)
	 * @param confidence
	 *            Identification confidence level
	 */
	public FormatIdentification(I8R jhove2FormatId, Confidence confidence, I8R identifierProduct) {
		this(jhove2FormatId, confidence, identifierProduct, jhove2FormatId, null);
	}
	
	/**
	 * Instantiate a new <code>FormatIdentification</code>.
	 * 
	 * @param jhove2FormatId
	 *            Presumptively identified format id (JHOVE2 namespace)
	 * @param confidence
	 *            Identification confidence level
	 * @param process
	 *            Identification process
	 */
	public FormatIdentification(I8R jhove2FormatId, Confidence confidence,
			I8R process, I8R id, List<Message> messages) {
		this.jhove2Identification = jhove2FormatId;
		this.confidence = confidence;
		this.identifierProduct = process;
		this.identification = id;
		if (messages==null){
			this.messages = new ArrayList<Message>();
		}
		else {
			this.messages = messages;
		}
	}
	@ReportableProperty(order = 1, value = "Format Identification returned by identifier.")
	public I8R getIdentification() {
		return identification;
	}
	/**
	 * Get identification process.
	 * 
	 * @return Identification process
	 */
	@ReportableProperty(order = 3, value = "Identifier product.")
	public I8R getIdentifierProduct() {
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

	@ReportableProperty(order = 5, value = "Messages returned by identifier.")
	public List<Message> getMessages() {
		return messages;
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
		if (this.getIdentification()==null){
			if (id.getIdentification()!=null){
				return -1;
			}
		}
		else if (id.getIdentification()==null){
			return 1;
		}
		else {
			int idComp = this.getIdentification().compareTo(id.getIdentification());
			if (idComp != 0){
				return idComp;
			}
		}
		int compareFormat = this.getJhove2Identification().compareTo(
				id.getJhove2Identification());
		if (compareFormat != 0){
			return compareFormat;
		}
		if (this.getIdentifierProduct()==null){
			if (id.getIdentifierProduct()!= null){
				return -1;
			}
		}
		else if (id.getIdentifierProduct()==null){
			return 1;
		}
		else {
			int compareProduct = this.getIdentifierProduct().compareTo
						(id.getIdentifierProduct());
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
		if (this.getIdentification()==null){
			if (fiObj.getIdentification()!=null){
				return false;
			}
		}
		boolean equals = (this.getIdentification().equals(fiObj.getIdentification()));
		if (!equals){
			return equals;
		}			
		equals = (this.getJhove2Identification().equals
				            (fiObj.getJhove2Identification()));
		if (!equals){
			return equals;
		}		
		if (this.getIdentifierProduct()==null){
			if (fiObj.getIdentifierProduct() != null){
				return false;
			}
		}
		else if (fiObj.getIdentifierProduct()==null){
			return false;
		}
		else {
			equals = this.getIdentifierProduct().equals(fiObj.getIdentifierProduct());
			if (!equals){
				return equals;
			}
		}			
		equals = this.getConfidence().getOrder()== fiObj.getConfidence().getOrder();
		return equals;
	}
	

	public void setConfidence(Confidence confidence) {
		this.confidence = confidence;
	}


	public void setIdentifierProduct(I8R identifierProduct) {
		this.identifierProduct = identifierProduct;
	}


	public void setIdentification(I8R identification) {
		this.identification = identification;
	}

	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}

	/**
	 * @return the jhove2FormatIdentification
	 */
	@ReportableProperty(order = 2, value = "Format Identification returned by identifier.")
	public I8R getJhove2Identification() {
		return jhove2Identification;
	}

	/**
	 * @param jhove2FormatIdentification the jhove2FormatIdentification to set
	 */
	public void setJhove2Identification(I8R jhove2FormatIdentification) {
		this.jhove2Identification = jhove2FormatIdentification;
	}

}
