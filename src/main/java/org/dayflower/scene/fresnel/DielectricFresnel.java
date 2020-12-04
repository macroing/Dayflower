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
package org.dayflower.scene.fresnel;

import static org.dayflower.util.Floats.abs;
import static org.dayflower.util.Floats.equal;
import static org.dayflower.util.Floats.max;
import static org.dayflower.util.Floats.saturate;
import static org.dayflower.util.Floats.sqrt;

import java.util.Objects;

import org.dayflower.image.Color3F;

/**
 * A {@code DielectricFresnel} is used to compute the Fresnel equation for materials that are dielectric.
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class DielectricFresnel implements Fresnel {
	private final float etaI;
	private final float etaT;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code DielectricFresnel} instance.
	 * 
	 * @param etaI the index of refraction (IOR) for the incident media
	 * @param etaT the index of refraction (IOR) for the transmitted media
	 */
	public DielectricFresnel(final float etaI, final float etaT) {
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
		return new Color3F(evaluate(cosThetaI, this.etaI, this.etaT));
	}
	
	/**
	 * Returns a {@code String} representation of this {@code DielectricFresnel} instance.
	 * 
	 * @return a {@code String} representation of this {@code DielectricFresnel} instance
	 */
	@Override
	public String toString() {
		return String.format("new DielectricFresnel(%+.10f, %+.10f)", Float.valueOf(this.etaI), Float.valueOf(this.etaT));
	}
	
	/**
	 * Compares {@code object} to this {@code DielectricFresnel} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code DielectricFresnel}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code DielectricFresnel} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code DielectricFresnel}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof DielectricFresnel)) {
			return false;
		} else if(!equal(this.etaI, DielectricFresnel.class.cast(object).etaI)) {
			return false;
		} else if(!equal(this.etaT, DielectricFresnel.class.cast(object).etaT)) {
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
	 * Returns a hash code for this {@code DielectricFresnel} instance.
	 * 
	 * @return a hash code for this {@code DielectricFresnel} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(Float.valueOf(this.etaI), Float.valueOf(this.etaT));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code float} with the amount of light reflected by the surface.
	 * 
	 * @param cosThetaI the cosine of the angle made by the incoming direction and the surface normal
	 * @param etaI the index of refraction (IOR) for the incident media
	 * @param etaT the index of refraction (IOR) for the transmitted media
	 * @return a {@code float} with the amount of light reflected by the surface
	 */
	public static float evaluate(final float cosThetaI, final float etaI, final float etaT) {
		final float saturateCosThetaI = saturate(cosThetaI, -1.0F, 1.0F);
		
		final boolean isEntering = saturateCosThetaI > 0.0F;
		
		final float currentCosThetaI = isEntering ? saturateCosThetaI : abs(saturateCosThetaI);
		final float currentEtaI = isEntering ? etaI : etaT;
		final float currentEtaT = isEntering ? etaT : etaI;
		
		final float currentSinThetaI = sqrt(max(0.0F, 1.0F - currentCosThetaI * currentCosThetaI));
		final float currentSinThetaT = currentEtaI / currentEtaT * currentSinThetaI;
		
		if(currentSinThetaT >= 1.0F) {
			return 1.0F;
		}
		
		final float currentCosThetaT = sqrt(max(0.0F, 1.0F - currentSinThetaT * currentSinThetaT));
		
		final float reflectancePara = ((currentEtaT * currentCosThetaI) - (currentEtaI * currentCosThetaT)) / ((currentEtaT * currentCosThetaI) + (currentEtaI * currentCosThetaT));
		final float reflectancePerp = ((currentEtaI * currentCosThetaI) - (currentEtaT * currentCosThetaT)) / ((currentEtaI * currentCosThetaI) + (currentEtaT * currentCosThetaT));
		final float reflectance = (reflectancePara * reflectancePara + reflectancePerp * reflectancePerp) / 2.0F;
		
		return reflectance;
	}
}