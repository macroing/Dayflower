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

import org.dayflower.geometry.Point2F;
import org.dayflower.mock.DataOutputMock;
import org.dayflower.mock.NodeHierarchicalVisitorMock;
import org.dayflower.mock.NodeVisitorMock;

import org.junit.jupiter.api.Test;

import org.macroing.java.util.visitor.NodeHierarchicalVisitor;
import org.macroing.java.util.visitor.NodeTraversalException;
import org.macroing.java.util.visitor.NodeVisitor;

@SuppressWarnings("static-method")
public final class Polygon2FUnitTests {
	public Polygon2FUnitTests() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Test
	public void testAcceptNodeHierarchicalVisitor() {
		final Point2F a = new Point2F(10.0F, 10.0F);
		final Point2F b = new Point2F(20.0F, 10.0F);
		final Point2F c = new Point2F(20.0F, 20.0F);
		final Point2F d = new Point2F(10.0F, 20.0F);
		
		final Polygon2F polygon = new Polygon2F(a, b, c, d);
		
		final List<LineSegment2F> lineSegments = polygon.getLineSegments();
		
		final LineSegment2F lineSegment0 = lineSegments.get(0);
		final LineSegment2F lineSegment1 = lineSegments.get(1);
		final LineSegment2F lineSegment2 = lineSegments.get(2);
		final LineSegment2F lineSegment3 = lineSegments.get(3);
		
		final Rectangle2F rectangle = polygon.getRectangle();
		
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
		final Polygon2F polygon = new Polygon2F(new Point2F(10.0F, 10.0F), new Point2F(20.0F, 10.0F), new Point2F(20.0F, 20.0F), new Point2F(10.0F, 20.0F));
		
		assertThrows(NodeTraversalException.class, () -> polygon.accept(new NodeVisitorMock(true)));
		assertThrows(NullPointerException.class, () -> polygon.accept((NodeVisitor)(null)));
	}
	
	@Test
	public void testConstants() {
		assertEquals(3, Polygon2F.ID);
		assertEquals("Polygon", Polygon2F.NAME);
	}
	
	@Test
	public void testConstructorPoint2Fs() {
		final Polygon2F polygon = new Polygon2F(new Point2F(10.0F, 10.0F), new Point2F(10.0F, 30.0F), new Point2F(30.0F, 30.0F), new Point2F(30.0F, 10.0F));
		
		final List<LineSegment2F> lineSegments = polygon.getLineSegments();
		
		final List<Point2F> points = polygon.getPoints();
		
		final Rectangle2F rectangle = polygon.getRectangle();
		
		assertEquals(new Point2F(10.0F, 10.0F), points.get(0));
		assertEquals(new Point2F(10.0F, 30.0F), points.get(1));
		assertEquals(new Point2F(30.0F, 30.0F), points.get(2));
		assertEquals(new Point2F(30.0F, 10.0F), points.get(3));
		
		assertNotNull(lineSegments);
		
		assertTrue(lineSegments.size() == 4);
		
		assertNotNull(lineSegments.get(0));
		assertNotNull(lineSegments.get(1));
		assertNotNull(lineSegments.get(2));
		assertNotNull(lineSegments.get(3));
		
		assertEquals(new Point2F(10.0F, 10.0F), lineSegments.get(0).getA());
		assertEquals(new Point2F(10.0F, 30.0F), lineSegments.get(0).getB());
		
		assertEquals(new Point2F(10.0F, 30.0F), lineSegments.get(1).getA());
		assertEquals(new Point2F(30.0F, 30.0F), lineSegments.get(1).getB());
		
		assertEquals(new Point2F(30.0F, 30.0F), lineSegments.get(2).getA());
		assertEquals(new Point2F(30.0F, 10.0F), lineSegments.get(2).getB());
		
		assertEquals(new Point2F(30.0F, 10.0F), lineSegments.get(3).getA());
		assertEquals(new Point2F(10.0F, 10.0F), lineSegments.get(3).getB());
		
		assertEquals(new Point2F(10.0F, 10.0F), rectangle.getA());
		assertEquals(new Point2F(30.0F, 10.0F), rectangle.getB());
		assertEquals(new Point2F(30.0F, 30.0F), rectangle.getC());
		assertEquals(new Point2F(10.0F, 30.0F), rectangle.getD());
		
		assertThrows(NullPointerException.class, () -> new Polygon2F(new Point2F(), new Point2F(), new Point2F(), null));
		assertThrows(NullPointerException.class, () -> new Polygon2F(new Point2F(), new Point2F(), null, new Point2F()));
		assertThrows(NullPointerException.class, () -> new Polygon2F(new Point2F(), null, new Point2F(), new Point2F()));
		assertThrows(NullPointerException.class, () -> new Polygon2F(null, new Point2F(), new Point2F(), new Point2F()));
		assertThrows(NullPointerException.class, () -> new Polygon2F((Point2F[])(null)));
		
		assertThrows(IllegalArgumentException.class, () -> new Polygon2F(new Point2F(10.0F, 10.0F), new Point2F(10.0F, 20.0F)));
	}
	
	@Test
	public void testContainsPoint2F() {
		final Polygon2F polygon = new Polygon2F(new Point2F(20.0F, 20.0F), new Point2F(30.0F, 20.0F), new Point2F(30.0F, 30.0F), new Point2F(20.0F, 30.0F));
		
		assertTrue(polygon.contains(new Point2F(20.0F, 20.0F)));
		assertTrue(polygon.contains(new Point2F(25.0F, 20.0F)));
		assertTrue(polygon.contains(new Point2F(30.0F, 20.0F)));
		assertTrue(polygon.contains(new Point2F(30.0F, 25.0F)));
		assertTrue(polygon.contains(new Point2F(30.0F, 30.0F)));
		assertTrue(polygon.contains(new Point2F(25.0F, 30.0F)));
		assertTrue(polygon.contains(new Point2F(20.0F, 30.0F)));
		assertTrue(polygon.contains(new Point2F(20.0F, 25.0F)));
		assertTrue(polygon.contains(new Point2F(25.0F, 25.0F)));
		
		assertFalse(polygon.contains(new Point2F(19.0F, 20.0F)));
		assertFalse(polygon.contains(new Point2F(31.0F, 20.0F)));
		assertFalse(polygon.contains(new Point2F(19.0F, 30.0F)));
		assertFalse(polygon.contains(new Point2F(31.0F, 30.0F)));
		assertFalse(polygon.contains(new Point2F(20.0F, 19.0F)));
		assertFalse(polygon.contains(new Point2F(20.0F, 31.0F)));
		assertFalse(polygon.contains(new Point2F(30.0F, 19.0F)));
		assertFalse(polygon.contains(new Point2F(30.0F, 31.0F)));
		
		assertThrows(NullPointerException.class, () -> polygon.contains(null));
	}
	
	@Test
	public void testEquals() {
		final Polygon2F a = new Polygon2F(new Point2F(20.0F, 20.0F), new Point2F(30.0F, 20.0F), new Point2F(30.0F, 30.0F), new Point2F(20.0F, 30.0F));
		final Polygon2F b = new Polygon2F(new Point2F(20.0F, 20.0F), new Point2F(30.0F, 20.0F), new Point2F(30.0F, 30.0F), new Point2F(20.0F, 30.0F));
		final Polygon2F c = new Polygon2F(new Point2F(20.0F, 20.0F), new Point2F(40.0F, 20.0F), new Point2F(40.0F, 30.0F), new Point2F(20.0F, 30.0F));
		final Polygon2F d = new Polygon2F(new Point2F(10.0F, 20.0F), new Point2F(30.0F, 20.0F), new Point2F(30.0F, 30.0F), new Point2F(10.0F, 30.0F));
		final Polygon2F e = new Polygon2F(new Point2F(20.0F, 20.0F), new Point2F(30.0F, 20.0F), new Point2F(30.0F, 40.0F), new Point2F(20.0F, 40.0F));
		final Polygon2F f = null;
		
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
	public void testGetID() {
		final Polygon2F polygon = new Polygon2F(new Point2F(10.0F, 10.0F), new Point2F(20.0F, 10.0F), new Point2F(20.0F, 20.0F), new Point2F(10.0F, 20.0F));
		
		assertEquals(Polygon2F.ID, polygon.getID());
	}
	
	@Test
	public void testGetLineSegments() {
		final Polygon2F polygon = new Polygon2F(new Point2F(10.0F, 10.0F), new Point2F(10.0F, 30.0F), new Point2F(30.0F, 30.0F), new Point2F(30.0F, 10.0F));
		
		final List<LineSegment2F> lineSegments = polygon.getLineSegments();
		
		assertNotNull(lineSegments);
		
		assertTrue(lineSegments.size() == 4);
		
		assertNotNull(lineSegments.get(0));
		assertNotNull(lineSegments.get(1));
		assertNotNull(lineSegments.get(2));
		assertNotNull(lineSegments.get(3));
		
		assertEquals(new Point2F(10.0F, 10.0F), lineSegments.get(0).getA());
		assertEquals(new Point2F(10.0F, 30.0F), lineSegments.get(0).getB());
		
		assertEquals(new Point2F(10.0F, 30.0F), lineSegments.get(1).getA());
		assertEquals(new Point2F(30.0F, 30.0F), lineSegments.get(1).getB());
		
		assertEquals(new Point2F(30.0F, 30.0F), lineSegments.get(2).getA());
		assertEquals(new Point2F(30.0F, 10.0F), lineSegments.get(2).getB());
		
		assertEquals(new Point2F(30.0F, 10.0F), lineSegments.get(3).getA());
		assertEquals(new Point2F(10.0F, 10.0F), lineSegments.get(3).getB());
	}
	
	@Test
	public void testGetName() {
		final Polygon2F polygon = new Polygon2F(new Point2F(10.0F, 10.0F), new Point2F(20.0F, 10.0F), new Point2F(20.0F, 20.0F), new Point2F(10.0F, 20.0F));
		
		assertEquals(Polygon2F.NAME, polygon.getName());
	}
	
	@Test
	public void testGetPoint() {
		final Polygon2F polygon = new Polygon2F(new Point2F(10.0F, 10.0F), new Point2F(20.0F, 10.0F), new Point2F(20.0F, 20.0F), new Point2F(10.0F, 20.0F));
		
		assertEquals(new Point2F(10.0F, 10.0F), polygon.getPoint(0));
		
		assertThrows(IllegalArgumentException.class, () -> polygon.getPoint(-1));
		assertThrows(IllegalArgumentException.class, () -> polygon.getPoint(+4));
	}
	
	@Test
	public void testGetPointCount() {
		final Polygon2F polygon = new Polygon2F(new Point2F(10.0F, 10.0F), new Point2F(20.0F, 10.0F), new Point2F(20.0F, 20.0F), new Point2F(10.0F, 20.0F));
		
		assertEquals(4, polygon.getPointCount());
	}
	
	@Test
	public void testGetPoints() {
		final Polygon2F polygon = new Polygon2F(new Point2F(10.0F, 10.0F), new Point2F(20.0F, 10.0F), new Point2F(20.0F, 20.0F), new Point2F(10.0F, 20.0F));
		
		final List<Point2F> points = polygon.getPoints();
		
		assertNotNull(points);
		
		assertTrue(points.size() == 4);
		
		assertEquals(new Point2F(10.0F, 10.0F), points.get(0));
		assertEquals(new Point2F(20.0F, 10.0F), points.get(1));
		assertEquals(new Point2F(20.0F, 20.0F), points.get(2));
		assertEquals(new Point2F(10.0F, 20.0F), points.get(3));
	}
	
	@Test
	public void testGetRectangle() {
		final Polygon2F polygon = new Polygon2F(new Point2F(10.0F, 10.0F), new Point2F(20.0F, 10.0F), new Point2F(20.0F, 20.0F), new Point2F(10.0F, 20.0F));
		
		final Rectangle2F rectangle = polygon.getRectangle();
		
		assertNotNull(rectangle);
		
		assertEquals(new Point2F(10.0F, 10.0F), rectangle.getA());
		assertEquals(new Point2F(20.0F, 10.0F), rectangle.getB());
		assertEquals(new Point2F(20.0F, 20.0F), rectangle.getC());
		assertEquals(new Point2F(10.0F, 20.0F), rectangle.getD());
	}
	
	@Test
	public void testHashCode() {
		final Polygon2F a = new Polygon2F(new Point2F(10.0F, 10.0F), new Point2F(20.0F, 10.0F), new Point2F(20.0F, 20.0F), new Point2F(10.0F, 20.0F));
		final Polygon2F b = new Polygon2F(new Point2F(10.0F, 10.0F), new Point2F(20.0F, 10.0F), new Point2F(20.0F, 20.0F), new Point2F(10.0F, 20.0F));
		
		assertEquals(a.hashCode(), a.hashCode());
		assertEquals(a.hashCode(), b.hashCode());
	}
	
	@Test
	public void testToString() {
		final Polygon2F polygon = new Polygon2F(new Point2F(10.0F, 10.0F), new Point2F(20.0F, 10.0F), new Point2F(20.0F, 20.0F), new Point2F(10.0F, 20.0F));
		
		assertEquals("new Polygon2F(new Point2F[] {new Point2F(10.0F, 10.0F), new Point2F(20.0F, 10.0F), new Point2F(20.0F, 20.0F), new Point2F(10.0F, 20.0F)})", polygon.toString());
	}
	
	@Test
	public void testWrite() {
		final Polygon2F a = new Polygon2F(new Point2F(10.0F, 10.0F), new Point2F(20.0F, 10.0F), new Point2F(20.0F, 20.0F), new Point2F(10.0F, 20.0F));
		
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		
		final DataOutput dataOutput = new DataOutputStream(byteArrayOutputStream);
		
		a.write(dataOutput);
		
		final byte[] bytes = byteArrayOutputStream.toByteArray();
		
		final Polygon2F b = new Polygon2FReader().read(new DataInputStream(new ByteArrayInputStream(bytes)));
		
		assertEquals(a, b);
		
		assertThrows(NullPointerException.class, () -> a.write((DataOutput)(null)));
		assertThrows(UncheckedIOException.class, () -> a.write(new DataOutputMock()));
	}
}