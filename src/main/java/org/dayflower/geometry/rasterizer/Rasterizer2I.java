/**
 * Copyright 2014 - 2021 J&#246;rgen Lundgren
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
package org.dayflower.geometry.rasterizer;

import static org.dayflower.utility.Floats.toFloat;
import static org.dayflower.utility.Ints.abs;
import static org.dayflower.utility.Ints.toInt;

import org.dayflower.geometry.Point2I;
import org.dayflower.geometry.shape.Line2I;
import org.dayflower.geometry.shape.Rectangle2I;
import org.dayflower.geometry.shape.Triangle2I;

/**
 * {@code Rasterizer2I} performs rasterization on {@link Line2I} and {@link Triangle2I} instances.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class Rasterizer2I {
	private Rasterizer2I() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Clips {@code line} against {@code rectangle} and rasterizes the result.
	 * <p>
	 * Returns a {@code Point2I[]} that represents a scanline.
	 * <p>
	 * If either {@code line} or {@code rectangle} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param line the {@link Line2I} to rasterize
	 * @param rectangle the {@link Rectangle2I} to clip against
	 * @return a {@code Point2I[]} that represents a scanline
	 * @throws NullPointerException thrown if, and only if, either {@code line} or {@code rectangle} are {@code null}
	 */
	public static Point2I[] rasterize(final Line2I line, final Rectangle2I rectangle) {
		final int lAX = line.getA().getX();
		final int lAY = line.getA().getY();
		final int lBX = line.getB().getX();
		final int lBY = line.getB().getY();
		
		final int rAX = rectangle.getA().getX();
		final int rAY = rectangle.getA().getY();
		final int rCX = rectangle.getC().getX();
		final int rCY = rectangle.getC().getY();
		
		final int clippedAX = lAX < rAX ? rAX : lAX >= rCX ? rCX - 1 : lAX;
		final int clippedAY = lAY < rAY ? rAY : lAY >= rCY ? rCY - 1 : lAY;
		final int clippedBX = lBX < rAX ? rAX : lBX >= rCX ? rCX - 1 : lBX;
		final int clippedBY = lBY < rAY ? rAY : lBY >= rCY ? rCY - 1 : lBY;
		
		final int w = clippedBX - clippedAX;
		final int h = clippedBY - clippedAY;
		
		final int wAbs = abs(w);
		final int hAbs = abs(h);
		
		final int dAX = w < 0 ? -1 : w > 0 ? 1 : 0;
		final int dAY = h < 0 ? -1 : h > 0 ? 1 : 0;
		final int dBX = wAbs > hAbs ? dAX : 0;
		final int dBY = wAbs > hAbs ? 0 : dAY;
		
		final int l = wAbs > hAbs ? wAbs : hAbs;
		final int s = wAbs > hAbs ? hAbs : wAbs;
		
		final Point2I[] scanLine = new Point2I[l + 1];
		
		int n = l >> 1;
		
		int x = clippedAX;
		int y = clippedAY;
		
		for(int i = 0; i <= l; i++) {
			scanLine[i] = new Point2I(x, y);
			
			n += s;
			
			if(n >= l) {
				n -= l;
				
				x += dAX;
				y += dAY;
			} else {
				x += dBX;
				y += dBY;
			}
		}
		
		return scanLine;
	}
	
	/**
	 * Clips {@code triangle} against {@code rectangle} and rasterizes the result.
	 * <p>
	 * Returns a {@code Point2I[][]} that represents scanlines.
	 * <p>
	 * If either {@code triangle} or {@code rectangle} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param triangle the {@link Triangle2I} to rasterize
	 * @param rectangle the {@link Rectangle2I} to clip against
	 * @return a {@code Point2I[][]} that represents scanlines
	 * @throws NullPointerException thrown if, and only if, either {@code triangle} or {@code rectangle} are {@code null}
	 */
	public static Point2I[][] rasterize(final Triangle2I triangle, final Rectangle2I rectangle) {
		final Point2I[] vertices = new Point2I[] {triangle.getA(), triangle.getB(), triangle.getC()};
		
		doSortVerticesAscendingByY(vertices);
		
		final Point2I vertexA = vertices[0];
		final Point2I vertexB = vertices[1];
		final Point2I vertexC = vertices[2];
		
		if(vertexB.getY() == vertexC.getY()) {
			return doRasterizeTriangleTopDown(vertexA, vertexB, vertexC, rectangle);
		} else if(vertexA.getY() == vertexB.getY()) {
			return doRasterizeTriangleBottomUp(vertexA, vertexB, vertexC, rectangle);
		} else {
			final Point2I vertexD = new Point2I(toInt(vertexA.getX() + (toFloat(vertexB.getY() - vertexA.getY()) / (vertexC.getY() - vertexA.getY())) * (vertexC.getX() - vertexA.getX())), vertexB.getY());
			
			final Point2I[][] scanLinesTopDown = doRasterizeTriangleTopDown(vertexA, vertexB, vertexD, rectangle);
			final Point2I[][] scanLinesBottomUp = doRasterizeTriangleBottomUp(vertexB, vertexD, vertexC, rectangle);
			final Point2I[][] scanLines = new Point2I[scanLinesTopDown.length + scanLinesBottomUp.length][];
			
			for(int i = 0; i < scanLinesTopDown.length; i++) {
				scanLines[i] = scanLinesTopDown[i];
			}
			
			for(int i = scanLinesTopDown.length, j = 0; i < scanLines.length && j < scanLinesBottomUp.length; i++, j++) {
				scanLines[i] = scanLinesBottomUp[j];
			}
			
			return scanLines;
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static Point2I[][] doRasterizeTriangleBottomUp(final Point2I vertexA, final Point2I vertexB, final Point2I vertexC, final Rectangle2I rectangle) {
		final float slope0 = toFloat(vertexC.getX() - vertexA.getX()) / (vertexC.getY() - vertexA.getY());
		final float slope1 = toFloat(vertexC.getX() - vertexB.getX()) / (vertexC.getY() - vertexB.getY());
		
		float x0 = vertexC.getX();
		float x1 = vertexC.getX() + 0.5F;
		
		final int yStart = vertexC.getY();
		final int yEnd = vertexA.getY();
		final int yLength = yStart - yEnd;
		
		final Point2I[][] scanLines = new Point2I[yLength][];
		
		for(int i = 0, y = yStart; y > yEnd; i++, y--) {
			final Point2I a = new Point2I(toInt(x0), y);
			final Point2I b = new Point2I(toInt(x1), y);
			
			final Line2I line = new Line2I(a, b);
			
			scanLines[i] = rasterize(line, rectangle);
			
			x0 -= slope0;
			x1 -= slope1;
		}
		
		return scanLines;
	}
	
	private static Point2I[][] doRasterizeTriangleTopDown(final Point2I vertexA, final Point2I vertexB, final Point2I vertexC, final Rectangle2I rectangle) {
		final float slope0 = toFloat(vertexB.getX() - vertexA.getX()) / (vertexB.getY() - vertexA.getY());
		final float slope1 = toFloat(vertexC.getX() - vertexA.getX()) / (vertexC.getY() - vertexA.getY());
		
		float x0 = vertexA.getX();
		float x1 = vertexA.getX() + 0.5F;
		
		final int yStart = vertexA.getY();
		final int yEnd = vertexB.getY();
		final int yLength = yEnd - yStart + 1;
		
		final Point2I[][] scanLines = new Point2I[yLength][];
		
		for(int i = 0, y = yStart; y <= yEnd; i++, y++) {
			final Point2I a = new Point2I(toInt(x0), y);
			final Point2I b = new Point2I(toInt(x1), y);
			
			final Line2I line = new Line2I(a, b);
			
			scanLines[i] = rasterize(line, rectangle);
			
			x0 += slope0;
			x1 += slope1;
		}
		
		return scanLines;
	}
	
	private static void doSortVerticesAscendingByY(final Point2I[] vertices) {
		if(vertices[0].getY() > vertices[1].getY()) {
			final Point2I a = vertices[0];
			final Point2I b = vertices[1];
			
			vertices[0] = b;
			vertices[1] = a;
		}
		
		if(vertices[0].getY() > vertices[2].getY()) {
			final Point2I a = vertices[0];
			final Point2I c = vertices[2];
			
			vertices[0] = c;
			vertices[2] = a;
		}
		
		if(vertices[1].getY() > vertices[2].getY()) {
			final Point2I b = vertices[1];
			final Point2I c = vertices[2];
			
			vertices[1] = c;
			vertices[2] = b;
		}
	}
}