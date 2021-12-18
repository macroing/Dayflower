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
public final class OrthonormalBasis33DUnitTests {
	public OrthonormalBasis33DUnitTests() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Test
	public void testConstructor() {
		final OrthonormalBasis33D orthonormalBasis = new OrthonormalBasis33D();
		
		assertEquals(new Vector3D(1.0D, 0.0D, 0.0D), orthonormalBasis.getU());
		assertEquals(new Vector3D(0.0D, 1.0D, 0.0D), orthonormalBasis.getV());
		assertEquals(new Vector3D(0.0D, 0.0D, 1.0D), orthonormalBasis.getW());
	}
	
	@Test
	public void testConstructorVector3D() {
		final OrthonormalBasis33D orthonormalBasis = new OrthonormalBasis33D(new Vector3D(0.0D, 0.0D, 1.0D));
		
//		TODO: Find out if this should change.
		assertEquals(new Vector3D(0.0D, -1.0D, +0.0D), orthonormalBasis.getU());
		assertEquals(new Vector3D(1.0D, +0.0D, -0.0D), orthonormalBasis.getV());
		assertEquals(new Vector3D(0.0D, +0.0D, +1.0D), orthonormalBasis.getW());
		
		assertThrows(NullPointerException.class, () -> new OrthonormalBasis33D(null));
	}
	
	@Test
	public void testConstructorVector3DVector3D() {
		final OrthonormalBasis33D orthonormalBasis = new OrthonormalBasis33D(new Vector3D(0.0D, 0.0D, 1.0D), new Vector3D(0.0D, 1.0D, 0.0D));
		
		assertEquals(new Vector3D(1.0D, 0.0D, 0.0D), orthonormalBasis.getU());
		assertEquals(new Vector3D(0.0D, 1.0D, 0.0D), orthonormalBasis.getV());
		assertEquals(new Vector3D(0.0D, 0.0D, 1.0D), orthonormalBasis.getW());
		
		assertThrows(NullPointerException.class, () -> new OrthonormalBasis33D(new Vector3D(0.0D, 0.0D, 1.0D), null));
		assertThrows(NullPointerException.class, () -> new OrthonormalBasis33D(null, new Vector3D(0.0D, 1.0D, 0.0D)));
	}
	
	@Test
	public void testConstructorVector3DVector3DVector3D() {
		final OrthonormalBasis33D orthonormalBasis = new OrthonormalBasis33D(new Vector3D(0.0D, 0.0D, 1.0D), new Vector3D(0.0D, 1.0D, 0.0D), new Vector3D(1.0D, 0.0D, 0.0D));
		
		assertEquals(new Vector3D(1.0D, 0.0D, 0.0D), orthonormalBasis.getU());
		assertEquals(new Vector3D(0.0D, 1.0D, 0.0D), orthonormalBasis.getV());
		assertEquals(new Vector3D(0.0D, 0.0D, 1.0D), orthonormalBasis.getW());
		
		assertThrows(NullPointerException.class, () -> new OrthonormalBasis33D(new Vector3D(0.0D, 0.0D, 1.0D), new Vector3D(0.0D, 1.0D, 0.0D), null));
		assertThrows(NullPointerException.class, () -> new OrthonormalBasis33D(new Vector3D(0.0D, 0.0D, 1.0D), null, new Vector3D(1.0D, 0.0D, 0.0D)));
		assertThrows(NullPointerException.class, () -> new OrthonormalBasis33D(null, new Vector3D(0.0D, 1.0D, 0.0D), new Vector3D(1.0D, 0.0D, 0.0D)));
	}
	
	@Test
	public void testGetU() {
		final OrthonormalBasis33D orthonormalBasis = new OrthonormalBasis33D(new Vector3D(0.0D, 0.0D, 1.0D), new Vector3D(0.0D, 1.0D, 0.0D), new Vector3D(1.0D, 0.0D, 0.0D));
		
		assertEquals(new Vector3D(1.0D, 0.0D, 0.0D), orthonormalBasis.getU());
	}
	
	@Test
	public void testGetV() {
		final OrthonormalBasis33D orthonormalBasis = new OrthonormalBasis33D(new Vector3D(0.0D, 0.0D, 1.0D), new Vector3D(0.0D, 1.0D, 0.0D), new Vector3D(1.0D, 0.0D, 0.0D));
		
		assertEquals(new Vector3D(0.0D, 1.0D, 0.0D), orthonormalBasis.getV());
	}
	
	@Test
	public void testGetW() {
		final OrthonormalBasis33D orthonormalBasis = new OrthonormalBasis33D(new Vector3D(0.0D, 0.0D, 1.0D), new Vector3D(0.0D, 1.0D, 0.0D), new Vector3D(1.0D, 0.0D, 0.0D));
		
		assertEquals(new Vector3D(0.0D, 0.0D, 1.0D), orthonormalBasis.getW());
	}
	
	@Test
	public void testToString() {
		final OrthonormalBasis33D orthonormalBasis = new OrthonormalBasis33D(new Vector3D(0.0D, 0.0D, 1.0D), new Vector3D(0.0D, 1.0D, 0.0D), new Vector3D(1.0D, 0.0D, 0.0D));
		
		assertEquals("new OrthonormalBasis33D(new Vector3D(0.0D, 0.0D, 1.0D), new Vector3D(0.0D, 1.0D, 0.0D), new Vector3D(1.0D, 0.0D, 0.0D))", orthonormalBasis.toString());
	}
}