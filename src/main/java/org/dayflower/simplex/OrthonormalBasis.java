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

import static org.dayflower.simplex.Point.point2D;
import static org.dayflower.simplex.Point.point3D;
import static org.dayflower.simplex.Point.point3DAdd;
import static org.dayflower.simplex.Point.point3DGetX;
import static org.dayflower.simplex.Point.point3DGetY;
import static org.dayflower.simplex.Point.point3DGetZ;
import static org.dayflower.simplex.Point.point3DLerp;
import static org.dayflower.simplex.Point.point3DMidpoint;
import static org.dayflower.simplex.Point.point3DSurfaceIntersectionPointCone3D;
import static org.dayflower.simplex.Point.point3DSurfaceIntersectionPointCylinder3D;
import static org.dayflower.simplex.Point.point3DSurfaceIntersectionPointDisk3D;
import static org.dayflower.simplex.Ray.ray3DGetDirection;
import static org.dayflower.simplex.Ray.ray3DGetOrigin;
import static org.dayflower.simplex.Shape.CONE_3_ID;
import static org.dayflower.simplex.Shape.CYLINDER_3_ID;
import static org.dayflower.simplex.Shape.DISK_3_ID;
import static org.dayflower.simplex.Shape.HYPERBOLOID_3_ID;
import static org.dayflower.simplex.Shape.PARABOLOID_3_ID;
import static org.dayflower.simplex.Shape.PLANE_3_ID;
import static org.dayflower.simplex.Shape.POLYGON_3_ID;
import static org.dayflower.simplex.Shape.RECTANGLE_3_ID;
import static org.dayflower.simplex.Shape.RECTANGULAR_CUBOID_3_ID;
import static org.dayflower.simplex.Shape.SPHERE_3_ID;
import static org.dayflower.simplex.Shape.TORUS_3_ID;
import static org.dayflower.simplex.Shape.TRIANGLE_3_ID;
import static org.dayflower.simplex.Shape.cone3DGetPhiMax;
import static org.dayflower.simplex.Shape.cone3DGetZMax;
import static org.dayflower.simplex.Shape.cylinder3DGetPhiMax;
import static org.dayflower.simplex.Shape.cylinder3DGetZMax;
import static org.dayflower.simplex.Shape.cylinder3DGetZMin;
import static org.dayflower.simplex.Shape.disk3DGetPhiMax;
import static org.dayflower.simplex.Shape.disk3DGetRadiusInner;
import static org.dayflower.simplex.Shape.disk3DGetRadiusOuter;
import static org.dayflower.simplex.Shape.hyperboloid3DGetA;
import static org.dayflower.simplex.Shape.hyperboloid3DGetB;
import static org.dayflower.simplex.Shape.hyperboloid3DGetPhiMax;
import static org.dayflower.simplex.Shape.paraboloid3DGetPhiMax;
import static org.dayflower.simplex.Shape.paraboloid3DGetZMax;
import static org.dayflower.simplex.Shape.paraboloid3DGetZMin;
import static org.dayflower.simplex.Shape.plane3DGetSurfaceNormal;
import static org.dayflower.simplex.Shape.polygon3DGetSurfaceNormal;
import static org.dayflower.simplex.Shape.rectangle3DGetA;
import static org.dayflower.simplex.Shape.rectangle3DGetB;
import static org.dayflower.simplex.Shape.rectangle3DGetC;
import static org.dayflower.simplex.Shape.rectangularCuboid3DGetMaximum;
import static org.dayflower.simplex.Shape.rectangularCuboid3DGetMinimum;
import static org.dayflower.simplex.Shape.shape3DGetID;
import static org.dayflower.simplex.Shape.sphere3DGetCenter;
import static org.dayflower.simplex.Shape.torus3DGetRadiusInner;
import static org.dayflower.simplex.Shape.torus3DGetRadiusOuter;
import static org.dayflower.simplex.Shape.triangle3DGetPositionA;
import static org.dayflower.simplex.Shape.triangle3DGetPositionB;
import static org.dayflower.simplex.Shape.triangle3DGetPositionC;
import static org.dayflower.simplex.Shape.triangle3DGetSurfaceNormalA;
import static org.dayflower.simplex.Shape.triangle3DGetSurfaceNormalB;
import static org.dayflower.simplex.Shape.triangle3DGetSurfaceNormalC;
import static org.dayflower.simplex.Shape.triangle3DGetTextureCoordinatesA;
import static org.dayflower.simplex.Shape.triangle3DGetTextureCoordinatesB;
import static org.dayflower.simplex.Shape.triangle3DGetTextureCoordinatesC;
import static org.dayflower.simplex.Vector.vector2DCrossProduct;
import static org.dayflower.simplex.Vector.vector2DDirection;
import static org.dayflower.simplex.Vector.vector2DGetX;
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
import static org.dayflower.simplex.Vector.vector3DLengthSquared;
import static org.dayflower.simplex.Vector.vector3DMultiply;
import static org.dayflower.simplex.Vector.vector3DNormalNormalized;
import static org.dayflower.simplex.Vector.vector3DNormalize;
import static org.dayflower.simplex.Vector.vector3DSet;
import static org.dayflower.utility.Doubles.PI_MULTIPLIED_BY_2;
import static org.dayflower.utility.Doubles.abs;
import static org.dayflower.utility.Doubles.atan2;
import static org.dayflower.utility.Doubles.cos;
import static org.dayflower.utility.Doubles.getOrAdd;
import static org.dayflower.utility.Doubles.isZero;
import static org.dayflower.utility.Doubles.sin;
import static org.dayflower.utility.Doubles.sqrt;

import java.lang.reflect.Field;//TODO: Add Javadocs!

/**
 * A class that consists exclusively of static methods that returns or performs various operations on orthonormal bases.
 * <p>
 * This class currently supports the following:
 * <ul>
 * <li>{@code OrthonormalBasis33D} - an orthonormal basis that is constructed by three 3-dimensional vectors and is represented by a {@code double[]}.</li>
 * </ul>
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class OrthonormalBasis {
	/**
	 * The relative offset for the vector denoted by U in a {@code double[]} that contains an orthonormal basis.
	 */
	public static final int ORTHONORMAL_BASIS_OFFSET_U = 0;
	
	/**
	 * The relative offset for the vector denoted by V in a {@code double[]} that contains an orthonormal basis.
	 */
	public static final int ORTHONORMAL_BASIS_OFFSET_V = 3;
	
	/**
	 * The relative offset for the vector denoted by W in a {@code double[]} that contains an orthonormal basis.
	 */
	public static final int ORTHONORMAL_BASIS_OFFSET_W = 6;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private OrthonormalBasis() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	// OrthonormalBasis33D /////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code double[]} that contains an orthonormal basis that consists of three vectors with three components each.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * OrthonormalBasis.orthonormalBasis33D(Vector.vector3D(1.0D, 0.0D, 0.0D), Vector.vector3D(0.0D, 1.0D, 0.0D), Vector.vector3D(0.0D, 0.0D, 1.0D));
	 * }
	 * </pre>
	 * 
	 * @return a {@code double[]} that contains an orthonormal basis that consists of three vectors with three components each
	 */
	public static double[] orthonormalBasis33D() {
		return orthonormalBasis33D(vector3D(1.0D, 0.0D, 0.0D), vector3D(0.0D, 1.0D, 0.0D), vector3D(0.0D, 0.0D, 1.0D));
	}
	
	/**
	 * Returns a {@code double[]} that contains an orthonormal basis that consists of three vectors with three components each.
	 * <p>
	 * If either {@code vector3DU}, {@code vector3DV} or {@code vector3DW} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code vector3DU.length < 3}, {@code vector3DV.length < 3} or {@code vector3DW.length < 3}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * OrthonormalBasis.orthonormalBasis33D(vector3DU, vector3DV, vector3DW, 0, 0, 0);
	 * }
	 * </pre>
	 * 
	 * @param vector3DU a {@code double[]} that contains the vector denoted by U
	 * @param vector3DV a {@code double[]} that contains the vector denoted by V
	 * @param vector3DW a {@code double[]} that contains the vector denoted by W
	 * @return a {@code double[]} that contains an orthonormal basis that consists of three vectors with three components each
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code vector3DU.length < 3}, {@code vector3DV.length < 3} or {@code vector3DW.length < 3}
	 * @throws NullPointerException thrown if, and only if, either {@code vector3DU}, {@code vector3DV} or {@code vector3DW} are {@code null}
	 */
	public static double[] orthonormalBasis33D(final double[] vector3DU, final double[] vector3DV, final double[] vector3DW) {
		return orthonormalBasis33D(vector3DU, vector3DV, vector3DW, 0, 0, 0);
	}
	
	/**
	 * Returns a {@code double[]} that contains an orthonormal basis that consists of three vectors with three components each.
	 * <p>
	 * If either {@code vector3DU}, {@code vector3DV} or {@code vector3DW} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code vector3DU.length < vector3DUOffset + 3}, {@code vector3DUOffset < 0}, {@code vector3DV.length < vector3DVOffset + 3}, {@code vector3DVOffset < 0}, {@code vector3DW.length < vector3DWOffset} or {@code vector3DWOffset < 0}, an
	 * {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param vector3DU a {@code double[]} that contains the vector denoted by U
	 * @param vector3DV a {@code double[]} that contains the vector denoted by V
	 * @param vector3DW a {@code double[]} that contains the vector denoted by W
	 * @param vector3DUOffset the offset in {@code vector3DU} to start at
	 * @param vector3DVOffset the offset in {@code vector3DV} to start at
	 * @param vector3DWOffset the offset in {@code vector3DW} to start at
	 * @return a {@code double[]} that contains an orthonormal basis that consists of three vectors with three components each
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code vector3DU.length < vector3DUOffset + 3}, {@code vector3DUOffset < 0}, {@code vector3DV.length < vector3DVOffset + 3}, {@code vector3DVOffset < 0},
	 *                                        {@code vector3DW.length < vector3DWOffset} or {@code vector3DWOffset < 0}
	 * @throws NullPointerException thrown if, and only if, either {@code vector3DU}, {@code vector3DV} or {@code vector3DW} are {@code null}
	 */
	public static double[] orthonormalBasis33D(final double[] vector3DU, final double[] vector3DV, final double[] vector3DW, final int vector3DUOffset, final int vector3DVOffset, final int vector3DWOffset) {
		final double uX = vector3DU[vector3DUOffset + 0];
		final double uY = vector3DU[vector3DUOffset + 1];
		final double uZ = vector3DU[vector3DUOffset + 2];
		
		final double vX = vector3DV[vector3DVOffset + 0];
		final double vY = vector3DV[vector3DVOffset + 1];
		final double vZ = vector3DV[vector3DVOffset + 2];
		
		final double wX = vector3DW[vector3DWOffset + 0];
		final double wY = vector3DW[vector3DWOffset + 1];
		final double wZ = vector3DW[vector3DWOffset + 2];
		
		return new double[] {uX, uY, uZ, vX, vY, vZ, wX, wY, wZ};
	}
	
//	TODO: Add Javadocs!
	public static double[] orthonormalBasis33DFromCone3D(final double[] ray3D, final double[] cone3D, final double t) {
		return orthonormalBasis33DFromCone3D(ray3D, cone3D, t, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] orthonormalBasis33DFromCone3D(final double[] ray3D, final double[] cone3D, final double t, final int ray3DOffset, final int cone3DOffset) {
		final double phiMax = cone3DGetPhiMax(cone3D, cone3DOffset);
		final double zMax = cone3DGetZMax(cone3D, cone3DOffset);
		
		final double[] point3DSurfaceIntersectionPoint = point3DSurfaceIntersectionPointCone3D(ray3D, cone3D, t, ray3DOffset, cone3DOffset);
		
		final double v = point3DGetZ(point3DSurfaceIntersectionPoint) / zMax;
		
		final double uX = -phiMax * point3DGetY(point3DSurfaceIntersectionPoint);
		final double uY = +phiMax * point3DGetX(point3DSurfaceIntersectionPoint);
		final double uZ = +0.0D;
		
		final double vX = -point3DGetX(point3DSurfaceIntersectionPoint) / (1.0D - v);
		final double vY = -point3DGetY(point3DSurfaceIntersectionPoint) / (1.0D - v);
		final double vZ = +zMax;
		
		final double[] vector3DU = vector3DNormalize(vector3D(uX, uY, uZ));
		final double[] vector3DV = vector3DNormalize(vector3D(vX, vY, vZ));
		final double[] vector3DW = vector3DCrossProduct(vector3DU, vector3DV);
		
		return orthonormalBasis33D(vector3DU, vector3DV, vector3DW);
	}
	
//	TODO: Add Javadocs!
	public static double[] orthonormalBasis33DFromCylinder3D(final double[] ray3D, final double[] cylinder3D, final double t) {
		return orthonormalBasis33DFromCylinder3D(ray3D, cylinder3D, t, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] orthonormalBasis33DFromCylinder3D(final double[] ray3D, final double[] cylinder3D, final double t, final int ray3DOffset, final int cylinder3DOffset) {
		final double phiMax = cylinder3DGetPhiMax(cylinder3D, cylinder3DOffset);
		final double zMax = cylinder3DGetZMax(cylinder3D, cylinder3DOffset);
		final double zMin = cylinder3DGetZMin(cylinder3D, cylinder3DOffset);
		
		final double[] point3DSurfaceIntersectionPoint = point3DSurfaceIntersectionPointCylinder3D(ray3D, cylinder3D, t, ray3DOffset, cylinder3DOffset);
		
		final double uX = -phiMax * point3DGetY(point3DSurfaceIntersectionPoint);
		final double uY = +phiMax * point3DGetX(point3DSurfaceIntersectionPoint);
		final double uZ = +0.0D;
		
		final double vX = 0.0D;
		final double vY = 0.0D;
		final double vZ = zMax - zMin;
		
		final double[] vector3DU = vector3DNormalize(vector3D(uX, uY, uZ));
		final double[] vector3DV = vector3DNormalize(vector3D(vX, vY, vZ));
		final double[] vector3DW = vector3DCrossProduct(vector3DU, vector3DV);
		
		return orthonormalBasis33D(vector3DU, vector3DV, vector3DW);
	}
	
//	TODO: Add Javadocs!
	public static double[] orthonormalBasis33DFromDisk3D(final double[] ray3D, final double[] disk3D, final double t) {
		return orthonormalBasis33DFromDisk3D(ray3D, disk3D, t, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] orthonormalBasis33DFromDisk3D(final double[] ray3D, final double[] disk3D, final double t, final int ray3DOffset, final int disk3DOffset) {
		final double phiMax = disk3DGetPhiMax(disk3D, disk3DOffset);
		final double radiusInner = disk3DGetRadiusInner(disk3D, disk3DOffset);
		final double radiusOuter = disk3DGetRadiusOuter(disk3D, disk3DOffset);
		
		final double[] point3DSurfaceIntersectionPoint = point3DSurfaceIntersectionPointDisk3D(ray3D, disk3D, t, ray3DOffset, disk3DOffset);
		
		final double distance = sqrt(point3DGetX(point3DSurfaceIntersectionPoint) * point3DGetX(point3DSurfaceIntersectionPoint) + point3DGetY(point3DSurfaceIntersectionPoint) * point3DGetY(point3DSurfaceIntersectionPoint));
		
		final double uX = -phiMax * point3DGetY(point3DSurfaceIntersectionPoint);
		final double uY = +phiMax * point3DGetX(point3DSurfaceIntersectionPoint);
		final double uZ = +0.0D;
		
		final double vX = point3DGetX(point3DSurfaceIntersectionPoint) * (radiusInner - radiusOuter) / distance;
		final double vY = point3DGetY(point3DSurfaceIntersectionPoint) * (radiusInner - radiusOuter) / distance;
		final double vZ = 0.0D;
		
		final double[] vector3DU = vector3DNormalize(vector3D(uX, uY, uZ));
		final double[] vector3DV = vector3DNormalize(vector3D(vX, vY, vZ));
		final double[] vector3DW = vector3DCrossProduct(vector3DU, vector3DV);
		
		return orthonormalBasis33D(vector3DU, vector3DV, vector3DW);
	}
	
//	TODO: Add Javadocs!
	public static double[] orthonormalBasis33DFromHyperboloid3D(final double[] ray3D, final double[] hyperboloid3D, final double t) {
		return orthonormalBasis33DFromHyperboloid3D(ray3D, hyperboloid3D, t, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] orthonormalBasis33DFromHyperboloid3D(final double[] ray3D, final double[] hyperboloid3D, final double t, final int ray3DOffset, final int hyperboloid3DOffset) {
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
		final double uZ = +0.0D;
		
		final double vX = (point3DGetX(point3DB) - point3DGetX(point3DA)) * cosPhi - (point3DGetY(point3DB) - point3DGetY(point3DA)) * sinPhi;
		final double vY = (point3DGetX(point3DB) - point3DGetX(point3DA)) * sinPhi + (point3DGetY(point3DB) - point3DGetY(point3DA)) * cosPhi;
		final double vZ = point3DGetZ(point3DB) - point3DGetZ(point3DA);
		
		final double[] vector3DU = vector3DNormalize(vector3D(uX, uY, uZ));
		final double[] vector3DV = vector3DNormalize(vector3D(vX, vY, vZ));
		final double[] vector3DW = vector3DCrossProduct(vector3DU, vector3DV);
		
		return orthonormalBasis33D(vector3DU, vector3DV, vector3DW);
	}
	
//	TODO: Add Javadocs!
	public static double[] orthonormalBasis33DFromParaboloid3D(final double[] ray3D, final double[] paraboloid3D, final double t) {
		return orthonormalBasis33DFromParaboloid3D(ray3D, paraboloid3D, t, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] orthonormalBasis33DFromParaboloid3D(final double[] ray3D, final double[] paraboloid3D, final double t, final int ray3DOffset, final int paraboloid3DOffset) {
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
	public static double[] orthonormalBasis33DFromPlane3D(final double[] ray3D, final double[] plane3D, final double t) {
		return orthonormalBasis33DFromPlane3D(ray3D, plane3D, t, 0, 0);
	}
	
//	TODO: Add Javadocs!
	@SuppressWarnings("unused")
	public static double[] orthonormalBasis33DFromPlane3D(final double[] ray3D, final double[] plane3D, final double t, final int ray3DOffset, final int plane3DOffset) {
		final double[] vector3DW = plane3DGetSurfaceNormal(plane3D, vector3D(), plane3DOffset, 0);
		
		return orthonormalBasis33DFromW(vector3DW);
	}
	
//	TODO: Add Javadocs!
	public static double[] orthonormalBasis33DFromPolygon3D(final double[] ray3D, final double[] polygon3D, final double t) {
		return orthonormalBasis33DFromPolygon3D(ray3D, polygon3D, t, 0, 0);
	}
	
//	TODO: Add Javadocs!
	@SuppressWarnings("unused")
	public static double[] orthonormalBasis33DFromPolygon3D(final double[] ray3D, final double[] polygon3D, final double t, final int ray3DOffset, final int polygon3DOffset) {
		final double[] vector3DW = polygon3DGetSurfaceNormal(polygon3D, vector3D(), polygon3DOffset, 0);
		
		return orthonormalBasis33DFromW(vector3DW);
	}
	
//	TODO: Add Javadocs!
	public static double[] orthonormalBasis33DFromRectangle3D(final double[] ray3D, final double[] rectangle3D, final double t) {
		return orthonormalBasis33DFromRectangle3D(ray3D, rectangle3D, t, 0, 0);
	}
	
//	TODO: Add Javadocs!
	@SuppressWarnings("unused")
	public static double[] orthonormalBasis33DFromRectangle3D(final double[] ray3D, final double[] rectangle3D, final double t, final int ray3DOffset, final int rectangle3DOffset) {
		final double[] point3DA = rectangle3DGetA(rectangle3D, point3D(), rectangle3DOffset, 0);
		final double[] point3DB = rectangle3DGetB(rectangle3D, point3D(), rectangle3DOffset, 0);
		final double[] point3DC = rectangle3DGetC(rectangle3D, point3D(), rectangle3DOffset, 0);
		
		final double[] vector3DW = vector3DNormalNormalized(point3DA, point3DB, point3DC);
		
		return orthonormalBasis33DFromW(vector3DW);
	}
	
//	TODO: Add Javadocs!
	public static double[] orthonormalBasis33DFromRectangularCuboid3D(final double[] ray3D, final double[] rectangularCuboid3D, final double t) {
		return orthonormalBasis33DFromRectangularCuboid3D(ray3D, rectangularCuboid3D, t, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] orthonormalBasis33DFromRectangularCuboid3D(final double[] ray3D, final double[] rectangularCuboid3D, final double t, final int ray3DOffset, final int rectangularCuboid3DOffset) {
		final double[] point3DOrigin = ray3DGetOrigin(ray3D, point3D(), ray3DOffset, 0);
		
		final double[] vector3DDirection = ray3DGetDirection(ray3D, vector3D(), ray3DOffset, 0);
		
		final double[] point3DMaximum = rectangularCuboid3DGetMaximum(rectangularCuboid3D, point3D(), rectangularCuboid3DOffset, 0);
		final double[] point3DMinimum = rectangularCuboid3DGetMinimum(rectangularCuboid3D, point3D(), rectangularCuboid3DOffset, 0);
		
		final double[] point3D = point3DAdd(point3DOrigin, vector3DDirection, t);
		
		final double[] point3DMidpoint = point3DMidpoint(point3DMaximum, point3DMinimum);
		
		final double[] vector3DHalfDistance = vector3DMultiply(vector3DDirection(point3DMinimum, point3DMaximum), 0.5D);
		
		final double epsilon = 0.0001D;
		
		if(point3DGetX(point3D) + vector3DGetX(vector3DHalfDistance) - epsilon < point3DGetX(point3DMidpoint)) {
			return orthonormalBasis33DFromW(vector3D(-1.0D, +0.0D, +0.0D));
		}
		
		if(point3DGetX(point3D) - vector3DGetX(vector3DHalfDistance) + epsilon > point3DGetX(point3DMidpoint)) {
			return orthonormalBasis33DFromW(vector3D(+1.0D, +0.0D, +0.0D));
		}
		
		if(point3DGetY(point3D) + vector3DGetY(vector3DHalfDistance) - epsilon < point3DGetY(point3DMidpoint)) {
			return orthonormalBasis33DFromW(vector3D(+0.0D, -1.0D, +0.0D));
		}
		
		if(point3DGetY(point3D) - vector3DGetY(vector3DHalfDistance) + epsilon > point3DGetY(point3DMidpoint)) {
			return orthonormalBasis33DFromW(vector3D(+0.0D, +1.0D, +0.0D));
		}
		
		if(point3DGetZ(point3D) + vector3DGetZ(vector3DHalfDistance) - epsilon < point3DGetZ(point3DMidpoint)) {
			return orthonormalBasis33DFromW(vector3D(+0.0D, +0.0D, -1.0D));
		}
		
		if(point3DGetZ(point3D) - vector3DGetZ(vector3DHalfDistance) + epsilon > point3DGetZ(point3DMidpoint)) {
			return orthonormalBasis33DFromW(vector3D(+0.0D, +0.0D, +1.0D));
		}
		
		return orthonormalBasis33D();
	}
	
//	TODO: Add Javadocs!
	public static double[] orthonormalBasis33DFromShape3D(final double[] ray3D, final double[] shape3D, final double t) {
		return orthonormalBasis33DFromShape3D(ray3D, shape3D, t, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] orthonormalBasis33DFromShape3D(final double[] ray3D, final double[] shape3D, final double t, final int ray3DOffset, final int shape3DOffset) {
		switch(shape3DGetID(shape3D, shape3DOffset)) {
			case CONE_3_ID:
				return orthonormalBasis33DFromCone3D(ray3D, shape3D, t, ray3DOffset, shape3DOffset);
			case CYLINDER_3_ID:
				return orthonormalBasis33DFromCylinder3D(ray3D, shape3D, t, ray3DOffset, shape3DOffset);
			case DISK_3_ID:
				return orthonormalBasis33DFromDisk3D(ray3D, shape3D, t, ray3DOffset, shape3DOffset);
			case HYPERBOLOID_3_ID:
				return orthonormalBasis33DFromHyperboloid3D(ray3D, shape3D, t, ray3DOffset, shape3DOffset);
			case PARABOLOID_3_ID:
				return orthonormalBasis33DFromParaboloid3D(ray3D, shape3D, t, ray3DOffset, shape3DOffset);
			case PLANE_3_ID:
				return orthonormalBasis33DFromPlane3D(ray3D, shape3D, t, ray3DOffset, shape3DOffset);
			case POLYGON_3_ID:
				return orthonormalBasis33DFromPolygon3D(ray3D, shape3D, t, ray3DOffset, shape3DOffset);
			case RECTANGLE_3_ID:
				return orthonormalBasis33DFromRectangle3D(ray3D, shape3D, t, ray3DOffset, shape3DOffset);
			case RECTANGULAR_CUBOID_3_ID:
				return orthonormalBasis33DFromRectangularCuboid3D(ray3D, shape3D, t, ray3DOffset, shape3DOffset);
			case SPHERE_3_ID:
				return orthonormalBasis33DFromSphere3D(ray3D, shape3D, t, ray3DOffset, shape3DOffset);
			case TORUS_3_ID:
				return orthonormalBasis33DFromTorus3D(ray3D, shape3D, t, ray3DOffset, shape3DOffset);
			case TRIANGLE_3_ID:
				return orthonormalBasis33DFromTriangle3D(ray3D, shape3D, t, ray3DOffset, shape3DOffset);
			default:
				return orthonormalBasis33D();
		}
	}
	
//	TODO: Add Javadocs!
	public static double[] orthonormalBasis33DFromSphere3D(final double[] ray3D, final double[] sphere3D, final double t) {
		return orthonormalBasis33DFromSphere3D(ray3D, sphere3D, t, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] orthonormalBasis33DFromSphere3D(final double[] ray3D, final double[] sphere3D, final double t, final int ray3DOffset, final int sphere3DOffset) {
		final double[] point3DOrigin = ray3DGetOrigin(ray3D, point3D(), ray3DOffset, 0);
		
		final double[] vector3DDirection = ray3DGetDirection(ray3D, vector3D(), ray3DOffset, 0);
		
		final double[] point3DCenter = sphere3DGetCenter(sphere3D, point3D(), sphere3DOffset, 0);
		final double[] point3D = point3DAdd(point3DOrigin, vector3DDirection, t);
		
		final double[] vector3DW = vector3DDirectionNormalized(point3DCenter, point3D);
		final double[] vector3DV = vector3D(-PI_MULTIPLIED_BY_2 * vector3DGetY(vector3DW), PI_MULTIPLIED_BY_2 * vector3DGetX(vector3DW), 0.0D);
		
		return orthonormalBasis33DFromWV(vector3DW, vector3DV);
	}
	
//	TODO: Add Javadocs!
	public static double[] orthonormalBasis33DFromTorus3D(final double[] ray3D, final double[] torus3D, final double t) {
		return orthonormalBasis33DFromTorus3D(ray3D, torus3D, t, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] orthonormalBasis33DFromTorus3D(final double[] ray3D, final double[] torus3D, final double t, final int ray3DOffset, final int torus3DOffset) {
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
	public static double[] orthonormalBasis33DFromTriangle3D(final double[] ray3D, final double[] triangle3D, final double t) {
		return orthonormalBasis33DFromTriangle3D(ray3D, triangle3D, t, 0, 0);
	}
	
//	TODO: Add Javadocs!
	@SuppressWarnings("unused")
	public static double[] orthonormalBasis33DFromTriangle3D(final double[] ray3D, final double[] triangle3D, final double t, final int ray3DOffset, final int triangle3DOffset) {
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
	public static double[] orthonormalBasis33DFromW(final double[] vector3DW) {
		return orthonormalBasis33DFromW(vector3DW, orthonormalBasis33D());
	}
	
//	TODO: Add Javadocs!
	public static double[] orthonormalBasis33DFromW(final double[] vector3DW, final double[] orthonormalBasis33DResult) {
		return orthonormalBasis33DFromW(vector3DW, orthonormalBasis33DResult, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] orthonormalBasis33DFromW(final double[] vector3DW, final double[] orthonormalBasis33DResult, final int vector3DWOffset, final int orthonormalBasis33DResultOffset) {
		final double[] vector3DWNormalized = vector3DNormalize(vector3DW, vector3D(), vector3DWOffset, 0);
		
		final double x = vector3DGetX(vector3DWNormalized);
		final double y = vector3DGetY(vector3DWNormalized);
		final double z = vector3DGetZ(vector3DWNormalized);
		
		final double[] vector3DV = abs(x) < abs(y) && abs(x) < abs(z) ? vector3D(0.0D, z, -y) : abs(y) < abs(z) ? vector3D(z, 0.0D, -x) : vector3D(y, -x, 0.0D);
		final double[] vector3DVNormalized = vector3DNormalize(vector3DV);
		
		final double[] vector3DUNormalized = vector3DCrossProduct(vector3DVNormalized, vector3DWNormalized);
		
		return orthonormalBasis33DSet(orthonormalBasis33DResult, vector3DUNormalized, vector3DVNormalized, vector3DWNormalized, orthonormalBasis33DResultOffset, 0, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] orthonormalBasis33DFromWV(final double[] vector3DW, final double[] vector3DV) {
		return orthonormalBasis33DFromWV(vector3DW, vector3DV, orthonormalBasis33D());
	}
	
//	TODO: Add Javadocs!
	public static double[] orthonormalBasis33DFromWV(final double[] vector3DW, final double[] vector3DV, final double[] orthonormalBasis33DResult) {
		return orthonormalBasis33DFromWV(vector3DW, vector3DV, orthonormalBasis33DResult, 0, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] orthonormalBasis33DFromWV(final double[] vector3DW, final double[] vector3DV, final double[] orthonormalBasis33DResult, final int vector3DWOffset, final int vector3DVOffset, final int orthonormalBasis33DResultOffset) {
		final double[] vector3DWNormalized = vector3DNormalize(vector3DW, vector3D(), vector3DWOffset, 0);
		final double[] vector3DUNormalized = vector3DCrossProduct(vector3DNormalize(vector3DV, vector3D(), vector3DVOffset, 0), vector3DWNormalized);
		final double[] vector3DVNormalized = vector3DCrossProduct(vector3DWNormalized, vector3DUNormalized);
		
		return orthonormalBasis33DSet(orthonormalBasis33DResult, vector3DUNormalized, vector3DVNormalized, vector3DWNormalized, orthonormalBasis33DResultOffset, 0, 0, 0);
	}
	
	/**
	 * Returns a {@code double[]} that contains the vector denoted by U and is associated with the orthonormal basis contained in {@code orthonormalBasis33D}.
	 * <p>
	 * If {@code orthonormalBasis33D} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code orthonormalBasis33D.length < 3}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * OrthonormalBasis.orthonormalBasis33DGetU(orthonormalBasis33D, Vector.vector3D());
	 * }
	 * </pre>
	 * 
	 * @param orthonormalBasis33D a {@code double[]} that contains an orthonormal basis
	 * @return a {@code double[]} that contains the vector denoted by U and is associated with the orthonormal basis contained in {@code orthonormalBasis33D}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code orthonormalBasis33D.length < 3}
	 * @throws NullPointerException thrown if, and only if, {@code orthonormalBasis33D} is {@code null}
	 */
	public static double[] orthonormalBasis33DGetU(final double[] orthonormalBasis33D) {
		return orthonormalBasis33DGetU(orthonormalBasis33D, vector3D());
	}
	
	/**
	 * Returns a {@code double[]} that contains the vector denoted by U and is associated with the orthonormal basis contained in {@code orthonormalBasis33D}.
	 * <p>
	 * If either {@code orthonormalBasis33D} or {@code vector3DUResult} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code orthonormalBasis33D.length < 3} or {@code vector3DUResult.length < 3}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * OrthonormalBasis.orthonormalBasis33DGetU(orthonormalBasis33D, vector3DUResult, 0, 0);
	 * }
	 * </pre>
	 * 
	 * @param orthonormalBasis33D a {@code double[]} that contains an orthonormal basis
	 * @param vector3DUResult a {@code double[]} that contains the vector to return
	 * @return a {@code double[]} that contains the vector denoted by U and is associated with the orthonormal basis contained in {@code orthonormalBasis33D}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code orthonormalBasis33D.length < 3} or {@code vector3DUResult.length < 3}
	 * @throws NullPointerException thrown if, and only if, either {@code orthonormalBasis33D} or {@code vector3DUResult} are {@code null}
	 */
	public static double[] orthonormalBasis33DGetU(final double[] orthonormalBasis33D, final double[] vector3DUResult) {
		return orthonormalBasis33DGetU(orthonormalBasis33D, vector3DUResult, 0, 0);
	}
	
	/**
	 * Returns a {@code double[]} that contains the vector denoted by U and is associated with the orthonormal basis contained in {@code orthonormalBasis33D}.
	 * <p>
	 * If either {@code orthonormalBasis33D} or {@code vector3DUResult} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code orthonormalBasis33D.length < orthonormalBasis33DOffset + 3}, {@code orthonormalBasis33DOffset < 0}, {@code vector3DUResult.length < vector3DUResultOffset + 3} or {@code vector3DUResultOffset < 0}, an
	 * {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param orthonormalBasis33D a {@code double[]} that contains an orthonormal basis
	 * @param vector3DUResult a {@code double[]} that contains the vector to return
	 * @param orthonormalBasis33DOffset the offset in {@code orthonormalBasis33D} to start at
	 * @param vector3DUResultOffset the offset in {@code vector3DUResult} to start at
	 * @return a {@code double[]} that contains the vector denoted by U and is associated with the orthonormal basis contained in {@code orthonormalBasis33D}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code orthonormalBasis33D.length < orthonormalBasis33DOffset + 3}, {@code orthonormalBasis33DOffset < 0}, {@code vector3DUResult.length < vector3DUResultOffset + 3} or
	 *                                        {@code vector3DUResultOffset < 0}
	 * @throws NullPointerException thrown if, and only if, either {@code orthonormalBasis33D} or {@code vector3DUResult} are {@code null}
	 */
	public static double[] orthonormalBasis33DGetU(final double[] orthonormalBasis33D, final double[] vector3DUResult, final int orthonormalBasis33DOffset, final int vector3DUResultOffset) {
		final double component1 = orthonormalBasis33D[orthonormalBasis33DOffset + ORTHONORMAL_BASIS_OFFSET_U + 0];
		final double component2 = orthonormalBasis33D[orthonormalBasis33DOffset + ORTHONORMAL_BASIS_OFFSET_U + 1];
		final double component3 = orthonormalBasis33D[orthonormalBasis33DOffset + ORTHONORMAL_BASIS_OFFSET_U + 2];
		
		return vector3DSet(vector3DUResult, component1, component2, component3, vector3DUResultOffset);
	}
	
	/**
	 * Returns a {@code double[]} that contains the vector denoted by V and is associated with the orthonormal basis contained in {@code orthonormalBasis33D}.
	 * <p>
	 * If {@code orthonormalBasis33D} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code orthonormalBasis33D.length < 6}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * OrthonormalBasis.orthonormalBasis33DGetV(orthonormalBasis33D, Vector.vector3D());
	 * }
	 * </pre>
	 * 
	 * @param orthonormalBasis33D a {@code double[]} that contains an orthonormal basis
	 * @return a {@code double[]} that contains the vector denoted by V and is associated with the orthonormal basis contained in {@code orthonormalBasis33D}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code orthonormalBasis33D.length < 6}
	 * @throws NullPointerException thrown if, and only if, {@code orthonormalBasis33D} is {@code null}
	 */
	public static double[] orthonormalBasis33DGetV(final double[] orthonormalBasis33D) {
		return orthonormalBasis33DGetV(orthonormalBasis33D, vector3D());
	}
	
	/**
	 * Returns a {@code double[]} that contains the vector denoted by V and is associated with the orthonormal basis contained in {@code orthonormalBasis33D}.
	 * <p>
	 * If either {@code orthonormalBasis33D} or {@code vector3DVResult} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code orthonormalBasis33D.length < 6} or {@code vector3DVResult.length < 3}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * OrthonormalBasis.orthonormalBasis33DGetV(orthonormalBasis33D, vector3DVResult, 0, 0);
	 * }
	 * </pre>
	 * 
	 * @param orthonormalBasis33D a {@code double[]} that contains an orthonormal basis
	 * @param vector3DVResult a {@code double[]} that contains the vector to return
	 * @return a {@code double[]} that contains the vector denoted by V and is associated with the orthonormal basis contained in {@code orthonormalBasis33D}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code orthonormalBasis33D.length < 6} or {@code vector3DVResult.length < 3}
	 * @throws NullPointerException thrown if, and only if, either {@code orthonormalBasis33D} or {@code vector3DVResult} are {@code null}
	 */
	public static double[] orthonormalBasis33DGetV(final double[] orthonormalBasis33D, final double[] vector3DVResult) {
		return orthonormalBasis33DGetV(orthonormalBasis33D, vector3DVResult, 0, 0);
	}
	
	/**
	 * Returns a {@code double[]} that contains the vector denoted by V and is associated with the orthonormal basis contained in {@code orthonormalBasis33D}.
	 * <p>
	 * If either {@code orthonormalBasis33D} or {@code vector3DVResult} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code orthonormalBasis33D.length < orthonormalBasis33DOffset + 6}, {@code orthonormalBasis33DOffset < 0}, {@code vector3DVResult.length < vector3DVResultOffset + 3} or {@code vector3DVResultOffset < 0}, an
	 * {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param orthonormalBasis33D a {@code double[]} that contains an orthonormal basis
	 * @param vector3DVResult a {@code double[]} that contains the vector to return
	 * @param orthonormalBasis33DOffset the offset in {@code orthonormalBasis33D} to start at
	 * @param vector3DVResultOffset the offset in {@code vector3DVResult} to start at
	 * @return a {@code double[]} that contains the vector denoted by V and is associated with the orthonormal basis contained in {@code orthonormalBasis33D}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code orthonormalBasis33D.length < orthonormalBasis33DOffset + 6}, {@code orthonormalBasis33DOffset < 0}, {@code vector3DVResult.length < vector3DVResultOffset + 3} or
	 *                                        {@code vector3DVResultOffset < 0}
	 * @throws NullPointerException thrown if, and only if, either {@code orthonormalBasis33D} or {@code vector3DVResult} are {@code null}
	 */
	public static double[] orthonormalBasis33DGetV(final double[] orthonormalBasis33D, final double[] vector3DVResult, final int orthonormalBasis33DOffset, final int vector3DVResultOffset) {
		final double component1 = orthonormalBasis33D[orthonormalBasis33DOffset + ORTHONORMAL_BASIS_OFFSET_V + 0];
		final double component2 = orthonormalBasis33D[orthonormalBasis33DOffset + ORTHONORMAL_BASIS_OFFSET_V + 1];
		final double component3 = orthonormalBasis33D[orthonormalBasis33DOffset + ORTHONORMAL_BASIS_OFFSET_V + 2];
		
		return vector3DSet(vector3DVResult, component1, component2, component3, vector3DVResultOffset);
	}
	
	/**
	 * Returns a {@code double[]} that contains the vector denoted by W and is associated with the orthonormal basis contained in {@code orthonormalBasis33D}.
	 * <p>
	 * If {@code orthonormalBasis33D} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code orthonormalBasis33D.length < 9}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * OrthonormalBasis.orthonormalBasis33DGetW(orthonormalBasis33D, Vector.vector3D());
	 * }
	 * </pre>
	 * 
	 * @param orthonormalBasis33D a {@code double[]} that contains an orthonormal basis
	 * @return a {@code double[]} that contains the vector denoted by W and is associated with the orthonormal basis contained in {@code orthonormalBasis33D}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code orthonormalBasis33D.length < 9}
	 * @throws NullPointerException thrown if, and only if, {@code orthonormalBasis33D} is {@code null}
	 */
	public static double[] orthonormalBasis33DGetW(final double[] orthonormalBasis33D) {
		return orthonormalBasis33DGetW(orthonormalBasis33D, vector3D());
	}
	
	/**
	 * Returns a {@code double[]} that contains the vector denoted by W and is associated with the orthonormal basis contained in {@code orthonormalBasis33D}.
	 * <p>
	 * If either {@code orthonormalBasis33D} or {@code vector3DWResult} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code orthonormalBasis33D.length < 9} or {@code vector3DWResult.length < 3}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * OrthonormalBasis.orthonormalBasis33DGetW(orthonormalBasis33D, vector3DWResult, 0, 0);
	 * }
	 * </pre>
	 * 
	 * @param orthonormalBasis33D a {@code double[]} that contains an orthonormal basis
	 * @param vector3DWResult a {@code double[]} that contains the vector to return
	 * @return a {@code double[]} that contains the vector denoted by W and is associated with the orthonormal basis contained in {@code orthonormalBasis33D}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code orthonormalBasis33D.length < 9} or {@code vector3DWResult.length < 3}
	 * @throws NullPointerException thrown if, and only if, either {@code orthonormalBasis33D} or {@code vector3DWResult} are {@code null}
	 */
	public static double[] orthonormalBasis33DGetW(final double[] orthonormalBasis33D, final double[] vector3DWResult) {
		return orthonormalBasis33DGetW(orthonormalBasis33D, vector3DWResult, 0, 0);
	}
	
	/**
	 * Returns a {@code double[]} that contains the vector denoted by W and is associated with the orthonormal basis contained in {@code orthonormalBasis33D}.
	 * <p>
	 * If either {@code orthonormalBasis33D} or {@code vector3DWResult} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code orthonormalBasis33D.length < orthonormalBasis33DOffset + 9}, {@code orthonormalBasis33DOffset < 0}, {@code vector3DWResult.length < vector3DWResultOffset + 3} or {@code vector3DWResultOffset < 0}, an
	 * {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param orthonormalBasis33D a {@code double[]} that contains an orthonormal basis
	 * @param vector3DWResult a {@code double[]} that contains the vector to return
	 * @param orthonormalBasis33DOffset the offset in {@code orthonormalBasis33D} to start at
	 * @param vector3DWResultOffset the offset in {@code vector3DWResult} to start at
	 * @return a {@code double[]} that contains the vector denoted by W and is associated with the orthonormal basis contained in {@code orthonormalBasis33D}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code orthonormalBasis33D.length < orthonormalBasis33DOffset + 9}, {@code orthonormalBasis33DOffset < 0}, {@code vector3DWResult.length < vector3DWResultOffset + 3} or
	 *                                        {@code vector3DWResultOffset < 0}
	 * @throws NullPointerException thrown if, and only if, either {@code orthonormalBasis33D} or {@code vector3DWResult} are {@code null}
	 */
	public static double[] orthonormalBasis33DGetW(final double[] orthonormalBasis33D, final double[] vector3DWResult, final int orthonormalBasis33DOffset, final int vector3DWResultOffset) {
		final double component1 = orthonormalBasis33D[orthonormalBasis33DOffset + ORTHONORMAL_BASIS_OFFSET_W + 0];
		final double component2 = orthonormalBasis33D[orthonormalBasis33DOffset + ORTHONORMAL_BASIS_OFFSET_W + 1];
		final double component3 = orthonormalBasis33D[orthonormalBasis33DOffset + ORTHONORMAL_BASIS_OFFSET_W + 2];
		
		return vector3DSet(vector3DWResult, component1, component2, component3, vector3DWResultOffset);
	}
	
//	TODO: Add Javadocs!
	public static double[] orthonormalBasis33DSet(final double[] orthonormalBasis33DResult, final double[] vector3DU, final double[] vector3DV, final double[] vector3DW) {
		return orthonormalBasis33DSet(orthonormalBasis33DResult, vector3DU, vector3DV, vector3DW, 0, 0, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] orthonormalBasis33DSet(final double[] orthonormalBasis33DResult, final double[] vector3DU, final double[] vector3DV, final double[] vector3DW, final int orthonormalBasis33DResultOffset, final int vector3DUOffset, final int vector3DVOffset, final int vector3DWOffset) {
		orthonormalBasis33DResult[orthonormalBasis33DResultOffset + ORTHONORMAL_BASIS_OFFSET_U + 0] = vector3DU[vector3DUOffset + 0];
		orthonormalBasis33DResult[orthonormalBasis33DResultOffset + ORTHONORMAL_BASIS_OFFSET_U + 1] = vector3DU[vector3DUOffset + 1];
		orthonormalBasis33DResult[orthonormalBasis33DResultOffset + ORTHONORMAL_BASIS_OFFSET_U + 2] = vector3DU[vector3DUOffset + 2];
		
		orthonormalBasis33DResult[orthonormalBasis33DResultOffset + ORTHONORMAL_BASIS_OFFSET_V + 0] = vector3DV[vector3DVOffset + 0];
		orthonormalBasis33DResult[orthonormalBasis33DResultOffset + ORTHONORMAL_BASIS_OFFSET_V + 1] = vector3DV[vector3DVOffset + 1];
		orthonormalBasis33DResult[orthonormalBasis33DResultOffset + ORTHONORMAL_BASIS_OFFSET_V + 2] = vector3DV[vector3DVOffset + 2];
		
		orthonormalBasis33DResult[orthonormalBasis33DResultOffset + ORTHONORMAL_BASIS_OFFSET_W + 0] = vector3DW[vector3DWOffset + 0];
		orthonormalBasis33DResult[orthonormalBasis33DResultOffset + ORTHONORMAL_BASIS_OFFSET_W + 1] = vector3DW[vector3DWOffset + 1];
		orthonormalBasis33DResult[orthonormalBasis33DResultOffset + ORTHONORMAL_BASIS_OFFSET_W + 2] = vector3DW[vector3DWOffset + 2];
		
		return orthonormalBasis33DResult;
	}
	
//	TODO: Add Javadocs!
	public static double[] orthonormalBasis33DSet(final double[] orthonormalBasis33DResult, final double[] orthonormalBasis33D, final int orthonormalBasis33DResultOffset, final int orthonormalBasis33DOffset) {
		orthonormalBasis33DResult[orthonormalBasis33DResultOffset + ORTHONORMAL_BASIS_OFFSET_U + 0] = orthonormalBasis33D[orthonormalBasis33DOffset + ORTHONORMAL_BASIS_OFFSET_U + 0];
		orthonormalBasis33DResult[orthonormalBasis33DResultOffset + ORTHONORMAL_BASIS_OFFSET_U + 1] = orthonormalBasis33D[orthonormalBasis33DOffset + ORTHONORMAL_BASIS_OFFSET_U + 1];
		orthonormalBasis33DResult[orthonormalBasis33DResultOffset + ORTHONORMAL_BASIS_OFFSET_U + 2] = orthonormalBasis33D[orthonormalBasis33DOffset + ORTHONORMAL_BASIS_OFFSET_U + 2];
		
		orthonormalBasis33DResult[orthonormalBasis33DResultOffset + ORTHONORMAL_BASIS_OFFSET_V + 0] = orthonormalBasis33D[orthonormalBasis33DOffset + ORTHONORMAL_BASIS_OFFSET_V + 0];
		orthonormalBasis33DResult[orthonormalBasis33DResultOffset + ORTHONORMAL_BASIS_OFFSET_V + 1] = orthonormalBasis33D[orthonormalBasis33DOffset + ORTHONORMAL_BASIS_OFFSET_V + 1];
		orthonormalBasis33DResult[orthonormalBasis33DResultOffset + ORTHONORMAL_BASIS_OFFSET_V + 2] = orthonormalBasis33D[orthonormalBasis33DOffset + ORTHONORMAL_BASIS_OFFSET_V + 2];
		
		orthonormalBasis33DResult[orthonormalBasis33DResultOffset + ORTHONORMAL_BASIS_OFFSET_W + 0] = orthonormalBasis33D[orthonormalBasis33DOffset + ORTHONORMAL_BASIS_OFFSET_W + 0];
		orthonormalBasis33DResult[orthonormalBasis33DResultOffset + ORTHONORMAL_BASIS_OFFSET_W + 1] = orthonormalBasis33D[orthonormalBasis33DOffset + ORTHONORMAL_BASIS_OFFSET_W + 1];
		orthonormalBasis33DResult[orthonormalBasis33DResultOffset + ORTHONORMAL_BASIS_OFFSET_W + 2] = orthonormalBasis33D[orthonormalBasis33DOffset + ORTHONORMAL_BASIS_OFFSET_W + 2];
		
		return orthonormalBasis33DResult;
	}
}