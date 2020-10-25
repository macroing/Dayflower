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

import static org.dayflower.util.Floats.log;
import static org.dayflower.util.Floats.max;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

import org.dayflower.image.Color3F;
import org.dayflower.scene.Intersection;
import org.dayflower.scene.Texture;
import org.dayflower.scene.texture.ConstantTexture;

//TODO: Add Javadocs!
public final class HairMaterial implements PBRTMaterial {
	private final Texture textureAlpha;
	private final Texture textureBetaM;
	private final Texture textureBetaN;
	private final Texture textureColor;
	private final Texture textureEta;
	private final Texture textureEumelanin;
	private final Texture texturePheomelanin;
	private final Texture textureSigmaA;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public HairMaterial() {
		this(ConstantTexture.GRAY_2_00, ConstantTexture.GRAY_0_30, ConstantTexture.GRAY_0_30, ConstantTexture.BLACK, new ConstantTexture(new Color3F(1.55F)), ConstantTexture.BLACK, ConstantTexture.BLACK, new ConstantTexture(doComputeSigmaAFromConcentration(1.3F, 0.0F)));
	}
	
//	TODO: Add Javadocs!
	public HairMaterial(final Texture textureAlpha, final Texture textureBetaM, final Texture textureBetaN, final Texture textureColor, final Texture textureEta, final Texture textureEumelanin, final Texture texturePheomelanin, final Texture textureSigmaA) {
		this.textureAlpha = Objects.requireNonNull(textureAlpha, "textureAlpha == null");
		this.textureBetaM = Objects.requireNonNull(textureBetaM, "textureBetaM == null");
		this.textureBetaN = Objects.requireNonNull(textureBetaN, "textureBetaN == null");
		this.textureColor = Objects.requireNonNull(textureColor, "textureColor == null");
		this.textureEta = Objects.requireNonNull(textureEta, "textureEta == null");
		this.textureEumelanin = Objects.requireNonNull(textureEumelanin, "textureEumelanin == null");
		this.texturePheomelanin = Objects.requireNonNull(texturePheomelanin, "texturePheomelanin == null");
		this.textureSigmaA = Objects.requireNonNull(textureSigmaA, "textureSigmaA == null");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	@Override
	public Optional<BSDF> computeBSDF(final Intersection intersection, final TransportMode transportMode, final boolean isAllowingMultipleLobes) {
		Objects.requireNonNull(intersection, "intersection == null");
		Objects.requireNonNull(transportMode, "transportMode == null");
		
		final float alpha = this.textureAlpha.getColorXYZ(intersection).average();
		final float betaM = this.textureBetaM.getColorXYZ(intersection).average();
		final float betaN = this.textureBetaN.getColorXYZ(intersection).average();
		final float eta = this.textureEta.getColorXYZ(intersection).average();
		
		final Color3F sigmaA = doComputeSigmaA(intersection, betaN);
		
		final float h = -1.0F + 2.0F * intersection.getSurfaceIntersectionWorldSpace().getTextureCoordinates().getV();
		
		return Optional.of(new BSDF(intersection, Arrays.asList(new HairBXDF(sigmaA, alpha, betaM, betaN, eta, h)), eta));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private Color3F doComputeSigmaA(final Intersection intersection, final float betaN) {
		final Color3F colorSigmaA = Color3F.saturate(this.textureSigmaA.getColorXYZ(intersection), 0.0F, Float.MAX_VALUE);
		
		if(!colorSigmaA.isBlack()) {
			return colorSigmaA;
		}
		
		final Color3F colorColor = Color3F.saturate(this.textureColor.getColorXYZ(intersection), 0.0F, Float.MAX_VALUE);
		
		if(!colorColor.isBlack()) {
			return doComputeSigmaAFromReflectance(colorColor, betaN);
		}
		
		final Color3F colorEumelanin = this.textureEumelanin.getColorXYZ(intersection);
		final Color3F colorPheomelanin = this.texturePheomelanin.getColorXYZ(intersection);
		
		final float eumelanin = max(0.0F, colorEumelanin.average());
		final float pheomelanin = max(0.0F, colorPheomelanin.average());
		
		return doComputeSigmaAFromConcentration(eumelanin, pheomelanin);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static Color3F doComputeSigmaAFromConcentration(final float colorEumelanin, final float colorPheomelanin) {
		final float[] sigmaA = new float[3];
		final float[] sigmaAEumelanin = new float[] {0.419F, 0.697F, 1.37F};
		final float[] sigmaAPheomelanin = new float[] {0.187F, 0.4F, 1.05F};
		
		for(int i = 0; i < 3; i++) {
			sigmaA[i] = colorEumelanin * sigmaAEumelanin[i] + colorPheomelanin * sigmaAPheomelanin[i];
		}
		
		return Color3F.convertRGBToXYZUsingPBRT(new Color3F(sigmaA[0], sigmaA[1], sigmaA[2]));
	}
	
	private static Color3F doComputeSigmaAFromReflectance(final Color3F color, final float betaN) {
		final float constant = (5.969F - 0.215F * betaN + 2.532F * (betaN * betaN) - 10.73F * doPow(betaN, 3) + 5.574F * doPow(betaN, 4) + 0.245F * doPow(betaN, 5));
		
		final float component1 = log(color.getComponent1()) / constant;
		final float component2 = log(color.getComponent2()) / constant;
		final float component3 = log(color.getComponent3()) / constant;
		
		return new Color3F(component1 * component1, component2 * component2, component3 * component3);
	}
	
	private static float doPow(final float value, final int exponent) {
		if(exponent == 0) {
			return 1.0F;
		} else if(exponent == 1) {
			return value;
		} else {
			final float n2 = doPow(value, exponent / 2);
			
			return n2 * n2 * doPow(value, exponent & 1);
		}
	}
}