/**
 * Copyright 2014 - 2024 J&#246;rgen Lundgren
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
package org.dayflower.pbrt;

import static org.dayflower.utility.Floats.pow;

import org.dayflower.color.Color3D;
import org.dayflower.color.Color3F;
import org.dayflower.color.ColorSpaceD;
import org.dayflower.color.ColorSpaceF;

import org.macroing.java.lang.Doubles;

public final class ColorConversions {
	private ColorConversions() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static void main(final String[] args) {
		doTestColor3D();
		doTestColor3F();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static Color3D doConvertRGBToXYZ(final Color3D color) {
		final double x = 0.412453D * color.r + 0.357580D * color.g + 0.180423D * color.b;
		final double y = 0.212671D * color.r + 0.715160D * color.g + 0.072169D * color.b;
		final double z = 0.019334D * color.r + 0.119193D * color.g + 0.950227D * color.b;
		
		return new Color3D(x, y, z);
	}
	
	private static Color3D doConvertXYZToRGB(final Color3D color) {
		final double r = +3.240479D * color.r - 1.537150D * color.g - 0.498535D * color.b;
		final double g = -0.969256D * color.r + 1.875991D * color.g + 0.041556D * color.b;
		final double b = +0.055648D * color.r - 0.204043D * color.g + 1.057311D * color.b;
		
		return new Color3D(r, g, b);
	}
	
	private static Color3D doRedoGammaCorrection(final Color3D color) {
		final double breakPoint = 0.0031308D;
		final double gamma = 2.4D;
		final double segmentOffset = 0.055D;
		final double slope = 12.92D;
		final double slopeMatch = 1.055D;
		
		final double component1 = color.r <= breakPoint ? color.r * slope : slopeMatch * Doubles.pow(color.r, 1.0D / gamma) - segmentOffset;
		final double component2 = color.g <= breakPoint ? color.g * slope : slopeMatch * Doubles.pow(color.g, 1.0D / gamma) - segmentOffset;
		final double component3 = color.b <= breakPoint ? color.b * slope : slopeMatch * Doubles.pow(color.b, 1.0D / gamma) - segmentOffset;
		
		return new Color3D(component1, component2, component3);
	}
	
	private static Color3D doUndoGammaCorrection(final Color3D color) {
		final double breakPoint = 0.0031308D;
		final double gamma = 2.4D;
		final double segmentOffset = 0.055D;
		final double slope = 12.92D;
		final double slopeMatch = 1.055D;
		
		final double component1 = color.r <= breakPoint * slope ? color.r / slope : Doubles.pow((color.r + segmentOffset) / slopeMatch, gamma);
		final double component2 = color.g <= breakPoint * slope ? color.g / slope : Doubles.pow((color.g + segmentOffset) / slopeMatch, gamma);
		final double component3 = color.b <= breakPoint * slope ? color.b / slope : Doubles.pow((color.b + segmentOffset) / slopeMatch, gamma);
		
		return new Color3D(component1, component2, component3);
	}
	
	private static Color3F doConvertRGBToXYZ(final Color3F color) {
		final float x = 0.412453F * color.r + 0.357580F * color.g + 0.180423F * color.b;
		final float y = 0.212671F * color.r + 0.715160F * color.g + 0.072169F * color.b;
		final float z = 0.019334F * color.r + 0.119193F * color.g + 0.950227F * color.b;
		
		return new Color3F(x, y, z);
	}
	
	private static Color3F doConvertXYZToRGB(final Color3F color) {
		final float r = +3.240479F * color.r - 1.537150F * color.g - 0.498535F * color.b;
		final float g = -0.969256F * color.r + 1.875991F * color.g + 0.041556F * color.b;
		final float b = +0.055648F * color.r - 0.204043F * color.g + 1.057311F * color.b;
		
		return new Color3F(r, g, b);
	}
	
	private static Color3F doRedoGammaCorrection(final Color3F color) {
		final float breakPoint = 0.0031308F;
		final float gamma = 2.4F;
		final float segmentOffset = 0.055F;
		final float slope = 12.92F;
		final float slopeMatch = 1.055F;
		
		final float component1 = color.r <= breakPoint ? color.r * slope : slopeMatch * pow(color.r, 1.0F / gamma) - segmentOffset;
		final float component2 = color.g <= breakPoint ? color.g * slope : slopeMatch * pow(color.g, 1.0F / gamma) - segmentOffset;
		final float component3 = color.b <= breakPoint ? color.b * slope : slopeMatch * pow(color.b, 1.0F / gamma) - segmentOffset;
		
		return new Color3F(component1, component2, component3);
	}
	
	private static Color3F doUndoGammaCorrection(final Color3F color) {
		final float breakPoint = 0.0031308F;
		final float gamma = 2.4F;
		final float segmentOffset = 0.055F;
		final float slope = 12.92F;
		final float slopeMatch = 1.055F;
		
		final float component1 = color.r <= breakPoint * slope ? color.r / slope : pow((color.r + segmentOffset) / slopeMatch, gamma);
		final float component2 = color.g <= breakPoint * slope ? color.g / slope : pow((color.g + segmentOffset) / slopeMatch, gamma);
		final float component3 = color.b <= breakPoint * slope ? color.b / slope : pow((color.b + segmentOffset) / slopeMatch, gamma);
		
		return new Color3F(component1, component2, component3);
	}
	
	private static void doTestColor3D() {
		final Color3D oA = new Color3D(0.33D, 1.66D, 2.99D);
		
		final Color3D pA = doConvertRGBToXYZ(oA);
		final Color3D pB = doConvertXYZToRGB(pA);
		final Color3D pC = doRedoGammaCorrection(oA);
		final Color3D pD = doUndoGammaCorrection(pC);
		
		final Color3D sA = ColorSpaceD.S_R_G_B.convertRGBToXYZ(oA);
		final Color3D sB = ColorSpaceD.S_R_G_B.convertXYZToRGB(sA);
		final Color3D sC = ColorSpaceD.S_R_G_B.redoGammaCorrection(oA);
		final Color3D sD = ColorSpaceD.S_R_G_B.undoGammaCorrection(sC);
		
		System.out.println("Color3D:");
		System.out.println(" Convert:");
		System.out.println("  P: " + oA + " -> " + pA + " -> " + pB);
		System.out.println("  S: " + oA + " -> " + sA + " -> " + sB);
		System.out.println(" Gamma Correction:");
		System.out.println("  P: " + oA + " -> " + pC + " -> " + pD);
		System.out.println("  S: " + oA + " -> " + sC + " -> " + sD);
	}
	
	private static void doTestColor3F() {
		final Color3F oA = new Color3F(0.33F, 1.66F, 2.99F);
		
		final Color3F pA = doConvertRGBToXYZ(oA);
		final Color3F pB = doConvertXYZToRGB(pA);
		final Color3F pC = doRedoGammaCorrection(oA);
		final Color3F pD = doUndoGammaCorrection(pC);
		
		final Color3F sA = ColorSpaceF.S_R_G_B.convertRGBToXYZ(oA);
		final Color3F sB = ColorSpaceF.S_R_G_B.convertXYZToRGB(sA);
		final Color3F sC = ColorSpaceF.S_R_G_B.redoGammaCorrection(oA);
		final Color3F sD = ColorSpaceF.S_R_G_B.undoGammaCorrection(sC);
		
		System.out.println();
		System.out.println("Color3F:");
		System.out.println(" Convert:");
		System.out.println("  P: " + oA + " -> " + pA + " -> " + pB);
		System.out.println("  S: " + oA + " -> " + sA + " -> " + sB);
		System.out.println(" Gamma Correction:");
		System.out.println("  P: " + oA + " -> " + pC + " -> " + pD);
		System.out.println("  S: " + oA + " -> " + sC + " -> " + sD);
	}
}