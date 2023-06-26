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

import org.dayflower.geometry.Point3F;
import org.dayflower.geometry.Vector3F;
import org.dayflower.scene.BSSRDF;
import org.dayflower.scene.Intersection;
import org.dayflower.scene.Material;
import org.dayflower.scene.TransportMode;
import org.dayflower.scene.fresnel.DielectricFresnel;

import org.macroing.art4j.color.Color3F;
import org.macroing.java.lang.Floats;

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
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Evaluates the distribution function.
	 * <p>
	 * Returns a {@code Color3F} instance.
	 * <p>
	 * If either {@code intersection} or {@code incoming} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param intersection an {@link Intersection} instance
	 * @param incoming a {@link Vector3F} instance that contains the incoming direction
	 * @return a {@code Color3F} instance
	 * @throws NullPointerException thrown if, and only if, either {@code intersection} or {@code incoming} are {@code null}
	 */
	@Override
	public Color3F evaluateDistributionFunction(final Intersection intersection, final Vector3F incoming) {
		final float f = DielectricFresnel.evaluate(Vector3F.negate(getIntersection().getRay().getDirection()).cosTheta(), 1.0F, getEta());
		
		return Color3F.multiply(Color3F.multiply(evaluateDistributionFunctionProfile(intersection), 1.0F - f), evaluateDistributionFunctionIncoming(incoming));
	}
	
	/**
	 * Evaluates the distribution function for {@code incoming}.
	 * <p>
	 * Returns a {@code Color3F} instance.
	 * <p>
	 * If {@code incoming} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param incoming a {@link Vector3F} instance that contains the incoming direction
	 * @return a {@code Color3F} instance
	 * @throws NullPointerException thrown if, and only if, {@code incoming} is {@code null}
	 */
	public Color3F evaluateDistributionFunctionIncoming(final Vector3F incoming) {
		final float c = 1.0F - 2.0F * Utilities.computeFresnelMoment1(1.0F / getEta());
		
		return new Color3F((1.0F - DielectricFresnel.evaluate(incoming.cosTheta(), 1.0F, getEta())) / (c * Floats.PI));
	}
	
	/**
	 * Evaluates the distribution function for {@code intersection}.
	 * <p>
	 * Returns a {@code Color3F} instance.
	 * <p>
	 * If {@code intersection} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param intersection an {@link Intersection} instance
	 * @return a {@code Color3F} instance
	 * @throws NullPointerException thrown if, and only if, {@code intersection} is {@code null}
	 */
	public Color3F evaluateDistributionFunctionProfile(final Intersection intersection) {
		return evaluateDistributionFunctionR(Point3F.distance(getIntersection().getSurfaceIntersectionPoint(), intersection.getSurfaceIntersectionPoint()));
	}
	
	/**
	 * Evaluates the distribution function for {@code distance}.
	 * <p>
	 * Returns a {@code Color3F} instance.
	 * 
	 * @param distance a {@code float} that contains the distance
	 * @return a {@code Color3F} instance
	 */
	public abstract Color3F evaluateDistributionFunctionR(final float distance);
}