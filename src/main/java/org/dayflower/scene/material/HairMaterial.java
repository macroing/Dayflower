/**
 * Copyright 2014 - 2025 J&#246;rgen Lundgren
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
import static org.dayflower.utility.Floats.max;

import java.util.Objects;

import org.dayflower.color.Color3F;
import org.dayflower.scene.BSDF;
import org.dayflower.scene.Intersection;
import org.dayflower.scene.Material;
import org.dayflower.scene.ScatteringFunctions;
import org.dayflower.scene.TransportMode;
import org.dayflower.scene.bxdf.HairBXDF;
import org.dayflower.scene.modifier.Modifier;
import org.dayflower.scene.modifier.NoOpModifier;
import org.dayflower.scene.texture.ConstantTexture;
import org.dayflower.scene.texture.Texture;

import org.macroing.java.util.visitor.NodeHierarchicalVisitor;
import org.macroing.java.util.visitor.NodeTraversalException;

/**
 * A {@code HairMaterial} is an implementation of {@link Material} that represents hair.
 * <p>
 * This class is immutable and thread-safe as long as the {@link Modifier} instance and all {@link Texture} instances are.
 * <p>
 * This {@code Material} implementation is not supported on the GPU.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class HairMaterial implements Material {
	/**
	 * The name of this {@code HairMaterial} class.
	 */
	public static final String NAME = "Hair";
	
	/**
	 * The ID of this {@code HairMaterial} class.
	 */
	public static final int ID = 9;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final Modifier modifier;
	private final Texture textureAlpha;
	private final Texture textureBetaM;
	private final Texture textureBetaN;
	private final Texture textureColor;
	private final Texture textureEmission;
	private final Texture textureEta;
	private final Texture textureEumelanin;
	private final Texture texturePheomelanin;
	private final Texture textureSigmaA;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code HairMaterial} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new HairMaterial(Color3F.BLACK);
	 * }
	 * </pre>
	 */
	public HairMaterial() {
		this(Color3F.BLACK);
	}
	
	/**
	 * Constructs a new {@code HairMaterial} instance.
	 * <p>
	 * If {@code colorColor} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new HairMaterial(colorColor, Color3F.BLACK);
	 * }
	 * </pre>
	 * 
	 * @param colorColor a {@link Color3F} instance for the color
	 * @throws NullPointerException thrown if, and only if, {@code colorColor} is {@code null}
	 */
	public HairMaterial(final Color3F colorColor) {
		this(colorColor, Color3F.BLACK);
	}
	
	/**
	 * Constructs a new {@code HairMaterial} instance.
	 * <p>
	 * If either {@code colorColor} or {@code colorEmission} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new HairMaterial(colorColor, colorEmission, HairBXDF.computeSigmaAFromConcentration(1.3F, 0.0F));
	 * }
	 * </pre>
	 * 
	 * @param colorColor a {@link Color3F} instance for the color
	 * @param colorEmission a {@code Color3F} instance for emission
	 * @throws NullPointerException thrown if, and only if, either {@code colorColor} or {@code colorEmission} are {@code null}
	 */
	public HairMaterial(final Color3F colorColor, final Color3F colorEmission) {
		this(colorColor, colorEmission, HairBXDF.computeSigmaAFromConcentration(1.3F, 0.0F));
	}
	
	/**
	 * Constructs a new {@code HairMaterial} instance.
	 * <p>
	 * If either {@code colorColor}, {@code colorEmission} or {@code colorSigmaA} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new HairMaterial(colorColor, colorEmission, colorSigmaA, 2.0F);
	 * }
	 * </pre>
	 * 
	 * @param colorColor a {@link Color3F} instance for the color
	 * @param colorEmission a {@code Color3F} instance for emission
	 * @param colorSigmaA a {@code Color3F} instance for the Sigma A value
	 * @throws NullPointerException thrown if, and only if, either {@code colorColor}, {@code colorEmission} or {@code colorSigmaA} are {@code null}
	 */
	public HairMaterial(final Color3F colorColor, final Color3F colorEmission, final Color3F colorSigmaA) {
		this(colorColor, colorEmission, colorSigmaA, 2.0F);
	}
	
	/**
	 * Constructs a new {@code HairMaterial} instance.
	 * <p>
	 * If either {@code colorColor}, {@code colorEmission} or {@code colorSigmaA} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new HairMaterial(colorColor, colorEmission, colorSigmaA, floatAlpha, 0.3F);
	 * }
	 * </pre>
	 * 
	 * @param colorColor a {@link Color3F} instance for the color
	 * @param colorEmission a {@code Color3F} instance for emission
	 * @param colorSigmaA a {@code Color3F} instance for the Sigma A value
	 * @param floatAlpha a {@code float} for the Alpha value
	 * @throws NullPointerException thrown if, and only if, either {@code colorColor}, {@code colorEmission} or {@code colorSigmaA} are {@code null}
	 */
	public HairMaterial(final Color3F colorColor, final Color3F colorEmission, final Color3F colorSigmaA, final float floatAlpha) {
		this(colorColor, colorEmission, colorSigmaA, floatAlpha, 0.3F);
	}
	
	/**
	 * Constructs a new {@code HairMaterial} instance.
	 * <p>
	 * If either {@code colorColor}, {@code colorEmission} or {@code colorSigmaA} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new HairMaterial(colorColor, colorEmission, colorSigmaA, floatAlpha, floatBetaM, 0.3F);
	 * }
	 * </pre>
	 * 
	 * @param colorColor a {@link Color3F} instance for the color
	 * @param colorEmission a {@code Color3F} instance for emission
	 * @param colorSigmaA a {@code Color3F} instance for the Sigma A value
	 * @param floatAlpha a {@code float} for the Alpha value
	 * @param floatBetaM a {@code float} for the Beta M value
	 * @throws NullPointerException thrown if, and only if, either {@code colorColor}, {@code colorEmission} or {@code colorSigmaA} are {@code null}
	 */
	public HairMaterial(final Color3F colorColor, final Color3F colorEmission, final Color3F colorSigmaA, final float floatAlpha, final float floatBetaM) {
		this(colorColor, colorEmission, colorSigmaA, floatAlpha, floatBetaM, 0.3F);
	}
	
	/**
	 * Constructs a new {@code HairMaterial} instance.
	 * <p>
	 * If either {@code colorColor}, {@code colorEmission} or {@code colorSigmaA} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new HairMaterial(colorColor, colorEmission, colorSigmaA, floatAlpha, floatBetaM, floatBetaN, 1.55F);
	 * }
	 * </pre>
	 * 
	 * @param colorColor a {@link Color3F} instance for the color
	 * @param colorEmission a {@code Color3F} instance for emission
	 * @param colorSigmaA a {@code Color3F} instance for the Sigma A value
	 * @param floatAlpha a {@code float} for the Alpha value
	 * @param floatBetaM a {@code float} for the Beta M value
	 * @param floatBetaN a {@code float} for the Beta N value
	 * @throws NullPointerException thrown if, and only if, either {@code colorColor}, {@code colorEmission} or {@code colorSigmaA} are {@code null}
	 */
	public HairMaterial(final Color3F colorColor, final Color3F colorEmission, final Color3F colorSigmaA, final float floatAlpha, final float floatBetaM, final float floatBetaN) {
		this(colorColor, colorEmission, colorSigmaA, floatAlpha, floatBetaM, floatBetaN, 1.55F);
	}
	
	/**
	 * Constructs a new {@code HairMaterial} instance.
	 * <p>
	 * If either {@code colorColor}, {@code colorEmission} or {@code colorSigmaA} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new HairMaterial(colorColor, colorEmission, colorSigmaA, floatAlpha, floatBetaM, floatBetaN, floatEta, 0.0F);
	 * }
	 * </pre>
	 * 
	 * @param colorColor a {@link Color3F} instance for the color
	 * @param colorEmission a {@code Color3F} instance for emission
	 * @param colorSigmaA a {@code Color3F} instance for the Sigma A value
	 * @param floatAlpha a {@code float} for the Alpha value
	 * @param floatBetaM a {@code float} for the Beta M value
	 * @param floatBetaN a {@code float} for the Beta N value
	 * @param floatEta a {@code float} for the index of refraction (IOR)
	 * @throws NullPointerException thrown if, and only if, either {@code colorColor}, {@code colorEmission} or {@code colorSigmaA} are {@code null}
	 */
	public HairMaterial(final Color3F colorColor, final Color3F colorEmission, final Color3F colorSigmaA, final float floatAlpha, final float floatBetaM, final float floatBetaN, final float floatEta) {
		this(colorColor, colorEmission, colorSigmaA, floatAlpha, floatBetaM, floatBetaN, floatEta, 0.0F);
	}
	
	/**
	 * Constructs a new {@code HairMaterial} instance.
	 * <p>
	 * If either {@code colorColor}, {@code colorEmission} or {@code colorSigmaA} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new HairMaterial(colorColor, colorEmission, colorSigmaA, floatAlpha, floatBetaM, floatBetaN, floatEta, floatEumelanin, 0.0F);
	 * }
	 * </pre>
	 * 
	 * @param colorColor a {@link Color3F} instance for the color
	 * @param colorEmission a {@code Color3F} instance for emission
	 * @param colorSigmaA a {@code Color3F} instance for the Sigma A value
	 * @param floatAlpha a {@code float} for the Alpha value
	 * @param floatBetaM a {@code float} for the Beta M value
	 * @param floatBetaN a {@code float} for the Beta N value
	 * @param floatEta a {@code float} for the index of refraction (IOR)
	 * @param floatEumelanin a {@code float} for the Eumelanin value
	 * @throws NullPointerException thrown if, and only if, either {@code colorColor}, {@code colorEmission} or {@code colorSigmaA} are {@code null}
	 */
	public HairMaterial(final Color3F colorColor, final Color3F colorEmission, final Color3F colorSigmaA, final float floatAlpha, final float floatBetaM, final float floatBetaN, final float floatEta, final float floatEumelanin) {
		this(colorColor, colorEmission, colorSigmaA, floatAlpha, floatBetaM, floatBetaN, floatEta, floatEumelanin, 0.0F);
	}
	
	/**
	 * Constructs a new {@code HairMaterial} instance.
	 * <p>
	 * If either {@code colorColor}, {@code colorEmission} or {@code colorSigmaA} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new HairMaterial(colorColor, colorEmission, colorSigmaA, floatAlpha, floatBetaM, floatBetaN, floatEta, floatEumelanin, floatPheomelanin, new NoOpModifier());
	 * }
	 * </pre>
	 * 
	 * @param colorColor a {@link Color3F} instance for the color
	 * @param colorEmission a {@code Color3F} instance for emission
	 * @param colorSigmaA a {@code Color3F} instance for the Sigma A value
	 * @param floatAlpha a {@code float} for the Alpha value
	 * @param floatBetaM a {@code float} for the Beta M value
	 * @param floatBetaN a {@code float} for the Beta N value
	 * @param floatEta a {@code float} for the index of refraction (IOR)
	 * @param floatEumelanin a {@code float} for the Eumelanin value
	 * @param floatPheomelanin a {@code float} for the Pheomelanin value
	 * @throws NullPointerException thrown if, and only if, either {@code colorColor}, {@code colorEmission} or {@code colorSigmaA} are {@code null}
	 */
	public HairMaterial(final Color3F colorColor, final Color3F colorEmission, final Color3F colorSigmaA, final float floatAlpha, final float floatBetaM, final float floatBetaN, final float floatEta, final float floatEumelanin, final float floatPheomelanin) {
		this(colorColor, colorEmission, colorSigmaA, floatAlpha, floatBetaM, floatBetaN, floatEta, floatEumelanin, floatPheomelanin, new NoOpModifier());
	}
	
	/**
	 * Constructs a new {@code HairMaterial} instance.
	 * <p>
	 * If either {@code colorColor}, {@code colorEmission}, {@code colorSigmaA} or {@code modifier} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param colorColor a {@link Color3F} instance for the color
	 * @param colorEmission a {@code Color3F} instance for emission
	 * @param colorSigmaA a {@code Color3F} instance for the Sigma A value
	 * @param floatAlpha a {@code float} for the Alpha value
	 * @param floatBetaM a {@code float} for the Beta M value
	 * @param floatBetaN a {@code float} for the Beta N value
	 * @param floatEta a {@code float} for the index of refraction (IOR)
	 * @param floatEumelanin a {@code float} for the Eumelanin value
	 * @param floatPheomelanin a {@code float} for the Pheomelanin value
	 * @param modifier a {@link Modifier} instance
	 * @throws NullPointerException thrown if, and only if, either {@code colorColor}, {@code colorEmission}, {@code colorSigmaA} or {@code modifier} are {@code null}
	 */
	public HairMaterial(final Color3F colorColor, final Color3F colorEmission, final Color3F colorSigmaA, final float floatAlpha, final float floatBetaM, final float floatBetaN, final float floatEta, final float floatEumelanin, final float floatPheomelanin, final Modifier modifier) {
		this.textureColor = new ConstantTexture(Objects.requireNonNull(colorColor, "colorColor == null"));
		this.textureEmission = new ConstantTexture(Objects.requireNonNull(colorEmission, "colorEmission == null"));
		this.textureSigmaA = new ConstantTexture(Objects.requireNonNull(colorSigmaA, "colorSigmaA == null"));
		this.textureAlpha = new ConstantTexture(floatAlpha);
		this.textureBetaM = new ConstantTexture(floatBetaM);
		this.textureBetaN = new ConstantTexture(floatBetaN);
		this.textureEta = new ConstantTexture(floatEta);
		this.textureEumelanin = new ConstantTexture(floatEumelanin);
		this.texturePheomelanin = new ConstantTexture(floatPheomelanin);
		this.modifier = Objects.requireNonNull(modifier, "modifier == null");
	}
	
	/**
	 * Constructs a new {@code HairMaterial} instance.
	 * <p>
	 * If {@code textureColor} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new HairMaterial(textureColor, ConstantTexture.BLACK);
	 * }
	 * </pre>
	 * 
	 * @param textureColor a {@link Texture} instance for the color
	 * @throws NullPointerException thrown if, and only if, {@code textureColor} is {@code null}
	 */
	public HairMaterial(final Texture textureColor) {
		this(textureColor, ConstantTexture.BLACK);
	}
	
	/**
	 * Constructs a new {@code HairMaterial} instance.
	 * <p>
	 * If either {@code textureColor} or {@code textureEmission} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new HairMaterial(textureColor, textureEmission, new ConstantTexture(HairBXDF.computeSigmaAFromConcentration(1.3F, 0.0F)));
	 * }
	 * </pre>
	 * 
	 * @param textureColor a {@link Texture} instance for the color
	 * @param textureEmission a {@code Texture} instance for emission
	 * @throws NullPointerException thrown if, and only if, either {@code textureColor} or {@code textureEmission} are {@code null}
	 */
	public HairMaterial(final Texture textureColor, final Texture textureEmission) {
		this(textureColor, textureEmission, new ConstantTexture(HairBXDF.computeSigmaAFromConcentration(1.3F, 0.0F)));
	}
	
	/**
	 * Constructs a new {@code HairMaterial} instance.
	 * <p>
	 * If either {@code textureColor}, {@code textureEmission} or {@code textureSigmaA} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new HairMaterial(textureColor, textureEmission, textureSigmaA, ConstantTexture.GRAY_2_00);
	 * }
	 * </pre>
	 * 
	 * @param textureColor a {@link Texture} instance for the color
	 * @param textureEmission a {@code Texture} instance for emission
	 * @param textureSigmaA a {@code Texture} instance for the Sigma A value
	 * @throws NullPointerException thrown if, and only if, either {@code textureColor}, {@code textureEmission} or {@code textureSigmaA} are {@code null}
	 */
	public HairMaterial(final Texture textureColor, final Texture textureEmission, final Texture textureSigmaA) {
		this(textureColor, textureEmission, textureSigmaA, ConstantTexture.GRAY_2_00);
	}
	
	/**
	 * Constructs a new {@code HairMaterial} instance.
	 * <p>
	 * If either {@code textureColor}, {@code textureEmission}, {@code textureSigmaA} or {@code textureAlpha} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new HairMaterial(textureColor, textureEmission, textureSigmaA, textureAlpha, ConstantTexture.GRAY_0_30);
	 * }
	 * </pre>
	 * 
	 * @param textureColor a {@link Texture} instance for the color
	 * @param textureEmission a {@code Texture} instance for emission
	 * @param textureSigmaA a {@code Texture} instance for the Sigma A value
	 * @param textureAlpha a {@code Texture} instance for the Alpha value
	 * @throws NullPointerException thrown if, and only if, either {@code textureColor}, {@code textureEmission}, {@code textureSigmaA} or {@code textureAlpha} are {@code null}
	 */
	public HairMaterial(final Texture textureColor, final Texture textureEmission, final Texture textureSigmaA, final Texture textureAlpha) {
		this(textureColor, textureEmission, textureSigmaA, textureAlpha, ConstantTexture.GRAY_0_30);
	}
	
	/**
	 * Constructs a new {@code HairMaterial} instance.
	 * <p>
	 * If either {@code textureColor}, {@code textureEmission}, {@code textureSigmaA}, {@code textureAlpha} or {@code textureBetaM} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new HairMaterial(textureColor, textureEmission, textureSigmaA, textureAlpha, textureBetaM, ConstantTexture.GRAY_0_30);
	 * }
	 * </pre>
	 * 
	 * @param textureColor a {@link Texture} instance for the color
	 * @param textureEmission a {@code Texture} instance for emission
	 * @param textureSigmaA a {@code Texture} instance for the Sigma A value
	 * @param textureAlpha a {@code Texture} instance for the Alpha value
	 * @param textureBetaM a {@code Texture} instance for the Beta M value
	 * @throws NullPointerException thrown if, and only if, either {@code textureColor}, {@code textureEmission}, {@code textureSigmaA}, {@code textureAlpha} or {@code textureBetaM} are {@code null}
	 */
	public HairMaterial(final Texture textureColor, final Texture textureEmission, final Texture textureSigmaA, final Texture textureAlpha, final Texture textureBetaM) {
		this(textureColor, textureEmission, textureSigmaA, textureAlpha, textureBetaM, ConstantTexture.GRAY_0_30);
	}
	
	/**
	 * Constructs a new {@code HairMaterial} instance.
	 * <p>
	 * If either {@code textureColor}, {@code textureEmission}, {@code textureSigmaA}, {@code textureAlpha}, {@code textureBetaM} or {@code textureBetaN} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new HairMaterial(textureColor, textureEmission, textureSigmaA, textureAlpha, textureBetaM, textureBetaN, ConstantTexture.GRAY_1_55);
	 * }
	 * </pre>
	 * 
	 * @param textureColor a {@link Texture} instance for the color
	 * @param textureEmission a {@code Texture} instance for emission
	 * @param textureSigmaA a {@code Texture} instance for the Sigma A value
	 * @param textureAlpha a {@code Texture} instance for the Alpha value
	 * @param textureBetaM a {@code Texture} instance for the Beta M value
	 * @param textureBetaN a {@code Texture} instance for the Beta N value
	 * @throws NullPointerException thrown if, and only if, either {@code textureColor}, {@code textureEmission}, {@code textureSigmaA}, {@code textureAlpha}, {@code textureBetaM} or {@code textureBetaN} are {@code null}
	 */
	public HairMaterial(final Texture textureColor, final Texture textureEmission, final Texture textureSigmaA, final Texture textureAlpha, final Texture textureBetaM, final Texture textureBetaN) {
		this(textureColor, textureEmission, textureSigmaA, textureAlpha, textureBetaM, textureBetaN, ConstantTexture.GRAY_1_55);
	}
	
	/**
	 * Constructs a new {@code HairMaterial} instance.
	 * <p>
	 * If either {@code textureColor}, {@code textureEmission}, {@code textureSigmaA}, {@code textureAlpha}, {@code textureBetaM}, {@code textureBetaN} or {@code textureEta} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new HairMaterial(textureColor, textureEmission, textureSigmaA, textureAlpha, textureBetaM, textureBetaN, textureEta, ConstantTexture.BLACK);
	 * }
	 * </pre>
	 * 
	 * @param textureColor a {@link Texture} instance for the color
	 * @param textureEmission a {@code Texture} instance for emission
	 * @param textureSigmaA a {@code Texture} instance for the Sigma A value
	 * @param textureAlpha a {@code Texture} instance for the Alpha value
	 * @param textureBetaM a {@code Texture} instance for the Beta M value
	 * @param textureBetaN a {@code Texture} instance for the Beta N value
	 * @param textureEta a {@code Texture} instance for the index of refraction (IOR)
	 * @throws NullPointerException thrown if, and only if, either {@code textureColor}, {@code textureEmission}, {@code textureSigmaA}, {@code textureAlpha}, {@code textureBetaM}, {@code textureBetaN} or {@code textureEta} are {@code null}
	 */
	public HairMaterial(final Texture textureColor, final Texture textureEmission, final Texture textureSigmaA, final Texture textureAlpha, final Texture textureBetaM, final Texture textureBetaN, final Texture textureEta) {
		this(textureColor, textureEmission, textureSigmaA, textureAlpha, textureBetaM, textureBetaN, textureEta, ConstantTexture.BLACK);
	}
	
	/**
	 * Constructs a new {@code HairMaterial} instance.
	 * <p>
	 * If either {@code textureColor}, {@code textureEmission}, {@code textureSigmaA}, {@code textureAlpha}, {@code textureBetaM}, {@code textureBetaN}, {@code textureEta} or {@code textureEumelanin} are {@code null}, a {@code NullPointerException}
	 * will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new HairMaterial(textureColor, textureEmission, textureSigmaA, textureAlpha, textureBetaM, textureBetaN, textureEta, textureEumelanin, ConstantTexture.BLACK);
	 * }
	 * </pre>
	 * 
	 * @param textureColor a {@link Texture} instance for the color
	 * @param textureEmission a {@code Texture} instance for emission
	 * @param textureSigmaA a {@code Texture} instance for the Sigma A value
	 * @param textureAlpha a {@code Texture} instance for the Alpha value
	 * @param textureBetaM a {@code Texture} instance for the Beta M value
	 * @param textureBetaN a {@code Texture} instance for the Beta N value
	 * @param textureEta a {@code Texture} instance for the index of refraction (IOR)
	 * @param textureEumelanin a {@code Texture} instance for the Eumelanin value
	 * @throws NullPointerException thrown if, and only if, either {@code textureColor}, {@code textureEmission}, {@code textureSigmaA}, {@code textureAlpha}, {@code textureBetaM}, {@code textureBetaN}, {@code textureEta} or {@code textureEumelanin}
	 *                              are {@code null}
	 */
	public HairMaterial(final Texture textureColor, final Texture textureEmission, final Texture textureSigmaA, final Texture textureAlpha, final Texture textureBetaM, final Texture textureBetaN, final Texture textureEta, final Texture textureEumelanin) {
		this(textureColor, textureEmission, textureSigmaA, textureAlpha, textureBetaM, textureBetaN, textureEta, textureEumelanin, ConstantTexture.BLACK);
	}
	
	/**
	 * Constructs a new {@code HairMaterial} instance.
	 * <p>
	 * If either {@code textureColor}, {@code textureEmission}, {@code textureSigmaA}, {@code textureAlpha}, {@code textureBetaM}, {@code textureBetaN}, {@code textureEta}, {@code textureEumelanin} or {@code texturePheomelanin} are {@code null}, a
	 * {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new HairMaterial(textureColor, textureEmission, textureSigmaA, textureAlpha, textureBetaM, textureBetaN, textureEta, textureEumelanin, texturePheomelanin, new NoOpModifier());
	 * }
	 * </pre>
	 * 
	 * @param textureColor a {@link Texture} instance for the color
	 * @param textureEmission a {@code Texture} instance for emission
	 * @param textureSigmaA a {@code Texture} instance for the Sigma A value
	 * @param textureAlpha a {@code Texture} instance for the Alpha value
	 * @param textureBetaM a {@code Texture} instance for the Beta M value
	 * @param textureBetaN a {@code Texture} instance for the Beta N value
	 * @param textureEta a {@code Texture} instance for the index of refraction (IOR)
	 * @param textureEumelanin a {@code Texture} instance for the Eumelanin value
	 * @param texturePheomelanin a {@code Texture} instance for the Pheomelanin value
	 * @throws NullPointerException thrown if, and only if, either {@code textureColor}, {@code textureEmission}, {@code textureSigmaA}, {@code textureAlpha}, {@code textureBetaM}, {@code textureBetaN}, {@code textureEta}, {@code textureEumelanin} or
	 *                              {@code texturePheomelanin} are {@code null}
	 */
	public HairMaterial(final Texture textureColor, final Texture textureEmission, final Texture textureSigmaA, final Texture textureAlpha, final Texture textureBetaM, final Texture textureBetaN, final Texture textureEta, final Texture textureEumelanin, final Texture texturePheomelanin) {
		this(textureColor, textureEmission, textureSigmaA, textureAlpha, textureBetaM, textureBetaN, textureEta, textureEumelanin, texturePheomelanin, new NoOpModifier());
	}
	
	/**
	 * Constructs a new {@code HairMaterial} instance.
	 * <p>
	 * If either {@code textureColor}, {@code textureEmission}, {@code textureSigmaA}, {@code textureAlpha}, {@code textureBetaM}, {@code textureBetaN}, {@code textureEta}, {@code textureEumelanin}, {@code texturePheomelanin} or {@code modifier}
	 * are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param textureColor a {@link Texture} instance for the color
	 * @param textureEmission a {@code Texture} instance for emission
	 * @param textureSigmaA a {@code Texture} instance for the Sigma A value
	 * @param textureAlpha a {@code Texture} instance for the Alpha value
	 * @param textureBetaM a {@code Texture} instance for the Beta M value
	 * @param textureBetaN a {@code Texture} instance for the Beta N value
	 * @param textureEta a {@code Texture} instance for the index of refraction (IOR)
	 * @param textureEumelanin a {@code Texture} instance for the Eumelanin value
	 * @param texturePheomelanin a {@code Texture} instance for the Pheomelanin value
	 * @param modifier a {@link Modifier} instance
	 * @throws NullPointerException thrown if, and only if, either {@code textureColor}, {@code textureEmission}, {@code textureSigmaA}, {@code textureAlpha}, {@code textureBetaM}, {@code textureBetaN}, {@code textureEta}, {@code textureEumelanin},
	 *                              {@code texturePheomelanin} or {@code modifier} are {@code null}
	 */
	public HairMaterial(final Texture textureColor, final Texture textureEmission, final Texture textureSigmaA, final Texture textureAlpha, final Texture textureBetaM, final Texture textureBetaN, final Texture textureEta, final Texture textureEumelanin, final Texture texturePheomelanin, final Modifier modifier) {
		this.textureColor = Objects.requireNonNull(textureColor, "textureColor == null");
		this.textureEmission = Objects.requireNonNull(textureEmission, "textureEmission == null");
		this.textureSigmaA = Objects.requireNonNull(textureSigmaA, "textureSigmaA == null");
		this.textureAlpha = Objects.requireNonNull(textureAlpha, "textureAlpha == null");
		this.textureBetaM = Objects.requireNonNull(textureBetaM, "textureBetaM == null");
		this.textureBetaN = Objects.requireNonNull(textureBetaN, "textureBetaN == null");
		this.textureEta = Objects.requireNonNull(textureEta, "textureEta == null");
		this.textureEumelanin = Objects.requireNonNull(textureEumelanin, "textureEumelanin == null");
		this.texturePheomelanin = Objects.requireNonNull(texturePheomelanin, "texturePheomelanin == null");
		this.modifier = Objects.requireNonNull(modifier, "modifier == null");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link Color3F} instance with the emittance of this {@code HairMaterial} instance at {@code intersection}.
	 * <p>
	 * If {@code intersection} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param intersection an {@link Intersection} instance
	 * @return a {@code Color3F} instance with the emittance of this {@code HairMaterial} instance at {@code intersection}
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
	 * Computes the {@link ScatteringFunctions} at {@code intersection}.
	 * <p>
	 * Returns a {@code ScatteringFunctions} instance.
	 * <p>
	 * If either {@code intersection} or {@code transportMode} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param intersection the {@link Intersection} to compute the {@code ScatteringFunctions} for
	 * @param transportMode the {@link TransportMode} to use
	 * @param isAllowingMultipleLobes {@code true} if, and only if, multiple lobes are allowed, {@code false} otherwise
	 * @return a {@code ScatteringFunctions} instance
	 * @throws NullPointerException thrown if, and only if, either {@code intersection} or {@code transportMode} are {@code null}
	 */
	@Override
	public ScatteringFunctions computeScatteringFunctions(final Intersection intersection, final TransportMode transportMode, final boolean isAllowingMultipleLobes) {
		Objects.requireNonNull(intersection, "intersection == null");
		Objects.requireNonNull(transportMode, "transportMode == null");
		
		this.modifier.modify(intersection);
		
		final float alpha = this.textureAlpha.getFloat(intersection);
		final float betaM = this.textureBetaM.getFloat(intersection);
		final float betaN = this.textureBetaN.getFloat(intersection);
		final float eta = this.textureEta.getFloat(intersection);
		
		final Color3F sigmaA = doComputeSigmaA(intersection, betaN);
		
		final float h = -1.0F + 2.0F * intersection.getTextureCoordinates().y;
		
		return new ScatteringFunctions(new BSDF(intersection, new HairBXDF(sigmaA, alpha, betaM, betaN, eta, h), false, eta));
	}
	
	/**
	 * Returns a {@code String} with the name of this {@code HairMaterial} instance.
	 * 
	 * @return a {@code String} with the name of this {@code HairMaterial} instance
	 */
	@Override
	public String getName() {
		return NAME;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code HairMaterial} instance.
	 * 
	 * @return a {@code String} representation of this {@code HairMaterial} instance
	 */
	@Override
	public String toString() {
		return String.format("new HairMaterial(%s, %s, %s, %s, %s, %s, %s, %s, %s, %s)", this.textureColor, this.textureEmission, this.textureSigmaA, this.textureAlpha, this.textureBetaM, this.textureBetaN, this.textureEta, this.textureEumelanin, this.texturePheomelanin, this.modifier);
	}
	
	/**
	 * Returns the {@link Texture} instance for the Alpha value.
	 * 
	 * @return the {@code Texture} instance for the Alpha value
	 */
	public Texture getTextureAlpha() {
		return this.textureAlpha;
	}
	
	/**
	 * Returns the {@link Texture} instance for the Beta M value.
	 * 
	 * @return the {@code Texture} instance for the Beta M value
	 */
	public Texture getTextureBetaM() {
		return this.textureBetaM;
	}
	
	/**
	 * Returns the {@link Texture} instance for the Beta N value.
	 * 
	 * @return the {@code Texture} instance for the Beta N value
	 */
	public Texture getTextureBetaN() {
		return this.textureBetaN;
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
	 * Returns the {@link Texture} instance for the Eumelanin value.
	 * 
	 * @return the {@code Texture} instance for the Eumelanin value
	 */
	public Texture getTextureEumelanin() {
		return this.textureEumelanin;
	}
	
	/**
	 * Returns the {@link Texture} instance for the Pheomelanin value.
	 * 
	 * @return the {@code Texture} instance for the Pheomelanin value
	 */
	public Texture getTexturePheomelanin() {
		return this.texturePheomelanin;
	}
	
	/**
	 * Returns the {@link Texture} instance for the Sigma A value.
	 * 
	 * @return the {@code Texture} instance for the Sigma A value
	 */
	public Texture getTextureSigmaA() {
		return this.textureSigmaA;
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
				
				if(!this.textureAlpha.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
				
				if(!this.textureBetaM.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
				
				if(!this.textureBetaN.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
				
				if(!this.textureColor.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
				
				if(!this.textureEmission.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
				
				if(!this.textureEta.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
				
				if(!this.textureEumelanin.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
				
				if(!this.texturePheomelanin.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
				
				if(!this.textureSigmaA.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
			}
			
			return nodeHierarchicalVisitor.visitLeave(this);
		} catch(final RuntimeException e) {
			throw new NodeTraversalException(e);
		}
	}
	
	/**
	 * Compares {@code object} to this {@code HairMaterial} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code HairMaterial}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code HairMaterial} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code HairMaterial}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof HairMaterial)) {
			return false;
		} else if(!Objects.equals(this.modifier, HairMaterial.class.cast(object).modifier)) {
			return false;
		} else if(!Objects.equals(this.textureAlpha, HairMaterial.class.cast(object).textureAlpha)) {
			return false;
		} else if(!Objects.equals(this.textureBetaM, HairMaterial.class.cast(object).textureBetaM)) {
			return false;
		} else if(!Objects.equals(this.textureBetaN, HairMaterial.class.cast(object).textureBetaN)) {
			return false;
		} else if(!Objects.equals(this.textureColor, HairMaterial.class.cast(object).textureColor)) {
			return false;
		} else if(!Objects.equals(this.textureEmission, HairMaterial.class.cast(object).textureEmission)) {
			return false;
		} else if(!Objects.equals(this.textureEta, HairMaterial.class.cast(object).textureEta)) {
			return false;
		} else if(!Objects.equals(this.textureEumelanin, HairMaterial.class.cast(object).textureEumelanin)) {
			return false;
		} else if(!Objects.equals(this.texturePheomelanin, HairMaterial.class.cast(object).texturePheomelanin)) {
			return false;
		} else if(!Objects.equals(this.textureSigmaA, HairMaterial.class.cast(object).textureSigmaA)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns an {@code int} with the ID of this {@code HairMaterial} instance.
	 * 
	 * @return an {@code int} with the ID of this {@code HairMaterial} instance
	 */
	@Override
	public int getID() {
		return ID;
	}
	
	/**
	 * Returns a hash code for this {@code HairMaterial} instance.
	 * 
	 * @return a hash code for this {@code HairMaterial} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.modifier, this.textureAlpha, this.textureBetaM, this.textureBetaN, this.textureColor, this.textureEmission, this.textureEta, this.textureEumelanin, this.texturePheomelanin, this.textureSigmaA);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private Color3F doComputeSigmaA(final Intersection intersection, final float betaN) {
		final Color3F colorSigmaA = Color3F.saturate(this.textureSigmaA.getColor(intersection), 0.0F, MAX_VALUE);
		
		if(!colorSigmaA.isBlack()) {
			return colorSigmaA;
		}
		
		final Color3F color = Color3F.saturate(this.textureColor.getColor(intersection), 0.0F, MAX_VALUE);
		
		if(!color.isBlack()) {
			return HairBXDF.computeSigmaAFromReflectance(color, betaN);
		}
		
		final float eumelanin = max(0.0F, this.textureEumelanin.getFloat(intersection));
		final float pheomelanin = max(0.0F, this.texturePheomelanin.getFloat(intersection));
		
		return HairBXDF.computeSigmaAFromConcentration(eumelanin, pheomelanin);
	}
}