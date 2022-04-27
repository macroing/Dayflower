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
public final class BoxFilter2FUnitTests {
	public BoxFilter2FUnitTests() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Test
	public void testConstructor() {
		final BoxFilter2F boxFilter2F = new BoxFilter2F();
		
		assertEquals(0.5F, boxFilter2F.getResolutionX());
		assertEquals(0.5F, boxFilter2F.getResolutionY());
		
		assertEquals(2.0F, boxFilter2F.getResolutionXReciprocal());
		assertEquals(2.0F, boxFilter2F.getResolutionYReciprocal());
	}
	
	@Test
	public void testConstructorFloatFloat() {
		final BoxFilter2F boxFilter2F = new BoxFilter2F(2.0F, 4.0F);
		
		assertEquals(2.0F, boxFilter2F.getResolutionX());
		assertEquals(4.0F, boxFilter2F.getResolutionY());
		
		assertEquals(0.50F, boxFilter2F.getResolutionXReciprocal());
		assertEquals(0.25F, boxFilter2F.getResolutionYReciprocal());
	}
	
	@Test
	public void testCreateFilterTable() {
		final BoxFilter2F boxFilter2F = new BoxFilter2F();
		
		final float[] filterTable = boxFilter2F.createFilterTable();
		
		assertNotNull(filterTable);
		
		assertEquals(16, Filter2F.FILTER_TABLE_SIZE);
		assertEquals(256, filterTable.length);
		
		for(int i = 0; i < filterTable.length; i++) {
			assertEquals(1.0F, filterTable[i]);
		}
	}
	
	@Test
	public void testEquals() {
		final BoxFilter2F a = new BoxFilter2F(2.0F, 4.0F);
		final BoxFilter2F b = new BoxFilter2F(2.0F, 4.0F);
		final BoxFilter2F c = new BoxFilter2F(2.0F, 6.0F);
		final BoxFilter2F d = new BoxFilter2F(6.0F, 4.0F);
		final BoxFilter2F e = null;
		
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
		final BoxFilter2F boxFilter2F = new BoxFilter2F();
		
		assertEquals(1.0F, boxFilter2F.evaluate(0.0F, 0.0F));
		assertEquals(1.0F, boxFilter2F.evaluate(0.0F, 1.0F));
		assertEquals(1.0F, boxFilter2F.evaluate(1.0F, 0.0F));
		assertEquals(1.0F, boxFilter2F.evaluate(1.0F, 1.0F));
	}
	
	@Test
	public void testHashCode() {
		final BoxFilter2F a = new BoxFilter2F(2.0F, 4.0F);
		final BoxFilter2F b = new BoxFilter2F(2.0F, 4.0F);
		
		assertEquals(a.hashCode(), b.hashCode());
	}
	
	@Test
	public void testToString() {
		final BoxFilter2F boxFilter2F = new BoxFilter2F(2.0F, 4.0F);
		
		assertEquals("new BoxFilter2F(2.0F, 4.0F)", boxFilter2F.toString());
	}
}