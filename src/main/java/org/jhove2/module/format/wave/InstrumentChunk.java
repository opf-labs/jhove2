/**
 * JHOVE2 - Next-generation architecture for format-aware characterization
 *
 * Copyright (c) 2009 by The Regents of the University of California.
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

package org.jhove2.module.format.wave;

import java.io.EOFException;
import java.io.IOException;
import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.Message;
import org.jhove2.core.Message.Context;
import org.jhove2.core.Message.Severity;
import org.jhove2.core.io.Input;
import org.jhove2.module.format.Validator.Validity;
import org.jhove2.module.format.riff.GenericChunk;

/** WAVE instrument chunk.
 * 
 * @author slabrams
 */
public class InstrumentChunk
        extends GenericChunk
{
    /** Instrument pitch shift adjustment in cents. */
    protected byte fineTune;
    
    /** Instrument suggested volume level in decibels. */
    protected byte gain;;
    
    /** MIDI note number suggested range, high. */
    protected short highNote;
    
    /** MIDI velocity suggested range, high. */
    protected short highVelocity;
    
    /** MIDI note number suggested range, low. */
    protected short lowNote;
    
    /** MIDI velocity suggested range, low. */
    protected short lowVelocity;
    
    /** MIDI note number for unshifted pitch. */
    protected short unshiftedNote;
    
    /** Invalid fine tune message. */
    protected Message invalidFineTuneMessage;
    
    /** Invalid MIDI note message. */
    protected Message invalidMIDINoteMessage;
    
    /** Invalid MIDI velocity message. */
    protected Message invalidMIDIVelocityMessage;
    
    /** Invalid unshifted note message. */
    protected Message invalidUnshiftedNoteMessage;
    
    /** Instantiate a new <code>InstrumentChunk</code> */
    public InstrumentChunk() {
        super();
    }
    
    /** 
     * Parse a WAVE chunk.
     * 
     * @param jhove2
     *            JHOVE2 framework
     * @param input
     *            WAVE input
     * @return Number of bytes consumed
     * @throws EOFException
     *             If End-of-File is reached reading the source unit
     * @throws IOException
     *             If an I/O exception is raised reading the source unit
     * @throws JHOVE2Exception
     */
    public long parse(JHOVE2 jhove2, Input input)
        throws EOFException, IOException, JHOVE2Exception
    {
        long consumed = super.parse(jhove2, input);
        
        /* Unshifted note. */
        this.unshiftedNote = input.readUnsignedByte();
        if (this.unshiftedNote < 0 || this.unshiftedNote > 127) {
            this.isValid = Validity.False;
            Object [] args = new Object [] {input.getPosition()-1L, this.unshiftedNote};
            this.invalidUnshiftedNoteMessage = new Message(Severity.ERROR,
                    Context.OBJECT,
                    "org.jhove2.module.format.wave.InstrumentChunk.invalidUnshiftedNote",
                    args, jhove2.getConfigInfo());
        }
        consumed++;
        
        /* Fine tune. */
        this.fineTune = input.readSignedByte();
        if (this.fineTune < -50 || this.fineTune > 50) {
            this.isValid = Validity.False;
            Object [] args = new Object [] {input.getPosition()-1L, this.fineTune};
            this.invalidFineTuneMessage = new Message(Severity.ERROR,
                    Context.OBJECT,
                    "org.jhove2.module.format.wave.InstrumentChunk.invalidFineTune",
                    args, jhove2.getConfigInfo());
        }
        consumed++;
        
        /* Gain. */
        this.gain = input.readSignedByte();
        consumed++;
        
        /* Low note. */
        this.lowNote = input.readUnsignedByte();
        if (this.lowNote < 0 || this.lowNote > 127) {
            this.isValid = Validity.False;
            Object [] args = new Object [] {input.getPosition()-1L, this.lowNote};
            this.invalidMIDINoteMessage = new Message(Severity.ERROR,
                    Context.OBJECT,
                    "org.jhove2.module.format.wave.InstrumentChunk.invalidMIDINote",
                    args, jhove2.getConfigInfo());
        }
        consumed++;
        
        /* High note. */
        this.highNote = input.readUnsignedByte();
        if (this.highNote < 0 || this.highNote > 127) {
            this.isValid = Validity.False;
            Object [] args = new Object [] {input.getPosition()-1L, this.highNote};
            this.invalidMIDINoteMessage = new Message(Severity.ERROR,
                    Context.OBJECT,
                    "org.jhove2.module.format.wave.InstrumentChunk.invalidMIDINote",
                    args, jhove2.getConfigInfo());
        }
        consumed++;
        
        /* Low velocity. */
        this.lowVelocity = input.readUnsignedByte();
        if (this.lowVelocity < 0 || this.lowVelocity > 127) {
            this.isValid = Validity.False;
            Object [] args = new Object [] {input.getPosition()-1L, this.lowVelocity};
            this.invalidMIDINoteMessage = new Message(Severity.ERROR,
                    Context.OBJECT,
                    "org.jhove2.module.format.wave.InstrumentChunk.invalidMIDIVelocity",
                    args, jhove2.getConfigInfo());
        }
        consumed++;
        
        /* High velocity. */
        this.highVelocity = input.readUnsignedByte();
        if (this.highVelocity < 0 || this.highVelocity > 127) {
            this.isValid = Validity.False;
            Object [] args = new Object [] {input.getPosition()-1L, this.highVelocity};
            this.invalidMIDINoteMessage = new Message(Severity.ERROR,
                    Context.OBJECT,
                    "org.jhove2.module.format.wave.InstrumentChunk.invalidMIDIVelocity",
                    args, jhove2.getConfigInfo());
        }
        consumed++;
        
        return consumed;
    }
    /** Get instrument pitch shift adjustment in cents.
     * @return Instrument pitch shift adjustment
     */
    @ReportableProperty(order=2, value="Instrument pitch shift adjustment in cents.")
    public byte getFineTune() {
        return this.fineTune;
    }
    
    /** Get instrument suggested volume level in decibels.
     * @return Instrument suggested volume level
     */
    @ReportableProperty(order=3, value="Instrument suggested volume level in decibels.")
    public byte getGain() {
        return this.gain;
    }
    
    /** Get MIDI note suggested range, high.
     * @return MIDI note suggested range, high
     */
    @ReportableProperty(order=5, value="MIDI note suggested range, high.")
    public short getHighNote() {
        return this.highNote;
    }
    
    /** Get MIDI velocity suggested range, high.
     * @return MIDI velocity suggested range, high
     */
    @ReportableProperty(order=7, value="MIDI velocity suggested range, high.")
    public short getHighVelocity() {
        return this.highVelocity;
    }
    
    /** Get invalid fine tune message.
     * @return Invalid fine tune message
     */
    @ReportableProperty(order=22, value="Invalid fine tune message.")
    public Message getInvalidFineTuneMessage() {
        return this.invalidFineTuneMessage;
    }
    
    /** Get invalid MIDI note message.
     * @return Invalid MIDI note message
     */
    @ReportableProperty(order=23, value="Invalid MIDI note message.")
    public Message getInvalidMIDINoteMessage() {
        return this.invalidMIDINoteMessage;
    }
    
    /** Get invalid MIDI velocity message.
     * @return Invalid MIDI velocity message
     */
    @ReportableProperty(order=24, value="Invalid MIDI velocity message.")
    public Message getInvalidMIDIVelocityMessage() {
        return this.invalidMIDIVelocityMessage;
    }
    
    /** Get invalid unshifted note message.
     * @return Invalid unshifted note message
     */
    @ReportableProperty(order=21, value="Invalid unshifted note message.")
    public Message getInvalidUnshiftedNoteMessage() {
        return this.invalidUnshiftedNoteMessage;
    }
    
    /** Get MIDI note suggested range, low.
     * @return MIDI note suggested range, low
     */
    @ReportableProperty(order=4, value="MIDI note suggested range, low.")
    public short getLowNote() {
        return this.lowNote;
    }
    
    /** Get MIDI velocity suggested range, low.
     * @return MIDI velocity suggested range, low
     */
    @ReportableProperty(order=6, value="MIDI velocity suggested range, low.")
    public short getLowVelocity() {
        return this.lowVelocity;
    }
    
    /** Get MIDI note number for unshifted pitch.
     * @return MIDI note number
     */
    @ReportableProperty(order=1, value="MIDI note number for unshifted pitch.")
    public short getUnshiftedNote() {
        return this.unshiftedNote;
    }
}