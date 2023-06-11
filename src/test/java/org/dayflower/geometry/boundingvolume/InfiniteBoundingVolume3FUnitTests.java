/**
 * Copyright 2014 - 2023 J&#246;rgen Lundgren
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

import org.dayflower.geometry.Matrix44F;
import org.dayflower.geometry.Point3F;
import org.dayflower.geometry.Ray3F;
import org.dayflower.geometry.Vector3F;
import org.dayflower.mock.DataOutputMock;
import org.dayflower.mock.NodeHierarchicalVisitorMock;
import org.dayflower.mock.NodeVisitorMock;

import org.junit.jupiter.api.Test;

import org.macroing.java.util.visitor.NodeHierarchicalVisitor;
import org.macroing.java.util.visitor.NodeTraversalException;
import org.macroing.java.util.visitor.NodeVisitor;

@SuppressWarnings("static-method")
public final class InfiniteBoundingVolume3FUnitTests {
	public InfiniteBoundingVolume3FUnitTests() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Test
	public void testAcceptNodeHierarchicalVisitor() {
		final InfiniteBoundingVolume3F infiniteBoundingVolume = new InfiniteBoundingVolume3F();
		
		assertTrue(infiniteBoundingVolume.accept(new NodeHierarchicalVisitorMock(node -> false,                               node -> node.equals(infiniteBoundingVolume))));
		assertTrue(infiniteBoundingVolume.accept(new NodeHierarchicalVisitorMock(node -> node.equals(infiniteBoundingVolume), node -> node.equals(infiniteBoundingVolume))));
		
		assertThrows(NodeTraversalException.class, () -> infiniteBoundingVolume.accept(new NodeHierarchicalVisitorMock(null, null)));
		assertThrows(NullPointerException.class, () -> infiniteBoundingVolume.accept((NodeHierarchicalVisitor)(null)));
	}
	
	@Test
	public void testAcceptNodeVisitor() {
		final InfiniteBoundingVolume3F infiniteBoundingVolume = new InfiniteBoundingVolume3F();
		
		infiniteBoundingVolume.accept(new NodeVisitorMock());
		
		assertThrows(NodeTraversalException.class, () -> infiniteBoundingVolume.accept(new NodeVisitorMock(true)));
		assertThrows(NullPointerException.class, () -> infiniteBoundingVolume.accept((NodeVisitor)(null)));
	}
	
	@Test
	public void testConstants() {
		assertEquals(3, InfiniteBoundingVolume3F.ID);
	}
	
	@Test
	public void testContains() {
		final InfiniteBoundingVolume3F infiniteBoundingVolume = new InfiniteBoundingVolume3F();
		
		final Point3F point = new Point3F(10.0F, 20.0F, 30.0F);
		
		assertTrue(infiniteBoundingVolume.contains(point));
		
		assertThrows(NullPointerException.class, () -> infiniteBoundingVolume.contains(null));
	}
	
	@Test
	public void testEquals() {
		final InfiniteBoundingVolume3F a = new InfiniteBoundingVolume3F();
		final InfiniteBoundingVolume3F b = new InfiniteBoundingVolume3F();
		final InfiniteBoundingVolume3F c = null;
		
		assertEquals(a, a);
		assertEquals(a, b);
		assertEquals(b, a);
		assertNotEquals(a, c);
		assertNotEquals(c, a);
	}
	
	@Test
	public void testGetClosestPointTo() {
		final InfiniteBoundingVolume3F infiniteBoundingVolume = new InfiniteBoundingVolume3F();
		
		final Point3F a = new Point3F(10.0F, 20.0F, 30.0F);
		final Point3F b = infiniteBoundingVolume.getClosestPointTo(a);
		
		assertTrue(a == b);
		
		assertThrows(NullPointerException.class, () -> infiniteBoundingVolume.getClosestPointTo(null));
	}
	
	@Test
	public void testGetID() {
		final InfiniteBoundingVolume3F infiniteBoundingVolume = new InfiniteBoundingVolume3F();
		
		assertEquals(InfiniteBoundingVolume3F.ID, infiniteBoundingVolume.getID());
	}
	
	@Test
	public void testGetMaximum() {
		final InfiniteBoundingVolume3F infiniteBoundingVolume = new InfiniteBoundingVolume3F();
		
		final Point3F a = new Point3F(Float.POSITIVE_INFINITY);
		final Point3F b = infiniteBoundingVolume.getMaximum();
		
		assertEquals(a, b);
	}
	
	@Test
	public void testGetMidpoint() {
		final InfiniteBoundingVolume3F infiniteBoundingVolume = new InfiniteBoundingVolume3F();
		
		final Point3F a = new Point3F(0.0F, 0.0F, 0.0F);
		final Point3F b = infiniteBoundingVolume.getMidpoint();
		
		assertEquals(a, b);
	}
	
	@Test
	public void testGetMinimum() {
		final InfiniteBoundingVolume3F infiniteBoundingVolume = new InfiniteBoundingVolume3F();
		
		final Point3F a = new Point3F(Float.NEGATIVE_INFINITY);
		final Point3F b = infiniteBoundingVolume.getMinimum();
		
		assertEquals(a, b);
	}
	
	@Test
	public void testGetSurfaceArea() {
		final InfiniteBoundingVolume3F infiniteBoundingVolume = new InfiniteBoundingVolume3F();
		
		assertEquals(Float.POSITIVE_INFINITY, infiniteBoundingVolume.getSurfaceArea());
	}
	
	@Test
	public void testGetVolume() {
		final InfiniteBoundingVolume3F infiniteBoundingVolume = new InfiniteBoundingVolume3F();
		
		assertEquals(Float.POSITIVE_INFINITY, infiniteBoundingVolume.getVolume());
	}
	
	@Test
	public void testHashCode() {
		final InfiniteBoundingVolume3F a = new InfiniteBoundingVolume3F();
		final InfiniteBoundingVolume3F b = new InfiniteBoundingVolume3F();
		
		assertEquals(a.hashCode(), a.hashCode());
		assertEquals(a.hashCode(), b.hashCode());
	}
	
	@Test
	public void testIntersection() {
		final Ray3F ray = new Ray3F(new Point3F(), Vector3F.z());
		
		final float tMinimum = 1.0F;
		final float tMaximum = Float.MAX_VALUE;
		
		final InfiniteBoundingVolume3F infiniteBoundingVolume = new InfiniteBoundingVolume3F();
		
		assertEquals(tMinimum, infiniteBoundingVolume.intersection(ray, tMinimum, tMaximum));
		
		assertThrows(NullPointerException.class, () -> infiniteBoundingVolume.intersection(null, tMinimum, tMaximum));
	}
	
	@Test
	public void testIntersectsBoundingVolume3F() {
		final InfiniteBoundingVolume3F infiniteBoundingVolume = new InfiniteBoundingVolume3F();
		
		assertTrue(infiniteBoundingVolume.intersects(infiniteBoundingVolume));
		
		assertThrows(NullPointerException.class, () -> infiniteBoundingVolume.intersects(null));
	}
	
	@Test
	public void testIntersectsRay3FFloatFloat() {
		final Ray3F ray = new Ray3F(new Point3F(), Vector3F.z());
		
		final float tMinimum = 1.0F;
		final float tMaximum = Float.MAX_VALUE;
		
		final InfiniteBoundingVolume3F infiniteBoundingVolume = new InfiniteBoundingVolume3F();
		
		assertTrue(infiniteBoundingVolume.intersects(ray, tMinimum, tMaximum));
		
		assertThrows(NullPointerException.class, () -> infiniteBoundingVolume.intersection(null, tMinimum, tMaximum));
	}
	
	@Test
	public void testToString() {
		final InfiniteBoundingVolume3F infiniteBoundingVolume = new InfiniteBoundingVolume3F();
		
		assertEquals("new InfiniteBoundingVolume3F()", infiniteBoundingVolume.toString());
	}
	
	@Test
	public void testTransform() {
		final InfiniteBoundingVolume3F a = new InfiniteBoundingVolume3F();
		final InfiniteBoundingVolume3F b = a.transform(Matrix44F.translate(10.0F, 20.0F, 30.0F));
		
		assertTrue(a == b);
		
		assertThrows(NullPointerException.class, () -> a.transform(null));
	}
	
	@Test
	public void testWrite() {
		final InfiniteBoundingVolume3F a = new InfiniteBoundingVolume3F();
		
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		
		final DataOutput dataOutput = new DataOutputStream(byteArrayOutputStream);
		
		a.write(dataOutput);
		
		final byte[] bytes = byteArrayOutputStream.toByteArray();
		
		final InfiniteBoundingVolume3F b = new InfiniteBoundingVolume3FReader().read(new DataInputStream(new ByteArrayInputStream(bytes)));
		
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
		InfiniteBoundingVolume3F infiniteBoundingVolume3F = new InfiniteBoundingVolume3F();
		infiniteBoundingVolume3F.write(new File("./generated/unit-tests/InfiniteBoundingVolume3F"));
		
		assertThrows(NullPointerException.class, () -> infiniteBoundingVolume3F.write((File)(null)));
		assertThrows(UncheckedIOException.class, () -> infiniteBoundingVolume3F.write(new File("./generated/unit-tests/non-existing-directory/InfiniteBoundingVolume3F")));
	}
	
	@Test
	public void testWriteString() {
		final File directory = new File("./generated/unit-tests");
		
		if(!directory.isDirectory()) {
			directory.mkdirs();
		}
		
		final
		InfiniteBoundingVolume3F infiniteBoundingVolume3F = new InfiniteBoundingVolume3F();
		infiniteBoundingVolume3F.write("./generated/unit-tests/InfiniteBoundingVolume3F");
		
		assertThrows(NullPointerException.class, () -> infiniteBoundingVolume3F.write((String)(null)));
		assertThrows(UncheckedIOException.class, () -> infiniteBoundingVolume3F.write("./generated/unit-tests/non-existing-directory/InfiniteBoundingVolume3F"));
	}
}