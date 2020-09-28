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
 * A {@code PBRTBXDFType} contains information about the behaviour for a BRDF (Bidirectional Reflectance Distribution Function) or a BTDF (Bidirectional Transmittance Distribution Function).
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class PBRTBXDFType {
	private final boolean hasReflection;
	private final boolean hasTransmission;
	private final boolean isDiffuse;
	private final boolean isGlossy;
	private final boolean isSpecular;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private PBRTBXDFType(final boolean hasReflection, final boolean hasTransmission, final boolean isDiffuse, final boolean isGlossy, final boolean isSpecular) {
		this.hasReflection = hasReflection;
		this.hasTransmission = hasTransmission;
		this.isDiffuse = isDiffuse;
		this.isGlossy = isGlossy;
		this.isSpecular = isSpecular;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code String} representation of this {@code PBRTBXDFType} instance.
	 * 
	 * @return a {@code String} representation of this {@code PBRTBXDFType} instance
	 */
	@Override
	public String toString() {
		if(isDiffuse() && hasReflection() && hasTransmission()) {
			return "PBRTBXDFType.createDiffuseReflectionAndTransmission()";
		} else if(isDiffuse() && hasReflection()) {
			return "PBRTBXDFType.createDiffuseReflection()";
		} else if(isDiffuse() && hasTransmission()) {
			return "PBRTBXDFType.createDiffuseTransmission()";
		} else if(isGlossy() && hasReflection() && hasTransmission()) {
			return "PBRTBXDFType.createGlossyReflectionAndTransmission()";
		} else if(isGlossy() && hasReflection()) {
			return "PBRTBXDFType.createGlossyReflection()";
		} else if(isGlossy() && hasTransmission()) {
			return "PBRTBXDFType.createGlossyTransmission()";
		} else if(isSpecular() && hasReflection() && hasTransmission()) {
			return "PBRTBXDFType.createSpecularReflectionAndTransmission()";
		} else if(isSpecular() && hasReflection()) {
			return "PBRTBXDFType.createSpecularReflection()";
		} else if(isSpecular() && hasTransmission()) {
			return "PBRTBXDFType.createSpecularTransmission()";
		} else {
			return "";
		}
	}
	
	/**
	 * Compares {@code object} to this {@code PBRTBXDFType} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code PBRTBXDFType}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code PBRTBXDFType} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code PBRTBXDFType}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof PBRTBXDFType)) {
			return false;
		} else if(this.hasReflection != PBRTBXDFType.class.cast(object).hasReflection) {
			return false;
		} else if(this.hasTransmission != PBRTBXDFType.class.cast(object).hasTransmission) {
			return false;
		} else if(this.isDiffuse != PBRTBXDFType.class.cast(object).isDiffuse) {
			return false;
		} else if(this.isGlossy != PBRTBXDFType.class.cast(object).isGlossy) {
			return false;
		} else if(this.isSpecular != PBRTBXDFType.class.cast(object).isSpecular) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code PBRTBXDFType} has reflection, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, this {@code PBRTBXDFType} has reflection, {@code false} otherwise
	 */
	public boolean hasReflection() {
		return this.hasReflection;
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code PBRTBXDFType} has transmission, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, this {@code PBRTBXDFType} has transmission, {@code false} otherwise
	 */
	public boolean hasTransmission() {
		return this.hasTransmission;
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code PBRTBXDFType} is diffuse, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, this {@code PBRTBXDFType} is diffuse, {@code false} otherwise
	 */
	public boolean isDiffuse() {
		return this.isDiffuse;
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code PBRTBXDFType} is glossy, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, this {@code PBRTBXDFType} is glossy, {@code false} otherwise
	 */
	public boolean isGlossy() {
		return this.isGlossy;
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code PBRTBXDFType} is specular, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, this {@code PBRTBXDFType} is specular, {@code false} otherwise
	 */
	public boolean isSpecular() {
		return this.isSpecular;
	}
	
	/**
	 * Returns a hash code for this {@code PBRTBXDFType} instance.
	 * 
	 * @return a hash code for this {@code PBRTBXDFType} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(Boolean.valueOf(this.hasReflection), Boolean.valueOf(this.hasTransmission), Boolean.valueOf(this.isDiffuse), Boolean.valueOf(this.isGlossy), Boolean.valueOf(this.isSpecular));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a diffuse {@code PBRTBXDFType} with reflection.
	 * 
	 * @return a diffuse {@code PBRTBXDFType} with reflection
	 */
	public static PBRTBXDFType createDiffuseReflection() {
		return doCreateReflection(true, false, false);
	}
	
	/**
	 * Returns a diffuse {@code PBRTBXDFType} with reflection and transmission.
	 * 
	 * @return a diffuse {@code PBRTBXDFType} with reflection and transmission
	 */
	public static PBRTBXDFType createDiffuseReflectionAndTransmission() {
		return doCreateReflectionAndTransmission(true, false, false);
	}
	
	/**
	 * Returns a diffuse {@code PBRTBXDFType} with transmission.
	 * 
	 * @return a diffuse {@code PBRTBXDFType} with transmission
	 */
	public static PBRTBXDFType createDiffuseTransmission() {
		return doCreateTransmission(true, false, false);
	}
	
	/**
	 * Returns a glossy {@code PBRTBXDFType} with reflection.
	 * 
	 * @return a glossy {@code PBRTBXDFType} with reflection
	 */
	public static PBRTBXDFType createGlossyReflection() {
		return doCreateReflection(false, true, false);
	}
	
	/**
	 * Returns a glossy {@code PBRTBXDFType} with reflection and transmission.
	 * 
	 * @return a glossy {@code PBRTBXDFType} with reflection and transmission
	 */
	public static PBRTBXDFType createGlossyReflectionAndTransmission() {
		return doCreateReflectionAndTransmission(false, true, false);
	}
	
	/**
	 * Returns a glossy {@code PBRTBXDFType} with transmission.
	 * 
	 * @return a glossy {@code PBRTBXDFType} with transmission
	 */
	public static PBRTBXDFType createGlossyTransmission() {
		return doCreateTransmission(false, true, false);
	}
	
	/**
	 * Returns a specular {@code PBRTBXDFType} with reflection.
	 * 
	 * @return a specular {@code PBRTBXDFType} with reflection
	 */
	public static PBRTBXDFType createSpecularReflection() {
		return doCreateReflection(false, false, true);
	}
	
	/**
	 * Returns a specular {@code PBRTBXDFType} with reflection and transmission.
	 * 
	 * @return a specular {@code PBRTBXDFType} with reflection and transmission
	 */
	public static PBRTBXDFType createSpecularReflectionAndTransmission() {
		return doCreateReflectionAndTransmission(false, false, true);
	}
	
	/**
	 * Returns a specular {@code PBRTBXDFType} with transmission.
	 * 
	 * @return a specular {@code PBRTBXDFType} with transmission
	 */
	public static PBRTBXDFType createSpecularTransmission() {
		return doCreateTransmission(false, false, true);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static PBRTBXDFType doCreateReflection(final boolean isDiffuse, final boolean isGlossy, final boolean isSpecular) {
		return new PBRTBXDFType(true, false, isDiffuse, isGlossy, isSpecular);
	}
	
	private static PBRTBXDFType doCreateReflectionAndTransmission(final boolean isDiffuse, final boolean isGlossy, final boolean isSpecular) {
		return new PBRTBXDFType(true, true, isDiffuse, isGlossy, isSpecular);
	}
	
	private static PBRTBXDFType doCreateTransmission(final boolean isDiffuse, final boolean isGlossy, final boolean isSpecular) {
		return new PBRTBXDFType(false, true, isDiffuse, isGlossy, isSpecular);
	}
}