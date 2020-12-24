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
package org.dayflower.sound;

/**
 * A {@code ToneSound} is an implementation of {@link Sound} that provides sound data by a given tone.
 * <p>
 * This class is immutable and therefore also thread-safe.
 * <p>
 * To use this class, consider the following example:
 * <pre>
 * {@code
 * double frequency = 110.0D;
 * 
 * long duration = 1000L;
 * 
 * Sound sound = new ToneSound(frequency, duration);
 * 
 * SoundPlayer soundPlayer = new SoundPlayer();
 * soundPlayer.play(sound);
 * soundPlayer.stop();
 * }
 * </pre>
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class ToneSound implements Sound {
	private static final float SAMPLE_RATE = 44100.0F;
	private static final int SAMPLE_SIZE_IN_BITS = 16;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final byte[] bytes;
	private final double frequency;
	private final long duration;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code ToneSound} instance.
	 * 
	 * @param frequency the frequency of this {@code ToneSound} instance in Hz
	 * @param duration the duration of this {@code ToneSound} instance in milliseconds
	 */
	public ToneSound(final double frequency, final long duration) {
		this.bytes = doToBytes(frequency, duration);
		this.frequency = frequency;
		this.duration = duration;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code byte} array with sound data.
	 * <p>
	 * Modifying the returned {@code byte} array will not affect this {@code ToneSound} instance.
	 * 
	 * @return a {@code byte} array with sound data
	 */
	@Override
	public byte[] toBytes() {
		return this.bytes.clone();
	}
	
	/**
	 * Returns the frequency in Hz.
	 * 
	 * @return the frequency in Hz
	 */
	public double getFrequency() {
		return this.frequency;
	}
	
	/**
	 * Returns the duration in milliseconds.
	 * 
	 * @return the duration in milliseconds
	 */
	public long getDuration() {
		return this.duration;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static byte[] doToBytes(final double frequency, final long duration) {
		int length = (int)((duration / 1000.0D) * SAMPLE_RATE * SAMPLE_SIZE_IN_BITS / 8.0D);
		
		if(length % 2 != 0) {
			length++;
		}
		
		final byte[] bytes = new byte[length];
		
		for(int i = 0; i < bytes.length; i++) {
			final double angle = i / (SAMPLE_RATE / frequency) * 2.0D * Math.PI;
			
			bytes[i] = (byte)(Math.sin(angle) * 110.0D);
		}
		
		return bytes;
	}
}