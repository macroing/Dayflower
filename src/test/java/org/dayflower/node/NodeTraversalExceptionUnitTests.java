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
package org.dayflower.node;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

@SuppressWarnings("static-method")
public final class NodeTraversalExceptionUnitTests {
	public NodeTraversalExceptionUnitTests() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Test
	public void testConstructor() {
		final NodeTraversalException nodeTraversalException = new NodeTraversalException();
		
		assertEquals(null, nodeTraversalException.getMessage());
		assertEquals(null, nodeTraversalException.getCause());
	}
	
	@Test
	public void testConstructorString() {
		final NodeTraversalException nodeTraversalException = new NodeTraversalException("Message.");
		
		assertEquals("Message.", nodeTraversalException.getMessage());
		assertEquals(null, nodeTraversalException.getCause());
	}
	
	@Test
	public void testConstructorStringThrowable() {
		final Throwable cause = new RuntimeException();
		
		final NodeTraversalException nodeTraversalException = new NodeTraversalException("Message.", cause);
		
		assertEquals("Message.", nodeTraversalException.getMessage());
		assertEquals(cause, nodeTraversalException.getCause());
	}
	
	@Test
	public void testConstructorThrowable() {
		final Throwable cause = new RuntimeException();
		
		final NodeTraversalException nodeTraversalException = new NodeTraversalException(cause);
		
		assertEquals("java.lang.RuntimeException", nodeTraversalException.getMessage());
		assertEquals(cause, nodeTraversalException.getCause());
	}
}