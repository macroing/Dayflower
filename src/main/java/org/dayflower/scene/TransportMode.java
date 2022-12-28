/**
 * Copyright 2014 - 2023 J&#246;rgen Lundgren
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
package org.dayflower.scene;

/**
 * A {@code TransportMode} represents a transport mode.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public enum TransportMode {
	/**
	 * A {@code TransportMode} instance for importance.
	 */
	IMPORTANCE,
	
	/**
	 * A {@code TransportMode} instance for radiance.
	 */
	RADIANCE;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private TransportMode() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code String} representation of this {@code TransportMode} instance.
	 * 
	 * @return a {@code String} representation of this {@code TransportMode} instance
	 */
	@Override
	public String toString() {
		switch(this) {
			case IMPORTANCE:
				return "TransportMode.IMPORTANCE";
			case RADIANCE:
				return "TransportMode.RADIANCE";
			default:
				return "";
		}
	}
}