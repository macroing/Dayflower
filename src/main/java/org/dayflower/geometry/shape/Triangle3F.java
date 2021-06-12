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
package org.dayflower.geometry.shape;

import static org.dayflower.utility.Floats.abs;
import static org.dayflower.utility.Floats.gamma;
import static org.dayflower.utility.Floats.isZero;
import static org.dayflower.utility.Floats.max;

import java.io.DataOutput;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Objects;
import java.util.Optional;

import org.dayflower.geometry.BoundingVolume3F;
import org.dayflower.geometry.Matrix44F;
import org.dayflower.geometry.OrthonormalBasis33F;
import org.dayflower.geometry.Point2F;
import org.dayflower.geometry.Point3F;
import org.dayflower.geometry.Point4F;
import org.dayflower.geometry.Ray3F;
import org.dayflower.geometry.SampleGeneratorF;
import org.dayflower.geometry.Shape3F;
import org.dayflower.geometry.SurfaceIntersection3F;
import org.dayflower.geometry.SurfaceSample3F;
import org.dayflower.geometry.Vector2F;
import org.dayflower.geometry.Vector3F;
import org.dayflower.geometry.boundingvolume.AxisAlignedBoundingBox3F;
import org.dayflower.node.Node;
import org.dayflower.node.NodeHierarchicalVisitor;
import org.dayflower.node.NodeTraversalException;

/**
 * A {@code Triangle3F} is an implementation of {@link Shape3F} that represents a triangle.
 * <p>
 * This class is immutable and therefore thread-safe.
 * <p>
 * This {@code Shape3F} implementation is supported on the GPU.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class Triangle3F implements Shape3F {
	/**
	 * The name of this {@code Triangle3F} class.
	 */
	public static final String NAME = "Triangle";
	
	/**
	 * The length of the {@code float[]}.
	 */
	public static final int ARRAY_LENGTH = 24;
	
	/**
	 * The offset for the {@link Vector3F} instance representing the W-direction of the {@link OrthonormalBasis33F} of {@link Vertex3F} {@code A} in the {@code float[]}.
	 */
	public static final int ARRAY_OFFSET_A_ORTHONORMAL_BASIS_W = 15;
	
	/**
	 * The offset for the {@link Point3F} instance representing the position of {@link Vertex3F} {@code A} in the {@code float[]}.
	 */
	public static final int ARRAY_OFFSET_A_POSITION = 0;
	
	/**
	 * The offset for the {@link Point2F} instance representing the texture coordinates of {@link Vertex3F} {@code A} in the {@code float[]}.
	 */
	public static final int ARRAY_OFFSET_A_TEXTURE_COORDINATES = 9;
	
	/**
	 * The offset for the {@link Vector3F} instance representing the W-direction of the {@link OrthonormalBasis33F} of {@link Vertex3F} {@code B} in the {@code float[]}.
	 */
	public static final int ARRAY_OFFSET_B_ORTHONORMAL_BASIS_W = 18;
	
	/**
	 * The offset for the {@link Point3F} instance representing the position of {@link Vertex3F} {@code B} in the {@code float[]}.
	 */
	public static final int ARRAY_OFFSET_B_POSITION = 3;
	
	/**
	 * The offset for the {@link Point2F} instance representing the texture coordinates of {@link Vertex3F} {@code B} in the {@code float[]}.
	 */
	public static final int ARRAY_OFFSET_B_TEXTURE_COORDINATES = 11;
	
	/**
	 * The offset for the {@link Vector3F} instance representing the W-direction of the {@link OrthonormalBasis33F} of {@link Vertex3F} {@code C} in the {@code float[]}.
	 */
	public static final int ARRAY_OFFSET_C_ORTHONORMAL_BASIS_W = 21;
	
	/**
	 * The offset for the {@link Point3F} instance representing the position of {@link Vertex3F} {@code C} in the {@code float[]}.
	 */
	public static final int ARRAY_OFFSET_C_POSITION = 6;
	
	/**
	 * The offset for the {@link Point2F} instance representing the texture coordinates of {@link Vertex3F} {@code C} in the {@code float[]}.
	 */
	public static final int ARRAY_OFFSET_C_TEXTURE_COORDINATES = 13;
	
	/**
	 * The ID of this {@code Triangle3F} class.
	 */
	public static final int ID = 15;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final Vector3F surfaceNormal;
	private final Vertex3F a;
	private final Vertex3F b;
	private final Vertex3F c;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code Triangle3F} instance.
	 */
	public Triangle3F() {
		this.a = new Vertex3F(new Point2F(0.5F, 0.0F), new Point4F(+0.0F, +1.0F, 0.0F), Vector3F.normalNormalized(new Point3F(0.0F, 1.0F, 0.0F), new Point3F(1.0F, -1.0F, 0.0F), new Point3F(-1.0F, -1.0F, 0.0F)));
		this.b = new Vertex3F(new Point2F(1.0F, 1.0F), new Point4F(+1.0F, -1.0F, 0.0F), Vector3F.normalNormalized(new Point3F(0.0F, 1.0F, 0.0F), new Point3F(1.0F, -1.0F, 0.0F), new Point3F(-1.0F, -1.0F, 0.0F)));
		this.c = new Vertex3F(new Point2F(0.0F, 1.0F), new Point4F(-1.0F, -1.0F, 0.0F), Vector3F.normalNormalized(new Point3F(0.0F, 1.0F, 0.0F), new Point3F(1.0F, -1.0F, 0.0F), new Point3F(-1.0F, -1.0F, 0.0F)));
		this.surfaceNormal = Vector3F.getCached(Vector3F.normalNormalized(new Point3F(this.a.getPosition()), new Point3F(this.b.getPosition()), new Point3F(this.c.getPosition())));
	}
	
	/**
	 * Constructs a new {@code Triangle3F} instance.
	 * <p>
	 * If either {@code a}, {@code b} or {@code c} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param a a {@link Vertex3F} instance denoted by {@code A}
	 * @param b a {@code Vertex3F} instance denoted by {@code B}
	 * @param c a {@code Vertex3F} instance denoted by {@code C}
	 * @throws NullPointerException thrown if, and only if, either {@code a}, {@code b} or {@code c} are {@code null}
	 */
	public Triangle3F(final Vertex3F a, final Vertex3F b, final Vertex3F c) {
		this.a = Objects.requireNonNull(a, "a == null");
		this.b = Objects.requireNonNull(b, "b == null");
		this.c = Objects.requireNonNull(c, "c == null");
		this.surfaceNormal = Vector3F.getCached(Vector3F.normalNormalized(new Point3F(a.getPosition()), new Point3F(b.getPosition()), new Point3F(c.getPosition())));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link BoundingVolume3F} instance that contains this {@code Triangle3F} instance.
	 * 
	 * @return a {@code BoundingVolume3F} instance that contains this {@code Triangle3F} instance
	 */
	@Override
	public BoundingVolume3F getBoundingVolume() {
		final Point3F a = Point3F.minimum(new Point3F(this.a.getPosition()), new Point3F(this.b.getPosition()), new Point3F(this.c.getPosition()));
		final Point3F b = Point3F.maximum(new Point3F(this.a.getPosition()), new Point3F(this.b.getPosition()), new Point3F(this.c.getPosition()));
		
		return new AxisAlignedBoundingBox3F(a, b);
	}
	
	/**
	 * Samples this {@code Triangle3F} instance.
	 * <p>
	 * Returns an optional {@link SurfaceSample3F} with the surface sample.
	 * <p>
	 * If {@code sample} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param sample a {@link Point2F} instance with a sample point
	 * @return an optional {@code SurfaceSample3F} with the surface sample
	 * @throws NullPointerException thrown if, and only if, {@code sample} is {@code null}
	 */
	@Override
	public Optional<SurfaceSample3F> sample(final Point2F sample) {
		Objects.requireNonNull(sample, "sample == null");
		
		final Point3F barycentricCoordinates = SampleGeneratorF.sampleTriangleUniformDistribution(sample.getU(), sample.getV());
		
		final Point4F positionA = this.a.getPosition();
		final Point4F positionB = this.b.getPosition();
		final Point4F positionC = this.c.getPosition();
		
		final Vector3F normalA = this.a.getOrthonormalBasis().getW();
		final Vector3F normalB = this.b.getOrthonormalBasis().getW();
		final Vector3F normalC = this.c.getOrthonormalBasis().getW();
		
		final float x = positionA.getX() * barycentricCoordinates.getX() + positionB.getX() * barycentricCoordinates.getY() + positionC.getX() * barycentricCoordinates.getZ();
		final float y = positionA.getY() * barycentricCoordinates.getX() + positionB.getY() * barycentricCoordinates.getY() + positionC.getY() * barycentricCoordinates.getZ();
		final float z = positionA.getZ() * barycentricCoordinates.getX() + positionB.getZ() * barycentricCoordinates.getY() + positionC.getZ() * barycentricCoordinates.getZ();
		
		final Point3F point = new Point3F(x, y, z);
		
		final Vector3F surfaceNormal = Vector3F.normalNormalized(normalA, normalB, normalC, barycentricCoordinates);
		
		final float pointErrorX = (abs(positionA.getX() * barycentricCoordinates.getX()) + abs(positionB.getX() * barycentricCoordinates.getY()) + abs(positionC.getX() * barycentricCoordinates.getZ())) * gamma(6);
		final float pointErrorY = (abs(positionA.getY() * barycentricCoordinates.getX()) + abs(positionB.getY() * barycentricCoordinates.getY()) + abs(positionC.getY() * barycentricCoordinates.getZ())) * gamma(6);
		final float pointErrorZ = (abs(positionA.getZ() * barycentricCoordinates.getX()) + abs(positionB.getZ() * barycentricCoordinates.getY()) + abs(positionC.getZ() * barycentricCoordinates.getZ())) * gamma(6);
		
		final Vector3F pointError = new Vector3F(pointErrorX, pointErrorY, pointErrorZ);
		
		final float probabilityDensityFunctionValue = 1.0F / getSurfaceArea();
		
		return Optional.of(new SurfaceSample3F(point, pointError, surfaceNormal, probabilityDensityFunctionValue));
	}
	
	/**
	 * Performs an intersection test between {@code ray} and this {@code Triangle3F} instance.
	 * <p>
	 * Returns an {@code Optional} with an optional {@link SurfaceIntersection3F} instance that contains information about the intersection, if it was found.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3F} to perform an intersection test against this {@code Triangle3F} instance
	 * @param tMinimum the minimum parametric distance
	 * @param tMaximum the maximum parametric distance
	 * @return an {@code Optional} with an optional {@code SurfaceIntersection3F} instance that contains information about the intersection, if it was found
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	@Override
	public Optional<SurfaceIntersection3F> intersection(final Ray3F ray, final float tMinimum, final float tMaximum) {
//		return doIntersectionOld(ray, tMinimum, tMaximum);
//		return doIntersectionPBRT(ray, tMinimum, tMaximum);
		return doIntersectionSunflow(ray, tMinimum, tMaximum);
	}
	
	/**
	 * Returns a {@code String} with the name of this {@code Triangle3F} instance.
	 * 
	 * @return a {@code String} with the name of this {@code Triangle3F} instance
	 */
	@Override
	public String getName() {
		return NAME;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code Triangle3F} instance.
	 * 
	 * @return a {@code String} representation of this {@code Triangle3F} instance
	 */
	@Override
	public String toString() {
		return String.format("new Triangle3F(%s, %s, %s)", this.a, this.b, this.c);
	}
	
	/**
	 * Returns the surface normal associated with this {@code Triangle3F} instance.
	 * 
	 * @return the surface normal associated with this {@code Triangle3F} instance
	 */
	public Vector3F getSurfaceNormal() {
		return this.surfaceNormal;
	}
	
	/**
	 * Returns the vertex denoted by {@code A} and is associated with this {@code Triangle3F} instance.
	 * 
	 * @return the vertex denoted by {@code A} and is associated with this {@code Triangle3F} instance
	 */
	public Vertex3F getA() {
		return this.a;
	}
	
	/**
	 * Returns the vertex denoted by {@code B} and is associated with this {@code Triangle3F} instance.
	 * 
	 * @return the vertex denoted by {@code B} and is associated with this {@code Triangle3F} instance
	 */
	public Vertex3F getB() {
		return this.b;
	}
	
	/**
	 * Returns the vertex denoted by {@code C} and is associated with this {@code Triangle3F} instance.
	 * 
	 * @return the vertex denoted by {@code C} and is associated with this {@code Triangle3F} instance
	 */
	public Vertex3F getC() {
		return this.c;
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
				if(!this.surfaceNormal.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
				
				if(!this.a.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
				
				if(!this.b.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
				
				if(!this.c.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
			}
			
			return nodeHierarchicalVisitor.visitLeave(this);
		} catch(final RuntimeException e) {
			throw new NodeTraversalException(e);
		}
	}
	
	/**
	 * Compares {@code object} to this {@code Triangle3F} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code Triangle3F}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code Triangle3F} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code Triangle3F}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof Triangle3F)) {
			return false;
		} else if(!Objects.equals(this.surfaceNormal, Triangle3F.class.cast(object).surfaceNormal)) {
			return false;
		} else if(!Objects.equals(this.a, Triangle3F.class.cast(object).a)) {
			return false;
		} else if(!Objects.equals(this.b, Triangle3F.class.cast(object).b)) {
			return false;
		} else if(!Objects.equals(this.c, Triangle3F.class.cast(object).c)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns the surface area of this {@code Triangle3F} instance.
	 * 
	 * @return the surface area of this {@code Triangle3F} instance
	 */
	@Override
	public float getSurfaceArea() {
		return getSurfaceAreaSquared() * 0.5F;
	}
	
	/**
	 * Returns the squared surface area of this {@code Triangle3F} instance.
	 * 
	 * @return the squared surface area of this {@code Triangle3F} instance
	 */
	public float getSurfaceAreaSquared() {
		final Point4F a = this.a.getPosition();
		final Point4F b = this.b.getPosition();
		final Point4F c = this.c.getPosition();
		
		final Vector3F edgeAB = Vector3F.direction(a, b);
		final Vector3F edgeAC = Vector3F.direction(a, c);
		final Vector3F edgeABCrossEdgeAC = Vector3F.crossProduct(edgeAB, edgeAC);
		
		return edgeABCrossEdgeAC.length();
	}
	
	/**
	 * Performs an intersection test between {@code ray} and this {@code Triangle3F} instance.
	 * <p>
	 * Returns {@code t}, the parametric distance to the surface intersection point, or {@code Float.NaN} if no intersection exists.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3F} to perform an intersection test against this {@code Triangle3F} instance
	 * @param tMinimum the minimum parametric distance
	 * @param tMaximum the maximum parametric distance
	 * @return {@code t}, the parametric distance to the surface intersection point, or {@code Float.NaN} if no intersection exists
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	@Override
	public float intersectionT(final Ray3F ray, final float tMinimum, final float tMaximum) {
//		return doIntersectionTOld(ray, tMinimum, tMaximum);
//		return doIntersectionTPBRT(ray, tMinimum, tMaximum);
		return doIntersectionTSunflow(ray, tMinimum, tMaximum);
	}
	
	/**
	 * Returns a {@code float[]} representation of this {@code Triangle3F} instance.
	 * 
	 * @return a {@code float[]} representation of this {@code Triangle3F} instance
	 */
	public float[] toArray() {
		final float[] array = new float[ARRAY_LENGTH];
		
		array[ARRAY_OFFSET_A_POSITION + 0] = this.a.getPosition().getX();								//Block #1
		array[ARRAY_OFFSET_A_POSITION + 1] = this.a.getPosition().getY();								//Block #1
		array[ARRAY_OFFSET_A_POSITION + 2] = this.a.getPosition().getZ();								//Block #1
		array[ARRAY_OFFSET_B_POSITION + 0] = this.b.getPosition().getX();								//Block #1
		array[ARRAY_OFFSET_B_POSITION + 1] = this.b.getPosition().getY();								//Block #1
		array[ARRAY_OFFSET_B_POSITION + 2] = this.b.getPosition().getZ();								//Block #1
		array[ARRAY_OFFSET_C_POSITION + 0] = this.c.getPosition().getX();								//Block #1
		array[ARRAY_OFFSET_C_POSITION + 1] = this.c.getPosition().getY();								//Block #1
		array[ARRAY_OFFSET_C_POSITION + 2] = this.c.getPosition().getZ();								//Block #2
		
		array[ARRAY_OFFSET_A_TEXTURE_COORDINATES + 0] = this.a.getTextureCoordinates().getU();			//Block #2
		array[ARRAY_OFFSET_A_TEXTURE_COORDINATES + 1] = this.a.getTextureCoordinates().getV();			//Block #2
		array[ARRAY_OFFSET_B_TEXTURE_COORDINATES + 0] = this.b.getTextureCoordinates().getU();			//Block #2
		array[ARRAY_OFFSET_B_TEXTURE_COORDINATES + 1] = this.b.getTextureCoordinates().getV();			//Block #2
		array[ARRAY_OFFSET_C_TEXTURE_COORDINATES + 0] = this.c.getTextureCoordinates().getU();			//Block #2
		array[ARRAY_OFFSET_C_TEXTURE_COORDINATES + 1] = this.c.getTextureCoordinates().getV();			//Block #2
		
		array[ARRAY_OFFSET_A_ORTHONORMAL_BASIS_W + 0] = this.a.getOrthonormalBasis().getW().getX();		//Block #2
		array[ARRAY_OFFSET_A_ORTHONORMAL_BASIS_W + 1] = this.a.getOrthonormalBasis().getW().getY();		//Block #3
		array[ARRAY_OFFSET_A_ORTHONORMAL_BASIS_W + 2] = this.a.getOrthonormalBasis().getW().getZ();		//Block #3
		array[ARRAY_OFFSET_B_ORTHONORMAL_BASIS_W + 0] = this.b.getOrthonormalBasis().getW().getX();		//Block #3
		array[ARRAY_OFFSET_B_ORTHONORMAL_BASIS_W + 1] = this.b.getOrthonormalBasis().getW().getY();		//Block #3
		array[ARRAY_OFFSET_B_ORTHONORMAL_BASIS_W + 2] = this.b.getOrthonormalBasis().getW().getZ();		//Block #3
		array[ARRAY_OFFSET_C_ORTHONORMAL_BASIS_W + 0] = this.c.getOrthonormalBasis().getW().getX();		//Block #3
		array[ARRAY_OFFSET_C_ORTHONORMAL_BASIS_W + 1] = this.c.getOrthonormalBasis().getW().getY();		//Block #3
		array[ARRAY_OFFSET_C_ORTHONORMAL_BASIS_W + 2] = this.c.getOrthonormalBasis().getW().getZ();		//Block #3
		
		return array;
	}
	
	/**
	 * Returns an {@code int} with the ID of this {@code Triangle3F} instance.
	 * 
	 * @return an {@code int} with the ID of this {@code Triangle3F} instance
	 */
	@Override
	public int getID() {
		return ID;
	}
	
	/**
	 * Returns a hash code for this {@code Triangle3F} instance.
	 * 
	 * @return a hash code for this {@code Triangle3F} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.surfaceNormal, this.a, this.b, this.c);
	}
	
	/**
	 * Writes this {@code Triangle3F} instance to {@code dataOutput}.
	 * <p>
	 * If {@code dataOutput} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * 
	 * @param dataOutput the {@code DataOutput} instance to write to
	 * @throws NullPointerException thrown if, and only if, {@code dataOutput} is {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
	@Override
	public void write(final DataOutput dataOutput) {
		try {
			dataOutput.writeInt(ID);
			
			this.a.write(dataOutput);
			this.b.write(dataOutput);
			this.c.write(dataOutput);
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * A {@code Vertex3F} denotes a vertex of a {@link Triangle3F} instance.
	 * <p>
	 * This class is immutable and therefore thread-safe.
	 * 
	 * @since 1.0.0
	 * @author J&#246;rgen Lundgren
	 */
	public static final class Vertex3F implements Node {
		private final OrthonormalBasis33F orthonormalBasis;
		private final Point2F textureCoordinates;
		private final Point4F position;
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		/**
		 * Constructs a new {@code Vertex3F} instance.
		 * <p>
		 * If either {@code textureCoordinates}, {@code position} or {@code orthonormalBasis} are {@code null}, a {@code NullPointerException} will be thrown.
		 * 
		 * @param textureCoordinates the texture coordinates associated with this {@code Vertex3F} instance
		 * @param position the position associated with this {@code Vertex3F} instance
		 * @param orthonormalBasis the orthonormal basis associated with this {@code Vertex3F} instance
		 * @throws NullPointerException thrown if, and only if, either {@code textureCoordinates}, {@code position} or {@code orthonormalBasis} are {@code null}
		 */
		public Vertex3F(final Point2F textureCoordinates, final Point4F position, final OrthonormalBasis33F orthonormalBasis) {
			this.textureCoordinates = Objects.requireNonNull(textureCoordinates, "textureCoordinates == null");
			this.position = Point4F.getCached(Objects.requireNonNull(position, "position == null"));
			this.orthonormalBasis = OrthonormalBasis33F.getCached(Objects.requireNonNull(orthonormalBasis, "orthonormalBasis == null"));
		}
		
		/**
		 * Constructs a new {@code Vertex3F} instance.
		 * <p>
		 * If either {@code textureCoordinates}, {@code position} or {@code w} are {@code null}, a {@code NullPointerException} will be thrown.
		 * 
		 * @param textureCoordinates the texture coordinates associated with this {@code Vertex3F} instance
		 * @param position the position associated with this {@code Vertex3F} instance
		 * @param w the W-direction associated with this {@code Vertex3F} instance
		 * @throws NullPointerException thrown if, and only if, either {@code textureCoordinates}, {@code position} or {@code w} are {@code null}
		 */
		public Vertex3F(final Point2F textureCoordinates, final Point4F position, final Vector3F w) {
			this.textureCoordinates = Objects.requireNonNull(textureCoordinates, "textureCoordinates == null");
			this.position = Point4F.getCached(Objects.requireNonNull(position, "position == null"));
			this.orthonormalBasis = OrthonormalBasis33F.getCached(new OrthonormalBasis33F(Objects.requireNonNull(w, "w == null")));
		}
		
		/**
		 * Constructs a new {@code Vertex3F} instance.
		 * <p>
		 * If either {@code textureCoordinates}, {@code position}, {@code w} or {@code v} are {@code null}, a {@code NullPointerException} will be thrown.
		 * 
		 * @param textureCoordinates the texture coordinates associated with this {@code Vertex3F} instance
		 * @param position the position associated with this {@code Vertex3F} instance
		 * @param w the W-direction associated with this {@code Vertex3F} instance
		 * @param v the V-direction associated with this {@code Vertex3F} instance
		 * @throws NullPointerException thrown if, and only if, either {@code textureCoordinates}, {@code position}, {@code w} or {@code v} are {@code null}
		 */
		public Vertex3F(final Point2F textureCoordinates, final Point4F position, final Vector3F w, final Vector3F v) {
			this.textureCoordinates = Objects.requireNonNull(textureCoordinates, "textureCoordinates == null");
			this.position = Point4F.getCached(Objects.requireNonNull(position, "position == null"));
			this.orthonormalBasis = OrthonormalBasis33F.getCached(new OrthonormalBasis33F(Objects.requireNonNull(w, "w == null"), Objects.requireNonNull(v, "v == null")));
		}
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		/**
		 * Returns the orthonormal basis associated with this {@code Vertex3F} instance.
		 * 
		 * @return the orthonormal basis associated with this {@code Vertex3F} instance
		 */
		public OrthonormalBasis33F getOrthonormalBasis() {
			return this.orthonormalBasis;
		}
		
		/**
		 * Returns the texture coordinates associated with this {@code Vertex3F} instance.
		 * 
		 * @return the texture coordinates associated with this {@code Vertex3F} instance
		 */
		public Point2F getTextureCoordinates() {
			return this.textureCoordinates;
		}
		
		/**
		 * Returns the position associated with this {@code Vertex3F} instance.
		 * 
		 * @return the position associated with this {@code Vertex3F} instance
		 */
		public Point4F getPosition() {
			return this.position;
		}
		
		/**
		 * Returns a {@code String} representation of this {@code Vertex3F} instance.
		 * 
		 * @return a {@code String} representation of this {@code Vertex3F} instance
		 */
		@Override
		public String toString() {
			return String.format("new Vertex3F(%s, %s, %s)", this.textureCoordinates, this.position, this.orthonormalBasis);
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
					if(!this.orthonormalBasis.accept(nodeHierarchicalVisitor)) {
						return nodeHierarchicalVisitor.visitLeave(this);
					}
					
					if(!this.textureCoordinates.accept(nodeHierarchicalVisitor)) {
						return nodeHierarchicalVisitor.visitLeave(this);
					}
					
					if(!this.position.accept(nodeHierarchicalVisitor)) {
						return nodeHierarchicalVisitor.visitLeave(this);
					}
				}
				
				return nodeHierarchicalVisitor.visitLeave(this);
			} catch(final RuntimeException e) {
				throw new NodeTraversalException(e);
			}
		}
		
		/**
		 * Compares {@code object} to this {@code Vertex3F} instance for equality.
		 * <p>
		 * Returns {@code true} if, and only if, {@code object} is an instance of {@code Vertex3F}, and their respective values are equal, {@code false} otherwise.
		 * 
		 * @param object the {@code Object} to compare to this {@code Vertex3F} instance for equality
		 * @return {@code true} if, and only if, {@code object} is an instance of {@code Vertex3F}, and their respective values are equal, {@code false} otherwise
		 */
		@Override
		public boolean equals(final Object object) {
			if(object == this) {
				return true;
			} else if(!(object instanceof Vertex3F)) {
				return false;
			} else if(!Objects.equals(this.orthonormalBasis, Vertex3F.class.cast(object).orthonormalBasis)) {
				return false;
			} else if(!Objects.equals(this.textureCoordinates, Vertex3F.class.cast(object).textureCoordinates)) {
				return false;
			} else if(!Objects.equals(this.position, Vertex3F.class.cast(object).position)) {
				return false;
			} else {
				return true;
			}
		}
		
		/**
		 * Returns a hash code for this {@code Vertex3F} instance.
		 * 
		 * @return a hash code for this {@code Vertex3F} instance
		 */
		@Override
		public int hashCode() {
			return Objects.hash(this.orthonormalBasis, this.textureCoordinates, this.position);
		}
		
		/**
		 * Writes this {@code Vertex3F} instance to {@code dataOutput}.
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
			this.textureCoordinates.write(dataOutput);
			this.position.write(dataOutput);
			this.orthonormalBasis.write(dataOutput);
		}
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		/**
		 * Performs a linear interpolation operation on the supplied values.
		 * <p>
		 * Returns a {@code Vertex3F} instance with the result of the linear interpolation operation.
		 * <p>
		 * If either {@code a} or {@code b} are {@code null}, a {@code NullPointerException} will be thrown.
		 * 
		 * @param a a {@code Vertex3F} instance
		 * @param b a {@code Vertex3F} instance
		 * @param t the factor
		 * @return a {@code Vertex3F} instance with the result of the linear interpolation operation
		 * @throws NullPointerException thrown if, and only if, either {@code a} or {@code b} are {@code null}
		 */
		public static Vertex3F lerp(final Vertex3F a, final Vertex3F b, final float t) {
			final Point2F textureCoordinates = Point2F.lerp(a.textureCoordinates, b.textureCoordinates, t);
			
			final Point4F position = Point4F.lerp(a.position, b.position, t);
			
			final OrthonormalBasis33F orthonormalBasis = OrthonormalBasis33F.lerp(a.orthonormalBasis, b.orthonormalBasis, t);
			
			return new Vertex3F(textureCoordinates, position, orthonormalBasis);
		}
		
		/**
		 * Performs a transformation.
		 * <p>
		 * Returns a new {@code Vertex3F} instance with the result of the transformation.
		 * <p>
		 * If either {@code vertex}, {@code matrix} or {@code matrixInverse} are {@code null}, a {@code NullPointerException} will be thrown.
		 * 
		 * @param vertex the {@code Vertex3F} instance to transform
		 * @param matrix the {@link Matrix44F} instance to perform the transformation with
		 * @param matrixInverse the {@code Matrix44F} instance to perform the inverse transformation with
		 * @return a new {@code Vertex3F} instance with the result of the transformation
		 * @throws NullPointerException thrown if, and only if, either {@code vertex}, {@code matrix} or {@code matrixInverse} are {@code null}
		 */
		public static Vertex3F transform(final Vertex3F vertex, final Matrix44F matrix, final Matrix44F matrixInverse) {
			final Point2F textureCoordinates = vertex.textureCoordinates;
			
			final Point4F position = Point4F.transform(matrix, vertex.position);
			
			final OrthonormalBasis33F orthonormalBasis = OrthonormalBasis33F.transform(matrixInverse, vertex.orthonormalBasis);
			
			return new Vertex3F(textureCoordinates, position, orthonormalBasis);
		}
		
		/**
		 * Performs a transformation.
		 * <p>
		 * Returns a new {@code Vertex3F} instance with the result of the transformation.
		 * <p>
		 * If either {@code vertex}, {@code matrix} or {@code matrixInverse} are {@code null}, a {@code NullPointerException} will be thrown.
		 * 
		 * @param vertex the {@code Vertex3F} instance to transform
		 * @param matrix the {@link Matrix44F} instance to perform the transformation with
		 * @param matrixInverse the {@code Matrix44F} instance to perform the inverse transformation with
		 * @return a new {@code Vertex3F} instance with the result of the transformation
		 * @throws NullPointerException thrown if, and only if, either {@code vertex}, {@code matrix} or {@code matrixInverse} are {@code null}
		 */
		public static Vertex3F transformAndDivide(final Vertex3F vertex, final Matrix44F matrix, final Matrix44F matrixInverse) {
			final Point2F textureCoordinates = vertex.textureCoordinates;
			
			final Point4F position = Point4F.transformAndDivide(matrix, vertex.position);
			
			final OrthonormalBasis33F orthonormalBasis = OrthonormalBasis33F.transform(matrixInverse, vertex.orthonormalBasis);
			
			return new Vertex3F(textureCoordinates, position, orthonormalBasis);
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@SuppressWarnings("unused")
	private Optional<SurfaceIntersection3F> doIntersectionOld(final Ray3F ray, final float tMinimum, final float tMaximum) {
		final Point4F a = this.a.getPosition();
		final Point4F b = this.b.getPosition();
		final Point4F c = this.c.getPosition();
		
		final Vector3F edgeAB = Vector3F.direction(a, b);
		final Vector3F edgeAC = Vector3F.direction(a, c);
		final Vector3F direction0 = ray.getDirection();
		final Vector3F direction1 = Vector3F.crossProduct(direction0, edgeAC);
		
		final float determinant = Vector3F.dotProduct(edgeAB, direction1);
		
		if(determinant >= -0.0001F && determinant <= 0.0001F) {
			return SurfaceIntersection3F.EMPTY;
		}
		
		final Point3F origin = ray.getOrigin();
		
		final Vector3F direction2 = Vector3F.direction(new Point3F(a), origin);
		
		final float determinantReciprocal = 1.0F / determinant;
		final float u = Vector3F.dotProduct(direction2, direction1) * determinantReciprocal;
		
		if(u < 0.0F || u > 1.0F) {
			return SurfaceIntersection3F.EMPTY;
		}
		
		final Vector3F direction3 = Vector3F.crossProduct(direction2, edgeAB);
		
		final float v = Vector3F.dotProduct(direction0, direction3) * determinantReciprocal;
		
		if(v < 0.0F || u + v > 1.0F) {
			return SurfaceIntersection3F.EMPTY;
		}
		
		final float t = Vector3F.dotProduct(edgeAC, direction3) * determinantReciprocal;
		
		if(t <= tMinimum || t >= tMaximum) {
			return SurfaceIntersection3F.EMPTY;
		}
		
		final float w = 1.0F - u - v;
		
		final Point3F barycentricCoordinates = new Point3F(w, u, v);
		final Point3F surfaceIntersectionPoint = Point3F.add(origin, direction0, t);
		
		final Point2F textureCoordinates = Point2F.createTextureCoordinates(this.a.getTextureCoordinates(), this.b.getTextureCoordinates(), this.c.getTextureCoordinates(), barycentricCoordinates);
		
		final OrthonormalBasis33F aOrthonormalBasis = this.a.getOrthonormalBasis();
		final OrthonormalBasis33F bOrthonormalBasis = this.b.getOrthonormalBasis();
		final OrthonormalBasis33F cOrthonormalBasis = this.c.getOrthonormalBasis();
		
		final Vector3F gW = this.surfaceNormal;
		final Vector3F sW = Vector3F.normalNormalized(aOrthonormalBasis.getW(), bOrthonormalBasis.getW(), cOrthonormalBasis.getW(), barycentricCoordinates);
		
		final Vector3F gV = Vector3F.directionNormalized(a, b);
		final Vector3F sV = Vector3F.normalNormalized(aOrthonormalBasis.getV(), bOrthonormalBasis.getV(), cOrthonormalBasis.getV(), barycentricCoordinates);
		
//		final Vector3F gU = Vector3F.directionNormalized(a, c);
//		final Vector3F sU = Vector3F.normalNormalized(aOrthonormalBasis.getU(), bOrthonormalBasis.getU(), cOrthonormalBasis.getU(), barycentricCoordinates);
		
		final OrthonormalBasis33F orthonormalBasisG = new OrthonormalBasis33F(gW, gV);
		final OrthonormalBasis33F orthonormalBasisS = new OrthonormalBasis33F(sW, sV);
//		final OrthonormalBasis33F orthonormalBasisG = new OrthonormalBasis33F(gW, gV, gU);
//		final OrthonormalBasis33F orthonormalBasisS = new OrthonormalBasis33F(sW, sV, sU);
		
		final float xAbsSum = abs(barycentricCoordinates.getU() + a.getX()) + abs(barycentricCoordinates.getV() + b.getX()) + abs(barycentricCoordinates.getW() + c.getX());
		final float yAbsSum = abs(barycentricCoordinates.getU() + a.getY()) + abs(barycentricCoordinates.getV() + b.getY()) + abs(barycentricCoordinates.getW() + c.getY());
		final float zAbsSum = abs(barycentricCoordinates.getU() + a.getZ()) + abs(barycentricCoordinates.getV() + b.getZ()) + abs(barycentricCoordinates.getW() + c.getZ());
		
		final Vector3F surfaceIntersectionPointError = Vector3F.multiply(new Vector3F(xAbsSum, yAbsSum, zAbsSum), gamma(7));
		
		return Optional.of(new SurfaceIntersection3F(orthonormalBasisG, orthonormalBasisS, textureCoordinates, surfaceIntersectionPoint, ray, this, surfaceIntersectionPointError, t));
	}
	
	@SuppressWarnings("unused")
	private Optional<SurfaceIntersection3F> doIntersectionPBRT(final Ray3F ray, final float tMinimum, final float tMaximum) {
		final Point3F p0 = new Point3F(this.a.getPosition());
		final Point3F p1 = new Point3F(this.b.getPosition());
		final Point3F p2 = new Point3F(this.c.getPosition());
		
		final int kZ = doMaxDimension(Vector3F.absolute(ray.getDirection()));
		final int kX = kZ == 2 ? 0 : kZ + 1;
		final int kY = kX == 2 ? 0 : kX + 1;
		
		final Vector3F d = doPermute(ray.getDirection(), kX, kY, kZ);
		
		final float shearX = -d.getX() / d.getZ();
		final float shearY = -d.getY() / d.getZ();
		final float shearZ = 1.0F / d.getZ();
		
		final Point3F p0T = doShear(doPermute(Point3F.subtract(p0, new Vector3F(ray.getOrigin())), kX, kY, kZ), shearX, shearY, shearZ);
		final Point3F p1T = doShear(doPermute(Point3F.subtract(p1, new Vector3F(ray.getOrigin())), kX, kY, kZ), shearX, shearY, shearZ);
		final Point3F p2T = doShear(doPermute(Point3F.subtract(p2, new Vector3F(ray.getOrigin())), kX, kY, kZ), shearX, shearY, shearZ);
		
		float e0 = p1T.getX() * p2T.getY() - p1T.getY() * p2T.getX();
		float e1 = p2T.getX() * p0T.getY() - p2T.getY() * p0T.getX();
		float e2 = p0T.getX() * p1T.getY() - p0T.getY() * p1T.getX();
		
		if(isZero(e0) || isZero(e1) || isZero(e2)) {
			final double p2TXP1TY = (double)(p2T.getX()) * (double)(p1T.getY());
			final double p2TYP1TX = (double)(p2T.getY()) * (double)(p1T.getX());
			final double p0TXP2TY = (double)(p0T.getX()) * (double)(p2T.getY());
			final double p0TYP2TX = (double)(p0T.getY()) * (double)(p2T.getX());
			final double p1TXP0TY = (double)(p1T.getX()) * (double)(p0T.getY());
			final double p1TYP0TX = (double)(p1T.getY()) * (double)(p0T.getX());
			
			e0 = (float)(p2TYP1TX - p2TXP1TY);
			e1 = (float)(p0TYP2TX - p0TXP2TY);
			e2 = (float)(p1TYP0TX - p1TXP0TY);
		}
		
		if((e0 < 0.0F || e1 < 0.0F || e2 < 0.0F) && (e0 > 0.0F || e1 > 0.0F || e2 > 0.0F)) {
			return SurfaceIntersection3F.EMPTY;
		}
		
		final float det = e0 + e1 + e2;
		
		if(isZero(det)) {
			return SurfaceIntersection3F.EMPTY;
		}
		
		final float tScaled = e0 * p0T.getZ() + e1 * p1T.getZ() + e2 * p2T.getZ();
		
		if(det < 0.0F && (tScaled >= 0.0F || tScaled < tMaximum * det)) {
			return SurfaceIntersection3F.EMPTY;
		} else if(det > 0.0F && (tScaled <= 0.0F || tScaled > tMaximum * det)) {
			return SurfaceIntersection3F.EMPTY;
		}
		
		final float invDet = 1.0F / det;
		
		final float b0 = e0 * invDet;
		final float b1 = e1 * invDet;
		final float b2 = e2 * invDet;
		
		final float t = tScaled * invDet;
		
		final float maxE = max(abs(e0), abs(e1), abs(e2));
		
		final float maxXT = max(abs(p0T.getX()), abs(p1T.getX()), abs(p2T.getX()));
		final float maxYT = max(abs(p0T.getY()), abs(p1T.getY()), abs(p2T.getY()));
		final float maxZT = max(abs(p0T.getZ()), abs(p1T.getZ()), abs(p2T.getZ()));
		
		final float deltaX = gamma(5) * (maxXT + maxZT);
		final float deltaY = gamma(5) * (maxYT + maxZT);
		final float deltaZ = gamma(3) * maxZT;
		final float deltaE = 2.0F * (gamma(2) * maxXT * maxYT + deltaY * maxXT + deltaX * maxYT);
		final float deltaT = 3.0F * (gamma(3) * maxE * maxZT + deltaE * maxZT + deltaZ * maxE) * abs(invDet);
		
		if(t <= deltaT) {
			return SurfaceIntersection3F.EMPTY;
		}
		
		final Point2F textureCoordinates0 = this.a.getTextureCoordinates();
		final Point2F textureCoordinates1 = this.b.getTextureCoordinates();
		final Point2F textureCoordinates2 = this.c.getTextureCoordinates();
		
		final Vector2F dUV02 = Vector2F.direction(textureCoordinates2, textureCoordinates0);
		final Vector2F dUV12 = Vector2F.direction(textureCoordinates2, textureCoordinates1);
		
		final Vector3F dP02 = Vector3F.direction(p2, p0);
		final Vector3F dP12 = Vector3F.direction(p2, p1);
		
		final float determinant = dUV02.getX() * dUV12.getY() - dUV02.getY() * dUV12.getX();
		
		final boolean hasDegenerateUVs = abs(determinant) < 1.0e-8F;
		
		Vector3F dPDU = new Vector3F();
		Vector3F dPDV = new Vector3F();
		
		if(!hasDegenerateUVs) {
			final float invDeterminant = 1.0F / determinant;
			
			dPDU = Vector3F.multiply(Vector3F.subtract(Vector3F.multiply(dP02, dUV12.getY()), Vector3F.multiply(dP12, dUV02.getY())), invDeterminant);
			dPDV = Vector3F.multiply(Vector3F.add(Vector3F.multiply(dP02, -dUV12.getX()), Vector3F.multiply(dP12, dUV02.getX())), invDeterminant);
		}
		
		if(hasDegenerateUVs || isZero(Vector3F.crossProduct(dPDU, dPDV).lengthSquared())) {
			final Vector3F ng = Vector3F.crossProduct(Vector3F.direction(p0, p2), Vector3F.direction(p0, p1));
			
			if(isZero(ng.lengthSquared())) {
				return SurfaceIntersection3F.EMPTY;
			}
			
			final OrthonormalBasis33F orthonormalBasis = OrthonormalBasis33F.coordinateSystem(Vector3F.normalize(ng));
			
			dPDU = orthonormalBasis.getU();
			dPDV = orthonormalBasis.getV();
		}
		
		final float xAbsSum = abs(b0 * p0.getX()) + abs(b1 * p1.getX()) + abs(b2 * p2.getX());
		final float yAbsSum = abs(b0 * p0.getY()) + abs(b1 * p1.getY()) + abs(b2 * p2.getY());
		final float zAbsSum = abs(b0 * p0.getZ()) + abs(b1 * p1.getZ()) + abs(b2 * p2.getZ());
		
		final Vector3F surfaceIntersectionPointError = Vector3F.multiply(new Vector3F(xAbsSum, yAbsSum, zAbsSum), gamma(7));
		
		final Point3F surfaceIntersectionPoint = new Point3F(b0 * p0.getX() + b1 * p1.getX() + b2 * p2.getX(), b0 * p0.getY() + b1 * p1.getY() + b2 * p2.getY(), b0 * p0.getZ() + b1 * p1.getZ() + b2 * p2.getZ());
		
		final Point2F textureCoordinates = new Point2F(b0 * textureCoordinates0.getU() + b1 + textureCoordinates1.getU() + b2 * textureCoordinates2.getU(), b0 * textureCoordinates0.getV() + b1 * textureCoordinates1.getV() + b2 * textureCoordinates2.getV());
		
		final Vector3F surfaceNormalG = Vector3F.normalize(Vector3F.crossProduct(dP02, dP12));
		
		final OrthonormalBasis33F orthonormalBasisG = new OrthonormalBasis33F(surfaceNormalG, dPDV, dPDU);
		final OrthonormalBasis33F orthonormalBasisS = orthonormalBasisG;
		
		return Optional.of(new SurfaceIntersection3F(orthonormalBasisG, orthonormalBasisS, textureCoordinates, surfaceIntersectionPoint, ray, this, surfaceIntersectionPointError, t));
	}
	
	private Optional<SurfaceIntersection3F> doIntersectionSunflow(final Ray3F ray, final float tMinimum, final float tMaximum) {
		final Point4F a = this.a.getPosition();
		final Point4F b = this.b.getPosition();
		final Point4F c = this.c.getPosition();
		
		final Vector3F edgeAB = Vector3F.direction(a, b);
		final Vector3F edgeCA = Vector3F.direction(c, a);
		final Vector3F direction0 = ray.getDirection();
		final Vector3F direction1 = Vector3F.crossProduct(edgeAB, edgeCA);
		
		final float determinant = Vector3F.dotProduct(direction0, direction1);
		final float determinantReciprocal = 1.0F / determinant;
		
		final Point3F origin = ray.getOrigin();
		
		final Vector3F direction2 = Vector3F.direction(origin, new Point3F(a));
		
		final float t = Vector3F.dotProduct(direction1, direction2) * determinantReciprocal;
		
		if(t <= tMinimum || t >= tMaximum) {
			return SurfaceIntersection3F.EMPTY;
		}
		
		final Vector3F direction3 = Vector3F.crossProduct(direction2, direction0);
		
		final float uScaled = Vector3F.dotProduct(direction3, edgeCA);
		final float u = uScaled * determinantReciprocal;
		
		if(u < 0.0F) {
			return SurfaceIntersection3F.EMPTY;
		}
		
		final float vScaled = Vector3F.dotProduct(direction3, edgeAB);
		final float v = vScaled * determinantReciprocal;
		
		if(v < 0.0F) {
			return SurfaceIntersection3F.EMPTY;
		}
		
		if((uScaled + vScaled) * determinant > determinant * determinant) {
			return SurfaceIntersection3F.EMPTY;
		}
		
		final float w = 1.0F - u - v;
		
		final Point3F barycentricCoordinates = new Point3F(w, u, v);
		final Point3F surfaceIntersectionPoint = Point3F.add(origin, direction0, t);
		
		final Point2F textureCoordinatesA = this.a.getTextureCoordinates();
		final Point2F textureCoordinatesB = this.b.getTextureCoordinates();
		final Point2F textureCoordinatesC = this.c.getTextureCoordinates();
		final Point2F textureCoordinates = Point2F.createTextureCoordinates(textureCoordinatesA, textureCoordinatesB, textureCoordinatesC, barycentricCoordinates);
		
		final OrthonormalBasis33F orthonormalBasisA = this.a.getOrthonormalBasis();
		final OrthonormalBasis33F orthonormalBasisB = this.b.getOrthonormalBasis();
		final OrthonormalBasis33F orthonormalBasisC = this.c.getOrthonormalBasis();
		
		final Vector3F surfaceNormalG = this.surfaceNormal;
		final Vector3F surfaceNormalS = Vector3F.normalNormalized(orthonormalBasisA.getW(), orthonormalBasisB.getW(), orthonormalBasisC.getW(), barycentricCoordinates);
		
		final float dU1 = textureCoordinatesA.getU() - textureCoordinatesC.getU();
		final float dU2 = textureCoordinatesB.getU() - textureCoordinatesC.getU();
		final float dV1 = textureCoordinatesA.getV() - textureCoordinatesC.getV();
		final float dV2 = textureCoordinatesB.getV() - textureCoordinatesC.getV();
		
		final float determinantUV = dU1 * dV2 - dV1 * dU2;
		
		if(isZero(determinantUV)) {
			final OrthonormalBasis33F orthonormalBasisG = new OrthonormalBasis33F(surfaceNormalG);
			final OrthonormalBasis33F orthonormalBasisS = new OrthonormalBasis33F(surfaceNormalS);
			
			final float xAbsSum = abs(barycentricCoordinates.getU() + a.getX()) + abs(barycentricCoordinates.getV() + b.getX()) + abs(barycentricCoordinates.getW() + c.getX());
			final float yAbsSum = abs(barycentricCoordinates.getU() + a.getY()) + abs(barycentricCoordinates.getV() + b.getY()) + abs(barycentricCoordinates.getW() + c.getY());
			final float zAbsSum = abs(barycentricCoordinates.getU() + a.getZ()) + abs(barycentricCoordinates.getV() + b.getZ()) + abs(barycentricCoordinates.getW() + c.getZ());
			
			final Vector3F surfaceIntersectionPointError = Vector3F.multiply(new Vector3F(xAbsSum, yAbsSum, zAbsSum), gamma(7));
			
			return Optional.of(new SurfaceIntersection3F(orthonormalBasisG, orthonormalBasisS, textureCoordinates, surfaceIntersectionPoint, ray, this, surfaceIntersectionPointError, t));
		}
		
		final float determinantUVReciprocal = 1.0F / determinantUV;
		
		final Vector3F dPU = Vector3F.direction(c, a);
		final Vector3F dPV = Vector3F.direction(c, b);
		
		final Vector3F vS = new Vector3F((-dU2 * dPU.getX() + dU1 * dPV.getX()) * determinantUVReciprocal, (-dU2 * dPU.getY() + dU1 * dPV.getY()) * determinantUVReciprocal, (-dU2 * dPU.getZ() + dU1 * dPV.getZ()) * determinantUVReciprocal);
		
		final OrthonormalBasis33F orthonormalBasisG = new OrthonormalBasis33F(surfaceNormalG);
		final OrthonormalBasis33F orthonormalBasisS = new OrthonormalBasis33F(surfaceNormalS, vS);
		
		final float xAbsSum = abs(barycentricCoordinates.getU() + a.getX()) + abs(barycentricCoordinates.getV() + b.getX()) + abs(barycentricCoordinates.getW() + c.getX());
		final float yAbsSum = abs(barycentricCoordinates.getU() + a.getY()) + abs(barycentricCoordinates.getV() + b.getY()) + abs(barycentricCoordinates.getW() + c.getY());
		final float zAbsSum = abs(barycentricCoordinates.getU() + a.getZ()) + abs(barycentricCoordinates.getV() + b.getZ()) + abs(barycentricCoordinates.getW() + c.getZ());
		
		final Vector3F surfaceIntersectionPointError = Vector3F.multiply(new Vector3F(xAbsSum, yAbsSum, zAbsSum), gamma(7));
		
		return Optional.of(new SurfaceIntersection3F(orthonormalBasisG, orthonormalBasisS, textureCoordinates, surfaceIntersectionPoint, ray, this, surfaceIntersectionPointError, t));
	}
	
	@SuppressWarnings("unused")
	private float doIntersectionTOld(final Ray3F ray, final float tMinimum, final float tMaximum) {
		final Point4F a = this.a.getPosition();
		final Point4F b = this.b.getPosition();
		final Point4F c = this.c.getPosition();
		
		final Vector3F edgeAB = Vector3F.direction(a, b);
		final Vector3F edgeAC = Vector3F.direction(a, c);
		final Vector3F direction0 = ray.getDirection();
		final Vector3F direction1 = Vector3F.crossProduct(direction0, edgeAC);
		
		final float determinant = Vector3F.dotProduct(edgeAB, direction1);
		
		if(determinant >= -0.0001F && determinant <= 0.0001F) {
			return Float.NaN;
		}
		
		final Point3F origin = ray.getOrigin();
		
		final Vector3F direction2 = Vector3F.direction(new Point3F(a), origin);
		
		final float determinantReciprocal = 1.0F / determinant;
		final float u = Vector3F.dotProduct(direction2, direction1) * determinantReciprocal;
		
		if(u < 0.0F || u > 1.0F) {
			return Float.NaN;
		}
		
		final Vector3F direction3 = Vector3F.crossProduct(direction2, edgeAB);
		
		final float v = Vector3F.dotProduct(direction0, direction3) * determinantReciprocal;
		
		if(v < 0.0F || u + v > 1.0F) {
			return Float.NaN;
		}
		
		final float t = Vector3F.dotProduct(edgeAC, direction3) * determinantReciprocal;
		
		if(t <= tMinimum || t >= tMaximum) {
			return Float.NaN;
		}
		
		return t;
	}
	
	@SuppressWarnings("unused")
	private float doIntersectionTPBRT(final Ray3F ray, final float tMinimum, final float tMaximum) {
		final Point3F p0 = new Point3F(this.a.getPosition());
		final Point3F p1 = new Point3F(this.b.getPosition());
		final Point3F p2 = new Point3F(this.c.getPosition());
		
		final int kZ = doMaxDimension(Vector3F.absolute(ray.getDirection()));
		final int kX = kZ == 2 ? 0 : kZ + 1;
		final int kY = kX == 2 ? 0 : kX + 1;
		
		final Vector3F d = doPermute(ray.getDirection(), kX, kY, kZ);
		
		final float shearX = -d.getX() / d.getZ();
		final float shearY = -d.getY() / d.getZ();
		final float shearZ = 1.0F / d.getZ();
		
		final Point3F p0T = doShear(doPermute(Point3F.subtract(p0, new Vector3F(ray.getOrigin())), kX, kY, kZ), shearX, shearY, shearZ);
		final Point3F p1T = doShear(doPermute(Point3F.subtract(p1, new Vector3F(ray.getOrigin())), kX, kY, kZ), shearX, shearY, shearZ);
		final Point3F p2T = doShear(doPermute(Point3F.subtract(p2, new Vector3F(ray.getOrigin())), kX, kY, kZ), shearX, shearY, shearZ);
		
		float e0 = p1T.getX() * p2T.getY() - p1T.getY() * p2T.getX();
		float e1 = p2T.getX() * p0T.getY() - p2T.getY() * p0T.getX();
		float e2 = p0T.getX() * p1T.getY() - p0T.getY() * p1T.getX();
		
		if(isZero(e0) || isZero(e1) || isZero(e2)) {
			final double p2TXP1TY = (double)(p2T.getX()) * (double)(p1T.getY());
			final double p2TYP1TX = (double)(p2T.getY()) * (double)(p1T.getX());
			final double p0TXP2TY = (double)(p0T.getX()) * (double)(p2T.getY());
			final double p0TYP2TX = (double)(p0T.getY()) * (double)(p2T.getX());
			final double p1TXP0TY = (double)(p1T.getX()) * (double)(p0T.getY());
			final double p1TYP0TX = (double)(p1T.getY()) * (double)(p0T.getX());
			
			e0 = (float)(p2TYP1TX - p2TXP1TY);
			e1 = (float)(p0TYP2TX - p0TXP2TY);
			e2 = (float)(p1TYP0TX - p1TXP0TY);
		}
		
		if((e0 < 0.0F || e1 < 0.0F || e2 < 0.0F) && (e0 > 0.0F || e1 > 0.0F || e2 > 0.0F)) {
			return Float.NaN;
		}
		
		final float det = e0 + e1 + e2;
		
		if(isZero(det)) {
			return Float.NaN;
		}
		
		final float tScaled = e0 * p0T.getZ() + e1 * p1T.getZ() + e2 * p2T.getZ();
		
		if(det < 0.0F && (tScaled >= 0.0F || tScaled < tMaximum * det)) {
			return Float.NaN;
		} else if(det > 0.0F && (tScaled <= 0.0F || tScaled > tMaximum * det)) {
			return Float.NaN;
		}
		
		final float invDet = 1.0F / det;
		
		final float t = tScaled * invDet;
		
		final float maxE = max(abs(e0), abs(e1), abs(e2));
		
		final float maxXT = max(abs(p0T.getX()), abs(p1T.getX()), abs(p2T.getX()));
		final float maxYT = max(abs(p0T.getY()), abs(p1T.getY()), abs(p2T.getY()));
		final float maxZT = max(abs(p0T.getZ()), abs(p1T.getZ()), abs(p2T.getZ()));
		
		final float deltaX = gamma(5) * (maxXT + maxZT);
		final float deltaY = gamma(5) * (maxYT + maxZT);
		final float deltaZ = gamma(3) * maxZT;
		final float deltaE = 2.0F * (gamma(2) * maxXT * maxYT + deltaY * maxXT + deltaX * maxYT);
		final float deltaT = 3.0F * (gamma(3) * maxE * maxZT + deltaE * maxZT + deltaZ * maxE) * abs(invDet);
		
		if(t <= deltaT) {
			return Float.NaN;
		}
		
		return t;
	}
	
	private float doIntersectionTSunflow(final Ray3F ray, final float tMinimum, final float tMaximum) {
		final Point4F a = this.a.getPosition();
		final Point4F b = this.b.getPosition();
		final Point4F c = this.c.getPosition();
		
		final Vector3F edgeAB = Vector3F.direction(a, b);
		final Vector3F edgeCA = Vector3F.direction(c, a);
		final Vector3F direction0 = ray.getDirection();
		final Vector3F direction1 = Vector3F.crossProduct(edgeAB, edgeCA);
		
		final float determinant = Vector3F.dotProduct(direction0, direction1);
		final float determinantReciprocal = 1.0F / determinant;
		
		final Point3F origin = ray.getOrigin();
		
		final Vector3F direction2 = Vector3F.direction(origin, new Point3F(a));
		
		final float t = Vector3F.dotProduct(direction1, direction2) * determinantReciprocal;
		
		if(t <= tMinimum || t >= tMaximum) {
			return Float.NaN;
		}
		
		final Vector3F direction3 = Vector3F.crossProduct(direction2, direction0);
		
		final float uScaled = Vector3F.dotProduct(direction3, edgeCA);
		final float u = uScaled * determinantReciprocal;
		
		if(u < 0.0F) {
			return Float.NaN;
		}
		
		final float vScaled = Vector3F.dotProduct(direction3, edgeAB);
		final float v = vScaled * determinantReciprocal;
		
		if(v < 0.0F) {
			return Float.NaN;
		}
		
		if((uScaled + vScaled) * determinant > determinant * determinant) {
			return Float.NaN;
		}
		
		return t;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static Point3F doPermute(final Point3F point, final int x, final int y, final int z) {
		final float component1 = x == 0 ? point.getX() : x == 1 ? point.getY() : point.getZ();
		final float component2 = y == 0 ? point.getX() : y == 1 ? point.getY() : point.getZ();
		final float component3 = z == 0 ? point.getX() : z == 1 ? point.getY() : point.getZ();
		
		return new Point3F(component1, component2, component3);
	}
	
	private static Point3F doShear(final Point3F point, final float shearX, final float shearY, final float shearZ) {
		final float component1 = point.getX() + shearX * point.getZ();
		final float component2 = point.getY() + shearY * point.getZ();
		final float component3 = point.getZ() * shearZ;
		
		return new Point3F(component1, component2, component3);
	}
	
	private static Vector3F doPermute(final Vector3F vector, final int x, final int y, final int z) {
		final float component1 = x == 0 ? vector.getX() : x == 1 ? vector.getY() : vector.getZ();
		final float component2 = y == 0 ? vector.getX() : y == 1 ? vector.getY() : vector.getZ();
		final float component3 = z == 0 ? vector.getX() : z == 1 ? vector.getY() : vector.getZ();
		
		return new Vector3F(component1, component2, component3);
	}
	
	private static int doMaxDimension(final Vector3F vector) {
		return vector.getX() > vector.getY() ? vector.getX() > vector.getZ() ? 0 : 2 : vector.getY() > vector.getZ() ? 1 : 2;
	}
}