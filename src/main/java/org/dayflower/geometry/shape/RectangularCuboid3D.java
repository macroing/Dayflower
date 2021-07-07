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

import static org.dayflower.utility.Doubles.isNaN;
import static org.dayflower.utility.Doubles.max;
import static org.dayflower.utility.Doubles.min;
import static org.dayflower.utility.Doubles.normalize;

import java.io.DataOutput;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Objects;
import java.util.Optional;

import org.dayflower.geometry.BoundingVolume3D;
import org.dayflower.geometry.OrthonormalBasis33D;
import org.dayflower.geometry.Point2D;
import org.dayflower.geometry.Point3D;
import org.dayflower.geometry.Ray3D;
import org.dayflower.geometry.Shape3D;
import org.dayflower.geometry.SurfaceIntersection3D;
import org.dayflower.geometry.Vector3D;
import org.dayflower.geometry.boundingvolume.AxisAlignedBoundingBox3D;
import org.dayflower.node.NodeHierarchicalVisitor;
import org.dayflower.node.NodeTraversalException;

/**
 * A {@code RectangularCuboid3D} is an implementation of {@link Shape3D} that represents a rectangular cuboid.
 * <p>
 * This class is immutable and therefore thread-safe.
 * <p>
 * This {@code Shape3D} implementation is not supported on the GPU.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class RectangularCuboid3D implements Shape3D {
	/**
	 * The name of this {@code RectangularCuboid3D} class.
	 */
	public static final String NAME = "Rectangular Cuboid";
	
	/**
	 * The ID of this {@code RectangularCuboid3D} class.
	 */
	public static final int ID = 12;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final Point3D maximum;
	private final Point3D minimum;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code RectangularCuboid3D} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new RectangularCuboid3D(new Point3D(-0.5D, -0.5D, -0.5D), new Point3D(0.5D, 0.5D, 0.5D));
	 * }
	 * </pre>
	 */
	public RectangularCuboid3D() {
		this(new Point3D(-0.5D, -0.5D, -0.5D), new Point3D(0.5D, 0.5D, 0.5D));
	}
	
	/**
	 * Constructs a new {@code RectangularCuboid3D} instance.
	 * <p>
	 * If either {@code a} or {@code b} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param a a reference {@link Point3D}
	 * @param b a reference {@code Point3D}
	 * @throws NullPointerException thrown if, and only if, either {@code a} or {@code b} are {@code null}
	 */
	public RectangularCuboid3D(final Point3D a, final Point3D b) {
		this.maximum = Point3D.getCached(Point3D.maximum(a, b));
		this.minimum = Point3D.getCached(Point3D.minimum(a, b));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link BoundingVolume3D} instance that contains this {@code RectangularCuboid3D} instance.
	 * 
	 * @return a {@code BoundingVolume3D} instance that contains this {@code RectangularCuboid3D} instance
	 */
	@Override
	public BoundingVolume3D getBoundingVolume() {
		return new AxisAlignedBoundingBox3D(this.maximum, this.minimum);
	}
	
	/**
	 * Performs an intersection test between {@code ray} and this {@code RectangularCuboid3D} instance.
	 * <p>
	 * Returns an {@code Optional} with an optional {@link SurfaceIntersection3D} instance that contains information about the intersection, if it was found.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3D} to perform an intersection test against this {@code RectangularCuboid3D} instance
	 * @param tMinimum the minimum parametric distance
	 * @param tMaximum the maximum parametric distance
	 * @return an {@code Optional} with an optional {@code SurfaceIntersection3D} instance that contains information about the intersection, if it was found
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	@Override
	public Optional<SurfaceIntersection3D> intersection(final Ray3D ray, final double tMinimum, final double tMaximum) {
		final Point3D maximum = getMaximum();
		final Point3D minimum = getMinimum();
		final Point3D origin = ray.getOrigin();
		
		final Vector3D direction = ray.getDirection();
		final Vector3D directionReciprocal = Vector3D.reciprocal(direction);
		
		final double t0X = (minimum.getX() - origin.getX()) * directionReciprocal.getX();
		final double t0Y = (minimum.getY() - origin.getY()) * directionReciprocal.getY();
		final double t0Z = (minimum.getZ() - origin.getZ()) * directionReciprocal.getZ();
		final double t1X = (maximum.getX() - origin.getX()) * directionReciprocal.getX();
		final double t1Y = (maximum.getY() - origin.getY()) * directionReciprocal.getY();
		final double t1Z = (maximum.getZ() - origin.getZ()) * directionReciprocal.getZ();
		
		final double t0 = max(min(t0X, t1X), min(t0Y, t1Y), min(t0Z, t1Z));
		final double t1 = min(max(t0X, t1X), max(t0Y, t1Y), max(t0Z, t1Z));
		
		final double t = t0 > t1 ? Double.NaN : t0 > tMinimum && t0 < tMaximum ? t0 : t1 > tMinimum && t1 < tMaximum ? t1 : Double.NaN;
		
		if(isNaN(t)) {
			return SurfaceIntersection3D.EMPTY;
		}
		
		final Point3D surfaceIntersectionPoint = Point3D.add(origin, direction, t);
		
		final int face = doCalculateFace(surfaceIntersectionPoint, maximum, minimum);
		
		final Point2D textureCoordinates = doCalculateTextureCoordinates(surfaceIntersectionPoint, maximum, minimum, face);
		
		final OrthonormalBasis33D orthonormalBasisG = new OrthonormalBasis33D(doCalculateSurfaceNormalG(face));
		final OrthonormalBasis33D orthonormalBasisS = orthonormalBasisG;
		
		final Vector3D surfaceIntersectionPointError = new Vector3D();
		
		return Optional.of(new SurfaceIntersection3D(orthonormalBasisG, orthonormalBasisS, textureCoordinates, surfaceIntersectionPoint, ray, this, surfaceIntersectionPointError, t));
	}
	
	/**
	 * Returns a {@link Point3D} with the largest component values that are contained in this {@code RectangularCuboid3D} instance.
	 * 
	 * @return a {@code Point3D} with the largest component values that are contained in this {@code RectangularCuboid3D} instance
	 */
	public Point3D getMaximum() {
		return this.maximum;
	}
	
	/**
	 * Returns a {@link Point3D} with the smallest component values that are contained in this {@code RectangularCuboid3D} instance.
	 * 
	 * @return a {@code Point3D} with the smallest component values that are contained in this {@code RectangularCuboid3D} instance
	 */
	public Point3D getMinimum() {
		return this.minimum;
	}
	
	/**
	 * Returns a {@code String} with the name of this {@code RectangularCuboid3D} instance.
	 * 
	 * @return a {@code String} with the name of this {@code RectangularCuboid3D} instance
	 */
	@Override
	public String getName() {
		return NAME;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code RectangularCuboid3D} instance.
	 * 
	 * @return a {@code String} representation of this {@code RectangularCuboid3D} instance
	 */
	@Override
	public String toString() {
		return String.format("new RectangularCuboid3D(%s, %s)", this.maximum, this.minimum);
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
				if(!this.maximum.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
				
				if(!this.minimum.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
			}
			
			return nodeHierarchicalVisitor.visitLeave(this);
		} catch(final RuntimeException e) {
			throw new NodeTraversalException(e);
		}
	}
	
	/**
	 * Compares {@code object} to this {@code RectangularCuboid3D} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code RectangularCuboid3D}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code RectangularCuboid3D} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code RectangularCuboid3D}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof RectangularCuboid3D)) {
			return false;
		} else if(!Objects.equals(this.maximum, RectangularCuboid3D.class.cast(object).maximum)) {
			return false;
		} else if(!Objects.equals(this.minimum, RectangularCuboid3D.class.cast(object).minimum)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns the surface area of this {@code RectangularCuboid3D} instance.
	 * 
	 * @return the surface area of this {@code RectangularCuboid3D} instance
	 */
	@Override
	public double getSurfaceArea() {
		final double x = this.maximum.getX() - this.minimum.getX();
		final double y = this.maximum.getY() - this.minimum.getY();
		final double z = this.maximum.getZ() - this.minimum.getZ();
		final double surfaceArea = 2.0D * (x * y + y * z + z * x);
		
		return surfaceArea;
	}
	
	/**
	 * Returns the volume of this {@code RectangularCuboid3D} instance.
	 * 
	 * @return the volume of this {@code RectangularCuboid3D} instance
	 */
	public double getVolume() {
		final double x = this.maximum.getX() - this.minimum.getX();
		final double y = this.maximum.getY() - this.minimum.getY();
		final double z = this.maximum.getZ() - this.minimum.getZ();
		final double volume = x * y * z;
		
		return volume;
	}
	
	/**
	 * Performs an intersection test between {@code ray} and this {@code RectangularCuboid3D} instance.
	 * <p>
	 * Returns {@code t}, the parametric distance to the surface intersection point, or {@code Double.NaN} if no intersection exists.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3D} to perform an intersection test against this {@code RectangularCuboid3D} instance
	 * @param tMinimum the minimum parametric distance
	 * @param tMaximum the maximum parametric distance
	 * @return {@code t}, the parametric distance to the surface intersection point, or {@code Double.NaN} if no intersection exists
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	@Override
	public double intersectionT(final Ray3D ray, final double tMinimum, final double tMaximum) {
		final Point3D maximum = getMaximum();
		final Point3D minimum = getMinimum();
		final Point3D origin = ray.getOrigin();
		
		final Vector3D direction = ray.getDirection();
		final Vector3D directionReciprocal = Vector3D.reciprocal(direction);
		
		final double t0X = (minimum.getX() - origin.getX()) * directionReciprocal.getX();
		final double t0Y = (minimum.getY() - origin.getY()) * directionReciprocal.getY();
		final double t0Z = (minimum.getZ() - origin.getZ()) * directionReciprocal.getZ();
		final double t1X = (maximum.getX() - origin.getX()) * directionReciprocal.getX();
		final double t1Y = (maximum.getY() - origin.getY()) * directionReciprocal.getY();
		final double t1Z = (maximum.getZ() - origin.getZ()) * directionReciprocal.getZ();
		
		final double t0 = max(min(t0X, t1X), min(t0Y, t1Y), min(t0Z, t1Z));
		final double t1 = min(max(t0X, t1X), max(t0Y, t1Y), max(t0Z, t1Z));
		
		final double t = t0 > t1 ? Double.NaN : t0 > tMinimum && t0 < tMaximum ? t0 : t1 > tMinimum && t1 < tMaximum ? t1 : Double.NaN;
		
		return t;
	}
	
	/**
	 * Returns an {@code int} with the ID of this {@code RectangularCuboid3D} instance.
	 * 
	 * @return an {@code int} with the ID of this {@code RectangularCuboid3D} instance
	 */
	@Override
	public int getID() {
		return ID;
	}
	
	/**
	 * Returns a hash code for this {@code RectangularCuboid3D} instance.
	 * 
	 * @return a hash code for this {@code RectangularCuboid3D} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.maximum, this.minimum);
	}
	
	/**
	 * Writes this {@code RectangularCuboid3D} instance to {@code dataOutput}.
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
			
			this.maximum.write(dataOutput);
			this.minimum.write(dataOutput);
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static Point2D doCalculateTextureCoordinates(final Point3D surfaceIntersectionPoint, final Point3D maximum, final Point3D minimum, final int face) {
		switch(face) {
			case 1:
				return new Point2D(normalize(surfaceIntersectionPoint.getZ(), minimum.getZ(), maximum.getZ()), normalize(surfaceIntersectionPoint.getY(), minimum.getY(), maximum.getY()));
			case 2:
				return new Point2D(normalize(surfaceIntersectionPoint.getZ(), minimum.getZ(), maximum.getZ()), normalize(surfaceIntersectionPoint.getY(), minimum.getY(), maximum.getY()));
			case 3:
				return new Point2D(normalize(surfaceIntersectionPoint.getX(), minimum.getX(), maximum.getX()), normalize(surfaceIntersectionPoint.getZ(), minimum.getZ(), maximum.getZ()));
			case 4:
				return new Point2D(normalize(surfaceIntersectionPoint.getX(), minimum.getX(), maximum.getX()), normalize(surfaceIntersectionPoint.getZ(), minimum.getZ(), maximum.getZ()));
			case 5:
				return new Point2D(normalize(surfaceIntersectionPoint.getX(), minimum.getX(), maximum.getX()), normalize(surfaceIntersectionPoint.getY(), minimum.getY(), maximum.getY()));
			case 6:
				return new Point2D(normalize(surfaceIntersectionPoint.getX(), minimum.getX(), maximum.getX()), normalize(surfaceIntersectionPoint.getY(), minimum.getY(), maximum.getY()));
			default:
				return new Point2D(0.5D, 0.5D);
		}
	}
	
	private static Vector3D doCalculateSurfaceNormalG(final int face) {
		switch(face) {
			case 1:
				return Vector3D.x(-1.0D);
			case 2:
				return Vector3D.x(+1.0D);
			case 3:
				return Vector3D.y(-1.0D);
			case 4:
				return Vector3D.y(+1.0D);
			case 5:
				return Vector3D.z(-1.0D);
			case 6:
				return Vector3D.z(+1.0D);
			default:
				return new Vector3D();
		}
	}
	
	private static int doCalculateFace(final Point3D surfaceIntersectionPoint, final Point3D maximum, final Point3D minimum) {
		final Point3D midpoint = Point3D.midpoint(maximum, minimum);
		
		final double surfaceIntersectionPointX = surfaceIntersectionPoint.getX();
		final double surfaceIntersectionPointY = surfaceIntersectionPoint.getY();
		final double surfaceIntersectionPointZ = surfaceIntersectionPoint.getZ();
		
		final double maximumX = maximum.getX();
		final double maximumY = maximum.getY();
		final double maximumZ = maximum.getZ();
		
		final double minimumX = minimum.getX();
		final double minimumY = minimum.getY();
		final double minimumZ = minimum.getZ();
		
		final double midpointX = midpoint.getX();
		final double midpointY = midpoint.getY();
		final double midpointZ = midpoint.getZ();
		
		final double halfX = (maximumX - minimumX) * 0.5D;
		final double halfY = (maximumY - minimumY) * 0.5D;
		final double halfZ = (maximumZ - minimumZ) * 0.5D;
		
		final double epsilon = 0.0001D;
		
		final int faceX = surfaceIntersectionPointX < midpointX && surfaceIntersectionPointX + halfX - epsilon < midpointX ? -1 : surfaceIntersectionPointX > midpointX && surfaceIntersectionPointX - halfX + epsilon > midpointX ? 1 : 0;
		final int faceY = surfaceIntersectionPointY < midpointY && surfaceIntersectionPointY + halfY - epsilon < midpointY ? -1 : surfaceIntersectionPointY > midpointY && surfaceIntersectionPointY - halfY + epsilon > midpointY ? 1 : 0;
		final int faceZ = surfaceIntersectionPointZ < midpointZ && surfaceIntersectionPointZ + halfZ - epsilon < midpointZ ? -1 : surfaceIntersectionPointZ > midpointZ && surfaceIntersectionPointZ - halfZ + epsilon > midpointZ ? 1 : 0;
		
		final int face = faceX == -1 ? 1 : faceX == 1 ? 2 : faceY == -1 ? 3 : faceY == 1 ? 4 : faceZ == -1 ? 5 : faceZ == 1 ? 6 : 0;
		
		return face;
	}
}