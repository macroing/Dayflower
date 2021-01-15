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

import java.util.concurrent.ThreadLocalRandom;

import org.dayflower.util.CircularBuffer;

/**
 * A {@code GuitarSound} is an implementation of {@link Sound} that simulates the sound from a guitar.
 * <p>
 * The algorithm used to emulate the sound of a guitar is called Karplus-Strong string synthesis.
 * <p>
 * This class is not thread-safe.
 * <p>
 * To use this class, consider the following example:
 * <pre>
 * {@code
 * GuitarSound guitarSound = new GuitarSound();
 * guitarSound.pluckGuitarStringA2();
 * 
 * SoundPlayer soundPlayer = new SoundPlayer();
 * soundPlayer.play(guitarSound);
 * soundPlayer.stop();
 * }
 * </pre>
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class GuitarSound implements Sound {
	private static final double A2 = 110.0D;
	private static final double B3 = 246.9D;
	private static final double D3 = 146.8D;
	private static final double E2 = 82.41D;
	private static final double E4 = 329.6D;
	private static final double G3 = 196.0D;
	private static final float SAMPLE_RATE = 44100.0F;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final GuitarString guitarStringA2;
	private final GuitarString guitarStringB3;
	private final GuitarString guitarStringD3;
	private final GuitarString guitarStringE2;
	private final GuitarString guitarStringE4;
	private final GuitarString guitarStringG3;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code GuitarSound} instance.
	 */
	public GuitarSound() {
		this.guitarStringA2 = new GuitarString(A2);
		this.guitarStringB3 = new GuitarString(B3);
		this.guitarStringD3 = new GuitarString(D3);
		this.guitarStringE2 = new GuitarString(E2);
		this.guitarStringE4 = new GuitarString(E4);
		this.guitarStringG3 = new GuitarString(G3);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns {@code true} if, and only if, this {@code GuitarSound} instance has been plucked, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, this {@code GuitarSound} instance has been plucked, {@code false} otherwise
	 */
	public boolean hasPlucked() {
		return this.guitarStringA2.hasPlucked() || this.guitarStringB3.hasPlucked() || this.guitarStringD3.hasPlucked() || this.guitarStringE2.hasPlucked() || this.guitarStringE4.hasPlucked() || this.guitarStringG3.hasPlucked();
	}
	
	/**
	 * Returns a {@code byte} array with sound data.
	 * <p>
	 * Modifying the returned {@code byte} array will not affect this {@code GuitarSound} instance.
	 * 
	 * @return a {@code byte} array with sound data
	 */
	@Override
	public byte[] toBytes() {
		if(!hasPlucked()) {
			return new byte[0];
		}
		
		final double duration = 5.0D;
		
		final int length = (int)(SAMPLE_RATE * duration) + 1;
		
		final double[] samples = new double[length];
		
		for(int i = 0; i < samples.length; i++) {
			this.guitarStringA2.update();
			this.guitarStringB3.update();
			this.guitarStringD3.update();
			this.guitarStringE2.update();
			this.guitarStringE4.update();
			this.guitarStringG3.update();
			
			double sample = 0.0D;
			
			sample += this.guitarStringA2.getSample();
			sample += this.guitarStringB3.getSample();
			sample += this.guitarStringD3.getSample();
			sample += this.guitarStringE2.getSample();
			sample += this.guitarStringE4.getSample();
			sample += this.guitarStringG3.getSample();
			
			samples[i] = sample;
		}
		
		final byte[] bytes = new byte[samples.length * 2];
		
		for(int i = 0, j = 0; i < samples.length; i++, j += 2) {
			final short value = (short)(Short.MAX_VALUE * samples[i]);
			
			bytes[j + 0] = (byte)(value >> 0);
			bytes[j + 1] = (byte)(value >> 8);
		}
		
		return bytes;
	}
	
	/**
	 * Increases the frequency of this {@code GuitarSound} instance.
	 */
	public void higher() {
		this.guitarStringA2.setFrequency(this.guitarStringA2.getFrequency() * 2);
		this.guitarStringB3.setFrequency(this.guitarStringB3.getFrequency() * 2);
		this.guitarStringD3.setFrequency(this.guitarStringD3.getFrequency() * 2);
		this.guitarStringE2.setFrequency(this.guitarStringE2.getFrequency() * 2);
		this.guitarStringE4.setFrequency(this.guitarStringE4.getFrequency() * 2);
		this.guitarStringG3.setFrequency(this.guitarStringG3.getFrequency() * 2);
	}
	
	/**
	 * Decreases the frequency of this {@code GuitarSound} instance.
	 */
	public void lower() {
		this.guitarStringA2.setFrequency(this.guitarStringA2.getFrequency() / 2);
		this.guitarStringB3.setFrequency(this.guitarStringB3.getFrequency() / 2);
		this.guitarStringD3.setFrequency(this.guitarStringD3.getFrequency() / 2);
		this.guitarStringE2.setFrequency(this.guitarStringE2.getFrequency() / 2);
		this.guitarStringE4.setFrequency(this.guitarStringE4.getFrequency() / 2);
		this.guitarStringG3.setFrequency(this.guitarStringG3.getFrequency() / 2);
	}
	
	/**
	 * Plucks the string usually called A2 of this {@code GuitarSound} instance.
	 */
	public void pluckGuitarStringA2() {
		this.guitarStringA2.pluck();
	}
	
	/**
	 * Plucks the string usually called B3 of this {@code GuitarSound} instance.
	 */
	public void pluckGuitarStringB3() {
		this.guitarStringB3.pluck();
	}
	
	/**
	 * Plucks the string usually called D3 of this {@code GuitarSound} instance.
	 */
	public void pluckGuitarStringD3() {
		this.guitarStringD3.pluck();
	}
	
	/**
	 * Plucks the string usually called E2 of this {@code GuitarSound} instance.
	 */
	public void pluckGuitarStringE2() {
		this.guitarStringE2.pluck();
	}
	
	/**
	 * Plucks the string usually called E4 of this {@code GuitarSound} instance.
	 */
	public void pluckGuitarStringE4() {
		this.guitarStringE4.pluck();
	}
	
	/**
	 * Plucks the string usually called G3 of this {@code GuitarSound} instance.
	 */
	public void pluckGuitarStringG3() {
		this.guitarStringG3.pluck();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static final class GuitarString {
		private static final double DECAY_FACTOR = 0.994D;
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		private CircularBuffer circularBuffer;
		private boolean hasPlucked;
		private double frequency;
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		public GuitarString(final double frequency) {
			setFrequency(frequency);
		}
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		public boolean hasPlucked() {
			return this.hasPlucked;
		}
		
		public double getFrequency() {
			return this.frequency;
		}
		
		public double getSample() {
			if(this.circularBuffer.isEmpty()) {
				this.hasPlucked = false;
				
				return 0.0D;
			}
			
			return this.circularBuffer.peek();
		}
		
		public void pluck() {
			this.hasPlucked = true;
			
			while(!this.circularBuffer.isEmpty()) {
				this.circularBuffer.pop();
			}
			
			while(!this.circularBuffer.isFull()) {
				final double x = ThreadLocalRandom.current().nextDouble() - 0.5D;
				
				this.circularBuffer.push(x);
			}
		}
		
		public void setFrequency(final double frequency) {
			this.circularBuffer = new CircularBuffer((int)(Math.round(SAMPLE_RATE / frequency)));
			this.frequency = frequency;
		}
		
		public void update() {
			if(!this.circularBuffer.isEmpty()) {
				final double x = this.circularBuffer.pop();
				
				if(!this.circularBuffer.isEmpty()) {
					final double y = this.circularBuffer.peek();
					final double z = (x + y) / 2.0D * DECAY_FACTOR;
					
					this.circularBuffer.push(z);
				}
			}
		}
	}
}