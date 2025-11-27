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
package org.dayflower.scene.material;

import java.util.Objects;
import java.util.function.Function;

import org.dayflower.color.Color3F;
import org.dayflower.geometry.Shape3F;
import org.dayflower.scene.Intersection;
import org.dayflower.scene.Material;
import org.dayflower.scene.ScatteringFunctions;
import org.dayflower.scene.TransportMode;

/**
 * A {@code FunctionMaterial} is an implementation of {@link Material} that alternates between different {@code Material} instances based on a {@code Function} instance.
 * <p>
 * This class is immutable and thread-safe as long as its associated {@link Material} instances are.
 * <p>
 * This {@code Material} implementation is not supported on the GPU.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class FunctionMaterial implements Material {
	/**
	 * The name of this {@code FunctionMaterial} class.
	 */
	public static final String NAME = "Function";
	
	/**
	 * The ID of this {@code FunctionMaterial} class.
	 */
	public static final int ID = 6;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final Function<Intersection, Material> function;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code FunctionMaterial} instance.
	 * <p>
	 * If {@code function} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param function a {@code Function} instance
	 * @throws NullPointerException thrown if, and only if, {@code function} is {@code null}
	 */
	public FunctionMaterial(final Function<Intersection, Material> function) {
		this.function = Objects.requireNonNull(function, "function == null");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link Color3F} instance with the emittance of this {@code FunctionMaterial} instance at {@code intersection}.
	 * <p>
	 * If {@code intersection} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param intersection an {@link Intersection} instance
	 * @return a {@code Color3F} instance with the emittance of this {@code FunctionMaterial} instance at {@code intersection}
	 * @throws NullPointerException thrown if, and only if, {@code intersection} is {@code null}
	 */
	@Override
	public Color3F emittance(final Intersection intersection) {
		Objects.requireNonNull(intersection, "intersection == null");
		
		final Material material = doGetMaterial(intersection);
		
		final Color3F emittance = material.emittance(intersection);
		
		return emittance;
	}
	
	/**
	 * Computes the {@link ScatteringFunctions} at {@code intersection}.
	 * <p>
	 * Returns a {@code ScatteringFunctions} instance.
	 * <p>
	 * If either {@code intersection} or {@code transportMode} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param intersection the {@link Intersection} to compute the {@code ScatteringFunctions} for
	 * @param transportMode the {@link TransportMode} to use
	 * @param isAllowingMultipleLobes {@code true} if, and only if, multiple lobes are allowed, {@code false} otherwise
	 * @return a {@code ScatteringFunctions} instance
	 * @throws NullPointerException thrown if, and only if, either {@code intersection} or {@code transportMode} are {@code null}
	 */
	@Override
	public ScatteringFunctions computeScatteringFunctions(final Intersection intersection, final TransportMode transportMode, final boolean isAllowingMultipleLobes) {
		Objects.requireNonNull(intersection, "intersection == null");
		Objects.requireNonNull(transportMode, "transportMode == null");
		
		final Material material = doGetMaterial(intersection);
		
		final ScatteringFunctions scatteringFunctions = material.computeScatteringFunctions(intersection, transportMode, isAllowingMultipleLobes);
		
		return scatteringFunctions;
	}
	
	/**
	 * Returns a {@code String} with the name of this {@code FunctionMaterial} instance.
	 * 
	 * @return a {@code String} with the name of this {@code FunctionMaterial} instance
	 */
	@Override
	public String getName() {
		return NAME;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code FunctionMaterial} instance.
	 * 
	 * @return a {@code String} representation of this {@code FunctionMaterial} instance
	 */
	@Override
	public String toString() {
		return "new FunctionMaterial(...)";
	}
	
	/**
	 * Compares {@code object} to this {@code FunctionMaterial} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code FunctionMaterial}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code FunctionMaterial} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code FunctionMaterial}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof FunctionMaterial)) {
			return false;
		} else if(!Objects.equals(this.function, FunctionMaterial.class.cast(object).function)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns an {@code int} with the ID of this {@code FunctionMaterial} instance.
	 * 
	 * @return an {@code int} with the ID of this {@code FunctionMaterial} instance
	 */
	@Override
	public int getID() {
		return ID;
	}
	
	/**
	 * Returns a hash code for this {@code FunctionMaterial} instance.
	 * 
	 * @return a hash code for this {@code FunctionMaterial} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.function);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code FunctionMaterial} instance that matches intersected {@link Shape3F} instances to {@link Material} instances based on their identity.
	 * <p>
	 * If either {@code shapeA}, {@code materialA}, {@code shapeB} or {@code materialB} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param shapeA a {@code Shape3F} instance to match against
	 * @param materialA the {@code Material} instance associated with {@code shapeA}
	 * @param shapeB a {@code Shape3F} instance to match against
	 * @param materialB the {@code Material} instance associated with {@code shapeB}
	 * @return a {@code FunctionMaterial} instance that matches intersected {@code Shape3F} instances to {@code Material} instances based on their identity
	 * @throws NullPointerException thrown if, and only if, either {@code shapeA}, {@code materialA}, {@code shapeB} or {@code materialB} are {@code null}
	 */
	public static FunctionMaterial createShapeIdentity(final Shape3F shapeA, final Material materialA, final Shape3F shapeB, final Material materialB) {
		Objects.requireNonNull(shapeA, "shapeA == null");
		Objects.requireNonNull(materialA, "materialA == null");
		Objects.requireNonNull(shapeB, "shapeB == null");
		Objects.requireNonNull(materialB, "materialB == null");
		
		final Material materialDefault = new MatteMaterial();
		
		final Function<Intersection, Material> function = intersection -> {
			if(intersection.getShape() == shapeA) {
				return materialA;
			}
			
			if(intersection.getShape() == shapeB) {
				return materialB;
			}
			
			return materialDefault;
		};
		
		return new FunctionMaterial(function);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private Material doGetMaterial(final Intersection intersection) {
		return Objects.requireNonNull(this.function.apply(intersection), "function.apply(intersection) == null");
	}
}