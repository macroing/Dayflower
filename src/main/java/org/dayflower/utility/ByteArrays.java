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

import static org.dayflower.utility.Bytes.toByte;

import java.util.Objects;

/**
 * A class that consists exclusively of static methods that returns or performs various operations on {@code byte[]} instances.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class ByteArrays {
	private ByteArrays() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code byte[]} representation of {@code intArray}.
	 * <p>
	 * If {@code intArray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param intArray an {@code int[]}
	 * @return a {@code byte[]} representation of {@code intArray}
	 * @throws NullPointerException thrown if, and only if, {@code intArray} is {@code null}
	 */
	public static byte[] convert(final int[] intArray) {
		Objects.requireNonNull(intArray, "intArray == null");
		
		final byte[] byteArray = new byte[intArray.length];
		
		for(int i = 0; i < intArray.length; i++) {
			byteArray[i] = toByte(intArray[i]);
		}
		
		return byteArray;
	}
}