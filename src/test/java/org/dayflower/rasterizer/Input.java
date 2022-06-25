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
package org.dayflower.rasterizer;

import java.awt.AWTException;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public final class Input implements KeyListener, FocusListener, MouseListener, MouseMotionListener {
	private final AtomicInteger mouseMovementX = new AtomicInteger();
	private final AtomicInteger mouseMovementY = new AtomicInteger();
	private boolean isRecenteringMouse = true;
	private final boolean[] keys = new boolean[65536];
	private final boolean[] mouseButtons = new boolean[4];
	private int mouseX;
	private int mouseY;
	private final JFrame jFrame;
	private final Point centerPoint = new Point();
	private final Robot robot = doCreateRobot();
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public Input(final JFrame jFrame) {
		this.jFrame = jFrame;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public boolean getKey(final int key) {
		return this.keys[key];
	}
	
	public boolean getMouse(final int button) {
		return this.mouseButtons[button];
	}
	
	public int getAndResetMouseMovementX() {
		return this.mouseMovementX.getAndSet(0);
	}
	
	public int getAndResetMouseMovementY() {
		return this.mouseMovementY.getAndSet(0);
	}
	
	public int getMouseX() {
		return this.mouseX;
	}
	
	public int getMouseY() {
		return this.mouseY;
	}
	
	@Override
	public void focusGained(final FocusEvent e) {
//		Do nothing.
	}
	
	@Override
	public void focusLost(final FocusEvent e) {
		for(int i = 0; i < this.keys.length; i++) {
			this.keys[i] = false;
		}
		
		for(int i = 0; i < this.mouseButtons.length; i++) {
			this.mouseButtons[i] = false;
		}
	}
	
	@Override
	public void keyPressed(final KeyEvent e) {
		final int code = e.getKeyCode();
		
		if(code > 0 && code < this.keys.length) {
			this.keys[code] = true;
		}
	}
	
	@Override
	public void keyReleased(final KeyEvent e) {
		final int code = e.getKeyCode();
		
		if(code > 0 && code < this.keys.length) {
			this.keys[code] = false;
		}
	}
	
	@Override
	public void keyTyped(final KeyEvent e) {
//		Do nothing.
	}
	
	@Override
	public void mouseClicked(final MouseEvent e) {
//		Do nothing.
	}
	
	@Override
	public void mouseDragged(final MouseEvent e) {
		doMouseMoved(e);
	}
	
	@Override
	public void mouseEntered(final MouseEvent e) {
//		Do nothing.
	}
	
	@Override
	public void mouseExited(final MouseEvent e) {
//		Do nothing.
	}
	
	@Override
	public void mouseMoved(final MouseEvent e) {
		doMouseMoved(e);
	}
	
	@Override
	public void mousePressed(final MouseEvent e) {
		final int code = e.getButton();
		
		if(code > 0 && code < this.mouseButtons.length) {
			this.mouseButtons[code] = true;
		}
	}
	
	@Override
	public void mouseReleased(final MouseEvent e) {
		final int code = e.getButton();
		
		if(code > 0 && code < this.mouseButtons.length) {
			this.mouseButtons[code] = false;
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private void doMouseMoved(final MouseEvent e) {
		if(this.jFrame.isActive()) {
			if(this.isRecenteringMouse && this.centerPoint.x == e.getXOnScreen() && this.centerPoint.y == e.getYOnScreen()) {
				this.isRecenteringMouse = false;
			} else {
				final int x = e.getXOnScreen();
				final int y = e.getYOnScreen();
				final int deltaX = x - this.centerPoint.x;
				final int deltaY = y - this.centerPoint.y;
				
				doRecenterMouse();
				
				this.mouseMovementX.addAndGet(deltaX);
				this.mouseMovementY.addAndGet(deltaY);
			}
		}
	}
	
	private synchronized void doRecenterMouse() {
		if(this.jFrame.isActive()) {
			this.centerPoint.x = this.jFrame.getWidth() / 2;
			this.centerPoint.y = this.jFrame.getHeight() / 2;
			
			SwingUtilities.convertPointToScreen(this.centerPoint, this.jFrame);
			
			this.isRecenteringMouse = true;
			
			this.mouseX = this.centerPoint.x;
			this.mouseY = this.centerPoint.y;
			
			this.robot.mouseMove(this.mouseX, this.mouseY);
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static Robot doCreateRobot() {
		try {
			return new Robot();
		} catch(final AWTException e) {
			return null;
		}
	}
}