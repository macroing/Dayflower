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
package org.dayflower.utility;

import java.lang.reflect.Field;
import java.util.Objects;

//TODO: Add Javadocs!
public interface FloatUnaryOperator {
//	TODO: Add Javadocs!
	float applyAsFloat(final float operand);
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	default FloatUnaryOperator andThen(final FloatUnaryOperator after) {
		Objects.requireNonNull(after, "after == null");
		
		return (float v) -> after.applyAsFloat(applyAsFloat(v));
	}
	
//	TODO: Add Javadocs!
	default FloatUnaryOperator compose(final FloatUnaryOperator before) {
		Objects.requireNonNull(before, "before == null");
		
		return (float v) -> applyAsFloat(before.applyAsFloat(v));
	}
	
//	TODO: Add Javadocs!
	default FloatUnaryOperator identity() {
		return t -> t;
	}
}