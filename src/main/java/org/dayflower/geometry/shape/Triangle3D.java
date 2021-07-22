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
import static org.dayflower.utility.Doubles.max;

import java.io.DataOutput;
import java.io.IOException;
import java.io.UncheckedIOException;
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
	public static final String NAME = "Triangle";
	
	/**
	 * The ID of this {@code Triangle3D} class.
	 */
	public static final int ID = 15;
	
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
	@Override
	public Optional<SurfaceSample3D> sample(final Point2D sample) {
		Objects.requireNonNull(sample, "sample == null");
		
		final Point3D barycentricCoordinates = SampleGeneratorD.sampleTriangleUniformDistribution(sample.getU(), sample.getV());
		
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
	@Override
	public Optional<SurfaceIntersection3D> intersection(final Ray3D ray, final double tMinimum, final double tMaximum) {
//		return doIntersectionOld(ray, tMinimum, tMaximum);
//		return doIntersectionPBRT(ray, tMinimum, tMaximum);
		return doIntersectionSunflow(ray, tMinimum, tMaximum);
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
//		return doIntersectionTOld(ray, tMinimum, tMaximum);
//		return doIntersectionTPBRT(ray, tMinimum, tMaximum);
		return doIntersectionTSunflow(ray, tMinimum, tMaximum);
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
		public void write(final DataOutput dataOutput) {
			this.textureCoordinates.write(dataOutput);
			this.position.write(dataOutput);
			this.orthonormalBasis.write(dataOutput);
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
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@SuppressWarnings("unused")
	private Optional<SurfaceIntersection3D> doIntersectionOld(final Ray3D ray, final double tMinimum, final double tMaximum) {
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
		
		final Vector3D gW = this.surfaceNormal;
		final Vector3D sW = Vector3D.normalNormalized(aOrthonormalBasis.getW(), bOrthonormalBasis.getW(), cOrthonormalBasis.getW(), barycentricCoordinates);
		
		final Vector3D gV = Vector3D.directionNormalized(a, b);
		final Vector3D sV = Vector3D.normalNormalized(aOrthonormalBasis.getV(), bOrthonormalBasis.getV(), cOrthonormalBasis.getV(), barycentricCoordinates);
		
//		final Vector3D gU = Vector3D.directionNormalized(a, c);
//		final Vector3D sU = Vector3D.normalNormalized(aOrthonormalBasis.getU(), bOrthonormalBasis.getU(), cOrthonormalBasis.getU(), barycentricCoordinates);
		
		final OrthonormalBasis33D orthonormalBasisG = new OrthonormalBasis33D(gW, gV);
		final OrthonormalBasis33D orthonormalBasisS = new OrthonormalBasis33D(sW, sV);
//		final OrthonormalBasis33D orthonormalBasisG = new OrthonormalBasis33D(gW, gV, gU);
//		final OrthonormalBasis33D orthonormalBasisS = new OrthonormalBasis33D(sW, sV, sU);
		
		final double xAbsSum = abs(barycentricCoordinates.getU() + a.getX()) + abs(barycentricCoordinates.getV() + b.getX()) + abs(barycentricCoordinates.getW() + c.getX());
		final double yAbsSum = abs(barycentricCoordinates.getU() + a.getY()) + abs(barycentricCoordinates.getV() + b.getY()) + abs(barycentricCoordinates.getW() + c.getY());
		final double zAbsSum = abs(barycentricCoordinates.getU() + a.getZ()) + abs(barycentricCoordinates.getV() + b.getZ()) + abs(barycentricCoordinates.getW() + c.getZ());
		
		final Vector3D surfaceIntersectionPointError = Vector3D.multiply(new Vector3D(xAbsSum, yAbsSum, zAbsSum), gamma(7));
		
		return Optional.of(new SurfaceIntersection3D(orthonormalBasisG, orthonormalBasisS, textureCoordinates, surfaceIntersectionPoint, ray, this, surfaceIntersectionPointError, t));
	}
	
	@SuppressWarnings("unused")
	private Optional<SurfaceIntersection3D> doIntersectionPBRT(final Ray3D ray, final double tMinimum, final double tMaximum) {
		final Point3D p0 = new Point3D(this.a.getPosition());
		final Point3D p1 = new Point3D(this.b.getPosition());
		final Point3D p2 = new Point3D(this.c.getPosition());
		
		final int kZ = doMaxDimension(Vector3D.absolute(ray.getDirection()));
		final int kX = kZ == 2 ? 0 : kZ + 1;
		final int kY = kX == 2 ? 0 : kX + 1;
		
		final Vector3D d = doPermute(ray.getDirection(), kX, kY, kZ);
		
		final double shearX = -d.getX() / d.getZ();
		final double shearY = -d.getY() / d.getZ();
		final double shearZ = 1.0D / d.getZ();
		
		final Point3D p0T = doShear(doPermute(Point3D.subtract(p0, new Vector3D(ray.getOrigin())), kX, kY, kZ), shearX, shearY, shearZ);
		final Point3D p1T = doShear(doPermute(Point3D.subtract(p1, new Vector3D(ray.getOrigin())), kX, kY, kZ), shearX, shearY, shearZ);
		final Point3D p2T = doShear(doPermute(Point3D.subtract(p2, new Vector3D(ray.getOrigin())), kX, kY, kZ), shearX, shearY, shearZ);
		
		double e0 = p1T.getX() * p2T.getY() - p1T.getY() * p2T.getX();
		double e1 = p2T.getX() * p0T.getY() - p2T.getY() * p0T.getX();
		double e2 = p0T.getX() * p1T.getY() - p0T.getY() * p1T.getX();
		
		if(isZero(e0) || isZero(e1) || isZero(e2)) {
			final double p2TXP1TY = p2T.getX() * p1T.getY();
			final double p2TYP1TX = p2T.getY() * p1T.getX();
			final double p0TXP2TY = p0T.getX() * p2T.getY();
			final double p0TYP2TX = p0T.getY() * p2T.getX();
			final double p1TXP0TY = p1T.getX() * p0T.getY();
			final double p1TYP0TX = p1T.getY() * p0T.getX();
			
			e0 = p2TYP1TX - p2TXP1TY;
			e1 = p0TYP2TX - p0TXP2TY;
			e2 = p1TYP0TX - p1TXP0TY;
		}
		
		if((e0 < 0.0D || e1 < 0.0D || e2 < 0.0D) && (e0 > 0.0D || e1 > 0.0D || e2 > 0.0D)) {
			return SurfaceIntersection3D.EMPTY;
		}
		
		final double det = e0 + e1 + e2;
		
		if(isZero(det)) {
			return SurfaceIntersection3D.EMPTY;
		}
		
		final double tScaled = e0 * p0T.getZ() + e1 * p1T.getZ() + e2 * p2T.getZ();
		
		if(det < 0.0D && (tScaled >= 0.0D || tScaled < tMaximum * det)) {
			return SurfaceIntersection3D.EMPTY;
		} else if(det > 0.0D && (tScaled <= 0.0D || tScaled > tMaximum * det)) {
			return SurfaceIntersection3D.EMPTY;
		}
		
		final double invDet = 1.0D / det;
		
		final double b0 = e0 * invDet;
		final double b1 = e1 * invDet;
		final double b2 = e2 * invDet;
		
		final double t = tScaled * invDet;
		
		final double maxE = max(abs(e0), abs(e1), abs(e2));
		
		final double maxXT = max(abs(p0T.getX()), abs(p1T.getX()), abs(p2T.getX()));
		final double maxYT = max(abs(p0T.getY()), abs(p1T.getY()), abs(p2T.getY()));
		final double maxZT = max(abs(p0T.getZ()), abs(p1T.getZ()), abs(p2T.getZ()));
		
		final double deltaX = gamma(5) * (maxXT + maxZT);
		final double deltaY = gamma(5) * (maxYT + maxZT);
		final double deltaZ = gamma(3) * maxZT;
		final double deltaE = 2.0D * (gamma(2) * maxXT * maxYT + deltaY * maxXT + deltaX * maxYT);
		final double deltaT = 3.0D * (gamma(3) * maxE * maxZT + deltaE * maxZT + deltaZ * maxE) * abs(invDet);
		
		if(t <= deltaT) {
			return SurfaceIntersection3D.EMPTY;
		}
		
		final Point2D textureCoordinates0 = this.a.getTextureCoordinates();
		final Point2D textureCoordinates1 = this.b.getTextureCoordinates();
		final Point2D textureCoordinates2 = this.c.getTextureCoordinates();
		
		final Vector2D dUV02 = Vector2D.direction(textureCoordinates2, textureCoordinates0);
		final Vector2D dUV12 = Vector2D.direction(textureCoordinates2, textureCoordinates1);
		
		final Vector3D dP02 = Vector3D.direction(p2, p0);
		final Vector3D dP12 = Vector3D.direction(p2, p1);
		
		final double determinant = dUV02.getX() * dUV12.getY() - dUV02.getY() * dUV12.getX();
		
		final boolean hasDegenerateUVs = abs(determinant) < 1.0e-8D;
		
		Vector3D dPDU = new Vector3D();
		Vector3D dPDV = new Vector3D();
		
		if(!hasDegenerateUVs) {
			final double invDeterminant = 1.0D / determinant;
			
			dPDU = Vector3D.multiply(Vector3D.subtract(Vector3D.multiply(dP02, dUV12.getY()), Vector3D.multiply(dP12, dUV02.getY())), invDeterminant);
			dPDV = Vector3D.multiply(Vector3D.add(Vector3D.multiply(dP02, -dUV12.getX()), Vector3D.multiply(dP12, dUV02.getX())), invDeterminant);
		}
		
		if(hasDegenerateUVs || isZero(Vector3D.crossProduct(dPDU, dPDV).lengthSquared())) {
			final Vector3D ng = Vector3D.crossProduct(Vector3D.direction(p0, p2), Vector3D.direction(p0, p1));
			
			if(isZero(ng.lengthSquared())) {
				return SurfaceIntersection3D.EMPTY;
			}
			
			final OrthonormalBasis33D orthonormalBasis = OrthonormalBasis33D.coordinateSystem(Vector3D.normalize(ng));
			
			dPDU = orthonormalBasis.getU();
			dPDV = orthonormalBasis.getV();
		}
		
		final double xAbsSum = abs(b0 * p0.getX()) + abs(b1 * p1.getX()) + abs(b2 * p2.getX());
		final double yAbsSum = abs(b0 * p0.getY()) + abs(b1 * p1.getY()) + abs(b2 * p2.getY());
		final double zAbsSum = abs(b0 * p0.getZ()) + abs(b1 * p1.getZ()) + abs(b2 * p2.getZ());
		
		final Vector3D surfaceIntersectionPointError = Vector3D.multiply(new Vector3D(xAbsSum, yAbsSum, zAbsSum), gamma(7));
		
		final Point3D surfaceIntersectionPoint = new Point3D(b0 * p0.getX() + b1 * p1.getX() + b2 * p2.getX(), b0 * p0.getY() + b1 * p1.getY() + b2 * p2.getY(), b0 * p0.getZ() + b1 * p1.getZ() + b2 * p2.getZ());
		
		final Point2D textureCoordinates = new Point2D(b0 * textureCoordinates0.getU() + b1 + textureCoordinates1.getU() + b2 * textureCoordinates2.getU(), b0 * textureCoordinates0.getV() + b1 * textureCoordinates1.getV() + b2 * textureCoordinates2.getV());
		
		final Vector3D surfaceNormalG = Vector3D.normalize(Vector3D.crossProduct(dP02, dP12));
		
		final OrthonormalBasis33D orthonormalBasisG = new OrthonormalBasis33D(surfaceNormalG, dPDV, dPDU);
		final OrthonormalBasis33D orthonormalBasisS = orthonormalBasisG;
		
		return Optional.of(new SurfaceIntersection3D(orthonormalBasisG, orthonormalBasisS, textureCoordinates, surfaceIntersectionPoint, ray, this, surfaceIntersectionPointError, t));
	}
	
	private Optional<SurfaceIntersection3D> doIntersectionSunflow(final Ray3D ray, final double tMinimum, final double tMaximum) {
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
			return SurfaceIntersection3D.EMPTY;
		}
		
		final Vector3D direction3 = Vector3D.crossProduct(direction2, direction0);
		
		final double uScaled = Vector3D.dotProduct(direction3, edgeCA);
		final double u = uScaled * determinantReciprocal;
		
		if(u < 0.0D) {
			return SurfaceIntersection3D.EMPTY;
		}
		
		final double vScaled = Vector3D.dotProduct(direction3, edgeAB);
		final double v = vScaled * determinantReciprocal;
		
		if(v < 0.0D) {
			return SurfaceIntersection3D.EMPTY;
		}
		
		if((uScaled + vScaled) * determinant > determinant * determinant) {
			return SurfaceIntersection3D.EMPTY;
		}
		
		final double w = 1.0D - u - v;
		
		final Point3D barycentricCoordinates = new Point3D(w, u, v);
		final Point3D surfaceIntersectionPoint = Point3D.add(origin, direction0, t);
		
		final Point2D textureCoordinatesA = this.a.getTextureCoordinates();
		final Point2D textureCoordinatesB = this.b.getTextureCoordinates();
		final Point2D textureCoordinatesC = this.c.getTextureCoordinates();
		final Point2D textureCoordinates = Point2D.createTextureCoordinates(textureCoordinatesA, textureCoordinatesB, textureCoordinatesC, barycentricCoordinates);
		
		final OrthonormalBasis33D orthonormalBasisA = this.a.getOrthonormalBasis();
		final OrthonormalBasis33D orthonormalBasisB = this.b.getOrthonormalBasis();
		final OrthonormalBasis33D orthonormalBasisC = this.c.getOrthonormalBasis();
		
		final Vector3D surfaceNormalG = this.surfaceNormal;
		final Vector3D surfaceNormalS = Vector3D.normalNormalized(orthonormalBasisA.getW(), orthonormalBasisB.getW(), orthonormalBasisC.getW(), barycentricCoordinates);
		
		final double dU1 = textureCoordinatesA.getU() - textureCoordinatesC.getU();
		final double dU2 = textureCoordinatesB.getU() - textureCoordinatesC.getU();
		final double dV1 = textureCoordinatesA.getV() - textureCoordinatesC.getV();
		final double dV2 = textureCoordinatesB.getV() - textureCoordinatesC.getV();
		
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
	
	@SuppressWarnings("unused")
	private double doIntersectionTOld(final Ray3D ray, final double tMinimum, final double tMaximum) {
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
	
	@SuppressWarnings("unused")
	private double doIntersectionTPBRT(final Ray3D ray, final double tMinimum, final double tMaximum) {
		final Point3D p0 = new Point3D(this.a.getPosition());
		final Point3D p1 = new Point3D(this.b.getPosition());
		final Point3D p2 = new Point3D(this.c.getPosition());
		
		final int kZ = doMaxDimension(Vector3D.absolute(ray.getDirection()));
		final int kX = kZ == 2 ? 0 : kZ + 1;
		final int kY = kX == 2 ? 0 : kX + 1;
		
		final Vector3D d = doPermute(ray.getDirection(), kX, kY, kZ);
		
		final double shearX = -d.getX() / d.getZ();
		final double shearY = -d.getY() / d.getZ();
		final double shearZ = 1.0D / d.getZ();
		
		final Point3D p0T = doShear(doPermute(Point3D.subtract(p0, new Vector3D(ray.getOrigin())), kX, kY, kZ), shearX, shearY, shearZ);
		final Point3D p1T = doShear(doPermute(Point3D.subtract(p1, new Vector3D(ray.getOrigin())), kX, kY, kZ), shearX, shearY, shearZ);
		final Point3D p2T = doShear(doPermute(Point3D.subtract(p2, new Vector3D(ray.getOrigin())), kX, kY, kZ), shearX, shearY, shearZ);
		
		double e0 = p1T.getX() * p2T.getY() - p1T.getY() * p2T.getX();
		double e1 = p2T.getX() * p0T.getY() - p2T.getY() * p0T.getX();
		double e2 = p0T.getX() * p1T.getY() - p0T.getY() * p1T.getX();
		
		if(isZero(e0) || isZero(e1) || isZero(e2)) {
			final double p2TXP1TY = p2T.getX() * p1T.getY();
			final double p2TYP1TX = p2T.getY() * p1T.getX();
			final double p0TXP2TY = p0T.getX() * p2T.getY();
			final double p0TYP2TX = p0T.getY() * p2T.getX();
			final double p1TXP0TY = p1T.getX() * p0T.getY();
			final double p1TYP0TX = p1T.getY() * p0T.getX();
			
			e0 = p2TYP1TX - p2TXP1TY;
			e1 = p0TYP2TX - p0TXP2TY;
			e2 = p1TYP0TX - p1TXP0TY;
		}
		
		if((e0 < 0.0D || e1 < 0.0D || e2 < 0.0D) && (e0 > 0.0D || e1 > 0.0D || e2 > 0.0D)) {
			return Double.NaN;
		}
		
		final double det = e0 + e1 + e2;
		
		if(isZero(det)) {
			return Double.NaN;
		}
		
		final double tScaled = e0 * p0T.getZ() + e1 * p1T.getZ() + e2 * p2T.getZ();
		
		if(det < 0.0D && (tScaled >= 0.0D || tScaled < tMaximum * det)) {
			return Double.NaN;
		} else if(det > 0.0D && (tScaled <= 0.0D || tScaled > tMaximum * det)) {
			return Double.NaN;
		}
		
		final double invDet = 1.0D / det;
		
		final double t = tScaled * invDet;
		
		final double maxE = max(abs(e0), abs(e1), abs(e2));
		
		final double maxXT = max(abs(p0T.getX()), abs(p1T.getX()), abs(p2T.getX()));
		final double maxYT = max(abs(p0T.getY()), abs(p1T.getY()), abs(p2T.getY()));
		final double maxZT = max(abs(p0T.getZ()), abs(p1T.getZ()), abs(p2T.getZ()));
		
		final double deltaX = gamma(5) * (maxXT + maxZT);
		final double deltaY = gamma(5) * (maxYT + maxZT);
		final double deltaZ = gamma(3) * maxZT;
		final double deltaE = 2.0D * (gamma(2) * maxXT * maxYT + deltaY * maxXT + deltaX * maxYT);
		final double deltaT = 3.0D * (gamma(3) * maxE * maxZT + deltaE * maxZT + deltaZ * maxE) * abs(invDet);
		
		if(t <= deltaT) {
			return Double.NaN;
		}
		
		return t;
	}
	
	private double doIntersectionTSunflow(final Ray3D ray, final double tMinimum, final double tMaximum) {
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
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static Point3D doPermute(final Point3D point, final int x, final int y, final int z) {
		final double component1 = x == 0 ? point.getX() : x == 1 ? point.getY() : point.getZ();
		final double component2 = y == 0 ? point.getX() : y == 1 ? point.getY() : point.getZ();
		final double component3 = z == 0 ? point.getX() : z == 1 ? point.getY() : point.getZ();
		
		return new Point3D(component1, component2, component3);
	}
	
	private static Point3D doShear(final Point3D point, final double shearX, final double shearY, final double shearZ) {
		final double component1 = point.getX() + shearX * point.getZ();
		final double component2 = point.getY() + shearY * point.getZ();
		final double component3 = point.getZ() * shearZ;
		
		return new Point3D(component1, component2, component3);
	}
	
	private static Vector3D doPermute(final Vector3D vector, final int x, final int y, final int z) {
		final double component1 = x == 0 ? vector.getX() : x == 1 ? vector.getY() : vector.getZ();
		final double component2 = y == 0 ? vector.getX() : y == 1 ? vector.getY() : vector.getZ();
		final double component3 = z == 0 ? vector.getX() : z == 1 ? vector.getY() : vector.getZ();
		
		return new Vector3D(component1, component2, component3);
	}
	
	private static int doMaxDimension(final Vector3D vector) {
		return vector.getX() > vector.getY() ? vector.getX() > vector.getZ() ? 0 : 2 : vector.getY() > vector.getZ() ? 1 : 2;
	}
}