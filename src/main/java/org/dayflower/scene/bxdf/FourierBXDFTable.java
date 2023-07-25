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
package org.dayflower.scene.bxdf;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.Field;//TODO: Add Javadoc!

import org.dayflower.interpolation.Interpolation;

//TODO: Add Javadoc!
public final class FourierBXDFTable {
	private float eta;
	private float[] a;
	private float[] a0;
	private float[] cDF;
	private float[] mu;
	private float[] recip;
	private int mMax;
	private int nChannels;
	private int nMu;
	private int[] aOffset;
	private int[] m;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadoc!
	public FourierBXDFTable() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadoc!
	public boolean getWeightsAndOffset(final float cosTheta, final int[] offset, final float[] weights) {
		return Interpolation.catmullRomWeights(this.nMu, this.mu, cosTheta, offset, weights);
	}
	
//	TODO: Add Javadoc!
	public boolean read(final String filename) {
		this.mu = null;
		this.cDF = null;
		this.a = null;
		this.aOffset = null;
		this.m = null;
		this.nChannels = 0;
		
		try(final BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(new File(filename)))) {
			if(!doReadHeader(bufferedInputStream)) {
				return false;
			}
			
			if(!doReadVersion(bufferedInputStream)) {
				return false;
			}
			
			if(!doReadFlags(bufferedInputStream)) {
				return false;
			}
			
			this.nMu = doReadInt32(bufferedInputStream);
			
			final int nCoeffs = doReadInt32(bufferedInputStream);
			
			this.mMax = doReadInt32(bufferedInputStream);
			this.nChannels = doReadInt32(bufferedInputStream);
			
			if(this.nChannels != 1 && this.nChannels != 3) {
				return false;
			}
			
			final int nBases = doReadInt32(bufferedInputStream);
			
			if(nBases != 1) {
				return false;
			}
			
//			Unused:
			doReadInt32(bufferedInputStream);
			doReadInt32(bufferedInputStream);
			doReadInt32(bufferedInputStream);
			
			this.eta = doReadFloat(bufferedInputStream);
			
//			Unused:
			doReadInt32(bufferedInputStream);
			doReadInt32(bufferedInputStream);
			doReadInt32(bufferedInputStream);
			doReadInt32(bufferedInputStream);
			
			this.mu = new float[this.nMu];
			this.cDF = new float[this.nMu * this.nMu];
			this.a0 = new float[this.nMu * this.nMu];
			
			final int[] offsetAndLength = new int[this.nMu * this.nMu * 2];
			
			this.aOffset = new int[this.nMu * this.nMu];
			this.m = new int[this.nMu * this.nMu];
			this.a = new float[nCoeffs];
			
			for(int i = 0; i < this.nMu; i++) {
				this.mu[i] = doReadFloat(bufferedInputStream);
			}
			
			for(int i = 0; i < this.nMu * this.nMu; i++) {
				this.cDF[i] = doReadFloat(bufferedInputStream);
			}
			
			for(int i = 0; i < this.nMu * this.nMu * 2; i++) {
				offsetAndLength[i] = doReadInt32(bufferedInputStream);
			}
			
			for(int i = 0; i < nCoeffs; i++) {
				this.a[i] = doReadFloat(bufferedInputStream);
			}
			
			for(int i = 0; i < this.nMu * this.nMu; i++) {
				final int offset = offsetAndLength[2 * i];
				final int length = offsetAndLength[2 * i + 1];
				
				this.aOffset[i] = offset;
				this.m[i] = length;
				this.a0[i] = length > 0 ? this.a[offset] : 0.0F;
			}
			
			this.recip = new float[this.mMax];
			
			for(int i = 0; i < this.mMax; i++) {
				this.recip[i] = 1.0F / i;
			}
			
			return true;
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
//	TODO: Add Javadoc!
	public float getEta() {
		return this.eta;
	}
	
//	TODO: Add Javadoc!
	public float[] getA() {
		return this.a;
	}
	
//	TODO: Add Javadoc!
	public float[] getA0() {
		return this.a0;
	}
	
//	TODO: Add Javadoc!
	public float[] getAk(final int offsetI, final int offsetO, final int[] mptr, final int[] offset) {
		mptr[0] = this.m[offsetO * this.nMu + offsetI];
		
		offset[0] = this.aOffset[offsetO * this.nMu + offsetI];
		
		return this.a;
	}
	
//	TODO: Add Javadoc!
	public float[] getCDF() {
		return this.cDF;
	}
	
//	TODO: Add Javadoc!
	public float[] getMu() {
		return this.mu;
	}
	
//	TODO: Add Javadoc!
	public float[] getRecip() {
		return this.recip;
	}
	
//	TODO: Add Javadoc!
	public int getMMax() {
		return this.mMax;
	}
	
//	TODO: Add Javadoc!
	public int getNChannels() {
		return this.nChannels;
	}
	
//	TODO: Add Javadoc!
	public int getNMu() {
		return this.nMu;
	}
	
//	TODO: Add Javadoc!
	public int[] getAOffset() {
		return this.aOffset;
	}
	
//	TODO: Add Javadoc!
	public int[] getM() {
		return this.m;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static boolean doReadFlags(final BufferedInputStream bufferedInputStream) throws IOException {
		final int flags = doReadInt32(bufferedInputStream);
		
		return flags == 1;
	}
	
	private static boolean doReadHeader(final BufferedInputStream bufferedInputStream) throws IOException {
		final StringBuilder stringBuilder = new StringBuilder();
		
		for(int i = 0; i < 7; i++) {
			stringBuilder.append((char)(bufferedInputStream.read()));
		}
		
		return stringBuilder.toString().equals("SCATFUN");
	}
	
	private static boolean doReadVersion(final BufferedInputStream bufferedInputStream) throws IOException {
		final int version = bufferedInputStream.read();
		
		return version == 1;
	}
	
	private static float doReadFloat(final BufferedInputStream bufferedInputStream) throws IOException {
		return Float.intBitsToFloat(doReadInt32(bufferedInputStream));
	}
	
	private static int doReadInt32(final BufferedInputStream bufferedInputStream) throws IOException {
		final int a = bufferedInputStream.read();
		final int b = bufferedInputStream.read();
		final int c = bufferedInputStream.read();
		final int d = bufferedInputStream.read();
		
		return ((a & 0xFF) << 0) | ((b & 0xFF) << 8) | ((c & 0xFF) << 16) | ((d & 0xFF) << 24);
	}
}