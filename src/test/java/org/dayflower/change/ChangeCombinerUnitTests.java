/**
 * Copyright 2014 - 2022 J&#246;rgen Lundgren
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
public final class ChangeCombinerUnitTests {
	public ChangeCombinerUnitTests() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Test
	public void testAddChangeGetChangesAndClear() {
		final ChangeCombiner changeCombiner = new ChangeCombiner();
		
//		Assert that the List returned by changeCombiner.getChanges() is empty:
		assertTrue(changeCombiner.getChanges().isEmpty());
		
		changeCombiner.add(new Change(() -> {/* Do nothing. */}, () -> {/* Do nothing. */}));
		changeCombiner.add(new Change(() -> {/* Do nothing. */}, () -> {/* Do nothing. */}));
		
//		Assert that the List returned by changeCombiner.getChanges() contains two elements:
		assertEquals(2, changeCombiner.getChanges().size());
		
		changeCombiner.clear();
		
//		Assert that the List returned by changeCombiner.getChanges() is empty:
		assertTrue(changeCombiner.getChanges().isEmpty());
	}
	
	@Test
	public void testAddNull() {
		final ChangeCombiner changeCombiner = new ChangeCombiner();
		
//		Assert that changeCombiner.add(null) throws a NullPointerException:
		assertThrows(NullPointerException.class, () -> changeCombiner.add(null));
	}
	
	@Test
	public void testToChangeAndChangeRedoAndUndo() {
		final List<String> list = new ArrayList<>();
		final List<String> listReverseOrder = new ArrayList<>();
		
		final
		ChangeCombiner changeCombiner = new ChangeCombiner();
		changeCombiner.add(new Change(() -> list.add("A"), () -> listReverseOrder.add(list.remove(0))));
		changeCombiner.add(new Change(() -> list.add("B"), () -> listReverseOrder.add(list.remove(1))));
		changeCombiner.add(new Change(() -> list.add("C"), () -> listReverseOrder.add(list.remove(2))));
		
		final Change change = changeCombiner.toChange();
		
//		Assert that list is empty:
		assertTrue(list.isEmpty());
		
		change.redo();
		
//		Assert that list contains the String instances "A", "B" and "C":
		assertEquals(Arrays.asList("A", "B", "C"), list);
		
		change.undo();
		
//		Assert that list is empty:
		assertTrue(list.isEmpty());
		
//		Assert that listReverseOrder contains the String instances "C", "B" and "A":
		assertEquals(Arrays.asList("C", "B", "A"), listReverseOrder);
	}
}