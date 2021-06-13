/**
 * Provides the Image API.
 * <p>
 * The Image API provides image processing functionality.
 * <h3>Overview</h3>
 * <p>
 * The following list contains information about the core classes in this API.
 * <ul>
 * <li>{@link org.dayflower.image.Image Image} represents an image.</li>
 * <li>{@link org.dayflower.image.ImageD ImageD} is an extension of {@code Image} that adds additional methods that operates on {@code double}-based data types.</li>
 * <li>{@link org.dayflower.image.ImageF ImageF} is an extension of {@code Image} that adds additional methods that operates on {@code float}-based data types.</li>
 * <li>{@link org.dayflower.image.PixelOperation PixelOperation} provides a set of operations to perform on a pixel when it is outside the boundaries of an image.</li>
 * </ul>
 * <p>
 * The following list contains information about specific {@code Image} implementations in this API.
 * <ul>
 * <li>{@link org.dayflower.image.ByteImageD ByteImageD} is an {@code ImageD} implementation that stores individual pixels as four {@code byte} values in a {@code byte[]}.</li>
 * <li>{@link org.dayflower.image.ByteImageF ByteImageF} is an {@code ImageF} implementation that stores individual pixels as four {@code byte} values in a {@code byte[]}.</li>
 * <li>{@link org.dayflower.image.DoubleImageD DoubleImageD} is an {@code ImageD} implementation that stores individual pixels as four {@code double} values in a {@code double[]}.</li>
 * <li>{@link org.dayflower.image.FloatImageF FloatImageF} is an {@code ImageF} implementation that stores individual pixels as four {@code float} values in a {@code float[]}.</li>
 * <li>{@link org.dayflower.image.IntImageD IntImageD} is an {@code ImageD} implementation that stores individual pixels as a packed {@code int} value in an {@code int[]}.</li>
 * <li>{@link org.dayflower.image.IntImageF IntImageF} is an {@code ImageF} implementation that stores individual pixels as a packed {@code int} value in an {@code int[]}.</li>
 * <li>{@link org.dayflower.image.PixelImageD PixelImageD} is an {@code ImageD} implementation that stores individual pixels as {@code PixelD} instances.</li>
 * <li>{@link org.dayflower.image.PixelImageF PixelImageF} is an {@code ImageF} implementation that stores individual pixels as {@code PixelF} instances.</li>
 * <li>{@link org.dayflower.image.PixelD PixelD} represents a pixel in a {@code PixelImageD} instance.</li>
 * <li>{@link org.dayflower.image.PixelF PixelF} represents a pixel in a {@code PixelImageF} instance.</li>
 * </ul>
 * <p>
 * The following list contains information about convolution kernels in this API.
 * <ul>
 * <li>{@link org.dayflower.image.ConvolutionKernel33D ConvolutionKernel33D} is a convolution kernel with 9 {@code double}-based elements in three rows and three columns.</li>
 * <li>{@link org.dayflower.image.ConvolutionKernel33F ConvolutionKernel33F} is a convolution kernel with 9 {@code float}-based elements in three rows and three columns.</li>
 * <li>{@link org.dayflower.image.ConvolutionKernel55D ConvolutionKernel55D} is a convolution kernel with 25 {@code double}-based elements in five rows and five columns.</li>
 * <li>{@link org.dayflower.image.ConvolutionKernel55F ConvolutionKernel55F} is a convolution kernel with 25 {@code float}-based elements in five rows and five columns.</li>
 * </ul>
 * <h3>Dependencies</h3>
 * <p>
 * The following list shows all dependencies for this API.
 * <ul>
 * <li>The Change API</li>
 * <li>The Color API</li>
 * <li>The Filter API</li>
 * <li>The Geometry API</li>
 * <li>The Geometry Rasterizer API</li>
 * <li>The Geometry Shape API</li>
 * <li>The Node API</li>
 * <li>The Noise API</li>
 * <li>The Utility API</li>
 * </ul>
 * <h3>Examples</h3>
 * <p>
 * The following example loads two images from your hard drive, blends them together and saves the result to your hard drive.
 * <pre>
 * <code>
 * import org.dayflower.image.ImageF;
 * import org.dayflower.image.PixelImageF;
 * 
 * public class Example {
 *     public static void main(String[] args) {
 *         ImageF imageF0 = PixelImageF.load("Image-0.png");
 *         ImageF imageF1 = PixelImageF.load("Image-1.png");
 *         
 *         ImageF imageF = PixelImageF.blend(imageF0, imageF1, 0.5F);
 *         imageF.save("Image-Result.png");
 *     }
 * }
 * </code>
 * </pre>
 * The following example demonstrates a few ways to create {@code PixelImageF} instances.
 * <pre>
 * <code>
 * import org.dayflower.color.Color4F;
 * import org.dayflower.filter.MitchellFilter2F;
 * import org.dayflower.image.PixelImageF;
 * 
 * public class Example {
 *     public static void main(String[] args) {
 *         PixelImageF pixelImageF0 = new PixelImageF(800, 800, Color4F.WHITE, new MitchellFilter2F());
 *         PixelImageF pixelImageF1 = new PixelImageF(pixelImageF0);
 *         PixelImageF pixelImageF2 = new PixelImageF(800, 800, Color4F.arrayRead(pixelImageF1.toIntArray()));
 *     }
 * }
 * </code>
 * </pre>
 * The following example creates an empty image, draws a line, a rectangle and a triangle and saves the result to your hard drive.
 * <pre>
 * <code>
 * import org.dayflower.color.Color4F;
 * import org.dayflower.geometry.Point2I;
 * import org.dayflower.geometry.shape.Line2I;
 * import org.dayflower.geometry.shape.Rectangle2I;
 * import org.dayflower.geometry.shape.Triangle2I;
 * import org.dayflower.image.ImageF;
 * import org.dayflower.image.PixelImageF;
 * 
 * public class Example {
 *     public static void main(String[] args) {
 *         ImageF imageF = new PixelImageF(150, 150);
 *         imageF.drawLine(new Line2I(new Point2I(20, 20), new Point2I(100, 100)), Color4F.RED);
 *         imageF.drawRectangle(new Rectangle2I(new Point2I(20, 20), new Point2I(100, 100)), Color4F.RED);
 *         imageF.drawTriangle(new Triangle2I(new Point2I(60, 20), new Point2I(100, 100), new Point2I(20, 100)), Color4F.RED);
 *         imageF.save("Image-Result.png");
 *     }
 * }
 * </code>
 * </pre>
 * The following example creates two images, one empty and one random. Then it fills the empty image with a circle and the random image and saves the result to your hard drive.
 * <pre>
 * <code>
 * import org.dayflower.color.Color4F;
 * import org.dayflower.geometry.Point2I;
 * import org.dayflower.geometry.shape.Circle2I;
 * import org.dayflower.geometry.shape.Rectangle2I;
 * import org.dayflower.image.ImageF;
 * import org.dayflower.image.PixelImageF;
 * 
 * public class Example {
 *     public static void main(String[] args) {
 *         ImageF imageFRandom = PixelImageF.random(50, 50);
 *         
 *         ImageF imageF = new PixelImageF(150, 150);
 *         imageF.fillCircle(new Circle2I(new Point2I(75, 75), 50), Color4F.RED);
 *         imageF.fillPixelImage(imageFRandom, imageFRandom.getBounds(), new Rectangle2I(new Point2I(50, 50), new Point2I(100, 100)));
 *         imageF.save("Image-Result.png");
 *     }
 * }
 * </code>
 * </pre>
 * The following example loads an image from your hard drive, multiplies it with a convolution kernel and saves the result to your hard drive.
 * <pre>
 * <code>
 * import org.dayflower.image.ConvolutionKernel33F;
 * import org.dayflower.image.ImageF;
 * import org.dayflower.image.PixelImageF;
 * 
 * public class Example {
 *     public static void main(String[] args) {
 *         ImageF imageF = PixelImageF.load("Image.png");
 *         imageF.multiply(ConvolutionKernel33F.SHARPEN);
 *         imageF.save("Image-Result.png");
 *     }
 * }
 * </code>
 * </pre>
 * The following example captures the contents of the screen and saves the result to your hard drive.
 * <pre>
 * <code>
 * import org.dayflower.geometry.Point2I;
 * import org.dayflower.geometry.shape.Rectangle2I;
 * import org.dayflower.image.Image;
 * import org.dayflower.image.PixelImageF;
 * 
 * public class Example {
 *     public static void main(String[] args) {
 *         Image image = PixelImageF.createScreenCapture(new Rectangle2I(new Point2I(100, 100), new Point2I(200, 200)));
 *         image.save("Image-Result.png");
 *     }
 * }
 * </code>
 * </pre>
 */
package org.dayflower.image;