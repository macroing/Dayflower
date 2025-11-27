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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.UncheckedIOException;

import org.dayflower.geometry.Point2F;
import org.dayflower.mock.DataOutputMock;
import org.dayflower.mock.NodeHierarchicalVisitorMock;
import org.dayflower.mock.NodeVisitorMock;

import org.junit.jupiter.api.Test;

import org.macroing.java.util.visitor.NodeHierarchicalVisitor;
import org.macroing.java.util.visitor.NodeTraversalException;
import org.macroing.java.util.visitor.NodeVisitor;

@SuppressWarnings("static-method")
public final class Triangle2FUnitTests {
	public Triangle2FUnitTests() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Test
	public void testAcceptNodeHierarchicalVisitor() {
		final Point2F a = new Point2F(10.0F, 10.0F);
		final Point2F b = new Point2F(20.0F, 10.0F);
		final Point2F c = new Point2F(20.0F, 20.0F);
		
		final Triangle2F triangle = new Triangle2F(a, b, c);
		
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
		final Triangle2F triangle = new Triangle2F(new Point2F(10.0F, 10.0F), new Point2F(20.0F, 10.0F), new Point2F(20.0F, 20.0F));
		
		assertThrows(NodeTraversalException.class, () -> triangle.accept(new NodeVisitorMock(true)));
		assertThrows(NullPointerException.class, () -> triangle.accept((NodeVisitor)(null)));
	}
	
	@Test
	public void testConstants() {
		assertEquals(5, Triangle2F.ID);
		assertEquals("Triangle", Triangle2F.NAME);
	}
	
	@Test
	public void testConstructorPoint2FPoint2FPoint2F() {
		final Triangle2F triangle = new Triangle2F(new Point2F(10.0F, 10.0F), new Point2F(20.0F, 10.0F), new Point2F(20.0F, 20.0F));
		
		assertEquals(new Point2F(10.0F, 10.0F), triangle.getA());
		assertEquals(new Point2F(20.0F, 10.0F), triangle.getB());
		assertEquals(new Point2F(20.0F, 20.0F), triangle.getC());
		
		assertThrows(NullPointerException.class, () -> new Triangle2F(new Point2F(), new Point2F(), null));
		assertThrows(NullPointerException.class, () -> new Triangle2F(new Point2F(), null, new Point2F()));
		assertThrows(NullPointerException.class, () -> new Triangle2F(null, new Point2F(), new Point2F()));
	}
	
	@Test
	public void testContainsPoint2F() {
		final Triangle2F triangle = new Triangle2F(new Point2F(20.0F, 20.0F), new Point2F(30.0F, 20.0F), new Point2F(30.0F, 30.0F));
		
		assertTrue(triangle.contains(new Point2F(20.0F, 20.0F)));
		assertTrue(triangle.contains(new Point2F(25.0F, 20.0F)));
		assertTrue(triangle.contains(new Point2F(30.0F, 20.0F)));
		assertTrue(triangle.contains(new Point2F(30.0F, 25.0F)));
		assertTrue(triangle.contains(new Point2F(30.0F, 30.0F)));
		assertTrue(triangle.contains(new Point2F(25.0F, 25.0F)));
		assertTrue(triangle.contains(new Point2F(27.0F, 23.0F)));
		
		assertFalse(triangle.contains(new Point2F(19.0F, 20.0F)));
		assertFalse(triangle.contains(new Point2F(31.0F, 20.0F)));
		assertFalse(triangle.contains(new Point2F(29.0F, 30.0F)));
		assertFalse(triangle.contains(new Point2F(31.0F, 30.0F)));
		assertFalse(triangle.contains(new Point2F(20.0F, 19.0F)));
		assertFalse(triangle.contains(new Point2F(20.0F, 21.0F)));
		assertFalse(triangle.contains(new Point2F(30.0F, 19.0F)));
		assertFalse(triangle.contains(new Point2F(30.0F, 31.0F)));
		
		assertThrows(NullPointerException.class, () -> triangle.contains(null));
	}
	
	@Test
	public void testEquals() {
		final Triangle2F a = new Triangle2F(new Point2F(20.0F, 20.0F), new Point2F(30.0F, 20.0F), new Point2F(30.0F, 30.0F));
		final Triangle2F b = new Triangle2F(new Point2F(20.0F, 20.0F), new Point2F(30.0F, 20.0F), new Point2F(30.0F, 30.0F));
		final Triangle2F c = new Triangle2F(new Point2F(20.0F, 20.0F), new Point2F(30.0F, 20.0F), new Point2F(30.0F, 40.0F));
		final Triangle2F d = new Triangle2F(new Point2F(20.0F, 20.0F), new Point2F(30.0F, 10.0F), new Point2F(30.0F, 30.0F));
		final Triangle2F e = new Triangle2F(new Point2F(10.0F, 20.0F), new Point2F(30.0F, 20.0F), new Point2F(30.0F, 30.0F));
		final Triangle2F f = null;
		
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
		final Triangle2F triangle = new Triangle2F(new Point2F(10.0F, 10.0F), new Point2F(20.0F, 10.0F), new Point2F(20.0F, 20.0F));
		
		assertEquals(new Point2F(10.0F, 10.0F), triangle.getA());
	}
	
	@Test
	public void testGetB() {
		final Triangle2F triangle = new Triangle2F(new Point2F(10.0F, 10.0F), new Point2F(20.0F, 10.0F), new Point2F(20.0F, 20.0F));
		
		assertEquals(new Point2F(20.0F, 10.0F), triangle.getB());
	}
	
	@Test
	public void testGetC() {
		final Triangle2F triangle = new Triangle2F(new Point2F(10.0F, 10.0F), new Point2F(20.0F, 10.0F), new Point2F(20.0F, 20.0F));
		
		assertEquals(new Point2F(20.0F, 20.0F), triangle.getC());
	}
	
	@Test
	public void testGetID() {
		final Triangle2F triangle = new Triangle2F(new Point2F(10.0F, 10.0F), new Point2F(20.0F, 10.0F), new Point2F(20.0F, 20.0F));
		
		assertEquals(Triangle2F.ID, triangle.getID());
	}
	
	@Test
	public void testGetName() {
		final Triangle2F triangle = new Triangle2F(new Point2F(10.0F, 10.0F), new Point2F(20.0F, 10.0F), new Point2F(20.0F, 20.0F));
		
		assertEquals(Triangle2F.NAME, triangle.getName());
	}
	
	@Test
	public void testHashCode() {
		final Triangle2F a = new Triangle2F(new Point2F(10.0F, 10.0F), new Point2F(20.0F, 10.0F), new Point2F(20.0F, 20.0F));
		final Triangle2F b = new Triangle2F(new Point2F(10.0F, 10.0F), new Point2F(20.0F, 10.0F), new Point2F(20.0F, 20.0F));
		
		assertEquals(a.hashCode(), a.hashCode());
		assertEquals(a.hashCode(), b.hashCode());
	}
	
	@Test
	public void testToString() {
		final Triangle2F triangle = new Triangle2F(new Point2F(10.0F, 10.0F), new Point2F(20.0F, 10.0F), new Point2F(20.0F, 20.0F));
		
		assertEquals("new Triangle2F(new Point2F(10.0F, 10.0F), new Point2F(20.0F, 10.0F), new Point2F(20.0F, 20.0F))", triangle.toString());
	}
	
	@Test
	public void testWrite() {
		final Triangle2F a = new Triangle2F(new Point2F(10.0F, 10.0F), new Point2F(20.0F, 10.0F), new Point2F(20.0F, 20.0F));
		
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		
		final DataOutput dataOutput = new DataOutputStream(byteArrayOutputStream);
		
		a.write(dataOutput);
		
		final byte[] bytes = byteArrayOutputStream.toByteArray();
		
		final Triangle2F b = new Triangle2FReader().read(new DataInputStream(new ByteArrayInputStream(bytes)));
		
		assertEquals(a, b);
		
		assertThrows(NullPointerException.class, () -> a.write((DataOutput)(null)));
		assertThrows(UncheckedIOException.class, () -> a.write(new DataOutputMock()));
	}
}