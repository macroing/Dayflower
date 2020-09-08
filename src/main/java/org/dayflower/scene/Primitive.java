/**
 * Copyright 2020 J&#246;rgen Lundgren
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

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.Optional;

import org.dayflower.geometry.Matrix44F;
import org.dayflower.geometry.Ray3F;
import org.dayflower.geometry.Shape3F;
import org.dayflower.geometry.Sphere3F;
import org.dayflower.geometry.SurfaceIntersection3F;
import org.dayflower.image.Color3F;

//TODO: Add Javadocs!
public final class Primitive {
	private Material material;
	private Matrix44F objectToWorld;
	private Matrix44F worldToObject;
	private Shape3F shape;
	private Texture textureAlbedo;
	private Texture textureEmittance;
	private Texture textureNormal;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code Primitive} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Primitive(new LambertianMaterial());
	 * }
	 * </pre>
	 */
	public Primitive() {
		this(new LambertianMaterial());
	}
	
	/**
	 * Constructs a new {@code Primitive} instance.
	 * <p>
	 * If {@code material} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Primitive(material, new Sphere3F());
	 * }
	 * </pre>
	 * 
	 * @param material the {@link Material} instance associated with this {@code Primitive} instance
	 * @throws NullPointerException thrown if, and only if, {@code material} is {@code null}
	 */
	public Primitive(final Material material) {
		this(material, new Sphere3F());
	}
	
	/**
	 * Constructs a new {@code Primitive} instance.
	 * <p>
	 * If either {@code material} or {@code shape} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Primitive(material, shape, new ConstantTexture(Color3F.GREEN));
	 * }
	 * </pre>
	 * 
	 * @param material the {@link Material} instance associated with this {@code Primitive} instance
	 * @param shape the {@link Shape3F} instance associated with this {@code Primitive} instance
	 * @throws NullPointerException thrown if, and only if, either {@code material} or {@code shape} are {@code null}
	 */
	public Primitive(final Material material, final Shape3F shape) {
		this(material, shape, new ConstantTexture(Color3F.GREEN));
	}
	
	/**
	 * Constructs a new {@code Primitive} instance.
	 * <p>
	 * If either {@code material}, {@code shape} or {@code textureAlbedo} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Primitive(material, shape, textureAlbedo, new ConstantTexture());
	 * }
	 * </pre>
	 * 
	 * @param material the {@link Material} instance associated with this {@code Primitive} instance
	 * @param shape the {@link Shape3F} instance associated with this {@code Primitive} instance
	 * @param textureAlbedo the {@link Texture} instance for the albedo color that is associated with this {@code Primitive} instance
	 * @throws NullPointerException thrown if, and only if, either {@code material}, {@code shape} or {@code textureAlbedo} are {@code null}
	 */
	public Primitive(final Material material, final Shape3F shape, final Texture textureAlbedo) {
		this(material, shape, textureAlbedo, new ConstantTexture());
	}
	
	/**
	 * Constructs a new {@code Primitive} instance.
	 * <p>
	 * If either {@code material}, {@code shape}, {@code textureAlbedo} or {@code textureEmittance} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Primitive(material, shape, textureAlbedo, textureEmittance, new ConstantTexture());
	 * }
	 * </pre>
	 * 
	 * @param material the {@link Material} instance associated with this {@code Primitive} instance
	 * @param shape the {@link Shape3F} instance associated with this {@code Primitive} instance
	 * @param textureAlbedo the {@link Texture} instance for the albedo color that is associated with this {@code Primitive} instance
	 * @param textureEmittance the {@code Texture} instance for the emittance that is associated with this {@code Primitive} instance
	 * @throws NullPointerException thrown if, and only if, either {@code material}, {@code shape}, {@code textureAlbedo} or {@code textureEmittance} are {@code null}
	 */
	public Primitive(final Material material, final Shape3F shape, final Texture textureAlbedo, final Texture textureEmittance) {
		this(material, shape, textureAlbedo, textureEmittance, new ConstantTexture());
	}
	
	/**
	 * Constructs a new {@code Primitive} instance.
	 * <p>
	 * If either {@code material}, {@code shape}, {@code textureAlbedo}, {@code textureEmittance} or {@code textureNormal} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Primitive(material, shape, textureAlbedo, textureEmittance, textureNormal, Matrix44F.identity());
	 * }
	 * </pre>
	 * 
	 * @param material the {@link Material} instance associated with this {@code Primitive} instance
	 * @param shape the {@link Shape3F} instance associated with this {@code Primitive} instance
	 * @param textureAlbedo the {@link Texture} instance for the albedo color that is associated with this {@code Primitive} instance
	 * @param textureEmittance the {@code Texture} instance for the emittance that is associated with this {@code Primitive} instance
	 * @param textureNormal the {@code Texture} instance for the normal that is associated with this {@code Primitive} instance
	 * @throws NullPointerException thrown if, and only if, either {@code material}, {@code shape}, {@code textureAlbedo}, {@code textureEmittance} or {@code textureNormal} are {@code null}
	 */
	public Primitive(final Material material, final Shape3F shape, final Texture textureAlbedo, final Texture textureEmittance, final Texture textureNormal) {
		this(material, shape, textureAlbedo, textureEmittance, textureNormal, Matrix44F.identity());
	}
	
	/**
	 * Constructs a new {@code Primitive} instance.
	 * <p>
	 * If either {@code material}, {@code shape}, {@code textureAlbedo}, {@code textureEmittance}, {@code textureNormal} or {@code objectToWorld} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param material the {@link Material} instance associated with this {@code Primitive} instance
	 * @param shape the {@link Shape3F} instance associated with this {@code Primitive} instance
	 * @param textureAlbedo the {@link Texture} instance for the albedo color that is associated with this {@code Primitive} instance
	 * @param textureEmittance the {@code Texture} instance for the emittance that is associated with this {@code Primitive} instance
	 * @param textureNormal the {@code Texture} instance for the normal that is associated with this {@code Primitive} instance
	 * @param objectToWorld the {@link Matrix44F} instance that is used to transform from object space to world space and is associated with this {@code Primitive} instance
	 * @throws NullPointerException thrown if, and only if, either {@code material}, {@code shape}, {@code textureAlbedo}, {@code textureEmittance}, {@code textureNormal} or {@code objectToWorld} are {@code null}
	 */
	public Primitive(final Material material, final Shape3F shape, final Texture textureAlbedo, final Texture textureEmittance, final Texture textureNormal, final Matrix44F objectToWorld) {
		this.material = Objects.requireNonNull(material, "material == null");
		this.shape = Objects.requireNonNull(shape, "shape == null");
		this.textureAlbedo = Objects.requireNonNull(textureAlbedo, "textureAlbedo == null");
		this.textureEmittance = Objects.requireNonNull(textureEmittance, "textureEmittance == null");
		this.textureNormal = Objects.requireNonNull(textureNormal, "textureNormal == null");
		this.objectToWorld = Objects.requireNonNull(objectToWorld, "objectToWorld == null");
		this.worldToObject = Matrix44F.inverse(objectToWorld);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the {@link Material} instance associated with this {@code Primitive} instance.
	 * 
	 * @return the {@link Material} instance associated with this {@code Primitive} instance
	 */
	public Material getMaterial() {
		return this.material;
	}
	
	/**
	 * Returns the {@link Matrix44F} instance that is used to transform from object space to world space and is associated with this {@code Primitive} instance.
	 * 
	 * @return the {@code Matrix44F} instance that is used to transform from object space to world space and is associated with this {@code Primitive} instance
	 */
	public Matrix44F getObjectToWorld() {
		return this.objectToWorld;
	}
	
	/**
	 * Returns the {@link Matrix44F} instance that is used to transform from world space to object space and is associated with this {@code Primitive} instance.
	 * 
	 * @return the {@code Matrix44F} instance that is used to transform from world space to object space and is associated with this {@code Primitive} instance
	 */
	public Matrix44F getWorldToObject() {
		return this.worldToObject;
	}
	
//	TODO: Add Javadocs!
	public Optional<Intersection> intersection(final Ray3F rayWorldSpace) {
		return intersection(rayWorldSpace, 0.0001F, Float.MAX_VALUE);
	}
	
//	TODO: Add Javadocs!
	public Optional<Intersection> intersection(final Ray3F rayWorldSpace, final float tMinimum, final float tMaximum) {
		return this.shape.intersection(Ray3F.transform(this.worldToObject, rayWorldSpace), tMinimum, tMaximum).map(surfaceIntersectionObjectSpace -> new Intersection(this, surfaceIntersectionObjectSpace));
	}
	
	/**
	 * Returns the {@link Shape3F} instance associated with this {@code Primitive} instance.
	 * 
	 * @return the {@link Shape3F} instance associated with this {@code Primitive} instance
	 */
	public Shape3F getShape() {
		return this.shape;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code Primitive} instance.
	 * 
	 * @return a {@code String} representation of this {@code Primitive} instance
	 */
	@Override
	public String toString() {
		return String.format("new Primitive(%s, %s, %s, %s, %s, %s)", this.material, this.shape, this.textureAlbedo, this.textureEmittance, this.textureNormal, this.objectToWorld);
	}
	
	/**
	 * Returns the {@link Texture} instance for the albedo color that is associated with this {@code Primitive} instance.
	 * 
	 * @return the {@code Texture} instance for the albedo color that is associated with this {@code Primitive} instance
	 */
	public Texture getTextureAlbedo() {
		return this.textureAlbedo;
	}
	
	/**
	 * Returns the {@link Texture} instance for the emittance that is associated with this {@code Primitive} instance.
	 * 
	 * @return the {@code Texture} instance for the emittance that is associated with this {@code Primitive} instance
	 */
	public Texture getTextureEmittance() {
		return this.textureEmittance;
	}
	
	/**
	 * Returns the {@link Texture} instance for the normal that is associated with this {@code Primitive} instance.
	 * 
	 * @return the {@code Texture} instance for the normal that is associated with this {@code Primitive} instance
	 */
	public Texture getTextureNormal() {
		return this.textureNormal;
	}
	
	/**
	 * Compares {@code object} to this {@code Primitive} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code Primitive}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code Primitive} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code Primitive}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof Primitive)) {
			return false;
		} else if(!Objects.equals(this.material, Primitive.class.cast(object).material)) {
			return false;
		} else if(!Objects.equals(this.objectToWorld, Primitive.class.cast(object).objectToWorld)) {
			return false;
		} else if(!Objects.equals(this.worldToObject, Primitive.class.cast(object).worldToObject)) {
			return false;
		} else if(!Objects.equals(this.shape, Primitive.class.cast(object).shape)) {
			return false;
		} else if(!Objects.equals(this.textureAlbedo, Primitive.class.cast(object).textureAlbedo)) {
			return false;
		} else if(!Objects.equals(this.textureEmittance, Primitive.class.cast(object).textureEmittance)) {
			return false;
		} else if(!Objects.equals(this.textureNormal, Primitive.class.cast(object).textureNormal)) {
			return false;
		} else {
			return true;
		}
	}
	
//	TODO: Add Javadocs!
	public boolean isIntersecting(final Ray3F rayWorldSpace) {
		return isIntersecting(rayWorldSpace, 0.0001F, Float.MAX_VALUE);
	}
	
//	TODO: Add Javadocs!
	public boolean isIntersecting(final Ray3F rayWorldSpace, final float tMinimum, final float tMaximum) {
		return this.shape.isIntersecting(Ray3F.transform(this.worldToObject, rayWorldSpace), tMinimum, tMaximum);
	}
	
	/**
	 * Returns a hash code for this {@code Primitive} instance.
	 * 
	 * @return a hash code for this {@code Primitive} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.material, this.objectToWorld, this.worldToObject, this.shape, this.textureAlbedo, this.textureEmittance, this.textureNormal);
	}
	
//	TODO: Add Javadocs!
	public void setMaterial(final Material material) {
		this.material = Objects.requireNonNull(material, "material == null");
	}
	
//	TODO: Add Javadocs!
	public void setObjectToWorld(final Matrix44F objectToWorld) {
		this.objectToWorld = Objects.requireNonNull(objectToWorld, "objectToWorld == null");
		this.worldToObject = Matrix44F.inverse(objectToWorld);
	}
	
//	TODO: Add Javadocs!
	public void setShape(final Shape3F shape) {
		this.shape = Objects.requireNonNull(shape, "shape == null");
	}
	
//	TODO: Add Javadocs!
	public void setTextureAlbedo(final Texture textureAlbedo) {
		this.textureAlbedo = Objects.requireNonNull(textureAlbedo, "textureAlbedo == null");
	}
	
//	TODO: Add Javadocs!
	public void setTextureEmittance(final Texture textureEmittance) {
		this.textureEmittance = Objects.requireNonNull(textureEmittance, "textureEmittance == null");
	}
	
//	TODO: Add Javadocs!
	public void setTextureNormal(final Texture textureNormal) {
		this.textureNormal = Objects.requireNonNull(textureNormal, "textureNormal == null");
	}
	
//	TODO: Add Javadocs!
	public void setWorldToObject(final Matrix44F worldToObject) {
		this.worldToObject = Objects.requireNonNull(worldToObject, "worldToObject == null");
		this.objectToWorld = Matrix44F.inverse(worldToObject);
	}
}