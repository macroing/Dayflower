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

import static org.dayflower.utility.Doubles.abs;
import static org.dayflower.utility.Doubles.gamma;
import static org.dayflower.utility.Doubles.isZero;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.Optional;

import org.dayflower.geometry.BoundingVolume3D;
import org.dayflower.geometry.Matrix44D;
import org.dayflower.geometry.OrthonormalBasis33D;
import org.dayflower.geometry.Point2D;
import org.dayflower.geometry.Point3D;
import org.dayflower.geometry.Point4D;
import org.dayflower.geometry.Ray3D;
import org.dayflower.geometry.SampleGeneratorD;
import org.dayflower.geometry.Shape3D;
import org.dayflower.geometry.SurfaceIntersection3D;
import org.dayflower.geometry.SurfaceSample3D;
import org.dayflower.geometry.Vector3D;
import org.dayflower.geometry.boundingvolume.AxisAlignedBoundingBox3D;
import org.dayflower.node.Node;
import org.dayflower.node.NodeHierarchicalVisitor;
import org.dayflower.node.NodeTraversalException;

/**
 * A {@code Triangle3D} denotes a 3-dimensional triangle that uses the data type {@code double}.
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class Triangle3D implements Shape3D {
	/**
	 * The name of this {@code Triangle3D} class.
	 */
	public static final String NAME = "Triangle";
	
	/**
	 * The length of the {@code double[]}.
	 */
	public static final int ARRAY_LENGTH = 24;
	
	/**
	 * The offset for the {@link Vector3D} instance representing the W-direction of the {@link OrthonormalBasis33D} of {@link Vertex3D} {@code A} in the {@code double[]}.
	 */
	public static final int ARRAY_OFFSET_A_ORTHONORMAL_BASIS_W = 15;
	
	/**
	 * The offset for the {@link Point3D} instance representing the position of {@link Vertex3D} {@code A} in the {@code double[]}.
	 */
	public static final int ARRAY_OFFSET_A_POSITION = 0;
	
	/**
	 * The offset for the {@link Point2D} instance representing the texture coordinates of {@link Vertex3D} {@code A} in the {@code double[]}.
	 */
	public static final int ARRAY_OFFSET_A_TEXTURE_COORDINATES = 9;
	
	/**
	 * The offset for the {@link Vector3D} instance representing the W-direction of the {@link OrthonormalBasis33D} of {@link Vertex3D} {@code B} in the {@code double[]}.
	 */
	public static final int ARRAY_OFFSET_B_ORTHONORMAL_BASIS_W = 18;
	
	/**
	 * The offset for the {@link Point3D} instance representing the position of {@link Vertex3D} {@code B} in the {@code double[]}.
	 */
	public static final int ARRAY_OFFSET_B_POSITION = 3;
	
	/**
	 * The offset for the {@link Point2D} instance representing the texture coordinates of {@link Vertex3D} {@code B} in the {@code double[]}.
	 */
	public static final int ARRAY_OFFSET_B_TEXTURE_COORDINATES = 11;
	
	/**
	 * The offset for the {@link Vector3D} instance representing the W-direction of the {@link OrthonormalBasis33D} of {@link Vertex3D} {@code C} in the {@code double[]}.
	 */
	public static final int ARRAY_OFFSET_C_ORTHONORMAL_BASIS_W = 21;
	
	/**
	 * The offset for the {@link Point3D} instance representing the position of {@link Vertex3D} {@code C} in the {@code double[]}.
	 */
	public static final int ARRAY_OFFSET_C_POSITION = 6;
	
	/**
	 * The offset for the {@link Point2D} instance representing the texture coordinates of {@link Vertex3D} {@code C} in the {@code double[]}.
	 */
	public static final int ARRAY_OFFSET_C_TEXTURE_COORDINATES = 13;
	
	/**
	 * The ID of this {@code Triangle3D} class.
	 */
	public static final int ID = 9;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final Vector3D surfaceNormal;
	private final Vertex3D a;
	private final Vertex3D b;
	private final Vertex3D c;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code Triangle3D} instance.
	 */
	public Triangle3D() {
		this.a = new Vertex3D(new Point2D(0.5D, 0.0D), new Point4D(+0.0D, +1.0D, 0.0D), Vector3D.normalNormalized(new Point3D(0.0D, 1.0D, 0.0D), new Point3D(1.0D, -1.0D, 0.0D), new Point3D(-1.0D, -1.0D, 0.0D)));
		this.b = new Vertex3D(new Point2D(1.0D, 1.0D), new Point4D(+1.0D, -1.0D, 0.0D), Vector3D.normalNormalized(new Point3D(0.0D, 1.0D, 0.0D), new Point3D(1.0D, -1.0D, 0.0D), new Point3D(-1.0D, -1.0D, 0.0D)));
		this.c = new Vertex3D(new Point2D(0.0D, 1.0D), new Point4D(-1.0D, -1.0D, 0.0D), Vector3D.normalNormalized(new Point3D(0.0D, 1.0D, 0.0D), new Point3D(1.0D, -1.0D, 0.0D), new Point3D(-1.0D, -1.0D, 0.0D)));
		this.surfaceNormal = Vector3D.getCached(Vector3D.normalNormalized(new Point3D(this.a.getPosition()), new Point3D(this.b.getPosition()), new Point3D(this.c.getPosition())));
	}
	
	/**
	 * Constructs a new {@code Triangle3D} instance.
	 * <p>
	 * If either {@code a}, {@code b} or {@code c} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param a a {@link Vertex3D} instance denoted by {@code A}
	 * @param b a {@code Vertex3D} instance denoted by {@code B}
	 * @param c a {@code Vertex3D} instance denoted by {@code C}
	 * @throws NullPointerException thrown if, and only if, either {@code a}, {@code b} or {@code c} are {@code null}
	 */
	public Triangle3D(final Vertex3D a, final Vertex3D b, final Vertex3D c) {
		this.a = Objects.requireNonNull(a, "a == null");
		this.b = Objects.requireNonNull(b, "b == null");
		this.c = Objects.requireNonNull(c, "c == null");
		this.surfaceNormal = Vector3D.getCached(Vector3D.normalNormalized(new Point3D(a.getPosition()), new Point3D(b.getPosition()), new Point3D(c.getPosition())));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link BoundingVolume3D} instance that contains this {@code Triangle3D} instance.
	 * 
	 * @return a {@code BoundingVolume3D} instance that contains this {@code Triangle3D} instance
	 */
	@Override
	public BoundingVolume3D getBoundingVolume() {
		final Point3D a = Point3D.minimum(new Point3D(this.a.getPosition()), new Point3D(this.b.getPosition()), new Point3D(this.c.getPosition()));
		final Point3D b = Point3D.maximum(new Point3D(this.a.getPosition()), new Point3D(this.b.getPosition()), new Point3D(this.c.getPosition()));
		
		return new AxisAlignedBoundingBox3D(a, b);
	}
	
	/**
	 * Samples this {@code Triangle3D} instance.
	 * <p>
	 * Returns an optional {@link SurfaceSample3D} with the surface sample.
	 * <p>
	 * If either {@code referencePoint} or {@code referenceSurfaceNormal} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param referencePoint the reference point on this {@code Triangle3D} instance
	 * @param referenceSurfaceNormal the reference surface normal on this {@code Triangle3D} instance
	 * @param u a random {@code double} with a uniform distribution between {@code 0.0D} and {@code 1.0D}
	 * @param v a random {@code double} with a uniform distribution between {@code 0.0D} and {@code 1.0D}
	 * @return an optional {@code SurfaceSample3D} with the surface sample
	 * @throws NullPointerException thrown if, and only if, either {@code referencePoint} or {@code referenceSurfaceNormal} are {@code null}
	 */
	@Override
	public Optional<SurfaceSample3D> sample(final Point3D referencePoint, final Vector3D referenceSurfaceNormal, final double u, final double v) {
		Objects.requireNonNull(referenceSurfaceNormal, "referenceSurfaceNormal == null");
		
		final Point3D barycentricCoordinates = SampleGeneratorD.sampleTriangleUniformDistribution(u, v);
		
		final Point4D positionA = this.a.getPosition();
		final Point4D positionB = this.b.getPosition();
		final Point4D positionC = this.c.getPosition();
		
		final Vector3D normalA = this.a.getOrthonormalBasis().getW();
		final Vector3D normalB = this.b.getOrthonormalBasis().getW();
		final Vector3D normalC = this.c.getOrthonormalBasis().getW();
		
		final double x = positionA.getX() * barycentricCoordinates.getX() + positionB.getX() * barycentricCoordinates.getY() + positionC.getX() * barycentricCoordinates.getZ();
		final double y = positionA.getY() * barycentricCoordinates.getX() + positionB.getY() * barycentricCoordinates.getY() + positionC.getY() * barycentricCoordinates.getZ();
		final double z = positionA.getZ() * barycentricCoordinates.getX() + positionB.getZ() * barycentricCoordinates.getY() + positionC.getZ() * barycentricCoordinates.getZ();
		
		final Point3D point = new Point3D(x, y, z);
		
		final Vector3D surfaceNormal = Vector3D.normalNormalized(normalA, normalB, normalC, barycentricCoordinates);
		
		final Vector3D directionToSurface = Vector3D.direction(point, referencePoint);
		final Vector3D directionToSurfaceNormalized = Vector3D.normalize(directionToSurface);
		
		final double probabilityDensityFunctionValue = directionToSurface.lengthSquared() * (1.0D / getSurfaceArea()) / abs(Vector3D.dotProduct(directionToSurfaceNormalized, surfaceNormal));
		
		return Optional.of(new SurfaceSample3D(point, surfaceNormal, probabilityDensityFunctionValue));
	}
	
	/**
	 * Performs an intersection test between {@code ray} and this {@code Triangle3D} instance.
	 * <p>
	 * Returns an {@code Optional} with an optional {@link SurfaceIntersection3D} instance that contains information about the intersection, if it was found.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3D} to perform an intersection test against this {@code Triangle3D} instance
	 * @param tMinimum the minimum parametric distance
	 * @param tMaximum the maximum parametric distance
	 * @return an {@code Optional} with an optional {@code SurfaceIntersection3F} instance that contains information about the intersection, if it was found
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	@Override
	public Optional<SurfaceIntersection3D> intersection(final Ray3D ray, final double tMinimum, final double tMaximum) {
		final Point4D a = this.a.getPosition();
		final Point4D b = this.b.getPosition();
		final Point4D c = this.c.getPosition();
		
		final Vector3D edgeAB = Vector3D.direction(a, b);
		final Vector3D edgeAC = Vector3D.direction(a, c);
		final Vector3D direction0 = ray.getDirection();
		final Vector3D direction1 = Vector3D.crossProduct(direction0, edgeAC);
		
		final double determinant = Vector3D.dotProduct(edgeAB, direction1);
		
		if(determinant >= -0.0001D && determinant <= 0.0001D) {
			return SurfaceIntersection3D.EMPTY;
		}
		
		final Point3D origin = ray.getOrigin();
		
		final Vector3D direction2 = Vector3D.direction(new Point3D(a), origin);
		
		final double determinantReciprocal = 1.0D / determinant;
		final double u = Vector3D.dotProduct(direction2, direction1) * determinantReciprocal;
		
		if(u < 0.0D || u > 1.0D) {
			return SurfaceIntersection3D.EMPTY;
		}
		
		final Vector3D direction3 = Vector3D.crossProduct(direction2, edgeAB);
		
		final double v = Vector3D.dotProduct(direction0, direction3) * determinantReciprocal;
		
		if(v < 0.0D || u + v > 1.0D) {
			return SurfaceIntersection3D.EMPTY;
		}
		
		final double t = Vector3D.dotProduct(edgeAC, direction3) * determinantReciprocal;
		
		if(t <= tMinimum || t >= tMaximum) {
			return SurfaceIntersection3D.EMPTY;
		}
		
		final double w = 1.0D - u - v;
		
		final Point3D barycentricCoordinates = new Point3D(w, u, v);
		final Point3D surfaceIntersectionPoint = Point3D.add(origin, direction0, t);
		
		final Point2D textureCoordinates = Point2D.createTextureCoordinates(this.a.getTextureCoordinates(), this.b.getTextureCoordinates(), this.c.getTextureCoordinates(), barycentricCoordinates);
		
		final OrthonormalBasis33D aOrthonormalBasis = this.a.getOrthonormalBasis();
		final OrthonormalBasis33D bOrthonormalBasis = this.b.getOrthonormalBasis();
		final OrthonormalBasis33D cOrthonormalBasis = this.c.getOrthonormalBasis();
		
		final Vector3D surfaceNormalG = this.surfaceNormal;
		final Vector3D surfaceNormalS = Vector3D.normalNormalized(aOrthonormalBasis.getW(), bOrthonormalBasis.getW(), cOrthonormalBasis.getW(), barycentricCoordinates);
		
		final double dU1 = this.a.getTextureCoordinates().getU() - this.c.getTextureCoordinates().getU();
		final double dU2 = this.b.getTextureCoordinates().getU() - this.c.getTextureCoordinates().getU();
		final double dV1 = this.a.getTextureCoordinates().getV() - this.c.getTextureCoordinates().getV();
		final double dV2 = this.b.getTextureCoordinates().getV() - this.c.getTextureCoordinates().getV();
		
		final double determinantUV = dU1 * dV2 - dV1 * dU2;
		
		if(isZero(determinantUV)) {
			final OrthonormalBasis33D orthonormalBasisG = new OrthonormalBasis33D(surfaceNormalG);
			final OrthonormalBasis33D orthonormalBasisS = new OrthonormalBasis33D(surfaceNormalS);
			
			final double xAbsSum = abs(barycentricCoordinates.getU() + a.getX()) + abs(barycentricCoordinates.getV() + b.getX()) + abs(barycentricCoordinates.getW() + c.getX());
			final double yAbsSum = abs(barycentricCoordinates.getU() + a.getY()) + abs(barycentricCoordinates.getV() + b.getY()) + abs(barycentricCoordinates.getW() + c.getY());
			final double zAbsSum = abs(barycentricCoordinates.getU() + a.getZ()) + abs(barycentricCoordinates.getV() + b.getZ()) + abs(barycentricCoordinates.getW() + c.getZ());
			
			final Vector3D surfaceIntersectionPointError = Vector3D.multiply(new Vector3D(xAbsSum, yAbsSum, zAbsSum), gamma(7));
			
			return Optional.of(new SurfaceIntersection3D(orthonormalBasisG, orthonormalBasisS, textureCoordinates, surfaceIntersectionPoint, ray, this, surfaceIntersectionPointError, t));
		}
		
		final double determinantUVReciprocal = 1.0D / determinantUV;
		
		final Vector3D dPU = Vector3D.direction(c, a);
		final Vector3D dPV = Vector3D.direction(c, b);
		
		final Vector3D vS = new Vector3D((-dU2 * dPU.getX() + dU1 * dPV.getX()) * determinantUVReciprocal, (-dU2 * dPU.getY() + dU1 * dPV.getY()) * determinantUVReciprocal, (-dU2 * dPU.getZ() + dU1 * dPV.getZ()) * determinantUVReciprocal);
		
		final OrthonormalBasis33D orthonormalBasisG = new OrthonormalBasis33D(surfaceNormalG);
		final OrthonormalBasis33D orthonormalBasisS = new OrthonormalBasis33D(surfaceNormalS, vS);
		
		final double xAbsSum = abs(barycentricCoordinates.getU() + a.getX()) + abs(barycentricCoordinates.getV() + b.getX()) + abs(barycentricCoordinates.getW() + c.getX());
		final double yAbsSum = abs(barycentricCoordinates.getU() + a.getY()) + abs(barycentricCoordinates.getV() + b.getY()) + abs(barycentricCoordinates.getW() + c.getY());
		final double zAbsSum = abs(barycentricCoordinates.getU() + a.getZ()) + abs(barycentricCoordinates.getV() + b.getZ()) + abs(barycentricCoordinates.getW() + c.getZ());
		
		final Vector3D surfaceIntersectionPointError = Vector3D.multiply(new Vector3D(xAbsSum, yAbsSum, zAbsSum), gamma(7));
		
		return Optional.of(new SurfaceIntersection3D(orthonormalBasisG, orthonormalBasisS, textureCoordinates, surfaceIntersectionPoint, ray, this, surfaceIntersectionPointError, t));
	}
	
	/**
	 * Returns a {@code String} with the name of this {@code Triangle3D} instance.
	 * 
	 * @return a {@code String} with the name of this {@code Triangle3D} instance
	 */
	@Override
	public String getName() {
		return NAME;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code Triangle3D} instance.
	 * 
	 * @return a {@code String} representation of this {@code Triangle3D} instance
	 */
	@Override
	public String toString() {
		return String.format("new Triangle3D(%s, %s, %s)", this.a, this.b, this.c);
	}
	
	/**
	 * Returns the surface normal associated with this {@code Triangle3D} instance.
	 * 
	 * @return the surface normal associated with this {@code Triangle3D} instance
	 */
	public Vector3D getSurfaceNormal() {
		return this.surfaceNormal;
	}
	
	/**
	 * Returns the vertex denoted by {@code A} and is associated with this {@code Triangle3D} instance.
	 * 
	 * @return the vertex denoted by {@code A} and is associated with this {@code Triangle3D} instance
	 */
	public Vertex3D getA() {
		return this.a;
	}
	
	/**
	 * Returns the vertex denoted by {@code B} and is associated with this {@code Triangle3D} instance.
	 * 
	 * @return the vertex denoted by {@code B} and is associated with this {@code Triangle3D} instance
	 */
	public Vertex3D getB() {
		return this.b;
	}
	
	/**
	 * Returns the vertex denoted by {@code C} and is associated with this {@code Triangle3D} instance.
	 * 
	 * @return the vertex denoted by {@code C} and is associated with this {@code Triangle3D} instance
	 */
	public Vertex3D getC() {
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
	 * Compares {@code object} to this {@code Triangle3D} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code Triangle3D}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code Triangle3D} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code Triangle3D}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof Triangle3D)) {
			return false;
		} else if(!Objects.equals(this.surfaceNormal, Triangle3D.class.cast(object).surfaceNormal)) {
			return false;
		} else if(!Objects.equals(this.a, Triangle3D.class.cast(object).a)) {
			return false;
		} else if(!Objects.equals(this.b, Triangle3D.class.cast(object).b)) {
			return false;
		} else if(!Objects.equals(this.c, Triangle3D.class.cast(object).c)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns the probability density function (PDF) value for solid angle.
	 * <p>
	 * If either {@code referencePoint}, {@code referenceSurfaceNormal}, {@code point} or {@code surfaceNormal} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param referencePoint the reference point on this {@code Triangle3D} instance
	 * @param referenceSurfaceNormal the reference surface normal on this {@code Triangle3D} instance
	 * @param point the point on this {@code Triangle3D} instance
	 * @param surfaceNormal the surface normal on this {@code Triangle3D} instance
	 * @return the probability density function (PDF) value for solid angle
	 * @throws NullPointerException thrown if, and only if, either {@code referencePoint}, {@code referenceSurfaceNormal}, {@code point} or {@code surfaceNormal} are {@code null}
	 */
	@Override
	public double evaluateProbabilityDensityFunction(final Point3D referencePoint, final Vector3D referenceSurfaceNormal, final Point3D point, final Vector3D surfaceNormal) {
		Objects.requireNonNull(referenceSurfaceNormal, "referenceSurfaceNormal == null");
		
		final Vector3D directionToSurface = Vector3D.direction(point, referencePoint);
		final Vector3D directionToSurfaceNormalized = Vector3D.normalize(directionToSurface);
		
		final double probabilityDensityFunctionValue = directionToSurface.lengthSquared() * (1.0D / getSurfaceArea()) / abs(Vector3D.dotProduct(directionToSurfaceNormalized, surfaceNormal));
		
		return probabilityDensityFunctionValue;
	}
	
	/**
	 * Returns the probability density function (PDF) value for solid angle.
	 * <p>
	 * If either {@code referencePoint}, {@code referenceSurfaceNormal} or {@code direction} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param referencePoint the reference point on this {@code Triangle3D} instance
	 * @param referenceSurfaceNormal the reference surface normal on this {@code Triangle3D} instance
	 * @param direction the direction to this {@code Triangle3D} instance
	 * @return the probability density function (PDF) value for solid angle
	 * @throws NullPointerException thrown if, and only if, either {@code referencePoint}, {@code referenceSurfaceNormal} or {@code direction} are {@code null}
	 */
	@Override
	public double evaluateProbabilityDensityFunction(final Point3D referencePoint, final Vector3D referenceSurfaceNormal, final Vector3D direction) {
		Objects.requireNonNull(referencePoint, "referencePoint == null");
		Objects.requireNonNull(referenceSurfaceNormal, "referenceSurfaceNormal == null");
		Objects.requireNonNull(direction, "direction == null");
		
//		TODO: Check if these variables should be supplied as parameters?
		final double tMinimum = 0.001D;
		final double tMaximum = Double.MAX_VALUE;
		
		final Optional<SurfaceIntersection3D> optionalSurfaceIntersection = intersection(new Ray3D(referencePoint, direction), tMinimum, tMaximum);
		
		if(optionalSurfaceIntersection.isPresent()) {
			final SurfaceIntersection3D surfaceIntersection = optionalSurfaceIntersection.get();
			
			final Point3D point = surfaceIntersection.getSurfaceIntersectionPoint();
			
			final Vector3D surfaceNormal = surfaceIntersection.getOrthonormalBasisS().getW();
			
			return evaluateProbabilityDensityFunction(referencePoint, referenceSurfaceNormal, point, surfaceNormal);
		}
		
		return 0.0D;
	}
	
	/**
	 * Returns the surface area of this {@code Triangle3D} instance.
	 * 
	 * @return the surface area of this {@code Triangle3D} instance
	 */
	@Override
	public double getSurfaceArea() {
		return getSurfaceAreaSquared() * 0.5D;
	}
	
	/**
	 * Returns the squared surface area of this {@code Triangle3D} instance.
	 * 
	 * @return the squared surface area of this {@code Triangle3D} instance
	 */
	public double getSurfaceAreaSquared() {
		final Point4D a = this.a.getPosition();
		final Point4D b = this.b.getPosition();
		final Point4D c = this.c.getPosition();
		
		final Vector3D edgeAB = Vector3D.direction(a, b);
		final Vector3D edgeAC = Vector3D.direction(a, c);
		final Vector3D edgeABCrossEdgeAC = Vector3D.crossProduct(edgeAB, edgeAC);
		
		return edgeABCrossEdgeAC.length();
	}
	
	/**
	 * Performs an intersection test between {@code ray} and this {@code Triangle3D} instance.
	 * <p>
	 * Returns {@code t}, the parametric distance to the surface intersection point, or {@code Double.NaN} if no intersection exists.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3D} to perform an intersection test against this {@code Triangle3D} instance
	 * @param tMinimum the minimum parametric distance
	 * @param tMaximum the maximum parametric distance
	 * @return {@code t}, the parametric distance to the surface intersection point, or {@code Double.NaN} if no intersection exists
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	@Override
	public double intersectionT(final Ray3D ray, final double tMinimum, final double tMaximum) {
		final Point4D a = this.a.getPosition();
		final Point4D b = this.b.getPosition();
		final Point4D c = this.c.getPosition();
		
		final Vector3D edgeAB = Vector3D.direction(a, b);
		final Vector3D edgeAC = Vector3D.direction(a, c);
		final Vector3D direction0 = ray.getDirection();
		final Vector3D direction1 = Vector3D.crossProduct(direction0, edgeAC);
		
		final double determinant = Vector3D.dotProduct(edgeAB, direction1);
		
		if(determinant >= -0.0001D && determinant <= 0.0001D) {
			return Double.NaN;
		}
		
		final Point3D origin = ray.getOrigin();
		
		final Vector3D direction2 = Vector3D.direction(new Point3D(a), origin);
		
		final double determinantReciprocal = 1.0D / determinant;
		final double u = Vector3D.dotProduct(direction2, direction1) * determinantReciprocal;
		
		if(u < 0.0D || u > 1.0D) {
			return Double.NaN;
		}
		
		final Vector3D direction3 = Vector3D.crossProduct(direction2, edgeAB);
		
		final double v = Vector3D.dotProduct(direction0, direction3) * determinantReciprocal;
		
		if(v < 0.0D || u + v > 1.0D) {
			return Double.NaN;
		}
		
		final double t = Vector3D.dotProduct(edgeAC, direction3) * determinantReciprocal;
		
		if(t <= tMinimum || t >= tMaximum) {
			return Double.NaN;
		}
		
		return t;
	}
	
	/**
	 * Returns a {@code double[]} representation of this {@code Triangle3D} instance.
	 * 
	 * @return a {@code double[]} representation of this {@code Triangle3D} instance
	 */
	public double[] toArray() {
		final double[] array = new double[ARRAY_LENGTH];
		
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
	 * Returns an {@code int} with the ID of this {@code Triangle3D} instance.
	 * 
	 * @return an {@code int} with the ID of this {@code Triangle3D} instance
	 */
	@Override
	public int getID() {
		return ID;
	}
	
	/**
	 * Returns a hash code for this {@code Triangle3D} instance.
	 * 
	 * @return a hash code for this {@code Triangle3D} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.surfaceNormal, this.a, this.b, this.c);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * A {@code Vertex3D} denotes a vertex of a {@link Triangle3D} instance.
	 * <p>
	 * This class is immutable and therefore thread-safe.
	 * 
	 * @since 1.0.0
	 * @author J&#246;rgen Lundgren
	 */
	public static final class Vertex3D implements Node {
		private final OrthonormalBasis33D orthonormalBasis;
		private final Point2D textureCoordinates;
		private final Point4D position;
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		/**
		 * Constructs a new {@code Vertex3D} instance.
		 * <p>
		 * If either {@code textureCoordinates}, {@code position} or {@code orthonormalBasis} are {@code null}, a {@code NullPointerException} will be thrown.
		 * 
		 * @param textureCoordinates the texture coordinates associated with this {@code Vertex3D} instance
		 * @param position the position associated with this {@code Vertex3D} instance
		 * @param orthonormalBasis the orthonormal basis associated with this {@code Vertex3D} instance
		 * @throws NullPointerException thrown if, and only if, either {@code textureCoordinates}, {@code position} or {@code orthonormalBasis} are {@code null}
		 */
		public Vertex3D(final Point2D textureCoordinates, final Point4D position, final OrthonormalBasis33D orthonormalBasis) {
			this.textureCoordinates = Objects.requireNonNull(textureCoordinates, "textureCoordinates == null");
			this.position = Point4D.getCached(Objects.requireNonNull(position, "position == null"));
			this.orthonormalBasis = OrthonormalBasis33D.getCached(Objects.requireNonNull(orthonormalBasis, "orthonormalBasis == null"));
		}
		
		/**
		 * Constructs a new {@code Vertex3D} instance.
		 * <p>
		 * If either {@code textureCoordinates}, {@code position} or {@code w} are {@code null}, a {@code NullPointerException} will be thrown.
		 * 
		 * @param textureCoordinates the texture coordinates associated with this {@code Vertex3D} instance
		 * @param position the position associated with this {@code Vertex3D} instance
		 * @param w the W-direction associated with this {@code Vertex3D} instance
		 * @throws NullPointerException thrown if, and only if, either {@code textureCoordinates}, {@code position} or {@code w} are {@code null}
		 */
		public Vertex3D(final Point2D textureCoordinates, final Point4D position, final Vector3D w) {
			this.textureCoordinates = Objects.requireNonNull(textureCoordinates, "textureCoordinates == null");
			this.position = Point4D.getCached(Objects.requireNonNull(position, "position == null"));
			this.orthonormalBasis = OrthonormalBasis33D.getCached(new OrthonormalBasis33D(Objects.requireNonNull(w, "w == null")));
		}
		
		/**
		 * Constructs a new {@code Vertex3D} instance.
		 * <p>
		 * If either {@code textureCoordinates}, {@code position}, {@code w} or {@code v} are {@code null}, a {@code NullPointerException} will be thrown.
		 * 
		 * @param textureCoordinates the texture coordinates associated with this {@code Vertex3D} instance
		 * @param position the position associated with this {@code Vertex3D} instance
		 * @param w the W-direction associated with this {@code Vertex3D} instance
		 * @param v the V-direction associated with this {@code Vertex3D} instance
		 * @throws NullPointerException thrown if, and only if, either {@code textureCoordinates}, {@code position}, {@code w} or {@code v} are {@code null}
		 */
		public Vertex3D(final Point2D textureCoordinates, final Point4D position, final Vector3D w, final Vector3D v) {
			this.textureCoordinates = Objects.requireNonNull(textureCoordinates, "textureCoordinates == null");
			this.position = Point4D.getCached(Objects.requireNonNull(position, "position == null"));
			this.orthonormalBasis = OrthonormalBasis33D.getCached(new OrthonormalBasis33D(Objects.requireNonNull(w, "w == null"), Objects.requireNonNull(v, "v == null")));
		}
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		/**
		 * Returns the orthonormal basis associated with this {@code Vertex3D} instance.
		 * 
		 * @return the orthonormal basis associated with this {@code Vertex3D} instance
		 */
		public OrthonormalBasis33D getOrthonormalBasis() {
			return this.orthonormalBasis;
		}
		
		/**
		 * Returns the texture coordinates associated with this {@code Vertex3D} instance.
		 * 
		 * @return the texture coordinates associated with this {@code Vertex3D} instance
		 */
		public Point2D getTextureCoordinates() {
			return this.textureCoordinates;
		}
		
		/**
		 * Returns the position associated with this {@code Vertex3D} instance.
		 * 
		 * @return the position associated with this {@code Vertex3D} instance
		 */
		public Point4D getPosition() {
			return this.position;
		}
		
		/**
		 * Returns a {@code String} representation of this {@code Vertex3D} instance.
		 * 
		 * @return a {@code String} representation of this {@code Vertex3D} instance
		 */
		@Override
		public String toString() {
			return String.format("new Vertex3D(%s, %s, %s)", this.textureCoordinates, this.position, this.orthonormalBasis);
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
		 * Compares {@code object} to this {@code Vertex3D} instance for equality.
		 * <p>
		 * Returns {@code true} if, and only if, {@code object} is an instance of {@code Vertex3D}, and their respective values are equal, {@code false} otherwise.
		 * 
		 * @param object the {@code Object} to compare to this {@code Vertex3D} instance for equality
		 * @return {@code true} if, and only if, {@code object} is an instance of {@code Vertex3D}, and their respective values are equal, {@code false} otherwise
		 */
		@Override
		public boolean equals(final Object object) {
			if(object == this) {
				return true;
			} else if(!(object instanceof Vertex3D)) {
				return false;
			} else if(!Objects.equals(this.orthonormalBasis, Vertex3D.class.cast(object).orthonormalBasis)) {
				return false;
			} else if(!Objects.equals(this.textureCoordinates, Vertex3D.class.cast(object).textureCoordinates)) {
				return false;
			} else if(!Objects.equals(this.position, Vertex3D.class.cast(object).position)) {
				return false;
			} else {
				return true;
			}
		}
		
		/**
		 * Returns a hash code for this {@code Vertex3D} instance.
		 * 
		 * @return a hash code for this {@code Vertex3D} instance
		 */
		@Override
		public int hashCode() {
			return Objects.hash(this.orthonormalBasis, this.textureCoordinates, this.position);
		}
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		/**
		 * Performs a linear interpolation operation on the supplied values.
		 * <p>
		 * Returns a {@code Vertex3D} instance with the result of the linear interpolation operation.
		 * <p>
		 * If either {@code a} or {@code b} are {@code null}, a {@code NullPointerException} will be thrown.
		 * 
		 * @param a a {@code Vertex3D} instance
		 * @param b a {@code Vertex3D} instance
		 * @param t the factor
		 * @return a {@code Vertex3D} instance with the result of the linear interpolation operation
		 * @throws NullPointerException thrown if, and only if, either {@code a} or {@code b} are {@code null}
		 */
		public static Vertex3D lerp(final Vertex3D a, final Vertex3D b, final double t) {
			final Point2D textureCoordinates = Point2D.lerp(a.textureCoordinates, b.textureCoordinates, t);
			
			final Point4D position = Point4D.lerp(a.position, b.position, t);
			
			final OrthonormalBasis33D orthonormalBasis = OrthonormalBasis33D.lerp(a.orthonormalBasis, b.orthonormalBasis, t);
			
			return new Vertex3D(textureCoordinates, position, orthonormalBasis);
		}
		
		/**
		 * Performs a transformation.
		 * <p>
		 * Returns a new {@code Vertex3D} instance with the result of the transformation.
		 * <p>
		 * If either {@code vertex}, {@code matrix} or {@code matrixInverse} are {@code null}, a {@code NullPointerException} will be thrown.
		 * 
		 * @param vertex the {@code Vertex3D} instance to transform
		 * @param matrix the {@link Matrix44D} instance to perform the transformation with
		 * @param matrixInverse the {@code Matrix44D} instance to perform the inverse transformation with
		 * @return a new {@code Vertex3D} instance with the result of the transformation
		 * @throws NullPointerException thrown if, and only if, either {@code vertex}, {@code matrix} or {@code matrixInverse} are {@code null}
		 */
		public static Vertex3D transform(final Vertex3D vertex, final Matrix44D matrix, final Matrix44D matrixInverse) {
			final Point2D textureCoordinates = vertex.textureCoordinates;
			
			final Point4D position = Point4D.transform(matrix, vertex.position);
			
			final OrthonormalBasis33D orthonormalBasis = OrthonormalBasis33D.transform(matrixInverse, vertex.orthonormalBasis);
			
			return new Vertex3D(textureCoordinates, position, orthonormalBasis);
		}
		
		/**
		 * Performs a transformation.
		 * <p>
		 * Returns a new {@code Vertex3D} instance with the result of the transformation.
		 * <p>
		 * If either {@code vertex}, {@code matrix} or {@code matrixInverse} are {@code null}, a {@code NullPointerException} will be thrown.
		 * 
		 * @param vertex the {@code Vertex3D} instance to transform
		 * @param matrix the {@link Matrix44D} instance to perform the transformation with
		 * @param matrixInverse the {@code Matrix44D} instance to perform the inverse transformation with
		 * @return a new {@code Vertex3D} instance with the result of the transformation
		 * @throws NullPointerException thrown if, and only if, either {@code vertex}, {@code matrix} or {@code matrixInverse} are {@code null}
		 */
		public static Vertex3D transformAndDivide(final Vertex3D vertex, final Matrix44D matrix, final Matrix44D matrixInverse) {
			final Point2D textureCoordinates = vertex.textureCoordinates;
			
			final Point4D position = Point4D.transformAndDivide(matrix, vertex.position);
			
			final OrthonormalBasis33D orthonormalBasis = OrthonormalBasis33D.transform(matrixInverse, vertex.orthonormalBasis);
			
			return new Vertex3D(textureCoordinates, position, orthonormalBasis);
		}
	}
}