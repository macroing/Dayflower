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

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.Optional;

import org.dayflower.geometry.Point2F;
import org.dayflower.geometry.Ray3F;
import org.dayflower.geometry.Vector3F;
import org.dayflower.image.Color3F;
import org.dayflower.scene.Intersection;
import org.dayflower.scene.Light;
import org.dayflower.scene.LightIncomingRadianceResult;
import org.dayflower.scene.Primitive;

/**
 * A {@code PrimitiveLight} is an implementation of {@link Light} that contains a {@link Primitive}.
 * <p>
 * This class is indirectly mutable and therefore not thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class PrimitiveLight implements Light {
	private final Primitive primitive;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code PrimitiveLight} instance.
	 * <p>
	 * If {@code primitive} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param primitive the {@link Primitive} instance associated with this {@code PrimitiveLight} instance
	 * @throws NullPointerException thrown if, and only if, {@code primitive} is {@code null}
	 */
	public PrimitiveLight(final Primitive primitive) {
		this.primitive = Objects.requireNonNull(primitive, "primitive == null");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link Color3F} instance with the emitted radiance for {@code ray}.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray a {@link Ray3F} instance
	 * @return a {@code Color3F} instance with the emitted radiance for {@code ray}
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	@Override
	public Color3F evaluateEmittedRadiance(final Ray3F ray) {
		Objects.requireNonNull(ray, "ray == null");
		
		return Color3F.BLACK;//TODO: Implement!
	}
	
//	TODO: Add Javadocs!
	@Override
	public Color3F power() {
		return Color3F.BLACK;//TODO: Implement!
	}
	
	/**
	 * Returns the {@link Primitive} instance associated with this {@code PrimitiveLight} instance.
	 * 
	 * @return the {@code Primitive} instance associated with this {@code PrimitiveLight} instance
	 */
	public Primitive getPrimitive() {
		return this.primitive;
	}
	
//	TODO: Add Javadocs!
	@Override
	public Optional<LightIncomingRadianceResult> sampleIncomingRadiance(final Intersection intersection, final Point2F sample) {
		Objects.requireNonNull(intersection, "intersection == null");
		Objects.requireNonNull(sample, "sample == null");
		
		return Optional.empty();//TODO: Implement!
	}
	
	/**
	 * Returns a {@code String} representation of this {@code PrimitiveLight} instance.
	 * 
	 * @return a {@code String} representation of this {@code PrimitiveLight} instance
	 */
	@Override
	public String toString() {
		return String.format("new PrimitiveLight(%s)", this.primitive);
	}
	
	/**
	 * Compares {@code object} to this {@code PrimitiveLight} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code PrimitiveLight}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code PrimitiveLight} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code PrimitiveLight}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof PrimitiveLight)) {
			return false;
		} else if(!Objects.equals(this.primitive, PrimitiveLight.class.cast(object).primitive)) {
			return false;
		} else {
			return true;
		}
	}
	
//	TODO: Add Javadocs!
	@Override
	public boolean isDeltaDistribution() {
		return false;
	}
	
//	TODO: Add Javadocs!
	@Override
	public float evaluateProbabilityDensityFunctionIncomingRadiance(final Intersection intersection, final Vector3F incoming) {
		Objects.requireNonNull(intersection, "intersection == null");
		Objects.requireNonNull(incoming, "incoming == null");
		
		return 0.0F;//TODO: Implement!
	}
	
	/**
	 * Returns a hash code for this {@code PrimitiveLight} instance.
	 * 
	 * @return a hash code for this {@code PrimitiveLight} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.primitive);
	}
}