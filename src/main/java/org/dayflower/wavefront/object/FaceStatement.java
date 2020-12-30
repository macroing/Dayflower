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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.OptionalInt;
import java.util.stream.Collectors;

/**
 * A {@code FaceStatement} represents a face statement ({@code "f"}) of a Wavefront Object file.
 * <p>
 * This class is not thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class FaceStatement implements ObjectFileStatement {
	/**
	 * The name of this {@code FaceStatement} which is {@code "f"}.
	 */
	public static final String NAME = "f";
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final List<FaceElement> faceElements;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new empty {@code FaceStatement} instance.
	 */
	public FaceStatement() {
		this.faceElements = new ArrayList<>();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code List} with all {@link FaceElement}s currently added to this {@code FaceStatement} instance.
	 * <p>
	 * Modifying the returned {@code List} will not affect this {@code FaceStatement} instance.
	 * 
	 * @return a {@code List} with all {@code FaceElement}s currently added to this {@code FaceStatement} instance
	 */
	public List<FaceElement> getFaceElements() {
		return new ArrayList<>(this.faceElements);
	}
	
	/**
	 * Returns the name of this {@code FaceStatement} instance.
	 * 
	 * @return the name of this {@code FaceStatement} instance
	 */
	@Override
	public String getName() {
		return NAME;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code FaceStatement} instance.
	 * 
	 * @return a {@code String} representation of this {@code FaceStatement} instance
	 */
	@Override
	public String toString() {
		return String.format("%s %s", getName(), this.faceElements.stream().map(faceElement -> faceElement.toString()).collect(Collectors.joining(" ")));
	}
	
	/**
	 * Compares {@code object} to this {@code FaceStatement} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code FaceStatement}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code FaceStatement} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code FaceStatement}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof FaceStatement)) {
			return false;
		} else if(!Objects.equals(getName(), FaceStatement.class.cast(object).getName())) {
			return false;
		} else if(!Objects.equals(getFaceElements(), FaceStatement.class.cast(object).getFaceElements())) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns a hash code for this {@code FaceStatement} instance.
	 * 
	 * @return a hash code for this {@code FaceStatement} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(getName(), getFaceElements());
	}
	
	/**
	 * Adds {@code faceElement} to this {@code FaceStatement} instance.
	 * <p>
	 * If {@code faceElement} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param faceElement the {@link FaceElement} to add
	 * @throws NullPointerException thrown if, and only if, {@code faceElement} is {@code null}
	 */
	public void addFaceElement(final FaceElement faceElement) {
		this.faceElements.add(Objects.requireNonNull(faceElement, "faceElement == null"));
	}
	
	/**
	 * Removes {@code faceElement} from this {@code FaceStatement} instance, if present.
	 * <p>
	 * If {@code faceElement} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param faceElement the {@link FaceElement} to remove
	 * @throws NullPointerException thrown if, and only if, {@code faceElement} is {@code null}
	 */
	public void removeFaceElement(final FaceElement faceElement) {
		this.faceElements.remove(Objects.requireNonNull(faceElement, "faceElement == null"));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * A {@code FaceElement} represents a face "element" of a Wavefront Object face statement ({@code "f"}).
	 * <p>
	 * This class is currently thread-safe.
	 * 
	 * @since 1.0.0
	 * @author J&#246;rgen Lundgren
	 */
	public static final class FaceElement {
		private final OptionalInt geometricVertexIndex;
		private final OptionalInt textureVertexIndex;
		private final OptionalInt vertexNormalIndex;
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		/**
		 * Constructs a new {@code FaceElement} instance.
		 * <p>
		 * If either {@code geometricVertexIndex}, {@code textureVertexIndex} or {@code vertexNormalIndex} are {@code null}, a {@code NullPointerException} will be thrown.
		 * 
		 * @param geometricVertexIndex an optional geometric vertex index
		 * @param textureVertexIndex an optional texture vertex index
		 * @param vertexNormalIndex an optional vertex normal index
		 * @throws NullPointerException thrown if, and only if, either {@code geometricVertexIndex}, {@code textureVertexIndex} or {@code vertexNormalIndex} are {@code null}
		 */
		public FaceElement(final OptionalInt geometricVertexIndex, final OptionalInt textureVertexIndex, final OptionalInt vertexNormalIndex) {
			this.geometricVertexIndex = Objects.requireNonNull(geometricVertexIndex, "geometricVertexIndex == null");
			this.textureVertexIndex = Objects.requireNonNull(textureVertexIndex, "textureVertexIndex == null");
			this.vertexNormalIndex = Objects.requireNonNull(vertexNormalIndex, "vertexNormalIndex == null");
		}
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		/**
		 * Returns the optional geometric vertex index.
		 * 
		 * @return the optional geometric vertex index
		 */
		public OptionalInt getGeometricVertexIndex() {
			return this.geometricVertexIndex;
		}
		
		/**
		 * Returns the optional texture vertex index.
		 * 
		 * @return the optional texture vertex index
		 */
		public OptionalInt getTextureVertexIndex() {
			return this.textureVertexIndex;
		}
		
		/**
		 * Returns the optional vertex normal index.
		 * 
		 * @return the optional vertex normal index
		 */
		public OptionalInt getVertexNormalIndex() {
			return this.vertexNormalIndex;
		}
		
		/**
		 * Returns a {@code String} representation of this {@code FaceElement} instance.
		 * 
		 * @return a {@code String} representation of this {@code FaceElement} instance
		 */
		@Override
		public String toString() {
			return String.format("%s/%s/%s", hasGeometricVertexIndex() ? Integer.toString(getGeometricVertexIndex().getAsInt()) : "", hasTextureVertexIndex() ? Integer.toString(getTextureVertexIndex().getAsInt()) : "", hasVertexNormalIndex() ? Integer.toString(getVertexNormalIndex().getAsInt()) : "");
		}
		
		/**
		 * Compares {@code object} to this {@code FaceElement} instance for equality.
		 * <p>
		 * Returns {@code true} if, and only if, {@code object} is an instance of {@code FaceElement}, and their respective values are equal, {@code false} otherwise.
		 * 
		 * @param object the {@code Object} to compare to this {@code FaceElement} instance for equality
		 * @return {@code true} if, and only if, {@code object} is an instance of {@code FaceElement}, and their respective values are equal, {@code false} otherwise
		 */
		@Override
		public boolean equals(final Object object) {
			if(object == this) {
				return true;
			} else if(!(object instanceof FaceElement)) {
				return false;
			} else if(!Objects.equals(getGeometricVertexIndex(), FaceElement.class.cast(object).getGeometricVertexIndex())) {
				return false;
			} else if(!Objects.equals(getTextureVertexIndex(), FaceElement.class.cast(object).getTextureVertexIndex())) {
				return false;
			} else if(!Objects.equals(getVertexNormalIndex(), FaceElement.class.cast(object).getVertexNormalIndex())) {
				return false;
			} else {
				return true;
			}
		}
		
		/**
		 * Returns {@code true} if, and only if, this {@code FaceElement} has a geometric vertex index, {@code false} otherwise.
		 * 
		 * @return {@code true} if, and only if, this {@code FaceElement} has a geometric vertex index, {@code false} otherwise
		 */
		public boolean hasGeometricVertexIndex() {
			return getGeometricVertexIndex().isPresent();
		}
		
		/**
		 * Returns {@code true} if, and only if, this {@code FaceElement} has a texture vertex index, {@code false} otherwise.
		 * 
		 * @return {@code true} if, and only if, this {@code FaceElement} has a texture vertex index, {@code false} otherwise
		 */
		public boolean hasTextureVertexIndex() {
			return getTextureVertexIndex().isPresent();
		}
		
		/**
		 * Returns {@code true} if, and only if, this {@code FaceElement} has a vertex normal index, {@code false} otherwise.
		 * 
		 * @return {@code true} if, and only if, this {@code FaceElement} has a vertex normal index, {@code false} otherwise
		 */
		public boolean hasVertexNormalIndex() {
			return getVertexNormalIndex().isPresent();
		}
		
		/**
		 * Returns a hash code for this {@code FaceElement} instance.
		 * 
		 * @return a hash code for this {@code FaceElement} instance
		 */
		@Override
		public int hashCode() {
			return Objects.hash(getGeometricVertexIndex(), getTextureVertexIndex(), getVertexNormalIndex());
		}
	}
}