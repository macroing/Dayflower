The Image API
=============
The Image API provides image processing functionality for Dayflower.

Supported Features
------------------
* `ByteImageD` - An implementation of `ImageD` that stores individual pixels as four `byte` values in a `byte[]`.
* `ByteImageF` - An implementation of `ImageF` that stores individual pixels as four `byte` values in a `byte[]`.
* `ConvolutionKernel33D` - A convolution kernel with three rows and three columns of data type `double`.
* `ConvolutionKernel33F` - A convolution kernel with three rows and three columns of data type `float`.
* `ConvolutionKernel55D` - A convolution kernel with five rows and five columns of data type `double`.
* `ConvolutionKernel55F` - A convolution kernel with five rows and five columns of data type `float`.
* `Image` - An abstract class that provides the general contract for an image.
* `ImageD` - An abstract class that implements `Image` and uses the data type `double`.
* `ImageF` - An abstract class that implements `Image` and uses the data type `float`.
* `IntImageD` - An implementation of `ImageD` that stores individual pixels as packed `int` values in an `int[]`.
* `IntImageF` - An implementation of `ImageF` that stores individual pixels as packed `int` values in an `int[]`.
* `PixelD` - A pixel representation that is used by the `PixelImageD` class.
* `PixelF` - A pixel representation that is used by the `PixelImageF` class.
* `PixelImageD` - An implementation of `ImageD` that stores individual pixels as `PixelD` instances.
* `PixelImageF` - An implementation of `ImageF` that stores individual pixels as `PixelF` instances.
* `PixelOperation` - An enumeration that can perform operations on pixel coordinates or indices.

Packages
--------
* `org.dayflower.image` - The Image API.

Dependencies
------------
* The Color API
* The Filter API
* The Geometry API
* The Node API
* The Utility API

Examples
--------
Below follows a few examples that demonstrates various features.

#### PixelImageF Blend Example
The following example loads two images from your hard drive, blends them together and saves the result to your hard drive.
```java
import org.dayflower.image.ImageF;
import org.dayflower.image.PixelImageF;

public class PixelImageFBlendExample {
    public static void main(String[] args) {
        ImageF imageF0 = PixelImageF.load("Image-0.png");
        ImageF imageF1 = PixelImageF.load("Image-1.png");
        ImageF imageF = PixelImageF.blend(imageF0, imageF1, 0.5F);
        imageF.save("PixelImageF-Blend-Example.png");
    }
}
```

#### PixelImageF Construction Example
The following example demonstrates a few ways to create `PixelImageF` instances.
```java
import org.dayflower.color.Color4F;
import org.dayflower.filter.MitchellFilter2F;
import org.dayflower.image.PixelImageF;

public class PixelImageFConstructionExample {
    public static void main(String[] args) {
        PixelImageF pixelImageF0 = new PixelImageF(800, 800, Color4F.WHITE, new MitchellFilter2F());
        PixelImageF pixelImageF1 = new PixelImageF(pixelImageF0);
        PixelImageF pixelImageF2 = new PixelImageF(800, 800, Color4F.arrayRead(pixelImageF1.toIntArray()));
    }
}
```

#### PixelImageF Draw Example
The following example creates an empty image, draws a line, a rectangle and a triangle and saves the result to your hard drive.
```java
import org.dayflower.color.Color4F;
import org.dayflower.geometry.Point2I;
import org.dayflower.geometry.shape.Line2I;
import org.dayflower.geometry.shape.Rectangle2I;
import org.dayflower.geometry.shape.Triangle2I;
import org.dayflower.image.ImageF;
import org.dayflower.image.PixelImageF;

public class PixelImageFDrawExample {
    public static void main(String[] args) {
        ImageF imageF = new PixelImageF(150, 150);
        imageF.drawLine(new Line2I(new Point2I(20, 20), new Point2I(100, 100)), Color4F.RED);
        imageF.drawRectangle(new Rectangle2I(new Point2I(20, 20), new Point2I(100, 100)), Color4F.RED);
        imageF.drawTriangle(new Triangle2I(new Point2I(60, 20), new Point2I(100, 100), new Point2I(20, 100)), Color4F.RED);
        imageF.save("PixelImageF-Draw-Example.png");
    }
}
```

#### PixelImageF Fill Example
The following example creates two images, one empty and one random. Then it fills the empty image with a circle and the random image and saves the result to your hard drive.
```java
import org.dayflower.color.Color4F;
import org.dayflower.geometry.Point2I;
import org.dayflower.geometry.shape.Circle2I;
import org.dayflower.geometry.shape.Rectangle2I;
import org.dayflower.image.ImageF;
import org.dayflower.image.PixelImageF;

public class PixelImageFFillExample {
    public static void main(String[] args) {
        ImageF imageFRandom = PixelImageF.random(50, 50);
        ImageF imageF = new PixelImageF(150, 150);
        imageF.fillCircle(new Circle2I(new Point2I(75, 75), 50), Color4F.RED);
        imageF.fillPixelImage(imageFRandom, imageFRandom.getBounds(), new Rectangle2I(new Point2I(50, 50), new Point2I(100, 100)));
        imageF.save("PixelImageF-Fill-Example.png");
    }
}
```

#### PixelImageF Multiply Example
The following example loads an image from your hard drive, multiplies it with a convolution kernel and saves the result to your hard drive.
```java
import org.dayflower.image.ConvolutionKernel33F;
import org.dayflower.image.ImageF;
import org.dayflower.image.PixelImageF;

public class PixelImageFMultiplyExample {
    public static void main(String[] args) {
        ImageF imageF = PixelImageF.load("Image.png");
        imageF.multiply(ConvolutionKernel33F.SHARPEN);
        imageF.save("PixelImageF-Multiply-Example.png");
    }
}
```

#### PixelImageF Screen Capture Example
The following example captures the contents of the screen and saves the result to your hard drive.
```java
import org.dayflower.geometry.Point2I;
import org.dayflower.geometry.shape.Rectangle2I;
import org.dayflower.image.ImageF;
import org.dayflower.image.PixelImageF;

public class PixelImageFScreenCaptureExample {
    public static void main(String[] args) {
        ImageF imageF = PixelImageF.createScreenCapture(new Rectangle2I(new Point2I(100, 100), new Point2I(200, 200)));
        imageF.save("PixelImageF-Screen-Capture-Example.png");
    }
}
```