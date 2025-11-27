/**
 * Copyright 2014 - 2025 J&#246;rgen Lundgren
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
public final class LanczosSincFilter2DUnitTests {
	public LanczosSincFilter2DUnitTests() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Test
	public void testConstructor() {
		final LanczosSincFilter2D lanczosSincFilter2D = new LanczosSincFilter2D();
		
		assertEquals(4.0D, lanczosSincFilter2D.getResolutionX());
		assertEquals(4.0D, lanczosSincFilter2D.getResolutionY());
		
		assertEquals(0.25D, lanczosSincFilter2D.getResolutionXReciprocal());
		assertEquals(0.25D, lanczosSincFilter2D.getResolutionYReciprocal());
		
		assertEquals(3.0D, lanczosSincFilter2D.getTau());
	}
	
	@Test
	public void testConstructorDoubleDoubleDouble() {
		final LanczosSincFilter2D lanczosSincFilter2D = new LanczosSincFilter2D(2.0D, 4.0D, 6.0D);
		
		assertEquals(2.0D, lanczosSincFilter2D.getResolutionX());
		assertEquals(4.0D, lanczosSincFilter2D.getResolutionY());
		
		assertEquals(0.50D, lanczosSincFilter2D.getResolutionXReciprocal());
		assertEquals(0.25D, lanczosSincFilter2D.getResolutionYReciprocal());
		
		assertEquals(6.0D, lanczosSincFilter2D.getTau());
	}
	
	@Test
	public void testEquals() {
		final LanczosSincFilter2D a = new LanczosSincFilter2D(2.0D, 4.0D, 6.0D);
		final LanczosSincFilter2D b = new LanczosSincFilter2D(2.0D, 4.0D, 6.0D);
		final LanczosSincFilter2D c = new LanczosSincFilter2D(2.0D, 4.0D, 8.0D);
		final LanczosSincFilter2D d = new LanczosSincFilter2D(2.0D, 8.0D, 6.0D);
		final LanczosSincFilter2D e = new LanczosSincFilter2D(8.0D, 4.0D, 6.0D);
		final LanczosSincFilter2D f = null;
		
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
		final LanczosSincFilter2D lanczosSincFilter2D = new LanczosSincFilter2D();
		
		assertEquals(1.00000000000000000D, lanczosSincFilter2D.evaluate( 0.0D,  0.0D));
		assertEquals(0.27018982304623407D, lanczosSincFilter2D.evaluate( 0.0D,  1.0D));
		assertEquals(0.27018982304623407D, lanczosSincFilter2D.evaluate( 1.0D,  0.0D));
		assertEquals(0.07300254047775528D, lanczosSincFilter2D.evaluate( 1.0D,  1.0D));
		assertEquals(0.00000000000000000D, lanczosSincFilter2D.evaluate(16.0D, 16.0D));
	}
	
	@Test
	public void testGetTable() {
		final LanczosSincFilter2D lanczosSincFilter2D = new LanczosSincFilter2D(4.0D, 4.0D, 3.0D);
		
		final double[] filterTable = lanczosSincFilter2D.getTable();
		
		assertNotNull(filterTable);
		
		assertEquals(16, Filter2D.TABLE_SIZE);
		assertEquals(256, filterTable.length);
		
		for(int i = 0, y = 0; y < 16; y++) {
			for(int x = 0; x < 16; x++, i++) {
				final double filterX = (x + 0.5D) * 4.0D * 0.0625D * 0.25D;
				final double filterY = (y + 0.5D) * 4.0D * 0.0625D * 0.25D;
				
				final double expectedX = filterX < 1.0e-5D ? 1.0D : filterX > 1.0D ? 0.0D : (Math.sin(filterX * Math.PI * 3.0D) / (filterX * Math.PI * 3.0D)) * (Math.sin(filterX * Math.PI) / (filterX * Math.PI));
				final double expectedY = filterY < 1.0e-5D ? 1.0D : filterY > 1.0D ? 0.0D : (Math.sin(filterY * Math.PI * 3.0D) / (filterY * Math.PI * 3.0D)) * (Math.sin(filterY * Math.PI) / (filterY * Math.PI));
				
				final double expectedValue = expectedX * expectedY;
				
				assertEquals(expectedValue, filterTable[i]);
			}
		}
	}
	
	@Test
	public void testGetTau() {
		final LanczosSincFilter2D lanczosSincFilter2D = new LanczosSincFilter2D(2.0D, 4.0D, 6.0D);
		
		assertEquals(6.0D, lanczosSincFilter2D.getTau());
	}
	
	@Test
	public void testHashCode() {
		final LanczosSincFilter2D a = new LanczosSincFilter2D(2.0D, 4.0D, 6.0D);
		final LanczosSincFilter2D b = new LanczosSincFilter2D(2.0D, 4.0D, 6.0D);
		
		assertEquals(a.hashCode(), b.hashCode());
	}
	
	@Test
	public void testToString() {
		final LanczosSincFilter2D lanczosSincFilter2D = new LanczosSincFilter2D(2.0D, 4.0D, 6.0D);
		
		assertEquals("new LanczosSincFilter2D(2.0D, 4.0D, 6.0D)", lanczosSincFilter2D.toString());
	}
}