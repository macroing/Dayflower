/**
 * Copyright 2014 - 2023 J&#246;rgen Lundgren
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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

@SuppressWarnings("static-method")
public final class ChangeHistoryUnitTests {
	public ChangeHistoryUnitTests() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Test
	public void testEmptyRedo() {
		final ChangeHistory changeHistory = new ChangeHistory();
		
//		Assert that changeHistory.redo() has nothing to redo, because it is empty:
		assertFalse(changeHistory.redo());
	}
	
	@Test
	public void testEmptyUndo() {
		final ChangeHistory changeHistory = new ChangeHistory();
		
//		Assert that changeHistory.undo() has nothing to undo, because it is empty:
		assertFalse(changeHistory.undo());
	}
	
	@Test
	public void testPushChangeBooleanPushChangeBooleanRedoUndoUndoRedoRedoRedoUndoUndoAndUndo() {
		final List<String> list = new ArrayList<>();
		
		final
		ChangeHistory changeHistory = new ChangeHistory();
		changeHistory.push(new Change(() -> list.add("A"), () -> list.remove("A")), true);
		changeHistory.push(new Change(() -> list.add("B"), () -> list.remove("B")), true);
		
//		Assert that list contains "A" and "B":
		assertEquals(Arrays.asList("A", "B"), list);
		
//		Assert that changeHistory.redo() has nothing to redo, because the operation was performed by ChangeHistory.push(Change, boolean):
		assertFalse(changeHistory.redo());
		
//		Assert that list contains "A" and "B" and nothing has changed because of changeHistory.redo():
		assertEquals(Arrays.asList("A", "B"), list);
		
//		Assert that changeHistory.undo() has something to undo:
		assertTrue(changeHistory.undo());
		
//		Assert that list contains "A":
		assertEquals(Arrays.asList("A"), list);
		
//		Assert that changeHistory.undo() has something to undo:
		assertTrue(changeHistory.undo());
		
//		Assert that list is empty:
		assertTrue(list.isEmpty());
		
//		Assert that changeHistory.redo() has something to redo:
		assertTrue(changeHistory.redo());
		
//		Assert that list contains "A":
		assertEquals(Arrays.asList("A"), list);
		
//		Assert that changeHistory.redo() has something to redo:
		assertTrue(changeHistory.redo());
		
//		Assert that list contains "A" and "B":
		assertEquals(Arrays.asList("A", "B"), list);
		
//		Assert that changeHistory.redo() has nothing to redo:
		assertFalse(changeHistory.redo());
		
//		Assert that list contains "A" and "B" and nothing has changed because of changeHistory.redo():
		assertEquals(Arrays.asList("A", "B"), list);
		
//		Assert that changeHistory.undo() has something to undo:
		assertTrue(changeHistory.undo());
		
//		Assert that list contains "A":
		assertEquals(Arrays.asList("A"), list);
		
//		Assert that changeHistory.undo() has something to undo:
		assertTrue(changeHistory.undo());
		
//		Assert that list is empty:
		assertTrue(list.isEmpty());
		
//		Assert that changeHistory.undo() has nothing to undo:
		assertFalse(changeHistory.undo());
		
//		Assert that list is empty and nothing has changed because of changeHistory.undo():
		assertTrue(list.isEmpty());
	}
	
	@Test
	public void testPushChangeRedoUndoRedoRedoUndoAndUndo() {
		final List<String> list = new ArrayList<>(Arrays.asList("A"));
		
		final
		ChangeHistory changeHistory = new ChangeHistory();
		changeHistory.push(new Change(() -> list.add("A"), () -> list.remove("A")));
		
//		Assert that list contains "A":
		assertEquals(Arrays.asList("A"), list);
		
//		Assert that changeHistory.redo() has nothing to redo, because the operation was performed outside:
		assertFalse(changeHistory.redo());
		
//		Assert that list contains "A" and nothing has changed because of changeHistory.redo():
		assertEquals(Arrays.asList("A"), list);
		
//		Assert that changeHistory.undo() has something to undo:
		assertTrue(changeHistory.undo());
		
//		Assert that list is empty:
		assertTrue(list.isEmpty());
		
//		Assert that changeHistory.redo() has something to redo:
		assertTrue(changeHistory.redo());
		
//		Assert that changeHistory.redo() has nothing to redo:
		assertFalse(changeHistory.redo());
		
//		Assert that list contains "A":
		assertEquals(Arrays.asList("A"), list);
		
//		Assert that changeHistory.undo() has something to undo:
		assertTrue(changeHistory.undo());
		
//		Assert that changeHistory.undo() has nothing to undo:
		assertFalse(changeHistory.undo());
	}
	
	@Test
	public void testPushNull() {
		final ChangeHistory changeHistory = new ChangeHistory();
		
//		Assert that changeHistory.push(null) throws a NullPointerException:
		assertThrows(NullPointerException.class, () -> changeHistory.push(null));
	}
}