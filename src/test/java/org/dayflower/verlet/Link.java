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

import org.dayflower.geometry.Point2I;
import org.dayflower.utility.Floats;

public final class Link {
	private PointMass a;
	private PointMass b;
	private boolean isDrawing;
	private float restingDistance;
	private float stiffness;
	private float tearSensitivity;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public Link(final PointMass a, final PointMass b, final float restingDistance, final float stiffness, final float tearSensitivity, final boolean isDrawing) {
		this.a = a;
		this.b = b;
		this.restingDistance = restingDistance;
		this.stiffness = stiffness;
		this.tearSensitivity = tearSensitivity;
		this.isDrawing = isDrawing;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void draw(final Camera camera, final Graphics g) {
		if(this.isDrawing) {
			final Point2I pA = camera.getProjectedPoint(this.a.getX(), this.a.getY(), this.a.getZ());
			final Point2I pB = camera.getProjectedPoint(this.b.getX(), this.b.getY(), this.b.getZ());
			
			if(pA != null && pB != null) {
				g.setColor(Color.BLACK);
				g.drawLine(pA.x, pA.y, pB.x, pB.y);
			}
		}
	}
	
	public void solve() {
		final float dX = this.a.getX() - this.b.getX();
		final float dY = this.a.getY() - this.b.getY();
		final float dZ = this.a.getZ() - this.b.getZ();
		
		final float d = Floats.sqrt(dX * dX + dY * dY + dZ * dZ);
		
		final float difference = (this.restingDistance - d) / d;
		
		if(d > this.tearSensitivity) {
			this.a.removeLink(this);
		}
		
		final float massReciprocalA = 1.0F / this.a.getMass();
		final float massReciprocalB = 1.0F / this.b.getMass();
		
		final float stiffnessA = (massReciprocalA / (massReciprocalA + massReciprocalB)) * this.stiffness;
		final float stiffnessB = this.stiffness - stiffnessA;
		
		this.a.add(dX * stiffnessA * difference, dY * stiffnessA * difference, dZ * stiffnessA * difference);
		
		this.b.subtract(dX * stiffnessB * difference, dY * stiffnessB * difference, dZ * stiffnessB * difference);
	}
}