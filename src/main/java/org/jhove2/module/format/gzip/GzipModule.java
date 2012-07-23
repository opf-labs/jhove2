/**
 * JHOVE2 - Next-generation architecture for format-aware characterization
 *
 * Copyright (c) 2009 by The Regents of the University of California,
 * Ithaka Harbors, Inc., and The Board of Trustees of the Leland Stanford
 * Junior University.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * o Redistributions of source code must retain the above copyright notice,
 *   this list of conditions and the following disclaimer.
 *
 * o Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * o Neither the name of the University of California/California Digital
 *   Library, Ithaka Harbors/Portico, or Stanford University, nor the names of
 *   its contributors may be used to endorse or promote products derived from
 *   this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package org.jhove2.module.format.gzip;

import java.io.BufferedInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteOrder;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.Invocation;
import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.Message;
import org.jhove2.core.TimerInfo;
import org.jhove2.core.format.Format;
import org.jhove2.core.format.FormatIdentification;
import org.jhove2.core.io.Input;
import org.jhove2.core.source.Source;
import org.jhove2.core.source.SourceFactory;
import org.jhove2.module.format.BaseFormatModule;
import org.jhove2.module.format.Parser;
import org.jhove2.module.format.Validator;
import org.jhove2.persist.FormatModuleAccessor;
import org.jwat.arc.ArcReader;
import org.jwat.warc.WarcReader;

import com.sleepycat.persist.model.NotPersistent;
import com.sleepycat.persist.model.Persistent;

/**
 * JHOVE2 GZip module.  This module parses and validates GZip files
 * in compliance with
 * <a href="http://www.ietf.org/rfc/rfc1952.txt">RFC 1952</a> (GZIP
 * file format specification version 4.3) and supports multiple member
 * GZIP files.</p>
 *
 * @author lbihanic, selghissassi, nicl
 */
@Persistent
public class GzipModule extends BaseFormatModule implements Validator {

    /** Module version identifier. */
    public final static String VERSION = "0.8.0";

    /** Module release date. */
    public final static String RELEASE = "2011-01-20";

    /** Module rights statement. */
    /*
    public final static String RIGHTS =
        "Copyright 2010 by The Royal Library in Denmark. " +
        "Available under the terms of the BSD license.";
    */

    /** Module validation coverage. */
    public static final Coverage COVERAGE = Coverage.Selective;

    /** Validation status. */
    private Validity isValid;

    /** The name of the ARC file. */
    private String gzipFileName;

    /** The size of the ARC file, in bytes. */
    private long gzipFileSize;

    /** Number of members compressed with the deflate compression method. */
    private long deflateMemberCount = 0;

    /** The amount of bytes consumed by the GZipReader- */
    private long gzipReaderConsumedBytes;

    /** Number of non-valid members. */
    private long invalidMembers = 0;

    /** Validation error messages. */
    //private final Collection<Message> validationMessages =
    //                                    new ConcurrentLinkedQueue<Message>();
    private final Collection<Message> validationMessages =
                                        new LinkedList<Message>();

    /** A parser object to decode woven compressed formats. */
    private Parser wovenFormatParser = null;
    /** Whether to recursively characterize GZip members. */
    private boolean recurse = true;

    /** Thread pool size for parallel characterization of GZip member. */
    //private int nThreads = 0;

    /**
     * GZip instance id to GZipModule lookup <code>Map</code>.
     * Used by ARC/WARC modules to access the ACTUAL parent GZipModule instead
     * of only getting access to a new instance populated with persisted data.
     */
    @NotPersistent
    public static final transient Map<Integer, GzipModule> gzipMap = new TreeMap<Integer, GzipModule>();

    /** Used to generate a unique id for each file parsed. */
    @NotPersistent
    public static final transient AutoIncrement autoIncId = new AutoIncrement();

    /** Id used by this instance of the module. */
    public Integer instanceId;

    /**
     * ARC/WARC reader set by a child ARC/WARC module in order to use the same
     * reader for all the entries in the same GZip file.
     */
    @NotPersistent
    public transient Object reader;

    /**
     * Presumptive format used to identify subsequent ARC/WARC records which are
     * not identified by the identifier module.
     */
    @NotPersistent
    public transient FormatIdentification presumptiveFormat;

    /**
     * Instantiate a new <code>ZipModule</code>.
     * This constructor is used by the Spring framework.
     * @param format Gzip format
     * @param formatModuleAccessor FormatModuleAccessor to manage access to Format Profiles
     */
    public GzipModule(Format format,
            FormatModuleAccessor formatModuleAccessor) {
        super(VERSION, RELEASE, RIGHTS, format, formatModuleAccessor);
        isValid = Validity.Undetermined;
    }

    /**
     * Instantiate a new <code>ArcModule</code> instance.
     * This constructor is used by the persistence layer.
     */
    public GzipModule() {
        this(null, null);
    }

    //------------------------------------------------------------------------
    // BaseFormatModule contract support
    //------------------------------------------------------------------------

    @Override
    public long parse(final JHOVE2 jhove2, Source source, Input input)
        throws EOFException, IOException, JHOVE2Exception
    {
        long consumed = 0L;

        Invocation cfg = jhove2.getInvocation();

        /*
        // Check for parallel characterization mode.
        ExecutorService threadPool = null;
        if (this.nThreads > 1) {
            threadPool = Executors.newFixedThreadPool(this.nThreads);
        }
        */

        // Reset state.
        //this.deflateMemberCount.set(0L);
        //this.invalidMembers.set(0L);
        deflateMemberCount = 0L;
        invalidMembers = 0L;
        validationMessages.clear();
        isValid = Validity.Undetermined;
        wovenFormatParser = null;
        final boolean doRecurse = recurse;

        // In GZip format, least-significant bytes come first.
        input.setByteOrder(ByteOrder.LITTLE_ENDIAN);
        // Characterize each GZip member from the source, validating
        // the corresponding GZip headers and trailers.
        GzipInputStream gz = new GzipInputStream(
                    new BufferedInputStream(source.getInputStream(), 8192));

        instanceId = autoIncId.get();
        // This is done because it is not persisted immediately.
        // It is needed in recursive calls and not when the gzip module exits.
        // Each time jhove2 looks up an existing module it actually
        // instantiates a new class and loads the persisted values.
        // So a version with the correct instanceId exists on the call stack
        // but every time someone requests it a new one is created and
        // populated with persisted data. Epic fail!
        getModuleAccessor().persistModule(this);
        synchronized (gzipMap) {
            gzipMap.put(instanceId, this);
        }

        try {
            source.setIsAggregate(true);
            SourceFactory factory = jhove2.getSourceFactory();

            GzipEntryProperties e = null;
            int memberCount = 0;
            while ((e = gz.getNextEntry()) != null) {
                // Wrap found member in a JHove2 Source object.
                /*
                final GzipMemberSource src = (GzipMemberSource)
                        (SourceFactory.getSource(cfg.getTempPrefix(),
                            cfg.getTempSuffix(), cfg.getBufferSize(), e,
                            (doRecurse)? gz.getEntryInputStream(): null));
                */
                InputStream stream = gz.getEntryInputStream();
                String name = e.getName();
                final Source src =
                    factory.getSource(jhove2, stream, name, e);
                if (src != null) {
                    memberCount++;
                    // Attach member to parent source.
                    source.addChildSource(src);

                    if (presumptiveFormat != null) {
                        src.addPresumptiveFormat(presumptiveFormat);
                    }

                    if (doRecurse) {
                        // Characterize member data.
                        if (memberCount == 1) {
                            // First member: Check for woven format.
                            // Set parent module.
                            //src.setParentModule(this);
                            // Characterize member content.
                            characterizeMember(jhove2, src);
                        }
                        else {
                            /*
                            // All members but the first: characterize content.
                            if (threadPool != null) {
                                // Submit to thread pool for asynchronous
                                // parallel execution.
                                final long offset = gz.getOffset();
                                threadPool.execute(new Runnable() {
                                    public void run() {
                                        try {
                                            characterizeMember(jhove2, src);
                                        }
                                        catch (Exception e) {
                                            handleError(e, jhove2, offset);
                                        }
                                    }
                                });
                                // Let executor threads a chance to run...
                                // Thread.yield();
                            }
                            else {
                            */
                                // Sync. characterization in current thread.
                                characterizeMember(jhove2, src);
                            /*
                            }
                            */
                        }
                    }
                    // Check member compression method (always deflate).
                    if (e.getCompressionMethod().getValue() ==
                                                    GzipInputStream.DEFLATE) {
                        //this.deflateMemberCount.incrementAndGet();
                        ++deflateMemberCount;
                    }
                    // Check member validity.
                    if (! e.isValid()) {
                        //this.invalidMembers.incrementAndGet();
                        ++invalidMembers;
                        isValid = Validity.False;
                        // Report errors on child source object.
                        reportValidationErrors(e, src, jhove2);
                    }
                }
            }
            if (isValid == Validity.Undetermined) {
                // No invalid members found and EOF reached without
                // any exception being thrown => Source is valid.
                isValid = Validity.True;
            }
        }
        catch (IOException e) {
            handleError(e, jhove2, gz.getOffset());
            if (! ((e instanceof EOFException) && (gz.getOffset() != 0L))) {
                // Not an EOF error occurring before the very first entry.
                throw e;
            }
        }
        finally {
            // Close GZip input stream.
            try {
                gz.close();
            }
            catch (Exception e) { /* Ignore... */ }

            /*
            // Shutdown thread pool (if any).
            if (threadPool != null) {
                threadPool.shutdown();
                // Wait for completion of all characterization tasks.
                boolean shutdownComplete = false;
                do {
                    try {
                        threadPool.awaitTermination(2L, TimeUnit.HOURS);
                        shutdownComplete = true;
                    }
                    catch (InterruptedException e) { /* Ignore... */ /*}
                }
                while (! shutdownComplete);
            }
            */
        }
        /*
         * Cleanup.
         */
        if (reader != null) {
            if (reader instanceof ArcReader) {
                ((ArcReader)reader).close();
            }
            else if (reader instanceof WarcReader) {
                ((WarcReader)reader).close();
            }
        }
        synchronized (gzipMap) {
            gzipMap.remove(instanceId);
        }
        /*
         * Consumed.
         */
        return consumed;
    }

    private void characterizeMember(JHOVE2 jhove2, Source source)
            throws JHOVE2Exception, IOException {
        Input input = source.getInput(jhove2);
        try {
            if (wovenFormatParser != null) {
                // Start timer.
                TimerInfo timer = source.getTimerInfo();
                timer.setStartTime();
                try {
                    // Update statistics.
                    jhove2.getSourceCounter().incrementSourceCounter(source);
                    // Configure temporary files deletion.
                    source.setDeleteTempFileOnClose(jhove2.getInvocation()
                            .getDeleteTempFilesOnClose());
                    // Woven format => Delegate content handling.
                    wovenFormatParser.parse(jhove2, source, input);
                }
                finally {
                    // Delete temp. files and compute processing duration.
                    source.close();
                    timer.setEndTime();
                }
            }
            else {
                // Directly characterize content.
                jhove2.characterize(source, input);
            }
        }
        finally {
            // Make sure all file descriptors are properly closed.
            if (input != null) {
                input.close();
            }
        }
    }

    private void handleError(Exception e, JHOVE2 jhove2, long offset) {
        try {
            isValid = Validity.False;
            validationMessages.add(newValidityError(jhove2,
                    "invalidGzipFile", Long.valueOf(offset), e));
        }
        catch (JHOVE2Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    //------------------------------------------------------------------------
    // Validator interface support
    //------------------------------------------------------------------------

    /** Validate the Gzip file.
     * @param jhove2 JHOVE2 framework object
     * @param source Gzip file source unit
     * @param input  Gzip file source input
     * @see org.jhove2.module.format.Validator#validate(org.jhove2.core.JHOVE2, org.jhove2.core.source.Source, org.jhove2.core.io.Input)
     */
    @Override
    public Validity validate(JHOVE2 jhove2, Source source, Input input)
                                                    throws JHOVE2Exception {
        return isValid();
    }

    /** Get validation coverage.
     * @return Validation coverage
     * @see org.jhove2.module.format.Validator#getCoverage()
     */
    @Override
    public Coverage getCoverage() {
        return COVERAGE;
    }

    /** Get validity.
     * @return Validity
     * @see org.jhove2.module.format.Validator#isValid()
     */
    @Override
    public Validity isValid() {
        return isValid;
    }

    //------------------------------------------------------------------------
    // Reportable properties
    //------------------------------------------------------------------------

    /**
     * gzipFileName getter
     * @return the gzipFileName
     */
    @ReportableProperty(order=1, value="GZip file name")
    public String getGZaipFileName() {
        return gzipFileName;
    }

    /**
     * gzipFileSize getter
     * @return the gzipFileSize
     */
    @ReportableProperty(order=2, value="GZip file size, in bytes")
    public long getGZipFileSize() {
        return gzipFileSize;
    }

    /**
     * Returns the number of GZip entries found.
     * @return the number of GZip entries found.
     */
    @ReportableProperty(order = 3,
                        value = "Number of members compressed with the deflate compression method")
    public long getNumDeflateMembers() {
        //return this.deflateMemberCount.get();
        return deflateMemberCount;
    }

    /**
     * arcReaderConsumedBytes getter
     * @return the arcReaderConsumedBytes
     */
    @ReportableProperty(order=4, value="ARC reader consumed bytes, in bytes")
    public long getArcReaderConsumedBytes() {
         return gzipReaderConsumedBytes;
    }

    /**
     * Returns the number of invalid GZip entries found.
     * @return the number of invalid GZip entries found.
     */
    @ReportableProperty(order = 5, value = "Number of non-valid members")
    public long getNumInvalidMembers() {
        //return this.invalidMembers.get();
        return invalidMembers;
    }

    /**
     * Returns the number of GZip entries marked as invalid.
     * @return the number of invalid GZip entries found.
     */
    @ReportableProperty(order = 6, value = "Validation error messages")
    public Collection<Message> getValidationMessages() {
        // Return null if the list is empty to prevent the displayer
        // from rendering this property.
        return (validationMessages.isEmpty())? null:
                    Collections.unmodifiableCollection(validationMessages);
    }

    //------------------------------------------------------------------------
    // Specific implementation
    //------------------------------------------------------------------------

    /**
     * <i>Dependency injection<i/> Sets whether to recursively
     * characterize GZip members.
     * @param  recurse   whether to recursively characterize GZip
     *                   members.
     */
    public void setRecurse(boolean recurse) {
        this.recurse = recurse;
    }

    /** Returns whether this module recursively characterizes the
     * found GZip members.
     * @return <code>true</code> if GZip members are recursively
     *         characterized; <code>false</code> otherwise.  Defaults
     *         to <code>true</code>.
     */
    public boolean getRecurse() {
        return recurse;
    }

    /*
    public void setParallelCharacterization(int level) {
        if (level < 0) {
            level = 0;
        }
        this.nThreads = level;
    }
    */

    /**
     * Sets the parser this module shall use when processing woven
     * format GZip member. Woven formats are formats such as compressed
     * ARC (web archive) where the file are made of concatenated
     * independently-gzipped records.
     * @param  parser   the parser to hand GZip members to rather than
     *                  attempting direct characterization.
     */
    public void setWovenFormatParser(Parser parser) {
        this.wovenFormatParser = parser;
    }

    /** Check member validation errors. */
    private void reportValidationErrors(GzipEntryProperties entry, Source src,
                                        JHOVE2 jhove2) throws JHOVE2Exception {
        if (! entry.isExtraFlagsValid()) {
            src.addMessage(this.newValidityError(jhove2, "invalidExtraFlags"));
        }
        if (! entry.isOperatingSystemValid()) {
            src.addMessage(this.newValidityError(jhove2,
                                                 "invalidOperatingSystem"));
        }
        if (! entry.isReservedFlagsValid()) {
            src.addMessage(this.newValidityError(jhove2, "reservedFlagsSet"));
        }
        if (! entry.isISizeValid()) {
            src.addMessage(this.newValidityError(jhove2, "invalidISize",
                                         Long.valueOf(entry.getISize()),
                                         Long.valueOf(entry.getComputedISize())));
        }
        if (! entry.isHeaderCrcValid()) {
            src.addMessage(this.newValidityError(jhove2, "invalidCrc16",
                                     entry.getCrc16(), entry.getComputedCrc16()));
        }
        if (! entry.isDataCrcValid()) {
            src.addMessage(this.newValidityError(jhove2, "invalidCrc32",
                                     entry.getCrc32(), entry.getComputedCrc32()));
        }
    }

    /** Returns a new JHove2 Message object for object parse errors. */
    private Message newValidityError(JHOVE2 jhove2, String id, Object... params)
                                                        throws JHOVE2Exception {
        return new Message(Message.Severity.ERROR,
                           Message.Context.OBJECT,
                           this.getClass().getName() + '.' + id,
                           params,
                           jhove2.getConfigInfo());
    }

}
