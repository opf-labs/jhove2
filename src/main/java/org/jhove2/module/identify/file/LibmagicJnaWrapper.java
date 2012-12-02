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

package org.jhove2.module.identify.file;


import java.nio.Buffer;

import org.jhove2.core.JHOVE2Exception;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.NativeLong;
import com.sun.jna.Platform;
import com.sun.jna.Pointer;


/**
 * A wrapper for the libmagic library that relies on JNA.
 *
 * @author hbian
 */
public class LibmagicJnaWrapper {

	public interface LibmagicDll extends Library {
        String LIBRARY_NAME = (Platform.isWindows())? "magic1": "magic";
        LibmagicDll BASE = (LibmagicDll)
                        Native.loadLibrary(LIBRARY_NAME, LibmagicDll.class);
        LibmagicDll INSTANCE = (LibmagicDll)Native.synchronizedLibrary(BASE);

        Pointer magic_open(int flags);
        void magic_close(Pointer cookie);
        int magic_setflags(Pointer cookie, int flags);

        String magic_file(Pointer cookie, String fileName);
        String magic_buffer(Pointer cookie, Buffer buffer, NativeLong length);

        int magic_compile(Pointer cookie, String magicFileName);
        int magic_check(Pointer cookie, String magicFileName);
        int magic_load(Pointer cookie, String magicFileName);

        int magic_errno(Pointer cookie);
        String magic_error(Pointer cookie);
    }

    /** Libmagic flag: No flags. */
    public final static int MAGIC_NONE                  = 0x000000;
    /** Libmagic flag: Turn on debugging. */
    public final static int MAGIC_DEBUG                 = 0x000001;
    /** Libmagic flag: Follow symlinks. */
    public final static int MAGIC_SYMLINK               = 0x000002;
    /** Libmagic flag: Check inside compressed files. */
    public final static int MAGIC_COMPRESS              = 0x000004;
    /** Libmagic flag: Look at the contents of devices. */
    public final static int MAGIC_DEVICES               = 0x000008;
    /** Libmagic flag: Return the MIME type. */
    public final static int MAGIC_MIME_TYPE             = 0x000010;
    /** Libmagic flag: Return all matches. */
    public final static int MAGIC_CONTINUE              = 0x000020;
    /** Libmagic flag: Print warnings to stderr. */
    public final static int MAGIC_CHECK                 = 0x000040;
    /** Libmagic flag: Restore access time on exit. */
    public final static int MAGIC_PRESERVE_ATIME        = 0x000080;
    /** Libmagic flag: Don't translate unprintable chars. */
    public final static int MAGIC_RAW                   = 0x000100;
    /** Libmagic flag: Handle ENOENT etc as real errors. */
    public final static int MAGIC_ERROR                 = 0x000200;
    /** Libmagic flag: Return the MIME encoding. */
    public final static int MAGIC_MIME_ENCODING         = 0x000400;
    /** Libmagic flag: Return both MIME type and encoding. */
    public final static int MAGIC_MIME                  =
                                        (MAGIC_MIME_TYPE | MAGIC_MIME_ENCODING);
    /** Libmagic flag: Return the Apple creator and type. */
    public final static int MAGIC_APPLE                 = 0x000800;
    /** Libmagic flag: Don't check for compressed files. */
    public final static int MAGIC_NO_CHECK_COMPRESS     = 0x001000;
    /** Libmagic flag: Don't check for tar files. */
    public final static int MAGIC_NO_CHECK_TAR          = 0x002000;
    /** Libmagic flag: Don't check magic entries. */
    public final static int MAGIC_NO_CHECK_SOFT         = 0x004000;
    /** Libmagic flag: Don't check application type. */
    public final static int MAGIC_NO_CHECK_APPTYPE      = 0x008000;
    /** Libmagic flag: Don't check for elf details. */
    public final static int MAGIC_NO_CHECK_ELF          = 0x010000;
    /** Libmagic flag: Don't check for text files. */
    public final static int MAGIC_NO_CHECK_TEXT         = 0x020000;
    /** Libmagic flag: Don't check for cdf files. */
    public final static int MAGIC_NO_CHECK_CDF          = 0x040000;
    /** Libmagic flag: Don't check tokens. */
    public final static int MAGIC_NO_CHECK_TOKENS       = 0x100000;
    /** Libmagic flag: Don't check text encodings. */
    public final static int MAGIC_NO_CHECK_ENCODING     = 0x200000;

    /** Magic cookie pointer. */
    private final Pointer cookie;

    /**
     * Creates a new instance returning the default information: MIME
     * type and character encoding.
     *
     * @throws JHOVE2Exception   if any error occurred while
     *         initializing the libmagic.
     *
     * @see    #LibmagicJnaWrapper(int)
     * @see    #MAGIC_MIME
     */
    public LibmagicJnaWrapper() throws JHOVE2Exception {
        this(MAGIC_MIME | MAGIC_SYMLINK);
    }

    /**
     * Creates a new instance returning the information specified in
     * the <code>flag</code> argument
     *
     * @throws JHOVE2Exception   if any error occurred while
     *         initializing the libmagic.
     */
    public LibmagicJnaWrapper(int flag) throws JHOVE2Exception {
        this.cookie = LibmagicDll.INSTANCE.magic_open(flag);
        if (this.cookie == null) {
            throw new JHOVE2Exception("Libmagic initialization failed");
        }
    }

    /**
     * Closes the magic database and deallocates any resources used.
     */
    public void close() {
        LibmagicDll.INSTANCE.magic_close(cookie);
    }

    /**
     * Returns a textual explanation of the last error.
     * @return the textual description of the last error, or
     *         <code>null</code> if there was no error.
     */
    public String getError() {
        return LibmagicDll.INSTANCE.magic_error(cookie);
    }

    /**
     * Returns the textual description of the contents of the
     * specified file.
     * @param filePath   the path of the file to be identified.
     *
     * @return the textual description of the file, or
     *         <code>null</code> if an error occurred.
     */
    public String getMimeType(String filePath) {
        if ((filePath == null) || (filePath.length() == 0)) {
            throw new IllegalArgumentException("filePath");
        }
        return LibmagicDll.INSTANCE.magic_file(cookie, filePath);
    }

    /**
     * Returns textual description of the contents of the
     * <code>buffer</code> argument.
     * @param buffer   the data to analyze.
     * @param length   the length, in bytes, of the buffer.
     *
     * @return the textual description of the buffer data, or
     *         <code>null</code> if an error occurred.
     */
    public String getMimeType(Buffer buffer, long length) {
        return LibmagicDll.INSTANCE.magic_buffer(cookie, buffer,
                                                 new NativeLong(length));
    }

    /**
     * Compiles the colon-separated list of database text files passed
     * in as <code>magicFiles</code>.
     * @param magicFiles   the magic database file(s), or
     *                     <code>null</code> to use the default
     *                     database.
     * @return 0 on success and -1 on failure.
     */
    public int compile(String magicFiles) {
        return LibmagicDll.INSTANCE.magic_compile(cookie, magicFiles);
    }

    /**
     * Loads the colon-separated list of database files passed in as
     * <code>magicFiles</code>. This method must be used before any
     * magic queries be performed.
     * @param magicFiles   the magic database file(s), or
     *                     <code>null</code> to use the default
     *                     database.
     * @return 0 on success and -1 on failure.
     */
    public int load(String magicFiles) {
        return LibmagicDll.INSTANCE.magic_load(cookie, magicFiles);
    }

}
