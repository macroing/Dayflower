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
package org.dayflower.scene.material.rayito;

import java.util.Optional;

import org.dayflower.image.Color3F;
import org.dayflower.scene.Intersection;
import org.dayflower.scene.Material;
import org.dayflower.scene.TransportMode;
import org.dayflower.scene.bxdf.rayito.RayitoBSDF;

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
	 * Returns a {@link Color3F} instance with the emittance of this {@code RayitoMaterial} instance at {@code intersection}.
	 * <p>
	 * If {@code intersection} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param intersection an {@link Intersection} instance
	 * @return a {@code Color3F} instance with the emittance of this {@code RayitoMaterial} instance at {@code intersection}
	 * @throws NullPointerException thrown if, and only if, {@code intersection} is {@code null}
	 */
	Color3F emittance(final Intersection intersection);
	
	/**
	 * Computes the {@link RayitoBSDF} at {@code intersection}.
	 * <p>
	 * Returns an optional {@code RayitoBSDF} instance.
	 * <p>
	 * If either {@code intersection} or {@code transportMode} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param intersection the {@link Intersection} to compute the {@code RayitoBSDF} for
	 * @param transportMode the {@link TransportMode} to use
	 * @param isAllowingMultipleLobes {@code true} if, and only if, multiple lobes are allowed, {@code false} otherwise
	 * @return an optional {@code RayitoBSDF} instance
	 * @throws NullPointerException thrown if, and only if, either {@code intersection} or {@code transportMode} are {@code null}
	 */
	@Override
	Optional<RayitoBSDF> computeBSDF(final Intersection intersection, final TransportMode transportMode, final boolean isAllowingMultipleLobes);
}