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

import static org.dayflower.util.Floats.isNaN;
import static org.dayflower.util.Floats.max;
import static org.dayflower.util.Floats.min;
import static org.dayflower.util.Floats.normalize;

import java.lang.reflect.Field;
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
 * A {@code Sphere3F} denotes a 3-dimensional rectangular cuboid that uses the data type {@code float}.
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class RectangularCuboid3F implements Shape3F {
	/**
	 * The name of this {@code RectangularCuboid3F} class.
	 */
	public static final String NAME = "Rectangular Cuboid";
	
	/**
	 * The length of the {@code float[]}.
	 */
	public static final int ARRAY_LENGTH = 8;
	
	/**
	 * The offset for the {@link Point3F} instance representing the maximum point in the {@code float[]}.
	 */
	public static final int ARRAY_OFFSET_MAXIMUM = 0;
	
	/**
	 * The offset for the {@link Point3F} instance representing the minimum point in the {@code float[]}.
	 */
	public static final int ARRAY_OFFSET_MINIMUM = 3;
	
	/**
	 * The ID of this {@code RectangularCuboid3F} class.
	 */
	public static final int ID = 6;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final Point3F maximum;
	private final Point3F minimum;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code RectangularCuboid3F} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new RectangularCuboid3F(new Point3F(-0.5F, -0.5F, -0.5F), new Point3F(0.5F, 0.5F, 0.5F));
	 * }
	 * </pre>
	 */
	public RectangularCuboid3F() {
		this(new Point3F(-0.5F, -0.5F, -0.5F), new Point3F(0.5F, 0.5F, 0.5F));
	}
	
	/**
	 * Constructs a new {@code RectangularCuboid3F} instance.
	 * <p>
	 * If either {@code a} or {@code b} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param a a reference {@link Point3F}
	 * @param b a reference {@code Point3F}
	 * @throws NullPointerException thrown if, and only if, either {@code a} or {@code b} are {@code null}
	 */
	public RectangularCuboid3F(final Point3F a, final Point3F b) {
		this.maximum = Point3F.getCached(Point3F.maximum(a, b));
		this.minimum = Point3F.getCached(Point3F.minimum(a, b));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link BoundingVolume3F} instance that contains this {@code RectangularCuboid3F} instance.
	 * 
	 * @return a {@code BoundingVolume3F} instance that contains this {@code RectangularCuboid3F} instance
	 */
	@Override
	public BoundingVolume3F getBoundingVolume() {
		return new AxisAlignedBoundingBox3F(this.maximum, this.minimum);
	}
	
	/**
	 * Performs an intersection test between {@code ray} and this {@code RectangularCuboid3F} instance.
	 * <p>
	 * Returns an {@code Optional} with an optional {@link SurfaceIntersection3F} instance that contains information about the intersection, if it was found.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3F} to perform an intersection test against this {@code RectangularCuboid3F} instance
	 * @param tMinimum the minimum parametric distance
	 * @param tMaximum the maximum parametric distance
	 * @return an {@code Optional} with an optional {@code SurfaceIntersection3F} instance that contains information about the intersection, if it was found
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	@Override
	public Optional<SurfaceIntersection3F> intersection(final Ray3F ray, final float tMinimum, final float tMaximum) {
		final Point3F maximum = getMaximum();
		final Point3F minimum = getMinimum();
		final Point3F origin = ray.getOrigin();
		
		final Vector3F direction = ray.getDirection();
		final Vector3F directionReciprocal = Vector3F.reciprocal(direction);
		
		final float t0X = (minimum.getX() - origin.getX()) * directionReciprocal.getX();
		final float t0Y = (minimum.getY() - origin.getY()) * directionReciprocal.getY();
		final float t0Z = (minimum.getZ() - origin.getZ()) * directionReciprocal.getZ();
		final float t1X = (maximum.getX() - origin.getX()) * directionReciprocal.getX();
		final float t1Y = (maximum.getY() - origin.getY()) * directionReciprocal.getY();
		final float t1Z = (maximum.getZ() - origin.getZ()) * directionReciprocal.getZ();
		
		final float t0 = max(min(t0X, t1X), min(t0Y, t1Y), min(t0Z, t1Z));
		final float t1 = min(max(t0X, t1X), max(t0Y, t1Y), max(t0Z, t1Z));
		
		final float t = t0 > t1 ? Float.NaN : t0 > tMinimum && t0 < tMaximum ? t0 : t1 > tMinimum && t1 < tMaximum ? t1 : Float.NaN;
		
		if(isNaN(t)) {
			return SurfaceIntersection3F.EMPTY;
		}
		
		final Point3F surfaceIntersectionPoint = Point3F.add(origin, direction, t);
		
		final int face = doCalculateFace(surfaceIntersectionPoint, maximum, minimum);
		
		final Point2F textureCoordinates = doCalculateTextureCoordinates(surfaceIntersectionPoint, maximum, minimum, face);
		
		final OrthonormalBasis33F orthonormalBasisG = new OrthonormalBasis33F(doCalculateSurfaceNormalG(face));
		final OrthonormalBasis33F orthonormalBasisS = orthonormalBasisG;
		
//		TODO: Implement!
		final Vector3F surfaceIntersectionPointError = new Vector3F();
		
		return Optional.of(new SurfaceIntersection3F(orthonormalBasisG, orthonormalBasisS, textureCoordinates, surfaceIntersectionPoint, ray, this, surfaceIntersectionPointError, t));
	}
	
	/**
	 * Returns a {@link Point3F} with the largest component values that are contained in this {@code RectangularCuboid3F} instance.
	 * 
	 * @return a {@code Point3F} with the largest component values that are contained in this {@code RectangularCuboid3F} instance
	 */
	public Point3F getMaximum() {
		return this.maximum;
	}
	
	/**
	 * Returns a {@link Point3F} with the smallest component values that are contained in this {@code RectangularCuboid3F} instance.
	 * 
	 * @return a {@code Point3F} with the smallest component values that are contained in this {@code RectangularCuboid3F} instance
	 */
	public Point3F getMinimum() {
		return this.minimum;
	}
	
	/**
	 * Returns a {@code String} with the name of this {@code RectangularCuboid3F} instance.
	 * 
	 * @return a {@code String} with the name of this {@code RectangularCuboid3F} instance
	 */
	@Override
	public String getName() {
		return NAME;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code RectangularCuboid3F} instance.
	 * 
	 * @return a {@code String} representation of this {@code RectangularCuboid3F} instance
	 */
	@Override
	public String toString() {
		return String.format("new RectangularCuboid3F(%s, %s)", this.maximum, this.minimum);
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
	 * Compares {@code object} to this {@code RectangularCuboid3F} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code RectangularCuboid3F}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code RectangularCuboid3F} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code RectangularCuboid3F}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof RectangularCuboid3F)) {
			return false;
		} else if(!Objects.equals(this.maximum, RectangularCuboid3F.class.cast(object).maximum)) {
			return false;
		} else if(!Objects.equals(this.minimum, RectangularCuboid3F.class.cast(object).minimum)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns the surface area of this {@code RectangularCuboid3F} instance.
	 * 
	 * @return the surface area of this {@code RectangularCuboid3F} instance
	 */
	@Override
	public float getSurfaceArea() {
		final float x = this.maximum.getX() - this.minimum.getX();
		final float y = this.maximum.getY() - this.minimum.getY();
		final float z = this.maximum.getZ() - this.minimum.getZ();
		final float surfaceArea = 2.0F * (x * y + y * z + z * x);
		
		return surfaceArea;
	}
	
	/**
	 * Returns the volume of this {@code RectangularCuboid3F} instance.
	 * 
	 * @return the volume of this {@code RectangularCuboid3F} instance
	 */
	public float getVolume() {
		final float x = this.maximum.getX() - this.minimum.getX();
		final float y = this.maximum.getY() - this.minimum.getY();
		final float z = this.maximum.getZ() - this.minimum.getZ();
		final float volume = x * y * z;
		
		return volume;
	}
	
	/**
	 * Performs an intersection test between {@code ray} and this {@code RectangularCuboid3F} instance.
	 * <p>
	 * Returns {@code t}, the parametric distance to the surface intersection point, or {@code Float.NaN} if no intersection exists.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3F} to perform an intersection test against this {@code RectangularCuboid3F} instance
	 * @param tMinimum the minimum parametric distance
	 * @param tMaximum the maximum parametric distance
	 * @return {@code t}, the parametric distance to the surface intersection point, or {@code Float.NaN} if no intersection exists
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	@Override
	public float intersectionT(final Ray3F ray, final float tMinimum, final float tMaximum) {
		final Point3F maximum = getMaximum();
		final Point3F minimum = getMinimum();
		final Point3F origin = ray.getOrigin();
		
		final Vector3F direction = ray.getDirection();
		final Vector3F directionReciprocal = Vector3F.reciprocal(direction);
		
		final float t0X = (minimum.getX() - origin.getX()) * directionReciprocal.getX();
		final float t0Y = (minimum.getY() - origin.getY()) * directionReciprocal.getY();
		final float t0Z = (minimum.getZ() - origin.getZ()) * directionReciprocal.getZ();
		final float t1X = (maximum.getX() - origin.getX()) * directionReciprocal.getX();
		final float t1Y = (maximum.getY() - origin.getY()) * directionReciprocal.getY();
		final float t1Z = (maximum.getZ() - origin.getZ()) * directionReciprocal.getZ();
		
		final float t0 = max(min(t0X, t1X), min(t0Y, t1Y), min(t0Z, t1Z));
		final float t1 = min(max(t0X, t1X), max(t0Y, t1Y), max(t0Z, t1Z));
		
		final float t = t0 > t1 ? Float.NaN : t0 > tMinimum && t0 < tMaximum ? t0 : t1 > tMinimum && t1 < tMaximum ? t1 : Float.NaN;
		
		return t;
	}
	
	/**
	 * Returns a {@code float[]} representation of this {@code RectangularCuboid3F} instance.
	 * 
	 * @return a {@code float[]} representation of this {@code RectangularCuboid3F} instance
	 */
	public float[] toArray() {
		final float[] array = new float[ARRAY_LENGTH];
		
		array[ARRAY_OFFSET_MAXIMUM + 0] = this.maximum.getX();	//Block #1
		array[ARRAY_OFFSET_MAXIMUM + 1] = this.maximum.getY();	//Block #1
		array[ARRAY_OFFSET_MAXIMUM + 2] = this.maximum.getZ();	//Block #1
		array[ARRAY_OFFSET_MINIMUM + 0] = this.minimum.getX();	//Block #1
		array[ARRAY_OFFSET_MINIMUM + 1] = this.minimum.getY();	//Block #1
		array[ARRAY_OFFSET_MINIMUM + 2] = this.minimum.getZ();	//Block #1
		array[6] = 0.0F;										//Block #1
		array[7] = 0.0F;										//Block #1
		
		return array;
	}
	
	/**
	 * Returns an {@code int} with the ID of this {@code RectangularCuboid3F} instance.
	 * 
	 * @return an {@code int} with the ID of this {@code RectangularCuboid3F} instance
	 */
	@Override
	public int getID() {
		return ID;
	}
	
	/**
	 * Returns a hash code for this {@code RectangularCuboid3F} instance.
	 * 
	 * @return a hash code for this {@code RectangularCuboid3F} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.maximum, this.minimum);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static Point2F doCalculateTextureCoordinates(final Point3F surfaceIntersectionPoint, final Point3F maximum, final Point3F minimum, final int face) {
		switch(face) {
			case 1:
				return new Point2F(normalize(surfaceIntersectionPoint.getZ(), minimum.getZ(), maximum.getZ()), normalize(surfaceIntersectionPoint.getY(), minimum.getY(), maximum.getY()));
			case 2:
				return new Point2F(normalize(surfaceIntersectionPoint.getZ(), minimum.getZ(), maximum.getZ()), normalize(surfaceIntersectionPoint.getY(), minimum.getY(), maximum.getY()));
			case 3:
				return new Point2F(normalize(surfaceIntersectionPoint.getX(), minimum.getX(), maximum.getX()), normalize(surfaceIntersectionPoint.getZ(), minimum.getZ(), maximum.getZ()));
			case 4:
				return new Point2F(normalize(surfaceIntersectionPoint.getX(), minimum.getX(), maximum.getX()), normalize(surfaceIntersectionPoint.getZ(), minimum.getZ(), maximum.getZ()));
			case 5:
				return new Point2F(normalize(surfaceIntersectionPoint.getX(), minimum.getX(), maximum.getX()), normalize(surfaceIntersectionPoint.getY(), minimum.getY(), maximum.getY()));
			case 6:
				return new Point2F(normalize(surfaceIntersectionPoint.getX(), minimum.getX(), maximum.getX()), normalize(surfaceIntersectionPoint.getY(), minimum.getY(), maximum.getY()));
			default:
				return new Point2F(0.5F, 0.5F);
		}
	}
	
	private static Vector3F doCalculateSurfaceNormalG(final int face) {
		switch(face) {
			case 1:
				return Vector3F.x(-1.0F);
			case 2:
				return Vector3F.x(+1.0F);
			case 3:
				return Vector3F.y(-1.0F);
			case 4:
				return Vector3F.y(+1.0F);
			case 5:
				return Vector3F.z(-1.0F);
			case 6:
				return Vector3F.z(+1.0F);
			default:
				return new Vector3F();
		}
	}
	
	private static int doCalculateFace(final Point3F surfaceIntersectionPoint, final Point3F maximum, final Point3F minimum) {
		final Point3F midpoint = Point3F.midpoint(maximum, minimum);
		
		final float surfaceIntersectionPointX = surfaceIntersectionPoint.getX();
		final float surfaceIntersectionPointY = surfaceIntersectionPoint.getY();
		final float surfaceIntersectionPointZ = surfaceIntersectionPoint.getZ();
		
		final float maximumX = maximum.getX();
		final float maximumY = maximum.getY();
		final float maximumZ = maximum.getZ();
		
		final float minimumX = minimum.getX();
		final float minimumY = minimum.getY();
		final float minimumZ = minimum.getZ();
		
		final float midpointX = midpoint.getX();
		final float midpointY = midpoint.getY();
		final float midpointZ = midpoint.getZ();
		
		final float halfX = (maximumX - minimumX) * 0.5F;
		final float halfY = (maximumY - minimumY) * 0.5F;
		final float halfZ = (maximumZ - minimumZ) * 0.5F;
		
		final float epsilon = 0.0001F;
		
		final int faceX = surfaceIntersectionPointX < midpointX && surfaceIntersectionPointX + halfX - epsilon < midpointX ? -1 : surfaceIntersectionPointX > midpointX && surfaceIntersectionPointX - halfX + epsilon > midpointX ? 1 : 0;
		final int faceY = surfaceIntersectionPointY < midpointY && surfaceIntersectionPointY + halfY - epsilon < midpointY ? -1 : surfaceIntersectionPointY > midpointY && surfaceIntersectionPointY - halfY + epsilon > midpointY ? 1 : 0;
		final int faceZ = surfaceIntersectionPointZ < midpointZ && surfaceIntersectionPointZ + halfZ - epsilon < midpointZ ? -1 : surfaceIntersectionPointZ > midpointZ && surfaceIntersectionPointZ - halfZ + epsilon > midpointZ ? 1 : 0;
		
		final int face = faceX == -1 ? 1 : faceX == 1 ? 2 : faceY == -1 ? 3 : faceY == 1 ? 4 : faceZ == -1 ? 5 : faceZ == 1 ? 6 : 0;
		
		return face;
	}
}