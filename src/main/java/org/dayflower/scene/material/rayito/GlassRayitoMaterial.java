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
package org.dayflower.scene.material.rayito;

import static org.dayflower.util.Floats.equal;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

import org.dayflower.image.Color3F;
import org.dayflower.node.NodeHierarchicalVisitor;
import org.dayflower.node.NodeTraversalException;
import org.dayflower.scene.BSSRDF;
import org.dayflower.scene.Intersection;
import org.dayflower.scene.TransportMode;
import org.dayflower.scene.bxdf.rayito.RayitoBSDF;
import org.dayflower.scene.bxdf.rayito.SpecularRayitoBTDF;
import org.dayflower.scene.texture.ConstantTexture;
import org.dayflower.scene.texture.Texture;

/**
 * A {@code GlassRayitoMaterial} is an implementation of {@link RayitoMaterial} that represents glass.
 * <p>
 * This class is immutable and thread-safe as long as all {@link Texture} instances are.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class GlassRayitoMaterial implements RayitoMaterial {
	/**
	 * The name of this {@code GlassRayitoMaterial} class.
	 */
	public static final String NAME = "Rayito - Glass";
	
	/**
	 * The ID of this {@code GlassRayitoMaterial} class.
	 */
	public static final int ID = 200;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final Texture textureAlbedo;
	private final Texture textureEmittance;
	private final float etaA;
	private final float etaB;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code GlassRayitoMaterial} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new GlassRayitoMaterial(Color3F.WHITE);
	 * }
	 * </pre>
	 */
	public GlassRayitoMaterial() {
		this(Color3F.WHITE);
	}
	
	/**
	 * Constructs a new {@code GlassRayitoMaterial} instance.
	 * <p>
	 * If {@code colorAlbedo} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new GlassRayitoMaterial(colorAlbedo, Color3F.BLACK);
	 * }
	 * </pre>
	 * 
	 * @param colorAlbedo a {@link Color3F} instance with the albedo color
	 * @throws NullPointerException thrown if, and only if, {@code colorAlbedo} is {@code null}
	 */
	public GlassRayitoMaterial(final Color3F colorAlbedo) {
		this(colorAlbedo, Color3F.BLACK);
	}
	
	/**
	 * Constructs a new {@code GlassRayitoMaterial} instance.
	 * <p>
	 * If either {@code colorAlbedo} or {@code colorEmittance} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new GlassRayitoMaterial(colorAlbedo, colorEmittance, 1.0F, 1.5F);
	 * }
	 * </pre>
	 * 
	 * @param colorAlbedo a {@link Color3F} instance with the albedo color
	 * @param colorEmittance a {@code Color3F} instance with the emittance
	 * @throws NullPointerException thrown if, and only if, either {@code colorAlbedo} or {@code colorEmittance} are {@code null}
	 */
	public GlassRayitoMaterial(final Color3F colorAlbedo, final Color3F colorEmittance) {
		this(colorAlbedo, colorEmittance, 1.0F, 1.5F);
	}
	
	/**
	 * Constructs a new {@code GlassRayitoMaterial} instance.
	 * <p>
	 * If either {@code colorAlbedo} or {@code colorEmittance} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new GlassRayitoMaterial(new ConstantTexture(colorAlbedo), new ConstantTexture(colorEmittance), etaA, etaB);
	 * }
	 * </pre>
	 * 
	 * @param colorAlbedo a {@link Color3F} instance with the albedo color
	 * @param colorEmittance a {@code Color3F} instance with the emittance
	 * @param etaA the index of refraction denoted by {@code A}
	 * @param etaB the index of refraction denoted by {@code B}
	 * @throws NullPointerException thrown if, and only if, either {@code colorAlbedo} or {@code colorEmittance} are {@code null}
	 */
	public GlassRayitoMaterial(final Color3F colorAlbedo, final Color3F colorEmittance, final float etaA, final float etaB) {
		this(new ConstantTexture(colorAlbedo), new ConstantTexture(colorEmittance), etaA, etaB);
	}
	
	/**
	 * Constructs a new {@code GlassRayitoMaterial} instance.
	 * <p>
	 * If {@code textureAlbedo} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new GlassRayitoMaterial(textureAlbedo, ConstantTexture.BLACK);
	 * }
	 * </pre>
	 * 
	 * @param textureAlbedo a {@link Texture} instance with the albedo color
	 * @throws NullPointerException thrown if, and only if, {@code textureAlbedo} is {@code null}
	 */
	public GlassRayitoMaterial(final Texture textureAlbedo) {
		this(textureAlbedo, ConstantTexture.BLACK);
	}
	
	/**
	 * Constructs a new {@code GlassRayitoMaterial} instance.
	 * <p>
	 * If either {@code textureAlbedo} or {@code textureEmittance} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new GlassRayitoMaterial(textureAlbedo, textureEmittance, 1.0F, 1.5F);
	 * }
	 * </pre>
	 * 
	 * @param textureAlbedo a {@link Texture} instance with the albedo color
	 * @param textureEmittance a {@code Texture} instance with the emittance
	 * @throws NullPointerException thrown if, and only if, either {@code textureAlbedo} or {@code textureEmittance} are {@code null}
	 */
	public GlassRayitoMaterial(final Texture textureAlbedo, final Texture textureEmittance) {
		this(textureAlbedo, textureEmittance, 1.0F, 1.5F);
	}
	
	/**
	 * Constructs a new {@code GlassRayitoMaterial} instance.
	 * <p>
	 * If either {@code textureAlbedo} or {@code textureEmittance} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param textureAlbedo a {@link Texture} instance with the albedo color
	 * @param textureEmittance a {@code Texture} instance with the emittance
	 * @param etaA the index of refraction denoted by {@code A}
	 * @param etaB the index of refraction denoted by {@code B}
	 * @throws NullPointerException thrown if, and only if, either {@code textureAlbedo} or {@code textureEmittance} are {@code null}
	 */
	public GlassRayitoMaterial(final Texture textureAlbedo, final Texture textureEmittance, final float etaA, final float etaB) {
		this.textureAlbedo = Objects.requireNonNull(textureAlbedo, "textureAlbedo == null");
		this.textureEmittance = Objects.requireNonNull(textureEmittance, "textureEmittance == null");
		this.etaA = etaA;
		this.etaB = etaB;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link Color3F} instance with the emittance of this {@code GlassRayitoMaterial} instance at {@code intersection}.
	 * <p>
	 * If {@code intersection} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param intersection an {@link Intersection} instance
	 * @return a {@code Color3F} instance with the emittance of this {@code GlassRayitoMaterial} instance at {@code intersection}
	 * @throws NullPointerException thrown if, and only if, {@code intersection} is {@code null}
	 */
	@Override
	public Color3F emittance(final Intersection intersection) {
		return this.textureEmittance.getColor(intersection);
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
		
		return Optional.of(new RayitoBSDF(intersection, Arrays.asList(new SpecularRayitoBTDF(Color3F.WHITE, this.textureAlbedo.getColor(intersection), this.etaA, this.etaB)), this.etaB));
	}
	
	/**
	 * Returns a {@code String} with the name of this {@code GlassRayitoMaterial} instance.
	 * 
	 * @return a {@code String} with the name of this {@code GlassRayitoMaterial} instance
	 */
	@Override
	public String getName() {
		return NAME;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code GlassRayitoMaterial} instance.
	 * 
	 * @return a {@code String} representation of this {@code GlassRayitoMaterial} instance
	 */
	@Override
	public String toString() {
		return "new GlassRayitoMaterial(...)";
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
				if(!this.textureAlbedo.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
				
				if(!this.textureEmittance.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
			}
			
			return nodeHierarchicalVisitor.visitLeave(this);
		} catch(final RuntimeException e) {
			throw new NodeTraversalException(e);
		}
	}
	
	/**
	 * Compares {@code object} to this {@code GlassRayitoMaterial} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code GlassRayitoMaterial}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code GlassRayitoMaterial} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code GlassRayitoMaterial}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof GlassRayitoMaterial)) {
			return false;
		} else if(!Objects.equals(this.textureAlbedo, GlassRayitoMaterial.class.cast(object).textureAlbedo)) {
			return false;
		} else if(!Objects.equals(this.textureEmittance, GlassRayitoMaterial.class.cast(object).textureEmittance)) {
			return false;
		} else if(!equal(this.etaA, GlassRayitoMaterial.class.cast(object).etaA)) {
			return false;
		} else if(!equal(this.etaB, GlassRayitoMaterial.class.cast(object).etaB)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns a {@code float[]} representation of this {@code GlassRayitoMaterial} instance.
	 * 
	 * @return a {@code float[]} representation of this {@code GlassRayitoMaterial} instance
	 */
	@Override
	public float[] toArray() {
		return new float[0];//TODO: Implement!
	}
	
	/**
	 * Returns an {@code int} with the ID of this {@code GlassRayitoMaterial} instance.
	 * 
	 * @return an {@code int} with the ID of this {@code GlassRayitoMaterial} instance
	 */
	@Override
	public int getID() {
		return ID;
	}
	
	/**
	 * Returns a hash code for this {@code GlassRayitoMaterial} instance.
	 * 
	 * @return a hash code for this {@code GlassRayitoMaterial} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.textureAlbedo, this.textureEmittance, Float.valueOf(this.etaA), Float.valueOf(this.etaB));
	}
}