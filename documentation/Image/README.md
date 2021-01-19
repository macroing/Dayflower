The Image API
=============
The Image API provides image processing functionality for Dayflower.

Supported Features
------------------
Coming soon...

Packages
--------
* `org.dayflower.image` - The Image API.

Examples
--------
Below follows a few examples that demonstrates various features.

#### Blend Example
The following example loads two images from your hard drive, blends them together and saves the result to your hard drive.
```java
import org.dayflower.image.PixelImageF;

public class BlendExample {
    public static void main(String[] args) {
        PixelImageF pixelImageF0 = PixelImageF.load("Image-0.png");
        PixelImageF pixelImageF1 = PixelImageF.load("Image-1.png");
        
        PixelImageF pixelImageF = PixelImageF.blend(pixelImageF0, pixelImageF1, 0.5F);
        pixelImageF.save("Blend-Example.png");
    }
}
```

#### Convolution Kernel 3x3 Example
The following example loads an image from your hard drive, multiplies it with a convolution kernel and saves the result to your hard drive.
```java
import org.dayflower.image.ConvolutionKernel33F;
import org.dayflower.image.PixelImageF;

public class ConvolutionKernel33FExample {
    public static void main(String[] args) {
        PixelImageF pixelImageF = PixelImageF.load("Image.png");
        pixelImageF.multiply(ConvolutionKernel33F.SHARPEN);
        pixelImageF.save("Convolution-Kernel-Example.png");
    }
}
```

#### Fill Example
The following example creates two images, one empty and one random. Then it fills the empty image with a circle and the random image and saves the result to your hard drive.
```java
import org.dayflower.color.Color3F;
import org.dayflower.geometry.Point2I;
import org.dayflower.geometry.shape.Circle2I;
import org.dayflower.geometry.shape.Rectangle2I;
import org.dayflower.image.PixelImageF;

public class FillExample {
    public static void main(String[] args) {
        PixelImageF pixelImageF0 = PixelImageF.random(50, 50);
        
        PixelImageF pixelImageF = new PixelImageF(150, 150);
        pixelImageF.fillCircle(new Circle2I(new Point2I(75, 75), 50), Color3F.RED);
        pixelImageF.fillPixelImage(pixelImageF0, pixelImageF0.getBounds(), new Rectangle2I(new Point2I(50, 50), new Point2I(100, 100)));
        pixelImageF.save("Fill-Example.png");
    }
}
```