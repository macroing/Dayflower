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

/**
 * An {@code EchoFilter} is an implementation of {@link Filter} that performs an echo effect.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class EchoFilter implements Filter {
	private final double decay;
	private final int delaySamples;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code EchoFilter} instance.
	 * <p>
	 * Calling this constructor is equivalent to calling {@code new EchoFilter(0.6D, 11025)}.
	 */
	public EchoFilter() {
		this(0.6D, 11025);
	}
	
	/**
	 * Constructs a new {@code EchoFilter} instance given its decay and delay samples.
	 * 
	 * @param decay the decay to use
	 * @param delaySamples the delay samples to use
	 */
	public EchoFilter(final double decay, final int delaySamples) {
		this.decay = decay;
		this.delaySamples = delaySamples;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Performs filtering on the samples in {@code bytes}.
	 * <p>
	 * Returns a {@code byte} array with the filtered samples.
	 * <p>
	 * If {@code bytes} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param bytes the {@code byte} array with samples to filter
	 * @return a {@code byte} array with the filtered samples
	 * @throws NullPointerException thrown if, and only if, {@code bytes} are {@code null}
	 */
	@Override
	public byte[] filter(final byte[] bytes) {
		final byte[] bytes0 = bytes.clone();
		
		final short[] delayBuffer = new short[this.delaySamples];
		
		for(int i = 0, j = 0; i < bytes0.length; i += 2, j = (j + 1) % delayBuffer.length) {
			final short oldSample = (short)(((bytes0[i + 1] & 0xFF) << 8) | (bytes0[i] & 0xFF));
			final short newSample = (short)(oldSample + this.decay * delayBuffer[j]);
			
			bytes0[i + 0] = (byte)((newSample >> 0) & 0xFF);
			bytes0[i + 1] = (byte)((newSample >> 8) & 0xFF);
			
			delayBuffer[j] = newSample;
		}
		
		return bytes0;
	}
}