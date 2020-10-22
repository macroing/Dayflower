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
package org.dayflower.scene.texture;

import static org.dayflower.util.Floats.equal;
import static org.dayflower.util.Floats.simplexFractionalBrownianMotionXYZ;

import java.util.Objects;

import org.dayflower.geometry.Point3F;
import org.dayflower.image.Color3F;
import org.dayflower.scene.Intersection;
import org.dayflower.scene.Texture;

/**
 * A {@code SimplexFractionalBrownianMotionTexture} is a {@link Texture} implementation that returns a {@link Color3F} instance using a Simplex noise-based fractional Brownian motion (fBm) algorithm.
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class SimplexFractionalBrownianMotionTexture implements Texture {
	private final Color3F color;
	private final float frequency;
	private final float gain;
	private final int octaves;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code SimplexFractionalBrownianMotionTexture} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new SimplexFractionalBrownianMotionTexture(new Color3F(0.75F, 0.5F, 0.75F));
	 * }
	 * </pre>
	 */
	public SimplexFractionalBrownianMotionTexture() {
		this(new Color3F(0.75F, 0.5F, 0.75F));
	}
	
	/**
	 * Constructs a new {@code SimplexFractionalBrownianMotionTexture} instance.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new SimplexFractionalBrownianMotionTexture(color, 5.0F, 0.5F, 16);
	 * }
	 * </pre>
	 * 
	 * @param color the {@link Color3F} to use
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public SimplexFractionalBrownianMotionTexture(final Color3F color) {
		this(color, 5.0F, 0.5F, 16);
	}
	
	/**
	 * Constructs a new {@code SimplexFractionalBrownianMotionTexture} instance.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color the {@link Color3F} to use
	 * @param frequency the frequency to use
	 * @param gain the gain to use
	 * @param octaves the octaves to use
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public SimplexFractionalBrownianMotionTexture(final Color3F color, final float frequency, final float gain, final int octaves) {
		this.color = Objects.requireNonNull(color, "color == null");
		this.frequency = frequency;
		this.gain = gain;
		this.octaves = octaves;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link Color3F} instance representing the color of the surface at {@code intersection} using an RGB-color space.
	 * <p>
	 * If {@code intersection} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param intersection an {@link Intersection} instance
	 * @return a {@code Color3F} instance representing the color of the surface at {@code intersection} using an RGB-color space
	 * @throws NullPointerException thrown if, and only if, {@code intersection} is {@code null}
	 */
	@Override
	public Color3F getColorRGB(final Intersection intersection) {
		final Point3F surfaceIntersectionPoint = intersection.getSurfaceIntersectionObjectSpace().getSurfaceIntersectionPoint();
		
		final float x = surfaceIntersectionPoint.getX();
		final float y = surfaceIntersectionPoint.getY();
		final float z = surfaceIntersectionPoint.getZ();
		
		final float noise = simplexFractionalBrownianMotionXYZ(x, y, z, this.frequency, this.gain, 0.0F, 1.0F, this.octaves);
		
		return Color3F.maximumTo1(Color3F.minimumTo0(Color3F.multiply(this.color, noise)));
	}
	
	/**
	 * Returns a {@link Color3F} instance representing the color of the surface at {@code intersection} using an XYZ-color space.
	 * <p>
	 * If {@code intersection} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param intersection an {@link Intersection} instance
	 * @return a {@code Color3F} instance representing the color of the surface at {@code intersection} using an XYZ-color space
	 * @throws NullPointerException thrown if, and only if, {@code intersection} is {@code null}
	 */
	@Override
	public Color3F getColorXYZ(final Intersection intersection) {
		return Color3F.convertRGBToXYZUsingPBRT(getColorRGB(intersection));
	}
	
	/**
	 * Returns a {@code String} representation of this {@code SimplexFractionalBrownianMotionTexture} instance.
	 * 
	 * @return a {@code String} representation of this {@code SimplexFractionalBrownianMotionTexture} instance
	 */
	@Override
	public String toString() {
		return String.format("new SimplexFractionalBrownianMotionTexture(%s, %+.10f, %+.10f, %d)", this.color, Float.valueOf(this.frequency), Float.valueOf(this.gain), Integer.valueOf(this.octaves));
	}
	
	/**
	 * Compares {@code object} to this {@code SimplexFractionalBrownianMotionTexture} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code SimplexFractionalBrownianMotionTexture}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code SimplexFractionalBrownianMotionTexture} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code SimplexFractionalBrownianMotionTexture}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof SimplexFractionalBrownianMotionTexture)) {
			return false;
		} else if(!Objects.equals(this.color, SimplexFractionalBrownianMotionTexture.class.cast(object).color)) {
			return false;
		} else if(!equal(this.frequency, SimplexFractionalBrownianMotionTexture.class.cast(object).frequency)) {
			return false;
		} else if(!equal(this.gain, SimplexFractionalBrownianMotionTexture.class.cast(object).gain)) {
			return false;
		} else if(this.octaves != SimplexFractionalBrownianMotionTexture.class.cast(object).octaves) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns a hash code for this {@code SimplexFractionalBrownianMotionTexture} instance.
	 * 
	 * @return a hash code for this {@code SimplexFractionalBrownianMotionTexture} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.color, Float.valueOf(this.frequency), Float.valueOf(this.gain), Integer.valueOf(this.octaves));
	}
}