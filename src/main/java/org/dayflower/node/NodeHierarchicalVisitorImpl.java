/**
 * Copyright 2014 - 2022 J&#246;rgen Lundgren
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

import java.util.List;
import java.util.Objects;

final class NodeHierarchicalVisitorImpl <T extends Node> implements NodeHierarchicalVisitor {
	private final Class<T> clazz;
	private final List<T> list;
	private final NodeFilter nodeFilter;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	NodeHierarchicalVisitorImpl(final NodeFilter nodeFilter, final List<T> list, final Class<T> clazz) {
		this.nodeFilter = Objects.requireNonNull(nodeFilter, "nodeFilter == null");
		this.list = Objects.requireNonNull(list, "list == null");
		this.clazz = Objects.requireNonNull(clazz, "clazz == null");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public boolean visitEnter(final Node node) {
		Objects.requireNonNull(node, "node == null");
		
		try {
			if(this.nodeFilter.isAccepted(node) && this.clazz.isAssignableFrom(node.getClass())) {
				this.list.add(this.clazz.cast(node));
			}
		} catch(final RuntimeException e) {
			throw new NodeTraversalException(e);
		}
		
		return true;
	}
	
	@Override
	public boolean visitLeave(final Node node) {
		Objects.requireNonNull(node, "node == null");
		
		return true;
	}
}