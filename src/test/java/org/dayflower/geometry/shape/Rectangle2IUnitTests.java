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

import org.dayflower.geometry.Point2D;
import org.dayflower.geometry.Point2F;
import org.dayflower.geometry.Point2I;
import org.dayflower.mock.DataOutputMock;
import org.dayflower.mock.NodeHierarchicalVisitorMock;
import org.dayflower.mock.NodeVisitorMock;

import org.junit.jupiter.api.Test;

import org.macroing.java.util.visitor.NodeHierarchicalVisitor;
import org.macroing.java.util.visitor.NodeTraversalException;
import org.macroing.java.util.visitor.NodeVisitor;

@SuppressWarnings("static-method")
public final class Rectangle2IUnitTests {
	public Rectangle2IUnitTests() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Test
	public void testAcceptNodeHierarchicalVisitor() {
		final Point2I a = new Point2I(10, 10);
		final Point2I b = new Point2I(20, 10);
		final Point2I c = new Point2I(20, 20);
		final Point2I d = new Point2I(10, 20);
		
		final Rectangle2I rectangle = new Rectangle2I(a, b, c, d);
		
		final List<LineSegment2I> lineSegments = rectangle.getLineSegments();
		
		final LineSegment2I lineSegment0 = lineSegments.get(0);
		final LineSegment2I lineSegment1 = lineSegments.get(1);
		final LineSegment2I lineSegment2 = lineSegments.get(2);
		final LineSegment2I lineSegment3 = lineSegments.get(3);
		
		assertTrue(rectangle.accept(new NodeHierarchicalVisitorMock(node -> false,                                                                                                                                                                                                              node -> node.equals(rectangle))));
		assertTrue(rectangle.accept(new NodeHierarchicalVisitorMock(node -> node.equals(rectangle),                                                                                                                                                                                             node -> node.equals(rectangle))));
		assertTrue(rectangle.accept(new NodeHierarchicalVisitorMock(node -> node.equals(rectangle) || node.equals(lineSegment0),                                                                                                                                                                node -> node.equals(rectangle) || node.equals(lineSegment0))));
		assertTrue(rectangle.accept(new NodeHierarchicalVisitorMock(node -> node.equals(rectangle) || node.equals(lineSegment0) || node.equals(lineSegment1),                                                                                                                                   node -> node.equals(rectangle) || node.equals(lineSegment0) || node.equals(lineSegment1))));
		assertTrue(rectangle.accept(new NodeHierarchicalVisitorMock(node -> node.equals(rectangle) || node.equals(lineSegment0) || node.equals(lineSegment1) || node.equals(lineSegment2),                                                                                                      node -> node.equals(rectangle) || node.equals(lineSegment0) || node.equals(lineSegment1) || node.equals(lineSegment2))));
		assertTrue(rectangle.accept(new NodeHierarchicalVisitorMock(node -> node.equals(rectangle) || node.equals(lineSegment0) || node.equals(lineSegment1) || node.equals(lineSegment2) || node.equals(lineSegment3),                                                                         node -> node.equals(rectangle) || node.equals(lineSegment0) || node.equals(lineSegment1) || node.equals(lineSegment2) || node.equals(lineSegment3))));
		assertTrue(rectangle.accept(new NodeHierarchicalVisitorMock(node -> node.equals(rectangle) || node.equals(lineSegment0) || node.equals(lineSegment1) || node.equals(lineSegment2) || node.equals(lineSegment3) || node.equals(a),                                                       node -> node.equals(rectangle) || node.equals(lineSegment0) || node.equals(lineSegment1) || node.equals(lineSegment2) || node.equals(lineSegment3) || node.equals(a))));
		assertTrue(rectangle.accept(new NodeHierarchicalVisitorMock(node -> node.equals(rectangle) || node.equals(lineSegment0) || node.equals(lineSegment1) || node.equals(lineSegment2) || node.equals(lineSegment3) || node.equals(a) || node.equals(b),                                     node -> node.equals(rectangle) || node.equals(lineSegment0) || node.equals(lineSegment1) || node.equals(lineSegment2) || node.equals(lineSegment3) || node.equals(a) || node.equals(b))));
		assertTrue(rectangle.accept(new NodeHierarchicalVisitorMock(node -> node.equals(rectangle) || node.equals(lineSegment0) || node.equals(lineSegment1) || node.equals(lineSegment2) || node.equals(lineSegment3) || node.equals(a) || node.equals(b) || node.equals(c),                   node -> node.equals(rectangle) || node.equals(lineSegment0) || node.equals(lineSegment1) || node.equals(lineSegment2) || node.equals(lineSegment3) || node.equals(a) || node.equals(b) || node.equals(c))));
		assertTrue(rectangle.accept(new NodeHierarchicalVisitorMock(node -> node.equals(rectangle) || node.equals(lineSegment0) || node.equals(lineSegment1) || node.equals(lineSegment2) || node.equals(lineSegment3) || node.equals(a) || node.equals(b) || node.equals(c) || node.equals(d), node -> node.equals(rectangle) || node.equals(lineSegment0) || node.equals(lineSegment1) || node.equals(lineSegment2) || node.equals(lineSegment3) || node.equals(a) || node.equals(b) || node.equals(c) || node.equals(d))));
		
		assertThrows(NodeTraversalException.class, () -> rectangle.accept(new NodeHierarchicalVisitorMock(null, null)));
		assertThrows(NullPointerException.class, () -> rectangle.accept((NodeHierarchicalVisitor)(null)));
	}
	
	@Test
	public void testAcceptNodeVisitor() {
		final Rectangle2I rectangle = new Rectangle2I(new Point2I(10, 10), new Point2I(20, 20));
		
		assertThrows(NodeTraversalException.class, () -> rectangle.accept(new NodeVisitorMock(true)));
		assertThrows(NullPointerException.class, () -> rectangle.accept((NodeVisitor)(null)));
	}
	
	@Test
	public void testConstants() {
		assertEquals(4, Rectangle2I.ID);
		assertEquals("Rectangle", Rectangle2I.NAME);
	}
	
	@Test
	public void testConstructorCircle2I() {
		final Rectangle2I rectangle = new Rectangle2I(new Circle2I(new Point2I(20, 20), 10));
		
		final List<LineSegment2I> lineSegments = rectangle.getLineSegments();
		
		assertEquals(new Point2I(10, 10), rectangle.getA());
		assertEquals(new Point2I(30, 10), rectangle.getB());
		assertEquals(new Point2I(30, 30), rectangle.getC());
		assertEquals(new Point2I(10, 30), rectangle.getD());
		
		assertNotNull(lineSegments);
		
		assertTrue(lineSegments.size() == 4);
		
		assertNotNull(lineSegments.get(0));
		assertNotNull(lineSegments.get(1));
		assertNotNull(lineSegments.get(2));
		assertNotNull(lineSegments.get(3));
		
		assertEquals(new Point2I(10, 10), lineSegments.get(0).getA());
		assertEquals(new Point2I(30, 10), lineSegments.get(0).getB());
		
		assertEquals(new Point2I(30, 10), lineSegments.get(1).getA());
		assertEquals(new Point2I(30, 30), lineSegments.get(1).getB());
		
		assertEquals(new Point2I(30, 30), lineSegments.get(2).getA());
		assertEquals(new Point2I(10, 30), lineSegments.get(2).getB());
		
		assertEquals(new Point2I(10, 30), lineSegments.get(3).getA());
		assertEquals(new Point2I(10, 10), lineSegments.get(3).getB());
		
		assertThrows(NullPointerException.class, () -> new Rectangle2I((Circle2I)(null)));
	}
	
	@Test
	public void testConstructorPoint2IPoint2I() {
		final Rectangle2I rectangle = new Rectangle2I(new Point2I(30, 30), new Point2I(10, 10));
		
		final List<LineSegment2I> lineSegments = rectangle.getLineSegments();
		
		assertEquals(new Point2I(10, 10), rectangle.getA());
		assertEquals(new Point2I(30, 10), rectangle.getB());
		assertEquals(new Point2I(30, 30), rectangle.getC());
		assertEquals(new Point2I(10, 30), rectangle.getD());
		
		assertNotNull(lineSegments);
		
		assertTrue(lineSegments.size() == 4);
		
		assertNotNull(lineSegments.get(0));
		assertNotNull(lineSegments.get(1));
		assertNotNull(lineSegments.get(2));
		assertNotNull(lineSegments.get(3));
		
		assertEquals(new Point2I(10, 10), lineSegments.get(0).getA());
		assertEquals(new Point2I(30, 10), lineSegments.get(0).getB());
		
		assertEquals(new Point2I(30, 10), lineSegments.get(1).getA());
		assertEquals(new Point2I(30, 30), lineSegments.get(1).getB());
		
		assertEquals(new Point2I(30, 30), lineSegments.get(2).getA());
		assertEquals(new Point2I(10, 30), lineSegments.get(2).getB());
		
		assertEquals(new Point2I(10, 30), lineSegments.get(3).getA());
		assertEquals(new Point2I(10, 10), lineSegments.get(3).getB());
		
		assertThrows(NullPointerException.class, () -> new Rectangle2I(new Point2I(), null));
		assertThrows(NullPointerException.class, () -> new Rectangle2I(null, new Point2I()));
	}
	
	@Test
	public void testConstructorPoint2IPoint2IPoint2IPoint2I() {
		final Rectangle2I rectangle = new Rectangle2I(new Point2I(10, 10), new Point2I(10, 30), new Point2I(30, 30), new Point2I(30, 10));
		
		final List<LineSegment2I> lineSegments = rectangle.getLineSegments();
		
		assertEquals(new Point2I(10, 10), rectangle.getA());
		assertEquals(new Point2I(10, 30), rectangle.getB());
		assertEquals(new Point2I(30, 30), rectangle.getC());
		assertEquals(new Point2I(30, 10), rectangle.getD());
		
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
		
		assertThrows(NullPointerException.class, () -> new Rectangle2I(new Point2I(), new Point2I(), new Point2I(), null));
		assertThrows(NullPointerException.class, () -> new Rectangle2I(new Point2I(), new Point2I(), null, new Point2I()));
		assertThrows(NullPointerException.class, () -> new Rectangle2I(new Point2I(), null, new Point2I(), new Point2I()));
		assertThrows(NullPointerException.class, () -> new Rectangle2I(null, new Point2I(), new Point2I(), new Point2I()));
		
		assertThrows(IllegalArgumentException.class, () -> new Rectangle2I(new Point2I(10, 10), new Point2I(10, 20), new Point2I(10, 40), new Point2I(10, 80)));
		assertThrows(IllegalArgumentException.class, () -> new Rectangle2I(new Point2I(10, 10), new Point2I(20, 10), new Point2I(40, 10), new Point2I(80, 10)));
	}
	
	@Test
	public void testConstructorRectangle2D() {
		final Rectangle2I rectangle = new Rectangle2I(new Rectangle2D(new Point2D(10.0D, 10.0D), new Point2D(10.0D, 30.0D), new Point2D(30.0D, 30.0D), new Point2D(30.0D, 10.0D)));
		
		final List<LineSegment2I> lineSegments = rectangle.getLineSegments();
		
		assertEquals(new Point2I(10, 10), rectangle.getA());
		assertEquals(new Point2I(10, 30), rectangle.getB());
		assertEquals(new Point2I(30, 30), rectangle.getC());
		assertEquals(new Point2I(30, 10), rectangle.getD());
		
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
		
		assertThrows(NullPointerException.class, () -> new Rectangle2I((Rectangle2D)(null)));
	}
	
	@Test
	public void testConstructorRectangle2F() {
		final Rectangle2I rectangle = new Rectangle2I(new Rectangle2F(new Point2F(10.0F, 10.0F), new Point2F(10.0F, 30.0F), new Point2F(30.0F, 30.0F), new Point2F(30.0F, 10.0F)));
		
		final List<LineSegment2I> lineSegments = rectangle.getLineSegments();
		
		assertEquals(new Point2I(10, 10), rectangle.getA());
		assertEquals(new Point2I(10, 30), rectangle.getB());
		assertEquals(new Point2I(30, 30), rectangle.getC());
		assertEquals(new Point2I(30, 10), rectangle.getD());
		
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
		
		assertThrows(NullPointerException.class, () -> new Rectangle2I((Rectangle2F)(null)));
	}
	
	@Test
	public void testContainsPoint2I() {
		final Rectangle2I rectangle2I = new Rectangle2I(new Point2I(20, 20), new Point2I(30, 30));
		
		assertTrue(rectangle2I.contains(new Point2I(20, 20)));
		assertTrue(rectangle2I.contains(new Point2I(25, 20)));
		assertTrue(rectangle2I.contains(new Point2I(30, 20)));
		assertTrue(rectangle2I.contains(new Point2I(30, 25)));
		assertTrue(rectangle2I.contains(new Point2I(30, 30)));
		assertTrue(rectangle2I.contains(new Point2I(25, 30)));
		assertTrue(rectangle2I.contains(new Point2I(20, 30)));
		assertTrue(rectangle2I.contains(new Point2I(20, 25)));
		assertTrue(rectangle2I.contains(new Point2I(25, 25)));
		
		assertFalse(rectangle2I.contains(new Point2I(19, 20)));
		assertFalse(rectangle2I.contains(new Point2I(31, 20)));
		assertFalse(rectangle2I.contains(new Point2I(19, 30)));
		assertFalse(rectangle2I.contains(new Point2I(31, 30)));
		assertFalse(rectangle2I.contains(new Point2I(20, 19)));
		assertFalse(rectangle2I.contains(new Point2I(20, 31)));
		assertFalse(rectangle2I.contains(new Point2I(30, 19)));
		assertFalse(rectangle2I.contains(new Point2I(30, 31)));
		
		assertThrows(NullPointerException.class, () -> rectangle2I.contains(null));
	}
	
	@Test
	public void testContainsPoint2IBoolean() {
		final Rectangle2I rectangle2I = new Rectangle2I(new Point2I(20, 20), new Point2I(30, 30));
		
		assertTrue(rectangle2I.contains(new Point2I(20, 20), false));
		assertTrue(rectangle2I.contains(new Point2I(25, 20), false));
		assertTrue(rectangle2I.contains(new Point2I(30, 20), false));
		assertTrue(rectangle2I.contains(new Point2I(30, 25), false));
		assertTrue(rectangle2I.contains(new Point2I(30, 30), false));
		assertTrue(rectangle2I.contains(new Point2I(25, 30), false));
		assertTrue(rectangle2I.contains(new Point2I(20, 30), false));
		assertTrue(rectangle2I.contains(new Point2I(20, 25), false));
		assertTrue(rectangle2I.contains(new Point2I(25, 25), false));
		
		assertTrue(rectangle2I.contains(new Point2I(20, 20), true));
		assertTrue(rectangle2I.contains(new Point2I(25, 20), true));
		assertTrue(rectangle2I.contains(new Point2I(30, 20), true));
		assertTrue(rectangle2I.contains(new Point2I(30, 25), true));
		assertTrue(rectangle2I.contains(new Point2I(30, 30), true));
		assertTrue(rectangle2I.contains(new Point2I(25, 30), true));
		assertTrue(rectangle2I.contains(new Point2I(20, 30), true));
		assertTrue(rectangle2I.contains(new Point2I(20, 25), true));
		
		assertFalse(rectangle2I.contains(new Point2I(19, 20), false));
		assertFalse(rectangle2I.contains(new Point2I(31, 20), false));
		assertFalse(rectangle2I.contains(new Point2I(19, 30), false));
		assertFalse(rectangle2I.contains(new Point2I(31, 30), false));
		assertFalse(rectangle2I.contains(new Point2I(20, 19), false));
		assertFalse(rectangle2I.contains(new Point2I(20, 31), false));
		assertFalse(rectangle2I.contains(new Point2I(30, 19), false));
		assertFalse(rectangle2I.contains(new Point2I(30, 31), false));
		
		assertFalse(rectangle2I.contains(new Point2I(19, 20), true));
		assertFalse(rectangle2I.contains(new Point2I(31, 20), true));
		assertFalse(rectangle2I.contains(new Point2I(19, 30), true));
		assertFalse(rectangle2I.contains(new Point2I(31, 30), true));
		assertFalse(rectangle2I.contains(new Point2I(20, 19), true));
		assertFalse(rectangle2I.contains(new Point2I(20, 31), true));
		assertFalse(rectangle2I.contains(new Point2I(30, 19), true));
		assertFalse(rectangle2I.contains(new Point2I(30, 31), true));
		
		assertFalse(rectangle2I.contains(new Point2I(25, 25), true));
		
		assertThrows(NullPointerException.class, () -> rectangle2I.contains(null, false));
	}
	
	@Test
	public void testEquals() {
		final Rectangle2I a = new Rectangle2I(new Point2I(20, 20), new Point2I(30, 20), new Point2I(30, 30), new Point2I(20, 30));
		final Rectangle2I b = new Rectangle2I(new Point2I(20, 20), new Point2I(30, 20), new Point2I(30, 30), new Point2I(20, 30));
		final Rectangle2I c = new Rectangle2I(new Point2I(20, 20), new Point2I(40, 20), new Point2I(40, 30), new Point2I(20, 30));
		final Rectangle2I d = new Rectangle2I(new Point2I(10, 20), new Point2I(30, 20), new Point2I(30, 30), new Point2I(10, 30));
		final Rectangle2I e = new Rectangle2I(new Point2I(20, 20), new Point2I(30, 20), new Point2I(30, 40), new Point2I(20, 40));
		final Rectangle2I f = null;
		
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
		final Rectangle2I rectangle2I = new Rectangle2I(new Point2I(0, 0), new Point2I(2, 0), new Point2I(2, 2), new Point2I(0, 2));
		
		final List<Point2I> pointsA = rectangle2I.findPoints(false);
		final List<Point2I> pointsB = rectangle2I.findPoints(true);
		
		assertNotNull(pointsA);
		assertNotNull(pointsB);
		
		assertEquals(9, pointsA.size());
		assertEquals(8, pointsB.size());
		
		assertEquals(new Point2I(0, 0), pointsA.get(0));
		assertEquals(new Point2I(1, 0), pointsA.get(1));
		assertEquals(new Point2I(2, 0), pointsA.get(2));
		assertEquals(new Point2I(0, 1), pointsA.get(3));
		assertEquals(new Point2I(1, 1), pointsA.get(4));
		assertEquals(new Point2I(2, 1), pointsA.get(5));
		assertEquals(new Point2I(0, 2), pointsA.get(6));
		assertEquals(new Point2I(1, 2), pointsA.get(7));
		assertEquals(new Point2I(2, 2), pointsA.get(8));
		
		assertEquals(new Point2I(0, 0), pointsB.get(0));
		assertEquals(new Point2I(1, 0), pointsB.get(1));
		assertEquals(new Point2I(2, 0), pointsB.get(2));
		assertEquals(new Point2I(0, 1), pointsB.get(3));
		assertEquals(new Point2I(2, 1), pointsB.get(4));
		assertEquals(new Point2I(0, 2), pointsB.get(5));
		assertEquals(new Point2I(1, 2), pointsB.get(6));
		assertEquals(new Point2I(2, 2), pointsB.get(7));
	}
	
	@Test
	public void testFromPoints() {
		final Rectangle2I a = Rectangle2I.fromPoints(new Point2I(10, 10), new Point2I(15, 15), new Point2I(20, 20), new Point2I(25, 25), new Point2I(30, 30));
		final Rectangle2I b = new Rectangle2I(new Point2I(10, 10), new Point2I(30, 30));
		
		assertEquals(a, b);
		
		assertThrows(NullPointerException.class, () -> Rectangle2I.fromPoints((Point2I[])(null)));
		assertThrows(NullPointerException.class, () -> Rectangle2I.fromPoints(new Point2I(), null));
		
		assertThrows(IllegalArgumentException.class, () -> Rectangle2I.fromPoints());
	}
	
	@Test
	public void testGetA() {
		final Rectangle2I rectangle = new Rectangle2I(new Point2I(10, 10), new Point2I(10, 30), new Point2I(30, 30), new Point2I(30, 10));
		
		assertEquals(new Point2I(10, 10), rectangle.getA());
	}
	
	@Test
	public void testGetB() {
		final Rectangle2I rectangle = new Rectangle2I(new Point2I(10, 10), new Point2I(10, 30), new Point2I(30, 30), new Point2I(30, 10));
		
		assertEquals(new Point2I(10, 30), rectangle.getB());
	}
	
	@Test
	public void testGetC() {
		final Rectangle2I rectangle = new Rectangle2I(new Point2I(10, 10), new Point2I(10, 30), new Point2I(30, 30), new Point2I(30, 10));
		
		assertEquals(new Point2I(30, 30), rectangle.getC());
	}
	
	@Test
	public void testGetD() {
		final Rectangle2I rectangle = new Rectangle2I(new Point2I(10, 10), new Point2I(10, 30), new Point2I(30, 30), new Point2I(30, 10));
		
		assertEquals(new Point2I(30, 10), rectangle.getD());
	}
	
	@Test
	public void testGetID() {
		final Rectangle2I rectangle = new Rectangle2I(new Point2I(10, 10), new Point2I(20, 20));
		
		assertEquals(Rectangle2I.ID, rectangle.getID());
	}
	
	@Test
	public void testGetLineSegments() {
		final Rectangle2I rectangle = new Rectangle2I(new Point2I(10, 10), new Point2I(10, 30), new Point2I(30, 30), new Point2I(30, 10));
		
		final List<LineSegment2I> lineSegments = rectangle.getLineSegments();
		
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
		final Rectangle2I rectangle = new Rectangle2I(new Point2I(10, 10), new Point2I(20, 20));
		
		assertEquals(new Point2I(20, 20), rectangle.getMaximum());
	}
	
	@Test
	public void testGetMinimum() {
		final Rectangle2I rectangle = new Rectangle2I(new Point2I(10, 10), new Point2I(20, 20));
		
		assertEquals(new Point2I(10, 10), rectangle.getMinimum());
	}
	
	@Test
	public void testGetName() {
		final Rectangle2I rectangle = new Rectangle2I(new Point2I(10, 10), new Point2I(20, 20));
		
		assertEquals(Rectangle2I.NAME, rectangle.getName());
	}
	
	@Test
	public void testHashCode() {
		final Rectangle2I a = new Rectangle2I(new Point2I(10, 10), new Point2I(20, 20));
		final Rectangle2I b = new Rectangle2I(new Point2I(10, 10), new Point2I(20, 20));
		
		assertEquals(a.hashCode(), a.hashCode());
		assertEquals(a.hashCode(), b.hashCode());
	}
	
	@Test
	public void testIsAxisAligned() {
		final Rectangle2I a = new Rectangle2I(new Point2I(10, 10), new Point2I(20, 10), new Point2I(20, 20), new Point2I(10, 20));
		final Rectangle2I b = new Rectangle2I(new Point2I(20, 20), new Point2I(25, 25), new Point2I(20, 30), new Point2I(15, 25));
		
		assertTrue(a.isAxisAligned());
		
		assertFalse(b.isAxisAligned());
	}
	
	@Test
	public void testIsRotated() {
		final Rectangle2I a = new Rectangle2I(new Point2I(20, 20), new Point2I(25, 25), new Point2I(20, 30), new Point2I(15, 25));
		final Rectangle2I b = new Rectangle2I(new Point2I(10, 10), new Point2I(20, 10), new Point2I(20, 20), new Point2I(10, 20));
		
		assertTrue(a.isRotated());
		
		assertFalse(b.isRotated());
	}
	
	@Test
	public void testToString() {
		final Rectangle2I rectangle = new Rectangle2I(new Point2I(10, 10), new Point2I(20, 20));
		
		assertEquals("new Rectangle2I(new Point2I(10, 10), new Point2I(20, 10), new Point2I(20, 20), new Point2I(10, 20))", rectangle.toString());
	}
	
	@Test
	public void testUnion() {
		final Rectangle2I a = new Rectangle2I(new Point2I(10, 10), new Point2I(20, 20));
		final Rectangle2I b = new Rectangle2I(new Point2I(20, 20), new Point2I(30, 30));
		final Rectangle2I c = new Rectangle2I(new Point2I(10, 10), new Point2I(30, 30));
		final Rectangle2I d = Rectangle2I.union(a, b);
		
		assertEquals(c, d);
		
		assertThrows(NullPointerException.class, () -> Rectangle2I.union(a, null));
		assertThrows(NullPointerException.class, () -> Rectangle2I.union(null, b));
	}
	
	@Test
	public void testWrite() {
		final Rectangle2I a = new Rectangle2I(new Point2I(10, 10), new Point2I(20, 20));
		
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		
		final DataOutput dataOutput = new DataOutputStream(byteArrayOutputStream);
		
		a.write(dataOutput);
		
		final byte[] bytes = byteArrayOutputStream.toByteArray();
		
		final Rectangle2I b = new Rectangle2IReader().read(new DataInputStream(new ByteArrayInputStream(bytes)));
		
		assertEquals(a, b);
		
		assertThrows(NullPointerException.class, () -> a.write((DataOutput)(null)));
		assertThrows(UncheckedIOException.class, () -> a.write(new DataOutputMock()));
	}
}