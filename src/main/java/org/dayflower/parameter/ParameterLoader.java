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

import java.util.List;

/**
 * A {@code ParameterLoader} is responsible for loading the values of {@link Parameter} instances.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public interface ParameterLoader {
	/**
	 * Loads the {@link Parameter} instances in the {@code List} {@code parameters}.
	 * <p>
	 * If either {@code parameters} or any of its elements are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param parameters a {@code List} of {@code Parameter} instances to load values for
	 * @throws NullPointerException thrown if, and only if, either {@code parameters} or any of its elements are {@code null}
	 */
	void load(final List<Parameter> parameters);
}