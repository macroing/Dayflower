/**
 * Copyright 2014 - 2022 J&#246;rgen Lundgren
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
import org.dayflower.geometry.Vector2F;
import org.dayflower.geometry.Vector3F;
import org.dayflower.image.ImageF;
import org.dayflower.node.NodeHierarchicalVisitor;
import org.dayflower.node.NodeTraversalException;
import org.dayflower.scene.Intersection;

import org.macroing.art4j.color.Color3F;

/**
 * A {@code NormalMapLDRImageModifier} is an {@link AbstractLDRImageModifier} implementation that modifies the surface normal using a {@link Color3F} instance from a low-dynamic-range (LDR) image.
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class NormalMapLDRImageModifier extends AbstractLDRImageModifier {
	/**
	 * The ID of this {@code NormalMapLDRImageModifier} class.
	 */
	public static final int ID = 3;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code NormalMapLDRImageModifier} instance.
	 * <p>
	 * If {@code image} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new NormalMapLDRImageModifier(image, AngleF.degrees(0.0F));
	 * }
	 * </pre>
	 * 
	 * @param image an {@link ImageF} instance
	 * @throws NullPointerException thrown if, and only if, {@code image} is {@code null}
	 */
	public NormalMapLDRImageModifier(final ImageF image) {
		super(image);
	}
	
	/**
	 * Constructs a new {@code NormalMapLDRImageModifier} instance.
	 * <p>
	 * If either {@code image} or {@code angle} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new NormalMapLDRImageModifier(image, angle, new Vector2F(1.0F, 1.0F));
	 * }
	 * </pre>
	 * 
	 * @param image an {@link ImageF} instance
	 * @param angle the {@link AngleF} instance to use
	 * @throws NullPointerException thrown if, and only if, either {@code image} or {@code angle} are {@code null}
	 */
	public NormalMapLDRImageModifier(final ImageF image, final AngleF angle) {
		super(image, angle);
	}
	
	/**
	 * Constructs a new {@code NormalMapLDRImageModifier} instance.
	 * <p>
	 * If either {@code image}, {@code angle} or {@code scale} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new NormalMapLDRImageModifier(image.getResolutionX(), image.getResolutionY(), image.toIntArrayPackedForm(), angle, scale);
	 * }
	 * </pre>
	 * 
	 * @param image an {@link ImageF} instance
	 * @param angle the {@link AngleF} instance to use
	 * @param scale the {@link Vector2F} instance to use as the scale factor
	 * @throws NullPointerException thrown if, and only if, either {@code image}, {@code angle} or {@code scale} are {@code null}
	 */
	public NormalMapLDRImageModifier(final ImageF image, final AngleF angle, final Vector2F scale) {
		super(image, angle, scale);
	}
	
	/**
	 * Constructs a new {@code NormalMapLDRImageModifier} instance.
	 * <p>
	 * If {@code image} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}, or {@code resolutionX * resolutionY != image.length}, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new NormalMapLDRImageModifier(resolutionX, resolutionY, image, AngleF.degrees(0.0F));
	 * }
	 * </pre>
	 * 
	 * @param resolutionX the resolution of the X-axis
	 * @param resolutionY the resolution of the Y-axis
	 * @param image the image to clone and use
	 * @throws IllegalArgumentException thrown if, and only if, either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}, or {@code resolutionX * resolutionY != image.length}
	 * @throws NullPointerException thrown if, and only if, {@code image} is {@code null}
	 */
	public NormalMapLDRImageModifier(final int resolutionX, final int resolutionY, final int[] image) {
		super(resolutionX, resolutionY, image);
	}
	
	/**
	 * Constructs a new {@code NormalMapLDRImageModifier} instance.
	 * <p>
	 * If either {@code image} or {@code angle} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}, or {@code resolutionX * resolutionY != image.length}, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new NormalMapLDRImageModifier(resolutionX, resolutionY, image, angle, new Vector2F(1.0F, 1.0F));
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
	public NormalMapLDRImageModifier(final int resolutionX, final int resolutionY, final int[] image, final AngleF angle) {
		super(resolutionX, resolutionY, image, angle);
	}
	
	/**
	 * Constructs a new {@code NormalMapLDRImageModifier} instance.
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
	public NormalMapLDRImageModifier(final int resolutionX, final int resolutionY, final int[] image, final AngleF angle, final Vector2F scale) {
		super(resolutionX, resolutionY, image, angle, scale);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code String} representation of this {@code NormalMapLDRImageModifier} instance.
	 * 
	 * @return a {@code String} representation of this {@code NormalMapLDRImageModifier} instance
	 */
	@Override
	public String toString() {
		return String.format("new NormalMapLDRImageModifier(%d, %d, %s, %s, %s)", Integer.valueOf(getResolutionX()), Integer.valueOf(getResolutionY()), "new int[] {...}", getAngle(), getScale());
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
				if(!getScale().accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
			}
			
			return nodeHierarchicalVisitor.visitLeave(this);
		} catch(final RuntimeException e) {
			throw new NodeTraversalException(e);
		}
	}
	
	/**
	 * Compares {@code object} to this {@code NormalMapLDRImageModifier} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code NormalMapLDRImageModifier}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code NormalMapLDRImageModifier} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code NormalMapLDRImageModifier}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof NormalMapLDRImageModifier)) {
			return false;
		} else if(!Objects.equals(getAngle(), NormalMapLDRImageModifier.class.cast(object).getAngle())) {
			return false;
		} else if(!Objects.equals(getScale(), NormalMapLDRImageModifier.class.cast(object).getScale())) {
			return false;
		} else if(getResolution() != NormalMapLDRImageModifier.class.cast(object).getResolution()) {
			return false;
		} else if(getResolutionX() != NormalMapLDRImageModifier.class.cast(object).getResolutionX()) {
			return false;
		} else if(getResolutionY() != NormalMapLDRImageModifier.class.cast(object).getResolutionY()) {
			return false;
		} else if(!Arrays.equals(getImage(), NormalMapLDRImageModifier.class.cast(object).getImage())) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns an {@code int} with the ID of this {@code NormalMapLDRImageModifier} instance.
	 * 
	 * @return an {@code int} with the ID of this {@code NormalMapLDRImageModifier} instance
	 */
	@Override
	public int getID() {
		return ID;
	}
	
	/**
	 * Returns a hash code for this {@code NormalMapLDRImageModifier} instance.
	 * 
	 * @return a hash code for this {@code NormalMapLDRImageModifier} instance
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
		
		final Color3F colorRGB = getColorRGB(intersection.getTextureCoordinates());
		
		final float r = colorRGB.r;
		final float g = colorRGB.g;
		final float b = colorRGB.b;
		
		final float x = r * 2.0F - 1.0F;
		final float y = g * 2.0F - 1.0F;
		final float z = b * 2.0F - 1.0F;
		
		final Vector3F surfaceNormalSLocalSpace = new Vector3F(x, y, z);
		final Vector3F surfaceNormalSWorldSpace = Vector3F.normalize(Vector3F.transform(surfaceNormalSLocalSpace, intersection.getSurfaceIntersectionWorldSpace().getOrthonormalBasisG()));
		
		intersection.setSurfaceNormalS(surfaceNormalSWorldSpace);
	}
}