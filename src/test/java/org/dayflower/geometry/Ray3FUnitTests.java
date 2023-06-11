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
public final class Ray3FUnitTests {
	public Ray3FUnitTests() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Test
	public void testAcceptNodeHierarchicalVisitor() {
		final Point3F origin = new Point3F(0.0F, 1.0F, 2.0F);
		
		final Vector3F direction = new Vector3F(1.0F, 0.0F, 0.0F);
		
		final Ray3F ray = new Ray3F(origin, direction);
		
		assertTrue(ray.accept(new NodeHierarchicalVisitorMock(node -> false,                                                             node -> node.equals(ray))));
		assertTrue(ray.accept(new NodeHierarchicalVisitorMock(node -> node.equals(ray),                                                  node -> node.equals(ray))));
		assertTrue(ray.accept(new NodeHierarchicalVisitorMock(node -> node.equals(ray) || node.equals(origin),                           node -> node.equals(ray) || node.equals(origin))));
		assertTrue(ray.accept(new NodeHierarchicalVisitorMock(node -> node.equals(ray) || node.equals(origin) || node.equals(direction), node -> node.equals(ray) || node.equals(origin) || node.equals(direction))));
		
		assertThrows(NodeTraversalException.class, () -> ray.accept(new NodeHierarchicalVisitorMock(null, null)));
		assertThrows(NullPointerException.class, () -> ray.accept((NodeHierarchicalVisitor)(null)));
	}
	
	@Test
	public void testAcceptNodeVisitor() {
		final Point3F origin = new Point3F(0.0F, 1.0F, 2.0F);
		
		final Vector3F direction = new Vector3F(1.0F, 0.0F, 0.0F);
		
		final Ray3F ray = new Ray3F(origin, direction);
		
		assertThrows(NodeTraversalException.class, () -> ray.accept(new NodeVisitorMock(true)));
		assertThrows(NullPointerException.class, () -> ray.accept((NodeVisitor)(null)));
	}
	
	@Test
	public void testConstructor() {
		assertThrows(NullPointerException.class, () -> new Ray3F(new Point3F(), null));
		assertThrows(NullPointerException.class, () -> new Ray3F(null, new Vector3F()));
	}
	
	@Test
	public void testEquals() {
		final Ray3F a = new Ray3F(new Point3F(0.0F, 1.0F, 2.0F), new Vector3F(1.0F, 0.0F, 0.0F));
		final Ray3F b = new Ray3F(new Point3F(0.0F, 1.0F, 2.0F), new Vector3F(1.0F, 0.0F, 0.0F));
		final Ray3F c = new Ray3F(new Point3F(0.0F, 1.0F, 2.0F), new Vector3F(0.0F, 1.0F, 0.0F));
		final Ray3F d = new Ray3F(new Point3F(1.0F, 2.0F, 0.0F), new Vector3F(1.0F, 0.0F, 0.0F));
		final Ray3F e = null;
		
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
		final Ray3F ray = new Ray3F(new Point3F(), new Vector3F(1.0F, 2.0F, 3.0F));
		
		assertEquals(Vector3F.normalize(new Vector3F(1.0F, 2.0F, 3.0F)), ray.getDirection());
	}
	
	@Test
	public void testGetOrigin() {
		final Ray3F ray = new Ray3F(new Point3F(1.0F, 2.0F, 3.0F), new Vector3F());
		
		assertEquals(new Point3F(1.0F, 2.0F, 3.0F), ray.getOrigin());
	}
	
	@Test
	public void testGetPointAt() {
		final Ray3F ray = new Ray3F(new Point3F(0.0F, 0.0F, 0.0F), new Vector3F(1.0F, 0.0F, 0.0F));
		
		assertEquals(new Point3F(1.0F, 0.0F, 0.0F), ray.getPointAt(1.0F));
	}
	
	@Test
	public void testHashCode() {
		final Ray3F a = new Ray3F(new Point3F(0.0F, 1.0F, 2.0F), new Vector3F(1.0F, 0.0F, 0.0F));
		final Ray3F b = new Ray3F(new Point3F(0.0F, 1.0F, 2.0F), new Vector3F(1.0F, 0.0F, 0.0F));
		
		assertEquals(a.hashCode(), a.hashCode());
		assertEquals(a.hashCode(), b.hashCode());
	}
	
	@Test
	public void testRead() throws IOException {
		final Ray3F a = new Ray3F(new Point3F(0.0F, 1.0F, 2.0F), new Vector3F(1.0F, 0.0F, 0.0F));
		
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		
		final
		DataOutput dataOutput = new DataOutputStream(byteArrayOutputStream);
		dataOutput.writeFloat(a.getOrigin().x);
		dataOutput.writeFloat(a.getOrigin().y);
		dataOutput.writeFloat(a.getOrigin().z);
		dataOutput.writeFloat(a.getDirection().x);
		dataOutput.writeFloat(a.getDirection().y);
		dataOutput.writeFloat(a.getDirection().z);
		
		final byte[] bytes = byteArrayOutputStream.toByteArray();
		
		final Ray3F b = Ray3F.read(new DataInputStream(new ByteArrayInputStream(bytes)));
		
		assertEquals(a, b);
		
		assertThrows(NullPointerException.class, () -> Ray3F.read(null));
		assertThrows(UncheckedIOException.class, () -> Ray3F.read(new DataInputStream(new ByteArrayInputStream(new byte[] {}))));
	}
	
	@Test
	public void testToString() {
		final Ray3F ray = new Ray3F(new Point3F(0.0F, 0.0F, 0.0F), new Vector3F(1.0F, 0.0F, 0.0F));
		
		assertEquals("new Ray3F(new Point3F(0.0F, 0.0F, 0.0F), new Vector3F(1.0F, 0.0F, 0.0F))", ray.toString());
	}
	
	@Test
	public void testTransform() {
		final Matrix44F m = Matrix44F.identity();
		
		final Ray3F a = new Ray3F(new Point3F(0.0F, 0.0F, 0.0F), new Vector3F(1.0F, 0.0F, 0.0F));
		final Ray3F b = Ray3F.transform(m, a);
		final Ray3F c = new Ray3F(new Point3F(0.0F, 0.0F, 0.0F), new Vector3F(0.0F, 0.0F, 0.0F));
		final Ray3F d = Ray3F.transform(m, c);
		
		assertEquals(a, b);
		assertEquals(c, d);
		
		assertThrows(NullPointerException.class, () -> Ray3F.transform(m, null));
		assertThrows(NullPointerException.class, () -> Ray3F.transform(null, a));
	}
	
	@Test
	public void testWrite() {
		final Ray3F a = new Ray3F(new Point3F(0.0F, 1.0F, 2.0F), new Vector3F(1.0F, 0.0F, 0.0F));
		
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		
		final DataOutput dataOutput = new DataOutputStream(byteArrayOutputStream);
		
		a.write(dataOutput);
		
		final byte[] bytes = byteArrayOutputStream.toByteArray();
		
		final Ray3F b = Ray3F.read(new DataInputStream(new ByteArrayInputStream(bytes)));
		
		assertEquals(a, b);
		
		assertThrows(NullPointerException.class, () -> a.write(null));
		assertThrows(UncheckedIOException.class, () -> a.write(new DataOutputMock()));
	}
}