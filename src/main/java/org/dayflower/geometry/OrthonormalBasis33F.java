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

import static org.dayflower.utility.Floats.abs;
import static org.dayflower.utility.Floats.sqrt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.UncheckedIOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.dayflower.node.Node;
import org.dayflower.node.NodeHierarchicalVisitor;
import org.dayflower.node.NodeTraversalException;

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
public final class OrthonormalBasis33F implements Node {
	private static final Map<OrthonormalBasis33F, OrthonormalBasis33F> CACHE = new HashMap<>();
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
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
		this.v = Vector3F.computeV(w);
		this.u = Vector3F.crossProduct(this.v, this.w);
		
//		The following seems to produce the same result as new OrthonormalBasis33F(w, v):
//		this.w = Vector3F.normalize(w);
//		this.u = Vector3F.computeV(w);
//		this.v = Vector3F.crossProduct(this.w, this.u);
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
	 * Returns {@code true} if, and only if, all {@link Vector3F} instances in this {@code OrthonormalBasis33F} instance are orthogonal, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, all {@code Vector3F} instances in this {@code OrthonormalBasis33F} instance are orthogonal, {@code false} otherwise
	 */
	public boolean hasOrthogonalVectors() {
		final boolean orthogonalUV = Vector3F.orthogonal(this.u, this.v);
		final boolean orthogonalVW = Vector3F.orthogonal(this.v, this.w);
		final boolean orthogonalWU = Vector3F.orthogonal(this.w, this.u);
		
		return orthogonalUV && orthogonalVW && orthogonalWU;
	}
	
	/**
	 * Returns {@code true} if, and only if, all {@link Vector3F} instances in this {@code OrthonormalBasis33F} instance are unit vectors, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, all {@code Vector3F} instances in this {@code OrthonormalBasis33F} instance are unit vectors, {@code false} otherwise
	 */
	public boolean hasUnitVectors() {
		final boolean isUnitVectorU = this.u.isUnitVector();
		final boolean isUnitVectorV = this.v.isUnitVector();
		final boolean isUnitVectorW = this.w.isUnitVector();
		
		return isUnitVectorU && isUnitVectorV && isUnitVectorW;
	}
	
	/**
	 * Returns {@code true} if, and only if, all {@link Vector3F} instances in this {@code OrthonormalBasis33F} instance are unit vectors and are orthogonal, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, all {@code Vector3F} instances in this {@code OrthonormalBasis33F} instance are unit vectors and are orthogonal, {@code false} otherwise
	 */
	public boolean isOrthonormal() {
		return hasUnitVectors() && hasOrthogonalVectors();
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
	
	/**
	 * Writes this {@code OrthonormalBasis33F} instance to {@code dataOutput}.
	 * <p>
	 * If {@code dataOutput} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * 
	 * @param dataOutput the {@code DataOutput} instance to write to
	 * @throws NullPointerException thrown if, and only if, {@code dataOutput} is {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
	public void write(final DataOutput dataOutput) {
		this.w.write(dataOutput);
		this.v.write(dataOutput);
		this.u.write(dataOutput);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a new {@code OrthonormalBasis33F} instance that is based on PBRT.
	 * <p>
	 * If {@code w} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param w the W-direction
	 * @return a new {@code OrthonormalBasis33F} instance that is based on PBRT
	 * @throws NullPointerException thrown if, and only if, {@code w} is {@code null}
	 */
	public static OrthonormalBasis33F coordinateSystem(final Vector3F w) {
		final Vector3F u = abs(w.getX()) > abs(w.getY()) ? Vector3F.divide(new Vector3F(-w.getZ(), 0.0F, w.getX()), sqrt(w.getX() * w.getX() + w.getZ() * w.getZ())) : Vector3F.divide(new Vector3F(0.0F, w.getZ(), - w.getY()), sqrt(w.getY() * w.getY() + w.getZ() * w.getZ()));
		final Vector3F v = Vector3F.crossProduct(w, u);
		
		return new OrthonormalBasis33F(w, v, u);
	}
	
	/**
	 * Returns a cached version of {@code orthonormalBasis}.
	 * <p>
	 * If {@code orthonormalBasis} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param orthonormalBasis an {@code OrthonormalBasis33F} instance
	 * @return a orthonormalBasis version of {@code orthonormalBasis}
	 * @throws NullPointerException thrown if, and only if, {@code orthonormalBasis} is {@code null}
	 */
	public static OrthonormalBasis33F getCached(final OrthonormalBasis33F orthonormalBasis) {
		return CACHE.computeIfAbsent(Objects.requireNonNull(orthonormalBasis, "orthonormalBasis == null"), key -> new OrthonormalBasis33F(Vector3F.getCached(orthonormalBasis.getW()), Vector3F.getCached(orthonormalBasis.getV()), Vector3F.getCached(orthonormalBasis.getU())));
	}
	
	/**
	 * Flips the directions of {@code orthonormalBasis}.
	 * <p>
	 * Returns a new {@code OrthonormalBasis33F} with the directions flipped.
	 * <p>
	 * If {@code orthonormalBasis} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param orthonormalBasis the {@code OrthonormalBasis33F} instance to flip the directions for
	 * @return a new {@code OrthonormalBasis33F} with the directions flipped
	 * @throws NullPointerException thrown if, and only if, {@code orthonormalBasis} is {@code null}
	 */
	public static OrthonormalBasis33F flip(final OrthonormalBasis33F orthonormalBasis) {
		return new OrthonormalBasis33F(Vector3F.negate(orthonormalBasis.w), Vector3F.negate(orthonormalBasis.v), Vector3F.negate(orthonormalBasis.u));
	}
	
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
	 * Performs a linear interpolation operation on the supplied values.
	 * <p>
	 * Returns an {@code OrthonormalBasis33F} instance with the result of the linear interpolation operation.
	 * <p>
	 * If either {@code a} or {@code b} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param a an {@code OrthonormalBasis33F} instance
	 * @param b an {@code OrthonormalBasis33F} instance
	 * @param t the factor
	 * @return an {@code OrthonormalBasis33F} instance with the result of the linear interpolation operation
	 * @throws NullPointerException thrown if, and only if, either {@code a} or {@code b} are {@code null}
	 */
	public static OrthonormalBasis33F lerp(final OrthonormalBasis33F a, final OrthonormalBasis33F b, final float t) {
		final Vector3F u = Vector3F.lerp(a.u, b.u, t);
		final Vector3F v = Vector3F.lerp(a.v, b.v, t);
		final Vector3F w = Vector3F.lerp(a.w, b.w, t);
		
		return new OrthonormalBasis33F(w, v, u);
	}
	
	/**
	 * Returns a new {@code OrthonormalBasis33F} instance by reading it from {@code dataInput}.
	 * <p>
	 * If {@code dataInput} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * 
	 * @param dataInput the {@code DataInput} instance to read from
	 * @return a new {@code OrthonormalBasis33F} instance by reading it from {@code dataInput}
	 * @throws NullPointerException thrown if, and only if, {@code dataInput} is {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
	public static OrthonormalBasis33F read(final DataInput dataInput) {
		return new OrthonormalBasis33F(Vector3F.read(dataInput), Vector3F.read(dataInput), Vector3F.read(dataInput));
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