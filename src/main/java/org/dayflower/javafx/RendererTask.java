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
package org.dayflower.javafx;

import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import javafx.concurrent.Task;

final class RendererTask extends Task<Boolean> {
	private final Callable<Boolean> callableCall;
	private final Runnable runnableSucceeded;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public RendererTask(final Callable<Boolean> callableCall, final Runnable runnableSucceeded) {
		this.callableCall = Objects.requireNonNull(callableCall, "callableCall == null");
		this.runnableSucceeded = Objects.requireNonNull(runnableSucceeded, "runnableSucceeded == null");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	protected Boolean call() {
		try {
			return this.callableCall.call();
		} catch(final Exception e) {
			doReportException(e);
			
			return Boolean.FALSE;
		}
	}
	
	@Override
	protected void succeeded() {
		try {
			if(get().booleanValue()) {
				this.runnableSucceeded.run();
			}
		} catch(final ExecutionException | InterruptedException e) {
			doReportException(e);
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static void doReportException(final Exception e) {
		e.printStackTrace();
	}
}