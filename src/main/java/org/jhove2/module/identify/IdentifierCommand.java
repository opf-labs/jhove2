/**
 * 
 */
package org.jhove2.module.identify;

import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Command;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.config.Configure;
import org.jhove2.core.source.Source;
import org.jhove2.module.AbstractModule;

/**
 * JHOVE2Command to execute identification on Sources
 * Does not detect Clump format instances. 
 * 
 * @author smorrissey
 *
 */
public class IdentifierCommand extends AbstractModule implements JHOVE2Command {

	/** IdentifierCommand module version identifier. */
	public static final String VERSION = "1.0.0";

	/** IdentifierCommand module release date. */
	public static final String RELEASE = "2009-09-09";

	/** IdentifierCommand module rights statement. */
	public static final String RIGHTS = "Copyright 2009 by The Regents of the University of California, "
		+ "Ithaka Harbors, Inc., and The Board of Trustees of the Leland "
		+ "Stanford Junior University. "
		+ "Available under the terms of the BSD license.";
	
	/**
	 * Constructor
	 */
	public IdentifierCommand(){
		this(VERSION, RELEASE, RIGHTS);
	}
	
	/**
	 * Constructor
	 * @param version of this module	
	 * @param release of this module	
	 * @param rights of this module	
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
	 * @see org.jhove2.core.JHOVE2Command#execute(org.jhove2.core.source.Source, org.jhove2.core.JHOVE2)
	 * @see org.jhove2.module.identify.Identifier#identify(org.jhove2.core.JHOVE2, org.jhove2.core.source.Source)
	 */
	@Override
	public void execute(Source source, JHOVE2 jhove2) throws JHOVE2Exception {	
		source.addModule(this);
		try {		
			Identifier identifier = 
				Configure.getReportable(Identifier.class, "Identifier");
			source.addModule(identifier);
			identifier.getTimerInfo().setStartTime();
			source.addPresumptiveFormatIdentifications(
					identifier.identify( jhove2, source));	
			identifier.getTimerInfo().setEndTime();						
		}
		catch (Exception e){
			throw new JHOVE2Exception("failed to execute identification",
					e);
		}	
        return;
	}

}
