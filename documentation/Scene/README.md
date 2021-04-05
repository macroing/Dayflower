The Scene API
=============
The Scene API provides the scene types and operations for Dayflower.

Supported Features
------------------
Below follows a few lists of features available in the Scene API.

#### The Scene API
* `AreaLight` - A `Light` that represents an area light.
* `BSDF` - A Bidirectional Scattering Distribution Function (BSDF) created by `Material` instances.
* `BSDFResult` - An instance of this class is created as a result by a `BSDF` instance.
* `BSSRDF` - A Bidirectional Surface Scattering Reflectance Distribution Function (BSSRDF) created by `Material` instances.
* `BXDF` - A Bidirectional Reflectance Distribution Function (BRDF) or a Bidirectional Transmittance Distribution Function (BTDF).
* `BXDFResult` - An instance of this class is created as a result by a `BXDF` instance.
* `Camera` - A camera from which an image can be formed. It is associated with a `Scene` instance.
* `Intersection` - A class that contains information about an intersection with a `Primitive` instance.
* `Light` - A light source that emits light. It is associated with a `Scene` instance.
* `Material` - A material assigned to a specific `Primitive` instance.
* `Primitive` - A primitive (or object) that can be rendered. It is associated with a `Scene` instance.
* `Scene` - Contains a `Camera`, a list of `Light` instances and a list of `Primitive` instances.
* `SceneLoader` - Loads a `Scene` instance.
* `Transform` - A transform assigned to a specific `Primitive` instance.

#### The Scene Light API
* `DiffuseAreaLight` - An `AreaLight` that represents a diffuse area light.
* `DirectionalLight` - A `Light` that represents a directional light.
* `LDRImageLight` - A `Light` that is backed by a low-dynamic-range (LDR) image.
* `PerezLight` - A `Light` implementation of the Perez algorithm.
* `PointLight` - A `Light` that represents a point light.
* `PrimitiveAreaLight` - An `AreaLight` that contains a `Primitive` instance.
* `SpotLight` - A `Light` that represents a spotlight.

#### The Scene Loader API
* `JavaSceneLoader` - A `SceneLoader` that produces `Scene` instances by loading, compiling and executing snippets of Java source code.

#### The Scene Material API
* `BullseyeMaterial` - A `Material` that alternates between two `Material` instances in a bullseye pattern.
* `CheckerboardMaterial` - A `Material` that alternates between two `Material` instances in a checkerboard pattern.
* `ClearCoatMaterial` - A `Material` that represents clear coat.
* `DisneyMaterial` - A `Material` that represents a Disney material.
* `FunctionMaterial` - A `Material` that alternates between different `Material` instances based on a `Function` instance.
* `GlassMaterial` - A `Material` that represents glass.
* `GlossyMaterial` - A `Material` that represents a glossy material.
* `HairMaterial` - A `Material` that represents hair.
* `MatteMaterial` - A `Material` that is used for matte surfaces.
* `MetalMaterial` - A `Material` that represents metal.
* `MirrorMaterial` - A `Material` that represents a mirror.
* `PlasticMaterial` - A `Material` that represents plastic.
* `SubstrateMaterial` - A `Material` that represents a substrate material.
* `UberMaterial` - A `Material` that can represent a wide variety of materials.

#### The Scene Modifier API
* `LDRImageNormalMapModifier` - A `Modifier` that performs normal mapping using a low-dynamic-range (LDR) image.
* `Modifier` - A modifier assigned to a specific `Material` instance and can modify the current `Intersection` instance.
* `NoOpModifier` - A `Modifier` that does nothing.
* `SimplexNoiseNormalMapModifier` - A `Modifier` that performs normal mapping using Simplex noise.

#### The Scene Texture API
* `BlendTexture` - Returns a `Color3F` by blending two `Texture` instances together.
* `BullseyeTexture` - Returns a `Color3F` by alternating between two `Texture` instances in a bullseye pattern.
* `CheckerboardTexture` - Returns a `Color3F` by alternating between two `Texture` instances in a checkerboard pattern.
* `ConstantTexture` - Returns a `Color3F` that is stored as a constant.
* `FunctionTexture` - Returns a `Color3F` by applying an `Intersection` to a `Function`.
* `LDRImageTexture` - Returns a `Color3F` by retrieving it from a low-dynamic-range (LDR) image.
* `MarbleTexture` - Returns a `Color3F` by alternating between three `Color3F` instances in a marble pattern.
* `SimplexFractionalBrownianMotionTexture` - Returns a `Color3F` using a Simplex noise-based fractional Brownian motion (fBm) algorithm.
* `SurfaceNormalTexture` - Returns a `Color3F` using the surface normal.
* `Texture` - A texture assigned to a specific `Material` instance.
* `UVTexture` - Returns a `Color3F` using the texture coordinates (UV-coordinates).

Packages
--------
* `org.dayflower.scene` - The Scene API.
* `org.dayflower.scene.bssrdf` - The Scene BSSRDF API.
* `org.dayflower.scene.bxdf` - The Scene BXDF API.
* `org.dayflower.scene.fresnel` - The Scene Fresnel API.
* `org.dayflower.scene.light` - The Scene Light API.
* `org.dayflower.scene.loader` - The Scene Loader API.
* `org.dayflower.scene.material` - The Scene Material API.
* `org.dayflower.scene.microfacet` - The Scene Microfacet API.
* `org.dayflower.scene.modifier` - The Scene Modifier API.
* `org.dayflower.scene.preview` - The Scene Preview API.
* `org.dayflower.scene.texture` - The Scene Texture API.

Examples
--------
Below follows a few examples that demonstrates various features.

#### Scene Radiance Example
The following example demonstrates a simple way to render an image using the Color, Geometry, Image and Scene APIs.
```java
import org.dayflower.color.Color3F;
import org.dayflower.geometry.AngleF;
import org.dayflower.geometry.Point3F;
import org.dayflower.geometry.shape.Plane3F;
import org.dayflower.image.PixelImageF;
import org.dayflower.scene.Camera;
import org.dayflower.scene.Primitive;
import org.dayflower.scene.Scene;
import org.dayflower.scene.light.PerezLight;
import org.dayflower.scene.material.MatteMaterial;

public class SceneRadianceExample {
    public static void main(String[] args) {
        Camera camera = new Camera(new Point3F(0.0F, 1.0F, 0.0F), AngleF.degrees(40.0F));
        camera.setResolution(800.0F, 800.0F);
        camera.setFieldOfViewY();
        camera.setOrthonormalBasis();
        
        Scene scene = new Scene(camera);
        scene.addLight(new PerezLight());
        scene.addPrimitive(new Primitive(new MatteMaterial(), new Plane3F()));
        
        PixelImageF pixelImageF = new PixelImageF(800, 800);
        pixelImageF.getPixels().forEach(pixel -> camera.createPrimaryRay(pixel.getX(), pixel.getY(), 0.5F, 0.5F).ifPresent(ray -> {
            pixelImageF.filmAddColorXYZ(pixel.getX() + 0.5F, pixel.getY() + 0.5F, Color3F.convertRGBToXYZUsingPBRT(scene.radiancePathTracer(ray)));
        }));
        pixelImageF.filmRender();
        pixelImageF.save("SceneRadianceExample.png", "png");
    }
}
```

#### Clear Coat Material Example
The following example demonstrates how the `ClearCoatMaterial` can be created and looks visually.
```java
new ClearCoatMaterial(new Color3F(0.5F, 0.01F, 0.01F));
```
![alt text](https://github.com/macroing/Dayflower/blob/master/documentation/Scene/ClearCoatMaterial.png "ClearCoatMaterial")

#### Disney Material Example
The following example demonstrates how the `DisneyMaterial` can be created and looks visually.
```java
new DisneyMaterial(new Color3F(255, 127, 80), Color3F.BLACK, Color3F.BLACK, 0.4F, 1.75F, 1.0F, 1.0F, 1.2F, 0.0F, 0.39F, 0.475F, 0.1F, 0.5F, 0.5F, 0.0F);
```
![alt text](https://github.com/macroing/Dayflower/blob/master/documentation/Scene/DisneyMaterial.png "DisneyMaterial")

#### Glass Material Example
The following example demonstrates how the `GlassMaterial` can be created and looks visually.
```java
new GlassMaterial();
```
![alt text](https://github.com/macroing/Dayflower/blob/master/documentation/Scene/GlassMaterial.png "GlassMaterial")

#### Glossy Material Example
The following example demonstrates how the `GlossyMaterial` can be created and looks visually.
```java
new GlossyMaterial(new Color3F(0.01F, 0.5F, 0.01F), Color3F.BLACK, 0.1F);
```
![alt text](https://github.com/macroing/Dayflower/blob/master/documentation/Scene/GlossyMaterial.png "GlossyMaterial")

#### Matte Material Example
The following example demonstrates how the `MatteMaterial` can be created and looks visually.
```java
new MatteMaterial();
```
![alt text](https://github.com/macroing/Dayflower/blob/master/documentation/Scene/MatteMaterial.png "MatteMaterial")

#### Metal Material Example
The following example demonstrates how the `MetalMaterial` can be created and looks visually.
```java
new MetalMaterial();
```
![alt text](https://github.com/macroing/Dayflower/blob/master/documentation/Scene/MetalMaterial.png "MetalMaterial")

#### Mirror Material Example
The following example demonstrates how the `MirrorMaterial` can be created and looks visually.
```java
new MirrorMaterial(Color3F.GRAY_0_50);
```
![alt text](https://github.com/macroing/Dayflower/blob/master/documentation/Scene/MirrorMaterial.png "MirrorMaterial")

#### Plastic Material Example
The following example demonstrates how the `PlasticMaterial` can be created and looks visually.
```java
new PlasticMaterial(new Color3F(0.05F, 0.05F, 1.0F), Color3F.GRAY_0_50);
```
![alt text](https://github.com/macroing/Dayflower/blob/master/documentation/Scene/PlasticMaterial.png "PlasticMaterial")

#### Substrate Material Example
The following example demonstrates how the `SubstrateMaterial` can be created and looks visually.
```java
new SubstrateMaterial(new Color3F(1.0F, 0.2F, 0.2F));
```
![alt text](https://github.com/macroing/Dayflower/blob/master/documentation/Scene/SubstrateMaterial.png "SubstrateMaterial")