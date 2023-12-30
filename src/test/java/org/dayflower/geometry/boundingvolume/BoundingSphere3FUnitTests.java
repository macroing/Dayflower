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
import static org.junit.jupiter.api.Assertions.assertFalse;
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
public final class BoundingSphere3FUnitTests {
	public BoundingSphere3FUnitTests() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Test
	public void testAcceptNodeHierarchicalVisitor() {
		final Point3F center = new Point3F(0.0F, 1.0F, 2.0F);
		
		final BoundingSphere3F boundingSphere = new BoundingSphere3F(1.0F, center);
		
		assertTrue(boundingSphere.accept(new NodeHierarchicalVisitorMock(node -> false,                                              node -> node.equals(boundingSphere))));
		assertTrue(boundingSphere.accept(new NodeHierarchicalVisitorMock(node -> node.equals(boundingSphere),                        node -> node.equals(boundingSphere))));
		assertTrue(boundingSphere.accept(new NodeHierarchicalVisitorMock(node -> node.equals(boundingSphere) || node.equals(center), node -> node.equals(boundingSphere) || node.equals(center))));
		
		assertThrows(NodeTraversalException.class, () -> boundingSphere.accept(new NodeHierarchicalVisitorMock(null, null)));
		assertThrows(NullPointerException.class, () -> boundingSphere.accept((NodeHierarchicalVisitor)(null)));
	}
	
	@Test
	public void testAcceptNodeVisitor() {
		final BoundingSphere3F boundingSphere = new BoundingSphere3F();
		
		assertThrows(NodeTraversalException.class, () -> boundingSphere.accept(new NodeVisitorMock(true)));
		assertThrows(NullPointerException.class, () -> boundingSphere.accept((NodeVisitor)(null)));
	}
	
	@Test
	public void testConstants() {
		assertEquals(2, BoundingSphere3F.ID);
	}
	
	@Test
	public void testConstructor() {
		final BoundingSphere3F boundingSphere = new BoundingSphere3F();
		
		assertEquals(1.0F, boundingSphere.getRadius());
		assertEquals(new Point3F(0.0F, 0.0F, 0.0F), boundingSphere.getCenter());
	}
	
	@Test
	public void testConstructorFloat() {
		final BoundingSphere3F boundingSphere = new BoundingSphere3F(2.0F);
		
		assertEquals(2.0F, boundingSphere.getRadius());
		assertEquals(new Point3F(0.0F, 0.0F, 0.0F), boundingSphere.getCenter());
	}
	
	@Test
	public void testConstructorFloatPoint3F() {
		final BoundingSphere3F boundingSphere = new BoundingSphere3F(2.0F, new Point3F(1.0F, 1.0F, 1.0F));
		
		assertEquals(2.0F, boundingSphere.getRadius());
		assertEquals(new Point3F(1.0F, 1.0F, 1.0F), boundingSphere.getCenter());
		
		assertThrows(NullPointerException.class, () -> new BoundingSphere3F(1.0F, null));
	}
	
	@Test
	public void testContains() {
		final BoundingSphere3F boundingSphere = new BoundingSphere3F(5.0F, new Point3F(0.0F, 0.0F, 0.0F));
		
		final Point3F a = new Point3F(+9.0F, +0.0F, +0.0F);
		final Point3F b = new Point3F(-9.0F, +0.0F, +0.0F);
		final Point3F c = new Point3F(+0.0F, +9.0F, +0.0F);
		final Point3F d = new Point3F(+0.0F, -9.0F, +0.0F);
		final Point3F e = new Point3F(+0.0F, +0.0F, +9.0F);
		final Point3F f = new Point3F(+0.0F, +0.0F, -9.0F);
		
		final Point3F g = new Point3F(+5.0F, +0.0F, +0.0F);
		final Point3F h = new Point3F(-5.0F, +0.0F, +0.0F);
		final Point3F i = new Point3F(+0.0F, +5.0F, +0.0F);
		final Point3F j = new Point3F(+0.0F, -5.0F, +0.0F);
		final Point3F k = new Point3F(+0.0F, +0.0F, +5.0F);
		final Point3F l = new Point3F(+0.0F, +0.0F, -5.0F);
		final Point3F m = new Point3F(+0.0F, +0.0F, +0.0F);
		
		assertFalse(boundingSphere.contains(a));
		assertFalse(boundingSphere.contains(b));
		assertFalse(boundingSphere.contains(c));
		assertFalse(boundingSphere.contains(d));
		assertFalse(boundingSphere.contains(e));
		assertFalse(boundingSphere.contains(f));
		
		assertTrue(boundingSphere.contains(g));
		assertTrue(boundingSphere.contains(h));
		assertTrue(boundingSphere.contains(i));
		assertTrue(boundingSphere.contains(j));
		assertTrue(boundingSphere.contains(k));
		assertTrue(boundingSphere.contains(l));
		assertTrue(boundingSphere.contains(m));
		
		assertThrows(NullPointerException.class, () -> boundingSphere.contains(null));
	}
	
	@Test
	public void testEquals() {
		final BoundingSphere3F a = new BoundingSphere3F(1.0F, new Point3F(0.0F, 0.0F, 0.0F));
		final BoundingSphere3F b = new BoundingSphere3F(1.0F, new Point3F(0.0F, 0.0F, 0.0F));
		final BoundingSphere3F c = new BoundingSphere3F(1.0F, new Point3F(1.0F, 1.0F, 1.0F));
		final BoundingSphere3F d = new BoundingSphere3F(2.0F, new Point3F(0.0F, 0.0F, 0.0F));
		final BoundingSphere3F e = null;
		
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
	public void testGetCenter() {
		final BoundingSphere3F boundingSphere = new BoundingSphere3F(1.0F, new Point3F(10.0F, 20.0F, 30.0F));
		
		assertEquals(new Point3F(10.0F, 20.0F, 30.0F), boundingSphere.getCenter());
	}
	
	@Test
	public void testGetClosestPointTo() {
		final BoundingSphere3F boundingSphere = new BoundingSphere3F(5.0F, new Point3F(0.0F, 0.0F, 0.0F));
		
		final Point3F a = new Point3F(+9.0F, +0.0F, +0.0F);
		final Point3F b = new Point3F(-9.0F, +0.0F, +0.0F);
		final Point3F c = new Point3F(+0.0F, +9.0F, +0.0F);
		final Point3F d = new Point3F(+0.0F, -9.0F, +0.0F);
		final Point3F e = new Point3F(+0.0F, +0.0F, +9.0F);
		final Point3F f = new Point3F(+0.0F, +0.0F, -9.0F);
		final Point3F g = new Point3F(+0.0F, +0.0F, +0.0F);
		
		final Point3F h = new Point3F(+5.0F, +0.0F, +0.0F);
		final Point3F i = new Point3F(-5.0F, +0.0F, +0.0F);
		final Point3F j = new Point3F(+0.0F, +5.0F, +0.0F);
		final Point3F k = new Point3F(+0.0F, -5.0F, +0.0F);
		final Point3F l = new Point3F(+0.0F, +0.0F, +5.0F);
		final Point3F m = new Point3F(+0.0F, +0.0F, -5.0F);
		final Point3F n = new Point3F(+0.0F, +0.0F, +0.0F);
		
		final Point3F o = boundingSphere.getClosestPointTo(a);
		final Point3F p = boundingSphere.getClosestPointTo(b);
		final Point3F q = boundingSphere.getClosestPointTo(c);
		final Point3F r = boundingSphere.getClosestPointTo(d);
		final Point3F s = boundingSphere.getClosestPointTo(e);
		final Point3F t = boundingSphere.getClosestPointTo(f);
		final Point3F u = boundingSphere.getClosestPointTo(g);
		
		assertEquals(h, o);
		assertEquals(i, p);
		assertEquals(j, q);
		assertEquals(k, r);
		assertEquals(l, s);
		assertEquals(m, t);
		assertEquals(n, u);
		
		assertThrows(NullPointerException.class, () -> boundingSphere.getClosestPointTo(null));
	}
	
	@Test
	public void testGetID() {
		final BoundingSphere3F boundingSphere = new BoundingSphere3F();
		
		assertEquals(BoundingSphere3F.ID, boundingSphere.getID());
	}
	
	@Test
	public void testGetMaximum() {
		final BoundingSphere3F boundingSphere = new BoundingSphere3F(5.0F, new Point3F(0.0F, 0.0F, 0.0F));
		
		assertEquals(new Point3F(+5.0F, +5.0F, +5.0F), boundingSphere.getMaximum());
	}
	
	@Test
	public void testGetMidpoint() {
		final BoundingSphere3F boundingSphere = new BoundingSphere3F(5.0F, new Point3F(0.0F, 0.0F, 0.0F));
		
		assertEquals(new Point3F(0.0F, 0.0F, 0.0F), boundingSphere.getMidpoint());
	}
	
	@Test
	public void testGetMinimum() {
		final BoundingSphere3F boundingSphere = new BoundingSphere3F(5.0F, new Point3F(0.0F, 0.0F, 0.0F));
		
		assertEquals(new Point3F(-5.0F, -5.0F, -5.0F), boundingSphere.getMinimum());
	}
	
	@Test
	public void testGetRadius() {
		final BoundingSphere3F boundingSphere = new BoundingSphere3F(2.0F, new Point3F(0.0F, 0.0F, 0.0F));
		
		assertEquals(2.0F, boundingSphere.getRadius());
	}
	
	@Test
	public void testGetRadiusSquared() {
		final BoundingSphere3F boundingSphere = new BoundingSphere3F(2.0F, new Point3F(0.0F, 0.0F, 0.0F));
		
		assertEquals(4.0F, boundingSphere.getRadiusSquared());
	}
	
	@Test
	public void testGetSurfaceArea() {
		final BoundingSphere3F a = new BoundingSphere3F(1.0F);
		final BoundingSphere3F b = new BoundingSphere3F(2.0F);
		
		final float expectedA = (float)(Math.PI) * 4.0F * 1.0F;
		final float expectedB = (float)(Math.PI) * 4.0F * 4.0F;
		
		assertEquals(expectedA, a.getSurfaceArea());
		assertEquals(expectedB, b.getSurfaceArea());
	}
	
	@Test
	public void testGetVolume() {
		final BoundingSphere3F a = new BoundingSphere3F(1.0F);
		final BoundingSphere3F b = new BoundingSphere3F(2.0F);
		
		final float expectedA = 4.0F / 3.0F * (float)(Math.PI) * 1.0F;
		final float expectedB = 4.0F / 3.0F * (float)(Math.PI) * 8.0F;
		
		assertEquals(expectedA, a.getVolume());
		assertEquals(expectedB, b.getVolume());
	}
	
	@Test
	public void testHashCode() {
		final BoundingSphere3F a = new BoundingSphere3F();
		final BoundingSphere3F b = new BoundingSphere3F();
		
		assertEquals(a.hashCode(), a.hashCode());
		assertEquals(a.hashCode(), b.hashCode());
	}
	
	@Test
	public void testIntersection() {
		final BoundingSphere3F boundingSphere = new BoundingSphere3F(5.0F, new Point3F(0.0F, 0.0F, 10.0F));
		
		final Ray3F rayA = new Ray3F(new Point3F(0.0F, 0.0F, 0.0F), Vector3F.z());
		final Ray3F rayB = new Ray3F(new Point3F(0.0F, 0.0F, 0.0F), Vector3F.x());
		
		final float tMinimumA =  0.0F;
		final float tMinimumB =  5.0F;
		final float tMinimumC = 15.0F;
		
		final float tMaximumA = Float.MAX_VALUE;
		final float tMaximumB = 5.0F;
		
		final float expectedTA =  5.0F;
		final float expectedTB = 15.0F;
		final float expectedTC = Float.NaN;
		
		assertEquals(expectedTA, boundingSphere.intersection(rayA, tMinimumA, tMaximumA));
		assertEquals(expectedTB, boundingSphere.intersection(rayA, tMinimumB, tMaximumA));
		assertEquals(expectedTC, boundingSphere.intersection(rayA, tMinimumC, tMaximumA));
		assertEquals(expectedTC, boundingSphere.intersection(rayA, tMinimumA, tMaximumB));
		
		assertEquals(expectedTC, boundingSphere.intersection(rayB, tMinimumA, tMaximumA));
		
		assertThrows(NullPointerException.class, () -> boundingSphere.intersection(null, 0.0F, Float.MAX_VALUE));
	}
	
	@Test
	public void testIntersectsBoundingVolume3F() {
		final BoundingSphere3F boundingSphere = new BoundingSphere3F();
		
		assertTrue(boundingSphere.intersects(boundingSphere));
		
		assertThrows(NullPointerException.class, () -> boundingSphere.intersects(null));
	}
	
	@Test
	public void testIntersectsRay3FFloatFloat() {
		final BoundingSphere3F boundingSphere = new BoundingSphere3F(5.0F, new Point3F(0.0F, 0.0F, 10.0F));
		
		assertTrue(boundingSphere.intersects(new Ray3F(new Point3F(0.0F, 0.0F, 0.0F), Vector3F.z()), 0.0F, Float.MAX_VALUE));
		
		assertFalse(boundingSphere.intersects(new Ray3F(new Point3F(0.0F, 0.0F, 0.0F), Vector3F.x()), 0.0F, Float.MAX_VALUE));
		
		assertThrows(NullPointerException.class, () -> boundingSphere.intersects(null, 0.0F, Float.MAX_VALUE));
	}
	
	@Test
	public void testToString() {
		final BoundingSphere3F boundingSphere = new BoundingSphere3F(1.0F, new Point3F(0.0F, 0.0F, 0.0F));
		
		assertEquals("new BoundingSphere3F(1.0F, new Point3F(0.0F, 0.0F, 0.0F))", boundingSphere.toString());
	}
	
	@Test
	public void testTransform() {
		final BoundingSphere3F a = new BoundingSphere3F(2.0F, new Point3F(10.0F, 20.0F, 30.0F));
		final BoundingSphere3F b = a.transform(Matrix44F.translate(+10.0F, +20.0F, +30.0F));
		final BoundingSphere3F c = b.transform(Matrix44F.translate(-10.0F, -20.0F, -30.0F));
		
		assertEquals(new Point3F(20.0F, 40.0F, 60.0F), b.getCenter());
		assertEquals(new Point3F(10.0F, 20.0F, 30.0F), c.getCenter());
		
		assertEquals(2.0F, b.getRadius());
		assertEquals(2.0F, c.getRadius());
		
		assertThrows(NullPointerException.class, () -> a.transform(null));
	}
	
	@Test
	public void testWrite() {
		final BoundingSphere3F a = new BoundingSphere3F(2.0F, new Point3F(2.0F, 2.0F, 2.0F));
		
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		
		final DataOutput dataOutput = new DataOutputStream(byteArrayOutputStream);
		
		a.write(dataOutput);
		
		final byte[] bytes = byteArrayOutputStream.toByteArray();
		
		final BoundingSphere3F b = new BoundingSphere3FReader().read(new DataInputStream(new ByteArrayInputStream(bytes)));
		
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
		BoundingSphere3F boundingSphere3F = new BoundingSphere3F(2.0F, new Point3F(2.0F, 2.0F, 2.0F));
		boundingSphere3F.write(new File("./generated/unit-tests/BoundingSphere3F"));
		
		assertThrows(NullPointerException.class, () -> boundingSphere3F.write((File)(null)));
		assertThrows(UncheckedIOException.class, () -> boundingSphere3F.write(new File("./generated/unit-tests/non-existing-directory/BoundingSphere3F")));
	}
	
	@Test
	public void testWriteString() {
		final File directory = new File("./generated/unit-tests");
		
		if(!directory.isDirectory()) {
			directory.mkdirs();
		}
		
		final
		BoundingSphere3F boundingSphere3F = new BoundingSphere3F(2.0F, new Point3F(2.0F, 2.0F, 2.0F));
		boundingSphere3F.write("./generated/unit-tests/BoundingSphere3F");
		
		assertThrows(NullPointerException.class, () -> boundingSphere3F.write((String)(null)));
		assertThrows(UncheckedIOException.class, () -> boundingSphere3F.write("./generated/unit-tests/non-existing-directory/BoundingSphere3F"));
	}
}