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

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Line;
import javax.sound.sampled.SourceDataLine;

import org.dayflower.filter.FilterNB;

/**
 * A {@code SoundPlayer} is used to play sound by giving it an instance of {@link Sound}.
 * <p>
 * This class is not thread-safe.
 * <p>
 * To use this class, consider the following example:
 * <pre>
 * {@code
 * Sound sound = ...;
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
public final class SoundPlayer {
	private static final float SAMPLE_RATE = 44100.0F;
	private static final int CHANNELS = 1;
	private static final int SAMPLE_SIZE_IN_BITS = 16;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final AtomicBoolean isPlaying;
	private final AudioFormat audioFormat;
	private final ExecutorService executorService;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code SoundPlayer} instance.
	 */
	public SoundPlayer() {
		this.isPlaying = new AtomicBoolean();
		this.audioFormat = new AudioFormat(SAMPLE_RATE, SAMPLE_SIZE_IN_BITS, CHANNELS, true, false);
		this.executorService = Executors.newCachedThreadPool();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Plays the sound provided by {@code sound}.
	 * <p>
	 * If {@code sound} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param sound a {@link Sound} with the sound to play
	 * @throws NullPointerException thrown if, and only if, {@code sound} is {@code null}
	 */
	public void play(final Sound sound) {
		Objects.requireNonNull(sound, "sound == null");
		
		if(this.isPlaying.compareAndSet(false, true)) {
			this.executorService.execute(new RunnableImpl(this.isPlaying, this.audioFormat, null, Objects.requireNonNull(sound, "sound == null")));
		}
	}
	
	/**
	 * Plays the sound provided by {@code sound} which is filtered by {@code filter}.
	 * <p>
	 * If either {@code sound} or {@code filter} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param sound a {@link Sound} with the sound to play
	 * @param filter a {@link FilterNB} to filter {@code sound}
	 * @throws NullPointerException thrown if, and only if, either {@code sound} or {@code filter} are {@code null}
	 */
	public void play(final Sound sound, final FilterNB filter) {
		Objects.requireNonNull(sound, "sound == null");
		Objects.requireNonNull(filter, "filter == null");
		
		if(this.isPlaying.compareAndSet(false, true)) {
			this.executorService.execute(new RunnableImpl(this.isPlaying, this.audioFormat, Objects.requireNonNull(filter, "filter == null"), Objects.requireNonNull(sound, "sound == null")));
		}
	}
	
	/**
	 * Stops this {@code SoundPlayer}.
	 */
	public void stop() {
		this.executorService.shutdown();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static final class RunnableImpl implements Runnable {
		private final AtomicBoolean isPlaying;
		private final AudioFormat audioFormat;
		private final FilterNB filter;
		private final Sound sound;
		private SourceDataLine sourceDataLine;
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		public RunnableImpl(final AtomicBoolean isPlaying, final AudioFormat audioFormat, final FilterNB filter, final Sound sound) {
			this.isPlaying = isPlaying;
			this.audioFormat = audioFormat;
			this.filter = filter;
			this.sound = sound;
		}
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		@Override
		public void run() {
			try {
				final DataLine.Info dataLine_Info = new DataLine.Info(SourceDataLine.class, this.audioFormat);
				
				try(final Line line = AudioSystem.getLine(dataLine_Info)) {
					final int bufferSize = this.audioFormat.getFrameSize() * Math.round(this.audioFormat.getSampleRate() / 10);
					
					this.sourceDataLine = SourceDataLine.class.cast(line);
					this.sourceDataLine.open(this.audioFormat, bufferSize);
					this.sourceDataLine.start();
					
					final byte[] bytes = this.filter != null ? this.filter.evaluate(this.sound.toBytes()) : this.sound.toBytes();
					
					final int offset = 0;
					final int length = bytes.length;
					
					if(length > 0) {
						this.sourceDataLine.write(bytes, offset, length);
					}
					
					this.sourceDataLine.drain();
				}
			} catch(final Exception e) {
//				Do nothing.
			} finally {
				this.isPlaying.set(false);
			}
		}
	}
}