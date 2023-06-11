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

import org.dayflower.geometry.Point2D;
import org.dayflower.mock.DataOutputMock;
import org.dayflower.mock.NodeHierarchicalVisitorMock;
import org.dayflower.mock.NodeVisitorMock;

import org.junit.jupiter.api.Test;

import org.macroing.java.util.visitor.NodeHierarchicalVisitor;
import org.macroing.java.util.visitor.NodeTraversalException;
import org.macroing.java.util.visitor.NodeVisitor;

@SuppressWarnings("static-method")
public final class LineSegment2DUnitTests {
	public LineSegment2DUnitTests() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Test
	public void testAcceptNodeHierarchicalVisitor() {
		final Point2D a = new Point2D(10.0D, 10.0D);
		final Point2D b = new Point2D(20.0D, 10.0D);
		
		final LineSegment2D lineSegment = new LineSegment2D(a, b);
		
		assertTrue(lineSegment.accept(new NodeHierarchicalVisitorMock(node -> false,                                                        node -> node.equals(lineSegment))));
		assertTrue(lineSegment.accept(new NodeHierarchicalVisitorMock(node -> node.equals(lineSegment),                                     node -> node.equals(lineSegment))));
		assertTrue(lineSegment.accept(new NodeHierarchicalVisitorMock(node -> node.equals(lineSegment) || node.equals(a),                   node -> node.equals(lineSegment) || node.equals(a))));
		assertTrue(lineSegment.accept(new NodeHierarchicalVisitorMock(node -> node.equals(lineSegment) || node.equals(a) || node.equals(b), node -> node.equals(lineSegment) || node.equals(a) || node.equals(b))));
		
		assertThrows(NodeTraversalException.class, () -> lineSegment.accept(new NodeHierarchicalVisitorMock(null, null)));
		assertThrows(NullPointerException.class, () -> lineSegment.accept((NodeHierarchicalVisitor)(null)));
	}
	
	@Test
	public void testAcceptNodeVisitor() {
		final LineSegment2D lineSegment = new LineSegment2D(new Point2D(10.0D, 10.0D), new Point2D(20.0D, 10.0D));
		
		assertThrows(NodeTraversalException.class, () -> lineSegment.accept(new NodeVisitorMock(true)));
		assertThrows(NullPointerException.class, () -> lineSegment.accept((NodeVisitor)(null)));
	}
	
	@Test
	public void testConstants() {
		assertEquals(2, LineSegment2D.ID);
		assertEquals("Line Segment", LineSegment2D.NAME);
	}
	
	@Test
	public void testConstructor() {
		final LineSegment2D lineSegment = new LineSegment2D(new Point2D(10.0D, 10.0D), new Point2D(20.0D, 10.0D));
		
		assertEquals(new Point2D(10.0D, 10.0D), lineSegment.getA());
		assertEquals(new Point2D(20.0D, 10.0D), lineSegment.getB());
		
		assertThrows(NullPointerException.class, () -> new LineSegment2D(new Point2D(10.0D, 10.0D), null));
		assertThrows(NullPointerException.class, () -> new LineSegment2D(null, new Point2D(20.0D, 10.0D)));
	}
	
	@Test
	public void testContains() {
		final List<LineSegment2D> lineSegments = LineSegment2D.fromPoints(new Point2D(10.0D, 0.0D), new Point2D(20.0D, 0.0D), new Point2D(30.0D, 10.0D), new Point2D(30.0D, 20.0D), new Point2D(20.0D, 30.0D), new Point2D(10.0D, 30.0D), new Point2D(0.0D, 20.0D), new Point2D(0.0D, 10.0D));
		
		for(final LineSegment2D lineSegment : lineSegments) {
			assertTrue(lineSegment.contains(lineSegment.getA()));
			assertTrue(lineSegment.contains(Point2D.midpoint(lineSegment.getA(), lineSegment.getB())));
			assertTrue(lineSegment.contains(lineSegment.getB()));
			
			assertFalse(lineSegment.contains(new Point2D(lineSegment.getA().x + 100.0D, lineSegment.getA().y)));
			assertFalse(lineSegment.contains(new Point2D(lineSegment.getA().x - 100.0D, lineSegment.getA().y)));
			
			assertFalse(lineSegment.contains(new Point2D(lineSegment.getA().x, lineSegment.getA().y + 100.0D)));
			assertFalse(lineSegment.contains(new Point2D(lineSegment.getA().x, lineSegment.getA().y - 100.0D)));
		}
		
		assertThrows(NullPointerException.class, () -> new LineSegment2D(new Point2D(10.0D, 10.0D), new Point2D(20.0D, 10.0D)).contains(null));
	}
	
	@Test
	public void testEquals() {
		final LineSegment2D a = new LineSegment2D(new Point2D(10.0D, 10.0D), new Point2D(20.0D, 10.0D));
		final LineSegment2D b = new LineSegment2D(new Point2D(10.0D, 10.0D), new Point2D(20.0D, 10.0D));
		final LineSegment2D c = new LineSegment2D(new Point2D(10.0D, 10.0D), new Point2D(30.0D, 30.0D));
		final LineSegment2D d = new LineSegment2D(new Point2D(30.0D, 30.0D), new Point2D(20.0D, 10.0D));
		final LineSegment2D e = null;
		
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
		final List<LineSegment2D> lineSegments = LineSegment2D.fromPoints(new Point2D(0.0D, 0.0D), new Point2D(10.0D, 0.0D), new Point2D(10.0D, 10.0D), new Point2D(0.0D, 10.0D));
		
		assertNotNull(lineSegments);
		
		assertEquals(4, lineSegments.size());
		
		assertEquals(new LineSegment2D(new Point2D( 0.0D,  0.0D), new Point2D(10.0D,  0.0D)), lineSegments.get(0));
		assertEquals(new LineSegment2D(new Point2D(10.0D,  0.0D), new Point2D(10.0D, 10.0D)), lineSegments.get(1));
		assertEquals(new LineSegment2D(new Point2D(10.0D, 10.0D), new Point2D( 0.0D, 10.0D)), lineSegments.get(2));
		assertEquals(new LineSegment2D(new Point2D( 0.0D, 10.0D), new Point2D( 0.0D,  0.0D)), lineSegments.get(3));
		
		assertThrows(IllegalArgumentException.class, () -> LineSegment2D.fromPoints(new Point2D()));
		assertThrows(NullPointerException.class, () -> LineSegment2D.fromPoints((Point2D[])(null)));
		assertThrows(NullPointerException.class, () -> LineSegment2D.fromPoints(new Point2D[] {new Point2D(), null}));
	}
	
	@Test
	public void testGetA() {
		final LineSegment2D lineSegment = new LineSegment2D(new Point2D(10.0D, 10.0D), new Point2D(20.0D, 10.0D));
		
		assertEquals(new Point2D(10.0D, 10.0D), lineSegment.getA());
	}
	
	@Test
	public void testGetB() {
		final LineSegment2D lineSegment = new LineSegment2D(new Point2D(10.0D, 10.0D), new Point2D(20.0D, 10.0D));
		
		assertEquals(new Point2D(20.0D, 10.0D), lineSegment.getB());
	}
	
	@Test
	public void testGetID() {
		final LineSegment2D lineSegment = new LineSegment2D(new Point2D(10.0D, 10.0D), new Point2D(20.0D, 10.0D));
		
		assertEquals(LineSegment2D.ID, lineSegment.getID());
	}
	
	@Test
	public void testGetName() {
		final LineSegment2D lineSegment = new LineSegment2D(new Point2D(10.0D, 10.0D), new Point2D(20.0D, 10.0D));
		
		assertEquals(LineSegment2D.NAME, lineSegment.getName());
	}
	
	@Test
	public void testHashCode() {
		final LineSegment2D a = new LineSegment2D(new Point2D(10.0D, 10.0D), new Point2D(20.0D, 10.0D));
		final LineSegment2D b = new LineSegment2D(new Point2D(10.0D, 10.0D), new Point2D(20.0D, 10.0D));
		
		assertEquals(a.hashCode(), a.hashCode());
		assertEquals(a.hashCode(), b.hashCode());
	}
	
	@Test
	public void testToString() {
		final LineSegment2D lineSegment = new LineSegment2D(new Point2D(10.0D, 10.0D), new Point2D(20.0D, 10.0D));
		
		assertEquals("new LineSegment2D(new Point2D(10.0D, 10.0D), new Point2D(20.0D, 10.0D))", lineSegment.toString());
	}
	
	@Test
	public void testWrite() {
		final LineSegment2D a = new LineSegment2D(new Point2D(10.0D, 10.0D), new Point2D(20.0D, 10.0D));
		
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		
		final DataOutput dataOutput = new DataOutputStream(byteArrayOutputStream);
		
		a.write(dataOutput);
		
		final byte[] bytes = byteArrayOutputStream.toByteArray();
		
		final LineSegment2D b = new LineSegment2DReader().read(new DataInputStream(new ByteArrayInputStream(bytes)));
		
		assertEquals(a, b);
		
		assertThrows(NullPointerException.class, () -> a.write((DataOutput)(null)));
		assertThrows(UncheckedIOException.class, () -> a.write(new DataOutputMock()));
	}
}