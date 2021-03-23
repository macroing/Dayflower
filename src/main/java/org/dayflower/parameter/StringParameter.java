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
import java.util.concurrent.atomic.AtomicReference;

//TODO: Add Javadocs!
public final class StringParameter extends Parameter {
	private final AtomicReference<String> value;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public StringParameter(final String name) {
		this(name, "");
	}
	
//	TODO: Add Javadocs!
	public StringParameter(final String name, final String valueDefault) {
		super(name);
		
		this.value = new AtomicReference<>(Objects.requireNonNull(valueDefault, "valueDefault == null"));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public String getValue() {
		return this.value.get();
	}
	
//	TODO: Add Javadocs!
	public void setValue(final String value) {
		this.value.set(Objects.requireNonNull(value, "value == null"));
	}
}