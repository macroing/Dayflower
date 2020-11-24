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
import static org.dayflower.util.Floats.max;
import static org.dayflower.util.Floats.min;
import static org.dayflower.util.Floats.minOrNaN;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.dayflower.geometry.BoundingVolume3F;
import org.dayflower.geometry.Point3F;
import org.dayflower.geometry.Ray3F;
import org.dayflower.geometry.boundingvolume.AxisAlignedBoundingBox3F;
import org.dayflower.geometry.boundingvolume.InfiniteBoundingVolume3F;
import org.dayflower.node.Node;
import org.dayflower.util.ParameterArguments;

/**
 * A {@code Scene} represents a scene and is associated with a {@link Camera} instance, a {@code List} of {@link Light} instances and a {@code List} of {@link Primitive} instances.
 * <p>
 * This class is mutable and therefore not thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class Scene implements Node {
	private BVHNode bVHNode;
	private Camera camera;
	private List<Light> lights;
	private List<Primitive> primitives;
	private List<Primitive> primitivesExternalToBVH;
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
		this.bVHNode = null;
		this.camera = Objects.requireNonNull(camera, "camera == null");
		this.lights = new ArrayList<>();
		this.primitives = new ArrayList<>();
		this.primitivesExternalToBVH = new ArrayList<>();
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
	 * @param tMinimum the minimum parametric distance
	 * @param tMaximum the maximum parametric distance
	 * @return an {@code Optional} with an optional {@code Intersection} instance that contains information about the intersection, if it was found
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	public Optional<Intersection> intersection(final Ray3F ray, final float tMinimum, final float tMaximum) {
		final MutableIntersection mutableIntersection = new MutableIntersection(ray, tMinimum, tMaximum);
		
		final BVHNode bVHNode = this.bVHNode;
		
		if(bVHNode != null) {
			for(final Primitive primitive : this.primitivesExternalToBVH) {
				mutableIntersection.intersection(primitive);
			}
			
			bVHNode.intersection(mutableIntersection);
			
			return mutableIntersection.computeIntersection();
		}
		
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
	 * @param tMinimum the minimum parametric distance
	 * @param tMaximum the maximum parametric distance
	 * @return {@code true} if, and only if, {@code ray} intersects any {@code Primitive} instance in this {@code Scene} instance, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	public boolean intersects(final Ray3F ray, final float tMinimum, final float tMaximum) {
		final BVHNode bVHNode = this.bVHNode;
		
		if(bVHNode != null) {
			for(final Primitive primitive : this.primitivesExternalToBVH) {
				if(primitive.intersects(ray, tMinimum, tMaximum)) {
					return true;
				}
			}
			
			return bVHNode.intersects(ray, tMinimum, tMaximum);
		}
		
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
	 * @param tMinimum the minimum parametric distance
	 * @param tMaximum the maximum parametric distance
	 * @return {@code t}, the parametric distance to the surface intersection point, or {@code Float.NaN} if no intersection exists
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	public float intersectionT(final Ray3F ray, final float tMinimum, final float tMaximum) {
		float t = Float.NaN;
		float tMax = tMaximum;
		float tMin = tMinimum;
		
		final BVHNode bVHNode = this.bVHNode;
		
		if(bVHNode != null) {
			for(final Primitive primitive : this.primitivesExternalToBVH) {
				t = minOrNaN(t, primitive.intersectionT(ray, tMin, tMax));
				
				if(!isNaN(t)) {
					tMax = t;
				}
			}
			
			t = minOrNaN(t, bVHNode.intersectionT(ray, new float[] {tMin, tMax}));
			
			return t;
		}
		
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
	 * Builds an acceleration structure for this {@code Scene} instance.
	 */
	public void buildAccelerationStructure() {
		final List<Primitive> primitives = this.primitives;
		final List<Primitive> primitivesExternalToBVH = new ArrayList<>();
		
		this.bVHNode = doCreateBVHNode(primitives, primitivesExternalToBVH);
		this.primitivesExternalToBVH = primitivesExternalToBVH;
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
		this.lights = new ArrayList<>(ParameterArguments.requireNonNullList(lights, "lights"));
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
		this.primitives = new ArrayList<>(ParameterArguments.requireNonNullList(primitives, "primitives"));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static BVHNode doCreateBVHNode(final List<LeafBVHNode> processableLeafBVHNodes, final Point3F maximum, final Point3F minimum, final int depth) {
		final int size = processableLeafBVHNodes.size();
		final int sizeHalf = size / 2;
		
		if(size < 4) {
			final List<Primitive> primitives = new ArrayList<>();
			
			for(final LeafBVHNode processableLeafBVHNode : processableLeafBVHNodes) {
				for(final Primitive primitive : processableLeafBVHNode.getPrimitives()) {
					primitives.add(primitive);
				}
			}
			
			return new LeafBVHNode(maximum, minimum, depth, primitives);
		}
		
		final float sideX = maximum.getX() - minimum.getX();
		final float sideY = maximum.getY() - minimum.getY();
		final float sideZ = maximum.getZ() - minimum.getZ();
		
		float minimumCost = size * (sideX * sideY + sideY * sideZ + sideZ * sideX);
		float bestSplit = Float.MAX_VALUE;
		
		int bestAxis = -1;
		
		for(int axis = 0; axis < 3; axis++) {
			final float start = minimum.getComponent(axis);
			final float stop  = maximum.getComponent(axis);
			
			if(abs(stop - start) < 1.0e-4F) {
				continue;
			}
			
			final float step = (stop - start) / (1024.0F / (depth + 1.0F));
			
			for(float oldSplit = 0.0F, newSplit = start + step; newSplit < stop - step; oldSplit = newSplit, newSplit += step) {
//				The following test prevents an infinite loop from occurring:
				if(equal(oldSplit, newSplit)) {
					break;
				}
				
				float maximumLX = Float.MIN_VALUE;
				float maximumLY = Float.MIN_VALUE;
				float maximumLZ = Float.MIN_VALUE;
				float minimumLX = Float.MAX_VALUE;
				float minimumLY = Float.MAX_VALUE;
				float minimumLZ = Float.MAX_VALUE;
				float maximumRX = Float.MIN_VALUE;
				float maximumRY = Float.MIN_VALUE;
				float maximumRZ = Float.MIN_VALUE;
				float minimumRX = Float.MAX_VALUE;
				float minimumRY = Float.MAX_VALUE;
				float minimumRZ = Float.MAX_VALUE;
				
				int countL = 0;
				int countR = 0;
				
				for(final LeafBVHNode processableLeafBVHNode : processableLeafBVHNodes) {
					final BoundingVolume3F boundingVolume = processableLeafBVHNode.getBoundingVolume();
					
					final Point3F max = boundingVolume.getMaximum();
					final Point3F mid = boundingVolume.getMidpoint();
					final Point3F min = boundingVolume.getMinimum();
					
					final float value = mid.getComponent(axis);
					
					if(value < newSplit) {
						maximumLX = max(maximumLX, max.getX());
						maximumLY = max(maximumLY, max.getY());
						maximumLZ = max(maximumLZ, max.getZ());
						minimumLX = min(minimumLX, min.getX());
						minimumLY = min(minimumLY, min.getY());
						minimumLZ = min(minimumLZ, min.getZ());
						
						countL++;
					} else {
						maximumRX = max(maximumRX, max.getX());
						maximumRY = max(maximumRY, max.getY());
						maximumRZ = max(maximumRZ, max.getZ());
						minimumRX = min(minimumRX, min.getX());
						minimumRY = min(minimumRY, min.getY());
						minimumRZ = min(minimumRZ, min.getZ());
						
						countR++;
					}
				}
				
				if(countL <= 1 || countR <= 1) {
					continue;
				}
				
				final float sideLX = maximumLX - minimumLX;
				final float sideLY = maximumLY - minimumLY;
				final float sideLZ = maximumLZ - minimumLZ;
				final float sideRX = maximumRX - minimumRX;
				final float sideRY = maximumRY - minimumRY;
				final float sideRZ = maximumRZ - minimumRZ;
				
				final float surfaceL = sideLX * sideLY + sideLY * sideLZ + sideLZ * sideLX;
				final float surfaceR = sideRX * sideRY + sideRY * sideRZ + sideRZ * sideRX;
				
				final float cost = surfaceL * countL + surfaceR * countR;
				
				if(cost < minimumCost) {
					minimumCost = cost;
					bestSplit = newSplit;
					bestAxis = axis;
				}
			}
		}
		
		if(bestAxis == -1) {
			final List<Primitive> primitives = new ArrayList<>();
			
			for(final LeafBVHNode processableLeafBVHNode : processableLeafBVHNodes) {
				for(final Primitive primitive : processableLeafBVHNode.getPrimitives()) {
					primitives.add(primitive);
				}
			}
			
			return new LeafBVHNode(maximum, minimum, depth, primitives);
		}
		
		final List<LeafBVHNode> leafBVHNodesL = new ArrayList<>(sizeHalf);
		final List<LeafBVHNode> leafBVHNodesR = new ArrayList<>(sizeHalf);
		
		float maximumLX = Float.MIN_VALUE;
		float maximumLY = Float.MIN_VALUE;
		float maximumLZ = Float.MIN_VALUE;
		float minimumLX = Float.MAX_VALUE;
		float minimumLY = Float.MAX_VALUE;
		float minimumLZ = Float.MAX_VALUE;
		float maximumRX = Float.MIN_VALUE;
		float maximumRY = Float.MIN_VALUE;
		float maximumRZ = Float.MIN_VALUE;
		float minimumRX = Float.MAX_VALUE;
		float minimumRY = Float.MAX_VALUE;
		float minimumRZ = Float.MAX_VALUE;
		
		for(final LeafBVHNode processableLeafBVHNode : processableLeafBVHNodes) {
			final BoundingVolume3F boundingVolume = processableLeafBVHNode.getBoundingVolume();
			
			final Point3F max = boundingVolume.getMaximum();
			final Point3F mid = boundingVolume.getMidpoint();
			final Point3F min = boundingVolume.getMinimum();
			
			final float value = mid.getComponent(bestAxis);
			
			if(value < bestSplit) {
				leafBVHNodesL.add(processableLeafBVHNode);
				
				maximumLX = max(maximumLX, max.getX());
				maximumLY = max(maximumLY, max.getY());
				maximumLZ = max(maximumLZ, max.getZ());
				minimumLX = min(minimumLX, min.getX());
				minimumLY = min(minimumLY, min.getY());
				minimumLZ = min(minimumLZ, min.getZ());
			} else {
				leafBVHNodesR.add(processableLeafBVHNode);
				
				maximumRX = max(maximumRX, max.getX());
				maximumRY = max(maximumRY, max.getY());
				maximumRZ = max(maximumRZ, max.getZ());
				minimumRX = min(minimumRX, min.getX());
				minimumRY = min(minimumRY, min.getY());
				minimumRZ = min(minimumRZ, min.getZ());
			}
		}
		
		final Point3F maximumL = new Point3F(maximumLX, maximumLY, maximumLZ);
		final Point3F minimumL = new Point3F(minimumLX, minimumLY, minimumLZ);
		final Point3F maximumR = new Point3F(maximumRX, maximumRY, maximumRZ);
		final Point3F minimumR = new Point3F(minimumRX, minimumRY, minimumRZ);
		
		final BVHNode bVHNodeL = doCreateBVHNode(leafBVHNodesL, maximumL, minimumL, depth + 1);
		final BVHNode bVHNodeR = doCreateBVHNode(leafBVHNodesR, maximumR, minimumR, depth + 1);
		
		return new TreeBVHNode(maximum, minimum, depth, bVHNodeL, bVHNodeR);
	}
	
	private static BVHNode doCreateBVHNode(final List<Primitive> primitives, final List<Primitive> primitivesExternalToBVH) {
		System.out.println("Generating acceleration structure...");
		
		final List<LeafBVHNode> processableLeafBVHNodes = new ArrayList<>(primitives.size());
		
		float maximumX = Float.MIN_VALUE;
		float maximumY = Float.MIN_VALUE;
		float maximumZ = Float.MIN_VALUE;
		float minimumX = Float.MAX_VALUE;
		float minimumY = Float.MAX_VALUE;
		float minimumZ = Float.MAX_VALUE;
		
		for(final Primitive primitive : primitives) {
			final BoundingVolume3F boundingVolume = primitive.getBoundingVolume();
			
			if(boundingVolume instanceof InfiniteBoundingVolume3F) {
				primitivesExternalToBVH.add(primitive);
				
				continue;
			}
			
			final Point3F maximum = boundingVolume.getMaximum();
			final Point3F minimum = boundingVolume.getMinimum();
			
			maximumX = max(maximumX, maximum.getX());
			maximumY = max(maximumY, maximum.getY());
			maximumZ = max(maximumZ, maximum.getZ());
			minimumX = min(minimumX, minimum.getX());
			minimumY = min(minimumY, minimum.getY());
			minimumZ = min(minimumZ, minimum.getZ());
			
			processableLeafBVHNodes.add(new LeafBVHNode(maximum, minimum, 0, Arrays.asList(primitive)));
		}
		
		final BVHNode bVHNode = doCreateBVHNode(processableLeafBVHNodes, new Point3F(maximumX, maximumY, maximumZ), new Point3F(minimumX, minimumY, minimumZ), 0);
		
		System.out.println(" - Done.");
		
		return bVHNode;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static final class LeafBVHNode extends BVHNode {
		private final List<Primitive> primitives;
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		public LeafBVHNode(final Point3F maximum, final Point3F minimum, final int depth, final List<Primitive> primitives) {
			super(maximum, minimum, depth);
			
			this.primitives = Objects.requireNonNull(primitives, "primitives == null");
		}
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		public List<Primitive> getPrimitives() {
			return this.primitives;
		}
		
		@Override
		public boolean equals(final Object object) {
			if(object == this) {
				return true;
			} else if(!(object instanceof LeafBVHNode)) {
				return false;
			} else if(!Objects.equals(getBoundingVolume(), LeafBVHNode.class.cast(object).getBoundingVolume())) {
				return false;
			} else if(getDepth() != LeafBVHNode.class.cast(object).getDepth()) {
				return false;
			} else if(!Objects.equals(this.primitives, LeafBVHNode.class.cast(object).primitives)) {
				return false;
			} else {
				return true;
			}
		}
		
		@Override
		public boolean intersection(final MutableIntersection mutableIntersection) {
			if(mutableIntersection.isIntersecting(getBoundingVolume())) {
				boolean isIntersecting = false;
				
				for(final Primitive primitive : this.primitives) {
					if(mutableIntersection.intersection(primitive)) {
						isIntersecting = true;
					}
				}
				
				return isIntersecting;
			}
			
			return false;
		}
		
		@Override
		public boolean intersects(final Ray3F ray, final float tMinimum, final float tMaximum) {
			if(getBoundingVolume().contains(ray.getOrigin()) || getBoundingVolume().intersects(ray, tMinimum, tMaximum)) {
				for(final Primitive primitive : this.primitives) {
					if(primitive.intersects(ray, tMinimum, tMaximum)) {
						return true;
					}
				}
			}
			
			return false;
		}
		
		@Override
		public float intersectionT(final Ray3F ray, final float[] tBounds) {
			float t = Float.NaN;
			
			if(getBoundingVolume().contains(ray.getOrigin()) || getBoundingVolume().intersects(ray, tBounds[0], tBounds[1])) {
				for(final Primitive primitive : this.primitives) {
					t = minOrNaN(t, primitive.intersectionT(ray, tBounds[0], tBounds[1]));
					
					if(!isNaN(t)) {
						tBounds[1] = t;
					}
				}
			}
			
			return t;
		}
		
		@Override
		public int hashCode() {
			return Objects.hash(getBoundingVolume(), Integer.valueOf(getDepth()), this.primitives);
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static abstract class BVHNode {
		private final BoundingVolume3F boundingVolume;
		private final int depth;
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		protected BVHNode(final Point3F maximum, final Point3F minimum, final int depth) {
			this.boundingVolume = new AxisAlignedBoundingBox3F(maximum, minimum);
			this.depth = depth;
		}
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		public final BoundingVolume3F getBoundingVolume() {
			return this.boundingVolume;
		}
		
		public abstract boolean intersection(final MutableIntersection mutableIntersection);
		
		public abstract boolean intersects(final Ray3F ray, final float tMinimum, final float tMaximum);
		
		public abstract float intersectionT(final Ray3F ray, final float[] tBounds);
		
		public final int getDepth() {
			return this.depth;
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static final class TreeBVHNode extends BVHNode {
		private final BVHNode bVHNodeL;
		private final BVHNode bVHNodeR;
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		public TreeBVHNode(final Point3F maximum, final Point3F minimum, final int depth, final BVHNode bVHNodeL, final BVHNode bVHNodeR) {
			super(maximum, minimum, depth);
			
			this.bVHNodeL = Objects.requireNonNull(bVHNodeL, "bVHNodeL == null");
			this.bVHNodeR = Objects.requireNonNull(bVHNodeR, "bVHNodeR == null");
		}
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		@Override
		public boolean equals(final Object object) {
			if(object == this) {
				return true;
			} else if(!(object instanceof TreeBVHNode)) {
				return false;
			} else if(!Objects.equals(getBoundingVolume(), TreeBVHNode.class.cast(object).getBoundingVolume())) {
				return false;
			} else if(getDepth() != TreeBVHNode.class.cast(object).getDepth()) {
				return false;
			} else if(!Objects.equals(this.bVHNodeL, TreeBVHNode.class.cast(object).bVHNodeL)) {
				return false;
			} else if(!Objects.equals(this.bVHNodeR, TreeBVHNode.class.cast(object).bVHNodeR)) {
				return false;
			} else {
				return true;
			}
		}
		
		@Override
		public boolean intersection(final MutableIntersection mutableIntersection) {
			if(mutableIntersection.isIntersecting(getBoundingVolume())) {
				final boolean isIntersectingL = this.bVHNodeL.intersection(mutableIntersection);
				final boolean isIntersectingR = this.bVHNodeR.intersection(mutableIntersection);
				
				return isIntersectingL || isIntersectingR;
			}
			
			return false;
		}
		
		@Override
		public boolean intersects(final Ray3F ray, final float tMinimum, final float tMaximum) {
			return (getBoundingVolume().contains(ray.getOrigin()) || getBoundingVolume().intersects(ray, tMinimum, tMaximum)) && (this.bVHNodeL.intersects(ray, tMinimum, tMaximum) || this.bVHNodeR.intersects(ray, tMinimum, tMaximum));
		}
		
		@Override
		public float intersectionT(final Ray3F ray, final float[] tBounds) {
			return getBoundingVolume().contains(ray.getOrigin()) || getBoundingVolume().intersects(ray, tBounds[0], tBounds[1]) ? minOrNaN(this.bVHNodeL.intersectionT(ray, tBounds), this.bVHNodeR.intersectionT(ray, tBounds)) : Float.NaN;
		}
		
		@Override
		public int hashCode() {
			return Objects.hash(getBoundingVolume(), Integer.valueOf(getDepth()), this.bVHNodeL, this.bVHNodeR);
		}
	}
}