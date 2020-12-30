/**
 * Copyright 2020 - 2021 J&#246;rgen Lundgren
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

import static org.dayflower.util.Doubles.PI;
import static org.dayflower.util.Doubles.abs;
import static org.dayflower.util.Doubles.cos;
import static org.dayflower.util.Doubles.isNaN;
import static org.dayflower.util.Doubles.pow;
import static org.dayflower.util.Doubles.random;
import static org.dayflower.util.Doubles.sin;
import static org.dayflower.util.Doubles.sqrt;

import org.dayflower.image.Color3F;
import org.dayflower.image.PixelImage;

public final class SmallPT {
	private static final Sphere[] SPHERES = {
		new Sphere(1.0e5D, new Vec( 1.0e5D +  1.0D,   40.8D,           81.6D),          new Vec(),                    new Vec(0.75D, 0.25D, 0.25D),                  Refl.DIFF),
		new Sphere(1.0e5D, new Vec(-1.0e5D + 99.0D,   40.8D,           81.6D),          new Vec(),                    new Vec(0.25D, 0.25D, 0.75D),                  Refl.DIFF),
		new Sphere(1.0e5D, new Vec(  50.0D,           40.8D,          1.0e5D),          new Vec(),                    new Vec(0.75D, 0.75D, 0.75D),                  Refl.DIFF),
		new Sphere(1.0e5D, new Vec(  50.0D,           40.8D,         -1.0e5D + 170.0D), new Vec(),                    new Vec(),                                     Refl.DIFF),
		new Sphere(1.0e5D, new Vec(  50.0D,          1.0e5D,           81.6D),          new Vec(),                    new Vec(0.75D, 0.75D, 0.75D),                  Refl.DIFF),
		new Sphere(1.0e5D, new Vec(  50.0D,         -1.0e5D + 81.6D,   81.6D),          new Vec(),                    new Vec(0.75D, 0.75D, 0.75D),                  Refl.DIFF),
		new Sphere( 16.5D, new Vec(  27.0D,           16.5D,           47.0D),          new Vec(),                    new Vec( 1.0D,  1.0D,  1.0D).multiply(0.999D), Refl.SPEC),
		new Sphere( 16.5D, new Vec(  73.0D,           16.5D,           78.0D),          new Vec(),                    new Vec( 1.0D,  1.0D,  1.0D).multiply(0.999D), Refl.REFR),
		new Sphere(600.0D, new Vec(  50.0D,          681.6D - 0.27D,   81.6D),          new Vec(12.0D, 12.0D, 12.0D), new Vec(),                                     Refl.DIFF)
	};
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private SmallPT() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static boolean intersect(final Ray r, final double[] t, final int[] id) {
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
	
	public static double clamp(final double v) {
		return v < 0.0D ? 0.0D : v > 1.0D ? 1.0D : v;
	}
	
	public static int toInt(final double v) {
		return (int)(pow(clamp(v), 1.0D / 2.2D) * 255.0D + 0.5D);
	}
	
	public static Vec radiance(final Ray r, final int depth) {
		final double[] t = new double[1];
		
		final int[] id = new int[1];
		
		if(!intersect(r, t, id)) {
			return new Vec();
		}
		
		final Sphere sphere = SPHERES[id[0]];
		
		final Vec x = r.o.add(r.d.multiply(t[0]));
		final Vec n = x.subtract(sphere.p).normalize();
		final Vec nl = n.dotProduct(r.d) < 0.0D ? n : n.multiply(-1.0D);
		
		Vec f = sphere.c;
		
		final int currentDepth = depth + 1;
		
		if(currentDepth > 5) {
			final double p = f.x > f.y && f.x > f.z ? f.x : f.y > f.z ? f.y : f.z;
			
			if(random() < p) {
				f = f.multiply(1.0D / p);
			} else {
				return sphere.e;
			}
		}
		
		if(sphere.refl == Refl.DIFF) {
			final double r1 = 2.0D * PI * random();
			final double r2 = random();
			final double r2s = sqrt(r2);
			
			final Vec w = nl;
			final Vec u = (abs(w.x) > 0.1D ? new Vec(0.0D, 1.0D, 0.0D) : new Vec(1.0D, 0.0D, 0.0D)).crossProduct(w).normalize();
			final Vec v = w.crossProduct(u);
			final Vec d = u.multiply(cos(r1)).multiply(r2s).add(v.multiply(sin(r1)).multiply(r2s)).add(w.multiply(sqrt(1.0D - r2))).normalize();
			
			return sphere.e.add(f.multiply(radiance(new Ray(x, d), currentDepth)));
		} else if(sphere.refl == Refl.SPEC) {
			return sphere.e.add(f.multiply(radiance(new Ray(x, r.d.subtract(n.multiply(2.0D).multiply(n.dotProduct(r.d)))), currentDepth)));
		}
		
		final Ray reflRay = new Ray(x, r.d.subtract(n.multiply(2.0D).multiply(n.dotProduct(r.d))));
		
		final boolean into = n.dotProduct(nl) > 0.0D;
		
		final double nc = 1.0D;
		final double nt = 1.5D;
		final double nnt = into ? nc / nt : nt / nc;
		final double ddn = r.d.dotProduct(nl);
		final double cos2t = 1.0D - nnt * nnt * (1.0D - ddn * ddn);
		
		if(cos2t < 0.0D) {
			return sphere.e.add(f.multiply(radiance(reflRay, currentDepth)));
		}
		
		final Vec tDir = r.d.multiply(nnt).subtract(n.multiply((into ? 1.0D : -1.0D) * (ddn * nnt + sqrt(cos2t)))).normalize();
		
		final double a = nt - nc;
		final double b = nt + nc;
		final double r0 = a * a / (b * b);
		final double c = 1.0D - (into ? -ddn : tDir.dotProduct(n));
		final double rE = r0 + (1.0D - r0) * c * c * c * c * c;
		final double tR = 1.0D - rE;
		final double p = 0.25D + 0.5D * rE;
		final double rP = rE / p;
		final double tP = tR / (1.0D - p);
		
		if(currentDepth > 2) {
			if(random() < p) {
				return sphere.e.add(f.multiply(radiance(reflRay, currentDepth).multiply(rP)));
			}
			
			return sphere.e.add(f.multiply(radiance(new Ray(x, tDir), currentDepth).multiply(tP)));
		}
		
		return sphere.e.add(f.multiply(radiance(reflRay, currentDepth).multiply(rE).add(radiance(new Ray(x, tDir), currentDepth).multiply(tR))));
	}
	
	public static void main(final String[] args) {
		final int w = 1024;
		final int h = 768;
		final int samps = 10;
		
		final Ray cam = new Ray(new Vec(50.0D, 52.0D, 295.6D), new Vec(0.0D, -0.042612D, -1.0D).normalize());
		
		final Vec cx = new Vec(w * 0.5135D / h, 0.0D, 0.0D);
		final Vec cy = cx.crossProduct(cam.d).normalize().multiply(0.5135D);
		
		final Vec[] c = new Vec[w * h];
		
		for(int i = 0; i < c.length; i++) {
			c[i] = new Vec();
		}
		
		for(int y = 0; y < h; y++) {
			for(int x = 0; x < w; x++) {
				for(int sy = 0, i = (h - y - 1) * w + x; sy < 2; sy++) {
					for(int sx = 0; sx < 2; sx++) {
						Vec r = new Vec();
						
						for(int s = 0; s < samps; s++) {
							final double r1 = 2.0D * random();
							final double dx = r1 < 1.0D ? sqrt(r1) - 1.0D : 1.0D - sqrt(2.0D - r1);
							final double r2 = 2.0D * random();
							final double dy = r2 < 1.0D ? sqrt(r2) - 1.0D : 1.0D - sqrt(2.0D - r2);
							
							final Vec d = cx.multiply(((sx + 0.5D + dx) / 2.0D + x) / w - 0.5D).add(cy.multiply(((sy + 0.5D + dy) / 2.0D + y) / h - 0.5D)).add(cam.d);
							
							r = r.add(radiance(new Ray(cam.o.add(d.multiply(140.0D)), d.normalize()), 0).multiply(1.0D / samps));
						}
						
						c[i] = c[i].add(new Vec(clamp(r.x), clamp(r.y), clamp(r.z)).multiply(0.25D));
					}
				}
			}
		}
		
		final PixelImage pixelImage = new PixelImage(w, h);
		
		for(int i = 0; i < c.length; i++) {
			pixelImage.setColorRGB(new Color3F(toInt(c[i].x), toInt(c[i].y), toInt(c[i].z)), i);
		}
		
		pixelImage.save("./generated/SmallPT-Reference.png");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static final class Ray {
		public final Vec d;
		public final Vec o;
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		public Ray(final Vec o, final Vec d) {
			this.o = o;
			this.d = d;
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static final class Sphere {
		public final double rad;
		public final Refl refl;
		public final Vec c;
		public final Vec e;
		public final Vec p;
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		public Sphere(final double rad, final Vec p, final Vec e, final Vec c, final Refl refl) {
			this.rad = rad;
			this.p = p;
			this.e = e;
			this.c = c;
			this.refl = refl;
		}
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		public double intersect(final Ray r) {
			final Vec op = this.p.subtract(r.o);
			
			final double eps = 1.0e-4D;
			final double b = op.dotProduct(r.d);
			final double det = b * b - op.dotProduct(op) + this.rad * this.rad;
			
			if(det < 0.0D) {
				return Double.NaN;
			}
			
			final double detSqrt = sqrt(det);
			final double t0 = b - detSqrt;
			
			if(t0 > eps) {
				return t0;
			}
			
			final double t1 = b + detSqrt;
			
			if(t1 > eps) {
				return t1;
			}
			
			return Double.NaN;
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static final class Vec {
		public final double x;
		public final double y;
		public final double z;
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		public Vec() {
			this(0.0D, 0.0D, 0.0D);
		}
		
		public Vec(final double x, final double y, final double z) {
			this.x = x;
			this.y = y;
			this.z = z;
		}
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		public double dotProduct(final Vec v) {
			return this.x * v.x + this.y * v.y + this.z * v.z;
		}
		
		public Vec add(final Vec v) {
			return new Vec(this.x + v.x, this.y + v.y, this.z + v.z);
		}
		
		public Vec crossProduct(final Vec v) {
			return new Vec(this.y * v.z - this.z * v.y, this.z * v.x - this.x * v.z, this.x * v.y - this.y * v.x);
		}
		
		public Vec multiply(final double s) {
			return new Vec(this.x * s, this.y * s, this.z * s);
		}
		
		public Vec multiply(final Vec v) {
			return new Vec(this.x * v.x, this.y * v.y, this.z * v.z);
		}
		
		public Vec normalize() {
			return multiply(1.0D / sqrt(this.x * this.x + this.y * this.y + this.z * this.z));
		}
		
		public Vec subtract(final Vec v) {
			return new Vec(this.x - v.x, this.y - v.y, this.z - v.z);
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static enum Refl {
		DIFF,
		REFR,
		SPEC;
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		private Refl() {
			
		}
	}
}