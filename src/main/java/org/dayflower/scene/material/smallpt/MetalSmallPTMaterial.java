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

import static org.dayflower.util.Floats.equal;
import static org.dayflower.util.Floats.random;

import java.lang.reflect.Field;
import java.util.Objects;

import org.dayflower.geometry.OrthonormalBasis33F;
import org.dayflower.geometry.SampleGeneratorF;
import org.dayflower.geometry.SurfaceIntersection3F;
import org.dayflower.geometry.Vector3F;
import org.dayflower.image.Color3F;
import org.dayflower.scene.Intersection;
import org.dayflower.scene.texture.ConstantTexture;
import org.dayflower.scene.texture.Texture;

//TODO: Add Javadocs!
public final class MetalSmallPTMaterial extends SmallPTMaterial {
//	TODO: Add Javadocs!
	public static final String NAME = "SmallPT - Metal";
	
	/**
	 * The ID of this {@code MetalSmallPTMaterial} class.
	 */
	public static final int ID = 303;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final Texture textureEmittance;
	private final Texture textureReflectanceScale;
	private final float exponent;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public MetalSmallPTMaterial() {
		this(Color3F.BLACK);
	}
	
//	TODO: Add Javadocs!
	public MetalSmallPTMaterial(final Color3F colorEmittance) {
		this(colorEmittance, Color3F.GRAY);
	}
	
//	TODO: Add Javadocs!
	public MetalSmallPTMaterial(final Color3F colorEmittance, final Color3F colorReflectance) {
		this(colorEmittance, colorReflectance, 20.0F);
	}
	
//	TODO: Add Javadocs!
	public MetalSmallPTMaterial(final Color3F colorEmittance, final Color3F colorReflectance, final float exponent) {
		this.textureEmittance = new ConstantTexture(Objects.requireNonNull(colorEmittance, "colorEmittance == null"));
		this.textureReflectanceScale = new ConstantTexture(Objects.requireNonNull(colorReflectance, "colorReflectance == null"));
		this.exponent = exponent;
	}
	
//	TODO: Add Javadocs!
	public MetalSmallPTMaterial(final Texture textureEmittance, final Texture textureReflectanceScale) {
		this(textureEmittance, textureReflectanceScale, 20.0F);
	}
	
//	TODO: Add Javadocs!
	public MetalSmallPTMaterial(final Texture textureEmittance, final Texture textureReflectanceScale, final float exponent) {
		this.textureEmittance = Objects.requireNonNull(textureEmittance, "textureEmittance == null");
		this.textureReflectanceScale = Objects.requireNonNull(textureReflectanceScale, "textureReflectanceScale == null");
		this.exponent = exponent;
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
		final Color3F result = this.textureReflectanceScale.getColor(intersection);
		
		final SurfaceIntersection3F surfaceIntersection = intersection.getSurfaceIntersectionWorldSpace();
		
		final Vector3F oldDirection = surfaceIntersection.getRay().getDirection();
		
		final Vector3F surfaceNormal = surfaceIntersection.getOrthonormalBasisS().getW();
		
		final Vector3F reflectionDirection = Vector3F.reflection(oldDirection, surfaceNormal, true);
		
		final OrthonormalBasis33F orthonormalBasis = new OrthonormalBasis33F(reflectionDirection);
		
		final Vector3F newDirectionLocalSpace = SampleGeneratorF.sampleHemispherePowerCosineDistribution(random(), random(), 20.0F);
		final Vector3F newDirection = Vector3F.transform(newDirectionLocalSpace, orthonormalBasis);
		
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
		return String.format("new MetalSmallPTMaterial(%s, %s, %+.10f)", this.textureEmittance, this.textureReflectanceScale, Float.valueOf(this.exponent));
	}
	
//	TODO: Add Javadocs!
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof MetalSmallPTMaterial)) {
			return false;
		} else if(!Objects.equals(this.textureEmittance, MetalSmallPTMaterial.class.cast(object).textureEmittance)) {
			return false;
		} else if(!Objects.equals(this.textureReflectanceScale, MetalSmallPTMaterial.class.cast(object).textureReflectanceScale)) {
			return false;
		} else if(!equal(this.exponent, MetalSmallPTMaterial.class.cast(object).exponent)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns a {@code float[]} representation of this {@code MetalSmallPTMaterial} instance.
	 * 
	 * @return a {@code float[]} representation of this {@code MetalSmallPTMaterial} instance
	 */
	@Override
	public float[] toArray() {
		return new float[0];//TODO: Implement!
	}
	
	/**
	 * Returns an {@code int} with the ID of this {@code MetalSmallPTMaterial} instance.
	 * 
	 * @return an {@code int} with the ID of this {@code MetalSmallPTMaterial} instance
	 */
	@Override
	public int getID() {
		return ID;
	}
	
//	TODO: Add Javadocs!
	@Override
	public int hashCode() {
		return Objects.hash(this.textureEmittance, this.textureReflectanceScale, Float.valueOf(this.exponent));
	}
}