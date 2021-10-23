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
package org.dayflower.change;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * A {@code ChangeCombiner} combines multiple {@link Change} instances into a single {@code Change} instance.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class ChangeCombiner {
	private final List<Change> changes;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code ChangeCombiner} instance.
	 */
	public ChangeCombiner() {
		this.changes = new ArrayList<>();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link Change} instance that combines all {@code Change} instances that have been added.
	 * <p>
	 * The {@link RedoAction} and {@link UndoAction} instances of the returned {@code Change} instance will iterate the combined {@code Change} instances in different orders. The {@code RedoAction} instance will use the order they were added and the {@code UndoAction} instance will use its reverse order.
	 * 
	 * @return a {@code Change} instance that combines all {@code Change} instances that have been added
	 */
	public Change toChange() {
		final List<Change> changesToRedo = getChanges();
		final List<Change> changesToUndo = getChanges();
		
		Collections.reverse(changesToUndo);
		
		final RedoAction redoAction = () -> changesToRedo.forEach(change -> change.redo());
		final UndoAction undoAction = () -> changesToUndo.forEach(change -> change.undo());
		
		return new Change(redoAction, undoAction);
	}
	
	/**
	 * Returns a {@code List} with all {@link Change} instances that have been added.
	 * <p>
	 * Modifications to the returned {@code List} will not affect this {@code ChangeCombiner} instance.
	 * 
	 * @return a {@code List} with all {@code Change} instances that have been added
	 */
	public List<Change> getChanges() {
		return new ArrayList<>(this.changes);
	}
	
	/**
	 * Adds {@code change} to this {@code ChangeCombiner} instance.
	 * <p>
	 * If {@code change} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param change the {@link Change} instance to add
	 * @throws NullPointerException thrown if, and only if, {@code change} is {@code null}
	 */
	public void add(final Change change) {
		this.changes.add(Objects.requireNonNull(change, "change == null"));
	}
	
	/**
	 * Clears this {@code ChangeCombiner} instance.
	 */
	public void clear() {
		this.changes.clear();
	}
}