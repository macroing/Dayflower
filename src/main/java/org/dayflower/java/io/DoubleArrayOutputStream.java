/**
 * Copyright 2014 - 2021 J&#246;rgen Lundgren
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
import java.util.Arrays;

/**
 * A {@code DoubleArrayOutputStream} is an {@code OutputStream} implementation that writes data to a {@code double} array.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class DoubleArrayOutputStream extends OutputStream {
	private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private double[] buffer;
	private int size;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code DoubleArrayOutputStream} instance with an initial capacity of {@code 32}.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new DoubleArrayOutputStream(32);
	 * }
	 * </pre>
	 */
	public DoubleArrayOutputStream() {
		this(32);
	}
	
	/**
	 * Constructs a new {@code DoubleArrayOutputStream} instance with an initial capacity of {@code capacity}.
	 * <p>
	 * If {@code capacity} is less than {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param capacity the initial capacity
	 * @throws IllegalArgumentException thrown if, and only if, {@code capacity} is less than {@code 0}
	 */
	public DoubleArrayOutputStream(final int capacity) {
		this.buffer = new double[doRequireRange(capacity, 0, Integer.MAX_VALUE, "capacity")];
		this.size = 0;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code double} array that contains the {@code double} values that have been written so far.
	 * 
	 * @return a {@code double} array that contains the {@code double} values that have been written so far
	 */
	public synchronized double[] toDoubleArray() {
		return Arrays.copyOf(this.buffer, this.size);
	}
	
	/**
	 * Returns the number of {@code double} values that can be written without expanding the current {@code double} array.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * int available = doubleArrayOutputStream.capacity() - doubleArrayOutputStream.size();
	 * }
	 * </pre>
	 * 
	 * @return the number of {@code double} values that can be written without expanding the current {@code double} array
	 */
	public synchronized int available() {
		return capacity() - size();
	}
	
	/**
	 * Returns the capacity of this {@code DoubleArrayOutputStream} instance.
	 * <p>
	 * The capacity represents the length of the current {@code double} array.
	 * 
	 * @return the capacity of this {@code DoubleArrayOutputStream} instance
	 */
	public synchronized int capacity() {
		return this.buffer.length;
	}
	
	/**
	 * Returns the size of this {@code DoubleArrayOutputStream} instance.
	 * <p>
	 * The size represents the number of {@code double} values that have been written so far.
	 * 
	 * @return the size of this {@code DoubleArrayOutputStream} instance
	 */
	public synchronized int size() {
		return this.size;
	}
	
	/**
	 * Closing a {@code DoubleArrayOutputStream} instance has no effect.
	 * <p>
	 * The methods in this class can be called after the stream has been closed without generating an {@code IOException}.
	 */
	@Override
	public void close() {
		
	}
	
	/**
	 * Resets this {@code DoubleArrayOutputStream} instance so that the size is {@code 0}.
	 */
	public synchronized void reset() {
		this.size = 0;
	}
	
	/**
	 * Writes the specified {@code double} value to this {@code DoubleArrayOutputStream} instance.
	 * 
	 * @param d the {@code double} value to write
	 */
	public synchronized void write(final double d) {
		doEnsureCapacity(this.size + 1);
		
		this.buffer[this.size] = d;
		this.size += 1;
	}
	
	/**
	 * Writes {@code d.length} {@code double} values, starting at offset {@code 0}, from {@code d} to this {@code DoubleArrayOutputStream} instance.
	 * <p>
	 * If {@code d} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * doubleArrayOutputStream.write(d, 0, d.length);
	 * }
	 * </pre>
	 * 
	 * @param d the {@code double} array to write from
	 * @throws NullPointerException thrown if, and only if, {@code d} is {@code null}
	 */
	public synchronized void write(final double[] d) {
		write(d, 0, d.length);
	}
	
	/**
	 * Writes {@code len} {@code double} values, starting at offset {@code off}, from {@code d} to this {@code DoubleArrayOutputStream} instance.
	 * <p>
	 * If {@code d} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If either {@code off < 0}, {@code off > d.length}, {@code len < 0} or {@code off + len - d.length > 0}, an {@code IndexOutOfBoundsException} will be thrown.
	 * 
	 * @param d the {@code double} array to write from
	 * @param off the offset to start at in {@code d}
	 * @param len the number of {@code double} values to write from {@code d}
	 * @throws IndexOutOfBoundsException thrown if, and only if, either {@code off < 0}, {@code off > d.length}, {@code len < 0} or {@code off + len - d.length > 0}
	 * @throws NullPointerException thrown if, and only if, {@code d} is {@code null}
	 */
	public synchronized void write(final double[] d, final int off, final int len) {
//		Similar to ByteArrayOutputStream. It looks like 'off > d.length' is a bug. Should it not be 'off >= d.length'?
		if(off < 0 || off > d.length || len < 0 || off + len - d.length > 0) {
			throw new IndexOutOfBoundsException();
		}
		
		doEnsureCapacity(this.size + len);
		
		System.arraycopy(d, off, this.buffer, this.size, len);
		
		this.size += len;
	}
	
	/**
	 * Writes the specified {@code byte} value to this {@code DoubleArrayOutputStream} instance.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * doubleArrayOutputStream.write((double)((byte)(b)));
	 * }
	 * </pre>
	 * 
	 * @param b the {@code byte} value to write
	 */
	@Override
	public synchronized void write(final int b) {
		write((double)((byte)(b)));
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