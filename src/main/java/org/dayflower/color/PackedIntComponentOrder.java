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
package org.dayflower.color;

/**
 * A {@code PackedIntComponentOrder} is used to tell us what order the R-, G-, B- and A-components are stored in an {@code int}, in a packed form.
 * <p>
 * The names of the constants in this enum signifies the order of the components, from most significant byte to least significant byte.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public enum PackedIntComponentOrder {
	/**
	 * A {@code PackedIntComponentOrder} that stores the A-, B-, G- and R-components, in that order, from most significant byte to least significant byte.
	 * <p>
	 * The components are stored in the following way:
	 * <pre>
	 * {@code
	 * int a = (colorABGR >> 24) & 0xFF;
	 * int b = (colorABGR >> 16) & 0xFF;
	 * int g = (colorABGR >>  8) & 0xFF;
	 * int r = (colorABGR >>  0) & 0xFF;
	 * }
	 * </pre>
	 */
	ABGR(0, 8, 16, 24),
	
	/**
	 * A {@code PackedIntComponentOrder} that stores the A-, R-, G- and B-components, in that order, from most significant byte to least significant byte.
	 * <p>
	 * The components are stored in the following way:
	 * <pre>
	 * {@code
	 * int a = (colorARGB >> 24) & 0xFF;
	 * int r = (colorARGB >> 16) & 0xFF;
	 * int g = (colorARGB >>  8) & 0xFF;
	 * int b = (colorARGB >>  0) & 0xFF;
	 * }
	 * </pre>
	 */
	ARGB(16, 8, 0, 24),
	
	/**
	 * A {@code PackedIntComponentOrder} that stores the B-, G- and R-components, in that order, from most significant byte to least significant byte.
	 * <p>
	 * The components are stored in the following way:
	 * <pre>
	 * {@code
	 * int b = (colorBGR >> 16) & 0xFF;
	 * int g = (colorBGR >>  8) & 0xFF;
	 * int r = (colorBGR >>  0) & 0xFF;
	 * }
	 * </pre>
	 */
	BGR(0, 8, 16, -1),
	
	/**
	 * A {@code PackedIntComponentOrder} that stores the R-, G- and B-components, in that order, from most significant byte to least significant byte.
	 * <p>
	 * The components are stored in the following way:
	 * <pre>
	 * {@code
	 * int r = (colorRGB >> 16) & 0xFF;
	 * int g = (colorRGB >>  8) & 0xFF;
	 * int b = (colorRGB >>  0) & 0xFF;
	 * }
	 * </pre>
	 */
	RGB(16, 8, 0, -1);
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final int shiftA;
	private final int shiftB;
	private final int shiftG;
	private final int shiftR;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private PackedIntComponentOrder(final int shiftR, final int shiftG, final int shiftB, final int shiftA) {
		this.shiftR = shiftR;
		this.shiftG = shiftG;
		this.shiftB = shiftB;
		this.shiftA = shiftA;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns {@code true} if, and only if, this {@code PackedIntComponentOrder} has a shift for the A-component, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, this {@code PackedIntComponentOrder} has a shift for the A-component, {@code false} otherwise
	 */
	public boolean hasShiftA() {
		return this.shiftA != -1;
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code PackedIntComponentOrder} has a shift for the B-component, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, this {@code PackedIntComponentOrder} has a shift for the B-component, {@code false} otherwise
	 */
	public boolean hasShiftB() {
		return this.shiftB != -1;
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code PackedIntComponentOrder} has a shift for the G-component, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, this {@code PackedIntComponentOrder} has a shift for the G-component, {@code false} otherwise
	 */
	public boolean hasShiftG() {
		return this.shiftG != -1;
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code PackedIntComponentOrder} has a shift for the R-component, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, this {@code PackedIntComponentOrder} has a shift for the R-component, {@code false} otherwise
	 */
	public boolean hasShiftR() {
		return this.shiftR != -1;
	}
	
	/**
	 * Returns the component count of this {@code PackedIntComponentOrder} instance.
	 * 
	 * @return the component count of this {@code PackedIntComponentOrder} instance
	 */
	public int getComponentCount() {
		return (hasShiftR() ? 1 : 0) + (hasShiftG() ? 1 : 0) + (hasShiftB() ? 1 : 0) + (hasShiftA() ? 1 : 0);
	}
	
	/**
	 * Returns the shift for the A-component, or {@code -1} if it does not have one.
	 * 
	 * @return the shift for the A-component, or {@code -1} if it does not have one
	 */
	public int getShiftA() {
		return this.shiftA;
	}
	
	/**
	 * Returns the shift for the B-component, or {@code -1} if it does not have one.
	 * 
	 * @return the shift for the B-component, or {@code -1} if it does not have one
	 */
	public int getShiftB() {
		return this.shiftB;
	}
	
	/**
	 * Returns the shift for the G-component, or {@code -1} if it does not have one.
	 * 
	 * @return the shift for the G-component, or {@code -1} if it does not have one
	 */
	public int getShiftG() {
		return this.shiftG;
	}
	
	/**
	 * Returns the shift for the R-component, or {@code -1} if it does not have one.
	 * 
	 * @return the shift for the R-component, or {@code -1} if it does not have one
	 */
	public int getShiftR() {
		return this.shiftR;
	}
	
	/**
	 * Returns an {@code int} with {@code r}, {@code g} and {@code b} in a packed form.
	 * 
	 * @param r the R-component
	 * @param g the G-component
	 * @param b the B-component
	 * @return an {@code int} with {@code r}, {@code g} and {@code b} in a packed form
	 */
	public int pack(final int r, final int g, final int b) {
		return (hasShiftR() ? ((r & 0xFF) << getShiftR()) : 0) | (hasShiftG() ? ((g & 0xFF) << getShiftG()) : 0) | (hasShiftB() ? ((b & 0xFF) << getShiftB()) : 0);
	}
	
	/**
	 * Returns an {@code int} with {@code r}, {@code g}, {@code b} and {@code a} in a packed form.
	 * 
	 * @param r the R-component
	 * @param g the G-component
	 * @param b the B-component
	 * @param a the A-component
	 * @return an {@code int} with {@code r}, {@code g}, {@code b} and {@code a} in a packed form
	 */
	public int pack(final int r, final int g, final int b, final int a) {
		return (hasShiftR() ? ((r & 0xFF) << getShiftR()) : 0) | (hasShiftG() ? ((g & 0xFF) << getShiftG()) : 0) | (hasShiftB() ? ((b & 0xFF) << getShiftB()) : 0) | (hasShiftA() ? ((a & 0xFF) << getShiftA()) : 0);
	}
	
	/**
	 * Returns an {@code int} with the unpacked A-component, or {@code 255} if it could not unpack.
	 * 
	 * @param color an {@code int} with the components in packed form
	 * @return an {@code int} with the unpacked A-component, or {@code 255} if it could not unpack
	 */
	public int unpackA(final int color) {
		return hasShiftA() ? (color >> getShiftA()) & 0xFF : 255;
	}
	
	/**
	 * Returns an {@code int} with the unpacked B-component, or {@code 0} if it could not unpack.
	 * 
	 * @param color an {@code int} with the components in packed form
	 * @return an {@code int} with the unpacked B-component, or {@code 0} if it could not unpack
	 */
	public int unpackB(final int color) {
		return hasShiftB() ? (color >> getShiftB()) & 0xFF : 0;
	}
	
	/**
	 * Returns an {@code int} with the unpacked G-component, or {@code 0} if it could not unpack.
	 * 
	 * @param color an {@code int} with the components in packed form
	 * @return an {@code int} with the unpacked G-component, or {@code 0} if it could not unpack
	 */
	public int unpackG(final int color) {
		return hasShiftG() ? (color >> getShiftG()) & 0xFF : 0;
	}
	
	/**
	 * Returns an {@code int} with the unpacked R-component, or {@code 0} if it could not unpack.
	 * 
	 * @param color an {@code int} with the components in packed form
	 * @return an {@code int} with the unpacked R-component, or {@code 0} if it could not unpack
	 */
	public int unpackR(final int color) {
		return hasShiftR() ? (color >> getShiftR()) & 0xFF : 0;
	}
}