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

import org.dayflower.geometry.AngleF;
import org.dayflower.geometry.Matrix44F;
import org.dayflower.geometry.Plane3F;
import org.dayflower.geometry.Point3F;
import org.dayflower.geometry.RectangularCuboid3F;
import org.dayflower.geometry.Sphere3F;
import org.dayflower.geometry.Vector2F;
import org.dayflower.image.Color3F;

/**
 * A class that consists exclusively of static methods that returns {@link Scene} instances.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class Scenes {
	private Scenes() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link Scene} instance.
	 * 
	 * @return a {@code Scene} instance
	 */
	public static Scene newDefaultScene() {
		final
		Scene scene = new Scene();
		scene.getCamera().setEye(new Point3F(0.0F, 2.0F, 0.0F));
		scene.addPrimitive(new Primitive(new AshikhminShirleyMaterial(), new Sphere3F(10.0F), new BullseyeTexture(new ConstantTexture(new Color3F(1.0F, 0.1F, 0.1F)), new ConstantTexture(new Color3F(0.5F, 0.1F, 0.1F)), new Point3F(0.0F, 10.0F, 0.0F), 2.0F), new ConstantTexture(), new ConstantTexture(), Matrix44F.translate(0.0F, 2.0F, 20.0F)));
		scene.addPrimitive(new Primitive(new LambertianMaterial(), new Plane3F(), new CheckerboardTexture(new ConstantTexture(new Color3F(0.1F)), new ConstantTexture(new Color3F(1.0F)), AngleF.degrees(90.0F), new Vector2F(0.5F, 0.5F)), new ConstantTexture(), new ConstantTexture(), Matrix44F.identity()));
		scene.addPrimitive(new Primitive(new AshikhminShirleyMaterial(new AshikhminShirleyBRDF(0.02F)), new RectangularCuboid3F(new Point3F(-1.0F), new Point3F(1.0F)), new CheckerboardTexture(new ConstantTexture(new Color3F(0.2F, 0.4F, 0.2F)), new ConstantTexture(new Color3F(0.6F, 0.8F, 0.6F)), AngleF.degrees(90.0F), new Vector2F(1.5F, 1.5F)), new ConstantTexture(), new ConstantTexture(), Matrix44F.translate(-2.0F, 1.0F, 5.0F)));
		
		return scene;
	}
}