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

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;

/**
 * A {@code ChangeHistory} represents a history of changes.
 * <p>
 * All changes in this {@code ChangeHistory} class are represented by {@link Change} instances.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class ChangeHistory {
	private final Deque<Change> changesToRedo;
	private final Deque<Change> changesToUndo;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code ChangeHistory} instance.
	 */
	public ChangeHistory() {
		this.changesToRedo = new ArrayDeque<>();
		this.changesToUndo = new ArrayDeque<>();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Performs the current redo operation.
	 * <p>
	 * Returns {@code true} if, and only if, the redo operation was performed, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, the redo operation was performed, {@code false} otherwise
	 */
	public boolean redo() {
		if(!this.changesToRedo.isEmpty()) {
			final
			Change change = this.changesToRedo.pop();
			change.redo();
			
			this.changesToUndo.push(change);
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * Performs the current undo operation.
	 * <p>
	 * Returns {@code true} if, and only if, the undo operation was performed, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, the undo operation was performed, {@code false} otherwise
	 */
	public boolean undo() {
		if(!this.changesToUndo.isEmpty()) {
			final
			Change change = this.changesToUndo.pop();
			change.undo();
			
			this.changesToRedo.push(change);
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * Pushes {@code change} to this {@code ChangeHistory} instance.
	 * <p>
	 * If {@code change} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * changeHistory.push(change, false);
	 * }
	 * </pre>
	 * 
	 * @param change the {@link Change} instance to push
	 * @throws NullPointerException thrown if, and only if, {@code change} is {@code null}
	 */
	public void push(final Change change) {
		push(change, false);
	}
	
	/**
	 * Pushes {@code change} to this {@code ChangeHistory} instance.
	 * <p>
	 * If {@code change} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code isRedoing} is {@code true}, {@code change.redo()} will be called.
	 * 
	 * @param change the {@link Change} instance to push
	 * @param isRedoing {@code true} if, and only if, {@code change.redo()} should be called
	 * @throws NullPointerException thrown if, and only if, {@code change} is {@code null}
	 */
	public void push(final Change change, final boolean isRedoing) {
		Objects.requireNonNull(change, "change == null");
		
		if(isRedoing) {
			change.redo();
		}
		
		this.changesToRedo.clear();
		this.changesToUndo.push(change);
	}
}