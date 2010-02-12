/**
 * 
 */
package org.jhove2.core.reportable;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

/**
 * @author smorrissey
 *
 */
public class PropertiesFactoryBeanTest {

	/**
	 * Test method for {@link org.jhove2.core.reportable.PropertiesFactoryBean#setPropertyFileBaseName(java.lang.String)}.
	 */
	@Test
	public void testSetPropertyFileBaseName() {
		PropertiesFactoryBean bean = new PropertiesFactoryBean();
		try {
			bean.setPropertyFileBaseName("displayer");
		}
		catch (IOException e){
			fail("IO exception");
		}
	}

}
