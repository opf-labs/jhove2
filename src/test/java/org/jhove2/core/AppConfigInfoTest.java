/**
 * 
 */
package org.jhove2.core;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * @author smorrissey
 *
 */
public class AppConfigInfoTest {

	private Invocation config;
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		config = new Invocation();
	}

//	/**
//	 * Test method for {@link org.jhove2.core.Invocation#getJHOVE2Home()}.
//	 */
//	@Test
//	public void testGetJhove2Home() {
//		// test assumes we have  set JHOVE2 HOME env variable
//		String sep = System.getProperty("file.separator");
//		assertEquals(config.getWorkingDirectory().concat(sep).
//				concat("target").concat(sep).concat("classes"),
//				config.getJHOVE2Home());
//	}

}
