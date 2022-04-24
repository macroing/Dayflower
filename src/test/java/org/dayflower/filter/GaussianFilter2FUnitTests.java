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
public final class GaussianFilter2FUnitTests {
	public GaussianFilter2FUnitTests() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Test
	public void testConstructor() {
		final GaussianFilter2F gaussianFilter2F = new GaussianFilter2F();
		
		assertEquals(2.0F, gaussianFilter2F.getResolutionX());
		assertEquals(2.0F, gaussianFilter2F.getResolutionY());
		
		assertEquals(0.5F, gaussianFilter2F.getResolutionXReciprocal());
		assertEquals(0.5F, gaussianFilter2F.getResolutionYReciprocal());
		
		assertEquals(2.0F, gaussianFilter2F.getFalloff());
	}
	
	@Test
	public void testConstructorFloatFloatFloat() {
		final GaussianFilter2F gaussianFilter2F = new GaussianFilter2F(2.0F, 4.0F, 6.0F);
		
		assertEquals(2.0F, gaussianFilter2F.getResolutionX());
		assertEquals(4.0F, gaussianFilter2F.getResolutionY());
		
		assertEquals(0.50F, gaussianFilter2F.getResolutionXReciprocal());
		assertEquals(0.25F, gaussianFilter2F.getResolutionYReciprocal());
		
		assertEquals(6.0F, gaussianFilter2F.getFalloff());
	}
	
	@Test
	public void testCreateFilterTable() {
		final GaussianFilter2F gaussianFilter2F = new GaussianFilter2F(2.0F, 2.0F, 2.0F);
		
		final float[] filterTable = gaussianFilter2F.createFilterTable();
		
		assertNotNull(filterTable);
		
		assertEquals(16, Filter2F.FILTER_TABLE_SIZE);
		assertEquals(256, filterTable.length);
		
		for(int i = 0, y = 0; y < 16; y++) {
			for(int x = 0; x < 16; x++, i++) {
				final float filterX = (x + 0.5F) * 2.0F * 0.0625F;
				final float filterY = (y + 0.5F) * 2.0F * 0.0625F;
				
				final float expectedValue = ((float)(Math.exp(-2.0F * filterX * filterX)) - (float)(Math.exp(-8.0F))) * ((float)(Math.exp(-2.0F * filterY * filterY)) - (float)(Math.exp(-8.0F)));
				
				assertEquals(expectedValue, filterTable[i]);
			}
		}
	}
	
	@Test
	public void testEquals() {
		final GaussianFilter2F a = new GaussianFilter2F(2.0F, 4.0F, 6.0F);
		final GaussianFilter2F b = new GaussianFilter2F(2.0F, 4.0F, 6.0F);
		final GaussianFilter2F c = new GaussianFilter2F(2.0F, 4.0F, 8.0F);
		final GaussianFilter2F d = new GaussianFilter2F(2.0F, 8.0F, 6.0F);
		final GaussianFilter2F e = new GaussianFilter2F(8.0F, 4.0F, 6.0F);
		final GaussianFilter2F f = null;
		
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
		final GaussianFilter2F gaussianFilter2F = new GaussianFilter2F(2.0F, 2.0F, 2.0F);
		
		final float expectedValueA = ((float)(Math.exp(-0.0F)) - (float)(Math.exp(-8.0F))) * ((float)(Math.exp(-0.0F)) - (float)(Math.exp(-8.0F)));
		final float expectedValueB = ((float)(Math.exp(-0.0F)) - (float)(Math.exp(-8.0F))) * ((float)(Math.exp(-2.0F)) - (float)(Math.exp(-8.0F)));
		final float expectedValueC = ((float)(Math.exp(-2.0F)) - (float)(Math.exp(-8.0F))) * ((float)(Math.exp(-0.0F)) - (float)(Math.exp(-8.0F)));
		final float expectedValueF = ((float)(Math.exp(-2.0F)) - (float)(Math.exp(-8.0F))) * ((float)(Math.exp(-2.0F)) - (float)(Math.exp(-8.0F)));
		
		assertEquals(expectedValueA, gaussianFilter2F.evaluate(0.0F, 0.0F));
		assertEquals(expectedValueB, gaussianFilter2F.evaluate(0.0F, 1.0F));
		assertEquals(expectedValueC, gaussianFilter2F.evaluate(1.0F, 0.0F));
		assertEquals(expectedValueF, gaussianFilter2F.evaluate(1.0F, 1.0F));
	}
	
	@Test
	public void testGetFalloff() {
		final GaussianFilter2F gaussianFilter2F = new GaussianFilter2F(2.0F, 4.0F, 6.0F);
		
		assertEquals(6.0F, gaussianFilter2F.getFalloff());
	}
	
	@Test
	public void testHashCode() {
		final GaussianFilter2F a = new GaussianFilter2F(2.0F, 4.0F, 6.0F);
		final GaussianFilter2F b = new GaussianFilter2F(2.0F, 4.0F, 6.0F);
		
		assertEquals(a.hashCode(), b.hashCode());
	}
	
	@Test
	public void testToString() {
		final GaussianFilter2F gaussianFilter2F = new GaussianFilter2F(2.0F, 4.0F, 6.0F);
		
		assertEquals("new GaussianFilter2F(2.0F, 4.0F, 6.0F)", gaussianFilter2F.toString());
	}
}