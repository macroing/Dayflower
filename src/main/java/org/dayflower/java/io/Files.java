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
package org.dayflower.java.io;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

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
	
	/**
	 * Returns a {@code List} with {@code File} instances that represents files in the directory denoted by {@code directory} or any of its sub-directories.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Files.findFilesFromDirectory(new File("."));
	 * }
	 * </pre>
	 * 
	 * @return a {@code List} with {@code File} instances that represents files in the directory denoted by {@code directory} or any of its sub-directories
	 */
	public static List<File> findFilesFromDirectory() {
		return findFilesFromDirectory(new File("."));
	}
	
	/**
	 * Returns a {@code List} with {@code File} instances that represents files in the directory denoted by {@code directory} or any of its sub-directories.
	 * <p>
	 * If {@code directory} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Files.findFilesFromDirectory(directory, currentFile -> true);
	 * }
	 * </pre>
	 * 
	 * @param directory a {@code File} instance that represents the root directory to start from
	 * @return a {@code List} with {@code File} instances that represents files in the directory denoted by {@code directory} or any of its sub-directories
	 * @throws NullPointerException thrown if, and only if, {@code directory} is {@code null}
	 */
	public static List<File> findFilesFromDirectory(final File directory) {
		return findFilesFromDirectory(directory, currentFile -> true);
	}
	
	/**
	 * Returns a {@code List} with {@code File} instances that represents files in the directory denoted by {@code directory} or any of its sub-directories.
	 * <p>
	 * If either {@code directory} or {@code filePredicate} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Files.findFilesFromDirectory(directory, filePredicate, currentDirectory -> true);
	 * }
	 * </pre>
	 * 
	 * @param directory a {@code File} instance that represents the root directory to start from
	 * @param filePredicate a {@code Predicate} instance that tests {@code File} instances that represents files
	 * @return a {@code List} with {@code File} instances that represents files in the directory denoted by {@code directory} or any of its sub-directories
	 * @throws NullPointerException thrown if, and only if, either {@code directory} or {@code filePredicate} are {@code null}
	 */
	public static List<File> findFilesFromDirectory(final File directory, final Predicate<File> filePredicate) {
		return findFilesFromDirectory(directory, filePredicate, currentDirectory -> true);
	}
	
	/**
	 * Returns a {@code List} with {@code File} instances that represents files in the directory denoted by {@code directory} or any of its sub-directories.
	 * <p>
	 * If either {@code directory}, {@code filePredicate} or {@code directoryPredicate} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param directory a {@code File} instance that represents the root directory to start from
	 * @param filePredicate a {@code Predicate} instance that tests {@code File} instances that represents files
	 * @param directoryPredicate a {@code Predicate} instance that tests {@code File} instances that represents directories
	 * @return a {@code List} with {@code File} instances that represents files in the directory denoted by {@code directory} or any of its sub-directories
	 * @throws NullPointerException thrown if, and only if, either {@code directory}, {@code filePredicate} or {@code directoryPredicate} are {@code null}
	 */
	public static List<File> findFilesFromDirectory(final File directory, final Predicate<File> filePredicate, final Predicate<File> directoryPredicate) {
		Objects.requireNonNull(directory, "directory == null");
		Objects.requireNonNull(filePredicate, "filePredicate == null");
		Objects.requireNonNull(directoryPredicate, "directoryPredicate == null");
		
		final List<File> files = new ArrayList<>();
		
		doFindFilesFromDirectory(directory, filePredicate, directoryPredicate, files);
		
		return files;
	}
	
	/**
	 * Reads all the bytes from a file into a {@code String}.
	 * <p>
	 * Returns a {@code String} instance.
	 * <p>
	 * If {@code file} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * 
	 * @param file a {@code File} that represents the file to read
	 * @return a {@code String} instance
	 * @throws NullPointerException thrown if, and only if, {@code file} is {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
	public static String readAllBytesToString(final File file) {
		try {
			return new String(java.nio.file.Files.readAllBytes(file.toPath()));
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static void doFindFilesFromDirectory(final File directory, final Predicate<File> filePredicate, final Predicate<File> directoryPredicate, final List<File> files) {
		if(directory.isDirectory()) {
			final File[] listedFiles = directory.listFiles();
			
			for(final File listedFile : listedFiles) {
				if(listedFile.isDirectory() && directoryPredicate.test(listedFile)) {
					doFindFilesFromDirectory(listedFile, filePredicate, directoryPredicate, files);
				} else if(listedFile.isFile() && filePredicate.test(listedFile)) {
					files.add(listedFile);
				}
			}
		}
	}
}