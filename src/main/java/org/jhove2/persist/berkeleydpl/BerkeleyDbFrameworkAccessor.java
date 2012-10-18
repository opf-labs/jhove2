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
package org.jhove2.persist.berkeleydpl;

import java.util.ArrayList;
import java.util.List;

import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.module.AbstractCommand;
import org.jhove2.module.AbstractModule;
import org.jhove2.module.Command;
import org.jhove2.persist.FrameworkAccessor;

import com.sleepycat.je.DatabaseException;
import com.sleepycat.persist.EntityCursor;
import com.sleepycat.persist.EntityIndex;
import com.sleepycat.persist.model.Persistent;

/**
 * @author smorrissey
 *
 */
@Persistent
public class BerkeleyDbFrameworkAccessor extends BerkeleyDbBaseModuleAccessor
		implements FrameworkAccessor {

	/**
	 * Constructor
	 */
	public BerkeleyDbFrameworkAccessor(){
		super();
	}
	
	/* (non-Javadoc)
	 * @see org.jhove2.persist.FrameworkAccessor#getCommands(org.jhove2.core.JHOVE2)
	 */
	@Override
	public List<Command> getCommands(JHOVE2 jhove2) throws JHOVE2Exception {
		List<Command> commands = new ArrayList<Command>();
		if (jhove2 != null){
			EntityIndex<Long, AbstractCommand> subIndex = null;
			EntityCursor<AbstractCommand> cursor = null;
			try{
				if (jhove2.getModuleId()== null){
					this.persistModule(jhove2);
				}
				subIndex = 
					this.getBerkeleyDbPersistenceManager().
						getCommandByParentFramework().subIndex(jhove2.getModuleId());
				cursor = subIndex.entities();
				for (AbstractModule command:cursor){
					commands.add((Command)command);
				}
			}
			catch (DatabaseException e){
				throw new JHOVE2Exception ("Unable to get Commands " , e);
			}
			finally{
				if (cursor != null){
					try{
						cursor.close();
					}
					catch (DatabaseException e){
						throw new JHOVE2Exception ("Unable to close cursor", e);
					}
				}
			}
		}
		return commands;
	}

	/* (non-Javadoc)
	 * @see org.jhove2.persist.FrameworkAccessor#setCommands(org.jhove2.core.JHOVE2, java.util.List)
	 */
	@Override
	public List<Command> setCommands(JHOVE2 jhove2, List<Command> commands)
			throws JHOVE2Exception {
		List<Command> childCommands = new ArrayList<Command>();;
		if (jhove2 != null){			
			try{
				if (jhove2.getModuleId()==null){
					this.persistModule(jhove2);
				}
				//Un-link any old commands
				List<Command> oldCommands = this.getCommands(jhove2);
				if (oldCommands != null){
					for (Command command:oldCommands){
						this.deleteCommand(jhove2, command);
					}
				}
				if (commands != null){
					for (Command command:commands){
						this.addCommand(jhove2, command);
						childCommands.add(command);
					}
				}
			}
			catch (DatabaseException e){
				throw new JHOVE2Exception ("Unable to set Commands " , e);
			}
		}
		return childCommands;
	}

	@Override
	public Command addCommand(JHOVE2 jhove2, Command command)
			throws JHOVE2Exception {
		if (jhove2 != null && command != null){
			if (jhove2.getModuleId()==null){
				this.persistModule(jhove2);
			}
			command.setJhove2ModuleId(jhove2.getModuleId());		
		}
		return command;
	}

	@Override
	public Command deleteCommand(JHOVE2 jhove2, Command command)
			throws JHOVE2Exception {
		if (jhove2 != null && command != null){
			if (jhove2.getModuleId()==null){
				this.persistModule(jhove2);
			}
			command.setJhove2ModuleId(null);	
		}
		return command;
	}

}
