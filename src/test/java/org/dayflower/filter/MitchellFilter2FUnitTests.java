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
public final class MitchellFilter2FUnitTests {
	public MitchellFilter2FUnitTests() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Test
	public void testConstructor() {
		final MitchellFilter2F mitchellFilter2F = new MitchellFilter2F();
		
		assertEquals(2.0F, mitchellFilter2F.getResolutionX());
		assertEquals(2.0F, mitchellFilter2F.getResolutionY());
		
		assertEquals(0.5F, mitchellFilter2F.getResolutionXReciprocal());
		assertEquals(0.5F, mitchellFilter2F.getResolutionYReciprocal());
		
		assertEquals(0.3333333333333333F, mitchellFilter2F.getB());
		assertEquals(0.3333333333333333F, mitchellFilter2F.getC());
	}
	
	@Test
	public void testConstructorFloatFloatFloatFloat() {
		final MitchellFilter2F mitchellFilter2F = new MitchellFilter2F(2.0F, 4.0F, 6.0F, 8.0F);
		
		assertEquals(2.0F, mitchellFilter2F.getResolutionX());
		assertEquals(4.0F, mitchellFilter2F.getResolutionY());
		
		assertEquals(0.50F, mitchellFilter2F.getResolutionXReciprocal());
		assertEquals(0.25F, mitchellFilter2F.getResolutionYReciprocal());
		
		assertEquals(6.0F, mitchellFilter2F.getB());
		assertEquals(8.0F, mitchellFilter2F.getC());
	}
	
	@Test
	public void testEquals() {
		final MitchellFilter2F a = new MitchellFilter2F(2.0F, 4.0F, 6.0F, 8.0F);
		final MitchellFilter2F b = new MitchellFilter2F(2.0F, 4.0F, 6.0F, 8.0F);
		final MitchellFilter2F c = new MitchellFilter2F(2.0F, 4.0F, 6.0F, 9.0F);
		final MitchellFilter2F d = new MitchellFilter2F(2.0F, 4.0F, 9.0F, 8.0F);
		final MitchellFilter2F e = new MitchellFilter2F(2.0F, 9.0F, 6.0F, 8.0F);
		final MitchellFilter2F f = new MitchellFilter2F(9.0F, 4.0F, 6.0F, 8.0F);
		final MitchellFilter2F g = null;
		
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
		final MitchellFilter2F mitchellFilter2F = new MitchellFilter2F();
		
		assertEquals(0.7901236000F, mitchellFilter2F.evaluate(0.0F, 0.0F));
		assertEquals(0.0493827420F, mitchellFilter2F.evaluate(0.0F, 1.0F));
		assertEquals(0.0493827420F, mitchellFilter2F.evaluate(1.0F, 0.0F));
		assertEquals(0.0030864228F, mitchellFilter2F.evaluate(1.0F, 1.0F));
	}
	
	@Test
	public void testGetB() {
		final MitchellFilter2F mitchellFilter2F = new MitchellFilter2F(2.0F, 4.0F, 6.0F, 8.0F);
		
		assertEquals(6.0F, mitchellFilter2F.getB());
	}
	
	@Test
	public void testGetC() {
		final MitchellFilter2F mitchellFilter2F = new MitchellFilter2F(2.0F, 4.0F, 6.0F, 8.0F);
		
		assertEquals(8.0F, mitchellFilter2F.getC());
	}
	
	@Test
	public void testGetTable() {
		final MitchellFilter2F mitchellFilter2F = new MitchellFilter2F();
		
		final float[] filterTable = mitchellFilter2F.getTable();
		
		assertNotNull(filterTable);
		
		assertEquals(16, Filter2F.TABLE_SIZE);
		assertEquals(256, filterTable.length);
		
		for(int i = 0, y = 0; y < 16; y++) {
			for(int x = 0; x < 16; x++, i++) {
				final float filterX = (x + 0.5F) * 2.0F * 0.0625F;
				final float filterY = (y + 0.5F) * 2.0F * 0.0625F;
				
				final float b = 1.0F / 3.0F;
				final float c = 1.0F / 3.0F;
				
				final float x1 = Math.abs(filterX);
				final float x2 = x1 * x1;
				final float x3 = x1 * x2;
				
				final float y1 = Math.abs(filterY);
				final float y2 = y1 * y1;
				final float y3 = y1 * y2;
				
				final float expectedX = x1 > 1.0F ? ((-b - 6.0F * c) * x3 + (6.0F * b + 30.0F * c) * x2 + (-12.0F * b - 48.0F * c) * x1 + (8.0F * b + 24.0F * c)) * (1.0F / 6.0F) : ((12.0F - 9.0F * b - 6.0F * c) * x3 + (-18.0F + 12.0F * b + 6.0F * c) * x2 + (6.0F - 2.0F * b)) * (1.0F / 6.0F);
				final float expectedY = y1 > 1.0F ? ((-b - 6.0F * c) * y3 + (6.0F * b + 30.0F * c) * y2 + (-12.0F * b - 48.0F * c) * y1 + (8.0F * b + 24.0F * c)) * (1.0F / 6.0F) : ((12.0F - 9.0F * b - 6.0F * c) * y3 + (-18.0F + 12.0F * b + 6.0F * c) * y2 + (6.0F - 2.0F * b)) * (1.0F / 6.0F);
				
				final float expectedValue = expectedX * expectedY;
				
				assertEquals(expectedValue, filterTable[i]);
			}
		}
	}
	
	@Test
	public void testHashCode() {
		final MitchellFilter2F a = new MitchellFilter2F(2.0F, 4.0F, 6.0F, 8.0F);
		final MitchellFilter2F b = new MitchellFilter2F(2.0F, 4.0F, 6.0F, 8.0F);
		
		assertEquals(a.hashCode(), b.hashCode());
	}
	
	@Test
	public void testToString() {
		final MitchellFilter2F mitchellFilter2F = new MitchellFilter2F(2.0F, 4.0F, 6.0F, 8.0F);
		
		assertEquals("new MitchellFilter2F(2.0F, 4.0F, 6.0F, 8.0F)", mitchellFilter2F.toString());
	}
}