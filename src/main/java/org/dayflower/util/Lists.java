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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * A class that consists exclusively of static methods that returns or performs various operations on {@code List} instances.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class Lists {
	private Lists() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code List} instance by merging the elements of the {@code List} instances in {@code lists}.
	 * <p>
	 * If either {@code lists}, at least one of its elements or at least one of their elements are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param lists an array of {@code List} instances
	 * @return a {@code List} instance by merging the elements of the {@code List} instances in {@code lists}
	 * @throws NullPointerException thrown if, and only if, either {@code lists}, at least one of its elements or at least one of their elements are {@code null}
	 */
	@SafeVarargs
	public static <T> List<T> merge(final List<? extends T>... lists) {
		return Stream.of(lists).flatMap(list -> list.stream()).filter(object -> Objects.requireNonNull(object) != null).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
	}
}