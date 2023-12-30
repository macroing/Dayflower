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
package org.dayflower.scene;

import java.util.Objects;
import java.util.Optional;

/**
 * A {@code ScatteringFunctions} contains the scattering functions {@link BSDF} and {@link BSSRDF}.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class ScatteringFunctions {
	private final BSDF bSDF;
	private final BSSRDF bSSRDF;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code ScatteringFunctions} instance.
	 */
	public ScatteringFunctions() {
		this.bSDF = null;
		this.bSSRDF = null;
	}
	
	/**
	 * Constructs a new {@code ScatteringFunctions} instance.
	 * <p>
	 * If {@code bSDF} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param bSDF a {@link BSDF} instance
	 * @throws NullPointerException thrown if, and only if, {@code bSDF} is {@code null}
	 */
	public ScatteringFunctions(final BSDF bSDF) {
		this.bSDF = Objects.requireNonNull(bSDF, "bSDF == null");
		this.bSSRDF = null;
	}
	
	/**
	 * Constructs a new {@code ScatteringFunctions} instance.
	 * <p>
	 * If either {@code bSDF} or {@code bSSRDF} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param bSDF a {@link BSDF} instance
	 * @param bSSRDF a {@link BSSRDF} instance
	 * @throws NullPointerException thrown if, and only if, either {@code bSDF} or {@code bSSRDF} are {@code null}
	 */
	public ScatteringFunctions(final BSDF bSDF, final BSSRDF bSSRDF) {
		this.bSDF = Objects.requireNonNull(bSDF, "bSDF == null");
		this.bSSRDF = Objects.requireNonNull(bSSRDF, "bSSRDF == null");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the optional {@link BSDF} instance.
	 * 
	 * @return the optional {@code BSDF} instance
	 */
	public Optional<BSDF> getBSDF() {
		return Optional.ofNullable(this.bSDF);
	}
	
	/**
	 * Returns the optional {@link BSSRDF} instance.
	 * 
	 * @return the optional {@code BSSRDF} instance
	 */
	public Optional<BSSRDF> getBSSRDF() {
		return Optional.ofNullable(this.bSSRDF);
	}
	
	/**
	 * Returns a {@code String} representation of this {@code ScatteringFunctions} instance.
	 * 
	 * @return a {@code String} representation of this {@code ScatteringFunctions} instance
	 */
	@Override
	public String toString() {
		return "new ScatteringFunctions(...)";
	}
	
	/**
	 * Compares {@code object} to this {@code ScatteringFunctions} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code ScatteringFunctions}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code ScatteringFunctions} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code ScatteringFunctions}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof ScatteringFunctions)) {
			return false;
		} else if(!Objects.equals(this.bSDF, ScatteringFunctions.class.cast(object).bSDF)) {
			return false;
		} else if(!Objects.equals(this.bSSRDF, ScatteringFunctions.class.cast(object).bSSRDF)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns a hash code for this {@code ScatteringFunctions} instance.
	 * 
	 * @return a hash code for this {@code ScatteringFunctions} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.bSDF, this.bSSRDF);
	}
}