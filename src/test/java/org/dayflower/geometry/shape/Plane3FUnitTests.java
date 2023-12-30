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
package org.dayflower.geometry.shape;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.UncheckedIOException;
import java.util.Optional;

import org.dayflower.geometry.OrthonormalBasis33F;
import org.dayflower.geometry.Point2F;
import org.dayflower.geometry.Point3F;
import org.dayflower.geometry.Ray3F;
import org.dayflower.geometry.SurfaceIntersection3F;
import org.dayflower.geometry.SurfaceIntersector3F;
import org.dayflower.geometry.SurfaceSample3F;
import org.dayflower.geometry.Vector3F;
import org.dayflower.geometry.boundingvolume.InfiniteBoundingVolume3F;
import org.dayflower.mock.DataOutputMock;
import org.dayflower.mock.NodeHierarchicalVisitorMock;
import org.dayflower.mock.NodeVisitorMock;

import org.junit.jupiter.api.Test;

import org.macroing.java.util.visitor.NodeHierarchicalVisitor;
import org.macroing.java.util.visitor.NodeTraversalException;
import org.macroing.java.util.visitor.NodeVisitor;

@SuppressWarnings("static-method")
public final class Plane3FUnitTests {
	public Plane3FUnitTests() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Test
	public void testAcceptNodeHierarchicalVisitor() {
		final Plane3F plane = new Plane3F();
		
		assertTrue(plane.accept(new NodeHierarchicalVisitorMock(node -> false,              node -> node.equals(plane))));
		assertTrue(plane.accept(new NodeHierarchicalVisitorMock(node -> node.equals(plane), node -> node.equals(plane))));
		
		assertThrows(NodeTraversalException.class, () -> plane.accept(new NodeHierarchicalVisitorMock(null, null)));
		assertThrows(NullPointerException.class, () -> plane.accept((NodeHierarchicalVisitor)(null)));
	}
	
	@Test
	public void testAcceptNodeVisitor() {
		final Plane3F plane = new Plane3F();
		
		assertThrows(NodeTraversalException.class, () -> plane.accept(new NodeVisitorMock(true)));
		assertThrows(NullPointerException.class, () -> plane.accept((NodeVisitor)(null)));
	}
	
	@Test
	public void testConstants() {
		assertEquals(10, Plane3F.ID);
		assertEquals("Plane", Plane3F.NAME);
	}
	
	@Test
	public void testContains() {
		final Plane3F plane = new Plane3F();
		
		assertTrue(plane.contains(new Point3F(2.0F, 2.0F, 0.0F)));
		
		assertFalse(plane.contains(new Point3F(2.0F, 2.0F, 2.0F)));
		
		assertThrows(NullPointerException.class, () -> plane.contains(null));
	}
	
	@Test
	public void testEquals() {
		final Plane3F a = new Plane3F();
		final Plane3F b = new Plane3F();
		final Plane3F c = null;
		
		assertEquals(a, a);
		
		assertEquals(a, b);
		assertEquals(b, a);
		
		assertNotEquals(a, c);
		assertNotEquals(c, a);
	}
	
	@Test
	public void testGetBoundingVolume() {
		final Plane3F plane = new Plane3F();
		
		assertEquals(new InfiniteBoundingVolume3F(), plane.getBoundingVolume());
	}
	
	@Test
	public void testGetID() {
		final Plane3F plane = new Plane3F();
		
		assertEquals(Plane3F.ID, plane.getID());
	}
	
	@Test
	public void testGetName() {
		final Plane3F plane = new Plane3F();
		
		assertEquals(Plane3F.NAME, plane.getName());
	}
	
	@Test
	public void testGetSurfaceArea() {
		final Plane3F plane = new Plane3F();
		
		assertEquals(Float.POSITIVE_INFINITY, plane.getSurfaceArea());
	}
	
	@Test
	public void testHashCode() {
		final Plane3F a = new Plane3F();
		final Plane3F b = new Plane3F();
		
		assertEquals(a.hashCode(), a.hashCode());
		assertEquals(a.hashCode(), b.hashCode());
	}
	
	@Test
	public void testIntersectionRay3FFloatFloat() {
		final Plane3F plane = new Plane3F();
		
		final Optional<SurfaceIntersection3F> optionalSurfaceIntersection = plane.intersection(new Ray3F(new Point3F(1.0F, 3.0F, 2.0F), new Vector3F(0.0F,  0.0F, -1.0F)), 0.0F, 3.0F);
		
		assertNotNull(optionalSurfaceIntersection);
		
		assertTrue(optionalSurfaceIntersection.isPresent());
		
		final SurfaceIntersection3F surfaceIntersection = optionalSurfaceIntersection.get();
		
		assertEquals(new OrthonormalBasis33F(new Vector3F(0.0F, 0.0F, 1.0F), new Vector3F(0.0F, 1.0F, 0.0F), new Vector3F(1.0F, 0.0F, 0.0F)), surfaceIntersection.getOrthonormalBasisG());
		assertEquals(new OrthonormalBasis33F(new Vector3F(0.0F, 0.0F, 1.0F), new Vector3F(0.0F, 1.0F, 0.0F), new Vector3F(1.0F, 0.0F, 0.0F)), surfaceIntersection.getOrthonormalBasisS());
		assertEquals(new Ray3F(new Point3F(1.0F, 3.0F, 2.0F), new Vector3F(0.0F, 0.0F, -1.0F)), surfaceIntersection.getRay());
		assertEquals(plane, surfaceIntersection.getShape());
		assertEquals(new Point3F(1.0F, 3.0F, 0.0F), surfaceIntersection.getSurfaceIntersectionPoint());
		assertEquals(new Vector3F(0.0F, 0.0F, 1.0F), surfaceIntersection.getSurfaceNormalG());
		assertEquals(new Vector3F(0.0F, 0.0F, 1.0F), surfaceIntersection.getSurfaceNormalS());
		assertEquals(2.0F, surfaceIntersection.getT());
		assertEquals(new Point2F(1.0F, 3.0F), surfaceIntersection.getTextureCoordinates());
		
		assertFalse(plane.intersection(new Ray3F(new Point3F(0.0F, 0.0F, 0.0F), new Vector3F(1.0F, 0.0F, 0.0F)), 0.0F, 1.0F).isPresent());
		
		assertThrows(NullPointerException.class, () -> plane.intersection(null, 0.0F, 0.0F));
	}
	
	@Test
	public void testIntersectionSurfaceIntersector3F() {
		final Plane3F plane = new Plane3F();
		
		assertTrue(plane.intersection(new SurfaceIntersector3F(new Ray3F(new Point3F(1.0F, 3.0F, 2.0F), new Vector3F(0.0F,  0.0F, -1.0F)), 0.0F, 3.0F)));
		
		assertFalse(plane.intersection(new SurfaceIntersector3F(new Ray3F(new Point3F(0.0F, 0.0F, 0.0F), new Vector3F(1.0F, 0.0F, 0.0F)), 0.0F, 1.0F)));
		
		assertThrows(NullPointerException.class, () -> plane.intersection(null));
	}
	
	@Test
	public void testIntersectionT() {
		final Plane3F plane = new Plane3F();
		
		assertEquals(Float.NaN, plane.intersectionT(new Ray3F(new Point3F(0.0F, 0.0F, +2.0F), new Vector3F(0.0F, 0.0F, -1.0F)), 0.0F, 1.0F));
		assertEquals(Float.NaN, plane.intersectionT(new Ray3F(new Point3F(0.0F, 0.0F, +2.0F), new Vector3F(0.0F, 0.0F, +1.0F)), 0.0F, 1.0F));
		assertEquals(Float.NaN, plane.intersectionT(new Ray3F(new Point3F(0.0F, 0.0F, -2.0F), new Vector3F(0.0F, 0.0F, -1.0F)), 0.0F, 1.0F));
		assertEquals(Float.NaN, plane.intersectionT(new Ray3F(new Point3F(0.0F, 0.0F, -2.0F), new Vector3F(0.0F, 0.0F, +1.0F)), 0.0F, 1.0F));
		
		assertEquals(Float.NaN, plane.intersectionT(new Ray3F(new Point3F(0.0F, 0.0F, 0.0F), new Vector3F(1.0F, 0.0F, 0.0F)), 0.0F, 1.0F));
		
		assertEquals(2.0F, plane.intersectionT(new Ray3F(new Point3F(0.0F, 0.0F, 2.0F), new Vector3F(0.0F, 0.0F, -1.0F)), 0.0F, 3.0F));
		
		assertThrows(NullPointerException.class, () -> plane.intersectionT(null, 0.0F, 0.0F));
	}
	
	@Test
	public void testIntersects() {
		final Plane3F plane = new Plane3F();
		
		assertFalse(plane.intersects(new Ray3F(new Point3F(0.0F, 0.0F, +2.0F), new Vector3F(0.0F, 0.0F, -1.0F)), 0.0F, 1.0F));
		assertFalse(plane.intersects(new Ray3F(new Point3F(0.0F, 0.0F, +2.0F), new Vector3F(0.0F, 0.0F, +1.0F)), 0.0F, 1.0F));
		assertFalse(plane.intersects(new Ray3F(new Point3F(0.0F, 0.0F, -2.0F), new Vector3F(0.0F, 0.0F, -1.0F)), 0.0F, 1.0F));
		assertFalse(plane.intersects(new Ray3F(new Point3F(0.0F, 0.0F, -2.0F), new Vector3F(0.0F, 0.0F, +1.0F)), 0.0F, 1.0F));
		
		assertFalse(plane.intersects(new Ray3F(new Point3F(0.0F, 0.0F, 0.0F), new Vector3F(1.0F, 0.0F, 0.0F)), 0.0F, 1.0F));
		
		assertTrue(plane.intersects(new Ray3F(new Point3F(0.0F, 0.0F, 2.0F), new Vector3F(0.0F, 0.0F, -1.0F)), 0.0F, 3.0F));
		
		assertThrows(NullPointerException.class, () -> plane.intersectionT(null, 0.0F, 0.0F));
	}
	
	@Test
	public void testSamplePoint2F() {
		final Plane3F plane = new Plane3F();
		
		final Optional<SurfaceSample3F> optionalSurfaceSample = plane.sample(new Point2F());
		
		assertFalse(optionalSurfaceSample.isPresent());
		
		assertThrows(NullPointerException.class, () -> plane.sample(null));
	}
	
	@Test
	public void testToString() {
		final Plane3F plane = new Plane3F();
		
		assertEquals("new Plane3F()", plane.toString());
	}
	
	@Test
	public void testWrite() {
		final Plane3F a = new Plane3F();
		
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		
		final DataOutput dataOutput = new DataOutputStream(byteArrayOutputStream);
		
		a.write(dataOutput);
		
		final byte[] bytes = byteArrayOutputStream.toByteArray();
		
		final Plane3F b = new Plane3FReader().read(new DataInputStream(new ByteArrayInputStream(bytes)));
		
		assertEquals(a, b);
		
		assertThrows(NullPointerException.class, () -> a.write((DataOutput)(null)));
		assertThrows(UncheckedIOException.class, () -> a.write(new DataOutputMock()));
	}
}