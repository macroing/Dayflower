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

import static org.dayflower.util.Floats.cos;
import static org.dayflower.util.Floats.equal;
import static org.dayflower.util.Floats.sin;
import static org.dayflower.util.Floats.sqrt;
import static org.dayflower.util.Floats.tan;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.Optional;

import org.dayflower.geometry.AngleF;
import org.dayflower.geometry.OrthoNormalBasis33F;
import org.dayflower.geometry.Point2F;
import org.dayflower.geometry.Point3F;
import org.dayflower.geometry.Ray3F;
import org.dayflower.geometry.SampleGeneratorF;
import org.dayflower.geometry.Vector3F;

//TODO: Add Javadocs!
public final class Camera {
	private static final int LENS_FISHEYE = 1;
	private static final int LENS_THIN = 2;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private AngleF fieldOfViewX;
	private AngleF fieldOfViewY;
	private AngleF pitch;
	private AngleF yaw;
	private OrthoNormalBasis33F orthoNormalBasis;
	private Point3F eye;
	private boolean isWalkLockEnabled;
	private float apertureRadius;
	private float focalDistance;
	private float resolutionX;
	private float resolutionY;
	private int lens;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public Camera() {
		this(new Point3F());
	}
	
//	TODO: Add Javadocs!
	public Camera(final Point3F eye) {
//		Initialize the default parameters:
		setApertureRadius(0.0F);
		setEye(eye);
		setFieldOfViewX(AngleF.degrees(90.0F));
		setFocalDistance(30.0F);
		setLensThin();
		setPitch(AngleF.pitch(Vector3F.x()));
		setResolution(800.0F, 800.0F);
		setWalkLockEnabled(true);
		setYaw(AngleF.yaw(Vector3F.y()));
		
//		Initialize the default parameters that require other parameters to be set:
		setFieldOfViewY();
		setOrthoNormalBasis();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public AngleF getFieldOfViewX() {
		return this.fieldOfViewX;
	}
	
//	TODO: Add Javadocs!
	public AngleF getFieldOfViewY() {
		return this.fieldOfViewY;
	}
	
//	TODO: Add Javadocs!
	public AngleF getPitch() {
		return this.pitch;
	}
	
//	TODO: Add Javadocs!
	public AngleF getYaw() {
		return this.yaw;
	}
	
//	TODO: Add Javadocs!
	public Optional<Ray3F> computePrimaryRay(final float pixelX, final float pixelY, final float sampleX, final float sampleY) {
		final float apertureRadius = this.apertureRadius;
		final float fieldOfViewX = tan(+this.fieldOfViewX.getRadians() * 0.5F);
		final float fieldOfViewY = tan(-this.fieldOfViewY.getRadians() * 0.5F);
		final float focalDistance = this.focalDistance;
		final float resolutionX = this.resolutionX;
		final float resolutionY = this.resolutionY;
		
		final Point3F eye = this.eye;
		
		final OrthoNormalBasis33F orthoNormalBasis = this.orthoNormalBasis;
		
		final Vector3F u = orthoNormalBasis.getU();
		final Vector3F v = orthoNormalBasis.getV();
		final Vector3F w = orthoNormalBasis.getW();
		
		final float sx = 2.0F * ((pixelX + sampleX) / (resolutionX - 1.0F)) - 1.0F;
		final float sy = 2.0F * ((pixelY + sampleY) / (resolutionY - 1.0F)) - 1.0F;
		
		float wFactor = 1.0F;
		
		if(isLensFisheye()) {
			final float dotProduct = sx * sx + sy * sy;
			
			if(dotProduct > 1.0F) {
				return Optional.empty();
			}
			
			wFactor = sqrt(1.0F - dotProduct);
		}
		
		final Point2F point = SampleGeneratorF.sampleDiskUniformDistribution();
		
		final Point3F pointOnPlaneOneUnitAwayFromEye = new Point3F(Vector3F.add(Vector3F.add(Vector3F.multiply(u, fieldOfViewX * sx), Vector3F.multiply(v, fieldOfViewY * sy)), Vector3F.add(new Vector3F(eye), Vector3F.multiply(w, wFactor))));
		final Point3F pointOnImagePlane = Point3F.add(eye, Vector3F.direction(eye, pointOnPlaneOneUnitAwayFromEye), focalDistance);
		final Point3F origin = apertureRadius > 0.00001F ? Point3F.add(eye, Vector3F.add(Vector3F.multiply(u, point.getU() * apertureRadius), Vector3F.multiply(v, point.getV() * apertureRadius))) : eye;
		
		final Vector3F direction = Vector3F.directionNormalized(origin, pointOnImagePlane);
		
		final Ray3F ray = new Ray3F(origin, direction);
		
		return Optional.of(ray);
	}
	
//	TODO: Add Javadocs!
	public OrthoNormalBasis33F getOrthoNormalBasis() {
		return this.orthoNormalBasis;
	}
	
//	TODO: Add Javadocs!
	public Point3F getEye() {
		return this.eye;
	}
	
//	TODO: Add Javadocs!
	@Override
	public String toString() {
		return String.format("new Camera(%s)", this.eye);
	}
	
//	TODO: Add Javadocs!
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof Camera)) {
			return false;
		} else if(!Objects.equals(this.fieldOfViewX, Camera.class.cast(object).fieldOfViewX)) {
			return false;
		} else if(!Objects.equals(this.fieldOfViewY, Camera.class.cast(object).fieldOfViewY)) {
			return false;
		} else if(!Objects.equals(this.pitch, Camera.class.cast(object).pitch)) {
			return false;
		} else if(!Objects.equals(this.yaw, Camera.class.cast(object).yaw)) {
			return false;
		} else if(!Objects.equals(this.orthoNormalBasis, Camera.class.cast(object).orthoNormalBasis)) {
			return false;
		} else if(!Objects.equals(this.eye, Camera.class.cast(object).eye)) {
			return false;
		} else if(this.isWalkLockEnabled != Camera.class.cast(object).isWalkLockEnabled) {
			return false;
		} else if(!equal(this.apertureRadius, Camera.class.cast(object).apertureRadius)) {
			return false;
		} else if(!equal(this.focalDistance, Camera.class.cast(object).focalDistance)) {
			return false;
		} else if(!equal(this.resolutionX, Camera.class.cast(object).resolutionX)) {
			return false;
		} else if(!equal(this.resolutionY, Camera.class.cast(object).resolutionY)) {
			return false;
		} else if(this.lens != Camera.class.cast(object).lens) {
			return false;
		} else {
			return true;
		}
	}
	
//	TODO: Add Javadocs!
	public boolean isLensFisheye() {
		return this.lens == LENS_FISHEYE;
	}
	
//	TODO: Add Javadocs!
	public boolean isLensThin() {
		return this.lens == LENS_THIN;
	}
	
//	TODO: Add Javadocs!
	public boolean isWalkLockEnabled() {
		return this.isWalkLockEnabled;
	}
	
//	TODO: Add Javadocs!
	public float getApertureRadius() {
		return this.apertureRadius;
	}
	
//	TODO: Add Javadocs!
	public float getFocalDistance() {
		return this.focalDistance;
	}
	
//	TODO: Add Javadocs!
	public float getResolutionX() {
		return this.resolutionX;
	}
	
//	TODO: Add Javadocs!
	public float getResolutionY() {
		return this.resolutionY;
	}
	
//	TODO: Add Javadocs!
	@Override
	public int hashCode() {
		return Objects.hash(this.fieldOfViewX, this.fieldOfViewY, this.pitch, this.yaw, this.orthoNormalBasis, this.eye, Boolean.valueOf(this.isWalkLockEnabled), Float.valueOf(this.apertureRadius), Float.valueOf(this.focalDistance), Float.valueOf(this.resolutionX), Float.valueOf(this.resolutionY), Integer.valueOf(this.lens));
	}
	
//	TODO: Add Javadocs!
	public void moveBackward(final float distance) {
		final Point3F eye = this.eye;
		
		final Vector3F w = this.orthoNormalBasis.getW();
		
		final boolean isWalkLockEnabled = this.isWalkLockEnabled;
		
		final float x = eye.getX() - w.getX() * distance * (isWalkLockEnabled ? 1.0F : 1.0F);
		final float y = eye.getY() - w.getY() * distance * (isWalkLockEnabled ? 0.0F : 1.0F);
		final float z = eye.getZ() - w.getZ() * distance * (isWalkLockEnabled ? 1.0F : 1.0F);
		
		this.eye = new Point3F(x, y, z);
	}
	
//	TODO: Add Javadocs!
	public void moveForward(final float distance) {
		final Point3F eye = this.eye;
		
		final Vector3F w = this.orthoNormalBasis.getW();
		
		final boolean isWalkLockEnabled = this.isWalkLockEnabled;
		
		final float x = eye.getX() + w.getX() * distance * (isWalkLockEnabled ? 1.0F : 1.0F);
		final float y = eye.getY() + w.getY() * distance * (isWalkLockEnabled ? 0.0F : 1.0F);
		final float z = eye.getZ() + w.getZ() * distance * (isWalkLockEnabled ? 1.0F : 1.0F);
		
		this.eye = new Point3F(x, y, z);
	}
	
//	TODO: Add Javadocs!
	public void moveLeft(final float distance) {
		final Point3F eye = this.eye;
		
		final Vector3F u = this.orthoNormalBasis.getU();
		
		final boolean isWalkLockEnabled = this.isWalkLockEnabled;
		
		final float x = eye.getX() - u.getX() * distance * (isWalkLockEnabled ? 1.0F : 1.0F);
		final float y = eye.getY() - u.getY() * distance * (isWalkLockEnabled ? 0.0F : 1.0F);
		final float z = eye.getZ() - u.getZ() * distance * (isWalkLockEnabled ? 1.0F : 1.0F);
		
		this.eye = new Point3F(x, y, z);
	}
	
//	TODO: Add Javadocs!
	public void moveRight(final float distance) {
		final Point3F eye = this.eye;
		
		final Vector3F u = this.orthoNormalBasis.getU();
		
		final boolean isWalkLockEnabled = this.isWalkLockEnabled;
		
		final float x = eye.getX() + u.getX() * distance * (isWalkLockEnabled ? 1.0F : 1.0F);
		final float y = eye.getY() + u.getY() * distance * (isWalkLockEnabled ? 0.0F : 1.0F);
		final float z = eye.getZ() + u.getZ() * distance * (isWalkLockEnabled ? 1.0F : 1.0F);
		
		this.eye = new Point3F(x, y, z);
	}
	
//	TODO: Add Javadocs!
	public void setApertureRadius(final float apertureRadius) {
		this.apertureRadius = apertureRadius;
	}
	
//	TODO: Add Javadocs!
	public void setEye(final Point3F eye) {
		this.eye = Objects.requireNonNull(eye, "eye == null");
	}
	
//	TODO: Add Javadocs!
	public void setFieldOfViewX() {
		this.fieldOfViewX = AngleF.fieldOfViewX(this.fieldOfViewY, this.resolutionX, this.resolutionY);
	}
	
//	TODO: Add Javadocs!
	public void setFieldOfViewX(final AngleF fieldOfViewX) {
		this.fieldOfViewX = Objects.requireNonNull(fieldOfViewX, "fieldOfViewX == null");
	}
	
//	TODO: Add Javadocs!
	public void setFieldOfViewY() {
		this.fieldOfViewY = AngleF.fieldOfViewY(this.fieldOfViewX, this.resolutionX, this.resolutionY);
	}
	
//	TODO: Add Javadocs!
	public void setFieldOfViewY(final AngleF fieldOfViewY) {
		this.fieldOfViewY = Objects.requireNonNull(fieldOfViewY, "fieldOfViewY == null");
	}
	
//	TODO: Add Javadocs!
	public void setFocalDistance(final float focalDistance) {
		this.focalDistance = focalDistance;
	}
	
//	TODO: Add Javadocs!
	public void setLensFisheye() {
		this.lens = LENS_FISHEYE;
	}
	
//	TODO: Add Javadocs!
	public void setLensThin() {
		this.lens = LENS_THIN;
	}
	
//	TODO: Add Javadocs!
	public void setOrthoNormalBasis() {
		final float pitch = this.pitch.getRadians();
		final float yaw = this.yaw.getRadians();
		
		final float x = sin(yaw) * cos(pitch);
		final float y = sin(pitch);
		final float z = cos(yaw) * cos(pitch);
		
		final Vector3F w = new Vector3F(x, y, z);
		final Vector3F v = Vector3F.v();
		
		this.orthoNormalBasis = new OrthoNormalBasis33F(w, v);
	}
	
//	TODO: Add Javadocs!
	public void setOrthoNormalBasis(final OrthoNormalBasis33F orthoNormalBasis) {
		this.orthoNormalBasis = Objects.requireNonNull(orthoNormalBasis, "orthoNormalBasis == null");
	}
	
//	TODO: Add Javadocs!
	public void setPitch(final AngleF pitch) {
		this.pitch = Objects.requireNonNull(pitch, "pitch == null");
	}
	
//	TODO: Add Javadocs!
	public void setResolution(final float resolutionX, final float resolutionY) {
		this.resolutionX = resolutionX;
		this.resolutionY = resolutionY;
	}
	
//	TODO: Add Javadocs!
	public void setResolutionX(final float resolutionX) {
		this.resolutionX = resolutionX;
	}
	
//	TODO: Add Javadocs!
	public void setResolutionY(final float resolutionY) {
		this.resolutionY = resolutionY;
	}
	
//	TODO: Add Javadocs!
	public void setWalkLockEnabled(final boolean isWalkLockEnabled) {
		this.isWalkLockEnabled = isWalkLockEnabled;
	}
	
//	TODO: Add Javadocs!
	public void setYaw(final AngleF yaw) {
		this.yaw = Objects.requireNonNull(yaw, "yaw == null");
	}
}