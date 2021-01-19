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
package org.dayflower.scene.material.rayito;

import org.dayflower.scene.Material;
import org.dayflower.scene.texture.Texture;

/**
 * A {@code RayitoMaterial} represents a material.
 * <p>
 * All official implementations of this interface are immutable and therefore thread-safe. But this cannot be guaranteed for all implementations.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public interface RayitoMaterial extends Material {
	/**
	 * The offset for the ID of the {@link Texture} denoted by {@code Emission} in the {@code float[]} or {@code int[]}.
	 */
	int ARRAY_OFFSET_TEXTURE_EMISSION_ID = 0;
	
	/**
	 * The offset for the offset of the {@link Texture} denoted by {@code Emission} in the {@code float[]} or {@code int[]}.
	 */
	int ARRAY_OFFSET_TEXTURE_EMISSION_OFFSET = 1;
}