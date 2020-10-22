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
package org.dayflower.scene.rayito;

import static org.dayflower.util.Floats.equal;

import java.util.Objects;

import org.dayflower.image.Color3F;
import org.dayflower.scene.Intersection;

/**
 * A {@code RefractionMaterial} is an implementation of {@link RayitoMaterial} that uses a {@link RefractionBTDF} instance.
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class RefractionMaterial implements RayitoMaterial {
	private final BXDF selectedBXDF;
	private final float selectedBXDFWeight;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code RefractionMaterial} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new RefractionMaterial(new RefractionBTDF());
	 * }
	 * </pre>
	 */
	public RefractionMaterial() {
		this(new RefractionBTDF());
	}
	
	/**
	 * Constructs a new {@code RefractionMaterial} instance.
	 * <p>
	 * If {@code refractionBTDF} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param refractionBTDF a {@link RefractionBTDF} instance
	 * @throws NullPointerException thrown if, and only if, {@code refractionBTDF} is {@code null}
	 */
	public RefractionMaterial(final RefractionBTDF refractionBTDF) {
		this.selectedBXDF = Objects.requireNonNull(refractionBTDF, "refractionBTDF == null");
		this.selectedBXDFWeight = 1.0F;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link Color3F} instance with the emittance of this {@code RefractionMaterial} instance at {@code intersection}.
	 * <p>
	 * If {@code intersection} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param intersection an {@link Intersection} instance
	 * @return a {@code Color3F} instance with the emittance of this {@code RefractionMaterial} instance at {@code intersection}
	 * @throws NullPointerException thrown if, and only if, {@code intersection} is {@code null}
	 */
	@Override
	public Color3F emittance(final Intersection intersection) {
		return intersection.getPrimitive().getTextureEmittance().getColorRGB(intersection);
	}
	
	/**
	 * Returns a {@link MaterialResult} instance with information about this {@code RefractionMaterial} instance at {@code intersection}.
	 * <p>
	 * If {@code intersection} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param intersection an {@link Intersection} instance
	 * @return a {@code MaterialResult} instance with information about this {@code RefractionMaterial} instance at {@code intersection}
	 * @throws NullPointerException thrown if, and only if, {@code intersection} is {@code null}
	 */
	@Override
	public MaterialResult evaluate(final Intersection intersection) {
		return new MaterialResult(intersection.getPrimitive().getTextureAlbedo().getColorRGB(intersection), this.selectedBXDF, this.selectedBXDFWeight);
	}
	
	/**
	 * Returns a {@code String} representation of this {@code RefractionMaterial} instance.
	 * 
	 * @return a {@code String} representation of this {@code RefractionMaterial} instance
	 */
	@Override
	public String toString() {
		return String.format("new RefractionMaterial(%s)", this.selectedBXDF);
	}
	
	/**
	 * Compares {@code object} to this {@code RefractionMaterial} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code RefractionMaterial}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code RefractionMaterial} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code RefractionMaterial}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof RefractionMaterial)) {
			return false;
		} else if(!Objects.equals(this.selectedBXDF, RefractionMaterial.class.cast(object).selectedBXDF)) {
			return false;
		} else if(!equal(this.selectedBXDFWeight, RefractionMaterial.class.cast(object).selectedBXDFWeight)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns a hash code for this {@code RefractionMaterial} instance.
	 * 
	 * @return a hash code for this {@code RefractionMaterial} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.selectedBXDF, Float.valueOf(this.selectedBXDFWeight));
	}
}