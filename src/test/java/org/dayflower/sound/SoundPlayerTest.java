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
package org.dayflower.sound;

import org.dayflower.filter.EchoFilterNB;
import org.dayflower.filter.FilterNB;

public final class SoundPlayerTest {
	private SoundPlayerTest() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static void main(final String[] args) {
		final
		GuitarSound guitarSound = new GuitarSound();
		guitarSound.pluckGuitarStringA2();
		
		final FilterNB filter = new EchoFilterNB(0.6D, 11025);
		
		final
		SoundPlayer soundPlayer = new SoundPlayer();
//		soundPlayer.play(guitarSound);
		soundPlayer.play(guitarSound, filter);
		soundPlayer.stop();
	}
}