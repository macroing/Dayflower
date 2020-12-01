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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.dayflower.geometry.Matrix44F;
import org.dayflower.geometry.Point3F;
import org.dayflower.geometry.Quaternion4F;
import org.dayflower.geometry.Vector3F;
import org.dayflower.node.Node;
import org.dayflower.node.NodeHierarchicalVisitor;
import org.dayflower.node.NodeTraversalException;
import org.dayflower.util.ParameterArguments;

//TODO: Add Javadocs!
public final class Transform implements Node {
	private List<TransformObserver> transformObservers;
	private Matrix44F objectToWorld;
	private Matrix44F worldToObject;
	private Point3F position;
	private Quaternion4F rotation;
	private Vector3F scale;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code Transform} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Transform(new Point3F());
	 * }
	 * </pre>
	 */
	public Transform() {
		this(new Point3F());
	}
	
	/**
	 * Constructs a new {@code Transform} instance.
	 * <p>
	 * If {@code position} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Transform(position, new Quaternion4F());
	 * }
	 * </pre>
	 * 
	 * @param position a {@link Point3F} instance with the position associated with this {@code Transform} instance
	 * @throws NullPointerException thrown if, and only if, {@code position} is {@code null}
	 */
	public Transform(final Point3F position) {
		this(position, new Quaternion4F());
	}
	
	/**
	 * Constructs a new {@code Transform} instance.
	 * <p>
	 * If either {@code position} or {@code rotation} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Transform(position, rotation, new Vector3F(1.0F));
	 * }
	 * </pre>
	 * 
	 * @param position a {@link Point3F} instance with the position associated with this {@code Transform} instance
	 * @param rotation a {@link Quaternion4F} instance with the rotation associated with this {@code Transform} instance
	 * @throws NullPointerException thrown if, and only if, either {@code position} or {@code rotation} are {@code null}
	 */
	public Transform(final Point3F position, final Quaternion4F rotation) {
		this(position, rotation, new Vector3F(1.0F));
	}
	
	/**
	 * Constructs a new {@code Transform} instance.
	 * <p>
	 * If either {@code position}, {@code rotation} or {@code scale} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param position a {@link Point3F} instance with the position associated with this {@code Transform} instance
	 * @param rotation a {@link Quaternion4F} instance with the rotation associated with this {@code Transform} instance
	 * @param scale a {@link Vector3F} instance with the scale associated with this {@code Transform} instance
	 * @throws NullPointerException thrown if, and only if, either {@code position}, {@code rotation} or {@code scale} are {@code null}
	 */
	public Transform(final Point3F position, final Quaternion4F rotation, final Vector3F scale) {
		this.transformObservers = new ArrayList<>();
		this.objectToWorld = null;
		this.worldToObject = null;
		this.position = Objects.requireNonNull(position, "position == null");
		this.rotation = Objects.requireNonNull(rotation, "rotation == null");
		this.scale = Objects.requireNonNull(scale, "scale == null");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code List} with all {@link TransformObserver} instances currently associated with this {@code Transform} instance.
	 * <p>
	 * Modifying the returned {@code List} will not affect this {@code Transform} instance.
	 * 
	 * @return a {@code List} with all {@code TransformObserver} instances currently associated with this {@code Transform} instance
	 */
	public List<TransformObserver> getTransformObservers() {
		return new ArrayList<>(this.transformObservers);
	}
	
	/**
	 * Returns the {@link Matrix44F} instance that is used to transform from object space to world space and is associated with this {@code Transform} instance.
	 * <p>
	 * The {@code Matrix44F} instance will automatically be recalculated when a change has occurred.
	 * 
	 * @return the {@code Matrix44F} instance that is used to transform from object space to world space and is associated with this {@code Transform} instance
	 */
	public Matrix44F getObjectToWorld() {
		return doAttemptToCalculateObjectToWorld();
	}
	
	/**
	 * Returns the {@link Matrix44F} instance that is used to transform from world space to object space and is associated with this {@code Transform} instance.
	 * <p>
	 * The {@code Matrix44F} instance will automatically be recalculated when a change has occurred.
	 * <p>
	 * If the {@code Matrix44F} instance that transforms from object space to world space cannot be inverted, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @return the {@code Matrix44F} instance that is used to transform from world space to object space and is associated with this {@code Transform} instance
	 * @throws IllegalArgumentException thrown if, and only if, the {@code Matrix44F} instance that transforms from object space to world space cannot be inverted
	 */
	public Matrix44F getWorldToObject() {
		return doAttemptToCalculateWorldToObject();
	}
	
//	TODO: Add Javadocs!
	public Point3F getPosition() {
		return this.position;
	}
	
//	TODO: Add Javadocs!
	public Quaternion4F getRotation() {
		return this.rotation;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code Transform} instance.
	 * 
	 * @return a {@code String} representation of this {@code Transform} instance
	 */
	@Override
	public String toString() {
		return String.format("new Transform(%s, %s, %s)", this.position, this.rotation, this.scale);
	}
	
//	TODO: Add Javadocs!
	public Vector3F getScale() {
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
				if(this.objectToWorld != null && !this.objectToWorld.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
				
				if(this.worldToObject != null && !this.worldToObject.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
				
				if(!this.position.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
				
				if(!this.rotation.accept(nodeHierarchicalVisitor)) {
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
	 * Adds {@code transformObserver} to this {@code Transform} instance.
	 * <p>
	 * Returns {@code true} if, and only if, {@code transformObserver} was added, {@code false} otherwise.
	 * <p>
	 * If {@code transformObserver} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param transformObserver the {@link TransformObserver} instance to add
	 * @return {@code true} if, and only if, {@code transformObserver} was added, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, {@code transformObserver} is {@code null}
	 */
	public boolean addTransformObserver(final TransformObserver transformObserver) {
		return this.transformObservers.add(Objects.requireNonNull(transformObserver, "transformObserver == null"));
	}
	
	/**
	 * Compares {@code object} to this {@code Transform} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code Transform}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code Transform} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code Transform}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof Transform)) {
			return false;
		} else if(!Objects.equals(this.transformObservers, Transform.class.cast(object).transformObservers)) {
			return false;
		} else if(!Objects.equals(this.objectToWorld, Transform.class.cast(object).objectToWorld)) {
			return false;
		} else if(!Objects.equals(this.worldToObject, Transform.class.cast(object).worldToObject)) {
			return false;
		} else if(!Objects.equals(this.position, Transform.class.cast(object).position)) {
			return false;
		} else if(!Objects.equals(this.rotation, Transform.class.cast(object).rotation)) {
			return false;
		} else if(!Objects.equals(this.scale, Transform.class.cast(object).scale)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Removes {@code transformObserver} from this {@code Transform} instance, if present.
	 * <p>
	 * Returns {@code true} if, and only if, {@code transformObserver} was removed, {@code false} otherwise.
	 * <p>
	 * If {@code transformObserver} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param transformObserver the {@link TransformObserver} instance to remove
	 * @return {@code true} if, and only if, {@code transformObserver} was removed, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, {@code transformObserver} is {@code null}
	 */
	public boolean removeTransformObserver(final TransformObserver transformObserver) {
		return this.transformObservers.remove(Objects.requireNonNull(transformObserver, "transformObserver == null"));
	}
	
	/**
	 * Returns a hash code for this {@code Transform} instance.
	 * 
	 * @return a hash code for this {@code Transform} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.transformObservers, this.objectToWorld, this.worldToObject, this.position, this.rotation, this.scale);
	}
	
//	TODO: Add Javadocs!
	public void moveX(final float x) {
		final Point3F oldPosition = this.position;
		final Point3F newPosition = new Point3F(oldPosition.getX() + x, oldPosition.getY(), oldPosition.getZ());
		
		setPosition(newPosition);
	}
	
//	TODO: Add Javadocs!
	public void moveY(final float y) {
		final Point3F oldPosition = this.position;
		final Point3F newPosition = new Point3F(oldPosition.getX(), oldPosition.getY() + y, oldPosition.getZ());
		
		setPosition(newPosition);
	}
	
//	TODO: Add Javadocs!
	public void moveZ(final float z) {
		final Point3F oldPosition = this.position;
		final Point3F newPosition = new Point3F(oldPosition.getX(), oldPosition.getY(), oldPosition.getZ() + z);
		
		setPosition(newPosition);
	}
	
//	TODO: Add Javadocs!
	public void rotate(final Quaternion4F rotation) {
		setRotation(Quaternion4F.normalize(Quaternion4F.multiply(rotation, this.rotation)));
	}
	
//	TODO: Add Javadocs!
	public void setPosition(final Point3F position) {
		Objects.requireNonNull(position, "position == null");
		
		if(!Objects.equals(this.position, position)) {
			final Point3F oldPosition = this.position;
			final Point3F newPosition =      position;
			
			this.position = position;
			
			this.objectToWorld = null;
			this.worldToObject = null;
			
			for(final TransformObserver transformObserver : this.transformObservers) {
				transformObserver.onChangePosition(this, oldPosition, newPosition);
			}
		}
	}
	
//	TODO: Add Javadocs!
	public void setRotation(final Quaternion4F rotation) {
		Objects.requireNonNull(rotation, "rotation == null");
		
		if(!Objects.equals(this.rotation, rotation)) {
			final Quaternion4F oldRotation = this.rotation;
			final Quaternion4F newRotation =      rotation;
			
			this.rotation = rotation;
			
			this.objectToWorld = null;
			this.worldToObject = null;
			
			for(final TransformObserver transformObserver : this.transformObservers) {
				transformObserver.onChangeRotation(this, oldRotation, newRotation);
			}
		}
	}
	
//	TODO: Add Javadocs!
	public void setScale(final Vector3F scale) {
		Objects.requireNonNull(scale, "scale == null");
		
		if(!Objects.equals(this.scale, scale)) {
			final Vector3F oldScale = this.scale;
			final Vector3F newScale =      scale;
			
			this.scale = scale;
			
			this.objectToWorld = null;
			this.worldToObject = null;
			
			for(final TransformObserver transformObserver : this.transformObservers) {
				transformObserver.onChangeScale(this, oldScale, newScale);
			}
		}
	}
	
	/**
	 * Sets the {@code List} with all {@link TransformObserver} instances associated with this {@code Transform} instance to a copy of {@code transformObservers}.
	 * <p>
	 * If either {@code transformObservers} or at least one of its elements are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param transformObservers a {@code List} with all {@code TransformObserver} instances associated with this {@code Transform} instance
	 * @throws NullPointerException thrown if, and only if, either {@code transformObservers} or at least one of its elements are {@code null}
	 */
	public void setTransformObservers(final List<TransformObserver> transformObservers) {
		this.transformObservers = new ArrayList<>(ParameterArguments.requireNonNullList(transformObservers, "transformObservers"));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private Matrix44F doAttemptToCalculateObjectToWorld() {
		if(this.objectToWorld == null) {
			final Matrix44F translate = Matrix44F.translate(this.position);
			final Matrix44F rotate = Matrix44F.rotate(this.rotation);
			final Matrix44F scale = Matrix44F.scale(this.scale);
			
			this.objectToWorld = Matrix44F.multiply(Matrix44F.multiply(translate, rotate), scale);
			
			for(final TransformObserver transformObserver : this.transformObservers) {
				transformObserver.onChangeObjectToWorld(this, this.objectToWorld);
			}
		}
		
		return this.objectToWorld;
	}
	
	private Matrix44F doAttemptToCalculateWorldToObject() {
		if(this.objectToWorld != null && this.worldToObject == null) {
			this.worldToObject = Matrix44F.inverse(this.objectToWorld);
			
			for(final TransformObserver transformObserver : this.transformObservers) {
				transformObserver.onChangeWorldToObject(this, this.worldToObject);
			}
		}
		
		return this.worldToObject;
	}
}