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

import java.awt.Canvas;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.image.BufferedImage;
import java.awt.image.BufferStrategy;
import java.awt.image.DataBufferByte;

import javax.swing.JFrame;

public final class Display extends Canvas {
	private static final int HEIGHT_SCALE = 2;
	private static final int WIDTH_SCALE = 2;
	private static final long serialVersionUID = 1L;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final BufferedImage bufferedImage;
	private final BufferStrategy bufferStrategy;
	private final byte[] components;
	private final Cursor cursor;
	private final Graphics graphics;
	private final GraphicsContext graphicsContext;
	private final Input input;
	private final JFrame jFrame;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public Display(final String title) {
		this.cursor = Toolkit.getDefaultToolkit().createCustomCursor(Toolkit.getDefaultToolkit().getImage(""), new Point(0, 0), "invisible");
		
		this.jFrame = new JFrame();
		
		final Rectangle bounds = getMaximumBoundsFor(this.jFrame, true);
		
		final Dimension size = new Dimension(bounds.width, bounds.height);
		
		setPreferredSize(size);
		setMinimumSize(size);
		setMaximumSize(size);
		
		this.bufferedImage = new BufferedImage(bounds.width / WIDTH_SCALE, bounds.height / HEIGHT_SCALE, BufferedImage.TYPE_3BYTE_BGR);
		this.components = DataBufferByte.class.cast(this.bufferedImage.getRaster().getDataBuffer()).getData();
		
		this.graphicsContext = new GraphicsContext(bounds.width / WIDTH_SCALE, bounds.height / HEIGHT_SCALE);
		
		this.jFrame.add(this);
		this.jFrame.pack();
		this.jFrame.setCursor(this.cursor);
		this.jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.jFrame.setLocationRelativeTo(null);
		this.jFrame.setResizable(false);
		this.jFrame.setSize(bounds.width, bounds.height);
		this.jFrame.setTitle(title);
		this.jFrame.setVisible(true);
		
		createBufferStrategy(1);
		
		this.bufferStrategy = getBufferStrategy();
		this.graphics = this.bufferStrategy.getDrawGraphics();
		
		this.input = new Input(this.jFrame);
		
		addFocusListener(this.input);
		addKeyListener(this.input);
		addMouseListener(this.input);
		addMouseMotionListener(this.input);
		setFocusable(true);
		requestFocus();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public GraphicsContext getGraphicsContext() {
		return this.graphicsContext;
	}
	
	public Input getInput() {
		return this.input;
	}
	
	public void setTitle(final String title) {
		this.jFrame.setTitle(title);
	}
	
	public void swapBuffers() {
		this.graphicsContext.copyToByteArray(this.components);
		
		this.graphics.drawImage(this.bufferedImage, 0, 0, this.graphicsContext.getWidth() * WIDTH_SCALE, this.graphicsContext.getHeight() * HEIGHT_SCALE, null);
		
		this.bufferStrategy.show();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static GraphicsConfiguration getGraphicsConfiguration(final Window window) {
		GraphicsConfiguration graphicsConfiguration = null;
		
		if(window != null) {
			graphicsConfiguration = window.getGraphicsConfiguration();
		} else {
			final GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
			
			final GraphicsDevice graphicsDevice = graphicsEnvironment.getDefaultScreenDevice();
			
			graphicsConfiguration = graphicsDevice.getDefaultConfiguration();
		}
		
		return graphicsConfiguration;
	}
	
	public static Insets getInsets(final Window window) {
		final GraphicsConfiguration graphicsConfiguration = getGraphicsConfiguration(window);
		
		final Toolkit toolkit = Toolkit.getDefaultToolkit();
		
		final Insets insets = toolkit.getScreenInsets(graphicsConfiguration);
		
		return insets;
	}
	
	public static Rectangle getMaximumBoundsFor(final Window window, final boolean isAccountingForInsets) {
		final GraphicsConfiguration graphicsConfiguration = getGraphicsConfiguration(window);
		
		final Insets insets = getInsets(window);
		
		final int bottom = isAccountingForInsets ? insets.bottom : 0;
		final int left = isAccountingForInsets ? insets.left : 0;
		final int right = isAccountingForInsets ? insets.right : 0;
		final int top = isAccountingForInsets ? insets.top : 0;
		
		final
		Rectangle rectangle = graphicsConfiguration.getBounds();
		rectangle.x += left;
		rectangle.y += top;
		rectangle.width -= left + right;
		rectangle.height -= top + bottom;
		
		return rectangle;
	}
}