/**
 * Provides the Color API.
 * <p>
 * The Color API provides data types for colors, color spaces and spectral curves.
 * <h3>Overview</h3>
 * <p>
 * The following list contains information about the data types that represents colors in this API.
 * <ul>
 * <li>{@link org.dayflower.color.Color3D Color3D} represents a color with three {@code double}-based components.</li>
 * <li>{@link org.dayflower.color.Color3F Color3F} represents a color with three {@code float}-based components.</li>
 * <li>{@link org.dayflower.color.Color4D Color4D} represents a color with four {@code double}-based components.</li>
 * <li>{@link org.dayflower.color.Color4F Color4F} represents a color with four {@code float}-based components.</li>
 * </ul>
 * <p>
 * The following list contains information about the data types that represents color spaces in this API.
 * <ul>
 * <li>{@link org.dayflower.color.ColorSpaceD ColorSpaceD} represents a color space that performs conversions for the color types {@code Color3D} and {@code Color4D}.</li>
 * <li>{@link org.dayflower.color.ColorSpaceF ColorSpaceF} represents a color space that performs conversions for the color types {@code Color3F} and {@code Color4F}.</li>
 * </ul>
 * <p>
 * The following list contains information about the data types that represents spectral curves.
 * <ul>
 * <li>{@link org.dayflower.color.SpectralCurveD SpectralCurveD} represents a spectral curve of type {@code double} that is used for sampled or analytic spectral data.</li>
 * <li>{@link org.dayflower.color.SpectralCurveF SpectralCurveF} represents a spectral curve of type {@code float} that is used for sampled or analytic spectral data.</li>
 * <li>{@link org.dayflower.color.ChromaticSpectralCurveD ChromaticSpectralCurveD} is an implementation of {@code SpectralCurveD} that contains chromaticity pairs.</li>
 * <li>{@link org.dayflower.color.ChromaticSpectralCurveF ChromaticSpectralCurveF} is an implementation of {@code SpectralCurveF} that contains chromaticity pairs.</li>
 * <li>{@link org.dayflower.color.ConstantSpectralCurveD ConstantSpectralCurveD} is an implementation of {@code SpectralCurveD} that contains a constant value.</li>
 * <li>{@link org.dayflower.color.ConstantSpectralCurveF ConstantSpectralCurveF} is an implementation of {@code SpectralCurveF} that contains a constant value.</li>
 * <li>{@link org.dayflower.color.IrregularSpectralCurveD IrregularSpectralCurveD} is an implementation of {@code SpectralCurveD} that contains irregular spectral data.</li>
 * <li>{@link org.dayflower.color.IrregularSpectralCurveF IrregularSpectralCurveF} is an implementation of {@code SpectralCurveF} that contains irregular spectral data.</li>
 * <li>{@link org.dayflower.color.RegularSpectralCurveD RegularSpectralCurveD} is an implementation of {@code SpectralCurveD} that contains regular spectral data.</li>
 * <li>{@link org.dayflower.color.RegularSpectralCurveF RegularSpectralCurveF} is an implementation of {@code SpectralCurveF} that contains regular spectral data.</li>
 * </ul>
 * <p>
 * The following list contains information about the remaining data types.
 * <ul>
 * <li>{@link org.dayflower.color.ArrayComponentOrder ArrayComponentOrder} is used to determine the order in which the components are stored in an array.</li>
 * <li>{@link org.dayflower.color.PackedIntComponentOrder PackedIntComponentOrder} is used to determine the order in which the components are stored in an {@code int}, in packed form.</li>
 * </ul>
 */
package org.dayflower.color;