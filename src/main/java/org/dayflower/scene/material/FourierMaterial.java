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
package org.dayflower.scene.material;

import java.util.Objects;

import org.dayflower.color.Color3F;
import org.dayflower.scene.BSDF;
import org.dayflower.scene.Intersection;
import org.dayflower.scene.Material;
import org.dayflower.scene.ScatteringFunctions;
import org.dayflower.scene.TransportMode;
import org.dayflower.scene.bxdf.FourierBXDF;
import org.dayflower.scene.bxdf.FourierBXDFTable;
import org.dayflower.scene.modifier.Modifier;
import org.dayflower.scene.texture.Texture;

import org.macroing.java.util.visitor.NodeHierarchicalVisitor;
import org.macroing.java.util.visitor.NodeTraversalException;

/**
 * A {@code FourierMaterial} is an implementation of {@link Material} that represents a Fourier material.
 * <p>
 * This class is immutable and thread-safe as long as the {@link Modifier} instance and the {@link Texture} instance are.
 * <p>
 * This {@code Material} implementation is not supported on the GPU.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class FourierMaterial implements Material {
	/**
	 * The name of this {@code FourierMaterial} class.
	 */
	public static final String NAME = "Fourier";
	
	/**
	 * The ID of this {@code FourierMaterial} class.
	 */
	public static final int ID = 5;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final FourierBXDFTable fourierBXDFTable;
	private final Modifier modifier;
	private final String filename;
	private final Texture textureEmission;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code FourierMaterial} instance.
	 * <p>
	 * If either {@code filename}, {@code textureEmission} or {@code modifier} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * 
	 * @param filename the filename of a BSDF file to read
	 * @param textureEmission a {@code Texture} instance for emission
	 * @param modifier a {@link Modifier} instance
	 * @throws NullPointerException thrown if, and only if, either {@code filename}, {@code textureEmission} or {@code modifier} are {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
	public FourierMaterial(final String filename, final Texture textureEmission, final Modifier modifier) {
		this.fourierBXDFTable = new FourierBXDFTable();
		this.fourierBXDFTable.read(Objects.requireNonNull(filename, "filename == null"));
		this.filename = filename;
		this.textureEmission = Objects.requireNonNull(textureEmission, "textureEmission == null");
		this.modifier = Objects.requireNonNull(modifier, "modifier == null");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link Color3F} instance with the emittance of this {@code FourierMaterial} instance at {@code intersection}.
	 * <p>
	 * If {@code intersection} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param intersection an {@link Intersection} instance
	 * @return a {@code Color3F} instance with the emittance of this {@code FourierMaterial} instance at {@code intersection}
	 * @throws NullPointerException thrown if, and only if, {@code intersection} is {@code null}
	 */
	@Override
	public Color3F emittance(final Intersection intersection) {
		Objects.requireNonNull(intersection, "intersection == null");
		
		return this.textureEmission.getColor(intersection);
	}
	
	/**
	 * Computes the {@link ScatteringFunctions} at {@code intersection}.
	 * <p>
	 * Returns a {@code ScatteringFunctions} instance.
	 * <p>
	 * If either {@code intersection} or {@code transportMode} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param intersection the {@link Intersection} to compute the {@code ScatteringFunctions} for
	 * @param transportMode the {@link TransportMode} to use
	 * @param isAllowingMultipleLobes {@code true} if, and only if, multiple lobes are allowed, {@code false} otherwise
	 * @return a {@code ScatteringFunctions} instance
	 * @throws NullPointerException thrown if, and only if, either {@code intersection} or {@code transportMode} are {@code null}
	 */
	@Override
	public ScatteringFunctions computeScatteringFunctions(final Intersection intersection, final TransportMode transportMode, final boolean isAllowingMultipleLobes) {
		Objects.requireNonNull(intersection, "intersection == null");
		Objects.requireNonNull(transportMode, "transportMode == null");
		
		this.modifier.modify(intersection);
		
		if(this.fourierBXDFTable.getNChannels() > 0) {
			return new ScatteringFunctions(new BSDF(intersection, new FourierBXDF(this.fourierBXDFTable, transportMode)));
		}
		
		return new ScatteringFunctions();
	}
	
	/**
	 * Returns a {@code String} with the name of this {@code FourierMaterial} instance.
	 * 
	 * @return a {@code String} with the name of this {@code FourierMaterial} instance
	 */
	@Override
	public String getName() {
		return NAME;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code FourierMaterial} instance.
	 * 
	 * @return a {@code String} representation of this {@code FourierMaterial} instance
	 */
	@Override
	public String toString() {
		return String.format("new FourierMaterial(\"%s\", %s, %s)", this.filename, this.textureEmission, this.modifier);
	}
	
	/**
	 * Accepts a {@link NodeHierarchicalVisitor}.
	 * <p>
	 * Returns the result of {@code nodeHierarchicalVisitor.visitLeave(this)}.
	 * <p>
	 * If {@code nodeHierarchicalVisitor} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If a {@code RuntimeException} is thrown by the current {@code NodeHierarchicalVisitor}, a {@code NodeTraversalException} will be thrown with the {@code RuntimeException} wrapped.
	 * <p>
	 * This implementation will:
	 * <ul>
	 * <li>throw a {@code NullPointerException} if {@code nodeHierarchicalVisitor} is {@code null}.</li>
	 * <li>throw a {@code NodeTraversalException} if {@code nodeHierarchicalVisitor} throws a {@code RuntimeException}.</li>
	 * <li>traverse its child {@code Node} instances.</li>
	 * </ul>
	 * 
	 * @param nodeHierarchicalVisitor the {@code NodeHierarchicalVisitor} to accept
	 * @return the result of {@code nodeHierarchicalVisitor.visitLeave(this)}
	 * @throws NodeTraversalException thrown if, and only if, a {@code RuntimeException} is thrown by the current {@code NodeHierarchicalVisitor}
	 * @throws NullPointerException thrown if, and only if, {@code nodeHierarchicalVisitor} is {@code null}
	 */
	@Override
	public boolean accept(final NodeHierarchicalVisitor nodeHierarchicalVisitor) {
		Objects.requireNonNull(nodeHierarchicalVisitor, "nodeHierarchicalVisitor == null");
		
		try {
			if(nodeHierarchicalVisitor.visitEnter(this)) {
				if(!this.modifier.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
				
				if(!this.textureEmission.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
			}
			
			return nodeHierarchicalVisitor.visitLeave(this);
		} catch(final RuntimeException e) {
			throw new NodeTraversalException(e);
		}
	}
	
	/**
	 * Compares {@code object} to this {@code FourierMaterial} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code FourierMaterial}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code FourierMaterial} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code FourierMaterial}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof FourierMaterial)) {
			return false;
		} else if(!Objects.equals(this.fourierBXDFTable, FourierMaterial.class.cast(object).fourierBXDFTable)) {
			return false;
		} else if(!Objects.equals(this.modifier, FourierMaterial.class.cast(object).modifier)) {
			return false;
		} else if(!Objects.equals(this.filename, FourierMaterial.class.cast(object).filename)) {
			return false;
		} else if(!Objects.equals(this.textureEmission, FourierMaterial.class.cast(object).textureEmission)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns an {@code int} with the ID of this {@code FourierMaterial} instance.
	 * 
	 * @return an {@code int} with the ID of this {@code FourierMaterial} instance
	 */
	@Override
	public int getID() {
		return ID;
	}
	
	/**
	 * Returns a hash code for this {@code FourierMaterial} instance.
	 * 
	 * @return a hash code for this {@code FourierMaterial} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.fourierBXDFTable, this.modifier, this.filename, this.textureEmission);
	}
}