Dayflower
=========
Dayflower is an application and a library for rendering in Java.

Its primary focus is photorealistic 3D-rendering, preferably in realtime. However, it can also be used for 2D-rendering and image processing.

![alt text](https://github.com/macroing/Dayflower/blob/master/images/Image-006.png "Dayflower")
![alt text](https://github.com/macroing/Dayflower/blob/master/images/Image-007.png "Dayflower")
![alt text](https://github.com/macroing/Dayflower/blob/master/images/Image-008.png "Dayflower")
![alt text](https://github.com/macroing/Dayflower/blob/master/images/Image-009.png "Dayflower")

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
        cPURenderer.render(10);
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
        gPURenderer.render(10);
        gPURenderer.dispose();
    }
}
```

Supported Features
------------------
The tables below show the main features of the renderer.

#### Supported Rendering Algorithms

| Name              | CPU | GPU |
| ----------------- | --- | --- |
| Ambient Occlusion | Yes | Yes |
| Path Tracing      | Yes | Yes |
| Ray Casting       | Yes | Yes |
| Ray Tracing       | Yes | Yes |

#### Supported Lights

| Name                    | CPU | GPU |
| ----------------------- | --- | --- |
| Diffuse Area Light      | Yes | No  |
| Directional Light       | Yes | Yes |
| Image Light             | Yes | Yes |
| Perez Light (Sun & Sky) | Yes | Yes |
| Point Light             | Yes | Yes |
| Primitive Area Light    | Yes | No  |
| Spot Light              | Yes | Yes |

#### Supported Materials

| Name                  | CPU | GPU |
| --------------------- | --- | --- |
| Bullseye Material     | Yes | Yes |
| Checkerboard Material | Yes | Yes |
| Clear Coat Material   | Yes | Yes |
| Disney Material       | Yes | Yes |
| Function Material     | Yes | No  |
| Glass Material        | Yes | Yes |
| Glossy Material       | Yes | Yes |
| Hair Material         | Yes | No  |
| Matte Material        | Yes | Yes |
| Metal Material        | Yes | Yes |
| Mirror Material       | Yes | Yes |
| Plastic Material      | Yes | Yes |
| Polka Dot Material    | Yes | Yes |
| Substrate Material    | Yes | Yes |
| Uber Material         | Yes | No  |

#### Supported Modifiers

| Name                                                      | CPU | GPU |
| --------------------------------------------------------- | --- | --- |
| Bi Modifier                                               | Yes | No  |
| No-Op Modifier                                            | Yes | Yes |
| Normal Map Low-Dynamic-Range (LDR) Image Modifier         | Yes | Yes |
| Simplex Noise Normal Map Modifier                         | Yes | Yes |
| Steep Parallax Map Low-Dynamic-Range (LDR) Image Modifier | Yes | No  |

#### Supported Shapes

| Name                              | CPU | GPU |
| --------------------------------- | --- | --- |
| Compound Shape                    | Yes | No  |
| Cone                              | Yes | Yes |
| Constructive Solid Geometry (CSG) | Yes | No  |
| Curve                             | Yes | No  |
| Cylinder                          | Yes | Yes |
| Disk                              | Yes | Yes |
| Hyperboloid                       | Yes | Yes |
| Paraboloid                        | Yes | Yes |
| Plane                             | Yes | Yes |
| Polygon                           | Yes | Yes |
| Procedural Terrain                | Yes | No  |
| Rectangle                         | Yes | Yes |
| Rectangular Cuboid                | Yes | Yes |
| Sphere                            | Yes | Yes |
| Torus                             | Yes | Yes |
| Triangle                          | Yes | Yes |
| Triangle Mesh                     | Yes | Yes |

#### Supported Textures

| Name                                       | CPU | GPU |
| ------------------------------------------ | --- | --- |
| Blend Texture                              | Yes | Yes |
| Bullseye Texture                           | Yes | Yes |
| Checkerboard Texture                       | Yes | Yes |
| Constant Texture                           | Yes | Yes |
| Dot Product Texture                        | Yes | Yes |
| Function Texture                           | Yes | No  |
| Low-Dynamic-Range (LDR) Image Texture      | Yes | Yes |
| Marble Texture                             | Yes | Yes |
| Polka Dot Texture                          | Yes | Yes |
| Simplex Fractional Brownian Motion Texture | Yes | Yes |
| Surface Normal Texture                     | Yes | Yes |
| UV Texture                                 | Yes | Yes |

Documentation
-------------
The documentation for this library can be found in the Javadocs that are generated when building it.

Library
-------
The following table describes the different APIs and their current status in the library.

| Name                                   | Javadoc | Unit Test |
| -------------------------------------- | ------- | --------- |
| Change API                             | 100.0%  | 100.0%    |
| Color API                              | 100.0%  |  83.8%    |
| Filter API                             | 100.0%  |   0.0%    |
| Geometry API                           | 100.0%  |  76.5%    |
| Geometry Bounding Volume API           | 100.0%  | 100.0%    |
| Geometry Bounding Volume Hierarchy API | 100.0%  |   0.0%    |
| Geometry Rasterizer API                | 100.0%  |   0.0%    |
| Geometry Shape API                     | 100.0%  |   6.4%    |
| Image API                              | 100.0%  |   0.0%    |
| JavaFX Concurrent API                  | 100.0%  |   0.0%    |
| JavaFX Scene Canvas API                | 100.0%  |   0.0%    |
| JavaFX Scene Control API               | 100.0%  |   0.0%    |
| JavaFX Scene Image API                 | 100.0%  | 100.0%    |
| JavaFX Scene Layout API                | 100.0%  |   0.0%    |
| Node API                               | 100.0%  |   5.5%    |
| Noise API                              | 100.0%  |   0.0%    |
| Parameter API                          | 100.0%  |   0.0%    |
| Renderer API                           | 100.0%  |   0.0%    |
| Renderer CPU API                       | 100.0%  |   0.0%    |
| Renderer GPU API                       | 100.0%  |   0.0%    |
| Renderer Observer API                  | 100.0%  |   0.0%    |
| Sampler API                            | 100.0%  |   0.0%    |
| Scene API                              | 100.0%  |   0.0%    |
| Scene BSSRDF API                       | 100.0%  |   0.0%    |
| Scene BXDF API                         | 100.0%  |   0.0%    |
| Scene Compiler API                     | 100.0%  |   0.0%    |
| Scene Demo API                         | 100.0%  |   0.0%    |
| Scene Fresnel API                      | 100.0%  |   0.0%    |
| Scene Light API                        | 100.0%  |   0.0%    |
| Scene Loader API                       | 100.0%  |   0.0%    |
| Scene Material API                     | 100.0%  |   0.0%    |
| Scene Microfacet API                   | 100.0%  |   0.0%    |
| Scene Modifier API                     | 100.0%  |   0.0%    |
| Scene Texture API                      | 100.0%  |   0.0%    |
| Utility API                            | 100.0%  |  66.3%    |
| Wavefront Material API                 | 100.0%  |   0.0%    |
| Wavefront Object API                   | 100.0%  |   0.0%    |

Dependencies
------------
 - [Java 8](http://www.java.com)
 - [Macroing / Aparapi](https://github.com/macroing/aparapi)
 - [Macroing / Java](https://github.com/macroing/Java)

References
----------
This library has been created with the help of many articles, books and renderer implementations.

The following list shows the renderer implementations that have been referenced the most, in alphabetical order.

 - [PBRT](https://www.pbrt.org/)
 - [Rayito](https://github.com/Tecla/Rayito)
 - [SmallPT](https://www.kevinbeason.com/smallpt/)
 - [Sunflow](http://sunflow.sourceforge.net/)

Java 9 and Above
----------------
Dayflower is built for Java 8. When it's being used in Java 9 and above, certain problems are likely to occur. Some of them are presented below.

 - JavaFX was removed from the JDK and the JRE when Java 11 was released. In Java 11 it has to be added manually.

Note
----
This library has not reached version 1.0.0 and been released to the public yet. Therefore, you can expect that backward incompatible changes are likely to occur between commits. When this library reaches version 1.0.0, it will be tagged and available on the "releases" page. At that point, backward incompatible changes should only occur when a new major release is made.