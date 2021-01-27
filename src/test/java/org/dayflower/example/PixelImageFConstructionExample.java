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
import org.dayflower.filter.MitchellFilter2F;
import org.dayflower.image.PixelImageF;

public class PixelImageFConstructionExample {
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		PixelImageF pixelImageF0 = new PixelImageF(800, 800, Color4F.WHITE, new MitchellFilter2F());
		PixelImageF pixelImageF1 = new PixelImageF(pixelImageF0);
		PixelImageF pixelImageF2 = new PixelImageF(800, 800, Color4F.arrayRead(pixelImageF1.toIntArray()));
	}
}