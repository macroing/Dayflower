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
package org.dayflower.renderer;

import static org.dayflower.util.Doubles.abs;
import static org.dayflower.util.Doubles.fresnelDielectricSchlick;
import static org.dayflower.util.Doubles.isNaN;
import static org.dayflower.util.Doubles.random;
import static org.dayflower.util.Doubles.solveQuadraticSystem;
import static org.dayflower.util.Doubles.sqrt;

import java.util.Objects;

import org.dayflower.geometry.Point2D;
import org.dayflower.geometry.Point3D;
import org.dayflower.geometry.Ray3D;
import org.dayflower.geometry.SampleGeneratorD;
import org.dayflower.geometry.Vector3D;
import org.dayflower.image.Color3D;
import org.dayflower.image.Color3F;
import org.dayflower.image.PixelImageF;

public final class SmallPTD {
	private SmallPTD() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static Color3D radiance(final Ray3D ray, final Scene scene, final int depth) {
		final Intersection intersection = scene.intersection(ray);
		
		if(intersection == null) {
			return Color3D.BLACK;
		}
		
		final Vector3D direction = ray.getDirection();
		
		final Sphere3D sphere = intersection.getSphere();
		
		final Point3D surfaceIntersectionPoint = intersection.getSurfaceIntersectionPoint();
		
		final Vector3D surfaceNormal = intersection.getSurfaceNormal();
		final Vector3D surfaceNormalCorrectlyOriented = Vector3D.dotProduct(surfaceNormal, direction) < 0.0D ? surfaceNormal : Vector3D.negate(surfaceNormal);
		
		Color3D albedo = sphere.getAlbedo();
		
		final Color3D emission = sphere.getEmission();
		
		final int currentDepth = depth + 1;
		
		if(currentDepth > 5) {
			final double probability = albedo.maximum();
			
			if(random() < probability) {
				albedo = Color3D.divide(albedo, probability);
			} else {
				return emission;
			}
		}
		
		switch(sphere.getMaterial()) {
			case DIFFUSE_LAMBERTIAN: {
				final Vector3D s = SampleGeneratorD.sampleHemisphereCosineDistribution2();
				final Vector3D w = surfaceNormalCorrectlyOriented;
				final Vector3D u = Vector3D.normalize(Vector3D.crossProduct(abs(w.getX()) > 0.1D ? Vector3D.y() : Vector3D.x(), w));
				final Vector3D v = Vector3D.crossProduct(w, u);
				final Vector3D d = Vector3D.normalize(Vector3D.add(Vector3D.multiply(u, s.getX()), Vector3D.multiply(v, s.getY()), Vector3D.multiply(w, s.getZ())));
				
				return Color3D.add(emission, Color3D.multiply(albedo, radiance(new Ray3D(surfaceIntersectionPoint, d), scene, currentDepth)));
			}
			case GLOSSY_PHONG: {
				final Vector3D s = SampleGeneratorD.sampleHemispherePowerCosineDistribution(random(), random(), 20.0D);
				final Vector3D w = Vector3D.normalize(Vector3D.reflection(direction, surfaceNormal, true));
				final Vector3D v = Vector3D.computeV(w);
				final Vector3D u = Vector3D.crossProduct(v, w);
				final Vector3D d = Vector3D.normalize(Vector3D.add(Vector3D.multiply(u, s.getX()), Vector3D.multiply(v, s.getY()), Vector3D.multiply(w, s.getZ())));
				
				return Color3D.add(emission, Color3D.multiply(albedo, radiance(new Ray3D(surfaceIntersectionPoint, d), scene, currentDepth)));
			}
			case REFLECTIVE: {
				final Vector3D d = Vector3D.reflection(direction, surfaceNormal, true);
				
				return Color3D.add(emission, Color3D.multiply(albedo, radiance(new Ray3D(surfaceIntersectionPoint, d), scene, currentDepth)));
			}
			case REFLECTIVE_AND_REFRACTIVE: {
				final Vector3D reflectionDirection = Vector3D.reflection(direction, surfaceNormal, true);
				
				final Ray3D reflectionRay = new Ray3D(surfaceIntersectionPoint, reflectionDirection);
				
				final boolean into = Vector3D.dotProduct(surfaceNormal, surfaceNormalCorrectlyOriented) > 0.0D;
				
				final double etaA = 1.0D;
				final double etaB = 1.5D;
				final double eta = into ? etaA / etaB : etaB / etaA;
				
				final double cosTheta = Vector3D.dotProduct(direction, surfaceNormalCorrectlyOriented);
				final double cosTheta2Squared = 1.0D - eta * eta * (1.0D - cosTheta * cosTheta);
				
				if(cosTheta2Squared < 0.0D) {
					return Color3D.add(emission, Color3D.multiply(albedo, radiance(reflectionRay, scene, currentDepth)));
				}
				
				final Vector3D transmissionDirection = Vector3D.normalize(Vector3D.subtract(Vector3D.multiply(direction, eta), Vector3D.multiply(surfaceNormal, (into ? 1.0D : -1.0D) * (cosTheta * eta + sqrt(cosTheta2Squared)))));
				
				final Ray3D transmissionRay = new Ray3D(surfaceIntersectionPoint, transmissionDirection);
				
				final double a = etaB - etaA;
				final double b = etaB + etaA;
				
				final double reflectance = fresnelDielectricSchlick(into ? -cosTheta : Vector3D.dotProduct(transmissionDirection, surfaceNormal), a * a / (b * b));
				final double transmittance = 1.0D - reflectance;
				
				final double probabilityRussianRoulette = 0.25D + 0.5D * reflectance;
				final double probabilityRussianRouletteReflection = reflectance / probabilityRussianRoulette;
				final double probabilityRussianRouletteTransmission = transmittance / (1.0D - probabilityRussianRoulette);
				
				final int type = currentDepth > 2 && random() < probabilityRussianRoulette ? 1 : currentDepth > 2 ? 2 : 3;
				
				switch(type) {
					case 1:
						return Color3D.add(emission, Color3D.multiply(albedo, radiance(reflectionRay, scene, currentDepth), probabilityRussianRouletteReflection));
					case 2:
						return Color3D.add(emission, Color3D.multiply(albedo, radiance(transmissionRay, scene, currentDepth), probabilityRussianRouletteTransmission));
					case 3:
						final Color3D radianceReflection = Color3D.multiply(radiance(reflectionRay, scene, currentDepth), reflectance);
						final Color3D radianceTransmission = Color3D.multiply(radiance(transmissionRay, scene, currentDepth), transmittance);
						
						return Color3D.add(emission, Color3D.multiply(albedo, Color3D.add(radianceReflection, radianceTransmission)));
					default:
						return Color3D.BLACK;
				}
			}
			default: {
				return Color3D.BLACK;
			}
		}
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
		
		pixelImage.redoGammaCorrectionSRGB();
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
			this.eye = new Point3D(50.0D, 52.0D, 295.6D);
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
			 * The list below shows some possible values assigned to either sampleU1 or sampleV1, both of which are in the range [-0.25D, +1.75D]:
			 * 
			 * sampleN1 #1: (0.0D + 0.5D + -1.0D) / 2.0D = -0.25D
			 * sampleN1 #2: (1.0D + 0.5D + -1.0D) / 2.0D = +0.25D
			 * sampleN1 #3: (2.0D + 0.5D + -1.0D) / 2.0D = +0.75D
			 * sampleN1 #4: (0.0D + 0.5D + +1.0D) / 2.0D = +0.75D
			 * sampleN1 #5: (1.0D + 0.5D + +1.0D) / 2.0D = +1.25D
			 * sampleN1 #6: (2.0D + 0.5D + +1.0D) / 2.0D = +1.75D
			 */
			
//			Generate a Point2D instance whose getU() and getV() methods returns a double in the range [-1.0D, +1.0D]:
			final Point2D sample = SampleGeneratorD.sampleExactInverseTentFilter();
			
//			The variables gridSampleU and gridSampleV are in the range [0.0D, 2.0D]:
			final double sampleU1 = (gridSampleU + 0.5D + sample.getU()) / 2.0D;
			final double sampleV1 = (gridSampleV + 0.5D + sample.getV()) / 2.0D;
			
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
				new Sphere3D(1.0e5D, new Point3D( 1.0e5D +  1.0D,   40.8D,           81.6D),          new Color3D(),                    new Color3D(0.750D, 0.250D, 0.250D), Material.DIFFUSE_LAMBERTIAN),
				new Sphere3D(1.0e5D, new Point3D(-1.0e5D + 99.0D,   40.8D,           81.6D),          new Color3D(),                    new Color3D(0.250D, 0.250D, 0.750D), Material.DIFFUSE_LAMBERTIAN),
				new Sphere3D(1.0e5D, new Point3D(  50.0D,           40.8D,          1.0e5D),          new Color3D(),                    new Color3D(0.750D, 0.750D, 0.750D), Material.DIFFUSE_LAMBERTIAN),
				new Sphere3D(1.0e5D, new Point3D(  50.0D,           40.8D,         -1.0e5D + 170.0D), new Color3D(),                    new Color3D(),                       Material.DIFFUSE_LAMBERTIAN),
				new Sphere3D(1.0e5D, new Point3D(  50.0D,          1.0e5D,           81.6D),          new Color3D(),                    new Color3D(0.750D, 0.750D, 0.750D), Material.DIFFUSE_LAMBERTIAN),
				new Sphere3D(1.0e5D, new Point3D(  50.0D,         -1.0e5D + 81.6D,   81.6D),          new Color3D(),                    new Color3D(0.750D, 0.750D, 0.750D), Material.DIFFUSE_LAMBERTIAN),
				new Sphere3D( 16.5D, new Point3D(  27.0D,           16.5D,           47.0D),          new Color3D(),                    new Color3D(0.999D, 0.999D, 0.999D), Material.REFLECTIVE),
				new Sphere3D( 16.5D, new Point3D(  73.0D,           16.5D,           78.0D),          new Color3D(),                    new Color3D(0.999D, 0.999D, 0.999D), Material.REFLECTIVE_AND_REFRACTIVE),
				new Sphere3D(600.0D, new Point3D(  50.0D,          681.6D - 0.27D,   81.6D),          new Color3D(12.0D, 12.0D, 12.0D), new Color3D(),                       Material.DIFFUSE_LAMBERTIAN)
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
			return intersection(ray, EPSILON, Double.MAX_VALUE);
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
		DIFFUSE_LAMBERTIAN,
		GLOSSY_PHONG,
		REFLECTIVE,
		REFLECTIVE_AND_REFRACTIVE;
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		private Material() {
			
		}
	}
}