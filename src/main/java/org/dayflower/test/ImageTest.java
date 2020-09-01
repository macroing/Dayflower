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
import org.dayflower.image.Image;

public final class ImageTest {
	private ImageTest() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static void main(final String[] args) {
//		doTestImageDrawLine();
//		doTestImageDrawRectangle();
//		doTestImageDrawTriangle();
//		doTestImageFillCircle();
		doTestImageFillImage();
//		doTestImageFillRectangle();
//		doTestImageFillTriangle();
//		doTestImageSave();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
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
	
	static void doTestImageSave() {
		final
		Image image = Image.random();
		image.save("./generated/doTestImageSave.png");
	}
}