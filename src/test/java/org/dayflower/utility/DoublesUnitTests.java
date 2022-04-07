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

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

@SuppressWarnings("static-method")
public final class DoublesUnitTests {
	public DoublesUnitTests() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Test
	public void testAbs() {
		assertEquals(1.0D, Doubles.abs(+1.0D));
		assertEquals(1.0D, Doubles.abs(-1.0D));
	}
	
	@Test
	public void testAcos() {
		assertEquals(Math.acos(0.5D), Doubles.acos(0.5D));
	}
	
	@Test
	public void testAsin() {
		assertEquals(Math.asin(0.5D), Doubles.asin(0.5D));
	}
	
	@Test
	public void testAsinpi() {
		assertEquals(Math.asin(0.5D) / Math.PI, Doubles.asinpi(0.5D));
	}
	
	@Test
	public void testAtan() {
		assertEquals(Math.atan(0.5D), Doubles.atan(0.5D));
	}
	
	@Test
	public void testAtan2() {
		assertEquals(Math.atan2(0.5D, 0.5D), Doubles.atan2(0.5D, 0.5D));
	}
	
	@Test
	public void testAtan2pi2() {
		assertEquals(Math.atan2(0.5D, 0.5D) / (Math.PI * 2.0D), Doubles.atan2pi2(0.5D, 0.5D));
	}
	
	@Test
	public void testBlerp() {
		assertEquals(0.5D, Doubles.blerp(0.0D, 1.0D, 0.0D, 1.0D, 0.5D, 0.5D));
		assertEquals(1.5D, Doubles.blerp(0.0D, 1.0D, 2.0D, 3.0D, 0.5D, 0.5D));
		assertEquals(0.5D, Doubles.blerp(0.0D, 1.0D, 2.0D, 3.0D, 0.5D, 0.0D));
		assertEquals(2.5D, Doubles.blerp(0.0D, 1.0D, 2.0D, 3.0D, 0.5D, 1.0D));
	}
	
	@Test
	public void testCeil() {
		assertEquals(Math.ceil(0.5D), Doubles.ceil(0.5D));
	}
	
	@Test
	public void testConstants() {
		assertEquals(Math.ulp(1.0D) * 0.5D, Doubles.MACHINE_EPSILON);
		assertEquals(+Double.MAX_VALUE, Doubles.MAX_VALUE);
		assertEquals(-Double.MAX_VALUE, Doubles.MIN_VALUE);
		assertEquals(Math.nextDown(1.0D), Doubles.NEXT_DOWN_1_1);
		assertEquals(Math.nextDown(Math.nextDown(1.0D)), Doubles.NEXT_DOWN_1_2);
		assertEquals(Math.nextDown(Math.nextDown(Math.nextDown(1.0D))), Doubles.NEXT_DOWN_1_3);
		assertEquals(Math.nextUp(1.0D), Doubles.NEXT_UP_1_1);
		assertEquals(Math.nextUp(Math.nextUp(1.0D)), Doubles.NEXT_UP_1_2);
		assertEquals(Math.nextUp(Math.nextUp(Math.nextUp(1.0D))), Doubles.NEXT_UP_1_3);
		assertEquals(Double.NaN, Doubles.NaN);
		assertEquals(Math.PI, Doubles.PI);
		assertEquals(Math.PI / 180.0D, Doubles.PI_DIVIDED_BY_180);
		assertEquals(Math.PI / 2.0D, Doubles.PI_DIVIDED_BY_2);
		assertEquals(Math.PI / 4.0D, Doubles.PI_DIVIDED_BY_4);
		assertEquals(Math.PI * 2.0D, Doubles.PI_MULTIPLIED_BY_2);
		assertEquals(1.0D / (Math.PI * 2.0D), Doubles.PI_MULTIPLIED_BY_2_RECIPROCAL);
		assertEquals(Math.PI * 4.0D, Doubles.PI_MULTIPLIED_BY_4);
		assertEquals(1.0D / (Math.PI * 4.0D), Doubles.PI_MULTIPLIED_BY_4_RECIPROCAL);
		assertEquals(1.0D / Math.PI, Doubles.PI_RECIPROCAL);
	}
	
	@Test
	public void testCos() {
		assertEquals(Math.cos(0.5D), Doubles.cos(0.5D));
	}
	
	@Test
	public void testEqualDoubleDouble() {
		assertTrue(Doubles.equal(1.0D, 1.0D));
		
		assertFalse(Doubles.equal(1.0D, 2.0D));
	}
	
	@Test
	public void testEqualDoubleDoubleDouble() {
		assertTrue(Doubles.equal(1.0D, 1.0D, 1.0D));
		
		assertFalse(Doubles.equal(1.0D, 1.0D, 2.0D));
		assertFalse(Doubles.equal(1.0D, 2.0D, 1.0D));
		assertFalse(Doubles.equal(2.0D, 1.0D, 1.0D));
	}
	
	@Test
	public void testExp() {
		assertEquals(Math.exp(0.5D), Doubles.exp(0.5D));
	}
	
	@Test
	public void testFiniteOrDefault() {
		assertEquals(0.0D, Doubles.finiteOrDefault(Double.NaN, 0.0D));
		assertEquals(0.0D, Doubles.finiteOrDefault(Double.NEGATIVE_INFINITY, 0.0D));
		assertEquals(0.0D, Doubles.finiteOrDefault(Double.POSITIVE_INFINITY, 0.0D));
		assertEquals(0.0D, Doubles.finiteOrDefault(0.0D, 1.0D));
	}
	
	@Test
	public void testFloor() {
		assertEquals(Math.floor(0.5D), Doubles.floor(0.5D));
	}
	
	@Test
	public void testFractionalPartDouble() {
		assertEquals(0.5D, Doubles.fractionalPart(-1.5D));
		assertEquals(0.8D, Doubles.fractionalPart(-1.2D));
		assertEquals(0.5D, Doubles.fractionalPart(+1.5D));
		assertEquals(0.8D, Doubles.fractionalPart(+1.8D));
	}
	
	@Test
	public void testFractionalPartDoubleBoolean() {
		assertEquals(0.5D, Doubles.fractionalPart(-1.5D, false));
		assertEquals(0.5D, Doubles.fractionalPart(-1.5D, true));
		assertEquals(0.8D, Doubles.fractionalPart(-1.2D, false));
		assertEquals(0.8D, Doubles.fractionalPart(-1.8D, true));
		assertEquals(0.5D, Doubles.fractionalPart(+1.5D, false));
		assertEquals(0.5D, Doubles.fractionalPart(+1.5D, true));
		assertEquals(0.8D, Doubles.fractionalPart(+1.8D, false));
	}
	
	@Test
	public void testGamma() {
		assertEquals(0.0000000000000005551115123125786D, Doubles.gamma(5));
	}
	
	@Test
	public void testGetOrAdd() {
		assertEquals(1.0D, Doubles.getOrAdd(0.0D, 0.1D, 1.0D));
		assertEquals(1.0D, Doubles.getOrAdd(1.0D, 0.0D, 1.0D));
	}
	
	@Test
	public void testIsInfinite() {
		assertTrue(Doubles.isInfinite(Double.NEGATIVE_INFINITY));
		assertTrue(Doubles.isInfinite(Double.POSITIVE_INFINITY));
		
		assertFalse(Doubles.isInfinite(Double.NaN));
		assertFalse(Doubles.isInfinite(0.0D));
	}
	
	@Test
	public void testIsNaN() {
		assertTrue(Doubles.isNaN(Double.NaN));
		
		assertFalse(Doubles.isNaN(Double.NEGATIVE_INFINITY));
		assertFalse(Doubles.isNaN(Double.POSITIVE_INFINITY));
		assertFalse(Doubles.isNaN(0.0D));
	}
	
	@Test
	public void testIsZero() {
		assertTrue(Doubles.isZero(+0.0D));
		assertTrue(Doubles.isZero(-0.0D));
		
		assertFalse(Doubles.isZero(Double.NaN));
		assertFalse(Doubles.isZero(1.0D));
	}
	
	@Test
	public void testLerp() {
//		Interpolation:
		assertEquals(1.0D, Doubles.lerp(1.0D, 2.0D, +0.0D));
		assertEquals(1.5D, Doubles.lerp(1.0D, 2.0D, +0.5D));
		assertEquals(2.0D, Doubles.lerp(1.0D, 2.0D, +1.0D));
		
//		Extrapolation:
		assertEquals(0.0D, Doubles.lerp(1.0D, 2.0D, -1.0D));
		assertEquals(3.0D, Doubles.lerp(1.0D, 2.0D, +2.0D));
	}
	
	@Test
	public void testLog() {
		assertEquals(Math.log(0.5D), Doubles.log(0.5D));
	}
	
	@Test
	public void testMaxDoubleDouble() {
		assertEquals(2.0D, Doubles.max(1.0D, 2.0D));
	}
	
	@Test
	public void testMaxDoubleDoubleDouble() {
		assertEquals(3.0D, Doubles.max(1.0D, 2.0D, 3.0D));
	}
	
	@Test
	public void testMaxDoubleDoubleDoubleDouble() {
		assertEquals(4.0D, Doubles.max(1.0D, 2.0D, 3.0D, 4.0D));
	}
	
	@Test
	public void testMaxOrNaN() {
		assertEquals(2.0D, Doubles.maxOrNaN(1.0D, 2.0D));
		assertEquals(2.0D, Doubles.maxOrNaN(Double.NaN, 2.0D));
		assertEquals(2.0D, Doubles.maxOrNaN(2.0D, Double.NaN));
		assertEquals(Double.NaN, Doubles.maxOrNaN(Double.NaN, Double.NaN));
	}
	
	@Test
	public void testMinDoubleDouble() {
		assertEquals(1.0D, Doubles.min(1.0D, 2.0D));
	}
	
	@Test
	public void testMinDoubleDoubleDouble() {
		assertEquals(1.0D, Doubles.min(1.0D, 2.0D, 3.0D));
	}
	
	@Test
	public void testMinDoubleDoubleDoubleDouble() {
		assertEquals(1.0D, Doubles.min(1.0D, 2.0D, 3.0D, 4.0D));
	}
	
	@Test
	public void testMinOrNaN() {
		assertEquals(1.0D, Doubles.minOrNaN(1.0D, 2.0D));
		assertEquals(1.0D, Doubles.minOrNaN(Double.NaN, 1.0D));
		assertEquals(1.0D, Doubles.minOrNaN(1.0D, Double.NaN));
		assertEquals(Double.NaN, Doubles.minOrNaN(Double.NaN, Double.NaN));
	}
	
	@Test
	public void testNextDown() {
		assertEquals(Math.nextDown(0.5D), Doubles.nextDown(0.5D));
	}
	
	@Test
	public void testNextUp() {
		assertEquals(Math.nextUp(0.5D), Doubles.nextUp(0.5D));
	}
	
	@Test
	public void testNormalize() {
		assertEquals(-1.0D, Doubles.normalize(  0.0D, 100.0D, 200.0D));
		assertEquals(+0.0D, Doubles.normalize(100.0D, 100.0D, 200.0D));
		assertEquals(+0.5D, Doubles.normalize(150.0D, 100.0D, 200.0D));
		assertEquals(+1.0D, Doubles.normalize(200.0D, 100.0D, 200.0D));
		assertEquals(+2.0D, Doubles.normalize(300.0D, 100.0D, 200.0D));
	}
	
	@Test
	public void testPow() {
		assertEquals(Math.pow(2.0D, 2.0D), Doubles.pow(2.0D, 2.0D));
	}
	
	@Test
	public void testPow2() {
		assertEquals(4.0D, Doubles.pow2(2.0D));
	}
	
	@Test
	public void testPow5() {
		assertEquals(32.0D, Doubles.pow5(2.0D));
	}
	
	@Test
	public void testPowR() {
		assertEquals( 1.0D, Doubles.powR(2.0D, 0));
		assertEquals( 2.0D, Doubles.powR(2.0D, 1));
		assertEquals( 4.0D, Doubles.powR(2.0D, 2));
		assertEquals( 8.0D, Doubles.powR(2.0D, 3));
		assertEquals(16.0D, Doubles.powR(2.0D, 4));
	}
	
	@Test
	public void testRandom() {
		final double value = Doubles.random();
		
		assertTrue(value >= 0.0D);
		assertTrue(value <= 1.0D);
	}
	
	@Test
	public void testSaturateDouble() {
		assertEquals(0.0D, Doubles.saturate(-1.0D));
		assertEquals(0.5D, Doubles.saturate(+0.5D));
		assertEquals(1.0D, Doubles.saturate(+2.0D));
	}
	
	@Test
	public void testSaturateDoubleDoubleDouble() {
		assertEquals(2.0D, Doubles.saturate(1.0D, 2.0D, 3.0D));
		assertEquals(2.0D, Doubles.saturate(1.0D, 3.0D, 2.0D));
		
		assertEquals(2.5D, Doubles.saturate(2.5D, 2.0D, 3.0D));
		assertEquals(2.5D, Doubles.saturate(2.5D, 3.0D, 2.0D));
		
		assertEquals(3.0D, Doubles.saturate(4.0D, 2.0D, 3.0D));
		assertEquals(3.0D, Doubles.saturate(4.0D, 3.0D, 2.0D));
	}
	
	@Test
	public void testSin() {
		assertEquals(Math.sin(0.5D), Doubles.sin(0.5D));
	}
	
	@Test
	public void testSinh() {
		assertEquals(Math.sinh(0.5D), Doubles.sinh(0.5D));
	}
	
	@Test
	public void testSmoothstep() {
		assertEquals(+0.00000D, Doubles.smoothstep(  0.0D, 100.0D, 200.0D));
		assertEquals(+0.00000D, Doubles.smoothstep(100.0D, 100.0D, 200.0D));
		assertEquals(+0.15625D, Doubles.smoothstep(125.0D, 100.0D, 200.0D));
		assertEquals(+0.50000D, Doubles.smoothstep(150.0D, 100.0D, 200.0D));
		assertEquals(+1.00000D, Doubles.smoothstep(200.0D, 100.0D, 200.0D));
		assertEquals(+1.00000D, Doubles.smoothstep(300.0D, 100.0D, 200.0D));
	}
	
	@Test
	public void testSolveQuadraticSystem() {
		assertArrayEquals(new double[] {-2.0D, -0.3333333333333333D}, Doubles.solveQuadraticSystem(3.0D, +7.0D, 2.0D));
		assertArrayEquals(new double[] {+0.3333333333333333D, +2.0D}, Doubles.solveQuadraticSystem(3.0D, -7.0D, 2.0D));
		assertArrayEquals(new double[] {-1.0D, -1.0D}, Doubles.solveQuadraticSystem(1.0D, 2.0D, 1.0D));
	}
	
	@Test
	public void testSqrt() {
		assertEquals(Math.sqrt(0.5D), Doubles.sqrt(0.5D));
	}
	
	@Test
	public void testTan() {
		assertEquals(Math.tan(0.5D), Doubles.tan(0.5D));
	}
	
	@Test
	public void testToDegrees() {
		assertEquals(Math.toDegrees(0.5D), Doubles.toDegrees(0.5D));
	}
	
	@Test
	public void testToDoubleFloat() {
		assertEquals(1.0D, Doubles.toDouble(1.0F));
	}
	
	@Test
	public void testToDoubleInt() {
		assertEquals(1.0D, Doubles.toDouble(1));
	}
	
	@Test
	public void testToRadians() {
		assertEquals(Math.toRadians(0.5D), Doubles.toRadians(0.5D));
	}
	
	@Test
	public void testWrapAround() {
		assertEquals(+100.0D, Doubles.wrapAround(+100.0D, +100.0D, +300.0D));
		assertEquals(+200.0D, Doubles.wrapAround(+200.0D, +100.0D, +300.0D));
		assertEquals(+300.0D, Doubles.wrapAround(+300.0D, +100.0D, +300.0D));
		
		assertEquals(+100.0D, Doubles.wrapAround(+100.0D, +300.0D, +100.0D));
		assertEquals(+200.0D, Doubles.wrapAround(+200.0D, +300.0D, +100.0D));
		assertEquals(+300.0D, Doubles.wrapAround(+300.0D, +300.0D, +100.0D));
		
		assertEquals(+299.0D, Doubles.wrapAround(+ 99.0D, +100.0D, +300.0D));
		assertEquals(+101.0D, Doubles.wrapAround(+301.0D, +100.0D, +300.0D));
		
		assertEquals(-100.0D, Doubles.wrapAround(-100.0D, -300.0D, -100.0D));
		assertEquals(-200.0D, Doubles.wrapAround(-200.0D, -300.0D, -100.0D));
		assertEquals(-300.0D, Doubles.wrapAround(-300.0D, -300.0D, -100.0D));
		
		assertEquals(-100.0D, Doubles.wrapAround(-100.0D, -100.0D, -300.0D));
		assertEquals(-200.0D, Doubles.wrapAround(-200.0D, -100.0D, -300.0D));
		assertEquals(-300.0D, Doubles.wrapAround(-300.0D, -100.0D, -300.0D));
		
		assertEquals(-101.0D, Doubles.wrapAround(-301.0D, -300.0D, -100.0D));
		assertEquals(-299.0D, Doubles.wrapAround(- 99.0D, -300.0D, -100.0D));
	}
}