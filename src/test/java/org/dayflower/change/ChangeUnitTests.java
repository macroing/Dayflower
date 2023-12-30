/**
 * Copyright 2014 - 2024 J&#246;rgen Lundgren
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
package org.dayflower.change;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

@SuppressWarnings("static-method")
public final class ChangeUnitTests {
	public ChangeUnitTests() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Test
	public void testNewNullAndUndoAction() {
//		Assert that new Change(null, ...) throws a NullPointerException:
		assertThrows(NullPointerException.class, () -> new Change(null, () -> {/* Do nothing. */}));
	}
	
	@Test
	public void testNewRedoActionAndNull() {
//		Assert that new Change(..., null) throws a NullPointerException:
		assertThrows(NullPointerException.class, () -> new Change(() -> {/* Do nothing. */}, null));
	}
	
	@Test
	public void testRedo() {
		final List<Integer> list = new ArrayList<>();
		
		final
		Change change = new Change(() -> list.add(Integer.valueOf(list.size())), () -> {/* Do nothing. */});
		change.redo();
		change.redo();
		
//		Assert that list contains Integer.valueOf(0) and Integer.valueOf(1):
		assertEquals(Arrays.asList(Integer.valueOf(0), Integer.valueOf(1)), list);
	}
	
	@Test
	public void testUndo() {
		final List<Integer> list = new ArrayList<>();
		
		final
		Change change = new Change(() -> list.add(Integer.valueOf(list.size())), () -> list.remove(list.size() - 1));
		change.redo();
		change.redo();
		change.undo();
		change.undo();
		
//		Assert that list is empty:
		assertTrue(list.isEmpty());
	}
}