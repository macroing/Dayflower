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

import static org.dayflower.util.Floats.PI;
import static org.dayflower.util.Floats.abs;
import static org.dayflower.util.Floats.equal;
import static org.dayflower.util.Floats.perlinTurbulenceXYZ;
import static org.dayflower.util.Floats.sin;

import java.util.Objects;

import org.dayflower.geometry.Point3F;
import org.dayflower.image.Color3F;
import org.dayflower.node.NodeHierarchicalVisitor;
import org.dayflower.node.NodeTraversalException;
import org.dayflower.scene.Intersection;

/**
 * A {@code MarbleTexture} is a {@link Texture} implementation that returns a {@link Color3F} instance by alternating between three {@code Color3F} instances in a marble pattern.
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class MarbleTexture implements Texture {
	/**
	 * The offset for the frequency in the {@code float[]}.
	 */
	public static final int ARRAY_OFFSET_FREQUENCY = 6;
	
	/**
	 * The offset for the octaves in the {@code float[]}.
	 */
	public static final int ARRAY_OFFSET_OCTAVES = 9;
	
	/**
	 * The offset for the scale in the {@code float[]}.
	 */
	public static final int ARRAY_OFFSET_SCALE = 7;
	
	/**
	 * The offset for the stripes in the {@code float[]}.
	 */
	public static final int ARRAY_OFFSET_STRIPES = 8;
	
	/**
	 * The offset for the ID of the {@link Texture} denoted by {@code A} in the {@code float[]}.
	 */
	public static final int ARRAY_OFFSET_TEXTURE_A_ID = 0;
	
	/**
	 * The offset for the offset of the {@link Texture} denoted by {@code A} in the {@code float[]}.
	 */
	public static final int ARRAY_OFFSET_TEXTURE_A_OFFSET = 1;
	
	/**
	 * The offset for the ID of the {@link Texture} denoted by {@code B} in the {@code float[]}.
	 */
	public static final int ARRAY_OFFSET_TEXTURE_B_ID = 2;
	
	/**
	 * The offset for the offset of the {@link Texture} denoted by {@code B} in the {@code float[]}.
	 */
	public static final int ARRAY_OFFSET_TEXTURE_B_OFFSET = 3;
	
	/**
	 * The offset for the ID of the {@link Texture} denoted by {@code B} in the {@code float[]}.
	 */
	public static final int ARRAY_OFFSET_TEXTURE_C_ID = 4;
	
	/**
	 * The offset for the offset of the {@link Texture} denoted by {@code B} in the {@code float[]}.
	 */
	public static final int ARRAY_OFFSET_TEXTURE_C_OFFSET = 5;
	
	/**
	 * The size of the {@code float[]}.
	 */
	public static final int ARRAY_SIZE = 16;
	
	/**
	 * The ID of this {@code MarbleTexture} class.
	 */
	public static final int ID = 7;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final Texture textureA;
	private final Texture textureB;
	private final Texture textureC;
	private final float frequency;
	private final float scale;
	private final float stripes;
	private final int octaves;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code MarbleTexture} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new MarbleTexture(new Color3F(0.8F, 0.8F, 0.8F), new Color3F(0.4F, 0.2F, 0.1F), new Color3F(0.06F, 0.04F, 0.02F));
	 * }
	 * </pre>
	 */
	public MarbleTexture() {
		this(new Color3F(0.8F, 0.8F, 0.8F), new Color3F(0.4F, 0.2F, 0.1F), new Color3F(0.06F, 0.04F, 0.02F));
	}
	
	/**
	 * Constructs a new {@code MarbleTexture} instance.
	 * <p>
	 * If either {@code colorA}, {@code colorB} or {@code colorC} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new MarbleTexture(colorA, colorB, colorC, 5.0F, 0.15F, 8);
	 * }
	 * </pre>
	 * 
	 * @param colorA one of the {@link Color3F} instances to use
	 * @param colorB one of the {@code Color3F} instances to use
	 * @param colorC one of the {@code Color3F} instances to use
	 * @throws NullPointerException thrown if, and only if, either {@code colorA}, {@code colorB} or {@code colorC} are {@code null}
	 */
	public MarbleTexture(final Color3F colorA, final Color3F colorB, final Color3F colorC) {
		this(colorA, colorB, colorC, 5.0F, 0.15F, 8);
	}
	
	/**
	 * Constructs a new {@code MarbleTexture} instance.
	 * <p>
	 * If either {@code colorA}, {@code colorB} or {@code colorC} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param colorA one of the {@link Color3F} instances to use
	 * @param colorB one of the {@code Color3F} instances to use
	 * @param colorC one of the {@code Color3F} instances to use
	 * @param scale the scale to use
	 * @param stripes the stripes to use
	 * @param octaves the octaves to use
	 * @throws NullPointerException thrown if, and only if, either {@code colorA}, {@code colorB} or {@code colorC} are {@code null}
	 */
	public MarbleTexture(final Color3F colorA, final Color3F colorB, final Color3F colorC, final float scale, final float stripes, final int octaves) {
		this.textureA = new ConstantTexture(Objects.requireNonNull(colorA, "colorA == null"));
		this.textureB = new ConstantTexture(Objects.requireNonNull(colorB, "colorB == null"));
		this.textureC = new ConstantTexture(Objects.requireNonNull(colorC, "colorC == null"));
		this.frequency = PI * stripes;
		this.scale = scale;
		this.stripes = stripes;
		this.octaves = octaves;
	}
	
	/**
	 * Constructs a new {@code MarbleTexture} instance.
	 * <p>
	 * If either {@code textureA}, {@code textureB} or {@code textureC} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new MarbleTexture(textureA, textureB, textureC, 5.0F, 0.15F, 8);
	 * }
	 * </pre>
	 * 
	 * @param textureA one of the {@link Texture} instances to use
	 * @param textureB one of the {@code Texture} instances to use
	 * @param textureC one of the {@code Texture} instances to use
	 * @throws NullPointerException thrown if, and only if, either {@code textureA}, {@code textureB} or {@code textureC} are {@code null}
	 */
	public MarbleTexture(final Texture textureA, final Texture textureB, final Texture textureC) {
		this(textureA, textureB, textureC, 5.0F, 0.15F, 8);
	}
	
	/**
	 * Constructs a new {@code MarbleTexture} instance.
	 * <p>
	 * If either {@code textureA}, {@code textureB} or {@code textureC} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param textureA one of the {@link Texture} instances to use
	 * @param textureB one of the {@code Texture} instances to use
	 * @param textureC one of the {@code Texture} instances to use
	 * @param scale the scale to use
	 * @param stripes the stripes to use
	 * @param octaves the octaves to use
	 * @throws NullPointerException thrown if, and only if, either {@code textureA}, {@code textureB} or {@code textureC} are {@code null}
	 */
	public MarbleTexture(final Texture textureA, final Texture textureB, final Texture textureC, final float scale, final float stripes, final int octaves) {
		this.textureA = Objects.requireNonNull(textureA, "textureA == null");
		this.textureB = Objects.requireNonNull(textureB, "textureB == null");
		this.textureC = Objects.requireNonNull(textureC, "textureC == null");
		this.frequency = PI * stripes;
		this.scale = scale;
		this.stripes = stripes;
		this.octaves = octaves;
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
		final Point3F surfaceIntersectionPoint = intersection.getSurfaceIntersectionWorldSpace().getSurfaceIntersectionPoint();
		
		final float x = surfaceIntersectionPoint.getX() * this.frequency;
		final float y = surfaceIntersectionPoint.getY() * this.frequency;
		final float z = surfaceIntersectionPoint.getZ() * this.frequency;
		final float r = this.scale * perlinTurbulenceXYZ(x, y, z, this.octaves);
		final float s = 2.0F * abs(sin(x + r));
		final float t = s < 1.0F ? s : s - 1.0F;
		
		final Color3F colorA = s < 1.0F ? this.textureC.getColor(intersection) : this.textureB.getColor(intersection);
		final Color3F colorB = s < 1.0F ? this.textureB.getColor(intersection) : this.textureA.getColor(intersection);
		
		return Color3F.blend(colorA, colorB, t);
	}
	
	/**
	 * Returns a {@code String} representation of this {@code MarbleTexture} instance.
	 * 
	 * @return a {@code String} representation of this {@code MarbleTexture} instance
	 */
	@Override
	public String toString() {
		return String.format("new MarbleTexture(%s, %s, %s, %+.10f, %+.10f, %d", this.textureA, this.textureB, this.textureC, Float.valueOf(this.scale), Float.valueOf(this.stripes), Integer.valueOf(this.octaves));
	}
	
	/**
	 * Returns one of the three {@link Texture} instances associated with this {@code MarbleTexture} instance.
	 * 
	 * @return one of the three {@code Texture} instances associated with this {@code MarbleTexture} instance
	 */
	public Texture getTextureA() {
		return this.textureA;
	}
	
	/**
	 * Returns one of the three {@link Texture} instances associated with this {@code MarbleTexture} instance.
	 * 
	 * @return one of the three {@code Texture} instances associated with this {@code MarbleTexture} instance
	 */
	public Texture getTextureB() {
		return this.textureB;
	}
	
	/**
	 * Returns one of the three {@link Texture} instances associated with this {@code MarbleTexture} instance.
	 * 
	 * @return one of the three {@code Texture} instances associated with this {@code MarbleTexture} instance
	 */
	public Texture getTextureC() {
		return this.textureC;
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
				
				if(!this.textureC.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
			}
			
			return nodeHierarchicalVisitor.visitLeave(this);
		} catch(final RuntimeException e) {
			throw new NodeTraversalException(e);
		}
	}
	
	/**
	 * Compares {@code object} to this {@code MarbleTexture} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code MarbleTexture}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code MarbleTexture} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code MarbleTexture}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof MarbleTexture)) {
			return false;
		} else if(!Objects.equals(this.textureA, MarbleTexture.class.cast(object).textureA)) {
			return false;
		} else if(!Objects.equals(this.textureB, MarbleTexture.class.cast(object).textureB)) {
			return false;
		} else if(!Objects.equals(this.textureC, MarbleTexture.class.cast(object).textureC)) {
			return false;
		} else if(!equal(this.scale, MarbleTexture.class.cast(object).scale)) {
			return false;
		} else if(!equal(this.stripes, MarbleTexture.class.cast(object).stripes)) {
			return false;
		} else if(this.octaves != MarbleTexture.class.cast(object).octaves) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns the scale associated with this {@code MarbleTexture} instance.
	 * 
	 * @return the scale associated with this {@code MarbleTexture} instance
	 */
	public float getScale() {
		return this.scale;
	}
	
	/**
	 * Returns the stripes associated with this {@code MarbleTexture} instance.
	 * 
	 * @return the stripes associated with this {@code MarbleTexture} instance
	 */
	public float getStripes() {
		return this.stripes;
	}
	
	/**
	 * Returns a {@code float[]} representation of this {@code MarbleTexture} instance.
	 * 
	 * @return a {@code float[]} representation of this {@code MarbleTexture} instance
	 */
	@Override
	public float[] toArray() {
		final float[] array = new float[ARRAY_SIZE];
		
//		Because the MarbleTexture occupy 16/16 positions in two blocks, it should be aligned.
		array[ARRAY_OFFSET_TEXTURE_A_ID] = this.textureA.getID();	//Block #1
		array[ARRAY_OFFSET_TEXTURE_A_OFFSET] = 0.0F;				//Block #1
		array[ARRAY_OFFSET_TEXTURE_B_ID] = this.textureB.getID();	//Block #1
		array[ARRAY_OFFSET_TEXTURE_B_OFFSET] = 0.0F;				//Block #1
		array[ARRAY_OFFSET_TEXTURE_C_ID] = this.textureC.getID();	//Block #1
		array[ARRAY_OFFSET_TEXTURE_C_OFFSET] = 0.0F;				//Block #1
		array[ARRAY_OFFSET_FREQUENCY] = this.frequency;				//Block #1
		array[ARRAY_OFFSET_SCALE] = this.scale;						//Block #1
		array[ARRAY_OFFSET_STRIPES] = this.stripes;					//Block #2
		array[ARRAY_OFFSET_OCTAVES] = this.octaves;					//Block #2
		array[10] = 0.0F;											//Block #2
		array[11] = 0.0F;											//Block #2
		array[12] = 0.0F;											//Block #2
		array[13] = 0.0F;											//Block #2
		array[14] = 0.0F;											//Block #2
		array[15] = 0.0F;											//Block #2
		
		return array;
	}
	
	/**
	 * Returns an {@code int} with the ID of this {@code MarbleTexture} instance.
	 * 
	 * @return an {@code int} with the ID of this {@code MarbleTexture} instance
	 */
	@Override
	public int getID() {
		return ID;
	}
	
	/**
	 * Returns the octaves associated with this {@code MarbleTexture} instance.
	 * 
	 * @return the octaves associated with this {@code MarbleTexture} instance
	 */
	public int getOctaves() {
		return this.octaves;
	}
	
	/**
	 * Returns a hash code for this {@code MarbleTexture} instance.
	 * 
	 * @return a hash code for this {@code MarbleTexture} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.textureA, this.textureB, this.textureC, Float.valueOf(this.scale), Float.valueOf(this.stripes), Integer.valueOf(this.octaves));
	}
}