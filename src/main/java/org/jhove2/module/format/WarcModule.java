package org.jhove2.module.format;

import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.format.Format;
import org.jhove2.core.io.Input;
import org.jhove2.core.source.Source;
import org.jhove2.module.format.Validator.Coverage;
import org.jhove2.module.format.Validator.Validity;
import org.jhove2.persist.FormatModuleAccessor;

import com.sleepycat.persist.model.Persistent;

@Persistent
public class WarcModule extends BaseFormatModule implements Validator {

    /** Validation status. */
    private volatile Validity isValid = Validity.Undetermined;

    /** Whether to recursively characterize ARC record objects. */
    private boolean recurse = true;
    /** Thread pool size for parallel characterization of ARC records. */
    //private int nThreads = 0;

    /**
     * Instantiate a new <code>ArcModule</code> instance.
     * @param  format ARC format.
     */
    public WarcModule(Format format, 
    		FormatModuleAccessor formatModuleAccessor) {
        super(VERSION, RELEASE, RIGHTS, format, formatModuleAccessor);
		this.isValid = Validity.Undetermined;
    }

    /**
     * Instantiate a new <code>ArcModule</code> instance.
     * @throws JHOVE2Exception 
     */
    public WarcModule() {
      this(null, null);
    }

    //------------------------------------------------------------------------
    // Validator interface support
    //------------------------------------------------------------------------
	
    /**
     * Validates the ARC file.
     * @param  jhove2   the JHove2 characterization context.
     * @param  source   ARC file source unit.
     * @param  input    ARC file source input.
     */
    @Override
    public Validity validate(JHOVE2 jhove2, Source source, Input input)
                                                        throws JHOVE2Exception {
        return this.isValid();
    }

    /**
     * Gets the validation coverage.
     * @return {@link Coverage.Selective selective}, always.
     */
    @Override
    public Coverage getCoverage() {
        return Coverage.Selective;
    }

    /**
     * Gets ARC file validation status.
     * @return the {@link Validity validity status}.
     */
    @Override
    public Validity isValid() {
        return this.isValid;
    }

}
