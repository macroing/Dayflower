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

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public final class Input implements KeyListener, FocusListener, MouseListener, MouseMotionListener {
	private boolean isDraggingMouse;
	private final boolean[] keys;
	private final boolean[] mouseButtons;
	private int mouseX;
	private int mouseXPressed;
	private int mouseY;
	private int mouseYPressed;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public Input() {
		this.keys = new boolean[65536];
		this.mouseButtons = new boolean[4];
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public boolean isDraggingMouse() {
		return this.isDraggingMouse;
	}
	
	public boolean isKeyPressed(final int key) {
		return this.keys[key];
	}
	
	public boolean isMouseButtonPressed() {
		return isMouseButtonPressed(0) || isMouseButtonPressed(1) || isMouseButtonPressed(2) || isMouseButtonPressed(3);
	}
	
	public boolean isMouseButtonPressed(final int button) {
		return this.mouseButtons[button];
	}
	
	public int getMouseX() {
		return this.mouseX;
	}
	
	public int getMouseXPressed() {
		return this.mouseXPressed;
	}
	
	public int getMouseY() {
		return this.mouseY;
	}
	
	public int getMouseYPressed() {
		return this.mouseYPressed;
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
		this.isDraggingMouse = true;
		
		this.mouseX = e.getX();
		this.mouseY = e.getY();
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
		this.isDraggingMouse = false;
		
		this.mouseX = e.getX();
		this.mouseY = e.getY();
	}
	
	@Override
	public void mousePressed(final MouseEvent e) {
		final int code = e.getButton();
		
		if(code > 0 && code < this.mouseButtons.length) {
			this.mouseButtons[code] = true;
			
			this.mouseXPressed = e.getX();
			this.mouseYPressed = e.getY();
		}
	}
	
	@Override
	public void mouseReleased(final MouseEvent e) {
		final int code = e.getButton();
		
		if(code > 0 && code < this.mouseButtons.length) {
			this.mouseButtons[code] = false;
		}
	}
}