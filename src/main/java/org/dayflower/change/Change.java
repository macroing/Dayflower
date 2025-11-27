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
package org.dayflower.change;

import java.util.Objects;

/**
 * A {@code Change} represents a change that can be undone and redone.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class Change {
	private final RedoAction redoAction;
	private final UndoAction undoAction;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code Change} instance.
	 * <p>
	 * If either {@code redoAction} or {@code undoAction} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param redoAction the {@link RedoAction} instance associated with this {@code Change} instance
	 * @param undoAction the {@link UndoAction} instance associated with this {@code Change} instance
	 * @throws NullPointerException thrown if, and only if, either {@code redoAction} or {@code undoAction} are {@code null}
	 */
	public Change(final RedoAction redoAction, final UndoAction undoAction) {
		this.redoAction = Objects.requireNonNull(redoAction, "redoAction == null");
		this.undoAction = Objects.requireNonNull(undoAction, "undoAction == null");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Executes the {@link RedoAction} instance that is associated with this {@code Change} instance.
	 */
	public void redo() {
		this.redoAction.redo();
	}
	
	/**
	 * Executes the {@link UndoAction} instance that is associated with this {@code Change} instance.
	 */
	public void undo() {
		this.undoAction.undo();
	}
}