/**
 * 
 */
package org.jhove2.core;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import org.jhove2.core.I8R.Namespace;
import org.jhove2.core.source.*;
import org.jhove2.module.Module;
import org.jhove2.module.identify.GlobPathRecognizer;

/**
 * @author smorrissey
 *
 */
public class I8RTest {

	private String sourceI8rString;
	private String moduleI8rString;
	private Source source;
	private Module module;
	private I8R sourceI8r;
	private I8R moduleI8r;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		source = new FileSetSource();
		String classString = source.getClass().getCanonicalName();
		classString = classString.replace(".", "/");
		StringBuffer sb = new StringBuffer(I8R.JHOVE2_PREFIX);
		sb.append("/");
		sb.append(I8R.JHOVE2_REPORTABLE_INFIX);
		sb.append("/");
		sb.append(classString);
		sourceI8rString = sb.toString();
		module = new GlobPathRecognizer();
		classString = module.getClass().getCanonicalName();
		classString = classString.replace(".", "/");
		sb = new StringBuffer(I8R.JHOVE2_PREFIX);
		sb.append("/");
		sb.append(I8R.JHOVE2_REPORTABLE_INFIX);
		sb.append("/");
		sb.append(classString);
		moduleI8rString = sb.toString();
		sourceI8r = I8R.makeReportableI8R(source);
		moduleI8r = I8R.makeReportableI8R(module);	
	}

	/**
	 * Test method for {@link org.jhove2.core.I8R#makeReportableI8R(org.jhove2.core.Reportable)}.
	 */
	@Test
	public void testMakeReportableI8R() {		
		assertEquals(sourceI8rString, sourceI8r.getValue());
		assertEquals(moduleI8rString, moduleI8r.getValue());
	}
	/**
	 * Test method for {@link org.jhove2.core.I8R#compareTo(org.jhove2.core.I8R)}.
	 */
	@Test
	public void testcompareTo() {
		assertEquals(0,sourceI8r.compareTo(sourceI8r));
		assertEquals(0,moduleI8r.compareTo(moduleI8r));
		assertEquals(-1,sourceI8r.compareTo(moduleI8r));
		assertEquals(1,moduleI8r.compareTo(sourceI8r));
		assertEquals(1,sourceI8r.compareTo(null));
		assertEquals(1,moduleI8r.compareTo(null));
	}
	
	/**
	 * Test method for {@link org.jhove2.core.I8R#equals(java.lang.Object)}.
	 */
	@Test
	public void testEquals() {
		assertEquals(sourceI8r,sourceI8r);
	    assertFalse(sourceI8r.equals(moduleI8r));
	    assertFalse(sourceI8r.equals(null));	    
	    assertEquals(moduleI8r,moduleI8r);
	    assertFalse(moduleI8r.equals(sourceI8r));
	    assertFalse(moduleI8r.equals(null));
	    
	    I8R otherNs = new I8R("somefakename",sourceI8r.getNamespace());
	    assertEquals(otherNs, otherNs);
	    assertFalse(otherNs.equals(null));
	    assertFalse(otherNs.equals(sourceI8r));
	    assertFalse(otherNs.equals(moduleI8r));
	    otherNs =  new I8R(sourceI8r.getValue(),sourceI8r.getNamespace());
	    assertEquals(otherNs, sourceI8r);
	    assertEquals(sourceI8r, otherNs);
	}
}
