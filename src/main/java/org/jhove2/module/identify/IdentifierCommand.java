/**
 * 
 */
package org.jhove2.module.identify;

import java.util.Set;

import org.jhove2.core.FormatIdentification;
import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Command;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.TimerInfo;
import org.jhove2.core.reportable.ReportableFactory;
import org.jhove2.core.source.Source;
import org.jhove2.module.AbstractModule;

/**
 * {@link org.jhove2.core.JHOVE2Command} to perform identification on
 * {@link org.jhove2.core.source.Source}s.
 * Note that this module does not detect
 * {@link org.jhove2.core.source.ClumpSource} format instances. 
 * 
 * @author smorrissey
 *
 */
public class IdentifierCommand
	extends AbstractModule
	implements JHOVE2Command
{
	/** IdentifierCommand module version identifier. */
	public static final String VERSION = "0.5.4";

	/** IdentifierCommand module release date. */
	public static final String RELEASE = "2009-12-22";

	/** IdentifierCommand module rights statement. */
	public static final String RIGHTS = "Copyright 2009 by The Regents of the University of California, "
		+ "Ithaka Harbors, Inc., and The Board of Trustees of the Leland "
		+ "Stanford Junior University. "
		+ "Available under the terms of the BSD license.";
	
	/** Instantiate a new <code>IdentifierCommand</code> module.
	 */
	public IdentifierCommand(){
		this(VERSION, RELEASE, RIGHTS);
	}
	
	/**
	 * Instantiate a new <code>IdentifierCommand</code> module.
	 * @param version Module version	
	 * @param release Module release date	
	 * @param rights  Module rights statement	
	 */
	public IdentifierCommand(String version, String release, String rights) {
		super(version, release, rights);
	}

	/**
	 * Instantiates Identifier and executes its identify method, attaching any resulting
	 * FormatIdentifications to the Source
	 * @param source Source to be identified
	 * @param jhove2 JHOVE2 framework object
	 * @throws JHOVE2Exception
	 * @see org.jhove2.core.JHOVE2Command#execute(org.jhove2.core.JHOVE2, org.jhove2.core.source.Source)
	 * @see org.jhove2.module.identify.Identifier#identify(org.jhove2.core.JHOVE2, org.jhove2.core.source.Source)
	 */
	@Override
	public void execute(JHOVE2 jhove2, Source source)
		throws JHOVE2Exception
	{	
		source.addModule(this);
		try {		
			Identifier identifier =
				ReportableFactory.getReportable(Identifier.class,
					                           "Identifier");			
			source.addModule(identifier);
			TimerInfo timer = identifier.getTimerInfo();
			timer.setStartTime();
			try {
				Set<FormatIdentification> formats = identifier.identify(jhove2,
					                                                    source);
				source.addPresumptiveFormats(formats);
			}
			finally {
				timer.setEndTime();
			}
		}
		catch (Exception e){
			throw new JHOVE2Exception("failed to execute identification", e);
		}	
        return;
	}
}
