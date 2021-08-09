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

import static org.dayflower.simplex.Vector.vector3D;
import static org.dayflower.simplex.Vector.vector3DCrossProduct;
import static org.dayflower.simplex.Vector.vector3DNormalize;
import static org.dayflower.simplex.Vector.vector3DSet;

import java.lang.reflect.Field;//TODO: Add Javadocs!

//TODO: Add Javadocs!
public final class OrthonormalBasis {
	private OrthonormalBasis() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public static double[] orthonormalBasis33D() {
		return new double[] {0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D};
	}
	
//	TODO: Add Javadocs!
	public static double[] orthonormalBasis33DFromVector3DWV(final double[] vector3DW, final double[] vector3DV) {
		return orthonormalBasis33DFromVector3DWV(vector3DW, vector3DV, orthonormalBasis33D());
	}
	
//	TODO: Add Javadocs!
	public static double[] orthonormalBasis33DFromVector3DWV(final double[] vector3DW, final double[] vector3DV, final double[] orthonormalBasis33DResult) {
		return orthonormalBasis33DFromVector3DWV(vector3DW, vector3DV, orthonormalBasis33DResult, 0, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] orthonormalBasis33DFromVector3DWV(final double[] vector3DW, final double[] vector3DV, final double[] orthonormalBasis33DResult, final int vector3DWOffset, final int vector3DVOffset, final int orthonormalBasis33DResultOffset) {
		final double[] vector3DWNormalized = vector3DNormalize(vector3DW, vector3D(), vector3DWOffset, 0);
		final double[] vector3DUNormalized = vector3DCrossProduct(vector3DNormalize(vector3DV, vector3D(), vector3DVOffset, 0), vector3DWNormalized);
		final double[] vector3DVNormalized = vector3DCrossProduct(vector3DWNormalized, vector3DUNormalized);
		
		return orthonormalBasis33DSet(orthonormalBasis33DResult, vector3DUNormalized, vector3DVNormalized, vector3DWNormalized, orthonormalBasis33DResultOffset, 0, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] orthonormalBasis33DSet(final double[] orthonormalBasis33DResult, final double[] vector3DU, final double[] vector3DV, final double[] vector3DW) {
		return orthonormalBasis33DSet(orthonormalBasis33DResult, vector3DU, vector3DV, vector3DW, 0, 0, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] orthonormalBasis33DSet(final double[] orthonormalBasis33DResult, final double[] vector3DU, final double[] vector3DV, final double[] vector3DW, final int orthonormalBasis33DResultOffset, final int vector3DUOffset, final int vector3DVOffset, final int vector3DWOffset) {
		orthonormalBasis33DResult[orthonormalBasis33DResultOffset + 0] = vector3DU[vector3DUOffset + 0];
		orthonormalBasis33DResult[orthonormalBasis33DResultOffset + 1] = vector3DU[vector3DUOffset + 1];
		orthonormalBasis33DResult[orthonormalBasis33DResultOffset + 2] = vector3DU[vector3DUOffset + 2];
		
		orthonormalBasis33DResult[orthonormalBasis33DResultOffset + 3] = vector3DV[vector3DVOffset + 0];
		orthonormalBasis33DResult[orthonormalBasis33DResultOffset + 4] = vector3DV[vector3DVOffset + 1];
		orthonormalBasis33DResult[orthonormalBasis33DResultOffset + 5] = vector3DV[vector3DVOffset + 2];
		
		orthonormalBasis33DResult[orthonormalBasis33DResultOffset + 6] = vector3DW[vector3DWOffset + 0];
		orthonormalBasis33DResult[orthonormalBasis33DResultOffset + 7] = vector3DW[vector3DWOffset + 1];
		orthonormalBasis33DResult[orthonormalBasis33DResultOffset + 8] = vector3DW[vector3DWOffset + 2];
		
		return orthonormalBasis33DResult;
	}
	
//	TODO: Add Javadocs!
	public static double[] orthonormalBasis33DToVector3DU(final double[] orthonormalBasis33D) {
		return orthonormalBasis33DToVector3DU(orthonormalBasis33D, vector3D());
	}
	
//	TODO: Add Javadocs!
	public static double[] orthonormalBasis33DToVector3DU(final double[] orthonormalBasis33D, final double[] vector3DUResult) {
		return orthonormalBasis33DToVector3DU(orthonormalBasis33D, vector3DUResult, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] orthonormalBasis33DToVector3DU(final double[] orthonormalBasis33D, final double[] vector3DUResult, final int orthonormalBasis33DOffset, final int vector3DUResultOffset) {
		final double component1 = orthonormalBasis33D[orthonormalBasis33DOffset + 0];
		final double component2 = orthonormalBasis33D[orthonormalBasis33DOffset + 1];
		final double component3 = orthonormalBasis33D[orthonormalBasis33DOffset + 2];
		
		return vector3DSet(vector3DUResult, component1, component2, component3, vector3DUResultOffset);
	}
	
//	TODO: Add Javadocs!
	public static double[] orthonormalBasis33DToVector3DV(final double[] orthonormalBasis33D) {
		return orthonormalBasis33DToVector3DV(orthonormalBasis33D, vector3D());
	}
	
//	TODO: Add Javadocs!
	public static double[] orthonormalBasis33DToVector3DV(final double[] orthonormalBasis33D, final double[] vector3DVResult) {
		return orthonormalBasis33DToVector3DV(orthonormalBasis33D, vector3DVResult, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] orthonormalBasis33DToVector3DV(final double[] orthonormalBasis33D, final double[] vector3DVResult, final int orthonormalBasis33DOffset, final int vector3DVResultOffset) {
		final double component1 = orthonormalBasis33D[orthonormalBasis33DOffset + 3];
		final double component2 = orthonormalBasis33D[orthonormalBasis33DOffset + 4];
		final double component3 = orthonormalBasis33D[orthonormalBasis33DOffset + 5];
		
		return vector3DSet(vector3DVResult, component1, component2, component3, vector3DVResultOffset);
	}
	
//	TODO: Add Javadocs!
	public static double[] orthonormalBasis33DToVector3DW(final double[] orthonormalBasis33D) {
		return orthonormalBasis33DToVector3DW(orthonormalBasis33D, vector3D());
	}
	
//	TODO: Add Javadocs!
	public static double[] orthonormalBasis33DToVector3DW(final double[] orthonormalBasis33D, final double[] vector3DWResult) {
		return orthonormalBasis33DToVector3DW(orthonormalBasis33D, vector3DWResult, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] orthonormalBasis33DToVector3DW(final double[] orthonormalBasis33D, final double[] vector3DWResult, final int orthonormalBasis33DOffset, final int vector3DWResultOffset) {
		final double component1 = orthonormalBasis33D[orthonormalBasis33DOffset + 6];
		final double component2 = orthonormalBasis33D[orthonormalBasis33DOffset + 7];
		final double component3 = orthonormalBasis33D[orthonormalBasis33DOffset + 8];
		
		return vector3DSet(vector3DWResult, component1, component2, component3, vector3DWResultOffset);
	}
}