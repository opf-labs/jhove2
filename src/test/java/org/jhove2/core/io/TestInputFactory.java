package org.jhove2.core.io;

import static org.junit.Assert.fail;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.jhove2.core.io.Input.Type;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class TestInputFactory {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	@Test
	public void testGetInputInputStreamIntType() {
		URL yahoo = null;
		try {
			yahoo = new URL("http://www.yahoo.com/");
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			Input input = InputFactory.getInput(yahoo.openStream(), 8192, Type.Direct);
			input.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		fail("Not yet implemented"); // TODO
	}
	
	@Ignore
	@Test
	public void testGetInputInputStreamIntTypeByteOrder() {
		fail("Not yet implemented"); // TODO
	}

}
