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
package org.dayflower.geometry;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;

import org.dayflower.mock.DataOutputMock;
import org.dayflower.mock.NodeHierarchicalVisitorMock;
import org.dayflower.mock.NodeVisitorMock;

import org.junit.jupiter.api.Test;

import org.macroing.java.util.visitor.NodeHierarchicalVisitor;
import org.macroing.java.util.visitor.NodeTraversalException;
import org.macroing.java.util.visitor.NodeVisitor;

@SuppressWarnings("static-method")
public final class Ray2DUnitTests {
	public Ray2DUnitTests() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Test
	public void testAcceptNodeHierarchicalVisitor() {
		final Point2D origin = new Point2D(0.0D, 1.0D);
		
		final Vector2D direction = new Vector2D(1.0D, 0.0D);
		
		final Ray2D ray = new Ray2D(origin, direction);
		
		assertTrue(ray.accept(new NodeHierarchicalVisitorMock(node -> false,                                                             node -> node.equals(ray))));
		assertTrue(ray.accept(new NodeHierarchicalVisitorMock(node -> node.equals(ray),                                                  node -> node.equals(ray))));
		assertTrue(ray.accept(new NodeHierarchicalVisitorMock(node -> node.equals(ray) || node.equals(origin),                           node -> node.equals(ray) || node.equals(origin))));
		assertTrue(ray.accept(new NodeHierarchicalVisitorMock(node -> node.equals(ray) || node.equals(origin) || node.equals(direction), node -> node.equals(ray) || node.equals(origin) || node.equals(direction))));
		
		assertThrows(NodeTraversalException.class, () -> ray.accept(new NodeHierarchicalVisitorMock(null, null)));
		assertThrows(NullPointerException.class, () -> ray.accept((NodeHierarchicalVisitor)(null)));
	}
	
	@Test
	public void testAcceptNodeVisitor() {
		final Point2D origin = new Point2D(0.0D, 1.0D);
		
		final Vector2D direction = new Vector2D(1.0D, 0.0D);
		
		final Ray2D ray = new Ray2D(origin, direction);
		
		assertThrows(NodeTraversalException.class, () -> ray.accept(new NodeVisitorMock(true)));
		assertThrows(NullPointerException.class, () -> ray.accept((NodeVisitor)(null)));
	}
	
	@Test
	public void testConstructor() {
		assertThrows(NullPointerException.class, () -> new Ray2D(new Point2D(), null));
		assertThrows(NullPointerException.class, () -> new Ray2D(null, new Vector2D()));
	}
	
	@Test
	public void testEquals() {
		final Ray2D a = new Ray2D(new Point2D(0.0D, 1.0D), new Vector2D(1.0D, 0.0D));
		final Ray2D b = new Ray2D(new Point2D(0.0D, 1.0D), new Vector2D(1.0D, 0.0D));
		final Ray2D c = new Ray2D(new Point2D(0.0D, 1.0D), new Vector2D(0.0D, 1.0D));
		final Ray2D d = new Ray2D(new Point2D(1.0D, 0.0D), new Vector2D(1.0D, 0.0D));
		final Ray2D e = null;
		
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
	public void testGetDirection() {
		final Ray2D ray = new Ray2D(new Point2D(), new Vector2D(1.0D, 2.0D));
		
		assertEquals(Vector2D.normalize(new Vector2D(1.0D, 2.0D)), ray.getDirection());
	}
	
	@Test
	public void testGetOrigin() {
		final Ray2D ray = new Ray2D(new Point2D(1.0D, 2.0D), new Vector2D());
		
		assertEquals(new Point2D(1.0D, 2.0D), ray.getOrigin());
	}
	
	@Test
	public void testGetPointAt() {
		final Ray2D ray = new Ray2D(new Point2D(0.0D, 0.0D), new Vector2D(1.0D, 0.0D));
		
		assertEquals(new Point2D(1.0D, 0.0D), ray.getPointAt(1.0D));
	}
	
	@Test
	public void testHashCode() {
		final Ray2D a = new Ray2D(new Point2D(0.0D, 1.0D), new Vector2D(1.0D, 0.0D));
		final Ray2D b = new Ray2D(new Point2D(0.0D, 1.0D), new Vector2D(1.0D, 0.0D));
		
		assertEquals(a.hashCode(), a.hashCode());
		assertEquals(a.hashCode(), b.hashCode());
	}
	
	@Test
	public void testRead() throws IOException {
		final Ray2D a = new Ray2D(new Point2D(0.0D, 1.0D), new Vector2D(1.0D, 0.0D));
		
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		
		final
		DataOutput dataOutput = new DataOutputStream(byteArrayOutputStream);
		dataOutput.writeDouble(a.getOrigin().x);
		dataOutput.writeDouble(a.getOrigin().y);
		dataOutput.writeDouble(a.getDirection().x);
		dataOutput.writeDouble(a.getDirection().y);
		
		final byte[] bytes = byteArrayOutputStream.toByteArray();
		
		final Ray2D b = Ray2D.read(new DataInputStream(new ByteArrayInputStream(bytes)));
		
		assertEquals(a, b);
		
		assertThrows(NullPointerException.class, () -> Ray2D.read(null));
		assertThrows(UncheckedIOException.class, () -> Ray2D.read(new DataInputStream(new ByteArrayInputStream(new byte[] {}))));
	}
	
	@Test
	public void testToString() {
		final Ray2D ray = new Ray2D(new Point2D(0.0D, 0.0D), new Vector2D(1.0D, 0.0D));
		
		assertEquals("new Ray2D(new Point2D(0.0D, 0.0D), new Vector2D(1.0D, 0.0D))", ray.toString());
	}
	
	@Test
	public void testTransform() {
		final Matrix33D m = Matrix33D.translate(1.0D, 1.0D);
		
		final Ray2D a = new Ray2D(new Point2D(0.0D, 0.0D), new Vector2D(1.0D, 0.0D));
		final Ray2D b = Ray2D.transform(m, a);
		final Ray2D c = new Ray2D(new Point2D(1.0D, 1.0D), new Vector2D(1.0D, 0.0D));
		
		assertEquals(c, b);
		
		assertThrows(NullPointerException.class, () -> Ray2D.transform(m, null));
		assertThrows(NullPointerException.class, () -> Ray2D.transform(null, a));
	}
	
	@Test
	public void testWrite() {
		final Ray2D a = new Ray2D(new Point2D(0.0D, 1.0D), new Vector2D(1.0D, 0.0D));
		
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		
		final DataOutput dataOutput = new DataOutputStream(byteArrayOutputStream);
		
		a.write(dataOutput);
		
		final byte[] bytes = byteArrayOutputStream.toByteArray();
		
		final Ray2D b = Ray2D.read(new DataInputStream(new ByteArrayInputStream(bytes)));
		
		assertEquals(a, b);
		
		assertThrows(NullPointerException.class, () -> a.write(null));
		assertThrows(UncheckedIOException.class, () -> a.write(new DataOutputMock()));
	}
}