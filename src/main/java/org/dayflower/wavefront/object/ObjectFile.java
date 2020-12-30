/**
 * Copyright 2020 - 2021 J&#246;rgen Lundgren
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
package org.dayflower.wavefront.object;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * An {@code ObjectFile} represents a Wavefront Object file.
 * <p>
 * A Wavefront Object file has a filename extension of either {@code .obj} (ASCII) or {@code .mod} (binary).
 * <p>
 * Currently this library only supports loading Wavefront Object files with a filename extension of {@code .obj} (ASCII).
 * <p>
 * This class is not thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class ObjectFile implements Iterable<ObjectFileStatement> {
	private final List<ObjectFileStatement> objectFileStatements;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new empty {@code ObjectFile} instance.
	 */
	public ObjectFile() {
		this.objectFileStatements = new ArrayList<>();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns an {@code Iterator} over the {@link ObjectFileStatement}s in this {@code ObjectFile} in proper sequence.
	 * 
	 * @return an {@code Iterator} over the {@code ObjectFileStatement}s in this {@code ObjectFile} in proper sequence
	 */
	@Override
	public Iterator<ObjectFileStatement> iterator() {
		return getObjectFileStatements().iterator();
	}
	
	/**
	 * Returns a {@code List} with all {@link ObjectFileStatement}s currently added to this {@code ObjectFile} instance.
	 * <p>
	 * Modifying the returned {@code List} will not affect this {@code ObjectFile} instance.
	 * 
	 * @return a {@code List} with all {@code ObjectFileStatement}s currently added to this {@code ObjectFile} instance
	 */
	public List<ObjectFileStatement> getObjectFileStatements() {
		return new ArrayList<>(this.objectFileStatements);
	}
	
	/**
	 * Returns a {@code String} representation of this {@code ObjectFile} instance.
	 * 
	 * @return a {@code String} representation of this {@code ObjectFile} instance
	 */
	@Override
	public String toString() {
		return this.objectFileStatements.stream().map(objectFileStatement -> objectFileStatement.toString()).collect(Collectors.joining("\n"));
	}
	
	/**
	 * Compares {@code object} to this {@code ObjectFile} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code ObjectFile}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code ObjectFile} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code ObjectFile}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof ObjectFile)) {
			return false;
		} else if(!Objects.equals(getObjectFileStatements(), ObjectFile.class.cast(object).getObjectFileStatements())) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns a hash code for this {@code ObjectFile} instance.
	 * 
	 * @return a hash code for this {@code ObjectFile} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(getObjectFileStatements());
	}
	
	/**
	 * Adds {@code objectFileStatement} to this {@code ObjectFile} instance.
	 * <p>
	 * If {@code objectFileStatement} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param objectFileStatement the {@link ObjectFileStatement} to add
	 * @throws NullPointerException thrown if, and only if, {@code objectFileStatement} is {@code null}
	 */
	public void addObjectFileStatement(final ObjectFileStatement objectFileStatement) {
		this.objectFileStatements.add(Objects.requireNonNull(objectFileStatement, "objectFileStatement == null"));
	}
	
	/**
	 * Removes {@code objectFileStatement} from this {@code ObjectFile} instance, if present.
	 * <p>
	 * If {@code objectFileStatement} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param objectFileStatement the {@link ObjectFileStatement} to remove
	 * @throws NullPointerException thrown if, and only if, {@code objectFileStatement} is {@code null}
	 */
	public void removeObjectFileStatement(final ObjectFileStatement objectFileStatement) {
		this.objectFileStatements.remove(Objects.requireNonNull(objectFileStatement, "objectFileStatement == null"));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Parses a Wavefront Object file from the file represented by {@code file} into an {@code ObjectFile} instance.
	 * <p>
	 * Returns an {@code ObjectFile} instance.
	 * <p>
	 * Calling this method is equivalent to {@code ObjectFile.parse(file, new ObjectFileParser())}.
	 * <p>
	 * If {@code file} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If the Wavefront Object file cannot be parsed, an {@link ObjectFileException} will be thrown.
	 * 
	 * @param file a {@code File} representing the file to parse from
	 * @return an {@code ObjectFile} instance
	 * @throws NullPointerException thrown if, and only if, {@code file} is {@code null}
	 * @throws ObjectFileException thrown if, and only if, the Wavefront Object file cannot be parsed
	 */
	public static ObjectFile parse(final File file) throws ObjectFileException {
		return parse(file, new ObjectFileParser());
	}
	
	/**
	 * Parses a Wavefront Object file from the file represented by {@code file} into an {@code ObjectFile} instance.
	 * <p>
	 * Returns an {@code ObjectFile} instance.
	 * <p>
	 * If either {@code file} or {@code objectFileParser} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If the Wavefront Object file cannot be parsed, an {@link ObjectFileException} will be thrown.
	 * 
	 * @param file a {@code File} representing the file to parse from
	 * @param objectFileParser the {@link ObjectFileParser} that takes care of parsing
	 * @return an {@code ObjectFile} instance
	 * @throws NullPointerException thrown if, and only if, either {@code file} or {@code objectFileParser} are {@code null}
	 * @throws ObjectFileException thrown if, and only if, the Wavefront Object file cannot be parsed
	 */
	public static ObjectFile parse(final File file, final ObjectFileParser objectFileParser) throws ObjectFileException {
		final ObjectFile objectFile = new ObjectFile();
		
		final ObjectFileParserObserver objectFileParserObserver = objectFileStatement -> objectFile.addObjectFileStatement(objectFileStatement);
		
		objectFileParser.addObjectFileParserObserver(objectFileParserObserver);
		objectFileParser.parse(file);
		objectFileParser.removeObjectFileParserObserver(objectFileParserObserver);
		
		return objectFile;
	}
	
	/**
	 * Parses a Wavefront Object file from the file represented by {@code filename} into an {@code ObjectFile} instance.
	 * <p>
	 * Returns an {@code ObjectFile} instance.
	 * <p>
	 * Calling this method is equivalent to {@code ObjectFile.parse(filename, new ObjectFileParser())}.
	 * <p>
	 * If {@code filename} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If the Wavefront Object file cannot be parsed, an {@link ObjectFileException} will be thrown.
	 * 
	 * @param filename a {@code String} representing the filename of the file to parse from
	 * @return an {@code ObjectFile} instance
	 * @throws NullPointerException thrown if, and only if, {@code filename} is {@code null}
	 * @throws ObjectFileException thrown if, and only if, the Wavefront Object file cannot be parsed
	 */
	public static ObjectFile parse(final String filename) throws ObjectFileException {
		return parse(filename, new ObjectFileParser());
	}
	
	/**
	 * Parses a Wavefront Object file from the file represented by {@code filename} into an {@code ObjectFile} instance.
	 * <p>
	 * Returns an {@code ObjectFile} instance.
	 * <p>
	 * Calling this method is equivalent to {@code ObjectFile.parse(new File(filename), objectFileParser)}, assuming {@code filename} is not {@code null}.
	 * <p>
	 * If either {@code filename} or {@code objectFileParser} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If the Wavefront Object file cannot be parsed, an {@link ObjectFileException} will be thrown.
	 * 
	 * @param filename a {@code String} representing the filename of the file to parse from
	 * @param objectFileParser the {@link ObjectFileParser} that takes care of parsing
	 * @return an {@code ObjectFile} instance
	 * @throws NullPointerException thrown if, and only if, either {@code filename} or {@code objectFileParser} are {@code null}
	 * @throws ObjectFileException thrown if, and only if, the Wavefront Object file cannot be parsed
	 */
	public static ObjectFile parse(final String filename, final ObjectFileParser objectFileParser) throws ObjectFileException {
		return parse(new File(Objects.requireNonNull(filename, "filename == null")), objectFileParser);
	}
}