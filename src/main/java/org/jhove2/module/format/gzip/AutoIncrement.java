package org.jhove2.module.format.gzip;

public class AutoIncrement {

	public int index;

	public AutoIncrement() {
		index = 0;
	}

	public synchronized int get() {
		return ++index;
	}

}
