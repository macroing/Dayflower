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
package org.dayflower.scene.material.rayito;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

import org.dayflower.image.Color3F;
import org.dayflower.scene.BSSRDF;
import org.dayflower.scene.Intersection;
import org.dayflower.scene.TransportMode;
import org.dayflower.scene.bxdf.rayito.LambertianRayitoBRDF;
import org.dayflower.scene.bxdf.rayito.RayitoBSDF;
import org.dayflower.scene.texture.ConstantTexture;
import org.dayflower.scene.texture.Texture;

/**
 * A {@code MatteRayitoMaterial} is an implementation of {@link RayitoMaterial} and is used for matte surfaces.
 * <p>
 * This class is immutable and thread-safe as long as all {@link Texture} instances are.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class MatteRayitoMaterial implements RayitoMaterial {
	/**
	 * The name of this {@code MatteRayitoMaterial} class.
	 */
	public static final String NAME = "Rayito - Matte";
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final Texture textureAlbedo;
	private final Texture textureEmittance;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code MatteRayitoMaterial} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new MatteRayitoMaterial(Color3F.GRAY);
	 * }
	 * </pre>
	 */
	public MatteRayitoMaterial() {
		this(Color3F.GRAY);
	}
	
	/**
	 * Constructs a new {@code MatteRayitoMaterial} instance.
	 * <p>
	 * If {@code colorAlbedo} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new MatteRayitoMaterial(colorAlbedo, Color3F.BLACK);
	 * }
	 * </pre>
	 * 
	 * @param colorAlbedo a {@link Color3F} instance with the albedo color
	 * @throws NullPointerException thrown if, and only if, {@code colorAlbedo} is {@code null}
	 */
	public MatteRayitoMaterial(final Color3F colorAlbedo) {
		this(colorAlbedo, Color3F.BLACK);
	}
	
	/**
	 * Constructs a new {@code MatteRayitoMaterial} instance.
	 * <p>
	 * If either {@code colorAlbedo} or {@code colorEmittance} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new MatteRayitoMaterial(new ConstantTexture(colorAlbedo), new ConstantTexture(colorEmittance));
	 * }
	 * </pre>
	 * 
	 * @param colorAlbedo a {@link Color3F} instance with the albedo color
	 * @param colorEmittance a {@code Color3F} instance with the emittance
	 * @throws NullPointerException thrown if, and only if, either {@code colorAlbedo} or {@code colorEmittance} are {@code null}
	 */
	public MatteRayitoMaterial(final Color3F colorAlbedo, final Color3F colorEmittance) {
		this(new ConstantTexture(colorAlbedo), new ConstantTexture(colorEmittance));
	}
	
	/**
	 * Constructs a new {@code MatteRayitoMaterial} instance.
	 * <p>
	 * If {@code textureAlbedo} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new MatteRayitoMaterial(textureAlbedo, ConstantTexture.BLACK);
	 * }
	 * </pre>
	 * 
	 * @param textureAlbedo a {@link Texture} instance with the albedo color
	 * @throws NullPointerException thrown if, and only if, {@code textureAlbedo} is {@code null}
	 */
	public MatteRayitoMaterial(final Texture textureAlbedo) {
		this(textureAlbedo, ConstantTexture.BLACK);
	}
	
	/**
	 * Constructs a new {@code MatteRayitoMaterial} instance.
	 * <p>
	 * If either {@code textureAlbedo} or {@code textureEmittance} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param textureAlbedo a {@link Texture} instance with the albedo color
	 * @param textureEmittance a {@code Texture} instance with the emittance
	 * @throws NullPointerException thrown if, and only if, either {@code textureAlbedo} or {@code textureEmittance} are {@code null}
	 */
	public MatteRayitoMaterial(final Texture textureAlbedo, final Texture textureEmittance) {
		this.textureAlbedo = Objects.requireNonNull(textureAlbedo, "textureAlbedo == null");
		this.textureEmittance = Objects.requireNonNull(textureEmittance, "textureEmittance == null");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link Color3F} instance with the emittance of this {@code MatteRayitoMaterial} instance at {@code intersection}.
	 * <p>
	 * If {@code intersection} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param intersection an {@link Intersection} instance
	 * @return a {@code Color3F} instance with the emittance of this {@code MatteRayitoMaterial} instance at {@code intersection}
	 * @throws NullPointerException thrown if, and only if, {@code intersection} is {@code null}
	 */
	@Override
	public Color3F emittance(final Intersection intersection) {
		return this.textureEmittance.getColorRGB(intersection);
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
	 * Computes the {@link RayitoBSDF} at {@code intersection}.
	 * <p>
	 * Returns an optional {@code RayitoBSDF} instance.
	 * <p>
	 * If either {@code intersection} or {@code transportMode} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param intersection the {@link Intersection} to compute the {@code RayitoBSDF} for
	 * @param transportMode the {@link TransportMode} to use
	 * @param isAllowingMultipleLobes {@code true} if, and only if, multiple lobes are allowed, {@code false} otherwise
	 * @return an optional {@code RayitoBSDF} instance
	 * @throws NullPointerException thrown if, and only if, either {@code intersection} or {@code transportMode} are {@code null}
	 */
	@Override
	public Optional<RayitoBSDF> computeBSDF(final Intersection intersection, final TransportMode transportMode, final boolean isAllowingMultipleLobes) {
		Objects.requireNonNull(intersection, "intersection == null");
		Objects.requireNonNull(transportMode, "transportMode == null");
		
		return Optional.of(new RayitoBSDF(intersection, Arrays.asList(new LambertianRayitoBRDF(this.textureAlbedo.getColorRGB(intersection)))));
	}
	
	/**
	 * Returns a {@code String} with the name of this {@code MatteRayitoMaterial} instance.
	 * 
	 * @return a {@code String} with the name of this {@code MatteRayitoMaterial} instance
	 */
	@Override
	public String getName() {
		return NAME;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code MatteRayitoMaterial} instance.
	 * 
	 * @return a {@code String} representation of this {@code MatteRayitoMaterial} instance
	 */
	@Override
	public String toString() {
		return "new MatteRayitoMaterial(...)";
	}
	
	/**
	 * Compares {@code object} to this {@code MatteRayitoMaterial} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code MatteRayitoMaterial}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code MatteRayitoMaterial} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code MatteRayitoMaterial}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof MatteRayitoMaterial)) {
			return false;
		} else if(!Objects.equals(this.textureAlbedo, MatteRayitoMaterial.class.cast(object).textureAlbedo)) {
			return false;
		} else if(!Objects.equals(this.textureEmittance, MatteRayitoMaterial.class.cast(object).textureEmittance)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns a hash code for this {@code MatteRayitoMaterial} instance.
	 * 
	 * @return a hash code for this {@code MatteRayitoMaterial} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.textureAlbedo, this.textureEmittance);
	}
}