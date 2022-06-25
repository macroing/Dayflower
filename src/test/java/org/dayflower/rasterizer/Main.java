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
package org.dayflower.rasterizer;

import java.io.IOException;

import org.dayflower.geometry.Matrix44F;
import org.dayflower.geometry.Quaternion4F;
import org.dayflower.geometry.Vector3F;
import org.dayflower.geometry.shape.TriangleMesh3F;
import org.dayflower.utility.Floats;

public final class Main {
	private static final byte BLACK = 0b0;
	private static final float FIELD_OF_VIEW = 70.0F;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private Main() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static void main(final String[] args) throws IOException {
		final Display display = new Display("Dayflower");
		
		final GraphicsContext graphicsContext = display.getGraphicsContext();
		
		final float width = graphicsContext.getWidth();
		final float height = graphicsContext.getHeight();
		
		final Bitmap bitmapMonkey = new Bitmap("./resources/textures/Texture_8.jpg");
		final Bitmap bitmapTerrain = new Bitmap("./resources/textures/Texture_2.png");
		
		final TriangleMesh3F meshMonkey = TriangleMesh3F.readWavefrontObject("./resources/models/monkey0.obj").get(0);
		final TriangleMesh3F meshTerrain = TriangleMesh3F.readWavefrontObject("./resources/models/terrain2.obj").get(0);
		
		Transform transformMonkey = new Transform(new Vector3F(0.0F, 1.0F, 3.0F));
		Transform transformTerrain = new Transform(new Vector3F(0.0F, -1.0F, 0.0F));
		
		final Camera camera = new Camera(doPerspective(FIELD_OF_VIEW, width / height, 0.1F, 1000.0F));
		
		long previousTime = System.nanoTime();
		
		final FPSCounter fPSCounter = new FPSCounter();
		
		while(true) {
			try {
				final long currentTime = System.nanoTime();
				
				final float delta = (float)((currentTime - previousTime) / 1000000000.0D);
				
				previousTime = currentTime;
				
				camera.update(display.getInput(), delta);
				
				final Matrix44F viewProjection = camera.getViewProjection();
				
				final float sinHalfAngle = Floats.sin(delta / 2.0F);
				final float cosHalfAngle = Floats.cos(delta / 2.0F);
				
				final float x = 0.0F * sinHalfAngle;
				final float y = 1.0F * sinHalfAngle;
				final float z = 0.0F * sinHalfAngle;
				
				transformMonkey = transformMonkey.rotate(new Quaternion4F(x, y, z, cosHalfAngle));
				
				graphicsContext.clear(BLACK);
				graphicsContext.clearZBuffer();
				graphicsContext.drawTriangleMesh(meshMonkey, viewProjection, transformMonkey.getTransformation(), bitmapMonkey);
				graphicsContext.drawTriangleMesh(meshTerrain, viewProjection, transformTerrain.getTransformation(), bitmapTerrain);
				
				display.swapBuffers();
				
				fPSCounter.update();
				
				display.setTitle("Dayflower - FPS: " + fPSCounter.getFPS());
			} catch(final Exception e) {
				e.printStackTrace();
				
				System.exit(0);
			}
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static Matrix44F doPerspective(final float fieldOfView, final float aspectRatio, final float zNear, final float zFar) {
		final float tanHalfFieldOfView = Floats.tan(Floats.toRadians(fieldOfView / 2.0F));
		
		return new Matrix44F(1.0F / (tanHalfFieldOfView * aspectRatio), 0.0F, 0.0F, 0.0F, 0.0F, 1.0F / tanHalfFieldOfView, 0.0F, 0.0F, 0.0F, 0.0F, zFar / (zFar - zNear), -zFar * zNear / (zFar - zNear), 0.0F, 0.0F, 1.0F, 0.0F);
	}
}