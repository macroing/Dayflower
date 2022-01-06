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

import org.dayflower.geometry.BoundingVolume3D;
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
public final class AxisAlignedBoundingBox3DUnitTests {
	public AxisAlignedBoundingBox3DUnitTests() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Test
	public void testAcceptNodeHierarchicalVisitor() {
		final Point3D maximum = new Point3D(+1.0D, +1.0D, +1.0D);
		final Point3D minimum = new Point3D(-1.0D, -1.0D, -1.0D);
		
		final AxisAlignedBoundingBox3D axisAlignedBoundingBox = new AxisAlignedBoundingBox3D(maximum, minimum);
		
		assertTrue(axisAlignedBoundingBox.accept(new NodeHierarchicalVisitorMock(node -> false,                                                                               node -> node.equals(axisAlignedBoundingBox))));
		assertTrue(axisAlignedBoundingBox.accept(new NodeHierarchicalVisitorMock(node -> node.equals(axisAlignedBoundingBox),                                                 node -> node.equals(axisAlignedBoundingBox))));
		assertTrue(axisAlignedBoundingBox.accept(new NodeHierarchicalVisitorMock(node -> node.equals(axisAlignedBoundingBox) || node.equals(maximum),                         node -> node.equals(axisAlignedBoundingBox) || node.equals(maximum))));
		assertTrue(axisAlignedBoundingBox.accept(new NodeHierarchicalVisitorMock(node -> node.equals(axisAlignedBoundingBox) || node.equals(maximum) || node.equals(minimum), node -> node.equals(axisAlignedBoundingBox) || node.equals(maximum) || node.equals(minimum))));
		
		assertThrows(NodeTraversalException.class, () -> axisAlignedBoundingBox.accept(new NodeHierarchicalVisitorMock(null, null)));
		assertThrows(NullPointerException.class, () -> axisAlignedBoundingBox.accept((NodeHierarchicalVisitor)(null)));
	}
	
	@Test
	public void testAcceptNodeVisitor() {
		final AxisAlignedBoundingBox3D axisAlignedBoundingBox = new AxisAlignedBoundingBox3D();
		
		assertThrows(NodeTraversalException.class, () -> axisAlignedBoundingBox.accept(new NodeVisitorMock(true)));
		assertThrows(NullPointerException.class, () -> axisAlignedBoundingBox.accept((NodeVisitor)(null)));
	}
	
	@Test
	public void testConstants() {
		assertEquals(1, AxisAlignedBoundingBox3D.ID);
	}
	
	@Test
	public void testConstructor() {
		final AxisAlignedBoundingBox3D axisAlignedBoundingBox = new AxisAlignedBoundingBox3D();
		
		assertEquals(new Point3D(+0.5D, +0.5D, +0.5D), axisAlignedBoundingBox.getMaximum());
		assertEquals(new Point3D(-0.5D, -0.5D, -0.5D), axisAlignedBoundingBox.getMinimum());
	}
	
	@Test
	public void testConstructorPoint3DPoint3D() {
		final AxisAlignedBoundingBox3D axisAlignedBoundingBox = new AxisAlignedBoundingBox3D(new Point3D(1.0D, 2.0D, 3.0D), new Point3D(4.0D, 5.0D, 6.0D));
		
		assertEquals(new Point3D(4.0D, 5.0D, 6.0D), axisAlignedBoundingBox.getMaximum());
		assertEquals(new Point3D(1.0D, 2.0D, 3.0D), axisAlignedBoundingBox.getMinimum());
		
		assertThrows(NullPointerException.class, () -> new AxisAlignedBoundingBox3D(new Point3D(), null));
		assertThrows(NullPointerException.class, () -> new AxisAlignedBoundingBox3D(null, new Point3D()));
	}
	
	@Test
	public void testContains() {
		final AxisAlignedBoundingBox3D axisAlignedBoundingBox = new AxisAlignedBoundingBox3D(new Point3D(-5.0D, -5.0D, -5.0D), new Point3D(+5.0D, +5.0D, +5.0D));
		
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
		
		assertFalse(axisAlignedBoundingBox.contains(a));
		assertFalse(axisAlignedBoundingBox.contains(b));
		assertFalse(axisAlignedBoundingBox.contains(c));
		assertFalse(axisAlignedBoundingBox.contains(d));
		assertFalse(axisAlignedBoundingBox.contains(e));
		assertFalse(axisAlignedBoundingBox.contains(f));
		
		assertTrue(axisAlignedBoundingBox.contains(g));
		assertTrue(axisAlignedBoundingBox.contains(h));
		assertTrue(axisAlignedBoundingBox.contains(i));
		assertTrue(axisAlignedBoundingBox.contains(j));
		assertTrue(axisAlignedBoundingBox.contains(k));
		assertTrue(axisAlignedBoundingBox.contains(l));
		assertTrue(axisAlignedBoundingBox.contains(m));
		
		assertThrows(NullPointerException.class, () -> axisAlignedBoundingBox.contains(null));
	}
	
	@Test
	public void testEquals() {
		final AxisAlignedBoundingBox3D a = new AxisAlignedBoundingBox3D(new Point3D(-5.0D, -5.0D, -5.0D), new Point3D(+5.0D, +5.0D, +5.0D));
		final AxisAlignedBoundingBox3D b = new AxisAlignedBoundingBox3D(new Point3D(-5.0D, -5.0D, -5.0D), new Point3D(+5.0D, +5.0D, +5.0D));
		final AxisAlignedBoundingBox3D c = new AxisAlignedBoundingBox3D(new Point3D(-5.0D, -5.0D, -5.0D), new Point3D(+6.0D, +6.0D, +6.0D));
		final AxisAlignedBoundingBox3D d = new AxisAlignedBoundingBox3D(new Point3D(-6.0D, -6.0D, -6.0D), new Point3D(+5.0D, +5.0D, +5.0D));
		final AxisAlignedBoundingBox3D e = null;
		
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
	public void testExpand() {
		final AxisAlignedBoundingBox3D a = new AxisAlignedBoundingBox3D(new Point3D(), new Point3D());
		final AxisAlignedBoundingBox3D b = AxisAlignedBoundingBox3D.expand(a, +9.0D);
		final AxisAlignedBoundingBox3D c = AxisAlignedBoundingBox3D.expand(b, -9.0D);
		
		assertEquals(new Point3D(+0.0D, +0.0D, +0.0D), a.getMaximum());
		assertEquals(new Point3D(+0.0D, +0.0D, +0.0D), a.getMinimum());
		
		assertEquals(new Point3D(+9.0D, +9.0D, +9.0D), b.getMaximum());
		assertEquals(new Point3D(-9.0D, -9.0D, -9.0D), b.getMinimum());
		
		assertEquals(new Point3D(+0.0D, +0.0D, +0.0D), c.getMaximum());
		assertEquals(new Point3D(+0.0D, +0.0D, +0.0D), c.getMinimum());
		
		assertThrows(NullPointerException.class, () -> AxisAlignedBoundingBox3D.expand(null, 0.0D));
	}
	
	@Test
	public void testFromPoints() {
		final AxisAlignedBoundingBox3D axisAlignedBoundingBox = AxisAlignedBoundingBox3D.fromPoints(new Point3D(1.0D, 2.0D, 3.0D), new Point3D(4.0D, 5.0D, 6.0D), new Point3D(7.0D, 8.0D, 9.0D));
		
		assertEquals(new Point3D(7.0D, 8.0D, 9.0D), axisAlignedBoundingBox.getMaximum());
		assertEquals(new Point3D(1.0D, 2.0D, 3.0D), axisAlignedBoundingBox.getMinimum());
		
		assertThrows(IllegalArgumentException.class, () -> AxisAlignedBoundingBox3D.fromPoints(new Point3D[0]));
		assertThrows(NullPointerException.class, () -> AxisAlignedBoundingBox3D.fromPoints((Point3D[])(null)));
		assertThrows(NullPointerException.class, () -> AxisAlignedBoundingBox3D.fromPoints(new Point3D(), null));
	}
	
	@Test
	public void testGetClosestPointTo() {
		final AxisAlignedBoundingBox3D axisAlignedBoundingBox = new AxisAlignedBoundingBox3D(new Point3D(-5.0D, -5.0D, -5.0D), new Point3D(+5.0D, +5.0D, +5.0D));
		
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
		
		final Point3D o = axisAlignedBoundingBox.getClosestPointTo(a);
		final Point3D p = axisAlignedBoundingBox.getClosestPointTo(b);
		final Point3D q = axisAlignedBoundingBox.getClosestPointTo(c);
		final Point3D r = axisAlignedBoundingBox.getClosestPointTo(d);
		final Point3D s = axisAlignedBoundingBox.getClosestPointTo(e);
		final Point3D t = axisAlignedBoundingBox.getClosestPointTo(f);
		final Point3D u = axisAlignedBoundingBox.getClosestPointTo(g);
		
		assertEquals(h, o);
		assertEquals(i, p);
		assertEquals(j, q);
		assertEquals(k, r);
		assertEquals(l, s);
		assertEquals(m, t);
		assertEquals(n, u);
		
		assertThrows(NullPointerException.class, () -> axisAlignedBoundingBox.getClosestPointTo(null));
	}
	
	@Test
	public void testGetID() {
		final AxisAlignedBoundingBox3D axisAlignedBoundingBox = new AxisAlignedBoundingBox3D();
		
		assertEquals(AxisAlignedBoundingBox3D.ID, axisAlignedBoundingBox.getID());
	}
	
	@Test
	public void testGetMaximum() {
		final AxisAlignedBoundingBox3D axisAlignedBoundingBox = new AxisAlignedBoundingBox3D(new Point3D(-1.0D, -1.0D, -1.0D), new Point3D(+1.0D, +1.0D, +1.0D));
		
		assertEquals(new Point3D(+1.0D, +1.0D, +1.0D), axisAlignedBoundingBox.getMaximum());
	}
	
	@Test
	public void testGetMidpoint() {
		final AxisAlignedBoundingBox3D axisAlignedBoundingBox = new AxisAlignedBoundingBox3D(new Point3D(-1.0D, -1.0D, -1.0D), new Point3D(+1.0D, +1.0D, +1.0D));
		
		assertEquals(new Point3D(0.0D, 0.0D, 0.0D), axisAlignedBoundingBox.getMidpoint());
	}
	
	@Test
	public void testGetMinimum() {
		final AxisAlignedBoundingBox3D axisAlignedBoundingBox = new AxisAlignedBoundingBox3D(new Point3D(-1.0D, -1.0D, -1.0D), new Point3D(+1.0D, +1.0D, +1.0D));
		
		assertEquals(new Point3D(-1.0D, -1.0D, -1.0D), axisAlignedBoundingBox.getMinimum());
	}
	
	@Test
	public void testGetSurfaceArea() {
		final AxisAlignedBoundingBox3D axisAlignedBoundingBox = new AxisAlignedBoundingBox3D(new Point3D(-1.0D, -2.0D, -3.0D), new Point3D(+1.0D, +2.0D, +3.0D));
		
		assertEquals(88.0D, axisAlignedBoundingBox.getSurfaceArea());
	}
	
	@Test
	public void testGetVolume() {
		final AxisAlignedBoundingBox3D axisAlignedBoundingBox = new AxisAlignedBoundingBox3D(new Point3D(-1.0D, -2.0D, -3.0D), new Point3D(+1.0D, +2.0D, +3.0D));
		
		assertEquals(48.0D, axisAlignedBoundingBox.getVolume());
	}
	
	@Test
	public void testHashCode() {
		final AxisAlignedBoundingBox3D a = new AxisAlignedBoundingBox3D();
		final AxisAlignedBoundingBox3D b = new AxisAlignedBoundingBox3D();
		
		assertEquals(a.hashCode(), a.hashCode());
		assertEquals(a.hashCode(), b.hashCode());
	}
	
	@Test
	public void testIntersection() {
		final AxisAlignedBoundingBox3D axisAlignedBoundingBox = new AxisAlignedBoundingBox3D(new Point3D(-5.0D, -5.0D, +5.0D), new Point3D(+5.0D, +5.0D, +10.0D));
		
		final Ray3D rayA = new Ray3D(new Point3D(0.0D, 0.0D, 0.0D), Vector3D.z());
		final Ray3D rayB = new Ray3D(new Point3D(0.0D, 0.0D, 0.0D), Vector3D.x());
		
		final double tMinimumA =  0.0D;
		final double tMinimumB =  5.0D;
		final double tMinimumC = 10.0D;
		
		final double tMaximumA = Double.MAX_VALUE;
		final double tMaximumB = 5.0D;
		
		final double expectedTA =  5.0D;
		final double expectedTB = 10.0D;
		final double expectedTC = Double.NaN;
		
		assertEquals(expectedTA, axisAlignedBoundingBox.intersection(rayA, tMinimumA, tMaximumA));
		assertEquals(expectedTB, axisAlignedBoundingBox.intersection(rayA, tMinimumB, tMaximumA));
		assertEquals(expectedTC, axisAlignedBoundingBox.intersection(rayA, tMinimumC, tMaximumA));
		assertEquals(expectedTC, axisAlignedBoundingBox.intersection(rayA, tMinimumA, tMaximumB));
		
		assertEquals(expectedTC, axisAlignedBoundingBox.intersection(rayB, tMinimumA, tMaximumA));
		
		assertThrows(NullPointerException.class, () -> axisAlignedBoundingBox.intersection(null, 0.0D, Double.MAX_VALUE));
	}
	
	@Test
	public void testToString() {
		final AxisAlignedBoundingBox3D axisAlignedBoundingBox = new AxisAlignedBoundingBox3D(new Point3D(-1.0D, -1.0D, -1.0D), new Point3D(+1.0D, +1.0D, +1.0D));
		
		assertEquals("new AxisAlignedBoundingBox3D(new Point3D(1.0D, 1.0D, 1.0D), new Point3D(-1.0D, -1.0D, -1.0D))", axisAlignedBoundingBox.toString());
	}
	
	@Test
	public void testTransform() {
		final AxisAlignedBoundingBox3D a = new AxisAlignedBoundingBox3D(new Point3D(10.0D, 20.0D, 30.0D), new Point3D(20.0D, 30.0D, 40.0D));
		final AxisAlignedBoundingBox3D b = a.transform(Matrix44D.translate(+10.0D, +20.0D, +30.0D));
		final AxisAlignedBoundingBox3D c = b.transform(Matrix44D.translate(-10.0D, -20.0D, -30.0D));
		
		assertEquals(new Point3D(30.0D, 50.0D, 70.0D), b.getMaximum());
		assertEquals(new Point3D(20.0D, 40.0D, 60.0D), b.getMinimum());
		assertEquals(new Point3D(20.0D, 30.0D, 40.0D), c.getMaximum());
		assertEquals(new Point3D(10.0D, 20.0D, 30.0D), c.getMinimum());
		
		assertThrows(NullPointerException.class, () -> a.transform(null));
	}
	
	@Test
	public void testUnionBoundingVolume3DBoundingVolume3D() {
		final AxisAlignedBoundingBox3D a = new AxisAlignedBoundingBox3D(new Point3D(-5.0D, -5.0D, -5.0D), new Point3D());
		final AxisAlignedBoundingBox3D b = new AxisAlignedBoundingBox3D(new Point3D(+5.0D, +5.0D, +5.0D), new Point3D());
		final AxisAlignedBoundingBox3D c = AxisAlignedBoundingBox3D.union(a, b);
		
		assertEquals(new Point3D(+5.0D, +5.0D, +5.0D), c.getMaximum());
		assertEquals(new Point3D(-5.0D, -5.0D, -5.0D), c.getMinimum());
		
		assertThrows(NullPointerException.class, () -> AxisAlignedBoundingBox3D.union(a, (BoundingVolume3D)(null)));
		assertThrows(NullPointerException.class, () -> AxisAlignedBoundingBox3D.union(null, b));
	}
	
	@Test
	public void testUnionBoundingVolume3DPoint3D() {
		final AxisAlignedBoundingBox3D a = new AxisAlignedBoundingBox3D(new Point3D(-5.0D, -5.0D, -5.0D), new Point3D());
		final AxisAlignedBoundingBox3D b = AxisAlignedBoundingBox3D.union(a, new Point3D(+5.0D, +5.0D, +5.0D));
		
		assertEquals(new Point3D(+5.0D, +5.0D, +5.0D), b.getMaximum());
		assertEquals(new Point3D(-5.0D, -5.0D, -5.0D), b.getMinimum());
		
		assertThrows(NullPointerException.class, () -> AxisAlignedBoundingBox3D.union(a, (Point3D)(null)));
		assertThrows(NullPointerException.class, () -> AxisAlignedBoundingBox3D.union(null, new Point3D()));
	}
	
	@Test
	public void testWrite() {
		final AxisAlignedBoundingBox3D a = new AxisAlignedBoundingBox3D(new Point3D(-1.0D, -1.0D, -1.0D), new Point3D(+1.0D, +1.0D, +1.0D));
		
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		
		final DataOutput dataOutput = new DataOutputStream(byteArrayOutputStream);
		
		a.write(dataOutput);
		
		final byte[] bytes = byteArrayOutputStream.toByteArray();
		
		final AxisAlignedBoundingBox3D b = new AxisAlignedBoundingBox3DReader().read(new DataInputStream(new ByteArrayInputStream(bytes)));
		
		assertEquals(a, b);
		
		assertThrows(NullPointerException.class, () -> a.write((DataOutput)(null)));
		assertThrows(UncheckedIOException.class, () -> a.write(new DataOutputMock()));
	}
}