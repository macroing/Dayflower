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
package org.dayflower.test;

import org.dayflower.geometry.Circle2I;
import org.dayflower.geometry.Line2I;
import org.dayflower.geometry.Point2I;
import org.dayflower.geometry.Rectangle2I;
import org.dayflower.geometry.Triangle2I;
import org.dayflower.image.Color3F;
import org.dayflower.image.ConvolutionKernel33F;
import org.dayflower.image.ConvolutionKernel55F;
import org.dayflower.image.Image;
import org.dayflower.image.IrregularSpectralCurve;

public final class ImageTest {
	private ImageTest() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static void main(final String[] args) {
		doTestImageCopper();
//		doTestImageDifference();
//		doTestImageDrawCircle();
//		doTestImageDrawLine();
//		doTestImageDrawRectangle();
//		doTestImageDrawTriangle();
//		doTestImageFillCircle();
//		doTestImageFillImage();
//		doTestImageFillRectangle();
//		doTestImageFillTriangle();
//		doTestImageMultiplyConvolutionKernel33F();
//		doTestImageMultiplyConvolutionKernel55F();
//		doTestImageSave();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	static void doTestImageCopper() {
		final float[] wavelengths = new float[] {
			298.7570554F, 302.4004341F, 306.13377280F, 309.9604450F, 313.8839949F,
			317.9081487F, 322.0368260F, 326.27415260F, 330.6244747F, 335.0923730F,
			339.6826795F, 344.4004944F, 349.25120560F, 354.2405086F, 359.3744290F,
			364.6593471F, 370.1020239F, 375.70963030F, 381.4897785F, 387.4505563F,
			393.6005651F, 399.9489613F, 406.50550160F, 413.2805933F, 420.2853492F,
			427.5316483F, 435.0322035F, 442.80063570F, 450.8515564F, 459.2006593F,
			467.8648226F, 476.8622231F, 486.21246270F, 495.9367120F, 506.0578694F,
			516.6007417F, 527.5922468F, 539.06164350F, 551.0407911F, 563.5644455F,
			576.6705953F, 590.4008476F, 604.80086830F, 619.9208900F, 635.8162974F,
			652.5483053F, 670.1847459F, 688.80098890F, 708.4810171F, 729.3186941F,
			751.4192606F, 774.9011125F, 799.89792260F, 826.5611867F, 855.0632966F,
			885.6012714F
		};
		
		final float[] n = new float[] {
			1.400313F, 1.380F, 1.358438F, 1.340F, 1.329063F, 1.325F, 1.332500F, 1.340F,
			1.334375F, 1.325F, 1.317812F, 1.310F, 1.300313F, 1.290F, 1.281563F, 1.270F,
			1.249062F, 1.225F, 1.200000F, 1.180F, 1.174375F, 1.175F, 1.177500F, 1.180F,
			1.178125F, 1.175F, 1.172812F, 1.170F, 1.165312F, 1.160F, 1.155312F, 1.150F,
			1.142812F, 1.135F, 1.131562F, 1.120F, 1.092437F, 1.040F, 0.950375F, 0.826F,
			0.645875F, 0.468F, 0.351250F, 0.272F, 0.230813F, 0.214F, 0.209250F, 0.213F,
			0.216250F, 0.223F, 0.236500F, 0.250F, 0.254188F, 0.260F, 0.280000F, 0.300F
		};
		
		final float[] k = new float[] {
			1.662125F, 1.687F, 1.703313F, 1.720F, 1.744563F, 1.770F, 1.791625F, 1.810F,
			1.822125F, 1.834F, 1.851750F, 1.872F, 1.894250F, 1.916F, 1.931688F, 1.950F,
			1.972438F, 2.015F, 2.121562F, 2.210F, 2.177188F, 2.130F, 2.160063F, 2.210F,
			2.249938F, 2.289F, 2.326000F, 2.362F, 2.397625F, 2.433F, 2.469187F, 2.504F,
			2.535875F, 2.564F, 2.589625F, 2.605F, 2.595562F, 2.583F, 2.576500F, 2.599F,
			2.678062F, 2.809F, 3.010750F, 3.240F, 3.458187F, 3.670F, 3.863125F, 4.050F,
			4.239563F, 4.430F, 4.619563F, 4.817F, 5.034125F, 5.260F, 5.485625F, 5.717F
		};
		
		final Color3F colorCopperN = Color3F.maximumTo1(Color3F.convertXYZToRGBUsingSRGB(new IrregularSpectralCurve(n, wavelengths).toColorXYZ()));
		final Color3F colorCopperK = Color3F.maximumTo1(Color3F.convertXYZToRGBUsingSRGB(new IrregularSpectralCurve(k, wavelengths).toColorXYZ()));
		
		System.out.println(colorCopperN);
		System.out.println(colorCopperK);
		
		final
		Image image = new Image(800, 800, colorCopperN);
		image.save("./generated/doTestImageCopper.png");
	}
	
	static void doTestImageDifference() {
		final
		Image image = Image.difference(Image.load("./generated/PathTracer-ShowcaseMaterialLambertianMaterial.png"), Image.load("./generated/PathTracer-ShowcaseMaterialOrenNayarMaterial.png"));
		image.save("./generated/doTestImageDifference.png");
	}
	
	static void doTestImageDrawCircle() {
		final
		Image image = new Image(800, 800, Color3F.WHITE);
		image.drawCircle(new Circle2I(new Point2I(60, 60), 40));
		image.save("./generated/doTestImageDrawCircle.png");
	}
	
	static void doTestImageDrawLine() {
		final
		Image image = new Image(800, 800, Color3F.WHITE);
		image.drawLine(new Line2I(new Point2I(20, 20), new Point2I(100, 100)));
		image.save("./generated/doTestImageDrawLine.png");
	}
	
	static void doTestImageDrawRectangle() {
		final
		Image image = new Image(800, 800, Color3F.WHITE);
		image.drawRectangle(new Rectangle2I(new Point2I(20, 20), new Point2I(100, 100)));
		image.save("./generated/doTestImageDrawRectangle.png");
	}
	
	static void doTestImageDrawTriangle() {
		final
		Image image = new Image(800, 800, Color3F.WHITE);
		image.drawTriangle(new Triangle2I(new Point2I(60, 20), new Point2I(100, 100), new Point2I(20, 100)));
		image.save("./generated/doTestImageDrawTriangle.png");
	}
	
	static void doTestImageFillCircle() {
		final
		Image image = new Image(800, 800, Color3F.WHITE);
		image.fillCircle(new Circle2I(new Point2I(60, 60), 40));
		image.save("./generated/doTestImageFillCircle.png");
	}
	
	static void doTestImageFillImage() {
		final
		Image image = new Image(800, 800, Color3F.WHITE);
		image.fillImage(Image.random(100, 100), new Rectangle2I(new Point2I(), new Point2I(100, 100)), new Rectangle2I(new Point2I(100, 100), new Point2I(200, 200)));
		image.save("./generated/doTestImageFillImage.png");
	}
	
	static void doTestImageFillRectangle() {
		final
		Image image = new Image(800, 800, Color3F.WHITE);
		image.fillRectangle(new Rectangle2I(new Point2I(20, 20), new Point2I(100, 100)));
		image.save("./generated/doTestImageFillRectangle.png");
	}
	
	static void doTestImageFillTriangle() {
		final
		Image image = new Image(800, 800, Color3F.WHITE);
		image.fillTriangle(new Triangle2I(new Point2I(60, 20), new Point2I(100, 100), new Point2I(20, 100)));
		image.save("./generated/doTestImageFillTriangle.png");
	}
	
	static void doTestImageMultiplyConvolutionKernel33F() {
		final
		Image image = Image.load("./generated/Image.jpg");
		image.multiply(ConvolutionKernel33F.GAUSSIAN_BLUR);
		image.save("./generated/doTestImageMultiplyConvolutionKernel33F.png");
	}
	
	static void doTestImageMultiplyConvolutionKernel55F() {
		final
		Image image = Image.load("./generated/Image.jpg");
		image.multiply(ConvolutionKernel55F.GAUSSIAN_BLUR);
		image.save("./generated/doTestImageMultiplyConvolutionKernel55F.png");
	}
	
	static void doTestImageSave() {
		final
		Image image = Image.random();
		image.save("./generated/doTestImageSave.png");
	}
}