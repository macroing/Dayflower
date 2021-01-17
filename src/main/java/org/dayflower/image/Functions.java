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
package org.dayflower.image;

import static org.dayflower.util.Floats.max;
import static org.dayflower.util.Floats.min;
import static org.dayflower.util.Floats.simplexFractionalBrownianMotionXY;

import java.util.Objects;
import java.util.function.Function;

import org.dayflower.geometry.Point2F;

/**
 * A class that consists exclusively of static methods that returns {@code Function} instances to be used with a {@link PixelImageF} instance.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class Functions {
	private Functions() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a new {@code Function} instance that returns a constant {@link Color3F} instance.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color the constant {@code Color3F} to return
	 * @return a new {@code Function} instance that returns a constant {@code Color3F} instance
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Function<PixelF, Color3F> constant(final Color3F color) {
		Objects.requireNonNull(color, "color == null");
		
		return pixel -> color;
	}
	
	/**
	 * Returns a new {@code Function} instance that returns {@code color} multiplied by Simplex fractional Brownian motion (fBm) noise.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Functions.simplexFractionalBrownianMotion(color, new Point2F(800.0F, 800.0F));
	 * }
	 * </pre>
	 * 
	 * @param color a {@link Color3F} instance
	 * @return a new {@code Function} instance that returns {@code color} multiplied by Simplex fractional Brownian motion (fBm) noise
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Function<PixelF, Color3F> simplexFractionalBrownianMotion(final Color3F color) {
		return simplexFractionalBrownianMotion(color, new Point2F(800.0F, 800.0F));
	}
	
	/**
	 * Returns a new {@code Function} instance that returns {@code color} multiplied by Simplex fractional Brownian motion (fBm) noise.
	 * <p>
	 * If either {@code color} or {@code boundaryA} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Functions.simplexFractionalBrownianMotion(color, boundaryA, new Point2F());
	 * }
	 * </pre>
	 * 
	 * @param color a {@link Color3F} instance
	 * @param boundaryA a {@link Point2F} instance with the minimum or maximum boundary
	 * @return a new {@code Function} instance that returns {@code color} multiplied by Simplex fractional Brownian motion (fBm) noise
	 * @throws NullPointerException thrown if, and only if, either {@code color} or {@code boundaryA} are {@code null}
	 */
	public static Function<PixelF, Color3F> simplexFractionalBrownianMotion(final Color3F color, final Point2F boundaryA) {
		return simplexFractionalBrownianMotion(color, boundaryA, new Point2F());
	}
	
	/**
	 * Returns a new {@code Function} instance that returns {@code color} multiplied by Simplex fractional Brownian motion (fBm) noise.
	 * <p>
	 * If either {@code color}, {@code boundaryA} or {@code boundaryB} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Functions.simplexFractionalBrownianMotion(color, boundaryA, boundaryB, 5.0F, 0.5F, 16);
	 * }
	 * </pre>
	 * 
	 * @param color a {@link Color3F} instance
	 * @param boundaryA a {@link Point2F} instance with the minimum or maximum boundary
	 * @param boundaryB a {@code Point2F} instance with the maximum or minimum boundary
	 * @return a new {@code Function} instance that returns {@code color} multiplied by Simplex fractional Brownian motion (fBm) noise
	 * @throws NullPointerException thrown if, and only if, either {@code color}, {@code boundaryA} or {@code boundaryB} are {@code null}
	 */
	public static Function<PixelF, Color3F> simplexFractionalBrownianMotion(final Color3F color, final Point2F boundaryA, final Point2F boundaryB) {
		return simplexFractionalBrownianMotion(color, boundaryA, boundaryB, 5.0F, 0.5F, 16);
	}
	
	/**
	 * Returns a new {@code Function} instance that returns {@code color} multiplied by Simplex fractional Brownian motion (fBm) noise.
	 * <p>
	 * If either {@code color}, {@code boundaryA} or {@code boundaryB} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@link Color3F} instance
	 * @param boundaryA a {@link Point2F} instance with the minimum or maximum boundary
	 * @param boundaryB a {@code Point2F} instance with the maximum or minimum boundary
	 * @param frequency the frequency to use
	 * @param gain the gain to use
	 * @param octaves the octaves to use
	 * @return a new {@code Function} instance that returns {@code color} multiplied by Simplex fractional Brownian motion (fBm) noise
	 * @throws NullPointerException thrown if, and only if, either {@code color}, {@code boundaryA} or {@code boundaryB} are {@code null}
	 */
	public static Function<PixelF, Color3F> simplexFractionalBrownianMotion(final Color3F color, final Point2F boundaryA, final Point2F boundaryB, final float frequency, final float gain, final int octaves) {
		Objects.requireNonNull(color, "color == null");
		Objects.requireNonNull(boundaryA, "boundaryA == null");
		Objects.requireNonNull(boundaryB, "boundaryB == null");
		
		final float minimumX = min(boundaryA.getX(), boundaryB.getX());
		final float minimumY = min(boundaryA.getY(), boundaryB.getY());
		final float maximumX = max(boundaryA.getX(), boundaryB.getX());
		final float maximumY = max(boundaryA.getY(), boundaryB.getY());
		
		return pixel -> {
			final float noiseX = (pixel.getX() - minimumX) / (maximumX - minimumX);
			final float noiseY = (pixel.getY() - minimumY) / (maximumY - minimumY);
			
			final float noise = simplexFractionalBrownianMotionXY(noiseX, noiseY, frequency, gain, 0.0F, 1.0F, octaves);
			
			return Color3F.maximumTo1(Color3F.minimumTo0(Color3F.multiply(color, noise)));
		};
	}
}