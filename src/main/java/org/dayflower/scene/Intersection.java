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
package org.dayflower.scene;

import java.util.Objects;
import java.util.Optional;

import org.dayflower.color.Color3F;
import org.dayflower.geometry.OrthonormalBasis33F;
import org.dayflower.geometry.Point2F;
import org.dayflower.geometry.Point3F;
import org.dayflower.geometry.Ray3F;
import org.dayflower.geometry.Shape3F;
import org.dayflower.geometry.SurfaceIntersection3F;
import org.dayflower.geometry.Vector3F;

/**
 * An {@code Intersection} denotes an intersection between a {@link Ray3F} instance and a {@link Primitive} instance.
 * <p>
 * This class is mutable and therefore not thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class Intersection {
	/**
	 * An empty {@code Optional} instance.
	 */
	public static final Optional<Intersection> EMPTY = Optional.empty();
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private OrthonormalBasis33F orthonormalBasisG;
	private OrthonormalBasis33F orthonormalBasisS;
	private Point2F textureCoordinates;
	private final Primitive primitive;
	private final SurfaceIntersection3F surfaceIntersectionObjectSpace;
	private final SurfaceIntersection3F surfaceIntersectionWorldSpace;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code Intersection} instance.
	 * <p>
	 * If either {@code primitive} or {@code surfaceIntersectionObjectSpace} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param primitive the {@link Primitive} instance associated with this {@code Intersection} instance
	 * @param surfaceIntersectionObjectSpace the {@link SurfaceIntersection3F} instance associated with this {@code Intersection} instance in object space
	 * @throws NullPointerException thrown if, and only if, either {@code primitive} or {@code surfaceIntersectionObjectSpace} are {@code null}
	 */
	public Intersection(final Primitive primitive, final SurfaceIntersection3F surfaceIntersectionObjectSpace) {
		this.primitive = Objects.requireNonNull(primitive, "primitive == null");
		this.surfaceIntersectionObjectSpace = Objects.requireNonNull(surfaceIntersectionObjectSpace, "surfaceIntersectionObjectSpace == null");
		this.surfaceIntersectionWorldSpace = SurfaceIntersection3F.transform(surfaceIntersectionObjectSpace, primitive.getTransform().getObjectToWorld(), primitive.getTransform().getWorldToObject());
		this.orthonormalBasisG = this.surfaceIntersectionWorldSpace.getOrthonormalBasisG();
		this.orthonormalBasisS = this.surfaceIntersectionWorldSpace.getOrthonormalBasisS();
		this.textureCoordinates = this.surfaceIntersectionWorldSpace.getTextureCoordinates();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Evaluates the emitted radiance along {@code direction}.
	 * <p>
	 * Returns a {@link Color3F} instance with the emitted radiance.
	 * <p>
	 * If {@code direction} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param direction the direction
	 * @return a {@code Color3F} instance with the emitted radiance
	 * @throws NullPointerException thrown if, and only if, {@code direction} is {@code null}
	 */
	public Color3F evaluateRadianceEmitted(final Vector3F direction) {
		Objects.requireNonNull(direction, "direction == null");
		
		final Optional<AreaLight> optionalAreaLight = this.primitive.getAreaLight();
		
		if(optionalAreaLight.isPresent()) {
			final AreaLight areaLight = optionalAreaLight.get();
			
			return areaLight.evaluateRadianceEmitted(this, direction);
		}
		
		return this.primitive.getMaterial().emittance(this);
	}
	
	/**
	 * Returns the {@link OrthonormalBasis33F} instance that is used as the orthonormal basis for the geometry in world space.
	 * 
	 * @return the {@code OrthonormalBasis33F} instance that is used as the orthonormal basis for the geometry in world space
	 */
	public OrthonormalBasis33F getOrthonormalBasisG() {
		return this.orthonormalBasisG;
	}
	
	/**
	 * Returns the {@link OrthonormalBasis33F} instance that is used as the orthonormal basis for shading in world space.
	 * 
	 * @return the {@code OrthonormalBasis33F} instance that is used as the orthonormal basis for shading in world space
	 */
	public OrthonormalBasis33F getOrthonormalBasisS() {
		return this.orthonormalBasisS;
	}
	
	/**
	 * Returns the {@link Point2F} instance that is used as the texture coordinates in world space.
	 * 
	 * @return the {@code Point2F} instance that is used as the texture coordinates in world space
	 */
	public Point2F getTextureCoordinates() {
		return this.textureCoordinates;
	}
	
	/**
	 * Returns the {@link Point3F} instance that is used as the surface intersection point in world space.
	 * 
	 * @return the {@code Point3F} instance that is used as the surface intersection point in world space
	 */
	public Point3F getSurfaceIntersectionPoint() {
		return this.surfaceIntersectionWorldSpace.getSurfaceIntersectionPoint();
	}
	
	/**
	 * Returns the {@link Primitive} instance associated with this {@code Intersection} instance.
	 * 
	 * @return the {@code Primitive} instance associated with this {@code Intersection} instance
	 */
	public Primitive getPrimitive() {
		return this.primitive;
	}
	
	/**
	 * Returns a new {@link Ray3F} in the direction towards {@code point}.
	 * <p>
	 * If {@code point} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param point a {@link Point3F} instance
	 * @return a new {@code Ray3F} in the direction towards {@code point}
	 * @throws NullPointerException thrown if, and only if, {@code point} is {@code null}
	 */
	public Ray3F createRay(final Point3F point) {
		return this.surfaceIntersectionWorldSpace.createRay(point, getSurfaceNormalSCorrectlyOriented());
	}
	
	/**
	 * Returns a new {@link Ray3F} in the direction {@code direction}.
	 * <p>
	 * If {@code direction} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param direction a {@link Vector3F} instance with the direction
	 * @return a new {@code Ray3F} in the direction {@code direction}
	 * @throws NullPointerException thrown if, and only if, {@code direction} is {@code null}
	 */
	public Ray3F createRay(final Vector3F direction) {
		return this.surfaceIntersectionWorldSpace.createRay(direction, getSurfaceNormalSCorrectlyOriented());
	}
	
	/**
	 * Returns the {@link Ray3F} instance that was used in the intersection operation in world space.
	 * 
	 * @return the {@code Ray3F} instance that was used in the intersection operation in world space
	 */
	public Ray3F getRay() {
		return this.surfaceIntersectionWorldSpace.getRay();
	}
	
	/**
	 * Returns the {@link Shape3F} instance that was intersected.
	 * 
	 * @return the {@code Shape3F} instance that was intersected
	 */
	public Shape3F getShape() {
		return this.surfaceIntersectionWorldSpace.getShape();
	}
	
	/**
	 * Returns a {@code String} representation of this {@code Intersection} instance.
	 * 
	 * @return a {@code String} representation of this {@code Intersection} instance
	 */
	@Override
	public String toString() {
		return String.format("new Intersection(%s, %s)", this.primitive, this.surfaceIntersectionObjectSpace);
	}
	
	/**
	 * Returns the {@link SurfaceIntersection3F} instance associated with this {@code Intersection} instance in object space.
	 * 
	 * @return the {@code SurfaceIntersection3F} instance associated with this {@code Intersection} instance in object space
	 */
	public SurfaceIntersection3F getSurfaceIntersectionObjectSpace() {
		return this.surfaceIntersectionObjectSpace;
	}
	
	/**
	 * Returns the {@link SurfaceIntersection3F} instance associated with this {@code Intersection} instance in world space.
	 * 
	 * @return the {@code SurfaceIntersection3F} instance associated with this {@code Intersection} instance in world space
	 */
	public SurfaceIntersection3F getSurfaceIntersectionWorldSpace() {
		return this.surfaceIntersectionWorldSpace;
	}
	
	/**
	 * Returns the {@link Vector3F} instance that represents the surface normal for the geometry in world space.
	 * 
	 * @return the {@code Vector3F} instance that represents the surface normal for the geometry in world space
	 */
	private Vector3F getSurfaceNormalG() {
		return this.orthonormalBasisG.w;
	}
	
	/**
	 * Returns the {@link Vector3F} instance that represents the surface normal for the geometry in world space and is correctly oriented.
	 * 
	 * @return the {@code Vector3F} instance that represents the surface normal for the geometry in world space and is correctly oriented
	 */
	public Vector3F getSurfaceNormalGCorrectlyOriented() {
		return Vector3F.faceForwardNegated(getSurfaceNormalG(), getRay().getDirection());
	}
	
	/**
	 * Returns the {@link Vector3F} instance that represents the surface normal for shading in world space.
	 * 
	 * @return the {@code Vector3F} instance that represents the surface normal for shading in world space
	 */
	public Vector3F getSurfaceNormalS() {
		return this.orthonormalBasisS.w;
	}
	
	/**
	 * Returns the {@link Vector3F} instance that represents the surface normal for shading in world space and is correctly oriented.
	 * 
	 * @return the {@code Vector3F} instance that represents the surface normal for shading in world space and is correctly oriented
	 */
	public Vector3F getSurfaceNormalSCorrectlyOriented() {
		return Vector3F.faceForwardNegated(getSurfaceNormalS(), getRay().getDirection());
	}
	
	/**
	 * Compares {@code object} to this {@code Intersection} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code Intersection}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code Intersection} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code Intersection}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof Intersection)) {
			return false;
		} else if(!Objects.equals(this.orthonormalBasisG, Intersection.class.cast(object).orthonormalBasisG)) {
			return false;
		} else if(!Objects.equals(this.orthonormalBasisS, Intersection.class.cast(object).orthonormalBasisS)) {
			return false;
		} else if(!Objects.equals(this.textureCoordinates, Intersection.class.cast(object).textureCoordinates)) {
			return false;
		} else if(!Objects.equals(this.primitive, Intersection.class.cast(object).primitive)) {
			return false;
		} else if(!Objects.equals(this.surfaceIntersectionObjectSpace, Intersection.class.cast(object).surfaceIntersectionObjectSpace)) {
			return false;
		} else if(!Objects.equals(this.surfaceIntersectionWorldSpace, Intersection.class.cast(object).surfaceIntersectionWorldSpace)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns the parametric {@code t} value that represents the distance to the intersection in world space.
	 * 
	 * @return the parametric {@code t} value that represents the distance to the intersection in world space
	 */
	public float getT() {
		return this.surfaceIntersectionWorldSpace.getT();
	}
	
	/**
	 * Returns a hash code for this {@code Intersection} instance.
	 * 
	 * @return a hash code for this {@code Intersection} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.orthonormalBasisG, this.orthonormalBasisS, this.textureCoordinates, this.primitive, this.surfaceIntersectionObjectSpace, this.surfaceIntersectionWorldSpace);
	}
	
	/**
	 * Sets the {@link OrthonormalBasis33F} instance that is used as the orthonormal basis for the geometry in world space to {@code orthonormalBasisG}.
	 * <p>
	 * If {@code orthonormalBasisG} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param orthonormalBasisG the {@code OrthonormalBasis33F} instance that is used as the orthonormal basis for the geometry in world space
	 * @throws NullPointerException thrown if, and only if, {@code orthonormalBasisG} is {@code null}
	 */
	public void setOrthonormalBasisG(final OrthonormalBasis33F orthonormalBasisG) {
		this.orthonormalBasisG = Objects.requireNonNull(orthonormalBasisG, "orthonormalBasisG == null");
	}
	
	/**
	 * Sets the {@link OrthonormalBasis33F} instance that is used as the orthonormal basis for shading in world space to {@code orthonormalBasisS}.
	 * <p>
	 * If {@code orthonormalBasisS} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param orthonormalBasisS the {@code OrthonormalBasis33F} instance that is used as the orthonormal basis for shading in world space
	 * @throws NullPointerException thrown if, and only if, {@code orthonormalBasisS} is {@code null}
	 */
	public void setOrthonormalBasisS(final OrthonormalBasis33F orthonormalBasisS) {
		this.orthonormalBasisS = Objects.requireNonNull(orthonormalBasisS, "orthonormalBasisS == null");
	}
	
	/**
	 * Sets the {@link Vector3F} instance that represents the surface normal for the geometry in world space to {@code surfaceNormalG}.
	 * <p>
	 * If {@code surfaceNormalG} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param surfaceNormalG the {@code Vector3F} instance that represents the surface normal for the geometry in world space
	 * @throws NullPointerException thrown if, and only if, {@code surfaceNormalG} is {@code null}
	 */
	public void setSurfaceNormalG(final Vector3F surfaceNormalG) {
		this.orthonormalBasisG = new OrthonormalBasis33F(Objects.requireNonNull(surfaceNormalG, "surfaceNormalG == null"), this.orthonormalBasisG.v);
	}
	
	/**
	 * Sets the {@link Vector3F} instance that represents the surface normal for shading in world space to {@code surfaceNormalS}.
	 * <p>
	 * If {@code surfaceNormalS} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param surfaceNormalS the {@code Vector3F} instance that represents the surface normal for shading in world space
	 * @throws NullPointerException thrown if, and only if, {@code surfaceNormalS} is {@code null}
	 */
	public void setSurfaceNormalS(final Vector3F surfaceNormalS) {
		this.orthonormalBasisS = new OrthonormalBasis33F(Objects.requireNonNull(surfaceNormalS, "surfaceNormalS == null"), this.orthonormalBasisS.v);
	}
	
	/**
	 * Sets the {@link Point2F} instance that is used as the texture coordinates in world space to {@code textureCoordinates}.
	 * <p>
	 * If {@code textureCoordinates} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param textureCoordinates the {@code Point2F} instance that is used as the texture coordinates in world space
	 * @throws NullPointerException thrown if, and only if, {@code textureCoordinates} is {@code null}
	 */
	public void setTextureCoordinates(final Point2F textureCoordinates) {
		this.textureCoordinates = Objects.requireNonNull(textureCoordinates, "textureCoordinates == null");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the closest {@code Intersection} instance.
	 * <p>
	 * If either {@code optionalIntersectionA} or {@code optionalIntersectionB} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param optionalIntersectionA an {@code Optional} with an optional {@code Intersection} instance
	 * @param optionalIntersectionB an {@code Optional} with an optional {@code Intersection} instance
	 * @return the closest {@code Intersection} instance
	 * @throws NullPointerException thrown if, and only if, either {@code optionalIntersectionA} or {@code optionalIntersectionB} are {@code null}
	 */
	public static Optional<Intersection> closest(final Optional<Intersection> optionalIntersectionA, final Optional<Intersection> optionalIntersectionB) {
		final Intersection intersectionA = optionalIntersectionA.orElse(null);
		final Intersection intersectionB = optionalIntersectionB.orElse(null);
		
		if(intersectionA != null && intersectionB != null) {
			return intersectionA.getSurfaceIntersectionWorldSpace().getT() < intersectionB.getSurfaceIntersectionWorldSpace().getT() ? optionalIntersectionA : optionalIntersectionB;
		} else if(intersectionA != null) {
			return optionalIntersectionA;
		} else if(intersectionB != null) {
			return optionalIntersectionB;
		} else {
			return EMPTY;
		}
	}
}