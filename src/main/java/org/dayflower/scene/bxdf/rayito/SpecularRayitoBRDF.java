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

/**
 * A {@code SpecularRayitoBRDF} is an implementation of {@link RayitoBXDF} that represents a BRDF (Bidirectional Reflectance Distribution Function) for specular reflection.
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class SpecularRayitoBRDF implements RayitoBXDF {
	/**
	 * Constructs a new {@code SpecularRayitoBRDF} instance.
	 */
	public SpecularRayitoBRDF() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Evaluates the solid angle for {@code o}, {@code n} and {@code i}.
	 * <p>
	 * Returns a {@link RayitoBXDFResult} with the result of the operation.
	 * <p>
	 * If either {@code o}, {@code n} or {@code i} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * specularRayitoBRDF.evaluateSolidAngle(o, n, i, false);
	 * }
	 * </pre>
	 * 
	 * @param o a {@link Vector3F} instance with the outgoing direction from the surface intersection point to the origin of the ray
	 * @param n a {@code Vector3F} instance with the surface normal
	 * @param i a {@code Vector3F} instance with the incoming direction from the light source to the surface intersection point
	 * @return a {@code RayitoBXDFResult} with the result of the operation
	 * @throws NullPointerException thrown if, and only if, either {@code o}, {@code n} or {@code i} are {@code null}
	 */
	@Override
	public RayitoBXDFResult evaluateSolidAngle(final Vector3F o, final Vector3F n, final Vector3F i) {
		return evaluateSolidAngle(o, n, i, false);
	}
	
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
	 * @param isProjected {@code true} if, and only if, the projected solid angle should be evaluated, {@code false} otherwise
	 * @return a {@code RayitoBXDFResult} with the result of the operation
	 * @throws NullPointerException thrown if, and only if, either {@code o}, {@code n} or {@code i} are {@code null}
	 */
	@Override
	public RayitoBXDFResult evaluateSolidAngle(final Vector3F o, final Vector3F n, final Vector3F i, final boolean isProjected) {
		return new RayitoBXDFResult(o, n, i, 0.0F, 0.0F);
	}
	
	/**
	 * Samples the solid angle for {@code o}, {@code n} and {@code orthonormalBasis}.
	 * <p>
	 * Returns a {@link RayitoBXDFResult} with the result of the operation.
	 * <p>
	 * If either {@code o}, {@code n} or {@code orthonormalBasis} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * specularRayitoBRDF.sampleSolidAngle(o, n, orthonormalBasis, u, v, false);
	 * }
	 * </pre>
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
		return sampleSolidAngle(o, n, orthonormalBasis, u, v, false);
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
	 * @param isProjected {@code true} if, and only if, the projected solid angle should be sampled, {@code false} otherwise
	 * @return a {@code RayitoBXDFResult} with the result of the operation
	 * @throws NullPointerException thrown if, and only if, either {@code o}, {@code n} or {@code orthonormalBasis} are {@code null}
	 */
	@Override
	public RayitoBXDFResult sampleSolidAngle(final Vector3F o, final Vector3F n, final OrthonormalBasis33F orthonormalBasis, final float u, final float v, final boolean isProjected) {
		Objects.requireNonNull(orthonormalBasis, "orthonormalBasis == null");
		
		final float nDotO = Vector3F.dotProduct(n, o);
		
		final Vector3F i = nDotO < 0.0F ? Vector3F.add(o, Vector3F.multiply(n, 2.0F * nDotO)) : Vector3F.subtract(o, Vector3F.multiply(n, 2.0F * nDotO));
		
		if(isProjected) {
			return new RayitoBXDFResult(o, n, i, 1.0F, 1.0F);
		}
		
		final float nDotI = Vector3F.dotProduct(n, i);
		
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
	 * Returns {@code true} if, and only if, this {@code SpecularRayitoBRDF} instance is using a Dirac distribution, {@code false} otherwise.
	 * <p>
	 * This method always returns {@code true}.
	 * 
	 * @return {@code true} if, and only if, this {@code SpecularRayitoBRDF} instance is using a Dirac distribution, {@code false} otherwise
	 */
	@Override
	public boolean isDiracDistribution() {
		return true;
	}
	
	/**
	 * Returns the probability density function (PDF) value of the solid angle for {@code o}, {@code n} and {@code i}.
	 * <p>
	 * If either {@code o}, {@code n} or {@code i} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * specularRayitoBRDF.probabilityDensityFunctionSolidAngle(o, n, i, false);
	 * }
	 * </pre>
	 * 
	 * @param o a {@link Vector3F} instance with the outgoing direction from the surface intersection point to the origin of the ray
	 * @param n a {@code Vector3F} instance with the surface normal
	 * @param i a {@code Vector3F} instance with the incoming direction from the light source to the surface intersection point
	 * @return the probability density function (PDF) value of the solid angle for {@code o}, {@code n} and {@code i}
	 * @throws NullPointerException thrown if, and only if, either {@code o}, {@code n} or {@code i} are {@code null}
	 */
	@Override
	public float probabilityDensityFunctionSolidAngle(final Vector3F o, final Vector3F n, final Vector3F i) {
		return probabilityDensityFunctionSolidAngle(o, n, i, false);
	}
	
	/**
	 * Returns the probability density function (PDF) value of the solid angle or the projected solid angle for {@code o}, {@code n} and {@code i}.
	 * <p>
	 * If either {@code o}, {@code n} or {@code i} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param o a {@link Vector3F} instance with the outgoing direction from the surface intersection point to the origin of the ray
	 * @param n a {@code Vector3F} instance with the surface normal
	 * @param i a {@code Vector3F} instance with the incoming direction from the light source to the surface intersection point
	 * @param isProjected {@code true} if, and only if, the projected solid angle should be used, {@code false} otherwise
	 * @return the probability density function (PDF) value of the solid angle for {@code o}, {@code n} and {@code i}
	 * @throws NullPointerException thrown if, and only if, either {@code o}, {@code n} or {@code i} are {@code null}
	 */
	@Override
	public float probabilityDensityFunctionSolidAngle(final Vector3F o, final Vector3F n, final Vector3F i, final boolean isProjected) {
		Objects.requireNonNull(o, "o == null");
		Objects.requireNonNull(n, "n == null");
		Objects.requireNonNull(i, "i == null");
		
		return isProjected ? 1.0F : abs(Vector3F.dotProduct(n, i));
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