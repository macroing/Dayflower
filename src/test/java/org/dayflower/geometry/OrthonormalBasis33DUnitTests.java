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
import static org.junit.jupiter.api.Assertions.assertFalse;
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
public final class OrthonormalBasis33DUnitTests {
	public OrthonormalBasis33DUnitTests() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Test
	public void testAcceptNodeHierarchicalVisitor() {
		final Vector3D u = new Vector3D(1.0D, 0.0D, 0.0D);
		final Vector3D v = new Vector3D(0.0D, 1.0D, 0.0D);
		final Vector3D w = new Vector3D(0.0D, 0.0D, 1.0D);
		
		final OrthonormalBasis33D orthonormalBasis = new OrthonormalBasis33D(w, v, u);
		
		assertTrue(orthonormalBasis.accept(new NodeHierarchicalVisitorMock(node -> false,                                                                               node -> node.equals(orthonormalBasis))));
		assertTrue(orthonormalBasis.accept(new NodeHierarchicalVisitorMock(node -> node.equals(orthonormalBasis),                                                       node -> node.equals(orthonormalBasis))));
		assertTrue(orthonormalBasis.accept(new NodeHierarchicalVisitorMock(node -> node.equals(orthonormalBasis) || node.equals(u),                                     node -> node.equals(orthonormalBasis) || node.equals(u))));
		assertTrue(orthonormalBasis.accept(new NodeHierarchicalVisitorMock(node -> node.equals(orthonormalBasis) || node.equals(u) || node.equals(v),                   node -> node.equals(orthonormalBasis) || node.equals(u) || node.equals(v))));
		assertTrue(orthonormalBasis.accept(new NodeHierarchicalVisitorMock(node -> node.equals(orthonormalBasis) || node.equals(u) || node.equals(v) || node.equals(w), node -> node.equals(orthonormalBasis) || node.equals(u) || node.equals(v) || node.equals(w))));
		
		assertThrows(NodeTraversalException.class, () -> orthonormalBasis.accept(new NodeHierarchicalVisitorMock(null, null)));
		assertThrows(NullPointerException.class, () -> orthonormalBasis.accept((NodeHierarchicalVisitor)(null)));
	}
	
	@Test
	public void testAcceptNodeVisitor() {
		final Vector3D u = new Vector3D(1.0D, 0.0D, 0.0D);
		final Vector3D v = new Vector3D(0.0D, 1.0D, 0.0D);
		final Vector3D w = new Vector3D(0.0D, 0.0D, 1.0D);
		
		final OrthonormalBasis33D orthonormalBasis = new OrthonormalBasis33D(w, v, u);
		
		assertThrows(NodeTraversalException.class, () -> orthonormalBasis.accept(new NodeVisitorMock(true)));
		assertThrows(NullPointerException.class, () -> orthonormalBasis.accept((NodeVisitor)(null)));
	}
	
	@Test
	public void testClearCacheAndGetCacheSizeAndGetCached() {
		assertEquals(0, OrthonormalBasis33D.getCacheSize());
		
		final OrthonormalBasis33D a = new OrthonormalBasis33D(new Vector3D(0.0D, 0.0D, 1.0D), new Vector3D(0.0D, 1.0D, 0.0D), new Vector3D(1.0D, 0.0D, 0.0D));
		final OrthonormalBasis33D b = new OrthonormalBasis33D(new Vector3D(0.0D, 0.0D, 1.0D), new Vector3D(0.0D, 1.0D, 0.0D), new Vector3D(1.0D, 0.0D, 0.0D));
		final OrthonormalBasis33D c = OrthonormalBasis33D.getCached(a);
		final OrthonormalBasis33D d = OrthonormalBasis33D.getCached(b);
		final OrthonormalBasis33D e = OrthonormalBasis33D.getCached(c);
		final OrthonormalBasis33D f = OrthonormalBasis33D.getCached(d);
		
		assertThrows(NullPointerException.class, () -> OrthonormalBasis33D.getCached(null));
		
		assertEquals(1, OrthonormalBasis33D.getCacheSize());
		
		OrthonormalBasis33D.clearCache();
		
		assertEquals(0, OrthonormalBasis33D.getCacheSize());
		
		assertTrue(a != b);
		assertTrue(a != c);
		assertTrue(a != d);
		
		assertTrue(b != a);
		assertTrue(b != c);
		assertTrue(b != d);
		
		assertTrue(c == e);
		assertTrue(c == f);
		
		assertTrue(d == e);
		assertTrue(d == f);
	}
	
	@Test
	public void testConstructor() {
		final OrthonormalBasis33D orthonormalBasis = new OrthonormalBasis33D();
		
		assertEquals(new Vector3D(1.0D, 0.0D, 0.0D), orthonormalBasis.getU());
		assertEquals(new Vector3D(0.0D, 1.0D, 0.0D), orthonormalBasis.getV());
		assertEquals(new Vector3D(0.0D, 0.0D, 1.0D), orthonormalBasis.getW());
	}
	
	@Test
	public void testConstructorVector3D() {
		final OrthonormalBasis33D orthonormalBasis = new OrthonormalBasis33D(new Vector3D(0.0D, 0.0D, 1.0D));
		
//		TODO: Find out if this should change.
		assertEquals(new Vector3D(0.0D, -1.0D, +0.0D), orthonormalBasis.getU());
		assertEquals(new Vector3D(1.0D, +0.0D, -0.0D), orthonormalBasis.getV());
		assertEquals(new Vector3D(0.0D, +0.0D, +1.0D), orthonormalBasis.getW());
		
		assertThrows(NullPointerException.class, () -> new OrthonormalBasis33D(null));
	}
	
	@Test
	public void testConstructorVector3DVector3D() {
		final OrthonormalBasis33D orthonormalBasis = new OrthonormalBasis33D(new Vector3D(0.0D, 0.0D, 1.0D), new Vector3D(0.0D, 1.0D, 0.0D));
		
		assertEquals(new Vector3D(1.0D, 0.0D, 0.0D), orthonormalBasis.getU());
		assertEquals(new Vector3D(0.0D, 1.0D, 0.0D), orthonormalBasis.getV());
		assertEquals(new Vector3D(0.0D, 0.0D, 1.0D), orthonormalBasis.getW());
		
		assertThrows(NullPointerException.class, () -> new OrthonormalBasis33D(new Vector3D(0.0D, 0.0D, 1.0D), null));
		assertThrows(NullPointerException.class, () -> new OrthonormalBasis33D(null, new Vector3D(0.0D, 1.0D, 0.0D)));
	}
	
	@Test
	public void testConstructorVector3DVector3DVector3D() {
		final OrthonormalBasis33D orthonormalBasis = new OrthonormalBasis33D(new Vector3D(0.0D, 0.0D, 1.0D), new Vector3D(0.0D, 1.0D, 0.0D), new Vector3D(1.0D, 0.0D, 0.0D));
		
		assertEquals(new Vector3D(1.0D, 0.0D, 0.0D), orthonormalBasis.getU());
		assertEquals(new Vector3D(0.0D, 1.0D, 0.0D), orthonormalBasis.getV());
		assertEquals(new Vector3D(0.0D, 0.0D, 1.0D), orthonormalBasis.getW());
		
		assertThrows(NullPointerException.class, () -> new OrthonormalBasis33D(new Vector3D(0.0D, 0.0D, 1.0D), new Vector3D(0.0D, 1.0D, 0.0D), null));
		assertThrows(NullPointerException.class, () -> new OrthonormalBasis33D(new Vector3D(0.0D, 0.0D, 1.0D), null, new Vector3D(1.0D, 0.0D, 0.0D)));
		assertThrows(NullPointerException.class, () -> new OrthonormalBasis33D(null, new Vector3D(0.0D, 1.0D, 0.0D), new Vector3D(1.0D, 0.0D, 0.0D)));
	}
	
	@Test
	public void testEquals() {
		final OrthonormalBasis33D a = new OrthonormalBasis33D(new Vector3D(0.0D, 0.0D, 1.0D), new Vector3D(0.0D, 1.0D, 0.0D), new Vector3D(1.0D, 0.0D, 0.0D));
		final OrthonormalBasis33D b = new OrthonormalBasis33D(new Vector3D(0.0D, 0.0D, 1.0D), new Vector3D(0.0D, 1.0D, 0.0D), new Vector3D(1.0D, 0.0D, 0.0D));
		final OrthonormalBasis33D c = new OrthonormalBasis33D(new Vector3D(0.0D, 0.0D, 1.0D), new Vector3D(0.0D, 1.0D, 0.0D), new Vector3D(2.0D, 0.0D, 0.0D));
		final OrthonormalBasis33D d = new OrthonormalBasis33D(new Vector3D(0.0D, 0.0D, 1.0D), new Vector3D(0.0D, 2.0D, 0.0D), new Vector3D(1.0D, 0.0D, 0.0D));
		final OrthonormalBasis33D e = new OrthonormalBasis33D(new Vector3D(0.0D, 0.0D, 2.0D), new Vector3D(0.0D, 1.0D, 0.0D), new Vector3D(1.0D, 0.0D, 0.0D));
		final OrthonormalBasis33D f = null;
		
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
	public void testFlip() {
		final OrthonormalBasis33D a = new OrthonormalBasis33D(new Vector3D(0.0D, 0.0D, 1.0D), new Vector3D(0.0D, 1.0D, 0.0D), new Vector3D(1.0D, 0.0D, 0.0D));
		final OrthonormalBasis33D b = OrthonormalBasis33D.flip(a);
		
		assertEquals(new Vector3D(-1.0F, -0.0D, -0.0D), b.getU());
		assertEquals(new Vector3D(-0.0F, -1.0D, -0.0D), b.getV());
		assertEquals(new Vector3D(-0.0F, -0.0D, -1.0D), b.getW());
		
		assertThrows(NullPointerException.class, () -> OrthonormalBasis33D.flip(null));
	}
	
	@Test
	public void testFlipU() {
		final OrthonormalBasis33D a = new OrthonormalBasis33D(new Vector3D(0.0D, 0.0D, 1.0D), new Vector3D(0.0D, 1.0D, 0.0D), new Vector3D(1.0D, 0.0D, 0.0D));
		final OrthonormalBasis33D b = OrthonormalBasis33D.flipU(a);
		
		assertEquals(new Vector3D(-1.0F, -0.0D, -0.0D), b.getU());
		assertEquals(new Vector3D(+0.0F, +1.0D, +0.0D), b.getV());
		assertEquals(new Vector3D(+0.0F, +0.0D, +1.0D), b.getW());
		
		assertThrows(NullPointerException.class, () -> OrthonormalBasis33D.flipU(null));
	}
	
	@Test
	public void testFlipV() {
		final OrthonormalBasis33D a = new OrthonormalBasis33D(new Vector3D(0.0D, 0.0D, 1.0D), new Vector3D(0.0D, 1.0D, 0.0D), new Vector3D(1.0D, 0.0D, 0.0D));
		final OrthonormalBasis33D b = OrthonormalBasis33D.flipV(a);
		
		assertEquals(new Vector3D(+1.0F, +0.0D, +0.0D), b.getU());
		assertEquals(new Vector3D(-0.0F, -1.0D, -0.0D), b.getV());
		assertEquals(new Vector3D(+0.0F, +0.0D, +1.0D), b.getW());
		
		assertThrows(NullPointerException.class, () -> OrthonormalBasis33D.flipV(null));
	}
	
	@Test
	public void testFlipW() {
		final OrthonormalBasis33D a = new OrthonormalBasis33D(new Vector3D(0.0D, 0.0D, 1.0D), new Vector3D(0.0D, 1.0D, 0.0D), new Vector3D(1.0D, 0.0D, 0.0D));
		final OrthonormalBasis33D b = OrthonormalBasis33D.flipW(a);
		
		assertEquals(new Vector3D(+1.0F, +0.0D, +0.0D), b.getU());
		assertEquals(new Vector3D(+0.0F, +1.0D, +0.0D), b.getV());
		assertEquals(new Vector3D(-0.0F, -0.0D, -1.0D), b.getW());
		
		assertThrows(NullPointerException.class, () -> OrthonormalBasis33D.flipW(null));
	}
	
	@Test
	public void testGetU() {
		final OrthonormalBasis33D orthonormalBasis = new OrthonormalBasis33D(new Vector3D(0.0D, 0.0D, 1.0D), new Vector3D(0.0D, 1.0D, 0.0D), new Vector3D(1.0D, 0.0D, 0.0D));
		
		assertEquals(new Vector3D(1.0D, 0.0D, 0.0D), orthonormalBasis.getU());
	}
	
	@Test
	public void testGetV() {
		final OrthonormalBasis33D orthonormalBasis = new OrthonormalBasis33D(new Vector3D(0.0D, 0.0D, 1.0D), new Vector3D(0.0D, 1.0D, 0.0D), new Vector3D(1.0D, 0.0D, 0.0D));
		
		assertEquals(new Vector3D(0.0D, 1.0D, 0.0D), orthonormalBasis.getV());
	}
	
	@Test
	public void testGetW() {
		final OrthonormalBasis33D orthonormalBasis = new OrthonormalBasis33D(new Vector3D(0.0D, 0.0D, 1.0D), new Vector3D(0.0D, 1.0D, 0.0D), new Vector3D(1.0D, 0.0D, 0.0D));
		
		assertEquals(new Vector3D(0.0D, 0.0D, 1.0D), orthonormalBasis.getW());
	}
	
	@Test
	public void testHasOrthogonalVectors() {
		final OrthonormalBasis33D a = new OrthonormalBasis33D(new Vector3D(0.0D, 0.0D, 1.0D), new Vector3D(0.0D, 1.0D, 0.0D), new Vector3D(1.0D, 0.0D, 0.0D));
		final OrthonormalBasis33D b = new OrthonormalBasis33D(new Vector3D(0.0D, 0.0D, 1.0D), new Vector3D(0.0D, 1.0D, 0.0D), new Vector3D(1.0D, 1.0D, 1.0D));
		final OrthonormalBasis33D c = new OrthonormalBasis33D(new Vector3D(0.0D, 0.0D, 1.0D), new Vector3D(1.0D, 1.0D, 1.0D), new Vector3D(1.0D, 0.0D, 0.0D));
		final OrthonormalBasis33D d = new OrthonormalBasis33D(new Vector3D(1.0D, 1.0D, 1.0D), new Vector3D(0.0D, 1.0D, 0.0D), new Vector3D(1.0D, 0.0D, 0.0D));
		final OrthonormalBasis33D e = new OrthonormalBasis33D(new Vector3D(0.0D, 0.0D, 1.0D), new Vector3D(0.0D, 0.0D, 1.0D), new Vector3D(1.0D, 0.0D, 0.0D));
		final OrthonormalBasis33D f = new OrthonormalBasis33D(new Vector3D(0.0D, 0.0D, 1.0D), new Vector3D(0.0D, 1.0D, 0.0D), new Vector3D(0.0D, 1.0D, 0.0D));
		final OrthonormalBasis33D g = new OrthonormalBasis33D(new Vector3D(0.0D, 0.0D, 1.0D), new Vector3D(0.0D, 1.0D, 0.0D), new Vector3D(0.0D, 0.0D, 1.0D));
		
		assertTrue(a.hasOrthogonalVectors());
		
		assertFalse(b.hasOrthogonalVectors());
		assertFalse(c.hasOrthogonalVectors());
		assertFalse(d.hasOrthogonalVectors());
		assertFalse(e.hasOrthogonalVectors());
		assertFalse(f.hasOrthogonalVectors());
		assertFalse(g.hasOrthogonalVectors());
	}
	
	@Test
	public void testHasUnitVectors() {
		final OrthonormalBasis33D a = new OrthonormalBasis33D(new Vector3D(0.0D, 0.0D, 1.0D), new Vector3D(0.0D, 1.0D, 0.0D), new Vector3D(1.0D, 0.0D, 0.0D));
		final OrthonormalBasis33D b = new OrthonormalBasis33D(new Vector3D(0.0D, 0.0D, 1.0D), new Vector3D(0.0D, 1.0D, 0.0D), new Vector3D(1.0D, 1.0D, 1.0D));
		final OrthonormalBasis33D c = new OrthonormalBasis33D(new Vector3D(0.0D, 0.0D, 1.0D), new Vector3D(1.0D, 1.0D, 1.0D), new Vector3D(1.0D, 0.0D, 0.0D));
		final OrthonormalBasis33D d = new OrthonormalBasis33D(new Vector3D(1.0D, 1.0D, 1.0D), new Vector3D(0.0D, 1.0D, 0.0D), new Vector3D(1.0D, 0.0D, 0.0D));
		
		assertTrue(a.hasUnitVectors());
		
		assertFalse(b.hasUnitVectors());
		assertFalse(c.hasUnitVectors());
		assertFalse(d.hasUnitVectors());
	}
	
	@Test
	public void testHashCode() {
		final OrthonormalBasis33D a = new OrthonormalBasis33D(new Vector3D(0.0D, 0.0D, 1.0D), new Vector3D(0.0D, 1.0D, 0.0D), new Vector3D(1.0D, 0.0D, 0.0D));
		final OrthonormalBasis33D b = new OrthonormalBasis33D(new Vector3D(0.0D, 0.0D, 1.0D), new Vector3D(0.0D, 1.0D, 0.0D), new Vector3D(1.0D, 0.0D, 0.0D));
		
		assertEquals(a.hashCode(), a.hashCode());
		assertEquals(a.hashCode(), b.hashCode());
	}
	
	@Test
	public void testIsOrthonormal() {
		final OrthonormalBasis33D a = new OrthonormalBasis33D(new Vector3D(0.0D, 0.0D, 1.0D), new Vector3D(0.0D, 1.0D, 0.0D), new Vector3D(1.0D, 0.0D, 0.0D));
		final OrthonormalBasis33D b = new OrthonormalBasis33D(new Vector3D(0.0D, 0.0D, 5.0D), new Vector3D(0.0D, 5.0D, 0.0D), new Vector3D(5.0D, 0.0D, 0.0D));
		final OrthonormalBasis33D c = new OrthonormalBasis33D(new Vector3D(0.0D, 0.0D, 1.0D), new Vector3D(0.0D, 0.0D, 1.0D), new Vector3D(0.0D, 0.0D, 1.0D));
		
		assertTrue(a.isOrthonormal());
		
		assertFalse(b.isOrthonormal());
		assertFalse(c.isOrthonormal());
	}
	
	@Test
	public void testRead() throws IOException {
		final OrthonormalBasis33D a = new OrthonormalBasis33D(new Vector3D(0.0D, 0.0D, 1.0D), new Vector3D(0.0D, 1.0D, 0.0D), new Vector3D(1.0D, 0.0D, 0.0D));
		
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		
		final
		DataOutput dataOutput = new DataOutputStream(byteArrayOutputStream);
		dataOutput.writeDouble(a.getW().x);
		dataOutput.writeDouble(a.getW().y);
		dataOutput.writeDouble(a.getW().z);
		dataOutput.writeDouble(a.getV().x);
		dataOutput.writeDouble(a.getV().y);
		dataOutput.writeDouble(a.getV().z);
		dataOutput.writeDouble(a.getU().x);
		dataOutput.writeDouble(a.getU().y);
		dataOutput.writeDouble(a.getU().z);
		
		final byte[] bytes = byteArrayOutputStream.toByteArray();
		
		final OrthonormalBasis33D b = OrthonormalBasis33D.read(new DataInputStream(new ByteArrayInputStream(bytes)));
		
		assertEquals(a, b);
		
		assertThrows(NullPointerException.class, () -> OrthonormalBasis33D.read(null));
		assertThrows(UncheckedIOException.class, () -> OrthonormalBasis33D.read(new DataInputStream(new ByteArrayInputStream(new byte[] {}))));
	}
	
	@Test
	public void testToString() {
		final OrthonormalBasis33D orthonormalBasis = new OrthonormalBasis33D(new Vector3D(0.0D, 0.0D, 1.0D), new Vector3D(0.0D, 1.0D, 0.0D), new Vector3D(1.0D, 0.0D, 0.0D));
		
		assertEquals("new OrthonormalBasis33D(new Vector3D(0.0D, 0.0D, 1.0D), new Vector3D(0.0D, 1.0D, 0.0D), new Vector3D(1.0D, 0.0D, 0.0D))", orthonormalBasis.toString());
	}
	
	@Test
	public void testTransform() {
		final OrthonormalBasis33D a = new OrthonormalBasis33D(new Vector3D(0.0D, 0.0D, 1.0D), new Vector3D(0.0D, 1.0D, 0.0D), new Vector3D(1.0D, 0.0D, 0.0D));
		final OrthonormalBasis33D b = OrthonormalBasis33D.transform(Matrix44D.rotateX(+180.0D), a);
		final OrthonormalBasis33D c = OrthonormalBasis33D.transform(Matrix44D.rotateX(-180.0D), b);
		final OrthonormalBasis33D d = OrthonormalBasis33D.transform(Matrix44D.rotateY(+180.0D), a);
		final OrthonormalBasis33D e = OrthonormalBasis33D.transform(Matrix44D.rotateY(-180.0D), d);
		final OrthonormalBasis33D f = OrthonormalBasis33D.transform(Matrix44D.rotateZ(+180.0D), a);
		final OrthonormalBasis33D g = OrthonormalBasis33D.transform(Matrix44D.rotateZ(-180.0D), f);
		
		assertEquals(a, c);
		assertEquals(a, e);
		assertEquals(a, g);
		
		assertTrue(b.isOrthonormal());
		assertTrue(d.isOrthonormal());
		assertTrue(f.isOrthonormal());
		
		assertEquals(new OrthonormalBasis33D(new Vector3D(0.0D, -0.00000000000000012246467991473532D, -1.0D), new Vector3D(0.0D, -1.0D, 0.00000000000000012246467991473532D), new Vector3D(1.0D, 0.0D, 0.0D)), b);
		assertEquals(new OrthonormalBasis33D(new Vector3D(0.00000000000000012246467991473532D, 0.0D, -1.0D), new Vector3D(0.0D, 1.0D, 0.0D), new Vector3D(-1.0D, 0.0D, -0.00000000000000012246467991473532D)), d);
		assertEquals(new OrthonormalBasis33D(new Vector3D(0.0D, 0.0D, 1.0D), new Vector3D(-0.00000000000000012246467991473532D, -1.0D, 0.0D), new Vector3D(-1.0D, 0.00000000000000012246467991473532D, 0.0D)), f);
		
		assertThrows(NullPointerException.class, () -> OrthonormalBasis33D.transform(Matrix44D.rotateX(+180.0D), null));
		assertThrows(NullPointerException.class, () -> OrthonormalBasis33D.transform(null, a));
	}
	
	@Test
	public void testTransformTranspose() {
		final OrthonormalBasis33D a = new OrthonormalBasis33D(new Vector3D(0.0D, 0.0D, 1.0D), new Vector3D(0.0D, 1.0D, 0.0D), new Vector3D(1.0D, 0.0D, 0.0D));
		final OrthonormalBasis33D b = OrthonormalBasis33D.transformTranspose(Matrix44D.transpose(Matrix44D.rotateX(+180.0D)), a);
		final OrthonormalBasis33D c = OrthonormalBasis33D.transformTranspose(Matrix44D.transpose(Matrix44D.rotateX(-180.0D)), b);
		final OrthonormalBasis33D d = OrthonormalBasis33D.transformTranspose(Matrix44D.transpose(Matrix44D.rotateY(+180.0D)), a);
		final OrthonormalBasis33D e = OrthonormalBasis33D.transformTranspose(Matrix44D.transpose(Matrix44D.rotateY(-180.0D)), d);
		final OrthonormalBasis33D f = OrthonormalBasis33D.transformTranspose(Matrix44D.transpose(Matrix44D.rotateZ(+180.0D)), a);
		final OrthonormalBasis33D g = OrthonormalBasis33D.transformTranspose(Matrix44D.transpose(Matrix44D.rotateZ(-180.0D)), f);
		
		assertEquals(a, c);
		assertEquals(a, e);
		assertEquals(a, g);
		
		assertTrue(b.isOrthonormal());
		assertTrue(d.isOrthonormal());
		assertTrue(f.isOrthonormal());
		
		assertEquals(new OrthonormalBasis33D(new Vector3D(0.0D, -0.00000000000000012246467991473532D, -1.0D), new Vector3D(0.0D, -1.0D, 0.00000000000000012246467991473532D), new Vector3D(1.0D, 0.0D, 0.0D)), b);
		assertEquals(new OrthonormalBasis33D(new Vector3D(0.00000000000000012246467991473532D, 0.0D, -1.0D), new Vector3D(0.0D, 1.0D, 0.0D), new Vector3D(-1.0D, 0.0D, -0.00000000000000012246467991473532D)), d);
		assertEquals(new OrthonormalBasis33D(new Vector3D(0.0D, 0.0D, 1.0D), new Vector3D(-0.00000000000000012246467991473532D, -1.0D, 0.0D), new Vector3D(-1.0D, 0.00000000000000012246467991473532D, 0.0D)), f);
		
		assertThrows(NullPointerException.class, () -> OrthonormalBasis33D.transformTranspose(Matrix44D.rotateX(+180.0D), null));
		assertThrows(NullPointerException.class, () -> OrthonormalBasis33D.transformTranspose(null, a));
	}
	
	@Test
	public void testWrite() {
		final OrthonormalBasis33D a = new OrthonormalBasis33D(new Vector3D(0.0D, 0.0D, 1.0D), new Vector3D(0.0D, 1.0D, 0.0D), new Vector3D(1.0D, 0.0D, 0.0D));
		
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		
		final DataOutput dataOutput = new DataOutputStream(byteArrayOutputStream);
		
		a.write(dataOutput);
		
		final byte[] bytes = byteArrayOutputStream.toByteArray();
		
		final OrthonormalBasis33D b = OrthonormalBasis33D.read(new DataInputStream(new ByteArrayInputStream(bytes)));
		
		assertEquals(a, b);
		
		assertThrows(NullPointerException.class, () -> a.write(null));
		assertThrows(UncheckedIOException.class, () -> a.write(new DataOutputMock()));
	}
}