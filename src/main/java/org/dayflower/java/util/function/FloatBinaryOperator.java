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

/**
 * Represents an operation upon two {@code float}-valued operands and producing a {@code float}-valued result.
 * <p>
 * This is a functional interface whose functional method is {@link #applyAsFloat(float, float)}.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
@FunctionalInterface
public interface FloatBinaryOperator {
	/**
	 * Applies this operator to the given operands.
	 * <p>
	 * Returns the operator result.
	 * 
	 * @param left the first operand
	 * @param right the second operand
	 * @return the operator result
	 */
	float applyAsFloat(final float left, final float right);
}