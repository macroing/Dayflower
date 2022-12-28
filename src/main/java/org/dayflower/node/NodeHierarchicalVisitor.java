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
package org.dayflower.node;

/**
 * A {@code NodeHierarchicalVisitor} is used for traversing the structure of a {@link Node} in a hierarchical fashion.
 * <p>
 * It uses a design pattern called Hierarchical Visitor.
 * <p>
 * This makes it possible to keep track of the current depth or skip a {@code Node}s children or siblings.
 * <p>
 * Implement it in order to perform various operations on various {@code Node}s in sequence.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public interface NodeHierarchicalVisitor {
	/**
	 * Called by a {@link Node} instance when entering it.
	 * <p>
	 * Returns {@code true} if, and only if, {@code node}s child {@code Node}s should be visited, {@code false} otherwise.
	 * <p>
	 * This method should be called by a {@code Node} instance soon after its {@code accept(NodeHierarchicalVisitor)} method has been called.
	 * <p>
	 * If {@code node} is {@code null}, a {@code NullPointerException} may be thrown. But no guarantees can be made.
	 * <p>
	 * If {@code node} could not be traversed by this {@code NodeHierarchicalVisitor}, a {@link NodeTraversalException} may be thrown. But no guarantees can be made.
	 * 
	 * @param node the {@code Node} to enter
	 * @return {@code true} if, and only if, {@code node}s child {@code Node}s should be visited, {@code false} otherwise
	 * @throws NodeTraversalException thrown if, and only if, {@code node} could not be traversed by this {@code NodeHierarchicalVisitor}
	 * @throws NullPointerException thrown if, and only if, {@code node} is {@code null}
	 */
	boolean visitEnter(final Node node);
	
	/**
	 * Called by a {@link Node} instance when leaving it.
	 * <p>
	 * Returns {@code true} if, and only if, {@code node}s sibling {@code Node}s should be visited, {@code false} otherwise
	 * <p>
	 * This method should be called by a {@code Node} instance just before its {@code accept(NodeHierarchicalVisitor)} method returns. It should be the result returned by that method.
	 * <p>
	 * If {@code node} is {@code null}, a {@code NullPointerException} may be thrown. But no guarantees can be made.
	 * <p>
	 * If {@code node} could not be traversed by this {@code NodeHierarchicalVisitor}, a {@link NodeTraversalException} may be thrown. But no guarantees can be made.
	 * 
	 * @param node the {@code Node} to leave
	 * @return {@code true} if, and only if, {@code node}s sibling {@code Node}s should be visited, {@code false} otherwise
	 * @throws NodeTraversalException thrown if, and only if, {@code node} could not be traversed by this {@code NodeHierarchicalVisitor}
	 * @throws NullPointerException thrown if, and only if, {@code node} is {@code null}
	 */
	boolean visitLeave(final Node node);
}