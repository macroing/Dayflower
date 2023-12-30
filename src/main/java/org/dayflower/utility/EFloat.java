/**
 * Copyright 2014 - 2024 J&#246;rgen Lundgren
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
package org.dayflower.utility;

import org.macroing.java.lang.Floats;

public final class EFloat {
	private final float lowerBound;
	private final float upperBound;
	private final float value;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public EFloat() {
		this(0.0F);
	}
	
	public EFloat(final float value) {
		this(value, 0.0F);
	}
	
	public EFloat(final float value, final float error) {
		if(error == 0.0F) {
			this.value = value;
			this.lowerBound = value;
			this.upperBound = value;
		} else {
			this.value = value;
			this.lowerBound = Floats.nextDown(value - error);
			this.upperBound = Floats.nextUp(value + error);
		}
	}
	
	public EFloat(final float value, final float lowerBound, final float upperBound) {
		this.value = value;
		this.lowerBound = lowerBound;
		this.upperBound = upperBound;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public float getAbsoluteError() {
		return Floats.nextUp(Floats.max(Floats.abs(this.upperBound - this.value), Floats.abs(this.value - this.lowerBound)));
	}
	
	public float getLowerBound() {
		return this.lowerBound;
	}
	
	public float getUpperBound() {
		return this.upperBound;
	}
	
	public float getValue() {
		return this.value;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static EFloat abs(final EFloat a) {
		if(a.lowerBound >= 0.0F) {
			return a;
		} else if(a.upperBound <= 0.0F) {
			final float value = -a.value;
			final float lowerBound = -a.upperBound;
			final float upperBound = -a.lowerBound;
			
			return new EFloat(value, lowerBound, upperBound);
		} else {
			final float value = Floats.abs(a.value);
			final float lowerBound = 0.0F;
			final float upperBound = Floats.max(-a.lowerBound, a.upperBound);
			
			return new EFloat(value, lowerBound, upperBound);
		}
	}
	
	public static EFloat add(final EFloat a, final EFloat b) {
		final float value = a.value + b.value;
		final float lowerBound = Floats.nextDown(a.lowerBound + b.lowerBound);
		final float upperBound = Floats.nextUp(a.upperBound + b.upperBound);
		
		return new EFloat(value, lowerBound, upperBound);
	}
	
	public static EFloat divide(final EFloat a, final EFloat b) {
		final float value = a.value / b.value;
		
		if(b.lowerBound < 0.0F && b.upperBound > 0.0F) {
			final float lowerBound = Float.NEGATIVE_INFINITY;
			final float upperBound = Float.POSITIVE_INFINITY;
			
			return new EFloat(value, lowerBound, upperBound);
		}
		
		final float[] div = {
			a.lowerBound / b.lowerBound,
			a.upperBound / b.lowerBound,
			a.lowerBound / b.upperBound,
			a.upperBound / b.upperBound
		};
		
		final float lowerBound = Floats.nextDown(Floats.min(Floats.min(div[0], div[1]), Floats.min(div[2], div[3])));
		final float upperBound = Floats.nextUp(Floats.max(Floats.max(div[0], div[1]), Floats.max(div[2], div[3])));
		
		return new EFloat(value, lowerBound, upperBound);
	}
	
	public static EFloat multiply(final EFloat a, final EFloat b) {
		final float value = a.value * b.value;
		
		final float[] prod = {
			a.lowerBound * b.lowerBound,
			a.upperBound * b.lowerBound,
			a.lowerBound * b.upperBound,
			a.upperBound * b.upperBound
		};
		
		final float lowerBound = Floats.nextDown(Floats.min(Floats.min(prod[0], prod[1]), Floats.min(prod[2], prod[3])));
		final float upperBound = Floats.nextUp(Floats.max(Floats.max(prod[0], prod[1]), Floats.max(prod[2], prod[3])));
		
		return new EFloat(value, lowerBound, upperBound);
	}
	
	public static EFloat negate(final EFloat a) {
		final float value = -a.value;
		final float lowerBound = -a.upperBound;
		final float upperBound = -a.lowerBound;
		
		return new EFloat(value, lowerBound, upperBound);
	}
	
	public static EFloat sqrt(final EFloat a) {
		final float value = Floats.sqrt(a.value);
		final float lowerBound = Floats.nextDown(Floats.sqrt(a.lowerBound));
		final float upperBound = Floats.nextUp(Floats.sqrt(a.upperBound));
		
		return new EFloat(value, lowerBound, upperBound);
	}
	
	public static EFloat subtract(final EFloat a, final EFloat b) {
		final float value = a.value - b.value;
		final float lowerBound = Floats.nextDown(a.lowerBound - b.upperBound);
		final float upperBound = Floats.nextUp(a.upperBound - b.lowerBound);
		
		return new EFloat(value, lowerBound, upperBound);
	}
	
	public static boolean quadratic(final EFloat a, final EFloat b, final EFloat c, final EFloat[] t0, final EFloat[] t1) {
		final double discrim = (double)(b.value) * (double)(b.value) - 4.0D * a.value * c.value;
		
		if(discrim < 0.0D) {
			return false;
		}
		
		final double rootDiscrim = Math.sqrt(discrim);
		
		final EFloat floatRootDiscrim = new EFloat((float)(rootDiscrim), Floats.MACHINE_EPSILON * (float)(rootDiscrim));
		
		EFloat q;
		
		if(b.value < 0.0F) {
			q = multiply(new EFloat(-0.5F), subtract(b, floatRootDiscrim));
		} else {
			q = multiply(new EFloat(-0.5F), add(b, floatRootDiscrim));
		}
		
		t0[0] = divide(q, a);
		t1[0] = divide(c, q);
		
		if(t0[0].value > t1[0].value) {
			final EFloat t2 = t0[0];
			
			t0[0] = t1[0];
			t1[0] = t2;
		}
		
		return true;
	}
}