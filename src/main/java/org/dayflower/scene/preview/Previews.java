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
package org.dayflower.scene.preview;

import java.util.Objects;

import org.dayflower.color.Color3F;
import org.dayflower.geometry.Shape3F;
import org.dayflower.geometry.shape.Sphere3F;
import org.dayflower.scene.AreaLight;
import org.dayflower.scene.Camera;
import org.dayflower.scene.Material;
import org.dayflower.scene.Primitive;
import org.dayflower.scene.Scene;
import org.dayflower.scene.Transform;
import org.dayflower.scene.light.DiffuseAreaLight;
import org.dayflower.scene.material.MatteMaterial;

/**
 * A class that consists exclusively of static methods that returns {@link Scene} instances used for previews.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class Previews {
	private Previews() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link Scene} instance for previewing {@code material}.
	 * <p>
	 * If {@code material} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param material a {@link Material} instance
	 * @return a {@code Scene} instance for previewing {@code material}
	 * @throws NullPointerException thrown if, and only if, {@code material} is {@code null}
	 */
	public static Scene createMaterialPreviewScene(final Material material) {
		Objects.requireNonNull(material, "material == null");
		
		final
		Camera camera = new Camera();
		camera.setResolution(32.0F, 32.0F);
		camera.setFieldOfViewY();
		camera.setOrthonormalBasis();
		
		final Material material0 = material;
		final Material material1 = new MatteMaterial();
		
		final Shape3F shape0 = new Sphere3F();
		final Shape3F shape1 = new Sphere3F(2.0F);
		
		final Transform transform0 = new Transform(camera.getPointInfrontOfEye(4.0F));
		final Transform transform1 = new Transform(camera.getPointBehindEye(4.0F));
		
		final AreaLight areaLight1 = new DiffuseAreaLight(transform1, 1, new Color3F(12.0F), shape1, true);
		
		final Primitive primitive0 = new Primitive(material0, shape0, transform0);
		final Primitive primitive1 = new Primitive(material1, shape1, transform1, areaLight1);
		
		final
		Scene scene = new Scene();
		scene.addLight(areaLight1);
		scene.addPrimitive(primitive0);
		scene.addPrimitive(primitive1);
		scene.setCamera(camera);
		scene.setName("Preview");
		
		return scene;
	}
}