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
package org.dayflower.scene.light;

import static org.dayflower.util.Floats.PI;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.Optional;

import org.dayflower.geometry.Matrix44F;
import org.dayflower.geometry.Point2F;
import org.dayflower.geometry.Ray3F;
import org.dayflower.geometry.Shape3F;
import org.dayflower.geometry.Vector3F;
import org.dayflower.image.Color3F;
import org.dayflower.scene.Intersection;
import org.dayflower.scene.LightRadianceEmittedResult;
import org.dayflower.scene.LightRadianceIncomingResult;

//TODO: Add Javadocs!
public final class DiffuseAreaLight extends AreaLight {
	private final Color3F radianceEmitted;
	private final Shape3F shape;
	private final boolean isTwoSided;
	private final float surfaceArea;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public DiffuseAreaLight(final Matrix44F lightToWorld, final int samples, final Color3F radianceEmitted, final Shape3F shape, final boolean isTwoSided) {
		super(lightToWorld, samples);
		
		this.radianceEmitted = Objects.requireNonNull(radianceEmitted, "radianceEmitted == null");
		this.shape = Objects.requireNonNull(shape, "shape == null");
		this.isTwoSided = isTwoSided;
		this.surfaceArea = shape.getSurfaceArea();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	@Override
	public Color3F evaluateRadiance(final Intersection intersection, final Vector3F direction) {
		Objects.requireNonNull(intersection, "intersection == null");
		Objects.requireNonNull(direction, "direction == null");
		
		return this.isTwoSided || Vector3F.dotProduct(intersection.getSurfaceIntersectionWorldSpace().getSurfaceNormalS(), direction) > 0.0F ? this.radianceEmitted : Color3F.BLACK;
	}
	
//	TODO: Add Javadocs!
	@Override
	public Color3F evaluateRadianceEmitted(final Ray3F ray) {
		Objects.requireNonNull(ray, "ray == null");
		
		return Color3F.BLACK;
	}
	
//	TODO: Add Javadocs!
	@Override
	public Color3F power() {
		return Color3F.multiply(Color3F.multiply(Color3F.multiply(this.radianceEmitted, this.isTwoSided ? 2.0F : 1.0F), this.surfaceArea), PI);
	}
	
//	TODO: Add Javadocs!
	@Override
	public Optional<LightRadianceEmittedResult> evaluateProbabilityDensityFunctionRadianceEmitted(final Ray3F ray, final Vector3F normal) {
		Objects.requireNonNull(ray, "ray == null");
		Objects.requireNonNull(normal, "normal == null");
		
		/*
	Interaction it(ray.o, n, Vector3f(), Vector3f(n), ray.time, mediumInterface);
	
	*pdfPos = shape->Pdf(it);
	*pdfDir = twoSided ? (.5 * CosineHemispherePdf(AbsDot(n, ray.d))) : CosineHemispherePdf(Dot(n, ray.d));
		 */
		
		return Optional.empty();//TODO: Implement!
	}
	
//	TODO: Add Javadocs!
	@Override
	public Optional<LightRadianceEmittedResult> sampleRadianceEmitted(final Point2F sampleA, final Point2F sampleB) {
		Objects.requireNonNull(sampleA, "sampleA == null");
		Objects.requireNonNull(sampleB, "sampleB == null");
		
		/*
//	Sample a point on the area light's _Shape_, _pShape_
	Interaction pShape = shape->Sample(u1, pdfPos);
	
	pShape.mediumInterface = mediumInterface;
	
	*nLight = pShape.n;
	
//	Sample a cosine-weighted outgoing direction _w_ for area light
	Vector3f w;
	
	if (twoSided) {
		Point2f u = u2;
		
//		Choose a side to sample and then remap u[0] to [0,1] before
//		applying cosine-weighted hemisphere sampling for the chosen side.
		if (u[0] < .5) {
			u[0] = std::min(u[0] * 2, OneMinusEpsilon);
			
			w = CosineSampleHemisphere(u);
		} else {
			u[0] = std::min((u[0] - .5f) * 2, OneMinusEpsilon);
			
			w = CosineSampleHemisphere(u);
			w.z *= -1;
		}
		
		*pdfDir = 0.5f * CosineHemispherePdf(std::abs(w.z));
	} else {
		w = CosineSampleHemisphere(u2);
		
		*pdfDir = CosineHemispherePdf(w.z);
	}
	
	Vector3f v1, v2, n(pShape.n);
	
	CoordinateSystem(n, &v1, &v2);
	
	w = w.x * v1 + w.y * v2 + w.z * n;
	
	*ray = pShape.SpawnRay(w);
	
	return L(pShape, w);
		 */
		
		return Optional.empty();//TODO: Implement!
	}
	
//	TODO: Add Javadocs!
	@Override
	public Optional<LightRadianceIncomingResult> sampleRadianceIncoming(final Intersection intersection, final Point2F sample) {
		Objects.requireNonNull(intersection, "intersection == null");
		Objects.requireNonNull(sample, "sample == null");
		
		/*
	Interaction pShape = shape->Sample(ref, u, pdf);
	
	pShape.mediumInterface = mediumInterface;
	
	if (*pdf == 0 || (pShape.p - ref.p).LengthSquared() == 0) {
		*pdf = 0;
		
		return 0.f;
	}
	
	*wi = Normalize(pShape.p - ref.p);
	
	*vis = VisibilityTester(ref, pShape);
	
	return L(pShape, -*wi);
		 */
		
		return Optional.empty();//TODO: Implement!
	}
	
//	TODO: Add Javadocs!
	@Override
	public String toString() {
		return "";//TODO: Implement!
	}
	
//	TODO: Add Javadocs!
	@Override
	public boolean equals(final Object object) {
		return false;//TODO: Implement!
	}
	
//	TODO: Add Javadocs!
	@Override
	public boolean isDeltaDistribution() {
		return false;
	}
	
//	TODO: Add Javadocs!
	@Override
	public float evaluateProbabilityDensityFunctionRadianceIncoming(final Intersection intersection, final Vector3F incoming) {
		Objects.requireNonNull(intersection, "intersection == null");
		Objects.requireNonNull(incoming, "incoming == null");
		
		
		
		/*
	return shape->Pdf(ref, wi);
		 */
		
		return 0.0F;//TODO: Implement!
	}
	
//	TODO: Add Javadocs!
	@Override
	public int hashCode() {
		return 0;//TODO: Implement!
	}
}