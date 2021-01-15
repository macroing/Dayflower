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
 * A {@code FloatArrayOutputStream} is an {@code OutputStream} implementation that writes data to a {@code float} array.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class FloatArrayOutputStream extends OutputStream {
	private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private float[] buffer;
	private int size;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code FloatArrayOutputStream} instance with an initial capacity of {@code 32}.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new FloatArrayOutputStream(32);
	 * }
	 * </pre>
	 */
	public FloatArrayOutputStream() {
		this(32);
	}
	
	/**
	 * Constructs a new {@code FloatArrayOutputStream} instance with an initial capacity of {@code capacity}.
	 * <p>
	 * If {@code capacity} is less than {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param capacity the initial capacity
	 * @throws IllegalArgumentException thrown if, and only if, {@code capacity} is less than {@code 0}
	 */
	public FloatArrayOutputStream(final int capacity) {
		this.buffer = new float[ParameterArguments.requireRange(capacity, 0, Integer.MAX_VALUE, "capacity")];
		this.size = 0;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code float} array that contains the {@code float} values that have been written so far.
	 * 
	 * @return a {@code float} array that contains the {@code float} values that have been written so far
	 */
	public synchronized float[] toFloatArray() {
		return Arrays.copyOf(this.buffer, this.size);
	}
	
	/**
	 * Returns the number of {@code float} values that can be written without expanding the current {@code float} array.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * int available = floatArrayOutputStream.capacity() - floatArrayOutputStream.size();
	 * }
	 * </pre>
	 * 
	 * @return the number of {@code float} values that can be written without expanding the current {@code float} array
	 */
	public synchronized int available() {
		return capacity() - size();
	}
	
	/**
	 * Returns the capacity of this {@code FloatArrayOutputStream} instance.
	 * <p>
	 * The capacity represents the length of the current {@code float} array.
	 * 
	 * @return the capacity of this {@code FloatArrayOutputStream} instance
	 */
	public synchronized int capacity() {
		return this.buffer.length;
	}
	
	/**
	 * Returns the size of this {@code FloatArrayOutputStream} instance.
	 * <p>
	 * The size represents the number of {@code float} values that have been written so far.
	 * 
	 * @return the size of this {@code FloatArrayOutputStream} instance
	 */
	public synchronized int size() {
		return this.size;
	}
	
	/**
	 * Closing a {@code FloatArrayOutputStream} instance has no effect.
	 * <p>
	 * The methods in this class can be called after the stream has been closed without generating an {@code IOException}.
	 */
	@Override
	public void close() {
		
	}
	
	/**
	 * Resets this {@code FloatArrayOutputStream} instance so that the size is {@code 0}.
	 */
	public synchronized void reset() {
		this.size = 0;
	}
	
	/**
	 * Writes {@code f.length} {@code float} values, starting at offset {@code 0}, from {@code f} to this {@code FloatArrayOutputStream} instance.
	 * <p>
	 * If {@code f} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * floatArrayOutputStream.write(f, 0, f.length);
	 * }
	 * </pre>
	 * 
	 * @param f the {@code float} array to write from
	 * @throws NullPointerException thrown if, and only if, {@code f} is {@code null}
	 */
	public synchronized void write(final float[] f) {
		write(f, 0, f.length);
	}
	
	/**
	 * Writes {@code len} {@code float} values, starting at offset {@code off}, from {@code f} to this {@code FloatArrayOutputStream} instance.
	 * <p>
	 * If {@code f} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If either {@code off < 0}, {@code off > f.length}, {@code len < 0} or {@code off + len - f.length > 0}, an {@code IndexOutOfBoundsException} will be thrown.
	 * 
	 * @param f the {@code float} array to write from
	 * @param off the offset to start at in {@code f}
	 * @param len the number of {@code float} values to write from {@code f}
	 * @throws IndexOutOfBoundsException thrown if, and only if, either {@code off < 0}, {@code off > f.length}, {@code len < 0} or {@code off + len - f.length > 0}
	 * @throws NullPointerException thrown if, and only if, {@code f} is {@code null}
	 */
	public synchronized void write(final float[] f, final int off, final int len) {
//		Similar to ByteArrayOutputStream. It looks like 'off > f.length' is a bug. Should it not be 'off >= f.length'?
		if(off < 0 || off > f.length || len < 0 || off + len - f.length > 0) {
			throw new IndexOutOfBoundsException();
		}
		
		doEnsureCapacity(this.size + len);
		
		System.arraycopy(f, off, this.buffer, this.size, len);
		
		this.size += len;
	}
	
	/**
	 * Writes the specified {@code float} value to this {@code FloatArrayOutputStream} instance.
	 * 
	 * @param f the {@code float} value to write
	 */
	public synchronized void write(final float f) {
		doEnsureCapacity(this.size + 1);
		
		this.buffer[this.size] = f;
		this.size += 1;
	}
	
	/**
	 * Writes the specified {@code byte} value to this {@code FloatArrayOutputStream} instance.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * floatArrayOutputStream.write((float)(b));
	 * }
	 * </pre>
	 * 
	 * @param b the {@code byte} value to write
	 */
	@Override
	public synchronized void write(final int b) {
		write((float)(b));
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