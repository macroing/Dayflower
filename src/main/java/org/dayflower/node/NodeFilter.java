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
import java.util.List;
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
	 * If {@code node} is {@code null}, a {@code NullPointerException} may be thrown. But no guarantees can be made.
	 * 
	 * @param node the {@code Node} to accept or reject
	 * @return {@code true} if, and only if, {@code node} is accepted, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, {@code node} is {@code null}
	 */
	boolean isAccepted(final Node node);
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code List} with all {@link Node}s.
	 * <p>
	 * All {@code Node}s are found by traversing {@code node} using a simple {@link NodeHierarchicalVisitor} implementation.
	 * <p>
	 * If {@code node} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param node the {@code Node} to start traversal from
	 * @return a {@code List} with all {@code Node}s
	 * @throws NullPointerException thrown if, and only if, {@code node} is {@code null}
	 */
	static List<Node> filter(final Node node) {
		return filter(node, any());
	}
	
	/**
	 * Returns a {@code List} with the {@link Node}s that satisfies the criterion specified by {@code nodeFilter}.
	 * <p>
	 * All {@code Node}s are found by traversing {@code node} using a simple {@link NodeHierarchicalVisitor} implementation.
	 * <p>
	 * If either {@code node} or {@code nodeFilter} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param node the {@code Node} to start traversal from
	 * @param nodeFilter the {@code NodeFilter} to accept or reject {@code Node}s
	 * @return a {@code List} with the {@code Node}s that satisfies the criterion specified by {@code nodeFilter}
	 * @throws NullPointerException thrown if, and only if, either {@code node} or {@code nodeFilter} are {@code null}
	 */
	static List<Node> filter(final Node node, final NodeFilter nodeFilter) {
		return filter(node, nodeFilter, Node.class);
	}
	
	/**
	 * Returns a {@code List} with the {@link Node}s that satisfies the criterion specified by {@code nodeFilter}, but only if they are assignment compatible to {@code clazz}.
	 * <p>
	 * The {@code List} returned will be cast to the {@code Class} specified by {@code clazz}.
	 * <p>
	 * All {@code Node}s are found by traversing {@code node} using a simple {@link NodeHierarchicalVisitor} implementation.
	 * <p>
	 * If either {@code node}, {@code nodeFilter} or {@code clazz} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param <T> the type the {@code List} should be cast to
	 * @param node the {@code Node} to start traversal from
	 * @param nodeFilter the {@code NodeFilter} to accept or reject {@code Node}s
	 * @param clazz the {@code Class} that is of the type {@code T}
	 * @return a {@code List} with the {@code Node}s that satisfies the criterion specified by {@code nodeFilter}, but only if they are assignment compatible to {@code clazz}
	 * @throws NullPointerException thrown if, and only if, either {@code node}, {@code nodeFilter} or {@code clazz} are {@code null}
	 */
	static <T extends Node> List<T> filter(final Node node, final NodeFilter nodeFilter, final Class<T> clazz) {
		final List<T> nodes = new ArrayList<>();
		
		node.accept(new NodeHierarchicalVisitorImpl<>(Objects.requireNonNull(nodeFilter, "nodeFilter == null"), nodes, Objects.requireNonNull(clazz, "clazz == null")));
		
		return nodes;
	}
	
	/**
	 * Returns a {@link NodeFilter} that accepts all {@link Node}s that are accepted by all of the aggregated {@code NodeFilter}s.
	 * <p>
	 * This means that this {@code NodeFilter} does not on its own contain any filtering logic. The logic that determines whether a {@code Node} is accepted or rejected, is fully up to the aggregated {@code NodeFilter}s.
	 * <p>
	 * When {@code isAccepted(Node)} is called, it iterates over the aggregated {@code NodeFilter}s until either one of them rejects the {@code Node}, in which case {@code false} is returned, or all of them accepts, in which case {@code true} is
	 * returned.
	 * <p>
	 * If either {@code nodeFilters}, or at least one of its elements are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * The {@code NodeFilter} returned by this method will throw a {@code NullPointerException} if, and only if, the {@code Node} to accept or reject is {@code null}.
	 * 
	 * @param nodeFilters the aggregated {@code NodeFilter}s that make up the filtering logic
	 * @return a {@code NodeFilter} that accepts all {@code Node}s that are accepted by all of the aggregated {@code NodeFilter}s
	 * @throws NullPointerException thrown if, and only if, either {@code nodeFilters}, or at least one of its elements are {@code null}
	 */
	static NodeFilter and(final NodeFilter... nodeFilters) {
		ParameterArguments.requireNonNullArray(nodeFilters, "nodeFilters");
		
		return node -> {
			Objects.requireNonNull(node, "node == null");
			
			return Arrays.stream(nodeFilters).allMatch(nodeFilter -> nodeFilter.isAccepted(node));
		};
	}
	
	/**
	 * Returns a {@link NodeFilter} that accepts any {@link Node}.
	 * <p>
	 * The {@code NodeFilter} returned by this method will throw a {@code NullPointerException} if, and only if, the {@code Node} to accept or reject is {@code null}.
	 * 
	 * @return a {@code NodeFilter} that accepts any {@code Node}
	 */
	static NodeFilter any() {
		return node -> {
			Objects.requireNonNull(node, "node == null");
			
			return true;
		};
	}
	
	/**
	 * Returns a {@link NodeFilter} that accepts all {@link Node}s that are accepted by at least one of the aggregated {@code NodeFilter}s.
	 * <p>
	 * This means that this {@code NodeFilter} does not on its own contain any filtering logic. The logic that determines whether a {@code Node} is accepted or rejected, is fully up to the aggregated {@code NodeFilter}s.
	 * <p>
	 * When {@code isAccepted(Node)} is called, it iterates over the aggregated {@code NodeFilter}s until either one of them accepts the {@code Node}, in which case {@code true} is returned, or none of them does, in which case {@code false} is
	 * returned.
	 * <p>
	 * If either {@code nodeFilters}, or at least one of its elements are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * The {@code NodeFilter} returned by this method will throw a {@code NullPointerException} if, and only if, the {@code Node} to accept or reject is {@code null}.
	 * 
	 * @param nodeFilters the aggregated {@code NodeFilter}s that make up the filtering logic
	 * @return a {@code NodeFilter} that accepts all {@code Node}s that are accepted by at least one of the aggregated {@code NodeFilter}s
	 * @throws NullPointerException thrown if, and only if, either {@code nodeFilters}, or at least one of its elements are {@code null}
	 */
	static NodeFilter or(final NodeFilter... nodeFilters) {
		ParameterArguments.requireNonNullArray(nodeFilters, "nodeFilters");
		
		return node -> {
			Objects.requireNonNull(node, "node == null");
			
			return Arrays.stream(nodeFilters).anyMatch(nodeFilter -> nodeFilter.isAccepted(Objects.requireNonNull(node, "node == null")));
		};
	}
	
	/**
	 * Returns a {@link NodeFilter} that accepts all {@link Node}s that have a {@code toString()} method that matches the given Regex.
	 * <p>
	 * If {@code regex} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * The {@code NodeFilter} returned by this method will throw a {@code NullPointerException} if, and only if, the {@code Node} to accept or reject is {@code null}.
	 * 
	 * @param regex a {@code String} representing the Regex to be used in the filtering process
	 * @return a {@code NodeFilter} that accepts all {@code Node}s that have a {@code toString()} method that matches the given Regex
	 * @throws NullPointerException thrown if, and only if, {@code regex} is {@code null}
	 */
	static NodeFilter regex(final String regex) {
		Objects.requireNonNull(regex, "regex == null");
		
		return node -> node.toString().matches(regex);
	}
}