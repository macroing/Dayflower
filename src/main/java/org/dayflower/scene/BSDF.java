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

import java.util.Optional;

import org.dayflower.geometry.Point2F;
import org.dayflower.geometry.Vector3F;
import org.dayflower.image.Color3F;

/**
 * A {@code BSDF} represents a BSDF (Bidirectional Scattering Distribution Function).
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public interface BSDF {
	/**
	 * Evaluates the distribution function.
	 * <p>
	 * Returns a {@link Color3F} with the result of the evaluation.
	 * <p>
	 * If either {@code bXDFType}, {@code outgoingWorldSpace}, {@code normalWorldSpace} or {@code incomingWorldSpace} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param bXDFType a {@link BXDFType} instance to match against
	 * @param outgoingWorldSpace the outgoing direction
	 * @param normalWorldSpace the normal
	 * @param incomingWorldSpace the incoming direction
	 * @return a {@code Color3F} with the result of the evaluation
	 * @throws NullPointerException thrown if, and only if, either {@code bXDFType}, {@code outgoingWorldSpace}, {@code normalWorldSpace} or {@code incomingWorldSpace} are {@code null}
	 */
	Color3F evaluateDistributionFunction(final BXDFType bXDFType, final Vector3F outgoingWorldSpace, final Vector3F normalWorldSpace, final Vector3F incomingWorldSpace);
	
	/**
	 * Samples the distribution function.
	 * <p>
	 * Returns an optional {@link BSDFResult} with the result of the sampling.
	 * <p>
	 * If either {@code bXDFType}, {@code outgoingWorldSpace}, {@code normalWorldSpace} or {@code sample} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param bXDFType a {@link BXDFType} instance to match against
	 * @param outgoingWorldSpace the outgoing direction
	 * @param normalWorldSpace the normal
	 * @param sample the sample point
	 * @return an optional {@code BSDFResult} with the result of the sampling
	 * @throws NullPointerException thrown if, and only if, either {@code bXDFType}, {@code outgoingWorldSpace}, {@code normalWorldSpace} or {@code sample} are {@code null}
	 */
	Optional<BSDFResult> sampleDistributionFunction(final BXDFType bXDFType, final Vector3F outgoingWorldSpace, final Vector3F normalWorldSpace, final Point2F sample);
	
	/**
	 * Evaluates the probability density function (PDF).
	 * <p>
	 * Returns a {@code float} with the probability density function (PDF) value.
	 * <p>
	 * If either {@code bXDFType}, {@code outgoingWorldSpace}, {@code normalWorldSpace} or {@code incomingWorldSpace} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param bXDFType a {@link BXDFType} instance to match against
	 * @param outgoingWorldSpace the outgoing direction
	 * @param normalWorldSpace the normal
	 * @param incomingWorldSpace the incoming direction
	 * @return a {@code float} with the probability density function (PDF) value
	 * @throws NullPointerException thrown if, and only if, either {@code bXDFType}, {@code outgoingWorldSpace}, {@code normalWorldSpace} or {@code incomingWorldSpace} are {@code null}
	 */
	float evaluateProbabilityDensityFunction(final BXDFType bXDFType, final Vector3F outgoingWorldSpace, final Vector3F normalWorldSpace, final Vector3F incomingWorldSpace);
	
	/**
	 * Returns the index of refraction (IOR).
	 * 
	 * @return the index of refraction (IOR)
	 */
	float getEta();
	
	/**
	 * Returns the {@link BXDF} count by specular or non-specular type.
	 * 
	 * @param isSpecular {@code true} if, and only if, specular types should be counted, {@code false} otherwise
	 * @return the {@code BXDF} count by specular or non-specular type
	 */
	int countBXDFsBySpecularType(final boolean isSpecular);
}