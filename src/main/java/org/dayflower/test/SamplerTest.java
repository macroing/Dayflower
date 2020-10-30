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

import org.dayflower.image.Color3F;
import org.dayflower.image.Image;
import org.dayflower.sampler.NRooksSampler;
import org.dayflower.sampler.RandomSampler;
import org.dayflower.sampler.Sample2F;
import org.dayflower.sampler.Sampler;

public final class SamplerTest {
	private SamplerTest() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static void main(final String[] args) {
		doTestNRooksSampler();
		doTestRandomSampler();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static void doTestNRooksSampler() {
		doTestSampler(new NRooksSampler(), "./generated/NRooksSampler.png");
	}
	
	private static void doTestRandomSampler() {
		doTestSampler(new RandomSampler(), "./generated/RandomSampler.png");
	}
	
	private static void doTestSampler(final Sampler sampler, final String pathname) {
		final Image image = new Image(800, 800);
		
		for(int y = 0; y < image.getResolutionY(); y += 8) {
			for(int x = 0; x < image.getResolutionX(); x += 8) {
				final Sample2F sample = sampler.sample2();
				
				final int index = (int)(sample.getY() * 8 + y) * image.getResolutionX() + (int)(sample.getX() * 8 + x);
				
				image.setColorRGB(Color3F.RED, index);
			}
		}
		
		image.save(pathname);
	}
}