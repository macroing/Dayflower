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

import org.dayflower.geometry.Point2D;
import org.dayflower.geometry.Point3D;
import org.dayflower.geometry.Ray3D;
import org.dayflower.geometry.SurfaceIntersection3D;
import org.dayflower.geometry.Vector3D;
import org.dayflower.geometry.boundingvolume.BoundingSphere3D;
import org.dayflower.mock.DataOutputMock;
import org.dayflower.mock.NodeHierarchicalVisitorMock;
import org.dayflower.mock.NodeVisitorMock;
import org.dayflower.node.NodeHierarchicalVisitor;
import org.dayflower.node.NodeTraversalException;
import org.dayflower.node.NodeVisitor;
import org.junit.jupiter.api.Test;

@SuppressWarnings("static-method")
public final class Sphere3DUnitTests {
	public Sphere3DUnitTests() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Test
	public void testAcceptNodeHierarchicalVisitor() {
		final Sphere3D sphere = new Sphere3D();
		
		assertTrue(sphere.accept(new NodeHierarchicalVisitorMock(node -> false,               node -> node.equals(sphere))));
		assertTrue(sphere.accept(new NodeHierarchicalVisitorMock(node -> node.equals(sphere), node -> node.equals(sphere))));
		
		assertThrows(NodeTraversalException.class, () -> sphere.accept(new NodeHierarchicalVisitorMock(null, null)));
		assertThrows(NullPointerException.class, () -> sphere.accept((NodeHierarchicalVisitor)(null)));
	}
	
	@Test
	public void testAcceptNodeVisitor() {
		final Sphere3D sphere = new Sphere3D();
		
		assertThrows(NodeTraversalException.class, () -> sphere.accept(new NodeVisitorMock(true)));
		assertThrows(NullPointerException.class, () -> sphere.accept((NodeVisitor)(null)));
	}
	
	@Test
	public void testConstants() {
		assertEquals(15, Sphere3D.ID);
		assertEquals("Sphere", Sphere3D.NAME);
	}
	
	@Test
	public void testContains() {
		final Sphere3D sphere = new Sphere3D();
		
		assertTrue(sphere.contains(new Point3D(0.0D, 0.0D, 0.0D)));
		
		assertFalse(sphere.contains(new Point3D(2.0D, 2.0D, 2.0D)));
		
		assertThrows(NullPointerException.class, () -> sphere.contains(null));
	}
	
	@Test
	public void testEquals() {
		final Sphere3D a = new Sphere3D();
		final Sphere3D b = new Sphere3D();
		final Sphere3D c = null;
		
		assertEquals(a, a);
		
		assertEquals(a, b);
		assertEquals(b, a);
		
		assertNotEquals(a, c);
		assertNotEquals(c, a);
	}
	
	@Test
	public void testGetBoundingVolume() {
		final Sphere3D sphere = new Sphere3D();
		
		assertEquals(new BoundingSphere3D(1.0D, new Point3D(0.0D, 0.0D, 0.0D)), sphere.getBoundingVolume());
	}
	
	@Test
	public void testGetID() {
		final Sphere3D sphere = new Sphere3D();
		
		assertEquals(Sphere3D.ID, sphere.getID());
	}
	
	@Test
	public void testGetName() {
		final Sphere3D sphere = new Sphere3D();
		
		assertEquals(Sphere3D.NAME, sphere.getName());
	}
	
	@Test
	public void testGetSurfaceArea() {
		final Sphere3D sphere = new Sphere3D();
		
		assertEquals(Math.PI * 4.0D, sphere.getSurfaceArea());
	}
	
	@Test
	public void testHashCode() {
		final Sphere3D a = new Sphere3D();
		final Sphere3D b = new Sphere3D();
		
		assertEquals(a.hashCode(), a.hashCode());
		assertEquals(a.hashCode(), b.hashCode());
	}
	
	@Test
	public void testIntersectionRay3DDoubleDouble() {
		final Sphere3D sphere = new Sphere3D();
		
		final Optional<SurfaceIntersection3D> optionalSurfaceIntersection = sphere.intersection(new Ray3D(new Point3D(0.0D, 0.0D, 2.0D), new Vector3D(0.0D,  0.0D, -1.0D)), 0.0D, 3.0D);
		
		assertNotNull(optionalSurfaceIntersection);
		
		assertTrue(optionalSurfaceIntersection.isPresent());
		
		final SurfaceIntersection3D surfaceIntersection = optionalSurfaceIntersection.get();
		
//		TODO: This needs to be fixed.
//		assertEquals(new OrthonormalBasis33D(new Vector3D(0.0D, 0.0D, 1.0D), new Vector3D(0.0D, 1.0D, 0.0D), new Vector3D(1.0D, 0.0D, 0.0D)), surfaceIntersection.getOrthonormalBasisG());
//		assertEquals(new OrthonormalBasis33D(new Vector3D(0.0D, 0.0D, 1.0D), new Vector3D(0.0D, 1.0D, 0.0D), new Vector3D(1.0D, 0.0D, 0.0D)), surfaceIntersection.getOrthonormalBasisS());
		assertEquals(new Ray3D(new Point3D(0.0D, 0.0D, 2.0D), new Vector3D(0.0D, 0.0D, -1.0D)), surfaceIntersection.getRay());
		assertEquals(sphere, surfaceIntersection.getShape());
		assertEquals(new Point3D(0.0D, 0.0D, 1.0D), surfaceIntersection.getSurfaceIntersectionPoint());
//		assertEquals(new Vector3D(), surfaceIntersection.getSurfaceIntersectionPointError());
		assertEquals(new Vector3D(0.0D, 0.0D, 1.0D), surfaceIntersection.getSurfaceNormalG());
		assertEquals(new Vector3D(0.0D, 0.0D, 1.0D), surfaceIntersection.getSurfaceNormalS());
		assertEquals(1.0D, surfaceIntersection.getT());
		assertEquals(new Point2D(0.0D, 0.0D), surfaceIntersection.getTextureCoordinates());
		
		assertFalse(sphere.intersection(new Ray3D(new Point3D(1.0D, 0.0D, 0.0D), new Vector3D(1.0D, 0.0D, 0.0D)), 0.0D, 1.0D).isPresent());
		
		assertThrows(NullPointerException.class, () -> sphere.intersection(null, 0.0D, 0.0D));
	}
	
//	@Test
//	public void testIntersectionSurfaceIntersector3D() {
//		
//	}
	
	@Test
	public void testIntersectionT() {
		final Sphere3D sphere = new Sphere3D();
		
		assertEquals(Double.NaN, sphere.intersectionT(new Ray3D(new Point3D(0.0D, 0.0D, +2.0D), new Vector3D(0.0D, 0.0D, -1.0D)), 0.0D, 1.0D));
		assertEquals(Double.NaN, sphere.intersectionT(new Ray3D(new Point3D(0.0D, 0.0D, +2.0D), new Vector3D(0.0D, 0.0D, +1.0D)), 0.0D, 1.0D));
		assertEquals(Double.NaN, sphere.intersectionT(new Ray3D(new Point3D(0.0D, 0.0D, -2.0D), new Vector3D(0.0D, 0.0D, -1.0D)), 0.0D, 1.0D));
		assertEquals(Double.NaN, sphere.intersectionT(new Ray3D(new Point3D(0.0D, 0.0D, -2.0D), new Vector3D(0.0D, 0.0D, +1.0D)), 0.0D, 1.0D));
		
		assertEquals(Double.NaN, sphere.intersectionT(new Ray3D(new Point3D(2.0D, 0.0D, 0.0D), new Vector3D(0.0D, 0.0D, +1.0D)), 0.0D, 2.0D));
		
		assertEquals(1.0D, sphere.intersectionT(new Ray3D(new Point3D(0.0D, 0.0D, 0.0D), new Vector3D(0.0D, 0.0D, +1.0D)), 0.0D, 2.0D));
		assertEquals(1.0D, sphere.intersectionT(new Ray3D(new Point3D(0.0D, 0.0D, 2.0D), new Vector3D(0.0D, 0.0D, -1.0D)), 0.0D, 2.0D));
		
		assertThrows(NullPointerException.class, () -> sphere.intersectionT(null, 0.0D, 0.0D));
	}
	
//	@Test
//	public void testIntersects() {
//		
//	}
	
//	@Test
//	public void testSamplePoint2D() {
//		
//	}
	
	@Test
	public void testToString() {
		final Sphere3D sphere = new Sphere3D();
		
		assertEquals("new Sphere3D()", sphere.toString());
	}
	
	@Test
	public void testWrite() {
		final Sphere3D a = new Sphere3D();
		
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		
		final DataOutput dataOutput = new DataOutputStream(byteArrayOutputStream);
		
		a.write(dataOutput);
		
		final byte[] bytes = byteArrayOutputStream.toByteArray();
		
		final Sphere3D b = new Sphere3DReader().read(new DataInputStream(new ByteArrayInputStream(bytes)));
		
		assertEquals(a, b);
		
		assertThrows(NullPointerException.class, () -> a.write((DataOutput)(null)));
		assertThrows(UncheckedIOException.class, () -> a.write(new DataOutputMock()));
	}
}