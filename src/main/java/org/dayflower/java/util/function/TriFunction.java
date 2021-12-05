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
package org.dayflower.java.util.function;

import java.util.Objects;
import java.util.function.Function;

/**
 * Represents a function that accepts three arguments and produces a result.
 * <p>
 * This is the three-arity specialization of {@code Function}.
 * <p>
 * This is a functional interface whose functional method is {@link #apply(Object, Object, Object)}.
 * 
 * @param <T> the type of the first argument to the function
 * @param <U> the type of the second argument to the function
 * @param <V> the type of the third argument to the function
 * @param <R> the type of the result of the function
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
@FunctionalInterface
public interface TriFunction<T, U, V, R> {
	/**
	 * Applies this function to the given arguments.
	 * <p>
	 * Returns the function result.
	 * 
	 * @param t the first function argument
	 * @param u the second function argument
	 * @param v the third function argument
	 * @return the function result
	 */
	R apply(final T t, final U u, final V v);
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a composed function that first applies this function to its input, and then applies the {@code after} function to the result.
	 * <p>
	 * If {@code after} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If evaluation of either function throws a {@code RuntimeException}, it is relayed to the caller of the composed function.
	 * 
	 * @param <W> the type of output of the {@code after} function, and of the composed function
	 * @param after the function to apply after this function is applied
	 * @return a composed function that first applies this function to its input, and then applies the {@code after} function to the result
	 * @throws NullPointerException thrown if, and only if, {@code after} is {@code null}
	 */
	default <W> TriFunction<T, U, V, W> andThen(final Function<? super R, ? extends W> after) {
		Objects.requireNonNull(after, "after == null");
		
		return (T t, U u, V v) -> after.apply(apply(t, u, v));
	}
}