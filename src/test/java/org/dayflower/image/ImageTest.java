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
package org.dayflower.image;

import org.dayflower.color.Color3F;
import org.dayflower.color.Color4F;
import org.dayflower.geometry.Point2I;
import org.dayflower.geometry.shape.Circle2I;
import org.dayflower.geometry.shape.Line2I;
import org.dayflower.geometry.shape.Rectangle2I;
import org.dayflower.geometry.shape.Triangle2I;
import org.dayflower.sampler.NRooksSampler;
import org.dayflower.sampler.RandomSampler;
import org.dayflower.sampler.Sample2F;
import org.dayflower.sampler.Sampler;

public final class ImageTest {
	private ImageTest() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static void main(final String[] args) {
		doTestPixelImageDrawCircle();
		doTestPixelImageDrawLine();
		doTestPixelImageDrawRectangle();
		doTestPixelImageDrawTriangle();
		doTestPixelImageFillCircle();
		doTestPixelImageFillPixelImage();
		doTestPixelImageFillRectangle();
		doTestPixelImageFillTriangle();
		doTestPixelImageMultiplyConvolutionKernel33F();
		doTestPixelImageMultiplyConvolutionKernel55F();
		doTestPixelImageSamplerNRooksSampler();
		doTestPixelImageSamplerRandomSampler();
		doTestPixelImageScreenCapture();
		doTestPixelImageSave();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	static void doTestPixelImageDrawCircle() {
		final
		PixelImageF pixelImageF = new PixelImageF(800, 800, Color4F.WHITE);
		pixelImageF.drawCircle(new Circle2I(new Point2I(60, 60), 40));
		pixelImageF.save("./generated/doTestImageDrawCircle.png");
	}
	
	static void doTestPixelImageDrawLine() {
		final
		PixelImageF pixelImageF = new PixelImageF(800, 800, Color4F.WHITE);
		pixelImageF.drawLine(new Line2I(new Point2I(20, 20), new Point2I(100, 100)));
		pixelImageF.save("./generated/doTestImageDrawLine.png");
	}
	
	static void doTestPixelImageDrawRectangle() {
		final
		PixelImageF pixelImageF = new PixelImageF(800, 800, Color4F.WHITE);
		pixelImageF.drawRectangle(new Rectangle2I(new Point2I(20, 20), new Point2I(100, 100)));
		pixelImageF.save("./generated/doTestImageDrawRectangle.png");
	}
	
	static void doTestPixelImageDrawTriangle() {
		final
		PixelImageF pixelImageF = new PixelImageF(800, 800, Color4F.WHITE);
		pixelImageF.drawTriangle(new Triangle2I(new Point2I(60, 20), new Point2I(100, 100), new Point2I(20, 100)));
		pixelImageF.save("./generated/doTestImageDrawTriangle.png");
	}
	
	static void doTestPixelImageFillCircle() {
		final
		PixelImageF pixelImageF = new PixelImageF(800, 800, Color4F.WHITE);
		pixelImageF.fillCircle(new Circle2I(new Point2I(60, 60), 40));
		pixelImageF.save("./generated/doTestImageFillCircle.png");
	}
	
	static void doTestPixelImageFillPixelImage() {
		final
		PixelImageF pixelImageF = new PixelImageF(800, 800, Color4F.WHITE);
		pixelImageF.fillPixelImage(PixelImageF.random(100, 100), new Rectangle2I(new Point2I(), new Point2I(100, 100)), new Rectangle2I(new Point2I(100, 100), new Point2I(200, 200)));
		pixelImageF.save("./generated/doTestImageFillImage.png");
	}
	
	static void doTestPixelImageFillRectangle() {
		final
		PixelImageF pixelImageF = new PixelImageF(800, 800, Color4F.WHITE);
		pixelImageF.fillRectangle(new Rectangle2I(new Point2I(20, 20), new Point2I(100, 100)));
		pixelImageF.save("./generated/doTestImageFillRectangle.png");
	}
	
	static void doTestPixelImageFillTriangle() {
		final
		PixelImageF pixelImageF = new PixelImageF(800, 800, Color4F.WHITE);
		pixelImageF.fillTriangle(new Triangle2I(new Point2I(60, 20), new Point2I(100, 100), new Point2I(20, 100)));
		pixelImageF.save("./generated/doTestImageFillTriangle.png");
	}
	
	static void doTestPixelImageMultiplyConvolutionKernel33F() {
		final
		PixelImageF pixelImageF = PixelImageF.createScreenCapture(new Rectangle2I(new Point2I(100, 100), new Point2I(200, 200)));
		pixelImageF.multiply(ConvolutionKernel33F.GAUSSIAN_BLUR);
		pixelImageF.save("./generated/doTestImageMultiplyConvolutionKernel33F.png");
	}
	
	static void doTestPixelImageMultiplyConvolutionKernel55F() {
		final
		PixelImageF pixelImageF = PixelImageF.createScreenCapture(new Rectangle2I(new Point2I(100, 100), new Point2I(200, 200)));
		pixelImageF.multiply(ConvolutionKernel55F.GAUSSIAN_BLUR);
		pixelImageF.save("./generated/doTestImageMultiplyConvolutionKernel55F.png");
	}
	
	static void doTestPixelImageSamplerNRooksSampler() {
		final PixelImageF pixelImage = new PixelImageF(800, 800, Color4F.WHITE);
		
		final Sampler sampler = new NRooksSampler();
		
		for(int y = 0; y < pixelImage.getResolutionY(); y += 8) {
			for(int x = 0; x < pixelImage.getResolutionX(); x += 8) {
				final Sample2F sample = sampler.sample2();
				
				final int index = (int)(sample.getY() * 8 + y) * pixelImage.getResolutionX() + (int)(sample.getX() * 8 + x);
				
				pixelImage.setColorRGB(Color3F.BLACK, index);
			}
		}
		
		pixelImage.save("./generated/NRooksSampler.png");
	}
	
	static void doTestPixelImageSamplerRandomSampler() {
		final PixelImageF pixelImage = new PixelImageF(800, 800, Color4F.WHITE);
		
		final Sampler sampler = new RandomSampler();
		
		for(int y = 0; y < pixelImage.getResolutionY(); y += 8) {
			for(int x = 0; x < pixelImage.getResolutionX(); x += 8) {
				final Sample2F sample = sampler.sample2();
				
				final int index = (int)(sample.getY() * 8 + y) * pixelImage.getResolutionX() + (int)(sample.getX() * 8 + x);
				
				pixelImage.setColorRGB(Color3F.BLACK, index);
			}
		}
		
		pixelImage.save("./generated/RandomSampler.png");
	}
	
	static void doTestPixelImageScreenCapture() {
		final
		PixelImageF pixelImageF = PixelImageF.createScreenCapture(new Rectangle2I(new Point2I(100, 100), new Point2I(200, 200)));
		pixelImageF.save("./generated/doTestImageScreenCapture.png");
	}
	
	static void doTestPixelImageSave() {
		final
		PixelImageF pixelImageF = PixelImageF.random();
		pixelImageF.save("./generated/doTestImageSave.png");
	}
}