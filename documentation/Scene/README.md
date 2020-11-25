The Scene API
=============
The Scene API provides the scene types and operations for Dayflower.

Supported Features
------------------
Below follows a few lists of features available in the Scene API.

#### The Scene API
* `AreaLight` - A `Light` that represents an area light.
* `Camera` - A camera from which an image can be formed. It is associated with a `Scene` instance.
* `Light` - A light source that emits light. It is associated with a `Scene` instance.
* `Material` - A material assigned to a specific `Primitive` instance.
* `Primitive` - A primitive (or object) that can be rendered. It is associated with a `Scene` instance.
* `Scene` - Contains a `Camera`, a list of `Light` instances and a list of `Primitive` instances.
* `SceneLoader` - Loads a `Scene` instance.
* `Texture` - A texture assigned to a specific `Primitive` instance or a specific `Material` instance.

#### The Scene Loader API
* `JavaSceneLoader` - A `SceneLoader` that produces `Scene` instances by loading, compiling and executing snippets of Java source code.

#### The Scene PBRT API
* `GlassMaterial` - A `Material` that represents glass.
* `HairMaterial` - A `Material` that represents hair.
* `MatteMaterial` - A `Material` that is used for matte surfaces.
* `MetalMaterial` - A `Material` that represents metal.
* `MirrorMaterial` - A `Material` that represents a mirror.
* `PlasticMaterial` - A `Material` that represents plastic.
* `SubstrateMaterial` - A `Material` that represents a substrate material.
* `UberMaterial` - A `Material` that can represent a wide variety of materials.

#### The Scene Texture API
* `BlendTexture` - Returns a `Color3F` by blending two `Texture` instances together.
* `BullseyeTexture` - Returns a `Color3F` by alternating between two `Texture` instances in a bullseye pattern.
* `CheckerboardTexture` - Returns a `Color3F` by alternating between two `Texture` instances in a checkerboard pattern.
* `ConstantTexture` - Returns a `Color3F` that is stored as a constant.
* `FunctionTexture` - Returns a `Color3F` by applying an `Intersection` to a `Function` that returns a `Texture`.
* `ImageTexture` - Returns a `Color3F` by retrieving it from an image.
* `MarbleTexture` - Returns a `Color3F` by alternating between three `Color3F` instances in a marble pattern.
* `SimplexFractionalBrownianMotionTexture` - Returns a `Color3F` using a Simplex noise-based fractional Brownian motion (fBm) algorithm.
* `SurfaceNormalTexture` - Returns a `Color3F` using the surface normal.
* `UVTexture` - Returns a `Color3F` using the texture coordinates (UV-coordinates).

Packages
--------
* `org.dayflower.scene` - The Scene API.
* `org.dayflower.scene.background` - The Scene Background API.
* `org.dayflower.scene.light` - The Scene Light API.
* `org.dayflower.scene.loader` - The Scene Loader API.
* `org.dayflower.scene.pbrt` - The Scene PBRT API.
* `org.dayflower.scene.rayito` - The Scene Rayito API.
* `org.dayflower.scene.texture` - The Scene Texture API.

Examples
--------
Coming soon...