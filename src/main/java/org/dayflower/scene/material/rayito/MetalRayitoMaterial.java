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
package org.dayflower.scene.material.rayito;

import java.util.Objects;
import java.util.Optional;

import org.dayflower.color.Color3F;
import org.dayflower.node.NodeHierarchicalVisitor;
import org.dayflower.node.NodeTraversalException;
import org.dayflower.scene.BSSRDF;
import org.dayflower.scene.Intersection;
import org.dayflower.scene.TransportMode;
import org.dayflower.scene.bxdf.rayito.AshikhminShirleyRayitoBRDF;
import org.dayflower.scene.bxdf.rayito.RayitoBSDF;
import org.dayflower.scene.texture.ConstantTexture;
import org.dayflower.scene.texture.Texture;

/**
 * A {@code MetalRayitoMaterial} is an implementation of {@link RayitoMaterial} that represents metal.
 * <p>
 * This class is immutable and thread-safe as long as all {@link Texture} instances are.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class MetalRayitoMaterial implements RayitoMaterial {
	/**
	 * The name of this {@code MetalRayitoMaterial} class.
	 */
	public static final String NAME = "Rayito - Metal";
	
	/**
	 * The length of the {@code int[]}.
	 */
	public static final int ARRAY_LENGTH = 8;
	
	/**
	 * The offset for the ID of the {@link Texture} denoted by {@code KR} in the {@code int[]}.
	 */
	public static final int ARRAY_OFFSET_TEXTURE_K_R_ID = 2;
	
	/**
	 * The offset for the offset of the {@link Texture} denoted by {@code KR} in the {@code int[]}.
	 */
	public static final int ARRAY_OFFSET_TEXTURE_K_R_OFFSET = 3;
	
	/**
	 * The offset for the ID of the {@link Texture} denoted by {@code Roughness} in the {@code int[]}.
	 */
	public static final int ARRAY_OFFSET_TEXTURE_ROUGHNESS_ID = 4;
	
	/**
	 * The offset for the offset of the {@link Texture} denoted by {@code Roughness} in the {@code int[]}.
	 */
	public static final int ARRAY_OFFSET_TEXTURE_ROUGHNESS_OFFSET = 5;
	
	/**
	 * The ID of this {@code MetalRayitoMaterial} class.
	 */
	public static final int ID = 202;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final Texture textureEmission;
	private final Texture textureKR;
	private final Texture textureRoughness;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code MetalRayitoMaterial} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new MetalRayitoMaterial(Color3F.GRAY_0_50);
	 * }
	 * </pre>
	 */
	public MetalRayitoMaterial() {
		this(Color3F.GRAY_0_50);
	}
	
	/**
	 * Constructs a new {@code MetalRayitoMaterial} instance.
	 * <p>
	 * If {@code colorKR} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new MetalRayitoMaterial(colorKR, Color3F.BLACK);
	 * }
	 * </pre>
	 * 
	 * @param colorKR a {@link Color3F} instance for the reflection coefficient
	 * @throws NullPointerException thrown if, and only if, {@code colorKR} is {@code null}
	 */
	public MetalRayitoMaterial(final Color3F colorKR) {
		this(colorKR, Color3F.BLACK);
	}
	
	/**
	 * Constructs a new {@code MetalRayitoMaterial} instance.
	 * <p>
	 * If either {@code colorKR} or {@code colorEmission} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new MetalRayitoMaterial(colorKR, colorEmission, 0.2F);
	 * }
	 * </pre>
	 * 
	 * @param colorKR a {@link Color3F} instance for the reflection coefficient
	 * @param colorEmission a {@code Color3F} instance for emission
	 * @throws NullPointerException thrown if, and only if, either {@code colorKR} or {@code colorEmission} are {@code null}
	 */
	public MetalRayitoMaterial(final Color3F colorKR, final Color3F colorEmission) {
		this(colorKR, colorEmission, 0.2F);
	}
	
	/**
	 * Constructs a new {@code MetalRayitoMaterial} instance.
	 * <p>
	 * If either {@code colorKR} or {@code colorEmission} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param colorKR a {@link Color3F} instance for the reflection coefficient
	 * @param colorEmission a {@code Color3F} instance for emission
	 * @param roughness the roughness to use
	 * @throws NullPointerException thrown if, and only if, either {@code colorKR} or {@code colorEmission} are {@code null}
	 */
	public MetalRayitoMaterial(final Color3F colorKR, final Color3F colorEmission, final float roughness) {
		this.textureKR = new ConstantTexture(Objects.requireNonNull(colorKR, "colorKR == null"));
		this.textureEmission = new ConstantTexture(Objects.requireNonNull(colorEmission, "colorEmission == null"));
		this.textureRoughness = new ConstantTexture(new Color3F(roughness));
	}
	
	/**
	 * Constructs a new {@code MetalRayitoMaterial} instance.
	 * <p>
	 * If {@code textureKR} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new MetalRayitoMaterial(textureKR, ConstantTexture.BLACK);
	 * }
	 * </pre>
	 * 
	 * @param textureKR a {@link Texture} instance for the reflection coefficient
	 * @throws NullPointerException thrown if, and only if, {@code textureKR} is {@code null}
	 */
	public MetalRayitoMaterial(final Texture textureKR) {
		this(textureKR, ConstantTexture.BLACK);
	}
	
	/**
	 * Constructs a new {@code MetalRayitoMaterial} instance.
	 * <p>
	 * If either {@code textureKR} or {@code textureEmission} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new MetalRayitoMaterial(textureKR, textureEmission, 0.2F);
	 * }
	 * </pre>
	 * 
	 * @param textureKR a {@link Texture} instance for the reflection coefficient
	 * @param textureEmission a {@code Texture} instance for emission
	 * @throws NullPointerException thrown if, and only if, either {@code textureKR} or {@code textureEmission} are {@code null}
	 */
	public MetalRayitoMaterial(final Texture textureKR, final Texture textureEmission) {
		this(textureKR, textureEmission, 0.2F);
	}
	
	/**
	 * Constructs a new {@code MetalRayitoMaterial} instance.
	 * <p>
	 * If either {@code textureKR}, {@code textureEmission} or {@code textureRoughness} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param textureKR a {@link Texture} instance for the reflection coefficient
	 * @param textureEmission a {@code Texture} instance for emission
	 * @param textureRoughness a {@code Texture} instance with the roughness to use
	 * @throws NullPointerException thrown if, and only if, either {@code textureKR}, {@code textureEmission} or {@code textureRoughness} are {@code null}
	 */
	public MetalRayitoMaterial(final Texture textureKR, final Texture textureEmission, final Texture textureRoughness) {
		this.textureKR = Objects.requireNonNull(textureKR, "textureKR == null");
		this.textureEmission = Objects.requireNonNull(textureEmission, "textureEmission == null");
		this.textureRoughness = Objects.requireNonNull(textureRoughness, "textureRoughness == null");
	}
	
	/**
	 * Constructs a new {@code MetalRayitoMaterial} instance.
	 * <p>
	 * If either {@code textureKR} or {@code textureEmission} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new MetalRayitoMaterial(textureKR, textureEmission, new ConstantTexture(new Color3F(roughness)));
	 * }
	 * </pre>
	 * 
	 * @param textureKR a {@link Texture} instance for the reflection coefficient
	 * @param textureEmission a {@code Texture} instance for emission
	 * @param roughness the roughness to use
	 * @throws NullPointerException thrown if, and only if, either {@code textureKR} or {@code textureEmission} are {@code null}
	 */
	public MetalRayitoMaterial(final Texture textureKR, final Texture textureEmission, final float roughness) {
		this(textureKR, textureEmission, new ConstantTexture(new Color3F(roughness)));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link Color3F} instance with the emittance of this {@code MetalRayitoMaterial} instance at {@code intersection}.
	 * <p>
	 * If {@code intersection} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param intersection an {@link Intersection} instance
	 * @return a {@code Color3F} instance with the emittance of this {@code MetalRayitoMaterial} instance at {@code intersection}
	 * @throws NullPointerException thrown if, and only if, {@code intersection} is {@code null}
	 */
	@Override
	public Color3F emittance(final Intersection intersection) {
		return this.textureEmission.getColor(intersection);
	}
	
	/**
	 * Computes the {@link BSSRDF} at {@code intersection}.
	 * <p>
	 * Returns an optional {@code BSSRDF} instance.
	 * <p>
	 * If either {@code intersection} or {@code transportMode} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param intersection the {@link Intersection} to compute the {@code BSSRDF} for
	 * @param transportMode the {@link TransportMode} to use
	 * @param isAllowingMultipleLobes {@code true} if, and only if, multiple lobes are allowed, {@code false} otherwise
	 * @return an optional {@code BSSRDF} instance
	 * @throws NullPointerException thrown if, and only if, either {@code intersection} or {@code transportMode} are {@code null}
	 */
	@Override
	public Optional<BSSRDF> computeBSSRDF(final Intersection intersection, final TransportMode transportMode, final boolean isAllowingMultipleLobes) {
		Objects.requireNonNull(intersection, "intersection == null");
		Objects.requireNonNull(transportMode, "transportMode == null");
		
		return Optional.empty();
	}
	
	/**
	 * Computes the {@link RayitoBSDF} at {@code intersection}.
	 * <p>
	 * Returns an optional {@code RayitoBSDF} instance.
	 * <p>
	 * If either {@code intersection} or {@code transportMode} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param intersection the {@link Intersection} to compute the {@code RayitoBSDF} for
	 * @param transportMode the {@link TransportMode} to use
	 * @param isAllowingMultipleLobes {@code true} if, and only if, multiple lobes are allowed, {@code false} otherwise
	 * @return an optional {@code RayitoBSDF} instance
	 * @throws NullPointerException thrown if, and only if, either {@code intersection} or {@code transportMode} are {@code null}
	 */
	@Override
	public Optional<RayitoBSDF> computeBSDF(final Intersection intersection, final TransportMode transportMode, final boolean isAllowingMultipleLobes) {
		Objects.requireNonNull(intersection, "intersection == null");
		Objects.requireNonNull(transportMode, "transportMode == null");
		
		final Color3F colorKR = this.textureKR.getColor(intersection);
		final Color3F colorRoughness = this.textureRoughness.getColor(intersection);
		
		final float roughness = colorRoughness.average();
		
		return Optional.of(new RayitoBSDF(intersection, new AshikhminShirleyRayitoBRDF(colorKR, roughness)));
	}
	
	/**
	 * Returns a {@code String} with the name of this {@code MetalRayitoMaterial} instance.
	 * 
	 * @return a {@code String} with the name of this {@code MetalRayitoMaterial} instance
	 */
	@Override
	public String getName() {
		return NAME;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code MetalRayitoMaterial} instance.
	 * 
	 * @return a {@code String} representation of this {@code MetalRayitoMaterial} instance
	 */
	@Override
	public String toString() {
		return String.format("new MetalRayitoMaterial(%s, %s, %s)", this.textureKR, this.textureEmission, this.textureRoughness);
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
	 * Returns the {@link Texture} instance with the roughness.
	 * 
	 * @return the {@code Texture} instance with the roughness
	 */
	public Texture getTextureRoughness() {
		return this.textureRoughness;
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
				
				if(!this.textureRoughness.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
			}
			
			return nodeHierarchicalVisitor.visitLeave(this);
		} catch(final RuntimeException e) {
			throw new NodeTraversalException(e);
		}
	}
	
	/**
	 * Compares {@code object} to this {@code MetalRayitoMaterial} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code MetalRayitoMaterial}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code MetalRayitoMaterial} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code MetalRayitoMaterial}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof MetalRayitoMaterial)) {
			return false;
		} else if(!Objects.equals(this.textureEmission, MetalRayitoMaterial.class.cast(object).textureEmission)) {
			return false;
		} else if(!Objects.equals(this.textureKR, MetalRayitoMaterial.class.cast(object).textureKR)) {
			return false;
		} else if(!Objects.equals(this.textureRoughness, MetalRayitoMaterial.class.cast(object).textureRoughness)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns an {@code int} with the ID of this {@code MetalRayitoMaterial} instance.
	 * 
	 * @return an {@code int} with the ID of this {@code MetalRayitoMaterial} instance
	 */
	@Override
	public int getID() {
		return ID;
	}
	
	/**
	 * Returns a hash code for this {@code MetalRayitoMaterial} instance.
	 * 
	 * @return a hash code for this {@code MetalRayitoMaterial} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.textureEmission, this.textureKR, this.textureRoughness);
	}
	
	/**
	 * Returns a {@code int[]} representation of this {@code MetalRayitoMaterial} instance.
	 * 
	 * @return a {@code int[]} representation of this {@code MetalRayitoMaterial} instance
	 */
	public int[] toArray() {
		final int[] array = new int[ARRAY_LENGTH];
		
//		Because the MetalRayitoMaterial occupy 8/8 positions in a block, it should be aligned.
		array[ARRAY_OFFSET_TEXTURE_EMISSION_ID] = this.textureEmission.getID();		//Block #1
		array[ARRAY_OFFSET_TEXTURE_EMISSION_OFFSET] = 0;							//Block #1
		array[ARRAY_OFFSET_TEXTURE_K_R_ID] = this.textureKR.getID();				//Block #1
		array[ARRAY_OFFSET_TEXTURE_K_R_OFFSET] = 0;									//Block #1
		array[ARRAY_OFFSET_TEXTURE_ROUGHNESS_ID] = this.textureRoughness.getID();	//Block #1
		array[ARRAY_OFFSET_TEXTURE_ROUGHNESS_OFFSET] = 0;							//Block #1
		array[6] = 0;																//Block #1
		array[7] = 0;																//Block #1
		
		return array;
	}
}