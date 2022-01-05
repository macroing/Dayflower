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

import java.util.Objects;

/**
 * A {@code Node} represents a node in a graph.
 * <p>
 * A tree is a graph, so it may represent the root of a tree, a node corresponding to a proper subtree of the same tree, or simply a leaf node therein.
 * <p>
 * In this API, you can traverse a {@code Node} in at least two different ways. The first using an implementation of the interface {@link NodeVisitor}, which uses a design pattern called Visitor. The second using an implementation of the interface
 * {@link NodeHierarchicalVisitor}, which uses a design pattern called Hierarchical Visitor.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public interface Node {
	/**
	 * Accepts a {@link NodeHierarchicalVisitor}.
	 * <p>
	 * Returns the result of {@code nodeHierarchicalVisitor.visitLeave(this)}.
	 * <p>
	 * If {@code nodeHierarchicalVisitor} is {@code null}, a {@code NullPointerException} may be thrown. But no guarantees can be made for all implementations.
	 * <p>
	 * If a {@code RuntimeException} is thrown by the current {@code NodeHierarchicalVisitor}, a {@code NodeTraversalException} may be thrown with the {@code RuntimeException} wrapped. But no guarantees can be made for all implementations.
	 * <p>
	 * To implement this method, consider the following example:
	 * <pre>
	 * <code>
	 * if(nodeHierarchicalVisitor.visitEnter(this)) {
	 *     for(Node childNode : childNodes) {
	 *         if(!childNode.accept(nodeHierarchicalVisitor)) {
	 *             break;
	 *         }
	 *     }
	 * }
	 * 
	 * return nodeHierarchicalVisitor.visitLeave(this);
	 * </code>
	 * </pre>
	 * The default implementation will:
	 * <ul>
	 * <li>throw a {@code NullPointerException} if {@code nodeHierarchicalVisitor} is {@code null}.</li>
	 * <li>throw a {@code NodeTraversalException} if {@code nodeHierarchicalVisitor} throws a {@code RuntimeException}.</li>
	 * <li>not traverse any child {@code Node}s, because it does not have any.</li>
	 * </ul>
	 * 
	 * @param nodeHierarchicalVisitor the {@code NodeHierarchicalVisitor} to accept
	 * @return the result of {@code nodeHierarchicalVisitor.visitLeave(this)}
	 * @throws NodeTraversalException thrown if, and only if, a {@code RuntimeException} is thrown by the current {@code NodeHierarchicalVisitor}
	 * @throws NullPointerException thrown if, and only if, {@code nodeHierarchicalVisitor} is {@code null}
	 */
	default boolean accept(final NodeHierarchicalVisitor nodeHierarchicalVisitor) {
		Objects.requireNonNull(nodeHierarchicalVisitor, "nodeHierarchicalVisitor == null");
		
		try {
			if(nodeHierarchicalVisitor.visitEnter(this)) {
//				Do nothing.
			}
			
			return nodeHierarchicalVisitor.visitLeave(this);
		} catch(final RuntimeException e) {
			throw new NodeTraversalException(e);
		}
	}
	
	/**
	 * Accepts a {@link NodeVisitor}.
	 * <p>
	 * If {@code nodeVisitor} is {@code null}, a {@code NullPointerException} may be thrown. But no guarantees can be made for all implementations.
	 * <p>
	 * If a {@code RuntimeException} is thrown by the current {@code NodeVisitor}, a {@code NodeTraversalException} may be thrown with the {@code RuntimeException} wrapped. But no guarantees can be made for all implementations.
	 * <p>
	 * To implement this method, consider the following example:
	 * <pre>
	 * <code>
	 * nodeVisitor.accept(this);
	 * </code>
	 * </pre>
	 * The default implementation will:
	 * <ul>
	 * <li>throw a {@code NullPointerException} if {@code nodeVisitor} is {@code null}.</li>
	 * <li>throw a {@code NodeTraversalException} if {@code nodeVisitor} throws a {@code RuntimeException}.</li>
	 * </ul>
	 * 
	 * @param nodeVisitor the {@code NodeVisitor} to accept
	 * @throws NodeTraversalException thrown if, and only if, a {@code RuntimeException} is thrown by the current {@code NodeVisitor}
	 * @throws NullPointerException thrown if, and only if, {@code nodeVisitor} is {@code null}
	 */
	default void accept(final NodeVisitor nodeVisitor) {
		Objects.requireNonNull(nodeVisitor, "nodeVisitor == null");
		
		try {
			nodeVisitor.visit(this);
		} catch(final RuntimeException e) {
			throw new NodeTraversalException(e);
		}
	}
}