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
package org.dayflower.color;

import static org.dayflower.utility.Doubles.pow;

import java.lang.reflect.Field;//TODO: Add Unit Tests!

/**
 * A {@code ColorSpaceD} represents a color space that performs conversions for the color types {@link Color3D} and {@link Color4D}.
 * <p>
 * An instance of this class can convert {@code Color3D} and {@code Color4D} instances from a specific RGB color space to a specific XYZ color space, as well as from a specific XYZ color space to a specific RGB color space. In addition to that, it can also redo or undo gamma correction.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class ColorSpaceD {
	/**
	 * A {@code ColorSpaceD} instance that represents the Adobe RGB (1998) color space.
	 */
//	TODO: Add Unit Tests!
	public static final ColorSpaceD ADOBE_R_G_B_1998 = new ColorSpaceD(0.0D, 2.2D, 0.6400D, 0.3300D, 0.2100D, 0.7100D, 0.1500D, 0.0600D, 0.31271D, 0.32902D);
	
	/**
	 * A {@code ColorSpaceD} instance that represents the Adobe Wide Gamut RGB color space.
	 */
//	TODO: Add Unit Tests!
	public static final ColorSpaceD ADOBE_WIDE_GAMUT_R_G_B = new ColorSpaceD(0.0D, 563.0D / 256.0D, 0.7347D, 0.2653D, 0.1152D, 0.8264D, 0.1566D, 0.0177D, 0.3457D, 0.3585D);
	
	/**
	 * A {@code ColorSpaceD} instance that represents the Apple color space.
	 */
//	TODO: Add Unit Tests!
	public static final ColorSpaceD APPLE = new ColorSpaceD(0.0D, 1.8D, 0.6250D, 0.3400D, 0.2800D, 0.5950D, 0.1550D, 0.0700D, 0.31271D, 0.32902D);
	
	/**
	 * A {@code ColorSpaceD} instance that represents the CIE color space.
	 */
//	TODO: Add Unit Tests!
	public static final ColorSpaceD C_I_E = new ColorSpaceD(0.0D, 2.2D, 0.7350D, 0.2650D, 0.2740D, 0.7170D, 0.1670D, 0.0090D, 1.0D / 3.0D, 1.0D / 3.0D);
	
	/**
	 * A {@code ColorSpaceD} instance that represents the EBU color space.
	 */
//	TODO: Add Unit Tests!
	public static final ColorSpaceD E_B_U = new ColorSpaceD(0.018D, 20.0D / 9.0D, 0.6400D, 0.3300D, 0.2900D, 0.6000D, 0.1500D, 0.0600D, 0.31271D, 0.32902D);
	
	/**
	 * A {@code ColorSpaceD} instance that represents the HDTV color space.
	 */
//	TODO: Add Unit Tests!
	public static final ColorSpaceD H_D_T_V = new ColorSpaceD(0.018D, 20.0D / 9.0D, 0.6400D, 0.3300D, 0.3000D, 0.6000D, 0.1500D, 0.0600D, 0.31271D, 0.32902D);
	
	/**
	 * A {@code ColorSpaceD} instance that represents the NTSC color space.
	 */
//	TODO: Add Unit Tests!
	public static final ColorSpaceD N_T_S_C = new ColorSpaceD(0.018D, 20.0D / 9.0D, 0.6700D, 0.3300D, 0.2100D, 0.7100D, 0.1400D, 0.0800D, 0.31010D, 0.31620D);
	
	/**
	 * A {@code ColorSpaceD} instance that represents the SMPTE 240M color space.
	 */
//	TODO: Add Unit Tests!
	public static final ColorSpaceD S_M_P_T_E_240_M = new ColorSpaceD(0.018D, 20.0D / 9.0D, 0.6300D, 0.3400D, 0.3100D, 0.5950D, 0.1550D, 0.0700D, 0.31271D, 0.32902D);
	
	/**
	 * A {@code ColorSpaceD} instance that represents the SMPTE C color space.
	 */
//	TODO: Add Unit Tests!
	public static final ColorSpaceD S_M_P_T_E_C = new ColorSpaceD(0.018D, 20.0D / 9.0D, 0.6300D, 0.3400D, 0.3100D, 0.5950D, 0.1550D, 0.0700D, 0.31271D, 0.32902D);
	
	/**
	 * A {@code ColorSpaceD} instance that represents the sRGB color space.
	 */
//	TODO: Add Unit Tests!
	public static final ColorSpaceD S_R_G_B = new ColorSpaceD(0.00304D, 2.4D, 0.6400D, 0.3300D, 0.3000D, 0.6000D, 0.1500D, 0.0600D, 0.31271D, 0.32902D);
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final double breakPoint;
	private final double gamma;
//	private final double gammaReciprocal;
	private final double segmentOffset;
	private final double slope;
	private final double slopeMatch;
//	private final double slopeMatchReciprocal;
//	private final double slopeReciprocal;
	private final double[] matrixRGBToXYZ;
	private final double[] matrixXYZToRGB;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code ColorSpaceD} instance.
	 * 
	 * @param breakPoint the break point to use
	 * @param gamma the gamma to use
	 * @param xR the XR element of the matrix
	 * @param yR the YR element of the matrix
	 * @param xG the XG element of the matrix
	 * @param yG the YG element of the matrix
	 * @param xB the XB element of the matrix
	 * @param yB the YB element of the matrix
	 * @param xW the XW element of the matrix
	 * @param yW the YW element of the matrix
	 */
//	TODO: Add Unit Tests!
	public ColorSpaceD(final double breakPoint, final double gamma, final double xR, final double yR, final double xG, final double yG, final double xB, final double yB, final double xW, final double yW) {
		this.breakPoint = breakPoint;
		this.gamma = gamma;
//		this.gammaReciprocal = 1.0D / gamma;
		this.slope = breakPoint > 0.0D ? 1.0D / (gamma / pow(breakPoint, 1.0D / gamma - 1.0D) - gamma * breakPoint + breakPoint) : 1.0D;
		this.slopeMatch = breakPoint > 0.0D ? gamma * this.slope / pow(breakPoint, 1.0D / gamma - 1.0D) : 1.0D;
//		this.slopeMatchReciprocal = 1.0D / this.slopeMatch;
//		this.slopeReciprocal = 1.0D / this.slope;
		this.segmentOffset = breakPoint > 0.0D ? this.slopeMatch * pow(breakPoint, 1.0D / gamma) - this.slope * breakPoint : 0.0D;
		this.matrixXYZToRGB = doCreateMatrixXYZToRGB(xR, yR, xG, yG, xB, yB, xW, yW);
		this.matrixRGBToXYZ = doCreateMatrixRGBToXYZ(xW, yW, this.matrixXYZToRGB);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Converts {@code colorRGB} from RGB color space to XYZ color space.
	 * <p>
	 * Returns a new {@link Color3D} instance with the result of the conversion.
	 * <p>
	 * If {@code colorRGB} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param colorRGB a {@code Color3D} instance in the RGB color space
	 * @return a new {@code Color3D} instance with the result of the conversion
	 * @throws NullPointerException thrown if, and only if, {@code colorRGB} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public Color3D convertRGBToXYZ(final Color3D colorRGB) {
		final double r = colorRGB.getR();
		final double g = colorRGB.getG();
		final double b = colorRGB.getB();
		
		final double x = this.matrixRGBToXYZ[0] * r + this.matrixRGBToXYZ[3] * g + this.matrixRGBToXYZ[6] * b;
		final double y = this.matrixRGBToXYZ[1] * r + this.matrixRGBToXYZ[4] * g + this.matrixRGBToXYZ[7] * b;
		final double z = this.matrixRGBToXYZ[2] * r + this.matrixRGBToXYZ[5] * g + this.matrixRGBToXYZ[8] * b;
		
		return new Color3D(x, y, z);
	}
	
	/**
	 * Converts {@code colorXYZ} from XYZ color space to RGB color space.
	 * <p>
	 * Returns a new {@link Color3D} instance with the result of the conversion.
	 * <p>
	 * If {@code colorXYZ} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param colorXYZ a {@code Color3D} instance in the XYZ color space
	 * @return a new {@code Color3D} instance with the result of the conversion
	 * @throws NullPointerException thrown if, and only if, {@code colorXYZ} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public Color3D convertXYZToRGB(final Color3D colorXYZ) {
		final double x = colorXYZ.getX();
		final double y = colorXYZ.getY();
		final double z = colorXYZ.getZ();
		
		final double r = this.matrixXYZToRGB[0] * x + this.matrixXYZToRGB[1] * y + this.matrixXYZToRGB[2] * z;
		final double g = this.matrixXYZToRGB[3] * x + this.matrixXYZToRGB[4] * y + this.matrixXYZToRGB[5] * z;
		final double b = this.matrixXYZToRGB[6] * x + this.matrixXYZToRGB[7] * y + this.matrixXYZToRGB[8] * z;
		
		return new Color3D(r, g, b);
	}
	
	/**
	 * Redoes gamma correction on {@code color}.
	 * <p>
	 * Returns a new {@link Color3D} instance with the result of the operation.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color3D} instance
	 * @return a new {@code Color3D} instance with the result of the operation
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public Color3D redoGammaCorrection(final Color3D color) {
		final double component1 = doRedoGammaCorrection(color.getComponent1());
		final double component2 = doRedoGammaCorrection(color.getComponent2());
		final double component3 = doRedoGammaCorrection(color.getComponent3());
		
		return new Color3D(component1, component2, component3);
	}
	
	/**
	 * Undoes gamma correction on {@code color}.
	 * <p>
	 * Returns a new {@link Color3D} instance with the result of the operation.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color3D} instance
	 * @return a new {@code Color3D} instance with the result of the operation
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public Color3D undoGammaCorrection(final Color3D color) {
		final double component1 = doUndoGammaCorrection(color.getComponent1());
		final double component2 = doUndoGammaCorrection(color.getComponent2());
		final double component3 = doUndoGammaCorrection(color.getComponent3());
		
		return new Color3D(component1, component2, component3);
	}
	
	/**
	 * Converts {@code colorRGBA} from RGB color space to XYZ color space.
	 * <p>
	 * Returns a new {@link Color4D} instance with the result of the conversion.
	 * <p>
	 * If {@code colorRGBA} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param colorRGBA a {@code Color4D} instance in the RGB color space
	 * @return a new {@code Color4D} instance with the result of the conversion
	 * @throws NullPointerException thrown if, and only if, {@code colorRGBA} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public Color4D convertRGBAToXYZA(final Color4D colorRGBA) {
		final double r = colorRGBA.getR();
		final double g = colorRGBA.getG();
		final double b = colorRGBA.getB();
		final double a = colorRGBA.getA();
		
		final double x = this.matrixRGBToXYZ[0] * r + this.matrixRGBToXYZ[3] * g + this.matrixRGBToXYZ[6] * b;
		final double y = this.matrixRGBToXYZ[1] * r + this.matrixRGBToXYZ[4] * g + this.matrixRGBToXYZ[7] * b;
		final double z = this.matrixRGBToXYZ[2] * r + this.matrixRGBToXYZ[5] * g + this.matrixRGBToXYZ[8] * b;
		
		return new Color4D(x, y, z, a);
	}
	
	/**
	 * Converts {@code colorXYZA} from XYZ color space to RGB color space.
	 * <p>
	 * Returns a new {@link Color4D} instance with the result of the conversion.
	 * <p>
	 * If {@code colorXYZA} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param colorXYZA a {@code Color4D} instance in the XYZ color space
	 * @return a new {@code Color4D} instance with the result of the conversion
	 * @throws NullPointerException thrown if, and only if, {@code colorXYZA} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public Color4D convertXYZAToRGBA(final Color4D colorXYZA) {
		final double x = colorXYZA.getX();
		final double y = colorXYZA.getY();
		final double z = colorXYZA.getZ();
		final double a = colorXYZA.getA();
		
		final double r = this.matrixXYZToRGB[0] * x + this.matrixXYZToRGB[1] * y + this.matrixXYZToRGB[2] * z;
		final double g = this.matrixXYZToRGB[3] * x + this.matrixXYZToRGB[4] * y + this.matrixXYZToRGB[5] * z;
		final double b = this.matrixXYZToRGB[6] * x + this.matrixXYZToRGB[7] * y + this.matrixXYZToRGB[8] * z;
		
		return new Color4D(r, g, b, a);
	}
	
	/**
	 * Redoes gamma correction on {@code color}.
	 * <p>
	 * Returns a new {@link Color4D} instance with the result of the operation.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color4D} instance
	 * @return a new {@code Color4D} instance with the result of the operation
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public Color4D redoGammaCorrection(final Color4D color) {
		final double component1 = doRedoGammaCorrection(color.getComponent1());
		final double component2 = doRedoGammaCorrection(color.getComponent2());
		final double component3 = doRedoGammaCorrection(color.getComponent3());
		final double component4 = color.getComponent4();
		
		return new Color4D(component1, component2, component3, component4);
	}
	
	/**
	 * Undoes gamma correction on {@code color}.
	 * <p>
	 * Returns a new {@link Color4D} instance with the result of the operation.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color4D} instance
	 * @return a new {@code Color4D} instance with the result of the operation
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public Color4D undoGammaCorrection(final Color4D color) {
		final double component1 = doUndoGammaCorrection(color.getComponent1());
		final double component2 = doUndoGammaCorrection(color.getComponent2());
		final double component3 = doUndoGammaCorrection(color.getComponent3());
		final double component4 = color.getComponent4();
		
		return new Color4D(component1, component2, component3, component4);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private double doRedoGammaCorrection(final double value) {
		return value <= this.breakPoint ? value * this.slope : this.slopeMatch * pow(value, 1.0D / this.gamma) - this.segmentOffset;
	}
	
	private double doUndoGammaCorrection(final double value) {
		return value <= this.breakPoint * this.slope ? value / this.slope : pow((value + this.segmentOffset) / this.slopeMatch, this.gamma);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static double[] doCreateMatrixRGBToXYZ(final double xW, final double yW, final double[] m) {
		final double a = m[0] * (m[4] * m[8] - m[7] * m[5]);
		final double b = m[1] * (m[3] * m[8] - m[6] * m[5]);
		final double c = m[2] * (m[3] * m[7] - m[6] * m[4]);
		final double s = 1.0D / (a - b + c);
		
		final double eXR = s * (m[4] * m[8] - m[5] * m[7]);
		final double eYR = s * (m[5] * m[6] - m[3] * m[8]);
		final double eZR = s * (m[3] * m[7] - m[4] * m[6]);
		final double eXG = s * (m[2] * m[7] - m[1] * m[8]);
		final double eYG = s * (m[0] * m[8] - m[2] * m[6]);
		final double eZG = s * (m[1] * m[6] - m[0] * m[7]);
		final double eXB = s * (m[1] * m[5] - m[2] * m[4]);
		final double eYB = s * (m[2] * m[3] - m[0] * m[5]);
		final double eZB = s * (m[0] * m[4] - m[1] * m[3]);
		final double eXW = xW;
		final double eYW = yW;
		final double eZW = 1.0D - (xW + yW);
		
		return new double[] {
			eXR, eYR, eZR,
			eXG, eYG, eZG,
			eXB, eYB, eZB,
			eXW, eYW, eZW
		};
	}
	
	private static double[] doCreateMatrixXYZToRGB(final double xR, final double yR, final double xG, final double yG, final double xB, final double yB, final double xW, final double yW) {
		final double zR = 1.0D - (xR + yR);
		final double zG = 1.0D - (xG + yG);
		final double zB = 1.0D - (xB + yB);
		final double zW = 1.0D - (xW + yW);
		final double rX = (yG * zB) - (yB * zG);
		final double rY = (xB * zG) - (xG * zB);
		final double rZ = (xG * yB) - (xB * yG);
		final double rW = ((rX * xW) + (rY * yW) + (rZ * zW)) / yW;
		final double gX = (yB * zR) - (yR * zB);
		final double gY = (xR * zB) - (xB * zR);
		final double gZ = (xB * yR) - (xR * yB);
		final double gW = ((gX * xW) + (gY * yW) + (gZ * zW)) / yW;
		final double bX = (yR * zG) - (yG * zR);
		final double bY = (xG * zR) - (xR * zG);
		final double bZ = (xR * yG) - (xG * yR);
		final double bW = ((bX * xW) + (bY * yW) + (bZ * zW)) / yW;
		
		final double eRX = rX / rW;
		final double eRY = rY / rW;
		final double eRZ = rZ / rW;
		final double eGX = gX / gW;
		final double eGY = gY / gW;
		final double eGZ = gZ / gW;
		final double eBX = bX / bW;
		final double eBY = bY / bW;
		final double eBZ = bZ / bW;
		final double eRW = rW;
		final double eGW = gW;
		final double eBW = bW;
		
		return new double[] {
			eRX, eRY, eRZ,
			eGX, eGY, eGZ,
			eBX, eBY, eBZ,
			eRW, eGW, eBW
		};
	}
}