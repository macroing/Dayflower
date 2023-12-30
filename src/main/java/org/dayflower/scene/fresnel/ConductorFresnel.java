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
package org.dayflower.scene.fresnel;

import java.util.Objects;

import org.macroing.art4j.color.Color3F;
import org.macroing.java.lang.Floats;

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
		return evaluate(Floats.abs(cosThetaI), this.etaI, this.etaT, this.k);
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
		final float saturateCosThetaI = Floats.saturate(cosThetaI, -1.0F, 1.0F);
		final float saturateCosThetaIMultipliedBy2 = saturateCosThetaI * 2.0F;
		
		final float etaR = etaT.r / etaI.r;
		final float etaG = etaT.g / etaI.g;
		final float etaB = etaT.b / etaI.b;
		
		final float etaKR = k.r / etaI.r;
		final float etaKG = k.g / etaI.g;
		final float etaKB = k.b / etaI.b;
		
		final float cosThetaISquared = saturateCosThetaI * saturateCosThetaI;
		final float sinThetaISquared = 1.0F - cosThetaISquared;
		final float sinThetaISquaredSquared = sinThetaISquared * sinThetaISquared;
		
		final float etaSquaredR = etaR * etaR;
		final float etaSquaredG = etaG * etaG;
		final float etaSquaredB = etaB * etaB;
		
		final float etaKSquaredR = etaKR * etaKR;
		final float etaKSquaredG = etaKG * etaKG;
		final float etaKSquaredB = etaKB * etaKB;
		
		final float t0R = etaSquaredR - etaKSquaredR - sinThetaISquared;
		final float t0G = etaSquaredG - etaKSquaredG - sinThetaISquared;
		final float t0B = etaSquaredB - etaKSquaredB - sinThetaISquared;
		
		final float t0SquaredR = t0R * t0R;
		final float t0SquaredG = t0G * t0G;
		final float t0SquaredB = t0B * t0B;
		
		final float aSquaredPlusBSquaredR = Floats.sqrt(t0SquaredR + etaSquaredR * etaKSquaredR * 4.0F);
		final float aSquaredPlusBSquaredG = Floats.sqrt(t0SquaredG + etaSquaredG * etaKSquaredG * 4.0F);
		final float aSquaredPlusBSquaredB = Floats.sqrt(t0SquaredB + etaSquaredB * etaKSquaredB * 4.0F);
		
		final float t1R = aSquaredPlusBSquaredR + cosThetaISquared;
		final float t1G = aSquaredPlusBSquaredG + cosThetaISquared;
		final float t1B = aSquaredPlusBSquaredB + cosThetaISquared;
		
		final float t2R = Floats.sqrt((aSquaredPlusBSquaredR + t0R) * 0.5F) * saturateCosThetaIMultipliedBy2;
		final float t2G = Floats.sqrt((aSquaredPlusBSquaredG + t0G) * 0.5F) * saturateCosThetaIMultipliedBy2;
		final float t2B = Floats.sqrt((aSquaredPlusBSquaredB + t0B) * 0.5F) * saturateCosThetaIMultipliedBy2;
		
		final float t3R = aSquaredPlusBSquaredR * cosThetaISquared + sinThetaISquaredSquared;
		final float t3G = aSquaredPlusBSquaredG * cosThetaISquared + sinThetaISquaredSquared;
		final float t3B = aSquaredPlusBSquaredB * cosThetaISquared + sinThetaISquaredSquared;
		
		final float t4R = t2R * sinThetaISquared;
		final float t4G = t2G * sinThetaISquared;
		final float t4B = t2B * sinThetaISquared;
		
		final float reflectanceSR = (t1R - t2R) / (t1R + t2R);
		final float reflectanceSG = (t1G - t2G) / (t1G + t2G);
		final float reflectanceSB = (t1B - t2B) / (t1B + t2B);
		
		final float reflectancePR = reflectanceSR * (t3R - t4R) / (t3R + t4R);
		final float reflectancePG = reflectanceSG * (t3G - t4G) / (t3G + t4G);
		final float reflectancePB = reflectanceSB * (t3B - t4B) / (t3B + t4B);
		
		final float reflectanceR = (reflectancePR + reflectanceSR) * 0.5F;
		final float reflectanceG = (reflectancePG + reflectanceSG) * 0.5F;
		final float reflectanceB = (reflectancePB + reflectanceSB) * 0.5F;
		
		return new Color3F(reflectanceR, reflectanceG, reflectanceB);
	}
}