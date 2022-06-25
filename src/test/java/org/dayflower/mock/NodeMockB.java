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
package org.dayflower.mock;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.dayflower.node.Node;
import org.dayflower.node.NodeHierarchicalVisitor;
import org.dayflower.node.NodeTraversalException;
import org.dayflower.node.NodeVisitor;
import org.dayflower.utility.ParameterArguments;

public final class NodeMockB implements Node {
	private final List<Node> nodes;
	private final String string;
	private final boolean isThrowingRuntimeException;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public NodeMockB(final String string) {
		this(string, new ArrayList<>());
	}
	
	public NodeMockB(final String string, final List<Node> nodes) {
		this(string, nodes, false);
	}
	
	public NodeMockB(final String string, final List<Node> nodes, final boolean isThrowingRuntimeException) {
		this.string = Objects.requireNonNull(string, "string == null");
		this.nodes = new ArrayList<>(ParameterArguments.requireNonNullList(nodes, "nodes"));
		this.isThrowingRuntimeException = isThrowingRuntimeException;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public String toString() {
		return this.string;
	}
	
	@Override
	public boolean accept(final NodeHierarchicalVisitor nodeHierarchicalVisitor) {
		Objects.requireNonNull(nodeHierarchicalVisitor, "nodeHierarchicalVisitor == null");
		
		try {
			if(this.isThrowingRuntimeException) {
				throw new RuntimeException();
			}
			
			if(nodeHierarchicalVisitor.visitEnter(this)) {
				for(final Node node : this.nodes) {
					if(!node.accept(nodeHierarchicalVisitor)) {
						return nodeHierarchicalVisitor.visitLeave(this);
					}
				}
			}
			
			return nodeHierarchicalVisitor.visitLeave(this);
		} catch(final RuntimeException e) {
			throw new NodeTraversalException(e);
		}
	}
	
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof NodeMockB)) {
			return false;
		} else if(!Objects.equals(this.nodes, NodeMockB.class.cast(object).nodes)) {
			return false;
		} else if(!Objects.equals(this.string, NodeMockB.class.cast(object).string)) {
			return false;
		} else if(this.isThrowingRuntimeException != NodeMockB.class.cast(object).isThrowingRuntimeException) {
			return false;
		} else {
			return true;
		}
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(this.nodes, this.string, Boolean.valueOf(this.isThrowingRuntimeException));
	}
	
	@Override
	public void accept(final NodeVisitor nodeVisitor) {
		Objects.requireNonNull(nodeVisitor, "nodeVisitor == null");
		
		try {
			if(this.isThrowingRuntimeException) {
				throw new RuntimeException();
			}
			
			nodeVisitor.visit(this);
		} catch(final RuntimeException e) {
			throw new NodeTraversalException(e);
		}
	}
}