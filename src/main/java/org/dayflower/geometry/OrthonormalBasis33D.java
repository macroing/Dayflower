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
package org.dayflower.geometry;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.dayflower.node.Node;
import org.dayflower.node.NodeHierarchicalVisitor;
import org.dayflower.node.NodeTraversalException;

/**
 * An {@code OrthonormalBasis33D} denotes an orthonormal basis constructed by three 3-dimensional vectors of type {@code double}.
 * <p>
 * The 3-dimensional vectors used by this class are represented by {@link Vector3D}.
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class OrthonormalBasis33D implements Node {
	private static final Map<OrthonormalBasis33D, OrthonormalBasis33D> CACHE = new HashMap<>();
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final Vector3D u;
	private final Vector3D v;
	private final Vector3D w;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code OrthonormalBasis33D} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new OrthonormalBasis33D(Vector3D.w(), Vector3D.v(), Vector3D.u());
	 * }
	 * </pre>
	 */
	public OrthonormalBasis33D() {
		this(Vector3D.w(), Vector3D.v(), Vector3D.u());
	}
	
	/**
	 * Constructs a new {@code OrthonormalBasis33D} instance given {@code w}.
	 * <p>
	 * If {@code w} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * <strong>Note:</strong> This constructor does normalize {@code w}.
	 * 
	 * @param w a {@link Vector3D} pointing in the W-direction
	 * @throws NullPointerException thrown if, and only if, {@code w} is {@code null}
	 */
	public OrthonormalBasis33D(final Vector3D w) {
		this.w = Vector3D.normalize(w);
		this.v = Vector3D.normalize(Vector3D.computeV(this.w));
		this.u = Vector3D.crossProduct(this.v, this.w);
	}
	
	/**
	 * Constructs a new {@code OrthonormalBasis33D} instance given {@code w} and {@code v}.
	 * <p>
	 * If either {@code w} or {@code v} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * <strong>Note:</strong> This constructor does normalize both {@code w} and {@code v}.
	 * 
	 * @param w a {@link Vector3D} pointing in the W-direction
	 * @param v a {@code Vector3D} pointing in the V-direction to some degree
	 * @throws NullPointerException thrown if, and only if, either {@code w} or {@code v} are {@code null}
	 */
	public OrthonormalBasis33D(final Vector3D w, final Vector3D v) {
		this.w = Vector3D.normalize(w);
		this.u = Vector3D.normalize(Vector3D.crossProduct(Vector3D.normalize(v), this.w));
		this.v = Vector3D.crossProduct(this.w, this.u);
	}
	
	/**
	 * Constructs a new {@code OrthonormalBasis33D} instance given {@code w}, {@code v} and {@code u}.
	 * <p>
	 * If either {@code w}, {@code v} or {@code u} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * <strong>Note:</strong> This constructor does not normalize {@code w}, {@code v} or {@code u}.
	 * 
	 * @param w a {@link Vector3D} pointing in the W-direction
	 * @param v a {@code Vector3D} pointing in the V-direction
	 * @param u a {@code Vector3D} pointing in the U-direction
	 * @throws NullPointerException thrown if, and only if, either {@code w}, {@code v} or {@code u} are {@code null}
	 */
	public OrthonormalBasis33D(final Vector3D w, final Vector3D v, final Vector3D u) {
		this.w = Objects.requireNonNull(w, "w == null");
		this.v = Objects.requireNonNull(v, "v == null");
		this.u = Objects.requireNonNull(u, "u == null");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code String} representation of this {@code OrthonormalBasis33D} instance.
	 * 
	 * @return a {@code String} representation of this {@code OrthonormalBasis33D} instance
	 */
	@Override
	public String toString() {
		return String.format("new OrthonormalBasis33D(%s, %s, %s)", this.w, this.v, this.u);
	}
	
	/**
	 * Returns the {@link Vector3D} pointing in the U-direction.
	 * 
	 * @return the {@code Vector3D} pointing in the U-direction
	 */
	public Vector3D getU() {
		return this.u;
	}
	
	/**
	 * Returns the {@link Vector3D} pointing in the V-direction.
	 * 
	 * @return the {@code Vector3D} pointing in the V-direction
	 */
	public Vector3D getV() {
		return this.v;
	}
	
	/**
	 * Returns the {@link Vector3D} pointing in the W-direction.
	 * 
	 * @return the {@code Vector3D} pointing in the W-direction
	 */
	public Vector3D getW() {
		return this.w;
	}
	
	/**
	 * Accepts a {@link NodeHierarchicalVisitor}.
	 * <p>
	 * Returns the result of {@code nodeHierarchicalVisitor.visitLeave(this)}.
	 * <p>
	 * If {@code nodeHierarchicalVisitor} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If a {@code RuntimeException} is thrown by the current {@code NodeHierarchicalVisitor}, a {@code NodeTraversalException} will be thrown with the {@code RuntimeException} wrapped.
	 * <p>
	 * This implementation will:
	 * <ul>
	 * <li>throw a {@code NullPointerException} if {@code nodeHierarchicalVisitor} is {@code null}.</li>
	 * <li>throw a {@code NodeTraversalException} if {@code nodeHierarchicalVisitor} throws a {@code RuntimeException}.</li>
	 * <li>traverse its child {@code Node} instances.</li>
	 * </ul>
	 * 
	 * @param nodeHierarchicalVisitor the {@code NodeHierarchicalVisitor} to accept
	 * @return the result of {@code nodeHierarchicalVisitor.visitLeave(this)}
	 * @throws NodeTraversalException thrown if, and only if, a {@code RuntimeException} is thrown by the current {@code NodeHierarchicalVisitor}
	 * @throws NullPointerException thrown if, and only if, {@code nodeHierarchicalVisitor} is {@code null}
	 */
	@Override
	public boolean accept(final NodeHierarchicalVisitor nodeHierarchicalVisitor) {
		Objects.requireNonNull(nodeHierarchicalVisitor, "nodeHierarchicalVisitor == null");
		
		try {
			if(nodeHierarchicalVisitor.visitEnter(this)) {
				if(!this.u.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
				
				if(!this.v.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
				
				if(!this.w.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
			}
			
			return nodeHierarchicalVisitor.visitLeave(this);
		} catch(final RuntimeException e) {
			throw new NodeTraversalException(e);
		}
	}
	
	/**
	 * Compares {@code object} to this {@code OrthonormalBasis33D} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code OrthonormalBasis33D}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code OrthonormalBasis33D} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code OrthonormalBasis33D}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof OrthonormalBasis33D)) {
			return false;
		} else if(!Objects.equals(this.u, OrthonormalBasis33D.class.cast(object).u)) {
			return false;
		} else if(!Objects.equals(this.v, OrthonormalBasis33D.class.cast(object).v)) {
			return false;
		} else if(!Objects.equals(this.w, OrthonormalBasis33D.class.cast(object).w)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns a hash code for this {@code OrthonormalBasis33D} instance.
	 * 
	 * @return a hash code for this {@code OrthonormalBasis33D} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.u, this.v, this.w);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a cached version of {@code orthonormalBasis}.
	 * <p>
	 * If {@code orthonormalBasis} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param orthonormalBasis an {@code OrthonormalBasis33D} instance
	 * @return a orthonormalBasis version of {@code orthonormalBasis}
	 * @throws NullPointerException thrown if, and only if, {@code orthonormalBasis} is {@code null}
	 */
	public static OrthonormalBasis33D getCached(final OrthonormalBasis33D orthonormalBasis) {
		return CACHE.computeIfAbsent(Objects.requireNonNull(orthonormalBasis, "orthonormalBasis == null"), key -> new OrthonormalBasis33D(Vector3D.getCached(orthonormalBasis.getW()), Vector3D.getCached(orthonormalBasis.getV()), Vector3D.getCached(orthonormalBasis.getU())));
	}
	
	/**
	 * Flips the directions of {@code orthonormalBasis}.
	 * <p>
	 * Returns a new {@code OrthonormalBasis33D} with the directions flipped.
	 * <p>
	 * If {@code orthonormalBasis} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param orthonormalBasis the {@code OrthonormalBasis33D} instance to flip the directions for
	 * @return a new {@code OrthonormalBasis33D} with the directions flipped
	 * @throws NullPointerException thrown if, and only if, {@code orthonormalBasis} is {@code null}
	 */
	public static OrthonormalBasis33D flip(final OrthonormalBasis33D orthonormalBasis) {
		return new OrthonormalBasis33D(Vector3D.negate(orthonormalBasis.w), Vector3D.negate(orthonormalBasis.v), Vector3D.negate(orthonormalBasis.u));
	}
	
	/**
	 * Flips the U-direction of {@code orthonormalBasis}.
	 * <p>
	 * Returns a new {@code OrthonormalBasis33D} with the U-direction flipped.
	 * <p>
	 * If {@code orthonormalBasis} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param orthonormalBasis the {@code OrthonormalBasis33D} instance to flip the U-direction for
	 * @return a new {@code OrthonormalBasis33D} with the U-direction flipped
	 * @throws NullPointerException thrown if, and only if, {@code orthonormalBasis} is {@code null}
	 */
	public static OrthonormalBasis33D flipU(final OrthonormalBasis33D orthonormalBasis) {
		return new OrthonormalBasis33D(orthonormalBasis.w, orthonormalBasis.v, Vector3D.negate(orthonormalBasis.u));
	}
	
	/**
	 * Flips the V-direction of {@code orthonormalBasis}.
	 * <p>
	 * Returns a new {@code OrthonormalBasis33D} with the V-direction flipped.
	 * <p>
	 * If {@code orthonormalBasis} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param orthonormalBasis the {@code OrthonormalBasis33D} instance to flip the V-direction for
	 * @return a new {@code OrthonormalBasis33D} with the V-direction flipped
	 * @throws NullPointerException thrown if, and only if, {@code orthonormalBasis} is {@code null}
	 */
	public static OrthonormalBasis33D flipV(final OrthonormalBasis33D orthonormalBasis) {
		return new OrthonormalBasis33D(orthonormalBasis.w, Vector3D.negate(orthonormalBasis.v), orthonormalBasis.u);
	}
	
	/**
	 * Flips the W-direction of {@code orthonormalBasis}.
	 * <p>
	 * Returns a new {@code OrthonormalBasis33D} with the W-direction flipped.
	 * <p>
	 * If {@code orthonormalBasis} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param orthonormalBasis the {@code OrthonormalBasis33D} instance to flip the W-direction for
	 * @return a new {@code OrthonormalBasis33D} with the W-direction flipped
	 * @throws NullPointerException thrown if, and only if, {@code orthonormalBasis} is {@code null}
	 */
	public static OrthonormalBasis33D flipW(final OrthonormalBasis33D orthonormalBasis) {
		return new OrthonormalBasis33D(Vector3D.negate(orthonormalBasis.w), orthonormalBasis.v, orthonormalBasis.u);
	}
	
	/**
	 * Performs a linear interpolation operation on the supplied values.
	 * <p>
	 * Returns an {@code OrthonormalBasis33D} instance with the result of the linear interpolation operation.
	 * <p>
	 * If either {@code a} or {@code b} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param a an {@code OrthonormalBasis33D} instance
	 * @param b an {@code OrthonormalBasis33D} instance
	 * @param t the factor
	 * @return an {@code OrthonormalBasis33D} instance with the result of the linear interpolation operation
	 * @throws NullPointerException thrown if, and only if, either {@code a} or {@code b} are {@code null}
	 */
	public static OrthonormalBasis33D lerp(final OrthonormalBasis33D a, final OrthonormalBasis33D b, final double t) {
		final Vector3D u = Vector3D.lerp(a.u, b.u, t);
		final Vector3D v = Vector3D.lerp(a.v, b.v, t);
		final Vector3D w = Vector3D.lerp(a.w, b.w, t);
		
		return new OrthonormalBasis33D(w, v, u);
	}
	
	/**
	 * Performs a transformation.
	 * <p>
	 * Returns a new {@code OrthonormalBasis33D} instance with the result of the transformation.
	 * <p>
	 * If either {@code matrixLHS} or {@code orthonormalBasisRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param matrixLHS the {@link Matrix44D} instance to perform the transformation with
	 * @param orthonormalBasisRHS the {@code OrthonormalBasis33D} to transform
	 * @return a new {@code OrthonormalBasis33D} instance with the result of the transformation
	 * @throws NullPointerException thrown if, and only if, either {@code matrixLHS} or {@code orthonormalBasisRHS} are {@code null}
	 */
	public static OrthonormalBasis33D transform(final Matrix44D matrixLHS, final OrthonormalBasis33D orthonormalBasisRHS) {
		final Vector3D u = Vector3D.normalize(Vector3D.transform(matrixLHS, orthonormalBasisRHS.u));
		final Vector3D v = Vector3D.normalize(Vector3D.transform(matrixLHS, orthonormalBasisRHS.v));
		final Vector3D w = Vector3D.normalize(Vector3D.transform(matrixLHS, orthonormalBasisRHS.w));
		
		return new OrthonormalBasis33D(w, v, u);
	}
	
	/**
	 * Performs a transformation in transpose order.
	 * <p>
	 * Returns a new {@code OrthonormalBasis33D} instance with the result of the transformation.
	 * <p>
	 * If either {@code matrixLHS} or {@code orthonormalBasisRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param matrixLHS the {@link Matrix44D} instance to perform the transformation with
	 * @param orthonormalBasisRHS the {@code OrthonormalBasis33D} to transform
	 * @return a new {@code OrthonormalBasis33D} instance with the result of the transformation
	 * @throws NullPointerException thrown if, and only if, either {@code matrixLHS} or {@code orthonormalBasisRHS} are {@code null}
	 */
	public static OrthonormalBasis33D transformTranspose(final Matrix44D matrixLHS, final OrthonormalBasis33D orthonormalBasisRHS) {
		final Vector3D u = Vector3D.normalize(Vector3D.transformTranspose(matrixLHS, orthonormalBasisRHS.u));
		final Vector3D v = Vector3D.normalize(Vector3D.transformTranspose(matrixLHS, orthonormalBasisRHS.v));
		final Vector3D w = Vector3D.normalize(Vector3D.transformTranspose(matrixLHS, orthonormalBasisRHS.w));
		
		return new OrthonormalBasis33D(w, v, u);
	}
	
	/**
	 * Returns the size of the cache.
	 * 
	 * @return the size of the cache
	 */
	public static int getCacheSize() {
		return CACHE.size();
	}
	
	/**
	 * Clears the cache.
	 */
	public static void clearCache() {
		CACHE.clear();
	}
}