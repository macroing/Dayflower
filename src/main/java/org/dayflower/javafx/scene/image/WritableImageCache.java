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
package org.dayflower.javafx.scene.image;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import javafx.scene.image.WritableImage;

/**
 * A {@code WritableImageCache} is a cache for {@code WritableImage} instances.
 * <p>
 * To use this class, consider the following example:
 * <pre>
 * {@code
 * WritableImageCache<String> writableImageCache = new WritableImageCache<>(pathname -> Image.load(pathname).toWritableImage());
 * 
 * WritableImage writableImage = writableImageCache.get("Image.png");
 * }
 * </pre>
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class WritableImageCache<T> {
	private final Function<T, WritableImage> writableImageFactory;
	private final Map<T, WritableImage> writableImageCache;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code WritableImageCache} instance.
	 * <p>
	 * If {@code writableImageFactory} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param writableImageFactory a {@code Function} that creates a {@code WritableImage} instance
	 * @throws NullPointerException thrown if, and only if, {@code writableImageFactory} is {@code null}
	 */
	public WritableImageCache(final Function<T, WritableImage> writableImageFactory) {
		this.writableImageFactory = Objects.requireNonNull(writableImageFactory, "writableImageFactory == null");
		this.writableImageCache = new HashMap<>();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the newly created or previously cached {@code WritableImage} instance associated with {@code object}.
	 * <p>
	 * If either {@code object} or the newly created {@code WritableImage} instance are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param object the {@code Object} associated with the {@code WritableImage} instance to return
	 * @return the newly created or previously cached {@code WritableImage} instance associated with {@code object}
	 * @throws NullPointerException thrown if, and only if, either {@code object} or the newly created {@code WritableImage} instance are {@code null}
	 */
	public WritableImage get(final T object) {
		return this.writableImageCache.computeIfAbsent(Objects.requireNonNull(object, "object == null"), key -> Objects.requireNonNull(this.writableImageFactory.apply(key)));
	}
	
	/**
	 * Clears this {@code WritableImageCache} instance.
	 */
	public void clear() {
		this.writableImageCache.clear();
	}
}