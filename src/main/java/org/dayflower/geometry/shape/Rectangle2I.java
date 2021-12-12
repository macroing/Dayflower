/**
 * Copyright 2014 - 2021 J&#246;rgen Lundgren
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

import static org.dayflower.utility.Ints.abs;
import static org.dayflower.utility.Ints.max;
import static org.dayflower.utility.Ints.min;

import java.io.DataOutput;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.Field;//TODO: Add Unit Tests!
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.dayflower.geometry.Point2I;
import org.dayflower.geometry.Shape2I;
import org.dayflower.node.NodeHierarchicalVisitor;
import org.dayflower.node.NodeTraversalException;
import org.dayflower.utility.ParameterArguments;

/**
 * A {@code Rectangle2I} is an implementation of {@link Shape2I} that represents a rectangle.
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class Rectangle2I implements Shape2I {
	/**
	 * The name of this {@code Rectangle2I} class.
	 */
	public static final String NAME = "Rectangle";
	
	/**
	 * The ID of this {@code Rectangle2I} class.
	 */
	public static final int ID = 4;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final List<LineSegment2I> lineSegments;
	private final Point2I a;
	private final Point2I b;
	private final Point2I c;
	private final Point2I d;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code Rectangle2I} instance that contains {@code circle}.
	 * <p>
	 * If {@code circle} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param circle a {@link Circle2I} instance
	 * @throws NullPointerException thrown if, and only if, {@code circle} is {@code null}
	 */
	public Rectangle2I(final Circle2I circle) {
		this(new Point2I(circle.getCenter().getX() - circle.getRadius(), circle.getCenter().getY() - circle.getRadius()), new Point2I(circle.getCenter().getX() + circle.getRadius(), circle.getCenter().getY() + circle.getRadius()));
	}
	
	/**
	 * Constructs a new {@code Rectangle2I} instance based on {@code x} and {@code y}.
	 * <p>
	 * If either {@code x} or {@code y} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param x a {@link Point2I} instance
	 * @param y a {@code Point2I} instance
	 * @throws NullPointerException thrown if, and only if, either {@code x} or {@code y} are {@code null}
	 */
	public Rectangle2I(final Point2I x, final Point2I y) {
		this.a = new Point2I(min(x.getX(), y.getX()), min(x.getY(), y.getY()));
		this.b = new Point2I(min(x.getX(), y.getX()), max(x.getY(), y.getY()));
		this.c = new Point2I(max(x.getX(), y.getX()), max(x.getY(), y.getY()));
		this.d = new Point2I(max(x.getX(), y.getX()), min(x.getY(), y.getY()));
		this.lineSegments = LineSegment2I.fromPoints(this.a, this.b, this.c, this.d);
	}
	
	/**
	 * Constructs a new {@code Rectangle2I} instance based on {@code a}, {@code b}, {@code c} and {@code d}.
	 * <p>
	 * If either {@code a}, {@code b}, {@code c} or {@code d} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code a}, {@code b}, {@code c} and {@code d} does not form a rectangle, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param a a {@link Point2I} instance
	 * @param b a {@code Point2I} instance
	 * @param c a {@code Point2I} instance
	 * @param d a {@code Point2I} instance
	 * @throws IllegalArgumentException thrown if, and only if, {@code a}, {@code b}, {@code c} and {@code d} does not form a rectangle
	 * @throws NullPointerException thrown if, and only if, either {@code a}, {@code b}, {@code c} or {@code d} are {@code null}
	 */
	public Rectangle2I(final Point2I a, final Point2I b, final Point2I c, final Point2I d) {
		doCheckPointValidity(a, b, c, d);
		
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
		this.lineSegments = LineSegment2I.fromPoints(this.a, this.b, this.c, this.d);
	}
	
	/**
	 * Constructs a new {@code Rectangle2I} instance from {@code rectangle}.
	 * <p>
	 * If {@code rectangle} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param rectangle a {@link Rectangle2D} instance
	 * @throws NullPointerException thrown if, and only if, {@code rectangle} is {@code null}
	 */
	public Rectangle2I(final Rectangle2D rectangle) {
		this.a = new Point2I(rectangle.getA());
		this.b = new Point2I(rectangle.getB());
		this.c = new Point2I(rectangle.getC());
		this.d = new Point2I(rectangle.getD());
		this.lineSegments = LineSegment2I.fromPoints(this.a, this.b, this.c, this.d);
	}
	
	/**
	 * Constructs a new {@code Rectangle2I} instance from {@code rectangle}.
	 * <p>
	 * If {@code rectangle} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param rectangle a {@link Rectangle2F} instance
	 * @throws NullPointerException thrown if, and only if, {@code rectangle} is {@code null}
	 */
	public Rectangle2I(final Rectangle2F rectangle) {
		this.a = new Point2I(rectangle.getA());
		this.b = new Point2I(rectangle.getB());
		this.c = new Point2I(rectangle.getC());
		this.d = new Point2I(rectangle.getD());
		this.lineSegments = LineSegment2I.fromPoints(this.a, this.b, this.c, this.d);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code List} that contains {@link LineSegment2I} instances that connects all {@link Point2I} instances in this {@code Rectangle2I} instance.
	 * 
	 * @return a {@code List} that contains {@code LineSegment2I} instances that connects all {@link Point2I} instances in this {@code Rectangle2I} instance
	 */
	public List<LineSegment2I> getLineSegments() {
		return new ArrayList<>(this.lineSegments);
	}
	
	/**
	 * Returns a {@code List} with {@link Point2I} instances contained in this {@code Rectangle2I} instance.
	 * 
	 * @param isIncludingBorderOnly {@code true} if, and only if, this method should only include {@code Point2I} instances on the border of this {@code Rectangle2I} instance, {@code false} otherwise
	 * @return a {@code List} with {@code Point2I} instances contained in this {@code Rectangle2I} instance
	 */
	@Override
	public List<Point2I> findPoints(final boolean isIncludingBorderOnly) {
		final List<Point2I> points = new ArrayList<>();
		
		final Point2I maximum = Point2I.maximum(Point2I.maximum(this.a, this.b), Point2I.maximum(this.c, this.d));
		final Point2I minimum = Point2I.minimum(Point2I.minimum(this.a, this.b), Point2I.minimum(this.c, this.d));
		
		final int maximumX = maximum.getX();
		final int minimumX = minimum.getX();
		final int maximumY = maximum.getY();
		final int minimumY = minimum.getY();
		
		for(int y = minimumY; y <= maximumY; y++) {
			for(int x = minimumX; x <= maximumX; x++) {
				final Point2I point = new Point2I(x, y);
				
				if(contains(point, isIncludingBorderOnly)) {
					points.add(point);
				}
			}
		}
		
		return points;
	}
	
	/**
	 * Returns the {@link Point2I} instance denoted by A.
	 * <p>
	 * This {@code Point2I} instance usually contains the minimum X and minimum Y component values.
	 * 
	 * @return the {@code Point2I} instance denoted by A
	 */
	public Point2I getA() {
		return this.a;
	}
	
	/**
	 * Returns the {@link Point2I} instance denoted by B.
	 * <p>
	 * This {@code Point2I} instance usually contains the minimum X and maximum Y component values.
	 * 
	 * @return the {@code Point2I} instance denoted by B
	 */
	public Point2I getB() {
		return this.b;
	}
	
	/**
	 * Returns the {@link Point2I} instance denoted by C.
	 * <p>
	 * This {@code Point2I} instance usually contains the maximum X and maximum Y component values.
	 * 
	 * @return the {@code Point2I} instance denoted by C
	 */
	public Point2I getC() {
		return this.c;
	}
	
	/**
	 * Returns the {@link Point2I} instance denoted by D.
	 * <p>
	 * This {@code Point2I} instance usually contains the maximum X and minimum Y component values.
	 * 
	 * @return the {@code Point2I} instance denoted by D
	 */
	public Point2I getD() {
		return this.d;
	}
	
	/**
	 * Returns a {@code String} with the name of this {@code Rectangle2I} instance.
	 * 
	 * @return a {@code String} with the name of this {@code Rectangle2I} instance
	 */
	@Override
	public String getName() {
		return NAME;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code Rectangle2I} instance.
	 * 
	 * @return a {@code String} representation of this {@code Rectangle2I} instance
	 */
	@Override
	public String toString() {
		return String.format("new Rectangle2I(%s, %s)", this.a, this.c);
	}
	
	/**
	 * Accepts a {@link NodeHierarchicalVisitor}.
	 * <p>
	 * Returns the result of {@code nodeHierarchicalVisitor.visitLeave(this)}.
	 * <p>
	 * If {@code nodeHierarchicalVisitor} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If a {@code RuntimeException} is thrown by the current {@code NodeHierarchicalVisitor}, a {@code NodeTraversalException} will be thrown with the {@code RuntimeException} wrapped.
	 * <p>
	 * This implementation will:
	 * <ul>
	 * <li>throw a {@code NullPointerException} if {@code nodeHierarchicalVisitor} is {@code null}.</li>
	 * <li>throw a {@code NodeTraversalException} if {@code nodeHierarchicalVisitor} throws a {@code RuntimeException}.</li>
	 * <li>traverse its child {@code Node} instances.</li>
	 * </ul>
	 * 
	 * @param nodeHierarchicalVisitor the {@code NodeHierarchicalVisitor} to accept
	 * @return the result of {@code nodeHierarchicalVisitor.visitLeave(this)}
	 * @throws NodeTraversalException thrown if, and only if, a {@code RuntimeException} is thrown by the current {@code NodeHierarchicalVisitor}
	 * @throws NullPointerException thrown if, and only if, {@code nodeHierarchicalVisitor} is {@code null}
	 */
	@Override
	public boolean accept(final NodeHierarchicalVisitor nodeHierarchicalVisitor) {
		Objects.requireNonNull(nodeHierarchicalVisitor, "nodeHierarchicalVisitor == null");
		
		try {
			if(nodeHierarchicalVisitor.visitEnter(this)) {
				for(final LineSegment2I lineSegment : this.lineSegments) {
					if(!lineSegment.accept(nodeHierarchicalVisitor)) {
						return nodeHierarchicalVisitor.visitLeave(this);
					}
				}
				
				if(!this.a.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
				
				if(!this.b.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
				
				if(!this.c.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
				
				if(!this.d.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
			}
			
			return nodeHierarchicalVisitor.visitLeave(this);
		} catch(final RuntimeException e) {
			throw new NodeTraversalException(e);
		}
	}
	
	/**
	 * Returns {@code true} if, and only if, {@code point} is contained in this {@code Rectangle2I} instance, {@code false} otherwise.
	 * <p>
	 * If {@code point} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param point a {@link Point2I} instance
	 * @param isIncludingBorderOnly {@code true} if, and only if, this method should only include {@code Point2I} instances on the border of this {@code Rectangle2I} instance, {@code false} otherwise
	 * @return {@code true} if, and only if, {@code point} is contained in this {@code Rectangle2I} instance, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, {@code point} is {@code null}
	 */
	@Override
	public boolean contains(final Point2I point, final boolean isIncludingBorderOnly) {
		return isIncludingBorderOnly ? doContainsOnLineSegments(point) : doContains(point) || doContainsOnLineSegments(point);
	}
	
	/**
	 * Compares {@code object} to this {@code Rectangle2I} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code Rectangle2I}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code Rectangle2I} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code Rectangle2I}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof Rectangle2I)) {
			return false;
		} else if(!Objects.equals(this.lineSegments, Rectangle2I.class.cast(object).lineSegments)) {
			return false;
		} else if(!Objects.equals(this.a, Rectangle2I.class.cast(object).a)) {
			return false;
		} else if(!Objects.equals(this.b, Rectangle2I.class.cast(object).b)) {
			return false;
		} else if(!Objects.equals(this.c, Rectangle2I.class.cast(object).c)) {
			return false;
		} else if(!Objects.equals(this.d, Rectangle2I.class.cast(object).d)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Rectangle2I} instance is axis-aligned, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, this {@code Rectangle2I} instance is axis-aligned, {@code false} otherwise
	 */
	public boolean isAxisAligned() {
		final boolean isAxisAlignedAB = this.a.getY() == this.b.getY();
		final boolean isAxisAlignedBC = this.b.getX() == this.c.getX();
		final boolean isAxisAlignedCD = this.c.getY() == this.d.getY();
		final boolean isAxisAlignedDA = this.d.getX() == this.a.getX();
		final boolean isAxisAligned = isAxisAlignedAB && isAxisAlignedBC && isAxisAlignedCD && isAxisAlignedDA;
		
		return isAxisAligned;
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Rectangle2I} instance is rotated, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, this {@code Rectangle2I} instance is rotated, {@code false} otherwise
	 */
	public boolean isRotated() {
		return !isAxisAligned();
	}
	
	/**
	 * Returns the height of this {@code Rectangle2I} instance.
	 * 
	 * @return the height of this {@code Rectangle2I} instance
	 */
	public int getHeight() {
		final int maximumY = max(max(this.a.getY(), this.b.getY()), max(this.c.getY(), this.d.getY()));
		final int minimumY = min(min(this.a.getY(), this.b.getY()), min(this.c.getY(), this.d.getY()));
		
		return maximumY - minimumY;
	}
	
	/**
	 * Returns an {@code int} with the ID of this {@code Rectangle2I} instance.
	 * 
	 * @return an {@code int} with the ID of this {@code Rectangle2I} instance
	 */
	@Override
	public int getID() {
		return ID;
	}
	
	/**
	 * Returns the width of this {@code Rectangle2I} instance.
	 * 
	 * @return the width of this {@code Rectangle2I} instance
	 */
	public int getWidth() {
		final int maximumX = max(max(this.a.getX(), this.b.getX()), max(this.c.getX(), this.d.getX()));
		final int minimumX = min(min(this.a.getX(), this.b.getX()), min(this.c.getX(), this.d.getX()));
		
		return maximumX - minimumX;
	}
	
	/**
	 * Returns a hash code for this {@code Rectangle2I} instance.
	 * 
	 * @return a hash code for this {@code Rectangle2I} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.lineSegments, this.a, this.b, this.c, this.d);
	}
	
	/**
	 * Writes this {@code Rectangle2I} instance to {@code dataOutput}.
	 * <p>
	 * If {@code dataOutput} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * 
	 * @param dataOutput the {@code DataOutput} instance to write to
	 * @throws NullPointerException thrown if, and only if, {@code dataOutput} is {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
	@Override
	public void write(final DataOutput dataOutput) {
		try {
			dataOutput.writeInt(ID);
			
			this.a.write(dataOutput);
			this.b.write(dataOutput);
			this.c.write(dataOutput);
			this.d.write(dataOutput);
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Computes the intersection between {@code a} and {@code b}.
	 * <p>
	 * Returns an {@code Optional} with an optional {@code Rectangle2I} instance.
	 * <p>
	 * If either {@code a} or {@code b} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param a a {@code Rectangle2I} instance
	 * @param b a {@code Rectangle2I} instance
	 * @return an {@code Optional} with an optional {@code Rectangle2I} instance
	 * @throws NullPointerException thrown if, and only if, either {@code a} or {@code b} are {@code null}
	 */
	public static Optional<Rectangle2I> intersection(final Rectangle2I a, final Rectangle2I b) {
		final Point2I minimumA = a.getA();
		final Point2I minimumB = b.getA();
		final Point2I maximumA = a.getC();
		final Point2I maximumB = b.getC();
		final Point2I minimumC = Point2I.maximum(minimumA, minimumB);
		final Point2I maximumC = Point2I.minimum(maximumA, maximumB);
		
		if(minimumC.getX() > maximumC.getX() || minimumC.getY() > maximumC.getY()) {
			return Optional.empty();
		}
		
		return Optional.of(new Rectangle2I(minimumC, maximumC));
	}
	
	/**
	 * Returns a {@code Rectangle2I} instance that contains all {@link Point2I} instances in {@code points}.
	 * <p>
	 * If either {@code points} or an element in {@code points} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code points.length} is less than {@code 1}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param points a {@code Point2I[]} instance
	 * @return a {@code Rectangle2I} instance that contains all {@code Point2I} instances in {@code points}
	 * @throws IllegalArgumentException thrown if, and only if, {@code points.length} is less than {@code 1}
	 * @throws NullPointerException thrown if, and only if, either {@code points} or an element in {@code points} are {@code null}
	 */
	public static Rectangle2I fromPoints(final Point2I... points) {
		ParameterArguments.requireNonNullArray(points, "points");
		ParameterArguments.requireRange(points.length, 1, Integer.MAX_VALUE, "points.length");
		
		Point2I maximum = Point2I.MINIMUM;
		Point2I minimum = Point2I.MAXIMUM;
		
		for(final Point2I point : points) {
			maximum = Point2I.maximum(maximum, point);
			minimum = Point2I.minimum(minimum, point);
		}
		
		return new Rectangle2I(maximum, minimum);
	}
	
	/**
	 * Returns a {@code Rectangle2I} instance that is the union of {@code a} and {@code b}.
	 * <p>
	 * If either {@code a} or {@code b} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param a a {@code Rectangle2I} instance
	 * @param b a {@code Rectangle2I} instance
	 * @return a {@code Rectangle2I} instance that is the union of {@code a} and {@code b}
	 * @throws NullPointerException thrown if, and only if, either {@code a} or {@code b} are {@code null}
	 */
	public static Rectangle2I union(final Rectangle2I a, final Rectangle2I b) {
		final Point2I minimum = Point2I.minimum(a.getA(), b.getA());
		final Point2I maximum = Point2I.maximum(a.getC(), b.getC());
		
		return new Rectangle2I(minimum, maximum);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private boolean doContains(final Point2I point) {
		boolean isInside = false;
		
		final int pX = point.getX();
		final int pY = point.getY();
		
		final Point2I[] points = {this.a, this.b, this.c, this.d};
		
		for(int i = 0, j = points.length - 1; i < points.length; j = i++) {
			final Point2I pointI = points[i];
			final Point2I pointJ = points[j];
			
			final int iX = pointI.getX();
			final int iY = pointI.getY();
			final int jX = pointJ.getX();
			final int jY = pointJ.getY();
			
			if((iY > pY) != (jY > pY) && pX < (jX - iX) * (pY - iY) / (jY - iY) + iX) {
				isInside = !isInside;
			}
		}
		
		return isInside;
	}
	
	private boolean doContainsOnLineSegments(final Point2I point) {
		for(final LineSegment2I lineSegment : this.lineSegments) {
			if(lineSegment.contains(point)) {
				return true;
			}
		}
		
		return false;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static void doCheckPointValidity(final Point2I a, final Point2I b, final Point2I c, final Point2I d) {
		Objects.requireNonNull(a, "a == null");
		Objects.requireNonNull(b, "b == null");
		Objects.requireNonNull(c, "c == null");
		Objects.requireNonNull(d, "d == null");
		
		final int distanceAB = Point2I.distance(a, b);
		final int distanceBC = Point2I.distance(b, c);
		final int distanceCD = Point2I.distance(c, d);
		final int distanceDA = Point2I.distance(d, a);
		
		final int deltaABCD = abs(distanceAB - distanceCD);
		final int deltaBCDA = abs(distanceBC - distanceDA);
		
		final boolean isValidABCD = deltaABCD == 0;
		final boolean isValidBCDA = deltaBCDA == 0;
		final boolean isValid = isValidABCD && isValidBCDA;
		
		if(!isValid) {
			throw new IllegalArgumentException();
		}
	}
}