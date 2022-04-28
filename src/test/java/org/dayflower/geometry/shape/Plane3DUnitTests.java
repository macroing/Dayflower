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
		final Point3D a = new Point3D(0.0D, 0.0D, 0.0D);
		final Point3D b = new Point3D(0.0D, 0.0D, 1.0D);
		final Point3D c = new Point3D(1.0D, 0.0D, 0.0D);
		
		final Plane3D plane = new Plane3D(a, b, c);
		
		final Vector3D surfaceNormal = plane.getSurfaceNormal();
		
		assertTrue(plane.accept(new NodeHierarchicalVisitorMock(node -> false,                                                                                                  node -> node.equals(plane))));
		assertTrue(plane.accept(new NodeHierarchicalVisitorMock(node -> node.equals(plane),                                                                                     node -> node.equals(plane))));
		assertTrue(plane.accept(new NodeHierarchicalVisitorMock(node -> node.equals(plane) || node.equals(a),                                                                   node -> node.equals(plane) || node.equals(a))));
		assertTrue(plane.accept(new NodeHierarchicalVisitorMock(node -> node.equals(plane) || node.equals(a) || node.equals(b),                                                 node -> node.equals(plane) || node.equals(a) || node.equals(b))));
		assertTrue(plane.accept(new NodeHierarchicalVisitorMock(node -> node.equals(plane) || node.equals(a) || node.equals(b) || node.equals(c),                               node -> node.equals(plane) || node.equals(a) || node.equals(b) || node.equals(c))));
		assertTrue(plane.accept(new NodeHierarchicalVisitorMock(node -> node.equals(plane) || node.equals(a) || node.equals(b) || node.equals(c) || node.equals(surfaceNormal), node -> node.equals(plane) || node.equals(a) || node.equals(b) || node.equals(c) || node.equals(surfaceNormal))));
		
		assertThrows(NodeTraversalException.class, () -> plane.accept(new NodeHierarchicalVisitorMock(null, null)));
		assertThrows(NullPointerException.class, () -> plane.accept((NodeHierarchicalVisitor)(null)));
	}
	
	@Test
	public void testAcceptNodeVisitor() {
		final Plane3D plane = new Plane3D(new Point3D(0.0D, 0.0D, 0.0D), new Point3D(0.0D, 0.0D, 1.0D), new Point3D(1.0D, 0.0D, 0.0D));
		
		assertThrows(NodeTraversalException.class, () -> plane.accept(new NodeVisitorMock(true)));
		assertThrows(NullPointerException.class, () -> plane.accept((NodeVisitor)(null)));
	}
	
	@Test
	public void testConstants() {
		assertEquals(10, Plane3D.ID);
		assertEquals("Plane", Plane3D.NAME);
	}
	
	@Test
	public void testConstructor() {
		final Plane3D plane = new Plane3D();
		
		assertEquals(new Point3D(0.0D, 0.0D, 0.0D), plane.getA());
		assertEquals(new Point3D(0.0D, 0.0D, 1.0D), plane.getB());
		assertEquals(new Point3D(1.0D, 0.0D, 0.0D), plane.getC());
		
		assertEquals(new Vector3D(0.0D, 1.0D, 0.0D), plane.getSurfaceNormal());
	}
	
	@Test
	public void testConstructorPoint3DPoint3DPoint3D() {
		final Plane3D plane = new Plane3D(new Point3D(0.0D, 0.0D, 0.0D), new Point3D(0.0D, 1.0D, 0.0D), new Point3D(1.0D, 0.0D, 0.0D));
		
		assertEquals(new Point3D(0.0D, 0.0D, 0.0D), plane.getA());
		assertEquals(new Point3D(0.0D, 1.0D, 0.0D), plane.getB());
		assertEquals(new Point3D(1.0D, 0.0D, 0.0D), plane.getC());
		
		assertEquals(new Vector3D(0.0D, 0.0D, -1.0D), plane.getSurfaceNormal());
		
		assertThrows(NullPointerException.class, () -> new Plane3D(new Point3D(), new Point3D(), null));
		assertThrows(NullPointerException.class, () -> new Plane3D(new Point3D(), null, new Point3D()));
		assertThrows(NullPointerException.class, () -> new Plane3D(null, new Point3D(), new Point3D()));
	}
	
	@Test
	public void testContains() {
		final Plane3D plane = new Plane3D(new Point3D(0.0D, 0.0D, 0.0D), new Point3D(0.0D, 1.0D, 0.0D), new Point3D(1.0D, 0.0D, 0.0D));
		
		assertTrue(plane.contains(new Point3D(2.0D, 2.0D, 0.0D)));
		
		assertFalse(plane.contains(new Point3D(2.0D, 2.0D, 2.0D)));
		
		assertThrows(NullPointerException.class, () -> plane.contains(null));
	}
	
	@Test
	public void testEquals() {
		final Plane3D a = new Plane3D(new Point3D(0.0D, 0.0D, 0.0D), new Point3D(0.0D, 0.0D, 1.0D), new Point3D(1.0D, 0.0D, 0.0D));
		final Plane3D b = new Plane3D(new Point3D(0.0D, 0.0D, 0.0D), new Point3D(0.0D, 0.0D, 1.0D), new Point3D(1.0D, 0.0D, 0.0D));
		final Plane3D c = new Plane3D(new Point3D(0.0D, 0.0D, 0.0D), new Point3D(0.0D, 0.0D, 1.0D), new Point3D(2.0D, 0.0D, 0.0D));
		final Plane3D d = new Plane3D(new Point3D(0.0D, 0.0D, 0.0D), new Point3D(0.0D, 0.0D, 2.0D), new Point3D(1.0D, 0.0D, 0.0D));
		final Plane3D e = new Plane3D(new Point3D(0.0D, 2.0D, 0.0D), new Point3D(0.0D, 0.0D, 1.0D), new Point3D(1.0D, 0.0D, 0.0D));
		final Plane3D f = null;
		
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
	public void testGetA() {
		final Plane3D plane = new Plane3D(new Point3D(0.0D, 0.0D, 0.0D), new Point3D(0.0D, 0.0D, 1.0D), new Point3D(1.0D, 0.0D, 0.0D));
		
		assertEquals(new Point3D(0.0D, 0.0D, 0.0D), plane.getA());
	}
	
	@Test
	public void testGetB() {
		final Plane3D plane = new Plane3D(new Point3D(0.0D, 0.0D, 0.0D), new Point3D(0.0D, 0.0D, 1.0D), new Point3D(1.0D, 0.0D, 0.0D));
		
		assertEquals(new Point3D(0.0D, 0.0D, 1.0D), plane.getB());
	}
	
	@Test
	public void testGetBoundingVolume() {
		final Plane3D plane = new Plane3D(new Point3D(0.0D, 0.0D, 0.0D), new Point3D(0.0D, 0.0D, 1.0D), new Point3D(1.0D, 0.0D, 0.0D));
		
		assertEquals(new InfiniteBoundingVolume3D(), plane.getBoundingVolume());
	}
	
	@Test
	public void testGetC() {
		final Plane3D plane = new Plane3D(new Point3D(0.0D, 0.0D, 0.0D), new Point3D(0.0D, 0.0D, 1.0D), new Point3D(1.0D, 0.0D, 0.0D));
		
		assertEquals(new Point3D(1.0D, 0.0D, 0.0D), plane.getC());
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
		final Plane3D plane = new Plane3D(new Point3D(0.0D, 0.0D, 0.0D), new Point3D(0.0D, 0.0D, 1.0D), new Point3D(1.0D, 0.0D, 0.0D));
		
		assertEquals(Double.POSITIVE_INFINITY, plane.getSurfaceArea());
	}
	
	@Test
	public void testGetSurfaceNormal() {
		final Plane3D plane = new Plane3D(new Point3D(0.0D, 0.0D, 0.0D), new Point3D(0.0D, 0.0D, 1.0D), new Point3D(1.0D, 0.0D, 0.0D));
		
		assertEquals(new Vector3D(0.0D, 1.0D, 0.0D), plane.getSurfaceNormal());
	}
	
	@Test
	public void testHashCode() {
		final Plane3D a = new Plane3D(new Point3D(0.0D, 0.0D, 0.0D), new Point3D(0.0D, 0.0D, 1.0D), new Point3D(1.0D, 0.0D, 0.0D));
		final Plane3D b = new Plane3D(new Point3D(0.0D, 0.0D, 0.0D), new Point3D(0.0D, 0.0D, 1.0D), new Point3D(1.0D, 0.0D, 0.0D));
		
		assertEquals(a.hashCode(), a.hashCode());
		assertEquals(a.hashCode(), b.hashCode());
	}
	
	@Test
	public void testIntersection() {
		final Plane3D planeA = new Plane3D(new Point3D(0.0D, 0.0D, 0.0D), new Point3D(0.0D, 0.0D, 1.0D), new Point3D(1.0D, 0.0D, 0.0D));
		final Plane3D planeB = new Plane3D(new Point3D(0.0D, 0.0D, 0.0D), new Point3D(0.0D, 1.0D, 0.0D), new Point3D(1.0D, 0.0D, 0.0D));
		
		final Optional<SurfaceIntersection3D> optionalSurfaceIntersectionA = planeA.intersection(new Ray3D(new Point3D(1.0D, 2.0D, 3.0D), new Vector3D(0.0D, -1.0D,  0.0D)), 0.0D, 3.0D);
		final Optional<SurfaceIntersection3D> optionalSurfaceIntersectionB = planeB.intersection(new Ray3D(new Point3D(1.0D, 3.0D, 2.0D), new Vector3D(0.0D,  0.0D, -1.0D)), 0.0D, 3.0D);
		
		assertNotNull(optionalSurfaceIntersectionA);
		assertNotNull(optionalSurfaceIntersectionB);
		
		assertTrue(optionalSurfaceIntersectionA.isPresent());
		assertTrue(optionalSurfaceIntersectionB.isPresent());
		
		final SurfaceIntersection3D surfaceIntersectionA = optionalSurfaceIntersectionA.get();
		final SurfaceIntersection3D surfaceIntersectionB = optionalSurfaceIntersectionB.get();
		
		/*
		 * TODO: Find out if the OrthonormalBasis33D instances and the Point2D instance needs to be changed.
		 */
		
		assertEquals(new OrthonormalBasis33D(new Vector3D(0.0D, 1.0D, 0.0D), new Vector3D(1.0D, -0.0D, 0.0D), new Vector3D(-0.0D, 0.0D, 1.0D)), surfaceIntersectionA.getOrthonormalBasisG());
		assertEquals(new OrthonormalBasis33D(new Vector3D(0.0D, 1.0D, 0.0D), new Vector3D(1.0D, -0.0D, 0.0D), new Vector3D(-0.0D, 0.0D, 1.0D)), surfaceIntersectionA.getOrthonormalBasisS());
		assertEquals(new Ray3D(new Point3D(1.0D, 2.0D, 3.0D), new Vector3D(0.0D, -1.0D, 0.0D)), surfaceIntersectionA.getRay());
		assertEquals(planeA, surfaceIntersectionA.getShape());
		assertEquals(new Point3D(1.0D, 0.0D, 3.0D), surfaceIntersectionA.getSurfaceIntersectionPoint());
		assertEquals(new Vector3D(), surfaceIntersectionA.getSurfaceIntersectionPointError());
		assertEquals(new Vector3D(0.0D, 1.0D, 0.0D), surfaceIntersectionA.getSurfaceNormalG());
		assertEquals(new Vector3D(0.0D, 1.0D, 0.0D), surfaceIntersectionA.getSurfaceNormalS());
		assertEquals(2.0D, surfaceIntersectionA.getT());
		assertEquals(new Point2D(3.0D, 1.0D), surfaceIntersectionA.getTextureCoordinates());
		
		assertEquals(new OrthonormalBasis33D(new Vector3D(0.0D, 0.0D, -1.0D), new Vector3D(-1.0D, 0.0D, -0.0D), new Vector3D(0.0D, -1.0D, -0.0D)), surfaceIntersectionB.getOrthonormalBasisG());
		assertEquals(new OrthonormalBasis33D(new Vector3D(0.0D, 0.0D, -1.0D), new Vector3D(-1.0D, 0.0D, -0.0D), new Vector3D(0.0D, -1.0D, -0.0D)), surfaceIntersectionB.getOrthonormalBasisS());
		assertEquals(new Ray3D(new Point3D(1.0D, 3.0D, 2.0D), new Vector3D(0.0D, 0.0D, -1.0D)), surfaceIntersectionB.getRay());
		assertEquals(planeB, surfaceIntersectionB.getShape());
		assertEquals(new Point3D(1.0D, 3.0D, 0.0D), surfaceIntersectionB.getSurfaceIntersectionPoint());
		assertEquals(new Vector3D(), surfaceIntersectionB.getSurfaceIntersectionPointError());
		assertEquals(new Vector3D(0.0D, 0.0D, -1.0D), surfaceIntersectionB.getSurfaceNormalG());
		assertEquals(new Vector3D(0.0D, 0.0D, -1.0D), surfaceIntersectionB.getSurfaceNormalS());
		assertEquals(2.0D, surfaceIntersectionB.getT());
		assertEquals(new Point2D(3.0D, 1.0D), surfaceIntersectionB.getTextureCoordinates());
		
		assertFalse(planeA.intersection(new Ray3D(new Point3D(0.0D, 0.0D, 0.0D), new Vector3D(1.0D, 0.0D, 0.0D)), 0.0D, 1.0D).isPresent());
		
		assertThrows(NullPointerException.class, () -> planeA.intersection(null, 0.0D, 0.0D));
	}
	
	@Test
	public void testIntersectionT() {
		final Plane3D plane = new Plane3D(new Point3D(0.0D, 0.0D, 0.0D), new Point3D(0.0D, 0.0D, 1.0D), new Point3D(1.0D, 0.0D, 0.0D));
		
		assertEquals(Double.NaN, plane.intersectionT(new Ray3D(new Point3D(0.0D, +2.0D, 0.0D), new Vector3D(0.0D, -1.0D, 0.0D)), 0.0D, 1.0D));
		assertEquals(Double.NaN, plane.intersectionT(new Ray3D(new Point3D(0.0D, +2.0D, 0.0D), new Vector3D(0.0D, +1.0D, 0.0D)), 0.0D, 1.0D));
		assertEquals(Double.NaN, plane.intersectionT(new Ray3D(new Point3D(0.0D, -2.0D, 0.0D), new Vector3D(0.0D, -1.0D, 0.0D)), 0.0D, 1.0D));
		assertEquals(Double.NaN, plane.intersectionT(new Ray3D(new Point3D(0.0D, -2.0D, 0.0D), new Vector3D(0.0D, +1.0D, 0.0D)), 0.0D, 1.0D));
		
		assertEquals(Double.NaN, plane.intersectionT(new Ray3D(new Point3D(0.0D, 0.0D, 0.0D), new Vector3D(1.0D, 0.0D, 0.0D)), 0.0D, 1.0D));
		
		assertEquals(2.0D, plane.intersectionT(new Ray3D(new Point3D(0.0D, 2.0D, 0.0D), new Vector3D(0.0D, -1.0D, 0.0D)), 0.0D, 3.0D));
		
		assertThrows(NullPointerException.class, () -> plane.intersectionT(null, 0.0D, 0.0D));
	}
	
	@Test
	public void testIntersects() {
		final Plane3D plane = new Plane3D(new Point3D(0.0D, 0.0D, 0.0D), new Point3D(0.0D, 0.0D, 1.0D), new Point3D(1.0D, 0.0D, 0.0D));
		
		assertFalse(plane.intersects(new Ray3D(new Point3D(0.0D, +2.0D, 0.0D), new Vector3D(0.0D, -1.0D, 0.0D)), 0.0D, 1.0D));
		assertFalse(plane.intersects(new Ray3D(new Point3D(0.0D, +2.0D, 0.0D), new Vector3D(0.0D, +1.0D, 0.0D)), 0.0D, 1.0D));
		assertFalse(plane.intersects(new Ray3D(new Point3D(0.0D, -2.0D, 0.0D), new Vector3D(0.0D, -1.0D, 0.0D)), 0.0D, 1.0D));
		assertFalse(plane.intersects(new Ray3D(new Point3D(0.0D, -2.0D, 0.0D), new Vector3D(0.0D, +1.0D, 0.0D)), 0.0D, 1.0D));
		
		assertFalse(plane.intersects(new Ray3D(new Point3D(0.0D, 0.0D, 0.0D), new Vector3D(1.0D, 0.0D, 0.0D)), 0.0D, 1.0D));
		
		assertTrue(plane.intersects(new Ray3D(new Point3D(0.0D, 2.0D, 0.0D), new Vector3D(0.0D, -1.0D, 0.0D)), 0.0D, 3.0D));
		
		assertThrows(NullPointerException.class, () -> plane.intersectionT(null, 0.0D, 0.0D));
	}
	
	@Test
	public void testToString() {
		final Plane3D plane = new Plane3D(new Point3D(0.0D, 0.0D, 0.0D), new Point3D(0.0D, 0.0D, 1.0D), new Point3D(1.0D, 0.0D, 0.0D));
		
		assertEquals("new Plane3D(new Point3D(0.0D, 0.0D, 0.0D), new Point3D(0.0D, 0.0D, 1.0D), new Point3D(1.0D, 0.0D, 0.0D))", plane.toString());
	}
	
	@Test
	public void testWrite() {
		final Plane3D a = new Plane3D(new Point3D(0.0D, 0.0D, 0.0D), new Point3D(0.0D, 0.0D, 1.0D), new Point3D(1.0D, 0.0D, 0.0D));
		
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