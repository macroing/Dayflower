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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * An {@code AbstractReflectiveNodeHierarchicalVisitor} is used for traversing the structure of a {@link Node} in a hierarchical fashion. 
 * <p>
 * This abstract class delegates method calls for {@code visitEnter(Node)} and {@code visitLeave(Node)} to overloaded methods with the same name.
 * <p>
 * The delegation is completely driven by the Java Reflection API. This means that you can create a subclass containing any number of overloaded methods, provided that it has exactly one parameter argument and that parameter argument is assignment
 * compatible to the interface {@code Node}.
 * <p>
 * The names of the overloaded methods must be either {@code visitEnter} or {@code visitLeave}.
 * <p>
 * The return type does not matter. But if being able to skip entering child or sibling {@code Node}s are important, the return type has to be either {@code Boolean} or {@code boolean} and the returned value has to be either {@code Boolean.FALSE} or
 * {@code false}. Any other return type and returned value will be interpreted as {@code boolean} and {@code true}, respectively.
 * <p>
 * Any access modifier may be used, so private methods are allowed.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public abstract class AbstractReflectiveNodeHierarchicalVisitor implements NodeHierarchicalVisitor {
	/**
	 * Constructs a new {@code AbstractReflectiveNodeHierarchicalVisitor} instance.
	 */
	protected AbstractReflectiveNodeHierarchicalVisitor() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Called by a {@link Node} instance when entering it.
	 * <p>
	 * Returns {@code true} if, and only if, {@code node}s child {@code Node}s should be visited, {@code false} otherwise.
	 * <p>
	 * This method should be called by a {@code Node} instance soon after its {@code accept(NodeHierarchicalVisitor)} method has been called.
	 * <p>
	 * If {@code node} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code node} could not be traversed by this {@code AbstractReflectiveNodeHierarchicalVisitor}, a {@link NodeTraversalException} may be thrown. But no guarantees can be made.
	 * <p>
	 * The implementation provided by this class will attempt to delegate all calls to overloaded methods with a single parameter argument that is assignment compatible to {@code Node}. If an overloaded method can be found for {@code node}, that method
	 * will be called. If a method was called and its return type is either {@code Boolean} or {@code boolean}, its returned value will be returned by this method. In any other case, {@code true} will be returned. The overloaded method may have any
	 * access modifier assigned to it, so private methods are allowed.
	 * 
	 * @param node the {@code Node} to enter
	 * @return {@code true} if, and only if, {@code node}s child {@code Node}s should be visited, {@code false} otherwise
	 * @throws NodeTraversalException thrown if, and only if, {@code node} could not be traversed by this {@code AbstractReflectiveNodeHierarchicalVisitor}
	 * @throws NullPointerException thrown if, and only if, {@code node} is {@code null}
	 */
	@Override
	public final boolean visitEnter(final Node node) {
		return doVisit(Objects.requireNonNull(node, "node == null"), "visitEnter");
	}
	
	/**
	 * Called by a {@link Node} instance when leaving it.
	 * <p>
	 * Returns {@code true} if, and only if, {@code node}s sibling {@code Node}s should be visited, {@code false} otherwise.
	 * <p>
	 * This method should be called by a {@code Node} instance just before its {@code accept(NodeHierarchicalVisitor)} method returns. It should be the result returned by that method.
	 * <p>
	 * If {@code node} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code node} could not be traversed by this {@code AbstractReflectiveNodeHierarchicalVisitor}, a {@link NodeTraversalException} may be thrown. But no guarantees can be made.
	 * <p>
	 * The implementation provided by this class will attempt to delegate all calls to overloaded methods with a single parameter argument that is assignment compatible to {@code Node}. If an overloaded method can be found for {@code node}, that method
	 * will be called. If a method was called and its return type is either {@code Boolean} or {@code boolean}, its returned value will be returned by this method. In any other case, {@code true} will be returned. The overloaded method may have any
	 * access modifier assigned to it, so private methods are allowed.
	 * 
	 * @param node the {@code Node} to leave
	 * @return {@code true} if, and only if, {@code node}s sibling {@code Node}s should be visited, {@code false} otherwise
	 * @throws NodeTraversalException thrown if, and only if, {@code node} could not be traversed by this {@code AbstractReflectiveNodeHierarchicalVisitor}
	 * @throws NullPointerException thrown if, and only if, {@code node} is {@code null}
	 */
	@Override
	public final boolean visitLeave(final Node node) {
		return doVisit(Objects.requireNonNull(node, "node == null"), "visitLeave");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private boolean doVisit(final Node node, final String methodName) {
		boolean result = true;
		
		try {
			final Set<Class<?>> classes = doGetClasses(node.getClass(), new HashSet<>());
			
			Method method = null;
			
			for(final Class<?> clazz : classes) {
				try {
					method = getClass().getDeclaredMethod(methodName, clazz);
					
					break;
				} catch(final NoSuchMethodException e) {
					
				}
			}
			
			if(method != null) {
				final boolean isAccessible = method.isAccessible();
				
				method.setAccessible(true);
				
				final Object object = method.invoke(this, node);
				
				method.setAccessible(isAccessible);
				
				if(object instanceof Boolean) {
					result = Boolean.class.cast(object).booleanValue();
				}
			}
		} catch(final IllegalAccessException | IllegalArgumentException | InvocationTargetException | SecurityException e) {
			throw new NodeTraversalException(e);
		}
		
		return result;
	}
	
	private Set<Class<?>> doGetClasses(final Class<?> thisClass, final Set<Class<?>> classes) {
		if(Node.class.isAssignableFrom(thisClass)) {
			classes.add(thisClass);
		}
		
		for(final Class<?> interfaceClass : thisClass.getInterfaces()) {
			if(Node.class.isAssignableFrom(interfaceClass)) {
				classes.add(interfaceClass);
				
				doGetClasses(interfaceClass, classes);
			}
		}
		
		final Class<?> superClass = thisClass.getSuperclass();
		
		return superClass != null && Node.class.isAssignableFrom(superClass) ? doGetClasses(superClass, classes) : classes;
	}
}