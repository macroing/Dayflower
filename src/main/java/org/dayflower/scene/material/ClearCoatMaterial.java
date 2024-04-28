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
package org.dayflower.scene.material;

import static org.dayflower.utility.Floats.random;

import java.util.Objects;
import java.util.Optional;

import org.dayflower.color.Color3F;
import org.dayflower.geometry.Vector3F;
import org.dayflower.scene.BSDF;
import org.dayflower.scene.Intersection;
import org.dayflower.scene.Material;
import org.dayflower.scene.ScatteringFunctions;
import org.dayflower.scene.TransportMode;
import org.dayflower.scene.bxdf.LambertianBRDF;
import org.dayflower.scene.bxdf.SpecularBRDF;
import org.dayflower.scene.fresnel.ConstantFresnel;
import org.dayflower.scene.fresnel.DielectricFresnel;
import org.dayflower.scene.modifier.Modifier;
import org.dayflower.scene.modifier.NoOpModifier;
import org.dayflower.scene.texture.ConstantTexture;
import org.dayflower.scene.texture.Texture;

import org.macroing.java.util.visitor.NodeHierarchicalVisitor;
import org.macroing.java.util.visitor.NodeTraversalException;

/**
 * A {@code ClearCoatMaterial} is an implementation of {@link Material} that represents a clear coat material.
 * <p>
 * This class is immutable and thread-safe as long as the {@link Modifier} instance and all {@link Texture} instances are.
 * <p>
 * This {@code Material} implementation is supported on the GPU.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class ClearCoatMaterial implements Material {
	/**
	 * The name of this {@code ClearCoatMaterial} class.
	 */
	public static final String NAME = "Clear Coat";
	
	/**
	 * The ID of this {@code ClearCoatMaterial} class.
	 */
	public static final int ID = 3;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final Modifier modifier;
	private final Texture textureEmission;
	private final Texture textureKD;
	private final Texture textureKS;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code ClearCoatMaterial} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new ClearCoatMaterial(Color3F.GRAY);
	 * }
	 * </pre>
	 */
	public ClearCoatMaterial() {
		this(Color3F.GRAY);
	}
	
	/**
	 * Constructs a new {@code ClearCoatMaterial} instance.
	 * <p>
	 * If {@code colorKD} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new ClearCoatMaterial(colorKD, Color3F.WHITE);
	 * }
	 * </pre>
	 * 
	 * @param colorKD a {@link Color3F} instance for the diffuse coefficient
	 * @throws NullPointerException thrown if, and only if, {@code colorKD} is {@code null}
	 */
	public ClearCoatMaterial(final Color3F colorKD) {
		this(colorKD, Color3F.WHITE);
	}
	
	/**
	 * Constructs a new {@code ClearCoatMaterial} instance.
	 * <p>
	 * If either {@code colorKD} or {@code colorKS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new ClearCoatMaterial(colorKD, colorKS, Color3F.BLACK);
	 * }
	 * </pre>
	 * 
	 * @param colorKD a {@link Color3F} instance for the diffuse coefficient
	 * @param colorKS a {@code Color3F} instance for the specular coefficient
	 * @throws NullPointerException thrown if, and only if, either {@code colorKD} or {@code colorKS} are {@code null}
	 */
	public ClearCoatMaterial(final Color3F colorKD, final Color3F colorKS) {
		this(colorKD, colorKS, Color3F.BLACK);
	}
	
	/**
	 * Constructs a new {@code ClearCoatMaterial} instance.
	 * <p>
	 * If either {@code colorKD}, {@code colorKS} or {@code colorEmission} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new ClearCoatMaterial(colorKD, colorKS, colorEmission, new NoOpModifier());
	 * }
	 * </pre>
	 * 
	 * @param colorKD a {@link Color3F} instance for the diffuse coefficient
	 * @param colorKS a {@code Color3F} instance for the specular coefficient
	 * @param colorEmission a {@code Color3F} instance for emission
	 * @throws NullPointerException thrown if, and only if, either {@code colorKD}, {@code colorKS} or {@code colorEmission} are {@code null}
	 */
	public ClearCoatMaterial(final Color3F colorKD, final Color3F colorKS, final Color3F colorEmission) {
		this(colorKD, colorKS, colorEmission, new NoOpModifier());
	}
	
	/**
	 * Constructs a new {@code ClearCoatMaterial} instance.
	 * <p>
	 * If either {@code colorKD}, {@code colorKS}, {@code colorEmission} or {@code modifier} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param colorKD a {@link Color3F} instance for the diffuse coefficient
	 * @param colorKS a {@code Color3F} instance for the specular coefficient
	 * @param colorEmission a {@code Color3F} instance for emission
	 * @param modifier a {@link Modifier} instance
	 * @throws NullPointerException thrown if, and only if, either {@code colorKD}, {@code colorKS}, {@code colorEmission} or {@code modifier} are {@code null}
	 */
	public ClearCoatMaterial(final Color3F colorKD, final Color3F colorKS, final Color3F colorEmission, final Modifier modifier) {
		this.textureKD = new ConstantTexture(Objects.requireNonNull(colorKD, "colorKD == null"));
		this.textureKS = new ConstantTexture(Objects.requireNonNull(colorKS, "colorKS == null"));
		this.textureEmission = new ConstantTexture(Objects.requireNonNull(colorEmission, "colorEmission == null"));
		this.modifier = Objects.requireNonNull(modifier, "modifier == null");
	}
	
	/**
	 * Constructs a new {@code ClearCoatMaterial} instance.
	 * <p>
	 * If {@code textureKD} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new ClearCoatMaterial(textureKD, ConstantTexture.WHITE);
	 * }
	 * </pre>
	 * 
	 * @param textureKD a {@link Texture} instance for the diffuse coefficient
	 * @throws NullPointerException thrown if, and only if, {@code textureKD} is {@code null}
	 */
	public ClearCoatMaterial(final Texture textureKD) {
		this(textureKD, ConstantTexture.WHITE);
	}
	
	/**
	 * Constructs a new {@code ClearCoatMaterial} instance.
	 * <p>
	 * If either {@code textureKD} or {@code textureKS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new ClearCoatMaterial(textureKD, textureKS, ConstantTexture.BLACK);
	 * }
	 * </pre>
	 * 
	 * @param textureKD a {@link Texture} instance for the diffuse coefficient
	 * @param textureKS a {@code Texture} instance for the specular coefficient
	 * @throws NullPointerException thrown if, and only if, either {@code textureKD} or {@code textureKS} are {@code null}
	 */
	public ClearCoatMaterial(final Texture textureKD, final Texture textureKS) {
		this(textureKD, textureKS, ConstantTexture.BLACK);
	}
	
	/**
	 * Constructs a new {@code ClearCoatMaterial} instance.
	 * <p>
	 * If either {@code textureKD}, {@code textureKS} or {@code textureEmission} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new ClearCoatMaterial(textureKD, textureKS, textureEmission, new NoOpModifier());
	 * }
	 * </pre>
	 * 
	 * @param textureKD a {@link Texture} instance for the diffuse coefficient
	 * @param textureKS a {@code Texture} instance for the specular coefficient
	 * @param textureEmission a {@code Texture} instance for emission
	 * @throws NullPointerException thrown if, and only if, either {@code textureKD}, {@code textureKS} or {@code textureEmission} are {@code null}
	 */
	public ClearCoatMaterial(final Texture textureKD, final Texture textureKS, final Texture textureEmission) {
		this(textureKD, textureKS, textureEmission, new NoOpModifier());
	}
	
	/**
	 * Constructs a new {@code ClearCoatMaterial} instance.
	 * <p>
	 * If either {@code textureKD}, {@code textureKS}, {@code textureEmission} or {@code modifier} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param textureKD a {@link Texture} instance for the diffuse coefficient
	 * @param textureKS a {@code Texture} instance for the specular coefficient
	 * @param textureEmission a {@code Texture} instance for emission
	 * @param modifier a {@link Modifier} instance
	 * @throws NullPointerException thrown if, and only if, either {@code textureKD}, {@code textureKS}, {@code textureEmission} or {@code modifier} are {@code null}
	 */
	public ClearCoatMaterial(final Texture textureKD, final Texture textureKS, final Texture textureEmission, final Modifier modifier) {
		this.textureKD = Objects.requireNonNull(textureKD, "textureKD == null");
		this.textureKS = Objects.requireNonNull(textureKS, "textureKS == null");
		this.textureEmission = Objects.requireNonNull(textureEmission, "textureEmission == null");
		this.modifier = Objects.requireNonNull(modifier, "modifier == null");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link Color3F} instance with the emittance of this {@code ClearCoatMaterial} instance at {@code intersection}.
	 * <p>
	 * If {@code intersection} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param intersection an {@link Intersection} instance
	 * @return a {@code Color3F} instance with the emittance of this {@code ClearCoatMaterial} instance at {@code intersection}
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
		
		this.modifier.modify(intersection);
		
		final Color3F colorKD = this.textureKD.getColor(intersection);
		final Color3F colorKS = this.textureKS.getColor(intersection);
		
		final Vector3F direction = intersection.getRay().getDirection();
		
		final Vector3F surfaceNormal = intersection.getSurfaceNormalS();
		final Vector3F surfaceNormalCorrectlyOriented = intersection.getSurfaceNormalSCorrectlyOriented();
		
		final boolean isEntering = Vector3F.dotProduct(surfaceNormal, surfaceNormalCorrectlyOriented) > 0.0F;
		
		final float etaA = 1.0F;
		final float etaB = 1.5F;
		final float etaI = isEntering ? etaA : etaB;
		final float etaT = isEntering ? etaB : etaA;
		final float eta = etaI / etaT;
		
		final Optional<Vector3F> optionalRefractionDirection = Vector3F.refraction(direction, surfaceNormalCorrectlyOriented, eta);
		
		if(optionalRefractionDirection.isPresent()) {
			final Vector3F refractionDirection = optionalRefractionDirection.get();
			
			final float cosThetaI = Vector3F.dotProduct(direction, surfaceNormalCorrectlyOriented);
			final float cosThetaICorrectlyOriented = isEntering ? -cosThetaI : Vector3F.dotProduct(refractionDirection, surfaceNormal);
			
			final float reflectance = DielectricFresnel.evaluate(cosThetaICorrectlyOriented, etaA, etaB);
			final float transmittance = 1.0F - reflectance;
			
			final float probabilityRussianRoulette = 0.25F + 0.5F * reflectance;
			final float probabilityRussianRouletteReflection = reflectance / probabilityRussianRoulette;
			final float probabilityRussianRouletteTransmission = transmittance / (1.0F - probabilityRussianRoulette);
			
			final boolean isChoosingSpecularReflection = random() < probabilityRussianRoulette;
			
			if(isChoosingSpecularReflection) {
				return new ScatteringFunctions(new BSDF(intersection, new SpecularBRDF(Color3F.multiply(colorKS, probabilityRussianRouletteReflection), new ConstantFresnel())));
			}
			
			return new ScatteringFunctions(new BSDF(intersection, new LambertianBRDF(Color3F.multiply(colorKD, probabilityRussianRouletteTransmission))));
		}
		
		return new ScatteringFunctions(new BSDF(intersection, new SpecularBRDF(colorKS, new ConstantFresnel())));
	}
	
	/**
	 * Returns a {@code String} with the name of this {@code ClearCoatMaterial} instance.
	 * 
	 * @return a {@code String} with the name of this {@code ClearCoatMaterial} instance
	 */
	@Override
	public String getName() {
		return NAME;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code ClearCoatMaterial} instance.
	 * 
	 * @return a {@code String} representation of this {@code ClearCoatMaterial} instance
	 */
	@Override
	public String toString() {
		return String.format("new ClearCoatMaterial(%s, %s, %s, %s)", this.textureKD, this.textureKS, this.textureEmission, this.modifier);
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
				if(!this.modifier.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
				
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
	 * Compares {@code object} to this {@code ClearCoatMaterial} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code ClearCoatMaterial}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code ClearCoatMaterial} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code ClearCoatMaterial}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof ClearCoatMaterial)) {
			return false;
		} else if(!Objects.equals(this.modifier, ClearCoatMaterial.class.cast(object).modifier)) {
			return false;
		} else if(!Objects.equals(this.textureEmission, ClearCoatMaterial.class.cast(object).textureEmission)) {
			return false;
		} else if(!Objects.equals(this.textureKD, ClearCoatMaterial.class.cast(object).textureKD)) {
			return false;
		} else if(!Objects.equals(this.textureKS, ClearCoatMaterial.class.cast(object).textureKS)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns an {@code int} with the ID of this {@code ClearCoatMaterial} instance.
	 * 
	 * @return an {@code int} with the ID of this {@code ClearCoatMaterial} instance
	 */
	@Override
	public int getID() {
		return ID;
	}
	
	/**
	 * Returns a hash code for this {@code ClearCoatMaterial} instance.
	 * 
	 * @return a hash code for this {@code ClearCoatMaterial} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.modifier, this.textureEmission, this.textureKD, this.textureKS);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * A {@code Builder} is used to build {@link ClearCoatMaterial} instances.
	 * <p>
	 * This class is mutable and not thread-safe.
	 * 
	 * @since 1.0.0
	 * @author J&#246;rgen Lundgren
	 */
	public static final class Builder {
		private Modifier modifier;
		private Texture textureEmission;
		private Texture textureKD;
		private Texture textureKS;
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		/**
		 * Constructs a new {@code Builder} instance.
		 */
		public Builder() {
			this(new ClearCoatMaterial());
		}
		
		/**
		 * Constructs a new {@code Builder} instance given {@code clearCoatMaterial}.
		 * <p>
		 * If {@code clearCoatMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
		 * 
		 * @param clearCoatMaterial a {@link ClearCoatMaterial} instance
		 * @throws NullPointerException thrown if, and only if, {@code clearCoatMaterial} is {@code null}
		 */
		public Builder(final ClearCoatMaterial clearCoatMaterial) {
			this.modifier = clearCoatMaterial.getModifier();
			this.textureEmission = clearCoatMaterial.getTextureEmission();
			this.textureKD = clearCoatMaterial.getTextureKD();
			this.textureKS = clearCoatMaterial.getTextureKS();
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
		 * Sets the {@link Texture} instance for the diffuse coefficient.
		 * <p>
		 * Returns this {@code Builder} instance.
		 * <p>
		 * If {@code colorKD} is {@code null}, a {@code NullPointerException} will be thrown.
		 * 
		 * @param colorKD a {@link Color3F} instance for the diffuse coefficient
		 * @return this {@code Builder} instance
		 * @throws NullPointerException thrown if, and only if, {@code colorKD} is {@code null}
		 */
		public Builder setTextureKD(final Color3F colorKD) {
			return setTextureKD(new ConstantTexture(Objects.requireNonNull(colorKD, "colorKD == null")));
		}
		
		/**
		 * Sets the {@link Texture} instance for the diffuse coefficient.
		 * <p>
		 * Returns this {@code Builder} instance.
		 * <p>
		 * If {@code textureKD} is {@code null}, a {@code NullPointerException} will be thrown.
		 * 
		 * @param textureKD the {@code Texture} instance for the diffuse coefficient
		 * @return this {@code Builder} instance
		 * @throws NullPointerException thrown if, and only if, {@code textureKD} is {@code null}
		 */
		public Builder setTextureKD(final Texture textureKD) {
			this.textureKD = Objects.requireNonNull(textureKD, "textureKD == null");
			
			return this;
		}
		
		/**
		 * Sets the {@link Texture} instance for the specular coefficient.
		 * <p>
		 * Returns this {@code Builder} instance.
		 * <p>
		 * If {@code colorKS} is {@code null}, a {@code NullPointerException} will be thrown.
		 * 
		 * @param colorKS a {@link Color3F} instance for the specular coefficient
		 * @return this {@code Builder} instance
		 * @throws NullPointerException thrown if, and only if, {@code colorKS} is {@code null}
		 */
		public Builder setTextureKS(final Color3F colorKS) {
			return setTextureKS(new ConstantTexture(Objects.requireNonNull(colorKS, "colorKS == null")));
		}
		
		/**
		 * Sets the {@link Texture} instance for the specular coefficient.
		 * <p>
		 * Returns this {@code Builder} instance.
		 * <p>
		 * If {@code textureKS} is {@code null}, a {@code NullPointerException} will be thrown.
		 * 
		 * @param textureKS the {@code Texture} instance for the specular coefficient
		 * @return this {@code Builder} instance
		 * @throws NullPointerException thrown if, and only if, {@code textureKS} is {@code null}
		 */
		public Builder setTextureKS(final Texture textureKS) {
			this.textureKS = Objects.requireNonNull(textureKS, "textureKS == null");
			
			return this;
		}
		
		/**
		 * Returns a new {@link ClearCoatMaterial} instance.
		 * 
		 * @return a new {@code ClearCoatMaterial} instance
		 */
		public ClearCoatMaterial build() {
			return new ClearCoatMaterial(this.textureKD, this.textureKS, this.textureEmission, this.modifier);
		}
	}
}