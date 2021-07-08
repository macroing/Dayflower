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
package org.dayflower.scene.texture;

import static org.dayflower.utility.Floats.equal;
import static org.dayflower.utility.Floats.remainder;

import java.util.Objects;

import org.dayflower.color.Color3F;
import org.dayflower.geometry.Point3F;
import org.dayflower.geometry.Vector3F;
import org.dayflower.node.NodeHierarchicalVisitor;
import org.dayflower.node.NodeTraversalException;
import org.dayflower.scene.Intersection;

/**
 * A {@code BullseyeTexture} is a {@link Texture} implementation that returns a {@link Color3F} instance by alternating between two other {@code Texture} instances in a bullseye pattern.
 * <p>
 * This class is immutable and therefore thread-safe if, and only if, both {@code Texture} instances are.
 * <p>
 * This {@code Texture} implementation is supported on the GPU.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class BullseyeTexture implements Texture {
	/**
	 * The ID of this {@code BullseyeTexture} class.
	 */
	public static final int ID = 2;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final Point3F origin;
	private final Texture textureA;
	private final Texture textureB;
	private final float scale;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code BullseyeTexture} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new BullseyeTexture(Color3F.GRAY_0_50, Color3F.WHITE);
	 * }
	 * </pre>
	 */
	public BullseyeTexture() {
		this(Color3F.GRAY_0_50, Color3F.WHITE);
	}
	
	/**
	 * Constructs a new {@code BullseyeTexture} instance.
	 * <p>
	 * If either {@code colorA} or {@code colorB} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new BullseyeTexture(colorA, colorB, new Point3F());
	 * }
	 * </pre>
	 * 
	 * @param colorA one of the two {@link Color3F} instances to use
	 * @param colorB one of the two {@code Color3F} instances to use
	 * @throws NullPointerException thrown if, and only if, either {@code colorA} or {@code colorB} are {@code null}
	 */
	public BullseyeTexture(final Color3F colorA, final Color3F colorB) {
		this(colorA, colorB, new Point3F());
	}
	
	/**
	 * Constructs a new {@code BullseyeTexture} instance.
	 * <p>
	 * If either {@code colorA}, {@code colorB} or {@code origin} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new BullseyeTexture(colorA, colorB, origin, 1.0F);
	 * }
	 * </pre>
	 * 
	 * @param colorA one of the two {@link Color3F} instances to use
	 * @param colorB one of the two {@code Color3F} instances to use
	 * @param origin a {@link Point3F} instance used as the origin for the bullseye pattern
	 * @throws NullPointerException thrown if, and only if, either {@code colorA}, {@code colorB} or {@code origin} are {@code null}
	 */
	public BullseyeTexture(final Color3F colorA, final Color3F colorB, final Point3F origin) {
		this(colorA, colorB, origin, 1.0F);
	}
	
	/**
	 * Constructs a new {@code BullseyeTexture} instance.
	 * <p>
	 * If either {@code colorA}, {@code colorB} or {@code origin} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new BullseyeTexture(new ConstantTexture(colorA), new ConstantTexture(colorB), origin, scale);
	 * }
	 * </pre>
	 * 
	 * @param colorA one of the two {@link Color3F} instances to use
	 * @param colorB one of the two {@code Color3F} instances to use
	 * @param origin a {@link Point3F} instance used as the origin for the bullseye pattern
	 * @param scale the scale for the bullseye pattern
	 * @throws NullPointerException thrown if, and only if, either {@code colorA}, {@code colorB} or {@code origin} are {@code null}
	 */
	public BullseyeTexture(final Color3F colorA, final Color3F colorB, final Point3F origin, final float scale) {
		this(new ConstantTexture(colorA), new ConstantTexture(colorB), origin, scale);
	}
	
	/**
	 * Constructs a new {@code BullseyeTexture} instance.
	 * <p>
	 * If either {@code textureA} or {@code textureB} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new BullseyeTexture(textureA, textureB, new Point3F());
	 * }
	 * </pre>
	 * 
	 * @param textureA one of the two {@link Texture} instances to use
	 * @param textureB one of the two {@code Texture} instances to use
	 * @throws NullPointerException thrown if, and only if, either {@code textureA} or {@code textureB} are {@code null}
	 */
	public BullseyeTexture(final Texture textureA, final Texture textureB) {
		this(textureA, textureB, new Point3F());
	}
	
	/**
	 * Constructs a new {@code BullseyeTexture} instance.
	 * <p>
	 * If either {@code textureA}, {@code textureB} or {@code origin} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new BullseyeTexture(textureA, textureB, origin, 1.0F);
	 * }
	 * </pre>
	 * 
	 * @param textureA one of the two {@link Texture} instances to use
	 * @param textureB one of the two {@code Texture} instances to use
	 * @param origin a {@link Point3F} instance used as the origin for the bullseye pattern
	 * @throws NullPointerException thrown if, and only if, either {@code textureA}, {@code textureB} or {@code origin} are {@code null}
	 */
	public BullseyeTexture(final Texture textureA, final Texture textureB, final Point3F origin) {
		this(textureA, textureB, origin, 1.0F);
	}
	
	/**
	 * Constructs a new {@code BullseyeTexture} instance.
	 * <p>
	 * If either {@code textureA}, {@code textureB} or {@code origin} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param textureA one of the two {@link Texture} instances to use
	 * @param textureB one of the two {@code Texture} instances to use
	 * @param origin a {@link Point3F} instance used as the origin for the bullseye pattern
	 * @param scale the scale for the bullseye pattern
	 * @throws NullPointerException thrown if, and only if, either {@code textureA}, {@code textureB} or {@code origin} are {@code null}
	 */
	public BullseyeTexture(final Texture textureA, final Texture textureB, final Point3F origin, final float scale) {
		this.textureA = Objects.requireNonNull(textureA, "textureA == null");
		this.textureB = Objects.requireNonNull(textureB, "textureB == null");
		this.origin = Objects.requireNonNull(origin, "origin == null");
		this.scale = scale;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link Color3F} instance representing the color of the surface at {@code intersection}.
	 * <p>
	 * If {@code intersection} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param intersection an {@link Intersection} instance
	 * @return a {@code Color3F} instance representing the color of the surface at {@code intersection}
	 * @throws NullPointerException thrown if, and only if, {@code intersection} is {@code null}
	 */
	@Override
	public Color3F getColor(final Intersection intersection) {
		final Vector3F direction = Vector3F.direction(this.origin, intersection.getSurfaceIntersectionObjectSpace().getSurfaceIntersectionPoint());
		
		final float directionLength = direction.length();
		final float directionLengthScaled = directionLength * this.scale;
		final float directionLengthScaledRemainder = remainder(directionLengthScaled, 1.0F);
		
		return directionLengthScaledRemainder > 0.5F ? this.textureA.getColor(intersection) : this.textureB.getColor(intersection);
	}
	
	/**
	 * Returns the {@link Point3F} instance used as the origin for the bullseye pattern.
	 * 
	 * @return the {@code Point3F} instance used as the origin for the bullseye pattern
	 */
	public Point3F getOrigin() {
		return this.origin;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code BullseyeTexture} instance.
	 * 
	 * @return a {@code String} representation of this {@code BullseyeTexture} instance
	 */
	@Override
	public String toString() {
		return String.format("new BullseyeTexture(%s, %s, %s, %+.10f)", this.origin, this.textureA, this.textureB, Float.valueOf(this.scale));
	}
	
	/**
	 * Returns one of the two {@link Texture} instances to use.
	 * 
	 * @return one of the two {@code Texture} instances to use
	 */
	public Texture getTextureA() {
		return this.textureA;
	}
	
	/**
	 * Returns one of the two {@link Texture} instances to use.
	 * 
	 * @return one of the two {@code Texture} instances to use
	 */
	public Texture getTextureB() {
		return this.textureB;
	}
	
	/**
	 * Accepts a {@link NodeHierarchicalVisitor}.
	 * <p>
	 * Returns the result of {@code nodeHierarchicalVisitor.visitLeave(this)}.
	 * <p>
	 * If {@code nodeHierarchicalVisitor} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If a {@code RuntimeException} is thrown by the current {@code NodeHierarchicalVisitor}, a {@code NodeTraversalException} will be thrown with the {@code RuntimeException} wrapped.
	 * <p>
	 * This implementation will:
	 * <ul>
	 * <li>throw a {@code NullPointerException} if {@code nodeHierarchicalVisitor} is {@code null}.</li>
	 * <li>throw a {@code NodeTraversalException} if {@code nodeHierarchicalVisitor} throws a {@code RuntimeException}.</li>
	 * <li>traverse its child {@code Node} instances.</li>
	 * </ul>
	 * 
	 * @param nodeHierarchicalVisitor the {@code NodeHierarchicalVisitor} to accept
	 * @return the result of {@code nodeHierarchicalVisitor.visitLeave(this)}
	 * @throws NodeTraversalException thrown if, and only if, a {@code RuntimeException} is thrown by the current {@code NodeHierarchicalVisitor}
	 * @throws NullPointerException thrown if, and only if, {@code nodeHierarchicalVisitor} is {@code null}
	 */
	@Override
	public boolean accept(final NodeHierarchicalVisitor nodeHierarchicalVisitor) {
		Objects.requireNonNull(nodeHierarchicalVisitor, "nodeHierarchicalVisitor == null");
		
		try {
			if(nodeHierarchicalVisitor.visitEnter(this)) {
				if(!this.origin.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
				
				if(!this.textureA.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
				
				if(!this.textureB.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
			}
			
			return nodeHierarchicalVisitor.visitLeave(this);
		} catch(final RuntimeException e) {
			throw new NodeTraversalException(e);
		}
	}
	
	/**
	 * Compares {@code object} to this {@code BullseyeTexture} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code BullseyeTexture}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code BullseyeTexture} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code BullseyeTexture}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof BullseyeTexture)) {
			return false;
		} else if(!Objects.equals(this.origin, BullseyeTexture.class.cast(object).origin)) {
			return false;
		} else if(!Objects.equals(this.textureA, BullseyeTexture.class.cast(object).textureA)) {
			return false;
		} else if(!Objects.equals(this.textureB, BullseyeTexture.class.cast(object).textureB)) {
			return false;
		} else if(!equal(this.scale, BullseyeTexture.class.cast(object).scale)) {
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
	 * Returns the scale for the bullseye pattern.
	 * 
	 * @return the scale for the bullseye pattern
	 */
	public float getScale() {
		return this.scale;
	}
	
	/**
	 * Returns an {@code int} with the ID of this {@code BullseyeTexture} instance.
	 * 
	 * @return an {@code int} with the ID of this {@code BullseyeTexture} instance
	 */
	@Override
	public int getID() {
		return ID;
	}
	
	/**
	 * Returns a hash code for this {@code BullseyeTexture} instance.
	 * 
	 * @return a hash code for this {@code BullseyeTexture} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.origin, this.textureA, this.textureB, Float.valueOf(this.scale));
	}
}