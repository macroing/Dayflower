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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.dayflower.geometry.Point2F;
import org.dayflower.image.Color3F;
import org.dayflower.util.Lists;

/**
 * A {@code BXDFReflectanceFunctionResult} contains the result produced by computing the reflectance function by a {@link BXDF} instance.
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class BXDFReflectanceFunctionResult {
	private final Color3F result;
	private final List<Point2F> samplesA;
	private final List<Point2F> samplesB;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code BXDFReflectanceFunctionResult} instance.
	 * <p>
	 * If either {@code result}, {@code samplesA} or at least one element in {@code samplesA} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * This constructor will create a copy of {@code samplesA}.
	 * 
	 * @param result a {@link Color3F} instance with the result of the reflectance function
	 * @param samplesA a {@code List} of sampled {@link Point2F} instances
	 * @throws NullPointerException thrown if, and only if, either {@code result}, {@code samplesA} or at least one element in {@code samplesA} are {@code null}
	 */
	public BXDFReflectanceFunctionResult(final Color3F result, final List<Point2F> samplesA) {
		this.result = Objects.requireNonNull(result, "result == null");
		this.samplesA = new ArrayList<>(Lists.requireNonNullList(samplesA, "samplesA"));
		this.samplesB = new ArrayList<>();
	}
	
	/**
	 * Constructs a new {@code BXDFReflectanceFunctionResult} instance.
	 * <p>
	 * If either {@code result}, {@code samplesA}, {@code samplesB} or at least one element in {@code samplesA} or {@code samplesB} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * This constructor will create a copy of {@code samplesA} and {@code samplesB}.
	 * 
	 * @param result a {@link Color3F} instance with the result of the reflectance function
	 * @param samplesA a {@code List} of sampled {@link Point2F} instances
	 * @param samplesB a {@code List} of sampled {@code Point2F} instances
	 * @throws NullPointerException thrown if, and only if, either {@code result}, {@code samplesA}, {@code samplesB} or at least one element in {@code samplesA} or {@code samplesB} are {@code null}
	 */
	public BXDFReflectanceFunctionResult(final Color3F result, final List<Point2F> samplesA, final List<Point2F> samplesB) {
		this.result = Objects.requireNonNull(result, "result == null");
		this.samplesA = new ArrayList<>(Lists.requireNonNullList(samplesA, "samplesA"));
		this.samplesB = new ArrayList<>(Lists.requireNonNullList(samplesB, "samplesB"));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link Color3F} instance with the result of the reflectance function.
	 * <p>
	 * The {@code Color3F} instance represents the {@code Spectrum} instance returned by the following {@code BxDF} methods in PBRT:
	 * <ul>
	 * <li>{@code rho(int nSamples, const Point2f *samples1, const Point2f *samples2)}</li>
	 * <li>{@code rho(const Vector3f &wo, int nSamples, const Point2f *samples)}</li>
	 * </ul>
	 * 
	 * @return a {@code Color3F} instance with the result of the reflectance function
	 */
	public Color3F getResult() {
		return this.result;
	}
	
	/**
	 * Returns a {@code List} of sampled {@link Point2F} instances.
	 * <p>
	 * Modifying the returned {@code List} will not affect this {@code BXDFReflectanceFunctionResult} instance.
	 * <p>
	 * The {@code List} represents the {@code Point2f} called {@code samples1} or {@code samples} and is passed as a parameter argument to the following {@code BxDF} methods in PBRT:
	 * <ul>
	 * <li>{@code rho(int nSamples, const Point2f *samples1, const Point2f *samples2)}</li>
	 * <li>{@code rho(const Vector3f &wo, int nSamples, const Point2f *samples)}</li>
	 * </ul>
	 * 
	 * @return a {@code List} of sampled {@code Point2F} instances
	 */
	public List<Point2F> getSamplesA() {
		return new ArrayList<>(this.samplesA);
	}
	
	/**
	 * Returns a {@code List} of sampled {@link Point2F} instances.
	 * <p>
	 * Modifying the returned {@code List} will not affect this {@code BXDFReflectanceFunctionResult} instance.
	 * <p>
	 * The {@code List} represents the {@code Point2f} called {@code samples2} and is passed as a parameter argument to the following {@code BxDF} method in PBRT:
	 * <ul>
	 * <li>{@code rho(int nSamples, const Point2f *samples1, const Point2f *samples2)}</li>
	 * </ul>
	 * 
	 * @return a {@code List} of sampled {@code Point2F} instances
	 */
	public List<Point2F> getSamplesB() {
		return new ArrayList<>(this.samplesB);
	}
	
	/**
	 * Returns a {@code String} representation of this {@code BXDFReflectanceFunctionResult} instance.
	 * 
	 * @return a {@code String} representation of this {@code BXDFReflectanceFunctionResult} instance
	 */
	@Override
	public String toString() {
		return "new BXDFReflectanceFunctionResult(..., ...)";
	}
	
	/**
	 * Compares {@code object} to this {@code BXDFReflectanceFunctionResult} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code BXDFReflectanceFunctionResult}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code BXDFReflectanceFunctionResult} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code BXDFReflectanceFunctionResult}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof BXDFReflectanceFunctionResult)) {
			return false;
		} else if(!Objects.equals(this.result, BXDFReflectanceFunctionResult.class.cast(object).result)) {
			return false;
		} else if(!Objects.equals(this.samplesA, BXDFReflectanceFunctionResult.class.cast(object).samplesA)) {
			return false;
		} else if(!Objects.equals(this.samplesB, BXDFReflectanceFunctionResult.class.cast(object).samplesB)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns a hash code for this {@code BXDFReflectanceFunctionResult} instance.
	 * 
	 * @return a hash code for this {@code BXDFReflectanceFunctionResult} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.result, this.samplesA, this.samplesB);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Scales the result of {@code bXDFReflectanceFunctionResult} with {@code scale}.
	 * <p>
	 * Returns a new {@code BXDFReflectanceFunctionResult} instance.
	 * <p>
	 * If either {@code bXDFReflectanceFunctionResult} or {@code scale} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param bXDFReflectanceFunctionResult the {@code BXDFReflectanceFunctionResult} instance to scale
	 * @param scale a {@link Color3F} instance used as the scale
	 * @return a new {@code BXDFReflectanceFunctionResult} instance
	 * @throws NullPointerException thrown if, and only if, either {@code bXDFReflectanceFunctionResult} or {@code scale} are {@code null}
	 */
	public static BXDFReflectanceFunctionResult scale(final BXDFReflectanceFunctionResult bXDFReflectanceFunctionResult, final Color3F scale) {
		final Color3F result = Color3F.multiply(bXDFReflectanceFunctionResult.result, scale);
		
		final List<Point2F> samplesA = bXDFReflectanceFunctionResult.samplesA;
		final List<Point2F> samplesB = bXDFReflectanceFunctionResult.samplesB;
		
		return new BXDFReflectanceFunctionResult(result, samplesA, samplesB);
	}
}