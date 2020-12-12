/**
 * Copyright 2020 J&#246;rgen Lundgren
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

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.Arrays;

//TODO: Add Javadocs!
public final class DoubleArrayOutputStream extends OutputStream {
	private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private double[] buffer;
	private int size;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public DoubleArrayOutputStream() {
		this(32);
	}
	
//	TODO: Add Javadocs!
	public DoubleArrayOutputStream(final int size) {
		this.buffer = new double[ParameterArguments.requireRange(size, 0, Integer.MAX_VALUE, "size")];
		this.size = 0;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public synchronized double[] toDoubleArray() {
		return Arrays.copyOf(this.buffer, this.size);
	}
	
//	TODO: Add Javadocs!
	public synchronized int size() {
		return this.size;
	}
	
//	TODO: Add Javadocs!
	@Override
	public void close() {
		
	}
	
//	TODO: Add Javadocs!
	public synchronized void reset() {
		this.size = 0;
	}
	
//	TODO: Add Javadocs!
	public synchronized void write(final double[] d) {
		write(d, 0, d.length);
	}
	
//	TODO: Add Javadocs!
	public synchronized void write(final double[] d, final int off, final int len) {
//		Similar to ByteArrayOutputStream. It looks like 'off > d.length' is a bug. Should it not be 'off >= d.length'?
		if(off < 0 || off > d.length || len < 0 || off + len - d.length > 0) {
			throw new IndexOutOfBoundsException();
		}
		
		doEnsureCapacity(this.size + len);
		
		System.arraycopy(d, off, this.buffer, this.size, len);
		
		this.size += len;
	}
	
//	TODO: Add Javadocs!
	public synchronized void write(final double d) {
		doEnsureCapacity(this.size + 1);
		
		this.buffer[this.size] = d;
		this.size += 1;
	}
	
//	TODO: Add Javadocs!
	@Override
	public synchronized void write(final int b) throws IOException {
		write((double)(b));
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