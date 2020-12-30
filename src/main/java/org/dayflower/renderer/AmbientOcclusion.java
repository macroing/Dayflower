/**
 * Copyright 2020 - 2021 J&#246;rgen Lundgren
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
package org.dayflower.renderer;

/**
 * An {@code AmbientOcclusion} is an {@link ImageOrderRenderer} for the Ambient Occlusion algorithm.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public interface AmbientOcclusion extends ImageOrderRenderer {
	/**
	 * Returns the maximum distance.
	 * 
	 * @return the maximum distance
	 */
	float getMaximumDistance();
	
	/**
	 * Returns the samples to use per render pass.
	 * 
	 * @return the samples to use per render pass
	 */
	int getSamples();
	
	/**
	 * Sets the maximum distance to {@code maximumDistance}.
	 * 
	 * @param maximumDistance the maximum distance
	 */
	void setMaximumDistance(final float maximumDistance);
	
	/**
	 * Sets the samples to use per render pass to {@code samples}.
	 * 
	 * @param samples the samples to use per render pass
	 */
	void setSamples(final int samples);
}