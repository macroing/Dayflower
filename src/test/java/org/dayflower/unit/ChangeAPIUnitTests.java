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
package org.dayflower.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;

import org.dayflower.change.Change;
import org.dayflower.change.ChangeCombiner;
import org.junit.jupiter.api.Test;

@SuppressWarnings({"static-method", "unused"})
public final class ChangeAPIUnitTests {
	public ChangeAPIUnitTests() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Test
	public void testChangeCombinerAddGetChangesAndClear() {
		final Deque<Integer> deque = new ArrayDeque<>();
		
		final ChangeCombiner changeCombiner = new ChangeCombiner();
		
//		Assert that the List returned by changeCombiner.getChanges() is empty:
		assertEquals(changeCombiner.getChanges().size(), 0);
		
		final Change change = new Change(() -> deque.push(Integer.valueOf(deque.size())), () -> deque.pop());
		
//		Assert that changeCombiner.add(null) throws a NullPointerException:
		assertThrows(NullPointerException.class, () -> {
			changeCombiner.add(null);
		});
		
		changeCombiner.add(change);
		changeCombiner.add(change);
		
//		Assert that the List returned by changeCombiner.getChanges() contains two elements:
		assertEquals(changeCombiner.getChanges().size(), 2);
		
		changeCombiner.clear();
		
//		Assert that the List returned by changeCombiner.getChanges() is empty:
		assertEquals(changeCombiner.getChanges().size(), 0);
	}
	
	@Test
	public void testChangeCombinerToChangeAndChangeRedoAndUndo() {
		final List<String> list = new ArrayList<>();
		final List<String> listReverseOrder = new ArrayList<>();
		
		final
		ChangeCombiner changeCombiner = new ChangeCombiner();
		changeCombiner.add(new Change(() -> list.add("A"), () -> listReverseOrder.add(list.remove(0))));
		changeCombiner.add(new Change(() -> list.add("B"), () -> listReverseOrder.add(list.remove(1))));
		changeCombiner.add(new Change(() -> list.add("C"), () -> listReverseOrder.add(list.remove(2))));
		
		final Change change = changeCombiner.toChange();
		
//		Assert that list is empty:
		assertEquals(list, Arrays.asList());
		
		change.redo();
		
//		Assert that list contains the String instances "A", "B" and "C":
		assertEquals(list, Arrays.asList("A", "B", "C"));
		
		change.undo();
		
//		Assert that list is empty:
		assertEquals(list, Arrays.asList());
		
//		Assert that listReverseOrder contains the String instances "C", "B" and "A":
		assertEquals(listReverseOrder, Arrays.asList("C", "B", "A"));
	}
	
	@Test
	public void testChangeConstructor() {
//		Assert that new Change(..., null) throws a NullPointerException:
		assertThrows(NullPointerException.class, () -> {
			new Change(() -> {}, null);
		});
		
//		Assert that new Change(null, ...) throws a NullPointerException:
		assertThrows(NullPointerException.class, () -> {
			new Change(null, () -> {});
		});
	}
	
	@Test
	public void testChangeRedoAndUndo() {
		final Deque<Integer> deque = new ArrayDeque<>();
		
//		Assert that deque is empty:
		assertEquals(deque.size(), 0);
		
		final
		Change change = new Change(() -> deque.push(Integer.valueOf(deque.size())), () -> deque.pop());
		change.redo();
		change.redo();
		
//		Assert that deque contains two elements:
		assertEquals(deque.size(), 2);
		
		change.undo();
		change.undo();
		
//		Assert that deque is empty:
		assertEquals(deque.size(), 0);
	}
}