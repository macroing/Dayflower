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

import static org.dayflower.util.Floats.isNaN;
import static org.dayflower.util.Floats.minOrNaN;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.dayflower.geometry.Ray3F;
import org.dayflower.util.Lists;

/**
 * A {@code Scene} represents a scene and is associated with a {@link Camera} instance, a {@code List} of {@link Light} instances and a {@code List} of {@link Primitive} instances.
 * <p>
 * This class is mutable and therefore not thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class Scene {
	private Camera camera;
	private List<Light> lights;
	private List<Primitive> primitives;
	private String name;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code Scene} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Scene(new Camera());
	 * }
	 * </pre>
	 */
	public Scene() {
		this(new Camera());
	}
	
	/**
	 * Constructs a new {@code Scene} instance.
	 * <p>
	 * If {@code camera} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Scene(camera, "Default");
	 * }
	 * </pre>
	 * 
	 * @param camera the {@link Camera} instance associated with this {@code Scene} instance
	 * @throws NullPointerException thrown if, and only if, {@code camera} is {@code null}
	 */
	public Scene(final Camera camera) {
		this(camera, "Default");
	}
	
	/**
	 * Constructs a new {@code Scene} instance.
	 * <p>
	 * If either {@code camera} or {@code name} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param camera the {@link Camera} instance associated with this {@code Scene} instance
	 * @param name the name associated with this {@code Scene} instance
	 * @throws NullPointerException thrown if, and only if, either {@code camera} or {@code name} are {@code null}
	 */
	public Scene(final Camera camera, final String name) {
		this.camera = Objects.requireNonNull(camera, "camera == null");
		this.lights = new ArrayList<>();
		this.primitives = new ArrayList<>();
		this.name = Objects.requireNonNull(name, "name == null");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the {@link Camera} instance associated with this {@code Scene} instance.
	 * 
	 * @return the {@code Camera} instance associated with this {@code Scene} instance
	 */
	public Camera getCamera() {
		return this.camera;
	}
	
	/**
	 * Returns a copy of the {@link Camera} instance associated with this {@code Scene} instance.
	 * 
	 * @return a copy of the {@code Camera} instance associated with this {@code Scene} instance
	 */
	public Camera getCameraCopy() {
		synchronized(this.camera) {
			return this.camera.copy();
		}
	}
	
	/**
	 * Returns the {@link Light} instance at {@code index}.
	 * <p>
	 * If {@code index} is less than {@code 0} or greater than or equal to {@code scene.getLightCount()}, an {@code IndexOutOfBoundsException} will be thrown.
	 * 
	 * @param index the index
	 * @return the {@code Light} instance at {@code index}
	 * @throws NullPointerException thrown if, and only if, {@code index} is less than {@code 0} or greater than or equal to {@code scene.getLightCount()}
	 */
	public Light getLight(final int index) {
		return this.lights.get(index);
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
		return intersection(ray, 0.0F, Float.MAX_VALUE);
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
		
		return mutableIntersection.computeIntersection();
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
		return String.format("new Scene(%s, %s)", this.camera, this.name);
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
		return intersects(ray, 0.0F, Float.MAX_VALUE);
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
		return intersectionT(ray, 0.0F, Float.MAX_VALUE);
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
	 * Returns the {@link Light} count in this {@code Scene} instance.
	 * 
	 * @return the {@code Light} count in this {@code Scene} instance
	 */
	public int getLightCount() {
		return this.lights.size();
	}
	
	/**
	 * Returns a hash code for this {@code Scene} instance.
	 * 
	 * @return a hash code for this {@code Scene} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.camera, this.lights, this.primitives, this.name);
	}
	
	/**
	 * Sets the {@link Camera} instance associated with this {@code Scene} instance to {@code camera}.
	 * <p>
	 * If {@code camera} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param camera the {@code Camera} instance associated with this {@code Scene} instance
	 * @throws NullPointerException thrown if, and only if, {@code camera} is {@code null}
	 */
	public void setCamera(final Camera camera) {
		this.camera = Objects.requireNonNull(camera, "camera == null");
	}
	
	/**
	 * Sets the {@code List} with all {@link Light} instances associated with this {@code Scene} instance to a copy of {@code lights}.
	 * <p>
	 * If either {@code lights} or at least one of its elements are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param lights a {@code List} with all {@code Light} instances associated with this {@code Scene} instance
	 * @throws NullPointerException thrown if, and only if, either {@code lights} or at least one of its elements are {@code null}
	 */
	public void setLights(final List<Light> lights) {
		this.lights = new ArrayList<>(Lists.requireNonNullList(lights, "lights"));
	}
	
	/**
	 * Sets the name associated with this {@code Scene} instance to {@code name}.
	 * <p>
	 * If {@code name} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param name the name associated with this {@code Scene} instance
	 * @throws NullPointerException thrown if, and only if, {@code name} is {@code null}
	 */
	public void setName(final String name) {
		this.name = Objects.requireNonNull(name, "name == null");
	}
	
	/**
	 * Sets the {@code List} with all {@link Primitive} instances associated with this {@code Scene} instance to a copy of {@code primitives}.
	 * <p>
	 * If either {@code primitives} or at least one of its elements are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param lights a {@code List} with all {@code Primitive} instances associated with this {@code Scene} instance
	 * @throws NullPointerException thrown if, and only if, either {@code primitives} or at least one of its elements are {@code null}
	 */
	public void setPrimitives(final List<Primitive> primitives) {
		this.primitives = new ArrayList<>(Lists.requireNonNullList(primitives, "primitives"));
	}
}