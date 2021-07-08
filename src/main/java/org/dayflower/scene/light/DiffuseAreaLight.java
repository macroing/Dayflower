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
package org.dayflower.scene.light;

import static org.dayflower.utility.Floats.PI;

import java.util.Objects;
import java.util.Optional;

import org.dayflower.color.Color3F;
import org.dayflower.geometry.Matrix44F;
import org.dayflower.geometry.Point2F;
import org.dayflower.geometry.Point3F;
import org.dayflower.geometry.Shape3F;
import org.dayflower.geometry.SurfaceIntersection3F;
import org.dayflower.geometry.SurfaceSample3F;
import org.dayflower.geometry.Vector3F;
import org.dayflower.geometry.shape.Sphere3F;
import org.dayflower.node.NodeHierarchicalVisitor;
import org.dayflower.node.NodeTraversalException;
import org.dayflower.scene.AreaLight;
import org.dayflower.scene.Intersection;
import org.dayflower.scene.LightSample;
import org.dayflower.scene.Transform;

/**
 * A {@code DiffuseAreaLight} is an implementation of {@link AreaLight} that represents a diffuse area light.
 * <p>
 * This class is mutable and not thread-safe.
 * <p>
 * This {@code Light} implementation is not supported on the GPU.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class DiffuseAreaLight extends AreaLight {
	/**
	 * The name of this {@code DiffuseAreaLight} class.
	 */
	public static final String NAME = "Diffuse Area Light";
	
	/**
	 * The ID of this {@code DiffuseAreaLight} class.
	 */
	public static final int ID = 1;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final Color3F radianceEmitted;
	private final Shape3F shape;
	private final boolean isTwoSided;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code DiffuseAreaLight} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new DiffuseAreaLight(new Transform());
	 * }
	 * </pre>
	 */
	public DiffuseAreaLight() {
		this(new Transform());
	}
	
	/**
	 * Constructs a new {@code DiffuseAreaLight} instance.
	 * <p>
	 * If {@code transform} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new DiffuseAreaLight(transform, 1);
	 * }
	 * </pre>
	 * 
	 * @param transform the {@link Transform} instance associated with this {@code AreaLight} instance
	 * @throws NullPointerException thrown if, and only if, {@code transform} is {@code null}
	 */
	public DiffuseAreaLight(final Transform transform) {
		this(transform, 1);
	}
	
	/**
	 * Constructs a new {@code DiffuseAreaLight} instance.
	 * <p>
	 * If {@code transform} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code sampleCount} is less than {@code 1}, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new DiffuseAreaLight(transform, sampleCount, Color3F.WHITE);
	 * }
	 * </pre>
	 * 
	 * @param transform the {@link Transform} instance associated with this {@code AreaLight} instance
	 * @param sampleCount the sample count associated with this {@code AreaLight} instance
	 * @throws IllegalArgumentException thrown if, and only if, {@code sampleCount} is less than {@code 1}
	 * @throws NullPointerException thrown if, and only if, {@code transform} is {@code null}
	 */
	public DiffuseAreaLight(final Transform transform, final int sampleCount) {
		this(transform, sampleCount, Color3F.WHITE);
	}
	
	/**
	 * Constructs a new {@code DiffuseAreaLight} instance.
	 * <p>
	 * If either {@code transform} or {@code radianceEmitted} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code sampleCount} is less than {@code 1}, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new DiffuseAreaLight(transform, sampleCount, radianceEmitted, new Sphere3F());
	 * }
	 * </pre>
	 * 
	 * @param transform the {@link Transform} instance associated with this {@code AreaLight} instance
	 * @param sampleCount the sample count associated with this {@code AreaLight} instance
	 * @param radianceEmitted a {@link Color3F} instance with the radiance emitted
	 * @throws IllegalArgumentException thrown if, and only if, {@code sampleCount} is less than {@code 1}
	 * @throws NullPointerException thrown if, and only if, either {@code transform} or {@code radianceEmitted} are {@code null}
	 */
	public DiffuseAreaLight(final Transform transform, final int sampleCount, final Color3F radianceEmitted) {
		this(transform, sampleCount, radianceEmitted, new Sphere3F());
	}
	
	/**
	 * Constructs a new {@code DiffuseAreaLight} instance.
	 * <p>
	 * If either {@code transform}, {@code radianceEmitted} or {@code shape} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code sampleCount} is less than {@code 1}, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new DiffuseAreaLight(transform, sampleCount, radianceEmitted, shape, false);
	 * }
	 * </pre>
	 * 
	 * @param transform the {@link Transform} instance associated with this {@code AreaLight} instance
	 * @param sampleCount the sample count associated with this {@code AreaLight} instance
	 * @param radianceEmitted a {@link Color3F} instance with the radiance emitted
	 * @param shape a {@link Shape3F} instance
	 * @throws IllegalArgumentException thrown if, and only if, {@code sampleCount} is less than {@code 1}
	 * @throws NullPointerException thrown if, and only if, either {@code transform}, {@code radianceEmitted} or {@code shape} are {@code null}
	 */
	public DiffuseAreaLight(final Transform transform, final int sampleCount, final Color3F radianceEmitted, final Shape3F shape) {
		this(transform, sampleCount, radianceEmitted, shape, false);
	}
	
	/**
	 * Constructs a new {@code DiffuseAreaLight} instance.
	 * <p>
	 * If either {@code transform}, {@code radianceEmitted} or {@code shape} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code sampleCount} is less than {@code 1}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param transform the {@link Transform} instance associated with this {@code AreaLight} instance
	 * @param sampleCount the sample count associated with this {@code AreaLight} instance
	 * @param radianceEmitted a {@link Color3F} instance with the radiance emitted
	 * @param shape a {@link Shape3F} instance
	 * @param isTwoSided {@code true} if, and only if, this {@code DiffuseAreaLight} is two-sided, {@code false} otherwise
	 * @throws IllegalArgumentException thrown if, and only if, {@code sampleCount} is less than {@code 1}
	 * @throws NullPointerException thrown if, and only if, either {@code transform}, {@code radianceEmitted} or {@code shape} are {@code null}
	 */
	public DiffuseAreaLight(final Transform transform, final int sampleCount, final Color3F radianceEmitted, final Shape3F shape, final boolean isTwoSided) {
		super(transform, sampleCount);
		
		this.radianceEmitted = Objects.requireNonNull(radianceEmitted, "radianceEmitted == null");
		this.shape = Objects.requireNonNull(shape, "shape == null");
		this.isTwoSided = isTwoSided;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link Color3F} instance with the radiance on {@code intersection} emitted in the direction of {@code direction}.
	 * <p>
	 * If either {@code intersection} or {@code direction} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param intersection an {@link Intersection} instance from which the radiance is emitted
	 * @param direction a {@link Vector3F} instance with a direction in which the radiance is emitted
	 * @return a {@code Color3F} instance with the radiance on {@code intersection} emitted in the direction of {@code direction}
	 * @throws NullPointerException thrown if, and only if, either {@code intersection} or {@code direction} are {@code null}
	 */
	@Override
	public Color3F evaluateRadianceEmitted(final Intersection intersection, final Vector3F direction) {
		Objects.requireNonNull(intersection, "intersection == null");
		Objects.requireNonNull(direction, "direction == null");
		
		return this.isTwoSided || Vector3F.dotProduct(intersection.getSurfaceNormalS(), direction) > 0.0F ? this.radianceEmitted : Color3F.BLACK;
	}
	
	/**
	 * Returns a {@link Color3F} instance with the radiance emitted.
	 * 
	 * @return a {@code Color3F} instance with the radiance emitted
	 */
	public Color3F getRadianceEmitted() {
		return this.radianceEmitted;
	}
	
	/**
	 * Returns a {@link Color3F} instance with the power of this {@code DiffuseAreaLight} instance.
	 * 
	 * @return a {@code Color3F} instance with the power of this {@code DiffuseAreaLight} instance
	 */
	@Override
	public Color3F power() {
		return Color3F.multiply(this.radianceEmitted, (this.isTwoSided ? 2.0F : 1.0F) * this.shape.getSurfaceArea() * PI);
	}
	
	/**
	 * Samples the incoming radiance.
	 * <p>
	 * Returns an optional {@link LightSample} with the result of the sampling.
	 * <p>
	 * If either {@code intersection} or {@code sample} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param intersection an {@link Intersection} instance
	 * @param sample a {@link Point2F} instance
	 * @return an optional {@code LightSample} with the result of the sampling
	 * @throws NullPointerException thrown if, and only if, either {@code intersection} or {@code sample} are {@code null}
	 */
	@Override
	public Optional<LightSample> sampleRadianceIncoming(final Intersection intersection, final Point2F sample) {
		Objects.requireNonNull(intersection, "intersection == null");
		Objects.requireNonNull(sample, "sample == null");
		
		final Transform transform = getTransform();
		
		final Matrix44F objectToWorld = transform.getObjectToWorld();
		final Matrix44F worldToObject = transform.getWorldToObject();
		
		final SurfaceIntersection3F surfaceIntersectionWorldSpace = intersection.getSurfaceIntersectionWorldSpace();
		final SurfaceIntersection3F surfaceIntersectionObjectSpace = SurfaceIntersection3F.transform(surfaceIntersectionWorldSpace, worldToObject, objectToWorld);
		
		final Optional<SurfaceSample3F> optionalSurfaceSampleObjectSpace = this.shape.sample(sample, surfaceIntersectionObjectSpace);
		
		if(optionalSurfaceSampleObjectSpace.isPresent()) {
			final SurfaceSample3F surfaceSampleObjectSpace = optionalSurfaceSampleObjectSpace.get();
			final SurfaceSample3F surfaceSampleWorldSpace = SurfaceSample3F.transform(surfaceSampleObjectSpace, objectToWorld, worldToObject);
			
			final float probabilityDensityFunctionValue = surfaceSampleWorldSpace.getProbabilityDensityFunctionValue();
			
			final Point3F pointWorldSpace = surfaceSampleWorldSpace.getPoint();
			
			final Vector3F incomingWorldSpace = Vector3F.directionNormalized(surfaceIntersectionWorldSpace.getSurfaceIntersectionPoint(), pointWorldSpace);
			
			if(probabilityDensityFunctionValue > 0.0F && (this.isTwoSided || Vector3F.dotProduct(surfaceSampleWorldSpace.getSurfaceNormal(), Vector3F.negate(incomingWorldSpace)) > 0.0F)) {
				return Optional.of(new LightSample(this.radianceEmitted, pointWorldSpace, incomingWorldSpace, probabilityDensityFunctionValue));
			}
		}
		
		return Optional.empty();
	}
	
	/**
	 * Returns the {@link Shape3F} instance associated with this {@code DiffuseAreaLight} instance.
	 * 
	 * @return the {@code Shape3F} instance associated with this {@code DiffuseAreaLight} instance
	 */
	public Shape3F getShape() {
		return this.shape;
	}
	
	/**
	 * Returns a {@code String} with the name of this {@code DiffuseAreaLight} instance.
	 * 
	 * @return a {@code String} with the name of this {@code DiffuseAreaLight} instance
	 */
	@Override
	public String getName() {
		return NAME;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code DiffuseAreaLight} instance.
	 * 
	 * @return a {@code String} representation of this {@code DiffuseAreaLight} instance
	 */
	@Override
	public String toString() {
		return String.format("new DiffuseAreaLight(%s, %d, %s, %s, %s)", getTransform(), Integer.valueOf(getSampleCount()), this.radianceEmitted, this.shape, Boolean.toString(this.isTwoSided));
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
				if(!getTransform().accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
				
				if(!this.shape.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
			}
			
			return nodeHierarchicalVisitor.visitLeave(this);
		} catch(final RuntimeException e) {
			throw new NodeTraversalException(e);
		}
	}
	
	/**
	 * Compares {@code object} to this {@code DiffuseAreaLight} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code DiffuseAreaLight}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code DiffuseAreaLight} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code DiffuseAreaLight}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof DiffuseAreaLight)) {
			return false;
		} else if(!Objects.equals(getTransform(), DiffuseAreaLight.class.cast(object).getTransform())) {
			return false;
		} else if(getSampleCount() != DiffuseAreaLight.class.cast(object).getSampleCount()) {
			return false;
		} else if(!Objects.equals(this.radianceEmitted, DiffuseAreaLight.class.cast(object).radianceEmitted)) {
			return false;
		} else if(!Objects.equals(this.shape, DiffuseAreaLight.class.cast(object).shape)) {
			return false;
		} else if(this.isTwoSided != DiffuseAreaLight.class.cast(object).isTwoSided) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code DiffuseAreaLight} instance is two-sided, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, this {@code DiffuseAreaLight} instance is two-sided, {@code false} otherwise
	 */
	public boolean isTwoSided() {
		return this.isTwoSided;
	}
	
	/**
	 * Evaluates the probability density function (PDF) for the incoming radiance.
	 * <p>
	 * Returns a {@code float} with the probability density function (PDF) value.
	 * <p>
	 * If either {@code intersection} or {@code incoming} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param intersection an {@link Intersection} instance
	 * @param incoming the incoming direction
	 * @return a {@code float} with the probability density function (PDF) value
	 * @throws NullPointerException thrown if, and only if, either {@code intersection} or {@code incoming} are {@code null}
	 */
	@Override
	public float evaluateProbabilityDensityFunctionRadianceIncoming(final Intersection intersection, final Vector3F incoming) {
		Objects.requireNonNull(intersection, "intersection == null");
		Objects.requireNonNull(incoming, "incoming == null");
		
		final Transform transform = getTransform();
		
		final Matrix44F objectToWorld = transform.getObjectToWorld();
		final Matrix44F worldToObject = transform.getWorldToObject();
		
		final Vector3F incomingObjectSpace = Vector3F.transform(worldToObject, incoming);
		
		final SurfaceIntersection3F surfaceIntersectionWorldSpace = intersection.getSurfaceIntersectionWorldSpace();
		final SurfaceIntersection3F surfaceIntersectionObjectSpace = SurfaceIntersection3F.transform(surfaceIntersectionWorldSpace, worldToObject, objectToWorld);
		
		return this.shape.evaluateProbabilityDensityFunction(surfaceIntersectionObjectSpace, incomingObjectSpace);
	}
	
	/**
	 * Returns an {@code int} with the ID of this {@code DiffuseAreaLight} instance.
	 * 
	 * @return an {@code int} with the ID of this {@code DiffuseAreaLight} instance
	 */
	@Override
	public int getID() {
		return ID;
	}
	
	/**
	 * Returns a hash code for this {@code DiffuseAreaLight} instance.
	 * 
	 * @return a hash code for this {@code DiffuseAreaLight} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(getTransform(), Integer.valueOf(getSampleCount()), this.radianceEmitted, this.shape, Boolean.valueOf(this.isTwoSided));
	}
}