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
public final class BoxFilter2DUnitTests {
	public BoxFilter2DUnitTests() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Test
	public void testConstructor() {
		final BoxFilter2D boxFilter2D = new BoxFilter2D();
		
		assertEquals(0.5D, boxFilter2D.getResolutionX());
		assertEquals(0.5D, boxFilter2D.getResolutionY());
		
		assertEquals(2.0D, boxFilter2D.getResolutionXReciprocal());
		assertEquals(2.0D, boxFilter2D.getResolutionYReciprocal());
	}
	
	@Test
	public void testConstructorDoubleDouble() {
		final BoxFilter2D boxFilter2D = new BoxFilter2D(2.0D, 4.0D);
		
		assertEquals(2.0D, boxFilter2D.getResolutionX());
		assertEquals(4.0D, boxFilter2D.getResolutionY());
		
		assertEquals(0.50D, boxFilter2D.getResolutionXReciprocal());
		assertEquals(0.25D, boxFilter2D.getResolutionYReciprocal());
	}
	
	@Test
	public void testCreateFilterTable() {
		final BoxFilter2D boxFilter2D = new BoxFilter2D();
		
		final double[] filterTable = boxFilter2D.createFilterTable();
		
		assertNotNull(filterTable);
		
		assertEquals(16, Filter2D.FILTER_TABLE_SIZE);
		assertEquals(256, filterTable.length);
		
		for(int i = 0; i < filterTable.length; i++) {
			assertEquals(1.0D, filterTable[i]);
		}
	}
	
	@Test
	public void testEquals() {
		final BoxFilter2D a = new BoxFilter2D(2.0D, 4.0D);
		final BoxFilter2D b = new BoxFilter2D(2.0D, 4.0D);
		final BoxFilter2D c = new BoxFilter2D(2.0D, 6.0D);
		final BoxFilter2D d = new BoxFilter2D(6.0D, 4.0D);
		final BoxFilter2D e = null;
		
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
		final BoxFilter2D boxFilter2D = new BoxFilter2D();
		
		assertEquals(1.0D, boxFilter2D.evaluate(0.0D, 0.0D));
		assertEquals(1.0D, boxFilter2D.evaluate(0.0D, 1.0D));
		assertEquals(1.0D, boxFilter2D.evaluate(1.0D, 0.0D));
		assertEquals(1.0D, boxFilter2D.evaluate(1.0D, 1.0D));
	}
	
	@Test
	public void testHashCode() {
		final BoxFilter2D a = new BoxFilter2D(2.0D, 4.0D);
		final BoxFilter2D b = new BoxFilter2D(2.0D, 4.0D);
		
		assertEquals(a.hashCode(), b.hashCode());
	}
	
	@Test
	public void testToString() {
		final BoxFilter2D boxFilter2D = new BoxFilter2D(2.0D, 4.0D);
		
		assertEquals("new BoxFilter2D(2.0D, 4.0D)", boxFilter2D.toString());
	}
}