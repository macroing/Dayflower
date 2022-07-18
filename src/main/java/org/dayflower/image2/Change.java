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
package org.dayflower.image2;

/**
 * A {@code Change} represents a change that can be undone and redone.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public interface Change {
	/**
	 * Executes the redo operation that is associated with this {@code Change} instance for {@code data}.
	 * <p>
	 * If {@code data} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param data the {@link Data} instance for which the redo operation is executed
	 * @throws NullPointerException thrown if, and only if, {@code data} is {@code null}
	 */
	void redo(final Data data);
	
	/**
	 * Executes the undo operation that is associated with this {@code Change} instance for {@code data}.
	 * <p>
	 * If {@code data} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param data the {@link Data} instance for which the undo operation is executed
	 * @throws NullPointerException thrown if, and only if, {@code data} is {@code null}
	 */
	void undo(final Data data);
}