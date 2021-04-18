The Renderer API
================
The Renderer API provides the rendering algorithms for Dayflower.

Supported Features
------------------
Coming soon...

Packages
--------
* `org.dayflower.renderer` - The Renderer API.
* `org.dayflower.renderer.cpu` - The Renderer CPU API.
* `org.dayflower.renderer.gpu` - The Renderer GPU API.
* `org.dayflower.renderer.observer` - The Renderer Observer API.

Examples
--------
Below follows a few examples that demonstrates various features.

#### CPU Renderer Example
The following example demonstrates how the `CPURenderer` class can be used to render a `Scene` to an image on your hard drive.
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
        cPURenderer.setRenderPasses(1000);
        cPURenderer.setRenderPassesPerDisplayUpdate(1);
        cPURenderer.setRenderingAlgorithm(RenderingAlgorithm.PATH_TRACING);
        cPURenderer.setup();
        cPURenderer.render();
        cPURenderer.dispose();
    }
}
```

#### GPU Renderer Example
The following example demonstrates how the `GPURenderer` class can be used to render a `Scene` to an image on your hard drive.
```java
import org.dayflower.renderer.RenderingAlgorithm;
import org.dayflower.renderer.gpu.GPURenderer;
import org.dayflower.renderer.observer.FileRendererObserver;
import org.dayflower.scene.demo.Demo;

public class CPURendererExample {
    public static void main(String[] args) {
        GPURenderer gPURenderer = new GPURenderer(new FileRendererObserver("Image.png", true, false));
        gPURenderer.setScene(Demo.createCornellBoxScene());
        gPURenderer.setImage();
        gPURenderer.setMaximumBounce(20);
        gPURenderer.setMinimumBounceRussianRoulette(5);
        gPURenderer.setRenderPasses(100);
        gPURenderer.setRenderPassesPerDisplayUpdate(10);
        gPURenderer.setRenderingAlgorithm(RenderingAlgorithm.PATH_TRACING);
        gPURenderer.setup();
        gPURenderer.render();
        gPURenderer.dispose();
    }
}
```