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

import static org.dayflower.util.Floats.abs;
import static org.dayflower.util.Floats.equal;
import static org.dayflower.util.Floats.isNaN;
import static org.dayflower.util.Floats.max;
import static org.dayflower.util.Floats.min;
import static org.dayflower.util.Floats.minOrNaN;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.dayflower.geometry.Triangle3F.Vertex3F;
import org.dayflower.util.Lists;

/**
 * A {@code TriangleMesh3F} denotes a 3-dimensional triangle mesh that uses the data type {@code float}.
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class TriangleMesh3F implements Shape3F {
	private final BoundingVolume3F boundingVolume;
	private final List<Triangle3F> triangles;
	private final Node node;
	private final String groupName;
	private final String materialName;
	private final String objectName;
	private final boolean isUsingAccelerationStructure;
	private final float surfaceArea;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code TriangleMesh3F} instance.
	 * <p>
	 * If either {@code triangles}, at least one of its elements, {@code groupName}, {@code materialName} or {@code objectName} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new TriangleMesh3F(triangles, groupName, materialName, objectName, true);
	 * }
	 * </pre>
	 * 
	 * @param triangles a {@code List} of {@link Triangle3F} instances
	 * @param groupName the group name of this {@code TriangleMesh3F} instance
	 * @param materialName the material name of this {@code TriangleMesh3F} instance
	 * @param objectName the object name of this {@code TriangleMesh3F} instance
	 * @throws NullPointerException thrown if, and only if, either {@code triangles}, at least one of its elements, {@code groupName}, {@code materialName} or {@code objectName} are {@code null}
	 */
	public TriangleMesh3F(final List<Triangle3F> triangles, final String groupName, final String materialName, final String objectName) {
		this(triangles, groupName, materialName, objectName, true);
	}
	
	/**
	 * Constructs a new {@code TriangleMesh3F} instance.
	 * <p>
	 * If either {@code triangles}, at least one of its elements, {@code groupName}, {@code materialName} or {@code objectName} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param triangles a {@code List} of {@link Triangle3F} instances
	 * @param groupName the group name of this {@code TriangleMesh3F} instance
	 * @param materialName the material name of this {@code TriangleMesh3F} instance
	 * @param objectName the object name of this {@code TriangleMesh3F} instance
	 * @param isUsingAccelerationStructure {@code true} if, and only if, an acceleration structure should be used, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, either {@code triangles}, at least one of its elements, {@code groupName}, {@code materialName} or {@code objectName} are {@code null}
	 */
	public TriangleMesh3F(final List<Triangle3F> triangles, final String groupName, final String materialName, final String objectName, final boolean isUsingAccelerationStructure) {
		this.triangles = new ArrayList<>(Lists.requireNonNullList(triangles, "triangles"));
		this.node = isUsingAccelerationStructure ? doCreateNode(this.triangles) : null;
		this.boundingVolume = isUsingAccelerationStructure ? this.node.getBoundingVolume() : doCreateBoundingVolume(this.triangles);
		this.groupName = Objects.requireNonNull(groupName, "groupName == null");
		this.materialName = Objects.requireNonNull(materialName, "materialName == null");
		this.objectName = Objects.requireNonNull(objectName, "objectName == null");
		this.isUsingAccelerationStructure = isUsingAccelerationStructure;
		this.surfaceArea = isUsingAccelerationStructure ? this.node.getSurfaceArea() : doCalculateSurfaceArea(this.triangles);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link BoundingVolume3F} instance that contains this {@code TriangleMesh3F} instance.
	 * 
	 * @return a {@code BoundingVolume3F} instance that contains this {@code TriangleMesh3F} instance
	 */
	@Override
	public BoundingVolume3F getBoundingVolume() {
		return this.boundingVolume;
	}
	
	/**
	 * Samples this {@code TriangleMesh3F} instance.
	 * <p>
	 * Returns an optional {@link SurfaceSample3F} with the surface sample.
	 * <p>
	 * If either {@code referencePoint} or {@code referenceSurfaceNormal} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Note: This method has not been implemented yet.
	 * 
	 * @param referencePoint the reference point on this {@code TriangleMesh3F} instance
	 * @param referenceSurfaceNormal the reference surface normal on this {@code TriangleMesh3F} instance
	 * @param u a random {@code float} with a uniform distribution between {@code 0.0F} and {@code 1.0F}
	 * @param v a random {@code float} with a uniform distribution between {@code 0.0F} and {@code 1.0F}
	 * @return an optional {@code SurfaceSample3F} with the surface sample
	 * @throws NullPointerException thrown if, and only if, either {@code referencePoint} or {@code referenceSurfaceNormal} are {@code null}
	 */
	@Override
	public Optional<SurfaceSample3F> sample(final Point3F referencePoint, final Vector3F referenceSurfaceNormal, final float u, final float v) {
		Objects.requireNonNull(referencePoint, "referencePoint == null");
		Objects.requireNonNull(referenceSurfaceNormal, "referenceSurfaceNormal == null");
		
		return SurfaceSample3F.EMPTY;//TODO: Implement!
	}
	
	/**
	 * Performs an intersection test between {@code ray} and this {@code TriangleMesh3F} instance.
	 * <p>
	 * Returns an {@code Optional} with an optional {@link SurfaceIntersection3F} instance that contains information about the intersection, if it was found.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3F} to perform an intersection test against this {@code TriangleMesh3F} instance
	 * @param tMinimum the minimum parametric distance
	 * @param tMaximum the maximum parametric distance
	 * @return an {@code Optional} with an optional {@code SurfaceIntersection3F} instance that contains information about the intersection, if it was found
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	@Override
	public Optional<SurfaceIntersection3F> intersection(final Ray3F ray, final float tMinimum, final float tMaximum) {
		if(this.isUsingAccelerationStructure) {
			return this.node.intersection(ray, new float[] {tMinimum, tMaximum});
		}
		
		final MutableSurfaceIntersection3F mutableSurfaceIntersection = new MutableSurfaceIntersection3F(ray, tMinimum, tMaximum);
		
		for(final Triangle3F triangle : this.triangles) {
			mutableSurfaceIntersection.intersection(triangle);
		}
		
		return mutableSurfaceIntersection.computeSurfaceIntersection();
	}
	
	/**
	 * Returns the group name of this {@code TriangleMesh3F} instance.
	 * 
	 * @return the group name of this {@code TriangleMesh3F} instance
	 */
	public String getGroupName() {
		return this.groupName;
	}
	
	/**
	 * Returns the material name of this {@code TriangleMesh3F} instance.
	 * 
	 * @return the material name of this {@code TriangleMesh3F} instance
	 */
	public String getMaterialName() {
		return this.materialName;
	}
	
	/**
	 * Returns the object name of this {@code TriangleMesh3F} instance.
	 * 
	 * @return the object name of this {@code TriangleMesh3F} instance
	 */
	public String getObjectName() {
		return this.objectName;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code TriangleMesh3F} instance.
	 * 
	 * @return a {@code String} representation of this {@code TriangleMesh3F} instance
	 */
	@Override
	public String toString() {
		return String.format("new TriangleMesh3F(..., \"%s\", \"%s\", \"%s\", %s)", this.groupName, this.materialName, this.objectName, Boolean.toString(this.isUsingAccelerationStructure));
	}
	
	/**
	 * Compares {@code object} to this {@code TriangleMesh3F} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code TriangleMesh3F}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code TriangleMesh3F} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code TriangleMesh3F}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof TriangleMesh3F)) {
			return false;
		} else if(!Objects.equals(this.boundingVolume, TriangleMesh3F.class.cast(object).boundingVolume)) {
			return false;
		} else if(!Objects.equals(this.triangles, TriangleMesh3F.class.cast(object).triangles)) {
			return false;
		} else if(!Objects.equals(this.node, TriangleMesh3F.class.cast(object).node)) {
			return false;
		} else if(!Objects.equals(this.groupName, TriangleMesh3F.class.cast(object).groupName)) {
			return false;
		} else if(!Objects.equals(this.materialName, TriangleMesh3F.class.cast(object).materialName)) {
			return false;
		} else if(!Objects.equals(this.objectName, TriangleMesh3F.class.cast(object).objectName)) {
			return false;
		} else if(this.isUsingAccelerationStructure != TriangleMesh3F.class.cast(object).isUsingAccelerationStructure) {
			return false;
		} else if(!equal(this.surfaceArea, TriangleMesh3F.class.cast(object).surfaceArea)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Performs an intersection test between {@code mutableSurfaceIntersection} and this {@code TriangleMesh3F} instance.
	 * <p>
	 * Returns {@code true} if, and only if, {@code mutableSurfaceIntersection} intersects this {@code TriangleMesh3F} instance, {@code false} otherwise.
	 * <p>
	 * If {@code mutableSurfaceIntersection} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param mutableSurfaceIntersection a {@link MutableSurfaceIntersection3F} instance
	 * @return {@code true} if, and only if, {@code mutableSurfaceIntersection} intersects this {@code TriangleMesh3F} instance, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, {@code mutableSurfaceIntersection} is {@code null}
	 */
	@Override
	public boolean intersection(final MutableSurfaceIntersection3F mutableSurfaceIntersection) {
		return this.isUsingAccelerationStructure ? this.node.intersection(mutableSurfaceIntersection) : mutableSurfaceIntersection.intersection(this);
	}
	
	/**
	 * Returns {@code true} if, and only if, {@code ray} intersects this {@code TriangleMesh3F} instance, {@code false} otherwise.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3F} to perform an intersection test against this {@code TriangleMesh3F} instance
	 * @param tMinimum the minimum parametric distance
	 * @param tMaximum the maximum parametric distance
	 * @return {@code true} if, and only if, {@code ray} intersects this {@code TriangleMesh3F} instance, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	@Override
	public boolean intersects(final Ray3F ray, final float tMinimum, final float tMaximum) {
		return this.isUsingAccelerationStructure ? this.node.intersects(ray, tMinimum, tMaximum) : !isNaN(intersectionT(ray, tMinimum, tMaximum));
	}
	
	/**
	 * Returns the probability density function (PDF) value for solid angle.
	 * <p>
	 * If either {@code referencePoint}, {@code referenceSurfaceNormal}, {@code point} or {@code surfaceNormal} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Note: This method has not been implemented yet.
	 * 
	 * @param referencePoint the reference point on this {@code TriangleMesh3F} instance
	 * @param referenceSurfaceNormal the reference surface normal on this {@code TriangleMesh3F} instance
	 * @param point the point on this {@code TriangleMesh3F} instance
	 * @param surfaceNormal the surface normal on this {@code TriangleMesh3F} instance
	 * @return the probability density function (PDF) value for solid angle
	 * @throws NullPointerException thrown if, and only if, either {@code referencePoint}, {@code referenceSurfaceNormal}, {@code point} or {@code surfaceNormal} are {@code null}
	 */
	@Override
	public float calculateProbabilityDensityFunctionValueForSolidAngle(final Point3F referencePoint, final Vector3F referenceSurfaceNormal, final Point3F point, final Vector3F surfaceNormal) {
		Objects.requireNonNull(referencePoint, "referencePoint == null");
		Objects.requireNonNull(referenceSurfaceNormal, "referenceSurfaceNormal == null");
		Objects.requireNonNull(point, "point == null");
		Objects.requireNonNull(surfaceNormal, "surfaceNormal == null");
		
		return 0.0F;//TODO: Implement!
	}
	
	/**
	 * Returns the probability density function (PDF) value for solid angle.
	 * <p>
	 * If either {@code referencePoint}, {@code referenceSurfaceNormal} or {@code direction} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Note: This method has not been implemented yet.
	 * 
	 * @param referencePoint the reference point on this {@code TriangleMesh3F} instance
	 * @param referenceSurfaceNormal the reference surface normal on this {@code TriangleMesh3F} instance
	 * @param direction the direction to this {@code TriangleMesh3F} instance
	 * @return the probability density function (PDF) value for solid angle
	 * @throws NullPointerException thrown if, and only if, either {@code referencePoint}, {@code referenceSurfaceNormal} or {@code direction} are {@code null}
	 */
	@Override
	public float calculateProbabilityDensityFunctionValueForSolidAngle(final Point3F referencePoint, final Vector3F referenceSurfaceNormal, final Vector3F direction) {
		Objects.requireNonNull(referencePoint, "referencePoint == null");
		Objects.requireNonNull(referenceSurfaceNormal, "referenceSurfaceNormal == null");
		Objects.requireNonNull(direction, "direction == null");
		
		return 0.0F;//TODO: Implement!
	}
	
	/**
	 * Returns the surface area of this {@code TriangleMesh3F} instance.
	 * 
	 * @return the surface area of this {@code TriangleMesh3F} instance
	 */
	@Override
	public float getSurfaceArea() {
		return this.surfaceArea;
	}
	
	/**
	 * Returns the surface area probability density function (PDF) value of this {@code TriangleMesh3F} instance.
	 * <p>
	 * Note: This method has not been implemented yet.
	 * 
	 * @return the surface area probability density function (PDF) value of this {@code TriangleMesh3F} instance
	 */
	@Override
	public float getSurfaceAreaProbabilityDensityFunctionValue() {
		return 0.0F;//TODO: Implement!
	}
	
	/**
	 * Returns the volume of this {@code TriangleMesh3F} instance.
	 * <p>
	 * This method returns {@code 0.0F}.
	 * 
	 * @return the volume of this {@code TriangleMesh3F} instance
	 */
	@Override
	public float getVolume() {
		return 0.0F;
	}
	
	/**
	 * Performs an intersection test between {@code ray} and this {@code TriangleMesh3F} instance.
	 * <p>
	 * Returns {@code t}, the parametric distance to the surface intersection point, or {@code Float.NaN} if no intersection exists.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3F} to perform an intersection test against this {@code TriangleMesh3F} instance
	 * @param tMinimum the minimum parametric distance
	 * @param tMaximum the maximum parametric distance
	 * @return {@code t}, the parametric distance to the surface intersection point, or {@code Float.NaN} if no intersection exists
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	@Override
	public float intersectionT(final Ray3F ray, final float tMinimum, final float tMaximum) {
		if(this.isUsingAccelerationStructure) {
			return this.node.intersectionT(ray, new float[] {tMinimum, tMaximum});
		}
		
		float t = Float.NaN;
		float tMax = tMaximum;
		float tMin = tMinimum;
		
		for(final Triangle3F triangle : this.triangles) {
			t = minOrNaN(t, triangle.intersectionT(ray, tMin, tMax));
			
			if(!isNaN(t)) {
				tMax = t;
			}
		}
		
		return t;
	}
	
	/**
	 * Returns a {@code float[]} representation of this {@code TriangleMesh3F} instance.
	 * <p>
	 * Note: This method has not been implemented yet.
	 * 
	 * @return a {@code float[]} representation of this {@code TriangleMesh3F} instance
	 */
	@Override
	public float[] toArray() {
		return new float[0];//TODO: Implement!
	}
	
	/**
	 * Returns a hash code for this {@code TriangleMesh3F} instance.
	 * 
	 * @return a hash code for this {@code TriangleMesh3F} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.boundingVolume, this.triangles, this.node, this.groupName, this.materialName, this.objectName, Boolean.valueOf(this.isUsingAccelerationStructure), Float.valueOf(this.surfaceArea));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Reads a Wavefront Object file into a {@code List} of {@code TriangleMesh3F} instances.
	 * <p>
	 * Returns a {@code List} of {@code TriangleMesh3F} instances.
	 * <p>
	 * If {@code file} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O-error occurs, an {@code UncheckedIOException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * TriangleMesh3F.readWavefrontObject(file, false);
	 * }
	 * </pre>
	 * 
	 * @param file a {@code File} instance
	 * @return a {@code List} of {@code TriangleMesh3F} instances
	 * @throws NullPointerException thrown if, and only if, {@code file} is {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O-error occurs
	 */
	public static List<TriangleMesh3F> readWavefrontObject(final File file) {
		return readWavefrontObject(file, false);
	}
	
	/**
	 * Reads a Wavefront Object file into a {@code List} of {@code TriangleMesh3F} instances.
	 * <p>
	 * Returns a {@code List} of {@code TriangleMesh3F} instances.
	 * <p>
	 * If {@code file} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O-error occurs, an {@code UncheckedIOException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * TriangleMesh3F.readWavefrontObject(file, isFlippingTextureCoordinateY, 1.0F);
	 * }
	 * </pre>
	 * 
	 * @param file a {@code File} instance
	 * @param isFlippingTextureCoordinateY {@code true} if, and only if, the Y-coordinate of the texture coordinates should be flipped, {@code false} otherwise
	 * @return a {@code List} of {@code TriangleMesh3F} instances
	 * @throws NullPointerException thrown if, and only if, {@code file} is {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O-error occurs
	 */
	public static List<TriangleMesh3F> readWavefrontObject(final File file, final boolean isFlippingTextureCoordinateY) {
		return readWavefrontObject(file, isFlippingTextureCoordinateY, 1.0F);
	}
	
	/**
	 * Reads a Wavefront Object file into a {@code List} of {@code TriangleMesh3F} instances.
	 * <p>
	 * Returns a {@code List} of {@code TriangleMesh3F} instances.
	 * <p>
	 * If {@code file} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O-error occurs, an {@code UncheckedIOException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * TriangleMesh3F.readWavefrontObject(file, isFlippingTextureCoordinateY, scale, true);
	 * }
	 * </pre>
	 * 
	 * @param file a {@code File} instance
	 * @param isFlippingTextureCoordinateY {@code true} if, and only if, the Y-coordinate of the texture coordinates should be flipped, {@code false} otherwise
	 * @param scale the scale to apply to all {@link Triangle3F} instances
	 * @return a {@code List} of {@code TriangleMesh3F} instances
	 * @throws NullPointerException thrown if, and only if, {@code file} is {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O-error occurs
	 */
	public static List<TriangleMesh3F> readWavefrontObject(final File file, final boolean isFlippingTextureCoordinateY, final float scale) {
		return readWavefrontObject(file, isFlippingTextureCoordinateY, scale, true);
	}
	
	/**
	 * Reads a Wavefront Object file into a {@code List} of {@code TriangleMesh3F} instances.
	 * <p>
	 * Returns a {@code List} of {@code TriangleMesh3F} instances.
	 * <p>
	 * If {@code file} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O-error occurs, an {@code UncheckedIOException} will be thrown.
	 * 
	 * @param file a {@code File} instance
	 * @param isFlippingTextureCoordinateY {@code true} if, and only if, the Y-coordinate of the texture coordinates should be flipped, {@code false} otherwise
	 * @param scale the scale to apply to all {@link Triangle3F} instances
	 * @param isUsingAccelerationStructure {@code true} if, and only if, an acceleration structure should be used, {@code false} otherwise
	 * @return a {@code List} of {@code TriangleMesh3F} instances
	 * @throws NullPointerException thrown if, and only if, {@code file} is {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O-error occurs
	 */
	public static List<TriangleMesh3F> readWavefrontObject(final File file, final boolean isFlippingTextureCoordinateY, final float scale, final boolean isUsingAccelerationStructure) {
		try {
			System.out.printf("Loading triangle meshes from file '%s'...%n", file.getName());
			
			final DefaultObjectModel defaultObjectModel = DefaultObjectModel.parseDefaultObjectModel(Objects.requireNonNull(file, "file == null"), isFlippingTextureCoordinateY);
			
			final IndexedObjectModel indexedObjectModel = defaultObjectModel.toIndexedObjectModel();
			
			final List<Integer> indices = indexedObjectModel.getIndices();
			final List<Point2F> textureCoordinates = indexedObjectModel.getTextureCoordinates();
			final List<Point3F> positions = indexedObjectModel.getPositions();
			final List<String> groupNames = indexedObjectModel.getGroupNames();
			final List<String> materialNames = indexedObjectModel.getMaterialNames();
			final List<String> objectNames = indexedObjectModel.getObjectNames();
			final List<Vector3F> normals = indexedObjectModel.getNormals();
			final List<Vector3F> tangents = indexedObjectModel.getTangents();
			final List<Triangle3F> triangles = new ArrayList<>();
			final List<TriangleMesh3F> triangleMeshes = new ArrayList<>();
			
			String previousGroupName = "";
			String previousMaterialName = "";
			String previousObjectName = "";
			
			final boolean isScaling = !equal(scale, 1.0F);
			
			final int maximumCount = Integer.MAX_VALUE;
			
			final Matrix44F matrix = isScaling ? Matrix44F.scale(scale) : null;
			
			if(matrix != null && !matrix.isInvertible()) {
				return new ArrayList<>();
			}
			
			final Matrix44F matrixInverse = isScaling ? Matrix44F.inverse(matrix) : null;
			
			for(int i = 0, j = 0; i < indices.size(); i += 3) {
				final int indexA = indices.get(i + 0).intValue();
				final int indexB = indices.get(i + 1).intValue();
				final int indexC = indices.get(i + 2).intValue();
				
				final String currentGroupName = groupNames.get(indexA);
				final String currentMaterialName = materialNames.get(indexA);
				final String currentObjectName = objectNames.get(indexA);
				
				if(!previousGroupName.equals(currentGroupName) || !previousMaterialName.equals(currentMaterialName)) {
					if(triangles.size() > 0) {
						System.out.printf(" - Creating triangle mesh with group name '%s', material name '%s' and object name '%s'.%n", previousGroupName, previousMaterialName, previousObjectName);
						
						triangleMeshes.add(new TriangleMesh3F(triangles, previousGroupName, previousMaterialName, previousObjectName, isUsingAccelerationStructure));
						triangles.clear();
						
						if(++j >= maximumCount) {
							break;
						}
					}
					
					if(!previousGroupName.equals(currentGroupName)) {
						previousGroupName = currentGroupName;
					}
					
					if(!previousMaterialName.equals(currentMaterialName)) {
						previousMaterialName = currentMaterialName;
					}
				}
				
				if(!previousObjectName.equals(currentObjectName)) {
					previousObjectName = currentObjectName;
				}
				
				final Point2F textureCoordinatesA = textureCoordinates.get(indexA);
				final Point2F textureCoordinatesB = textureCoordinates.get(indexB);
				final Point2F textureCoordinatesC = textureCoordinates.get(indexC);
				
				final Point3F positionA = isScaling ? Point3F.transformAndDivide(matrix, positions.get(indexA)) : positions.get(indexA);
				final Point3F positionB = isScaling ? Point3F.transformAndDivide(matrix, positions.get(indexB)) : positions.get(indexB);
				final Point3F positionC = isScaling ? Point3F.transformAndDivide(matrix, positions.get(indexC)) : positions.get(indexC);
				
				final Vector3F normalA = isScaling ? Vector3F.transformTranspose(matrixInverse, normals.get(indexA)) : normals.get(indexA);
				final Vector3F normalB = isScaling ? Vector3F.transformTranspose(matrixInverse, normals.get(indexB)) : normals.get(indexB);
				final Vector3F normalC = isScaling ? Vector3F.transformTranspose(matrixInverse, normals.get(indexC)) : normals.get(indexC);
				
				final Vector3F tangentA = isScaling ? Vector3F.transformTranspose(matrixInverse, tangents.get(indexA)) : tangents.get(indexA);
				final Vector3F tangentB = isScaling ? Vector3F.transformTranspose(matrixInverse, tangents.get(indexB)) : tangents.get(indexB);
				final Vector3F tangentC = isScaling ? Vector3F.transformTranspose(matrixInverse, tangents.get(indexC)) : tangents.get(indexC);
				
				final Vertex3F a = new Vertex3F(textureCoordinatesA, positionA, normalA, tangentA);
				final Vertex3F b = new Vertex3F(textureCoordinatesB, positionB, normalB, tangentB);
				final Vertex3F c = new Vertex3F(textureCoordinatesC, positionC, normalC, tangentC);
				
				final Triangle3F triangle = new Triangle3F(a, b, c);
				
				triangles.add(triangle);
			}
			
			if(triangles.size() > 0) {
				System.out.printf(" - Creating triangle mesh with group name '%s', material name '%s' and object name '%s'.%n", previousGroupName, previousMaterialName, previousObjectName);
				
				triangleMeshes.add(new TriangleMesh3F(triangles, previousGroupName, previousMaterialName, previousObjectName, isUsingAccelerationStructure));
				triangles.clear();
			}
			
			System.out.println(" - Done.");
			
			return triangleMeshes;
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	/**
	 * Reads a Wavefront Object file into a {@code List} of {@code TriangleMesh3F} instances.
	 * <p>
	 * Returns a {@code List} of {@code TriangleMesh3F} instances.
	 * <p>
	 * If {@code pathname} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O-error occurs, an {@code UncheckedIOException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * TriangleMesh3F.readWavefrontObject(pathname, false);
	 * }
	 * </pre>
	 * 
	 * @param pathname a {@code String} instance with the pathname to a file
	 * @return a {@code List} of {@code TriangleMesh3F} instances
	 * @throws NullPointerException thrown if, and only if, {@code pathname} is {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O-error occurs
	 */
	public static List<TriangleMesh3F> readWavefrontObject(final String pathname) {
		return readWavefrontObject(pathname, false);
	}
	
	/**
	 * Reads a Wavefront Object file into a {@code List} of {@code TriangleMesh3F} instances.
	 * <p>
	 * Returns a {@code List} of {@code TriangleMesh3F} instances.
	 * <p>
	 * If {@code pathname} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O-error occurs, an {@code UncheckedIOException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * TriangleMesh3F.readWavefrontObject(pathname, isFlippingTextureCoordinateY, 1.0F);
	 * }
	 * </pre>
	 * 
	 * @param pathname a {@code String} instance with the pathname to a file
	 * @param isFlippingTextureCoordinateY {@code true} if, and only if, the Y-coordinate of the texture coordinates should be flipped, {@code false} otherwise
	 * @return a {@code List} of {@code TriangleMesh3F} instances
	 * @throws NullPointerException thrown if, and only if, {@code pathname} is {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O-error occurs
	 */
	public static List<TriangleMesh3F> readWavefrontObject(final String pathname, final boolean isFlippingTextureCoordinateY) {
		return readWavefrontObject(pathname, isFlippingTextureCoordinateY, 1.0F);
	}
	
	/**
	 * Reads a Wavefront Object file into a {@code List} of {@code TriangleMesh3F} instances.
	 * <p>
	 * Returns a {@code List} of {@code TriangleMesh3F} instances.
	 * <p>
	 * If {@code pathname} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O-error occurs, an {@code UncheckedIOException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * TriangleMesh3F.readWavefrontObject(pathname, isFlippingTextureCoordinateY, scale, true);
	 * }
	 * </pre>
	 * 
	 * @param pathname a {@code String} instance with the pathname to a file
	 * @param isFlippingTextureCoordinateY {@code true} if, and only if, the Y-coordinate of the texture coordinates should be flipped, {@code false} otherwise
	 * @param scale the scale to apply to all {@link Triangle3F} instances
	 * @return a {@code List} of {@code TriangleMesh3F} instances
	 * @throws NullPointerException thrown if, and only if, {@code pathname} is {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O-error occurs
	 */
	public static List<TriangleMesh3F> readWavefrontObject(final String pathname, final boolean isFlippingTextureCoordinateY, final float scale) {
		return readWavefrontObject(pathname, isFlippingTextureCoordinateY, scale, true);
	}
	
	/**
	 * Reads a Wavefront Object file into a {@code List} of {@code TriangleMesh3F} instances.
	 * <p>
	 * Returns a {@code List} of {@code TriangleMesh3F} instances.
	 * <p>
	 * If {@code pathname} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O-error occurs, an {@code UncheckedIOException} will be thrown.
	 * 
	 * @param pathname a {@code String} instance with the pathname to a file
	 * @param isFlippingTextureCoordinateY {@code true} if, and only if, the Y-coordinate of the texture coordinates should be flipped, {@code false} otherwise
	 * @param scale the scale to apply to all {@link Triangle3F} instances
	 * @param isUsingAccelerationStructure {@code true} if, and only if, an acceleration structure should be used, {@code false} otherwise
	 * @return a {@code List} of {@code TriangleMesh3F} instances
	 * @throws NullPointerException thrown if, and only if, {@code pathname} is {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O-error occurs
	 */
	public static List<TriangleMesh3F> readWavefrontObject(final String pathname, final boolean isFlippingTextureCoordinateY, final float scale, final boolean isUsingAccelerationStructure) {
		return readWavefrontObject(new File(Objects.requireNonNull(pathname, "pathname == null")), isFlippingTextureCoordinateY, scale, isUsingAccelerationStructure);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static BoundingVolume3F doCreateBoundingVolume(final List<Triangle3F> triangles) {
		Point3F maximum = Point3F.MINIMUM;
		Point3F minimum = Point3F.MAXIMUM;
		
		for(final Triangle3F triangle : triangles) {
			final Point3F a = triangle.getA().getPosition();
			final Point3F b = triangle.getB().getPosition();
			final Point3F c = triangle.getC().getPosition();
			
			maximum = Point3F.maximum(maximum, Point3F.maximum(a, b, c));
			minimum = Point3F.minimum(minimum, Point3F.minimum(a, b, c));
		}
		
		return new AxisAlignedBoundingBox3F(maximum, minimum);
	}
	
	private static Node doCreateNode(final List<LeafNode> processableLeafNodes, final Point3F maximum, final Point3F minimum, final int depth) {
		final int size = processableLeafNodes.size();
		final int sizeHalf = size / 2;
		
		if(size < 4) {
			final List<Triangle3F> triangles = new ArrayList<>();
			
			for(final LeafNode processableLeafNode : processableLeafNodes) {
				for(final Triangle3F triangle : processableLeafNode.getTriangles()) {
					triangles.add(triangle);
				}
			}
			
			return new LeafNode(maximum, minimum, depth, triangles);
		}
		
		final float sideX = maximum.getX() - minimum.getX();
		final float sideY = maximum.getY() - minimum.getY();
		final float sideZ = maximum.getZ() - minimum.getZ();
		
		float minimumCost = size * (sideX * sideY + sideY * sideZ + sideZ * sideX);
		float bestSplit = Float.MAX_VALUE;
		
		int bestAxis = -1;
		
		for(int axis = 0; axis < 3; axis++) {
			final float start = minimum.getComponent(axis);
			final float stop  = maximum.getComponent(axis);
			
			if(abs(stop - start) < 1.0e-4F) {
				continue;
			}
			
			final float step = (stop - start) / (1024.0F / (depth + 1.0F));
			
			for(float oldSplit = 0.0F, newSplit = start + step; newSplit < stop - step; oldSplit = newSplit, newSplit += step) {
//				The following test prevents an infinite loop from occurring:
				if(equal(oldSplit, newSplit)) {
					break;
				}
				
				float maximumLX = Float.MIN_VALUE;
				float maximumLY = Float.MIN_VALUE;
				float maximumLZ = Float.MIN_VALUE;
				float minimumLX = Float.MAX_VALUE;
				float minimumLY = Float.MAX_VALUE;
				float minimumLZ = Float.MAX_VALUE;
				float maximumRX = Float.MIN_VALUE;
				float maximumRY = Float.MIN_VALUE;
				float maximumRZ = Float.MIN_VALUE;
				float minimumRX = Float.MAX_VALUE;
				float minimumRY = Float.MAX_VALUE;
				float minimumRZ = Float.MAX_VALUE;
				
				int countL = 0;
				int countR = 0;
				
				for(final LeafNode processableLeafNode : processableLeafNodes) {
					final BoundingVolume3F boundingVolume = processableLeafNode.getBoundingVolume();
					
					final Point3F max = boundingVolume.getMaximum();
					final Point3F mid = boundingVolume.getMidpoint();
					final Point3F min = boundingVolume.getMinimum();
					
					final float value = mid.getComponent(axis);
					
					if(value < newSplit) {
						maximumLX = max(maximumLX, max.getX());
						maximumLY = max(maximumLY, max.getY());
						maximumLZ = max(maximumLZ, max.getZ());
						minimumLX = min(minimumLX, min.getX());
						minimumLY = min(minimumLY, min.getY());
						minimumLZ = min(minimumLZ, min.getZ());
						
						countL++;
					} else {
						maximumRX = max(maximumRX, max.getX());
						maximumRY = max(maximumRY, max.getY());
						maximumRZ = max(maximumRZ, max.getZ());
						minimumRX = min(minimumRX, min.getX());
						minimumRY = min(minimumRY, min.getY());
						minimumRZ = min(minimumRZ, min.getZ());
						
						countR++;
					}
				}
				
				if(countL <= 1 || countR <= 1) {
					continue;
				}
				
				final float sideLX = maximumLX - minimumLX;
				final float sideLY = maximumLY - minimumLY;
				final float sideLZ = maximumLZ - minimumLZ;
				final float sideRX = maximumRX - minimumRX;
				final float sideRY = maximumRY - minimumRY;
				final float sideRZ = maximumRZ - minimumRZ;
				
				final float surfaceL = sideLX * sideLY + sideLY * sideLZ + sideLZ * sideLX;
				final float surfaceR = sideRX * sideRY + sideRY * sideRZ + sideRZ * sideRX;
				
				final float cost = surfaceL * countL + surfaceR * countR;
				
				if(cost < minimumCost) {
					minimumCost = cost;
					bestSplit = newSplit;
					bestAxis = axis;
				}
			}
		}
		
		if(bestAxis == -1) {
			final List<Triangle3F> triangles = new ArrayList<>();
			
			for(final LeafNode processableLeafNode : processableLeafNodes) {
				for(final Triangle3F triangle : processableLeafNode.getTriangles()) {
					triangles.add(triangle);
				}
			}
			
			return new LeafNode(maximum, minimum, depth, triangles);
		}
		
		final List<LeafNode> leafNodesL = new ArrayList<>(sizeHalf);
		final List<LeafNode> leafNodesR = new ArrayList<>(sizeHalf);
		
		float maximumLX = Float.MIN_VALUE;
		float maximumLY = Float.MIN_VALUE;
		float maximumLZ = Float.MIN_VALUE;
		float minimumLX = Float.MAX_VALUE;
		float minimumLY = Float.MAX_VALUE;
		float minimumLZ = Float.MAX_VALUE;
		float maximumRX = Float.MIN_VALUE;
		float maximumRY = Float.MIN_VALUE;
		float maximumRZ = Float.MIN_VALUE;
		float minimumRX = Float.MAX_VALUE;
		float minimumRY = Float.MAX_VALUE;
		float minimumRZ = Float.MAX_VALUE;
		
		for(final LeafNode processableLeafNode : processableLeafNodes) {
			final BoundingVolume3F boundingVolume = processableLeafNode.getBoundingVolume();
			
			final Point3F max = boundingVolume.getMaximum();
			final Point3F mid = boundingVolume.getMidpoint();
			final Point3F min = boundingVolume.getMinimum();
			
			final float value = mid.getComponent(bestAxis);
			
			if(value < bestSplit) {
				leafNodesL.add(processableLeafNode);
				
				maximumLX = max(maximumLX, max.getX());
				maximumLY = max(maximumLY, max.getY());
				maximumLZ = max(maximumLZ, max.getZ());
				minimumLX = min(minimumLX, min.getX());
				minimumLY = min(minimumLY, min.getY());
				minimumLZ = min(minimumLZ, min.getZ());
			} else {
				leafNodesR.add(processableLeafNode);
				
				maximumRX = max(maximumRX, max.getX());
				maximumRY = max(maximumRY, max.getY());
				maximumRZ = max(maximumRZ, max.getZ());
				minimumRX = min(minimumRX, min.getX());
				minimumRY = min(minimumRY, min.getY());
				minimumRZ = min(minimumRZ, min.getZ());
			}
		}
		
		final Point3F maximumL = new Point3F(maximumLX, maximumLY, maximumLZ);
		final Point3F minimumL = new Point3F(minimumLX, minimumLY, minimumLZ);
		final Point3F maximumR = new Point3F(maximumRX, maximumRY, maximumRZ);
		final Point3F minimumR = new Point3F(minimumRX, minimumRY, minimumRZ);
		
		final Node nodeL = doCreateNode(leafNodesL, maximumL, minimumL, depth + 1);
		final Node nodeR = doCreateNode(leafNodesR, maximumR, minimumR, depth + 1);
		
		return new TreeNode(maximum, minimum, depth, nodeL, nodeR);
	}
	
	private static Node doCreateNode(final List<Triangle3F> triangles) {
		final List<LeafNode> processableLeafNodes = new ArrayList<>(triangles.size());
		
		float maximumX = Float.MIN_VALUE;
		float maximumY = Float.MIN_VALUE;
		float maximumZ = Float.MIN_VALUE;
		float minimumX = Float.MAX_VALUE;
		float minimumY = Float.MAX_VALUE;
		float minimumZ = Float.MAX_VALUE;
		
		for(final Triangle3F triangle : triangles) {
			final Point3F a = triangle.getA().getPosition();
			final Point3F b = triangle.getB().getPosition();
			final Point3F c = triangle.getC().getPosition();
			
			final Point3F maximum = Point3F.maximum(a, b, c);
			final Point3F minimum = Point3F.minimum(a, b, c);
			
			maximumX = max(maximumX, maximum.getX());
			maximumY = max(maximumY, maximum.getY());
			maximumZ = max(maximumZ, maximum.getZ());
			minimumX = min(minimumX, minimum.getX());
			minimumY = min(minimumY, minimum.getY());
			minimumZ = min(minimumZ, minimum.getZ());
			
			processableLeafNodes.add(new LeafNode(maximum, minimum, 0, Arrays.asList(triangle)));
		}
		
		return doCreateNode(processableLeafNodes, new Point3F(maximumX, maximumY, maximumZ), new Point3F(minimumX, minimumY, minimumZ), 0);
	}
	
	private static float doCalculateSurfaceArea(final List<Triangle3F> triangles) {
		float surfaceArea = 0.0F;
		
		for(final Triangle3F triangle : triangles) {
			surfaceArea += triangle.getSurfaceArea();
		}
		
		return surfaceArea;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static final class DefaultObjectModel {
		private final List<Point2F> textureCoordinates;
		private final List<Point3F> positions;
		private final List<String> groupNames;
		private final List<String> materialNames;
		private final List<String> objectNames;
		private final List<Vector3F> normals;
		private final List<Vertex> vertices;
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		public DefaultObjectModel() {
			this.textureCoordinates = new ArrayList<>();
			this.positions = new ArrayList<>();
			this.groupNames = new ArrayList<>();
			this.materialNames = new ArrayList<>();
			this.objectNames = new ArrayList<>();
			this.normals = new ArrayList<>();
			this.vertices = new ArrayList<>();
		}
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		public IndexedObjectModel toIndexedObjectModel() {
			final IndexedObjectModel indexedObjectModel0 = new IndexedObjectModel();
			final IndexedObjectModel indexedObjectModel1 = new IndexedObjectModel();
			
			final Map<Integer, Integer> normalModelIndices0 = new HashMap<>();
			final Map<Integer, Integer> normalModelIndices1 = new HashMap<>();
			final Map<Vertex, Integer> modelVertexIndices = new HashMap<>();
			
			boolean hasNormals = false;
			
			for(int i = 0; i < this.vertices.size(); i++) {
				final Vertex vertex = this.vertices.get(i);
				
				final Point2F textureCoordinates = vertex.hasTextureVertexIndex() ? this.textureCoordinates.get(vertex.getTextureVertexIndex()) : new Point2F();
				
				final Point3F position = vertex.hasGeometricVertexIndex() ? this.positions.get(vertex.getGeometricVertexIndex()) : new Point3F();
				
				final Vector3F normal = vertex.hasVertexNormalIndex() ? this.normals.get(vertex.getVertexNormalIndex()) : new Vector3F();
				
				if(vertex.hasVertexNormalIndex()) {
					hasNormals = true;
				}
				
				final String groupName = this.groupNames.get(i);
				final String materialName = this.materialNames.get(i);
				final String objectName = this.objectNames.get(i);
				
				final Integer modelVertexIndex = modelVertexIndices.computeIfAbsent(vertex, keyVertex -> {
					indexedObjectModel0.addGroupName(groupName);
					indexedObjectModel0.addMaterialName(materialName);
					indexedObjectModel0.addObjectName(objectName);
					indexedObjectModel0.addPosition(position);
					indexedObjectModel0.addTextureCoordinates(textureCoordinates);
					
					if(vertex.hasVertexNormalIndex()) {
						indexedObjectModel0.addNormal(normal);
					}
					
					return Integer.valueOf(indexedObjectModel0.getPositionCount() - 1);
				});
				
				final Integer normalModelIndex = normalModelIndices0.computeIfAbsent(Integer.valueOf(vertex.getGeometricVertexIndex()), keyGeometricVertexIndex -> {
					indexedObjectModel1.addGroupName(groupName);
					indexedObjectModel1.addMaterialName(materialName);
					indexedObjectModel1.addNormal(normal);
					indexedObjectModel1.addObjectName(objectName);
					indexedObjectModel1.addPosition(position);
					indexedObjectModel1.addTangent(new Vector3F());
					indexedObjectModel1.addTextureCoordinates(textureCoordinates);
					
					return Integer.valueOf(indexedObjectModel1.getPositionCount() - 1);
				});
				
				indexedObjectModel0.addIndex(modelVertexIndex);
				indexedObjectModel1.addIndex(normalModelIndex);
				
				normalModelIndices1.put(modelVertexIndex, normalModelIndex);
			}
			
			if(!hasNormals) {
				indexedObjectModel1.calculateNormals();
				
				for(int i = 0; i < indexedObjectModel0.getPositionCount(); i++) {
					indexedObjectModel0.addNormal(indexedObjectModel1.getNormal(normalModelIndices1.get(Integer.valueOf(i)).intValue()));
				}
			}
			
			indexedObjectModel1.calculateTangents();
			
			for(int i = 0; i < indexedObjectModel0.getPositionCount(); i++) {
				indexedObjectModel0.addTangent(indexedObjectModel1.getTangent(normalModelIndices1.get(Integer.valueOf(i)).intValue()));
			}
			
			return indexedObjectModel0;
		}
		
		public void addGroupName(final String groupName) {
			this.groupNames.add(Objects.requireNonNull(groupName, "groupName == null"));
		}
		
		public void addMaterialName(final String materialName) {
			this.materialNames.add(Objects.requireNonNull(materialName, "materialName == null"));
		}
		
		public void addNormal(final Vector3F normal) {
			this.normals.add(Objects.requireNonNull(normal, "normal == null"));
		}
		
		public void addObjectName(final String objectName) {
			this.objectNames.add(Objects.requireNonNull(objectName, "objectName == null"));
		}
		
		public void addPosition(final Point3F position) {
			this.positions.add(Objects.requireNonNull(position, "position == null"));
		}
		
		public void addTextureCoordinates(final Point2F textureCoordinates) {
			this.textureCoordinates.add(Objects.requireNonNull(textureCoordinates, "textureCoordinates == null"));
		}
		
		public void addVertex(final Vertex vertex) {
			this.vertices.add(Objects.requireNonNull(vertex, "vertex == null"));
		}
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		public static DefaultObjectModel parseDefaultObjectModel(final File file, final boolean isFlippingTextureCoordinateY) throws IOException {
			final DefaultObjectModel defaultObjectModel = new DefaultObjectModel();
			
			String currentGroupName = "";
			String currentMaterialName = "";
			String currentObjectName = "";
			
			try(final BufferedReader bufferedReader = new BufferedReader(new FileReader(Objects.requireNonNull(file, "file == null")))) {
				for(String line = bufferedReader.readLine(); line != null; line = bufferedReader.readLine()) {
					final String[] elements = line.split("\\s+");
					
					if(elements.length > 0) {
						switch(elements[0]) {
							case "#":
								continue;
							case "f":
								for(int i = 0; i < elements.length - 3; i++) {
									defaultObjectModel.addGroupName(currentGroupName);
									defaultObjectModel.addGroupName(currentGroupName);
									defaultObjectModel.addGroupName(currentGroupName);
									defaultObjectModel.addMaterialName(currentMaterialName);
									defaultObjectModel.addMaterialName(currentMaterialName);
									defaultObjectModel.addMaterialName(currentMaterialName);
									defaultObjectModel.addObjectName(currentObjectName);
									defaultObjectModel.addObjectName(currentObjectName);
									defaultObjectModel.addObjectName(currentObjectName);
									defaultObjectModel.addVertex(Vertex.parseVertex(elements[1 + 0]));
									defaultObjectModel.addVertex(Vertex.parseVertex(elements[2 + i]));
									defaultObjectModel.addVertex(Vertex.parseVertex(elements[3 + i]));
								}
								
								break;
							case "g":
								currentGroupName = elements[1];
								
								break;
							case "o":
								currentObjectName = elements[1];
								
								break;
							case "usemtl":
								currentMaterialName = elements[1];
								
								break;
							case "v":
								defaultObjectModel.addPosition(new Point3F(Float.parseFloat(elements[1]), Float.parseFloat(elements[2]), Float.parseFloat(elements[3])));
								
								break;
							case "vn":
								defaultObjectModel.addNormal(new Vector3F(Float.parseFloat(elements[1]), Float.parseFloat(elements[2]), Float.parseFloat(elements[3])));
								
								break;
							case "vt":
								defaultObjectModel.addTextureCoordinates(new Point2F(Float.parseFloat(elements[1]), isFlippingTextureCoordinateY ? 1.0F - Float.parseFloat(elements[2]) : Float.parseFloat(elements[2])));
								
								break;
							default:
								break;
						}
					}
				}
			}
			
			return defaultObjectModel;
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static final class IndexedObjectModel {
		private final List<Integer> indices;
		private final List<Point2F> textureCoordinates;
		private final List<Point3F> positions;
		private final List<String> groupNames;
		private final List<String> materialNames;
		private final List<String> objectNames;
		private final List<Vector3F> normals;
		private final List<Vector3F> tangents;
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		public IndexedObjectModel() {
			this.indices = new ArrayList<>();
			this.textureCoordinates = new ArrayList<>();
			this.positions = new ArrayList<>();
			this.groupNames = new ArrayList<>();
			this.materialNames = new ArrayList<>();
			this.objectNames = new ArrayList<>();
			this.normals = new ArrayList<>();
			this.tangents = new ArrayList<>();
		}
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		public List<Integer> getIndices() {
			return this.indices;
		}
		
		public List<Point2F> getTextureCoordinates() {
			return this.textureCoordinates;
		}
		
		public List<Point3F> getPositions() {
			return this.positions;
		}
		
		public List<String> getGroupNames() {
			return this.groupNames;
		}
		
		public List<String> getMaterialNames() {
			return this.materialNames;
		}
		
		public List<String> getObjectNames() {
			return this.objectNames;
		}
		
		public List<Vector3F> getNormals() {
			return this.normals;
		}
		
		public List<Vector3F> getTangents() {
			return this.tangents;
		}
		
		public Vector3F getNormal(final int index) {
			return this.normals.get(index);
		}
		
		public Vector3F getTangent(final int index) {
			return this.tangents.get(index);
		}
		
		public int getPositionCount() {
			return this.positions.size();
		}
		
		public void addGroupName(final String groupName) {
			this.groupNames.add(Objects.requireNonNull(groupName, "groupName == null"));
		}
		
		public void addIndex(final Integer index) {
			this.indices.add(Objects.requireNonNull(index, "index == null"));
		}
		
		public void addMaterialName(final String materialName) {
			this.materialNames.add(Objects.requireNonNull(materialName, "materialName == null"));
		}
		
		public void addNormal(final Vector3F normal) {
			this.normals.add(Objects.requireNonNull(normal, "normal == null"));
		}
		
		public void addObjectName(final String objectName) {
			this.objectNames.add(Objects.requireNonNull(objectName, "objectName == null"));
		}
		
		public void addPosition(final Point3F position) {
			this.positions.add(Objects.requireNonNull(position, "position == null"));
		}
		
		public void addTangent(final Vector3F tangent) {
			this.tangents.add(Objects.requireNonNull(tangent, "tangent == null"));
			
			if(Float.isNaN(tangent.getX()) || Float.isNaN(tangent.getY()) || Float.isNaN(tangent.getZ())) {
				throw new IllegalArgumentException(tangent.toString());
			}
		}
		
		public void addTextureCoordinates(final Point2F textureCoordinates) {
			this.textureCoordinates.add(Objects.requireNonNull(textureCoordinates, "textureCoordinates == null"));
		}
		
		public void calculateNormals() {
			for(int i = 0; i < this.indices.size(); i += 3) {
				final int indexA = this.indices.get(i + 0).intValue();
				final int indexB = this.indices.get(i + 1).intValue();
				final int indexC = this.indices.get(i + 2).intValue();
				
				final Vector3F edgeAB = Vector3F.direction(this.positions.get(indexA), this.positions.get(indexB));
				final Vector3F edgeAC = Vector3F.direction(this.positions.get(indexA), this.positions.get(indexC));
				final Vector3F normal = Vector3F.normalize(Vector3F.crossProduct(edgeAB, edgeAC));
				
				this.normals.set(indexA, Vector3F.add(this.normals.get(indexA), normal));
				this.normals.set(indexB, Vector3F.add(this.normals.get(indexB), normal));
				this.normals.set(indexC, Vector3F.add(this.normals.get(indexC), normal));
			}
			
			for(int i = 0; i < this.normals.size(); i++) {
				this.normals.set(i, Vector3F.normalize(this.normals.get(i)));
			}
		}
		
		public void calculateTangents() {
			for(int i = 0; i < this.indices.size(); i += 3) {
				final int indexA = this.indices.get(i + 0).intValue();
				final int indexB = this.indices.get(i + 1).intValue();
				final int indexC = this.indices.get(i + 2).intValue();
				
				final Vector3F edgeAB = Vector3F.direction(this.positions.get(indexA), this.positions.get(indexB));
				final Vector3F edgeAC = Vector3F.direction(this.positions.get(indexA), this.positions.get(indexC));
				
				final float deltaABU = this.textureCoordinates.get(indexB).getX() - this.textureCoordinates.get(indexA).getX();
				final float deltaABV = this.textureCoordinates.get(indexB).getY() - this.textureCoordinates.get(indexA).getY();
				final float deltaACU = this.textureCoordinates.get(indexC).getX() - this.textureCoordinates.get(indexA).getX();
				final float deltaACV = this.textureCoordinates.get(indexC).getY() - this.textureCoordinates.get(indexA).getY();
				
				final float dividend = (deltaABU * deltaACV - deltaACU * deltaABV);
				final float fraction = dividend < -0.0F || dividend > +0.0F ? 1.0F / dividend : 0.0F;
				
				final float x = fraction * (deltaACV * edgeAB.getX() - deltaABV * edgeAC.getX());
				final float y = fraction * (deltaACV * edgeAB.getY() - deltaABV * edgeAC.getY());
				final float z = fraction * (deltaACV * edgeAB.getY() - deltaABV * edgeAC.getY());
				
				final Vector3F tangent = new Vector3F(x, y, z);
				
				this.tangents.set(indexA, Vector3F.add(this.tangents.get(indexA), tangent));
				this.tangents.set(indexB, Vector3F.add(this.tangents.get(indexB), tangent));
				this.tangents.set(indexC, Vector3F.add(this.tangents.get(indexC), tangent));
			}
			
			for(int i = 0; i < this.tangents.size(); i++) {
				this.tangents.set(i, Vector3F.normalize(this.tangents.get(i)));
			}
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static final class LeafNode extends Node {
		private final List<Triangle3F> triangles;
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		public LeafNode(final Point3F maximum, final Point3F minimum, final int depth, final List<Triangle3F> triangles) {
			super(maximum, minimum, depth);
			
			this.triangles = Objects.requireNonNull(triangles, "triangles == null");
		}
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		public List<Triangle3F> getTriangles() {
			return this.triangles;
		}
		
		@Override
		public Optional<SurfaceIntersection3F> intersection(final Ray3F ray, final float[] tBounds) {
			Optional<SurfaceIntersection3F> optionalSurfaceIntersection = SurfaceIntersection3F.EMPTY;
			
			if(getBoundingVolume().contains(ray.getOrigin()) || getBoundingVolume().intersects(ray, tBounds[0], tBounds[1])) {
				for(final Triangle3F triangle : this.triangles) {
					optionalSurfaceIntersection = SurfaceIntersection3F.closest(optionalSurfaceIntersection, triangle.intersection(ray, tBounds[0], tBounds[1]));
					
					if(optionalSurfaceIntersection.isPresent()) {
						tBounds[1] = optionalSurfaceIntersection.get().getT();
					}
				}
			}
			
			return optionalSurfaceIntersection;
		}
		
		@Override
		public boolean equals(final Object object) {
			if(object == this) {
				return true;
			} else if(!(object instanceof LeafNode)) {
				return false;
			} else if(!Objects.equals(getBoundingVolume(), LeafNode.class.cast(object).getBoundingVolume())) {
				return false;
			} else if(getDepth() != LeafNode.class.cast(object).getDepth()) {
				return false;
			} else if(!Objects.equals(this.triangles, LeafNode.class.cast(object).triangles)) {
				return false;
			} else {
				return true;
			}
		}
		
		@Override
		public boolean intersection(final MutableSurfaceIntersection3F mutableSurfaceIntersection) {
			if(mutableSurfaceIntersection.isIntersecting(getBoundingVolume())) {
				boolean isIntersecting = false;
				
				for(final Triangle3F triangle : this.triangles) {
					if(mutableSurfaceIntersection.intersection(triangle)) {
						isIntersecting = true;
					}
				}
				
				return isIntersecting;
			}
			
			return false;
		}
		
		@Override
		public boolean intersects(final Ray3F ray, final float tMinimum, final float tMaximum) {
			if(getBoundingVolume().contains(ray.getOrigin()) || getBoundingVolume().intersects(ray, tMinimum, tMaximum)) {
				for(final Triangle3F triangle : this.triangles) {
					if(triangle.intersects(ray, tMinimum, tMaximum)) {
						return true;
					}
				}
			}
			
			return false;
		}
		
		@Override
		public float getSurfaceArea() {
			float surfaceArea = 0.0F;
			
			for(final Triangle3F triangle : this.triangles) {
				surfaceArea += triangle.getSurfaceArea();
			}
			
			return surfaceArea;
		}
		
		@Override
		public float intersectionT(final Ray3F ray, final float[] tBounds) {
			float t = Float.NaN;
			
			if(getBoundingVolume().contains(ray.getOrigin()) || getBoundingVolume().intersects(ray, tBounds[0], tBounds[1])) {
				for(final Triangle3F triangle : this.triangles) {
					t = minOrNaN(t, triangle.intersectionT(ray, tBounds[0], tBounds[1]));
					
					if(!isNaN(t)) {
						tBounds[1] = t;
					}
				}
			}
			
			return t;
		}
		
		@Override
		public int hashCode() {
			return Objects.hash(getBoundingVolume(), Integer.valueOf(getDepth()), this.triangles);
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static abstract class Node {
		private final BoundingVolume3F boundingVolume;
		private final int depth;
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		protected Node(final Point3F maximum, final Point3F minimum, final int depth) {
			this.boundingVolume = new AxisAlignedBoundingBox3F(maximum, minimum);
			this.depth = depth;
		}
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		public final BoundingVolume3F getBoundingVolume() {
			return this.boundingVolume;
		}
		
		public abstract Optional<SurfaceIntersection3F> intersection(final Ray3F ray, final float[] tBounds);
		
		public abstract boolean intersection(final MutableSurfaceIntersection3F mutableSurfaceIntersection);
		
		public abstract boolean intersects(final Ray3F ray, final float tMinimum, final float tMaximum);
		
		public abstract float getSurfaceArea();
		
		public abstract float intersectionT(final Ray3F ray, final float[] tBounds);
		
		public final int getDepth() {
			return this.depth;
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static final class TreeNode extends Node {
		private final Node nodeL;
		private final Node nodeR;
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		public TreeNode(final Point3F maximum, final Point3F minimum, final int depth, final Node nodeL, final Node nodeR) {
			super(maximum, minimum, depth);
			
			this.nodeL = Objects.requireNonNull(nodeL, "nodeL == null");
			this.nodeR = Objects.requireNonNull(nodeR, "nodeR == null");
		}
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		@Override
		public Optional<SurfaceIntersection3F> intersection(final Ray3F ray, final float[] tBounds) {
			return getBoundingVolume().contains(ray.getOrigin()) || getBoundingVolume().intersects(ray, tBounds[0], tBounds[1]) ? SurfaceIntersection3F.closest(this.nodeL.intersection(ray, tBounds), this.nodeR.intersection(ray, tBounds)) : Optional.empty();
		}
		
		@Override
		public boolean equals(final Object object) {
			if(object == this) {
				return true;
			} else if(!(object instanceof TreeNode)) {
				return false;
			} else if(!Objects.equals(getBoundingVolume(), TreeNode.class.cast(object).getBoundingVolume())) {
				return false;
			} else if(getDepth() != TreeNode.class.cast(object).getDepth()) {
				return false;
			} else if(!Objects.equals(this.nodeL, TreeNode.class.cast(object).nodeL)) {
				return false;
			} else if(!Objects.equals(this.nodeR, TreeNode.class.cast(object).nodeR)) {
				return false;
			} else {
				return true;
			}
		}
		
		@Override
		public boolean intersection(final MutableSurfaceIntersection3F mutableSurfaceIntersection) {
			if(mutableSurfaceIntersection.isIntersecting(getBoundingVolume())) {
				final boolean isIntersectingL = this.nodeL.intersection(mutableSurfaceIntersection);
				final boolean isIntersectingR = this.nodeR.intersection(mutableSurfaceIntersection);
				
				return isIntersectingL || isIntersectingR;
			}
			
			return false;
		}
		
		@Override
		public boolean intersects(final Ray3F ray, final float tMinimum, final float tMaximum) {
			return (getBoundingVolume().contains(ray.getOrigin()) || getBoundingVolume().intersects(ray, tMinimum, tMaximum)) && (this.nodeL.intersects(ray, tMinimum, tMaximum) || this.nodeR.intersects(ray, tMinimum, tMaximum));
		}
		
		@Override
		public float getSurfaceArea() {
			return this.nodeL.getSurfaceArea() + this.nodeR.getSurfaceArea();
		}
		
		@Override
		public float intersectionT(final Ray3F ray, final float[] tBounds) {
			return getBoundingVolume().contains(ray.getOrigin()) || getBoundingVolume().intersects(ray, tBounds[0], tBounds[1]) ? minOrNaN(this.nodeL.intersectionT(ray, tBounds), this.nodeR.intersectionT(ray, tBounds)) : Float.NaN;
		}
		
		@Override
		public int hashCode() {
			return Objects.hash(getBoundingVolume(), Integer.valueOf(getDepth()), this.nodeL, this.nodeR);
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static final class Vertex {
		private final int geometricVertexIndex;
		private final int textureVertexIndex;
		private final int vertexNormalIndex;
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		public Vertex(final int geometricVertexIndex, final int textureVertexIndex, final int vertexNormalIndex) {
			this.geometricVertexIndex = geometricVertexIndex;
			this.textureVertexIndex = textureVertexIndex;
			this.vertexNormalIndex = vertexNormalIndex;
		}
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		@Override
		public boolean equals(final Object object) {
			if(object == this) {
				return true;
			} else if(!(object instanceof Vertex)) {
				return false;
			} else if(this.geometricVertexIndex != Vertex.class.cast(object).geometricVertexIndex) {
				return false;
			} else if(this.textureVertexIndex != Vertex.class.cast(object).textureVertexIndex) {
				return false;
			} else if(this.vertexNormalIndex != Vertex.class.cast(object).vertexNormalIndex) {
				return false;
			} else {
				return true;
			}
		}
		
		public boolean hasGeometricVertexIndex() {
			return this.geometricVertexIndex != Integer.MIN_VALUE;
		}
		
		public boolean hasTextureVertexIndex() {
			return this.textureVertexIndex != Integer.MIN_VALUE;
		}
		
		public boolean hasVertexNormalIndex() {
			return this.vertexNormalIndex != Integer.MIN_VALUE;
		}
		
		public int getGeometricVertexIndex() {
			return this.geometricVertexIndex;
		}
		
		public int getTextureVertexIndex() {
			return this.textureVertexIndex;
		}
		
		public int getVertexNormalIndex() {
			return this.vertexNormalIndex;
		}
		
		@Override
		public int hashCode() {
			return Objects.hash(Integer.valueOf(this.geometricVertexIndex), Integer.valueOf(this.textureVertexIndex), Integer.valueOf(this.vertexNormalIndex));
		}
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		public static Vertex parseVertex(final String string) {
			final String[] strings = string.split("/");
			
			final int geometricVertexIndex = strings.length > 0 && !strings[0].isEmpty() ? Integer.parseInt(strings[0]) - 1 : Integer.MIN_VALUE;
			final int textureVertexIndex   = strings.length > 1 && !strings[1].isEmpty() ? Integer.parseInt(strings[1]) - 1 : Integer.MIN_VALUE;
			final int vertexNormalIndex    = strings.length > 2 && !strings[2].isEmpty() ? Integer.parseInt(strings[2]) - 1 : Integer.MIN_VALUE;
			
			return new Vertex(geometricVertexIndex, textureVertexIndex, vertexNormalIndex);
		}
	}
}