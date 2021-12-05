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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

@SuppressWarnings("static-method")
public final class TriFunctionUnitTests {
	public TriFunctionUnitTests() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Test
	public void testAndThen() {
		final TriFunction<Float, Float, Float, Float> a = (t, u, v) -> Float.valueOf(t.floatValue() + u.floatValue() + v.floatValue());
		final TriFunction<Float, Float, Float, Float> b = a.andThen(value -> Float.valueOf(value.floatValue() * 2.0F));
		
		final Float value = b.apply(Float.valueOf(1.0F), Float.valueOf(2.0F), Float.valueOf(3.0F));
		
		assertEquals(Float.valueOf(12.0F), value);
		
		assertThrows(NullPointerException.class, () -> a.andThen(null));
	}
}