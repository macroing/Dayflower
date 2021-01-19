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
package org.dayflower.scene.material.pbrt;

import static org.dayflower.util.Floats.lerp;
import static org.dayflower.util.Floats.max;
import static org.dayflower.util.Floats.sqrt;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.dayflower.color.Color3F;
import org.dayflower.node.NodeHierarchicalVisitor;
import org.dayflower.node.NodeTraversalException;
import org.dayflower.scene.BSSRDF;
import org.dayflower.scene.Intersection;
import org.dayflower.scene.TransportMode;
import org.dayflower.scene.bxdf.pbrt.DisneyClearCoatPBRTBRDF;
import org.dayflower.scene.bxdf.pbrt.DisneyDiffusePBRTBRDF;
import org.dayflower.scene.bxdf.pbrt.DisneyFakeSSPBRTBRDF;
import org.dayflower.scene.bxdf.pbrt.DisneyRetroPBRTBRDF;
import org.dayflower.scene.bxdf.pbrt.DisneySheenPBRTBRDF;
import org.dayflower.scene.bxdf.pbrt.LambertianPBRTBTDF;
import org.dayflower.scene.bxdf.pbrt.PBRTBSDF;
import org.dayflower.scene.bxdf.pbrt.PBRTBXDF;
import org.dayflower.scene.bxdf.pbrt.SpecularPBRTBTDF;
import org.dayflower.scene.bxdf.pbrt.TorranceSparrowPBRTBRDF;
import org.dayflower.scene.bxdf.pbrt.TorranceSparrowPBRTBTDF;
import org.dayflower.scene.fresnel.DisneyFresnel;
import org.dayflower.scene.fresnel.Fresnel;
import org.dayflower.scene.microfacet.MicrofacetDistribution;
import org.dayflower.scene.microfacet.TrowbridgeReitzMicrofacetDistribution;
import org.dayflower.scene.texture.ConstantTexture;
import org.dayflower.scene.texture.Texture;

/**
 * A {@code DisneyPBRTMaterial} is an implementation of {@link PBRTMaterial} that represents a Disney material.
 * <p>
 * This class is immutable and thread-safe as long as all {@link Texture} instances are.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class DisneyPBRTMaterial implements PBRTMaterial {
	/**
	 * The name of this {@code DisneyPBRTMaterial} class.
	 */
	public static final String NAME = "PBRT - Disney";
	
	/**
	 * The ID of this {@code DisneyPBRTMaterial} class.
	 */
	public static final int ID = 100;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
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
	 * Constructs a new {@code DisneyPBRTMaterial} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new DisneyPBRTMaterial(Color3F.GRAY_0_50);
	 * }
	 * </pre>
	 */
	public DisneyPBRTMaterial() {
		this(Color3F.GRAY_0_50);
	}
	
	/**
	 * Constructs a new {@code DisneyPBRTMaterial} instance.
	 * <p>
	 * If {@code colorColor} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new DisneyPBRTMaterial(colorColor, Color3F.BLACK);
	 * }
	 * </pre>
	 * 
	 * @param colorColor a {@link Color3F} instance for the color
	 * @throws NullPointerException thrown if, and only if, {@code colorColor} is {@code null}
	 */
	public DisneyPBRTMaterial(final Color3F colorColor) {
		this(colorColor, Color3F.BLACK);
	}
	
	/**
	 * Constructs a new {@code DisneyPBRTMaterial} instance.
	 * <p>
	 * If either {@code colorColor} or {@code colorEmission} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new DisneyPBRTMaterial(colorColor, colorEmission, Color3F.BLACK);
	 * }
	 * </pre>
	 * 
	 * @param colorColor a {@link Color3F} instance for the color
	 * @param colorEmission a {@code Color3F} instance for emission
	 * @throws NullPointerException thrown if, and only if, either {@code colorColor} or {@code colorEmission} are {@code null}
	 */
	public DisneyPBRTMaterial(final Color3F colorColor, final Color3F colorEmission) {
		this(colorColor, colorEmission, Color3F.BLACK);
	}
	
	/**
	 * Constructs a new {@code DisneyPBRTMaterial} instance.
	 * <p>
	 * If either {@code colorColor}, {@code colorEmission} or {@code colorScatterDistance} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new DisneyPBRTMaterial(colorColor, colorEmission, colorScatterDistance, 0.0F);
	 * }
	 * </pre>
	 * 
	 * @param colorColor a {@link Color3F} instance for the color
	 * @param colorEmission a {@code Color3F} instance for emission
	 * @param colorScatterDistance a {@code Color3F} instance for the scatter distance
	 * @throws NullPointerException thrown if, and only if, either {@code colorColor}, {@code colorEmission} or {@code colorScatterDistance} are {@code null}
	 */
	public DisneyPBRTMaterial(final Color3F colorColor, final Color3F colorEmission, final Color3F colorScatterDistance) {
		this(colorColor, colorEmission, colorScatterDistance, 0.0F);
	}
	
	/**
	 * Constructs a new {@code DisneyPBRTMaterial} instance.
	 * <p>
	 * If either {@code colorColor}, {@code colorEmission} or {@code colorScatterDistance} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new DisneyPBRTMaterial(colorColor, colorEmission, colorScatterDistance, floatAnisotropic, 0.0F);
	 * }
	 * </pre>
	 * 
	 * @param colorColor a {@link Color3F} instance for the color
	 * @param colorEmission a {@code Color3F} instance for emission
	 * @param colorScatterDistance a {@code Color3F} instance for the scatter distance
	 * @param floatAnisotropic a {@code float} for the anisotropic value
	 * @throws NullPointerException thrown if, and only if, either {@code colorColor}, {@code colorEmission} or {@code colorScatterDistance} are {@code null}
	 */
	public DisneyPBRTMaterial(final Color3F colorColor, final Color3F colorEmission, final Color3F colorScatterDistance, final float floatAnisotropic) {
		this(colorColor, colorEmission, colorScatterDistance, floatAnisotropic, 0.0F);
	}
	
	/**
	 * Constructs a new {@code DisneyPBRTMaterial} instance.
	 * <p>
	 * If either {@code colorColor}, {@code colorEmission} or {@code colorScatterDistance} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new DisneyPBRTMaterial(colorColor, colorEmission, colorScatterDistance, floatAnisotropic, floatClearCoat, 1.0F);
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
	public DisneyPBRTMaterial(final Color3F colorColor, final Color3F colorEmission, final Color3F colorScatterDistance, final float floatAnisotropic, final float floatClearCoat) {
		this(colorColor, colorEmission, colorScatterDistance, floatAnisotropic, floatClearCoat, 1.0F);
	}
	
	/**
	 * Constructs a new {@code DisneyPBRTMaterial} instance.
	 * <p>
	 * If either {@code colorColor}, {@code colorEmission} or {@code colorScatterDistance} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new DisneyPBRTMaterial(colorColor, colorEmission, colorScatterDistance, floatAnisotropic, floatClearCoat, floatClearCoatGloss, 1.0F);
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
	public DisneyPBRTMaterial(final Color3F colorColor, final Color3F colorEmission, final Color3F colorScatterDistance, final float floatAnisotropic, final float floatClearCoat, final float floatClearCoatGloss) {
		this(colorColor, colorEmission, colorScatterDistance, floatAnisotropic, floatClearCoat, floatClearCoatGloss, 1.0F);
	}
	
	/**
	 * Constructs a new {@code DisneyPBRTMaterial} instance.
	 * <p>
	 * If either {@code colorColor}, {@code colorEmission} or {@code colorScatterDistance} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new DisneyPBRTMaterial(colorColor, colorEmission, colorScatterDistance, floatAnisotropic, floatClearCoat, floatClearCoatGloss, floatDiffuseTransmission, 1.5F);
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
	public DisneyPBRTMaterial(final Color3F colorColor, final Color3F colorEmission, final Color3F colorScatterDistance, final float floatAnisotropic, final float floatClearCoat, final float floatClearCoatGloss, final float floatDiffuseTransmission) {
		this(colorColor, colorEmission, colorScatterDistance, floatAnisotropic, floatClearCoat, floatClearCoatGloss, floatDiffuseTransmission, 1.5F);
	}
	
	/**
	 * Constructs a new {@code DisneyPBRTMaterial} instance.
	 * <p>
	 * If either {@code colorColor}, {@code colorEmission} or {@code colorScatterDistance} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new DisneyPBRTMaterial(colorColor, colorEmission, colorScatterDistance, floatAnisotropic, floatClearCoat, floatClearCoatGloss, floatDiffuseTransmission, floatEta, 0.0F);
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
	public DisneyPBRTMaterial(final Color3F colorColor, final Color3F colorEmission, final Color3F colorScatterDistance, final float floatAnisotropic, final float floatClearCoat, final float floatClearCoatGloss, final float floatDiffuseTransmission, final float floatEta) {
		this(colorColor, colorEmission, colorScatterDistance, floatAnisotropic, floatClearCoat, floatClearCoatGloss, floatDiffuseTransmission, floatEta, 0.0F);
	}
	
	/**
	 * Constructs a new {@code DisneyPBRTMaterial} instance.
	 * <p>
	 * If either {@code colorColor}, {@code colorEmission} or {@code colorScatterDistance} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new DisneyPBRTMaterial(colorColor, colorEmission, colorScatterDistance, floatAnisotropic, floatClearCoat, floatClearCoatGloss, floatDiffuseTransmission, floatEta, floatFlatness, 0.0F);
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
	public DisneyPBRTMaterial(final Color3F colorColor, final Color3F colorEmission, final Color3F colorScatterDistance, final float floatAnisotropic, final float floatClearCoat, final float floatClearCoatGloss, final float floatDiffuseTransmission, final float floatEta, final float floatFlatness) {
		this(colorColor, colorEmission, colorScatterDistance, floatAnisotropic, floatClearCoat, floatClearCoatGloss, floatDiffuseTransmission, floatEta, floatFlatness, 0.0F);
	}
	
	/**
	 * Constructs a new {@code DisneyPBRTMaterial} instance.
	 * <p>
	 * If either {@code colorColor}, {@code colorEmission} or {@code colorScatterDistance} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new DisneyPBRTMaterial(colorColor, colorEmission, colorScatterDistance, floatAnisotropic, floatClearCoat, floatClearCoatGloss, floatDiffuseTransmission, floatEta, floatFlatness, floatMetallic, 0.5F);
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
	public DisneyPBRTMaterial(final Color3F colorColor, final Color3F colorEmission, final Color3F colorScatterDistance, final float floatAnisotropic, final float floatClearCoat, final float floatClearCoatGloss, final float floatDiffuseTransmission, final float floatEta, final float floatFlatness, final float floatMetallic) {
		this(colorColor, colorEmission, colorScatterDistance, floatAnisotropic, floatClearCoat, floatClearCoatGloss, floatDiffuseTransmission, floatEta, floatFlatness, floatMetallic, 0.5F);
	}
	
	/**
	 * Constructs a new {@code DisneyPBRTMaterial} instance.
	 * <p>
	 * If either {@code colorColor}, {@code colorEmission} or {@code colorScatterDistance} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new DisneyPBRTMaterial(colorColor, colorEmission, colorScatterDistance, floatAnisotropic, floatClearCoat, floatClearCoatGloss, floatDiffuseTransmission, floatEta, floatFlatness, floatMetallic, floatRoughness, 0.0F);
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
	public DisneyPBRTMaterial(final Color3F colorColor, final Color3F colorEmission, final Color3F colorScatterDistance, final float floatAnisotropic, final float floatClearCoat, final float floatClearCoatGloss, final float floatDiffuseTransmission, final float floatEta, final float floatFlatness, final float floatMetallic, final float floatRoughness) {
		this(colorColor, colorEmission, colorScatterDistance, floatAnisotropic, floatClearCoat, floatClearCoatGloss, floatDiffuseTransmission, floatEta, floatFlatness, floatMetallic, floatRoughness, 0.0F);
	}
	
	/**
	 * Constructs a new {@code DisneyPBRTMaterial} instance.
	 * <p>
	 * If either {@code colorColor}, {@code colorEmission} or {@code colorScatterDistance} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new DisneyPBRTMaterial(colorColor, colorEmission, colorScatterDistance, floatAnisotropic, floatClearCoat, floatClearCoatGloss, floatDiffuseTransmission, floatEta, floatFlatness, floatMetallic, floatRoughness, floatSheen, 0.5F);
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
	public DisneyPBRTMaterial(final Color3F colorColor, final Color3F colorEmission, final Color3F colorScatterDistance, final float floatAnisotropic, final float floatClearCoat, final float floatClearCoatGloss, final float floatDiffuseTransmission, final float floatEta, final float floatFlatness, final float floatMetallic, final float floatRoughness, final float floatSheen) {
		this(colorColor, colorEmission, colorScatterDistance, floatAnisotropic, floatClearCoat, floatClearCoatGloss, floatDiffuseTransmission, floatEta, floatFlatness, floatMetallic, floatRoughness, floatSheen, 0.5F);
	}
	
	/**
	 * Constructs a new {@code DisneyPBRTMaterial} instance.
	 * <p>
	 * If either {@code colorColor}, {@code colorEmission} or {@code colorScatterDistance} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new DisneyPBRTMaterial(colorColor, colorEmission, colorScatterDistance, floatAnisotropic, floatClearCoat, floatClearCoatGloss, floatDiffuseTransmission, floatEta, floatFlatness, floatMetallic, floatRoughness, floatSheen, floatSheenTint, 0.0F);
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
	public DisneyPBRTMaterial(final Color3F colorColor, final Color3F colorEmission, final Color3F colorScatterDistance, final float floatAnisotropic, final float floatClearCoat, final float floatClearCoatGloss, final float floatDiffuseTransmission, final float floatEta, final float floatFlatness, final float floatMetallic, final float floatRoughness, final float floatSheen, final float floatSheenTint) {
		this(colorColor, colorEmission, colorScatterDistance, floatAnisotropic, floatClearCoat, floatClearCoatGloss, floatDiffuseTransmission, floatEta, floatFlatness, floatMetallic, floatRoughness, floatSheen, floatSheenTint, 0.0F);
	}
	
	/**
	 * Constructs a new {@code DisneyPBRTMaterial} instance.
	 * <p>
	 * If either {@code colorColor}, {@code colorEmission} or {@code colorScatterDistance} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new DisneyPBRTMaterial(colorColor, colorEmission, colorScatterDistance, floatAnisotropic, floatClearCoat, floatClearCoatGloss, floatDiffuseTransmission, floatEta, floatFlatness, floatMetallic, floatRoughness, floatSheen, floatSheenTint, floatSpecularTint, 0.0F);
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
	public DisneyPBRTMaterial(final Color3F colorColor, final Color3F colorEmission, final Color3F colorScatterDistance, final float floatAnisotropic, final float floatClearCoat, final float floatClearCoatGloss, final float floatDiffuseTransmission, final float floatEta, final float floatFlatness, final float floatMetallic, final float floatRoughness, final float floatSheen, final float floatSheenTint, final float floatSpecularTint) {
		this(colorColor, colorEmission, colorScatterDistance, floatAnisotropic, floatClearCoat, floatClearCoatGloss, floatDiffuseTransmission, floatEta, floatFlatness, floatMetallic, floatRoughness, floatSheen, floatSheenTint, floatSpecularTint, 0.0F);
	}
	
	/**
	 * Constructs a new {@code DisneyPBRTMaterial} instance.
	 * <p>
	 * If either {@code colorColor}, {@code colorEmission} or {@code colorScatterDistance} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new DisneyPBRTMaterial(colorColor, colorEmission, colorScatterDistance, floatAnisotropic, floatClearCoat, floatClearCoatGloss, floatDiffuseTransmission, floatEta, floatFlatness, floatMetallic, floatRoughness, floatSheen, floatSheenTint, floatSpecularTint, floatSpecularTransmission, false);
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
	public DisneyPBRTMaterial(final Color3F colorColor, final Color3F colorEmission, final Color3F colorScatterDistance, final float floatAnisotropic, final float floatClearCoat, final float floatClearCoatGloss, final float floatDiffuseTransmission, final float floatEta, final float floatFlatness, final float floatMetallic, final float floatRoughness, final float floatSheen, final float floatSheenTint, final float floatSpecularTint, final float floatSpecularTransmission) {
		this(colorColor, colorEmission, colorScatterDistance, floatAnisotropic, floatClearCoat, floatClearCoatGloss, floatDiffuseTransmission, floatEta, floatFlatness, floatMetallic, floatRoughness, floatSheen, floatSheenTint, floatSpecularTint, floatSpecularTransmission, false);
	}
	
	/**
	 * Constructs a new {@code DisneyPBRTMaterial} instance.
	 * <p>
	 * If either {@code colorColor}, {@code colorEmission} or {@code colorScatterDistance} are {@code null}, a {@code NullPointerException} will be thrown.
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
	 * @param isThin {@code true} if, and only if, this {@code DisneyPBRTMaterial} instance is thin, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, either {@code colorColor}, {@code colorEmission} or {@code colorScatterDistance} are {@code null}
	 */
	public DisneyPBRTMaterial(final Color3F colorColor, final Color3F colorEmission, final Color3F colorScatterDistance, final float floatAnisotropic, final float floatClearCoat, final float floatClearCoatGloss, final float floatDiffuseTransmission, final float floatEta, final float floatFlatness, final float floatMetallic, final float floatRoughness, final float floatSheen, final float floatSheenTint, final float floatSpecularTint, final float floatSpecularTransmission, final boolean isThin) {
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
	}
	
	/**
	 * Constructs a new {@code DisneyPBRTMaterial} instance.
	 * <p>
	 * If {@code textureColor} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new DisneyPBRTMaterial(textureColor, ConstantTexture.BLACK);
	 * }
	 * </pre>
	 * 
	 * @param textureColor a {@link Texture} instance for the color
	 * @throws NullPointerException thrown if, and only if, {@code textureColor} is {@code null}
	 */
	public DisneyPBRTMaterial(final Texture textureColor) {
		this(textureColor, ConstantTexture.BLACK);
	}
	
	/**
	 * Constructs a new {@code DisneyPBRTMaterial} instance.
	 * <p>
	 * If either {@code textureColor} or {@code textureEmission} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new DisneyPBRTMaterial(textureColor, textureEmission, ConstantTexture.BLACK);
	 * }
	 * </pre>
	 * 
	 * @param textureColor a {@link Texture} instance for the color
	 * @param textureEmission a {@code Texture} instance for emission
	 * @throws NullPointerException thrown if, and only if, either {@code textureColor} or {@code textureEmission} are {@code null}
	 */
	public DisneyPBRTMaterial(final Texture textureColor, final Texture textureEmission) {
		this(textureColor, textureEmission, ConstantTexture.BLACK);
	}
	
	/**
	 * Constructs a new {@code DisneyPBRTMaterial} instance.
	 * <p>
	 * If either {@code textureColor}, {@code textureEmission} or {@code textureScatterDistance} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new DisneyPBRTMaterial(textureColor, textureEmission, textureScatterDistance, ConstantTexture.BLACK);
	 * }
	 * </pre>
	 * 
	 * @param textureColor a {@link Texture} instance for the color
	 * @param textureEmission a {@code Texture} instance for emission
	 * @param textureScatterDistance a {@code Texture} instance for the scatter distance
	 * @throws NullPointerException thrown if, and only if, either {@code textureColor}, {@code textureEmission} or {@code textureScatterDistance} are {@code null}
	 */
	public DisneyPBRTMaterial(final Texture textureColor, final Texture textureEmission, final Texture textureScatterDistance) {
		this(textureColor, textureEmission, textureScatterDistance, ConstantTexture.BLACK);
	}
	
	/**
	 * Constructs a new {@code DisneyPBRTMaterial} instance.
	 * <p>
	 * If either {@code textureColor}, {@code textureEmission}, {@code textureScatterDistance} or {@code textureAnisotropic} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new DisneyPBRTMaterial(textureColor, textureEmission, textureScatterDistance, textureAnisotropic, ConstantTexture.BLACK);
	 * }
	 * </pre>
	 * 
	 * @param textureColor a {@link Texture} instance for the color
	 * @param textureEmission a {@code Texture} instance for emission
	 * @param textureScatterDistance a {@code Texture} instance for the scatter distance
	 * @param textureAnisotropic a {@code Texture} instance for the anisotropic value
	 * @throws NullPointerException thrown if, and only if, either {@code textureColor}, {@code textureEmission}, {@code textureScatterDistance} or {@code textureAnisotropic} are {@code null}
	 */
	public DisneyPBRTMaterial(final Texture textureColor, final Texture textureEmission, final Texture textureScatterDistance, final Texture textureAnisotropic) {
		this(textureColor, textureEmission, textureScatterDistance, textureAnisotropic, ConstantTexture.BLACK);
	}
	
	/**
	 * Constructs a new {@code DisneyPBRTMaterial} instance.
	 * <p>
	 * If either {@code textureColor}, {@code textureEmission}, {@code textureScatterDistance}, {@code textureAnisotropic} or {@code textureClearCoat} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new DisneyPBRTMaterial(textureColor, textureEmission, textureScatterDistance, textureAnisotropic, textureClearCoat, ConstantTexture.WHITE);
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
	public DisneyPBRTMaterial(final Texture textureColor, final Texture textureEmission, final Texture textureScatterDistance, final Texture textureAnisotropic, final Texture textureClearCoat) {
		this(textureColor, textureEmission, textureScatterDistance, textureAnisotropic, textureClearCoat, ConstantTexture.WHITE);
	}
	
	/**
	 * Constructs a new {@code DisneyPBRTMaterial} instance.
	 * <p>
	 * If either {@code textureColor}, {@code textureEmission}, {@code textureScatterDistance}, {@code textureAnisotropic}, {@code textureClearCoat} or {@code textureClearCoatGloss} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new DisneyPBRTMaterial(textureColor, textureEmission, textureScatterDistance, textureAnisotropic, textureClearCoat, textureClearCoatGloss, ConstantTexture.WHITE);
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
	public DisneyPBRTMaterial(final Texture textureColor, final Texture textureEmission, final Texture textureScatterDistance, final Texture textureAnisotropic, final Texture textureClearCoat, final Texture textureClearCoatGloss) {
		this(textureColor, textureEmission, textureScatterDistance, textureAnisotropic, textureClearCoat, textureClearCoatGloss, ConstantTexture.WHITE);
	}
	
	/**
	 * Constructs a new {@code DisneyPBRTMaterial} instance.
	 * <p>
	 * If either {@code textureColor}, {@code textureEmission}, {@code textureScatterDistance}, {@code textureAnisotropic}, {@code textureClearCoat}, {@code textureClearCoatGloss} or {@code textureDiffuseTransmission} are {@code null}, a
	 * {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new DisneyPBRTMaterial(textureColor, textureEmission, textureScatterDistance, textureAnisotropic, textureClearCoat, textureClearCoatGloss, textureDiffuseTransmission, ConstantTexture.GRAY_1_50);
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
	public DisneyPBRTMaterial(final Texture textureColor, final Texture textureEmission, final Texture textureScatterDistance, final Texture textureAnisotropic, final Texture textureClearCoat, final Texture textureClearCoatGloss, final Texture textureDiffuseTransmission) {
		this(textureColor, textureEmission, textureScatterDistance, textureAnisotropic, textureClearCoat, textureClearCoatGloss, textureDiffuseTransmission, ConstantTexture.GRAY_1_50);
	}
	
	/**
	 * Constructs a new {@code DisneyPBRTMaterial} instance.
	 * <p>
	 * If either {@code textureColor}, {@code textureEmission}, {@code textureScatterDistance}, {@code textureAnisotropic}, {@code textureClearCoat}, {@code textureClearCoatGloss}, {@code textureDiffuseTransmission} or {@code textureEta} are
	 * {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new DisneyPBRTMaterial(textureColor, textureEmission, textureScatterDistance, textureAnisotropic, textureClearCoat, textureClearCoatGloss, textureDiffuseTransmission, textureEta, ConstantTexture.BLACK);
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
	public DisneyPBRTMaterial(final Texture textureColor, final Texture textureEmission, final Texture textureScatterDistance, final Texture textureAnisotropic, final Texture textureClearCoat, final Texture textureClearCoatGloss, final Texture textureDiffuseTransmission, final Texture textureEta) {
		this(textureColor, textureEmission, textureScatterDistance, textureAnisotropic, textureClearCoat, textureClearCoatGloss, textureDiffuseTransmission, textureEta, ConstantTexture.BLACK);
	}
	
	/**
	 * Constructs a new {@code DisneyPBRTMaterial} instance.
	 * <p>
	 * If either {@code textureColor}, {@code textureEmission}, {@code textureScatterDistance}, {@code textureAnisotropic}, {@code textureClearCoat}, {@code textureClearCoatGloss}, {@code textureDiffuseTransmission}, {@code textureEta} or
	 * {@code textureFlatness} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new DisneyPBRTMaterial(textureColor, textureEmission, textureScatterDistance, textureAnisotropic, textureClearCoat, textureClearCoatGloss, textureDiffuseTransmission, textureEta, textureFlatness, ConstantTexture.BLACK);
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
	public DisneyPBRTMaterial(final Texture textureColor, final Texture textureEmission, final Texture textureScatterDistance, final Texture textureAnisotropic, final Texture textureClearCoat, final Texture textureClearCoatGloss, final Texture textureDiffuseTransmission, final Texture textureEta, final Texture textureFlatness) {
		this(textureColor, textureEmission, textureScatterDistance, textureAnisotropic, textureClearCoat, textureClearCoatGloss, textureDiffuseTransmission, textureEta, textureFlatness, ConstantTexture.BLACK);
	}
	
	/**
	 * Constructs a new {@code DisneyPBRTMaterial} instance.
	 * <p>
	 * If either {@code textureColor}, {@code textureEmission}, {@code textureScatterDistance}, {@code textureAnisotropic}, {@code textureClearCoat}, {@code textureClearCoatGloss}, {@code textureDiffuseTransmission}, {@code textureEta},
	 * {@code textureFlatness} or {@code textureMetallic} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new DisneyPBRTMaterial(textureColor, textureEmission, textureScatterDistance, textureAnisotropic, textureClearCoat, textureClearCoatGloss, textureDiffuseTransmission, textureEta, textureFlatness, textureMetallic, ConstantTexture.GRAY_0_50);
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
	public DisneyPBRTMaterial(final Texture textureColor, final Texture textureEmission, final Texture textureScatterDistance, final Texture textureAnisotropic, final Texture textureClearCoat, final Texture textureClearCoatGloss, final Texture textureDiffuseTransmission, final Texture textureEta, final Texture textureFlatness, final Texture textureMetallic) {
		this(textureColor, textureEmission, textureScatterDistance, textureAnisotropic, textureClearCoat, textureClearCoatGloss, textureDiffuseTransmission, textureEta, textureFlatness, textureMetallic, ConstantTexture.GRAY_0_50);
	}
	
	/**
	 * Constructs a new {@code DisneyPBRTMaterial} instance.
	 * <p>
	 * If either {@code textureColor}, {@code textureEmission}, {@code textureScatterDistance}, {@code textureAnisotropic}, {@code textureClearCoat}, {@code textureClearCoatGloss}, {@code textureDiffuseTransmission}, {@code textureEta},
	 * {@code textureFlatness}, {@code textureMetallic} or {@code textureRoughness} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new DisneyPBRTMaterial(textureColor, textureEmission, textureScatterDistance, textureAnisotropic, textureClearCoat, textureClearCoatGloss, textureDiffuseTransmission, textureEta, textureFlatness, textureMetallic, textureRoughness, ConstantTexture.BLACK);
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
	public DisneyPBRTMaterial(final Texture textureColor, final Texture textureEmission, final Texture textureScatterDistance, final Texture textureAnisotropic, final Texture textureClearCoat, final Texture textureClearCoatGloss, final Texture textureDiffuseTransmission, final Texture textureEta, final Texture textureFlatness, final Texture textureMetallic, final Texture textureRoughness) {
		this(textureColor, textureEmission, textureScatterDistance, textureAnisotropic, textureClearCoat, textureClearCoatGloss, textureDiffuseTransmission, textureEta, textureFlatness, textureMetallic, textureRoughness, ConstantTexture.BLACK);
	}
	
	/**
	 * Constructs a new {@code DisneyPBRTMaterial} instance.
	 * <p>
	 * If either {@code textureColor}, {@code textureEmission}, {@code textureScatterDistance}, {@code textureAnisotropic}, {@code textureClearCoat}, {@code textureClearCoatGloss}, {@code textureDiffuseTransmission}, {@code textureEta},
	 * {@code textureFlatness}, {@code textureMetallic}, {@code textureRoughness} or {@code textureSheen} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new DisneyPBRTMaterial(textureColor, textureEmission, textureScatterDistance, textureAnisotropic, textureClearCoat, textureClearCoatGloss, textureDiffuseTransmission, textureEta, textureFlatness, textureMetallic, textureRoughness, textureSheen, ConstantTexture.GRAY_0_50);
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
	public DisneyPBRTMaterial(final Texture textureColor, final Texture textureEmission, final Texture textureScatterDistance, final Texture textureAnisotropic, final Texture textureClearCoat, final Texture textureClearCoatGloss, final Texture textureDiffuseTransmission, final Texture textureEta, final Texture textureFlatness, final Texture textureMetallic, final Texture textureRoughness, final Texture textureSheen) {
		this(textureColor, textureEmission, textureScatterDistance, textureAnisotropic, textureClearCoat, textureClearCoatGloss, textureDiffuseTransmission, textureEta, textureFlatness, textureMetallic, textureRoughness, textureSheen, ConstantTexture.GRAY_0_50);
	}
	
	/**
	 * Constructs a new {@code DisneyPBRTMaterial} instance.
	 * <p>
	 * If either {@code textureColor}, {@code textureEmission}, {@code textureScatterDistance}, {@code textureAnisotropic}, {@code textureClearCoat}, {@code textureClearCoatGloss}, {@code textureDiffuseTransmission}, {@code textureEta},
	 * {@code textureFlatness}, {@code textureMetallic}, {@code textureRoughness}, {@code textureSheen} or {@code textureSheenTint} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new DisneyPBRTMaterial(textureColor, textureEmission, textureScatterDistance, textureAnisotropic, textureClearCoat, textureClearCoatGloss, textureDiffuseTransmission, textureEta, textureFlatness, textureMetallic, textureRoughness, textureSheen, textureSheenTint, ConstantTexture.BLACK);
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
	public DisneyPBRTMaterial(final Texture textureColor, final Texture textureEmission, final Texture textureScatterDistance, final Texture textureAnisotropic, final Texture textureClearCoat, final Texture textureClearCoatGloss, final Texture textureDiffuseTransmission, final Texture textureEta, final Texture textureFlatness, final Texture textureMetallic, final Texture textureRoughness, final Texture textureSheen, final Texture textureSheenTint) {
		this(textureColor, textureEmission, textureScatterDistance, textureAnisotropic, textureClearCoat, textureClearCoatGloss, textureDiffuseTransmission, textureEta, textureFlatness, textureMetallic, textureRoughness, textureSheen, textureSheenTint, ConstantTexture.BLACK);
	}
	
	/**
	 * Constructs a new {@code DisneyPBRTMaterial} instance.
	 * <p>
	 * If either {@code textureColor}, {@code textureEmission}, {@code textureScatterDistance}, {@code textureAnisotropic}, {@code textureClearCoat}, {@code textureClearCoatGloss}, {@code textureDiffuseTransmission}, {@code textureEta},
	 * {@code textureFlatness}, {@code textureMetallic}, {@code textureRoughness}, {@code textureSheen}, {@code textureSheenTint} or {@code textureSpecularTint} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new DisneyPBRTMaterial(textureColor, textureEmission, textureScatterDistance, textureAnisotropic, textureClearCoat, textureClearCoatGloss, textureDiffuseTransmission, textureEta, textureFlatness, textureMetallic, textureRoughness, textureSheen, textureSheenTint, textureSpecularTint, ConstantTexture.BLACK);
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
	public DisneyPBRTMaterial(final Texture textureColor, final Texture textureEmission, final Texture textureScatterDistance, final Texture textureAnisotropic, final Texture textureClearCoat, final Texture textureClearCoatGloss, final Texture textureDiffuseTransmission, final Texture textureEta, final Texture textureFlatness, final Texture textureMetallic, final Texture textureRoughness, final Texture textureSheen, final Texture textureSheenTint, final Texture textureSpecularTint) {
		this(textureColor, textureEmission, textureScatterDistance, textureAnisotropic, textureClearCoat, textureClearCoatGloss, textureDiffuseTransmission, textureEta, textureFlatness, textureMetallic, textureRoughness, textureSheen, textureSheenTint, textureSpecularTint, ConstantTexture.BLACK);
	}
	
	/**
	 * Constructs a new {@code DisneyPBRTMaterial} instance.
	 * <p>
	 * If either {@code textureColor}, {@code textureEmission}, {@code textureScatterDistance}, {@code textureAnisotropic}, {@code textureClearCoat}, {@code textureClearCoatGloss}, {@code textureDiffuseTransmission}, {@code textureEta},
	 * {@code textureFlatness}, {@code textureMetallic}, {@code textureRoughness}, {@code textureSheen}, {@code textureSheenTint}, {@code textureSpecularTint} or {@code textureSpecularTransmission} are {@code null}, a {@code NullPointerException} will
	 * be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new DisneyPBRTMaterial(textureColor, textureEmission, textureScatterDistance, textureAnisotropic, textureClearCoat, textureClearCoatGloss, textureDiffuseTransmission, textureEta, textureFlatness, textureMetallic, textureRoughness, textureSheen, textureSheenTint, textureSpecularTint, textureSpecularTransmission, false);
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
	public DisneyPBRTMaterial(final Texture textureColor, final Texture textureEmission, final Texture textureScatterDistance, final Texture textureAnisotropic, final Texture textureClearCoat, final Texture textureClearCoatGloss, final Texture textureDiffuseTransmission, final Texture textureEta, final Texture textureFlatness, final Texture textureMetallic, final Texture textureRoughness, final Texture textureSheen, final Texture textureSheenTint, final Texture textureSpecularTint, final Texture textureSpecularTransmission) {
		this(textureColor, textureEmission, textureScatterDistance, textureAnisotropic, textureClearCoat, textureClearCoatGloss, textureDiffuseTransmission, textureEta, textureFlatness, textureMetallic, textureRoughness, textureSheen, textureSheenTint, textureSpecularTint, textureSpecularTransmission, false);
	}
	
	/**
	 * Constructs a new {@code DisneyPBRTMaterial} instance.
	 * <p>
	 * If either {@code textureColor}, {@code textureEmission}, {@code textureScatterDistance}, {@code textureAnisotropic}, {@code textureClearCoat}, {@code textureClearCoatGloss}, {@code textureDiffuseTransmission}, {@code textureEta},
	 * {@code textureFlatness}, {@code textureMetallic}, {@code textureRoughness}, {@code textureSheen}, {@code textureSheenTint}, {@code textureSpecularTint} or {@code textureSpecularTransmission} are {@code null}, a {@code NullPointerException} will
	 * be thrown.
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
	 * @param isThin {@code true} if, and only if, this {@code DisneyPBRTMaterial} instance is thin, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, either {@code textureColor}, {@code textureEmission}, {@code textureScatterDistance}, {@code textureAnisotropic}, {@code textureClearCoat}, {@code textureClearCoatGloss},
	 *                              {@code textureDiffuseTransmission}, {@code textureEta}, {@code textureFlatness}, {@code textureMetallic}, {@code textureRoughness}, {@code textureSheen}, {@code textureSheenTint}, {@code textureSpecularTint} or
	 *                              {@code textureSpecularTransmission} are {@code null}
	 */
	public DisneyPBRTMaterial(final Texture textureColor, final Texture textureEmission, final Texture textureScatterDistance, final Texture textureAnisotropic, final Texture textureClearCoat, final Texture textureClearCoatGloss, final Texture textureDiffuseTransmission, final Texture textureEta, final Texture textureFlatness, final Texture textureMetallic, final Texture textureRoughness, final Texture textureSheen, final Texture textureSheenTint, final Texture textureSpecularTint, final Texture textureSpecularTransmission, final boolean isThin) {
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
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link Color3F} instance with the emittance of this {@code DisneyPBRTMaterial} instance at {@code intersection}.
	 * <p>
	 * If {@code intersection} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param intersection an {@link Intersection} instance
	 * @return a {@code Color3F} instance with the emittance of this {@code DisneyPBRTMaterial} instance at {@code intersection}
	 * @throws NullPointerException thrown if, and only if, {@code intersection} is {@code null}
	 */
	@Override
	public Color3F emittance(final Intersection intersection) {
		return this.textureEmission.getColor(intersection);
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
//				final Color3F colorColor = Color3F.saturate(this.textureColor.getColor(intersection), 0.0F, Float.MAX_VALUE);
				
//				final float floatEta = this.textureEta.getFloat(intersection);
				
//				TODO: Add support for BSSRDF.
			}
		}
		
		return Optional.empty();
	}
	
	/**
	 * Computes the {@link PBRTBSDF} at {@code intersection}.
	 * <p>
	 * Returns an optional {@code PBRTBSDF} instance.
	 * <p>
	 * If either {@code intersection} or {@code transportMode} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param intersection the {@link Intersection} to compute the {@code PBRTBSDF} for
	 * @param transportMode the {@link TransportMode} to use
	 * @param isAllowingMultipleLobes {@code true} if, and only if, multiple lobes are allowed, {@code false} otherwise
	 * @return an optional {@code PBRTBSDF} instance
	 * @throws NullPointerException thrown if, and only if, either {@code intersection} or {@code transportMode} are {@code null}
	 */
	@Override
	public Optional<PBRTBSDF> computeBSDF(final Intersection intersection, final TransportMode transportMode, final boolean isAllowingMultipleLobes) {
		Objects.requireNonNull(intersection, "intersection == null");
		Objects.requireNonNull(transportMode, "transportMode == null");
		
		final List<PBRTBXDF> pBRTBXDFs = new ArrayList<>();
		
		final float floatAnisotropic = this.textureAnisotropic.getFloat(intersection);
		final float floatClearCoat = this.textureClearCoat.getFloat(intersection);
		final float floatDiffuseTransmission = this.textureDiffuseTransmission.getFloat(intersection) / 2.0F;
		final float floatEta = this.textureEta.getFloat(intersection);
		final float floatMetallic = this.textureMetallic.getFloat(intersection);
		final float floatRoughness = this.textureRoughness.getFloat(intersection);
		final float floatSheen = this.textureSheen.getFloat(intersection);
		final float floatSpecularTint = this.textureSpecularTint.getFloat(intersection);
		final float floatSpecularTransmission = this.textureSpecularTransmission.getFloat(intersection);
		
		final Color3F colorColor = Color3F.saturate(this.textureColor.getColor(intersection), 0.0F, Float.MAX_VALUE);
		final Color3F colorTint = Color3F.normalizeLuminance(colorColor);
		final Color3F colorSheen = floatSheen > 0.0F ? Color3F.blend(Color3F.WHITE, colorTint, this.textureSheenTint.getFloat(intersection)) : Color3F.BLACK;
		
		final float diffuseWeight = (1.0F - floatMetallic) * (1.0F - floatSpecularTransmission);
		
		if(diffuseWeight > 0.0F) {
			if(this.isThin) {
				final float floatFlatness = this.textureFlatness.getFloat(intersection);
				
				final Color3F colorReflectanceScale0 = Color3F.multiply(colorColor, diffuseWeight * (1.0F - floatFlatness) * (1.0F - floatDiffuseTransmission));
				final Color3F colorReflectanceScale1 = Color3F.multiply(colorColor, diffuseWeight * (0.0F + floatFlatness) * (1.0F - floatDiffuseTransmission));
				
				pBRTBXDFs.add(new DisneyDiffusePBRTBRDF(colorReflectanceScale0));
				pBRTBXDFs.add(new DisneyFakeSSPBRTBRDF(colorReflectanceScale1, floatRoughness));
			} else {
				final Color3F colorScatterDistance = this.textureScatterDistance.getColor(intersection);
				
				if(colorScatterDistance.isBlack()) {
					pBRTBXDFs.add(new DisneyDiffusePBRTBRDF(Color3F.multiply(colorColor, diffuseWeight)));
				} else {
					pBRTBXDFs.add(new SpecularPBRTBTDF(Color3F.WHITE, transportMode, 1.0F, floatEta));
				}
			}
			
			pBRTBXDFs.add(new DisneyRetroPBRTBRDF(Color3F.multiply(colorColor, diffuseWeight), floatRoughness));
			
			if(floatSheen > 0.0F) {
				pBRTBXDFs.add(new DisneySheenPBRTBRDF(Color3F.multiply(colorSheen, diffuseWeight * floatSheen)));
			}
		}
		
		final float aspect = sqrt(1.0F - floatAnisotropic * 0.9F);
		
		final float alphaX = max(0.001F, floatRoughness * floatRoughness / aspect);
		final float alphaY = max(0.001F, floatRoughness * floatRoughness * aspect);
		
		final MicrofacetDistribution microfacetDistribution = new TrowbridgeReitzMicrofacetDistribution(true, true, alphaX, alphaY);
		
		final float floatR0 = ((floatEta - 1.0F) * (floatEta - 1.0F)) / ((floatEta + 1.0F) * (floatEta + 1.0F));
		
		final Color3F colorSpecularR0 = Color3F.blend(Color3F.multiply(Color3F.blend(Color3F.WHITE, colorTint, floatSpecularTint), floatR0), colorColor, floatMetallic);
		
		final Fresnel fresnel = new DisneyFresnel(colorSpecularR0, floatEta, floatMetallic);
		
		pBRTBXDFs.add(new TorranceSparrowPBRTBRDF(Color3F.WHITE, fresnel, microfacetDistribution));
		
		if(floatClearCoat > 0.0F) {
			pBRTBXDFs.add(new DisneyClearCoatPBRTBRDF(lerp(0.1F, 0.001F, this.textureClearCoatGloss.getFloat(intersection)), floatClearCoat));
		}
		
		if(floatSpecularTransmission > 0.0F) {
			final Color3F transmittanceScale = Color3F.multiply(Color3F.sqrt(colorColor), floatSpecularTransmission);
			
			if(this.isThin) {
				final float floatRoughnessScaled = (0.65F * floatEta - 0.35F) * floatRoughness;
				
				final float alphaXScaled = max(0.001F, floatRoughnessScaled * floatRoughnessScaled / aspect);
				final float alphaYScaled = max(0.001F, floatRoughnessScaled * floatRoughnessScaled * aspect);
				
				final MicrofacetDistribution microfacetDistributionScaled = new TrowbridgeReitzMicrofacetDistribution(true, false, alphaXScaled, alphaYScaled);
				
				pBRTBXDFs.add(new TorranceSparrowPBRTBTDF(transmittanceScale, microfacetDistributionScaled, transportMode, 1.0F, floatEta));
			} else {
				pBRTBXDFs.add(new TorranceSparrowPBRTBTDF(transmittanceScale, microfacetDistribution, transportMode, 1.0F, floatEta));
			}
		}
		
		if(this.isThin) {
			pBRTBXDFs.add(new LambertianPBRTBTDF(Color3F.multiply(colorColor, floatDiffuseTransmission)));
		}
		
		return Optional.of(new PBRTBSDF(intersection, pBRTBXDFs));
	}
	
	/**
	 * Returns a {@code String} with the name of this {@code DisneyPBRTMaterial} instance.
	 * 
	 * @return a {@code String} with the name of this {@code DisneyPBRTMaterial} instance
	 */
	@Override
	public String getName() {
		return NAME;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code DisneyPBRTMaterial} instance.
	 * 
	 * @return a {@code String} representation of this {@code DisneyPBRTMaterial} instance
	 */
	@Override
	public String toString() {
		return String.format("new DisneyPBRTMaterial(%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)", this.textureColor, this.textureEmission, this.textureScatterDistance, this.textureAnisotropic, this.textureClearCoat, this.textureClearCoatGloss, this.textureDiffuseTransmission, this.textureEta, this.textureFlatness, this.textureMetallic, this.textureRoughness, this.textureSheen, this.textureSheenTint, this.textureSpecularTint, this.textureSpecularTransmission, Boolean.toString(this.isThin));
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
	 * Compares {@code object} to this {@code DisneyPBRTMaterial} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code DisneyPBRTMaterial}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code DisneyPBRTMaterial} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code DisneyPBRTMaterial}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof DisneyPBRTMaterial)) {
			return false;
		} else if(!Objects.equals(this.textureAnisotropic, DisneyPBRTMaterial.class.cast(object).textureAnisotropic)) {
			return false;
		} else if(!Objects.equals(this.textureClearCoat, DisneyPBRTMaterial.class.cast(object).textureClearCoat)) {
			return false;
		} else if(!Objects.equals(this.textureClearCoatGloss, DisneyPBRTMaterial.class.cast(object).textureClearCoatGloss)) {
			return false;
		} else if(!Objects.equals(this.textureColor, DisneyPBRTMaterial.class.cast(object).textureColor)) {
			return false;
		} else if(!Objects.equals(this.textureDiffuseTransmission, DisneyPBRTMaterial.class.cast(object).textureDiffuseTransmission)) {
			return false;
		} else if(!Objects.equals(this.textureEmission, DisneyPBRTMaterial.class.cast(object).textureEmission)) {
			return false;
		} else if(!Objects.equals(this.textureEta, DisneyPBRTMaterial.class.cast(object).textureEta)) {
			return false;
		} else if(!Objects.equals(this.textureFlatness, DisneyPBRTMaterial.class.cast(object).textureFlatness)) {
			return false;
		} else if(!Objects.equals(this.textureMetallic, DisneyPBRTMaterial.class.cast(object).textureMetallic)) {
			return false;
		} else if(!Objects.equals(this.textureRoughness, DisneyPBRTMaterial.class.cast(object).textureRoughness)) {
			return false;
		} else if(!Objects.equals(this.textureScatterDistance, DisneyPBRTMaterial.class.cast(object).textureScatterDistance)) {
			return false;
		} else if(!Objects.equals(this.textureSheen, DisneyPBRTMaterial.class.cast(object).textureSheen)) {
			return false;
		} else if(!Objects.equals(this.textureSheenTint, DisneyPBRTMaterial.class.cast(object).textureSheenTint)) {
			return false;
		} else if(!Objects.equals(this.textureSpecularTint, DisneyPBRTMaterial.class.cast(object).textureSpecularTint)) {
			return false;
		} else if(!Objects.equals(this.textureSpecularTransmission, DisneyPBRTMaterial.class.cast(object).textureSpecularTransmission)) {
			return false;
		} else if(this.isThin != DisneyPBRTMaterial.class.cast(object).isThin) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code DisneyPBRTMaterial} instance is thin, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, this {@code DisneyPBRTMaterial} instance is thin, {@code false} otherwise
	 */
	public boolean isThin() {
		return this.isThin;
	}
	
	/**
	 * Returns an {@code int} with the ID of this {@code DisneyPBRTMaterial} instance.
	 * 
	 * @return an {@code int} with the ID of this {@code DisneyPBRTMaterial} instance
	 */
	@Override
	public int getID() {
		return ID;
	}
	
	/**
	 * Returns a hash code for this {@code DisneyPBRTMaterial} instance.
	 * 
	 * @return a hash code for this {@code DisneyPBRTMaterial} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.textureAnisotropic, this.textureClearCoat, this.textureClearCoatGloss, this.textureColor, this.textureDiffuseTransmission, this.textureEmission, this.textureEta, this.textureFlatness, this.textureMetallic, this.textureRoughness, this.textureScatterDistance, this.textureSheen, this.textureSheenTint, this.textureSpecularTint, this.textureSpecularTransmission, Boolean.valueOf(this.isThin));
	}
}