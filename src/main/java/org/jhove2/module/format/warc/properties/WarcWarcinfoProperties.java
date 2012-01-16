package org.jhove2.module.format.warc.properties;

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.reportable.AbstractReportable;

import com.sleepycat.persist.model.Persistent;

@Persistent
public class WarcWarcinfoProperties extends AbstractReportable {

	protected WarcRecordData record;

	public WarcWarcinfoProperties() {
	}

	public WarcWarcinfoProperties(WarcRecordData record) {
		this.record = record;
	}

    @ReportableProperty(order = 1, value = "WarcFilename header value.")
    public String getWarcFilename() {
        return record.warcFilename;
    }

}
