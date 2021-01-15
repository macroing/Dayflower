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
package org.dayflower.util;

import java.io.OutputStream;
import java.util.Arrays;

/**
 * An {@code IntArrayOutputStream} is an {@code OutputStream} implementation that writes data to an {@code int} array.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class IntArrayOutputStream extends OutputStream {
	private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private int size;
	private int[] buffer;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code IntArrayOutputStream} instance with an initial capacity of {@code 32}.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new IntArrayOutputStream(32);
	 * }
	 * </pre>
	 */
	public IntArrayOutputStream() {
		this(32);
	}
	
	/**
	 * Constructs a new {@code IntArrayOutputStream} instance with an initial capacity of {@code capacity}.
	 * <p>
	 * If {@code capacity} is less than {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param capacity the initial capacity
	 * @throws IllegalArgumentException thrown if, and only if, {@code capacity} is less than {@code 0}
	 */
	public IntArrayOutputStream(final int capacity) {
		this.size = 0;
		this.buffer = new int[ParameterArguments.requireRange(capacity, 0, Integer.MAX_VALUE, "capacity")];
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns an {@code int} array that contains the {@code int} values that have been written so far.
	 * 
	 * @return an {@code int} array that contains the {@code int} values that have been written so far
	 */
	public synchronized int[] toIntArray() {
		return Arrays.copyOf(this.buffer, this.size);
	}
	
	/**
	 * Returns the number of {@code int} values that can be written without expanding the current {@code int} array.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * int available = intArrayOutputStream.capacity() - intArrayOutputStream.size();
	 * }
	 * </pre>
	 * 
	 * @return the number of {@code int} values that can be written without expanding the current {@code int} array
	 */
	public synchronized int available() {
		return capacity() - size();
	}
	
	/**
	 * Returns the capacity of this {@code IntArrayOutputStream} instance.
	 * <p>
	 * The capacity represents the length of the current {@code int} array.
	 * 
	 * @return the capacity of this {@code IntArrayOutputStream} instance
	 */
	public synchronized int capacity() {
		return this.buffer.length;
	}
	
	/**
	 * Returns the size of this {@code IntArrayOutputStream} instance.
	 * <p>
	 * The size represents the number of {@code int} values that have been written so far.
	 * 
	 * @return the size of this {@code IntArrayOutputStream} instance
	 */
	public synchronized int size() {
		return this.size;
	}
	
	/**
	 * Closing an {@code IntArrayOutputStream} instance has no effect.
	 * <p>
	 * The methods in this class can be called after the stream has been closed without generating an {@code IOException}.
	 */
	@Override
	public void close() {
		
	}
	
	/**
	 * Resets this {@code IntArrayOutputStream} instance so that the size is {@code 0}.
	 */
	public synchronized void reset() {
		this.size = 0;
	}
	
	/**
	 * Writes {@code i.length} {@code int} values, starting at offset {@code 0}, from {@code i} to this {@code IntArrayOutputStream} instance.
	 * <p>
	 * If {@code i} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * intArrayOutputStream.write(i, 0, i.length);
	 * }
	 * </pre>
	 * 
	 * @param i the {@code int} array to write from
	 * @throws NullPointerException thrown if, and only if, {@code i} is {@code null}
	 */
	public synchronized void write(final int[] i) {
		write(i, 0, i.length);
	}
	
	/**
	 * Writes {@code len} {@code int} values, starting at offset {@code off}, from {@code i} to this {@code IntArrayOutputStream} instance.
	 * <p>
	 * If {@code i} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If either {@code off < 0}, {@code off > i.length}, {@code len < 0} or {@code off + len - i.length > 0}, an {@code IndexOutOfBoundsException} will be thrown.
	 * 
	 * @param i the {@code int} array to write from
	 * @param off the offset to start at in {@code i}
	 * @param len the number of {@code int} values to write from {@code i}
	 * @throws IndexOutOfBoundsException thrown if, and only if, either {@code off < 0}, {@code off > i.length}, {@code len < 0} or {@code off + len - i.length > 0}
	 * @throws NullPointerException thrown if, and only if, {@code i} is {@code null}
	 */
	public synchronized void write(final int[] i, final int off, final int len) {
//		Similar to ByteArrayOutputStream. It looks like 'off > i.length' is a bug. Should it not be 'off >= i.length'?
		if(off < 0 || off > i.length || len < 0 || off + len - i.length > 0) {
			throw new IndexOutOfBoundsException();
		}
		
		doEnsureCapacity(this.size + len);
		
		System.arraycopy(i, off, this.buffer, this.size, len);
		
		this.size += len;
	}
	
	/**
	 * Writes the specified {@code int} value to this {@code IntArrayOutputStream} instance.
	 * 
	 * @param i the {@code int} value to write
	 */
	@Override
	public synchronized void write(final int i) {
		doEnsureCapacity(this.size + 1);
		
		this.buffer[this.size] = i;
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
}