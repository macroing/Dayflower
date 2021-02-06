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

#### The Scene Loader API
* `JavaSceneLoader` - A `SceneLoader` that produces `Scene` instances by loading, compiling and executing snippets of Java source code.

#### The Scene Material API
* `ClearCoatMaterial` - A `Material` that represents clear coat.
* `DisneyMaterial` - A `Material` that represents a Disney material.
* `GlassMaterial` - A `Material` that represents glass.
* `HairMaterial` - A `Material` that represents hair.
* `MatteMaterial` - A `Material` that is used for matte surfaces.
* `MetalMaterial` - A `Material` that represents metal.
* `MirrorMaterial` - A `Material` that represents a mirror.
* `PlasticMaterial` - A `Material` that represents plastic.
* `SubstrateMaterial` - A `Material` that represents a substrate material.
* `UberMaterial` - A `Material` that can represent a wide variety of materials.

#### The Scene Material Rayito API
* `GlassRayitoMaterial` - A `Material` that represents glass.
* `MatteRayitoMaterial` - A `Material` that is used for matte surfaces.
* `MetalRayitoMaterial` - A `Material` that represents metal.
* `MirrorRayitoMaterial` - A `Material` that represents a mirror.

#### The Scene Material SmallPT API
* `ClearCoatSmallPTMaterial` - A `Material` that represents clear coat.
* `GlassSmallPTMaterial` - A `Material` that represents glass.
* `MatteSmallPTMaterial` - A `Material` that is used for matte surfaces.
* `MetalSmallPTMaterial` - A `Material` that represents metal.
* `MirrorSmallPTMaterial` - A `Material` that represents a mirror.

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
* `org.dayflower.scene.material.rayito` - The Scene Material Rayito API.
* `org.dayflower.scene.material.smallpt` - The Scene Material SmallPT API.
* `org.dayflower.scene.microfacet` - The Scene Microfacet API.
* `org.dayflower.scene.preview` - The Scene Preview API.
* `org.dayflower.scene.texture` - The Scene Texture API.

Examples
--------
Below follows a few examples that demonstrates various features.

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

#### Metal Material Example
The following example demonstrates how the `MetalMaterial` can be created and looks visually.
```java
new MetalMaterial();
```
![alt text](https://github.com/macroing/Dayflower/blob/master/documentation/Scene/MetalMaterial.png "MetalMaterial")

#### Plastic Material Example
The following example demonstrates how the `PlasticMaterial` can be created and looks visually.
```java
new PlasticMaterial(new Color3F(0.2F, 0.2F, 1.0F), Color3F.GRAY_0_50, Color3F.BLACK, 0.01F);
```
![alt text](https://github.com/macroing/Dayflower/blob/master/documentation/Scene/PlasticMaterial.png "PlasticMaterial")

#### Substrate Material Example
The following example demonstrates how the `SubstrateMaterial` can be created and looks visually.
```java
new SubstrateMaterial(new Color3F(1.0F, 0.2F, 0.2F));
```
![alt text](https://github.com/macroing/Dayflower/blob/master/documentation/Scene/SubstrateMaterial.png "SubstrateMaterial")