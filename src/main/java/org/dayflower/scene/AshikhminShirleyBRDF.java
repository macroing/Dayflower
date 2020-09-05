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

import org.dayflower.geometry.OrthonormalBasis33F;
import org.dayflower.geometry.Vector3F;

//TODO: Add Javadocs!
public final class AshikhminShirleyBRDF implements BXDF {
//	TODO: Add Javadocs!
	@Override
	public BXDFResult evaluateSolidAngle(final Vector3F o, final Vector3F n, final Vector3F i) {
		return evaluateSolidAngle(o, n, i, false);
	}
	
//	TODO: Add Javadocs!
	@Override
	public BXDFResult evaluateSolidAngle(final Vector3F o, final Vector3F n, final Vector3F i, final boolean isProjected) {
		return null;//TODO: Implement!
	}
	
//	TODO: Add Javadocs!
	@Override
	public BXDFResult sampleSolidAngle(final Vector3F o, final Vector3F n, final OrthonormalBasis33F orthonormalBasis, final float u, final float v) {
		return sampleSolidAngle(o, n, orthonormalBasis, u, v, false);
	}
	
//	TODO: Add Javadocs!
	@Override
	public BXDFResult sampleSolidAngle(final Vector3F o, final Vector3F n, final OrthonormalBasis33F orthonormalBasis, final float u, final float v, final boolean isProjected) {
		return null;//TODO: Implement!
	}
	
//	TODO: Add Javadocs!
	@Override
	public boolean isDiracDistribution() {
		return false;
	}
	
//	TODO: Add Javadocs!
	@Override
	public float probabilityDensityFunctionSolidAngle(final Vector3F o, final Vector3F n, final Vector3F i) {
		return probabilityDensityFunctionSolidAngle(o, n, i, false);
	}
	
//	TODO: Add Javadocs!
	@Override
	public float probabilityDensityFunctionSolidAngle(final Vector3F o, final Vector3F n, final Vector3F i, final boolean isProjected) {
		return 0.0F;//TODO: Implement!
	}
}