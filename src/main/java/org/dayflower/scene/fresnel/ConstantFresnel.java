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
package org.dayflower.scene.fresnel;

import java.util.Objects;

import org.macroing.art4j.color.Color3F;

/**
 * A {@code ConstantFresnel} is used to ignore computing the Fresnel equation in certain situations.
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class ConstantFresnel implements Fresnel {
	private final Color3F light;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code ConstantFresnel} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new ConstantFresnel(Color3F.WHITE);
	 * }
	 * </pre>
	 */
	public ConstantFresnel() {
		this(Color3F.WHITE);
	}
	
	/**
	 * Constructs a new {@code ConstantFresnel} instance.
	 * <p>
	 * If {@code light} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param light a {@link Color3F} that represents the constant light reflected by the surface
	 * @throws NullPointerException thrown if, and only if, {@code light} is {@code null}
	 */
	public ConstantFresnel(final Color3F light) {
		this.light = Objects.requireNonNull(light, "light == null");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link Color3F} instance that represents the amount of light reflected by the surface.
	 * 
	 * @param cosThetaI the cosine of the angle made by the incoming direction and the surface normal
	 * @return a {@code Color3F} instance that represents the amount of light reflected by the surface
	 */
	@Override
	public Color3F evaluate(final float cosThetaI) {
		return this.light;
	}
	
	/**
	 * Returns a {@link Color3F} that represents the constant light reflected by the surface.
	 * 
	 * @return a {@code Color3F} that represents the constant light reflected by the surface
	 */
	public Color3F getLight() {
		return this.light;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code ConstantFresnel} instance.
	 * 
	 * @return a {@code String} representation of this {@code ConstantFresnel} instance
	 */
	@Override
	public String toString() {
		return String.format("new ConstantFresnel(%s)", this.light);
	}
	
	/**
	 * Compares {@code object} to this {@code ConstantFresnel} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code ConstantFresnel}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code ConstantFresnel} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code ConstantFresnel}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof ConstantFresnel)) {
			return false;
		} else if(!Objects.equals(this.light, ConstantFresnel.class.cast(object).light)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns a hash code for this {@code ConstantFresnel} instance.
	 * 
	 * @return a hash code for this {@code ConstantFresnel} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.light);
	}
}