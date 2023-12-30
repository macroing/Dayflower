/**
 * Copyright 2014 - 2024 J&#246;rgen Lundgren
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

import org.dayflower.geometry.BoundingVolume3D;
import org.dayflower.geometry.OrthonormalBasis33D;
import org.dayflower.geometry.Point2D;
import org.dayflower.geometry.Point3D;
import org.dayflower.geometry.Ray3D;
import org.dayflower.geometry.Shape3D;
import org.dayflower.geometry.SurfaceIntersection3D;
import org.dayflower.geometry.Vector2D;
import org.dayflower.geometry.Vector3D;
import org.dayflower.geometry.boundingvolume.AxisAlignedBoundingBox3D;
import org.dayflower.utility.ParameterArguments;

import org.macroing.java.lang.Doubles;
import org.macroing.java.util.visitor.NodeHierarchicalVisitor;
import org.macroing.java.util.visitor.NodeTraversalException;

/**
 * A {@code Polygon3D} is an implementation of {@link Shape3D} that represents a polygon.
 * <p>
 * This class is immutable and therefore thread-safe.
 * <p>
 * This {@code Shape3D} implementation is not supported on the GPU.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class Polygon3D implements Shape3D {
	/**
	 * The name of this {@code Polygon3D} class.
	 */
//	TODO: Add Unit Tests!
	public static final String NAME = "Polygon";
	
	/**
	 * The ID of this {@code Polygon3D} class.
	 */
//	TODO: Add Unit Tests!
	public static final int ID = 11;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final Point3D[] points;
	private final Polygon2D projectedPolygon;
	private final Vector3D surfaceNormal;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code Polygon3D} instance.
	 * <p>
	 * If either {@code points} or at least one of its elements are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code points.length} is less than {@code 3} or the provided {@link Point3D} instances are not coplanar, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param points the {@code Point3D} instances of this {@code Polygon3D} instance
	 * @throws IllegalArgumentException thrown if, and only if, {@code points.length} is less than {@code 3} or the provided {@code Point3D} instances are not coplanar
	 * @throws NullPointerException thrown if, and only if, either {@code points} or at least one of its elements are {@code null}
	 */
//	TODO: Add Unit Tests!
	public Polygon3D(final Point3D... points) {
		this.points = doRequireValidPoints(points);
		this.surfaceNormal = Vector3D.normalNormalized(this.points[0], this.points[1], this.points[2]);
		this.projectedPolygon = doCreateProjectedPolygon(this.points, this.surfaceNormal);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link BoundingVolume3D} instance that contains this {@code Polygon3D} instance.
	 * 
	 * @return a {@code BoundingVolume3D} instance that contains this {@code Polygon3D} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public BoundingVolume3D getBoundingVolume() {
		return AxisAlignedBoundingBox3D.fromPoints(this.points);
	}
	
	/**
	 * Performs an intersection test between {@code ray} and this {@code Polygon3D} instance.
	 * <p>
	 * Returns an {@code Optional} with an optional {@link SurfaceIntersection3D} instance that contains information about the intersection, if it was found.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3D} to perform an intersection test against this {@code Polygon3D} instance
	 * @param tMinimum the minimum parametric distance
	 * @param tMaximum the maximum parametric distance
	 * @return an {@code Optional} with an optional {@code SurfaceIntersection3D} instance that contains information about the intersection, if it was found
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
//	TODO: Add Unit Tests!
	@Override
	public Optional<SurfaceIntersection3D> intersection(final Ray3D ray, final double tMinimum, final double tMaximum) {
		final double t = intersectionT(ray, tMinimum, tMaximum);
		
		if(Doubles.isNaN(t)) {
			return SurfaceIntersection3D.EMPTY;
		}
		
		return Optional.of(doCreateSurfaceIntersection(ray, t));
	}
	
	/**
	 * Returns the {@link Point3D} instance at index {@code index} in this {@code Polygon3D} instance.
	 * <p>
	 * If {@code index} is less than {@code 0} or greater than or equal to {@code polygon.getPointCount()}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param index the index of the {@code Point3D} to return
	 * @return the {@code Point3D} instance at index {@code index} in this {@code Polygon3D} instance
	 * @throws IllegalArgumentException thrown if, and only if, {@code index} is less than {@code 0} or greater than or equal to {@code polygon.getPointCount()}
	 */
//	TODO: Add Unit Tests!
	public Point3D getPoint(final int index) {
		ParameterArguments.requireRange(index, 0, getPointCount() - 1, "index");
		
		return this.points[index];
	}
	
	/**
	 * Returns a {@link Polygon2D} instance that contains the projected polygon.
	 * 
	 * @return a {@code Polygon2D} instance that contains the projected polygon
	 */
//	TODO: Add Unit Tests!
	public Polygon2D getProjectedPolygon() {
		return this.projectedPolygon;
	}
	
	/**
	 * Returns a {@code String} with the name of this {@code Polygon3D} instance.
	 * 
	 * @return a {@code String} with the name of this {@code Polygon3D} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public String getName() {
		return NAME;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code Polygon3D} instance.
	 * 
	 * @return a {@code String} representation of this {@code Polygon3D} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public String toString() {
		return String.format("new Polygon3D(%s)", Point3D.toString(this.points));
	}
	
	/**
	 * Returns a {@link Vector3D} instance with the surface normal associated with this {@code Polygon3D} instance.
	 * 
	 * @return a {@code Vector3D} instance with the surface normal associated with this {@code Polygon3D} instance
	 */
//	TODO: Add Unit Tests!
	public Vector3D getSurfaceNormal() {
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
				for(final Point3D point : this.points) {
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
	 * Returns {@code true} if, and only if, {@code point} is contained in this {@code Polygon3D} instance, {@code false} otherwise.
	 * <p>
	 * If {@code point} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param point a {@link Point3D} instance
	 * @return {@code true} if, and only if, {@code point} is contained in this {@code Polygon3D} instance, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, {@code point} is {@code null}
	 */
//	TODO: Add Unit Tests!
	@Override
	public boolean contains(final Point3D point) {
		final Point3D a = this.points[0];
		final Point3D b = this.points[1];
		final Point3D c = this.points[2];
		final Point3D p = Objects.requireNonNull(point, "point == null");
		
		if(!Point3D.coplanar(a, b, c, p)) {
			return false;
		}
		
		return doContains(point);
	}
	
	/**
	 * Compares {@code object} to this {@code Polygon3D} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code Polygon3D}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code Polygon3D} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code Polygon3D}, and their respective values are equal, {@code false} otherwise
	 */
//	TODO: Add Unit Tests!
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof Polygon3D)) {
			return false;
		} else if(!Arrays.equals(this.points, Polygon3D.class.cast(object).points)) {
			return false;
		} else if(!Objects.equals(this.projectedPolygon, Polygon3D.class.cast(object).projectedPolygon)) {
			return false;
		} else if(!Objects.equals(this.surfaceNormal, Polygon3D.class.cast(object).surfaceNormal)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns the surface area of this {@code Polygon3D} instance.
	 * 
	 * @return the surface area of this {@code Polygon3D} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public double getSurfaceArea() {
		Vector3D surfaceArea = Vector3D.ZERO;
		
		for(int i = 0, j = this.points.length - 1; i < this.points.length; j = i, i++) {
			surfaceArea = Vector3D.add(surfaceArea, Vector3D.crossProduct(new Vector3D(this.points[i]), new Vector3D(this.points[j])));
		}
		
		return 0.5D * Vector3D.dotProductAbs(this.surfaceNormal, surfaceArea);
	}
	
	/**
	 * Performs an intersection test between {@code ray} and this {@code Polygon3D} instance.
	 * <p>
	 * Returns {@code t}, the parametric distance to the surface intersection point, or {@code Double.NaN} if no intersection exists.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3D} to perform an intersection test against this {@code Polygon3D} instance
	 * @param tMinimum the minimum parametric distance
	 * @param tMaximum the maximum parametric distance
	 * @return {@code t}, the parametric distance to the surface intersection point, or {@code Double.NaN} if no intersection exists
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
//	TODO: Add Unit Tests!
	@Override
	public double intersectionT(final Ray3D ray, final double tMinimum, final double tMaximum) {
		final double dotProduct = Vector3D.dotProduct(this.surfaceNormal, ray.getDirection());
		
		if(Doubles.isZero(dotProduct)) {
			return Double.NaN;
		}
		
		final double t = Vector3D.dotProduct(Vector3D.direction(ray.getOrigin(), this.points[0]), this.surfaceNormal) / dotProduct;
		
		if(t > tMinimum && t < tMaximum && doContains(doCreateSurfaceIntersectionPoint(ray, t))) {
			return t;
		}
		
		return Double.NaN;
	}
	
	/**
	 * Returns an {@code int} with the ID of this {@code Polygon3D} instance.
	 * 
	 * @return an {@code int} with the ID of this {@code Polygon3D} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public int getID() {
		return ID;
	}
	
	/**
	 * Returns the {@link Point3D} count of this {@code Polygon3D} instance.
	 * 
	 * @return the {@code Point3D} count of this {@code Polygon3D} instance
	 */
//	TODO: Add Unit Tests!
	public int getPointCount() {
		return this.points.length;
	}
	
	/**
	 * Returns a hash code for this {@code Polygon3D} instance.
	 * 
	 * @return a hash code for this {@code Polygon3D} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public int hashCode() {
		return Objects.hash(Integer.valueOf(Arrays.hashCode(this.points)), this.projectedPolygon, this.surfaceNormal);
	}
	
	/**
	 * Writes this {@code Polygon3D} instance to {@code dataOutput}.
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
			
			for(final Point3D point : this.points) {
				point.write(dataOutput);
			}
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private OrthonormalBasis33D doCreateOrthonormalBasisG() {
		return new OrthonormalBasis33D(this.surfaceNormal);
	}
	
	private Point2D doCreateTextureCoordinates(final Point3D surfaceIntersectionPoint) {
		final Vector3D surfaceNormalAbs = Vector3D.absolute(this.surfaceNormal);
		
		final boolean isX = surfaceNormalAbs.x > surfaceNormalAbs.y && surfaceNormalAbs.x > surfaceNormalAbs.z;
		final boolean isY = surfaceNormalAbs.y > surfaceNormalAbs.z;
		
		final Point3D a = this.points[0];
		final Point3D b = this.points[1];
		final Point3D c = this.points[this.points.length - 1];
		
		final Vector2D vA = isX ? Vector2D.directionYZ(a) : isY ? Vector2D.directionZX(a) : Vector2D.directionXY(a);
		final Vector2D vB = isX ? Vector2D.directionYZ(c) : isY ? Vector2D.directionZX(c) : Vector2D.directionXY(c);
		final Vector2D vC = isX ? Vector2D.directionYZ(b) : isY ? Vector2D.directionZX(b) : Vector2D.directionXY(b);
		final Vector2D vAB = Vector2D.subtract(vB, vA);
		final Vector2D vAC = Vector2D.subtract(vC, vA);
		
		final double determinant = Vector2D.crossProduct(vAB, vAC);
		final double determinantReciprocal = 1.0D / determinant;
		
		final double hU = isX ? surfaceIntersectionPoint.y : isY ? surfaceIntersectionPoint.z : surfaceIntersectionPoint.x;
		final double hV = isX ? surfaceIntersectionPoint.z : isY ? surfaceIntersectionPoint.x : surfaceIntersectionPoint.y;
		
		final double u = hU * (-vAB.y * determinantReciprocal) + hV * (+vAB.x * determinantReciprocal) + Vector2D.crossProduct(vA, vAB) * determinantReciprocal;
		final double v = hU * (+vAC.y * determinantReciprocal) + hV * (-vAC.x * determinantReciprocal) + Vector2D.crossProduct(vAC, vA) * determinantReciprocal;
		
		return new Point2D(u, v);
	}
	
	private SurfaceIntersection3D doCreateSurfaceIntersection(final Ray3D ray, final double t) {
		final Point3D surfaceIntersectionPoint = doCreateSurfaceIntersectionPoint(ray, t);
		
		final OrthonormalBasis33D orthonormalBasisG = doCreateOrthonormalBasisG();
		final OrthonormalBasis33D orthonormalBasisS = orthonormalBasisG;
		
		final Point2D textureCoordinates = doCreateTextureCoordinates(surfaceIntersectionPoint);
		
		return new SurfaceIntersection3D(orthonormalBasisG, orthonormalBasisS, textureCoordinates, surfaceIntersectionPoint, ray, this, t);
	}
	
	private boolean doContains(final Point3D point) {
		final Point3D a = this.points[0];
		final Point3D b = this.points[1];
		final Point3D p = point;
		
		final Vector3D w = this.surfaceNormal;
		final Vector3D u = Vector3D.directionNormalized(a, b);
		final Vector3D v = Vector3D.crossProduct(w, u);
		
		return this.projectedPolygon.contains(doMap(a, p, u, v));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static Point2D doMap(final Point3D a, final Point3D b, final Vector3D u, final Vector3D v) {
		final Vector3D directionAB = Vector3D.direction(a, b);
		
		final double x = Vector3D.dotProduct(directionAB, u);
		final double y = Vector3D.dotProduct(directionAB, v);
		
		return new Point2D(x, y);
	}
	
	private static Point3D doCreateSurfaceIntersectionPoint(final Ray3D ray, final double t) {
		return Point3D.add(ray.getOrigin(), ray.getDirection(), t);
	}
	
	private static Point3D[] doRequireValidPoints(final Point3D[] points) {
		ParameterArguments.requireNonNullArray(points, "points");
		ParameterArguments.requireRange(points.length, 3, Integer.MAX_VALUE, "points.length");
		
		if(Point3D.coplanar(points)) {
			return points.clone();
		}
		
		throw new IllegalArgumentException("The provided Point3D instances are not coplanar.");
	}
	
	private static Polygon2D doCreateProjectedPolygon(final Point3D[] points, final Vector3D surfaceNormal) {
		final Point3D a = points[0];
		final Point3D b = points[1];
		
		final Vector3D w = surfaceNormal;
		final Vector3D u = Vector3D.directionNormalized(a, b);
		final Vector3D v = Vector3D.crossProduct(w, u);
		
		final Point2D[] point2Ds = new Point2D[points.length];
		
		for(int i = 0; i < points.length; i++) {
			point2Ds[i] = doMap(a, points[i], u, v);
		}
		
		final Polygon2D polygon = new Polygon2D(point2Ds);
		
		return polygon;
	}
}