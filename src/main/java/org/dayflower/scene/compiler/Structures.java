/**
 * Copyright 2014 - 2023 J&#246;rgen Lundgren
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

import static org.dayflower.utility.Ints.pack;
import static org.dayflower.utility.Ints.unpack;

import java.util.Objects;

import org.dayflower.utility.ParameterArguments;

import org.macroing.java.util.Arrays;

final class Structures {
	private Structures() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static boolean updateStructure(final float[] structures, final float[] oldStructure, final float[] newStructure, final int structureOffset) {
		Objects.requireNonNull(structures, "structures == null");
		Objects.requireNonNull(oldStructure, "oldStructure == null");
		Objects.requireNonNull(newStructure, "newStructure == null");
		
		ParameterArguments.requireRange(structureOffset, 0, structures.length - 1, "structureOffset");
		ParameterArguments.requireRange(structureOffset + oldStructure.length, 0, structures.length, "structureOffset + oldStructure.length");
		ParameterArguments.requireExact(newStructure.length, oldStructure.length, "newStructure.length");
		
		if(Arrays.equals(structures, oldStructure, structureOffset, 0, oldStructure.length)) {
			System.arraycopy(newStructure, 0, structures, structureOffset, newStructure.length);
			
			return true;
		}
		
		return false;
	}
	
	public static int getStructureCount(final float[] structures, final int structureLength) {
		Objects.requireNonNull(structures, "structures == null");
		
		ParameterArguments.requireRange(structureLength, 1, Integer.MAX_VALUE, "structureLength");
		
		return structures.length % structureLength == 0 ? structures.length / structureLength : 0;
	}
	
	public static int getStructureCount(final int[] structures, final int structureLength) {
		Objects.requireNonNull(structures, "structures == null");
		
		ParameterArguments.requireRange(structureLength, 1, Integer.MAX_VALUE, "structureLength");
		
		return structures.length % structureLength == 0 ? structures.length / structureLength : 0;
	}
	
	public static int getStructureOffsetAbsolute(final float[] structures, final float[] structure) {
		return Arrays.indexOf(structure, structures, true, false);
	}
	
	public static int getStructureOffsetAbsolute(final float[] structures, final float[] structure, final int[] structureOffsets) {
		Objects.requireNonNull(structures, "structures == null");
		Objects.requireNonNull(structure, "structure == null");
		Objects.requireNonNull(structureOffsets, "structureOffsets == null");
		
		final int structureCount = structureOffsets.length;
		
		for(int offsetAbsolute = 0, offsetRelative = 0; offsetRelative < structureCount; offsetRelative++) {
			final int structureLength = offsetRelative + 1 < structureOffsets.length ? structureOffsets[offsetRelative + 1] - structureOffsets[offsetRelative] : structures.length - structureOffsets[offsetRelative];
			
			if(structure.length == structureLength && Arrays.equals(structures, structure, offsetAbsolute, 0, structureLength)) {
				return offsetAbsolute;
			}
			
			offsetAbsolute += structureLength;
		}
		
		return -1;
	}
	
	public static int getStructureOffsetAbsolute(final int[] structures, final int[] structure) {
		return Arrays.indexOf(structure, structures, true, false);
	}
	
	public static int getStructureOffsetAbsolute(final int[] structures, final int[] structure, final int[] structureOffsets) {
		Objects.requireNonNull(structures, "structures == null");
		Objects.requireNonNull(structure, "structure == null");
		Objects.requireNonNull(structureOffsets, "structureOffsets == null");
		
		final int structureCount = structureOffsets.length;
		
		for(int offsetAbsolute = 0, offsetRelative = 0; offsetRelative < structureCount; offsetRelative++) {
			final int structureLength = offsetRelative + 1 < structureOffsets.length ? structureOffsets[offsetRelative + 1] - structureOffsets[offsetRelative] : structures.length - structureOffsets[offsetRelative];
			
			if(structure.length == structureLength && Arrays.equals(structures, structure, offsetAbsolute, 0, structureLength)) {
				return offsetAbsolute;
			}
			
			offsetAbsolute += structureLength;
		}
		
		return -1;
	}
	
	public static int getStructureOffsetRelative(final float[] structures, final float[] structure) {
		return Arrays.indexOf(structure, structures, true, true);
	}
	
	public static int getStructureOffsetRelative(final float[] structures, final float[] structure, final int[] structureOffsets) {
		Objects.requireNonNull(structures, "structures == null");
		Objects.requireNonNull(structure, "structure == null");
		Objects.requireNonNull(structureOffsets, "structureOffsets == null");
		
		final int structureCount = structureOffsets.length;
		
		for(int offsetAbsolute = 0, offsetRelative = 0; offsetRelative < structureCount; offsetRelative++) {
			final int structureLength = offsetRelative + 1 < structureOffsets.length ? structureOffsets[offsetRelative + 1] - structureOffsets[offsetRelative] : structures.length - structureOffsets[offsetRelative];
			
			if(structure.length == structureLength && Arrays.equals(structures, structure, offsetAbsolute, 0, structureLength)) {
				return offsetRelative;
			}
			
			offsetAbsolute += structureLength;
		}
		
		return -1;
	}
	
	public static int getStructureOffsetRelative(final int[] structures, final int[] structure) {
		return Arrays.indexOf(structure, structures, true, true);
	}
	
	public static int getStructureOffsetRelative(final int[] structures, final int[] structure, final int[] structureOffsets) {
		Objects.requireNonNull(structures, "structures == null");
		Objects.requireNonNull(structure, "structure == null");
		Objects.requireNonNull(structureOffsets, "structureOffsets == null");
		
		final int structureCount = structureOffsets.length;
		
		for(int offsetAbsolute = 0, offsetRelative = 0; offsetRelative < structureCount; offsetRelative++) {
			final int structureLength = offsetRelative + 1 < structureOffsets.length ? structureOffsets[offsetRelative + 1] - structureOffsets[offsetRelative] : structures.length - structureOffsets[offsetRelative];
			
			if(structure.length == structureLength && Arrays.equals(structures, structure, offsetAbsolute, 0, structureLength)) {
				return offsetRelative;
			}
			
			offsetAbsolute += structureLength;
		}
		
		return -1;
	}
	
	public static int[] addStructureIDAndOffset(final int[] structureIDsAndOffsets, final int structureID, final int structureOffset) {
		Objects.requireNonNull(structureIDsAndOffsets, "structureIDsAndOffsets == null");
		
		ParameterArguments.requireRange(structureID, 0, Integer.MAX_VALUE, "structureID");
		ParameterArguments.requireRange(structureOffset, 0, Integer.MAX_VALUE, "structureOffset");
		
		final int[] newStructureIDsAndOffsets = new int[structureIDsAndOffsets.length + 1];
		
		System.arraycopy(structureIDsAndOffsets, 0, newStructureIDsAndOffsets, 0, structureIDsAndOffsets.length);
		
		newStructureIDsAndOffsets[newStructureIDsAndOffsets.length - 1] = pack(structureID, structureOffset);
		
		return newStructureIDsAndOffsets;
	}
	
	public static int[] removeStructureIDAndOffset(final int[] structureIDsAndOffsets, final int structureID, final int structureOffset) {
		Objects.requireNonNull(structureIDsAndOffsets, "structureIDsAndOffsets == null");
		
		ParameterArguments.requireRange(structureID, 0, Integer.MAX_VALUE, "structureID");
		ParameterArguments.requireRange(structureOffset, 0, Integer.MAX_VALUE, "structureOffset");
		
		final int structureIDAndOffset = pack(structureID, structureOffset);
		final int index = Arrays.indexOf(structureIDAndOffset, structureIDsAndOffsets);
		
		if(index == -1) {
			return structureIDsAndOffsets;
		}
		
		final int[] newStructureIDsAndOffsets = new int[structureIDsAndOffsets.length - 1];
		
		System.arraycopy(structureIDsAndOffsets, 0, newStructureIDsAndOffsets, 0, index);
		
		for(int i = index + 1; i < structureIDsAndOffsets.length; i++) {
			final int[] currentStructureIDAndOffset = unpack(structureIDsAndOffsets[i]);
			
			final int currentStructureID = currentStructureIDAndOffset[0];
			final int currentStructureOffset = currentStructureIDAndOffset[1];
			
			if(currentStructureID == structureID) {
				newStructureIDsAndOffsets[i - 1] = pack(currentStructureID, currentStructureOffset - 1);
			} else {
				newStructureIDsAndOffsets[i - 1] = pack(currentStructureID, currentStructureOffset);
			}
		}
		
		return newStructureIDsAndOffsets;
	}
	
	public static int[] removeStructureOffset(final int[] structureOffsets, final int structureOffset, final int structureLength) {
		Objects.requireNonNull(structureOffsets, "structureOffsets == null");
		
		ParameterArguments.requireRange(structureOffset, 0, Integer.MAX_VALUE, "structureOffset");
		ParameterArguments.requireRange(structureLength, 1, Integer.MAX_VALUE, "structureLength");
		
		final int index = Arrays.indexOf(structureOffset, structureOffsets);
		
		if(index == -1) {
			return structureOffsets;
		}
		
		final int[] newStructureOffsets = new int[structureOffsets.length - 1];
		
		System.arraycopy(structureOffsets, 0, newStructureOffsets, 0, index);
		
		for(int i = index + 1; i < structureOffsets.length; i++) {
			newStructureOffsets[i - 1] = structureOffsets[i] - structureLength;
		}
		
		return newStructureOffsets;
	}
}