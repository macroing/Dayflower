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
package org.dayflower.scene.bssrdf;

import java.util.Objects;

import org.dayflower.geometry.Vector3F;
import org.dayflower.scene.BSSRDF;
import org.dayflower.scene.Intersection;
import org.dayflower.scene.Material;
import org.dayflower.scene.TransportMode;

/**
 * A {@code SeparableBSSRDF} represents a separable BSSRDF (Bidirectional Scattering Surface Reflectance Distribution Function).
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public abstract class SeparableBSSRDF extends BSSRDF {
	private final Material material;
	private final TransportMode transportMode;
	private final Vector3F normalLocalSpace;
	private final Vector3F normalWorldSpace;
	private final Vector3F outgoingLocalSpace;
	private final Vector3F outgoingWorldSpace;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code SeparableBSSRDF} instance.
	 * <p>
	 * If either {@code intersection}, {@code material} or {@code transportMode} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param intersection an {@link Intersection} instance
	 * @param eta the index of refraction
	 * @param material a {@link Material} instance
	 * @param transportMode a {@link TransportMode} instance
	 * @throws NullPointerException thrown if, and only if, either {@code intersection}, {@code material} or {@code transportMode} are {@code null}
	 */
	protected SeparableBSSRDF(final Intersection intersection, final float eta, final Material material, final TransportMode transportMode) {
		super(intersection, eta);
		
		this.material = Objects.requireNonNull(material, "material == null");
		this.transportMode = Objects.requireNonNull(transportMode, "transportMode == null");
		this.normalWorldSpace = intersection.getSurfaceNormalSCorrectlyOriented();
		this.normalLocalSpace = Vector3F.normalize(Vector3F.transformReverse(this.normalWorldSpace, intersection.getOrthonormalBasisS()));
		this.outgoingWorldSpace = Vector3F.negate(intersection.getRay().getDirection());
		this.outgoingLocalSpace = Vector3F.normalize(Vector3F.transformReverse(this.outgoingWorldSpace, intersection.getOrthonormalBasisS()));
	}
}