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

import java.awt.event.KeyEvent;

import org.dayflower.geometry.Matrix44F;
import org.dayflower.geometry.Quaternion4F;
import org.dayflower.geometry.Vector3F;
import org.dayflower.utility.Floats;

public final class Camera {
	private static final Vector3F Y_AXIS = new Vector3F(0.0F, 1.0F, 0.0F);
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final Matrix44F projection;
	private Transform transform;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public Camera(final Matrix44F projection) {
		this.projection = projection;
		this.transform = new Transform();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public Matrix44F getViewProjection() {
		final Matrix44F rotation = Matrix44F.transpose(Matrix44F.rotate(Quaternion4F.conjugate(this.transform.getRotation())));
		
		final Vector3F position = Vector3F.multiply(this.transform.getPosition(), -1.0F);
		
		final float x = position.x;
		final float y = position.y;
		final float z = position.z;
		
		final Matrix44F translation = Matrix44F.translate(x, y, z);
		final Matrix44F view = Matrix44F.multiply(rotation, translation);
		
		return Matrix44F.multiply(this.projection, view);
	}
	
	public void update(final Input input, final float delta) {
		final float sensitivityX = 2.66F * delta;
		final float sensitivityY = 2.0F * delta;
		final float distance = 5.0F * delta;
		
		final float mouseMovementX = input.getAndResetMouseMovementX() * 0.5F * delta;
		final float mouseMovementY = input.getAndResetMouseMovementY() * 0.5F * delta;
		
		doRotate(Y_AXIS, mouseMovementX);
		doRotate(doGetRight(this.transform.getRotation()), mouseMovementY);
		
		if(input.getKey(KeyEvent.VK_ESCAPE)) {
			System.exit(0);
		}
		
		if(input.getKey(KeyEvent.VK_W)) {
			doMove(doGetForward(this.transform.getRotation()), distance);
		}
		
		if(input.getKey(KeyEvent.VK_S)) {
			doMove(doGetBack(this.transform.getRotation()), distance);
		}
		
		if(input.getKey(KeyEvent.VK_A)) {
			doMove(doGetLeft(this.transform.getRotation()), distance);
		}
		
		if(input.getKey(KeyEvent.VK_D)) {
			doMove(doGetRight(this.transform.getRotation()), distance);
		}
		
		if(input.getKey(KeyEvent.VK_RIGHT)) {
			doRotate(Y_AXIS, sensitivityX);
		}
		
		if(input.getKey(KeyEvent.VK_LEFT)) {
			doRotate(Y_AXIS, -sensitivityX);
		}
		
		if(input.getKey(KeyEvent.VK_DOWN)) {
			doRotate(doGetRight(this.transform.getRotation()), sensitivityY);
		}
		
		if(input.getKey(KeyEvent.VK_UP)) {
			doRotate(doGetRight(this.transform.getRotation()), -sensitivityY);
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private void doMove(final Vector3F direction, final float distance) {
		this.transform = this.transform.setPosition(Vector3F.add(this.transform.getPosition(), Vector3F.multiply(direction, distance)));
	}
	
	private void doRotate(final Vector3F axis, final float angle) {
		final float sinHalfAngle = Floats.sin(angle / 2.0F);
		final float cosHalfAngle = Floats.cos(angle / 2.0F);
		
		final float x = axis.x * sinHalfAngle;
		final float y = axis.y * sinHalfAngle;
		final float z = axis.z * sinHalfAngle;
		
		this.transform = this.transform.rotate(new Quaternion4F(x, y, z, cosHalfAngle));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static Vector3F doGetBack(final Quaternion4F q) {
		return doRotate(q, new Vector3F(0.0F, 0.0F, -1.0F));
	}
	
	@SuppressWarnings("unused")
	private static Vector3F doGetDown(final Quaternion4F q) {
		return doRotate(q, new Vector3F(0.0F, -1.0F, 0.0F));
	}
	
	private static Vector3F doGetForward(final Quaternion4F q) {
		return doRotate(q, new Vector3F(0.0F, 0.0F, 1.0F));
	}
	
	private static Vector3F doGetLeft(final Quaternion4F q) {
		return doRotate(q, new Vector3F(-1.0F, 0.0F, 0.0F));
	}
	
	private static Vector3F doGetRight(final Quaternion4F q) {
		return doRotate(q, new Vector3F(1.0F, 0.0F, 0.0F));
	}
	
	@SuppressWarnings("unused")
	private static Vector3F doGetUp(final Quaternion4F q) {
		return doRotate(q, new Vector3F(0.0F, 1.0F, 0.0F));
	}
	
	private static Vector3F doRotate(final Quaternion4F q, final Vector3F v) {
		final Quaternion4F q0 = Quaternion4F.conjugate(q);
		final Quaternion4F q1 = Quaternion4F.multiply(q, v);
		final Quaternion4F q2 = Quaternion4F.multiply(q1, q0);
		
		return new Vector3F(q2.x, q2.y, q2.z);
	}
}