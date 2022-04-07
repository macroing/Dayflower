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
package org.dayflower.utility;

import java.lang.reflect.Field;//TODO: Add Unit Tests!

/**
 * A {@code CircularBuffer} is an implementation of a data structure called a circular buffer.
 * <p>
 * This class is not thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class CircularBuffer {
	private double[] buffer;
	private int readOffset;
	private int size;
	private int writeOffset;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code CircularBuffer} instance given its capacity.
	 * <p>
	 * If {@code capacity} is less than {@code 1}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param capacity the capacity of this {@code CircularBuffer} instance
	 * @throws IllegalArgumentException thrown if, and only if, {@code capacity} is less than {@code 1}
	 */
//	TODO: Add Unit Tests!
	public CircularBuffer(final int capacity) {
		this.buffer = new double[ParameterArguments.requireRange(capacity, 1, Integer.MAX_VALUE, "capacity")];
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private CircularBuffer(final double[] buffer, final int readOffset, final int size, final int writeOffset) {
		this.buffer = buffer;
		this.readOffset = readOffset;
		this.size = size;
		this.writeOffset = writeOffset;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a copy of this {@code CircularBuffer} instance.
	 * 
	 * @return a copy of this {@code CircularBuffer} instance
	 */
//	TODO: Add Unit Tests!
	public CircularBuffer copy() {
		return new CircularBuffer(this.buffer.clone(), this.readOffset, this.size, this.writeOffset);
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code CircularBuffer} instance is empty, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, this {@code CircularBuffer} instance is empty, {@code false} otherwise
	 */
//	TODO: Add Unit Tests!
	public boolean isEmpty() {
		return this.size == 0;
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code CircularBuffer} instance is full, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, this {@code CircularBuffer} instance is full, {@code false} otherwise
	 */
//	TODO: Add Unit Tests!
	public boolean isFull() {
		return this.size == this.buffer.length;
	}
	
	/**
	 * Performs a peek operation on this {@code CircularBuffer} instance.
	 * <p>
	 * Returns the peeked value.
	 * <p>
	 * If {@code isEmpty()} returns {@code true}, a {@link CircularBufferException} will be thrown.
	 * 
	 * @return the peeked value
	 * @throws CircularBufferException thrown if, and only if, {@code isEmpty()} returns {@code true}
	 */
//	TODO: Add Unit Tests!
	public double peek() {
		if(isEmpty()) {
			throw new CircularBufferException("peek() cannot be called while isEmpty() returns true");
		}
		
		if(this.readOffset >= this.buffer.length) {
			this.readOffset = 0;
		}
		
		return this.buffer[this.readOffset];
	}
	
	/**
	 * Performs a pop operation on this {@code CircularBuffer} instance.
	 * <p>
	 * Returns the popped value.
	 * <p>
	 * If {@code isEmpty()} returns {@code true}, a {@link CircularBufferException} will be thrown.
	 * 
	 * @return the popped value
	 * @throws CircularBufferException thrown if, and only if, {@code isEmpty()} returns {@code true}
	 */
//	TODO: Add Unit Tests!
	public double pop() {
		if(isEmpty()) {
			throw new CircularBufferException("pop() cannot be called while isEmpty() returns true");
		}
		
		this.readOffset %= this.buffer.length;
		
		final double value = this.buffer[this.readOffset];
		
		this.buffer[this.readOffset++] = 0.0D;
		
		this.size--;
		
		return value;
	}
	
	/**
	 * Returns the size of this {@code CircularBuffer} instance.
	 * 
	 * @return the size of this {@code CircularBuffer} instance
	 */
//	TODO: Add Unit Tests!
	public int size() {
		return this.size;
	}
	
	/**
	 * Performs a push operation on this {@code CircularBuffer} instance.
	 * <p>
	 * If {@code isFull()} returns {@code true}, a {@link CircularBufferException} will be thrown.
	 * 
	 * @param value the value to push
	 * @throws CircularBufferException thrown if, and only if, {@code isFull()} returns {@code true}
	 */
//	TODO: Add Unit Tests!
	public void push(final double value) {
		if(isFull()) {
			throw new CircularBufferException("push(double) cannot be called while isFull() returns true");
		}
		
		this.writeOffset %= this.buffer.length;
		
		this.buffer[this.writeOffset++] = value;
		
		this.size++;
	}
}