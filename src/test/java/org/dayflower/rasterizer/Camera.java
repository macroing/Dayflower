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
package org.dayflower.rasterizer;

import org.dayflower.geometry.Matrix44F;
import org.dayflower.geometry.Point3F;
import org.dayflower.geometry.Quaternion4F;
import org.dayflower.geometry.Vector3F;
import org.dayflower.scene.Transform;
import org.dayflower.utility.Floats;

public final class Camera {
	private static final Vector3F Y_AXIS = new Vector3F(0.0F, 1.0F, 0.0F);
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final Matrix44F projection;
	private final Transform transform;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public Camera(final Matrix44F projection) {
		this.projection = projection;
		this.transform = new Transform();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public Matrix44F getViewProjection() {
		final Matrix44F rotation = Matrix44F.rotate(this.transform.getRotation());
		
		final Point3F position = this.transform.getPosition();
		
		final float x = -position.x;
		final float y = -position.y;
		final float z = -position.z;
		
		final Matrix44F translation = Matrix44F.translate(x, y, z);
		final Matrix44F view = Matrix44F.multiply(rotation, translation);
		
		return Matrix44F.multiply(this.projection, view);
	}
	
	public void moveBack(final float distance) {
		doMove(doGetBack(), distance);
	}
	
	public void moveForward(final float distance) {
		doMove(doGetForward(), distance);
	}
	
	public void moveLeft(final float distance) {
		doMove(doGetLeft(), distance);
	}
	
	public void moveRight(final float distance) {
		doMove(doGetRight(), distance);
	}
	
	public void rotateRight(final float distance) {
		doRotate(doGetRight(), distance);
	}
	
	public void rotateUp(final float distance) {
		doRotate(Y_AXIS, distance);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private void doMove(final Vector3F direction, final float distance) {
		this.transform.setPosition(Point3F.add(this.transform.getPosition(), Vector3F.multiply(direction, distance)));
	}
	
	private void doRotate(final Vector3F axis, final float angle) {
		final float sinHalfAngle = Floats.sin(angle / 2.0F);
		final float cosHalfAngle = Floats.cos(angle / 2.0F);
		
		final float x = axis.x * sinHalfAngle;
		final float y = axis.y * sinHalfAngle;
		final float z = axis.z * sinHalfAngle;
		
		this.transform.rotate(new Quaternion4F(x, y, z, cosHalfAngle));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private Vector3F doGetBack() {
		return Vector3F.rotate(this.transform.getRotation(), Vector3F.z(-1.0F));
	}
	
	@SuppressWarnings("unused")
	private Vector3F doGetDown() {
		return Vector3F.rotate(this.transform.getRotation(), Vector3F.y(-1.0F));
	}
	
	private Vector3F doGetForward() {
		return Vector3F.rotate(this.transform.getRotation(), Vector3F.z(1.0F));
	}
	
	private Vector3F doGetLeft() {
		return Vector3F.rotate(this.transform.getRotation(), Vector3F.x(-1.0F));
	}
	
	private Vector3F doGetRight() {
		return Vector3F.rotate(this.transform.getRotation(), Vector3F.x(1.0F));
	}
	
	@SuppressWarnings("unused")
	private Vector3F doGetUp() {
		return Vector3F.rotate(this.transform.getRotation(), Vector3F.y(1.0F));
	}
}