/**
 * Copyright 2014 - 2023 J&#246;rgen Lundgren
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

import java.lang.reflect.Field;//TODO: Add Unit Tests!
import java.util.ArrayList;
import java.util.List;

import org.dayflower.geometry.BoundingVolume3D;
import org.dayflower.geometry.Point3D;
import org.dayflower.geometry.Shape3D;

import org.macroing.java.lang.Doubles;

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
	 * If either {@code processableBVHItems}, at least one of its elements, {@code maximum} or {@code minimum} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code depth} is less than {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param <T> the generic {@link Shape3D} type to use
	 * @param processableBVHItems a {@code List} of {@link BVHItem3D} instances to process
	 * @param maximum a {@link Point3D} instance with the maximum coordinates of the {@link BoundingVolume3D} instance that contains the returned {@code BVHNode3D} instance
	 * @param minimum a {@code Point3D} instance with the minimum coordinates of the {@code BoundingVolume3D} instance that contains the returned {@code BVHNode3D} instance
	 * @param depth the depth of the returned {@code BVHNode3D} instance
	 * @return a {@code BVHNode3D} instance that represents the root of the bounding volume hierarchy (BVH) structure
	 * @throws IllegalArgumentException thrown if, and only if, {@code depth} is less than {@code 0}
	 * @throws NullPointerException thrown if, and only if, either {@code processableBVHItems}, at least one of its elements, {@code maximum} or {@code minimum} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public static <T extends Shape3D> BVHNode3D create(final List<BVHItem3D<T>> processableBVHItems, final Point3D maximum, final Point3D minimum, final int depth) {
		final int size = processableBVHItems.size();
		final int sizeHalf = size / 2;
		
		if(size < 4) {
			final List<T> shapes = new ArrayList<>(size);
			
			for(final BVHItem3D<T> processableBVHItem : processableBVHItems) {
				shapes.add(processableBVHItem.getShape());
			}
			
			return new LeafBVHNode3D<>(maximum, minimum, depth, shapes);
		}
		
		final double sideX = maximum.x - minimum.x;
		final double sideY = maximum.y - minimum.y;
		final double sideZ = maximum.z - minimum.z;
		
		double minimumCost = size * (sideX * sideY + sideY * sideZ + sideZ * sideX);
		double bestSplit = Doubles.MAX_VALUE;
		
		int bestAxis = -1;
		
		for(int axis = 0; axis < 3; axis++) {
			final double start = minimum.getComponent(axis);
			final double stop  = maximum.getComponent(axis);
			
			if(Doubles.abs(stop - start) < 1.0e-4D) {
				continue;
			}
			
			final double step = (stop - start) / (1024.0D / (depth + 1.0D));
			
			for(double oldSplit = 0.0D, newSplit = start + step; newSplit < stop - step; oldSplit = newSplit, newSplit += step) {
//				The following test prevents an infinite loop from occurring (it might not be necessary anymore, due to a fix earlier):
				if(Doubles.equals(oldSplit, newSplit)) {
					break;
				}
				
				double maximumLX = Doubles.MIN_VALUE;
				double maximumLY = Doubles.MIN_VALUE;
				double maximumLZ = Doubles.MIN_VALUE;
				double minimumLX = Doubles.MAX_VALUE;
				double minimumLY = Doubles.MAX_VALUE;
				double minimumLZ = Doubles.MAX_VALUE;
				double maximumRX = Doubles.MIN_VALUE;
				double maximumRY = Doubles.MIN_VALUE;
				double maximumRZ = Doubles.MIN_VALUE;
				double minimumRX = Doubles.MAX_VALUE;
				double minimumRY = Doubles.MAX_VALUE;
				double minimumRZ = Doubles.MAX_VALUE;
				
				int countL = 0;
				int countR = 0;
				
				for(final BVHItem3D<T> processableBVHItem : processableBVHItems) {
					final BoundingVolume3D boundingVolume = processableBVHItem.getBoundingVolume();
					
					final Point3D max = boundingVolume.getMaximum();
					final Point3D mid = boundingVolume.getMidpoint();
					final Point3D min = boundingVolume.getMinimum();
					
					final double value = mid.getComponent(axis);
					
					if(value < newSplit) {
						maximumLX = Doubles.max(maximumLX, max.x);
						maximumLY = Doubles.max(maximumLY, max.y);
						maximumLZ = Doubles.max(maximumLZ, max.z);
						minimumLX = Doubles.min(minimumLX, min.x);
						minimumLY = Doubles.min(minimumLY, min.y);
						minimumLZ = Doubles.min(minimumLZ, min.z);
						
						countL++;
					} else {
						maximumRX = Doubles.max(maximumRX, max.x);
						maximumRY = Doubles.max(maximumRY, max.y);
						maximumRZ = Doubles.max(maximumRZ, max.z);
						minimumRX = Doubles.min(minimumRX, min.x);
						minimumRY = Doubles.min(minimumRY, min.y);
						minimumRZ = Doubles.min(minimumRZ, min.z);
						
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
			final List<T> shapes = new ArrayList<>(size);
			
			for(final BVHItem3D<T> processableBVHItem : processableBVHItems) {
				shapes.add(processableBVHItem.getShape());
			}
			
			return new LeafBVHNode3D<>(maximum, minimum, depth, shapes);
		}
		
		final List<BVHItem3D<T>> processableBVHItemsL = new ArrayList<>(sizeHalf);
		final List<BVHItem3D<T>> processableBVHItemsR = new ArrayList<>(sizeHalf);
		
		double maximumLX = Doubles.MIN_VALUE;
		double maximumLY = Doubles.MIN_VALUE;
		double maximumLZ = Doubles.MIN_VALUE;
		double minimumLX = Doubles.MAX_VALUE;
		double minimumLY = Doubles.MAX_VALUE;
		double minimumLZ = Doubles.MAX_VALUE;
		double maximumRX = Doubles.MIN_VALUE;
		double maximumRY = Doubles.MIN_VALUE;
		double maximumRZ = Doubles.MIN_VALUE;
		double minimumRX = Doubles.MAX_VALUE;
		double minimumRY = Doubles.MAX_VALUE;
		double minimumRZ = Doubles.MAX_VALUE;
		
		for(final BVHItem3D<T> processableBVHItem : processableBVHItems) {
			final BoundingVolume3D boundingVolume = processableBVHItem.getBoundingVolume();
			
			final Point3D max = boundingVolume.getMaximum();
			final Point3D mid = boundingVolume.getMidpoint();
			final Point3D min = boundingVolume.getMinimum();
			
			final double value = mid.getComponent(bestAxis);
			
			if(value < bestSplit) {
				processableBVHItemsL.add(processableBVHItem);
				
				maximumLX = Doubles.max(maximumLX, max.x);
				maximumLY = Doubles.max(maximumLY, max.y);
				maximumLZ = Doubles.max(maximumLZ, max.z);
				minimumLX = Doubles.min(minimumLX, min.x);
				minimumLY = Doubles.min(minimumLY, min.y);
				minimumLZ = Doubles.min(minimumLZ, min.z);
			} else {
				processableBVHItemsR.add(processableBVHItem);
				
				maximumRX = Doubles.max(maximumRX, max.x);
				maximumRY = Doubles.max(maximumRY, max.y);
				maximumRZ = Doubles.max(maximumRZ, max.z);
				minimumRX = Doubles.min(minimumRX, min.x);
				minimumRY = Doubles.min(minimumRY, min.y);
				minimumRZ = Doubles.min(minimumRZ, min.z);
			}
		}
		
		final Point3D maximumL = new Point3D(maximumLX, maximumLY, maximumLZ);
		final Point3D minimumL = new Point3D(minimumLX, minimumLY, minimumLZ);
		final Point3D maximumR = new Point3D(maximumRX, maximumRY, maximumRZ);
		final Point3D minimumR = new Point3D(minimumRX, minimumRY, minimumRZ);
		
		final BVHNode3D bVHNodeL = create(processableBVHItemsL, maximumL, minimumL, depth + 1);
		final BVHNode3D bVHNodeR = create(processableBVHItemsR, maximumR, minimumR, depth + 1);
		
		return new TreeBVHNode3D(maximum, minimum, depth, bVHNodeL, bVHNodeR);
	}
}