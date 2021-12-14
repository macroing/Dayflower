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
import static org.dayflower.utility.Doubles.isNaN;
import static org.dayflower.utility.Doubles.isZero;

import java.io.DataOutput;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.Field;//TODO: Add Unit Tests!
import java.util.HashMap;
import java.util.Map;
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
import org.dayflower.geometry.Vector2D;
import org.dayflower.geometry.Vector3D;
import org.dayflower.geometry.boundingvolume.AxisAlignedBoundingBox3D;
import org.dayflower.node.Node;
import org.dayflower.node.NodeHierarchicalVisitor;
import org.dayflower.node.NodeTraversalException;

/**
 * A {@code Triangle3D} is an implementation of {@link Shape3D} that represents a triangle.
 * <p>
 * This class is immutable and therefore thread-safe.
 * <p>
 * This {@code Shape3D} implementation is not supported on the GPU.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class Triangle3D implements Shape3D {
	/**
	 * The name of this {@code Triangle3D} class.
	 */
//	TODO: Add Unit Tests!
	public static final String NAME = "Triangle";
	
	/**
	 * The ID of this {@code Triangle3D} class.
	 */
//	TODO: Add Unit Tests!
	public static final int ID = 17;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final Vector3D surfaceNormal;
	private final Vertex3D a;
	private final Vertex3D b;
	private final Vertex3D c;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code Triangle3D} instance.
	 */
//	TODO: Add Unit Tests!
	public Triangle3D() {
		this(new Point3D(0.0D, 1.0D, 0.0D), new Point3D(1.0D, -1.0D, 0.0D), new Point3D(-1.0D, -1.0D, 0.0D));
	}
	
	/**
	 * Constructs a new {@code Triangle3D} instance.
	 * <p>
	 * If either {@code a}, {@code b} or {@code c} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param a a {@link Point3D} instance used as part of the position for the {@link Vertex3D} instance denoted by {@code A}
	 * @param b a {@code Point3D} instance used as part of the position for the {@code Vertex3D} instance denoted by {@code B}
	 * @param c a {@code Point3D} instance used as part of the position for the {@code Vertex3D} instance denoted by {@code C}
	 * @throws NullPointerException thrown if, and only if, either {@code a}, {@code b} or {@code c} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public Triangle3D(final Point3D a, final Point3D b, final Point3D c) {
		this.surfaceNormal = Vector3D.getCached(Vector3D.normalNormalized(a, b, c));
		this.a = Vertex3D.getCached(new Vertex3D(new Point2D(0.5D, 0.0D), new Point4D(a), this.surfaceNormal));
		this.b = Vertex3D.getCached(new Vertex3D(new Point2D(1.0D, 1.0D), new Point4D(b), this.surfaceNormal));
		this.c = Vertex3D.getCached(new Vertex3D(new Point2D(0.0D, 1.0D), new Point4D(c), this.surfaceNormal));
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
//	TODO: Add Unit Tests!
	public Triangle3D(final Vertex3D a, final Vertex3D b, final Vertex3D c) {
		this.a = Vertex3D.getCached(Objects.requireNonNull(a, "a == null"));
		this.b = Vertex3D.getCached(Objects.requireNonNull(b, "b == null"));
		this.c = Vertex3D.getCached(Objects.requireNonNull(c, "c == null"));
		this.surfaceNormal = Vector3D.getCached(Vector3D.normalNormalized(new Point3D(a.getPosition()), new Point3D(b.getPosition()), new Point3D(c.getPosition())));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link BoundingVolume3D} instance that contains this {@code Triangle3D} instance.
	 * 
	 * @return a {@code BoundingVolume3D} instance that contains this {@code Triangle3D} instance
	 */
//	TODO: Add Unit Tests!
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
	 * If {@code sample} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param sample a {@link Point2D} instance with a sample point
	 * @return an optional {@code SurfaceSample3D} with the surface sample
	 * @throws NullPointerException thrown if, and only if, {@code sample} is {@code null}
	 */
//	TODO: Add Unit Tests!
	@Override
	public Optional<SurfaceSample3D> sample(final Point2D sample) {
		Objects.requireNonNull(sample, "sample == null");
		
		final Point3D barycentricCoordinates = SampleGeneratorD.sampleTriangleUniformDistribution(sample.getU(), sample.getV());
		
		final Point4D positionA = this.a.getPosition();
		final Point4D positionB = this.b.getPosition();
		final Point4D positionC = this.c.getPosition();
		
		final Vector3D normalA = this.a.getNormal();
		final Vector3D normalB = this.b.getNormal();
		final Vector3D normalC = this.c.getNormal();
		
		final double x = positionA.getX() * barycentricCoordinates.getX() + positionB.getX() * barycentricCoordinates.getY() + positionC.getX() * barycentricCoordinates.getZ();
		final double y = positionA.getY() * barycentricCoordinates.getX() + positionB.getY() * barycentricCoordinates.getY() + positionC.getY() * barycentricCoordinates.getZ();
		final double z = positionA.getZ() * barycentricCoordinates.getX() + positionB.getZ() * barycentricCoordinates.getY() + positionC.getZ() * barycentricCoordinates.getZ();
		
		final Point3D point = new Point3D(x, y, z);
		
		final Vector3D surfaceNormal = Vector3D.normalNormalized(normalA, normalB, normalC, barycentricCoordinates);
		
		final double pointErrorX = (abs(positionA.getX() * barycentricCoordinates.getX()) + abs(positionB.getX() * barycentricCoordinates.getY()) + abs(positionC.getX() * barycentricCoordinates.getZ())) * gamma(6);
		final double pointErrorY = (abs(positionA.getY() * barycentricCoordinates.getX()) + abs(positionB.getY() * barycentricCoordinates.getY()) + abs(positionC.getY() * barycentricCoordinates.getZ())) * gamma(6);
		final double pointErrorZ = (abs(positionA.getZ() * barycentricCoordinates.getX()) + abs(positionB.getZ() * barycentricCoordinates.getY()) + abs(positionC.getZ() * barycentricCoordinates.getZ())) * gamma(6);
		
		final Vector3D pointError = new Vector3D(pointErrorX, pointErrorY, pointErrorZ);
		
		final double probabilityDensityFunctionValue = 1.0D / getSurfaceArea();
		
		return Optional.of(new SurfaceSample3D(point, pointError, surfaceNormal, probabilityDensityFunctionValue));
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
//	TODO: Add Unit Tests!
	@Override
	public Optional<SurfaceIntersection3D> intersection(final Ray3D ray, final double tMinimum, final double tMaximum) {
		final double t = intersectionT(ray, tMinimum, tMaximum);
		
		if(isNaN(t)) {
			return SurfaceIntersection3D.EMPTY;
		}
		
		return Optional.of(doCreateSurfaceIntersection(ray, t));
	}
	
	/**
	 * Returns a {@code String} with the name of this {@code Triangle3D} instance.
	 * 
	 * @return a {@code String} with the name of this {@code Triangle3D} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public String getName() {
		return NAME;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code Triangle3D} instance.
	 * 
	 * @return a {@code String} representation of this {@code Triangle3D} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public String toString() {
		return String.format("new Triangle3D(%s, %s, %s)", this.a, this.b, this.c);
	}
	
	/**
	 * Returns the bitangent of this {@code Triangle3D} instance.
	 * 
	 * @return the bitangent of this {@code Triangle3D} instance
	 */
//	TODO: Add Unit Tests!
	public Vector3D calculateBitangent() {
		final Point2D textureCoordinatesA = this.a.getTextureCoordinates();
		final Point2D textureCoordinatesB = this.b.getTextureCoordinates();
		final Point2D textureCoordinatesC = this.c.getTextureCoordinates();
		
		final Point4D positionA = this.a.getPosition();
		final Point4D positionB = this.b.getPosition();
		final Point4D positionC = this.c.getPosition();
		
		final Vector3D edgeAB = Vector3D.direction(positionA, positionB);
		final Vector3D edgeAC = Vector3D.direction(positionA, positionC);
		
		final double deltaABU = textureCoordinatesB.getU() - textureCoordinatesA.getU();
		final double deltaABV = textureCoordinatesB.getV() - textureCoordinatesA.getV();
		final double deltaACU = textureCoordinatesC.getU() - textureCoordinatesA.getU();
		final double deltaACV = textureCoordinatesC.getV() - textureCoordinatesA.getV();
		
		final double dividend = (deltaABU * deltaACV - deltaACU * deltaABV);
		final double fraction = dividend < -0.0D || dividend > +0.0D ? 1.0D / dividend : 0.0D;
		
		final double x = fraction * (-deltaACU * edgeAB.getX() + deltaABU * edgeAC.getX());
		final double y = fraction * (-deltaACU * edgeAB.getY() + deltaABU * edgeAC.getY());
		final double z = fraction * (-deltaACU * edgeAB.getZ() + deltaABU * edgeAC.getZ());
		
		return Vector3D.normalize(new Vector3D(x, y, z));
	}
	
	/**
	 * Returns the tangent of this {@code Triangle3D} instance.
	 * 
	 * @return the tangent of this {@code Triangle3D} instance
	 */
//	TODO: Add Unit Tests!
	public Vector3D calculateTangent() {
		final Point2D textureCoordinatesA = this.a.getTextureCoordinates();
		final Point2D textureCoordinatesB = this.b.getTextureCoordinates();
		final Point2D textureCoordinatesC = this.c.getTextureCoordinates();
		
		final Point4D positionA = this.a.getPosition();
		final Point4D positionB = this.b.getPosition();
		final Point4D positionC = this.c.getPosition();
		
		final Vector3D edgeAB = Vector3D.direction(positionA, positionB);
		final Vector3D edgeAC = Vector3D.direction(positionA, positionC);
		
		final double deltaABU = textureCoordinatesB.getU() - textureCoordinatesA.getU();
		final double deltaABV = textureCoordinatesB.getV() - textureCoordinatesA.getV();
		final double deltaACU = textureCoordinatesC.getU() - textureCoordinatesA.getU();
		final double deltaACV = textureCoordinatesC.getV() - textureCoordinatesA.getV();
		
		final double dividend = (deltaABU * deltaACV - deltaACU * deltaABV);
		final double fraction = dividend < -0.0D || dividend > +0.0D ? 1.0D / dividend : 0.0D;
		
		final double x = fraction * (deltaACV * edgeAB.getX() - deltaABV * edgeAC.getX());
		final double y = fraction * (deltaACV * edgeAB.getY() - deltaABV * edgeAC.getY());
		final double z = fraction * (deltaACV * edgeAB.getZ() - deltaABV * edgeAC.getZ());
		
		return Vector3D.normalize(new Vector3D(x, y, z));
	}
	
	/**
	 * Returns the surface normal associated with this {@code Triangle3D} instance.
	 * 
	 * @return the surface normal associated with this {@code Triangle3D} instance
	 */
//	TODO: Add Unit Tests!
	public Vector3D getSurfaceNormal() {
		return this.surfaceNormal;
	}
	
	/**
	 * Returns the vertex denoted by {@code A} and is associated with this {@code Triangle3D} instance.
	 * 
	 * @return the vertex denoted by {@code A} and is associated with this {@code Triangle3D} instance
	 */
//	TODO: Add Unit Tests!
	public Vertex3D getA() {
		return this.a;
	}
	
	/**
	 * Returns the vertex denoted by {@code B} and is associated with this {@code Triangle3D} instance.
	 * 
	 * @return the vertex denoted by {@code B} and is associated with this {@code Triangle3D} instance
	 */
//	TODO: Add Unit Tests!
	public Vertex3D getB() {
		return this.b;
	}
	
	/**
	 * Returns the vertex denoted by {@code C} and is associated with this {@code Triangle3D} instance.
	 * 
	 * @return the vertex denoted by {@code C} and is associated with this {@code Triangle3D} instance
	 */
//	TODO: Add Unit Tests!
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
	 * Returns {@code true} if, and only if, {@code point} is contained in this {@code Triangle3D} instance, {@code false} otherwise.
	 * <p>
	 * If {@code point} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param point a {@link Point3D} instance
	 * @return {@code true} if, and only if, {@code point} is contained in this {@code Triangle3D} instance, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, {@code point} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public boolean contains(final Point3D point) {
		final Point3D a = new Point3D(this.a.getPosition());
		final Point3D b = new Point3D(this.b.getPosition());
		final Point3D c = new Point3D(this.c.getPosition());
		final Point3D p = Objects.requireNonNull(point, "point == null");
		
		if(Point3D.coplanar(a, b, c, point)) {
			final Vector3D surfaceNormal = this.surfaceNormal;
			
			final Vector3D edgeAB = Vector3D.direction(a, b);
			final Vector3D edgeBC = Vector3D.direction(b, c);
			final Vector3D edgeCA = Vector3D.direction(c, a);
			
			final Vector3D edgeAP = Vector3D.direction(a, p);
			final Vector3D edgeBP = Vector3D.direction(b, p);
			final Vector3D edgeCP = Vector3D.direction(c, p);
			
			final boolean isInsideA = Vector3D.tripleProduct(surfaceNormal, edgeAB, edgeAP) > 0.0D;
			final boolean isInsideB = Vector3D.tripleProduct(surfaceNormal, edgeBC, edgeBP) > 0.0D;
			final boolean isInsideC = Vector3D.tripleProduct(surfaceNormal, edgeCA, edgeCP) > 0.0D;
			final boolean isInside = isInsideA && isInsideB && isInsideC;
			
			return isInside;
		}
		
		return false;
	}
	
	/**
	 * Compares {@code object} to this {@code Triangle3D} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code Triangle3D}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code Triangle3D} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code Triangle3D}, and their respective values are equal, {@code false} otherwise
	 */
//	TODO: Add Unit Tests!
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
	 * Returns the surface area of this {@code Triangle3D} instance.
	 * 
	 * @return the surface area of this {@code Triangle3D} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public double getSurfaceArea() {
		final Point4D a = this.a.getPosition();
		final Point4D b = this.b.getPosition();
		final Point4D c = this.c.getPosition();
		
		final Vector3D edgeAB = Vector3D.direction(a, b);
		final Vector3D edgeAC = Vector3D.direction(a, c);
		final Vector3D edgeABCrossEdgeAC = Vector3D.crossProduct(edgeAB, edgeAC);
		
		return edgeABCrossEdgeAC.length() * 0.5D;
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
//	TODO: Add Unit Tests!
	@Override
	public double intersectionT(final Ray3D ray, final double tMinimum, final double tMaximum) {
		final Point4D a = this.a.getPosition();
		final Point4D b = this.b.getPosition();
		final Point4D c = this.c.getPosition();
		
		final Vector3D edgeAB = Vector3D.direction(a, b);
		final Vector3D edgeCA = Vector3D.direction(c, a);
		final Vector3D direction0 = ray.getDirection();
		final Vector3D direction1 = Vector3D.crossProduct(edgeAB, edgeCA);
		
		final double determinant = Vector3D.dotProduct(direction0, direction1);
		final double determinantReciprocal = 1.0D / determinant;
		
		final Point3D origin = ray.getOrigin();
		
		final Vector3D direction2 = Vector3D.direction(origin, new Point3D(a));
		
		final double t = Vector3D.dotProduct(direction1, direction2) * determinantReciprocal;
		
		if(t <= tMinimum || t >= tMaximum) {
			return Double.NaN;
		}
		
		final Vector3D direction3 = Vector3D.crossProduct(direction2, direction0);
		
		final double uScaled = Vector3D.dotProduct(direction3, edgeCA);
		final double u = uScaled * determinantReciprocal;
		
		if(u < 0.0D) {
			return Double.NaN;
		}
		
		final double vScaled = Vector3D.dotProduct(direction3, edgeAB);
		final double v = vScaled * determinantReciprocal;
		
		if(v < 0.0D) {
			return Double.NaN;
		}
		
		if((uScaled + vScaled) * determinant > determinant * determinant) {
			return Double.NaN;
		}
		
		return t;
	}
	
	/**
	 * Returns an {@code int} with the ID of this {@code Triangle3D} instance.
	 * 
	 * @return an {@code int} with the ID of this {@code Triangle3D} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public int getID() {
		return ID;
	}
	
	/**
	 * Returns a hash code for this {@code Triangle3D} instance.
	 * 
	 * @return a hash code for this {@code Triangle3D} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public int hashCode() {
		return Objects.hash(this.surfaceNormal, this.a, this.b, this.c);
	}
	
	/**
	 * Writes this {@code Triangle3D} instance to {@code dataOutput}.
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
	 * A {@code Vertex3D} denotes a vertex of a {@link Triangle3D} instance.
	 * <p>
	 * This class is immutable and therefore thread-safe.
	 * 
	 * @since 1.0.0
	 * @author J&#246;rgen Lundgren
	 */
	public static final class Vertex3D implements Node {
		private static final Map<Vertex3D, Vertex3D> CACHE = new HashMap<>();
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		private final Point2D textureCoordinates;
		private final Point4D position;
		private final Vector3D normal;
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		/**
		 * Constructs a new {@code Vertex3D} instance.
		 * <p>
		 * If either {@code textureCoordinates}, {@code position} or {@code normal} are {@code null}, a {@code NullPointerException} will be thrown.
		 * 
		 * @param textureCoordinates the texture coordinates associated with this {@code Vertex3D} instance
		 * @param position the position associated with this {@code Vertex3D} instance
		 * @param normal the normal associated with this {@code Vertex3D} instance
		 * @throws NullPointerException thrown if, and only if, either {@code textureCoordinates}, {@code position} or {@code normal} are {@code null}
		 */
//		TODO: Add Unit Tests!
		public Vertex3D(final Point2D textureCoordinates, final Point4D position, final Vector3D normal) {
			this.textureCoordinates = Objects.requireNonNull(textureCoordinates, "textureCoordinates == null");
			this.position = Point4D.getCached(Objects.requireNonNull(position, "position == null"));
			this.normal = Vector3D.getCached(Objects.requireNonNull(normal, "normal == null"));
		}
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		/**
		 * Returns the texture coordinates associated with this {@code Vertex3D} instance.
		 * 
		 * @return the texture coordinates associated with this {@code Vertex3D} instance
		 */
//		TODO: Add Unit Tests!
		public Point2D getTextureCoordinates() {
			return this.textureCoordinates;
		}
		
		/**
		 * Returns the position associated with this {@code Vertex3D} instance.
		 * 
		 * @return the position associated with this {@code Vertex3D} instance
		 */
//		TODO: Add Unit Tests!
		public Point4D getPosition() {
			return this.position;
		}
		
		/**
		 * Returns a {@code String} representation of this {@code Vertex3D} instance.
		 * 
		 * @return a {@code String} representation of this {@code Vertex3D} instance
		 */
//		TODO: Add Unit Tests!
		@Override
		public String toString() {
			return String.format("new Vertex3D(%s, %s, %s)", this.textureCoordinates, this.position, this.normal);
		}
		
		/**
		 * Returns the normal associated with this {@code Vertex3D} instance.
		 * 
		 * @return the normal associated with this {@code Vertex3D} instance
		 */
//		TODO: Add Unit Tests!
		public Vector3D getNormal() {
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
		 * Compares {@code object} to this {@code Vertex3D} instance for equality.
		 * <p>
		 * Returns {@code true} if, and only if, {@code object} is an instance of {@code Vertex3D}, and their respective values are equal, {@code false} otherwise.
		 * 
		 * @param object the {@code Object} to compare to this {@code Vertex3D} instance for equality
		 * @return {@code true} if, and only if, {@code object} is an instance of {@code Vertex3D}, and their respective values are equal, {@code false} otherwise
		 */
//		TODO: Add Unit Tests!
		@Override
		public boolean equals(final Object object) {
			if(object == this) {
				return true;
			} else if(!(object instanceof Vertex3D)) {
				return false;
			} else if(!Objects.equals(this.textureCoordinates, Vertex3D.class.cast(object).textureCoordinates)) {
				return false;
			} else if(!Objects.equals(this.position, Vertex3D.class.cast(object).position)) {
				return false;
			} else if(!Objects.equals(this.normal, Vertex3D.class.cast(object).normal)) {
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
//		TODO: Add Unit Tests!
		@Override
		public int hashCode() {
			return Objects.hash(this.textureCoordinates, this.position, this.normal);
		}
		
		/**
		 * Writes this {@code Vertex3D} instance to {@code dataOutput}.
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
		 * @param vertex a {@code Vertex3D} instance
		 * @return a cached version of {@code vertex}
		 * @throws NullPointerException thrown if, and only if, {@code vertex} is {@code null}
		 */
//		TODO: Add Unit Tests!
		public static Vertex3D getCached(final Vertex3D vertex) {
			return CACHE.computeIfAbsent(Objects.requireNonNull(vertex, "vertex == null"), key -> vertex);
		}
		
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
//		TODO: Add Unit Tests!
		public static Vertex3D lerp(final Vertex3D a, final Vertex3D b, final double t) {
			final Point2D textureCoordinates = Point2D.lerp(a.textureCoordinates, b.textureCoordinates, t);
			
			final Point4D position = Point4D.lerp(a.position, b.position, t);
			
			final Vector3D normal = Vector3D.normalize(Vector3D.lerp(a.normal, b.normal, t));
			
			return new Vertex3D(textureCoordinates, position, normal);
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
//		TODO: Add Unit Tests!
		public static Vertex3D transform(final Vertex3D vertex, final Matrix44D matrix, final Matrix44D matrixInverse) {
			final Point2D textureCoordinates = vertex.textureCoordinates;
			
			final Point4D position = Point4D.transform(matrix, vertex.position);
			
			final Vector3D normal = Vector3D.normalize(Vector3D.transformTranspose(matrixInverse, vertex.normal));
			
			return new Vertex3D(textureCoordinates, position, normal);
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
//		TODO: Add Unit Tests!
		public static Vertex3D transformAndDivide(final Vertex3D vertex, final Matrix44D matrix, final Matrix44D matrixInverse) {
			final Point2D textureCoordinates = vertex.textureCoordinates;
			
			final Point4D position = Point4D.transformAndDivide(matrix, vertex.position);
			
			final Vector3D normal = Vector3D.normalize(Vector3D.transformTranspose(matrixInverse, vertex.normal));
			
			return new Vertex3D(textureCoordinates, position, normal);
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
	
	private OrthonormalBasis33D doCreateOrthonormalBasisG() {
		return new OrthonormalBasis33D(this.surfaceNormal);
	}
	
	private OrthonormalBasis33D doCreateOrthonormalBasisS(final Point3D barycentricCoordinates) {
		final Point2D textureCoordinatesA = this.a.getTextureCoordinates();
		final Point2D textureCoordinatesB = this.b.getTextureCoordinates();
		final Point2D textureCoordinatesC = this.c.getTextureCoordinates();
		
		final Vector2D textureCoordinatesCA = Vector2D.direction(textureCoordinatesC, textureCoordinatesA);
		final Vector2D textureCoordinatesCB = Vector2D.direction(textureCoordinatesC, textureCoordinatesB);
		
		final Point4D a = this.a.getPosition();
		final Point4D b = this.b.getPosition();
		final Point4D c = this.c.getPosition();
		
		final Vector3D edgeCA = Vector3D.direction(c, a);
		final Vector3D edgeCB = Vector3D.direction(c, b);
		
		final Vector3D normalA = this.a.getNormal();
		final Vector3D normalB = this.b.getNormal();
		final Vector3D normalC = this.c.getNormal();
		
		final Vector3D w = Vector3D.normalNormalized(normalA, normalB, normalC, barycentricCoordinates);
		
		final double determinant = Vector2D.crossProduct(textureCoordinatesCA, textureCoordinatesCB);
		
		if(isZero(determinant)) {
			return new OrthonormalBasis33D(w);
		}
		
		final double determinantReciprocal = 1.0D / determinant;
		
		final double x = (-textureCoordinatesCB.getU() * edgeCA.getX() + textureCoordinatesCA.getU() * edgeCB.getX()) * determinantReciprocal;
		final double y = (-textureCoordinatesCB.getU() * edgeCA.getY() + textureCoordinatesCA.getU() * edgeCB.getY()) * determinantReciprocal;
		final double z = (-textureCoordinatesCB.getU() * edgeCA.getZ() + textureCoordinatesCA.getU() * edgeCB.getZ()) * determinantReciprocal;
		
		final Vector3D v = new Vector3D(x, y, z);
		
		return new OrthonormalBasis33D(w, v);
	}
	
	private Point2D doCreateTextureCoordinates(final Point3D barycentricCoordinates) {
		final Point2D textureCoordinatesA = this.a.getTextureCoordinates();
		final Point2D textureCoordinatesB = this.b.getTextureCoordinates();
		final Point2D textureCoordinatesC = this.c.getTextureCoordinates();
		final Point2D textureCoordinates = Point2D.createTextureCoordinates(textureCoordinatesA, textureCoordinatesB, textureCoordinatesC, barycentricCoordinates);
		
		return textureCoordinates;
	}
	
	private Point3D doCreateBarycentricCoordinates(final Ray3D ray) {
		final Point4D a = this.a.getPosition();
		final Point4D b = this.b.getPosition();
		final Point4D c = this.c.getPosition();
		
		final Vector3D edgeAB = Vector3D.direction(a, b);
		final Vector3D edgeCA = Vector3D.direction(c, a);
		
		final Vector3D direction0 = ray.getDirection();
		final Vector3D direction1 = Vector3D.crossProduct(edgeAB, edgeCA);
		
		final double determinant = Vector3D.dotProduct(direction0, direction1);
		final double determinantReciprocal = 1.0D / determinant;
		
		final Point3D origin = ray.getOrigin();
		
		final Vector3D direction2 = Vector3D.direction(origin, new Point3D(a));
		final Vector3D direction3 = Vector3D.crossProduct(direction2, direction0);
		
		final double u = Vector3D.dotProduct(direction3, edgeCA) * determinantReciprocal;
		final double v = Vector3D.dotProduct(direction3, edgeAB) * determinantReciprocal;
		final double w = 1.0D - u - v;
		
		return new Point3D(w, u, v);
	}
	
	private SurfaceIntersection3D doCreateSurfaceIntersection(final Ray3D ray, final double t) {
		final Point3D barycentricCoordinates = doCreateBarycentricCoordinates(ray);
		final Point3D surfaceIntersectionPoint = doCreateSurfaceIntersectionPoint(ray, t);
		
		final Point2D textureCoordinates = doCreateTextureCoordinates(barycentricCoordinates);
		
		final OrthonormalBasis33D orthonormalBasisG = doCreateOrthonormalBasisG();
		final OrthonormalBasis33D orthonormalBasisS = doCreateOrthonormalBasisS(barycentricCoordinates);
		
		final Vector3D surfaceIntersectionPointError = doCreateSurfaceIntersectionPointError(barycentricCoordinates);
		
		return new SurfaceIntersection3D(orthonormalBasisG, orthonormalBasisS, textureCoordinates, surfaceIntersectionPoint, ray, this, surfaceIntersectionPointError, t);
	}
	
	private Vector3D doCreateSurfaceIntersectionPointError(final Point3D barycentricCoordinates) {
		final Point4D a = this.a.getPosition();
		final Point4D b = this.b.getPosition();
		final Point4D c = this.c.getPosition();
		
		final double xAbsSum = abs(barycentricCoordinates.getU() + a.getX()) + abs(barycentricCoordinates.getV() + b.getX()) + abs(barycentricCoordinates.getW() + c.getX());
		final double yAbsSum = abs(barycentricCoordinates.getU() + a.getY()) + abs(barycentricCoordinates.getV() + b.getY()) + abs(barycentricCoordinates.getW() + c.getY());
		final double zAbsSum = abs(barycentricCoordinates.getU() + a.getZ()) + abs(barycentricCoordinates.getV() + b.getZ()) + abs(barycentricCoordinates.getW() + c.getZ());
		
		return Vector3D.multiply(new Vector3D(xAbsSum, yAbsSum, zAbsSum), gamma(7));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static Point3D doCreateSurfaceIntersectionPoint(final Ray3D ray, final double t) {
		return Point3D.add(ray.getOrigin(), ray.getDirection(), t);
	}
}