/**
 * Copyright 2014 - 2024 J&#246;rgen Lundgren
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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * A {@code ParameterList} represents a list of parameters.
 * <p>
 * The parameters are represented as {@link Parameter} instances. But convenience methods for {@code boolean}, {@code double}, {@code float}, {@code int} and {@code String} exists.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class ParameterList {
	private final List<Parameter> parameters;
	private final ParameterLoader parameterLoader;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code ParameterList} instance.
	 */
	public ParameterList() {
		this(parameters -> {/* Do nothing. */});
	}
	
	/**
	 * Constructs a new {@code ParameterList} instance that is associated with {@code parameterLoader} as its {@link ParameterLoader}.
	 * <p>
	 * If {@code parameterLoader} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param parameterLoader a {@code ParameterLoader} instance
	 * @throws NullPointerException thrown if, and only if, {@code parameterLoader} is {@code null}
	 */
	public ParameterList(final ParameterLoader parameterLoader) {
		this.parameters = new ArrayList<>();
		this.parameterLoader = Objects.requireNonNull(parameterLoader, "parameterLoader == null");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code List} with all {@link Parameter} instances that are currently added to this {@code ParameterList} instance.
	 * <p>
	 * A {@code ParameterList} instance will not be affected by modifications to the {@code List} itself. However, it will be affected by modifications to the {@code Parameter} instances in the {@code List}.
	 * 
	 * @return a {@code List} with all {@code Parameter} instances that are currently added to this {@code ParameterList} instance
	 */
	public List<Parameter> getParameters() {
		return new ArrayList<>(this.parameters);
	}
	
	/**
	 * Returns the value of the first {@code String} parameter with a name of {@code name}, or {@code ""} if no such parameter exists.
	 * <p>
	 * If {@code name} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param name the name
	 * @return the value of the first {@code String} parameter with a name of {@code name}, or {@code ""} if no such parameter exists
	 * @throws NullPointerException thrown if, and only if, {@code name} is {@code null}
	 */
	public String getString(final String name) {
		return getString(name, "");
	}
	
	/**
	 * Returns the value of the first {@code String} parameter with a name of {@code name}, or {@code valueDefault} if no such parameter exists.
	 * <p>
	 * If either {@code name} or {@code valueDefault} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param name the name
	 * @param valueDefault the default value
	 * @return the value of the first {@code String} parameter with a name of {@code name}, or {@code valueDefault} if no such parameter exists
	 * @throws NullPointerException thrown if, and only if, either {@code name} or {@code valueDefault} are {@code null}
	 */
	public String getString(final String name, final String valueDefault) {
		Objects.requireNonNull(name, "name == null");
		Objects.requireNonNull(valueDefault, "valueDefault == null");
		
		return this.parameters.stream().filter(doFilterName(name)).filter(doFilterClass(StringParameter.class)).map(parameter -> StringParameter.class.cast(parameter)).map(stringParameter -> stringParameter.getValue()).findFirst().orElse(valueDefault);
	}
	
	/**
	 * Returns the value of the first {@code boolean} parameter with a name of {@code name}, or {@code false} if no such parameter exists.
	 * <p>
	 * If {@code name} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param name the name
	 * @return the value of the first {@code boolean} parameter with a name of {@code name}, or {@code false} if no such parameter exists
	 * @throws NullPointerException thrown if, and only if, {@code name} is {@code null}
	 */
	public boolean getBoolean(final String name) {
		return getBoolean(name, false);
	}
	
	/**
	 * Returns the value of the first {@code boolean} parameter with a name of {@code name}, or {@code valueDefault} if no such parameter exists.
	 * <p>
	 * If {@code name} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param name the name
	 * @param valueDefault the default value
	 * @return the value of the first {@code boolean} parameter with a name of {@code name}, or {@code valueDefault} if no such parameter exists
	 * @throws NullPointerException thrown if, and only if, {@code name} is {@code null}
	 */
	public boolean getBoolean(final String name, final boolean valueDefault) {
		Objects.requireNonNull(name, "name == null");
		
		return this.parameters.stream().filter(doFilterName(name)).filter(doFilterClass(BooleanParameter.class)).map(parameter -> BooleanParameter.class.cast(parameter)).map(booleanParameter -> Boolean.valueOf(booleanParameter.getValue())).findFirst().orElse(Boolean.valueOf(valueDefault)).booleanValue();
	}
	
	/**
	 * Returns the value of the first {@code double} parameter with a name of {@code name}, or {@code 0.0D} if no such parameter exists.
	 * <p>
	 * If {@code name} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param name the name
	 * @return the value of the first {@code double} parameter with a name of {@code name}, or {@code 0.0D} if no such parameter exists
	 * @throws NullPointerException thrown if, and only if, {@code name} is {@code null}
	 */
	public double getDouble(final String name) {
		return getDouble(name, 0.0D);
	}
	
	/**
	 * Returns the value of the first {@code double} parameter with a name of {@code name}, or {@code valueDefault} if no such parameter exists.
	 * <p>
	 * If {@code name} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param name the name
	 * @param valueDefault the default value
	 * @return the value of the first {@code double} parameter with a name of {@code name}, or {@code valueDefault} if no such parameter exists
	 * @throws NullPointerException thrown if, and only if, {@code name} is {@code null}
	 */
	public double getDouble(final String name, final double valueDefault) {
		Objects.requireNonNull(name, "name == null");
		
		return this.parameters.stream().filter(doFilterName(name)).filter(doFilterClass(DoubleParameter.class)).map(parameter -> DoubleParameter.class.cast(parameter)).map(doubleParameter -> Double.valueOf(doubleParameter.getValue())).findFirst().orElse(Double.valueOf(valueDefault)).doubleValue();
	}
	
	/**
	 * Returns the value of the first {@code float} parameter with a name of {@code name}, or {@code 0.0F} if no such parameter exists.
	 * <p>
	 * If {@code name} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param name the name
	 * @return the value of the first {@code float} parameter with a name of {@code name}, or {@code 0.0F} if no such parameter exists
	 * @throws NullPointerException thrown if, and only if, {@code name} is {@code null}
	 */
	public float getFloat(final String name) {
		return getFloat(name, 0.0F);
	}
	
	/**
	 * Returns the value of the first {@code float} parameter with a name of {@code name}, or {@code valueDefault} if no such parameter exists.
	 * <p>
	 * If {@code name} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param name the name
	 * @param valueDefault the default value
	 * @return the value of the first {@code float} parameter with a name of {@code name}, or {@code valueDefault} if no such parameter exists
	 * @throws NullPointerException thrown if, and only if, {@code name} is {@code null}
	 */
	public float getFloat(final String name, final float valueDefault) {
		Objects.requireNonNull(name, "name == null");
		
		return this.parameters.stream().filter(doFilterName(name)).filter(doFilterClass(FloatParameter.class)).map(parameter -> FloatParameter.class.cast(parameter)).map(floatParameter -> Float.valueOf(floatParameter.getValue())).findFirst().orElse(Float.valueOf(valueDefault)).floatValue();
	}
	
	/**
	 * Returns the value of the first {@code int} parameter with a name of {@code name}, or {@code 0} if no such parameter exists.
	 * <p>
	 * If {@code name} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param name the name
	 * @return the value of the first {@code int} parameter with a name of {@code name}, or {@code 0} if no such parameter exists
	 * @throws NullPointerException thrown if, and only if, {@code name} is {@code null}
	 */
	public int getInt(final String name) {
		return getInt(name, 0);
	}
	
	/**
	 * Returns the value of the first {@code int} parameter with a name of {@code name}, or {@code valueDefault} if no such parameter exists.
	 * <p>
	 * If {@code name} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param name the name
	 * @param valueDefault the default value
	 * @return the value of the first {@code int} parameter with a name of {@code name}, or {@code valueDefault} if no such parameter exists
	 * @throws NullPointerException thrown if, and only if, {@code name} is {@code null}
	 */
	public int getInt(final String name, final int valueDefault) {
		Objects.requireNonNull(name, "name == null");
		
		return this.parameters.stream().filter(doFilterName(name)).filter(doFilterClass(IntParameter.class)).map(parameter -> IntParameter.class.cast(parameter)).map(intParameter -> Integer.valueOf(intParameter.getValue())).findFirst().orElse(Integer.valueOf(valueDefault)).intValue();
	}
	
	/**
	 * Adds a {@code boolean} parameter to this {@code ParameterList} instance.
	 * <p>
	 * If {@code name} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * parameterList.addBoolean(name, false);
	 * }
	 * </pre>
	 * 
	 * @param name the name
	 * @throws NullPointerException thrown if, and only if, {@code name} is {@code null}
	 */
	public void addBoolean(final String name) {
		addBoolean(name, false);
	}
	
	/**
	 * Adds a {@code boolean} parameter to this {@code ParameterList} instance.
	 * <p>
	 * If {@code name} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * parameterList.addParameter(new BooleanParameter(name, valueDefault));
	 * }
	 * </pre>
	 * 
	 * @param name the name
	 * @param valueDefault the default value
	 * @throws NullPointerException thrown if, and only if, {@code name} is {@code null}
	 */
	public void addBoolean(final String name, final boolean valueDefault) {
		addParameter(new BooleanParameter(name, valueDefault));
	}
	
	/**
	 * Adds a {@code double} parameter to this {@code ParameterList} instance.
	 * <p>
	 * If {@code name} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * parameterList.addDouble(name, 0.0D);
	 * }
	 * </pre>
	 * 
	 * @param name the name
	 * @throws NullPointerException thrown if, and only if, {@code name} is {@code null}
	 */
	public void addDouble(final String name) {
		addDouble(name, 0.0D);
	}
	
	/**
	 * Adds a {@code double} parameter to this {@code ParameterList} instance.
	 * <p>
	 * If {@code name} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * parameterList.addParameter(new DoubleParameter(name, valueDefault));
	 * }
	 * </pre>
	 * 
	 * @param name the name
	 * @param valueDefault the default value
	 * @throws NullPointerException thrown if, and only if, {@code name} is {@code null}
	 */
	public void addDouble(final String name, final double valueDefault) {
		addParameter(new DoubleParameter(name, valueDefault));
	}
	
	/**
	 * Adds a {@code float} parameter to this {@code ParameterList} instance.
	 * <p>
	 * If {@code name} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * parameterList.addFloat(name, 0.0F);
	 * }
	 * </pre>
	 * 
	 * @param name the name
	 * @throws NullPointerException thrown if, and only if, {@code name} is {@code null}
	 */
	public void addFloat(final String name) {
		addFloat(name, 0.0F);
	}
	
	/**
	 * Adds a {@code float} parameter to this {@code ParameterList} instance.
	 * <p>
	 * If {@code name} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * parameterList.addParameter(new FloatParameter(name, valueDefault));
	 * }
	 * </pre>
	 * 
	 * @param name the name
	 * @param valueDefault the default value
	 * @throws NullPointerException thrown if, and only if, {@code name} is {@code null}
	 */
	public void addFloat(final String name, final float valueDefault) {
		addParameter(new FloatParameter(name, valueDefault));
	}
	
	/**
	 * Adds an {@code int} parameter to this {@code ParameterList} instance.
	 * <p>
	 * If {@code name} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * parameterList.addInt(name, 0);
	 * }
	 * </pre>
	 * 
	 * @param name the name
	 * @throws NullPointerException thrown if, and only if, {@code name} is {@code null}
	 */
	public void addInt(final String name) {
		addInt(name, 0);
	}
	
	/**
	 * Adds an {@code int} parameter to this {@code ParameterList} instance.
	 * <p>
	 * If {@code name} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * parameterList.addParameter(new IntParameter(name, valueDefault));
	 * }
	 * </pre>
	 * 
	 * @param name the name
	 * @param valueDefault the default value
	 * @throws NullPointerException thrown if, and only if, {@code name} is {@code null}
	 */
	public void addInt(final String name, final int valueDefault) {
		addParameter(new IntParameter(name, valueDefault));
	}
	
	/**
	 * Adds {@code parameter} to this {@code ParameterList} instance.
	 * <p>
	 * If {@code parameter} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param parameter the {@link Parameter} instance to add
	 * @throws NullPointerException thrown if, and only if, {@code parameter} is {@code null}
	 */
	public void addParameter(final Parameter parameter) {
		this.parameters.add(Objects.requireNonNull(parameter, "parameter == null"));
	}
	
	/**
	 * Adds a {@code String} parameter to this {@code ParameterList} instance.
	 * <p>
	 * If {@code name} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * parameterList.addString(name, "");
	 * }
	 * </pre>
	 * 
	 * @param name the name
	 * @throws NullPointerException thrown if, and only if, {@code name} is {@code null}
	 */
	public void addString(final String name) {
		addString(name, "");
	}
	
	/**
	 * Adds a {@code String} parameter to this {@code ParameterList} instance.
	 * <p>
	 * If either {@code name} or {@code valueDefault} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * parameterList.addParameter(new StringParameter(name, valueDefault));
	 * }
	 * </pre>
	 * 
	 * @param name the name
	 * @param valueDefault the default value
	 * @throws NullPointerException thrown if, and only if, either {@code name} or {@code valueDefault} are {@code null}
	 */
	public void addString(final String name, final String valueDefault) {
		addParameter(new StringParameter(name, valueDefault));
	}
	
	/**
	 * Loads the parameter values.
	 * <p>
	 * The parameters, that are represented as {@link Parameter} instances, are loaded using the {@link ParameterLoader} instance that is associated with this {@code ParameterList} instance.
	 */
	public void load() {
		this.parameterLoader.load(getParameters());
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static Predicate<Parameter> doFilterClass(final Class<? extends Parameter> clazz) {
		return parameter -> clazz.isAssignableFrom(parameter.getClass());
	}
	
	private static Predicate<Parameter> doFilterName(final String name) {
		return parameter -> parameter.getName().equals(name);
	}
}