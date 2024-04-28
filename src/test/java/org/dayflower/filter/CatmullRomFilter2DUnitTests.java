/**
 * Copyright 2014 - 2024 J&#246;rgen Lundgren
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
public final class CatmullRomFilter2DUnitTests {
	public CatmullRomFilter2DUnitTests() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Test
	public void testConstructor() {
		final CatmullRomFilter2D catmullRomFilter2D = new CatmullRomFilter2D();
		
		assertEquals(2.0D, catmullRomFilter2D.getResolutionX());
		assertEquals(2.0D, catmullRomFilter2D.getResolutionY());
		
		assertEquals(0.5D, catmullRomFilter2D.getResolutionXReciprocal());
		assertEquals(0.5D, catmullRomFilter2D.getResolutionYReciprocal());
	}
	
	@Test
	public void testEquals() {
		final CatmullRomFilter2D a = new CatmullRomFilter2D();
		final CatmullRomFilter2D b = new CatmullRomFilter2D();
		final CatmullRomFilter2D c = null;
		
		assertEquals(a, a);
		
		assertEquals(a, b);
		assertEquals(b, a);
		
		assertNotEquals(a, c);
		assertNotEquals(c, a);
	}
	
	@Test
	public void testEvaluate() {
		final CatmullRomFilter2D catmullRomFilter2D = new CatmullRomFilter2D();
		
		assertEquals(4.0D, catmullRomFilter2D.evaluate(0.0D, 0.0D));
		assertEquals(0.0D, catmullRomFilter2D.evaluate(0.0D, 1.0D));
		assertEquals(0.0D, catmullRomFilter2D.evaluate(1.0D, 0.0D));
		assertEquals(0.0D, catmullRomFilter2D.evaluate(1.0D, 1.0D));
		assertEquals(0.0D, catmullRomFilter2D.evaluate(2.0D, 2.0D));
	}
	
	@Test
	public void testGetTable() {
		final CatmullRomFilter2D catmullRomFilter2D = new CatmullRomFilter2D();
		
		final double[] filterTable = catmullRomFilter2D.getTable();
		
		assertNotNull(filterTable);
		
		assertEquals(16, Filter2D.TABLE_SIZE);
		assertEquals(256, filterTable.length);
		
		for(int i = 0, y = 0; y < 16; y++) {
			for(int x = 0; x < 16; x++, i++) {
				final double filterX = (x + 0.5D) * 2.0D * 0.0625D;
				final double filterY = (y + 0.5D) * 2.0D * 0.0625D;
				
				final double expectedX = filterX >= 2.0D ? 0.0D : filterX < 1.0D ? 3.0D * filterX * filterX * filterX - 5.0D * filterX * filterX + 2.0D : -(filterX * filterX * filterX) + 5.0D * filterX * filterX - 8.0D * filterX + 4.0D;
				final double expectedY = filterY >= 2.0D ? 0.0D : filterY < 1.0D ? 3.0D * filterY * filterY * filterY - 5.0D * filterY * filterY + 2.0D : -(filterY * filterY * filterY) + 5.0D * filterY * filterY - 8.0D * filterY + 4.0D;
				
				final double expectedValue = expectedX * expectedY;
				
				assertEquals(expectedValue, filterTable[i]);
			}
		}
	}
	
	@Test
	public void testHashCode() {
		final CatmullRomFilter2D a = new CatmullRomFilter2D();
		final CatmullRomFilter2D b = new CatmullRomFilter2D();
		
		assertEquals(a.hashCode(), b.hashCode());
	}
	
	@Test
	public void testToString() {
		final CatmullRomFilter2D catmullRomFilter2D = new CatmullRomFilter2D();
		
		assertEquals("new CatmullRomFilter2D()", catmullRomFilter2D.toString());
	}
}