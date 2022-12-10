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

import org.dayflower.geometry.OrthonormalBasis33D;
import org.dayflower.geometry.Point2D;
import org.dayflower.geometry.Point3D;
import org.dayflower.geometry.Ray3D;
import org.dayflower.geometry.SurfaceIntersection3D;
import org.dayflower.geometry.SurfaceIntersector3D;
import org.dayflower.geometry.SurfaceSample3D;
import org.dayflower.geometry.Vector3D;
import org.dayflower.geometry.boundingvolume.InfiniteBoundingVolume3D;
import org.dayflower.mock.DataOutputMock;
import org.dayflower.mock.NodeHierarchicalVisitorMock;
import org.dayflower.mock.NodeVisitorMock;
import org.dayflower.node.NodeHierarchicalVisitor;
import org.dayflower.node.NodeTraversalException;
import org.dayflower.node.NodeVisitor;
import org.junit.jupiter.api.Test;

@SuppressWarnings("static-method")
public final class Plane3DUnitTests {
	public Plane3DUnitTests() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Test
	public void testAcceptNodeHierarchicalVisitor() {
		final Plane3D plane = new Plane3D();
		
		assertTrue(plane.accept(new NodeHierarchicalVisitorMock(node -> false,              node -> node.equals(plane))));
		assertTrue(plane.accept(new NodeHierarchicalVisitorMock(node -> node.equals(plane), node -> node.equals(plane))));
		
		assertThrows(NodeTraversalException.class, () -> plane.accept(new NodeHierarchicalVisitorMock(null, null)));
		assertThrows(NullPointerException.class, () -> plane.accept((NodeHierarchicalVisitor)(null)));
	}
	
	@Test
	public void testAcceptNodeVisitor() {
		final Plane3D plane = new Plane3D();
		
		assertThrows(NodeTraversalException.class, () -> plane.accept(new NodeVisitorMock(true)));
		assertThrows(NullPointerException.class, () -> plane.accept((NodeVisitor)(null)));
	}
	
	@Test
	public void testConstants() {
		assertEquals(10, Plane3D.ID);
		assertEquals("Plane", Plane3D.NAME);
	}
	
	@Test
	public void testContains() {
		final Plane3D plane = new Plane3D();
		
		assertTrue(plane.contains(new Point3D(2.0D, 2.0D, 0.0D)));
		
		assertFalse(plane.contains(new Point3D(2.0D, 2.0D, 2.0D)));
		
		assertThrows(NullPointerException.class, () -> plane.contains(null));
	}
	
	@Test
	public void testEquals() {
		final Plane3D a = new Plane3D();
		final Plane3D b = new Plane3D();
		final Plane3D c = null;
		
		assertEquals(a, a);
		
		assertEquals(a, b);
		assertEquals(b, a);
		
		assertNotEquals(a, c);
		assertNotEquals(c, a);
	}
	
	@Test
	public void testGetBoundingVolume() {
		final Plane3D plane = new Plane3D();
		
		assertEquals(new InfiniteBoundingVolume3D(), plane.getBoundingVolume());
	}
	
	@Test
	public void testGetID() {
		final Plane3D plane = new Plane3D();
		
		assertEquals(Plane3D.ID, plane.getID());
	}
	
	@Test
	public void testGetName() {
		final Plane3D plane = new Plane3D();
		
		assertEquals(Plane3D.NAME, plane.getName());
	}
	
	@Test
	public void testGetSurfaceArea() {
		final Plane3D plane = new Plane3D();
		
		assertEquals(Double.POSITIVE_INFINITY, plane.getSurfaceArea());
	}
	
	@Test
	public void testHashCode() {
		final Plane3D a = new Plane3D();
		final Plane3D b = new Plane3D();
		
		assertEquals(a.hashCode(), a.hashCode());
		assertEquals(a.hashCode(), b.hashCode());
	}
	
	@Test
	public void testIntersectionRay3DDoubleDouble() {
		final Plane3D plane = new Plane3D();
		
		final Optional<SurfaceIntersection3D> optionalSurfaceIntersection = plane.intersection(new Ray3D(new Point3D(1.0D, 3.0D, 2.0D), new Vector3D(0.0D,  0.0D, -1.0D)), 0.0D, 3.0D);
		
		assertNotNull(optionalSurfaceIntersection);
		
		assertTrue(optionalSurfaceIntersection.isPresent());
		
		final SurfaceIntersection3D surfaceIntersection = optionalSurfaceIntersection.get();
		
		assertEquals(new OrthonormalBasis33D(new Vector3D(0.0D, 0.0D, 1.0D), new Vector3D(0.0D, 1.0D, 0.0D), new Vector3D(1.0D, 0.0D, 0.0D)), surfaceIntersection.getOrthonormalBasisG());
		assertEquals(new OrthonormalBasis33D(new Vector3D(0.0D, 0.0D, 1.0D), new Vector3D(0.0D, 1.0D, 0.0D), new Vector3D(1.0D, 0.0D, 0.0D)), surfaceIntersection.getOrthonormalBasisS());
		assertEquals(new Ray3D(new Point3D(1.0D, 3.0D, 2.0D), new Vector3D(0.0D, 0.0D, -1.0D)), surfaceIntersection.getRay());
		assertEquals(plane, surfaceIntersection.getShape());
		assertEquals(new Point3D(1.0D, 3.0D, 0.0D), surfaceIntersection.getSurfaceIntersectionPoint());
		assertEquals(new Vector3D(0.0D, 0.0D, 1.0D), surfaceIntersection.getSurfaceNormalG());
		assertEquals(new Vector3D(0.0D, 0.0D, 1.0D), surfaceIntersection.getSurfaceNormalS());
		assertEquals(2.0D, surfaceIntersection.getT());
		assertEquals(new Point2D(1.0D, 3.0D), surfaceIntersection.getTextureCoordinates());
		
		assertFalse(plane.intersection(new Ray3D(new Point3D(0.0D, 0.0D, 0.0D), new Vector3D(1.0D, 0.0D, 0.0D)), 0.0D, 1.0D).isPresent());
		
		assertThrows(NullPointerException.class, () -> plane.intersection(null, 0.0D, 0.0D));
	}
	
	@Test
	public void testIntersectionSurfaceIntersector3D() {
		final Plane3D plane = new Plane3D();
		
		assertTrue(plane.intersection(new SurfaceIntersector3D(new Ray3D(new Point3D(1.0D, 3.0D, 2.0D), new Vector3D(0.0D,  0.0D, -1.0D)), 0.0D, 3.0D)));
		
		assertFalse(plane.intersection(new SurfaceIntersector3D(new Ray3D(new Point3D(0.0D, 0.0D, 0.0D), new Vector3D(1.0D, 0.0D, 0.0D)), 0.0D, 1.0D)));
		
		assertThrows(NullPointerException.class, () -> plane.intersection(null));
	}
	
	@Test
	public void testIntersectionT() {
		final Plane3D plane = new Plane3D();
		
		assertEquals(Double.NaN, plane.intersectionT(new Ray3D(new Point3D(0.0D, 0.0D, +2.0D), new Vector3D(0.0D, 0.0D, -1.0D)), 0.0D, 1.0D));
		assertEquals(Double.NaN, plane.intersectionT(new Ray3D(new Point3D(0.0D, 0.0D, +2.0D), new Vector3D(0.0D, 0.0D, +1.0D)), 0.0D, 1.0D));
		assertEquals(Double.NaN, plane.intersectionT(new Ray3D(new Point3D(0.0D, 0.0D, -2.0D), new Vector3D(0.0D, 0.0D, -1.0D)), 0.0D, 1.0D));
		assertEquals(Double.NaN, plane.intersectionT(new Ray3D(new Point3D(0.0D, 0.0D, -2.0D), new Vector3D(0.0D, 0.0D, +1.0D)), 0.0D, 1.0D));
		
		assertEquals(Double.NaN, plane.intersectionT(new Ray3D(new Point3D(0.0D, 0.0D, 0.0D), new Vector3D(1.0D, 0.0D, 0.0D)), 0.0D, 1.0D));
		
		assertEquals(2.0D, plane.intersectionT(new Ray3D(new Point3D(0.0D, 0.0D, 2.0D), new Vector3D(0.0D, 0.0D, -1.0D)), 0.0D, 3.0D));
		
		assertThrows(NullPointerException.class, () -> plane.intersectionT(null, 0.0D, 0.0D));
	}
	
	@Test
	public void testIntersects() {
		final Plane3D plane = new Plane3D();
		
		assertFalse(plane.intersects(new Ray3D(new Point3D(0.0D, 0.0D, +2.0D), new Vector3D(0.0D, 0.0D, -1.0D)), 0.0D, 1.0D));
		assertFalse(plane.intersects(new Ray3D(new Point3D(0.0D, 0.0D, +2.0D), new Vector3D(0.0D, 0.0D, +1.0D)), 0.0D, 1.0D));
		assertFalse(plane.intersects(new Ray3D(new Point3D(0.0D, 0.0D, -2.0D), new Vector3D(0.0D, 0.0D, -1.0D)), 0.0D, 1.0D));
		assertFalse(plane.intersects(new Ray3D(new Point3D(0.0D, 0.0D, -2.0D), new Vector3D(0.0D, 0.0D, +1.0D)), 0.0D, 1.0D));
		
		assertFalse(plane.intersects(new Ray3D(new Point3D(0.0D, 0.0D, 0.0D), new Vector3D(1.0D, 0.0D, 0.0D)), 0.0D, 1.0D));
		
		assertTrue(plane.intersects(new Ray3D(new Point3D(0.0D, 0.0D, 2.0D), new Vector3D(0.0D, 0.0D, -1.0D)), 0.0D, 3.0D));
		
		assertThrows(NullPointerException.class, () -> plane.intersectionT(null, 0.0D, 0.0D));
	}
	
	@Test
	public void testSamplePoint2D() {
		final Plane3D plane = new Plane3D();
		
		final Optional<SurfaceSample3D> optionalSurfaceSample = plane.sample(new Point2D());
		
		assertFalse(optionalSurfaceSample.isPresent());
		
		assertThrows(NullPointerException.class, () -> plane.sample(null));
	}
	
	@Test
	public void testToString() {
		final Plane3D plane = new Plane3D();
		
		assertEquals("new Plane3D()", plane.toString());
	}
	
	@Test
	public void testWrite() {
		final Plane3D a = new Plane3D();
		
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		
		final DataOutput dataOutput = new DataOutputStream(byteArrayOutputStream);
		
		a.write(dataOutput);
		
		final byte[] bytes = byteArrayOutputStream.toByteArray();
		
		final Plane3D b = new Plane3DReader().read(new DataInputStream(new ByteArrayInputStream(bytes)));
		
		assertEquals(a, b);
		
		assertThrows(NullPointerException.class, () -> a.write((DataOutput)(null)));
		assertThrows(UncheckedIOException.class, () -> a.write(new DataOutputMock()));
	}
}