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
package org.dayflower.geometry;

import static org.dayflower.utility.Floats.PI_MULTIPLIED_BY_2_RECIPROCAL;
import static org.dayflower.utility.Floats.PI_RECIPROCAL;
import static org.dayflower.utility.Floats.cos;
import static org.dayflower.utility.Floats.equal;
import static org.dayflower.utility.Floats.max;
import static org.dayflower.utility.Floats.min;
import static org.dayflower.utility.Floats.positiveModulo;
import static org.dayflower.utility.Floats.sin;

import java.util.Objects;

import org.dayflower.node.Node;
import org.dayflower.utility.Floats;
import org.dayflower.utility.MortonCodes;

/**
 * A {@code Point2F} denotes a 2-dimensional point with two coordinates, of type {@code float}.
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class Point2F implements Node {
	private final float component1;
	private final float component2;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code Point2F} instance given the component values {@code 0.0F} and {@code 0.0F}.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Point2F(0.0F, 0.0F);
	 * }
	 * </pre>
	 */
	public Point2F() {
		this(0.0F, 0.0F);
	}
	
	/**
	 * Constructs a new {@code Point2F} instance given the component values {@code vector.getComponent1()} and {@code vector.getComponent2()}.
	 * <p>
	 * If {@code vector} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Point2F(vector.getComponent1(), vector.getComponent2());
	 * }
	 * </pre>
	 * 
	 * @param vector a {@link Vector2F} instance
	 * @throws NullPointerException thrown if, and only if, {@code vector} is {@code null}
	 */
	public Point2F(final Vector2F vector) {
		this(vector.getComponent1(), vector.getComponent2());
	}
	
	/**
	 * Constructs a new {@code Point2F} instance given the component values {@code component1} and {@code component2}.
	 * 
	 * @param component1 the value of component 1
	 * @param component2 the value of component 2
	 */
	public Point2F(final float component1, final float component2) {
		this.component1 = component1;
		this.component2 = component2;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code String} representation of this {@code Point2F} instance.
	 * 
	 * @return a {@code String} representation of this {@code Point2F} instance
	 */
	@Override
	public String toString() {
		return String.format("new Point2F(%+.10f, %+.10f)", Float.valueOf(this.component1), Float.valueOf(this.component2));
	}
	
	/**
	 * Compares {@code object} to this {@code Point2F} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code Point2F}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code Point2F} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code Point2F}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof Point2F)) {
			return false;
		} else if(!equal(this.component1, Point2F.class.cast(object).component1)) {
			return false;
		} else if(!equal(this.component2, Point2F.class.cast(object).component2)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns the value of component 1.
	 * 
	 * @return the value of component 1
	 */
	public float getComponent1() {
		return this.component1;
	}
	
	/**
	 * Returns the value of component 2.
	 * 
	 * @return the value of component 2
	 */
	public float getComponent2() {
		return this.component2;
	}
	
	/**
	 * Returns the value of the U-component.
	 * 
	 * @return the value of the U-component
	 */
	public float getU() {
		return this.component1;
	}
	
	/**
	 * Returns the value of the V-component.
	 * 
	 * @return the value of the V-component
	 */
	public float getV() {
		return this.component2;
	}
	
	/**
	 * Returns the value of the X-component.
	 * 
	 * @return the value of the X-component
	 */
	public float getX() {
		return this.component1;
	}
	
	/**
	 * Returns the value of the Y-component.
	 * 
	 * @return the value of the Y-component
	 */
	public float getY() {
		return this.component2;
	}
	
	/**
	 * Returns a {@code float[]} representation of this {@code Point2F} instance.
	 * 
	 * @return a {@code float[]} representation of this {@code Point2F} instance
	 */
	public float[] toArray() {
		return new float[] {
			this.component1,
			this.component2
		};
	}
	
	/**
	 * Returns a hash code for this {@code Point2F} instance.
	 * 
	 * @return a hash code for this {@code Point2F} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(Float.valueOf(this.component1), Float.valueOf(this.component2));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Adds the component values of {@code vectorRHS} to the component values of {@code pointLHS}.
	 * <p>
	 * Returns a new {@code Point2F} instance with the result of the addition.
	 * <p>
	 * If either {@code pointLHS} or {@code vectorRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param pointLHS the {@code Point2F} instance on the left-hand side
	 * @param vectorRHS the {@link Vector2F} instance on the right-hand side
	 * @return a new {@code Point2F} instance with the result of the addition
	 * @throws NullPointerException thrown if, and only if, either {@code pointLHS} or {@code vectorRHS} are {@code null}
	 */
	public static Point2F add(final Point2F pointLHS, final Vector2F vectorRHS) {
		final float component1 = pointLHS.component1 + vectorRHS.getComponent1();
		final float component2 = pointLHS.component2 + vectorRHS.getComponent2();
		
		return new Point2F(component1, component2);
	}
	
	/**
	 * Computes texture coordinates from three other texture coordinates via Barycentric interpolation.
	 * <p>
	 * Returns a new {@code Point2F} instance with the interpolated texture coordinates.
	 * <p>
	 * If either {@code textureCoordinatesA}, {@code textureCoordinatesB}, {@code textureCoordinatesC} or {@code barycentricCoordinates} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param textureCoordinatesA a {@code Point2F} instance denoting the texture coordinates of vertex {@code A} of a triangle
	 * @param textureCoordinatesB a {@code Point2F} instance denoting the texture coordinates of vertex {@code B} of a triangle
	 * @param textureCoordinatesC a {@code Point2F} instance denoting the texture coordinates of vertex {@code C} of a triangle
	 * @param barycentricCoordinates a {@link Point3F} instance denoting the Barycentric coordinates
	 * @return a new {@code Point2F} instance with the interpolated texture coordinates
	 * @throws NullPointerException thrown if, and only if, either {@code textureCoordinatesA}, {@code textureCoordinatesB}, {@code textureCoordinatesC} or {@code barycentricCoordinates} are {@code null}
	 */
	public static Point2F createTextureCoordinates(final Point2F textureCoordinatesA, final Point2F textureCoordinatesB, final Point2F textureCoordinatesC, final Point3F barycentricCoordinates) {
		final float u = textureCoordinatesA.getU() * barycentricCoordinates.getU() + textureCoordinatesB.getU() * barycentricCoordinates.getV() + textureCoordinatesC.getU() * barycentricCoordinates.getW();
		final float v = textureCoordinatesA.getV() * barycentricCoordinates.getU() + textureCoordinatesB.getV() * barycentricCoordinates.getV() + textureCoordinatesC.getV() * barycentricCoordinates.getW();
		
		return new Point2F(u, v);
	}
	
	/**
	 * Returns a new {@code Point2F} instance based on the decoded coordinates of the Morton code {@code mortonCode}.
	 * 
	 * @param mortonCode the Morton code to decode
	 * @return a new {@code Point2F} instance based on the decoded coordinates of the Morton code {@code mortonCode}
	 */
	public static Point2F decodeMortonCode1By1(final float mortonCode) {
		return decodeMortonCode1By1((int)(mortonCode * (1L << 32L)));
	}
	
	/**
	 * Returns a new {@code Point2F} instance based on the decoded coordinates of the Morton code {@code mortonCode}.
	 * 
	 * @param mortonCode the Morton code to decode
	 * @return a new {@code Point2F} instance based on the decoded coordinates of the Morton code {@code mortonCode}
	 */
	public static Point2F decodeMortonCode1By1(final int mortonCode) {
		final int mortonCodeX = MortonCodes.decode1By1X(mortonCode);
		final int mortonCodeY = MortonCodes.decode1By1Y(mortonCode);
		
		final float x = mortonCodeX / (float)(1 << 16);
		final float y = mortonCodeY / (float)(1 << 16);
		
		return new Point2F(x, y);
	}
	
	/**
	 * Performs a linear interpolation operation on the supplied values.
	 * <p>
	 * Returns a {@code Point2F} instance with the result of the linear interpolation operation.
	 * <p>
	 * If either {@code a} or {@code b} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param a a {@code Point2F} instance
	 * @param b a {@code Point2F} instance
	 * @param t the factor
	 * @return a {@code Point2F} instance with the result of the linear interpolation operation
	 * @throws NullPointerException thrown if, and only if, either {@code a} or {@code b} are {@code null}
	 */
	public static Point2F lerp(final Point2F a, final Point2F b, final float t) {
		final float component1 = Floats.lerp(a.component1, b.component1, t);
		final float component2 = Floats.lerp(a.component2, b.component2, t);
		
		return new Point2F(component1, component2);
	}
	
	/**
	 * Returns a new {@code Point2F} instance with the largest component values.
	 * 
	 * @return a new {@code Point2F} instance with the largest component values
	 */
	public static Point2F maximum() {
		final float component1 = Float.MAX_VALUE;
		final float component2 = Float.MAX_VALUE;
		
		return new Point2F(component1, component2);
	}
	
	/**
	 * Returns a new {@code Point2F} instance with the largest component values of {@code a} and {@code b}.
	 * <p>
	 * If either {@code a} or {@code b} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param a a {@code Point2F} instance
	 * @param b a {@code Point2F} instance
	 * @return a new {@code Point2F} instance with the largest component values of {@code a} and {@code b}
	 * @throws NullPointerException thrown if, and only if, either {@code a} or {@code b} are {@code null}
	 */
	public static Point2F maximum(final Point2F a, final Point2F b) {
		final float component1 = max(a.component1, b.component1);
		final float component2 = max(a.component2, b.component2);
		
		return new Point2F(component1, component2);
	}
	
	/**
	 * Returns a new {@code Point2F} instance with the largest component values of {@code a}, {@code b} and {@code c}.
	 * <p>
	 * If either {@code a}, {@code b} or {@code c} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param a a {@code Point2F} instance
	 * @param b a {@code Point2F} instance
	 * @param c a {@code Point2F} instance
	 * @return a new {@code Point2F} instance with the largest component values of {@code a}, {@code b} and {@code c}
	 * @throws NullPointerException thrown if, and only if, either {@code a}, {@code b} or {@code c} are {@code null}
	 */
	public static Point2F maximum(final Point2F a, final Point2F b, final Point2F c) {
		final float component1 = max(a.component1, b.component1, c.component1);
		final float component2 = max(a.component2, b.component2, c.component2);
		
		return new Point2F(component1, component2);
	}
	
	/**
	 * Returns a new {@code Point2F} instance with the largest component values of {@code a}, {@code b}, {@code c} and {@code d}.
	 * <p>
	 * If either {@code a}, {@code b}, {@code c} or {@code d} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param a a {@code Point2F} instance
	 * @param b a {@code Point2F} instance
	 * @param c a {@code Point2F} instance
	 * @param d a {@code Point2F} instance
	 * @return a new {@code Point2F} instance with the largest component values of {@code a}, {@code b}, {@code c} and {@code d}
	 * @throws NullPointerException thrown if, and only if, either {@code a}, {@code b}, {@code c} or {@code d} are {@code null}
	 */
	public static Point2F maximum(final Point2F a, final Point2F b, final Point2F c, final Point2F d) {
		final float component1 = max(a.component1, b.component1, c.component1, d.component1);
		final float component2 = max(a.component2, b.component2, c.component2, d.component2);
		
		return new Point2F(component1, component2);
	}
	
	/**
	 * Returns a new {@code Point2F} instance with the smallest component values.
	 * 
	 * @return a new {@code Point2F} instance with the smallest component values
	 */
	public static Point2F minimum() {
		final float component1 = Float.MIN_VALUE;
		final float component2 = Float.MIN_VALUE;
		
		return new Point2F(component1, component2);
	}
	
	/**
	 * Returns a new {@code Point2F} instance with the smallest component values of {@code a} and {@code b}.
	 * <p>
	 * If either {@code a} or {@code b} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param a a {@code Point2F} instance
	 * @param b a {@code Point2F} instance
	 * @return a new {@code Point2F} instance with the smallest component values of {@code a} and {@code b}
	 * @throws NullPointerException thrown if, and only if, either {@code a} or {@code b} are {@code null}
	 */
	public static Point2F minimum(final Point2F a, final Point2F b) {
		final float component1 = min(a.component1, b.component1);
		final float component2 = min(a.component2, b.component2);
		
		return new Point2F(component1, component2);
	}
	
	/**
	 * Returns a new {@code Point2F} instance with the smallest component values of {@code a}, {@code b} and {@code c}.
	 * <p>
	 * If either {@code a}, {@code b} or {@code c} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param a a {@code Point2F} instance
	 * @param b a {@code Point2F} instance
	 * @param c a {@code Point2F} instance
	 * @return a new {@code Point2F} instance with the smallest component values of {@code a}, {@code b} and {@code c}
	 * @throws NullPointerException thrown if, and only if, either {@code a}, {@code b} or {@code c} are {@code null}
	 */
	public static Point2F minimum(final Point2F a, final Point2F b, final Point2F c) {
		final float component1 = min(a.component1, b.component1, c.component1);
		final float component2 = min(a.component2, b.component2, c.component2);
		
		return new Point2F(component1, component2);
	}
	
	/**
	 * Returns a new {@code Point2F} instance with the smallest component values of {@code a}, {@code b}, {@code c} and {@code d}.
	 * <p>
	 * If either {@code a}, {@code b}, {@code c} or {@code d} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param a a {@code Point2F} instance
	 * @param b a {@code Point2F} instance
	 * @param c a {@code Point2F} instance
	 * @param d a {@code Point2F} instance
	 * @return a new {@code Point2F} instance with the smallest component values of {@code a}, {@code b}, {@code c} and {@code d}
	 * @throws NullPointerException thrown if, and only if, either {@code a}, {@code b}, {@code c} or {@code d} are {@code null}
	 */
	public static Point2F minimum(final Point2F a, final Point2F b, final Point2F c, final Point2F d) {
		final float component1 = min(a.component1, b.component1, c.component1, d.component1);
		final float component2 = min(a.component2, b.component2, c.component2, d.component2);
		
		return new Point2F(component1, component2);
	}
	
	/**
	 * Rotates {@code point} using {@code angle}.
	 * <p>
	 * Returns a new {@code Point2F} instance with the rotation applied.
	 * <p>
	 * If either {@code point} or {@code angle} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param point a {@code Point2F} instance
	 * @param angle an {@link AngleF} instance
	 * @return a new {@code Point2F} instance with the rotation applied
	 * @throws NullPointerException thrown if, and only if, either {@code point} or {@code angle} are {@code null}
	 */
	public static Point2F rotate(final Point2F point, final AngleF angle) {
		final float angleRadians = angle.getRadians();
		final float angleRadiansCos = cos(angleRadians);
		final float angleRadiansSin = sin(angleRadians);
		
		final float component1 = point.component1 * angleRadiansCos - point.component2 * angleRadiansSin;
		final float component2 = point.component2 * angleRadiansCos + point.component1 * angleRadiansSin;
		
		return new Point2F(component1, component2);
	}
	
	/**
	 * Scales {@code point} using {@code scale}.
	 * <p>
	 * Returns a new {@code Point2F} instance with the scale applied.
	 * <p>
	 * If either {@code point} or {@code scale} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param point a {@code Point2F} instance
	 * @param scale a {@link Vector2F} instance
	 * @return a new {@code Point2F} instance with the scale applied
	 * @throws NullPointerException thrown if, and only if, either {@code point} or {@code scale} are {@code null}
	 */
	public static Point2F scale(final Point2F point, final Vector2F scale) {
		final float component1 = point.component1 * scale.getComponent1();
		final float component2 = point.component2 * scale.getComponent2();
		
		return new Point2F(component1, component2);
	}
	
	/**
	 * Returns a new {@code Point2F} instance with the spherical coordinates of {@code direction} as its component values.
	 * <p>
	 * If {@code direction} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param direction a {@link Vector3F} instance
	 * @return a new {@code Point2F} instance with the spherical coordinates of {@code direction} as its component values
	 * @throws NullPointerException thrown if, and only if, {@code direction} is {@code null}
	 */
	public static Point2F sphericalCoordinates(final Vector3F direction) {
		return new Point2F(direction.sphericalPhi() * PI_MULTIPLIED_BY_2_RECIPROCAL, direction.sphericalTheta() * PI_RECIPROCAL);
	}
	
	/**
	 * Returns a new {@code Point2F} instance with {@code point} inside the image represented by {@code resolutionX} and {@code resolutionY}.
	 * <p>
	 * If {@code point} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param point a {@code Point2F} instance
	 * @param resolutionX the resolution of the X-axis
	 * @param resolutionY the resolution of the Y-axis
	 * @return a new {@code Point2F} instance with {@code point} inside the image represented by {@code resolutionX} and {@code resolutionY}
	 * @throws NullPointerException thrown if, and only if, {@code point} is {@code null}
	 */
	public static Point2F toImage(final Point2F point, final float resolutionX, final float resolutionY) {
		final float component1 = positiveModulo(point.component1 * resolutionX - 0.5F, resolutionX);
		final float component2 = positiveModulo(point.component2 * resolutionY - 0.5F, resolutionY);
		
		return new Point2F(component1, component2);
	}
	
	/**
	 * Returns the distance from {@code eye} to {@code lookAt}.
	 * <p>
	 * If either {@code eye} or {@code lookAt} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param eye a {@code Point2F} instance denoting the eye to look from
	 * @param lookAt a {@code Point2F} instance denoting the target to look at
	 * @return the distance from {@code eye} to {@code lookAt}
	 * @throws NullPointerException thrown if, and only if, either {@code eye} or {@code lookAt} are {@code null}
	 */
	public static float distance(final Point2F eye, final Point2F lookAt) {
		return Vector2F.direction(eye, lookAt).length();
	}
	
	/**
	 * Returns the squared distance from {@code eye} to {@code lookAt}.
	 * <p>
	 * If either {@code eye} or {@code lookAt} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param eye a {@code Point2F} instance denoting the eye to look from
	 * @param lookAt a {@code Point2F} instance denoting the target to look at
	 * @return the squared distance from {@code eye} to {@code lookAt}
	 * @throws NullPointerException thrown if, and only if, either {@code eye} or {@code lookAt} are {@code null}
	 */
	public static float distanceSquared(final Point2F eye, final Point2F lookAt) {
		return Vector2F.direction(eye, lookAt).lengthSquared();
	}
}