/**
 * Copyright 2020 J&#246;rgen Lundgren
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
import static org.dayflower.util.Floats.minOrNaN;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.dayflower.geometry.BoundingVolume3F;
import org.dayflower.geometry.MutableSurfaceIntersection3F;
import org.dayflower.geometry.Point3F;
import org.dayflower.geometry.Ray3F;
import org.dayflower.geometry.Shape3F;
import org.dayflower.geometry.SurfaceIntersection3F;
import org.dayflower.geometry.SurfaceSample3F;
import org.dayflower.geometry.Vector3F;
import org.dayflower.geometry.boundingvolume.AxisAlignedBoundingBox3F;
import org.dayflower.util.ParameterArguments;

/**
 * A {@code Curves3F} is a container of {@link Curve3F} instances.
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class Curves3F implements Shape3F {
	private final List<Curve3F> curves;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code Curves3F} instance.
	 * <p>
	 * If either {@code curves} or at least one of its elements are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Modifying {@code curves} will not affect this {@code Curves3F} instance.
	 * 
	 * @param curves a {@code List} of {@link Curve3F} instances
	 * @throws NullPointerException thrown if, and only if, either {@code curves} or at least one of its elements are {@code null}
	 */
	public Curves3F(final List<Curve3F> curves) {
		this.curves = new ArrayList<>(ParameterArguments.requireNonNullList(curves, "curves"));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link BoundingVolume3F} instance that contains this {@code Curves3F} instance.
	 * 
	 * @return a {@code BoundingVolume3F} instance that contains this {@code Curves3F} instance
	 */
	@Override
	public BoundingVolume3F getBoundingVolume() {
		return doGetAxisAlignedBoundingBoxes().stream().reduce((a, b) -> AxisAlignedBoundingBox3F.union(a, b)).orElse(new AxisAlignedBoundingBox3F(new Point3F(), new Point3F()));
	}
	
	/**
	 * Samples this {@code Curves3F} instance.
	 * <p>
	 * Returns an optional {@link SurfaceSample3F} with the surface sample.
	 * <p>
	 * If either {@code referencePoint} or {@code referenceSurfaceNormal} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Note: This method has not been implemented yet.
	 * 
	 * @param referencePoint the reference point on this {@code Curves3F} instance
	 * @param referenceSurfaceNormal the reference surface normal on this {@code Curves3F} instance
	 * @param u a random {@code float} with a uniform distribution between {@code 0.0F} and {@code 1.0F}
	 * @param v a random {@code float} with a uniform distribution between {@code 0.0F} and {@code 1.0F}
	 * @return an optional {@code SurfaceSample3F} with the surface sample
	 * @throws NullPointerException thrown if, and only if, either {@code referencePoint} or {@code referenceSurfaceNormal} are {@code null}
	 */
	@Override
	public Optional<SurfaceSample3F> sample(final Point3F referencePoint, final Vector3F referenceSurfaceNormal, final float u, final float v) {
		Objects.requireNonNull(referencePoint, "referencePoint == null");
		Objects.requireNonNull(referenceSurfaceNormal, "referenceSurfaceNormal == null");
		
		return SurfaceSample3F.EMPTY;//TODO: Implement!
	}
	
	/**
	 * Performs an intersection test between {@code ray} and this {@code Curves3F} instance.
	 * <p>
	 * Returns an {@code Optional} with an optional {@link SurfaceIntersection3F} instance that contains information about the intersection, if it was found.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3F} to perform an intersection test against this {@code Curves3F} instance
	 * @param tMinimum the minimum parametric distance
	 * @param tMaximum the maximum parametric distance
	 * @return an {@code Optional} with an optional {@code SurfaceIntersection3F} instance that contains information about the intersection, if it was found
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	@Override
	public Optional<SurfaceIntersection3F> intersection(final Ray3F ray, final float tMinimum, final float tMaximum) {
		final MutableSurfaceIntersection3F mutableSurfaceIntersection = new MutableSurfaceIntersection3F(ray, tMinimum, tMaximum);
		
		for(final Curve3F curve : this.curves) {
			mutableSurfaceIntersection.intersection(curve);
		}
		
		return mutableSurfaceIntersection.computeSurfaceIntersection();
	}
	
	/**
	 * Returns a {@code String} representation of this {@code Curves3F} instance.
	 * 
	 * @return a {@code String} representation of this {@code Curves3F} instance
	 */
	@Override
	public String toString() {
		return "new Curves3F(...)";
	}
	
	/**
	 * Compares {@code object} to this {@code Curves3F} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code Curves3F}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code Curves3F} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code Curves3F}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof Curves3F)) {
			return false;
		} else if(!Objects.equals(this.curves, Curves3F.class.cast(object).curves)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns the probability density function (PDF) value for solid angle.
	 * <p>
	 * If either {@code referencePoint}, {@code referenceSurfaceNormal}, {@code point} or {@code surfaceNormal} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Note: This method has not been implemented yet.
	 * 
	 * @param referencePoint the reference point on this {@code Curves3F} instance
	 * @param referenceSurfaceNormal the reference surface normal on this {@code Curves3F} instance
	 * @param point the point on this {@code Curves3F} instance
	 * @param surfaceNormal the surface normal on this {@code Curves3F} instance
	 * @return the probability density function (PDF) value for solid angle
	 * @throws NullPointerException thrown if, and only if, either {@code referencePoint}, {@code referenceSurfaceNormal}, {@code point} or {@code surfaceNormal} are {@code null}
	 */
	@Override
	public float calculateProbabilityDensityFunctionValueForSolidAngle(final Point3F referencePoint, final Vector3F referenceSurfaceNormal, final Point3F point, final Vector3F surfaceNormal) {
		Objects.requireNonNull(referencePoint, "referencePoint == null");
		Objects.requireNonNull(referenceSurfaceNormal, "referenceSurfaceNormal == null");
		Objects.requireNonNull(point, "point == null");
		Objects.requireNonNull(surfaceNormal, "surfaceNormal == null");
		
		return 0.0F;//TODO: Implement!
	}
	
	/**
	 * Returns the probability density function (PDF) value for solid angle.
	 * <p>
	 * If either {@code referencePoint}, {@code referenceSurfaceNormal} or {@code direction} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Note: This method has not been implemented yet.
	 * 
	 * @param referencePoint the reference point on this {@code Curves3F} instance
	 * @param referenceSurfaceNormal the reference surface normal on this {@code Curves3F} instance
	 * @param direction the direction to this {@code Curves3F} instance
	 * @return the probability density function (PDF) value for solid angle
	 * @throws NullPointerException thrown if, and only if, either {@code referencePoint}, {@code referenceSurfaceNormal} or {@code direction} are {@code null}
	 */
	@Override
	public float calculateProbabilityDensityFunctionValueForSolidAngle(final Point3F referencePoint, final Vector3F referenceSurfaceNormal, final Vector3F direction) {
		Objects.requireNonNull(referencePoint, "referencePoint == null");
		Objects.requireNonNull(referenceSurfaceNormal, "referenceSurfaceNormal == null");
		Objects.requireNonNull(direction, "direction == null");
		
		return 0.0F;//TODO: Implement!
	}
	
	/**
	 * Returns the surface area of this {@code Curves3F} instance.
	 * 
	 * @return the surface area of this {@code Curves3F} instance
	 */
	@Override
	public float getSurfaceArea() {
		float surfaceArea = 0.0F;
		
		for(final Curve3F curve : this.curves) {
			surfaceArea += curve.getSurfaceArea();
		}
		
		return surfaceArea;
	}
	
	/**
	 * Returns the surface area probability density function (PDF) value of this {@code Curves3F} instance.
	 * <p>
	 * Note: This method has not been implemented yet.
	 * 
	 * @return the surface area probability density function (PDF) value of this {@code Curves3F} instance
	 */
	@Override
	public float getSurfaceAreaProbabilityDensityFunctionValue() {
		return 0.0F;//TODO: Implement!
	}
	
	/**
	 * Returns the volume of this {@code Curves3F} instance.
	 * <p>
	 * Note: This method has not been implemented yet.
	 * 
	 * @return the volume of this {@code Curves3F} instance
	 */
	@Override
	public float getVolume() {
		return 0.0F;//TODO: Implement!
	}
	
	/**
	 * Performs an intersection test between {@code ray} and this {@code Curves3F} instance.
	 * <p>
	 * Returns {@code t}, the parametric distance to the surface intersection point, or {@code Float.NaN} if no intersection exists.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3F} to perform an intersection test against this {@code Curves3F} instance
	 * @param tMinimum the minimum parametric distance
	 * @param tMaximum the maximum parametric distance
	 * @return {@code t}, the parametric distance to the surface intersection point, or {@code Float.NaN} if no intersection exists
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	@Override
	public float intersectionT(final Ray3F ray, final float tMinimum, final float tMaximum) {
		float t = Float.NaN;
		float tMax = tMaximum;
		float tMin = tMinimum;
		
		for(final Curve3F curve : this.curves) {
			t = minOrNaN(t, curve.intersectionT(ray, tMin, tMax));
			
			if(!isNaN(t)) {
				tMax = t;
			}
		}
		
		return t;
	}
	
	/**
	 * Returns a {@code float[]} representation of this {@code Curves3F} instance.
	 * <p>
	 * Note: This method has not been implemented yet.
	 * 
	 * @return a {@code float[]} representation of this {@code Curves3F} instance
	 */
	@Override
	public float[] toArray() {
		return new float[0];//TODO: Implement!
	}
	
	/**
	 * Returns a hash code for this {@code Curves3F} instance.
	 * 
	 * @return a hash code for this {@code Curves3F} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.curves);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private List<AxisAlignedBoundingBox3F> doGetAxisAlignedBoundingBoxes() {
		return doGetBoundingVolumes().stream().filter(boundingVolume -> boundingVolume instanceof AxisAlignedBoundingBox3F).map(boundingVolume -> AxisAlignedBoundingBox3F.class.cast(boundingVolume)).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
	}
	
	private List<BoundingVolume3F> doGetBoundingVolumes() {
		return this.curves.stream().map(curve -> curve.getBoundingVolume()).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
	}
}