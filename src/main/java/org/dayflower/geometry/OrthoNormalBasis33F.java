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
package org.dayflower.geometry;

import java.lang.reflect.Field;
import java.util.Objects;

//TODO: Add Javadocs!
public final class OrthoNormalBasis33F {
	private final Vector3F u;
	private final Vector3F v;
	private final Vector3F w;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public OrthoNormalBasis33F() {
		this(Vector3F.w(), Vector3F.v(), Vector3F.u());
	}
	
//	TODO: Add Javadocs!
	public OrthoNormalBasis33F(final Vector3F w) {
		this.w = Vector3F.normalize(w);
		this.v = Vector3F.normalize(Vector3F.computeV(this.w));
		this.u = Vector3F.crossProduct(this.v, this.w);
	}
	
//	TODO: Add Javadocs!
	public OrthoNormalBasis33F(final Vector3F w, final Vector3F v) {
		this.w = Vector3F.normalize(w);
		this.u = Vector3F.normalize(Vector3F.crossProduct(v, this.w));
		this.v = Vector3F.crossProduct(this.w, this.u);
	}
	
//	TODO: Add Javadocs!
	public OrthoNormalBasis33F(final Vector3F w, final Vector3F v, final Vector3F u) {
		this.w = Objects.requireNonNull(w, "w == null");
		this.v = Objects.requireNonNull(v, "v == null");
		this.u = Objects.requireNonNull(u, "u == null");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	@Override
	public String toString() {
		return String.format("new OrthoNormalBasis33F(%s, %s, %s)", this.w, this.v, this.u);
	}
	
//	TODO: Add Javadocs!
	public Vector3F getU() {
		return this.u;
	}
	
//	TODO: Add Javadocs!
	public Vector3F getV() {
		return this.v;
	}
	
//	TODO: Add Javadocs!
	public Vector3F getW() {
		return this.w;
	}
	
//	TODO: Add Javadocs!
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof OrthoNormalBasis33F)) {
			return false;
		} else if(!Objects.equals(this.u, OrthoNormalBasis33F.class.cast(object).u)) {
			return false;
		} else if(!Objects.equals(this.v, OrthoNormalBasis33F.class.cast(object).v)) {
			return false;
		} else if(!Objects.equals(this.w, OrthoNormalBasis33F.class.cast(object).w)) {
			return false;
		} else {
			return true;
		}
	}
	
//	TODO: Add Javadocs!
	@Override
	public int hashCode() {
		return Objects.hash(this.u, this.v, this.w);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public static OrthoNormalBasis33F flipU(final OrthoNormalBasis33F o) {
		return new OrthoNormalBasis33F(o.w, o.v, Vector3F.negate(o.u));
	}
	
//	TODO: Add Javadocs!
	public static OrthoNormalBasis33F flipV(final OrthoNormalBasis33F o) {
		return new OrthoNormalBasis33F(o.w, Vector3F.negate(o.v), o.u);
	}
	
//	TODO: Add Javadocs!
	public static OrthoNormalBasis33F flipW(final OrthoNormalBasis33F o) {
		return new OrthoNormalBasis33F(Vector3F.negate(o.w), o.v, o.u);
	}
	
//	TODO: Add Javadocs!
	public static OrthoNormalBasis33F transform(final Matrix44F mLHS, final OrthoNormalBasis33F oRHS) {
		final Vector3F u = Vector3F.transform(mLHS, oRHS.u);
		final Vector3F v = Vector3F.transform(mLHS, oRHS.v);
		final Vector3F w = Vector3F.transform(mLHS, oRHS.w);
		
		return new OrthoNormalBasis33F(w, v, u);
	}
	
//	TODO: Add Javadocs!
	public static OrthoNormalBasis33F transformTranspose(final Matrix44F mLHS, final OrthoNormalBasis33F oRHS) {
		final Vector3F u = Vector3F.transformTranspose(mLHS, oRHS.u);
		final Vector3F v = Vector3F.transformTranspose(mLHS, oRHS.v);
		final Vector3F w = Vector3F.transformTranspose(mLHS, oRHS.w);
		
		return new OrthoNormalBasis33F(w, v, u);
	}
}