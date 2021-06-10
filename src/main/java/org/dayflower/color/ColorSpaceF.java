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

import static org.dayflower.utility.Floats.pow;

/**
 * A {@code ColorSpaceF} represents a color space that performs conversions for the color types {@link Color3F} and {@link Color4F}.
 * <p>
 * An instance of this class can convert {@code Color3F} and {@code Color4F} instances from a specific RGB color space to a specific XYZ color space, as well as from a specific XYZ color space to a specific RGB color space. In addition to that, it can
 * also redo or undo gamma correction.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class ColorSpaceF {
	/**
	 * A {@code ColorSpaceF} instance that represents the Adobe RGB (1998) color space.
	 */
	public static final ColorSpaceF ADOBE_R_G_B_1998 = new ColorSpaceF(0.0F, 2.2F, 0.6400F, 0.3300F, 0.2100F, 0.7100F, 0.1500F, 0.0600F, 0.31271F, 0.32902F);
	
	/**
	 * A {@code ColorSpaceF} instance that represents the Adobe Wide Gamut RGB color space.
	 */
	public static final ColorSpaceF ADOBE_WIDE_GAMUT_R_G_B = new ColorSpaceF(0.0F, 563.0F / 256.0F, 0.7347F, 0.2653F, 0.1152F, 0.8264F, 0.1566F, 0.0177F, 0.3457F, 0.3585F);
	
	/**
	 * A {@code ColorSpaceF} instance that represents the Apple color space.
	 */
	public static final ColorSpaceF APPLE = new ColorSpaceF(0.0F, 1.8F, 0.6250F, 0.3400F, 0.2800F, 0.5950F, 0.1550F, 0.0700F, 0.31271F, 0.32902F);
	
	/**
	 * A {@code ColorSpaceF} instance that represents the CIE color space.
	 */
	public static final ColorSpaceF C_I_E = new ColorSpaceF(0.0F, 2.2F, 0.7350F, 0.2650F, 0.2740F, 0.7170F, 0.1670F, 0.0090F, 1.0F / 3.0F, 1.0F / 3.0F);
	
	/**
	 * A {@code ColorSpaceF} instance that represents the EBU color space.
	 */
	public static final ColorSpaceF E_B_U = new ColorSpaceF(0.018F, 20.0F / 9.0F, 0.6400F, 0.3300F, 0.2900F, 0.6000F, 0.1500F, 0.0600F, 0.31271F, 0.32902F);
	
	/**
	 * A {@code ColorSpaceF} instance that represents the HDTV color space.
	 */
	public static final ColorSpaceF H_D_T_V = new ColorSpaceF(0.018F, 20.0F / 9.0F, 0.6400F, 0.3300F, 0.3000F, 0.6000F, 0.1500F, 0.0600F, 0.31271F, 0.32902F);
	
	/**
	 * A {@code ColorSpaceF} instance that represents the NTSC color space.
	 */
	public static final ColorSpaceF N_T_S_C = new ColorSpaceF(0.018F, 20.0F / 9.0F, 0.6700F, 0.3300F, 0.2100F, 0.7100F, 0.1400F, 0.0800F, 0.31010F, 0.31620F);
	
	/**
	 * A {@code ColorSpaceF} instance that represents the SMPTE 240M color space.
	 */
	public static final ColorSpaceF S_M_P_T_E_240_M = new ColorSpaceF(0.018F, 20.0F / 9.0F, 0.6300F, 0.3400F, 0.3100F, 0.5950F, 0.1550F, 0.0700F, 0.31271F, 0.32902F);
	
	/**
	 * A {@code ColorSpaceF} instance that represents the SMPTE C color space.
	 */
	public static final ColorSpaceF S_M_P_T_E_C = new ColorSpaceF(0.018F, 20.0F / 9.0F, 0.6300F, 0.3400F, 0.3100F, 0.5950F, 0.1550F, 0.0700F, 0.31271F, 0.32902F);
	
	/**
	 * A {@code ColorSpaceF} instance that represents the sRGB color space.
	 */
	public static final ColorSpaceF S_R_G_B = new ColorSpaceF(0.00304F, 2.4F, 0.6400F, 0.3300F, 0.3000F, 0.6000F, 0.1500F, 0.0600F, 0.31271F, 0.32902F);
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final float breakPoint;
	private final float gamma;
	private final float segmentOffset;
	private final float slope;
	private final float slopeMatch;
	private final float[] matrixRGBToXYZ;
	private final float[] matrixXYZToRGB;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code ColorSpaceF} instance.
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
	public ColorSpaceF(final float breakPoint, final float gamma, final float xR, final float yR, final float xG, final float yG, final float xB, final float yB, final float xW, final float yW) {
		this.breakPoint = breakPoint;
		this.gamma = gamma;
		this.slope = breakPoint > 0.0F ? 1.0F / (gamma / pow(breakPoint, 1.0F / gamma - 1.0F) - gamma * breakPoint + breakPoint) : 1.0F;
		this.slopeMatch = breakPoint > 0.0F ? gamma * this.slope / pow(breakPoint, 1.0F / gamma - 1.0F) : 1.0F;
		this.segmentOffset = breakPoint > 0.0F ? this.slopeMatch * pow(breakPoint, 1.0F / gamma) - this.slope * breakPoint : 0.0F;
		this.matrixXYZToRGB = doCreateColorSpace3MatrixXYZToRGB(xR, yR, xG, yG, xB, yB, xW, yW);
		this.matrixRGBToXYZ = doCreateColorSpace3MatrixRGBToXYZ(xW, yW, this.matrixXYZToRGB);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Converts {@code colorRGB} from RGB color space to XYZ color space.
	 * <p>
	 * Returns a new {@link Color3F} instance with the result of the conversion.
	 * <p>
	 * If {@code colorRGB} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param colorRGB a {@code Color3F} instance in the RGB color space
	 * @return a new {@code Color3F} instance with the result of the conversion
	 * @throws NullPointerException thrown if, and only if, {@code colorRGB} is {@code null}
	 */
	public Color3F convertRGBToXYZ(final Color3F colorRGB) {
		final float r = colorRGB.getR();
		final float g = colorRGB.getG();
		final float b = colorRGB.getB();
		
		final float x = this.matrixRGBToXYZ[0] * r + this.matrixRGBToXYZ[3] * g + this.matrixRGBToXYZ[6] * b;
		final float y = this.matrixRGBToXYZ[1] * r + this.matrixRGBToXYZ[4] * g + this.matrixRGBToXYZ[7] * b;
		final float z = this.matrixRGBToXYZ[2] * r + this.matrixRGBToXYZ[5] * g + this.matrixRGBToXYZ[8] * b;
		
		return new Color3F(x, y, z);
	}
	
	/**
	 * Converts {@code colorXYZ} from XYZ color space to RGB color space.
	 * <p>
	 * Returns a new {@link Color3F} instance with the result of the conversion.
	 * <p>
	 * If {@code colorXYZ} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param colorXYZ a {@code Color3F} instance in the XYZ color space
	 * @return a new {@code Color3F} instance with the result of the conversion
	 * @throws NullPointerException thrown if, and only if, {@code colorXYZ} is {@code null}
	 */
	public Color3F convertXYZToRGB(final Color3F colorXYZ) {
		final float x = colorXYZ.getX();
		final float y = colorXYZ.getY();
		final float z = colorXYZ.getZ();
		
		final float r = this.matrixXYZToRGB[0] * x + this.matrixXYZToRGB[1] * y + this.matrixXYZToRGB[2] * z;
		final float g = this.matrixXYZToRGB[3] * x + this.matrixXYZToRGB[4] * y + this.matrixXYZToRGB[5] * z;
		final float b = this.matrixXYZToRGB[6] * x + this.matrixXYZToRGB[7] * y + this.matrixXYZToRGB[8] * z;
		
		return new Color3F(r, g, b);
	}
	
	/**
	 * Redoes gamma correction on {@code color}.
	 * <p>
	 * Returns a new {@link Color3F} instance with the result of the operation.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color3F} instance
	 * @return a new {@code Color3F} instance with the result of the operation
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public Color3F redoGammaCorrection(final Color3F color) {
		final float component1 = doRedoGammaCorrection(color.getComponent1());
		final float component2 = doRedoGammaCorrection(color.getComponent2());
		final float component3 = doRedoGammaCorrection(color.getComponent3());
		
		return new Color3F(component1, component2, component3);
	}
	
	/**
	 * Undoes gamma correction on {@code color}.
	 * <p>
	 * Returns a new {@link Color3F} instance with the result of the operation.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color3F} instance
	 * @return a new {@code Color3F} instance with the result of the operation
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public Color3F undoGammaCorrection(final Color3F color) {
		final float component1 = doUndoGammaCorrection(color.getComponent1());
		final float component2 = doUndoGammaCorrection(color.getComponent2());
		final float component3 = doUndoGammaCorrection(color.getComponent3());
		
		return new Color3F(component1, component2, component3);
	}
	
	/**
	 * Converts {@code colorRGBA} from RGB color space to XYZ color space.
	 * <p>
	 * Returns a new {@link Color4F} instance with the result of the conversion.
	 * <p>
	 * If {@code colorRGBA} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param colorRGBA a {@code Color4F} instance in the RGB color space
	 * @return a new {@code Color4F} instance with the result of the conversion
	 * @throws NullPointerException thrown if, and only if, {@code colorRGBA} is {@code null}
	 */
	public Color4F convertRGBAToXYZA(final Color4F colorRGBA) {
		final float r = colorRGBA.getR();
		final float g = colorRGBA.getG();
		final float b = colorRGBA.getB();
		final float a = colorRGBA.getA();
		
		final float x = this.matrixRGBToXYZ[0] * r + this.matrixRGBToXYZ[3] * g + this.matrixRGBToXYZ[6] * b;
		final float y = this.matrixRGBToXYZ[1] * r + this.matrixRGBToXYZ[4] * g + this.matrixRGBToXYZ[7] * b;
		final float z = this.matrixRGBToXYZ[2] * r + this.matrixRGBToXYZ[5] * g + this.matrixRGBToXYZ[8] * b;
		
		return new Color4F(x, y, z, a);
	}
	
	/**
	 * Converts {@code colorXYZA} from XYZ color space to RGB color space.
	 * <p>
	 * Returns a new {@link Color4F} instance with the result of the conversion.
	 * <p>
	 * If {@code colorXYZA} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param colorXYZA a {@code Color4F} instance in the XYZ color space
	 * @return a new {@code Color4F} instance with the result of the conversion
	 * @throws NullPointerException thrown if, and only if, {@code colorXYZA} is {@code null}
	 */
	public Color4F convertXYZAToRGBA(final Color4F colorXYZA) {
		final float x = colorXYZA.getX();
		final float y = colorXYZA.getY();
		final float z = colorXYZA.getZ();
		final float a = colorXYZA.getA();
		
		final float r = this.matrixXYZToRGB[0] * x + this.matrixXYZToRGB[1] * y + this.matrixXYZToRGB[2] * z;
		final float g = this.matrixXYZToRGB[3] * x + this.matrixXYZToRGB[4] * y + this.matrixXYZToRGB[5] * z;
		final float b = this.matrixXYZToRGB[6] * x + this.matrixXYZToRGB[7] * y + this.matrixXYZToRGB[8] * z;
		
		return new Color4F(r, g, b, a);
	}
	
	/**
	 * Redoes gamma correction on {@code color}.
	 * <p>
	 * Returns a new {@link Color4F} instance with the result of the operation.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color4F} instance
	 * @return a new {@code Color4F} instance with the result of the operation
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public Color4F redoGammaCorrection(final Color4F color) {
		final float component1 = doRedoGammaCorrection(color.getComponent1());
		final float component2 = doRedoGammaCorrection(color.getComponent2());
		final float component3 = doRedoGammaCorrection(color.getComponent3());
		final float component4 = color.getComponent4();
		
		return new Color4F(component1, component2, component3, component4);
	}
	
	/**
	 * Undoes gamma correction on {@code color}.
	 * <p>
	 * Returns a new {@link Color4F} instance with the result of the operation.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color4F} instance
	 * @return a new {@code Color4F} instance with the result of the operation
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public Color4F undoGammaCorrection(final Color4F color) {
		final float component1 = doUndoGammaCorrection(color.getComponent1());
		final float component2 = doUndoGammaCorrection(color.getComponent2());
		final float component3 = doUndoGammaCorrection(color.getComponent3());
		final float component4 = color.getComponent4();
		
		return new Color4F(component1, component2, component3, component4);
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