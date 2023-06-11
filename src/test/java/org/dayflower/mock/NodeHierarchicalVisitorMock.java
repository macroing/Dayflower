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
package org.dayflower.mock;

import java.util.function.Predicate;

import org.macroing.java.util.visitor.Node;
import org.macroing.java.util.visitor.NodeHierarchicalVisitor;

public final class NodeHierarchicalVisitorMock implements NodeHierarchicalVisitor {
	private final Predicate<Node> predicateEnter;
	private final Predicate<Node> predicateLeave;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public NodeHierarchicalVisitorMock(final Predicate<Node> predicateEnter, final Predicate<Node> predicateLeave) {
		this.predicateEnter = predicateEnter;
		this.predicateLeave = predicateLeave;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public boolean visitEnter(final Node node) {
		if(this.predicateEnter != null) {
			return this.predicateEnter.test(node);
		}
		
		throw new RuntimeException();
	}
	
	@Override
	public boolean visitLeave(final Node node) {
		if(this.predicateLeave != null) {
			return this.predicateLeave.test(node);
		}
		
		throw new RuntimeException();
	}
}