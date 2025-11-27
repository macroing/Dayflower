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

import org.dayflower.geometry.Point2I;
import org.dayflower.mock.DataOutputMock;
import org.dayflower.mock.NodeHierarchicalVisitorMock;
import org.dayflower.mock.NodeVisitorMock;

import org.junit.jupiter.api.Test;

import org.macroing.java.util.visitor.NodeHierarchicalVisitor;
import org.macroing.java.util.visitor.NodeTraversalException;
import org.macroing.java.util.visitor.NodeVisitor;

@SuppressWarnings("static-method")
public final class LineSegment2IUnitTests {
	public LineSegment2IUnitTests() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Test
	public void testAcceptNodeHierarchicalVisitor() {
		final Point2I a = new Point2I(10, 10);
		final Point2I b = new Point2I(20, 10);
		
		final LineSegment2I lineSegment = new LineSegment2I(a, b);
		
		assertTrue(lineSegment.accept(new NodeHierarchicalVisitorMock(node -> false,                                                        node -> node.equals(lineSegment))));
		assertTrue(lineSegment.accept(new NodeHierarchicalVisitorMock(node -> node.equals(lineSegment),                                     node -> node.equals(lineSegment))));
		assertTrue(lineSegment.accept(new NodeHierarchicalVisitorMock(node -> node.equals(lineSegment) || node.equals(a),                   node -> node.equals(lineSegment) || node.equals(a))));
		assertTrue(lineSegment.accept(new NodeHierarchicalVisitorMock(node -> node.equals(lineSegment) || node.equals(a) || node.equals(b), node -> node.equals(lineSegment) || node.equals(a) || node.equals(b))));
		
		assertThrows(NodeTraversalException.class, () -> lineSegment.accept(new NodeHierarchicalVisitorMock(null, null)));
		assertThrows(NullPointerException.class, () -> lineSegment.accept((NodeHierarchicalVisitor)(null)));
	}
	
	@Test
	public void testAcceptNodeVisitor() {
		final LineSegment2I lineSegment = new LineSegment2I(new Point2I(10, 10), new Point2I(20, 10));
		
		assertThrows(NodeTraversalException.class, () -> lineSegment.accept(new NodeVisitorMock(true)));
		assertThrows(NullPointerException.class, () -> lineSegment.accept((NodeVisitor)(null)));
	}
	
	@Test
	public void testConstants() {
		assertEquals(2, LineSegment2I.ID);
		assertEquals("Line Segment", LineSegment2I.NAME);
	}
	
	@Test
	public void testConstructor() {
		final LineSegment2I lineSegment = new LineSegment2I(new Point2I(10, 10), new Point2I(20, 10));
		
		assertEquals(new Point2I(10, 10), lineSegment.getA());
		assertEquals(new Point2I(20, 10), lineSegment.getB());
		
		assertThrows(NullPointerException.class, () -> new LineSegment2I(new Point2I(10, 10), null));
		assertThrows(NullPointerException.class, () -> new LineSegment2I(null, new Point2I(20, 10)));
	}
	
	@Test
	public void testContainsPoint2I() {
		final List<LineSegment2I> lineSegments = LineSegment2I.fromPoints(new Point2I(10, 0), new Point2I(20, 0), new Point2I(30, 10), new Point2I(30, 20), new Point2I(20, 30), new Point2I(10, 30), new Point2I(0, 20), new Point2I(0, 10));
		
		for(final LineSegment2I lineSegment : lineSegments) {
			assertTrue(lineSegment.contains(lineSegment.getA()));
			assertTrue(lineSegment.contains(Point2I.midpoint(lineSegment.getA(), lineSegment.getB())));
			assertTrue(lineSegment.contains(lineSegment.getB()));
			
			assertFalse(lineSegment.contains(new Point2I(lineSegment.getA().x + 100, lineSegment.getA().y)));
			assertFalse(lineSegment.contains(new Point2I(lineSegment.getA().x - 100, lineSegment.getA().y)));
			
			assertFalse(lineSegment.contains(new Point2I(lineSegment.getA().x, lineSegment.getA().y + 100)));
			assertFalse(lineSegment.contains(new Point2I(lineSegment.getA().x, lineSegment.getA().y - 100)));
		}
		
		assertThrows(NullPointerException.class, () -> new LineSegment2I(new Point2I(10, 10), new Point2I(20, 10)).contains(null));
	}
	
	@Test
	public void testContainsPoint2IBoolean() {
		final List<LineSegment2I> lineSegments = LineSegment2I.fromPoints(new Point2I(10, 0), new Point2I(20, 0), new Point2I(30, 10), new Point2I(30, 20), new Point2I(20, 30), new Point2I(10, 30), new Point2I(0, 20), new Point2I(0, 10));
		
		for(final LineSegment2I lineSegment : lineSegments) {
			assertTrue(lineSegment.contains(lineSegment.getA(), false));
			assertTrue(lineSegment.contains(Point2I.midpoint(lineSegment.getA(), lineSegment.getB()), false));
			assertTrue(lineSegment.contains(lineSegment.getB(), false));
			
			assertFalse(lineSegment.contains(new Point2I(lineSegment.getA().x + 100, lineSegment.getA().y), false));
			assertFalse(lineSegment.contains(new Point2I(lineSegment.getA().x - 100, lineSegment.getA().y), false));
			
			assertFalse(lineSegment.contains(new Point2I(lineSegment.getA().x, lineSegment.getA().y + 100), false));
			assertFalse(lineSegment.contains(new Point2I(lineSegment.getA().x, lineSegment.getA().y - 100), false));
		}
		
		assertThrows(NullPointerException.class, () -> new LineSegment2I(new Point2I(10, 10), new Point2I(20, 10)).contains(null, false));
	}
	
	@Test
	public void testEquals() {
		final LineSegment2I a = new LineSegment2I(new Point2I(10, 10), new Point2I(20, 10));
		final LineSegment2I b = new LineSegment2I(new Point2I(10, 10), new Point2I(20, 10));
		final LineSegment2I c = new LineSegment2I(new Point2I(10, 10), new Point2I(30, 30));
		final LineSegment2I d = new LineSegment2I(new Point2I(30, 30), new Point2I(20, 10));
		final LineSegment2I e = null;
		
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
	public void testFindPointsBoolean() {
		final LineSegment2I lineSegmentA = new LineSegment2I(new Point2I(10, 10), new Point2I(15, 10));
		final LineSegment2I lineSegmentB = new LineSegment2I(new Point2I(10, 10), new Point2I(10, 15));
		final LineSegment2I lineSegmentC = new LineSegment2I(new Point2I(15, 10), new Point2I(10, 10));
		final LineSegment2I lineSegmentD = new LineSegment2I(new Point2I(10, 15), new Point2I(10, 10));
		final LineSegment2I lineSegmentE = new LineSegment2I(new Point2I(10, 10), new Point2I(15, 12));
		
		final List<Point2I> lineSegmentAPoints = lineSegmentA.findPoints(false);
		final List<Point2I> lineSegmentBPoints = lineSegmentB.findPoints(false);
		final List<Point2I> lineSegmentCPoints = lineSegmentC.findPoints(false);
		final List<Point2I> lineSegmentDPoints = lineSegmentD.findPoints(false);
		final List<Point2I> lineSegmentEPoints = lineSegmentE.findPoints(false);
		
		assertNotNull(lineSegmentAPoints);
		assertNotNull(lineSegmentBPoints);
		assertNotNull(lineSegmentCPoints);
		assertNotNull(lineSegmentDPoints);
		assertNotNull(lineSegmentEPoints);
		
		assertEquals(6, lineSegmentAPoints.size());
		assertEquals(6, lineSegmentBPoints.size());
		assertEquals(6, lineSegmentCPoints.size());
		assertEquals(6, lineSegmentDPoints.size());
		assertEquals(6, lineSegmentEPoints.size());
		
		assertEquals(new Point2I(10, 10), lineSegmentAPoints.get(0));
		assertEquals(new Point2I(11, 10), lineSegmentAPoints.get(1));
		assertEquals(new Point2I(12, 10), lineSegmentAPoints.get(2));
		assertEquals(new Point2I(13, 10), lineSegmentAPoints.get(3));
		assertEquals(new Point2I(14, 10), lineSegmentAPoints.get(4));
		assertEquals(new Point2I(15, 10), lineSegmentAPoints.get(5));
		
		assertEquals(new Point2I(10, 10), lineSegmentBPoints.get(0));
		assertEquals(new Point2I(10, 11), lineSegmentBPoints.get(1));
		assertEquals(new Point2I(10, 12), lineSegmentBPoints.get(2));
		assertEquals(new Point2I(10, 13), lineSegmentBPoints.get(3));
		assertEquals(new Point2I(10, 14), lineSegmentBPoints.get(4));
		assertEquals(new Point2I(10, 15), lineSegmentBPoints.get(5));
		
		assertEquals(new Point2I(15, 10), lineSegmentCPoints.get(0));
		assertEquals(new Point2I(14, 10), lineSegmentCPoints.get(1));
		assertEquals(new Point2I(13, 10), lineSegmentCPoints.get(2));
		assertEquals(new Point2I(12, 10), lineSegmentCPoints.get(3));
		assertEquals(new Point2I(11, 10), lineSegmentCPoints.get(4));
		assertEquals(new Point2I(10, 10), lineSegmentCPoints.get(5));
		
		assertEquals(new Point2I(10, 15), lineSegmentDPoints.get(0));
		assertEquals(new Point2I(10, 14), lineSegmentDPoints.get(1));
		assertEquals(new Point2I(10, 13), lineSegmentDPoints.get(2));
		assertEquals(new Point2I(10, 12), lineSegmentDPoints.get(3));
		assertEquals(new Point2I(10, 11), lineSegmentDPoints.get(4));
		assertEquals(new Point2I(10, 10), lineSegmentDPoints.get(5));
		
		assertEquals(new Point2I(10, 10), lineSegmentEPoints.get(0));
		assertEquals(new Point2I(11, 10), lineSegmentEPoints.get(1));
		assertEquals(new Point2I(12, 11), lineSegmentEPoints.get(2));
		assertEquals(new Point2I(13, 11), lineSegmentEPoints.get(3));
		assertEquals(new Point2I(14, 12), lineSegmentEPoints.get(4));
		assertEquals(new Point2I(15, 12), lineSegmentEPoints.get(5));
	}
	
	@Test
	public void testFindPointsOfComplementShape2IBoolean() {
		final LineSegment2I a = new LineSegment2I(new Point2I(10, 10), new Point2I(20, 10));
		final LineSegment2I b = new LineSegment2I(new Point2I(10, 10), new Point2I(21, 10));
		
		final List<Point2I> points = a.findPointsOfComplement(b, false);
		
		assertNotNull(points);
		
		assertEquals(1, points.size());
		
		assertEquals(new Point2I(21, 10), points.get(0));
		
		assertThrows(NullPointerException.class, () -> a.findPointsOfComplement(null, false));
	}
	
	@Test
	public void testFromPoints() {
		final List<LineSegment2I> lineSegments = LineSegment2I.fromPoints(new Point2I(0, 0), new Point2I(10, 0), new Point2I(10, 10), new Point2I(0, 10));
		
		assertNotNull(lineSegments);
		
		assertEquals(4, lineSegments.size());
		
		assertEquals(new LineSegment2I(new Point2I( 0,  0), new Point2I(10,  0)), lineSegments.get(0));
		assertEquals(new LineSegment2I(new Point2I(10,  0), new Point2I(10, 10)), lineSegments.get(1));
		assertEquals(new LineSegment2I(new Point2I(10, 10), new Point2I( 0, 10)), lineSegments.get(2));
		assertEquals(new LineSegment2I(new Point2I( 0, 10), new Point2I( 0,  0)), lineSegments.get(3));
		
		assertThrows(IllegalArgumentException.class, () -> LineSegment2I.fromPoints(new Point2I()));
		assertThrows(NullPointerException.class, () -> LineSegment2I.fromPoints((Point2I[])(null)));
		assertThrows(NullPointerException.class, () -> LineSegment2I.fromPoints(new Point2I[] {new Point2I(), null}));
	}
	
	@Test
	public void testGetA() {
		final LineSegment2I lineSegment = new LineSegment2I(new Point2I(10, 10), new Point2I(20, 10));
		
		assertEquals(new Point2I(10, 10), lineSegment.getA());
	}
	
	@Test
	public void testGetB() {
		final LineSegment2I lineSegment = new LineSegment2I(new Point2I(10, 10), new Point2I(20, 10));
		
		assertEquals(new Point2I(20, 10), lineSegment.getB());
	}
	
	@Test
	public void testGetID() {
		final LineSegment2I lineSegment = new LineSegment2I(new Point2I(10, 10), new Point2I(20, 10));
		
		assertEquals(LineSegment2I.ID, lineSegment.getID());
	}
	
	@Test
	public void testGetMaximum() {
		final LineSegment2I lineSegment = new LineSegment2I(new Point2I(10, 10), new Point2I(20, 10));
		
		assertEquals(new Point2I(20, 10), lineSegment.getMaximum());
	}
	
	@Test
	public void testGetMinimum() {
		final LineSegment2I lineSegment = new LineSegment2I(new Point2I(10, 10), new Point2I(20, 10));
		
		assertEquals(new Point2I(10, 10), lineSegment.getMinimum());
	}
	
	@Test
	public void testGetName() {
		final LineSegment2I lineSegment = new LineSegment2I(new Point2I(10, 10), new Point2I(20, 10));
		
		assertEquals(LineSegment2I.NAME, lineSegment.getName());
	}
	
	@Test
	public void testHashCode() {
		final LineSegment2I a = new LineSegment2I(new Point2I(10, 10), new Point2I(20, 10));
		final LineSegment2I b = new LineSegment2I(new Point2I(10, 10), new Point2I(20, 10));
		
		assertEquals(a.hashCode(), a.hashCode());
		assertEquals(a.hashCode(), b.hashCode());
	}
	
	@Test
	public void testToString() {
		final LineSegment2I lineSegment = new LineSegment2I(new Point2I(10, 10), new Point2I(20, 10));
		
		assertEquals("new LineSegment2I(new Point2I(10, 10), new Point2I(20, 10))", lineSegment.toString());
	}
	
	@Test
	public void testWrite() {
		final LineSegment2I a = new LineSegment2I(new Point2I(10, 10), new Point2I(20, 10));
		
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		
		final DataOutput dataOutput = new DataOutputStream(byteArrayOutputStream);
		
		a.write(dataOutput);
		
		final byte[] bytes = byteArrayOutputStream.toByteArray();
		
		final LineSegment2I b = new LineSegment2IReader().read(new DataInputStream(new ByteArrayInputStream(bytes)));
		
		assertEquals(a, b);
		
		assertThrows(NullPointerException.class, () -> a.write((DataOutput)(null)));
		assertThrows(UncheckedIOException.class, () -> a.write(new DataOutputMock()));
	}
}