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
import org.dayflower.geometry.Quaternion4F;
import org.dayflower.geometry.Vector3F;
import org.dayflower.geometry.Vector4F;

public final class Transform {
	private final Quaternion4F rotation;
	private final Vector3F position;
	private final Vector4F scale;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public Transform() {
		this(new Vector3F(0.0F, 0.0F, 0.0F));
	}
	
	public Transform(final Vector3F position) {
		this(position, new Quaternion4F(0.0F, 0.0F, 0.0F, 1.0F), new Vector4F(1.0F, 1.0F, 1.0F, 1.0F));
	}
	
	public Transform(final Vector3F position, final Quaternion4F rotation, final Vector4F scale) {
		this.position = position;
		this.rotation = rotation;
		this.scale = scale;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public Matrix44F getTransformation() {
		final Matrix44F translation = Matrix44F.translate(this.position.x, this.position.y, this.position.z);
		final Matrix44F rotation = Matrix44F.transpose(Matrix44F.rotate(this.rotation));
		final Matrix44F scale = Matrix44F.scale(this.scale.x, this.scale.y, this.scale.z);
		
		return Matrix44F.multiply(translation, Matrix44F.multiply(rotation, scale));
	}
	
	public Quaternion4F getLookAtRotation(final Vector3F point, final Vector3F up) {
		return Quaternion4F.from(Matrix44F.rotate(up, Vector3F.normalize(Vector3F.subtract(point, this.position))));
	}
	
	public Quaternion4F getRotation() {
		return this.rotation;
	}
	
	public Transform lookAt(final Vector3F point, final Vector3F up) {
		return rotate(getLookAtRotation(point, up));
	}
	
	public Transform rotate(final Quaternion4F rotation) {
		return new Transform(this.position, Quaternion4F.normalize(Quaternion4F.multiply(rotation, this.rotation)), this.scale);
	}
	
	public Transform setPosition(final Vector3F position) {
		return new Transform(position, this.rotation, this.scale);
	}
	
	public Vector3F getPosition() {
		return this.position;
	}
	
	public Vector4F getScale() {
		return this.scale;
	}
}