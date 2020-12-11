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
package org.dayflower.node;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.dayflower.util.ParameterArguments;

/**
 * A filter that accepts or rejects a {@link Node}, based on some criterion.
 * <p>
 * You may use a concrete implementation of this interface in combination with the static methods provided.
 * <p>
 * Some of the static methods take an instance of {@code Node} as their first parameter argument and an instance of {@code NodeFilter} as their second and returns a {@code List} with the {@code Node}s that satisfies said criterion.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
@FunctionalInterface
public interface NodeFilter {
	/**
	 * Returns {@code true} if, and only if, {@code node} is accepted, {@code false} otherwise.
	 * <p>
	 * If {@code node} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param node the {@code Node} to accept or reject
	 * @return {@code true} if, and only if, {@code node} is accepted, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, {@code node} is {@code null}
	 */
	boolean isAccepted(final Node node);
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code List} with all {@link Node} instances in {@code node}.
	 * <p>
	 * If {@code node} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * All {@code Node} instances are found by traversing {@code node} using a simple {@link NodeHierarchicalVisitor} implementation.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * NodeFilter.filter(node, NodeFilter.any());
	 * }
	 * </pre>
	 * 
	 * @param node the {@code Node} instance to start traversal from
	 * @return a {@code List} with all {@code Node} instances in {@code node}
	 * @throws NullPointerException thrown if, and only if, {@code node} is {@code null}
	 */
	static List<Node> filter(final Node node) {
		return filter(node, any());
	}
	
	/**
	 * Returns a {@code List} with the {@link Node} instances in {@code node} that satisfies the criterion specified by {@code nodeFilter}.
	 * <p>
	 * If either {@code node} or {@code nodeFilter} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * All {@code Node} instances are found by traversing {@code node} using a simple {@link NodeHierarchicalVisitor} implementation.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * NodeFilter.filter(node, nodeFilter, Node.class);
	 * }
	 * </pre>
	 * 
	 * @param node the {@code Node} instance to start traversal from
	 * @param nodeFilter the {@code NodeFilter} instance that accepts or rejects {@code Node} instances
	 * @return a {@code List} with the {@code Node} instances in {@code node} that satisfies the criterion specified by {@code nodeFilter}
	 * @throws NullPointerException thrown if, and only if, either {@code node} or {@code nodeFilter} are {@code null}
	 */
	static List<Node> filter(final Node node, final NodeFilter nodeFilter) {
		return filter(node, nodeFilter, Node.class);
	}
	
	/**
	 * Returns a {@code List} with the {@link Node} instances in {@code node} that satisfies the criterion specified by {@code nodeFilter}, but only if they are assignment compatible to {@code clazz}.
	 * <p>
	 * If either {@code node}, {@code nodeFilter} or {@code clazz} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * The {@code List} returned will be cast to the {@code Class} specified by {@code clazz}.
	 * <p>
	 * All {@code Node} instances are found by traversing {@code node} using a simple {@link NodeHierarchicalVisitor} implementation.
	 * 
	 * @param <T> the type the {@code List} should be cast to
	 * @param node the {@code Node} instance to start traversal from
	 * @param nodeFilter the {@code NodeFilter} instance that accepts or rejects {@code Node} instances
	 * @param clazz the {@code Class} that is of the type {@code T}
	 * @return a {@code List} with the {@code Node} instances in {@code node} that satisfies the criterion specified by {@code nodeFilter}, but only if they are assignment compatible to {@code clazz}
	 * @throws NullPointerException thrown if, and only if, either {@code node}, {@code nodeFilter} or {@code clazz} are {@code null}
	 */
	static <T extends Node> List<T> filter(final Node node, final NodeFilter nodeFilter, final Class<T> clazz) {
		final List<T> nodes = new ArrayList<>();
		
		node.accept(new NodeHierarchicalVisitorImpl<>(Objects.requireNonNull(nodeFilter, "nodeFilter == null"), nodes, Objects.requireNonNull(clazz, "clazz == null")));
		
		return nodes;
	}
	
	/**
	 * Returns a {@code List} with all {@link Node} instances in {@code node} that are assignment compatible to {@code clazz}.
	 * <p>
	 * If either {@code node} or {@code clazz} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * The {@code List} returned will be cast to the {@code Class} specified by {@code clazz}.
	 * <p>
	 * All {@code Node} instances are found by traversing {@code node} using a simple {@link NodeHierarchicalVisitor} implementation.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * NodeFilter.filter(node, NodeFilter.any(), clazz);
	 * }
	 * </pre>
	 * 
	 * @param <T> the type the {@code List} should be cast to
	 * @param node the {@code Node} instance to start traversal from
	 * @param clazz the {@code Class} that is of the type {@code T}
	 * @return a {@code List} with all {@code Node} instances in {@code node} that are assignment compatible to {@code clazz}
	 * @throws NullPointerException thrown if, and only if, either {@code node} or {@code clazz} are {@code null}
	 */
	static <T extends Node> List<T> filterAll(final Node node, final Class<T> clazz) {
		return filter(node, any(), clazz);
	}
	
	/**
	 * Returns a {@code List} with all distinct {@link Node} instances in {@code node} that are assignment compatible to {@code clazz}.
	 * <p>
	 * If either {@code node} or {@code clazz} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * The {@code List} returned will be cast to the {@code Class} specified by {@code clazz}.
	 * <p>
	 * All {@code Node} instances are found by traversing {@code node} using a simple {@link NodeHierarchicalVisitor} implementation.
	 * 
	 * @param <T> the type the {@code List} should be cast to
	 * @param node the {@code Node} instance to start traversal from
	 * @param clazz the {@code Class} that is of the type {@code T}
	 * @return a {@code List} with all distinct {@code Node} instances in {@code node} that are assignment compatible to {@code clazz}
	 * @throws NullPointerException thrown if, and only if, either {@code node} or {@code clazz} are {@code null}
	 */
	static <T extends Node> List<T> filterAllDistinct(final Node node, final Class<T> clazz) {
		return filterAll(node, clazz).stream().distinct().collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
	}
	
	/**
	 * Returns a {@code Map} that maps distinct {@code Node} instances to their offsets.
	 * <p>
	 * If {@code distinctNodes} or at least one of its elements are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * NodeFilter.mapDistinctToOffsets(distinctNodes, 1);
	 * }
	 * </pre>
	 * 
	 * @param <T> the generic type
	 * @param distinctNodes a {@code List} with distinct {@code Node} instances
	 * @return a {@code Map} that maps distinct {@code Node} instances to their offsets
	 * @throws NullPointerException thrown if, and only if, {@code distinctNodes} or at least one of its elements are {@code null}
	 */
	static <T extends Node> Map<T, Integer> mapDistinctToOffsets(final List<T> distinctNodes) {
		return mapDistinctToOffsets(distinctNodes, 1);
	}
	
	/**
	 * Returns a {@code Map} that maps distinct {@code Node} instances to their offsets.
	 * <p>
	 * If {@code distinctNodes} or at least one of its elements are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code sizeNode} is less than {@code 1} or at least one offset is less than {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param <T> the generic type
	 * @param distinctNodes a {@code List} with distinct {@code Node} instances
	 * @param sizeNode the size of a {@code Node} instance
	 * @return a {@code Map} that maps distinct {@code Node} instances to their offsets
	 * @throws IllegalArgumentException thrown if, and only if, {@code sizeNode} is less than {@code 1} or at least one offset is less than {@code 0}
	 * @throws NullPointerException thrown if, and only if, {@code distinctNodes} or at least one of its elements are {@code null}
	 */
	static <T extends Node> Map<T, Integer> mapDistinctToOffsets(final List<T> distinctNodes, final int sizeNode) {
		ParameterArguments.requireNonNullList(distinctNodes, "distinctNodes");
		ParameterArguments.requireRange(sizeNode, 1, Integer.MAX_VALUE, "sizeNode");
		
		final Map<T, Integer> map = new LinkedHashMap<>();
		
		for(int i = 0; i < distinctNodes.size(); i++) {
			map.put(distinctNodes.get(i), Integer.valueOf(ParameterArguments.requireRangef(i * sizeNode, 0, Integer.MAX_VALUE, "(%d * %d)", Integer.valueOf(i), Integer.valueOf(sizeNode))));
		}
		
		return map;
	}
	
	/**
	 * Returns a {@link NodeFilter} instance that accepts all {@link Node} instances that are accepted by all of the aggregated {@code NodeFilter} instances.
	 * <p>
	 * If either {@code nodeFilters} or at least one of its elements are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * The returned {@code NodeFilter} instance does not contain any filtering logic by itself. The filtering logic that determines whether a {@code Node} instance is accepted or rejected, is fully up to the aggregated {@code NodeFilter} instances.
	 * <p>
	 * When {@code isAccepted(Node)} is called, it iterates over the aggregated {@code NodeFilter} instances until either one of them rejects the {@code Node} instance, in which case {@code false} is returned, or all of them accepts, in which case
	 * {@code true} is returned.
	 * 
	 * @param nodeFilters the aggregated {@code NodeFilter} instances that make up the filtering logic
	 * @return a {@code NodeFilter} instance that accepts all {@code Node} instances that are accepted by all of the aggregated {@code NodeFilter} instances
	 * @throws NullPointerException thrown if, and only if, either {@code nodeFilters} or at least one of its elements are {@code null}
	 */
	static NodeFilter and(final NodeFilter... nodeFilters) {
		ParameterArguments.requireNonNullArray(nodeFilters, "nodeFilters");
		
		return node -> {
			Objects.requireNonNull(node, "node == null");
			
			return Arrays.stream(nodeFilters).allMatch(nodeFilter -> nodeFilter.isAccepted(node));
		};
	}
	
	/**
	 * Returns a {@link NodeFilter} instance that accepts any {@link Node} instance.
	 * 
	 * @return a {@code NodeFilter} instance that accepts any {@code Node} instance
	 */
	static NodeFilter any() {
		return node -> {
			Objects.requireNonNull(node, "node == null");
			
			return true;
		};
	}
	
	/**
	 * Returns a {@link NodeFilter} instance that accepts all {@link Node} instances that are accepted by at least one of the aggregated {@code NodeFilter} instances.
	 * <p>
	 * If either {@code nodeFilters} or at least one of its elements are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * The returned {@code NodeFilter} instance does not contain any filtering logic by itself. The filtering logic that determines whether a {@code Node} instance is accepted or rejected, is fully up to the aggregated {@code NodeFilter} instances.
	 * <p>
	 * When {@code isAccepted(Node)} is called, it iterates over the aggregated {@code NodeFilter} instances until either one of them accepts the {@code Node} instance, in which case {@code true} is returned, or none of them does, in which case
	 * {@code false} is returned.
	 * 
	 * @param nodeFilters the aggregated {@code NodeFilter} instances that make up the filtering logic
	 * @return a {@code NodeFilter} instance that accepts all {@code Node} instances that are accepted by at least one of the aggregated {@code NodeFilter} instances
	 * @throws NullPointerException thrown if, and only if, either {@code nodeFilters} or at least one of its elements are {@code null}
	 */
	static NodeFilter or(final NodeFilter... nodeFilters) {
		ParameterArguments.requireNonNullArray(nodeFilters, "nodeFilters");
		
		return node -> {
			Objects.requireNonNull(node, "node == null");
			
			return Arrays.stream(nodeFilters).anyMatch(nodeFilter -> nodeFilter.isAccepted(Objects.requireNonNull(node, "node == null")));
		};
	}
	
	/**
	 * Returns a {@link NodeFilter} instance that accepts all {@link Node} instances that have a {@code toString()} method that matches the given Regex.
	 * <p>
	 * If {@code regex} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param regex a {@code String} representing the Regex to be used in the filtering process
	 * @return a {@code NodeFilter} instance that accepts all {@code Node} instances that have a {@code toString()} method that matches the given Regex
	 * @throws NullPointerException thrown if, and only if, {@code regex} is {@code null}
	 */
	static NodeFilter regex(final String regex) {
		Objects.requireNonNull(regex, "regex == null");
		
		return node -> node.toString().matches(regex);
	}
}