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

import org.macroing.java.lang.Doubles;

public final class EDouble {
	private final double lowerBound;
	private final double upperBound;
	private final double value;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public EDouble() {
		this(0.0D);
	}
	
	public EDouble(final double value) {
		this(value, 0.0D);
	}
	
	public EDouble(final double value, final double error) {
		if(error == 0.0D) {
			this.value = value;
			this.lowerBound = value;
			this.upperBound = value;
		} else {
			this.value = value;
			this.lowerBound = Doubles.nextDown(value - error);
			this.upperBound = Doubles.nextUp(value + error);
		}
	}
	
	public EDouble(final double value, final double lowerBound, final double upperBound) {
		this.value = value;
		this.lowerBound = lowerBound;
		this.upperBound = upperBound;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public double getAbsoluteError() {
		return Doubles.nextUp(Doubles.max(Doubles.abs(this.upperBound - this.value), Doubles.abs(this.value - this.lowerBound)));
	}
	
	public double getLowerBound() {
		return this.lowerBound;
	}
	
	public double getUpperBound() {
		return this.upperBound;
	}
	
	public double getValue() {
		return this.value;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static EDouble abs(final EDouble a) {
		if(a.lowerBound >= 0.0D) {
			return a;
		} else if(a.upperBound <= 0.0D) {
			final double value = -a.value;
			final double lowerBound = -a.upperBound;
			final double upperBound = -a.lowerBound;
			
			return new EDouble(value, lowerBound, upperBound);
		} else {
			final double value = Doubles.abs(a.value);
			final double lowerBound = 0.0D;
			final double upperBound = Doubles.max(-a.lowerBound, a.upperBound);
			
			return new EDouble(value, lowerBound, upperBound);
		}
	}
	
	public static EDouble add(final EDouble a, final EDouble b) {
		final double value = a.value + b.value;
		final double lowerBound = Doubles.nextDown(a.lowerBound + b.lowerBound);
		final double upperBound = Doubles.nextUp(a.upperBound + b.upperBound);
		
		return new EDouble(value, lowerBound, upperBound);
	}
	
	public static EDouble divide(final EDouble a, final EDouble b) {
		final double value = a.value / b.value;
		
		if(b.lowerBound < 0.0D && b.upperBound > 0.0D) {
			final double lowerBound = Double.NEGATIVE_INFINITY;
			final double upperBound = Double.POSITIVE_INFINITY;
			
			return new EDouble(value, lowerBound, upperBound);
		}
		
		final double[] div = {
			a.lowerBound / b.lowerBound,
			a.upperBound / b.lowerBound,
			a.lowerBound / b.upperBound,
			a.upperBound / b.upperBound
		};
		
		final double lowerBound = Doubles.nextDown(Doubles.min(Doubles.min(div[0], div[1]), Doubles.min(div[2], div[3])));
		final double upperBound = Doubles.nextUp(Doubles.max(Doubles.max(div[0], div[1]), Doubles.max(div[2], div[3])));
		
		return new EDouble(value, lowerBound, upperBound);
	}
	
	public static EDouble multiply(final EDouble a, final EDouble b) {
		final double value = a.value * b.value;
		
		final double[] prod = {
			a.lowerBound * b.lowerBound,
			a.upperBound * b.lowerBound,
			a.lowerBound * b.upperBound,
			a.upperBound * b.upperBound
		};
		
		final double lowerBound = Doubles.nextDown(Doubles.min(Doubles.min(prod[0], prod[1]), Doubles.min(prod[2], prod[3])));
		final double upperBound = Doubles.nextUp(Doubles.max(Doubles.max(prod[0], prod[1]), Doubles.max(prod[2], prod[3])));
		
		return new EDouble(value, lowerBound, upperBound);
	}
	
	public static EDouble negate(final EDouble a) {
		final double value = -a.value;
		final double lowerBound = -a.upperBound;
		final double upperBound = -a.lowerBound;
		
		return new EDouble(value, lowerBound, upperBound);
	}
	
	public static EDouble sqrt(final EDouble a) {
		final double value = Doubles.sqrt(a.value);
		final double lowerBound = Doubles.nextDown(Doubles.sqrt(a.lowerBound));
		final double upperBound = Doubles.nextUp(Doubles.sqrt(a.upperBound));
		
		return new EDouble(value, lowerBound, upperBound);
	}
	
	public static EDouble subtract(final EDouble a, final EDouble b) {
		final double value = a.value - b.value;
		final double lowerBound = Doubles.nextDown(a.lowerBound - b.upperBound);
		final double upperBound = Doubles.nextUp(a.upperBound - b.lowerBound);
		
		return new EDouble(value, lowerBound, upperBound);
	}
	
	public static boolean quadratic(final EDouble a, final EDouble b, final EDouble c, final EDouble[] t0, final EDouble[] t1) {
		final double discrim = b.value * b.value - 4.0D * a.value * c.value;
		
		if(discrim < 0.0D) {
			return false;
		}
		
		final double rootDiscrim = Math.sqrt(discrim);
		
		final EDouble doubleRootDiscrim = new EDouble(rootDiscrim, Doubles.MACHINE_EPSILON * rootDiscrim);
		
		EDouble q;
		
		if(b.value < 0.0D) {
			q = multiply(new EDouble(-0.5D), subtract(b, doubleRootDiscrim));
		} else {
			q = multiply(new EDouble(-0.5D), add(b, doubleRootDiscrim));
		}
		
		t0[0] = divide(q, a);
		t1[0] = divide(c, q);
		
		if(t0[0].value > t1[0].value) {
			final EDouble t2 = t0[0];
			
			t0[0] = t1[0];
			t1[0] = t2;
		}
		
		return true;
	}
}