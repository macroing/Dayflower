/**
 * Copyright 2014 - 2022 J&#246;rgen Lundgren
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
import static org.dayflower.utility.Floats.equal;
import static org.dayflower.utility.Floats.fractionalPart;
import static org.dayflower.utility.Floats.sin;

import java.util.Objects;
import java.util.Optional;

import org.dayflower.geometry.AngleF;
import org.dayflower.node.NodeHierarchicalVisitor;
import org.dayflower.node.NodeTraversalException;
import org.dayflower.scene.BSDF;
import org.dayflower.scene.BSSRDF;
import org.dayflower.scene.Intersection;
import org.dayflower.scene.Material;
import org.dayflower.scene.TransportMode;

import org.macroing.art4j.color.Color3F;

/**
 * A {@code PolkaDotMaterial} is an implementation of {@link Material} that alternates between two other {@code Material} instances in a polka dot pattern.
 * <p>
 * This class is immutable and thread-safe as long as its two associated {@link Material} instances are.
 * <p>
 * This {@code Material} implementation is supported on the GPU.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class PolkaDotMaterial implements Material {
	/**
	 * The name of this {@code PolkaDotMaterial} class.
	 */
	public static final String NAME = "Polka Dot";
	
	/**
	 * The ID of this {@code PolkaDotMaterial} class.
	 */
	public static final int ID = 13;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final AngleF angle;
	private final Material materialA;
	private final Material materialB;
	private final float cellResolution;
	private final float polkaDotRadius;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code PolkaDotMaterial} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new PolkaDotMaterial(new MatteMaterial(Color3F.GRAY), new MatteMaterial(Color3F.WHITE));
	 * }
	 * </pre>
	 */
	public PolkaDotMaterial() {
		this(new MatteMaterial(Color3F.GRAY), new MatteMaterial(Color3F.WHITE));
	}
	
	/**
	 * Constructs a new {@code PolkaDotMaterial} instance.
	 * <p>
	 * If either {@code materialA} or {@code materialB} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new PolkaDotMaterial(materialA, materialB, AngleF.degrees(0.0F));
	 * }
	 * </pre>
	 * 
	 * @param materialA the {@link Material} instance that is denoted by {@code A} and is used for the polka dot
	 * @param materialB the {@code Material} instance that is denoted by {@code B} and is used for the cell
	 * @throws NullPointerException thrown if, and only if, either {@code materialA} or {@code materialB} are {@code null}
	 */
	public PolkaDotMaterial(final Material materialA, final Material materialB) {
		this(materialA, materialB, AngleF.degrees(0.0F));
	}
	
	/**
	 * Constructs a new {@code PolkaDotMaterial} instance.
	 * <p>
	 * If either {@code materialA}, {@code materialB} or {@code angle} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new PolkaDotMaterial(materialA, materialB, angle, 10.0F);
	 * }
	 * </pre>
	 * 
	 * @param materialA the {@link Material} instance that is denoted by {@code A} and is used for the polka dot
	 * @param materialB the {@code Material} instance that is denoted by {@code B} and is used for the cell
	 * @param angle the {@link AngleF} instance that is used to rotate the polka dot pattern
	 * @throws NullPointerException thrown if, and only if, either {@code materialA}, {@code materialB} or {@code angle} are {@code null}
	 */
	public PolkaDotMaterial(final Material materialA, final Material materialB, final AngleF angle) {
		this(materialA, materialB, angle, 10.0F);
	}
	
	/**
	 * Constructs a new {@code PolkaDotMaterial} instance.
	 * <p>
	 * If either {@code materialA}, {@code materialB} or {@code angle} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new PolkaDotMaterial(materialA, materialB, angle, cellResolution, 0.25F);
	 * }
	 * </pre>
	 * 
	 * @param materialA the {@link Material} instance that is denoted by {@code A} and is used for the polka dot
	 * @param materialB the {@code Material} instance that is denoted by {@code B} and is used for the cell
	 * @param angle the {@link AngleF} instance that is used to rotate the polka dot pattern
	 * @param cellResolution the resolution of the cell
	 * @throws NullPointerException thrown if, and only if, either {@code materialA}, {@code materialB} or {@code angle} are {@code null}
	 */
	public PolkaDotMaterial(final Material materialA, final Material materialB, final AngleF angle, final float cellResolution) {
		this(materialA, materialB, angle, cellResolution, 0.25F);
	}
	
	/**
	 * Constructs a new {@code PolkaDotMaterial} instance.
	 * <p>
	 * If either {@code materialA}, {@code materialB} or {@code angle} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param materialA the {@link Material} instance that is denoted by {@code A} and is used for the polka dot
	 * @param materialB the {@code Material} instance that is denoted by {@code B} and is used for the cell
	 * @param angle the {@link AngleF} instance that is used to rotate the polka dot pattern
	 * @param cellResolution the resolution of the cell
	 * @param polkaDotRadius the radius of the polka dot
	 * @throws NullPointerException thrown if, and only if, either {@code materialA}, {@code materialB} or {@code angle} are {@code null}
	 */
	public PolkaDotMaterial(final Material materialA, final Material materialB, final AngleF angle, final float cellResolution, final float polkaDotRadius) {
		this.materialA = Objects.requireNonNull(materialA, "materialA == null");
		this.materialB = Objects.requireNonNull(materialB, "materialB == null");
		this.angle = Objects.requireNonNull(angle, "angle == null");
		this.cellResolution = cellResolution;
		this.polkaDotRadius = polkaDotRadius;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the {@link AngleF} instance that is used to rotate the polka dot pattern.
	 * 
	 * @return the {@code AngleF} instance that is used to rotate the polka dot pattern
	 */
	public AngleF getAngle() {
		return this.angle;
	}
	
	/**
	 * Returns a {@link Color3F} instance with the emittance of this {@code PolkaDotMaterial} instance at {@code intersection}.
	 * <p>
	 * If {@code intersection} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param intersection an {@link Intersection} instance
	 * @return a {@code Color3F} instance with the emittance of this {@code PolkaDotMaterial} instance at {@code intersection}
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
	 * Returns the {@link Material} instance that is denoted by {@code A} and is used for the polka dot.
	 * 
	 * @return the {@code Material} instance that is denoted by {@code A} and is used for the polka dot
	 */
	public Material getMaterialA() {
		return this.materialA;
	}
	
	/**
	 * Returns the {@link Material} instance that is denoted by {@code B} and is used for the cell.
	 * 
	 * @return the {@code Material} instance that is denoted by {@code B} and is used for the cell
	 */
	public Material getMaterialB() {
		return this.materialB;
	}
	
	/**
	 * Computes the {@link BSDF} at {@code intersection}.
	 * <p>
	 * Returns an optional {@code BSDF} instance.
	 * <p>
	 * If either {@code intersection} or {@code transportMode} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param intersection the {@link Intersection} to compute the {@code BSDF} for
	 * @param transportMode the {@link TransportMode} to use
	 * @param isAllowingMultipleLobes {@code true} if, and only if, multiple lobes are allowed, {@code false} otherwise
	 * @return an optional {@code BSDF} instance
	 * @throws NullPointerException thrown if, and only if, either {@code intersection} or {@code transportMode} are {@code null}
	 */
	@Override
	public Optional<BSDF> computeBSDF(final Intersection intersection, final TransportMode transportMode, final boolean isAllowingMultipleLobes) {
		Objects.requireNonNull(intersection, "intersection == null");
		Objects.requireNonNull(transportMode, "transportMode == null");
		
		final Material material = doGetMaterial(intersection);
		
		final Optional<BSDF> optionalBSDF = material.computeBSDF(intersection, transportMode, isAllowingMultipleLobes);
		
		return optionalBSDF;
	}
	
	/**
	 * Computes the {@link BSSRDF} at {@code intersection}.
	 * <p>
	 * Returns an optional {@code BSSRDF} instance.
	 * <p>
	 * If either {@code intersection} or {@code transportMode} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param intersection the {@link Intersection} to compute the {@code BSSRDF} for
	 * @param transportMode the {@link TransportMode} to use
	 * @param isAllowingMultipleLobes {@code true} if, and only if, multiple lobes are allowed, {@code false} otherwise
	 * @return an optional {@code BSSRDF} instance
	 * @throws NullPointerException thrown if, and only if, either {@code intersection} or {@code transportMode} are {@code null}
	 */
	@Override
	public Optional<BSSRDF> computeBSSRDF(final Intersection intersection, final TransportMode transportMode, final boolean isAllowingMultipleLobes) {
		Objects.requireNonNull(intersection, "intersection == null");
		Objects.requireNonNull(transportMode, "transportMode == null");
		
		final Material material = doGetMaterial(intersection);
		
		final Optional<BSSRDF> optionalBSSRDF = material.computeBSSRDF(intersection, transportMode, isAllowingMultipleLobes);
		
		return optionalBSSRDF;
	}
	
	/**
	 * Returns a {@code String} with the name of this {@code PolkaDotMaterial} instance.
	 * 
	 * @return a {@code String} with the name of this {@code PolkaDotMaterial} instance
	 */
	@Override
	public String getName() {
		return NAME;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code PolkaDotMaterial} instance.
	 * 
	 * @return a {@code String} representation of this {@code PolkaDotMaterial} instance
	 */
	@Override
	public String toString() {
		return String.format("new PolkaDotMaterial(%s, %s, %s, %+.10f, %+.10f)", this.materialA, this.materialB, this.angle, Float.valueOf(this.cellResolution), Float.valueOf(this.polkaDotRadius));
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
			}
			
			return nodeHierarchicalVisitor.visitLeave(this);
		} catch(final RuntimeException e) {
			throw new NodeTraversalException(e);
		}
	}
	
	/**
	 * Compares {@code object} to this {@code PolkaDotMaterial} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code PolkaDotMaterial}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code PolkaDotMaterial} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code PolkaDotMaterial}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof PolkaDotMaterial)) {
			return false;
		} else if(!Objects.equals(this.angle, PolkaDotMaterial.class.cast(object).angle)) {
			return false;
		} else if(!Objects.equals(this.materialA, PolkaDotMaterial.class.cast(object).materialA)) {
			return false;
		} else if(!Objects.equals(this.materialB, PolkaDotMaterial.class.cast(object).materialB)) {
			return false;
		} else if(!equal(this.cellResolution, PolkaDotMaterial.class.cast(object).cellResolution)) {
			return false;
		} else if(!equal(this.polkaDotRadius, PolkaDotMaterial.class.cast(object).polkaDotRadius)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns the resolution of the cell.
	 * 
	 * @return the resolution of the cell
	 */
	public float getCellResolution() {
		return this.cellResolution;
	}
	
	/**
	 * Returns the radius of the polka dot.
	 * 
	 * @return the radius of the polka dot
	 */
	public float getPolkaDotRadius() {
		return this.polkaDotRadius;
	}
	
	/**
	 * Returns an {@code int} with the ID of this {@code PolkaDotMaterial} instance.
	 * 
	 * @return an {@code int} with the ID of this {@code PolkaDotMaterial} instance
	 */
	@Override
	public int getID() {
		return ID;
	}
	
	/**
	 * Returns a hash code for this {@code PolkaDotMaterial} instance.
	 * 
	 * @return a hash code for this {@code PolkaDotMaterial} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.angle, this.materialA, this.materialB, Float.valueOf(this.cellResolution), Float.valueOf(this.polkaDotRadius));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * A {@code Builder} is used to build {@link PolkaDotMaterial} instances.
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
		private float cellResolution;
		private float polkaDotRadius;
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		/**
		 * Constructs a new {@code Builder} instance.
		 */
		public Builder() {
			this(new PolkaDotMaterial());
		}
		
		/**
		 * Constructs a new {@code Builder} instance given {@code polkaDotMaterial}.
		 * <p>
		 * If {@code polkaDotMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
		 * 
		 * @param polkaDotMaterial a {@link PolkaDotMaterial} instance
		 * @throws NullPointerException thrown if, and only if, {@code polkaDotMaterial} is {@code null}
		 */
		public Builder(final PolkaDotMaterial polkaDotMaterial) {
			this.angle = polkaDotMaterial.getAngle();
			this.materialA = polkaDotMaterial.getMaterialA();
			this.materialB = polkaDotMaterial.getMaterialB();
			this.cellResolution = polkaDotMaterial.getCellResolution();
			this.polkaDotRadius = polkaDotMaterial.getPolkaDotRadius();
		}
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		/**
		 * Sets the {@link AngleF} instance that is used to rotate the polka dot pattern.
		 * <p>
		 * Returns this {@code Builder} instance.
		 * <p>
		 * If {@code angle} is {@code null}, a {@code NullPointerException} will be thrown.
		 * 
		 * @param angle the {@code AngleF} instance that is used to rotate the polka dot pattern
		 * @return this {@code Builder} instance
		 * @throws NullPointerException thrown if, and only if, {@code angle} is {@code null}
		 */
		public Builder setAngle(final AngleF angle) {
			this.angle = Objects.requireNonNull(angle, "angle == null");
			
			return this;
		}
		
		/**
		 * Sets the resolution of the cell.
		 * <p>
		 * Returns this {@code Builder} instance.
		 * 
		 * @param cellResolution the resolution of the cell
		 * @return this {@code Builder} instance
		 */
		public Builder setCellResolution(final float cellResolution) {
			this.cellResolution = cellResolution;
			
			return this;
		}
		
		/**
		 * Sets the {@link Material} instance that is denoted by {@code A} and is used for the polka dot.
		 * <p>
		 * Returns this {@code Builder} instance.
		 * <p>
		 * If {@code materialA} is {@code null}, a {@code NullPointerException} will be thrown.
		 * 
		 * @param materialA the {@code Material} instance that is denoted by {@code A} and is used for the polka dot
		 * @return this {@code Builder} instance
		 * @throws NullPointerException thrown if, and only if, {@code materialA} is {@code null}
		 */
		public Builder setMaterialA(final Material materialA) {
			this.materialA = Objects.requireNonNull(materialA, "materialA == null");
			
			return this;
		}
		
		/**
		 * Sets the {@link Material} instance that is denoted by {@code B} and is used for the cell.
		 * <p>
		 * Returns this {@code Builder} instance.
		 * <p>
		 * If {@code materialB} is {@code null}, a {@code NullPointerException} will be thrown.
		 * 
		 * @param materialB the {@code Material} instance that is denoted by {@code B} and is used for the cell
		 * @return this {@code Builder} instance
		 * @throws NullPointerException thrown if, and only if, {@code materialB} is {@code null}
		 */
		public Builder setMaterialB(final Material materialB) {
			this.materialB = Objects.requireNonNull(materialB, "materialB == null");
			
			return this;
		}
		
		/**
		 * Sets the radius of the polka dot.
		 * <p>
		 * Returns this {@code Builder} instance.
		 * 
		 * @param polkaDotRadius the radius of the polka dot
		 * @return this {@code Builder} instance
		 */
		public Builder setPolkaDotRadius(final float polkaDotRadius) {
			this.polkaDotRadius = polkaDotRadius;
			
			return this;
		}
		
		/**
		 * Returns a new {@link PolkaDotMaterial} instance.
		 * 
		 * @return a new {@code PolkaDotMaterial} instance
		 */
		public PolkaDotMaterial build() {
			return new PolkaDotMaterial(this.materialA, this.materialB, this.angle, this.cellResolution, this.polkaDotRadius);
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private Material doGetMaterial(final Intersection intersection) {
		final float cosAngleRadians = cos(this.angle.getRadians());
		final float sinAngleRadians = sin(this.angle.getRadians());
		
		final float u = intersection.getTextureCoordinates().x;
		final float v = intersection.getTextureCoordinates().y;
		
		final float x = fractionalPart((u * cosAngleRadians - v * sinAngleRadians) * this.cellResolution) - 0.5F;
		final float y = fractionalPart((v * cosAngleRadians + u * sinAngleRadians) * this.cellResolution) - 0.5F;
		
		final float distanceSquared = x * x + y * y;
		
		if(distanceSquared < this.polkaDotRadius * this.polkaDotRadius) {
			return this.materialA;
		}
		
		return this.materialB;
	}
}