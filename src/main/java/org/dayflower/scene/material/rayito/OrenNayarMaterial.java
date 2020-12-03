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

import org.dayflower.geometry.AngleF;
import org.dayflower.image.Color3F;
import org.dayflower.scene.Intersection;
import org.dayflower.scene.Texture;
import org.dayflower.scene.bxdf.rayito.RayitoBXDF;
import org.dayflower.scene.bxdf.rayito.OrenNayarRayitoBRDF;
import org.dayflower.scene.bxdf.rayito.RayitoBSDF;
import org.dayflower.scene.texture.ConstantTexture;

/**
 * An {@code OrenNayarMaterial} is an implementation of {@link RayitoMaterial} that uses an {@link OrenNayarRayitoBRDF} instance.
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class OrenNayarMaterial implements RayitoMaterial {
	/**
	 * The name of this {@code OrenNayarMaterial} class.
	 */
	public static final String NAME = "Rayito - Matte";
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final RayitoBXDF selectedBXDF;
	private final Texture textureAlbedo;
	private final Texture textureEmittance;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code OrenNayarMaterial} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new OrenNayarMaterial(AngleF.degrees(20.0F));
	 * }
	 * </pre>
	 */
	public OrenNayarMaterial() {
		this(AngleF.degrees(20.0F));
	}
	
	/**
	 * Constructs a new {@code OrenNayarMaterial} instance.
	 * <p>
	 * If {@code angle} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new OrenNayarMaterial(angle, Color3F.GRAY);
	 * }
	 * </pre>
	 * 
	 * @param angle an {@link AngleF} instance
	 * @throws NullPointerException thrown if, and only if, {@code angle} is {@code null}
	 */
	public OrenNayarMaterial(final AngleF angle) {
		this(angle, Color3F.GRAY);
	}
	
	/**
	 * Constructs a new {@code OrenNayarMaterial} instance.
	 * <p>
	 * If either {@code angle} or {@code colorAlbedo} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new OrenNayarMaterial(angle, colorAlbedo, Color3F.BLACK);
	 * }
	 * </pre>
	 * 
	 * @param angle an {@link AngleF} instance
	 * @param colorAlbedo a {@link Color3F} instance with the albedo color
	 * @throws NullPointerException thrown if, and only if, either {@code angle} or {@code colorAlbedo} are {@code null}
	 */
	public OrenNayarMaterial(final AngleF angle, final Color3F colorAlbedo) {
		this(angle, colorAlbedo, Color3F.BLACK);
	}
	
	/**
	 * Constructs a new {@code OrenNayarMaterial} instance.
	 * <p>
	 * If either {@code angle}, {@code colorAlbedo} or {@code colorEmittance} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new OrenNayarMaterial(angle, new ConstantTexture(colorAlbedo), new ConstantTexture(colorEmittance));
	 * }
	 * </pre>
	 * 
	 * @param angle an {@link AngleF} instance
	 * @param colorAlbedo a {@link Color3F} instance with the albedo color
	 * @param colorEmittance a {@code Color3F} instance with the emittance
	 * @throws NullPointerException thrown if, and only if, either {@code angle}, {@code colorAlbedo} or {@code colorEmittance} are {@code null}
	 */
	public OrenNayarMaterial(final AngleF angle, final Color3F colorAlbedo, final Color3F colorEmittance) {
		this(angle, new ConstantTexture(colorAlbedo), new ConstantTexture(colorEmittance));
	}
	
	/**
	 * Constructs a new {@code OrenNayarMaterial} instance.
	 * <p>
	 * If either {@code angle} or {@code textureAlbedo} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new OrenNayarMaterial(angle, textureAlbedo, ConstantTexture.BLACK);
	 * }
	 * </pre>
	 * 
	 * @param angle an {@link AngleF} instance
	 * @param textureAlbedo a {@link Texture} instance with the albedo color
	 * @throws NullPointerException thrown if, and only if, either {@code angle} or {@code textureAlbedo} are {@code null}
	 */
	public OrenNayarMaterial(final AngleF angle, final Texture textureAlbedo) {
		this(angle, textureAlbedo, ConstantTexture.BLACK);
	}
	
	/**
	 * Constructs a new {@code OrenNayarMaterial} instance.
	 * <p>
	 * If either {@code angle}, {@code textureAlbedo} or {@code textureEmittance} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param angle an {@link AngleF} instance
	 * @param textureAlbedo a {@link Texture} instance with the albedo color
	 * @param textureEmittance a {@code Texture} instance with the emittance
	 * @throws NullPointerException thrown if, and only if, either {@code angle}, {@code textureAlbedo} or {@code textureEmittance} are {@code null}
	 */
	public OrenNayarMaterial(final AngleF angle, final Texture textureAlbedo, final Texture textureEmittance) {
		this.selectedBXDF = new OrenNayarRayitoBRDF(angle);
		this.textureAlbedo = Objects.requireNonNull(textureAlbedo, "textureAlbedo == null");
		this.textureEmittance = Objects.requireNonNull(textureEmittance, "textureEmittance == null");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link Color3F} instance with the emittance of this {@code OrenNayarMaterial} instance at {@code intersection}.
	 * <p>
	 * If {@code intersection} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param intersection an {@link Intersection} instance
	 * @return a {@code Color3F} instance with the emittance of this {@code OrenNayarMaterial} instance at {@code intersection}
	 * @throws NullPointerException thrown if, and only if, {@code intersection} is {@code null}
	 */
	@Override
	public Color3F emittance(final Intersection intersection) {
		return this.textureEmittance.getColorRGB(intersection);
	}
	
	/**
	 * Returns a {@link RayitoBSDF} instance with information about this {@code OrenNayarMaterial} instance at {@code intersection}.
	 * <p>
	 * If {@code intersection} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param intersection an {@link Intersection} instance
	 * @return a {@code RayitoBSDF} instance with information about this {@code OrenNayarMaterial} instance at {@code intersection}
	 * @throws NullPointerException thrown if, and only if, {@code intersection} is {@code null}
	 */
	@Override
	public RayitoBSDF evaluate(final Intersection intersection) {
		return new RayitoBSDF(this.textureAlbedo.getColorRGB(intersection), this.selectedBXDF);
	}
	
	/**
	 * Returns a {@code String} with the name of this {@code OrenNayarMaterial} instance.
	 * 
	 * @return a {@code String} with the name of this {@code OrenNayarMaterial} instance
	 */
	@Override
	public String getName() {
		return NAME;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code OrenNayarMaterial} instance.
	 * 
	 * @return a {@code String} representation of this {@code OrenNayarMaterial} instance
	 */
	@Override
	public String toString() {
		return "new OrenNayarMaterial(...)";
	}
	
	/**
	 * Compares {@code object} to this {@code OrenNayarMaterial} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code OrenNayarMaterial}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code OrenNayarMaterial} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code OrenNayarMaterial}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof OrenNayarMaterial)) {
			return false;
		} else if(!Objects.equals(this.selectedBXDF, OrenNayarMaterial.class.cast(object).selectedBXDF)) {
			return false;
		} else if(!Objects.equals(this.textureAlbedo, OrenNayarMaterial.class.cast(object).textureAlbedo)) {
			return false;
		} else if(!Objects.equals(this.textureEmittance, OrenNayarMaterial.class.cast(object).textureEmittance)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns a hash code for this {@code OrenNayarMaterial} instance.
	 * 
	 * @return a hash code for this {@code OrenNayarMaterial} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.selectedBXDF, this.textureAlbedo, this.textureEmittance);
	}
}