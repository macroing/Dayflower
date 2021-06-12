/**
 * Provides the Geometry API.
 * <p>
 * The Geometry API provides data types for angles, bounding volumes, matrices, orthonormal bases, points, quaternions, rays, shapes and vectors.
 * <h3>Overview</h3>
 * <p>
 * The following list contains information about the data types that represents angles in this API.
 * <ul>
 * <li>{@link org.dayflower.geometry.AngleD AngleD} represents an angle and contains {@code double}-based values for both degrees and radians.</li>
 * <li>{@link org.dayflower.geometry.AngleF AngleF} represents an angle and contains {@code float}-based values for both degrees and radians.</li>
 * </ul>
 * <p>
 * The following list contains information about the data types that represents bounding volumes in this API.
 * <ul>
 * <li>{@link org.dayflower.geometry.BoundingVolume BoundingVolume} represents a bounding volume.</li>
 * <li>{@link org.dayflower.geometry.BoundingVolumeReader BoundingVolumeReader} is used for reading {@code BoundingVolume} instances from a {@code DataInput} instance.</li>
 * <li>{@link org.dayflower.geometry.BoundingVolume3D BoundingVolume3D} is a 3-dimensional extension of {@code BoundingVolume} that adds additional methods that operates on {@code double}-based data types.</li>
 * <li>{@link org.dayflower.geometry.BoundingVolume3DReader BoundingVolume3DReader} is an extension of {@code BoundingVolumeReader} and is used for reading {@code BoundingVolume3D} instances from a {@code DataInput} instance.</li>
 * <li>{@link org.dayflower.geometry.BoundingVolume3F BoundingVolume3F} is a 3-dimensional extension of {@code BoundingVolume} that adds additional methods that operates on {@code float}-based data types.</li>
 * <li>{@link org.dayflower.geometry.BoundingVolume3FReader BoundingVolume3FReader} is an extension of {@code BoundingVolumeReader} and is used for reading {@code BoundingVolume3F} instances from a {@code DataInput} instance.</li>
 * </ul>
 * <p>
 * The following list contains information about the data types that represents matrices in this API.
 * <ul>
 * <li>{@link org.dayflower.geometry.Matrix33D Matrix33D} represents a 3 x 3 matrix with 9 {@code double}-based elements.</li>
 * <li>{@link org.dayflower.geometry.Matrix33F Matrix33F} represents a 3 x 3 matrix with 9 {@code float}-based elements.</li>
 * <li>{@link org.dayflower.geometry.Matrix44D Matrix44D} represents a 4 x 4 matrix with 16 {@code double}-based elements.</li>
 * <li>{@link org.dayflower.geometry.Matrix44F Matrix44F} represents a 4 x 4 matrix with 16 {@code float}-based elements.</li>
 * </ul>
 * <p>
 * The following list contains information about the data types that represents orthonormal bases in this API.
 * <ul>
 * <li>{@link org.dayflower.geometry.OrthonormalBasis33D OrthonormalBasis33D} represents an orthonormal basis constructed by three {@code Vector3D} instances.</li>
 * <li>{@link org.dayflower.geometry.OrthonormalBasis33F OrthonormalBasis33F} represents an orthonormal basis constructed by three {@code Vector3F} instances.</li>
 * </ul>
 * <p>
 * The following list contains information about the data types that represents points in this API.
 * <ul>
 * <li>{@link org.dayflower.geometry.Point2D Point2D} represents a point with two {@code double}-based components.</li>
 * <li>{@link org.dayflower.geometry.Point2F Point2F} represents a point with two {@code float}-based components.</li>
 * <li>{@link org.dayflower.geometry.Point2I Point2I} represents a point with two {@code int}-based components.</li>
 * <li>{@link org.dayflower.geometry.Point3D Point3D} represents a point with three {@code double}-based components.</li>
 * <li>{@link org.dayflower.geometry.Point3F Point3F} represents a point with three {@code float}-based components.</li>
 * <li>{@link org.dayflower.geometry.Point4D Point4D} represents a point with four {@code double}-based components.</li>
 * <li>{@link org.dayflower.geometry.Point4F Point4F} represents a point with four {@code float}-based components.</li>
 * </ul>
 * <p>
 * The following list contains information about the data types that represents quaternions in this API.
 * <ul>
 * <li>{@link org.dayflower.geometry.Quaternion4D Quaternion4D} represents a quaternion with four {@code double}-based components.</li>
 * <li>{@link org.dayflower.geometry.Quaternion4F Quaternion4F} represents a quaternion with four {@code float}-based components.</li>
 * </ul>
 * <p>
 * The following list contains information about the data types that represents rays in this API.
 * <ul>
 * <li>{@link org.dayflower.geometry.Ray2D Ray2D} represents a 2-dimensional ray with a point of type {@code Point2D} and a vector of type {@code Vector2D}.</li>
 * <li>{@link org.dayflower.geometry.Ray2F Ray2F} represents a 2-dimensional ray with a point of type {@code Point2F} and a vector of type {@code Vector2F}.</li>
 * <li>{@link org.dayflower.geometry.Ray3D Ray3D} represents a 3-dimensional ray with a point of type {@code Point3D} and a vector of type {@code Vector3D}.</li>
 * <li>{@link org.dayflower.geometry.Ray3F Ray3F} represents a 3-dimensional ray with a point of type {@code Point3F} and a vector of type {@code Vector3F}.</li>
 * </ul>
 * <p>
 * The following list contains information about the data types that represents shapes in this API.
 * <ul>
 * <li>{@link org.dayflower.geometry.Shape Shape} represents a shape.</li>
 * <li>{@link org.dayflower.geometry.ShapeReader ShapeReader} is used for reading {@code Shape} instances from a {@code DataInput} instance.</li>
 * <li>{@link org.dayflower.geometry.Shape2D Shape2D} is a 2-dimensional extension of {@code Shape} that adds additional methods that operates on {@code double}-based data types.</li>
 * <li>{@link org.dayflower.geometry.Shape2DReader Shape2DReader} is an extension of {@code ShapeReader} and is used for reading {@code Shape2D} instances from a {@code DataInput} instance.</li>
 * <li>{@link org.dayflower.geometry.Shape2F Shape2F} is a 2-dimensional extension of {@code Shape} that adds additional methods that operates on {@code float}-based data types.</li>
 * <li>{@link org.dayflower.geometry.Shape2FReader Shape2FReader} is an extension of {@code ShapeReader} and is used for reading {@code Shape2F} instances from a {@code DataInput} instance.</li>
 * <li>{@link org.dayflower.geometry.Shape2I Shape2I} is a 2-dimensional extension of {@code Shape} that adds additional methods that operates on {@code int}-based data types.</li>
 * <li>{@link org.dayflower.geometry.Shape2IReader Shape2IReader} is an extension of {@code ShapeReader} and is used for reading {@code Shape2I} instances from a {@code DataInput} instance.</li>
 * <li>{@link org.dayflower.geometry.Shape3D Shape3D} is a 3-dimensional extension of {@code Shape} that adds additional methods that operates on {@code double}-based data types.</li>
 * <li>{@link org.dayflower.geometry.Shape3DReader Shape3DReader} is an extension of {@code ShapeReader} and is used for reading {@code Shape3D} instances from a {@code DataInput} instance.</li>
 * <li>{@link org.dayflower.geometry.Shape3F Shape3F} is a 3-dimensional extension of {@code Shape} that adds additional methods that operates on {@code float}-based data types.</li>
 * <li>{@link org.dayflower.geometry.Shape3FReader Shape3FReader} is an extension of {@code ShapeReader} and is used for reading {@code Shape3F} instances from a {@code DataInput} instance.</li>
 * </ul>
 * <p>
 * The following list contains information about the data types that represents vectors in this API.
 * <ul>
 * <li>{@link org.dayflower.geometry.Vector2D Vector2D} represents a vector with two {@code double}-based components.</li>
 * <li>{@link org.dayflower.geometry.Vector2F Vector2F} represents a vector with two {@code float}-based components.</li>
 * <li>{@link org.dayflower.geometry.Vector2I Vector2I} represents a vector with two {@code int}-based components.</li>
 * <li>{@link org.dayflower.geometry.Vector3D Vector3D} represents a vector with three {@code double}-based components.</li>
 * <li>{@link org.dayflower.geometry.Vector3F Vector3F} represents a vector with three {@code float}-based components.</li>
 * <li>{@link org.dayflower.geometry.Vector4D Vector4D} represents a vector with four {@code double}-based components.</li>
 * <li>{@link org.dayflower.geometry.Vector4F Vector4F} represents a vector with four {@code float}-based components.</li>
 * </ul>
 * <p>
 * The following list contains information about the remaining data types.
 * <ul>
 * <li>{@link org.dayflower.geometry.SampleGeneratorD SampleGeneratorD} contains methods to generate {@code double}-based samples.</li>
 * <li>{@link org.dayflower.geometry.SampleGeneratorF SampleGeneratorF} contains methods to generate {@code float}-based samples.</li>
 * <li>{@link org.dayflower.geometry.SurfaceIntersection3D SurfaceIntersection3D} contains information about the surface of a {@code Shape3D} instance where a {@code Ray3D} instance intersects.</li>
 * <li>{@link org.dayflower.geometry.SurfaceIntersection3F SurfaceIntersection3F} contains information about the surface of a {@code Shape3F} instance where a {@code Ray3F} instance intersects.</li>
 * <li>{@link org.dayflower.geometry.SurfaceIntersector3D SurfaceIntersector3D} is an utility class that is useful for performing intersection tests on {@code Shape3D} instances.</li>
 * <li>{@link org.dayflower.geometry.SurfaceIntersector3F SurfaceIntersector3F} is an utility class that is useful for performing intersection tests on {@code Shape3F} instances.</li>
 * <li>{@link org.dayflower.geometry.SurfaceSample3D SurfaceSample3D} contains information about the surface of a {@code Shape3D} instance where it is being sampled.</li>
 * <li>{@link org.dayflower.geometry.SurfaceSample3F SurfaceSample3F} contains information about the surface of a {@code Shape3F} instance where it is being sampled.</li>
 * </ul>
 */
package org.dayflower.geometry;