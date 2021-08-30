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
import static org.dayflower.simplex.Point.point2DEquals;
import static org.dayflower.simplex.Point.point2DFromBarycentricCoordinates;
import static org.dayflower.simplex.Point.point2DDistance;
import static org.dayflower.simplex.Point.point2DGetX;
import static org.dayflower.simplex.Point.point2DGetY;
import static org.dayflower.simplex.Point.point2DSet;
import static org.dayflower.simplex.Point.point2DSphericalCoordinates;
import static org.dayflower.simplex.Point.point3D;
import static org.dayflower.simplex.Point.point3DAdd;
import static org.dayflower.simplex.Point.point3DCoplanar;
import static org.dayflower.simplex.Point.point3DDistanceSquared;
import static org.dayflower.simplex.Point.point3DEquals;
import static org.dayflower.simplex.Point.point3DGetX;
import static org.dayflower.simplex.Point.point3DGetY;
import static org.dayflower.simplex.Point.point3DGetZ;
import static org.dayflower.simplex.Point.point3DLerp;
import static org.dayflower.simplex.Point.point3DMaximum;
import static org.dayflower.simplex.Point.point3DMidpoint;
import static org.dayflower.simplex.Point.point3DMinimum;
import static org.dayflower.simplex.Point.point3DSet;
import static org.dayflower.simplex.Point.point3DSwap;
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
import static org.dayflower.simplex.Vector.vector3DAdd;
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
import static org.dayflower.simplex.Vector.vector3DLerp;
import static org.dayflower.simplex.Vector.vector3DMultiply;
import static org.dayflower.simplex.Vector.vector3DNormalNormalized;
import static org.dayflower.simplex.Vector.vector3DNormalize;
import static org.dayflower.simplex.Vector.vector3DReciprocal;
import static org.dayflower.simplex.Vector.vector3DSet;
import static org.dayflower.utility.DoubleArrays.merge;
import static org.dayflower.utility.Doubles.NaN;
import static org.dayflower.utility.Doubles.PI;
import static org.dayflower.utility.Doubles.PI_DIVIDED_BY_2;
import static org.dayflower.utility.Doubles.PI_MULTIPLIED_BY_2;
import static org.dayflower.utility.Doubles.PI_MULTIPLIED_BY_2_RECIPROCAL;
import static org.dayflower.utility.Doubles.PI_MULTIPLIED_BY_4;
import static org.dayflower.utility.Doubles.PI_RECIPROCAL;
import static org.dayflower.utility.Doubles.abs;
import static org.dayflower.utility.Doubles.asin;
import static org.dayflower.utility.Doubles.atan2;
import static org.dayflower.utility.Doubles.cos;
import static org.dayflower.utility.Doubles.equal;
import static org.dayflower.utility.Doubles.getOrAdd;
import static org.dayflower.utility.Doubles.isInfinite;
import static org.dayflower.utility.Doubles.isNaN;
import static org.dayflower.utility.Doubles.isZero;
import static org.dayflower.utility.Doubles.max;
import static org.dayflower.utility.Doubles.min;
import static org.dayflower.utility.Doubles.normalize;
import static org.dayflower.utility.Doubles.pow;
import static org.dayflower.utility.Doubles.saturate;
import static org.dayflower.utility.Doubles.sin;
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
	public static final int CONE_3_ID = 0;
	
	/**
	 * The relative offset for the ID in a {@code double[]} that contains a cone.
	 */
	public static final int CONE_3_OFFSET_ID = 0;
	
	/**
	 * The relative offset for the maximum phi angle in a {@code double[]} that contains a cone.
	 */
	public static final int CONE_3_OFFSET_PHI_MAX = 2;
	
	/**
	 * The relative offset for the radius in a {@code double[]} that contains a cone.
	 */
	public static final int CONE_3_OFFSET_RADIUS = 3;
	
	/**
	 * The relative offset for the size in a {@code double[]} that contains a cone.
	 */
	public static final int CONE_3_OFFSET_SIZE = 1;
	
	/**
	 * The relative offset for the maximum Z-value in a {@code double[]} that contains a cone.
	 */
	public static final int CONE_3_OFFSET_Z_MAX = 4;
	
	/**
	 * The size of a cone.
	 */
	public static final int CONE_3_SIZE = 5;
	
	/**
	 * The ID of a cylinder.
	 */
	public static final int CYLINDER_3_ID = 1;
	
	/**
	 * The relative offset for the ID in a {@code double[]} that contains a cylinder.
	 */
	public static final int CYLINDER_3_OFFSET_ID = 0;
	
	/**
	 * The relative offset for the maximum phi angle in a {@code double[]} that contains a cylinder.
	 */
	public static final int CYLINDER_3_OFFSET_PHI_MAX = 2;
	
	/**
	 * The relative offset for the radius in a {@code double[]} that contains a cylinder.
	 */
	public static final int CYLINDER_3_OFFSET_RADIUS = 3;
	
	/**
	 * The relative offset for the size in a {@code double[]} that contains a cylinder.
	 */
	public static final int CYLINDER_3_OFFSET_SIZE = 1;
	
	/**
	 * The relative offset for the maximum Z-value in a {@code double[]} that contains a cylinder.
	 */
	public static final int CYLINDER_3_OFFSET_Z_MAX = 4;
	
	/**
	 * The relative offset for the minimum Z-value in a {@code double[]} that contains a cylinder.
	 */
	public static final int CYLINDER_3_OFFSET_Z_MIN = 5;
	
	/**
	 * The size of a cylinder.
	 */
	public static final int CYLINDER_3_SIZE = 6;
	
	/**
	 * The ID of a disk.
	 */
	public static final int DISK_3_ID = 2;
	
	/**
	 * The relative offset for the ID in a {@code double[]} that contains a disk.
	 */
	public static final int DISK_3_OFFSET_ID = 0;
	
	/**
	 * The relative offset for the maximum phi angle in a {@code double[]} that contains a disk.
	 */
	public static final int DISK_3_OFFSET_PHI_MAX = 2;
	
	/**
	 * The relative offset for the inner radius in a {@code double[]} that contains a disk.
	 */
	public static final int DISK_3_OFFSET_RADIUS_INNER = 3;
	
	/**
	 * The relative offset for the outer radius in a {@code double[]} that contains a disk.
	 */
	public static final int DISK_3_OFFSET_RADIUS_OUTER = 4;
	
	/**
	 * The relative offset for the size in a {@code double[]} that contains a disk.
	 */
	public static final int DISK_3_OFFSET_SIZE = 1;
	
	/**
	 * The relative offset for the maximum Z-value in a {@code double[]} that contains a disk.
	 */
	public static final int DISK_3_OFFSET_Z_MAX = 5;
	
	/**
	 * The size of a disk.
	 */
	public static final int DISK_3_SIZE = 6;
	
	/**
	 * The ID of a hyperboloid.
	 */
	public static final int HYPERBOLOID_3_ID = 3;
	
	/**
	 * The relative offset for the point denoted by A in a {@code double[]} that contains a hyperboloid.
	 */
	public static final int HYPERBOLOID_3_OFFSET_A = 3;
	
	/**
	 * The relative offset for the AH in a {@code double[]} that contains a hyperboloid.
	 */
	public static final int HYPERBOLOID_3_OFFSET_A_H = 9;
	
	/**
	 * The relative offset for the point denoted by B in a {@code double[]} that contains a hyperboloid.
	 */
	public static final int HYPERBOLOID_3_OFFSET_B = 6;
	
	/**
	 * The relative offset for the CH in a {@code double[]} that contains a hyperboloid.
	 */
	public static final int HYPERBOLOID_3_OFFSET_C_H = 10;
	
	/**
	 * The relative offset for the ID in a {@code double[]} that contains a hyperboloid.
	 */
	public static final int HYPERBOLOID_3_OFFSET_ID = 0;
	
	/**
	 * The relative offset for the maximum phi angle in a {@code double[]} that contains a hyperboloid.
	 */
	public static final int HYPERBOLOID_3_OFFSET_PHI_MAX = 2;
	
	/**
	 * The relative offset for the maximum R-value in a {@code double[]} that contains a hyperboloid.
	 */
	public static final int HYPERBOLOID_3_OFFSET_R_MAX = 11;
	
	/**
	 * The relative offset for the size in a {@code double[]} that contains a hyperboloid.
	 */
	public static final int HYPERBOLOID_3_OFFSET_SIZE = 1;
	
	/**
	 * The relative offset for the maximum Z-value in a {@code double[]} that contains a hyperboloid.
	 */
	public static final int HYPERBOLOID_3_OFFSET_Z_MAX = 12;
	
	/**
	 * The relative offset for the minimum Z-value in a {@code double[]} that contains a hyperboloid.
	 */
	public static final int HYPERBOLOID_3_OFFSET_Z_MIN = 13;
	
	/**
	 * The size of a hyperboloid.
	 */
	public static final int HYPERBOLOID_3_SIZE = 14;
	
	/**
	 * The ID of a line segment.
	 */
	public static final int LINE_SEGMENT_2_ID = 4;
	
	/**
	 * The relative offset for the point denoted by A in a {@code double[]} that contains a line segment.
	 */
	public static final int LINE_SEGMENT_2_OFFSET_A = 2;
	
	/**
	 * The relative offset for the point denoted by B in a {@code double[]} that contains a line segment.
	 */
	public static final int LINE_SEGMENT_2_OFFSET_B = 4;
	
	/**
	 * The relative offset for the ID in a {@code double[]} that contains a line segment.
	 */
	public static final int LINE_SEGMENT_2_OFFSET_ID = 0;
	
	/**
	 * The relative offset for the size in a {@code double[]} that contains a line segment.
	 */
	public static final int LINE_SEGMENT_2_OFFSET_SIZE = 1;
	
	/**
	 * The size of a line segment.
	 */
	public static final int LINE_SEGMENT_2_SIZE = 6;
	
	/**
	 * The ID of a line segment.
	 */
	public static final int LINE_SEGMENT_3_ID = 4;
	
	/**
	 * The relative offset for the point denoted by A in a {@code double[]} that contains a line segment.
	 */
	public static final int LINE_SEGMENT_3_OFFSET_A = 2;
	
	/**
	 * The relative offset for the point denoted by B in a {@code double[]} that contains a line segment.
	 */
	public static final int LINE_SEGMENT_3_OFFSET_B = 5;
	
	/**
	 * The relative offset for the ID in a {@code double[]} that contains a line segment.
	 */
	public static final int LINE_SEGMENT_3_OFFSET_ID = 0;
	
	/**
	 * The relative offset for the size in a {@code double[]} that contains a line segment.
	 */
	public static final int LINE_SEGMENT_3_OFFSET_SIZE = 1;
	
	/**
	 * The size of a line segment.
	 */
	public static final int LINE_SEGMENT_3_SIZE = 8;
	
	/**
	 * The ID of a paraboloid.
	 */
	public static final int PARABOLOID_3_ID = 5;
	
	/**
	 * The relative offset for the ID in a {@code double[]} that contains a paraboloid.
	 */
	public static final int PARABOLOID_3_OFFSET_ID = 0;
	
	/**
	 * The relative offset for the maximum phi angle in a {@code double[]} that contains a paraboloid.
	 */
	public static final int PARABOLOID_3_OFFSET_PHI_MAX = 2;
	
	/**
	 * The relative offset for the radius in a {@code double[]} that contains a paraboloid.
	 */
	public static final int PARABOLOID_3_OFFSET_RADIUS = 3;
	
	/**
	 * The relative offset for the size in a {@code double[]} that contains a paraboloid.
	 */
	public static final int PARABOLOID_3_OFFSET_SIZE = 1;
	
	/**
	 * The relative offset for the maximum Z-value in a {@code double[]} that contains a paraboloid.
	 */
	public static final int PARABOLOID_3_OFFSET_Z_MAX = 4;
	
	/**
	 * The relative offset for the minimum Z-value in a {@code double[]} that contains a paraboloid.
	 */
	public static final int PARABOLOID_3_OFFSET_Z_MIN = 5;
	
	/**
	 * The size of a paraboloid.
	 */
	public static final int PARABOLOID_3_SIZE = 6;
	
	/**
	 * The ID of a plane.
	 */
	public static final int PLANE_3_ID = 6;
	
	/**
	 * The relative offset for the point denoted by A in a {@code double[]} that contains a plane.
	 */
	public static final int PLANE_3_OFFSET_A = 2;
	
	/**
	 * The relative offset for the point denoted by B in a {@code double[]} that contains a plane.
	 */
	public static final int PLANE_3_OFFSET_B = 5;
	
	/**
	 * The relative offset for the point denoted by C in a {@code double[]} that contains a plane.
	 */
	public static final int PLANE_3_OFFSET_C = 8;
	
	/**
	 * The relative offset for the ID in a {@code double[]} that contains a plane.
	 */
	public static final int PLANE_3_OFFSET_ID = 0;
	
	/**
	 * The relative offset for the size in a {@code double[]} that contains a plane.
	 */
	public static final int PLANE_3_OFFSET_SIZE = 1;
	
	/**
	 * The size of a plane.
	 */
	public static final int PLANE_3_SIZE = 11;
	
	/**
	 * The ID of a polygon.
	 */
	public static final int POLYGON_2_ID = 7;
	
	/**
	 * The relative offset for the ID in a {@code double[]} that contains a polygon.
	 */
	public static final int POLYGON_2_OFFSET_ID = 0;
	
	/**
	 * The relative offset for the point count in a {@code double[]} that contains a polygon.
	 */
	public static final int POLYGON_2_OFFSET_POINT_COUNT = 2;
	
	/**
	 * The relative offset for the first point in a {@code double[]} that contains a polygon.
	 */
	public static final int POLYGON_2_OFFSET_POINT_FIRST = 3;
	
	/**
	 * The relative offset for the size in a {@code double[]} that contains a polygon.
	 */
	public static final int POLYGON_2_OFFSET_SIZE = 1;
	
	/**
	 * The ID of a polygon.
	 */
	public static final int POLYGON_3_ID = 7;
	
	/**
	 * The relative offset for the ID in a {@code double[]} that contains a polygon.
	 */
	public static final int POLYGON_3_OFFSET_ID = 0;
	
	/**
	 * The relative offset for the point count in a {@code double[]} that contains a polygon.
	 */
	public static final int POLYGON_3_OFFSET_POINT_COUNT = 2;
	
	/**
	 * The relative offset for the first point in a {@code double[]} that contains a polygon.
	 */
	public static final int POLYGON_3_OFFSET_POINT_FIRST = 3;
	
	/**
	 * The relative offset for the size in a {@code double[]} that contains a polygon.
	 */
	public static final int POLYGON_3_OFFSET_SIZE = 1;
	
	/**
	 * The ID of a rectangle.
	 */
	public static final int RECTANGLE_2_ID = 8;
	
	/**
	 * The relative offset for the point denoted by A in a {@code double[]} that contains a rectangle.
	 */
	public static final int RECTANGLE_2_OFFSET_A = 2;
	
	/**
	 * The relative offset for the point denoted by B in a {@code double[]} that contains a rectangle.
	 */
	public static final int RECTANGLE_2_OFFSET_B = 4;
	
	/**
	 * The relative offset for the point denoted by C in a {@code double[]} that contains a rectangle.
	 */
	public static final int RECTANGLE_2_OFFSET_C = 6;
	
	/**
	 * The relative offset for the point denoted by C in a {@code double[]} that contains a rectangle.
	 */
	public static final int RECTANGLE_2_OFFSET_D = 8;
	
	/**
	 * The relative offset for the ID in a {@code double[]} that contains a rectangle.
	 */
	public static final int RECTANGLE_2_OFFSET_ID = 0;
	
	/**
	 * The relative offset for the size in a {@code double[]} that contains a rectangle.
	 */
	public static final int RECTANGLE_2_OFFSET_SIZE = 1;
	
	/**
	 * The size of a rectangle.
	 */
	public static final int RECTANGLE_2_SIZE = 10;
	
	/**
	 * The ID of a rectangle.
	 */
	public static final int RECTANGLE_3_ID = 8;
	
	/**
	 * The relative offset for the point denoted by A in a {@code double[]} that contains a rectangle.
	 */
	public static final int RECTANGLE_3_OFFSET_A = 2;
	
	/**
	 * The relative offset for the point denoted by B in a {@code double[]} that contains a rectangle.
	 */
	public static final int RECTANGLE_3_OFFSET_B = 5;
	
	/**
	 * The relative offset for the point denoted by C in a {@code double[]} that contains a rectangle.
	 */
	public static final int RECTANGLE_3_OFFSET_C = 8;
	
	/**
	 * The relative offset for the point denoted by C in a {@code double[]} that contains a rectangle.
	 */
	public static final int RECTANGLE_3_OFFSET_D = 11;
	
	/**
	 * The relative offset for the ID in a {@code double[]} that contains a rectangle.
	 */
	public static final int RECTANGLE_3_OFFSET_ID = 0;
	
	/**
	 * The relative offset for the size in a {@code double[]} that contains a rectangle.
	 */
	public static final int RECTANGLE_3_OFFSET_SIZE = 1;
	
	/**
	 * The size of a rectangle.
	 */
	public static final int RECTANGLE_3_SIZE = 14;
	
	/**
	 * The ID of a rectangular cuboid.
	 */
	public static final int RECTANGULAR_CUBOID_3_ID = 9;
	
	/**
	 * The relative offset for the ID in a {@code double[]} that contains a rectangular cuboid.
	 */
	public static final int RECTANGULAR_CUBOID_3_OFFSET_ID = 0;
	
	/**
	 * The relative offset for the point denoted by Maximum in a {@code double[]} that contains a rectangular cuboid.
	 */
	public static final int RECTANGULAR_CUBOID_3_OFFSET_MAXIMUM = 2;
	
	/**
	 * The relative offset for the point denoted by Minimum in a {@code double[]} that contains a rectangular cuboid.
	 */
	public static final int RECTANGULAR_CUBOID_3_OFFSET_MINIMUM = 5;
	
	/**
	 * The relative offset for the size in a {@code double[]} that contains a rectangular cuboid.
	 */
	public static final int RECTANGULAR_CUBOID_3_OFFSET_SIZE = 1;
	
	/**
	 * The size of a rectangular cuboid.
	 */
	public static final int RECTANGULAR_CUBOID_3_SIZE = 8;
	
	/**
	 * The relative offset for the ID in a {@code double[]} that contains a shape.
	 */
	public static final int SHAPE_2_OFFSET_ID = 0;
	
	/**
	 * The relative offset for the size in a {@code double[]} that contains a shape.
	 */
	public static final int SHAPE_2_OFFSET_SIZE = 1;
	
	/**
	 * The relative offset for the ID in a {@code double[]} that contains a shape.
	 */
	public static final int SHAPE_3_OFFSET_ID = 0;
	
	/**
	 * The relative offset for the size in a {@code double[]} that contains a shape.
	 */
	public static final int SHAPE_3_OFFSET_SIZE = 1;
	
	/**
	 * The ID of a sphere.
	 */
	public static final int SPHERE_3_ID = 10;
	
	/**
	 * The relative offset for the point denoted by Center in a {@code double[]} that contains a sphere.
	 */
	public static final int SPHERE_3_OFFSET_CENTER = 2;
	
	/**
	 * The relative offset for the ID in a {@code double[]} that contains a sphere.
	 */
	public static final int SPHERE_3_OFFSET_ID = 0;
	
	/**
	 * The relative offset for the radius in a {@code double[]} that contains a sphere.
	 */
	public static final int SPHERE_3_OFFSET_RADIUS = 5;
	
	/**
	 * The relative offset for the size in a {@code double[]} that contains a sphere.
	 */
	public static final int SPHERE_3_OFFSET_SIZE = 1;
	
	/**
	 * The size of a sphere.
	 */
	public static final int SPHERE_3_SIZE = 6;
	
	/**
	 * The ID of a torus.
	 */
	public static final int TORUS_3_ID = 11;
	
	/**
	 * The relative offset for the ID in a {@code double[]} that contains a torus.
	 */
	public static final int TORUS_3_OFFSET_ID = 0;
	
	/**
	 * The relative offset for the inner radius in a {@code double[]} that contains a torus.
	 */
	public static final int TORUS_3_OFFSET_RADIUS_INNER = 2;
	
	/**
	 * The relative offset for the outer radius in a {@code double[]} that contains a torus.
	 */
	public static final int TORUS_3_OFFSET_RADIUS_OUTER = 3;
	
	/**
	 * The relative offset for the size in a {@code double[]} that contains a torus.
	 */
	public static final int TORUS_3_OFFSET_SIZE = 1;
	
	/**
	 * The size of a torus.
	 */
	public static final int TORUS_3_SIZE = 4;
	
	/**
	 * The ID of a triangle.
	 */
	public static final int TRIANGLE_3_ID = 12;
	
	/**
	 * The relative offset for the ID in a {@code double[]} that contains a triangle.
	 */
	public static final int TRIANGLE_3_OFFSET_ID = 0;
	
	/**
	 * The relative offset for the point denoted by Position A in a {@code double[]} that contains a triangle.
	 */
	public static final int TRIANGLE_3_OFFSET_POSITION_A = 2;
	
	/**
	 * The relative offset for the point denoted by Position B in a {@code double[]} that contains a triangle.
	 */
	public static final int TRIANGLE_3_OFFSET_POSITION_B = 5;
	
	/**
	 * The relative offset for the point denoted by Position C in a {@code double[]} that contains a triangle.
	 */
	public static final int TRIANGLE_3_OFFSET_POSITION_C = 8;
	
	/**
	 * The relative offset for the size in a {@code double[]} that contains a triangle.
	 */
	public static final int TRIANGLE_3_OFFSET_SIZE = 1;
	
	/**
	 * The relative offset for the vector denoted by Surface Normal A in a {@code double[]} that contains a triangle.
	 */
	public static final int TRIANGLE_3_OFFSET_SURFACE_NORMAL_A = 11;
	
	/**
	 * The relative offset for the vector denoted by Surface Normal B in a {@code double[]} that contains a triangle.
	 */
	public static final int TRIANGLE_3_OFFSET_SURFACE_NORMAL_B = 14;
	
	/**
	 * The relative offset for the vector denoted by Surface Normal C in a {@code double[]} that contains a triangle.
	 */
	public static final int TRIANGLE_3_OFFSET_SURFACE_NORMAL_C = 17;
	
	/**
	 * The relative offset for the point denoted by Texture Coordinates A in a {@code double[]} that contains a triangle.
	 */
	public static final int TRIANGLE_3_OFFSET_TEXTURE_COORDINATES_A = 20;
	
	/**
	 * The relative offset for the point denoted by Texture Coordinates B in a {@code double[]} that contains a triangle.
	 */
	public static final int TRIANGLE_3_OFFSET_TEXTURE_COORDINATES_B = 22;
	
	/**
	 * The relative offset for the point denoted by Texture Coordinates C in a {@code double[]} that contains a triangle.
	 */
	public static final int TRIANGLE_3_OFFSET_TEXTURE_COORDINATES_C = 24;
	
	/**
	 * The size of a triangle.
	 */
	public static final int TRIANGLE_3_SIZE = 26;
	
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
		return cone3D[cone3DOffset + CONE_3_OFFSET_PHI_MAX];
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
		return cone3D[cone3DOffset + CONE_3_OFFSET_RADIUS];
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
		return cone3D[cone3DOffset + CONE_3_OFFSET_Z_MAX];
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
		return cone3DSet(new double[CONE_3_SIZE], phiMax, radius, zMax);
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
		cone3DResult[cone3DResultOffset + CONE_3_OFFSET_ID] = CONE_3_ID;
		cone3DResult[cone3DResultOffset + CONE_3_OFFSET_SIZE] = CONE_3_SIZE;
		cone3DResult[cone3DResultOffset + CONE_3_OFFSET_PHI_MAX] = phiMax;
		cone3DResult[cone3DResultOffset + CONE_3_OFFSET_RADIUS] = radius;
		cone3DResult[cone3DResultOffset + CONE_3_OFFSET_Z_MAX] = zMax;
		
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
		return cylinder3D[cylinder3DOffset + CYLINDER_3_OFFSET_PHI_MAX];
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
		return cylinder3D[cylinder3DOffset + CYLINDER_3_OFFSET_RADIUS];
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
		return cylinder3D[cylinder3DOffset + CYLINDER_3_OFFSET_Z_MAX];
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
		return cylinder3D[cylinder3DOffset + CYLINDER_3_OFFSET_Z_MIN];
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
		return cylinder3DSet(new double[CYLINDER_3_SIZE], phiMax, radius, zMax, zMin);
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
		cylinder3DResult[cylinder3DResultOffset + CYLINDER_3_OFFSET_ID] = CYLINDER_3_ID;
		cylinder3DResult[cylinder3DResultOffset + CYLINDER_3_OFFSET_SIZE] = CYLINDER_3_SIZE;
		cylinder3DResult[cylinder3DResultOffset + CYLINDER_3_OFFSET_PHI_MAX] = phiMax;
		cylinder3DResult[cylinder3DResultOffset + CYLINDER_3_OFFSET_RADIUS] = radius;
		cylinder3DResult[cylinder3DResultOffset + CYLINDER_3_OFFSET_Z_MAX] = zMax;
		cylinder3DResult[cylinder3DResultOffset + CYLINDER_3_OFFSET_Z_MIN] = zMin;
		
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
		return disk3D[disk3DOffset + DISK_3_OFFSET_PHI_MAX];
	}
	
//	TODO: Add Javadocs!
	public static double disk3DGetRadiusInner(final double[] disk3D) {
		return disk3DGetRadiusInner(disk3D, 0);
	}
	
//	TODO: Add Javadocs!
	public static double disk3DGetRadiusInner(final double[] disk3D, final int disk3DOffset) {
		return disk3D[disk3DOffset + DISK_3_OFFSET_RADIUS_INNER];
	}
	
//	TODO: Add Javadocs!
	public static double disk3DGetRadiusOuter(final double[] disk3D) {
		return disk3DGetRadiusOuter(disk3D, 0);
	}
	
//	TODO: Add Javadocs!
	public static double disk3DGetRadiusOuter(final double[] disk3D, final int disk3DOffset) {
		return disk3D[disk3DOffset + DISK_3_OFFSET_RADIUS_OUTER];
	}
	
//	TODO: Add Javadocs!
	public static double disk3DGetZMax(final double[] disk3D) {
		return disk3DGetZMax(disk3D, 0);
	}
	
//	TODO: Add Javadocs!
	public static double disk3DGetZMax(final double[] disk3D, final int disk3DOffset) {
		return disk3D[disk3DOffset + DISK_3_OFFSET_Z_MAX];
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
		return disk3DSet(new double[DISK_3_SIZE], phiMax, radiusInner, radiusOuter, zMax);
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
		disk3DResult[disk3DResultOffset + DISK_3_OFFSET_ID] = DISK_3_ID;
		disk3DResult[disk3DResultOffset + DISK_3_OFFSET_SIZE] = DISK_3_SIZE;
		disk3DResult[disk3DResultOffset + DISK_3_OFFSET_PHI_MAX] = phiMax;
		disk3DResult[disk3DResultOffset + DISK_3_OFFSET_RADIUS_INNER] = radiusInner;
		disk3DResult[disk3DResultOffset + DISK_3_OFFSET_RADIUS_OUTER] = radiusOuter;
		disk3DResult[disk3DResultOffset + DISK_3_OFFSET_Z_MAX] = zMax;
		
		return disk3DResult;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	// Hyperboloid3D ///////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public static double hyperboloid3DGetAH(final double[] hyperboloid3D) {
		return hyperboloid3DGetAH(hyperboloid3D, 0);
	}
	
//	TODO: Add Javadocs!
	public static double hyperboloid3DGetAH(final double[] hyperboloid3D, final int hyperboloid3DOffset) {
		return hyperboloid3D[hyperboloid3DOffset + HYPERBOLOID_3_OFFSET_A_H];
	}
	
//	TODO: Add Javadocs!
	public static double hyperboloid3DGetCH(final double[] hyperboloid3D) {
		return hyperboloid3DGetCH(hyperboloid3D, 0);
	}
	
//	TODO: Add Javadocs!
	public static double hyperboloid3DGetCH(final double[] hyperboloid3D, final int hyperboloid3DOffset) {
		return hyperboloid3D[hyperboloid3DOffset + HYPERBOLOID_3_OFFSET_C_H];
	}
	
//	TODO: Add Javadocs!
	public static double hyperboloid3DGetPhiMax(final double[] hyperboloid3D) {
		return hyperboloid3DGetPhiMax(hyperboloid3D, 0);
	}
	
//	TODO: Add Javadocs!
	public static double hyperboloid3DGetPhiMax(final double[] hyperboloid3D, final int hyperboloid3DOffset) {
		return hyperboloid3D[hyperboloid3DOffset + HYPERBOLOID_3_OFFSET_PHI_MAX];
	}
	
//	TODO: Add Javadocs!
	public static double hyperboloid3DGetRMax(final double[] hyperboloid3D) {
		return hyperboloid3DGetRMax(hyperboloid3D, 0);
	}
	
//	TODO: Add Javadocs!
	public static double hyperboloid3DGetRMax(final double[] hyperboloid3D, final int hyperboloid3DOffset) {
		return hyperboloid3D[hyperboloid3DOffset + HYPERBOLOID_3_OFFSET_R_MAX];
	}
	
//	TODO: Add Javadocs!
	public static double hyperboloid3DGetZMax(final double[] hyperboloid3D) {
		return hyperboloid3DGetZMax(hyperboloid3D, 0);
	}
	
//	TODO: Add Javadocs!
	public static double hyperboloid3DGetZMax(final double[] hyperboloid3D, final int hyperboloid3DOffset) {
		return hyperboloid3D[hyperboloid3DOffset + HYPERBOLOID_3_OFFSET_Z_MAX];
	}
	
//	TODO: Add Javadocs!
	public static double hyperboloid3DGetZMin(final double[] hyperboloid3D) {
		return hyperboloid3DGetZMin(hyperboloid3D, 0);
	}
	
//	TODO: Add Javadocs!
	public static double hyperboloid3DGetZMin(final double[] hyperboloid3D, final int hyperboloid3DOffset) {
		return hyperboloid3D[hyperboloid3DOffset + HYPERBOLOID_3_OFFSET_Z_MIN];
	}
	
//	TODO: Add Javadocs!
	public static double hyperboloid3DIntersection(final double[] ray3D, final double[] hyperboloid3D) {
		return hyperboloid3DIntersection(ray3D, hyperboloid3D, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double hyperboloid3DIntersection(final double[] ray3D, final double[] hyperboloid3D, final int ray3DOffset, final int hyperboloid3DOffset) {
		final double[] point3DOrigin = ray3DGetOrigin(ray3D, point3D(), ray3DOffset, 0);
		
		final double[] vector3DDirection = ray3DGetDirection(ray3D, vector3D(), ray3DOffset, 0);
		
		final double tMinimum = ray3DGetTMinimum(ray3D, ray3DOffset);
		final double tMaximum = ray3DGetTMaximum(ray3D, ray3DOffset);
		
		final double[] point3DA = hyperboloid3DGetA(hyperboloid3D, point3D(), hyperboloid3DOffset, 0);
		final double[] point3DB = hyperboloid3DGetB(hyperboloid3D, point3D(), hyperboloid3DOffset, 0);
		
		final double phiMax = hyperboloid3DGetPhiMax(hyperboloid3D, hyperboloid3DOffset);
		final double aH = hyperboloid3DGetAH(hyperboloid3D, hyperboloid3DOffset);
		final double cH = hyperboloid3DGetCH(hyperboloid3D, hyperboloid3DOffset);
		final double zMax = hyperboloid3DGetZMax(hyperboloid3D, hyperboloid3DOffset);
		final double zMin = hyperboloid3DGetZMin(hyperboloid3D, hyperboloid3DOffset);
		
		final double a = aH * vector3DGetX(vector3DDirection) * vector3DGetX(vector3DDirection) + aH * vector3DGetY(vector3DDirection) * vector3DGetY(vector3DDirection) - cH * vector3DGetZ(vector3DDirection) * vector3DGetZ(vector3DDirection);
		final double b = 2.0D * (aH * vector3DGetX(vector3DDirection) * point3DGetX(point3DOrigin) + aH * vector3DGetY(vector3DDirection) * point3DGetY(point3DOrigin) - cH * vector3DGetZ(vector3DDirection) * point3DGetZ(point3DOrigin));
		final double c = aH * point3DGetX(point3DOrigin) * point3DGetX(point3DOrigin) + aH * point3DGetY(point3DOrigin) * point3DGetY(point3DOrigin) - cH * point3DGetZ(point3DOrigin) * point3DGetZ(point3DOrigin) - 1.0D;
		
		final double[] ts = solveQuadraticSystem(a, b, c);
		
		for(int i = 0; i < ts.length; i++) {
			final double t = ts[i];
			
			if(isNaN(t)) {
				return NaN;
			}
			
			if(t > tMinimum && t < tMaximum) {
				final double[] point3D = point3DAdd(point3DOrigin, vector3DDirection, t);
				final double[] point3DTransformed = point3DLerp(point3DA, point3DB, (point3DGetZ(point3D) - point3DGetZ(point3DA)) / (point3DGetZ(point3DB) - point3DGetZ(point3DA)));
				
				final double x = point3DGetX(point3D) * point3DGetX(point3DTransformed) + point3DGetY(point3D) * point3DGetY(point3DTransformed);
				final double y = point3DGetY(point3D) * point3DGetX(point3DTransformed) - point3DGetX(point3D) * point3DGetY(point3DTransformed);
				
				final double phi = getOrAdd(atan2(y, x), 0.0D, PI_MULTIPLIED_BY_2);
				
				if(point3DGetZ(point3D) >= zMin && point3DGetZ(point3D) <= zMax && phi <= phiMax) {
					return t;
				}
			}
		}
		
		return NaN;
	}
	
//	TODO: Add Javadocs!
	public static double hyperboloid3DSurfaceArea(final double[] hyperboloid3D) {
		return hyperboloid3DSurfaceArea(hyperboloid3D, 0);
	}
	
//	TODO: Add Javadocs!
	public static double hyperboloid3DSurfaceArea(final double[] hyperboloid3D, final int hyperboloid3DOffset) {
		final double[] point3DA = hyperboloid3DGetA(hyperboloid3D, point3D(), hyperboloid3DOffset, 0);
		final double[] point3DB = hyperboloid3DGetB(hyperboloid3D, point3D(), hyperboloid3DOffset, 0);
		
		final double aX11 = point3DGetX(point3DA);
		final double aX21 = aX11 * aX11;
		final double aX31 = aX21 * aX11;
		final double aX41 = aX31 * aX11;
		final double aX42 = aX41 * 2.0D;
		final double aY11 = point3DGetY(point3DA);
		final double aY21 = aY11 * aY11;
		final double aY25 = aY21 * 5.0D;
		final double aZ11 = point3DGetZ(point3DA);
		final double aZ21 = aZ11 * aZ11;
		
		final double bX11 = point3DGetX(point3DB);
		final double bX21 = bX11 * bX11;
		final double bX31 = bX21 * bX11;
		final double bX41 = bX31 * bX11;
		final double bX42 = bX41 * 2.0D;
		final double bY11 = point3DGetY(point3DB);
		final double bY21 = bY11 * bY11;
		final double bY24 = bY21 * 4.0D;
		final double bY25 = bY21 * 5.0D;
		final double bZ11 = point3DGetZ(point3DB);
		final double bZ21 = bZ11 * bZ11;
		
		final double cX11 = aX11 * bX11;
		final double cX12 = cX11 * 2.0D;
		final double cY11 = aY11 * bY11;
		final double cY12 = cY11 * 2.0D;
		final double cY15 = cY11 * 5.0D;
		final double cZ11 = aZ11 * bZ11;
		final double cZ12 = cZ11 * 2.0D;
		
		final double dY11 = (aY11 - bY11) * (aY11 - bY11);
		final double dZ11 = (aZ11 - bZ11) * (aZ11 - bZ11);
		final double dZ12 = dZ11 * 2.0D;
		
		final double a = aX42;
		final double b = aX31 * bX11 * 2.0D;
		final double c = bX42;
		final double d = (aY21 + cY11 + bY21) * (dY11 + dZ11) * 2.0D;
		final double e = bX21 * (aY25 + cY12 - bY24 + dZ12);
		final double f = aX21 * ((aY21 * -4.0D) + cY12 + bY25 + dZ12);
		final double g = cX12 * (bX21 - aY21 + cY15 - bY21 - aZ21 + cZ12 - bZ21);
		final double h = a - b + c + d + e + f - g;
		
		final double phiMax = hyperboloid3DGetPhiMax(hyperboloid3D, hyperboloid3DOffset);
		
		final double surfaceArea = phiMax / 6.0D * h;
		
		return surfaceArea;
	}
	
//	TODO: Add Javadocs!
	public static double[] hyperboloid3D() {
		return hyperboloid3D(toRadians(360.0D));
	}
	
//	TODO: Add Javadocs!
	public static double[] hyperboloid3D(final double phiMax) {
		return hyperboloid3D(phiMax, point3D(0.0001D, 0.0001D, 0.0D));
	}
	
//	TODO: Add Javadocs!
	public static double[] hyperboloid3D(final double phiMax, final double[] point3DA) {
		return hyperboloid3D(phiMax, point3DA, point3D(1.0D, 1.0D, 1.0D));
	}
	
//	TODO: Add Javadocs!
	public static double[] hyperboloid3D(final double phiMax, final double[] point3DA, final double[] point3DB) {
		return hyperboloid3D(phiMax, point3DA, point3DB, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] hyperboloid3D(final double phiMax, final double[] point3DA, final double[] point3DB, final int point3DAOffset, final int point3DBOffset) {
		return hyperboloid3DSet(new double[HYPERBOLOID_3_SIZE], phiMax, point3DA, point3DB, 0, point3DAOffset, point3DBOffset);
	}
	
//	TODO: Add Javadocs!
	public static double[] hyperboloid3DComputeOrthonormalBasis(final double[] ray3D, final double[] hyperboloid3D, final double t) {
		return hyperboloid3DComputeOrthonormalBasis(ray3D, hyperboloid3D, t, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] hyperboloid3DComputeOrthonormalBasis(final double[] ray3D, final double[] hyperboloid3D, final double t, final int ray3DOffset, final int hyperboloid3DOffset) {
		final double[] point3DOrigin = ray3DGetOrigin(ray3D, point3D(), ray3DOffset, 0);
		
		final double[] vector3DDirection = ray3DGetDirection(ray3D, vector3D(), ray3DOffset, 0);
		
		final double[] point3DA = hyperboloid3DGetA(hyperboloid3D, point3D(), hyperboloid3DOffset, 0);
		final double[] point3DB = hyperboloid3DGetB(hyperboloid3D, point3D(), hyperboloid3DOffset, 0);
		
		final double phiMax = hyperboloid3DGetPhiMax(hyperboloid3D, hyperboloid3DOffset);
		
		final double[] point3D = point3DAdd(point3DOrigin, vector3DDirection, t);
		final double[] point3DTransformed = point3DLerp(point3DA, point3DB, (point3DGetZ(point3D) - point3DGetZ(point3DA)) / (point3DGetZ(point3DB) - point3DGetZ(point3DA)));
		
		final double x = point3DGetX(point3D) * point3DGetX(point3DTransformed) + point3DGetY(point3D) * point3DGetY(point3DTransformed);
		final double y = point3DGetY(point3D) * point3DGetX(point3DTransformed) - point3DGetX(point3D) * point3DGetY(point3DTransformed);
		
		final double phi = getOrAdd(atan2(y, x), 0.0D, PI_MULTIPLIED_BY_2);
		
		final double cosPhi = cos(phi);
		final double sinPhi = sin(phi);
		
		final double uX = -phiMax * point3DGetY(point3DTransformed);
		final double uY = +phiMax * point3DGetX(point3DTransformed);
		final double uZ = 0.0D;
		
		final double vX = (point3DGetX(point3DB) - point3DGetX(point3DA)) * cosPhi - (point3DGetY(point3DB) - point3DGetY(point3DA)) * sinPhi;
		final double vY = (point3DGetX(point3DB) - point3DGetX(point3DA)) * sinPhi + (point3DGetY(point3DB) - point3DGetY(point3DA)) * cosPhi;
		final double vZ = point3DGetZ(point3DB) - point3DGetZ(point3DA);
		
		final double[] vector3DU = vector3DNormalize(vector3D(uX, uY, uZ));
		final double[] vector3DV = vector3DNormalize(vector3D(vX, vY, vZ));
		final double[] vector3DW = vector3DCrossProduct(vector3DU, vector3DV);
		
		return orthonormalBasis33D(vector3DU, vector3DV, vector3DW);
	}
	
//	TODO: Add Javadocs!
	public static double[] hyperboloid3DComputeSurfaceNormal(final double[] ray3D, final double[] hyperboloid3D, final double t) {
		return hyperboloid3DComputeSurfaceNormal(ray3D, hyperboloid3D, t, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] hyperboloid3DComputeSurfaceNormal(final double[] ray3D, final double[] hyperboloid3D, final double t, final int ray3DOffset, final int hyperboloid3DOffset) {
		return orthonormalBasis33DGetW(hyperboloid3DComputeOrthonormalBasis(ray3D, hyperboloid3D, t, ray3DOffset, hyperboloid3DOffset));
	}
	
//	TODO: Add Javadocs!
	public static double[] hyperboloid3DComputeTextureCoordinates(final double[] ray3D, final double[] hyperboloid3D, final double t) {
		return hyperboloid3DComputeTextureCoordinates(ray3D, hyperboloid3D, t, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] hyperboloid3DComputeTextureCoordinates(final double[] ray3D, final double[] hyperboloid3D, final double t, final int ray3DOffset, final int hyperboloid3DOffset) {
		final double[] point3DOrigin = ray3DGetOrigin(ray3D, point3D(), ray3DOffset, 0);
		
		final double[] vector3DDirection = ray3DGetDirection(ray3D, vector3D(), ray3DOffset, 0);
		
		final double[] point3DA = hyperboloid3DGetA(hyperboloid3D, point3D(), hyperboloid3DOffset, 0);
		final double[] point3DB = hyperboloid3DGetB(hyperboloid3D, point3D(), hyperboloid3DOffset, 0);
		
		final double phiMax = hyperboloid3DGetPhiMax(hyperboloid3D, hyperboloid3DOffset);
		
		final double[] point3D = point3DAdd(point3DOrigin, vector3DDirection, t);
		
		final double v = (point3DGetZ(point3D) - point3DGetZ(point3DA)) / (point3DGetZ(point3DB) - point3DGetZ(point3DA));
		
		final double[] point3DTransformed = point3DLerp(point3DA, point3DB, v);
		
		final double x = point3DGetX(point3D) * point3DGetX(point3DTransformed) + point3DGetY(point3D) * point3DGetY(point3DTransformed);
		final double y = point3DGetY(point3D) * point3DGetX(point3DTransformed) - point3DGetX(point3D) * point3DGetY(point3DTransformed);
		
		final double phi = getOrAdd(atan2(y, x), 0.0D, PI_MULTIPLIED_BY_2);
		
		final double u = phi / phiMax;
		
		return point2D(u, v);
	}
	
//	TODO: Add Javadocs!
	public static double[] hyperboloid3DGetA(final double[] hyperboloid3D) {
		return hyperboloid3DGetA(hyperboloid3D, point3D());
	}
	
//	TODO: Add Javadocs!
	public static double[] hyperboloid3DGetA(final double[] hyperboloid3D, final double[] point3DAResult) {
		return hyperboloid3DGetA(hyperboloid3D, point3DAResult, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] hyperboloid3DGetA(final double[] hyperboloid3D, final double[] point3DAResult, final int hyperboloid3DOffset, final int point3DAResultOffset) {
		return point3DSet(point3DAResult, hyperboloid3D, point3DAResultOffset, hyperboloid3DOffset + HYPERBOLOID_3_OFFSET_A);
	}
	
//	TODO: Add Javadocs!
	public static double[] hyperboloid3DGetB(final double[] hyperboloid3D) {
		return hyperboloid3DGetB(hyperboloid3D, point3D());
	}
	
//	TODO: Add Javadocs!
	public static double[] hyperboloid3DGetB(final double[] hyperboloid3D, final double[] point3DBResult) {
		return hyperboloid3DGetB(hyperboloid3D, point3DBResult, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] hyperboloid3DGetB(final double[] hyperboloid3D, final double[] point3DBResult, final int hyperboloid3DOffset, final int point3DBResultOffset) {
		return point3DSet(point3DBResult, hyperboloid3D, point3DBResultOffset, hyperboloid3DOffset + HYPERBOLOID_3_OFFSET_B);
	}
	
//	TODO: Add Javadocs!
	public static double[] hyperboloid3DSet(final double[] hyperboloid3DResult, final double phiMax, final double[] point3DA, final double[] point3DB) {
		return hyperboloid3DSet(hyperboloid3DResult, phiMax, point3DA, point3DB, 0, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] hyperboloid3DSet(final double[] hyperboloid3DResult, final double phiMax, final double[] point3DA, final double[] point3DB, final int hyperboloid3DResultOffset, final int point3DAOffset, final int point3DBOffset) {
		final double[] point3DCurrentA = point3DSet(point3D(), point3DA, 0, point3DAOffset);
		final double[] point3DCurrentB = point3DSet(point3D(), point3DB, 0, point3DBOffset);
		
		if(isZero(point3DGetZ(point3DCurrentB))) {
			point3DSwap(point3DCurrentA, point3DCurrentB);
		}
		
		final double[] point3DCurrentC = point3DSet(point3D(), point3DCurrentA, 0, 0);
		
		double aH = Double.POSITIVE_INFINITY;
		double cH = Double.POSITIVE_INFINITY;
		
		for(int i = 0; i < 10 && (isInfinite(aH) || isNaN(aH)); i++) {
			point3DAdd(point3DCurrentC, vector3DMultiply(vector3DDirection(point3DCurrentA, point3DCurrentB), 2.0D), point3DCurrentC);
			
			final double bX = point3DGetX(point3DCurrentB);
			final double bY = point3DGetY(point3DCurrentB);
			final double bZ = point3DGetZ(point3DCurrentB);
			
			final double cX = point3DGetX(point3DCurrentC);
			final double cY = point3DGetY(point3DCurrentC);
			final double cZ = point3DGetZ(point3DCurrentC);
			
			final double b = bX * bX + bY * bY;
			final double c = cX * cX + cY * cY;
			
			aH = (1.0D / c - (cZ * cZ) / (c * bZ * bZ)) / (1.0D - (b * cZ * cZ) / (c * bZ * bZ));
			cH = (aH * b - 1.0D) / (bZ * bZ);
		}
		
		final double aX = point3DGetX(point3DCurrentA);
		final double aY = point3DGetY(point3DCurrentA);
		final double aZ = point3DGetZ(point3DCurrentA);
		
		final double bX = point3DGetX(point3DCurrentB);
		final double bY = point3DGetY(point3DCurrentB);
		final double bZ = point3DGetZ(point3DCurrentB);
		
		final double rMax = max(sqrt(aX * aX + aY * aY), sqrt(bX * bX + bY * bY));
		final double zMax = max(aZ, bZ);
		final double zMin = min(aZ, bZ);
		
		hyperboloid3DResult[hyperboloid3DResultOffset + HYPERBOLOID_3_OFFSET_ID] = HYPERBOLOID_3_ID;
		hyperboloid3DResult[hyperboloid3DResultOffset + HYPERBOLOID_3_OFFSET_SIZE] = HYPERBOLOID_3_SIZE;
		hyperboloid3DResult[hyperboloid3DResultOffset + HYPERBOLOID_3_OFFSET_PHI_MAX] = phiMax;
		hyperboloid3DResult[hyperboloid3DResultOffset + HYPERBOLOID_3_OFFSET_A_H] = aH;
		hyperboloid3DResult[hyperboloid3DResultOffset + HYPERBOLOID_3_OFFSET_C_H] = cH;
		hyperboloid3DResult[hyperboloid3DResultOffset + HYPERBOLOID_3_OFFSET_R_MAX] = rMax;
		hyperboloid3DResult[hyperboloid3DResultOffset + HYPERBOLOID_3_OFFSET_Z_MAX] = zMax;
		hyperboloid3DResult[hyperboloid3DResultOffset + HYPERBOLOID_3_OFFSET_Z_MIN] = zMin;
		
		point3DSet(hyperboloid3DResult, point3DCurrentA, hyperboloid3DResultOffset + HYPERBOLOID_3_OFFSET_A, 0);
		point3DSet(hyperboloid3DResult, point3DCurrentB, hyperboloid3DResultOffset + HYPERBOLOID_3_OFFSET_B, 0);
		
		return hyperboloid3DResult;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	// LineSegment2D ///////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public static boolean lineSegment2DContainsPoint2D(final double[] lineSegment2D, final double[] point2D) {
		return lineSegment2DContainsPoint2D(lineSegment2D, point2D, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static boolean lineSegment2DContainsPoint2D(final double[] lineSegment2D, final double[] point2D, final int lineSegment2DOffset, final int point2DOffset) {
		final double[] point2DA = lineSegment2DGetA(lineSegment2D, point2D(), lineSegment2DOffset, 0);
		final double[] point2DB = lineSegment2DGetB(lineSegment2D, point2D(), lineSegment2DOffset, 0);
		final double[] point2DP = point2DSet(point2D(), point2D, 0, point2DOffset);
		
		if(point2DEquals(point2DP, point2DA) || point2DEquals(point2DP, point2DB)) {
			return true;
		}
		
		final double[] vector2DAB = vector2DDirection(point2DA, point2DB);
		final double[] vector2DAP = vector2DDirection(point2DA, point2DP);
		
		final double crossProduct = vector2DCrossProduct(vector2DAP, vector2DAB);
		
		if(!isZero(crossProduct)) {
			return false;
		}
		
		final double aX = point2DGetX(point2DA);
		final double aY = point2DGetY(point2DA);
		final double bX = point2DGetX(point2DB);
		final double bY = point2DGetY(point2DB);
		final double pX = point2DGetX(point2DP);
		final double pY = point2DGetY(point2DP);
		
		final double x = vector2DGetX(vector2DAB);
		final double y = vector2DGetY(vector2DAB);
		
		final boolean containsX = x > 0.0D ? aX <= pX && pX <= bX : bX <= pX && pX <= aX;
		final boolean containsY = y > 0.0D ? aY <= pY && pY <= bY : bY <= pY && pY <= aY;
		final boolean contains = abs(x) >= abs(y) ? containsX : containsY;
		
		return contains;
	}
	
//	TODO: Add Javadocs!
	public static double[] lineSegment2D() {
		return lineSegment2D(point2D(0.0D, 0.0D), point2D(1.0D, 1.0D));
	}
	
//	TODO: Add Javadocs!
	public static double[] lineSegment2D(final double[] point2DA, final double[] point2DB) {
		return lineSegment2D(point2DA, point2DB, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] lineSegment2D(final double[] point2DA, final double[] point2DB, final int point2DAOffset, final int point2DBOffset) {
		return lineSegment2DSet(new double[LINE_SEGMENT_2_SIZE], point2DA, point2DB, 0, point2DAOffset, point2DBOffset);
	}
	
//	TODO: Add Javadocs!
	public static double[] lineSegment2DGetA(final double[] lineSegment2D) {
		return lineSegment2DGetA(lineSegment2D, point2D());
	}
	
//	TODO: Add Javadocs!
	public static double[] lineSegment2DGetA(final double[] lineSegment2D, final double[] point2DAResult) {
		return lineSegment2DGetA(lineSegment2D, point2DAResult, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] lineSegment2DGetA(final double[] lineSegment2D, final double[] point2DAResult, final int lineSegment2DOffset, final int point2DAResultOffset) {
		return point2DSet(point2DAResult, lineSegment2D, point2DAResultOffset, lineSegment2DOffset + LINE_SEGMENT_2_OFFSET_A);
	}
	
//	TODO: Add Javadocs!
	public static double[] lineSegment2DGetB(final double[] lineSegment2D) {
		return lineSegment2DGetB(lineSegment2D, point2D());
	}
	
//	TODO: Add Javadocs!
	public static double[] lineSegment2DGetB(final double[] lineSegment2D, final double[] point2DBResult) {
		return lineSegment2DGetB(lineSegment2D, point2DBResult, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] lineSegment2DGetB(final double[] lineSegment2D, final double[] point2DBResult, final int lineSegment2DOffset, final int point2DBResultOffset) {
		return point2DSet(point2DBResult, lineSegment2D, point2DBResultOffset, lineSegment2DOffset + LINE_SEGMENT_2_OFFSET_B);
	}
	
//	TODO: Add Javadocs!
	public static double[] lineSegment2DSet(final double[] lineSegment2DResult, final double[] point2DA, final double[] point2DB) {
		return lineSegment2DSet(lineSegment2DResult, point2DA, point2DB, 0, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] lineSegment2DSet(final double[] lineSegment2DResult, final double[] point2DA, final double[] point2DB, final int lineSegment2DResultOffset, final int point2DAOffset, final int point2DBOffset) {
		lineSegment2DResult[lineSegment2DResultOffset + LINE_SEGMENT_2_OFFSET_ID] = LINE_SEGMENT_2_ID;
		lineSegment2DResult[lineSegment2DResultOffset + LINE_SEGMENT_2_OFFSET_SIZE] = LINE_SEGMENT_2_SIZE;
		
		point2DSet(lineSegment2DResult, point2DA, lineSegment2DResultOffset + LINE_SEGMENT_2_OFFSET_A, point2DAOffset);
		point2DSet(lineSegment2DResult, point2DB, lineSegment2DResultOffset + LINE_SEGMENT_2_OFFSET_B, point2DBOffset);
		
		return lineSegment2DResult;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	// LineSegment3D ///////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public static boolean lineSegment3DContainsPoint3D(final double[] lineSegment3D, final double[] point3D) {
		return lineSegment3DContainsPoint3D(lineSegment3D, point3D, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static boolean lineSegment3DContainsPoint3D(final double[] lineSegment3D, final double[] point3D, final int lineSegment3DOffset, final int point3DOffset) {
		final double[] point3DA = lineSegment3DGetA(lineSegment3D, point3D(), lineSegment3DOffset, 0);
		final double[] point3DB = lineSegment3DGetB(lineSegment3D, point3D(), lineSegment3DOffset, 0);
		final double[] point3DP = point3DSet(point3D(), point3D, 0, point3DOffset);
		
		if(point3DEquals(point3DP, point3DA) || point3DEquals(point3DP, point3DB)) {
			return true;
		}
		
		final double[] vector3DA = vector3DFromPoint3D(point3DA);
		final double[] vector3DB = vector3DFromPoint3D(point3DB);
		final double[] vector3DP = vector3DFromPoint3D(point3DP);
		
		final double[] vector3DBA = vector3DDirection(point3DB, point3DA);
		final double[] vector3DBP = vector3DDirection(point3DB, point3DP);
		
		final double t = vector3DDotProduct(vector3DBA, vector3DBP) / point3DDistanceSquared(point3DA, point3DB);
		
		final double[] vector3DProjection = vector3DLerp(vector3DB, vector3DA, t);
		
		final double lineWidth = PI * 0.5D / 4096.0D;
		final double lineWidthCos = cos(lineWidth);
		
		final boolean contains = vector3DDotProduct(vector3DProjection, vector3DP) / vector3DLength(vector3DProjection) / vector3DLength(vector3DP) >= lineWidthCos;
		
		return contains;
	}
	
//	TODO: Add Javadocs!
	public static double[] lineSegment3D() {
		return lineSegment3D(point3D(0.0D, 0.0D, 0.0D), point3D(1.0D, 1.0D, 1.0D));
	}
	
//	TODO: Add Javadocs!
	public static double[] lineSegment3D(final double[] point3DA, final double[] point3DB) {
		return lineSegment3D(point3DA, point3DB, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] lineSegment3D(final double[] point3DA, final double[] point3DB, final int point3DAOffset, final int point3DBOffset) {
		return lineSegment3DSet(new double[LINE_SEGMENT_3_SIZE], point3DA, point3DB, 0, point3DAOffset, point3DBOffset);
	}
	
//	TODO: Add Javadocs!
	public static double[] lineSegment3DGetA(final double[] lineSegment3D) {
		return lineSegment3DGetA(lineSegment3D, point3D());
	}
	
//	TODO: Add Javadocs!
	public static double[] lineSegment3DGetA(final double[] lineSegment3D, final double[] point3DAResult) {
		return lineSegment3DGetA(lineSegment3D, point3DAResult, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] lineSegment3DGetA(final double[] lineSegment3D, final double[] point3DAResult, final int lineSegment3DOffset, final int point3DAResultOffset) {
		return point3DSet(point3DAResult, lineSegment3D, point3DAResultOffset, lineSegment3DOffset + LINE_SEGMENT_3_OFFSET_A);
	}
	
//	TODO: Add Javadocs!
	public static double[] lineSegment3DGetB(final double[] lineSegment3D) {
		return lineSegment3DGetB(lineSegment3D, point3D());
	}
	
//	TODO: Add Javadocs!
	public static double[] lineSegment3DGetB(final double[] lineSegment3D, final double[] point3DBResult) {
		return lineSegment3DGetB(lineSegment3D, point3DBResult, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] lineSegment3DGetB(final double[] lineSegment3D, final double[] point3DBResult, final int lineSegment3DOffset, final int point3DBResultOffset) {
		return point3DSet(point3DBResult, lineSegment3D, point3DBResultOffset, lineSegment3DOffset + LINE_SEGMENT_3_OFFSET_B);
	}
	
//	TODO: Add Javadocs!
	public static double[] lineSegment3DSet(final double[] lineSegment3DResult, final double[] point3DA, final double[] point3DB) {
		return lineSegment3DSet(lineSegment3DResult, point3DA, point3DB, 0, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] lineSegment3DSet(final double[] lineSegment3DResult, final double[] point3DA, final double[] point3DB, final int lineSegment3DResultOffset, final int point3DAOffset, final int point3DBOffset) {
		lineSegment3DResult[lineSegment3DResultOffset + LINE_SEGMENT_3_OFFSET_ID] = LINE_SEGMENT_3_ID;
		lineSegment3DResult[lineSegment3DResultOffset + LINE_SEGMENT_3_OFFSET_SIZE] = LINE_SEGMENT_3_SIZE;
		
		point3DSet(lineSegment3DResult, point3DA, lineSegment3DResultOffset + LINE_SEGMENT_3_OFFSET_A, point3DAOffset);
		point3DSet(lineSegment3DResult, point3DB, lineSegment3DResultOffset + LINE_SEGMENT_3_OFFSET_B, point3DBOffset);
		
		return lineSegment3DResult;
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
		return paraboloid3D[paraboloid3DOffset + PARABOLOID_3_OFFSET_PHI_MAX];
	}
	
//	TODO: Add Javadocs!
	public static double paraboloid3DGetRadius(final double[] paraboloid3D) {
		return paraboloid3DGetRadius(paraboloid3D, 0);
	}
	
//	TODO: Add Javadocs!
	public static double paraboloid3DGetRadius(final double[] paraboloid3D, final int paraboloid3DOffset) {
		return paraboloid3D[paraboloid3DOffset + PARABOLOID_3_OFFSET_RADIUS];
	}
	
//	TODO: Add Javadocs!
	public static double paraboloid3DGetZMax(final double[] paraboloid3D) {
		return paraboloid3DGetZMax(paraboloid3D, 0);
	}
	
//	TODO: Add Javadocs!
	public static double paraboloid3DGetZMax(final double[] paraboloid3D, final int paraboloid3DOffset) {
		return paraboloid3D[paraboloid3DOffset + PARABOLOID_3_OFFSET_Z_MAX];
	}
	
//	TODO: Add Javadocs!
	public static double paraboloid3DGetZMin(final double[] paraboloid3D) {
		return paraboloid3DGetZMin(paraboloid3D, 0);
	}
	
//	TODO: Add Javadocs!
	public static double paraboloid3DGetZMin(final double[] paraboloid3D, final int paraboloid3DOffset) {
		return paraboloid3D[paraboloid3DOffset + PARABOLOID_3_OFFSET_Z_MIN];
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
		return paraboloid3DSet(new double[PARABOLOID_3_SIZE], phiMax, radius, zMax, zMin);
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
		paraboloid3DResult[paraboloid3DResultOffset + PARABOLOID_3_OFFSET_ID] = PARABOLOID_3_ID;
		paraboloid3DResult[paraboloid3DResultOffset + PARABOLOID_3_OFFSET_SIZE] = PARABOLOID_3_SIZE;
		paraboloid3DResult[paraboloid3DResultOffset + PARABOLOID_3_OFFSET_PHI_MAX] = phiMax;
		paraboloid3DResult[paraboloid3DResultOffset + PARABOLOID_3_OFFSET_RADIUS] = radius;
		paraboloid3DResult[paraboloid3DResultOffset + PARABOLOID_3_OFFSET_Z_MAX] = zMax;
		paraboloid3DResult[paraboloid3DResultOffset + PARABOLOID_3_OFFSET_Z_MIN] = zMin;
		
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
		
		final double[] vector3DSurfaceNormal = plane3DGetSurfaceNormal(plane3D, vector3D(), plane3DOffset, 0);
		
		final double surfaceNormalDotDirection = vector3DDotProduct(vector3DSurfaceNormal, vector3DDirection);
		
		if(isZero(surfaceNormalDotDirection)) {
			return NaN;
		}
		
		final double[] point3DA = plane3DGetA(plane3D, point3D(), plane3DOffset, 0);
		
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
		return plane3DSet(new double[PLANE_3_SIZE], point3DA, point3DB, point3DC, 0, point3DAOffset, point3DBOffset, point3DCOffset);
	}
	
//	TODO: Add Javadocs!
	public static double[] plane3DComputeOrthonormalBasis(final double[] ray3D, final double[] plane3D, final double t) {
		return plane3DComputeOrthonormalBasis(ray3D, plane3D, t, 0, 0);
	}
	
//	TODO: Add Javadocs!
	@SuppressWarnings("unused")
	public static double[] plane3DComputeOrthonormalBasis(final double[] ray3D, final double[] plane3D, final double t, final int ray3DOffset, final int plane3DOffset) {
		final double[] vector3DW = plane3DGetSurfaceNormal(plane3D, vector3D(), plane3DOffset, 0);
		
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
		
		final double[] vector3DSurfaceNormal = plane3DGetSurfaceNormal(plane3D, vector3D(), plane3DOffset, 0);
		
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
		return point3DSet(point3DAResult, plane3D, point3DAResultOffset, plane3DOffset + PLANE_3_OFFSET_A);
	}
	
//	TODO: Add Javadocs!
	public static double[] plane3DGetB(final double[] plane3D) {
		return plane3DGetB(plane3D, point3D());
	}
	
//	TODO: Add Javadocs!
	public static double[] plane3DGetB(final double[] plane3D, final double[] point3DBResult) {
		return plane3DGetB(plane3D, point3DBResult, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] plane3DGetB(final double[] plane3D, final double[] point3DBResult, final int plane3DOffset, final int point3DBResultOffset) {
		return point3DSet(point3DBResult, plane3D, point3DBResultOffset, plane3DOffset + PLANE_3_OFFSET_B);
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
		return point3DSet(point3DCResult, plane3D, point3DCResultOffset, plane3DOffset + PLANE_3_OFFSET_C);
	}
	
//	TODO: Add Javadocs!
	public static double[] plane3DGetSurfaceNormal(final double[] plane3D) {
		return plane3DGetSurfaceNormal(plane3D, vector3D());
	}
	
//	TODO: Add Javadocs!
	public static double[] plane3DGetSurfaceNormal(final double[] plane3D, final double[] vector3DSurfaceNormalResult) {
		return plane3DGetSurfaceNormal(plane3D, vector3DSurfaceNormalResult, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] plane3DGetSurfaceNormal(final double[] plane3D, final double[] vector3DSurfaceNormalResult, final int plane3DOffset, final int vector3DSurfaceNormalResultOffset) {
		final double[] point3DA = plane3DGetA(plane3D, point3D(), plane3DOffset, 0);
		final double[] point3DB = plane3DGetB(plane3D, point3D(), plane3DOffset, 0);
		final double[] point3DC = plane3DGetC(plane3D, point3D(), plane3DOffset, 0);
		
		final double[] vector3DSurfaceNormal = vector3DNormalNormalized(point3DA, point3DB, point3DC, vector3DSurfaceNormalResult, 0, 0, 0, vector3DSurfaceNormalResultOffset);
		
		return vector3DSurfaceNormal;
	}
	
//	TODO: Add Javadocs!
	public static double[] plane3DSet(final double[] plane3DResult, final double[] point3DA, final double[] point3DB, final double[] point3DC) {
		return plane3DSet(plane3DResult, point3DA, point3DB, point3DC, 0, 0, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] plane3DSet(final double[] plane3DResult, final double[] point3DA, final double[] point3DB, final double[] point3DC, final int plane3DResultOffset, final int point3DAOffset, final int point3DBOffset, final int point3DCOffset) {
		plane3DResult[plane3DResultOffset + PLANE_3_OFFSET_ID] = PLANE_3_ID;
		plane3DResult[plane3DResultOffset + PLANE_3_OFFSET_SIZE] = PLANE_3_SIZE;
		
		point3DSet(plane3DResult, point3DA, plane3DResultOffset + PLANE_3_OFFSET_A, point3DAOffset);
		point3DSet(plane3DResult, point3DB, plane3DResultOffset + PLANE_3_OFFSET_B, point3DBOffset);
		point3DSet(plane3DResult, point3DC, plane3DResultOffset + PLANE_3_OFFSET_C, point3DCOffset);
		
		return plane3DResult;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	// Polygon2D ///////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public static boolean polygon2DContainsPoint2D(final double[] polygon2D, final double[] point2D) {
		return polygon2DContainsPoint2D(polygon2D, point2D, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static boolean polygon2DContainsPoint2D(final double[] polygon2D, final double[] point2D, final int polygon2DOffset, final int point2DOffset) {
		final double[] point2DP = point2DSet(point2D(), point2D, 0, point2DOffset);
		
		final int point2DCount = polygon2DGetPoint2DCount(polygon2D, polygon2DOffset);
		
		final double pX = point2DGetX(point2DP);
		final double pY = point2DGetY(point2DP);
		
		boolean isInside = false;
		
		for(int i = 0, j = point2DCount - 1; i < point2DCount; j = i, i++) {
			final double[] point2DI = polygon2DGetPoint2D(polygon2D, i, point2D(), polygon2DOffset, 0);
			final double[] point2DJ = polygon2DGetPoint2D(polygon2D, j, point2D(), polygon2DOffset, 0);
			
			final double[] lineSegment2D = lineSegment2D(point2DI, point2DJ);
			
			if(lineSegment2DContainsPoint2D(lineSegment2D, point2DP)) {
				isInside = true;
				
				break;
			}
			
			final double iX = point2DGetX(point2DI);
			final double iY = point2DGetY(point2DI);
			final double jX = point2DGetX(point2DJ);
			final double jY = point2DGetY(point2DJ);
			
			if((iY > pY) != (jY > pY) && pX < (jX - iX) * (pY - iY) / (jY - iY) + iX) {
				isInside = !isInside;
			}
		}
		
		return isInside;
	}
	
//	TODO: Add Javadocs!
	public static double[] polygon2D() {
		return polygon2D(merge(point2D(-2.0D, 2.0D), point2D(0.0D, 3.0D), point2D(2.0D, 2.0D), point2D(2.0D, -2.0D), point2D(-2.0D, -2.0D)));
	}
	
//	TODO: Add Javadocs!
	public static double[] polygon2D(final double[] point2Ds) {
		return polygon2D(point2Ds, point2Ds.length / 2);
	}
	
//	TODO: Add Javadocs!
	public static double[] polygon2D(final double[] point2Ds, final int point2DCount) {
		return polygon2D(point2Ds, point2DCount, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] polygon2D(final double[] point2Ds, final int point2DCount, final int point2DsOffset) {
		return polygon2DSet(new double[3 + point2DCount * 2], point2Ds, point2DCount, 0, point2DsOffset);
	}
	
//	TODO: Add Javadocs!
	public static double[] polygon2DFromPolygon3D(final double[] polygon3D) {
		return polygon2DFromPolygon3D(polygon3D, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] polygon2DFromPolygon3D(final double[] polygon3D, final int polygon3DOffset) {
		final int point3DCount = polygon3DGetPoint3DCount(polygon3D, polygon3DOffset);
		
		final double[] point3DA = polygon3DGetPoint3D(polygon3D, 0, point3D(), polygon3DOffset, 0);
		final double[] point3DB = polygon3DGetPoint3D(polygon3D, 1, point3D(), polygon3DOffset, 0);
		
		final double[] vector3DW = polygon3DGetSurfaceNormal(polygon3D, vector3D(), polygon3DOffset, 0);
		final double[] vector3DU = vector3DDirectionNormalized(point3DA, point3DB);
		final double[] vector3DV = vector3DNormalize(vector3DCrossProduct(vector3DW, vector3DU));
		
		final double[] point2Ds = new double[point3DCount * 2];
		
		for(int i = 0; i < point3DCount; i++) {
			final double[] point3DI = polygon3DGetPoint3D(polygon3D, i, point3D(), polygon3DOffset, 0);
			
			final double[] vector3DDirectionAI = vector3DDirection(point3DA, point3DI);
			
			final double x = vector3DDotProduct(vector3DDirectionAI, vector3DU);
			final double y = vector3DDotProduct(vector3DDirectionAI, vector3DV);
			
			point2DSet(point2Ds, x, y, i * 2);
		}
		
		return polygon2D(point2Ds);
	}
	
//	TODO: Add Javadocs!
	public static double[] polygon2DGetPoint2D(final double[] polygon2D, final int point2DIndex) {
		return polygon2DGetPoint2D(polygon2D, point2DIndex, point2D());
	}
	
//	TODO: Add Javadocs!
	public static double[] polygon2DGetPoint2D(final double[] polygon2D, final int point2DIndex, final double[] point2DResult) {
		return polygon2DGetPoint2D(polygon2D, point2DIndex, point2DResult, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] polygon2DGetPoint2D(final double[] polygon2D, final int point2DIndex, final double[] point2DResult, final int polygon2DOffset, final int point2DResultOffset) {
		return point2DSet(point2DResult, polygon2D, point2DResultOffset, polygon2DOffset + POLYGON_2_OFFSET_POINT_FIRST + point2DIndex * 2);
	}
	
//	TODO: Add Javadocs!
	public static double[] polygon2DSet(final double[] polygon2DResult, final double[] point2Ds) {
		return polygon2DSet(polygon2DResult, point2Ds, point2Ds.length / 2);
	}
	
//	TODO: Add Javadocs!
	public static double[] polygon2DSet(final double[] polygon2DResult, final double[] point2Ds, final int point2DCount) {
		return polygon2DSet(polygon2DResult, point2Ds, point2DCount, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] polygon2DSet(final double[] polygon2DResult, final double[] point2Ds, final int point2DCount, final int polygon2DResultOffset, final int point2DsOffset) {
		polygon2DResult[polygon2DResultOffset + POLYGON_2_OFFSET_ID] = POLYGON_2_ID;
		polygon2DResult[polygon2DResultOffset + POLYGON_2_OFFSET_SIZE] = 3 + point2DCount * 2;
		polygon2DResult[polygon2DResultOffset + POLYGON_2_OFFSET_POINT_COUNT] = point2DCount;
		
		for(int i = 0; i < point2DCount; i++) {
			point2DSet(polygon2DResult, point2Ds, polygon2DResultOffset + POLYGON_2_OFFSET_POINT_FIRST + i * 2, point2DsOffset + i * 2);
		}
		
		return polygon2DResult;
	}
	
//	TODO: Add Javadocs!
	public static int polygon2DGetPoint2DCount(final double[] polygon2D) {
		return polygon2DGetPoint2DCount(polygon2D, 0);
	}
	
//	TODO: Add Javadocs!
	public static int polygon2DGetPoint2DCount(final double[] polygon2D, final int polygon2DOffset) {
		return toInt(polygon2D[polygon2DOffset + POLYGON_2_OFFSET_POINT_COUNT]);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	// Polygon3D ///////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public static boolean polygon3DContainsPoint3D(final double[] polygon3D, final double[] point3D) {
		return polygon3DContainsPoint3D(polygon3D, point3D, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static boolean polygon3DContainsPoint3D(final double[] polygon3D, final double[] point3D, final int polygon3DOffset, final int point3DOffset) {
		final double[] point3DA = polygon3DGetPoint3D(polygon3D, 0, point3D(), polygon3DOffset, 0);
		final double[] point3DB = polygon3DGetPoint3D(polygon3D, 1, point3D(), polygon3DOffset, 0);
		final double[] point3DC = polygon3DGetPoint3D(polygon3D, 2, point3D(), polygon3DOffset, 0);
		final double[] point3DP = point3DSet(point3D(), point3D, 0, point3DOffset);
		
		if(!point3DCoplanar(point3DA, point3DB, point3DC, point3DP)) {
			return false;
		}
		
		final double[] polygon2D = polygon2DFromPolygon3D(polygon3D, polygon3DOffset);
		
		final double[] vector3DW = polygon3DGetSurfaceNormal(polygon3D, vector3D(), polygon3DOffset, 0);
		final double[] vector3DU = vector3DDirectionNormalized(point3DA, point3DB);
		final double[] vector3DV = vector3DCrossProduct(vector3DW, vector3DU);
		
		final double[] vector3DDirectionAP = vector3DDirection(point3DA, point3DP);
		
		final double[] point2DP = point2D(vector3DDotProduct(vector3DDirectionAP, vector3DU), vector3DDotProduct(vector3DDirectionAP, vector3DV));
		
		final boolean contains = polygon2DContainsPoint2D(polygon2D, point2DP);
		
		return contains;
	}
	
//	TODO: Add Javadocs!
	public static double polygon3DIntersection(final double[] ray3D, final double[] polygon3D) {
		return polygon3DIntersection(ray3D, polygon3D, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double polygon3DIntersection(final double[] ray3D, final double[] polygon3D, final int ray3DOffset, final int polygon3DOffset) {
		final double[] point3DOrigin = ray3DGetOrigin(ray3D, point3D(), ray3DOffset, 0);
		
		final double[] vector3DDirection = ray3DGetDirection(ray3D, vector3D(), ray3DOffset, 0);
		
		final double tMinimum = ray3DGetTMinimum(ray3D, ray3DOffset);
		final double tMaximum = ray3DGetTMaximum(ray3D, ray3DOffset);
		
		final double[] point3DA = polygon3DGetPoint3D(polygon3D, 0, point3D(), polygon3DOffset, 0);
		final double[] point3DB = polygon3DGetPoint3D(polygon3D, 1, point3D(), polygon3DOffset, 0);
		
		final double[] vector3DSurfaceNormal = polygon3DGetSurfaceNormal(polygon3D, vector3D(), polygon3DOffset, 0);
		
		final double surfaceNormalDotDirection = vector3DDotProduct(vector3DSurfaceNormal, vector3DDirection);
		
		if(isZero(surfaceNormalDotDirection)) {
			return NaN;
		}
		
		final double[] vector3DOriginToA = vector3DDirection(point3DOrigin, point3DA);
		
		final double t = vector3DDotProduct(vector3DOriginToA, vector3DSurfaceNormal) / surfaceNormalDotDirection;
		
		if(t <= tMinimum || t >= tMaximum) {
			return NaN;
		}
		
		final double[] polygon2D = polygon2DFromPolygon3D(polygon3D, polygon3DOffset);
		
		final double[] point3DP = point3DAdd(point3DOrigin, vector3DDirection, t);
		
		final double[] vector3DW = vector3DSurfaceNormal;
		final double[] vector3DU = vector3DDirectionNormalized(point3DA, point3DB);
		final double[] vector3DV = vector3DCrossProduct(vector3DW, vector3DU);
		
		final double[] vector3DDirectionAP = vector3DDirection(point3DA, point3DP);
		
		final double[] point2DP = point2D(vector3DDotProduct(vector3DDirectionAP, vector3DU), vector3DDotProduct(vector3DDirectionAP, vector3DV));
		
		final boolean contains = polygon2DContainsPoint2D(polygon2D, point2DP);
		
		if(contains) {
			return t;
		}
		
		return NaN;
	}
	
//	TODO: Add Javadocs!
	public static double polygon3DSurfaceArea(final double[] polygon3D) {
		return polygon3DSurfaceArea(polygon3D, 0);
	}
	
//	TODO: Add Javadocs!
	public static double polygon3DSurfaceArea(final double[] polygon3D, final int polygon3DOffset) {
		final double[] vector3DSurfaceArea = vector3D();
		
		final int point3DCount = polygon3DGetPoint3DCount(polygon3D, polygon3DOffset);
		
		for(int i = 0, j = point3DCount - 1; i < point3DCount; j = i, i++) {
			final double[] point3DI = polygon3DGetPoint3D(polygon3D, i, point3D(), polygon3DOffset, 0);
			final double[] point3DJ = polygon3DGetPoint3D(polygon3D, j, point3D(), polygon3DOffset, 0);
			
			final double[] vector3DI = vector3DFromPoint3D(point3DI);
			final double[] vector3DJ = vector3DFromPoint3D(point3DJ);
			
			vector3DAdd(vector3DSurfaceArea, vector3DCrossProduct(vector3DI, vector3DJ), vector3DSurfaceArea);
		}
		
		final double[] vector3DSurfaceNormal = polygon3DGetSurfaceNormal(polygon3D, vector3D(), polygon3DOffset, 0);
		
		final double surfaceArea = 0.5D * abs(vector3DDotProduct(vector3DSurfaceNormal, vector3DSurfaceArea));
		
		return surfaceArea;
	}
	
//	TODO: Add Javadocs!
	public static double[] polygon3D() {
		return polygon3D(merge(point3D(-1.0D, 1.0D, 0.0D), point3D(0.0D, 1.5D, 0.0D), point3D(1.0D, 1.0D, 0.0D), point3D(1.0D, -1.0D, 0.0D), point3D(-1.0D, -1.0D, 0.0D)));
	}
	
//	TODO: Add Javadocs!
	public static double[] polygon3D(final double[] point3Ds) {
		return polygon3D(point3Ds, point3Ds.length / 3);
	}
	
//	TODO: Add Javadocs!
	public static double[] polygon3D(final double[] point3Ds, final int point3DCount) {
		return polygon3D(point3Ds, point3DCount, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] polygon3D(final double[] point3Ds, final int point3DCount, final int point3DsOffset) {
		return polygon3DSet(new double[3 + point3DCount * 3], point3Ds, point3DCount, 0, point3DsOffset);
	}
	
//	TODO: Add Javadocs!
	public static double[] polygon3DComputeOrthonormalBasis(final double[] ray3D, final double[] polygon3D, final double t) {
		return polygon3DComputeOrthonormalBasis(ray3D, polygon3D, t, 0, 0);
	}
	
//	TODO: Add Javadocs!
	@SuppressWarnings("unused")
	public static double[] polygon3DComputeOrthonormalBasis(final double[] ray3D, final double[] polygon3D, final double t, final int ray3DOffset, final int polygon3DOffset) {
		final double[] vector3DW = polygon3DGetSurfaceNormal(polygon3D, vector3D(), polygon3DOffset, 0);
		
		return orthonormalBasis33DFromW(vector3DW);
	}
	
//	TODO: Add Javadocs!
	public static double[] polygon3DComputeSurfaceNormal(final double[] ray3D, final double[] polygon3D, final double t) {
		return polygon3DComputeSurfaceNormal(ray3D, polygon3D, t, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] polygon3DComputeSurfaceNormal(final double[] ray3D, final double[] polygon3D, final double t, final int ray3DOffset, final int polygon3DOffset) {
		return orthonormalBasis33DGetW(polygon3DComputeOrthonormalBasis(ray3D, polygon3D, t, ray3DOffset, polygon3DOffset));
	}
	
//	TODO: Add Javadocs!
	public static double[] polygon3DComputeTextureCoordinates(final double[] ray3D, final double[] polygon3D, final double t) {
		return polygon3DComputeTextureCoordinates(ray3D, polygon3D, t, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] polygon3DComputeTextureCoordinates(final double[] ray3D, final double[] polygon3D, final double t, final int ray3DOffset, final int polygon3DOffset) {
		final double[] point3DOrigin = ray3DGetOrigin(ray3D, point3D(), ray3DOffset, 0);
		
		final double[] vector3DDirection = ray3DGetDirection(ray3D, vector3D(), ray3DOffset, 0);
		
		final int n = polygon3DGetPoint3DCount(polygon3D, polygon3DOffset) - 1;
		
		final double[] point3DA = polygon3DGetPoint3D(polygon3D, 0, point3D(), polygon3DOffset, 0);
		final double[] point3DB = polygon3DGetPoint3D(polygon3D, 1, point3D(), polygon3DOffset, 0);
		final double[] point3DN = polygon3DGetPoint3D(polygon3D, n, point3D(), polygon3DOffset, 0);
		
		final double[] vector3DSurfaceNormal = polygon3DGetSurfaceNormal(polygon3D, vector3D(), polygon3DOffset, 0);
		
		final double[] point3D = point3DAdd(point3DOrigin, vector3DDirection, t);
		
		final boolean isX = abs(vector3DGetX(vector3DSurfaceNormal)) > abs(vector3DGetY(vector3DSurfaceNormal)) && abs(vector3DGetX(vector3DSurfaceNormal)) > abs(vector3DGetZ(vector3DSurfaceNormal));
		final boolean isY = abs(vector3DGetY(vector3DSurfaceNormal)) > abs(vector3DGetZ(vector3DSurfaceNormal));
		
		final double[] vector2DA = isX ? vector2DDirectionYZ(point3DA) : isY ? vector2DDirectionZX(point3DA) : vector2DDirectionXY(point3DA);
		final double[] vector2DB = isX ? vector2DDirectionYZ(point3DN) : isY ? vector2DDirectionZX(point3DN) : vector2DDirectionXY(point3DN);
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
	public static double[] polygon3DGetPoint3D(final double[] polygon3D, final int point3DIndex) {
		return polygon3DGetPoint3D(polygon3D, point3DIndex, point3D());
	}
	
//	TODO: Add Javadocs!
	public static double[] polygon3DGetPoint3D(final double[] polygon3D, final int point3DIndex, final double[] point3DResult) {
		return polygon3DGetPoint3D(polygon3D, point3DIndex, point3DResult, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] polygon3DGetPoint3D(final double[] polygon3D, final int point3DIndex, final double[] point3DResult, final int polygon3DOffset, final int point3DResultOffset) {
		return point3DSet(point3DResult, polygon3D, point3DResultOffset, polygon3DOffset + POLYGON_3_OFFSET_POINT_FIRST + point3DIndex * 3);
	}
	
//	TODO: Add Javadocs!
	public static double[] polygon3DGetSurfaceNormal(final double[] polygon3D) {
		return polygon3DGetSurfaceNormal(polygon3D, vector3D());
	}
	
//	TODO: Add Javadocs!
	public static double[] polygon3DGetSurfaceNormal(final double[] polygon3D, final double[] vector3DSurfaceNormalResult) {
		return polygon3DGetSurfaceNormal(polygon3D, vector3DSurfaceNormalResult, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] polygon3DGetSurfaceNormal(final double[] polygon3D, final double[] vector3DSurfaceNormalResult, final int polygon3DOffset, final int vector3DSurfaceNormalResultOffset) {
		final double[] point3DA = polygon3DGetPoint3D(polygon3D, 0, point3D(), polygon3DOffset, 0);
		final double[] point3DB = polygon3DGetPoint3D(polygon3D, 1, point3D(), polygon3DOffset, 0);
		final double[] point3DC = polygon3DGetPoint3D(polygon3D, 2, point3D(), polygon3DOffset, 0);
		
		final double[] vector3DSurfaceNormal = vector3DNormalNormalized(point3DA, point3DB, point3DC, vector3DSurfaceNormalResult, 0, 0, 0, vector3DSurfaceNormalResultOffset);
		
		return vector3DSurfaceNormal;
	}
	
//	TODO: Add Javadocs!
	public static double[] polygon3DSet(final double[] polygon3DResult, final double[] point3Ds) {
		return polygon3DSet(polygon3DResult, point3Ds, point3Ds.length / 3);
	}
	
//	TODO: Add Javadocs!
	public static double[] polygon3DSet(final double[] polygon3DResult, final double[] point3Ds, final int point3DCount) {
		return polygon3DSet(polygon3DResult, point3Ds, point3DCount, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] polygon3DSet(final double[] polygon3DResult, final double[] point3Ds, final int point3DCount, final int polygon3DResultOffset, final int point3DsOffset) {
		polygon3DResult[polygon3DResultOffset + POLYGON_3_OFFSET_ID] = POLYGON_3_ID;
		polygon3DResult[polygon3DResultOffset + POLYGON_3_OFFSET_SIZE] = 3 + point3DCount * 3;
		polygon3DResult[polygon3DResultOffset + POLYGON_3_OFFSET_POINT_COUNT] = point3DCount;
		
		for(int i = 0; i < point3DCount; i++) {
			point3DSet(polygon3DResult, point3Ds, polygon3DResultOffset + POLYGON_3_OFFSET_POINT_FIRST + i * 3, point3DsOffset + i * 3);
		}
		
		return polygon3DResult;
	}
	
//	TODO: Add Javadocs!
	public static int polygon3DGetPoint3DCount(final double[] polygon3D) {
		return polygon3DGetPoint3DCount(polygon3D, 0);
	}
	
//	TODO: Add Javadocs!
	public static int polygon3DGetPoint3DCount(final double[] polygon3D, final int polygon3DOffset) {
		return toInt(polygon3D[polygon3DOffset + POLYGON_3_OFFSET_POINT_COUNT]);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	// Rectangle2D /////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public static boolean rectangle2DContainsPoint2D(final double[] rectangle2D, final double[] point2D) {
		return rectangle2DContainsPoint2D(rectangle2D, point2D, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static boolean rectangle2DContainsPoint2D(final double[] rectangle2D, final double[] point2D, final int rectangle2DOffset, final int point2DOffset) {
		final double[] point2DA = rectangle2DGetA(rectangle2D, point2D(), rectangle2DOffset, 0);
		final double[] point2DB = rectangle2DGetB(rectangle2D, point2D(), rectangle2DOffset, 0);
		final double[] point2DC = rectangle2DGetC(rectangle2D, point2D(), rectangle2DOffset, 0);
		final double[] point2DD = rectangle2DGetD(rectangle2D, point2D(), rectangle2DOffset, 0);
		final double[] point2DP = point2DSet(point2D(), point2D, 0, point2DOffset);
		
		/*
		 * The following code is faster. But it has too many restrictions.
		 * 
		 * 1. The rectangle has to be axis-aligned and not rotated.
		 * 2. The rectangle has to be rectangular with two sets of two equal sides.
		 * 3. The point denoted by A has to be the minimum point in both directions.
		 * 4. The point denoted by C has to be the maximum point in both directions.
		 * 
		 * Performing checks to verify that these restrictions are met is probably worse performance-wize than using the Polygon2D in all cases.
		 */
		
//		if(rectangle2DIsAxisAligned(rectangle2D, rectangle2DOffset) && rectangle2DIsRectangular(rectangle2D, rectangle2DOffset)) {
//			final boolean containsX = point2DGetX(point2DP) >= point2DGetX(point2DA) && point2DGetX(point2DP) <= point2DGetX(point2DC);
//			final boolean containsY = point2DGetY(point2DP) >= point2DGetY(point2DA) && point2DGetY(point2DP) <= point2DGetY(point2DC);
//			final boolean contains = containsX && containsY;
			
//			return contains;
//		}
		
		final double[] polygon2D = polygon2D(merge(point2DA, point2DB, point2DC, point2DD));
		
		final boolean contains = polygon2DContainsPoint2D(polygon2D, point2DP);
		
		return contains;
	}
	
//	TODO: Add Javadocs!
	public static boolean rectangle2DIsAxisAligned(final double[] rectangle2D) {
		return rectangle2DIsAxisAligned(rectangle2D, 0);
	}
	
//	TODO: Add Javadocs!
	public static boolean rectangle2DIsAxisAligned(final double[] rectangle2D, final int rectangle2DOffset) {
		final double[] point2DA = rectangle2DGetA(rectangle2D, point2D(), rectangle2DOffset, 0);
		final double[] point2DB = rectangle2DGetB(rectangle2D, point2D(), rectangle2DOffset, 0);
		final double[] point2DC = rectangle2DGetC(rectangle2D, point2D(), rectangle2DOffset, 0);
		final double[] point2DD = rectangle2DGetD(rectangle2D, point2D(), rectangle2DOffset, 0);
		
		final boolean isAxisAlignedAB = equal(point2DGetY(point2DA), point2DGetY(point2DB));
		final boolean isAxisAlignedBC = equal(point2DGetX(point2DB), point2DGetX(point2DC));
		final boolean isAxisAlignedCD = equal(point2DGetY(point2DC), point2DGetY(point2DD));
		final boolean isAxisAlignedDA = equal(point2DGetX(point2DD), point2DGetX(point2DA));
		final boolean isAxisAligned = isAxisAlignedAB && isAxisAlignedBC && isAxisAlignedCD && isAxisAlignedDA;
		
		return isAxisAligned;
	}
	
//	TODO: Add Javadocs!
	public static boolean rectangle2DIsRectangular(final double[] rectangle2D) {
		return rectangle2DIsRectangular(rectangle2D, 0);
	}
	
//	TODO: Add Javadocs!
	public static boolean rectangle2DIsRectangular(final double[] rectangle2D, final int rectangle2DOffset) {
		final double[] point2DA = rectangle2DGetA(rectangle2D, point2D(), rectangle2DOffset, 0);
		final double[] point2DB = rectangle2DGetB(rectangle2D, point2D(), rectangle2DOffset, 0);
		final double[] point2DC = rectangle2DGetC(rectangle2D, point2D(), rectangle2DOffset, 0);
		final double[] point2DD = rectangle2DGetD(rectangle2D, point2D(), rectangle2DOffset, 0);
		
		final double distanceAB = abs(point2DDistance(point2DA, point2DB));
		final double distanceBC = abs(point2DDistance(point2DB, point2DC));
		final double distanceCD = abs(point2DDistance(point2DC, point2DD));
		final double distanceDA = abs(point2DDistance(point2DD, point2DA));
		
		final boolean isRectangular = equal(distanceAB, distanceCD) && equal(distanceBC, distanceDA);
		
		return isRectangular;
	}
	
//	TODO: Add Javadocs!
	public static double[] rectangle2D() {
		return rectangle2D(point2D(-1.0D, -1.0D), point2D(1.0D, -1.0D), point2D(1.0D, 1.0D), point2D(-1.0D, 1.0D));
	}
	
//	TODO: Add Javadocs!
	public static double[] rectangle2D(final double[] point2DA, final double[] point2DB, final double[] point2DC, final double[] point2DD) {
		return rectangle2D(point2DA, point2DB, point2DC, point2DD, 0, 0, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] rectangle2D(final double[] point2DA, final double[] point2DB, final double[] point2DC, final double[] point2DD, final int point2DAOffset, final int point2DBOffset, final int point2DCOffset, final int point2DDOffset) {
		return rectangle2DSet(new double[RECTANGLE_2_SIZE], point2DA, point2DB, point2DC, point2DD, 0, point2DAOffset, point2DBOffset, point2DCOffset, point2DDOffset);
	}
	
//	TODO: Add Javadocs!
	public static double[] rectangle2DGetA(final double[] rectangle2D) {
		return rectangle2DGetA(rectangle2D, point2D());
	}
	
//	TODO: Add Javadocs!
	public static double[] rectangle2DGetA(final double[] rectangle2D, final double[] point2DAResult) {
		return rectangle2DGetA(rectangle2D, point2DAResult, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] rectangle2DGetA(final double[] rectangle2D, final double[] point2DAResult, final int rectangle2DOffset, final int point2DAResultOffset) {
		return point2DSet(point2DAResult, rectangle2D, point2DAResultOffset, rectangle2DOffset + RECTANGLE_2_OFFSET_A);
	}
	
//	TODO: Add Javadocs!
	public static double[] rectangle2DGetB(final double[] rectangle2D) {
		return rectangle2DGetB(rectangle2D, point2D());
	}
	
//	TODO: Add Javadocs!
	public static double[] rectangle2DGetB(final double[] rectangle2D, final double[] point2DBResult) {
		return rectangle2DGetB(rectangle2D, point2DBResult, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] rectangle2DGetB(final double[] rectangle2D, final double[] point2DBResult, final int rectangle2DOffset, final int point2DBResultOffset) {
		return point2DSet(point2DBResult, rectangle2D, point2DBResultOffset, rectangle2DOffset + RECTANGLE_2_OFFSET_B);
	}
	
//	TODO: Add Javadocs!
	public static double[] rectangle2DGetC(final double[] rectangle2D) {
		return rectangle2DGetC(rectangle2D, point2D());
	}
	
//	TODO: Add Javadocs!
	public static double[] rectangle2DGetC(final double[] rectangle2D, final double[] point2DCResult) {
		return rectangle2DGetC(rectangle2D, point2DCResult, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] rectangle2DGetC(final double[] rectangle2D, final double[] point2DCResult, final int rectangle2DOffset, final int point2DCResultOffset) {
		return point2DSet(point2DCResult, rectangle2D, point2DCResultOffset, rectangle2DOffset + RECTANGLE_2_OFFSET_C);
	}
	
//	TODO: Add Javadocs!
	public static double[] rectangle2DGetD(final double[] rectangle2D) {
		return rectangle2DGetD(rectangle2D, point2D());
	}
	
//	TODO: Add Javadocs!
	public static double[] rectangle2DGetD(final double[] rectangle2D, final double[] point2DDResult) {
		return rectangle2DGetD(rectangle2D, point2DDResult, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] rectangle2DGetD(final double[] rectangle2D, final double[] point2DDResult, final int rectangle2DOffset, final int point2DDResultOffset) {
		return point2DSet(point2DDResult, rectangle2D, point2DDResultOffset, rectangle2DOffset + RECTANGLE_2_OFFSET_D);
	}
	
//	TODO: Add Javadocs!
	public static double[] rectangle2DSet(final double[] rectangle2DResult, final double[] point2DA, final double[] point2DB, final double[] point2DC, final double[] point2DD) {
		return rectangle2DSet(rectangle2DResult, point2DA, point2DB, point2DC, point2DD, 0, 0, 0, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] rectangle2DSet(final double[] rectangle2DResult, final double[] point2DA, final double[] point2DB, final double[] point2DC, final double[] point2DD, final int rectangle2DResultOffset, final int point2DAOffset, final int point2DBOffset, final int point2DCOffset, final int point2DDOffset) {
		rectangle2DResult[rectangle2DResultOffset + RECTANGLE_2_OFFSET_ID] = RECTANGLE_2_ID;
		rectangle2DResult[rectangle2DResultOffset + RECTANGLE_2_OFFSET_SIZE] = RECTANGLE_2_SIZE;
		
		point2DSet(rectangle2DResult, point2DA, rectangle2DResultOffset + RECTANGLE_2_OFFSET_A, point2DAOffset);
		point2DSet(rectangle2DResult, point2DB, rectangle2DResultOffset + RECTANGLE_2_OFFSET_B, point2DBOffset);
		point2DSet(rectangle2DResult, point2DC, rectangle2DResultOffset + RECTANGLE_2_OFFSET_C, point2DCOffset);
		point2DSet(rectangle2DResult, point2DD, rectangle2DResultOffset + RECTANGLE_2_OFFSET_D, point2DDOffset);
		
		return rectangle2DResult;
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
		
		final double[] vector3DSurfaceNormal = vector3DNormalNormalized(point3DA, point3DB, point3DC);
		
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
		return rectangle3DSet(new double[RECTANGLE_3_SIZE], point3DA, point3DB, point3DC, point3DD, 0, point3DAOffset, point3DBOffset, point3DCOffset, point3DDOffset);
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
		
		final double[] vector3DW = vector3DNormalNormalized(point3DA, point3DB, point3DC);
		
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
		return point3DSet(point3DAResult, rectangle3D, point3DAResultOffset, rectangle3DOffset + RECTANGLE_3_OFFSET_A);
	}
	
//	TODO: Add Javadocs!
	public static double[] rectangle3DGetB(final double[] rectangle3D) {
		return rectangle3DGetB(rectangle3D, point3D());
	}
	
//	TODO: Add Javadocs!
	public static double[] rectangle3DGetB(final double[] rectangle3D, final double[] point3DBResult) {
		return rectangle3DGetB(rectangle3D, point3DBResult, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] rectangle3DGetB(final double[] rectangle3D, final double[] point3DBResult, final int rectangle3DOffset, final int point3DBResultOffset) {
		return point3DSet(point3DBResult, rectangle3D, point3DBResultOffset, rectangle3DOffset + RECTANGLE_3_OFFSET_B);
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
		return point3DSet(point3DCResult, rectangle3D, point3DCResultOffset, rectangle3DOffset + RECTANGLE_3_OFFSET_C);
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
		return point3DSet(point3DDResult, rectangle3D, point3DDResultOffset, rectangle3DOffset + RECTANGLE_3_OFFSET_D);
	}
	
//	TODO: Add Javadocs!
	public static double[] rectangle3DSet(final double[] rectangle3DResult, final double[] point3DA, final double[] point3DB, final double[] point3DC, final double[] point3DD) {
		return rectangle3DSet(rectangle3DResult, point3DA, point3DB, point3DC, point3DD, 0, 0, 0, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] rectangle3DSet(final double[] rectangle3DResult, final double[] point3DA, final double[] point3DB, final double[] point3DC, final double[] point3DD, final int rectangle3DResultOffset, final int point3DAOffset, final int point3DBOffset, final int point3DCOffset, final int point3DDOffset) {
		rectangle3DResult[rectangle3DResultOffset + RECTANGLE_3_OFFSET_ID] = RECTANGLE_3_ID;
		rectangle3DResult[rectangle3DResultOffset + RECTANGLE_3_OFFSET_SIZE] = RECTANGLE_3_SIZE;
		
		point3DSet(rectangle3DResult, point3DA, rectangle3DResultOffset + RECTANGLE_3_OFFSET_A, point3DAOffset);
		point3DSet(rectangle3DResult, point3DB, rectangle3DResultOffset + RECTANGLE_3_OFFSET_B, point3DBOffset);
		point3DSet(rectangle3DResult, point3DC, rectangle3DResultOffset + RECTANGLE_3_OFFSET_C, point3DCOffset);
		point3DSet(rectangle3DResult, point3DD, rectangle3DResultOffset + RECTANGLE_3_OFFSET_D, point3DDOffset);
		
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
		return rectangularCuboid3DSet(new double[RECTANGULAR_CUBOID_3_SIZE], point3DA, point3DB, 0, point3DAOffset, point3DBOffset);
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
	public static double[] rectangularCuboid3DGetMaximum(final double[] rectangularCuboid3D) {
		return rectangularCuboid3DGetMaximum(rectangularCuboid3D, point3D());
	}
	
//	TODO: Add Javadocs!
	public static double[] rectangularCuboid3DGetMaximum(final double[] rectangularCuboid3D, final double[] point3DMaximumResult) {
		return rectangularCuboid3DGetMaximum(rectangularCuboid3D, point3DMaximumResult, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] rectangularCuboid3DGetMaximum(final double[] rectangularCuboid3D, final double[] point3DMaximumResult, final int rectangularCuboid3DOffset, final int point3DMaximumResultOffset) {
		return point3DSet(point3DMaximumResult, rectangularCuboid3D, point3DMaximumResultOffset, rectangularCuboid3DOffset + RECTANGULAR_CUBOID_3_OFFSET_MAXIMUM);
	}
	
//	TODO: Add Javadocs!
	public static double[] rectangularCuboid3DGetMinimum(final double[] rectangularCuboid3D) {
		return rectangularCuboid3DGetMinimum(rectangularCuboid3D, point3D());
	}
	
//	TODO: Add Javadocs!
	public static double[] rectangularCuboid3DGetMinimum(final double[] rectangularCuboid3D, final double[] point3DMinimumResult) {
		return rectangularCuboid3DGetMinimum(rectangularCuboid3D, point3DMinimumResult, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] rectangularCuboid3DGetMinimum(final double[] rectangularCuboid3D, final double[] point3DMinimumResult, final int rectangularCuboid3DOffset, final int point3DMinimumResultOffset) {
		return point3DSet(point3DMinimumResult, rectangularCuboid3D, point3DMinimumResultOffset, rectangularCuboid3DOffset + RECTANGULAR_CUBOID_3_OFFSET_MINIMUM);
	}
	
//	TODO: Add Javadocs!
	public static double[] rectangularCuboid3DSet(final double[] rectangularCuboid3DResult, final double[] point3DA, final double[] point3DB) {
		return rectangularCuboid3DSet(rectangularCuboid3DResult, point3DA, point3DB, 0, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] rectangularCuboid3DSet(final double[] rectangularCuboid3DResult, final double[] point3DA, final double[] point3DB, final int rectangularCuboid3DResultOffset, final int point3DAOffset, final int point3DBOffset) {
		rectangularCuboid3DResult[rectangularCuboid3DResultOffset + RECTANGULAR_CUBOID_3_OFFSET_ID] = RECTANGULAR_CUBOID_3_ID;
		rectangularCuboid3DResult[rectangularCuboid3DResultOffset + RECTANGULAR_CUBOID_3_OFFSET_SIZE] = RECTANGULAR_CUBOID_3_SIZE;
		
		point3DSet(rectangularCuboid3DResult, point3DMaximum(point3DA, point3DB, point3D(), point3DAOffset, point3DBOffset, 0), rectangularCuboid3DResultOffset + RECTANGULAR_CUBOID_3_OFFSET_MAXIMUM, 0);
		point3DSet(rectangularCuboid3DResult, point3DMinimum(point3DA, point3DB, point3D(), point3DAOffset, point3DBOffset, 0), rectangularCuboid3DResultOffset + RECTANGULAR_CUBOID_3_OFFSET_MINIMUM, 0);
		
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
			case CONE_3_ID:
				return cone3DIntersection(ray3D, shape3D, ray3DOffset, shape3DOffset);
			case CYLINDER_3_ID:
				return cylinder3DIntersection(ray3D, shape3D, ray3DOffset, shape3DOffset);
			case DISK_3_ID:
				return disk3DIntersection(ray3D, shape3D, ray3DOffset, shape3DOffset);
			case HYPERBOLOID_3_ID:
				return hyperboloid3DIntersection(ray3D, shape3D, ray3DOffset, shape3DOffset);
			case PARABOLOID_3_ID:
				return paraboloid3DIntersection(ray3D, shape3D, ray3DOffset, shape3DOffset);
			case PLANE_3_ID:
				return plane3DIntersection(ray3D, shape3D, ray3DOffset, shape3DOffset);
			case POLYGON_3_ID:
				return polygon3DIntersection(ray3D, shape3D, ray3DOffset, shape3DOffset);
			case RECTANGLE_3_ID:
				return rectangle3DIntersection(ray3D, shape3D, ray3DOffset, shape3DOffset);
			case RECTANGULAR_CUBOID_3_ID:
				return rectangularCuboid3DIntersection(ray3D, shape3D, ray3DOffset, shape3DOffset);
			case SPHERE_3_ID:
				return sphere3DIntersection(ray3D, shape3D, ray3DOffset, shape3DOffset);
			case TORUS_3_ID:
				return torus3DIntersection(ray3D, shape3D, ray3DOffset, shape3DOffset);
			case TRIANGLE_3_ID:
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
			case CONE_3_ID:
				return cone3DComputeOrthonormalBasis(ray3D, shape3D, t, ray3DOffset, shape3DOffset);
			case CYLINDER_3_ID:
				return cylinder3DComputeOrthonormalBasis(ray3D, shape3D, t, ray3DOffset, shape3DOffset);
			case DISK_3_ID:
				return disk3DComputeOrthonormalBasis(ray3D, shape3D, t, ray3DOffset, shape3DOffset);
			case HYPERBOLOID_3_ID:
				return hyperboloid3DComputeOrthonormalBasis(ray3D, shape3D, t, ray3DOffset, shape3DOffset);
			case PARABOLOID_3_ID:
				return paraboloid3DComputeOrthonormalBasis(ray3D, shape3D, t, ray3DOffset, shape3DOffset);
			case PLANE_3_ID:
				return plane3DComputeOrthonormalBasis(ray3D, shape3D, t, ray3DOffset, shape3DOffset);
			case POLYGON_3_ID:
				return polygon3DComputeOrthonormalBasis(ray3D, shape3D, t, ray3DOffset, shape3DOffset);
			case RECTANGLE_3_ID:
				return rectangle3DComputeOrthonormalBasis(ray3D, shape3D, t, ray3DOffset, shape3DOffset);
			case RECTANGULAR_CUBOID_3_ID:
				return rectangularCuboid3DComputeOrthonormalBasis(ray3D, shape3D, t, ray3DOffset, shape3DOffset);
			case SPHERE_3_ID:
				return sphere3DComputeOrthonormalBasis(ray3D, shape3D, t, ray3DOffset, shape3DOffset);
			case TORUS_3_ID:
				return torus3DComputeOrthonormalBasis(ray3D, shape3D, t, ray3DOffset, shape3DOffset);
			case TRIANGLE_3_ID:
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
			case CONE_3_ID:
				return cone3DComputeTextureCoordinates(ray3D, shape3D, t, ray3DOffset, shape3DOffset);
			case CYLINDER_3_ID:
				return cylinder3DComputeTextureCoordinates(ray3D, shape3D, t, ray3DOffset, shape3DOffset);
			case DISK_3_ID:
				return disk3DComputeTextureCoordinates(ray3D, shape3D, t, ray3DOffset, shape3DOffset);
			case HYPERBOLOID_3_ID:
				return hyperboloid3DComputeTextureCoordinates(ray3D, shape3D, t, ray3DOffset, shape3DOffset);
			case PARABOLOID_3_ID:
				return paraboloid3DComputeTextureCoordinates(ray3D, shape3D, t, ray3DOffset, shape3DOffset);
			case PLANE_3_ID:
				return plane3DComputeTextureCoordinates(ray3D, shape3D, t, ray3DOffset, shape3DOffset);
			case POLYGON_3_ID:
				return polygon3DComputeTextureCoordinates(ray3D, shape3D, t, ray3DOffset, shape3DOffset);
			case RECTANGLE_3_ID:
				return rectangle3DComputeTextureCoordinates(ray3D, shape3D, t, ray3DOffset, shape3DOffset);
			case RECTANGULAR_CUBOID_3_ID:
				return rectangularCuboid3DComputeTextureCoordinates(ray3D, shape3D, t, ray3DOffset, shape3DOffset);
			case SPHERE_3_ID:
				return sphere3DComputeTextureCoordinates(ray3D, shape3D, t, ray3DOffset, shape3DOffset);
			case TORUS_3_ID:
				return torus3DComputeTextureCoordinates(ray3D, shape3D, t, ray3DOffset, shape3DOffset);
			case TRIANGLE_3_ID:
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
		return toInt(shape3D[shape3DOffset + SHAPE_3_OFFSET_ID]);
	}
	
//	TODO: Add Javadocs!
	public static int shape3DGetSize(final double[] shape3D) {
		return shape3DGetSize(shape3D, 0);
	}
	
//	TODO: Add Javadocs!
	public static int shape3DGetSize(final double[] shape3D, final int shape3DOffset) {
		return toInt(shape3D[shape3DOffset + SHAPE_3_OFFSET_SIZE]);
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
		return sphere3D[sphere3DOffset + SPHERE_3_OFFSET_RADIUS];
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
		return sphere3DSet(new double[SPHERE_3_SIZE], point3DCenter, radius, 0, point3DCenterOffset);
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
		return point3DSet(point3DCenterResult, sphere3D, point3DCenterResultOffset, sphere3DOffset + SPHERE_3_OFFSET_CENTER);
	}
	
//	TODO: Add Javadocs!
	public static double[] sphere3DSet(final double[] sphere3DResult, final double[] point3DCenter, final double radius) {
		return sphere3DSet(sphere3DResult, point3DCenter, radius, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] sphere3DSet(final double[] sphere3DResult, final double[] point3DCenter, final double radius, final int sphere3DResultOffset, final int point3DCenterOffset) {
		sphere3DResult[sphere3DResultOffset + SPHERE_3_OFFSET_ID] = SPHERE_3_ID;
		sphere3DResult[sphere3DResultOffset + SPHERE_3_OFFSET_SIZE] = SPHERE_3_SIZE;
		sphere3DResult[sphere3DResultOffset + SPHERE_3_OFFSET_RADIUS] = radius;
		
		point3DSet(sphere3DResult, point3DCenter, sphere3DResultOffset + SPHERE_3_OFFSET_CENTER, point3DCenterOffset);
		
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
		return torus3D[torus3DOffset + TORUS_3_OFFSET_RADIUS_INNER];
	}
	
//	TODO: Add Javadocs!
	public static double torus3DGetRadiusOuter(final double[] torus3D) {
		return torus3DGetRadiusOuter(torus3D, 0);
	}
	
//	TODO: Add Javadocs!
	public static double torus3DGetRadiusOuter(final double[] torus3D, final int torus3DOffset) {
		return torus3D[torus3DOffset + TORUS_3_OFFSET_RADIUS_OUTER];
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
		return torus3DSet(new double[TORUS_3_SIZE], radiusInner, radiusOuter);
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
		torus3DResult[torus3DResultOffset + TORUS_3_OFFSET_ID] = TORUS_3_ID;
		torus3DResult[torus3DResultOffset + TORUS_3_OFFSET_SIZE] = TORUS_3_SIZE;
		torus3DResult[torus3DResultOffset + TORUS_3_OFFSET_RADIUS_INNER] = radiusInner;
		torus3DResult[torus3DResultOffset + TORUS_3_OFFSET_RADIUS_OUTER] = radiusOuter;
		
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
		return triangle3D(point3DPositionA, point3DPositionB, point3DPositionC, vector3DNormalNormalized(point3DPositionA, point3DPositionB, point3DPositionC));
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
		return triangle3DSet(new double[TRIANGLE_3_SIZE], point3DPositionA, point3DPositionB, point3DPositionC, vector3DSurfaceNormalA, vector3DSurfaceNormalB, vector3DSurfaceNormalC, point2DTextureCoordinatesA, point2DTextureCoordinatesB, point2DTextureCoordinatesC, 0, point3DPositionAOffset, point3DPositionBOffset, point3DPositionCOffset, vector3DSurfaceNormalAOffset, vector3DSurfaceNormalBOffset, vector3DSurfaceNormalCOffset, point2DTextureCoordinatesAOffset, point2DTextureCoordinatesBOffset, point2DTextureCoordinatesCOffset);
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
		return point3DSet(point3DPositionAResult, triangle3D, point3DPositionAResultOffset, triangle3DOffset + TRIANGLE_3_OFFSET_POSITION_A);
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
		return point3DSet(point3DPositionBResult, triangle3D, point3DPositionBResultOffset, triangle3DOffset + TRIANGLE_3_OFFSET_POSITION_B);
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
		return point3DSet(point3DPositionCResult, triangle3D, point3DPositionCResultOffset, triangle3DOffset + TRIANGLE_3_OFFSET_POSITION_C);
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
		return vector3DSet(vector3DSurfaceNormalAResult, triangle3D, vector3DSurfaceNormalAResultOffset, triangle3DOffset + TRIANGLE_3_OFFSET_SURFACE_NORMAL_A);
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
		return vector3DSet(vector3DSurfaceNormalBResult, triangle3D, vector3DSurfaceNormalBResultOffset, triangle3DOffset + TRIANGLE_3_OFFSET_SURFACE_NORMAL_B);
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
		return vector3DSet(vector3DSurfaceNormalCResult, triangle3D, vector3DSurfaceNormalCResultOffset, triangle3DOffset + TRIANGLE_3_OFFSET_SURFACE_NORMAL_C);
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
		return point2DSet(point2DTextureCoordinatesAResult, triangle3D, point2DTextureCoordinatesAResultOffset, triangle3DOffset + TRIANGLE_3_OFFSET_TEXTURE_COORDINATES_A);
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
		return point2DSet(point2DTextureCoordinatesBResult, triangle3D, point2DTextureCoordinatesBResultOffset, triangle3DOffset + TRIANGLE_3_OFFSET_TEXTURE_COORDINATES_B);
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
		return point2DSet(point2DTextureCoordinatesCResult, triangle3D, point2DTextureCoordinatesCResultOffset, triangle3DOffset + TRIANGLE_3_OFFSET_TEXTURE_COORDINATES_C);
	}
	
//	TODO: Add Javadocs!
	public static double[] triangle3DSet(final double[] triangle3DResult, final double[] point3DPositionA, final double[] point3DPositionB, final double[] point3DPositionC, final double[] vector3DSurfaceNormalA, final double[] vector3DSurfaceNormalB, final double[] vector3DSurfaceNormalC, final double[] point2DTextureCoordinatesA, final double[] point2DTextureCoordinatesB, final double[] point2DTextureCoordinatesC) {
		return triangle3DSet(triangle3DResult, point3DPositionA, point3DPositionB, point3DPositionC, vector3DSurfaceNormalA, vector3DSurfaceNormalB, vector3DSurfaceNormalC, point2DTextureCoordinatesA, point2DTextureCoordinatesB, point2DTextureCoordinatesC, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] triangle3DSet(final double[] triangle3DResult, final double[] point3DPositionA, final double[] point3DPositionB, final double[] point3DPositionC, final double[] vector3DSurfaceNormalA, final double[] vector3DSurfaceNormalB, final double[] vector3DSurfaceNormalC, final double[] point2DTextureCoordinatesA, final double[] point2DTextureCoordinatesB, final double[] point2DTextureCoordinatesC, final int triangle3DResultOffset, final int point3DPositionAOffset, final int point3DPositionBOffset, final int point3DPositionCOffset, final int vector3DSurfaceNormalAOffset, final int vector3DSurfaceNormalBOffset, final int vector3DSurfaceNormalCOffset, final int point2DTextureCoordinatesAOffset, final int point2DTextureCoordinatesBOffset, final int point2DTextureCoordinatesCOffset) {
		triangle3DResult[triangle3DResultOffset + TRIANGLE_3_OFFSET_ID] = TRIANGLE_3_ID;
		triangle3DResult[triangle3DResultOffset + TRIANGLE_3_OFFSET_SIZE] = TRIANGLE_3_SIZE;
		
		point3DSet(triangle3DResult, point3DPositionA, triangle3DResultOffset + TRIANGLE_3_OFFSET_POSITION_A, point3DPositionAOffset);
		point3DSet(triangle3DResult, point3DPositionB, triangle3DResultOffset + TRIANGLE_3_OFFSET_POSITION_B, point3DPositionBOffset);
		point3DSet(triangle3DResult, point3DPositionC, triangle3DResultOffset + TRIANGLE_3_OFFSET_POSITION_C, point3DPositionCOffset);
		
		vector3DSet(triangle3DResult, vector3DSurfaceNormalA, triangle3DResultOffset + TRIANGLE_3_OFFSET_SURFACE_NORMAL_A, vector3DSurfaceNormalAOffset);
		vector3DSet(triangle3DResult, vector3DSurfaceNormalB, triangle3DResultOffset + TRIANGLE_3_OFFSET_SURFACE_NORMAL_B, vector3DSurfaceNormalBOffset);
		vector3DSet(triangle3DResult, vector3DSurfaceNormalC, triangle3DResultOffset + TRIANGLE_3_OFFSET_SURFACE_NORMAL_C, vector3DSurfaceNormalCOffset);
		
		point2DSet(triangle3DResult, point2DTextureCoordinatesA, triangle3DResultOffset + TRIANGLE_3_OFFSET_TEXTURE_COORDINATES_A, point2DTextureCoordinatesAOffset);
		point2DSet(triangle3DResult, point2DTextureCoordinatesB, triangle3DResultOffset + TRIANGLE_3_OFFSET_TEXTURE_COORDINATES_B, point2DTextureCoordinatesBOffset);
		point2DSet(triangle3DResult, point2DTextureCoordinatesC, triangle3DResultOffset + TRIANGLE_3_OFFSET_TEXTURE_COORDINATES_C, point2DTextureCoordinatesCOffset);
		
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