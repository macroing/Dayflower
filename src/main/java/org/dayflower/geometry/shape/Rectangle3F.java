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

import static org.dayflower.utility.Floats.abs;
import static org.dayflower.utility.Floats.isZero;

import java.io.DataOutput;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Objects;
import java.util.Optional;

import org.dayflower.geometry.BoundingVolume3F;
import org.dayflower.geometry.OrthonormalBasis33F;
import org.dayflower.geometry.Point2F;
import org.dayflower.geometry.Point3F;
import org.dayflower.geometry.Ray3F;
import org.dayflower.geometry.Shape3F;
import org.dayflower.geometry.SurfaceIntersection3F;
import org.dayflower.geometry.Vector3F;
import org.dayflower.geometry.boundingvolume.AxisAlignedBoundingBox3F;
import org.dayflower.node.NodeHierarchicalVisitor;
import org.dayflower.node.NodeTraversalException;

/**
 * A {@code Rectangle3F} is an implementation of {@link Shape3F} that represents a rectangle.
 * <p>
 * This class is immutable and therefore thread-safe.
 * <p>
 * This {@code Shape3F} implementation is supported on the GPU.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class Rectangle3F implements Shape3F {
	/**
	 * The name of this {@code Rectangle3F} class.
	 */
	public static final String NAME = "Rectangle";
	
	/**
	 * The ID of this {@code Rectangle3F} class.
	 */
	public static final int ID = 13;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final Point3F a;
	private final Point3F b;
	private final Point3F c;
	private final Point3F d;
	private final Vector3F surfaceNormal;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code Rectangle3F} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Rectangle3F(new Point3F(-2.0F, +2.0F, 0.0F), new Point3F(+2.0F, +2.0F, 0.0F), new Point3F(+2.0F, -2.0F, 0.0F), new Point3F(-2.0F, -2.0F, 0.0F));
	 * }
	 * </pre>
	 */
	public Rectangle3F() {
		this(new Point3F(-2.0F, +2.0F, 0.0F), new Point3F(+2.0F, +2.0F, 0.0F), new Point3F(+2.0F, -2.0F, 0.0F), new Point3F(-2.0F, -2.0F, 0.0F));
	}
	
	/**
	 * Constructs a new {@code Rectangle3F} instance.
	 * <p>
	 * If either {@code a}, {@code b}, {@code c} or {@code d} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If the provided {@link Point3F} instances are not coplanar or they do not represent a rectangle, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param a the {@code Point3F} instance denoted by {@code A}
	 * @param b the {@code Point3F} instance denoted by {@code B}
	 * @param c the {@code Point3F} instance denoted by {@code C}
	 * @param d the {@code Point3F} instance denoted by {@code D}
	 * @throws IllegalArgumentException thrown if, and only if, the provided {@code Point3F} instances are not coplanar or they do not represent a rectangle
	 * @throws NullPointerException thrown if, and only if, either {@code a}, {@code b}, {@code c} or {@code d} are {@code null}
	 */
	public Rectangle3F(final Point3F a, final Point3F b, final Point3F c, final Point3F d) {
		doCheckPointValidity(a, b, c, d);
		
		this.a = Point3F.getCached(a);
		this.b = Point3F.getCached(b);
		this.c = Point3F.getCached(c);
		this.d = Point3F.getCached(d);
		this.surfaceNormal = Vector3F.getCached(Vector3F.normalNormalized(a, b, c));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link BoundingVolume3F} instance that contains this {@code Rectangle3F} instance.
	 * 
	 * @return a {@code BoundingVolume3F} instance that contains this {@code Rectangle3F} instance
	 */
	@Override
	public BoundingVolume3F getBoundingVolume() {
		return AxisAlignedBoundingBox3F.fromPoints(this.a, this.b, this.c, this.d);
	}
	
	/**
	 * Performs an intersection test between {@code ray} and this {@code Rectangle3F} instance.
	 * <p>
	 * Returns an {@code Optional} with an optional {@link SurfaceIntersection3F} instance that contains information about the intersection, if it was found.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3F} to perform an intersection test against this {@code Rectangle3F} instance
	 * @param tMinimum the minimum parametric distance
	 * @param tMaximum the maximum parametric distance
	 * @return an {@code Optional} with an optional {@code SurfaceIntersection3F} instance that contains information about the intersection, if it was found
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	@Override
	public Optional<SurfaceIntersection3F> intersection(final Ray3F ray, final float tMinimum, final float tMaximum) {
		final Vector3F direction = ray.getDirection();
		final Vector3F surfaceNormal = this.surfaceNormal;
		
		final float nDotD = Vector3F.dotProduct(surfaceNormal, direction);
		
		if(isZero(nDotD)) {
			return SurfaceIntersection3F.EMPTY;
		}
		
		final Point3F origin = ray.getOrigin();
		final Point3F a = this.a;
		final Point3F b = this.b;
		final Point3F c = this.c;
		final Point3F d = this.d;
		
		final float t = Vector3F.dotProduct(Vector3F.direction(origin, a), surfaceNormal) / nDotD;
		
		if(t <= tMinimum || t >= tMaximum) {
			return SurfaceIntersection3F.EMPTY;
		}
		
		final Point3F p = Point3F.add(origin, direction, t);
		
		final Vector3F directionAB = Vector3F.direction(a, b);
		final Vector3F directionBC = Vector3F.direction(b, c);
		final Vector3F directionAP = Vector3F.direction(a, p);
		
		final float dotProductAPAB = Vector3F.dotProduct(directionAP, Vector3F.normalize(directionAB));
		final float dotProductAPBC = Vector3F.dotProduct(directionAP, Vector3F.normalize(directionBC));
		
		if(dotProductAPAB < 0.0F || dotProductAPAB > directionAB.length() || dotProductAPBC < 0.0F || dotProductAPBC > directionBC.length()) {
			return SurfaceIntersection3F.EMPTY;
		}
		
		final OrthonormalBasis33F orthonormalBasisG = new OrthonormalBasis33F(surfaceNormal);
		final OrthonormalBasis33F orthonormalBasisS = orthonormalBasisG;
		
		final float x = abs(surfaceNormal.getX());
		final float y = abs(surfaceNormal.getY());
		final float z = abs(surfaceNormal.getZ());
		
		final boolean isX = x > y && x > z;
		final boolean isY = y > z;
		
		final float aX = isX ? a.getY()      : isY ? a.getZ()      : a.getX();
		final float aY = isX ? a.getZ()      : isY ? a.getX()      : a.getY();
		final float bX = isX ? d.getY() - aX : isY ? d.getZ() - aX : d.getX() - aX;
		final float bY = isX ? d.getZ() - aY : isY ? d.getX() - aY : d.getY() - aY;
		final float cX = isX ? b.getY() - aX : isY ? b.getZ() - aX : b.getX() - aX;
		final float cY = isX ? b.getZ() - aY : isY ? b.getX() - aY : b.getY() - aY;
		
		final float determinant = bX * cY - bY * cX;
		final float determinantReciprocal = 1.0F / determinant;
		
		final float hU = isX ? p.getY() : isY ? p.getZ() : p.getX();
		final float hV = isX ? p.getZ() : isY ? p.getX() : p.getY();
		
		final float u = hU * (-bY * determinantReciprocal) + hV * (+bX * determinantReciprocal) + (bY * aX - bX * aY) * determinantReciprocal;
		final float v = hU * (+cY * determinantReciprocal) + hV * (-cX * determinantReciprocal) + (cX * aY - cY * aX) * determinantReciprocal;
		
		final Point2F textureCoordinates = new Point2F(u, v);
		
		final Vector3F surfaceIntersectionPointError = new Vector3F();
		
		return Optional.of(new SurfaceIntersection3F(orthonormalBasisG, orthonormalBasisS, textureCoordinates, p, ray, this, surfaceIntersectionPointError, t));
	}
	
	/**
	 * Returns the {@link Point3F} instance denoted by {@code A}.
	 * 
	 * @return the {@code Point3F} instance denoted by {@code A}
	 */
	public Point3F getA() {
		return this.a;
	}
	
	/**
	 * Returns the {@link Point3F} instance denoted by {@code B}.
	 * 
	 * @return the {@code Point3F} instance denoted by {@code B}
	 */
	public Point3F getB() {
		return this.b;
	}
	
	/**
	 * Returns the {@link Point3F} instance denoted by {@code C}.
	 * 
	 * @return the {@code Point3F} instance denoted by {@code C}
	 */
	public Point3F getC() {
		return this.c;
	}
	
	/**
	 * Returns the {@link Point3F} instance denoted by {@code D}.
	 * 
	 * @return the {@code Point3F} instance denoted by {@code D}
	 */
	public Point3F getD() {
		return this.d;
	}
	
	/**
	 * Returns a {@code String} with the name of this {@code Rectangle3F} instance.
	 * 
	 * @return a {@code String} with the name of this {@code Rectangle3F} instance
	 */
	@Override
	public String getName() {
		return NAME;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code Rectangle3F} instance.
	 * 
	 * @return a {@code String} representation of this {@code Rectangle3F} instance
	 */
	@Override
	public String toString() {
		return String.format("new Rectangle3F(%s, %s, %s, %s)", this.a, this.b, this.c, this.d);
	}
	
	/**
	 * Returns the surface normal of this {@code Rectangle3F} instance.
	 * 
	 * @return the surface normal of this {@code Rectangle3F} instance
	 */
	public Vector3F getSurfaceNormal() {
		return this.surfaceNormal;
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
				
				if(!this.surfaceNormal.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
			}
			
			return nodeHierarchicalVisitor.visitLeave(this);
		} catch(final RuntimeException e) {
			throw new NodeTraversalException(e);
		}
	}
	
	/**
	 * Returns {@code true} if, and only if, {@code point} is contained in this {@code Rectangle3F} instance, {@code false} otherwise.
	 * <p>
	 * If {@code point} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param point a {@link Point3F} instance
	 * @return {@code true} if, and only if, {@code point} is contained in this {@code Rectangle3F} instance, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, {@code point} is {@code null}
	 */
	public boolean contains(final Point3F point) {
		final Point3F a = this.a;
		final Point3F b = this.b;
		final Point3F c = this.c;
		final Point3F p = Objects.requireNonNull(point, "point == null");
		
		if(!Point3F.coplanar(a, b, p)) {
			return false;
		}
		
		final Vector3F directionAB = Vector3F.direction(a, b);
		final Vector3F directionBC = Vector3F.direction(b, c);
		final Vector3F directionAP = Vector3F.direction(a, p);
		
		final float dotProductAPAB = Vector3F.dotProduct(directionAP, Vector3F.normalize(directionAB));
		final float dotProductAPBC = Vector3F.dotProduct(directionAP, Vector3F.normalize(directionBC));
		
		return dotProductAPAB >= 0.0F && dotProductAPAB <= directionAB.length() && dotProductAPBC >= 0.0F && dotProductAPBC <= directionBC.length();
	}
	
	/**
	 * Compares {@code object} to this {@code Rectangle3F} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code Rectangle3F}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code Rectangle3F} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code Rectangle3F}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof Rectangle3F)) {
			return false;
		} else if(!Objects.equals(this.a, Rectangle3F.class.cast(object).a)) {
			return false;
		} else if(!Objects.equals(this.b, Rectangle3F.class.cast(object).b)) {
			return false;
		} else if(!Objects.equals(this.c, Rectangle3F.class.cast(object).c)) {
			return false;
		} else if(!Objects.equals(this.d, Rectangle3F.class.cast(object).d)) {
			return false;
		} else if(!Objects.equals(this.surfaceNormal, Rectangle3F.class.cast(object).surfaceNormal)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns the surface area of this {@code Rectangle3F} instance.
	 * 
	 * @return the surface area of this {@code Rectangle3F} instance
	 */
	@Override
	public float getSurfaceArea() {
		return Vector3F.crossProduct(Vector3F.direction(this.a, this.b), Vector3F.direction(this.b, this.c)).length();
	}
	
	/**
	 * Performs an intersection test between {@code ray} and this {@code Rectangle3F} instance.
	 * <p>
	 * Returns {@code t}, the parametric distance to the surface intersection point, or {@code Float.NaN} if no intersection exists.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3F} to perform an intersection test against this {@code Rectangle3F} instance
	 * @param tMinimum the minimum parametric distance
	 * @param tMaximum the maximum parametric distance
	 * @return {@code t}, the parametric distance to the surface intersection point, or {@code Float.NaN} if no intersection exists
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	@Override
	public float intersectionT(final Ray3F ray, final float tMinimum, final float tMaximum) {
		final Vector3F direction = ray.getDirection();
		final Vector3F surfaceNormal = this.surfaceNormal;
		
		final float nDotD = Vector3F.dotProduct(surfaceNormal, direction);
		
		if(isZero(nDotD)) {
			return Float.NaN;
		}
		
		final Point3F origin = ray.getOrigin();
		final Point3F a = this.a;
		final Point3F b = this.b;
		final Point3F c = this.c;
		
		final float t = Vector3F.dotProduct(Vector3F.direction(origin, a), surfaceNormal) / nDotD;
		
		if(t <= tMinimum || t >= tMaximum) {
			return Float.NaN;
		}
		
		final Point3F p = Point3F.add(origin, direction, t);
		
		final Vector3F directionAB = Vector3F.direction(a, b);
		final Vector3F directionBC = Vector3F.direction(b, c);
		final Vector3F directionAP = Vector3F.direction(a, p);
		
		final float dotProductAPAB = Vector3F.dotProduct(directionAP, Vector3F.normalize(directionAB));
		final float dotProductAPBC = Vector3F.dotProduct(directionAP, Vector3F.normalize(directionBC));
		
		if(dotProductAPAB < 0.0F || dotProductAPAB > directionAB.length() || dotProductAPBC < 0.0F || dotProductAPBC > directionBC.length()) {
			return Float.NaN;
		}
		
		return t;
	}
	
	/**
	 * Returns an {@code int} with the ID of this {@code Rectangle3F} instance.
	 * 
	 * @return an {@code int} with the ID of this {@code Rectangle3F} instance
	 */
	@Override
	public int getID() {
		return ID;
	}
	
	/**
	 * Returns a hash code for this {@code Rectangle3F} instance.
	 * 
	 * @return a hash code for this {@code Rectangle3F} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.a, this.b, this.c, this.d, this.surfaceNormal);
	}
	
	/**
	 * Writes this {@code Rectangle3F} instance to {@code dataOutput}.
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
	
	private static void doCheckPointValidity(final Point3F a, final Point3F b, final Point3F c, final Point3F d) {
		Objects.requireNonNull(a, "a == null");
		Objects.requireNonNull(b, "b == null");
		Objects.requireNonNull(c, "c == null");
		Objects.requireNonNull(d, "d == null");
		
		if(!Point3F.coplanar(a, b, c, d)) {
			throw new IllegalArgumentException("The provided Point3F instances are not coplanar.");
		}
		
		final float distanceAB = Point3F.distance(a, b);
		final float distanceBC = Point3F.distance(b, c);
		final float distanceCD = Point3F.distance(c, d);
		final float distanceDA = Point3F.distance(d, a);
		
		final float deltaABCD = abs(distanceAB - distanceCD);
		final float deltaBCDA = abs(distanceBC - distanceDA);
		
		final boolean isValidABCD = deltaABCD <= 0.00001F;
		final boolean isValidBCDA = deltaBCDA <= 0.00001F;
		final boolean isValid = isValidABCD && isValidBCDA;
		
		if(!isValid) {
			throw new IllegalArgumentException();
		}
	}
}