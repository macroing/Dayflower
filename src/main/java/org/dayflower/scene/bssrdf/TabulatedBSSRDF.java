/**
 * Copyright 2014 - 2023 J&#246;rgen Lundgren
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
package org.dayflower.scene.bssrdf;

import java.lang.reflect.Field;//TODO: Add Javadocs!
import java.util.Objects;
import java.util.Optional;

import org.dayflower.geometry.OrthonormalBasis33F;
import org.dayflower.geometry.Point2F;
import org.dayflower.geometry.Point3F;
import org.dayflower.geometry.Ray3F;
import org.dayflower.geometry.Vector3F;
import org.dayflower.scene.BSDF;
import org.dayflower.scene.BSSRDFResult;
import org.dayflower.scene.BXDF;
import org.dayflower.scene.Intersection;
import org.dayflower.scene.Material;
import org.dayflower.scene.Scene;
import org.dayflower.scene.TransportMode;

import org.macroing.art4j.color.Color3F;
import org.macroing.java.lang.Floats;
import org.macroing.java.lang.Ints;

/**
 * A {@code TabulatedBSSRDF} represents a tabulated BSSRDF (Bidirectional Scattering Surface Reflectance Distribution Function).
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class TabulatedBSSRDF extends SeparableBSSRDF {
	private final BSSRDFTable bSSRDFTable;
	private final Color3F rho;
	private final Color3F sigmaT;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public TabulatedBSSRDF(final Intersection intersection, final float eta, final Material material, final TransportMode transportMode, final Color3F sigmaA, final Color3F sigmaS, final BSSRDFTable bSSRDFTable) {
		super(intersection, eta, material, transportMode);
		
		this.bSSRDFTable = Objects.requireNonNull(bSSRDFTable, "bSSRDFTable == null");
		this.sigmaT = Color3F.add(sigmaA, sigmaS);
		this.rho = new Color3F(this.sigmaT.r != 0.0F ? sigmaS.r / this.sigmaT.r : 0.0F, this.sigmaT.g != 0.0F ? sigmaS.g / this.sigmaT.g : 0.0F, this.sigmaT.b != 0.0F ? sigmaS.b / this.sigmaT.b : 0.0F);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	@Override
	public BSSRDFResult sampleS(final Scene scene, final float u1, final Point2F u2) {
		final SeparableBSSRDFResult separableBSSRDFResult = sampleSP(scene, u1, u2);
		
		final Color3F result = separableBSSRDFResult.getResult();
		
		final Intersection intersection = separableBSSRDFResult.getIntersection();
		
		final float probabilityDensityFunctionValue = separableBSSRDFResult.getProbabilityDensityFunctionValue();
		
		if(!result.isBlack() && intersection != null) {
			final BXDF bXDF = new SeparableBSSRDFBXDF(this);
			
			final BSDF bSDF = new BSDF(separableBSSRDFResult.getIntersection(), bXDF);
			
			final Vector3F outgoing = intersection.getSurfaceNormalS();
			
			return new BSSRDFResult(result, intersection, probabilityDensityFunctionValue, bSDF, outgoing);
		}
		
		return new BSSRDFResult(result, intersection, probabilityDensityFunctionValue);
	}
	
//	TODO: Add Javadocs!
	@Override
	public Color3F evaluateSR(final float distance) {
		final float[] srResult = {0.0F, 0.0F, 0.0F};
		final float[] sigmaT = {this.sigmaT.r, this.sigmaT.g, this.sigmaT.b};
		final float[] rho = {this.rho.r, this.rho.g, this.rho.b};
		
		for(int i = 0; i < 3; i++) {
			final float rOptical = distance * sigmaT[i];
			
			final int[] rhoOffset = new int[1];
			final int[] radiusOffset = new int[1];
			
			final float[] rhoWeights = new float[4];
			final float[] radiusWeights = new float[4];
			
			if(!Utilities.catmullRomWeights(this.bSSRDFTable.getRhoSamples().length, this.bSSRDFTable.getRhoSamples(), rho[i], rhoOffset, rhoWeights) || !Utilities.catmullRomWeights(this.bSSRDFTable.getRadiusSamples().length, this.bSSRDFTable.getRadiusSamples(), rOptical, radiusOffset, radiusWeights)) {
				continue;
			}
			
			float sr = 0.0F;
			
			for(int j = 0; j < 4; j++) {
				for(int k = 0; k < 4; k++) {
					final float weight = rhoWeights[j] * radiusWeights[k];
					
					if(weight != 0.0F) {
						sr += weight * this.bSSRDFTable.evaluateProfile(rhoOffset[0] + j, radiusOffset[0] + k);
					}
				}
			}
			
			if(rOptical != 0.0F) {
				sr /= Floats.PI_MULTIPLIED_BY_2 * rOptical;
			}
			
			srResult[i] = sr;
		}
		
		for(int i = 0; i < 3; i++) {
			srResult[i] *= sigmaT[i] * sigmaT[i];
			srResult[i] = Floats.saturate(srResult[i]);
		}
		
		return new Color3F(srResult[0], srResult[1], srResult[2]);
	}
	
//	TODO: Add Javadocs!
	@Override
	public SeparableBSSRDFResult sampleSP(final Scene scene, final float u1, final Point2F u2) {
		final OrthonormalBasis33F orthonormalBasis = getIntersection().getOrthonormalBasisS();
		
		final Vector3F u = orthonormalBasis.u;
		final Vector3F v = orthonormalBasis.v;
		final Vector3F w = orthonormalBasis.w;
		
		Vector3F vx;
		Vector3F vy;
		Vector3F vz;
		
		float u3 = u1;
		
		if(u3 < 0.5F) {
			vx = u;
			vy = v;
			vz = w;
			
			u3 *= 2.0F;
		} else if(u3 < 0.75F) {
			vx = v;
			vy = w;
			vz = u;
			
			u3 = (u3 - 0.5F) * 4.0F;
		} else {
			vx = w;
			vy = u;
			vz = v;
			
			u3 = (u3 - 0.75F) * 4.0F;
		}
		
		final int index = Ints.saturate((int)(u3 * 3), 0, 2);
		
		u3 = u3 * 3 - index;
		
		final float r = sampleSR(index, u2.x);
		
		if(r < 0.0F) {
			return new SeparableBSSRDFResult(Color3F.BLACK, null, 0.0F);
		}
		
		final float phi = Floats.PI_MULTIPLIED_BY_2 * u2.y;
		final float phiCos = Floats.cos(phi);
		final float phiSin = Floats.sin(phi);
		
		final float rMax = sampleSR(index, 0.999F);
		
		if(r >= rMax) {
			return new SeparableBSSRDFResult(Color3F.BLACK, null, 0.0F);
		}
		
		final float l = 2.0F * Floats.sqrt(rMax * rMax - r * r);
		
		final Point3F p0 = getIntersection().getSurfaceIntersectionPoint();
		
		final float x = p0.x + r * (vx.x * phiCos + vy.x * phiSin) - l * vz.x * 0.5F;
		final float y = p0.y + r * (vx.y * phiCos + vy.y * phiSin) - l * vz.y * 0.5F;
		final float z = p0.z + r * (vx.z * phiCos + vy.z * phiSin) - l * vz.z * 0.5F;
		
		final Point3F p1 = new Point3F(x, y, z);
		final Point3F p2 = Point3F.add(p1, vz, l);
		
		Intersection intersection = null;
		IntersectionChain intersectionChain = new IntersectionChain();
		IntersectionChain intersectionChainFirst = intersectionChain;
		
		int found = 0;
		
		while(true) {
			final Ray3F ray = intersection == null ? new Ray3F(p1, Vector3F.directionNormalized(p1, p2)) : intersection.createRay(p2);
			
			if(ray.getDirection().equals(new Vector3F(0.0F, 0.0F, 0.0F))) {
				break;
			}
			
			final Optional<Intersection> optionalIntersection = scene.intersection(ray, 0.0F, Floats.MAX_VALUE);
			
			if(optionalIntersection.isEmpty()) {
				break;
			}
			
			intersection = optionalIntersection.get();
			
			intersectionChain.setIntersection(intersection);
			
			if(intersection.getPrimitive().getMaterial() == getMaterial()) {
				final IntersectionChain newIntersectionChain = new IntersectionChain();
				
				intersectionChain.setIntersectionChain(newIntersectionChain);
				
				intersectionChain = newIntersectionChain;
				
				found++;
			}
		}
		
		if(found == 0) {
			return new SeparableBSSRDFResult(Color3F.BLACK, null, 0.0F);
		}
		
		int selected = Ints.saturate((int)(u3 * found), 0, found - 1);
		
		while(selected-- > 0) {
			intersectionChainFirst = intersectionChainFirst.getIntersectionChain();
		}
		
		final Intersection intersectionSelected = intersectionChainFirst.getIntersection();
		
		final float probabilityDensityFunction = evaluateProbabilityDensityFunctionSP(intersectionSelected) / found;
		
		final Color3F result = evaluateSP(intersectionSelected);
		
		return new SeparableBSSRDFResult(result, intersectionSelected, probabilityDensityFunction);
	}
	
//	TODO: Add Javadocs!
	@Override
	public float evaluateProbabilityDensityFunctionSP(final Intersection intersection) {
		final OrthonormalBasis33F orthonormalBasis = getIntersection().getOrthonormalBasisS();
		
		final Vector3F u = orthonormalBasis.u;
		final Vector3F v = orthonormalBasis.v;
		final Vector3F w = orthonormalBasis.w;
		
		final Vector3F d = Vector3F.direction(intersection.getSurfaceIntersectionPoint(), getIntersection().getSurfaceIntersectionPoint());
		final Vector3F dLocal = new Vector3F(Vector3F.dotProduct(u, d), Vector3F.dotProduct(v, d), Vector3F.dotProduct(w, d));
		
		final Vector3F n = intersection.getSurfaceNormalS();
		final Vector3F nLocal = new Vector3F(Vector3F.dotProduct(u, n), Vector3F.dotProduct(v, n), Vector3F.dotProduct(w, n));
		
		final float[] rProj = {
			Floats.sqrt(dLocal.y * dLocal.y + dLocal.z * dLocal.z),
			Floats.sqrt(dLocal.z * dLocal.z + dLocal.x * dLocal.x),
			Floats.sqrt(dLocal.x * dLocal.x + dLocal.y * dLocal.y)
		};
		
		final float probability = 1.0F / 3.0F;
		
		final float[] axisProbability = {0.25F, 0.25F, 0.5F};
		
		float probabilityDensityFunctionValue = 0.0F;
		
		for(int i = 0; i < 3; i++) {
			probabilityDensityFunctionValue += evaluateProbabilityDensityFunctionSR(0, rProj[i]) * Floats.abs(nLocal.getComponentAt(i)) * probability * axisProbability[i];
			probabilityDensityFunctionValue += evaluateProbabilityDensityFunctionSR(1, rProj[i]) * Floats.abs(nLocal.getComponentAt(i)) * probability * axisProbability[i];
			probabilityDensityFunctionValue += evaluateProbabilityDensityFunctionSR(2, rProj[i]) * Floats.abs(nLocal.getComponentAt(i)) * probability * axisProbability[i];
		}
		
		return probabilityDensityFunctionValue;
	}
	
//	TODO: Add Javadocs!
	@Override
	public float evaluateProbabilityDensityFunctionSR(final int index, final float distance) {
		final float rOptical = distance * doGetComponentAt(this.sigmaT, index);
		
		final int[] rhoOffset = new int[1];
		final int[] radiusOffset = new int[1];
		
		final float[] rhoWeights = new float[4];
		final float[] radiusWeights = new float[4];
		
		if(!Utilities.catmullRomWeights(this.bSSRDFTable.getRhoSamples().length, this.bSSRDFTable.getRhoSamples(), doGetComponentAt(this.rho, index), rhoOffset, rhoWeights) || !Utilities.catmullRomWeights(this.bSSRDFTable.getRadiusSamples().length, this.bSSRDFTable.getRadiusSamples(), rOptical, radiusOffset, radiusWeights)) {
			return 0.0F;
		}
		
		float sr = 0.0F;
		float rhoEff = 0.0F;
		
		for(int i = 0; i < 4; i++) {
			if(rhoWeights[i] == 0.0F) {
				continue;
			}
			
			for(int j = 0; j < 4; j++) {
				if(radiusWeights[j] == 0.0F) {
					continue;
				}
				
				sr += this.bSSRDFTable.evaluateProfile(rhoOffset[0] + i, radiusOffset[0] + j) * rhoWeights[i] * radiusWeights[j];
			}
		}
		
		if(rOptical != 0.0F) {
			sr /= Floats.PI_MULTIPLIED_BY_2 * rOptical;
		}
		
		return Floats.max(0.0F, sr * doGetComponentAt(this.sigmaT, index) * doGetComponentAt(this.sigmaT, index) / rhoEff);
	}
	
//	TODO: Add Javadocs!
	@Override
	public float sampleSR(final int index, final float u) {
		if(doGetComponentAt(this.sigmaT, index) == 0.0F) {
			return -1.0F;
		}
		
		return Utilities.sampleCatmullRom2D(this.bSSRDFTable.getRhoSamples().length, this.bSSRDFTable.getRadiusSamples().length, this.bSSRDFTable.getRhoSamples(), this.bSSRDFTable.getRadiusSamples(), this.bSSRDFTable.getProfile(), this.bSSRDFTable.getProfileCDF(), doGetComponentAt(this.rho, index), u, new float[1], new float[1]) / doGetComponentAt(this.sigmaT, index);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static float doGetComponentAt(final Color3F color, final int index) {
		switch(index) {
			case 0:
				return color.r;
			case 1:
				return color.g;
			case 2:
				return color.b;
			default:
				throw new IllegalArgumentException();
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static final class IntersectionChain {
		private Intersection intersection;
		private IntersectionChain intersectionChain;
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		public IntersectionChain() {
			
		}
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		public Intersection getIntersection() {
			return this.intersection;
		}
		
		public IntersectionChain getIntersectionChain() {
			return this.intersectionChain;
		}
		
		public void setIntersection(final Intersection intersection) {
			this.intersection = intersection;
		}
		
		public void setIntersectionChain(final IntersectionChain intersectionChain) {
			this.intersectionChain = intersectionChain;
		}
	}
}