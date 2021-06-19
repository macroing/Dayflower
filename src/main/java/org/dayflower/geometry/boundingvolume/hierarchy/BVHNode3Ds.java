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
package org.dayflower.geometry.boundingvolume.hierarchy;

import static org.dayflower.utility.Doubles.MAX_VALUE;
import static org.dayflower.utility.Doubles.MIN_VALUE;
import static org.dayflower.utility.Doubles.abs;
import static org.dayflower.utility.Doubles.equal;
import static org.dayflower.utility.Doubles.max;
import static org.dayflower.utility.Doubles.min;
import static org.dayflower.utility.Ints.padding;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.dayflower.geometry.BoundingVolume3D;
import org.dayflower.geometry.Point3D;
import org.dayflower.geometry.Shape3D;
import org.dayflower.node.NodeFilter;
import org.dayflower.utility.ParameterArguments;
import org.macroing.java.io.IntArrayOutputStream;

/**
 * A class that consists exclusively of static methods that returns or performs various operations on {@link BVHNode3D} instances.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class BVHNode3Ds {
	private BVHNode3Ds() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Creates a bounding volume hierarchy (BVH) structure.
	 * 
	 * Returns a {@link BVHNode3D} instance that represents the root of the bounding volume hierarchy (BVH) structure.
	 * <p>
	 * If either {@code processableLeafBVHNodes}, at least one of its elements, {@code maximum} or {@code minimum} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code depth} is less than {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param processableLeafBVHNodes a {@code List} of {@link LeafBVHNode3D} instances to process
	 * @param maximum a {@link Point3D} instance with the maximum coordinates of the {@link BoundingVolume3D} instance that contains the returned {@code BVHNode3D} instance
	 * @param minimum a {@code Point3D} instance with the minimum coordinates of the {@code BoundingVolume3D} instance that contains the returned {@code BVHNode3D} instance
	 * @param depth the depth of the returned {@code BVHNode3D} instance
	 * @return a {@code BVHNode3D} instance that represents the root of the bounding volume hierarchy (BVH) structure
	 * @throws IllegalArgumentException thrown if, and only if, {@code depth} is less than {@code 0}
	 * @throws NullPointerException thrown if, and only if, either {@code processableLeafBVHNodes}, at least one of its elements, {@code maximum} or {@code minimum} are {@code null}
	 */
	public static <T extends Shape3D> BVHNode3D create(final List<LeafBVHNode3D<T>> processableLeafBVHNodes, final Point3D maximum, final Point3D minimum, final int depth) {
		final int size = processableLeafBVHNodes.size();
		final int sizeHalf = size / 2;
		
		if(size < 4) {
			final List<T> shapes = new ArrayList<>();
			
			for(final LeafBVHNode3D<T> processableLeafBVHNode : processableLeafBVHNodes) {
				for(final T shape : processableLeafBVHNode.getShapes()) {
					shapes.add(shape);
				}
			}
			
			return new LeafBVHNode3D<>(maximum, minimum, depth, shapes);
		}
		
		final double sideX = maximum.getX() - minimum.getX();
		final double sideY = maximum.getY() - minimum.getY();
		final double sideZ = maximum.getZ() - minimum.getZ();
		
		double minimumCost = size * (sideX * sideY + sideY * sideZ + sideZ * sideX);
		double bestSplit = MAX_VALUE;
		
		int bestAxis = -1;
		
		for(int axis = 0; axis < 3; axis++) {
			final double start = minimum.getComponent(axis);
			final double stop  = maximum.getComponent(axis);
			
			if(abs(stop - start) < 1.0e-4D) {
				continue;
			}
			
			final double step = (stop - start) / (1024.0D / (depth + 1.0D));
			
			for(double oldSplit = 0.0D, newSplit = start + step; newSplit < stop - step; oldSplit = newSplit, newSplit += step) {
//				The following test prevents an infinite loop from occurring (it might not be necessary anymore, due to a fix earlier):
				if(equal(oldSplit, newSplit)) {
					break;
				}
				
				double maximumLX = MIN_VALUE;
				double maximumLY = MIN_VALUE;
				double maximumLZ = MIN_VALUE;
				double minimumLX = MAX_VALUE;
				double minimumLY = MAX_VALUE;
				double minimumLZ = MAX_VALUE;
				double maximumRX = MIN_VALUE;
				double maximumRY = MIN_VALUE;
				double maximumRZ = MIN_VALUE;
				double minimumRX = MAX_VALUE;
				double minimumRY = MAX_VALUE;
				double minimumRZ = MAX_VALUE;
				
				int countL = 0;
				int countR = 0;
				
				for(final LeafBVHNode3D<T> processableLeafBVHNode : processableLeafBVHNodes) {
					final BoundingVolume3D boundingVolume = processableLeafBVHNode.getBoundingVolume();
					
					final Point3D max = boundingVolume.getMaximum();
					final Point3D mid = boundingVolume.getMidpoint();
					final Point3D min = boundingVolume.getMinimum();
					
					final double value = mid.getComponent(axis);
					
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
				
				final double sideLX = maximumLX - minimumLX;
				final double sideLY = maximumLY - minimumLY;
				final double sideLZ = maximumLZ - minimumLZ;
				final double sideRX = maximumRX - minimumRX;
				final double sideRY = maximumRY - minimumRY;
				final double sideRZ = maximumRZ - minimumRZ;
				
				final double surfaceL = sideLX * sideLY + sideLY * sideLZ + sideLZ * sideLX;
				final double surfaceR = sideRX * sideRY + sideRY * sideRZ + sideRZ * sideRX;
				
				final double cost = surfaceL * countL + surfaceR * countR;
				
				if(cost < minimumCost) {
					minimumCost = cost;
					bestSplit = newSplit;
					bestAxis = axis;
				}
			}
		}
		
		if(bestAxis == -1) {
			final List<T> shapes = new ArrayList<>();
			
			for(final LeafBVHNode3D<T> processableLeafBVHNode : processableLeafBVHNodes) {
				for(final T shape : processableLeafBVHNode.getShapes()) {
					shapes.add(shape);
				}
			}
			
			return new LeafBVHNode3D<>(maximum, minimum, depth, shapes);
		}
		
		final List<LeafBVHNode3D<T>> leafBVHNodesL = new ArrayList<>(sizeHalf);
		final List<LeafBVHNode3D<T>> leafBVHNodesR = new ArrayList<>(sizeHalf);
		
		double maximumLX = MIN_VALUE;
		double maximumLY = MIN_VALUE;
		double maximumLZ = MIN_VALUE;
		double minimumLX = MAX_VALUE;
		double minimumLY = MAX_VALUE;
		double minimumLZ = MAX_VALUE;
		double maximumRX = MIN_VALUE;
		double maximumRY = MIN_VALUE;
		double maximumRZ = MIN_VALUE;
		double minimumRX = MAX_VALUE;
		double minimumRY = MAX_VALUE;
		double minimumRZ = MAX_VALUE;
		
		for(final LeafBVHNode3D<T> processableLeafBVHNode : processableLeafBVHNodes) {
			final BoundingVolume3D boundingVolume = processableLeafBVHNode.getBoundingVolume();
			
			final Point3D max = boundingVolume.getMaximum();
			final Point3D mid = boundingVolume.getMidpoint();
			final Point3D min = boundingVolume.getMinimum();
			
			final double value = mid.getComponent(bestAxis);
			
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
		
		final Point3D maximumL = new Point3D(maximumLX, maximumLY, maximumLZ);
		final Point3D minimumL = new Point3D(minimumLX, minimumLY, minimumLZ);
		final Point3D maximumR = new Point3D(maximumRX, maximumRY, maximumRZ);
		final Point3D minimumR = new Point3D(minimumRX, minimumRY, minimumRZ);
		
		final BVHNode3D bVHNodeL = create(leafBVHNodesL, maximumL, minimumL, depth + 1);
		final BVHNode3D bVHNodeR = create(leafBVHNodesR, maximumR, minimumR, depth + 1);
		
		return new TreeBVHNode3D(maximum, minimum, depth, bVHNodeL, bVHNodeR);
	}
	
	/**
	 * Returns an {@code int[]} with a compiled version of {@code rootBVHNode}.
	 * <p>
	 * If either {@code rootBVHNode}, {@code shapes} or at least one of its elements are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param rootBVHNode a {@link BVHNode3D} instance that represents the root of the bounding volume hierarchy (BVH) structure to compile
	 * @param shapes a {@code List} of {@link Shape3D} instances
	 * @return an {@code int[]} with a compiled version of {@code rootBVHNode}
	 * @throws NullPointerException thrown if, and only if, either {@code rootBVHNode}, {@code shapes} or at least one of its elements are {@code null}
	 */
	public static <T extends Shape3D> int[] toArray(final BVHNode3D rootBVHNode, final List<T> shapes) {
		Objects.requireNonNull(rootBVHNode, "rootBVHNode == null");
		
		ParameterArguments.requireNonNullList(shapes, "shapes");
		
		final List<BVHNode3D> bVHNodes = NodeFilter.filterAll(rootBVHNode, BVHNode3D.class);
		final List<BoundingVolume3D> boundingVolumes = NodeFilter.filterAll(rootBVHNode, BVHNode3D.class).stream().map(bVHNode -> bVHNode.getBoundingVolume()).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
		
		final int[] offsets = new int[bVHNodes.size()];
		
		for(int i = 0, j = 0; i < offsets.length; j += bVHNodes.get(i).getArrayLength(), i++) {
			offsets[i] = j;
		}
		
		try(final IntArrayOutputStream intArrayOutputStream = new IntArrayOutputStream()) {
			for(int i = 0; i < bVHNodes.size(); i++) {
				final BVHNode3D bVHNode = bVHNodes.get(i);
				
				if(bVHNode instanceof LeafBVHNode3D) {
					final LeafBVHNode3D<?> leafBVHNode = LeafBVHNode3D.class.cast(bVHNode);
					
					final int id = LeafBVHNode3D.ID;
					final int boundingVolumeOffset = boundingVolumes.indexOf(leafBVHNode.getBoundingVolume());
					final int nextOffset = doFindNextOffset(bVHNodes, leafBVHNode.getDepth(), i + 1, offsets);
					final int shapeCount = leafBVHNode.getShapeCount();
					
					intArrayOutputStream.writeInt(id);
					intArrayOutputStream.writeInt(boundingVolumeOffset);
					intArrayOutputStream.writeInt(nextOffset);
					intArrayOutputStream.writeInt(shapeCount);
					
					for(final Shape3D shape : leafBVHNode.getShapes()) {
						intArrayOutputStream.writeInt(shapes.indexOf(shape));
					}
					
					final int padding = padding(4 + shapeCount);
					
					for(int j = 0; j < padding; j++) {
						intArrayOutputStream.writeInt(0);
					}
				} else if(bVHNode instanceof TreeBVHNode3D) {
					final TreeBVHNode3D treeBVHNode = TreeBVHNode3D.class.cast(bVHNode);
					
					final int id = TreeBVHNode3D.ID;
					final int boundingVolumeOffset = boundingVolumes.indexOf(treeBVHNode.getBoundingVolume());
					final int nextOffset = doFindNextOffset(bVHNodes, treeBVHNode.getDepth(), i + 1, offsets);
					final int leftOffset = doFindLeftOffset(bVHNodes, treeBVHNode.getDepth(), i + 1, offsets);
					
					intArrayOutputStream.writeInt(id);
					intArrayOutputStream.writeInt(boundingVolumeOffset);
					intArrayOutputStream.writeInt(nextOffset);
					intArrayOutputStream.writeInt(leftOffset);
					intArrayOutputStream.writeInt(0);
					intArrayOutputStream.writeInt(0);
					intArrayOutputStream.writeInt(0);
					intArrayOutputStream.writeInt(0);
				}
			}
			
			return intArrayOutputStream.toIntArray();
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static int doFindLeftOffset(final List<BVHNode3D> bVHNodes, final int depth, final int index, final int[] offsets) {
		for(int i = index; i < bVHNodes.size(); i++) {
			if(bVHNodes.get(i).getDepth() == depth + 1) {
				return offsets[i];
			}
		}
		
		return -1;
	}
	
	private static int doFindNextOffset(final List<BVHNode3D> bVHNodes, final int depth, final int index, final int[] offsets) {
		for(int i = index; i < bVHNodes.size(); i++) {
			if(bVHNodes.get(i).getDepth() <= depth) {
				return offsets[i];
			}
		}
		
		return -1;
	}
}