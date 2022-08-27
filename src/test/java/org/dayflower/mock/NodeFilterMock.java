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
package org.dayflower.mock;

import java.util.Objects;

import org.dayflower.node.Node;
import org.dayflower.node.NodeFilter;

public final class NodeFilterMock implements NodeFilter {
	private final boolean isAccepted;
	private final boolean isThrowingRuntimeException;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public NodeFilterMock(final boolean isAccepted, final boolean isThrowingRuntimeException) {
		this.isAccepted = isAccepted;
		this.isThrowingRuntimeException = isThrowingRuntimeException;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public boolean isAccepted(final Node node) {
		Objects.requireNonNull(node, "node == null");
		
		if(this.isThrowingRuntimeException) {
			throw new RuntimeException();
		}
		
		return this.isAccepted;
	}
}