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

import java.io.DataOutput;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.Field;//TODO: Add Unit Tests!
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

import org.dayflower.geometry.BoundingVolume3F;
import org.dayflower.geometry.OrthonormalBasis33F;
import org.dayflower.geometry.Point2F;
import org.dayflower.geometry.Point3F;
import org.dayflower.geometry.Ray3F;
import org.dayflower.geometry.Shape3F;
import org.dayflower.geometry.SurfaceIntersection3F;
import org.dayflower.geometry.Vector2F;
import org.dayflower.geometry.Vector3F;
import org.dayflower.geometry.boundingvolume.AxisAlignedBoundingBox3F;
import org.dayflower.utility.ParameterArguments;

import org.macroing.java.lang.Floats;
import org.macroing.java.util.visitor.NodeHierarchicalVisitor;
import org.macroing.java.util.visitor.NodeTraversalException;

/**
 * A {@code Polygon3F} is an implementation of {@link Shape3F} that represents a polygon.
 * <p>
 * This class is immutable and therefore thread-safe.
 * <p>
 * This {@code Shape3F} implementation is supported on the GPU.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class Polygon3F implements Shape3F {
	/**
	 * The name of this {@code Polygon3F} class.
	 */
//	TODO: Add Unit Tests!
	public static final String NAME = "Polygon";
	
	/**
	 * The ID of this {@code Polygon3F} class.
	 */
//	TODO: Add Unit Tests!
	public static final int ID = 11;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final Point3F[] points;
	private final Polygon2F projectedPolygon;
	private final Vector3F surfaceNormal;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code Polygon3F} instance.
	 * <p>
	 * If either {@code points} or at least one of its elements are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code points.length} is less than {@code 3} or the provided {@link Point3F} instances are not coplanar, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param points the {@code Point3F} instances of this {@code Polygon3F} instance
	 * @throws IllegalArgumentException thrown if, and only if, {@code points.length} is less than {@code 3} or the provided {@code Point3F} instances are not coplanar
	 * @throws NullPointerException thrown if, and only if, either {@code points} or at least one of its elements are {@code null}
	 */
//	TODO: Add Unit Tests!
	public Polygon3F(final Point3F... points) {
		this.points = doRequireValidPoints(points);
		this.surfaceNormal = Vector3F.normalNormalized(this.points[0], this.points[1], this.points[2]);
		this.projectedPolygon = doCreateProjectedPolygon(this.points, this.surfaceNormal);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link BoundingVolume3F} instance that contains this {@code Polygon3F} instance.
	 * 
	 * @return a {@code BoundingVolume3F} instance that contains this {@code Polygon3F} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public BoundingVolume3F getBoundingVolume() {
		return AxisAlignedBoundingBox3F.fromPoints(this.points);
	}
	
	/**
	 * Performs an intersection test between {@code ray} and this {@code Polygon3F} instance.
	 * <p>
	 * Returns an {@code Optional} with an optional {@link SurfaceIntersection3F} instance that contains information about the intersection, if it was found.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3F} to perform an intersection test against this {@code Polygon3F} instance
	 * @param tMinimum the minimum parametric distance
	 * @param tMaximum the maximum parametric distance
	 * @return an {@code Optional} with an optional {@code SurfaceIntersection3F} instance that contains information about the intersection, if it was found
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
//	TODO: Add Unit Tests!
	@Override
	public Optional<SurfaceIntersection3F> intersection(final Ray3F ray, final float tMinimum, final float tMaximum) {
		final float t = intersectionT(ray, tMinimum, tMaximum);
		
		if(Floats.isNaN(t)) {
			return SurfaceIntersection3F.EMPTY;
		}
		
		return Optional.of(doCreateSurfaceIntersection(ray, t));
	}
	
	/**
	 * Returns the {@link Point3F} instance at index {@code index} in this {@code Polygon3F} instance.
	 * <p>
	 * If {@code index} is less than {@code 0} or greater than or equal to {@code polygon.getPointCount()}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param index the index of the {@code Point3F} to return
	 * @return the {@code Point3F} instance at index {@code index} in this {@code Polygon3F} instance
	 * @throws IllegalArgumentException thrown if, and only if, {@code index} is less than {@code 0} or greater than or equal to {@code polygon.getPointCount()}
	 */
//	TODO: Add Unit Tests!
	public Point3F getPoint(final int index) {
		ParameterArguments.requireRange(index, 0, getPointCount() - 1, "index");
		
		return this.points[index];
	}
	
	/**
	 * Returns a {@link Polygon2F} instance that contains the projected polygon.
	 * 
	 * @return a {@code Polygon2F} instance that contains the projected polygon
	 */
//	TODO: Add Unit Tests!
	public Polygon2F getProjectedPolygon() {
		return this.projectedPolygon;
	}
	
	/**
	 * Returns a {@code String} with the name of this {@code Polygon3F} instance.
	 * 
	 * @return a {@code String} with the name of this {@code Polygon3F} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public String getName() {
		return NAME;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code Polygon3F} instance.
	 * 
	 * @return a {@code String} representation of this {@code Polygon3F} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public String toString() {
		return String.format("new Polygon3F(%s)", Point3F.toString(this.points));
	}
	
	/**
	 * Returns a {@link Vector3F} instance with the surface normal associated with this {@code Polygon3F} instance.
	 * 
	 * @return a {@code Vector3F} instance with the surface normal associated with this {@code Polygon3F} instance
	 */
//	TODO: Add Unit Tests!
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
//	TODO: Add Unit Tests!
	@Override
	public boolean accept(final NodeHierarchicalVisitor nodeHierarchicalVisitor) {
		Objects.requireNonNull(nodeHierarchicalVisitor, "nodeHierarchicalVisitor == null");
		
		try {
			if(nodeHierarchicalVisitor.visitEnter(this)) {
				for(final Point3F point : this.points) {
					if(!point.accept(nodeHierarchicalVisitor)) {
						return nodeHierarchicalVisitor.visitLeave(this);
					}
				}
				
				if(!this.projectedPolygon.accept(nodeHierarchicalVisitor)) {
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
	 * Returns {@code true} if, and only if, {@code point} is contained in this {@code Polygon3F} instance, {@code false} otherwise.
	 * <p>
	 * If {@code point} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param point a {@link Point3F} instance
	 * @return {@code true} if, and only if, {@code point} is contained in this {@code Polygon3F} instance, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, {@code point} is {@code null}
	 */
//	TODO: Add Unit Tests!
	@Override
	public boolean contains(final Point3F point) {
		final Point3F a = this.points[0];
		final Point3F b = this.points[1];
		final Point3F c = this.points[2];
		final Point3F p = Objects.requireNonNull(point, "point == null");
		
		if(!Point3F.coplanar(a, b, c, p)) {
			return false;
		}
		
		return doContains(point);
	}
	
	/**
	 * Compares {@code object} to this {@code Polygon3F} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code Polygon3F}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code Polygon3F} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code Polygon3F}, and their respective values are equal, {@code false} otherwise
	 */
//	TODO: Add Unit Tests!
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof Polygon3F)) {
			return false;
		} else if(!Arrays.equals(this.points, Polygon3F.class.cast(object).points)) {
			return false;
		} else if(!Objects.equals(this.projectedPolygon, Polygon3F.class.cast(object).projectedPolygon)) {
			return false;
		} else if(!Objects.equals(this.surfaceNormal, Polygon3F.class.cast(object).surfaceNormal)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns the surface area of this {@code Polygon3F} instance.
	 * 
	 * @return the surface area of this {@code Polygon3F} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public float getSurfaceArea() {
		Vector3F surfaceArea = Vector3F.ZERO;
		
		for(int i = 0, j = this.points.length - 1; i < this.points.length; j = i, i++) {
			surfaceArea = Vector3F.add(surfaceArea, Vector3F.crossProduct(new Vector3F(this.points[i]), new Vector3F(this.points[j])));
		}
		
		return 0.5F * Vector3F.dotProductAbs(this.surfaceNormal, surfaceArea);
	}
	
	/**
	 * Performs an intersection test between {@code ray} and this {@code Polygon3F} instance.
	 * <p>
	 * Returns {@code t}, the parametric distance to the surface intersection point, or {@code Float.NaN} if no intersection exists.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3F} to perform an intersection test against this {@code Polygon3F} instance
	 * @param tMinimum the minimum parametric distance
	 * @param tMaximum the maximum parametric distance
	 * @return {@code t}, the parametric distance to the surface intersection point, or {@code Float.NaN} if no intersection exists
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
//	TODO: Add Unit Tests!
	@Override
	public float intersectionT(final Ray3F ray, final float tMinimum, final float tMaximum) {
		final float dotProduct = Vector3F.dotProduct(this.surfaceNormal, ray.getDirection());
		
		if(Floats.isZero(dotProduct)) {
			return Float.NaN;
		}
		
		final float t = Vector3F.dotProduct(Vector3F.direction(ray.getOrigin(), this.points[0]), this.surfaceNormal) / dotProduct;
		
		if(t > tMinimum && t < tMaximum && doContains(doCreateSurfaceIntersectionPoint(ray, t))) {
			return t;
		}
		
		return Float.NaN;
	}
	
	/**
	 * Returns an {@code int} with the ID of this {@code Polygon3F} instance.
	 * 
	 * @return an {@code int} with the ID of this {@code Polygon3F} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public int getID() {
		return ID;
	}
	
	/**
	 * Returns the {@link Point3F} count of this {@code Polygon3F} instance.
	 * 
	 * @return the {@code Point3F} count of this {@code Polygon3F} instance
	 */
//	TODO: Add Unit Tests!
	public int getPointCount() {
		return this.points.length;
	}
	
	/**
	 * Returns a hash code for this {@code Polygon3F} instance.
	 * 
	 * @return a hash code for this {@code Polygon3F} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public int hashCode() {
		return Objects.hash(Integer.valueOf(Arrays.hashCode(this.points)), this.projectedPolygon, this.surfaceNormal);
	}
	
	/**
	 * Writes this {@code Polygon3F} instance to {@code dataOutput}.
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
			dataOutput.writeInt(this.points.length);
			
			for(final Point3F point : this.points) {
				point.write(dataOutput);
			}
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private OrthonormalBasis33F doCreateOrthonormalBasisG() {
		return new OrthonormalBasis33F(this.surfaceNormal);
	}
	
	private Point2F doCreateTextureCoordinates(final Point3F surfaceIntersectionPoint) {
		final Vector3F surfaceNormalAbs = Vector3F.absolute(this.surfaceNormal);
		
		final boolean isX = surfaceNormalAbs.x > surfaceNormalAbs.y && surfaceNormalAbs.x > surfaceNormalAbs.z;
		final boolean isY = surfaceNormalAbs.y > surfaceNormalAbs.z;
		
		final Point3F a = this.points[0];
		final Point3F b = this.points[1];
		final Point3F c = this.points[this.points.length - 1];
		
		final Vector2F vA = isX ? Vector2F.directionYZ(a) : isY ? Vector2F.directionZX(a) : Vector2F.directionXY(a);
		final Vector2F vB = isX ? Vector2F.directionYZ(c) : isY ? Vector2F.directionZX(c) : Vector2F.directionXY(c);
		final Vector2F vC = isX ? Vector2F.directionYZ(b) : isY ? Vector2F.directionZX(b) : Vector2F.directionXY(b);
		final Vector2F vAB = Vector2F.subtract(vB, vA);
		final Vector2F vAC = Vector2F.subtract(vC, vA);
		
		final float determinant = Vector2F.crossProduct(vAB, vAC);
		final float determinantReciprocal = 1.0F / determinant;
		
		final float hU = isX ? surfaceIntersectionPoint.y : isY ? surfaceIntersectionPoint.z : surfaceIntersectionPoint.x;
		final float hV = isX ? surfaceIntersectionPoint.z : isY ? surfaceIntersectionPoint.x : surfaceIntersectionPoint.y;
		
		final float u = hU * (-vAB.y * determinantReciprocal) + hV * (+vAB.x * determinantReciprocal) + Vector2F.crossProduct(vA, vAB) * determinantReciprocal;
		final float v = hU * (+vAC.y * determinantReciprocal) + hV * (-vAC.x * determinantReciprocal) + Vector2F.crossProduct(vAC, vA) * determinantReciprocal;
		
		return new Point2F(u, v);
	}
	
	private SurfaceIntersection3F doCreateSurfaceIntersection(final Ray3F ray, final float t) {
		final Point3F surfaceIntersectionPoint = doCreateSurfaceIntersectionPoint(ray, t);
		
		final OrthonormalBasis33F orthonormalBasisG = doCreateOrthonormalBasisG();
		final OrthonormalBasis33F orthonormalBasisS = orthonormalBasisG;
		
		final Point2F textureCoordinates = doCreateTextureCoordinates(surfaceIntersectionPoint);
		
		return new SurfaceIntersection3F(orthonormalBasisG, orthonormalBasisS, textureCoordinates, surfaceIntersectionPoint, ray, this, t);
	}
	
	private boolean doContains(final Point3F point) {
		final Point3F a = this.points[0];
		final Point3F b = this.points[1];
		final Point3F p = point;
		
		final Vector3F w = this.surfaceNormal;
		final Vector3F u = Vector3F.directionNormalized(a, b);
		final Vector3F v = Vector3F.crossProduct(w, u);
		
		return this.projectedPolygon.contains(doMap(a, p, u, v));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static Point2F doMap(final Point3F a, final Point3F b, final Vector3F u, final Vector3F v) {
		final Vector3F directionAB = Vector3F.direction(a, b);
		
		final float x = Vector3F.dotProduct(directionAB, u);
		final float y = Vector3F.dotProduct(directionAB, v);
		
		return new Point2F(x, y);
	}
	
	private static Point3F doCreateSurfaceIntersectionPoint(final Ray3F ray, final float t) {
		return Point3F.add(ray.getOrigin(), ray.getDirection(), t);
	}
	
	private static Point3F[] doRequireValidPoints(final Point3F[] points) {
		ParameterArguments.requireNonNullArray(points, "points");
		ParameterArguments.requireRange(points.length, 3, Integer.MAX_VALUE, "points.length");
		
		if(Point3F.coplanar(points)) {
			return points.clone();
		}
		
		throw new IllegalArgumentException("The provided Point3F instances are not coplanar.");
	}
	
	private static Polygon2F doCreateProjectedPolygon(final Point3F[] points, final Vector3F surfaceNormal) {
		final Point3F a = points[0];
		final Point3F b = points[1];
		
		final Vector3F w = surfaceNormal;
		final Vector3F u = Vector3F.directionNormalized(a, b);
		final Vector3F v = Vector3F.crossProduct(w, u);
		
		final Point2F[] point2Fs = new Point2F[points.length];
		
		for(int i = 0; i < points.length; i++) {
			point2Fs[i] = doMap(a, points[i], u, v);
		}
		
		final Polygon2F polygon = new Polygon2F(point2Fs);
		
		return polygon;
	}
}