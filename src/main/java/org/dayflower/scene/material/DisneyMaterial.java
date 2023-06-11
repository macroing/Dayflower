/**
 * Copyright 2014 - 2023 J&#246;rgen Lundgren
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
package org.dayflower.scene.material;

import static org.dayflower.utility.Floats.MAX_VALUE;
import static org.dayflower.utility.Floats.lerp;
import static org.dayflower.utility.Floats.max;
import static org.dayflower.utility.Floats.sqrt;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.dayflower.scene.BSDF;
import org.dayflower.scene.BSSRDF;
import org.dayflower.scene.BXDF;
import org.dayflower.scene.Intersection;
import org.dayflower.scene.Material;
import org.dayflower.scene.TransportMode;
import org.dayflower.scene.bxdf.DisneyClearCoatBRDF;
import org.dayflower.scene.bxdf.DisneyDiffuseBRDF;
import org.dayflower.scene.bxdf.DisneyFakeSSBRDF;
import org.dayflower.scene.bxdf.DisneyRetroBRDF;
import org.dayflower.scene.bxdf.DisneySheenBRDF;
import org.dayflower.scene.bxdf.LambertianBTDF;
import org.dayflower.scene.bxdf.SpecularBTDF;
import org.dayflower.scene.bxdf.TorranceSparrowBRDF;
import org.dayflower.scene.bxdf.TorranceSparrowBTDF;
import org.dayflower.scene.fresnel.DisneyFresnel;
import org.dayflower.scene.fresnel.Fresnel;
import org.dayflower.scene.microfacet.MicrofacetDistribution;
import org.dayflower.scene.microfacet.TrowbridgeReitzMicrofacetDistribution;
import org.dayflower.scene.modifier.Modifier;
import org.dayflower.scene.modifier.NoOpModifier;
import org.dayflower.scene.texture.ConstantTexture;
import org.dayflower.scene.texture.Texture;

import org.macroing.art4j.color.Color3F;
import org.macroing.java.util.visitor.NodeHierarchicalVisitor;
import org.macroing.java.util.visitor.NodeTraversalException;

/**
 * A {@code DisneyMaterial} is an implementation of {@link Material} that represents a Disney material.
 * <p>
 * This class is immutable and thread-safe as long as the {@link Modifier} instance and all {@link Texture} instances are.
 * <p>
 * This {@code Material} implementation is supported on the GPU.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class DisneyMaterial implements Material {
	/**
	 * The name of this {@code DisneyMaterial} class.
	 */
	public static final String NAME = "Disney";
	
	/**
	 * The ID of this {@code DisneyMaterial} class.
	 */
	public static final int ID = 4;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final Modifier modifier;
	private final Texture textureAnisotropic;
	private final Texture textureClearCoat;
	private final Texture textureClearCoatGloss;
	private final Texture textureColor;
	private final Texture textureDiffuseTransmission;
	private final Texture textureEmission;
	private final Texture textureEta;
	private final Texture textureFlatness;
	private final Texture textureMetallic;
	private final Texture textureRoughness;
	private final Texture textureScatterDistance;
	private final Texture textureSheen;
	private final Texture textureSheenTint;
	private final Texture textureSpecularTint;
	private final Texture textureSpecularTransmission;
	private final boolean isThin;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code DisneyMaterial} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new DisneyMaterial(Color3F.GRAY);
	 * }
	 * </pre>
	 */
	public DisneyMaterial() {
		this(Color3F.GRAY);
	}
	
	/**
	 * Constructs a new {@code DisneyMaterial} instance.
	 * <p>
	 * If {@code colorColor} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new DisneyMaterial(colorColor, Color3F.BLACK);
	 * }
	 * </pre>
	 * 
	 * @param colorColor a {@link Color3F} instance for the color
	 * @throws NullPointerException thrown if, and only if, {@code colorColor} is {@code null}
	 */
	public DisneyMaterial(final Color3F colorColor) {
		this(colorColor, Color3F.BLACK);
	}
	
	/**
	 * Constructs a new {@code DisneyMaterial} instance.
	 * <p>
	 * If either {@code colorColor} or {@code colorEmission} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new DisneyMaterial(colorColor, colorEmission, Color3F.BLACK);
	 * }
	 * </pre>
	 * 
	 * @param colorColor a {@link Color3F} instance for the color
	 * @param colorEmission a {@code Color3F} instance for emission
	 * @throws NullPointerException thrown if, and only if, either {@code colorColor} or {@code colorEmission} are {@code null}
	 */
	public DisneyMaterial(final Color3F colorColor, final Color3F colorEmission) {
		this(colorColor, colorEmission, Color3F.BLACK);
	}
	
	/**
	 * Constructs a new {@code DisneyMaterial} instance.
	 * <p>
	 * If either {@code colorColor}, {@code colorEmission} or {@code colorScatterDistance} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new DisneyMaterial(colorColor, colorEmission, colorScatterDistance, 0.0F);
	 * }
	 * </pre>
	 * 
	 * @param colorColor a {@link Color3F} instance for the color
	 * @param colorEmission a {@code Color3F} instance for emission
	 * @param colorScatterDistance a {@code Color3F} instance for the scatter distance
	 * @throws NullPointerException thrown if, and only if, either {@code colorColor}, {@code colorEmission} or {@code colorScatterDistance} are {@code null}
	 */
	public DisneyMaterial(final Color3F colorColor, final Color3F colorEmission, final Color3F colorScatterDistance) {
		this(colorColor, colorEmission, colorScatterDistance, 0.0F);
	}
	
	/**
	 * Constructs a new {@code DisneyMaterial} instance.
	 * <p>
	 * If either {@code colorColor}, {@code colorEmission} or {@code colorScatterDistance} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new DisneyMaterial(colorColor, colorEmission, colorScatterDistance, floatAnisotropic, 0.0F);
	 * }
	 * </pre>
	 * 
	 * @param colorColor a {@link Color3F} instance for the color
	 * @param colorEmission a {@code Color3F} instance for emission
	 * @param colorScatterDistance a {@code Color3F} instance for the scatter distance
	 * @param floatAnisotropic a {@code float} for the anisotropic value
	 * @throws NullPointerException thrown if, and only if, either {@code colorColor}, {@code colorEmission} or {@code colorScatterDistance} are {@code null}
	 */
	public DisneyMaterial(final Color3F colorColor, final Color3F colorEmission, final Color3F colorScatterDistance, final float floatAnisotropic) {
		this(colorColor, colorEmission, colorScatterDistance, floatAnisotropic, 0.0F);
	}
	
	/**
	 * Constructs a new {@code DisneyMaterial} instance.
	 * <p>
	 * If either {@code colorColor}, {@code colorEmission} or {@code colorScatterDistance} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new DisneyMaterial(colorColor, colorEmission, colorScatterDistance, floatAnisotropic, floatClearCoat, 1.0F);
	 * }
	 * </pre>
	 * 
	 * @param colorColor a {@link Color3F} instance for the color
	 * @param colorEmission a {@code Color3F} instance for emission
	 * @param colorScatterDistance a {@code Color3F} instance for the scatter distance
	 * @param floatAnisotropic a {@code float} for the anisotropic value
	 * @param floatClearCoat a {@code float} for the clear coat value
	 * @throws NullPointerException thrown if, and only if, either {@code colorColor}, {@code colorEmission} or {@code colorScatterDistance} are {@code null}
	 */
	public DisneyMaterial(final Color3F colorColor, final Color3F colorEmission, final Color3F colorScatterDistance, final float floatAnisotropic, final float floatClearCoat) {
		this(colorColor, colorEmission, colorScatterDistance, floatAnisotropic, floatClearCoat, 1.0F);
	}
	
	/**
	 * Constructs a new {@code DisneyMaterial} instance.
	 * <p>
	 * If either {@code colorColor}, {@code colorEmission} or {@code colorScatterDistance} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new DisneyMaterial(colorColor, colorEmission, colorScatterDistance, floatAnisotropic, floatClearCoat, floatClearCoatGloss, 1.0F);
	 * }
	 * </pre>
	 * 
	 * @param colorColor a {@link Color3F} instance for the color
	 * @param colorEmission a {@code Color3F} instance for emission
	 * @param colorScatterDistance a {@code Color3F} instance for the scatter distance
	 * @param floatAnisotropic a {@code float} for the anisotropic value
	 * @param floatClearCoat a {@code float} for the clear coat value
	 * @param floatClearCoatGloss a {@code float} for the clear coat gloss value
	 * @throws NullPointerException thrown if, and only if, either {@code colorColor}, {@code colorEmission} or {@code colorScatterDistance} are {@code null}
	 */
	public DisneyMaterial(final Color3F colorColor, final Color3F colorEmission, final Color3F colorScatterDistance, final float floatAnisotropic, final float floatClearCoat, final float floatClearCoatGloss) {
		this(colorColor, colorEmission, colorScatterDistance, floatAnisotropic, floatClearCoat, floatClearCoatGloss, 1.0F);
	}
	
	/**
	 * Constructs a new {@code DisneyMaterial} instance.
	 * <p>
	 * If either {@code colorColor}, {@code colorEmission} or {@code colorScatterDistance} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new DisneyMaterial(colorColor, colorEmission, colorScatterDistance, floatAnisotropic, floatClearCoat, floatClearCoatGloss, floatDiffuseTransmission, 1.5F);
	 * }
	 * </pre>
	 * 
	 * @param colorColor a {@link Color3F} instance for the color
	 * @param colorEmission a {@code Color3F} instance for emission
	 * @param colorScatterDistance a {@code Color3F} instance for the scatter distance
	 * @param floatAnisotropic a {@code float} for the anisotropic value
	 * @param floatClearCoat a {@code float} for the clear coat value
	 * @param floatClearCoatGloss a {@code float} for the clear coat gloss value
	 * @param floatDiffuseTransmission a {@code float} for the diffuse transmission
	 * @throws NullPointerException thrown if, and only if, either {@code colorColor}, {@code colorEmission} or {@code colorScatterDistance} are {@code null}
	 */
	public DisneyMaterial(final Color3F colorColor, final Color3F colorEmission, final Color3F colorScatterDistance, final float floatAnisotropic, final float floatClearCoat, final float floatClearCoatGloss, final float floatDiffuseTransmission) {
		this(colorColor, colorEmission, colorScatterDistance, floatAnisotropic, floatClearCoat, floatClearCoatGloss, floatDiffuseTransmission, 1.5F);
	}
	
	/**
	 * Constructs a new {@code DisneyMaterial} instance.
	 * <p>
	 * If either {@code colorColor}, {@code colorEmission} or {@code colorScatterDistance} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new DisneyMaterial(colorColor, colorEmission, colorScatterDistance, floatAnisotropic, floatClearCoat, floatClearCoatGloss, floatDiffuseTransmission, floatEta, 0.0F);
	 * }
	 * </pre>
	 * 
	 * @param colorColor a {@link Color3F} instance for the color
	 * @param colorEmission a {@code Color3F} instance for emission
	 * @param colorScatterDistance a {@code Color3F} instance for the scatter distance
	 * @param floatAnisotropic a {@code float} for the anisotropic value
	 * @param floatClearCoat a {@code float} for the clear coat value
	 * @param floatClearCoatGloss a {@code float} for the clear coat gloss value
	 * @param floatDiffuseTransmission a {@code float} for the diffuse transmission
	 * @param floatEta a {@code float} for the index of refraction (IOR)
	 * @throws NullPointerException thrown if, and only if, either {@code colorColor}, {@code colorEmission} or {@code colorScatterDistance} are {@code null}
	 */
	public DisneyMaterial(final Color3F colorColor, final Color3F colorEmission, final Color3F colorScatterDistance, final float floatAnisotropic, final float floatClearCoat, final float floatClearCoatGloss, final float floatDiffuseTransmission, final float floatEta) {
		this(colorColor, colorEmission, colorScatterDistance, floatAnisotropic, floatClearCoat, floatClearCoatGloss, floatDiffuseTransmission, floatEta, 0.0F);
	}
	
	/**
	 * Constructs a new {@code DisneyMaterial} instance.
	 * <p>
	 * If either {@code colorColor}, {@code colorEmission} or {@code colorScatterDistance} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new DisneyMaterial(colorColor, colorEmission, colorScatterDistance, floatAnisotropic, floatClearCoat, floatClearCoatGloss, floatDiffuseTransmission, floatEta, floatFlatness, 0.0F);
	 * }
	 * </pre>
	 * 
	 * @param colorColor a {@link Color3F} instance for the color
	 * @param colorEmission a {@code Color3F} instance for emission
	 * @param colorScatterDistance a {@code Color3F} instance for the scatter distance
	 * @param floatAnisotropic a {@code float} for the anisotropic value
	 * @param floatClearCoat a {@code float} for the clear coat value
	 * @param floatClearCoatGloss a {@code float} for the clear coat gloss value
	 * @param floatDiffuseTransmission a {@code float} for the diffuse transmission
	 * @param floatEta a {@code float} for the index of refraction (IOR)
	 * @param floatFlatness a {@code float} for the flatness
	 * @throws NullPointerException thrown if, and only if, either {@code colorColor}, {@code colorEmission} or {@code colorScatterDistance} are {@code null}
	 */
	public DisneyMaterial(final Color3F colorColor, final Color3F colorEmission, final Color3F colorScatterDistance, final float floatAnisotropic, final float floatClearCoat, final float floatClearCoatGloss, final float floatDiffuseTransmission, final float floatEta, final float floatFlatness) {
		this(colorColor, colorEmission, colorScatterDistance, floatAnisotropic, floatClearCoat, floatClearCoatGloss, floatDiffuseTransmission, floatEta, floatFlatness, 0.0F);
	}
	
	/**
	 * Constructs a new {@code DisneyMaterial} instance.
	 * <p>
	 * If either {@code colorColor}, {@code colorEmission} or {@code colorScatterDistance} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new DisneyMaterial(colorColor, colorEmission, colorScatterDistance, floatAnisotropic, floatClearCoat, floatClearCoatGloss, floatDiffuseTransmission, floatEta, floatFlatness, floatMetallic, 0.5F);
	 * }
	 * </pre>
	 * 
	 * @param colorColor a {@link Color3F} instance for the color
	 * @param colorEmission a {@code Color3F} instance for emission
	 * @param colorScatterDistance a {@code Color3F} instance for the scatter distance
	 * @param floatAnisotropic a {@code float} for the anisotropic value
	 * @param floatClearCoat a {@code float} for the clear coat value
	 * @param floatClearCoatGloss a {@code float} for the clear coat gloss value
	 * @param floatDiffuseTransmission a {@code float} for the diffuse transmission
	 * @param floatEta a {@code float} for the index of refraction (IOR)
	 * @param floatFlatness a {@code float} for the flatness
	 * @param floatMetallic a {@code float} for the metallic value
	 * @throws NullPointerException thrown if, and only if, either {@code colorColor}, {@code colorEmission} or {@code colorScatterDistance} are {@code null}
	 */
	public DisneyMaterial(final Color3F colorColor, final Color3F colorEmission, final Color3F colorScatterDistance, final float floatAnisotropic, final float floatClearCoat, final float floatClearCoatGloss, final float floatDiffuseTransmission, final float floatEta, final float floatFlatness, final float floatMetallic) {
		this(colorColor, colorEmission, colorScatterDistance, floatAnisotropic, floatClearCoat, floatClearCoatGloss, floatDiffuseTransmission, floatEta, floatFlatness, floatMetallic, 0.5F);
	}
	
	/**
	 * Constructs a new {@code DisneyMaterial} instance.
	 * <p>
	 * If either {@code colorColor}, {@code colorEmission} or {@code colorScatterDistance} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new DisneyMaterial(colorColor, colorEmission, colorScatterDistance, floatAnisotropic, floatClearCoat, floatClearCoatGloss, floatDiffuseTransmission, floatEta, floatFlatness, floatMetallic, floatRoughness, 0.0F);
	 * }
	 * </pre>
	 * 
	 * @param colorColor a {@link Color3F} instance for the color
	 * @param colorEmission a {@code Color3F} instance for emission
	 * @param colorScatterDistance a {@code Color3F} instance for the scatter distance
	 * @param floatAnisotropic a {@code float} for the anisotropic value
	 * @param floatClearCoat a {@code float} for the clear coat value
	 * @param floatClearCoatGloss a {@code float} for the clear coat gloss value
	 * @param floatDiffuseTransmission a {@code float} for the diffuse transmission
	 * @param floatEta a {@code float} for the index of refraction (IOR)
	 * @param floatFlatness a {@code float} for the flatness
	 * @param floatMetallic a {@code float} for the metallic value
	 * @param floatRoughness a {@code float} for the roughness
	 * @throws NullPointerException thrown if, and only if, either {@code colorColor}, {@code colorEmission} or {@code colorScatterDistance} are {@code null}
	 */
	public DisneyMaterial(final Color3F colorColor, final Color3F colorEmission, final Color3F colorScatterDistance, final float floatAnisotropic, final float floatClearCoat, final float floatClearCoatGloss, final float floatDiffuseTransmission, final float floatEta, final float floatFlatness, final float floatMetallic, final float floatRoughness) {
		this(colorColor, colorEmission, colorScatterDistance, floatAnisotropic, floatClearCoat, floatClearCoatGloss, floatDiffuseTransmission, floatEta, floatFlatness, floatMetallic, floatRoughness, 0.0F);
	}
	
	/**
	 * Constructs a new {@code DisneyMaterial} instance.
	 * <p>
	 * If either {@code colorColor}, {@code colorEmission} or {@code colorScatterDistance} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new DisneyMaterial(colorColor, colorEmission, colorScatterDistance, floatAnisotropic, floatClearCoat, floatClearCoatGloss, floatDiffuseTransmission, floatEta, floatFlatness, floatMetallic, floatRoughness, floatSheen, 0.5F);
	 * }
	 * </pre>
	 * 
	 * @param colorColor a {@link Color3F} instance for the color
	 * @param colorEmission a {@code Color3F} instance for emission
	 * @param colorScatterDistance a {@code Color3F} instance for the scatter distance
	 * @param floatAnisotropic a {@code float} for the anisotropic value
	 * @param floatClearCoat a {@code float} for the clear coat value
	 * @param floatClearCoatGloss a {@code float} for the clear coat gloss value
	 * @param floatDiffuseTransmission a {@code float} for the diffuse transmission
	 * @param floatEta a {@code float} for the index of refraction (IOR)
	 * @param floatFlatness a {@code float} for the flatness
	 * @param floatMetallic a {@code float} for the metallic value
	 * @param floatRoughness a {@code float} for the roughness
	 * @param floatSheen a {@code float} for the sheen value
	 * @throws NullPointerException thrown if, and only if, either {@code colorColor}, {@code colorEmission} or {@code colorScatterDistance} are {@code null}
	 */
	public DisneyMaterial(final Color3F colorColor, final Color3F colorEmission, final Color3F colorScatterDistance, final float floatAnisotropic, final float floatClearCoat, final float floatClearCoatGloss, final float floatDiffuseTransmission, final float floatEta, final float floatFlatness, final float floatMetallic, final float floatRoughness, final float floatSheen) {
		this(colorColor, colorEmission, colorScatterDistance, floatAnisotropic, floatClearCoat, floatClearCoatGloss, floatDiffuseTransmission, floatEta, floatFlatness, floatMetallic, floatRoughness, floatSheen, 0.5F);
	}
	
	/**
	 * Constructs a new {@code DisneyMaterial} instance.
	 * <p>
	 * If either {@code colorColor}, {@code colorEmission} or {@code colorScatterDistance} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new DisneyMaterial(colorColor, colorEmission, colorScatterDistance, floatAnisotropic, floatClearCoat, floatClearCoatGloss, floatDiffuseTransmission, floatEta, floatFlatness, floatMetallic, floatRoughness, floatSheen, floatSheenTint, 0.0F);
	 * }
	 * </pre>
	 * 
	 * @param colorColor a {@link Color3F} instance for the color
	 * @param colorEmission a {@code Color3F} instance for emission
	 * @param colorScatterDistance a {@code Color3F} instance for the scatter distance
	 * @param floatAnisotropic a {@code float} for the anisotropic value
	 * @param floatClearCoat a {@code float} for the clear coat value
	 * @param floatClearCoatGloss a {@code float} for the clear coat gloss value
	 * @param floatDiffuseTransmission a {@code float} for the diffuse transmission
	 * @param floatEta a {@code float} for the index of refraction (IOR)
	 * @param floatFlatness a {@code float} for the flatness
	 * @param floatMetallic a {@code float} for the metallic value
	 * @param floatRoughness a {@code float} for the roughness
	 * @param floatSheen a {@code float} for the sheen value
	 * @param floatSheenTint a {@code float} for the sheen tint value
	 * @throws NullPointerException thrown if, and only if, either {@code colorColor}, {@code colorEmission} or {@code colorScatterDistance} are {@code null}
	 */
	public DisneyMaterial(final Color3F colorColor, final Color3F colorEmission, final Color3F colorScatterDistance, final float floatAnisotropic, final float floatClearCoat, final float floatClearCoatGloss, final float floatDiffuseTransmission, final float floatEta, final float floatFlatness, final float floatMetallic, final float floatRoughness, final float floatSheen, final float floatSheenTint) {
		this(colorColor, colorEmission, colorScatterDistance, floatAnisotropic, floatClearCoat, floatClearCoatGloss, floatDiffuseTransmission, floatEta, floatFlatness, floatMetallic, floatRoughness, floatSheen, floatSheenTint, 0.0F);
	}
	
	/**
	 * Constructs a new {@code DisneyMaterial} instance.
	 * <p>
	 * If either {@code colorColor}, {@code colorEmission} or {@code colorScatterDistance} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new DisneyMaterial(colorColor, colorEmission, colorScatterDistance, floatAnisotropic, floatClearCoat, floatClearCoatGloss, floatDiffuseTransmission, floatEta, floatFlatness, floatMetallic, floatRoughness, floatSheen, floatSheenTint, floatSpecularTint, 0.0F);
	 * }
	 * </pre>
	 * 
	 * @param colorColor a {@link Color3F} instance for the color
	 * @param colorEmission a {@code Color3F} instance for emission
	 * @param colorScatterDistance a {@code Color3F} instance for the scatter distance
	 * @param floatAnisotropic a {@code float} for the anisotropic value
	 * @param floatClearCoat a {@code float} for the clear coat value
	 * @param floatClearCoatGloss a {@code float} for the clear coat gloss value
	 * @param floatDiffuseTransmission a {@code float} for the diffuse transmission
	 * @param floatEta a {@code float} for the index of refraction (IOR)
	 * @param floatFlatness a {@code float} for the flatness
	 * @param floatMetallic a {@code float} for the metallic value
	 * @param floatRoughness a {@code float} for the roughness
	 * @param floatSheen a {@code float} for the sheen value
	 * @param floatSheenTint a {@code float} for the sheen tint value
	 * @param floatSpecularTint a {@code float} for the specular tint
	 * @throws NullPointerException thrown if, and only if, either {@code colorColor}, {@code colorEmission} or {@code colorScatterDistance} are {@code null}
	 */
	public DisneyMaterial(final Color3F colorColor, final Color3F colorEmission, final Color3F colorScatterDistance, final float floatAnisotropic, final float floatClearCoat, final float floatClearCoatGloss, final float floatDiffuseTransmission, final float floatEta, final float floatFlatness, final float floatMetallic, final float floatRoughness, final float floatSheen, final float floatSheenTint, final float floatSpecularTint) {
		this(colorColor, colorEmission, colorScatterDistance, floatAnisotropic, floatClearCoat, floatClearCoatGloss, floatDiffuseTransmission, floatEta, floatFlatness, floatMetallic, floatRoughness, floatSheen, floatSheenTint, floatSpecularTint, 0.0F);
	}
	
	/**
	 * Constructs a new {@code DisneyMaterial} instance.
	 * <p>
	 * If either {@code colorColor}, {@code colorEmission} or {@code colorScatterDistance} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new DisneyMaterial(colorColor, colorEmission, colorScatterDistance, floatAnisotropic, floatClearCoat, floatClearCoatGloss, floatDiffuseTransmission, floatEta, floatFlatness, floatMetallic, floatRoughness, floatSheen, floatSheenTint, floatSpecularTint, floatSpecularTransmission, false);
	 * }
	 * </pre>
	 * 
	 * @param colorColor a {@link Color3F} instance for the color
	 * @param colorEmission a {@code Color3F} instance for emission
	 * @param colorScatterDistance a {@code Color3F} instance for the scatter distance
	 * @param floatAnisotropic a {@code float} for the anisotropic value
	 * @param floatClearCoat a {@code float} for the clear coat value
	 * @param floatClearCoatGloss a {@code float} for the clear coat gloss value
	 * @param floatDiffuseTransmission a {@code float} for the diffuse transmission
	 * @param floatEta a {@code float} for the index of refraction (IOR)
	 * @param floatFlatness a {@code float} for the flatness
	 * @param floatMetallic a {@code float} for the metallic value
	 * @param floatRoughness a {@code float} for the roughness
	 * @param floatSheen a {@code float} for the sheen value
	 * @param floatSheenTint a {@code float} for the sheen tint value
	 * @param floatSpecularTint a {@code float} for the specular tint
	 * @param floatSpecularTransmission a {@code float} for the specular transmission
	 * @throws NullPointerException thrown if, and only if, either {@code colorColor}, {@code colorEmission} or {@code colorScatterDistance} are {@code null}
	 */
	public DisneyMaterial(final Color3F colorColor, final Color3F colorEmission, final Color3F colorScatterDistance, final float floatAnisotropic, final float floatClearCoat, final float floatClearCoatGloss, final float floatDiffuseTransmission, final float floatEta, final float floatFlatness, final float floatMetallic, final float floatRoughness, final float floatSheen, final float floatSheenTint, final float floatSpecularTint, final float floatSpecularTransmission) {
		this(colorColor, colorEmission, colorScatterDistance, floatAnisotropic, floatClearCoat, floatClearCoatGloss, floatDiffuseTransmission, floatEta, floatFlatness, floatMetallic, floatRoughness, floatSheen, floatSheenTint, floatSpecularTint, floatSpecularTransmission, false);
	}
	
	/**
	 * Constructs a new {@code DisneyMaterial} instance.
	 * <p>
	 * If either {@code colorColor}, {@code colorEmission} or {@code colorScatterDistance} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new DisneyMaterial(colorColor, colorEmission, colorScatterDistance, floatAnisotropic, floatClearCoat, floatClearCoatGloss, floatDiffuseTransmission, floatEta, floatFlatness, floatMetallic, floatRoughness, floatSheen, floatSheenTint, floatSpecularTint, floatSpecularTransmission, isThin, new NoOpModifier());
	 * }
	 * </pre>
	 * 
	 * @param colorColor a {@link Color3F} instance for the color
	 * @param colorEmission a {@code Color3F} instance for emission
	 * @param colorScatterDistance a {@code Color3F} instance for the scatter distance
	 * @param floatAnisotropic a {@code float} for the anisotropic value
	 * @param floatClearCoat a {@code float} for the clear coat value
	 * @param floatClearCoatGloss a {@code float} for the clear coat gloss value
	 * @param floatDiffuseTransmission a {@code float} for the diffuse transmission
	 * @param floatEta a {@code float} for the index of refraction (IOR)
	 * @param floatFlatness a {@code float} for the flatness
	 * @param floatMetallic a {@code float} for the metallic value
	 * @param floatRoughness a {@code float} for the roughness
	 * @param floatSheen a {@code float} for the sheen value
	 * @param floatSheenTint a {@code float} for the sheen tint value
	 * @param floatSpecularTint a {@code float} for the specular tint
	 * @param floatSpecularTransmission a {@code float} for the specular transmission
	 * @param isThin {@code true} if, and only if, this {@code DisneyMaterial} instance is thin, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, either {@code colorColor}, {@code colorEmission} or {@code colorScatterDistance} are {@code null}
	 */
	public DisneyMaterial(final Color3F colorColor, final Color3F colorEmission, final Color3F colorScatterDistance, final float floatAnisotropic, final float floatClearCoat, final float floatClearCoatGloss, final float floatDiffuseTransmission, final float floatEta, final float floatFlatness, final float floatMetallic, final float floatRoughness, final float floatSheen, final float floatSheenTint, final float floatSpecularTint, final float floatSpecularTransmission, final boolean isThin) {
		this(colorColor, colorEmission, colorScatterDistance, floatAnisotropic, floatClearCoat, floatClearCoatGloss, floatDiffuseTransmission, floatEta, floatFlatness, floatMetallic, floatRoughness, floatSheen, floatSheenTint, floatSpecularTint, floatSpecularTransmission, isThin, new NoOpModifier());
	}
	
	/**
	 * Constructs a new {@code DisneyMaterial} instance.
	 * <p>
	 * If either {@code colorColor}, {@code colorEmission}, {@code colorScatterDistance} or {@code modifier} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param colorColor a {@link Color3F} instance for the color
	 * @param colorEmission a {@code Color3F} instance for emission
	 * @param colorScatterDistance a {@code Color3F} instance for the scatter distance
	 * @param floatAnisotropic a {@code float} for the anisotropic value
	 * @param floatClearCoat a {@code float} for the clear coat value
	 * @param floatClearCoatGloss a {@code float} for the clear coat gloss value
	 * @param floatDiffuseTransmission a {@code float} for the diffuse transmission
	 * @param floatEta a {@code float} for the index of refraction (IOR)
	 * @param floatFlatness a {@code float} for the flatness
	 * @param floatMetallic a {@code float} for the metallic value
	 * @param floatRoughness a {@code float} for the roughness
	 * @param floatSheen a {@code float} for the sheen value
	 * @param floatSheenTint a {@code float} for the sheen tint value
	 * @param floatSpecularTint a {@code float} for the specular tint
	 * @param floatSpecularTransmission a {@code float} for the specular transmission
	 * @param isThin {@code true} if, and only if, this {@code DisneyMaterial} instance is thin, {@code false} otherwise
	 * @param modifier a {@link Modifier} instance
	 * @throws NullPointerException thrown if, and only if, either {@code colorColor}, {@code colorEmission}, {@code colorScatterDistance} or {@code modifier} are {@code null}
	 */
	public DisneyMaterial(final Color3F colorColor, final Color3F colorEmission, final Color3F colorScatterDistance, final float floatAnisotropic, final float floatClearCoat, final float floatClearCoatGloss, final float floatDiffuseTransmission, final float floatEta, final float floatFlatness, final float floatMetallic, final float floatRoughness, final float floatSheen, final float floatSheenTint, final float floatSpecularTint, final float floatSpecularTransmission, final boolean isThin, final Modifier modifier) {
		this.textureColor = new ConstantTexture(Objects.requireNonNull(colorColor, "colorColor == null"));
		this.textureEmission = new ConstantTexture(Objects.requireNonNull(colorEmission, "colorEmission == null"));
		this.textureScatterDistance = new ConstantTexture(Objects.requireNonNull(colorScatterDistance, "colorScatterDistance == null"));
		this.textureAnisotropic = new ConstantTexture(floatAnisotropic);
		this.textureClearCoat = new ConstantTexture(floatClearCoat);
		this.textureClearCoatGloss = new ConstantTexture(floatClearCoatGloss);
		this.textureDiffuseTransmission = new ConstantTexture(floatDiffuseTransmission);
		this.textureEta = new ConstantTexture(floatEta);
		this.textureFlatness = new ConstantTexture(floatFlatness);
		this.textureMetallic = new ConstantTexture(floatMetallic);
		this.textureRoughness = new ConstantTexture(floatRoughness);
		this.textureSheen = new ConstantTexture(floatSheen);
		this.textureSheenTint = new ConstantTexture(floatSheenTint);
		this.textureSpecularTint = new ConstantTexture(floatSpecularTint);
		this.textureSpecularTransmission = new ConstantTexture(floatSpecularTransmission);
		this.isThin = isThin;
		this.modifier = Objects.requireNonNull(modifier, "modifier == null");
	}
	
	/**
	 * Constructs a new {@code DisneyMaterial} instance.
	 * <p>
	 * If {@code textureColor} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new DisneyMaterial(textureColor, ConstantTexture.BLACK);
	 * }
	 * </pre>
	 * 
	 * @param textureColor a {@link Texture} instance for the color
	 * @throws NullPointerException thrown if, and only if, {@code textureColor} is {@code null}
	 */
	public DisneyMaterial(final Texture textureColor) {
		this(textureColor, ConstantTexture.BLACK);
	}
	
	/**
	 * Constructs a new {@code DisneyMaterial} instance.
	 * <p>
	 * If either {@code textureColor} or {@code textureEmission} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new DisneyMaterial(textureColor, textureEmission, ConstantTexture.BLACK);
	 * }
	 * </pre>
	 * 
	 * @param textureColor a {@link Texture} instance for the color
	 * @param textureEmission a {@code Texture} instance for emission
	 * @throws NullPointerException thrown if, and only if, either {@code textureColor} or {@code textureEmission} are {@code null}
	 */
	public DisneyMaterial(final Texture textureColor, final Texture textureEmission) {
		this(textureColor, textureEmission, ConstantTexture.BLACK);
	}
	
	/**
	 * Constructs a new {@code DisneyMaterial} instance.
	 * <p>
	 * If either {@code textureColor}, {@code textureEmission} or {@code textureScatterDistance} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new DisneyMaterial(textureColor, textureEmission, textureScatterDistance, ConstantTexture.BLACK);
	 * }
	 * </pre>
	 * 
	 * @param textureColor a {@link Texture} instance for the color
	 * @param textureEmission a {@code Texture} instance for emission
	 * @param textureScatterDistance a {@code Texture} instance for the scatter distance
	 * @throws NullPointerException thrown if, and only if, either {@code textureColor}, {@code textureEmission} or {@code textureScatterDistance} are {@code null}
	 */
	public DisneyMaterial(final Texture textureColor, final Texture textureEmission, final Texture textureScatterDistance) {
		this(textureColor, textureEmission, textureScatterDistance, ConstantTexture.BLACK);
	}
	
	/**
	 * Constructs a new {@code DisneyMaterial} instance.
	 * <p>
	 * If either {@code textureColor}, {@code textureEmission}, {@code textureScatterDistance} or {@code textureAnisotropic} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new DisneyMaterial(textureColor, textureEmission, textureScatterDistance, textureAnisotropic, ConstantTexture.BLACK);
	 * }
	 * </pre>
	 * 
	 * @param textureColor a {@link Texture} instance for the color
	 * @param textureEmission a {@code Texture} instance for emission
	 * @param textureScatterDistance a {@code Texture} instance for the scatter distance
	 * @param textureAnisotropic a {@code Texture} instance for the anisotropic value
	 * @throws NullPointerException thrown if, and only if, either {@code textureColor}, {@code textureEmission}, {@code textureScatterDistance} or {@code textureAnisotropic} are {@code null}
	 */
	public DisneyMaterial(final Texture textureColor, final Texture textureEmission, final Texture textureScatterDistance, final Texture textureAnisotropic) {
		this(textureColor, textureEmission, textureScatterDistance, textureAnisotropic, ConstantTexture.BLACK);
	}
	
	/**
	 * Constructs a new {@code DisneyMaterial} instance.
	 * <p>
	 * If either {@code textureColor}, {@code textureEmission}, {@code textureScatterDistance}, {@code textureAnisotropic} or {@code textureClearCoat} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new DisneyMaterial(textureColor, textureEmission, textureScatterDistance, textureAnisotropic, textureClearCoat, ConstantTexture.WHITE);
	 * }
	 * </pre>
	 * 
	 * @param textureColor a {@link Texture} instance for the color
	 * @param textureEmission a {@code Texture} instance for emission
	 * @param textureScatterDistance a {@code Texture} instance for the scatter distance
	 * @param textureAnisotropic a {@code Texture} instance for the anisotropic value
	 * @param textureClearCoat a {@code Texture} instance for the clear coat value
	 * @throws NullPointerException thrown if, and only if, either {@code textureColor}, {@code textureEmission}, {@code textureScatterDistance}, {@code textureAnisotropic} or {@code textureClearCoat} are {@code null}
	 */
	public DisneyMaterial(final Texture textureColor, final Texture textureEmission, final Texture textureScatterDistance, final Texture textureAnisotropic, final Texture textureClearCoat) {
		this(textureColor, textureEmission, textureScatterDistance, textureAnisotropic, textureClearCoat, ConstantTexture.WHITE);
	}
	
	/**
	 * Constructs a new {@code DisneyMaterial} instance.
	 * <p>
	 * If either {@code textureColor}, {@code textureEmission}, {@code textureScatterDistance}, {@code textureAnisotropic}, {@code textureClearCoat} or {@code textureClearCoatGloss} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new DisneyMaterial(textureColor, textureEmission, textureScatterDistance, textureAnisotropic, textureClearCoat, textureClearCoatGloss, ConstantTexture.WHITE);
	 * }
	 * </pre>
	 * 
	 * @param textureColor a {@link Texture} instance for the color
	 * @param textureEmission a {@code Texture} instance for emission
	 * @param textureScatterDistance a {@code Texture} instance for the scatter distance
	 * @param textureAnisotropic a {@code Texture} instance for the anisotropic value
	 * @param textureClearCoat a {@code Texture} instance for the clear coat value
	 * @param textureClearCoatGloss a {@code Texture} instance for the clear coat gloss value
	 * @throws NullPointerException thrown if, and only if, either {@code textureColor}, {@code textureEmission}, {@code textureScatterDistance}, {@code textureAnisotropic}, {@code textureClearCoat} or {@code textureClearCoatGloss} are {@code null}
	 */
	public DisneyMaterial(final Texture textureColor, final Texture textureEmission, final Texture textureScatterDistance, final Texture textureAnisotropic, final Texture textureClearCoat, final Texture textureClearCoatGloss) {
		this(textureColor, textureEmission, textureScatterDistance, textureAnisotropic, textureClearCoat, textureClearCoatGloss, ConstantTexture.WHITE);
	}
	
	/**
	 * Constructs a new {@code DisneyMaterial} instance.
	 * <p>
	 * If either {@code textureColor}, {@code textureEmission}, {@code textureScatterDistance}, {@code textureAnisotropic}, {@code textureClearCoat}, {@code textureClearCoatGloss} or {@code textureDiffuseTransmission} are {@code null}, a
	 * {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new DisneyMaterial(textureColor, textureEmission, textureScatterDistance, textureAnisotropic, textureClearCoat, textureClearCoatGloss, textureDiffuseTransmission, ConstantTexture.GRAY_1_50);
	 * }
	 * </pre>
	 * 
	 * @param textureColor a {@link Texture} instance for the color
	 * @param textureEmission a {@code Texture} instance for emission
	 * @param textureScatterDistance a {@code Texture} instance for the scatter distance
	 * @param textureAnisotropic a {@code Texture} instance for the anisotropic value
	 * @param textureClearCoat a {@code Texture} instance for the clear coat value
	 * @param textureClearCoatGloss a {@code Texture} instance for the clear coat gloss value
	 * @param textureDiffuseTransmission a {@code Texture} instance for the diffuse transmission
	 * @throws NullPointerException thrown if, and only if, either {@code textureColor}, {@code textureEmission}, {@code textureScatterDistance}, {@code textureAnisotropic}, {@code textureClearCoat}, {@code textureClearCoatGloss} or
	 *                              {@code textureDiffuseTransmission} are {@code null}
	 */
	public DisneyMaterial(final Texture textureColor, final Texture textureEmission, final Texture textureScatterDistance, final Texture textureAnisotropic, final Texture textureClearCoat, final Texture textureClearCoatGloss, final Texture textureDiffuseTransmission) {
		this(textureColor, textureEmission, textureScatterDistance, textureAnisotropic, textureClearCoat, textureClearCoatGloss, textureDiffuseTransmission, ConstantTexture.GRAY_1_50);
	}
	
	/**
	 * Constructs a new {@code DisneyMaterial} instance.
	 * <p>
	 * If either {@code textureColor}, {@code textureEmission}, {@code textureScatterDistance}, {@code textureAnisotropic}, {@code textureClearCoat}, {@code textureClearCoatGloss}, {@code textureDiffuseTransmission} or {@code textureEta} are
	 * {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new DisneyMaterial(textureColor, textureEmission, textureScatterDistance, textureAnisotropic, textureClearCoat, textureClearCoatGloss, textureDiffuseTransmission, textureEta, ConstantTexture.BLACK);
	 * }
	 * </pre>
	 * 
	 * @param textureColor a {@link Texture} instance for the color
	 * @param textureEmission a {@code Texture} instance for emission
	 * @param textureScatterDistance a {@code Texture} instance for the scatter distance
	 * @param textureAnisotropic a {@code Texture} instance for the anisotropic value
	 * @param textureClearCoat a {@code Texture} instance for the clear coat value
	 * @param textureClearCoatGloss a {@code Texture} instance for the clear coat gloss value
	 * @param textureDiffuseTransmission a {@code Texture} instance for the diffuse transmission
	 * @param textureEta a {@code Texture} instance for the index of refraction (IOR)
	 * @throws NullPointerException thrown if, and only if, either {@code textureColor}, {@code textureEmission}, {@code textureScatterDistance}, {@code textureAnisotropic}, {@code textureClearCoat}, {@code textureClearCoatGloss},
	 *                              {@code textureDiffuseTransmission} or {@code textureEta} are {@code null}
	 */
	public DisneyMaterial(final Texture textureColor, final Texture textureEmission, final Texture textureScatterDistance, final Texture textureAnisotropic, final Texture textureClearCoat, final Texture textureClearCoatGloss, final Texture textureDiffuseTransmission, final Texture textureEta) {
		this(textureColor, textureEmission, textureScatterDistance, textureAnisotropic, textureClearCoat, textureClearCoatGloss, textureDiffuseTransmission, textureEta, ConstantTexture.BLACK);
	}
	
	/**
	 * Constructs a new {@code DisneyMaterial} instance.
	 * <p>
	 * If either {@code textureColor}, {@code textureEmission}, {@code textureScatterDistance}, {@code textureAnisotropic}, {@code textureClearCoat}, {@code textureClearCoatGloss}, {@code textureDiffuseTransmission}, {@code textureEta} or
	 * {@code textureFlatness} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new DisneyMaterial(textureColor, textureEmission, textureScatterDistance, textureAnisotropic, textureClearCoat, textureClearCoatGloss, textureDiffuseTransmission, textureEta, textureFlatness, ConstantTexture.BLACK);
	 * }
	 * </pre>
	 * 
	 * @param textureColor a {@link Texture} instance for the color
	 * @param textureEmission a {@code Texture} instance for emission
	 * @param textureScatterDistance a {@code Texture} instance for the scatter distance
	 * @param textureAnisotropic a {@code Texture} instance for the anisotropic value
	 * @param textureClearCoat a {@code Texture} instance for the clear coat value
	 * @param textureClearCoatGloss a {@code Texture} instance for the clear coat gloss value
	 * @param textureDiffuseTransmission a {@code Texture} instance for the diffuse transmission
	 * @param textureEta a {@code Texture} instance for the index of refraction (IOR)
	 * @param textureFlatness a {@code Texture} instance for the flatness
	 * @throws NullPointerException thrown if, and only if, either {@code textureColor}, {@code textureEmission}, {@code textureScatterDistance}, {@code textureAnisotropic}, {@code textureClearCoat}, {@code textureClearCoatGloss},
	 *                              {@code textureDiffuseTransmission}, {@code textureEta} or {@code textureFlatness} are {@code null}
	 */
	public DisneyMaterial(final Texture textureColor, final Texture textureEmission, final Texture textureScatterDistance, final Texture textureAnisotropic, final Texture textureClearCoat, final Texture textureClearCoatGloss, final Texture textureDiffuseTransmission, final Texture textureEta, final Texture textureFlatness) {
		this(textureColor, textureEmission, textureScatterDistance, textureAnisotropic, textureClearCoat, textureClearCoatGloss, textureDiffuseTransmission, textureEta, textureFlatness, ConstantTexture.BLACK);
	}
	
	/**
	 * Constructs a new {@code DisneyMaterial} instance.
	 * <p>
	 * If either {@code textureColor}, {@code textureEmission}, {@code textureScatterDistance}, {@code textureAnisotropic}, {@code textureClearCoat}, {@code textureClearCoatGloss}, {@code textureDiffuseTransmission}, {@code textureEta},
	 * {@code textureFlatness} or {@code textureMetallic} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new DisneyMaterial(textureColor, textureEmission, textureScatterDistance, textureAnisotropic, textureClearCoat, textureClearCoatGloss, textureDiffuseTransmission, textureEta, textureFlatness, textureMetallic, ConstantTexture.GRAY_0_50);
	 * }
	 * </pre>
	 * 
	 * @param textureColor a {@link Texture} instance for the color
	 * @param textureEmission a {@code Texture} instance for emission
	 * @param textureScatterDistance a {@code Texture} instance for the scatter distance
	 * @param textureAnisotropic a {@code Texture} instance for the anisotropic value
	 * @param textureClearCoat a {@code Texture} instance for the clear coat value
	 * @param textureClearCoatGloss a {@code Texture} instance for the clear coat gloss value
	 * @param textureDiffuseTransmission a {@code Texture} instance for the diffuse transmission
	 * @param textureEta a {@code Texture} instance for the index of refraction (IOR)
	 * @param textureFlatness a {@code Texture} instance for the flatness
	 * @param textureMetallic a {@code Texture} instance for the metallic value
	 * @throws NullPointerException thrown if, and only if, either {@code textureColor}, {@code textureEmission}, {@code textureScatterDistance}, {@code textureAnisotropic}, {@code textureClearCoat}, {@code textureClearCoatGloss},
	 *                              {@code textureDiffuseTransmission}, {@code textureEta}, {@code textureFlatness} or {@code textureMetallic} are {@code null}
	 */
	public DisneyMaterial(final Texture textureColor, final Texture textureEmission, final Texture textureScatterDistance, final Texture textureAnisotropic, final Texture textureClearCoat, final Texture textureClearCoatGloss, final Texture textureDiffuseTransmission, final Texture textureEta, final Texture textureFlatness, final Texture textureMetallic) {
		this(textureColor, textureEmission, textureScatterDistance, textureAnisotropic, textureClearCoat, textureClearCoatGloss, textureDiffuseTransmission, textureEta, textureFlatness, textureMetallic, ConstantTexture.GRAY_0_50);
	}
	
	/**
	 * Constructs a new {@code DisneyMaterial} instance.
	 * <p>
	 * If either {@code textureColor}, {@code textureEmission}, {@code textureScatterDistance}, {@code textureAnisotropic}, {@code textureClearCoat}, {@code textureClearCoatGloss}, {@code textureDiffuseTransmission}, {@code textureEta},
	 * {@code textureFlatness}, {@code textureMetallic} or {@code textureRoughness} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new DisneyMaterial(textureColor, textureEmission, textureScatterDistance, textureAnisotropic, textureClearCoat, textureClearCoatGloss, textureDiffuseTransmission, textureEta, textureFlatness, textureMetallic, textureRoughness, ConstantTexture.BLACK);
	 * }
	 * </pre>
	 * 
	 * @param textureColor a {@link Texture} instance for the color
	 * @param textureEmission a {@code Texture} instance for emission
	 * @param textureScatterDistance a {@code Texture} instance for the scatter distance
	 * @param textureAnisotropic a {@code Texture} instance for the anisotropic value
	 * @param textureClearCoat a {@code Texture} instance for the clear coat value
	 * @param textureClearCoatGloss a {@code Texture} instance for the clear coat gloss value
	 * @param textureDiffuseTransmission a {@code Texture} instance for the diffuse transmission
	 * @param textureEta a {@code Texture} instance for the index of refraction (IOR)
	 * @param textureFlatness a {@code Texture} instance for the flatness
	 * @param textureMetallic a {@code Texture} instance for the metallic value
	 * @param textureRoughness a {@code Texture} instance for the roughness
	 * @throws NullPointerException thrown if, and only if, either {@code textureColor}, {@code textureEmission}, {@code textureScatterDistance}, {@code textureAnisotropic}, {@code textureClearCoat}, {@code textureClearCoatGloss},
	 *                              {@code textureDiffuseTransmission}, {@code textureEta}, {@code textureFlatness}, {@code textureMetallic} or {@code textureRoughness} are {@code null}
	 */
	public DisneyMaterial(final Texture textureColor, final Texture textureEmission, final Texture textureScatterDistance, final Texture textureAnisotropic, final Texture textureClearCoat, final Texture textureClearCoatGloss, final Texture textureDiffuseTransmission, final Texture textureEta, final Texture textureFlatness, final Texture textureMetallic, final Texture textureRoughness) {
		this(textureColor, textureEmission, textureScatterDistance, textureAnisotropic, textureClearCoat, textureClearCoatGloss, textureDiffuseTransmission, textureEta, textureFlatness, textureMetallic, textureRoughness, ConstantTexture.BLACK);
	}
	
	/**
	 * Constructs a new {@code DisneyMaterial} instance.
	 * <p>
	 * If either {@code textureColor}, {@code textureEmission}, {@code textureScatterDistance}, {@code textureAnisotropic}, {@code textureClearCoat}, {@code textureClearCoatGloss}, {@code textureDiffuseTransmission}, {@code textureEta},
	 * {@code textureFlatness}, {@code textureMetallic}, {@code textureRoughness} or {@code textureSheen} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new DisneyMaterial(textureColor, textureEmission, textureScatterDistance, textureAnisotropic, textureClearCoat, textureClearCoatGloss, textureDiffuseTransmission, textureEta, textureFlatness, textureMetallic, textureRoughness, textureSheen, ConstantTexture.GRAY_0_50);
	 * }
	 * </pre>
	 * 
	 * @param textureColor a {@link Texture} instance for the color
	 * @param textureEmission a {@code Texture} instance for emission
	 * @param textureScatterDistance a {@code Texture} instance for the scatter distance
	 * @param textureAnisotropic a {@code Texture} instance for the anisotropic value
	 * @param textureClearCoat a {@code Texture} instance for the clear coat value
	 * @param textureClearCoatGloss a {@code Texture} instance for the clear coat gloss value
	 * @param textureDiffuseTransmission a {@code Texture} instance for the diffuse transmission
	 * @param textureEta a {@code Texture} instance for the index of refraction (IOR)
	 * @param textureFlatness a {@code Texture} instance for the flatness
	 * @param textureMetallic a {@code Texture} instance for the metallic value
	 * @param textureRoughness a {@code Texture} instance for the roughness
	 * @param textureSheen a {@code Texture} instance for the sheen value
	 * @throws NullPointerException thrown if, and only if, either {@code textureColor}, {@code textureEmission}, {@code textureScatterDistance}, {@code textureAnisotropic}, {@code textureClearCoat}, {@code textureClearCoatGloss},
	 *                              {@code textureDiffuseTransmission}, {@code textureEta}, {@code textureFlatness}, {@code textureMetallic}, {@code textureRoughness} or {@code textureSheen} are {@code null}
	 */
	public DisneyMaterial(final Texture textureColor, final Texture textureEmission, final Texture textureScatterDistance, final Texture textureAnisotropic, final Texture textureClearCoat, final Texture textureClearCoatGloss, final Texture textureDiffuseTransmission, final Texture textureEta, final Texture textureFlatness, final Texture textureMetallic, final Texture textureRoughness, final Texture textureSheen) {
		this(textureColor, textureEmission, textureScatterDistance, textureAnisotropic, textureClearCoat, textureClearCoatGloss, textureDiffuseTransmission, textureEta, textureFlatness, textureMetallic, textureRoughness, textureSheen, ConstantTexture.GRAY_0_50);
	}
	
	/**
	 * Constructs a new {@code DisneyMaterial} instance.
	 * <p>
	 * If either {@code textureColor}, {@code textureEmission}, {@code textureScatterDistance}, {@code textureAnisotropic}, {@code textureClearCoat}, {@code textureClearCoatGloss}, {@code textureDiffuseTransmission}, {@code textureEta},
	 * {@code textureFlatness}, {@code textureMetallic}, {@code textureRoughness}, {@code textureSheen} or {@code textureSheenTint} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new DisneyMaterial(textureColor, textureEmission, textureScatterDistance, textureAnisotropic, textureClearCoat, textureClearCoatGloss, textureDiffuseTransmission, textureEta, textureFlatness, textureMetallic, textureRoughness, textureSheen, textureSheenTint, ConstantTexture.BLACK);
	 * }
	 * </pre>
	 * 
	 * @param textureColor a {@link Texture} instance for the color
	 * @param textureEmission a {@code Texture} instance for emission
	 * @param textureScatterDistance a {@code Texture} instance for the scatter distance
	 * @param textureAnisotropic a {@code Texture} instance for the anisotropic value
	 * @param textureClearCoat a {@code Texture} instance for the clear coat value
	 * @param textureClearCoatGloss a {@code Texture} instance for the clear coat gloss value
	 * @param textureDiffuseTransmission a {@code Texture} instance for the diffuse transmission
	 * @param textureEta a {@code Texture} instance for the index of refraction (IOR)
	 * @param textureFlatness a {@code Texture} instance for the flatness
	 * @param textureMetallic a {@code Texture} instance for the metallic value
	 * @param textureRoughness a {@code Texture} instance for the roughness
	 * @param textureSheen a {@code Texture} instance for the sheen value
	 * @param textureSheenTint a {@code Texture} instance for the sheen tint value
	 * @throws NullPointerException thrown if, and only if, either {@code textureColor}, {@code textureEmission}, {@code textureScatterDistance}, {@code textureAnisotropic}, {@code textureClearCoat}, {@code textureClearCoatGloss},
	 *                              {@code textureDiffuseTransmission}, {@code textureEta}, {@code textureFlatness}, {@code textureMetallic}, {@code textureRoughness}, {@code textureSheen} or {@code textureSheenTint} are {@code null}
	 */
	public DisneyMaterial(final Texture textureColor, final Texture textureEmission, final Texture textureScatterDistance, final Texture textureAnisotropic, final Texture textureClearCoat, final Texture textureClearCoatGloss, final Texture textureDiffuseTransmission, final Texture textureEta, final Texture textureFlatness, final Texture textureMetallic, final Texture textureRoughness, final Texture textureSheen, final Texture textureSheenTint) {
		this(textureColor, textureEmission, textureScatterDistance, textureAnisotropic, textureClearCoat, textureClearCoatGloss, textureDiffuseTransmission, textureEta, textureFlatness, textureMetallic, textureRoughness, textureSheen, textureSheenTint, ConstantTexture.BLACK);
	}
	
	/**
	 * Constructs a new {@code DisneyMaterial} instance.
	 * <p>
	 * If either {@code textureColor}, {@code textureEmission}, {@code textureScatterDistance}, {@code textureAnisotropic}, {@code textureClearCoat}, {@code textureClearCoatGloss}, {@code textureDiffuseTransmission}, {@code textureEta},
	 * {@code textureFlatness}, {@code textureMetallic}, {@code textureRoughness}, {@code textureSheen}, {@code textureSheenTint} or {@code textureSpecularTint} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new DisneyMaterial(textureColor, textureEmission, textureScatterDistance, textureAnisotropic, textureClearCoat, textureClearCoatGloss, textureDiffuseTransmission, textureEta, textureFlatness, textureMetallic, textureRoughness, textureSheen, textureSheenTint, textureSpecularTint, ConstantTexture.BLACK);
	 * }
	 * </pre>
	 * 
	 * @param textureColor a {@link Texture} instance for the color
	 * @param textureEmission a {@code Texture} instance for emission
	 * @param textureScatterDistance a {@code Texture} instance for the scatter distance
	 * @param textureAnisotropic a {@code Texture} instance for the anisotropic value
	 * @param textureClearCoat a {@code Texture} instance for the clear coat value
	 * @param textureClearCoatGloss a {@code Texture} instance for the clear coat gloss value
	 * @param textureDiffuseTransmission a {@code Texture} instance for the diffuse transmission
	 * @param textureEta a {@code Texture} instance for the index of refraction (IOR)
	 * @param textureFlatness a {@code Texture} instance for the flatness
	 * @param textureMetallic a {@code Texture} instance for the metallic value
	 * @param textureRoughness a {@code Texture} instance for the roughness
	 * @param textureSheen a {@code Texture} instance for the sheen value
	 * @param textureSheenTint a {@code Texture} instance for the sheen tint value
	 * @param textureSpecularTint a {@code Texture} instance for the specular tint
	 * @throws NullPointerException thrown if, and only if, either {@code textureColor}, {@code textureEmission}, {@code textureScatterDistance}, {@code textureAnisotropic}, {@code textureClearCoat}, {@code textureClearCoatGloss},
	 *                              {@code textureDiffuseTransmission}, {@code textureEta}, {@code textureFlatness}, {@code textureMetallic}, {@code textureRoughness}, {@code textureSheen}, {@code textureSheenTint} or {@code textureSpecularTint} are
	 *                              {@code null}
	 */
	public DisneyMaterial(final Texture textureColor, final Texture textureEmission, final Texture textureScatterDistance, final Texture textureAnisotropic, final Texture textureClearCoat, final Texture textureClearCoatGloss, final Texture textureDiffuseTransmission, final Texture textureEta, final Texture textureFlatness, final Texture textureMetallic, final Texture textureRoughness, final Texture textureSheen, final Texture textureSheenTint, final Texture textureSpecularTint) {
		this(textureColor, textureEmission, textureScatterDistance, textureAnisotropic, textureClearCoat, textureClearCoatGloss, textureDiffuseTransmission, textureEta, textureFlatness, textureMetallic, textureRoughness, textureSheen, textureSheenTint, textureSpecularTint, ConstantTexture.BLACK);
	}
	
	/**
	 * Constructs a new {@code DisneyMaterial} instance.
	 * <p>
	 * If either {@code textureColor}, {@code textureEmission}, {@code textureScatterDistance}, {@code textureAnisotropic}, {@code textureClearCoat}, {@code textureClearCoatGloss}, {@code textureDiffuseTransmission}, {@code textureEta},
	 * {@code textureFlatness}, {@code textureMetallic}, {@code textureRoughness}, {@code textureSheen}, {@code textureSheenTint}, {@code textureSpecularTint} or {@code textureSpecularTransmission} are {@code null}, a {@code NullPointerException} will
	 * be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new DisneyMaterial(textureColor, textureEmission, textureScatterDistance, textureAnisotropic, textureClearCoat, textureClearCoatGloss, textureDiffuseTransmission, textureEta, textureFlatness, textureMetallic, textureRoughness, textureSheen, textureSheenTint, textureSpecularTint, textureSpecularTransmission, false);
	 * }
	 * </pre>
	 * 
	 * @param textureColor a {@link Texture} instance for the color
	 * @param textureEmission a {@code Texture} instance for emission
	 * @param textureScatterDistance a {@code Texture} instance for the scatter distance
	 * @param textureAnisotropic a {@code Texture} instance for the anisotropic value
	 * @param textureClearCoat a {@code Texture} instance for the clear coat value
	 * @param textureClearCoatGloss a {@code Texture} instance for the clear coat gloss value
	 * @param textureDiffuseTransmission a {@code Texture} instance for the diffuse transmission
	 * @param textureEta a {@code Texture} instance for the index of refraction (IOR)
	 * @param textureFlatness a {@code Texture} instance for the flatness
	 * @param textureMetallic a {@code Texture} instance for the metallic value
	 * @param textureRoughness a {@code Texture} instance for the roughness
	 * @param textureSheen a {@code Texture} instance for the sheen value
	 * @param textureSheenTint a {@code Texture} instance for the sheen tint value
	 * @param textureSpecularTint a {@code Texture} instance for the specular tint
	 * @param textureSpecularTransmission a {@code Texture} instance for the specular transmission
	 * @throws NullPointerException thrown if, and only if, either {@code textureColor}, {@code textureEmission}, {@code textureScatterDistance}, {@code textureAnisotropic}, {@code textureClearCoat}, {@code textureClearCoatGloss},
	 *                              {@code textureDiffuseTransmission}, {@code textureEta}, {@code textureFlatness}, {@code textureMetallic}, {@code textureRoughness}, {@code textureSheen}, {@code textureSheenTint}, {@code textureSpecularTint} or
	 *                              {@code textureSpecularTransmission} are {@code null}
	 */
	public DisneyMaterial(final Texture textureColor, final Texture textureEmission, final Texture textureScatterDistance, final Texture textureAnisotropic, final Texture textureClearCoat, final Texture textureClearCoatGloss, final Texture textureDiffuseTransmission, final Texture textureEta, final Texture textureFlatness, final Texture textureMetallic, final Texture textureRoughness, final Texture textureSheen, final Texture textureSheenTint, final Texture textureSpecularTint, final Texture textureSpecularTransmission) {
		this(textureColor, textureEmission, textureScatterDistance, textureAnisotropic, textureClearCoat, textureClearCoatGloss, textureDiffuseTransmission, textureEta, textureFlatness, textureMetallic, textureRoughness, textureSheen, textureSheenTint, textureSpecularTint, textureSpecularTransmission, false);
	}
	
	/**
	 * Constructs a new {@code DisneyMaterial} instance.
	 * <p>
	 * If either {@code textureColor}, {@code textureEmission}, {@code textureScatterDistance}, {@code textureAnisotropic}, {@code textureClearCoat}, {@code textureClearCoatGloss}, {@code textureDiffuseTransmission}, {@code textureEta},
	 * {@code textureFlatness}, {@code textureMetallic}, {@code textureRoughness}, {@code textureSheen}, {@code textureSheenTint}, {@code textureSpecularTint} or {@code textureSpecularTransmission} are {@code null}, a {@code NullPointerException} will
	 * be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new DisneyMaterial(textureColor, textureEmission, textureScatterDistance, textureAnisotropic, textureClearCoat, textureClearCoatGloss, textureDiffuseTransmission, textureEta, textureFlatness, textureMetallic, textureRoughness, textureSheen, textureSheenTint, textureSpecularTint, textureSpecularTransmission, isThin, new NoOpModifier());
	 * }
	 * </pre>
	 * 
	 * @param textureColor a {@link Texture} instance for the color
	 * @param textureEmission a {@code Texture} instance for emission
	 * @param textureScatterDistance a {@code Texture} instance for the scatter distance
	 * @param textureAnisotropic a {@code Texture} instance for the anisotropic value
	 * @param textureClearCoat a {@code Texture} instance for the clear coat value
	 * @param textureClearCoatGloss a {@code Texture} instance for the clear coat gloss value
	 * @param textureDiffuseTransmission a {@code Texture} instance for the diffuse transmission
	 * @param textureEta a {@code Texture} instance for the index of refraction (IOR)
	 * @param textureFlatness a {@code Texture} instance for the flatness
	 * @param textureMetallic a {@code Texture} instance for the metallic value
	 * @param textureRoughness a {@code Texture} instance for the roughness
	 * @param textureSheen a {@code Texture} instance for the sheen value
	 * @param textureSheenTint a {@code Texture} instance for the sheen tint value
	 * @param textureSpecularTint a {@code Texture} instance for the specular tint
	 * @param textureSpecularTransmission a {@code Texture} instance for the specular transmission
	 * @param isThin {@code true} if, and only if, this {@code DisneyMaterial} instance is thin, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, either {@code textureColor}, {@code textureEmission}, {@code textureScatterDistance}, {@code textureAnisotropic}, {@code textureClearCoat}, {@code textureClearCoatGloss},
	 *                              {@code textureDiffuseTransmission}, {@code textureEta}, {@code textureFlatness}, {@code textureMetallic}, {@code textureRoughness}, {@code textureSheen}, {@code textureSheenTint}, {@code textureSpecularTint} or
	 *                              {@code textureSpecularTransmission} are {@code null}
	 */
	public DisneyMaterial(final Texture textureColor, final Texture textureEmission, final Texture textureScatterDistance, final Texture textureAnisotropic, final Texture textureClearCoat, final Texture textureClearCoatGloss, final Texture textureDiffuseTransmission, final Texture textureEta, final Texture textureFlatness, final Texture textureMetallic, final Texture textureRoughness, final Texture textureSheen, final Texture textureSheenTint, final Texture textureSpecularTint, final Texture textureSpecularTransmission, final boolean isThin) {
		this(textureColor, textureEmission, textureScatterDistance, textureAnisotropic, textureClearCoat, textureClearCoatGloss, textureDiffuseTransmission, textureEta, textureFlatness, textureMetallic, textureRoughness, textureSheen, textureSheenTint, textureSpecularTint, textureSpecularTransmission, isThin, new NoOpModifier());
	}
	
	/**
	 * Constructs a new {@code DisneyMaterial} instance.
	 * <p>
	 * If either {@code textureColor}, {@code textureEmission}, {@code textureScatterDistance}, {@code textureAnisotropic}, {@code textureClearCoat}, {@code textureClearCoatGloss}, {@code textureDiffuseTransmission}, {@code textureEta},
	 * {@code textureFlatness}, {@code textureMetallic}, {@code textureRoughness}, {@code textureSheen}, {@code textureSheenTint}, {@code textureSpecularTint}, {@code textureSpecularTransmission} or {@code modifier} are {@code null}, a
	 * {@code NullPointerException} will be thrown.
	 * 
	 * @param textureColor a {@link Texture} instance for the color
	 * @param textureEmission a {@code Texture} instance for emission
	 * @param textureScatterDistance a {@code Texture} instance for the scatter distance
	 * @param textureAnisotropic a {@code Texture} instance for the anisotropic value
	 * @param textureClearCoat a {@code Texture} instance for the clear coat value
	 * @param textureClearCoatGloss a {@code Texture} instance for the clear coat gloss value
	 * @param textureDiffuseTransmission a {@code Texture} instance for the diffuse transmission
	 * @param textureEta a {@code Texture} instance for the index of refraction (IOR)
	 * @param textureFlatness a {@code Texture} instance for the flatness
	 * @param textureMetallic a {@code Texture} instance for the metallic value
	 * @param textureRoughness a {@code Texture} instance for the roughness
	 * @param textureSheen a {@code Texture} instance for the sheen value
	 * @param textureSheenTint a {@code Texture} instance for the sheen tint value
	 * @param textureSpecularTint a {@code Texture} instance for the specular tint
	 * @param textureSpecularTransmission a {@code Texture} instance for the specular transmission
	 * @param isThin {@code true} if, and only if, this {@code DisneyMaterial} instance is thin, {@code false} otherwise
	 * @param modifier a {@link Modifier} instance
	 * @throws NullPointerException thrown if, and only if, either {@code textureColor}, {@code textureEmission}, {@code textureScatterDistance}, {@code textureAnisotropic}, {@code textureClearCoat}, {@code textureClearCoatGloss},
	 *                              {@code textureDiffuseTransmission}, {@code textureEta}, {@code textureFlatness}, {@code textureMetallic}, {@code textureRoughness}, {@code textureSheen}, {@code textureSheenTint}, {@code textureSpecularTint},
	 *                              {@code textureSpecularTransmission} or {@code modifier} are {@code null}
	 */
	public DisneyMaterial(final Texture textureColor, final Texture textureEmission, final Texture textureScatterDistance, final Texture textureAnisotropic, final Texture textureClearCoat, final Texture textureClearCoatGloss, final Texture textureDiffuseTransmission, final Texture textureEta, final Texture textureFlatness, final Texture textureMetallic, final Texture textureRoughness, final Texture textureSheen, final Texture textureSheenTint, final Texture textureSpecularTint, final Texture textureSpecularTransmission, final boolean isThin, final Modifier modifier) {
		this.textureColor = Objects.requireNonNull(textureColor, "textureColor == null");
		this.textureEmission = Objects.requireNonNull(textureEmission, "textureEmission == null");
		this.textureScatterDistance = Objects.requireNonNull(textureScatterDistance, "textureScatterDistance == null");
		this.textureAnisotropic = Objects.requireNonNull(textureAnisotropic, "textureAnisotropic == null");
		this.textureClearCoat = Objects.requireNonNull(textureClearCoat, "textureClearCoat == null");
		this.textureClearCoatGloss = Objects.requireNonNull(textureClearCoatGloss, "textureClearCoatGloss == null");
		this.textureDiffuseTransmission = Objects.requireNonNull(textureDiffuseTransmission, "textureDiffuseTransmission == null");
		this.textureEta = Objects.requireNonNull(textureEta, "textureEta == null");
		this.textureFlatness = Objects.requireNonNull(textureFlatness, "textureFlatness == null");
		this.textureMetallic = Objects.requireNonNull(textureMetallic, "textureMetallic == null");
		this.textureRoughness = Objects.requireNonNull(textureRoughness, "textureRoughness == null");
		this.textureSheen = Objects.requireNonNull(textureSheen, "textureSheen == null");
		this.textureSheenTint = Objects.requireNonNull(textureSheenTint, "textureSheenTint == null");
		this.textureSpecularTint = Objects.requireNonNull(textureSpecularTint, "textureSpecularTint == null");
		this.textureSpecularTransmission = Objects.requireNonNull(textureSpecularTransmission, "textureSpecularTransmission == null");
		this.isThin = isThin;
		this.modifier = Objects.requireNonNull(modifier, "modifier == null");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link Color3F} instance with the emittance of this {@code DisneyMaterial} instance at {@code intersection}.
	 * <p>
	 * If {@code intersection} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param intersection an {@link Intersection} instance
	 * @return a {@code Color3F} instance with the emittance of this {@code DisneyMaterial} instance at {@code intersection}
	 * @throws NullPointerException thrown if, and only if, {@code intersection} is {@code null}
	 */
	@Override
	public Color3F emittance(final Intersection intersection) {
		Objects.requireNonNull(intersection, "intersection == null");
		
		return this.textureEmission.getColor(intersection);
	}
	
	/**
	 * Returns the {@link Modifier} instance.
	 * 
	 * @return the {@code Modifier} instance
	 */
	public Modifier getModifier() {
		return this.modifier;
	}
	
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
		
		this.modifier.modify(intersection);
		
		final List<BXDF> bXDFs = new ArrayList<>();
		
		final float floatAnisotropic = this.textureAnisotropic.getFloat(intersection);
		final float floatClearCoat = this.textureClearCoat.getFloat(intersection);
		final float floatDiffuseTransmission = this.textureDiffuseTransmission.getFloat(intersection) / 2.0F;
		final float floatEta = this.textureEta.getFloat(intersection);
		final float floatMetallic = this.textureMetallic.getFloat(intersection);
		final float floatRoughness = this.textureRoughness.getFloat(intersection);
		final float floatSheen = this.textureSheen.getFloat(intersection);
		final float floatSpecularTint = this.textureSpecularTint.getFloat(intersection);
		final float floatSpecularTransmission = this.textureSpecularTransmission.getFloat(intersection);
		
		final Color3F colorColor = Color3F.saturate(this.textureColor.getColor(intersection), 0.0F, MAX_VALUE);
		final Color3F colorTint = Color3F.normalizeRelativeLuminance(colorColor);
		final Color3F colorSheen = floatSheen > 0.0F ? Color3F.blend(Color3F.WHITE, colorTint, this.textureSheenTint.getFloat(intersection)) : Color3F.BLACK;
		
		final float diffuseWeight = (1.0F - floatMetallic) * (1.0F - floatSpecularTransmission);
		
		if(diffuseWeight > 0.0F) {
			if(this.isThin) {
				final float floatFlatness = this.textureFlatness.getFloat(intersection);
				
				final Color3F colorReflectanceScale0 = Color3F.multiply(colorColor, diffuseWeight * (1.0F - floatFlatness) * (1.0F - floatDiffuseTransmission));
				final Color3F colorReflectanceScale1 = Color3F.multiply(colorColor, diffuseWeight * (0.0F + floatFlatness) * (1.0F - floatDiffuseTransmission));
				
				bXDFs.add(new DisneyDiffuseBRDF(colorReflectanceScale0));
				bXDFs.add(new DisneyFakeSSBRDF(colorReflectanceScale1, floatRoughness));
			} else {
				final Color3F colorScatterDistance = this.textureScatterDistance.getColor(intersection);
				
				if(colorScatterDistance.isBlack()) {
					bXDFs.add(new DisneyDiffuseBRDF(Color3F.multiply(colorColor, diffuseWeight)));
				} else {
					bXDFs.add(new SpecularBTDF(Color3F.WHITE, transportMode, 1.0F, floatEta));
				}
			}
			
			bXDFs.add(new DisneyRetroBRDF(Color3F.multiply(colorColor, diffuseWeight), floatRoughness));
			
			if(floatSheen > 0.0F) {
				bXDFs.add(new DisneySheenBRDF(Color3F.multiply(colorSheen, diffuseWeight * floatSheen)));
			}
		}
		
		final float aspect = sqrt(1.0F - floatAnisotropic * 0.9F);
		
		final float alphaX = max(0.001F, floatRoughness * floatRoughness / aspect);
		final float alphaY = max(0.001F, floatRoughness * floatRoughness * aspect);
		
		final MicrofacetDistribution microfacetDistribution = new TrowbridgeReitzMicrofacetDistribution(true, true, alphaX, alphaY);
		
		final float floatR0 = ((floatEta - 1.0F) * (floatEta - 1.0F)) / ((floatEta + 1.0F) * (floatEta + 1.0F));
		
		final Color3F colorSpecularR0 = Color3F.blend(Color3F.multiply(Color3F.blend(Color3F.WHITE, colorTint, floatSpecularTint), floatR0), colorColor, floatMetallic);
		
		final Fresnel fresnel = new DisneyFresnel(colorSpecularR0, floatEta, floatMetallic);
		
		bXDFs.add(new TorranceSparrowBRDF(Color3F.WHITE, fresnel, microfacetDistribution));
		
		if(floatClearCoat > 0.0F) {
			bXDFs.add(new DisneyClearCoatBRDF(lerp(0.1F, 0.001F, this.textureClearCoatGloss.getFloat(intersection)), floatClearCoat));
		}
		
		if(floatSpecularTransmission > 0.0F) {
			final Color3F transmittanceScale = Color3F.multiply(Color3F.sqrt(colorColor), floatSpecularTransmission);
			
			if(this.isThin) {
				final float floatRoughnessScaled = (0.65F * floatEta - 0.35F) * floatRoughness;
				
				final float alphaXScaled = max(0.001F, floatRoughnessScaled * floatRoughnessScaled / aspect);
				final float alphaYScaled = max(0.001F, floatRoughnessScaled * floatRoughnessScaled * aspect);
				
				final MicrofacetDistribution microfacetDistributionScaled = new TrowbridgeReitzMicrofacetDistribution(true, false, alphaXScaled, alphaYScaled);
				
				bXDFs.add(new TorranceSparrowBTDF(transmittanceScale, microfacetDistributionScaled, transportMode, 1.0F, floatEta));
			} else {
				bXDFs.add(new TorranceSparrowBTDF(transmittanceScale, microfacetDistribution, transportMode, 1.0F, floatEta));
			}
		}
		
		if(this.isThin) {
			bXDFs.add(new LambertianBTDF(Color3F.multiply(colorColor, floatDiffuseTransmission)));
		}
		
		return Optional.of(new BSDF(intersection, bXDFs));
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
		
		final float floatMetallic = this.textureMetallic.getFloat(intersection);
		final float floatSpecularTransmission = this.textureSpecularTransmission.getFloat(intersection);
		
		final float diffuseWeight = (1.0F - floatMetallic) * (1.0F - floatSpecularTransmission);
		
		if(diffuseWeight > 0.0F && !this.isThin) {
			final Color3F colorScatterDistance = this.textureScatterDistance.getColor(intersection);
			
			if(!colorScatterDistance.isBlack()) {
//				final Color3F colorColor = Color3F.saturate(this.textureColor.getColor(intersection), 0.0F, MAX_VALUE);
				
//				final float floatEta = this.textureEta.getFloat(intersection);
				
//				TODO: Add support for BSSRDF.
			}
		}
		
		return Optional.empty();
	}
	
	/**
	 * Returns a {@code String} with the name of this {@code DisneyMaterial} instance.
	 * 
	 * @return a {@code String} with the name of this {@code DisneyMaterial} instance
	 */
	@Override
	public String getName() {
		return NAME;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code DisneyMaterial} instance.
	 * 
	 * @return a {@code String} representation of this {@code DisneyMaterial} instance
	 */
	@Override
	public String toString() {
		return String.format("new DisneyMaterial(%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)", this.textureColor, this.textureEmission, this.textureScatterDistance, this.textureAnisotropic, this.textureClearCoat, this.textureClearCoatGloss, this.textureDiffuseTransmission, this.textureEta, this.textureFlatness, this.textureMetallic, this.textureRoughness, this.textureSheen, this.textureSheenTint, this.textureSpecularTint, this.textureSpecularTransmission, Boolean.toString(this.isThin), this.modifier);
	}
	
	/**
	 * Returns the {@link Texture} instance for the anisotropic value.
	 * 
	 * @return the {@code Texture} instance for the anisotropic value
	 */
	public Texture getTextureAnisotropic() {
		return this.textureAnisotropic;
	}
	
	/**
	 * Returns the {@link Texture} instance for the clear coat value.
	 * 
	 * @return the {@code Texture} instance for the clear coat value
	 */
	public Texture getTextureClearCoat() {
		return this.textureClearCoat;
	}
	
	/**
	 * Returns the {@link Texture} instance for the clear coat gloss value.
	 * 
	 * @return the {@code Texture} instance for the clear coat gloss value
	 */
	public Texture getTextureClearCoatGloss() {
		return this.textureClearCoatGloss;
	}
	
	/**
	 * Returns the {@link Texture} instance for the color.
	 * 
	 * @return the {@code Texture} instance for the color
	 */
	public Texture getTextureColor() {
		return this.textureColor;
	}
	
	/**
	 * Returns the {@link Texture} instance for the diffuse transmission.
	 * 
	 * @return the {@code Texture} instance for the diffuse transmission
	 */
	public Texture getTextureDiffuseTransmission() {
		return this.textureDiffuseTransmission;
	}
	
	/**
	 * Returns the {@link Texture} instance for emission.
	 * 
	 * @return the {@code Texture} instance for emission
	 */
	public Texture getTextureEmission() {
		return this.textureEmission;
	}
	
	/**
	 * Returns the {@link Texture} instance for the index of refraction (IOR).
	 * 
	 * @return the {@code Texture} instance for the index of refraction (IOR)
	 */
	public Texture getTextureEta() {
		return this.textureEta;
	}
	
	/**
	 * Returns the {@link Texture} instance for the flatness.
	 * 
	 * @return the {@code Texture} instance for the flatness
	 */
	public Texture getTextureFlatness() {
		return this.textureFlatness;
	}
	
	/**
	 * Returns the {@link Texture} instance for the metallic value.
	 * 
	 * @return the {@code Texture} instance for the metallic value
	 */
	public Texture getTextureMetallic() {
		return this.textureMetallic;
	}
	
	/**
	 * Returns the {@link Texture} instance for the roughness.
	 * 
	 * @return the {@code Texture} instance for the roughness
	 */
	public Texture getTextureRoughness() {
		return this.textureRoughness;
	}
	
	/**
	 * Returns the {@link Texture} instance for the scatter distance.
	 * 
	 * @return the {@code Texture} instance for the scatter distance
	 */
	public Texture getTextureScatterDistance() {
		return this.textureScatterDistance;
	}
	
	/**
	 * Returns the {@link Texture} instance for the sheen value.
	 * 
	 * @return the {@code Texture} instance for the sheen value
	 */
	public Texture getTextureSheen() {
		return this.textureSheen;
	}
	
	/**
	 * Returns the {@link Texture} instance for the sheen tint value.
	 * 
	 * @return the {@code Texture} instance for the sheen tint value
	 */
	public Texture getTextureSheenTint() {
		return this.textureSheenTint;
	}
	
	/**
	 * Returns the {@link Texture} instance for the specular tint.
	 * 
	 * @return the {@code Texture} instance for the specular tint
	 */
	public Texture getTextureSpecularTint() {
		return this.textureSpecularTint;
	}
	
	/**
	 * Returns the {@link Texture} instance for the specular transmission.
	 * 
	 * @return the {@code Texture} instance for the specular transmission
	 */
	public Texture getTextureSpecularTransmission() {
		return this.textureSpecularTransmission;
	}
	
	/**
	 * Accepts a {@link NodeHierarchicalVisitor}.
	 * <p>
	 * Returns the result of {@code nodeHierarchicalVisitor.visitLeave(this)}.
	 * <p>
	 * If {@code nodeHierarchicalVisitor} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If a {@code RuntimeException} is thrown by the current {@code NodeHierarchicalVisitor}, a {@code NodeTraversalException} will be thrown with the {@code RuntimeException} wrapped.
	 * <p>
	 * This implementation will:
	 * <ul>
	 * <li>throw a {@code NullPointerException} if {@code nodeHierarchicalVisitor} is {@code null}.</li>
	 * <li>throw a {@code NodeTraversalException} if {@code nodeHierarchicalVisitor} throws a {@code RuntimeException}.</li>
	 * <li>traverse its child {@code Node} instances.</li>
	 * </ul>
	 * 
	 * @param nodeHierarchicalVisitor the {@code NodeHierarchicalVisitor} to accept
	 * @return the result of {@code nodeHierarchicalVisitor.visitLeave(this)}
	 * @throws NodeTraversalException thrown if, and only if, a {@code RuntimeException} is thrown by the current {@code NodeHierarchicalVisitor}
	 * @throws NullPointerException thrown if, and only if, {@code nodeHierarchicalVisitor} is {@code null}
	 */
	@Override
	public boolean accept(final NodeHierarchicalVisitor nodeHierarchicalVisitor) {
		Objects.requireNonNull(nodeHierarchicalVisitor, "nodeHierarchicalVisitor == null");
		
		try {
			if(nodeHierarchicalVisitor.visitEnter(this)) {
				if(!this.modifier.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
				
				if(!this.textureAnisotropic.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
				
				if(!this.textureClearCoat.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
				
				if(!this.textureClearCoatGloss.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
				
				if(!this.textureColor.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
				
				if(!this.textureDiffuseTransmission.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
				
				if(!this.textureEmission.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
				
				if(!this.textureEta.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
				
				if(!this.textureFlatness.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
				
				if(!this.textureMetallic.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
				
				if(!this.textureRoughness.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
				
				if(!this.textureScatterDistance.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
				
				if(!this.textureSheen.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
				
				if(!this.textureSheenTint.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
				
				if(!this.textureSpecularTint.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
				
				if(!this.textureSpecularTransmission.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
			}
			
			return nodeHierarchicalVisitor.visitLeave(this);
		} catch(final RuntimeException e) {
			throw new NodeTraversalException(e);
		}
	}
	
	/**
	 * Compares {@code object} to this {@code DisneyMaterial} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code DisneyMaterial}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code DisneyMaterial} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code DisneyMaterial}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof DisneyMaterial)) {
			return false;
		} else if(!Objects.equals(this.modifier, DisneyMaterial.class.cast(object).modifier)) {
			return false;
		} else if(!Objects.equals(this.textureAnisotropic, DisneyMaterial.class.cast(object).textureAnisotropic)) {
			return false;
		} else if(!Objects.equals(this.textureClearCoat, DisneyMaterial.class.cast(object).textureClearCoat)) {
			return false;
		} else if(!Objects.equals(this.textureClearCoatGloss, DisneyMaterial.class.cast(object).textureClearCoatGloss)) {
			return false;
		} else if(!Objects.equals(this.textureColor, DisneyMaterial.class.cast(object).textureColor)) {
			return false;
		} else if(!Objects.equals(this.textureDiffuseTransmission, DisneyMaterial.class.cast(object).textureDiffuseTransmission)) {
			return false;
		} else if(!Objects.equals(this.textureEmission, DisneyMaterial.class.cast(object).textureEmission)) {
			return false;
		} else if(!Objects.equals(this.textureEta, DisneyMaterial.class.cast(object).textureEta)) {
			return false;
		} else if(!Objects.equals(this.textureFlatness, DisneyMaterial.class.cast(object).textureFlatness)) {
			return false;
		} else if(!Objects.equals(this.textureMetallic, DisneyMaterial.class.cast(object).textureMetallic)) {
			return false;
		} else if(!Objects.equals(this.textureRoughness, DisneyMaterial.class.cast(object).textureRoughness)) {
			return false;
		} else if(!Objects.equals(this.textureScatterDistance, DisneyMaterial.class.cast(object).textureScatterDistance)) {
			return false;
		} else if(!Objects.equals(this.textureSheen, DisneyMaterial.class.cast(object).textureSheen)) {
			return false;
		} else if(!Objects.equals(this.textureSheenTint, DisneyMaterial.class.cast(object).textureSheenTint)) {
			return false;
		} else if(!Objects.equals(this.textureSpecularTint, DisneyMaterial.class.cast(object).textureSpecularTint)) {
			return false;
		} else if(!Objects.equals(this.textureSpecularTransmission, DisneyMaterial.class.cast(object).textureSpecularTransmission)) {
			return false;
		} else if(this.isThin != DisneyMaterial.class.cast(object).isThin) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code DisneyMaterial} instance is thin, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, this {@code DisneyMaterial} instance is thin, {@code false} otherwise
	 */
	public boolean isThin() {
		return this.isThin;
	}
	
	/**
	 * Returns an {@code int} with the ID of this {@code DisneyMaterial} instance.
	 * 
	 * @return an {@code int} with the ID of this {@code DisneyMaterial} instance
	 */
	@Override
	public int getID() {
		return ID;
	}
	
	/**
	 * Returns a hash code for this {@code DisneyMaterial} instance.
	 * 
	 * @return a hash code for this {@code DisneyMaterial} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.modifier, this.textureAnisotropic, this.textureClearCoat, this.textureClearCoatGloss, this.textureColor, this.textureDiffuseTransmission, this.textureEmission, this.textureEta, this.textureFlatness, this.textureMetallic, this.textureRoughness, this.textureScatterDistance, this.textureSheen, this.textureSheenTint, this.textureSpecularTint, this.textureSpecularTransmission, Boolean.valueOf(this.isThin));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * A {@code Builder} is used to build {@link DisneyMaterial} instances.
	 * <p>
	 * This class is mutable and not thread-safe.
	 * 
	 * @since 1.0.0
	 * @author J&#246;rgen Lundgren
	 */
	public static final class Builder {
		private Modifier modifier;
		private Texture textureAnisotropic;
		private Texture textureClearCoat;
		private Texture textureClearCoatGloss;
		private Texture textureColor;
		private Texture textureDiffuseTransmission;
		private Texture textureEmission;
		private Texture textureEta;
		private Texture textureFlatness;
		private Texture textureMetallic;
		private Texture textureRoughness;
		private Texture textureScatterDistance;
		private Texture textureSheen;
		private Texture textureSheenTint;
		private Texture textureSpecularTint;
		private Texture textureSpecularTransmission;
		private boolean isThin;
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		/**
		 * Constructs a new {@code Builder} instance.
		 */
		public Builder() {
			this(new DisneyMaterial());
		}
		
		/**
		 * Constructs a new {@code Builder} instance given {@code disneyMaterial}.
		 * <p>
		 * If {@code disneyMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
		 * 
		 * @param disneyMaterial a {@link DisneyMaterial} instance
		 * @throws NullPointerException thrown if, and only if, {@code disneyMaterial} is {@code null}
		 */
		public Builder(final DisneyMaterial disneyMaterial) {
			this.modifier = disneyMaterial.getModifier();
			this.textureAnisotropic = disneyMaterial.getTextureAnisotropic();
			this.textureClearCoat = disneyMaterial.getTextureClearCoat();
			this.textureClearCoatGloss = disneyMaterial.getTextureClearCoatGloss();
			this.textureColor = disneyMaterial.getTextureColor();
			this.textureDiffuseTransmission = disneyMaterial.getTextureDiffuseTransmission();
			this.textureEmission = disneyMaterial.getTextureEmission();
			this.textureEta = disneyMaterial.getTextureEta();
			this.textureFlatness = disneyMaterial.getTextureFlatness();
			this.textureMetallic = disneyMaterial.getTextureMetallic();
			this.textureRoughness = disneyMaterial.getTextureRoughness();
			this.textureScatterDistance = disneyMaterial.getTextureScatterDistance();
			this.textureSheen = disneyMaterial.getTextureSheen();
			this.textureSheenTint = disneyMaterial.getTextureSheenTint();
			this.textureSpecularTint = disneyMaterial.getTextureSpecularTint();
			this.textureSpecularTransmission = disneyMaterial.getTextureSpecularTransmission();
			this.isThin = disneyMaterial.isThin();
		}
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		/**
		 * Sets the {@link Modifier} instance.
		 * <p>
		 * Returns this {@code Builder} instance.
		 * <p>
		 * If {@code modifier} is {@code null}, a {@code NullPointerException} will be thrown.
		 * 
		 * @param modifier the {@code Modifier} instance
		 * @return this {@code Builder} instance
		 * @throws NullPointerException thrown if, and only if, {@code modifier} is {@code null}
		 */
		public Builder setModifier(final Modifier modifier) {
			this.modifier = Objects.requireNonNull(modifier, "modifier == null");
			
			return this;
		}
		
		/**
		 * Sets the {@link Texture} instance for the anisotropic value.
		 * <p>
		 * Returns this {@code Builder} instance.
		 * <p>
		 * If {@code textureAnisotropic} is {@code null}, a {@code NullPointerException} will be thrown.
		 * 
		 * @param textureAnisotropic the {@code Texture} instance for the anisotropic value
		 * @return this {@code Builder} instance
		 * @throws NullPointerException thrown if, and only if, {@code textureAnisotropic} is {@code null}
		 */
		public Builder setTextureAnisotropic(final Texture textureAnisotropic) {
			this.textureAnisotropic = Objects.requireNonNull(textureAnisotropic, "textureAnisotropic == null");
			
			return this;
		}
		
		/**
		 * Sets the {@link Texture} instance for the anisotropic value.
		 * <p>
		 * Returns this {@code Builder} instance.
		 * 
		 * @param floatAnisotropic a {@code float} for the anisotropic value
		 * @return this {@code Builder} instance
		 */
		public Builder setTextureAnisotropic(final float floatAnisotropic) {
			return setTextureAnisotropic(new ConstantTexture(floatAnisotropic));
		}
		
		/**
		 * Sets the {@link Texture} instance for the clear coat value.
		 * <p>
		 * Returns this {@code Builder} instance.
		 * <p>
		 * If {@code textureClearCoat} is {@code null}, a {@code NullPointerException} will be thrown.
		 * 
		 * @param textureClearCoat the {@code Texture} instance for the clear coat value
		 * @return this {@code Builder} instance
		 * @throws NullPointerException thrown if, and only if, {@code textureClearCoat} is {@code null}
		 */
		public Builder setTextureClearCoat(final Texture textureClearCoat) {
			this.textureClearCoat = Objects.requireNonNull(textureClearCoat, "textureClearCoat == null");
			
			return this;
		}
		
		/**
		 * Sets the {@link Texture} instance for the clear coat value.
		 * <p>
		 * Returns this {@code Builder} instance.
		 * 
		 * @param floatClearCoat a {@code float} for the clear coat value
		 * @return this {@code Builder} instance
		 */
		public Builder setTextureClearCoat(final float floatClearCoat) {
			return setTextureClearCoat(new ConstantTexture(floatClearCoat));
		}
		
		/**
		 * Sets the {@link Texture} instance for the clear coat gloss value.
		 * <p>
		 * Returns this {@code Builder} instance.
		 * <p>
		 * If {@code textureClearCoatGloss} is {@code null}, a {@code NullPointerException} will be thrown.
		 * 
		 * @param textureClearCoatGloss the {@code Texture} instance for the clear coat gloss value
		 * @return this {@code Builder} instance
		 * @throws NullPointerException thrown if, and only if, {@code textureClearCoatGloss} is {@code null}
		 */
		public Builder setTextureClearCoatGloss(final Texture textureClearCoatGloss) {
			this.textureClearCoatGloss = Objects.requireNonNull(textureClearCoatGloss, "textureClearCoatGloss == null");
			
			return this;
		}
		
		/**
		 * Sets the {@link Texture} instance for the clear coat gloss value.
		 * <p>
		 * Returns this {@code Builder} instance.
		 * 
		 * @param floatClearCoatGloss a {@code float} for the clear coat gloss value
		 * @return this {@code Builder} instance
		 */
		public Builder setTextureClearCoatGloss(final float floatClearCoatGloss) {
			return setTextureClearCoatGloss(new ConstantTexture(floatClearCoatGloss));
		}
		
		/**
		 * Sets the {@link Texture} instance for the color.
		 * <p>
		 * Returns this {@code Builder} instance.
		 * <p>
		 * If {@code colorColor} is {@code null}, a {@code NullPointerException} will be thrown.
		 * 
		 * @param colorColor a {@link Color3F} instance for the color
		 * @return this {@code Builder} instance
		 * @throws NullPointerException thrown if, and only if, {@code colorColor} is {@code null}
		 */
		public Builder setTextureColor(final Color3F colorColor) {
			return setTextureColor(new ConstantTexture(Objects.requireNonNull(colorColor, "colorColor == null")));
		}
		
		/**
		 * Sets the {@link Texture} instance for the color.
		 * <p>
		 * Returns this {@code Builder} instance.
		 * <p>
		 * If {@code textureColor} is {@code null}, a {@code NullPointerException} will be thrown.
		 * 
		 * @param textureColor the {@code Texture} instance for the color
		 * @return this {@code Builder} instance
		 * @throws NullPointerException thrown if, and only if, {@code textureColor} is {@code null}
		 */
		public Builder setTextureColor(final Texture textureColor) {
			this.textureColor = Objects.requireNonNull(textureColor, "textureColor == null");
			
			return this;
		}
		
		/**
		 * Sets the {@link Texture} instance for the diffuse transmission.
		 * <p>
		 * Returns this {@code Builder} instance.
		 * <p>
		 * If {@code textureDiffuseTransmission} is {@code null}, a {@code NullPointerException} will be thrown.
		 * 
		 * @param textureDiffuseTransmission the {@code Texture} instance for the diffuse transmission
		 * @return this {@code Builder} instance
		 * @throws NullPointerException thrown if, and only if, {@code textureDiffuseTransmission} is {@code null}
		 */
		public Builder setTextureDiffuseTransmission(final Texture textureDiffuseTransmission) {
			this.textureDiffuseTransmission = Objects.requireNonNull(textureDiffuseTransmission, "textureDiffuseTransmission == null");
			
			return this;
		}
		
		/**
		 * Sets the {@link Texture} instance for the diffuse transmission.
		 * <p>
		 * Returns this {@code Builder} instance.
		 * 
		 * @param floatDiffuseTransmission a {@code float} for the diffuse transmission
		 * @return this {@code Builder} instance
		 */
		public Builder setTextureDiffuseTransmission(final float floatDiffuseTransmission) {
			return setTextureDiffuseTransmission(new ConstantTexture(floatDiffuseTransmission));
		}
		
		/**
		 * Sets the {@link Texture} instance for emission.
		 * <p>
		 * Returns this {@code Builder} instance.
		 * <p>
		 * If {@code colorEmission} is {@code null}, a {@code NullPointerException} will be thrown.
		 * 
		 * @param colorEmission a {@link Color3F} instance for emission
		 * @return this {@code Builder} instance
		 * @throws NullPointerException thrown if, and only if, {@code colorEmission} is {@code null}
		 */
		public Builder setTextureEmission(final Color3F colorEmission) {
			return setTextureEmission(new ConstantTexture(Objects.requireNonNull(colorEmission, "colorEmission == null")));
		}
		
		/**
		 * Sets the {@link Texture} instance for emission.
		 * <p>
		 * Returns this {@code Builder} instance.
		 * <p>
		 * If {@code textureEmission} is {@code null}, a {@code NullPointerException} will be thrown.
		 * 
		 * @param textureEmission the {@code Texture} instance for emission
		 * @return this {@code Builder} instance
		 * @throws NullPointerException thrown if, and only if, {@code textureEmission} is {@code null}
		 */
		public Builder setTextureEmission(final Texture textureEmission) {
			this.textureEmission = Objects.requireNonNull(textureEmission, "textureEmission == null");
			
			return this;
		}
		
		/**
		 * Sets the {@link Texture} instance for the index of refraction (IOR).
		 * <p>
		 * Returns this {@code Builder} instance.
		 * <p>
		 * If {@code textureEta} is {@code null}, a {@code NullPointerException} will be thrown.
		 * 
		 * @param textureEta the {@code Texture} instance for the index of refraction (IOR)
		 * @return this {@code Builder} instance
		 * @throws NullPointerException thrown if, and only if, {@code textureEta} is {@code null}
		 */
		public Builder setTextureEta(final Texture textureEta) {
			this.textureEta = Objects.requireNonNull(textureEta, "textureEta == null");
			
			return this;
		}
		
		/**
		 * Sets the {@link Texture} instance for the index of refraction (IOR).
		 * <p>
		 * Returns this {@code Builder} instance.
		 * 
		 * @param floatEta a {@code float} for the index of refraction (IOR)
		 * @return this {@code Builder} instance
		 */
		public Builder setTextureEta(final float floatEta) {
			return setTextureEta(new ConstantTexture(floatEta));
		}
		
		/**
		 * Sets the {@link Texture} instance for the flatness.
		 * <p>
		 * Returns this {@code Builder} instance.
		 * <p>
		 * If {@code textureFlatness} is {@code null}, a {@code NullPointerException} will be thrown.
		 * 
		 * @param textureFlatness the {@code Texture} instance for the flatness
		 * @return this {@code Builder} instance
		 * @throws NullPointerException thrown if, and only if, {@code textureFlatness} is {@code null}
		 */
		public Builder setTextureFlatness(final Texture textureFlatness) {
			this.textureFlatness = Objects.requireNonNull(textureFlatness, "textureFlatness == null");
			
			return this;
		}
		
		/**
		 * Sets the {@link Texture} instance for the flatness.
		 * <p>
		 * Returns this {@code Builder} instance.
		 * 
		 * @param floatFlatness a {@code float} for the flatness
		 * @return this {@code Builder} instance
		 */
		public Builder setTextureFlatness(final float floatFlatness) {
			return setTextureFlatness(new ConstantTexture(floatFlatness));
		}
		
		/**
		 * Sets the {@link Texture} instance for the metallic value.
		 * <p>
		 * Returns this {@code Builder} instance.
		 * <p>
		 * If {@code textureMetallic} is {@code null}, a {@code NullPointerException} will be thrown.
		 * 
		 * @param textureMetallic the {@code Texture} instance for the metallic value
		 * @return this {@code Builder} instance
		 * @throws NullPointerException thrown if, and only if, {@code textureMetallic} is {@code null}
		 */
		public Builder setTextureMetallic(final Texture textureMetallic) {
			this.textureMetallic = Objects.requireNonNull(textureMetallic, "textureMetallic == null");
			
			return this;
		}
		
		/**
		 * Sets the {@link Texture} instance for the metallic value.
		 * <p>
		 * Returns this {@code Builder} instance.
		 * 
		 * @param floatMetallic a {@code float} for the metallic value
		 * @return this {@code Builder} instance
		 */
		public Builder setTextureMetallic(final float floatMetallic) {
			return setTextureMetallic(new ConstantTexture(floatMetallic));
		}
		
		/**
		 * Sets the {@link Texture} instance for the roughness.
		 * <p>
		 * Returns this {@code Builder} instance.
		 * <p>
		 * If {@code textureRoughness} is {@code null}, a {@code NullPointerException} will be thrown.
		 * 
		 * @param textureRoughness the {@code Texture} instance for the roughness
		 * @return this {@code Builder} instance
		 * @throws NullPointerException thrown if, and only if, {@code textureRoughness} is {@code null}
		 */
		public Builder setTextureRoughness(final Texture textureRoughness) {
			this.textureRoughness = Objects.requireNonNull(textureRoughness, "textureRoughness == null");
			
			return this;
		}
		
		/**
		 * Sets the {@link Texture} instance for the roughness.
		 * <p>
		 * Returns this {@code Builder} instance.
		 * 
		 * @param floatRoughness a {@code float} for the roughness
		 * @return this {@code Builder} instance
		 */
		public Builder setTextureRoughness(final float floatRoughness) {
			return setTextureRoughness(new ConstantTexture(floatRoughness));
		}
		
		/**
		 * Sets the {@link Texture} instance for the scatter distance.
		 * <p>
		 * Returns this {@code Builder} instance.
		 * <p>
		 * If {@code colorScatterDistance} is {@code null}, a {@code NullPointerException} will be thrown.
		 * 
		 * @param colorScatterDistance a {@link Color3F} instance for the scatter distance
		 * @return this {@code Builder} instance
		 * @throws NullPointerException thrown if, and only if, {@code colorScatterDistance} is {@code null}
		 */
		public Builder setTextureScatterDistance(final Color3F colorScatterDistance) {
			return setTextureScatterDistance(new ConstantTexture(Objects.requireNonNull(colorScatterDistance, "colorScatterDistance == null")));
		}
		
		/**
		 * Sets the {@link Texture} instance for the scatter distance.
		 * <p>
		 * Returns this {@code Builder} instance.
		 * <p>
		 * If {@code textureScatterDistance} is {@code null}, a {@code NullPointerException} will be thrown.
		 * 
		 * @param textureScatterDistance the {@code Texture} instance for the scatter distance
		 * @return this {@code Builder} instance
		 * @throws NullPointerException thrown if, and only if, {@code textureScatterDistance} is {@code null}
		 */
		public Builder setTextureScatterDistance(final Texture textureScatterDistance) {
			this.textureScatterDistance = Objects.requireNonNull(textureScatterDistance, "textureScatterDistance == null");
			
			return this;
		}
		
		/**
		 * Sets the {@link Texture} instance for the sheen value.
		 * <p>
		 * Returns this {@code Builder} instance.
		 * <p>
		 * If {@code textureSheen} is {@code null}, a {@code NullPointerException} will be thrown.
		 * 
		 * @param textureSheen the {@code Texture} instance for the sheen value
		 * @return this {@code Builder} instance
		 * @throws NullPointerException thrown if, and only if, {@code textureSheen} is {@code null}
		 */
		public Builder setTextureSheen(final Texture textureSheen) {
			this.textureSheen = Objects.requireNonNull(textureSheen, "textureSheen == null");
			
			return this;
		}
		
		/**
		 * Sets the {@link Texture} instance for the sheen value.
		 * <p>
		 * Returns this {@code Builder} instance.
		 * 
		 * @param floatSheen a {@code float} for the sheen value
		 * @return this {@code Builder} instance
		 */
		public Builder setTextureSheen(final float floatSheen) {
			return setTextureSheen(new ConstantTexture(floatSheen));
		}
		
		/**
		 * Sets the {@link Texture} instance for the sheen tint value.
		 * <p>
		 * Returns this {@code Builder} instance.
		 * <p>
		 * If {@code textureSheenTint} is {@code null}, a {@code NullPointerException} will be thrown.
		 * 
		 * @param textureSheenTint the {@code Texture} instance for the sheen tint value
		 * @return this {@code Builder} instance
		 * @throws NullPointerException thrown if, and only if, {@code textureSheenTint} is {@code null}
		 */
		public Builder setTextureSheenTint(final Texture textureSheenTint) {
			this.textureSheenTint = Objects.requireNonNull(textureSheenTint, "textureSheenTint == null");
			
			return this;
		}
		
		/**
		 * Sets the {@link Texture} instance for the sheen tint value.
		 * <p>
		 * Returns this {@code Builder} instance.
		 * 
		 * @param floatSheenTint a {@code float} for the sheen tint value
		 * @return this {@code Builder} instance
		 */
		public Builder setTextureSheenTint(final float floatSheenTint) {
			return setTextureSheenTint(new ConstantTexture(floatSheenTint));
		}
		
		/**
		 * Sets the {@link Texture} instance for the specular tint.
		 * <p>
		 * Returns this {@code Builder} instance.
		 * <p>
		 * If {@code textureSpecularTint} is {@code null}, a {@code NullPointerException} will be thrown.
		 * 
		 * @param textureSpecularTint the {@code Texture} instance for the specular tint
		 * @return this {@code Builder} instance
		 * @throws NullPointerException thrown if, and only if, {@code textureSpecularTint} is {@code null}
		 */
		public Builder setTextureSpecularTint(final Texture textureSpecularTint) {
			this.textureSpecularTint = Objects.requireNonNull(textureSpecularTint, "textureSpecularTint == null");
			
			return this;
		}
		
		/**
		 * Sets the {@link Texture} instance for the specular tint.
		 * <p>
		 * Returns this {@code Builder} instance.
		 * 
		 * @param floatSpecularTint a {@code float} for the specular tint
		 * @return this {@code Builder} instance
		 */
		public Builder setTextureSpecularTint(final float floatSpecularTint) {
			return setTextureSpecularTint(new ConstantTexture(floatSpecularTint));
		}
		
		/**
		 * Sets the {@link Texture} instance for the specular transmission.
		 * <p>
		 * Returns this {@code Builder} instance.
		 * <p>
		 * If {@code textureSpecularTransmission} is {@code null}, a {@code NullPointerException} will be thrown.
		 * 
		 * @param textureSpecularTransmission the {@code Texture} instance for the specular transmission
		 * @return this {@code Builder} instance
		 * @throws NullPointerException thrown if, and only if, {@code textureSpecularTransmission} is {@code null}
		 */
		public Builder setTextureSpecularTransmission(final Texture textureSpecularTransmission) {
			this.textureSpecularTransmission = Objects.requireNonNull(textureSpecularTransmission, "textureSpecularTransmission == null");
			
			return this;
		}
		
		/**
		 * Sets the {@link Texture} instance for the specular transmission.
		 * <p>
		 * Returns this {@code Builder} instance.
		 * 
		 * @param floatSpecularTransmission a {@code float} for the specular transmission
		 * @return this {@code Builder} instance
		 */
		public Builder setTextureSpecularTransmission(final float floatSpecularTransmission) {
			return setTextureSpecularTransmission(new ConstantTexture(floatSpecularTransmission));
		}
		
		/**
		 * Sets the thin state.
		 * <p>
		 * Returns this {@code Builder} instance.
		 * 
		 * @param isThin {@code true} if, and only if, the {@link DisneyMaterial} instance should be thin, {@code false} otherwise
		 * @return this {@code Builder} instance
		 */
		public Builder setThin(final boolean isThin) {
			this.isThin = isThin;
			
			return this;
		}
		
		/**
		 * Returns a new {@link DisneyMaterial} instance.
		 * 
		 * @return a new {@code DisneyMaterial} instance
		 */
		public DisneyMaterial build() {
			return new DisneyMaterial(this.textureColor, this.textureEmission, this.textureScatterDistance, this.textureAnisotropic, this.textureClearCoat, this.textureClearCoatGloss, this.textureDiffuseTransmission, this.textureEta, this.textureFlatness, this.textureMetallic, this.textureRoughness, this.textureSheen, this.textureSheenTint, this.textureSpecularTint, this.textureSpecularTransmission, this.isThin, this.modifier);
		}
	}
}