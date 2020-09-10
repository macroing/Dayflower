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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

//TODO: Add Javadocs!
public final class BoundingVolumeHierarchy3F {
	private final Node node;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public BoundingVolumeHierarchy3F(final List<Triangle3F> triangles) {
		this.node = doCreateNode(Objects.requireNonNull(triangles, "triangles == null"));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof BoundingVolumeHierarchy3F)) {
			return false;
		} else if(!Objects.equals(this.node, BoundingVolumeHierarchy3F.class.cast(object).node)) {
			return false;
		} else {
			return true;
		}
	}
	
//	TODO: Add Javadocs!
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
					final Point3F midpoint = processableLeafNode.getMidpoint();
					
					final float value = midpoint.getComponent(axis);
					
					if(value < split) {
						maximumL = Point3F.maximum(maximumL, processableLeafNode.getMaximum());
						minimumL = Point3F.minimum(minimumL, processableLeafNode.getMinimum());
						
						countL++;
					} else {
						maximumR = Point3F.maximum(maximumR, processableLeafNode.getMaximum());
						minimumR = Point3F.minimum(minimumR, processableLeafNode.getMinimum());
						
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
			final Point3F midpoint = processableLeafNode.getMidpoint();
			
			final float value = midpoint.getComponent(bestAxis);
			
			if(value < bestSplit) {
				leafNodesL.add(processableLeafNode);
				
				maximumL = Point3F.maximum(maximumL, processableLeafNode.getMaximum());
				minimumL = Point3F.minimum(minimumL, processableLeafNode.getMinimum());
			} else {
				leafNodesR.add(processableLeafNode);
				
				maximumR = Point3F.maximum(maximumR, processableLeafNode.getMaximum());
				minimumR = Point3F.minimum(minimumR, processableLeafNode.getMinimum());
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
			
			maximum = Point3F.maximum(maximum, leafNode.getMaximum());
			minimum = Point3F.minimum(minimum, leafNode.getMinimum());
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
		public boolean equals(final Object object) {
			if(object == this) {
				return true;
			} else if(!(object instanceof LeafNode)) {
				return false;
			} else if(!Objects.equals(getMaximum(), LeafNode.class.cast(object).getMaximum())) {
				return false;
			} else if(!Objects.equals(getMidpoint(), LeafNode.class.cast(object).getMidpoint())) {
				return false;
			} else if(!Objects.equals(getMinimum(), LeafNode.class.cast(object).getMinimum())) {
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
		public int hashCode() {
			return Objects.hash(getMaximum(), getMidpoint(), getMinimum(), Integer.valueOf(getDepth()), this.triangles);
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static abstract class Node {
		private final Point3F maximum;
		private final Point3F midpoint;
		private final Point3F minimum;
		private final int depth;
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		protected Node(final Point3F maximum, final Point3F minimum, final int depth) {
			this.maximum = Objects.requireNonNull(maximum, "maximum == null");
			this.minimum = Objects.requireNonNull(minimum, "minimum == null");
			this.depth = depth;
			this.midpoint = Point3F.midpoint(maximum, minimum);
		}
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		public final Point3F getMaximum() {
			return this.maximum;
		}
		
		public final Point3F getMidpoint() {
			return this.midpoint;
		}
		
		public final Point3F getMinimum() {
			return this.minimum;
		}
		
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
		public boolean equals(final Object object) {
			if(object == this) {
				return true;
			} else if(!(object instanceof TreeNode)) {
				return false;
			} else if(!Objects.equals(getMaximum(), TreeNode.class.cast(object).getMaximum())) {
				return false;
			} else if(!Objects.equals(getMidpoint(), TreeNode.class.cast(object).getMidpoint())) {
				return false;
			} else if(!Objects.equals(getMinimum(), TreeNode.class.cast(object).getMinimum())) {
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
		public int hashCode() {
			return Objects.hash(getMaximum(), getMidpoint(), getMinimum(), Integer.valueOf(getDepth()), this.nodeL, this.nodeR);
		}
	}
}