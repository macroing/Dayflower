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
		
		if(cameraGenerateRay()) {
			if(intersectsPlane3F()/*intersectsRectangularCuboid3F()*//*intersectsSphere3F()*//*intersectsTorus3F()*//*intersectsTriangle3F()*/) {
				this.radianceArray[index + 0] = 1.0F;
				this.radianceArray[index + 1] = 0.0F;
				this.radianceArray[index + 2] = 0.0F;
			} else {
				this.radianceArray[index + 0] = 0.0F;
				this.radianceArray[index + 1] = 0.0F;
				this.radianceArray[index + 2] = 0.0F;
			}
		} else {
			this.radianceArray[index + 0] = 1.0F;
			this.radianceArray[index + 1] = 1.0F;
			this.radianceArray[index + 2] = 1.0F;
		}
	}
}