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
package org.dayflower.renderer.gpu;

import java.lang.reflect.Field;

import org.dayflower.renderer.RendererConfiguration;
import org.dayflower.renderer.RendererObserver;
import org.dayflower.renderer.observer.FileRendererObserver;

//TODO: Add Javadocs!
public final class GPURenderer extends AbstractGPURenderer {
//	TODO: Add Javadocs!
	public GPURenderer() {
		this(new RendererConfiguration());
	}
	
//	TODO: Add Javadocs!
	public GPURenderer(final RendererConfiguration rendererConfiguration) {
		this(rendererConfiguration, new FileRendererObserver());
	}
	
//	TODO: Add Javadocs!
	public GPURenderer(final RendererConfiguration rendererConfiguration, final RendererObserver rendererObserver) {
		super(rendererConfiguration, rendererObserver);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	@Override
	public void run() {
		final int index = getGlobalId() * 3;
		
		if(ray3FCameraGenerate()) {
			doTest(index);
//			doTestShape3F(index);
//			doTestShape3FPlane3F(index);
//			doTestShape3FRectangularCuboid3F(index);
//			doTestShape3FSphere3F(index);
//			doTestShape3FTorus3F(index);
//			doTestShape3FTriangle3F(index);
		} else {
			this.radianceRGBArray[index + 0] = 1.0F;
			this.radianceRGBArray[index + 1] = 1.0F;
			this.radianceRGBArray[index + 2] = 1.0F;
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private void doTest(final int index) {
		if(intersectionComputeShape3F()) {
			final float surfaceNormalX = this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 0];
			final float surfaceNormalY = this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 1];
			final float surfaceNormalZ = this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 2];
			
			final float r = (surfaceNormalX + 1.0F) * 0.5F;
			final float g = (surfaceNormalY + 1.0F) * 0.5F;
			final float b = (surfaceNormalZ + 1.0F) * 0.5F;
			
			this.radianceRGBArray[index + 0] = r;
			this.radianceRGBArray[index + 1] = g;
			this.radianceRGBArray[index + 2] = b;
		} else {
			this.radianceRGBArray[index + 0] = 0.0F;
			this.radianceRGBArray[index + 1] = 0.0F;
			this.radianceRGBArray[index + 2] = 0.0F;
		}
	}
	
	private void doTestShape3F(final int index) {
		if(intersectsShape3F()) {
			this.radianceRGBArray[index + 0] = 1.0F;
			this.radianceRGBArray[index + 1] = 0.0F;
			this.radianceRGBArray[index + 2] = 0.0F;
		} else {
			this.radianceRGBArray[index + 0] = 0.0F;
			this.radianceRGBArray[index + 1] = 0.0F;
			this.radianceRGBArray[index + 2] = 0.0F;
		}
	}
	
	private void doTestShape3FPlane3F(final int index) {
		if(intersectsShape3FPlane3F(0)) {
			this.radianceRGBArray[index + 0] = 1.0F;
			this.radianceRGBArray[index + 1] = 0.0F;
			this.radianceRGBArray[index + 2] = 0.0F;
		} else {
			this.radianceRGBArray[index + 0] = 0.0F;
			this.radianceRGBArray[index + 1] = 0.0F;
			this.radianceRGBArray[index + 2] = 0.0F;
		}
	}
	
	private void doTestShape3FRectangularCuboid3F(final int index) {
		if(intersectsShape3FRectangularCuboid3F(0)) {
			this.radianceRGBArray[index + 0] = 1.0F;
			this.radianceRGBArray[index + 1] = 0.0F;
			this.radianceRGBArray[index + 2] = 0.0F;
		} else {
			this.radianceRGBArray[index + 0] = 0.0F;
			this.radianceRGBArray[index + 1] = 0.0F;
			this.radianceRGBArray[index + 2] = 0.0F;
		}
	}
	
	private void doTestShape3FSphere3F(final int index) {
		if(intersectsShape3FSphere3F(0)) {
			this.radianceRGBArray[index + 0] = 1.0F;
			this.radianceRGBArray[index + 1] = 0.0F;
			this.radianceRGBArray[index + 2] = 0.0F;
		} else {
			this.radianceRGBArray[index + 0] = 0.0F;
			this.radianceRGBArray[index + 1] = 0.0F;
			this.radianceRGBArray[index + 2] = 0.0F;
		}
	}
	
	private void doTestShape3FTorus3F(final int index) {
		if(intersectsShape3FTorus3F(0)) {
			this.radianceRGBArray[index + 0] = 1.0F;
			this.radianceRGBArray[index + 1] = 0.0F;
			this.radianceRGBArray[index + 2] = 0.0F;
		} else {
			this.radianceRGBArray[index + 0] = 0.0F;
			this.radianceRGBArray[index + 1] = 0.0F;
			this.radianceRGBArray[index + 2] = 0.0F;
		}
	}
	
	private void doTestShape3FTriangle3F(final int index) {
		if(intersectsShape3FTriangle3F()) {
			this.radianceRGBArray[index + 0] = 1.0F;
			this.radianceRGBArray[index + 1] = 0.0F;
			this.radianceRGBArray[index + 2] = 0.0F;
		} else {
			this.radianceRGBArray[index + 0] = 0.0F;
			this.radianceRGBArray[index + 1] = 0.0F;
			this.radianceRGBArray[index + 2] = 0.0F;
		}
	}
}