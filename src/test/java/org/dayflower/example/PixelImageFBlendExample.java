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

import org.dayflower.image.PixelImageF;

public class PixelImageFBlendExample {
	public static void main(String[] args) {
		PixelImageF pixelImageF0 = PixelImageF.load("Image-0.png");
		PixelImageF pixelImageF1 = PixelImageF.load("Image-1.png");
		
		PixelImageF pixelImageF = PixelImageF.blend(pixelImageF0, pixelImageF1, 0.5F);
		pixelImageF.save("PixelImageF-Blend-Example.png");
	}
}