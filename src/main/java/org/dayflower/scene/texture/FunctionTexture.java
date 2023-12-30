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
package org.dayflower.scene.texture;

import java.util.Objects;
import java.util.function.Function;

import org.dayflower.geometry.Shape3F;
import org.dayflower.scene.Intersection;

import org.macroing.art4j.color.Color3F;

/**
 * A {@code FunctionTexture} is a {@link Texture} implementation that returns a {@link Color3F} instance by applying an {@link Intersection} instance to a {@code Function} instance.
 * <p>
 * This class is immutable and therefore thread-safe if, and only if, the {@code Function} is.
 * <p>
 * This {@code Texture} implementation is not supported on the GPU.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class FunctionTexture implements Texture {
	/**
	 * The ID of this {@code FunctionTexture} class.
	 */
	public static final int ID = 6;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final Function<Intersection, Color3F> function;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code FunctionTexture} instance.
	 * <p>
	 * If {@code function} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param function a {@code Function} instance
	 * @throws NullPointerException thrown if, and only if, {@code function} is {@code null}
	 */
	public FunctionTexture(final Function<Intersection, Color3F> function) {
		this.function = Objects.requireNonNull(function, "function == null");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link Color3F} instance representing the color of the surface at {@code intersection}.
	 * <p>
	 * If either {@code intersection} or {@code function.apply(intersection)} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param intersection an {@link Intersection} instance
	 * @return a {@code Color3F} instance representing the color of the surface at {@code intersection}
	 * @throws NullPointerException thrown if, and only if, either {@code intersection} or {@code function.apply(intersection)} are {@code null}
	 */
	@Override
	public Color3F getColor(final Intersection intersection) {
		return Objects.requireNonNull(this.function.apply(Objects.requireNonNull(intersection, "intersection == null")), "function.apply(intersection) == null");
	}
	
	/**
	 * Returns a {@code String} representation of this {@code FunctionTexture} instance.
	 * 
	 * @return a {@code String} representation of this {@code FunctionTexture} instance
	 */
	@Override
	public String toString() {
		return "new FunctionTexture(...)";
	}
	
	/**
	 * Compares {@code object} to this {@code FunctionTexture} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code FunctionTexture}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code FunctionTexture} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code FunctionTexture}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof FunctionTexture)) {
			return false;
		} else if(!Objects.equals(this.function, FunctionTexture.class.cast(object).function)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns a {@code float} representing the value of the surface at {@code intersection}.
	 * <p>
	 * If {@code intersection} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param intersection an {@link Intersection} instance
	 * @return a {@code float} representing the value of the surface at {@code intersection}
	 * @throws NullPointerException thrown if, and only if, {@code intersection} is {@code null}
	 */
	@Override
	public float getFloat(final Intersection intersection) {
		return getColor(intersection).average();
	}
	
	/**
	 * Returns an {@code int} with the ID of this {@code FunctionTexture} instance.
	 * 
	 * @return an {@code int} with the ID of this {@code FunctionTexture} instance
	 */
	@Override
	public int getID() {
		return ID;
	}
	
	/**
	 * Returns a hash code for this {@code FunctionTexture} instance.
	 * 
	 * @return a hash code for this {@code FunctionTexture} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.function);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code FunctionTexture} instance that matches intersected {@link Shape3F} instances to {@link Texture} instances based on their identity.
	 * <p>
	 * If either {@code shapeA}, {@code textureA}, {@code shapeB} or {@code textureB} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param shapeA a {@code Shape3F} instance to match against
	 * @param textureA the {@code Texture} instance associated with {@code shapeA}
	 * @param shapeB a {@code Shape3F} instance to match against
	 * @param textureB the {@code Texture} instance associated with {@code shapeB}
	 * @return a {@code FunctionTexture} instance that matches intersected {@code Shape3F} instances to {@code Texture} instances based on their identity
	 * @throws NullPointerException thrown if, and only if, either {@code shapeA}, {@code textureA}, {@code shapeB} or {@code textureB} are {@code null}
	 */
	public static FunctionTexture createShapeIdentity(final Shape3F shapeA, final Texture textureA, final Shape3F shapeB, final Texture textureB) {
		Objects.requireNonNull(shapeA, "shapeA == null");
		Objects.requireNonNull(textureA, "textureA == null");
		Objects.requireNonNull(shapeB, "shapeB == null");
		Objects.requireNonNull(textureB, "textureB == null");
		
		final Function<Intersection, Color3F> function = intersection -> {
			if(intersection.getShape() == shapeA) {
				return textureA.getColor(intersection);
			}
			
			if(intersection.getShape() == shapeB) {
				return textureB.getColor(intersection);
			}
			
			return Color3F.BLACK;
		};
		
		return new FunctionTexture(function);
	}
}