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
public final class TriangleFilter2DUnitTests {
	public TriangleFilter2DUnitTests() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Test
	public void testConstructor() {
		final TriangleFilter2D triangleFilter2D = new TriangleFilter2D();
		
		assertEquals(2.0D, triangleFilter2D.getResolutionX());
		assertEquals(2.0D, triangleFilter2D.getResolutionY());
		
		assertEquals(0.5D, triangleFilter2D.getResolutionXReciprocal());
		assertEquals(0.5D, triangleFilter2D.getResolutionYReciprocal());
	}
	
	@Test
	public void testConstructorDoubleDouble() {
		final TriangleFilter2D triangleFilter2D = new TriangleFilter2D(2.0D, 4.0D);
		
		assertEquals(2.0D, triangleFilter2D.getResolutionX());
		assertEquals(4.0D, triangleFilter2D.getResolutionY());
		
		assertEquals(0.50D, triangleFilter2D.getResolutionXReciprocal());
		assertEquals(0.25D, triangleFilter2D.getResolutionYReciprocal());
	}
	
	@Test
	public void testCreateFilterTable() {
		final TriangleFilter2D triangleFilter2D = new TriangleFilter2D();
		
		final double[] filterTable = triangleFilter2D.createFilterTable();
		
		assertNotNull(filterTable);
		
		assertEquals(16, Filter2D.FILTER_TABLE_SIZE);
		assertEquals(256, filterTable.length);
		
		for(int i = 0, y = 0; y < 16; y++) {
			for(int x = 0; x < 16; x++, i++) {
				final double filterX = (x + 0.5D) * 2.0D * 0.0625D;
				final double filterY = (y + 0.5D) * 2.0D * 0.0625D;
				
				final double expectedValue = (2.0D - filterX) * (2.0D - filterY);
				
				assertEquals(expectedValue, filterTable[i]);
			}
		}
	}
	
	@Test
	public void testEquals() {
		final TriangleFilter2D a = new TriangleFilter2D(2.0D, 4.0D);
		final TriangleFilter2D b = new TriangleFilter2D(2.0D, 4.0D);
		final TriangleFilter2D c = new TriangleFilter2D(2.0D, 6.0D);
		final TriangleFilter2D d = new TriangleFilter2D(6.0D, 4.0D);
		final TriangleFilter2D e = null;
		
		assertEquals(a, a);
		
		assertEquals(a, b);
		assertEquals(b, a);
		
		assertNotEquals(a, c);
		assertNotEquals(c, a);
		
		assertNotEquals(a, d);
		assertNotEquals(d, a);
		
		assertNotEquals(a, e);
		assertNotEquals(e, a);
	}
	
	@Test
	public void testEvaluate() {
		final TriangleFilter2D triangleFilter2D = new TriangleFilter2D();
		
		assertEquals(4.0D, triangleFilter2D.evaluate(0.0D, 0.0D));
		assertEquals(2.0D, triangleFilter2D.evaluate(0.0D, 1.0D));
		assertEquals(2.0D, triangleFilter2D.evaluate(1.0D, 0.0D));
		assertEquals(1.0D, triangleFilter2D.evaluate(1.0D, 1.0D));
	}
	
	@Test
	public void testHashCode() {
		final TriangleFilter2D a = new TriangleFilter2D(2.0D, 4.0D);
		final TriangleFilter2D b = new TriangleFilter2D(2.0D, 4.0D);
		
		assertEquals(a.hashCode(), b.hashCode());
	}
	
	@Test
	public void testToString() {
		final TriangleFilter2D triangleFilter2D = new TriangleFilter2D(2.0D, 4.0D);
		
		assertEquals("new TriangleFilter2D(2.0D, 4.0D)", triangleFilter2D.toString());
	}
}