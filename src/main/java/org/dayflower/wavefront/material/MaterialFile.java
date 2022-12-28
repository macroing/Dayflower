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
package org.dayflower.wavefront.material;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * A {@code MaterialFile} represents a Wavefront Material file.
 * <p>
 * A Wavefront Material file has a filename extension of {@code .mtl} (ASCII).
 * <p>
 * This class is not thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class MaterialFile implements Iterable<MaterialFileStatement> {
	private final List<MaterialFileStatement> materialFileStatements;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new empty {@code MaterialFile} instance.
	 */
	public MaterialFile() {
		this.materialFileStatements = new ArrayList<>();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns an {@code Iterator} over the {@link MaterialFileStatement}s in this {@code MaterialFile} in proper sequence.
	 * 
	 * @return an {@code Iterator} over the {@code MaterialFileStatement}s in this {@code MaterialFile} in proper sequence
	 */
	@Override
	public Iterator<MaterialFileStatement> iterator() {
		return getMaterialFileStatements().iterator();
	}
	
	/**
	 * Returns a {@code List} with all {@link MaterialFileStatement}s currently added to this {@code MaterialFile} instance.
	 * <p>
	 * Modifying the returned {@code List} will not affect this {@code MaterialFile} instance.
	 * 
	 * @return a {@code List} with all {@code MaterialFileStatement}s currently added to this {@code MaterialFile} instance
	 */
	public List<MaterialFileStatement> getMaterialFileStatements() {
		return new ArrayList<>(this.materialFileStatements);
	}
	
	/**
	 * Returns a {@code String} representation of this {@code MaterialFile} instance.
	 * 
	 * @return a {@code String} representation of this {@code MaterialFile} instance
	 */
	@Override
	public String toString() {
		return this.materialFileStatements.stream().map(materialFileStatement -> materialFileStatement.toString()).collect(Collectors.joining("\n"));
	}
	
	/**
	 * Compares {@code object} to this {@code MaterialFile} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code MaterialFile}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code MaterialFile} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code MaterialFile}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof MaterialFile)) {
			return false;
		} else if(!Objects.equals(getMaterialFileStatements(), MaterialFile.class.cast(object).getMaterialFileStatements())) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns a hash code for this {@code MaterialFile} instance.
	 * 
	 * @return a hash code for this {@code MaterialFile} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(getMaterialFileStatements());
	}
	
	/**
	 * Adds {@code materialFileStatement} to this {@code MaterialFile} instance.
	 * <p>
	 * If {@code materialFileStatement} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param materialFileStatement the {@link MaterialFileStatement} to add
	 * @throws NullPointerException thrown if, and only if, {@code materialFileStatement} is {@code null}
	 */
	public void addMaterialFileStatement(final MaterialFileStatement materialFileStatement) {
		this.materialFileStatements.add(Objects.requireNonNull(materialFileStatement, "materialFileStatement == null"));
	}
	
	/**
	 * Removes {@code materialFileStatement} from this {@code MaterialFile} instance, if present.
	 * <p>
	 * If {@code materialFileStatement} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param materialFileStatement the {@link MaterialFileStatement} to remove
	 * @throws NullPointerException thrown if, and only if, {@code materialFileStatement} is {@code null}
	 */
	public void removeMaterialFileStatement(final MaterialFileStatement materialFileStatement) {
		this.materialFileStatements.remove(Objects.requireNonNull(materialFileStatement, "materialFileStatement == null"));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Parses a Wavefront Material file from the file represented by {@code file} into a {@code MaterialFile} instance.
	 * <p>
	 * Returns a {@code MaterialFile} instance.
	 * <p>
	 * Calling this method is equivalent to {@code MaterialFile.parse(file, new MaterialFileParser())}.
	 * <p>
	 * If {@code file} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If the Wavefront Material file cannot be parsed, a {@link MaterialFileException} will be thrown.
	 * 
	 * @param file a {@code File} representing the file to parse from
	 * @return a {@code MaterialFile} instance
	 * @throws MaterialFileException thrown if, and only if, the Wavefront Material file cannot be parsed
	 * @throws NullPointerException thrown if, and only if, {@code file} is {@code null}
	 */
	public static MaterialFile parse(final File file) throws MaterialFileException {
		return parse(file, new MaterialFileParser());
	}
	
	/**
	 * Parses a Wavefront Material file from the file represented by {@code file} into a {@code MaterialFile} instance.
	 * <p>
	 * Returns a {@code MaterialFile} instance.
	 * <p>
	 * If either {@code file} or {@code materialFileParser} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If the Wavefront Material file cannot be parsed, a {@link MaterialFileException} will be thrown.
	 * 
	 * @param file a {@code File} representing the file to parse from
	 * @param materialFileParser the {@link MaterialFileParser} that takes care of parsing
	 * @return a {@code MaterialFile} instance
	 * @throws MaterialFileException thrown if, and only if, the Wavefront Material file cannot be parsed
	 * @throws NullPointerException thrown if, and only if, either {@code file} or {@code materialFileParser} are {@code null}
	 */
	public static MaterialFile parse(final File file, final MaterialFileParser materialFileParser) throws MaterialFileException {
		final MaterialFile materialFile = new MaterialFile();
		
		final MaterialFileParserObserver materialFileParserObserver = materialFileStatement -> materialFile.addMaterialFileStatement(materialFileStatement);
		
		materialFileParser.addMaterialFileParserObserver(materialFileParserObserver);
		materialFileParser.parse(file);
		materialFileParser.removeMaterialFileParserObserver(materialFileParserObserver);
		
		return materialFile;
	}
	
	/**
	 * Parses a Wavefront Material file from the file represented by {@code filename} into a {@code MaterialFile} instance.
	 * <p>
	 * Returns a {@code MaterialFile} instance.
	 * <p>
	 * Calling this method is equivalent to {@code MaterialFile.parse(filename, new MaterialFileParser())}.
	 * <p>
	 * If {@code filename} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If the Wavefront Material file cannot be parsed, a {@link MaterialFileException} will be thrown.
	 * 
	 * @param filename a {@code String} representing the filename of the file to parse from
	 * @return a {@code MaterialFile} instance
	 * @throws MaterialFileException thrown if, and only if, the Wavefront Material file cannot be parsed
	 * @throws NullPointerException thrown if, and only if, {@code filename} is {@code null}
	 */
	public static MaterialFile parse(final String filename) throws MaterialFileException {
		return parse(filename, new MaterialFileParser());
	}
	
	/**
	 * Parses a Wavefront Material file from the file represented by {@code filename} into a {@code MaterialFile} instance.
	 * <p>
	 * Returns a {@code MaterialFile} instance.
	 * <p>
	 * Calling this method is equivalent to {@code MaterialFile.parse(new File(filename), materialFileParser)}, assuming {@code filename} is not {@code null}.
	 * <p>
	 * If either {@code filename} or {@code materialFileParser} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If the Wavefront Material file cannot be parsed, a {@link MaterialFileException} will be thrown.
	 * 
	 * @param filename a {@code String} representing the filename of the file to parse from
	 * @param materialFileParser the {@link MaterialFileParser} that takes care of parsing
	 * @return a {@code MaterialFile} instance
	 * @throws MaterialFileException thrown if, and only if, the Wavefront Material file cannot be parsed
	 * @throws NullPointerException thrown if, and only if, either {@code filename} or {@code materialFileParser} are {@code null}
	 */
	public static MaterialFile parse(final String filename, final MaterialFileParser materialFileParser) throws MaterialFileException {
		return parse(new File(Objects.requireNonNull(filename, "filename == null")), materialFileParser);
	}
}