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
package org.dayflower.scene.light;

import static org.dayflower.util.Floats.PI_MULTIPLIED_BY_2;
import static org.dayflower.util.Floats.cos;
import static org.dayflower.util.Floats.equal;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.Optional;

import org.dayflower.geometry.AngleF;
import org.dayflower.geometry.Matrix44F;
import org.dayflower.geometry.OrthonormalBasis33F;
import org.dayflower.geometry.Point2F;
import org.dayflower.geometry.Point3F;
import org.dayflower.geometry.Ray3F;
import org.dayflower.geometry.SurfaceIntersection3F;
import org.dayflower.geometry.Vector3F;
import org.dayflower.image.Color3F;
import org.dayflower.scene.Intersection;
import org.dayflower.scene.Light;
import org.dayflower.scene.LightIncomingRadianceResult;

//TODO: Add Javadocs!
public final class SpotLight implements Light {
	private final AngleF coneAngle;
	private final AngleF coneAngleDelta;
	private final Color3F intensity;
	private final Matrix44F lightToWorld;
	private final Matrix44F lightToWorldInternal;
	private final Matrix44F worldToLightInternal;
	private final Point3F eye;
	private final Point3F lookAt;
	private final Point3F position;
	private final float cosConeAngle;
	private final float cosConeAngleMinusConeAngleDelta;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public SpotLight() {
		this(AngleF.degrees(50.0F));
	}
	
//	TODO: Add Javadocs!
	public SpotLight(final AngleF coneAngle) {
		this(coneAngle, AngleF.degrees(10.0F));
	}
	
//	TODO: Add Javadocs!
	public SpotLight(final AngleF coneAngle, final AngleF coneAngleDelta) {
		this(coneAngle, coneAngleDelta, new Color3F(100.0F));
	}
	
//	TODO: Add Javadocs!
	public SpotLight(final AngleF coneAngle, final AngleF coneAngleDelta, final Color3F intensity) {
		this(coneAngle, coneAngleDelta, intensity, Matrix44F.identity());
	}
	
//	TODO: Add Javadocs!
	public SpotLight(final AngleF coneAngle, final AngleF coneAngleDelta, final Color3F intensity, final Matrix44F lightToWorld) {
		this(coneAngle, coneAngleDelta, intensity, lightToWorld, new Point3F(0.0F, 1.0F, 0.0F), new Point3F(0.0F, 1.0F, 1.0F));
	}
	
//	TODO: Add Javadocs!
	public SpotLight(final AngleF coneAngle, final AngleF coneAngleDelta, final Color3F intensity, final Matrix44F lightToWorld, final Point3F eye, final Point3F lookAt) {
		this.coneAngle = Objects.requireNonNull(coneAngle, "coneAngle == null");
		this.coneAngleDelta = Objects.requireNonNull(coneAngleDelta, "coneAngleDelta == null");
		this.intensity = Objects.requireNonNull(intensity, "intensity == null");
		this.lightToWorld = Objects.requireNonNull(lightToWorld, "lightToWorld == null");
		this.lightToWorldInternal = doCreateLightToWorldInternal(lightToWorld, eye, lookAt);
		this.worldToLightInternal = Matrix44F.inverse(this.lightToWorldInternal);
		this.eye = Objects.requireNonNull(eye, "eye == null");
		this.lookAt = Objects.requireNonNull(lookAt, "lookAt == null");
		this.position = Point3F.transform(this.lightToWorldInternal, new Point3F());
		this.cosConeAngle = cos(coneAngle.getRadians());
		this.cosConeAngleMinusConeAngleDelta = cos(AngleF.subtract(coneAngle, coneAngleDelta).getRadians());
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link Color3F} instance with the emitted radiance for {@code ray}.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray a {@link Ray3F} instance
	 * @return a {@code Color3F} instance with the emitted radiance for {@code ray}
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	@Override
	public Color3F evaluateEmittedRadiance(final Ray3F ray) {
		Objects.requireNonNull(ray, "ray == null");
		
		return Color3F.BLACK;
	}
	
//	TODO: Add Javadocs!
	@Override
	public Color3F power() {
		return Color3F.multiply(Color3F.multiply(this.intensity, PI_MULTIPLIED_BY_2), 1.0F - 0.5F * (this.cosConeAngleMinusConeAngleDelta + this.cosConeAngle));
	}
	
//	TODO: Add Javadocs!
	@Override
	public Optional<LightIncomingRadianceResult> sampleIncomingRadiance(final Intersection intersection, final Point2F sample) {
		Objects.requireNonNull(intersection, "intersection == null");
		Objects.requireNonNull(sample, "sample == null");
		
		final SurfaceIntersection3F surfaceIntersection = intersection.getSurfaceIntersectionWorldSpace();
		
		final Point3F position = this.position;
		final Point3F surfaceIntersectionPoint = surfaceIntersection.getSurfaceIntersectionPoint();
		
		final Vector3F incoming = Vector3F.directionNormalized(surfaceIntersectionPoint, position);
		
		final Color3F intensity = this.intensity;
		final Color3F result = Color3F.divide(Color3F.multiply(intensity, doComputeFalloff(Vector3F.negate(incoming))), Point3F.distanceSquared(surfaceIntersectionPoint, position));
		
		final float probabilityDensityFunctionValue = 1.0F;
		
		return Optional.of(new LightIncomingRadianceResult(result, position, incoming, probabilityDensityFunctionValue));
	}
	
//	TODO: Add Javadocs!
	@Override
	public String toString() {
		return String.format("new SpotLight(%s, %s, %s, %s, %s, %s)", this.coneAngle, this.coneAngleDelta, this.intensity, this.lightToWorld, this.eye, this.lookAt);
	}
	
//	TODO: Add Javadocs!
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof SpotLight)) {
			return false;
		} else if(!Objects.equals(this.coneAngle, SpotLight.class.cast(object).coneAngle)) {
			return false;
		} else if(!Objects.equals(this.coneAngleDelta, SpotLight.class.cast(object).coneAngleDelta)) {
			return false;
		} else if(!Objects.equals(this.intensity, SpotLight.class.cast(object).intensity)) {
			return false;
		} else if(!Objects.equals(this.lightToWorld, SpotLight.class.cast(object).lightToWorld)) {
			return false;
		} else if(!Objects.equals(this.lightToWorldInternal, SpotLight.class.cast(object).lightToWorldInternal)) {
			return false;
		} else if(!Objects.equals(this.worldToLightInternal, SpotLight.class.cast(object).worldToLightInternal)) {
			return false;
		} else if(!Objects.equals(this.eye, SpotLight.class.cast(object).eye)) {
			return false;
		} else if(!Objects.equals(this.lookAt, SpotLight.class.cast(object).lookAt)) {
			return false;
		} else if(!Objects.equals(this.position, SpotLight.class.cast(object).position)) {
			return false;
		} else if(!equal(this.cosConeAngle, SpotLight.class.cast(object).cosConeAngle)) {
			return false;
		} else if(!equal(this.cosConeAngleMinusConeAngleDelta, SpotLight.class.cast(object).cosConeAngleMinusConeAngleDelta)) {
			return false;
		} else {
			return true;
		}
	}
	
//	TODO: Add Javadocs!
	@Override
	public boolean isDeltaDistribution() {
		return true;
	}
	
//	TODO: Add Javadocs!
	@Override
	public float evaluateProbabilityDensityFunctionIncomingRadiance(final Intersection intersection, final Vector3F incoming) {
		Objects.requireNonNull(intersection, "intersection == null");
		Objects.requireNonNull(incoming, "incoming == null");
		
		return 0.0F;
	}
	
//	TODO: Add Javadocs!
	@Override
	public int hashCode() {
		return Objects.hash(this.coneAngle, this.coneAngleDelta, this.intensity, this.lightToWorld, this.lightToWorldInternal, this.worldToLightInternal, this.eye, this.lookAt, this.position, Float.valueOf(this.cosConeAngle), Float.valueOf(this.cosConeAngleMinusConeAngleDelta));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private float doComputeFalloff(final Vector3F direction) {
		final Vector3F directionLightSpace = Vector3F.normalize(Vector3F.transform(this.worldToLightInternal, direction));
		
		final float cosTheta = directionLightSpace.cosTheta();
		
		if(cosTheta < this.cosConeAngle) {
			return 0.0F;
		}
		
		if(cosTheta >= this.cosConeAngleMinusConeAngleDelta) {
			return 1.0F;
		}
		
		final float delta = (cosTheta - this.cosConeAngle) / (this.cosConeAngleMinusConeAngleDelta - this.cosConeAngle);
		
		return (delta * delta) * (delta * delta);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static Matrix44F doCreateLightToWorldInternal(final Matrix44F lightToWorld, final Point3F eye, final Point3F lookAt) {
		final Vector3F w = Vector3F.directionNormalized(eye, lookAt);
		
		final OrthonormalBasis33F orthonormalBasis = new OrthonormalBasis33F(w);
		
		return Matrix44F.multiply(Matrix44F.multiply(lightToWorld, Matrix44F.translate(eye)), Matrix44F.inverse(Matrix44F.rotate(orthonormalBasis)));
	}
}