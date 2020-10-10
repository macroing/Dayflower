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
import static org.dayflower.util.Floats.isNaN;
import static org.dayflower.util.Floats.minOrNaN;
import static org.dayflower.util.Floats.random;
import static org.dayflower.util.Ints.min;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.dayflower.geometry.Point2F;
import org.dayflower.geometry.Point3F;
import org.dayflower.geometry.Ray3F;
import org.dayflower.geometry.SampleGeneratorF;
import org.dayflower.geometry.SurfaceIntersection3F;
import org.dayflower.geometry.Vector3F;
import org.dayflower.image.Color3F;
import org.dayflower.scene.pbrt.BSDF;
import org.dayflower.scene.pbrt.BXDFType;

/**
 * A {@code Scene} represents a scene and is associated with a {@link Camera} instance, a {@code List} of {@link Light} instances and a {@code List} of {@link Primitive} instances.
 * <p>
 * This class is mutable and therefore not thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class Scene {
	private final Background background;
	private final Camera camera;
	private final List<Light> lights;
	private final List<Primitive> primitives;
	private final String name;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code Scene} instance.
	 * <p>
	 * If either {@code background}, {@code camera} or {@code name} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param background the {@link Background} instance associated with this {@code Scene} instance
	 * @param camera the {@link Camera} instance associated with this {@code Scene} instance
	 * @param name the name associated with this {@code Scene} instance
	 * @throws NullPointerException thrown if, and only if, either {@code background}, {@code camera} or {@code name} are {@code null}
	 */
	public Scene(final Background background, final Camera camera, final String name) {
		this.background = Objects.requireNonNull(background, "background == null");
		this.camera = Objects.requireNonNull(camera, "camera == null");
		this.lights = new ArrayList<>();
		this.primitives = new ArrayList<>();
		this.name = Objects.requireNonNull(name, "name == null");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the {@link Background} instance associated with this {@code Scene} instance.
	 * 
	 * @return the {@code Background} instance associated with this {@code Scene} instance
	 */
	public Background getBackground() {
		return this.background;
	}
	
	/**
	 * Returns the {@link Camera} instance associated with this {@code Scene} instance.
	 * 
	 * @return the {@code Camera} instance associated with this {@code Scene} instance
	 */
	public Camera getCamera() {
		return this.camera;
	}
	
//	TODO: Add Javadocs!
	public Color3F lightEstimateDirect(final BSDF bSDF, final Intersection intersection, final Light light, final Point2F sampleA, final Point2F sampleB, final boolean isSpecular) {
		Objects.requireNonNull(bSDF, "bSDF == null");
		Objects.requireNonNull(intersection, "intersection == null");
		Objects.requireNonNull(light, "light == null");
		Objects.requireNonNull(sampleA, "sampleA == null");
		Objects.requireNonNull(sampleB, "sampleB == null");
		
		final BXDFType bXDFType = isSpecular ? BXDFType.ALL : BXDFType.ALL_EXCEPT_SPECULAR;
		
		final Optional<LightIncomingRadianceResult> optionalLightIncomingRadianceResult = light.sampleIncomingRadiance(intersection, sampleA);
		
		if(optionalLightIncomingRadianceResult.isPresent()) {
			final LightIncomingRadianceResult lightIncomingRadianceResult = optionalLightIncomingRadianceResult.get();
			
			final SurfaceIntersection3F surfaceIntersection = intersection.getSurfaceIntersectionWorldSpace();
			
			final Color3F lightResult = lightIncomingRadianceResult.getResult();
			
			final Vector3F incoming = lightIncomingRadianceResult.getIncoming();
			final Vector3F outgoing = Vector3F.negate(surfaceIntersection.getRay().getDirection());
			
			final float lightProbabilityDensityFunctionValue = lightIncomingRadianceResult.getProbabilityDensityFunctionValue();
			
			if(!lightResult.isBlack() && lightProbabilityDensityFunctionValue > 0.0F) {
				final Color3F scatteringResult = Color3F.multiply(bSDF.evaluateDistributionFunction(bXDFType, outgoing, incoming), abs(Vector3F.dotProduct(incoming, surfaceIntersection.getSurfaceNormalS())));
				
				final float scatteringProbabilityDensityFunctionValue = bSDF.evaluateProbabilityDensityFunction(bXDFType, outgoing, incoming);
				
				if(!scatteringResult.isBlack()) {
					final Point3F pointEye = surfaceIntersection.getSurfaceIntersectionPoint();
					final Point3F pointLookAt = lightIncomingRadianceResult.getPoint();
					
					final float tMinimum = 0.0001F;
					final float tMaximum = abs(Point3F.distance(pointEye, pointLookAt));
					
					final Ray3F ray = surfaceIntersection.createRay(pointLookAt);
					
					if(!intersects(ray, tMinimum, tMaximum)) {
						if(light.isDeltaDistribution()) {
							return Color3F.divide(Color3F.multiply(scatteringResult, lightResult), lightProbabilityDensityFunctionValue);
						}
						
						return Color3F.divide(Color3F.multiply(Color3F.multiply(scatteringResult, lightResult), SampleGeneratorF.multipleImportanceSamplingPowerHeuristic(lightProbabilityDensityFunctionValue, scatteringProbabilityDensityFunctionValue, 1, 1)), lightProbabilityDensityFunctionValue);
					}
				}
			}
		}
		
		return Color3F.BLACK;
	}
	
//	TODO: Add Javadocs!
	public Color3F lightSampleAllUniformDistribution(final BSDF bSDF, final Intersection intersection) {
		Objects.requireNonNull(bSDF, "bSDF == null");
		Objects.requireNonNull(intersection, "intersection == null");
		
		return new Color3F(0.25F);
	}
	
//	TODO: Add Javadocs!
	public Color3F lightSampleOneUniformDistribution(final BSDF bSDF, final Intersection intersection) {
		Objects.requireNonNull(bSDF, "bSDF == null");
		Objects.requireNonNull(intersection, "intersection == null");
		
		final int lightCount = this.lights.size();
		
		if(lightCount == 0) {
			return Color3F.BLACK;
		}
		
		final int lightIndex = min((int)(random() * lightCount), lightCount - 1);
		
		final float lightProbabilityDensityFunctionValue = 1.0F / lightCount;
		
		final Light light = this.lights.get(lightIndex);
		
		final Point2F sampleA = new Point2F(random(), random());
		final Point2F sampleB = new Point2F(random(), random());
		
		return Color3F.divide(lightEstimateDirect(bSDF, intersection, light, sampleA, sampleB, false), lightProbabilityDensityFunctionValue);
	}
	
	/**
	 * Returns a {@code List} with all {@link Light} instances currently associated with this {@code Scene} instance.
	 * <p>
	 * Modifying the returned {@code List} will not affect this {@code Scene} instance.
	 * 
	 * @return a {@code List} with all {@code Light} instances currently associated with this {@code Scene} instance
	 */
	public List<Light> getLights() {
		return new ArrayList<>(this.lights);
	}
	
	/**
	 * Returns a {@code List} with all {@link Primitive} instances currently associated with this {@code Scene} instance.
	 * <p>
	 * Modifying the returned {@code List} will not affect this {@code Scene} instance.
	 * 
	 * @return a {@code List} with all {@code Primitive} instances currently associated with this {@code Scene} instance
	 */
	public List<Primitive> getPrimitives() {
		return new ArrayList<>(this.primitives);
	}
	
	/**
	 * Performs an intersection test between {@code ray} and this {@code Scene} instance.
	 * <p>
	 * Returns an {@code Optional} with an optional {@link Intersection} instance that contains information about the intersection, if it was found.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3F} in world space to perform an intersection test against this {@code Scene} instance
	 * @return an {@code Optional} with an optional {@code Intersection} instance that contains information about the intersection, if it was found
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	public Optional<Intersection> intersection(final Ray3F ray) {
		return intersection(ray, 0.0001F, Float.MAX_VALUE);
	}
	
	/**
	 * Performs an intersection test between {@code ray} and this {@code Scene} instance.
	 * <p>
	 * Returns an {@code Optional} with an optional {@link Intersection} instance that contains information about the intersection, if it was found.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3F} in world space to perform an intersection test against this {@code Scene} instance
	 * @param tMinimum the minimum parametric distance
	 * @param tMaximum the maximum parametric distance
	 * @return an {@code Optional} with an optional {@code Intersection} instance that contains information about the intersection, if it was found
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	public Optional<Intersection> intersection(final Ray3F ray, final float tMinimum, final float tMaximum) {
		final MutableIntersection mutableIntersection = new MutableIntersection(ray, tMinimum, tMaximum);
		
		for(final Primitive primitive : this.primitives) {
			mutableIntersection.intersection(primitive);
		}
		
		return mutableIntersection.intersection();
		
		/*
		Primitive primitive = null;
		
		float t = Float.NaN;
		
		float tMax = tMaximum;
		float tMin = tMinimum;
		
		for(final Primitive currentPrimitive : this.primitives) {
			final float currentT = currentPrimitive.intersectionT(ray, tMin, tMax);
			
			if(!isNaN(currentT) && (isNaN(t) || currentT < t)) {
				primitive = currentPrimitive;
				
				t = currentT;
				
				tMax = t;
			}
		}
		
		if(primitive != null) {
			return primitive.intersection(ray, tMinimum, tMaximum);
		}
		
		return Optional.empty();
		*/
	}
	
	/**
	 * Returns the name associated with this {@code Scene} instance.
	 * 
	 * @return the name associated with this {@code Scene} instance
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code Scene} instance.
	 * 
	 * @return a {@code String} representation of this {@code Scene} instance
	 */
	@Override
	public String toString() {
		return String.format("new Scene(%s, %s, %s)", this.background, this.camera, this.name);
	}
	
	/**
	 * Adds {@code light} to this {@code Scene} instance.
	 * <p>
	 * Returns {@code true} if, and only if, {@code light} was added, {@code false} otherwise.
	 * <p>
	 * If {@code light} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param light the {@link Light} instance to add
	 * @return {@code true} if, and only if, {@code light} was added, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, {@code light} is {@code null}
	 */
	public boolean addLight(final Light light) {
		return this.lights.add(Objects.requireNonNull(light, "light == null"));
	}
	
	/**
	 * Adds {@code primitive} to this {@code Scene} instance.
	 * <p>
	 * Returns {@code true} if, and only if, {@code primitive} was added, {@code false} otherwise.
	 * <p>
	 * If {@code primitive} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param primitive the {@link Primitive} instance to add
	 * @return {@code true} if, and only if, {@code primitive} was added, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, {@code primitive} is {@code null}
	 */
	public boolean addPrimitive(final Primitive primitive) {
		return this.primitives.add(Objects.requireNonNull(primitive, "primitive == null"));
	}
	
	/**
	 * Compares {@code object} to this {@code Scene} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code Scene}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code Scene} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code Scene}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof Scene)) {
			return false;
		} else if(!Objects.equals(this.background, Scene.class.cast(object).background)) {
			return false;
		} else if(!Objects.equals(this.camera, Scene.class.cast(object).camera)) {
			return false;
		} else if(!Objects.equals(this.lights, Scene.class.cast(object).lights)) {
			return false;
		} else if(!Objects.equals(this.primitives, Scene.class.cast(object).primitives)) {
			return false;
		} else if(!Objects.equals(this.name, Scene.class.cast(object).name)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns {@code true} if, and only if, {@code ray} intersects any {@link Primitive} instance in this {@code Scene} instance, {@code false} otherwise.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3F} in world space to perform an intersection test against this {@code Scene} instance
	 * @return {@code true} if, and only if, {@code ray} intersects any {@code Primitive} instance in this {@code Scene} instance, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	public boolean intersects(final Ray3F ray) {
		return intersects(ray, 0.0001F, Float.MAX_VALUE);
	}
	
	/**
	 * Returns {@code true} if, and only if, {@code ray} intersects any {@link Primitive} instance in this {@code Scene} instance, {@code false} otherwise.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3F} in world space to perform an intersection test against this {@code Scene} instance
	 * @param tMinimum the minimum parametric distance
	 * @param tMaximum the maximum parametric distance
	 * @return {@code true} if, and only if, {@code ray} intersects any {@code Primitive} instance in this {@code Scene} instance, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	public boolean intersects(final Ray3F ray, final float tMinimum, final float tMaximum) {
		for(final Primitive primitive : this.primitives) {
			if(primitive.intersects(ray, tMinimum, tMaximum)) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Removes {@code light} from this {@code Scene} instance, if present.
	 * <p>
	 * Returns {@code true} if, and only if, {@code light} was removed, {@code false} otherwise.
	 * <p>
	 * If {@code light} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param light the {@link Light} instance to remove
	 * @return {@code true} if, and only if, {@code light} was removed, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, {@code light} is {@code null}
	 */
	public boolean removeLight(final Light light) {
		return this.lights.remove(Objects.requireNonNull(light, "light == null"));
	}
	
	/**
	 * Removes {@code primitive} from this {@code Scene} instance, if present.
	 * <p>
	 * Returns {@code true} if, and only if, {@code primitive} was removed, {@code false} otherwise.
	 * <p>
	 * If {@code primitive} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param primitive the {@link Primitive} instance to remove
	 * @return {@code true} if, and only if, {@code primitive} was removed, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, {@code primitive} is {@code null}
	 */
	public boolean removePrimitive(final Primitive primitive) {
		return this.primitives.remove(Objects.requireNonNull(primitive, "primitive == null"));
	}
	
	/**
	 * Performs an intersection test between {@code ray} and this {@code Scene} instance.
	 * <p>
	 * Returns {@code t}, the parametric distance to the surface intersection point, or {@code Float.NaN} if no intersection exists.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3F} to perform an intersection test against this {@code Scene} instance
	 * @return {@code t}, the parametric distance to the surface intersection point, or {@code Float.NaN} if no intersection exists
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	public float intersectionT(final Ray3F ray) {
		return intersectionT(ray, 0.0001F, Float.MAX_VALUE);
	}
	
	/**
	 * Performs an intersection test between {@code ray} and this {@code Scene} instance.
	 * <p>
	 * Returns {@code t}, the parametric distance to the surface intersection point, or {@code Float.NaN} if no intersection exists.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3F} to perform an intersection test against this {@code Scene} instance
	 * @param tMinimum the minimum parametric distance
	 * @param tMaximum the maximum parametric distance
	 * @return {@code t}, the parametric distance to the surface intersection point, or {@code Float.NaN} if no intersection exists
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	public float intersectionT(final Ray3F ray, final float tMinimum, final float tMaximum) {
		float t = Float.NaN;
		float tMax = tMaximum;
		float tMin = tMinimum;
		
		for(final Primitive primitive : this.primitives) {
			t = minOrNaN(t, primitive.intersectionT(ray, tMin, tMax));
			
			if(!isNaN(t)) {
				tMax = t;
			}
		}
		
		return t;
	}
	
	/**
	 * Returns a hash code for this {@code Scene} instance.
	 * 
	 * @return a hash code for this {@code Scene} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.background, this.camera, this.lights, this.primitives, this.name);
	}
}