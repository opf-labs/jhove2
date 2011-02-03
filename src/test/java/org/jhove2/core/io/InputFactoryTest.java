package org.jhove2.core.io;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.source.URLSource;
import org.jhove2.persist.inmemory.InMemorySourceFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class InputFactoryTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetInputInputStreamIntType() throws JHOVE2Exception {
		URL yahoo = null;
		try {
			yahoo = new URL("http://www.yahoo.com/");
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		}
		try {
			JHOVE2 jhove2 = new JHOVE2();
			InMemorySourceFactory sourceFactory = new InMemorySourceFactory();
			jhove2.setSourceFactory(sourceFactory);
			URLSource yahooURL = (URLSource)jhove2.getSourceFactory().getSource
											  (jhove2, yahoo);
			Input input = yahooURL.getInput(jhove2);
			File file = yahooURL.getFile();
			assertTrue("File doesn't exist", file.exists());
            // this closes the channel and the stream associated with any temp
            // file created
			// this invokes file.delete() on any temp file created
            input.close();
			yahooURL.close();
			file = yahooURL.getFile();
			assertTrue("File still exists - should be deleted!", !file.exists());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
