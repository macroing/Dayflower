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
package org.dayflower.geometry.shape;

import static org.dayflower.util.Floats.abs;
import static org.dayflower.util.Floats.gamma;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.Optional;

import org.dayflower.geometry.BoundingVolume3F;
import org.dayflower.geometry.OrthonormalBasis33F;
import org.dayflower.geometry.Point2F;
import org.dayflower.geometry.Point3F;
import org.dayflower.geometry.Ray3F;
import org.dayflower.geometry.SampleGeneratorF;
import org.dayflower.geometry.Shape3F;
import org.dayflower.geometry.SurfaceIntersection3F;
import org.dayflower.geometry.SurfaceSample3F;
import org.dayflower.geometry.Vector3F;
import org.dayflower.geometry.boundingvolume.AxisAlignedBoundingBox3F;
import org.dayflower.node.Node;
import org.dayflower.node.NodeHierarchicalVisitor;
import org.dayflower.node.NodeTraversalException;

/**
 * A {@code Triangle3F} denotes a 3-dimensional triangle that uses the data type {@code float}.
 * <p>
 * This class is immutable and therefore thread-safe.
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
	public static final int ARRAY_LENGTH = 40;
	
	/**
	 * The offset for the {@link Vector3F} instance representing the V-direction of the {@link OrthonormalBasis33F} of {@link Vertex} {@code A} in the {@code float[]}.
	 */
	public static final int ARRAY_OFFSET_A_ORTHONORMAL_BASIS_V = 24;
	
	/**
	 * The offset for the {@link Vector3F} instance representing the W-direction of the {@link OrthonormalBasis33F} of {@link Vertex} {@code A} in the {@code float[]}.
	 */
	public static final int ARRAY_OFFSET_A_ORTHONORMAL_BASIS_W = 15;
	
	/**
	 * The offset for the {@link Point3F} instance representing the position of {@link Vertex} {@code A} in the {@code float[]}.
	 */
	public static final int ARRAY_OFFSET_A_POSITION = 0;
	
	/**
	 * The offset for the {@link Point2F} instance representing the texture coordinates of {@link Vertex} {@code A} in the {@code float[]}.
	 */
	public static final int ARRAY_OFFSET_A_TEXTURE_COORDINATES = 9;
	
	/**
	 * The offset for the {@link Vector3F} instance representing the V-direction of the {@link OrthonormalBasis33F} of {@link Vertex} {@code B} in the {@code float[]}.
	 */
	public static final int ARRAY_OFFSET_B_ORTHONORMAL_BASIS_V = 27;
	
	/**
	 * The offset for the {@link Vector3F} instance representing the W-direction of the {@link OrthonormalBasis33F} of {@link Vertex} {@code B} in the {@code float[]}.
	 */
	public static final int ARRAY_OFFSET_B_ORTHONORMAL_BASIS_W = 18;
	
	/**
	 * The offset for the {@link Point3F} instance representing the position of {@link Vertex} {@code B} in the {@code float[]}.
	 */
	public static final int ARRAY_OFFSET_B_POSITION = 3;
	
	/**
	 * The offset for the {@link Point2F} instance representing the texture coordinates of {@link Vertex} {@code B} in the {@code float[]}.
	 */
	public static final int ARRAY_OFFSET_B_TEXTURE_COORDINATES = 11;
	
	/**
	 * The offset for the {@link Vector3F} instance representing the V-direction of the {@link OrthonormalBasis33F} of {@link Vertex} {@code C} in the {@code float[]}.
	 */
	public static final int ARRAY_OFFSET_C_ORTHONORMAL_BASIS_V = 30;
	
	/**
	 * The offset for the {@link Vector3F} instance representing the W-direction of the {@link OrthonormalBasis33F} of {@link Vertex} {@code C} in the {@code float[]}.
	 */
	public static final int ARRAY_OFFSET_C_ORTHONORMAL_BASIS_W = 21;
	
	/**
	 * The offset for the {@link Point3F} instance representing the position of {@link Vertex} {@code C} in the {@code float[]}.
	 */
	public static final int ARRAY_OFFSET_C_POSITION = 6;
	
	/**
	 * The offset for the {@link Point2F} instance representing the texture coordinates of {@link Vertex} {@code C} in the {@code float[]}.
	 */
	public static final int ARRAY_OFFSET_C_TEXTURE_COORDINATES = 13;
	
	/**
	 * The ID of this {@code Triangle3F} class.
	 */
	public static final int ID = 9;
	
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
		this.a = new Vertex3F(new Point2F(0.5F, 0.0F), new Point3F(+0.0F, +1.0F, 0.0F), Vector3F.normalNormalized(new Point3F(0.0F, 1.0F, 0.0F), new Point3F(1.0F, -1.0F, 0.0F), new Point3F(-1.0F, -1.0F, 0.0F)));
		this.b = new Vertex3F(new Point2F(1.0F, 1.0F), new Point3F(+1.0F, -1.0F, 0.0F), Vector3F.normalNormalized(new Point3F(0.0F, 1.0F, 0.0F), new Point3F(1.0F, -1.0F, 0.0F), new Point3F(-1.0F, -1.0F, 0.0F)));
		this.c = new Vertex3F(new Point2F(0.0F, 1.0F), new Point3F(-1.0F, -1.0F, 0.0F), Vector3F.normalNormalized(new Point3F(0.0F, 1.0F, 0.0F), new Point3F(1.0F, -1.0F, 0.0F), new Point3F(-1.0F, -1.0F, 0.0F)));
		this.surfaceNormal = Vector3F.getCached(Vector3F.normalNormalized(this.a.getPosition(), this.b.getPosition(), this.c.getPosition()));
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
		this.surfaceNormal = Vector3F.getCached(Vector3F.normalNormalized(a.getPosition(), b.getPosition(), c.getPosition()));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link BoundingVolume3F} instance that contains this {@code Triangle3F} instance.
	 * 
	 * @return a {@code BoundingVolume3F} instance that contains this {@code Triangle3F} instance
	 */
	@Override
	public BoundingVolume3F getBoundingVolume() {
		return new AxisAlignedBoundingBox3F(Point3F.minimum(this.a.getPosition(), this.b.getPosition(), this.c.getPosition()), Point3F.maximum(this.a.getPosition(), this.b.getPosition(), this.c.getPosition()));
	}
	
	/**
	 * Samples this {@code Triangle3F} instance.
	 * <p>
	 * Returns an optional {@link SurfaceSample3F} with the surface sample.
	 * <p>
	 * If either {@code referencePoint} or {@code referenceSurfaceNormal} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param referencePoint the reference point on this {@code Triangle3F} instance
	 * @param referenceSurfaceNormal the reference surface normal on this {@code Triangle3F} instance
	 * @param u a random {@code float} with a uniform distribution between {@code 0.0F} and {@code 1.0F}
	 * @param v a random {@code float} with a uniform distribution between {@code 0.0F} and {@code 1.0F}
	 * @return an optional {@code SurfaceSample3F} with the surface sample
	 * @throws NullPointerException thrown if, and only if, either {@code referencePoint} or {@code referenceSurfaceNormal} are {@code null}
	 */
	@Override
	public Optional<SurfaceSample3F> sample(final Point3F referencePoint, final Vector3F referenceSurfaceNormal, final float u, final float v) {
		Objects.requireNonNull(referenceSurfaceNormal, "referenceSurfaceNormal == null");
		
		final Point3F barycentricCoordinates = SampleGeneratorF.sampleTriangleUniformDistribution(u, v);
		
		final Point3F positionA = this.a.getPosition();
		final Point3F positionB = this.b.getPosition();
		final Point3F positionC = this.c.getPosition();
		
		final Vector3F normalA = this.a.getOrthonormalBasis().getW();
		final Vector3F normalB = this.b.getOrthonormalBasis().getW();
		final Vector3F normalC = this.c.getOrthonormalBasis().getW();
		
		final float x = positionA.getX() * barycentricCoordinates.getX() + positionB.getX() * barycentricCoordinates.getY() + positionC.getX() * barycentricCoordinates.getZ();
		final float y = positionA.getY() * barycentricCoordinates.getX() + positionB.getY() * barycentricCoordinates.getY() + positionC.getY() * barycentricCoordinates.getZ();
		final float z = positionA.getZ() * barycentricCoordinates.getX() + positionB.getZ() * barycentricCoordinates.getY() + positionC.getZ() * barycentricCoordinates.getZ();
		
		final Point3F point = new Point3F(x, y, z);
		
		final Vector3F surfaceNormal = Vector3F.normalNormalized(normalA, normalB, normalC, barycentricCoordinates);
		
		final Vector3F directionToSurface = Vector3F.direction(point, referencePoint);
		final Vector3F directionToSurfaceNormalized = Vector3F.normalize(directionToSurface);
		
		final float probabilityDensityFunctionValue = directionToSurface.lengthSquared() * getSurfaceAreaProbabilityDensityFunctionValue() / abs(Vector3F.dotProduct(directionToSurfaceNormalized, surfaceNormal));
		
		return Optional.of(new SurfaceSample3F(point, surfaceNormal, probabilityDensityFunctionValue));
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
		final Point3F a = this.a.getPosition();
		final Point3F b = this.b.getPosition();
		final Point3F c = this.c.getPosition();
		
		final Vector3F edgeAB = Vector3F.direction(a, b);
		final Vector3F edgeAC = Vector3F.direction(a, c);
		final Vector3F direction0 = ray.getDirection();
		final Vector3F direction1 = Vector3F.crossProduct(direction0, edgeAC);
		
		final float determinant = Vector3F.dotProduct(edgeAB, direction1);
		
		if(determinant >= -0.0001F && determinant <= 0.0001F) {
			return SurfaceIntersection3F.EMPTY;
		}
		
		final Point3F origin = ray.getOrigin();
		
		final Vector3F direction2 = Vector3F.direction(a, origin);
		
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
		
		final OrthonormalBasis33F orthonormalBasisG = new OrthonormalBasis33F(gW, gV);
		final OrthonormalBasis33F orthonormalBasisS = new OrthonormalBasis33F(sW, sV);
		
		final float xAbsSum = abs(barycentricCoordinates.getU() + a.getX()) + abs(barycentricCoordinates.getV() + b.getX()) + abs(barycentricCoordinates.getW() + c.getX());
		final float yAbsSum = abs(barycentricCoordinates.getU() + a.getY()) + abs(barycentricCoordinates.getV() + b.getY()) + abs(barycentricCoordinates.getW() + c.getY());
		final float zAbsSum = abs(barycentricCoordinates.getU() + a.getZ()) + abs(barycentricCoordinates.getV() + b.getZ()) + abs(barycentricCoordinates.getW() + c.getZ());
		
		final Vector3F surfaceIntersectionPointError = Vector3F.multiply(new Vector3F(xAbsSum, yAbsSum, zAbsSum), gamma(7));
		
		return Optional.of(new SurfaceIntersection3F(orthonormalBasisG, orthonormalBasisS, textureCoordinates, surfaceIntersectionPoint, ray, this, surfaceIntersectionPointError, t));
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
	 * Returns the probability density function (PDF) value for solid angle.
	 * <p>
	 * If either {@code referencePoint}, {@code referenceSurfaceNormal}, {@code point} or {@code surfaceNormal} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param referencePoint the reference point on this {@code Triangle3F} instance
	 * @param referenceSurfaceNormal the reference surface normal on this {@code Triangle3F} instance
	 * @param point the point on this {@code Triangle3F} instance
	 * @param surfaceNormal the surface normal on this {@code Triangle3F} instance
	 * @return the probability density function (PDF) value for solid angle
	 * @throws NullPointerException thrown if, and only if, either {@code referencePoint}, {@code referenceSurfaceNormal}, {@code point} or {@code surfaceNormal} are {@code null}
	 */
	@Override
	public float calculateProbabilityDensityFunctionValueForSolidAngle(final Point3F referencePoint, final Vector3F referenceSurfaceNormal, final Point3F point, final Vector3F surfaceNormal) {
		Objects.requireNonNull(referenceSurfaceNormal, "referenceSurfaceNormal == null");
		
		final Vector3F directionToSurface = Vector3F.direction(point, referencePoint);
		final Vector3F directionToSurfaceNormalized = Vector3F.normalize(directionToSurface);
		
		final float probabilityDensityFunctionValue = directionToSurface.lengthSquared() * getSurfaceAreaProbabilityDensityFunctionValue() / abs(Vector3F.dotProduct(directionToSurfaceNormalized, surfaceNormal));
		
		return probabilityDensityFunctionValue;
	}
	
	/**
	 * Returns the probability density function (PDF) value for solid angle.
	 * <p>
	 * If either {@code referencePoint}, {@code referenceSurfaceNormal} or {@code direction} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param referencePoint the reference point on this {@code Triangle3F} instance
	 * @param referenceSurfaceNormal the reference surface normal on this {@code Triangle3F} instance
	 * @param direction the direction to this {@code Triangle3F} instance
	 * @return the probability density function (PDF) value for solid angle
	 * @throws NullPointerException thrown if, and only if, either {@code referencePoint}, {@code referenceSurfaceNormal} or {@code direction} are {@code null}
	 */
	@Override
	public float calculateProbabilityDensityFunctionValueForSolidAngle(final Point3F referencePoint, final Vector3F referenceSurfaceNormal, final Vector3F direction) {
		Objects.requireNonNull(referencePoint, "referencePoint == null");
		Objects.requireNonNull(referenceSurfaceNormal, "referenceSurfaceNormal == null");
		Objects.requireNonNull(direction, "direction == null");
		
//		TODO: Check if these variables should be supplied as parameters?
		final float tMinimum = 0.001F;
		final float tMaximum = Float.MAX_VALUE;
		
		final Optional<SurfaceIntersection3F> optionalSurfaceIntersection = intersection(new Ray3F(referencePoint, direction), tMinimum, tMaximum);
		
		if(optionalSurfaceIntersection.isPresent()) {
			final SurfaceIntersection3F surfaceIntersection = optionalSurfaceIntersection.get();
			
			final Point3F point = surfaceIntersection.getSurfaceIntersectionPoint();
			
			final Vector3F surfaceNormal = surfaceIntersection.getOrthonormalBasisS().getW();//.getSurfaceNormalS();
			
			return calculateProbabilityDensityFunctionValueForSolidAngle(referencePoint, referenceSurfaceNormal, point, surfaceNormal);
		}
		
		return 0.0F;
	}
	
	/**
	 * Returns the surface area of this {@code Triangle3F} instance.
	 * 
	 * @return the surface area of this {@code Triangle3F} instance
	 */
	@Override
	public float getSurfaceArea() {
		final Point3F a = this.a.getPosition();
		final Point3F b = this.b.getPosition();
		final Point3F c = this.c.getPosition();
		
		final Vector3F edgeAB = Vector3F.direction(a, b);
		final Vector3F edgeAC = Vector3F.direction(a, c);
		final Vector3F edgeABCrossEdgeAC = Vector3F.crossProduct(edgeAB, edgeAC);
		
		return edgeABCrossEdgeAC.length() * 0.5F;
	}
	
	/**
	 * Returns the surface area probability density function (PDF) value of this {@code Triangle3F} instance.
	 * 
	 * @return the surface area probability density function (PDF) value of this {@code Triangle3F} instance
	 */
	@Override
	public float getSurfaceAreaProbabilityDensityFunctionValue() {
		return 1.0F / getSurfaceArea();
	}
	
	/**
	 * Returns the volume of this {@code Triangle3F} instance.
	 * <p>
	 * This method returns {@code 0.0F}.
	 * 
	 * @return the volume of this {@code Triangle3F} instance
	 */
	@Override
	public float getVolume() {
		return 0.0F;
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
		final Point3F a = this.a.getPosition();
		final Point3F b = this.b.getPosition();
		final Point3F c = this.c.getPosition();
		
		final Vector3F edgeAB = Vector3F.direction(a, b);
		final Vector3F edgeAC = Vector3F.direction(a, c);
		final Vector3F direction0 = ray.getDirection();
		final Vector3F direction1 = Vector3F.crossProduct(direction0, edgeAC);
		
		final float determinant = Vector3F.dotProduct(edgeAB, direction1);
		
		if(determinant >= -0.0001F && determinant <= 0.0001F) {
			return Float.NaN;
		}
		
		final Point3F origin = ray.getOrigin();
		
		final Vector3F direction2 = Vector3F.direction(a, origin);
		
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
		
		array[ARRAY_OFFSET_A_ORTHONORMAL_BASIS_V + 0] = this.a.getOrthonormalBasis().getV().getX();		//Block #4
		array[ARRAY_OFFSET_A_ORTHONORMAL_BASIS_V + 1] = this.a.getOrthonormalBasis().getV().getY();		//Block #4
		array[ARRAY_OFFSET_A_ORTHONORMAL_BASIS_V + 2] = this.a.getOrthonormalBasis().getV().getZ();		//Block #4
		array[ARRAY_OFFSET_B_ORTHONORMAL_BASIS_V + 0] = this.b.getOrthonormalBasis().getV().getX();		//Block #4
		array[ARRAY_OFFSET_B_ORTHONORMAL_BASIS_V + 1] = this.b.getOrthonormalBasis().getV().getY();		//Block #4
		array[ARRAY_OFFSET_B_ORTHONORMAL_BASIS_V + 2] = this.b.getOrthonormalBasis().getV().getZ();		//Block #4
		array[ARRAY_OFFSET_C_ORTHONORMAL_BASIS_V + 0] = this.c.getOrthonormalBasis().getV().getX();		//Block #4
		array[ARRAY_OFFSET_C_ORTHONORMAL_BASIS_V + 1] = this.c.getOrthonormalBasis().getV().getY();		//Block #4
		array[ARRAY_OFFSET_C_ORTHONORMAL_BASIS_V + 2] = this.c.getOrthonormalBasis().getV().getZ();		//Block #5
		
//		Add padding:
		array[33] = 0.0F;																				//Block #5
		array[34] = 0.0F;																				//Block #5
		array[35] = 0.0F;																				//Block #5
		array[36] = 0.0F;																				//Block #5
		array[37] = 0.0F;																				//Block #5
		array[38] = 0.0F;																				//Block #5
		array[39] = 0.0F;																				//Block #5
		
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
		private final Point3F position;
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
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
		public Vertex3F(final Point2F textureCoordinates, final Point3F position, final Vector3F w) {
			this.textureCoordinates = Objects.requireNonNull(textureCoordinates, "textureCoordinates == null");
			this.position = Point3F.getCached(Objects.requireNonNull(position, "position == null"));
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
		public Vertex3F(final Point2F textureCoordinates, final Point3F position, final Vector3F w, final Vector3F v) {
			this.textureCoordinates = Objects.requireNonNull(textureCoordinates, "textureCoordinates == null");
			this.position = Point3F.getCached(Objects.requireNonNull(position, "position == null"));
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
		public Point3F getPosition() {
			return this.position;
		}
		
		/**
		 * Returns a {@code String} representation of this {@code Vertex3F} instance.
		 * 
		 * @return a {@code String} representation of this {@code Vertex3F} instance
		 */
		@Override
		public String toString() {
			return String.format("new Vertex3F(%s, %s, %s)", this.orthonormalBasis, this.textureCoordinates, this.position);
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
	}
}