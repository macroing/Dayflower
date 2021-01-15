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
package org.dayflower.scene;

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
	/**
	 * A {@code BXDFType} instance with all properties set to {@code true}.
	 */
	public static final BXDFType ALL = new BXDFType(true, true, true, true, true);
	
	/**
	 * A {@code BXDFType} instance with all properties set to {@code true} except for specular.
	 */
	public static final BXDFType ALL_EXCEPT_SPECULAR = new BXDFType(true, true, true, true, false);
	
	/**
	 * A diffuse {@code BXDFType} with reflection.
	 */
	public static final BXDFType DIFFUSE_REFLECTION = doCreateReflection(true, false, false);
	
	/**
	 * A diffuse {@code BXDFType} with reflection and transmission.
	 */
	public static final BXDFType DIFFUSE_REFLECTION_AND_TRANSMISSION = doCreateReflectionAndTransmission(true, false, false);
	
	/**
	 * A diffuse {@code BXDFType} with transmission.
	 */
	public static final BXDFType DIFFUSE_TRANSMISSION = doCreateTransmission(true, false, false);
	
	/**
	 * A glossy {@code BXDFType} with reflection.
	 */
	public static final BXDFType GLOSSY_REFLECTION = doCreateReflection(false, true, false);
	
	/**
	 * A glossy {@code BXDFType} with reflection and transmission.
	 */
	public static final BXDFType GLOSSY_REFLECTION_AND_TRANSMISSION = doCreateReflectionAndTransmission(false, true, false);
	
	/**
	 * A glossy {@code BXDFType} with transmission.
	 */
	public static final BXDFType GLOSSY_TRANSMISSION = doCreateTransmission(false, true, false);
	
	/**
	 * A specular {@code BXDFType} with reflection.
	 */
	public static final BXDFType SPECULAR_REFLECTION = doCreateReflection(false, false, true);
	
	/**
	 * A specular {@code BXDFType} with reflection and transmission.
	 */
	public static final BXDFType SPECULAR_REFLECTION_AND_TRANSMISSION = doCreateReflectionAndTransmission(false, false, true);
	
	/**
	 * A specular {@code BXDFType} with transmission.
	 */
	public static final BXDFType SPECULAR_TRANSMISSION = doCreateTransmission(false, false, true);
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static final int BIT_FLAG_HAS_REFLECTION = 1 << 0;
	private static final int BIT_FLAG_HAS_TRANSMISSION = 1 << 1;
	private static final int BIT_FLAG_IS_DIFFUSE = 1 << 2;
	private static final int BIT_FLAG_IS_GLOSSY = 1 << 3;
	private static final int BIT_FLAG_IS_SPECULAR = 1 << 4;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final int bitFlags;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private BXDFType(final boolean hasReflection, final boolean hasTransmission, final boolean isDiffuse, final boolean isGlossy, final boolean isSpecular) {
		this.bitFlags = doCreateBitFlags(hasReflection, hasTransmission, isDiffuse, isGlossy, isSpecular);
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
			return "BXDFType.DIFFUSE_REFLECTION_AND_TRANSMISSION";
		} else if(isDiffuse() && hasReflection()) {
			return "BXDFType.DIFFUSE_REFLECTION";
		} else if(isDiffuse() && hasTransmission()) {
			return "BXDFType.DIFFUSE_TRANSMISSION";
		} else if(isGlossy() && hasReflection() && hasTransmission()) {
			return "BXDFType.GLOSSY_REFLECTION_AND_TRANSMISSION";
		} else if(isGlossy() && hasReflection()) {
			return "BXDFType.GLOSSY_REFLECTION";
		} else if(isGlossy() && hasTransmission()) {
			return "BXDFType.GLOSSY_TRANSMISSION";
		} else if(isSpecular() && hasReflection() && hasTransmission()) {
			return "BXDFType.SPECULAR_REFLECTION_AND_TRANSMISSION";
		} else if(isSpecular() && hasReflection()) {
			return "BXDFType.SPECULAR_REFLECTION";
		} else if(isSpecular() && hasTransmission()) {
			return "BXDFType.SPECULAR_TRANSMISSION";
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
		} else if(this.bitFlags != BXDFType.class.cast(object).bitFlags) {
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
		return (this.bitFlags & BIT_FLAG_HAS_REFLECTION) == BIT_FLAG_HAS_REFLECTION;
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code BXDFType} has transmission, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, this {@code BXDFType} has transmission, {@code false} otherwise
	 */
	public boolean hasTransmission() {
		return (this.bitFlags & BIT_FLAG_HAS_TRANSMISSION) == BIT_FLAG_HAS_TRANSMISSION;
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code BXDFType} is diffuse, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, this {@code BXDFType} is diffuse, {@code false} otherwise
	 */
	public boolean isDiffuse() {
		return (this.bitFlags & BIT_FLAG_IS_DIFFUSE) == BIT_FLAG_IS_DIFFUSE;
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code BXDFType} is glossy, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, this {@code BXDFType} is glossy, {@code false} otherwise
	 */
	public boolean isGlossy() {
		return (this.bitFlags & BIT_FLAG_IS_GLOSSY) == BIT_FLAG_IS_GLOSSY;
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code BXDFType} is specular, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, this {@code BXDFType} is specular, {@code false} otherwise
	 */
	public boolean isSpecular() {
		return (this.bitFlags & BIT_FLAG_IS_SPECULAR) == BIT_FLAG_IS_SPECULAR;
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code BXDFType} instance matches {@code bXDFType}, {@code false} otherwise.
	 * <p>
	 * If {@code bXDFType} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param bXDFType a {@code BXDFType} instance
	 * @return {@code true} if, and only if, this {@code BXDFType} instance matches {@code bXDFType}, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, {@code bXDFType} is {@code null}
	 */
	public boolean matches(final BXDFType bXDFType) {
		return (this.bitFlags & bXDFType.bitFlags) == this.bitFlags;
	}
	
	/**
	 * Returns a hash code for this {@code BXDFType} instance.
	 * 
	 * @return a hash code for this {@code BXDFType} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(Integer.valueOf(this.bitFlags));
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
	
	private static int doCreateBitFlags(final boolean hasReflection, final boolean hasTransmission, final boolean isDiffuse, final boolean isGlossy, final boolean isSpecular) {
		int bitFlags = 0;
		
		if(hasReflection) {
			bitFlags |= BIT_FLAG_HAS_REFLECTION;
		}
		
		if(hasTransmission) {
			bitFlags |= BIT_FLAG_HAS_TRANSMISSION;
		}
		
		if(isDiffuse) {
			bitFlags |= BIT_FLAG_IS_DIFFUSE;
		}
		
		if(isGlossy) {
			bitFlags |= BIT_FLAG_IS_GLOSSY;
		}
		
		if(isSpecular) {
			bitFlags |= BIT_FLAG_IS_SPECULAR;
		}
		
		return bitFlags;
	}
}