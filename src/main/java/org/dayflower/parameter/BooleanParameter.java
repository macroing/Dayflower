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
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

//TODO: Add Javadocs!
public final class BooleanParameter implements Parameter {
	private final AtomicBoolean value;
	private final AtomicBoolean valueDefault;
	private final String name;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public BooleanParameter(final String name, final boolean valueDefault) {
		this.value = new AtomicBoolean(valueDefault);
		this.valueDefault = new AtomicBoolean(valueDefault);
		this.name = Objects.requireNonNull(name, "name == null");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public String getName() {
		return this.name;
	}
	
//	TODO: Add Javadocs!
	public boolean getValue() {
		return this.value.get();
	}
	
//	TODO: Add Javadocs!
	public boolean getValueDefault() {
		return this.valueDefault.get();
	}
	
//	TODO: Add Javadocs!
	public void setValue(final boolean value) {
		this.value.set(value);
	}
}