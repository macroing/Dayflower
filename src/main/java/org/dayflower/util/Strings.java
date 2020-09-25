/**
 * Copyright 2020 J&#246;rgen Lundgren
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
package org.dayflower.util;

import java.text.DecimalFormat;
import java.util.Objects;

/**
 * A class that consists exclusively of static methods that returns or performs various operations on {@code String} instances.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class Strings {
	private static final DecimalFormat DECIMAL_FORMAT = doCreateDecimalFormat();
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private Strings() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code String} representation of {@code value} without scientific notation.
	 * 
	 * @param value a {@code float} value
	 * @return a {@code String} representation of {@code value} without scientific notation
	 */
	public static String toNonScientificNotation(final float value) {
		return DECIMAL_FORMAT.format(value).replace(',', '.');
	}
	
	/**
	 * Splits {@code string} on {@code regex} and discards empty substrings.
	 * <p>
	 * Returns a {@code String} array with the result.
	 * <p>
	 * If either {@code string} or {@code regex} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param string a {@code String}
	 * @param regex the {@code String} to split with
	 * @return a {@code String} array with the result
	 * @throws NullPointerException thrown if, and only if, either {@code string} or {@code regex} are {@code null}
	 */
	public static String[] splitAndDiscardEmptySubstrings(final String string, final String regex) {
		final String[] oldStrings = string.split(Objects.requireNonNull(regex, "regex == null"));
		
		int length = 0;
		
		for(int i = 0; i < oldStrings.length; i++) {
			if(oldStrings[i].isEmpty()) {
				oldStrings[i] = null;
			} else {
				length++;
			}
		}
		
		final String[] newStrings = new String[length];
		
		for(int i = 0, j = 0; i < oldStrings.length; i++) {
			if(oldStrings[i] != null) {
				newStrings[j++] = oldStrings[i];
			}
		}
		
		return newStrings;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static DecimalFormat doCreateDecimalFormat() {
		final
		DecimalFormat decimalFormat = new DecimalFormat("#");
		decimalFormat.setDecimalSeparatorAlwaysShown(true);
		decimalFormat.setMaximumFractionDigits(8);
		decimalFormat.setMinimumFractionDigits(1);
		decimalFormat.setMinimumIntegerDigits(1);
		
		return decimalFormat;
	}
}