/**
 * Copyright 2014 - 2022 J&#246;rgen Lundgren
 * 
 * This file is part of Fayflower.
 * 
 * Fayflower is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Fayflower is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with Fayflower. If not, see <http://www.gnu.org/licenses/>.
 */
package org.dayflower.filter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

@SuppressWarnings("static-method")
public final class LanczosSincFilter2FUnitTests {
	public LanczosSincFilter2FUnitTests() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Test
	public void testConstructor() {
		final LanczosSincFilter2F lanczosSincFilter2F = new LanczosSincFilter2F();
		
		assertEquals(4.0F, lanczosSincFilter2F.getResolutionX());
		assertEquals(4.0F, lanczosSincFilter2F.getResolutionY());
		
		assertEquals(0.25F, lanczosSincFilter2F.getResolutionXReciprocal());
		assertEquals(0.25F, lanczosSincFilter2F.getResolutionYReciprocal());
		
		assertEquals(3.0F, lanczosSincFilter2F.getTau());
	}
	
	@Test
	public void testConstructorFloatFloatFloat() {
		final LanczosSincFilter2F lanczosSincFilter2F = new LanczosSincFilter2F(2.0F, 4.0F, 6.0F);
		
		assertEquals(2.0F, lanczosSincFilter2F.getResolutionX());
		assertEquals(4.0F, lanczosSincFilter2F.getResolutionY());
		
		assertEquals(0.50F, lanczosSincFilter2F.getResolutionXReciprocal());
		assertEquals(0.25F, lanczosSincFilter2F.getResolutionYReciprocal());
		
		assertEquals(6.0F, lanczosSincFilter2F.getTau());
	}
	
	@Test
	public void testCreateFilterTable() {
		final LanczosSincFilter2F lanczosSincFilter2F = new LanczosSincFilter2F(4.0F, 4.0F, 3.0F);
		
		final float[] filterTable = lanczosSincFilter2F.createFilterTable();
		
		assertNotNull(filterTable);
		
		assertEquals(16, Filter2F.FILTER_TABLE_SIZE);
		assertEquals(256, filterTable.length);
		
		for(int i = 0, y = 0; y < 16; y++) {
			for(int x = 0; x < 16; x++, i++) {
				final float filterX = (x + 0.5F) * 4.0F * 0.0625F * 0.25F;
				final float filterY = (y + 0.5F) * 4.0F * 0.0625F * 0.25F;
				
				final float expectedX = filterX < 1.0e-5F ? 1.0F : filterX > 1.0F ? 0.0F : ((float)(Math.sin(filterX * (float)(Math.PI) * 3.0F)) / (filterX * (float)(Math.PI) * 3.0F)) * ((float)(Math.sin(filterX * (float)(Math.PI))) / (filterX * (float)(Math.PI)));
				final float expectedY = filterY < 1.0e-5F ? 1.0F : filterY > 1.0F ? 0.0F : ((float)(Math.sin(filterY * (float)(Math.PI) * 3.0F)) / (filterY * (float)(Math.PI) * 3.0F)) * ((float)(Math.sin(filterY * (float)(Math.PI))) / (filterY * (float)(Math.PI)));
				
				final float expectedValue = expectedX * expectedY;
				
				assertEquals(expectedValue, filterTable[i]);
			}
		}
	}
	
	@Test
	public void testEquals() {
		final LanczosSincFilter2F a = new LanczosSincFilter2F(2.0F, 4.0F, 6.0F);
		final LanczosSincFilter2F b = new LanczosSincFilter2F(2.0F, 4.0F, 6.0F);
		final LanczosSincFilter2F c = new LanczosSincFilter2F(2.0F, 4.0F, 8.0F);
		final LanczosSincFilter2F d = new LanczosSincFilter2F(2.0F, 8.0F, 6.0F);
		final LanczosSincFilter2F e = new LanczosSincFilter2F(8.0F, 4.0F, 6.0F);
		final LanczosSincFilter2F f = null;
		
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
	}
	
	@Test
	public void testEvaluate() {
		final LanczosSincFilter2F lanczosSincFilter2F = new LanczosSincFilter2F();
		
		assertEquals(1.000000000F, lanczosSincFilter2F.evaluate( 0.0F,  0.0F));
		assertEquals(0.270189800F, lanczosSincFilter2F.evaluate( 0.0F,  1.0F));
		assertEquals(0.270189800F, lanczosSincFilter2F.evaluate( 1.0F,  0.0F));
		assertEquals(0.073002525F, lanczosSincFilter2F.evaluate( 1.0F,  1.0F));
		assertEquals(0.000000000F, lanczosSincFilter2F.evaluate(16.0F, 16.0F));
	}
	
	@Test
	public void testGetTau() {
		final LanczosSincFilter2F lanczosSincFilter2F = new LanczosSincFilter2F(2.0F, 4.0F, 6.0F);
		
		assertEquals(6.0F, lanczosSincFilter2F.getTau());
	}
	
	@Test
	public void testHashCode() {
		final LanczosSincFilter2F a = new LanczosSincFilter2F(2.0F, 4.0F, 6.0F);
		final LanczosSincFilter2F b = new LanczosSincFilter2F(2.0F, 4.0F, 6.0F);
		
		assertEquals(a.hashCode(), b.hashCode());
	}
	
	@Test
	public void testToString() {
		final LanczosSincFilter2F lanczosSincFilter2F = new LanczosSincFilter2F(2.0F, 4.0F, 6.0F);
		
		assertEquals("new LanczosSincFilter2F(2.0F, 4.0F, 6.0F)", lanczosSincFilter2F.toString());
	}
}