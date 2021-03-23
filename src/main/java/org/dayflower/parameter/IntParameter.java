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
package org.dayflower.parameter;

import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicInteger;

//TODO: Add Javadocs!
public final class IntParameter extends Parameter {
	private final AtomicInteger value;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public IntParameter(final String name) {
		this(name, 0);
	}
	
//	TODO: Add Javadocs!
	public IntParameter(final String name, final int valueDefault) {
		super(name);
		
		this.value = new AtomicInteger(valueDefault);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public int getValue() {
		return this.value.get();
	}
	
//	TODO: Add Javadocs!
	public void setValue(final int value) {
		this.value.set(value);
	}
}