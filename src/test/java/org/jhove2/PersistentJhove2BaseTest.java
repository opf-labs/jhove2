/**
 * 
 */
package org.jhove2;


import org.jhove2.core.JHOVE2Exception;
import org.jhove2.persist.PersistenceManager;
import org.jhove2.persist.PersistenceManagerUtil;
import org.junit.AfterClass;
import org.junit.BeforeClass;

/**
 * @author smorrissey
 *
 */
public class PersistentJhove2BaseTest {
	
	static String persistenceMgrClassName = "org.jhove2.config.spring.SpringBerkeleyDbPersistenceManagerFactory";
	static PersistenceManager persistenceManager = null;
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		PersistenceManagerUtil.createPersistenceManagerFactory(persistenceMgrClassName);
		persistenceManager = PersistenceManagerUtil.getPersistenceManagerFactory().getInstance();
		persistenceManager.initialize();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		if (persistenceManager != null){
			try{
				persistenceManager.close();
			}
			catch (JHOVE2Exception je){
				System.err.println(je.getMessage());
				je.printStackTrace(System.err);
			}
		}
	}

}
