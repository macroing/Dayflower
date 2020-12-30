/**
 * Copyright 2020 - 2021 J&#246;rgen Lundgren
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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;

/**
 * A {@code ByteArraySound} is an implementation of {@link Sound} that provides sound data by a fixed length {@code byte} array.
 * <p>
 * This class is immutable and therefore also thread-safe.
 * <p>
 * To use this class, consider the following example:
 * <pre>
 * {@code
 * Sound sound = ByteArraySound.read("path/to/sound.wav");
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
public final class ByteArraySound implements Sound {
	private final byte[] bytes;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code ByteArraySound} instance.
	 * <p>
	 * Modifying {@code bytes} will not affect this {@code ByteArraySound} instance.
	 * <p>
	 * If {@code bytes} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param bytes a {@code byte} array with sound data
	 */
	public ByteArraySound(final byte[] bytes) {
		this.bytes = bytes.clone();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code byte} array with sound data.
	 * <p>
	 * Modifying the returned {@code byte} array will not affect this {@code ByteArraySound} instance.
	 * 
	 * @return a {@code byte} array with sound data
	 */
	@Override
	public byte[] toBytes() {
		return this.bytes.clone();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Reads sound data from a file given the {@code File} {@code file}.
	 * <p>
	 * Returns a new {@code ByteArraySound} with the sound data read.
	 * <p>
	 * If an I/O-error occurred while reading, an {@code IOException} will be thrown.
	 * <p>
	 * If {@code file} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param file the file to read from
	 * @return a new {@code ByteArraySound} with the sound data read
	 * @throws IOException thrown if, and only if, an I/O-error occurred while reading
	 * @throws NullPointerException thrown if, and only if, {@code file} is {@code null}
	 */
	public static ByteArraySound read(final File file) throws IOException {
		return new ByteArraySound(Files.readAllBytes(file.toPath()));
	}
	
	/**
	 * Reads sound data from a file given the filename {@code filename}.
	 * <p>
	 * Returns a new {@code ByteArraySound} with the sound data read.
	 * <p>
	 * If an I/O-error occurred while reading, an {@code IOException} will be thrown.
	 * <p>
	 * If {@code filename} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param filename the filename of the file to read from
	 * @return a new {@code ByteArraySound} with the sound data read
	 * @throws IOException thrown if, and only if, an I/O-error occurred while reading
	 * @throws NullPointerException thrown if, and only if, {@code filename} is {@code null}
	 */
	public static ByteArraySound read(final String filename) throws IOException {
		return read(new File(Objects.requireNonNull(filename, "filename == null")));
	}
}