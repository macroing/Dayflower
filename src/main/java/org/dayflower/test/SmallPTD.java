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
package org.dayflower.test;

import static org.dayflower.util.Doubles.PI;
import static org.dayflower.util.Doubles.abs;
import static org.dayflower.util.Doubles.cos;
import static org.dayflower.util.Doubles.isNaN;
import static org.dayflower.util.Doubles.max;
import static org.dayflower.util.Doubles.pow;
import static org.dayflower.util.Doubles.random;
import static org.dayflower.util.Doubles.saturate;
import static org.dayflower.util.Doubles.sin;
import static org.dayflower.util.Doubles.solveQuadraticSystem;
import static org.dayflower.util.Doubles.sqrt;

import org.dayflower.geometry.Point2D;
import org.dayflower.geometry.Point3D;
import org.dayflower.geometry.Ray3D;
import org.dayflower.geometry.SampleGeneratorD;
import org.dayflower.geometry.Vector3D;
import org.dayflower.image.Color3D;
import org.dayflower.image.Color3F;
import org.dayflower.image.Image;

public final class SmallPTD {
	private static final Sphere3D[] SPHERES = {
		new Sphere3D(1.0e5D, new Point3D( 1.0e5D +  1.0D,   40.8D,           81.6D),          new Color3D(),                    new Color3D(0.750D, 0.250D, 0.250D), Material.DIFFUSE_LAMBERTIAN),
		new Sphere3D(1.0e5D, new Point3D(-1.0e5D + 99.0D,   40.8D,           81.6D),          new Color3D(),                    new Color3D(0.250D, 0.250D, 0.750D), Material.DIFFUSE_LAMBERTIAN),
		new Sphere3D(1.0e5D, new Point3D(  50.0D,           40.8D,          1.0e5D),          new Color3D(),                    new Color3D(0.750D, 0.750D, 0.750D), Material.DIFFUSE_LAMBERTIAN),
		new Sphere3D(1.0e5D, new Point3D(  50.0D,           40.8D,         -1.0e5D + 170.0D), new Color3D(),                    new Color3D(),                       Material.DIFFUSE_LAMBERTIAN),
		new Sphere3D(1.0e5D, new Point3D(  50.0D,          1.0e5D,           81.6D),          new Color3D(),                    new Color3D(0.750D, 0.750D, 0.750D), Material.DIFFUSE_LAMBERTIAN),
		new Sphere3D(1.0e5D, new Point3D(  50.0D,         -1.0e5D + 81.6D,   81.6D),          new Color3D(),                    new Color3D(0.750D, 0.750D, 0.750D), Material.DIFFUSE_LAMBERTIAN),
		new Sphere3D( 16.5D, new Point3D(  27.0D,           16.5D,           47.0D),          new Color3D(),                    new Color3D(0.999D, 0.999D, 0.999D), Material.GLOSSY_PHONG),
		new Sphere3D( 16.5D, new Point3D(  73.0D,           16.5D,           78.0D),          new Color3D(),                    new Color3D(0.999D, 0.999D, 0.999D), Material.REFLECTIVE_AND_REFRACTIVE),
		new Sphere3D(600.0D, new Point3D(  50.0D,          681.6D - 0.27D,   81.6D),          new Color3D(12.0D, 12.0D, 12.0D), new Color3D(),                       Material.DIFFUSE_LAMBERTIAN)
	};
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private SmallPTD() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static Color3D radiance(final Ray3D ray, final int depth) {
		final double[] t = new double[1];
		
		final int[] id = new int[1];
		
		if(!intersect(ray, t, id)) {
			return new Color3D();
		}
		
		final Sphere3D sphere = SPHERES[id[0]];
		
		final Point3D x = Point3D.add(ray.getOrigin(), ray.getDirection(), t[0]);
		
		final Vector3D n = Vector3D.normalize(Vector3D.direction(sphere.center, x));
		final Vector3D nl = Vector3D.dotProduct(n, ray.getDirection()) < 0.0D ? n : Vector3D.multiply(n, -1.0D);
		
		Color3D f = sphere.albedo;
		
		final int currentDepth = depth + 1;
		
		if(currentDepth > 5) {
			final double p = f.maximum();
			
			if(random() < p) {
				f = Color3D.divide(f, p);
			} else {
				return sphere.emission;
			}
		}
		
		switch(sphere.material) {
			case DIFFUSE_LAMBERTIAN: {
				final double r1 = 2.0D * PI * random();
				final double r2 = random();
				final double r2s = sqrt(r2);
				
				final Vector3D w = nl;
				final Vector3D u = Vector3D.normalize(Vector3D.crossProduct(abs(w.getX()) > 0.1D ? new Vector3D(0.0D, 1.0D, 0.0D) : new Vector3D(1.0D, 0.0D, 0.0D), w));
				final Vector3D v = Vector3D.crossProduct(w, u);
				final Vector3D d = Vector3D.normalize(Vector3D.add(Vector3D.add(Vector3D.multiply(Vector3D.multiply(u, cos(r1)), r2s), Vector3D.multiply(Vector3D.multiply(v, sin(r1)), r2s)), Vector3D.multiply(w, sqrt(1.0D - r2))));
				
				return Color3D.add(sphere.emission, Color3D.multiply(f, radiance(new Ray3D(x, d), currentDepth)));
			}
			case GLOSSY_PHONG: {
				final double exponent = 20.0D;
				final double cosTheta = pow(1.0D - random(), 1.0D / (exponent + 1.0D));
				final double sinTheta = sqrt(max(0.0D, 1.0D - cosTheta * cosTheta));
				final double phi = PI * 2.0D * random();
				
				final Vector3D s = new Vector3D(cos(phi) * sinTheta, sin(phi) * sinTheta, cosTheta);
				final Vector3D w = Vector3D.normalize(Vector3D.subtract(ray.getDirection(), Vector3D.multiply(Vector3D.multiply(n, 2.0D), Vector3D.dotProduct(n, ray.getDirection()))));
				final Vector3D v = Vector3D.computeV(w);
				final Vector3D u = Vector3D.crossProduct(v, w);
//				final Vector3D u = Vector3D.normalize(abs(w.getX()) > 0.1D ? new Vec(w.getZ(), 0.0D, -w.getX()) : new Vec(0.0D, -w.getZ(), w.getY())));
//				final Vector3D v = Vector3D.crossProduct(w, u);
				final Vector3D d = Vector3D.normalize(new Vector3D(u.getX() * s.getX() + v.getX() * s.getY() + w.getX() * s.getZ(), u.getY() * s.getX() + v.getY() * s.getY() + w.getY() * s.getZ(), u.getZ() * s.getX() + v.getZ() * s.getY() + w.getZ() * s.getZ()));
				
				return Color3D.add(sphere.emission, Color3D.multiply(f, radiance(new Ray3D(x, d), currentDepth)));
			}
			case REFLECTIVE: {
				final Vector3D d = Vector3D.subtract(ray.getDirection(), Vector3D.multiply(Vector3D.multiply(n, 2.0D), Vector3D.dotProduct(n, ray.getDirection())));
				
				return Color3D.add(sphere.emission, Color3D.multiply(f, radiance(new Ray3D(x, d), currentDepth)));
			}
			case REFLECTIVE_AND_REFRACTIVE: {
				final Ray3D reflectionRay = new Ray3D(x, Vector3D.subtract(ray.getDirection(), Vector3D.multiply(Vector3D.multiply(n, 2.0D), Vector3D.dotProduct(n, ray.getDirection()))));
				
				final boolean into = Vector3D.dotProduct(n, nl) > 0.0D;
				
				final double nc = 1.0D;
				final double nt = 1.5D;
				final double nnt = into ? nc / nt : nt / nc;
				final double ddn = Vector3D.dotProduct(ray.getDirection(), nl);
				final double cos2t = 1.0D - nnt * nnt * (1.0D - ddn * ddn);
				
				if(cos2t < 0.0D) {
					return Color3D.add(sphere.emission, Color3D.multiply(f, radiance(reflectionRay, currentDepth)));
				}
				
				final Vector3D transmissionDirection = Vector3D.normalize(Vector3D.subtract(Vector3D.multiply(ray.getDirection(), nnt), Vector3D.multiply(n, (into ? 1.0D : -1.0D) * (ddn * nnt + sqrt(cos2t)))));
				
				final double a = nt - nc;
				final double b = nt + nc;
				final double r0 = a * a / (b * b);
				final double c = 1.0D - (into ? -ddn : Vector3D.dotProduct(transmissionDirection, n));
				final double rE = r0 + (1.0D - r0) * c * c * c * c * c;
				final double tR = 1.0D - rE;
				final double p = 0.25D + 0.5D * rE;
				final double rP = rE / p;
				final double tP = tR / (1.0D - p);
				
				return Color3D.add(sphere.emission, Color3D.multiply(f, currentDepth > 2 ? (random() < p ? Color3D.multiply(radiance(reflectionRay, currentDepth), rP) : Color3D.multiply(radiance(new Ray3D(x, transmissionDirection), currentDepth), tP)) : Color3D.add(Color3D.multiply(radiance(reflectionRay, currentDepth), rE), Color3D.multiply(radiance(new Ray3D(x, transmissionDirection), currentDepth), tR))));
			}
			default: {
				return new Color3D();
			}
		}
	}
	
	public static boolean intersect(final Ray3D r, final double[] t, final int[] id) {
		t[0] = Double.NaN;
		
		for(int i = 0; i < SPHERES.length; i++) {
			final double currentT = SPHERES[i].intersect(r);
			
			if(!isNaN(currentT) && (isNaN(t[0]) || currentT < t[0])) {
				t[0] = currentT;
				
				id[0] = i;
			}
		}
		
		return !isNaN(t[0]);
	}
	
	public static void main(final String[] args) {
		final int resolutionX = 1024;
		final int resolutionY = 768;
		final int samples = 10;
		
		final Camera camera = new Camera(resolutionX, resolutionY);
		
		final Color3D[] colors = Color3D.array(resolutionX * resolutionY);
		
		for(int y = 0; y < resolutionY; y++) {
			for(int x = 0; x < resolutionX; x++) {
				for(int sy = 0, i = (resolutionY - y - 1) * resolutionX + x; sy < 2; sy++) {
					for(int sx = 0; sx < 2; sx++) {
						Color3D radiance = Color3D.BLACK;
						
						for(int s = 0; s < samples; s++) {
							final Ray3D ray = camera.generatePrimaryRay(x, y, sx, sy);
							
							radiance = Color3D.add(radiance, Color3D.multiply(radiance(ray, 0), 1.0D / samples));
						}
						
						colors[i] = Color3D.add(colors[i], Color3D.multiply(new Color3D(saturate(radiance.getX()), saturate(radiance.getY()), saturate(radiance.getZ())), 0.25D));
					}
				}
			}
		}
		
		final Image image = new Image(resolutionX, resolutionY);
		
		for(int i = 0; i < colors.length; i++) {
			image.setColorRGB(new Color3F(colors[i]), i);
		}
		
		image.redoGammaCorrectionSRGB();
		image.save("./generated/SmallPT.png");
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
			this.eye = new Point3D(50.0D, 52.0D, 295.6D);
			this.w = Vector3D.normalize(new Vector3D(0.0D, -0.042612D, -1.0D));
			this.u = new Vector3D(resolutionX * 0.5135D / resolutionY, 0.0D, 0.0D);
			this.v = Vector3D.multiply(Vector3D.normalize(Vector3D.crossProduct(this.u, this.w)), 0.5135D);
			this.resolutionX = resolutionX;
			this.resolutionY = resolutionY;
		}
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		public Ray3D generatePrimaryRay(final int x, final int y, final int sx, final int sy) {
			final Point2D sample = SampleGeneratorD.sampleExactInverseTentFilter();
			
			final Vector3D u = Vector3D.multiply(this.u, ((sx + 0.5D + sample.getU()) / 2.0D + x) / this.resolutionX - 0.5D);
			final Vector3D v = Vector3D.multiply(this.v, ((sy + 0.5D + sample.getV()) / 2.0D + y) / this.resolutionY - 0.5D);
			final Vector3D w = this.w;
			
			final Vector3D direction = Vector3D.add(Vector3D.add(u, v), w);
			
			final Point3D origin = Point3D.add(this.eye, Vector3D.multiply(direction, 140.0D));
			
			final Vector3D directionNormalized = Vector3D.normalize(direction);
			
			return new Ray3D(origin, directionNormalized);
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static final class Sphere3D {
		private static final double EPSILON = 1.0e-4D;
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		public final Color3D albedo;
		public final Color3D emission;
		public final Material material;
		public final Point3D center;
		public final double radius;
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		public Sphere3D(final double radius, final Point3D center, final Color3D emission, final Color3D albedo, final Material material) {
			this.radius = radius;
			this.center = center;
			this.emission = emission;
			this.albedo = albedo;
			this.material = material;
		}
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		public double intersect(final Ray3D ray) {
			return intersect(ray, EPSILON, Double.MAX_VALUE);
		}
		
		public double intersect(final Ray3D ray, final double tMinimum, final double tMaximum) {
			final Point3D origin = ray.getOrigin();
			final Point3D center = this.center;
			
			final Vector3D direction = ray.getDirection();
			final Vector3D centerToOrigin = Vector3D.direction(center, origin);
			
			final double radiusSquared = this.radius * this.radius;
			
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