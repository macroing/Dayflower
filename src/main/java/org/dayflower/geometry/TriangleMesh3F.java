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
package org.dayflower.geometry;

import static org.dayflower.util.Floats.abs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * A {@code TriangleMesh3F} denotes a 3-dimensional triangle mesh that uses the data type {@code float}.
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class TriangleMesh3F implements Shape3F {
	private final Node node;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code TriangleMesh3F} instance.
	 * <p>
	 * If either {@code triangles} or any of its elements are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param triangles a {@code List} of {@link Triangle3F} instances
	 * @throws NullPointerException thrown if, and only if, either {@code triangles} or any of its elements are {@code null}
	 */
	public TriangleMesh3F(final List<Triangle3F> triangles) {
		this.node = doCreateNode(Objects.requireNonNull(triangles, "triangles == null"));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link BoundingVolume3F} instance that contains this {@code TriangleMesh3F} instance.
	 * 
	 * @return a {@code BoundingVolume3F} instance that contains this {@code TriangleMesh3F} instance
	 */
	@Override
	public BoundingVolume3F getBoundingVolume() {
		return this.node.getBoundingVolume();
	}
	
	/**
	 * Samples this {@code TriangleMesh3F} instance.
	 * <p>
	 * Returns an optional {@link SurfaceSample3F} with the surface sample.
	 * <p>
	 * If either {@code referencePoint} or {@code referenceSurfaceNormal} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Note: This method has not been implemented yet.
	 * 
	 * @param referencePoint the reference point on this {@code TriangleMesh3F} instance
	 * @param referenceSurfaceNormal the reference surface normal on this {@code TriangleMesh3F} instance
	 * @param u a random {@code float} with a uniform distribution between {@code 0.0F} and {@code 1.0F}
	 * @param v a random {@code float} with a uniform distribution between {@code 0.0F} and {@code 1.0F}
	 * @return an optional {@code SurfaceSample3F} with the surface sample
	 * @throws NullPointerException thrown if, and only if, either {@code referencePoint} or {@code referenceSurfaceNormal} are {@code null}
	 */
	@Override
	public Optional<SurfaceSample3F> sample(final Point3F referencePoint, final Vector3F referenceSurfaceNormal, final float u, final float v) {
		Objects.requireNonNull(referencePoint, "referencePoint == null");
		Objects.requireNonNull(referenceSurfaceNormal, "referenceSurfaceNormal == null");
		
		return Optional.empty();//TODO: Implement!
	}
	
	/**
	 * Performs an intersection test between {@code ray} and this {@code TriangleMesh3F} instance.
	 * <p>
	 * Returns an {@code Optional} with an optional {@link SurfaceIntersection3F} instance that contains information about the intersection, if it was found.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3F} to perform an intersection test against this {@code TriangleMesh3F} instance
	 * @return an {@code Optional} with an optional {@code SurfaceIntersection3F} instance that contains information about the intersection, if it was found
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	@Override
	public Optional<SurfaceIntersection3F> intersection(final Ray3F ray) {
		return intersection(ray, 0.0001F, Float.MAX_VALUE);
	}
	
	/**
	 * Performs an intersection test between {@code ray} and this {@code TriangleMesh3F} instance.
	 * <p>
	 * Returns an {@code Optional} with an optional {@link SurfaceIntersection3F} instance that contains information about the intersection, if it was found.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3F} to perform an intersection test against this {@code TriangleMesh3F} instance
	 * @param tMinimum the minimum parametric distance
	 * @param tMaximum the maximum parametric distance
	 * @return an {@code Optional} with an optional {@code SurfaceIntersection3F} instance that contains information about the intersection, if it was found
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	@Override
	public Optional<SurfaceIntersection3F> intersection(final Ray3F ray, final float tMinimum, final float tMaximum) {
		return this.node.intersection(ray, tMinimum, tMaximum);
	}
	
	/**
	 * Returns a {@code String} representation of this {@code TriangleMesh3F} instance.
	 * 
	 * @return a {@code String} representation of this {@code TriangleMesh3F} instance
	 */
	@Override
	public String toString() {
		return "new TriangleMesh3F(...)";
	}
	
	/**
	 * Compares {@code object} to this {@code TriangleMesh3F} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code TriangleMesh3F}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code TriangleMesh3F} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code TriangleMesh3F}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof TriangleMesh3F)) {
			return false;
		} else if(!Objects.equals(this.node, TriangleMesh3F.class.cast(object).node)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns {@code true} if, and only if, {@code ray} intersects this {@code TriangleMesh3F} instance, {@code false} otherwise.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3F} to perform an intersection test against this {@code TriangleMesh3F} instance
	 * @return {@code true} if, and only if, {@code ray} intersects this {@code TriangleMesh3F} instance, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	@Override
	public boolean intersects(final Ray3F ray) {
		return intersects(ray, 0.0001F, Float.MAX_VALUE);
	}
	
	/**
	 * Returns {@code true} if, and only if, {@code ray} intersects this {@code TriangleMesh3F} instance, {@code false} otherwise.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3F} to perform an intersection test against this {@code TriangleMesh3F} instance
	 * @param tMinimum the minimum parametric distance
	 * @param tMaximum the maximum parametric distance
	 * @return {@code true} if, and only if, {@code ray} intersects this {@code TriangleMesh3F} instance, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	@Override
	public boolean intersects(final Ray3F ray, final float tMinimum, final float tMaximum) {
		return this.node.intersects(ray, tMinimum, tMaximum);
	}
	
	/**
	 * Returns the probability density function (PDF) value for solid angle.
	 * <p>
	 * If either {@code referencePoint}, {@code referenceSurfaceNormal}, {@code point} or {@code surfaceNormal} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Note: This method has not been implemented yet.
	 * 
	 * @param referencePoint the reference point on this {@code Triangle3F} instance
	 * @param referenceSurfaceNormal the reference surface normal on this {@code Triangle3F} instance
	 * @param point the point on this {@code Triangle3F} instance
	 * @param surfaceNormal the surface normal on this {@code Triangle3F} instance
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
	 * Returns the surface area of this {@code TriangleMesh3F} instance.
	 * 
	 * @return the surface area of this {@code TriangleMesh3F} instance
	 */
	@Override
	public float getSurfaceArea() {
		return this.node.getSurfaceArea();
	}
	
	/**
	 * Returns the surface area probability density function (PDF) value of this {@code TriangleMesh3F} instance.
	 * <p>
	 * Note: This method has not been implemented yet.
	 * 
	 * @return the surface area probability density function (PDF) value of this {@code TriangleMesh3F} instance
	 */
	@Override
	public float getSurfaceAreaProbabilityDensityFunctionValue() {
		return 0.0F;//TODO: Implement!
	}
	
	/**
	 * Returns the volume of this {@code TriangleMesh3F} instance.
	 * <p>
	 * This method returns {@code 0.0F}.
	 * 
	 * @return the volume of this {@code TriangleMesh3F} instance
	 */
	@Override
	public float getVolume() {
		return 0.0F;
	}
	
	/**
	 * Returns a hash code for this {@code TriangleMesh3F} instance.
	 * 
	 * @return a hash code for this {@code TriangleMesh3F} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.node);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static Node doCreateNode(final List<LeafNode> processableLeafNodes, final Point3F maximum, final Point3F minimum, final int depth) {
		final int size = processableLeafNodes.size();
		final int sizeHalf = size / 2;
		
		if(size < 4) {
			final List<Triangle3F> triangles = new ArrayList<>();
			
			for(final LeafNode processableLeafNode : processableLeafNodes) {
				for(final Triangle3F triangle : processableLeafNode.getTriangles()) {
					triangles.add(triangle);
				}
			}
			
			return new LeafNode(maximum, minimum, depth, triangles);
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
			
			for(float split = start + step; split < stop - step; split += step) {
				Point3F maximumL = Point3F.minimum();
				Point3F minimumL = Point3F.maximum();
				Point3F maximumR = Point3F.minimum();
				Point3F minimumR = Point3F.maximum();
				
				int countL = 0;
				int countR = 0;
				
				for(final LeafNode processableLeafNode : processableLeafNodes) {
					final Point3F midpoint = processableLeafNode.getBoundingVolume().getMidpoint();
					
					final float value = midpoint.getComponent(axis);
					
					if(value < split) {
						maximumL = Point3F.maximum(maximumL, processableLeafNode.getBoundingVolume().getMaximum());
						minimumL = Point3F.minimum(minimumL, processableLeafNode.getBoundingVolume().getMinimum());
						
						countL++;
					} else {
						maximumR = Point3F.maximum(maximumR, processableLeafNode.getBoundingVolume().getMaximum());
						minimumR = Point3F.minimum(minimumR, processableLeafNode.getBoundingVolume().getMinimum());
						
						countR++;
					}
				}
				
				if(countL <= 1 || countR <= 1) {
					continue;
				}
				
				final float sideLX = maximumL.getX() - minimumL.getX();
				final float sideLY = maximumL.getY() - minimumL.getY();
				final float sideLZ = maximumL.getZ() - minimumL.getZ();
				final float sideRX = maximumR.getX() - minimumR.getX();
				final float sideRY = maximumR.getY() - minimumR.getY();
				final float sideRZ = maximumR.getZ() - minimumR.getZ();
				
				final float surfaceL = sideLX * sideLY + sideLY * sideLZ + sideLZ * sideLX;
				final float surfaceR = sideRX * sideRY + sideRY * sideRZ + sideRZ * sideRX;
				
				final float cost = surfaceL * countL + surfaceR * countR;
				
				if(cost < minimumCost) {
					minimumCost = cost;
					bestSplit = split;
					bestAxis = axis;
				}
			}
		}
		
		if(bestAxis == -1) {
			final List<Triangle3F> triangles = new ArrayList<>();
			
			for(final LeafNode processableLeafNode : processableLeafNodes) {
				for(final Triangle3F triangle : processableLeafNode.getTriangles()) {
					triangles.add(triangle);
				}
			}
			
			return new LeafNode(maximum, minimum, depth, triangles);
		}
		
		final List<LeafNode> leafNodesL = new ArrayList<>(sizeHalf);
		final List<LeafNode> leafNodesR = new ArrayList<>(sizeHalf);
		
		Point3F maximumL = Point3F.minimum();
		Point3F minimumL = Point3F.maximum();
		Point3F maximumR = Point3F.minimum();
		Point3F minimumR = Point3F.maximum();
		
		for(final LeafNode processableLeafNode : processableLeafNodes) {
			final Point3F midpoint = processableLeafNode.getBoundingVolume().getMidpoint();
			
			final float value = midpoint.getComponent(bestAxis);
			
			if(value < bestSplit) {
				leafNodesL.add(processableLeafNode);
				
				maximumL = Point3F.maximum(maximumL, processableLeafNode.getBoundingVolume().getMaximum());
				minimumL = Point3F.minimum(minimumL, processableLeafNode.getBoundingVolume().getMinimum());
			} else {
				leafNodesR.add(processableLeafNode);
				
				maximumR = Point3F.maximum(maximumR, processableLeafNode.getBoundingVolume().getMaximum());
				minimumR = Point3F.minimum(minimumR, processableLeafNode.getBoundingVolume().getMinimum());
			}
		}
		
		final Node nodeL = doCreateNode(leafNodesL, maximumL, minimumL, depth + 1);
		final Node nodeR = doCreateNode(leafNodesR, maximumR, minimumR, depth + 1);
		
		return new TreeNode(maximum, minimum, depth, nodeL, nodeR);
	}
	
	private static Node doCreateNode(final List<Triangle3F> triangles) {
		final List<LeafNode> processableLeafNodes = new ArrayList<>(triangles.size());
		
		Point3F maximum = Point3F.minimum();
		Point3F minimum = Point3F.maximum();
		
		for(final Triangle3F triangle : triangles) {
			final Point3F a = triangle.getA().getPosition();
			final Point3F b = triangle.getB().getPosition();
			final Point3F c = triangle.getC().getPosition();
			
			final LeafNode leafNode = new LeafNode(Point3F.maximum(a, b, c), Point3F.minimum(a, b, c), 0, Arrays.asList(triangle));
			
			processableLeafNodes.add(leafNode);
			
			maximum = Point3F.maximum(maximum, leafNode.getBoundingVolume().getMaximum());
			minimum = Point3F.minimum(minimum, leafNode.getBoundingVolume().getMinimum());
		}
		
		return doCreateNode(processableLeafNodes, maximum, minimum, 0);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static final class LeafNode extends Node {
		private final List<Triangle3F> triangles;
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		public LeafNode(final Point3F maximum, final Point3F minimum, final int depth, final List<Triangle3F> triangles) {
			super(maximum, minimum, depth);
			
			this.triangles = Objects.requireNonNull(triangles, "triangles == null");
		}
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		public List<Triangle3F> getTriangles() {
			return this.triangles;
		}
		
		@Override
		public Optional<SurfaceIntersection3F> intersection(final Ray3F ray, final float tMinimum, final float tMaximum) {
			Optional<SurfaceIntersection3F> optionalSurfaceIntersectionA = Optional.empty();
			
			if(getBoundingVolume().intersects(ray, tMinimum, tMaximum)) {
				for(final Triangle3F triangle : this.triangles) {
					optionalSurfaceIntersectionA = SurfaceIntersection3F.closest(optionalSurfaceIntersectionA, triangle.intersection(ray, tMinimum, tMaximum));
				}
			}
			
			return optionalSurfaceIntersectionA;
		}
		
		@Override
		public boolean equals(final Object object) {
			if(object == this) {
				return true;
			} else if(!(object instanceof LeafNode)) {
				return false;
			} else if(!Objects.equals(getBoundingVolume(), LeafNode.class.cast(object).getBoundingVolume())) {
				return false;
			} else if(getDepth() != LeafNode.class.cast(object).getDepth()) {
				return false;
			} else if(!Objects.equals(this.triangles, LeafNode.class.cast(object).triangles)) {
				return false;
			} else {
				return true;
			}
		}
		
		@Override
		public boolean intersects(final Ray3F ray, final float tMinimum, final float tMaximum) {
			if(getBoundingVolume().intersects(ray, tMinimum, tMaximum)) {
				for(final Triangle3F triangle : this.triangles) {
					if(triangle.intersects(ray, tMinimum, tMaximum)) {
						return true;
					}
				}
			}
			
			return false;
		}
		
		@Override
		public float getSurfaceArea() {
			float surfaceArea = 0.0F;
			
			for(final Triangle3F triangle : this.triangles) {
				surfaceArea += triangle.getSurfaceArea();
			}
			
			return surfaceArea;
		}
		
		@Override
		public int hashCode() {
			return Objects.hash(getBoundingVolume(), Integer.valueOf(getDepth()), this.triangles);
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static abstract class Node {
		private final BoundingVolume3F boundingVolume;
		private final int depth;
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		protected Node(final Point3F maximum, final Point3F minimum, final int depth) {
			this.boundingVolume = new AxisAlignedBoundingBox3F(maximum, minimum);
			this.depth = depth;
		}
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		public final BoundingVolume3F getBoundingVolume() {
			return this.boundingVolume;
		}
		
		public abstract Optional<SurfaceIntersection3F> intersection(final Ray3F ray, final float tMinimum, final float tMaximum);
		
		public abstract boolean intersects(final Ray3F ray, final float tMinimum, final float tMaximum);
		
		public abstract float getSurfaceArea();
		
		public final int getDepth() {
			return this.depth;
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static final class TreeNode extends Node {
		private final Node nodeL;
		private final Node nodeR;
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		public TreeNode(final Point3F maximum, final Point3F minimum, final int depth, final Node nodeL, final Node nodeR) {
			super(maximum, minimum, depth);
			
			this.nodeL = Objects.requireNonNull(nodeL, "nodeL == null");
			this.nodeR = Objects.requireNonNull(nodeR, "nodeR == null");
		}
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		@Override
		public Optional<SurfaceIntersection3F> intersection(final Ray3F ray, final float tMinimum, final float tMaximum) {
			return getBoundingVolume().intersects(ray, tMinimum, tMaximum) ? SurfaceIntersection3F.closest(this.nodeL.intersection(ray, tMinimum, tMaximum), this.nodeR.intersection(ray, tMinimum, tMaximum)) : Optional.empty();
		}
		
		@Override
		public boolean equals(final Object object) {
			if(object == this) {
				return true;
			} else if(!(object instanceof TreeNode)) {
				return false;
			} else if(!Objects.equals(getBoundingVolume(), TreeNode.class.cast(object).getBoundingVolume())) {
				return false;
			} else if(getDepth() != TreeNode.class.cast(object).getDepth()) {
				return false;
			} else if(!Objects.equals(this.nodeL, TreeNode.class.cast(object).nodeL)) {
				return false;
			} else if(!Objects.equals(this.nodeR, TreeNode.class.cast(object).nodeR)) {
				return false;
			} else {
				return true;
			}
		}
		
		@Override
		public boolean intersects(final Ray3F ray, final float tMinimum, final float tMaximum) {
			return getBoundingVolume().intersects(ray, tMinimum, tMaximum) && (this.nodeL.intersects(ray, tMinimum, tMaximum) || this.nodeR.intersects(ray, tMinimum, tMaximum));
		}
		
		@Override
		public float getSurfaceArea() {
			return this.nodeL.getSurfaceArea() + this.nodeR.getSurfaceArea();
		}
		
		@Override
		public int hashCode() {
			return Objects.hash(getBoundingVolume(), Integer.valueOf(getDepth()), this.nodeL, this.nodeR);
		}
	}
}