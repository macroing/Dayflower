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
package org.dayflower.scene;

import static org.dayflower.util.Floats.abs;
import static org.dayflower.util.Floats.equal;
import static org.dayflower.util.Floats.isNaN;

import java.util.Objects;
import java.util.Optional;

import org.dayflower.geometry.BoundingVolume3F;
import org.dayflower.geometry.Matrix44F;
import org.dayflower.geometry.Point3F;
import org.dayflower.geometry.Ray3F;
import org.dayflower.geometry.Shape3F;
import org.dayflower.geometry.SurfaceIntersection3F;
import org.dayflower.geometry.SurfaceSample3F;
import org.dayflower.geometry.Vector3F;
import org.dayflower.image.Color3F;

/**
 * A {@code Primitive} represents a primitive and is associated with a {@link Material} instance, a {@link Shape3F} instance and some other properties.
 * <p>
 * This class is mutable and therefore not thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class Primitive {
	private AreaLight areaLight;
	private BoundingVolume3F boundingVolume;
	private Material material;
	private Matrix44F objectToWorld;
	private Matrix44F worldToObject;
	private Shape3F shape;
	private Texture textureAlbedo;
	private Texture textureEmittance;
	private Texture textureNormal;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code Primitive} instance.
	 * <p>
	 * If either {@code material}, {@code shape}, {@code textureAlbedo}, {@code textureEmittance} or {@code textureNormal} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Primitive(material, shape, textureAlbedo, textureEmittance, textureNormal, Matrix44F.identity());
	 * }
	 * </pre>
	 * 
	 * @param material the {@link Material} instance associated with this {@code Primitive} instance
	 * @param shape the {@link Shape3F} instance associated with this {@code Primitive} instance
	 * @param textureAlbedo the {@link Texture} instance for the albedo color that is associated with this {@code Primitive} instance
	 * @param textureEmittance the {@code Texture} instance for the emittance that is associated with this {@code Primitive} instance
	 * @param textureNormal the {@code Texture} instance for the normal that is associated with this {@code Primitive} instance
	 * @throws NullPointerException thrown if, and only if, either {@code material}, {@code shape}, {@code textureAlbedo}, {@code textureEmittance} or {@code textureNormal} are {@code null}
	 */
	public Primitive(final Material material, final Shape3F shape, final Texture textureAlbedo, final Texture textureEmittance, final Texture textureNormal) {
		this(material, shape, textureAlbedo, textureEmittance, textureNormal, Matrix44F.identity());
	}
	
	/**
	 * Constructs a new {@code Primitive} instance.
	 * <p>
	 * If either {@code material}, {@code shape}, {@code textureAlbedo}, {@code textureEmittance}, {@code textureNormal} or {@code objectToWorld} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param material the {@link Material} instance associated with this {@code Primitive} instance
	 * @param shape the {@link Shape3F} instance associated with this {@code Primitive} instance
	 * @param textureAlbedo the {@link Texture} instance for the albedo color that is associated with this {@code Primitive} instance
	 * @param textureEmittance the {@code Texture} instance for the emittance that is associated with this {@code Primitive} instance
	 * @param textureNormal the {@code Texture} instance for the normal that is associated with this {@code Primitive} instance
	 * @param objectToWorld the {@link Matrix44F} instance that is used to transform from object space to world space and is associated with this {@code Primitive} instance
	 * @throws NullPointerException thrown if, and only if, either {@code material}, {@code shape}, {@code textureAlbedo}, {@code textureEmittance}, {@code textureNormal} or {@code objectToWorld} are {@code null}
	 */
	public Primitive(final Material material, final Shape3F shape, final Texture textureAlbedo, final Texture textureEmittance, final Texture textureNormal, final Matrix44F objectToWorld) {
		this.material = Objects.requireNonNull(material, "material == null");
		this.shape = Objects.requireNonNull(shape, "shape == null");
		this.textureAlbedo = Objects.requireNonNull(textureAlbedo, "textureAlbedo == null");
		this.textureEmittance = Objects.requireNonNull(textureEmittance, "textureEmittance == null");
		this.textureNormal = Objects.requireNonNull(textureNormal, "textureNormal == null");
		this.objectToWorld = Objects.requireNonNull(objectToWorld, "objectToWorld == null");
		this.worldToObject = Matrix44F.inverse(objectToWorld);
		this.boundingVolume = shape.getBoundingVolume().transform(objectToWorld);
		this.areaLight = null;
	}
	
	/**
	 * Constructs a new {@code Primitive} instance.
	 * <p>
	 * If either {@code material}, {@code shape}, {@code textureAlbedo}, {@code textureEmittance}, {@code textureNormal}, {@code objectToWorld} or {@code areaLight} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param material the {@link Material} instance associated with this {@code Primitive} instance
	 * @param shape the {@link Shape3F} instance associated with this {@code Primitive} instance
	 * @param textureAlbedo the {@link Texture} instance for the albedo color that is associated with this {@code Primitive} instance
	 * @param textureEmittance the {@code Texture} instance for the emittance that is associated with this {@code Primitive} instance
	 * @param textureNormal the {@code Texture} instance for the normal that is associated with this {@code Primitive} instance
	 * @param objectToWorld the {@link Matrix44F} instance that is used to transform from object space to world space and is associated with this {@code Primitive} instance
	 * @param areaLight the {@link AreaLight} instance associated with this {@code Primitive} instance
	 * @throws NullPointerException thrown if, and only if, either {@code material}, {@code shape}, {@code textureAlbedo}, {@code textureEmittance}, {@code textureNormal}, {@code objectToWorld} or {@code areaLight} are {@code null}
	 */
	public Primitive(final Material material, final Shape3F shape, final Texture textureAlbedo, final Texture textureEmittance, final Texture textureNormal, final Matrix44F objectToWorld, final AreaLight areaLight) {
		this.material = Objects.requireNonNull(material, "material == null");
		this.shape = Objects.requireNonNull(shape, "shape == null");
		this.textureAlbedo = Objects.requireNonNull(textureAlbedo, "textureAlbedo == null");
		this.textureEmittance = Objects.requireNonNull(textureEmittance, "textureEmittance == null");
		this.textureNormal = Objects.requireNonNull(textureNormal, "textureNormal == null");
		this.objectToWorld = Objects.requireNonNull(objectToWorld, "objectToWorld == null");
		this.worldToObject = Matrix44F.inverse(objectToWorld);
		this.boundingVolume = shape.getBoundingVolume().transform(objectToWorld);
		this.areaLight = Objects.requireNonNull(areaLight, "areaLight == null");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the {@link BoundingVolume3F} instance associated with this {@code Primitive} instance.
	 * 
	 * @return the {@code BoundingVolume3F} instance associated with this {@code Primitive} instance
	 */
	public BoundingVolume3F getBoundingVolume() {
		return this.boundingVolume;
	}
	
	/**
	 * Returns a {@link Color3F} instance representing the albedo of the surface at {@code intersection} using an RGB-color space.
	 * <p>
	 * If {@code intersection} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param intersection an {@link Intersection} instance
	 * @return a {@code Color3F} instance representing the albedo of the surface at {@code intersection} using an RGB-color space
	 * @throws NullPointerException thrown if, and only if, {@code intersection} is {@code null}
	 */
	public Color3F calculateAlbedoRGB(final Intersection intersection) {
		return this.textureAlbedo.getColorRGB(Objects.requireNonNull(intersection, "intersection == null"));
	}
	
	/**
	 * Returns a {@link Color3F} instance representing the albedo of the surface at {@code intersection} using an XYZ-color space.
	 * <p>
	 * If {@code intersection} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param intersection an {@link Intersection} instance
	 * @return a {@code Color3F} instance representing the albedo of the surface at {@code intersection} using an XYZ-color space
	 * @throws NullPointerException thrown if, and only if, {@code intersection} is {@code null}
	 */
	public Color3F calculateAlbedoXYZ(final Intersection intersection) {
		return this.textureAlbedo.getColorXYZ(Objects.requireNonNull(intersection, "intersection == null"));
	}
	
	/**
	 * Returns a {@link Color3F} instance representing the emittance of the surface at {@code intersection} using an RGB-color space.
	 * <p>
	 * If {@code intersection} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param intersection an {@link Intersection} instance
	 * @return a {@code Color3F} instance representing the emittance of the surface at {@code intersection} using an RGB-color space
	 * @throws NullPointerException thrown if, and only if, {@code intersection} is {@code null}
	 */
	public Color3F calculateEmittanceRGB(final Intersection intersection) {
		return this.textureEmittance.getColorRGB(Objects.requireNonNull(intersection, "intersection == null"));
	}
	
	/**
	 * Returns a {@link Color3F} instance representing the emittance of the surface at {@code intersection} using an XYZ-color space.
	 * <p>
	 * If {@code intersection} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param intersection an {@link Intersection} instance
	 * @return a {@code Color3F} instance representing the emittance of the surface at {@code intersection} using an XYZ-color space
	 * @throws NullPointerException thrown if, and only if, {@code intersection} is {@code null}
	 */
	public Color3F calculateEmittanceXYZ(final Intersection intersection) {
		return this.textureEmittance.getColorXYZ(Objects.requireNonNull(intersection, "intersection == null"));
	}
	
	/**
	 * Returns the {@link Material} instance associated with this {@code Primitive} instance.
	 * 
	 * @return the {@link Material} instance associated with this {@code Primitive} instance
	 */
	public Material getMaterial() {
		return this.material;
	}
	
	/**
	 * Returns the {@link Matrix44F} instance that is used to transform from object space to world space and is associated with this {@code Primitive} instance.
	 * 
	 * @return the {@code Matrix44F} instance that is used to transform from object space to world space and is associated with this {@code Primitive} instance
	 */
	public Matrix44F getObjectToWorld() {
		return this.objectToWorld;
	}
	
	/**
	 * Returns the {@link Matrix44F} instance that is used to transform from world space to object space and is associated with this {@code Primitive} instance.
	 * 
	 * @return the {@code Matrix44F} instance that is used to transform from world space to object space and is associated with this {@code Primitive} instance
	 */
	public Matrix44F getWorldToObject() {
		return this.worldToObject;
	}
	
	/**
	 * Returns the optional {@link AreaLight} that is associated with this {@code Primitive} instance.
	 * 
	 * @return the optional {@code AreaLight} that is associated with this {@code Primitive} instance
	 */
	public Optional<AreaLight> getAreaLight() {
		return Optional.ofNullable(this.areaLight);
	}
	
	/**
	 * Performs an intersection test between {@code ray} and this {@code Primitive} instance.
	 * <p>
	 * Returns an {@code Optional} with an optional {@link Intersection} instance that contains information about the intersection, if it was found.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3F} to perform an intersection test against this {@code Primitive} instance
	 * @return an {@code Optional} with an optional {@code Intersection} instance that contains information about the intersection, if it was found
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	public Optional<Intersection> intersection(final Ray3F ray) {
		return intersection(ray, 0.0001F, Float.MAX_VALUE);
	}
	
	/**
	 * Performs an intersection test between {@code ray} and this {@code Primitive} instance.
	 * <p>
	 * Returns an {@code Optional} with an optional {@link Intersection} instance that contains information about the intersection, if it was found.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3F} to perform an intersection test against this {@code Primitive} instance
	 * @param tMinimum the minimum parametric distance
	 * @param tMaximum the maximum parametric distance
	 * @return an {@code Optional} with an optional {@code Intersection} instance that contains information about the intersection, if it was found
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	public Optional<Intersection> intersection(final Ray3F ray, final float tMinimum, final float tMaximum) {
		if(this.boundingVolume.intersects(ray, tMinimum, tMaximum)) {
			final Ray3F rayObjectSpace = Ray3F.transform(this.worldToObject, ray);
			
			final float tMinimumObjectSpace = tMinimum;
			final float tMaximumObjectSpace = doTransformT(this.worldToObject, ray, rayObjectSpace, tMaximum);
			
			final Optional<SurfaceIntersection3F> optionalSurfaceIntersectionObjectSpace = this.shape.intersection(rayObjectSpace, tMinimumObjectSpace, tMaximumObjectSpace);
			
			if(optionalSurfaceIntersectionObjectSpace.isPresent()) {
				final SurfaceIntersection3F surfaceIntersectionObjectSpace = optionalSurfaceIntersectionObjectSpace.get();
				
				final Intersection intersection = new Intersection(this, surfaceIntersectionObjectSpace);
				
				final SurfaceIntersection3F surfaceIntersectionWorldSpace = intersection.getSurfaceIntersectionWorldSpace();
				
				final float t = surfaceIntersectionWorldSpace.getT();
				
				if(t > tMinimum && t < tMaximum) {
					return Optional.of(intersection);
				}
			}
		}
		
		return Optional.empty();
	}
	
	/**
	 * Samples this {@code Primitive} instance.
	 * <p>
	 * Returns an optional {@link SurfaceSample3F} with the surface sample.
	 * <p>
	 * If either {@code referencePoint} or {@code referenceSurfaceNormal} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param referencePoint the reference point on this {@code Primitive} instance
	 * @param referenceSurfaceNormal the reference surface normal on this {@code Primitive} instance
	 * @param u a random {@code float} with a uniform distribution between {@code 0.0F} and {@code 1.0F}
	 * @param v a random {@code float} with a uniform distribution between {@code 0.0F} and {@code 1.0F}
	 * @return an optional {@code SurfaceSample3F} with the surface sample
	 * @throws NullPointerException thrown if, and only if, either {@code referencePoint} or {@code referenceSurfaceNormal} are {@code null}
	 */
	public Optional<SurfaceSample3F> sample(final Point3F referencePoint, final Vector3F referenceSurfaceNormal, final float u, final float v) {
		final Optional<SurfaceSample3F> optionalSurfaceSampleObjectSpace = this.shape.sample(Point3F.transformAndDivide(this.worldToObject, referencePoint), Vector3F.transformTranspose(this.objectToWorld, referenceSurfaceNormal), u, v);
		
		if(optionalSurfaceSampleObjectSpace.isPresent()) {
			final SurfaceSample3F surfaceSampleObjectSpace = optionalSurfaceSampleObjectSpace.get();
			final SurfaceSample3F surfaceSampleWorldSpace = SurfaceSample3F.transform(surfaceSampleObjectSpace, this.objectToWorld, this.worldToObject);
			
			if(Vector3F.dotProduct(surfaceSampleWorldSpace.getSurfaceNormal(), Vector3F.direction(surfaceSampleWorldSpace.getPoint(), referencePoint)) >= 0.0F) {
				return Optional.of(surfaceSampleWorldSpace);
			}
		}
		
		return Optional.empty();
	}
	
	/**
	 * Returns the {@link Shape3F} instance associated with this {@code Primitive} instance.
	 * 
	 * @return the {@link Shape3F} instance associated with this {@code Primitive} instance
	 */
	public Shape3F getShape() {
		return this.shape;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code Primitive} instance.
	 * 
	 * @return a {@code String} representation of this {@code Primitive} instance
	 */
	@Override
	public String toString() {
		return String.format("new Primitive(%s, %s, %s, %s, %s, %s)", this.material, this.shape, this.textureAlbedo, this.textureEmittance, this.textureNormal, this.objectToWorld);
	}
	
	/**
	 * Returns the {@link Texture} instance for the albedo color that is associated with this {@code Primitive} instance.
	 * 
	 * @return the {@code Texture} instance for the albedo color that is associated with this {@code Primitive} instance
	 */
	public Texture getTextureAlbedo() {
		return this.textureAlbedo;
	}
	
	/**
	 * Returns the {@link Texture} instance for the emittance that is associated with this {@code Primitive} instance.
	 * 
	 * @return the {@code Texture} instance for the emittance that is associated with this {@code Primitive} instance
	 */
	public Texture getTextureEmittance() {
		return this.textureEmittance;
	}
	
	/**
	 * Returns the {@link Texture} instance for the normal that is associated with this {@code Primitive} instance.
	 * 
	 * @return the {@code Texture} instance for the normal that is associated with this {@code Primitive} instance
	 */
	public Texture getTextureNormal() {
		return this.textureNormal;
	}
	
	/**
	 * Compares {@code object} to this {@code Primitive} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code Primitive}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code Primitive} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code Primitive}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof Primitive)) {
			return false;
		} else if(!Objects.equals(this.boundingVolume, Primitive.class.cast(object).boundingVolume)) {
			return false;
		} else if(!Objects.equals(this.material, Primitive.class.cast(object).material)) {
			return false;
		} else if(!Objects.equals(this.objectToWorld, Primitive.class.cast(object).objectToWorld)) {
			return false;
		} else if(!Objects.equals(this.worldToObject, Primitive.class.cast(object).worldToObject)) {
			return false;
		} else if(!Objects.equals(this.shape, Primitive.class.cast(object).shape)) {
			return false;
		} else if(!Objects.equals(this.textureAlbedo, Primitive.class.cast(object).textureAlbedo)) {
			return false;
		} else if(!Objects.equals(this.textureEmittance, Primitive.class.cast(object).textureEmittance)) {
			return false;
		} else if(!Objects.equals(this.textureNormal, Primitive.class.cast(object).textureNormal)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns {@code true} if, and only if, {@code ray} intersects this {@code Primitive} instance, {@code false} otherwise.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3F} to perform an intersection test against this {@code Primitive} instance
	 * @return {@code true} if, and only if, {@code ray} intersects this {@code Primitive} instance, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	public boolean intersects(final Ray3F ray) {
		return intersects(ray, 0.0001F, Float.MAX_VALUE);
	}
	
	/**
	 * Returns {@code true} if, and only if, {@code ray} intersects this {@code Primitive} instance, {@code false} otherwise.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3F} to perform an intersection test against this {@code Primitive} instance
	 * @param tMinimum the minimum parametric distance
	 * @param tMaximum the maximum parametric distance
	 * @return {@code true} if, and only if, {@code ray} intersects this {@code Primitive} instance, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	public boolean intersects(final Ray3F ray, final float tMinimum, final float tMaximum) {
		return !isNaN(intersectionT(ray, tMinimum, tMaximum));
	}
	
	/**
	 * Returns the probability density function (PDF) value for solid angle.
	 * <p>
	 * If either {@code referencePoint}, {@code referenceSurfaceNormal}, {@code point} or {@code surfaceNormal} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param referencePoint the reference point on this {@code Primitive} instance
	 * @param referenceSurfaceNormal the reference surface normal on this {@code Primitive} instance
	 * @param point the point on this {@code Primitive} instance
	 * @param surfaceNormal the surface normal on this {@code Primitive} instance
	 * @return the probability density function (PDF) value for solid angle
	 * @throws NullPointerException thrown if, and only if, either {@code referencePoint}, {@code referenceSurfaceNormal}, {@code point} or {@code surfaceNormal} are {@code null}
	 */
	public float calculateProbabilityDensityFunctionValueForSolidAngle(final Point3F referencePoint, final Vector3F referenceSurfaceNormal, final Point3F point, final Vector3F surfaceNormal) {
		return this.shape.calculateProbabilityDensityFunctionValueForSolidAngle(Point3F.transformAndDivide(this.worldToObject, referencePoint), Vector3F.transformTranspose(this.objectToWorld, referenceSurfaceNormal), Point3F.transformAndDivide(this.worldToObject, point), Vector3F.transformTranspose(this.objectToWorld, surfaceNormal));
	}
	
	/**
	 * Returns the probability density function (PDF) value for solid angle.
	 * <p>
	 * If either {@code ray} or {@code intersection} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * primitive.calculateProbabilityDensityFunctionValueForSolidAngle(ray.getOrigin(), ray.getDirection(), intersection.getSurfaceIntersectionWorldSpace().getSurfaceIntersectionPoint(), intersection.getSurfaceIntersectionWorldSpace().getSurfaceNormalS());
	 * }
	 * </pre>
	 * 
	 * @param ray a {@link Ray3F} instance
	 * @param intersection an {@link Intersection} instance
	 * @return the probability density function (PDF) value for solid angle
	 * @throws NullPointerException thrown if, and only if, either {@code ray} or {@code intersection} are {@code null}
	 */
	public float calculateProbabilityDensityFunctionValueForSolidAngle(final Ray3F ray, final Intersection intersection) {
		return calculateProbabilityDensityFunctionValueForSolidAngle(ray.getOrigin(), ray.getDirection(), intersection.getSurfaceIntersectionWorldSpace().getSurfaceIntersectionPoint(), intersection.getSurfaceIntersectionWorldSpace().getOrthonormalBasisS().getW());
	}
	
	/**
	 * Performs an intersection test between {@code ray} and this {@code Primitive} instance.
	 * <p>
	 * Returns {@code t}, the parametric distance to the surface intersection point, or {@code Float.NaN} if no intersection exists.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3F} to perform an intersection test against this {@code Primitive} instance
	 * @return {@code t}, the parametric distance to the surface intersection point, or {@code Float.NaN} if no intersection exists
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	public float intersectionT(final Ray3F ray) {
		return intersectionT(ray, 0.0001F, Float.MAX_VALUE);
	}
	
	/**
	 * Performs an intersection test between {@code ray} and this {@code Primitive} instance.
	 * <p>
	 * Returns {@code t}, the parametric distance to the surface intersection point, or {@code Float.NaN} if no intersection exists.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3F} to perform an intersection test against this {@code Primitive} instance
	 * @param tMinimum the minimum parametric distance
	 * @param tMaximum the maximum parametric distance
	 * @return {@code t}, the parametric distance to the surface intersection point, or {@code Float.NaN} if no intersection exists
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	public float intersectionT(final Ray3F ray, final float tMinimum, final float tMaximum) {
		if(!this.boundingVolume.intersects(ray, tMinimum, tMaximum)) {
			return Float.NaN;
		}
		
		final Ray3F rayWorldSpace = ray;
		final Ray3F rayObjectSpace = Ray3F.transform(this.worldToObject, rayWorldSpace);
		
		final float tMinimumObjectSpace = tMinimum;
		final float tMaximumObjectSpace = doTransformT(this.worldToObject, rayWorldSpace, rayObjectSpace, tMaximum);
		
		final float tObjectSpace = this.shape.intersectionT(rayObjectSpace, tMinimumObjectSpace, tMaximumObjectSpace);
		
		if(isNaN(tObjectSpace)) {
			return Float.NaN;
		}
		
		final float tWorldSpace = doTransformT(this.objectToWorld, rayObjectSpace, rayWorldSpace, tObjectSpace);
		
		if(tWorldSpace <= tMinimum || tWorldSpace >= tMaximum) {
			return Float.NaN;
		}
		
		return tWorldSpace;
	}
	
	/**
	 * Returns a hash code for this {@code Primitive} instance.
	 * 
	 * @return a hash code for this {@code Primitive} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.boundingVolume, this.material, this.objectToWorld, this.worldToObject, this.shape, this.textureAlbedo, this.textureEmittance, this.textureNormal);
	}
	
	/**
	 * Sets the {@link AreaLight} instance associated with this {@code Primitive} instance to {@code areaLight}.
	 * <p>
	 * If {@code areaLight} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param areaLight the {@code AreaLight} instance associated with this {@code Primitive} instance
	 * @throws NullPointerException thrown if, and only if, {@code areaLight} is {@code null}
	 */
	public void setAreaLight(final AreaLight areaLight) {
		this.areaLight = Objects.requireNonNull(areaLight, "areaLight == null");
	}
	
	/**
	 * Sets the {@link Material} instance associated with this {@code Primitive} instance to {@code material}.
	 * <p>
	 * If {@code material} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param material the {@code Material} instance associated with this {@code Primitive} instance
	 * @throws NullPointerException thrown if, and only if, {@code material} is {@code null}
	 */
	public void setMaterial(final Material material) {
		this.material = Objects.requireNonNull(material, "material == null");
	}
	
	/**
	 * Sets the {@link Matrix44F} instance that is used to transform from object space to world space.
	 * <p>
	 * If {@code objectToWorld} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code objectToWorld} cannot be inverted, an {@code IllegalStateException} will be thrown.
	 * <p>
	 * This method will also set the {@code Matrix44F} instance that is used to transform from world space to object space.
	 * 
	 * @param objectToWorld the {@code Matrix44F} instance that is used to transform from object space to world space and is associated with this {@code Primitive} instance
	 * @throws IllegalStateException thrown if, and only if, {@code objectToWorld} cannot be inverted
	 * @throws NullPointerException thrown if, and only if, {@code objectToWorld} is {@code null}
	 */
	public void setObjectToWorld(final Matrix44F objectToWorld) {
		this.objectToWorld = Objects.requireNonNull(objectToWorld, "objectToWorld == null");
		this.worldToObject = Matrix44F.inverse(objectToWorld);
		this.boundingVolume = this.shape.getBoundingVolume().transform(this.objectToWorld);
	}
	
	/**
	 * Sets the {@link Shape3F} instance associated with this {@code Primitive} instance to {@code shape}.
	 * <p>
	 * If {@code shape} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param shape the {@code Shape3F} instance associated with this {@code Primitive} instance
	 * @throws NullPointerException thrown if, and only if, {@code shape} is {@code null}
	 */
	public void setShape(final Shape3F shape) {
		this.shape = Objects.requireNonNull(shape, "shape == null");
		this.boundingVolume = this.shape.getBoundingVolume().transform(this.objectToWorld);
	}
	
	/**
	 * Sets the {@link Texture} instance for the albedo color that is associated with this {@code Primitive} instance to {@code textureAlbedo}.
	 * <p>
	 * If {@code textureAlbedo} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param textureAlbedo the {@code Texture} instance for the albedo color that is associated with this {@code Primitive} instance
	 * @throws NullPointerException thrown if, and only if, {@code textureAlbedo} is {@code null}
	 */
	public void setTextureAlbedo(final Texture textureAlbedo) {
		this.textureAlbedo = Objects.requireNonNull(textureAlbedo, "textureAlbedo == null");
	}
	
	/**
	 * Sets the {@link Texture} instance for the emittance that is associated with this {@code Primitive} instance to {@code textureEmittance}.
	 * <p>
	 * If {@code textureEmittance} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param textureEmittance the {@code Texture} instance for the emittance that is associated with this {@code Primitive} instance
	 * @throws NullPointerException thrown if, and only if, {@code textureEmittance} is {@code null}
	 */
	public void setTextureEmittance(final Texture textureEmittance) {
		this.textureEmittance = Objects.requireNonNull(textureEmittance, "textureEmittance == null");
	}
	
	/**
	 * Sets the {@link Texture} instance for the normal that is associated with this {@code Primitive} instance to {@code textureNormal}.
	 * <p>
	 * If {@code textureNormal} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param textureNormal the {@code Texture} instance for the normal that is associated with this {@code Primitive} instance
	 * @throws NullPointerException thrown if, and only if, {@code textureNormal} is {@code null}
	 */
	public void setTextureNormal(final Texture textureNormal) {
		this.textureNormal = Objects.requireNonNull(textureNormal, "textureNormal == null");
	}
	
	/**
	 * Sets the {@link Matrix44F} instance that is used to transform from world space to object space.
	 * <p>
	 * If {@code worldToObject} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code worldToObject} cannot be inverted, an {@code IllegalStateException} will be thrown.
	 * <p>
	 * This method will also set the {@code Matrix44F} instance that is used to transform from object space to world space.
	 * 
	 * @param worldToObject the {@code Matrix44F} instance that is used to transform from world space to object space and is associated with this {@code Primitive} instance
	 * @throws IllegalStateException thrown if, and only if, {@code worldToObject} cannot be inverted
	 * @throws NullPointerException thrown if, and only if, {@code worldToObject} is {@code null}
	 */
	public void setWorldToObject(final Matrix44F worldToObject) {
		this.worldToObject = Objects.requireNonNull(worldToObject, "worldToObject == null");
		this.objectToWorld = Matrix44F.inverse(worldToObject);
		this.boundingVolume = this.shape.getBoundingVolume().transform(this.objectToWorld);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static float doTransformT(final Matrix44F matrix, final Ray3F rayOldSpace, final Ray3F rayNewSpace, final float t) {
		return !equal(t, 0.0F) && t < Float.MAX_VALUE ? abs(Point3F.distance(rayNewSpace.getOrigin(), Point3F.transformAndDivide(matrix, Point3F.add(rayOldSpace.getOrigin(), rayOldSpace.getDirection(), t)))) : t;
	}
}