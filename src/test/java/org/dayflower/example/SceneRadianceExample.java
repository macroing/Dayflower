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
package org.dayflower.example;

import org.dayflower.color.Color3F;
import org.dayflower.geometry.AngleF;
import org.dayflower.geometry.Point3F;
import org.dayflower.geometry.shape.Plane3F;
import org.dayflower.image.PixelImageF;
import org.dayflower.scene.Camera;
import org.dayflower.scene.Primitive;
import org.dayflower.scene.Scene;
import org.dayflower.scene.light.PerezLight;
import org.dayflower.scene.material.MatteMaterial;

public class SceneRadianceExample {
	public static void main(String[] args) {
		Camera camera = new Camera(new Point3F(0.0F, 1.0F, 0.0F), AngleF.degrees(40.0F));
		camera.setResolution(800.0F, 800.0F);
		camera.setFieldOfViewY();
		camera.setOrthonormalBasis();
		
		Scene scene = new Scene(camera);
		scene.addLight(new PerezLight());
		scene.addPrimitive(new Primitive(new MatteMaterial(), new Plane3F()));
		
		PixelImageF pixelImageF = new PixelImageF(800, 800);
		pixelImageF.getPixels().forEach(pixel -> camera.createPrimaryRay(pixel.getX(), pixel.getY(), 0.5F, 0.5F).ifPresent(ray -> {
			pixelImageF.filmAddColorXYZ(pixel.getX() + 0.5F, pixel.getY() + 0.5F, Color3F.convertRGBToXYZUsingPBRT(scene.radiancePathTracer(ray)));
		}));
		pixelImageF.filmRender();
		pixelImageF.save("SceneRadianceExample.png", "png");
	}
}