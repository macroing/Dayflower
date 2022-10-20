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

import static org.dayflower.utility.Floats.cos;
import static org.dayflower.utility.Floats.equal;
import static org.dayflower.utility.Floats.max;
import static org.dayflower.utility.Floats.min;
import static org.dayflower.utility.Floats.sin;
import static org.dayflower.utility.Floats.sqrt;
import static org.dayflower.utility.Floats.tan;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.dayflower.geometry.AngleF;
import org.dayflower.geometry.Matrix44F;
import org.dayflower.geometry.OrthonormalBasis33F;
import org.dayflower.geometry.Point2F;
import org.dayflower.geometry.Point3F;
import org.dayflower.geometry.Ray3F;
import org.dayflower.geometry.SampleGeneratorF;
import org.dayflower.geometry.Vector3F;
import org.dayflower.node.Node;
import org.dayflower.utility.ParameterArguments;

/**
 * A {@code Camera} represents a camera.
 * <p>
 * This class is mutable and therefore not thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class Camera implements Node {
	private AngleF fieldOfViewX;
	private AngleF fieldOfViewY;
	private AngleF pitch;
	private AngleF yaw;
	private Lens lens;
	private List<CameraObserver> cameraObservers;
	private OrthonormalBasis33F orthonormalBasis;
	private Point3F eye;
	private boolean isSamplingCenter;
	private boolean isWalkLockEnabled;
	private float apertureRadius;
	private float focalDistance;
	private float resolutionX;
	private float resolutionY;
	
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
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Camera(eye, AngleF.degrees(40.0F));
	 * }
	 * </pre>
	 * 
	 * @param eye a {@link Point3F} instance representing the eye that is associated with this {@code Camera} instance
	 * @throws NullPointerException thrown if, and only if, {@code eye} is {@code null}
	 */
	public Camera(final Point3F eye) {
		this(eye, AngleF.degrees(40.0F));
	}
	
	/**
	 * Constructs a new {@code Camera} instance with an eye of {@code eye} and a field of view on the X-axis of {@code fieldOfViewX}.
	 * <p>
	 * If either {@code eye} or {@code fieldOfViewX} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param eye a {@link Point3F} instance representing the eye that is associated with this {@code Camera} instance
	 * @param fieldOfViewX an {@link AngleF} instance representing the field of view on the X-axis
	 * @throws NullPointerException thrown if, and only if, either {@code eye} or {@code fieldOfViewX} are {@code null}
	 */
	public Camera(final Point3F eye, final AngleF fieldOfViewX) {
//		Initialize the CameraObservers:
		setCameraObservers();
		
//		Initialize the default parameters:
		setApertureRadius(0.0F);
		setEye(eye);
		setFieldOfViewX(fieldOfViewX);
		setFocalDistance(30.0F);
		setLens(Lens.THIN);
		setPitch(AngleF.pitch(Vector3F.x()));
		setResolution(800.0F, 800.0F);
		setWalkLockEnabled(true);
		setYaw(AngleF.yaw(Vector3F.y()));
		
//		Initialize the default parameters that require other parameters to be set:
		setFieldOfViewY();
		setOrthonormalBasis();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private Camera(final AngleF fieldOfViewX, final AngleF fieldOfViewY, final AngleF pitch, final AngleF yaw, final Lens lens, final List<CameraObserver> cameraObservers, final OrthonormalBasis33F orthonormalBasis, final Point3F eye, final boolean isWalkLockEnabled, final float apertureRadius, final float focalDistance, final float resolutionX, final float resolutionY) {
		this.fieldOfViewX = fieldOfViewX;
		this.fieldOfViewY = fieldOfViewY;
		this.pitch = pitch;
		this.yaw = yaw;
		this.lens = lens;
		this.cameraObservers = new ArrayList<>(cameraObservers);
		this.orthonormalBasis = orthonormalBasis;
		this.eye = eye;
		this.isWalkLockEnabled = isWalkLockEnabled;
		this.apertureRadius = apertureRadius;
		this.focalDistance = focalDistance;
		this.resolutionX = resolutionX;
		this.resolutionY = resolutionY;
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
	 * Returns a copy of this {@code Camera} instance.
	 * 
	 * @return a copy of this {@code Camera} instance
	 */
	public Camera copy() {
		return new Camera(this.fieldOfViewX, this.fieldOfViewY, this.pitch, this.yaw, this.lens, this.cameraObservers, this.orthonormalBasis, this.eye, this.isWalkLockEnabled, this.apertureRadius, this.focalDistance, this.resolutionX, this.resolutionY);
	}
	
	/**
	 * Returns a {@link Lens} instance representing the lens that is associated with this {@code Camera} instance.
	 * 
	 * @return a {@code Lens} instance representing the lens that is associated with this {@code Camera} instance
	 */
	public Lens getLens() {
		return this.lens;
	}
	
	/**
	 * Returns a {@code List} with all {@link CameraObserver} instances currently associated with this {@code Camera} instance.
	 * <p>
	 * Modifying the returned {@code List} will not affect this {@code Camera} instance.
	 * 
	 * @return a {@code List} with all {@code CameraObserver} instances currently associated with this {@code Camera} instance
	 */
	public List<CameraObserver> getCameraObservers() {
		return new ArrayList<>(this.cameraObservers);
	}
	
	/**
	 * Creates a new primary {@link Ray3F} instance given {@code imageX} and {@code imageY} as the image coordinates and {@code 0.5F} and {@code 0.5F} as the pixel coordinates.
	 * <p>
	 * Returns an {@code Optional} with an optional {@code Ray3F} instance.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * camera.createPrimaryRay(imageX, imageY, 0.5F, 0.5F);
	 * }
	 * </pre>
	 * 
	 * @param imageX the X-coordinate of the image
	 * @param imageY the Y-coordinate of the image
	 * @return an {@code Optional} with an optional {@code Ray3F} instance
	 */
	public Optional<Ray3F> createPrimaryRay(final float imageX, final float imageY) {
		return createPrimaryRay(imageX, imageY, 0.5F, 0.5F);
	}
	
	/**
	 * Creates a new primary {@link Ray3F} instance given {@code imageX} and {@code imageY} as the image coordinates and {@code pixelX} and {@code pixelY} as the pixel coordinates.
	 * <p>
	 * Returns an {@code Optional} with an optional {@code Ray3F} instance.
	 * 
	 * @param imageX the X-coordinate of the image
	 * @param imageY the Y-coordinate of the image
	 * @param pixelX the X-coordinate of the pixel
	 * @param pixelY the Y-coordinate of the pixel
	 * @return an {@code Optional} with an optional {@code Ray3F} instance
	 */
	public Optional<Ray3F> createPrimaryRay(final float imageX, final float imageY, final float pixelX, final float pixelY) {
		final float apertureRadius = this.apertureRadius;
		final float fieldOfViewX = tan(+this.fieldOfViewX.getRadians() * 0.5F);
		final float fieldOfViewY = tan(-this.fieldOfViewY.getRadians() * 0.5F);
		final float focalDistance = this.focalDistance;
		final float resolutionX = this.resolutionX;
		final float resolutionY = this.resolutionY;
		
		final Point3F eye = this.eye;
		
		final OrthonormalBasis33F orthonormalBasis = this.orthonormalBasis;
		
		final Vector3F u = orthonormalBasis.u;
		final Vector3F v = orthonormalBasis.v;
		final Vector3F w = orthonormalBasis.w;
		
		final float cameraX = 2.0F * ((imageX + pixelX) / (resolutionX - 1.0F)) - 1.0F;
		final float cameraY = 2.0F * ((imageY + pixelY) / (resolutionY - 1.0F)) - 1.0F;
		
		float wFactor = 1.0F;
		
		if(this.lens == Lens.FISHEYE) {
			final float dotProduct = cameraX * cameraX + cameraY * cameraY;
			
			if(dotProduct > 1.0F) {
				return Optional.empty();
			}
			
			wFactor = sqrt(1.0F - dotProduct);
		}
		
		final Point2F point = this.isSamplingCenter ? new Point2F() : SampleGeneratorF.sampleDiskUniformDistribution();
		
		final Point3F pointOnPlaneOneUnitAwayFromEye = new Point3F(Vector3F.add(Vector3F.add(Vector3F.multiply(u, fieldOfViewX * cameraX), Vector3F.multiply(v, fieldOfViewY * cameraY)), Vector3F.add(new Vector3F(eye), Vector3F.multiply(w, wFactor))));
		final Point3F pointOnImagePlane = Point3F.add(eye, Vector3F.direction(eye, pointOnPlaneOneUnitAwayFromEye), focalDistance);
		final Point3F origin = apertureRadius > 0.00001F ? Point3F.add(eye, Vector3F.add(Vector3F.multiply(u, point.x * apertureRadius), Vector3F.multiply(v, point.y * apertureRadius))) : eye;
		
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
	 * Returns a {@link Point3F} instance that is {@code distance} units above the eye.
	 * 
	 * @param distance the distance
	 * @return a {@code Point3F} instance that is {@code distance} units above the eye
	 */
	public Point3F getPointAboveEye(final float distance) {
		final Point3F eye = this.eye;
		
		final Vector3F v = this.orthonormalBasis.v;
		
		final float x = eye.x + v.x * distance;
		final float y = eye.y + v.y * distance;
		final float z = eye.z + v.z * distance;
		
		return new Point3F(x, y, z);
	}
	
	/**
	 * Returns a {@link Point3F} instance that is {@code distance} units behind the eye.
	 * 
	 * @param distance the distance
	 * @return a {@code Point3F} instance that is {@code distance} units behind the eye
	 */
	public Point3F getPointBehindEye(final float distance) {
		final Point3F eye = this.eye;
		
		final Vector3F w = this.orthonormalBasis.w;
		
		final float x = eye.x - w.x * distance;
		final float y = eye.y - w.y * distance;
		final float z = eye.z - w.z * distance;
		
		return new Point3F(x, y, z);
	}
	
	/**
	 * Returns a {@link Point3F} instance that is {@code distance} units below the eye.
	 * 
	 * @param distance the distance
	 * @return a {@code Point3F} instance that is {@code distance} units below the eye
	 */
	public Point3F getPointBelowEye(final float distance) {
		final Point3F eye = this.eye;
		
		final Vector3F v = this.orthonormalBasis.v;
		
		final float x = eye.x - v.x * distance;
		final float y = eye.y - v.y * distance;
		final float z = eye.z - v.z * distance;
		
		return new Point3F(x, y, z);
	}
	
	/**
	 * Returns a {@link Point3F} instance that is {@code distance} units infront of the eye.
	 * 
	 * @param distance the distance
	 * @return a {@code Point3F} instance that is {@code distance} units infront of the eye
	 */
	public Point3F getPointInfrontOfEye(final float distance) {
		final Point3F eye = this.eye;
		
		final Vector3F w = this.orthonormalBasis.w;
		
		final float x = eye.x + w.x * distance;
		final float y = eye.y + w.y * distance;
		final float z = eye.z + w.z * distance;
		
		return new Point3F(x, y, z);
	}
	
	/**
	 * Returns a {@link Point3F} instance that is {@code distance} units left of the eye.
	 * 
	 * @param distance the distance
	 * @return a {@code Point3F} instance that is {@code distance} units left of the eye
	 */
	public Point3F getPointLeftOfEye(final float distance) {
		final Point3F eye = this.eye;
		
		final Vector3F u = this.orthonormalBasis.u;
		
		final float x = eye.x - u.x * distance;
		final float y = eye.y - u.y * distance;
		final float z = eye.z - u.z * distance;
		
		return new Point3F(x, y, z);
	}
	
	/**
	 * Returns a {@link Point3F} instance that is {@code distance} units right of the eye.
	 * 
	 * @param distance the distance
	 * @return a {@code Point3F} instance that is {@code distance} units right of the eye
	 */
	public Point3F getPointRightOfEye(final float distance) {
		final Point3F eye = this.eye;
		
		final Vector3F u = this.orthonormalBasis.u;
		
		final float x = eye.x + u.x * distance;
		final float y = eye.y + u.y * distance;
		final float z = eye.z + u.z * distance;
		
		return new Point3F(x, y, z);
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
	 * Adds {@code cameraObserver} to this {@code Camera} instance.
	 * <p>
	 * Returns {@code true} if, and only if, {@code cameraObserver} was added, {@code false} otherwise.
	 * <p>
	 * If {@code cameraObserver} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param cameraObserver the {@link CameraObserver} instance to add
	 * @return {@code true} if, and only if, {@code cameraObserver} was added, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, {@code cameraObserver} is {@code null}
	 */
	public boolean addCameraObserver(final CameraObserver cameraObserver) {
		return this.cameraObservers.add(Objects.requireNonNull(cameraObserver, "cameraObserver == null"));
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
		} else if(!Objects.equals(this.lens, Camera.class.cast(object).lens)) {
			return false;
		} else if(!Objects.equals(this.cameraObservers, Camera.class.cast(object).cameraObservers)) {
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
		} else {
			return true;
		}
	}
	
	/**
	 * Returns the center sampling state associated with this {@code Camera} instance.
	 * 
	 * @return the center sampling state associated with this {@code Camera} instance
	 */
	public boolean isSamplingCenter() {
		return this.isSamplingCenter;
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
	 * Removes {@code cameraObserver} from this {@code Camera} instance, if present.
	 * <p>
	 * Returns {@code true} if, and only if, {@code cameraObserver} was removed, {@code false} otherwise.
	 * <p>
	 * If {@code cameraObserver} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param cameraObserver the {@link CameraObserver} instance to remove
	 * @return {@code true} if, and only if, {@code cameraObserver} was removed, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, {@code cameraObserver} is {@code null}
	 */
	public boolean removeCameraObserver(final CameraObserver cameraObserver) {
		return this.cameraObservers.remove(Objects.requireNonNull(cameraObserver, "cameraObserver == null"));
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
		return Objects.hash(this.fieldOfViewX, this.fieldOfViewY, this.pitch, this.yaw, this.lens, this.cameraObservers, this.orthonormalBasis, this.eye, Boolean.valueOf(this.isWalkLockEnabled), Float.valueOf(this.apertureRadius), Float.valueOf(this.focalDistance), Float.valueOf(this.resolutionX), Float.valueOf(this.resolutionY));
	}
	
	/**
	 * Moves this {@code Camera} instance backward by {@code distance}.
	 * 
	 * @param distance the distance to move by
	 */
	public void moveBackward(final float distance) {
		final Point3F eye = this.eye;
		
		final Vector3F w = this.orthonormalBasis.w;
		
		final boolean isWalkLockEnabled = this.isWalkLockEnabled;
		
		final float x = eye.x - w.x * distance * (isWalkLockEnabled ? 1.0F : 1.0F);
		final float y = eye.y - w.y * distance * (isWalkLockEnabled ? 0.0F : 1.0F);
		final float z = eye.z - w.z * distance * (isWalkLockEnabled ? 1.0F : 1.0F);
		
		setEye(new Point3F(x, y, z));
	}
	
	/**
	 * Moves this {@code Camera} instance down by {@code distance}.
	 * 
	 * @param distance the distance to move by
	 */
	public void moveDown(final float distance) {
		final Point3F eye = this.eye;
		
		final Vector3F v = this.orthonormalBasis.v;
		
		final boolean isWalkLockEnabled = this.isWalkLockEnabled;
		
		final float x = eye.x - v.x * distance * (isWalkLockEnabled ? 0.0F : 1.0F);
		final float y = eye.y - v.y * distance * (isWalkLockEnabled ? 1.0F : 1.0F);
		final float z = eye.z - v.z * distance * (isWalkLockEnabled ? 0.0F : 1.0F);
		
		setEye(new Point3F(x, y, z));
	}
	
	/**
	 * Moves this {@code Camera} instance forward by {@code distance}.
	 * 
	 * @param distance the distance to move by
	 */
	public void moveForward(final float distance) {
		final Point3F eye = this.eye;
		
		final Vector3F w = this.orthonormalBasis.w;
		
		final boolean isWalkLockEnabled = this.isWalkLockEnabled;
		
		final float x = eye.x + w.x * distance * (isWalkLockEnabled ? 1.0F : 1.0F);
		final float y = eye.y + w.y * distance * (isWalkLockEnabled ? 0.0F : 1.0F);
		final float z = eye.z + w.z * distance * (isWalkLockEnabled ? 1.0F : 1.0F);
		
		setEye(new Point3F(x, y, z));
	}
	
	/**
	 * Moves this {@code Camera} instance left by {@code distance}.
	 * 
	 * @param distance the distance to move by
	 */
	public void moveLeft(final float distance) {
		final Point3F eye = this.eye;
		
		final Vector3F u = this.orthonormalBasis.u;
		
		final boolean isWalkLockEnabled = this.isWalkLockEnabled;
		
		final float x = eye.x - u.x * distance * (isWalkLockEnabled ? 1.0F : 1.0F);
		final float y = eye.y - u.y * distance * (isWalkLockEnabled ? 0.0F : 1.0F);
		final float z = eye.z - u.z * distance * (isWalkLockEnabled ? 1.0F : 1.0F);
		
		setEye(new Point3F(x, y, z));
	}
	
	/**
	 * Moves this {@code Camera} instance right by {@code distance}.
	 * 
	 * @param distance the distance to move by
	 */
	public void moveRight(final float distance) {
		final Point3F eye = this.eye;
		
		final Vector3F u = this.orthonormalBasis.u;
		
		final boolean isWalkLockEnabled = this.isWalkLockEnabled;
		
		final float x = eye.x + u.x * distance * (isWalkLockEnabled ? 1.0F : 1.0F);
		final float y = eye.y + u.y * distance * (isWalkLockEnabled ? 0.0F : 1.0F);
		final float z = eye.z + u.z * distance * (isWalkLockEnabled ? 1.0F : 1.0F);
		
		setEye(new Point3F(x, y, z));
	}
	
	/**
	 * Moves this {@code Camera} instance up by {@code distance}.
	 * 
	 * @param distance the distance to move by
	 */
	public void moveUp(final float distance) {
		final Point3F eye = this.eye;
		
		final Vector3F v = this.orthonormalBasis.v;
		
		final boolean isWalkLockEnabled = this.isWalkLockEnabled;
		
		final float x = eye.x + v.x * distance * (isWalkLockEnabled ? 0.0F : 1.0F);
		final float y = eye.y + v.y * distance * (isWalkLockEnabled ? 1.0F : 1.0F);
		final float z = eye.z + v.z * distance * (isWalkLockEnabled ? 0.0F : 1.0F);
		
		setEye(new Point3F(x, y, z));
	}
	
	/**
	 * Rotates this {@code Camera} instance along {@code vector} with an angle of {@code angle}.
	 * <p>
	 * If either {@code angle} or {@code vector} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param angle an {@link AngleF} instance
	 * @param vector a {@link Vector3F} instance that represents an axis
	 * @throws NullPointerException thrown if, and only if, either {@code angle} or {@code vector} are {@code null}
	 */
	public void rotate(final AngleF angle, final Vector3F vector) {
		setOrthonormalBasis(OrthonormalBasis33F.transform(Matrix44F.rotate(angle, vector), this.orthonormalBasis));
	}
	
	/**
	 * Rotates this {@code Camera} instance along {@code x} and {@code y} as movement on the screen.
	 * 
	 * @param x the X-coordinate on the screen
	 * @param y the Y-coordinate on the screen
	 */
	public void rotate(final float x, final float y) {
		final AngleF oldYaw = getYaw();
		final AngleF newYaw = AngleF.add(oldYaw, AngleF.degrees(-x * 0.25F));
		
		final AngleF oldPitch = getPitch();
		final AngleF newPitch = AngleF.degrees(max(min(oldPitch.getDegrees() + AngleF.degrees(y * 0.25F, -90.0F, 90.0F).getDegrees(), 90.00F), -90.00F), -90.00F, 90.00F);
		
		setYaw(newYaw);
		setPitch(newPitch);
		setOrthonormalBasis();
	}
	
	/**
	 * Rotates this {@code Camera} instance along the X-axis with an angle of {@code angle}.
	 * <p>
	 * If {@code angle} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param angle an {@link AngleF} instance
	 * @throws NullPointerException thrown if, and only if, {@code angle} is {@code null}
	 */
	public void rotateX(final AngleF angle) {
		setOrthonormalBasis(OrthonormalBasis33F.transform(Matrix44F.rotateX(angle), this.orthonormalBasis));
	}
	
	/**
	 * Rotates this {@code Camera} instance along the Y-axis with an angle of {@code angle}.
	 * <p>
	 * If {@code angle} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param angle an {@link AngleF} instance
	 * @throws NullPointerException thrown if, and only if, {@code angle} is {@code null}
	 */
	public void rotateY(final AngleF angle) {
		setOrthonormalBasis(OrthonormalBasis33F.transform(Matrix44F.rotateY(angle), this.orthonormalBasis));
	}
	
	/**
	 * Rotates this {@code Camera} instance along the Z-axis with an angle of {@code angle}.
	 * <p>
	 * If {@code angle} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param angle an {@link AngleF} instance
	 * @throws NullPointerException thrown if, and only if, {@code angle} is {@code null}
	 */
	public void rotateZ(final AngleF angle) {
		setOrthonormalBasis(OrthonormalBasis33F.transform(Matrix44F.rotateZ(angle), this.orthonormalBasis));
	}
	
	/**
	 * Sets the aperture radius associated with this {@code Camera} instance to {@code apertureRadius}.
	 * 
	 * @param apertureRadius a {@code float} representing the aperture radius associated with this {@code Camera} instance
	 */
	public void setApertureRadius(final float apertureRadius) {
		if(!equal(this.apertureRadius, apertureRadius)) {
			final float oldApertureRadius = this.apertureRadius;
			final float newApertureRadius =      apertureRadius;
			
			this.apertureRadius = apertureRadius;
			
			for(final CameraObserver cameraObserver : this.cameraObservers) {
				cameraObserver.onChangeApertureRadius(this, oldApertureRadius, newApertureRadius);
			}
		}
	}
	
	/**
	 * Sets the {@code List} with all {@link CameraObserver} instances associated with this {@code Camera} instance to an empty {@code List}.
	 */
	public void setCameraObservers() {
		this.cameraObservers = new ArrayList<>();
	}
	
	/**
	 * Sets the {@code List} with all {@link CameraObserver} instances associated with this {@code Camera} instance to a copy of {@code cameraObservers}.
	 * <p>
	 * If either {@code cameraObservers} or at least one of its elements are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param cameraObservers a {@code List} with all {@code CameraObserver} instances associated with this {@code Camera} instance
	 * @throws NullPointerException thrown if, and only if, either {@code cameraObservers} or at least one of its elements are {@code null}
	 */
	public void setCameraObservers(final List<CameraObserver> cameraObservers) {
		this.cameraObservers = new ArrayList<>(ParameterArguments.requireNonNullList(cameraObservers, "cameraObservers"));
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
		Objects.requireNonNull(eye, "eye == null");
		
		if(!Objects.equals(this.eye, eye)) {
			final Point3F oldEye = this.eye;
			final Point3F newEye =      eye;
			
			this.eye = eye;
			
			for(final CameraObserver cameraObserver : this.cameraObservers) {
				cameraObserver.onChangeEye(this, oldEye, newEye);
			}
		}
	}
	
	/**
	 * Sets the field of view on the X-axis associated with this {@code Camera} instance.
	 * <p>
	 * This method requires that the field of view on the Y-axis and the resolution (on both axes) are set.
	 */
	public void setFieldOfViewX() {
		setFieldOfViewX(AngleF.fieldOfViewX(this.fieldOfViewY, this.resolutionX, this.resolutionY));
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
		Objects.requireNonNull(fieldOfViewX, "fieldOfViewX == null");
		
		if(!Objects.equals(this.fieldOfViewX, fieldOfViewX)) {
			final AngleF oldFieldOfViewX = this.fieldOfViewX;
			final AngleF newFieldOfViewX =      fieldOfViewX;
			
			this.fieldOfViewX = fieldOfViewX;
			
			for(final CameraObserver cameraObserver : this.cameraObservers) {
				cameraObserver.onChangeFieldOfViewX(this, oldFieldOfViewX, newFieldOfViewX);
			}
		}
	}
	
	/**
	 * Sets the field of view on the Y-axis associated with this {@code Camera} instance.
	 * <p>
	 * This method requires that the field of view on the X-axis and the resolution (on both axes) are set.
	 */
	public void setFieldOfViewY() {
		setFieldOfViewY(AngleF.fieldOfViewY(this.fieldOfViewX, this.resolutionX, this.resolutionY));
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
		Objects.requireNonNull(fieldOfViewY, "fieldOfViewY == null");
		
		if(!Objects.equals(this.fieldOfViewY, fieldOfViewY)) {
			final AngleF oldFieldOfViewY = this.fieldOfViewY;
			final AngleF newFieldOfViewY =      fieldOfViewY;
			
			this.fieldOfViewY = fieldOfViewY;
			
			for(final CameraObserver cameraObserver : this.cameraObservers) {
				cameraObserver.onChangeFieldOfViewY(this, oldFieldOfViewY, newFieldOfViewY);
			}
		}
	}
	
	/**
	 * Sets the focal distance associated with this {@code Camera} instance to {@code focalDistance}.
	 * 
	 * @param focalDistance a {@code float} representing the focal distance associated with this {@code Camera} instance
	 */
	public void setFocalDistance(final float focalDistance) {
		if(!equal(this.focalDistance, focalDistance)) {
			final float oldFocalDistance = this.focalDistance;
			final float newFocalDistance =      focalDistance;
			
			this.focalDistance = focalDistance;
			
			for(final CameraObserver cameraObserver : this.cameraObservers) {
				cameraObserver.onChangeFocalDistance(this, oldFocalDistance, newFocalDistance);
			}
		}
	}
	
	/**
	 * Sets the lens associated with this {@code Camera} instance to {@code lens}.
	 * <p>
	 * If {@code lens} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param lens a {@link Lens} representing the lens associated with this {@code Camera} instance
	 * @throws NullPointerException thrown if, and only if, {@code lens} is {@code null}
	 */
	public void setLens(final Lens lens) {
		Objects.requireNonNull(lens, "lens == null");
		
		if(!Objects.equals(this.lens, lens)) {
			final Lens oldLens = this.lens;
			final Lens newLens =      lens;
			
			this.lens = lens;
			
			for(final CameraObserver cameraObserver : this.cameraObservers) {
				cameraObserver.onChangeLens(this, oldLens, newLens);
			}
		}
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
		
		setOrthonormalBasis(new OrthonormalBasis33F(w, v));
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
		Objects.requireNonNull(orthonormalBasis, "orthonormalBasis == null");
		
		if(!Objects.equals(this.orthonormalBasis, orthonormalBasis)) {
			final OrthonormalBasis33F oldOrthonormalBasis = this.orthonormalBasis;
			final OrthonormalBasis33F newOrthonormalBasis =      orthonormalBasis;
			
			this.orthonormalBasis = orthonormalBasis;
			
			for(final CameraObserver cameraObserver : this.cameraObservers) {
				cameraObserver.onChangeOrthonormalBasis(this, oldOrthonormalBasis, newOrthonormalBasis);
			}
		}
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
		Objects.requireNonNull(pitch, "pitch == null");
		
		if(!Objects.equals(this.pitch, pitch)) {
			final AngleF oldPitch = this.pitch;
			final AngleF newPitch =      pitch;
			
			this.pitch = pitch;
			
			for(final CameraObserver cameraObserver : this.cameraObservers) {
				cameraObserver.onChangePitch(this, oldPitch, newPitch);
			}
		}
	}
	
	/**
	 * Sets the resolution associated with this {@code Camera} instance to {@code resolutionX} and {@code resolutionY}.
	 * 
	 * @param resolutionX a {@code float} representing the resolution on the X-axis that is associated with this {@code Camera} instance
	 * @param resolutionY a {@code float} representing the resolution on the Y-axis that is associated with this {@code Camera} instance
	 */
	public void setResolution(final float resolutionX, final float resolutionY) {
		setResolutionX(resolutionX);
		setResolutionY(resolutionY);
	}
	
	/**
	 * Sets the resolution on the X-axis associated with this {@code Camera} instance to {@code resolutionX}.
	 * 
	 * @param resolutionX a {@code float} representing the resolution on the X-axis that is associated with this {@code Camera} instance
	 */
	public void setResolutionX(final float resolutionX) {
		if(!equal(this.resolutionX, resolutionX)) {
			final float oldResolutionX = this.resolutionX;
			final float newResolutionX =      resolutionX;
			
			this.resolutionX = resolutionX;
			
			for(final CameraObserver cameraObserver : this.cameraObservers) {
				cameraObserver.onChangeResolutionX(this, oldResolutionX, newResolutionX);
			}
		}
	}
	
	/**
	 * Sets the resolution on the Y-axis associated with this {@code Camera} instance to {@code resolutionY}.
	 * 
	 * @param resolutionY a {@code float} representing the resolution on the Y-axis that is associated with this {@code Camera} instance
	 */
	public void setResolutionY(final float resolutionY) {
		if(!equal(this.resolutionY, resolutionY)) {
			final float oldResolutionY = this.resolutionY;
			final float newResolutionY =      resolutionY;
			
			this.resolutionY = resolutionY;
			
			for(final CameraObserver cameraObserver : this.cameraObservers) {
				cameraObserver.onChangeResolutionY(this, oldResolutionY, newResolutionY);
			}
		}
	}
	
	/**
	 * Sets the center sampling state associated with this {@code Camera} instance.
	 * 
	 * @param isSamplingCenter {@code true} if, and only if, center sampling should be enabled, {@code false} otherwise
	 */
	public void setSamplingCenter(final boolean isSamplingCenter) {
		this.isSamplingCenter = isSamplingCenter;
	}
	
	/**
	 * Sets the walk lock state associated with this {@code Camera} instance.
	 * 
	 * @param isWalkLockEnabled {@code true} if, and only if, walk lock should be enabled, {@code false} otherwise
	 */
	public void setWalkLockEnabled(final boolean isWalkLockEnabled) {
		if(this.isWalkLockEnabled != isWalkLockEnabled) {
			final boolean oldIsWalkLockEnabled = this.isWalkLockEnabled;
			final boolean newIsWalkLockEnabled =      isWalkLockEnabled;
			
			this.isWalkLockEnabled = isWalkLockEnabled;
			
			for(final CameraObserver cameraObserver : this.cameraObservers) {
				cameraObserver.onChangeWalkLockEnabled(this, oldIsWalkLockEnabled, newIsWalkLockEnabled);
			}
		}
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
		Objects.requireNonNull(yaw, "yaw == null");
		
		if(!Objects.equals(this.yaw, yaw)) {
			final AngleF oldYaw = this.yaw;
			final AngleF newYaw =      yaw;
			
			this.yaw = yaw;
			
			for(final CameraObserver cameraObserver : this.cameraObservers) {
				cameraObserver.onChangeYaw(this, oldYaw, newYaw);
			}
		}
	}
}