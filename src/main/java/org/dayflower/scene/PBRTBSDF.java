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

import org.dayflower.geometry.Point2F;
import org.dayflower.geometry.Vector3F;

public final class PBRTBSDF {
	
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public PBRTBSDF() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	BxDF: f(Vector3f &wo, Vector3f &wi)
	public PBRTBSDFResult evaluateDistributionFunction(final Vector3F outgoing, final Vector3F incoming) {
		return null;
	}
	
//	BxDF: Sample_f(Vector3f &wo, Vector3f *wi, Point2f &sample, Float *pdf);
	public PBRTBSDFResult sampleDistributionFunction(final Vector3F outgoing, final Point2F sample) {
		return null;
	}
}