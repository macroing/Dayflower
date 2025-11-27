/**
 * Copyright 2014 - 2025 J&#246;rgen Lundgren
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

import org.junit.jupiter.api.Test;

import org.macroing.java.util.visitor.NodeHierarchicalVisitor;
import org.macroing.java.util.visitor.NodeTraversalException;
import org.macroing.java.util.visitor.NodeVisitor;

@SuppressWarnings("static-method")
public final class Polygon2IUnitTests {
	public Polygon2IUnitTests() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Test
	public void testAcceptNodeHierarchicalVisitor() {
		final Point2I a = new Point2I(10, 10);
		final Point2I b = new Point2I(20, 10);
		final Point2I c = new Point2I(20, 20);
		final Point2I d = new Point2I(10, 20);
		
		final Polygon2I polygon = new Polygon2I(a, b, c, d);
		
		final List<LineSegment2I> lineSegments = polygon.getLineSegments();
		
		final LineSegment2I lineSegment0 = lineSegments.get(0);
		final LineSegment2I lineSegment1 = lineSegments.get(1);
		final LineSegment2I lineSegment2 = lineSegments.get(2);
		final LineSegment2I lineSegment3 = lineSegments.get(3);
		
		final Rectangle2I rectangle = polygon.getRectangle();
		
		assertTrue(polygon.accept(new NodeHierarchicalVisitorMock(node -> false,                                                                                                                                                                                                                                      node -> node.equals(polygon))));
		assertTrue(polygon.accept(new NodeHierarchicalVisitorMock(node -> node.equals(polygon),                                                                                                                                                                                                                       node -> node.equals(polygon))));
		assertTrue(polygon.accept(new NodeHierarchicalVisitorMock(node -> node.equals(polygon) || node.equals(lineSegment0),                                                                                                                                                                                          node -> node.equals(polygon) || node.equals(lineSegment0))));
		assertTrue(polygon.accept(new NodeHierarchicalVisitorMock(node -> node.equals(polygon) || node.equals(lineSegment0) || node.equals(lineSegment1),                                                                                                                                                             node -> node.equals(polygon) || node.equals(lineSegment0) || node.equals(lineSegment1))));
		assertTrue(polygon.accept(new NodeHierarchicalVisitorMock(node -> node.equals(polygon) || node.equals(lineSegment0) || node.equals(lineSegment1) || node.equals(lineSegment2),                                                                                                                                node -> node.equals(polygon) || node.equals(lineSegment0) || node.equals(lineSegment1) || node.equals(lineSegment2))));
		assertTrue(polygon.accept(new NodeHierarchicalVisitorMock(node -> node.equals(polygon) || node.equals(lineSegment0) || node.equals(lineSegment1) || node.equals(lineSegment2) || node.equals(lineSegment3),                                                                                                   node -> node.equals(polygon) || node.equals(lineSegment0) || node.equals(lineSegment1) || node.equals(lineSegment2) || node.equals(lineSegment3))));
		assertTrue(polygon.accept(new NodeHierarchicalVisitorMock(node -> node.equals(polygon) || node.equals(lineSegment0) || node.equals(lineSegment1) || node.equals(lineSegment2) || node.equals(lineSegment3) || node.equals(a),                                                                                 node -> node.equals(polygon) || node.equals(lineSegment0) || node.equals(lineSegment1) || node.equals(lineSegment2) || node.equals(lineSegment3) || node.equals(a))));
		assertTrue(polygon.accept(new NodeHierarchicalVisitorMock(node -> node.equals(polygon) || node.equals(lineSegment0) || node.equals(lineSegment1) || node.equals(lineSegment2) || node.equals(lineSegment3) || node.equals(a) || node.equals(b),                                                               node -> node.equals(polygon) || node.equals(lineSegment0) || node.equals(lineSegment1) || node.equals(lineSegment2) || node.equals(lineSegment3) || node.equals(a) || node.equals(b))));
		assertTrue(polygon.accept(new NodeHierarchicalVisitorMock(node -> node.equals(polygon) || node.equals(lineSegment0) || node.equals(lineSegment1) || node.equals(lineSegment2) || node.equals(lineSegment3) || node.equals(a) || node.equals(b) || node.equals(c),                                             node -> node.equals(polygon) || node.equals(lineSegment0) || node.equals(lineSegment1) || node.equals(lineSegment2) || node.equals(lineSegment3) || node.equals(a) || node.equals(b) || node.equals(c))));
		assertTrue(polygon.accept(new NodeHierarchicalVisitorMock(node -> node.equals(polygon) || node.equals(lineSegment0) || node.equals(lineSegment1) || node.equals(lineSegment2) || node.equals(lineSegment3) || node.equals(a) || node.equals(b) || node.equals(c) || node.equals(d),                           node -> node.equals(polygon) || node.equals(lineSegment0) || node.equals(lineSegment1) || node.equals(lineSegment2) || node.equals(lineSegment3) || node.equals(a) || node.equals(b) || node.equals(c) || node.equals(d))));
		assertTrue(polygon.accept(new NodeHierarchicalVisitorMock(node -> node.equals(polygon) || node.equals(lineSegment0) || node.equals(lineSegment1) || node.equals(lineSegment2) || node.equals(lineSegment3) || node.equals(a) || node.equals(b) || node.equals(c) || node.equals(d) || node.equals(rectangle), node -> node.equals(polygon) || node.equals(lineSegment0) || node.equals(lineSegment1) || node.equals(lineSegment2) || node.equals(lineSegment3) || node.equals(a) || node.equals(b) || node.equals(c) || node.equals(d) || node.equals(rectangle))));
		
		assertThrows(NodeTraversalException.class, () -> polygon.accept(new NodeHierarchicalVisitorMock(null, null)));
		assertThrows(NullPointerException.class, () -> polygon.accept((NodeHierarchicalVisitor)(null)));
	}
	
	@Test
	public void testAcceptNodeVisitor() {
		final Polygon2I polygon = new Polygon2I(new Point2I(10, 10), new Point2I(20, 10), new Point2I(20, 20), new Point2I(10, 20));
		
		assertThrows(NodeTraversalException.class, () -> polygon.accept(new NodeVisitorMock(true)));
		assertThrows(NullPointerException.class, () -> polygon.accept((NodeVisitor)(null)));
	}
	
	@Test
	public void testConstants() {
		assertEquals(3, Polygon2I.ID);
		assertEquals("Polygon", Polygon2I.NAME);
	}
	
	@Test
	public void testConstructorPoint2Is() {
		final Polygon2I polygon = new Polygon2I(new Point2I(10, 10), new Point2I(10, 30), new Point2I(30, 30), new Point2I(30, 10));
		
		final List<LineSegment2I> lineSegments = polygon.getLineSegments();
		
		final List<Point2I> points = polygon.getPoints();
		
		final Rectangle2I rectangle = polygon.getRectangle();
		
		assertEquals(new Point2I(10, 10), points.get(0));
		assertEquals(new Point2I(10, 30), points.get(1));
		assertEquals(new Point2I(30, 30), points.get(2));
		assertEquals(new Point2I(30, 10), points.get(3));
		
		assertNotNull(lineSegments);
		
		assertTrue(lineSegments.size() == 4);
		
		assertNotNull(lineSegments.get(0));
		assertNotNull(lineSegments.get(1));
		assertNotNull(lineSegments.get(2));
		assertNotNull(lineSegments.get(3));
		
		assertEquals(new Point2I(10, 10), lineSegments.get(0).getA());
		assertEquals(new Point2I(10, 30), lineSegments.get(0).getB());
		
		assertEquals(new Point2I(10, 30), lineSegments.get(1).getA());
		assertEquals(new Point2I(30, 30), lineSegments.get(1).getB());
		
		assertEquals(new Point2I(30, 30), lineSegments.get(2).getA());
		assertEquals(new Point2I(30, 10), lineSegments.get(2).getB());
		
		assertEquals(new Point2I(30, 10), lineSegments.get(3).getA());
		assertEquals(new Point2I(10, 10), lineSegments.get(3).getB());
		
		assertEquals(new Point2I(10, 10), rectangle.getA());
		assertEquals(new Point2I(30, 10), rectangle.getB());
		assertEquals(new Point2I(30, 30), rectangle.getC());
		assertEquals(new Point2I(10, 30), rectangle.getD());
		
		assertThrows(NullPointerException.class, () -> new Polygon2I(new Point2I(), new Point2I(), new Point2I(), null));
		assertThrows(NullPointerException.class, () -> new Polygon2I(new Point2I(), new Point2I(), null, new Point2I()));
		assertThrows(NullPointerException.class, () -> new Polygon2I(new Point2I(), null, new Point2I(), new Point2I()));
		assertThrows(NullPointerException.class, () -> new Polygon2I(null, new Point2I(), new Point2I(), new Point2I()));
		assertThrows(NullPointerException.class, () -> new Polygon2I((Point2I[])(null)));
		
		assertThrows(IllegalArgumentException.class, () -> new Polygon2I(new Point2I(10, 10), new Point2I(10, 20)));
	}
	
	@Test
	public void testContainsPoint2I() {
		final Polygon2I polygon = new Polygon2I(new Point2I(20, 20), new Point2I(30, 20), new Point2I(30, 30), new Point2I(20, 30));
		
		assertTrue(polygon.contains(new Point2I(20, 20)));
		assertTrue(polygon.contains(new Point2I(25, 20)));
		assertTrue(polygon.contains(new Point2I(30, 20)));
		assertTrue(polygon.contains(new Point2I(30, 25)));
		assertTrue(polygon.contains(new Point2I(30, 30)));
		assertTrue(polygon.contains(new Point2I(25, 30)));
		assertTrue(polygon.contains(new Point2I(20, 30)));
		assertTrue(polygon.contains(new Point2I(20, 25)));
		assertTrue(polygon.contains(new Point2I(25, 25)));
		
		assertFalse(polygon.contains(new Point2I(19, 20)));
		assertFalse(polygon.contains(new Point2I(31, 20)));
		assertFalse(polygon.contains(new Point2I(19, 30)));
		assertFalse(polygon.contains(new Point2I(31, 30)));
		assertFalse(polygon.contains(new Point2I(20, 19)));
		assertFalse(polygon.contains(new Point2I(20, 31)));
		assertFalse(polygon.contains(new Point2I(30, 19)));
		assertFalse(polygon.contains(new Point2I(30, 31)));
		
		assertThrows(NullPointerException.class, () -> polygon.contains(null));
	}
	
	@Test
	public void testContainsPoint2IBoolean() {
		final Polygon2I polygon = new Polygon2I(new Point2I(20, 20), new Point2I(30, 20), new Point2I(30, 30), new Point2I(20, 30));
		
		assertTrue(polygon.contains(new Point2I(20, 20), false));
		assertTrue(polygon.contains(new Point2I(25, 20), false));
		assertTrue(polygon.contains(new Point2I(30, 20), false));
		assertTrue(polygon.contains(new Point2I(30, 25), false));
		assertTrue(polygon.contains(new Point2I(30, 30), false));
		assertTrue(polygon.contains(new Point2I(25, 30), false));
		assertTrue(polygon.contains(new Point2I(20, 30), false));
		assertTrue(polygon.contains(new Point2I(20, 25), false));
		assertTrue(polygon.contains(new Point2I(25, 25), false));
		
		assertTrue(polygon.contains(new Point2I(20, 20), true));
		assertTrue(polygon.contains(new Point2I(25, 20), true));
		assertTrue(polygon.contains(new Point2I(30, 20), true));
		assertTrue(polygon.contains(new Point2I(30, 25), true));
		assertTrue(polygon.contains(new Point2I(30, 30), true));
		assertTrue(polygon.contains(new Point2I(25, 30), true));
		assertTrue(polygon.contains(new Point2I(20, 30), true));
		assertTrue(polygon.contains(new Point2I(20, 25), true));
		
		assertFalse(polygon.contains(new Point2I(19, 20), false));
		assertFalse(polygon.contains(new Point2I(31, 20), false));
		assertFalse(polygon.contains(new Point2I(19, 30), false));
		assertFalse(polygon.contains(new Point2I(31, 30), false));
		assertFalse(polygon.contains(new Point2I(20, 19), false));
		assertFalse(polygon.contains(new Point2I(20, 31), false));
		assertFalse(polygon.contains(new Point2I(30, 19), false));
		assertFalse(polygon.contains(new Point2I(30, 31), false));
		
		assertFalse(polygon.contains(new Point2I(19, 20), true));
		assertFalse(polygon.contains(new Point2I(31, 20), true));
		assertFalse(polygon.contains(new Point2I(19, 30), true));
		assertFalse(polygon.contains(new Point2I(31, 30), true));
		assertFalse(polygon.contains(new Point2I(20, 19), true));
		assertFalse(polygon.contains(new Point2I(20, 31), true));
		assertFalse(polygon.contains(new Point2I(30, 19), true));
		assertFalse(polygon.contains(new Point2I(30, 31), true));
		
		assertFalse(polygon.contains(new Point2I(25, 25), true));
		
		assertThrows(NullPointerException.class, () -> polygon.contains(null, false));
	}
	
	@Test
	public void testEquals() {
		final Polygon2I a = new Polygon2I(new Point2I(20, 20), new Point2I(30, 20), new Point2I(30, 30), new Point2I(20, 30));
		final Polygon2I b = new Polygon2I(new Point2I(20, 20), new Point2I(30, 20), new Point2I(30, 30), new Point2I(20, 30));
		final Polygon2I c = new Polygon2I(new Point2I(20, 20), new Point2I(40, 20), new Point2I(40, 30), new Point2I(20, 30));
		final Polygon2I d = new Polygon2I(new Point2I(10, 20), new Point2I(30, 20), new Point2I(30, 30), new Point2I(10, 30));
		final Polygon2I e = new Polygon2I(new Point2I(20, 20), new Point2I(30, 20), new Point2I(30, 40), new Point2I(20, 40));
		final Polygon2I f = null;
		
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
		final Polygon2I polygon = new Polygon2I(new Point2I(1, 0), new Point2I(3, 0), new Point2I(4, 1), new Point2I(3, 2), new Point2I(1, 2), new Point2I(0, 1));
		
		final List<Point2I> pointsA = polygon.findPoints(false);
		final List<Point2I> pointsB = polygon.findPoints(true);
		
		assertNotNull(pointsA);
		assertNotNull(pointsB);
		
		assertEquals(11, pointsA.size());
		assertEquals( 8, pointsB.size());
		
		assertEquals(new Point2I(1, 0), pointsA.get( 0));
		assertEquals(new Point2I(2, 0), pointsA.get( 1));
		assertEquals(new Point2I(3, 0), pointsA.get( 2));
		assertEquals(new Point2I(0, 1), pointsA.get( 3));
		assertEquals(new Point2I(1, 1), pointsA.get( 4));
		assertEquals(new Point2I(2, 1), pointsA.get( 5));
		assertEquals(new Point2I(3, 1), pointsA.get( 6));
		assertEquals(new Point2I(4, 1), pointsA.get( 7));
		assertEquals(new Point2I(1, 2), pointsA.get( 8));
		assertEquals(new Point2I(2, 2), pointsA.get( 9));
		assertEquals(new Point2I(3, 2), pointsA.get(10));
		
		assertEquals(new Point2I(1, 0), pointsB.get(0));
		assertEquals(new Point2I(2, 0), pointsB.get(1));
		assertEquals(new Point2I(3, 0), pointsB.get(2));
		assertEquals(new Point2I(0, 1), pointsB.get(3));
		assertEquals(new Point2I(4, 1), pointsB.get(4));
		assertEquals(new Point2I(1, 2), pointsB.get(5));
		assertEquals(new Point2I(2, 2), pointsB.get(6));
		assertEquals(new Point2I(3, 2), pointsB.get(7));
	}
	
	@Test
	public void testGetID() {
		final Polygon2I polygon = new Polygon2I(new Point2I(10, 10), new Point2I(20, 10), new Point2I(20, 20), new Point2I(10, 20));
		
		assertEquals(Polygon2I.ID, polygon.getID());
	}
	
	@Test
	public void testGetLineSegments() {
		final Polygon2I polygon = new Polygon2I(new Point2I(10, 10), new Point2I(10, 30), new Point2I(30, 30), new Point2I(30, 10));
		
		final List<LineSegment2I> lineSegments = polygon.getLineSegments();
		
		assertNotNull(lineSegments);
		
		assertTrue(lineSegments.size() == 4);
		
		assertNotNull(lineSegments.get(0));
		assertNotNull(lineSegments.get(1));
		assertNotNull(lineSegments.get(2));
		assertNotNull(lineSegments.get(3));
		
		assertEquals(new Point2I(10, 10), lineSegments.get(0).getA());
		assertEquals(new Point2I(10, 30), lineSegments.get(0).getB());
		
		assertEquals(new Point2I(10, 30), lineSegments.get(1).getA());
		assertEquals(new Point2I(30, 30), lineSegments.get(1).getB());
		
		assertEquals(new Point2I(30, 30), lineSegments.get(2).getA());
		assertEquals(new Point2I(30, 10), lineSegments.get(2).getB());
		
		assertEquals(new Point2I(30, 10), lineSegments.get(3).getA());
		assertEquals(new Point2I(10, 10), lineSegments.get(3).getB());
	}
	
	@Test
	public void testGetMaximum() {
		final Polygon2I polygon = new Polygon2I(new Point2I(10, 10), new Point2I(20, 10), new Point2I(20, 20), new Point2I(10, 20));
		
		assertEquals(new Point2I(20, 20), polygon.getMaximum());
	}
	
	@Test
	public void testGetMinimum() {
		final Polygon2I polygon = new Polygon2I(new Point2I(10, 10), new Point2I(20, 10), new Point2I(20, 20), new Point2I(10, 20));
		
		assertEquals(new Point2I(10, 10), polygon.getMinimum());
	}
	
	@Test
	public void testGetName() {
		final Polygon2I polygon = new Polygon2I(new Point2I(10, 10), new Point2I(20, 10), new Point2I(20, 20), new Point2I(10, 20));
		
		assertEquals(Polygon2I.NAME, polygon.getName());
	}
	
	@Test
	public void testGetPoints() {
		final Polygon2I polygon = new Polygon2I(new Point2I(10, 10), new Point2I(20, 10), new Point2I(20, 20), new Point2I(10, 20));
		
		final List<Point2I> points = polygon.getPoints();
		
		assertNotNull(points);
		
		assertTrue(points.size() == 4);
		
		assertEquals(new Point2I(10, 10), points.get(0));
		assertEquals(new Point2I(20, 10), points.get(1));
		assertEquals(new Point2I(20, 20), points.get(2));
		assertEquals(new Point2I(10, 20), points.get(3));
	}
	
	@Test
	public void testGetRectangle() {
		final Polygon2I polygon = new Polygon2I(new Point2I(10, 10), new Point2I(20, 10), new Point2I(20, 20), new Point2I(10, 20));
		
		final Rectangle2I rectangle = polygon.getRectangle();
		
		assertNotNull(rectangle);
		
		assertEquals(new Point2I(10, 10), rectangle.getA());
		assertEquals(new Point2I(20, 10), rectangle.getB());
		assertEquals(new Point2I(20, 20), rectangle.getC());
		assertEquals(new Point2I(10, 20), rectangle.getD());
	}
	
	@Test
	public void testHashCode() {
		final Polygon2I a = new Polygon2I(new Point2I(10, 10), new Point2I(20, 10), new Point2I(20, 20), new Point2I(10, 20));
		final Polygon2I b = new Polygon2I(new Point2I(10, 10), new Point2I(20, 10), new Point2I(20, 20), new Point2I(10, 20));
		
		assertEquals(a.hashCode(), a.hashCode());
		assertEquals(a.hashCode(), b.hashCode());
	}
	
	@Test
	public void testToString() {
		final Polygon2I polygon = new Polygon2I(new Point2I(10, 10), new Point2I(20, 10), new Point2I(20, 20), new Point2I(10, 20));
		
		assertEquals("new Polygon2I(new Point2I[] {new Point2I(10, 10), new Point2I(20, 10), new Point2I(20, 20), new Point2I(10, 20)})", polygon.toString());
	}
	
	@Test
	public void testWrite() {
		final Polygon2I a = new Polygon2I(new Point2I(10, 10), new Point2I(20, 10), new Point2I(20, 20), new Point2I(10, 20));
		
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		
		final DataOutput dataOutput = new DataOutputStream(byteArrayOutputStream);
		
		a.write(dataOutput);
		
		final byte[] bytes = byteArrayOutputStream.toByteArray();
		
		final Polygon2I b = new Polygon2IReader().read(new DataInputStream(new ByteArrayInputStream(bytes)));
		
		assertEquals(a, b);
		
		assertThrows(NullPointerException.class, () -> a.write((DataOutput)(null)));
		assertThrows(UncheckedIOException.class, () -> a.write(new DataOutputMock()));
	}
}