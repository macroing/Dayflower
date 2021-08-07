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

import static org.dayflower.utility.Doubles.PI_MULTIPLIED_BY_2;
import static org.dayflower.utility.Doubles.cos;
import static org.dayflower.utility.Doubles.equal;
import static org.dayflower.utility.Doubles.isZero;
import static org.dayflower.utility.Doubles.random;
import static org.dayflower.utility.Doubles.sin;
import static org.dayflower.utility.Doubles.sqrt;

import java.lang.reflect.Field;//TODO: Add Javadocs!
import java.util.Objects;

import org.dayflower.utility.ParameterArguments;

//TODO: Add Javadocs!
public final class Point {
	private Point() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public static double[] point2D(final double component1, final double component2) {
		return new double[] {component1, component2};
	}
	
//	TODO: Add Javadocs!
//	TODO: Refactor!
	public static double[] point2DSampleDiskUniformDistribution() {
		return point2DSampleDiskUniformDistribution(random(), random());
	}
	
//	TODO: Add Javadocs!
//	TODO: Refactor!
	public static double[] point2DSampleDiskUniformDistribution(final double u, final double v) {
		final double r = sqrt(u);
		final double theta = PI_MULTIPLIED_BY_2 * v;
		
		final double component1 = r * cos(theta);
		final double component2 = r * sin(theta);
		
		return point2D(component1, component2);
	}
	
//	TODO: Add Javadocs!
	public static double[] point3D(final double component1, final double component2, final double component3) {
		return new double[] {component1, component2, component3};
	}
	
//	TODO: Add Javadocs!
//	TODO: Refactor!
	public static double[] point3DAdd(final double[] point3D, final double[] vector3D) {
		Objects.requireNonNull(point3D, "point3D == null");
		Objects.requireNonNull(vector3D, "vector3D == null");
		
		ParameterArguments.requireExactArrayLength(point3D, 3, "point3D");
		ParameterArguments.requireExactArrayLength(vector3D, 3, "vector3D");
		
		final double component1 = point3D[0] + vector3D[0];
		final double component2 = point3D[1] + vector3D[1];
		final double component3 = point3D[2] + vector3D[2];
		
		return point3D(component1, component2, component3);
	}
	
//	TODO: Add Javadocs!
//	TODO: Refactor!
	public static double[] point3DAdd(final double[] point3D, final double[] vector3D, final double scalar) {
		Objects.requireNonNull(point3D, "point3D == null");
		Objects.requireNonNull(vector3D, "vector3D == null");
		
		ParameterArguments.requireExactArrayLength(point3D, 3, "point3D");
		ParameterArguments.requireExactArrayLength(vector3D, 3, "vector3D");
		
		final double component1 = point3D[0] + vector3D[0] * scalar;
		final double component2 = point3D[1] + vector3D[1] * scalar;
		final double component3 = point3D[2] + vector3D[2] * scalar;
		
		return point3D(component1, component2, component3);
	}
	
//	TODO: Add Javadocs!
//	TODO: Refactor!
	public static double[] point3DFromVector3D(final double[] vector3D) {
		Objects.requireNonNull(vector3D, "vector3D == null");
		
		ParameterArguments.requireExactArrayLength(vector3D, 3, "vector3D");
		
		final double component1 = vector3D[0];
		final double component2 = vector3D[1];
		final double component3 = vector3D[2];
		
		return point3D(component1, component2, component3);
	}
	
//	TODO: Add Javadocs!
//	TODO: Refactor!
	public static double[] point3DTransformMatrix44D(final double[] matrix44DLHS, final double[] point3DRHS) {
		Objects.requireNonNull(matrix44DLHS, "matrix44DLHS == null");
		Objects.requireNonNull(point3DRHS, "point3DRHS == null");
		
		ParameterArguments.requireExactArrayLength(matrix44DLHS, 16, "matrix44DLHS");
		ParameterArguments.requireExactArrayLength(point3DRHS, 3, "point3DRHS");
		
		final double component1 = matrix44DLHS[0] * point3DRHS[0] + matrix44DLHS[1] * point3DRHS[1] + matrix44DLHS[ 2] * point3DRHS[2] + matrix44DLHS[ 3];
		final double component2 = matrix44DLHS[4] * point3DRHS[0] + matrix44DLHS[5] * point3DRHS[1] + matrix44DLHS[ 6] * point3DRHS[2] + matrix44DLHS[ 7];
		final double component3 = matrix44DLHS[8] * point3DRHS[0] + matrix44DLHS[9] * point3DRHS[1] + matrix44DLHS[10] * point3DRHS[2] + matrix44DLHS[11];
		
		return point3D(component1, component2, component3);
	}
	
//	TODO: Add Javadocs!
//	TODO: Refactor!
	public static double[] point3DTransformAndDivideMatrix44D(final double[] matrix44DLHS, final double[] point3DRHS) {
		Objects.requireNonNull(matrix44DLHS, "matrix44DLHS == null");
		Objects.requireNonNull(point3DRHS, "point3DRHS == null");
		
		ParameterArguments.requireExactArrayLength(matrix44DLHS, 16, "matrix44DLHS");
		ParameterArguments.requireExactArrayLength(point3DRHS, 3, "point3DRHS");
		
		final double component1 = matrix44DLHS[ 0] * point3DRHS[0] + matrix44DLHS[ 1] * point3DRHS[1] + matrix44DLHS[ 2] * point3DRHS[2] + matrix44DLHS[ 3];
		final double component2 = matrix44DLHS[ 4] * point3DRHS[0] + matrix44DLHS[ 5] * point3DRHS[1] + matrix44DLHS[ 6] * point3DRHS[2] + matrix44DLHS[ 7];
		final double component3 = matrix44DLHS[ 8] * point3DRHS[0] + matrix44DLHS[ 9] * point3DRHS[1] + matrix44DLHS[10] * point3DRHS[2] + matrix44DLHS[11];
		final double component4 = matrix44DLHS[12] * point3DRHS[0] + matrix44DLHS[13] * point3DRHS[1] + matrix44DLHS[14] * point3DRHS[2] + matrix44DLHS[15];
		
		if(equal(component4, 1.0D) || isZero(component4)) {
			return point3D(component1, component2, component3);
		}
		
		return point3D(component1 / component4, component2 / component4, component3 / component4);
	}
}