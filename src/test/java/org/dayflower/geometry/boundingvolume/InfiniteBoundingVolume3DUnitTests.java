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
package org.dayflower.geometry.boundingvolume;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.File;
import java.io.UncheckedIOException;

import org.dayflower.geometry.Matrix44D;
import org.dayflower.geometry.Point3D;
import org.dayflower.geometry.Ray3D;
import org.dayflower.geometry.Vector3D;
import org.dayflower.mock.DataOutputMock;
import org.dayflower.mock.NodeHierarchicalVisitorMock;
import org.dayflower.mock.NodeVisitorMock;

import org.junit.jupiter.api.Test;

import org.macroing.java.util.visitor.NodeHierarchicalVisitor;
import org.macroing.java.util.visitor.NodeTraversalException;
import org.macroing.java.util.visitor.NodeVisitor;

@SuppressWarnings("static-method")
public final class InfiniteBoundingVolume3DUnitTests {
	public InfiniteBoundingVolume3DUnitTests() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Test
	public void testAcceptNodeHierarchicalVisitor() {
		final InfiniteBoundingVolume3D infiniteBoundingVolume = new InfiniteBoundingVolume3D();
		
		assertTrue(infiniteBoundingVolume.accept(new NodeHierarchicalVisitorMock(node -> false,                               node -> node.equals(infiniteBoundingVolume))));
		assertTrue(infiniteBoundingVolume.accept(new NodeHierarchicalVisitorMock(node -> node.equals(infiniteBoundingVolume), node -> node.equals(infiniteBoundingVolume))));
		
		assertThrows(NodeTraversalException.class, () -> infiniteBoundingVolume.accept(new NodeHierarchicalVisitorMock(null, null)));
		assertThrows(NullPointerException.class, () -> infiniteBoundingVolume.accept((NodeHierarchicalVisitor)(null)));
	}
	
	@Test
	public void testAcceptNodeVisitor() {
		final InfiniteBoundingVolume3D infiniteBoundingVolume = new InfiniteBoundingVolume3D();
		
		infiniteBoundingVolume.accept(new NodeVisitorMock());
		
		assertThrows(NodeTraversalException.class, () -> infiniteBoundingVolume.accept(new NodeVisitorMock(true)));
		assertThrows(NullPointerException.class, () -> infiniteBoundingVolume.accept((NodeVisitor)(null)));
	}
	
	@Test
	public void testConstants() {
		assertEquals(3, InfiniteBoundingVolume3D.ID);
	}
	
	@Test
	public void testContains() {
		final InfiniteBoundingVolume3D infiniteBoundingVolume = new InfiniteBoundingVolume3D();
		
		final Point3D point = new Point3D(10.0D, 20.0D, 30.0D);
		
		assertTrue(infiniteBoundingVolume.contains(point));
		
		assertThrows(NullPointerException.class, () -> infiniteBoundingVolume.contains(null));
	}
	
	@Test
	public void testEquals() {
		final InfiniteBoundingVolume3D a = new InfiniteBoundingVolume3D();
		final InfiniteBoundingVolume3D b = new InfiniteBoundingVolume3D();
		final InfiniteBoundingVolume3D c = null;
		
		assertEquals(a, a);
		assertEquals(a, b);
		assertEquals(b, a);
		assertNotEquals(a, c);
		assertNotEquals(c, a);
	}
	
	@Test
	public void testGetClosestPointTo() {
		final InfiniteBoundingVolume3D infiniteBoundingVolume = new InfiniteBoundingVolume3D();
		
		final Point3D a = new Point3D(10.0D, 20.0D, 30.0D);
		final Point3D b = infiniteBoundingVolume.getClosestPointTo(a);
		
		assertTrue(a == b);
		
		assertThrows(NullPointerException.class, () -> infiniteBoundingVolume.getClosestPointTo(null));
	}
	
	@Test
	public void testGetID() {
		final InfiniteBoundingVolume3D infiniteBoundingVolume = new InfiniteBoundingVolume3D();
		
		assertEquals(InfiniteBoundingVolume3D.ID, infiniteBoundingVolume.getID());
	}
	
	@Test
	public void testGetMaximum() {
		final InfiniteBoundingVolume3D infiniteBoundingVolume = new InfiniteBoundingVolume3D();
		
		final Point3D a = new Point3D(Double.POSITIVE_INFINITY);
		final Point3D b = infiniteBoundingVolume.getMaximum();
		
		assertEquals(a, b);
	}
	
	@Test
	public void testGetMidpoint() {
		final InfiniteBoundingVolume3D infiniteBoundingVolume = new InfiniteBoundingVolume3D();
		
		final Point3D a = new Point3D(0.0D, 0.0D, 0.0D);
		final Point3D b = infiniteBoundingVolume.getMidpoint();
		
		assertEquals(a, b);
	}
	
	@Test
	public void testGetMinimum() {
		final InfiniteBoundingVolume3D infiniteBoundingVolume = new InfiniteBoundingVolume3D();
		
		final Point3D a = new Point3D(Double.NEGATIVE_INFINITY);
		final Point3D b = infiniteBoundingVolume.getMinimum();
		
		assertEquals(a, b);
	}
	
	@Test
	public void testGetSurfaceArea() {
		final InfiniteBoundingVolume3D infiniteBoundingVolume = new InfiniteBoundingVolume3D();
		
		assertEquals(Double.POSITIVE_INFINITY, infiniteBoundingVolume.getSurfaceArea());
	}
	
	@Test
	public void testGetVolume() {
		final InfiniteBoundingVolume3D infiniteBoundingVolume = new InfiniteBoundingVolume3D();
		
		assertEquals(Double.POSITIVE_INFINITY, infiniteBoundingVolume.getVolume());
	}
	
	@Test
	public void testHashCode() {
		final InfiniteBoundingVolume3D a = new InfiniteBoundingVolume3D();
		final InfiniteBoundingVolume3D b = new InfiniteBoundingVolume3D();
		
		assertEquals(a.hashCode(), a.hashCode());
		assertEquals(a.hashCode(), b.hashCode());
	}
	
	@Test
	public void testIntersection() {
		final Ray3D ray = new Ray3D(new Point3D(), Vector3D.z());
		
		final double tMinimum = 1.0D;
		final double tMaximum = Double.MAX_VALUE;
		
		final InfiniteBoundingVolume3D infiniteBoundingVolume = new InfiniteBoundingVolume3D();
		
		assertEquals(tMinimum, infiniteBoundingVolume.intersection(ray, tMinimum, tMaximum));
		
		assertThrows(NullPointerException.class, () -> infiniteBoundingVolume.intersection(null, tMinimum, tMaximum));
	}
	
	@Test
	public void testIntersectsBoundingVolume3D() {
		final InfiniteBoundingVolume3D infiniteBoundingVolume = new InfiniteBoundingVolume3D();
		
		assertTrue(infiniteBoundingVolume.intersects(infiniteBoundingVolume));
		
		assertThrows(NullPointerException.class, () -> infiniteBoundingVolume.intersects(null));
	}
	
	@Test
	public void testIntersectsRay3DDoubleDouble() {
		final Ray3D ray = new Ray3D(new Point3D(), Vector3D.z());
		
		final double tMinimum = 1.0D;
		final double tMaximum = Double.MAX_VALUE;
		
		final InfiniteBoundingVolume3D infiniteBoundingVolume = new InfiniteBoundingVolume3D();
		
		assertTrue(infiniteBoundingVolume.intersects(ray, tMinimum, tMaximum));
		
		assertThrows(NullPointerException.class, () -> infiniteBoundingVolume.intersection(null, tMinimum, tMaximum));
	}
	
	@Test
	public void testToString() {
		final InfiniteBoundingVolume3D infiniteBoundingVolume = new InfiniteBoundingVolume3D();
		
		assertEquals("new InfiniteBoundingVolume3D()", infiniteBoundingVolume.toString());
	}
	
	@Test
	public void testTransform() {
		final InfiniteBoundingVolume3D a = new InfiniteBoundingVolume3D();
		final InfiniteBoundingVolume3D b = a.transform(Matrix44D.translate(10.0D, 20.0D, 30.0D));
		
		assertTrue(a == b);
		
		assertThrows(NullPointerException.class, () -> a.transform(null));
	}
	
	@Test
	public void testWrite() {
		final InfiniteBoundingVolume3D a = new InfiniteBoundingVolume3D();
		
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		
		final DataOutput dataOutput = new DataOutputStream(byteArrayOutputStream);
		
		a.write(dataOutput);
		
		final byte[] bytes = byteArrayOutputStream.toByteArray();
		
		final InfiniteBoundingVolume3D b = new InfiniteBoundingVolume3DReader().read(new DataInputStream(new ByteArrayInputStream(bytes)));
		
		assertEquals(a, b);
		
		assertThrows(NullPointerException.class, () -> a.write((DataOutput)(null)));
		assertThrows(UncheckedIOException.class, () -> a.write(new DataOutputMock()));
	}
	
	@Test
	public void testWriteFile() {
		final File directory = new File("./generated/unit-tests");
		
		if(!directory.isDirectory()) {
			directory.mkdirs();
		}
		
		final
		InfiniteBoundingVolume3D infiniteBoundingVolume3D = new InfiniteBoundingVolume3D();
		infiniteBoundingVolume3D.write(new File("./generated/unit-tests/InfiniteBoundingVolume3D"));
		
		assertThrows(NullPointerException.class, () -> infiniteBoundingVolume3D.write((File)(null)));
		assertThrows(UncheckedIOException.class, () -> infiniteBoundingVolume3D.write(new File("./generated/unit-tests/non-existing-directory/InfiniteBoundingVolume3D")));
	}
	
	@Test
	public void testWriteString() {
		final File directory = new File("./generated/unit-tests");
		
		if(!directory.isDirectory()) {
			directory.mkdirs();
		}
		
		final
		InfiniteBoundingVolume3D infiniteBoundingVolume3D = new InfiniteBoundingVolume3D();
		infiniteBoundingVolume3D.write("./generated/unit-tests/InfiniteBoundingVolume3D");
		
		assertThrows(NullPointerException.class, () -> infiniteBoundingVolume3D.write((String)(null)));
		assertThrows(UncheckedIOException.class, () -> infiniteBoundingVolume3D.write("./generated/unit-tests/non-existing-directory/InfiniteBoundingVolume3D"));
	}
}