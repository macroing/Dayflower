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

import java.awt.Color;
import java.awt.Graphics;

public final class Circle {
	private PointMass attachedPointMass;
	private final float radius;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public Circle(final float radius) {
		this.radius = radius;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void attachToPointMass(final PointMass attachedPointMass) {
		this.attachedPointMass = attachedPointMass;
	}
	
	public void draw(final Graphics g) {
		g.setColor(Color.BLACK);
		g.fillOval((int)(this.attachedPointMass.getX()), (int)(this.attachedPointMass.getY()), (int)(this.radius * 2.0F), (int)(this.radius * 2.0F));
	}
	
	public void solveConstraints(final int width, final int height) {
		float x = this.attachedPointMass.getX();
		float y = this.attachedPointMass.getY();
		
		if(y < this.radius) {
			y = 2.0F * (this.radius) - y;
		}
		
		if(y > height - this.radius) {
			y = 2.0F * (height - this.radius) - y;
		}
		
		if(x > width - this.radius) {
			x = 2.0F * (width - this.radius) - x;
		}
		
		if(x < this.radius) {
			x = 2.0F * this.radius - x;
		}
		
		this.attachedPointMass.setX(x);
		this.attachedPointMass.setY(y);
	}
}