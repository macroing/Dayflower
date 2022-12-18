/**
 * Copyright 2014 - 2022 J&#246;rgen Lundgren
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

import static org.dayflower.utility.Doubles.minOrNaN;

import java.io.BufferedReader;
import java.io.DataOutput;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.Field;//TODO: Add Unit Tests!
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Scanner;

import org.dayflower.geometry.BoundingVolume3D;
import org.dayflower.geometry.Matrix44D;
import org.dayflower.geometry.Point2D;
import org.dayflower.geometry.Point3D;
import org.dayflower.geometry.Point4D;
import org.dayflower.geometry.Ray3D;
import org.dayflower.geometry.Shape3D;
import org.dayflower.geometry.SurfaceIntersection3D;
import org.dayflower.geometry.SurfaceIntersector3D;
import org.dayflower.geometry.Vector3D;
import org.dayflower.geometry.boundingvolume.AxisAlignedBoundingBox3D;
import org.dayflower.geometry.boundingvolume.hierarchy.BVHItem3D;
import org.dayflower.geometry.boundingvolume.hierarchy.BVHNode3D;
import org.dayflower.geometry.boundingvolume.hierarchy.BVHNode3Ds;
import org.dayflower.geometry.shape.Triangle3D.Vertex3D;
import org.dayflower.node.NodeFilter;
import org.dayflower.node.NodeHierarchicalVisitor;
import org.dayflower.node.NodeTraversalException;
import org.dayflower.utility.ParameterArguments;
import org.macroing.java.lang.Doubles;

/**
 * A {@code TriangleMesh3D} is an implementation of {@link Shape3D} that represents a triangle mesh.
 * <p>
 * This class is immutable and therefore thread-safe.
 * <p>
 * This {@code Shape3D} implementation is not supported on the GPU.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class TriangleMesh3D implements Shape3D {
	/**
	 * The name of this {@code TriangleMesh3D} class.
	 */
//	TODO: Add Unit Tests!
	public static final String NAME = "Triangle Mesh";
	
	/**
	 * The ID of this {@code TriangleMesh3D} class.
	 */
//	TODO: Add Unit Tests!
	public static final int ID = 18;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final BVHNode3D bVHNode;
	private final BoundingVolume3D boundingVolume;
	private final List<Triangle3D> triangles;
	private final String groupName;
	private final String materialName;
	private final String objectName;
	private final boolean isUsingAccelerationStructure;
	private final double surfaceArea;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code TriangleMesh3D} instance.
	 * <p>
	 * If either {@code triangles}, at least one of its elements, {@code groupName}, {@code materialName} or {@code objectName} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new TriangleMesh3D(triangles, groupName, materialName, objectName, true);
	 * }
	 * </pre>
	 * 
	 * @param triangles a {@code List} of {@link Triangle3D} instances
	 * @param groupName the group name of this {@code TriangleMesh3D} instance
	 * @param materialName the material name of this {@code TriangleMesh3D} instance
	 * @param objectName the object name of this {@code TriangleMesh3D} instance
	 * @throws NullPointerException thrown if, and only if, either {@code triangles}, at least one of its elements, {@code groupName}, {@code materialName} or {@code objectName} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public TriangleMesh3D(final List<Triangle3D> triangles, final String groupName, final String materialName, final String objectName) {
		this(triangles, groupName, materialName, objectName, true);
	}
	
	/**
	 * Constructs a new {@code TriangleMesh3D} instance.
	 * <p>
	 * If either {@code triangles}, at least one of its elements, {@code groupName}, {@code materialName} or {@code objectName} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param triangles a {@code List} of {@link Triangle3D} instances
	 * @param groupName the group name of this {@code TriangleMesh3D} instance
	 * @param materialName the material name of this {@code TriangleMesh3D} instance
	 * @param objectName the object name of this {@code TriangleMesh3D} instance
	 * @param isUsingAccelerationStructure {@code true} if, and only if, an acceleration structure should be used, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, either {@code triangles}, at least one of its elements, {@code groupName}, {@code materialName} or {@code objectName} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public TriangleMesh3D(final List<Triangle3D> triangles, final String groupName, final String materialName, final String objectName, final boolean isUsingAccelerationStructure) {
		this.triangles = new ArrayList<>(ParameterArguments.requireNonNullList(triangles, "triangles"));
		this.bVHNode = isUsingAccelerationStructure ? doCreateBVHNode(this.triangles) : null;
		this.boundingVolume = isUsingAccelerationStructure ? this.bVHNode.getBoundingVolume() : doCreateBoundingVolume(this.triangles);
		this.groupName = Objects.requireNonNull(groupName, "groupName == null");
		this.materialName = Objects.requireNonNull(materialName, "materialName == null");
		this.objectName = Objects.requireNonNull(objectName, "objectName == null");
		this.isUsingAccelerationStructure = isUsingAccelerationStructure;
		this.surfaceArea = isUsingAccelerationStructure ? this.bVHNode.getSurfaceArea() : doCalculateSurfaceArea(this.triangles);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link BoundingVolume3D} instance that contains this {@code TriangleMesh3D} instance.
	 * 
	 * @return a {@code BoundingVolume3D} instance that contains this {@code TriangleMesh3D} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public BoundingVolume3D getBoundingVolume() {
		return this.boundingVolume;
	}
	
	/**
	 * Returns a {@code List} that contains all {@link BoundingVolume3D} instances used by the bounding volume hierarchy.
	 * 
	 * @return a {@code List} that contains all {@code BoundingVolume3D} instances used by the bounding volume hierarchy
	 */
//	TODO: Add Unit Tests!
	public List<BoundingVolume3D> getBoundingVolumes() {
		return this.isUsingAccelerationStructure ? NodeFilter.filterAll(this.bVHNode, BVHNode3D.class).stream().map(bVHNode -> bVHNode.getBoundingVolume()).collect(ArrayList::new, ArrayList::add, ArrayList::addAll) : new ArrayList<>();
	}
	
	/**
	 * Returns a {@code List} that contains all {@link Triangle3D} instances.
	 * 
	 * @return a {@code List} that contains all {@code Triangle3D} instances
	 */
//	TODO: Add Unit Tests!
	public List<Triangle3D> getTriangles() {
		return new ArrayList<>(this.triangles);
	}
	
	/**
	 * Returns the optional {@link BVHNode3D} instance that is used by this {@code TriangleMesh3D} instance.
	 * 
	 * @return the optional {@code BVHNode3D} instance that is used by this {@code TriangleMesh3D} instance
	 */
//	TODO: Add Unit Tests!
	public Optional<BVHNode3D> getRootBVHNode() {
		return this.isUsingAccelerationStructure ? Optional.of(this.bVHNode) : Optional.empty();
	}
	
	/**
	 * Performs an intersection test between {@code ray} and this {@code TriangleMesh3D} instance.
	 * <p>
	 * Returns an {@code Optional} with an optional {@link SurfaceIntersection3D} instance that contains information about the intersection, if it was found.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3D} to perform an intersection test against this {@code TriangleMesh3D} instance
	 * @param tMinimum the minimum parametric distance
	 * @param tMaximum the maximum parametric distance
	 * @return an {@code Optional} with an optional {@code SurfaceIntersection3D} instance that contains information about the intersection, if it was found
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
//	TODO: Add Unit Tests!
	@Override
	public Optional<SurfaceIntersection3D> intersection(final Ray3D ray, final double tMinimum, final double tMaximum) {
		if(this.isUsingAccelerationStructure) {
			return this.bVHNode.intersection(ray, tMinimum, tMaximum);
		}
		
		final SurfaceIntersector3D surfaceIntersector = new SurfaceIntersector3D(ray, tMinimum, tMaximum);
		
		for(final Triangle3D triangle : this.triangles) {
			surfaceIntersector.intersection(triangle);
		}
		
		return surfaceIntersector.computeSurfaceIntersection();
	}
	
	/**
	 * Returns the group name of this {@code TriangleMesh3D} instance.
	 * 
	 * @return the group name of this {@code TriangleMesh3D} instance
	 */
//	TODO: Add Unit Tests!
	public String getGroupName() {
		return this.groupName;
	}
	
	/**
	 * Returns the material name of this {@code TriangleMesh3D} instance.
	 * 
	 * @return the material name of this {@code TriangleMesh3D} instance
	 */
//	TODO: Add Unit Tests!
	public String getMaterialName() {
		return this.materialName;
	}
	
	/**
	 * Returns a {@code String} with the name of this {@code TriangleMesh3D} instance.
	 * 
	 * @return a {@code String} with the name of this {@code TriangleMesh3D} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public String getName() {
		return NAME;
	}
	
	/**
	 * Returns the object name of this {@code TriangleMesh3D} instance.
	 * 
	 * @return the object name of this {@code TriangleMesh3D} instance
	 */
//	TODO: Add Unit Tests!
	public String getObjectName() {
		return this.objectName;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code TriangleMesh3D} instance.
	 * 
	 * @return a {@code String} representation of this {@code TriangleMesh3D} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public String toString() {
		return String.format("new TriangleMesh3D(..., \"%s\", \"%s\", \"%s\", %s)", this.groupName, this.materialName, this.objectName, Boolean.toString(this.isUsingAccelerationStructure));
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
				if(!this.boundingVolume.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
				
				for(final Triangle3D triangle : this.triangles) {
					if(!triangle.accept(nodeHierarchicalVisitor)) {
						return nodeHierarchicalVisitor.visitLeave(this);
					}
				}
				
				if(this.bVHNode != null && !this.bVHNode.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
			}
			
			return nodeHierarchicalVisitor.visitLeave(this);
		} catch(final RuntimeException e) {
			throw new NodeTraversalException(e);
		}
	}
	
	/**
	 * Returns {@code true} if, and only if, {@code point} is contained in this {@code TriangleMesh3D} instance, {@code false} otherwise.
	 * <p>
	 * If {@code point} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param point a {@link Point3D} instance
	 * @return {@code true} if, and only if, {@code point} is contained in this {@code TriangleMesh3D} instance, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, {@code point} is {@code null}
	 */
//	TODO: Add Unit Tests!
	@Override
	public boolean contains(final Point3D point) {
		Objects.requireNonNull(point, "point == null");
		
		for(final Triangle3D triangle : this.triangles) {
			if(triangle.contains(point)) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Compares {@code object} to this {@code TriangleMesh3D} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code TriangleMesh3D}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code TriangleMesh3D} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code TriangleMesh3D}, and their respective values are equal, {@code false} otherwise
	 */
//	TODO: Add Unit Tests!
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof TriangleMesh3D)) {
			return false;
		} else if(!Objects.equals(this.bVHNode, TriangleMesh3D.class.cast(object).bVHNode)) {
			return false;
		} else if(!Objects.equals(this.boundingVolume, TriangleMesh3D.class.cast(object).boundingVolume)) {
			return false;
		} else if(!Objects.equals(this.triangles, TriangleMesh3D.class.cast(object).triangles)) {
			return false;
		} else if(!Objects.equals(this.groupName, TriangleMesh3D.class.cast(object).groupName)) {
			return false;
		} else if(!Objects.equals(this.materialName, TriangleMesh3D.class.cast(object).materialName)) {
			return false;
		} else if(!Objects.equals(this.objectName, TriangleMesh3D.class.cast(object).objectName)) {
			return false;
		} else if(this.isUsingAccelerationStructure != TriangleMesh3D.class.cast(object).isUsingAccelerationStructure) {
			return false;
		} else if(!Doubles.equals(this.surfaceArea, TriangleMesh3D.class.cast(object).surfaceArea)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Performs an intersection test between {@code surfaceIntersector} and this {@code TriangleMesh3D} instance.
	 * <p>
	 * Returns {@code true} if, and only if, {@code surfaceIntersector} intersects this {@code TriangleMesh3D} instance, {@code false} otherwise.
	 * <p>
	 * If {@code surfaceIntersector} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param surfaceIntersector a {@link SurfaceIntersector3D} instance
	 * @return {@code true} if, and only if, {@code surfaceIntersector} intersects this {@code TriangleMesh3D} instance, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, {@code surfaceIntersector} is {@code null}
	 */
//	TODO: Add Unit Tests!
	@Override
	public boolean intersection(final SurfaceIntersector3D surfaceIntersector) {
		return this.isUsingAccelerationStructure ? this.bVHNode.intersection(surfaceIntersector) : surfaceIntersector.intersection(this);
	}
	
	/**
	 * Returns {@code true} if, and only if, {@code ray} intersects this {@code TriangleMesh3D} instance, {@code false} otherwise.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3D} to perform an intersection test against this {@code TriangleMesh3D} instance
	 * @param tMinimum the minimum parametric distance
	 * @param tMaximum the maximum parametric distance
	 * @return {@code true} if, and only if, {@code ray} intersects this {@code TriangleMesh3D} instance, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
//	TODO: Add Unit Tests!
	@Override
	public boolean intersects(final Ray3D ray, final double tMinimum, final double tMaximum) {
		return this.isUsingAccelerationStructure ? this.bVHNode.intersects(ray, tMinimum, tMaximum) : !Doubles.isNaN(intersectionT(ray, tMinimum, tMaximum));
	}
	
	/**
	 * Returns the surface area of this {@code TriangleMesh3D} instance.
	 * 
	 * @return the surface area of this {@code TriangleMesh3D} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public double getSurfaceArea() {
		return this.surfaceArea;
	}
	
	/**
	 * Performs an intersection test between {@code ray} and this {@code TriangleMesh3D} instance.
	 * <p>
	 * Returns {@code t}, the parametric distance to the surface intersection point, or {@code Double.NaN} if no intersection exists.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3D} to perform an intersection test against this {@code TriangleMesh3D} instance
	 * @param tMinimum the minimum parametric distance
	 * @param tMaximum the maximum parametric distance
	 * @return {@code t}, the parametric distance to the surface intersection point, or {@code Double.NaN} if no intersection exists
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
//	TODO: Add Unit Tests!
	@Override
	public double intersectionT(final Ray3D ray, final double tMinimum, final double tMaximum) {
		if(this.isUsingAccelerationStructure) {
			return this.bVHNode.intersectionT(ray, tMinimum, tMaximum);
		}
		
		double t = Double.NaN;
		double tMax = tMaximum;
		double tMin = tMinimum;
		
		for(final Triangle3D triangle : this.triangles) {
			t = minOrNaN(t, triangle.intersectionT(ray, tMin, tMax));
			
			if(!Doubles.isNaN(t)) {
				tMax = t;
			}
		}
		
		return t;
	}
	
	/**
	 * Returns an {@code int} with the ID of this {@code TriangleMesh3D} instance.
	 * 
	 * @return an {@code int} with the ID of this {@code TriangleMesh3D} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public int getID() {
		return ID;
	}
	
	/**
	 * Returns a hash code for this {@code TriangleMesh3D} instance.
	 * 
	 * @return a hash code for this {@code TriangleMesh3D} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public int hashCode() {
		return Objects.hash(this.bVHNode, this.boundingVolume, this.triangles, this.groupName, this.materialName, this.objectName, Boolean.valueOf(this.isUsingAccelerationStructure), Double.valueOf(this.surfaceArea));
	}
	
	/**
	 * Writes this {@code TriangleMesh3D} instance to {@code dataOutput}.
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
			dataOutput.writeInt(this.triangles.size());
			
			for(final Triangle3D triangle : this.triangles) {
				triangle.write(dataOutput);
			}
			
			dataOutput.writeUTF(this.groupName);
			dataOutput.writeUTF(this.materialName);
			dataOutput.writeUTF(this.objectName);
			dataOutput.writeBoolean(this.isUsingAccelerationStructure);
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Reads a Wavefront Object file into a {@code List} of {@code TriangleMesh3D} instances.
	 * <p>
	 * Returns a {@code List} of {@code TriangleMesh3D} instances.
	 * <p>
	 * If {@code file} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O-error occurs, an {@code UncheckedIOException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * TriangleMesh3D.readWavefrontObject(file, false);
	 * }
	 * </pre>
	 * 
	 * @param file a {@code File} instance
	 * @return a {@code List} of {@code TriangleMesh3D} instances
	 * @throws NullPointerException thrown if, and only if, {@code file} is {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O-error occurs
	 */
//	TODO: Add Unit Tests!
	public static List<TriangleMesh3D> readWavefrontObject(final File file) {
		return readWavefrontObject(file, false);
	}
	
	/**
	 * Reads a Wavefront Object file into a {@code List} of {@code TriangleMesh3D} instances.
	 * <p>
	 * Returns a {@code List} of {@code TriangleMesh3D} instances.
	 * <p>
	 * If {@code file} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O-error occurs, an {@code UncheckedIOException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * TriangleMesh3D.readWavefrontObject(file, isFlippingTextureCoordinateY, 1.0D);
	 * }
	 * </pre>
	 * 
	 * @param file a {@code File} instance
	 * @param isFlippingTextureCoordinateY {@code true} if, and only if, the Y-coordinate of the texture coordinates should be flipped, {@code false} otherwise
	 * @return a {@code List} of {@code TriangleMesh3D} instances
	 * @throws NullPointerException thrown if, and only if, {@code file} is {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O-error occurs
	 */
//	TODO: Add Unit Tests!
	public static List<TriangleMesh3D> readWavefrontObject(final File file, final boolean isFlippingTextureCoordinateY) {
		return readWavefrontObject(file, isFlippingTextureCoordinateY, 1.0D);
	}
	
	/**
	 * Reads a Wavefront Object file into a {@code List} of {@code TriangleMesh3D} instances.
	 * <p>
	 * Returns a {@code List} of {@code TriangleMesh3D} instances.
	 * <p>
	 * If {@code file} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O-error occurs, an {@code UncheckedIOException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * TriangleMesh3D.readWavefrontObject(file, isFlippingTextureCoordinateY, scale, true);
	 * }
	 * </pre>
	 * 
	 * @param file a {@code File} instance
	 * @param isFlippingTextureCoordinateY {@code true} if, and only if, the Y-coordinate of the texture coordinates should be flipped, {@code false} otherwise
	 * @param scale the scale to apply to all {@link Triangle3D} instances
	 * @return a {@code List} of {@code TriangleMesh3D} instances
	 * @throws NullPointerException thrown if, and only if, {@code file} is {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O-error occurs
	 */
//	TODO: Add Unit Tests!
	public static List<TriangleMesh3D> readWavefrontObject(final File file, final boolean isFlippingTextureCoordinateY, final double scale) {
		return readWavefrontObject(file, isFlippingTextureCoordinateY, scale, true);
	}
	
	/**
	 * Reads a Wavefront Object file into a {@code List} of {@code TriangleMesh3D} instances.
	 * <p>
	 * Returns a {@code List} of {@code TriangleMesh3D} instances.
	 * <p>
	 * If {@code file} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O-error occurs, an {@code UncheckedIOException} will be thrown.
	 * 
	 * @param file a {@code File} instance
	 * @param isFlippingTextureCoordinateY {@code true} if, and only if, the Y-coordinate of the texture coordinates should be flipped, {@code false} otherwise
	 * @param scale the scale to apply to all {@link Triangle3D} instances
	 * @param isUsingAccelerationStructure {@code true} if, and only if, an acceleration structure should be used, {@code false} otherwise
	 * @return a {@code List} of {@code TriangleMesh3D} instances
	 * @throws NullPointerException thrown if, and only if, {@code file} is {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O-error occurs
	 */
//	TODO: Add Unit Tests!
	public static List<TriangleMesh3D> readWavefrontObject(final File file, final boolean isFlippingTextureCoordinateY, final double scale, final boolean isUsingAccelerationStructure) {
		try {
			System.out.printf("Loading triangle meshes from file '%s'...%n", file.getName());
			
			final DefaultObjectModel defaultObjectModel = DefaultObjectModel.parseDefaultObjectModel(Objects.requireNonNull(file, "file == null"), isFlippingTextureCoordinateY);
			
			final IndexedObjectModel indexedObjectModel = defaultObjectModel.toIndexedObjectModel();
			
			final List<Integer> indices = indexedObjectModel.getIndices();
			final List<Point2D> textureCoordinates = indexedObjectModel.getTextureCoordinates();
			final List<Point4D> positions = indexedObjectModel.getPositions();
			final List<String> groupNames = indexedObjectModel.getGroupNames();
			final List<String> materialNames = indexedObjectModel.getMaterialNames();
			final List<String> objectNames = indexedObjectModel.getObjectNames();
			final List<Vector3D> normals = indexedObjectModel.getNormals();
//			final List<Vector3D> tangents = indexedObjectModel.getTangents();
			final List<Triangle3D> triangles = new ArrayList<>();
			final List<TriangleMesh3D> triangleMeshes = new ArrayList<>();
			
			String previousGroupName = "";
			String previousMaterialName = "";
			String previousObjectName = "";
			
			final boolean isScaling = !Doubles.equals(scale, 1.0D);
			
			final int maximumCount = Integer.MAX_VALUE;
			
			final Matrix44D matrix = isScaling ? Matrix44D.scale(scale) : null;
			
			if(matrix != null && !matrix.isInvertible()) {
				return new ArrayList<>();
			}
			
			final Matrix44D matrixInverse = isScaling ? Matrix44D.inverse(matrix) : null;
			
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
						
						triangleMeshes.add(new TriangleMesh3D(triangles, previousGroupName, previousMaterialName, previousObjectName, isUsingAccelerationStructure));
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
				
				final Point2D textureCoordinatesA = textureCoordinates.get(indexA);
				final Point2D textureCoordinatesB = textureCoordinates.get(indexB);
				final Point2D textureCoordinatesC = textureCoordinates.get(indexC);
				
				final Point4D positionA = isScaling ? Point4D.transformAndDivide(matrix, positions.get(indexA)) : positions.get(indexA);
				final Point4D positionB = isScaling ? Point4D.transformAndDivide(matrix, positions.get(indexB)) : positions.get(indexB);
				final Point4D positionC = isScaling ? Point4D.transformAndDivide(matrix, positions.get(indexC)) : positions.get(indexC);
				
				final Vector3D normalA = isScaling ? Vector3D.transformTranspose(matrixInverse, normals.get(indexA)) : normals.get(indexA);
				final Vector3D normalB = isScaling ? Vector3D.transformTranspose(matrixInverse, normals.get(indexB)) : normals.get(indexB);
				final Vector3D normalC = isScaling ? Vector3D.transformTranspose(matrixInverse, normals.get(indexC)) : normals.get(indexC);
				
//				final Vector3D tangentA = isScaling ? Vector3D.transformTranspose(matrixInverse, tangents.get(indexA)) : tangents.get(indexA);
//				final Vector3D tangentB = isScaling ? Vector3D.transformTranspose(matrixInverse, tangents.get(indexB)) : tangents.get(indexB);
//				final Vector3D tangentC = isScaling ? Vector3D.transformTranspose(matrixInverse, tangents.get(indexC)) : tangents.get(indexC);
				
				final Vertex3D a = new Vertex3D(textureCoordinatesA, positionA, normalA);
				final Vertex3D b = new Vertex3D(textureCoordinatesB, positionB, normalB);
				final Vertex3D c = new Vertex3D(textureCoordinatesC, positionC, normalC);
				
				final Triangle3D triangle = new Triangle3D(a, b, c);
				
				triangles.add(triangle);
			}
			
			if(triangles.size() > 0) {
				System.out.printf(" - Creating triangle mesh with group name '%s', material name '%s' and object name '%s'.%n", previousGroupName, previousMaterialName, previousObjectName);
				
				triangleMeshes.add(new TriangleMesh3D(triangles, previousGroupName, previousMaterialName, previousObjectName, isUsingAccelerationStructure));
				triangles.clear();
			}
			
			System.out.println(" - Done.");
			
			return triangleMeshes;
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	/**
	 * Reads a Wavefront Object file into a {@code List} of {@code TriangleMesh3D} instances.
	 * <p>
	 * Returns a {@code List} of {@code TriangleMesh3D} instances.
	 * <p>
	 * If {@code pathname} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O-error occurs, an {@code UncheckedIOException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * TriangleMesh3D.readWavefrontObject(pathname, false);
	 * }
	 * </pre>
	 * 
	 * @param pathname a {@code String} instance with the pathname to a file
	 * @return a {@code List} of {@code TriangleMesh3D} instances
	 * @throws NullPointerException thrown if, and only if, {@code pathname} is {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O-error occurs
	 */
//	TODO: Add Unit Tests!
	public static List<TriangleMesh3D> readWavefrontObject(final String pathname) {
		return readWavefrontObject(pathname, false);
	}
	
	/**
	 * Reads a Wavefront Object file into a {@code List} of {@code TriangleMesh3D} instances.
	 * <p>
	 * Returns a {@code List} of {@code TriangleMesh3D} instances.
	 * <p>
	 * If {@code pathname} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O-error occurs, an {@code UncheckedIOException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * TriangleMesh3D.readWavefrontObject(pathname, isFlippingTextureCoordinateY, 1.0D);
	 * }
	 * </pre>
	 * 
	 * @param pathname a {@code String} instance with the pathname to a file
	 * @param isFlippingTextureCoordinateY {@code true} if, and only if, the Y-coordinate of the texture coordinates should be flipped, {@code false} otherwise
	 * @return a {@code List} of {@code TriangleMesh3D} instances
	 * @throws NullPointerException thrown if, and only if, {@code pathname} is {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O-error occurs
	 */
//	TODO: Add Unit Tests!
	public static List<TriangleMesh3D> readWavefrontObject(final String pathname, final boolean isFlippingTextureCoordinateY) {
		return readWavefrontObject(pathname, isFlippingTextureCoordinateY, 1.0D);
	}
	
	/**
	 * Reads a Wavefront Object file into a {@code List} of {@code TriangleMesh3D} instances.
	 * <p>
	 * Returns a {@code List} of {@code TriangleMesh3D} instances.
	 * <p>
	 * If {@code pathname} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O-error occurs, an {@code UncheckedIOException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * TriangleMesh3D.readWavefrontObject(pathname, isFlippingTextureCoordinateY, scale, true);
	 * }
	 * </pre>
	 * 
	 * @param pathname a {@code String} instance with the pathname to a file
	 * @param isFlippingTextureCoordinateY {@code true} if, and only if, the Y-coordinate of the texture coordinates should be flipped, {@code false} otherwise
	 * @param scale the scale to apply to all {@link Triangle3D} instances
	 * @return a {@code List} of {@code TriangleMesh3D} instances
	 * @throws NullPointerException thrown if, and only if, {@code pathname} is {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O-error occurs
	 */
//	TODO: Add Unit Tests!
	public static List<TriangleMesh3D> readWavefrontObject(final String pathname, final boolean isFlippingTextureCoordinateY, final double scale) {
		return readWavefrontObject(pathname, isFlippingTextureCoordinateY, scale, true);
	}
	
	/**
	 * Reads a Wavefront Object file into a {@code List} of {@code TriangleMesh3D} instances.
	 * <p>
	 * Returns a {@code List} of {@code TriangleMesh3D} instances.
	 * <p>
	 * If {@code pathname} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O-error occurs, an {@code UncheckedIOException} will be thrown.
	 * 
	 * @param pathname a {@code String} instance with the pathname to a file
	 * @param isFlippingTextureCoordinateY {@code true} if, and only if, the Y-coordinate of the texture coordinates should be flipped, {@code false} otherwise
	 * @param scale the scale to apply to all {@link Triangle3D} instances
	 * @param isUsingAccelerationStructure {@code true} if, and only if, an acceleration structure should be used, {@code false} otherwise
	 * @return a {@code List} of {@code TriangleMesh3D} instances
	 * @throws NullPointerException thrown if, and only if, {@code pathname} is {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O-error occurs
	 */
//	TODO: Add Unit Tests!
	public static List<TriangleMesh3D> readWavefrontObject(final String pathname, final boolean isFlippingTextureCoordinateY, final double scale, final boolean isUsingAccelerationStructure) {
		return readWavefrontObject(new File(Objects.requireNonNull(pathname, "pathname == null")), isFlippingTextureCoordinateY, scale, isUsingAccelerationStructure);
	}
	
	/**
	 * Reads a Geo file into a {@code TriangleMesh3D} instance.
	 * <p>
	 * Returns a {@code TriangleMesh3D} instance.
	 * <p>
	 * If {@code file} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O-error occurs, an {@code UncheckedIOException} will be thrown.
	 * <p>
	 * If the {@code double} values cannot be parsed, a {@code NumberFormatException} will be thrown.
	 * 
	 * @param file a {@code File} instance
	 * @return a {@code TriangleMesh3D} instance
	 * @throws NullPointerException thrown if, and only if, {@code file} is {@code null}
	 * @throws NumberFormatException thrown if, and only if, the {@code double} values cannot be parsed
	 * @throws UncheckedIOException thrown if, and only if, an I/O-error occurs
	 */
//	TODO: Add Unit Tests!
	public static TriangleMesh3D readGeo(final File file) {
		try(final Scanner scanner = new Scanner(Objects.requireNonNull(file, "file == null"))) {
			final int[] faceIndices = new int[scanner.nextInt()];
			
			int vertexIndexCount = 0;
			
			for(int i = 0; i < faceIndices.length; i++) {
				faceIndices[i] = scanner.nextInt();
				
				vertexIndexCount += faceIndices[i];
			}
			
			final int[] vertexIndices = new int[vertexIndexCount];
			
			int vertexCount = 0;
			
			for(int i = 0; i < vertexIndices.length; i++) {
				vertexIndices[i] = scanner.nextInt();
				
				if(vertexIndices[i] > vertexCount) {
					vertexCount = vertexIndices[i];
				}
			}
			
			vertexCount++;
			
			final Point4D[] positions = new Point4D[vertexCount];
			
			for(int i = 0; i < positions.length; i++) {
				positions[i] = new Point4D(Double.parseDouble(scanner.next()), Double.parseDouble(scanner.next()), Double.parseDouble(scanner.next()));
			}
			
			final Vector3D[] normals = new Vector3D[vertexIndexCount];
			
			for(int i = 0; i < normals.length; i++) {
				normals[i] = new Vector3D(Double.parseDouble(scanner.next()), Double.parseDouble(scanner.next()), Double.parseDouble(scanner.next()));
			}
			
			final Point2D[] textureCoordinates = new Point2D[vertexIndexCount];
			
			for(int i = 0; i < textureCoordinates.length; i++) {
				textureCoordinates[i] = new Point2D(Double.parseDouble(scanner.next()), Double.parseDouble(scanner.next()));
			}
			
			final List<Triangle3D> triangles = new ArrayList<>();
			
			for(int i = 0, j = 0; i < faceIndices.length; j += faceIndices[i], i++) {
				for(int k = 0; k < faceIndices[i] - 2; k++) {
					final int indexA = j;
					final int indexB = j + k + 1;
					final int indexC = j + k + 2;
					
					final Point4D positionA = positions[vertexIndices[indexA]];
					final Point4D positionB = positions[vertexIndices[indexB]];
					final Point4D positionC = positions[vertexIndices[indexC]];
					
					final Point2D textureCoordinatesA = textureCoordinates[indexA];
					final Point2D textureCoordinatesB = textureCoordinates[indexB];
					final Point2D textureCoordinatesC = textureCoordinates[indexC];
					
					final Vector3D normalA = Vector3D.normalize(normals[indexA]);
					final Vector3D normalB = Vector3D.normalize(normals[indexB]);
					final Vector3D normalC = Vector3D.normalize(normals[indexC]);
					
					final Vertex3D vertexA = new Vertex3D(textureCoordinatesA, positionA, normalA);
					final Vertex3D vertexB = new Vertex3D(textureCoordinatesB, positionB, normalB);
					final Vertex3D vertexC = new Vertex3D(textureCoordinatesC, positionC, normalC);
					
					triangles.add(new Triangle3D(vertexA, vertexB, vertexC));
				}
			}
			
			return new TriangleMesh3D(triangles, "", "", "");
		} catch(final FileNotFoundException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	/**
	 * Reads a Geo file into a {@code TriangleMesh3D} instance.
	 * <p>
	 * Returns a {@code TriangleMesh3D} instance.
	 * <p>
	 * If {@code pathname} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O-error occurs, an {@code UncheckedIOException} will be thrown.
	 * <p>
	 * If the {@code double} values cannot be parsed, a {@code NumberFormatException} will be thrown.
	 * 
	 * @param pathname a {@code String} instance with the pathname to a file
	 * @return a {@code TriangleMesh3D} instance
	 * @throws NullPointerException thrown if, and only if, {@code pathname} is {@code null}
	 * @throws NumberFormatException thrown if, and only if, the {@code double} values cannot be parsed
	 * @throws UncheckedIOException thrown if, and only if, an I/O-error occurs
	 */
//	TODO: Add Unit Tests!
	public static TriangleMesh3D readGeo(final String pathname) {
		return readGeo(new File(Objects.requireNonNull(pathname, "pathname == null")));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static BVHNode3D doCreateBVHNode(final List<Triangle3D> triangles) {
		final List<BVHItem3D<Triangle3D>> processableBVHItems = new ArrayList<>(triangles.size());
		
		double maximumX = Doubles.MIN_VALUE;
		double maximumY = Doubles.MIN_VALUE;
		double maximumZ = Doubles.MIN_VALUE;
		double minimumX = Doubles.MAX_VALUE;
		double minimumY = Doubles.MAX_VALUE;
		double minimumZ = Doubles.MAX_VALUE;
		
		for(final Triangle3D triangle : triangles) {
			final Point3D a = new Point3D(triangle.getA().getPosition());
			final Point3D b = new Point3D(triangle.getB().getPosition());
			final Point3D c = new Point3D(triangle.getC().getPosition());
			
			final Point3D maximum = Point3D.maximum(a, b, c);
			final Point3D minimum = Point3D.minimum(a, b, c);
			
			maximumX = Doubles.max(maximumX, maximum.x);
			maximumY = Doubles.max(maximumY, maximum.y);
			maximumZ = Doubles.max(maximumZ, maximum.z);
			minimumX = Doubles.min(minimumX, minimum.x);
			minimumY = Doubles.min(minimumY, minimum.y);
			minimumZ = Doubles.min(minimumZ, minimum.z);
			
			processableBVHItems.add(new BVHItem3D<>(new AxisAlignedBoundingBox3D(maximum, minimum), triangle));
		}
		
		return BVHNode3Ds.create(processableBVHItems, new Point3D(maximumX, maximumY, maximumZ), new Point3D(minimumX, minimumY, minimumZ), 0);
	}
	
	private static BoundingVolume3D doCreateBoundingVolume(final List<Triangle3D> triangles) {
		Point3D maximum = Point3D.MINIMUM;
		Point3D minimum = Point3D.MAXIMUM;
		
		for(final Triangle3D triangle : triangles) {
			final Point3D a = new Point3D(triangle.getA().getPosition());
			final Point3D b = new Point3D(triangle.getB().getPosition());
			final Point3D c = new Point3D(triangle.getC().getPosition());
			
			maximum = Point3D.maximum(maximum, Point3D.maximum(a, b, c));
			minimum = Point3D.minimum(minimum, Point3D.minimum(a, b, c));
		}
		
		return new AxisAlignedBoundingBox3D(maximum, minimum);
	}
	
	private static double doCalculateSurfaceArea(final List<Triangle3D> triangles) {
		double surfaceArea = 0.0D;
		
		for(final Triangle3D triangle : triangles) {
			surfaceArea += triangle.getSurfaceArea();
		}
		
		return surfaceArea;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static final class DefaultObjectModel {
		private final List<Point2D> textureCoordinates;
		private final List<Point4D> positions;
		private final List<String> groupNames;
		private final List<String> materialNames;
		private final List<String> objectNames;
		private final List<Vector3D> normals;
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
				
				final Point2D textureCoordinates = vertex.hasTextureVertexIndex() ? this.textureCoordinates.get(vertex.getTextureVertexIndex()) : new Point2D();
				
				final Point4D position = vertex.hasGeometricVertexIndex() ? this.positions.get(vertex.getGeometricVertexIndex()) : new Point4D();
				
				final Vector3D normal = vertex.hasVertexNormalIndex() ? this.normals.get(vertex.getVertexNormalIndex()) : new Vector3D();
				
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
//					indexedObjectModel1.addTangent(new Vector3D());
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
			
//			indexedObjectModel1.calculateTangents();
			
//			for(int i = 0; i < indexedObjectModel0.getPositionCount(); i++) {
//				indexedObjectModel0.addTangent(indexedObjectModel1.getTangent(normalModelIndices1.get(Integer.valueOf(i)).intValue()));
//			}
			
			return indexedObjectModel0;
		}
		
		public void addGroupName(final String groupName) {
			this.groupNames.add(Objects.requireNonNull(groupName, "groupName == null"));
		}
		
		public void addMaterialName(final String materialName) {
			this.materialNames.add(Objects.requireNonNull(materialName, "materialName == null"));
		}
		
		public void addNormal(final Vector3D normal) {
			this.normals.add(Objects.requireNonNull(normal, "normal == null"));
		}
		
		public void addObjectName(final String objectName) {
			this.objectNames.add(Objects.requireNonNull(objectName, "objectName == null"));
		}
		
		public void addPosition(final Point4D position) {
			this.positions.add(Objects.requireNonNull(position, "position == null"));
		}
		
		public void addTextureCoordinates(final Point2D textureCoordinates) {
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
								defaultObjectModel.addPosition(new Point4D(Double.parseDouble(elements[1]), Double.parseDouble(elements[2]), Double.parseDouble(elements[3])));
								
								break;
							case "vn":
								defaultObjectModel.addNormal(new Vector3D(Double.parseDouble(elements[1]), Double.parseDouble(elements[2]), Double.parseDouble(elements[3])));
								
								break;
							case "vt":
								defaultObjectModel.addTextureCoordinates(new Point2D(Double.parseDouble(elements[1]), isFlippingTextureCoordinateY ? 1.0D - Double.parseDouble(elements[2]) : Double.parseDouble(elements[2])));
								
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
		private final List<Point2D> textureCoordinates;
		private final List<Point4D> positions;
		private final List<String> groupNames;
		private final List<String> materialNames;
		private final List<String> objectNames;
		private final List<Vector3D> normals;
//		private final List<Vector3D> tangents;
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		public IndexedObjectModel() {
			this.indices = new ArrayList<>();
			this.textureCoordinates = new ArrayList<>();
			this.positions = new ArrayList<>();
			this.groupNames = new ArrayList<>();
			this.materialNames = new ArrayList<>();
			this.objectNames = new ArrayList<>();
			this.normals = new ArrayList<>();
//			this.tangents = new ArrayList<>();
		}
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		public List<Integer> getIndices() {
			return this.indices;
		}
		
		public List<Point2D> getTextureCoordinates() {
			return this.textureCoordinates;
		}
		
		public List<Point4D> getPositions() {
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
		
		public List<Vector3D> getNormals() {
			return this.normals;
		}
		
//		public List<Vector3D> getTangents() {
//			return this.tangents;
//		}
		
		public Vector3D getNormal(final int index) {
			return this.normals.get(index);
		}
		
//		public Vector3D getTangent(final int index) {
//			return this.tangents.get(index);
//		}
		
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
		
		public void addNormal(final Vector3D normal) {
			this.normals.add(Objects.requireNonNull(normal, "normal == null"));
		}
		
		public void addObjectName(final String objectName) {
			this.objectNames.add(Objects.requireNonNull(objectName, "objectName == null"));
		}
		
		public void addPosition(final Point4D position) {
			this.positions.add(Objects.requireNonNull(position, "position == null"));
		}
		
//		public void addTangent(final Vector3D tangent) {
//			this.tangents.add(Objects.requireNonNull(tangent, "tangent == null"));
//		}
		
		public void addTextureCoordinates(final Point2D textureCoordinates) {
			this.textureCoordinates.add(Objects.requireNonNull(textureCoordinates, "textureCoordinates == null"));
		}
		
		public void calculateNormals() {
			for(int i = 0; i < this.indices.size(); i += 3) {
				final int indexA = this.indices.get(i + 0).intValue();
				final int indexB = this.indices.get(i + 1).intValue();
				final int indexC = this.indices.get(i + 2).intValue();
				
				final Vector3D edgeAB = Vector3D.direction(this.positions.get(indexA), this.positions.get(indexB));
				final Vector3D edgeAC = Vector3D.direction(this.positions.get(indexA), this.positions.get(indexC));
				final Vector3D normal = Vector3D.normalize(Vector3D.crossProduct(edgeAB, edgeAC));
				
				this.normals.set(indexA, Vector3D.add(this.normals.get(indexA), normal));
				this.normals.set(indexB, Vector3D.add(this.normals.get(indexB), normal));
				this.normals.set(indexC, Vector3D.add(this.normals.get(indexC), normal));
			}
			
			for(int i = 0; i < this.normals.size(); i++) {
				this.normals.set(i, Vector3D.normalize(this.normals.get(i)));
			}
		}
		
		/*
		public void calculateTangents() {
			for(int i = 0; i < this.indices.size(); i += 3) {
				final int indexA = this.indices.get(i + 0).intValue();
				final int indexB = this.indices.get(i + 1).intValue();
				final int indexC = this.indices.get(i + 2).intValue();
				
				final Vector3D edgeAB = Vector3D.direction(this.positions.get(indexA), this.positions.get(indexB));
				final Vector3D edgeAC = Vector3D.direction(this.positions.get(indexA), this.positions.get(indexC));
				
				final double deltaABU = this.textureCoordinates.get(indexB).getX() - this.textureCoordinates.get(indexA).getX();
				final double deltaABV = this.textureCoordinates.get(indexB).getY() - this.textureCoordinates.get(indexA).getY();
				final double deltaACU = this.textureCoordinates.get(indexC).getX() - this.textureCoordinates.get(indexA).getX();
				final double deltaACV = this.textureCoordinates.get(indexC).getY() - this.textureCoordinates.get(indexA).getY();
				
				final double dividend = (deltaABU * deltaACV - deltaACU * deltaABV);
				final double fraction = dividend < -0.0D || dividend > +0.0D ? 1.0D / dividend : 0.0D;
				
				final double x = fraction * (deltaACV * edgeAB.getX() - deltaABV * edgeAC.getX());
				final double y = fraction * (deltaACV * edgeAB.getY() - deltaABV * edgeAC.getY());
				final double z = fraction * (deltaACV * edgeAB.getZ() - deltaABV * edgeAC.getZ());
				
				final Vector3D tangent = new Vector3D(x, y, z);
				
				this.tangents.set(indexA, Vector3D.add(this.tangents.get(indexA), tangent));
				this.tangents.set(indexB, Vector3D.add(this.tangents.get(indexB), tangent));
				this.tangents.set(indexC, Vector3D.add(this.tangents.get(indexC), tangent));
			}
			
			for(int i = 0; i < this.tangents.size(); i++) {
				this.tangents.set(i, Vector3D.normalize(this.tangents.get(i)));
			}
		}
		*/
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