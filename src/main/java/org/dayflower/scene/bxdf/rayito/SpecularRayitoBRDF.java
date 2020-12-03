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
package org.dayflower.scene.bxdf.rayito;

import static org.dayflower.util.Floats.abs;

import java.util.Objects;

import org.dayflower.geometry.OrthonormalBasis33F;
import org.dayflower.geometry.Vector3F;
import org.dayflower.scene.BXDFType;

/**
 * A {@code SpecularRayitoBRDF} is an implementation of {@link RayitoBXDF} that represents a BRDF (Bidirectional Reflectance Distribution Function) for specular reflection.
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class SpecularRayitoBRDF extends RayitoBXDF {
	/**
	 * Constructs a new {@code SpecularRayitoBRDF} instance.
	 */
	public SpecularRayitoBRDF() {
		super(BXDFType.SPECULAR_REFLECTION);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Evaluates the solid angle or the projected solid angle for {@code o}, {@code n} and {@code i}.
	 * <p>
	 * Returns a {@link RayitoBXDFResult} with the result of the operation.
	 * <p>
	 * If either {@code o}, {@code n} or {@code i} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param o a {@link Vector3F} instance with the outgoing direction from the surface intersection point to the origin of the ray
	 * @param n a {@code Vector3F} instance with the surface normal
	 * @param i a {@code Vector3F} instance with the incoming direction from the light source to the surface intersection point
	 * @return a {@code RayitoBXDFResult} with the result of the operation
	 * @throws NullPointerException thrown if, and only if, either {@code o}, {@code n} or {@code i} are {@code null}
	 */
	@Override
	public RayitoBXDFResult evaluateSolidAngle(final Vector3F o, final Vector3F n, final Vector3F i) {
		return new RayitoBXDFResult(o, n, i, 0.0F, 0.0F);
	}
	
	/**
	 * Samples the solid angle or the projected solid angle for {@code o}, {@code n} and {@code orthonormalBasis}.
	 * <p>
	 * Returns a {@link RayitoBXDFResult} with the result of the operation.
	 * <p>
	 * If either {@code o}, {@code n} or {@code orthonormalBasis} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param o a {@link Vector3F} instance with the outgoing direction from the surface intersection point to the origin of the ray
	 * @param n a {@code Vector3F} instance with the surface normal
	 * @param orthonormalBasis an {@link OrthonormalBasis33F} instance
	 * @param u the U-coordinate
	 * @param v the V-coordinate
	 * @return a {@code RayitoBXDFResult} with the result of the operation
	 * @throws NullPointerException thrown if, and only if, either {@code o}, {@code n} or {@code orthonormalBasis} are {@code null}
	 */
	@Override
	public RayitoBXDFResult sampleSolidAngle(final Vector3F o, final Vector3F n, final OrthonormalBasis33F orthonormalBasis, final float u, final float v) {
		Objects.requireNonNull(orthonormalBasis, "orthonormalBasis == null");
		
		final float nDotO = Vector3F.dotProduct(n, o);
		
		final Vector3F i = nDotO < 0.0F ? Vector3F.add(o, Vector3F.multiply(n, 2.0F * nDotO)) : Vector3F.subtract(o, Vector3F.multiply(n, 2.0F * nDotO));
		
		final float nDotI = Vector3F.dotProduct(n, i);
		
//		TODO: Find out why PDF is not 1.0F?
		return new RayitoBXDFResult(o, n, i, abs(nDotI), 1.0F);
	}
	
	/**
	 * Returns a {@code String} representation of this {@code SpecularRayitoBRDF} instance.
	 * 
	 * @return a {@code String} representation of this {@code SpecularRayitoBRDF} instance
	 */
	@Override
	public String toString() {
		return "new SpecularRayitoBRDF()";
	}
	
	/**
	 * Compares {@code object} to this {@code SpecularRayitoBRDF} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code SpecularRayitoBRDF}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code SpecularRayitoBRDF} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code SpecularRayitoBRDF}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof SpecularRayitoBRDF)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns the probability density function (PDF) value of the solid angle or the projected solid angle for {@code o}, {@code n} and {@code i}.
	 * <p>
	 * If either {@code o}, {@code n} or {@code i} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param o a {@link Vector3F} instance with the outgoing direction from the surface intersection point to the origin of the ray
	 * @param n a {@code Vector3F} instance with the surface normal
	 * @param i a {@code Vector3F} instance with the incoming direction from the light source to the surface intersection point
	 * @return the probability density function (PDF) value of the solid angle for {@code o}, {@code n} and {@code i}
	 * @throws NullPointerException thrown if, and only if, either {@code o}, {@code n} or {@code i} are {@code null}
	 */
	@Override
	public float probabilityDensityFunctionSolidAngle(final Vector3F o, final Vector3F n, final Vector3F i) {
		Objects.requireNonNull(o, "o == null");
		Objects.requireNonNull(n, "n == null");
		Objects.requireNonNull(i, "i == null");
		
		return 0.0F;
	}
	
	/**
	 * Returns a hash code for this {@code SpecularRayitoBRDF} instance.
	 * 
	 * @return a hash code for this {@code SpecularRayitoBRDF} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash();
	}
}