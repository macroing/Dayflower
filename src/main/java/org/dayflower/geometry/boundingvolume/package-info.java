/**
 * Provides the Geometry Bounding Volume API.
 * <p>
 * The Geometry Bounding Volume API provides implementations for the data types {@link org.dayflower.geometry.BoundingVolume BoundingVolume} and {@link org.dayflower.geometry.BoundingVolumeReader BoundingVolumeReader}.
 * <h3>Overview</h3>
 * <p>
 * The following list contains information about the data types that represents axis-aligned bounding boxes (AABBs) in this API.
 * <ul>
 * <li>{@link org.dayflower.geometry.boundingvolume.AxisAlignedBoundingBox3D AxisAlignedBoundingBox3D} is an implementation of {@code BoundingVolume3D} that represents an axis-aligned bounding box (AABB).</li>
 * <li>{@link org.dayflower.geometry.boundingvolume.AxisAlignedBoundingBox3DReader AxisAlignedBoundingBox3DReader} is a {@code BoundingVolume3DReader} implementation that reads {@code AxisAlignedBoundingBox3D} instances from a {@code DataInput} instance.</li>
 * <li>{@link org.dayflower.geometry.boundingvolume.AxisAlignedBoundingBox3F AxisAlignedBoundingBox3F} is an implementation of {@code BoundingVolume3F} that represents an axis-aligned bounding box (AABB).</li>
 * <li>{@link org.dayflower.geometry.boundingvolume.AxisAlignedBoundingBox3FReader AxisAlignedBoundingBox3FReader} is a {@code BoundingVolume3FReader} implementation that reads {@code AxisAlignedBoundingBox3F} instances from a {@code DataInput} instance.</li>
 * </ul>
 * <p>
 * The following list contains information about the data types that represents bounding spheres in this API.
 * <ul>
 * <li>{@link org.dayflower.geometry.boundingvolume.BoundingSphere3D BoundingSphere3D} is an implementation of {@code BoundingVolume3D} that represents a bounding sphere.</li>
 * <li>{@link org.dayflower.geometry.boundingvolume.BoundingSphere3DReader BoundingSphere3DReader} is a {@code BoundingVolume3DReader} implementation that reads {@code BoundingSphere3D} instances from a {@code DataInput} instance.</li>
 * <li>{@link org.dayflower.geometry.boundingvolume.BoundingSphere3F BoundingSphere3F} is an implementation of {@code BoundingVolume3F} that represents a bounding sphere.</li>
 * <li>{@link org.dayflower.geometry.boundingvolume.BoundingSphere3FReader BoundingSphere3FReader} is a {@code BoundingVolume3FReader} implementation that reads {@code BoundingSphere3F} instances from a {@code DataInput} instance.</li>
 * </ul>
 * <p>
 * The following list contains information about the data types that represents infinite bounding volumes in this API.
 * <ul>
 * <li>{@link org.dayflower.geometry.boundingvolume.InfiniteBoundingVolume3D InfiniteBoundingVolume3D} is an implementation of {@code BoundingVolume3D} that represents an infinite bounding volume.</li>
 * <li>{@link org.dayflower.geometry.boundingvolume.InfiniteBoundingVolume3DReader InfiniteBoundingVolume3DReader} is a {@code BoundingVolume3DReader} implementation that reads {@code InfiniteBoundingVolume3D} instances from a {@code DataInput} instance.</li>
 * <li>{@link org.dayflower.geometry.boundingvolume.InfiniteBoundingVolume3F InfiniteBoundingVolume3F} is an implementation of {@code BoundingVolume3F} that represents an infinite bounding volume.</li>
 * <li>{@link org.dayflower.geometry.boundingvolume.InfiniteBoundingVolume3FReader InfiniteBoundingVolume3FReader} is a {@code BoundingVolume3FReader} implementation that reads {@code InfiniteBoundingVolume3F} instances from a {@code DataInput} instance.</li>
 * </ul>
 * <p>
 * The following list contains information about the remaining data types.
 * <ul>
 * <li>{@link org.dayflower.geometry.boundingvolume.DefaultBoundingVolume3DReader DefaultBoundingVolume3DReader} is a {@code BoundingVolume3DReader} implementation that reads all official {@code BoundingVolume3D} instances from a {@code DataInput} instance.</li>
 * <li>{@link org.dayflower.geometry.boundingvolume.DefaultBoundingVolume3FReader DefaultBoundingVolume3FReader} is a {@code BoundingVolume3FReader} implementation that reads all official {@code BoundingVolume3F} instances from a {@code DataInput} instance.</li>
 * </ul>
 * <h3>Dependencies</h3>
 * <p>
 * The following list shows all dependencies for this API.
 * <ul>
 * <li>The Geometry API</li>
 * <li>The Node API</li>
 * <li>The Utility API</li>
 * </ul>
 */
package org.dayflower.geometry.boundingvolume;