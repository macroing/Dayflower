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
import org.dayflower.image.Color3F;

//TODO: Add Javadocs!
public interface PBRTBXDF {
//	BxDF: Spectrum f(Vector3f &wo, Vector3f &wi)
//	TODO: Add Javadocs!
	PBRTBXDFDistributionFunctionResult evaluateDistributionFunction(final Vector3F outgoing, final Vector3F incoming);
	
//	BxDF: Spectrum Sample_f(Vector3f &wo, Vector3f *wi, Point2f &sample, Float *pdf)
//	TODO: Add Javadocs!
	PBRTBXDFDistributionFunctionResult sampleDistributionFunction(final Vector3F outgoing, final Point2F sample);
	
//	BxDF: Spectrum rho(int nSamples, Point2f *samples1, Point2f *samples2)
//	TODO: Add Javadocs!
	PBRTBXDFReflectanceFunctionResult sampleReflectanceFunction(final int samples);
	
//	BxDF: Spectrum rho(Vector3f &wo, int nSamples, Point2f *samples)
//	TODO: Add Javadocs!
	PBRTBXDFReflectanceFunctionResult sampleReflectanceFunction(final int samples, final Vector3F outgoing);
}