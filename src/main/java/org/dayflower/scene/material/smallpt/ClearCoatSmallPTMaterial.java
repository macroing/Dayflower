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
import org.dayflower.geometry.OrthonormalBasis33F;
import org.dayflower.geometry.SampleGeneratorF;
import org.dayflower.geometry.SurfaceIntersection3F;
import org.dayflower.geometry.Vector3F;
import org.dayflower.node.NodeHierarchicalVisitor;
import org.dayflower.node.NodeTraversalException;
import org.dayflower.scene.Intersection;
import org.dayflower.scene.fresnel.DielectricFresnel;
import org.dayflower.scene.texture.ConstantTexture;
import org.dayflower.scene.texture.Texture;

/**
 * A {@code ClearCoatSmallPTMaterial} is an implementation of {@link SmallPTMaterial} that represents clear coat.
 * <p>
 * This class is immutable and thread-safe as long as all {@link Texture} instances are.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class ClearCoatSmallPTMaterial extends SmallPTMaterial {
	/**
	 * The name of this {@code ClearCoatSmallPTMaterial} class.
	 */
	public static final String NAME = "SmallPT - Clear Coat";
	
	/**
	 * The length of the {@code int[]}.
	 */
	public static final int ARRAY_LENGTH = 8;
	
	/**
	 * The offset for the ID of the {@link Texture} denoted by {@code KD} in the {@code int[]}.
	 */
	public static final int ARRAY_OFFSET_TEXTURE_K_D_ID = 2;
	
	/**
	 * The offset for the offset of the {@link Texture} denoted by {@code KD} in the {@code int[]}.
	 */
	public static final int ARRAY_OFFSET_TEXTURE_K_D_OFFSET = 3;
	
	/**
	 * The offset for the ID of the {@link Texture} denoted by {@code KS} in the {@code int[]}.
	 */
	public static final int ARRAY_OFFSET_TEXTURE_K_S_ID = 4;
	
	/**
	 * The offset for the offset of the {@link Texture} denoted by {@code KS} in the {@code int[]}.
	 */
	public static final int ARRAY_OFFSET_TEXTURE_K_S_OFFSET = 5;
	
	/**
	 * The ID of this {@code ClearCoatSmallPTMaterial} class.
	 */
	public static final int ID = 300;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final Texture textureEmission;
	private final Texture textureKD;
	private final Texture textureKS;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code ClearCoatSmallPTMaterial} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new ClearCoatSmallPTMaterial(Color3F.GRAY_0_50);
	 * }
	 * </pre>
	 */
	public ClearCoatSmallPTMaterial() {
		this(Color3F.GRAY_0_50);
	}
	
	/**
	 * Constructs a new {@code ClearCoatSmallPTMaterial} instance.
	 * <p>
	 * If {@code colorKD} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new ClearCoatSmallPTMaterial(colorKD, Color3F.WHITE);
	 * }
	 * </pre>
	 * 
	 * @param colorKD a {@link Color3F} instance for the diffuse coefficient
	 * @throws NullPointerException thrown if, and only if, {@code colorKD} is {@code null}
	 */
	public ClearCoatSmallPTMaterial(final Color3F colorKD) {
		this(colorKD, Color3F.WHITE);
	}
	
	/**
	 * Constructs a new {@code ClearCoatSmallPTMaterial} instance.
	 * <p>
	 * If either {@code colorKD} or {@code colorKS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new ClearCoatSmallPTMaterial(colorKD, colorKS, Color3F.BLACK);
	 * }
	 * </pre>
	 * 
	 * @param colorKD a {@link Color3F} instance for the diffuse coefficient
	 * @param colorKS a {@code Color3F} instance for the specular coefficient
	 * @throws NullPointerException thrown if, and only if, either {@code colorKD} or {@code colorKS} are {@code null}
	 */
	public ClearCoatSmallPTMaterial(final Color3F colorKD, final Color3F colorKS) {
		this(colorKD, colorKS, Color3F.BLACK);
	}
	
	/**
	 * Constructs a new {@code ClearCoatSmallPTMaterial} instance.
	 * <p>
	 * If either {@code colorKD}, {@code colorKS} or {@code colorEmission} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param colorKD a {@link Color3F} instance for the diffuse coefficient
	 * @param colorKS a {@code Color3F} instance for the specular coefficient
	 * @param colorEmission a {@code Color3F} instance for emission
	 * @throws NullPointerException thrown if, and only if, either {@code colorKD}, {@code colorKS} or {@code colorEmission} are {@code null}
	 */
	public ClearCoatSmallPTMaterial(final Color3F colorKD, final Color3F colorKS, final Color3F colorEmission) {
		this.textureKD = new ConstantTexture(Objects.requireNonNull(colorKD, "colorKD == null"));
		this.textureKS = new ConstantTexture(Objects.requireNonNull(colorKS, "colorKS == null"));
		this.textureEmission = new ConstantTexture(Objects.requireNonNull(colorEmission, "colorEmission == null"));
	}
	
	/**
	 * Constructs a new {@code ClearCoatSmallPTMaterial} instance.
	 * <p>
	 * If {@code textureKD} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new ClearCoatSmallPTMaterial(textureKD, ConstantTexture.WHITE);
	 * }
	 * </pre>
	 * 
	 * @param textureKD a {@link Texture} instance for the diffuse coefficient
	 * @throws NullPointerException thrown if, and only if, {@code textureKD} is {@code null}
	 */
	public ClearCoatSmallPTMaterial(final Texture textureKD) {
		this(textureKD, ConstantTexture.WHITE);
	}
	
	/**
	 * Constructs a new {@code ClearCoatSmallPTMaterial} instance.
	 * <p>
	 * If either {@code textureKD} or {@code textureKS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new ClearCoatSmallPTMaterial(textureKD, textureKS, ConstantTexture.BLACK);
	 * }
	 * </pre>
	 * 
	 * @param textureKD a {@link Texture} instance for the diffuse coefficient
	 * @param textureKS a {@code Texture} instance for the specular coefficient
	 * @throws NullPointerException thrown if, and only if, either {@code textureKD} or {@code textureKS} are {@code null}
	 */
	public ClearCoatSmallPTMaterial(final Texture textureKD, final Texture textureKS) {
		this(textureKD, textureKS, ConstantTexture.BLACK);
	}
	
	/**
	 * Constructs a new {@code ClearCoatSmallPTMaterial} instance.
	 * <p>
	 * If either {@code textureKD}, {@code textureKS} or {@code textureEmission} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param textureKD a {@link Texture} instance for the diffuse coefficient
	 * @param textureKS a {@code Texture} instance for the specular coefficient
	 * @param textureEmission a {@code Texture} instance for emission
	 * @throws NullPointerException thrown if, and only if, either {@code textureKD}, {@code textureKS} or {@code textureEmission} are {@code null}
	 */
	public ClearCoatSmallPTMaterial(final Texture textureKD, final Texture textureKS, final Texture textureEmission) {
		this.textureKD = Objects.requireNonNull(textureKD, "textureKD == null");
		this.textureKS = Objects.requireNonNull(textureKS, "textureKS == null");
		this.textureEmission = Objects.requireNonNull(textureEmission, "textureEmission == null");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link Color3F} instance with the emittance of this {@code ClearCoatSmallPTMaterial} instance at {@code intersection}.
	 * <p>
	 * If {@code intersection} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param intersection an {@link Intersection} instance
	 * @return a {@code Color3F} instance with the emittance of this {@code ClearCoatSmallPTMaterial} instance at {@code intersection}
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
				final Color3F result = Color3F.multiply(this.textureKS.getColor(intersection), probabilityRussianRouletteReflection);
				
				final Vector3F newDirection = Vector3F.reflection(oldDirection, surfaceNormal, true);
				
				return new SmallPTSample(result, newDirection);
			}
			
			final Color3F result = Color3F.multiply(this.textureKD.getColor(intersection), probabilityRussianRouletteTransmission);
			
			final OrthonormalBasis33F orthonormalBasis = new OrthonormalBasis33F(surfaceNormalCorrectlyOriented);
			
			final Vector3F newDirectionLocalSpace = SampleGeneratorF.sampleHemisphereCosineDistribution2();
			final Vector3F newDirection = Vector3F.transform(newDirectionLocalSpace, orthonormalBasis);
			
			return new SmallPTSample(result, newDirection);
		}
		
		final Color3F result = this.textureKS.getColor(intersection);
		
		final Vector3F newDirection = Vector3F.reflection(oldDirection, surfaceNormal, true);
		
		return new SmallPTSample(result, newDirection);
	}
	
	/**
	 * Returns a {@code String} with the name of this {@code ClearCoatSmallPTMaterial} instance.
	 * 
	 * @return a {@code String} with the name of this {@code ClearCoatSmallPTMaterial} instance
	 */
	@Override
	public String getName() {
		return NAME;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code ClearCoatSmallPTMaterial} instance.
	 * 
	 * @return a {@code String} representation of this {@code ClearCoatSmallPTMaterial} instance
	 */
	@Override
	public String toString() {
		return String.format("new ClearCoatSmallPTMaterial(%s, %s, %s)", this.textureKD, this.textureKS, this.textureEmission);
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
	 * Returns the {@link Texture} instance for the diffuse coefficient.
	 * 
	 * @return the {@code Texture} instance for the diffuse coefficient
	 */
	public Texture getTextureKD() {
		return this.textureKD;
	}
	
	/**
	 * Returns the {@link Texture} instance for the specular coefficient.
	 * 
	 * @return the {@code Texture} instance for the specular coefficient
	 */
	public Texture getTextureKS() {
		return this.textureKS;
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
				
				if(!this.textureKD.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
				
				if(!this.textureKS.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
			}
			
			return nodeHierarchicalVisitor.visitLeave(this);
		} catch(final RuntimeException e) {
			throw new NodeTraversalException(e);
		}
	}
	
	/**
	 * Compares {@code object} to this {@code ClearCoatSmallPTMaterial} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code ClearCoatSmallPTMaterial}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code ClearCoatSmallPTMaterial} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code ClearCoatSmallPTMaterial}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof ClearCoatSmallPTMaterial)) {
			return false;
		} else if(!Objects.equals(this.textureEmission, ClearCoatSmallPTMaterial.class.cast(object).textureEmission)) {
			return false;
		} else if(!Objects.equals(this.textureKD, ClearCoatSmallPTMaterial.class.cast(object).textureKD)) {
			return false;
		} else if(!Objects.equals(this.textureKS, ClearCoatSmallPTMaterial.class.cast(object).textureKS)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns an {@code int} with the ID of this {@code ClearCoatSmallPTMaterial} instance.
	 * 
	 * @return an {@code int} with the ID of this {@code ClearCoatSmallPTMaterial} instance
	 */
	@Override
	public int getID() {
		return ID;
	}
	
	/**
	 * Returns a hash code for this {@code ClearCoatSmallPTMaterial} instance.
	 * 
	 * @return a hash code for this {@code ClearCoatSmallPTMaterial} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.textureEmission, this.textureKD, this.textureKS);
	}
	
	/**
	 * Returns an {@code int[]} representation of this {@code ClearCoatSmallPTMaterial} instance.
	 * 
	 * @return an {@code int[]} representation of this {@code ClearCoatSmallPTMaterial} instance
	 */
	public int[] toArray() {
		final int[] array = new int[ARRAY_LENGTH];
		
//		Because the ClearCoatSmallPTMaterial occupy 8/8 positions in a block, it should be aligned.
		array[ARRAY_OFFSET_TEXTURE_EMISSION_ID] = this.textureEmission.getID();	//Block #1
		array[ARRAY_OFFSET_TEXTURE_EMISSION_OFFSET] = 0;						//Block #1
		array[ARRAY_OFFSET_TEXTURE_K_D_ID] = this.textureKD.getID();			//Block #1
		array[ARRAY_OFFSET_TEXTURE_K_D_OFFSET] = 0;								//Block #1
		array[ARRAY_OFFSET_TEXTURE_K_S_ID] = this.textureKS.getID();			//Block #1
		array[ARRAY_OFFSET_TEXTURE_K_S_OFFSET] = 0;								//Block #1
		array[6] = 0;															//Block #1
		array[7] = 0;															//Block #1
		
		return array;
	}
}