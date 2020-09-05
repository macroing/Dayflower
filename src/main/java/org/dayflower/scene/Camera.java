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

import java.util.Objects;
import java.util.Optional;

import org.dayflower.geometry.AngleF;
import org.dayflower.geometry.OrthonormalBasis33F;
import org.dayflower.geometry.Point2F;
import org.dayflower.geometry.Point3F;
import org.dayflower.geometry.Ray3F;
import org.dayflower.geometry.SampleGeneratorF;
import org.dayflower.geometry.Vector3F;

/**
 * A {@code Camera} represents a camera.
 * <p>
 * This class is mutable and therefore not thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class Camera {
	private static final int LENS_FISHEYE = 1;
	private static final int LENS_THIN = 2;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private AngleF fieldOfViewX;
	private AngleF fieldOfViewY;
	private AngleF pitch;
	private AngleF yaw;
	private OrthonormalBasis33F orthonormalBasis;
	private Point3F eye;
	private boolean isWalkLockEnabled;
	private float apertureRadius;
	private float focalDistance;
	private float resolutionX;
	private float resolutionY;
	private int lens;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code Camera} instance with an eye of {@code new Point3F()}.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Camera(new Point3F());
	 * }
	 * </pre>
	 */
	public Camera() {
		this(new Point3F());
	}
	
	/**
	 * Constructs a new {@code Camera} instance with an eye of {@code eye}.
	 * <p>
	 * If {@code eye} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param eye a {@link Point3F} instance representing the eye that is associated with this {@code Camera} instance
	 * @throws NullPointerException thrown if, and only if, {@code eye} is {@code null}
	 */
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
		setOrthonormalBasis();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns an {@link AngleF} instance representing the field of view on the X-axis that is associated with this {@code Camera} instance.
	 * 
	 * @return an {@code AngleF} instance representing the field of view on the X-axis that is associated with this {@code Camera} instance
	 */
	public AngleF getFieldOfViewX() {
		return this.fieldOfViewX;
	}
	
	/**
	 * Returns an {@link AngleF} instance representing the field of view on the Y-axis that is associated with this {@code Camera} instance.
	 * 
	 * @return an {@code AngleF} instance representing the field of view on the Y-axis that is associated with this {@code Camera} instance
	 */
	public AngleF getFieldOfViewY() {
		return this.fieldOfViewY;
	}
	
	/**
	 * Returns an {@link AngleF} instance representing the pitch that is associated with this {@code Camera} instance.
	 * 
	 * @return an {@code AngleF} instance representing the pitch that is associated with this {@code Camera} instance
	 */
	public AngleF getPitch() {
		return this.pitch;
	}
	
	/**
	 * Returns an {@link AngleF} instance representing the yaw that is associated with this {@code Camera} instance.
	 * 
	 * @return an {@code AngleF} instance representing the yaw that is associated with this {@code Camera} instance
	 */
	public AngleF getYaw() {
		return this.yaw;
	}
	
	/**
	 * Creates a new primary {@link Ray3F} instance given {@code pixelX} and {@code pixelY} as the pixel coordinates and {@code sampleX} and {@code sampleY} as the sample coordinates within a pixel.
	 * <p>
	 * Returns an {@code Optional} with an optional {@code Ray3F} instance.
	 * 
	 * @param pixelX the X-coordinate of the pixel
	 * @param pixelY the Y-coordinate of the pixel
	 * @param sampleX the X-coordinate of the sample inside the pixel
	 * @param sampleY the Y-coordinate of the sample inside the pixel
	 * @return an {@code Optional} with an optional {@code Ray3F} instance
	 */
	public Optional<Ray3F> createPrimaryRay(final float pixelX, final float pixelY, final float sampleX, final float sampleY) {
		final float apertureRadius = this.apertureRadius;
		final float fieldOfViewX = tan(+this.fieldOfViewX.getRadians() * 0.5F);
		final float fieldOfViewY = tan(-this.fieldOfViewY.getRadians() * 0.5F);
		final float focalDistance = this.focalDistance;
		final float resolutionX = this.resolutionX;
		final float resolutionY = this.resolutionY;
		
		final Point3F eye = this.eye;
		
		final OrthonormalBasis33F orthonormalBasis = this.orthonormalBasis;
		
		final Vector3F u = orthonormalBasis.getU();
		final Vector3F v = orthonormalBasis.getV();
		final Vector3F w = orthonormalBasis.getW();
		
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
	
	/**
	 * Returns an {@link OrthonormalBasis33F} instance representing the orthonormal basis that is associated with this {@code Camera} instance.
	 * 
	 * @return an {@code OrthonormalBasis33F} instance representing the orthonormal basis that is associated with this {@code Camera} instance
	 */
	public OrthonormalBasis33F getOrthonormalBasis() {
		return this.orthonormalBasis;
	}
	
	/**
	 * Returns a {@link Point3F} instance representing the eye that is associated with this {@code Camera} instance.
	 * 
	 * @return a {@code Point3F} instance representing the eye that is associated with this {@code Camera} instance
	 */
	public Point3F getEye() {
		return this.eye;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code Camera} instance.
	 * 
	 * @return a {@code String} representation of this {@code Camera} instance
	 */
	@Override
	public String toString() {
		return String.format("new Camera(%s)", this.eye);
	}
	
	/**
	 * Compares {@code object} to this {@code Camera} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code Camera}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code Camera} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code Camera}, and their respective values are equal, {@code false} otherwise
	 */
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
		} else if(!Objects.equals(this.orthonormalBasis, Camera.class.cast(object).orthonormalBasis)) {
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
	
	/**
	 * Returns {@code true} if, and only if, the lens associated with this {@code Camera} instance is a fisheye lens, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, the lens associated with this {@code Camera} instance is a fisheye lens, {@code false} otherwise
	 */
	public boolean isLensFisheye() {
		return this.lens == LENS_FISHEYE;
	}
	
	/**
	 * Returns {@code true} if, and only if, the lens associated with this {@code Camera} instance is a thin lens, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, the lens associated with this {@code Camera} instance is a thin lens, {@code false} otherwise
	 */
	public boolean isLensThin() {
		return this.lens == LENS_THIN;
	}
	
	/**
	 * Returns the walk lock state associated with this {@code Camera} instance.
	 * 
	 * @return the walk lock state associated with this {@code Camera} instance
	 */
	public boolean isWalkLockEnabled() {
		return this.isWalkLockEnabled;
	}
	
	/**
	 * Returns a {@code float} representing the aperture radius that is associated with this {@code Camera} instance.
	 * 
	 * @return a {@code float} representing the aperture radius that is associated with this {@code Camera} instance
	 */
	public float getApertureRadius() {
		return this.apertureRadius;
	}
	
	/**
	 * Returns a {@code float} representing the focal distance that is associated with this {@code Camera} instance.
	 * 
	 * @return a {@code float} representing the focal distance that is associated with this {@code Camera} instance
	 */
	public float getFocalDistance() {
		return this.focalDistance;
	}
	
	/**
	 * Returns a {@code float} representing the resolution on the X-axis that is associated with this {@code Camera} instance.
	 * 
	 * @return a {@code float} representing the resolution on the X-axis that is associated with this {@code Camera} instance
	 */
	public float getResolutionX() {
		return this.resolutionX;
	}
	
	/**
	 * Returns a {@code float} representing the resolution on the Y-axis that is associated with this {@code Camera} instance.
	 * 
	 * @return a {@code float} representing the resolution on the Y-axis that is associated with this {@code Camera} instance
	 */
	public float getResolutionY() {
		return this.resolutionY;
	}
	
	/**
	 * Returns a hash code for this {@code Camera} instance.
	 * 
	 * @return a hash code for this {@code Camera} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.fieldOfViewX, this.fieldOfViewY, this.pitch, this.yaw, this.orthonormalBasis, this.eye, Boolean.valueOf(this.isWalkLockEnabled), Float.valueOf(this.apertureRadius), Float.valueOf(this.focalDistance), Float.valueOf(this.resolutionX), Float.valueOf(this.resolutionY), Integer.valueOf(this.lens));
	}
	
	/**
	 * Moves this {@code Camera} instance backward by {@code distance}.
	 * 
	 * @param distance the distance to move by
	 */
	public void moveBackward(final float distance) {
		final Point3F eye = this.eye;
		
		final Vector3F w = this.orthonormalBasis.getW();
		
		final boolean isWalkLockEnabled = this.isWalkLockEnabled;
		
		final float x = eye.getX() - w.getX() * distance * (isWalkLockEnabled ? 1.0F : 1.0F);
		final float y = eye.getY() - w.getY() * distance * (isWalkLockEnabled ? 0.0F : 1.0F);
		final float z = eye.getZ() - w.getZ() * distance * (isWalkLockEnabled ? 1.0F : 1.0F);
		
		this.eye = new Point3F(x, y, z);
	}
	
	/**
	 * Moves this {@code Camera} instance forward by {@code distance}.
	 * 
	 * @param distance the distance to move by
	 */
	public void moveForward(final float distance) {
		final Point3F eye = this.eye;
		
		final Vector3F w = this.orthonormalBasis.getW();
		
		final boolean isWalkLockEnabled = this.isWalkLockEnabled;
		
		final float x = eye.getX() + w.getX() * distance * (isWalkLockEnabled ? 1.0F : 1.0F);
		final float y = eye.getY() + w.getY() * distance * (isWalkLockEnabled ? 0.0F : 1.0F);
		final float z = eye.getZ() + w.getZ() * distance * (isWalkLockEnabled ? 1.0F : 1.0F);
		
		this.eye = new Point3F(x, y, z);
	}
	
	/**
	 * Moves this {@code Camera} instance left by {@code distance}.
	 * 
	 * @param distance the distance to move by
	 */
	public void moveLeft(final float distance) {
		final Point3F eye = this.eye;
		
		final Vector3F u = this.orthonormalBasis.getU();
		
		final boolean isWalkLockEnabled = this.isWalkLockEnabled;
		
		final float x = eye.getX() - u.getX() * distance * (isWalkLockEnabled ? 1.0F : 1.0F);
		final float y = eye.getY() - u.getY() * distance * (isWalkLockEnabled ? 0.0F : 1.0F);
		final float z = eye.getZ() - u.getZ() * distance * (isWalkLockEnabled ? 1.0F : 1.0F);
		
		this.eye = new Point3F(x, y, z);
	}
	
	/**
	 * Moves this {@code Camera} instance right by {@code distance}.
	 * 
	 * @param distance the distance to move by
	 */
	public void moveRight(final float distance) {
		final Point3F eye = this.eye;
		
		final Vector3F u = this.orthonormalBasis.getU();
		
		final boolean isWalkLockEnabled = this.isWalkLockEnabled;
		
		final float x = eye.getX() + u.getX() * distance * (isWalkLockEnabled ? 1.0F : 1.0F);
		final float y = eye.getY() + u.getY() * distance * (isWalkLockEnabled ? 0.0F : 1.0F);
		final float z = eye.getZ() + u.getZ() * distance * (isWalkLockEnabled ? 1.0F : 1.0F);
		
		this.eye = new Point3F(x, y, z);
	}
	
	/**
	 * Sets the aperture radius associated with this {@code Camera} instance to {@code apertureRadius}.
	 * 
	 * @param apertureRadius a {@code float} representing the aperture radius associated with this {@code Camera} instance
	 */
	public void setApertureRadius(final float apertureRadius) {
		this.apertureRadius = apertureRadius;
	}
	
	/**
	 * Sets the eye associated with this {@code Camera} instance to {@code eye}.
	 * <p>
	 * If {@code eye} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param eye a {@link Point3F} representing the eye associated with this {@code Camera} instance
	 * @throws NullPointerException thrown if, and only if, {@code eye} is {@code null}
	 */
	public void setEye(final Point3F eye) {
		this.eye = Objects.requireNonNull(eye, "eye == null");
	}
	
	/**
	 * Sets the field of view on the X-axis associated with this {@code Camera} instance.
	 * <p>
	 * This method requires that the field of view on the Y-axis and the resolution (on both axes) are set.
	 */
	public void setFieldOfViewX() {
		this.fieldOfViewX = AngleF.fieldOfViewX(this.fieldOfViewY, this.resolutionX, this.resolutionY);
	}
	
	/**
	 * Sets the field of view on the X-axis associated with this {@code Camera} instance to {@code fieldOfViewX}.
	 * <p>
	 * If {@code fieldOfViewX} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param fieldOfViewX an {@link AngleF} representing the field of view on the X-axis associated with this {@code Camera} instance
	 * @throws NullPointerException thrown if, and only if, {@code fieldOfViewX} is {@code null}
	 */
	public void setFieldOfViewX(final AngleF fieldOfViewX) {
		this.fieldOfViewX = Objects.requireNonNull(fieldOfViewX, "fieldOfViewX == null");
	}
	
	/**
	 * Sets the field of view on the Y-axis associated with this {@code Camera} instance.
	 * <p>
	 * This method requires that the field of view on the X-axis and the resolution (on both axes) are set.
	 */
	public void setFieldOfViewY() {
		this.fieldOfViewY = AngleF.fieldOfViewY(this.fieldOfViewX, this.resolutionX, this.resolutionY);
	}
	
	/**
	 * Sets the field of view on the Y-axis associated with this {@code Camera} instance to {@code fieldOfViewY}.
	 * <p>
	 * If {@code fieldOfViewY} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param fieldOfViewY an {@link AngleF} representing the field of view on the Y-axis associated with this {@code Camera} instance
	 * @throws NullPointerException thrown if, and only if, {@code fieldOfViewY} is {@code null}
	 */
	public void setFieldOfViewY(final AngleF fieldOfViewY) {
		this.fieldOfViewY = Objects.requireNonNull(fieldOfViewY, "fieldOfViewY == null");
	}
	
	/**
	 * Sets the focal distance associated with this {@code Camera} instance to {@code focalDistance}.
	 * 
	 * @param focalDistance a {@code float} representing the focal distance associated with this {@code Camera} instance
	 */
	public void setFocalDistance(final float focalDistance) {
		this.focalDistance = focalDistance;
	}
	
	/**
	 * Sets the lens associated with this {@code Camera} instance to fisheye lens.
	 */
	public void setLensFisheye() {
		this.lens = LENS_FISHEYE;
	}
	
	/**
	 * Sets the lens associated with this {@code Camera} instance to thin lens.
	 */
	public void setLensThin() {
		this.lens = LENS_THIN;
	}
	
	/**
	 * Sets the orthonormal basis associated with this {@code Camera} instance.
	 * <p>
	 * This method takes into account the pitch and yaw angles associated with this {@code Camera} instance when constructing the {@code OrthonormalBasis33F} instance.
	 */
	public void setOrthonormalBasis() {
		final float pitch = this.pitch.getRadians();
		final float yaw = this.yaw.getRadians();
		
		final float x = sin(yaw) * cos(pitch);
		final float y = sin(pitch);
		final float z = cos(yaw) * cos(pitch);
		
		final Vector3F w = new Vector3F(x, y, z);
		final Vector3F v = Vector3F.v();
		
		this.orthonormalBasis = new OrthonormalBasis33F(w, v);
	}
	
	/**
	 * Sets the orthonormal basis associated with this {@code Camera} instance to {@code orthonormalBasis}.
	 * <p>
	 * If {@code orthonormalBasis} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Make sure that {@code orthonormalBasis} takes into account the pitch and yaw angles associated with this {@code Camera} instance. If they're not, looking or moving around will not work.
	 * <p>
	 * Consider calling {@link #setOrthonormalBasis()} instead.
	 * 
	 * @param orthonormalBasis an {@link OrthonormalBasis33F} representing the orthonormal basis associated with this {@code Camera} instance
	 * @throws NullPointerException thrown if, and only if, {@code orthonormalBasis} is {@code null}
	 */
	public void setOrthonormalBasis(final OrthonormalBasis33F orthonormalBasis) {
		this.orthonormalBasis = Objects.requireNonNull(orthonormalBasis, "orthonormalBasis == null");
	}
	
	/**
	 * Sets the pitch associated with this {@code Camera} instance to {@code pitch}.
	 * <p>
	 * If {@code pitch} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param pitch an {@link AngleF} representing the pitch associated with this {@code Camera} instance
	 * @throws NullPointerException thrown if, and only if, {@code pitch} is {@code null}
	 */
	public void setPitch(final AngleF pitch) {
		this.pitch = Objects.requireNonNull(pitch, "pitch == null");
	}
	
	/**
	 * Sets the resolution associated with this {@code Camera} instance to {@code resolutionX} and {@code resolutionY}.
	 * 
	 * @param resolutionX a {@code float} representing the resolution on the X-axis that is associated with this {@code Camera} instance
	 * @param resolutionY a {@code float} representing the resolution on the Y-axis that is associated with this {@code Camera} instance
	 */
	public void setResolution(final float resolutionX, final float resolutionY) {
		this.resolutionX = resolutionX;
		this.resolutionY = resolutionY;
	}
	
	/**
	 * Sets the resolution on the X-axis associated with this {@code Camera} instance to {@code resolutionX}.
	 * 
	 * @param resolutionX a {@code float} representing the resolution on the X-axis that is associated with this {@code Camera} instance
	 */
	public void setResolutionX(final float resolutionX) {
		this.resolutionX = resolutionX;
	}
	
	/**
	 * Sets the resolution on the Y-axis associated with this {@code Camera} instance to {@code resolutionY}.
	 * 
	 * @param resolutionY a {@code float} representing the resolution on the Y-axis that is associated with this {@code Camera} instance
	 */
	public void setResolutionY(final float resolutionY) {
		this.resolutionY = resolutionY;
	}
	
	/**
	 * Sets the walk lock state associated with this {@code Camera} instance.
	 * 
	 * @param isWalkLockEnabled {@code true} if, and only if, walk lock should be enabled, {@code false} otherwise
	 */
	public void setWalkLockEnabled(final boolean isWalkLockEnabled) {
		this.isWalkLockEnabled = isWalkLockEnabled;
	}
	
	/**
	 * Sets the yaw associated with this {@code Camera} instance to {@code yaw}.
	 * <p>
	 * If {@code yaw} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param yaw an {@link AngleF} representing the yaw associated with this {@code Camera} instance
	 * @throws NullPointerException thrown if, and only if, {@code yaw} is {@code null}
	 */
	public void setYaw(final AngleF yaw) {
		this.yaw = Objects.requireNonNull(yaw, "yaw == null");
	}
}