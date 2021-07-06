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
package org.dayflower.node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * A {@code NodeCache} stores {@link Node} instances in a cache.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class NodeCache {
	private final Map<String, List<Node>> cache;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new empty {@code NodeCache} instance.
	 */
	public NodeCache() {
		this.cache = new HashMap<>();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code List} with all {@link Node} instances in this {@code NodeCache} instance that are assignment compatible to {@code clazz}.
	 * <p>
	 * If {@code clazz} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param <T> the type the {@code List} should be cast to
	 * @param clazz the {@code Class} that is of the type {@code T}
	 * @return a {@code List} with all {@code Node} instances in this {@code NodeCache} instance that are assignment compatible to {@code clazz}
	 * @throws NullPointerException thrown if, and only if, {@code clazz} is {@code null}
	 */
	public <T extends Node> List<T> getAll(final Class<T> clazz) {
		Objects.requireNonNull(clazz, "clazz == null");
		
		final String key = clazz.getName();
		
		if(this.cache.containsKey(key)) {
			return this.cache.get(key).stream().filter(node -> clazz.isAssignableFrom(node.getClass())).map(node -> clazz.cast(node)).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
		}
		
		return new ArrayList<>();
	}
	
	/**
	 * Returns a {@code List} with all distinct {@link Node} instances in this {@code NodeCache} instance that are assignment compatible to {@code clazz}.
	 * <p>
	 * If {@code clazz} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param <T> the type the {@code List} should be cast to
	 * @param clazz the {@code Class} that is of the type {@code T}
	 * @return a {@code List} with all distinct {@code Node} instances in this {@code NodeCache} instance that are assignment compatible to {@code clazz}
	 * @throws NullPointerException thrown if, and only if, {@code clazz} is {@code null}
	 */
	public <T extends Node> List<T> getAllDistinct(final Class<T> clazz) {
		Objects.requireNonNull(clazz, "clazz == null");
		
		final String key = clazz.getName();
		
		if(this.cache.containsKey(key)) {
			return this.cache.get(key).stream().filter(node -> clazz.isAssignableFrom(node.getClass())).map(node -> clazz.cast(node)).distinct().collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
		}
		
		return new ArrayList<>();
	}
	
	/**
	 * Adds {@code node} and its direct or indirect children to this {@code NodeCache} instance.
	 * <p>
	 * If {@code node} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * nodeCache.add(node, NodeFilter.any());
	 * }
	 * </pre>
	 * 
	 * @param node the root {@link Node} to add
	 * @throws NullPointerException thrown if, and only if, {@code node} is {@code null}
	 */
	public void add(final Node node) {
		add(node, NodeFilter.any());
	}
	
	/**
	 * Adds {@code node} and its direct or indirect children to this {@code NodeCache} instance.
	 * <p>
	 * If either {@code node} or {@code nodeFilter} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param node the root {@link Node} to add
	 * @param nodeFilter a {@link NodeFilter} that determines whether a {@code Node} instance will be added or not
	 * @throws NullPointerException thrown if, and only if, either {@code node} or {@code nodeFilter} are {@code null}
	 */
	public void add(final Node node, final NodeFilter nodeFilter) {
		Objects.requireNonNull(node, "node == null");
		Objects.requireNonNull(nodeFilter, "nodeFilter == null");
		
		node.accept(new NodeHierarchicalVisitorImpl(this.cache, nodeFilter));
	}
	
	/**
	 * Clears this {@code NodeCache} instance.
	 */
	public void clear() {
		this.cache.clear();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static final class NodeHierarchicalVisitorImpl implements NodeHierarchicalVisitor {
		private final Map<String, List<Node>> cache;
		private final NodeFilter nodeFilter;
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		public NodeHierarchicalVisitorImpl(final Map<String, List<Node>> cache, final NodeFilter nodeFilter) {
			this.cache = Objects.requireNonNull(cache, "cache == null");
			this.nodeFilter = Objects.requireNonNull(nodeFilter, "nodeFilter == null");
		}
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		@Override
		public boolean visitEnter(final Node node) {
			Objects.requireNonNull(node, "node == null");
			
			if(this.nodeFilter.isAccepted(node)) {
				this.cache.computeIfAbsent(node.getClass().getName(), key -> new ArrayList<>()).add(node);
			}
			
			return true;
		}
		
		@Override
		public boolean visitLeave(final Node node) {
			Objects.requireNonNull(node, "node == null");
			
			return true;
		}
	}
}