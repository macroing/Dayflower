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

import org.dayflower.utility.AtomicDouble;

//TODO: Add Javadocs!
public final class DoubleParameter extends Parameter {
	private final AtomicDouble value;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public DoubleParameter(final String name) {
		this(name, 0.0D);
	}
	
//	TODO: Add Javadocs!
	public DoubleParameter(final String name, final double valueDefault) {
		super(name);
		
		this.value = new AtomicDouble(valueDefault);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public double getValue() {
		return this.value.get();
	}
	
//	TODO: Add Javadocs!
	public void setValue(final double value) {
		this.value.set(value);
	}
}