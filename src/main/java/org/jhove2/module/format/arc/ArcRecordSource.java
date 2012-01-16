package org.jhove2.module.format.arc;

import org.jhove2.core.JHOVE2;
import org.jhove2.core.source.AbstractSource;

import com.sleepycat.persist.model.Persistent;

@Persistent
public class ArcRecordSource extends AbstractSource {

	/**
	 * Instantiate a new <code>ClumpSource</code>.
	 */
	protected ArcRecordSource() {
		super();
	}

    /**
     * Instantiate a new <code>ClumpSource</code>.
     * @param jhove2 JHOVE2 framework object
     */
    protected ArcRecordSource(JHOVE2 jhove2) {
        super(jhove2);
    }

}
