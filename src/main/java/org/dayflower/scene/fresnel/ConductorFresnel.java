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

import static org.dayflower.image.Color3F.add;
import static org.dayflower.image.Color3F.divide;
import static org.dayflower.image.Color3F.multiply;
import static org.dayflower.image.Color3F.sqrt;
import static org.dayflower.image.Color3F.subtract;
import static org.dayflower.util.Floats.abs;
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
	private final Color3F etaI;
	private final Color3F etaT;
	private final Color3F k;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code ConductorFresnel} instance.
	 * <p>
	 * If either {@code etaI}, {@code etaT} or {@code k} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param etaI the index of refraction (IOR) for the incident media
	 * @param etaT the index of refraction (IOR) for the transmitted media
	 * @param k the absorption coefficient
	 * @throws NullPointerException thrown if, and only if, either {@code etaI}, {@code etaT} or {@code k} are {@code null}
	 */
	public ConductorFresnel(final Color3F etaI, final Color3F etaT, final Color3F k) {
		this.etaI = Objects.requireNonNull(etaI, "etaI == null");
		this.etaT = Objects.requireNonNull(etaT, "etaT == null");
		this.k = Objects.requireNonNull(k, "k == null");
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
		return evaluate(abs(cosThetaI), this.etaI, this.etaT, this.k);
	}
	
	/**
	 * Returns the index of refraction (IOR) for the incident media.
	 * 
	 * @return the index of refraction (IOR) for the incident media
	 */
	public Color3F getEtaI() {
		return this.etaI;
	}
	
	/**
	 * Returns the index of refraction (IOR) for the transmitted media.
	 * 
	 * @return the index of refraction (IOR) for the transmitted media
	 */
	public Color3F getEtaT() {
		return this.etaT;
	}
	
	/**
	 * Returns the absorption coefficient.
	 * 
	 * @return the absorption coefficient
	 */
	public Color3F getK() {
		return this.k;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code ConductorFresnel} instance.
	 * 
	 * @return a {@code String} representation of this {@code ConductorFresnel} instance
	 */
	@Override
	public String toString() {
		return String.format("new ConductorFresnel(%s, %s, %s)", this.etaI, this.etaT, this.k);
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
		} else if(!Objects.equals(this.etaI, ConductorFresnel.class.cast(object).etaI)) {
			return false;
		} else if(!Objects.equals(this.etaT, ConductorFresnel.class.cast(object).etaT)) {
			return false;
		} else if(!Objects.equals(this.k, ConductorFresnel.class.cast(object).k)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns a hash code for this {@code ConductorFresnel} instance.
	 * 
	 * @return a hash code for this {@code ConductorFresnel} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.etaI, this.etaT, this.k);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link Color3F} instance with the amount of light reflected by the surface.
	 * <p>
	 * If either {@code etaI}, {@code etaT} or {@code k} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param cosThetaI the cosine of the angle made by the incoming direction and the surface normal
	 * @param etaI the index of refraction (IOR) for the incident media
	 * @param etaT the index of refraction (IOR) for the transmitted media
	 * @param k the absorption coefficient
	 * @return a {@code Color3F} instance with the amount of light reflected by the surface
	 * @throws NullPointerException thrown if, and only if, either {@code etaI}, {@code etaT} or {@code k} are {@code null}
	 */
	public static Color3F evaluate(final float cosThetaI, final Color3F etaI, final Color3F etaT, final Color3F k) {
		final float saturateCosThetaI = saturate(cosThetaI, -1.0F, 1.0F);
		
		final Color3F eta = divide(etaT, etaI);
		final Color3F etaK = divide(k, etaI);
		
		final float cosThetaISquared = saturateCosThetaI * saturateCosThetaI;
		final float sinThetaISquared = 1.0F - cosThetaISquared;
		
		final Color3F etaSquared = multiply(eta, eta);
		final Color3F etaKSquared = multiply(etaK, etaK);
		
		final Color3F t0 = subtract(etaSquared, etaKSquared, sinThetaISquared);
		final Color3F t0Squared = multiply(t0, t0);
		
		final Color3F aSquaredPlusBSquared = sqrt(add(t0Squared, multiply(etaSquared, etaKSquared, 4.0F)));
		
		final Color3F t1 = add(aSquaredPlusBSquared, cosThetaISquared);
		final Color3F t2 = multiply(sqrt(multiply(add(aSquaredPlusBSquared, t0), 0.5F)), 2.0F * saturateCosThetaI);
		final Color3F t3 = add(multiply(aSquaredPlusBSquared, cosThetaISquared), sinThetaISquared * sinThetaISquared);
		final Color3F t4 = multiply(t2, sinThetaISquared);
		
		final Color3F reflectanceS = divide(subtract(t1, t2), add(t1, t2));
		final Color3F reflectanceP = divide(multiply(reflectanceS, subtract(t3, t4)), add(t3, t4));
		final Color3F reflectance = multiply(add(reflectanceP, reflectanceS), 0.5F);
		
		return reflectance;
	}
}