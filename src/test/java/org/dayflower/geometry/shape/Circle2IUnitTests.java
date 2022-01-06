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
import org.dayflower.geometry.Point2F;
import org.dayflower.geometry.Point2I;
import org.dayflower.geometry.Shape2I;
import org.dayflower.mock.DataOutputMock;
import org.dayflower.mock.NodeHierarchicalVisitorMock;
import org.dayflower.mock.NodeVisitorMock;
import org.dayflower.node.NodeHierarchicalVisitor;
import org.dayflower.node.NodeTraversalException;
import org.dayflower.node.NodeVisitor;
import org.junit.jupiter.api.Test;

@SuppressWarnings("static-method")
public final class Circle2IUnitTests {
	public Circle2IUnitTests() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Test
	public void testAcceptNodeHierarchicalVisitor() {
		final Point2I center = new Point2I(10, 10);
		
		final Circle2I circle = new Circle2I(center, 20);
		
		assertTrue(circle.accept(new NodeHierarchicalVisitorMock(node -> false,                                      node -> node.equals(circle))));
		assertTrue(circle.accept(new NodeHierarchicalVisitorMock(node -> node.equals(circle),                        node -> node.equals(circle))));
		assertTrue(circle.accept(new NodeHierarchicalVisitorMock(node -> node.equals(circle) || node.equals(center), node -> node.equals(circle) || node.equals(center))));
		
		assertThrows(NodeTraversalException.class, () -> circle.accept(new NodeHierarchicalVisitorMock(null, null)));
		assertThrows(NullPointerException.class, () -> circle.accept((NodeHierarchicalVisitor)(null)));
	}
	
	@Test
	public void testAcceptNodeVisitor() {
		final Circle2I circle = new Circle2I();
		
		assertThrows(NodeTraversalException.class, () -> circle.accept(new NodeVisitorMock(true)));
		assertThrows(NullPointerException.class, () -> circle.accept((NodeVisitor)(null)));
	}
	
	@Test
	public void testConstants() {
		assertEquals(1, Circle2I.ID);
		assertEquals("Circle", Circle2I.NAME);
	}
	
	@Test
	public void testConstructor() {
		final Circle2I circle = new Circle2I();
		
		assertEquals(new Point2I(), circle.getCenter());
		assertEquals(10, circle.getRadius());
	}
	
	@Test
	public void testConstructorCircle2D() {
		final Circle2I circle = new Circle2I(new Circle2D(new Point2D(10.0D, 10.0D), 20.0D));
		
		assertEquals(new Point2I(10, 10), circle.getCenter());
		assertEquals(20, circle.getRadius());
		
		assertThrows(NullPointerException.class, () -> new Circle2I((Circle2D)(null)));
	}
	
	@Test
	public void testConstructorCircle2F() {
		final Circle2I circle = new Circle2I(new Circle2F(new Point2F(10.0F, 10.0F), 20.0F));
		
		assertEquals(new Point2I(10, 10), circle.getCenter());
		assertEquals(20, circle.getRadius());
		
		assertThrows(NullPointerException.class, () -> new Circle2I((Circle2F)(null)));
	}
	
	@Test
	public void testConstructorPoint2I() {
		final Circle2I circle = new Circle2I(new Point2I(10, 10));
		
		assertEquals(new Point2I(10, 10), circle.getCenter());
		assertEquals(10.0D, circle.getRadius());
		
		assertThrows(NullPointerException.class, () -> new Circle2I((Point2I)(null)));
	}
	
	@Test
	public void testConstructorPoint2IInt() {
		final Circle2I circle = new Circle2I(new Point2I(10, 10), 20);
		
		assertEquals(new Point2I(10, 10), circle.getCenter());
		assertEquals(20, circle.getRadius());
		
		assertThrows(NullPointerException.class, () -> new Circle2I(null, 20));
	}
	
	@Test
	public void testContains() {
		final Circle2I circle = new Circle2I(new Point2I(0, 0), 10);
		
		assertTrue(circle.contains(new Point2I(+10, 0)));
		assertTrue(circle.contains(new Point2I(-10, 0)));
		
		assertTrue(circle.contains(new Point2I(0, +10)));
		assertTrue(circle.contains(new Point2I(0, -10)));
		
		assertTrue(circle.contains(new Point2I(0, 0)));
		
		assertFalse(circle.contains(new Point2I(10, 10)));
		
		assertThrows(NullPointerException.class, () -> circle.contains(null));
	}
	
	@Test
	public void testContainsBoolean() {
		final Circle2I circle = new Circle2I(new Point2I(0, 0), 10);
		
		assertTrue(circle.contains(new Point2I(+10, 0), false));
		assertTrue(circle.contains(new Point2I(+10, 0), true));
		assertTrue(circle.contains(new Point2I(-10, 0), false));
		assertTrue(circle.contains(new Point2I(-10, 0), true));
		
		assertTrue(circle.contains(new Point2I(0, +10), false));
		assertTrue(circle.contains(new Point2I(0, +10), true));
		assertTrue(circle.contains(new Point2I(0, -10), false));
		assertTrue(circle.contains(new Point2I(0, -10), true));
		
		assertTrue(circle.contains(new Point2I(0, 0), false));
		
		assertFalse(circle.contains(new Point2I(0, 0), true));
		
		assertFalse(circle.contains(new Point2I(10, 10), false));
		assertFalse(circle.contains(new Point2I(10, 10), true));
		
		assertThrows(NullPointerException.class, () -> circle.contains(null, false));
	}
	
	@Test
	public void testContainsDifference() {
		final Circle2I a = new Circle2I(new Point2I(10, 10), 10);
		final Circle2I b = new Circle2I(new Point2I(30, 10), 10);
		
		assertTrue(Shape2I.containsDifference(new Point2I(10, 10), a, b));
		
		assertFalse(Shape2I.containsDifference(new Point2I(20, 10), a, b));
		assertFalse(Shape2I.containsDifference(new Point2I(30, 10), a, b));
		assertFalse(Shape2I.containsDifference(new Point2I(50, 10), a, b));
		
		assertThrows(NullPointerException.class, () -> Shape2I.containsDifference(new Point2I(), a, null));
		assertThrows(NullPointerException.class, () -> Shape2I.containsDifference(new Point2I(), null, b));
		assertThrows(NullPointerException.class, () -> Shape2I.containsDifference(null, a, b));
	}
	
	@Test
	public void testContainsIntersection() {
		final Circle2I a = new Circle2I(new Point2I(10, 10), 10);
		final Circle2I b = new Circle2I(new Point2I(20, 10), 10);
		
		assertTrue(Shape2I.containsIntersection(new Point2I(15, 10), a, b));
		
		assertFalse(Shape2I.containsIntersection(new Point2I( 0, 10), a, b));
		assertFalse(Shape2I.containsIntersection(new Point2I(30, 10), a, b));
		assertFalse(Shape2I.containsIntersection(new Point2I(50, 10), a, b));
		
		assertThrows(NullPointerException.class, () -> Shape2I.containsIntersection(new Point2I(), a, null));
		assertThrows(NullPointerException.class, () -> Shape2I.containsIntersection(new Point2I(), null, b));
		assertThrows(NullPointerException.class, () -> Shape2I.containsIntersection(null, a, b));
	}
	
	@Test
	public void testContainsUnion() {
		final Circle2I a = new Circle2I(new Point2I(10, 10), 10);
		final Circle2I b = new Circle2I(new Point2I(20, 10), 10);
		
		assertTrue(Shape2I.containsUnion(new Point2I( 0, 10), a, b));
		assertTrue(Shape2I.containsUnion(new Point2I(15, 10), a, b));
		assertTrue(Shape2I.containsUnion(new Point2I(30, 10), a, b));
		
		assertFalse(Shape2I.containsUnion(new Point2I(50, 10), a, b));
		
		assertThrows(NullPointerException.class, () -> Shape2I.containsUnion(new Point2I(), a, null));
		assertThrows(NullPointerException.class, () -> Shape2I.containsUnion(new Point2I(), null, b));
		assertThrows(NullPointerException.class, () -> Shape2I.containsUnion(null, a, b));
	}
	
	@Test
	public void testEquals() {
		final Circle2I a = new Circle2I(new Point2I(10, 10), 20);
		final Circle2I b = new Circle2I(new Point2I(10, 10), 20);
		final Circle2I c = new Circle2I(new Point2I(10, 10), 30);
		final Circle2I d = new Circle2I(new Point2I(20, 20), 20);
		final Circle2I e = null;
		
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
	public void testFindPoints() {
		final Circle2I circle = new Circle2I();
		
		final List<Point2I> points = circle.findPoints();
		
		assertNotNull(points);
		
		for(final Point2I point : points) {
			assertNotNull(point);
			
			assertTrue(circle.contains(point));
		}
	}
	
	@Test
	public void testFindPointsBoolean() {
		final Circle2I circle = new Circle2I();
		
		final List<Point2I> points = circle.findPoints(false);
		final List<Point2I> pointsIncludingBorderOnly = circle.findPoints(true);
		
		assertNotNull(points);
		assertNotNull(pointsIncludingBorderOnly);
		
		for(final Point2I point : points) {
			assertNotNull(point);
			
			assertTrue(circle.contains(point, false));
		}
		
		for(final Point2I point : pointsIncludingBorderOnly) {
			assertNotNull(point);
			
			assertTrue(circle.contains(point, true));
		}
	}
	
	@Test
	public void testFindPointsOfComplementShape2I() {
		final Circle2I circle = new Circle2I();
		
		final List<Point2I> pointsOfComplement = circle.findPointsOfComplement(circle);
		
		assertNotNull(pointsOfComplement);
		
		assertTrue(pointsOfComplement.isEmpty());
		
		assertThrows(NullPointerException.class, () -> circle.findPointsOfComplement(null));
	}
	
	@Test
	public void testFindPointsOfComplementShape2IBoolean() {
		final Circle2I circle = new Circle2I();
		
		final List<Point2I> points = circle.findPoints();
		final List<Point2I> pointsIncludingBorderOnly = circle.findPoints(true);
		final List<Point2I> pointsOfComplement = circle.findPointsOfComplement(circle, false);
		final List<Point2I> pointsOfComplementExcludingBorderOnly = circle.findPointsOfComplement(circle, true);
		
		points.removeAll(pointsIncludingBorderOnly);
		
		assertNotNull(pointsOfComplement);
		assertNotNull(pointsOfComplementExcludingBorderOnly);
		
		assertTrue(pointsOfComplement.isEmpty());
		
		assertEquals(points, pointsOfComplementExcludingBorderOnly);
		
		assertThrows(NullPointerException.class, () -> circle.findPointsOfComplement(null, false));
	}
	
	@Test
	public void testFindPointsOfIntersectionShape2I() {
		final Circle2I circle = new Circle2I();
		
		final List<Point2I> points = circle.findPoints();
		final List<Point2I> pointsOfIntersection = circle.findPointsOfIntersection(circle);
		
		assertNotNull(pointsOfIntersection);
		
		assertEquals(points, pointsOfIntersection);
		
		assertThrows(NullPointerException.class, () -> circle.findPointsOfIntersection(null));
	}
	
	@Test
	public void testFindPointsOfIntersectionShape2IBoolean() {
		final Circle2I circle = new Circle2I();
		
		final List<Point2I> points = circle.findPoints();
		final List<Point2I> pointsIncludingBorderOnly = circle.findPoints(true);
		final List<Point2I> pointsOfIntersection = circle.findPointsOfIntersection(circle, false);
		final List<Point2I> pointsOfIntersectionIncludingBorderOnly = circle.findPointsOfIntersection(circle, true);
		
		assertNotNull(pointsOfIntersection);
		assertNotNull(pointsOfIntersectionIncludingBorderOnly);
		
		assertEquals(points, pointsOfIntersection);
		assertEquals(pointsIncludingBorderOnly, pointsOfIntersectionIncludingBorderOnly);
		
		assertThrows(NullPointerException.class, () -> circle.findPointsOfIntersection(null, false));
	}
	
	@Test
	public void testGetCenter() {
		final Circle2I circle = new Circle2I(new Point2I(10, 10));
		
		assertEquals(new Point2I(10, 10), circle.getCenter());
	}
	
	@Test
	public void testGetID() {
		final Circle2I circle = new Circle2I();
		
		assertEquals(Circle2I.ID, circle.getID());
	}
	
	@Test
	public void testGetMaximum() {
		final Circle2I circle = new Circle2I(new Point2I(20, 20), 10);
		
		assertEquals(new Point2I(30, 30), circle.getMaximum());
	}
	
	@Test
	public void testGetMinimum() {
		final Circle2I circle = new Circle2I(new Point2I(20, 20), 10);
		
		assertEquals(new Point2I(10, 10), circle.getMinimum());
	}
	
	@Test
	public void testGetName() {
		final Circle2I circle = new Circle2I();
		
		assertEquals(Circle2I.NAME, circle.getName());
	}
	
	@Test
	public void testGetRadius() {
		final Circle2I circle = new Circle2I(new Point2I(), 20);
		
		assertEquals(20, circle.getRadius());
	}
	
	@Test
	public void testHashCode() {
		final Circle2I a = new Circle2I();
		final Circle2I b = new Circle2I();
		
		assertEquals(a.hashCode(), a.hashCode());
		assertEquals(a.hashCode(), b.hashCode());
	}
	
	@Test
	public void testToString() {
		final Circle2I circle = new Circle2I(new Point2I(10, 10), 20);
		
		assertEquals("new Circle2I(new Point2I(10, 10), 20)", circle.toString());
	}
	
	@Test
	public void testWrite() {
		final Circle2I a = new Circle2I(new Point2I(10, 10), 20);
		
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		
		final DataOutput dataOutput = new DataOutputStream(byteArrayOutputStream);
		
		a.write(dataOutput);
		
		final byte[] bytes = byteArrayOutputStream.toByteArray();
		
		final Circle2I b = new Circle2IReader().read(new DataInputStream(new ByteArrayInputStream(bytes)));
		
		assertEquals(a, b);
		
		assertThrows(NullPointerException.class, () -> a.write((DataOutput)(null)));
		assertThrows(UncheckedIOException.class, () -> a.write(new DataOutputMock()));
	}
}