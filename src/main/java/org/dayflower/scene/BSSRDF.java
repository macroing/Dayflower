/**
 * Copyright 2014 - 2024 J&#246;rgen Lundgren
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

import java.lang.reflect.Field;//TODO: Add Javadocs!
import java.util.Objects;

import org.dayflower.geometry.Point2F;
import org.dayflower.geometry.Vector3F;

import org.macroing.art4j.color.Color3F;

/**
 * A {@code BSSRDF} represents a BSSRDF (Bidirectional Scattering Surface Reflectance Distribution Function).
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public abstract class BSSRDF {
	private final Intersection intersection;
	private final float eta;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	protected BSSRDF(final Intersection intersection, final float eta) {
		this.intersection = Objects.requireNonNull(intersection, "intersection == null");
		this.eta = eta;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public abstract BSSRDFResult sampleS(final Scene scene, final float u1, final Point2F u2);
	
//	TODO: Add Javadocs!
	public abstract Color3F evaluateS(final Intersection intersection, final Vector3F incoming);
	
//	TODO: Add Javadocs!
	public final Intersection getIntersection() {
		return this.intersection;
	}
	
//	TODO: Add Javadocs!
	public final float getEta() {
		return this.eta;
	}
}