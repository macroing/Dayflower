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
package org.dayflower.scene;

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
	
	/**
	 * Constructs a new {@code BSSRDF} instance.
	 * <p>
	 * If {@code intersection} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param intersection an {@link Intersection} instance
	 * @param eta the index of refraction
	 * @throws NullPointerException thrown if, and only if, {@code intersection} is {@code null}
	 */
	protected BSSRDF(final Intersection intersection, final float eta) {
		this.intersection = Objects.requireNonNull(intersection, "intersection == null");
		this.eta = eta;
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
	public abstract Color3F evaluateDistributionFunction(final Intersection intersection, final Vector3F incoming);
	
	/**
	 * Samples the distribution function.
	 * <p>
	 * Returns a {@code Color3F} instance.
	 * <p>
	 * If either {@code scene}, {@code u2}, {@code intersection} or {@code pDF} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code pDF.length < 1}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param scene a {@link Scene} instance
	 * @param u1 a {@code float} that contains a sample
	 * @param u2 a {@link Point2F} instance that contains a sample
	 * @param intersection an {@link Intersection} instance
	 * @param pDF a {@code float[]} that will be populated with the PDF value
	 * @return a {@code Color3F} instance
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code pDF.length < 1}
	 * @throws NullPointerException thrown if, and only if, either {@code scene}, {@code u2}, {@code intersection} or {@code pDF} are {@code null}
	 */
	public abstract Color3F sampleDistributionFunction(final Scene scene, final float u1, final Point2F u2, final Intersection intersection, final float[] pDF);
	
	/**
	 * Returns the {@link Intersection} instance associated with this {@code BSSRDF} instance.
	 * 
	 * @return the {@code Intersection} instance associated with this {@code BSSRDF} instance
	 */
	public final Intersection getIntersection() {
		return this.intersection;
	}
	
	/**
	 * Returns the index of refraction associated with this {@code BSSRDF} instance.
	 * 
	 * @return the index of refraction associated with this {@code BSSRDF} instance
	 */
	public final float getEta() {
		return this.eta;
	}
}