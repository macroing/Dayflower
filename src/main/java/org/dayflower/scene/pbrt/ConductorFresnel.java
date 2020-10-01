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

import static org.dayflower.util.Floats.abs;
import static org.dayflower.util.Floats.equal;
import static org.dayflower.util.Floats.saturate;

import java.util.Objects;

import org.dayflower.image.Color3F;

/**
 * A {@code ConductorFresnel} is used to compute the Fresnel equation for materials that are conductors.
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class ConductorFresnel implements Fresnel {
	private final float etaI;
	private final float etaT;
	private final float k;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code ConductorFresnel} instance.
	 * 
	 * @param etaI the index of refraction (IOR) for the incident media
	 * @param etaT the index of refraction (IOR) for the transmitted media
	 * @param k the absorption coefficient
	 */
	public ConductorFresnel(final float etaI, final float etaT, final float k) {
		this.etaI = etaI;
		this.etaT = etaT;
		this.k = k;
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
		return doEvaluate(abs(cosThetaI), new Color3F(this.etaI), new Color3F(this.etaT), new Color3F(this.k));
	}
	
	/**
	 * Returns a {@code String} representation of this {@code ConductorFresnel} instance.
	 * 
	 * @return a {@code String} representation of this {@code ConductorFresnel} instance
	 */
	@Override
	public String toString() {
		return String.format("new ConductorFresnel(%+.10f, %+.10f, %+.10f)", Float.valueOf(this.etaI), Float.valueOf(this.etaT), Float.valueOf(this.k));
	}
	
	/**
	 * Compares {@code object} to this {@code ConductorFresnel} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code ConductorFresnel}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code ConductorFresnel} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code ConductorFresnel}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof ConductorFresnel)) {
			return false;
		} else if(!equal(this.etaI, ConductorFresnel.class.cast(object).etaI)) {
			return false;
		} else if(!equal(this.etaT, ConductorFresnel.class.cast(object).etaT)) {
			return false;
		} else if(!equal(this.k, ConductorFresnel.class.cast(object).k)) {
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
	 * Returns the absorption coefficient.
	 * 
	 * @return the absorption coefficient
	 */
	public float getK() {
		return this.k;
	}
	
	/**
	 * Returns a hash code for this {@code ConductorFresnel} instance.
	 * 
	 * @return a hash code for this {@code ConductorFresnel} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(Float.valueOf(this.etaI), Float.valueOf(this.etaT), Float.valueOf(this.k));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static Color3F doEvaluate(final float cosThetaI, final Color3F etaI, final Color3F etaT, final Color3F k) {
		final float saturateCosThetaI = saturate(cosThetaI, -1.0F, 1.0F);
		
		final Color3F eta = Color3F.divide(etaT, etaI);
		final Color3F etaK = Color3F.divide(k, etaI);
		
		final float cosThetaISquared = saturateCosThetaI * saturateCosThetaI;
		final float sinThetaISquared = 1.0F - cosThetaISquared;
		
		final Color3F etaSquared = Color3F.multiply(eta, eta);
		final Color3F etaKSquared = Color3F.multiply(etaK, etaK);
		
		final Color3F t0 = Color3F.subtract(Color3F.subtract(etaSquared, etaKSquared), sinThetaISquared);
		
		final Color3F aSquaredPlusBSquared = Color3F.sqrt(Color3F.add(Color3F.multiply(t0, t0), Color3F.multiply(Color3F.multiply(etaSquared, 4.0F), etaKSquared)));
		
		final Color3F t1 = Color3F.add(aSquaredPlusBSquared, cosThetaISquared);
		final Color3F t2 = Color3F.multiply(Color3F.sqrt(Color3F.multiply(Color3F.add(aSquaredPlusBSquared, t0), 0.5F)), 2.0F * saturateCosThetaI);
		final Color3F t3 = Color3F.add(Color3F.multiply(aSquaredPlusBSquared, cosThetaISquared), sinThetaISquared * sinThetaISquared);
		final Color3F t4 = Color3F.multiply(t2, sinThetaISquared);
		
		final Color3F reflectanceS = Color3F.divide(Color3F.subtract(t1, t2), Color3F.add(t1, t2));
		final Color3F reflectanceP = Color3F.divide(Color3F.multiply(reflectanceS, Color3F.subtract(t3, t4)), Color3F.add(t3, t4));
		final Color3F reflectance = Color3F.multiply(Color3F.add(reflectanceP, reflectanceS), 0.5F);
		
		return reflectance;
	}
}