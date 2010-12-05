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

package org.jhove2.core.io;

import java.io.BufferedInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;

/**
 * Abstract JHOVE2 inputter.
 * 
 * @author mstrong, slabrams
 */
public abstract class AbstractInput
    implements Input
{
	/** Buffer to hold data from channel. */
	protected ByteBuffer buffer;

	/** Starting offset of the current buffer. */
	protected long bufferOffset;

	/** Current buffer size. */
	protected int bufferSize;

	/** Current byte order of buffer. */
	protected ByteOrder byteOrder;

	/** AbstractInput channel. */
	protected FileChannel channel;

	/** File underlying the inputable. */
	protected File file;

	/** InputStream underlying the inputable. */
	protected InputStream stream;
	
	/** Buffer type. */
	protected Type bufferType;

	/**
	 * Current position relative to the beginning of the inputable, as a byte
	 * offset. equal to buffer offset + buffer position
	 */
	protected long inputablePosition;

	/** Size, in bytes. */
	protected long fileSize;

	/** Buffer size, in bytes. */
	protected int maxBufferSize;

	/**
	 * Instantiate a new, big-endian <code>AbstractInput</code>.
	 * 
	 * @param file
	 *            Java {@link java.io.File} underlying the inputable
	 * @throws FileNotFoundException
	 *             File not found
	 * @throws IOException
	 *             I/O exception instantiating input
	 */
	public AbstractInput(File file, int maxBufferSize)
	    throws FileNotFoundException, IOException
	{
		this(file, maxBufferSize, ByteOrder.BIG_ENDIAN);
	}

	/**
	 * Instantiate a new <code>AbstractInput</code>.
	 * 
	 * @param file
	 *            Java {@link java.io.File} underlying the inputable
	 * @param order
	 *            Byte order
	 * @throws FileNotFoundException
	 *             File not found
	 * @throws IOException
	 *             I/O exception instantiating input
	 */
	public AbstractInput(File file, int maxBufferSize, ByteOrder order)
		throws FileNotFoundException, IOException
	{
	    if (!file.isDirectory()) {
	        this.file = file;
	        this.byteOrder = order;   
	        this.maxBufferSize = maxBufferSize;
	        this.stream = new BufferedInputStream(new FileInputStream(file),
	                                              this.maxBufferSize);
	        this.fileSize = file.length();
	        this.inputablePosition = 0L;

	        RandomAccessFile raf = new RandomAccessFile(file, "r");
	        this.channel = raf.getChannel();
	    }
	}

	/**
	 * Close the inputable.
	 * 
	 * @see org.jhove2.core.io.Input#close()
	 */
	@Override
	public void close()
	    throws IOException
	{
	    if (this.stream != null) {
	        this.stream.close();
	    }
	    if (this.channel != null) {
	        this.channel.close();
	    }
	}

	/**
	 * Get the {@link java.nio.ByteBuffer} underlying the inputable.
	 * 
	 * @return Buffer underlying the inputable
	 * @see org.jhove2.core.io.Input#getBuffer()
	 */
	@Override
	public ByteBuffer getBuffer() {
		return this.buffer;
	}

	/**
	 * Get byte order.
	 * 
	 * @return Byte order
	 */
	public ByteOrder getByteOrder() {
		return this.byteOrder;
	}
	
	/** Get buffer type.
	 * @return Buffer type
	 */
	@Override
	public Type getBufferType() {
	    return this.bufferType;
	}

	/**
	 * Get {@link java.io.File} backing the input.
	 * 
	 * @return File backing the input
	 * @see org.jhove2.core.io.Input#getFile()
	 */
	@Override
	public File getFile() {
		return this.file;
	}

	/**
	 * Get {@link java.io.InputStream} backing the input.
	 * 
	 * @return Input stream backing the input
	 * @see org.jhove2.core.io.Input#getInputStream()
	 */
	@Override
	public InputStream getInputStream() {
		return this.stream;
	}

	/**
	 * Get current buffer offset from the beginning of the inputable, in bytes.
	 * This offset is the beginning of the buffer.
	 * 
	 * Buffer Offset = channel position - buffer size
	 * 
	 * Note: the channel position is the location in the inputable where data
	 * will next be read from or written to.
	 * 
	 * @return Current buffer offset, in bytes
	 * @see org.jhove2.core.io.Input#getBufferOffset()
	 */
	@Override
	public long getBufferOffset() {
		return this.bufferOffset;
	}

	/**
	 * Get current buffer size, in bytes.
	 * 
	 * @return Current buffer size, in bytes
	 * @see org.jhove2.core.io.Input#getBufferSize()
	 */
	@Override
	public int getBufferSize() {
		return this.bufferSize;
	}

	/**
	 * Get the current buffer as a <code>byte[]</code> array.
	 * 
	 * @return Byte array
	 * @see org.jhove2.core.io.Input#getByteArray()
	 */
	@Override
	public byte[] getByteArray() {
		byte[] buffer = null;
		if (this.buffer != null) {
		    if (this.buffer.hasArray()) {
		        buffer = this.buffer.array();
		    }
		    else {
		        buffer = new byte[this.buffer.limit()];
		        /* Instead of using buffer.mark() and reset(), we explicitly
		         * save the current the position and reset it after getting
		         * the next buffer.
		         */
		        int mark_position = this.buffer.position();
		        this.buffer.position(0);
		        this.buffer.get(buffer);
                this.buffer.position(mark_position);
		    }
		}

		return buffer;
	}

	/**
	 * Get maximum buffer size, in bytes.
	 * 
	 * @return Maximum buffer size, in bytes
	 * @see org.jhove2.core.io.Input#getMaxBufferSize()
	 */
	@Override
	public int getMaxBufferSize() {
	    return this.maxBufferSize;
	}

	/**
	 * Get the next buffer's worth of data from the channel.
	 * 
	 * @return Number of bytes actually read, possibly 0 or -1 if EOF
	 * @throws IOException
	 */
	protected long getNextBuffer() throws IOException {
	    if (this.buffer != null && this.channel != null) {
	        this.buffer.clear();
	        int n = this.channel.read(this.buffer);
	        this.buffer.flip();
	        this.bufferOffset = this.channel.position() - n;
	        this.bufferSize = n;
	        this.inputablePosition = this.bufferOffset + this.buffer.position();
	        return this.bufferSize;
	    }
	    return 0L;
	}

	/**
	 * Get the current position in the inputable, as a byte offset.
	 * 
	 * @return Current position, as a byte offset
	 * @see org.jhove2.core.io.Input#getPosition()
	 */
	@Override
	public long getPosition() {
		return this.inputablePosition;
	}

	/**
	 * Get size, in bytes.
	 * 
	 * @return Size
	 */
	@Override
	public long getSize() {
		return this.fileSize;
	}
    
    /** Get UTF-16BE Unicode character at the current position.  This
     * implicitly advances the current position by two bytes.
     * @return Character at the current position
     * @see org.jhove2.core.io.Input#readChar()
     */
	@Override
    public char readChar()
	    throws EOFException, IOException
	{
	    char ch;
        int charValue = 0;
        int remaining = this.buffer.limit() - this.buffer.position();
        if (remaining < 2) {
            for (int i = 0; i < remaining; i++) {
                /*
                 * LITTLE_ENDIAN - shift byte value then add to accumlative
                 * value
                 */
                if (byteOrder == ByteOrder.LITTLE_ENDIAN) {
                    int in = (((int) this.buffer.get() & 0xff));
                    in <<= (8 * i);
                    charValue += in;
                }
                else {
                    /* BIG_ENDIAN - shift accumulative value then add byte value */
                    charValue <<= 8;
                    charValue += (((int) this.buffer.get() & 0xff));
                }
            }
            if (getNextBuffer() == EOF) {
                throw new EOFException();
            }
            for (int i = remaining; i < 2; i++) {
                if (byteOrder == ByteOrder.LITTLE_ENDIAN) {
                    int in = (((int) this.buffer.get() & 0xff));
                    in <<= (8 * i);
                    charValue += in;
                }
                else {
                    charValue <<= 8;
                    charValue += (((int) this.buffer.get() & 0xff));
                }
            }
            ch = (char) charValue;
        }
        else {
            ch = this.buffer.getChar();
        }
        this.inputablePosition += 2L;

        return ch;
    }
    
	/**
	 * Get signed byte at the current position. This implicitly advances the
	 * current position by one byte.
	 * 
	 * @return Signed byte at the current position, or -1 if EOF
	 * @see org.jhove2.core.io.Input#readSignedByte()
	 */
	@Override
	public byte readSignedByte() throws IOException {
		if (this.buffer.position() >= this.buffer.limit()) {
			if (getNextBuffer() == EOF) {
				return EOF;
			}
		}
		byte b = this.buffer.get();
		this.inputablePosition += 1;

		return b;
	}

	/**
	 * Get signed (four byte) integer at the current position. This implicitly
	 * advances the current position by four bytes.
	 * 
	 * @return signed integer at the current position, or -1 if EOF
	 * @see org.jhove2.core.io.Input#readUnsignedInt()
	 */
	@Override
	public int readSignedInt() throws IOException {
		int in = 0;
		int byteValue = 0;
		int remaining = this.buffer.limit() - this.buffer.position();
		if (remaining < 4) {
			for (int i = 0; i < remaining; i++) {
				/*
				 * LITTLE_ENDIAN - shift byte value then add to accumlative
				 * value
				 */
				if (byteOrder == ByteOrder.LITTLE_ENDIAN) {
					byteValue = (((int) this.buffer.get() & 0xff));
					byteValue <<= (8 * i);
					in += byteValue;
				} else {
					/* BIG_ENDIAN - shift accumulative value then add byte value */
					in <<= 8;
					in += (((int) this.buffer.get() & 0xff));
				}
			}
			if (getNextBuffer() == EOF) {
				return EOF;
			}
			for (int i = remaining; i < 4; i++) {
				if (byteOrder == ByteOrder.LITTLE_ENDIAN) {
					byteValue = (((int) this.buffer.get() & 0xff));
					byteValue <<= (8 * i);
					in += byteValue;
				} else {
					in <<= 8;
					in += (((int) this.buffer.get() & 0xff));
				}
			}
		} else {
			in = this.buffer.getInt();
		}
		this.inputablePosition += 4L;

		return in;
	}

	/**
	 * Get signed long integer at the current position. This implicitly advances
	 * the current position by eight bytes.
	 * 
	 * @return signed long integer at the current position, or -1 if EOF
	 * @see org.jhove2.core.io.Input#readSignedLong()
	 */
	@Override
	public long readSignedLong() throws IOException {
		long in = 0;
		int byteValue = 0;
		int remaining = this.buffer.limit() - this.buffer.position();
		if (remaining < 8) {
			for (int i = 0; i < remaining; i++) {
				/*
				 * LITTLE_ENDIAN - shift byte value then add to accumlative
				 * value
				 */
				if (byteOrder == ByteOrder.LITTLE_ENDIAN) {
					byteValue = (((int) this.buffer.get() & 0xff));
					byteValue <<= (8 * i);
					in += byteValue;
				} else {
					/* BIG_ENDIAN - shift accumulative value then add byte value */
					in <<= 8;
					in += (((int) this.buffer.get() & 0xff));
				}
			}
			if (getNextBuffer() == EOF) {
				return EOF;
			}
			for (int i = remaining; i < 8; i++) {
				if (byteOrder == ByteOrder.LITTLE_ENDIAN) {
					byteValue = (((int) this.buffer.get() & 0xff));
					byteValue <<= (8 * i);
					in += byteValue;
				} else {
					in <<= 8;
					in += (((int) this.buffer.get() & 0xff));
				}
			}
		} else {
			in = this.buffer.getLong();
		}
		this.inputablePosition += 8L;

		return in;
	}

    /**
     * Get signed double float point at the current position. This implicitly advances
     * the current position by eight bytes.
     * 
     * @return signed double floating point at the current position, or -1 if EOF
     * @see org.jhove2.core.io.Input#readDouble()
     */
    @Override
    public double readDouble() throws IOException {
        double in = 0.0F;
        long longbits = 0;
        long byteValue = 0;
        int remaining = this.buffer.limit() - this.buffer.position();
        if (remaining < 8) {
            for (int i = 0; i < remaining; i++) {
                /*
                 * LITTLE_ENDIAN - shift byte value then add to accumlative
                 * value
                 */
                if (byteOrder == ByteOrder.LITTLE_ENDIAN) {
                    byteValue = ((long) this.buffer.get() & 0xffL);
                    byteValue <<= (8 * i);
                    longbits += byteValue;
                } else {
                    /* BIG_ENDIAN - shift accumulative value then add byte value */
                    longbits <<= 8;
                    longbits += (((long) this.buffer.get() & 0xffL));
                }
            }
            if (getNextBuffer() == EOF) {
                return EOF;
            }
            for (int i = remaining; i < 8; i++) {
                if (byteOrder == ByteOrder.LITTLE_ENDIAN) {
                    byteValue = (((int) this.buffer.get() & 0xff));
                    byteValue <<= (8 * i);
                    longbits += byteValue;
                } else {
                    longbits <<= 8;
                    longbits += (((long) this.buffer.get() & 0xffL));
                }
            }
            in = Double.longBitsToDouble(longbits);
            
        } else {
            in = this.buffer.getDouble();
        }
        this.inputablePosition += 8L;

        return in;
    }

    /**
     * Get signed 32 bit floating point float at the current position. This implicitly advances
     * the current position by four bytes.
     * 
     * @return signed 32 bit floating point float at the current position, or -1 if EOF
     * @see org.jhove2.core.io.Input#readFloat()
     */
    @Override
    public float readFloat() throws IOException {
        float in = 0.0F;
        int intbits = 0;
        int byteValue = 0;
        int remaining = this.buffer.limit() - this.buffer.position();
        if (remaining < 4) {
            for (int i = 0; i < remaining; i++) {
                /*
                 * LITTLE_ENDIAN - shift byte value then add to accumlative
                 * value
                 */
                if (byteOrder == ByteOrder.LITTLE_ENDIAN) {
                    byteValue = ((int) this.buffer.get() & 0xff);
                    byteValue <<= (8 * i);
                    intbits += byteValue;
                } else {
                    /* BIG_ENDIAN - shift accumulative value then add byte value */
                    intbits <<= 8;
                    intbits += (((long) this.buffer.get() & 0xff));
                }
            }
            if (getNextBuffer() == EOF) {
                return EOF;
            }
            for (int i = remaining; i < 4; i++) {
                if (byteOrder == ByteOrder.LITTLE_ENDIAN) {
                    byteValue = (((int) this.buffer.get() & 0xff));
                    byteValue <<= (8 * i);
                    intbits += byteValue;
                } else {
                    intbits <<= 8;
                    intbits += (((int) this.buffer.get() & 0xff));
                }
            }
            in = Float.intBitsToFloat(intbits);
            
        } else {
            in = this.buffer.getFloat();
        }
        this.inputablePosition += 4L;

        return in;
    }

	/**
	 * Get signed short integer at the current position. This implicitly
	 * advances the current position by two bytes.
	 * 
	 * @return Signed short integer at the current position, or -1 if EOF
	 * @see org.jhove2.core.io.Input#readSignedShort()
	 */
	@Override
	public short readSignedShort() throws IOException {
		{
			short in = 0;
			int byteValue = 0;
			int remaining = this.buffer.limit() - this.buffer.position();
			if (remaining < 2) {
				for (int i = 0; i < remaining; i++) {
					/*
					 * LITTLE_ENDIAN - shift byte value then add to accumlative
					 * value
					 */
					if (byteOrder == ByteOrder.LITTLE_ENDIAN) {
						byteValue = (((int) this.buffer.get() & 0xff));
						byteValue <<= (8 * i);
						in += byteValue;
					} else {
						/*
						 * BIG_ENDIAN - shift accumulative value then add byte
						 * value
						 */
						in <<= 8;
						in += (((int) this.buffer.get() & 0xff));
					}
				}
				if (getNextBuffer() == EOF) {
					return EOF;
				}
				for (int i = remaining; i < 2; i++) {
					if (byteOrder == ByteOrder.LITTLE_ENDIAN) {
						byteValue = (((int) this.buffer.get() & 0xff));
						byteValue <<= (8 * i);
						in += byteValue;
					} else {
						in <<= 8;
						in += (((int) this.buffer.get() & 0xff));
					}
				}
			} else {
				in = this.buffer.getShort();
			}
			this.inputablePosition += 2L;

			return in;
		}
	}

	/**
	 * Get unsigned byte at the current position. This implicitly advances the
	 * current position by one byte.
	 * 
	 * @return Unsigned byte at the current position, or -1 if EOF
	 * @see org.jhove2.core.io.Input#readUnsignedByte()
	 */
	@Override
	public short readUnsignedByte() throws IOException {
		if (this.buffer.position() >= this.buffer.limit()) {
			if (getNextBuffer() == EOF) {
				return EOF;
			}
		}
		short b = this.buffer.get();
		b &= 0xffL;
		this.inputablePosition += 1L;

		return b;
	}

	/**
	 * Get unsigned (four byte) integer at the current position. This implicitly
	 * advances the current position by four bytes.
	 * 
	 * @return Unsigned short integer at the current position, or -1 if EOF
	 * @see org.jhove2.core.io.Input#readUnsignedInt()
	 */
	@Override
	public long readUnsignedInt() throws IOException {
		long in = 0L;
		long byteValue = 0L;
		int remaining = this.buffer.limit() - this.buffer.position();
		if (remaining < 4) {
			for (int i = 0; i < remaining; i++) {
				/*
				 * LITTLE_ENDIAN - shift byte value then add to accumlative
				 * value
				 */
				if (byteOrder == ByteOrder.LITTLE_ENDIAN) {
					byteValue = (((long) this.buffer.get() & 0xffL));
					byteValue <<= (8 * i);
					in += byteValue;
				} else {
					/* BIG_ENDIAN - shift accumulative value then add byte value */
					in <<= 8;
					in += (((long) this.buffer.get() & 0xffL));
				}
			}
			if (getNextBuffer() == EOF) {
				return EOF;
			}
			for (int i = remaining; i < 4; i++) {
				if (byteOrder == ByteOrder.LITTLE_ENDIAN) {
					byteValue = (((long) this.buffer.get() & 0xffL));
					byteValue <<= (8 * i);
					in += byteValue;
				} else {
					in <<= 8;
					in += (((long) this.buffer.get() & 0xffL));
				}
			}
		} else {
			in = this.buffer.getInt();
			in &= 0xffffffffL;
		}
		this.inputablePosition += 4L;

		return in;
	}

	/**
	 * Get unsigned short (two byte) integer at the the current position. This
	 * implicitly advances the current position by two bytes.
	 * 
	 * @return Unsigned integer at the current position, or -1 if EOF
	 * @see org.jhove2.core.io.Input#readUnsignedShort()
	 */
	@Override
	public int readUnsignedShort() throws IOException {
		int sh = 0;
		int byteValue = 0;
		int remaining = this.buffer.limit() - this.buffer.position();
		if (remaining < 2) {
			for (int i = 0; i < remaining; i++) {
				/*
				 * LITTLE_ENDIAN - shift byte value then add to accumlative
				 * value
				 */
				if (byteOrder == ByteOrder.LITTLE_ENDIAN) {
					byteValue = (((int) this.buffer.get() & 0xff));
					byteValue <<= (8 * i);
					sh += byteValue;
				} else {
					/* BIG_ENDIAN - shift accumulative value then add byte value */
					sh <<= 8;
					sh += (((int) this.buffer.get() & 0xffL));
				}
			}
			if (getNextBuffer() == EOF) {
				return EOF;
			}
			for (int i = remaining; i < 4; i++) {
				if (byteOrder == ByteOrder.LITTLE_ENDIAN) {
					byteValue = (((int) this.buffer.get() & 0xff));
					byteValue <<= (8 * i);
					sh += byteValue;
				} else {
					sh <<= 8;
					sh += (((int) this.buffer.get() & 0xffL));
				}
			}
		} else {
			sh = this.buffer.getShort();
			sh &= 0xffffL;
		}
		this.inputablePosition += 2L;

		return sh;
	}

	/**
	 * Set byte order: big-endian or little-endian.
	 * 
	 * @param order
	 *            Byte order
	 * @see org.jhove2.core.io.Input#setByteOrder(ByteOrder)
	 */
	@Override
	public void setByteOrder(ByteOrder order) {
		this.buffer.order(order);
		this.byteOrder = this.buffer.order();
	}

	/**
	 * Set the current position, as a byte offset
	 * 
	 * @param position
	 *            Current position, as a byte offset
	 * @throws IOException
	 * @see org.jhove2.core.io.Input#setPosition(long)
	 */
	@Override
	public void setPosition(long position) throws IOException {
		/* Determine if the new position is within the current buffer. */
		int pos = this.buffer.position();
		int lim = this.buffer.limit();
		long del = position - this.inputablePosition;
		if (-del > pos || del > lim - pos) {
			this.channel.position(position);
			getNextBuffer();
		} else {
			this.buffer.position(pos + (int) del);
			this.inputablePosition = position;
		}

	}

	/** Set buffer type.
	 * @param type Buffer type
	 */
	@Override
	public void setBufferType(Type type) {
	    this.bufferType = type;
	}
}
