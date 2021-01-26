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
package org.dayflower.scene;

import static org.dayflower.utility.Floats.abs;
import static org.dayflower.utility.Floats.isNaN;
import static org.dayflower.utility.Floats.isZero;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.dayflower.geometry.BoundingVolume3F;
import org.dayflower.geometry.Matrix44F;
import org.dayflower.geometry.Point2F;
import org.dayflower.geometry.Point3F;
import org.dayflower.geometry.Quaternion4F;
import org.dayflower.geometry.Ray3F;
import org.dayflower.geometry.Shape3F;
import org.dayflower.geometry.SurfaceIntersection3F;
import org.dayflower.geometry.SurfaceSample3F;
import org.dayflower.geometry.Vector3F;
import org.dayflower.node.Node;
import org.dayflower.node.NodeHierarchicalVisitor;
import org.dayflower.node.NodeTraversalException;
import org.dayflower.utility.ParameterArguments;

/**
 * A {@code Primitive} represents a primitive and is associated with a {@link Material} instance, a {@link Shape3F} instance and some other properties.
 * <p>
 * This class is mutable and therefore not thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class Primitive implements Node {
	/**
	 * The length of the {@code int[]}.
	 */
	public static final int ARRAY_LENGTH = 8;
	
	/**
	 * The offset for the {@link AreaLight} ID in the {@code int[]}.
	 */
	public static final int ARRAY_OFFSET_AREA_LIGHT_ID = 0;
	
	/**
	 * The offset for the {@link AreaLight} offset in the {@code int[]}.
	 */
	public static final int ARRAY_OFFSET_AREA_LIGHT_OFFSET = 1;
	
	/**
	 * The offset for the {@link BoundingVolume3F} ID in the {@code int[]}.
	 */
	public static final int ARRAY_OFFSET_BOUNDING_VOLUME_ID = 2;
	
	/**
	 * The offset for the {@link BoundingVolume3F} offset in the {@code int[]}.
	 */
	public static final int ARRAY_OFFSET_BOUNDING_VOLUME_OFFSET = 3;
	
	/**
	 * The offset for the {@link Material} ID in the {@code int[]}.
	 */
	public static final int ARRAY_OFFSET_MATERIAL_ID = 4;
	
	/**
	 * The offset for the {@link Material} offset in the {@code int[]}.
	 */
	public static final int ARRAY_OFFSET_MATERIAL_OFFSET = 5;
	
	/**
	 * The offset for the {@link Shape3F} ID in the {@code int[]}.
	 */
	public static final int ARRAY_OFFSET_SHAPE_ID = 6;
	
	/**
	 * The offset for the {@link Shape3F} offset in the {@code int[]}.
	 */
	public static final int ARRAY_OFFSET_SHAPE_OFFSET = 7;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private AreaLight areaLight;
	private BoundingVolume3F boundingVolume;
	private final List<PrimitiveObserver> primitiveObservers;
	private Material material;
	private Shape3F shape;
	private Transform transform;
	private final TransformObserver transformObserver;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code Primitive} instance.
	 * <p>
	 * If either {@code material} or {@code shape} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Primitive(material, shape, new Transform());
	 * }
	 * </pre>
	 * 
	 * @param material the {@link Material} instance associated with this {@code Primitive} instance
	 * @param shape the {@link Shape3F} instance associated with this {@code Primitive} instance
	 * @throws NullPointerException thrown if, and only if, either {@code material} or {@code shape} are {@code null}
	 */
	public Primitive(final Material material, final Shape3F shape) {
		this(material, shape, new Transform());
	}
	
	/**
	 * Constructs a new {@code Primitive} instance.
	 * <p>
	 * If either {@code material}, {@code shape} or {@code transform} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param material the {@link Material} instance associated with this {@code Primitive} instance
	 * @param shape the {@link Shape3F} instance associated with this {@code Primitive} instance
	 * @param transform the {@link Transform} instance that is associated with this {@code Primitive} instance
	 * @throws NullPointerException thrown if, and only if, either {@code material}, {@code shape} or {@code transform} are {@code null}
	 */
	public Primitive(final Material material, final Shape3F shape, final Transform transform) {
		this.areaLight = null;
		this.boundingVolume = shape.getBoundingVolume().transform(transform.getObjectToWorld());
		this.primitiveObservers = new ArrayList<>();
		this.material = Objects.requireNonNull(material, "material == null");
		this.shape = shape;
		this.transformObserver = new TransformObserverImpl(this::doSetBoundingVolume);
		this.transform = transform;
		this.transform.addTransformObserver(this.transformObserver);
	}
	
	/**
	 * Constructs a new {@code Primitive} instance.
	 * <p>
	 * If either {@code material}, {@code shape}, {@code transform} or {@code areaLight} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param material the {@link Material} instance associated with this {@code Primitive} instance
	 * @param shape the {@link Shape3F} instance associated with this {@code Primitive} instance
	 * @param transform the {@link Transform} instance that is associated with this {@code Primitive} instance
	 * @param areaLight the {@link AreaLight} instance associated with this {@code Primitive} instance
	 * @throws NullPointerException thrown if, and only if, either {@code material}, {@code shape}, {@code transform} or {@code areaLight} are {@code null}
	 */
	public Primitive(final Material material, final Shape3F shape, final Transform transform, final AreaLight areaLight) {
		this.areaLight = Objects.requireNonNull(areaLight, "areaLight == null");
		this.boundingVolume = shape.getBoundingVolume().transform(transform.getObjectToWorld());
		this.primitiveObservers = new ArrayList<>();
		this.material = Objects.requireNonNull(material, "material == null");
		this.shape = shape;
		this.transformObserver = new TransformObserverImpl(this::doSetBoundingVolume);
		this.transform = transform;
		this.transform.addTransformObserver(this.transformObserver);
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
	 * Returns a {@code List} with all {@link PrimitiveObserver} instances currently associated with this {@code Primitive} instance.
	 * <p>
	 * Modifying the returned {@code List} will not affect this {@code Primitive} instance.
	 * 
	 * @return a {@code List} with all {@code PrimitiveObserver} instances currently associated with this {@code Primitive} instance
	 */
	public List<PrimitiveObserver> getPrimitiveObservers() {
		return new ArrayList<>(this.primitiveObservers);
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
	 * @param tMinimum the minimum parametric distance
	 * @param tMaximum the maximum parametric distance
	 * @return an {@code Optional} with an optional {@code Intersection} instance that contains information about the intersection, if it was found
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	public Optional<Intersection> intersection(final Ray3F ray, final float tMinimum, final float tMaximum) {
		final BoundingVolume3F boundingVolume = getBoundingVolume();
		
		if(boundingVolume.contains(ray.getOrigin()) || boundingVolume.intersects(ray, tMinimum, tMaximum)) {
			final Transform transform = getTransform();
			
			final Matrix44F worldToObject = transform.getWorldToObject();
			
			final Ray3F rayObjectSpace = Ray3F.transform(worldToObject, ray);
			
			final float tMinimumObjectSpace = tMinimum;
			final float tMaximumObjectSpace = doTransformT(worldToObject, ray, rayObjectSpace, tMaximum);
			
			final Shape3F shape = getShape();
			
			final Optional<SurfaceIntersection3F> optionalSurfaceIntersectionObjectSpace = shape.intersection(rayObjectSpace, tMinimumObjectSpace, tMaximumObjectSpace);
			
			if(optionalSurfaceIntersectionObjectSpace.isPresent()) {
				final SurfaceIntersection3F surfaceIntersectionObjectSpace = optionalSurfaceIntersectionObjectSpace.get();
				
				final Intersection intersection = new Intersection(this, surfaceIntersectionObjectSpace);
				
				final float t = intersection.getT();
				
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
	 * Returns an optional {@link Sample} with the sample.
	 * <p>
	 * If either {@code sample} or {@code intersection} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param sample a {@link Point2F} instance with a sample point
	 * @param intersection an {@link Intersection} instance
	 * @return an optional {@code Sample} with the sample
	 * @throws NullPointerException thrown if, and only if, either {@code sample} or {@code intersection} are {@code null}
	 */
	public Optional<Sample> sample(final Point2F sample, final Intersection intersection) {
		Objects.requireNonNull(sample, "sample == null");
		Objects.requireNonNull(intersection, "intersection == null");
		
		final Transform transform = getTransform();
		
		final Matrix44F objectToWorld = transform.getObjectToWorld();
		final Matrix44F worldToObject = transform.getWorldToObject();
		
		final Shape3F shape = getShape();
		
		final SurfaceIntersection3F surfaceIntersectionWorldSpace = intersection.getSurfaceIntersectionWorldSpace();
		final SurfaceIntersection3F surfaceIntersectionObjectSpace = SurfaceIntersection3F.transform(surfaceIntersectionWorldSpace, worldToObject, objectToWorld);
		
		final Optional<SurfaceSample3F> optionalSurfaceSample = shape.sample(sample, surfaceIntersectionObjectSpace);
		
		if(optionalSurfaceSample.isPresent()) {
			final SurfaceSample3F surfaceSampleObjectSpace = optionalSurfaceSample.get();
			
			return Optional.of(new Sample(this, surfaceSampleObjectSpace));
		}
		
		return Sample.EMPTY;
	}
	
	/**
	 * Returns the {@link Shape3F} instance associated with this {@code Primitive} instance.
	 * 
	 * @return the {@code Shape3F} instance associated with this {@code Primitive} instance
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
		return String.format("new Primitive(%s, %s, %s)", this.material, this.shape, this.transform);
	}
	
	/**
	 * Returns the {@link Transform} instance associated with this {@code Primitive} instance.
	 * 
	 * @return the {@code Transform} instance associated with this {@code Primitive} instance
	 */
	public Transform getTransform() {
		return this.transform;
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
				if(this.areaLight != null && !this.areaLight.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
				
				if(!this.boundingVolume.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
				
				if(!this.material.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
				
				if(!this.shape.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
				
				if(!this.transform.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
			}
			
			return nodeHierarchicalVisitor.visitLeave(this);
		} catch(final RuntimeException e) {
			throw new NodeTraversalException(e);
		}
	}
	
	/**
	 * Adds {@code primitiveObserver} to this {@code Primitive} instance.
	 * <p>
	 * Returns {@code true} if, and only if, {@code primitiveObserver} was added, {@code false} otherwise.
	 * <p>
	 * If {@code primitiveObserver} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param primitiveObserver the {@link PrimitiveObserver} instance to add
	 * @return {@code true} if, and only if, {@code primitiveObserver} was added, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, {@code primitiveObserver} is {@code null}
	 */
	public boolean addPrimitiveObserver(final PrimitiveObserver primitiveObserver) {
		return this.primitiveObservers.add(Objects.requireNonNull(primitiveObserver, "primitiveObserver == null"));
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
		} else if(!Objects.equals(this.areaLight, Primitive.class.cast(object).areaLight)) {
			return false;
		} else if(!Objects.equals(this.boundingVolume, Primitive.class.cast(object).boundingVolume)) {
			return false;
		} else if(!Objects.equals(this.primitiveObservers, Primitive.class.cast(object).primitiveObservers)) {
			return false;
		} else if(!Objects.equals(this.material, Primitive.class.cast(object).material)) {
			return false;
		} else if(!Objects.equals(this.shape, Primitive.class.cast(object).shape)) {
			return false;
		} else if(!Objects.equals(this.transform, Primitive.class.cast(object).transform)) {
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
	 * @param tMinimum the minimum parametric distance
	 * @param tMaximum the maximum parametric distance
	 * @return {@code true} if, and only if, {@code ray} intersects this {@code Primitive} instance, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	public boolean intersects(final Ray3F ray, final float tMinimum, final float tMaximum) {
		return !isNaN(intersectionT(ray, tMinimum, tMaximum));
	}
	
	/**
	 * Removes {@code primitiveObserver} from this {@code Primitive} instance, if present.
	 * <p>
	 * Returns {@code true} if, and only if, {@code primitiveObserver} was removed, {@code false} otherwise.
	 * <p>
	 * If {@code primitiveObserver} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param primitiveObserver the {@link PrimitiveObserver} instance to remove
	 * @return {@code true} if, and only if, {@code primitiveObserver} was removed, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, {@code primitiveObserver} is {@code null}
	 */
	public boolean removePrimitiveObserver(final PrimitiveObserver primitiveObserver) {
		return this.primitiveObservers.remove(Objects.requireNonNull(primitiveObserver, "primitiveObserver == null"));
	}
	
	/**
	 * Evaluates the probability density function (PDF) for {@code intersection} and {@code incoming}.
	 * <p>
	 * Returns the probability density function (PDF) value.
	 * <p>
	 * If either {@code intersection} or {@code incoming} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param intersection an {@link Intersection} instance
	 * @param incoming a {@link Vector3F} instance with the incoming direction
	 * @return the probability density function (PDF) value
	 * @throws NullPointerException thrown if, and only if, either {@code intersection} or {@code incoming} are {@code null}
	 */
	public float evaluateProbabilityDensityFunction(final Intersection intersection, final Vector3F incoming) {
		final Transform transform = getTransform();
		
		final Matrix44F objectToWorld = transform.getObjectToWorld();
		final Matrix44F worldToObject = transform.getWorldToObject();
		
		final Shape3F shape = getShape();
		
		return shape.evaluateProbabilityDensityFunction(SurfaceIntersection3F.transform(intersection.getSurfaceIntersectionWorldSpace(), worldToObject, objectToWorld), Vector3F.transform(worldToObject, incoming));
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
		final BoundingVolume3F boundingVolume = getBoundingVolume();
		
		if(!boundingVolume.contains(ray.getOrigin()) && !boundingVolume.intersects(ray, tMinimum, tMaximum)) {
			return Float.NaN;
		}
		
		final Transform transform = getTransform();
		
		final Matrix44F objectToWorld = transform.getObjectToWorld();
		final Matrix44F worldToObject = transform.getWorldToObject();
		
		final Ray3F rayWorldSpace = ray;
		final Ray3F rayObjectSpace = Ray3F.transform(worldToObject, rayWorldSpace);
		
		final Shape3F shape = getShape();
		
		final float tMinimumObjectSpace = tMinimum;
		final float tMaximumObjectSpace = doTransformT(worldToObject, rayWorldSpace, rayObjectSpace, tMaximum);
		
		final float tObjectSpace = shape.intersectionT(rayObjectSpace, tMinimumObjectSpace, tMaximumObjectSpace);
		
		if(isNaN(tObjectSpace)) {
			return Float.NaN;
		}
		
		final float tWorldSpace = doTransformT(objectToWorld, rayObjectSpace, rayWorldSpace, tObjectSpace);
		
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
		return Objects.hash(this.areaLight, this.boundingVolume, this.primitiveObservers, this.material, this.shape, this.transform);
	}
	
	/**
	 * Returns an {@code int[]} representation of this {@code Primitive} instance.
	 * 
	 * @return an {@code int[]} representation of this {@code Primitive} instance
	 */
	public int[] toArray() {
		final int[] array = new int[ARRAY_LENGTH];
		
		array[ARRAY_OFFSET_AREA_LIGHT_ID] = 0;
		array[ARRAY_OFFSET_AREA_LIGHT_OFFSET] = 0;
		array[ARRAY_OFFSET_BOUNDING_VOLUME_ID] = this.boundingVolume.getID();
		array[ARRAY_OFFSET_BOUNDING_VOLUME_OFFSET] = 0;
		array[ARRAY_OFFSET_MATERIAL_ID] = this.material.getID();
		array[ARRAY_OFFSET_MATERIAL_OFFSET] = 0;
		array[ARRAY_OFFSET_SHAPE_ID] = this.shape.getID();
		array[ARRAY_OFFSET_SHAPE_OFFSET] = 0;
		
		return array;
	}
	
	/**
	 * Clears the {@link AreaLight} instance associated with this {@code Primitive} instance.
	 */
	public void clearAreaLight() {
		final AreaLight areaLight = this.areaLight;
		
		if(!Objects.equals(areaLight, null)) {
			final Optional<AreaLight> oldOptionalAreaLight = Optional.ofNullable(areaLight);
			final Optional<AreaLight> newOptionalAreaLight = Optional.empty();
			
			this.areaLight = null;
			
			for(final PrimitiveObserver primitiveObserver : this.primitiveObservers) {
				primitiveObserver.onChangeAreaLight(this, oldOptionalAreaLight, newOptionalAreaLight);
			}
		}
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
		Objects.requireNonNull(areaLight, "areaLight == null");
		
		if(!Objects.equals(this.areaLight, areaLight)) {
			final Optional<AreaLight> oldOptionalAreaLight = Optional.ofNullable(this.areaLight);
			final Optional<AreaLight> newOptionalAreaLight = Optional.of(areaLight);
			
			this.areaLight = areaLight;
			
			for(final PrimitiveObserver primitiveObserver : this.primitiveObservers) {
				primitiveObserver.onChangeAreaLight(this, oldOptionalAreaLight, newOptionalAreaLight);
			}
		}
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
		Objects.requireNonNull(material, "material == null");
		
		if(!Objects.equals(this.material, material)) {
			final Material oldMaterial = this.material;
			final Material newMaterial =      material;
			
			this.material = material;
			
			for(final PrimitiveObserver primitiveObserver : this.primitiveObservers) {
				primitiveObserver.onChangeMaterial(this, oldMaterial, newMaterial);
			}
		}
	}
	
	/**
	 * Sets the {@code List} with all {@link PrimitiveObserver} instances associated with this {@code Primitive} instance to a copy of {@code primitiveObservers}.
	 * <p>
	 * If either {@code primitiveObservers} or at least one of its elements are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param primitiveObservers a {@code List} with all {@code PrimitiveObserver} instances associated with this {@code Primitive} instance
	 * @throws NullPointerException thrown if, and only if, either {@code primitiveObservers} or at least one of its elements are {@code null}
	 */
	public void setPrimitiveObservers(final List<PrimitiveObserver> primitiveObservers) {
		this.primitiveObservers.clear();
		this.primitiveObservers.addAll(ParameterArguments.requireNonNullList(primitiveObservers, "primitiveObservers"));
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
		Objects.requireNonNull(shape, "shape == null");
		
		if(doSetShape(shape)) {
			doSetBoundingVolume();
		}
	}
	
	/**
	 * Sets the {@link Transform} instance that is associated with this {@code Primitive} instance to {@code transform}.
	 * <p>
	 * If {@code transform} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param transform the {@code Transform} instance that is associated with this {@code Primitive} instance
	 * @throws NullPointerException thrown if, and only if, {@code transform} is {@code null}
	 */
	public void setTransform(final Transform transform) {
		Objects.requireNonNull(transform, "transform == null");
		
		final Transform oldTransform = this.transform;
		final Transform newTransform =      transform;
		
		this.transform.removeTransformObserver(this.transformObserver);
		this.transform = transform;
		this.transform.addTransformObserver(this.transformObserver);
		
		for(final PrimitiveObserver primitiveObserver : this.primitiveObservers) {
			primitiveObserver.onChangeTransform(this, oldTransform, newTransform);
		}
		
		doSetBoundingVolume();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private boolean doSetBoundingVolume() {
		return doSetBoundingVolume(this.shape.getBoundingVolume().transform(this.transform.getObjectToWorld()));
	}
	
	private boolean doSetBoundingVolume(final BoundingVolume3F boundingVolume) {
		Objects.requireNonNull(boundingVolume, "boundingVolume == null");
		
		if(!Objects.equals(this.boundingVolume, boundingVolume)) {
			final BoundingVolume3F oldBoundingVolume = this.boundingVolume;
			final BoundingVolume3F newBoundingVolume =      boundingVolume;
			
			this.boundingVolume = boundingVolume;
			
			for(final PrimitiveObserver primitiveObserver : this.primitiveObservers) {
				primitiveObserver.onChangeBoundingVolume(this, oldBoundingVolume, newBoundingVolume);
			}
			
			return true;
		}
		
		return false;
	}
	
	private boolean doSetShape(final Shape3F shape) {
		Objects.requireNonNull(shape, "shape == null");
		
		if(!Objects.equals(this.shape, shape)) {
			final Shape3F oldShape = this.shape;
			final Shape3F newShape =      shape;
			
			this.shape = shape;
			
			for(final PrimitiveObserver primitiveObserver : this.primitiveObservers) {
				primitiveObserver.onChangeShape(this, oldShape, newShape);
			}
			
			return true;
		}
		
		return false;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static float doTransformT(final Matrix44F matrix, final Ray3F rayOldSpace, final Ray3F rayNewSpace, final float t) {
		return !isNaN(t) && !isZero(t) && t < Float.MAX_VALUE ? abs(Point3F.distance(rayNewSpace.getOrigin(), Point3F.transformAndDivide(matrix, Point3F.add(rayOldSpace.getOrigin(), rayOldSpace.getDirection(), t)))) : t;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static final class TransformObserverImpl implements TransformObserver {
		private final Runnable updateBoundingVolumeRunnable;
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		public TransformObserverImpl(final Runnable updateBoundingVolumeRunnable) {
			this.updateBoundingVolumeRunnable = Objects.requireNonNull(updateBoundingVolumeRunnable, "updateBoundingVolumeRunnable == null");
		}
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		@Override
		public void onChangeObjectToWorld(final Transform transform, final Matrix44F newObjectToWorld) {
			Objects.requireNonNull(transform, "transform == null");
			Objects.requireNonNull(newObjectToWorld, "newObjectToWorld == null");
			
			this.updateBoundingVolumeRunnable.run();
		}
		
		@Override
		public void onChangePosition(final Transform transform, final Point3F oldPosition, final Point3F newPosition) {
			Objects.requireNonNull(transform, "transform == null");
			Objects.requireNonNull(oldPosition, "oldPosition == null");
			Objects.requireNonNull(newPosition, "newPosition == null");
		}
		
		@Override
		public void onChangeRotation(final Transform transform, final Quaternion4F oldRotation, final Quaternion4F newRotation) {
			Objects.requireNonNull(transform, "transform == null");
			Objects.requireNonNull(oldRotation, "oldRotation == null");
			Objects.requireNonNull(newRotation, "newRotation == null");
		}
		
		@Override
		public void onChangeScale(final Transform transform, final Vector3F oldScale, final Vector3F newScale) {
			Objects.requireNonNull(transform, "transform == null");
			Objects.requireNonNull(oldScale, "oldScale == null");
			Objects.requireNonNull(newScale, "newScale == null");
		}
		
		@Override
		public void onChangeWorldToObject(final Transform transform, final Matrix44F newWorldToObject) {
			Objects.requireNonNull(transform, "transform == null");
			Objects.requireNonNull(newWorldToObject, "newWorldToObject == null");
		}
	}
}