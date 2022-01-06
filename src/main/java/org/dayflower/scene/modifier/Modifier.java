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
package org.dayflower.scene.modifier;

import org.dayflower.geometry.SurfaceIntersection3F;
import org.dayflower.node.Node;
import org.dayflower.scene.Intersection;

/**
 * A {@code Modifier} represents a modifier of a surface.
 * <p>
 * All official implementations of this interface are immutable and therefore thread-safe. But this cannot be guaranteed for all implementations.
 * <p>
 * If you create a {@code Modifier} implementation, it is important that you use the {@link SurfaceIntersection3F} instance (in world space) to retrieve the original intersection properties. If not, there is a possibility that the modification is
 * performed on intersection properties that have already been modified.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public interface Modifier extends Node {
	/**
	 * Returns an {@code int} with the ID of this {@code Modifier} instance.
	 * 
	 * @return an {@code int} with the ID of this {@code Modifier} instance
	 */
	int getID();
	
	/**
	 * Modifies the surface at {@code intersection}.
	 * <p>
	 * If {@code intersection} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param intersection an {@link Intersection} instance
	 * @throws NullPointerException thrown if, and only if, {@code intersection} is {@code null}
	 */
	void modify(final Intersection intersection);
}