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
package org.dayflower.geometry;

import java.util.Objects;

/**
 * An {@code OrthoNormalBasis33F} denotes an orthonormal basis constructed by three 3-dimensional vectors of type {@code float}.
 * <p>
 * The 3-dimensional vectors used by this class are represented by {@link Vector3F}.
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class OrthoNormalBasis33F {
	private final Vector3F u;
	private final Vector3F v;
	private final Vector3F w;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code OrthoNormalBasis33F} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new OrthoNormalBasis33F(Vector3F.w(), Vector3F.v(), Vector3F.u());
	 * }
	 * </pre>
	 */
	public OrthoNormalBasis33F() {
		this(Vector3F.w(), Vector3F.v(), Vector3F.u());
	}
	
	/**
	 * Constructs a new {@code OrthoNormalBasis33F} instance given {@code w}.
	 * <p>
	 * If {@code w} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * <strong>Note:</strong> This constructor does normalize {@code w}.
	 * 
	 * @param w a {@link Vector3F} pointing in the W-direction
	 * @throws NullPointerException thrown if, and only if, {@code w} is {@code null}
	 */
	public OrthoNormalBasis33F(final Vector3F w) {
		this.w = Vector3F.normalize(w);
		this.v = Vector3F.normalize(Vector3F.computeV(this.w));
		this.u = Vector3F.crossProduct(this.v, this.w);
	}
	
	/**
	 * Constructs a new {@code OrthoNormalBasis33F} instance given {@code w} and {@code v}.
	 * <p>
	 * If either {@code w} or {@code v} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * <strong>Note:</strong> This constructor does normalize both {@code w} and {@code v}.
	 * 
	 * @param w a {@link Vector3F} pointing in the W-direction
	 * @param v a {@code Vector3F} pointing in the V-direction to some degree
	 * @throws NullPointerException thrown if, and only if, either {@code w} or {@code v} are {@code null}
	 */
	public OrthoNormalBasis33F(final Vector3F w, final Vector3F v) {
		this.w = Vector3F.normalize(w);
		this.u = Vector3F.normalize(Vector3F.crossProduct(Vector3F.normalize(v), this.w));
		this.v = Vector3F.crossProduct(this.w, this.u);
	}
	
	/**
	 * Constructs a new {@code OrthoNormalBasis33F} instance given {@code w}, {@code v} and {@code u}.
	 * <p>
	 * If either {@code w}, {@code v} or {@code u} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * <strong>Note:</strong> This constructor does not normalize {@code w}, {@code v} or {@code u}.
	 * 
	 * @param w a {@link Vector3F} pointing in the W-direction
	 * @param v a {@code Vector3F} pointing in the V-direction
	 * @param u a {@code Vector3F} pointing in the U-direction
	 * @throws NullPointerException thrown if, and only if, either {@code w}, {@code v} or {@code u} are {@code null}
	 */
	public OrthoNormalBasis33F(final Vector3F w, final Vector3F v, final Vector3F u) {
		this.w = Objects.requireNonNull(w, "w == null");
		this.v = Objects.requireNonNull(v, "v == null");
		this.u = Objects.requireNonNull(u, "u == null");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code String} representation of this {@code OrthoNormalBasis33F} instance.
	 * 
	 * @return a {@code String} representation of this {@code OrthoNormalBasis33F} instance
	 */
	@Override
	public String toString() {
		return String.format("new OrthoNormalBasis33F(%s, %s, %s)", this.w, this.v, this.u);
	}
	
	/**
	 * Returns the {@link Vector3F} pointing in the U-direction.
	 * 
	 * @return the {@code Vector3F} pointing in the U-direction
	 */
	public Vector3F getU() {
		return this.u;
	}
	
	/**
	 * Returns the {@link Vector3F} pointing in the V-direction.
	 * 
	 * @return the {@code Vector3F} pointing in the V-direction
	 */
	public Vector3F getV() {
		return this.v;
	}
	
	/**
	 * Returns the {@link Vector3F} pointing in the W-direction.
	 * 
	 * @return the {@code Vector3F} pointing in the W-direction
	 */
	public Vector3F getW() {
		return this.w;
	}
	
	/**
	 * Compares {@code object} to this {@code OrthoNormalBasis33F} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code OrthoNormalBasis33F}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code OrthoNormalBasis33F} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code OrthoNormalBasis33F}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof OrthoNormalBasis33F)) {
			return false;
		} else if(!Objects.equals(this.u, OrthoNormalBasis33F.class.cast(object).u)) {
			return false;
		} else if(!Objects.equals(this.v, OrthoNormalBasis33F.class.cast(object).v)) {
			return false;
		} else if(!Objects.equals(this.w, OrthoNormalBasis33F.class.cast(object).w)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns a hash code for this {@code OrthoNormalBasis33F} instance.
	 * 
	 * @return a hash code for this {@code OrthoNormalBasis33F} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.u, this.v, this.w);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Flips the U-direction of {@code orthoNormalBasis}.
	 * <p>
	 * Returns a new {@code OrthoNormalBasis33F} with the U-direction flipped.
	 * <p>
	 * If {@code orthoNormalBasis} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param orthoNormalBasis the {@code OrthoNormalBasis33F} instance to flip the U-direction for
	 * @return a new {@code OrthoNormalBasis33F} with the U-direction flipped
	 * @throws NullPointerException thrown if, and only if, {@code orthoNormalBasis} is {@code null}
	 */
	public static OrthoNormalBasis33F flipU(final OrthoNormalBasis33F orthoNormalBasis) {
		return new OrthoNormalBasis33F(orthoNormalBasis.w, orthoNormalBasis.v, Vector3F.negate(orthoNormalBasis.u));
	}
	
	/**
	 * Flips the V-direction of {@code orthoNormalBasis}.
	 * <p>
	 * Returns a new {@code OrthoNormalBasis33F} with the V-direction flipped.
	 * <p>
	 * If {@code orthoNormalBasis} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param orthoNormalBasis the {@code OrthoNormalBasis33F} instance to flip the V-direction for
	 * @return a new {@code OrthoNormalBasis33F} with the V-direction flipped
	 * @throws NullPointerException thrown if, and only if, {@code orthoNormalBasis} is {@code null}
	 */
	public static OrthoNormalBasis33F flipV(final OrthoNormalBasis33F orthoNormalBasis) {
		return new OrthoNormalBasis33F(orthoNormalBasis.w, Vector3F.negate(orthoNormalBasis.v), orthoNormalBasis.u);
	}
	
	/**
	 * Flips the W-direction of {@code orthoNormalBasis}.
	 * <p>
	 * Returns a new {@code OrthoNormalBasis33F} with the W-direction flipped.
	 * <p>
	 * If {@code orthoNormalBasis} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param orthoNormalBasis the {@code OrthoNormalBasis33F} instance to flip the W-direction for
	 * @return a new {@code OrthoNormalBasis33F} with the W-direction flipped
	 * @throws NullPointerException thrown if, and only if, {@code orthoNormalBasis} is {@code null}
	 */
	public static OrthoNormalBasis33F flipW(final OrthoNormalBasis33F orthoNormalBasis) {
		return new OrthoNormalBasis33F(Vector3F.negate(orthoNormalBasis.w), orthoNormalBasis.v, orthoNormalBasis.u);
	}
	
	/**
	 * Performs a transformation.
	 * <p>
	 * Returns a new {@code OrthoNormalBasis33F} instance with the result of the transformation.
	 * <p>
	 * If either {@code matrixLHS} or {@code orthoNormalBasisRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param matrixLHS the {@link Matrix44F} instance to perform the transformation with
	 * @param orthoNormalBasisRHS the {@code OrthoNormalBasis33F} to transform
	 * @return a new {@code OrthoNormalBasis33F} instance with the result of the transformation
	 * @throws NullPointerException thrown if, and only if, either {@code matrixLHS} or {@code orthoNormalBasisRHS} are {@code null}
	 */
	public static OrthoNormalBasis33F transform(final Matrix44F matrixLHS, final OrthoNormalBasis33F orthoNormalBasisRHS) {
		final Vector3F u = Vector3F.transform(matrixLHS, orthoNormalBasisRHS.u);
		final Vector3F v = Vector3F.transform(matrixLHS, orthoNormalBasisRHS.v);
		final Vector3F w = Vector3F.transform(matrixLHS, orthoNormalBasisRHS.w);
		
		return new OrthoNormalBasis33F(w, v, u);
	}
	
	/**
	 * Performs a transformation in transpose order.
	 * <p>
	 * Returns a new {@code OrthoNormalBasis33F} instance with the result of the transformation.
	 * <p>
	 * If either {@code matrixLHS} or {@code orthoNormalBasisRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param matrixLHS the {@link Matrix44F} instance to perform the transformation with
	 * @param orthoNormalBasisRHS the {@code OrthoNormalBasis33F} to transform
	 * @return a new {@code OrthoNormalBasis33F} instance with the result of the transformation
	 * @throws NullPointerException thrown if, and only if, either {@code matrixLHS} or {@code orthoNormalBasisRHS} are {@code null}
	 */
	public static OrthoNormalBasis33F transformTranspose(final Matrix44F matrixLHS, final OrthoNormalBasis33F orthoNormalBasisRHS) {
		final Vector3F u = Vector3F.transformTranspose(matrixLHS, orthoNormalBasisRHS.u);
		final Vector3F v = Vector3F.transformTranspose(matrixLHS, orthoNormalBasisRHS.v);
		final Vector3F w = Vector3F.transformTranspose(matrixLHS, orthoNormalBasisRHS.w);
		
		return new OrthoNormalBasis33F(w, v, u);
	}
}