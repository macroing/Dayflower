/**
 * Copyright 2014 - 2025 J&#246;rgen Lundgren
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
package org.dayflower.javafx.concurrent;

import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import javafx.concurrent.Task;

/**
 * A {@code PredicateTask} is a {@code Task} of {@code Boolean} that delegates the computation elsewhere and uses the {@code Boolean} result as a predicate in the {@code succeeded()} method.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class PredicateTask extends Task<Boolean> {
	private final AtomicBoolean hasFinishedSucceeded;
	private final Callable<Boolean> callCallable;
	private final Consumer<Exception> exceptionConsumer;
	private final Runnable succeededRunnable;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code PredicateTask} instance.
	 * <p>
	 * If {@code callCallable} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new PredicateTask(callCallable, () -> {});
	 * }
	 * </pre>
	 * 
	 * @param callCallable a {@code Callable} instance that is delegated to in the {@code call()} method to perform the computation and return a {@code Boolean} result
	 * @throws NullPointerException thrown if, and only if, {@code callCallable} is {@code null}
	 */
	public PredicateTask(final Callable<Boolean> callCallable) {
		this(callCallable, () -> {/* Do nothing. */});
	}
	
	/**
	 * Constructs a new {@code PredicateTask} instance.
	 * <p>
	 * If either {@code callCallable} or {@code succeededRunnable} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new PredicateTask(callCallable, succeededRunnable, e -> e.printStackTrace());
	 * }
	 * </pre>
	 * 
	 * @param callCallable a {@code Callable} instance that is delegated to in the {@code call()} method to perform the computation and return a {@code Boolean} result
	 * @param succeededRunnable a {@code Runnable} instance that is delegated to in the {@code succeeded()} method to perform GUI updates if, and only if, the {@code call()} method returns {@code Boolean.TRUE}
	 * @throws NullPointerException thrown if, and only if, either {@code callCallable} or {@code succeededRunnable} are {@code null}
	 */
	public PredicateTask(final Callable<Boolean> callCallable, final Runnable succeededRunnable) {
		this(callCallable, succeededRunnable, e -> e.printStackTrace());
	}
	
	/**
	 * Constructs a new {@code PredicateTask} instance.
	 * <p>
	 * If either {@code callCallable}, {@code succeededRunnable} or {@code exceptionConsumer} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param callCallable a {@code Callable} instance that is delegated to in the {@code call()} method to perform the computation and return a {@code Boolean} result
	 * @param succeededRunnable a {@code Runnable} instance that is delegated to in the {@code succeeded()} method to perform GUI updates if, and only if, the {@code call()} method returns {@code Boolean.TRUE}
	 * @param exceptionConsumer a {@code Consumer} instance that is delegated to if, and only if, an {@code Exception} is caught in the {@code call()} or {@code succeeded()} methods
	 * @throws NullPointerException thrown if, and only if, either {@code callCallable}, {@code succeededRunnable} or {@code exceptionConsumer} are {@code null}
	 */
	public PredicateTask(final Callable<Boolean> callCallable, final Runnable succeededRunnable, final Consumer<Exception> exceptionConsumer) {
		this.callCallable = Objects.requireNonNull(callCallable, "callCallable == null");
		this.succeededRunnable = Objects.requireNonNull(succeededRunnable, "succeededRunnable == null");
		this.exceptionConsumer = Objects.requireNonNull(exceptionConsumer, "exceptionConsumer == null");
		this.hasFinishedSucceeded = new AtomicBoolean();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns {@code true} if, and only if, the {@code Runnable} instance that is delegated to in the {@code succeeded()} method to perform GUI updates has finished, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, the {@code Runnable} instance that is delegated to in the {@code succeeded()} method to perform GUI updates has finished, {@code false} otherwise
	 */
	public boolean hasFinishedSucceeded() {
		return this.hasFinishedSucceeded.get();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * This method is called from a background thread when this {@code PredicateTask} is executed.
	 * <p>
	 * Returns {@code Boolean.TRUE} on success and {@code Boolean.FALSE} on failure.
	 * <p>
	 * This method delegates the computation and return value to the {@code call()} method of {@code callCallable}.
	 * <p>
	 * If an {@code Exception} is caught, the {@code accept(Exception)} method of {@code exceptionConsumer} will be called with that {@code Exception} instance as its only parameter argument and {@code Boolean.FALSE} will be returned.
	 * 
	 * @return {@code Boolean.TRUE} on success and {@code Boolean.FALSE} on failure
	 */
	@Override
	protected Boolean call() {
		try {
			return this.callCallable.call();
		} catch(final Exception e) {
			this.exceptionConsumer.accept(e);
			
			return Boolean.FALSE;
		}
	}
	
	/**
	 * This method is called on the {@code FX Application Thread} whenever the state of this {@code PredicateTask} has transitioned to the {@code SUCCEEDED} state.
	 * <p>
	 * If the {@code call()} method returns {@code Boolean.TRUE}, the {@code run()} method of {@code succeededRunnable} will be called.
	 * <p>
	 * If one of {@code ExecutionException}, {@code InterruptedException} or {@code RuntimeException} is caught, the {@code accept(Exception)} method of {@code exceptionConsumer} will be called with that {@code Exception} instance as its only parameter
	 * argument.
	 */
	@Override
	protected void succeeded() {
		try {
			if(get().booleanValue()) {
				this.succeededRunnable.run();
			}
		} catch(final ExecutionException | InterruptedException | RuntimeException e) {
			this.exceptionConsumer.accept(e);
		} finally {
			this.hasFinishedSucceeded.set(true);
		}
	}
}