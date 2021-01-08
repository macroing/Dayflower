/**
 * Copyright 2020 - 2021 J&#246;rgen Lundgren
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
package org.dayflower.scene.material.pbrt;

import static org.dayflower.util.Floats.isZero;
import static org.dayflower.util.Floats.saturate;

import java.util.Objects;
import java.util.Optional;

import org.dayflower.geometry.AngleF;
import org.dayflower.image.Color3F;
import org.dayflower.node.NodeHierarchicalVisitor;
import org.dayflower.node.NodeTraversalException;
import org.dayflower.scene.BSSRDF;
import org.dayflower.scene.Intersection;
import org.dayflower.scene.TransportMode;
import org.dayflower.scene.bxdf.pbrt.PBRTBSDF;
import org.dayflower.scene.bxdf.pbrt.LambertianPBRTBRDF;
import org.dayflower.scene.bxdf.pbrt.OrenNayarPBRTBRDF;
import org.dayflower.scene.texture.ConstantTexture;
import org.dayflower.scene.texture.Texture;

/**
 * A {@code MattePBRTMaterial} is an implementation of {@link PBRTMaterial} and is used for matte surfaces.
 * <p>
 * This class is immutable and thread-safe as long as all {@link Texture} instances are.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class MattePBRTMaterial implements PBRTMaterial {
	/**
	 * The name of this {@code MattePBRTMaterial} class.
	 */
	public static final String NAME = "PBRT - Matte";
	
	/**
	 * The ID of this {@code MattePBRTMaterial} class.
	 */
	public static final int ID = 102;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final Texture textureAngle;
	private final Texture textureEmission;
	private final Texture textureKD;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code MattePBRTMaterial} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new MattePBRTMaterial(Color3F.GRAY_0_50);
	 * }
	 * </pre>
	 */
	public MattePBRTMaterial() {
		this(Color3F.GRAY_0_50);
	}
	
	/**
	 * Constructs a new {@code MattePBRTMaterial} instance.
	 * <p>
	 * If {@code colorKD} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new MattePBRTMaterial(colorKD, Color3F.BLACK);
	 * }
	 * </pre>
	 * 
	 * @param colorKD a {@link Color3F} instance for the diffuse coefficient
	 * @throws NullPointerException thrown if, and only if, {@code colorKD} is {@code null}
	 */
	public MattePBRTMaterial(final Color3F colorKD) {
		this(colorKD, Color3F.BLACK);
	}
	
	/**
	 * Constructs a new {@code MattePBRTMaterial} instance.
	 * <p>
	 * If either {@code colorKD} or {@code colorEmission} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new MattePBRTMaterial(colorKD, colorEmission, 0.0F);
	 * }
	 * </pre>
	 * 
	 * @param colorKD a {@link Color3F} instance for the diffuse coefficient
	 * @param colorEmission a {@code Color3F} instance for emission
	 * @throws NullPointerException thrown if, and only if, either {@code colorKD} or {@code colorEmission} are {@code null}
	 */
	public MattePBRTMaterial(final Color3F colorKD, final Color3F colorEmission) {
		this(colorKD, colorEmission, 0.0F);
	}
	
	/**
	 * Constructs a new {@code MattePBRTMaterial} instance.
	 * <p>
	 * If either {@code colorKD} or {@code colorEmission} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param colorKD a {@link Color3F} instance for the diffuse coefficient
	 * @param colorEmission a {@code Color3F} instance for emission
	 * @param floatAngle a {@code float} for the angle
	 * @throws NullPointerException thrown if, and only if, either {@code colorKD} or {@code colorEmission} are {@code null}
	 */
	public MattePBRTMaterial(final Color3F colorKD, final Color3F colorEmission, final float floatAngle) {
		this.textureKD = new ConstantTexture(Objects.requireNonNull(colorKD, "colorKD == null"));
		this.textureEmission = new ConstantTexture(Objects.requireNonNull(colorEmission, "colorEmission == null"));
		this.textureAngle = new ConstantTexture(new Color3F(floatAngle));
	}
	
	/**
	 * Constructs a new {@code MattePBRTMaterial} instance.
	 * <p>
	 * If {@code textureKD} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new MattePBRTMaterial(textureKD, ConstantTexture.BLACK);
	 * }
	 * </pre>
	 * 
	 * @param textureKD a {@link Texture} instance for the diffuse coefficient
	 * @throws NullPointerException thrown if, and only if, {@code textureKD} is {@code null}
	 */
	public MattePBRTMaterial(final Texture textureKD) {
		this(textureKD, ConstantTexture.BLACK);
	}
	
	/**
	 * Constructs a new {@code MattePBRTMaterial} instance.
	 * <p>
	 * If either {@code textureKD} or {@code textureEmission} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new MattePBRTMaterial(textureKD, textureEmission, ConstantTexture.BLACK);
	 * }
	 * </pre>
	 * 
	 * @param textureKD a {@link Texture} instance for the diffuse coefficient
	 * @param textureEmission a {@code Texture} instance for emission
	 * @throws NullPointerException thrown if, and only if, either {@code textureKD} or {@code textureEmission} are {@code null}
	 */
	public MattePBRTMaterial(final Texture textureKD, final Texture textureEmission) {
		this(textureKD, textureEmission, ConstantTexture.BLACK);
	}
	
	/**
	 * Constructs a new {@code MattePBRTMaterial} instance.
	 * <p>
	 * If either {@code textureKD}, {@code textureEmission} or {@code textureAngle} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param textureKD a {@link Texture} instance for the diffuse coefficient
	 * @param textureEmission a {@code Texture} instance for emission
	 * @param textureAngle a {@code Texture} instance for the angle
	 * @throws NullPointerException thrown if, and only if, either {@code textureKD}, {@code textureEmission} or {@code textureAngle} are {@code null}
	 */
	public MattePBRTMaterial(final Texture textureKD, final Texture textureEmission, final Texture textureAngle) {
		this.textureKD = Objects.requireNonNull(textureKD, "textureKD == null");
		this.textureEmission = Objects.requireNonNull(textureEmission, "textureEmission == null");
		this.textureAngle = Objects.requireNonNull(textureAngle, "textureAngle == null");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link Color3F} instance with the emittance of this {@code MattePBRTMaterial} instance at {@code intersection}.
	 * <p>
	 * If {@code intersection} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param intersection an {@link Intersection} instance
	 * @return a {@code Color3F} instance with the emittance of this {@code MattePBRTMaterial} instance at {@code intersection}
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
	 * Computes the {@link PBRTBSDF} at {@code intersection}.
	 * <p>
	 * Returns an optional {@code PBRTBSDF} instance.
	 * <p>
	 * If either {@code intersection} or {@code transportMode} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param intersection the {@link Intersection} to compute the {@code PBRTBSDF} for
	 * @param transportMode the {@link TransportMode} to use
	 * @param isAllowingMultipleLobes {@code true} if, and only if, multiple lobes are allowed, {@code false} otherwise
	 * @return an optional {@code PBRTBSDF} instance
	 * @throws NullPointerException thrown if, and only if, either {@code intersection} or {@code transportMode} are {@code null}
	 */
	@Override
	public Optional<PBRTBSDF> computeBSDF(final Intersection intersection, final TransportMode transportMode, final boolean isAllowingMultipleLobes) {
		Objects.requireNonNull(intersection, "intersection == null");
		Objects.requireNonNull(transportMode, "transportMode == null");
		
		final Color3F colorKD = Color3F.saturate(this.textureKD.getColor(intersection), 0.0F, Float.MAX_VALUE);
		
		final float floatAngle = this.textureAngle.getFloat(intersection);
		
		final AngleF angle = AngleF.degrees(saturate(floatAngle, 0.0F, 90.0F));
		
		if(colorKD.isBlack()) {
			return Optional.empty();
		}
		
		if(isZero(angle.getDegrees())) {
			return Optional.of(new PBRTBSDF(intersection, new LambertianPBRTBRDF(colorKD)));
		}
		
		return Optional.of(new PBRTBSDF(intersection, new OrenNayarPBRTBRDF(angle, colorKD)));
	}
	
	/**
	 * Returns a {@code String} with the name of this {@code MattePBRTMaterial} instance.
	 * 
	 * @return a {@code String} with the name of this {@code MattePBRTMaterial} instance
	 */
	@Override
	public String getName() {
		return NAME;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code MattePBRTMaterial} instance.
	 * 
	 * @return a {@code String} representation of this {@code MattePBRTMaterial} instance
	 */
	@Override
	public String toString() {
		return String.format("new MattePBRTMaterial(%s, %s, %s)", this.textureKD, this.textureEmission, this.textureAngle);
	}
	
	/**
	 * Returns the {@link Texture} instance for the angle.
	 * 
	 * @return the {@code Texture} instance for the angle
	 */
	public Texture getTextureAngle() {
		return this.textureAngle;
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
				if(!this.textureAngle.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
				
				if(!this.textureEmission.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
				
				if(!this.textureKD.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
			}
			
			return nodeHierarchicalVisitor.visitLeave(this);
		} catch(final RuntimeException e) {
			throw new NodeTraversalException(e);
		}
	}
	
	/**
	 * Compares {@code object} to this {@code MattePBRTMaterial} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code MattePBRTMaterial}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code MattePBRTMaterial} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code MattePBRTMaterial}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof MattePBRTMaterial)) {
			return false;
		} else if(!Objects.equals(this.textureAngle, MattePBRTMaterial.class.cast(object).textureAngle)) {
			return false;
		} else if(!Objects.equals(this.textureEmission, MattePBRTMaterial.class.cast(object).textureEmission)) {
			return false;
		} else if(!Objects.equals(this.textureKD, MattePBRTMaterial.class.cast(object).textureKD)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns an {@code int} with the ID of this {@code MattePBRTMaterial} instance.
	 * 
	 * @return an {@code int} with the ID of this {@code MattePBRTMaterial} instance
	 */
	@Override
	public int getID() {
		return ID;
	}
	
	/**
	 * Returns a hash code for this {@code MattePBRTMaterial} instance.
	 * 
	 * @return a hash code for this {@code MattePBRTMaterial} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.textureAngle, this.textureEmission, this.textureKD);
	}
}