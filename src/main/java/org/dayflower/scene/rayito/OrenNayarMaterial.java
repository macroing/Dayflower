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

import org.dayflower.geometry.AngleF;
import org.dayflower.image.Color3F;
import org.dayflower.scene.Intersection;

/**
 * An {@code OrenNayarMaterial} is an implementation of {@link RayitoMaterial} that uses an {@link OrenNayarBRDF} instance.
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class OrenNayarMaterial implements RayitoMaterial {
	private final BXDF selectedBXDF;
	private final float selectedBXDFWeight;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code OrenNayarMaterial} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new OrenNayarMaterial(new OrenNayarBRDF());
	 * }
	 * </pre>
	 */
	public OrenNayarMaterial() {
		this(new OrenNayarBRDF());
	}
	
	/**
	 * Constructs a new {@code OrenNayarMaterial} instance.
	 * <p>
	 * If {@code angle} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new OrenNayarMaterial(new OrenNayarBRDF(angle));
	 * }
	 * </pre>
	 * 
	 * @param angle an {@link AngleF} instance
	 * @throws NullPointerException thrown if, and only if, {@code angle} is {@code null}
	 */
	public OrenNayarMaterial(final AngleF angle) {
		this(new OrenNayarBRDF(angle));
	}
	
	/**
	 * Constructs a new {@code OrenNayarMaterial} instance.
	 * <p>
	 * If {@code orenNayarBRDF} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param orenNayarBRDF an {@link OrenNayarBRDF} instance
	 * @throws NullPointerException thrown if, and only if, {@code orenNayarBRDF} is {@code null}
	 */
	public OrenNayarMaterial(final OrenNayarBRDF orenNayarBRDF) {
		this.selectedBXDF = Objects.requireNonNull(orenNayarBRDF, "orenNayarBRDF == null");
		this.selectedBXDFWeight = 1.0F;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link Color3F} instance with the emittance of this {@code OrenNayarMaterial} instance at {@code intersection}.
	 * <p>
	 * If {@code intersection} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param intersection an {@link Intersection} instance
	 * @return a {@code Color3F} instance with the emittance of this {@code OrenNayarMaterial} instance at {@code intersection}
	 * @throws NullPointerException thrown if, and only if, {@code intersection} is {@code null}
	 */
	@Override
	public Color3F emittance(final Intersection intersection) {
		return intersection.getPrimitive().getTextureEmittance().getColor(intersection);
	}
	
	/**
	 * Returns a {@link MaterialResult} instance with information about this {@code OrenNayarMaterial} instance at {@code intersection}.
	 * <p>
	 * If {@code intersection} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param intersection an {@link Intersection} instance
	 * @return a {@code MaterialResult} instance with information about this {@code OrenNayarMaterial} instance at {@code intersection}
	 * @throws NullPointerException thrown if, and only if, {@code intersection} is {@code null}
	 */
	@Override
	public MaterialResult evaluate(final Intersection intersection) {
		return new MaterialResult(intersection.getPrimitive().getTextureAlbedo().getColor(intersection), this.selectedBXDF, this.selectedBXDFWeight);
	}
	
	/**
	 * Returns a {@code String} representation of this {@code OrenNayarMaterial} instance.
	 * 
	 * @return a {@code String} representation of this {@code OrenNayarMaterial} instance
	 */
	@Override
	public String toString() {
		return String.format("new OrenNayarMaterial(%s)", this.selectedBXDF);
	}
	
	/**
	 * Compares {@code object} to this {@code OrenNayarMaterial} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code OrenNayarMaterial}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code OrenNayarMaterial} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code OrenNayarMaterial}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof OrenNayarMaterial)) {
			return false;
		} else if(!Objects.equals(this.selectedBXDF, OrenNayarMaterial.class.cast(object).selectedBXDF)) {
			return false;
		} else if(!equal(this.selectedBXDFWeight, OrenNayarMaterial.class.cast(object).selectedBXDFWeight)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns a hash code for this {@code OrenNayarMaterial} instance.
	 * 
	 * @return a hash code for this {@code OrenNayarMaterial} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.selectedBXDF, Float.valueOf(this.selectedBXDFWeight));
	}
}