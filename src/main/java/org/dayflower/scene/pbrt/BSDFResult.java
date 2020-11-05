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

import java.util.Objects;

import org.dayflower.geometry.Vector3F;
import org.dayflower.image.Color3F;

/**
 * A {@code BSDFResult} contains the result produced by sampling the distribution function by a {@link BSDF} instance.
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class BSDFResult {
	private final BXDFType bXDFType;
	private final Color3F result;
	private final Vector3F incoming;
	private final Vector3F outgoing;
	private final float probabilityDensityFunctionValue;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code BSDFResult} instance.
	 * <p>
	 * If {@code bXDFType} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new BSDFResult(bXDFType, Color3F.BLACK);
	 * }
	 * </pre>
	 * 
	 * @param bXDFType a {@link BXDFType} instance
	 * @throws NullPointerException thrown if, and only if, {@code bXDFType} is {@code null}
	 */
	public BSDFResult(final BXDFType bXDFType) {
		this(bXDFType, Color3F.BLACK);
	}
	
	/**
	 * Constructs a new {@code BSDFResult} instance.
	 * <p>
	 * If either {@code bXDFType} or {@code result} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new BSDFResult(bXDFType, result, Vector3F.NaN, Vector3F.NaN);
	 * }
	 * </pre>
	 * 
	 * @param bXDFType a {@link BXDFType} instance
	 * @param result a {@link Color3F} instance with the result of the distribution function
	 * @throws NullPointerException thrown if, and only if, either {@code bXDFType} or {@code result} are {@code null}
	 */
	public BSDFResult(final BXDFType bXDFType, final Color3F result) {
		this(bXDFType, result, Vector3F.NaN, Vector3F.NaN);
	}
	
	/**
	 * Constructs a new {@code BSDFResult} instance.
	 * <p>
	 * If either {@code bXDFType}, {@code result}, {@code incoming} or {@code outgoing} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new BSDFResult(bXDFType, result, incoming, outgoing, 0.0F);
	 * }
	 * </pre>
	 * 
	 * @param bXDFType a {@link BXDFType} instance
	 * @param result a {@link Color3F} instance with the result of the distribution function
	 * @param incoming a {@link Vector3F} instance with the incoming direction used by the distribution function
	 * @param outgoing a {@code Vector3F} instance with the outgoing direction used by the distribution function
	 * @throws NullPointerException thrown if, and only if, either {@code bXDFType}, {@code result}, {@code incoming} or {@code outgoing} are {@code null}
	 */
	public BSDFResult(final BXDFType bXDFType, final Color3F result, final Vector3F incoming, final Vector3F outgoing) {
		this(bXDFType, result, incoming, outgoing, 0.0F);
	}
	
	/**
	 * Constructs a new {@code BSDFResult} instance.
	 * <p>
	 * If either {@code bXDFType}, {@code result}, {@code incoming} or {@code outgoing} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param bXDFType a {@link BXDFType} instance
	 * @param result a {@link Color3F} instance with the result of the distribution function
	 * @param incoming a {@link Vector3F} instance with the incoming direction used by the distribution function
	 * @param outgoing a {@code Vector3F} instance with the outgoing direction used by the distribution function
	 * @param probabilityDensityFunctionValue a {@code float} with the probability density function (PDF) value computed by the distribution function
	 * @throws NullPointerException thrown if, and only if, either {@code bXDFType}, {@code result}, {@code incoming} or {@code outgoing} are {@code null}
	 */
	public BSDFResult(final BXDFType bXDFType, final Color3F result, final Vector3F incoming, final Vector3F outgoing, final float probabilityDensityFunctionValue) {
		this.bXDFType = Objects.requireNonNull(bXDFType, "bXDFType == null");
		this.result = Objects.requireNonNull(result, "result == null");
		this.incoming = Objects.requireNonNull(incoming, "incoming == null");
		this.outgoing = Objects.requireNonNull(outgoing, "outgoing == null");
		this.probabilityDensityFunctionValue = probabilityDensityFunctionValue;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link BXDFType} instance.
	 * 
	 * @return a {@code BXDFType} instance
	 */
	public BXDFType getBXDFType() {
		return this.bXDFType;
	}
	
	/**
	 * Returns a {@link Color3F} instance with the result of the distribution function.
	 * <p>
	 * The {@code Color3F} instance represents the {@code Spectrum} instance returned by the following {@code BSDF} method in PBRT:
	 * <ul>
	 * <li>{@code Sample_f(const Vector3f &wo, Vector3f *wi, const Point2f &u, Float *pdf, BxDFType type = BSDF_ALL, BxDFType *sampledType = nullptr)}</li>
	 * </ul>
	 * 
	 * @return a {@code Color3F} instance with the result of the distribution function
	 */
	public Color3F getResult() {
		return this.result;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code BSDFResult} instance.
	 * 
	 * @return a {@code String} representation of this {@code BSDFResult} instance
	 */
	@Override
	public String toString() {
		return String.format("new BSDFResult(%s, %s, %s, %s, %+.10f)", this.bXDFType, this.result, this.incoming, this.outgoing, Float.valueOf(this.probabilityDensityFunctionValue));
	}
	
	/**
	 * Returns a {@link Vector3F} instance with the incoming direction used by the distribution function.
	 * <p>
	 * The {@code Vector3F} instance represents the {@code Vector3f} called {@code wi} and is passed as a parameter argument to the following {@code BSDF} method in PBRT:
	 * <ul>
	 * <li>{@code Sample_f(const Vector3f &wo, Vector3f *wi, const Point2f &u, Float *pdf, BxDFType type = BSDF_ALL, BxDFType *sampledType = nullptr)}</li>
	 * </ul>
	 * 
	 * @return a {@code Vector3F} instance with the incoming direction used by the distribution function
	 */
	public Vector3F getIncoming() {
		return this.incoming;
	}
	
	/**
	 * Returns a {@link Vector3F} instance with the outgoing direction used by the distribution function.
	 * <p>
	 * The {@code Vector3F} instance represents the {@code Vector3f} called {@code wo} and is passed as a parameter argument to the following {@code BSDF} method in PBRT:
	 * <ul>
	 * <li>{@code Sample_f(const Vector3f &wo, Vector3f *wi, const Point2f &u, Float *pdf, BxDFType type = BSDF_ALL, BxDFType *sampledType = nullptr)}</li>
	 * </ul>
	 * 
	 * @return a {@code Vector3F} instance with the outgoing direction used by the distribution function
	 */
	public Vector3F getOutgoing() {
		return this.outgoing;
	}
	
	/**
	 * Compares {@code object} to this {@code BSDFResult} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code BSDFResult}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code BSDFResult} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code BSDFResult}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof BSDFResult)) {
			return false;
		} else if(!Objects.equals(this.bXDFType, BSDFResult.class.cast(object).bXDFType)) {
			return false;
		} else if(!Objects.equals(this.result, BSDFResult.class.cast(object).result)) {
			return false;
		} else if(!Objects.equals(this.incoming, BSDFResult.class.cast(object).incoming)) {
			return false;
		} else if(!Objects.equals(this.outgoing, BSDFResult.class.cast(object).outgoing)) {
			return false;
		} else if(!equal(this.probabilityDensityFunctionValue, BSDFResult.class.cast(object).probabilityDensityFunctionValue)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns a {@code float} with the probability density function (PDF) value computed by the distribution function.
	 * <p>
	 * The {@code float} represents the {@code Float} called {@code pdf} and is passed as a parameter argument to the following {@code BSDF} method in PBRT:
	 * <ul>
	 * <li>{@code Sample_f(const Vector3f &wo, Vector3f *wi, const Point2f &u, Float *pdf, BxDFType type = BSDF_ALL, BxDFType *sampledType = nullptr)}</li>
	 * </ul>
	 * 
	 * @return a {@code float} with the probability density function (PDF) value computed by the distribution function
	 */
	public float getProbabilityDensityFunctionValue() {
		return this.probabilityDensityFunctionValue;
	}
	
	/**
	 * Returns a hash code for this {@code BSDFResult} instance.
	 * 
	 * @return a hash code for this {@code BSDFResult} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.bXDFType, this.result, this.incoming, this.outgoing, Float.valueOf(this.probabilityDensityFunctionValue));
	}
}