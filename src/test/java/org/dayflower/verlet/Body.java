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
import java.util.concurrent.ThreadLocalRandom;

public final class Body {
	private final Circle headCircle;
	private final PointMass elbowLeft;
	private final PointMass elbowRight;
	private final PointMass footLeft;
	private final PointMass footRight;
	private final PointMass handLeft;
	private final PointMass handRight;
	private final PointMass head;
	private final PointMass kneeLeft;
	private final PointMass kneeRight;
	private final PointMass pelvis;
	private final PointMass shoulder;
	private final float headLength;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public Body(final float x, final float y, final float bodyHeight) {
		this.headLength = bodyHeight / 7.5F;
		
		this.shoulder = new PointMass(x + doRandom(-5.0F, 5.0F), y + doRandom(-5.0F, 5.0F), 5.0F);
		this.shoulder.setMass(26.0F);
		
		this.head = new PointMass(x + doRandom(-5.0F, 5.0F), y + doRandom(-5.0F, 5.0F), 5.0F);
		this.head.setMass(4.0F);
		this.head.attachTo(this.shoulder, 5.0F / 4.0F * this.headLength, 1.0F, bodyHeight * 2.0F, true);
		
		this.elbowLeft = new PointMass(x + doRandom(-5.0F, 5.0F), y + doRandom(-5.0F, 5.0F), 5.0F);
		this.elbowLeft.setMass(2.0F);
		this.elbowLeft.attachTo(this.shoulder, this.headLength * 3.0F / 2.0F, 1.0F, bodyHeight * 2.0F, true);
		
		this.elbowRight = new PointMass(x + doRandom(-5.0F, 5.0F), y + doRandom(-5.0F, 5.0F), 5.0F);
		this.elbowRight.setMass(2.0F);
		this.elbowRight.attachTo(this.shoulder, this.headLength * 3.0F / 2.0F, 1.0F, bodyHeight * 2.0F, true);
		
		this.handLeft = new PointMass(x + doRandom(-5.0F, 5.0F), y + doRandom(-5.0F, 5.0F), 5.0F);
		this.handLeft.setMass(2.0F);
		this.handLeft.attachTo(this.elbowLeft, this.headLength * 2.0F, 1.0F, bodyHeight * 2.0F, true);
		
		this.handRight = new PointMass(x + doRandom(-5.0F, 5.0F), y + doRandom(-5.0F, 5.0F), 5.0F);
		this.handRight.setMass(2.0F);
		this.handRight.attachTo(this.elbowRight, this.headLength * 2.0F, 1.0F, bodyHeight * 2.0F, true);
		
		this.pelvis = new PointMass(x + doRandom(-5.0F, 5.0F), y + doRandom(-5.0F, 5.0F), 5.0F);
		this.pelvis.setMass(15.0F);
		this.pelvis.attachTo(this.shoulder, this.headLength * 3.5F, 0.8F, bodyHeight * 2.0F, true);
		this.pelvis.attachTo(this.head, this.headLength * 4.75F, 0.02F, bodyHeight * 2.0F, false);
		
		this.kneeLeft = new PointMass(x + doRandom(-5.0F, 5.0F), y + doRandom(-5.0F, 5.0F), 5.0F);
		this.kneeLeft.setMass(10.0F);
		this.kneeLeft.attachTo(this.pelvis, this.headLength * 2.0F, 1.0F, bodyHeight * 2.0F, true);
		
		this.kneeRight = new PointMass(x + doRandom(-5.0F, 5.0F), y + doRandom(-5.0F, 5.0F), 5.0F);
		this.kneeRight.setMass(10.0F);
		this.kneeRight.attachTo(this.pelvis, this.headLength * 2.0F, 1.0F, bodyHeight * 2.0F, true);
		
		this.footLeft = new PointMass(x + doRandom(-5.0F, 5.0F), y + doRandom(-5.0F, 5.0F), 5.0F);
		this.footLeft.setMass(5.0F);
		this.footLeft.attachTo(this.kneeLeft, this.headLength * 2.0F, 1.0F, bodyHeight * 2.0F, true);
		this.footLeft.attachTo(this.shoulder, this.headLength * 7.5F, 0.001F, bodyHeight * 2.0F, false);
		
		this.footRight = new PointMass(x + doRandom(-5.0F, 5.0F), y + doRandom(-5.0F, 5.0F), 5.0F);
		this.footRight.setMass(5.0F);
		this.footRight.attachTo(this.kneeRight, this.headLength * 2.0F, 1.0F, bodyHeight * 2.0F, true);
		this.footRight.attachTo(this.shoulder, this.headLength * 7.5F, 0.001F, bodyHeight * 2.0F, false);
		
		this.headCircle = new Circle(this.headLength * 0.75F);
		this.headCircle.attachToPointMass(this.head);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void add(final List<Circle> circles, final List<PointMass> pointMasses) {
		circles.add(this.headCircle);
		
		pointMasses.add(this.head);
		pointMasses.add(this.shoulder);
		pointMasses.add(this.pelvis);
		pointMasses.add(this.elbowLeft);
		pointMasses.add(this.elbowRight);
		pointMasses.add(this.handLeft);
		pointMasses.add(this.handRight);
		pointMasses.add(this.kneeLeft);
		pointMasses.add(this.kneeRight);
		pointMasses.add(this.footLeft);
		pointMasses.add(this.footRight);
	}
	
	public void remove(final List<Circle> circles, final List<PointMass> pointMasses) {
		circles.remove(this.headCircle);
		
		pointMasses.remove(this.head);
		pointMasses.remove(this.shoulder);
		pointMasses.remove(this.pelvis);
		pointMasses.remove(this.elbowLeft);
		pointMasses.remove(this.elbowRight);
		pointMasses.remove(this.handLeft);
		pointMasses.remove(this.handRight);
		pointMasses.remove(this.kneeLeft);
		pointMasses.remove(this.kneeRight);
		pointMasses.remove(this.footLeft);
		pointMasses.remove(this.footRight);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static Body createRandom(final int width, final int height) {
		return new Body(doRandom(width), doRandom(height), 40.0F);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static float doRandom(final float bound) {
		return (float)(ThreadLocalRandom.current().nextDouble(bound));
	}
	
	private static float doRandom(final float origin, final float bound) {
		return (float)(ThreadLocalRandom.current().nextDouble(origin, bound));
	}
}