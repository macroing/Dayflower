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

import java.lang.reflect.Field;//TODO: Add Javadocs!
import java.util.Objects;

import org.dayflower.geometry.Point2F;
import org.dayflower.geometry.Point3F;
import org.dayflower.geometry.Vector3F;
import org.dayflower.scene.BSSRDF;
import org.dayflower.scene.Intersection;
import org.dayflower.scene.Material;
import org.dayflower.scene.Scene;
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
	
//	TODO: Add Javadocs!
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
	
//	TODO: Add Javadocs!
	@Override
	public Color3F evaluateS(final Intersection intersection, final Vector3F incoming) {
		final float f = DielectricFresnel.evaluate(Vector3F.negate(getIntersection().getRay().getDirection()).cosTheta(), 1.0F, getEta());
		
		return Color3F.multiply(Color3F.multiply(evaluateSP(intersection), 1.0F - f), evaluateSW(incoming));
	}
	
//	TODO: Add Javadocs!
	public Color3F evaluateSP(final Intersection intersection) {
		return evaluateSR(Point3F.distance(getIntersection().getSurfaceIntersectionPoint(), intersection.getSurfaceIntersectionPoint()));
	}
	
//	TODO: Add Javadocs!
	public Color3F evaluateSW(final Vector3F incoming) {
		final float c = 1.0F - 2.0F * Utilities.computeFresnelMoment1(1.0F / getEta());
		
		return new Color3F((1.0F - DielectricFresnel.evaluate(incoming.cosTheta(), 1.0F, getEta())) / (c * Floats.PI));
	}
	
//	TODO: Add Javadocs!
	public abstract Color3F evaluateSR(final float distance);
	
//	TODO: Add Javadocs!
	public abstract SeparableBSSRDFResult sampleSP(final Scene scene, final float u1, final Point2F u2);
	
//	TODO: Add Javadocs!
	public abstract float evaluateProbabilityDensityFunctionSP(final Intersection intersection);
	
//	TODO: Add Javadocs!
	public abstract float evaluateProbabilityDensityFunctionSR(final int index, final float distance);
	
//	TODO: Add Javadocs!
	public abstract float sampleSR(final int index, final float u);
}