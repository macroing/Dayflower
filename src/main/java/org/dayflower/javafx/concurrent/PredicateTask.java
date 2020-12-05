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
package org.dayflower.javafx.concurrent;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

import javafx.concurrent.Task;

//TODO: Add Javadocs!
public final class PredicateTask extends Task<Boolean> {
	private final Callable<Boolean> callCallable;
	private final Consumer<Exception> exceptionConsumer;
	private final Runnable succeededRunnable;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public PredicateTask(final Callable<Boolean> callCallable) {
		this(callCallable, () -> {});
	}
	
//	TODO: Add Javadocs!
	public PredicateTask(final Callable<Boolean> callCallable, final Runnable succeededRunnable) {
		this(callCallable, succeededRunnable, e -> e.printStackTrace());
	}
	
//	TODO: Add Javadocs!
	public PredicateTask(final Callable<Boolean> callCallable, final Runnable succeededRunnable, final Consumer<Exception> exceptionConsumer) {
		this.callCallable = Objects.requireNonNull(callCallable, "callCallable == null");
		this.succeededRunnable = Objects.requireNonNull(succeededRunnable, "succeededRunnable == null");
		this.exceptionConsumer = Objects.requireNonNull(exceptionConsumer, "exceptionConsumer == null");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	@Override
	protected Boolean call() {
		try {
			return this.callCallable.call();
		} catch(final Exception e) {
			this.exceptionConsumer.accept(e);
			
			return Boolean.FALSE;
		}
	}
	
//	TODO: Add Javadocs!
	@Override
	protected void succeeded() {
		try {
			if(get().booleanValue()) {
				this.succeededRunnable.run();
			}
		} catch(final ExecutionException | InterruptedException e) {
			this.exceptionConsumer.accept(e);
		}
	}
}