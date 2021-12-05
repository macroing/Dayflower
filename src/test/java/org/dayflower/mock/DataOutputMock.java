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
package org.dayflower.mock;

import java.io.DataOutput;
import java.io.IOException;

public final class DataOutputMock implements DataOutput {
	public DataOutputMock() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public void write(final byte[] b) throws IOException {
		throw new IOException();
	}
	
	@Override
	public void write(final byte[] b, final int off, final int len) throws IOException {
		throw new IOException();
	}
	
	@Override
	public void write(final int b) throws IOException {
		throw new IOException();
	}
	
	@Override
	public void writeBoolean(final boolean v) throws IOException {
		throw new IOException();
	}
	
	@Override
	public void writeByte(final int v) throws IOException {
		throw new IOException();
	}
	
	@Override
	public void writeBytes(final String s) throws IOException {
		throw new IOException();
	}
	
	@Override
	public void writeChar(final int v) throws IOException {
		throw new IOException();
	}
	
	@Override
	public void writeChars(final String s) throws IOException {
		throw new IOException();
	}
	
	@Override
	public void writeDouble(final double v) throws IOException {
		throw new IOException();
	}
	
	@Override
	public void writeFloat(final float v) throws IOException {
		throw new IOException();
	}
	
	@Override
	public void writeInt(final int v) throws IOException {
		throw new IOException();
	}
	
	@Override
	public void writeLong(final long v) throws IOException {
		throw new IOException();
	}
	
	@Override
	public void writeShort(final int v) throws IOException {
		throw new IOException();
	}
	
	@Override
	public void writeUTF(final String s) throws IOException {
		throw new IOException();
	}
}