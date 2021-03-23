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
import java.util.concurrent.atomic.AtomicInteger;

//TODO: Add Javadocs!
public final class AtomicFloat extends Number {
	private static final long serialVersionUID = 1L;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final AtomicInteger bits;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public AtomicFloat() {
		this(0.0F);
	}
	
//	TODO: Add Javadocs!
	public AtomicFloat(final float initialValue) {
		this.bits = new AtomicInteger(Float.floatToIntBits(initialValue));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	@Override
	public String toString() {
		return Float.toString(get());
	}
	
//	TODO: Add Javadocs!
	public boolean compareAndSet(final float expect, final float update) {
		return this.bits.compareAndSet(Float.floatToIntBits(expect), Float.floatToIntBits(update));
	}
	
//	TODO: Add Javadocs!
	public boolean weakCompareAndSet(final float expect, final float update) {
		return this.bits.weakCompareAndSet(Float.floatToIntBits(expect), Float.floatToIntBits(update));
	}
	
//	TODO: Add Javadocs!
	@Override
	public double doubleValue() {
		return get();
	}
	
//	TODO: Add Javadocs!
	public float accumulateAndGet(final float x, final FloatBinaryOperator accumulatorFunction) {
		float previous;
		float next;
		
		do {
			previous = get();
			next = accumulatorFunction.applyAsFloat(previous, x);
		} while(!compareAndSet(previous, next));
		
		return next;
	}
	
//	TODO: Add Javadocs!
	public float addAndGet(final float delta) {
		return Float.intBitsToFloat(this.bits.addAndGet(Float.floatToIntBits(delta)));
	}
	
//	TODO: Add Javadocs!
	public float decrementAndGet() {
		return Float.intBitsToFloat(this.bits.decrementAndGet());
	}
	
//	TODO: Add Javadocs!
	@Override
	public float floatValue() {
		return get();
	}
	
//	TODO: Add Javadocs!
	public float get() {
		return Float.intBitsToFloat(this.bits.get());
	}
	
//	TODO: Add Javadocs!
	public float getAndAccumulate(final float x, final FloatBinaryOperator accumulatorFunction) {
		float previous;
		float next;
		
		do {
			previous = get();
			next = accumulatorFunction.applyAsFloat(previous, x);
		} while(!compareAndSet(previous, next));
		
		return previous;
	}
	
//	TODO: Add Javadocs!
	public float getAndAdd(final float delta) {
		return Float.intBitsToFloat(this.bits.getAndAdd(Float.floatToIntBits(delta)));
	}
	
//	TODO: Add Javadocs!
	public float getAndDecrement() {
		return Float.intBitsToFloat(this.bits.getAndDecrement());
	}
	
//	TODO: Add Javadocs!
	public float getAndIncrement() {
		return Float.intBitsToFloat(this.bits.getAndIncrement());
	}
	
//	TODO: Add Javadocs!
	public float getAndSet(final float newValue) {
		return Float.intBitsToFloat(this.bits.getAndSet(Float.floatToIntBits(newValue)));
	}
	
//	TODO: Add Javadocs!
	public float getAndUpdate(final FloatUnaryOperator updateFunction) {
		float previous;
		float next;
		
		do {
			previous = get();
			next = updateFunction.applyAsFloat(previous);
		} while(!compareAndSet(previous, next));
		
		return previous;
	}
	
//	TODO: Add Javadocs!
	public float incrementAndGet() {
		return Float.intBitsToFloat(this.bits.incrementAndGet());
	}
	
//	TODO: Add Javadocs!
	public float updateAndGet(final FloatUnaryOperator updateFunction) {
		float previous;
		float next;
		
		do {
			previous = get();
			next = updateFunction.applyAsFloat(previous);
		} while(!compareAndSet(previous, next));
		
		return next;
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
	public void lazySet(final float newValue) {
		this.bits.lazySet(Float.floatToIntBits(newValue));
	}
	
//	TODO: Add Javadocs!
	public void set(final float newValue) {
		this.bits.set(Float.floatToIntBits(newValue));
	}
}