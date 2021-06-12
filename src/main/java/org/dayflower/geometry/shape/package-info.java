/**
 * Provides the Geometry Shape API.
 * <p>
 * The Geometry Shape API provides implementations for the data types {@link org.dayflower.geometry.Shape Shape} and {@link org.dayflower.geometry.ShapeReader ShapeReader}.
 * <h3>Overview</h3>
 * <p>
 * The following list contains information about the data types that represents circles in this API.
 * <ul>
 * <li>{@link org.dayflower.geometry.shape.Circle2D Circle2D} is an implementation of {@code Shape2D} that represents a circle.</li>
 * <li>{@link org.dayflower.geometry.shape.Circle2DReader Circle2DReader} is a {@code Shape2DReader} implementation that reads {@code Circle2D} instances from a {@code DataInput} instance.</li>
 * <li>{@link org.dayflower.geometry.shape.Circle2F Circle2F} is an implementation of {@code Shape2F} that represents a circle.</li>
 * <li>{@link org.dayflower.geometry.shape.Circle2FReader Circle2FReader} is a {@code Shape2FReader} implementation that reads {@code Circle2F} instances from a {@code DataInput} instance.</li>
 * <li>{@link org.dayflower.geometry.shape.Circle2I Circle2I} is an implementation of {@code Shape2I} that represents a circle.</li>
 * <li>{@link org.dayflower.geometry.shape.Circle2IReader Circle2IReader} is a {@code Shape2IReader} implementation that reads {@code Circle2I} instances from a {@code DataInput} instance.</li>
 * </ul>
 * <p>
 * The following list contains information about the data types that represents cones in this API.
 * <ul>
 * <li>{@link org.dayflower.geometry.shape.Cone3D Cone3D} is an implementation of {@code Shape3D} that represents a cone.</li>
 * <li>{@link org.dayflower.geometry.shape.Cone3DReader Cone3DReader} is a {@code Shape3DReader} implementation that reads {@code Cone3D} instances from a {@code DataInput} instance.</li>
 * <li>{@link org.dayflower.geometry.shape.Cone3F Cone3F} is an implementation of {@code Shape3F} that represents a cone.</li>
 * <li>{@link org.dayflower.geometry.shape.Cone3FReader Cone3FReader} is a {@code Shape3FReader} implementation that reads {@code Cone3F} instances from a {@code DataInput} instance.</li>
 * </ul>
 * <p>
 * The following list contains information about the data types that can be used for constructive solid geometry (CSG) in this API.
 * <ul>
 * <li>{@link org.dayflower.geometry.shape.ConstructiveSolidGeometry3D ConstructiveSolidGeometry3D} is an implementation of {@code Shape3D} that can be used for constructive solid geometry (CSG).</li>
 * <li>{@link org.dayflower.geometry.shape.ConstructiveSolidGeometry3DReader ConstructiveSolidGeometry3DReader} is a {@code Shape3DReader} implementation that reads {@code ConstructiveSolidGeometry3D} instances from a {@code DataInput} instance.</li>
 * <li>{@link org.dayflower.geometry.shape.ConstructiveSolidGeometry3F ConstructiveSolidGeometry3F} is an implementation of {@code Shape3F} that can be used for constructive solid geometry (CSG).</li>
 * <li>{@link org.dayflower.geometry.shape.ConstructiveSolidGeometry3FReader ConstructiveSolidGeometry3FReader} is a {@code Shape3FReader} implementation that reads {@code ConstructiveSolidGeometry3F} instances from a {@code DataInput} instance.</li>
 * </ul>
 * <p>
 * The following list contains information about the data types that represents curves in this API.
 * <ul>
 * <li>{@link org.dayflower.geometry.shape.Curve3D Curve3D} is an implementation of {@code Shape3D} that represents a curve.</li>
 * <li>{@link org.dayflower.geometry.shape.Curve3DReader Curve3DReader} is a {@code Shape3DReader} implementation that reads {@code Curve3D} instances from a {@code DataInput} instance.</li>
 * <li>{@link org.dayflower.geometry.shape.Curve3F Curve3F} is an implementation of {@code Shape3F} that represents a curve.</li>
 * <li>{@link org.dayflower.geometry.shape.Curve3FReader Curve3FReader} is a {@code Shape3FReader} implementation that reads {@code Curve3F} instances from a {@code DataInput} instance.</li>
 * <li>{@link org.dayflower.geometry.shape.Curves3D Curves3D} is an implementation of {@code Shape3D} that contains a list of {@code Curve3D} instances.</li>
 * <li>{@link org.dayflower.geometry.shape.Curves3DReader Curves3DReader} is a {@code Shape3DReader} implementation that reads {@code Curves3D} instances from a {@code DataInput} instance.</li>
 * <li>{@link org.dayflower.geometry.shape.Curves3F Curves3F} is an implementation of {@code Shape3F} that contains a list of {@code Curve3F} instances.</li>
 * <li>{@link org.dayflower.geometry.shape.Curves3FReader Curves3FReader} is a {@code Shape3FReader} implementation that reads {@code Curves3F} instances from a {@code DataInput} instance.</li>
 * </ul>
 * <p>
 * The following list contains information about the data types that represents cylinders in this API.
 * <ul>
 * <li>{@link org.dayflower.geometry.shape.Cylinder3D Cylinder3D} is an implementation of {@code Shape3D} that represents a cylinder.</li>
 * <li>{@link org.dayflower.geometry.shape.Cylinder3DReader Cylinder3DReader} is a {@code Shape3DReader} implementation that reads {@code Cylinder3D} instances from a {@code DataInput} instance.</li>
 * <li>{@link org.dayflower.geometry.shape.Cylinder3F Cylinder3F} is an implementation of {@code Shape3F} that represents a cylinder.</li>
 * <li>{@link org.dayflower.geometry.shape.Cylinder3FReader Cylinder3FReader} is a {@code Shape3FReader} implementation that reads {@code Cylinder3F} instances from a {@code DataInput} instance.</li>
 * </ul>
 * <p>
 * The following list contains information about the data types that represents disks in this API.
 * <ul>
 * <li>{@link org.dayflower.geometry.shape.Disk3D Disk3D} is an implementation of {@code Shape3D} that represents a disk.</li>
 * <li>{@link org.dayflower.geometry.shape.Disk3DReader Disk3DReader} is a {@code Shape3DReader} implementation that reads {@code Disk3D} instances from a {@code DataInput} instance.</li>
 * <li>{@link org.dayflower.geometry.shape.Disk3F Disk3F} is an implementation of {@code Shape3F} that represents a disk.</li>
 * <li>{@link org.dayflower.geometry.shape.Disk3FReader Disk3FReader} is a {@code Shape3FReader} implementation that reads {@code Disk3F} instances from a {@code DataInput} instance.</li>
 * </ul>
 * <p>
 * The following list contains information about the data types that represents hyperboloids in this API.
 * <ul>
 * <li>{@link org.dayflower.geometry.shape.Hyperboloid3D Hyperboloid3D} is an implementation of {@code Shape3D} that represents a hyperboloid.</li>
 * <li>{@link org.dayflower.geometry.shape.Hyperboloid3DReader Hyperboloid3DReader} is a {@code Shape3DReader} implementation that reads {@code Hyperboloid3D} instances from a {@code DataInput} instance.</li>
 * <li>{@link org.dayflower.geometry.shape.Hyperboloid3F Hyperboloid3F} is an implementation of {@code Shape3F} that represents a hyperboloid.</li>
 * <li>{@link org.dayflower.geometry.shape.Hyperboloid3FReader Hyperboloid3FReader} is a {@code Shape3FReader} implementation that reads {@code Hyperboloid3F} instances from a {@code DataInput} instance.</li>
 * </ul>
 * <p>
 * The following list contains information about the data types that represents lines in this API.
 * <ul>
 * <li>{@link org.dayflower.geometry.shape.Line2D Line2D} is an implementation of {@code Shape2D} that represents a line.</li>
 * <li>{@link org.dayflower.geometry.shape.Line2DReader Line2DReader} is a {@code Shape2DReader} implementation that reads {@code Line2D} instances from a {@code DataInput} instance.</li>
 * <li>{@link org.dayflower.geometry.shape.Line2F Line2F} is an implementation of {@code Shape2F} that represents a line.</li>
 * <li>{@link org.dayflower.geometry.shape.Line2FReader Line2FReader} is a {@code Shape2FReader} implementation that reads {@code Line2F} instances from a {@code DataInput} instance.</li>
 * <li>{@link org.dayflower.geometry.shape.Line2I Line2I} is an implementation of {@code Shape2I} that represents a line.</li>
 * <li>{@link org.dayflower.geometry.shape.Line2IReader Line2IReader} is a {@code Shape2IReader} implementation that reads {@code Line2I} instances from a {@code DataInput} instance.</li>
 * </ul>
 * <p>
 * The following list contains information about the data types that represents paraboloids in this API.
 * <ul>
 * <li>{@link org.dayflower.geometry.shape.Paraboloid3D Paraboloid3D} is an implementation of {@code Shape3D} that represents a paraboloid.</li>
 * <li>{@link org.dayflower.geometry.shape.Paraboloid3DReader Paraboloid3DReader} is a {@code Shape3DReader} implementation that reads {@code Paraboloid3D} instances from a {@code DataInput} instance.</li>
 * <li>{@link org.dayflower.geometry.shape.Paraboloid3F Paraboloid3F} is an implementation of {@code Shape3F} that represents a paraboloid.</li>
 * <li>{@link org.dayflower.geometry.shape.Paraboloid3FReader Paraboloid3FReader} is a {@code Shape3FReader} implementation that reads {@code Paraboloid3F} instances from a {@code DataInput} instance.</li>
 * </ul>
 * <p>
 * The following list contains information about the data types that represents planes in this API.
 * <ul>
 * <li>{@link org.dayflower.geometry.shape.Plane3D Plane3D} is an implementation of {@code Shape3D} that represents a plane.</li>
 * <li>{@link org.dayflower.geometry.shape.Plane3DReader Plane3DReader} is a {@code Shape3DReader} implementation that reads {@code Plane3D} instances from a {@code DataInput} instance.</li>
 * <li>{@link org.dayflower.geometry.shape.Plane3F Plane3F} is an implementation of {@code Shape3F} that represents a plane.</li>
 * <li>{@link org.dayflower.geometry.shape.Plane3FReader Plane3FReader} is a {@code Shape3FReader} implementation that reads {@code Plane3F} instances from a {@code DataInput} instance.</li>
 * </ul>
 * <p>
 * The following list contains information about the data types that represents procedural terrains in this API.
 * <ul>
 * <li>{@link org.dayflower.geometry.shape.ProceduralTerrain3D ProceduralTerrain3D} is an implementation of {@code Shape3D} that represents a procedural terrain.</li>
 * <li>{@link org.dayflower.geometry.shape.ProceduralTerrain3DReader ProceduralTerrain3DReader} is a {@code Shape3DReader} implementation that reads {@code ProceduralTerrain3D} instances from a {@code DataInput} instance.</li>
 * <li>{@link org.dayflower.geometry.shape.ProceduralTerrain3F ProceduralTerrain3F} is an implementation of {@code Shape3F} that represents a procedural terrain.</li>
 * <li>{@link org.dayflower.geometry.shape.ProceduralTerrain3FReader ProceduralTerrain3FReader} is a {@code Shape3FReader} implementation that reads {@code ProceduralTerrain3F} instances from a {@code DataInput} instance.</li>
 * </ul>
 * <p>
 * The following list contains information about the data types that represents rectangles in this API.
 * <ul>
 * <li>{@link org.dayflower.geometry.shape.Rectangle2D Rectangle2D} is an implementation of {@code Shape2D} that represents a rectangle.</li>
 * <li>{@link org.dayflower.geometry.shape.Rectangle2DReader Rectangle2DReader} is a {@code Shape2DReader} implementation that reads {@code Rectangle2D} instances from a {@code DataInput} instance.</li>
 * <li>{@link org.dayflower.geometry.shape.Rectangle2F Rectangle2F} is an implementation of {@code Shape2F} that represents a rectangle.</li>
 * <li>{@link org.dayflower.geometry.shape.Rectangle2FReader Rectangle2FReader} is a {@code Shape2FReader} implementation that reads {@code Rectangle2F} instances from a {@code DataInput} instance.</li>
 * <li>{@link org.dayflower.geometry.shape.Rectangle2I Rectangle2I} is an implementation of {@code Shape2I} that represents a rectangle.</li>
 * <li>{@link org.dayflower.geometry.shape.Rectangle2IReader Rectangle2IReader} is a {@code Shape2IReader} implementation that reads {@code Rectangle2I} instances from a {@code DataInput} instance.</li>
 * <li>{@link org.dayflower.geometry.shape.Rectangle3D Rectangle3D} is an implementation of {@code Shape3D} that represents a rectangle.</li>
 * <li>{@link org.dayflower.geometry.shape.Rectangle3DReader Rectangle3DReader} is a {@code Shape3DReader} implementation that reads {@code Rectangle3D} instances from a {@code DataInput} instance.</li>
 * <li>{@link org.dayflower.geometry.shape.Rectangle3F Rectangle3F} is an implementation of {@code Shape3F} that represents a rectangle.</li>
 * <li>{@link org.dayflower.geometry.shape.Rectangle3FReader Rectangle3FReader} is a {@code Shape3FReader} implementation that reads {@code Rectangle3F} instances from a {@code DataInput} instance.</li>
 * </ul>
 * <p>
 * The following list contains information about the data types that represents rectangular cuboids in this API.
 * <ul>
 * <li>{@link org.dayflower.geometry.shape.RectangularCuboid3D RectangularCuboid3D} is an implementation of {@code Shape3D} that represents a rectangular cuboid.</li>
 * <li>{@link org.dayflower.geometry.shape.RectangularCuboid3DReader RectangularCuboid3DReader} is a {@code Shape3DReader} implementation that reads {@code RectangularCuboid3D} instances from a {@code DataInput} instance.</li>
 * <li>{@link org.dayflower.geometry.shape.RectangularCuboid3F RectangularCuboid3F} is an implementation of {@code Shape3F} that represents a rectangular cuboid.</li>
 * <li>{@link org.dayflower.geometry.shape.RectangularCuboid3FReader RectangularCuboid3FReader} is a {@code Shape3FReader} implementation that reads {@code RectangularCuboid3F} instances from a {@code DataInput} instance.</li>
 * </ul>
 * <p>
 * The following list contains information about the data types that represents spheres in this API.
 * <ul>
 * <li>{@link org.dayflower.geometry.shape.Sphere3D Sphere3D} is an implementation of {@code Shape3D} that represents a sphere.</li>
 * <li>{@link org.dayflower.geometry.shape.Sphere3DReader Sphere3DReader} is a {@code Shape3DReader} implementation that reads {@code Sphere3D} instances from a {@code DataInput} instance.</li>
 * <li>{@link org.dayflower.geometry.shape.Sphere3F Sphere3F} is an implementation of {@code Shape3F} that represents a sphere.</li>
 * <li>{@link org.dayflower.geometry.shape.Sphere3FReader Sphere3FReader} is a {@code Shape3FReader} implementation that reads {@code Sphere3F} instances from a {@code DataInput} instance.</li>
 * </ul>
 * <p>
 * The following list contains information about the data types that represents torii in this API.
 * <ul>
 * <li>{@link org.dayflower.geometry.shape.Torus3D Torus3D} is an implementation of {@code Shape3D} that represents a torus.</li>
 * <li>{@link org.dayflower.geometry.shape.Torus3DReader Torus3DReader} is a {@code Shape3DReader} implementation that reads {@code Torus3D} instances from a {@code DataInput} instance.</li>
 * <li>{@link org.dayflower.geometry.shape.Torus3F Torus3F} is an implementation of {@code Shape3F} that represents a torus.</li>
 * <li>{@link org.dayflower.geometry.shape.Torus3FReader Torus3FReader} is a {@code Shape3FReader} implementation that reads {@code Torus3F} instances from a {@code DataInput} instance.</li>
 * </ul>
 * <p>
 * The following list contains information about the data types that represents triangles in this API.
 * <ul>
 * <li>{@link org.dayflower.geometry.shape.Triangle2D Triangle2D} is an implementation of {@code Shape2D} that represents a triangle.</li>
 * <li>{@link org.dayflower.geometry.shape.Triangle2DReader Triangle2DReader} is a {@code Shape2DReader} implementation that reads {@code Triangle2D} instances from a {@code DataInput} instance.</li>
 * <li>{@link org.dayflower.geometry.shape.Triangle2F Triangle2F} is an implementation of {@code Shape2F} that represents a triangle.</li>
 * <li>{@link org.dayflower.geometry.shape.Triangle2FReader Triangle2FReader} is a {@code Shape2FReader} implementation that reads {@code Triangle2F} instances from a {@code DataInput} instance.</li>
 * <li>{@link org.dayflower.geometry.shape.Triangle2I Triangle2I} is an implementation of {@code Shape2I} that represents a triangle.</li>
 * <li>{@link org.dayflower.geometry.shape.Triangle2IReader Triangle2IReader} is a {@code Shape2IReader} implementation that reads {@code Triangle2I} instances from a {@code DataInput} instance.</li>
 * <li>{@link org.dayflower.geometry.shape.Triangle3D Triangle3D} is an implementation of {@code Shape3D} that represents a triangle.</li>
 * <li>{@link org.dayflower.geometry.shape.Triangle3DReader Triangle3DReader} is a {@code Shape3DReader} implementation that reads {@code Triangle3D} instances from a {@code DataInput} instance.</li>
 * <li>{@link org.dayflower.geometry.shape.Triangle3F Triangle3F} is an implementation of {@code Shape3F} that represents a triangle.</li>
 * <li>{@link org.dayflower.geometry.shape.Triangle3FReader Triangle3FReader} is a {@code Shape3FReader} implementation that reads {@code Triangle3F} instances from a {@code DataInput} instance.</li>
 * </ul>
 * <p>
 * The following list contains information about the data types that represents triangle meshes in this API.
 * <ul>
 * <li>{@link org.dayflower.geometry.shape.TriangleMesh3D TriangleMesh3D} is an implementation of {@code Shape3D} that represents a triangle mesh.</li>
 * <li>{@link org.dayflower.geometry.shape.TriangleMesh3F TriangleMesh3F} is an implementation of {@code Shape3F} that represents a triangle mesh.</li>
 * </ul>
 * <h3>Dependencies</h3>
 * <p>
 * The following list shows all dependencies for this API.
 * <ul>
 * <li>The Geometry API</li>
 * <li>The Geometry Bounding Volume API</li>
 * <li>The Node API</li>
 * <li>The Noise API</li>
 * <li>The Utility API</li>
 * </ul>
 */
package org.dayflower.geometry.shape;