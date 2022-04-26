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
import java.util.List;

import org.dayflower.geometry.Point2I;
import org.dayflower.mock.DataOutputMock;
import org.dayflower.mock.NodeHierarchicalVisitorMock;
import org.dayflower.mock.NodeVisitorMock;
import org.dayflower.node.NodeHierarchicalVisitor;
import org.dayflower.node.NodeTraversalException;
import org.dayflower.node.NodeVisitor;
import org.junit.jupiter.api.Test;

@SuppressWarnings("static-method")
public final class Triangle2IUnitTests {
	public Triangle2IUnitTests() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Test
	public void testAcceptNodeHierarchicalVisitor() {
		final Point2I a = new Point2I(10, 10);
		final Point2I b = new Point2I(20, 10);
		final Point2I c = new Point2I(20, 20);
		
		final Triangle2I triangle = new Triangle2I(a, b, c);
		
		final List<LineSegment2I> lineSegments = triangle.getLineSegments();
		
		final LineSegment2I lineSegment0 = lineSegments.get(0);
		final LineSegment2I lineSegment1 = lineSegments.get(1);
		final LineSegment2I lineSegment2 = lineSegments.get(2);
		
		final Rectangle2I rectangle = triangle.getRectangle();
		
		assertTrue(triangle.accept(new NodeHierarchicalVisitorMock(node -> false,                                                                                                                                                                                        node -> node.equals(triangle))));
		assertTrue(triangle.accept(new NodeHierarchicalVisitorMock(node -> node.equals(triangle),                                                                                                                                                                        node -> node.equals(triangle))));
		assertTrue(triangle.accept(new NodeHierarchicalVisitorMock(node -> node.equals(triangle) || node.equals(lineSegment0),                                                                                                                                           node -> node.equals(triangle) || node.equals(lineSegment0))));
		assertTrue(triangle.accept(new NodeHierarchicalVisitorMock(node -> node.equals(triangle) || node.equals(lineSegment0) || node.equals(lineSegment1),                                                                                                              node -> node.equals(triangle) || node.equals(lineSegment0) || node.equals(lineSegment1))));
		assertTrue(triangle.accept(new NodeHierarchicalVisitorMock(node -> node.equals(triangle) || node.equals(lineSegment0) || node.equals(lineSegment1) || node.equals(lineSegment2),                                                                                 node -> node.equals(triangle) || node.equals(lineSegment0) || node.equals(lineSegment1) || node.equals(lineSegment2))));
		assertTrue(triangle.accept(new NodeHierarchicalVisitorMock(node -> node.equals(triangle) || node.equals(lineSegment0) || node.equals(lineSegment1) || node.equals(lineSegment2) || node.equals(a),                                                               node -> node.equals(triangle) || node.equals(lineSegment0) || node.equals(lineSegment1) || node.equals(lineSegment2) || node.equals(a))));
		assertTrue(triangle.accept(new NodeHierarchicalVisitorMock(node -> node.equals(triangle) || node.equals(lineSegment0) || node.equals(lineSegment1) || node.equals(lineSegment2) || node.equals(a) || node.equals(b),                                             node -> node.equals(triangle) || node.equals(lineSegment0) || node.equals(lineSegment1) || node.equals(lineSegment2) || node.equals(a) || node.equals(b))));
		assertTrue(triangle.accept(new NodeHierarchicalVisitorMock(node -> node.equals(triangle) || node.equals(lineSegment0) || node.equals(lineSegment1) || node.equals(lineSegment2) || node.equals(a) || node.equals(b) || node.equals(c),                           node -> node.equals(triangle) || node.equals(lineSegment0) || node.equals(lineSegment1) || node.equals(lineSegment2) || node.equals(a) || node.equals(b) || node.equals(c))));
		assertTrue(triangle.accept(new NodeHierarchicalVisitorMock(node -> node.equals(triangle) || node.equals(lineSegment0) || node.equals(lineSegment1) || node.equals(lineSegment2) || node.equals(a) || node.equals(b) || node.equals(c) || node.equals(rectangle), node -> node.equals(triangle) || node.equals(lineSegment0) || node.equals(lineSegment1) || node.equals(lineSegment2) || node.equals(a) || node.equals(b) || node.equals(c) || node.equals(rectangle))));
		
		assertThrows(NodeTraversalException.class, () -> triangle.accept(new NodeHierarchicalVisitorMock(null, null)));
		assertThrows(NullPointerException.class, () -> triangle.accept((NodeHierarchicalVisitor)(null)));
	}
	
	@Test
	public void testAcceptNodeVisitor() {
		final Triangle2I triangle = new Triangle2I(new Point2I(10, 10), new Point2I(20, 10), new Point2I(20, 20));
		
		assertThrows(NodeTraversalException.class, () -> triangle.accept(new NodeVisitorMock(true)));
		assertThrows(NullPointerException.class, () -> triangle.accept((NodeVisitor)(null)));
	}
	
	@Test
	public void testConstants() {
		assertEquals(5, Triangle2I.ID);
		assertEquals("Triangle", Triangle2I.NAME);
	}
	
	@Test
	public void testConstructorPoint2IPoint2IPoint2I() {
		final Triangle2I triangle = new Triangle2I(new Point2I(10, 10), new Point2I(20, 10), new Point2I(20, 20));
		
		final List<LineSegment2I> lineSegments = triangle.getLineSegments();
		
		final Rectangle2I rectangle = triangle.getRectangle();
		
		assertEquals(new Point2I(10, 10), triangle.getA());
		assertEquals(new Point2I(20, 10), triangle.getB());
		assertEquals(new Point2I(20, 20), triangle.getC());
		
		assertNotNull(lineSegments);
		
		assertTrue(lineSegments.size() == 3);
		
		assertNotNull(lineSegments.get(0));
		assertNotNull(lineSegments.get(1));
		assertNotNull(lineSegments.get(2));
		
		assertEquals(new Point2I(10, 10), lineSegments.get(0).getA());
		assertEquals(new Point2I(20, 10), lineSegments.get(0).getB());
		
		assertEquals(new Point2I(20, 10), lineSegments.get(1).getA());
		assertEquals(new Point2I(20, 20), lineSegments.get(1).getB());
		
		assertEquals(new Point2I(20, 20), lineSegments.get(2).getA());
		assertEquals(new Point2I(10, 10), lineSegments.get(2).getB());
		
		assertNotNull(rectangle);
		
		assertEquals(new Point2I(10, 10), rectangle.getA());
		assertEquals(new Point2I(10, 20), rectangle.getB());
		assertEquals(new Point2I(20, 20), rectangle.getC());
		assertEquals(new Point2I(20, 10), rectangle.getD());
		
		assertThrows(NullPointerException.class, () -> new Triangle2I(new Point2I(), new Point2I(), null));
		assertThrows(NullPointerException.class, () -> new Triangle2I(new Point2I(), null, new Point2I()));
		assertThrows(NullPointerException.class, () -> new Triangle2I(null, new Point2I(), new Point2I()));
	}
	
	@Test
	public void testContainsPoint2IBoolean() {
		final Triangle2I triangle = new Triangle2I(new Point2I(20, 20), new Point2I(30, 20), new Point2I(30, 30));
		
		assertTrue(triangle.contains(new Point2I(20, 20), false));
		assertTrue(triangle.contains(new Point2I(25, 20), false));
		assertTrue(triangle.contains(new Point2I(30, 20), false));
		assertTrue(triangle.contains(new Point2I(30, 25), false));
		assertTrue(triangle.contains(new Point2I(30, 30), false));
		assertTrue(triangle.contains(new Point2I(25, 25), false));
		assertTrue(triangle.contains(new Point2I(27, 23), false));
		
		assertTrue(triangle.contains(new Point2I(20, 20), true));
		assertTrue(triangle.contains(new Point2I(25, 20), true));
		assertTrue(triangle.contains(new Point2I(30, 20), true));
		assertTrue(triangle.contains(new Point2I(30, 25), true));
		assertTrue(triangle.contains(new Point2I(30, 30), true));
		assertTrue(triangle.contains(new Point2I(25, 25), true));
		
		assertFalse(triangle.contains(new Point2I(19, 20), false));
		assertFalse(triangle.contains(new Point2I(31, 20), false));
		assertFalse(triangle.contains(new Point2I(29, 30), false));
		assertFalse(triangle.contains(new Point2I(31, 30), false));
		assertFalse(triangle.contains(new Point2I(20, 19), false));
		assertFalse(triangle.contains(new Point2I(20, 21), false));
		assertFalse(triangle.contains(new Point2I(30, 19), false));
		assertFalse(triangle.contains(new Point2I(30, 31), false));
		
		assertFalse(triangle.contains(new Point2I(19, 20), true));
		assertFalse(triangle.contains(new Point2I(31, 20), true));
		assertFalse(triangle.contains(new Point2I(29, 30), true));
		assertFalse(triangle.contains(new Point2I(31, 30), true));
		assertFalse(triangle.contains(new Point2I(20, 19), true));
		assertFalse(triangle.contains(new Point2I(20, 21), true));
		assertFalse(triangle.contains(new Point2I(30, 19), true));
		assertFalse(triangle.contains(new Point2I(30, 31), true));
		
		assertFalse(triangle.contains(new Point2I(27, 23), true));
		
		assertThrows(NullPointerException.class, () -> triangle.contains(null, false));
	}
	
	@Test
	public void testEquals() {
		final Triangle2I a = new Triangle2I(new Point2I(20, 20), new Point2I(30, 20), new Point2I(30, 30));
		final Triangle2I b = new Triangle2I(new Point2I(20, 20), new Point2I(30, 20), new Point2I(30, 30));
		final Triangle2I c = new Triangle2I(new Point2I(20, 20), new Point2I(30, 20), new Point2I(30, 40));
		final Triangle2I d = new Triangle2I(new Point2I(20, 20), new Point2I(30, 10), new Point2I(30, 30));
		final Triangle2I e = new Triangle2I(new Point2I(10, 20), new Point2I(30, 20), new Point2I(30, 30));
		final Triangle2I f = null;
		
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
	public void testFindPointsBoolean() {
		final Triangle2I triangle = new Triangle2I(new Point2I(0, 0), new Point2I(3, 0), new Point2I(3, 3));
		
		final List<Point2I> pointsA = triangle.findPoints(false);
		final List<Point2I> pointsB = triangle.findPoints(true);
		
		assertNotNull(pointsA);
		assertNotNull(pointsB);
		
		assertEquals(10, pointsA.size());
		assertEquals( 9, pointsB.size());
		
		assertEquals(new Point2I(0, 0), pointsA.get(0));
		assertEquals(new Point2I(1, 0), pointsA.get(1));
		assertEquals(new Point2I(2, 0), pointsA.get(2));
		assertEquals(new Point2I(3, 0), pointsA.get(3));
		assertEquals(new Point2I(1, 1), pointsA.get(4));
		assertEquals(new Point2I(2, 1), pointsA.get(5));
		assertEquals(new Point2I(3, 1), pointsA.get(6));
		assertEquals(new Point2I(2, 2), pointsA.get(7));
		assertEquals(new Point2I(3, 2), pointsA.get(8));
		assertEquals(new Point2I(3, 3), pointsA.get(9));
		
		assertEquals(new Point2I(0, 0), pointsB.get(0));
		assertEquals(new Point2I(1, 0), pointsB.get(1));
		assertEquals(new Point2I(2, 0), pointsB.get(2));
		assertEquals(new Point2I(3, 0), pointsB.get(3));
		assertEquals(new Point2I(1, 1), pointsB.get(4));
		assertEquals(new Point2I(3, 1), pointsB.get(5));
		assertEquals(new Point2I(2, 2), pointsB.get(6));
		assertEquals(new Point2I(3, 2), pointsB.get(7));
		assertEquals(new Point2I(3, 3), pointsB.get(8));
	}
	
	@Test
	public void testGetA() {
		final Triangle2I triangle = new Triangle2I(new Point2I(10, 10), new Point2I(20, 10), new Point2I(20, 20));
		
		assertEquals(new Point2I(10, 10), triangle.getA());
	}
	
	@Test
	public void testGetB() {
		final Triangle2I triangle = new Triangle2I(new Point2I(10, 10), new Point2I(20, 10), new Point2I(20, 20));
		
		assertEquals(new Point2I(20, 10), triangle.getB());
	}
	
	@Test
	public void testGetC() {
		final Triangle2I triangle = new Triangle2I(new Point2I(10, 10), new Point2I(20, 10), new Point2I(20, 20));
		
		assertEquals(new Point2I(20, 20), triangle.getC());
	}
	
	@Test
	public void testGetID() {
		final Triangle2I triangle = new Triangle2I(new Point2I(10, 10), new Point2I(20, 10), new Point2I(20, 20));
		
		assertEquals(Triangle2I.ID, triangle.getID());
	}
	
	@Test
	public void testGetLineSegments() {
		final Triangle2I triangle = new Triangle2I(new Point2I(10, 10), new Point2I(20, 10), new Point2I(20, 20));
		
		final List<LineSegment2I> lineSegments = triangle.getLineSegments();
		
		assertNotNull(lineSegments);
		
		assertTrue(lineSegments.size() == 3);
		
		assertNotNull(lineSegments.get(0));
		assertNotNull(lineSegments.get(1));
		assertNotNull(lineSegments.get(2));
		
		assertEquals(new Point2I(10, 10), lineSegments.get(0).getA());
		assertEquals(new Point2I(20, 10), lineSegments.get(0).getB());
		
		assertEquals(new Point2I(20, 10), lineSegments.get(1).getA());
		assertEquals(new Point2I(20, 20), lineSegments.get(1).getB());
		
		assertEquals(new Point2I(20, 20), lineSegments.get(2).getA());
		assertEquals(new Point2I(10, 10), lineSegments.get(2).getB());
	}
	
	@Test
	public void testGetMaximum() {
		final Triangle2I triangle = new Triangle2I(new Point2I(10, 10), new Point2I(20, 10), new Point2I(20, 20));
		
		assertEquals(new Point2I(20, 20), triangle.getMaximum());
	}
	
	@Test
	public void testGetMinimum() {
		final Triangle2I triangle = new Triangle2I(new Point2I(10, 10), new Point2I(20, 10), new Point2I(20, 20));
		
		assertEquals(new Point2I(10, 10), triangle.getMinimum());
	}
	
	@Test
	public void testGetName() {
		final Triangle2I triangle = new Triangle2I(new Point2I(10, 10), new Point2I(20, 10), new Point2I(20, 20));
		
		assertEquals(Triangle2I.NAME, triangle.getName());
	}
	
	@Test
	public void testGetRectangle() {
		final Triangle2I triangle = new Triangle2I(new Point2I(10, 10), new Point2I(20, 10), new Point2I(20, 20));
		
		final Rectangle2I rectangle = triangle.getRectangle();
		
		assertNotNull(rectangle);
		
		assertEquals(new Point2I(10, 10), rectangle.getA());
		assertEquals(new Point2I(10, 20), rectangle.getB());
		assertEquals(new Point2I(20, 20), rectangle.getC());
		assertEquals(new Point2I(20, 10), rectangle.getD());
	}
	
	@Test
	public void testHashCode() {
		final Triangle2I a = new Triangle2I(new Point2I(10, 10), new Point2I(20, 10), new Point2I(20, 20));
		final Triangle2I b = new Triangle2I(new Point2I(10, 10), new Point2I(20, 10), new Point2I(20, 20));
		
		assertEquals(a.hashCode(), a.hashCode());
		assertEquals(a.hashCode(), b.hashCode());
	}
	
	@Test
	public void testToString() {
		final Triangle2I triangle = new Triangle2I(new Point2I(10, 10), new Point2I(20, 10), new Point2I(20, 20));
		
		assertEquals("new Triangle2I(new Point2I(10, 10), new Point2I(20, 10), new Point2I(20, 20))", triangle.toString());
	}
	
	@Test
	public void testWrite() {
		final Triangle2I a = new Triangle2I(new Point2I(10, 10), new Point2I(20, 10), new Point2I(20, 20));
		
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		
		final DataOutput dataOutput = new DataOutputStream(byteArrayOutputStream);
		
		a.write(dataOutput);
		
		final byte[] bytes = byteArrayOutputStream.toByteArray();
		
		final Triangle2I b = new Triangle2IReader().read(new DataInputStream(new ByteArrayInputStream(bytes)));
		
		assertEquals(a, b);
		
		assertThrows(NullPointerException.class, () -> a.write((DataOutput)(null)));
		assertThrows(UncheckedIOException.class, () -> a.write(new DataOutputMock()));
	}
}