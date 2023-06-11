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

import org.junit.jupiter.api.Test;

import org.macroing.java.util.visitor.NodeHierarchicalVisitor;
import org.macroing.java.util.visitor.NodeTraversalException;
import org.macroing.java.util.visitor.NodeVisitor;

@SuppressWarnings("static-method")
public final class OrthonormalBasis33FUnitTests {
	public OrthonormalBasis33FUnitTests() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Test
	public void testAcceptNodeHierarchicalVisitor() {
		final Vector3F u = new Vector3F(1.0F, 0.0F, 0.0F);
		final Vector3F v = new Vector3F(0.0F, 1.0F, 0.0F);
		final Vector3F w = new Vector3F(0.0F, 0.0F, 1.0F);
		
		final OrthonormalBasis33F orthonormalBasis = new OrthonormalBasis33F(w, v, u);
		
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
		final Vector3F u = new Vector3F(1.0F, 0.0F, 0.0F);
		final Vector3F v = new Vector3F(0.0F, 1.0F, 0.0F);
		final Vector3F w = new Vector3F(0.0F, 0.0F, 1.0F);
		
		final OrthonormalBasis33F orthonormalBasis = new OrthonormalBasis33F(w, v, u);
		
		assertThrows(NodeTraversalException.class, () -> orthonormalBasis.accept(new NodeVisitorMock(true)));
		assertThrows(NullPointerException.class, () -> orthonormalBasis.accept((NodeVisitor)(null)));
	}
	
	@Test
	public void testClearCacheAndGetCacheSizeAndGetCached() {
		assertEquals(0, OrthonormalBasis33F.getCacheSize());
		
		final OrthonormalBasis33F a = new OrthonormalBasis33F(new Vector3F(0.0F, 0.0F, 1.0F), new Vector3F(0.0F, 1.0F, 0.0F), new Vector3F(1.0F, 0.0F, 0.0F));
		final OrthonormalBasis33F b = new OrthonormalBasis33F(new Vector3F(0.0F, 0.0F, 1.0F), new Vector3F(0.0F, 1.0F, 0.0F), new Vector3F(1.0F, 0.0F, 0.0F));
		final OrthonormalBasis33F c = OrthonormalBasis33F.getCached(a);
		final OrthonormalBasis33F d = OrthonormalBasis33F.getCached(b);
		final OrthonormalBasis33F e = OrthonormalBasis33F.getCached(c);
		final OrthonormalBasis33F f = OrthonormalBasis33F.getCached(d);
		
		assertThrows(NullPointerException.class, () -> OrthonormalBasis33F.getCached(null));
		
		assertEquals(1, OrthonormalBasis33F.getCacheSize());
		
		OrthonormalBasis33F.clearCache();
		
		assertEquals(0, OrthonormalBasis33F.getCacheSize());
		
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
		final OrthonormalBasis33F orthonormalBasis = new OrthonormalBasis33F();
		
		assertEquals(new Vector3F(1.0F, 0.0F, 0.0F), orthonormalBasis.u);
		assertEquals(new Vector3F(0.0F, 1.0F, 0.0F), orthonormalBasis.v);
		assertEquals(new Vector3F(0.0F, 0.0F, 1.0F), orthonormalBasis.w);
	}
	
	@Test
	public void testConstructorVector3F() {
		final OrthonormalBasis33F orthonormalBasis = new OrthonormalBasis33F(new Vector3F(0.0F, 0.0F, 1.0F));
		
//		TODO: Find out if this should change.
		assertEquals(new Vector3F(0.0F, -1.0F, +0.0F), orthonormalBasis.u);
		assertEquals(new Vector3F(1.0F, +0.0F, -0.0F), orthonormalBasis.v);
		assertEquals(new Vector3F(0.0F, +0.0F, +1.0F), orthonormalBasis.w);
		
		assertThrows(NullPointerException.class, () -> new OrthonormalBasis33F(null));
	}
	
	@Test
	public void testConstructorVector3FVector3F() {
		final OrthonormalBasis33F orthonormalBasis = new OrthonormalBasis33F(new Vector3F(0.0F, 0.0F, 1.0F), new Vector3F(0.0F, 1.0F, 0.0F));
		
		assertEquals(new Vector3F(1.0F, 0.0F, 0.0F), orthonormalBasis.u);
		assertEquals(new Vector3F(0.0F, 1.0F, 0.0F), orthonormalBasis.v);
		assertEquals(new Vector3F(0.0F, 0.0F, 1.0F), orthonormalBasis.w);
		
		assertThrows(NullPointerException.class, () -> new OrthonormalBasis33F(new Vector3F(0.0F, 0.0F, 1.0F), null));
		assertThrows(NullPointerException.class, () -> new OrthonormalBasis33F(null, new Vector3F(0.0F, 1.0F, 0.0F)));
	}
	
	@Test
	public void testConstructorVector3FVector3FVector3F() {
		final OrthonormalBasis33F orthonormalBasis = new OrthonormalBasis33F(new Vector3F(0.0F, 0.0F, 1.0F), new Vector3F(0.0F, 1.0F, 0.0F), new Vector3F(1.0F, 0.0F, 0.0F));
		
		assertEquals(new Vector3F(1.0F, 0.0F, 0.0F), orthonormalBasis.u);
		assertEquals(new Vector3F(0.0F, 1.0F, 0.0F), orthonormalBasis.v);
		assertEquals(new Vector3F(0.0F, 0.0F, 1.0F), orthonormalBasis.w);
		
		assertThrows(NullPointerException.class, () -> new OrthonormalBasis33F(new Vector3F(0.0F, 0.0F, 1.0F), new Vector3F(0.0F, 1.0F, 0.0F), null));
		assertThrows(NullPointerException.class, () -> new OrthonormalBasis33F(new Vector3F(0.0F, 0.0F, 1.0F), null, new Vector3F(1.0F, 0.0F, 0.0F)));
		assertThrows(NullPointerException.class, () -> new OrthonormalBasis33F(null, new Vector3F(0.0F, 1.0F, 0.0F), new Vector3F(1.0F, 0.0F, 0.0F)));
	}
	
	@Test
	public void testEquals() {
		final OrthonormalBasis33F a = new OrthonormalBasis33F(new Vector3F(0.0F, 0.0F, 1.0F), new Vector3F(0.0F, 1.0F, 0.0F), new Vector3F(1.0F, 0.0F, 0.0F));
		final OrthonormalBasis33F b = new OrthonormalBasis33F(new Vector3F(0.0F, 0.0F, 1.0F), new Vector3F(0.0F, 1.0F, 0.0F), new Vector3F(1.0F, 0.0F, 0.0F));
		final OrthonormalBasis33F c = new OrthonormalBasis33F(new Vector3F(0.0F, 0.0F, 1.0F), new Vector3F(0.0F, 1.0F, 0.0F), new Vector3F(2.0F, 0.0F, 0.0F));
		final OrthonormalBasis33F d = new OrthonormalBasis33F(new Vector3F(0.0F, 0.0F, 1.0F), new Vector3F(0.0F, 2.0F, 0.0F), new Vector3F(1.0F, 0.0F, 0.0F));
		final OrthonormalBasis33F e = new OrthonormalBasis33F(new Vector3F(0.0F, 0.0F, 2.0F), new Vector3F(0.0F, 1.0F, 0.0F), new Vector3F(1.0F, 0.0F, 0.0F));
		final OrthonormalBasis33F f = null;
		
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
		final OrthonormalBasis33F a = new OrthonormalBasis33F(new Vector3F(0.0F, 0.0F, 1.0F), new Vector3F(0.0F, 1.0F, 0.0F), new Vector3F(1.0F, 0.0F, 0.0F));
		final OrthonormalBasis33F b = OrthonormalBasis33F.flip(a);
		
		assertEquals(new Vector3F(-1.0F, -0.0F, -0.0F), b.u);
		assertEquals(new Vector3F(-0.0F, -1.0F, -0.0F), b.v);
		assertEquals(new Vector3F(-0.0F, -0.0F, -1.0F), b.w);
		
		assertThrows(NullPointerException.class, () -> OrthonormalBasis33F.flip(null));
	}
	
	@Test
	public void testFlipU() {
		final OrthonormalBasis33F a = new OrthonormalBasis33F(new Vector3F(0.0F, 0.0F, 1.0F), new Vector3F(0.0F, 1.0F, 0.0F), new Vector3F(1.0F, 0.0F, 0.0F));
		final OrthonormalBasis33F b = OrthonormalBasis33F.flipU(a);
		
		assertEquals(new Vector3F(-1.0F, -0.0F, -0.0F), b.u);
		assertEquals(new Vector3F(+0.0F, +1.0F, +0.0F), b.v);
		assertEquals(new Vector3F(+0.0F, +0.0F, +1.0F), b.w);
		
		assertThrows(NullPointerException.class, () -> OrthonormalBasis33F.flipU(null));
	}
	
	@Test
	public void testFlipV() {
		final OrthonormalBasis33F a = new OrthonormalBasis33F(new Vector3F(0.0F, 0.0F, 1.0F), new Vector3F(0.0F, 1.0F, 0.0F), new Vector3F(1.0F, 0.0F, 0.0F));
		final OrthonormalBasis33F b = OrthonormalBasis33F.flipV(a);
		
		assertEquals(new Vector3F(+1.0F, +0.0F, +0.0F), b.u);
		assertEquals(new Vector3F(-0.0F, -1.0F, -0.0F), b.v);
		assertEquals(new Vector3F(+0.0F, +0.0F, +1.0F), b.w);
		
		assertThrows(NullPointerException.class, () -> OrthonormalBasis33F.flipV(null));
	}
	
	@Test
	public void testFlipW() {
		final OrthonormalBasis33F a = new OrthonormalBasis33F(new Vector3F(0.0F, 0.0F, 1.0F), new Vector3F(0.0F, 1.0F, 0.0F), new Vector3F(1.0F, 0.0F, 0.0F));
		final OrthonormalBasis33F b = OrthonormalBasis33F.flipW(a);
		
		assertEquals(new Vector3F(+1.0F, +0.0F, +0.0F), b.u);
		assertEquals(new Vector3F(+0.0F, +1.0F, +0.0F), b.v);
		assertEquals(new Vector3F(-0.0F, -0.0F, -1.0F), b.w);
		
		assertThrows(NullPointerException.class, () -> OrthonormalBasis33F.flipW(null));
	}
	
	@Test
	public void testHasOrthogonalVectors() {
		final OrthonormalBasis33F a = new OrthonormalBasis33F(new Vector3F(0.0F, 0.0F, 1.0F), new Vector3F(0.0F, 1.0F, 0.0F), new Vector3F(1.0F, 0.0F, 0.0F));
		final OrthonormalBasis33F b = new OrthonormalBasis33F(new Vector3F(0.0F, 0.0F, 1.0F), new Vector3F(0.0F, 1.0F, 0.0F), new Vector3F(1.0F, 1.0F, 1.0F));
		final OrthonormalBasis33F c = new OrthonormalBasis33F(new Vector3F(0.0F, 0.0F, 1.0F), new Vector3F(1.0F, 1.0F, 1.0F), new Vector3F(1.0F, 0.0F, 0.0F));
		final OrthonormalBasis33F d = new OrthonormalBasis33F(new Vector3F(1.0F, 1.0F, 1.0F), new Vector3F(0.0F, 1.0F, 0.0F), new Vector3F(1.0F, 0.0F, 0.0F));
		final OrthonormalBasis33F e = new OrthonormalBasis33F(new Vector3F(0.0F, 0.0F, 1.0F), new Vector3F(0.0F, 0.0F, 1.0F), new Vector3F(1.0F, 0.0F, 0.0F));
		final OrthonormalBasis33F f = new OrthonormalBasis33F(new Vector3F(0.0F, 0.0F, 1.0F), new Vector3F(0.0F, 1.0F, 0.0F), new Vector3F(0.0F, 1.0F, 0.0F));
		final OrthonormalBasis33F g = new OrthonormalBasis33F(new Vector3F(0.0F, 0.0F, 1.0F), new Vector3F(0.0F, 1.0F, 0.0F), new Vector3F(0.0F, 0.0F, 1.0F));
		
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
		final OrthonormalBasis33F a = new OrthonormalBasis33F(new Vector3F(0.0F, 0.0F, 1.0F), new Vector3F(0.0F, 1.0F, 0.0F), new Vector3F(1.0F, 0.0F, 0.0F));
		final OrthonormalBasis33F b = new OrthonormalBasis33F(new Vector3F(0.0F, 0.0F, 1.0F), new Vector3F(0.0F, 1.0F, 0.0F), new Vector3F(1.0F, 1.0F, 1.0F));
		final OrthonormalBasis33F c = new OrthonormalBasis33F(new Vector3F(0.0F, 0.0F, 1.0F), new Vector3F(1.0F, 1.0F, 1.0F), new Vector3F(1.0F, 0.0F, 0.0F));
		final OrthonormalBasis33F d = new OrthonormalBasis33F(new Vector3F(1.0F, 1.0F, 1.0F), new Vector3F(0.0F, 1.0F, 0.0F), new Vector3F(1.0F, 0.0F, 0.0F));
		
		assertTrue(a.hasUnitVectors());
		
		assertFalse(b.hasUnitVectors());
		assertFalse(c.hasUnitVectors());
		assertFalse(d.hasUnitVectors());
	}
	
	@Test
	public void testHashCode() {
		final OrthonormalBasis33F a = new OrthonormalBasis33F(new Vector3F(0.0F, 0.0F, 1.0F), new Vector3F(0.0F, 1.0F, 0.0F), new Vector3F(1.0F, 0.0F, 0.0F));
		final OrthonormalBasis33F b = new OrthonormalBasis33F(new Vector3F(0.0F, 0.0F, 1.0F), new Vector3F(0.0F, 1.0F, 0.0F), new Vector3F(1.0F, 0.0F, 0.0F));
		
		assertEquals(a.hashCode(), a.hashCode());
		assertEquals(a.hashCode(), b.hashCode());
	}
	
	@Test
	public void testIsOrthonormal() {
		final OrthonormalBasis33F a = new OrthonormalBasis33F(new Vector3F(0.0F, 0.0F, 1.0F), new Vector3F(0.0F, 1.0F, 0.0F), new Vector3F(1.0F, 0.0F, 0.0F));
		final OrthonormalBasis33F b = new OrthonormalBasis33F(new Vector3F(0.0F, 0.0F, 5.0F), new Vector3F(0.0F, 5.0F, 0.0F), new Vector3F(5.0F, 0.0F, 1.0F));
		final OrthonormalBasis33F c = new OrthonormalBasis33F(new Vector3F(0.0F, 0.0F, 1.0F), new Vector3F(0.0F, 0.0F, 1.0F), new Vector3F(0.0F, 0.0F, 1.0F));
		
		assertTrue(a.isOrthonormal());
		
		assertFalse(b.isOrthonormal());
		assertFalse(c.isOrthonormal());
	}
	
	@Test
	public void testRead() throws IOException {
		final OrthonormalBasis33F a = new OrthonormalBasis33F(new Vector3F(0.0F, 0.0F, 1.0F), new Vector3F(0.0F, 1.0F, 0.0F), new Vector3F(1.0F, 0.0F, 0.0F));
		
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		
		final
		DataOutput dataOutput = new DataOutputStream(byteArrayOutputStream);
		dataOutput.writeFloat(a.w.x);
		dataOutput.writeFloat(a.w.y);
		dataOutput.writeFloat(a.w.z);
		dataOutput.writeFloat(a.v.x);
		dataOutput.writeFloat(a.v.y);
		dataOutput.writeFloat(a.v.z);
		dataOutput.writeFloat(a.u.x);
		dataOutput.writeFloat(a.u.y);
		dataOutput.writeFloat(a.u.z);
		
		final byte[] bytes = byteArrayOutputStream.toByteArray();
		
		final OrthonormalBasis33F b = OrthonormalBasis33F.read(new DataInputStream(new ByteArrayInputStream(bytes)));
		
		assertEquals(a, b);
		
		assertThrows(NullPointerException.class, () -> OrthonormalBasis33F.read(null));
		assertThrows(UncheckedIOException.class, () -> OrthonormalBasis33F.read(new DataInputStream(new ByteArrayInputStream(new byte[] {}))));
	}
	
	@Test
	public void testToString() {
		final OrthonormalBasis33F orthonormalBasis = new OrthonormalBasis33F(new Vector3F(0.0F, 0.0F, 1.0F), new Vector3F(0.0F, 1.0F, 0.0F), new Vector3F(1.0F, 0.0F, 0.0F));
		
		assertEquals("new OrthonormalBasis33F(new Vector3F(0.0F, 0.0F, 1.0F), new Vector3F(0.0F, 1.0F, 0.0F), new Vector3F(1.0F, 0.0F, 0.0F))", orthonormalBasis.toString());
	}
	
	@Test
	public void testTransform() {
		final OrthonormalBasis33F a = new OrthonormalBasis33F(new Vector3F(0.0F, 0.0F, 1.0F), new Vector3F(0.0F, 1.0F, 0.0F), new Vector3F(1.0F, 0.0F, 0.0F));
		final OrthonormalBasis33F b = OrthonormalBasis33F.transform(Matrix44F.rotateX(+180.0F), a);
		final OrthonormalBasis33F c = OrthonormalBasis33F.transform(Matrix44F.rotateX(-180.0F), b);
		final OrthonormalBasis33F d = OrthonormalBasis33F.transform(Matrix44F.rotateY(+180.0F), a);
		final OrthonormalBasis33F e = OrthonormalBasis33F.transform(Matrix44F.rotateY(-180.0F), d);
		final OrthonormalBasis33F f = OrthonormalBasis33F.transform(Matrix44F.rotateZ(+180.0F), a);
		final OrthonormalBasis33F g = OrthonormalBasis33F.transform(Matrix44F.rotateZ(-180.0F), f);
		
		assertEquals(a, c);
		assertEquals(a, e);
		assertEquals(a, g);
		
		assertTrue(b.isOrthonormal());
		assertTrue(d.isOrthonormal());
		assertTrue(f.isOrthonormal());
		
		assertEquals(new OrthonormalBasis33F(new Vector3F(0.0F, 0.0000000874227766F, -1.0F), new Vector3F(0.0F, -1.0F, -0.0000000874227766F), new Vector3F(1.0F, 0.0F, 0.0F)), b);
		assertEquals(new OrthonormalBasis33F(new Vector3F(-0.0000000874227766F, 0.0F, -1.0F), new Vector3F(0.0F, 1.0F, 0.0F), new Vector3F(-1.0F, 0.0F, 0.0000000874227766F)), d);
		assertEquals(new OrthonormalBasis33F(new Vector3F(0.0F, 0.0F, 1.0F), new Vector3F(0.0000000874227766F, -1.0F, 0.0F), new Vector3F(-1.0F, -0.0000000874227766F, 0.0F)), f);
		
		assertThrows(NullPointerException.class, () -> OrthonormalBasis33F.transform(Matrix44F.rotateX(+180.0F), null));
		assertThrows(NullPointerException.class, () -> OrthonormalBasis33F.transform(null, a));
	}
	
	@Test
	public void testTransformTranspose() {
		final OrthonormalBasis33F a = new OrthonormalBasis33F(new Vector3F(0.0F, 0.0F, 1.0F), new Vector3F(0.0F, 1.0F, 0.0F), new Vector3F(1.0F, 0.0F, 0.0F));
		final OrthonormalBasis33F b = OrthonormalBasis33F.transformTranspose(Matrix44F.transpose(Matrix44F.rotateX(+180.0F)), a);
		final OrthonormalBasis33F c = OrthonormalBasis33F.transformTranspose(Matrix44F.transpose(Matrix44F.rotateX(-180.0F)), b);
		final OrthonormalBasis33F d = OrthonormalBasis33F.transformTranspose(Matrix44F.transpose(Matrix44F.rotateY(+180.0F)), a);
		final OrthonormalBasis33F e = OrthonormalBasis33F.transformTranspose(Matrix44F.transpose(Matrix44F.rotateY(-180.0F)), d);
		final OrthonormalBasis33F f = OrthonormalBasis33F.transformTranspose(Matrix44F.transpose(Matrix44F.rotateZ(+180.0F)), a);
		final OrthonormalBasis33F g = OrthonormalBasis33F.transformTranspose(Matrix44F.transpose(Matrix44F.rotateZ(-180.0F)), f);
		
		assertEquals(a, c);
		assertEquals(a, e);
		assertEquals(a, g);
		
		assertTrue(b.isOrthonormal());
		assertTrue(d.isOrthonormal());
		assertTrue(f.isOrthonormal());
		
		assertEquals(new OrthonormalBasis33F(new Vector3F(0.0F, 0.0000000874227766F, -1.0F), new Vector3F(0.0F, -1.0F, -0.0000000874227766F), new Vector3F(1.0F, 0.0F, 0.0F)), b);
		assertEquals(new OrthonormalBasis33F(new Vector3F(-0.0000000874227766F, 0.0F, -1.0F), new Vector3F(0.0F, 1.0F, 0.0F), new Vector3F(-1.0F, 0.0F, 0.0000000874227766F)), d);
		assertEquals(new OrthonormalBasis33F(new Vector3F(0.0F, 0.0F, 1.0F), new Vector3F(0.0000000874227766F, -1.0F, 0.0F), new Vector3F(-1.0F, -0.0000000874227766F, 0.0F)), f);
		
		assertThrows(NullPointerException.class, () -> OrthonormalBasis33F.transformTranspose(Matrix44F.rotateX(+180.0F), null));
		assertThrows(NullPointerException.class, () -> OrthonormalBasis33F.transformTranspose(null, a));
	}
	
	@Test
	public void testWrite() {
		final OrthonormalBasis33F a = new OrthonormalBasis33F(new Vector3F(0.0F, 0.0F, 1.0F), new Vector3F(0.0F, 1.0F, 0.0F), new Vector3F(1.0F, 0.0F, 0.0F));
		
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		
		final DataOutput dataOutput = new DataOutputStream(byteArrayOutputStream);
		
		a.write(dataOutput);
		
		final byte[] bytes = byteArrayOutputStream.toByteArray();
		
		final OrthonormalBasis33F b = OrthonormalBasis33F.read(new DataInputStream(new ByteArrayInputStream(bytes)));
		
		assertEquals(a, b);
		
		assertThrows(NullPointerException.class, () -> a.write(null));
		assertThrows(UncheckedIOException.class, () -> a.write(new DataOutputMock()));
	}
}