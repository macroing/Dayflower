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
package org.dayflower.scene.modifier;

import static org.dayflower.utility.Floats.equal;

import java.util.Objects;

import org.dayflower.geometry.Point3F;
import org.dayflower.geometry.Vector3F;
import org.dayflower.noise.SimplexNoiseF;
import org.dayflower.scene.Intersection;

/**
 * A {@code SimplexNoiseNormalMapModifier} is a {@link Modifier} implementation that modifies the surface normal using Simplex noise.
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class SimplexNoiseNormalMapModifier implements Modifier {
	/**
	 * The ID of this {@code SimplexNoiseNormalMapModifier} class.
	 */
	public static final int ID = 3;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final float frequency;
	private final float scale;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code SimplexNoiseNormalMapModifier} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new SimplexNoiseNormalMapModifier(16.0F, 1.0F);
	 * }
	 * </pre>
	 */
	public SimplexNoiseNormalMapModifier() {
		this(16.0F, 1.0F);
	}
	
	/**
	 * Constructs a new {@code SimplexNoiseNormalMapModifier} instance.
	 * 
	 * @param frequency the frequency to use
	 * @param scale the scale to use
	 */
	public SimplexNoiseNormalMapModifier(final float frequency, final float scale) {
		this.frequency = frequency;
		this.scale = scale;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code String} representation of this {@code SimplexNoiseNormalMapModifier} instance.
	 * 
	 * @return a {@code String} representation of this {@code SimplexNoiseNormalMapModifier} instance
	 */
	@Override
	public String toString() {
		return String.format("new SimplexNoiseNormalMapModifier(%+.10f, %+.10f)", Float.valueOf(this.frequency), Float.valueOf(this.scale));
	}
	
	/**
	 * Compares {@code object} to this {@code SimplexNoiseNormalMapModifier} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code SimplexNoiseNormalMapModifier}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code SimplexNoiseNormalMapModifier} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code SimplexNoiseNormalMapModifier}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof SimplexNoiseNormalMapModifier)) {
			return false;
		} else if(!equal(this.frequency, SimplexNoiseNormalMapModifier.class.cast(object).frequency)) {
			return false;
		} else if(!equal(this.scale, SimplexNoiseNormalMapModifier.class.cast(object).scale)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns the frequency.
	 * 
	 * @return the frequency
	 */
	public float getFrequency() {
		return this.frequency;
	}
	
	/**
	 * Returns the scale.
	 * 
	 * @return the scale
	 */
	public float getScale() {
		return this.scale;
	}
	
	/**
	 * Returns an {@code int} with the ID of this {@code SimplexNoiseNormalMapModifier} instance.
	 * 
	 * @return an {@code int} with the ID of this {@code SimplexNoiseNormalMapModifier} instance
	 */
	@Override
	public int getID() {
		return ID;
	}
	
	/**
	 * Returns a hash code for this {@code SimplexNoiseNormalMapModifier} instance.
	 * 
	 * @return a hash code for this {@code SimplexNoiseNormalMapModifier} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(Float.valueOf(this.frequency), Float.valueOf(this.scale));
	}
	
	/**
	 * Modifies the surface at {@code intersection}.
	 * <p>
	 * If {@code intersection} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param intersection an {@link Intersection} instance
	 * @throws NullPointerException thrown if, and only if, {@code intersection} is {@code null}
	 */
	@Override
	public void modify(final Intersection intersection) {
		Objects.requireNonNull(intersection, "intersection == null");
		
		final Point3F surfaceIntersectionPoint = intersection.getSurfaceIntersectionPoint();
		
		final Vector3F surfaceNormalS = intersection.getSurfaceNormalS();
		
		final float frequency = this.frequency;
		final float frequencyReciprocal = 1.0F / frequency;
		
		final float scale = this.scale;
		
		final float x0 = surfaceIntersectionPoint.getX() * frequencyReciprocal;
		final float y0 = surfaceIntersectionPoint.getY() * frequencyReciprocal;
		final float z0 = surfaceIntersectionPoint.getZ() * frequencyReciprocal;
		
		final float x1 = SimplexNoiseF.fractionalBrownianMotionXYZ(x0, y0, z0, frequency, 0.5F, surfaceNormalS.getX() - 0.25F, surfaceNormalS.getX() + 0.25F, 16) * scale;
		final float y1 = SimplexNoiseF.fractionalBrownianMotionXYZ(y0, z0, x0, frequency, 0.5F, surfaceNormalS.getY() - 0.25F, surfaceNormalS.getY() + 0.25F, 16) * scale;
		final float z1 = SimplexNoiseF.fractionalBrownianMotionXYZ(z0, x0, y0, frequency, 0.5F, surfaceNormalS.getZ() - 0.25F, surfaceNormalS.getZ() + 0.25F, 16) * scale;
		
		intersection.setSurfaceNormalS(Vector3F.normalize(new Vector3F(x1, y1, z1)));
	}
}