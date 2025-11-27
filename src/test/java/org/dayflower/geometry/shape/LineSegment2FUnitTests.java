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

import org.dayflower.geometry.Point2F;
import org.dayflower.mock.DataOutputMock;
import org.dayflower.mock.NodeHierarchicalVisitorMock;
import org.dayflower.mock.NodeVisitorMock;

import org.junit.jupiter.api.Test;

import org.macroing.java.util.visitor.NodeHierarchicalVisitor;
import org.macroing.java.util.visitor.NodeTraversalException;
import org.macroing.java.util.visitor.NodeVisitor;

@SuppressWarnings("static-method")
public final class LineSegment2FUnitTests {
	public LineSegment2FUnitTests() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Test
	public void testAcceptNodeHierarchicalVisitor() {
		final Point2F a = new Point2F(10.0F, 10.0F);
		final Point2F b = new Point2F(20.0F, 10.0F);
		
		final LineSegment2F lineSegment = new LineSegment2F(a, b);
		
		assertTrue(lineSegment.accept(new NodeHierarchicalVisitorMock(node -> false,                                                        node -> node.equals(lineSegment))));
		assertTrue(lineSegment.accept(new NodeHierarchicalVisitorMock(node -> node.equals(lineSegment),                                     node -> node.equals(lineSegment))));
		assertTrue(lineSegment.accept(new NodeHierarchicalVisitorMock(node -> node.equals(lineSegment) || node.equals(a),                   node -> node.equals(lineSegment) || node.equals(a))));
		assertTrue(lineSegment.accept(new NodeHierarchicalVisitorMock(node -> node.equals(lineSegment) || node.equals(a) || node.equals(b), node -> node.equals(lineSegment) || node.equals(a) || node.equals(b))));
		
		assertThrows(NodeTraversalException.class, () -> lineSegment.accept(new NodeHierarchicalVisitorMock(null, null)));
		assertThrows(NullPointerException.class, () -> lineSegment.accept((NodeHierarchicalVisitor)(null)));
	}
	
	@Test
	public void testAcceptNodeVisitor() {
		final LineSegment2F lineSegment = new LineSegment2F(new Point2F(10.0F, 10.0F), new Point2F(20.0F, 10.0F));
		
		assertThrows(NodeTraversalException.class, () -> lineSegment.accept(new NodeVisitorMock(true)));
		assertThrows(NullPointerException.class, () -> lineSegment.accept((NodeVisitor)(null)));
	}
	
	@Test
	public void testConstants() {
		assertEquals(2, LineSegment2F.ID);
		assertEquals("Line Segment", LineSegment2F.NAME);
	}
	
	@Test
	public void testConstructor() {
		final LineSegment2F lineSegment = new LineSegment2F(new Point2F(10.0F, 10.0F), new Point2F(20.0F, 10.0F));
		
		assertEquals(new Point2F(10.0F, 10.0F), lineSegment.getA());
		assertEquals(new Point2F(20.0F, 10.0F), lineSegment.getB());
		
		assertThrows(NullPointerException.class, () -> new LineSegment2F(new Point2F(10.0F, 10.0F), null));
		assertThrows(NullPointerException.class, () -> new LineSegment2F(null, new Point2F(20.0F, 10.0F)));
	}
	
	@Test
	public void testContains() {
		final List<LineSegment2F> lineSegments = LineSegment2F.fromPoints(new Point2F(10.0F, 0.0F), new Point2F(20.0F, 0.0F), new Point2F(30.0F, 10.0F), new Point2F(30.0F, 20.0F), new Point2F(20.0F, 30.0F), new Point2F(10.0F, 30.0F), new Point2F(0.0F, 20.0F), new Point2F(0.0F, 10.0F));
		
		for(final LineSegment2F lineSegment : lineSegments) {
			assertTrue(lineSegment.contains(lineSegment.getA()));
			assertTrue(lineSegment.contains(Point2F.midpoint(lineSegment.getA(), lineSegment.getB())));
			assertTrue(lineSegment.contains(lineSegment.getB()));
			
			assertFalse(lineSegment.contains(new Point2F(lineSegment.getA().x + 100.0F, lineSegment.getA().y)));
			assertFalse(lineSegment.contains(new Point2F(lineSegment.getA().x - 100.0F, lineSegment.getA().y)));
			
			assertFalse(lineSegment.contains(new Point2F(lineSegment.getA().x, lineSegment.getA().y + 100.0F)));
			assertFalse(lineSegment.contains(new Point2F(lineSegment.getA().x, lineSegment.getA().y - 100.0F)));
		}
		
		assertThrows(NullPointerException.class, () -> new LineSegment2F(new Point2F(10.0F, 10.0F), new Point2F(20.0F, 10.0F)).contains(null));
	}
	
	@Test
	public void testEquals() {
		final LineSegment2F a = new LineSegment2F(new Point2F(10.0F, 10.0F), new Point2F(20.0F, 10.0F));
		final LineSegment2F b = new LineSegment2F(new Point2F(10.0F, 10.0F), new Point2F(20.0F, 10.0F));
		final LineSegment2F c = new LineSegment2F(new Point2F(10.0F, 10.0F), new Point2F(30.0F, 30.0F));
		final LineSegment2F d = new LineSegment2F(new Point2F(30.0F, 30.0F), new Point2F(20.0F, 10.0F));
		final LineSegment2F e = null;
		
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
	public void testFromPoints() {
		final List<LineSegment2F> lineSegments = LineSegment2F.fromPoints(new Point2F(0.0F, 0.0F), new Point2F(10.0F, 0.0F), new Point2F(10.0F, 10.0F), new Point2F(0.0F, 10.0F));
		
		assertNotNull(lineSegments);
		
		assertEquals(4, lineSegments.size());
		
		assertEquals(new LineSegment2F(new Point2F( 0.0F,  0.0F), new Point2F(10.0F,  0.0F)), lineSegments.get(0));
		assertEquals(new LineSegment2F(new Point2F(10.0F,  0.0F), new Point2F(10.0F, 10.0F)), lineSegments.get(1));
		assertEquals(new LineSegment2F(new Point2F(10.0F, 10.0F), new Point2F( 0.0F, 10.0F)), lineSegments.get(2));
		assertEquals(new LineSegment2F(new Point2F( 0.0F, 10.0F), new Point2F( 0.0F,  0.0F)), lineSegments.get(3));
		
		assertThrows(IllegalArgumentException.class, () -> LineSegment2F.fromPoints(new Point2F()));
		assertThrows(NullPointerException.class, () -> LineSegment2F.fromPoints((Point2F[])(null)));
		assertThrows(NullPointerException.class, () -> LineSegment2F.fromPoints(new Point2F[] {new Point2F(), null}));
	}
	
	@Test
	public void testGetA() {
		final LineSegment2F lineSegment = new LineSegment2F(new Point2F(10.0F, 10.0F), new Point2F(20.0F, 10.0F));
		
		assertEquals(new Point2F(10.0F, 10.0F), lineSegment.getA());
	}
	
	@Test
	public void testGetB() {
		final LineSegment2F lineSegment = new LineSegment2F(new Point2F(10.0F, 10.0F), new Point2F(20.0F, 10.0F));
		
		assertEquals(new Point2F(20.0F, 10.0F), lineSegment.getB());
	}
	
	@Test
	public void testGetID() {
		final LineSegment2F lineSegment = new LineSegment2F(new Point2F(10.0F, 10.0F), new Point2F(20.0F, 10.0F));
		
		assertEquals(LineSegment2F.ID, lineSegment.getID());
	}
	
	@Test
	public void testGetName() {
		final LineSegment2F lineSegment = new LineSegment2F(new Point2F(10.0F, 10.0F), new Point2F(20.0F, 10.0F));
		
		assertEquals(LineSegment2F.NAME, lineSegment.getName());
	}
	
	@Test
	public void testHashCode() {
		final LineSegment2F a = new LineSegment2F(new Point2F(10.0F, 10.0F), new Point2F(20.0F, 10.0F));
		final LineSegment2F b = new LineSegment2F(new Point2F(10.0F, 10.0F), new Point2F(20.0F, 10.0F));
		
		assertEquals(a.hashCode(), a.hashCode());
		assertEquals(a.hashCode(), b.hashCode());
	}
	
	@Test
	public void testToString() {
		final LineSegment2F lineSegment = new LineSegment2F(new Point2F(10.0F, 10.0F), new Point2F(20.0F, 10.0F));
		
		assertEquals("new LineSegment2F(new Point2F(10.0F, 10.0F), new Point2F(20.0F, 10.0F))", lineSegment.toString());
	}
	
	@Test
	public void testWrite() {
		final LineSegment2F a = new LineSegment2F(new Point2F(10.0F, 10.0F), new Point2F(20.0F, 10.0F));
		
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		
		final DataOutput dataOutput = new DataOutputStream(byteArrayOutputStream);
		
		a.write(dataOutput);
		
		final byte[] bytes = byteArrayOutputStream.toByteArray();
		
		final LineSegment2F b = new LineSegment2FReader().read(new DataInputStream(new ByteArrayInputStream(bytes)));
		
		assertEquals(a, b);
		
		assertThrows(NullPointerException.class, () -> a.write((DataOutput)(null)));
		assertThrows(UncheckedIOException.class, () -> a.write(new DataOutputMock()));
	}
}