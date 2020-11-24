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
package org.dayflower.scene;

import org.dayflower.image.Color3F;
import org.dayflower.node.Node;

/**
 * A {@code Texture} represents a texture of a surface.
 * <p>
 * All official implementations of this interface are immutable and therefore thread-safe. But this cannot be guaranteed for all implementations.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public interface Texture extends Node {
	/**
	 * Returns a {@link Color3F} instance representing the color of the surface at {@code intersection} using an RGB-color space.
	 * <p>
	 * If {@code intersection} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param intersection an {@link Intersection} instance
	 * @return a {@code Color3F} instance representing the color of the surface at {@code intersection} using an RGB-color space
	 * @throws NullPointerException thrown if, and only if, {@code intersection} is {@code null}
	 */
	Color3F getColorRGB(final Intersection intersection);
	
	/**
	 * Returns a {@link Color3F} instance representing the color of the surface at {@code intersection} using an XYZ-color space.
	 * <p>
	 * If {@code intersection} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param intersection an {@link Intersection} instance
	 * @return a {@code Color3F} instance representing the color of the surface at {@code intersection} using an XYZ-color space
	 * @throws NullPointerException thrown if, and only if, {@code intersection} is {@code null}
	 */
	Color3F getColorXYZ(final Intersection intersection);
}