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
package org.dayflower.filter;

/**
 * A {@code FilterNB} represents an N-dimensional filter that operates on and returns {@code byte[]} values.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public interface FilterNB {
	/**
	 * Evaluates this {@code FilterNB} instance given {@code bytes}.
	 * <p>
	 * Returns the evaluated value.
	 * <p>
	 * If {@code bytes} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param bytes the {@code byte[]}
	 * @return the evaluated value
	 * @throws NullPointerException thrown if, and only if, {@code bytes} are {@code null}
	 */
	byte[] evaluate(final byte[] bytes);
}