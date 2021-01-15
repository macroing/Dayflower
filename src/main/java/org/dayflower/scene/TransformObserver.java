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
package org.dayflower.scene;

import org.dayflower.geometry.Matrix44F;
import org.dayflower.geometry.Point3F;
import org.dayflower.geometry.Quaternion4F;
import org.dayflower.geometry.Vector3F;

/**
 * A {@code TransformObserver} is used to observe changes to a {@link Transform} instance.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public interface TransformObserver {
	/**
	 * This method is called by {@code transform} when the object space to world space transformation changes.
	 * <p>
	 * If either {@code transform} or {@code newObjectToWorld} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param transform the {@link Transform} instance that called this method
	 * @param newObjectToWorld a {@link Matrix44F} instance that represents the new object space to world space transformation
	 * @throws NullPointerException thrown if, and only if, either {@code transform} or {@code newObjectToWorld} are {@code null}
	 */
	void onChangeObjectToWorld(final Transform transform, final Matrix44F newObjectToWorld);
	
	/**
	 * This method is called by {@code transform} when the position changes.
	 * <p>
	 * If either {@code transform}, {@code oldPosition} or {@code newPosition} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param transform the {@link Transform} instance that called this method
	 * @param oldPosition a {@link Point3F} instance that represents the old position
	 * @param newPosition a {@code Point3F} instance that represents the new position
	 * @throws NullPointerException thrown if, and only if, either {@code transform}, {@code oldPosition} or {@code newPosition} are {@code null}
	 */
	void onChangePosition(final Transform transform, final Point3F oldPosition, final Point3F newPosition);
	
	/**
	 * This method is called by {@code transform} when the rotation changes.
	 * <p>
	 * If either {@code transform}, {@code oldRotation} or {@code newRotation} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param transform the {@link Transform} instance that called this method
	 * @param oldRotation a {@link Quaternion4F} instance that represents the old rotation
	 * @param newRotation a {@code Quaternion4F} instance that represents the new rotation
	 * @throws NullPointerException thrown if, and only if, either {@code transform}, {@code oldRotation} or {@code newRotation} are {@code null}
	 */
	void onChangeRotation(final Transform transform, final Quaternion4F oldRotation, final Quaternion4F newRotation);
	
	/**
	 * This method is called by {@code transform} when the scale changes.
	 * <p>
	 * If either {@code transform}, {@code oldScale} or {@code newScale} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param transform the {@link Transform} instance that called this method
	 * @param oldScale a {@link Vector3F} instance that represents the old scale
	 * @param newScale a {@code Vector3F} instance that represents the new scale
	 * @throws NullPointerException thrown if, and only if, either {@code transform}, {@code oldScale} or {@code newScale} are {@code null}
	 */
	void onChangeScale(final Transform transform, final Vector3F oldScale, final Vector3F newScale);
	
	/**
	 * This method is called by {@code transform} when the world space to object space transformation changes.
	 * <p>
	 * If either {@code transform} or {@code newWorldToObject} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param transform the {@link Transform} instance that called this method
	 * @param newWorldToObject a {@link Matrix44F} instance that represents the new world space to object space transformation
	 * @throws NullPointerException thrown if, and only if, either {@code transform} or {@code newWorldToObject} are {@code null}
	 */
	void onChangeWorldToObject(final Transform transform, final Matrix44F newWorldToObject);
}