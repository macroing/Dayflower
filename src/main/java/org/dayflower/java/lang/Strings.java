/**
 * Copyright 2014 - 2022 J&#246;rgen Lundgren
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
package org.dayflower.java.lang;

import java.text.DecimalFormat;
import java.util.Objects;

/**
 * A class that consists exclusively of static methods that returns or performs various operations on {@code String} instances.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class Strings {
	private static final DecimalFormat DECIMAL_FORMAT_DOUBLE = doCreateDecimalFormat(16);
	private static final DecimalFormat DECIMAL_FORMAT_FLOAT = doCreateDecimalFormat(8);
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private Strings() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code String} instance with {@code string} repeated {@code count} times.
	 * <p>
	 * If {@code string} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code count} is less than {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param string the {@code String} instance to repeat
	 * @param count the repetition count
	 * @return a {@code String} instance with {@code string} repeated {@code count} times
	 * @throws IllegalArgumentException thrown if, and only if, {@code count} is less than {@code 0}
	 * @throws NullPointerException thrown if, and only if, {@code string} is {@code null}
	 */
	public static String repeat(final String string, final int count) {
		Objects.requireNonNull(string, "string == null");
		
		if(count < 0) {
			throw new IllegalArgumentException(String.format("count < 0: count == %d", Integer.valueOf(count)));
		}
		
		final StringBuilder stringBuilder = new StringBuilder();
		
		for(int i = 0; i < count; i++) {
			stringBuilder.append(string);
		}
		
		return stringBuilder.toString();
	}
	
	/**
	 * Returns a {@code String} representation of {@code value} without scientific notation.
	 * 
	 * @param value a {@code double} value
	 * @return a {@code String} representation of {@code value} without scientific notation
	 */
	public static String toNonScientificNotation(final double value) {
		return DECIMAL_FORMAT_DOUBLE.format(value).replace(',', '.');
	}
	
	/**
	 * Returns a {@code String} representation of {@code value} without scientific notation.
	 * 
	 * @param value a {@code float} value
	 * @return a {@code String} representation of {@code value} without scientific notation
	 */
	public static String toNonScientificNotation(final float value) {
		return DECIMAL_FORMAT_FLOAT.format(value).replace(',', '.');
	}
	
	/**
	 * Returns a {@code String} representation of {@code value} without scientific notation and as a Java {@code double} literal.
	 * 
	 * @param value a {@code double} value
	 * @return a {@code String} representation of {@code value} without scientific notation and as a Java {@code double} literal
	 */
	public static String toNonScientificNotationJava(final double value) {
		if(Double.isNaN(value)) {
			return "Double.NaN";
		} else if(value == Double.NEGATIVE_INFINITY) {
			return "Double.NEGATIVE_INFINITY";
		} else if(value == Double.POSITIVE_INFINITY) {
			return "Double.POSITIVE_INFINITY";
		} else {
			return DECIMAL_FORMAT_DOUBLE.format(value).replace(',', '.') + "D";
		}
	}
	
	/**
	 * Returns a {@code String} representation of {@code value} without scientific notation and as a Java {@code float} literal.
	 * 
	 * @param value a {@code float} value
	 * @return a {@code String} representation of {@code value} without scientific notation and as a Java {@code float} literal
	 */
	public static String toNonScientificNotationJava(final float value) {
		if(Float.isNaN(value)) {
			return "Float.NaN";
		} else if(value == Float.NEGATIVE_INFINITY) {
			return "Float.NEGATIVE_INFINITY";
		} else if(value == Float.POSITIVE_INFINITY) {
			return "Float.POSITIVE_INFINITY";
		} else {
			return DECIMAL_FORMAT_FLOAT.format(value).replace(',', '.') + "F";
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static DecimalFormat doCreateDecimalFormat(final int maximumFractionDigits) {
		final
		DecimalFormat decimalFormat = new DecimalFormat("#");
		decimalFormat.setDecimalSeparatorAlwaysShown(true);
		decimalFormat.setMaximumFractionDigits(maximumFractionDigits);
		decimalFormat.setMinimumFractionDigits(1);
		decimalFormat.setMinimumIntegerDigits(1);
		
		return decimalFormat;
	}
}