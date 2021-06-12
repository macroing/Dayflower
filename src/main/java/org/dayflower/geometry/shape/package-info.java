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
 */
package org.dayflower.geometry.shape;