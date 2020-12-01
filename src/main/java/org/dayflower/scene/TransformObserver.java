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

import java.lang.reflect.Field;

import org.dayflower.geometry.Matrix44F;
import org.dayflower.geometry.Point3F;
import org.dayflower.geometry.Quaternion4F;
import org.dayflower.geometry.Vector3F;

//TODO: Add Javadocs!
public interface TransformObserver {
//	TODO: Add Javadocs!
	void onChangeObjectToWorld(final Transform transform, final Matrix44F newObjectToWorld);
	
//	TODO: Add Javadocs!
	void onChangePosition(final Transform transform, final Point3F oldPosition, final Point3F newPosition);
	
//	TODO: Add Javadocs!
	void onChangeRotation(final Transform transform, final Quaternion4F oldRotation, final Quaternion4F newRotation);
	
//	TODO: Add Javadocs!
	void onChangeScale(final Transform transform, final Vector3F oldScale, final Vector3F newScale);
	
//	TODO: Add Javadocs!
	void onChangeWorldToObject(final Transform transform, final Matrix44F newWorldToObject);
}