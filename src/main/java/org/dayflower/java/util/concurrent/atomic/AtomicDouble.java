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
package org.dayflower.java.util.concurrent.atomic;

import java.util.concurrent.atomic.AtomicLong;
import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleUnaryOperator;

/**
 * A {@code double} value that may be updated atomically.
 * <p>
 * An {@code AtomicDouble} is used in applications such as atomically incremented counters, and cannot be used as a replacement for a {@code Double}. However, this class does extend {@code Number} to allow uniform access by tools and utilities that
 * deal with numerically-based classes.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class AtomicDouble extends Number {
	private static final long serialVersionUID = 1L;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final AtomicLong bits;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code AtomicDouble} with an initial value of {@code 0.0D}.
	 */
	public AtomicDouble() {
		this(0.0D);
	}
	
	/**
	 * Constructs a new {@code AtomicDouble} with an initial value of {@code initialValue}.
	 * 
	 * @param initialValue the initial value
	 */
	public AtomicDouble(final double initialValue) {
		this.bits = new AtomicLong(Double.doubleToLongBits(initialValue));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code String} representation of the current value.
	 * 
	 * @return a {@code String} representation of the current value
	 */
	@Override
	public String toString() {
		return Double.toString(get());
	}
	
	/**
	 * Atomically sets the current value to the given updated value if the current value equals the expected value.
	 * <p>
	 * Returns {@code true} if, and only if, the current value was set to {@code update}, {@code false} otherwise.
	 * 
	 * @param expect the expected value
	 * @param update the new value
	 * @return {@code true} if, and only if, the current value was set to {@code update}, {@code false} otherwise
	 */
	public boolean compareAndSet(final double expect, final double update) {
		return this.bits.compareAndSet(Double.doubleToLongBits(expect), Double.doubleToLongBits(update));
	}
	
	/**
	 * Atomically sets the current value to the given updated value if the current value equals the expected value.
	 * <p>
	 * Returns {@code true} if, and only if, the current value was set to {@code update}, {@code false} otherwise.
	 * <p>
	 * This method may fail spuriously and does not provide ordering guarantees, so it is only rarely an appropriate alternative to {@code compareAndSet}.
	 * 
	 * @param expect the expected value
	 * @param update the new value
	 * @return {@code true} if, and only if, the current value was set to {@code update}, {@code false} otherwise
	 */
	public boolean weakCompareAndSet(final double expect, final double update) {
		return this.bits.weakCompareAndSet(Double.doubleToLongBits(expect), Double.doubleToLongBits(update));
	}
	
	/**
	 * Atomically updates the current value with the results of applying the given function to the current and given values.
	 * <p>
	 * Returns the updated value.
	 * <p>
	 * If {@code accumulatorFunction} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * The function should be side-effect-free, since it may be re-applied when attempted updates fail due to contention among threads. The function is applied with the current value as its first argument, and the given update as the second argument.
	 * 
	 * @param x the update value
	 * @param accumulatorFunction a side-effect-free function of two arguments
	 * @return the updated value
	 * @throws NullPointerException thrown if, and only if, {@code accumulatorFunction} is {@code null}
	 */
	public double accumulateAndGet(final double x, final DoubleBinaryOperator accumulatorFunction) {
		double previous;
		double next;
		
		do {
			previous = get();
			next = accumulatorFunction.applyAsDouble(previous, x);
		} while(!compareAndSet(previous, next));
		
		return next;
	}
	
	/**
	 * Atomically adds {@code delta} to the current value.
	 * <p>
	 * Returns the updated value.
	 * 
	 * @param delta the value to add
	 * @return the updated value
	 */
	public double addAndGet(final double delta) {
		return updateAndGet(currentValue -> currentValue + delta);
	}
	
	/**
	 * Atomically decrements the current value by one.
	 * <p>
	 * Returns the updated value.
	 * 
	 * @return the updated value
	 */
	public double decrementAndGet() {
		return updateAndGet(currentValue -> currentValue - 1.0D);
	}
	
	/**
	 * Returns the current value of this {@code AtomicDouble} instance as a {@code double}.
	 * 
	 * @return the current value of this {@code AtomicDouble} instance as a {@code double}
	 */
	@Override
	public double doubleValue() {
		return get();
	}
	
	/**
	 * Returns the current value.
	 * 
	 * @return the current value
	 */
	public double get() {
		return Double.longBitsToDouble(this.bits.get());
	}
	
	/**
	 * Atomically updates the current value with the results of applying the given function to the current and given values.
	 * <p>
	 * Returns the previous value.
	 * <p>
	 * If {@code accumulatorFunction} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * The function should be side-effect-free, since it may be re-applied when attempted updates fail due to contention among threads. The function is applied with the current value as its first argument, and the given update as the second argument.
	 * 
	 * @param x the update value
	 * @param accumulatorFunction a side-effect-free function of two arguments
	 * @return the previous value
	 * @throws NullPointerException thrown if, and only if, {@code accumulatorFunction} is {@code null}
	 */
	public double getAndAccumulate(final double x, final DoubleBinaryOperator accumulatorFunction) {
		double previous;
		double next;
		
		do {
			previous = get();
			next = accumulatorFunction.applyAsDouble(previous, x);
		} while(!compareAndSet(previous, next));
		
		return previous;
	}
	
	/**
	 * Atomically adds {@code delta} to the current value.
	 * <p>
	 * Returns the previous value.
	 * 
	 * @param delta the value to add
	 * @return the previous value
	 */
	public double getAndAdd(final double delta) {
		return getAndUpdate(currentValue -> currentValue + delta);
	}
	
	/**
	 * Atomically decrements the current value by one.
	 * <p>
	 * Returns the previous value.
	 * 
	 * @return the previous value
	 */
	public double getAndDecrement() {
		return getAndUpdate(currentValue -> currentValue - 1.0D);
	}
	
	/**
	 * Atomically increments the current value by one.
	 * <p>
	 * Returns the previous value.
	 * 
	 * @return the previous value
	 */
	public double getAndIncrement() {
		return getAndUpdate(currentValue -> currentValue + 1.0D);
	}
	
	/**
	 * Atomically sets the current value to {@code newValue}.
	 * <p>
	 * Returns the previous value.
	 * 
	 * @param newValue the new value
	 * @return the previous value
	 */
	public double getAndSet(final double newValue) {
		return Double.longBitsToDouble(this.bits.getAndSet(Double.doubleToLongBits(newValue)));
	}
	
	/**
	 * Atomically updates the current value with the results of applying the given function.
	 * <p>
	 * Returns the previous value.
	 * <p>
	 * If {@code updateFunction} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * The function should be side-effect-free, since it may be re-applied when attempted updates fail due to contention among threads.
	 * 
	 * @param updateFunction a side-effect-free function
	 * @return the previous value
	 * @throws NullPointerException thrown if, and only if, {@code updateFunction} is {@code null}
	 */
	public double getAndUpdate(final DoubleUnaryOperator updateFunction) {
		double previous;
		double next;
		
		do {
			previous = get();
			next = updateFunction.applyAsDouble(previous);
		} while(!compareAndSet(previous, next));
		
		return previous;
	}
	
	/**
	 * Atomically increments the current value by one.
	 * <p>
	 * Returns the updated value.
	 * 
	 * @return the updated value
	 */
	public double incrementAndGet() {
		return updateAndGet(currentValue -> currentValue + 1.0D);
	}
	
	/**
	 * Atomically updates the current value with the results of applying the given function.
	 * <p>
	 * Returns the updated value.
	 * <p>
	 * If {@code updateFunction} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * The function should be side-effect-free, since it may be re-applied when attempted updates fail due to contention among threads.
	 * 
	 * @param updateFunction a side-effect-free function
	 * @return the updated value
	 * @throws NullPointerException thrown if, and only if, {@code updateFunction} is {@code null}
	 */
	public double updateAndGet(final DoubleUnaryOperator updateFunction) {
		double previous;
		double next;
		
		do {
			previous = get();
			next = updateFunction.applyAsDouble(previous);
		} while(!compareAndSet(previous, next));
		
		return next;
	}
	
	/**
	 * Returns the current value of this {@code AtomicDouble} instance as a {@code float} after a widening primitive conversion.
	 * 
	 * @return the current value of this {@code AtomicDouble} instance as a {@code float} after a widening primitive conversion
	 */
	@Override
	public float floatValue() {
		return (float)(get());
	}
	
	/**
	 * Returns the current value of this {@code AtomicDouble} instance as an {@code int} after a widening primitive conversion.
	 * 
	 * @return the current value of this {@code AtomicDouble} instance as an {@code int} after a widening primitive conversion
	 */
	@Override
	public int intValue() {
		return (int)(get());
	}
	
	/**
	 * Returns the current value of this {@code AtomicDouble} instance as a {@code long} after a widening primitive conversion.
	 * 
	 * @return the current value of this {@code AtomicDouble} instance as a {@code long} after a widening primitive conversion
	 */
	@Override
	public long longValue() {
		return (long)(get());
	}
	
	/**
	 * Eventually sets the current value to {@code newValue}.
	 * 
	 * @param newValue the new value
	 */
	public void lazySet(final double newValue) {
		this.bits.lazySet(Double.doubleToLongBits(newValue));
	}
	
	/**
	 * Sets the current value to {@code newValue}.
	 * 
	 * @param newValue the new value
	 */
	public void set(final double newValue) {
		this.bits.set(Double.doubleToLongBits(newValue));
	}
}