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
package org.dayflower.scene.material;

import static org.dayflower.utility.Floats.cos;
import static org.dayflower.utility.Floats.fractionalPart;
import static org.dayflower.utility.Floats.sin;

import java.util.Objects;

import org.dayflower.color.Color3F;
import org.dayflower.geometry.AngleF;
import org.dayflower.geometry.Vector2F;
import org.dayflower.scene.Intersection;
import org.dayflower.scene.Material;
import org.dayflower.scene.ScatteringFunctions;
import org.dayflower.scene.TransportMode;

import org.macroing.java.util.visitor.NodeHierarchicalVisitor;
import org.macroing.java.util.visitor.NodeTraversalException;

/**
 * A {@code CheckerboardMaterial} is an implementation of {@link Material} that alternates between two other {@code Material} instances in a checkerboard pattern.
 * <p>
 * This class is immutable and thread-safe as long as its two associated {@link Material} instances are.
 * <p>
 * This {@code Material} implementation is supported on the GPU.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class CheckerboardMaterial implements Material {
	/**
	 * The name of this {@code CheckerboardMaterial} class.
	 */
	public static final String NAME = "Checkerboard";
	
	/**
	 * The ID of this {@code CheckerboardMaterial} class.
	 */
	public static final int ID = 2;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final AngleF angle;
	private final Material materialA;
	private final Material materialB;
	private final Vector2F scale;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code CheckerboardMaterial} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new CheckerboardMaterial(new MatteMaterial(Color3F.GRAY), new MatteMaterial(Color3F.WHITE));
	 * }
	 * </pre>
	 */
	public CheckerboardMaterial() {
		this(new MatteMaterial(Color3F.GRAY), new MatteMaterial(Color3F.WHITE));
	}
	
	/**
	 * Constructs a new {@code CheckerboardMaterial} instance.
	 * <p>
	 * If either {@code materialA} or {@code materialB} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new CheckerboardMaterial(materialA, materialB, AngleF.degrees(0.0F));
	 * }
	 * </pre>
	 * 
	 * @param materialA the {@link Material} instance that is denoted by {@code A} and is used as the first checkerboard pattern component
	 * @param materialB the {@code Material} instance that is denoted by {@code B} and is used as the second checkerboard pattern component
	 * @throws NullPointerException thrown if, and only if, either {@code materialA} or {@code materialB} are {@code null}
	 */
	public CheckerboardMaterial(final Material materialA, final Material materialB) {
		this(materialA, materialB, AngleF.degrees(0.0F));
	}
	
	/**
	 * Constructs a new {@code CheckerboardMaterial} instance.
	 * <p>
	 * If either {@code materialA}, {@code materialB} or {@code angle} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new CheckerboardMaterial(materialA, materialB, angle, new Vector2F(1.0F, 1.0F));
	 * }
	 * </pre>
	 * 
	 * @param materialA the {@link Material} instance that is denoted by {@code A} and is used as the first checkerboard pattern component
	 * @param materialB the {@code Material} instance that is denoted by {@code B} and is used as the second checkerboard pattern component
	 * @param angle the {@link AngleF} instance that is used to rotate the checkerboard pattern
	 * @throws NullPointerException thrown if, and only if, either {@code materialA}, {@code materialB} or {@code angle} are {@code null}
	 */
	public CheckerboardMaterial(final Material materialA, final Material materialB, final AngleF angle) {
		this(materialA, materialB, angle, new Vector2F(1.0F, 1.0F));
	}
	
	/**
	 * Constructs a new {@code CheckerboardMaterial} instance.
	 * <p>
	 * If either {@code materialA}, {@code materialB}, {@code angle} or {@code scale} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param materialA the {@link Material} instance that is denoted by {@code A} and is used as the first checkerboard pattern component
	 * @param materialB the {@code Material} instance that is denoted by {@code B} and is used as the second checkerboard pattern component
	 * @param angle the {@link AngleF} instance that is used to rotate the checkerboard pattern
	 * @param scale the {@link Vector2F} instance that is used to scale the checkerboard pattern
	 * @throws NullPointerException thrown if, and only if, either {@code materialA}, {@code materialB}, {@code angle} or {@code scale} are {@code null}
	 */
	public CheckerboardMaterial(final Material materialA, final Material materialB, final AngleF angle, final Vector2F scale) {
		this.materialA = Objects.requireNonNull(materialA, "materialA == null");
		this.materialB = Objects.requireNonNull(materialB, "materialB == null");
		this.angle = Objects.requireNonNull(angle, "angle == null");
		this.scale = Objects.requireNonNull(scale, "scale == null");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the {@link AngleF} instance that is used to rotate the checkerboard pattern.
	 * 
	 * @return the {@code AngleF} instance that is used to rotate the checkerboard pattern
	 */
	public AngleF getAngle() {
		return this.angle;
	}
	
	/**
	 * Returns a {@link Color3F} instance with the emittance of this {@code CheckerboardMaterial} instance at {@code intersection}.
	 * <p>
	 * If {@code intersection} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param intersection an {@link Intersection} instance
	 * @return a {@code Color3F} instance with the emittance of this {@code CheckerboardMaterial} instance at {@code intersection}
	 * @throws NullPointerException thrown if, and only if, {@code intersection} is {@code null}
	 */
	@Override
	public Color3F emittance(final Intersection intersection) {
		Objects.requireNonNull(intersection, "intersection == null");
		
		final Material material = doGetMaterial(intersection);
		
		final Color3F emittance = material.emittance(intersection);
		
		return emittance;
	}
	
	/**
	 * Returns the {@link Material} instance that is denoted by {@code A} and is used as the first checkerboard pattern component.
	 * 
	 * @return the {@code Material} instance that is denoted by {@code A} and is used as the first checkerboard pattern component
	 */
	public Material getMaterialA() {
		return this.materialA;
	}
	
	/**
	 * Returns the {@link Material} instance that is denoted by {@code B} and is used as the second checkerboard pattern component.
	 * 
	 * @return the {@code Material} instance that is denoted by {@code B} and is used as the second checkerboard pattern component
	 */
	public Material getMaterialB() {
		return this.materialB;
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
		
		final Material material = doGetMaterial(intersection);
		
		final ScatteringFunctions scatteringFunctions = material.computeScatteringFunctions(intersection, transportMode, isAllowingMultipleLobes);
		
		return scatteringFunctions;
	}
	
	/**
	 * Returns a {@code String} with the name of this {@code CheckerboardMaterial} instance.
	 * 
	 * @return a {@code String} with the name of this {@code CheckerboardMaterial} instance
	 */
	@Override
	public String getName() {
		return NAME;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code CheckerboardMaterial} instance.
	 * 
	 * @return a {@code String} representation of this {@code CheckerboardMaterial} instance
	 */
	@Override
	public String toString() {
		return String.format("new CheckerboardMaterial(%s, %s, %s, %s)", this.materialA, this.materialB, this.angle, this.scale);
	}
	
	/**
	 * Returns the {@link Vector2F} instance that is used to scale the checkerboard pattern.
	 * 
	 * @return the {@code Vector2F} instance that is used to scale the checkerboard pattern
	 */
	public Vector2F getScale() {
		return this.scale;
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
				if(!this.materialA.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
				
				if(!this.materialB.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
				
				if(!this.scale.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
			}
			
			return nodeHierarchicalVisitor.visitLeave(this);
		} catch(final RuntimeException e) {
			throw new NodeTraversalException(e);
		}
	}
	
	/**
	 * Compares {@code object} to this {@code CheckerboardMaterial} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code CheckerboardMaterial}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code CheckerboardMaterial} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code CheckerboardMaterial}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof CheckerboardMaterial)) {
			return false;
		} else if(!Objects.equals(this.angle, CheckerboardMaterial.class.cast(object).angle)) {
			return false;
		} else if(!Objects.equals(this.materialA, CheckerboardMaterial.class.cast(object).materialA)) {
			return false;
		} else if(!Objects.equals(this.materialB, CheckerboardMaterial.class.cast(object).materialB)) {
			return false;
		} else if(!Objects.equals(this.scale, CheckerboardMaterial.class.cast(object).scale)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns an {@code int} with the ID of this {@code CheckerboardMaterial} instance.
	 * 
	 * @return an {@code int} with the ID of this {@code CheckerboardMaterial} instance
	 */
	@Override
	public int getID() {
		return ID;
	}
	
	/**
	 * Returns a hash code for this {@code CheckerboardMaterial} instance.
	 * 
	 * @return a hash code for this {@code CheckerboardMaterial} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.angle, this.materialA, this.materialB, this.scale);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * A {@code Builder} is used to build {@link CheckerboardMaterial} instances.
	 * <p>
	 * This class is mutable and not thread-safe.
	 * 
	 * @since 1.0.0
	 * @author J&#246;rgen Lundgren
	 */
	public static final class Builder {
		private AngleF angle;
		private Material materialA;
		private Material materialB;
		private Vector2F scale;
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		/**
		 * Constructs a new {@code Builder} instance.
		 */
		public Builder() {
			this(new CheckerboardMaterial());
		}
		
		/**
		 * Constructs a new {@code Builder} instance given {@code checkerboardMaterial}.
		 * <p>
		 * If {@code checkerboardMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
		 * 
		 * @param checkerboardMaterial a {@link CheckerboardMaterial} instance
		 * @throws NullPointerException thrown if, and only if, {@code checkerboardMaterial} is {@code null}
		 */
		public Builder(final CheckerboardMaterial checkerboardMaterial) {
			this.angle = checkerboardMaterial.getAngle();
			this.materialA = checkerboardMaterial.getMaterialA();
			this.materialB = checkerboardMaterial.getMaterialB();
			this.scale = checkerboardMaterial.getScale();
		}
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		/**
		 * Sets the {@link AngleF} instance that is used to rotate the checkerboard pattern.
		 * <p>
		 * Returns this {@code Builder} instance.
		 * <p>
		 * If {@code angle} is {@code null}, a {@code NullPointerException} will be thrown.
		 * 
		 * @param angle the {@code AngleF} instance that is used to rotate the checkerboard pattern
		 * @return this {@code Builder} instance
		 * @throws NullPointerException thrown if, and only if, {@code angle} is {@code null}
		 */
		public Builder setAngle(final AngleF angle) {
			this.angle = Objects.requireNonNull(angle, "angle == null");
			
			return this;
		}
		
		/**
		 * Sets the {@link Material} instance that is denoted by {@code A} and is used as the first checkerboard pattern component.
		 * <p>
		 * Returns this {@code Builder} instance.
		 * <p>
		 * If {@code materialA} is {@code null}, a {@code NullPointerException} will be thrown.
		 * 
		 * @param materialA the {@code Material} instance that is denoted by {@code A} and is used as the first checkerboard pattern component
		 * @return this {@code Builder} instance
		 * @throws NullPointerException thrown if, and only if, {@code materialA} is {@code null}
		 */
		public Builder setMaterialA(final Material materialA) {
			this.materialA = Objects.requireNonNull(materialA, "materialA == null");
			
			return this;
		}
		
		/**
		 * Sets the {@link Material} instance that is denoted by {@code B} and is used as the second checkerboard pattern component.
		 * <p>
		 * Returns this {@code Builder} instance.
		 * <p>
		 * If {@code materialB} is {@code null}, a {@code NullPointerException} will be thrown.
		 * 
		 * @param materialB the {@code Material} instance that is denoted by {@code B} and is used as the second checkerboard pattern component
		 * @return this {@code Builder} instance
		 * @throws NullPointerException thrown if, and only if, {@code materialB} is {@code null}
		 */
		public Builder setMaterialB(final Material materialB) {
			this.materialB = Objects.requireNonNull(materialB, "materialB == null");
			
			return this;
		}
		
		/**
		 * Sets the {@link Vector2F} instance that is used to scale the checkerboard pattern.
		 * <p>
		 * Returns this {@code Builder} instance.
		 * <p>
		 * If {@code scale} is {@code null}, a {@code NullPointerException} will be thrown.
		 * 
		 * @param scale the {@code Vector2F} instance that is used to scale the checkerboard pattern
		 * @return this {@code Builder} instance
		 * @throws NullPointerException thrown if, and only if, {@code scale} is {@code null}
		 */
		public Builder setScale(final Vector2F scale) {
			this.scale = Objects.requireNonNull(scale, "scale == null");
			
			return this;
		}
		
		/**
		 * Returns a new {@link CheckerboardMaterial} instance.
		 * 
		 * @return a new {@code CheckerboardMaterial} instance
		 */
		public CheckerboardMaterial build() {
			return new CheckerboardMaterial(this.materialA, this.materialB, this.angle, this.scale);
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private Material doGetMaterial(final Intersection intersection) {
		final float cosAngleRadians = cos(this.angle.getRadians());
		final float sinAngleRadians = sin(this.angle.getRadians());
		
		final float u = intersection.getTextureCoordinates().x;
		final float v = intersection.getTextureCoordinates().y;
		
		final boolean isU = fractionalPart((u * cosAngleRadians - v * sinAngleRadians) * this.scale.x) > 0.5F;
		final boolean isV = fractionalPart((v * cosAngleRadians + u * sinAngleRadians) * this.scale.y) > 0.5F;
		
		return isU ^ isV ? this.materialA : this.materialB;
	}
}