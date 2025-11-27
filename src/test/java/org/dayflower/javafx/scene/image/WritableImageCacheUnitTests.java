/**
 * Copyright 2014 - 2025 J&#246;rgen Lundgren
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import javafx.scene.image.WritableImage;

@SuppressWarnings("static-method")
public final class WritableImageCacheUnitTests {
	public WritableImageCacheUnitTests() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Test
	public void testClearAndGetAndSize() {
		final WritableImageCache<String> writableImageCache = new WritableImageCache<>(name -> new WritableImage(10, 10));
		
		assertEquals(0, writableImageCache.size());
		
		final WritableImage a = writableImageCache.get("1");
		final WritableImage b = writableImageCache.get("1");
		final WritableImage c = writableImageCache.get("2");
		
		assertNotNull(a);
		assertNotNull(b);
		assertNotNull(c);
		
		assertTrue(a == b);
		assertTrue(a != c);
		
		assertThrows(NullPointerException.class, () -> writableImageCache.get(null));
		assertThrows(NullPointerException.class, () -> new WritableImageCache<String>(name -> null).get(""));
		
		assertEquals(2, writableImageCache.size());
		
		writableImageCache.clear();
		
		assertEquals(0, writableImageCache.size());
		
		final WritableImage d = writableImageCache.get("1");
		final WritableImage e = writableImageCache.get("2");
		
		assertTrue(a != d);
		assertTrue(c != e);
		
		writableImageCache.clear();
	}
	
	@Test
	public void testConstructor() {
		assertThrows(NullPointerException.class, () -> new WritableImageCache<String>(null));
	}
}