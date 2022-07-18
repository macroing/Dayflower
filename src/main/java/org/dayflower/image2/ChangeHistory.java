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
package org.dayflower.image2;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

final class ChangeHistory {
	private final AtomicBoolean hasBegun;
	private final Deque<Change> changesToRedo;
	private final Deque<Change> changesToUndo;
	private final List<Change> changes;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public ChangeHistory() {
		this.hasBegun = new AtomicBoolean();
		this.changesToRedo = new ArrayDeque<>();
		this.changesToUndo = new ArrayDeque<>();
		this.changes = new ArrayList<>();
	}
	
	public ChangeHistory(final ChangeHistory changeHistory) {
		this.hasBegun = new AtomicBoolean(changeHistory.hasBegun.get());
		this.changesToRedo = new ArrayDeque<>(changeHistory.changesToRedo);
		this.changesToUndo = new ArrayDeque<>(changeHistory.changesToUndo);
		this.changes = new ArrayList<>(changeHistory.changes);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public boolean add(final Change change) {
		Objects.requireNonNull(change, "change == null");
		
		if(hasBegun()) {
			this.changes.add(change);
			
			return true;
		}
		
		return false;
	}
	
	public boolean begin() {
		if(this.hasBegun.compareAndSet(false, true)) {
			this.changes.clear();
			
			return true;
		}
		
		return false;
	}
	
	public boolean end() {
		if(this.hasBegun.compareAndSet(true, false)) {
			if(this.changes.size() > 0) {
				final Change change = this.changes.size() == 1 ? this.changes.get(0) : new CombinedChange(this.changes);
				
				this.changesToRedo.clear();
				this.changesToUndo.push(change);
				this.changes.clear();
			}
			
			return true;
		}
		
		return false;
	}
	
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof ChangeHistory)) {
			return false;
		} else if(this.hasBegun.get() != ChangeHistory.class.cast(object).hasBegun.get()) {
			return false;
		} else if(!Objects.equals(this.changesToRedo, ChangeHistory.class.cast(object).changesToRedo)) {
			return false;
		} else if(!Objects.equals(this.changesToUndo, ChangeHistory.class.cast(object).changesToUndo)) {
			return false;
		} else if(!Objects.equals(this.changes, ChangeHistory.class.cast(object).changes)) {
			return false;
		} else {
			return true;
		}
	}
	
	public boolean hasBegun() {
		return this.hasBegun.get();
	}
	
	public boolean redo(final Data data) {
		if(!this.changesToRedo.isEmpty()) {
			final
			Change change = this.changesToRedo.pop();
			change.redo(data);
			
			this.changesToUndo.push(change);
			
			return true;
		}
		
		return false;
	}
	
	public boolean undo(final Data data) {
		if(!this.changesToUndo.isEmpty()) {
			final
			Change change = this.changesToUndo.pop();
			change.undo(data);
			
			this.changesToRedo.push(change);
			
			return true;
		}
		
		return false;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(Boolean.valueOf(this.hasBegun.get()), this.changesToRedo, this.changesToUndo, this.changes);
	}
}