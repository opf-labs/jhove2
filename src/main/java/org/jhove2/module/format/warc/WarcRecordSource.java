package org.jhove2.module.format.warc;

import org.jhove2.core.JHOVE2;
import org.jhove2.core.source.AbstractSource;

import com.sleepycat.persist.model.Persistent;

@Persistent
public class WarcRecordSource extends AbstractSource {

	/**
	 * Instantiate a new <code>ClumpSource</code>.
	 */
	protected WarcRecordSource() {
		super();
	}

    /**
     * Instantiate a new <code>ClumpSource</code>.
     * @param jhove2 JHOVE2 framework object
     */
    protected WarcRecordSource(JHOVE2 jhove2) {
        super(jhove2);
    }

}
