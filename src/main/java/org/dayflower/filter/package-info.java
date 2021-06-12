/**
 * Provides the Filter API.
 * <p>
 * The Filter API provides data types that represents filters.
 * <h3>Overview</h3>
 * <p>
 * The following list contains information about the core classes and interfaces in this API.
 * <ul>
 * <li>{@link org.dayflower.filter.Filter2D Filter2D} represents a 2-dimensional filter that operates on and returns {@code double} values.</li>
 * <li>{@link org.dayflower.filter.Filter2F Filter2F} represents a 2-dimensional filter that operates on and returns {@code float} values.</li>
 * <li>{@link org.dayflower.filter.FilterNB FilterNB} represents an N-dimensional filter that operates on and returns {@code byte[]} values.</li>
 * </ul>
 * <p>
 * The following list contains information about the data types that represents box filters in this API.
 * <ul>
 * <li>{@link org.dayflower.filter.BoxFilter2D BoxFilter2D} is an implementation of {@code Filter2D} that represents a box filter.</li>
 * <li>{@link org.dayflower.filter.BoxFilter2F BoxFilter2F} is an implementation of {@code Filter2F} that represents a box filter.</li>
 * </ul>
 * <p>
 * The following list contains information about the data types that represents Catmull-Rom filters in this API.
 * <ul>
 * <li>{@link org.dayflower.filter.CatmullRomFilter2D CatmullRomFilter2D} is an implementation of {@code Filter2D} that represents a Catmull-Rom filter.</li>
 * <li>{@link org.dayflower.filter.CatmullRomFilter2F CatmullRomFilter2F} is an implementation of {@code Filter2F} that represents a Catmull-Rom filter.</li>
 * </ul>
 * <p>
 * The following list contains information about the data types that represents echo filters in this API.
 * <ul>
 * <li>{@link org.dayflower.filter.EchoFilterNB EchoFilterNB} is an implementation of {@code FilterNB} that performs an echo effect.</li>
 * </ul>
 * <p>
 * The following list contains information about the data types that represents Gaussian filters in this API.
 * <ul>
 * <li>{@link org.dayflower.filter.GaussianFilter2D GaussianFilter2D} is an implementation of {@code Filter2D} that represents a Gaussian filter.</li>
 * <li>{@link org.dayflower.filter.GaussianFilter2F GaussianFilter2F} is an implementation of {@code Filter2F} that represents a Gaussian filter.</li>
 * </ul>
 * <p>
 * The following list contains information about the data types that represents Lanczos-Sinc filters in this API.
 * <ul>
 * <li>{@link org.dayflower.filter.LanczosSincFilter2D LanczosSincFilter2D} is an implementation of {@code Filter2D} that represents a Lanczos-Sinc filter.</li>
 * <li>{@link org.dayflower.filter.LanczosSincFilter2F LanczosSincFilter2F} is an implementation of {@code Filter2F} that represents a Lanczos-Sinc filter.</li>
 * </ul>
 * <p>
 * The following list contains information about the data types that represents Mitchell filters in this API.
 * <ul>
 * <li>{@link org.dayflower.filter.MitchellFilter2D MitchellFilter2D} is an implementation of {@code Filter2D} that represents a Mitchell filter.</li>
 * <li>{@link org.dayflower.filter.MitchellFilter2F MitchellFilter2F} is an implementation of {@code Filter2F} that represents a Mitchell filter.</li>
 * </ul>
 * <p>
 * The following list contains information about the data types that represents triangle filters in this API.
 * <ul>
 * <li>{@link org.dayflower.filter.TriangleFilter2D TriangleFilter2D} is an implementation of {@code Filter2D} that represents a triangle filter.</li>
 * <li>{@link org.dayflower.filter.TriangleFilter2F TriangleFilter2F} is an implementation of {@code Filter2F} that represents a triangle filter.</li>
 * </ul>
 * <h3>Dependencies</h3>
 * <p>
 * The following list shows all dependencies for this API.
 * <ul>
 * <li>The Utility API</li>
 * </ul>
 */
package org.dayflower.filter;