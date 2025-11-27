/**
 * Copyright 2014 - 2025 J&#246;rgen Lundgren
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

import org.dayflower.color.Color3F;

import org.macroing.java.lang.Doubles;
import org.macroing.java.lang.Floats;

/**
 * A class that consists exclusively of static methods that performs Fresnel operations based on Schlicks approximation.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class Schlick {
	private Schlick() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the dielectric Fresnel reflectance based on Schlicks approximation.
	 * <p>
	 * If {@code r0} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param cosTheta the cosine of the angle theta
	 * @param r0 the reflectance at grazing angle
	 * @return the dielectric Fresnel reflectance based on Schlicks approximation
	 * @throws NullPointerException thrown if, and only if, {@code r0} is {@code null}
	 */
	public static Color3F fresnelDielectric(final float cosTheta, final Color3F r0) {
		return Color3F.add(r0, Color3F.multiply(Color3F.subtract(Color3F.WHITE, r0), Floats.pow5(1.0F - cosTheta)));
	}
	
	/**
	 * Returns the dielectric Fresnel reflectance based on Schlicks approximation.
	 * 
	 * @param cosTheta the cosine of the angle theta
	 * @param r0 the reflectance at grazing angle
	 * @return the dielectric Fresnel reflectance based on Schlicks approximation
	 */
	public static double fresnelDielectric(final double cosTheta, final double r0) {
		return r0 + (1.0D - r0) * fresnelWeight(cosTheta);
	}
	
	/**
	 * Returns the weight for the Fresnel reflectance based on Schlicks approximation.
	 * 
	 * @param cosTheta the cosine of the angle theta
	 * @return the weight for the Fresnel reflectance based on Schlicks approximation
	 */
	public static double fresnelWeight(final double cosTheta) {
		final double m = Doubles.saturate(1.0D - cosTheta);
		
		return (m * m) * (m * m) * m;
	}
	
	/**
	 * Returns the Fresnel reflectance based on Schlicks approximation using linear interpolation and the Fresnel reflectance weight.
	 * 
	 * @param cosTheta the cosine of the angle theta
	 * @param r0 the reflectance at grazing angle
	 * @return the Fresnel reflectance based on Schlicks approximation using linear interpolation and the Fresnel reflectance weight
	 */
	public static double fresnelWeightLerp(final double cosTheta, final double r0) {
		return Doubles.lerp(r0, 1.0D, fresnelWeight(cosTheta));
	}
	
	/**
	 * Returns the dielectric Fresnel reflectance based on Schlicks approximation.
	 * 
	 * @param cosTheta the cosine of the angle theta
	 * @param r0 the reflectance at grazing angle
	 * @return the dielectric Fresnel reflectance based on Schlicks approximation
	 */
	public static float fresnelDielectric(final float cosTheta, final float r0) {
		return r0 + (1.0F - r0) * fresnelWeight(cosTheta);
	}
	
	/**
	 * Returns the weight for the Fresnel reflectance based on Schlicks approximation.
	 * 
	 * @param cosTheta the cosine of the angle theta
	 * @return the weight for the Fresnel reflectance based on Schlicks approximation
	 */
	public static float fresnelWeight(final float cosTheta) {
		final float m = Floats.saturate(1.0F - cosTheta);
		
		return (m * m) * (m * m) * m;
	}
	
	/**
	 * Returns the Fresnel reflectance based on Schlicks approximation using linear interpolation and the Fresnel reflectance weight.
	 * 
	 * @param cosTheta the cosine of the angle theta
	 * @param r0 the reflectance at grazing angle
	 * @return the Fresnel reflectance based on Schlicks approximation using linear interpolation and the Fresnel reflectance weight
	 */
	public static float fresnelWeightLerp(final float cosTheta, final float r0) {
		return Floats.lerp(r0, 1.0F, fresnelWeight(cosTheta));
	}
}