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
import static org.dayflower.utility.Ints.equal;

import java.util.Objects;

import org.dayflower.utility.ParameterArguments;

final class Structures {
	private Structures() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static boolean updateStructure(final float[] structures, final float[] structure, final int structureOffset) {
//		Check that both 'structures' and 'structure' are not 'null':
		Objects.requireNonNull(structures, "structures == null");
		Objects.requireNonNull(structure, "structure == null");
		
//		Check that 'structureOffset' is greater than or equal to '0' and less than 'structures.length' and 'structureOffset + structure.length' is greater than or equal to '0' and less than or equal to 'structures.length':
		ParameterArguments.requireRange(structureOffset, 0, structures.length - 1, "structureOffset");
		ParameterArguments.requireRange(structureOffset + structure.length, 0, structures.length, "structureOffset + structure.length");
		
		boolean hasUpdated = false;
		
		for(int i = 0; i < structure.length; i++) {
			if(!equal(structures[structureOffset + i], structure[i])) {
				structures[structureOffset + i] = structure[i];
				
				hasUpdated = true;
			}
		}
		
		return hasUpdated;
	}
	
	public static float[] addStructure(final float[] structures, final float[] structure) {
		Objects.requireNonNull(structures, "structures == null");
		Objects.requireNonNull(structure, "structure == null");
		
		final float[] newStructures = new float[structures.length + structure.length];
		
		System.arraycopy(structures, 0, newStructures, 0, structures.length);
		System.arraycopy(structure, 0, newStructures, structures.length, structure.length);
		
		return newStructures;
	}
	
	public static float[] removeStructure(final float[] structures, final int structureOffsetAbsolute, final int structureLength) {
		Objects.requireNonNull(structures, "structures == null");
		
		ParameterArguments.requireRange(structureLength, 1, Integer.MAX_VALUE, "structureLength");
		ParameterArguments.requireRange(structureOffsetAbsolute, 0, structures.length - 1, "structureOffsetAbsolute");
		ParameterArguments.requireRange(structureOffsetAbsolute + structureLength, 0, structures.length, "structureOffsetAbsolute + structureLength");
		
		final float[] newStructures = new float[structures.length - structureLength];
		
		System.arraycopy(structures, 0, newStructures, 0, structureOffsetAbsolute);
		System.arraycopy(structures, structureOffsetAbsolute + structureLength, newStructures, structureOffsetAbsolute, structures.length - (structureOffsetAbsolute + structureLength));
		
		return newStructures;
	}
	
	public static int getStructureCount(final float[] structures, final int structureLength) {
//		Check that 'structures' is not 'null':
		Objects.requireNonNull(structures, "structures == null");
		
//		Check that 'structureLength' is greater than or equal to '1':
		ParameterArguments.requireRange(structureLength, 1, Integer.MAX_VALUE, "structureLength");
		
		return structures.length % structureLength == 0 ? structures.length / structureLength : 0;
	}
	
	public static int getStructureCount(final float[] structures, final int structureLength, final int structureLengthToReturn) {
//		Check that 'structures' is not 'null':
		Objects.requireNonNull(structures, "structures == null");
		
//		Check that 'structureLength' is greater than or equal to '1' and 'structureLengthToReturn' is greater than or equal to '0':
		ParameterArguments.requireRange(structureLength, 1, Integer.MAX_VALUE, "structureLength");
		ParameterArguments.requireRange(structureLengthToReturn, 0, Integer.MAX_VALUE, "structureLengthToReturn");
		
		return structures.length % structureLength == 0 ? structureLengthToReturn : 0;
	}
	
	public static int getStructureCount(final int[] structures, final int structureLength) {
//		Check that 'structures' is not 'null':
		Objects.requireNonNull(structures, "structures == null");
		
//		Check that 'structureLength' is greater than or equal to '1':
		ParameterArguments.requireRange(structureLength, 1, Integer.MAX_VALUE, "structureLength");
		
		return structures.length % structureLength == 0 ? structures.length / structureLength : 0;
	}
	
	public static int getStructureOffsetAbsolute(final float[] structures, final float[] structure, final int structureCount, final int structureLength) {
//		Check that both 'structures' and 'structure' are not 'null':
		Objects.requireNonNull(structures, "structures == null");
		Objects.requireNonNull(structure, "structure == null");
		
//		Check that 'structureCount' is greater than or equal to '0', 'structureLength' is greater than or equal to '1' and 'structure.length' is equal to 'structureLength':
		ParameterArguments.requireRange(structureCount, 0, Integer.MAX_VALUE, "structureCount");
		ParameterArguments.requireRange(structureLength, 1, Integer.MAX_VALUE, "structureLength");
		ParameterArguments.requireExactArrayLength(structure, structureLength, "structure");
		
		if(structureCount > 0) {
//			Check that 'structures.length' is equal to 'structureCount * structureLength':
			ParameterArguments.requireExactArrayLength(structures, structureCount * structureLength, "structureArray");
		}
		
		for(int offsetAbsolute = 0, offsetRelative = 0; offsetRelative < structureCount; offsetAbsolute += structureLength, offsetRelative++) {
			if(equal(structures, structure, offsetAbsolute, 0, structureLength)) {
				return offsetAbsolute;
			}
		}
		
		return -1;
	}
	
	public static int getStructureOffsetAbsolute(final int[] structures, final int[] structure, final int structureCount, final int structureLength) {
//		Check that both 'structures' and 'structure' are not 'null':
		Objects.requireNonNull(structures, "structures == null");
		Objects.requireNonNull(structure, "structure == null");
		
//		Check that 'structureCount' is greater than or equal to '0', 'structureLength' is greater than or equal to '1' and 'structure.length' is equal to 'structureLength':
		ParameterArguments.requireRange(structureCount, 0, Integer.MAX_VALUE, "structureCount");
		ParameterArguments.requireRange(structureLength, 1, Integer.MAX_VALUE, "structureLength");
		ParameterArguments.requireExactArrayLength(structure, structureLength, "structure");
		
		if(structureCount > 0) {
//			Check that 'structures.length' is equal to 'structureCount * structureLength':
			ParameterArguments.requireExactArrayLength(structures, structureCount * structureLength, "structureArray");
		}
		
		for(int offsetAbsolute = 0, offsetRelative = 0; offsetRelative < structureCount; offsetAbsolute += structureLength, offsetRelative++) {
			if(equal(structures, structure, offsetAbsolute, 0, structureLength)) {
				return offsetAbsolute;
			}
		}
		
		return -1;
	}
	
	public static int getStructureOffsetRelative(final float[] structures, final float[] structure, final int structureCount, final int structureLength) {
//		Check that both 'structures' and 'structure' are not 'null':
		Objects.requireNonNull(structures, "structures == null");
		Objects.requireNonNull(structure, "structure == null");
		
//		Check that 'structureCount' is greater than or equal to '0', 'structureLength' is greater than or equal to '1' and 'structure.length' is equal to 'structureLength':
		ParameterArguments.requireRange(structureCount, 0, Integer.MAX_VALUE, "structureCount");
		ParameterArguments.requireRange(structureLength, 1, Integer.MAX_VALUE, "structureLength");
		ParameterArguments.requireExactArrayLength(structure, structureLength, "structure");
		
		if(structureCount > 0) {
//			Check that 'structures.length' is equal to 'structureCount * structureLength':
			ParameterArguments.requireExactArrayLength(structures, structureCount * structureLength, "structureArray");
		}
		
		for(int offsetAbsolute = 0, offsetRelative = 0; offsetRelative < structureCount; offsetAbsolute += structureLength, offsetRelative++) {
			if(equal(structures, structure, offsetAbsolute, 0, structureLength)) {
				return offsetRelative;
			}
		}
		
		return -1;
	}
	
	public static int getStructureOffsetRelative(final int[] structures, final int[] structure, final int structureCount, final int structureLength) {
//		Check that both 'structures' and 'structure' are not 'null':
		Objects.requireNonNull(structures, "structures == null");
		Objects.requireNonNull(structure, "structure == null");
		
//		Check that 'structureCount' is greater than or equal to '0', 'structureLength' is greater than or equal to '1' and 'structure.length' is equal to 'structureLength':
		ParameterArguments.requireRange(structureCount, 0, Integer.MAX_VALUE, "structureCount");
		ParameterArguments.requireRange(structureLength, 1, Integer.MAX_VALUE, "structureLength");
		ParameterArguments.requireExactArrayLength(structure, structureLength, "structure");
		
		if(structureCount > 0) {
//			Check that 'structures.length' is equal to 'structureCount * structureLength':
			ParameterArguments.requireExactArrayLength(structures, structureCount * structureLength, "structureArray");
		}
		
		for(int offsetAbsolute = 0, offsetRelative = 0; offsetRelative < structureCount; offsetAbsolute += structureLength, offsetRelative++) {
			if(equal(structures, structure, offsetAbsolute, 0, structureLength)) {
				return offsetRelative;
			}
		}
		
		return -1;
	}
}