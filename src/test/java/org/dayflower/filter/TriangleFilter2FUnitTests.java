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
public final class TriangleFilter2FUnitTests {
	public TriangleFilter2FUnitTests() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Test
	public void testConstructor() {
		final TriangleFilter2F triangleFilter2F = new TriangleFilter2F();
		
		assertEquals(2.0F, triangleFilter2F.getResolutionX());
		assertEquals(2.0F, triangleFilter2F.getResolutionY());
		
		assertEquals(0.5F, triangleFilter2F.getResolutionXReciprocal());
		assertEquals(0.5F, triangleFilter2F.getResolutionYReciprocal());
	}
	
	@Test
	public void testConstructorFloatFloat() {
		final TriangleFilter2F triangleFilter2F = new TriangleFilter2F(2.0F, 4.0F);
		
		assertEquals(2.0F, triangleFilter2F.getResolutionX());
		assertEquals(4.0F, triangleFilter2F.getResolutionY());
		
		assertEquals(0.50F, triangleFilter2F.getResolutionXReciprocal());
		assertEquals(0.25F, triangleFilter2F.getResolutionYReciprocal());
	}
	
	@Test
	public void testEquals() {
		final TriangleFilter2F a = new TriangleFilter2F(2.0F, 4.0F);
		final TriangleFilter2F b = new TriangleFilter2F(2.0F, 4.0F);
		final TriangleFilter2F c = new TriangleFilter2F(2.0F, 6.0F);
		final TriangleFilter2F d = new TriangleFilter2F(6.0F, 4.0F);
		final TriangleFilter2F e = null;
		
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
		final TriangleFilter2F triangleFilter2F = new TriangleFilter2F();
		
		assertEquals(4.0F, triangleFilter2F.evaluate(0.0F, 0.0F));
		assertEquals(2.0F, triangleFilter2F.evaluate(0.0F, 1.0F));
		assertEquals(2.0F, triangleFilter2F.evaluate(1.0F, 0.0F));
		assertEquals(1.0F, triangleFilter2F.evaluate(1.0F, 1.0F));
	}
	
	@Test
	public void testGetTable() {
		final TriangleFilter2F triangleFilter2F = new TriangleFilter2F();
		
		final float[] filterTable = triangleFilter2F.getTable();
		
		assertNotNull(filterTable);
		
		assertEquals(16, Filter2F.TABLE_SIZE);
		assertEquals(256, filterTable.length);
		
		for(int i = 0, y = 0; y < 16; y++) {
			for(int x = 0; x < 16; x++, i++) {
				final float filterX = (x + 0.5F) * 2.0F * 0.0625F;
				final float filterY = (y + 0.5F) * 2.0F * 0.0625F;
				
				final float expectedValue = (2.0F - filterX) * (2.0F - filterY);
				
				assertEquals(expectedValue, filterTable[i]);
			}
		}
	}
	
	@Test
	public void testHashCode() {
		final TriangleFilter2F a = new TriangleFilter2F(2.0F, 4.0F);
		final TriangleFilter2F b = new TriangleFilter2F(2.0F, 4.0F);
		
		assertEquals(a.hashCode(), b.hashCode());
	}
	
	@Test
	public void testToString() {
		final TriangleFilter2F triangleFilter2F = new TriangleFilter2F(2.0F, 4.0F);
		
		assertEquals("new TriangleFilter2F(2.0F, 4.0F)", triangleFilter2F.toString());
	}
}