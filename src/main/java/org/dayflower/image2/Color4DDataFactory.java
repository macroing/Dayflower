/**
 * Copyright 2014 - 2022 J&#246;rgen Lundgren
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
package org.dayflower.image2;

import java.awt.image.BufferedImage;

import org.dayflower.color.Color4D;

final class Color4DDataFactory extends DataFactory {
	public Color4DDataFactory() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public Data create(final BufferedImage bufferedImage) {
		return new Color4DData(bufferedImage);
	}
	
	@Override
	public Data create(final int resolutionX, final int resolutionY) {
		return new Color4DData(resolutionX, resolutionY);
	}
	
	@Override
	public Data create(final int resolutionX, final int resolutionY, final Color4D color) {
		return new Color4DData(resolutionX, resolutionY, color);
	}
}