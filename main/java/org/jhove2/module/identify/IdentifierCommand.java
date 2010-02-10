/**
 * 
 */
package org.jhove2.module.identify;

import java.util.Set;

import org.jhove2.core.FormatIdentification;
import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.TimerInfo;
import org.jhove2.core.reportable.ReportableFactory;
import org.jhove2.core.source.Source;
import org.jhove2.module.AbstractCommand;

/**
 * {@link org.jhove2.module.Command} to perform identification on
 * {@link org.jhove2.core.source.Source}s.
 * Note that this module does not detect
 * {@link org.jhove2.core.source.ClumpSource} format instances. 
 * 
 * @author smorrissey
 */
public class IdentifierCommand
	extends AbstractCommand
{
	/** IdentifierCommand module version identifier. */
	public static final String VERSION = "0.5.4";

	/** IdentifierCommand module release date. */
	public static final String RELEASE = "2010-01-26";

	/** IdentifierCommand module rights statement. */
	public static final String RIGHTS = "Copyright 2010 by The Regents of the University of California, "
		+ "Ithaka Harbors, Inc., and The Board of Trustees of the Leland "
		+ "Stanford Junior University. "
		+ "Available under the terms of the BSD license.";
	
	/** Instantiate a new <code>IdentifierCommand</code>.
	 */
	public IdentifierCommand(){
		super(VERSION, RELEASE, RIGHTS, Scope.Generic);
	}

	/**
	 * Identify the presumptive formats of the source unit.
	 * @param jhove2 JHOVE2 framework object
	 * @param source Source to be identified
	 * @throws JHOVE2Exception
	 * @see org.jhove2.module.Command#execute(org.jhove2.core.JHOVE2, org.jhove2.core.source.Source)
	 * @see org.jhove2.module.identify.Identifier#identify(org.jhove2.core.JHOVE2, org.jhove2.core.source.Source)
	 */
	@Override
	public void execute(JHOVE2 jhove2, Source source)
		throws JHOVE2Exception
	{
		try {		
			Identifier identifier =
				ReportableFactory.getReportable(Identifier.class,
					                           "IdentifierModule");	
			this.addModule(identifier);
			this.addModule(identifier.getFileSourceIdentifier());
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
