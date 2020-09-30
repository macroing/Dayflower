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
package org.dayflower.scene.pbrt;

import static org.dayflower.util.Floats.equal;

import java.lang.reflect.Field;
import java.util.Objects;

import org.dayflower.image.Color3F;

/**
 * A {@code PBRTDielectricFresnel} is used to compute the Fresnel equation for dielectric materials.
 * <p>
 * This class is immutable and therefore thread-safe.
 * <p>
 * Note: This class will change name from {@code PBRTDielectricFresnel} to {@code DielectricFresnel} in the future.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class PBRTDielectricFresnel implements PBRTFresnel {
	private final float etaI;
	private final float etaT;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code PBRTDielectricFresnel} instance.
	 * 
	 * @param etaI the index of refraction (IOR) for the incident media
	 * @param etaT the index of refraction (IOR) for the transmitted media
	 */
	public PBRTDielectricFresnel(final float etaI, final float etaT) {
		this.etaI = etaI;
		this.etaT = etaT;
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
		return null;//TODO: Implement!
	}
	
	/**
	 * Returns a {@code String} representation of this {@code PBRTDielectricFresnel} instance.
	 * 
	 * @return a {@code String} representation of this {@code PBRTDielectricFresnel} instance
	 */
	@Override
	public String toString() {
		return String.format("new PBRTDielectricFresnel(%+.10f, %+.10f)", Float.valueOf(this.etaI), Float.valueOf(this.etaT));
	}
	
	/**
	 * Compares {@code object} to this {@code PBRTDielectricFresnel} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code PBRTDielectricFresnel}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code PBRTDielectricFresnel} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code PBRTDielectricFresnel}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof PBRTDielectricFresnel)) {
			return false;
		} else if(!equal(this.etaI, PBRTDielectricFresnel.class.cast(object).etaI)) {
			return false;
		} else if(!equal(this.etaT, PBRTDielectricFresnel.class.cast(object).etaT)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns the index of refraction (IOR) for the incident media.
	 * 
	 * @return the index of refraction (IOR) for the incident media
	 */
	public float getEtaI() {
		return this.etaI;
	}
	
	/**
	 * Returns the index of refraction (IOR) for the transmitted media.
	 * 
	 * @return the index of refraction (IOR) for the transmitted media
	 */
	public float getEtaT() {
		return this.etaT;
	}
	
	/**
	 * Returns a hash code for this {@code PBRTDielectricFresnel} instance.
	 * 
	 * @return a hash code for this {@code PBRTDielectricFresnel} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(Float.valueOf(this.etaI), Float.valueOf(this.etaT));
	}
}