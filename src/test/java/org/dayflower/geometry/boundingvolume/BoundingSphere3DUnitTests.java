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
import java.io.UncheckedIOException;

import org.dayflower.geometry.Matrix44D;
import org.dayflower.geometry.Point3D;
import org.dayflower.geometry.Ray3D;
import org.dayflower.geometry.Vector3D;
import org.dayflower.mock.DataOutputMock;
import org.dayflower.mock.NodeHierarchicalVisitorMock;
import org.dayflower.mock.NodeVisitorMock;
import org.dayflower.node.NodeHierarchicalVisitor;
import org.dayflower.node.NodeTraversalException;
import org.dayflower.node.NodeVisitor;
import org.junit.jupiter.api.Test;

@SuppressWarnings("static-method")
public final class BoundingSphere3DUnitTests {
	public BoundingSphere3DUnitTests() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Test
	public void testAcceptNodeHierarchicalVisitor() {
		final Point3D center = new Point3D(0.0D, 1.0D, 2.0D);
		
		final BoundingSphere3D boundingSphere = new BoundingSphere3D(1.0D, center);
		
		assertTrue(boundingSphere.accept(new NodeHierarchicalVisitorMock(node -> false,                                              node -> node.equals(boundingSphere))));
		assertTrue(boundingSphere.accept(new NodeHierarchicalVisitorMock(node -> node.equals(boundingSphere),                        node -> node.equals(boundingSphere))));
		assertTrue(boundingSphere.accept(new NodeHierarchicalVisitorMock(node -> node.equals(boundingSphere) || node.equals(center), node -> node.equals(boundingSphere) || node.equals(center))));
		
		assertThrows(NodeTraversalException.class, () -> boundingSphere.accept(new NodeHierarchicalVisitorMock(null, null)));
		assertThrows(NullPointerException.class, () -> boundingSphere.accept((NodeHierarchicalVisitor)(null)));
	}
	
	@Test
	public void testAcceptNodeVisitor() {
		final BoundingSphere3D boundingSphere = new BoundingSphere3D();
		
		assertThrows(NodeTraversalException.class, () -> boundingSphere.accept(new NodeVisitorMock(true)));
		assertThrows(NullPointerException.class, () -> boundingSphere.accept((NodeVisitor)(null)));
	}
	
	@Test
	public void testConstants() {
		assertEquals(2, BoundingSphere3D.ID);
	}
	
	@Test
	public void testConstructor() {
		final BoundingSphere3D boundingSphere = new BoundingSphere3D();
		
		assertEquals(1.0D, boundingSphere.getRadius());
		assertEquals(new Point3D(0.0D, 0.0D, 0.0D), boundingSphere.getCenter());
	}
	
	@Test
	public void testConstructorDouble() {
		final BoundingSphere3D boundingSphere = new BoundingSphere3D(2.0D);
		
		assertEquals(2.0D, boundingSphere.getRadius());
		assertEquals(new Point3D(0.0D, 0.0D, 0.0D), boundingSphere.getCenter());
	}
	
	@Test
	public void testConstructorDoublePoint3D() {
		final BoundingSphere3D boundingSphere = new BoundingSphere3D(2.0D, new Point3D(1.0D, 1.0D, 1.0D));
		
		assertEquals(2.0D, boundingSphere.getRadius());
		assertEquals(new Point3D(1.0D, 1.0D, 1.0D), boundingSphere.getCenter());
		
		assertThrows(NullPointerException.class, () -> new BoundingSphere3D(1.0D, null));
	}
	
	@Test
	public void testContains() {
		final BoundingSphere3D boundingSphere = new BoundingSphere3D(5.0D, new Point3D(0.0D, 0.0D, 0.0D));
		
		final Point3D a = new Point3D(+9.0D, +0.0D, +0.0D);
		final Point3D b = new Point3D(-9.0D, +0.0D, +0.0D);
		final Point3D c = new Point3D(+0.0D, +9.0D, +0.0D);
		final Point3D d = new Point3D(+0.0D, -9.0D, +0.0D);
		final Point3D e = new Point3D(+0.0D, +0.0D, +9.0D);
		final Point3D f = new Point3D(+0.0D, +0.0D, -9.0D);
		
		final Point3D g = new Point3D(+5.0D, +0.0D, +0.0D);
		final Point3D h = new Point3D(-5.0D, +0.0D, +0.0D);
		final Point3D i = new Point3D(+0.0D, +5.0D, +0.0D);
		final Point3D j = new Point3D(+0.0D, -5.0D, +0.0D);
		final Point3D k = new Point3D(+0.0D, +0.0D, +5.0D);
		final Point3D l = new Point3D(+0.0D, +0.0D, -5.0D);
		final Point3D m = new Point3D(+0.0D, +0.0D, +0.0D);
		
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
		final BoundingSphere3D a = new BoundingSphere3D(1.0D, new Point3D(0.0D, 0.0D, 0.0D));
		final BoundingSphere3D b = new BoundingSphere3D(1.0D, new Point3D(0.0D, 0.0D, 0.0D));
		final BoundingSphere3D c = new BoundingSphere3D(1.0D, new Point3D(1.0D, 1.0D, 1.0D));
		final BoundingSphere3D d = new BoundingSphere3D(2.0D, new Point3D(0.0D, 0.0D, 0.0D));
		final BoundingSphere3D e = null;
		
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
		final BoundingSphere3D boundingSphere = new BoundingSphere3D(1.0D, new Point3D(10.0D, 20.0D, 30.0D));
		
		assertEquals(new Point3D(10.0D, 20.0D, 30.0D), boundingSphere.getCenter());
	}
	
	@Test
	public void testGetClosestPointTo() {
		final BoundingSphere3D boundingSphere = new BoundingSphere3D(5.0D, new Point3D(0.0D, 0.0D, 0.0D));
		
		final Point3D a = new Point3D(+9.0D, +0.0D, +0.0D);
		final Point3D b = new Point3D(-9.0D, +0.0D, +0.0D);
		final Point3D c = new Point3D(+0.0D, +9.0D, +0.0D);
		final Point3D d = new Point3D(+0.0D, -9.0D, +0.0D);
		final Point3D e = new Point3D(+0.0D, +0.0D, +9.0D);
		final Point3D f = new Point3D(+0.0D, +0.0D, -9.0D);
		final Point3D g = new Point3D(+0.0D, +0.0D, +0.0D);
		
		final Point3D h = new Point3D(+5.0D, +0.0D, +0.0D);
		final Point3D i = new Point3D(-5.0D, +0.0D, +0.0D);
		final Point3D j = new Point3D(+0.0D, +5.0D, +0.0D);
		final Point3D k = new Point3D(+0.0D, -5.0D, +0.0D);
		final Point3D l = new Point3D(+0.0D, +0.0D, +5.0D);
		final Point3D m = new Point3D(+0.0D, +0.0D, -5.0D);
		final Point3D n = new Point3D(+0.0D, +0.0D, +0.0D);
		
		final Point3D o = boundingSphere.getClosestPointTo(a);
		final Point3D p = boundingSphere.getClosestPointTo(b);
		final Point3D q = boundingSphere.getClosestPointTo(c);
		final Point3D r = boundingSphere.getClosestPointTo(d);
		final Point3D s = boundingSphere.getClosestPointTo(e);
		final Point3D t = boundingSphere.getClosestPointTo(f);
		final Point3D u = boundingSphere.getClosestPointTo(g);
		
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
		final BoundingSphere3D boundingSphere = new BoundingSphere3D();
		
		assertEquals(BoundingSphere3D.ID, boundingSphere.getID());
	}
	
	@Test
	public void testGetMaximum() {
		final BoundingSphere3D boundingSphere = new BoundingSphere3D(5.0D, new Point3D(0.0D, 0.0D, 0.0D));
		
		assertEquals(new Point3D(+5.0D, +5.0D, +5.0D), boundingSphere.getMaximum());
	}
	
	@Test
	public void testGetMidpoint() {
		final BoundingSphere3D boundingSphere = new BoundingSphere3D(5.0D, new Point3D(0.0D, 0.0D, 0.0D));
		
		assertEquals(new Point3D(0.0D, 0.0D, 0.0D), boundingSphere.getMidpoint());
	}
	
	@Test
	public void testGetMinimum() {
		final BoundingSphere3D boundingSphere = new BoundingSphere3D(5.0D, new Point3D(0.0D, 0.0D, 0.0D));
		
		assertEquals(new Point3D(-5.0D, -5.0D, -5.0D), boundingSphere.getMinimum());
	}
	
	@Test
	public void testGetRadius() {
		final BoundingSphere3D boundingSphere = new BoundingSphere3D(2.0D, new Point3D(0.0D, 0.0D, 0.0D));
		
		assertEquals(2.0D, boundingSphere.getRadius());
	}
	
	@Test
	public void testGetRadiusSquared() {
		final BoundingSphere3D boundingSphere = new BoundingSphere3D(2.0D, new Point3D(0.0D, 0.0D, 0.0D));
		
		assertEquals(4.0D, boundingSphere.getRadiusSquared());
	}
	
	@Test
	public void testGetSurfaceArea() {
		final BoundingSphere3D a = new BoundingSphere3D(1.0D);
		final BoundingSphere3D b = new BoundingSphere3D(2.0D);
		
		final double expectedA = Math.PI * 4.0D * 1.0D;
		final double expectedB = Math.PI * 4.0D * 4.0D;
		
		assertEquals(expectedA, a.getSurfaceArea());
		assertEquals(expectedB, b.getSurfaceArea());
	}
	
	@Test
	public void testGetVolume() {
		final BoundingSphere3D a = new BoundingSphere3D(1.0D);
		final BoundingSphere3D b = new BoundingSphere3D(2.0D);
		
		final double expectedA = 4.0D / 3.0D * Math.PI * 1.0D;
		final double expectedB = 4.0D / 3.0D * Math.PI * 8.0D;
		
		assertEquals(expectedA, a.getVolume());
		assertEquals(expectedB, b.getVolume());
	}
	
	@Test
	public void testHashCode() {
		final BoundingSphere3D a = new BoundingSphere3D();
		final BoundingSphere3D b = new BoundingSphere3D();
		
		assertEquals(a.hashCode(), a.hashCode());
		assertEquals(a.hashCode(), b.hashCode());
	}
	
	@Test
	public void testIntersection() {
		final BoundingSphere3D boundingSphere = new BoundingSphere3D(5.0D, new Point3D(0.0D, 0.0D, 10.0D));
		
		final Ray3D rayA = new Ray3D(new Point3D(0.0D, 0.0D, 0.0D), Vector3D.z());
		final Ray3D rayB = new Ray3D(new Point3D(0.0D, 0.0D, 0.0D), Vector3D.x());
		
		final double tMinimumA =  0.0D;
		final double tMinimumB =  5.0D;
		final double tMinimumC = 15.0D;
		
		final double tMaximumA = Double.MAX_VALUE;
		final double tMaximumB = 5.0D;
		
		final double expectedTA =  5.0D;
		final double expectedTB = 15.0D;
		final double expectedTC = Double.NaN;
		
		assertEquals(expectedTA, boundingSphere.intersection(rayA, tMinimumA, tMaximumA));
		assertEquals(expectedTB, boundingSphere.intersection(rayA, tMinimumB, tMaximumA));
		assertEquals(expectedTC, boundingSphere.intersection(rayA, tMinimumC, tMaximumA));
		assertEquals(expectedTC, boundingSphere.intersection(rayA, tMinimumA, tMaximumB));
		
		assertEquals(expectedTC, boundingSphere.intersection(rayB, tMinimumA, tMaximumA));
		
		assertThrows(NullPointerException.class, () -> boundingSphere.intersection(null, 0.0D, Double.MAX_VALUE));
	}
	
	@Test
	public void testIntersectsBoundingVolume3D() {
		final BoundingSphere3D boundingSphere = new BoundingSphere3D();
		
		assertTrue(boundingSphere.intersects(boundingSphere));
		
		assertThrows(NullPointerException.class, () -> boundingSphere.intersects(null));
	}
	
	@Test
	public void testIntersectsRay3DDoubleDouble() {
		final BoundingSphere3D boundingSphere = new BoundingSphere3D(5.0D, new Point3D(0.0D, 0.0D, 10.0D));
		
		assertTrue(boundingSphere.intersects(new Ray3D(new Point3D(0.0D, 0.0D, 0.0D), Vector3D.z()), 0.0D, Double.MAX_VALUE));
		
		assertFalse(boundingSphere.intersects(new Ray3D(new Point3D(0.0D, 0.0D, 0.0D), Vector3D.x()), 0.0D, Double.MAX_VALUE));
		
		assertThrows(NullPointerException.class, () -> boundingSphere.intersects(null, 0.0D, Double.MAX_VALUE));
	}
	
	@Test
	public void testToString() {
		final BoundingSphere3D boundingSphere = new BoundingSphere3D(1.0D, new Point3D(0.0D, 0.0D, 0.0D));
		
		assertEquals("new BoundingSphere3D(1.0D, new Point3D(0.0D, 0.0D, 0.0D))", boundingSphere.toString());
	}
	
	@Test
	public void testTransform() {
		final BoundingSphere3D a = new BoundingSphere3D(2.0D, new Point3D(10.0D, 20.0D, 30.0D));
		final BoundingSphere3D b = a.transform(Matrix44D.translate(+10.0D, +20.0D, +30.0D));
		final BoundingSphere3D c = b.transform(Matrix44D.translate(-10.0D, -20.0D, -30.0D));
		
		assertEquals(new Point3D(20.0D, 40.0D, 60.0D), b.getCenter());
		assertEquals(new Point3D(10.0D, 20.0D, 30.0D), c.getCenter());
		
		assertEquals(2.0D, b.getRadius());
		assertEquals(2.0D, c.getRadius());
		
		assertThrows(NullPointerException.class, () -> a.transform(null));
	}
	
	@Test
	public void testWrite() {
		final BoundingSphere3D a = new BoundingSphere3D(2.0D, new Point3D(2.0D, 2.0D, 2.0D));
		
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		
		final DataOutput dataOutput = new DataOutputStream(byteArrayOutputStream);
		
		a.write(dataOutput);
		
		final byte[] bytes = byteArrayOutputStream.toByteArray();
		
		final BoundingSphere3D b = new BoundingSphere3DReader().read(new DataInputStream(new ByteArrayInputStream(bytes)));
		
		assertEquals(a, b);
		
		assertThrows(NullPointerException.class, () -> a.write((DataOutput)(null)));
		assertThrows(UncheckedIOException.class, () -> a.write(new DataOutputMock()));
	}
}