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

import static org.dayflower.utility.Floats.abs;
import static org.dayflower.utility.Floats.equal;
import static org.dayflower.utility.Floats.max;
import static org.dayflower.utility.Floats.min;

import java.io.DataOutput;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.Field;//TODO: Add Unit Tests!
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.dayflower.geometry.AngleF;
import org.dayflower.geometry.Point2F;
import org.dayflower.geometry.Shape2F;
import org.dayflower.geometry.Vector2F;
import org.dayflower.node.NodeHierarchicalVisitor;
import org.dayflower.node.NodeTraversalException;
import org.dayflower.utility.ParameterArguments;

/**
 * A {@code Rectangle2F} is an implementation of {@link Shape2F} that represents a rectangle.
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class Rectangle2F implements Shape2F {
	/**
	 * The name of this {@code Rectangle2F} class.
	 */
//	TODO: Add Unit Tests!
	public static final String NAME = "Rectangle";
	
	/**
	 * The ID of this {@code Rectangle2F} class.
	 */
//	TODO: Add Unit Tests!
	public static final int ID = 4;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final List<LineSegment2F> lineSegments;
	private final Point2F a;
	private final Point2F b;
	private final Point2F c;
	private final Point2F d;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code Rectangle2F} instance that contains {@code circle}.
	 * <p>
	 * If {@code circle} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param circle a {@link Circle2F} instance
	 * @throws NullPointerException thrown if, and only if, {@code circle} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public Rectangle2F(final Circle2F circle) {
		this(new Point2F(circle.getCenter().getX() - circle.getRadius(), circle.getCenter().getY() - circle.getRadius()), new Point2F(circle.getCenter().getX() + circle.getRadius(), circle.getCenter().getY() + circle.getRadius()));
	}
	
	/**
	 * Constructs a new {@code Rectangle2F} instance based on {@code x} and {@code y}.
	 * <p>
	 * If either {@code x} or {@code y} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param x a {@link Point2F} instance
	 * @param y a {@code Point2F} instance
	 * @throws NullPointerException thrown if, and only if, either {@code x} or {@code y} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public Rectangle2F(final Point2F x, final Point2F y) {
		this.a = new Point2F(min(x.getX(), y.getX()), min(x.getY(), y.getY()));
		this.b = new Point2F(min(x.getX(), y.getX()), max(x.getY(), y.getY()));
		this.c = new Point2F(max(x.getX(), y.getX()), max(x.getY(), y.getY()));
		this.d = new Point2F(max(x.getX(), y.getX()), min(x.getY(), y.getY()));
		this.lineSegments = LineSegment2F.fromPoints(this.a, this.b, this.c, this.d);
	}
	
	/**
	 * Constructs a new {@code Rectangle2F} instance based on {@code a}, {@code b}, {@code c} and {@code d}.
	 * <p>
	 * If either {@code a}, {@code b}, {@code c} or {@code d} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code a}, {@code b}, {@code c} and {@code d} does not form a rectangle, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param a a {@link Point2F} instance
	 * @param b a {@code Point2F} instance
	 * @param c a {@code Point2F} instance
	 * @param d a {@code Point2F} instance
	 * @throws IllegalArgumentException thrown if, and only if, {@code a}, {@code b}, {@code c} and {@code d} does not form a rectangle
	 * @throws NullPointerException thrown if, and only if, either {@code a}, {@code b}, {@code c} or {@code d} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public Rectangle2F(final Point2F a, final Point2F b, final Point2F c, final Point2F d) {
		doCheckPointValidity(a, b, c, d);
		
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
		this.lineSegments = LineSegment2F.fromPoints(this.a, this.b, this.c, this.d);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code List} that contains {@link LineSegment2F} instances that connects all {@link Point2F} instances in this {@code Rectangle2F} instance.
	 * 
	 * @return a {@code List} that contains {@code LineSegment2F} instances that connects all {@link Point2F} instances in this {@code Rectangle2F} instance
	 */
//	TODO: Add Unit Tests!
	public List<LineSegment2F> getLineSegments() {
		return new ArrayList<>(this.lineSegments);
	}
	
	/**
	 * Returns the {@link Point2F} instance denoted by A.
	 * <p>
	 * This {@code Point2F} instance usually contains the minimum X and minimum Y component values.
	 * 
	 * @return the {@code Point2F} instance denoted by A
	 */
//	TODO: Add Unit Tests!
	public Point2F getA() {
		return this.a;
	}
	
	/**
	 * Returns the {@link Point2F} instance denoted by B.
	 * <p>
	 * This {@code Point2F} instance usually contains the minimum X and maximum Y component values.
	 * 
	 * @return the {@code Point2F} instance denoted by B
	 */
//	TODO: Add Unit Tests!
	public Point2F getB() {
		return this.b;
	}
	
	/**
	 * Returns the {@link Point2F} instance denoted by C.
	 * <p>
	 * This {@code Point2F} instance usually contains the maximum X and maximum Y component values.
	 * 
	 * @return the {@code Point2F} instance denoted by C
	 */
//	TODO: Add Unit Tests!
	public Point2F getC() {
		return this.c;
	}
	
	/**
	 * Returns the {@link Point2F} instance denoted by D.
	 * <p>
	 * This {@code Point2F} instance usually contains the maximum X and minimum Y component values.
	 * 
	 * @return the {@code Point2F} instance denoted by D
	 */
//	TODO: Add Unit Tests!
	public Point2F getD() {
		return this.d;
	}
	
	/**
	 * Returns a {@code String} with the name of this {@code Rectangle2F} instance.
	 * 
	 * @return a {@code String} with the name of this {@code Rectangle2F} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public String getName() {
		return NAME;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code Rectangle2F} instance.
	 * 
	 * @return a {@code String} representation of this {@code Rectangle2F} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public String toString() {
		return String.format("new Rectangle2F(%s, %s)", this.a, this.c);
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
//	TODO: Add Unit Tests!
	@Override
	public boolean accept(final NodeHierarchicalVisitor nodeHierarchicalVisitor) {
		Objects.requireNonNull(nodeHierarchicalVisitor, "nodeHierarchicalVisitor == null");
		
		try {
			if(nodeHierarchicalVisitor.visitEnter(this)) {
				for(final LineSegment2F lineSegment : this.lineSegments) {
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
	 * Returns {@code true} if, and only if, {@code point} is contained in this {@code Rectangle2F} instance, {@code false} otherwise.
	 * <p>
	 * If {@code point} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param point a {@link Point2F} instance
	 * @return {@code true} if, and only if, {@code point} is contained in this {@code Rectangle2F} instance, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, {@code point} is {@code null}
	 */
//	TODO: Add Unit Tests!
	@Override
	public boolean contains(final Point2F point) {
		return doContains(point) || doContainsOnLineSegments(point);
	}
	
	/**
	 * Compares {@code object} to this {@code Rectangle2F} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code Rectangle2F}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code Rectangle2F} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code Rectangle2F}, and their respective values are equal, {@code false} otherwise
	 */
//	TODO: Add Unit Tests!
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof Rectangle2F)) {
			return false;
		} else if(!Objects.equals(this.lineSegments, Rectangle2F.class.cast(object).lineSegments)) {
			return false;
		} else if(!Objects.equals(this.a, Rectangle2F.class.cast(object).a)) {
			return false;
		} else if(!Objects.equals(this.b, Rectangle2F.class.cast(object).b)) {
			return false;
		} else if(!Objects.equals(this.c, Rectangle2F.class.cast(object).c)) {
			return false;
		} else if(!Objects.equals(this.d, Rectangle2F.class.cast(object).d)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Rectangle2F} instance is axis-aligned, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, this {@code Rectangle2F} instance is axis-aligned, {@code false} otherwise
	 */
//	TODO: Add Unit Tests!
	public boolean isAxisAligned() {
		final boolean isAxisAlignedAB = equal(this.a.getY(), this.b.getY());
		final boolean isAxisAlignedBC = equal(this.b.getX(), this.c.getX());
		final boolean isAxisAlignedCD = equal(this.c.getY(), this.d.getY());
		final boolean isAxisAlignedDA = equal(this.d.getX(), this.a.getX());
		final boolean isAxisAligned = isAxisAlignedAB && isAxisAlignedBC && isAxisAlignedCD && isAxisAlignedDA;
		
		return isAxisAligned;
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Rectangle2F} instance is rotated, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, this {@code Rectangle2F} instance is rotated, {@code false} otherwise
	 */
//	TODO: Add Unit Tests!
	public boolean isRotated() {
		return !isAxisAligned();
	}
	
	/**
	 * Returns an {@code int} with the ID of this {@code Rectangle2F} instance.
	 * 
	 * @return an {@code int} with the ID of this {@code Rectangle2F} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public int getID() {
		return ID;
	}
	
	/**
	 * Returns a hash code for this {@code Rectangle2F} instance.
	 * 
	 * @return a hash code for this {@code Rectangle2F} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public int hashCode() {
		return Objects.hash(this.lineSegments, this.a, this.b, this.c, this.d);
	}
	
	/**
	 * Writes this {@code Rectangle2F} instance to {@code dataOutput}.
	 * <p>
	 * If {@code dataOutput} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * 
	 * @param dataOutput the {@code DataOutput} instance to write to
	 * @throws NullPointerException thrown if, and only if, {@code dataOutput} is {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
//	TODO: Add Unit Tests!
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
	 * Returns an {@code Optional} with an optional {@code Rectangle2F} instance.
	 * <p>
	 * If either {@code a} or {@code b} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param a a {@code Rectangle2F} instance
	 * @param b a {@code Rectangle2F} instance
	 * @return an {@code Optional} with an optional {@code Rectangle2F} instance
	 * @throws NullPointerException thrown if, and only if, either {@code a} or {@code b} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public static Optional<Rectangle2F> intersection(final Rectangle2F a, final Rectangle2F b) {
		final Point2F minimumA = a.getA();
		final Point2F minimumB = b.getA();
		final Point2F maximumA = a.getC();
		final Point2F maximumB = b.getC();
		final Point2F minimumC = Point2F.maximum(minimumA, minimumB);
		final Point2F maximumC = Point2F.minimum(maximumA, maximumB);
		
		if(minimumC.getX() > maximumC.getX() || minimumC.getY() > maximumC.getY()) {
			return Optional.empty();
		}
		
		return Optional.of(new Rectangle2F(minimumC, maximumC));
	}
	
	/**
	 * Returns a {@code Rectangle2F} instance that contains all {@link Point2F} instances in {@code points}.
	 * <p>
	 * If either {@code points} or an element in {@code points} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code points.length} is less than {@code 1}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param points a {@code Point2F[]} instance
	 * @return a {@code Rectangle2F} instance that contains all {@code Point2F} instances in {@code points}
	 * @throws IllegalArgumentException thrown if, and only if, {@code points.length} is less than {@code 1}
	 * @throws NullPointerException thrown if, and only if, either {@code points} or an element in {@code points} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public static Rectangle2F fromPoints(final Point2F... points) {
		ParameterArguments.requireNonNullArray(points, "points");
		ParameterArguments.requireRange(points.length, 1, Integer.MAX_VALUE, "points.length");
		
		Point2F maximum = Point2F.MINIMUM;
		Point2F minimum = Point2F.MAXIMUM;
		
		for(final Point2F point : points) {
			maximum = Point2F.maximum(maximum, point);
			minimum = Point2F.minimum(minimum, point);
		}
		
		return new Rectangle2F(maximum, minimum);
	}
	
	/**
	 * Rotates {@code rectangle} using {@code angle}.
	 * <p>
	 * Returns a new {@code Rectangle2F} instance with the result of the operation.
	 * <p>
	 * If either {@code rectangle} or {@code angle} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If the rotated {@link Point2F} instances do not form a rectangle, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param rectangle the {@code Rectangle2F} instance to rotate
	 * @param angle the {@link AngleF} instance to rotate with
	 * @return a new {@code Rectangle2F} instance with the result of the operation
	 * @throws IllegalArgumentException thrown if, and only if, the rotated {@code Point2F} instances do not form a rectangle
	 * @throws NullPointerException thrown if, and only if, either {@code rectangle} or {@code angle} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public static Rectangle2F rotate(final Rectangle2F rectangle, final AngleF angle) {
		final Point2F a = Point2F.rotate(rectangle.a, angle);
		final Point2F b = Point2F.rotate(rectangle.b, angle);
		final Point2F c = Point2F.rotate(rectangle.c, angle);
		final Point2F d = Point2F.rotate(rectangle.d, angle);
		
		return new Rectangle2F(a, b, c, d);
	}
	
	/**
	 * Translates {@code rectangle} in the direction of {@code direction}.
	 * <p>
	 * Returns a new {@code Rectangle2F} instance with the result of the operation.
	 * <p>
	 * If either {@code rectangle} or {@code direction} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param rectangle the {@code Rectangle2F} instance to translate
	 * @param direction the {@link Vector2F} instance to translate with
	 * @return a new {@code Rectangle2F} instance with the result of the operation
	 * @throws NullPointerException thrown if, and only if, either {@code rectangle} or {@code direction} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public static Rectangle2F translate(final Rectangle2F rectangle, final Vector2F direction) {
		final Point2F a = Point2F.add(rectangle.a, direction);
		final Point2F b = Point2F.add(rectangle.b, direction);
		final Point2F c = Point2F.add(rectangle.c, direction);
		final Point2F d = Point2F.add(rectangle.d, direction);
		
		return new Rectangle2F(a, b, c, d);
	}
	
	/**
	 * Returns a {@code Rectangle2F} instance that is the union of {@code a} and {@code b}.
	 * <p>
	 * If either {@code a} or {@code b} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param a a {@code Rectangle2F} instance
	 * @param b a {@code Rectangle2F} instance
	 * @return a {@code Rectangle2F} instance that is the union of {@code a} and {@code b}
	 * @throws NullPointerException thrown if, and only if, either {@code a} or {@code b} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public static Rectangle2F union(final Rectangle2F a, final Rectangle2F b) {
		final Point2F minimum = Point2F.minimum(Point2F.minimum(a.getA(), a.getB(), a.getC(), a.getD()), Point2F.minimum(b.getA(), b.getB(), b.getC(), b.getD()));
		final Point2F maximum = Point2F.maximum(Point2F.maximum(a.getA(), a.getB(), a.getC(), a.getD()), Point2F.maximum(b.getA(), b.getB(), b.getC(), b.getD()));
		
		return new Rectangle2F(minimum, maximum);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private boolean doContains(final Point2F point) {
		boolean isInside = false;
		
		final float pX = point.getX();
		final float pY = point.getY();
		
		final Point2F[] points = {this.a, this.b, this.c, this.d};
		
		for(int i = 0, j = points.length - 1; i < points.length; j = i++) {
			final Point2F pointI = points[i];
			final Point2F pointJ = points[j];
			
			final float iX = pointI.getX();
			final float iY = pointI.getY();
			final float jX = pointJ.getX();
			final float jY = pointJ.getY();
			
			if((iY > pY) != (jY > pY) && pX < (jX - iX) * (pY - iY) / (jY - iY) + iX) {
				isInside = !isInside;
			}
		}
		
		return isInside;
	}
	
	private boolean doContainsOnLineSegments(final Point2F point) {
		for(final LineSegment2F lineSegment : this.lineSegments) {
			if(lineSegment.contains(point)) {
				return true;
			}
		}
		
		return false;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static void doCheckPointValidity(final Point2F a, final Point2F b, final Point2F c, final Point2F d) {
		Objects.requireNonNull(a, "a == null");
		Objects.requireNonNull(b, "b == null");
		Objects.requireNonNull(c, "c == null");
		Objects.requireNonNull(d, "d == null");
		
		final float distanceAB = Point2F.distance(a, b);
		final float distanceBC = Point2F.distance(b, c);
		final float distanceCD = Point2F.distance(c, d);
		final float distanceDA = Point2F.distance(d, a);
		
		final float deltaABCD = abs(distanceAB - distanceCD);
		final float deltaBCDA = abs(distanceBC - distanceDA);
		
		final boolean isValidABCD = deltaABCD <= 0.1F;
		final boolean isValidBCDA = deltaBCDA <= 0.1F;
		final boolean isValid = isValidABCD && isValidBCDA;
		
		if(!isValid) {
			throw new IllegalArgumentException();
		}
	}
}