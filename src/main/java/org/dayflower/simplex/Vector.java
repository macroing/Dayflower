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
package org.dayflower.simplex;

import static org.dayflower.utility.Doubles.sqrt;

import java.lang.reflect.Field;//TODO: Add Javadocs!
import java.util.Objects;

import org.dayflower.utility.ParameterArguments;

//TODO: Add Javadocs!
public final class Vector {
	private Vector() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
//	TODO: Refactor!
	public static double vector3DDotProduct(final double[] vector3DLHS, final double[] vector3DRHS) {
		Objects.requireNonNull(vector3DLHS, "vector3DLHS == null");
		Objects.requireNonNull(vector3DRHS, "vector3DRHS == null");
		
		ParameterArguments.requireExactArrayLength(vector3DLHS, 3, "vector3DLHS");
		ParameterArguments.requireExactArrayLength(vector3DRHS, 3, "vector3DRHS");
		
		return vector3DLHS[0] * vector3DRHS[0] + vector3DLHS[1] * vector3DRHS[1] + vector3DLHS[2] * vector3DRHS[2];
	}
	
//	TODO: Add Javadocs!
//	TODO: Refactor!
	public static double vector3DLength(final double[] vector3D) {
		return sqrt(vector3DLengthSquared(vector3D));
	}
	
//	TODO: Add Javadocs!
//	TODO: Refactor!
	public static double vector3DLengthSquared(final double[] vector3D) {
		Objects.requireNonNull(vector3D, "vector3D == null");
		
		ParameterArguments.requireExactArrayLength(vector3D, 3, "vector3D");
		
		return vector3D[0] * vector3D[0] + vector3D[1] * vector3D[1] + vector3D[2] * vector3D[2];
	}
	
//	TODO: Add Javadocs!
	public static double[] vector3D(final double component1, final double component2, final double component3) {
		return new double[] {component1, component2, component3};
	}
	
//	TODO: Add Javadocs!
//	TODO: Refactor!
	public static double[] vector3DAdd(final double[] vector3DLHS, final double[] vector3DRHS) {
		Objects.requireNonNull(vector3DLHS, "vector3DLHS == null");
		Objects.requireNonNull(vector3DRHS, "vector3DRHS == null");
		
		ParameterArguments.requireExactArrayLength(vector3DLHS, 3, "vector3DLHS");
		ParameterArguments.requireExactArrayLength(vector3DRHS, 3, "vector3DRHS");
		
		final double component1 = vector3DLHS[0] + vector3DRHS[0];
		final double component2 = vector3DLHS[1] + vector3DRHS[1];
		final double component3 = vector3DLHS[2] + vector3DRHS[2];
		
		return vector3D(component1, component2, component3);
	}
	
//	TODO: Add Javadocs!
//	TODO: Refactor!
	public static double[] vector3DCrossProduct(final double[] vector3DLHS, final double[] vector3DRHS) {
		Objects.requireNonNull(vector3DLHS, "vector3DLHS == null");
		Objects.requireNonNull(vector3DRHS, "vector3DRHS == null");
		
		ParameterArguments.requireExactArrayLength(vector3DLHS, 3, "vector3DLHS");
		ParameterArguments.requireExactArrayLength(vector3DRHS, 3, "vector3DRHS");
		
		final double component1 = vector3DLHS[1] * vector3DRHS[2] - vector3DLHS[2] * vector3DRHS[1];
		final double component2 = vector3DLHS[2] * vector3DRHS[0] - vector3DLHS[0] * vector3DRHS[2];
		final double component3 = vector3DLHS[0] * vector3DRHS[1] - vector3DLHS[1] * vector3DRHS[0];
		
		return vector3D(component1, component2, component3);
	}
	
//	TODO: Add Javadocs!
//	TODO: Refactor!
	public static double[] vector3DDirection(final double[] point3DEye, final double[] point3DTarget) {
		Objects.requireNonNull(point3DEye, "point3DEye == null");
		Objects.requireNonNull(point3DTarget, "point3DTarget == null");
		
		ParameterArguments.requireExactArrayLength(point3DEye, 3, "point3DEye");
		ParameterArguments.requireExactArrayLength(point3DTarget, 3, "point3DTarget");
		
		final double component1 = point3DTarget[0] - point3DEye[0];
		final double component2 = point3DTarget[1] - point3DEye[1];
		final double component3 = point3DTarget[2] - point3DEye[2];
		
		return vector3D(component1, component2, component3);
	}
	
//	TODO: Add Javadocs!
//	TODO: Refactor!
	public static double[] vector3DDirectionNormalized(final double[] point3DEye, final double[] point3DTarget) {
		return vector3DNormalize(vector3DDirection(point3DEye, point3DTarget));
	}
	
//	TODO: Add Javadocs!
//	TODO: Refactor!
	public static double[] vector3DDivide(final double[] vector3DLHS, final double scalarRHS) {
		Objects.requireNonNull(vector3DLHS, "vector3DLHS == null");
		
		ParameterArguments.requireExactArrayLength(vector3DLHS, 3, "vector3DLHS");
		
		final double component1 = vector3DLHS[0] / scalarRHS;
		final double component2 = vector3DLHS[1] / scalarRHS;
		final double component3 = vector3DLHS[2] / scalarRHS;
		
		return vector3D(component1, component2, component3);
	}
	
//	TODO: Add Javadocs!
//	TODO: Refactor!
	public static double[] vector3DFromPoint3D(final double[] point3D) {
		Objects.requireNonNull(point3D, "point3D == null");
		
		ParameterArguments.requireExactArrayLength(point3D, 3, "point3D");
		
		final double component1 = point3D[0];
		final double component2 = point3D[1];
		final double component3 = point3D[2];
		
		return vector3D(component1, component2, component3);
	}
	
//	TODO: Add Javadocs!
//	TODO: Refactor!
	public static double[] vector3DMultiply(final double[] vector3DLHS, final double scalarRHS) {
		Objects.requireNonNull(vector3DLHS, "vector3DLHS == null");
		
		ParameterArguments.requireExactArrayLength(vector3DLHS, 3, "vector3DLHS");
		
		final double component1 = vector3DLHS[0] * scalarRHS;
		final double component2 = vector3DLHS[1] * scalarRHS;
		final double component3 = vector3DLHS[2] * scalarRHS;
		
		return vector3D(component1, component2, component3);
	}
	
//	TODO: Add Javadocs!
//	TODO: Refactor!
	public static double[] vector3DNormalize(final double[] vector3D) {
		return vector3DDivide(vector3D, vector3DLength(vector3D));
	}
	
//	TODO: Add Javadocs!
//	TODO: Refactor!
	public static double[] vector3DTransformMatrix44D(final double[] matrix44DLHS, final double[] vector3DRHS) {
		Objects.requireNonNull(matrix44DLHS, "matrix44DLHS == null");
		Objects.requireNonNull(vector3DRHS, "vector3DRHS == null");
		
		ParameterArguments.requireExactArrayLength(matrix44DLHS, 16, "matrix44DLHS");
		ParameterArguments.requireExactArrayLength(vector3DRHS, 3, "vector3DRHS");
		
		final double component1 = matrix44DLHS[0] * vector3DRHS[0] + matrix44DLHS[1] * vector3DRHS[1] + matrix44DLHS[ 2] * vector3DRHS[2];
		final double component2 = matrix44DLHS[4] * vector3DRHS[0] + matrix44DLHS[5] * vector3DRHS[1] + matrix44DLHS[ 6] * vector3DRHS[2];
		final double component3 = matrix44DLHS[8] * vector3DRHS[0] + matrix44DLHS[9] * vector3DRHS[1] + matrix44DLHS[10] * vector3DRHS[2];
		
		return vector3D(component1, component2, component3);
	}
	
//	TODO: Add Javadocs!
//	TODO: Refactor!
	public static double[] vector3DTransformTransposeMatrix44D(final double[] matrix44DLHS, final double[] vector3DRHS) {
		Objects.requireNonNull(matrix44DLHS, "matrix44DLHS == null");
		Objects.requireNonNull(vector3DRHS, "vector3DRHS == null");
		
		ParameterArguments.requireExactArrayLength(matrix44DLHS, 16, "matrix44DLHS");
		ParameterArguments.requireExactArrayLength(vector3DRHS, 3, "vector3DRHS");
		
		final double component1 = matrix44DLHS[0] * vector3DRHS[0] + matrix44DLHS[4] * vector3DRHS[1] + matrix44DLHS[ 8] * vector3DRHS[2];
		final double component2 = matrix44DLHS[1] * vector3DRHS[0] + matrix44DLHS[5] * vector3DRHS[1] + matrix44DLHS[ 9] * vector3DRHS[2];
		final double component3 = matrix44DLHS[2] * vector3DRHS[0] + matrix44DLHS[6] * vector3DRHS[1] + matrix44DLHS[10] * vector3DRHS[2];
		
		return vector3D(component1, component2, component3);
	}
}