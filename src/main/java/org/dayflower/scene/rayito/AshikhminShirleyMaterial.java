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
package org.dayflower.scene.rayito;

import static org.dayflower.util.Floats.equal;

import java.util.Objects;

import org.dayflower.image.Color3F;
import org.dayflower.scene.Intersection;
import org.dayflower.scene.Texture;
import org.dayflower.scene.texture.ConstantTexture;

/**
 * An {@code AshikhminShirleyMaterial} is an implementation of {@link RayitoMaterial} that uses an {@link AshikhminShirleyBRDF} instance.
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class AshikhminShirleyMaterial implements RayitoMaterial {
	/**
	 * The name of this {@code AshikhminShirleyMaterial} class.
	 */
	public static final String NAME = "Rayito - Metal";
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final BXDF selectedBXDF;
	private final Texture textureAlbedo;
	private final Texture textureEmittance;
	private final float selectedBXDFWeight;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code AshikhminShirleyMaterial} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new AshikhminShirleyMaterial(Color3F.GRAY);
	 * }
	 * </pre>
	 */
	public AshikhminShirleyMaterial() {
		this(Color3F.GRAY);
	}
	
	/**
	 * Constructs a new {@code AshikhminShirleyMaterial} instance.
	 * <p>
	 * If {@code colorAlbedo} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new AshikhminShirleyMaterial(colorAlbedo, Color3F.BLACK);
	 * }
	 * </pre>
	 * 
	 * @param colorAlbedo a {@link Color3F} instance with the albedo color
	 * @throws NullPointerException thrown if, and only if, {@code colorAlbedo} is {@code null}
	 */
	public AshikhminShirleyMaterial(final Color3F colorAlbedo) {
		this(colorAlbedo, Color3F.BLACK);
	}
	
	/**
	 * Constructs a new {@code AshikhminShirleyMaterial} instance.
	 * <p>
	 * If either {@code colorAlbedo} or {@code colorEmittance} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new AshikhminShirleyMaterial(colorAlbedo, colorEmittance, 0.05F);
	 * }
	 * </pre>
	 * 
	 * @param colorAlbedo a {@link Color3F} instance with the albedo color
	 * @param colorEmittance a {@code Color3F} instance with the emittance
	 * @throws NullPointerException thrown if, and only if, either {@code colorAlbedo} or {@code colorEmittance} are {@code null}
	 */
	public AshikhminShirleyMaterial(final Color3F colorAlbedo, final Color3F colorEmittance) {
		this(colorAlbedo, colorEmittance, 0.05F);
	}
	
	/**
	 * Constructs a new {@code AshikhminShirleyMaterial} instance.
	 * <p>
	 * If either {@code colorAlbedo} or {@code colorEmittance} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new AshikhminShirleyMaterial(new ConstantTexture(colorAlbedo), new ConstantTexture(colorEmittance), roughness);
	 * }
	 * </pre>
	 * 
	 * @param colorAlbedo a {@link Color3F} instance with the albedo color
	 * @param colorEmittance a {@code Color3F} instance with the emittance
	 * @param roughness the roughness to use
	 * @throws NullPointerException thrown if, and only if, either {@code colorAlbedo} or {@code colorEmittance} are {@code null}
	 */
	public AshikhminShirleyMaterial(final Color3F colorAlbedo, final Color3F colorEmittance, final float roughness) {
		this(new ConstantTexture(colorAlbedo), new ConstantTexture(colorEmittance), roughness);
	}
	
	/**
	 * Constructs a new {@code AshikhminShirleyMaterial} instance.
	 * <p>
	 * If {@code textureAlbedo} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new AshikhminShirleyMaterial(textureAlbedo, ConstantTexture.BLACK);
	 * }
	 * </pre>
	 * 
	 * @param textureAlbedo a {@link Texture} instance with the albedo color
	 * @throws NullPointerException thrown if, and only if, {@code textureAlbedo} is {@code null}
	 */
	public AshikhminShirleyMaterial(final Texture textureAlbedo) {
		this(textureAlbedo, ConstantTexture.BLACK);
	}
	
	/**
	 * Constructs a new {@code AshikhminShirleyMaterial} instance.
	 * <p>
	 * If either {@code textureAlbedo} or {@code textureEmittance} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new AshikhminShirleyMaterial(textureAlbedo, textureEmittance, 0.05F);
	 * }
	 * </pre>
	 * 
	 * @param textureAlbedo a {@link Texture} instance with the albedo color
	 * @param textureEmittance a {@code Texture} instance with the emittance
	 * @throws NullPointerException thrown if, and only if, either {@code textureAlbedo} or {@code textureEmittance} are {@code null}
	 */
	public AshikhminShirleyMaterial(final Texture textureAlbedo, final Texture textureEmittance) {
		this(textureAlbedo, textureEmittance, 0.05F);
	}
	
	/**
	 * Constructs a new {@code AshikhminShirleyMaterial} instance.
	 * <p>
	 * If either {@code textureAlbedo} or {@code textureEmittance} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param textureAlbedo a {@link Texture} instance with the albedo color
	 * @param textureEmittance a {@code Texture} instance with the emittance
	 * @param roughness the roughness to use
	 * @throws NullPointerException thrown if, and only if, either {@code textureAlbedo} or {@code textureEmittance} are {@code null}
	 */
	public AshikhminShirleyMaterial(final Texture textureAlbedo, final Texture textureEmittance, final float roughness) {
		this.selectedBXDF = new AshikhminShirleyBRDF(roughness);
		this.textureAlbedo = Objects.requireNonNull(textureAlbedo, "textureAlbedo == null");
		this.textureEmittance = Objects.requireNonNull(textureEmittance, "textureEmittance == null");
		this.selectedBXDFWeight = 1.0F;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link Color3F} instance with the emittance of this {@code AshikhminShirleyMaterial} instance at {@code intersection}.
	 * <p>
	 * If {@code intersection} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param intersection an {@link Intersection} instance
	 * @return a {@code Color3F} instance with the emittance of this {@code AshikhminShirleyMaterial} instance at {@code intersection}
	 * @throws NullPointerException thrown if, and only if, {@code intersection} is {@code null}
	 */
	@Override
	public Color3F emittance(final Intersection intersection) {
		return this.textureEmittance.getColorRGB(intersection);
	}
	
	/**
	 * Returns a {@link MaterialResult} instance with information about this {@code AshikhminShirleyMaterial} instance at {@code intersection}.
	 * <p>
	 * If {@code intersection} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param intersection an {@link Intersection} instance
	 * @return a {@code MaterialResult} instance with information about this {@code AshikhminShirleyMaterial} instance at {@code intersection}
	 * @throws NullPointerException thrown if, and only if, {@code intersection} is {@code null}
	 */
	@Override
	public MaterialResult evaluate(final Intersection intersection) {
		return new MaterialResult(this.textureAlbedo.getColorRGB(intersection), this.selectedBXDF, this.selectedBXDFWeight);
	}
	
	/**
	 * Returns a {@code String} with the name of this {@code AshikhminShirleyMaterial} instance.
	 * 
	 * @return a {@code String} with the name of this {@code AshikhminShirleyMaterial} instance
	 */
	@Override
	public String getName() {
		return NAME;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code AshikhminShirleyMaterial} instance.
	 * 
	 * @return a {@code String} representation of this {@code AshikhminShirleyMaterial} instance
	 */
	@Override
	public String toString() {
		return "new AshikhminShirleyMaterial(...)";
	}
	
	/**
	 * Compares {@code object} to this {@code AshikhminShirleyMaterial} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code AshikhminShirleyMaterial}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code AshikhminShirleyMaterial} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code AshikhminShirleyMaterial}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof AshikhminShirleyMaterial)) {
			return false;
		} else if(!Objects.equals(this.selectedBXDF, AshikhminShirleyMaterial.class.cast(object).selectedBXDF)) {
			return false;
		} else if(!Objects.equals(this.textureAlbedo, AshikhminShirleyMaterial.class.cast(object).textureAlbedo)) {
			return false;
		} else if(!Objects.equals(this.textureEmittance, AshikhminShirleyMaterial.class.cast(object).textureEmittance)) {
			return false;
		} else if(!equal(this.selectedBXDFWeight, AshikhminShirleyMaterial.class.cast(object).selectedBXDFWeight)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns a hash code for this {@code AshikhminShirleyMaterial} instance.
	 * 
	 * @return a hash code for this {@code AshikhminShirleyMaterial} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.selectedBXDF, this.textureAlbedo, this.textureEmittance, Float.valueOf(this.selectedBXDFWeight));
	}
}