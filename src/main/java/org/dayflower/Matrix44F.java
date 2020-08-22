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
package org.dayflower;

import static org.dayflower.Floats.abs;
import static org.dayflower.Floats.cos;
import static org.dayflower.Floats.equal;
import static org.dayflower.Floats.sin;

import java.util.Objects;

public final class Matrix44F {
	private final float element11;
	private final float element12;
	private final float element13;
	private final float element14;
	private final float element21;
	private final float element22;
	private final float element23;
	private final float element24;
	private final float element31;
	private final float element32;
	private final float element33;
	private final float element34;
	private final float element41;
	private final float element42;
	private final float element43;
	private final float element44;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public Matrix44F() {
		this(1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F);
	}
	
	public Matrix44F(final float element11, final float element12, final float element13, final float element14, final float element21, final float element22, final float element23, final float element24, final float element31, final float element32, final float element33, final float element34, final float element41, final float element42, final float element43, final float element44) {
		this.element11 = element11;
		this.element12 = element12;
		this.element13 = element13;
		this.element14 = element14;
		this.element21 = element21;
		this.element22 = element22;
		this.element23 = element23;
		this.element24 = element24;
		this.element31 = element31;
		this.element32 = element32;
		this.element33 = element33;
		this.element34 = element34;
		this.element41 = element41;
		this.element42 = element42;
		this.element43 = element43;
		this.element44 = element44;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public String toString() {
		final String row1 = String.format("%+.10f, %+.10f, %+.10f, %+.10f", Float.valueOf(this.element11), Float.valueOf(this.element12), Float.valueOf(this.element13), Float.valueOf(this.element14));
		final String row2 = String.format("%+.10f, %+.10f, %+.10f, %+.10f", Float.valueOf(this.element21), Float.valueOf(this.element22), Float.valueOf(this.element23), Float.valueOf(this.element24));
		final String row3 = String.format("%+.10f, %+.10f, %+.10f, %+.10f", Float.valueOf(this.element31), Float.valueOf(this.element32), Float.valueOf(this.element33), Float.valueOf(this.element34));
		final String row4 = String.format("%+.10f, %+.10f, %+.10f, %+.10f", Float.valueOf(this.element41), Float.valueOf(this.element42), Float.valueOf(this.element43), Float.valueOf(this.element44));
		
		return String.format("new Matrix44F(%s, %s, %s, %s)", row1, row2, row3, row4);
	}
	
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof Matrix44F)) {
			return false;
		} else if(!equal(this.element11, Matrix44F.class.cast(object).element11)) {
			return false;
		} else if(!equal(this.element12, Matrix44F.class.cast(object).element12)) {
			return false;
		} else if(!equal(this.element13, Matrix44F.class.cast(object).element13)) {
			return false;
		} else if(!equal(this.element14, Matrix44F.class.cast(object).element14)) {
			return false;
		} else if(!equal(this.element21, Matrix44F.class.cast(object).element21)) {
			return false;
		} else if(!equal(this.element22, Matrix44F.class.cast(object).element22)) {
			return false;
		} else if(!equal(this.element23, Matrix44F.class.cast(object).element23)) {
			return false;
		} else if(!equal(this.element24, Matrix44F.class.cast(object).element24)) {
			return false;
		} else if(!equal(this.element31, Matrix44F.class.cast(object).element31)) {
			return false;
		} else if(!equal(this.element32, Matrix44F.class.cast(object).element32)) {
			return false;
		} else if(!equal(this.element33, Matrix44F.class.cast(object).element33)) {
			return false;
		} else if(!equal(this.element34, Matrix44F.class.cast(object).element34)) {
			return false;
		} else if(!equal(this.element41, Matrix44F.class.cast(object).element41)) {
			return false;
		} else if(!equal(this.element42, Matrix44F.class.cast(object).element42)) {
			return false;
		} else if(!equal(this.element43, Matrix44F.class.cast(object).element43)) {
			return false;
		} else if(!equal(this.element44, Matrix44F.class.cast(object).element44)) {
			return false;
		} else {
			return true;
		}
	}
	
	public boolean isInvertible() {
		return abs(determinant()) >= 1.0e-12F;
	}
	
	public float determinant() {
		final float a = this.element11 * this.element22 - this.element12 * this.element21;
		final float b = this.element11 * this.element23 - this.element13 * this.element21;
		final float c = this.element11 * this.element24 - this.element14 * this.element21;
		final float d = this.element12 * this.element23 - this.element13 * this.element22;
		final float e = this.element12 * this.element24 - this.element14 * this.element22;
		final float f = this.element13 * this.element24 - this.element14 * this.element23;
		final float g = this.element31 * this.element42 - this.element32 * this.element41;
		final float h = this.element31 * this.element43 - this.element33 * this.element41;
		final float i = this.element31 * this.element44 - this.element34 * this.element41;
		final float j = this.element32 * this.element43 - this.element33 * this.element42;
		final float k = this.element32 * this.element44 - this.element34 * this.element42;
		final float l = this.element33 * this.element44 - this.element34 * this.element43;
		
		return a * l - b * k + c * j + d * i - e * h + f * g;
	}
	
	public float getElement11() {
		return this.element11;
	}
	
	public float getElement12() {
		return this.element12;
	}
	
	public float getElement13() {
		return this.element13;
	}
	
	public float getElement14() {
		return this.element14;
	}
	
	public float getElement21() {
		return this.element21;
	}
	
	public float getElement22() {
		return this.element22;
	}
	
	public float getElement23() {
		return this.element23;
	}
	
	public float getElement24() {
		return this.element24;
	}
	
	public float getElement31() {
		return this.element31;
	}
	
	public float getElement32() {
		return this.element32;
	}
	
	public float getElement33() {
		return this.element33;
	}
	
	public float getElement34() {
		return this.element34;
	}
	
	public float getElement41() {
		return this.element41;
	}
	
	public float getElement42() {
		return this.element42;
	}
	
	public float getElement43() {
		return this.element43;
	}
	
	public float getElement44() {
		return this.element44;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(new Object[] {
			Float.valueOf(this.element11), Float.valueOf(this.element12), Float.valueOf(this.element13), Float.valueOf(this.element14),
			Float.valueOf(this.element21), Float.valueOf(this.element22), Float.valueOf(this.element23), Float.valueOf(this.element24),
			Float.valueOf(this.element31), Float.valueOf(this.element32), Float.valueOf(this.element33), Float.valueOf(this.element34),
			Float.valueOf(this.element41), Float.valueOf(this.element42), Float.valueOf(this.element43), Float.valueOf(this.element44)
		});
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static Matrix44F identity() {
		return new Matrix44F();
	}
	
	public static Matrix44F inverse(final Matrix44F m) {
		final float a = m.element11 * m.element22 - m.element12 * m.element21;
		final float b = m.element11 * m.element23 - m.element13 * m.element21;
		final float c = m.element11 * m.element24 - m.element14 * m.element21;
		final float d = m.element12 * m.element23 - m.element13 * m.element22;
		final float e = m.element12 * m.element24 - m.element14 * m.element22;
		final float f = m.element13 * m.element24 - m.element14 * m.element23;
		final float g = m.element31 * m.element42 - m.element32 * m.element41;
		final float h = m.element31 * m.element43 - m.element33 * m.element41;
		final float i = m.element31 * m.element44 - m.element34 * m.element41;
		final float j = m.element32 * m.element43 - m.element33 * m.element42;
		final float k = m.element32 * m.element44 - m.element34 * m.element42;
		final float l = m.element33 * m.element44 - m.element34 * m.element43;
		
		final float determinant = a * l - b * k + c * j + d * i - e * h + f * g;
		final float determinantReciprocal = 1.0F / determinant;
		
		if(abs(determinant) < 1.0e-12F) {
			throw new IllegalArgumentException("The Matrix44F 'm' cannot be inverted!");
		}
		
		final float element11 = (+m.element22 * l - m.element23 * k + m.element24 * j) * determinantReciprocal;
		final float element12 = (-m.element12 * l + m.element13 * k - m.element14 * j) * determinantReciprocal;
		final float element13 = (+m.element42 * f - m.element43 * e + m.element44 * d) * determinantReciprocal;
		final float element14 = (-m.element32 * f + m.element33 * e - m.element34 * d) * determinantReciprocal;
		final float element21 = (-m.element21 * l + m.element23 * i - m.element24 * h) * determinantReciprocal;
		final float element22 = (+m.element11 * l - m.element13 * i + m.element14 * h) * determinantReciprocal;
		final float element23 = (-m.element41 * f + m.element43 * c - m.element44 * b) * determinantReciprocal;
		final float element24 = (+m.element31 * f - m.element33 * c + m.element34 * b) * determinantReciprocal;
		final float element31 = (+m.element21 * k - m.element22 * i + m.element24 * g) * determinantReciprocal;
		final float element32 = (-m.element11 * k + m.element12 * i - m.element14 * g) * determinantReciprocal;
		final float element33 = (+m.element41 * e - m.element42 * c + m.element44 * a) * determinantReciprocal;
		final float element34 = (-m.element31 * e + m.element32 * c - m.element34 * a) * determinantReciprocal;
		final float element41 = (-m.element21 * j + m.element22 * h - m.element23 * g) * determinantReciprocal;
		final float element42 = (+m.element11 * j - m.element12 * h + m.element13 * g) * determinantReciprocal;
		final float element43 = (-m.element41 * d + m.element42 * b - m.element43 * a) * determinantReciprocal;
		final float element44 = (+m.element31 * d - m.element32 * b + m.element33 * a) * determinantReciprocal;
		
		return new Matrix44F(element11, element12, element13, element14, element21, element22, element23, element24, element31, element32, element33, element34, element41, element42, element43, element44);
	}
	
	public static Matrix44F multiply(final Matrix44F mLHS, final Matrix44F mRHS) {
		final float element11 = mLHS.element11 * mRHS.element11 + mLHS.element12 * mRHS.element21 + mLHS.element13 * mRHS.element31 + mLHS.element14 * mRHS.element41;
		final float element12 = mLHS.element11 * mRHS.element12 + mLHS.element12 * mRHS.element22 + mLHS.element13 * mRHS.element32 + mLHS.element14 * mRHS.element42;
		final float element13 = mLHS.element11 * mRHS.element13 + mLHS.element12 * mRHS.element23 + mLHS.element13 * mRHS.element33 + mLHS.element14 * mRHS.element43;
		final float element14 = mLHS.element11 * mRHS.element14 + mLHS.element12 * mRHS.element24 + mLHS.element13 * mRHS.element34 + mLHS.element14 * mRHS.element44;
		final float element21 = mLHS.element21 * mRHS.element11 + mLHS.element22 * mRHS.element21 + mLHS.element23 * mRHS.element31 + mLHS.element24 * mRHS.element41;
		final float element22 = mLHS.element21 * mRHS.element12 + mLHS.element22 * mRHS.element22 + mLHS.element23 * mRHS.element32 + mLHS.element24 * mRHS.element42;
		final float element23 = mLHS.element21 * mRHS.element13 + mLHS.element22 * mRHS.element23 + mLHS.element23 * mRHS.element33 + mLHS.element24 * mRHS.element43;
		final float element24 = mLHS.element21 * mRHS.element14 + mLHS.element22 * mRHS.element24 + mLHS.element23 * mRHS.element34 + mLHS.element24 * mRHS.element44;
		final float element31 = mLHS.element31 * mRHS.element11 + mLHS.element32 * mRHS.element21 + mLHS.element33 * mRHS.element31 + mLHS.element34 * mRHS.element41;
		final float element32 = mLHS.element31 * mRHS.element12 + mLHS.element32 * mRHS.element22 + mLHS.element33 * mRHS.element32 + mLHS.element34 * mRHS.element42;
		final float element33 = mLHS.element31 * mRHS.element13 + mLHS.element32 * mRHS.element23 + mLHS.element33 * mRHS.element33 + mLHS.element34 * mRHS.element43;
		final float element34 = mLHS.element31 * mRHS.element14 + mLHS.element32 * mRHS.element24 + mLHS.element33 * mRHS.element34 + mLHS.element34 * mRHS.element44;
		final float element41 = mLHS.element41 * mRHS.element11 + mLHS.element42 * mRHS.element21 + mLHS.element43 * mRHS.element31 + mLHS.element44 * mRHS.element41;
		final float element42 = mLHS.element41 * mRHS.element12 + mLHS.element42 * mRHS.element22 + mLHS.element43 * mRHS.element32 + mLHS.element44 * mRHS.element42;
		final float element43 = mLHS.element41 * mRHS.element13 + mLHS.element42 * mRHS.element23 + mLHS.element43 * mRHS.element33 + mLHS.element44 * mRHS.element43;
		final float element44 = mLHS.element41 * mRHS.element14 + mLHS.element42 * mRHS.element24 + mLHS.element43 * mRHS.element34 + mLHS.element44 * mRHS.element44;
		
		return new Matrix44F(element11, element12, element13, element14, element21, element22, element23, element24, element31, element32, element33, element34, element41, element42, element43, element44);
	}
	
	public static Matrix44F rotate(final AngleF a, final Vector3F v) {
		final Vector3F vNormalized = Vector3F.normalize(v);
		
		final float cos = cos(a.getRadians());
		final float sin = sin(a.getRadians());
		final float oneMinusCos = 1.0F - cos;
		
		final float element11 = oneMinusCos * vNormalized.getX() * vNormalized.getX() + cos;
		final float element12 = oneMinusCos * vNormalized.getX() * vNormalized.getY() - sin * vNormalized.getZ();
		final float element13 = oneMinusCos * vNormalized.getX() * vNormalized.getZ() + sin * vNormalized.getY();
		final float element14 = 0.0F;
		final float element21 = oneMinusCos * vNormalized.getX() * vNormalized.getY() + sin * vNormalized.getZ();
		final float element22 = oneMinusCos * vNormalized.getY() * vNormalized.getY() + cos;
		final float element23 = oneMinusCos * vNormalized.getY() * vNormalized.getZ() - sin * vNormalized.getX();
		final float element24 = 0.0F;
		final float element31 = oneMinusCos * vNormalized.getX() * vNormalized.getZ() - sin * vNormalized.getY();
		final float element32 = oneMinusCos * vNormalized.getY() * vNormalized.getZ() + sin * vNormalized.getX();
		final float element33 = oneMinusCos * vNormalized.getZ() * vNormalized.getZ() + cos;
		final float element34 = 0.0F;
		final float element41 = 0.0F;
		final float element42 = 0.0F;
		final float element43 = 0.0F;
		final float element44 = 1.0F;
		
		return new Matrix44F(element11, element12, element13, element14, element21, element22, element23, element24, element31, element32, element33, element34, element41, element42, element43, element44);
	}
	
	public static Matrix44F rotate(final AngleF a, final float x, final float y, final float z) {
		return rotate(a, new Vector3F(x, y, z));
	}
	
	public static Matrix44F rotateX(final AngleF a) {
		final float cos = cos(a.getRadians());
		final float sin = sin(a.getRadians());
		
		return new Matrix44F(1.0F, 0.0F, 0.0F, 0.0F, 0.0F, cos, -sin, 0.0F, 0.0F, sin, cos, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F);
	}
	
	public static Matrix44F rotateY(final AngleF a) {
		final float cos = cos(a.getRadians());
		final float sin = sin(a.getRadians());
		
		return new Matrix44F(cos, 0.0F, sin, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, -sin, 0.0F, cos, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F);
	}
	
	public static Matrix44F rotateZ(final AngleF a) {
		final float cos = cos(a.getRadians());
		final float sin = sin(a.getRadians());
		
		return new Matrix44F(cos, -sin, 0.0F, 0.0F, sin, cos, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F);
	}
	
	public static Matrix44F scale(final Vector3F v) {
		return scale(v.getX(), v.getY(), v.getZ());
	}
	
	public static Matrix44F scale(final float s) {
		return scale(s, s, s);
	}
	
	public static Matrix44F scale(final float x, final float y, final float z) {
		return new Matrix44F(x, 0.0F, 0.0F, 0.0F, 0.0F, y, 0.0F, 0.0F, 0.0F, 0.0F, z, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F);
	}
	
	public static Matrix44F translate(final Point3F p) {
		return translate(p.getX(), p.getY(), p.getZ());
	}
	
	public static Matrix44F translate(final float x, final float y, final float z) {
		return new Matrix44F(1.0F, 0.0F, 0.0F, x, 0.0F, 1.0F, 0.0F, y, 0.0F, 0.0F, 1.0F, z, 0.0F, 0.0F, 0.0F, 1.0F);
	}
	
	public static Matrix44F transpose(final Matrix44F m) {
		final float element11 = m.element11;
		final float element12 = m.element21;
		final float element13 = m.element31;
		final float element14 = m.element41;
		final float element21 = m.element12;
		final float element22 = m.element22;
		final float element23 = m.element32;
		final float element24 = m.element42;
		final float element31 = m.element13;
		final float element32 = m.element23;
		final float element33 = m.element33;
		final float element34 = m.element43;
		final float element41 = m.element14;
		final float element42 = m.element24;
		final float element43 = m.element34;
		final float element44 = m.element44;
		
		return new Matrix44F(element11, element12, element13, element14, element21, element22, element23, element24, element31, element32, element33, element34, element41, element42, element43, element44);
	}
}