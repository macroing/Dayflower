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
import static org.dayflower.utility.Floats.isNaN;
import static org.dayflower.utility.Floats.isZero;

import java.io.DataOutput;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.Field;//TODO: Add Unit Tests!
import java.util.HashMap;
import java.util.Map;
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
//	TODO: Add Unit Tests!
	public static final String NAME = "Triangle";
	
	/**
	 * The ID of this {@code Triangle3F} class.
	 */
//	TODO: Add Unit Tests!
	public static final int ID = 17;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final Vector3F surfaceNormal;
	private final Vertex3F a;
	private final Vertex3F b;
	private final Vertex3F c;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code Triangle3F} instance.
	 */
//	TODO: Add Unit Tests!
	public Triangle3F() {
		this(new Point3F(0.0F, 1.0F, 0.0F), new Point3F(1.0F, -1.0F, 0.0F), new Point3F(-1.0F, -1.0F, 0.0F));
	}
	
	/**
	 * Constructs a new {@code Triangle3F} instance.
	 * <p>
	 * If either {@code a}, {@code b} or {@code c} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param a a {@link Point3F} instance used as part of the position for the {@link Vertex3F} instance denoted by {@code A}
	 * @param b a {@code Point3F} instance used as part of the position for the {@code Vertex3F} instance denoted by {@code B}
	 * @param c a {@code Point3F} instance used as part of the position for the {@code Vertex3F} instance denoted by {@code C}
	 * @throws NullPointerException thrown if, and only if, either {@code a}, {@code b} or {@code c} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public Triangle3F(final Point3F a, final Point3F b, final Point3F c) {
		this.surfaceNormal = Vector3F.getCached(Vector3F.normalNormalized(a, b, c));
		this.a = Vertex3F.getCached(new Vertex3F(new Point2F(0.5F, 0.0F), new Point4F(a), this.surfaceNormal));
		this.b = Vertex3F.getCached(new Vertex3F(new Point2F(1.0F, 1.0F), new Point4F(b), this.surfaceNormal));
		this.c = Vertex3F.getCached(new Vertex3F(new Point2F(0.0F, 1.0F), new Point4F(c), this.surfaceNormal));
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
//	TODO: Add Unit Tests!
	public Triangle3F(final Vertex3F a, final Vertex3F b, final Vertex3F c) {
		this.a = Vertex3F.getCached(Objects.requireNonNull(a, "a == null"));
		this.b = Vertex3F.getCached(Objects.requireNonNull(b, "b == null"));
		this.c = Vertex3F.getCached(Objects.requireNonNull(c, "c == null"));
		this.surfaceNormal = Vector3F.getCached(Vector3F.normalNormalized(new Point3F(a.getPosition()), new Point3F(b.getPosition()), new Point3F(c.getPosition())));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link BoundingVolume3F} instance that contains this {@code Triangle3F} instance.
	 * 
	 * @return a {@code BoundingVolume3F} instance that contains this {@code Triangle3F} instance
	 */
//	TODO: Add Unit Tests!
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
//	TODO: Add Unit Tests!
	@Override
	public Optional<SurfaceSample3F> sample(final Point2F sample) {
		Objects.requireNonNull(sample, "sample == null");
		
		final Point3F barycentricCoordinates = SampleGeneratorF.sampleTriangleUniformDistribution(sample.getU(), sample.getV());
		
		final Point4F positionA = this.a.getPosition();
		final Point4F positionB = this.b.getPosition();
		final Point4F positionC = this.c.getPosition();
		
		final Vector3F normalA = this.a.getNormal();
		final Vector3F normalB = this.b.getNormal();
		final Vector3F normalC = this.c.getNormal();
		
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
//	TODO: Add Unit Tests!
	@Override
	public Optional<SurfaceIntersection3F> intersection(final Ray3F ray, final float tMinimum, final float tMaximum) {
		final float t = intersectionT(ray, tMinimum, tMaximum);
		
		if(isNaN(t)) {
			return SurfaceIntersection3F.EMPTY;
		}
		
		return Optional.of(doCreateSurfaceIntersection(ray, t));
	}
	
	/**
	 * Returns a {@code String} with the name of this {@code Triangle3F} instance.
	 * 
	 * @return a {@code String} with the name of this {@code Triangle3F} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public String getName() {
		return NAME;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code Triangle3F} instance.
	 * 
	 * @return a {@code String} representation of this {@code Triangle3F} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public String toString() {
		return String.format("new Triangle3F(%s, %s, %s)", this.a, this.b, this.c);
	}
	
	/**
	 * Returns the bitangent of this {@code Triangle3F} instance.
	 * 
	 * @return the bitangent of this {@code Triangle3F} instance
	 */
//	TODO: Add Unit Tests!
	public Vector3F calculateBitangent() {
		final Point2F textureCoordinatesA = this.a.getTextureCoordinates();
		final Point2F textureCoordinatesB = this.b.getTextureCoordinates();
		final Point2F textureCoordinatesC = this.c.getTextureCoordinates();
		
		final Point4F positionA = this.a.getPosition();
		final Point4F positionB = this.b.getPosition();
		final Point4F positionC = this.c.getPosition();
		
		final Vector3F edgeAB = Vector3F.direction(positionA, positionB);
		final Vector3F edgeAC = Vector3F.direction(positionA, positionC);
		
		final float deltaABU = textureCoordinatesB.getU() - textureCoordinatesA.getU();
		final float deltaABV = textureCoordinatesB.getV() - textureCoordinatesA.getV();
		final float deltaACU = textureCoordinatesC.getU() - textureCoordinatesA.getU();
		final float deltaACV = textureCoordinatesC.getV() - textureCoordinatesA.getV();
		
		final float dividend = (deltaABU * deltaACV - deltaACU * deltaABV);
		final float fraction = dividend < -0.0F || dividend > +0.0F ? 1.0F / dividend : 0.0F;
		
		final float x = fraction * (-deltaACU * edgeAB.getX() + deltaABU * edgeAC.getX());
		final float y = fraction * (-deltaACU * edgeAB.getY() + deltaABU * edgeAC.getY());
		final float z = fraction * (-deltaACU * edgeAB.getZ() + deltaABU * edgeAC.getZ());
		
		return Vector3F.normalize(new Vector3F(x, y, z));
	}
	
	/**
	 * Returns the tangent of this {@code Triangle3F} instance.
	 * 
	 * @return the tangent of this {@code Triangle3F} instance
	 */
//	TODO: Add Unit Tests!
	public Vector3F calculateTangent() {
		final Point2F textureCoordinatesA = this.a.getTextureCoordinates();
		final Point2F textureCoordinatesB = this.b.getTextureCoordinates();
		final Point2F textureCoordinatesC = this.c.getTextureCoordinates();
		
		final Point4F positionA = this.a.getPosition();
		final Point4F positionB = this.b.getPosition();
		final Point4F positionC = this.c.getPosition();
		
		final Vector3F edgeAB = Vector3F.direction(positionA, positionB);
		final Vector3F edgeAC = Vector3F.direction(positionA, positionC);
		
		final float deltaABU = textureCoordinatesB.getU() - textureCoordinatesA.getU();
		final float deltaABV = textureCoordinatesB.getV() - textureCoordinatesA.getV();
		final float deltaACU = textureCoordinatesC.getU() - textureCoordinatesA.getU();
		final float deltaACV = textureCoordinatesC.getV() - textureCoordinatesA.getV();
		
		final float dividend = (deltaABU * deltaACV - deltaACU * deltaABV);
		final float fraction = dividend < -0.0F || dividend > +0.0F ? 1.0F / dividend : 0.0F;
		
		final float x = fraction * (deltaACV * edgeAB.getX() - deltaABV * edgeAC.getX());
		final float y = fraction * (deltaACV * edgeAB.getY() - deltaABV * edgeAC.getY());
		final float z = fraction * (deltaACV * edgeAB.getZ() - deltaABV * edgeAC.getZ());
		
		return Vector3F.normalize(new Vector3F(x, y, z));
	}
	
	/**
	 * Returns the surface normal associated with this {@code Triangle3F} instance.
	 * 
	 * @return the surface normal associated with this {@code Triangle3F} instance
	 */
//	TODO: Add Unit Tests!
	public Vector3F getSurfaceNormal() {
		return this.surfaceNormal;
	}
	
	/**
	 * Returns the vertex denoted by {@code A} and is associated with this {@code Triangle3F} instance.
	 * 
	 * @return the vertex denoted by {@code A} and is associated with this {@code Triangle3F} instance
	 */
//	TODO: Add Unit Tests!
	public Vertex3F getA() {
		return this.a;
	}
	
	/**
	 * Returns the vertex denoted by {@code B} and is associated with this {@code Triangle3F} instance.
	 * 
	 * @return the vertex denoted by {@code B} and is associated with this {@code Triangle3F} instance
	 */
//	TODO: Add Unit Tests!
	public Vertex3F getB() {
		return this.b;
	}
	
	/**
	 * Returns the vertex denoted by {@code C} and is associated with this {@code Triangle3F} instance.
	 * 
	 * @return the vertex denoted by {@code C} and is associated with this {@code Triangle3F} instance
	 */
//	TODO: Add Unit Tests!
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
//	TODO: Add Unit Tests!
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
	 * Returns {@code true} if, and only if, {@code point} is contained in this {@code Triangle3F} instance, {@code false} otherwise.
	 * <p>
	 * If {@code point} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param point a {@link Point3F} instance
	 * @return {@code true} if, and only if, {@code point} is contained in this {@code Triangle3F} instance, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, {@code point} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public boolean contains(final Point3F point) {
		final Point3F a = new Point3F(this.a.getPosition());
		final Point3F b = new Point3F(this.b.getPosition());
		final Point3F c = new Point3F(this.c.getPosition());
		final Point3F p = Objects.requireNonNull(point, "point == null");
		
		if(Point3F.coplanar(a, b, c, point)) {
			final Vector3F surfaceNormal = this.surfaceNormal;
			
			final Vector3F edgeAB = Vector3F.direction(a, b);
			final Vector3F edgeBC = Vector3F.direction(b, c);
			final Vector3F edgeCA = Vector3F.direction(c, a);
			
			final Vector3F edgeAP = Vector3F.direction(a, p);
			final Vector3F edgeBP = Vector3F.direction(b, p);
			final Vector3F edgeCP = Vector3F.direction(c, p);
			
			final boolean isInsideA = Vector3F.tripleProduct(surfaceNormal, edgeAB, edgeAP) > 0.0F;
			final boolean isInsideB = Vector3F.tripleProduct(surfaceNormal, edgeBC, edgeBP) > 0.0F;
			final boolean isInsideC = Vector3F.tripleProduct(surfaceNormal, edgeCA, edgeCP) > 0.0F;
			final boolean isInside = isInsideA && isInsideB && isInsideC;
			
			return isInside;
		}
		
		return false;
	}
	
	/**
	 * Compares {@code object} to this {@code Triangle3F} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code Triangle3F}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code Triangle3F} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code Triangle3F}, and their respective values are equal, {@code false} otherwise
	 */
//	TODO: Add Unit Tests!
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
//	TODO: Add Unit Tests!
	@Override
	public float getSurfaceArea() {
		final Point4F a = this.a.getPosition();
		final Point4F b = this.b.getPosition();
		final Point4F c = this.c.getPosition();
		
		final Vector3F edgeAB = Vector3F.direction(a, b);
		final Vector3F edgeAC = Vector3F.direction(a, c);
		final Vector3F edgeABCrossEdgeAC = Vector3F.crossProduct(edgeAB, edgeAC);
		
		return edgeABCrossEdgeAC.length() * 0.5F;
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
//	TODO: Add Unit Tests!
	@Override
	public float intersectionT(final Ray3F ray, final float tMinimum, final float tMaximum) {
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
	
	/**
	 * Returns an {@code int} with the ID of this {@code Triangle3F} instance.
	 * 
	 * @return an {@code int} with the ID of this {@code Triangle3F} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public int getID() {
		return ID;
	}
	
	/**
	 * Returns a hash code for this {@code Triangle3F} instance.
	 * 
	 * @return a hash code for this {@code Triangle3F} instance
	 */
//	TODO: Add Unit Tests!
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
//	TODO: Add Unit Tests!
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
		private static final Map<Vertex3F, Vertex3F> CACHE = new HashMap<>();
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		private final Point2F textureCoordinates;
		private final Point4F position;
		private final Vector3F normal;
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		/**
		 * Constructs a new {@code Vertex3F} instance.
		 * <p>
		 * If either {@code textureCoordinates}, {@code position} or {@code normal} are {@code null}, a {@code NullPointerException} will be thrown.
		 * 
		 * @param textureCoordinates the texture coordinates associated with this {@code Vertex3F} instance
		 * @param position the position associated with this {@code Vertex3F} instance
		 * @param normal the normal associated with this {@code Vertex3F} instance
		 * @throws NullPointerException thrown if, and only if, either {@code textureCoordinates}, {@code position} or {@code normal} are {@code null}
		 */
//		TODO: Add Unit Tests!
		public Vertex3F(final Point2F textureCoordinates, final Point4F position, final Vector3F normal) {
			this.textureCoordinates = Objects.requireNonNull(textureCoordinates, "textureCoordinates == null");
			this.position = Point4F.getCached(Objects.requireNonNull(position, "position == null"));
			this.normal = Vector3F.getCached(Objects.requireNonNull(normal, "normal == null"));
		}
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		/**
		 * Returns the texture coordinates associated with this {@code Vertex3F} instance.
		 * 
		 * @return the texture coordinates associated with this {@code Vertex3F} instance
		 */
//		TODO: Add Unit Tests!
		public Point2F getTextureCoordinates() {
			return this.textureCoordinates;
		}
		
		/**
		 * Returns the position associated with this {@code Vertex3F} instance.
		 * 
		 * @return the position associated with this {@code Vertex3F} instance
		 */
//		TODO: Add Unit Tests!
		public Point4F getPosition() {
			return this.position;
		}
		
		/**
		 * Returns a {@code String} representation of this {@code Vertex3F} instance.
		 * 
		 * @return a {@code String} representation of this {@code Vertex3F} instance
		 */
//		TODO: Add Unit Tests!
		@Override
		public String toString() {
			return String.format("new Vertex3F(%s, %s, %s)", this.textureCoordinates, this.position, this.normal);
		}
		
		/**
		 * Returns the normal associated with this {@code Vertex3F} instance.
		 * 
		 * @return the normal associated with this {@code Vertex3F} instance
		 */
//		TODO: Add Unit Tests!
		public Vector3F getNormal() {
			return this.normal;
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
//		TODO: Add Unit Tests!
		@Override
		public boolean accept(final NodeHierarchicalVisitor nodeHierarchicalVisitor) {
			Objects.requireNonNull(nodeHierarchicalVisitor, "nodeHierarchicalVisitor == null");
			
			try {
				if(nodeHierarchicalVisitor.visitEnter(this)) {
					if(!this.textureCoordinates.accept(nodeHierarchicalVisitor)) {
						return nodeHierarchicalVisitor.visitLeave(this);
					}
					
					if(!this.position.accept(nodeHierarchicalVisitor)) {
						return nodeHierarchicalVisitor.visitLeave(this);
					}
					
					if(!this.normal.accept(nodeHierarchicalVisitor)) {
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
//		TODO: Add Unit Tests!
		@Override
		public boolean equals(final Object object) {
			if(object == this) {
				return true;
			} else if(!(object instanceof Vertex3F)) {
				return false;
			} else if(!Objects.equals(this.textureCoordinates, Vertex3F.class.cast(object).textureCoordinates)) {
				return false;
			} else if(!Objects.equals(this.position, Vertex3F.class.cast(object).position)) {
				return false;
			} else if(!Objects.equals(this.normal, Vertex3F.class.cast(object).normal)) {
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
//		TODO: Add Unit Tests!
		@Override
		public int hashCode() {
			return Objects.hash(this.textureCoordinates, this.position, this.normal);
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
//		TODO: Add Unit Tests!
		public void write(final DataOutput dataOutput) {
			this.textureCoordinates.write(dataOutput);
			this.position.write(dataOutput);
			this.normal.write(dataOutput);
		}
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		/**
		 * Returns a cached version of {@code vertex}.
		 * <p>
		 * If {@code vertex} is {@code null}, a {@code NullPointerException} will be thrown.
		 * 
		 * @param vertex a {@code Vertex3F} instance
		 * @return a cached version of {@code vertex}
		 * @throws NullPointerException thrown if, and only if, {@code vertex} is {@code null}
		 */
//		TODO: Add Unit Tests!
		public static Vertex3F getCached(final Vertex3F vertex) {
			return CACHE.computeIfAbsent(Objects.requireNonNull(vertex, "vertex == null"), key -> vertex);
		}
		
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
//		TODO: Add Unit Tests!
		public static Vertex3F lerp(final Vertex3F a, final Vertex3F b, final float t) {
			final Point2F textureCoordinates = Point2F.lerp(a.textureCoordinates, b.textureCoordinates, t);
			
			final Point4F position = Point4F.lerp(a.position, b.position, t);
			
			final Vector3F normal = Vector3F.normalize(Vector3F.lerp(a.normal, b.normal, t));
			
			return new Vertex3F(textureCoordinates, position, normal);
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
//		TODO: Add Unit Tests!
		public static Vertex3F transform(final Vertex3F vertex, final Matrix44F matrix, final Matrix44F matrixInverse) {
			final Point2F textureCoordinates = vertex.textureCoordinates;
			
			final Point4F position = Point4F.transform(matrix, vertex.position);
			
			final Vector3F normal = Vector3F.normalize(Vector3F.transformTranspose(matrixInverse, vertex.normal));
			
			return new Vertex3F(textureCoordinates, position, normal);
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
//		TODO: Add Unit Tests!
		public static Vertex3F transformAndDivide(final Vertex3F vertex, final Matrix44F matrix, final Matrix44F matrixInverse) {
			final Point2F textureCoordinates = vertex.textureCoordinates;
			
			final Point4F position = Point4F.transformAndDivide(matrix, vertex.position);
			
			final Vector3F normal = Vector3F.normalize(Vector3F.transformTranspose(matrixInverse, vertex.normal));
			
			return new Vertex3F(textureCoordinates, position, normal);
		}
		
		/**
		 * Returns the size of the cache.
		 * 
		 * @return the size of the cache
		 */
//		TODO: Add Unit Tests!
		public static int getCacheSize() {
			return CACHE.size();
		}
		
		/**
		 * Clears the cache.
		 */
//		TODO: Add Unit Tests!
		public static void clearCache() {
			CACHE.clear();
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private OrthonormalBasis33F doCreateOrthonormalBasisG() {
		return new OrthonormalBasis33F(this.surfaceNormal);
	}
	
	private OrthonormalBasis33F doCreateOrthonormalBasisS(final Point3F barycentricCoordinates) {
		final Point2F textureCoordinatesA = this.a.getTextureCoordinates();
		final Point2F textureCoordinatesB = this.b.getTextureCoordinates();
		final Point2F textureCoordinatesC = this.c.getTextureCoordinates();
		
		final Vector2F textureCoordinatesCA = Vector2F.direction(textureCoordinatesC, textureCoordinatesA);
		final Vector2F textureCoordinatesCB = Vector2F.direction(textureCoordinatesC, textureCoordinatesB);
		
		final Point4F a = this.a.getPosition();
		final Point4F b = this.b.getPosition();
		final Point4F c = this.c.getPosition();
		
		final Vector3F edgeCA = Vector3F.direction(c, a);
		final Vector3F edgeCB = Vector3F.direction(c, b);
		
		final Vector3F normalA = this.a.getNormal();
		final Vector3F normalB = this.b.getNormal();
		final Vector3F normalC = this.c.getNormal();
		
		final Vector3F w = Vector3F.normalNormalized(normalA, normalB, normalC, barycentricCoordinates);
		
		final float determinant = Vector2F.crossProduct(textureCoordinatesCA, textureCoordinatesCB);
		
		if(isZero(determinant)) {
			return new OrthonormalBasis33F(w);
		}
		
		final float determinantReciprocal = 1.0F / determinant;
		
		final float x = (-textureCoordinatesCB.getU() * edgeCA.getX() + textureCoordinatesCA.getU() * edgeCB.getX()) * determinantReciprocal;
		final float y = (-textureCoordinatesCB.getU() * edgeCA.getY() + textureCoordinatesCA.getU() * edgeCB.getY()) * determinantReciprocal;
		final float z = (-textureCoordinatesCB.getU() * edgeCA.getZ() + textureCoordinatesCA.getU() * edgeCB.getZ()) * determinantReciprocal;
		
		final Vector3F v = new Vector3F(x, y, z);
		
		return new OrthonormalBasis33F(w, v);
	}
	
	private Point2F doCreateTextureCoordinates(final Point3F barycentricCoordinates) {
		final Point2F textureCoordinatesA = this.a.getTextureCoordinates();
		final Point2F textureCoordinatesB = this.b.getTextureCoordinates();
		final Point2F textureCoordinatesC = this.c.getTextureCoordinates();
		final Point2F textureCoordinates = Point2F.createTextureCoordinates(textureCoordinatesA, textureCoordinatesB, textureCoordinatesC, barycentricCoordinates);
		
		return textureCoordinates;
	}
	
	private Point3F doCreateBarycentricCoordinates(final Ray3F ray) {
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
		final Vector3F direction3 = Vector3F.crossProduct(direction2, direction0);
		
		final float u = Vector3F.dotProduct(direction3, edgeCA) * determinantReciprocal;
		final float v = Vector3F.dotProduct(direction3, edgeAB) * determinantReciprocal;
		final float w = 1.0F - u - v;
		
		return new Point3F(w, u, v);
	}
	
	private SurfaceIntersection3F doCreateSurfaceIntersection(final Ray3F ray, final float t) {
		final Point3F barycentricCoordinates = doCreateBarycentricCoordinates(ray);
		final Point3F surfaceIntersectionPoint = doCreateSurfaceIntersectionPoint(ray, t);
		
		final Point2F textureCoordinates = doCreateTextureCoordinates(barycentricCoordinates);
		
		final OrthonormalBasis33F orthonormalBasisG = doCreateOrthonormalBasisG();
		final OrthonormalBasis33F orthonormalBasisS = doCreateOrthonormalBasisS(barycentricCoordinates);
		
		final Vector3F surfaceIntersectionPointError = doCreateSurfaceIntersectionPointError(barycentricCoordinates);
		
		return new SurfaceIntersection3F(orthonormalBasisG, orthonormalBasisS, textureCoordinates, surfaceIntersectionPoint, ray, this, surfaceIntersectionPointError, t);
	}
	
	private Vector3F doCreateSurfaceIntersectionPointError(final Point3F barycentricCoordinates) {
		final Point4F a = this.a.getPosition();
		final Point4F b = this.b.getPosition();
		final Point4F c = this.c.getPosition();
		
		final float xAbsSum = abs(barycentricCoordinates.getU() + a.getX()) + abs(barycentricCoordinates.getV() + b.getX()) + abs(barycentricCoordinates.getW() + c.getX());
		final float yAbsSum = abs(barycentricCoordinates.getU() + a.getY()) + abs(barycentricCoordinates.getV() + b.getY()) + abs(barycentricCoordinates.getW() + c.getY());
		final float zAbsSum = abs(barycentricCoordinates.getU() + a.getZ()) + abs(barycentricCoordinates.getV() + b.getZ()) + abs(barycentricCoordinates.getW() + c.getZ());
		
		return Vector3F.multiply(new Vector3F(xAbsSum, yAbsSum, zAbsSum), gamma(7));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static Point3F doCreateSurfaceIntersectionPoint(final Ray3F ray, final float t) {
		return Point3F.add(ray.getOrigin(), ray.getDirection(), t);
	}
}