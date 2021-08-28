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
package org.dayflower.simplex;

import static org.dayflower.simplex.OrthonormalBasis.orthonormalBasis33D;
import static org.dayflower.simplex.OrthonormalBasis.orthonormalBasis33DFromW;
import static org.dayflower.simplex.OrthonormalBasis.orthonormalBasis33DFromWV;
import static org.dayflower.simplex.OrthonormalBasis.orthonormalBasis33DGetW;
import static org.dayflower.simplex.Point.point2D;
import static org.dayflower.simplex.Point.point2DFromBarycentricCoordinates;
import static org.dayflower.simplex.Point.point2DSet;
import static org.dayflower.simplex.Point.point2DSphericalCoordinates;
import static org.dayflower.simplex.Point.point3D;
import static org.dayflower.simplex.Point.point3DAdd;
import static org.dayflower.simplex.Point.point3DCoplanar;
import static org.dayflower.simplex.Point.point3DDistanceSquared;
import static org.dayflower.simplex.Point.point3DGetX;
import static org.dayflower.simplex.Point.point3DGetY;
import static org.dayflower.simplex.Point.point3DGetZ;
import static org.dayflower.simplex.Point.point3DMaximum;
import static org.dayflower.simplex.Point.point3DMidpoint;
import static org.dayflower.simplex.Point.point3DMinimum;
import static org.dayflower.simplex.Point.point3DSet;
import static org.dayflower.simplex.Ray.ray3DGetDirection;
import static org.dayflower.simplex.Ray.ray3DGetOrigin;
import static org.dayflower.simplex.Ray.ray3DGetTMaximum;
import static org.dayflower.simplex.Ray.ray3DGetTMinimum;
import static org.dayflower.simplex.Vector.vector2DCrossProduct;
import static org.dayflower.simplex.Vector.vector2DDirection;
import static org.dayflower.simplex.Vector.vector2DDirectionXY;
import static org.dayflower.simplex.Vector.vector2DDirectionYZ;
import static org.dayflower.simplex.Vector.vector2DDirectionZX;
import static org.dayflower.simplex.Vector.vector2DGetX;
import static org.dayflower.simplex.Vector.vector2DGetY;
import static org.dayflower.simplex.Vector.vector2DSubtract;
import static org.dayflower.simplex.Vector.vector3D;
import static org.dayflower.simplex.Vector.vector3DCrossProduct;
import static org.dayflower.simplex.Vector.vector3DDirection;
import static org.dayflower.simplex.Vector.vector3DDirectionNormalized;
import static org.dayflower.simplex.Vector.vector3DDotProduct;
import static org.dayflower.simplex.Vector.vector3DFromBarycentricCoordinatesNormalized;
import static org.dayflower.simplex.Vector.vector3DFromPoint3D;
import static org.dayflower.simplex.Vector.vector3DGetX;
import static org.dayflower.simplex.Vector.vector3DGetY;
import static org.dayflower.simplex.Vector.vector3DGetZ;
import static org.dayflower.simplex.Vector.vector3DLength;
import static org.dayflower.simplex.Vector.vector3DLengthSquared;
import static org.dayflower.simplex.Vector.vector3DMultiply;
import static org.dayflower.simplex.Vector.vector3DNormalize;
import static org.dayflower.simplex.Vector.vector3DReciprocal;
import static org.dayflower.simplex.Vector.vector3DSet;
import static org.dayflower.utility.Doubles.NaN;
import static org.dayflower.utility.Doubles.PI_DIVIDED_BY_2;
import static org.dayflower.utility.Doubles.PI_MULTIPLIED_BY_2;
import static org.dayflower.utility.Doubles.PI_MULTIPLIED_BY_2_RECIPROCAL;
import static org.dayflower.utility.Doubles.PI_MULTIPLIED_BY_4;
import static org.dayflower.utility.Doubles.PI_RECIPROCAL;
import static org.dayflower.utility.Doubles.abs;
import static org.dayflower.utility.Doubles.asin;
import static org.dayflower.utility.Doubles.atan2;
import static org.dayflower.utility.Doubles.getOrAdd;
import static org.dayflower.utility.Doubles.isNaN;
import static org.dayflower.utility.Doubles.isZero;
import static org.dayflower.utility.Doubles.max;
import static org.dayflower.utility.Doubles.min;
import static org.dayflower.utility.Doubles.normalize;
import static org.dayflower.utility.Doubles.pow;
import static org.dayflower.utility.Doubles.saturate;
import static org.dayflower.utility.Doubles.solveQuadraticSystem;
import static org.dayflower.utility.Doubles.solveQuartic;
import static org.dayflower.utility.Doubles.sqrt;
import static org.dayflower.utility.Doubles.toRadians;
import static org.dayflower.utility.Ints.toInt;

import java.lang.reflect.Field;//TODO: Add Javadocs!

//TODO: Add Javadocs!
public final class Shape {
	/**
	 * The ID of a cone.
	 */
	public static final int CONE_ID = 0;
	
	/**
	 * The relative offset for the ID in a {@code double[]} that contains a cone.
	 */
	public static final int CONE_OFFSET_ID = 0;
	
	/**
	 * The relative offset for the maximum phi angle in a {@code double[]} that contains a cone.
	 */
	public static final int CONE_OFFSET_PHI_MAX = 2;
	
	/**
	 * The relative offset for the radius in a {@code double[]} that contains a cone.
	 */
	public static final int CONE_OFFSET_RADIUS = 3;
	
	/**
	 * The relative offset for the size in a {@code double[]} that contains a cone.
	 */
	public static final int CONE_OFFSET_SIZE = 1;
	
	/**
	 * The relative offset for the maximum Z-value in a {@code double[]} that contains a cone.
	 */
	public static final int CONE_OFFSET_Z_MAX = 4;
	
	/**
	 * The size of a cone.
	 */
	public static final int CONE_SIZE = 5;
	
	/**
	 * The ID of a cylinder.
	 */
	public static final int CYLINDER_ID = 1;
	
	/**
	 * The relative offset for the ID in a {@code double[]} that contains a cylinder.
	 */
	public static final int CYLINDER_OFFSET_ID = 0;
	
	/**
	 * The relative offset for the maximum phi angle in a {@code double[]} that contains a cylinder.
	 */
	public static final int CYLINDER_OFFSET_PHI_MAX = 2;
	
	/**
	 * The relative offset for the radius in a {@code double[]} that contains a cylinder.
	 */
	public static final int CYLINDER_OFFSET_RADIUS = 3;
	
	/**
	 * The relative offset for the size in a {@code double[]} that contains a cylinder.
	 */
	public static final int CYLINDER_OFFSET_SIZE = 1;
	
	/**
	 * The relative offset for the maximum Z-value in a {@code double[]} that contains a cylinder.
	 */
	public static final int CYLINDER_OFFSET_Z_MAX = 4;
	
	/**
	 * The relative offset for the minimum Z-value in a {@code double[]} that contains a cylinder.
	 */
	public static final int CYLINDER_OFFSET_Z_MIN = 5;
	
	/**
	 * The size of a cylinder.
	 */
	public static final int CYLINDER_SIZE = 6;
	
	/**
	 * The ID of a disk.
	 */
	public static final int DISK_ID = 2;
	
	/**
	 * The relative offset for the ID in a {@code double[]} that contains a disk.
	 */
	public static final int DISK_OFFSET_ID = 0;
	
	/**
	 * The relative offset for the maximum phi angle in a {@code double[]} that contains a disk.
	 */
	public static final int DISK_OFFSET_PHI_MAX = 2;
	
	/**
	 * The relative offset for the inner radius in a {@code double[]} that contains a disk.
	 */
	public static final int DISK_OFFSET_RADIUS_INNER = 3;
	
	/**
	 * The relative offset for the outer radius in a {@code double[]} that contains a disk.
	 */
	public static final int DISK_OFFSET_RADIUS_OUTER = 4;
	
	/**
	 * The relative offset for the size in a {@code double[]} that contains a disk.
	 */
	public static final int DISK_OFFSET_SIZE = 1;
	
	/**
	 * The relative offset for the maximum Z-value in a {@code double[]} that contains a disk.
	 */
	public static final int DISK_OFFSET_Z_MAX = 5;
	
	/**
	 * The size of a disk.
	 */
	public static final int DISK_SIZE = 6;
	
	/**
	 * The ID of a hyperboloid.
	 */
	public static final int HYPERBOLOID_ID = 3;
	
	/**
	 * The ID of a paraboloid.
	 */
	public static final int PARABOLOID_ID = 4;
	
	/**
	 * The relative offset for the ID in a {@code double[]} that contains a paraboloid.
	 */
	public static final int PARABOLOID_OFFSET_ID = 0;
	
	/**
	 * The relative offset for the maximum phi angle in a {@code double[]} that contains a paraboloid.
	 */
	public static final int PARABOLOID_OFFSET_PHI_MAX = 2;
	
	/**
	 * The relative offset for the radius in a {@code double[]} that contains a paraboloid.
	 */
	public static final int PARABOLOID_OFFSET_RADIUS = 3;
	
	/**
	 * The relative offset for the size in a {@code double[]} that contains a paraboloid.
	 */
	public static final int PARABOLOID_OFFSET_SIZE = 1;
	
	/**
	 * The relative offset for the maximum Z-value in a {@code double[]} that contains a paraboloid.
	 */
	public static final int PARABOLOID_OFFSET_Z_MAX = 4;
	
	/**
	 * The relative offset for the minimum Z-value in a {@code double[]} that contains a paraboloid.
	 */
	public static final int PARABOLOID_OFFSET_Z_MIN = 5;
	
	/**
	 * The size of a paraboloid.
	 */
	public static final int PARABOLOID_SIZE = 6;
	
	/**
	 * The ID of a plane.
	 */
	public static final int PLANE_ID = 5;
	
	/**
	 * The relative offset for the point denoted by A in a {@code double[]} that contains a plane.
	 */
	public static final int PLANE_OFFSET_A = 2;
	
	/**
	 * The relative offset for the point denoted by B in a {@code double[]} that contains a plane.
	 */
	public static final int PLANE_OFFSET_B = 5;
	
	/**
	 * The relative offset for the point denoted by C in a {@code double[]} that contains a plane.
	 */
	public static final int PLANE_OFFSET_C = 8;
	
	/**
	 * The relative offset for the ID in a {@code double[]} that contains a plane.
	 */
	public static final int PLANE_OFFSET_ID = 0;
	
	/**
	 * The relative offset for the size in a {@code double[]} that contains a plane.
	 */
	public static final int PLANE_OFFSET_SIZE = 1;
	
	/**
	 * The size of a plane.
	 */
	public static final int PLANE_SIZE = 11;
	
	/**
	 * The ID of a rectangle.
	 */
	public static final int RECTANGLE_ID = 7;
	
	/**
	 * The relative offset for the point denoted by A in a {@code double[]} that contains a rectangle.
	 */
	public static final int RECTANGLE_OFFSET_A = 2;
	
	/**
	 * The relative offset for the point denoted by B in a {@code double[]} that contains a rectangle.
	 */
	public static final int RECTANGLE_OFFSET_B = 5;
	
	/**
	 * The relative offset for the point denoted by C in a {@code double[]} that contains a rectangle.
	 */
	public static final int RECTANGLE_OFFSET_C = 8;
	
	/**
	 * The relative offset for the point denoted by C in a {@code double[]} that contains a rectangle.
	 */
	public static final int RECTANGLE_OFFSET_D = 11;
	
	/**
	 * The relative offset for the ID in a {@code double[]} that contains a rectangle.
	 */
	public static final int RECTANGLE_OFFSET_ID = 0;
	
	/**
	 * The relative offset for the size in a {@code double[]} that contains a rectangle.
	 */
	public static final int RECTANGLE_OFFSET_SIZE = 1;
	
	/**
	 * The size of a rectangle.
	 */
	public static final int RECTANGLE_SIZE = 14;
	
	/**
	 * The ID of a rectangular cuboid.
	 */
	public static final int RECTANGULAR_CUBOID_ID = 8;
	
	/**
	 * The relative offset for the ID in a {@code double[]} that contains a rectangular cuboid.
	 */
	public static final int RECTANGULAR_CUBOID_OFFSET_ID = 0;
	
	/**
	 * The relative offset for the point denoted by Maximum in a {@code double[]} that contains a rectangular cuboid.
	 */
	public static final int RECTANGULAR_CUBOID_OFFSET_MAXIMUM = 2;
	
	/**
	 * The relative offset for the point denoted by Minimum in a {@code double[]} that contains a rectangular cuboid.
	 */
	public static final int RECTANGULAR_CUBOID_OFFSET_MINIMUM = 5;
	
	/**
	 * The relative offset for the size in a {@code double[]} that contains a rectangular cuboid.
	 */
	public static final int RECTANGULAR_CUBOID_OFFSET_SIZE = 1;
	
	/**
	 * The size of a rectangular cuboid.
	 */
	public static final int RECTANGULAR_CUBOID_SIZE = 8;
	
	/**
	 * The relative offset for the ID in a {@code double[]} that contains a shape.
	 */
	public static final int SHAPE_OFFSET_ID = 0;
	
	/**
	 * The relative offset for the size in a {@code double[]} that contains a shape.
	 */
	public static final int SHAPE_OFFSET_SIZE = 1;
	
	/**
	 * The ID of a sphere.
	 */
	public static final int SPHERE_ID = 9;
	
	/**
	 * The relative offset for the point denoted by Center in a {@code double[]} that contains a sphere.
	 */
	public static final int SPHERE_OFFSET_CENTER = 2;
	
	/**
	 * The relative offset for the ID in a {@code double[]} that contains a sphere.
	 */
	public static final int SPHERE_OFFSET_ID = 0;
	
	/**
	 * The relative offset for the radius in a {@code double[]} that contains a sphere.
	 */
	public static final int SPHERE_OFFSET_RADIUS = 5;
	
	/**
	 * The relative offset for the size in a {@code double[]} that contains a sphere.
	 */
	public static final int SPHERE_OFFSET_SIZE = 1;
	
	/**
	 * The size of a sphere.
	 */
	public static final int SPHERE_SIZE = 6;
	
	/**
	 * The ID of a torus.
	 */
	public static final int TORUS_ID = 10;
	
	/**
	 * The relative offset for the ID in a {@code double[]} that contains a torus.
	 */
	public static final int TORUS_OFFSET_ID = 0;
	
	/**
	 * The relative offset for the inner radius in a {@code double[]} that contains a torus.
	 */
	public static final int TORUS_OFFSET_RADIUS_INNER = 2;
	
	/**
	 * The relative offset for the outer radius in a {@code double[]} that contains a torus.
	 */
	public static final int TORUS_OFFSET_RADIUS_OUTER = 3;
	
	/**
	 * The relative offset for the size in a {@code double[]} that contains a torus.
	 */
	public static final int TORUS_OFFSET_SIZE = 1;
	
	/**
	 * The size of a torus.
	 */
	public static final int TORUS_SIZE = 4;
	
	/**
	 * The ID of a triangle.
	 */
	public static final int TRIANGLE_ID = 11;
	
	/**
	 * The relative offset for the ID in a {@code double[]} that contains a triangle.
	 */
	public static final int TRIANGLE_OFFSET_ID = 0;
	
	/**
	 * The relative offset for the point denoted by Position A in a {@code double[]} that contains a triangle.
	 */
	public static final int TRIANGLE_OFFSET_POSITION_A = 2;
	
	/**
	 * The relative offset for the point denoted by Position B in a {@code double[]} that contains a triangle.
	 */
	public static final int TRIANGLE_OFFSET_POSITION_B = 5;
	
	/**
	 * The relative offset for the point denoted by Position C in a {@code double[]} that contains a triangle.
	 */
	public static final int TRIANGLE_OFFSET_POSITION_C = 8;
	
	/**
	 * The relative offset for the size in a {@code double[]} that contains a triangle.
	 */
	public static final int TRIANGLE_OFFSET_SIZE = 1;
	
	/**
	 * The relative offset for the vector denoted by Surface Normal A in a {@code double[]} that contains a triangle.
	 */
	public static final int TRIANGLE_OFFSET_SURFACE_NORMAL_A = 11;
	
	/**
	 * The relative offset for the vector denoted by Surface Normal B in a {@code double[]} that contains a triangle.
	 */
	public static final int TRIANGLE_OFFSET_SURFACE_NORMAL_B = 14;
	
	/**
	 * The relative offset for the vector denoted by Surface Normal C in a {@code double[]} that contains a triangle.
	 */
	public static final int TRIANGLE_OFFSET_SURFACE_NORMAL_C = 17;
	
	/**
	 * The relative offset for the point denoted by Texture Coordinates A in a {@code double[]} that contains a triangle.
	 */
	public static final int TRIANGLE_OFFSET_TEXTURE_COORDINATES_A = 20;
	
	/**
	 * The relative offset for the point denoted by Texture Coordinates B in a {@code double[]} that contains a triangle.
	 */
	public static final int TRIANGLE_OFFSET_TEXTURE_COORDINATES_B = 22;
	
	/**
	 * The relative offset for the point denoted by Texture Coordinates C in a {@code double[]} that contains a triangle.
	 */
	public static final int TRIANGLE_OFFSET_TEXTURE_COORDINATES_C = 24;
	
	/**
	 * The size of a triangle.
	 */
	public static final int TRIANGLE_SIZE = 26;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private Shape() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	// Cone3D //////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the maximum phi angle assigned to the cone contained in {@code cone3D} at offset {@code 0}.
	 * <p>
	 * If {@code cone3D} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code cone3D.length < 3}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Shape.cone3DGetPhiMax(cone3D, 0);
	 * }
	 * </pre>
	 * 
	 * @param cone3D a {@code double[]} that contains a cone
	 * @return the maximum phi angle assigned to the cone contained in {@code cone3D} at offset {@code 0}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code cone3D.length < 3}
	 * @throws NullPointerException thrown if, and only if, {@code cone3D} is {@code null}
	 */
	public static double cone3DGetPhiMax(final double[] cone3D) {
		return cone3DGetPhiMax(cone3D, 0);
	}
	
	/**
	 * Returns the maximum phi angle assigned to the cone contained in {@code cone3D} at offset {@code cone3DOffset}.
	 * <p>
	 * If {@code cone3D} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code cone3D.length < cone3DOffset + 3} or {@code cone3DOffset < 0}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param cone3D a {@code double[]} that contains a cone
	 * @param cone3DOffset the offset in {@code cone3D} to start at
	 * @return the maximum phi angle assigned to the cone contained in {@code cone3D} at offset {@code cone3DOffset}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code cone3D.length < cone3DOffset + 3} or {@code cone3DOffset < 0}
	 * @throws NullPointerException thrown if, and only if, {@code cone3D} is {@code null}
	 */
	public static double cone3DGetPhiMax(final double[] cone3D, final int cone3DOffset) {
		return cone3D[cone3DOffset + CONE_OFFSET_PHI_MAX];
	}
	
	/**
	 * Returns the radius assigned to the cone contained in {@code cone3D} at offset {@code 0}.
	 * <p>
	 * If {@code cone3D} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code cone3D.length < 4}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Shape.cone3DGetRadius(cone3D, 0);
	 * }
	 * </pre>
	 * 
	 * @param cone3D a {@code double[]} that contains a cone
	 * @return the radius assigned to the cone contained in {@code cone3D} at offset {@code 0}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code cone3D.length < 4}
	 * @throws NullPointerException thrown if, and only if, {@code cone3D} is {@code null}
	 */
	public static double cone3DGetRadius(final double[] cone3D) {
		return cone3DGetRadius(cone3D, 0);
	}
	
	/**
	 * Returns the radius assigned to the cone contained in {@code cone3D} at offset {@code cone3DOffset}.
	 * <p>
	 * If {@code cone3D} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code cone3D.length < cone3DOffset + 4} or {@code cone3DOffset < 0}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param cone3D a {@code double[]} that contains a cone
	 * @param cone3DOffset the offset in {@code cone3D} to start at
	 * @return the radius assigned to the cone contained in {@code cone3D} at offset {@code cone3DOffset}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code cone3D.length < cone3DOffset + 4} or {@code cone3DOffset < 0}
	 * @throws NullPointerException thrown if, and only if, {@code cone3D} is {@code null}
	 */
	public static double cone3DGetRadius(final double[] cone3D, final int cone3DOffset) {
		return cone3D[cone3DOffset + CONE_OFFSET_RADIUS];
	}
	
	/**
	 * Returns the maximum Z-value assigned to the cone contained in {@code cone3D} at offset {@code 0}.
	 * <p>
	 * If {@code cone3D} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code cone3D.length < 5}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Shape.cone3DGetZMax(cone3D, 0);
	 * }
	 * </pre>
	 * 
	 * @param cone3D a {@code double[]} that contains a cone
	 * @return the maximum Z-value assigned to the cone contained in {@code cone3D} at offset {@code 0}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code cone3D.length < 5}
	 * @throws NullPointerException thrown if, and only if, {@code cone3D} is {@code null}
	 */
	public static double cone3DGetZMax(final double[] cone3D) {
		return cone3DGetZMax(cone3D, 0);
	}
	
	/**
	 * Returns the maximum Z-value assigned to the cone contained in {@code cone3D} at offset {@code cone3DOffset}.
	 * <p>
	 * If {@code cone3D} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code cone3D.length < cone3DOffset + 5} or {@code cone3DOffset < 0}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param cone3D a {@code double[]} that contains a cone
	 * @param cone3DOffset the offset in {@code cone3D} to start at
	 * @return the maximum Z-value assigned to the cone contained in {@code cone3D} at offset {@code cone3DOffset}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code cone3D.length < cone3DOffset + 5} or {@code cone3DOffset < 0}
	 * @throws NullPointerException thrown if, and only if, {@code cone3D} is {@code null}
	 */
	public static double cone3DGetZMax(final double[] cone3D, final int cone3DOffset) {
		return cone3D[cone3DOffset + CONE_OFFSET_Z_MAX];
	}
	
	/**
	 * Performs an intersection test between the ray contained in {@code ray3D} and the cone contained in {@code cone3D}.
	 * <p>
	 * Returns the parametric distance to the point of intersection, or {@code Doubles.NaN} if there is no intersection.
	 * <p>
	 * If either {@code ray3D} or {@code cone3D} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code ray3D.length < 8} or {@code cone3D.length < 5}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Shape.cone3DIntersection(ray3D, cone3D, 0, 0);
	 * }
	 * </pre>
	 * 
	 * @param ray3D a {@code double[]} that contains a ray
	 * @param cone3D a {@code double[]} that contains a cone
	 * @return the parametric distance to the point of intersection, or {@code Doubles.NaN} if there is no intersection
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code ray3D.length < 8} or {@code cone3D.length < 5}
	 * @throws NullPointerException thrown if, and only if, either {@code ray3D} or {@code cone3D} are {@code null}
	 */
	public static double cone3DIntersection(final double[] ray3D, final double[] cone3D) {
		return cone3DIntersection(ray3D, cone3D, 0, 0);
	}
	
	/**
	 * Performs an intersection test between the ray contained in {@code ray3D} and the cone contained in {@code cone3D}.
	 * <p>
	 * Returns the parametric distance to the point of intersection, or {@code Doubles.NaN} if there is no intersection.
	 * <p>
	 * If either {@code ray3D} or {@code cone3D} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code ray3D.length < ray3DOffset + 8}, {@code ray3DOffset < 0}, {@code cone3D.length < cone3DOffset + 5} or {@code cone3DOffset < 0}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param ray3D a {@code double[]} that contains a ray
	 * @param cone3D a {@code double[]} that contains a cone
	 * @param ray3DOffset the offset in {@code ray3D} to start at
	 * @param cone3DOffset the offset in {@code cone3D} to start at
	 * @return the parametric distance to the point of intersection, or {@code Doubles.NaN} if there is no intersection
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code ray3D.length < ray3DOffset + 8}, {@code ray3DOffset < 0}, {@code cone3D.length < cone3DOffset + 5} or {@code cone3DOffset < 0}
	 * @throws NullPointerException thrown if, and only if, either {@code ray3D} or {@code cone3D} are {@code null}
	 */
	public static double cone3DIntersection(final double[] ray3D, final double[] cone3D, final int ray3DOffset, final int cone3DOffset) {
		final double[] point3DOrigin = ray3DGetOrigin(ray3D, point3D(), ray3DOffset, 0);
		
		final double[] vector3DDirection = ray3DGetDirection(ray3D, vector3D(), ray3DOffset, 0);
		
		final double tMinimum = ray3DGetTMinimum(ray3D, ray3DOffset);
		final double tMaximum = ray3DGetTMaximum(ray3D, ray3DOffset);
		
		final double phiMax = cone3DGetPhiMax(cone3D, cone3DOffset);
		final double radius = cone3DGetRadius(cone3D, cone3DOffset);
		final double zMax = cone3DGetZMax(cone3D, cone3DOffset);
		
		final double k = (radius / zMax) * (radius / zMax);
		
		final double a = vector3DGetX(vector3DDirection) * vector3DGetX(vector3DDirection) + vector3DGetY(vector3DDirection) * vector3DGetY(vector3DDirection) - k * vector3DGetZ(vector3DDirection) * vector3DGetZ(vector3DDirection);
		final double b = 2.0D * (vector3DGetX(vector3DDirection) * point3DGetX(point3DOrigin) + vector3DGetY(vector3DDirection) * point3DGetY(point3DOrigin) - k * vector3DGetZ(vector3DDirection) * (point3DGetZ(point3DOrigin) - zMax));
		final double c = point3DGetX(point3DOrigin) * point3DGetX(point3DOrigin) + point3DGetY(point3DOrigin) * point3DGetY(point3DOrigin) - k * (point3DGetZ(point3DOrigin) - zMax) * (point3DGetZ(point3DOrigin) - zMax);
		
		final double[] ts = solveQuadraticSystem(a, b, c);
		
		for(int i = 0; i < ts.length; i++) {
			final double t = ts[i];
			
			if(isNaN(t)) {
				return NaN;
			}
			
			if(t > tMinimum && t < tMaximum) {
				final double[] point3D = point3DAdd(point3DOrigin, vector3DDirection, t);
				
				final double phi = getOrAdd(atan2(point3DGetY(point3D), point3DGetX(point3D)), 0.0D, PI_MULTIPLIED_BY_2);
				
				if(point3DGetZ(point3D) >= 0.0D && point3DGetZ(point3D) <= zMax && phi <= phiMax) {
					return t;
				}
			}
		}
		
		return NaN;
	}
	
//	TODO: Add Javadocs!
	public static double cone3DSurfaceArea(final double[] cone3D) {
		return cone3DSurfaceArea(cone3D, 0);
	}
	
//	TODO: Add Javadocs!
	public static double cone3DSurfaceArea(final double[] cone3D, final int cone3DOffset) {
		final double phiMax = cone3DGetPhiMax(cone3D, cone3DOffset);
		
		final double radius = cone3DGetRadius(cone3D, cone3DOffset);
		final double radiusSquared = radius * radius;
		
		final double zMax = cone3DGetZMax(cone3D, cone3DOffset);
		final double zMaxSquared = zMax * zMax;
		
		final double surfaceArea = radius * sqrt(zMaxSquared + radiusSquared) * phiMax / 2.0D;
		
		return surfaceArea;
	}
	
	/**
	 * Returns a {@code double[]} that contains a cone.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Shape.cone3D(Doubles.toRadians(360.0D), 1.0D, 1.0D);
	 * }
	 * </pre>
	 * 
	 * @return a {@code double[]} that contains a cone
	 */
	public static double[] cone3D() {
		return cone3D(toRadians(360.0D), 1.0D, 1.0D);
	}
	
	/**
	 * Returns a {@code double[]} that contains a cone.
	 * 
	 * @param phiMax the maximum phi angle of the cone
	 * @param radius the radius of the cone
	 * @param zMax the maximum Z-value of the cone
	 * @return a {@code double[]} that contains a cone
	 */
	public static double[] cone3D(final double phiMax, final double radius, final double zMax) {
		return cone3DSet(new double[CONE_SIZE], phiMax, radius, zMax);
	}
	
//	TODO: Add Javadocs!
	public static double[] cone3DComputeOrthonormalBasis(final double[] ray3D, final double[] cone3D, final double t) {
		return cone3DComputeOrthonormalBasis(ray3D, cone3D, t, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] cone3DComputeOrthonormalBasis(final double[] ray3D, final double[] cone3D, final double t, final int ray3DOffset, final int cone3DOffset) {
		final double[] point3DOrigin = ray3DGetOrigin(ray3D, point3D(), ray3DOffset, 0);
		
		final double[] vector3DDirection = ray3DGetDirection(ray3D, vector3D(), ray3DOffset, 0);
		
		final double phiMax = cone3DGetPhiMax(cone3D, cone3DOffset);
		final double zMax = cone3DGetZMax(cone3D, cone3DOffset);
		
		final double[] point3D = point3DAdd(point3DOrigin, vector3DDirection, t);
		
		final double v = point3DGetZ(point3D) / zMax;
		
		final double[] vector3DU = vector3DNormalize(vector3D(-phiMax * point3DGetY(point3D), phiMax * point3DGetX(point3D), 0.0D));
		final double[] vector3DV = vector3DNormalize(vector3D(-point3DGetX(point3D) / (1.0D - v), -point3DGetY(point3D) / (1.0D - v), zMax));
		final double[] vector3DW = vector3DCrossProduct(vector3DU, vector3DV);
		
		return orthonormalBasis33D(vector3DU, vector3DV, vector3DW);
	}
	
//	TODO: Add Javadocs!
	public static double[] cone3DComputeSurfaceNormal(final double[] ray3D, final double[] cone3D, final double t) {
		return cone3DComputeSurfaceNormal(ray3D, cone3D, t, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] cone3DComputeSurfaceNormal(final double[] ray3D, final double[] cone3D, final double t, final int ray3DOffset, final int cone3DOffset) {
		return orthonormalBasis33DGetW(cone3DComputeOrthonormalBasis(ray3D, cone3D, t, ray3DOffset, cone3DOffset));
	}
	
//	TODO: Add Javadocs!
	public static double[] cone3DComputeTextureCoordinates(final double[] ray3D, final double[] cone3D, final double t) {
		return cone3DComputeTextureCoordinates(ray3D, cone3D, t, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] cone3DComputeTextureCoordinates(final double[] ray3D, final double[] cone3D, final double t, final int ray3DOffset, final int cone3DOffset) {
		final double[] point3DOrigin = ray3DGetOrigin(ray3D, point3D(), ray3DOffset, 0);
		
		final double[] vector3DDirection = ray3DGetDirection(ray3D, vector3D(), ray3DOffset, 0);
		
		final double phiMax = cone3DGetPhiMax(cone3D, cone3DOffset);
		final double zMax = cone3DGetZMax(cone3D, cone3DOffset);
		
		final double[] point3D = point3DAdd(point3DOrigin, vector3DDirection, t);
		
		final double phi = getOrAdd(atan2(point3DGetY(point3D), point3DGetX(point3D)), 0.0D, PI_MULTIPLIED_BY_2);
		
		final double u = phi / phiMax;
		final double v = point3DGetZ(point3D) / zMax;
		
		return point2D(u, v);
	}
	
	/**
	 * Sets the values of the cone contained in {@code cone3DResult} at offset {@code 0}.
	 * <p>
	 * Returns {@code cone3DResult}.
	 * <p>
	 * If {@code cone3DResult} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code cone3DResult.length < 5}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Shape.cone3DSet(cone3DResult, phiMax, radius, zMax, 0);
	 * }
	 * </pre>
	 * 
	 * @param cone3DResult a {@code double[]} that contains a cone
	 * @param phiMax the maximum phi angle of the cone
	 * @param radius the radius of the cone
	 * @param zMax the maximum Z-value of the cone
	 * @return {@code cone3DResult}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code cone3DResult.length < 5}
	 * @throws NullPointerException thrown if, and only if, {@code cone3DResult} is {@code null}
	 */
	public static double[] cone3DSet(final double[] cone3DResult, final double phiMax, final double radius, final double zMax) {
		return cone3DSet(cone3DResult, phiMax, radius, zMax, 0);
	}
	
	/**
	 * Sets the values of the cone contained in {@code cone3DResult} at offset {@code cone3DResultOffset}.
	 * <p>
	 * Returns {@code cone3DResult}.
	 * <p>
	 * If {@code cone3DResult} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code cone3DResult.length < cone3DResultOffset + 5} or {@code cone3DResultOffset < 0}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param cone3DResult a {@code double[]} that contains a cone
	 * @param phiMax the maximum phi angle of the cone
	 * @param radius the radius of the cone
	 * @param zMax the maximum Z-value of the cone
	 * @param cone3DResultOffset the offset in {@code cone3DResult} to start at
	 * @return {@code cone3DResult}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code cone3DResult.length < cone3DResultOffset + 5} or {@code cone3DResultOffset < 0}
	 * @throws NullPointerException thrown if, and only if, {@code cone3DResult} is {@code null}
	 */
	public static double[] cone3DSet(final double[] cone3DResult, final double phiMax, final double radius, final double zMax, final int cone3DResultOffset) {
		cone3DResult[cone3DResultOffset + CONE_OFFSET_ID] = CONE_ID;
		cone3DResult[cone3DResultOffset + CONE_OFFSET_SIZE] = CONE_SIZE;
		cone3DResult[cone3DResultOffset + CONE_OFFSET_PHI_MAX] = phiMax;
		cone3DResult[cone3DResultOffset + CONE_OFFSET_RADIUS] = radius;
		cone3DResult[cone3DResultOffset + CONE_OFFSET_Z_MAX] = zMax;
		
		return cone3DResult;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	// Cylinder3D //////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the maximum phi angle assigned to the cylinder contained in {@code cylinder3D} at offset {@code 0}.
	 * <p>
	 * If {@code cylinder3D} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code cylinder3D.length < 3}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Shape.cylinder3DGetPhiMax(cylinder3D, 0);
	 * }
	 * </pre>
	 * 
	 * @param cylinder3D a {@code double[]} that contains a cylinder
	 * @return the maximum phi angle assigned to the cylinder contained in {@code cylinder3D} at offset {@code 0}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code cylinder3D.length < 3}
	 * @throws NullPointerException thrown if, and only if, {@code cylinder3D} is {@code null}
	 */
	public static double cylinder3DGetPhiMax(final double[] cylinder3D) {
		return cylinder3DGetPhiMax(cylinder3D, 0);
	}
	
	/**
	 * Returns the maximum phi angle assigned to the cylinder contained in {@code cylinder3D} at offset {@code cylinder3DOffset}.
	 * <p>
	 * If {@code cylinder3D} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code cylinder3D.length < cylinder3DOffset + 3} or {@code cylinder3DOffset < 0}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param cylinder3D a {@code double[]} that contains a cylinder
	 * @param cylinder3DOffset the offset in {@code cylinder3D} to start at
	 * @return the maximum phi angle assigned to the cylinder contained in {@code cylinder3D} at offset {@code cylinder3DOffset}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code cylinder3D.length < cylinder3DOffset + 3} or {@code cylinder3DOffset < 0}
	 * @throws NullPointerException thrown if, and only if, {@code cylinder3D} is {@code null}
	 */
	public static double cylinder3DGetPhiMax(final double[] cylinder3D, final int cylinder3DOffset) {
		return cylinder3D[cylinder3DOffset + CYLINDER_OFFSET_PHI_MAX];
	}
	
	/**
	 * Returns the radius assigned to the cylinder contained in {@code cylinder3D} at offset {@code 0}.
	 * <p>
	 * If {@code cylinder3D} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code cylinder3D.length < 4}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Shape.cylinder3DGetRadius(cylinder3D, 0);
	 * }
	 * </pre>
	 * 
	 * @param cylinder3D a {@code double[]} that contains a cylinder
	 * @return the radius assigned to the cylinder contained in {@code cylinder3D} at offset {@code 0}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code cylinder3D.length < 4}
	 * @throws NullPointerException thrown if, and only if, {@code cylinder3D} is {@code null}
	 */
	public static double cylinder3DGetRadius(final double[] cylinder3D) {
		return cylinder3DGetRadius(cylinder3D, 0);
	}
	
	/**
	 * Returns the radius assigned to the cylinder contained in {@code cylinder3D} at offset {@code cylinder3DOffset}.
	 * <p>
	 * If {@code cylinder3D} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code cylinder3D.length < cylinder3DOffset + 4} or {@code cylinder3DOffset < 0}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param cylinder3D a {@code double[]} that contains a cylinder
	 * @param cylinder3DOffset the offset in {@code cylinder3D} to start at
	 * @return the radius assigned to the cylinder contained in {@code cylinder3D} at offset {@code cylinder3DOffset}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code cylinder3D.length < cylinder3DOffset + 4} or {@code cylinder3DOffset < 0}
	 * @throws NullPointerException thrown if, and only if, {@code cylinder3D} is {@code null}
	 */
	public static double cylinder3DGetRadius(final double[] cylinder3D, final int cylinder3DOffset) {
		return cylinder3D[cylinder3DOffset + CYLINDER_OFFSET_RADIUS];
	}
	
	/**
	 * Returns the maximum Z-value assigned to the cylinder contained in {@code cylinder3D} at offset {@code 0}.
	 * <p>
	 * If {@code cylinder3D} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code cylinder3D.length < 5}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Shape.cylinder3DGetZMax(cylinder3D, 0);
	 * }
	 * </pre>
	 * 
	 * @param cylinder3D a {@code double[]} that contains a cylinder
	 * @return the maximum Z-value assigned to the cylinder contained in {@code cylinder3D} at offset {@code 0}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code cylinder3D.length < 5}
	 * @throws NullPointerException thrown if, and only if, {@code cylinder3D} is {@code null}
	 */
	public static double cylinder3DGetZMax(final double[] cylinder3D) {
		return cylinder3DGetZMax(cylinder3D, 0);
	}
	
	/**
	 * Returns the maximum Z-value assigned to the cylinder contained in {@code cylinder3D} at offset {@code cylinder3DOffset}.
	 * <p>
	 * If {@code cylinder3D} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code cylinder3D.length < cylinder3DOffset + 5} or {@code cylinder3DOffset < 0}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param cylinder3D a {@code double[]} that contains a cylinder
	 * @param cylinder3DOffset the offset in {@code cylinder3D} to start at
	 * @return the maximum Z-value assigned to the cylinder contained in {@code cylinder3D} at offset {@code cylinder3DOffset}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code cylinder3D.length < cylinder3DOffset + 5} or {@code cylinder3DOffset < 0}
	 * @throws NullPointerException thrown if, and only if, {@code cylinder3D} is {@code null}
	 */
	public static double cylinder3DGetZMax(final double[] cylinder3D, final int cylinder3DOffset) {
		return cylinder3D[cylinder3DOffset + CYLINDER_OFFSET_Z_MAX];
	}
	
	/**
	 * Returns the minimum Z-value assigned to the cylinder contained in {@code cylinder3D} at offset {@code 0}.
	 * <p>
	 * If {@code cylinder3D} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code cylinder3D.length < 6}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Shape.cylinder3DGetZMin(cylinder3D, 0);
	 * }
	 * </pre>
	 * 
	 * @param cylinder3D a {@code double[]} that contains a cylinder
	 * @return the minimum Z-value assigned to the cylinder contained in {@code cylinder3D} at offset {@code 0}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code cylinder3D.length < 6}
	 * @throws NullPointerException thrown if, and only if, {@code cylinder3D} is {@code null}
	 */
	public static double cylinder3DGetZMin(final double[] cylinder3D) {
		return cylinder3DGetZMin(cylinder3D, 0);
	}
	
	/**
	 * Returns the minimum Z-value assigned to the cylinder contained in {@code cylinder3D} at offset {@code cylinder3DOffset}.
	 * <p>
	 * If {@code cylinder3D} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code cylinder3D.length < cylinder3DOffset + 6} or {@code cylinder3DOffset < 0}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param cylinder3D a {@code double[]} that contains a cylinder
	 * @param cylinder3DOffset the offset in {@code cylinder3D} to start at
	 * @return the minimum Z-value assigned to the cylinder contained in {@code cylinder3D} at offset {@code cylinder3DOffset}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code cylinder3D.length < cylinder3DOffset + 6} or {@code cylinder3DOffset < 0}
	 * @throws NullPointerException thrown if, and only if, {@code cylinder3D} is {@code null}
	 */
	public static double cylinder3DGetZMin(final double[] cylinder3D, final int cylinder3DOffset) {
		return cylinder3D[cylinder3DOffset + CYLINDER_OFFSET_Z_MIN];
	}
	
	/**
	 * Performs an intersection test between the ray contained in {@code ray3D} and the cylinder contained in {@code cylinder3D}.
	 * <p>
	 * Returns the parametric distance to the point of intersection, or {@code Doubles.NaN} if there is no intersection.
	 * <p>
	 * If either {@code ray3D} or {@code cylinder3D} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code ray3D.length < 8} or {@code cylinder3D.length < 6}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Shape.cylinder3DIntersection(ray3D, cylinder3D, 0, 0);
	 * }
	 * </pre>
	 * 
	 * @param ray3D a {@code double[]} that contains a ray
	 * @param cylinder3D a {@code double[]} that contains a cylinder
	 * @return the parametric distance to the point of intersection, or {@code Doubles.NaN} if there is no intersection
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code ray3D.length < 8} or {@code cylinder3D.length < 6}
	 * @throws NullPointerException thrown if, and only if, either {@code ray3D} or {@code cylinder3D} are {@code null}
	 */
	public static double cylinder3DIntersection(final double[] ray3D, final double[] cylinder3D) {
		return cylinder3DIntersection(ray3D, cylinder3D, 0, 0);
	}
	
	/**
	 * Performs an intersection test between the ray contained in {@code ray3D} and the cylinder contained in {@code cylinder3D}.
	 * <p>
	 * Returns the parametric distance to the point of intersection, or {@code Doubles.NaN} if there is no intersection.
	 * <p>
	 * If either {@code ray3D} or {@code cylinder3D} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code ray3D.length < ray3DOffset + 8}, {@code ray3DOffset < 0}, {@code cylinder3D.length < cylinder3DOffset + 6} or {@code cylinder3DOffset < 0}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param ray3D a {@code double[]} that contains a ray
	 * @param cylinder3D a {@code double[]} that contains a cylinder
	 * @param ray3DOffset the offset in {@code ray3D} to start at
	 * @param cylinder3DOffset the offset in {@code cylinder3D} to start at
	 * @return the parametric distance to the point of intersection, or {@code Doubles.NaN} if there is no intersection
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code ray3D.length < ray3DOffset + 8}, {@code ray3DOffset < 0}, {@code cylinder3D.length < cylinder3DOffset + 6} or {@code cylinder3DOffset < 0}
	 * @throws NullPointerException thrown if, and only if, either {@code ray3D} or {@code cylinder3D} are {@code null}
	 */
	public static double cylinder3DIntersection(final double[] ray3D, final double[] cylinder3D, final int ray3DOffset, final int cylinder3DOffset) {
		final double[] point3DOrigin = ray3DGetOrigin(ray3D, point3D(), ray3DOffset, 0);
		
		final double[] vector3DDirection = ray3DGetDirection(ray3D, vector3D(), ray3DOffset, 0);
		
		final double tMinimum = ray3DGetTMinimum(ray3D, ray3DOffset);
		final double tMaximum = ray3DGetTMaximum(ray3D, ray3DOffset);
		
		final double phiMax = cylinder3DGetPhiMax(cylinder3D, cylinder3DOffset);
		final double radius = cylinder3DGetRadius(cylinder3D, cylinder3DOffset);
		final double zMax = cylinder3DGetZMax(cylinder3D, cylinder3DOffset);
		final double zMin = cylinder3DGetZMin(cylinder3D, cylinder3DOffset);
		
		final double a = vector3DGetX(vector3DDirection) * vector3DGetX(vector3DDirection) + vector3DGetY(vector3DDirection) * vector3DGetY(vector3DDirection);
		final double b = 2.0D * (vector3DGetX(vector3DDirection) * point3DGetX(point3DOrigin) + vector3DGetY(vector3DDirection) * point3DGetY(point3DOrigin));
		final double c = point3DGetX(point3DOrigin) * point3DGetX(point3DOrigin) + point3DGetY(point3DOrigin) * point3DGetY(point3DOrigin) - radius * radius;
		
		final double[] ts = solveQuadraticSystem(a, b, c);
		
		for(int i = 0; i < ts.length; i++) {
			final double t = ts[i];
			
			if(isNaN(t)) {
				return NaN;
			}
			
			if(t > tMinimum && t < tMaximum) {
				final double[] point3D = point3DAdd(point3DOrigin, vector3DDirection, t);
				
				final double r = sqrt(point3DGetX(point3D) * point3DGetX(point3D) + point3DGetY(point3D) * point3DGetY(point3D));
				final double phi = getOrAdd(atan2(point3DGetY(point3D) * (radius / r), point3DGetX(point3D) * (radius / r)), 0.0D, PI_MULTIPLIED_BY_2);
				
				if(point3DGetZ(point3D) >= zMin && point3DGetZ(point3D) <= zMax && phi <= phiMax) {
					return t;
				}
			}
		}
		
		return NaN;
	}
	
//	TODO: Add Javadocs!
	public static double cylinder3DSurfaceArea(final double[] cylinder3D) {
		return cylinder3DSurfaceArea(cylinder3D, 0);
	}
	
//	TODO: Add Javadocs!
	public static double cylinder3DSurfaceArea(final double[] cylinder3D, final int cylinder3DOffset) {
		final double phiMax = cylinder3DGetPhiMax(cylinder3D, cylinder3DOffset);
		
		final double radius = cylinder3DGetRadius(cylinder3D, cylinder3DOffset);
		
		final double zMax = cylinder3DGetZMax(cylinder3D, cylinder3DOffset);
		final double zMin = cylinder3DGetZMin(cylinder3D, cylinder3DOffset);
		
		final double surfaceArea = (zMax - zMin) * radius * phiMax;
		
		return surfaceArea;
	}
	
	/**
	 * Returns a {@code double[]} that contains a cylinder.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Shape.cylinder3D(Doubles.toRadians(360.0D), 1.0D, 1.0D, -1.0D);
	 * }
	 * </pre>
	 * 
	 * @return a {@code double[]} that contains a cylinder
	 */
	public static double[] cylinder3D() {
		return cylinder3D(toRadians(360.0D), 1.0D, 1.0D, -1.0D);
	}
	
	/**
	 * Returns a {@code double[]} that contains a cylinder.
	 * 
	 * @param phiMax the maximum phi angle of the cylinder
	 * @param radius the radius of the cylinder
	 * @param zMax the maximum Z-value of the cylinder
	 * @param zMin the minimum Z-value of the cylinder
	 * @return a {@code double[]} that contains a cylinder
	 */
	public static double[] cylinder3D(final double phiMax, final double radius, final double zMax, final double zMin) {
		return cylinder3DSet(new double[CYLINDER_SIZE], phiMax, radius, zMax, zMin);
	}
	
//	TODO: Add Javadocs!
	public static double[] cylinder3DComputeOrthonormalBasis(final double[] ray3D, final double[] cylinder3D, final double t) {
		return cylinder3DComputeOrthonormalBasis(ray3D, cylinder3D, t, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] cylinder3DComputeOrthonormalBasis(final double[] ray3D, final double[] cylinder3D, final double t, final int ray3DOffset, final int cylinder3DOffset) {
		final double[] point3DOrigin = ray3DGetOrigin(ray3D, point3D(), ray3DOffset, 0);
		
		final double[] vector3DDirection = ray3DGetDirection(ray3D, vector3D(), ray3DOffset, 0);
		
		final double phiMax = cylinder3DGetPhiMax(cylinder3D, cylinder3DOffset);
		final double zMax = cylinder3DGetZMax(cylinder3D, cylinder3DOffset);
		final double zMin = cylinder3DGetZMin(cylinder3D, cylinder3DOffset);
		
		final double[] point3D = point3DAdd(point3DOrigin, vector3DDirection, t);
		
		final double[] vector3DU = vector3DNormalize(vector3D(-phiMax * point3DGetY(point3D), phiMax * point3DGetX(point3D), 0.0D));
		final double[] vector3DV = vector3DNormalize(vector3D(0.0D, 0.0D, zMax - zMin));
		final double[] vector3DW = vector3DCrossProduct(vector3DU, vector3DV);
		
		return orthonormalBasis33D(vector3DU, vector3DV, vector3DW);
	}
	
//	TODO: Add Javadocs!
	public static double[] cylinder3DComputeSurfaceNormal(final double[] ray3D, final double[] cylinder3D, final double t) {
		return cylinder3DComputeSurfaceNormal(ray3D, cylinder3D, t, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] cylinder3DComputeSurfaceNormal(final double[] ray3D, final double[] cylinder3D, final double t, final int ray3DOffset, final int cylinder3DOffset) {
		return orthonormalBasis33DGetW(cylinder3DComputeOrthonormalBasis(ray3D, cylinder3D, t, ray3DOffset, cylinder3DOffset));
	}
	
//	TODO: Add Javadocs!
	public static double[] cylinder3DComputeTextureCoordinates(final double[] ray3D, final double[] cylinder3D, final double t) {
		return cylinder3DComputeTextureCoordinates(ray3D, cylinder3D, t, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] cylinder3DComputeTextureCoordinates(final double[] ray3D, final double[] cylinder3D, final double t, final int ray3DOffset, final int cylinder3DOffset) {
		final double[] point3DOrigin = ray3DGetOrigin(ray3D, point3D(), ray3DOffset, 0);
		
		final double[] vector3DDirection = ray3DGetDirection(ray3D, vector3D(), ray3DOffset, 0);
		
		final double phiMax = cylinder3DGetPhiMax(cylinder3D, cylinder3DOffset);
		final double radius = cylinder3DGetRadius(cylinder3D, cylinder3DOffset);
		final double zMax = cylinder3DGetZMax(cylinder3D, cylinder3DOffset);
		final double zMin = cylinder3DGetZMin(cylinder3D, cylinder3DOffset);
		
		final double[] point3D = point3DAdd(point3DOrigin, vector3DDirection, t);
		
		final double r = sqrt(point3DGetX(point3D) * point3DGetX(point3D) + point3DGetY(point3D) * point3DGetY(point3D));
		
		final double[] point3DTransformed = point3D(point3DGetX(point3D) * (radius / r), point3DGetY(point3D) * (radius / r), point3DGetZ(point3D));
		
		final double phi = getOrAdd(atan2(point3DGetY(point3DTransformed), point3DGetX(point3DTransformed)), 0.0D, PI_MULTIPLIED_BY_2);
		
		final double u = phi / phiMax;
		final double v = (point3DGetZ(point3DTransformed) - zMin) / (zMax - zMin);
		
		return point2D(u, v);
	}
	
	/**
	 * Sets the values of the cylinder contained in {@code cylinder3DResult} at offset {@code 0}.
	 * <p>
	 * Returns {@code cylinder3DResult}.
	 * <p>
	 * If {@code cylinder3DResult} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code cylinder3DResult.length < 6}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Shape.cylinder3DSet(cylinder3DResult, phiMax, radius, zMax, zMin, 0);
	 * }
	 * </pre>
	 * 
	 * @param cylinder3DResult a {@code double[]} that contains a cylinder
	 * @param phiMax the maximum phi angle of the cylinder
	 * @param radius the radius of the cylinder
	 * @param zMax the maximum Z-value of the cylinder
	 * @param zMin the minimum Z-value of the cylinder
	 * @return {@code cylinder3DResult}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code cylinder3DResult.length < 6}
	 * @throws NullPointerException thrown if, and only if, {@code cylinder3DResult} is {@code null}
	 */
	public static double[] cylinder3DSet(final double[] cylinder3DResult, final double phiMax, final double radius, final double zMax, final double zMin) {
		return cylinder3DSet(cylinder3DResult, phiMax, radius, zMax, zMin, 0);
	}
	
	/**
	 * Sets the values of the cylinder contained in {@code cylinder3DResult} at offset {@code cylinder3DResultOffset}.
	 * <p>
	 * Returns {@code cylinder3DResult}.
	 * <p>
	 * If {@code cylinder3DResult} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code cylinder3DResult.length < cylinder3DResultOffset + 6} or {@code cylinder3DResultOffset < 0}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param cylinder3DResult a {@code double[]} that contains a cylinder
	 * @param phiMax the maximum phi angle of the cylinder
	 * @param radius the radius of the cylinder
	 * @param zMax the maximum Z-value of the cylinder
	 * @param zMin the minimum Z-value of the cylinder
	 * @param cylinder3DResultOffset the offset in {@code cylinder3DResult} to start at
	 * @return {@code cylinder3DResult}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code cylinder3DResult.length < cylinder3DResultOffset + 6} or {@code cylinder3DResultOffset < 0}
	 * @throws NullPointerException thrown if, and only if, {@code cylinder3DResult} is {@code null}
	 */
	public static double[] cylinder3DSet(final double[] cylinder3DResult, final double phiMax, final double radius, final double zMax, final double zMin, final int cylinder3DResultOffset) {
		cylinder3DResult[cylinder3DResultOffset + CYLINDER_OFFSET_ID] = CYLINDER_ID;
		cylinder3DResult[cylinder3DResultOffset + CYLINDER_OFFSET_SIZE] = CYLINDER_SIZE;
		cylinder3DResult[cylinder3DResultOffset + CYLINDER_OFFSET_PHI_MAX] = phiMax;
		cylinder3DResult[cylinder3DResultOffset + CYLINDER_OFFSET_RADIUS] = radius;
		cylinder3DResult[cylinder3DResultOffset + CYLINDER_OFFSET_Z_MAX] = zMax;
		cylinder3DResult[cylinder3DResultOffset + CYLINDER_OFFSET_Z_MIN] = zMin;
		
		return cylinder3DResult;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	// Disk3D //////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public static double disk3DGetPhiMax(final double[] disk3D) {
		return disk3DGetPhiMax(disk3D, 0);
	}
	
//	TODO: Add Javadocs!
	public static double disk3DGetPhiMax(final double[] disk3D, final int disk3DOffset) {
		return disk3D[disk3DOffset + DISK_OFFSET_PHI_MAX];
	}
	
//	TODO: Add Javadocs!
	public static double disk3DGetRadiusInner(final double[] disk3D) {
		return disk3DGetRadiusInner(disk3D, 0);
	}
	
//	TODO: Add Javadocs!
	public static double disk3DGetRadiusInner(final double[] disk3D, final int disk3DOffset) {
		return disk3D[disk3DOffset + DISK_OFFSET_RADIUS_INNER];
	}
	
//	TODO: Add Javadocs!
	public static double disk3DGetRadiusOuter(final double[] disk3D) {
		return disk3DGetRadiusOuter(disk3D, 0);
	}
	
//	TODO: Add Javadocs!
	public static double disk3DGetRadiusOuter(final double[] disk3D, final int disk3DOffset) {
		return disk3D[disk3DOffset + DISK_OFFSET_RADIUS_OUTER];
	}
	
//	TODO: Add Javadocs!
	public static double disk3DGetZMax(final double[] disk3D) {
		return disk3DGetZMax(disk3D, 0);
	}
	
//	TODO: Add Javadocs!
	public static double disk3DGetZMax(final double[] disk3D, final int disk3DOffset) {
		return disk3D[disk3DOffset + DISK_OFFSET_Z_MAX];
	}
	
//	TODO: Add Javadocs!
	public static double disk3DIntersection(final double[] ray3D, final double[] disk3D) {
		return disk3DIntersection(ray3D, disk3D, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double disk3DIntersection(final double[] ray3D, final double[] disk3D, final int ray3DOffset, final int disk3DOffset) {
		final double[] point3DOrigin = ray3DGetOrigin(ray3D, point3D(), ray3DOffset, 0);
		
		final double[] vector3DDirection = ray3DGetDirection(ray3D, vector3D(), ray3DOffset, 0);
		
		final double tMinimum = ray3DGetTMinimum(ray3D, ray3DOffset);
		final double tMaximum = ray3DGetTMaximum(ray3D, ray3DOffset);
		
		if(isZero(vector3DGetZ(vector3DDirection))) {
			return NaN;
		}
		
		final double phiMax = disk3DGetPhiMax(disk3D, disk3DOffset);
		final double radiusInner = disk3DGetRadiusInner(disk3D, disk3DOffset);
		final double radiusOuter = disk3DGetRadiusOuter(disk3D, disk3DOffset);
		final double zMax = disk3DGetZMax(disk3D, disk3DOffset);
		
		final double t = (zMax - point3DGetZ(point3DOrigin)) / vector3DGetZ(vector3DDirection);
		
		if(t <= tMinimum || t >= tMaximum) {
			return NaN;
		}
		
		final double[] point3D = point3DAdd(point3DOrigin, vector3DDirection, t);
		
		final double distanceSquared = point3DGetX(point3D) * point3DGetX(point3D) + point3DGetY(point3D) * point3DGetY(point3D);
		
		if(distanceSquared > radiusOuter * radiusOuter || distanceSquared < radiusInner * radiusInner) {
			return NaN;
		}
		
		final double phi = getOrAdd(atan2(point3DGetY(point3D), point3DGetX(point3D)), 0.0D, PI_MULTIPLIED_BY_2);
		
		if(phi > phiMax) {
			return NaN;
		}
		
		return t;
	}
	
//	TODO: Add Javadocs!
	public static double disk3DSurfaceArea(final double[] disk3D) {
		return disk3DSurfaceArea(disk3D, 0);
	}
	
//	TODO: Add Javadocs!
	public static double disk3DSurfaceArea(final double[] disk3D, final int disk3DOffset) {
		final double phiMax = disk3DGetPhiMax(disk3D, disk3DOffset);
		
		final double radiusInner = disk3DGetRadiusInner(disk3D, disk3DOffset);
		final double radiusInnerSquared = radiusInner * radiusInner;
		
		final double radiusOuter = disk3DGetRadiusOuter(disk3D, disk3DOffset);
		final double radiusOuterSquared = radiusOuter * radiusOuter;
		
		final double surfaceArea = phiMax * 0.5D * (radiusOuterSquared - radiusInnerSquared);
		
		return surfaceArea;
	}
	
//	TODO: Add Javadocs!
	public static double[] disk3D() {
		return disk3D(toRadians(360.0D), 0.0D, 1.0D, 0.0D);
	}
	
//	TODO: Add Javadocs!
	public static double[] disk3D(final double phiMax, final double radiusInner, final double radiusOuter, final double zMax) {
		return disk3DSet(new double[DISK_SIZE], phiMax, radiusInner, radiusOuter, zMax);
	}
	
//	TODO: Add Javadocs!
	public static double[] disk3DComputeOrthonormalBasis(final double[] ray3D, final double[] disk3D, final double t) {
		return disk3DComputeOrthonormalBasis(ray3D, disk3D, t, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] disk3DComputeOrthonormalBasis(final double[] ray3D, final double[] disk3D, final double t, final int ray3DOffset, final int disk3DOffset) {
		final double[] point3DOrigin = ray3DGetOrigin(ray3D, point3D(), ray3DOffset, 0);
		
		final double[] vector3DDirection = ray3DGetDirection(ray3D, vector3D(), ray3DOffset, 0);
		
		final double phiMax = disk3DGetPhiMax(disk3D, disk3DOffset);
		final double radiusInner = disk3DGetRadiusInner(disk3D, disk3DOffset);
		final double radiusOuter = disk3DGetRadiusOuter(disk3D, disk3DOffset);
		
		final double[] point3D = point3DAdd(point3DOrigin, vector3DDirection, t);
		
		final double distance = sqrt(point3DGetX(point3D) * point3DGetX(point3D) + point3DGetY(point3D) * point3DGetY(point3D));
		
		final double[] vector3DU = vector3DNormalize(vector3D(-phiMax * point3DGetY(point3D), phiMax * point3DGetX(point3D), 0.0D));
		final double[] vector3DV = vector3DNormalize(vector3D(point3DGetX(point3D) * (radiusInner - radiusOuter) / distance, point3DGetY(point3D) * (radiusInner - radiusOuter) / distance, 0.0D));
		final double[] vector3DW = vector3DCrossProduct(vector3DU, vector3DV);
		
		return orthonormalBasis33D(vector3DU, vector3DV, vector3DW);
	}
	
//	TODO: Add Javadocs!
	public static double[] disk3DComputeSurfaceNormal(final double[] ray3D, final double[] disk3D, final double t) {
		return disk3DComputeSurfaceNormal(ray3D, disk3D, t, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] disk3DComputeSurfaceNormal(final double[] ray3D, final double[] disk3D, final double t, final int ray3DOffset, final int disk3DOffset) {
		return orthonormalBasis33DGetW(disk3DComputeOrthonormalBasis(ray3D, disk3D, t, ray3DOffset, disk3DOffset));
	}
	
//	TODO: Add Javadocs!
	public static double[] disk3DComputeTextureCoordinates(final double[] ray3D, final double[] disk3D, final double t) {
		return disk3DComputeTextureCoordinates(ray3D, disk3D, t, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] disk3DComputeTextureCoordinates(final double[] ray3D, final double[] disk3D, final double t, final int ray3DOffset, final int disk3DOffset) {
		final double[] point3DOrigin = ray3DGetOrigin(ray3D, point3D(), ray3DOffset, 0);
		
		final double[] vector3DDirection = ray3DGetDirection(ray3D, vector3D(), ray3DOffset, 0);
		
		final double phiMax = disk3DGetPhiMax(disk3D, disk3DOffset);
		final double radiusInner = disk3DGetRadiusInner(disk3D, disk3DOffset);
		final double radiusOuter = disk3DGetRadiusOuter(disk3D, disk3DOffset);
		
		final double[] point3D = point3DAdd(point3DOrigin, vector3DDirection, t);
		
		final double distance = sqrt(point3DGetX(point3D) * point3DGetX(point3D) + point3DGetY(point3D) * point3DGetY(point3D));
		
		final double phi = getOrAdd(atan2(point3DGetY(point3D), point3DGetX(point3D)), 0.0D, PI_MULTIPLIED_BY_2);
		
		final double u = phi / phiMax;
		final double v = (radiusOuter - distance) / (radiusOuter - radiusInner);
		
		return point2D(u, v);
	}
	
//	TODO: Add Javadocs!
	public static double[] disk3DSet(final double[] disk3DResult, final double phiMax, final double radiusInner, final double radiusOuter, final double zMax) {
		return disk3DSet(disk3DResult, phiMax, radiusInner, radiusOuter, zMax, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] disk3DSet(final double[] disk3DResult, final double phiMax, final double radiusInner, final double radiusOuter, final double zMax, final int disk3DResultOffset) {
		disk3DResult[disk3DResultOffset + DISK_OFFSET_ID] = DISK_ID;
		disk3DResult[disk3DResultOffset + DISK_OFFSET_SIZE] = DISK_SIZE;
		disk3DResult[disk3DResultOffset + DISK_OFFSET_PHI_MAX] = phiMax;
		disk3DResult[disk3DResultOffset + DISK_OFFSET_RADIUS_INNER] = radiusInner;
		disk3DResult[disk3DResultOffset + DISK_OFFSET_RADIUS_OUTER] = radiusOuter;
		disk3DResult[disk3DResultOffset + DISK_OFFSET_Z_MAX] = zMax;
		
		return disk3DResult;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	// Paraboloid3D ////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public static double paraboloid3DGetPhiMax(final double[] paraboloid3D) {
		return paraboloid3DGetPhiMax(paraboloid3D, 0);
	}
	
//	TODO: Add Javadocs!
	public static double paraboloid3DGetPhiMax(final double[] paraboloid3D, final int paraboloid3DOffset) {
		return paraboloid3D[paraboloid3DOffset + PARABOLOID_OFFSET_PHI_MAX];
	}
	
//	TODO: Add Javadocs!
	public static double paraboloid3DGetRadius(final double[] paraboloid3D) {
		return paraboloid3DGetRadius(paraboloid3D, 0);
	}
	
//	TODO: Add Javadocs!
	public static double paraboloid3DGetRadius(final double[] paraboloid3D, final int paraboloid3DOffset) {
		return paraboloid3D[paraboloid3DOffset + PARABOLOID_OFFSET_RADIUS];
	}
	
//	TODO: Add Javadocs!
	public static double paraboloid3DGetZMax(final double[] paraboloid3D) {
		return paraboloid3DGetZMax(paraboloid3D, 0);
	}
	
//	TODO: Add Javadocs!
	public static double paraboloid3DGetZMax(final double[] paraboloid3D, final int paraboloid3DOffset) {
		return paraboloid3D[paraboloid3DOffset + PARABOLOID_OFFSET_Z_MAX];
	}
	
//	TODO: Add Javadocs!
	public static double paraboloid3DGetZMin(final double[] paraboloid3D) {
		return paraboloid3DGetZMin(paraboloid3D, 0);
	}
	
//	TODO: Add Javadocs!
	public static double paraboloid3DGetZMin(final double[] paraboloid3D, final int paraboloid3DOffset) {
		return paraboloid3D[paraboloid3DOffset + PARABOLOID_OFFSET_Z_MIN];
	}
	
//	TODO: Add Javadocs!
	public static double paraboloid3DIntersection(final double[] ray3D, final double[] paraboloid3D) {
		return paraboloid3DIntersection(ray3D, paraboloid3D, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double paraboloid3DIntersection(final double[] ray3D, final double[] paraboloid3D, final int ray3DOffset, final int paraboloid3DOffset) {
		final double[] point3DOrigin = ray3DGetOrigin(ray3D, point3D(), ray3DOffset, 0);
		
		final double[] vector3DDirection = ray3DGetDirection(ray3D, vector3D(), ray3DOffset, 0);
		
		final double tMinimum = ray3DGetTMinimum(ray3D, ray3DOffset);
		final double tMaximum = ray3DGetTMaximum(ray3D, ray3DOffset);
		
		final double phiMax = paraboloid3DGetPhiMax(paraboloid3D, paraboloid3DOffset);
		final double radius = paraboloid3DGetRadius(paraboloid3D, paraboloid3DOffset);
		final double zMax = paraboloid3DGetZMax(paraboloid3D, paraboloid3DOffset);
		final double zMin = paraboloid3DGetZMin(paraboloid3D, paraboloid3DOffset);
		
		final double k = zMax / (radius * radius);
		
		final double a = k * (vector3DGetX(vector3DDirection) * vector3DGetX(vector3DDirection) + vector3DGetY(vector3DDirection) * vector3DGetY(vector3DDirection));
		final double b = 2.0D * k * (vector3DGetX(vector3DDirection) * point3DGetX(point3DOrigin) + vector3DGetY(vector3DDirection) * point3DGetY(point3DOrigin)) - vector3DGetZ(vector3DDirection);
		final double c = k * (point3DGetX(point3DOrigin) * point3DGetX(point3DOrigin) + point3DGetY(point3DOrigin) * point3DGetY(point3DOrigin)) - point3DGetZ(point3DOrigin);
		
		final double[] ts = solveQuadraticSystem(a, b, c);
		
		for(int i = 0; i < ts.length; i++) {
			final double t = ts[i];
			
			if(isNaN(t)) {
				return NaN;
			}
			
			if(t > tMinimum && t < tMaximum) {
				final double[] point3D = point3DAdd(point3DOrigin, vector3DDirection, t);
				
				final double phi = getOrAdd(atan2(point3DGetY(point3D), point3DGetX(point3D)), 0.0D, PI_MULTIPLIED_BY_2);
				
				if(point3DGetZ(point3D) >= zMin && point3DGetZ(point3D) <= zMax && phi <= phiMax) {
					return t;
				}
			}
		}
		
		return NaN;
	}
	
//	TODO: Add Javadocs!
	public static double paraboloid3DSurfaceArea(final double[] paraboloid3D) {
		return paraboloid3DSurfaceArea(paraboloid3D, 0);
	}
	
//	TODO: Add Javadocs!
	public static double paraboloid3DSurfaceArea(final double[] paraboloid3D, final int paraboloid3DOffset) {
		final double phiMax = paraboloid3DGetPhiMax(paraboloid3D, paraboloid3DOffset);
		
		final double radius = paraboloid3DGetRadius(paraboloid3D, paraboloid3DOffset);
		final double radiusSquared = radius * radius;
		
		final double zMax = paraboloid3DGetZMax(paraboloid3D, paraboloid3DOffset);
		final double zMin = paraboloid3DGetZMin(paraboloid3D, paraboloid3DOffset);
		
		final double k = 4.0D * zMax / radiusSquared;
		
		final double a = radiusSquared * radiusSquared * phiMax / (12.0D * zMax * zMax);
		final double b = pow(k * zMax + 1.0D, 1.5D) - pow(k * zMin + 1.0D, 1.5D);
		
		final double surfaceArea = a * b;
		
		return surfaceArea;
	}
	
//	TODO: Add Javadocs!
	public static double[] paraboloid3D() {
		return paraboloid3D(toRadians(360.0D), 1.0D, 1.0D, 0.0D);
	}
	
//	TODO: Add Javadocs!
	public static double[] paraboloid3D(final double phiMax, final double radius, final double zMax, final double zMin) {
		return paraboloid3DSet(new double[PARABOLOID_SIZE], phiMax, radius, zMax, zMin);
	}
	
//	TODO: Add Javadocs!
	public static double[] paraboloid3DComputeOrthonormalBasis(final double[] ray3D, final double[] paraboloid3D, final double t) {
		return paraboloid3DComputeOrthonormalBasis(ray3D, paraboloid3D, t, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] paraboloid3DComputeOrthonormalBasis(final double[] ray3D, final double[] paraboloid3D, final double t, final int ray3DOffset, final int paraboloid3DOffset) {
		final double[] point3DOrigin = ray3DGetOrigin(ray3D, point3D(), ray3DOffset, 0);
		
		final double[] vector3DDirection = ray3DGetDirection(ray3D, vector3D(), ray3DOffset, 0);
		
		final double phiMax = paraboloid3DGetPhiMax(paraboloid3D, paraboloid3DOffset);
		final double zMax = paraboloid3DGetZMax(paraboloid3D, paraboloid3DOffset);
		final double zMin = paraboloid3DGetZMin(paraboloid3D, paraboloid3DOffset);
		
		final double[] point3D = point3DAdd(point3DOrigin, vector3DDirection, t);
		
		final double[] vector3DU = vector3DNormalize(vector3D(-phiMax * point3DGetY(point3D), phiMax * point3DGetX(point3D), 0.0D));
		final double[] vector3DV = vector3DNormalize(vector3D((zMax - zMin) * (point3DGetX(point3D) / (2.0D * point3DGetZ(point3D))), (zMax - zMin) * (point3DGetY(point3D) / (2.0D * point3DGetZ(point3D))), zMax - zMin));
		final double[] vector3DW = vector3DCrossProduct(vector3DU, vector3DV);
		
		return orthonormalBasis33D(vector3DU, vector3DV, vector3DW);
	}
	
//	TODO: Add Javadocs!
	public static double[] paraboloid3DComputeSurfaceNormal(final double[] ray3D, final double[] paraboloid3D, final double t) {
		return paraboloid3DComputeSurfaceNormal(ray3D, paraboloid3D, t, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] paraboloid3DComputeSurfaceNormal(final double[] ray3D, final double[] paraboloid3D, final double t, final int ray3DOffset, final int paraboloid3DOffset) {
		return orthonormalBasis33DGetW(paraboloid3DComputeOrthonormalBasis(ray3D, paraboloid3D, t, ray3DOffset, paraboloid3DOffset));
	}
	
//	TODO: Add Javadocs!
	public static double[] paraboloid3DComputeTextureCoordinates(final double[] ray3D, final double[] paraboloid3D, final double t) {
		return paraboloid3DComputeTextureCoordinates(ray3D, paraboloid3D, t, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] paraboloid3DComputeTextureCoordinates(final double[] ray3D, final double[] paraboloid3D, final double t, final int ray3DOffset, final int paraboloid3DOffset) {
		final double[] point3DOrigin = ray3DGetOrigin(ray3D, point3D(), ray3DOffset, 0);
		
		final double[] vector3DDirection = ray3DGetDirection(ray3D, vector3D(), ray3DOffset, 0);
		
		final double phiMax = paraboloid3DGetPhiMax(paraboloid3D, paraboloid3DOffset);
		final double zMax = paraboloid3DGetZMax(paraboloid3D, paraboloid3DOffset);
		final double zMin = paraboloid3DGetZMin(paraboloid3D, paraboloid3DOffset);
		
		final double[] point3D = point3DAdd(point3DOrigin, vector3DDirection, t);
		
		final double phi = getOrAdd(atan2(point3DGetY(point3D), point3DGetX(point3D)), 0.0D, PI_MULTIPLIED_BY_2);
		
		final double u = phi / phiMax;
		final double v = (point3DGetZ(point3D) - zMin) / (zMax - zMin);
		
		return point2D(u, v);
	}
	
//	TODO: Add Javadocs!
	public static double[] paraboloid3DSet(final double[] paraboloid3DResult, final double phiMax, final double radius, final double zMax, final double zMin) {
		return paraboloid3DSet(paraboloid3DResult, phiMax, radius, zMax, zMin, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] paraboloid3DSet(final double[] paraboloid3DResult, final double phiMax, final double radius, final double zMax, final double zMin, final int paraboloid3DResultOffset) {
		paraboloid3DResult[paraboloid3DResultOffset + PARABOLOID_OFFSET_ID] = PARABOLOID_ID;
		paraboloid3DResult[paraboloid3DResultOffset + PARABOLOID_OFFSET_SIZE] = PARABOLOID_SIZE;
		paraboloid3DResult[paraboloid3DResultOffset + PARABOLOID_OFFSET_PHI_MAX] = phiMax;
		paraboloid3DResult[paraboloid3DResultOffset + PARABOLOID_OFFSET_RADIUS] = radius;
		paraboloid3DResult[paraboloid3DResultOffset + PARABOLOID_OFFSET_Z_MAX] = zMax;
		paraboloid3DResult[paraboloid3DResultOffset + PARABOLOID_OFFSET_Z_MIN] = zMin;
		
		return paraboloid3DResult;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	// Plane3D /////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public static boolean plane3DContainsPoint3D(final double[] plane3D, final double[] point3D) {
		return plane3DContainsPoint3D(plane3D, point3D, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static boolean plane3DContainsPoint3D(final double[] plane3D, final double[] point3D, final int plane3DOffset, final int point3DOffset) {
		final double[] point3DA = plane3DGetA(plane3D, point3D(), plane3DOffset, 0);
		final double[] point3DB = plane3DGetB(plane3D, point3D(), plane3DOffset, 0);
		final double[] point3DC = plane3DGetC(plane3D, point3D(), plane3DOffset, 0);
		
		final boolean contains = point3DCoplanar(point3DA, point3DB, point3DC, point3D, 0, 0, 0, point3DOffset);
		
		return contains;
	}
	
//	TODO: Add Javadocs!
	public static double plane3DIntersection(final double[] ray3D, final double[] plane3D) {
		return plane3DIntersection(ray3D, plane3D, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double plane3DIntersection(final double[] ray3D, final double[] plane3D, final int ray3DOffset, final int plane3DOffset) {
		final double[] point3DOrigin = ray3DGetOrigin(ray3D, point3D(), ray3DOffset, 0);
		
		final double[] vector3DDirection = ray3DGetDirection(ray3D, vector3D(), ray3DOffset, 0);
		
		final double tMinimum = ray3DGetTMinimum(ray3D, ray3DOffset);
		final double tMaximum = ray3DGetTMaximum(ray3D, ray3DOffset);
		
		final double[] point3DA = plane3DGetA(plane3D, point3D(), plane3DOffset, 0);
		final double[] point3DB = plane3DGetB(plane3D, point3D(), plane3DOffset, 0);
		final double[] point3DC = plane3DGetC(plane3D, point3D(), plane3DOffset, 0);
		
		final double[] vector3DAB = vector3DDirectionNormalized(point3DA, point3DB);
		final double[] vector3DAC = vector3DDirectionNormalized(point3DA, point3DC);
		
		final double[] vector3DSurfaceNormal = vector3DNormalize(vector3DCrossProduct(vector3DAB, vector3DAC));
		
		final double surfaceNormalDotDirection = vector3DDotProduct(vector3DSurfaceNormal, vector3DDirection);
		
		if(isZero(surfaceNormalDotDirection)) {
			return NaN;
		}
		
		final double[] vector3DOriginToA = vector3DDirection(point3DOrigin, point3DA);
		
		final double t = vector3DDotProduct(vector3DOriginToA, vector3DSurfaceNormal) / surfaceNormalDotDirection;
		
		if(t > tMinimum && t < tMaximum) {
			return t;
		}
		
		return NaN;
	}
	
//	TODO: Add Javadocs!
	public static double[] plane3D() {
		return plane3D(point3D(0.0D, 0.0D, 0.0D), point3D(0.0D, 0.0D, 1.0D), point3D(1.0D, 0.0D, 0.0D));
	}
	
//	TODO: Add Javadocs!
	public static double[] plane3D(final double[] point3DA, final double[] point3DB, final double[] point3DC) {
		return plane3D(point3DA, point3DB, point3DC, 0, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] plane3D(final double[] point3DA, final double[] point3DB, final double[] point3DC, final int point3DAOffset, final int point3DBOffset, final int point3DCOffset) {
		return plane3DSet(new double[PLANE_SIZE], point3DA, point3DB, point3DC, 0, point3DAOffset, point3DBOffset, point3DCOffset);
	}
	
//	TODO: Add Javadocs!
	public static double[] plane3DComputeOrthonormalBasis(final double[] ray3D, final double[] plane3D, final double t) {
		return plane3DComputeOrthonormalBasis(ray3D, plane3D, t, 0, 0);
	}
	
//	TODO: Add Javadocs!
	@SuppressWarnings("unused")
	public static double[] plane3DComputeOrthonormalBasis(final double[] ray3D, final double[] plane3D, final double t, final int ray3DOffset, final int plane3DOffset) {
		final double[] point3DA = plane3DGetA(plane3D, point3D(), plane3DOffset, 0);
		final double[] point3DB = plane3DGetB(plane3D, point3D(), plane3DOffset, 0);
		final double[] point3DC = plane3DGetC(plane3D, point3D(), plane3DOffset, 0);
		
		final double[] vector3DAB = vector3DDirectionNormalized(point3DA, point3DB);
		final double[] vector3DAC = vector3DDirectionNormalized(point3DA, point3DC);
		
		final double[] vector3DW = vector3DCrossProduct(vector3DAB, vector3DAC);
		
		return orthonormalBasis33DFromW(vector3DW);
	}
	
//	TODO: Add Javadocs!
	public static double[] plane3DComputeSurfaceNormal(final double[] ray3D, final double[] plane3D, final double t) {
		return plane3DComputeSurfaceNormal(ray3D, plane3D, t, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] plane3DComputeSurfaceNormal(final double[] ray3D, final double[] plane3D, final double t, final int ray3DOffset, final int plane3DOffset) {
		return orthonormalBasis33DGetW(plane3DComputeOrthonormalBasis(ray3D, plane3D, t, ray3DOffset, plane3DOffset));
	}
	
//	TODO: Add Javadocs!
	public static double[] plane3DComputeTextureCoordinates(final double[] ray3D, final double[] plane3D, final double t) {
		return plane3DComputeTextureCoordinates(ray3D, plane3D, t, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] plane3DComputeTextureCoordinates(final double[] ray3D, final double[] plane3D, final double t, final int ray3DOffset, final int plane3DOffset) {
		final double[] point3DOrigin = ray3DGetOrigin(ray3D, point3D(), ray3DOffset, 0);
		
		final double[] vector3DDirection = ray3DGetDirection(ray3D, vector3D(), ray3DOffset, 0);
		
		final double[] point3DA = plane3DGetA(plane3D, point3D(), plane3DOffset, 0);
		final double[] point3DB = plane3DGetB(plane3D, point3D(), plane3DOffset, 0);
		final double[] point3DC = plane3DGetC(plane3D, point3D(), plane3DOffset, 0);
		
		final double[] vector3DSurfaceNormal = plane3DComputeSurfaceNormal(ray3D, plane3D, t, ray3DOffset, plane3DOffset);
		
		final double[] point3D = point3DAdd(point3DOrigin, vector3DDirection, t);
		
		final boolean isX = abs(vector3DGetX(vector3DSurfaceNormal)) > abs(vector3DGetY(vector3DSurfaceNormal)) && abs(vector3DGetX(vector3DSurfaceNormal)) > abs(vector3DGetZ(vector3DSurfaceNormal));
		final boolean isY = abs(vector3DGetY(vector3DSurfaceNormal)) > abs(vector3DGetZ(vector3DSurfaceNormal));
		
		final double[] vector2DA = isX ? vector2DDirectionYZ(point3DA) : isY ? vector2DDirectionZX(point3DA) : vector2DDirectionXY(point3DA);
		final double[] vector2DB = isX ? vector2DDirectionYZ(point3DC) : isY ? vector2DDirectionZX(point3DC) : vector2DDirectionXY(point3DC);
		final double[] vector2DC = isX ? vector2DDirectionYZ(point3DB) : isY ? vector2DDirectionZX(point3DB) : vector2DDirectionXY(point3DB);
		
		final double[] vector2DAB = vector2DSubtract(vector2DB, vector2DA);
		final double[] vector2DAC = vector2DSubtract(vector2DC, vector2DA);
		
		final double determinant = vector2DCrossProduct(vector2DAB, vector2DAC);
		final double determinantReciprocal = 1.0D / determinant;
		
		final double hU = isX ? point3DGetY(point3D) : isY ? point3DGetZ(point3D) : point3DGetX(point3D);
		final double hV = isX ? point3DGetZ(point3D) : isY ? point3DGetX(point3D) : point3DGetY(point3D);
		
		final double u = hU * (-vector2DGetY(vector2DAB) * determinantReciprocal) + hV * (+vector2DGetX(vector2DAB) * determinantReciprocal) + vector2DCrossProduct(vector2DA, vector2DAB) * determinantReciprocal;
		final double v = hU * (+vector2DGetY(vector2DAC) * determinantReciprocal) + hV * (-vector2DGetX(vector2DAC) * determinantReciprocal) + vector2DCrossProduct(vector2DAC, vector2DA) * determinantReciprocal;
		
		return point2D(u, v);
	}
	
//	TODO: Add Javadocs!
	public static double[] plane3DGetA(final double[] plane3D) {
		return plane3DGetA(plane3D, point3D());
	}
	
//	TODO: Add Javadocs!
	public static double[] plane3DGetA(final double[] plane3D, final double[] point3DAResult) {
		return plane3DGetA(plane3D, point3DAResult, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] plane3DGetA(final double[] plane3D, final double[] point3DAResult, final int plane3DOffset, final int point3DAResultOffset) {
		return point3DSet(point3DAResult, plane3D, point3DAResultOffset, plane3DOffset + PLANE_OFFSET_A);
	}
	
//	TODO: Add Javadocs!
	public static double[] plane3DGetB(final double[] plane3D) {
		return plane3DGetB(plane3D, point3D());
	}
	
//	TODO: Add Javadocs!
	public static double[] plane3DGetB(final double[] plane3D, final double[] point3DBResult) {
		return plane3DGetB(plane3D, point3DBResult);
	}
	
//	TODO: Add Javadocs!
	public static double[] plane3DGetB(final double[] plane3D, final double[] point3DBResult, final int plane3DOffset, final int point3DBResultOffset) {
		return point3DSet(point3DBResult, plane3D, point3DBResultOffset, plane3DOffset + PLANE_OFFSET_B);
	}
	
//	TODO: Add Javadocs!
	public static double[] plane3DGetC(final double[] plane3D) {
		return plane3DGetC(plane3D, point3D());
	}
	
//	TODO: Add Javadocs!
	public static double[] plane3DGetC(final double[] plane3D, final double[] point3DCResult) {
		return plane3DGetC(plane3D, point3DCResult, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] plane3DGetC(final double[] plane3D, final double[] point3DCResult, final int plane3DOffset, final int point3DCResultOffset) {
		return point3DSet(point3DCResult, plane3D, point3DCResultOffset, plane3DOffset + PLANE_OFFSET_C);
	}
	
//	TODO: Add Javadocs!
	public static double[] plane3DSet(final double[] plane3DResult, final double[] point3DA, final double[] point3DB, final double[] point3DC) {
		return plane3DSet(plane3DResult, point3DA, point3DB, point3DC, 0, 0, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] plane3DSet(final double[] plane3DResult, final double[] point3DA, final double[] point3DB, final double[] point3DC, final int plane3DResultOffset, final int point3DAOffset, final int point3DBOffset, final int point3DCOffset) {
		plane3DResult[plane3DResultOffset + PLANE_OFFSET_ID] = PLANE_ID;
		plane3DResult[plane3DResultOffset + PLANE_OFFSET_SIZE] = PLANE_SIZE;
		
		point3DSet(plane3DResult, point3DA, plane3DResultOffset + PLANE_OFFSET_A, point3DAOffset);
		point3DSet(plane3DResult, point3DB, plane3DResultOffset + PLANE_OFFSET_B, point3DBOffset);
		point3DSet(plane3DResult, point3DC, plane3DResultOffset + PLANE_OFFSET_C, point3DCOffset);
		
		return plane3DResult;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	// Rectangle3D /////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public static boolean rectangle3DContainsPoint3D(final double[] rectangle3D, final double[] point3D) {
		return rectangle3DContainsPoint3D(rectangle3D, point3D, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static boolean rectangle3DContainsPoint3D(final double[] rectangle3D, final double[] point3D, final int rectangle3DOffset, final int point3DOffset) {
		final double[] point3DA = rectangle3DGetA(rectangle3D, point3D(), rectangle3DOffset, 0);
		final double[] point3DB = rectangle3DGetB(rectangle3D, point3D(), rectangle3DOffset, 0);
		final double[] point3DC = rectangle3DGetC(rectangle3D, point3D(), rectangle3DOffset, 0);
		final double[] point3DP = point3DSet(point3D(), point3D, 0, point3DOffset);
		
		final boolean containsPlane = point3DCoplanar(point3DA, point3DB, point3DC, point3DP);
		
		if(containsPlane) {
			return false;
		}
		
		final double[] vector3DEdgeAB = vector3DDirection(point3DA, point3DB);
		final double[] vector3DEdgeBC = vector3DDirection(point3DB, point3DC);
		final double[] vector3DEdgeAP = vector3DDirection(point3DA, point3DP);
		
		final double dotProductAPAB = vector3DDotProduct(vector3DEdgeAP, vector3DNormalize(vector3DEdgeAB));
		final double dotProductAPBC = vector3DDotProduct(vector3DEdgeAP, vector3DNormalize(vector3DEdgeBC));
		
		final boolean containsAPAB = dotProductAPAB >= 0.0D && dotProductAPAB <= vector3DLength(vector3DEdgeAB);
		final boolean containsAPBC = dotProductAPBC >= 0.0D && dotProductAPBC <= vector3DLength(vector3DEdgeBC);
		final boolean contains = containsAPAB && containsAPBC;
		
		return contains;
	}
	
//	TODO: Add Javadocs!
	public static double rectangle3DIntersection(final double[] ray3D, final double[] rectangle3D) {
		return rectangle3DIntersection(ray3D, rectangle3D, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double rectangle3DIntersection(final double[] ray3D, final double[] rectangle3D, final int ray3DOffset, final int rectangle3DOffset) {
		final double[] point3DOrigin = ray3DGetOrigin(ray3D, point3D(), ray3DOffset, 0);
		
		final double[] vector3DDirection = ray3DGetDirection(ray3D, vector3D(), ray3DOffset, 0);
		
		final double tMinimum = ray3DGetTMinimum(ray3D, ray3DOffset);
		final double tMaximum = ray3DGetTMaximum(ray3D, ray3DOffset);
		
		final double[] point3DA = rectangle3DGetA(rectangle3D, point3D(), rectangle3DOffset, 0);
		final double[] point3DB = rectangle3DGetB(rectangle3D, point3D(), rectangle3DOffset, 0);
		final double[] point3DC = rectangle3DGetC(rectangle3D, point3D(), rectangle3DOffset, 0);
		
		final double[] vector3DAB = vector3DDirectionNormalized(point3DA, point3DB);
		final double[] vector3DAC = vector3DDirectionNormalized(point3DA, point3DC);
		
		final double[] vector3DSurfaceNormal = vector3DNormalize(vector3DCrossProduct(vector3DAB, vector3DAC));
		
		final double surfaceNormalDotDirection = vector3DDotProduct(vector3DSurfaceNormal, vector3DDirection);
		
		if(isZero(surfaceNormalDotDirection)) {
			return NaN;
		}
		
		final double[] vector3DOriginToA = vector3DDirection(point3DOrigin, point3DA);
		
		final double t = vector3DDotProduct(vector3DOriginToA, vector3DSurfaceNormal) / surfaceNormalDotDirection;
		
		if(t <= tMinimum || t >= tMaximum) {
			return NaN;
		}
		
		final double[] point3DP = point3DAdd(point3DOrigin, vector3DDirection, t);
		
		final double[] vector3DEdgeAB = vector3DDirection(point3DA, point3DB);
		final double[] vector3DEdgeBC = vector3DDirection(point3DB, point3DC);
		final double[] vector3DEdgeAP = vector3DDirection(point3DA, point3DP);
		
		final double dotProductAPAB = vector3DDotProduct(vector3DEdgeAP, vector3DNormalize(vector3DEdgeAB));
		final double dotProductAPBC = vector3DDotProduct(vector3DEdgeAP, vector3DNormalize(vector3DEdgeBC));
		
		final boolean containsAPAB = dotProductAPAB >= 0.0D && dotProductAPAB <= vector3DLength(vector3DEdgeAB);
		final boolean containsAPBC = dotProductAPBC >= 0.0D && dotProductAPBC <= vector3DLength(vector3DEdgeBC);
		final boolean contains = containsAPAB && containsAPBC;
		
		if(contains) {
			return t;
		}
		
		return NaN;
	}
	
//	TODO: Add Javadocs!
	public static double rectangle3DSurfaceArea(final double[] rectangle3D) {
		return rectangle3DSurfaceArea(rectangle3D, 0);
	}
	
//	TODO: Add Javadocs!
	public static double rectangle3DSurfaceArea(final double[] rectangle3D, final int rectangle3DOffset) {
		final double[] point3DA = rectangle3DGetA(rectangle3D, point3D(), rectangle3DOffset, 0);
		final double[] point3DB = rectangle3DGetB(rectangle3D, point3D(), rectangle3DOffset, 0);
		final double[] point3DC = rectangle3DGetC(rectangle3D, point3D(), rectangle3DOffset, 0);
		
		final double[] vector3DEdgeAB = vector3DDirection(point3DA, point3DB);
		final double[] vector3DEdgeBC = vector3DDirection(point3DB, point3DC);
		final double[] vector3DEdgeABCrossEdgeBC = vector3DCrossProduct(vector3DEdgeAB, vector3DEdgeBC);
		
		final double surfaceArea = vector3DLength(vector3DEdgeABCrossEdgeBC);
		
		return surfaceArea;
	}
	
//	TODO: Add Javadocs!
	public static double[] rectangle3D() {
		return rectangle3D(point3D(-1.0D, 1.0D, 0.0D), point3D(1.0D, 1.0D, 0.0D), point3D(1.0D, -1.0D, 0.0D), point3D(-1.0D, -1.0D, 0.0D));
	}
	
//	TODO: Add Javadocs!
	public static double[] rectangle3D(final double[] point3DA, final double[] point3DB, final double[] point3DC, final double[] point3DD) {
		return rectangle3D(point3DA, point3DB, point3DC, point3DD, 0, 0, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] rectangle3D(final double[] point3DA, final double[] point3DB, final double[] point3DC, final double[] point3DD, final int point3DAOffset, final int point3DBOffset, final int point3DCOffset, final int point3DDOffset) {
		return rectangle3DSet(new double[RECTANGLE_SIZE], point3DA, point3DB, point3DC, point3DD, 0, point3DAOffset, point3DBOffset, point3DCOffset, point3DDOffset);
	}
	
//	TODO: Add Javadocs!
	public static double[] rectangle3DComputeOrthonormalBasis(final double[] ray3D, final double[] rectangle3D, final double t) {
		return rectangle3DComputeOrthonormalBasis(ray3D, rectangle3D, t, 0, 0);
	}
	
//	TODO: Add Javadocs!
	@SuppressWarnings("unused")
	public static double[] rectangle3DComputeOrthonormalBasis(final double[] ray3D, final double[] rectangle3D, final double t, final int ray3DOffset, final int rectangle3DOffset) {
		final double[] point3DA = rectangle3DGetA(rectangle3D, point3D(), rectangle3DOffset, 0);
		final double[] point3DB = rectangle3DGetB(rectangle3D, point3D(), rectangle3DOffset, 0);
		final double[] point3DC = rectangle3DGetC(rectangle3D, point3D(), rectangle3DOffset, 0);
		
		final double[] vector3DAB = vector3DDirectionNormalized(point3DA, point3DB);
		final double[] vector3DAC = vector3DDirectionNormalized(point3DA, point3DC);
		
		final double[] vector3DW = vector3DCrossProduct(vector3DAB, vector3DAC);
		
		return orthonormalBasis33DFromW(vector3DW);
	}
	
//	TODO: Add Javadocs!
	public static double[] rectangle3DComputeSurfaceNormal(final double[] ray3D, final double[] rectangle3D, final double t) {
		return rectangle3DComputeSurfaceNormal(ray3D, rectangle3D, t, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] rectangle3DComputeSurfaceNormal(final double[] ray3D, final double[] rectangle3D, final double t, final int ray3DOffset, final int rectangle3DOffset) {
		return orthonormalBasis33DGetW(rectangle3DComputeOrthonormalBasis(ray3D, rectangle3D, t, ray3DOffset, rectangle3DOffset));
	}
	
//	TODO: Add Javadocs!
	public static double[] rectangle3DComputeTextureCoordinates(final double[] ray3D, final double[] rectangle3D, final double t) {
		return rectangle3DComputeTextureCoordinates(ray3D, rectangle3D, t, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] rectangle3DComputeTextureCoordinates(final double[] ray3D, final double[] rectangle3D, final double t, final int ray3DOffset, final int rectangle3DOffset) {
		final double[] point3DOrigin = ray3DGetOrigin(ray3D, point3D(), ray3DOffset, 0);
		
		final double[] vector3DDirection = ray3DGetDirection(ray3D, vector3D(), ray3DOffset, 0);
		
		final double[] point3DA = rectangle3DGetA(rectangle3D, point3D(), rectangle3DOffset, 0);
		final double[] point3DB = rectangle3DGetB(rectangle3D, point3D(), rectangle3DOffset, 0);
		final double[] point3DD = rectangle3DGetD(rectangle3D, point3D(), rectangle3DOffset, 0);
		
		final double[] vector3DSurfaceNormal = rectangle3DComputeSurfaceNormal(ray3D, rectangle3D, t, ray3DOffset, rectangle3DOffset);
		
		final double[] point3D = point3DAdd(point3DOrigin, vector3DDirection, t);
		
		final boolean isX = abs(vector3DGetX(vector3DSurfaceNormal)) > abs(vector3DGetY(vector3DSurfaceNormal)) && abs(vector3DGetX(vector3DSurfaceNormal)) > abs(vector3DGetZ(vector3DSurfaceNormal));
		final boolean isY = abs(vector3DGetY(vector3DSurfaceNormal)) > abs(vector3DGetZ(vector3DSurfaceNormal));
		
		final double[] vector2DA = isX ? vector2DDirectionYZ(point3DA) : isY ? vector2DDirectionZX(point3DA) : vector2DDirectionXY(point3DA);
		final double[] vector2DB = isX ? vector2DDirectionYZ(point3DD) : isY ? vector2DDirectionZX(point3DD) : vector2DDirectionXY(point3DD);
		final double[] vector2DC = isX ? vector2DDirectionYZ(point3DB) : isY ? vector2DDirectionZX(point3DB) : vector2DDirectionXY(point3DB);
		
		final double[] vector2DAB = vector2DSubtract(vector2DB, vector2DA);
		final double[] vector2DAC = vector2DSubtract(vector2DC, vector2DA);
		
		final double determinant = vector2DCrossProduct(vector2DAB, vector2DAC);
		final double determinantReciprocal = 1.0D / determinant;
		
		final double hU = isX ? point3DGetY(point3D) : isY ? point3DGetZ(point3D) : point3DGetX(point3D);
		final double hV = isX ? point3DGetZ(point3D) : isY ? point3DGetX(point3D) : point3DGetY(point3D);
		
		final double u = hU * (-vector2DGetY(vector2DAB) * determinantReciprocal) + hV * (+vector2DGetX(vector2DAB) * determinantReciprocal) + vector2DCrossProduct(vector2DA, vector2DAB) * determinantReciprocal;
		final double v = hU * (+vector2DGetY(vector2DAC) * determinantReciprocal) + hV * (-vector2DGetX(vector2DAC) * determinantReciprocal) + vector2DCrossProduct(vector2DAC, vector2DA) * determinantReciprocal;
		
		return point2D(u, v);
	}
	
//	TODO: Add Javadocs!
	public static double[] rectangle3DGetA(final double[] rectangle3D) {
		return rectangle3DGetA(rectangle3D, point3D());
	}
	
//	TODO: Add Javadocs!
	public static double[] rectangle3DGetA(final double[] rectangle3D, final double[] point3DAResult) {
		return rectangle3DGetA(rectangle3D, point3DAResult, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] rectangle3DGetA(final double[] rectangle3D, final double[] point3DAResult, final int rectangle3DOffset, final int point3DAResultOffset) {
		return point3DSet(point3DAResult, rectangle3D, point3DAResultOffset, rectangle3DOffset + RECTANGLE_OFFSET_A);
	}
	
//	TODO: Add Javadocs!
	public static double[] rectangle3DGetB(final double[] rectangle3D) {
		return rectangle3DGetB(rectangle3D, point3D());
	}
	
//	TODO: Add Javadocs!
	public static double[] rectangle3DGetB(final double[] rectangle3D, final double[] point3DBResult) {
		return rectangle3DGetB(rectangle3D, point3DBResult);
	}
	
//	TODO: Add Javadocs!
	public static double[] rectangle3DGetB(final double[] rectangle3D, final double[] point3DBResult, final int rectangle3DOffset, final int point3DBResultOffset) {
		return point3DSet(point3DBResult, rectangle3D, point3DBResultOffset, rectangle3DOffset + RECTANGLE_OFFSET_B);
	}
	
//	TODO: Add Javadocs!
	public static double[] rectangle3DGetC(final double[] rectangle3D) {
		return rectangle3DGetC(rectangle3D, point3D());
	}
	
//	TODO: Add Javadocs!
	public static double[] rectangle3DGetC(final double[] rectangle3D, final double[] point3DCResult) {
		return rectangle3DGetC(rectangle3D, point3DCResult, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] rectangle3DGetC(final double[] rectangle3D, final double[] point3DCResult, final int rectangle3DOffset, final int point3DCResultOffset) {
		return point3DSet(point3DCResult, rectangle3D, point3DCResultOffset, rectangle3DOffset + RECTANGLE_OFFSET_C);
	}
	
//	TODO: Add Javadocs!
	public static double[] rectangle3DGetD(final double[] rectangle3D) {
		return rectangle3DGetD(rectangle3D, point3D());
	}
	
//	TODO: Add Javadocs!
	public static double[] rectangle3DGetD(final double[] rectangle3D, final double[] point3DDResult) {
		return rectangle3DGetD(rectangle3D, point3DDResult, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] rectangle3DGetD(final double[] rectangle3D, final double[] point3DDResult, final int rectangle3DOffset, final int point3DDResultOffset) {
		return point3DSet(point3DDResult, rectangle3D, point3DDResultOffset, rectangle3DOffset + RECTANGLE_OFFSET_D);
	}
	
//	TODO: Add Javadocs!
	public static double[] rectangle3DSet(final double[] rectangle3DResult, final double[] point3DA, final double[] point3DB, final double[] point3DC, final double[] point3DD) {
		return rectangle3DSet(rectangle3DResult, point3DA, point3DB, point3DC, point3DD, 0, 0, 0, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] rectangle3DSet(final double[] rectangle3DResult, final double[] point3DA, final double[] point3DB, final double[] point3DC, final double[] point3DD, final int rectangle3DResultOffset, final int point3DAOffset, final int point3DBOffset, final int point3DCOffset, final int point3DDOffset) {
		rectangle3DResult[rectangle3DResultOffset + RECTANGLE_OFFSET_ID] = RECTANGLE_ID;
		rectangle3DResult[rectangle3DResultOffset + RECTANGLE_OFFSET_SIZE] = RECTANGLE_SIZE;
		
		point3DSet(rectangle3DResult, point3DA, rectangle3DResultOffset + RECTANGLE_OFFSET_A, point3DAOffset);
		point3DSet(rectangle3DResult, point3DB, rectangle3DResultOffset + RECTANGLE_OFFSET_B, point3DBOffset);
		point3DSet(rectangle3DResult, point3DC, rectangle3DResultOffset + RECTANGLE_OFFSET_C, point3DCOffset);
		point3DSet(rectangle3DResult, point3DD, rectangle3DResultOffset + RECTANGLE_OFFSET_D, point3DDOffset);
		
		return rectangle3DResult;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	// RectangularCuboid3D /////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public static boolean rectangularCuboid3DContainsPoint3D(final double[] rectangularCuboid3D, final double[] point3D) {
		return rectangularCuboid3DContainsPoint3D(rectangularCuboid3D, point3D, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static boolean rectangularCuboid3DContainsPoint3D(final double[] rectangularCuboid3D, final double[] point3D, final int rectangularCuboid3DOffset, final int point3DOffset) {
		final double[] point3DMaximum = rectangularCuboid3DGetMaximum(rectangularCuboid3D, point3D(), rectangularCuboid3DOffset, 0);
		final double[] point3DMinimum = rectangularCuboid3DGetMinimum(rectangularCuboid3D, point3D(), rectangularCuboid3DOffset, 0);
		
		final boolean containsX = point3DGetX(point3D, point3DOffset) >= point3DGetX(point3DMinimum) && point3DGetX(point3D, point3DOffset) <= point3DGetX(point3DMaximum);
		final boolean containsY = point3DGetY(point3D, point3DOffset) >= point3DGetY(point3DMinimum) && point3DGetY(point3D, point3DOffset) <= point3DGetY(point3DMaximum);
		final boolean containsZ = point3DGetZ(point3D, point3DOffset) >= point3DGetZ(point3DMinimum) && point3DGetZ(point3D, point3DOffset) <= point3DGetZ(point3DMaximum);
		final boolean contains = containsX && containsY && containsZ;
		
		return contains;
	}
	
//	TODO: Add Javadocs!
	public static double rectangularCuboid3DIntersection(final double[] ray3D, final double[] rectangularCuboid3D) {
		return rectangularCuboid3DIntersection(ray3D, rectangularCuboid3D, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double rectangularCuboid3DIntersection(final double[] ray3D, final double[] rectangularCuboid3D, final int ray3DOffset, final int rectangularCuboid3DOffset) {
		final double[] point3DOrigin = ray3DGetOrigin(ray3D, point3D(), ray3DOffset, 0);
		
		final double[] vector3DDirection = ray3DGetDirection(ray3D, vector3D(), ray3DOffset, 0);
		final double[] vector3DDirectionReciprocal = vector3DReciprocal(vector3DDirection);
		
		final double tMinimum = ray3DGetTMinimum(ray3D, ray3DOffset);
		final double tMaximum = ray3DGetTMaximum(ray3D, ray3DOffset);
		
		final double[] point3DMaximum = rectangularCuboid3DGetMaximum(rectangularCuboid3D, point3D(), rectangularCuboid3DOffset, 0);
		final double[] point3DMinimum = rectangularCuboid3DGetMinimum(rectangularCuboid3D, point3D(), rectangularCuboid3DOffset, 0);
		
		final double[] vector3DOriginToMaximum = vector3DDirection(point3DOrigin, point3DMaximum);
		final double[] vector3DOriginToMinimum = vector3DDirection(point3DOrigin, point3DMinimum);
		
		final double t0X = vector3DGetX(vector3DOriginToMinimum) * vector3DGetX(vector3DDirectionReciprocal);
		final double t0Y = vector3DGetY(vector3DOriginToMinimum) * vector3DGetY(vector3DDirectionReciprocal);
		final double t0Z = vector3DGetZ(vector3DOriginToMinimum) * vector3DGetZ(vector3DDirectionReciprocal);
		final double t1X = vector3DGetX(vector3DOriginToMaximum) * vector3DGetX(vector3DDirectionReciprocal);
		final double t1Y = vector3DGetY(vector3DOriginToMaximum) * vector3DGetY(vector3DDirectionReciprocal);
		final double t1Z = vector3DGetZ(vector3DOriginToMaximum) * vector3DGetZ(vector3DDirectionReciprocal);
		
		final double t0 = max(min(t0X, t1X), min(t0Y, t1Y), min(t0Z, t1Z));
		final double t1 = min(max(t0X, t1X), max(t0Y, t1Y), max(t0Z, t1Z));
		
		if(t0 > t1) {
			return NaN;
		}
		
		if(t0 > tMinimum && t0 < tMaximum) {
			return t0;
		}
		
		if(t1 > tMinimum && t1 < tMaximum) {
			return t1;
		}
		
		return NaN;
	}
	
//	TODO: Add Javadocs!
	public static double rectangularCuboid3DSurfaceArea(final double[] rectangularCuboid3D) {
		return rectangularCuboid3DSurfaceArea(rectangularCuboid3D, 0);
	}
	
//	TODO: Add Javadocs!
	public static double rectangularCuboid3DSurfaceArea(final double[] rectangularCuboid3D, final int rectangularCuboid3DOffset) {
		final double[] point3DMaximum = rectangularCuboid3DGetMaximum(rectangularCuboid3D, point3D(), rectangularCuboid3DOffset, 0);
		final double[] point3DMinimum = rectangularCuboid3DGetMinimum(rectangularCuboid3D, point3D(), rectangularCuboid3DOffset, 0);
		
		final double[] vector3DDirection = vector3DDirection(point3DMinimum, point3DMaximum);
		
		final double x = vector3DGetX(vector3DDirection);
		final double y = vector3DGetY(vector3DDirection);
		final double z = vector3DGetZ(vector3DDirection);
		
		final double surfaceArea = 2.0D * (x * y + y * z + z * x);
		
		return surfaceArea;
	}
	
//	TODO: Add Javadocs!
	public static double[] rectangularCuboid3D() {
		return rectangularCuboid3D(point3D(-1.0D, -1.0D, -1.0D), point3D(1.0D, 1.0D, 1.0D));
	}
	
//	TODO: Add Javadocs!
	public static double[] rectangularCuboid3D(final double[] point3DA, final double[] point3DB) {
		return rectangularCuboid3D(point3DA, point3DB, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] rectangularCuboid3D(final double[] point3DA, final double[] point3DB, final int point3DAOffset, final int point3DBOffset) {
		return rectangularCuboid3DSet(new double[RECTANGULAR_CUBOID_SIZE], point3DA, point3DB, 0, point3DAOffset, point3DBOffset);
	}
	
//	TODO: Add Javadocs!
	public static double[] rectangularCuboid3DComputeOrthonormalBasis(final double[] ray3D, final double[] rectangularCuboid3D, final double t) {
		return rectangularCuboid3DComputeOrthonormalBasis(ray3D, rectangularCuboid3D, t, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] rectangularCuboid3DComputeOrthonormalBasis(final double[] ray3D, final double[] rectangularCuboid3D, final double t, final int ray3DOffset, final int rectangularCuboid3DOffset) {
		final double[] point3DOrigin = ray3DGetOrigin(ray3D, point3D(), ray3DOffset, 0);
		
		final double[] vector3DDirection = ray3DGetDirection(ray3D, vector3D(), ray3DOffset, 0);
		
		final double[] point3DMaximum = rectangularCuboid3DGetMaximum(rectangularCuboid3D, point3D(), rectangularCuboid3DOffset, 0);
		final double[] point3DMinimum = rectangularCuboid3DGetMinimum(rectangularCuboid3D, point3D(), rectangularCuboid3DOffset, 0);
		
		final double[] point3D = point3DAdd(point3DOrigin, vector3DDirection, t);
		
		final int face = doRectangularCuboid3DComputeFace(point3D, point3DMaximum, point3DMinimum);
		
		switch(face) {
			case 1:
				return orthonormalBasis33DFromW(vector3D(-1.0D, +0.0D, +0.0D));
			case 2:
				return orthonormalBasis33DFromW(vector3D(+1.0D, +0.0D, +0.0D));
			case 3:
				return orthonormalBasis33DFromW(vector3D(+0.0D, -1.0D, +0.0D));
			case 4:
				return orthonormalBasis33DFromW(vector3D(+0.0D, +1.0D, +0.0D));
			case 5:
				return orthonormalBasis33DFromW(vector3D(+0.0D, +0.0D, -1.0D));
			case 6:
				return orthonormalBasis33DFromW(vector3D(+0.0D, +0.0D, +1.0D));
			default:
				return orthonormalBasis33D();
		}
	}
	
//	TODO: Add Javadocs!
	public static double[] rectangularCuboid3DComputeSurfaceNormal(final double[] ray3D, final double[] rectangularCuboid3D, final double t) {
		return rectangularCuboid3DComputeSurfaceNormal(ray3D, rectangularCuboid3D, t, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] rectangularCuboid3DComputeSurfaceNormal(final double[] ray3D, final double[] rectangularCuboid3D, final double t, final int ray3DOffset, final int rectangularCuboid3DOffset) {
		return orthonormalBasis33DGetW(rectangularCuboid3DComputeOrthonormalBasis(ray3D, rectangularCuboid3D, t, ray3DOffset, rectangularCuboid3DOffset));
	}
	
//	TODO: Add Javadocs!
	public static double[] rectangularCuboid3DComputeTextureCoordinates(final double[] ray3D, final double[] rectangularCuboid3D, final double t) {
		return rectangularCuboid3DComputeTextureCoordinates(ray3D, rectangularCuboid3D, t, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] rectangularCuboid3DComputeTextureCoordinates(final double[] ray3D, final double[] rectangularCuboid3D, final double t, final int ray3DOffset, final int rectangularCuboid3DOffset) {
		final double[] point3DOrigin = ray3DGetOrigin(ray3D, point3D(), ray3DOffset, 0);
		
		final double[] vector3DDirection = ray3DGetDirection(ray3D, vector3D(), ray3DOffset, 0);
		
		final double[] point3DMaximum = rectangularCuboid3DGetMaximum(rectangularCuboid3D, point3D(), rectangularCuboid3DOffset, 0);
		final double[] point3DMinimum = rectangularCuboid3DGetMinimum(rectangularCuboid3D, point3D(), rectangularCuboid3DOffset, 0);
		
		final double[] point3D = point3DAdd(point3DOrigin, vector3DDirection, t);
		
		final int face = doRectangularCuboid3DComputeFace(point3D, point3DMaximum, point3DMinimum);
		
		switch(face) {
			case 1:
			case 2:
				return point2D(normalize(point3DGetZ(point3D), point3DGetZ(point3DMinimum), point3DGetZ(point3DMaximum)), normalize(point3DGetY(point3D), point3DGetY(point3DMinimum), point3DGetY(point3DMaximum)));
			case 3:
			case 4:
				return point2D(normalize(point3DGetX(point3D), point3DGetX(point3DMinimum), point3DGetX(point3DMaximum)), normalize(point3DGetZ(point3D), point3DGetZ(point3DMinimum), point3DGetZ(point3DMaximum)));
			case 5:
			case 6:
				return point2D(normalize(point3DGetX(point3D), point3DGetX(point3DMinimum), point3DGetX(point3DMaximum)), normalize(point3DGetY(point3D), point3DGetY(point3DMinimum), point3DGetY(point3DMaximum)));
			default:
				return point2D(0.5D, 0.5D);
		}
	}
	
//	TODO: Add Javadocs!
	public static double[] rectangularCuboid3DGetMaximum(final double[] rectangularCuboid3D, final double[] point3DMaximumResult) {
		return rectangularCuboid3DGetMaximum(rectangularCuboid3D, point3DMaximumResult, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] rectangularCuboid3DGetMaximum(final double[] rectangularCuboid3D, final double[] point3DMaximumResult, final int rectangularCuboid3DOffset, final int point3DMaximumResultOffset) {
		return point3DSet(point3DMaximumResult, rectangularCuboid3D, point3DMaximumResultOffset, rectangularCuboid3DOffset + RECTANGULAR_CUBOID_OFFSET_MAXIMUM);
	}
	
//	TODO: Add Javadocs!
	public static double[] rectangularCuboid3DGetMinimum(final double[] rectangularCuboid3D, final double[] point3DMinimumResult) {
		return rectangularCuboid3DGetMinimum(rectangularCuboid3D, point3DMinimumResult, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] rectangularCuboid3DGetMinimum(final double[] rectangularCuboid3D, final double[] point3DMinimumResult, final int rectangularCuboid3DOffset, final int point3DMinimumResultOffset) {
		return point3DSet(point3DMinimumResult, rectangularCuboid3D, point3DMinimumResultOffset, rectangularCuboid3DOffset + RECTANGULAR_CUBOID_OFFSET_MINIMUM);
	}
	
//	TODO: Add Javadocs!
	public static double[] rectangularCuboid3DSet(final double[] rectangularCuboid3DResult, final double[] point3DA, final double[] point3DB) {
		return rectangularCuboid3DSet(rectangularCuboid3DResult, point3DA, point3DB, 0, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] rectangularCuboid3DSet(final double[] rectangularCuboid3DResult, final double[] point3DA, final double[] point3DB, final int rectangularCuboid3DResultOffset, final int point3DAOffset, final int point3DBOffset) {
		rectangularCuboid3DResult[rectangularCuboid3DResultOffset + RECTANGULAR_CUBOID_OFFSET_ID] = RECTANGULAR_CUBOID_ID;
		rectangularCuboid3DResult[rectangularCuboid3DResultOffset + RECTANGULAR_CUBOID_OFFSET_SIZE] = RECTANGULAR_CUBOID_SIZE;
		
		point3DSet(rectangularCuboid3DResult, point3DMaximum(point3DA, point3DB, point3D(), point3DAOffset, point3DBOffset, 0), rectangularCuboid3DResultOffset + RECTANGULAR_CUBOID_OFFSET_MAXIMUM, 0);
		point3DSet(rectangularCuboid3DResult, point3DMinimum(point3DA, point3DB, point3D(), point3DAOffset, point3DBOffset, 0), rectangularCuboid3DResultOffset + RECTANGULAR_CUBOID_OFFSET_MINIMUM, 0);
		
		return rectangularCuboid3DResult;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	// Shape3D /////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public static double shape3DIntersection(final double[] ray3D, final double[] shape3D) {
		return shape3DIntersection(ray3D, shape3D, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double shape3DIntersection(final double[] ray3D, final double[] shape3D, final int ray3DOffset, final int shape3DOffset) {
		switch(shape3DGetID(shape3D, shape3DOffset)) {
			case CONE_ID:
				return cone3DIntersection(ray3D, shape3D, ray3DOffset, shape3DOffset);
			case CYLINDER_ID:
				return cylinder3DIntersection(ray3D, shape3D, ray3DOffset, shape3DOffset);
			case DISK_ID:
				return disk3DIntersection(ray3D, shape3D, ray3DOffset, shape3DOffset);
			case PARABOLOID_ID:
				return paraboloid3DIntersection(ray3D, shape3D, ray3DOffset, shape3DOffset);
			case PLANE_ID:
				return plane3DIntersection(ray3D, shape3D, ray3DOffset, shape3DOffset);
			case RECTANGLE_ID:
				return rectangle3DIntersection(ray3D, shape3D, ray3DOffset, shape3DOffset);
			case RECTANGULAR_CUBOID_ID:
				return rectangularCuboid3DIntersection(ray3D, shape3D, ray3DOffset, shape3DOffset);
			case SPHERE_ID:
				return sphere3DIntersection(ray3D, shape3D, ray3DOffset, shape3DOffset);
			case TORUS_ID:
				return torus3DIntersection(ray3D, shape3D, ray3DOffset, shape3DOffset);
			case TRIANGLE_ID:
				return triangle3DIntersection(ray3D, shape3D, ray3DOffset, shape3DOffset);
			default:
				return NaN;
		}
	}
	
//	TODO: Add Javadocs!
	public static double[] shape3DComputeOrthonormalBasis(final double[] ray3D, final double[] shape3D, final double t) {
		return shape3DComputeOrthonormalBasis(ray3D, shape3D, t, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] shape3DComputeOrthonormalBasis(final double[] ray3D, final double[] shape3D, final double t, final int ray3DOffset, final int shape3DOffset) {
		switch(shape3DGetID(shape3D, shape3DOffset)) {
			case CONE_ID:
				return cone3DComputeOrthonormalBasis(ray3D, shape3D, t, ray3DOffset, shape3DOffset);
			case CYLINDER_ID:
				return cylinder3DComputeOrthonormalBasis(ray3D, shape3D, t, ray3DOffset, shape3DOffset);
			case DISK_ID:
				return disk3DComputeOrthonormalBasis(ray3D, shape3D, t, ray3DOffset, shape3DOffset);
			case PARABOLOID_ID:
				return paraboloid3DComputeOrthonormalBasis(ray3D, shape3D, t, ray3DOffset, shape3DOffset);
			case PLANE_ID:
				return plane3DComputeOrthonormalBasis(ray3D, shape3D, t, ray3DOffset, shape3DOffset);
			case RECTANGLE_ID:
				return rectangle3DComputeOrthonormalBasis(ray3D, shape3D, t, ray3DOffset, shape3DOffset);
			case RECTANGULAR_CUBOID_ID:
				return rectangularCuboid3DComputeOrthonormalBasis(ray3D, shape3D, t, ray3DOffset, shape3DOffset);
			case SPHERE_ID:
				return sphere3DComputeOrthonormalBasis(ray3D, shape3D, t, ray3DOffset, shape3DOffset);
			case TORUS_ID:
				return torus3DComputeOrthonormalBasis(ray3D, shape3D, t, ray3DOffset, shape3DOffset);
			case TRIANGLE_ID:
				return triangle3DComputeOrthonormalBasis(ray3D, shape3D, t, ray3DOffset, shape3DOffset);
			default:
				return orthonormalBasis33D();
		}
	}
	
//	TODO: Add Javadocs!
	public static double[] shape3DComputeSurfaceNormal(final double[] ray3D, final double[] shape3D, final double t) {
		return shape3DComputeSurfaceNormal(ray3D, shape3D, t, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] shape3DComputeSurfaceNormal(final double[] ray3D, final double[] shape3D, final double t, final int ray3DOffset, final int shape3DOffset) {
		return orthonormalBasis33DGetW(shape3DComputeOrthonormalBasis(ray3D, shape3D, t, ray3DOffset, shape3DOffset));
	}
	
//	TODO: Add Javadocs!
	public static double[] shape3DComputeTextureCoordinates(final double[] ray3D, final double[] shape3D, final double t) {
		return shape3DComputeTextureCoordinates(ray3D, shape3D, t, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] shape3DComputeTextureCoordinates(final double[] ray3D, final double[] shape3D, final double t, final int ray3DOffset, final int shape3DOffset) {
		switch(shape3DGetID(shape3D, shape3DOffset)) {
			case CONE_ID:
				return cone3DComputeTextureCoordinates(ray3D, shape3D, t, ray3DOffset, shape3DOffset);
			case CYLINDER_ID:
				return cylinder3DComputeTextureCoordinates(ray3D, shape3D, t, ray3DOffset, shape3DOffset);
			case DISK_ID:
				return disk3DComputeTextureCoordinates(ray3D, shape3D, t, ray3DOffset, shape3DOffset);
			case PARABOLOID_ID:
				return paraboloid3DComputeTextureCoordinates(ray3D, shape3D, t, ray3DOffset, shape3DOffset);
			case PLANE_ID:
				return plane3DComputeTextureCoordinates(ray3D, shape3D, t, ray3DOffset, shape3DOffset);
			case RECTANGLE_ID:
				return rectangle3DComputeTextureCoordinates(ray3D, shape3D, t, ray3DOffset, shape3DOffset);
			case RECTANGULAR_CUBOID_ID:
				return rectangularCuboid3DComputeTextureCoordinates(ray3D, shape3D, t, ray3DOffset, shape3DOffset);
			case SPHERE_ID:
				return sphere3DComputeTextureCoordinates(ray3D, shape3D, t, ray3DOffset, shape3DOffset);
			case TORUS_ID:
				return torus3DComputeTextureCoordinates(ray3D, shape3D, t, ray3DOffset, shape3DOffset);
			case TRIANGLE_ID:
				return triangle3DComputeTextureCoordinates(ray3D, shape3D, t, ray3DOffset, shape3DOffset);
			default:
				return point2D();
		}
	}
	
//	TODO: Add Javadocs!
	public static int shape3DGetID(final double[] shape3D) {
		return shape3DGetID(shape3D, 0);
	}
	
//	TODO: Add Javadocs!
	public static int shape3DGetID(final double[] shape3D, final int shape3DOffset) {
		return toInt(shape3D[shape3DOffset + SHAPE_OFFSET_ID]);
	}
	
//	TODO: Add Javadocs!
	public static int shape3DGetSize(final double[] shape3D) {
		return shape3DGetSize(shape3D, 0);
	}
	
//	TODO: Add Javadocs!
	public static int shape3DGetSize(final double[] shape3D, final int shape3DOffset) {
		return toInt(shape3D[shape3DOffset + SHAPE_OFFSET_SIZE]);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	// Sphere3D ////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public static boolean sphere3DContainsPoint3D(final double[] sphere3D, final double[] point3D) {
		return sphere3DContainsPoint3D(sphere3D, point3D, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static boolean sphere3DContainsPoint3D(final double[] sphere3D, final double[] point3D, final int sphere3DOffset, final int point3DOffset) {
		final double radius = sphere3DGetRadius(sphere3D, sphere3DOffset);
		final double radiusSquared = radius * radius;
		
		final double[] point3DCenter = sphere3DGetCenter(sphere3D, point3D(), sphere3DOffset, 0);
		
		final double distanceSquared = point3DDistanceSquared(point3DCenter, point3D, 0, point3DOffset);
		
		final boolean contains = distanceSquared <= radiusSquared;
		
		return contains;
	}
	
//	TODO: Add Javadocs!
	public static double sphere3DGetRadius(final double[] sphere3D) {
		return sphere3DGetRadius(sphere3D, 0);
	}
	
//	TODO: Add Javadocs!
	public static double sphere3DGetRadius(final double[] sphere3D, final int sphere3DOffset) {
		return sphere3D[sphere3DOffset + SPHERE_OFFSET_RADIUS];
	}
	
//	TODO: Add Javadocs!
	public static double sphere3DIntersection(final double[] ray3D, final double[] sphere3D) {
		return sphere3DIntersection(ray3D, sphere3D, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double sphere3DIntersection(final double[] ray3D, final double[] sphere3D, final int ray3DOffset, final int sphere3DOffset) {
		final double[] point3DOrigin = ray3DGetOrigin(ray3D, point3D(), ray3DOffset, 0);
		
		final double[] vector3DDirection = ray3DGetDirection(ray3D, vector3D(), ray3DOffset, 0);
		
		final double tMinimum = ray3DGetTMinimum(ray3D, ray3DOffset);
		final double tMaximum = ray3DGetTMaximum(ray3D, ray3DOffset);
		
		final double[] point3DCenter = sphere3DGetCenter(sphere3D, point3D(), sphere3DOffset, 0);
		
		final double radius = sphere3DGetRadius(sphere3D, sphere3DOffset);
		
		final double[] vector3DCenterToOrigin = vector3DDirection(point3DCenter, point3DOrigin);
		
		final double a = vector3DLengthSquared(vector3DDirection);
		final double b = vector3DDotProduct(vector3DCenterToOrigin, vector3DDirection) * 2.0D;
		final double c = vector3DLengthSquared(vector3DCenterToOrigin) - radius * radius;
		
		final double[] ts = solveQuadraticSystem(a, b, c);
		
		final double t0 = ts[0];
		final double t1 = ts[1];
		
		if(!isNaN(t0) && t0 > tMinimum && t0 < tMaximum) {
			return t0;
		}
		
		if(!isNaN(t1) && t1 > tMinimum && t1 < tMaximum) {
			return t1;
		}
		
		return NaN;
	}
	
//	TODO: Add Javadocs!
	public static double sphere3DSurfaceArea(final double[] sphere3D) {
		return sphere3DSurfaceArea(sphere3D, 0);
	}
	
//	TODO: Add Javadocs!
	public static double sphere3DSurfaceArea(final double[] sphere3D, final int sphere3DOffset) {
		final double radius = sphere3DGetRadius(sphere3D, sphere3DOffset);
		final double radiusSquared = radius * radius;
		
		final double surfaceArea = PI_MULTIPLIED_BY_4 * radiusSquared;
		
		return surfaceArea;
	}
	
//	TODO: Add Javadocs!
	public static double[] sphere3D() {
		return sphere3D(point3D(0.0D, 0.0D, 0.0D), 1.0D);
	}
	
//	TODO: Add Javadocs!
	public static double[] sphere3D(final double[] point3DCenter, final double radius) {
		return sphere3D(point3DCenter, radius, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] sphere3D(final double[] point3DCenter, final double radius, final int point3DCenterOffset) {
		return sphere3DSet(new double[SPHERE_SIZE], point3DCenter, radius, 0, point3DCenterOffset);
	}
	
//	TODO: Add Javadocs!
	public static double[] sphere3DComputeOrthonormalBasis(final double[] ray3D, final double[] sphere3D, final double t) {
		return sphere3DComputeOrthonormalBasis(ray3D, sphere3D, t, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] sphere3DComputeOrthonormalBasis(final double[] ray3D, final double[] sphere3D, final double t, final int ray3DOffset, final int sphere3DOffset) {
		final double[] point3DOrigin = ray3DGetOrigin(ray3D, point3D(), ray3DOffset, 0);
		
		final double[] vector3DDirection = ray3DGetDirection(ray3D, vector3D(), ray3DOffset, 0);
		
		final double[] point3DCenter = sphere3DGetCenter(sphere3D, point3D(), sphere3DOffset, 0);
		final double[] point3D = point3DAdd(point3DOrigin, vector3DDirection, t);
		
		final double[] vector3DW = vector3DDirectionNormalized(point3DCenter, point3D);
		final double[] vector3DV = vector3D(-PI_MULTIPLIED_BY_2 * vector3DGetY(vector3DW), PI_MULTIPLIED_BY_2 * vector3DGetX(vector3DW), 0.0D);
		
		return orthonormalBasis33DFromWV(vector3DW, vector3DV);
	}
	
//	TODO: Add Javadocs!
	public static double[] sphere3DComputeSurfaceNormal(final double[] ray3D, final double[] sphere3D, final double t) {
		return sphere3DComputeSurfaceNormal(ray3D, sphere3D, t, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] sphere3DComputeSurfaceNormal(final double[] ray3D, final double[] sphere3D, final double t, final int ray3DOffset, final int sphere3DOffset) {
		return orthonormalBasis33DGetW(sphere3DComputeOrthonormalBasis(ray3D, sphere3D, t, ray3DOffset, sphere3DOffset));
	}
	
//	TODO: Add Javadocs!
	public static double[] sphere3DComputeTextureCoordinates(final double[] ray3D, final double[] sphere3D, final double t) {
		return sphere3DComputeTextureCoordinates(ray3D, sphere3D, t, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] sphere3DComputeTextureCoordinates(final double[] ray3D, final double[] sphere3D, final double t, final int ray3DOffset, final int sphere3DOffset) {
		return point2DSphericalCoordinates(sphere3DComputeSurfaceNormal(ray3D, sphere3D, t, ray3DOffset, sphere3DOffset));
	}
	
//	TODO: Add Javadocs!
	public static double[] sphere3DGetCenter(final double[] sphere3D) {
		return sphere3DGetCenter(sphere3D, point3D());
	}
	
//	TODO: Add Javadocs!
	public static double[] sphere3DGetCenter(final double[] sphere3D, final double[] point3DCenterResult) {
		return sphere3DGetCenter(sphere3D, point3DCenterResult, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] sphere3DGetCenter(final double[] sphere3D, final double[] point3DCenterResult, final int sphere3DOffset, final int point3DCenterResultOffset) {
		return point3DSet(point3DCenterResult, sphere3D, point3DCenterResultOffset, sphere3DOffset + SPHERE_OFFSET_CENTER);
	}
	
//	TODO: Add Javadocs!
	public static double[] sphere3DSet(final double[] sphere3DResult, final double[] point3DCenter, final double radius) {
		return sphere3DSet(sphere3DResult, point3DCenter, radius, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] sphere3DSet(final double[] sphere3DResult, final double[] point3DCenter, final double radius, final int sphere3DResultOffset, final int point3DCenterOffset) {
		sphere3DResult[sphere3DResultOffset + SPHERE_OFFSET_ID] = SPHERE_ID;
		sphere3DResult[sphere3DResultOffset + SPHERE_OFFSET_SIZE] = SPHERE_SIZE;
		sphere3DResult[sphere3DResultOffset + SPHERE_OFFSET_RADIUS] = radius;
		
		point3DSet(sphere3DResult, point3DCenter, sphere3DResultOffset + SPHERE_OFFSET_CENTER, point3DCenterOffset);
		
		return sphere3DResult;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	// Torus3D /////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public static double torus3DGetRadiusInner(final double[] torus3D) {
		return torus3DGetRadiusInner(torus3D, 0);
	}
	
//	TODO: Add Javadocs!
	public static double torus3DGetRadiusInner(final double[] torus3D, final int torus3DOffset) {
		return torus3D[torus3DOffset + TORUS_OFFSET_RADIUS_INNER];
	}
	
//	TODO: Add Javadocs!
	public static double torus3DGetRadiusOuter(final double[] torus3D) {
		return torus3DGetRadiusOuter(torus3D, 0);
	}
	
//	TODO: Add Javadocs!
	public static double torus3DGetRadiusOuter(final double[] torus3D, final int torus3DOffset) {
		return torus3D[torus3DOffset + TORUS_OFFSET_RADIUS_OUTER];
	}
	
//	TODO: Add Javadocs!
	public static double torus3DIntersection(final double[] ray3D, final double[] torus3D) {
		return torus3DIntersection(ray3D, torus3D, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double torus3DIntersection(final double[] ray3D, final double[] torus3D, final int ray3DOffset, final int torus3DOffset) {
		final double[] point3DOrigin = ray3DGetOrigin(ray3D, point3D(), ray3DOffset, 0);
		
		final double[] vector3DDirection = ray3DGetDirection(ray3D, vector3D(), ray3DOffset, 0);
		
		final double tMinimum = ray3DGetTMinimum(ray3D, ray3DOffset);
		final double tMaximum = ray3DGetTMaximum(ray3D, ray3DOffset);
		
		final double radiusInner = torus3DGetRadiusInner(torus3D, torus3DOffset);
		final double radiusOuter = torus3DGetRadiusOuter(torus3D, torus3DOffset);
		
		final double[] vector3DOrigin = vector3DFromPoint3D(point3DOrigin);
		
		final double f0 = vector3DLengthSquared(vector3DDirection);
		final double f1 = vector3DDotProduct(vector3DOrigin, vector3DDirection) * 2.0D;
		final double f2 = radiusInner * radiusInner;
		final double f3 = radiusOuter * radiusOuter;
		final double f4 = vector3DLengthSquared(vector3DOrigin) - f2 - f3;
		final double f5 = vector3DDirection[2];
		final double f6 = vector3DOrigin[2];
		
		final double a = f0 * f0;
		final double b = f0 * 2.0D * f1;
		final double c = f1 * f1 + 2.0D * f0 * f4 + 4.0D * f3 * f5 * f5;
		final double d = f1 * 2.0D * f4 + 8.0D * f3 * f6 * f5;
		final double e = f4 * f4 + 4.0D * f3 * f6 * f6 - 4.0D * f3 * f2;
		
		final double[] ts = solveQuartic(a, b, c, d, e);
		
		if(ts.length == 0) {
			return NaN;
		}
		
		if(ts[0] >= tMaximum || ts[ts.length - 1] <= tMinimum) {
			return NaN;
		}
		
		for(int i = 0; i < ts.length; i++) {
			if(ts[i] > tMinimum) {
				return ts[i];
			}
		}
		
		return NaN;
	}
	
//	TODO: Add Javadocs!
	public static double[] torus3D() {
		return torus3D(0.25D, 1.0D);
	}
	
//	TODO: Add Javadocs!
	public static double[] torus3D(final double radiusInner, final double radiusOuter) {
		return torus3DSet(new double[TORUS_SIZE], radiusInner, radiusOuter);
	}
	
//	TODO: Add Javadocs!
	public static double[] torus3DComputeOrthonormalBasis(final double[] ray3D, final double[] torus3D, final double t) {
		return torus3DComputeOrthonormalBasis(ray3D, torus3D, t, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] torus3DComputeOrthonormalBasis(final double[] ray3D, final double[] torus3D, final double t, final int ray3DOffset, final int torus3DOffset) {
		final double[] point3DOrigin = ray3DGetOrigin(ray3D, point3D(), ray3DOffset, 0);
		
		final double[] vector3DDirection = ray3DGetDirection(ray3D, vector3D(), ray3DOffset, 0);
		
		final double radiusInner = torus3DGetRadiusInner(torus3D, torus3DOffset);
		final double radiusInnerSquared = radiusInner * radiusInner;
		
		final double radiusOuter = torus3DGetRadiusOuter(torus3D, torus3DOffset);
		final double radiusOuterSquared = radiusOuter * radiusOuter;
		
		final double[] point3D = point3DAdd(point3DOrigin, vector3DDirection, t);
		
		final double derivative = vector3DLengthSquared(vector3DFromPoint3D(point3D)) - radiusInnerSquared - radiusOuterSquared;
		
		final double[] vector3DW = vector3DNormalize(vector3D(point3DGetX(point3D) * derivative, point3DGetY(point3D) * derivative, point3DGetZ(point3D) * derivative + 2.0D * radiusOuterSquared * point3DGetZ(point3D)));
		
		return orthonormalBasis33DFromW(vector3DW);
	}
	
//	TODO: Add Javadocs!
	public static double[] torus3DComputeSurfaceNormal(final double[] ray3D, final double[] torus3D, final double t) {
		return torus3DComputeSurfaceNormal(ray3D, torus3D, t, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] torus3DComputeSurfaceNormal(final double[] ray3D, final double[] torus3D, final double t, final int ray3DOffset, final int torus3DOffset) {
		return orthonormalBasis33DGetW(torus3DComputeOrthonormalBasis(ray3D, torus3D, t, ray3DOffset, torus3DOffset));
	}
	
//	TODO: Add Javadocs!
	public static double[] torus3DComputeTextureCoordinates(final double[] ray3D, final double[] torus3D, final double t) {
		return torus3DComputeTextureCoordinates(ray3D, torus3D, t, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] torus3DComputeTextureCoordinates(final double[] ray3D, final double[] torus3D, final double t, final int ray3DOffset, final int torus3DOffset) {
		final double[] point3DOrigin = ray3DGetOrigin(ray3D, point3D(), ray3DOffset, 0);
		
		final double[] vector3DDirection = ray3DGetDirection(ray3D, vector3D(), ray3DOffset, 0);
		
		final double radiusInner = torus3DGetRadiusInner(torus3D, torus3DOffset);
		
		final double[] point3D = point3DAdd(point3DOrigin, vector3DDirection, t);
		
		final double u = getOrAdd(atan2(point3DGetY(point3D), point3DGetX(point3D)), 0.0D, PI_MULTIPLIED_BY_2) * PI_MULTIPLIED_BY_2_RECIPROCAL;
		final double v = (asin(saturate(point3DGetZ(point3D) / radiusInner, -1.0D, 1.0D)) + PI_DIVIDED_BY_2) * PI_RECIPROCAL;
		
		return point2D(u, v);
	}
	
//	TODO: Add Javadocs!
	public static double[] torus3DSet(final double[] torus3DResult, final double radiusInner, final double radiusOuter) {
		return torus3DSet(torus3DResult, radiusInner, radiusOuter, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] torus3DSet(final double[] torus3DResult, final double radiusInner, final double radiusOuter, final int torus3DResultOffset) {
		torus3DResult[torus3DResultOffset + TORUS_OFFSET_ID] = TORUS_ID;
		torus3DResult[torus3DResultOffset + TORUS_OFFSET_SIZE] = TORUS_SIZE;
		torus3DResult[torus3DResultOffset + TORUS_OFFSET_RADIUS_INNER] = radiusInner;
		torus3DResult[torus3DResultOffset + TORUS_OFFSET_RADIUS_OUTER] = radiusOuter;
		
		return torus3DResult;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	// Triangle3D //////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public static double triangle3DIntersection(final double[] ray3D, final double[] triangle3D) {
		return triangle3DIntersection(ray3D, triangle3D, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double triangle3DIntersection(final double[] ray3D, final double[] triangle3D, final int ray3DOffset, final int triangle3DOffset) {
		final double[] point3DOrigin = ray3DGetOrigin(ray3D, point3D(), ray3DOffset, 0);
		
		final double[] vector3DDirection = ray3DGetDirection(ray3D, vector3D(), ray3DOffset, 0);
		
		final double tMinimum = ray3DGetTMinimum(ray3D, ray3DOffset);
		final double tMaximum = ray3DGetTMaximum(ray3D, ray3DOffset);
		
		final double[] point3DPositionA = triangle3DGetPositionA(triangle3D, point3D(), triangle3DOffset, 0);
		final double[] point3DPositionB = triangle3DGetPositionB(triangle3D, point3D(), triangle3DOffset, 0);
		final double[] point3DPositionC = triangle3DGetPositionC(triangle3D, point3D(), triangle3DOffset, 0);
		
		final double[] vector3DEdgeAB = vector3DDirection(point3DPositionA, point3DPositionB);
		final double[] vector3DEdgeCA = vector3DDirection(point3DPositionC, point3DPositionA);
		final double[] vector3DEdgeABCrossEdgeCA = vector3DCrossProduct(vector3DEdgeAB, vector3DEdgeCA);
		final double[] vector3DOriginToPositionA = vector3DDirection(point3DOrigin, point3DPositionA);
		final double[] vector3DOriginToPositionACrossDirection = vector3DCrossProduct(vector3DOriginToPositionA, vector3DDirection);
		
		final double determinant = vector3DDotProduct(vector3DDirection, vector3DEdgeABCrossEdgeCA);
		
		final double t = vector3DDotProduct(vector3DEdgeABCrossEdgeCA, vector3DOriginToPositionA) / determinant;
		
		if(t <= tMinimum || t >= tMaximum) {
			return NaN;
		}
		
		final double barycentricCoordinatesUScaled = vector3DDotProduct(vector3DOriginToPositionACrossDirection, vector3DEdgeCA);
		final double barycentricCoordinatesU = barycentricCoordinatesUScaled / determinant;
		
		if(barycentricCoordinatesU < 0.0D) {
			return NaN;
		}
		
		final double barycentricCoordinatesVScaled = vector3DDotProduct(vector3DOriginToPositionACrossDirection, vector3DEdgeAB);
		final double barycentricCoordinatesV = barycentricCoordinatesVScaled / determinant;
		
		if(barycentricCoordinatesV < 0.0D) {
			return NaN;
		}
		
		if((barycentricCoordinatesUScaled + barycentricCoordinatesVScaled) * determinant > determinant * determinant) {
			return NaN;
		}
		
		return t;
	}
	
//	TODO: Add Javadocs!
	public static double[] triangle3D() {
		return triangle3D(point3D(0.0D, 1.0D, 0.0D), point3D(1.0D, -1.0D, 0.0D), point3D(-1.0D, -1.0D, 0.0D));
	}
	
//	TODO: Add Javadocs!
	public static double[] triangle3D(final double[] point3DPositionA, final double[] point3DPositionB, final double[] point3DPositionC) {
		return triangle3D(point3DPositionA, point3DPositionB, point3DPositionC, vector3DNormalize(vector3DCrossProduct(vector3DDirectionNormalized(point3DPositionA, point3DPositionB), vector3DDirectionNormalized(point3DPositionA, point3DPositionC))));
	}
	
//	TODO: Add Javadocs!
	public static double[] triangle3D(final double[] point3DPositionA, final double[] point3DPositionB, final double[] point3DPositionC, final double[] vector3DSurfaceNormal) {
		return triangle3D(point3DPositionA, point3DPositionB, point3DPositionC, vector3DSurfaceNormal, vector3DSurfaceNormal, vector3DSurfaceNormal);
	}
	
//	TODO: Add Javadocs!
	public static double[] triangle3D(final double[] point3DPositionA, final double[] point3DPositionB, final double[] point3DPositionC, final double[] vector3DSurfaceNormalA, final double[] vector3DSurfaceNormalB, final double[] vector3DSurfaceNormalC) {
		return triangle3D(point3DPositionA, point3DPositionB, point3DPositionC, vector3DSurfaceNormalA, vector3DSurfaceNormalB, vector3DSurfaceNormalC, point2D(0.5D, 0.0D), point2D(1.0D, 1.0D), point2D(0.0D, 1.0D));
	}
	
//	TODO: Add Javadocs!
	public static double[] triangle3D(final double[] point3DPositionA, final double[] point3DPositionB, final double[] point3DPositionC, final double[] vector3DSurfaceNormalA, final double[] vector3DSurfaceNormalB, final double[] vector3DSurfaceNormalC, final double[] point2DTextureCoordinatesA, final double[] point2DTextureCoordinatesB, final double[] point2DTextureCoordinatesC) {
		return triangle3D(point3DPositionA, point3DPositionB, point3DPositionC, vector3DSurfaceNormalA, vector3DSurfaceNormalB, vector3DSurfaceNormalC, point2DTextureCoordinatesA, point2DTextureCoordinatesB, point2DTextureCoordinatesC, 0, 0, 0, 0, 0, 0, 0, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] triangle3D(final double[] point3DPositionA, final double[] point3DPositionB, final double[] point3DPositionC, final double[] vector3DSurfaceNormalA, final double[] vector3DSurfaceNormalB, final double[] vector3DSurfaceNormalC, final double[] point2DTextureCoordinatesA, final double[] point2DTextureCoordinatesB, final double[] point2DTextureCoordinatesC, final int point3DPositionAOffset, final int point3DPositionBOffset, final int point3DPositionCOffset, final int vector3DSurfaceNormalAOffset, final int vector3DSurfaceNormalBOffset, final int vector3DSurfaceNormalCOffset, final int point2DTextureCoordinatesAOffset, final int point2DTextureCoordinatesBOffset, final int point2DTextureCoordinatesCOffset) {
		return triangle3DSet(new double[TRIANGLE_SIZE], point3DPositionA, point3DPositionB, point3DPositionC, vector3DSurfaceNormalA, vector3DSurfaceNormalB, vector3DSurfaceNormalC, point2DTextureCoordinatesA, point2DTextureCoordinatesB, point2DTextureCoordinatesC, 0, point3DPositionAOffset, point3DPositionBOffset, point3DPositionCOffset, vector3DSurfaceNormalAOffset, vector3DSurfaceNormalBOffset, vector3DSurfaceNormalCOffset, point2DTextureCoordinatesAOffset, point2DTextureCoordinatesBOffset, point2DTextureCoordinatesCOffset);
	}
	
//	TODO: Add Javadocs!
	public static double[] triangle3DComputeOrthonormalBasis(final double[] ray3D, final double[] triangle3D, final double t) {
		return triangle3DComputeOrthonormalBasis(ray3D, triangle3D, t, 0, 0);
	}
	
//	TODO: Add Javadocs!
	@SuppressWarnings("unused")
	public static double[] triangle3DComputeOrthonormalBasis(final double[] ray3D, final double[] triangle3D, final double t, final int ray3DOffset, final int triangle3DOffset) {
		final double[] point3DOrigin = ray3DGetOrigin(ray3D, point3D(), ray3DOffset, 0);
		
		final double[] vector3DDirection = ray3DGetDirection(ray3D, vector3D(), ray3DOffset, 0);
		
		final double[] point2DTextureCoordinatesA = triangle3DGetTextureCoordinatesA(triangle3D, point2D(), triangle3DOffset, 0);
		final double[] point2DTextureCoordinatesB = triangle3DGetTextureCoordinatesB(triangle3D, point2D(), triangle3DOffset, 0);
		final double[] point2DTextureCoordinatesC = triangle3DGetTextureCoordinatesC(triangle3D, point2D(), triangle3DOffset, 0);
		
		final double[] point3DPositionA = triangle3DGetPositionA(triangle3D, point3D(), triangle3DOffset, 0);
		final double[] point3DPositionB = triangle3DGetPositionB(triangle3D, point3D(), triangle3DOffset, 0);
		final double[] point3DPositionC = triangle3DGetPositionC(triangle3D, point3D(), triangle3DOffset, 0);
		
		final double[] vector3DSurfaceNormalA = triangle3DGetSurfaceNormalA(triangle3D, vector3D(), triangle3DOffset, 0);
		final double[] vector3DSurfaceNormalB = triangle3DGetSurfaceNormalB(triangle3D, vector3D(), triangle3DOffset, 0);
		final double[] vector3DSurfaceNormalC = triangle3DGetSurfaceNormalC(triangle3D, vector3D(), triangle3DOffset, 0);
		
		final double[] vector3DEdgeAB = vector3DDirection(point3DPositionA, point3DPositionB);
		final double[] vector3DEdgeCA = vector3DDirection(point3DPositionC, point3DPositionA);
		final double[] vector3DEdgeCB = vector3DDirection(point3DPositionC, point3DPositionB);
		final double[] vector3DEdgeABCrossEdgeCA = vector3DCrossProduct(vector3DEdgeAB, vector3DEdgeCA);
		final double[] vector3DOriginToPositionA = vector3DDirection(point3DOrigin, point3DPositionA);
		final double[] vector3DOriginToPositionACrossDirection = vector3DCrossProduct(vector3DOriginToPositionA, vector3DDirection);
		
		final double determinant = vector3DDotProduct(vector3DDirection, vector3DEdgeABCrossEdgeCA);
		
		final double barycentricCoordinatesU = vector3DDotProduct(vector3DOriginToPositionACrossDirection, vector3DEdgeCA) / determinant;
		final double barycentricCoordinatesV = vector3DDotProduct(vector3DOriginToPositionACrossDirection, vector3DEdgeAB) / determinant;
		final double barycentricCoordinatesW = 1.0D - barycentricCoordinatesU - barycentricCoordinatesV;
		
		final double[] point3DBarycentricCoordinates = point3D(barycentricCoordinatesU, barycentricCoordinatesV, barycentricCoordinatesW);
		
		final double[] vector2DEdgeCA = vector2DDirection(point2DTextureCoordinatesC, point2DTextureCoordinatesA);
		final double[] vector2DEdgeCB = vector2DDirection(point2DTextureCoordinatesC, point2DTextureCoordinatesB);
		
		final double determinantUV = vector2DCrossProduct(vector2DEdgeCA, vector2DEdgeCB);
		
		if(isZero(determinantUV)) {
			final double[] vector3DW = vector3DFromBarycentricCoordinatesNormalized(vector3DSurfaceNormalA, vector3DSurfaceNormalB, vector3DSurfaceNormalC, point3DBarycentricCoordinates);
			
			return orthonormalBasis33DFromW(vector3DW);
		}
		
		final double determinantUVReciprocal = 1.0D / determinantUV;
		
		final double x = (-vector2DGetX(vector2DEdgeCB) * vector3DGetX(vector3DEdgeCA) + vector2DGetX(vector2DEdgeCA) * vector3DGetX(vector3DEdgeCB)) * determinantUVReciprocal;
		final double y = (-vector2DGetX(vector2DEdgeCB) * vector3DGetY(vector3DEdgeCA) + vector2DGetX(vector2DEdgeCA) * vector3DGetY(vector3DEdgeCB)) * determinantUVReciprocal;
		final double z = (-vector2DGetX(vector2DEdgeCB) * vector3DGetZ(vector3DEdgeCA) + vector2DGetX(vector2DEdgeCA) * vector3DGetZ(vector3DEdgeCB)) * determinantUVReciprocal;
		
		final double[] vector3DW = vector3DFromBarycentricCoordinatesNormalized(vector3DSurfaceNormalA, vector3DSurfaceNormalB, vector3DSurfaceNormalC, point3DBarycentricCoordinates);
		final double[] vector3DV = vector3D(x, y, z);
		
		return orthonormalBasis33DFromWV(vector3DW, vector3DV);
	}
	
//	TODO: Add Javadocs!
	public static double[] triangle3DComputeSurfaceNormal(final double[] ray3D, final double[] triangle3D, final double t) {
		return triangle3DComputeSurfaceNormal(ray3D, triangle3D, t, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] triangle3DComputeSurfaceNormal(final double[] ray3D, final double[] triangle3D, final double t, final int ray3DOffset, final int triangle3DOffset) {
		return orthonormalBasis33DGetW(triangle3DComputeOrthonormalBasis(ray3D, triangle3D, t, ray3DOffset, triangle3DOffset));
	}
	
//	TODO: Add Javadocs!
	public static double[] triangle3DComputeTextureCoordinates(final double[] ray3D, final double[] triangle3D, final double t) {
		return triangle3DComputeTextureCoordinates(ray3D, triangle3D, t, 0, 0);
	}
	
//	TODO: Add Javadocs!
	@SuppressWarnings("unused")
	public static double[] triangle3DComputeTextureCoordinates(final double[] ray3D, final double[] triangle3D, final double t, final int ray3DOffset, final int triangle3DOffset) {
		final double[] point3DOrigin = ray3DGetOrigin(ray3D, point3D(), ray3DOffset, 0);
		
		final double[] vector3DDirection = ray3DGetDirection(ray3D, vector3D(), ray3DOffset, 0);
		
		final double[] point2DTextureCoordinatesA = triangle3DGetTextureCoordinatesA(triangle3D, point2D(), triangle3DOffset, 0);
		final double[] point2DTextureCoordinatesB = triangle3DGetTextureCoordinatesB(triangle3D, point2D(), triangle3DOffset, 0);
		final double[] point2DTextureCoordinatesC = triangle3DGetTextureCoordinatesC(triangle3D, point2D(), triangle3DOffset, 0);
		
		final double[] point3DPositionA = triangle3DGetPositionA(triangle3D, point3D(), triangle3DOffset, 0);
		final double[] point3DPositionB = triangle3DGetPositionB(triangle3D, point3D(), triangle3DOffset, 0);
		final double[] point3DPositionC = triangle3DGetPositionC(triangle3D, point3D(), triangle3DOffset, 0);
		
		final double[] vector3DEdgeAB = vector3DDirection(point3DPositionA, point3DPositionB);
		final double[] vector3DEdgeCA = vector3DDirection(point3DPositionC, point3DPositionA);
		final double[] vector3DEdgeABCrossEdgeCA = vector3DCrossProduct(vector3DEdgeAB, vector3DEdgeCA);
		final double[] vector3DOriginToPositionA = vector3DDirection(point3DOrigin, point3DPositionA);
		final double[] vector3DOriginToPositionACrossDirection = vector3DCrossProduct(vector3DOriginToPositionA, vector3DDirection);
		
		final double determinant = vector3DDotProduct(vector3DDirection, vector3DEdgeABCrossEdgeCA);
		
		final double barycentricCoordinatesU = vector3DDotProduct(vector3DOriginToPositionACrossDirection, vector3DEdgeCA) / determinant;
		final double barycentricCoordinatesV = vector3DDotProduct(vector3DOriginToPositionACrossDirection, vector3DEdgeAB) / determinant;
		final double barycentricCoordinatesW = 1.0D - barycentricCoordinatesU - barycentricCoordinatesV;
		
		final double[] point3DBarycentricCoordinates = point3D(barycentricCoordinatesU, barycentricCoordinatesV, barycentricCoordinatesW);
		
		final double[] point2DTextureCoordinates = point2DFromBarycentricCoordinates(point2DTextureCoordinatesA, point2DTextureCoordinatesB, point2DTextureCoordinatesC, point3DBarycentricCoordinates);
		
		return point2DTextureCoordinates;
	}
	
//	TODO: Add Javadocs!
	public static double[] triangle3DGetPositionA(final double[] triangle3D) {
		return triangle3DGetPositionA(triangle3D, point3D());
	}
	
//	TODO: Add Javadocs!
	public static double[] triangle3DGetPositionA(final double[] triangle3D, final double[] point3DPositionAResult) {
		return triangle3DGetPositionA(triangle3D, point3DPositionAResult, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] triangle3DGetPositionA(final double[] triangle3D, final double[] point3DPositionAResult, final int triangle3DOffset, final int point3DPositionAResultOffset) {
		return point3DSet(point3DPositionAResult, triangle3D, point3DPositionAResultOffset, triangle3DOffset + TRIANGLE_OFFSET_POSITION_A);
	}
	
//	TODO: Add Javadocs!
	public static double[] triangle3DGetPositionB(final double[] triangle3D) {
		return triangle3DGetPositionB(triangle3D, point3D());
	}
	
//	TODO: Add Javadocs!
	public static double[] triangle3DGetPositionB(final double[] triangle3D, final double[] point3DPositionBResult) {
		return triangle3DGetPositionB(triangle3D, point3DPositionBResult, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] triangle3DGetPositionB(final double[] triangle3D, final double[] point3DPositionBResult, final int triangle3DOffset, final int point3DPositionBResultOffset) {
		return point3DSet(point3DPositionBResult, triangle3D, point3DPositionBResultOffset, triangle3DOffset + TRIANGLE_OFFSET_POSITION_B);
	}
	
//	TODO: Add Javadocs!
	public static double[] triangle3DGetPositionC(final double[] triangle3D) {
		return triangle3DGetPositionC(triangle3D, point3D());
	}
	
//	TODO: Add Javadocs!
	public static double[] triangle3DGetPositionC(final double[] triangle3D, final double[] point3DPositionCResult) {
		return triangle3DGetPositionC(triangle3D, point3DPositionCResult, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] triangle3DGetPositionC(final double[] triangle3D, final double[] point3DPositionCResult, final int triangle3DOffset, final int point3DPositionCResultOffset) {
		return point3DSet(point3DPositionCResult, triangle3D, point3DPositionCResultOffset, triangle3DOffset + TRIANGLE_OFFSET_POSITION_C);
	}
	
//	TODO: Add Javadocs!
	public static double[] triangle3DGetSurfaceNormalA(final double[] triangle3D) {
		return triangle3DGetSurfaceNormalA(triangle3D, vector3D());
	}
	
//	TODO: Add Javadocs!
	public static double[] triangle3DGetSurfaceNormalA(final double[] triangle3D, final double[] vector3DSurfaceNormalAResult) {
		return triangle3DGetSurfaceNormalA(triangle3D, vector3DSurfaceNormalAResult, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] triangle3DGetSurfaceNormalA(final double[] triangle3D, final double[] vector3DSurfaceNormalAResult, final int triangle3DOffset, final int vector3DSurfaceNormalAResultOffset) {
		return vector3DSet(vector3DSurfaceNormalAResult, triangle3D, vector3DSurfaceNormalAResultOffset, triangle3DOffset + TRIANGLE_OFFSET_SURFACE_NORMAL_A);
	}
	
//	TODO: Add Javadocs!
	public static double[] triangle3DGetSurfaceNormalB(final double[] triangle3D) {
		return triangle3DGetSurfaceNormalB(triangle3D, vector3D());
	}
	
//	TODO: Add Javadocs!
	public static double[] triangle3DGetSurfaceNormalB(final double[] triangle3D, final double[] vector3DSurfaceNormalBResult) {
		return triangle3DGetSurfaceNormalB(triangle3D, vector3DSurfaceNormalBResult, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] triangle3DGetSurfaceNormalB(final double[] triangle3D, final double[] vector3DSurfaceNormalBResult, final int triangle3DOffset, final int vector3DSurfaceNormalBResultOffset) {
		return vector3DSet(vector3DSurfaceNormalBResult, triangle3D, vector3DSurfaceNormalBResultOffset, triangle3DOffset + TRIANGLE_OFFSET_SURFACE_NORMAL_B);
	}
	
//	TODO: Add Javadocs!
	public static double[] triangle3DGetSurfaceNormalC(final double[] triangle3D) {
		return triangle3DGetSurfaceNormalC(triangle3D, vector3D());
	}
	
//	TODO: Add Javadocs!
	public static double[] triangle3DGetSurfaceNormalC(final double[] triangle3D, final double[] vector3DSurfaceNormalCResult) {
		return triangle3DGetSurfaceNormalC(triangle3D, vector3DSurfaceNormalCResult, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] triangle3DGetSurfaceNormalC(final double[] triangle3D, final double[] vector3DSurfaceNormalCResult, final int triangle3DOffset, final int vector3DSurfaceNormalCResultOffset) {
		return vector3DSet(vector3DSurfaceNormalCResult, triangle3D, vector3DSurfaceNormalCResultOffset, triangle3DOffset + TRIANGLE_OFFSET_SURFACE_NORMAL_C);
	}
	
//	TODO: Add Javadocs!
	public static double[] triangle3DGetTextureCoordinatesA(final double[] triangle3D) {
		return triangle3DGetTextureCoordinatesA(triangle3D, vector3D());
	}
	
//	TODO: Add Javadocs!
	public static double[] triangle3DGetTextureCoordinatesA(final double[] triangle3D, final double[] point2DTextureCoordinatesAResult) {
		return triangle3DGetTextureCoordinatesA(triangle3D, point2DTextureCoordinatesAResult, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] triangle3DGetTextureCoordinatesA(final double[] triangle3D, final double[] point2DTextureCoordinatesAResult, final int triangle3DOffset, final int point2DTextureCoordinatesAResultOffset) {
		return point2DSet(point2DTextureCoordinatesAResult, triangle3D, point2DTextureCoordinatesAResultOffset, triangle3DOffset + TRIANGLE_OFFSET_TEXTURE_COORDINATES_A);
	}
	
//	TODO: Add Javadocs!
	public static double[] triangle3DGetTextureCoordinatesB(final double[] triangle3D) {
		return triangle3DGetTextureCoordinatesB(triangle3D, vector3D());
	}
	
//	TODO: Add Javadocs!
	public static double[] triangle3DGetTextureCoordinatesB(final double[] triangle3D, final double[] point2DTextureCoordinatesBResult) {
		return triangle3DGetTextureCoordinatesB(triangle3D, point2DTextureCoordinatesBResult, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] triangle3DGetTextureCoordinatesB(final double[] triangle3D, final double[] point2DTextureCoordinatesBResult, final int triangle3DOffset, final int point2DTextureCoordinatesBResultOffset) {
		return point2DSet(point2DTextureCoordinatesBResult, triangle3D, point2DTextureCoordinatesBResultOffset, triangle3DOffset + TRIANGLE_OFFSET_TEXTURE_COORDINATES_B);
	}
	
//	TODO: Add Javadocs!
	public static double[] triangle3DGetTextureCoordinatesC(final double[] triangle3D) {
		return triangle3DGetTextureCoordinatesC(triangle3D, vector3D());
	}
	
//	TODO: Add Javadocs!
	public static double[] triangle3DGetTextureCoordinatesC(final double[] triangle3D, final double[] point2DTextureCoordinatesCResult) {
		return triangle3DGetTextureCoordinatesC(triangle3D, point2DTextureCoordinatesCResult, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] triangle3DGetTextureCoordinatesC(final double[] triangle3D, final double[] point2DTextureCoordinatesCResult, final int triangle3DOffset, final int point2DTextureCoordinatesCResultOffset) {
		return point2DSet(point2DTextureCoordinatesCResult, triangle3D, point2DTextureCoordinatesCResultOffset, triangle3DOffset + TRIANGLE_OFFSET_TEXTURE_COORDINATES_C);
	}
	
//	TODO: Add Javadocs!
	public static double[] triangle3DSet(final double[] triangle3DResult, final double[] point3DPositionA, final double[] point3DPositionB, final double[] point3DPositionC, final double[] vector3DSurfaceNormalA, final double[] vector3DSurfaceNormalB, final double[] vector3DSurfaceNormalC, final double[] point2DTextureCoordinatesA, final double[] point2DTextureCoordinatesB, final double[] point2DTextureCoordinatesC) {
		return triangle3DSet(triangle3DResult, point3DPositionA, point3DPositionB, point3DPositionC, vector3DSurfaceNormalA, vector3DSurfaceNormalB, vector3DSurfaceNormalC, point2DTextureCoordinatesA, point2DTextureCoordinatesB, point2DTextureCoordinatesC, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] triangle3DSet(final double[] triangle3DResult, final double[] point3DPositionA, final double[] point3DPositionB, final double[] point3DPositionC, final double[] vector3DSurfaceNormalA, final double[] vector3DSurfaceNormalB, final double[] vector3DSurfaceNormalC, final double[] point2DTextureCoordinatesA, final double[] point2DTextureCoordinatesB, final double[] point2DTextureCoordinatesC, final int triangle3DResultOffset, final int point3DPositionAOffset, final int point3DPositionBOffset, final int point3DPositionCOffset, final int vector3DSurfaceNormalAOffset, final int vector3DSurfaceNormalBOffset, final int vector3DSurfaceNormalCOffset, final int point2DTextureCoordinatesAOffset, final int point2DTextureCoordinatesBOffset, final int point2DTextureCoordinatesCOffset) {
		triangle3DResult[triangle3DResultOffset + TRIANGLE_OFFSET_ID] = TRIANGLE_ID;
		triangle3DResult[triangle3DResultOffset + TRIANGLE_OFFSET_SIZE] = TRIANGLE_SIZE;
		
		point3DSet(triangle3DResult, point3DPositionA, triangle3DResultOffset + TRIANGLE_OFFSET_POSITION_A, point3DPositionAOffset);
		point3DSet(triangle3DResult, point3DPositionB, triangle3DResultOffset + TRIANGLE_OFFSET_POSITION_B, point3DPositionBOffset);
		point3DSet(triangle3DResult, point3DPositionC, triangle3DResultOffset + TRIANGLE_OFFSET_POSITION_C, point3DPositionCOffset);
		
		vector3DSet(triangle3DResult, vector3DSurfaceNormalA, triangle3DResultOffset + TRIANGLE_OFFSET_SURFACE_NORMAL_A, vector3DSurfaceNormalAOffset);
		vector3DSet(triangle3DResult, vector3DSurfaceNormalB, triangle3DResultOffset + TRIANGLE_OFFSET_SURFACE_NORMAL_B, vector3DSurfaceNormalBOffset);
		vector3DSet(triangle3DResult, vector3DSurfaceNormalC, triangle3DResultOffset + TRIANGLE_OFFSET_SURFACE_NORMAL_C, vector3DSurfaceNormalCOffset);
		
		point2DSet(triangle3DResult, point2DTextureCoordinatesA, triangle3DResultOffset + TRIANGLE_OFFSET_TEXTURE_COORDINATES_A, point2DTextureCoordinatesAOffset);
		point2DSet(triangle3DResult, point2DTextureCoordinatesB, triangle3DResultOffset + TRIANGLE_OFFSET_TEXTURE_COORDINATES_B, point2DTextureCoordinatesBOffset);
		point2DSet(triangle3DResult, point2DTextureCoordinatesC, triangle3DResultOffset + TRIANGLE_OFFSET_TEXTURE_COORDINATES_C, point2DTextureCoordinatesCOffset);
		
		return triangle3DResult;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static int doRectangularCuboid3DComputeFace(final double[] point3D, final double[] point3DMaximum, final double[] point3DMinimum) {
		final double[] point3DMidpoint = point3DMidpoint(point3DMaximum, point3DMinimum);
		
		final double[] vector3DHalfDistance = vector3DMultiply(vector3DDirection(point3DMinimum, point3DMaximum), 0.5D);
		
		final double epsilon = 0.0001D;
		
		if(point3DGetX(point3D) + vector3DGetX(vector3DHalfDistance) - epsilon < point3DGetX(point3DMidpoint)) {
			return 1;
		}
		
		if(point3DGetX(point3D) - vector3DGetX(vector3DHalfDistance) + epsilon > point3DGetX(point3DMidpoint)) {
			return 2;
		}
		
		if(point3DGetY(point3D) + vector3DGetY(vector3DHalfDistance) - epsilon < point3DGetY(point3DMidpoint)) {
			return 3;
		}
		
		if(point3DGetY(point3D) - vector3DGetY(vector3DHalfDistance) + epsilon > point3DGetY(point3DMidpoint)) {
			return 4;
		}
		
		if(point3DGetZ(point3D) + vector3DGetZ(vector3DHalfDistance) - epsilon < point3DGetZ(point3DMidpoint)) {
			return 5;
		}
		
		if(point3DGetZ(point3D) - vector3DGetZ(vector3DHalfDistance) + epsilon > point3DGetZ(point3DMidpoint)) {
			return 6;
		}
		
		return 0;
	}
}