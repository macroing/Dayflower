/**
 * Copyright 2014 - 2023 J&#246;rgen Lundgren
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
package org.dayflower.javafx.application;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;

import org.dayflower.parameter.Parameter;
import org.dayflower.parameter.ParameterLoader;
import org.dayflower.utility.ParameterArguments;

import javafx.application.Platform;
import javafx.stage.Stage;

final class ParameterLoaderImpl implements ParameterLoader {
	private final Stage stage;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public ParameterLoaderImpl(final Stage stage) {
		this.stage = Objects.requireNonNull(stage, "stage == null");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public void load(final List<Parameter> parameters) {
		ParameterArguments.requireNonNullList(parameters, "parameters");
		
		final CountDownLatch countDownLatch = new CountDownLatch(1);
		
		Platform.runLater(() -> {
			try {
				final
				ParameterDialog parameterDialog = new ParameterDialog(this.stage, parameters);
				parameterDialog.showAndWait();
			} finally {
				countDownLatch.countDown();
			}
		});
		
		try {
			countDownLatch.await();
		} catch(final InterruptedException e) {
//			Do nothing for now.
		}
	}
}