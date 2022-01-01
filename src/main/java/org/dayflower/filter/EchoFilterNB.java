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
package org.dayflower.filter;

import java.lang.reflect.Field;//TODO: Add Unit Tests!

/**
 * An {@code EchoFilterNB} is an implementation of {@link FilterNB} that performs an echo effect.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class EchoFilterNB implements FilterNB {
	private final double decay;
	private final int delaySamples;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code EchoFilterNB} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new EchoFilter(0.6D, 11025);
	 * }
	 * </pre>
	 */
//	TODO: Add Unit Tests!
	public EchoFilterNB() {
		this(0.6D, 11025);
	}
	
	/**
	 * Constructs a new {@code EchoFilterNB} instance given its decay and delay samples.
	 * 
	 * @param decay the decay to use
	 * @param delaySamples the delay samples to use
	 */
//	TODO: Add Unit Tests!
	public EchoFilterNB(final double decay, final int delaySamples) {
		this.decay = decay;
		this.delaySamples = delaySamples;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Evaluates this {@code EchoFilterNB} instance given {@code bytes}.
	 * <p>
	 * Returns the evaluated value.
	 * <p>
	 * If {@code bytes} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param bytes the {@code byte[]}
	 * @return the evaluated value
	 * @throws NullPointerException thrown if, and only if, {@code bytes} are {@code null}
	 */
//	TODO: Add Unit Tests!
	@Override
	public byte[] evaluate(final byte[] bytes) {
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