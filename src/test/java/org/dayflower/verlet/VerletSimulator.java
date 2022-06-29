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

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

public final class VerletSimulator extends Canvas {
	private static final long serialVersionUID = 1L;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final BufferStrategy bufferStrategy;
	private final BufferedImage bufferedImage;
	private final Camera camera;
	private final Graphics graphics;
	private final Input input;
	private final JFrame jFrame;
	private final List<Circle> circles;
	private final List<PointMass> pointMasses;
	private final Physics physics;
	private final int resolutionX;
	private final int resolutionY;
	private final int resolutionZ;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public VerletSimulator(final int resolutionX, final int resolutionY, final int resolutionZ) {
		final Dimension size = new Dimension(resolutionX, resolutionY);
		
		setPreferredSize(size);
		setMinimumSize(size);
		setMaximumSize(size);
		
		createBufferStrategy(1);
		
		this.bufferStrategy = getBufferStrategy();
		this.bufferedImage = new BufferedImage(resolutionX, resolutionY, BufferedImage.TYPE_INT_ARGB);
		this.camera = new Camera();
		this.input = new Input();
		this.jFrame = new JFrame();
		this.circles = new ArrayList<>();
		this.pointMasses = new ArrayList<>();
		this.physics = new Physics(this.circles, this.pointMasses);
		this.resolutionX = resolutionX;
		this.resolutionY = resolutionY;
		this.resolutionZ = resolutionZ;
		
		this.jFrame.add(this);
		this.jFrame.pack();
		this.jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.jFrame.setLocationRelativeTo(null);
		this.jFrame.setResizable(false);
		this.jFrame.setTitle("Verlet Simulator");
		
		this.graphics = this.bufferStrategy.getDrawGraphics();
		
		doCreateCurtain();
		doCreateBodies();
		
		addFocusListener(this.input);
		addKeyListener(this.input);
		addMouseListener(this.input);
		addMouseMotionListener(this.input);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static void main(final String[] args) {
		final
		VerletSimulator verletSimulator = new VerletSimulator(640, 480, 100);
		verletSimulator.doShow();
		
		long previousTime = System.nanoTime();
		
		while(true) {
			try {
				final long currentTime = System.nanoTime();
				
				final float delta = (float)((currentTime - previousTime) / 1000000000.0D);
				
				previousTime = currentTime;
				
				verletSimulator.doUpdate(delta);
				verletSimulator.doDraw();
				verletSimulator.doSwapBuffers();
				
				Thread.sleep(5L);
			} catch(final Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private void doCreateBodies() {
		for(int i = 0; i < 25; i++) {
			final
			Body body = Body.createRandom(this.resolutionX, this.resolutionY);
			body.add(this.circles, this.pointMasses);
		}
	}
	
	private void doCreateCurtain() {
		final float restingDistances = 6.0F;
		final float stiffnesses = 1.0F;
		
		final int curtainHeight = 40;
		final int curtainWidth = 60;
		
		final int midWidth = (int)(this.resolutionX / 2.0F - (curtainWidth * restingDistances) / 2.0F);
		
		final int yStart = 25;
		
		for(int y = 0; y <= curtainHeight; y++) {
			for(int x = 0; x <= curtainWidth; x++) {
				final PointMass pointMass = new PointMass(midWidth + x * restingDistances, y * restingDistances + yStart, 5.0F);
				
				if(x != 0) {
					pointMass.attachTo(this.pointMasses.get(this.pointMasses.size() - 1), restingDistances, stiffnesses);
				}
				
				if(y != 0) {
					pointMass.attachTo(this.pointMasses.get((y - 1) * (curtainWidth + 1) + x), restingDistances, stiffnesses);
				}
				
				if(y == 0) {
					pointMass.pinTo(pointMass.getX(), pointMass.getY(), pointMass.getZ());
				}
				
				this.pointMasses.add(pointMass);
			}
		}
	}
	
	private void doDraw() {
		final Graphics g = this.bufferedImage.getGraphics();
		
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		this.physics.update(this.input, this.resolutionX, this.resolutionY, this.resolutionZ);
		
		for(final PointMass pointMass : this.pointMasses) {
			pointMass.draw(this.camera, g);
		}
		
		for(final Circle circle : this.circles) {
			circle.draw(this.camera, g);
		}
	}
	
	private void doShow() {
		this.jFrame.setVisible(true);
		
		setFocusable(true);
		
		requestFocus();
	}
	
	private void doSwapBuffers() {
		this.graphics.drawImage(this.bufferedImage, 0, 0, this.resolutionX, this.resolutionY, null);
		
		this.bufferStrategy.show();
	}
	
	private void doUpdate(final float delta) {
		final float distance = 50.0F * delta;
		
		final float sensitivityX = 4.66F * delta;
		final float sensitivityY = 4.0F * delta;
		
		if(this.input.isKeyPressed(KeyEvent.VK_R)) {
			this.pointMasses.clear();
			this.circles.clear();
			
			doCreateCurtain();
			doCreateBodies();
		}
		
		if(this.input.isKeyPressed(KeyEvent.VK_G)) {
			this.physics.toggleGravity();
		}
		
		if(this.input.isKeyPressed(KeyEvent.VK_W)) {
			this.camera.moveForward(distance);
		}
		
		if(this.input.isKeyPressed(KeyEvent.VK_S)) {
			this.camera.moveBack(distance);
		}
		
		if(this.input.isKeyPressed(KeyEvent.VK_A)) {
			this.camera.moveLeft(distance);
		}
		
		if(this.input.isKeyPressed(KeyEvent.VK_D)) {
			this.camera.moveRight(distance);
		}
		
		if(this.input.isKeyPressed(KeyEvent.VK_RIGHT)) {
			this.camera.rotateUp(sensitivityX);
		}
		
		if(this.input.isKeyPressed(KeyEvent.VK_LEFT)) {
			this.camera.rotateUp(-sensitivityX);
		}
		
		if(this.input.isKeyPressed(KeyEvent.VK_DOWN)) {
			this.camera.rotateRight(sensitivityY);
		}
		
		if(this.input.isKeyPressed(KeyEvent.VK_UP)) {
			this.camera.rotateRight(-sensitivityY);
		}
	}
}