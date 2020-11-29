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

//TODO: Add Javadocs!
public interface SceneObserver {
//	TODO: Add Javadocs!
	void onAddLight(final Scene scene, final Light newLight);
	
//	TODO: Add Javadocs!
	void onAddPrimitive(final Scene scene, final Primitive newPrimitive);
	
//	TODO: Add Javadocs!
	void onChangeCamera(final Scene scene, final Camera oldCamera);
	
//	TODO: Add Javadocs!
	void onChangeCamera(final Scene scene, final Camera oldCamera, final Camera newCamera);
	
//	TODO: Add Javadocs!
	void onChangeName(final Scene scene, final String oldName, final String newName);
	
//	TODO: Add Javadocs!
	void onChangePrimitive(final Scene scene, final Primitive oldPrimitive);
	
//	TODO: Add Javadocs!
	void onRemoveLight(final Scene scene, final Light oldLight);
	
//	TODO: Add Javadocs!
	void onRemovePrimitive(final Scene scene, final Primitive oldPrimitive);
}