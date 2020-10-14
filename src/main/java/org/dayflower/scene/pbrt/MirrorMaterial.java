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
package org.dayflower.scene.pbrt;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

import org.dayflower.image.Color3F;
import org.dayflower.scene.Intersection;
import org.dayflower.scene.Texture;
import org.dayflower.scene.texture.ConstantTexture;

/**
 * A {@code MirrorMaterial} is an implementation of {@link PBRTMaterial} that represents a mirror.
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class MirrorMaterial implements PBRTMaterial {
	private final Texture textureReflectanceScale;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code MirrorMaterial} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new MirrorMaterial(new ConstantTexture(new Color3F(0.9F)));
	 * }
	 * </pre>
	 */
	public MirrorMaterial() {
		this(new ConstantTexture(new Color3F(0.9F)));
	}
	
	/**
	 * Constructs a new {@code MirrorMaterial} instance.
	 * <p>
	 * If {@code textureReflectanceScale} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param textureReflectanceScale a {@link Texture} instance used for the reflectance scale
	 * @throws NullPointerException thrown if, and only if, {@code textureReflectanceScale} is {@code null}
	 */
	public MirrorMaterial(final Texture textureReflectanceScale) {
		this.textureReflectanceScale = Objects.requireNonNull(textureReflectanceScale, "textureReflectanceScale == null");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Computes the {@link BSDF} at {@code intersection}.
	 * <p>
	 * Returns an optional {@code BSDF} instance.
	 * <p>
	 * If either {@code intersection} or {@code transportMode} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param intersection the {@link Intersection} to compute the {@code BSDF} for
	 * @param transportMode the {@link TransportMode} to use
	 * @param isAllowingMultipleLobes {@code true} if, and only if, multiple lobes are allowed, {@code false} otherwise
	 * @return an optional {@code BSDF} instance
	 * @throws NullPointerException thrown if, and only if, either {@code intersection} or {@code transportMode} are {@code null}
	 */
	@Override
	public Optional<BSDF> computeBSDF(final Intersection intersection, final TransportMode transportMode, final boolean isAllowingMultipleLobes) {
		Objects.requireNonNull(intersection, "intersection == null");
		Objects.requireNonNull(transportMode, "transportMode == null");
		
		final Color3F colorReflectanceScale = Color3F.saturate(this.textureReflectanceScale.getColor(intersection), 0.0F, Float.MAX_VALUE);
		
		if(!colorReflectanceScale.isBlack()) {
			return Optional.of(new BSDF(intersection, Arrays.asList(new SpecularReflectionBRDF(colorReflectanceScale, new ConstantFresnel()))));
		}
		
		return Optional.empty();
	}
	
	/**
	 * Returns a {@code String} representation of this {@code MirrorMaterial} instance.
	 * 
	 * @return a {@code String} representation of this {@code MirrorMaterial} instance
	 */
	@Override
	public String toString() {
		return String.format("new MirrorMaterial(%s)", this.textureReflectanceScale);
	}
	
	/**
	 * Compares {@code object} to this {@code MirrorMaterial} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code MirrorMaterial}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code MirrorMaterial} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code MirrorMaterial}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof MirrorMaterial)) {
			return false;
		} else if(!Objects.equals(this.textureReflectanceScale, MirrorMaterial.class.cast(object).textureReflectanceScale)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns a hash code for this {@code MirrorMaterial} instance.
	 * 
	 * @return a hash code for this {@code MirrorMaterial} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.textureReflectanceScale);
	}
}