/**
 * Copyright 2014 - 2023 J&#246;rgen Lundgren
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
package org.dayflower.scene.modifier;

import java.util.Arrays;
import java.util.Objects;

import org.dayflower.geometry.AngleF;
import org.dayflower.geometry.OrthonormalBasis33F;
import org.dayflower.geometry.Point2F;
import org.dayflower.geometry.SurfaceIntersection3F;
import org.dayflower.geometry.Vector2F;
import org.dayflower.geometry.Vector3F;
import org.dayflower.image.ImageF;
import org.dayflower.scene.Intersection;

import org.macroing.art4j.color.Color3F;
import org.macroing.java.util.visitor.NodeHierarchicalVisitor;
import org.macroing.java.util.visitor.NodeTraversalException;

/**
 * A {@code SteepParallaxMapLDRImageModifier} is an {@link AbstractLDRImageModifier} implementation that modifies the texture coordinates using a {@link Color3F} instance from a low-dynamic-range (LDR) image.
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class SteepParallaxMapLDRImageModifier extends AbstractLDRImageModifier {
	/**
	 * The ID of this {@code SteepParallaxMapLDRImageModifier} class.
	 */
	public static final int ID = 5;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code SteepParallaxMapLDRImageModifier} instance.
	 * <p>
	 * If {@code image} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new SteepParallaxMapLDRImageModifier(image, AngleF.degrees(0.0F));
	 * }
	 * </pre>
	 * 
	 * @param image an {@link ImageF} instance
	 * @throws NullPointerException thrown if, and only if, {@code image} is {@code null}
	 */
	public SteepParallaxMapLDRImageModifier(final ImageF image) {
		super(image);
	}
	
	/**
	 * Constructs a new {@code SteepParallaxMapLDRImageModifier} instance.
	 * <p>
	 * If either {@code image} or {@code angle} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new SteepParallaxMapLDRImageModifier(image, angle, new Vector2F(1.0F, 1.0F));
	 * }
	 * </pre>
	 * 
	 * @param image an {@link ImageF} instance
	 * @param angle the {@link AngleF} instance to use
	 * @throws NullPointerException thrown if, and only if, either {@code image} or {@code angle} are {@code null}
	 */
	public SteepParallaxMapLDRImageModifier(final ImageF image, final AngleF angle) {
		super(image, angle);
	}
	
	/**
	 * Constructs a new {@code SteepParallaxMapLDRImageModifier} instance.
	 * <p>
	 * If either {@code image}, {@code angle} or {@code scale} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new SteepParallaxMapLDRImageModifier(image.getResolutionX(), image.getResolutionY(), image.toIntArrayPackedForm(), angle, scale);
	 * }
	 * </pre>
	 * 
	 * @param image an {@link ImageF} instance
	 * @param angle the {@link AngleF} instance to use
	 * @param scale the {@link Vector2F} instance to use as the scale factor
	 * @throws NullPointerException thrown if, and only if, either {@code image}, {@code angle} or {@code scale} are {@code null}
	 */
	public SteepParallaxMapLDRImageModifier(final ImageF image, final AngleF angle, final Vector2F scale) {
		super(image, angle, scale);
	}
	
	/**
	 * Constructs a new {@code SteepParallaxMapLDRImageModifier} instance.
	 * <p>
	 * If {@code image} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}, or {@code resolutionX * resolutionY != image.length}, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new SteepParallaxMapLDRImageModifier(resolutionX, resolutionY, image, AngleF.degrees(0.0F));
	 * }
	 * </pre>
	 * 
	 * @param resolutionX the resolution of the X-axis
	 * @param resolutionY the resolution of the Y-axis
	 * @param image the image to clone and use
	 * @throws IllegalArgumentException thrown if, and only if, either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}, or {@code resolutionX * resolutionY != image.length}
	 * @throws NullPointerException thrown if, and only if, {@code image} is {@code null}
	 */
	public SteepParallaxMapLDRImageModifier(final int resolutionX, final int resolutionY, final int[] image) {
		super(resolutionX, resolutionY, image);
	}
	
	/**
	 * Constructs a new {@code SteepParallaxMapLDRImageModifier} instance.
	 * <p>
	 * If either {@code image} or {@code angle} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}, or {@code resolutionX * resolutionY != image.length}, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new SteepParallaxMapLDRImageModifier(resolutionX, resolutionY, image, angle, new Vector2F(1.0F, 1.0F));
	 * }
	 * </pre>
	 * 
	 * @param resolutionX the resolution of the X-axis
	 * @param resolutionY the resolution of the Y-axis
	 * @param image the image to clone and use
	 * @param angle the {@link AngleF} instance to use
	 * @throws IllegalArgumentException thrown if, and only if, either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}, or {@code resolutionX * resolutionY != image.length}
	 * @throws NullPointerException thrown if, and only if, either {@code image} or {@code angle} are {@code null}
	 */
	public SteepParallaxMapLDRImageModifier(final int resolutionX, final int resolutionY, final int[] image, final AngleF angle) {
		super(resolutionX, resolutionY, image, angle);
	}
	
	/**
	 * Constructs a new {@code SteepParallaxMapLDRImageModifier} instance.
	 * <p>
	 * If either {@code image}, {@code angle} or {@code scale} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}, or {@code resolutionX * resolutionY != image.length}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param resolutionX the resolution of the X-axis
	 * @param resolutionY the resolution of the Y-axis
	 * @param image the image to clone and use
	 * @param angle the {@link AngleF} instance to use
	 * @param scale the {@link Vector2F} instance to use as the scale factor
	 * @throws IllegalArgumentException thrown if, and only if, either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}, or {@code resolutionX * resolutionY != image.length}
	 * @throws NullPointerException thrown if, and only if, either {@code image}, {@code angle} or {@code scale} are {@code null}
	 */
	public SteepParallaxMapLDRImageModifier(final int resolutionX, final int resolutionY, final int[] image, final AngleF angle, final Vector2F scale) {
		super(resolutionX, resolutionY, image, angle, scale);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code String} representation of this {@code SteepParallaxMapLDRImageModifier} instance.
	 * 
	 * @return a {@code String} representation of this {@code SteepParallaxMapLDRImageModifier} instance
	 */
	@Override
	public String toString() {
		return String.format("new SteepParallaxMapLDRImageModifier(%d, %d, %s, %s, %s)", Integer.valueOf(getResolutionX()), Integer.valueOf(getResolutionY()), "new int[] {...}", getAngle(), getScale());
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
				if(!this.getScale().accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
			}
			
			return nodeHierarchicalVisitor.visitLeave(this);
		} catch(final RuntimeException e) {
			throw new NodeTraversalException(e);
		}
	}
	
	/**
	 * Compares {@code object} to this {@code SteepParallaxMapLDRImageModifier} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code SteepParallaxMapLDRImageModifier}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code SteepParallaxMapLDRImageModifier} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code SteepParallaxMapLDRImageModifier}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof SteepParallaxMapLDRImageModifier)) {
			return false;
		} else if(!Objects.equals(getAngle(), SteepParallaxMapLDRImageModifier.class.cast(object).getAngle())) {
			return false;
		} else if(!Objects.equals(getScale(), SteepParallaxMapLDRImageModifier.class.cast(object).getScale())) {
			return false;
		} else if(getResolution() != SteepParallaxMapLDRImageModifier.class.cast(object).getResolution()) {
			return false;
		} else if(getResolutionX() != SteepParallaxMapLDRImageModifier.class.cast(object).getResolutionX()) {
			return false;
		} else if(getResolutionY() != SteepParallaxMapLDRImageModifier.class.cast(object).getResolutionY()) {
			return false;
		} else if(!Arrays.equals(getImage(), SteepParallaxMapLDRImageModifier.class.cast(object).getImage())) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns an {@code int} with the ID of this {@code SteepParallaxMapLDRImageModifier} instance.
	 * 
	 * @return an {@code int} with the ID of this {@code SteepParallaxMapLDRImageModifier} instance
	 */
	@Override
	public int getID() {
		return ID;
	}
	
	/**
	 * Returns a hash code for this {@code SteepParallaxMapLDRImageModifier} instance.
	 * 
	 * @return a hash code for this {@code SteepParallaxMapLDRImageModifier} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(getAngle(), getScale(), Integer.valueOf(getResolution()), Integer.valueOf(getResolutionX()), Integer.valueOf(getResolutionY()), Integer.valueOf(Arrays.hashCode(getImage())));
	}
	
	/**
	 * Modifies the surface at {@code intersection}.
	 * <p>
	 * If {@code intersection} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param intersection an {@link Intersection} instance
	 * @throws NullPointerException thrown if, and only if, {@code intersection} is {@code null}
	 */
	@Override
	public void modify(final Intersection intersection) {
		Objects.requireNonNull(intersection, "intersection == null");
		
		final float layers = 10.0F;
		final float layersReciprocal = 1.0F / layers;
		
		final float heightScale = 0.1F;
		
		final SurfaceIntersection3F surfaceIntersection = intersection.getSurfaceIntersectionObjectSpace();
		
		final OrthonormalBasis33F orthonormalBasisS = surfaceIntersection.getOrthonormalBasisS();
		
		final Point2F textureCoordinates = surfaceIntersection.getTextureCoordinates();
		
		final Vector3F direction = surfaceIntersection.getRay().getDirection();
		final Vector3F directionNegated = Vector3F.negate(direction);
		final Vector3F directionTransformed = Vector3F.transformReverse(directionNegated, orthonormalBasisS);
		final Vector3F directionNormalized = Vector3F.normalize(directionTransformed);
		final Vector3F directionScaled = Vector3F.multiply(directionNormalized, heightScale);
		
		final float deltaU = directionScaled.x * layersReciprocal;
		final float deltaV = directionScaled.y * layersReciprocal;
		
		float u = textureCoordinates.x;
		float v = textureCoordinates.y;
		
		float currentHeight = getColorRGB(u, v).r;
		float currentLayerDepth = 0.0F;
		
		while(currentLayerDepth < currentHeight) {
			u -= deltaU;
			v -= deltaV;
			
			currentHeight = getColorRGB(u, v).r;
			currentLayerDepth += layersReciprocal;
		}
		
		intersection.setTextureCoordinates(new Point2F(u, v));
	}
}