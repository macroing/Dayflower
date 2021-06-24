/**
 * Copyright 2014 - 2021 J&#246;rgen Lundgren
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
package org.dayflower.scene.compiler;

import java.util.Objects;

import org.dayflower.geometry.Shape3F;
import org.dayflower.geometry.shape.Cone3F;
import org.dayflower.geometry.shape.Cylinder3F;
import org.dayflower.geometry.shape.Disk3F;
import org.dayflower.geometry.shape.Paraboloid3F;
import org.dayflower.geometry.shape.Plane3F;
import org.dayflower.geometry.shape.Rectangle3F;
import org.dayflower.geometry.shape.RectangularCuboid3F;
import org.dayflower.geometry.shape.Sphere3F;
import org.dayflower.geometry.shape.Torus3F;
import org.dayflower.geometry.shape.Triangle3F;
import org.dayflower.geometry.shape.TriangleMesh3F;

/**
 * A {@code CompiledShape3FCache} contains {@link Shape3F} instances in compiled form.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class CompiledShape3FCache {
	private float[] shape3FCone3FArray;
	private float[] shape3FCylinder3FArray;
	private float[] shape3FDisk3FArray;
	private float[] shape3FParaboloid3FArray;
	private float[] shape3FPlane3FArray;
	private float[] shape3FRectangle3FArray;
	private float[] shape3FRectangularCuboid3FArray;
	private float[] shape3FSphere3FArray;
	private float[] shape3FTorus3FArray;
	private float[] shape3FTriangle3FArray;
	private int[] shape3FTriangleMesh3FArray;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code CompiledShape3FCache} instance.
	 */
	public CompiledShape3FCache() {
		setShape3FCone3FArray(new float[1]);
		setShape3FCylinder3FArray(new float[1]);
		setShape3FDisk3FArray(new float[1]);
		setShape3FParaboloid3FArray(new float[1]);
		setShape3FPlane3FArray(new float[1]);
		setShape3FRectangle3FArray(new float[1]);
		setShape3FRectangularCuboid3FArray(new float[1]);
		setShape3FSphere3FArray(new float[1]);
		setShape3FTorus3FArray(new float[1]);
		setShape3FTriangle3FArray(new float[1]);
		setShape3FTriangleMesh3FArray(new int[1]);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code float[]} that contains all {@link Cone3F} instances in compiled form that are associated with this {@code CompiledShape3FCache} instance.
	 * 
	 * @return a {@code float[]} that contains all {@code Cone3F} instances in compiled form that are associated with this {@code CompiledShape3FCache} instance
	 */
	public float[] getShape3FCone3FArray() {
		return this.shape3FCone3FArray;
	}
	
	/**
	 * Returns a {@code float[]} that contains all {@link Cylinder3F} instances in compiled form that are associated with this {@code CompiledShape3FCache} instance.
	 * 
	 * @return a {@code float[]} that contains all {@code Cylinder3F} instances in compiled form that are associated with this {@code CompiledShape3FCache} instance
	 */
	public float[] getShape3FCylinder3FArray() {
		return this.shape3FCylinder3FArray;
	}
	
	/**
	 * Returns a {@code float[]} that contains all {@link Disk3F} instances in compiled form that are associated with this {@code CompiledShape3FCache} instance.
	 * 
	 * @return a {@code float[]} that contains all {@code Disk3F} instances in compiled form that are associated with this {@code CompiledShape3FCache} instance
	 */
	public float[] getShape3FDisk3FArray() {
		return this.shape3FDisk3FArray;
	}
	
	/**
	 * Returns a {@code float[]} that contains all {@link Paraboloid3F} instances in compiled form that are associated with this {@code CompiledShape3FCache} instance.
	 * 
	 * @return a {@code float[]} that contains all {@code Paraboloid3F} instances in compiled form that are associated with this {@code CompiledShape3FCache} instance
	 */
	public float[] getShape3FParaboloid3FArray() {
		return this.shape3FParaboloid3FArray;
	}
	
	/**
	 * Returns a {@code float[]} that contains all {@link Plane3F} instances in compiled form that are associated with this {@code CompiledShape3FCache} instance.
	 * 
	 * @return a {@code float[]} that contains all {@code Plane3F} instances in compiled form that are associated with this {@code CompiledShape3FCache} instance
	 */
	public float[] getShape3FPlane3FArray() {
		return this.shape3FPlane3FArray;
	}
	
	/**
	 * Returns a {@code float[]} that contains all {@link Rectangle3F} instances in compiled form that are associated with this {@code CompiledShape3FCache} instance.
	 * 
	 * @return a {@code float[]} that contains all {@code Rectangle3F} instances in compiled form that are associated with this {@code CompiledShape3FCache} instance
	 */
	public float[] getShape3FRectangle3FArray() {
		return this.shape3FRectangle3FArray;
	}
	
	/**
	 * Returns a {@code float[]} that contains all {@link RectangularCuboid3F} instances in compiled form that are associated with this {@code CompiledShape3FCache} instance.
	 * 
	 * @return a {@code float[]} that contains all {@code RectangularCuboid3F} instances in compiled form that are associated with this {@code CompiledShape3FCache} instance
	 */
	public float[] getShape3FRectangularCuboid3FArray() {
		return this.shape3FRectangularCuboid3FArray;
	}
	
	/**
	 * Returns a {@code float[]} that contains all {@link Sphere3F} instances in compiled form that are associated with this {@code CompiledShape3FCache} instance.
	 * 
	 * @return a {@code float[]} that contains all {@code Sphere3F} instances in compiled form that are associated with this {@code CompiledShape3FCache} instance
	 */
	public float[] getShape3FSphere3FArray() {
		return this.shape3FSphere3FArray;
	}
	
	/**
	 * Returns a {@code float[]} that contains all {@link Torus3F} instances in compiled form that are associated with this {@code CompiledShape3FCache} instance.
	 * 
	 * @return a {@code float[]} that contains all {@code Torus3F} instances in compiled form that are associated with this {@code CompiledShape3FCache} instance
	 */
	public float[] getShape3FTorus3FArray() {
		return this.shape3FTorus3FArray;
	}
	
	/**
	 * Returns a {@code float[]} that contains all {@link Triangle3F} instances in compiled form that are associated with this {@code CompiledShape3FCache} instance.
	 * 
	 * @return a {@code float[]} that contains all {@code Triangle3F} instances in compiled form that are associated with this {@code CompiledShape3FCache} instance
	 */
	public float[] getShape3FTriangle3FArray() {
		return this.shape3FTriangle3FArray;
	}
	
	/**
	 * Returns the {@link Cone3F} count in this {@code CompiledShape3FCache} instance.
	 * 
	 * @return the {@code Cone3F} count in this {@code CompiledShape3FCache} instance
	 */
	public int getShape3FCone3FCount() {
		return Structures.getStructureCount(this.shape3FCone3FArray, Cone3F.ARRAY_LENGTH);
	}
	
	/**
	 * Returns the absolute offset of {@code shape3FCone3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code shape3FCone3F} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param shape3FCone3F a {@link Cone3F} instance in compiled form
	 * @return the absolute offset of {@code shape3FCone3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found
	 * @throws NullPointerException thrown if, and only if, {@code shape3FCone3F} is {@code null}
	 */
	public int getShape3FCone3FOffsetAbsolute(final float[] shape3FCone3F) {
		return Structures.getStructureOffsetAbsolute(this.shape3FCone3FArray, Objects.requireNonNull(shape3FCone3F, "shape3FCone3F == null"), getShape3FCone3FCount(), Cone3F.ARRAY_LENGTH);
	}
	
	/**
	 * Returns the relative offset of {@code shape3FCone3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code shape3FCone3F} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param shape3FCone3F a {@link Cone3F} instance in compiled form
	 * @return the relative offset of {@code shape3FCone3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found
	 * @throws NullPointerException thrown if, and only if, {@code shape3FCone3F} is {@code null}
	 */
	public int getShape3FCone3FOffsetRelative(final float[] shape3FCone3F) {
		return Structures.getStructureOffsetRelative(this.shape3FCone3FArray, Objects.requireNonNull(shape3FCone3F, "shape3FCone3F == null"), getShape3FCone3FCount(), Cone3F.ARRAY_LENGTH);
	}
	
	/**
	 * Returns the {@link Cylinder3F} count in this {@code CompiledShape3FCache} instance.
	 * 
	 * @return the {@code Cylinder3F} count in this {@code CompiledShape3FCache} instance
	 */
	public int getShape3FCylinder3FCount() {
		return Structures.getStructureCount(this.shape3FCylinder3FArray, Cylinder3F.ARRAY_LENGTH);
	}
	
	/**
	 * Returns the absolute offset of {@code shape3FCylinder3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code shape3FCylinder3F} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param shape3FCylinder3F a {@link Cylinder3F} instance in compiled form
	 * @return the absolute offset of {@code shape3FCylinder3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found
	 * @throws NullPointerException thrown if, and only if, {@code shape3FCylinder3F} is {@code null}
	 */
	public int getShape3FCylinder3FOffsetAbsolute(final float[] shape3FCylinder3F) {
		return Structures.getStructureOffsetAbsolute(this.shape3FCylinder3FArray, Objects.requireNonNull(shape3FCylinder3F, "shape3FCylinder3F == null"), getShape3FCylinder3FCount(), Cylinder3F.ARRAY_LENGTH);
	}
	
	/**
	 * Returns the relative offset of {@code shape3FCylinder3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code shape3FCylinder3F} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param shape3FCylinder3F a {@link Cylinder3F} instance in compiled form
	 * @return the relative offset of {@code shape3FCylinder3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found
	 * @throws NullPointerException thrown if, and only if, {@code shape3FCylinder3F} is {@code null}
	 */
	public int getShape3FCylinder3FOffsetRelative(final float[] shape3FCylinder3F) {
		return Structures.getStructureOffsetRelative(this.shape3FCylinder3FArray, Objects.requireNonNull(shape3FCylinder3F, "shape3FCylinder3F == null"), getShape3FCylinder3FCount(), Cylinder3F.ARRAY_LENGTH);
	}
	
	/**
	 * Returns the {@link Disk3F} count in this {@code CompiledShape3FCache} instance.
	 * 
	 * @return the {@code Disk3F} count in this {@code CompiledShape3FCache} instance
	 */
	public int getShape3FDisk3FCount() {
		return Structures.getStructureCount(this.shape3FDisk3FArray, Disk3F.ARRAY_LENGTH);
	}
	
	/**
	 * Returns the absolute offset of {@code shape3FDisk3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code shape3FDisk3F} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param shape3FDisk3F a {@link Disk3F} instance in compiled form
	 * @return the absolute offset of {@code shape3FDisk3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found
	 * @throws NullPointerException thrown if, and only if, {@code shape3FDisk3F} is {@code null}
	 */
	public int getShape3FDisk3FOffsetAbsolute(final float[] shape3FDisk3F) {
		return Structures.getStructureOffsetAbsolute(this.shape3FDisk3FArray, Objects.requireNonNull(shape3FDisk3F, "shape3FDisk3F == null"), getShape3FDisk3FCount(), Disk3F.ARRAY_LENGTH);
	}
	
	/**
	 * Returns the relative offset of {@code shape3FDisk3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code shape3FDisk3F} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param shape3FDisk3F a {@link Disk3F} instance in compiled form
	 * @return the relative offset of {@code shape3FDisk3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found
	 * @throws NullPointerException thrown if, and only if, {@code shape3FDisk3F} is {@code null}
	 */
	public int getShape3FDisk3FOffsetRelative(final float[] shape3FDisk3F) {
		return Structures.getStructureOffsetRelative(this.shape3FDisk3FArray, Objects.requireNonNull(shape3FDisk3F, "shape3FDisk3F == null"), getShape3FDisk3FCount(), Disk3F.ARRAY_LENGTH);
	}
	
	/**
	 * Returns the {@link Paraboloid3F} count in this {@code CompiledShape3FCache} instance.
	 * 
	 * @return the {@code Paraboloid3F} count in this {@code CompiledShape3FCache} instance
	 */
	public int getShape3FParaboloid3FCount() {
		return Structures.getStructureCount(this.shape3FParaboloid3FArray, Paraboloid3F.ARRAY_LENGTH);
	}
	
	/**
	 * Returns the absolute offset of {@code shape3FParaboloid3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code shape3FParaboloid3F} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param shape3FParaboloid3F a {@link Paraboloid3F} instance in compiled form
	 * @return the absolute offset of {@code shape3FParaboloid3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found
	 * @throws NullPointerException thrown if, and only if, {@code shape3FParaboloid3F} is {@code null}
	 */
	public int getShape3FParaboloid3FOffsetAbsolute(final float[] shape3FParaboloid3F) {
		return Structures.getStructureOffsetAbsolute(this.shape3FParaboloid3FArray, Objects.requireNonNull(shape3FParaboloid3F, "shape3FParaboloid3F == null"), getShape3FParaboloid3FCount(), Paraboloid3F.ARRAY_LENGTH);
	}
	
	/**
	 * Returns the relative offset of {@code shape3FParaboloid3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code shape3FParaboloid3F} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param shape3FParaboloid3F a {@link Paraboloid3F} instance in compiled form
	 * @return the relative offset of {@code shape3FParaboloid3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found
	 * @throws NullPointerException thrown if, and only if, {@code shape3FParaboloid3F} is {@code null}
	 */
	public int getShape3FParaboloid3FOffsetRelative(final float[] shape3FParaboloid3F) {
		return Structures.getStructureOffsetRelative(this.shape3FParaboloid3FArray, Objects.requireNonNull(shape3FParaboloid3F, "shape3FParaboloid3F == null"), getShape3FParaboloid3FCount(), Paraboloid3F.ARRAY_LENGTH);
	}
	
	/**
	 * Returns the {@link Plane3F} count in this {@code CompiledShape3FCache} instance.
	 * 
	 * @return the {@code Plane3F} count in this {@code CompiledShape3FCache} instance
	 */
	public int getShape3FPlane3FCount() {
		return Structures.getStructureCount(this.shape3FPlane3FArray, Plane3F.ARRAY_LENGTH);
	}
	
	/**
	 * Returns the absolute offset of {@code shape3FPlane3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code shape3FPlane3F} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param shape3FPlane3F a {@link Plane3F} instance in compiled form
	 * @return the absolute offset of {@code shape3FPlane3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found
	 * @throws NullPointerException thrown if, and only if, {@code shape3FPlane3F} is {@code null}
	 */
	public int getShape3FPlane3FOffsetAbsolute(final float[] shape3FPlane3F) {
		return Structures.getStructureOffsetAbsolute(this.shape3FPlane3FArray, Objects.requireNonNull(shape3FPlane3F, "shape3FPlane3F == null"), getShape3FPlane3FCount(), Plane3F.ARRAY_LENGTH);
	}
	
	/**
	 * Returns the relative offset of {@code shape3FPlane3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code shape3FPlane3F} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param shape3FPlane3F a {@link Plane3F} instance in compiled form
	 * @return the relative offset of {@code shape3FPlane3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found
	 * @throws NullPointerException thrown if, and only if, {@code shape3FPlane3F} is {@code null}
	 */
	public int getShape3FPlane3FOffsetRelative(final float[] shape3FPlane3F) {
		return Structures.getStructureOffsetRelative(this.shape3FPlane3FArray, Objects.requireNonNull(shape3FPlane3F, "shape3FPlane3F == null"), getShape3FPlane3FCount(), Plane3F.ARRAY_LENGTH);
	}
	
	/**
	 * Returns the {@link Rectangle3F} count in this {@code CompiledShape3FCache} instance.
	 * 
	 * @return the {@code Rectangle3F} count in this {@code CompiledShape3FCache} instance
	 */
	public int getShape3FRectangle3FCount() {
		return Structures.getStructureCount(this.shape3FRectangle3FArray, Rectangle3F.ARRAY_LENGTH);
	}
	
	/**
	 * Returns the absolute offset of {@code shape3FRectangle3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code shape3FRectangle3F} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param shape3FRectangle3F a {@link Rectangle3F} instance in compiled form
	 * @return the absolute offset of {@code shape3FRectangle3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found
	 * @throws NullPointerException thrown if, and only if, {@code shape3FRectangle3F} is {@code null}
	 */
	public int getShape3FRectangle3FOffsetAbsolute(final float[] shape3FRectangle3F) {
		return Structures.getStructureOffsetAbsolute(this.shape3FRectangle3FArray, Objects.requireNonNull(shape3FRectangle3F, "shape3FRectangle3F == null"), getShape3FRectangle3FCount(), Rectangle3F.ARRAY_LENGTH);
	}
	
	/**
	 * Returns the relative offset of {@code shape3FRectangle3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code shape3FRectangle3F} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param shape3FRectangle3F a {@link Rectangle3F} instance in compiled form
	 * @return the relative offset of {@code shape3FRectangle3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found
	 * @throws NullPointerException thrown if, and only if, {@code shape3FRectangle3F} is {@code null}
	 */
	public int getShape3FRectangle3FOffsetRelative(final float[] shape3FRectangle3F) {
		return Structures.getStructureOffsetRelative(this.shape3FRectangle3FArray, Objects.requireNonNull(shape3FRectangle3F, "shape3FRectangle3F == null"), getShape3FRectangle3FCount(), Rectangle3F.ARRAY_LENGTH);
	}
	
	/**
	 * Returns the {@link RectangularCuboid3F} count in this {@code CompiledShape3FCache} instance.
	 * 
	 * @return the {@code RectangularCuboid3F} count in this {@code CompiledShape3FCache} instance
	 */
	public int getShape3FRectangularCuboid3FCount() {
		return Structures.getStructureCount(this.shape3FRectangularCuboid3FArray, RectangularCuboid3F.ARRAY_LENGTH);
	}
	
	/**
	 * Returns the absolute offset of {@code shape3FRectangularCuboid3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code shape3FRectangularCuboid3F} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param shape3FRectangularCuboid3F a {@link RectangularCuboid3F} instance in compiled form
	 * @return the absolute offset of {@code shape3FRectangularCuboid3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found
	 * @throws NullPointerException thrown if, and only if, {@code shape3FRectangularCuboid3F} is {@code null}
	 */
	public int getShape3FRectangularCuboid3FOffsetAbsolute(final float[] shape3FRectangularCuboid3F) {
		return Structures.getStructureOffsetAbsolute(this.shape3FRectangularCuboid3FArray, Objects.requireNonNull(shape3FRectangularCuboid3F, "shape3FRectangularCuboid3F == null"), getShape3FRectangularCuboid3FCount(), RectangularCuboid3F.ARRAY_LENGTH);
	}
	
	/**
	 * Returns the relative offset of {@code shape3FRectangularCuboid3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code shape3FRectangularCuboid3F} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param shape3FRectangularCuboid3F a {@link RectangularCuboid3F} instance in compiled form
	 * @return the relative offset of {@code shape3FRectangularCuboid3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found
	 * @throws NullPointerException thrown if, and only if, {@code shape3FRectangularCuboid3F} is {@code null}
	 */
	public int getShape3FRectangularCuboid3FOffsetRelative(final float[] shape3FRectangularCuboid3F) {
		return Structures.getStructureOffsetRelative(this.shape3FRectangularCuboid3FArray, Objects.requireNonNull(shape3FRectangularCuboid3F, "shape3FRectangularCuboid3F == null"), getShape3FRectangularCuboid3FCount(), RectangularCuboid3F.ARRAY_LENGTH);
	}
	
	/**
	 * Returns the {@link Sphere3F} count in this {@code CompiledShape3FCache} instance.
	 * 
	 * @return the {@code Sphere3F} count in this {@code CompiledShape3FCache} instance
	 */
	public int getShape3FSphere3FCount() {
		return Structures.getStructureCount(this.shape3FSphere3FArray, Sphere3F.ARRAY_LENGTH);
	}
	
	/**
	 * Returns the absolute offset of {@code shape3FSphere3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code shape3FSphere3F} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param shape3FSphere3F a {@link Sphere3F} instance in compiled form
	 * @return the absolute offset of {@code shape3FSphere3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found
	 * @throws NullPointerException thrown if, and only if, {@code shape3FSphere3F} is {@code null}
	 */
	public int getShape3FSphere3FOffsetAbsolute(final float[] shape3FSphere3F) {
		return Structures.getStructureOffsetAbsolute(this.shape3FSphere3FArray, Objects.requireNonNull(shape3FSphere3F, "shape3FSphere3F == null"), getShape3FSphere3FCount(), Sphere3F.ARRAY_LENGTH);
	}
	
	/**
	 * Returns the relative offset of {@code shape3FSphere3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code shape3FSphere3F} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param shape3FSphere3F a {@link Sphere3F} instance in compiled form
	 * @return the relative offset of {@code shape3FSphere3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found
	 * @throws NullPointerException thrown if, and only if, {@code shape3FSphere3F} is {@code null}
	 */
	public int getShape3FSphere3FOffsetRelative(final float[] shape3FSphere3F) {
		return Structures.getStructureOffsetRelative(this.shape3FSphere3FArray, Objects.requireNonNull(shape3FSphere3F, "shape3FSphere3F == null"), getShape3FSphere3FCount(), Sphere3F.ARRAY_LENGTH);
	}
	
	/**
	 * Returns the {@link Torus3F} count in this {@code CompiledShape3FCache} instance.
	 * 
	 * @return the {@code Torus3F} count in this {@code CompiledShape3FCache} instance
	 */
	public int getShape3FTorus3FCount() {
		return Structures.getStructureCount(this.shape3FTorus3FArray, Torus3F.ARRAY_LENGTH);
	}
	
	/**
	 * Returns the absolute offset of {@code shape3FTorus3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code shape3FTorus3F} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param shape3FTorus3F a {@link Torus3F} instance in compiled form
	 * @return the absolute offset of {@code shape3FTorus3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found
	 * @throws NullPointerException thrown if, and only if, {@code shape3FTorus3F} is {@code null}
	 */
	public int getShape3FTorus3FOffsetAbsolute(final float[] shape3FTorus3F) {
		return Structures.getStructureOffsetAbsolute(this.shape3FTorus3FArray, Objects.requireNonNull(shape3FTorus3F, "shape3FTorus3F == null"), getShape3FTorus3FCount(), Torus3F.ARRAY_LENGTH);
	}
	
	/**
	 * Returns the relative offset of {@code shape3FTorus3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code shape3FTorus3F} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param shape3FTorus3F a {@link Torus3F} instance in compiled form
	 * @return the relative offset of {@code shape3FTorus3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found
	 * @throws NullPointerException thrown if, and only if, {@code shape3FTorus3F} is {@code null}
	 */
	public int getShape3FTorus3FOffsetRelative(final float[] shape3FTorus3F) {
		return Structures.getStructureOffsetRelative(this.shape3FTorus3FArray, Objects.requireNonNull(shape3FTorus3F, "shape3FTorus3F == null"), getShape3FTorus3FCount(), Torus3F.ARRAY_LENGTH);
	}
	
	/**
	 * Returns the {@link Triangle3F} count in this {@code CompiledShape3FCache} instance.
	 * 
	 * @return the {@code Triangle3F} count in this {@code CompiledShape3FCache} instance
	 */
	public int getShape3FTriangle3FCount() {
		return Structures.getStructureCount(this.shape3FTriangle3FArray, Triangle3F.ARRAY_LENGTH);
	}
	
	/**
	 * Returns the absolute offset of {@code shape3FTriangle3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code shape3FTriangle3F} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param shape3FTriangle3F a {@link Triangle3F} instance in compiled form
	 * @return the absolute offset of {@code shape3FTriangle3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found
	 * @throws NullPointerException thrown if, and only if, {@code shape3FTriangle3F} is {@code null}
	 */
	public int getShape3FTriangle3FOffsetAbsolute(final float[] shape3FTriangle3F) {
		return Structures.getStructureOffsetAbsolute(this.shape3FTriangle3FArray, Objects.requireNonNull(shape3FTriangle3F, "shape3FTriangle3F == null"), getShape3FTriangle3FCount(), Triangle3F.ARRAY_LENGTH);
	}
	
	/**
	 * Returns the relative offset of {@code shape3FTriangle3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code shape3FTriangle3F} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param shape3FTriangle3F a {@link Triangle3F} instance in compiled form
	 * @return the relative offset of {@code shape3FTriangle3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found
	 * @throws NullPointerException thrown if, and only if, {@code shape3FTriangle3F} is {@code null}
	 */
	public int getShape3FTriangle3FOffsetRelative(final float[] shape3FTriangle3F) {
		return Structures.getStructureOffsetRelative(this.shape3FTriangle3FArray, Objects.requireNonNull(shape3FTriangle3F, "shape3FTriangle3F == null"), getShape3FTriangle3FCount(), Triangle3F.ARRAY_LENGTH);
	}
	
	/**
	 * Returns an {@code int[]} that contains all {@link TriangleMesh3F} instances in compiled form that are associated with this {@code CompiledShape3FCache} instance.
	 * 
	 * @return an {@code int[]} that contains all {@code TriangleMesh3F} instances in compiled form that are associated with this {@code CompiledShape3FCache} instance
	 */
	public int[] getShape3FTriangleMesh3FArray() {
		return this.shape3FTriangleMesh3FArray;
	}
	
	/**
	 * Sets all {@link Cone3F} instances in compiled form to {@code shape3FCone3FArray}.
	 * <p>
	 * If {@code shape3FCone3FArray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param shape3FCone3FArray the {@code Cone3F} instances in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code shape3FCone3FArray} is {@code null}
	 */
	public void setShape3FCone3FArray(final float[] shape3FCone3FArray) {
		this.shape3FCone3FArray = Objects.requireNonNull(shape3FCone3FArray, "shape3FCone3FArray == null");
	}
	
	/**
	 * Sets all {@link Cylinder3F} instances in compiled form to {@code shape3FCylinder3FArray}.
	 * <p>
	 * If {@code shape3FCylinder3FArray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param shape3FCylinder3FArray the {@code Cylinder3F} instances in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code shape3FCylinder3FArray} is {@code null}
	 */
	public void setShape3FCylinder3FArray(final float[] shape3FCylinder3FArray) {
		this.shape3FCylinder3FArray = Objects.requireNonNull(shape3FCylinder3FArray, "shape3FCylinder3FArray == null");
	}
	
	/**
	 * Sets all {@link Disk3F} instances in compiled form to {@code shape3FDisk3FArray}.
	 * <p>
	 * If {@code shape3FDisk3FArray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param shape3FDisk3FArray the {@code Disk3F} instances in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code shape3FDisk3FArray} is {@code null}
	 */
	public void setShape3FDisk3FArray(final float[] shape3FDisk3FArray) {
		this.shape3FDisk3FArray = Objects.requireNonNull(shape3FDisk3FArray, "shape3FDisk3FArray == null");
	}
	
	/**
	 * Sets all {@link Paraboloid3F} instances in compiled form to {@code shape3FParaboloid3FArray}.
	 * <p>
	 * If {@code shape3FParaboloid3FArray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param shape3FParaboloid3FArray the {@code Paraboloid3F} instances in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code shape3FParaboloid3FArray} is {@code null}
	 */
	public void setShape3FParaboloid3FArray(final float[] shape3FParaboloid3FArray) {
		this.shape3FParaboloid3FArray = Objects.requireNonNull(shape3FParaboloid3FArray, "shape3FParaboloid3FArray == null");
	}
	
	/**
	 * Sets all {@link Plane3F} instances in compiled form to {@code shape3FPlane3FArray}.
	 * <p>
	 * If {@code shape3FPlane3FArray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param shape3FPlane3FArray the {@code Plane3F} instances in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code shape3FPlane3FArray} is {@code null}
	 */
	public void setShape3FPlane3FArray(final float[] shape3FPlane3FArray) {
		this.shape3FPlane3FArray = Objects.requireNonNull(shape3FPlane3FArray, "shape3FPlane3FArray == null");
	}
	
	/**
	 * Sets all {@link Rectangle3F} instances in compiled form to {@code shape3FRectangle3FArray}.
	 * <p>
	 * If {@code shape3FRectangle3FArray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param shape3FRectangle3FArray the {@code Rectangle3F} instances in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code shape3FRectangle3FArray} is {@code null}
	 */
	public void setShape3FRectangle3FArray(final float[] shape3FRectangle3FArray) {
		this.shape3FRectangle3FArray = Objects.requireNonNull(shape3FRectangle3FArray, "shape3FRectangle3FArray == null");
	}
	
	/**
	 * Sets all {@link RectangularCuboid3F} instances in compiled form to {@code shape3FRectangularCuboid3FArray}.
	 * <p>
	 * If {@code shape3FRectangularCuboid3FArray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param shape3FRectangularCuboid3FArray the {@code RectangularCuboid3F} instances in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code shape3FRectangularCuboid3FArray} is {@code null}
	 */
	public void setShape3FRectangularCuboid3FArray(final float[] shape3FRectangularCuboid3FArray) {
		this.shape3FRectangularCuboid3FArray = Objects.requireNonNull(shape3FRectangularCuboid3FArray, "shape3FRectangularCuboid3FArray == null");
	}
	
	/**
	 * Sets all {@link Sphere3F} instances in compiled form to {@code shape3FSphere3FArray}.
	 * <p>
	 * If {@code shape3FSphere3FArray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param shape3FSphere3FArray the {@code Sphere3F} instances in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code shape3FSphere3FArray} is {@code null}
	 */
	public void setShape3FSphere3FArray(final float[] shape3FSphere3FArray) {
		this.shape3FSphere3FArray = Objects.requireNonNull(shape3FSphere3FArray, "shape3FSphere3FArray == null");
	}
	
	/**
	 * Sets all {@link Torus3F} instances in compiled form to {@code shape3FTorus3FArray}.
	 * <p>
	 * If {@code shape3FTorus3FArray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param shape3FTorus3FArray the {@code Torus3F} instances in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code shape3FTorus3FArray} is {@code null}
	 */
	public void setShape3FTorus3FArray(final float[] shape3FTorus3FArray) {
		this.shape3FTorus3FArray = Objects.requireNonNull(shape3FTorus3FArray, "shape3FTorus3FArray == null");
	}
	
	/**
	 * Sets all {@link Triangle3F} instances in compiled form to {@code shape3FTriangle3FArray}.
	 * <p>
	 * If {@code shape3FTriangle3FArray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param shape3FTriangle3FArray the {@code Triangle3F} instances in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code shape3FTriangle3FArray} is {@code null}
	 */
	public void setShape3FTriangle3FArray(final float[] shape3FTriangle3FArray) {
		this.shape3FTriangle3FArray = Objects.requireNonNull(shape3FTriangle3FArray, "shape3FTriangle3FArray == null");
	}
	
	/**
	 * Sets all {@link TriangleMesh3F} instances in compiled form to {@code shape3FTriangleMesh3FArray}.
	 * <p>
	 * If {@code shape3FTriangleMesh3FArray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param shape3FTriangleMesh3FArray the {@code TriangleMesh3F} instances in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code shape3FTriangleMesh3FArray} is {@code null}
	 */
	public void setShape3FTriangleMesh3FArray(final int[] shape3FTriangleMesh3FArray) {
		this.shape3FTriangleMesh3FArray = Objects.requireNonNull(shape3FTriangleMesh3FArray, "shape3FTriangleMesh3FArray == null");
	}
}