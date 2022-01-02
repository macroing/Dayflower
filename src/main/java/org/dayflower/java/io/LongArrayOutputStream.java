/**
 * Copyright 2014 - 2022 J&#246;rgen Lundgren
 * 
 * This file is part of Dayflower.
 * 
 * Dayflower is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Dayflower is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with Dayflower. If not, see <http://www.gnu.org/licenses/>.
 */
package org.dayflower.java.io;

import java.io.OutputStream;
import java.lang.reflect.Field;//TODO: Add Unit Tests!
import java.util.Arrays;

/**
 * A {@code LongArrayOutputStream} is an {@code OutputStream} implementation that writes data to a {@code long} array.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class LongArrayOutputStream extends OutputStream {
	private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private int size;
	private long[] buffer;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code LongArrayOutputStream} instance with an initial capacity of {@code 32}.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new LongArrayOutputStream(32);
	 * }
	 * </pre>
	 */
//	TODO: Add Unit Tests!
	public LongArrayOutputStream() {
		this(32);
	}
	
	/**
	 * Constructs a new {@code LongArrayOutputStream} instance with an initial capacity of {@code capacity}.
	 * <p>
	 * If {@code capacity} is less than {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param capacity the initial capacity
	 * @throws IllegalArgumentException thrown if, and only if, {@code capacity} is less than {@code 0}
	 */
//	TODO: Add Unit Tests!
	public LongArrayOutputStream(final int capacity) {
		this.size = 0;
		this.buffer = new long[doRequireRange(capacity, 0, Integer.MAX_VALUE, "capacity")];
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the number of {@code long} values that can be written without expanding the current {@code long} array.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * int available = longArrayOutputStream.capacity() - longArrayOutputStream.size();
	 * }
	 * </pre>
	 * 
	 * @return the number of {@code long} values that can be written without expanding the current {@code long} array
	 */
//	TODO: Add Unit Tests!
	public synchronized int available() {
		return capacity() - size();
	}
	
	/**
	 * Returns the capacity of this {@code LongArrayOutputStream} instance.
	 * <p>
	 * The capacity represents the length of the current {@code long} array.
	 * 
	 * @return the capacity of this {@code LongArrayOutputStream} instance
	 */
//	TODO: Add Unit Tests!
	public synchronized int capacity() {
		return this.buffer.length;
	}
	
	/**
	 * Returns the size of this {@code LongArrayOutputStream} instance.
	 * <p>
	 * The size represents the number of {@code long} values that have been written so far.
	 * 
	 * @return the size of this {@code LongArrayOutputStream} instance
	 */
//	TODO: Add Unit Tests!
	public synchronized int size() {
		return this.size;
	}
	
	/**
	 * Returns a {@code long} array that contains the {@code long} values that have been written so far.
	 * 
	 * @return a {@code long} array that contains the {@code long} values that have been written so far
	 */
//	TODO: Add Unit Tests!
	public synchronized long[] toLongArray() {
		return Arrays.copyOf(this.buffer, this.size);
	}
	
	/**
	 * Closing a {@code LongArrayOutputStream} instance has no effect.
	 * <p>
	 * The methods in this class can be called after the stream has been closed without generating an {@code IOException}.
	 */
//	TODO: Add Unit Tests!
	@Override
	public void close() {
		
	}
	
	/**
	 * Resets this {@code LongArrayOutputStream} instance so that the size is {@code 0}.
	 */
//	TODO: Add Unit Tests!
	public synchronized void reset() {
		this.size = 0;
	}
	
	/**
	 * Writes the specified {@code byte} value to this {@code LongArrayOutputStream} instance.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * longArrayOutputStream.writeLong((byte)(b));
	 * }
	 * </pre>
	 * 
	 * @param b the {@code byte} value to write
	 */
//	TODO: Add Unit Tests!
	@Override
	public synchronized void write(final int b) {
		writeLong((byte)(b));
	}
	
	/**
	 * Writes {@code l.length} {@code long} values, starting at offset {@code 0}, from {@code l} to this {@code LongArrayOutputStream} instance.
	 * <p>
	 * If {@code l} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * longArrayOutputStream.write(l, 0, l.length);
	 * }
	 * </pre>
	 * 
	 * @param l the {@code long} array to write from
	 * @throws NullPointerException thrown if, and only if, {@code l} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public synchronized void write(final long[] l) {
		write(l, 0, l.length);
	}
	
	/**
	 * Writes {@code len} {@code long} values, starting at offset {@code off}, from {@code l} to this {@code LongArrayOutputStream} instance.
	 * <p>
	 * If {@code l} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If either {@code off < 0}, {@code off > l.length}, {@code len < 0} or {@code off + len - l.length > 0}, an {@code IndexOutOfBoundsException} will be thrown.
	 * 
	 * @param l the {@code long} array to write from
	 * @param off the offset to start at in {@code l}
	 * @param len the number of {@code long} values to write from {@code l}
	 * @throws IndexOutOfBoundsException thrown if, and only if, either {@code off < 0}, {@code off > l.length}, {@code len < 0} or {@code off + len - l.length > 0}
	 * @throws NullPointerException thrown if, and only if, {@code l} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public synchronized void write(final long[] l, final int off, final int len) {
//		Similar to ByteArrayOutputStream. It looks like 'off > l.length' is a bug. Should it not be 'off >= l.length'?
		if(off < 0 || off > l.length || len < 0 || off + len - l.length > 0) {
			throw new IndexOutOfBoundsException();
		}
		
		doEnsureCapacity(this.size + len);
		
		System.arraycopy(l, off, this.buffer, this.size, len);
		
		this.size += len;
	}
	
	/**
	 * Writes the specified {@code long} value to this {@code LongArrayOutputStream} instance.
	 * 
	 * @param l the {@code long} value to write
	 */
//	TODO: Add Unit Tests!
	public synchronized void writeLong(final long l) {
		doEnsureCapacity(this.size + 1);
		
		this.buffer[this.size] = l;
		this.size += 1;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private void doEnsureCapacity(final int minCapacity) {
		if(minCapacity - this.buffer.length > 0) {
			doGrow(minCapacity);
		}
	}
	
	private void doGrow(final int minCapacity) {
		if(minCapacity < 0) {
			throw new OutOfMemoryError();
		}
		
		int oldCapacity = this.buffer.length;
		int newCapacity = oldCapacity << 1;
		
		if(newCapacity - minCapacity < 0) {
			newCapacity = minCapacity;
		}
		
		if(newCapacity - MAX_ARRAY_SIZE > 0) {
			newCapacity = minCapacity > MAX_ARRAY_SIZE ? Integer.MAX_VALUE : MAX_ARRAY_SIZE;
		}
		
		this.buffer = Arrays.copyOf(this.buffer, newCapacity);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static int doRequireRange(final int value, final int minimum, final int maximum, final String name) {
		if(value < minimum) {
			throw new IllegalArgumentException(String.format("%s < %d: %s == %d", name, Integer.valueOf(minimum), name, Integer.valueOf(value)));
		} else if(value > maximum) {
			throw new IllegalArgumentException(String.format("%s > %d: %s == %d", name, Integer.valueOf(maximum), name, Integer.valueOf(value)));
		} else {
			return value;
		}
	}
}