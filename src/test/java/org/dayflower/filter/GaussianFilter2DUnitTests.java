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
public final class GaussianFilter2DUnitTests {
	public GaussianFilter2DUnitTests() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Test
	public void testConstructor() {
		final GaussianFilter2D gaussianFilter2D = new GaussianFilter2D();
		
		assertEquals(2.0D, gaussianFilter2D.getResolutionX());
		assertEquals(2.0D, gaussianFilter2D.getResolutionY());
		
		assertEquals(0.5D, gaussianFilter2D.getResolutionXReciprocal());
		assertEquals(0.5D, gaussianFilter2D.getResolutionYReciprocal());
		
		assertEquals(2.0D, gaussianFilter2D.getFalloff());
	}
	
	@Test
	public void testConstructorDoubleDoubleDouble() {
		final GaussianFilter2D gaussianFilter2D = new GaussianFilter2D(2.0D, 4.0D, 6.0D);
		
		assertEquals(2.0D, gaussianFilter2D.getResolutionX());
		assertEquals(4.0D, gaussianFilter2D.getResolutionY());
		
		assertEquals(0.50D, gaussianFilter2D.getResolutionXReciprocal());
		assertEquals(0.25D, gaussianFilter2D.getResolutionYReciprocal());
		
		assertEquals(6.0D, gaussianFilter2D.getFalloff());
	}
	
	@Test
	public void testCreateFilterTable() {
		final GaussianFilter2D gaussianFilter2D = new GaussianFilter2D(2.0D, 2.0D, 2.0D);
		
		final double[] filterTable = gaussianFilter2D.createFilterTable();
		
		assertNotNull(filterTable);
		
		assertEquals(16, Filter2D.FILTER_TABLE_SIZE);
		assertEquals(256, filterTable.length);
		
		for(int i = 0, y = 0; y < 16; y++) {
			for(int x = 0; x < 16; x++, i++) {
				final double filterX = (x + 0.5D) * 2.0D * 0.0625D;
				final double filterY = (y + 0.5D) * 2.0D * 0.0625D;
				
				final double expectedValue = (Math.exp(-2.0D * filterX * filterX) - Math.exp(-8.0D)) * (Math.exp(-2.0D * filterY * filterY) - Math.exp(-8.0D));
				
				assertEquals(expectedValue, filterTable[i]);
			}
		}
	}
	
	@Test
	public void testEquals() {
		final GaussianFilter2D a = new GaussianFilter2D(2.0D, 4.0D, 6.0D);
		final GaussianFilter2D b = new GaussianFilter2D(2.0D, 4.0D, 6.0D);
		final GaussianFilter2D c = new GaussianFilter2D(2.0D, 4.0D, 8.0D);
		final GaussianFilter2D d = new GaussianFilter2D(2.0D, 8.0D, 6.0D);
		final GaussianFilter2D e = new GaussianFilter2D(8.0D, 4.0D, 6.0D);
		final GaussianFilter2D f = null;
		
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
		final GaussianFilter2D gaussianFilter2D = new GaussianFilter2D(2.0D, 2.0D, 2.0D);
		
		final double expectedValueA = (Math.exp(-0.0D) - Math.exp(-8.0D)) * (Math.exp(-0.0D) - Math.exp(-8.0D));
		final double expectedValueB = (Math.exp(-0.0D) - Math.exp(-8.0D)) * (Math.exp(-2.0D) - Math.exp(-8.0D));
		final double expectedValueC = (Math.exp(-2.0D) - Math.exp(-8.0D)) * (Math.exp(-0.0D) - Math.exp(-8.0D));
		final double expectedValueD = (Math.exp(-2.0D) - Math.exp(-8.0D)) * (Math.exp(-2.0D) - Math.exp(-8.0D));
		
		assertEquals(expectedValueA, gaussianFilter2D.evaluate(0.0D, 0.0D));
		assertEquals(expectedValueB, gaussianFilter2D.evaluate(0.0D, 1.0D));
		assertEquals(expectedValueC, gaussianFilter2D.evaluate(1.0D, 0.0D));
		assertEquals(expectedValueD, gaussianFilter2D.evaluate(1.0D, 1.0D));
	}
	
	@Test
	public void testGetFalloff() {
		final GaussianFilter2D gaussianFilter2D = new GaussianFilter2D(2.0D, 4.0D, 6.0D);
		
		assertEquals(6.0D, gaussianFilter2D.getFalloff());
	}
	
	@Test
	public void testHashCode() {
		final GaussianFilter2D a = new GaussianFilter2D(2.0D, 4.0D, 6.0D);
		final GaussianFilter2D b = new GaussianFilter2D(2.0D, 4.0D, 6.0D);
		
		assertEquals(a.hashCode(), b.hashCode());
	}
	
	@Test
	public void testToString() {
		final GaussianFilter2D gaussianFilter2D = new GaussianFilter2D(2.0D, 4.0D, 6.0D);
		
		assertEquals("new GaussianFilter2D(2.0D, 4.0D, 6.0D)", gaussianFilter2D.toString());
	}
}