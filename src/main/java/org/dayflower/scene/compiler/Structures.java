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
package org.dayflower.scene.compiler;

import static org.dayflower.utility.Floats.equal;

final class Structures {
	private Structures() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static int getStructureCount(final float[] structureArray, final int structureLength) {
		return structureArray.length % structureLength == 0 ? structureArray.length / structureLength : 0;
	}
	
	public static int getStructureCount(final float[] structureArray, final int structureLength, final int structureLengthToReturn) {
		return structureArray.length % structureLength == 0 ? structureLengthToReturn : 0;
	}
	
	public static int getStructureCount(final int[] structureArray, final int structureLength) {
		return structureArray.length % structureLength == 0 ? structureArray.length / structureLength : 0;
	}
	
	public static int getStructureOffsetAbsolute(final float[] structureArray, final float[] structure, final int structureCount, final int structureLength) {
		for(int offsetAbsolute = 0, offsetRelative = 0; offsetRelative < structureCount; offsetAbsolute += structureLength, offsetRelative++) {
			if(equal(structureArray, structure, offsetAbsolute, 0, structureLength)) {
				return offsetAbsolute;
			}
		}
		
		return -1;
	}
	
	public static int getStructureOffsetRelative(final float[] structureArray, final float[] structure, final int structureCount, final int structureLength) {
		for(int offsetAbsolute = 0, offsetRelative = 0; offsetRelative < structureCount; offsetAbsolute += structureLength, offsetRelative++) {
			if(equal(structureArray, structure, offsetAbsolute, 0, structureLength)) {
				return offsetRelative;
			}
		}
		
		return -1;
	}
}