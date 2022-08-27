/**
 * Provides the Color API.
 * <p>
 * The Color API provides data types for colors, color spaces and spectral curves.
 * <h3>Overview</h3>
 * <p>
 * The following list contains information about the data types that represents colors in this API.
 * <ul>
 * <li>{@link org.dayflower.color.Color3F Color3F} represents a color with three {@code float}-based components.</li>
 * <li>{@link org.dayflower.color.Color4F Color4F} represents a color with four {@code float}-based components.</li>
 * </ul>
 * <p>
 * The following list contains information about the data types that represents color spaces in this API.
 * <ul>
 * <li>{@link org.dayflower.color.ColorSpaceF ColorSpaceF} represents a color space that performs conversions for the color types {@code Color3F} and {@code Color4F}.</li>
 * </ul>
 * <p>
 * The following list contains information about the data types that represents spectral curves.
 * <ul>
 * <li>{@link org.dayflower.color.SpectralCurveF SpectralCurveF} represents a spectral curve of type {@code float} that is used for sampled or analytic spectral data.</li>
 * <li>{@link org.dayflower.color.ChromaticSpectralCurveF ChromaticSpectralCurveF} is an implementation of {@code SpectralCurveF} that contains chromaticity pairs.</li>
 * <li>{@link org.dayflower.color.ConstantSpectralCurveF ConstantSpectralCurveF} is an implementation of {@code SpectralCurveF} that contains a constant value.</li>
 * <li>{@link org.dayflower.color.IrregularSpectralCurveF IrregularSpectralCurveF} is an implementation of {@code SpectralCurveF} that contains irregular spectral data.</li>
 * <li>{@link org.dayflower.color.RegularSpectralCurveF RegularSpectralCurveF} is an implementation of {@code SpectralCurveF} that contains regular spectral data.</li>
 * </ul>
 * <h3>Dependencies</h3>
 * <p>
 * The following list shows all dependencies for this API.
 * <ul>
 * <li>The Utility API</li>
 * </ul>
 */
package org.dayflower.color;