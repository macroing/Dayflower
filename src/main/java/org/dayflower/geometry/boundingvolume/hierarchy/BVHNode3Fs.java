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

import static org.dayflower.utility.Floats.MAX_VALUE;
import static org.dayflower.utility.Floats.MIN_VALUE;
import static org.dayflower.utility.Floats.abs;
import static org.dayflower.utility.Floats.equal;
import static org.dayflower.utility.Floats.max;
import static org.dayflower.utility.Floats.min;

import java.lang.reflect.Field;//TODO: Add Unit Tests!
import java.util.ArrayList;
import java.util.List;

import org.dayflower.geometry.BoundingVolume3F;
import org.dayflower.geometry.Point3F;
import org.dayflower.geometry.Shape3F;

/**
 * A class that consists exclusively of static methods that returns or performs various operations on {@link BVHNode3F} instances.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class BVHNode3Fs {
	private BVHNode3Fs() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Creates a bounding volume hierarchy (BVH) structure.
	 * 
	 * Returns a {@link BVHNode3F} instance that represents the root of the bounding volume hierarchy (BVH) structure.
	 * <p>
	 * If either {@code processableBVHItems}, at least one of its elements, {@code maximum} or {@code minimum} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code depth} is less than {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param <T> the generic {@link Shape3F} type to use
	 * @param processableBVHItems a {@code List} of {@link BVHItem3F} instances to process
	 * @param maximum a {@link Point3F} instance with the maximum coordinates of the {@link BoundingVolume3F} instance that contains the returned {@code BVHNode3F} instance
	 * @param minimum a {@code Point3F} instance with the minimum coordinates of the {@code BoundingVolume3F} instance that contains the returned {@code BVHNode3F} instance
	 * @param depth the depth of the returned {@code BVHNode3F} instance
	 * @return a {@code BVHNode3F} instance that represents the root of the bounding volume hierarchy (BVH) structure
	 * @throws IllegalArgumentException thrown if, and only if, {@code depth} is less than {@code 0}
	 * @throws NullPointerException thrown if, and only if, either {@code processableBVHItems}, at least one of its elements, {@code maximum} or {@code minimum} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public static <T extends Shape3F> BVHNode3F create(final List<BVHItem3F<T>> processableBVHItems, final Point3F maximum, final Point3F minimum, final int depth) {
		final int size = processableBVHItems.size();
		final int sizeHalf = size / 2;
		
		if(size < 4) {
			final List<T> shapes = new ArrayList<>(size);
			
			for(final BVHItem3F<T> processableBVHItem : processableBVHItems) {
				shapes.add(processableBVHItem.getShape());
			}
			
			return new LeafBVHNode3F<>(maximum, minimum, depth, shapes);
		}
		
		final float sideX = maximum.getX() - minimum.getX();
		final float sideY = maximum.getY() - minimum.getY();
		final float sideZ = maximum.getZ() - minimum.getZ();
		
		float minimumCost = size * (sideX * sideY + sideY * sideZ + sideZ * sideX);
		float bestSplit = MAX_VALUE;
		
		int bestAxis = -1;
		
		for(int axis = 0; axis < 3; axis++) {
			final float start = minimum.getComponent(axis);
			final float stop  = maximum.getComponent(axis);
			
			if(abs(stop - start) < 1.0e-4F) {
				continue;
			}
			
			final float step = (stop - start) / (1024.0F / (depth + 1.0F));
			
			for(float oldSplit = 0.0F, newSplit = start + step; newSplit < stop - step; oldSplit = newSplit, newSplit += step) {
//				The following test prevents an infinite loop from occurring (it might not be necessary anymore, due to a fix earlier):
				if(equal(oldSplit, newSplit)) {
					break;
				}
				
				float maximumLX = MIN_VALUE;
				float maximumLY = MIN_VALUE;
				float maximumLZ = MIN_VALUE;
				float minimumLX = MAX_VALUE;
				float minimumLY = MAX_VALUE;
				float minimumLZ = MAX_VALUE;
				float maximumRX = MIN_VALUE;
				float maximumRY = MIN_VALUE;
				float maximumRZ = MIN_VALUE;
				float minimumRX = MAX_VALUE;
				float minimumRY = MAX_VALUE;
				float minimumRZ = MAX_VALUE;
				
				int countL = 0;
				int countR = 0;
				
				for(final BVHItem3F<T> processableBVHItem : processableBVHItems) {
					final BoundingVolume3F boundingVolume = processableBVHItem.getBoundingVolume();
					
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
			final List<T> shapes = new ArrayList<>(size);
			
			for(final BVHItem3F<T> processableBVHItem : processableBVHItems) {
				shapes.add(processableBVHItem.getShape());
			}
			
			return new LeafBVHNode3F<>(maximum, minimum, depth, shapes);
		}
		
		final List<BVHItem3F<T>> processableBVHItemsL = new ArrayList<>(sizeHalf);
		final List<BVHItem3F<T>> processableBVHItemsR = new ArrayList<>(sizeHalf);
		
		float maximumLX = MIN_VALUE;
		float maximumLY = MIN_VALUE;
		float maximumLZ = MIN_VALUE;
		float minimumLX = MAX_VALUE;
		float minimumLY = MAX_VALUE;
		float minimumLZ = MAX_VALUE;
		float maximumRX = MIN_VALUE;
		float maximumRY = MIN_VALUE;
		float maximumRZ = MIN_VALUE;
		float minimumRX = MAX_VALUE;
		float minimumRY = MAX_VALUE;
		float minimumRZ = MAX_VALUE;
		
		for(final BVHItem3F<T> processableBVHItem : processableBVHItems) {
			final BoundingVolume3F boundingVolume = processableBVHItem.getBoundingVolume();
			
			final Point3F max = boundingVolume.getMaximum();
			final Point3F mid = boundingVolume.getMidpoint();
			final Point3F min = boundingVolume.getMinimum();
			
			final float value = mid.getComponent(bestAxis);
			
			if(value < bestSplit) {
				processableBVHItemsL.add(processableBVHItem);
				
				maximumLX = max(maximumLX, max.getX());
				maximumLY = max(maximumLY, max.getY());
				maximumLZ = max(maximumLZ, max.getZ());
				minimumLX = min(minimumLX, min.getX());
				minimumLY = min(minimumLY, min.getY());
				minimumLZ = min(minimumLZ, min.getZ());
			} else {
				processableBVHItemsR.add(processableBVHItem);
				
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
		
		final BVHNode3F bVHNodeL = create(processableBVHItemsL, maximumL, minimumL, depth + 1);
		final BVHNode3F bVHNodeR = create(processableBVHItemsR, maximumR, minimumR, depth + 1);
		
		return new TreeBVHNode3F(maximum, minimum, depth, bVHNodeL, bVHNodeR);
	}
}