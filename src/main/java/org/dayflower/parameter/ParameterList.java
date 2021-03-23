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
	private final ParameterLoader parameterLoader;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public ParameterList() {
		this(parameters -> {});
	}
	
//	TODO: Add Javadocs!
	public ParameterList(final ParameterLoader parameterLoader) {
		this.parameters = new ArrayList<>();
		this.parameterLoader = Objects.requireNonNull(parameterLoader, "parameterLoader == null");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public String getString(final String name) {
		return getString(name, "");
	}
	
//	TODO: Add Javadocs!
	public String getString(final String name, final String valueDefault) {
		Objects.requireNonNull(name, "name == null");
		Objects.requireNonNull(valueDefault, "valueDefault == null");
		
		return this.parameters.stream().filter(parameter -> parameter.getName().equals(name)).filter(parameter -> parameter instanceof StringParameter).map(parameter -> StringParameter.class.cast(parameter)).map(stringParameter -> stringParameter.getValue()).findFirst().orElse(valueDefault);
	}
	
//	TODO: Add Javadocs!
	public boolean getBoolean(final String name) {
		return getBoolean(name, false);
	}
	
//	TODO: Add Javadocs!
	public boolean getBoolean(final String name, final boolean valueDefault) {
		Objects.requireNonNull(name, "name == null");
		
		return this.parameters.stream().filter(parameter -> parameter.getName().equals(name)).filter(parameter -> parameter instanceof BooleanParameter).map(parameter -> BooleanParameter.class.cast(parameter)).map(booleanParameter -> Boolean.valueOf(booleanParameter.getValue())).findFirst().orElse(Boolean.valueOf(valueDefault)).booleanValue();
	}
	
//	TODO: Add Javadocs!
	public double getDouble(final String name) {
		return getDouble(name, 0.0D);
	}
	
//	TODO: Add Javadocs!
	public double getDouble(final String name, final double valueDefault) {
		Objects.requireNonNull(name, "name == null");
		
		return this.parameters.stream().filter(parameter -> parameter.getName().equals(name)).filter(parameter -> parameter instanceof DoubleParameter).map(parameter -> DoubleParameter.class.cast(parameter)).map(doubleParameter -> Double.valueOf(doubleParameter.getValue())).findFirst().orElse(Double.valueOf(valueDefault)).doubleValue();
	}
	
//	TODO: Add Javadocs!
	public float getFloat(final String name) {
		return getFloat(name, 0.0F);
	}
	
//	TODO: Add Javadocs!
	public float getFloat(final String name, final float valueDefault) {
		Objects.requireNonNull(name, "name == null");
		
		return this.parameters.stream().filter(parameter -> parameter.getName().equals(name)).filter(parameter -> parameter instanceof FloatParameter).map(parameter -> FloatParameter.class.cast(parameter)).map(floatParameter -> Float.valueOf(floatParameter.getValue())).findFirst().orElse(Float.valueOf(valueDefault)).floatValue();
	}
	
//	TODO: Add Javadocs!
	public int getInt(final String name) {
		return getInt(name, 0);
	}
	
//	TODO: Add Javadocs!
	public int getInt(final String name, final int valueDefault) {
		Objects.requireNonNull(name, "name == null");
		
		return this.parameters.stream().filter(parameter -> parameter.getName().equals(name)).filter(parameter -> parameter instanceof IntParameter).map(parameter -> IntParameter.class.cast(parameter)).map(intParameter -> Integer.valueOf(intParameter.getValue())).findFirst().orElse(Integer.valueOf(valueDefault)).intValue();
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
	public void addDouble(final String name) {
		addDouble(name, 0.0D);
	}
	
//	TODO: Add Javadocs!
	public void addDouble(final String name, final double valueDefault) {
		this.parameters.add(new DoubleParameter(Objects.requireNonNull(name, "name == null"), valueDefault));
	}
	
//	TODO: Add Javadocs!
	public void addFloat(final String name) {
		addFloat(name, 0.0F);
	}
	
//	TODO: Add Javadocs!
	public void addFloat(final String name, final float valueDefault) {
		this.parameters.add(new FloatParameter(Objects.requireNonNull(name, "name == null"), valueDefault));
	}
	
//	TODO: Add Javadocs!
	public void addInt(final String name) {
		addInt(name, 0);
	}
	
//	TODO: Add Javadocs!
	public void addInt(final String name, final int valueDefault) {
		this.parameters.add(new IntParameter(Objects.requireNonNull(name, "name == null"), valueDefault));
	}
	
//	TODO: Add Javadocs!
	public void addString(final String name) {
		addString(name, "");
	}
	
//	TODO: Add Javadocs!
	public void addString(final String name, final String valueDefault) {
		this.parameters.add(new StringParameter(Objects.requireNonNull(name, "name == null"), Objects.requireNonNull(valueDefault, "valueDefault == null")));
	}
	
//	TODO: Add Javadocs!
	public void load() {
		this.parameterLoader.load(new ArrayList<>(this.parameters));
	}
}