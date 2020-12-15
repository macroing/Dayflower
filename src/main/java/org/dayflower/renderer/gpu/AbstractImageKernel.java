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
package org.dayflower.renderer.gpu;

import org.dayflower.util.Bytes;
import org.dayflower.util.Floats;
import org.dayflower.util.Ints;

/**
 * An {@code AbstractImageKernel} is an abstract extension of the {@code AbstractKernel} class that adds additional features.
 * <p>
 * The features added are the following:
 * <ul>
 * <li>Color conversion methods</li>
 * <li>Image rendering methods</li>
 * <li>Monte Carlo-method based image sampling using a stable Moving Average algorithm</li>
 * </ul>
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public abstract class AbstractImageKernel extends AbstractKernel {
	private static final int FILM_FLAG_CLEAR = 0x0001;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * A {@code byte[]} with image colors.
	 */
	protected byte[] imageColorByteArray;
	
	/**
	 * A {@code float[]} with film colors.
	 */
	protected float[] filmColorFloatArray;
	
	/**
	 * A {@code float[]} with image colors.
	 */
	protected float[] imageColorFloatArray;
	
	/**
	 * An {@code int} with film flags.
	 */
	protected int filmFlags;
	
	/**
	 * An {@code int[]} with film samples.
	 */
	protected int[] filmSampleIntArray;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code AbstractImageKernel} instance.
	 */
	protected AbstractImageKernel() {
//		Initialize the film variables:
		this.filmColorFloatArray = new float[0];
		this.filmFlags = 0;
		this.filmSampleIntArray = new int[0];
		
//		Initialize the image variables:
		this.imageColorByteArray = new byte[0];
		this.imageColorFloatArray = new float[0];
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Call this method to hint to this {@code AbstractImageKernel} instance that it should clear the film before rendering to it in the next render pass.
	 */
	public final void filmClear() {
		this.filmFlags |= FILM_FLAG_CLEAR;
	}
	
	/**
	 * Call this method to clear the film flags.
	 */
	public final void filmClearFilmFlags() {
		this.filmFlags = 0;
	}
	
	/**
	 * Sets up all necessary resources for this {@code AbstractImageKernel} instance.
	 */
	@Override
	public void setup() {
		super.setup();
		
		doSetupFilmColorFloatArray();
		doSetupFilmSampleIntArray();
		doSetupImageColorByteArray();
		doSetupImageColorFloatArray();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the {@code byte[]} with image colors.
	 * 
	 * @return the {@code byte[]} with image colors
	 */
	protected final byte[] getImageColorByteArray() {
		return getAndReturn(this.imageColorByteArray);
	}
	
	/**
	 * Returns the B-component of {@code colorRGB} as a {@code float} in the interval [0.0, 1.0].
	 * 
	 * @param colorRGB an {@code int} with a color in packed form using either RGB or ARGB as its order
	 * @return the B-component of {@code colorRGB} as a {@code float} in the interval [0.0, 1.0]
	 */
	protected final float colorRGBIntToBFloat(final int colorRGB) {
		return colorRGBIntToBInt(colorRGB) / 255.0F;
	}
	
	/**
	 * Returns the G-component of {@code colorRGB} as a {@code float} in the interval [0.0, 1.0].
	 * 
	 * @param colorRGB an {@code int} with a color in packed form using either RGB or ARGB as its order
	 * @return the G-component of {@code colorRGB} as a {@code float} in the interval [0.0, 1.0]
	 */
	protected final float colorRGBIntToGFloat(final int colorRGB) {
		return colorRGBIntToGInt(colorRGB) / 255.0F;
	}
	
	/**
	 * Returns the R-component of {@code colorRGB} as a {@code float} in the interval [0.0, 1.0].
	 * 
	 * @param colorRGB an {@code int} with a color in packed form using either RGB or ARGB as its order
	 * @return the R-component of {@code colorRGB} as a {@code float} in the interval [0.0, 1.0]
	 */
	protected final float colorRGBIntToRFloat(final int colorRGB) {
		return colorRGBIntToRInt(colorRGB) / 255.0F;
	}
	
	/**
	 * Returns a {@code float} with the B-component of the image at the current pixel.
	 * 
	 * @return a {@code float} with the B-component of the image at the current pixel
	 */
	protected final float imageGetColorB() {
		return this.imageColorFloatArray[getGlobalId() * 3 + 2];
	}
	
	/**
	 * Returns a {@code float} with the G-component of the image at the current pixel.
	 * 
	 * @return a {@code float} with the G-component of the image at the current pixel
	 */
	protected final float imageGetColorG() {
		return this.imageColorFloatArray[getGlobalId() * 3 + 1];
	}
	
	/**
	 * Returns a {@code float} with the R-component of the image at the current pixel.
	 * 
	 * @return a {@code float} with the R-component of the image at the current pixel
	 */
	protected final float imageGetColorR() {
		return this.imageColorFloatArray[getGlobalId() * 3 + 0];
	}
	
	/**
	 * Returns the {@code float[]} with image colors.
	 * 
	 * @return the {@code float[]} with image colors
	 */
	protected final float[] getImageColorFloatArray() {
		return getAndReturn(this.imageColorFloatArray);
	}
	
	/**
	 * Returns an {@code int} representation of {@code colorB}.
	 * <p>
	 * If {@code colorB} is outside the interval [0.0, 1.0], it will be saturated.
	 * 
	 * @param colorB a {@code float} with the B-component
	 * @return an {@code int} representation of {@code colorB}
	 */
	protected final int colorBFloatToBInt(final float colorB) {
		return (int)(saturateFloat(colorB, 0.0F, 1.0F) * 255.0F + 0.5F);
	}
	
	/**
	 * Returns an {@code int} representation of {@code colorG}.
	 * <p>
	 * If {@code colorG} is outside the interval [0.0, 1.0], it will be saturated.
	 * 
	 * @param colorG a {@code float} with the G-component
	 * @return an {@code int} representation of {@code colorG}
	 */
	protected final int colorGFloatToGInt(final float colorG) {
		return (int)(saturateFloat(colorG, 0.0F, 1.0F) * 255.0F + 0.5F);
	}
	
	/**
	 * Returns an {@code int} representation of {@code colorR}.
	 * <p>
	 * If {@code colorR} is outside the interval [0.0, 1.0], it will be saturated.
	 * 
	 * @param colorR a {@code float} with the R-component
	 * @return an {@code int} representation of {@code colorR}
	 */
	protected final int colorRFloatToRInt(final float colorR) {
		return (int)(saturateFloat(colorR, 0.0F, 1.0F) * 255.0F + 0.5F);
	}
	
	/**
	 * Returns an {@code int} with the color represented by {@code colorR}, {@code colorG} and {@code colorB} in packed form using either RGB or ARGB as its order.
	 * <p>
	 * If either {@code colorR}, {@code colorG} or {@code colorB} are outside the interval [0.0, 1.0], they will be saturated.
	 * 
	 * @param colorR a {@code float} with the R-component
	 * @param colorG a {@code float} with the G-component
	 * @param colorB a {@code float} with the B-component
	 * @return an {@code int} with the color represented by {@code colorR}, {@code colorG} and {@code colorB} in packed form using either RGB or ARGB as its order
	 */
	protected final int colorRGBFloatToRGBInt(final float colorR, final float colorG, final float colorB) {
		return colorRGBIntToRGBInt(colorRFloatToRInt(colorR), colorGFloatToGInt(colorG), colorBFloatToBInt(colorB));
	}
	
	/**
	 * Returns an {@code int} with the color represented by {@code colorR}, {@code colorG} and {@code colorB} in packed form using either RGB or ARGB as its order.
	 * <p>
	 * If either {@code colorR}, {@code colorG} or {@code colorB} are outside the interval [0, 255], they will be saturated.
	 * 
	 * @param colorR an {@code int} with the R-component
	 * @param colorG an {@code int} with the G-component
	 * @param colorB an {@code int} with the B-component
	 * @return an {@code int} with the color represented by {@code colorR}, {@code colorG} and {@code colorB} in packed form using either RGB or ARGB as its order
	 */
	protected final int colorRGBIntToRGBInt(final int colorR, final int colorG, final int colorB) {
		final int colorRSaturated = saturateInt(colorR, 0, 255);
		final int colorGSaturated = saturateInt(colorG, 0, 255);
		final int colorBSaturated = saturateInt(colorB, 0, 255);
		
		final int colorRGB = (colorRSaturated << 16) | (colorGSaturated << 8) | (colorBSaturated << 0);
		
		return colorRGB;
	}
	
	/**
	 * Returns the B-component of {@code colorRGB} as an {@code int} in the interval [0, 255].
	 * 
	 * @param colorRGB an {@code int} with a color in packed form using either RGB or ARGB as its order
	 * @return the B-component of {@code colorRGB} as an {@code int} in the interval [0, 255]
	 */
	@SuppressWarnings("static-method")
	protected final int colorRGBIntToBInt(final int colorRGB) {
		return (colorRGB >>  0) & 0xFF;
	}
	
	/**
	 * Returns the G-component of {@code colorRGB} as an {@code int} in the interval [0, 255].
	 * 
	 * @param colorRGB an {@code int} with a color in packed form using either RGB or ARGB as its order
	 * @return the G-component of {@code colorRGB} as an {@code int} in the interval [0, 255]
	 */
	@SuppressWarnings("static-method")
	protected final int colorRGBIntToGInt(final int colorRGB) {
		return (colorRGB >>  8) & 0xFF;
	}
	
	/**
	 * Returns the R-component of {@code colorRGB} as an {@code int} in the interval [0, 255].
	 * 
	 * @param colorRGB an {@code int} with a color in packed form using either RGB or ARGB as its order
	 * @return the R-component of {@code colorRGB} as an {@code int} in the interval [0, 255]
	 */
	@SuppressWarnings("static-method")
	protected final int colorRGBIntToRInt(final int colorRGB) {
		return (colorRGB >> 16) & 0xFF;
	}
	
	/**
	 * Returns the resolution along the X-axis of the film.
	 * 
	 * @return the resolution along the X-axis of the film
	 */
	protected final int filmGetResolutionX() {
		return super.resolutionX;
	}
	
	/**
	 * Returns the resolution along the Y-axis of the film.
	 * 
	 * @return the resolution along the Y-axis of the film
	 */
	protected final int filmGetResolutionY() {
		return super.resolutionY;
	}
	
	/**
	 * Returns the X-coordinate of the film.
	 * 
	 * @return the X-coordinate of the film
	 */
	protected final int filmGetX() {
		return getGlobalId() % super.resolutionX;
	}
	
	/**
	 * Returns the Y-coordinate of the film.
	 * 
	 * @return the Y-coordinate of the film
	 */
	protected final int filmGetY() {
		return getGlobalId() / super.resolutionX;
	}
	
	/**
	 * Returns the resolution along the X-axis of the image.
	 * 
	 * @return the resolution along the X-axis of the image
	 */
	protected final int imageGetResolutionX() {
		return super.resolutionX;
	}
	
	/**
	 * Returns the resolution along the Y-axis of the image.
	 * 
	 * @return the resolution along the Y-axis of the image
	 */
	protected final int imageGetResolutionY() {
		return super.resolutionY;
	}
	
	/**
	 * Returns the X-coordinate of the image.
	 * 
	 * @return the X-coordinate of the image
	 */
	protected final int imageGetX() {
		return getGlobalId() % super.resolutionX;
	}
	
	/**
	 * Returns the Y-coordinate of the image.
	 * 
	 * @return the Y-coordinate of the image
	 */
	protected final int imageGetY() {
		return getGlobalId() / super.resolutionX;
	}
	
	/**
	 * Adds the sample RGB component values {@code colorR}, {@code colorG} and {@code colorB} to the current pixel of the film.
	 * <p>
	 * This method is useful for Monte Carlo-method based rendering. It should be avoided if you already know the final RGB color.
	 * <p>
	 * The current moving average algorithm used by this method is stable enough, that adding the same RGB color repeatedly won't cause major precision loss.
	 * 
	 * @param colorR the value of the R-component
	 * @param colorG the value of the G-component
	 * @param colorB the value of the B-component
	 */
	protected final void filmAddColor(final float colorR, final float colorG, final float colorB) {
		final int filmColorFloatArrayOffset = getGlobalId() * 3;
		final int filmSampleIntArrayOffset = getGlobalId();
		
		if((this.filmFlags & FILM_FLAG_CLEAR) != 0) {
			this.filmColorFloatArray[filmColorFloatArrayOffset + 0] = colorR;
			this.filmColorFloatArray[filmColorFloatArrayOffset + 1] = colorG;
			this.filmColorFloatArray[filmColorFloatArrayOffset + 2] = colorB;
			this.filmSampleIntArray[filmSampleIntArrayOffset] = 1;
		} else {
			final int oldFilmSample = this.filmSampleIntArray[filmSampleIntArrayOffset];
			final int newFilmSample = oldFilmSample + 1;
			
			final float oldFilmColorR = this.filmColorFloatArray[filmColorFloatArrayOffset + 0];
			final float oldFilmColorG = this.filmColorFloatArray[filmColorFloatArrayOffset + 1];
			final float oldFilmColorB = this.filmColorFloatArray[filmColorFloatArrayOffset + 2];
			
			final float newFilmColorR = oldFilmColorR + ((colorR - oldFilmColorR) / newFilmSample);
			final float newFilmColorG = oldFilmColorG + ((colorG - oldFilmColorG) / newFilmSample);
			final float newFilmColorB = oldFilmColorB + ((colorB - oldFilmColorB) / newFilmSample);
			
			this.filmColorFloatArray[filmColorFloatArrayOffset + 0] = newFilmColorR;
			this.filmColorFloatArray[filmColorFloatArrayOffset + 1] = newFilmColorG;
			this.filmColorFloatArray[filmColorFloatArrayOffset + 2] = newFilmColorB;
			this.filmSampleIntArray[filmSampleIntArrayOffset] = newFilmSample;
		}
	}
	
	/**
	 * Sets the RGB component values {@code colorR}, {@code colorG} and {@code colorB} for the current pixel of the film.
	 * <p>
	 * This method is useful if you already know the final RGB color. It should be avoided if you're using Monte Carlo-method based rendering.
	 * 
	 * @param colorR the value of the R-component
	 * @param colorG the value of the G-component
	 * @param colorB the value of the B-component
	 */
	protected final void filmSetColor(final float colorR, final float colorG, final float colorB) {
		final int filmColorFloatArrayOffset = getGlobalId() * 3;
		final int filmSampleIntArrayOffset = getGlobalId();
		
		this.filmColorFloatArray[filmColorFloatArrayOffset + 0] = colorR;
		this.filmColorFloatArray[filmColorFloatArrayOffset + 1] = colorG;
		this.filmColorFloatArray[filmColorFloatArrayOffset + 2] = colorB;
		this.filmSampleIntArray[filmSampleIntArrayOffset] = 1;
	}
	
	/**
	 * Adds the RGB component values {@code colorR}, {@code colorG} and {@code colorB} to the current pixel of the image.
	 * 
	 * @param colorR the value of the R-component
	 * @param colorG the value of the G-component
	 * @param colorB the value of the B-component
	 */
	protected final void imageAddColor(final float colorR, final float colorG, final float colorB) {
		final int imageColorFloatArrayOffset = getGlobalId() * 3;
		
		final float oldColorR = this.imageColorFloatArray[imageColorFloatArrayOffset + 0];
		final float oldColorG = this.imageColorFloatArray[imageColorFloatArrayOffset + 1];
		final float oldColorB = this.imageColorFloatArray[imageColorFloatArrayOffset + 2];
		
		final float newColorR = oldColorR + colorR;
		final float newColorG = oldColorG + colorG;
		final float newColorB = oldColorB + colorB;
		
		this.imageColorFloatArray[imageColorFloatArrayOffset + 0] = newColorR;
		this.imageColorFloatArray[imageColorFloatArrayOffset + 1] = newColorG;
		this.imageColorFloatArray[imageColorFloatArrayOffset + 2] = newColorB;
	}
	
	/**
	 * The image processing stage begins.
	 * <p>
	 * This method copies the current film color into an image color buffer. The image color buffer can be manipulated by other image processing methods.
	 * <p>
	 * When the image processing is done, {@link #imageEnd()} has to be called in order to end the image processing stage.
	 */
	protected final void imageBegin() {
		final int filmColorFloatArrayOffset = getGlobalId() * 3;
		final int imageColorFloatArrayOffset = getGlobalId() * 3;
		
		final float colorR = this.filmColorFloatArray[filmColorFloatArrayOffset + 0];
		final float colorG = this.filmColorFloatArray[filmColorFloatArrayOffset + 1];
		final float colorB = this.filmColorFloatArray[filmColorFloatArrayOffset + 2];
		
		this.imageColorFloatArray[imageColorFloatArrayOffset + 0] = colorR;
		this.imageColorFloatArray[imageColorFloatArrayOffset + 1] = colorG;
		this.imageColorFloatArray[imageColorFloatArrayOffset + 2] = colorB;
	}
	
	/**
	 * The image processing stage ends.
	 * <p>
	 * This method copies the current image color to its final destination so it can be displayed.
	 */
	protected final void imageEnd() {
		final int imageColorByteArrayOffset = getGlobalId() * 4;
		final int imageColorFloatArrayOffset = getGlobalId() * 3;
		
		final float imageColorFloatR = this.imageColorFloatArray[imageColorFloatArrayOffset + 0];
		final float imageColorFloatG = this.imageColorFloatArray[imageColorFloatArrayOffset + 1];
		final float imageColorFloatB = this.imageColorFloatArray[imageColorFloatArrayOffset + 2];
		
		final int imageColorIntR = (int)(min(max(imageColorFloatR, 0.0F), 1.0F) * 255.0F + 0.5F);
		final int imageColorIntG = (int)(min(max(imageColorFloatG, 0.0F), 1.0F) * 255.0F + 0.5F);
		final int imageColorIntB = (int)(min(max(imageColorFloatB, 0.0F), 1.0F) * 255.0F + 0.5F);
		final int imageColorIntA = 255;
		
		this.imageColorByteArray[imageColorByteArrayOffset + 0] = (byte)(imageColorIntB);
		this.imageColorByteArray[imageColorByteArrayOffset + 1] = (byte)(imageColorIntG);
		this.imageColorByteArray[imageColorByteArrayOffset + 2] = (byte)(imageColorIntR);
		this.imageColorByteArray[imageColorByteArrayOffset + 3] = (byte)(imageColorIntA);
	}
	
	/**
	 * Redoes the gamma correction for the current pixel of the image using the algorithm provided by PBRT.
	 * <p>
	 * This method assumes the RGB color for the current pixel is linear.
	 */
	protected final void imageRedoGammaCorrectionPBRT() {
		final int imageColorFloatArrayOffset = getGlobalId() * 3;
		
		final float oldColorR = this.imageColorFloatArray[imageColorFloatArrayOffset + 0];
		final float oldColorG = this.imageColorFloatArray[imageColorFloatArrayOffset + 1];
		final float oldColorB = this.imageColorFloatArray[imageColorFloatArrayOffset + 2];
		
		final float newColorR = oldColorR <= 0.0031308F ? 12.92F * oldColorR : 1.055F * pow(oldColorR, 1.0F / 2.4F) - 0.055F;
		final float newColorG = oldColorG <= 0.0031308F ? 12.92F * oldColorG : 1.055F * pow(oldColorG, 1.0F / 2.4F) - 0.055F;
		final float newColorB = oldColorB <= 0.0031308F ? 12.92F * oldColorB : 1.055F * pow(oldColorB, 1.0F / 2.4F) - 0.055F;
		
		this.imageColorFloatArray[imageColorFloatArrayOffset + 0] = newColorR;
		this.imageColorFloatArray[imageColorFloatArrayOffset + 1] = newColorG;
		this.imageColorFloatArray[imageColorFloatArrayOffset + 2] = newColorB;
	}
	
	/**
	 * Sets the RGB component values {@code colorR}, {@code colorG} and {@code colorB} for the current pixel of the image.
	 * 
	 * @param colorR the value of the R-component
	 * @param colorG the value of the G-component
	 * @param colorB the value of the B-component
	 */
	protected final void imageSetColor(final float colorR, final float colorG, final float colorB) {
		final int imageColorFloatArrayOffset = getGlobalId() * 3;
		
		this.imageColorFloatArray[imageColorFloatArrayOffset + 0] = colorR;
		this.imageColorFloatArray[imageColorFloatArrayOffset + 1] = colorG;
		this.imageColorFloatArray[imageColorFloatArrayOffset + 2] = colorB;
	}
	
	/**
	 * Undoes the gamma correction for the current pixel of the image using the algorithm provided by PBRT.
	 * <p>
	 * This method assumes the RGB color for the current pixel is non-linear.
	 */
	protected final void imageUndoGammaCorrectionPBRT() {
		final int imageColorFloatArrayOffset = getGlobalId() * 3;
		
		final float oldColorR = this.imageColorFloatArray[imageColorFloatArrayOffset + 0];
		final float oldColorG = this.imageColorFloatArray[imageColorFloatArrayOffset + 1];
		final float oldColorB = this.imageColorFloatArray[imageColorFloatArrayOffset + 2];
		
		final float newColorR = oldColorR <= 0.04045F ? oldColorR * 1.0F / 12.92F : pow((oldColorR + 0.055F) * 1.0F / 1.055F, 2.4F);
		final float newColorG = oldColorG <= 0.04045F ? oldColorG * 1.0F / 12.92F : pow((oldColorG + 0.055F) * 1.0F / 1.055F, 2.4F);
		final float newColorB = oldColorB <= 0.04045F ? oldColorB * 1.0F / 12.92F : pow((oldColorB + 0.055F) * 1.0F / 1.055F, 2.4F);
		
		this.imageColorFloatArray[imageColorFloatArrayOffset + 0] = newColorR;
		this.imageColorFloatArray[imageColorFloatArrayOffset + 1] = newColorG;
		this.imageColorFloatArray[imageColorFloatArrayOffset + 2] = newColorB;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private void doSetupFilmColorFloatArray() {
		put(this.filmColorFloatArray = Floats.array(getResolution() * 3, 0.0F));
	}
	
	private void doSetupFilmSampleIntArray() {
		put(this.filmSampleIntArray = Ints.array(getResolution(), 0));
	}
	
	private void doSetupImageColorByteArray() {
		put(this.imageColorByteArray = Bytes.array(getResolution() * 4));
	}
	
	private void doSetupImageColorFloatArray() {
		put(this.imageColorFloatArray = Floats.array(getResolution() * 3, 0.0F));
	}
}