/**
 * Copyright 2014 - 2025 J&#246;rgen Lundgren
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
package org.dayflower.wavefront.object;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * An {@code UnsupportedObjectFileStatement} is used to manage Wavefront Object statements that are not supported by this library.
 * <p>
 * This class is not thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class UnsupportedObjectFileStatement implements ObjectFileStatement {
	private final List<String> tokens;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new empty {@code UnsupportedObjectFileStatement} instance.
	 */
	public UnsupportedObjectFileStatement() {
		this.tokens = new ArrayList<>();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code List} of {@code String} with all tokens currently added to this {@code UnsupportedObjectFileStatement} instance.
	 * <p>
	 * Modifying the returned {@code List} will not affect this {@code UnsupportedObjectFileStatement} instance.
	 * 
	 * @return a {@code List} of {@code String} with all tokens currently added to this {@code UnsupportedObjectFileStatement} instance
	 */
	public List<String> getTokens() {
		return new ArrayList<>(this.tokens);
	}
	
	/**
	 * Returns the name of this {@code UnsupportedObjectFileStatement} instance.
	 * 
	 * @return the name of this {@code UnsupportedObjectFileStatement} instance
	 */
	@Override
	public String getName() {
		return this.tokens.stream().findFirst().orElse("");
	}
	
	/**
	 * Returns a {@code String} representation of this {@code UnsupportedObjectFileStatement} instance.
	 * 
	 * @return a {@code String} representation of this {@code UnsupportedObjectFileStatement} instance
	 */
	@Override
	public String toString() {
		return this.tokens.stream().collect(Collectors.joining(" "));
	}
	
	/**
	 * Compares {@code object} to this {@code UnsupportedObjectFileStatement} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code UnsupportedObjectFileStatement}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code UnsupportedObjectFileStatement} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code UnsupportedObjectFileStatement}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof UnsupportedObjectFileStatement)) {
			return false;
		} else if(!Objects.equals(getTokens(), UnsupportedObjectFileStatement.class.cast(object).getTokens())) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns a hash code for this {@code UnsupportedObjectFileStatement} instance.
	 * 
	 * @return a hash code for this {@code UnsupportedObjectFileStatement} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(getTokens());
	}
	
	/**
	 * Adds {@code token} to this {@code UnsupportedObjectFileStatement} instance.
	 * <p>
	 * If {@code token} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param token the token to add
	 * @throws NullPointerException thrown if, and only if, {@code token} is {@code null}
	 */
	public void addToken(final String token) {
		this.tokens.add(Objects.requireNonNull(token, "token == null"));
	}
	
	/**
	 * Removes {@code token} from this {@code UnsupportedObjectFileStatement} instance, if present.
	 * <p>
	 * If {@code token} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param token the token to remove
	 * @throws NullPointerException thrown if, and only if, {@code token} is {@code null}
	 */
	public void removeToken(final String token) {
		this.tokens.remove(Objects.requireNonNull(token, "token == null"));
	}
}