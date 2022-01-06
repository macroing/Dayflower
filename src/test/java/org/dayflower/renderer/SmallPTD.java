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
package org.dayflower.renderer;

import static org.dayflower.utility.Doubles.MAX_VALUE;
import static org.dayflower.utility.Doubles.abs;
import static org.dayflower.utility.Doubles.isNaN;
import static org.dayflower.utility.Doubles.random;
import static org.dayflower.utility.Doubles.solveQuadraticSystem;

import java.util.Objects;
import java.util.Optional;

import org.dayflower.color.Color3D;
import org.dayflower.color.Color3F;
import org.dayflower.geometry.Point2D;
import org.dayflower.geometry.Point3D;
import org.dayflower.geometry.Ray3D;
import org.dayflower.geometry.SampleGeneratorD;
import org.dayflower.geometry.Vector3D;
import org.dayflower.image.PixelImageF;
import org.dayflower.scene.fresnel.Schlick;

public final class SmallPTD {
	private SmallPTD() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static Color3D radiance(final Ray3D ray, final Scene scene, final int depth) {
		final Intersection intersection = scene.intersection(ray);
		
		if(intersection == null) {
			return Color3D.BLACK;
		}
		
		final Sphere3D sphere = intersection.getSphere();
		
		final Color3D emission = sphere.getEmission();
		
		final int currentDepth = depth + 1;
		
		if(currentDepth > 20) {
			return emission;
		}
		
		Color3D albedo = sphere.getAlbedo();
		
		if(currentDepth > 5) {
			final double probability = albedo.maximum();
			
			if(random() >= probability) {
				return emission;
			}
			
			albedo = Color3D.divide(albedo, probability);
		}
		
		switch(sphere.getMaterial()) {
			case GLASS:
				return radianceGlass(ray, scene, currentDepth, albedo, emission, intersection);
			case MATTE:
				return radianceMatte(ray, scene, currentDepth, albedo, emission, intersection);
			case METAL:
				return radianceMetal(ray, scene, currentDepth, albedo, emission, intersection);
			case MIRROR:
				return radianceMirror(ray, scene, currentDepth, albedo, emission, intersection);
			default:
				return Color3D.BLACK;
		}
	}
	
	public static Color3D radianceGlass(final Ray3D ray, final Scene scene, final int depth, final Color3D albedo, final Color3D emission, final Intersection intersection) {
		final Vector3D direction = ray.getDirection();
		
		final Vector3D surfaceNormal = intersection.getSurfaceNormal();
		final Vector3D surfaceNormalCorrectlyOriented = Vector3D.dotProduct(surfaceNormal, direction) < 0.0D ? surfaceNormal : Vector3D.negate(surfaceNormal);
		
		final boolean isEntering = Vector3D.dotProduct(surfaceNormal, surfaceNormalCorrectlyOriented) > 0.0D;
		
		final double etaA = 1.0D;
		final double etaB = 1.5D;
		final double etaI = isEntering ? etaA : etaB;
		final double etaT = isEntering ? etaB : etaA;
		final double eta = etaI / etaT;
		
		final Point3D reflectionOrigin = intersection.getSurfaceIntersectionPoint();
		
		final Vector3D reflectionDirection = Vector3D.reflection(direction, surfaceNormal, true);
		
		final Ray3D reflectionRay = new Ray3D(reflectionOrigin, reflectionDirection);
		
		final Optional<Vector3D> optionalTransmissionDirection = Vector3D.refraction2(direction, surfaceNormalCorrectlyOriented, eta);
		
		if(optionalTransmissionDirection.isPresent()) {
			final Point3D transmissionOrigin = intersection.getSurfaceIntersectionPoint();
			
			final Vector3D transmissionDirection = optionalTransmissionDirection.get();
			
			final Ray3D transmissionRay = new Ray3D(transmissionOrigin, transmissionDirection);
			
			final double cosThetaI = Vector3D.dotProduct(direction, surfaceNormalCorrectlyOriented);
			final double cosThetaICorrectlyOriented = isEntering ? -cosThetaI : Vector3D.dotProduct(transmissionDirection, surfaceNormal);
			
			final double r0 = (etaB - etaA) * (etaB - etaA) / ((etaB + etaA) * (etaB + etaA));
			
			final double reflectance = Schlick.fresnelDielectric(cosThetaICorrectlyOriented, r0);
			final double transmittance = 1.0D - reflectance;
			
			final double probabilityRussianRoulette = 0.25D + 0.5D * reflectance;
			final double probabilityRussianRouletteReflection = reflectance / probabilityRussianRoulette;
			final double probabilityRussianRouletteTransmission = transmittance / (1.0D - probabilityRussianRoulette);
			
			final boolean isChoosingSpecularReflection = random() < probabilityRussianRoulette;
			
			if(isChoosingSpecularReflection) {
				return Color3D.addAndMultiply(emission, albedo, radiance(reflectionRay, scene, depth), probabilityRussianRouletteReflection);
			}
			
			return Color3D.addAndMultiply(emission, albedo, radiance(transmissionRay, scene, depth), probabilityRussianRouletteTransmission);
		}
		
		return Color3D.addAndMultiply(emission, albedo, radiance(reflectionRay, scene, depth));
	}
	
	public static Color3D radianceMatte(final Ray3D ray, final Scene scene, final int depth, final Color3D albedo, final Color3D emission, final Intersection intersection) {
		final Vector3D surfaceNormal = intersection.getSurfaceNormal();
		final Vector3D surfaceNormalCorrectlyOriented = Vector3D.dotProduct(surfaceNormal, ray.getDirection()) < 0.0D ? surfaceNormal : Vector3D.negate(surfaceNormal);
		
		final Vector3D s = SampleGeneratorD.sampleHemisphereCosineDistribution2();
		
		final Vector3D w = surfaceNormalCorrectlyOriented;
		final Vector3D u = Vector3D.normalize(Vector3D.crossProduct(abs(w.getX()) > 0.1D ? Vector3D.y() : Vector3D.x(), w));
		final Vector3D v = Vector3D.crossProduct(w, u);
		
		final Point3D newOrigin = intersection.getSurfaceIntersectionPoint();
		
		final Vector3D newDirection = Vector3D.normalize(Vector3D.add(Vector3D.multiply(u, s.getX()), Vector3D.multiply(v, s.getY()), Vector3D.multiply(w, s.getZ())));
		
		final Ray3D newRay = new Ray3D(newOrigin, newDirection);
		
		final Color3D radiance = radiance(newRay, scene, depth);
		
		return Color3D.addAndMultiply(emission, albedo, radiance);
	}
	
	public static Color3D radianceMetal(final Ray3D ray, final Scene scene, final int depth, final Color3D albedo, final Color3D emission, final Intersection intersection) {
		final Vector3D s = SampleGeneratorD.sampleHemispherePowerCosineDistribution(random(), random(), 20.0D);
		
		final Vector3D w = Vector3D.normalize(Vector3D.reflection(ray.getDirection(), intersection.getSurfaceNormal(), true));
		final Vector3D v = Vector3D.computeV(w);
		final Vector3D u = Vector3D.crossProduct(v, w);
		
		final Point3D newOrigin = intersection.getSurfaceIntersectionPoint();
		
		final Vector3D newDirection = Vector3D.normalize(Vector3D.add(Vector3D.multiply(u, s.getX()), Vector3D.multiply(v, s.getY()), Vector3D.multiply(w, s.getZ())));
		
		final Ray3D newRay = new Ray3D(newOrigin, newDirection);
		
		final Color3D radiance = radiance(newRay, scene, depth);
		
		return Color3D.addAndMultiply(emission, albedo, radiance);
	}
	
	public static Color3D radianceMirror(final Ray3D ray, final Scene scene, final int depth, final Color3D albedo, final Color3D emission, final Intersection intersection) {
		final Point3D newOrigin = intersection.getSurfaceIntersectionPoint();
		
		final Vector3D newDirection = Vector3D.reflection(ray.getDirection(), intersection.getSurfaceNormal(), true);
		
		final Ray3D newRay = new Ray3D(newOrigin, newDirection);
		
		final Color3D radiance = radiance(newRay, scene, depth);
		
		return Color3D.addAndMultiply(emission, albedo, radiance);
	}
	
	public static void main(final String[] args) {
		final int gridX = 2;
		final int gridY = 2;
		final int resolutionX = 1024;
		final int resolutionY = 768;
		final int samples = 10;
		
		final Camera camera = new Camera(resolutionX, resolutionY);
		
		final Scene scene = new Scene();
		
		final Color3D[] colors = Color3D.array(resolutionX * resolutionY);
		
		for(int pixelY = 0; pixelY < resolutionY; pixelY++) {
			for(int pixelX = 0; pixelX < resolutionX; pixelX++) {
				for(int gridSampleY = 0, i = (resolutionY - pixelY - 1) * resolutionX + pixelX; gridSampleY < gridY; gridSampleY++) {
					for(int gridSampleX = 0; gridSampleX < gridX; gridSampleX++) {
						Color3D radiance = Color3D.BLACK;
						
						for(int sample = 0; sample < samples; sample++) {
							final Ray3D ray = camera.generatePrimaryRay(pixelX, pixelY, gridSampleX, gridSampleY);
							
							radiance = Color3D.add(radiance, Color3D.divide(radiance(ray, scene, 0), samples));
						}
						
						colors[i] = Color3D.add(colors[i], Color3D.divide(Color3D.saturate(radiance), gridX * gridY));
					}
				}
			}
		}
		
		final PixelImageF pixelImage = new PixelImageF(resolutionX, resolutionY);
		
		for(int i = 0; i < colors.length; i++) {
			pixelImage.setColorRGB(new Color3F(colors[i]), i);
		}
		
		pixelImage.redoGammaCorrection();
		pixelImage.save("./generated/SmallPT.png");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static final class Camera {
		private final Point3D eye;
		private final Vector3D u;
		private final Vector3D v;
		private final Vector3D w;
		private final double resolutionX;
		private final double resolutionY;
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		public Camera(final double resolutionX, final double resolutionY) {
			this(resolutionX, resolutionY, 0.5135D);
		}
		
		public Camera(final double resolutionX, final double resolutionY, final double fieldOfView) {
			this.eye = new Point3D(50.0D, 50.0D, 295.6D);
			this.w = Vector3D.normalize(new Vector3D(0.0D, -0.042612D, -1.0D));
			this.u = new Vector3D(fieldOfView * resolutionX / resolutionY, 0.0D, 0.0D);
			this.v = Vector3D.multiply(Vector3D.normalize(Vector3D.crossProduct(this.u, this.w)), fieldOfView);
			this.resolutionX = resolutionX;
			this.resolutionY = resolutionY;
		}
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		public Ray3D generatePrimaryRay(final double pixelX, final double pixelY, final double gridSampleU, final double gridSampleV) {
			final Point2D sample = doSample(pixelX, pixelY, gridSampleU, gridSampleV);
			
			final Vector3D u = Vector3D.multiply(this.u, sample.getU());
			final Vector3D v = Vector3D.multiply(this.v, sample.getV());
			final Vector3D w = this.w;
			
			final Vector3D direction = Vector3D.add(u, v, w);
			
			final Point3D origin = Point3D.add(this.eye, Vector3D.multiply(direction, 140.0D));
			
			final Vector3D directionNormalized = Vector3D.normalize(direction);
			
			return new Ray3D(origin, directionNormalized);
		}
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		private Point2D doSample(final double pixelX, final double pixelY, final double gridSampleU, final double gridSampleV) {
			/*
			 * The list below shows some possible values assigned to either sampleU1 or sampleV1, both of which are in the range [-0.25D, +1.25D]:
			 * 
			 * sampleN1 #1: (0.0D + 0.5D + -1.0D) / 2.0D = -0.25D
			 * sampleN1 #2: (1.0D + 0.5D + -1.0D) / 2.0D = +0.25D
			 * sampleN1 #3: (0.0D + 0.5D + +1.0D) / 2.0D = +0.75D
			 * sampleN1 #4: (1.0D + 0.5D + +1.0D) / 2.0D = +1.25D
			 * 
			 * The list below shows some possible values assigned to either sampleU2 or sampleV2, given a pixel coordinate of 100.0D and a resolution of 200.0D:
			 * 
			 * sampleN2 #1: (-0.25D + 100.0D) / 200.0D - 0.5D = 0.00125D
			 * sampleN2 #2: (+0.25D + 100.0D) / 200.0D - 0.5D = 0.00125D
			 * sampleN2 #3: (+0.75D + 100.0D) / 200.0D - 0.5D = 0.00375D
			 * sampleN2 #4: (+1.25D + 100.0D) / 200.0D - 0.5D = 0.00625D
			 */
			
//			Generate a Point2D instance whose getU() and getV() methods returns a double in the range [-1.0D, +1.0D]:
			final Point2D sample = SampleGeneratorD.sampleExactInverseTentFilter();
			
//			The variables gridSampleU and gridSampleV are in the range [0.0D, 1.0D]:
			final double sampleU1 = (gridSampleU + 0.5D + sample.getU()) / 2.0D;
			final double sampleV1 = (gridSampleV + 0.5D + sample.getV()) / 2.0D;
			
//			The variables sampleU1 and sampleV1 are in the range [-0.25D, +1.25D]:
			final double sampleU2 = (sampleU1 + pixelX) / this.resolutionX - 0.5D;
			final double sampleV2 = (sampleV1 + pixelY) / this.resolutionY - 0.5D;
			
			return new Point2D(sampleU2, sampleV2);
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static final class Intersection {
		private final Point3D surfaceIntersectionPoint;
		private final Sphere3D sphere;
		private final Vector3D surfaceNormal;
		private final double t;
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		public Intersection(final Point3D surfaceIntersectionPoint, final Sphere3D sphere, final Vector3D surfaceNormal, final double t) {
			this.surfaceIntersectionPoint = Objects.requireNonNull(surfaceIntersectionPoint, "surfaceIntersectionPoint == null");
			this.sphere = Objects.requireNonNull(sphere, "sphere == null");
			this.surfaceNormal = Objects.requireNonNull(surfaceNormal, "surfaceNormal == null");
			this.t = t;
		}
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		public Point3D getSurfaceIntersectionPoint() {
			return this.surfaceIntersectionPoint;
		}
		
		public Sphere3D getSphere() {
			return this.sphere;
		}
		
		public Vector3D getSurfaceNormal() {
			return this.surfaceNormal;
		}
		
		public double getT() {
			return this.t;
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static final class Scene {
		private final Sphere3D[] spheres;
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		public Scene() {
			this.spheres = new Sphere3D[] {
				new Sphere3D(1.0e5D, new Point3D( 1.0e5D +  1.0D,   40.8D,           81.6D),          new Color3D(),                    new Color3D(0.750D, 0.250D, 0.250D), Material.MATTE),
				new Sphere3D(1.0e5D, new Point3D(-1.0e5D + 99.0D,   40.8D,           81.6D),          new Color3D(),                    new Color3D(0.250D, 0.250D, 0.750D), Material.MATTE),
				new Sphere3D(1.0e5D, new Point3D(  50.0D,           40.8D,          1.0e5D),          new Color3D(),                    new Color3D(0.750D, 0.750D, 0.750D), Material.MATTE),
				new Sphere3D(1.0e5D, new Point3D(  50.0D,           40.8D,         -1.0e5D + 170.0D), new Color3D(),                    new Color3D(),                       Material.MATTE),
				new Sphere3D(1.0e5D, new Point3D(  50.0D,          1.0e5D,           81.6D),          new Color3D(),                    new Color3D(0.750D, 0.750D, 0.750D), Material.MATTE),
				new Sphere3D(1.0e5D, new Point3D(  50.0D,         -1.0e5D + 81.6D,   81.6D),          new Color3D(),                    new Color3D(0.750D, 0.750D, 0.750D), Material.MATTE),
				new Sphere3D( 16.5D, new Point3D(  27.0D,           16.5D,           47.0D),          new Color3D(),                    new Color3D(0.999D, 0.999D, 0.999D), Material.MIRROR),
				new Sphere3D( 16.5D, new Point3D(  73.0D,           16.5D,           78.0D),          new Color3D(),                    new Color3D(0.999D, 0.999D, 0.999D), Material.GLASS),
				new Sphere3D(600.0D, new Point3D(  50.0D,          681.6D - 0.27D,   81.6D),          new Color3D(12.0D, 12.0D, 12.0D), new Color3D(),                       Material.MATTE)
			};
		}
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		public Intersection intersection(final Ray3D ray) {
			Intersection intersection = null;
			
			for(final Sphere3D sphere : this.spheres) {
				final double t = sphere.intersection(ray);
				
				if(!isNaN(t) && (intersection == null || t < intersection.getT())) {
					intersection = sphere.toIntersection(ray, t);
				}
			}
			
			return intersection;
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static final class Sphere3D {
		private static final double EPSILON = 1.0e-4D;
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		private final Color3D albedo;
		private final Color3D emission;
		private final Material material;
		private final Point3D center;
		private final double radius;
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		public Sphere3D(final double radius, final Point3D center, final Color3D emission, final Color3D albedo, final Material material) {
			this.radius = radius;
			this.center = center;
			this.emission = emission;
			this.albedo = albedo;
			this.material = material;
		}
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		public Color3D getAlbedo() {
			return this.albedo;
		}
		
		public Color3D getEmission() {
			return this.emission;
		}
		
		public Intersection toIntersection(final Ray3D ray, final double t) {
			final Point3D origin = ray.getOrigin();
			
			final Vector3D direction = ray.getDirection();
			
			final Point3D surfaceIntersectionPoint = Point3D.add(origin, direction, t);
			
			final Vector3D surfaceNormal = Vector3D.normalize(Vector3D.direction(getCenter(), surfaceIntersectionPoint));
			
			return new Intersection(surfaceIntersectionPoint, this, surfaceNormal, t);
		}
		
		public Material getMaterial() {
			return this.material;
		}
		
		public Point3D getCenter() {
			return this.center;
		}
		
		public double getRadius() {
			return this.radius;
		}
		
		public double getRadiusSquared() {
			return getRadius() * getRadius();
		}
		
		public double intersection(final Ray3D ray) {
			return intersection(ray, EPSILON, MAX_VALUE);
		}
		
		public double intersection(final Ray3D ray, final double tMinimum, final double tMaximum) {
			final Point3D origin = ray.getOrigin();
			final Point3D center = getCenter();
			
			final Vector3D direction = ray.getDirection();
			final Vector3D centerToOrigin = Vector3D.direction(center, origin);
			
			final double radiusSquared = getRadiusSquared();
			
			final double a = direction.lengthSquared();
			final double b = 2.0D * Vector3D.dotProduct(centerToOrigin, direction);
			final double c = centerToOrigin.lengthSquared() - radiusSquared;
			
			final double[] ts = solveQuadraticSystem(a, b, c);
			
			final double t0 = ts[0];
			final double t1 = ts[1];
			
			final double t = !isNaN(t0) && t0 > tMinimum && t0 < tMaximum ? t0 : !isNaN(t1) && t1 > tMinimum && t1 < tMaximum ? t1 : Double.NaN;
			
			return t;
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static enum Material {
		GLASS,
		MATTE,
		METAL,
		MIRROR;
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		private Material() {
			
		}
	}
}