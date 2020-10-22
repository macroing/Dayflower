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

import static org.dayflower.util.Floats.PI;
import static org.dayflower.util.Floats.abs;
import static org.dayflower.util.Floats.equal;
import static org.dayflower.util.Floats.perlinTurbulenceXYZ;
import static org.dayflower.util.Floats.sin;

import java.util.Objects;

import org.dayflower.geometry.Point3F;
import org.dayflower.image.Color3F;
import org.dayflower.scene.Intersection;
import org.dayflower.scene.Texture;

/**
 * A {@code MarbleTexture} is a {@link Texture} implementation that returns a {@link Color3F} instance by alternating between three {@code Color3F} instances in a marble pattern.
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class MarbleTexture implements Texture {
	private final Color3F colorA;
	private final Color3F colorB;
	private final Color3F colorC;
	private final float frequency;
	private final float scale;
	private final float stripes;
	private final int octaves;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code MarbleTexture} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new MarbleTexture(new Color3F(0.8F, 0.8F, 0.8F), new Color3F(0.4F, 0.2F, 0.1F), new Color3F(0.06F, 0.04F, 0.02F));
	 * }
	 * </pre>
	 */
	public MarbleTexture() {
		this(new Color3F(0.8F, 0.8F, 0.8F), new Color3F(0.4F, 0.2F, 0.1F), new Color3F(0.06F, 0.04F, 0.02F));
	}
	
	/**
	 * Constructs a new {@code MarbleTexture} instance.
	 * <p>
	 * If either {@code colorA}, {@code colorB} or {@code colorC} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new MarbleTexture(colorA, colorB, colorC, 5.0F, 0.15F, 8);
	 * }
	 * </pre>
	 * 
	 * @param colorA one of the {@link Color3F} instances to use
	 * @param colorB one of the {@code Color3F} instances to use
	 * @param colorC one of the {@code Color3F} instances to use
	 * @throws NullPointerException thrown if, and only if, either {@code colorA}, {@code colorB} or {@code colorC} are {@code null}
	 */
	public MarbleTexture(final Color3F colorA, final Color3F colorB, final Color3F colorC) {
		this(colorA, colorB, colorC, 5.0F, 0.15F, 8);
	}
	
	/**
	 * Constructs a new {@code MarbleTexture} instance.
	 * <p>
	 * If either {@code colorA}, {@code colorB} or {@code colorC} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param colorA one of the {@link Color3F} instances to use
	 * @param colorB one of the {@code Color3F} instances to use
	 * @param colorC one of the {@code Color3F} instances to use
	 * @param scale the scale to use
	 * @param stripes the stripes to use
	 * @param octaves the octaves to use
	 * @throws NullPointerException thrown if, and only if, either {@code colorA}, {@code colorB} or {@code colorC} are {@code null}
	 */
	public MarbleTexture(final Color3F colorA, final Color3F colorB, final Color3F colorC, final float scale, final float stripes, final int octaves) {
		this.colorA = Objects.requireNonNull(colorA, "colorA == null");
		this.colorB = Objects.requireNonNull(colorB, "colorB == null");
		this.colorC = Objects.requireNonNull(colorC, "colorC == null");
		this.frequency = PI * stripes;
		this.scale = scale;
		this.stripes = stripes;
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
		final Point3F surfaceIntersectionPoint = intersection.getSurfaceIntersectionWorldSpace().getSurfaceIntersectionPoint();
		
		final float x = surfaceIntersectionPoint.getX() * this.frequency;
		final float y = surfaceIntersectionPoint.getY() * this.frequency;
		final float z = surfaceIntersectionPoint.getZ() * this.frequency;
		final float r = this.scale * perlinTurbulenceXYZ(x, y, z, this.octaves);
		final float s = 2.0F * abs(sin(x + r));
		final float t = s < 1.0F ? s : s - 1.0F;
		
		final Color3F colorA = s < 1.0F ? this.colorC : this.colorB;
		final Color3F colorB = s < 1.0F ? this.colorB : this.colorA;
		
		return Color3F.blend(colorA, colorB, t);
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
	 * Returns a {@code String} representation of this {@code MarbleTexture} instance.
	 * 
	 * @return a {@code String} representation of this {@code MarbleTexture} instance
	 */
	@Override
	public String toString() {
		return String.format("new MarbleTexture(%s, %s, %s, %+.10f, %+.10f, %d", this.colorA, this.colorB, this.colorC, Float.valueOf(this.scale), Float.valueOf(this.stripes), Integer.valueOf(this.octaves));
	}
	
	/**
	 * Compares {@code object} to this {@code MarbleTexture} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code MarbleTexture}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code MarbleTexture} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code MarbleTexture}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof MarbleTexture)) {
			return false;
		} else if(!Objects.equals(this.colorA, MarbleTexture.class.cast(object).colorA)) {
			return false;
		} else if(!Objects.equals(this.colorB, MarbleTexture.class.cast(object).colorB)) {
			return false;
		} else if(!Objects.equals(this.colorC, MarbleTexture.class.cast(object).colorC)) {
			return false;
		} else if(!equal(this.scale, MarbleTexture.class.cast(object).scale)) {
			return false;
		} else if(!equal(this.stripes, MarbleTexture.class.cast(object).stripes)) {
			return false;
		} else if(this.octaves != MarbleTexture.class.cast(object).octaves) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns a hash code for this {@code MarbleTexture} instance.
	 * 
	 * @return a hash code for this {@code MarbleTexture} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.colorA, this.colorB, this.colorC, Float.valueOf(this.scale), Float.valueOf(this.stripes), Integer.valueOf(this.octaves));
	}
}