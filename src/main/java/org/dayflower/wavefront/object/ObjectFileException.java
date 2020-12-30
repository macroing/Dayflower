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
package org.dayflower.wavefront.object;

import java.io.IOException;

/**
 * Thrown to indicate that a Wavefront Object file could not be loaded.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class ObjectFileException extends IOException {
	private static final long serialVersionUID = 1L;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a {@code ObjectFileException} with no detail message or cause.
	 */
	public ObjectFileException() {
		
	}
	
	/**
	 * Constructs a {@code ObjectFileException} with a detail message but no cause.
	 * 
	 * @param message a message describing this {@code ObjectFileException}
	 */
	public ObjectFileException(final String message) {
		super(message);
	}
	
	/**
	 * Constructs a {@code ObjectFileException} with a detail message and a cause.
	 * 
	 * @param message a message describing this {@code ObjectFileException}
	 * @param cause the {@code Throwable} that caused this {@code ObjectFileException} to be thrown
	 */
	public ObjectFileException(final String message, final Throwable cause) {
		super(message, cause);
	}
	
	/**
	 * Constructs a {@code ObjectFileException} with no detail message but a cause.
	 * 
	 * @param cause the {@code Throwable} that caused this {@code ObjectFileException} to be thrown
	 */
	public ObjectFileException(final Throwable cause) {
		super(cause);
	}
}