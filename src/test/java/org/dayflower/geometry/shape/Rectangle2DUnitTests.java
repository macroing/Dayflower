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

import org.dayflower.geometry.AngleD;
import org.dayflower.geometry.Point2D;
import org.dayflower.geometry.Vector2D;
import org.dayflower.mock.DataOutputMock;
import org.dayflower.mock.NodeHierarchicalVisitorMock;
import org.dayflower.mock.NodeVisitorMock;

import org.junit.jupiter.api.Test;

import org.macroing.java.util.visitor.NodeHierarchicalVisitor;
import org.macroing.java.util.visitor.NodeTraversalException;
import org.macroing.java.util.visitor.NodeVisitor;

@SuppressWarnings("static-method")
public final class Rectangle2DUnitTests {
	public Rectangle2DUnitTests() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Test
	public void testAcceptNodeHierarchicalVisitor() {
		final Point2D a = new Point2D(10.0D, 10.0D);
		final Point2D b = new Point2D(20.0D, 10.0D);
		final Point2D c = new Point2D(20.0D, 20.0D);
		final Point2D d = new Point2D(10.0D, 20.0D);
		
		final Rectangle2D rectangle = new Rectangle2D(a, b, c, d);
		
		final List<LineSegment2D> lineSegments = rectangle.getLineSegments();
		
		final LineSegment2D lineSegment0 = lineSegments.get(0);
		final LineSegment2D lineSegment1 = lineSegments.get(1);
		final LineSegment2D lineSegment2 = lineSegments.get(2);
		final LineSegment2D lineSegment3 = lineSegments.get(3);
		
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
		final Rectangle2D rectangle = new Rectangle2D(new Point2D(10.0D, 10.0D), new Point2D(20.0D, 20.0D));
		
		assertThrows(NodeTraversalException.class, () -> rectangle.accept(new NodeVisitorMock(true)));
		assertThrows(NullPointerException.class, () -> rectangle.accept((NodeVisitor)(null)));
	}
	
	@Test
	public void testConstants() {
		assertEquals(4, Rectangle2D.ID);
		assertEquals("Rectangle", Rectangle2D.NAME);
	}
	
	@Test
	public void testConstructorCircle2D() {
		final Rectangle2D rectangle = new Rectangle2D(new Circle2D(new Point2D(20.0D, 20.0D), 10.0D));
		
		final List<LineSegment2D> lineSegments = rectangle.getLineSegments();
		
		assertEquals(new Point2D(10.0D, 10.0D), rectangle.getA());
		assertEquals(new Point2D(30.0D, 10.0D), rectangle.getB());
		assertEquals(new Point2D(30.0D, 30.0D), rectangle.getC());
		assertEquals(new Point2D(10.0D, 30.0D), rectangle.getD());
		
		assertNotNull(lineSegments);
		
		assertTrue(lineSegments.size() == 4);
		
		assertNotNull(lineSegments.get(0));
		assertNotNull(lineSegments.get(1));
		assertNotNull(lineSegments.get(2));
		assertNotNull(lineSegments.get(3));
		
		assertEquals(new Point2D(10.0D, 10.0D), lineSegments.get(0).getA());
		assertEquals(new Point2D(30.0D, 10.0D), lineSegments.get(0).getB());
		
		assertEquals(new Point2D(30.0D, 10.0D), lineSegments.get(1).getA());
		assertEquals(new Point2D(30.0D, 30.0D), lineSegments.get(1).getB());
		
		assertEquals(new Point2D(30.0D, 30.0D), lineSegments.get(2).getA());
		assertEquals(new Point2D(10.0D, 30.0D), lineSegments.get(2).getB());
		
		assertEquals(new Point2D(10.0D, 30.0D), lineSegments.get(3).getA());
		assertEquals(new Point2D(10.0D, 10.0D), lineSegments.get(3).getB());
		
		assertThrows(NullPointerException.class, () -> new Rectangle2D((Circle2D)(null)));
	}
	
	@Test
	public void testConstructorPoint2DPoint2D() {
		final Rectangle2D rectangle = new Rectangle2D(new Point2D(30.0D, 30.0D), new Point2D(10.0D, 10.0D));
		
		final List<LineSegment2D> lineSegments = rectangle.getLineSegments();
		
		assertEquals(new Point2D(10.0D, 10.0D), rectangle.getA());
		assertEquals(new Point2D(30.0D, 10.0D), rectangle.getB());
		assertEquals(new Point2D(30.0D, 30.0D), rectangle.getC());
		assertEquals(new Point2D(10.0D, 30.0D), rectangle.getD());
		
		assertNotNull(lineSegments);
		
		assertTrue(lineSegments.size() == 4);
		
		assertNotNull(lineSegments.get(0));
		assertNotNull(lineSegments.get(1));
		assertNotNull(lineSegments.get(2));
		assertNotNull(lineSegments.get(3));
		
		assertEquals(new Point2D(10.0D, 10.0D), lineSegments.get(0).getA());
		assertEquals(new Point2D(30.0D, 10.0D), lineSegments.get(0).getB());
		
		assertEquals(new Point2D(30.0D, 10.0D), lineSegments.get(1).getA());
		assertEquals(new Point2D(30.0D, 30.0D), lineSegments.get(1).getB());
		
		assertEquals(new Point2D(30.0D, 30.0D), lineSegments.get(2).getA());
		assertEquals(new Point2D(10.0D, 30.0D), lineSegments.get(2).getB());
		
		assertEquals(new Point2D(10.0D, 30.0D), lineSegments.get(3).getA());
		assertEquals(new Point2D(10.0D, 10.0D), lineSegments.get(3).getB());
		
		assertThrows(NullPointerException.class, () -> new Rectangle2D(new Point2D(), null));
		assertThrows(NullPointerException.class, () -> new Rectangle2D(null, new Point2D()));
	}
	
	@Test
	public void testConstructorPoint2DPoint2DPoint2DPoint2D() {
		final Rectangle2D rectangle = new Rectangle2D(new Point2D(10.0D, 10.0D), new Point2D(10.0D, 30.0D), new Point2D(30.0D, 30.0D), new Point2D(30.0D, 10.0D));
		
		final List<LineSegment2D> lineSegments = rectangle.getLineSegments();
		
		assertEquals(new Point2D(10.0D, 10.0D), rectangle.getA());
		assertEquals(new Point2D(10.0D, 30.0D), rectangle.getB());
		assertEquals(new Point2D(30.0D, 30.0D), rectangle.getC());
		assertEquals(new Point2D(30.0D, 10.0D), rectangle.getD());
		
		assertNotNull(lineSegments);
		
		assertTrue(lineSegments.size() == 4);
		
		assertNotNull(lineSegments.get(0));
		assertNotNull(lineSegments.get(1));
		assertNotNull(lineSegments.get(2));
		assertNotNull(lineSegments.get(3));
		
		assertEquals(new Point2D(10.0D, 10.0D), lineSegments.get(0).getA());
		assertEquals(new Point2D(10.0D, 30.0D), lineSegments.get(0).getB());
		
		assertEquals(new Point2D(10.0D, 30.0D), lineSegments.get(1).getA());
		assertEquals(new Point2D(30.0D, 30.0D), lineSegments.get(1).getB());
		
		assertEquals(new Point2D(30.0D, 30.0D), lineSegments.get(2).getA());
		assertEquals(new Point2D(30.0D, 10.0D), lineSegments.get(2).getB());
		
		assertEquals(new Point2D(30.0D, 10.0D), lineSegments.get(3).getA());
		assertEquals(new Point2D(10.0D, 10.0D), lineSegments.get(3).getB());
		
		assertThrows(NullPointerException.class, () -> new Rectangle2D(new Point2D(), new Point2D(), new Point2D(), null));
		assertThrows(NullPointerException.class, () -> new Rectangle2D(new Point2D(), new Point2D(), null, new Point2D()));
		assertThrows(NullPointerException.class, () -> new Rectangle2D(new Point2D(), null, new Point2D(), new Point2D()));
		assertThrows(NullPointerException.class, () -> new Rectangle2D(null, new Point2D(), new Point2D(), new Point2D()));
		
		assertThrows(IllegalArgumentException.class, () -> new Rectangle2D(new Point2D(10.0D, 10.0D), new Point2D(10.0D, 20.0D), new Point2D(10.0D, 40.0D), new Point2D(10.0D, 80.0D)));
		assertThrows(IllegalArgumentException.class, () -> new Rectangle2D(new Point2D(10.0D, 10.0D), new Point2D(20.0D, 10.0D), new Point2D(40.0D, 10.0D), new Point2D(80.0D, 10.0D)));
	}
	
	@Test
	public void testContainsPoint2D() {
		final Rectangle2D rectangle2D = new Rectangle2D(new Point2D(20.0D, 20.0D), new Point2D(30.0D, 30.0D));
		
		assertTrue(rectangle2D.contains(new Point2D(20.0D, 20.0D)));
		assertTrue(rectangle2D.contains(new Point2D(25.0D, 20.0D)));
		assertTrue(rectangle2D.contains(new Point2D(30.0D, 20.0D)));
		assertTrue(rectangle2D.contains(new Point2D(30.0D, 25.0D)));
		assertTrue(rectangle2D.contains(new Point2D(30.0D, 30.0D)));
		assertTrue(rectangle2D.contains(new Point2D(25.0D, 30.0D)));
		assertTrue(rectangle2D.contains(new Point2D(20.0D, 30.0D)));
		assertTrue(rectangle2D.contains(new Point2D(20.0D, 25.0D)));
		assertTrue(rectangle2D.contains(new Point2D(25.0D, 25.0D)));
		
		assertFalse(rectangle2D.contains(new Point2D(19.0D, 20.0D)));
		assertFalse(rectangle2D.contains(new Point2D(31.0D, 20.0D)));
		assertFalse(rectangle2D.contains(new Point2D(19.0D, 30.0D)));
		assertFalse(rectangle2D.contains(new Point2D(31.0D, 30.0D)));
		assertFalse(rectangle2D.contains(new Point2D(20.0D, 19.0D)));
		assertFalse(rectangle2D.contains(new Point2D(20.0D, 31.0D)));
		assertFalse(rectangle2D.contains(new Point2D(30.0D, 19.0D)));
		assertFalse(rectangle2D.contains(new Point2D(30.0D, 31.0D)));
		
		assertThrows(NullPointerException.class, () -> rectangle2D.contains(null));
	}
	
	@Test
	public void testEquals() {
		final Rectangle2D a = new Rectangle2D(new Point2D(20.0D, 20.0D), new Point2D(30.0D, 20.0D), new Point2D(30.0D, 30.0D), new Point2D(20.0D, 30.0D));
		final Rectangle2D b = new Rectangle2D(new Point2D(20.0D, 20.0D), new Point2D(30.0D, 20.0D), new Point2D(30.0D, 30.0D), new Point2D(20.0D, 30.0D));
		final Rectangle2D c = new Rectangle2D(new Point2D(20.0D, 20.0D), new Point2D(40.0D, 20.0D), new Point2D(40.0D, 30.0D), new Point2D(20.0D, 30.0D));
		final Rectangle2D d = new Rectangle2D(new Point2D(10.0D, 20.0D), new Point2D(30.0D, 20.0D), new Point2D(30.0D, 30.0D), new Point2D(10.0D, 30.0D));
		final Rectangle2D e = new Rectangle2D(new Point2D(20.0D, 20.0D), new Point2D(30.0D, 20.0D), new Point2D(30.0D, 40.0D), new Point2D(20.0D, 40.0D));
		final Rectangle2D f = null;
		
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
	public void testFromPoints() {
		final Rectangle2D a = Rectangle2D.fromPoints(new Point2D(10.0D, 10.0D), new Point2D(15.0D, 15.0D), new Point2D(20.0D, 20.0D), new Point2D(25.0D, 25.0D), new Point2D(30.0D, 30.0D));
		final Rectangle2D b = new Rectangle2D(new Point2D(10.0D, 10.0D), new Point2D(30.0D, 30.0D));
		
		assertEquals(a, b);
		
		assertThrows(NullPointerException.class, () -> Rectangle2D.fromPoints((Point2D[])(null)));
		assertThrows(NullPointerException.class, () -> Rectangle2D.fromPoints(new Point2D(), null));
		
		assertThrows(IllegalArgumentException.class, () -> Rectangle2D.fromPoints());
	}
	
	@Test
	public void testGetA() {
		final Rectangle2D rectangle = new Rectangle2D(new Point2D(10.0D, 10.0D), new Point2D(10.0D, 30.0D), new Point2D(30.0D, 30.0D), new Point2D(30.0D, 10.0D));
		
		assertEquals(new Point2D(10.0D, 10.0D), rectangle.getA());
	}
	
	@Test
	public void testGetB() {
		final Rectangle2D rectangle = new Rectangle2D(new Point2D(10.0D, 10.0D), new Point2D(10.0D, 30.0D), new Point2D(30.0D, 30.0D), new Point2D(30.0D, 10.0D));
		
		assertEquals(new Point2D(10.0D, 30.0D), rectangle.getB());
	}
	
	@Test
	public void testGetC() {
		final Rectangle2D rectangle = new Rectangle2D(new Point2D(10.0D, 10.0D), new Point2D(10.0D, 30.0D), new Point2D(30.0D, 30.0D), new Point2D(30.0D, 10.0D));
		
		assertEquals(new Point2D(30.0D, 30.0D), rectangle.getC());
	}
	
	@Test
	public void testGetD() {
		final Rectangle2D rectangle = new Rectangle2D(new Point2D(10.0D, 10.0D), new Point2D(10.0D, 30.0D), new Point2D(30.0D, 30.0D), new Point2D(30.0D, 10.0D));
		
		assertEquals(new Point2D(30.0D, 10.0D), rectangle.getD());
	}
	
	@Test
	public void testGetID() {
		final Rectangle2D rectangle = new Rectangle2D(new Point2D(10.0D, 10.0D), new Point2D(20.0D, 20.0D));
		
		assertEquals(Rectangle2D.ID, rectangle.getID());
	}
	
	@Test
	public void testGetLineSegments() {
		final Rectangle2D rectangle = new Rectangle2D(new Point2D(10.0D, 10.0D), new Point2D(10.0D, 30.0D), new Point2D(30.0D, 30.0D), new Point2D(30.0D, 10.0D));
		
		final List<LineSegment2D> lineSegments = rectangle.getLineSegments();
		
		assertNotNull(lineSegments);
		
		assertTrue(lineSegments.size() == 4);
		
		assertNotNull(lineSegments.get(0));
		assertNotNull(lineSegments.get(1));
		assertNotNull(lineSegments.get(2));
		assertNotNull(lineSegments.get(3));
		
		assertEquals(new Point2D(10.0D, 10.0D), lineSegments.get(0).getA());
		assertEquals(new Point2D(10.0D, 30.0D), lineSegments.get(0).getB());
		
		assertEquals(new Point2D(10.0D, 30.0D), lineSegments.get(1).getA());
		assertEquals(new Point2D(30.0D, 30.0D), lineSegments.get(1).getB());
		
		assertEquals(new Point2D(30.0D, 30.0D), lineSegments.get(2).getA());
		assertEquals(new Point2D(30.0D, 10.0D), lineSegments.get(2).getB());
		
		assertEquals(new Point2D(30.0D, 10.0D), lineSegments.get(3).getA());
		assertEquals(new Point2D(10.0D, 10.0D), lineSegments.get(3).getB());
	}
	
	@Test
	public void testGetName() {
		final Rectangle2D rectangle = new Rectangle2D(new Point2D(10.0D, 10.0D), new Point2D(20.0D, 20.0D));
		
		assertEquals(Rectangle2D.NAME, rectangle.getName());
	}
	
	@Test
	public void testHashCode() {
		final Rectangle2D a = new Rectangle2D(new Point2D(10.0D, 10.0D), new Point2D(20.0D, 20.0D));
		final Rectangle2D b = new Rectangle2D(new Point2D(10.0D, 10.0D), new Point2D(20.0D, 20.0D));
		
		assertEquals(a.hashCode(), a.hashCode());
		assertEquals(a.hashCode(), b.hashCode());
	}
	
	@Test
	public void testIsAxisAligned() {
		final Rectangle2D a = new Rectangle2D(new Point2D(10.0D, 10.0D), new Point2D(20.0D, 10.0D), new Point2D(20.0D, 20.0D), new Point2D(10.0D, 20.0D));
		final Rectangle2D b = new Rectangle2D(new Point2D(20.0D, 20.0D), new Point2D(25.0D, 25.0D), new Point2D(20.0D, 30.0D), new Point2D(15.0D, 25.0D));
		
		assertTrue(a.isAxisAligned());
		
		assertFalse(b.isAxisAligned());
	}
	
	@Test
	public void testIsRotated() {
		final Rectangle2D a = new Rectangle2D(new Point2D(20.0D, 20.0D), new Point2D(25.0D, 25.0D), new Point2D(20.0D, 30.0D), new Point2D(15.0D, 25.0D));
		final Rectangle2D b = new Rectangle2D(new Point2D(10.0D, 10.0D), new Point2D(20.0D, 10.0D), new Point2D(20.0D, 20.0D), new Point2D(10.0D, 20.0D));
		
		assertTrue(a.isRotated());
		
		assertFalse(b.isRotated());
	}
	
	@Test
	public void testRotate() {
		final Rectangle2D a = new Rectangle2D(new Point2D(-10.0D, -10.0D), new Point2D(+10.0D, -10.0D), new Point2D(+10.0D, +10.0D), new Point2D(-10.0D, +10.0D));
		final Rectangle2D b = new Rectangle2D(new Point2D(+10.0D, -10.0D), new Point2D(+10.0D, +10.0D), new Point2D(-10.0D, +10.0D), new Point2D(-10.0D, -10.0D));
		final Rectangle2D c = Rectangle2D.rotate(a, AngleD.degrees(90.0D));
		
		assertEquals(b, c);
		
		assertThrows(NullPointerException.class, () -> Rectangle2D.rotate(a, null));
		assertThrows(NullPointerException.class, () -> Rectangle2D.rotate(null, AngleD.degrees(0.0D)));
	}
	
	@Test
	public void testToString() {
		final Rectangle2D rectangle = new Rectangle2D(new Point2D(10.0D, 10.0D), new Point2D(20.0D, 20.0D));
		
		assertEquals("new Rectangle2D(new Point2D(10.0D, 10.0D), new Point2D(20.0D, 10.0D), new Point2D(20.0D, 20.0D), new Point2D(10.0D, 20.0D))", rectangle.toString());
	}
	
	@Test
	public void testTranslate() {
		final Rectangle2D a = new Rectangle2D(new Point2D(10.0D, 10.0D), new Point2D(20.0D, 20.0D));
		final Rectangle2D b = new Rectangle2D(new Point2D(20.0D, 10.0D), new Point2D(30.0D, 20.0D));
		final Rectangle2D c = new Rectangle2D(new Point2D(10.0D, 20.0D), new Point2D(20.0D, 30.0D));
		final Rectangle2D d = Rectangle2D.translate(a, new Vector2D(10.0D,  0.0D));
		final Rectangle2D e = Rectangle2D.translate(a, new Vector2D( 0.0D, 10.0D));
		
		assertEquals(b, d);
		assertEquals(c, e);
		
		assertThrows(NullPointerException.class, () -> Rectangle2D.translate(a, null));
		assertThrows(NullPointerException.class, () -> Rectangle2D.translate(null, new Vector2D(1.0D, 1.0D)));
	}
	
	@Test
	public void testUnion() {
		final Rectangle2D a = new Rectangle2D(new Point2D(10.0D, 10.0D), new Point2D(20.0D, 20.0D));
		final Rectangle2D b = new Rectangle2D(new Point2D(20.0D, 20.0D), new Point2D(30.0D, 30.0D));
		final Rectangle2D c = new Rectangle2D(new Point2D(10.0D, 10.0D), new Point2D(30.0D, 30.0D));
		final Rectangle2D d = Rectangle2D.union(a, b);
		
		assertEquals(c, d);
		
		assertThrows(NullPointerException.class, () -> Rectangle2D.union(a, null));
		assertThrows(NullPointerException.class, () -> Rectangle2D.union(null, b));
	}
	
	@Test
	public void testWrite() {
		final Rectangle2D a = new Rectangle2D(new Point2D(10.0D, 10.0D), new Point2D(20.0D, 20.0D));
		
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		
		final DataOutput dataOutput = new DataOutputStream(byteArrayOutputStream);
		
		a.write(dataOutput);
		
		final byte[] bytes = byteArrayOutputStream.toByteArray();
		
		final Rectangle2D b = new Rectangle2DReader().read(new DataInputStream(new ByteArrayInputStream(bytes)));
		
		assertEquals(a, b);
		
		assertThrows(NullPointerException.class, () -> a.write((DataOutput)(null)));
		assertThrows(UncheckedIOException.class, () -> a.write(new DataOutputMock()));
	}
}