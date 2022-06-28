/**
 * Copyright 2014 - 2022 J&#246;rgen Lundgren
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
package org.dayflower.verlet;

import java.util.List;
import java.util.Objects;

public final class Physics {
	private List<Circle> circles;
	private List<PointMass> pointMasses;
	private float fixedDeltaTimeSeconds;
	private float gravity;
	private float mouseInfluenceScalar;
	private float mouseInfluenceSize;
	private float mouseTearSize;
	private int constraintAccuracy;
	private int fixedDeltaTime;
	private int leftOverDeltaTime;
	private long currentTime;
	private long previousTime;
	private long startTime;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public Physics(final List<Circle> circles, final List<PointMass> pointMasses) {
		this.circles = Objects.requireNonNull(circles, "circles == null");
		this.pointMasses = Objects.requireNonNull(pointMasses, "pointMasses == null");
		this.fixedDeltaTime = 16;
		this.fixedDeltaTimeSeconds = this.fixedDeltaTime / 1000.0F;
		this.gravity = 980.0F;
		this.mouseInfluenceScalar = 5.0F;
		this.mouseInfluenceSize = 20.0F * 20.0F;
		this.mouseTearSize = 8.0F * 8.0F;
		this.constraintAccuracy = 3;
		this.startTime = System.currentTimeMillis();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void setGravity(final float gravity) {
		this.gravity = gravity;
	}
	
	public void toggleGravity() {
		if(Float.compare(this.gravity, 0.0F) != 0) {
			this.gravity = 0.0F;
		} else {
			this.gravity = 980.0F;
		}
	}
	
	public void update(final Input input, final int width, final int height) {
		this.currentTime = System.currentTimeMillis() - this.startTime;
		
		final long deltaTimeMS = this.currentTime - this.previousTime;
		
		this.previousTime = this.currentTime;
		
		final int timeStepAmount = Math.min((int)((float)(deltaTimeMS + this.leftOverDeltaTime) / (float)(this.fixedDeltaTime)), 5);
		
		this.leftOverDeltaTime = (int)(deltaTimeMS) - (timeStepAmount * this.fixedDeltaTime);
		
		this.mouseInfluenceScalar = 1.0F / timeStepAmount;
		
		for(int iteration = 1; iteration <= timeStepAmount; iteration++) {
			for(int x = 0; x < this.constraintAccuracy; x++) {
				for(final PointMass pointMass : this.pointMasses) {
					pointMass.solveConstraints(width, height);
				}
				
				for(final Circle circle : this.circles) {
					circle.solveConstraints(width, height);
				}
			}
			
			for(final PointMass pointMass : this.pointMasses) {
				pointMass.updateInteractions(input, this.mouseInfluenceScalar, this.mouseInfluenceSize, this.mouseTearSize);
				pointMass.updatePhysics(this.gravity, this.fixedDeltaTimeSeconds);
			}
		}
	}
}