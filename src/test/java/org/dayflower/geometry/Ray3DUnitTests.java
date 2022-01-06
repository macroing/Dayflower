/**
 * Copyright 2014 - 2022 J&#246;rgen Lundgren
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
import org.dayflower.node.NodeHierarchicalVisitor;
import org.dayflower.node.NodeTraversalException;
import org.dayflower.node.NodeVisitor;
import org.junit.jupiter.api.Test;

@SuppressWarnings("static-method")
public final class Ray3DUnitTests {
	public Ray3DUnitTests() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Test
	public void testAcceptNodeHierarchicalVisitor() {
		final Point3D origin = new Point3D(0.0D, 1.0D, 2.0D);
		
		final Vector3D direction = new Vector3D(1.0D, 0.0D, 0.0D);
		
		final Ray3D ray = new Ray3D(origin, direction);
		
		assertTrue(ray.accept(new NodeHierarchicalVisitorMock(node -> false,                                                             node -> node.equals(ray))));
		assertTrue(ray.accept(new NodeHierarchicalVisitorMock(node -> node.equals(ray),                                                  node -> node.equals(ray))));
		assertTrue(ray.accept(new NodeHierarchicalVisitorMock(node -> node.equals(ray) || node.equals(origin),                           node -> node.equals(ray) || node.equals(origin))));
		assertTrue(ray.accept(new NodeHierarchicalVisitorMock(node -> node.equals(ray) || node.equals(origin) || node.equals(direction), node -> node.equals(ray) || node.equals(origin) || node.equals(direction))));
		
		assertThrows(NodeTraversalException.class, () -> ray.accept(new NodeHierarchicalVisitorMock(null, null)));
		assertThrows(NullPointerException.class, () -> ray.accept((NodeHierarchicalVisitor)(null)));
	}
	
	@Test
	public void testAcceptNodeVisitor() {
		final Point3D origin = new Point3D(0.0D, 1.0D, 2.0D);
		
		final Vector3D direction = new Vector3D(1.0D, 0.0D, 0.0D);
		
		final Ray3D ray = new Ray3D(origin, direction);
		
		assertThrows(NodeTraversalException.class, () -> ray.accept(new NodeVisitorMock(true)));
		assertThrows(NullPointerException.class, () -> ray.accept((NodeVisitor)(null)));
	}
	
	@Test
	public void testConstructor() {
		assertThrows(NullPointerException.class, () -> new Ray3D(new Point3D(), null));
		assertThrows(NullPointerException.class, () -> new Ray3D(null, new Vector3D()));
	}
	
	@Test
	public void testEquals() {
		final Ray3D a = new Ray3D(new Point3D(0.0D, 1.0D, 2.0D), new Vector3D(1.0D, 0.0D, 0.0D));
		final Ray3D b = new Ray3D(new Point3D(0.0D, 1.0D, 2.0D), new Vector3D(1.0D, 0.0D, 0.0D));
		final Ray3D c = new Ray3D(new Point3D(0.0D, 1.0D, 2.0D), new Vector3D(0.0D, 1.0D, 0.0D));
		final Ray3D d = new Ray3D(new Point3D(1.0D, 2.0D, 0.0D), new Vector3D(1.0D, 0.0D, 0.0D));
		final Ray3D e = null;
		
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
		final Ray3D ray = new Ray3D(new Point3D(), new Vector3D(1.0D, 2.0D, 3.0D));
		
		assertEquals(Vector3D.normalize(new Vector3D(1.0D, 2.0D, 3.0D)), ray.getDirection());
	}
	
	@Test
	public void testGetOrigin() {
		final Ray3D ray = new Ray3D(new Point3D(1.0D, 2.0D, 3.0D), new Vector3D());
		
		assertEquals(new Point3D(1.0D, 2.0D, 3.0D), ray.getOrigin());
	}
	
	@Test
	public void testGetPointAt() {
		final Ray3D ray = new Ray3D(new Point3D(0.0D, 0.0D, 0.0D), new Vector3D(1.0D, 0.0D, 0.0D));
		
		assertEquals(new Point3D(1.0D, 0.0D, 0.0D), ray.getPointAt(1.0D));
	}
	
	@Test
	public void testHashCode() {
		final Ray3D a = new Ray3D(new Point3D(0.0D, 1.0D, 2.0D), new Vector3D(1.0D, 0.0D, 0.0D));
		final Ray3D b = new Ray3D(new Point3D(0.0D, 1.0D, 2.0D), new Vector3D(1.0D, 0.0D, 0.0D));
		
		assertEquals(a.hashCode(), a.hashCode());
		assertEquals(a.hashCode(), b.hashCode());
	}
	
	@Test
	public void testRead() throws IOException {
		final Ray3D a = new Ray3D(new Point3D(0.0D, 1.0D, 2.0D), new Vector3D(1.0D, 0.0D, 0.0D));
		
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		
		final
		DataOutput dataOutput = new DataOutputStream(byteArrayOutputStream);
		dataOutput.writeDouble(a.getOrigin().getX());
		dataOutput.writeDouble(a.getOrigin().getY());
		dataOutput.writeDouble(a.getOrigin().getZ());
		dataOutput.writeDouble(a.getDirection().getX());
		dataOutput.writeDouble(a.getDirection().getY());
		dataOutput.writeDouble(a.getDirection().getZ());
		
		final byte[] bytes = byteArrayOutputStream.toByteArray();
		
		final Ray3D b = Ray3D.read(new DataInputStream(new ByteArrayInputStream(bytes)));
		
		assertEquals(a, b);
		
		assertThrows(NullPointerException.class, () -> Ray3D.read(null));
		assertThrows(UncheckedIOException.class, () -> Ray3D.read(new DataInputStream(new ByteArrayInputStream(new byte[] {}))));
	}
	
	@Test
	public void testToString() {
		final Ray3D ray = new Ray3D(new Point3D(0.0D, 0.0D, 0.0D), new Vector3D(1.0D, 0.0D, 0.0D));
		
		assertEquals("new Ray3D(new Point3D(0.0D, 0.0D, 0.0D), new Vector3D(1.0D, 0.0D, 0.0D))", ray.toString());
	}
	
	@Test
	public void testTransform() {
		final Matrix44D m = Matrix44D.identity();
		
		final Ray3D a = new Ray3D(new Point3D(0.0D, 0.0D, 0.0D), new Vector3D(1.0D, 0.0D, 0.0D));
		final Ray3D b = Ray3D.transform(m, a);
		final Ray3D c = new Ray3D(new Point3D(0.0D, 0.0D, 0.0D), new Vector3D(0.0D, 0.0D, 0.0D));
		final Ray3D d = Ray3D.transform(m, c);
		
		assertEquals(a, b);
		assertEquals(c, d);
		
		assertThrows(NullPointerException.class, () -> Ray3D.transform(m, null));
		assertThrows(NullPointerException.class, () -> Ray3D.transform(null, a));
	}
	
	@Test
	public void testWrite() {
		final Ray3D a = new Ray3D(new Point3D(0.0D, 1.0D, 2.0D), new Vector3D(1.0D, 0.0D, 0.0D));
		
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		
		final DataOutput dataOutput = new DataOutputStream(byteArrayOutputStream);
		
		a.write(dataOutput);
		
		final byte[] bytes = byteArrayOutputStream.toByteArray();
		
		final Ray3D b = Ray3D.read(new DataInputStream(new ByteArrayInputStream(bytes)));
		
		assertEquals(a, b);
		
		assertThrows(NullPointerException.class, () -> a.write(null));
		assertThrows(UncheckedIOException.class, () -> a.write(new DataOutputMock()));
	}
}