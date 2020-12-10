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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.dayflower.node.Node;
import org.dayflower.node.NodeFilter;
import org.dayflower.util.ParameterArguments;

/**
 * A {@code Point2I} denotes a 2-dimensional point with two coordinates, of type {@code int}.
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class Point2I implements Node {
	private final int component1;
	private final int component2;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code Point2I} instance given the component values {@code 0} and {@code 0}.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Point2I(0, 0);
	 * }
	 * </pre>
	 */
	public Point2I() {
		this(0, 0);
	}
	
	/**
	 * Constructs a new {@code Point2I} instance given the component values {@code component1} and {@code component2}.
	 * 
	 * @param component1 the value of component 1
	 * @param component2 the value of component 2
	 */
	public Point2I(final int component1, final int component2) {
		this.component1 = component1;
		this.component2 = component2;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code String} representation of this {@code Point2I} instance.
	 * 
	 * @return a {@code String} representation of this {@code Point2I} instance
	 */
	@Override
	public String toString() {
		return String.format("new Point2I(%d, %d)", Integer.valueOf(this.component1), Integer.valueOf(this.component2));
	}
	
	/**
	 * Compares {@code object} to this {@code Point2I} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code Point2I}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code Point2I} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code Point2I}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof Point2I)) {
			return false;
		} else if(this.component1 != Point2I.class.cast(object).component1) {
			return false;
		} else if(this.component2 != Point2I.class.cast(object).component2) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns the value of component 1.
	 * 
	 * @return the value of component 1
	 */
	public int getComponent1() {
		return this.component1;
	}
	
	/**
	 * Returns the value of component 2.
	 * 
	 * @return the value of component 2
	 */
	public int getComponent2() {
		return this.component2;
	}
	
	/**
	 * Returns the value of the U-component.
	 * 
	 * @return the value of the U-component
	 */
	public int getU() {
		return this.component1;
	}
	
	/**
	 * Returns the value of the V-component.
	 * 
	 * @return the value of the V-component
	 */
	public int getV() {
		return this.component2;
	}
	
	/**
	 * Returns the value of the X-component.
	 * 
	 * @return the value of the X-component
	 */
	public int getX() {
		return this.component1;
	}
	
	/**
	 * Returns the value of the Y-component.
	 * 
	 * @return the value of the Y-component
	 */
	public int getY() {
		return this.component2;
	}
	
	/**
	 * Returns a hash code for this {@code Point2I} instance.
	 * 
	 * @return a hash code for this {@code Point2I} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(Integer.valueOf(this.component1), Integer.valueOf(this.component2));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code List} with all {@code Point2I} instances in {@code node}.
	 * <p>
	 * If {@code node} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param node a {@link Node} instance
	 * @return a {@code List} with all {@code Point2I} instances in {@code node}
	 * @throws NullPointerException thrown if, and only if, {@code node} is {@code null}
	 */
	public static List<Point2I> filterAll(final Node node) {
		return NodeFilter.filter(node, NodeFilter.any(), Point2I.class);
	}
	
	/**
	 * Returns a {@code List} with all distinct {@code Point2I} instances in {@code node}.
	 * <p>
	 * If {@code node} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param node a {@link Node} instance
	 * @return a {@code List} with all distinct {@code Point2I} instances in {@code node}
	 * @throws NullPointerException thrown if, and only if, {@code node} is {@code null}
	 */
	public static List<Point2I> filterAllDistinct(final Node node) {
		return filterAll(node).stream().distinct().collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
	}
	
	/**
	 * Returns a {@code Map} that maps distinct {@code Point2I} instances to their offsets.
	 * <p>
	 * If {@code distinctPoints} or at least one of its elements are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Point2I.mapDistinctToOffsets(distinctPoints, 1);
	 * }
	 * </pre>
	 * 
	 * @param distinctPoints a {@code List} with distinct {@code Point2I} instances
	 * @return a {@code Map} that maps distinct {@code Point2I} instances to their offsets
	 * @throws NullPointerException thrown if, and only if, {@code distinctPoints} or at least one of its elements are {@code null}
	 */
	public static Map<Point2I, Integer> mapDistinctToOffsets(final List<Point2I> distinctPoints) {
		return mapDistinctToOffsets(distinctPoints, 1);
	}
	
	/**
	 * Returns a {@code Map} that maps distinct {@code Point2I} instances to their offsets.
	 * <p>
	 * If {@code distinctPoints} or at least one of its elements are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code sizePoint} is less than {@code 1} or at least one offset is less than {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param distinctPoints a {@code List} with distinct {@code Point2I} instances
	 * @param sizePoint the size of a {@code Point2I} instance
	 * @return a {@code Map} that maps distinct {@code Point2I} instances to their offsets
	 * @throws IllegalArgumentException thrown if, and only if, {@code sizePoint} is less than {@code 1} or at least one offset is less than {@code 0}
	 * @throws NullPointerException thrown if, and only if, {@code distinctPoints} or at least one of its elements are {@code null}
	 */
	public static Map<Point2I, Integer> mapDistinctToOffsets(final List<Point2I> distinctPoints, final int sizePoint) {
		ParameterArguments.requireNonNullList(distinctPoints, "distinctPoints");
		ParameterArguments.requireRange(sizePoint, 1, Integer.MAX_VALUE, "sizePoint");
		
		final Map<Point2I, Integer> map = new LinkedHashMap<>();
		
		for(int i = 0; i < distinctPoints.size(); i++) {
			map.put(distinctPoints.get(i), Integer.valueOf(ParameterArguments.requireRangef(i * sizePoint, 0, Integer.MAX_VALUE, "(%d * %d)", Integer.valueOf(i), Integer.valueOf(sizePoint))));
		}
		
		return map;
	}
	
	/**
	 * Returns an {@code int[]} representation of {@code points}.
	 * <p>
	 * If either {@code points} or at least one of its elements are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param points a {@code List} of {@code Point2I} instances
	 * @return an {@code int[]} representation of {@code points}
	 * @throws NullPointerException thrown if, and only if, either {@code points} or at least one of its elements are {@code null}
	 */
	public static int[] toArray(final List<Point2I> points) {
		final int[] array = new int[points.size() * 2];
		
		for(int i = 0, j = 0; i < points.size(); i++, j += 2) {
			final Point2I point = points.get(i);
			
			array[j + 0] = point.component1;
			array[j + 1] = point.component2;
		}
		
		return array;
	}
}