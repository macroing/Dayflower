Dayflower
=========
Dayflower is a renderer written in Java.

![alt text](https://github.com/macroing/Dayflower/blob/master/images/Image-002.png "Dayflower - Path Tracer")
![alt text](https://github.com/macroing/Dayflower/blob/master/images/Image-007.png "Dayflower - Path Tracer")

Getting Started
---------------
To clone this repository, you can type the following in Git Bash.

```bash
git clone https://github.com/macroing/Dayflower.git
```

Supported Features
------------------
#### BRDF
* Ashikhmin-Shirley
* Lambertian
* Oren-Nayar
* Reflection

#### BTDF
* Refraction

#### Background
* Constant
* Image
* Perez

#### Camera Lens
* Fisheye
* Thin

#### Filter
* Box
* Catmull Rom
* Gaussian
* Lanczos Sinc
* Mitchell
* Triangle

#### Light
* Directional
* Point
* Primitive

#### Rendering Algorithms
* Ambient Occlusion
* Path Tracer
* Ray Caster

#### Shapes
* Plane
* Rectangular Cuboid
* Sphere
* Torus
* Triangle
* Triangle Mesh with Bounding Volume Hierarchy (BVH) based on the Surface Area Heuristic (SAH)

#### Textures
* Blend
* Bullseye
* Checkerboard
* Constant
* Image
* Simplex Fractional Brownian Motion
* Surface Normal
* UV

Dependencies
------------
 - [Java 8](http://www.java.com)

Note
----
This library has not reached version 1.0.0 and been released to the public yet. Therefore, you can expect that backward incompatible changes are likely to occur between commits. When this library reaches version 1.0.0, it will be tagged and available on the "releases" page. At that point, backward incompatible changes should only occur when a new major release is made.