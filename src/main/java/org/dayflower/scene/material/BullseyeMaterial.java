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

import static org.dayflower.utility.Floats.equal;

import java.util.Objects;

import org.dayflower.color.Color3F;
import org.dayflower.geometry.Point3F;
import org.dayflower.geometry.Vector3F;
import org.dayflower.scene.Intersection;
import org.dayflower.scene.Material;
import org.dayflower.scene.ScatteringFunctions;
import org.dayflower.scene.TransportMode;

import org.macroing.java.util.visitor.NodeHierarchicalVisitor;
import org.macroing.java.util.visitor.NodeTraversalException;

/**
 * A {@code BullseyeMaterial} is an implementation of {@link Material} that alternates between two other {@code Material} instances in a bullseye pattern.
 * <p>
 * This class is immutable and thread-safe as long as its two associated {@link Material} instances are.
 * <p>
 * This {@code Material} implementation is supported on the GPU.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class BullseyeMaterial implements Material {
	/**
	 * The name of this {@code BullseyeMaterial} class.
	 */
	public static final String NAME = "Bullseye";
	
	/**
	 * The ID of this {@code BullseyeMaterial} class.
	 */
	public static final int ID = 1;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final Material materialA;
	private final Material materialB;
	private final Point3F origin;
	private final float scale;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code BullseyeMaterial} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new BullseyeMaterial(new MatteMaterial(Color3F.GRAY), new MatteMaterial(Color3F.WHITE));
	 * }
	 * </pre>
	 */
	public BullseyeMaterial() {
		this(new MatteMaterial(Color3F.GRAY), new MatteMaterial(Color3F.WHITE));
	}
	
	/**
	 * Constructs a new {@code BullseyeMaterial} instance.
	 * <p>
	 * If either {@code materialA} or {@code materialB} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new BullseyeMaterial(materialA, materialB, new Point3F(0.0F, 10.0F, 0.0F));
	 * }
	 * </pre>
	 * 
	 * @param materialA the {@link Material} instance that is denoted by {@code A} and is used as the first bullseye pattern component
	 * @param materialB the {@code Material} instance that is denoted by {@code B} and is used as the second bullseye pattern component
	 * @throws NullPointerException thrown if, and only if, either {@code materialA} or {@code materialB} are {@code null}
	 */
	public BullseyeMaterial(final Material materialA, final Material materialB) {
		this(materialA, materialB, new Point3F(0.0F, 10.0F, 0.0F));
	}
	
	/**
	 * Constructs a new {@code BullseyeMaterial} instance.
	 * <p>
	 * If either {@code materialA}, {@code materialB} or {@code origin} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new BullseyeMaterial(materialA, materialB, origin, 1.0F);
	 * }
	 * </pre>
	 * 
	 * @param materialA the {@link Material} instance that is denoted by {@code A} and is used as the first bullseye pattern component
	 * @param materialB the {@code Material} instance that is denoted by {@code B} and is used as the second bullseye pattern component
	 * @param origin the {@link Point3F} instance that is used as the origin for the bullseye pattern
	 * @throws NullPointerException thrown if, and only if, either {@code materialA}, {@code materialB} or {@code origin} are {@code null}
	 */
	public BullseyeMaterial(final Material materialA, final Material materialB, final Point3F origin) {
		this(materialA, materialB, origin, 1.0F);
	}
	
	/**
	 * Constructs a new {@code BullseyeMaterial} instance.
	 * <p>
	 * If either {@code materialA}, {@code materialB} or {@code origin} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param materialA the {@link Material} instance that is denoted by {@code A} and is used as the first bullseye pattern component
	 * @param materialB the {@code Material} instance that is denoted by {@code B} and is used as the second bullseye pattern component
	 * @param origin the {@link Point3F} instance that is used as the origin for the bullseye pattern
	 * @param scale the {@code float} value that is used to scale the bullseye pattern
	 * @throws NullPointerException thrown if, and only if, either {@code materialA}, {@code materialB} or {@code origin} are {@code null}
	 */
	public BullseyeMaterial(final Material materialA, final Material materialB, final Point3F origin, final float scale) {
		this.materialA = Objects.requireNonNull(materialA, "materialA == null");
		this.materialB = Objects.requireNonNull(materialB, "materialB == null");
		this.origin = Objects.requireNonNull(origin, "origin == null");
		this.scale = scale;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link Color3F} instance with the emittance of this {@code BullseyeMaterial} instance at {@code intersection}.
	 * <p>
	 * If {@code intersection} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param intersection an {@link Intersection} instance
	 * @return a {@code Color3F} instance with the emittance of this {@code BullseyeMaterial} instance at {@code intersection}
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
	 * Returns the {@link Material} instance that is denoted by {@code A} and is used as the first bullseye pattern component.
	 * 
	 * @return the {@code Material} instance that is denoted by {@code A} and is used as the first bullseye pattern component
	 */
	public Material getMaterialA() {
		return this.materialA;
	}
	
	/**
	 * Returns the {@link Material} instance that is denoted by {@code B} and is used as the second bullseye pattern component.
	 * 
	 * @return the {@code Material} instance that is denoted by {@code B} and is used as the second bullseye pattern component
	 */
	public Material getMaterialB() {
		return this.materialB;
	}
	
	/**
	 * Returns the {@link Point3F} instance that is used as the origin for the bullseye pattern.
	 * 
	 * @return the {@code Point3F} instance that is used as the origin for the bullseye pattern
	 */
	public Point3F getOrigin() {
		return this.origin;
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
	 * Returns a {@code String} with the name of this {@code BullseyeMaterial} instance.
	 * 
	 * @return a {@code String} with the name of this {@code BullseyeMaterial} instance
	 */
	@Override
	public String getName() {
		return NAME;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code BullseyeMaterial} instance.
	 * 
	 * @return a {@code String} representation of this {@code BullseyeMaterial} instance
	 */
	@Override
	public String toString() {
		return String.format("new BullseyeMaterial(%s, %s, %s, %+.10f)", this.materialA, this.materialB, this.origin, Float.valueOf(this.scale));
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
				
				if(!this.origin.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
			}
			
			return nodeHierarchicalVisitor.visitLeave(this);
		} catch(final RuntimeException e) {
			throw new NodeTraversalException(e);
		}
	}
	
	/**
	 * Compares {@code object} to this {@code BullseyeMaterial} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code BullseyeMaterial}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code BullseyeMaterial} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code BullseyeMaterial}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof BullseyeMaterial)) {
			return false;
		} else if(!Objects.equals(this.materialA, BullseyeMaterial.class.cast(object).materialA)) {
			return false;
		} else if(!Objects.equals(this.materialB, BullseyeMaterial.class.cast(object).materialB)) {
			return false;
		} else if(!Objects.equals(this.origin, BullseyeMaterial.class.cast(object).origin)) {
			return false;
		} else if(!equal(this.scale, BullseyeMaterial.class.cast(object).scale)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns the {@code float} value that is used to scale the bullseye pattern.
	 * 
	 * @return the {@code float} value that is used to scale the bullseye pattern
	 */
	public float getScale() {
		return this.scale;
	}
	
	/**
	 * Returns an {@code int} with the ID of this {@code BullseyeMaterial} instance.
	 * 
	 * @return an {@code int} with the ID of this {@code BullseyeMaterial} instance
	 */
	@Override
	public int getID() {
		return ID;
	}
	
	/**
	 * Returns a hash code for this {@code BullseyeMaterial} instance.
	 * 
	 * @return a hash code for this {@code BullseyeMaterial} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.materialA, this.materialB, this.origin, Float.valueOf(this.scale));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * A {@code Builder} is used to build {@link BullseyeMaterial} instances.
	 * <p>
	 * This class is mutable and not thread-safe.
	 * 
	 * @since 1.0.0
	 * @author J&#246;rgen Lundgren
	 */
	public static final class Builder {
		private Material materialA;
		private Material materialB;
		private Point3F origin;
		private float scale;
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		/**
		 * Constructs a new {@code Builder} instance.
		 */
		public Builder() {
			this(new BullseyeMaterial());
		}
		
		/**
		 * Constructs a new {@code Builder} instance given {@code bullseyeMaterial}.
		 * <p>
		 * If {@code bullseyeMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
		 * 
		 * @param bullseyeMaterial a {@link BullseyeMaterial} instance
		 * @throws NullPointerException thrown if, and only if, {@code bullseyeMaterial} is {@code null}
		 */
		public Builder(final BullseyeMaterial bullseyeMaterial) {
			this.materialA = bullseyeMaterial.getMaterialA();
			this.materialB = bullseyeMaterial.getMaterialB();
			this.origin = bullseyeMaterial.getOrigin();
			this.scale = bullseyeMaterial.getScale();
		}
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		/**
		 * Sets the {@link Material} instance that is denoted by {@code A} and is used as the first bullseye pattern component.
		 * <p>
		 * Returns this {@code Builder} instance.
		 * <p>
		 * If {@code materialA} is {@code null}, a {@code NullPointerException} will be thrown.
		 * 
		 * @param materialA the {@code Material} instance that is denoted by {@code A} and is used as the first bullseye pattern component
		 * @return this {@code Builder} instance
		 * @throws NullPointerException thrown if, and only if, {@code materialA} is {@code null}
		 */
		public Builder setMaterialA(final Material materialA) {
			this.materialA = Objects.requireNonNull(materialA, "materialA == null");
			
			return this;
		}
		
		/**
		 * Sets the {@link Material} instance that is denoted by {@code B} and is used as the second bullseye pattern component.
		 * <p>
		 * Returns this {@code Builder} instance.
		 * <p>
		 * If {@code materialB} is {@code null}, a {@code NullPointerException} will be thrown.
		 * 
		 * @param materialB the {@code Material} instance that is denoted by {@code B} and is used as the second bullseye pattern component
		 * @return this {@code Builder} instance
		 * @throws NullPointerException thrown if, and only if, {@code materialB} is {@code null}
		 */
		public Builder setMaterialB(final Material materialB) {
			this.materialB = Objects.requireNonNull(materialB, "materialB == null");
			
			return this;
		}
		
		/**
		 * Sets the {@link Point3F} instance that is used as the origin for the bullseye pattern.
		 * <p>
		 * Returns this {@code Builder} instance.
		 * <p>
		 * If {@code origin} is {@code null}, a {@code NullPointerException} will be thrown.
		 * 
		 * @param origin the {@code Point3F} instance that is used as the origin for the bullseye pattern
		 * @return this {@code Builder} instance
		 * @throws NullPointerException thrown if, and only if, {@code origin} is {@code null}
		 */
		public Builder setOrigin(final Point3F origin) {
			this.origin = Objects.requireNonNull(origin, "origin == null");
			
			return this;
		}
		
		/**
		 * Sets the {@code float} value that is used to scale the bullseye pattern.
		 * <p>
		 * Returns this {@code Builder} instance.
		 * 
		 * @param scale the {@code float} value that is used to scale the bullseye pattern
		 * @return this {@code Builder} instance
		 */
		public Builder setScale(final float scale) {
			this.scale = scale;
			
			return this;
		}
		
		/**
		 * Returns a new {@link BullseyeMaterial} instance.
		 * 
		 * @return a new {@code BullseyeMaterial} instance
		 */
		public BullseyeMaterial build() {
			return new BullseyeMaterial(this.materialA, this.materialB, this.origin, this.scale);
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private Material doGetMaterial(final Intersection intersection) {
		final Vector3F direction = Vector3F.direction(this.origin, intersection.getSurfaceIntersectionObjectSpace().getSurfaceIntersectionPoint());
		
		final float directionLength = direction.length();
		final float directionLengthScaled = directionLength * this.scale;
		final float directionLengthScaledRemainder = directionLengthScaled % 1.0F;
		
		return directionLengthScaledRemainder > 0.5F ? this.materialA : this.materialB;
	}
}