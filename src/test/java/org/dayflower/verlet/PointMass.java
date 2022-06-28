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
import java.util.ArrayList;
import java.util.List;

public final class PointMass {
	private List<Link> links;
	private boolean isPinned;
	private float accX;
	private float accY;
	private float accZ;
	private float lastX;
	private float lastY;
	private float lastZ;
	private float mass;
	private float pinX;
	private float pinY;
	private float pinZ;
	private float x;
	private float y;
	private float z;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public PointMass(final float x, final float y) {
		this(x, y, 0.0F);
	}
	
	public PointMass(final float x, final float y, final float z) {
		this.links = new ArrayList<>();
		this.lastX = x;
		this.lastY = y;
		this.lastZ = z;
		this.mass = 1.0F;
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public float getMass() {
		return this.mass;
	}
	
	public float getX() {
		return this.x;
	}
	
	public float getY() {
		return this.y;
	}
	
	public float getZ() {
		return this.z;
	}
	
	public void add(final float x, final float y) {
		add(x, y, 0.0F);
	}
	
	public void add(final float x, final float y, final float z) {
		this.x += x;
		this.y += y;
		this.z += z;
	}
	
	public void applyForce(final float forceX, final float forceY) {
		applyForce(forceX, forceY, 0.0F);
	}
	
	public void applyForce(final float forceX, final float forceY, final float forceZ) {
		this.accX += forceX / this.mass;
		this.accY += forceY / this.mass;
		this.accZ += forceZ / this.mass;
	}
	
	public void attachTo(final PointMass pointMass, final float restingDistance, final float stiffness) {
		attachTo(pointMass, restingDistance, stiffness, 30.0F, true);
	}
	
	public void attachTo(final PointMass pointMass, final float restingDistance, final float stiffness, final boolean isDrawing) {
		attachTo(pointMass, restingDistance, stiffness, 30.0F, isDrawing);
	}
	
	public void attachTo(final PointMass pointMass, final float restingDistance, final float stiffness, final float tearSensitivity) {
		attachTo(pointMass, restingDistance, stiffness, tearSensitivity, true);
	}
	
	public void attachTo(final PointMass pointMass, final float restingDistance, final float stiffness, final float tearSensitivity, final boolean isDrawing) {
		this.links.add(new Link(this, pointMass, restingDistance, stiffness, tearSensitivity, isDrawing));
	}
	
	public void draw(final Graphics g) {
		g.setColor(Color.BLACK);
		
		if(this.links.size() > 0) {
			for(int i = 0; i < this.links.size(); i++) {
				final
				Link link = this.links.get(i);
				link.draw(g);
			}
		} else {
			g.drawRect((int)(this.x), (int)(this.y), 1, 1);
		}
	}
	
	public void pinTo(final float pinX, final float pinY) {
		pinTo(pinX, pinY, 0.0F);
	}
	
	public void pinTo(final float pinX, final float pinY, final float pinZ) {
		this.isPinned = true;
		
		this.pinX = pinX;
		this.pinY = pinY;
		this.pinZ = pinZ;
	}
	
	public void removeLink(final Link link) {
		this.links.remove(link);
	}
	
	public void setMass(final float mass) {
		this.mass = mass;
	}
	
	public void setX(final float x) {
		this.x = x;
	}
	
	public void setY(final float y) {
		this.y = y;
	}
	
	public void setZ(final float z) {
		this.z = z;
	}
	
	public void solveConstraints(final int x, final int y) {
		solveConstraints(x, y, 0.0F);
	}
	
	public void solveConstraints(final int x, final int y, final float z) {
		for(int i = 0; i < this.links.size(); i++) {
			final
			Link link = this.links.get(i);
			link.solve();
		}
		
		if(this.z < 1.0F) {
			this.z = 2.0F - this.z;
		}
		
		if(this.z > z - 1.0F) {
			this.z = 2.0F * (z - 1.0F) - this.z;
		}
		
		if(this.y < 1.0F) {
			this.y = 2.0F - this.y;
		}
		
		if(this.y > y - 1.0F) {
			this.y = 2.0F * (y - 1.0F) - this.y;
		}
		
		if(this.x > x - 1.0F) {
			this.x = 2.0F * (x - 1.0F) - this.x;
		}
		
		if(this.x < 1.0F) {
			this.x = 2.0F - this.x;
		}
		
		if(this.isPinned) {
			this.x = this.pinX;
			this.y = this.pinY;
			this.z = this.pinZ;
		}
	}
	
	public void subtract(final float x, final float y) {
		subtract(x, y, 0.0F);
	}
	
	public void subtract(final float x, final float y, final float z) {
		this.x -= x;
		this.y -= y;
		this.z -= z;
	}
	
	public void updateInteractions(final Input input, final float mouseInfluenceScalar, final float mouseInfluenceSize, final float mouseTearSize) {
		if(input.isMouseButtonPressed()) {
			final float distanceSquared = doDistanceSquared(input.getMouseXPressed(), input.getMouseYPressed(), input.getMouseX(), input.getMouseY(), this.x, this.y);
			
			if(input.isMouseButtonPressed(0)) {
				if(distanceSquared < mouseInfluenceSize) {
					this.lastX = this.x - (input.getMouseX() - input.getMouseXPressed()) * mouseInfluenceScalar;
					this.lastY = this.y - (input.getMouseY() - input.getMouseYPressed()) * mouseInfluenceScalar;
				}
			} else {
				if(distanceSquared < mouseTearSize) {
					this.links.clear();
				}
			}
		}
	}
	
	public void updatePhysics(final float gravity, final float timeStep) {
		applyForce(0, this.mass * gravity);
		
		final float velX = (this.x - this.lastX) * 0.99F;
		final float velY = (this.y - this.lastY) * 0.99F;
		final float velZ = (this.z - this.lastZ) * 0.99F; 
		
		final float timeStepSq = timeStep * timeStep;
		
		final float nextX = this.x + velX + 0.5F * this.accX * timeStepSq;
		final float nextY = this.y + velY + 0.5F * this.accY * timeStepSq;
		final float nextZ = this.z + velZ + 0.5F * this.accZ * timeStepSq;
		
		this.lastX = this.x;
		this.lastY = this.y;
		this.lastZ = this.z;
		
		this.x = nextX;
		this.y = nextY;
		this.z = nextZ;
		
		this.accX = 0.0F;
		this.accY = 0.0F;
		this.accZ = 0.0F;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static float doDistanceSquared(final float aX, final float aY, final float bX, final float bY, final float cX, final float cY) {
		final float dX = aX - cX;
		final float dY = aY - cY;
		
		final float eX = bX - aX;
		final float eY = bY - aY;
		
		final float length = eX * eX + eY * eY;
		
		final float determinantDE = -dX * eX + -dY * eY;
		
		if(determinantDE < 0.0F || determinantDE > length) {
			final float fX = bX - cX;
			final float fY = bY - cY;
			
			return Math.min(dX * dX + dY * dY, fX * fX + fY * fY);
		}
		
		final float determinantED = eX * dY - eY * dX;
		
		return (determinantED * determinantED) / length;
	}
}