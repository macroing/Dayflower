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

import org.dayflower.geometry.Point2I;
import org.dayflower.geometry.shape.Circle2I;
import org.dayflower.geometry.shape.Rectangle2I;
import org.dayflower.image.Color3F;
import org.dayflower.image.PixelImageF;

public class FillExample {
	public static void main(String[] args) {
		PixelImageF pixelImageF0 = PixelImageF.random(50, 50);
		
		PixelImageF pixelImageF = new PixelImageF(150, 150);
		pixelImageF.fillCircle(new Circle2I(new Point2I(75, 75), 50), Color3F.RED);
		pixelImageF.fillPixelImage(pixelImageF0, pixelImageF0.getBounds(), new Rectangle2I(new Point2I(50, 50), new Point2I(100, 100)));
		pixelImageF.save("Fill-Example.png");
	}
}