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
public final class CatmullRomFilter2FUnitTests {
	public CatmullRomFilter2FUnitTests() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Test
	public void testConstructor() {
		final CatmullRomFilter2F catmullRomFilter2F = new CatmullRomFilter2F();
		
		assertEquals(2.0F, catmullRomFilter2F.getResolutionX());
		assertEquals(2.0F, catmullRomFilter2F.getResolutionY());
		
		assertEquals(0.5F, catmullRomFilter2F.getResolutionXReciprocal());
		assertEquals(0.5F, catmullRomFilter2F.getResolutionYReciprocal());
	}
	
	@Test
	public void testCreateFilterTable() {
		final CatmullRomFilter2F catmullRomFilter2F = new CatmullRomFilter2F();
		
		final float[] filterTable = catmullRomFilter2F.createFilterTable();
		
		assertNotNull(filterTable);
		
		assertEquals(16, Filter2F.FILTER_TABLE_SIZE);
		assertEquals(256, filterTable.length);
		
		for(int i = 0, y = 0; y < 16; y++) {
			for(int x = 0; x < 16; x++, i++) {
				final float filterX = (x + 0.5F) * 2.0F * 0.0625F;
				final float filterY = (y + 0.5F) * 2.0F * 0.0625F;
				
				final float expectedX = filterX >= 2.0F ? 0.0F : filterX < 1.0F ? 3.0F * filterX * filterX * filterX - 5.0F * filterX * filterX + 2.0F : -(filterX * filterX * filterX) + 5.0F * filterX * filterX - 8.0F * filterX + 4.0F;
				final float expectedY = filterY >= 2.0F ? 0.0F : filterY < 1.0F ? 3.0F * filterY * filterY * filterY - 5.0F * filterY * filterY + 2.0F : -(filterY * filterY * filterY) + 5.0F * filterY * filterY - 8.0F * filterY + 4.0F;
				
				final float expectedValue = expectedX * expectedY;
				
				assertEquals(expectedValue, filterTable[i]);
			}
		}
	}
	
	@Test
	public void testEquals() {
		final CatmullRomFilter2F a = new CatmullRomFilter2F();
		final CatmullRomFilter2F b = new CatmullRomFilter2F();
		final CatmullRomFilter2F c = null;
		
		assertEquals(a, a);
		
		assertEquals(a, b);
		assertEquals(b, a);
		
		assertNotEquals(a, c);
		assertNotEquals(c, a);
	}
	
	@Test
	public void testEvaluate() {
		final CatmullRomFilter2F catmullRomFilter2F = new CatmullRomFilter2F();
		
		assertEquals(4.0F, catmullRomFilter2F.evaluate(0.0F, 0.0F));
		assertEquals(0.0F, catmullRomFilter2F.evaluate(0.0F, 1.0F));
		assertEquals(0.0F, catmullRomFilter2F.evaluate(1.0F, 0.0F));
		assertEquals(0.0F, catmullRomFilter2F.evaluate(1.0F, 1.0F));
		assertEquals(0.0F, catmullRomFilter2F.evaluate(2.0F, 2.0F));
	}
	
	@Test
	public void testHashCode() {
		final CatmullRomFilter2F a = new CatmullRomFilter2F();
		final CatmullRomFilter2F b = new CatmullRomFilter2F();
		
		assertEquals(a.hashCode(), b.hashCode());
	}
	
	@Test
	public void testToString() {
		final CatmullRomFilter2F catmullRomFilter2F = new CatmullRomFilter2F();
		
		assertEquals("new CatmullRomFilter2F()", catmullRomFilter2F.toString());
	}
}