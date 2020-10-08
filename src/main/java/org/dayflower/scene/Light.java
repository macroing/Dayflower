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

import java.lang.reflect.Field;
import java.util.Optional;

import org.dayflower.geometry.Point2F;
import org.dayflower.geometry.Ray3F;
import org.dayflower.geometry.Vector3F;
import org.dayflower.image.Color3F;

/**
 * A {@code Light} represents a light.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public interface Light {
	/**
	 * Returns a {@link Color3F} instance with the emitted radiance for {@code ray}.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray a {@link Ray3F} instance
	 * @return a {@code Color3F} instance with the emitted radiance for {@code ray}
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	Color3F evaluateEmittedRadiance(final Ray3F ray);
	
//	TODO: Add Javadocs!
	Color3F power();
	
//	TODO: Add Javadocs!
	Optional<LightIncomingRadianceResult> sampleIncomingRadiance(final Intersection intersection, final Point2F sample);
	
//	TODO: Add Javadocs!
	boolean isDeltaDistribution();
	
//	TODO: Add Javadocs!
	float evaluateProbabilityDensityFunctionIncomingRadiance(final Intersection intersection, final Vector3F incoming);
}