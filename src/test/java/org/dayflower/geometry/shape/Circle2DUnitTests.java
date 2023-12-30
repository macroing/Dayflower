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
package org.dayflower.geometry.shape;

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

import org.dayflower.geometry.Point2D;
import org.dayflower.geometry.Shape2D;
import org.dayflower.mock.DataOutputMock;
import org.dayflower.mock.NodeHierarchicalVisitorMock;
import org.dayflower.mock.NodeVisitorMock;

import org.junit.jupiter.api.Test;

import org.macroing.java.util.visitor.NodeHierarchicalVisitor;
import org.macroing.java.util.visitor.NodeTraversalException;
import org.macroing.java.util.visitor.NodeVisitor;

@SuppressWarnings("static-method")
public final class Circle2DUnitTests {
	public Circle2DUnitTests() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Test
	public void testAcceptNodeHierarchicalVisitor() {
		final Point2D center = new Point2D(10.0D, 10.0D);
		
		final Circle2D circle = new Circle2D(center, 20.0D);
		
		assertTrue(circle.accept(new NodeHierarchicalVisitorMock(node -> false,                                      node -> node.equals(circle))));
		assertTrue(circle.accept(new NodeHierarchicalVisitorMock(node -> node.equals(circle),                        node -> node.equals(circle))));
		assertTrue(circle.accept(new NodeHierarchicalVisitorMock(node -> node.equals(circle) || node.equals(center), node -> node.equals(circle) || node.equals(center))));
		
		assertThrows(NodeTraversalException.class, () -> circle.accept(new NodeHierarchicalVisitorMock(null, null)));
		assertThrows(NullPointerException.class, () -> circle.accept((NodeHierarchicalVisitor)(null)));
	}
	
	@Test
	public void testAcceptNodeVisitor() {
		final Circle2D circle = new Circle2D();
		
		assertThrows(NodeTraversalException.class, () -> circle.accept(new NodeVisitorMock(true)));
		assertThrows(NullPointerException.class, () -> circle.accept((NodeVisitor)(null)));
	}
	
	@Test
	public void testConstants() {
		assertEquals(1, Circle2D.ID);
		assertEquals("Circle", Circle2D.NAME);
	}
	
	@Test
	public void testConstructor() {
		final Circle2D circle = new Circle2D();
		
		assertEquals(new Point2D(), circle.getCenter());
		assertEquals(10.0D, circle.getRadius());
	}
	
	@Test
	public void testConstructorPoint2D() {
		final Circle2D circle = new Circle2D(new Point2D(10.0D, 10.0D));
		
		assertEquals(new Point2D(10.0D, 10.0D), circle.getCenter());
		assertEquals(10.0D, circle.getRadius());
		
		assertThrows(NullPointerException.class, () -> new Circle2D((Point2D)(null)));
	}
	
	@Test
	public void testConstructorPoint2DDouble() {
		final Circle2D circle = new Circle2D(new Point2D(10.0D, 10.0D), 20.0D);
		
		assertEquals(new Point2D(10.0D, 10.0D), circle.getCenter());
		assertEquals(20.0D, circle.getRadius());
		
		assertThrows(NullPointerException.class, () -> new Circle2D(null, 20.0D));
	}
	
	@Test
	public void testConstructorRectangle2D() {
		final Circle2D a = new Circle2D(new Rectangle2D(new Point2D(10.0D, 10.0D), new Point2D(20.0D, 10.0D), new Point2D(20.0D, 20.0D), new Point2D(10.0D, 20.0D)));
		final Circle2D b = new Circle2D(new Rectangle2D(new Point2D(10.0D, 10.0D), new Point2D(30.0D, 10.0D), new Point2D(30.0D, 20.0D), new Point2D(10.0D, 20.0D)));
		final Circle2D c = new Circle2D(new Rectangle2D(new Point2D(10.0D, 10.0D), new Point2D(20.0D, 10.0D), new Point2D(20.0D, 30.0D), new Point2D(10.0D, 30.0D)));
		
		assertEquals(new Point2D(15.0D, 15.0D), a.getCenter());
		assertEquals(new Point2D(20.0D, 15.0D), b.getCenter());
		assertEquals(new Point2D(15.0D, 20.0D), c.getCenter());
		
		assertEquals( 7.0710678118654755D, a.getRadius());
		assertEquals(11.180339887498949D,  b.getRadius());
		assertEquals(11.180339887498949D,  c.getRadius());
		
		assertThrows(NullPointerException.class, () -> new Circle2D((Rectangle2D)(null)));
	}
	
	@Test
	public void testContains() {
		final Circle2D circle = new Circle2D(new Point2D(0.0D, 0.0D), 10.0D);
		
		assertTrue(circle.contains(new Point2D(+10.0D, 0.0D)));
		assertTrue(circle.contains(new Point2D(-10.0D, 0.0D)));
		
		assertTrue(circle.contains(new Point2D(0.0D, +10.0D)));
		assertTrue(circle.contains(new Point2D(0.0D, -10.0D)));
		
		assertFalse(circle.contains(new Point2D(10.0D, 10.0D)));
		
		assertThrows(NullPointerException.class, () -> circle.contains(null));
	}
	
	@Test
	public void testContainsDifference() {
		final Circle2D a = new Circle2D(new Point2D(10.0D, 10.0D), 10.0D);
		final Circle2D b = new Circle2D(new Point2D(30.0D, 10.0D), 10.0D);
		
		assertTrue(Shape2D.containsDifference(new Point2D(10.0D, 10.0D), a, b));
		
		assertFalse(Shape2D.containsDifference(new Point2D(20.0D, 10.0D), a, b));
		assertFalse(Shape2D.containsDifference(new Point2D(30.0D, 10.0D), a, b));
		assertFalse(Shape2D.containsDifference(new Point2D(50.0D, 10.0D), a, b));
		
		assertThrows(NullPointerException.class, () -> Shape2D.containsDifference(new Point2D(), a, null));
		assertThrows(NullPointerException.class, () -> Shape2D.containsDifference(new Point2D(), null, b));
		assertThrows(NullPointerException.class, () -> Shape2D.containsDifference(null, a, b));
	}
	
	@Test
	public void testContainsIntersection() {
		final Circle2D a = new Circle2D(new Point2D(10.0D, 10.0D), 10.0D);
		final Circle2D b = new Circle2D(new Point2D(20.0D, 10.0D), 10.0D);
		
		assertTrue(Shape2D.containsIntersection(new Point2D(15.0D, 10.0D), a, b));
		
		assertFalse(Shape2D.containsIntersection(new Point2D( 0.0D, 10.0D), a, b));
		assertFalse(Shape2D.containsIntersection(new Point2D(30.0D, 10.0D), a, b));
		assertFalse(Shape2D.containsIntersection(new Point2D(50.0D, 10.0D), a, b));
		
		assertThrows(NullPointerException.class, () -> Shape2D.containsIntersection(new Point2D(), a, null));
		assertThrows(NullPointerException.class, () -> Shape2D.containsIntersection(new Point2D(), null, b));
		assertThrows(NullPointerException.class, () -> Shape2D.containsIntersection(null, a, b));
	}
	
	@Test
	public void testContainsUnion() {
		final Circle2D a = new Circle2D(new Point2D(10.0D, 10.0D), 10.0D);
		final Circle2D b = new Circle2D(new Point2D(20.0D, 10.0D), 10.0D);
		
		assertTrue(Shape2D.containsUnion(new Point2D( 0.0D, 10.0D), a, b));
		assertTrue(Shape2D.containsUnion(new Point2D(15.0D, 10.0D), a, b));
		assertTrue(Shape2D.containsUnion(new Point2D(30.0D, 10.0D), a, b));
		
		assertFalse(Shape2D.containsUnion(new Point2D(50.0D, 10.0D), a, b));
		
		assertThrows(NullPointerException.class, () -> Shape2D.containsUnion(new Point2D(), a, null));
		assertThrows(NullPointerException.class, () -> Shape2D.containsUnion(new Point2D(), null, b));
		assertThrows(NullPointerException.class, () -> Shape2D.containsUnion(null, a, b));
	}
	
	@Test
	public void testEquals() {
		final Circle2D a = new Circle2D(new Point2D(10.0D, 10.0D), 20.0D);
		final Circle2D b = new Circle2D(new Point2D(10.0D, 10.0D), 20.0D);
		final Circle2D c = new Circle2D(new Point2D(10.0D, 10.0D), 30.0D);
		final Circle2D d = new Circle2D(new Point2D(20.0D, 20.0D), 20.0D);
		final Circle2D e = null;
		
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
		final Circle2D circle = new Circle2D(new Point2D(10.0D, 10.0D));
		
		assertEquals(new Point2D(10.0D, 10.0D), circle.getCenter());
	}
	
	@Test
	public void testGetID() {
		final Circle2D circle = new Circle2D();
		
		assertEquals(Circle2D.ID, circle.getID());
	}
	
	@Test
	public void testGetName() {
		final Circle2D circle = new Circle2D();
		
		assertEquals(Circle2D.NAME, circle.getName());
	}
	
	@Test
	public void testGetRadius() {
		final Circle2D circle = new Circle2D(new Point2D(), 20.0D);
		
		assertEquals(20.0D, circle.getRadius());
	}
	
	@Test
	public void testHashCode() {
		final Circle2D a = new Circle2D();
		final Circle2D b = new Circle2D();
		
		assertEquals(a.hashCode(), a.hashCode());
		assertEquals(a.hashCode(), b.hashCode());
	}
	
	@Test
	public void testToString() {
		final Circle2D circle = new Circle2D(new Point2D(10.0D, 10.0D), 20.0D);
		
		assertEquals("new Circle2D(new Point2D(10.0D, 10.0D), 20.0D)", circle.toString());
	}
	
	@Test
	public void testWrite() {
		final Circle2D a = new Circle2D(new Point2D(10.0D, 10.0D), 20.0D);
		
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		
		final DataOutput dataOutput = new DataOutputStream(byteArrayOutputStream);
		
		a.write(dataOutput);
		
		final byte[] bytes = byteArrayOutputStream.toByteArray();
		
		final Circle2D b = new Circle2DReader().read(new DataInputStream(new ByteArrayInputStream(bytes)));
		
		assertEquals(a, b);
		
		assertThrows(NullPointerException.class, () -> a.write((DataOutput)(null)));
		assertThrows(UncheckedIOException.class, () -> a.write(new DataOutputMock()));
	}
}