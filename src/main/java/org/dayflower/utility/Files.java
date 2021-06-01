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
package org.dayflower.utility;

import java.io.File;
import java.util.Objects;

/**
 * A class that consists exclusively of static methods that returns or performs various operations on {@code File} instances.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class Files {
	private Files() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code File} instance that represents the closest existing directory to {@code directory}, or {@code new File(".")} if no such directory exists.
	 * <p>
	 * If {@code directory} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param directory a {@code File} instance that represents the directory to start at
	 * @return a {@code File} instance that represents the closest existing directory to {@code directory}, or {@code new File(".")} if no such directory exists
	 * @throws NullPointerException thrown if, and only if, {@code directory} is {@code null}
	 */
	public static File findClosestExistingDirectoryTo(final File directory) {
		Objects.requireNonNull(directory, "directory == null");
		
		File currentDirectory = directory;
		
		while(currentDirectory != null && !currentDirectory.isDirectory()) {
			currentDirectory = currentDirectory.getParentFile();
		}
		
		return currentDirectory != null && currentDirectory.isDirectory() ? currentDirectory : new File(".");
	}
}