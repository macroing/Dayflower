/**
 * Copyright 2020 J&#246;rgen Lundgren
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

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import javafx.scene.image.WritableImage;

//TODO: Add Javadocs!
public final class WritableImageCache<T> {
	private final Function<T, WritableImage> writableImageFactory;
	private final Map<T, WritableImage> writableImageCache;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public WritableImageCache(final Function<T, WritableImage> writableImageFactory) {
		this.writableImageFactory = Objects.requireNonNull(writableImageFactory, "writableImageFactory == null");
		this.writableImageCache = new HashMap<>();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public WritableImage get(final T object) {
		return this.writableImageCache.computeIfAbsent(Objects.requireNonNull(object, "object == null"), key -> Objects.requireNonNull(this.writableImageFactory.apply(key)));
	}
}