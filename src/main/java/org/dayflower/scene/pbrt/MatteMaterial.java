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

import static org.dayflower.util.Floats.isZero;
import static org.dayflower.util.Floats.saturate;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

import org.dayflower.geometry.AngleF;
import org.dayflower.image.Color3F;
import org.dayflower.scene.Intersection;
import org.dayflower.scene.Texture;
import org.dayflower.scene.texture.ConstantTexture;

/**
 * A {@code MatteMaterial} is an implementation of {@link PBRTMaterial} and is used for matte surfaces.
 * <p>
 * This class is immutable and thread-safe as long as all {@link Texture} instances are.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class MatteMaterial implements PBRTMaterial {
	private final Texture textureAngle;
	private final Texture textureDiffuse;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code MatteMaterial} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new MatteMaterial(new ConstantTexture(Color3F.BLACK), new ConstantTexture(Color3F.GRAY));
	 * }
	 * </pre>
	 */
	public MatteMaterial() {
		this(new ConstantTexture(Color3F.BLACK), new ConstantTexture(Color3F.GRAY));
	}
	
	/**
	 * Constructs a new {@code MatteMaterial} instance.
	 * <p>
	 * If either {@code textureAngle} or {@code textureDiffuse} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param textureAngle a {@link Texture} instance used for the angle
	 * @param textureDiffuse a {@code Texture} instance used for the diffuse component
	 * @throws NullPointerException thrown if, and only if, either {@code textureAngle} or {@code textureDiffuse} are {@code null}
	 */
	public MatteMaterial(final Texture textureAngle, final Texture textureDiffuse) {
		this.textureAngle = Objects.requireNonNull(textureAngle, "textureAngle == null");
		this.textureDiffuse = Objects.requireNonNull(textureDiffuse, "textureDiffuse == null");
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
		
		final Color3F colorAngle = this.textureAngle.getColorRGB(intersection);
		final Color3F colorDiffuse = Color3F.saturate(this.textureDiffuse.getColorRGB(intersection), 0.0F, Float.MAX_VALUE);
		
		final AngleF angle = AngleF.degrees(saturate(colorAngle.average(), 0.0F, 90.0F));
		
		if(colorDiffuse.isBlack()) {
			return Optional.empty();
		}
		
		if(isZero(angle.getDegrees())) {
			return Optional.of(new BSDF(intersection, Arrays.asList(new LambertianBRDF(colorDiffuse))));
		}
		
		return Optional.of(new BSDF(intersection, Arrays.asList(new OrenNayarBRDF(angle, colorDiffuse))));
	}
	
	/**
	 * Computes the {@link BSSRDF} at {@code intersection}.
	 * <p>
	 * Returns an optional {@code BSSRDF} instance.
	 * <p>
	 * If either {@code intersection} or {@code transportMode} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param intersection the {@link Intersection} to compute the {@code BSSRDF} for
	 * @param transportMode the {@link TransportMode} to use
	 * @param isAllowingMultipleLobes {@code true} if, and only if, multiple lobes are allowed, {@code false} otherwise
	 * @return an optional {@code BSSRDF} instance
	 * @throws NullPointerException thrown if, and only if, either {@code intersection} or {@code transportMode} are {@code null}
	 */
	@Override
	public Optional<BSSRDF> computeBSSRDF(final Intersection intersection, final TransportMode transportMode, final boolean isAllowingMultipleLobes) {
		Objects.requireNonNull(intersection, "intersection == null");
		Objects.requireNonNull(transportMode, "transportMode == null");
		
		return Optional.empty();
	}
	
	/**
	 * Returns a {@code String} representation of this {@code MatteMaterial} instance.
	 * 
	 * @return a {@code String} representation of this {@code MatteMaterial} instance
	 */
	@Override
	public String toString() {
		return String.format("new MatteMaterial(%s, %s)", this.textureAngle, this.textureDiffuse);
	}
	
	/**
	 * Compares {@code object} to this {@code MatteMaterial} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code MatteMaterial}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code MatteMaterial} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code MatteMaterial}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof MatteMaterial)) {
			return false;
		} else if(!Objects.equals(this.textureAngle, MatteMaterial.class.cast(object).textureAngle)) {
			return false;
		} else if(!Objects.equals(this.textureDiffuse, MatteMaterial.class.cast(object).textureDiffuse)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns a hash code for this {@code MatteMaterial} instance.
	 * 
	 * @return a hash code for this {@code MatteMaterial} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.textureAngle, this.textureDiffuse);
	}
}