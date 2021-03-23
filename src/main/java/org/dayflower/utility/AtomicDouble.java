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
package org.dayflower.utility;

import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleUnaryOperator;

//TODO: Add Javadocs!
public final class AtomicDouble extends Number {
	private static final long serialVersionUID = 1L;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final AtomicLong bits;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public AtomicDouble() {
		this(0.0D);
	}
	
//	TODO: Add Javadocs!
	public AtomicDouble(final double initialValue) {
		this.bits = new AtomicLong(Double.doubleToLongBits(initialValue));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	@Override
	public String toString() {
		return Double.toString(get());
	}
	
//	TODO: Add Javadocs!
	public boolean compareAndSet(final double expect, final double update) {
		return this.bits.compareAndSet(Double.doubleToLongBits(expect), Double.doubleToLongBits(update));
	}
	
//	TODO: Add Javadocs!
	public boolean weakCompareAndSet(final double expect, final double update) {
		return this.bits.weakCompareAndSet(Double.doubleToLongBits(expect), Double.doubleToLongBits(update));
	}
	
//	TODO: Add Javadocs!
	public double accumulateAndGet(final double x, final DoubleBinaryOperator accumulatorFunction) {
		double previous;
		double next;
		
		do {
			previous = get();
			next = accumulatorFunction.applyAsDouble(previous, x);
		} while(!compareAndSet(previous, next));
		
		return next;
	}
	
//	TODO: Add Javadocs!
	public double addAndGet(final double delta) {
		return Double.longBitsToDouble(this.bits.addAndGet(Double.doubleToLongBits(delta)));
	}
	
//	TODO: Add Javadocs!
	public double decrementAndGet() {
		return Double.longBitsToDouble(this.bits.decrementAndGet());
	}
	
//	TODO: Add Javadocs!
	@Override
	public double doubleValue() {
		return get();
	}
	
//	TODO: Add Javadocs!
	public double get() {
		return Double.longBitsToDouble(this.bits.get());
	}
	
//	TODO: Add Javadocs!
	public double getAndAccumulate(final double x, final DoubleBinaryOperator accumulatorFunction) {
		double previous;
		double next;
		
		do {
			previous = get();
			next = accumulatorFunction.applyAsDouble(previous, x);
		} while(!compareAndSet(previous, next));
		
		return previous;
	}
	
//	TODO: Add Javadocs!
	public double getAndAdd(final double delta) {
		return Double.longBitsToDouble(this.bits.getAndAdd(Double.doubleToLongBits(delta)));
	}
	
//	TODO: Add Javadocs!
	public double getAndDecrement() {
		return Double.longBitsToDouble(this.bits.getAndDecrement());
	}
	
//	TODO: Add Javadocs!
	public double getAndIncrement() {
		return Double.longBitsToDouble(this.bits.getAndIncrement());
	}
	
//	TODO: Add Javadocs!
	public double getAndSet(final double newValue) {
		return Double.longBitsToDouble(this.bits.getAndSet(Double.doubleToLongBits(newValue)));
	}
	
//	TODO: Add Javadocs!
	public double getAndUpdate(final DoubleUnaryOperator updateFunction) {
		double previous;
		double next;
		
		do {
			previous = get();
			next = updateFunction.applyAsDouble(previous);
		} while(!compareAndSet(previous, next));
		
		return previous;
	}
	
//	TODO: Add Javadocs!
	public double incrementAndGet() {
		return Double.longBitsToDouble(this.bits.incrementAndGet());
	}
	
//	TODO: Add Javadocs!
	public double updateAndGet(final DoubleUnaryOperator updateFunction) {
		double previous;
		double next;
		
		do {
			previous = get();
			next = updateFunction.applyAsDouble(previous);
		} while(!compareAndSet(previous, next));
		
		return next;
	}
	
//	TODO: Add Javadocs!
	@Override
	public float floatValue() {
		return (float)(get());
	}
	
//	TODO: Add Javadocs!
	@Override
	public int intValue() {
		return (int)(get());
	}
	
//	TODO: Add Javadocs!
	@Override
	public long longValue() {
		return (long)(get());
	}
	
//	TODO: Add Javadocs!
	public void lazySet(final double newValue) {
		this.bits.lazySet(Double.doubleToLongBits(newValue));
	}
	
//	TODO: Add Javadocs!
	public void set(final double newValue) {
		this.bits.set(Double.doubleToLongBits(newValue));
	}
}