The Scene API
=============
The Scene API provides the scene types and operations for Dayflower.

Supported Features
------------------
Below follows a few lists of features available in the Scene API.

#### Textures
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