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
package org.dayflower.filter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

@SuppressWarnings("static-method")
public final class MitchellFilter2DUnitTests {
	public MitchellFilter2DUnitTests() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Test
	public void testConstructor() {
		final MitchellFilter2D mitchellFilter2D = new MitchellFilter2D();
		
		assertEquals(2.0D, mitchellFilter2D.getResolutionX());
		assertEquals(2.0D, mitchellFilter2D.getResolutionY());
		
		assertEquals(0.5D, mitchellFilter2D.getResolutionXReciprocal());
		assertEquals(0.5D, mitchellFilter2D.getResolutionYReciprocal());
		
		assertEquals(0.3333333333333333D, mitchellFilter2D.getB());
		assertEquals(0.3333333333333333D, mitchellFilter2D.getC());
	}
	
	@Test
	public void testConstructorDoubleDoubleDoubleDouble() {
		final MitchellFilter2D mitchellFilter2D = new MitchellFilter2D(2.0D, 4.0D, 6.0D, 8.0D);
		
		assertEquals(2.0D, mitchellFilter2D.getResolutionX());
		assertEquals(4.0D, mitchellFilter2D.getResolutionY());
		
		assertEquals(0.50D, mitchellFilter2D.getResolutionXReciprocal());
		assertEquals(0.25D, mitchellFilter2D.getResolutionYReciprocal());
		
		assertEquals(6.0D, mitchellFilter2D.getB());
		assertEquals(8.0D, mitchellFilter2D.getC());
	}
	
	@Test
	public void testCreateFilterTable() {
		final MitchellFilter2D mitchellFilter2D = new MitchellFilter2D();
		
		final double[] filterTable = mitchellFilter2D.createFilterTable();
		
		assertNotNull(filterTable);
		
		assertEquals(16, Filter2D.FILTER_TABLE_SIZE);
		assertEquals(256, filterTable.length);
		
		for(int i = 0, y = 0; y < 16; y++) {
			for(int x = 0; x < 16; x++, i++) {
				final double filterX = (x + 0.5D) * 2.0D * 0.0625D;
				final double filterY = (y + 0.5D) * 2.0D * 0.0625D;
				
				final double b = 1.0D / 3.0D;
				final double c = 1.0D / 3.0D;
				
				final double x1 = Math.abs(filterX);
				final double x2 = x1 * x1;
				final double x3 = x1 * x2;
				
				final double y1 = Math.abs(filterY);
				final double y2 = y1 * y1;
				final double y3 = y1 * y2;
				
				final double expectedX = x1 > 1.0D ? ((-b - 6.0D * c) * x3 + (6.0D * b + 30.0D * c) * x2 + (-12.0D * b - 48.0D * c) * x1 + (8.0D * b + 24.0D * c)) * (1.0D / 6.0D) : ((12.0D - 9.0D * b - 6.0D * c) * x3 + (-18.0D + 12.0D * b + 6.0D * c) * x2 + (6.0D - 2.0D * b)) * (1.0D / 6.0D);
				final double expectedY = y1 > 1.0D ? ((-b - 6.0D * c) * y3 + (6.0D * b + 30.0D * c) * y2 + (-12.0D * b - 48.0D * c) * y1 + (8.0D * b + 24.0D * c)) * (1.0D / 6.0D) : ((12.0D - 9.0D * b - 6.0D * c) * y3 + (-18.0D + 12.0D * b + 6.0D * c) * y2 + (6.0D - 2.0D * b)) * (1.0D / 6.0D);
				
				final double expectedValue = expectedX * expectedY;
				
				assertEquals(expectedValue, filterTable[i]);
			}
		}
	}
	
	@Test
	public void testEquals() {
		final MitchellFilter2D a = new MitchellFilter2D(2.0D, 4.0D, 6.0D, 8.0D);
		final MitchellFilter2D b = new MitchellFilter2D(2.0D, 4.0D, 6.0D, 8.0D);
		final MitchellFilter2D c = new MitchellFilter2D(2.0D, 4.0D, 6.0D, 9.0D);
		final MitchellFilter2D d = new MitchellFilter2D(2.0D, 4.0D, 9.0D, 8.0D);
		final MitchellFilter2D e = new MitchellFilter2D(2.0D, 9.0D, 6.0D, 8.0D);
		final MitchellFilter2D f = new MitchellFilter2D(9.0D, 4.0D, 6.0D, 8.0D);
		final MitchellFilter2D g = null;
		
		assertEquals(a, a);
		
		assertEquals(a, b);
		assertEquals(b, a);
		
		assertNotEquals(a, c);
		assertNotEquals(c, a);
		
		assertNotEquals(a, d);
		assertNotEquals(d, a);
		
		assertNotEquals(a, e);
		assertNotEquals(e, a);
		
		assertNotEquals(a, f);
		assertNotEquals(f, a);
		
		assertNotEquals(a, g);
		assertNotEquals(g, a);
	}
	
	@Test
	public void testEvaluate() {
		final MitchellFilter2D mitchellFilter2D = new MitchellFilter2D();
		
		assertEquals(0.790123456790123400D, mitchellFilter2D.evaluate(0.0D, 0.0D));
		assertEquals(0.049382716049382665D, mitchellFilter2D.evaluate(0.0D, 1.0D));
		assertEquals(0.049382716049382665D, mitchellFilter2D.evaluate(1.0D, 0.0D));
		assertEquals(0.003086419753086414D, mitchellFilter2D.evaluate(1.0D, 1.0D));
	}
	
	@Test
	public void testGetB() {
		final MitchellFilter2D mitchellFilter2D = new MitchellFilter2D(2.0D, 4.0D, 6.0D, 8.0D);
		
		assertEquals(6.0D, mitchellFilter2D.getB());
	}
	
	@Test
	public void testGetC() {
		final MitchellFilter2D mitchellFilter2D = new MitchellFilter2D(2.0D, 4.0D, 6.0D, 8.0D);
		
		assertEquals(8.0D, mitchellFilter2D.getC());
	}
	
	@Test
	public void testHashCode() {
		final MitchellFilter2D a = new MitchellFilter2D(2.0D, 4.0D, 6.0D, 8.0D);
		final MitchellFilter2D b = new MitchellFilter2D(2.0D, 4.0D, 6.0D, 8.0D);
		
		assertEquals(a.hashCode(), b.hashCode());
	}
	
	@Test
	public void testToString() {
		final MitchellFilter2D mitchellFilter2D = new MitchellFilter2D(2.0D, 4.0D, 6.0D, 8.0D);
		
		assertEquals("new MitchellFilter2D(2.0D, 4.0D, 6.0D, 8.0D)", mitchellFilter2D.toString());
	}
}