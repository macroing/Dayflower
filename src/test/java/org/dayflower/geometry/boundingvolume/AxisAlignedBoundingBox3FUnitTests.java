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

import org.dayflower.geometry.BoundingVolume3F;
import org.dayflower.geometry.Matrix44F;
import org.dayflower.geometry.Point3F;
import org.dayflower.geometry.Ray3F;
import org.dayflower.geometry.Vector3F;
import org.dayflower.mock.DataOutputMock;
import org.dayflower.mock.NodeHierarchicalVisitorMock;
import org.dayflower.mock.NodeVisitorMock;
import org.dayflower.node.NodeHierarchicalVisitor;
import org.dayflower.node.NodeTraversalException;
import org.dayflower.node.NodeVisitor;
import org.junit.jupiter.api.Test;

@SuppressWarnings("static-method")
public final class AxisAlignedBoundingBox3FUnitTests {
	public AxisAlignedBoundingBox3FUnitTests() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Test
	public void testAcceptNodeHierarchicalVisitor() {
		final Point3F maximum = new Point3F(+1.0F, +1.0F, +1.0F);
		final Point3F minimum = new Point3F(-1.0F, -1.0F, -1.0F);
		
		final AxisAlignedBoundingBox3F axisAlignedBoundingBox = new AxisAlignedBoundingBox3F(maximum, minimum);
		
		assertTrue(axisAlignedBoundingBox.accept(new NodeHierarchicalVisitorMock(node -> false,                                                                               node -> node.equals(axisAlignedBoundingBox))));
		assertTrue(axisAlignedBoundingBox.accept(new NodeHierarchicalVisitorMock(node -> node.equals(axisAlignedBoundingBox),                                                 node -> node.equals(axisAlignedBoundingBox))));
		assertTrue(axisAlignedBoundingBox.accept(new NodeHierarchicalVisitorMock(node -> node.equals(axisAlignedBoundingBox) || node.equals(maximum),                         node -> node.equals(axisAlignedBoundingBox) || node.equals(maximum))));
		assertTrue(axisAlignedBoundingBox.accept(new NodeHierarchicalVisitorMock(node -> node.equals(axisAlignedBoundingBox) || node.equals(maximum) || node.equals(minimum), node -> node.equals(axisAlignedBoundingBox) || node.equals(maximum) || node.equals(minimum))));
		
		assertThrows(NodeTraversalException.class, () -> axisAlignedBoundingBox.accept(new NodeHierarchicalVisitorMock(null, null)));
		assertThrows(NullPointerException.class, () -> axisAlignedBoundingBox.accept((NodeHierarchicalVisitor)(null)));
	}
	
	@Test
	public void testAcceptNodeVisitor() {
		final AxisAlignedBoundingBox3F axisAlignedBoundingBox = new AxisAlignedBoundingBox3F();
		
		assertThrows(NodeTraversalException.class, () -> axisAlignedBoundingBox.accept(new NodeVisitorMock(true)));
		assertThrows(NullPointerException.class, () -> axisAlignedBoundingBox.accept((NodeVisitor)(null)));
	}
	
	@Test
	public void testConstants() {
		assertEquals(1, AxisAlignedBoundingBox3F.ID);
	}
	
	@Test
	public void testConstructor() {
		final AxisAlignedBoundingBox3F axisAlignedBoundingBox = new AxisAlignedBoundingBox3F();
		
		assertEquals(new Point3F(+0.5F, +0.5F, +0.5F), axisAlignedBoundingBox.getMaximum());
		assertEquals(new Point3F(-0.5F, -0.5F, -0.5F), axisAlignedBoundingBox.getMinimum());
	}
	
	@Test
	public void testConstructorPoint3FPoint3F() {
		final AxisAlignedBoundingBox3F axisAlignedBoundingBox = new AxisAlignedBoundingBox3F(new Point3F(1.0F, 2.0F, 3.0F), new Point3F(4.0F, 5.0F, 6.0F));
		
		assertEquals(new Point3F(4.0F, 5.0F, 6.0F), axisAlignedBoundingBox.getMaximum());
		assertEquals(new Point3F(1.0F, 2.0F, 3.0F), axisAlignedBoundingBox.getMinimum());
		
		assertThrows(NullPointerException.class, () -> new AxisAlignedBoundingBox3F(new Point3F(), null));
		assertThrows(NullPointerException.class, () -> new AxisAlignedBoundingBox3F(null, new Point3F()));
	}
	
	@Test
	public void testContains() {
		final AxisAlignedBoundingBox3F axisAlignedBoundingBox = new AxisAlignedBoundingBox3F(new Point3F(-5.0F, -5.0F, -5.0F), new Point3F(+5.0F, +5.0F, +5.0F));
		
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
		final AxisAlignedBoundingBox3F a = new AxisAlignedBoundingBox3F(new Point3F(-5.0F, -5.0F, -5.0F), new Point3F(+5.0F, +5.0F, +5.0F));
		final AxisAlignedBoundingBox3F b = new AxisAlignedBoundingBox3F(new Point3F(-5.0F, -5.0F, -5.0F), new Point3F(+5.0F, +5.0F, +5.0F));
		final AxisAlignedBoundingBox3F c = new AxisAlignedBoundingBox3F(new Point3F(-5.0F, -5.0F, -5.0F), new Point3F(+6.0F, +6.0F, +6.0F));
		final AxisAlignedBoundingBox3F d = new AxisAlignedBoundingBox3F(new Point3F(-6.0F, -6.0F, -6.0F), new Point3F(+5.0F, +5.0F, +5.0F));
		final AxisAlignedBoundingBox3F e = null;
		
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
		final AxisAlignedBoundingBox3F a = new AxisAlignedBoundingBox3F(new Point3F(), new Point3F());
		final AxisAlignedBoundingBox3F b = AxisAlignedBoundingBox3F.expand(a, +9.0F);
		final AxisAlignedBoundingBox3F c = AxisAlignedBoundingBox3F.expand(b, -9.0F);
		
		assertEquals(new Point3F(+0.0F, +0.0F, +0.0F), a.getMaximum());
		assertEquals(new Point3F(+0.0F, +0.0F, +0.0F), a.getMinimum());
		
		assertEquals(new Point3F(+9.0F, +9.0F, +9.0F), b.getMaximum());
		assertEquals(new Point3F(-9.0F, -9.0F, -9.0F), b.getMinimum());
		
		assertEquals(new Point3F(+0.0F, +0.0F, +0.0F), c.getMaximum());
		assertEquals(new Point3F(+0.0F, +0.0F, +0.0F), c.getMinimum());
		
		assertThrows(NullPointerException.class, () -> AxisAlignedBoundingBox3F.expand(null, 0.0F));
	}
	
	@Test
	public void testFromPoints() {
		final AxisAlignedBoundingBox3F axisAlignedBoundingBox = AxisAlignedBoundingBox3F.fromPoints(new Point3F(1.0F, 2.0F, 3.0F), new Point3F(4.0F, 5.0F, 6.0F), new Point3F(7.0F, 8.0F, 9.0F));
		
		assertEquals(new Point3F(7.0F, 8.0F, 9.0F), axisAlignedBoundingBox.getMaximum());
		assertEquals(new Point3F(1.0F, 2.0F, 3.0F), axisAlignedBoundingBox.getMinimum());
		
		assertThrows(IllegalArgumentException.class, () -> AxisAlignedBoundingBox3F.fromPoints(new Point3F[0]));
		assertThrows(NullPointerException.class, () -> AxisAlignedBoundingBox3F.fromPoints((Point3F[])(null)));
		assertThrows(NullPointerException.class, () -> AxisAlignedBoundingBox3F.fromPoints(new Point3F(), null));
	}
	
	@Test
	public void testGetClosestPointTo() {
		final AxisAlignedBoundingBox3F axisAlignedBoundingBox = new AxisAlignedBoundingBox3F(new Point3F(-5.0F, -5.0F, -5.0F), new Point3F(+5.0F, +5.0F, +5.0F));
		
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
		
		final Point3F o = axisAlignedBoundingBox.getClosestPointTo(a);
		final Point3F p = axisAlignedBoundingBox.getClosestPointTo(b);
		final Point3F q = axisAlignedBoundingBox.getClosestPointTo(c);
		final Point3F r = axisAlignedBoundingBox.getClosestPointTo(d);
		final Point3F s = axisAlignedBoundingBox.getClosestPointTo(e);
		final Point3F t = axisAlignedBoundingBox.getClosestPointTo(f);
		final Point3F u = axisAlignedBoundingBox.getClosestPointTo(g);
		
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
		final AxisAlignedBoundingBox3F axisAlignedBoundingBox = new AxisAlignedBoundingBox3F();
		
		assertEquals(AxisAlignedBoundingBox3F.ID, axisAlignedBoundingBox.getID());
	}
	
	@Test
	public void testGetMaximum() {
		final AxisAlignedBoundingBox3F axisAlignedBoundingBox = new AxisAlignedBoundingBox3F(new Point3F(-1.0F, -1.0F, -1.0F), new Point3F(+1.0F, +1.0F, +1.0F));
		
		assertEquals(new Point3F(+1.0F, +1.0F, +1.0F), axisAlignedBoundingBox.getMaximum());
	}
	
	@Test
	public void testGetMidpoint() {
		final AxisAlignedBoundingBox3F axisAlignedBoundingBox = new AxisAlignedBoundingBox3F(new Point3F(-1.0F, -1.0F, -1.0F), new Point3F(+1.0F, +1.0F, +1.0F));
		
		assertEquals(new Point3F(0.0F, 0.0F, 0.0F), axisAlignedBoundingBox.getMidpoint());
	}
	
	@Test
	public void testGetMinimum() {
		final AxisAlignedBoundingBox3F axisAlignedBoundingBox = new AxisAlignedBoundingBox3F(new Point3F(-1.0F, -1.0F, -1.0F), new Point3F(+1.0F, +1.0F, +1.0F));
		
		assertEquals(new Point3F(-1.0F, -1.0F, -1.0F), axisAlignedBoundingBox.getMinimum());
	}
	
	@Test
	public void testGetSurfaceArea() {
		final AxisAlignedBoundingBox3F axisAlignedBoundingBox = new AxisAlignedBoundingBox3F(new Point3F(-1.0F, -2.0F, -3.0F), new Point3F(+1.0F, +2.0F, +3.0F));
		
		assertEquals(88.0F, axisAlignedBoundingBox.getSurfaceArea());
	}
	
	@Test
	public void testGetVolume() {
		final AxisAlignedBoundingBox3F axisAlignedBoundingBox = new AxisAlignedBoundingBox3F(new Point3F(-1.0F, -2.0F, -3.0F), new Point3F(+1.0F, +2.0F, +3.0F));
		
		assertEquals(48.0F, axisAlignedBoundingBox.getVolume());
	}
	
	@Test
	public void testHashCode() {
		final AxisAlignedBoundingBox3F a = new AxisAlignedBoundingBox3F();
		final AxisAlignedBoundingBox3F b = new AxisAlignedBoundingBox3F();
		
		assertEquals(a.hashCode(), a.hashCode());
		assertEquals(a.hashCode(), b.hashCode());
	}
	
	@Test
	public void testIntersection() {
		final AxisAlignedBoundingBox3F axisAlignedBoundingBox = new AxisAlignedBoundingBox3F(new Point3F(-5.0F, -5.0F, +5.0F), new Point3F(+5.0F, +5.0F, +10.0F));
		
		final Ray3F rayA = new Ray3F(new Point3F(0.0F, 0.0F, 0.0F), Vector3F.z());
		final Ray3F rayB = new Ray3F(new Point3F(0.0F, 0.0F, 0.0F), Vector3F.x());
		
		final float tMinimumA =  0.0F;
		final float tMinimumB =  5.0F;
		final float tMinimumC = 10.0F;
		
		final float tMaximumA = Float.MAX_VALUE;
		final float tMaximumB = 5.0F;
		
		final float expectedTA =  5.0F;
		final float expectedTB = 10.0F;
		final float expectedTC = Float.NaN;
		
		assertEquals(expectedTA, axisAlignedBoundingBox.intersection(rayA, tMinimumA, tMaximumA));
		assertEquals(expectedTB, axisAlignedBoundingBox.intersection(rayA, tMinimumB, tMaximumA));
		assertEquals(expectedTC, axisAlignedBoundingBox.intersection(rayA, tMinimumC, tMaximumA));
		assertEquals(expectedTC, axisAlignedBoundingBox.intersection(rayA, tMinimumA, tMaximumB));
		
		assertEquals(expectedTC, axisAlignedBoundingBox.intersection(rayB, tMinimumA, tMaximumA));
		
		assertThrows(NullPointerException.class, () -> axisAlignedBoundingBox.intersection(null, 0.0F, Float.MAX_VALUE));
	}
	
	@Test
	public void testToString() {
		final AxisAlignedBoundingBox3F axisAlignedBoundingBox = new AxisAlignedBoundingBox3F(new Point3F(-1.0F, -1.0F, -1.0F), new Point3F(+1.0F, +1.0F, +1.0F));
		
		assertEquals("new AxisAlignedBoundingBox3F(new Point3F(1.0F, 1.0F, 1.0F), new Point3F(-1.0F, -1.0F, -1.0F))", axisAlignedBoundingBox.toString());
	}
	
	@Test
	public void testTransform() {
		final AxisAlignedBoundingBox3F a = new AxisAlignedBoundingBox3F(new Point3F(10.0F, 20.0F, 30.0F), new Point3F(20.0F, 30.0F, 40.0F));
		final AxisAlignedBoundingBox3F b = a.transform(Matrix44F.translate(+10.0F, +20.0F, +30.0F));
		final AxisAlignedBoundingBox3F c = b.transform(Matrix44F.translate(-10.0F, -20.0F, -30.0F));
		
		assertEquals(new Point3F(30.0F, 50.0F, 70.0F), b.getMaximum());
		assertEquals(new Point3F(20.0F, 40.0F, 60.0F), b.getMinimum());
		assertEquals(new Point3F(20.0F, 30.0F, 40.0F), c.getMaximum());
		assertEquals(new Point3F(10.0F, 20.0F, 30.0F), c.getMinimum());
		
		assertThrows(NullPointerException.class, () -> a.transform(null));
	}
	
	@Test
	public void testUnionBoundingVolume3FBoundingVolume3F() {
		final AxisAlignedBoundingBox3F a = new AxisAlignedBoundingBox3F(new Point3F(-5.0F, -5.0F, -5.0F), new Point3F());
		final AxisAlignedBoundingBox3F b = new AxisAlignedBoundingBox3F(new Point3F(+5.0F, +5.0F, +5.0F), new Point3F());
		final AxisAlignedBoundingBox3F c = AxisAlignedBoundingBox3F.union(a, b);
		
		assertEquals(new Point3F(+5.0F, +5.0F, +5.0F), c.getMaximum());
		assertEquals(new Point3F(-5.0F, -5.0F, -5.0F), c.getMinimum());
		
		assertThrows(NullPointerException.class, () -> AxisAlignedBoundingBox3F.union(a, (BoundingVolume3F)(null)));
		assertThrows(NullPointerException.class, () -> AxisAlignedBoundingBox3F.union(null, b));
	}
	
	@Test
	public void testUnionBoundingVolume3FPoint3F() {
		final AxisAlignedBoundingBox3F a = new AxisAlignedBoundingBox3F(new Point3F(-5.0F, -5.0F, -5.0F), new Point3F());
		final AxisAlignedBoundingBox3F b = AxisAlignedBoundingBox3F.union(a, new Point3F(+5.0F, +5.0F, +5.0F));
		
		assertEquals(new Point3F(+5.0F, +5.0F, +5.0F), b.getMaximum());
		assertEquals(new Point3F(-5.0F, -5.0F, -5.0F), b.getMinimum());
		
		assertThrows(NullPointerException.class, () -> AxisAlignedBoundingBox3F.union(a, (Point3F)(null)));
		assertThrows(NullPointerException.class, () -> AxisAlignedBoundingBox3F.union(null, new Point3F()));
	}
	
	@Test
	public void testWrite() {
		final AxisAlignedBoundingBox3F a = new AxisAlignedBoundingBox3F(new Point3F(-1.0F, -1.0F, -1.0F), new Point3F(+1.0F, +1.0F, +1.0F));
		
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		
		final DataOutput dataOutput = new DataOutputStream(byteArrayOutputStream);
		
		a.write(dataOutput);
		
		final byte[] bytes = byteArrayOutputStream.toByteArray();
		
		final AxisAlignedBoundingBox3F b = new AxisAlignedBoundingBox3FReader().read(new DataInputStream(new ByteArrayInputStream(bytes)));
		
		assertEquals(a, b);
		
		assertThrows(NullPointerException.class, () -> a.write((DataOutput)(null)));
		assertThrows(UncheckedIOException.class, () -> a.write(new DataOutputMock()));
	}
}