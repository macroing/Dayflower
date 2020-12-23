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
import org.dayflower.image.PixelImage;

public class BlendExample {
    public static void main(String[] args) {
        PixelImage pixelImage0 = PixelImage.load("Image-0.png");
        PixelImage pixelImage1 = PixelImage.load("Image-1.png");
        
        PixelImage pixelImage = PixelImage.blend(pixelImage0, pixelImage1, 0.5F);
        pixelImage.save("Blend-Example.png");
    }
}
```

#### Convolution Kernel Example
The following example loads an image from your hard drive, multiplies it with a convolution kernel and saves the result to your hard drive.
```java
import org.dayflower.image.ConvolutionKernel33F;
import org.dayflower.image.PixelImage;

public class ConvolutionKernel33FExample {
    public static void main(String[] args) {
        PixelImage pixelImage = PixelImage.load("Image.png");
        pixelImage.multiply(ConvolutionKernel33F.SHARPEN);
        pixelImage.save("Convolution-Kernel-Example.png");
    }
}
```

#### Fill And Draw Examples
The following example creates two images, one empty and one random. Then it fills the empty image with a circle and the random image and saves the result to your hard drive.
```java
import org.dayflower.geometry.Point2I;
import org.dayflower.geometry.shape.Circle2I;
import org.dayflower.geometry.shape.Rectangle2I;
import org.dayflower.image.Color3F;
import org.dayflower.image.PixelImage;

public class FillExample {
    public static void main(String[] args) {
        PixelImage pixelImage0 = PixelImage.random(50, 50);
        
        PixelImage pixelImage = new PixelImage(150, 150);
        pixelImage.fillCircle(new Circle2I(new Point2I(75, 75), 50), Color3F.RED);
        pixelImage.fillPixelImage(pixelImage0, pixelImage0.getBounds(), new Rectangle2I(new Point2I(50, 50), new Point2I(100, 100)));
        pixelImage.save("Fill-Example.png");
    }
}
```