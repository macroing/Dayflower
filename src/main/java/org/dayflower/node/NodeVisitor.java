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

/**
 * A {@code NodeVisitor} is used for traversing the structure of a {@link Node}.
 * <p>
 * It uses a design pattern called Visitor.
 * <p>
 * Implement it in order to perform various operations on various {@code Node}s in sequence.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
@FunctionalInterface
public interface NodeVisitor {
	/**
	 * Called by a concrete {@link Node} implementation when visiting it.
	 * <p>
	 * This method should be called by a {@code Node} instance soon after its {@code accept(NodeVisitor)} method has been called, with this {@code NodeVisitor} as its parameter argument.
	 * <p>
	 * If {@code node} is {@code null}, a {@code NullPointerException} may be thrown. But no guarantees can be made.
	 * <p>
	 * If {@code node} could not be traversed by this {@code NodeVisitor}, a {@link NodeTraversalException} may be thrown. But no guarantees can be made.
	 * 
	 * @param node the {@code Node} currently being visited
	 * @throws NodeTraversalException thrown if, and only if, {@code node} could not be traversed by this {@code NodeVisitor}
	 * @throws NullPointerException thrown if, and only if, {@code node} is {@code null}
	 */
	void visit(final Node node);
}