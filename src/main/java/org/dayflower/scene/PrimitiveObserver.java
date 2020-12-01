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
package org.dayflower.scene;

import java.util.Optional;

import org.dayflower.geometry.BoundingVolume3F;
import org.dayflower.geometry.Matrix44F;
import org.dayflower.geometry.Shape3F;

/**
 * A {@code PrimitiveObserver} is used to observe changes to a {@link Primitive} instance.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public interface PrimitiveObserver {
	/**
	 * This method is called by {@code primitive} when the {@link AreaLight} changes.
	 * <p>
	 * If either {@code primitive}, {@code oldOptionalAreaLight} or {@code newOptionalAreaLight} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param primitive the {@link Primitive} instance that called this method
	 * @param oldOptionalAreaLight an {@code Optional} with the old {@code AreaLight} instance
	 * @param newOptionalAreaLight an {@code Optional} with the new {@code AreaLight} instance
	 * @throws NullPointerException thrown if, and only if, either {@code primitive}, {@code oldOptionalAreaLight} or {@code newOptionalAreaLight} are {@code null}
	 */
	void onChangeAreaLight(final Primitive primitive, final Optional<AreaLight> oldOptionalAreaLight, final Optional<AreaLight> newOptionalAreaLight);
	
	/**
	 * This method is called by {@code primitive} when the {@link BoundingVolume3F} changes.
	 * <p>
	 * If either {@code primitive}, {@code oldBoundingVolume} or {@code newBoundingVolume} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param primitive the {@link Primitive} instance that called this method
	 * @param oldBoundingVolume the old {@code BoundingVolume3F} instance
	 * @param newBoundingVolume the new {@code BoundingVolume3F} instance
	 * @throws NullPointerException thrown if, and only if, either {@code primitive}, {@code oldBoundingVolume} or {@code newBoundingVolume} are {@code null}
	 */
	void onChangeBoundingVolume(final Primitive primitive, final BoundingVolume3F oldBoundingVolume, final BoundingVolume3F newBoundingVolume);
	
	/**
	 * This method is called by {@code primitive} when the {@link Material} changes.
	 * <p>
	 * If either {@code primitive}, {@code oldMaterial} or {@code newMaterial} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param primitive the {@link Primitive} instance that called this method
	 * @param oldMaterial the old {@code Material} instance
	 * @param newMaterial the new {@code Material} instance
	 * @throws NullPointerException thrown if, and only if, either {@code primitive}, {@code oldMaterial} or {@code newMaterial} are {@code null}
	 */
	void onChangeMaterial(final Primitive primitive, final Material oldMaterial, final Material newMaterial);
	
	/**
	 * This method is called by {@code primitive} when the {@link Matrix44F} instance that transforms from object space to world space changes.
	 * <p>
	 * If either {@code primitive}, {@code oldObjectToWorld} or {@code newObjectToWorld} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param primitive the {@link Primitive} instance that called this method
	 * @param oldObjectToWorld the old {@code Matrix44F} instance that transforms from object space to world space
	 * @param newObjectToWorld the new {@code Matrix44F} instance that transforms from object space to world space
	 * @throws NullPointerException thrown if, and only if, either {@code primitive}, {@code oldObjectToWorld} or {@code newObjectToWorld} are {@code null}
	 */
	void onChangeObjectToWorld(final Primitive primitive, final Matrix44F oldObjectToWorld, final Matrix44F newObjectToWorld);
	
	/**
	 * This method is called by {@code primitive} when the {@link Shape3F} changes.
	 * <p>
	 * If either {@code primitive}, {@code oldShape} or {@code newShape} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param primitive the {@link Primitive} instance that called this method
	 * @param oldShape the old {@code Shape3F} instance
	 * @param newShape the new {@code Shape3F} instance
	 * @throws NullPointerException thrown if, and only if, either {@code primitive}, {@code oldShape} or {@code newShape} are {@code null}
	 */
	void onChangeShape(final Primitive primitive, final Shape3F oldShape, final Shape3F newShape);
	
	/**
	 * This method is called by {@code primitive} when the {@link Texture} instance for the albedo color changes.
	 * <p>
	 * If either {@code primitive}, {@code oldTextureAlbedo} or {@code newTextureAlbedo} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param primitive the {@link Primitive} instance that called this method
	 * @param oldTextureAlbedo the old {@code Texture} instance for the albedo color
	 * @param newTextureAlbedo the new {@code Texture} instance for the albedo color
	 * @throws NullPointerException thrown if, and only if, either {@code primitive}, {@code oldTextureAlbedo} or {@code newTextureAlbedo} are {@code null}
	 */
	void onChangeTextureAlbedo(final Primitive primitive, final Texture oldTextureAlbedo, final Texture newTextureAlbedo);
	
	/**
	 * This method is called by {@code primitive} when the {@link Texture} instance for the emittance changes.
	 * <p>
	 * If either {@code primitive}, {@code oldTextureEmittance} or {@code newTextureEmittance} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param primitive the {@link Primitive} instance that called this method
	 * @param oldTextureEmittance the old {@code Texture} instance for the emittance
	 * @param newTextureEmittance the new {@code Texture} instance for the emittance
	 * @throws NullPointerException thrown if, and only if, either {@code primitive}, {@code oldTextureEmittance} or {@code newTextureEmittance} are {@code null}
	 */
	void onChangeTextureEmittance(final Primitive primitive, final Texture oldTextureEmittance, final Texture newTextureEmittance);
	
	/**
	 * This method is called by {@code primitive} when the {@link Transform} instance changes.
	 * <p>
	 * If either {@code primitive}, {@code oldTransform} or {@code newTransform} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param primitive the {@link Primitive} instance that called this method
	 * @param oldTransform the old {@code Transform} instance
	 * @param newTransform the new {@code Transform} instance
	 * @throws NullPointerException thrown if, and only if, either {@code primitive}, {@code oldTransform} or {@code newTransform} are {@code null}
	 */
	void onChangeTransform(final Primitive primitive, final Transform oldTransform, final Transform newTransform);
	
	/**
	 * This method is called by {@code primitive} when the {@link Matrix44F} instance that transforms from world space to object space changes.
	 * <p>
	 * If either {@code primitive}, {@code oldWorldToObject} or {@code newWorldToObject} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param primitive the {@link Primitive} instance that called this method
	 * @param oldWorldToObject the old {@code Matrix44F} instance that transforms from world space to object space
	 * @param newWorldToObject the new {@code Matrix44F} instance that transforms from world space to object space
	 * @throws NullPointerException thrown if, and only if, either {@code primitive}, {@code oldWorldToObject} or {@code newWorldToObject} are {@code null}
	 */
	void onChangeWorldToObject(final Primitive primitive, final Matrix44F oldWorldToObject, final Matrix44F newWorldToObject);
}