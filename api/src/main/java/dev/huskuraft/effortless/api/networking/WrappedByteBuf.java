package dev.huskuraft.effortless.api.networking;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.channels.GatheringByteChannel;
import java.nio.channels.ScatteringByteChannel;
import java.nio.charset.Charset;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.util.ByteProcessor;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.StringUtil;

class WrappedByteBuf extends ByteBuf {

    protected final ByteBuf source;

    protected WrappedByteBuf(ByteBuf source) {
        this.source = ObjectUtil.checkNotNull(source, "source");
    }

    @Override
    public final boolean hasMemoryAddress() {
        return source.hasMemoryAddress();
    }

    @Override
    public boolean isContiguous() {
        return source.isContiguous();
    }

    @Override
    public final long memoryAddress() {
        return source.memoryAddress();
    }

    @Override
    public final int capacity() {
        return source.capacity();
    }

    @Override
    public ByteBuf capacity(int newCapacity) {
        source.capacity(newCapacity);
        return this;
    }

    @Override
    public final int maxCapacity() {
        return source.maxCapacity();
    }

    @Override
    public final ByteBufAllocator alloc() {
        return source.alloc();
    }

    @Override
    public final ByteOrder order() {
        return source.order();
    }

    @Override
    public ByteBuf order(ByteOrder endianness) {
        return source.order(endianness);
    }

    @Override
    public final ByteBuf unwrap() {
        return source;
    }

    @Override
    public ByteBuf asReadOnly() {
        return source.asReadOnly();
    }

    @Override
    public boolean isReadOnly() {
        return source.isReadOnly();
    }

    @Override
    public final boolean isDirect() {
        return source.isDirect();
    }

    @Override
    public final int readerIndex() {
        return source.readerIndex();
    }

    @Override
    public final ByteBuf readerIndex(int readerIndex) {
        source.readerIndex(readerIndex);
        return this;
    }

    @Override
    public final int writerIndex() {
        return source.writerIndex();
    }

    @Override
    public final ByteBuf writerIndex(int writerIndex) {
        source.writerIndex(writerIndex);
        return this;
    }

    @Override
    public ByteBuf setIndex(int readerIndex, int writerIndex) {
        source.setIndex(readerIndex, writerIndex);
        return this;
    }

    @Override
    public final int readableBytes() {
        return source.readableBytes();
    }

    @Override
    public final int writableBytes() {
        return source.writableBytes();
    }

    @Override
    public final int maxWritableBytes() {
        return source.maxWritableBytes();
    }

    @Override
    public int maxFastWritableBytes() {
        return source.maxFastWritableBytes();
    }

    @Override
    public final boolean isReadable() {
        return source.isReadable();
    }

    @Override
    public final boolean isWritable() {
        return source.isWritable();
    }

    @Override
    public final ByteBuf clear() {
        source.clear();
        return this;
    }

    @Override
    public final ByteBuf markReaderIndex() {
        source.markReaderIndex();
        return this;
    }

    @Override
    public final ByteBuf resetReaderIndex() {
        source.resetReaderIndex();
        return this;
    }

    @Override
    public final ByteBuf markWriterIndex() {
        source.markWriterIndex();
        return this;
    }

    @Override
    public final ByteBuf resetWriterIndex() {
        source.resetWriterIndex();
        return this;
    }

    @Override
    public ByteBuf discardReadBytes() {
        source.discardReadBytes();
        return this;
    }

    @Override
    public ByteBuf discardSomeReadBytes() {
        source.discardSomeReadBytes();
        return this;
    }

    @Override
    public ByteBuf ensureWritable(int minWritableBytes) {
        source.ensureWritable(minWritableBytes);
        return this;
    }

    @Override
    public int ensureWritable(int minWritableBytes, boolean force) {
        return source.ensureWritable(minWritableBytes, force);
    }

    @Override
    public boolean getBoolean(int index) {
        return source.getBoolean(index);
    }

    @Override
    public byte getByte(int index) {
        return source.getByte(index);
    }

    @Override
    public short getUnsignedByte(int index) {
        return source.getUnsignedByte(index);
    }

    @Override
    public short getShort(int index) {
        return source.getShort(index);
    }

    @Override
    public short getShortLE(int index) {
        return source.getShortLE(index);
    }

    @Override
    public int getUnsignedShort(int index) {
        return source.getUnsignedShort(index);
    }

    @Override
    public int getUnsignedShortLE(int index) {
        return source.getUnsignedShortLE(index);
    }

    @Override
    public int getMedium(int index) {
        return source.getMedium(index);
    }

    @Override
    public int getMediumLE(int index) {
        return source.getMediumLE(index);
    }

    @Override
    public int getUnsignedMedium(int index) {
        return source.getUnsignedMedium(index);
    }

    @Override
    public int getUnsignedMediumLE(int index) {
        return source.getUnsignedMediumLE(index);
    }

    @Override
    public int getInt(int index) {
        return source.getInt(index);
    }

    @Override
    public int getIntLE(int index) {
        return source.getIntLE(index);
    }

    @Override
    public long getUnsignedInt(int index) {
        return source.getUnsignedInt(index);
    }

    @Override
    public long getUnsignedIntLE(int index) {
        return source.getUnsignedIntLE(index);
    }

    @Override
    public long getLong(int index) {
        return source.getLong(index);
    }

    @Override
    public long getLongLE(int index) {
        return source.getLongLE(index);
    }

    @Override
    public char getChar(int index) {
        return source.getChar(index);
    }

    @Override
    public float getFloat(int index) {
        return source.getFloat(index);
    }

    @Override
    public double getDouble(int index) {
        return source.getDouble(index);
    }

    @Override
    public ByteBuf getBytes(int index, ByteBuf dst) {
        source.getBytes(index, dst);
        return this;
    }

    @Override
    public ByteBuf getBytes(int index, ByteBuf dst, int length) {
        source.getBytes(index, dst, length);
        return this;
    }

    @Override
    public ByteBuf getBytes(int index, ByteBuf dst, int dstIndex, int length) {
        source.getBytes(index, dst, dstIndex, length);
        return this;
    }

    @Override
    public ByteBuf getBytes(int index, byte[] dst) {
        source.getBytes(index, dst);
        return this;
    }

    @Override
    public ByteBuf getBytes(int index, byte[] dst, int dstIndex, int length) {
        source.getBytes(index, dst, dstIndex, length);
        return this;
    }

    @Override
    public ByteBuf getBytes(int index, ByteBuffer dst) {
        source.getBytes(index, dst);
        return this;
    }

    @Override
    public ByteBuf getBytes(int index, OutputStream out, int length) throws IOException {
        source.getBytes(index, out, length);
        return this;
    }

    @Override
    public int getBytes(int index, GatheringByteChannel out, int length) throws IOException {
        return source.getBytes(index, out, length);
    }

    @Override
    public int getBytes(int index, FileChannel out, long position, int length) throws IOException {
        return source.getBytes(index, out, position, length);
    }

    @Override
    public CharSequence getCharSequence(int index, int length, Charset charset) {
        return source.getCharSequence(index, length, charset);
    }

    @Override
    public ByteBuf setBoolean(int index, boolean value) {
        source.setBoolean(index, value);
        return this;
    }

    @Override
    public ByteBuf setByte(int index, int value) {
        source.setByte(index, value);
        return this;
    }

    @Override
    public ByteBuf setShort(int index, int value) {
        source.setShort(index, value);
        return this;
    }

    @Override
    public ByteBuf setShortLE(int index, int value) {
        source.setShortLE(index, value);
        return this;
    }

    @Override
    public ByteBuf setMedium(int index, int value) {
        source.setMedium(index, value);
        return this;
    }

    @Override
    public ByteBuf setMediumLE(int index, int value) {
        source.setMediumLE(index, value);
        return this;
    }

    @Override
    public ByteBuf setInt(int index, int value) {
        source.setInt(index, value);
        return this;
    }

    @Override
    public ByteBuf setIntLE(int index, int value) {
        source.setIntLE(index, value);
        return this;
    }

    @Override
    public ByteBuf setLong(int index, long value) {
        source.setLong(index, value);
        return this;
    }

    @Override
    public ByteBuf setLongLE(int index, long value) {
        source.setLongLE(index, value);
        return this;
    }

    @Override
    public ByteBuf setChar(int index, int value) {
        source.setChar(index, value);
        return this;
    }

    @Override
    public ByteBuf setFloat(int index, float value) {
        source.setFloat(index, value);
        return this;
    }

    @Override
    public ByteBuf setDouble(int index, double value) {
        source.setDouble(index, value);
        return this;
    }

    @Override
    public ByteBuf setBytes(int index, ByteBuf src) {
        source.setBytes(index, src);
        return this;
    }

    @Override
    public ByteBuf setBytes(int index, ByteBuf src, int length) {
        source.setBytes(index, src, length);
        return this;
    }

    @Override
    public ByteBuf setBytes(int index, ByteBuf src, int srcIndex, int length) {
        source.setBytes(index, src, srcIndex, length);
        return this;
    }

    @Override
    public ByteBuf setBytes(int index, byte[] src) {
        source.setBytes(index, src);
        return this;
    }

    @Override
    public ByteBuf setBytes(int index, byte[] src, int srcIndex, int length) {
        source.setBytes(index, src, srcIndex, length);
        return this;
    }

    @Override
    public ByteBuf setBytes(int index, ByteBuffer src) {
        source.setBytes(index, src);
        return this;
    }

    @Override
    public int setBytes(int index, InputStream in, int length) throws IOException {
        return source.setBytes(index, in, length);
    }

    @Override
    public int setBytes(int index, ScatteringByteChannel in, int length) throws IOException {
        return source.setBytes(index, in, length);
    }

    @Override
    public int setBytes(int index, FileChannel in, long position, int length) throws IOException {
        return source.setBytes(index, in, position, length);
    }

    @Override
    public ByteBuf setZero(int index, int length) {
        source.setZero(index, length);
        return this;
    }

    @Override
    public int setCharSequence(int index, CharSequence sequence, Charset charset) {
        return source.setCharSequence(index, sequence, charset);
    }

    @Override
    public boolean readBoolean() {
        return source.readBoolean();
    }

    @Override
    public byte readByte() {
        return source.readByte();
    }

    @Override
    public short readUnsignedByte() {
        return source.readUnsignedByte();
    }

    @Override
    public short readShort() {
        return source.readShort();
    }

    @Override
    public short readShortLE() {
        return source.readShortLE();
    }

    @Override
    public int readUnsignedShort() {
        return source.readUnsignedShort();
    }

    @Override
    public int readUnsignedShortLE() {
        return source.readUnsignedShortLE();
    }

    @Override
    public int readMedium() {
        return source.readMedium();
    }

    @Override
    public int readMediumLE() {
        return source.readMediumLE();
    }

    @Override
    public int readUnsignedMedium() {
        return source.readUnsignedMedium();
    }

    @Override
    public int readUnsignedMediumLE() {
        return source.readUnsignedMediumLE();
    }

    @Override
    public int readInt() {
        return source.readInt();
    }

    @Override
    public int readIntLE() {
        return source.readIntLE();
    }

    @Override
    public long readUnsignedInt() {
        return source.readUnsignedInt();
    }

    @Override
    public long readUnsignedIntLE() {
        return source.readUnsignedIntLE();
    }

    @Override
    public long readLong() {
        return source.readLong();
    }

    @Override
    public long readLongLE() {
        return source.readLongLE();
    }

    @Override
    public char readChar() {
        return source.readChar();
    }

    @Override
    public float readFloat() {
        return source.readFloat();
    }

    @Override
    public double readDouble() {
        return source.readDouble();
    }

    @Override
    public ByteBuf readBytes(int length) {
        return source.readBytes(length);
    }

    @Override
    public ByteBuf readSlice(int length) {
        return source.readSlice(length);
    }

    @Override
    public ByteBuf readRetainedSlice(int length) {
        return source.readRetainedSlice(length);
    }

    @Override
    public ByteBuf readBytes(ByteBuf dst) {
        source.readBytes(dst);
        return this;
    }

    @Override
    public ByteBuf readBytes(ByteBuf dst, int length) {
        source.readBytes(dst, length);
        return this;
    }

    @Override
    public ByteBuf readBytes(ByteBuf dst, int dstIndex, int length) {
        source.readBytes(dst, dstIndex, length);
        return this;
    }

    @Override
    public ByteBuf readBytes(byte[] dst) {
        source.readBytes(dst);
        return this;
    }

    @Override
    public ByteBuf readBytes(byte[] dst, int dstIndex, int length) {
        source.readBytes(dst, dstIndex, length);
        return this;
    }

    @Override
    public ByteBuf readBytes(ByteBuffer dst) {
        source.readBytes(dst);
        return this;
    }

    @Override
    public ByteBuf readBytes(OutputStream out, int length) throws IOException {
        source.readBytes(out, length);
        return this;
    }

    @Override
    public int readBytes(GatheringByteChannel out, int length) throws IOException {
        return source.readBytes(out, length);
    }

    @Override
    public int readBytes(FileChannel out, long position, int length) throws IOException {
        return source.readBytes(out, position, length);
    }

    @Override
    public CharSequence readCharSequence(int length, Charset charset) {
        return source.readCharSequence(length, charset);
    }

    @Override
    public ByteBuf skipBytes(int length) {
        source.skipBytes(length);
        return this;
    }

    @Override
    public ByteBuf writeBoolean(boolean value) {
        source.writeBoolean(value);
        return this;
    }

    @Override
    public ByteBuf writeByte(int value) {
        source.writeByte(value);
        return this;
    }

    @Override
    public ByteBuf writeShort(int value) {
        source.writeShort(value);
        return this;
    }

    @Override
    public ByteBuf writeShortLE(int value) {
        source.writeShortLE(value);
        return this;
    }

    @Override
    public ByteBuf writeMedium(int value) {
        source.writeMedium(value);
        return this;
    }

    @Override
    public ByteBuf writeMediumLE(int value) {
        source.writeMediumLE(value);
        return this;
    }

    @Override
    public ByteBuf writeInt(int value) {
        source.writeInt(value);
        return this;
    }

    @Override
    public ByteBuf writeIntLE(int value) {
        source.writeIntLE(value);
        return this;
    }

    @Override
    public ByteBuf writeLong(long value) {
        source.writeLong(value);
        return this;
    }

    @Override
    public ByteBuf writeLongLE(long value) {
        source.writeLongLE(value);
        return this;
    }

    @Override
    public ByteBuf writeChar(int value) {
        source.writeChar(value);
        return this;
    }

    @Override
    public ByteBuf writeFloat(float value) {
        source.writeFloat(value);
        return this;
    }

    @Override
    public ByteBuf writeDouble(double value) {
        source.writeDouble(value);
        return this;
    }

    @Override
    public ByteBuf writeBytes(ByteBuf src) {
        source.writeBytes(src);
        return this;
    }

    @Override
    public ByteBuf writeBytes(ByteBuf src, int length) {
        source.writeBytes(src, length);
        return this;
    }

    @Override
    public ByteBuf writeBytes(ByteBuf src, int srcIndex, int length) {
        source.writeBytes(src, srcIndex, length);
        return this;
    }

    @Override
    public ByteBuf writeBytes(byte[] src) {
        source.writeBytes(src);
        return this;
    }

    @Override
    public ByteBuf writeBytes(byte[] src, int srcIndex, int length) {
        source.writeBytes(src, srcIndex, length);
        return this;
    }

    @Override
    public ByteBuf writeBytes(ByteBuffer src) {
        source.writeBytes(src);
        return this;
    }

    @Override
    public int writeBytes(InputStream in, int length) throws IOException {
        return source.writeBytes(in, length);
    }

    @Override
    public int writeBytes(ScatteringByteChannel in, int length) throws IOException {
        return source.writeBytes(in, length);
    }

    @Override
    public int writeBytes(FileChannel in, long position, int length) throws IOException {
        return source.writeBytes(in, position, length);
    }

    @Override
    public ByteBuf writeZero(int length) {
        source.writeZero(length);
        return this;
    }

    @Override
    public int writeCharSequence(CharSequence sequence, Charset charset) {
        return source.writeCharSequence(sequence, charset);
    }

    @Override
    public int indexOf(int fromIndex, int toIndex, byte value) {
        return source.indexOf(fromIndex, toIndex, value);
    }

    @Override
    public int bytesBefore(byte value) {
        return source.bytesBefore(value);
    }

    @Override
    public int bytesBefore(int length, byte value) {
        return source.bytesBefore(length, value);
    }

    @Override
    public int bytesBefore(int index, int length, byte value) {
        return source.bytesBefore(index, length, value);
    }

    @Override
    public int forEachByte(ByteProcessor processor) {
        return source.forEachByte(processor);
    }

    @Override
    public int forEachByte(int index, int length, ByteProcessor processor) {
        return source.forEachByte(index, length, processor);
    }

    @Override
    public int forEachByteDesc(ByteProcessor processor) {
        return source.forEachByteDesc(processor);
    }

    @Override
    public int forEachByteDesc(int index, int length, ByteProcessor processor) {
        return source.forEachByteDesc(index, length, processor);
    }

    @Override
    public ByteBuf copy() {
        return source.copy();
    }

    @Override
    public ByteBuf copy(int index, int length) {
        return source.copy(index, length);
    }

    @Override
    public ByteBuf slice() {
        return source.slice();
    }

    @Override
    public ByteBuf retainedSlice() {
        return source.retainedSlice();
    }

    @Override
    public ByteBuf slice(int index, int length) {
        return source.slice(index, length);
    }

    @Override
    public ByteBuf retainedSlice(int index, int length) {
        return source.retainedSlice(index, length);
    }

    @Override
    public ByteBuf duplicate() {
        return source.duplicate();
    }

    @Override
    public ByteBuf retainedDuplicate() {
        return source.retainedDuplicate();
    }

    @Override
    public int nioBufferCount() {
        return source.nioBufferCount();
    }

    @Override
    public ByteBuffer nioBuffer() {
        return source.nioBuffer();
    }

    @Override
    public ByteBuffer nioBuffer(int index, int length) {
        return source.nioBuffer(index, length);
    }

    @Override
    public ByteBuffer[] nioBuffers() {
        return source.nioBuffers();
    }

    @Override
    public ByteBuffer[] nioBuffers(int index, int length) {
        return source.nioBuffers(index, length);
    }

    @Override
    public ByteBuffer internalNioBuffer(int index, int length) {
        return source.internalNioBuffer(index, length);
    }

    @Override
    public boolean hasArray() {
        return source.hasArray();
    }

    @Override
    public byte[] array() {
        return source.array();
    }

    @Override
    public int arrayOffset() {
        return source.arrayOffset();
    }

    @Override
    public String toString(Charset charset) {
        return source.toString(charset);
    }

    @Override
    public String toString(int index, int length, Charset charset) {
        return source.toString(index, length, charset);
    }

    @Override
    public int hashCode() {
        return source.hashCode();
    }

    @Override
    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    public boolean equals(Object obj) {
        return source.equals(obj);
    }

    @Override
    public int compareTo(ByteBuf buffer) {
        return source.compareTo(buffer);
    }

    @Override
    public String toString() {
        return StringUtil.simpleClassName(this) + '(' + source.toString() + ')';
    }

    @Override
    public ByteBuf retain(int increment) {
        source.retain(increment);
        return this;
    }

    @Override
    public ByteBuf retain() {
        source.retain();
        return this;
    }

    @Override
    public ByteBuf touch() {
        source.touch();
        return this;
    }

    @Override
    public ByteBuf touch(Object hint) {
        source.touch(hint);
        return this;
    }

    @Override
    public final boolean isReadable(int size) {
        return source.isReadable(size);
    }

    @Override
    public final boolean isWritable(int size) {
        return source.isWritable(size);
    }

    @Override
    public final int refCnt() {
        return source.refCnt();
    }

    @Override
    public boolean release() {
        return source.release();
    }

    @Override
    public boolean release(int decrement) {
        return source.release(decrement);
    }

}
