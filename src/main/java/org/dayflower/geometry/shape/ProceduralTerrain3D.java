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

import static org.dayflower.utility.Doubles.isNaN;

import java.util.Objects;
import java.util.Optional;
import java.util.function.DoubleBinaryOperator;

import org.dayflower.geometry.BoundingVolume3D;
import org.dayflower.geometry.OrthonormalBasis33D;
import org.dayflower.geometry.Point2D;
import org.dayflower.geometry.Point3D;
import org.dayflower.geometry.Ray3D;
import org.dayflower.geometry.Shape3D;
import org.dayflower.geometry.SurfaceIntersection3D;
import org.dayflower.geometry.Vector3D;
import org.dayflower.geometry.boundingvolume.AxisAlignedBoundingBox3D;
import org.dayflower.utility.Doubles;
import org.dayflower.utility.NoiseD;

/**
 * A {@code ProceduralTerrain3D} denotes a 3-dimensional procedural terrain that uses the data type {@code double}.
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class ProceduralTerrain3D implements Shape3D {
	/**
	 * The name of this {@code ProceduralTerrain3D} class.
	 */
	public static final String NAME = "Procedural Terrain";
	
	/**
	 * The ID of this {@code ProceduralTerrain3D} class.
	 */
	public static final int ID = 10;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final DoubleBinaryOperator doubleBinaryOperator;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code ProceduralTerrain3D} instance.
	 * <p>
	 * If {@code doubleBinaryOperator} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param doubleBinaryOperator a {@code DoubleBinaryOperator} instance
	 * @throws NullPointerException thrown if, and only if, {@code doubleBinaryOperator} is {@code null}
	 */
	public ProceduralTerrain3D(final DoubleBinaryOperator doubleBinaryOperator) {
		this.doubleBinaryOperator = Objects.requireNonNull(doubleBinaryOperator, "doubleBinaryOperator == null");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link BoundingVolume3D} instance that contains parts of this {@code ProceduralTerrain3D} instance.
	 * 
	 * @return a {@code BoundingVolume3D} instance that contains parts of this {@code ProceduralTerrain3D} instance
	 */
	@Override
	public BoundingVolume3D getBoundingVolume() {
		return new AxisAlignedBoundingBox3D(Point3D.minimum(), Point3D.maximum());
	}
	
	/**
	 * Performs an intersection test between {@code ray} and this {@code ProceduralTerrain3D} instance.
	 * <p>
	 * Returns an {@code Optional} with an optional {@link SurfaceIntersection3D} instance that contains information about the intersection, if it was found.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3D} to perform an intersection test against this {@code ProceduralTerrain3D} instance
	 * @param tMinimum the minimum parametric distance
	 * @param tMaximum the maximum parametric distance
	 * @return an {@code Optional} with an optional {@code SurfaceIntersection3D} instance that contains information about the intersection, if it was found
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	@Override
	public Optional<SurfaceIntersection3D> intersection(final Ray3D ray, final double tMinimum, final double tMaximum) {
		final double t = doIntersection(ray);
		
		if(!isNaN(t)) {
			final Point3D origin = ray.getOrigin();
			
			final Vector3D direction = ray.getDirection();
			
			final Point3D surfaceIntersectionPoint = Point3D.add(origin, direction, t);
			
			final Point2D textureCoordinates = new Point2D(surfaceIntersectionPoint.getX(), surfaceIntersectionPoint.getZ());
			
			final OrthonormalBasis33D orthonormalBasisG = new OrthonormalBasis33D(doCalculateSurfaceNormalG(surfaceIntersectionPoint));
			final OrthonormalBasis33D orthonormalBasisS = orthonormalBasisG;
			
			final Vector3D surfaceIntersectionPointError = new Vector3D();
			
			return Optional.of(new SurfaceIntersection3D(orthonormalBasisG, orthonormalBasisS, textureCoordinates, surfaceIntersectionPoint, ray, this, surfaceIntersectionPointError, t));
		}
		
		return SurfaceIntersection3D.EMPTY;
	}
	
	/**
	 * Returns a {@code String} with the name of this {@code ProceduralTerrain3D} instance.
	 * 
	 * @return a {@code String} with the name of this {@code ProceduralTerrain3D} instance
	 */
	@Override
	public String getName() {
		return NAME;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code ProceduralTerrain3D} instance.
	 * 
	 * @return a {@code String} representation of this {@code ProceduralTerrain3D} instance
	 */
	@Override
	public String toString() {
		return "new ProceduralTerrain3D(...)";
	}
	
	/**
	 * Compares {@code object} to this {@code ProceduralTerrain3D} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code ProceduralTerrain3D}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code ProceduralTerrain3D} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code ProceduralTerrain3D}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof ProceduralTerrain3D)) {
			return false;
		} else if(!Objects.equals(this.doubleBinaryOperator, ProceduralTerrain3D.class.cast(object).doubleBinaryOperator)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns the surface area of this {@code ProceduralTerrain3D} instance.
	 * <p>
	 * This method returns {@code Double.POSITIVE_INFINITY}.
	 * 
	 * @return the surface area of this {@code ProceduralTerrain3D} instance
	 */
	@Override
	public double getSurfaceArea() {
		return Double.POSITIVE_INFINITY;
	}
	
	/**
	 * Performs an intersection test between {@code ray} and this {@code ProceduralTerrain3D} instance.
	 * <p>
	 * Returns {@code t}, the parametric distance to the surface intersection point, or {@code Double.NaN} if no intersection exists.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3D} to perform an intersection test against this {@code ProceduralTerrain3D} instance
	 * @param tMinimum the minimum parametric distance
	 * @param tMaximum the maximum parametric distance
	 * @return {@code t}, the parametric distance to the surface intersection point, or {@code Double.NaN} if no intersection exists
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	@Override
	public double intersectionT(final Ray3D ray, final double tMinimum, final double tMaximum) {
		return doIntersection(ray);
	}
	
	/**
	 * Returns an {@code int} with the ID of this {@code ProceduralTerrain3D} instance.
	 * 
	 * @return an {@code int} with the ID of this {@code ProceduralTerrain3D} instance
	 */
	@Override
	public int getID() {
		return ID;
	}
	
	/**
	 * Returns a hash code for this {@code ProceduralTerrain3D} instance.
	 * 
	 * @return a hash code for this {@code ProceduralTerrain3D} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.doubleBinaryOperator);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code ProceduralTerrain3D} instance that uses a Simplex-based fractal algorithm.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * ProceduralTerrain3D.simplexFractalXY(0.8D, 0.2D, 1.0D / 2.0D, 2.0D, 2);
	 * }
	 * </pre>
	 * 
	 * @return a {@code ProceduralTerrain3D} instance that uses a Simplex-based fractal algorithm
	 */
	public static ProceduralTerrain3D simplexFractalXY() {
		return simplexFractalXY(0.8D, 0.2D, 1.0D / 2.0D, 2.0D, 2);
	}
	
	/**
	 * Returns a {@code ProceduralTerrain3D} instance that uses a Simplex-based fractal algorithm.
	 * 
	 * @param amplitude the amplitude to use
	 * @param frequency the frequency to use
	 * @param gain the gain to use
	 * @param lacunarity the lacunarity to use
	 * @param octaves the octaves to use
	 * @return a {@code ProceduralTerrain3D} instance that uses a Simplex-based fractal algorithm
	 */
	public static ProceduralTerrain3D simplexFractalXY(final double amplitude, final double frequency, final double gain, final double lacunarity, final int octaves) {
		return new ProceduralTerrain3D((x, y) -> NoiseD.simplexFractalXY(x, y, amplitude, frequency, gain, lacunarity, octaves));
	}
	
	/**
	 * Returns a {@code ProceduralTerrain3D} instance that uses a sine algorithm.
	 * 
	 * @return a {@code ProceduralTerrain3D} instance that uses a sine algorithm
	 */
	public static ProceduralTerrain3D sin() {
		return new ProceduralTerrain3D((x, y) -> Doubles.sin(x) * Doubles.sin(y));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private Vector3D doCalculateSurfaceNormalG(final Point3D surfaceIntersectionPoint) {
		final double x = doApplyAsDouble(surfaceIntersectionPoint.getX() - 0.0001D, surfaceIntersectionPoint.getZ() - 0.0000D) - doApplyAsDouble(surfaceIntersectionPoint.getX() + 0.0001D, surfaceIntersectionPoint.getZ() + 0.0000D);
		final double y = 2.0D * 0.0001D;
		final double z = doApplyAsDouble(surfaceIntersectionPoint.getX() - 0.0000D, surfaceIntersectionPoint.getZ() - 0.0001D) - doApplyAsDouble(surfaceIntersectionPoint.getX() + 0.0000D, surfaceIntersectionPoint.getZ() + 0.0001D);
		
		return Vector3D.normalize(new Vector3D(x, y, z));
	}
	
	private double doApplyAsDouble(final double x, final double y) {
		return this.doubleBinaryOperator.applyAsDouble(x, y);
	}
	
	private double doIntersection(final Ray3D ray) {
		return doIntersection(ray, 0.001D, 100.0D);
	}
	
	private double doIntersection(final Ray3D ray, final double tMinimum, final double tMaximum) {
		final Point3D origin = ray.getOrigin();
		
		final Vector3D direction = ray.getDirection();
		
		final double originX = origin.getX();
		final double originY = origin.getY();
		final double originZ = origin.getZ();
		
		final double directionX = direction.getX();
		final double directionY = direction.getY();
		final double directionZ = direction.getZ();
		
		double t = 0.0D;
		
		double tDelta = 0.01D;
		
		double lastH = 0.0D;
		double lastY = 0.0D;
		
		for(double tCurrent = tMinimum; tCurrent < tMaximum; tCurrent += tDelta) {
			final double surfaceIntersectionPointX = originX + directionX * tCurrent;
			final double surfaceIntersectionPointY = originY + directionY * tCurrent;
			final double surfaceIntersectionPointZ = originZ + directionZ * tCurrent;
			
			final double h = doApplyAsDouble(surfaceIntersectionPointX, surfaceIntersectionPointZ);
			
			if(surfaceIntersectionPointY < h) {
				t = tCurrent - tDelta + tDelta * (lastH - lastY) / (surfaceIntersectionPointY - lastY - h + lastH);
				
				tCurrent = tMaximum;
			}
			
			tDelta = 0.01D * tCurrent;
			
			lastH = h;
			lastY = surfaceIntersectionPointY;
		}
		
		return t > 0.0D ? t : Double.NaN;
	}
}