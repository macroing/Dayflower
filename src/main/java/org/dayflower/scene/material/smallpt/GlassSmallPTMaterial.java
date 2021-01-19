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

import static org.dayflower.util.Floats.random;

import java.util.Objects;
import java.util.Optional;

import org.dayflower.color.Color3F;
import org.dayflower.geometry.SurfaceIntersection3F;
import org.dayflower.geometry.Vector3F;
import org.dayflower.node.NodeHierarchicalVisitor;
import org.dayflower.node.NodeTraversalException;
import org.dayflower.scene.Intersection;
import org.dayflower.scene.fresnel.DielectricFresnel;
import org.dayflower.scene.texture.ConstantTexture;
import org.dayflower.scene.texture.Texture;

/**
 * A {@code GlassSmallPTMaterial} is an implementation of {@link SmallPTMaterial} that represents glass.
 * <p>
 * This class is immutable and thread-safe as long as all {@link Texture} instances are.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class GlassSmallPTMaterial extends SmallPTMaterial {
	/**
	 * The name of this {@code GlassSmallPTMaterial} class.
	 */
	public static final String NAME = "SmallPT - Glass";
	
	/**
	 * The length of the {@code int[]}.
	 */
	public static final int ARRAY_LENGTH = 8;
	
	/**
	 * The offset for the ID of the {@link Texture} denoted by {@code Eta} in the {@code int[]}.
	 */
	public static final int ARRAY_OFFSET_TEXTURE_ETA_ID = 2;
	
	/**
	 * The offset for the offset of the {@link Texture} denoted by {@code Eta} in the {@code int[]}.
	 */
	public static final int ARRAY_OFFSET_TEXTURE_ETA_OFFSET = 3;
	
	/**
	 * The offset for the ID of the {@link Texture} denoted by {@code KR} in the {@code int[]}.
	 */
	public static final int ARRAY_OFFSET_TEXTURE_K_R_ID = 4;
	
	/**
	 * The offset for the offset of the {@link Texture} denoted by {@code KR} in the {@code int[]}.
	 */
	public static final int ARRAY_OFFSET_TEXTURE_K_R_OFFSET = 5;
	
	/**
	 * The offset for the ID of the {@link Texture} denoted by {@code KT} in the {@code int[]}.
	 */
	public static final int ARRAY_OFFSET_TEXTURE_K_T_ID = 6;
	
	/**
	 * The offset for the offset of the {@link Texture} denoted by {@code KT} in the {@code int[]}.
	 */
	public static final int ARRAY_OFFSET_TEXTURE_K_T_OFFSET = 7;
	
	/**
	 * The ID of this {@code GlassSmallPTMaterial} class.
	 */
	public static final int ID = 301;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final Texture textureEmission;
	private final Texture textureEta;
	private final Texture textureKR;
	private final Texture textureKT;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code GlassSmallPTMaterial} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new GlassSmallPTMaterial(Color3F.WHITE);
	 * }
	 * </pre>
	 */
	public GlassSmallPTMaterial() {
		this(Color3F.WHITE);
	}
	
	/**
	 * Constructs a new {@code GlassSmallPTMaterial} instance.
	 * <p>
	 * If {@code colorKR} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new GlassSmallPTMaterial(colorKR, Color3F.WHITE);
	 * }
	 * </pre>
	 * 
	 * @param colorKR a {@link Color3F} instance for the reflection coefficient
	 * @throws NullPointerException thrown if, and only if, {@code colorKR} is {@code null}
	 */
	public GlassSmallPTMaterial(final Color3F colorKR) {
		this(colorKR, Color3F.WHITE);
	}
	
	/**
	 * Constructs a new {@code GlassSmallPTMaterial} instance.
	 * <p>
	 * If either {@code colorKR} or {@code colorKT} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new GlassSmallPTMaterial(colorKR, colorKT, Color3F.BLACK);
	 * }
	 * </pre>
	 * 
	 * @param colorKR a {@link Color3F} instance for the reflection coefficient
	 * @param colorKT a {@code Color3F} instance for the transmission coefficient
	 * @throws NullPointerException thrown if, and only if, either {@code colorKR} or {@code colorKT} are {@code null}
	 */
	public GlassSmallPTMaterial(final Color3F colorKR, final Color3F colorKT) {
		this(colorKR, colorKT, Color3F.BLACK);
	}
	
	/**
	 * Constructs a new {@code GlassSmallPTMaterial} instance.
	 * <p>
	 * If either {@code colorKR}, {@code colorKT} or {@code colorEmission} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new GlassSmallPTMaterial(colorKR, colorKT, colorEmission, Color3F.GRAY_1_50);
	 * }
	 * </pre>
	 * 
	 * @param colorKR a {@link Color3F} instance for the reflection coefficient
	 * @param colorKT a {@code Color3F} instance for the transmission coefficient
	 * @param colorEmission a {@code Color3F} instance for emission
	 * @throws NullPointerException thrown if, and only if, either {@code colorKR}, {@code colorKT} or {@code colorEmission} are {@code null}
	 */
	public GlassSmallPTMaterial(final Color3F colorKR, final Color3F colorKT, final Color3F colorEmission) {
		this(colorKR, colorKT, colorEmission, Color3F.GRAY_1_50);
	}
	
	/**
	 * Constructs a new {@code GlassSmallPTMaterial} instance.
	 * <p>
	 * If either {@code colorKR}, {@code colorKT}, {@code colorEmission} or {@code colorEta} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param colorKR a {@link Color3F} instance for the reflection coefficient
	 * @param colorKT a {@code Color3F} instance for the transmission coefficient
	 * @param colorEmission a {@code Color3F} instance for emission
	 * @param colorEta a {@code Color3F} instance for the index of refraction (IOR)
	 * @throws NullPointerException thrown if, and only if, either {@code colorKR}, {@code colorKT}, {@code colorEmission} or {@code colorEta} are {@code null}
	 */
	public GlassSmallPTMaterial(final Color3F colorKR, final Color3F colorKT, final Color3F colorEmission, final Color3F colorEta) {
		this.textureKR = new ConstantTexture(Objects.requireNonNull(colorKR, "colorKR == null"));
		this.textureKT = new ConstantTexture(Objects.requireNonNull(colorKT, "colorKT == null"));
		this.textureEmission = new ConstantTexture(Objects.requireNonNull(colorEmission, "colorEmission == null"));
		this.textureEta = new ConstantTexture(Objects.requireNonNull(colorEta, "colorEta == null"));
	}
	
	/**
	 * Constructs a new {@code GlassSmallPTMaterial} instance.
	 * <p>
	 * If {@code textureKR} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new GlassSmallPTMaterial(textureKR, ConstantTexture.WHITE);
	 * }
	 * </pre>
	 * 
	 * @param textureKR a {@link Texture} instance for the reflection coefficient
	 * @throws NullPointerException thrown if, and only if, {@code textureKR} is {@code null}
	 */
	public GlassSmallPTMaterial(final Texture textureKR) {
		this(textureKR, ConstantTexture.WHITE);
	}
	
	/**
	 * Constructs a new {@code GlassSmallPTMaterial} instance.
	 * <p>
	 * If either {@code textureKR} or {@code textureKT} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new GlassSmallPTMaterial(textureKR, textureKT, ConstantTexture.BLACK);
	 * }
	 * </pre>
	 * 
	 * @param textureKR a {@link Texture} instance for the reflection coefficient
	 * @param textureKT a {@code Texture} instance for the transmission coefficient
	 * @throws NullPointerException thrown if, and only if, either {@code textureKR} or {@code textureKT} are {@code null}
	 */
	public GlassSmallPTMaterial(final Texture textureKR, final Texture textureKT) {
		this(textureKR, textureKT, ConstantTexture.BLACK);
	}
	
	/**
	 * Constructs a new {@code GlassSmallPTMaterial} instance.
	 * <p>
	 * If either {@code textureKR}, {@code textureKT} or {@code textureEmission} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new GlassSmallPTMaterial(textureKR, textureKT, textureEmission, ConstantTexture.GRAY_1_50);
	 * }
	 * </pre>
	 * 
	 * @param textureKR a {@link Texture} instance for the reflection coefficient
	 * @param textureKT a {@code Texture} instance for the transmission coefficient
	 * @param textureEmission a {@code Texture} instance for emission
	 * @throws NullPointerException thrown if, and only if, either {@code textureKR}, {@code textureKT} or {@code textureEmission} are {@code null}
	 */
	public GlassSmallPTMaterial(final Texture textureKR, final Texture textureKT, final Texture textureEmission) {
		this(textureKR, textureKT, textureEmission, ConstantTexture.GRAY_1_50);
	}
	
	/**
	 * Constructs a new {@code GlassSmallPTMaterial} instance.
	 * <p>
	 * If either {@code textureKR}, {@code textureKT}, {@code textureEmission} or {@code textureEta} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param textureKR a {@link Texture} instance for the reflection coefficient
	 * @param textureKT a {@code Texture} instance for the transmission coefficient
	 * @param textureEmission a {@code Texture} instance for emission
	 * @param textureEta a {@code Texture} instance for the index of refraction (IOR)
	 * @throws NullPointerException thrown if, and only if, either {@code textureKR}, {@code textureKT}, {@code textureEmission} or {@code textureEta} are {@code null}
	 */
	public GlassSmallPTMaterial(final Texture textureKR, final Texture textureKT, final Texture textureEmission, final Texture textureEta) {
		this.textureKR = Objects.requireNonNull(textureKR, "textureKR == null");
		this.textureKT = Objects.requireNonNull(textureKT, "textureKT == null");
		this.textureEmission = Objects.requireNonNull(textureEmission, "textureEmission == null");
		this.textureEta = Objects.requireNonNull(textureEta, "textureEta == null");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link Color3F} instance with the emittance of this {@code GlassSmallPTMaterial} instance at {@code intersection}.
	 * <p>
	 * If {@code intersection} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param intersection an {@link Intersection} instance
	 * @return a {@code Color3F} instance with the emittance of this {@code GlassSmallPTMaterial} instance at {@code intersection}
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
		final SurfaceIntersection3F surfaceIntersection = intersection.getSurfaceIntersectionWorldSpace();
		
		final Vector3F oldDirection = surfaceIntersection.getRay().getDirection();
		
		final Vector3F surfaceNormal = surfaceIntersection.getOrthonormalBasisS().getW();
		final Vector3F surfaceNormalCorrectlyOriented = Vector3F.faceForwardNegated(surfaceNormal, oldDirection);
		
		final boolean isEntering = Vector3F.dotProduct(surfaceNormal, surfaceNormalCorrectlyOriented) > 0.0F;
		
		final float etaA = 1.0F;
		final float etaB = 1.5F;
		final float etaI = isEntering ? etaA : etaB;
		final float etaT = isEntering ? etaB : etaA;
		final float eta = etaI / etaT;
		
		final Optional<Vector3F> optionalRefractionDirection = Vector3F.refraction2(oldDirection, surfaceNormalCorrectlyOriented, eta);
		
		if(optionalRefractionDirection.isPresent()) {
			final Vector3F refractionDirection = optionalRefractionDirection.get();
			
			final float cosThetaI = Vector3F.dotProduct(oldDirection, surfaceNormalCorrectlyOriented);
			final float cosThetaICorrectlyOriented = isEntering ? -cosThetaI : Vector3F.dotProduct(refractionDirection, surfaceNormal);
			
			final float reflectance = DielectricFresnel.evaluate(cosThetaICorrectlyOriented, etaA, etaB);
			final float transmittance = 1.0F - reflectance;
			
			final float probabilityRussianRoulette = 0.25F + 0.5F * reflectance;
			final float probabilityRussianRouletteReflection = reflectance / probabilityRussianRoulette;
			final float probabilityRussianRouletteTransmission = transmittance / (1.0F - probabilityRussianRoulette);
			
			final boolean isChoosingSpecularReflection = random() < probabilityRussianRoulette;
			
			if(isChoosingSpecularReflection) {
				final Color3F result = Color3F.multiply(this.textureKR.getColor(intersection), probabilityRussianRouletteReflection);
				
				final Vector3F newDirection = Vector3F.reflection(oldDirection, surfaceNormal, true);
				
				return new SmallPTSample(result, newDirection);
			}
			
			final Color3F result = Color3F.multiply(this.textureKT.getColor(intersection), probabilityRussianRouletteTransmission);
			
			final Vector3F newDirection = refractionDirection;
			
			return new SmallPTSample(result, newDirection);
		}
		
		final Color3F result = this.textureKR.getColor(intersection);
		
		final Vector3F newDirection = Vector3F.reflection(oldDirection, surfaceNormal, true);
		
		return new SmallPTSample(result, newDirection);
	}
	
	/**
	 * Returns a {@code String} with the name of this {@code GlassSmallPTMaterial} instance.
	 * 
	 * @return a {@code String} with the name of this {@code GlassSmallPTMaterial} instance
	 */
	@Override
	public String getName() {
		return NAME;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code GlassSmallPTMaterial} instance.
	 * 
	 * @return a {@code String} representation of this {@code GlassSmallPTMaterial} instance
	 */
	@Override
	public String toString() {
		return String.format("new GlassSmallPTMaterial(%s, %s, %s, %s)", this.textureKR, this.textureKT, this.textureEmission, this.textureEta);
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
	 * Returns the {@link Texture} instance for the index of refraction (IOR).
	 * 
	 * @return the {@code Texture} instance for the index of refraction (IOR)
	 */
	public Texture getTextureEta() {
		return this.textureEta;
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
	 * Returns the {@link Texture} instance for the transmission coefficient.
	 * 
	 * @return the {@code Texture} instance for the transmission coefficient
	 */
	public Texture getTextureKT() {
		return this.textureKT;
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
				
				if(!this.textureEta.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
				
				if(!this.textureKR.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
				
				if(!this.textureKT.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
			}
			
			return nodeHierarchicalVisitor.visitLeave(this);
		} catch(final RuntimeException e) {
			throw new NodeTraversalException(e);
		}
	}
	
	/**
	 * Compares {@code object} to this {@code GlassSmallPTMaterial} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code GlassSmallPTMaterial}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code GlassSmallPTMaterial} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code GlassSmallPTMaterial}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof GlassSmallPTMaterial)) {
			return false;
		} else if(!Objects.equals(this.textureEmission, GlassSmallPTMaterial.class.cast(object).textureEmission)) {
			return false;
		} else if(!Objects.equals(this.textureEta, GlassSmallPTMaterial.class.cast(object).textureEta)) {
			return false;
		} else if(!Objects.equals(this.textureKR, GlassSmallPTMaterial.class.cast(object).textureKR)) {
			return false;
		} else if(!Objects.equals(this.textureKT, GlassSmallPTMaterial.class.cast(object).textureKT)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns an {@code int} with the ID of this {@code GlassSmallPTMaterial} instance.
	 * 
	 * @return an {@code int} with the ID of this {@code GlassSmallPTMaterial} instance
	 */
	@Override
	public int getID() {
		return ID;
	}
	
	/**
	 * Returns a hash code for this {@code GlassSmallPTMaterial} instance.
	 * 
	 * @return a hash code for this {@code GlassSmallPTMaterial} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.textureEmission, this.textureEta, this.textureKR, this.textureKT);
	}
	
	/**
	 * Returns an {@code int[]} representation of this {@code GlassSmallPTMaterial} instance.
	 * 
	 * @return an {@code int[]} representation of this {@code GlassSmallPTMaterial} instance
	 */
	public int[] toArray() {
		final int[] array = new int[ARRAY_LENGTH];
		
//		Because the GlassSmallPTMaterial occupy 8/8 positions in a block, it should be aligned.
		array[ARRAY_OFFSET_TEXTURE_EMISSION_ID] = this.textureEmission.getID();	//Block #1
		array[ARRAY_OFFSET_TEXTURE_EMISSION_OFFSET] = 0;						//Block #1
		array[ARRAY_OFFSET_TEXTURE_ETA_ID] = this.textureEta.getID();			//Block #1
		array[ARRAY_OFFSET_TEXTURE_ETA_OFFSET] = 0;								//Block #1
		array[ARRAY_OFFSET_TEXTURE_K_R_ID] = this.textureKR.getID();			//Block #1
		array[ARRAY_OFFSET_TEXTURE_K_R_OFFSET] = 0;								//Block #1
		array[ARRAY_OFFSET_TEXTURE_K_T_ID] = this.textureKT.getID();			//Block #1
		array[ARRAY_OFFSET_TEXTURE_K_T_OFFSET] = 0;								//Block #1
		
		return array;
	}
}