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

import org.dayflower.geometry.Point2F;
import org.dayflower.geometry.Shape2F;
import org.dayflower.mock.DataOutputMock;
import org.dayflower.mock.NodeHierarchicalVisitorMock;
import org.dayflower.mock.NodeVisitorMock;

import org.junit.jupiter.api.Test;

import org.macroing.java.util.visitor.NodeHierarchicalVisitor;
import org.macroing.java.util.visitor.NodeTraversalException;
import org.macroing.java.util.visitor.NodeVisitor;

@SuppressWarnings("static-method")
public final class Circle2FUnitTests {
	public Circle2FUnitTests() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Test
	public void testAcceptNodeHierarchicalVisitor() {
		final Point2F center = new Point2F(10.0F, 10.0F);
		
		final Circle2F circle = new Circle2F(center, 20.0F);
		
		assertTrue(circle.accept(new NodeHierarchicalVisitorMock(node -> false,                                      node -> node.equals(circle))));
		assertTrue(circle.accept(new NodeHierarchicalVisitorMock(node -> node.equals(circle),                        node -> node.equals(circle))));
		assertTrue(circle.accept(new NodeHierarchicalVisitorMock(node -> node.equals(circle) || node.equals(center), node -> node.equals(circle) || node.equals(center))));
		
		assertThrows(NodeTraversalException.class, () -> circle.accept(new NodeHierarchicalVisitorMock(null, null)));
		assertThrows(NullPointerException.class, () -> circle.accept((NodeHierarchicalVisitor)(null)));
	}
	
	@Test
	public void testAcceptNodeVisitor() {
		final Circle2F circle = new Circle2F();
		
		assertThrows(NodeTraversalException.class, () -> circle.accept(new NodeVisitorMock(true)));
		assertThrows(NullPointerException.class, () -> circle.accept((NodeVisitor)(null)));
	}
	
	@Test
	public void testConstants() {
		assertEquals(1, Circle2F.ID);
		assertEquals("Circle", Circle2F.NAME);
	}
	
	@Test
	public void testConstructor() {
		final Circle2F circle = new Circle2F();
		
		assertEquals(new Point2F(), circle.getCenter());
		assertEquals(10.0F, circle.getRadius());
	}
	
	@Test
	public void testConstructorPoint2F() {
		final Circle2F circle = new Circle2F(new Point2F(10.0F, 10.0F));
		
		assertEquals(new Point2F(10.0F, 10.0F), circle.getCenter());
		assertEquals(10.0F, circle.getRadius());
		
		assertThrows(NullPointerException.class, () -> new Circle2F((Point2F)(null)));
	}
	
	@Test
	public void testConstructorPoint2FFloat() {
		final Circle2F circle = new Circle2F(new Point2F(10.0F, 10.0F), 20.0F);
		
		assertEquals(new Point2F(10.0F, 10.0F), circle.getCenter());
		assertEquals(20.0F, circle.getRadius());
		
		assertThrows(NullPointerException.class, () -> new Circle2F(null, 20.0F));
	}
	
	@Test
	public void testConstructorRectangle2F() {
		final Circle2F a = new Circle2F(new Rectangle2F(new Point2F(10.0F, 10.0F), new Point2F(20.0F, 10.0F), new Point2F(20.0F, 20.0F), new Point2F(10.0F, 20.0F)));
		final Circle2F b = new Circle2F(new Rectangle2F(new Point2F(10.0F, 10.0F), new Point2F(30.0F, 10.0F), new Point2F(30.0F, 20.0F), new Point2F(10.0F, 20.0F)));
		final Circle2F c = new Circle2F(new Rectangle2F(new Point2F(10.0F, 10.0F), new Point2F(20.0F, 10.0F), new Point2F(20.0F, 30.0F), new Point2F(10.0F, 30.0F)));
		
		assertEquals(new Point2F(15.0F, 15.0F), a.getCenter());
		assertEquals(new Point2F(20.0F, 15.0F), b.getCenter());
		assertEquals(new Point2F(15.0F, 20.0F), c.getCenter());
		
		assertEquals( 7.0710678118654755F, a.getRadius());
		assertEquals(11.180339887498949F,  b.getRadius());
		assertEquals(11.180339887498949F,  c.getRadius());
		
		assertThrows(NullPointerException.class, () -> new Circle2F((Rectangle2F)(null)));
	}
	
	@Test
	public void testContains() {
		final Circle2F circle = new Circle2F(new Point2F(0.0F, 0.0F), 10.0F);
		
		assertTrue(circle.contains(new Point2F(+10.0F, 0.0F)));
		assertTrue(circle.contains(new Point2F(-10.0F, 0.0F)));
		
		assertTrue(circle.contains(new Point2F(0.0F, +10.0F)));
		assertTrue(circle.contains(new Point2F(0.0F, -10.0F)));
		
		assertFalse(circle.contains(new Point2F(10.0F, 10.0F)));
		
		assertThrows(NullPointerException.class, () -> circle.contains(null));
	}
	
	@Test
	public void testContainsDifference() {
		final Circle2F a = new Circle2F(new Point2F(10.0F, 10.0F), 10.0F);
		final Circle2F b = new Circle2F(new Point2F(30.0F, 10.0F), 10.0F);
		
		assertTrue(Shape2F.containsDifference(new Point2F(10.0F, 10.0F), a, b));
		
		assertFalse(Shape2F.containsDifference(new Point2F(20.0F, 10.0F), a, b));
		assertFalse(Shape2F.containsDifference(new Point2F(30.0F, 10.0F), a, b));
		assertFalse(Shape2F.containsDifference(new Point2F(50.0F, 10.0F), a, b));
		
		assertThrows(NullPointerException.class, () -> Shape2F.containsDifference(new Point2F(), a, null));
		assertThrows(NullPointerException.class, () -> Shape2F.containsDifference(new Point2F(), null, b));
		assertThrows(NullPointerException.class, () -> Shape2F.containsDifference(null, a, b));
	}
	
	@Test
	public void testContainsIntersection() {
		final Circle2F a = new Circle2F(new Point2F(10.0F, 10.0F), 10.0F);
		final Circle2F b = new Circle2F(new Point2F(20.0F, 10.0F), 10.0F);
		
		assertTrue(Shape2F.containsIntersection(new Point2F(15.0F, 10.0F), a, b));
		
		assertFalse(Shape2F.containsIntersection(new Point2F( 0.0F, 10.0F), a, b));
		assertFalse(Shape2F.containsIntersection(new Point2F(30.0F, 10.0F), a, b));
		assertFalse(Shape2F.containsIntersection(new Point2F(50.0F, 10.0F), a, b));
		
		assertThrows(NullPointerException.class, () -> Shape2F.containsIntersection(new Point2F(), a, null));
		assertThrows(NullPointerException.class, () -> Shape2F.containsIntersection(new Point2F(), null, b));
		assertThrows(NullPointerException.class, () -> Shape2F.containsIntersection(null, a, b));
	}
	
	@Test
	public void testContainsUnion() {
		final Circle2F a = new Circle2F(new Point2F(10.0F, 10.0F), 10.0F);
		final Circle2F b = new Circle2F(new Point2F(20.0F, 10.0F), 10.0F);
		
		assertTrue(Shape2F.containsUnion(new Point2F( 0.0F, 10.0F), a, b));
		assertTrue(Shape2F.containsUnion(new Point2F(15.0F, 10.0F), a, b));
		assertTrue(Shape2F.containsUnion(new Point2F(30.0F, 10.0F), a, b));
		
		assertFalse(Shape2F.containsUnion(new Point2F(50.0F, 10.0F), a, b));
		
		assertThrows(NullPointerException.class, () -> Shape2F.containsUnion(new Point2F(), a, null));
		assertThrows(NullPointerException.class, () -> Shape2F.containsUnion(new Point2F(), null, b));
		assertThrows(NullPointerException.class, () -> Shape2F.containsUnion(null, a, b));
	}
	
	@Test
	public void testEquals() {
		final Circle2F a = new Circle2F(new Point2F(10.0F, 10.0F), 20.0F);
		final Circle2F b = new Circle2F(new Point2F(10.0F, 10.0F), 20.0F);
		final Circle2F c = new Circle2F(new Point2F(10.0F, 10.0F), 30.0F);
		final Circle2F d = new Circle2F(new Point2F(20.0F, 20.0F), 20.0F);
		final Circle2F e = null;
		
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
		final Circle2F circle = new Circle2F(new Point2F(10.0F, 10.0F));
		
		assertEquals(new Point2F(10.0F, 10.0F), circle.getCenter());
	}
	
	@Test
	public void testGetID() {
		final Circle2F circle = new Circle2F();
		
		assertEquals(Circle2F.ID, circle.getID());
	}
	
	@Test
	public void testGetName() {
		final Circle2F circle = new Circle2F();
		
		assertEquals(Circle2F.NAME, circle.getName());
	}
	
	@Test
	public void testGetRadius() {
		final Circle2F circle = new Circle2F(new Point2F(), 20.0F);
		
		assertEquals(20.0F, circle.getRadius());
	}
	
	@Test
	public void testHashCode() {
		final Circle2F a = new Circle2F();
		final Circle2F b = new Circle2F();
		
		assertEquals(a.hashCode(), a.hashCode());
		assertEquals(a.hashCode(), b.hashCode());
	}
	
	@Test
	public void testToString() {
		final Circle2F circle = new Circle2F(new Point2F(10.0F, 10.0F), 20.0F);
		
		assertEquals("new Circle2F(new Point2F(10.0F, 10.0F), 20.0F)", circle.toString());
	}
	
	@Test
	public void testWrite() {
		final Circle2F a = new Circle2F(new Point2F(10.0F, 10.0F), 20.0F);
		
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		
		final DataOutput dataOutput = new DataOutputStream(byteArrayOutputStream);
		
		a.write(dataOutput);
		
		final byte[] bytes = byteArrayOutputStream.toByteArray();
		
		final Circle2F b = new Circle2FReader().read(new DataInputStream(new ByteArrayInputStream(bytes)));
		
		assertEquals(a, b);
		
		assertThrows(NullPointerException.class, () -> a.write((DataOutput)(null)));
		assertThrows(UncheckedIOException.class, () -> a.write(new DataOutputMock()));
	}
}