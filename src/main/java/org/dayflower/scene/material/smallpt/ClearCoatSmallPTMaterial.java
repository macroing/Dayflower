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
package org.dayflower.scene.material.smallpt;

import static org.dayflower.util.Floats.random;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.Optional;

import org.dayflower.geometry.OrthonormalBasis33F;
import org.dayflower.geometry.SampleGeneratorF;
import org.dayflower.geometry.SurfaceIntersection3F;
import org.dayflower.geometry.Vector3F;
import org.dayflower.image.Color3F;
import org.dayflower.scene.Intersection;
import org.dayflower.scene.fresnel.DielectricFresnel;
import org.dayflower.scene.texture.ConstantTexture;
import org.dayflower.scene.texture.Texture;

//TODO: Add Javadocs!
public final class ClearCoatSmallPTMaterial extends SmallPTMaterial {
//	TODO: Add Javadocs!
	public static final String NAME = "SmallPT - Clear Coat";
	
	/**
	 * The ID of this {@code ClearCoatSmallPTMaterial} class.
	 */
	public static final int ID = 300;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final Texture textureEmittance;
	private final Texture textureDiffuseReflectanceScale;
	private final Texture textureSpecularReflectanceScale;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public ClearCoatSmallPTMaterial() {
		this(Color3F.BLACK);
	}
	
//	TODO: Add Javadocs!
	public ClearCoatSmallPTMaterial(final Color3F colorEmittance) {
		this(colorEmittance, Color3F.GRAY);
	}
	
//	TODO: Add Javadocs!
	public ClearCoatSmallPTMaterial(final Color3F colorEmittance, final Color3F colorDiffuseReflectance) {
		this(colorEmittance, colorDiffuseReflectance, Color3F.WHITE);
	}
	
//	TODO: Add Javadocs!
	public ClearCoatSmallPTMaterial(final Color3F colorEmittance, final Color3F colorDiffuseReflectance, final Color3F colorSpecularReflectance) {
		this.textureEmittance = new ConstantTexture(Objects.requireNonNull(colorEmittance, "colorEmittance == null"));
		this.textureDiffuseReflectanceScale = new ConstantTexture(Objects.requireNonNull(colorDiffuseReflectance, "colorDiffuseReflectance == null"));
		this.textureSpecularReflectanceScale = new ConstantTexture(Objects.requireNonNull(colorSpecularReflectance, "colorSpecularReflectance == null"));
	}
	
//	TODO: Add Javadocs!
	public ClearCoatSmallPTMaterial(final Texture textureEmittance, final Texture textureDiffuseReflectanceScale, final Texture textureSpecularReflectanceScale) {
		this.textureEmittance = Objects.requireNonNull(textureEmittance, "textureEmittance == null");
		this.textureDiffuseReflectanceScale = Objects.requireNonNull(textureDiffuseReflectanceScale, "textureDiffuseReflectanceScale == null");
		this.textureSpecularReflectanceScale = Objects.requireNonNull(textureSpecularReflectanceScale, "textureSpecularReflectanceScale == null");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	@Override
	public Color3F emittance(final Intersection intersection) {
		return this.textureEmittance.getColor(intersection);
	}
	
//	TODO: Add Javadocs!
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
				final Color3F result = Color3F.multiply(this.textureSpecularReflectanceScale.getColor(intersection), probabilityRussianRouletteReflection);
				
				final Vector3F newDirection = Vector3F.reflection(oldDirection, surfaceNormal, true);
				
				return new SmallPTSample(result, newDirection);
			}
			
			final Color3F result = Color3F.multiply(this.textureDiffuseReflectanceScale.getColor(intersection), probabilityRussianRouletteTransmission);
			
			final OrthonormalBasis33F orthonormalBasis = new OrthonormalBasis33F(surfaceNormalCorrectlyOriented);
			
			final Vector3F newDirectionLocalSpace = SampleGeneratorF.sampleHemisphereCosineDistribution2();
			final Vector3F newDirection = Vector3F.transform(newDirectionLocalSpace, orthonormalBasis);
			
			return new SmallPTSample(result, newDirection);
		}
		
		final Color3F result = this.textureSpecularReflectanceScale.getColor(intersection);
		
		final Vector3F newDirection = Vector3F.reflection(oldDirection, surfaceNormal, true);
		
		return new SmallPTSample(result, newDirection);
	}
	
//	TODO: Add Javadocs!
	@Override
	public String getName() {
		return NAME;
	}
	
//	TODO: Add Javadocs!
	@Override
	public String toString() {
		return String.format("new ClearCoatSmallPTMaterial(%s, %s, %s)", this.textureEmittance, this.textureDiffuseReflectanceScale, this.textureSpecularReflectanceScale);
	}
	
//	TODO: Add Javadocs!
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof ClearCoatSmallPTMaterial)) {
			return false;
		} else if(!Objects.equals(this.textureEmittance, ClearCoatSmallPTMaterial.class.cast(object).textureEmittance)) {
			return false;
		} else if(!Objects.equals(this.textureDiffuseReflectanceScale, ClearCoatSmallPTMaterial.class.cast(object).textureDiffuseReflectanceScale)) {
			return false;
		} else if(!Objects.equals(this.textureSpecularReflectanceScale, ClearCoatSmallPTMaterial.class.cast(object).textureSpecularReflectanceScale)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns a {@code float[]} representation of this {@code ClearCoatSmallPTMaterial} instance.
	 * 
	 * @return a {@code float[]} representation of this {@code ClearCoatSmallPTMaterial} instance
	 */
	@Override
	public float[] toArray() {
		return new float[0];//TODO: Implement!
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
	
//	TODO: Add Javadocs!
	@Override
	public int hashCode() {
		return Objects.hash(this.textureEmittance, this.textureDiffuseReflectanceScale, this.textureSpecularReflectanceScale);
	}
}