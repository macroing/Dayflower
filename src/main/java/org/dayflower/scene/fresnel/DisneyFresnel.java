/**
 * Copyright 2014 - 2021 J&#246;rgen Lundgren
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

import static org.dayflower.utility.Floats.equal;
import static org.dayflower.utility.Floats.fresnelSchlickWeight;

import java.util.Objects;

import org.dayflower.color.Color3F;

/**
 * A {@code DisneyFresnel} is used to compute the Fresnel equation for materials in a Disney-specific way.
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class DisneyFresnel implements Fresnel {
	private final Color3F r0;
	private final float eta;
	private final float metallic;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code DisneyFresnel} instance.
	 * <p>
	 * If {@code r0} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param r0 the reflectance at grazing angle
	 * @param eta the index of refraction (IOR)
	 * @param metallic the metallic coefficient
	 * @throws NullPointerException thrown if, and only if, {@code r0} is {@code null}
	 */
	public DisneyFresnel(final Color3F r0, final float eta, final float metallic) {
		this.r0 = Objects.requireNonNull(r0, "r0 == null");
		this.eta = eta;
		this.metallic = metallic;
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
		return Color3F.blend(new Color3F(DielectricFresnel.evaluate(cosThetaI, 1.0F, this.eta)), Color3F.blend(this.r0, Color3F.WHITE, fresnelSchlickWeight(cosThetaI)), this.metallic);
	}
	
	/**
	 * Returns the reflectance at grazing angle.
	 * 
	 * @return the reflectance at grazing angle
	 */
	public Color3F getR0() {
		return this.r0;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code DisneyFresnel} instance.
	 * 
	 * @return a {@code String} representation of this {@code DisneyFresnel} instance
	 */
	@Override
	public String toString() {
		return String.format("new DisneyFresnel(%s, %+.10f, %+.10f)", this.r0, Float.valueOf(this.eta), Float.valueOf(this.metallic));
	}
	
	/**
	 * Compares {@code object} to this {@code DisneyFresnel} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code DisneyFresnel}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code DisneyFresnel} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code DisneyFresnel}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof DisneyFresnel)) {
			return false;
		} else if(!Objects.equals(this.r0, DisneyFresnel.class.cast(object).r0)) {
			return false;
		} else if(!equal(this.eta, DisneyFresnel.class.cast(object).eta)) {
			return false;
		} else if(!equal(this.metallic, DisneyFresnel.class.cast(object).metallic)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns the index of refraction (IOR).
	 * 
	 * @return the index of refraction (IOR)
	 */
	public float getEta() {
		return this.eta;
	}
	
	/**
	 * Returns the metallic coefficient.
	 * 
	 * @return the metallic coefficient
	 */
	public float getMetallic() {
		return this.metallic;
	}
	
	/**
	 * Returns a hash code for this {@code DisneyFresnel} instance.
	 * 
	 * @return a hash code for this {@code DisneyFresnel} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.r0, Float.valueOf(this.eta), Float.valueOf(this.metallic));
	}
}