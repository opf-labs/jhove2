package org.jhove2.module.format.gzip;

import com.sleepycat.persist.model.Persistent;

@Persistent
public abstract class Closable {

	public abstract void close();

}
