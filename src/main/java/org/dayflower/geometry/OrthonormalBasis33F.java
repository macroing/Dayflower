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
 * An {@code OrthonormalBasis33F} denotes an orthonormal basis constructed by three 3-dimensional vectors of type {@code float}.
 * <p>
 * The 3-dimensional vectors used by this class are represented by {@link Vector3F}.
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class OrthonormalBasis33F {
	private final Vector3F u;
	private final Vector3F v;
	private final Vector3F w;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code OrthonormalBasis33F} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new OrthonormalBasis33F(Vector3F.w(), Vector3F.v(), Vector3F.u());
	 * }
	 * </pre>
	 */
	public OrthonormalBasis33F() {
		this(Vector3F.w(), Vector3F.v(), Vector3F.u());
	}
	
	/**
	 * Constructs a new {@code OrthonormalBasis33F} instance given {@code w}.
	 * <p>
	 * If {@code w} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * <strong>Note:</strong> This constructor does normalize {@code w}.
	 * 
	 * @param w a {@link Vector3F} pointing in the W-direction
	 * @throws NullPointerException thrown if, and only if, {@code w} is {@code null}
	 */
	public OrthonormalBasis33F(final Vector3F w) {
		this.w = Vector3F.normalize(w);
		this.v = Vector3F.normalize(Vector3F.computeV(this.w));
		this.u = Vector3F.crossProduct(this.v, this.w);
	}
	
	/**
	 * Constructs a new {@code OrthonormalBasis33F} instance given {@code w} and {@code v}.
	 * <p>
	 * If either {@code w} or {@code v} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * <strong>Note:</strong> This constructor does normalize both {@code w} and {@code v}.
	 * 
	 * @param w a {@link Vector3F} pointing in the W-direction
	 * @param v a {@code Vector3F} pointing in the V-direction to some degree
	 * @throws NullPointerException thrown if, and only if, either {@code w} or {@code v} are {@code null}
	 */
	public OrthonormalBasis33F(final Vector3F w, final Vector3F v) {
		this.w = Vector3F.normalize(w);
		this.u = Vector3F.normalize(Vector3F.crossProduct(Vector3F.normalize(v), this.w));
		this.v = Vector3F.crossProduct(this.w, this.u);
	}
	
	/**
	 * Constructs a new {@code OrthonormalBasis33F} instance given {@code w}, {@code v} and {@code u}.
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
	public OrthonormalBasis33F(final Vector3F w, final Vector3F v, final Vector3F u) {
		this.w = Objects.requireNonNull(w, "w == null");
		this.v = Objects.requireNonNull(v, "v == null");
		this.u = Objects.requireNonNull(u, "u == null");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code String} representation of this {@code OrthonormalBasis33F} instance.
	 * 
	 * @return a {@code String} representation of this {@code OrthonormalBasis33F} instance
	 */
	@Override
	public String toString() {
		return String.format("new OrthonormalBasis33F(%s, %s, %s)", this.w, this.v, this.u);
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
	 * Compares {@code object} to this {@code OrthonormalBasis33F} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code OrthonormalBasis33F}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code OrthonormalBasis33F} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code OrthonormalBasis33F}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof OrthonormalBasis33F)) {
			return false;
		} else if(!Objects.equals(this.u, OrthonormalBasis33F.class.cast(object).u)) {
			return false;
		} else if(!Objects.equals(this.v, OrthonormalBasis33F.class.cast(object).v)) {
			return false;
		} else if(!Objects.equals(this.w, OrthonormalBasis33F.class.cast(object).w)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns a hash code for this {@code OrthonormalBasis33F} instance.
	 * 
	 * @return a hash code for this {@code OrthonormalBasis33F} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.u, this.v, this.w);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Flips the U-direction of {@code orthonormalBasis}.
	 * <p>
	 * Returns a new {@code OrthonormalBasis33F} with the U-direction flipped.
	 * <p>
	 * If {@code orthonormalBasis} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param orthonormalBasis the {@code OrthonormalBasis33F} instance to flip the U-direction for
	 * @return a new {@code OrthonormalBasis33F} with the U-direction flipped
	 * @throws NullPointerException thrown if, and only if, {@code orthonormalBasis} is {@code null}
	 */
	public static OrthonormalBasis33F flipU(final OrthonormalBasis33F orthonormalBasis) {
		return new OrthonormalBasis33F(orthonormalBasis.w, orthonormalBasis.v, Vector3F.negate(orthonormalBasis.u));
	}
	
	/**
	 * Flips the V-direction of {@code orthonormalBasis}.
	 * <p>
	 * Returns a new {@code OrthonormalBasis33F} with the V-direction flipped.
	 * <p>
	 * If {@code orthonormalBasis} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param orthonormalBasis the {@code OrthonormalBasis33F} instance to flip the V-direction for
	 * @return a new {@code OrthonormalBasis33F} with the V-direction flipped
	 * @throws NullPointerException thrown if, and only if, {@code orthonormalBasis} is {@code null}
	 */
	public static OrthonormalBasis33F flipV(final OrthonormalBasis33F orthonormalBasis) {
		return new OrthonormalBasis33F(orthonormalBasis.w, Vector3F.negate(orthonormalBasis.v), orthonormalBasis.u);
	}
	
	/**
	 * Flips the W-direction of {@code orthonormalBasis}.
	 * <p>
	 * Returns a new {@code OrthonormalBasis33F} with the W-direction flipped.
	 * <p>
	 * If {@code orthonormalBasis} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param orthonormalBasis the {@code OrthonormalBasis33F} instance to flip the W-direction for
	 * @return a new {@code OrthonormalBasis33F} with the W-direction flipped
	 * @throws NullPointerException thrown if, and only if, {@code orthonormalBasis} is {@code null}
	 */
	public static OrthonormalBasis33F flipW(final OrthonormalBasis33F orthonormalBasis) {
		return new OrthonormalBasis33F(Vector3F.negate(orthonormalBasis.w), orthonormalBasis.v, orthonormalBasis.u);
	}
	
	/**
	 * Performs a transformation.
	 * <p>
	 * Returns a new {@code OrthonormalBasis33F} instance with the result of the transformation.
	 * <p>
	 * If either {@code matrixLHS} or {@code orthonormalBasisRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param matrixLHS the {@link Matrix44F} instance to perform the transformation with
	 * @param orthonormalBasisRHS the {@code OrthonormalBasis33F} to transform
	 * @return a new {@code OrthonormalBasis33F} instance with the result of the transformation
	 * @throws NullPointerException thrown if, and only if, either {@code matrixLHS} or {@code orthonormalBasisRHS} are {@code null}
	 */
	public static OrthonormalBasis33F transform(final Matrix44F matrixLHS, final OrthonormalBasis33F orthonormalBasisRHS) {
		final Vector3F u = Vector3F.normalize(Vector3F.transform(matrixLHS, orthonormalBasisRHS.u));
		final Vector3F v = Vector3F.normalize(Vector3F.transform(matrixLHS, orthonormalBasisRHS.v));
		final Vector3F w = Vector3F.normalize(Vector3F.transform(matrixLHS, orthonormalBasisRHS.w));
		
		return new OrthonormalBasis33F(w, v, u);
	}
	
	/**
	 * Performs a transformation in transpose order.
	 * <p>
	 * Returns a new {@code OrthonormalBasis33F} instance with the result of the transformation.
	 * <p>
	 * If either {@code matrixLHS} or {@code orthonormalBasisRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param matrixLHS the {@link Matrix44F} instance to perform the transformation with
	 * @param orthonormalBasisRHS the {@code OrthonormalBasis33F} to transform
	 * @return a new {@code OrthonormalBasis33F} instance with the result of the transformation
	 * @throws NullPointerException thrown if, and only if, either {@code matrixLHS} or {@code orthonormalBasisRHS} are {@code null}
	 */
	public static OrthonormalBasis33F transformTranspose(final Matrix44F matrixLHS, final OrthonormalBasis33F orthonormalBasisRHS) {
		final Vector3F u = Vector3F.normalize(Vector3F.transformTranspose(matrixLHS, orthonormalBasisRHS.u));
		final Vector3F v = Vector3F.normalize(Vector3F.transformTranspose(matrixLHS, orthonormalBasisRHS.v));
		final Vector3F w = Vector3F.normalize(Vector3F.transformTranspose(matrixLHS, orthonormalBasisRHS.w));
		
		return new OrthonormalBasis33F(w, v, u);
	}
}