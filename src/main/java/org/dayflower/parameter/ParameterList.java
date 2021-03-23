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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

//TODO: Add Javadocs!
public final class ParameterList {
	private final List<Parameter> parameters;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public ParameterList() {
		this.parameters = new ArrayList<>();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public boolean getBoolean(final String name, final boolean valueDefault) {
		return this.parameters.stream().filter(parameter -> doFilterBoolean(parameter, name)).map(ParameterList::doMapBoolean).findFirst().orElse(Boolean.valueOf(valueDefault)).booleanValue();
	}
	
//	TODO: Add Javadocs!
	public void addBoolean(final String name) {
		addBoolean(name, false);
	}
	
//	TODO: Add Javadocs!
	public void addBoolean(final String name, final boolean valueDefault) {
		this.parameters.add(new BooleanParameter(Objects.requireNonNull(name, "name == null"), valueDefault));
	}
	
//	TODO: Add Javadocs!
	public void load() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static Boolean doMapBoolean(final Parameter parameter) {
		return Boolean.valueOf(BooleanParameter.class.cast(parameter).getValue());
	}
	
	private static boolean doFilterBoolean(final Parameter parameter, final String name) {
		return parameter instanceof BooleanParameter && BooleanParameter.class.cast(parameter).getName().equals(name);
	}
}