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
package org.dayflower.scene.material;

import static org.dayflower.utility.Ints.pack;

import java.util.Objects;
import java.util.Optional;

import org.dayflower.color.Color3F;
import org.dayflower.node.NodeHierarchicalVisitor;
import org.dayflower.node.NodeTraversalException;
import org.dayflower.scene.BSDF;
import org.dayflower.scene.BSSRDF;
import org.dayflower.scene.Intersection;
import org.dayflower.scene.Material;
import org.dayflower.scene.TransportMode;
import org.dayflower.scene.bxdf.TorranceSparrowBRDF;
import org.dayflower.scene.fresnel.ConductorFresnel;
import org.dayflower.scene.microfacet.TrowbridgeReitzMicrofacetDistribution;
import org.dayflower.scene.modifier.Modifier;
import org.dayflower.scene.modifier.NoOpModifier;
import org.dayflower.scene.texture.ConstantTexture;
import org.dayflower.scene.texture.Texture;

/**
 * A {@code GlossyMaterial} is an implementation of {@link Material} that represents a glossy material.
 * <p>
 * This class is immutable and thread-safe as long as the {@link Modifier} instance and all {@link Texture} instances are.
 * <p>
 * This {@code Material} implementation is supported on the GPU.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class GlossyMaterial implements Material {
	/**
	 * The name of this {@code GlossyMaterial} class.
	 */
	public static final String NAME = "Glossy";
	
	/**
	 * The length of the {@code int[]}.
	 */
	public static final int ARRAY_LENGTH = 2;
	
	/**
	 * The IDs and offsets for the {@link Texture} instances denoted by {@code KR} and {@code Roughness} in the {@code int[]}.
	 */
	public static final int ARRAY_OFFSET_TEXTURE_K_R_AND_TEXTURE_ROUGHNESS = 1;
	
	/**
	 * The ID of this {@code GlossyMaterial} class.
	 */
	public static final int ID = 106;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final Modifier modifier;
	private final Texture textureEmission;
	private final Texture textureKR;
	private final Texture textureRoughness;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code GlossyMaterial} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new GlossyMaterial(Color3F.GRAY_0_50);
	 * }
	 * </pre>
	 */
	public GlossyMaterial() {
		this(Color3F.GRAY_0_50);
	}
	
	/**
	 * Constructs a new {@code GlossyMaterial} instance.
	 * <p>
	 * If {@code colorKR} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new GlossyMaterial(colorKR, Color3F.BLACK);
	 * }
	 * </pre>
	 * 
	 * @param colorKR a {@link Color3F} instance for the reflection coefficient
	 * @throws NullPointerException thrown if, and only if, {@code colorKR} is {@code null}
	 */
	public GlossyMaterial(final Color3F colorKR) {
		this(colorKR, Color3F.BLACK);
	}
	
	/**
	 * Constructs a new {@code GlossyMaterial} instance.
	 * <p>
	 * If either {@code colorKR} or {@code colorEmission} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new GlossyMaterial(colorKR, colorEmission, 0.2F);
	 * }
	 * </pre>
	 * 
	 * @param colorKR a {@link Color3F} instance for the reflection coefficient
	 * @param colorEmission a {@code Color3F} instance for emission
	 * @throws NullPointerException thrown if, and only if, either {@code colorKR} or {@code colorEmission} are {@code null}
	 */
	public GlossyMaterial(final Color3F colorKR, final Color3F colorEmission) {
		this(colorKR, colorEmission, 0.2F);
	}
	
	/**
	 * Constructs a new {@code GlossyMaterial} instance.
	 * <p>
	 * If either {@code colorKR} or {@code colorEmission} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new GlossyMaterial(colorKR, colorEmission, roughness, new NoOpModifier());
	 * }
	 * </pre>
	 * 
	 * @param colorKR a {@link Color3F} instance for the reflection coefficient
	 * @param colorEmission a {@code Color3F} instance for emission
	 * @param roughness the roughness to use
	 * @throws NullPointerException thrown if, and only if, either {@code colorKR} or {@code colorEmission} are {@code null}
	 */
	public GlossyMaterial(final Color3F colorKR, final Color3F colorEmission, final float roughness) {
		this(colorKR, colorEmission, roughness, new NoOpModifier());
	}
	
	/**
	 * Constructs a new {@code GlossyMaterial} instance.
	 * <p>
	 * If either {@code colorKR}, {@code colorEmission} or {@code modifier} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param colorKR a {@link Color3F} instance for the reflection coefficient
	 * @param colorEmission a {@code Color3F} instance for emission
	 * @param roughness the roughness to use
	 * @param modifier a {@link Modifier} instance
	 * @throws NullPointerException thrown if, and only if, either {@code colorKR}, {@code colorEmission} or {@code modifier} are {@code null}
	 */
	public GlossyMaterial(final Color3F colorKR, final Color3F colorEmission, final float roughness, final Modifier modifier) {
		this.textureKR = new ConstantTexture(Objects.requireNonNull(colorKR, "colorKR == null"));
		this.textureEmission = new ConstantTexture(Objects.requireNonNull(colorEmission, "colorEmission == null"));
		this.textureRoughness = new ConstantTexture(new Color3F(roughness));
		this.modifier = Objects.requireNonNull(modifier, "modifier == null");
	}
	
	/**
	 * Constructs a new {@code GlossyMaterial} instance.
	 * <p>
	 * If {@code textureKR} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new GlossyMaterial(textureKR, ConstantTexture.BLACK);
	 * }
	 * </pre>
	 * 
	 * @param textureKR a {@link Texture} instance for the reflection coefficient
	 * @throws NullPointerException thrown if, and only if, {@code textureKR} is {@code null}
	 */
	public GlossyMaterial(final Texture textureKR) {
		this(textureKR, ConstantTexture.BLACK);
	}
	
	/**
	 * Constructs a new {@code GlossyMaterial} instance.
	 * <p>
	 * If either {@code textureKR} or {@code textureEmission} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new GlossyMaterial(textureKR, textureEmission, 0.2F);
	 * }
	 * </pre>
	 * 
	 * @param textureKR a {@link Texture} instance for the reflection coefficient
	 * @param textureEmission a {@code Texture} instance for emission
	 * @throws NullPointerException thrown if, and only if, either {@code textureKR} or {@code textureEmission} are {@code null}
	 */
	public GlossyMaterial(final Texture textureKR, final Texture textureEmission) {
		this(textureKR, textureEmission, 0.2F);
	}
	
	/**
	 * Constructs a new {@code GlossyMaterial} instance.
	 * <p>
	 * If either {@code textureKR}, {@code textureEmission} or {@code textureRoughness} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new GlossyMaterial(textureKR, textureEmission, textureRoughness, new NoOpModifier());
	 * }
	 * </pre>
	 * 
	 * @param textureKR a {@link Texture} instance for the reflection coefficient
	 * @param textureEmission a {@code Texture} instance for emission
	 * @param textureRoughness a {@code Texture} instance with the roughness to use
	 * @throws NullPointerException thrown if, and only if, either {@code textureKR}, {@code textureEmission} or {@code textureRoughness} are {@code null}
	 */
	public GlossyMaterial(final Texture textureKR, final Texture textureEmission, final Texture textureRoughness) {
		this(textureKR, textureEmission, textureRoughness, new NoOpModifier());
	}
	
	/**
	 * Constructs a new {@code GlossyMaterial} instance.
	 * <p>
	 * If either {@code textureKR}, {@code textureEmission}, {@code textureRoughness} or {@code modifier} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param textureKR a {@link Texture} instance for the reflection coefficient
	 * @param textureEmission a {@code Texture} instance for emission
	 * @param textureRoughness a {@code Texture} instance with the roughness to use
	 * @param modifier a {@link Modifier} instance
	 * @throws NullPointerException thrown if, and only if, either {@code textureKR}, {@code textureEmission}, {@code textureRoughness} or {@code modifier} are {@code null}
	 */
	public GlossyMaterial(final Texture textureKR, final Texture textureEmission, final Texture textureRoughness, final Modifier modifier) {
		this.textureKR = Objects.requireNonNull(textureKR, "textureKR == null");
		this.textureEmission = Objects.requireNonNull(textureEmission, "textureEmission == null");
		this.textureRoughness = Objects.requireNonNull(textureRoughness, "textureRoughness == null");
		this.modifier = Objects.requireNonNull(modifier, "modifier == null");
	}
	
	/**
	 * Constructs a new {@code GlossyMaterial} instance.
	 * <p>
	 * If either {@code textureKR} or {@code textureEmission} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new GlossyMaterial(textureKR, textureEmission, roughness, new NoOpModifier());
	 * }
	 * </pre>
	 * 
	 * @param textureKR a {@link Texture} instance for the reflection coefficient
	 * @param textureEmission a {@code Texture} instance for emission
	 * @param roughness the roughness to use
	 * @throws NullPointerException thrown if, and only if, either {@code textureKR} or {@code textureEmission} are {@code null}
	 */
	public GlossyMaterial(final Texture textureKR, final Texture textureEmission, final float roughness) {
		this(textureKR, textureEmission, roughness, new NoOpModifier());
	}
	
	/**
	 * Constructs a new {@code GlossyMaterial} instance.
	 * <p>
	 * If either {@code textureKR}, {@code textureEmission} or {@code modifier} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new GlossyMaterial(textureKR, textureEmission, new ConstantTexture(roughness), modifier);
	 * }
	 * </pre>
	 * 
	 * @param textureKR a {@link Texture} instance for the reflection coefficient
	 * @param textureEmission a {@code Texture} instance for emission
	 * @param roughness the roughness to use
	 * @param modifier a {@link Modifier} instance
	 * @throws NullPointerException thrown if, and only if, either {@code textureKR}, {@code textureEmission} or {@code modifier} are {@code null}
	 */
	public GlossyMaterial(final Texture textureKR, final Texture textureEmission, final float roughness, final Modifier modifier) {
		this(textureKR, textureEmission, new ConstantTexture(roughness), modifier);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link Color3F} instance with the emittance of this {@code GlossyMaterial} instance at {@code intersection}.
	 * <p>
	 * If {@code intersection} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param intersection an {@link Intersection} instance
	 * @return a {@code Color3F} instance with the emittance of this {@code GlossyMaterial} instance at {@code intersection}
	 * @throws NullPointerException thrown if, and only if, {@code intersection} is {@code null}
	 */
	@Override
	public Color3F emittance(final Intersection intersection) {
		Objects.requireNonNull(intersection, "intersection == null");
		
		return this.textureEmission.getColor(intersection);
	}
	
	/**
	 * Returns the {@link Modifier} instance.
	 * 
	 * @return the {@code Modifier} instance
	 */
	public Modifier getModifier() {
		return this.modifier;
	}
	
	/**
	 * Computes the {@link BSDF} at {@code intersection}.
	 * <p>
	 * Returns an optional {@code BSDF} instance.
	 * <p>
	 * If either {@code intersection} or {@code transportMode} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param intersection the {@link Intersection} to compute the {@code BSDF} for
	 * @param transportMode the {@link TransportMode} to use
	 * @param isAllowingMultipleLobes {@code true} if, and only if, multiple lobes are allowed, {@code false} otherwise
	 * @return an optional {@code BSDF} instance
	 * @throws NullPointerException thrown if, and only if, either {@code intersection} or {@code transportMode} are {@code null}
	 */
	@Override
	public Optional<BSDF> computeBSDF(final Intersection intersection, final TransportMode transportMode, final boolean isAllowingMultipleLobes) {
		Objects.requireNonNull(intersection, "intersection == null");
		Objects.requireNonNull(transportMode, "transportMode == null");
		
		this.modifier.modify(intersection);
		
		final Color3F colorKR = this.textureKR.getColor(intersection);
		
		final float floatRoughness = this.textureRoughness.getFloat(intersection);
		
//		The old version used the AshikhminShirleyBRDF:
//		return Optional.of(new BSDF(intersection, new AshikhminShirleyBRDF(colorKR, floatRoughness), true));
		return Optional.of(new BSDF(intersection, new TorranceSparrowBRDF(colorKR, new ConductorFresnel(colorKR, Color3F.WHITE, Color3F.WHITE), new TrowbridgeReitzMicrofacetDistribution(true, false, floatRoughness, floatRoughness))));
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
	 * Returns a {@code String} with the name of this {@code GlossyMaterial} instance.
	 * 
	 * @return a {@code String} with the name of this {@code GlossyMaterial} instance
	 */
	@Override
	public String getName() {
		return NAME;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code GlossyMaterial} instance.
	 * 
	 * @return a {@code String} representation of this {@code GlossyMaterial} instance
	 */
	@Override
	public String toString() {
		return String.format("new GlossyMaterial(%s, %s, %s)", this.textureKR, this.textureEmission, this.textureRoughness);
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
	 * Compares {@code object} to this {@code GlossyMaterial} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code GlossyMaterial}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code GlossyMaterial} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code GlossyMaterial}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof GlossyMaterial)) {
			return false;
		} else if(!Objects.equals(this.textureEmission, GlossyMaterial.class.cast(object).textureEmission)) {
			return false;
		} else if(!Objects.equals(this.textureKR, GlossyMaterial.class.cast(object).textureKR)) {
			return false;
		} else if(!Objects.equals(this.textureRoughness, GlossyMaterial.class.cast(object).textureRoughness)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns an {@code int} with the ID of this {@code GlossyMaterial} instance.
	 * 
	 * @return an {@code int} with the ID of this {@code GlossyMaterial} instance
	 */
	@Override
	public int getID() {
		return ID;
	}
	
	/**
	 * Returns a hash code for this {@code GlossyMaterial} instance.
	 * 
	 * @return a hash code for this {@code GlossyMaterial} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.textureEmission, this.textureKR, this.textureRoughness);
	}
	
	/**
	 * Returns a {@code int[]} representation of this {@code GlossyMaterial} instance.
	 * 
	 * @return a {@code int[]} representation of this {@code GlossyMaterial} instance
	 */
	public int[] toArray() {
		final int[] array = new int[ARRAY_LENGTH];
		
//		Because the GlossyMaterial occupy 2/8 positions in a block, it should be aligned.
		array[ARRAY_OFFSET_TEXTURE_EMISSION] = pack(this.textureEmission.getID(), 0, 0, 0);											//Block #1
		array[ARRAY_OFFSET_TEXTURE_K_R_AND_TEXTURE_ROUGHNESS] = pack(this.textureKR.getID(), 0, this.textureRoughness.getID(), 0);	//Block #1
		
		return array;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * A {@code Builder} is used to build {@link GlossyMaterial} instances.
	 * <p>
	 * This class is mutable and not thread-safe.
	 * 
	 * @since 1.0.0
	 * @author J&#246;rgen Lundgren
	 */
	public static final class Builder {
		private Modifier modifier;
		private Texture textureEmission;
		private Texture textureKR;
		private Texture textureRoughness;
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		/**
		 * Constructs a new {@code Builder} instance.
		 */
		public Builder() {
			this(new GlossyMaterial());
		}
		
		/**
		 * Constructs a new {@code Builder} instance given {@code glossyMaterial}.
		 * <p>
		 * If {@code glossyMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
		 * 
		 * @param glossyMaterial a {@link GlossyMaterial} instance
		 * @throws NullPointerException thrown if, and only if, {@code glossyMaterial} is {@code null}
		 */
		public Builder(final GlossyMaterial glossyMaterial) {
			this.modifier = glossyMaterial.getModifier();
			this.textureEmission = glossyMaterial.getTextureEmission();
			this.textureKR = glossyMaterial.getTextureKR();
			this.textureRoughness = glossyMaterial.getTextureRoughness();
		}
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		/**
		 * Sets the {@link Modifier} instance.
		 * <p>
		 * Returns this {@code Builder} instance.
		 * <p>
		 * If {@code modifier} is {@code null}, a {@code NullPointerException} will be thrown.
		 * 
		 * @param modifier the {@code Modifier} instance
		 * @return this {@code Builder} instance
		 * @throws NullPointerException thrown if, and only if, {@code modifier} is {@code null}
		 */
		public Builder setModifier(final Modifier modifier) {
			this.modifier = Objects.requireNonNull(modifier, "modifier == null");
			
			return this;
		}
		
		/**
		 * Sets the {@link Texture} instance for emission.
		 * <p>
		 * Returns this {@code Builder} instance.
		 * <p>
		 * If {@code colorEmission} is {@code null}, a {@code NullPointerException} will be thrown.
		 * 
		 * @param colorEmission a {@link Color3F} instance for emission
		 * @return this {@code Builder} instance
		 * @throws NullPointerException thrown if, and only if, {@code colorEmission} is {@code null}
		 */
		public Builder setTextureEmission(final Color3F colorEmission) {
			return setTextureEmission(new ConstantTexture(Objects.requireNonNull(colorEmission, "colorEmission == null")));
		}
		
		/**
		 * Sets the {@link Texture} instance for emission.
		 * <p>
		 * Returns this {@code Builder} instance.
		 * <p>
		 * If {@code textureEmission} is {@code null}, a {@code NullPointerException} will be thrown.
		 * 
		 * @param textureEmission the {@code Texture} instance for emission
		 * @return this {@code Builder} instance
		 * @throws NullPointerException thrown if, and only if, {@code textureEmission} is {@code null}
		 */
		public Builder setTextureEmission(final Texture textureEmission) {
			this.textureEmission = Objects.requireNonNull(textureEmission, "textureEmission == null");
			
			return this;
		}
		
		/**
		 * Sets the {@link Texture} instance for the reflection coefficient.
		 * <p>
		 * Returns this {@code Builder} instance.
		 * <p>
		 * If {@code colorKR} is {@code null}, a {@code NullPointerException} will be thrown.
		 * 
		 * @param colorKR a {@link Color3F} instance for the reflection coefficient
		 * @return this {@code Builder} instance
		 * @throws NullPointerException thrown if, and only if, {@code colorKR} is {@code null}
		 */
		public Builder setTextureKR(final Color3F colorKR) {
			return setTextureKR(new ConstantTexture(Objects.requireNonNull(colorKR, "colorKR == null")));
		}
		
		/**
		 * Sets the {@link Texture} instance for the reflection coefficient.
		 * <p>
		 * Returns this {@code Builder} instance.
		 * <p>
		 * If {@code textureKR} is {@code null}, a {@code NullPointerException} will be thrown.
		 * 
		 * @param textureKR the {@code Texture} instance for the reflection coefficient
		 * @return this {@code Builder} instance
		 * @throws NullPointerException thrown if, and only if, {@code textureKR} is {@code null}
		 */
		public Builder setTextureKR(final Texture textureKR) {
			this.textureKR = Objects.requireNonNull(textureKR, "textureKR == null");
			
			return this;
		}
		
		/**
		 * Sets the {@link Texture} instance for the roughness.
		 * <p>
		 * Returns this {@code Builder} instance.
		 * <p>
		 * If {@code textureRoughness} is {@code null}, a {@code NullPointerException} will be thrown.
		 * 
		 * @param textureRoughness the {@code Texture} instance for the roughness
		 * @return this {@code Builder} instance
		 * @throws NullPointerException thrown if, and only if, {@code textureRoughness} is {@code null}
		 */
		public Builder setTextureRoughness(final Texture textureRoughness) {
			this.textureRoughness = Objects.requireNonNull(textureRoughness, "textureRoughness == null");
			
			return this;
		}
		
		/**
		 * Sets the {@link Texture} instance for the roughness.
		 * <p>
		 * Returns this {@code Builder} instance.
		 * 
		 * @param floatRoughness a {@code float} for the roughness
		 * @return this {@code Builder} instance
		 */
		public Builder setTextureRoughness(final float floatRoughness) {
			return setTextureRoughness(new ConstantTexture(floatRoughness));
		}
		
		/**
		 * Returns a new {@link GlossyMaterial} instance.
		 * 
		 * @return a new {@code GlossyMaterial} instance
		 */
		public GlossyMaterial build() {
			return new GlossyMaterial(this.textureKR, this.textureEmission, this.textureRoughness, this.modifier);
		}
	}
}