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

import java.util.Objects;

import org.dayflower.image.Color3F;
import org.dayflower.scene.Intersection;
import org.dayflower.scene.Texture;
import org.dayflower.scene.bxdf.rayito.RayitoBXDF;
import org.dayflower.scene.bxdf.rayito.RayitoBSDF;
import org.dayflower.scene.bxdf.rayito.SpecularRayitoBTDF;
import org.dayflower.scene.texture.ConstantTexture;

/**
 * A {@code RefractionMaterial} is an implementation of {@link RayitoMaterial} that uses a {@link SpecularRayitoBTDF} instance.
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class RefractionMaterial implements RayitoMaterial {
	/**
	 * The name of this {@code RefractionMaterial} class.
	 */
	public static final String NAME = "Rayito - Glass";
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final RayitoBXDF selectedBXDF;
	private final Texture textureAlbedo;
	private final Texture textureEmittance;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code RefractionMaterial} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new RefractionMaterial(Color3F.WHITE);
	 * }
	 * </pre>
	 */
	public RefractionMaterial() {
		this(Color3F.WHITE);
	}
	
	/**
	 * Constructs a new {@code RefractionMaterial} instance.
	 * <p>
	 * If {@code colorAlbedo} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new RefractionMaterial(colorAlbedo, Color3F.BLACK);
	 * }
	 * </pre>
	 * 
	 * @param colorAlbedo a {@link Color3F} instance with the albedo color
	 * @throws NullPointerException thrown if, and only if, {@code colorAlbedo} is {@code null}
	 */
	public RefractionMaterial(final Color3F colorAlbedo) {
		this(colorAlbedo, Color3F.BLACK);
	}
	
	/**
	 * Constructs a new {@code RefractionMaterial} instance.
	 * <p>
	 * If either {@code colorAlbedo} or {@code colorEmittance} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new RefractionMaterial(colorAlbedo, colorEmittance, 1.0F, 1.5F);
	 * }
	 * </pre>
	 * 
	 * @param colorAlbedo a {@link Color3F} instance with the albedo color
	 * @param colorEmittance a {@code Color3F} instance with the emittance
	 * @throws NullPointerException thrown if, and only if, either {@code colorAlbedo} or {@code colorEmittance} are {@code null}
	 */
	public RefractionMaterial(final Color3F colorAlbedo, final Color3F colorEmittance) {
		this(colorAlbedo, colorEmittance, 1.0F, 1.5F);
	}
	
	/**
	 * Constructs a new {@code RefractionMaterial} instance.
	 * <p>
	 * If either {@code colorAlbedo} or {@code colorEmittance} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new RefractionMaterial(new ConstantTexture(colorAlbedo), new ConstantTexture(colorEmittance), etaA, etaB);
	 * }
	 * </pre>
	 * 
	 * @param colorAlbedo a {@link Color3F} instance with the albedo color
	 * @param colorEmittance a {@code Color3F} instance with the emittance
	 * @param etaA the index of refraction denoted by {@code A}
	 * @param etaB the index of refraction denoted by {@code B}
	 * @throws NullPointerException thrown if, and only if, either {@code colorAlbedo} or {@code colorEmittance} are {@code null}
	 */
	public RefractionMaterial(final Color3F colorAlbedo, final Color3F colorEmittance, final float etaA, final float etaB) {
		this(new ConstantTexture(colorAlbedo), new ConstantTexture(colorEmittance), etaA, etaB);
	}
	
	/**
	 * Constructs a new {@code RefractionMaterial} instance.
	 * <p>
	 * If {@code textureAlbedo} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new RefractionMaterial(textureAlbedo, ConstantTexture.BLACK);
	 * }
	 * </pre>
	 * 
	 * @param textureAlbedo a {@link Texture} instance with the albedo color
	 * @throws NullPointerException thrown if, and only if, {@code textureAlbedo} is {@code null}
	 */
	public RefractionMaterial(final Texture textureAlbedo) {
		this(textureAlbedo, ConstantTexture.BLACK);
	}
	
	/**
	 * Constructs a new {@code RefractionMaterial} instance.
	 * <p>
	 * If either {@code textureAlbedo} or {@code textureEmittance} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new RefractionMaterial(textureAlbedo, textureEmittance, 1.0F, 1.5F);
	 * }
	 * </pre>
	 * 
	 * @param textureAlbedo a {@link Texture} instance with the albedo color
	 * @param textureEmittance a {@code Texture} instance with the emittance
	 * @throws NullPointerException thrown if, and only if, either {@code textureAlbedo} or {@code textureEmittance} are {@code null}
	 */
	public RefractionMaterial(final Texture textureAlbedo, final Texture textureEmittance) {
		this(textureAlbedo, textureEmittance, 1.0F, 1.5F);
	}
	
	/**
	 * Constructs a new {@code RefractionMaterial} instance.
	 * <p>
	 * If either {@code textureAlbedo} or {@code textureEmittance} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param textureAlbedo a {@link Texture} instance with the albedo color
	 * @param textureEmittance a {@code Texture} instance with the emittance
	 * @param etaA the index of refraction denoted by {@code A}
	 * @param etaB the index of refraction denoted by {@code B}
	 * @throws NullPointerException thrown if, and only if, either {@code textureAlbedo} or {@code textureEmittance} are {@code null}
	 */
	public RefractionMaterial(final Texture textureAlbedo, final Texture textureEmittance, final float etaA, final float etaB) {
		this.selectedBXDF = new SpecularRayitoBTDF(etaA, etaB);
		this.textureAlbedo = Objects.requireNonNull(textureAlbedo, "textureAlbedo == null");
		this.textureEmittance = Objects.requireNonNull(textureEmittance, "textureEmittance == null");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link Color3F} instance with the emittance of this {@code RefractionMaterial} instance at {@code intersection}.
	 * <p>
	 * If {@code intersection} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param intersection an {@link Intersection} instance
	 * @return a {@code Color3F} instance with the emittance of this {@code RefractionMaterial} instance at {@code intersection}
	 * @throws NullPointerException thrown if, and only if, {@code intersection} is {@code null}
	 */
	@Override
	public Color3F emittance(final Intersection intersection) {
		return this.textureEmittance.getColorRGB(intersection);
	}
	
	/**
	 * Returns a {@link RayitoBSDF} instance with information about this {@code RefractionMaterial} instance at {@code intersection}.
	 * <p>
	 * If {@code intersection} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param intersection an {@link Intersection} instance
	 * @return a {@code RayitoBSDF} instance with information about this {@code RefractionMaterial} instance at {@code intersection}
	 * @throws NullPointerException thrown if, and only if, {@code intersection} is {@code null}
	 */
	@Override
	public RayitoBSDF evaluate(final Intersection intersection) {
		return new RayitoBSDF(this.textureAlbedo.getColorRGB(intersection), this.selectedBXDF);
	}
	
	/**
	 * Returns a {@code String} with the name of this {@code RefractionMaterial} instance.
	 * 
	 * @return a {@code String} with the name of this {@code RefractionMaterial} instance
	 */
	@Override
	public String getName() {
		return NAME;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code RefractionMaterial} instance.
	 * 
	 * @return a {@code String} representation of this {@code RefractionMaterial} instance
	 */
	@Override
	public String toString() {
		return "new RefractionMaterial(...)";
	}
	
	/**
	 * Compares {@code object} to this {@code RefractionMaterial} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code RefractionMaterial}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code RefractionMaterial} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code RefractionMaterial}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof RefractionMaterial)) {
			return false;
		} else if(!Objects.equals(this.selectedBXDF, RefractionMaterial.class.cast(object).selectedBXDF)) {
			return false;
		} else if(!Objects.equals(this.textureAlbedo, RefractionMaterial.class.cast(object).textureAlbedo)) {
			return false;
		} else if(!Objects.equals(this.textureEmittance, RefractionMaterial.class.cast(object).textureEmittance)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns a hash code for this {@code RefractionMaterial} instance.
	 * 
	 * @return a hash code for this {@code RefractionMaterial} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.selectedBXDF, this.textureAlbedo, this.textureEmittance);
	}
}