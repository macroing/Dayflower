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
package org.dayflower.scene;

import java.util.Objects;

/**
 * A {@code BXDF} represents a BRDF (Bidirectional Reflectance Distribution Function) or a BTDF (Bidirectional Transmittance Distribution Function).
 * <p>
 * All official implementations of this class are immutable and therefore thread-safe. But this cannot be guaranteed for all implementations.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public abstract class BXDF {
	private final BXDFType bXDFType;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code BXDF} instance.
	 * <p>
	 * If {@code bXDFType} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param bXDFType a {@link BXDFType} that contains information about the behaviour for this {@code BXDF} instance
	 * @throws NullPointerException thrown if, and only if, {@code bXDFType} is {@code null}
	 */
	protected BXDF(final BXDFType bXDFType) {
		this.bXDFType = Objects.requireNonNull(bXDFType, "bXDFType == null");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link BXDFType} that contains information about the behaviour for this {@code BXDF} instance.
	 * 
	 * @return a {@code BXDFType} that contains information about the behaviour for this {@code BXDF} instance
	 */
	public final BXDFType getBXDFType() {
		return this.bXDFType;
	}
}