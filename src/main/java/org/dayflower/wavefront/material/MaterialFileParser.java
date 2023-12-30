/**
 * Copyright 2014 - 2024 J&#246;rgen Lundgren
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.dayflower.utility.TextLine;

/**
 * A {@code MaterialFileParser} is used for parsing Wavefront Material files.
 * <p>
 * To use this class, consider the following example:
 * <pre>
 * <code>
 * try {
 *     MaterialFileParser materialFileParser = new MaterialFileParser();
 *     materialFileParser.addMaterialFileParserObserver(materialFileStatement -&gt; System.out.println(materialFileStatement));
 *     materialFileParser.parse("path/to/file.mtl");
 * } catch(MaterialFileException e) {
 *     e.printStackTrace();
 * }
 * </code>
 * </pre>
 * This class is not thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class MaterialFileParser {
	private final List<MaterialFileParserObserver> materialFileParserObservers;
	private final List<MaterialFileStatementParser> materialFileStatementParsers;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code MaterialFileParser} instance.
	 */
	public MaterialFileParser() {
		this.materialFileParserObservers = new ArrayList<>();
		this.materialFileStatementParsers = new ArrayList<>();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Adds {@code materialFileParserObserver} to this {@code MaterialFileParser} instance.
	 * <p>
	 * If {@code materialFileParserObserver} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param materialFileParserObserver the {@link MaterialFileParserObserver} to add
	 * @throws NullPointerException thrown if, and only if, {@code materialFileParserObserver} is {@code null}
	 */
	public void addMaterialFileParserObserver(final MaterialFileParserObserver materialFileParserObserver) {
		this.materialFileParserObservers.add(Objects.requireNonNull(materialFileParserObserver, "materialFileParserObserver == null"));
	}
	
	/**
	 * Adds {@code materialFileStatementParser} to this {@code MaterialFileParser} instance.
	 * <p>
	 * If {@code materialFileStatementParser} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param materialFileStatementParser the {@link MaterialFileStatementParser} to add
	 * @throws NullPointerException thrown if, and only if, {@code materialFileStatementParser} is {@code null}
	 */
	public void addMaterialFileStatementParser(final MaterialFileStatementParser materialFileStatementParser) {
		this.materialFileStatementParsers.add(Objects.requireNonNull(materialFileStatementParser, "materialFileStatementParser == null"));
	}
	
	/**
	 * Parses a Wavefront Material file from the file represented by {@code file}.
	 * <p>
	 * If {@code file} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If the Wavefront Material file cannot be parsed, a {@link MaterialFileException} will be thrown.
	 * 
	 * @param file a {@code File} representing the file to parse from
	 * @throws MaterialFileException thrown if, and only if, the Wavefront Material file cannot be parsed
	 * @throws NullPointerException thrown if, and only if, {@code file} is {@code null}
	 */
	public void parse(final File file) throws MaterialFileException {
		try(final BufferedReader bufferedReader = new BufferedReader(new FileReader(Objects.requireNonNull(file, "file == null")))) {
			main:
			for(String line = bufferedReader.readLine(); line != null; line = bufferedReader.readLine()) {
				final TextLine textLine = new TextLine(line);
				
				if(textLine.isEmpty() || textLine.hasString(0, "#")) {
					continue;
				} else if(textLine.hasString(0, AmbientReflectivityStatement.NAME)) {
					doOnMaterialFileStatement(doParseAmbientStatement(textLine));
				} else if(textLine.hasString(0, DiffuseReflectivityStatement.NAME)) {
					doOnMaterialFileStatement(doParseDiffuseStatement(textLine));
				} else if(textLine.hasString(0, DissolveStatement.NAME)) {
					doOnMaterialFileStatement(doParseDissolveStatement(textLine));
				} else if(textLine.hasString(0, EmissiveStatement.NAME)) {
					doOnMaterialFileStatement(doParseEmissiveStatement(textLine));
				} else if(textLine.hasString(0, IlluminationModelStatement.NAME)) {
					doOnMaterialFileStatement(doParseIlluminationModelStatement(textLine));
				} else if(textLine.hasString(0, MaterialDescriptionStatement.NAME)) {
					doOnMaterialFileStatement(doParseMaterialDescriptionStatement(textLine));
				} else if(textLine.hasString(0, OpticalDensityStatement.NAME)) {
					doOnMaterialFileStatement(doParseOpticalDensityStatement(textLine));
				} else if(textLine.hasString(0, SpecularExponentStatement.NAME)) {
					doOnMaterialFileStatement(doParseSpecularExponentStatement(textLine));
				} else if(textLine.hasString(0, SpecularReflectivityStatement.NAME)) {
					doOnMaterialFileStatement(doParseSpecularStatement(textLine));
				} else if(textLine.hasString(0, TransmissionFilterStatement.NAME)) {
					doOnMaterialFileStatement(doParseTransmissionFilterStatement(textLine));
				} else if(textLine.hasString(0, TransparencyStatement.NAME)) {
					doOnMaterialFileStatement(doParseTransparencyStatement(textLine));
				} else {
					for(final MaterialFileStatementParser materialFileStatementParser : this.materialFileStatementParsers) {
						final Optional<MaterialFileStatement> optionalMaterialFileStatement = materialFileStatementParser.parse(textLine);
						
						if(optionalMaterialFileStatement.isPresent()) {
							doOnMaterialFileStatement(optionalMaterialFileStatement.get());
							
							continue main;
						}
					}
					
					doOnMaterialFileStatement(doParseUnsupportedMaterialFileStatement(textLine));
				}
			}
		} catch(final IOException e) {
			throw new MaterialFileException(e);
		}
	}
	
	/**
	 * Parses a Wavefront Material file from the file represented by {@code filename}.
	 * <p>
	 * Calling this method is equivalent to {@code materialFileParser.parse(new File(filename))}, assuming {@code filename} is not {@code null}.
	 * <p>
	 * If {@code filename} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If the Wavefront Material file cannot be parsed, a {@link MaterialFileException} will be thrown.
	 * 
	 * @param filename a {@code String} representing the filename of the file to parse from
	 * @throws MaterialFileException thrown if, and only if, the Wavefront Material file cannot be parsed
	 * @throws NullPointerException thrown if, and only if, {@code filename} is {@code null}
	 */
	public void parse(final String filename) throws MaterialFileException {
		parse(new File(Objects.requireNonNull(filename, "filename == null")));
	}
	
	/**
	 * Removes {@code materialFileParserObserver} from this {@code MaterialFileParser} instance, if present.
	 * <p>
	 * If {@code materialFileParserObserver} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param materialFileParserObserver the {@link MaterialFileParserObserver} to remove
	 * @throws NullPointerException thrown if, and only if, {@code materialFileParserObserver} is {@code null}
	 */
	public void removeMaterialFileParserObserver(final MaterialFileParserObserver materialFileParserObserver) {
		this.materialFileParserObservers.remove(Objects.requireNonNull(materialFileParserObserver, "materialFileParserObserver == null"));
	}
	
	/**
	 * Removes {@code materialFileStatementParser} from this {@code MaterialFileParser} instance, if present.
	 * <p>
	 * If {@code materialFileStatementParser} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param materialFileStatementParser the {@link MaterialFileStatementParser} to remove
	 * @throws NullPointerException thrown if, and only if, {@code materialFileStatementParser} is {@code null}
	 */
	public void removeMaterialFileStatementParser(final MaterialFileStatementParser materialFileStatementParser) {
		this.materialFileStatementParsers.remove(Objects.requireNonNull(materialFileStatementParser, "materialFileStatementParser == null"));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private void doOnMaterialFileStatement(final MaterialFileStatement materialFileStatement) {
		this.materialFileParserObservers.forEach(materialFileParserObserver -> materialFileParserObserver.onMaterialFileStatement(materialFileStatement));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static AmbientReflectivityStatement doParseAmbientStatement(final TextLine textLine) throws MaterialFileException {
		if(textLine.hasString(0, AmbientReflectivityStatement.NAME)) {
			if(textLine.hasString(1, SpectralAmbientReflectivityStatement.NAME)) {
				final String filename = textLine.getString(2, "");
				
				final float factor = textLine.getFloat(3, 1.0F);
				
				return new SpectralAmbientReflectivityStatement(filename, factor);
			} else if(textLine.hasString(1, XYZAmbientReflectivityStatement.NAME)) {
				final float x = textLine.getFloat(2, 0.0F);
				final float y = textLine.getFloat(3, x);
				final float z = textLine.getFloat(4, x);
				
				return new XYZAmbientReflectivityStatement(x, y, z);
			} else {
				final float r = textLine.getFloat(1, 0.0F);
				final float g = textLine.getFloat(2, r);
				final float b = textLine.getFloat(3, r);
				
				return new RGBAmbientReflectivityStatement(r, g, b);
			}
		}
		
		throw new MaterialFileException(String.format("Unable to parse statement %s!", textLine));
	}
	
	private static DiffuseReflectivityStatement doParseDiffuseStatement(final TextLine textLine) throws MaterialFileException {
		if(textLine.hasString(0, DiffuseReflectivityStatement.NAME)) {
			if(textLine.hasString(1, SpectralDiffuseReflectivityStatement.NAME)) {
				final String filename = textLine.getString(2, "");
				
				final float factor = textLine.getFloat(3, 1.0F);
				
				return new SpectralDiffuseReflectivityStatement(filename, factor);
			} else if(textLine.hasString(1, XYZDiffuseReflectivityStatement.NAME)) {
				final float x = textLine.getFloat(2, 0.0F);
				final float y = textLine.getFloat(3, x);
				final float z = textLine.getFloat(4, x);
				
				return new XYZDiffuseReflectivityStatement(x, y, z);
			} else {
				final float r = textLine.getFloat(1, 0.0F);
				final float g = textLine.getFloat(2, r);
				final float b = textLine.getFloat(3, r);
				
				return new RGBDiffuseReflectivityStatement(r, g, b);
			}
		}
		
		throw new MaterialFileException(String.format("Unable to parse statement %s!", textLine));
	}
	
	private static DissolveStatement doParseDissolveStatement(final TextLine textLine) throws MaterialFileException {
		if(textLine.hasString(0, DissolveStatement.NAME)) {
			final boolean hasHalo = textLine.hasString(1, "-halo");
			
			final float factor = textLine.getFloat(hasHalo ? 2 : 1, 1.0F);
			
			return new DissolveStatement(factor, hasHalo);
		}
		
		throw new MaterialFileException(String.format("Unable to parse statement %s!", textLine));
	}
	
	private static EmissiveStatement doParseEmissiveStatement(final TextLine textLine) throws MaterialFileException {
		if(textLine.hasString(0, EmissiveStatement.NAME)) {
			if(textLine.hasString(1, SpectralEmissiveStatement.NAME)) {
				final String filename = textLine.getString(2, "");
				
				final float factor = textLine.getFloat(3, 1.0F);
				
				return new SpectralEmissiveStatement(filename, factor);
			} else if(textLine.hasString(1, XYZEmissiveStatement.NAME)) {
				final float x = textLine.getFloat(2, 0.0F);
				final float y = textLine.getFloat(3, x);
				final float z = textLine.getFloat(4, x);
				
				return new XYZEmissiveStatement(x, y, z);
			} else {
				final float r = textLine.getFloat(1, 0.0F);
				final float g = textLine.getFloat(2, r);
				final float b = textLine.getFloat(3, r);
				
				return new RGBEmissiveStatement(r, g, b);
			}
		}
		
		throw new MaterialFileException(String.format("Unable to parse statement %s!", textLine));
	}
	
	private static IlluminationModelStatement doParseIlluminationModelStatement(final TextLine textLine) throws MaterialFileException {
		if(textLine.hasString(0, IlluminationModelStatement.NAME)) {
			final int number = textLine.getInt(1, 0);
			
			return new IlluminationModelStatement(number);
		}
		
		throw new MaterialFileException(String.format("Unable to parse statement %s!", textLine));
	}
	
	private static MaterialDescriptionStatement doParseMaterialDescriptionStatement(final TextLine textLine) throws MaterialFileException {
		if(textLine.hasString(0, MaterialDescriptionStatement.NAME)) {
			final String materialName = textLine.getString(1, "");
			
			return new MaterialDescriptionStatement(materialName);
		}
		
		throw new MaterialFileException(String.format("Unable to parse statement %s!", textLine));
	}
	
	private static OpticalDensityStatement doParseOpticalDensityStatement(final TextLine textLine) throws MaterialFileException {
		if(textLine.hasString(0, OpticalDensityStatement.NAME)) {
			final float opticalDensity = textLine.getFloat(1, 1.0F);
			
			return new OpticalDensityStatement(opticalDensity);
		}
		
		throw new MaterialFileException(String.format("Unable to parse statement %s!", textLine));
	}
	
	private static SpecularExponentStatement doParseSpecularExponentStatement(final TextLine textLine) throws MaterialFileException {
		if(textLine.hasString(0, SpecularExponentStatement.NAME)) {
			final float exponent = textLine.getFloat(1, 0.0F);
			
			return new SpecularExponentStatement(exponent);
		}
		
		throw new MaterialFileException(String.format("Unable to parse statement %s!", textLine));
	}
	
	private static SpecularReflectivityStatement doParseSpecularStatement(final TextLine textLine) throws MaterialFileException {
		if(textLine.hasString(0, SpecularReflectivityStatement.NAME)) {
			if(textLine.hasString(1, SpectralSpecularReflectivityStatement.NAME)) {
				final String filename = textLine.getString(2, "");
				
				final float factor = textLine.getFloat(3, 1.0F);
				
				return new SpectralSpecularReflectivityStatement(filename, factor);
			} else if(textLine.hasString(1, XYZSpecularReflectivityStatement.NAME)) {
				final float x = textLine.getFloat(2, 0.0F);
				final float y = textLine.getFloat(3, x);
				final float z = textLine.getFloat(4, x);
				
				return new XYZSpecularReflectivityStatement(x, y, z);
			} else {
				final float r = textLine.getFloat(1, 0.0F);
				final float g = textLine.getFloat(2, r);
				final float b = textLine.getFloat(3, r);
				
				return new RGBSpecularReflectivityStatement(r, g, b);
			}
		}
		
		throw new MaterialFileException(String.format("Unable to parse statement %s!", textLine));
	}
	
	private static TransmissionFilterStatement doParseTransmissionFilterStatement(final TextLine textLine) throws MaterialFileException {
		if(textLine.hasString(0, TransmissionFilterStatement.NAME)) {
			if(textLine.hasString(1, SpectralTransmissionFilterStatement.NAME)) {
				final String filename = textLine.getString(2, "");
				
				final float factor = textLine.getFloat(3, 1.0F);
				
				return new SpectralTransmissionFilterStatement(filename, factor);
			} else if(textLine.hasString(1, XYZTransmissionFilterStatement.NAME)) {
				final float x = textLine.getFloat(2, 0.0F);
				final float y = textLine.getFloat(3, x);
				final float z = textLine.getFloat(4, x);
				
				return new XYZTransmissionFilterStatement(x, y, z);
			} else {
				final float r = textLine.getFloat(1, 0.0F);
				final float g = textLine.getFloat(2, r);
				final float b = textLine.getFloat(3, r);
				
				return new RGBTransmissionFilterStatement(r, g, b);
			}
		}
		
		throw new MaterialFileException(String.format("Unable to parse statement %s!", textLine));
	}
	
	private static TransparencyStatement doParseTransparencyStatement(final TextLine textLine) throws MaterialFileException {
		if(textLine.hasString(0, TransparencyStatement.NAME)) {
			final float factor = textLine.getFloat(1, 1.0F);
			
			return new TransparencyStatement(factor);
		}
		
		throw new MaterialFileException(String.format("Unable to parse statement %s!", textLine));
	}
	
	private static UnsupportedMaterialFileStatement doParseUnsupportedMaterialFileStatement(final TextLine textLine) {
		final List<String> tokens = textLine.getTokens();
		
		final UnsupportedMaterialFileStatement unsupportedMaterialFileStatement = new UnsupportedMaterialFileStatement();
		
		for(final String token : tokens) {
			unsupportedMaterialFileStatement.addToken(token);
		}
		
		return unsupportedMaterialFileStatement;
	}
}