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
package org.macroing.dayflower;

import static org.macroing.dayflower.Floats.equal;
import static org.macroing.dayflower.Floats.exp;
import static org.macroing.dayflower.Floats.isNaN;
import static org.macroing.dayflower.Floats.lerp;
import static org.macroing.dayflower.Floats.max;
import static org.macroing.dayflower.Floats.min;
import static org.macroing.dayflower.Floats.pow;
import static org.macroing.dayflower.Ints.toInt;

import java.util.Objects;

public final class Color3F {
	private final float component1;
	private final float component2;
	private final float component3;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public Color3F() {
		this(0.0F);
	}
	
	public Color3F(final float component) {
		this(component, component, component);
	}
	
	public Color3F(final float component1, final float component2, final float component3) {
		this.component1 = component1;
		this.component2 = component2;
		this.component3 = component3;
	}
	
	public Color3F(final int component) {
		this(component, component, component);
	}
	
	public Color3F(final int component1, final int component2, final int component3) {
		this.component1 = Ints.saturate(component1) / 255.0F;
		this.component2 = Ints.saturate(component2) / 255.0F;
		this.component3 = Ints.saturate(component3) / 255.0F;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public String toString() {
		return String.format("new Color3F(%+.10f, %+.10f, %+.10f)", Float.valueOf(this.component1), Float.valueOf(this.component2), Float.valueOf(this.component3));
	}
	
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof Color3F)) {
			return false;
		} else if(!equal(this.component1, Color3F.class.cast(object).component1)) {
			return false;
		} else if(!equal(this.component2, Color3F.class.cast(object).component2)) {
			return false;
		} else if(!equal(this.component3, Color3F.class.cast(object).component3)) {
			return false;
		} else {
			return true;
		}
	}
	
	public boolean hasNaNs() {
		return isNaN(this.component1) || isNaN(this.component2) || isNaN(this.component3);
	}
	
	public boolean isBlack() {
		return isGrayscale() && equal(this.component1, 0.0F);
	}
	
	public boolean isGrayscale() {
		return equal(this.component1, this.component2, this.component3);
	}
	
	public boolean isWhite() {
		return isGrayscale() && this.component1 >= 1.0F;
	}
	
	public float average() {
		return (this.component1 + this.component2 + this.component3) / 3.0F;
	}
	
	public float getB() {
		return this.component3;
	}
	
	public float getComponent1() {
		return this.component1;
	}
	
	public float getComponent2() {
		return this.component2;
	}
	
	public float getComponent3() {
		return this.component3;
	}
	
	public float getG() {
		return this.component2;
	}
	
	public float getR() {
		return this.component1;
	}
	
	public float getX() {
		return this.component1;
	}
	
	public float getY() {
		return this.component2;
	}
	
	public float getZ() {
		return this.component3;
	}
	
	public float lightness() {
		return (maximum() + minimum()) / 2.0F;
	}
	
	public float luminance() {
		return this.component1 * 0.212671F + this.component2 * 0.715160F + this.component3 * 0.072169F;
	}
	
	public float maximum() {
		return max(this.component1, this.component2, this.component3);
	}
	
	public float minimum() {
		return min(this.component1, this.component2, this.component3);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(Float.valueOf(this.component1), Float.valueOf(this.component2), Float.valueOf(this.component3));
	}
	
	public int packToARGB() {
		final int a = (toInt(Floats.saturate(1.0F)   * 255.0F + 0.5F) & 0xFF) << 24;
		final int r = (toInt(Floats.saturate(getR()) * 255.0F + 0.5F) & 0xFF) << 16;
		final int g = (toInt(Floats.saturate(getG()) * 255.0F + 0.5F) & 0xFF) <<  8;
		final int b = (toInt(Floats.saturate(getB()) * 255.0F + 0.5F) & 0xFF) <<  0;
		
		return a | r | g | b;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static Color3F add(final Color3F cLHS, final Color3F cRHS) {
		final float component1 = cLHS.component1 + cRHS.component1;
		final float component2 = cLHS.component2 + cRHS.component2;
		final float component3 = cLHS.component3 + cRHS.component3;
		
		return new Color3F(component1, component2, component3);
	}
	
	public static Color3F addSample(final Color3F cLHS, final Color3F cRHS, final int samples) {
		final float component1 = cLHS.component1 + ((cRHS.component1 - cLHS.component1) / samples);
		final float component2 = cLHS.component2 + ((cRHS.component2 - cLHS.component2) / samples);
		final float component3 = cLHS.component3 + ((cRHS.component3 - cLHS.component3) / samples);
		
		return new Color3F(component1, component2, component3);
	}
	
	public static Color3F blend(final Color3F cLHS, final Color3F cRHS) {
		return blend(cLHS, cRHS, 0.5F);
	}
	
	public static Color3F blend(final Color3F cLHS, final Color3F cRHS, final float t) {
		return blend(cLHS, cRHS, t, t, t);
	}
	
	public static Color3F blend(final Color3F cLHS, final Color3F cRHS, final float tComponent1, final float tComponent2, final float tComponent3) {
		final float component1 = lerp(cLHS.component1, cRHS.component1, tComponent1);
		final float component2 = lerp(cLHS.component2, cRHS.component2, tComponent2);
		final float component3 = lerp(cLHS.component3, cRHS.component3, tComponent3);
		
		return new Color3F(component1, component2, component3);
	}
	
	public static Color3F convertRGBToXYZUsingPBRT(final Color3F c) {
		final float x = 0.412453F * c.getR() + 0.357580F * c.getG() + 0.180423F * c.getB();
		final float y = 0.212671F * c.getR() + 0.715160F * c.getG() + 0.072169F * c.getB();
		final float z = 0.019334F * c.getR() + 0.119193F * c.getG() + 0.950227F * c.getB();
		
		return new Color3F(x, y, z);
	}
	
	public static Color3F convertRGBToXYZUsingSRGB(final Color3F c) {
		return ColorSpace3F.SRGB.convertRGBToXYZ(c);
	}
	
	public static Color3F convertXYZToRGBUsingPBRT(final Color3F c) {
		final float r = +3.240479F * c.getX() - 1.537150F * c.getY() - 0.498535F * c.getZ();
		final float g = -0.969256F * c.getX() + 1.875991F * c.getY() + 0.041556F * c.getZ();
		final float b = +0.055648F * c.getX() - 0.204043F * c.getY() + 1.057311F * c.getZ();
		
		return new Color3F(r, g, b);
	}
	
	public static Color3F convertXYZToRGBUsingSRGB(final Color3F c) {
		return ColorSpace3F.SRGB.convertXYZToRGB(c);
	}
	
	public static Color3F divide(final Color3F cLHS, final Color3F cRHS) {
		final float component1 = cLHS.component1 / cRHS.component1;
		final float component2 = cLHS.component2 / cRHS.component2;
		final float component3 = cLHS.component3 / cRHS.component3;
		
		return new Color3F(component1, component2, component3);
	}
	
	public static Color3F divide(final Color3F cLHS, final float sRHS) {
		final float component1 = cLHS.component1 / sRHS;
		final float component2 = cLHS.component2 / sRHS;
		final float component3 = cLHS.component3 / sRHS;
		
		return new Color3F(component1, component2, component3);
	}
	
	public static Color3F grayscaleAverage(final Color3F c) {
		return new Color3F(c.average());
	}
	
	public static Color3F grayscaleComponent1(final Color3F c) {
		return new Color3F(c.component1);
	}
	
	public static Color3F grayscaleComponent2(final Color3F c) {
		return new Color3F(c.component2);
	}
	
	public static Color3F grayscaleComponent3(final Color3F c) {
		return new Color3F(c.component3);
	}
	
	public static Color3F grayscaleLightness(final Color3F c) {
		return new Color3F(c.lightness());
	}
	
	public static Color3F grayscaleLuminance(final Color3F c) {
		return new Color3F(c.luminance());
	}
	
	public static Color3F grayscaleMaximum(final Color3F c) {
		return new Color3F(c.maximum());
	}
	
	public static Color3F grayscaleMinimum(final Color3F c) {
		return new Color3F(c.minimum());
	}
	
	public static Color3F invert(final Color3F c) {
		final float component1 = 1.0F - c.component1;
		final float component2 = 1.0F - c.component2;
		final float component3 = 1.0F - c.component3;
		
		return new Color3F(component1, component2, component3);
	}
	
	public static Color3F maximum(final Color3F cA, final Color3F cB) {
		final float component1 = max(cA.component1, cB.component1);
		final float component2 = max(cA.component2, cB.component2);
		final float component3 = max(cA.component3, cB.component3);
		
		return new Color3F(component1, component2, component3);
	}
	
	public static Color3F maximumTo1(final Color3F c) {
		final float maximum = c.maximum();
		
		if(maximum > 1.0F) {
			final float component1 = c.component1 / maximum;
			final float component2 = c.component2 / maximum;
			final float component3 = c.component3 / maximum;
			
			return new Color3F(component1, component2, component3);
		}
		
		return c;
	}
	
	public static Color3F minimum(final Color3F cA, final Color3F cB) {
		final float component1 = min(cA.component1, cB.component1);
		final float component2 = min(cA.component2, cB.component2);
		final float component3 = min(cA.component3, cB.component3);
		
		return new Color3F(component1, component2, component3);
	}
	
	public static Color3F minimumTo0(final Color3F c) {
		final float minimum = c.minimum();
		
		if(minimum < 0.0F) {
			final float component1 = c.component1 + -minimum;
			final float component2 = c.component2 + -minimum;
			final float component3 = c.component3 + -minimum;
			
			return new Color3F(component1, component2, component3);
		}
		
		return c;
	}
	
	public static Color3F multiply(final Color3F cLHS, final Color3F cRHS) {
		final float component1 = cLHS.component1 * cRHS.component1;
		final float component2 = cLHS.component2 * cRHS.component2;
		final float component3 = cLHS.component3 * cRHS.component3;
		
		return new Color3F(component1, component2, component3);
	}
	
	public static Color3F multiply(final Color3F cLHS, final float sRHS) {
		final float component1 = cLHS.component1 * sRHS;
		final float component2 = cLHS.component2 * sRHS;
		final float component3 = cLHS.component3 * sRHS;
		
		return new Color3F(component1, component2, component3);
	}
	
	public static Color3F negate(final Color3F c) {
		final float component1 = -c.component1;
		final float component2 = -c.component2;
		final float component3 = -c.component3;
		
		return new Color3F(component1, component2, component3);
	}
	
	public static Color3F normalize(final Color3F c) {
		final float sum = c.component1 + c.component2 + c.component3;
		
		if(sum < 1.0e-6F) {
			return c;
		}
		
		final float sumReciprocal = 1.0F / sum;
		
		final float component1 = c.component1 * sumReciprocal;
		final float component2 = c.component2 * sumReciprocal;
		final float component3 = c.component3 * sumReciprocal;
		
		return new Color3F(component1, component2, component3);
	}
	
	public static Color3F random() {
		final float component1 = Floats.random();
		final float component2 = Floats.random();
		final float component3 = Floats.random();
		
		return new Color3F(component1, component2, component3);
	}
	
	public static Color3F randomComponent1() {
		final float component1 = Floats.random();
		final float component2 = 0.0F;
		final float component3 = 0.0F;
		
		return new Color3F(component1, component2, component3);
	}
	
	public static Color3F randomComponent2() {
		final float component1 = 0.0F;
		final float component2 = Floats.random();
		final float component3 = 0.0F;
		
		return new Color3F(component1, component2, component3);
	}
	
	public static Color3F randomComponent3() {
		final float component1 = 0.0F;
		final float component2 = 0.0F;
		final float component3 = Floats.random();
		
		return new Color3F(component1, component2, component3);
	}
	
	public static Color3F redoGammaCorrectionPBRT(final Color3F c) {
		final float component1 = c.component1 <= 0.0031308F ? 12.92F * c.component1 : 1.055F * pow(c.component1, 1.0F / 2.4F) - 0.055F;
		final float component2 = c.component2 <= 0.0031308F ? 12.92F * c.component2 : 1.055F * pow(c.component2, 1.0F / 2.4F) - 0.055F;
		final float component3 = c.component3 <= 0.0031308F ? 12.92F * c.component3 : 1.055F * pow(c.component3, 1.0F / 2.4F) - 0.055F;
		
		return new Color3F(component1, component2, component3);
	}
	
	public static Color3F redoGammaCorrectionSRGB(final Color3F c) {
		return ColorSpace3F.SRGB.redoGammaCorrection(c);
	}
	
	public static Color3F saturate(final Color3F c) {
		return saturate(c, 0.0F, 1.0F);
	}
	
	public static Color3F saturate(final Color3F c, final float edgeA, final float edgeB) {
		final float component1 = Floats.saturate(c.component1, edgeA, edgeB);
		final float component2 = Floats.saturate(c.component2, edgeA, edgeB);
		final float component3 = Floats.saturate(c.component3, edgeA, edgeB);
		
		return new Color3F(component1, component2, component3);
	}
	
	public static Color3F sepia(final Color3F c) {
		final float component1 = c.component1 * 0.393F + c.component2 * 0.769F + c.component3 * 0.189F;
		final float component2 = c.component1 * 0.349F + c.component2 * 0.686F + c.component3 * 0.168F;
		final float component3 = c.component1 * 0.272F + c.component2 * 0.534F + c.component3 * 0.131F;
		
		return new Color3F(component1, component2, component3);
	}
	
	public static Color3F subtract(final Color3F cLHS, final Color3F cRHS) {
		final float component1 = cLHS.component1 - cRHS.component1;
		final float component2 = cLHS.component2 - cRHS.component2;
		final float component3 = cLHS.component3 - cRHS.component3;
		
		return new Color3F(component1, component2, component3);
	}
	
	public static Color3F toneMapFilmicCurve(final Color3F color, final float exposure, final float a, final float b, final float c, final float d, final float e) {
		return toneMapFilmicCurve(color, exposure, a, b, c, d, e, 0.0F, Float.MIN_VALUE);
	}
	
	public static Color3F toneMapFilmicCurve(final Color3F color, final float exposure, final float a, final float b, final float c, final float d, final float e, final float subtract, final float minimum) {
		final float component11 = max(color.component1 * exposure - subtract, minimum);
		final float component21 = max(color.component2 * exposure - subtract, minimum);
		final float component31 = max(color.component3 * exposure - subtract, minimum);
		
		final float component12 = Floats.saturate((component11 * (a * component11 + b)) / (component11 * (c * component11 + d) + e));
		final float component22 = Floats.saturate((component21 * (a * component21 + b)) / (component21 * (c * component21 + d) + e));
		final float component32 = Floats.saturate((component31 * (a * component31 + b)) / (component31 * (c * component31 + d) + e));
		
		return new Color3F(component12, component22, component32);
	}
	
	public static Color3F toneMapFilmicCurveACES2(final Color3F color, final float exposure) {
//		Source: https://knarkowicz.wordpress.com/2016/01/06/aces-filmic-tone-mapping-curve/
		return toneMapFilmicCurve(color, exposure, 2.51F, 0.03F, 2.43F, 0.59F, 0.14F);
	}
	
	public static Color3F toneMapFilmicCurveGammaCorrection22(final Color3F color, final float exposure) {
//		Source: http://filmicworlds.com/blog/why-a-filmic-curve-saturates-your-blacks/
		return toneMapFilmicCurve(color, exposure, 6.2F, 0.5F, 6.2F, 1.7F, 0.06F, 0.004F, 0.0F);
	}
	
	public static Color3F toneMapReinhard(final Color3F c, final float exposure) {
//		Source: https://www.shadertoy.com/view/WdjSW3
		
		final float component11 = c.component1 * exposure;
		final float component21 = c.component2 * exposure;
		final float component31 = c.component3 * exposure;
		
		final float component12 = component11 / (1.0F + component11);
		final float component22 = component21 / (1.0F + component21);
		final float component32 = component31 / (1.0F + component31);
		
		return new Color3F(component12, component22, component32);
	}
	
	public static Color3F toneMapReinhardModifiedVersion1(final Color3F c, final float exposure) {
//		Source: https://www.shadertoy.com/view/WdjSW3
		
		final float lWhite = 4.0F;
		
		final float component11 = c.component1 * exposure;
		final float component21 = c.component2 * exposure;
		final float component31 = c.component3 * exposure;
		
		final float component12 = component11 * (1.0F + component11 / (lWhite * lWhite)) / (1.0F + component11);
		final float component22 = component21 * (1.0F + component21 / (lWhite * lWhite)) / (1.0F + component21);
		final float component32 = component31 * (1.0F + component31 / (lWhite * lWhite)) / (1.0F + component31);
		
		return new Color3F(component12, component22, component32);
	}
	
	public static Color3F toneMapReinhardModifiedVersion2(final Color3F c, final float exposure) {
		final float component11 = c.component1 * exposure;
		final float component21 = c.component2 * exposure;
		final float component31 = c.component3 * exposure;
		
		final float component12 = 1.0F - exp(-component11 * exposure);
		final float component22 = 1.0F - exp(-component21 * exposure);
		final float component32 = 1.0F - exp(-component31 * exposure);
		
		return new Color3F(component12, component22, component32);
	}
	
	public static Color3F toneMapUnreal3(final Color3F c, final float exposure) {
//		Source: https://www.shadertoy.com/view/WdjSW3
		
		final float component11 = c.component1 * exposure;
		final float component21 = c.component2 * exposure;
		final float component31 = c.component3 * exposure;
		
		final float component12 = component11 / (component11 + 0.155F) * 1.019F;
		final float component22 = component21 / (component21 + 0.155F) * 1.019F;
		final float component32 = component31 / (component31 + 0.155F) * 1.019F;
		
		return new Color3F(component12, component22, component32);
	}
	
	public static Color3F undoGammaCorrectionPBRT(final Color3F c) {
		final float component1 = c.component1 <= 0.04045F ? c.component1 * 1.0F / 12.92F : pow((c.component1 + 0.055F) * 1.0F / 1.055F, 2.4F);
		final float component2 = c.component2 <= 0.04045F ? c.component2 * 1.0F / 12.92F : pow((c.component2 + 0.055F) * 1.0F / 1.055F, 2.4F);
		final float component3 = c.component3 <= 0.04045F ? c.component3 * 1.0F / 12.92F : pow((c.component3 + 0.055F) * 1.0F / 1.055F, 2.4F);
		
		return new Color3F(component1, component2, component3);
	}
	
	public static Color3F undoGammaCorrectionSRGB(final Color3F c) {
		return ColorSpace3F.SRGB.undoGammaCorrection(c);
	}
	
	public static Color3F unpackFromARGB(final int colorARGB) {
		final int r = (colorARGB >> 16) & 0xFF;
		final int g = (colorARGB >>  8) & 0xFF;
		final int b = (colorARGB >>  0) & 0xFF;
		
		return new Color3F(r, g, b);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static final class ColorSpace3F {
		public static final ColorSpace3F SRGB = new ColorSpace3F(0.00304F, 2.4F, 0.6400F, 0.3300F, 0.3000F, 0.6000F, 0.1500F, 0.0600F, 0.31271F, 0.32902F);
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		private final float breakPoint;
		private final float gamma;
		private final float segmentOffset;
		private final float slope;
		private final float slopeMatch;
		private final float[] matrixRGBToXYZ;
		private final float[] matrixXYZToRGB;
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		public ColorSpace3F(final float breakPoint, final float gamma, final float xR, final float yR, final float xG, final float yG, final float xB, final float yB, final float xW, final float yW) {
			this.breakPoint = breakPoint;
			this.gamma = gamma;
			this.slope = breakPoint > 0.0F ? 1.0F / (gamma / pow(breakPoint, 1.0F / gamma - 1.0F) - gamma * breakPoint + breakPoint) : 1.0F;
			this.slopeMatch = breakPoint > 0.0F ? gamma * this.slope / pow(breakPoint, 1.0F / gamma - 1.0F) : 1.0F;
			this.segmentOffset = breakPoint > 0.0F ? this.slopeMatch * pow(breakPoint, 1.0F / gamma) - this.slope * breakPoint : 0.0F;
			this.matrixXYZToRGB = doCreateColorSpace3MatrixXYZToRGB(xR, yR, xG, yG, xB, yB, xW, yW);
			this.matrixRGBToXYZ = doCreateColorSpace3MatrixRGBToXYZ(xW, yW, this.matrixXYZToRGB);
		}
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		public Color3F convertRGBToXYZ(final Color3F c) {
			final float r = c.getR();
			final float g = c.getG();
			final float b = c.getB();
			
			final float x = this.matrixRGBToXYZ[0] * r + this.matrixRGBToXYZ[3] * g + this.matrixRGBToXYZ[6] * b;
			final float y = this.matrixRGBToXYZ[1] * r + this.matrixRGBToXYZ[4] * g + this.matrixRGBToXYZ[7] * b;
			final float z = this.matrixRGBToXYZ[2] * r + this.matrixRGBToXYZ[5] * g + this.matrixRGBToXYZ[8] * b;
			
			return new Color3F(x, y, z);
		}
		
		public Color3F convertXYZToRGB(final Color3F c) {
			final float x = c.getX();
			final float y = c.getY();
			final float z = c.getZ();
			
			final float r = this.matrixXYZToRGB[0] * x + this.matrixXYZToRGB[1] * y + this.matrixXYZToRGB[2] * z;
			final float g = this.matrixXYZToRGB[3] * x + this.matrixXYZToRGB[4] * y + this.matrixXYZToRGB[5] * z;
			final float b = this.matrixXYZToRGB[6] * x + this.matrixXYZToRGB[7] * y + this.matrixXYZToRGB[8] * z;
			
			return new Color3F(r, g, b);
		}
		
		public Color3F redoGammaCorrection(final Color3F c) {
			final float component1 = doRedoGammaCorrection(c.getComponent1());
			final float component2 = doRedoGammaCorrection(c.getComponent2());
			final float component3 = doRedoGammaCorrection(c.getComponent3());
			
			return new Color3F(component1, component2, component3);
		}
		
		public Color3F undoGammaCorrection(final Color3F c) {
			final float component1 = doUndoGammaCorrection(c.getComponent1());
			final float component2 = doUndoGammaCorrection(c.getComponent2());
			final float component3 = doUndoGammaCorrection(c.getComponent3());
			
			return new Color3F(component1, component2, component3);
		}
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		private float doRedoGammaCorrection(final float value) {
			if(value <= 0.0F) {
				return 0.0F;
			} else if(value >= 1.0F) {
				return 1.0F;
			} else if(value <= this.breakPoint) {
				return value * this.slope;
			} else {
				return this.slopeMatch * pow(value, 1.0F / this.gamma) - this.segmentOffset;
			}
		}
		
		private float doUndoGammaCorrection(final float value) {
			if(value <= 0.0F) {
				return 0.0F;
			} else if(value >= 1.0F) {
				return 1.0F;
			} else if(value <= this.breakPoint * this.slope) {
				return value / this.slope;
			} else {
				return pow((value + this.segmentOffset) / this.slopeMatch, this.gamma);
			}
		}
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		private static float[] doCreateColorSpace3MatrixRGBToXYZ(final float xW, final float yW, final float[] m) {
			final float a = m[0] * (m[4] * m[8] - m[7] * m[5]);
			final float b = m[1] * (m[3] * m[8] - m[6] * m[5]);
			final float c = m[2] * (m[3] * m[7] - m[6] * m[4]);
			final float s = 1.0F / (a - b + c);
			
			final float eXR = s * (m[4] * m[8] - m[5] * m[7]);
			final float eYR = s * (m[5] * m[6] - m[3] * m[8]);
			final float eZR = s * (m[3] * m[7] - m[4] * m[6]);
			final float eXG = s * (m[2] * m[7] - m[1] * m[8]);
			final float eYG = s * (m[0] * m[8] - m[2] * m[6]);
			final float eZG = s * (m[1] * m[6] - m[0] * m[7]);
			final float eXB = s * (m[1] * m[5] - m[2] * m[4]);
			final float eYB = s * (m[2] * m[3] - m[0] * m[5]);
			final float eZB = s * (m[0] * m[4] - m[1] * m[3]);
			final float eXW = xW;
			final float eYW = yW;
			final float eZW = 1.0F - (xW + yW);
			
			return new float[] {
				eXR, eYR, eZR,
				eXG, eYG, eZG,
				eXB, eYB, eZB,
				eXW, eYW, eZW
			};
		}
		
		private static float[] doCreateColorSpace3MatrixXYZToRGB(final float xR, final float yR, final float xG, final float yG, final float xB, final float yB, final float xW, final float yW) {
			final float zR = 1.0F - (xR + yR);
			final float zG = 1.0F - (xG + yG);
			final float zB = 1.0F - (xB + yB);
			final float zW = 1.0F - (xW + yW);
			final float rX = (yG * zB) - (yB * zG);
			final float rY = (xB * zG) - (xG * zB);
			final float rZ = (xG * yB) - (xB * yG);
			final float rW = ((rX * xW) + (rY * yW) + (rZ * zW)) / yW;
			final float gX = (yB * zR) - (yR * zB);
			final float gY = (xR * zB) - (xB * zR);
			final float gZ = (xB * yR) - (xR * yB);
			final float gW = ((gX * xW) + (gY * yW) + (gZ * zW)) / yW;
			final float bX = (yR * zG) - (yG * zR);
			final float bY = (xG * zR) - (xR * zG);
			final float bZ = (xR * yG) - (xG * yR);
			final float bW = ((bX * xW) + (bY * yW) + (bZ * zW)) / yW;
			
			final float eRX = rX / rW;
			final float eRY = rY / rW;
			final float eRZ = rZ / rW;
			final float eGX = gX / gW;
			final float eGY = gY / gW;
			final float eGZ = gZ / gW;
			final float eBX = bX / bW;
			final float eBY = bY / bW;
			final float eBZ = bZ / bW;
			final float eRW = rW;
			final float eGW = gW;
			final float eBW = bW;
			
			return new float[] {
				eRX, eRY, eRZ,
				eGX, eGY, eGZ,
				eBX, eBY, eBZ,
				eRW, eGW, eBW
			};
		}
	}
}