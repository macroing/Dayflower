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
package org.dayflower.scene;

import java.io.File;
import java.io.UncheckedIOException;

/**
 * A {@code SceneLoader} is used to load {@link Scene} instances.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public interface SceneLoader {
	/**
	 * Loads a {@link Scene} instance from the file represented by {@code file}.
	 * <p>
	 * Returns the loaded {@code Scene} instance.
	 * <p>
	 * If {@code file} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * sceneLoader.load(file, new Scene());
	 * }
	 * </pre>
	 * 
	 * @param file a {@code File} instance that represents a file
	 * @return the loaded {@code Scene} instance
	 * @throws NullPointerException thrown if, and only if, {@code file} is {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
	Scene load(final File file);
	
	/**
	 * Loads a {@link Scene} instance from the file represented by {@code file} into {@code scene}.
	 * <p>
	 * Returns the loaded {@code Scene} instance, {@code scene}.
	 * <p>
	 * If either {@code file} or {@code scene} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * 
	 * @param file a {@code File} instance that represents a file
	 * @param scene the {@code Scene} instance to load into
	 * @return the loaded {@code Scene} instance, {@code scene}
	 * @throws NullPointerException thrown if, and only if, either {@code file} or {@code scene} are {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
	Scene load(final File file, final Scene scene);
	
	/**
	 * Loads a {@link Scene} instance from the file represented by {@code pathname}.
	 * <p>
	 * Returns the loaded {@code Scene} instance.
	 * <p>
	 * If {@code pathname} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * sceneLoader.load(pathname, new Scene());
	 * }
	 * </pre>
	 * 
	 * @param pathname a {@code String} instance that represents a pathname to a file
	 * @return the loaded {@code Scene} instance
	 * @throws NullPointerException thrown if, and only if, {@code pathname} is {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
	Scene load(final String pathname);
	
	/**
	 * Loads a {@link Scene} instance from the file represented by {@code pathname} into {@code scene}.
	 * <p>
	 * Returns the loaded {@code Scene} instance, {@code scene}.
	 * <p>
	 * If either {@code pathname} or {@code scene} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * 
	 * @param pathname a {@code String} instance that represents a pathname to a file
	 * @param scene the {@code Scene} instance to load into
	 * @return the loaded {@code Scene} instance, {@code scene}
	 * @throws NullPointerException thrown if, and only if, either {@code pathname} or {@code scene} are {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
	Scene load(final String pathname, final Scene scene);
}