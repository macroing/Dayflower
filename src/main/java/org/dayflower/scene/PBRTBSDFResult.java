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
import java.util.Objects;

import org.dayflower.geometry.Vector3F;
import org.dayflower.image.Color3F;

public final class PBRTBSDFResult {
	private final PBRTBXDFType sampledBXDFType;
	private final Color3F distributionFunctionValue;
	private final Vector3F incoming;
	private final Vector3F outgoing;
	private final float probabilityDensityFunctionValue;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public PBRTBSDFResult(final PBRTBXDFType sampledBXDFType, final Color3F distributionFunctionValue, final Vector3F incoming, final Vector3F outgoing, final float probabilityDensityFunctionValue) {
		this.sampledBXDFType = Objects.requireNonNull(sampledBXDFType, "sampledBXDFType == null");
		this.distributionFunctionValue = Objects.requireNonNull(distributionFunctionValue, "distributionFunctionValue == null");
		this.incoming = Objects.requireNonNull(incoming, "incoming == null");
		this.outgoing = Objects.requireNonNull(outgoing, "outgoing == null");
		this.probabilityDensityFunctionValue = probabilityDensityFunctionValue;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public PBRTBXDFType getSampledBXDFType() {
		return this.sampledBXDFType;
	}
	
	public Color3F getDistributionFunctionValue() {
		return this.distributionFunctionValue;
	}
	
	public Vector3F getIncoming() {
		return this.incoming;
	}
	
	public Vector3F getOutgoing() {
		return this.outgoing;
	}
	
	public float getProbabilityDensityFunctionValue() {
		return this.probabilityDensityFunctionValue;
	}
}