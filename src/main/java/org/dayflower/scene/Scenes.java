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
import org.dayflower.geometry.Plane3F;
import org.dayflower.geometry.Point3F;
import org.dayflower.geometry.Sphere3F;
import org.dayflower.image.Color3F;

//TODO: Add Javadocs!
public final class Scenes {
	private Scenes() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public static Scene newDefaultScene() {
		final
		Scene scene = new Scene();
		scene.getCamera().setEye(new Point3F(0.0F, 2.0F, 0.0F));
		scene.addPrimitive(new Primitive(new LambertianMaterial(), new Sphere3F(10.0F), new ConstantTexture(Color3F.RED), new ConstantTexture(), new ConstantTexture(), Matrix44F.translate(0.0F, 2.0F, 20.0F)));
		scene.addPrimitive(new Primitive(new LambertianMaterial(), new Plane3F(), new ConstantTexture(Color3F.GREEN), new ConstantTexture(), new ConstantTexture(), Matrix44F.identity()));
		
		return scene;
	}
}