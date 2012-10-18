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

import java.io.File;

import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.app.AbstractApplication;
import org.jhove2.core.source.AbstractSource;
import org.jhove2.module.AbstractCommand;
import org.jhove2.module.AbstractModule;
import org.jhove2.module.aggrefy.AbstractRecognizer;
import org.jhove2.module.aggrefy.AggrefierModule;
import org.jhove2.module.display.AbstractDisplayer;
import org.jhove2.module.format.AbstractFormatProfile;
import org.jhove2.module.format.BaseFormatModule;
import org.jhove2.module.identify.AbstractFileSourceIdentifier;
import org.jhove2.module.identify.DROIDIdentifier;
import org.jhove2.module.identify.IdentifierModule;
import org.jhove2.persist.PersistenceManager;
import org.jhove2.persist.berkeleydpl.proxies.*;

import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.persist.EntityStore;
import com.sleepycat.persist.PrimaryIndex;
import com.sleepycat.persist.SecondaryIndex;
import com.sleepycat.persist.StoreConfig;
import com.sleepycat.persist.model.AnnotationModel;
import com.sleepycat.persist.model.EntityModel;

/**
 * Persistence manager implemented using BerkeleyDB JE Direct Persistence Layer (DPL)
 * @author smorrissey
 *
 */
public class BerkeleyDbPersistenceManager implements PersistenceManager {

	protected EnvironmentConfig envConfig;
	protected StoreConfig storeConfig;
	protected String envHome;
	protected String entityStoreName;
	
	protected Environment env;
	protected EntityStore store;
	
	protected PrimaryIndex<Long, AbstractSource> sourceBySourceId;
	protected SecondaryIndex<Long, Long, AbstractSource>  sourceByParentSource;
	
	protected PrimaryIndex<Long, AbstractModule> moduleByModuleId;
	protected SecondaryIndex<Long, Long, AbstractModule> moduleByParentSource;
	protected SecondaryIndex<Long, Long, AbstractDisplayer> displayerByParentApp;
	protected SecondaryIndex<Long, Long, AbstractFormatProfile> formatProfileByParentFormatModule;
	protected SecondaryIndex<Long, Long, AbstractCommand> commandByParentFramework;
	protected SecondaryIndex<Long, Long, AbstractRecognizer> recognizerByParentAggrefier;
	protected SecondaryIndex<Long, Long, AbstractFileSourceIdentifier> fileSourceIdentifierByParentIdentifier;
		
	/**
	 * Constructor
	 */
	public BerkeleyDbPersistenceManager() {
		super();
	}


	@Override
	public void initialize() throws JHOVE2Exception {
		File envHomeFile = new File(envHome);
		boolean exists = envHomeFile.exists();
		if (!exists){
			boolean couldMakeDirs = envHomeFile.mkdirs();
			if (!couldMakeDirs){
				throw new JHOVE2Exception("Could not create BerkeleyDB env home path "+
						envHome);
			}
		}
		boolean isDir = envHomeFile.isDirectory(); 
		if (!isDir){
			throw new JHOVE2Exception("BerkeleyDB env home path " +
					envHome + " is not a directory");
		}
		try{
			env = new Environment(envHomeFile, envConfig);			
			EntityModel model = new AnnotationModel();
			
			// register sub-classes for indexes
			model.registerClass(AbstractSource.class);
			model.registerClass(AbstractModule.class);
			model.registerClass(AbstractDisplayer.class);
			model.registerClass(AbstractApplication.class);
			model.registerClass(BaseFormatModule.class);
			model.registerClass(AbstractFormatProfile.class);
			model.registerClass(JHOVE2.class);
			model.registerClass(AggrefierModule.class);
			model.registerClass(AbstractRecognizer.class);
			model.registerClass(AbstractCommand.class);
			model.registerClass(IdentifierModule.class);
			model.registerClass(DROIDIdentifier.class);
			model.registerClass(AbstractFileSourceIdentifier.class);
		
			//register proxies
			model.registerClass(LocalePersisentProxy.class);
			model.registerClass(PatternPersistentProxy.class);
			model.registerClass(LinkedHashSetPersistentProxy.class);
			model.registerClass(FilePersistentProxy.class);
			model.registerClass(ByteOrderPersistentProxy.class);
			model.registerClass(LinkedHashMapPersistentProxy.class);
			model.registerClass(URLPersistentProxy.class);
			model.registerClass(PropertiesPersistentProxy.class);
			model.registerClass(StringBufferPersistentProxy.class);
			
			storeConfig.setModel(model);					
			storeConfig.setAllowCreate(envConfig.getAllowCreate());
			storeConfig.setTransactional(envConfig.getTransactional());
			store = new EntityStore(env,entityStoreName,storeConfig);
				
			// create indexes for Source entities
			sourceBySourceId = 
				this.getStore().getPrimaryIndex(Long.class, AbstractSource.class);			
			sourceByParentSource = 
				this.getStore().getSecondaryIndex(sourceBySourceId, Long.class, "parentSourceId");
			// create indexes for Module entities
			moduleByModuleId = 
				this.getStore().getPrimaryIndex(Long.class, AbstractModule.class);
			moduleByParentSource =
				this.getStore().getSecondaryIndex(moduleByModuleId, Long.class, "moduleParentSourceId");
			displayerByParentApp =
				this.getStore().getSubclassIndex(moduleByModuleId, AbstractDisplayer.class, Long.class, "parentAppId");
			formatProfileByParentFormatModule =
				this.getStore().getSubclassIndex(moduleByModuleId, AbstractFormatProfile.class, Long.class,"formatModuleId");
			commandByParentFramework =
				this.getStore().getSubclassIndex(moduleByModuleId, AbstractCommand.class, Long.class,"jhove2ModuleId");
			recognizerByParentAggrefier =
				this.getStore().getSubclassIndex(moduleByModuleId, AbstractRecognizer.class, Long.class, "parentAggrefierId");
			fileSourceIdentifierByParentIdentifier =
				this.getStore().getSubclassIndex(moduleByModuleId, AbstractFileSourceIdentifier.class, Long.class, "parentIdentifierId");
		}
		catch (Exception e){
			throw new JHOVE2Exception ("Cannot initialize Berkeley DB environment", e);
		}
	}

	@Override
	public void close() throws JHOVE2Exception {
		if (this.getStore()!= null){
			try {
				this.getStore().close();
			}
			catch(DatabaseException e){
				throw new JHOVE2Exception("Unable to close database Store",e);			
			}
		}
		if (this.getEnv()!= null){
			try{
				this.getEnv().close();
			}
			catch(DatabaseException e){
				throw new JHOVE2Exception("Unable to close database Environment",e);			
			}
		}
		return;
	}

	
	/**
	 * @return the envConfig
	 */
	public EnvironmentConfig getEnvConfig() {
		return envConfig;
	}

	/**
	 * @param envConfig the envConfig to set
	 */
	public void setEnvConfig(EnvironmentConfig envConfig) {
		this.envConfig = envConfig;
	}

	/**
	 * @return the storeConfig
	 */
	public StoreConfig getStoreConfig() {
		return storeConfig;
	}

	/**
	 * @return the envHome
	 */
	public String getEnvHome() {
		return envHome;
	}

	/**
	 * @return the env
	 */
	public Environment getEnv() {
		return env;
	}

	/**
	 * @return the store
	 */
	public EntityStore getStore() {
		return store;
	}

	/**
	 * @param storeConfig the storeConfig to set
	 */
	public void setStoreConfig(StoreConfig storeConfig) {
		this.storeConfig = storeConfig;
	}

	/**
	 * @param envHome the envHome to set
	 */
	public void setEnvHome(String envHome) {
		this.envHome = envHome;
	}

	/**
	 * @param env the env to set
	 */
	public void setEnv(Environment env) {
		this.env = env;
	}

	/**
	 * @param store the store to set
	 */
	public void setStore(EntityStore store) {
		this.store = store;
	}


	/**
	 * @return the entityStoreName
	 */
	public String getEntityStoreName() {
		return entityStoreName;
	}


	/**
	 * @param entityStoreName the entityStoreName to set
	 */
	public void setEntityStoreName(String entityStoreName) {
		this.entityStoreName = entityStoreName;
	}


	/**
	 * @return the sourceBySourceId
	 */
	public PrimaryIndex<Long, AbstractSource> getSourceBySourceId() {
		return sourceBySourceId;
	}


	/**
	 * @return the sourceByParentSource
	 */
	public SecondaryIndex<Long, Long, AbstractSource> getSourceByParentSource() {
		return sourceByParentSource;
	}


	/**
	 * @return the moduleByModuleId
	 */
	public PrimaryIndex<Long, AbstractModule> getModuleByModuleId() {
		return moduleByModuleId;
	}


	/**
	 * @return the moduleByParentSource
	 */
	public SecondaryIndex<Long, Long, AbstractModule> getModuleByParentSource() {
		return moduleByParentSource;
	}


	/**
	 * @return the displayerByParentApp
	 */
	public SecondaryIndex<Long, Long, AbstractDisplayer> getDisplayerByParentApp() {
		return displayerByParentApp;
	}


	/**
	 * @return the formatProfileByParentFormatModule
	 */
	public SecondaryIndex<Long, Long, AbstractFormatProfile> getFormatProfileByParentFormatModule() {
		return formatProfileByParentFormatModule;
	}


	/**
	 * @return the commandByParentFramework
	 */
	public SecondaryIndex<Long, Long, AbstractCommand> getCommandByParentFramework() {
		return commandByParentFramework;
	}


	/**
	 * @return the recognizerByParentAggrefier
	 */
	public SecondaryIndex<Long, Long, AbstractRecognizer> getRecognizerByParentAggrefier() {
		return recognizerByParentAggrefier;
	}


	/**
	 * @param sourceBySourceId the sourceBySourceId to set
	 */
	public void setSourceBySourceId(
			PrimaryIndex<Long, AbstractSource> sourceBySourceId) {
		this.sourceBySourceId = sourceBySourceId;
	}


	/**
	 * @param sourceByParentSource the sourceByParentSource to set
	 */
	public void setSourceByParentSource(
			SecondaryIndex<Long, Long, AbstractSource> sourceByParentSource) {
		this.sourceByParentSource = sourceByParentSource;
	}


	/**
	 * @param moduleByModuleId the moduleByModuleId to set
	 */
	public void setModuleByModuleId(
			PrimaryIndex<Long, AbstractModule> moduleByModuleId) {
		this.moduleByModuleId = moduleByModuleId;
	}


	/**
	 * @param moduleByParentSource the moduleByParentSource to set
	 */
	public void setModuleByParentSource(
			SecondaryIndex<Long, Long, AbstractModule> moduleByParentSource) {
		this.moduleByParentSource = moduleByParentSource;
	}


	/**
	 * @param displayerByParentApp the displayerByParentApp to set
	 */
	public void setDisplayerByParentApp(
			SecondaryIndex<Long, Long, AbstractDisplayer> displayerByParentApp) {
		this.displayerByParentApp = displayerByParentApp;
	}


	/**
	 * @param formatProfileByParentFormatModule the formatProfileByParentFormatModule to set
	 */
	public void setFormatProfileByParentFormatModule(
			SecondaryIndex<Long, Long, AbstractFormatProfile> formatProfileByParentFormatModule) {
		this.formatProfileByParentFormatModule = formatProfileByParentFormatModule;
	}


	/**
	 * @param commandByParentFramework the commandByParentFramework to set
	 */
	public void setCommandByParentFramework(
			SecondaryIndex<Long, Long, AbstractCommand> commandByParentFramework) {
		this.commandByParentFramework = commandByParentFramework;
	}


	/**
	 * @param recognizerByParentAggrefier the recognizerByParentAggrefier to set
	 */
	public void setRecognizerByParentAggrefier(
			SecondaryIndex<Long, Long, AbstractRecognizer> recognizerByParentAggrefier) {
		this.recognizerByParentAggrefier = recognizerByParentAggrefier;
	}


	/**
	 * @return the fileSourceIdentifierByParentIdentifier
	 */
	public SecondaryIndex<Long, Long, AbstractFileSourceIdentifier> getFileSourceIdentifierByParentIdentifier() {
		return fileSourceIdentifierByParentIdentifier;
	}


	/**
	 * @param fileSourceIdentifierByParentIdentifier the fileSourceIdentifierByParentIdentifier to set
	 */
	public void setFileSourceIdentifierByParentIdentifier(
			SecondaryIndex<Long, Long, AbstractFileSourceIdentifier> fileSourceIdentifierByParentIdentifier) {
		this.fileSourceIdentifierByParentIdentifier = fileSourceIdentifierByParentIdentifier;
	}

}
