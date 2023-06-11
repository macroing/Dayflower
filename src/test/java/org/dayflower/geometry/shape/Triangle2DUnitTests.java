/**
 * Copyright 20.0D14 - 20.0D22 J&#246;rgen Lundgren
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
import org.dayflower.mock.DataOutputMock;
import org.dayflower.mock.NodeHierarchicalVisitorMock;
import org.dayflower.mock.NodeVisitorMock;

import org.junit.jupiter.api.Test;

import org.macroing.java.util.visitor.NodeHierarchicalVisitor;
import org.macroing.java.util.visitor.NodeTraversalException;
import org.macroing.java.util.visitor.NodeVisitor;

@SuppressWarnings("static-method")
public final class Triangle2DUnitTests {
	public Triangle2DUnitTests() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Test
	public void testAcceptNodeHierarchicalVisitor() {
		final Point2D a = new Point2D(10.0D, 10.0D);
		final Point2D b = new Point2D(20.0D, 10.0D);
		final Point2D c = new Point2D(20.0D, 20.0D);
		
		final Triangle2D triangle = new Triangle2D(a, b, c);
		
		assertTrue(triangle.accept(new NodeHierarchicalVisitorMock(node -> false,                                                                       node -> node.equals(triangle))));
		assertTrue(triangle.accept(new NodeHierarchicalVisitorMock(node -> node.equals(triangle),                                                       node -> node.equals(triangle))));
		assertTrue(triangle.accept(new NodeHierarchicalVisitorMock(node -> node.equals(triangle) || node.equals(a),                                     node -> node.equals(triangle) || node.equals(a))));
		assertTrue(triangle.accept(new NodeHierarchicalVisitorMock(node -> node.equals(triangle) || node.equals(a) || node.equals(b),                   node -> node.equals(triangle) || node.equals(a) || node.equals(b))));
		assertTrue(triangle.accept(new NodeHierarchicalVisitorMock(node -> node.equals(triangle) || node.equals(a) || node.equals(b) || node.equals(c), node -> node.equals(triangle) || node.equals(a) || node.equals(b) || node.equals(c))));
		
		assertThrows(NodeTraversalException.class, () -> triangle.accept(new NodeHierarchicalVisitorMock(null, null)));
		assertThrows(NullPointerException.class, () -> triangle.accept((NodeHierarchicalVisitor)(null)));
	}
	
	@Test
	public void testAcceptNodeVisitor() {
		final Triangle2D triangle = new Triangle2D(new Point2D(10.0D, 10.0D), new Point2D(20.0D, 10.0D), new Point2D(20.0D, 20.0D));
		
		assertThrows(NodeTraversalException.class, () -> triangle.accept(new NodeVisitorMock(true)));
		assertThrows(NullPointerException.class, () -> triangle.accept((NodeVisitor)(null)));
	}
	
	@Test
	public void testConstants() {
		assertEquals(5, Triangle2D.ID);
		assertEquals("Triangle", Triangle2D.NAME);
	}
	
	@Test
	public void testConstructorPoint2DPoint2DPoint2D() {
		final Triangle2D triangle = new Triangle2D(new Point2D(10.0D, 10.0D), new Point2D(20.0D, 10.0D), new Point2D(20.0D, 20.0D));
		
		assertEquals(new Point2D(10.0D, 10.0D), triangle.getA());
		assertEquals(new Point2D(20.0D, 10.0D), triangle.getB());
		assertEquals(new Point2D(20.0D, 20.0D), triangle.getC());
		
		assertThrows(NullPointerException.class, () -> new Triangle2D(new Point2D(), new Point2D(), null));
		assertThrows(NullPointerException.class, () -> new Triangle2D(new Point2D(), null, new Point2D()));
		assertThrows(NullPointerException.class, () -> new Triangle2D(null, new Point2D(), new Point2D()));
	}
	
	@Test
	public void testContainsPoint2D() {
		final Triangle2D triangle = new Triangle2D(new Point2D(20.0D, 20.0D), new Point2D(30.0D, 20.0D), new Point2D(30.0D, 30.0D));
		
		assertTrue(triangle.contains(new Point2D(20.0D, 20.0D)));
		assertTrue(triangle.contains(new Point2D(25.0D, 20.0D)));
		assertTrue(triangle.contains(new Point2D(30.0D, 20.0D)));
		assertTrue(triangle.contains(new Point2D(30.0D, 25.0D)));
		assertTrue(triangle.contains(new Point2D(30.0D, 30.0D)));
		assertTrue(triangle.contains(new Point2D(25.0D, 25.0D)));
		assertTrue(triangle.contains(new Point2D(27.0D, 23.0D)));
		
		assertFalse(triangle.contains(new Point2D(19.0D, 20.0D)));
		assertFalse(triangle.contains(new Point2D(31.0D, 20.0D)));
		assertFalse(triangle.contains(new Point2D(29.0D, 30.0D)));
		assertFalse(triangle.contains(new Point2D(31.0D, 30.0D)));
		assertFalse(triangle.contains(new Point2D(20.0D, 19.0D)));
		assertFalse(triangle.contains(new Point2D(20.0D, 21.0D)));
		assertFalse(triangle.contains(new Point2D(30.0D, 19.0D)));
		assertFalse(triangle.contains(new Point2D(30.0D, 31.0D)));
		
		assertThrows(NullPointerException.class, () -> triangle.contains(null));
	}
	
	@Test
	public void testEquals() {
		final Triangle2D a = new Triangle2D(new Point2D(20.0D, 20.0D), new Point2D(30.0D, 20.0D), new Point2D(30.0D, 30.0D));
		final Triangle2D b = new Triangle2D(new Point2D(20.0D, 20.0D), new Point2D(30.0D, 20.0D), new Point2D(30.0D, 30.0D));
		final Triangle2D c = new Triangle2D(new Point2D(20.0D, 20.0D), new Point2D(30.0D, 20.0D), new Point2D(30.0D, 40.0D));
		final Triangle2D d = new Triangle2D(new Point2D(20.0D, 20.0D), new Point2D(30.0D, 10.0D), new Point2D(30.0D, 30.0D));
		final Triangle2D e = new Triangle2D(new Point2D(10.0D, 20.0D), new Point2D(30.0D, 20.0D), new Point2D(30.0D, 30.0D));
		final Triangle2D f = null;
		
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
	public void testGetA() {
		final Triangle2D triangle = new Triangle2D(new Point2D(10.0D, 10.0D), new Point2D(20.0D, 10.0D), new Point2D(20.0D, 20.0D));
		
		assertEquals(new Point2D(10.0D, 10.0D), triangle.getA());
	}
	
	@Test
	public void testGetB() {
		final Triangle2D triangle = new Triangle2D(new Point2D(10.0D, 10.0D), new Point2D(20.0D, 10.0D), new Point2D(20.0D, 20.0D));
		
		assertEquals(new Point2D(20.0D, 10.0D), triangle.getB());
	}
	
	@Test
	public void testGetC() {
		final Triangle2D triangle = new Triangle2D(new Point2D(10.0D, 10.0D), new Point2D(20.0D, 10.0D), new Point2D(20.0D, 20.0D));
		
		assertEquals(new Point2D(20.0D, 20.0D), triangle.getC());
	}
	
	@Test
	public void testGetID() {
		final Triangle2D triangle = new Triangle2D(new Point2D(10.0D, 10.0D), new Point2D(20.0D, 10.0D), new Point2D(20.0D, 20.0D));
		
		assertEquals(Triangle2D.ID, triangle.getID());
	}
	
	@Test
	public void testGetName() {
		final Triangle2D triangle = new Triangle2D(new Point2D(10.0D, 10.0D), new Point2D(20.0D, 10.0D), new Point2D(20.0D, 20.0D));
		
		assertEquals(Triangle2D.NAME, triangle.getName());
	}
	
	@Test
	public void testHashCode() {
		final Triangle2D a = new Triangle2D(new Point2D(10.0D, 10.0D), new Point2D(20.0D, 10.0D), new Point2D(20.0D, 20.0D));
		final Triangle2D b = new Triangle2D(new Point2D(10.0D, 10.0D), new Point2D(20.0D, 10.0D), new Point2D(20.0D, 20.0D));
		
		assertEquals(a.hashCode(), a.hashCode());
		assertEquals(a.hashCode(), b.hashCode());
	}
	
	@Test
	public void testToString() {
		final Triangle2D triangle = new Triangle2D(new Point2D(10.0D, 10.0D), new Point2D(20.0D, 10.0D), new Point2D(20.0D, 20.0D));
		
		assertEquals("new Triangle2D(new Point2D(10.0D, 10.0D), new Point2D(20.0D, 10.0D), new Point2D(20.0D, 20.0D))", triangle.toString());
	}
	
	@Test
	public void testWrite() {
		final Triangle2D a = new Triangle2D(new Point2D(10.0D, 10.0D), new Point2D(20.0D, 10.0D), new Point2D(20.0D, 20.0D));
		
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		
		final DataOutput dataOutput = new DataOutputStream(byteArrayOutputStream);
		
		a.write(dataOutput);
		
		final byte[] bytes = byteArrayOutputStream.toByteArray();
		
		final Triangle2D b = new Triangle2DReader().read(new DataInputStream(new ByteArrayInputStream(bytes)));
		
		assertEquals(a, b);
		
		assertThrows(NullPointerException.class, () -> a.write((DataOutput)(null)));
		assertThrows(UncheckedIOException.class, () -> a.write(new DataOutputMock()));
	}
}