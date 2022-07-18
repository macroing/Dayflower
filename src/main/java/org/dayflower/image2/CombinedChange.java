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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.dayflower.utility.ParameterArguments;

final class CombinedChange implements Change {
	private final List<Change> changes;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public CombinedChange(final List<Change> changes) {
		this.changes = new ArrayList<>(ParameterArguments.requireNonNullList(changes, "changes"));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof CombinedChange)) {
			return false;
		} else if(!Objects.equals(this.changes, CombinedChange.class.cast(object).changes)) {
			return false;
		} else {
			return true;
		}
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(this.changes);
	}
	
	@Override
	public void redo(final Data data) {
		for(final Change change : this.changes) {
			change.redo(data);
		}
	}
	
	@Override
	public void undo(final Data data) {
		for(int i = this.changes.size() - 1; i >= 0; i--) {
			this.changes.get(i).undo(data);
		}
	}
}