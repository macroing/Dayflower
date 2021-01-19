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
package org.dayflower.scene.material.smallpt;

import java.util.Objects;

import org.dayflower.color.Color3F;
import org.dayflower.geometry.SurfaceIntersection3F;
import org.dayflower.geometry.Vector3F;
import org.dayflower.node.NodeHierarchicalVisitor;
import org.dayflower.node.NodeTraversalException;
import org.dayflower.scene.Intersection;
import org.dayflower.scene.texture.ConstantTexture;
import org.dayflower.scene.texture.Texture;

/**
 * A {@code MirrorSmallPTMaterial} is an implementation of {@link SmallPTMaterial} that represents a mirror.
 * <p>
 * This class is immutable and thread-safe as long as all {@link Texture} instances are.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class MirrorSmallPTMaterial extends SmallPTMaterial {
	/**
	 * The name of this {@code MirrorSmallPTMaterial} class.
	 */
	public static final String NAME = "SmallPT - Mirror";
	
	/**
	 * The length of the {@code int[]}.
	 */
	public static final int ARRAY_LENGTH = 4;
	
	/**
	 * The offset for the ID of the {@link Texture} denoted by {@code KR} in the {@code int[]}.
	 */
	public static final int ARRAY_OFFSET_TEXTURE_K_R_ID = 2;
	
	/**
	 * The offset for the offset of the {@link Texture} denoted by {@code KR} in the {@code int[]}.
	 */
	public static final int ARRAY_OFFSET_TEXTURE_K_R_OFFSET = 3;
	
	/**
	 * The ID of this {@code MirrorSmallPTMaterial} class.
	 */
	public static final int ID = 304;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final Texture textureEmission;
	private final Texture textureKR;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code MirrorSmallPTMaterial} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new MirrorSmallPTMaterial(Color3F.WHITE);
	 * }
	 * </pre>
	 */
	public MirrorSmallPTMaterial() {
		this(Color3F.WHITE);
	}
	
	/**
	 * Constructs a new {@code MirrorSmallPTMaterial} instance.
	 * <p>
	 * If {@code colorKR} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new MirrorSmallPTMaterial(colorKR, Color3F.BLACK);
	 * }
	 * </pre>
	 * 
	 * @param colorKR a {@link Color3F} instance for the reflection coefficient
	 * @throws NullPointerException thrown if, and only if, {@code colorKR} is {@code null}
	 */
	public MirrorSmallPTMaterial(final Color3F colorKR) {
		this(colorKR, Color3F.BLACK);
	}
	
	/**
	 * Constructs a new {@code MirrorSmallPTMaterial} instance.
	 * <p>
	 * If either {@code colorKR} or {@code colorEmission} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param colorKR a {@link Color3F} instance for the reflection coefficient
	 * @param colorEmission a {@code Color3F} instance for emission
	 * @throws NullPointerException thrown if, and only if, either {@code colorKR} or {@code colorEmission} are {@code null}
	 */
	public MirrorSmallPTMaterial(final Color3F colorKR, final Color3F colorEmission) {
		this.textureKR = new ConstantTexture(Objects.requireNonNull(colorKR, "colorKR == null"));
		this.textureEmission = new ConstantTexture(Objects.requireNonNull(colorEmission, "colorEmission == null"));
	}
	
	/**
	 * Constructs a new {@code MirrorSmallPTMaterial} instance.
	 * <p>
	 * If {@code textureKR} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new MirrorSmallPTMaterial(textureKR, ConstantTexture.BLACK);
	 * }
	 * </pre>
	 * 
	 * @param textureKR a {@link Texture} instance for the reflection coefficient
	 * @throws NullPointerException thrown if, and only if, {@code textureKR} is {@code null}
	 */
	public MirrorSmallPTMaterial(final Texture textureKR) {
		this(textureKR, ConstantTexture.BLACK);
	}
	
	/**
	 * Constructs a new {@code MirrorSmallPTMaterial} instance.
	 * <p>
	 * If either {@code textureKR} or {@code textureEmission} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param textureKR a {@link Texture} instance for the reflection coefficient
	 * @param textureEmission a {@code Texture} instance for emission
	 * @throws NullPointerException thrown if, and only if, either {@code textureKR} or {@code textureEmission} are {@code null}
	 */
	public MirrorSmallPTMaterial(final Texture textureKR, final Texture textureEmission) {
		this.textureKR = Objects.requireNonNull(textureKR, "textureKR == null");
		this.textureEmission = Objects.requireNonNull(textureEmission, "textureEmission == null");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link Color3F} instance with the emittance of this {@code MirrorSmallPTMaterial} instance at {@code intersection}.
	 * <p>
	 * If {@code intersection} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param intersection an {@link Intersection} instance
	 * @return a {@code Color3F} instance with the emittance of this {@code MirrorSmallPTMaterial} instance at {@code intersection}
	 * @throws NullPointerException thrown if, and only if, {@code intersection} is {@code null}
	 */
	@Override
	public Color3F emittance(final Intersection intersection) {
		return this.textureEmission.getColor(intersection);
	}
	
	/**
	 * Samples the distribution function.
	 * <p>
	 * Returns a {@link SmallPTSample} instance with the result of the sampling.
	 * <p>
	 * If {@code intersection} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param intersection an {@link Intersection} instance
	 * @return a {@code SmallPTSample} instance with the result of the sampling
	 * @throws NullPointerException thrown if, and only if, {@code intersection} is {@code null}
	 */
	@Override
	public SmallPTSample sampleDistributionFunction(final Intersection intersection) {
		final Color3F result = this.textureKR.getColor(intersection);
		
		final SurfaceIntersection3F surfaceIntersection = intersection.getSurfaceIntersectionWorldSpace();
		
		final Vector3F oldDirection = surfaceIntersection.getRay().getDirection();
		
		final Vector3F surfaceNormal = surfaceIntersection.getOrthonormalBasisS().getW();
		
		final Vector3F newDirection = Vector3F.reflection(oldDirection, surfaceNormal, true);
		
		return new SmallPTSample(result, newDirection);
	}
	
	/**
	 * Returns a {@code String} with the name of this {@code MirrorSmallPTMaterial} instance.
	 * 
	 * @return a {@code String} with the name of this {@code MirrorSmallPTMaterial} instance
	 */
	@Override
	public String getName() {
		return NAME;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code MirrorSmallPTMaterial} instance.
	 * 
	 * @return a {@code String} representation of this {@code MirrorSmallPTMaterial} instance
	 */
	@Override
	public String toString() {
		return String.format("new MirrorSmallPTMaterial(%s, %s)", this.textureKR, this.textureEmission);
	}
	
	/**
	 * Returns the {@link Texture} instance for emission.
	 * 
	 * @return the {@code Texture} instance for emission
	 */
	public Texture getTextureEmission() {
		return this.textureEmission;
	}
	
	/**
	 * Returns the {@link Texture} instance for the reflection coefficient.
	 * 
	 * @return the {@code Texture} instance for the reflection coefficient
	 */
	public Texture getTextureKR() {
		return this.textureKR;
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
				if(!this.textureEmission.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
				
				if(!this.textureKR.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
			}
			
			return nodeHierarchicalVisitor.visitLeave(this);
		} catch(final RuntimeException e) {
			throw new NodeTraversalException(e);
		}
	}
	
	/**
	 * Compares {@code object} to this {@code MirrorSmallPTMaterial} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code MirrorSmallPTMaterial}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code MirrorSmallPTMaterial} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code MirrorSmallPTMaterial}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof MirrorSmallPTMaterial)) {
			return false;
		} else if(!Objects.equals(this.textureEmission, MirrorSmallPTMaterial.class.cast(object).textureEmission)) {
			return false;
		} else if(!Objects.equals(this.textureKR, MirrorSmallPTMaterial.class.cast(object).textureKR)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns an {@code int} with the ID of this {@code MirrorSmallPTMaterial} instance.
	 * 
	 * @return an {@code int} with the ID of this {@code MirrorSmallPTMaterial} instance
	 */
	@Override
	public int getID() {
		return ID;
	}
	
	/**
	 * Returns a hash code for this {@code MirrorSmallPTMaterial} instance.
	 * 
	 * @return a hash code for this {@code MirrorSmallPTMaterial} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.textureEmission, this.textureKR);
	}
	
	/**
	 * Returns an {@code int[]} representation of this {@code MirrorSmallPTMaterial} instance.
	 * 
	 * @return an {@code int[]} representation of this {@code MirrorSmallPTMaterial} instance
	 */
	public int[] toArray() {
		final int[] array = new int[ARRAY_LENGTH];
		
//		Because the MirrorSmallPTMaterial occupy 4/8 positions in a block, it should be aligned.
		array[ARRAY_OFFSET_TEXTURE_EMISSION_ID] = this.textureEmission.getID();	//Block #1
		array[ARRAY_OFFSET_TEXTURE_EMISSION_OFFSET] = 0;						//Block #1
		array[ARRAY_OFFSET_TEXTURE_K_R_ID] = this.textureKR.getID();			//Block #1
		array[ARRAY_OFFSET_TEXTURE_K_R_OFFSET] = 0;								//Block #1
		
		return array;
	}
}