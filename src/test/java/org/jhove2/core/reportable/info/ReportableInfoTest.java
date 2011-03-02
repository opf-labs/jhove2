/**
 * 
 */
package org.jhove2.core.reportable.info;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.jhove2.core.JHOVE2;
import org.jhove2.core.source.Source;
import org.jhove2.core.source.SourceFactory;
import org.jhove2.persist.inmemory.InMemorySourceFactory;
import org.junit.Test;

/**
 * @author Sheila Morrissey
 *
 */
public class ReportableInfoTest {
	private Source source;
	/**
	 * Test method for {@link org.jhove2.core.reportable.info.ReportableInfo#ReportableInfo(org.jhove2.core.Reportable)}.
	 */
	@Test
	public void testReportableInfoReportable() {
		
	}

	/**
	 * Test method for {@link org.jhove2.core.reportable.info.ReportableInfo#ReportableInfo(java.lang.Class)}.
	 */
	@Test
	public void testReportableInfoClassOfQextendsReportable() {
		
	}

	/**
	 * Test method for {@link org.jhove2.core.info.ReportableInfo#checkInterfaces(java.lang.Class<?>[], java.util.Map, org.jhove2.core.info.ReportablePropertyComparator)}.
	 */
	@Test
	public void testCheckInterfaces() {
		
	}

	/**
	 * Test method for {@link org.jhove2.core.reportable.info.ReportableInfo#getIdentifier()}.
	 */
	@Test
	public void testGetIdentifier() {
//		source = new FileSetSource();
		SourceFactory factory = new InMemorySourceFactory();
		try {
		    JHOVE2 jhove2 = new JHOVE2();
	        jhove2.setSourceFactory(factory);
			source = factory.getSource(jhove2, "");
		} catch (Exception e) {
			fail("unable to create Source");
		}

		ReportableInfo reportableInfo = new ReportableInfo(source);
		assertEquals("JHOVE2", reportableInfo.getIdentifier().getNamespace().toString());
	}

}
