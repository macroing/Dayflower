/**
 * Copyright 2014 - 2022 J&#246;rgen Lundgren
 * 
 * This file is part of Fayflower.
 * 
 * Fayflower is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Fayflower is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with Fayflower. If not, see <http://www.gnu.org/licenses/>.
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

import org.dayflower.geometry.AngleF;
import org.dayflower.geometry.Point2F;
import org.dayflower.geometry.Vector2F;
import org.dayflower.mock.DataOutputMock;
import org.dayflower.mock.NodeHierarchicalVisitorMock;
import org.dayflower.mock.NodeVisitorMock;
import org.dayflower.node.NodeHierarchicalVisitor;
import org.dayflower.node.NodeTraversalException;
import org.dayflower.node.NodeVisitor;
import org.junit.jupiter.api.Test;

@SuppressWarnings("static-method")
public final class Rectangle2FUnitTests {
	public Rectangle2FUnitTests() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Test
	public void testAcceptNodeHierarchicalVisitor() {
		final Point2F a = new Point2F(10.0F, 10.0F);
		final Point2F b = new Point2F(20.0F, 10.0F);
		final Point2F c = new Point2F(20.0F, 20.0F);
		final Point2F d = new Point2F(10.0F, 20.0F);
		
		final Rectangle2F rectangle = new Rectangle2F(a, b, c, d);
		
		final List<LineSegment2F> lineSegments = rectangle.getLineSegments();
		
		final LineSegment2F lineSegment0 = lineSegments.get(0);
		final LineSegment2F lineSegment1 = lineSegments.get(1);
		final LineSegment2F lineSegment2 = lineSegments.get(2);
		final LineSegment2F lineSegment3 = lineSegments.get(3);
		
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
		final Rectangle2F rectangle = new Rectangle2F(new Point2F(10.0F, 10.0F), new Point2F(20.0F, 20.0F));
		
		assertThrows(NodeTraversalException.class, () -> rectangle.accept(new NodeVisitorMock(true)));
		assertThrows(NullPointerException.class, () -> rectangle.accept((NodeVisitor)(null)));
	}
	
	@Test
	public void testConstants() {
		assertEquals(4, Rectangle2F.ID);
		assertEquals("Rectangle", Rectangle2F.NAME);
	}
	
	@Test
	public void testConstructorCircle2F() {
		final Rectangle2F rectangle = new Rectangle2F(new Circle2F(new Point2F(20.0F, 20.0F), 10.0F));
		
		final List<LineSegment2F> lineSegments = rectangle.getLineSegments();
		
		assertEquals(new Point2F(10.0F, 10.0F), rectangle.getA());
		assertEquals(new Point2F(10.0F, 30.0F), rectangle.getB());
		assertEquals(new Point2F(30.0F, 30.0F), rectangle.getC());
		assertEquals(new Point2F(30.0F, 10.0F), rectangle.getD());
		
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
		
		assertThrows(NullPointerException.class, () -> new Rectangle2F((Circle2F)(null)));
	}
	
	@Test
	public void testConstructorPoint2FPoint2F() {
		final Rectangle2F rectangle = new Rectangle2F(new Point2F(30.0F, 30.0F), new Point2F(10.0F, 10.0F));
		
		final List<LineSegment2F> lineSegments = rectangle.getLineSegments();
		
		assertEquals(new Point2F(10.0F, 10.0F), rectangle.getA());
		assertEquals(new Point2F(10.0F, 30.0F), rectangle.getB());
		assertEquals(new Point2F(30.0F, 30.0F), rectangle.getC());
		assertEquals(new Point2F(30.0F, 10.0F), rectangle.getD());
		
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
		
		assertThrows(NullPointerException.class, () -> new Rectangle2F(new Point2F(), null));
		assertThrows(NullPointerException.class, () -> new Rectangle2F(null, new Point2F()));
	}
	
	@Test
	public void testConstructorPoint2FPoint2FPoint2FPoint2F() {
		final Rectangle2F rectangle = new Rectangle2F(new Point2F(10.0F, 10.0F), new Point2F(10.0F, 30.0F), new Point2F(30.0F, 30.0F), new Point2F(30.0F, 10.0F));
		
		final List<LineSegment2F> lineSegments = rectangle.getLineSegments();
		
		assertEquals(new Point2F(10.0F, 10.0F), rectangle.getA());
		assertEquals(new Point2F(10.0F, 30.0F), rectangle.getB());
		assertEquals(new Point2F(30.0F, 30.0F), rectangle.getC());
		assertEquals(new Point2F(30.0F, 10.0F), rectangle.getD());
		
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
		
		assertThrows(NullPointerException.class, () -> new Rectangle2F(new Point2F(), new Point2F(), new Point2F(), null));
		assertThrows(NullPointerException.class, () -> new Rectangle2F(new Point2F(), new Point2F(), null, new Point2F()));
		assertThrows(NullPointerException.class, () -> new Rectangle2F(new Point2F(), null, new Point2F(), new Point2F()));
		assertThrows(NullPointerException.class, () -> new Rectangle2F(null, new Point2F(), new Point2F(), new Point2F()));
		
		assertThrows(IllegalArgumentException.class, () -> new Rectangle2F(new Point2F(10.0F, 10.0F), new Point2F(10.0F, 20.0F), new Point2F(10.0F, 40.0F), new Point2F(10.0F, 80.0F)));
		assertThrows(IllegalArgumentException.class, () -> new Rectangle2F(new Point2F(10.0F, 10.0F), new Point2F(20.0F, 10.0F), new Point2F(40.0F, 10.0F), new Point2F(80.0F, 10.0F)));
	}
	
	@Test
	public void testContainsPoint2F() {
		final Rectangle2F rectangle2F = new Rectangle2F(new Point2F(20.0F, 20.0F), new Point2F(30.0F, 30.0F));
		
		assertTrue(rectangle2F.contains(new Point2F(20.0F, 20.0F)));
		assertTrue(rectangle2F.contains(new Point2F(25.0F, 20.0F)));
		assertTrue(rectangle2F.contains(new Point2F(30.0F, 20.0F)));
		assertTrue(rectangle2F.contains(new Point2F(30.0F, 25.0F)));
		assertTrue(rectangle2F.contains(new Point2F(30.0F, 30.0F)));
		assertTrue(rectangle2F.contains(new Point2F(25.0F, 30.0F)));
		assertTrue(rectangle2F.contains(new Point2F(20.0F, 30.0F)));
		assertTrue(rectangle2F.contains(new Point2F(20.0F, 25.0F)));
		assertTrue(rectangle2F.contains(new Point2F(25.0F, 25.0F)));
		
		assertFalse(rectangle2F.contains(new Point2F(19.0F, 20.0F)));
		assertFalse(rectangle2F.contains(new Point2F(31.0F, 20.0F)));
		assertFalse(rectangle2F.contains(new Point2F(19.0F, 30.0F)));
		assertFalse(rectangle2F.contains(new Point2F(31.0F, 30.0F)));
		assertFalse(rectangle2F.contains(new Point2F(20.0F, 19.0F)));
		assertFalse(rectangle2F.contains(new Point2F(20.0F, 31.0F)));
		assertFalse(rectangle2F.contains(new Point2F(30.0F, 19.0F)));
		assertFalse(rectangle2F.contains(new Point2F(30.0F, 31.0F)));
		
		assertThrows(NullPointerException.class, () -> rectangle2F.contains(null));
	}
	
	@Test
	public void testEquals() {
		final Rectangle2F a = new Rectangle2F(new Point2F(20.0F, 20.0F), new Point2F(30.0F, 20.0F), new Point2F(30.0F, 30.0F), new Point2F(20.0F, 30.0F));
		final Rectangle2F b = new Rectangle2F(new Point2F(20.0F, 20.0F), new Point2F(30.0F, 20.0F), new Point2F(30.0F, 30.0F), new Point2F(20.0F, 30.0F));
		final Rectangle2F c = new Rectangle2F(new Point2F(20.0F, 20.0F), new Point2F(40.0F, 20.0F), new Point2F(40.0F, 30.0F), new Point2F(20.0F, 30.0F));
		final Rectangle2F d = new Rectangle2F(new Point2F(10.0F, 20.0F), new Point2F(30.0F, 20.0F), new Point2F(30.0F, 30.0F), new Point2F(10.0F, 30.0F));
		final Rectangle2F e = new Rectangle2F(new Point2F(20.0F, 20.0F), new Point2F(30.0F, 20.0F), new Point2F(30.0F, 40.0F), new Point2F(20.0F, 40.0F));
		final Rectangle2F f = null;
		
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
		final Rectangle2F a = Rectangle2F.fromPoints(new Point2F(10.0F, 10.0F), new Point2F(15.0F, 15.0F), new Point2F(20.0F, 20.0F), new Point2F(25.0F, 25.0F), new Point2F(30.0F, 30.0F));
		final Rectangle2F b = new Rectangle2F(new Point2F(10.0F, 10.0F), new Point2F(30.0F, 30.0F));
		
		assertEquals(a, b);
		
		assertThrows(NullPointerException.class, () -> Rectangle2F.fromPoints((Point2F[])(null)));
		assertThrows(NullPointerException.class, () -> Rectangle2F.fromPoints(new Point2F(), null));
		
		assertThrows(IllegalArgumentException.class, () -> Rectangle2F.fromPoints());
	}
	
	@Test
	public void testGetA() {
		final Rectangle2F rectangle = new Rectangle2F(new Point2F(10.0F, 10.0F), new Point2F(10.0F, 30.0F), new Point2F(30.0F, 30.0F), new Point2F(30.0F, 10.0F));
		
		assertEquals(new Point2F(10.0F, 10.0F), rectangle.getA());
	}
	
	@Test
	public void testGetB() {
		final Rectangle2F rectangle = new Rectangle2F(new Point2F(10.0F, 10.0F), new Point2F(10.0F, 30.0F), new Point2F(30.0F, 30.0F), new Point2F(30.0F, 10.0F));
		
		assertEquals(new Point2F(10.0F, 30.0F), rectangle.getB());
	}
	
	@Test
	public void testGetC() {
		final Rectangle2F rectangle = new Rectangle2F(new Point2F(10.0F, 10.0F), new Point2F(10.0F, 30.0F), new Point2F(30.0F, 30.0F), new Point2F(30.0F, 10.0F));
		
		assertEquals(new Point2F(30.0F, 30.0F), rectangle.getC());
	}
	
	@Test
	public void testGetD() {
		final Rectangle2F rectangle = new Rectangle2F(new Point2F(10.0F, 10.0F), new Point2F(10.0F, 30.0F), new Point2F(30.0F, 30.0F), new Point2F(30.0F, 10.0F));
		
		assertEquals(new Point2F(30.0F, 10.0F), rectangle.getD());
	}
	
	@Test
	public void testGetID() {
		final Rectangle2F rectangle = new Rectangle2F(new Point2F(10.0F, 10.0F), new Point2F(20.0F, 20.0F));
		
		assertEquals(Rectangle2F.ID, rectangle.getID());
	}
	
	@Test
	public void testGetLineSegments() {
		final Rectangle2F rectangle = new Rectangle2F(new Point2F(10.0F, 10.0F), new Point2F(10.0F, 30.0F), new Point2F(30.0F, 30.0F), new Point2F(30.0F, 10.0F));
		
		final List<LineSegment2F> lineSegments = rectangle.getLineSegments();
		
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
		final Rectangle2F rectangle = new Rectangle2F(new Point2F(10.0F, 10.0F), new Point2F(20.0F, 20.0F));
		
		assertEquals(Rectangle2F.NAME, rectangle.getName());
	}
	
	@Test
	public void testHashCode() {
		final Rectangle2F a = new Rectangle2F(new Point2F(10.0F, 10.0F), new Point2F(20.0F, 20.0F));
		final Rectangle2F b = new Rectangle2F(new Point2F(10.0F, 10.0F), new Point2F(20.0F, 20.0F));
		
		assertEquals(a.hashCode(), a.hashCode());
		assertEquals(a.hashCode(), b.hashCode());
	}
	
	@Test
	public void testIsAxisAligned() {
		final Rectangle2F a = new Rectangle2F(new Point2F(10.0F, 10.0F), new Point2F(20.0F, 10.0F), new Point2F(20.0F, 20.0F), new Point2F(10.0F, 20.0F));
		final Rectangle2F b = new Rectangle2F(new Point2F(20.0F, 20.0F), new Point2F(25.0F, 25.0F), new Point2F(20.0F, 30.0F), new Point2F(15.0F, 25.0F));
		
		assertTrue(a.isAxisAligned());
		
		assertFalse(b.isAxisAligned());
	}
	
	@Test
	public void testIsRotated() {
		final Rectangle2F a = new Rectangle2F(new Point2F(20.0F, 20.0F), new Point2F(25.0F, 25.0F), new Point2F(20.0F, 30.0F), new Point2F(15.0F, 25.0F));
		final Rectangle2F b = new Rectangle2F(new Point2F(10.0F, 10.0F), new Point2F(20.0F, 10.0F), new Point2F(20.0F, 20.0F), new Point2F(10.0F, 20.0F));
		
		assertTrue(a.isRotated());
		
		assertFalse(b.isRotated());
	}
	
	@Test
	public void testRotate() {
		final Rectangle2F a = new Rectangle2F(new Point2F(-10.0F, -10.0F), new Point2F(+10.0F, -10.0F), new Point2F(+10.0F, +10.0F), new Point2F(-10.0F, +10.0F));
		final Rectangle2F b = new Rectangle2F(new Point2F(+10.0F, -10.0F), new Point2F(+10.0F, +10.0F), new Point2F(-10.0F, +10.0F), new Point2F(-10.0F, -10.0F));
		final Rectangle2F c = Rectangle2F.rotate(a, AngleF.degrees(90.0F));
		
		assertEquals(b, c);
		
		assertThrows(NullPointerException.class, () -> Rectangle2F.rotate(a, null));
		assertThrows(NullPointerException.class, () -> Rectangle2F.rotate(null, AngleF.degrees(0.0F)));
	}
	
	@Test
	public void testToString() {
		final Rectangle2F rectangle = new Rectangle2F(new Point2F(10.0F, 10.0F), new Point2F(20.0F, 20.0F));
		
		assertEquals("new Rectangle2F(new Point2F(10.0F, 10.0F), new Point2F(10.0F, 20.0F), new Point2F(20.0F, 20.0F), new Point2F(20.0F, 10.0F))", rectangle.toString());
	}
	
	@Test
	public void testTranslate() {
		final Rectangle2F a = new Rectangle2F(new Point2F(10.0F, 10.0F), new Point2F(20.0F, 20.0F));
		final Rectangle2F b = new Rectangle2F(new Point2F(20.0F, 10.0F), new Point2F(30.0F, 20.0F));
		final Rectangle2F c = new Rectangle2F(new Point2F(10.0F, 20.0F), new Point2F(20.0F, 30.0F));
		final Rectangle2F d = Rectangle2F.translate(a, new Vector2F(10.0F,  0.0F));
		final Rectangle2F e = Rectangle2F.translate(a, new Vector2F( 0.0F, 10.0F));
		
		assertEquals(b, d);
		assertEquals(c, e);
		
		assertThrows(NullPointerException.class, () -> Rectangle2F.translate(a, null));
		assertThrows(NullPointerException.class, () -> Rectangle2F.translate(null, new Vector2F(1.0F, 1.0F)));
	}
	
	@Test
	public void testUnion() {
		final Rectangle2F a = new Rectangle2F(new Point2F(10.0F, 10.0F), new Point2F(20.0F, 20.0F));
		final Rectangle2F b = new Rectangle2F(new Point2F(20.0F, 20.0F), new Point2F(30.0F, 30.0F));
		final Rectangle2F c = new Rectangle2F(new Point2F(10.0F, 10.0F), new Point2F(30.0F, 30.0F));
		final Rectangle2F d = Rectangle2F.union(a, b);
		
		assertEquals(c, d);
		
		assertThrows(NullPointerException.class, () -> Rectangle2F.union(a, null));
		assertThrows(NullPointerException.class, () -> Rectangle2F.union(null, b));
	}
	
	@Test
	public void testWrite() {
		final Rectangle2F a = new Rectangle2F(new Point2F(10.0F, 10.0F), new Point2F(20.0F, 20.0F));
		
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		
		final DataOutput dataOutput = new DataOutputStream(byteArrayOutputStream);
		
		a.write(dataOutput);
		
		final byte[] bytes = byteArrayOutputStream.toByteArray();
		
		final Rectangle2F b = new Rectangle2FReader().read(new DataInputStream(new ByteArrayInputStream(bytes)));
		
		assertEquals(a, b);
		
		assertThrows(NullPointerException.class, () -> a.write((DataOutput)(null)));
		assertThrows(UncheckedIOException.class, () -> a.write(new DataOutputMock()));
	}
}