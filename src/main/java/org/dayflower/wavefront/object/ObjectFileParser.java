/**
 * Copyright 2014 - 2025 J&#246;rgen Lundgren
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;

import org.dayflower.utility.TextLine;
import org.dayflower.wavefront.object.FaceStatement.FaceElement;

/**
 * An {@code ObjectFileParser} is used for parsing Wavefront Object files.
 * <p>
 * To use this class, consider the following example:
 * <pre>
 * <code>
 * try {
 *     ObjectFileParser objectFileParser = new ObjectFileParser();
 *     objectFileParser.addObjectFileParserObserver(objectFileStatement -&gt; System.out.println(objectFileStatement));
 *     objectFileParser.parse("path/to/file.obj");
 * } catch(ObjectFileException e) {
 *     e.printStackTrace();
 * }
 * </code>
 * </pre>
 * This class is not thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class ObjectFileParser {
	private final List<ObjectFileParserObserver> objectFileParserObservers;
	private final List<ObjectFileStatementParser> objectFileStatementParsers;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code ObjectFileParser} instance.
	 */
	public ObjectFileParser() {
		this.objectFileParserObservers = new ArrayList<>();
		this.objectFileStatementParsers = new ArrayList<>();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Adds {@code objectFileParserObserver} to this {@code ObjectFileParser} instance.
	 * <p>
	 * If {@code objectFileParserObserver} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param objectFileParserObserver the {@link ObjectFileParserObserver} to add
	 * @throws NullPointerException thrown if, and only if, {@code objectFileParserObserver} is {@code null}
	 */
	public void addObjectFileParserObserver(final ObjectFileParserObserver objectFileParserObserver) {
		this.objectFileParserObservers.add(Objects.requireNonNull(objectFileParserObserver, "objectFileParserObserver == null"));
	}
	
	/**
	 * Adds {@code objectFileStatementParser} to this {@code ObjectFileParser} instance.
	 * <p>
	 * If {@code objectFileStatementParser} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param objectFileStatementParser the {@link ObjectFileStatementParser} to add
	 * @throws NullPointerException thrown if, and only if, {@code objectFileStatementParser} is {@code null}
	 */
	public void addObjectFileStatementParser(final ObjectFileStatementParser objectFileStatementParser) {
		this.objectFileStatementParsers.add(Objects.requireNonNull(objectFileStatementParser, "objectFileStatementParser == null"));
	}
	
	/**
	 * Parses a Wavefront Object file from the file represented by {@code file}.
	 * <p>
	 * If {@code file} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If the Wavefront Object file cannot be parsed, an {@link ObjectFileException} will be thrown.
	 * 
	 * @param file a {@code File} representing the file to parse from
	 * @throws NullPointerException thrown if, and only if, {@code file} is {@code null}
	 * @throws ObjectFileException thrown if, and only if, the Wavefront Object file cannot be parsed
	 */
	public void parse(final File file) throws ObjectFileException {
		try(final BufferedReader bufferedReader = new BufferedReader(new FileReader(Objects.requireNonNull(file, "file == null")))) {
			main:
			for(String line = bufferedReader.readLine(); line != null; line = bufferedReader.readLine()) {
				final TextLine textLine = new TextLine(line);
				
				if(textLine.isEmpty() || textLine.hasString(0, "#")) {
					continue;
				} else if(textLine.hasString(0, FaceStatement.NAME)) {
					doOnObjectFileStatement(doParseFaceStatement(textLine));
				} else if(textLine.hasString(0, GeometricVertexStatement.NAME)) {
					doOnObjectFileStatement(doParseGeometricVertexStatement(textLine));
				} else if(textLine.hasString(0, GroupStatement.NAME)) {
					doOnObjectFileStatement(doParseGroupStatement(textLine));
				} else if(textLine.hasString(0, MaterialLibraryStatement.NAME)) {
					doOnObjectFileStatement(doParseMaterialLibraryStatement(textLine));
				} else if(textLine.hasString(0, MaterialStatement.NAME)) {
					doOnObjectFileStatement(doParseMaterialStatement(textLine));
				} else if(textLine.hasString(0, ObjectStatement.NAME)) {
					doOnObjectFileStatement(doParseObjectStatement(textLine));
				} else if(textLine.hasString(0, SmoothingGroupStatement.NAME)) {
					doOnObjectFileStatement(doParseSmoothingGroupStatement(textLine));
				} else if(textLine.hasString(0, TextureVertexStatement.NAME)) {
					doOnObjectFileStatement(doParseTextureVertexStatement(textLine));
				} else if(textLine.hasString(0, VertexNormalStatement.NAME)) {
					doOnObjectFileStatement(doParseVertexNormalStatement(textLine));
				} else {
					for(final ObjectFileStatementParser objectFileStatementParser : this.objectFileStatementParsers) {
						final Optional<ObjectFileStatement> optionalObjectFileStatement = objectFileStatementParser.parse(textLine);
						
						if(optionalObjectFileStatement.isPresent()) {
							doOnObjectFileStatement(optionalObjectFileStatement.get());
							
							continue main;
						}
					}
					
					doOnObjectFileStatement(doParseUnsupportedObjectFileStatement(textLine));
				}
			}
		} catch(final IOException e) {
			throw new ObjectFileException(e);
		}
	}
	
	/**
	 * Parses a Wavefront Object file from the file represented by {@code filename}.
	 * <p>
	 * Calling this method is equivalent to {@code objectFileParser.parse(new File(filename))}, assuming {@code filename} is not {@code null}.
	 * <p>
	 * If {@code filename} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If the Wavefront Object file cannot be parsed, an {@link ObjectFileException} will be thrown.
	 * 
	 * @param filename a {@code String} representing the filename of the file to parse from
	 * @throws NullPointerException thrown if, and only if, {@code filename} is {@code null}
	 * @throws ObjectFileException thrown if, and only if, the Wavefront Object file cannot be parsed
	 */
	public void parse(final String filename) throws ObjectFileException {
		parse(new File(Objects.requireNonNull(filename, "filename == null")));
	}
	
	/**
	 * Removes {@code objectFileParserObserver} from this {@code ObjectFileParser} instance, if present.
	 * <p>
	 * If {@code objectFileParserObserver} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param objectFileParserObserver the {@link ObjectFileParserObserver} to remove
	 * @throws NullPointerException thrown if, and only if, {@code objectFileParserObserver} is {@code null}
	 */
	public void removeObjectFileParserObserver(final ObjectFileParserObserver objectFileParserObserver) {
		this.objectFileParserObservers.remove(Objects.requireNonNull(objectFileParserObserver, "objectFileParserObserver == null"));
	}
	
	/**
	 * Removes {@code objectFileStatementParser} from this {@code ObjectFileParser} instance, if present.
	 * <p>
	 * If {@code objectFileStatementParser} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param objectFileStatementParser the {@link ObjectFileStatementParser} to remove
	 * @throws NullPointerException thrown if, and only if, {@code objectFileStatementParser} is {@code null}
	 */
	public void removeObjectFileStatementParser(final ObjectFileStatementParser objectFileStatementParser) {
		this.objectFileStatementParsers.remove(Objects.requireNonNull(objectFileStatementParser, "objectFileStatementParser == null"));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private void doOnObjectFileStatement(final ObjectFileStatement objectFileStatement) {
		this.objectFileParserObservers.forEach(objectFileParserObserver -> objectFileParserObserver.onObjectFileStatement(objectFileStatement));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static FaceStatement doParseFaceStatement(final TextLine textLine) throws ObjectFileException {
		if(textLine.hasString(0, FaceStatement.NAME)) {
			final FaceStatement faceStatement = new FaceStatement();
			
			for(int i = 1; i < textLine.length(); i++) {
				final TextLine textLine0 = new TextLine(textLine.getString(i, null), "/");
				
				final OptionalInt geometricVertexIndex = textLine0.hasInt(0) ? OptionalInt.of(textLine0.getInt(0, 0)) : OptionalInt.empty();
				final OptionalInt textureVertexIndex = textLine0.hasInt(1) ? OptionalInt.of(textLine0.getInt(1, 0)) : OptionalInt.empty();
				final OptionalInt vertexNormalIndex = textLine0.hasInt(2) ? OptionalInt.of(textLine0.getInt(2, 0)) : OptionalInt.empty();
				
				faceStatement.addFaceElement(new FaceElement(geometricVertexIndex, textureVertexIndex, vertexNormalIndex));
			}
			
			return faceStatement;
		}
		
		throw new ObjectFileException(String.format("Unable to parse statement %s!", textLine));
	}
	
	private static GeometricVertexStatement doParseGeometricVertexStatement(final TextLine textLine) throws ObjectFileException {
		if(textLine.hasString(0, GeometricVertexStatement.NAME)) {
			final float x = textLine.getFloat(1, 0.0F);
			final float y = textLine.getFloat(2, 0.0F);
			final float z = textLine.getFloat(3, 0.0F);
			final float w = textLine.getFloat(4, 1.0F);
			
			return new GeometricVertexStatement(x, y, z, w);
		}
		
		throw new ObjectFileException(String.format("Unable to parse statement %s!", textLine));
	}
	
	private static GroupStatement doParseGroupStatement(final TextLine textLine) throws ObjectFileException {
		if(textLine.hasString(0, GroupStatement.NAME)) {
			final GroupStatement groupStatement = new GroupStatement();
			
			for(int i = 1; i < textLine.length(); i++) {
				groupStatement.addGroupName(textLine.getString(i, ""));
			}
			
			return groupStatement;
		}
		
		throw new ObjectFileException(String.format("Unable to parse statement %s!", textLine));
	}
	
	private static MaterialLibraryStatement doParseMaterialLibraryStatement(final TextLine textLine) throws ObjectFileException {
		if(textLine.hasString(0, MaterialLibraryStatement.NAME)) {
			final MaterialLibraryStatement materialLibraryStatement = new MaterialLibraryStatement();
			
			for(int i = 1; i < textLine.length(); i++) {
				materialLibraryStatement.addFilename(textLine.getString(i, ""));
			}
			
			return materialLibraryStatement;
		}
		
		throw new ObjectFileException(String.format("Unable to parse statement %s!", textLine));
	}
	
	private static MaterialStatement doParseMaterialStatement(final TextLine textLine) throws ObjectFileException {
		if(textLine.hasString(0, MaterialStatement.NAME)) {
			return new MaterialStatement(textLine.getString(1, ""));
		}
		
		throw new ObjectFileException(String.format("Unable to parse statement %s!", textLine));
	}
	
	private static ObjectStatement doParseObjectStatement(final TextLine textLine) throws ObjectFileException {
		if(textLine.hasString(0, ObjectStatement.NAME)) {
			return new ObjectStatement(textLine.getString(1, ""));
		}
		
		throw new ObjectFileException(String.format("Unable to parse statement %s!", textLine));
	}
	
	private static SmoothingGroupStatement doParseSmoothingGroupStatement(final TextLine textLine) throws ObjectFileException {
		if(textLine.hasString(0, SmoothingGroupStatement.NAME)) {
			return new SmoothingGroupStatement(textLine.getInt(1, 0));
		}
		
		throw new ObjectFileException(String.format("Unable to parse statement %s!", textLine));
	}
	
	private static TextureVertexStatement doParseTextureVertexStatement(final TextLine textLine) throws ObjectFileException {
		if(textLine.hasString(0, TextureVertexStatement.NAME)) {
			final float u = textLine.getFloat(1, 0.0F);
			final float v = textLine.getFloat(2, 0.0F);
			final float w = textLine.getFloat(3, 0.0F);
			
			return new TextureVertexStatement(u, v, w);
		}
		
		throw new ObjectFileException(String.format("Unable to parse statement %s!", textLine));
	}
	
	private static UnsupportedObjectFileStatement doParseUnsupportedObjectFileStatement(final TextLine textLine) {
		final List<String> tokens = textLine.getTokens();
		
		final UnsupportedObjectFileStatement unsupportedObjectFileStatement = new UnsupportedObjectFileStatement();
		
		for(final String token : tokens) {
			unsupportedObjectFileStatement.addToken(token);
		}
		
		return unsupportedObjectFileStatement;
	}
	
	private static VertexNormalStatement doParseVertexNormalStatement(final TextLine textLine) throws ObjectFileException {
		if(textLine.hasString(0, VertexNormalStatement.NAME)) {
			final float x = textLine.getFloat(1, 0.0F);
			final float y = textLine.getFloat(2, 0.0F);
			final float z = textLine.getFloat(3, 0.0F);
			
			return new VertexNormalStatement(x, y, z);
		}
		
		throw new ObjectFileException(String.format("Unable to parse statement %s!", textLine));
	}
}