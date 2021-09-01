Dayflower
=========
Dayflower is an application and a library for rendering in Java.

Its primary focus is photorealistic 3D-rendering, preferably in realtime. However, it can also be used for 2D-rendering and image processing.

![alt text](https://github.com/macroing/Dayflower/blob/master/images/Image-002.png "Dayflower")
![alt text](https://github.com/macroing/Dayflower/blob/master/images/Image-001.png "Dayflower")

Getting Started
---------------
To clone this repository and build the project using Apache Ant, you can type the following in Git Bash.

```bash
git clone https://github.com/macroing/Dayflower.git
cd Dayflower
ant
```

If you have cloned this repository and built the project using Apache Ant as presented above, you can run the project using its GUI in the following way.
```bash
cd distribution/Dayflower
java -Djava.library.path=lib -jar Dayflower.jar
```

If you prefer to use Dayflower as a library and not as an application, the following Java source code demonstrates how you can use the CPU- and GPU renderers.
```java
import org.dayflower.renderer.RenderingAlgorithm;
import org.dayflower.renderer.cpu.CPURenderer;
import org.dayflower.renderer.observer.FileRendererObserver;
import org.dayflower.scene.demo.Demo;

public class CPURendererExample {
    public static void main(String[] args) {
        CPURenderer cPURenderer = new CPURenderer(new FileRendererObserver("Image.png", true, false));
        cPURenderer.setScene(Demo.createCornellBoxScene());
        cPURenderer.setImage();
        cPURenderer.setMaximumBounce(20);
        cPURenderer.setMinimumBounceRussianRoulette(5);
        cPURenderer.setRenderingAlgorithm(RenderingAlgorithm.PATH_TRACING);
        cPURenderer.setup();
        cPURenderer.render();
        cPURenderer.dispose();
    }
}
```
```java
import org.dayflower.renderer.RenderingAlgorithm;
import org.dayflower.renderer.gpu.GPURenderer;
import org.dayflower.renderer.observer.FileRendererObserver;
import org.dayflower.scene.demo.Demo;

public class GPURendererExample {
    public static void main(String[] args) {
        GPURenderer gPURenderer = new GPURenderer(new FileRendererObserver("Image.png", true, false));
        gPURenderer.setScene(Demo.createCornellBoxScene());
        gPURenderer.setImage();
        gPURenderer.setMaximumBounce(20);
        gPURenderer.setMinimumBounceRussianRoulette(5);
        gPURenderer.setRenderingAlgorithm(RenderingAlgorithm.PATH_TRACING);
        gPURenderer.setup();
        gPURenderer.render();
        gPURenderer.dispose();
    }
}
```

Supported Features
------------------
The lists below show the main features of the renderer.

#### Supported Rendering Algorithms
* Ambient Occlusion (CPU & GPU)
* Path Tracing (CPU & GPU)
* Ray Casting (CPU & GPU)
* Ray Tracing (CPU)

#### Supported Lights
* Diffuse Area Light (CPU)
* Directional Light (CPU & GPU)
* Low-Dynamic-Range (LDR) Image Light (CPU & GPU)
* Perez Light (CPU & GPU)
* Point Light (CPU & GPU)
* Primitive Area Light (CPU)
* Spot Light (CPU & GPU)

#### Supported Materials
* Bullseye Material (CPU & GPU)
* Checkerboard Material (CPU & GPU)
* Clear Coat Material (CPU & GPU)
* Disney Material (CPU & GPU)
* Function Material (CPU)
* Glass Material (CPU & GPU)
* Glossy Material (CPU & GPU)
* Hair Material (CPU)
* Matte Material (CPU & GPU)
* Metal Material (CPU & GPU)
* Mirror Material (CPU & GPU)
* Plastic Material (CPU & GPU)
* Polka Dot Material (CPU & GPU)
* Substrate Material (CPU & GPU)
* Uber Material (CPU)

#### Supported Modifiers
* Low-Dynamic-Range (LDR) Image Normal Map Modifier (CPU)
* No-Op Modifier (CPU)
* Simplex Noise Normal Map Modifier (CPU)

#### Supported Shapes
* Cone (CPU & GPU)
* Constructive Solid Geometry (CSG) (CPU)
* Curve (CPU)
* Cylinder (CPU & GPU)
* Disk (CPU & GPU)
* Hyperboloid (CPU & GPU)
* Paraboloid (CPU & GPU)
* Plane (CPU & GPU)
* Polygon (CPU)
* Procedural Terrain (CPU)
* Rectangle (CPU & GPU)
* Rectangular Cuboid (CPU & GPU)
* Sphere (CPU & GPU)
* Torus (CPU & GPU)
* Triangle (CPU & GPU)
* Triangle Mesh (CPU & GPU)

#### Supported Textures
* Blend Texture (CPU & GPU)
* Bullseye Texture (CPU & GPU)
* Checkerboard Texture (CPU & GPU)
* Constant Texture (CPU & GPU)
* Dot Product Texture (CPU)
* Function Texture (CPU)
* Low-Dynamic-Range (LDR) Image Texture (CPU & GPU)
* Marble Texture (CPU & GPU)
* Polka Dot Texture (CPU & GPU)
* Simplex Fractional Brownian Motion Texture (CPU & GPU)
* Surface Normal Texture (CPU & GPU)
* UV Texture (CPU & GPU)


Documentation
-------------
The documentation for this library can be found in the Javadocs that are generated when building it.

Dependencies
------------
 - [Java 8](http://www.java.com)
 - [Macroing / Aparapi](https://github.com/macroing/aparapi)

References
----------
This library has been created with the help of many articles, books and renderer implementations.

The following list shows the renderer implementations that have been referenced the most, in alphabetical order.

 - [PBRT](https://www.pbrt.org/)
 - [Rayito](https://github.com/Tecla/Rayito)
 - [SmallPT](https://www.kevinbeason.com/smallpt/)
 - [Sunflow](http://sunflow.sourceforge.net/)

Note
----
This library has not reached version 1.0.0 and been released to the public yet. Therefore, you can expect that backward incompatible changes are likely to occur between commits. When this library reaches version 1.0.0, it will be tagged and available on the "releases" page. At that point, backward incompatible changes should only occur when a new major release is made.