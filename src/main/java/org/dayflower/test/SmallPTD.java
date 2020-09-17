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
import static org.dayflower.util.Doubles.sqrt;

import org.dayflower.geometry.Vector3D;
import org.dayflower.image.Color3F;
import org.dayflower.image.Image;

public final class SmallPTD {
	private static final Sphere3D[] SPHERES = {
		new Sphere3D(1.0e5D, new Vector3D( 1.0e5D +  1.0D,   40.8D,           81.6D),          new Vector3D(),                    new Vector3D(0.750D, 0.250D, 0.250D), Material.DIFFUSE_LAMBERTIAN),
		new Sphere3D(1.0e5D, new Vector3D(-1.0e5D + 99.0D,   40.8D,           81.6D),          new Vector3D(),                    new Vector3D(0.250D, 0.250D, 0.750D), Material.DIFFUSE_LAMBERTIAN),
		new Sphere3D(1.0e5D, new Vector3D(  50.0D,           40.8D,          1.0e5D),          new Vector3D(),                    new Vector3D(0.750D, 0.750D, 0.750D), Material.DIFFUSE_LAMBERTIAN),
		new Sphere3D(1.0e5D, new Vector3D(  50.0D,           40.8D,         -1.0e5D + 170.0D), new Vector3D(),                    new Vector3D(),                       Material.DIFFUSE_LAMBERTIAN),
		new Sphere3D(1.0e5D, new Vector3D(  50.0D,          1.0e5D,           81.6D),          new Vector3D(),                    new Vector3D(0.750D, 0.750D, 0.750D), Material.DIFFUSE_LAMBERTIAN),
		new Sphere3D(1.0e5D, new Vector3D(  50.0D,         -1.0e5D + 81.6D,   81.6D),          new Vector3D(),                    new Vector3D(0.750D, 0.750D, 0.750D), Material.DIFFUSE_LAMBERTIAN),
		new Sphere3D( 16.5D, new Vector3D(  27.0D,           16.5D,           47.0D),          new Vector3D(),                    new Vector3D(0.999D, 0.999D, 0.999D), Material.GLOSSY_PHONG),
		new Sphere3D( 16.5D, new Vector3D(  73.0D,           16.5D,           78.0D),          new Vector3D(),                    new Vector3D(0.999D, 0.999D, 0.999D), Material.REFLECTIVE_AND_REFRACTIVE),
		new Sphere3D(600.0D, new Vector3D(  50.0D,          681.6D - 0.27D,   81.6D),          new Vector3D(12.0D, 12.0D, 12.0D), new Vector3D(),                       Material.DIFFUSE_LAMBERTIAN)
	};
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private SmallPTD() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static Vector3D radiance(final Ray3D r, final int depth) {
		final double[] t = new double[1];
		
		final int[] id = new int[1];
		
		if(!intersect(r, t, id)) {
			return new Vector3D();
		}
		
		final Sphere3D sphere = SPHERES[id[0]];
		
		final Vector3D x = Vector3D.add(r.o, Vector3D.multiply(r.d, t[0]));
		final Vector3D n = Vector3D.normalize(Vector3D.subtract(x, sphere.center));
		final Vector3D nl = Vector3D.dotProduct(n, r.d) < 0.0D ? n : Vector3D.multiply(n, -1.0D);
		
		Vector3D f = sphere.albedo;
		
		final int currentDepth = depth + 1;
		
		if(currentDepth > 5) {
			final double p = f.getX() > f.getY() && f.getX() > f.getZ() ? f.getX() : f.getY() > f.getZ() ? f.getY() : f.getZ();
			
			if(random() < p) {
				f = Vector3D.divide(f, p);
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
				
				return Vector3D.add(sphere.emission, Vector3D.multiply(f, radiance(new Ray3D(x, d), currentDepth)));
			}
			case GLOSSY_PHONG: {
				final double exponent = 20.0D;
				final double cosTheta = pow(1.0D - random(), 1.0D / (exponent + 1.0D));
				final double sinTheta = sqrt(max(0.0D, 1.0D - cosTheta * cosTheta));
				final double phi = PI * 2.0D * random();
				
				final Vector3D s = new Vector3D(cos(phi) * sinTheta, sin(phi) * sinTheta, cosTheta);
				final Vector3D w = Vector3D.normalize(Vector3D.subtract(r.d, Vector3D.multiply(Vector3D.multiply(n, 2.0D), Vector3D.dotProduct(n, r.d))));
				final Vector3D v = Vector3D.computeV(w);
				final Vector3D u = Vector3D.crossProduct(v, w);
//				final Vector3D u = Vector3D.normalize(abs(w.getX()) > 0.1D ? new Vec(w.getZ(), 0.0D, -w.getX()) : new Vec(0.0D, -w.getZ(), w.getY())));
//				final Vector3D v = Vector3D.crossProduct(w, u);
				final Vector3D d = Vector3D.normalize(new Vector3D(u.getX() * s.getX() + v.getX() * s.getY() + w.getX() * s.getZ(), u.getY() * s.getX() + v.getY() * s.getY() + w.getY() * s.getZ(), u.getZ() * s.getX() + v.getZ() * s.getY() + w.getZ() * s.getZ()));
				
				return Vector3D.add(sphere.emission, Vector3D.multiply(f, radiance(new Ray3D(x, d), currentDepth)));
			}
			case REFLECTIVE: {
				final Vector3D d = Vector3D.subtract(r.d, Vector3D.multiply(Vector3D.multiply(n, 2.0D), Vector3D.dotProduct(n, r.d)));
				
				return Vector3D.add(sphere.emission, Vector3D.multiply(f, radiance(new Ray3D(x, d), currentDepth)));
			}
			case REFLECTIVE_AND_REFRACTIVE: {
				final Ray3D reflectionRay = new Ray3D(x, Vector3D.subtract(r.d, Vector3D.multiply(Vector3D.multiply(n, 2.0D), Vector3D.dotProduct(n, r.d))));
				
				final boolean into = Vector3D.dotProduct(n, nl) > 0.0D;
				
				final double nc = 1.0D;
				final double nt = 1.5D;
				final double nnt = into ? nc / nt : nt / nc;
				final double ddn = Vector3D.dotProduct(r.d, nl);
				final double cos2t = 1.0D - nnt * nnt * (1.0D - ddn * ddn);
				
				if(cos2t < 0.0D) {
					return Vector3D.add(sphere.emission, Vector3D.multiply(f, radiance(reflectionRay, currentDepth)));
				}
				
				final Vector3D transmissionDirection = Vector3D.normalize(Vector3D.subtract(Vector3D.multiply(r.d, nnt), Vector3D.multiply(n, (into ? 1.0D : -1.0D) * (ddn * nnt + sqrt(cos2t)))));
				
				final double a = nt - nc;
				final double b = nt + nc;
				final double r0 = a * a / (b * b);
				final double c = 1.0D - (into ? -ddn : Vector3D.dotProduct(transmissionDirection, n));
				final double rE = r0 + (1.0D - r0) * c * c * c * c * c;
				final double tR = 1.0D - rE;
				final double p = 0.25D + 0.5D * rE;
				final double rP = rE / p;
				final double tP = tR / (1.0D - p);
				
				return Vector3D.add(sphere.emission, Vector3D.multiply(f, currentDepth > 2 ? (random() < p ? Vector3D.multiply(radiance(reflectionRay, currentDepth), rP) : Vector3D.multiply(radiance(new Ray3D(x, transmissionDirection), currentDepth), tP)) : Vector3D.add(Vector3D.multiply(radiance(reflectionRay, currentDepth), rE), Vector3D.multiply(radiance(new Ray3D(x, transmissionDirection), currentDepth), tR))));
			}
			default: {
				return new Vector3D();
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
	
	public static int toInt(final double v) {
		return (int)(pow(saturate(v), 1.0D / 2.2D) * 255.0D + 0.5D);
	}
	
	public static void main(final String[] args) {
		final int w = 1024;
		final int h = 768;
		final int samps = 10;
		
		final Ray3D camera = new Ray3D(new Vector3D(50.0D, 52.0D, 295.6D), Vector3D.normalize(new Vector3D(0.0D, -0.042612D, -1.0D)));
		
		final Vector3D cx = new Vector3D(w * 0.5135D / h, 0.0D, 0.0D);
		final Vector3D cy = Vector3D.multiply(Vector3D.normalize(Vector3D.crossProduct(cx, camera.d)), 0.5135D);
		
		final Vector3D[] colors = new Vector3D[w * h];
		
		for(int i = 0; i < colors.length; i++) {
			colors[i] = new Vector3D();
		}
		
		for(int y = 0; y < h; y++) {
			for(int x = 0; x < w; x++) {
				for(int sy = 0, i = (h - y - 1) * w + x; sy < 2; sy++) {
					for(int sx = 0; sx < 2; sx++) {
						Vector3D radiance = new Vector3D();
						
						for(int s = 0; s < samps; s++) {
							final double r1 = 2.0D * random();
							final double r2 = 2.0D * random();
							final double dx = r1 < 1.0D ? sqrt(r1) - 1.0D : 1.0D - sqrt(2.0D - r1);
							final double dy = r2 < 1.0D ? sqrt(r2) - 1.0D : 1.0D - sqrt(2.0D - r2);
							
							final Vector3D d = Vector3D.add(Vector3D.add(Vector3D.multiply(cx, ((sx + 0.5D + dx) / 2.0D + x) / w - 0.5D), Vector3D.multiply(cy, ((sy + 0.5D + dy) / 2.0D + y) / h - 0.5D)), camera.d);
							
							radiance = Vector3D.add(radiance, Vector3D.multiply(radiance(new Ray3D(Vector3D.add(camera.o, Vector3D.multiply(d, 140.0D)), Vector3D.normalize(d)), 0), 1.0D / samps));
						}
						
						colors[i] = Vector3D.add(colors[i], Vector3D.multiply(new Vector3D(saturate(radiance.getX()), saturate(radiance.getY()), saturate(radiance.getZ())), 0.25D));
					}
				}
			}
		}
		
		final Image image = new Image(w, h);
		
		for(int i = 0; i < colors.length; i++) {
			image.setColorRGB(new Color3F(toInt(colors[i].getX()), toInt(colors[i].getY()), toInt(colors[i].getZ())), i);
		}
		
		image.save("./generated/SmallPT.png");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static final class Ray3D {
		public final Vector3D d;
		public final Vector3D o;
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		public Ray3D(final Vector3D o, final Vector3D d) {
			this.o = o;
			this.d = d;
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static final class Sphere3D {
		private static final double EPSILON = 1.0e-4D;
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		public final Material material;
		public final Vector3D albedo;
		public final Vector3D center;
		public final Vector3D emission;
		public final double radius;
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		public Sphere3D(final double radius, final Vector3D center, final Vector3D emission, final Vector3D albedo, final Material material) {
			this.radius = radius;
			this.center = center;
			this.emission = emission;
			this.albedo = albedo;
			this.material = material;
		}
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		public double intersect(final Ray3D r) {
			final Vector3D op = Vector3D.subtract(this.center, r.o);
			
			final double b = Vector3D.dotProduct(op, r.d);
			final double det = b * b - Vector3D.dotProduct(op, op) + this.radius * this.radius;
			
			if(det < 0.0D) {
				return Double.NaN;
			}
			
			final double detSqrt = sqrt(det);
			final double t0 = b - detSqrt;
			
			if(t0 > EPSILON) {
				return t0;
			}
			
			final double t1 = b + detSqrt;
			
			if(t1 > EPSILON) {
				return t1;
			}
			
			return Double.NaN;
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