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
package org.dayflower.scene.pbrt;

import java.util.Objects;

/**
 * A {@code BXDFType} contains information about the behaviour for a BRDF (Bidirectional Reflectance Distribution Function) or a BTDF (Bidirectional Transmittance Distribution Function).
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class BXDFType {
	private final boolean hasReflection;
	private final boolean hasTransmission;
	private final boolean isDiffuse;
	private final boolean isGlossy;
	private final boolean isSpecular;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private BXDFType(final boolean hasReflection, final boolean hasTransmission, final boolean isDiffuse, final boolean isGlossy, final boolean isSpecular) {
		this.hasReflection = hasReflection;
		this.hasTransmission = hasTransmission;
		this.isDiffuse = isDiffuse;
		this.isGlossy = isGlossy;
		this.isSpecular = isSpecular;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code String} representation of this {@code BXDFType} instance.
	 * 
	 * @return a {@code String} representation of this {@code BXDFType} instance
	 */
	@Override
	public String toString() {
		if(isDiffuse() && hasReflection() && hasTransmission()) {
			return "BXDFType.createDiffuseReflectionAndTransmission()";
		} else if(isDiffuse() && hasReflection()) {
			return "BXDFType.createDiffuseReflection()";
		} else if(isDiffuse() && hasTransmission()) {
			return "BXDFType.createDiffuseTransmission()";
		} else if(isGlossy() && hasReflection() && hasTransmission()) {
			return "BXDFType.createGlossyReflectionAndTransmission()";
		} else if(isGlossy() && hasReflection()) {
			return "BXDFType.createGlossyReflection()";
		} else if(isGlossy() && hasTransmission()) {
			return "BXDFType.createGlossyTransmission()";
		} else if(isSpecular() && hasReflection() && hasTransmission()) {
			return "BXDFType.createSpecularReflectionAndTransmission()";
		} else if(isSpecular() && hasReflection()) {
			return "BXDFType.createSpecularReflection()";
		} else if(isSpecular() && hasTransmission()) {
			return "BXDFType.createSpecularTransmission()";
		} else {
			return "";
		}
	}
	
	/**
	 * Compares {@code object} to this {@code BXDFType} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code BXDFType}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code BXDFType} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code BXDFType}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof BXDFType)) {
			return false;
		} else if(this.hasReflection != BXDFType.class.cast(object).hasReflection) {
			return false;
		} else if(this.hasTransmission != BXDFType.class.cast(object).hasTransmission) {
			return false;
		} else if(this.isDiffuse != BXDFType.class.cast(object).isDiffuse) {
			return false;
		} else if(this.isGlossy != BXDFType.class.cast(object).isGlossy) {
			return false;
		} else if(this.isSpecular != BXDFType.class.cast(object).isSpecular) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code BXDFType} has reflection, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, this {@code BXDFType} has reflection, {@code false} otherwise
	 */
	public boolean hasReflection() {
		return this.hasReflection;
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code BXDFType} has transmission, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, this {@code BXDFType} has transmission, {@code false} otherwise
	 */
	public boolean hasTransmission() {
		return this.hasTransmission;
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code BXDFType} is diffuse, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, this {@code BXDFType} is diffuse, {@code false} otherwise
	 */
	public boolean isDiffuse() {
		return this.isDiffuse;
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code BXDFType} is glossy, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, this {@code BXDFType} is glossy, {@code false} otherwise
	 */
	public boolean isGlossy() {
		return this.isGlossy;
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code BXDFType} is specular, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, this {@code BXDFType} is specular, {@code false} otherwise
	 */
	public boolean isSpecular() {
		return this.isSpecular;
	}
	
	/**
	 * Returns a hash code for this {@code BXDFType} instance.
	 * 
	 * @return a hash code for this {@code BXDFType} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(Boolean.valueOf(this.hasReflection), Boolean.valueOf(this.hasTransmission), Boolean.valueOf(this.isDiffuse), Boolean.valueOf(this.isGlossy), Boolean.valueOf(this.isSpecular));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a diffuse {@code BXDFType} with reflection.
	 * 
	 * @return a diffuse {@code BXDFType} with reflection
	 */
	public static BXDFType createDiffuseReflection() {
		return doCreateReflection(true, false, false);
	}
	
	/**
	 * Returns a diffuse {@code BXDFType} with reflection and transmission.
	 * 
	 * @return a diffuse {@code BXDFType} with reflection and transmission
	 */
	public static BXDFType createDiffuseReflectionAndTransmission() {
		return doCreateReflectionAndTransmission(true, false, false);
	}
	
	/**
	 * Returns a diffuse {@code BXDFType} with transmission.
	 * 
	 * @return a diffuse {@code BXDFType} with transmission
	 */
	public static BXDFType createDiffuseTransmission() {
		return doCreateTransmission(true, false, false);
	}
	
	/**
	 * Returns a glossy {@code BXDFType} with reflection.
	 * 
	 * @return a glossy {@code BXDFType} with reflection
	 */
	public static BXDFType createGlossyReflection() {
		return doCreateReflection(false, true, false);
	}
	
	/**
	 * Returns a glossy {@code BXDFType} with reflection and transmission.
	 * 
	 * @return a glossy {@code BXDFType} with reflection and transmission
	 */
	public static BXDFType createGlossyReflectionAndTransmission() {
		return doCreateReflectionAndTransmission(false, true, false);
	}
	
	/**
	 * Returns a glossy {@code BXDFType} with transmission.
	 * 
	 * @return a glossy {@code BXDFType} with transmission
	 */
	public static BXDFType createGlossyTransmission() {
		return doCreateTransmission(false, true, false);
	}
	
	/**
	 * Returns a specular {@code BXDFType} with reflection.
	 * 
	 * @return a specular {@code BXDFType} with reflection
	 */
	public static BXDFType createSpecularReflection() {
		return doCreateReflection(false, false, true);
	}
	
	/**
	 * Returns a specular {@code BXDFType} with reflection and transmission.
	 * 
	 * @return a specular {@code BXDFType} with reflection and transmission
	 */
	public static BXDFType createSpecularReflectionAndTransmission() {
		return doCreateReflectionAndTransmission(false, false, true);
	}
	
	/**
	 * Returns a specular {@code BXDFType} with transmission.
	 * 
	 * @return a specular {@code BXDFType} with transmission
	 */
	public static BXDFType createSpecularTransmission() {
		return doCreateTransmission(false, false, true);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static BXDFType doCreateReflection(final boolean isDiffuse, final boolean isGlossy, final boolean isSpecular) {
		return new BXDFType(true, false, isDiffuse, isGlossy, isSpecular);
	}
	
	private static BXDFType doCreateReflectionAndTransmission(final boolean isDiffuse, final boolean isGlossy, final boolean isSpecular) {
		return new BXDFType(true, true, isDiffuse, isGlossy, isSpecular);
	}
	
	private static BXDFType doCreateTransmission(final boolean isDiffuse, final boolean isGlossy, final boolean isSpecular) {
		return new BXDFType(false, true, isDiffuse, isGlossy, isSpecular);
	}
}