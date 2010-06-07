/**
 * JHOVE2 - Next-generation architecture for format-aware characterization
 * <p>
 * Copyright (c) 2010 by The Regents of the University of California, Ithaka
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
package org.jhove2.util.externalprocess;

import java.util.ArrayList;

import org.jhove2.core.JHOVE2Exception;


/**
 * This class is used to create a child process using Java's Runtime API, which actually
 * invokes fork() underneath. The child process will run a shell.
 * 
 * This class is base on one developed for similar purposes at Portico by Suresh Kadirvel.
 * 
 * @author smorrissey
 *
 */
public class ForkShellHandler implements ExternalProcessHandler {
	

	/** the full path to the shell command */	
	protected String shellEnv = null;  
	/** params passed to shell before actual process t be executed */
	protected String shellParms = null;
	/** indicates if command string should be quoted when shell is invoked */
	protected boolean shouldQuoteCommand = true;

	/**
	 * This function is invoked whenever a child process need to be created using Java Runtime API 
	 * The input command should be a valid shell command with/without directing the standard output/error
	 * into filesystem as this function returns only success/failure status of execution. Output/Error should
	 * be retrieved from the filesystem directly.
	 * 
	 */
	public void executeCommand(String command) throws JHOVE2Exception {
		
		try {
			StringBuffer sbCommand = new StringBuffer();
			if (shouldQuoteCommand){
				sbCommand.append('"');
			}
			sbCommand.append(command);
			/* Append "sync" command to flush the stdout,stderr*/
			if(SYNC_AFTER_EXECUTION){
				sbCommand.append(SYNC_COMMAND_WITH_DELIMITER );
			}			
			if (shouldQuoteCommand){
				sbCommand.append('"');
			}			
     		String commandString = sbCommand.toString();
     	    ArrayList<String> cmds = new ArrayList<String>();
     	    String[] cmdArray ={};
     	    if (shellEnv != null){
     	    	cmds.add(shellEnv);
     	    }
     	    if (shellParms != null){
     	    	cmds.add(shellParms);
     	    }
     	    cmds.add(commandString);
			cmdArray = cmds.toArray(cmdArray);
			Runtime currentRuntime = Runtime.getRuntime();
	        Process openProcess = currentRuntime.exec(cmdArray);
	        openProcess.waitFor();        
		} catch (Exception exception) {
			throw new JHOVE2Exception("Exception thrown executing command " + command,
					exception);
		}
	}

	/**
	 * @return the shellEnv
	 */
	public String getShellEnv() {
		return shellEnv;
	}

	/**
	 * @param shellEnv the shellEnv to set
	 */
	public void setShellEnv(String shellEnv) {
		this.shellEnv = shellEnv;
	}

	/**
	 * @return the shellParms
	 */
	public String getShellParms() {
		return shellParms;
	}

	/**
	 * @param shellParms the shellParms to set
	 */
	public void setShellParms(String shellParms) {
		this.shellParms = shellParms;
	}

	/**
	 * @return the shouldQuoteCommand
	 */
	public boolean isShouldQuoteCommand() {
		return shouldQuoteCommand;
	}

	/**
	 * @param shouldQuoteCommand the shouldQuoteCommand to set
	 */
	public void setShouldQuoteCommand(boolean shouldQuoteCommand) {
		this.shouldQuoteCommand = shouldQuoteCommand;
	}

}
