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

import static org.dayflower.utility.Floats.isNaN;

import java.io.DataOutput;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Objects;
import java.util.Optional;

import org.dayflower.geometry.BoundingVolume3F;
import org.dayflower.geometry.OrthonormalBasis33F;
import org.dayflower.geometry.Point2F;
import org.dayflower.geometry.Point3F;
import org.dayflower.geometry.Ray3F;
import org.dayflower.geometry.Shape3F;
import org.dayflower.geometry.SurfaceIntersection3F;
import org.dayflower.geometry.Vector3F;
import org.dayflower.geometry.boundingvolume.AxisAlignedBoundingBox3F;
import org.dayflower.noise.SimplexNoiseF;
import org.dayflower.utility.Floats;
import org.macroing.java.util.function.FloatBinaryOperator;

/**
 * A {@code ProceduralTerrain3F} is an implementation of {@link Shape3F} that represents a procedural terrain.
 * <p>
 * This class is immutable and therefore thread-safe.
 * <p>
 * This {@code Shape3F} implementation is not supported on the GPU.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class ProceduralTerrain3F implements Shape3F {
	/**
	 * The name of this {@code ProceduralTerrain3F} class.
	 */
	public static final String NAME = "Procedural Terrain";
	
	/**
	 * The ID of this {@code ProceduralTerrain3F} class.
	 */
	public static final int ID = 10;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final FloatBinaryOperator floatBinaryOperator;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code ProceduralTerrain3F} instance.
	 * <p>
	 * If {@code floatBinaryOperator} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param floatBinaryOperator a {@link FloatBinaryOperator} instance
	 * @throws NullPointerException thrown if, and only if, {@code floatBinaryOperator} is {@code null}
	 */
	public ProceduralTerrain3F(final FloatBinaryOperator floatBinaryOperator) {
		this.floatBinaryOperator = Objects.requireNonNull(floatBinaryOperator, "floatBinaryOperator == null");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link BoundingVolume3F} instance that contains parts of this {@code ProceduralTerrain3F} instance.
	 * 
	 * @return a {@code BoundingVolume3F} instance that contains parts of this {@code ProceduralTerrain3F} instance
	 */
	@Override
	public BoundingVolume3F getBoundingVolume() {
		return new AxisAlignedBoundingBox3F(Point3F.minimum(), Point3F.maximum());
	}
	
	/**
	 * Performs an intersection test between {@code ray} and this {@code ProceduralTerrain3F} instance.
	 * <p>
	 * Returns an {@code Optional} with an optional {@link SurfaceIntersection3F} instance that contains information about the intersection, if it was found.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3F} to perform an intersection test against this {@code ProceduralTerrain3F} instance
	 * @param tMinimum the minimum parametric distance
	 * @param tMaximum the maximum parametric distance
	 * @return an {@code Optional} with an optional {@code SurfaceIntersection3F} instance that contains information about the intersection, if it was found
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	@Override
	public Optional<SurfaceIntersection3F> intersection(final Ray3F ray, final float tMinimum, final float tMaximum) {
		final float t = doIntersection(ray);
		
		if(!isNaN(t)) {
			final Point3F origin = ray.getOrigin();
			
			final Vector3F direction = ray.getDirection();
			
			final Point3F surfaceIntersectionPoint = Point3F.add(origin, direction, t);
			
			final Point2F textureCoordinates = new Point2F(surfaceIntersectionPoint.getX(), surfaceIntersectionPoint.getZ());
			
			final OrthonormalBasis33F orthonormalBasisG = new OrthonormalBasis33F(doCalculateSurfaceNormalG(surfaceIntersectionPoint));
			final OrthonormalBasis33F orthonormalBasisS = orthonormalBasisG;
			
			final Vector3F surfaceIntersectionPointError = new Vector3F();
			
			return Optional.of(new SurfaceIntersection3F(orthonormalBasisG, orthonormalBasisS, textureCoordinates, surfaceIntersectionPoint, ray, this, surfaceIntersectionPointError, t));
		}
		
		return SurfaceIntersection3F.EMPTY;
	}
	
	/**
	 * Returns a {@code String} with the name of this {@code ProceduralTerrain3F} instance.
	 * 
	 * @return a {@code String} with the name of this {@code ProceduralTerrain3F} instance
	 */
	@Override
	public String getName() {
		return NAME;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code ProceduralTerrain3F} instance.
	 * 
	 * @return a {@code String} representation of this {@code ProceduralTerrain3F} instance
	 */
	@Override
	public String toString() {
		return "new ProceduralTerrain3F(...)";
	}
	
	/**
	 * Compares {@code object} to this {@code ProceduralTerrain3F} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code ProceduralTerrain3F}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code ProceduralTerrain3F} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code ProceduralTerrain3F}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof ProceduralTerrain3F)) {
			return false;
		} else if(!Objects.equals(this.floatBinaryOperator, ProceduralTerrain3F.class.cast(object).floatBinaryOperator)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns the surface area of this {@code ProceduralTerrain3F} instance.
	 * <p>
	 * This method returns {@code Float.POSITIVE_INFINITY}.
	 * 
	 * @return the surface area of this {@code ProceduralTerrain3F} instance
	 */
	@Override
	public float getSurfaceArea() {
		return Float.POSITIVE_INFINITY;
	}
	
	/**
	 * Performs an intersection test between {@code ray} and this {@code ProceduralTerrain3F} instance.
	 * <p>
	 * Returns {@code t}, the parametric distance to the surface intersection point, or {@code Float.NaN} if no intersection exists.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3F} to perform an intersection test against this {@code ProceduralTerrain3F} instance
	 * @param tMinimum the minimum parametric distance
	 * @param tMaximum the maximum parametric distance
	 * @return {@code t}, the parametric distance to the surface intersection point, or {@code Float.NaN} if no intersection exists
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	@Override
	public float intersectionT(final Ray3F ray, final float tMinimum, final float tMaximum) {
		return doIntersection(ray);
	}
	
	/**
	 * Returns an {@code int} with the ID of this {@code ProceduralTerrain3F} instance.
	 * 
	 * @return an {@code int} with the ID of this {@code ProceduralTerrain3F} instance
	 */
	@Override
	public int getID() {
		return ID;
	}
	
	/**
	 * Returns a hash code for this {@code ProceduralTerrain3F} instance.
	 * 
	 * @return a hash code for this {@code ProceduralTerrain3F} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.floatBinaryOperator);
	}
	
	/**
	 * Writes this {@code ProceduralTerrain3F} instance to {@code dataOutput}.
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
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code ProceduralTerrain3F} instance that uses a Simplex-based fractal algorithm.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * ProceduralTerrain3F.simplexFractalXY(0.8F, 0.2F, 1.0F / 2.0F, 2.0F, 2);
	 * }
	 * </pre>
	 * 
	 * @return a {@code ProceduralTerrain3F} instance that uses a Simplex-based fractal algorithm
	 */
	public static ProceduralTerrain3F simplexFractalXY() {
		return simplexFractalXY(0.8F, 0.2F, 1.0F / 2.0F, 2.0F, 2);
	}
	
	/**
	 * Returns a {@code ProceduralTerrain3F} instance that uses a Simplex-based fractal algorithm.
	 * 
	 * @param amplitude the amplitude to use
	 * @param frequency the frequency to use
	 * @param gain the gain to use
	 * @param lacunarity the lacunarity to use
	 * @param octaves the octaves to use
	 * @return a {@code ProceduralTerrain3F} instance that uses a Simplex-based fractal algorithm
	 */
	public static ProceduralTerrain3F simplexFractalXY(final float amplitude, final float frequency, final float gain, final float lacunarity, final int octaves) {
		return new ProceduralTerrain3F((x, y) -> SimplexNoiseF.fractalXY(x, y, amplitude, frequency, gain, lacunarity, octaves));
	}
	
	/**
	 * Returns a {@code ProceduralTerrain3F} instance that uses a sine algorithm.
	 * 
	 * @return a {@code ProceduralTerrain3F} instance that uses a sine algorithm
	 */
	public static ProceduralTerrain3F sin() {
		return new ProceduralTerrain3F((x, y) -> Floats.sin(x) * Floats.sin(y));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private Vector3F doCalculateSurfaceNormalG(final Point3F surfaceIntersectionPoint) {
		final float x = doApplyAsFloat(surfaceIntersectionPoint.getX() - 0.0001F, surfaceIntersectionPoint.getZ() - 0.0000F) - doApplyAsFloat(surfaceIntersectionPoint.getX() + 0.0001F, surfaceIntersectionPoint.getZ() + 0.0000F);
		final float y = 2.0F * 0.0001F;
		final float z = doApplyAsFloat(surfaceIntersectionPoint.getX() - 0.0000F, surfaceIntersectionPoint.getZ() - 0.0001F) - doApplyAsFloat(surfaceIntersectionPoint.getX() + 0.0000F, surfaceIntersectionPoint.getZ() + 0.0001F);
		
		return Vector3F.normalize(new Vector3F(x, y, z));
	}
	
	private float doApplyAsFloat(final float x, final float y) {
		return this.floatBinaryOperator.applyAsFloat(x, y);
	}
	
	private float doIntersection(final Ray3F ray) {
		return doIntersection(ray, 0.001F, 100.0F);
	}
	
	private float doIntersection(final Ray3F ray, final float tMinimum, final float tMaximum) {
		final Point3F origin = ray.getOrigin();
		
		final Vector3F direction = ray.getDirection();
		
		final float originX = origin.getX();
		final float originY = origin.getY();
		final float originZ = origin.getZ();
		
		final float directionX = direction.getX();
		final float directionY = direction.getY();
		final float directionZ = direction.getZ();
		
		float t = 0.0F;
		
		float tDelta = 0.01F;
		
		float lastH = 0.0F;
		float lastY = 0.0F;
		
		for(float tCurrent = tMinimum; tCurrent < tMaximum; tCurrent += tDelta) {
			final float surfaceIntersectionPointX = originX + directionX * tCurrent;
			final float surfaceIntersectionPointY = originY + directionY * tCurrent;
			final float surfaceIntersectionPointZ = originZ + directionZ * tCurrent;
			
			final float h = doApplyAsFloat(surfaceIntersectionPointX, surfaceIntersectionPointZ);
			
			if(surfaceIntersectionPointY < h) {
				t = tCurrent - tDelta + tDelta * (lastH - lastY) / (surfaceIntersectionPointY - lastY - h + lastH);
				
				tCurrent = tMaximum;
			}
			
			tDelta = 0.01F * tCurrent;
			
			lastH = h;
			lastY = surfaceIntersectionPointY;
		}
		
		return t > 0.0F ? t : Float.NaN;
	}
}