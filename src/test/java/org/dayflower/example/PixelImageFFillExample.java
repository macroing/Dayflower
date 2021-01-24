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
package org.dayflower.example;

import org.dayflower.color.Color4F;
import org.dayflower.geometry.Point2I;
import org.dayflower.geometry.shape.Circle2I;
import org.dayflower.geometry.shape.Rectangle2I;
import org.dayflower.image.ImageF;
import org.dayflower.image.PixelImageF;

public class PixelImageFFillExample {
	public static void main(String[] args) {
		ImageF imageFRandom = PixelImageF.random(50, 50);
		ImageF imageF = new PixelImageF(150, 150);
		imageF.fillCircle(new Circle2I(new Point2I(75, 75), 50), Color4F.RED);
		imageF.fillImage(imageFRandom, imageFRandom.getBounds(), new Rectangle2I(new Point2I(50, 50), new Point2I(100, 100)));
		imageF.save("PixelImageF-Fill-Example.png");
	}
}