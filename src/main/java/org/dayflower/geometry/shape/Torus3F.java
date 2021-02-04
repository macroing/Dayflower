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

import static org.dayflower.utility.Doubles.solveQuartic;
import static org.dayflower.utility.Floats.PI;
import static org.dayflower.utility.Floats.PI_DIVIDED_BY_2;
import static org.dayflower.utility.Floats.PI_MULTIPLIED_BY_2;
import static org.dayflower.utility.Floats.PI_MULTIPLIED_BY_2_RECIPROCAL;
import static org.dayflower.utility.Floats.PI_RECIPROCAL;
import static org.dayflower.utility.Floats.asin;
import static org.dayflower.utility.Floats.atan2;
import static org.dayflower.utility.Floats.equal;
import static org.dayflower.utility.Floats.getOrAdd;
import static org.dayflower.utility.Floats.saturate;
import static org.dayflower.utility.Floats.toFloat;

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
import org.dayflower.geometry.boundingvolume.BoundingSphere3F;
import org.dayflower.node.NodeHierarchicalVisitor;
import org.dayflower.node.NodeTraversalException;

/**
 * A {@code Torus3F} denotes a 3-dimensional torus that uses the data type {@code float}.
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class Torus3F implements Shape3F {
	/**
	 * The name of this {@code Torus3F} class.
	 */
	public static final String NAME = "Torus";
	
	/**
	 * The length of the {@code float[]}.
	 */
	public static final int ARRAY_LENGTH = 2;
	
	/**
	 * The offset for the inner radius in the {@code float[]}.
	 */
	public static final int ARRAY_OFFSET_RADIUS_INNER = 0;
	
	/**
	 * The offset for the outer radius in the {@code float[]}.
	 */
	public static final int ARRAY_OFFSET_RADIUS_OUTER = 1;
	
	/**
	 * The ID of this {@code Torus3F} class.
	 */
	public static final int ID = 8;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final BoundingVolume3F boundingVolume;
	private final float radiusInner;
	private final float radiusInnerSquared;
	private final float radiusOuter;
	private final float radiusOuterSquared;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code Torus3F} instance with an inner radius of {@code 0.25F} and an outer radius of {@code 1.0F}.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Torus3F(0.25F, 1.0F);
	 * }
	 * </pre>
	 */
	public Torus3F() {
		this(0.25F, 1.0F);
	}
	
	/**
	 * Constructs a new {@code Torus3F} instance with an inner radius of {@code radiusInner} and an outer radius of {@code radiusOuter}.
	 * 
	 * @param radiusInner the inner radius of this {@code Torus3F} instance
	 * @param radiusOuter the outer radius of this {@code Torus3F} instance
	 */
	public Torus3F(final float radiusInner, final float radiusOuter) {
		this.boundingVolume = new BoundingSphere3F(radiusInner + radiusOuter, new Point3F());
		this.radiusInner = radiusInner;
		this.radiusInnerSquared = radiusInner * radiusInner;
		this.radiusOuter = radiusOuter;
		this.radiusOuterSquared = radiusOuter * radiusOuter;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link BoundingVolume3F} instance that contains this {@code Torus3F} instance.
	 * 
	 * @return a {@code BoundingVolume3F} instance that contains this {@code Torus3F} instance
	 */
	@Override
	public BoundingVolume3F getBoundingVolume() {
		return this.boundingVolume;
	}
	
	/**
	 * Performs an intersection test between {@code ray} and this {@code Torus3F} instance.
	 * <p>
	 * Returns an {@code Optional} with an optional {@link SurfaceIntersection3F} instance that contains information about the intersection, if it was found.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3F} to perform an intersection test against this {@code Torus3F} instance
	 * @param tMinimum the minimum parametric distance
	 * @param tMaximum the maximum parametric distance
	 * @return an {@code Optional} with an optional {@code SurfaceIntersection3F} instance that contains information about the intersection, if it was found
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	@Override
	public Optional<SurfaceIntersection3F> intersection(final Ray3F ray, final float tMinimum, final float tMaximum) {
		final Point3F origin = ray.getOrigin();
		
		final Vector3F direction = ray.getDirection();
		final Vector3F directionToOrigin = new Vector3F(origin);
		
		final double f0 = direction.lengthSquared();
		final double f1 = Vector3F.dotProduct(directionToOrigin, direction) * 2.0D;
		final double f2 = this.radiusInnerSquared;
		final double f3 = this.radiusOuterSquared;
		final double f4 = directionToOrigin.lengthSquared() - f2 - f3;
		final double f5 = direction.getZ();
		final double f6 = directionToOrigin.getZ();
		
		final double a = f0 * f0;
		final double b = f0 * 2.0D * f1;
		final double c = f1 * f1 + 2.0D * f0 * f4 + 4.0D * f3 * f5 * f5;
		final double d = f1 * 2.0D * f4 + 8.0D * f3 * f6 * f5;
		final double e = f4 * f4 + 4.0D * f3 * f6 * f6 - 4.0D * f3 * f2;
		
		final double[] ts = solveQuartic(a, b, c, d, e);
		
		if(ts.length == 0) {
			return Optional.empty();
		}
		
		if(ts[0] >= tMaximum || ts[ts.length - 1] <= tMinimum) {
			return Optional.empty();
		}
		
		for(int i = 0; i < ts.length; i++) {
			if(ts[i] > tMinimum) {
				final float t = toFloat(ts[i]);
				
				final Point3F surfaceIntersectionPoint = Point3F.add(origin, direction, t);
				
				final Point2F textureCoordinates = doCreateTextureCoordinates(surfaceIntersectionPoint, this.radiusInner);
				
				final OrthonormalBasis33F orthonormalBasisG = new OrthonormalBasis33F(doCreateSurfaceNormalG(surfaceIntersectionPoint, this.radiusInnerSquared, this.radiusOuterSquared));
				final OrthonormalBasis33F orthonormalBasisS = orthonormalBasisG;
				
				final Vector3F surfaceIntersectionPointError = new Vector3F();
				
				return Optional.of(new SurfaceIntersection3F(orthonormalBasisG, orthonormalBasisS, textureCoordinates, surfaceIntersectionPoint, ray, this, surfaceIntersectionPointError, t));
			}
		}
		
		return Optional.empty();
	}
	
	/**
	 * Returns a {@code String} with the name of this {@code Torus3F} instance.
	 * 
	 * @return a {@code String} with the name of this {@code Torus3F} instance
	 */
	@Override
	public String getName() {
		return NAME;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code Torus3F} instance.
	 * 
	 * @return a {@code String} representation of this {@code Torus3F} instance
	 */
	@Override
	public String toString() {
		return String.format("new Torus3F(%+.10f, %+.10f)", Float.valueOf(this.radiusInner), Float.valueOf(this.radiusOuter));
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
				if(!this.boundingVolume.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
			}
			
			return nodeHierarchicalVisitor.visitLeave(this);
		} catch(final RuntimeException e) {
			throw new NodeTraversalException(e);
		}
	}
	
	/**
	 * Compares {@code object} to this {@code Torus3F} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code Torus3F}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code Torus3F} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code Torus3F}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof Torus3F)) {
			return false;
		} else if(!Objects.equals(this.boundingVolume, Torus3F.class.cast(object).boundingVolume)) {
			return false;
		} else if(!equal(this.radiusInner, Torus3F.class.cast(object).radiusInner)) {
			return false;
		} else if(!equal(this.radiusInnerSquared, Torus3F.class.cast(object).radiusInnerSquared)) {
			return false;
		} else if(!equal(this.radiusOuter, Torus3F.class.cast(object).radiusOuter)) {
			return false;
		} else if(!equal(this.radiusOuterSquared, Torus3F.class.cast(object).radiusOuterSquared)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns the inner radius of this {@code Torus3F} instance.
	 * 
	 * @return the inner radius of this {@code Torus3F} instance
	 */
	public float getRadiusInner() {
		return this.radiusInner;
	}
	
	/**
	 * Returns the squared inner radius of this {@code Torus3F} instance.
	 * 
	 * @return the squared inner radius of this {@code Torus3F} instance
	 */
	public float getRadiusInnerSquared() {
		return this.radiusInnerSquared;
	}
	
	/**
	 * Returns the outer radius of this {@code Torus3F} instance.
	 * 
	 * @return the outer radius of this {@code Torus3F} instance
	 */
	public float getRadiusOuter() {
		return this.radiusOuter;
	}
	
	/**
	 * Returns the squared outer radius of this {@code Torus3F} instance.
	 * 
	 * @return the squared outer radius of this {@code Torus3F} instance
	 */
	public float getRadiusOuterSquared() {
		return this.radiusOuterSquared;
	}
	
	/**
	 * Returns the surface area of this {@code Torus3F} instance.
	 * 
	 * @return the surface area of this {@code Torus3F} instance
	 */
	@Override
	public float getSurfaceArea() {
		return 4.0F * PI * PI * this.radiusOuter * this.radiusInner;
	}
	
	/**
	 * Returns the volume of this {@code Torus3F} instance.
	 * 
	 * @return the volume of this {@code Torus3F} instance
	 */
	public float getVolume() {
		return 2.0F * PI * PI * this.radiusOuter * this.radiusInnerSquared;
	}
	
	/**
	 * Performs an intersection test between {@code ray} and this {@code Torus3F} instance.
	 * <p>
	 * Returns {@code t}, the parametric distance to the surface intersection point, or {@code Float.NaN} if no intersection exists.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3F} to perform an intersection test against this {@code Torus3F} instance
	 * @param tMinimum the minimum parametric distance
	 * @param tMaximum the maximum parametric distance
	 * @return {@code t}, the parametric distance to the surface intersection point, or {@code Float.NaN} if no intersection exists
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	@Override
	public float intersectionT(final Ray3F ray, final float tMinimum, final float tMaximum) {
		final Point3F origin = ray.getOrigin();
		
		final Vector3F direction = ray.getDirection();
		final Vector3F directionToOrigin = new Vector3F(origin);
		
		final double f0 = direction.lengthSquared();
		final double f1 = Vector3F.dotProduct(directionToOrigin, direction) * 2.0D;
		final double f2 = this.radiusInnerSquared;
		final double f3 = this.radiusOuterSquared;
		final double f4 = directionToOrigin.lengthSquared() - f2 - f3;
		final double f5 = direction.getZ();
		final double f6 = directionToOrigin.getZ();
		
		final double a = f0 * f0;
		final double b = f0 * 2.0D * f1;
		final double c = f1 * f1 + 2.0D * f0 * f4 + 4.0D * f3 * f5 * f5;
		final double d = f1 * 2.0D * f4 + 8.0D * f3 * f6 * f5;
		final double e = f4 * f4 + 4.0D * f3 * f6 * f6 - 4.0D * f3 * f2;
		
		final double[] ts = solveQuartic(a, b, c, d, e);
		
		if(ts.length == 0) {
			return Float.NaN;
		}
		
		if(ts[0] >= tMaximum || ts[ts.length - 1] <= tMinimum) {
			return Float.NaN;
		}
		
		for(int i = 0; i < ts.length; i++) {
			if(ts[i] > tMinimum) {
				return toFloat(ts[i]);
			}
		}
		
		return Float.NaN;
	}
	
	/**
	 * Returns a {@code float[]} representation of this {@code Torus3F} instance.
	 * 
	 * @return a {@code float[]} representation of this {@code Torus3F} instance
	 */
	public float[] toArray() {
		final float[] array = new float[ARRAY_LENGTH];
		
		array[ARRAY_OFFSET_RADIUS_INNER] = this.radiusInner;
		array[ARRAY_OFFSET_RADIUS_OUTER] = this.radiusOuter;
		
		return array;
	}
	
	/**
	 * Returns an {@code int} with the ID of this {@code Torus3F} instance.
	 * 
	 * @return an {@code int} with the ID of this {@code Torus3F} instance
	 */
	@Override
	public int getID() {
		return ID;
	}
	
	/**
	 * Returns a hash code for this {@code Torus3F} instance.
	 * 
	 * @return a hash code for this {@code Torus3F} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.boundingVolume, Float.valueOf(this.radiusInner), Float.valueOf(this.radiusInnerSquared), Float.valueOf(this.radiusOuter), Float.valueOf(this.radiusOuterSquared));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static Point2F doCreateTextureCoordinates(final Point3F surfaceIntersectionPoint, final float radiusInner) {
		final float phi = asin(saturate(surfaceIntersectionPoint.getZ() / radiusInner, -1.0F, 1.0F));
		final float theta = getOrAdd(atan2(surfaceIntersectionPoint.getY(), surfaceIntersectionPoint.getX()), 0.0F, PI_MULTIPLIED_BY_2);
		
		final float u = theta * PI_MULTIPLIED_BY_2_RECIPROCAL;
		final float v = (phi + PI_DIVIDED_BY_2) * PI_RECIPROCAL;
		
		return new Point2F(u, v);
	}
	
	private static Vector3F doCreateSurfaceNormalG(final Point3F surfaceIntersectionPoint, final float radiusInnerSquared, final float radiusOuterSquared) {
		final Vector3F direction = new Vector3F(surfaceIntersectionPoint);
		
		final float derivative = direction.lengthSquared() - radiusInnerSquared - radiusOuterSquared;
		
		final float x = surfaceIntersectionPoint.getX() * derivative;
		final float y = surfaceIntersectionPoint.getY() * derivative;
		final float z = surfaceIntersectionPoint.getZ() * derivative + 2.0F * radiusOuterSquared * surfaceIntersectionPoint.getZ();
		
		return Vector3F.normalize(new Vector3F(x, y, z));
	}
}