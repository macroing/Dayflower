/**
 * Copyright 2014 - 2021 J&#246;rgen Lundgren
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
package org.dayflower.geometry;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

@SuppressWarnings("static-method")
public final class OrthonormalBasis33FUnitTests {
	public OrthonormalBasis33FUnitTests() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Test
	public void testConstructor() {
		final OrthonormalBasis33F orthonormalBasis = new OrthonormalBasis33F();
		
		assertEquals(new Vector3F(1.0F, 0.0F, 0.0F), orthonormalBasis.getU());
		assertEquals(new Vector3F(0.0F, 1.0F, 0.0F), orthonormalBasis.getV());
		assertEquals(new Vector3F(0.0F, 0.0F, 1.0F), orthonormalBasis.getW());
	}
	
	@Test
	public void testConstructorVector3F() {
		final OrthonormalBasis33F orthonormalBasis = new OrthonormalBasis33F(new Vector3F(0.0F, 0.0F, 1.0F));
		
//		TODO: Find out if this should change.
		assertEquals(new Vector3F(0.0F, -1.0F, +0.0F), orthonormalBasis.getU());
		assertEquals(new Vector3F(1.0F, +0.0F, -0.0F), orthonormalBasis.getV());
		assertEquals(new Vector3F(0.0F, +0.0F, +1.0F), orthonormalBasis.getW());
		
		assertThrows(NullPointerException.class, () -> new OrthonormalBasis33F(null));
	}
	
	@Test
	public void testConstructorVector3FVector3F() {
		final OrthonormalBasis33F orthonormalBasis = new OrthonormalBasis33F(new Vector3F(0.0F, 0.0F, 1.0F), new Vector3F(0.0F, 1.0F, 0.0F));
		
		assertEquals(new Vector3F(1.0F, 0.0F, 0.0F), orthonormalBasis.getU());
		assertEquals(new Vector3F(0.0F, 1.0F, 0.0F), orthonormalBasis.getV());
		assertEquals(new Vector3F(0.0F, 0.0F, 1.0F), orthonormalBasis.getW());
		
		assertThrows(NullPointerException.class, () -> new OrthonormalBasis33F(new Vector3F(0.0F, 0.0F, 1.0F), null));
		assertThrows(NullPointerException.class, () -> new OrthonormalBasis33F(null, new Vector3F(0.0F, 1.0F, 0.0F)));
	}
	
	@Test
	public void testConstructorVector3FVector3FVector3F() {
		final OrthonormalBasis33F orthonormalBasis = new OrthonormalBasis33F(new Vector3F(0.0F, 0.0F, 1.0F), new Vector3F(0.0F, 1.0F, 0.0F), new Vector3F(1.0F, 0.0F, 0.0F));
		
		assertEquals(new Vector3F(1.0F, 0.0F, 0.0F), orthonormalBasis.getU());
		assertEquals(new Vector3F(0.0F, 1.0F, 0.0F), orthonormalBasis.getV());
		assertEquals(new Vector3F(0.0F, 0.0F, 1.0F), orthonormalBasis.getW());
		
		assertThrows(NullPointerException.class, () -> new OrthonormalBasis33F(new Vector3F(0.0F, 0.0F, 1.0F), new Vector3F(0.0F, 1.0F, 0.0F), null));
		assertThrows(NullPointerException.class, () -> new OrthonormalBasis33F(new Vector3F(0.0F, 0.0F, 1.0F), null, new Vector3F(1.0F, 0.0F, 0.0F)));
		assertThrows(NullPointerException.class, () -> new OrthonormalBasis33F(null, new Vector3F(0.0F, 1.0F, 0.0F), new Vector3F(1.0F, 0.0F, 0.0F)));
	}
	
	@Test
	public void testGetU() {
		final OrthonormalBasis33F orthonormalBasis = new OrthonormalBasis33F(new Vector3F(0.0F, 0.0F, 1.0F), new Vector3F(0.0F, 1.0F, 0.0F), new Vector3F(1.0F, 0.0F, 0.0F));
		
		assertEquals(new Vector3F(1.0F, 0.0F, 0.0F), orthonormalBasis.getU());
	}
	
	@Test
	public void testGetV() {
		final OrthonormalBasis33F orthonormalBasis = new OrthonormalBasis33F(new Vector3F(0.0F, 0.0F, 1.0F), new Vector3F(0.0F, 1.0F, 0.0F), new Vector3F(1.0F, 0.0F, 0.0F));
		
		assertEquals(new Vector3F(0.0F, 1.0F, 0.0F), orthonormalBasis.getV());
	}
	
	@Test
	public void testGetW() {
		final OrthonormalBasis33F orthonormalBasis = new OrthonormalBasis33F(new Vector3F(0.0F, 0.0F, 1.0F), new Vector3F(0.0F, 1.0F, 0.0F), new Vector3F(1.0F, 0.0F, 0.0F));
		
		assertEquals(new Vector3F(0.0F, 0.0F, 1.0F), orthonormalBasis.getW());
	}
	
	@Test
	public void testToString() {
		final OrthonormalBasis33F orthonormalBasis = new OrthonormalBasis33F(new Vector3F(0.0F, 0.0F, 1.0F), new Vector3F(0.0F, 1.0F, 0.0F), new Vector3F(1.0F, 0.0F, 0.0F));
		
		assertEquals("new OrthonormalBasis33F(new Vector3F(0.0F, 0.0F, 1.0F), new Vector3F(0.0F, 1.0F, 0.0F), new Vector3F(1.0F, 0.0F, 0.0F))", orthonormalBasis.toString());
	}
}