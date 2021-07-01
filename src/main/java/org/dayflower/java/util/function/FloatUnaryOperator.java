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

/**
 * Represents an operation on a single {@code float}-valued operand that produces a {@code float}-valued result.
 * <p>
 * This is the primitive type specialization of {@code UnaryOperator} for {@code float}.
 * <p>
 * This is a functional interface whose functional method is {@link #applyAsFloat(float)}.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
@FunctionalInterface
public interface FloatUnaryOperator {
	/**
	 * Applies this operator to the given operand.
	 * <p>
	 * Returns the operator result.
	 * 
	 * @param operand the operand
	 * @return the operator result
	 */
	float applyAsFloat(final float operand);
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a composed operator that first applies this operator to its input, and then applies the {@code after} operator to the result.
	 * <p>
	 * If {@code after} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If evaluation of either operator throws an exception, it is relayed to the caller of the composed operator.
	 * 
	 * @param after the operator to apply after this operator is applied
	 * @return a composed operator that first applies this operator to its input, and then applies the {@code after} operator to the result
	 * @throws NullPointerException thrown if, and only if, {@code after} is {@code null}
	 */
	default FloatUnaryOperator andThen(final FloatUnaryOperator after) {
		Objects.requireNonNull(after, "after == null");
		
		return (float v) -> after.applyAsFloat(applyAsFloat(v));
	}
	
	/**
	 * Returns a composed operator that first applies the {@code before} operator to its input, and then applies this operator to the result.
	 * <p>
	 * If {@code before} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If evaluation of either operator throws an exception, it is relayed to the caller of the composed operator.
	 * 
	 * @param before the operator to apply before this operator is applied
	 * @return a composed operator that first applies the {@code before} operator to its input, and then applies this operator to the result
	 * @throws NullPointerException thrown if, and only if, {@code before} is {@code null}
	 */
	default FloatUnaryOperator compose(final FloatUnaryOperator before) {
		Objects.requireNonNull(before, "before == null");
		
		return (float v) -> applyAsFloat(before.applyAsFloat(v));
	}
	
	/**
	 * Returns a unary operator that always returns its input argument.
	 * 
	 * @return a unary operator that always returns its input argument
	 */
	default FloatUnaryOperator identity() {
		return t -> t;
	}
}