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
package org.dayflower.test;

import org.dayflower.color.Color3F;
import org.dayflower.color.Color4F;
import org.dayflower.geometry.AngleF;
import org.dayflower.geometry.Point2I;
import org.dayflower.geometry.Vector2F;
import org.dayflower.geometry.shape.Rectangle2I;
import org.dayflower.image.ConvolutionKernel33F;
import org.dayflower.image.ConvolutionKernel55F;
import org.dayflower.image.ImageF;
import org.dayflower.image.IntImageF;

public final class Test {
	private Test() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static void main(final String[] args) {
		doTestImageF();
		doTestImageFBlendOver();
		doTestImageFConvolutionKernelBoxBlur();
		doTestImageFConvolutionKernelEdgeDetection();
		doTestImageFConvolutionKernelEmboss();
		doTestImageFConvolutionKernelGaussianBlur();
		doTestImageFConvolutionKernelGradientHorizontal();
		doTestImageFConvolutionKernelGradientVertical();
		doTestImageFConvolutionKernelSharpen();
		doTestImageFConvolutionKernelUnsharpMasking();
		doTestImageFCopy();
		doTestImageFFillRectangle();
		doTestImageFFillSimplexFractionalBrownianMotion();
		doTestImageFFlipX();
		doTestImageFFlipY();
		doTestImageFGradient();
		doTestImageFGrayscaleAverage();
		doTestImageFGrayscaleComponent1();
		doTestImageFGrayscaleComponent2();
		doTestImageFGrayscaleComponent3();
		doTestImageFGrayscaleLightness();
		doTestImageFGrayscaleLuminance();
		doTestImageFGrayscaleMaximum();
		doTestImageFGrayscaleMinimum();
		doTestImageFInvert();
		doTestImageFRotate();
		doTestImageFScaleBoth();
		doTestImageFScaleDown();
		doTestImageFScaleUp();
		doTestImageFSepia();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static ImageF doCreateImageFGradient(final int resolutionX, final int resolutionY) {
		final
		ImageF imageF = new IntImageF(resolutionX, resolutionY);
		imageF.fillGradient(Color3F.BLACK, Color3F.RED, Color3F.GREEN, Color3F.YELLOW);
		
		return imageF;
	}
	
	private static void doTestImageF() {
		final ImageF imageFBackground = new IntImageF(800, 800).fillSimplexFractionalBrownianMotion();
		final ImageF imageFOriginal = IntImageF.load("./generated/Test/Image-Original.jpg");
		final ImageF imageFOriginalScaled = imageFOriginal.scale(new Vector2F(0.25F, 0.25F));
		final ImageF imageF0 = imageFOriginalScaled.copy().transparency(0.5F).blendOver(doCreateImageFGradient(imageFOriginalScaled.getResolutionX(), imageFOriginalScaled.getResolutionY())).rotate(AngleF.negate(AngleF.degrees(20.0F)));
		final ImageF imageF1 = imageFOriginalScaled.copy().grayscaleAverage().rotate(AngleF.degrees(0.0F));
		final ImageF imageF2 = imageFOriginalScaled.copy().sepia().rotate(AngleF.degrees(20.0F));
		
		imageFBackground.fillImage(imageF0, new Point2I(50, 200));
		imageFBackground.fillImage(imageF1, new Point2I(imageFBackground.getResolutionX() / 2 - imageF1.getResolutionX() / 2, 200));
		imageFBackground.fillImage(imageF2, new Point2I(imageFBackground.getResolutionX() - 50 - imageF2.getResolutionX(), 200));
		imageFBackground.save("./generated/Test/Image.png");
	}
	
	private static void doTestImageFBlendOver() {
		final
		ImageF imageF = IntImageF.load("./generated/Test/Image-Original.jpg");
		imageF.transparency(0.5F);
		imageF.blendOver(doCreateImageFGradient(imageF.getResolutionX(), imageF.getResolutionY()));
		imageF.save("./generated/Test/Image-Blend-Over.png");
	}
	
	private static void doTestImageFConvolutionKernelBoxBlur() {
		final
		ImageF imageF = IntImageF.load("./generated/Test/Image-Original.jpg");
		imageF.multiply(ConvolutionKernel33F.BOX_BLUR);
		imageF.save("./generated/Test/Image-Convolution-Kernel-Box-Blur.png");
	}
	
	private static void doTestImageFConvolutionKernelEdgeDetection() {
		final
		ImageF imageF = IntImageF.load("./generated/Test/Image-Original.jpg");
		imageF.multiply(ConvolutionKernel33F.EDGE_DETECTION);
		imageF.save("./generated/Test/Image-Convolution-Kernel-Edge-Detection.png");
	}
	
	private static void doTestImageFConvolutionKernelEmboss() {
		final
		ImageF imageF = IntImageF.load("./generated/Test/Image-Original.jpg");
		imageF.multiply(ConvolutionKernel33F.EMBOSS);
		imageF.save("./generated/Test/Image-Convolution-Kernel-Emboss.png");
	}
	
	private static void doTestImageFConvolutionKernelGaussianBlur() {
		final
		ImageF imageF = IntImageF.load("./generated/Test/Image-Original.jpg");
		imageF.multiply(ConvolutionKernel55F.GAUSSIAN_BLUR);
		imageF.save("./generated/Test/Image-Convolution-Kernel-Gaussian-Blur.png");
	}
	
	private static void doTestImageFConvolutionKernelGradientHorizontal() {
		final
		ImageF imageF = IntImageF.load("./generated/Test/Image-Original.jpg");
		imageF.multiply(ConvolutionKernel33F.GRADIENT_HORIZONTAL);
		imageF.save("./generated/Test/Image-Convolution-Kernel-Gradient-Horizontal.png");
	}
	
	private static void doTestImageFConvolutionKernelGradientVertical() {
		final
		ImageF imageF = IntImageF.load("./generated/Test/Image-Original.jpg");
		imageF.multiply(ConvolutionKernel33F.GRADIENT_VERTICAL);
		imageF.save("./generated/Test/Image-Convolution-Kernel-Gradient-Vertical.png");
	}
	
	private static void doTestImageFConvolutionKernelSharpen() {
		final
		ImageF imageF = IntImageF.load("./generated/Test/Image-Original.jpg");
		imageF.multiply(ConvolutionKernel33F.SHARPEN);
		imageF.save("./generated/Test/Image-Convolution-Kernel-Sharpen.png");
	}
	
	private static void doTestImageFConvolutionKernelUnsharpMasking() {
		final
		ImageF imageF = IntImageF.load("./generated/Test/Image-Original.jpg");
		imageF.multiply(ConvolutionKernel55F.UNSHARP_MASKING);
		imageF.save("./generated/Test/Image-Convolution-Kernel-Unsharp-Masking.png");
	}
	
	private static void doTestImageFCopy() {
		final
		ImageF imageF = IntImageF.load("./generated/Test/Image-Original.jpg").copy(new Rectangle2I(new Point2I(100, 100), new Point2I(1080 - 100, 1256 - 100)));
		imageF.save("./generated/Test/Image-Copy.png");
	}
	
	private static void doTestImageFFillRectangle() {
		final
		ImageF imageF = IntImageF.load("./generated/Test/Image-Original.jpg");
		imageF.fillRectangle(new Rectangle2I(new Point2I(100, 100), new Point2I(imageF.getResolutionX() - 1 - 100, imageF.getResolutionY() - 1 - 100)), (color, point) -> Color4F.blendOver(new Color4F(1.0F, 0.0F, 0.0F, 0.5F), color));
		imageF.save("./generated/Test/Image-Fill-Rectangle.png");
	}
	
	private static void doTestImageFFillSimplexFractionalBrownianMotion() {
		final
		ImageF imageF = new IntImageF(800, 800);
		imageF.fillSimplexFractionalBrownianMotion();
		imageF.save("./generated/Test/Image-Fill-Simplex-Fractional-Brownian-Motion.png");
	}
	
	private static void doTestImageFFlipX() {
		final
		ImageF imageF = IntImageF.load("./generated/Test/Image-Original.jpg");
		imageF.flipX();
		imageF.save("./generated/Test/Image-Flip-X.png");
	}
	
	private static void doTestImageFFlipY() {
		final
		ImageF imageF = IntImageF.load("./generated/Test/Image-Original.jpg");
		imageF.flipY();
		imageF.save("./generated/Test/Image-Flip-Y.png");
	}
	
	private static void doTestImageFGradient() {
		final
		ImageF imageF = doCreateImageFGradient(800, 800);
		imageF.save("./generated/Test/Image-Gradient.png");
	}
	
	private static void doTestImageFGrayscaleAverage() {
		final
		ImageF imageF = IntImageF.load("./generated/Test/Image-Original.jpg");
		imageF.grayscaleAverage();
		imageF.save("./generated/Test/Image-Grayscale-Average.png");
	}
	
	private static void doTestImageFGrayscaleComponent1() {
		final
		ImageF imageF = IntImageF.load("./generated/Test/Image-Original.jpg");
		imageF.grayscaleComponent1();
		imageF.save("./generated/Test/Image-Grayscale-Component-1.png");
	}
	
	private static void doTestImageFGrayscaleComponent2() {
		final
		ImageF imageF = IntImageF.load("./generated/Test/Image-Original.jpg");
		imageF.grayscaleComponent2();
		imageF.save("./generated/Test/Image-Grayscale-Component-2.png");
	}
	
	private static void doTestImageFGrayscaleComponent3() {
		final
		ImageF imageF = IntImageF.load("./generated/Test/Image-Original.jpg");
		imageF.grayscaleComponent3();
		imageF.save("./generated/Test/Image-Grayscale-Component-3.png");
	}
	
	private static void doTestImageFGrayscaleLightness() {
		final
		ImageF imageF = IntImageF.load("./generated/Test/Image-Original.jpg");
		imageF.grayscaleLightness();
		imageF.save("./generated/Test/Image-Grayscale-Lightness.png");
	}
	
	private static void doTestImageFGrayscaleLuminance() {
		final
		ImageF imageF = IntImageF.load("./generated/Test/Image-Original.jpg");
		imageF.grayscaleLuminance();
		imageF.save("./generated/Test/Image-Grayscale-Luminance.png");
	}
	
	private static void doTestImageFGrayscaleMaximum() {
		final
		ImageF imageF = IntImageF.load("./generated/Test/Image-Original.jpg");
		imageF.grayscaleMaximum();
		imageF.save("./generated/Test/Image-Grayscale-Maximum.png");
	}
	
	private static void doTestImageFGrayscaleMinimum() {
		final
		ImageF imageF = IntImageF.load("./generated/Test/Image-Original.jpg");
		imageF.grayscaleMinimum();
		imageF.save("./generated/Test/Image-Grayscale-Minimum.png");
	}
	
	private static void doTestImageFInvert() {
		final
		ImageF imageF = IntImageF.load("./generated/Test/Image-Original.jpg");
		imageF.invert();
		imageF.save("./generated/Test/Image-Invert.png");
	}
	
	private static void doTestImageFRotate() {
		final
		ImageF imageF = IntImageF.load("./generated/Test/Image-Original.jpg").rotate(AngleF.degrees(45.0F));
		imageF.save("./generated/Test/Image-Rotate.png");
	}
	
	private static void doTestImageFScaleBoth() {
		final
		ImageF imageF = IntImageF.load("./generated/Test/Image-Original.jpg").scale(new Vector2F(0.5F, 2.0F));
		imageF.save("./generated/Test/Image-Scale-Both.png");
	}
	
	private static void doTestImageFScaleDown() {
		final
		ImageF imageF = IntImageF.load("./generated/Test/Image-Original.jpg").scale(new Vector2F(0.5F, 0.5F));
		imageF.save("./generated/Test/Image-Scale-Down.png");
	}
	
	private static void doTestImageFScaleUp() {
		final
		ImageF imageF = IntImageF.load("./generated/Test/Image-Original.jpg").scale(new Vector2F(2.0F, 2.0F));
		imageF.save("./generated/Test/Image-Scale-Up.png");
	}
	
	private static void doTestImageFSepia() {
		final
		ImageF imageF = IntImageF.load("./generated/Test/Image-Original.jpg");
		imageF.sepia();
		imageF.save("./generated/Test/Image-Sepia.png");
	}
}