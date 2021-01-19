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
package org.dayflower.utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A {@code TextLine} takes a {@code String} and tokenizes it into individual tokens (or substrings if you will). These individual tokens can then be retrieved by various methods.
 * <p>
 * The tokenization process is performed by a single call to {@link Strings#splitAndDiscardEmptySubstrings(String, String)}.
 * <p>
 * The length of a {@code TextLine} does not refer to the length of the initial {@code String} before tokenization. It does not either refer to the combined length of each individual token. What it does refer to is the length of the underlying array
 * that stores each individual token.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class TextLine {
	private final String[] tokens;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code TextLine} instance for {@code string} and the Regex pattern {@code "\\s"}.
	 * <p>
	 * Calling this constructor is equivalent to {@code new TextLine(string, "\\s")}.
	 * <p>
	 * If {@code string} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param string a {@code String} that will be split on matches of whitespace ({@code "\\s"})
	 * @throws NullPointerException thrown if, and only if, {@code string} is {@code null}
	 */
	public TextLine(final String string) {
		this(string, "\\s");
	}
	
	/**
	 * Constructs a new {@code TextLine} instance for {@code string} and {@code regex}.
	 * <p>
	 * If either {@code string} or {@code regex} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param string a {@code String} that will be split on matches of {@code regex}
	 * @param regex the Regex pattern to split {@code string} with
	 * @throws NullPointerException thrown if, and only if, either {@code string} or {@code regex} are {@code null}
	 */
	public TextLine(final String string, final String regex) {
		this.tokens = Strings.splitAndDiscardEmptySubstrings(string, regex);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code List} of {@code String} with all tokens in this {@code TextLine} instance.
	 * <p>
	 * Modifying the returned {@code List} will not affect this {@code TextLine} instance.
	 * 
	 * @return a {@code List} of {@code String} with all tokens in this {@code TextLine} instance
	 */
	public List<String> getTokens() {
		return new ArrayList<>(Arrays.asList(this.tokens));
	}
	
	/**
	 * Returns the token at index {@code index} as a {@code String} if it exists, {@code defaultValue} otherwise.
	 * 
	 * @param index an index
	 * @param defaultValue a default {@code String} value
	 * @return the token at index {@code index} as a {@code String} if it exists, {@code defaultValue} otherwise
	 */
	public String getString(final int index, final String defaultValue) {
		return hasString(index) ? this.tokens[index] : defaultValue;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code TextLine} instance.
	 * 
	 * @return a {@code String} representation of this {@code TextLine} instance
	 */
	@Override
	public String toString() {
		return Arrays.toString(this.tokens);
	}
	
	/**
	 * Returns {@code true} if, and only if, a token at index {@code index} exists and it can be converted to a {@code float}, {@code false} otherwise.
	 * 
	 * @param index an index
	 * @return {@code true} if, and only if, a token at index {@code index} exists and it can be converted to a {@code float}, {@code false} otherwise
	 */
	public boolean hasFloat(final int index) {
		try {
			return index >= 0 && index < this.tokens.length && Float.parseFloat(this.tokens[index]) != Float.NaN;
		} catch(final NumberFormatException e) {
			return false;
		}
	}
	
	/**
	 * Returns {@code true} if, and only if, a token at index {@code index} exists and it can be converted to a {@code float} that is considered equal to {@code value}, {@code false} otherwise.
	 * <p>
	 * The comparison is performed by {@code Float.compare(float, float)}.
	 * 
	 * @param index an index
	 * @param value a {@code float} value
	 * @return {@code true} if, and only if, a token at index {@code index} exists and it can be converted to a {@code float} that is considered equal to {@code value}, {@code false} otherwise
	 */
	public boolean hasFloat(final int index, final float value) {
		try {
			return index >= 0 && index < this.tokens.length && Float.compare(Float.parseFloat(this.tokens[index]), value) == 0;
		} catch(final NumberFormatException e) {
			return false;
		}
	}
	
	/**
	 * Returns {@code true} if, and only if, a token at index {@code index} exists and it can be converted to an {@code int}, {@code false} otherwise.
	 * 
	 * @param index an index
	 * @return {@code true} if, and only if, a token at index {@code index} exists and it can be converted to an {@code int}, {@code false} otherwise
	 */
	public boolean hasInt(final int index) {
		try {
			return index >= 0 && index < this.tokens.length && Integer.parseInt(this.tokens[index]) >= Integer.MIN_VALUE;
		} catch(final NumberFormatException e) {
			return false;
		}
	}
	
	/**
	 * Returns {@code true} if, and only if, a token at index {@code index} exists and it can be converted to an {@code int} that is considered equal to {@code value}, {@code false} otherwise.
	 * <p>
	 * The comparison is performed by the default {@code ==} comparison.
	 * 
	 * @param index an index
	 * @param value an {@code int} value
	 * @return {@code true} if, and only if, a token at index {@code index} exists and it can be converted to an {@code int} that is considered equal to {@code value}, {@code false} otherwise
	 */
	public boolean hasInt(final int index, final int value) {
		try {
			return index >= 0 && index < this.tokens.length && Integer.parseInt(this.tokens[index]) == value;
		} catch(final NumberFormatException e) {
			return false;
		}
	}
	
	/**
	 * Returns {@code true} if, and only if, a token at index {@code index} exists, {@code false} otherwise.
	 * 
	 * @param index an index
	 * @return {@code true} if, and only if, a token at index {@code index} exists, {@code false} otherwise
	 */
	public boolean hasString(final int index) {
		return index >= 0 && index < this.tokens.length;
	}
	
	/**
	 * Returns {@code true} if, and only if, a token at index {@code index} exists and it is considered equal to {@code value}, {@code false} otherwise.
	 * <p>
	 * The comparison is performed by the default {@code Object.equals(Object)} comparison.
	 * 
	 * @param index an index
	 * @param value a {@code String} value
	 * @return {@code true} if, and only if, a token at index {@code index} exists and it is considered equal to {@code value}, {@code false} otherwise
	 */
	public boolean hasString(final int index, final String value) {
		return index >= 0 && index < this.tokens.length && value.equals(this.tokens[index]);
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code TextLine} is empty, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, this {@code TextLine} is empty, {@code false} otherwise
	 */
	public boolean isEmpty() {
		return this.tokens.length == 0;
	}
	
	/**
	 * Returns the token at index {@code index} as a {@code float} if it exists and is convertible, {@code defaultValue} otherwise.
	 * 
	 * @param index an index
	 * @param defaultValue a default {@code float} value
	 * @return the token at index {@code index} as a {@code float} if it exists and is convertible, {@code defaultValue} otherwise
	 */
	public float getFloat(final int index, final float defaultValue) {
		return hasFloat(index) ? Float.parseFloat(this.tokens[index]) : defaultValue;
	}
	
	/**
	 * Returns the token at index {@code index} as an {@code int} if it exists and is convertible, {@code defaultValue} otherwise.
	 * 
	 * @param index an index
	 * @param defaultValue a default {@code int} value
	 * @return the token at index {@code index} as an {@code int} if it exists and is convertible, {@code defaultValue} otherwise
	 */
	public int getInt(final int index, final int defaultValue) {
		return hasInt(index) ? Integer.parseInt(this.tokens[index]) : defaultValue;
	}
	
	/**
	 * Returns the length of this {@code TextLine}.
	 * <p>
	 * The length of a {@code TextLine} does not refer to the length of the initial {@code String} before tokenization. It does not either refer to the combined length of each individual token. What it does refer to is the length of the underlying
	 * array that stores each individual token.
	 * 
	 * @return the length of this {@code TextLine}
	 */
	public int length() {
		return this.tokens.length;
	}
}