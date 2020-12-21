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
package org.dayflower.scene.texture;

import static org.dayflower.util.Floats.cos;
import static org.dayflower.util.Floats.fractionalPart;
import static org.dayflower.util.Floats.sin;

import java.util.Objects;

import org.dayflower.geometry.AngleF;
import org.dayflower.geometry.Vector2F;
import org.dayflower.image.Color3F;
import org.dayflower.node.NodeHierarchicalVisitor;
import org.dayflower.node.NodeTraversalException;
import org.dayflower.scene.Intersection;

/**
 * A {@code CheckerboardTexture} is a {@link Texture} implementation that returns a {@link Color3F} instance by alternating between two other {@code Texture} instances in a checkerboard pattern.
 * <p>
 * This class is immutable and therefore thread-safe if, and only if, both {@code Texture} instances are.
 * <p>
 * This {@code Texture} implementation is supported on the GPU.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class CheckerboardTexture implements Texture {
	/**
	 * The offset for the angle in degrees in the {@code float[]}.
	 */
	public static final int ARRAY_OFFSET_ANGLE_DEGREES = 0;
	
	/**
	 * The offset for the angle in radians in the {@code float[]}.
	 */
	public static final int ARRAY_OFFSET_ANGLE_RADIANS = 1;
	
	/**
	 * The offset for the {@link Vector2F} instance representing the scale in the {@code float[]}.
	 */
	public static final int ARRAY_OFFSET_SCALE = 6;
	
	/**
	 * The offset for the ID of the {@link Texture} denoted by {@code A} in the {@code float[]}.
	 */
	public static final int ARRAY_OFFSET_TEXTURE_A_ID = 2;
	
	/**
	 * The offset for the offset of the {@link Texture} denoted by {@code A} in the {@code float[]}.
	 */
	public static final int ARRAY_OFFSET_TEXTURE_A_OFFSET = 3;
	
	/**
	 * The offset for the ID of the {@link Texture} denoted by {@code B} in the {@code float[]}.
	 */
	public static final int ARRAY_OFFSET_TEXTURE_B_ID = 4;
	
	/**
	 * The offset for the offset of the {@link Texture} denoted by {@code B} in the {@code float[]}.
	 */
	public static final int ARRAY_OFFSET_TEXTURE_B_OFFSET = 5;
	
	/**
	 * The size of the {@code float[]}.
	 */
	public static final int ARRAY_SIZE = 8;
	
	/**
	 * The ID of this {@code CheckerboardTexture} class.
	 */
	public static final int ID = 3;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final AngleF angle;
	private final Texture textureA;
	private final Texture textureB;
	private final Vector2F scale;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code CheckerboardTexture} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new CheckerboardTexture(Color3F.GRAY, Color3F.WHITE);
	 * }
	 * </pre>
	 */
	public CheckerboardTexture() {
		this(Color3F.GRAY, Color3F.WHITE);
	}
	
	/**
	 * Constructs a new {@code CheckerboardTexture} instance.
	 * <p>
	 * If either {@code colorA} or {@code colorB} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new CheckerboardTexture(colorA, colorB, AngleF.degrees(0.0F));
	 * }
	 * </pre>
	 * 
	 * @param colorA one of the two {@link Color3F} instances to use
	 * @param colorB one of the two {@code Color3F} instances to use
	 * @throws NullPointerException thrown if, and only if, either {@code colorA} or {@code colorB} are {@code null}
	 */
	public CheckerboardTexture(final Color3F colorA, final Color3F colorB) {
		this(new ConstantTexture(colorA), new ConstantTexture(colorB), AngleF.degrees(0.0F));
	}
	
	/**
	 * Constructs a new {@code CheckerboardTexture} instance.
	 * <p>
	 * If either {@code colorA}, {@code colorB} or {@code angle} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new CheckerboardTexture(colorA, colorB, angle, new Vector2F(1.0F, 1.0F));
	 * }
	 * </pre>
	 * 
	 * @param colorA one of the two {@link Color3F} instances to use
	 * @param colorB one of the two {@code Color3F} instances to use
	 * @param angle the {@link AngleF} instance to use
	 * @throws NullPointerException thrown if, and only if, either {@code colorA}, {@code colorB} or {@code angle} are {@code null}
	 */
	public CheckerboardTexture(final Color3F colorA, final Color3F colorB, final AngleF angle) {
		this(new ConstantTexture(colorA), new ConstantTexture(colorB), angle, new Vector2F(1.0F, 1.0F));
	}
	
	/**
	 * Constructs a new {@code CheckerboardTexture} instance.
	 * <p>
	 * If either {@code colorA}, {@code colorB}, {@code angle} or {@code scale} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new CheckerboardTexture(new ConstantTexture(colorA), new ConstantTexture(colorB), angle, scale);
	 * }
	 * </pre>
	 * 
	 * @param colorA one of the two {@link Color3F} instances to use
	 * @param colorB one of the two {@code Color3F} instances to use
	 * @param angle the {@link AngleF} instance to use
	 * @param scale the {@link Vector2F} instance to use as the scale factor
	 * @throws NullPointerException thrown if, and only if, either {@code colorA}, {@code colorB}, {@code angle} or {@code scale} are {@code null}
	 */
	public CheckerboardTexture(final Color3F colorA, final Color3F colorB, final AngleF angle, Vector2F scale) {
		this(new ConstantTexture(colorA), new ConstantTexture(colorB), angle, scale);
	}
	
	/**
	 * Constructs a new {@code CheckerboardTexture} instance.
	 * <p>
	 * If either {@code textureA} or {@code textureB} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new CheckerboardTexture(textureA, textureB, AngleF.degrees(0.0F));
	 * }
	 * </pre>
	 * 
	 * @param textureA one of the two {@link Texture} instances to use
	 * @param textureB one of the two {@code Texture} instances to use
	 * @throws NullPointerException thrown if, and only if, either {@code textureA} or {@code textureB} are {@code null}
	 */
	public CheckerboardTexture(final Texture textureA, final Texture textureB) {
		this(textureA, textureB, AngleF.degrees(0.0F));
	}
	
	/**
	 * Constructs a new {@code CheckerboardTexture} instance.
	 * <p>
	 * If either {@code textureA}, {@code textureB} or {@code angle} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new CheckerboardTexture(textureA, textureB, angle, new Vector2F(1.0F, 1.0F));
	 * }
	 * </pre>
	 * 
	 * @param textureA one of the two {@link Texture} instances to use
	 * @param textureB one of the two {@code Texture} instances to use
	 * @param angle the {@link AngleF} instance to use
	 * @throws NullPointerException thrown if, and only if, either {@code textureA}, {@code textureB} or {@code angle} are {@code null}
	 */
	public CheckerboardTexture(final Texture textureA, final Texture textureB, final AngleF angle) {
		this(textureA, textureB, angle, new Vector2F(1.0F, 1.0F));
	}
	
	/**
	 * Constructs a new {@code CheckerboardTexture} instance.
	 * <p>
	 * If either {@code textureA}, {@code textureB}, {@code angle} or {@code scale} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param textureA one of the two {@link Texture} instances to use
	 * @param textureB one of the two {@code Texture} instances to use
	 * @param angle the {@link AngleF} instance to use
	 * @param scale the {@link Vector2F} instance to use as the scale factor
	 * @throws NullPointerException thrown if, and only if, either {@code textureA}, {@code textureB}, {@code angle} or {@code scale} are {@code null}
	 */
	public CheckerboardTexture(final Texture textureA, final Texture textureB, final AngleF angle, final Vector2F scale) {
		this.textureA = Objects.requireNonNull(textureA, "textureA == null");
		this.textureB = Objects.requireNonNull(textureB, "textureB == null");
		this.angle = Objects.requireNonNull(angle, "angle == null");
		this.scale = Objects.requireNonNull(scale, "scale == null");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the {@link AngleF} instance to use.
	 * 
	 * @return the {@code AngleF} instance to use
	 */
	public AngleF getAngle() {
		return this.angle;
	}
	
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
		final float cosAngleRadians = cos(this.angle.getRadians());
		final float sinAngleRadians = sin(this.angle.getRadians());
		
		final float u = intersection.getSurfaceIntersectionObjectSpace().getTextureCoordinates().getU();
		final float v = intersection.getSurfaceIntersectionObjectSpace().getTextureCoordinates().getV();
		
		final boolean isU = fractionalPart((u * cosAngleRadians - v * sinAngleRadians) * this.scale.getU()) > 0.5F;
		final boolean isV = fractionalPart((v * cosAngleRadians + u * sinAngleRadians) * this.scale.getV()) > 0.5F;
		
		return isU ^ isV ? this.textureA.getColor(intersection) : this.textureB.getColor(intersection);
	}
	
	/**
	 * Returns a {@code String} representation of this {@code CheckerboardTexture} instance.
	 * 
	 * @return a {@code String} representation of this {@code CheckerboardTexture} instance
	 */
	@Override
	public String toString() {
		return String.format("new CheckerboardTexture(%s, %s, %s, %s)", this.angle, this.textureA, this.textureB, this.scale);
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
	 * Returns the {@link Vector2F} instance to use as the scale factor.
	 * 
	 * @return the {@code Vector2F} instance to use as the scale factor
	 */
	public Vector2F getScale() {
		return this.scale;
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
				if(!this.textureA.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
				
				if(!this.textureB.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
				
				if(!this.scale.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
			}
			
			return nodeHierarchicalVisitor.visitLeave(this);
		} catch(final RuntimeException e) {
			throw new NodeTraversalException(e);
		}
	}
	
	/**
	 * Compares {@code object} to this {@code CheckerboardTexture} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code CheckerboardTexture}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code CheckerboardTexture} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code CheckerboardTexture}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof CheckerboardTexture)) {
			return false;
		} else if(!Objects.equals(this.angle, CheckerboardTexture.class.cast(object).angle)) {
			return false;
		} else if(!Objects.equals(this.textureA, CheckerboardTexture.class.cast(object).textureA)) {
			return false;
		} else if(!Objects.equals(this.textureB, CheckerboardTexture.class.cast(object).textureB)) {
			return false;
		} else if(!Objects.equals(this.scale, CheckerboardTexture.class.cast(object).scale)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns a {@code float[]} representation of this {@code CheckerboardTexture} instance.
	 * 
	 * @return a {@code float[]} representation of this {@code CheckerboardTexture} instance
	 */
	public float[] toArray() {
		final float[] array = new float[ARRAY_SIZE];
		
//		Because the CheckerboardTexture occupy 8/8 positions in a block, it should be aligned.
		array[ARRAY_OFFSET_ANGLE_DEGREES] = this.angle.getDegrees();//Block #1
		array[ARRAY_OFFSET_ANGLE_RADIANS] = this.angle.getRadians();//Block #1
		array[ARRAY_OFFSET_TEXTURE_A_ID] = this.textureA.getID();	//Block #1
		array[ARRAY_OFFSET_TEXTURE_A_OFFSET] = 0.0F;				//Block #1
		array[ARRAY_OFFSET_TEXTURE_B_ID] = this.textureB.getID();	//Block #1
		array[ARRAY_OFFSET_TEXTURE_B_OFFSET] = 0.0F;				//Block #1
		array[ARRAY_OFFSET_SCALE + 0] = this.scale.getX();			//Block #1
		array[ARRAY_OFFSET_SCALE + 1] = this.scale.getY();			//Block #1
		
		return array;
	}
	
	/**
	 * Returns an {@code int} with the ID of this {@code CheckerboardTexture} instance.
	 * 
	 * @return an {@code int} with the ID of this {@code CheckerboardTexture} instance
	 */
	@Override
	public int getID() {
		return ID;
	}
	
	/**
	 * Returns a hash code for this {@code CheckerboardTexture} instance.
	 * 
	 * @return a hash code for this {@code CheckerboardTexture} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.angle, this.textureA, this.textureB, this.scale);
	}
}