/**
 * Copyright 2014 - 2025 J&#246;rgen Lundgren
 * 
 * This file is part of Dayflower.
 * 
 * Dayflower is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Dayflower is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with Dayflower. If not, see <http://www.gnu.org/licenses/>.
 */
package org.dayflower.scene.loader;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Map.Entry;

import javax.imageio.ImageIO;

import org.dayflower.geometry.AngleF;
import org.dayflower.geometry.Matrix44F;
import org.dayflower.geometry.Point2F;
import org.dayflower.geometry.Point2I;
import org.dayflower.geometry.Point3F;
import org.dayflower.geometry.Point4F;
import org.dayflower.geometry.Quaternion4F;
import org.dayflower.geometry.Vector2F;
import org.dayflower.geometry.Vector3F;
import org.dayflower.geometry.shape.Plane3F;
import org.dayflower.geometry.shape.Triangle3F;
import org.dayflower.geometry.shape.Triangle3F.Vertex3F;
import org.dayflower.geometry.shape.TriangleMesh3F;
import org.dayflower.parameter.ParameterList;
import org.dayflower.scene.Camera;
import org.dayflower.scene.Primitive;
import org.dayflower.scene.Scene;
import org.dayflower.scene.SceneLoader;
import org.dayflower.scene.Transform;
import org.dayflower.scene.light.PerezLight;
import org.dayflower.scene.material.MatteMaterial;
import org.dayflower.scene.texture.LDRImageTexture;
import org.dayflower.scene.texture.Texture;
import org.dayflower.utility.Floats;

/**
 * An {@code ImageSceneLoader} is a {@link SceneLoader} implementation that loads {@link Scene} instances by interpreting colors in an image.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class ImageSceneLoader implements SceneLoader {
	private static final Texture FLOOR_TEXTURE = LDRImageTexture.undoGammaCorrectionSRGB(LDRImageTexture.load("./resources/textures/laminate.jpg", AngleF.degrees(0.0F), new Vector2F(32.0F, 32.0F)));
	private static final Texture GRASS_TEXTURE = LDRImageTexture.undoGammaCorrectionSRGB(LDRImageTexture.load("./resources/textures/Texture_2.png", AngleF.degrees(0.0F), new Vector2F(0.1F, 0.1F)));
	private static final Texture WALL_TEXTURE = LDRImageTexture.undoGammaCorrectionSRGB(LDRImageTexture.load("./resources/textures/Wall.jpg", AngleF.degrees(0.0F), new Vector2F(16.0F, 16.0F)));
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final List<LineSegment3I> horizontalLineSegments;
	private final List<LineSegment3I> horizontalLineSegmentsOuter;
	private final List<LineSegment3I> verticalLineSegments;
	private final List<LineSegment3I> verticalLineSegmentsOuter;
	private final List<Point3F> outerPoints;
	private final List<TriangleMesh3F> triangleMeshes;
	private float maximumX;
	private float maximumZ;
	private float minimumX;
	private float minimumZ;
	private int resolutionX;
	private int resolutionY;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code ImageSceneLoader} instance.
	 */
	public ImageSceneLoader() {
		this.horizontalLineSegments = new ArrayList<>();
		this.horizontalLineSegmentsOuter = new ArrayList<>();
		this.verticalLineSegments = new ArrayList<>();
		this.verticalLineSegmentsOuter = new ArrayList<>();
		this.outerPoints = new ArrayList<>();
		this.triangleMeshes = new ArrayList<>();
		this.maximumX = -Float.MAX_VALUE;
		this.maximumZ = -Float.MAX_VALUE;
		this.minimumX = +Float.MAX_VALUE;
		this.minimumZ = +Float.MAX_VALUE;
		this.resolutionX = 0;
		this.resolutionY = 0;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Loads a {@link Scene} instance from the file represented by {@code file}.
	 * <p>
	 * Returns the loaded {@code Scene} instance.
	 * <p>
	 * If {@code file} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * imageSceneLoader.load(file, new Scene());
	 * }
	 * </pre>
	 * 
	 * @param file a {@code File} instance that represents a file
	 * @return the loaded {@code Scene} instance
	 * @throws NullPointerException thrown if, and only if, {@code file} is {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
	@Override
	public Scene load(final File file) {
		return load(file, new Scene());
	}
	
	/**
	 * Loads a {@link Scene} instance from the file represented by {@code file} into {@code scene}.
	 * <p>
	 * Returns the loaded {@code Scene} instance, {@code scene}.
	 * <p>
	 * If either {@code file} or {@code scene} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * imageSceneLoader.load(file, scene, new ParameterList());
	 * }
	 * </pre>
	 * 
	 * @param file a {@code File} instance that represents a file
	 * @param scene the {@code Scene} instance to load into
	 * @return the loaded {@code Scene} instance, {@code scene}
	 * @throws NullPointerException thrown if, and only if, either {@code file} or {@code scene} are {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
	@Override
	public Scene load(final File file, final Scene scene) {
		return load(file, scene, new ParameterList());
	}
	
	/**
	 * Loads a {@link Scene} instance from the file represented by {@code file} into {@code scene}.
	 * <p>
	 * Returns the loaded {@code Scene} instance, {@code scene}.
	 * <p>
	 * If either {@code file}, {@code scene} or {@code parameterList} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * 
	 * @param file a {@code File} instance that represents a file
	 * @param scene the {@code Scene} instance to load into
	 * @param parameterList the {@link ParameterList} that contains parameters
	 * @return the loaded {@code Scene} instance, {@code scene}
	 * @throws NullPointerException thrown if, and only if, either {@code file}, {@code scene} or {@code parameterList} are {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
	@Override
	public Scene load(final File file, final Scene scene, final ParameterList parameterList) {
		Objects.requireNonNull(file, "file == null");
		Objects.requireNonNull(scene, "scene == null");
		Objects.requireNonNull(parameterList, "parameterList == null");
		
		doLoad(file);
		doGenerate(scene);
		
		final
		Camera camera = new Camera(new Point3F(0.0F, 5.0F, 0.0F));
		camera.setResolution(2560.0F / 3.0F, 1440.0F / 3.0F);
		camera.setFieldOfViewX(AngleF.degrees(90.0F));
		camera.setFieldOfViewY();
		
		scene.addPrimitive(new Primitive(new MatteMaterial(GRASS_TEXTURE), new Plane3F(), new Transform(new Point3F(0.0F, -0.1F, 0.0F), Quaternion4F.from(Matrix44F.rotateX(AngleF.degrees(90.0F))))));
		scene.addLight(new PerezLight());
		scene.setCamera(camera);
		scene.setName("Image");
		scene.buildAccelerationStructure();
		
		return scene;
	}
	
	/**
	 * Loads a {@link Scene} instance from the file represented by {@code pathname}.
	 * <p>
	 * Returns the loaded {@code Scene} instance.
	 * <p>
	 * If {@code pathname} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * imageSceneLoader.load(pathname, new Scene());
	 * }
	 * </pre>
	 * 
	 * @param pathname a {@code String} instance that represents a pathname to a file
	 * @return the loaded {@code Scene} instance
	 * @throws NullPointerException thrown if, and only if, {@code pathname} is {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
	@Override
	public Scene load(final String pathname) {
		return load(pathname, new Scene());
	}
	
	/**
	 * Loads a {@link Scene} instance from the file represented by {@code pathname} into {@code scene}.
	 * <p>
	 * Returns the loaded {@code Scene} instance, {@code scene}.
	 * <p>
	 * If either {@code pathname} or {@code scene} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * imageSceneLoader.load(pathname, scene, new ParameterList());
	 * }
	 * </pre>
	 * 
	 * @param pathname a {@code String} instance that represents a pathname to a file
	 * @param scene the {@code Scene} instance to load into
	 * @return the loaded {@code Scene} instance, {@code scene}
	 * @throws NullPointerException thrown if, and only if, either {@code pathname} or {@code scene} are {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
	@Override
	public Scene load(final String pathname, final Scene scene) {
		return load(pathname, scene, new ParameterList());
	}
	
	/**
	 * Loads a {@link Scene} instance from the file represented by {@code pathname} into {@code scene}.
	 * <p>
	 * Returns the loaded {@code Scene} instance, {@code scene}.
	 * <p>
	 * If either {@code pathname}, {@code scene} or {@code parameterList} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * 
	 * @param pathname a {@code String} instance that represents a pathname to a file
	 * @param scene the {@code Scene} instance to load into
	 * @param parameterList the {@link ParameterList} that contains parameters
	 * @return the loaded {@code Scene} instance, {@code scene}
	 * @throws NullPointerException thrown if, and only if, either {@code pathname}, {@code scene} or {@code parameterList} are {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
	@Override
	public Scene load(final String pathname, final Scene scene, final ParameterList parameterList) {
		return load(new File(Objects.requireNonNull(pathname, "pathname == null")), scene, parameterList);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private void doClear() {
		this.triangleMeshes.clear();
		
		this.maximumX = -Float.MAX_VALUE;
		this.maximumZ = -Float.MAX_VALUE;
		this.minimumX = +Float.MAX_VALUE;
		this.minimumZ = +Float.MAX_VALUE;
	}
	
	private void doClearLineSegments() {
		this.horizontalLineSegments.clear();
		this.horizontalLineSegmentsOuter.clear();
		
		this.verticalLineSegments.clear();
		this.verticalLineSegmentsOuter.clear();
	}
	
	private void doGenerate(final Scene scene) {
		doClear();
		doGenerateOuterLineSegments();
		doGenerateHorizontalWalls(scene);
		doGenerateVerticalWalls(scene);
		doGenerateOuterPoints();
		doGenerateFloor(scene);
	}
	
	private void doGenerateFloor(final Scene scene) {
		final List<TriangleInfo> triangleInfos = new ArrayList<>();
		
		final int nTriangles = TriangleInfo.triangulate(this.outerPoints.size(), this.outerPoints, new Vector3F(0.0F, 1.0F, 0.0F), triangleInfos);
		
		final List<Triangle3F> triangles = new ArrayList<>();
		
		for(int i = 0; i < nTriangles; i++) {
			final TriangleInfo triangleInfo = triangleInfos.get(i);
			
			final Point3F a = triangleInfo.getA();
			final Point3F b = triangleInfo.getB();
			final Point3F c = triangleInfo.getC();
			
			final Vector3F normal = triangleInfo.getN();
			
			final Point2F uvA = new Point2F(a.x / (this.maximumZ - this.minimumZ), a.z / (this.maximumX - this.minimumX));
			final Point2F uvB = new Point2F(b.x / (this.maximumZ - this.minimumZ), b.z / (this.maximumX - this.minimumX));
			final Point2F uvC = new Point2F(c.x / (this.maximumZ - this.minimumZ), c.z / (this.maximumX - this.minimumX));
			
			final Vertex3F vertexA = new Vertex3F(uvA, new Point4F(a), normal);
			final Vertex3F vertexB = new Vertex3F(uvB, new Point4F(b), normal);
			final Vertex3F vertexC = new Vertex3F(uvC, new Point4F(c), normal);
			
			triangles.add(new Triangle3F(vertexA, vertexB, vertexC));
		}
		
		final TriangleMesh3F triangleMesh = new TriangleMesh3F(triangles, "", "", "Floor");
		
		scene.addPrimitive(new Primitive(new MatteMaterial(FLOOR_TEXTURE), triangleMesh, new Transform()));
	}
	
	private void doGenerateHorizontalWalls(final Scene scene) {
		final float scaleX = Floats.max(this.resolutionX, this.resolutionY) / 32.0F;
		final float scaleY = 255.0F * 32.0F / 32.0F;
		final float scaleZ = Floats.max(this.resolutionX, this.resolutionY) / 32.0F;
		
		for(int i = 0; i < this.horizontalLineSegments.size(); i++) {
			final LineSegment3I horizontalLineSegment = this.horizontalLineSegments.get(i);
			
//			We need to swap the axes for X, Y and Z:
			
			final float startX = horizontalLineSegment.getStartX();
			final float startY = horizontalLineSegment.getStartZ();
			final float startZ = horizontalLineSegment.getStartY();
			
			final float endX = horizontalLineSegment.getEndX();
//			final float endY = horizontalLineSegment.getEndZ();
			final float endZ = horizontalLineSegment.getEndY();
			
			final float startXScaled = startX / scaleX;
			final float startYScaled = startY / scaleY;
			final float startZScaled = startZ / scaleZ;
			
			final float endXScaled = endX / scaleX;
//			final float endYScaled = endY / scaleY;
			final float endZScaled = endZ / scaleZ;
			
			final float startXTranslated = startXScaled * 2.0F - 1.0F;
			final float startYTranslated = startYScaled;
			final float startZTranslated = startZScaled * 2.0F - 1.0F;
			
			final float endXTranslated = endXScaled * 2.0F - 1.0F;
//			final float endYTranslated = endYScaled;
			final float endZTranslated = endZScaled * 2.0F - 1.0F;
			
			final float centerX = startXTranslated + (endXTranslated - startXTranslated) / 2.0F;
			final float centerY = startYTranslated;
			final float centerZ = startZTranslated + (endZTranslated - startZTranslated) / 2.0F;
			
			final float thickness = 1.0F / scaleX;
			final float height = centerY;
			final float length = Floats.sqrt((endXTranslated - startXTranslated) * (endXTranslated - startXTranslated)) / 2.0F;
			
			final Transform transform = new Transform(new Point3F(centerX, centerY, centerZ), new Quaternion4F(), new Vector3F(length, height, thickness));
			
			final int material = horizontalLineSegment.getMaterial();
			
			final TriangleMesh3F triangleMesh = TriangleMesh3F.createCube("Wall-" + Integer.toString(material));
			
			scene.addPrimitive(new Primitive(new MatteMaterial(WALL_TEXTURE), triangleMesh, transform));
			
			this.minimumX = Floats.min(this.minimumX, startXTranslated, endXTranslated);
			this.minimumZ = Floats.min(this.minimumZ, startZTranslated, endZTranslated);
			
			this.maximumX = Floats.max(this.maximumX, startXTranslated, endXTranslated);
			this.maximumZ = Floats.max(this.maximumZ, startZTranslated, endZTranslated);
		}
	}
	
	private void doGenerateOuterLineSegments() {
		boolean hasChanged = false;
		boolean hasStarted = false;
		
		while(hasChanged || !hasStarted) {
			hasChanged = false;
			hasStarted = true;
			
			for(int i = this.horizontalLineSegmentsOuter.size() - 1; i >= 0; i--) {
				final LineSegment3I horizontalLineSegment = this.horizontalLineSegmentsOuter.get(i);
				
				final boolean isConnected = horizontalLineSegment.isConnected();
				final boolean isConnectedAtEnd = horizontalLineSegment.isConnectedAtEnd();
				final boolean isConnectedAtStart = horizontalLineSegment.isConnectedAtStart();
				
				if(!isConnected) {
					this.horizontalLineSegmentsOuter.remove(i);
					
					hasChanged = true;
				} else if(!isConnectedAtEnd && !isConnectedAtStart) {
					final Map<Point2I, List<LineSegment3I>> connections = horizontalLineSegment.getConnections();
					
					if(connections.size() > 0) {
						int maximumX = horizontalLineSegment.getStartX();
						int minimumX = horizontalLineSegment.getEndX();
						
						for(final Point2I point : connections.keySet()) {
							maximumX = Math.max(maximumX, point.x);
							minimumX = Math.min(minimumX, point.x);
						}
						
						if(maximumX == minimumX) {
							for(final Entry<Point2I, List<LineSegment3I>> entry : connections.entrySet()) {
								for(final LineSegment3I lineSegment : entry.getValue()) {
									lineSegment.removeConnections(entry.getKey());
								}
							}
							
							this.horizontalLineSegmentsOuter.remove(i);
							
							hasChanged = true;
						} else if(maximumX < horizontalLineSegment.getEndX() || minimumX > horizontalLineSegment.getStartX()) {
							horizontalLineSegment.setEnd(maximumX, horizontalLineSegment.getEndY(), horizontalLineSegment.getEndZ());
							horizontalLineSegment.setStart(minimumX, horizontalLineSegment.getStartY(), horizontalLineSegment.getStartZ());
							
							hasChanged = true;
						}
					} else {
						this.horizontalLineSegmentsOuter.remove(i);
						
						hasChanged = true;
					}
				} else if(!isConnectedAtEnd) {
					final Map<Point2I, List<LineSegment3I>> connections = horizontalLineSegment.getConnections();
					
					if(connections.size() > 0) {
						int maximumX = horizontalLineSegment.getStartX();
						
						for(final Point2I point : connections.keySet()) {
							maximumX = Math.max(maximumX, point.x);
						}
						
						if(maximumX == horizontalLineSegment.getStartX()) {
							for(final Entry<Point2I, List<LineSegment3I>> entry : connections.entrySet()) {
								for(final LineSegment3I lineSegment : entry.getValue()) {
									lineSegment.removeConnections(entry.getKey());
								}
							}
							
							this.horizontalLineSegmentsOuter.remove(i);
							
							hasChanged = true;
						} else if(maximumX < horizontalLineSegment.getEndX()) {
							horizontalLineSegment.setEnd(maximumX, horizontalLineSegment.getEndY(), horizontalLineSegment.getEndZ());
							
							hasChanged = true;
						}
					} else {
						this.horizontalLineSegmentsOuter.remove(i);
						
						hasChanged = true;
					}
				} else if(!isConnectedAtStart) {
					final Map<Point2I, List<LineSegment3I>> connections = horizontalLineSegment.getConnections();
					
					if(connections.size() > 0) {
						int minimumX = horizontalLineSegment.getEndX();
						
						for(final Point2I point : connections.keySet()) {
							minimumX = Math.min(minimumX, point.x);
						}
						
						if(minimumX == horizontalLineSegment.getEndX()) {
							for(final Entry<Point2I, List<LineSegment3I>> entry : connections.entrySet()) {
								for(final LineSegment3I lineSegment : entry.getValue()) {
									lineSegment.removeConnections(entry.getKey());
								}
							}
							
							this.horizontalLineSegmentsOuter.remove(i);
							
							hasChanged = true;
						} else if(minimumX > horizontalLineSegment.getStartX()) {
							horizontalLineSegment.setStart(minimumX, horizontalLineSegment.getStartY(), horizontalLineSegment.getStartZ());
							
							hasChanged = true;
						}
					} else {
						this.horizontalLineSegmentsOuter.remove(i);
						
						hasChanged = true;
					}
				}
			}
			
			for(int i = this.verticalLineSegmentsOuter.size() - 1; i >= 0; i--) {
				final LineSegment3I verticalLineSegment = this.verticalLineSegmentsOuter.get(i);
				
				final boolean isConnected = verticalLineSegment.isConnected();
				final boolean isConnectedAtEnd = verticalLineSegment.isConnectedAtEnd();
				final boolean isConnectedAtStart = verticalLineSegment.isConnectedAtStart();
				
				if(!isConnected) {
					this.verticalLineSegmentsOuter.remove(i);
					
					hasChanged = true;
				} else if(!isConnectedAtEnd && !isConnectedAtStart) {
					final Map<Point2I, List<LineSegment3I>> connections = verticalLineSegment.getConnections();
					
					if(connections.size() > 0) {
						int maximumY = verticalLineSegment.getStartY();
						int minimumY = verticalLineSegment.getEndY();
						
						for(final Point2I point : connections.keySet()) {
							maximumY = Math.max(maximumY, point.y);
							minimumY = Math.min(minimumY, point.y);
						}
						
						if(maximumY == minimumY) {
							for(final Entry<Point2I, List<LineSegment3I>> entry : connections.entrySet()) {
								for(final LineSegment3I lineSegment : entry.getValue()) {
									lineSegment.removeConnections(entry.getKey());
								}
							}
							
							this.verticalLineSegmentsOuter.remove(i);
							
							hasChanged = true;
						} else if(maximumY < verticalLineSegment.getEndY() || minimumY > verticalLineSegment.getStartY()) {
							verticalLineSegment.setEnd(verticalLineSegment.getEndX(), maximumY, verticalLineSegment.getEndZ());
							verticalLineSegment.setStart(verticalLineSegment.getStartX(), minimumY, verticalLineSegment.getStartZ());
							
							hasChanged = true;
						}
					} else {
						this.verticalLineSegmentsOuter.remove(i);
						
						hasChanged = true;
					}
				} else if(!isConnectedAtEnd) {
					final Map<Point2I, List<LineSegment3I>> connections = verticalLineSegment.getConnections();
					
					if(connections.size() > 0) {
						int maximumY = verticalLineSegment.getStartY();
						
						for(final Point2I point : connections.keySet()) {
							maximumY = Math.max(maximumY, point.y);
						}
						
						if(maximumY == verticalLineSegment.getStartY()) {
							for(final Entry<Point2I, List<LineSegment3I>> entry : connections.entrySet()) {
								for(final LineSegment3I lineSegment : entry.getValue()) {
									lineSegment.removeConnections(entry.getKey());
								}
							}
							
							this.verticalLineSegmentsOuter.remove(i);
							
							hasChanged = true;
						} else if(maximumY < verticalLineSegment.getEndY()) {
							verticalLineSegment.setEnd(verticalLineSegment.getEndX(), maximumY, verticalLineSegment.getEndZ());
							
							hasChanged = true;
						}
					} else {
						this.verticalLineSegmentsOuter.remove(i);
						
						hasChanged = true;
					}
				} else if(!isConnectedAtStart) {
					final Map<Point2I, List<LineSegment3I>> connections = verticalLineSegment.getConnections();
					
					if(connections.size() > 0) {
						int minimumY = verticalLineSegment.getEndY();
						
						for(final Point2I point : connections.keySet()) {
							minimumY = Math.min(minimumY, point.y);
						}
						
						if(minimumY == verticalLineSegment.getEndY()) {
							for(final Entry<Point2I, List<LineSegment3I>> entry : connections.entrySet()) {
								for(final LineSegment3I lineSegment : entry.getValue()) {
									lineSegment.removeConnections(entry.getKey());
								}
							}
							
							this.verticalLineSegmentsOuter.remove(i);
							
							hasChanged = true;
						} else if(minimumY > verticalLineSegment.getStartY()) {
							verticalLineSegment.setStart(verticalLineSegment.getStartX(), minimumY, verticalLineSegment.getStartZ());
							
							hasChanged = true;
						}
					} else {
						this.verticalLineSegmentsOuter.remove(i);
						
						hasChanged = true;
					}
				}
			}
		}
	}
	
	private void doGenerateOuterPoints() {
		this.outerPoints.clear();
		
		if(this.horizontalLineSegmentsOuter.size() > 0) {
			final float scaleX = Floats.max(this.resolutionX, this.resolutionY) / 32.0F;
			final float scaleY = 255.0F * 32.0F / 32.0F;
			final float scaleZ = Floats.max(this.resolutionX, this.resolutionY) / 32.0F;
			
			final List<LineSegment3I> outerLineSegments = this.horizontalLineSegmentsOuter.get(0).getConnectionList();
			
			int currentX = outerLineSegments.get(0).getEndX();
			int currentY = outerLineSegments.get(0).getEndY();
			
			for(int i = 0; i < outerLineSegments.size(); i++) {
				final LineSegment3I lineSegment = outerLineSegments.get(i);
				
				final boolean isHorizontal = lineSegment.getStartY() == lineSegment.getEndY();
				final boolean isVertical = lineSegment.getStartX() == lineSegment.getEndX();
				
				final float startX = lineSegment.getStartX();
				final float startY = 0.0F;
				final float startZ = lineSegment.getStartY();
				
				final float endX = lineSegment.getEndX();
				final float endY = 0.0F;
				final float endZ = lineSegment.getEndY();
				
				final float startXScaled = startX / scaleX;
				final float startYScaled = startY / scaleY;
				final float startZScaled = startZ / scaleZ;
				
				final float endXScaled = endX / scaleX;
				final float endYScaled = endY / scaleY;
				final float endZScaled = endZ / scaleZ;
				
				final float startXTranslated = startXScaled * 2.0F - 1.0F;
				final float startYTranslated = startYScaled;
				final float startZTranslated = startZScaled * 2.0F - 1.0F;
				
				final float endXTranslated = endXScaled * 2.0F - 1.0F;
				final float endYTranslated = endYScaled;
				final float endZTranslated = endZScaled * 2.0F - 1.0F;
				
				final Point3F pointStart = new Point3F(startXTranslated, startYTranslated, startZTranslated);
				final Point3F pointEnd = new Point3F(endXTranslated, endYTranslated, endZTranslated);
				
				if(isHorizontal) {
					if(currentX >= lineSegment.getEndX()) {
						if(!this.outerPoints.contains(pointEnd)) {
							this.outerPoints.add(pointEnd);
						}
						
						if(!this.outerPoints.contains(pointStart)) {
							this.outerPoints.add(pointStart);
						}
						
						currentX = lineSegment.getStartX();
						currentY = lineSegment.getStartY();
					} else {
						if(!this.outerPoints.contains(pointStart)) {
							this.outerPoints.add(pointStart);
						}
						
						if(!this.outerPoints.contains(pointEnd)) {
							this.outerPoints.add(pointEnd);
						}
						
						currentX = lineSegment.getEndX();
						currentY = lineSegment.getEndY();
					}
				} else if(isVertical) {
					if(currentY >= lineSegment.getEndY()) {
						if(!this.outerPoints.contains(pointStart)) {
							this.outerPoints.add(pointStart);
						}
						
						if(!this.outerPoints.contains(pointEnd)) {
							this.outerPoints.add(pointEnd);
						}
						
						currentX = lineSegment.getEndX();
						currentY = lineSegment.getEndY();
					} else {
						if(!this.outerPoints.contains(pointEnd)) {
							this.outerPoints.add(pointEnd);
						}
						
						if(!this.outerPoints.contains(pointStart)) {
							this.outerPoints.add(pointStart);
						}
						
						currentX = lineSegment.getStartX();
						currentY = lineSegment.getStartY();
					}
				}
			}
		}
	}
	
	private void doGenerateVerticalWalls(final Scene scene) {
		final float scaleX = Floats.max(this.resolutionX, this.resolutionY) / 32.0F;
		final float scaleY = 255.0F * 32.0F / 32.0F;
		final float scaleZ = Floats.max(this.resolutionX, this.resolutionY) / 32.0F;
		
		for(int i = 0; i < this.verticalLineSegments.size(); i++) {
			final LineSegment3I verticalLineSegment = this.verticalLineSegments.get(i);
			
//			We need to swap the axes for X, Y and Z:
			
			final float startX = verticalLineSegment.getStartX();
			final float startY = verticalLineSegment.getStartZ();
			final float startZ = verticalLineSegment.getStartY();
			
			final float endX = verticalLineSegment.getEndX();
//			final float endY = verticalLineSegment.getEndZ();
			final float endZ = verticalLineSegment.getEndY();
			
			final float startXScaled = startX / scaleX;
			final float startYScaled = startY / scaleY;
			final float startZScaled = startZ / scaleZ;
			
			final float endXScaled = endX / scaleX;
//			final float endYScaled = endY / scaleY;
			final float endZScaled = endZ / scaleZ;
			
			final float startXTranslated = startXScaled * 2.0F - 1.0F;
			final float startYTranslated = startYScaled;
			final float startZTranslated = startZScaled * 2.0F - 1.0F;
			
			final float endXTranslated = endXScaled * 2.0F - 1.0F;
//			final float endYTranslated = endYScaled;
			final float endZTranslated = endZScaled * 2.0F - 1.0F;
			
			final float centerX = startXTranslated + (endXTranslated - startXTranslated) / 2.0F;
			final float centerY = startYTranslated;
			final float centerZ = startZTranslated + (endZTranslated - startZTranslated) / 2.0F;
			
			final float thickness = 1.0F / scaleZ;
			final float height = centerY;
			final float length = Floats.sqrt((endZTranslated - startZTranslated) * (endZTranslated - startZTranslated)) / 2.0F;
			
			final Transform transform = new Transform(new Point3F(centerX, centerY, centerZ), new Quaternion4F(), new Vector3F(thickness, height, length));
			
			final int material = verticalLineSegment.getMaterial();
			
			final TriangleMesh3F triangleMesh = TriangleMesh3F.createCube("Wall-" + Integer.toString(material));
			
			scene.addPrimitive(new Primitive(new MatteMaterial(WALL_TEXTURE), triangleMesh, transform));
			
			this.minimumX = Floats.min(this.minimumX, startXTranslated, endXTranslated);
			this.minimumZ = Floats.min(this.minimumZ, startZTranslated, endZTranslated);
			
			this.maximumX = Floats.max(this.maximumX, startXTranslated, endXTranslated);
			this.maximumZ = Floats.max(this.maximumZ, startZTranslated, endZTranslated);
		}
	}
	
	private void doLoad(final File file) {
		try {
			doClearLineSegments();
			
			final BufferedImage bufferedImage = ImageIO.read(Objects.requireNonNull(file, "file == null"));
			
			final int w = bufferedImage.getWidth();
			final int h = bufferedImage.getHeight();
			
			this.resolutionX = w;
			this.resolutionY = h;
			
			final List<LineSegment3I> horizontalLineSegments = new ArrayList<>();
			final List<LineSegment3I> horizontalLineSegmentsOuter = new ArrayList<>();
			final List<LineSegment3I> verticalLineSegments = new ArrayList<>();
			final List<LineSegment3I> verticalLineSegmentsOuter = new ArrayList<>();
			
			final int minimumLength = 2;
			
//			Find all horizontal line segments with a length of at least 2 in the image:
			for(int y = 0; y < h; y++) {
				LineSegment3I currentHorizontalLineSegment = null;
				
				for(int x = 0; x < w; x++) {
					final int rgb = bufferedImage.getRGB(x, y);
					
					final int r = (rgb >> 16) & 0xFF;
					final int g = (rgb >>  8) & 0xFF;
					final int b = (rgb >>  0) & 0xFF;
					
					final int z = 255 - r;
					
					final int material = ((g & 0xFF) << 8) | ((b & 0xFF) << 0);
					
					if(z == 0 && currentHorizontalLineSegment != null) {
						if(currentHorizontalLineSegment.getEndX() - currentHorizontalLineSegment.getStartX() >= minimumLength) {
							horizontalLineSegments.add(currentHorizontalLineSegment);
						}
						
						currentHorizontalLineSegment = null;
					} else if(z > 0 && currentHorizontalLineSegment != null) {
						if(z != currentHorizontalLineSegment.getStartZ() || material != currentHorizontalLineSegment.getMaterial()) {
							if(currentHorizontalLineSegment.getEndX() - currentHorizontalLineSegment.getStartX() >= minimumLength) {
								horizontalLineSegments.add(currentHorizontalLineSegment);
							}
							
							currentHorizontalLineSegment = new LineSegment3I(x, y, z);
							currentHorizontalLineSegment.setMaterial(material);
						} else {
							currentHorizontalLineSegment.setEnd(x, y, z);
						}
					} else if(z > 0) {
						currentHorizontalLineSegment = new LineSegment3I(x, y, z);
						currentHorizontalLineSegment.setMaterial(material);
					}
				}
			}
			
//			Find all vertical line segments with a length of at least 2 in the image:
			for(int x = 0; x < w; x++) {
				LineSegment3I currentVerticalLineSegment = null;
				
				for(int y = 0; y < h; y++) {
					final int rgb = bufferedImage.getRGB(x, y);
					
					final int r = (rgb >> 16) & 0xFF;
					final int g = (rgb >>  8) & 0xFF;
					final int b = (rgb >>  0) & 0xFF;
					
					final int z = 255 - r;
					
					final int material = ((g & 0xFF) << 8) | ((b & 0xFF) << 0);
					
					if(z == 0 && currentVerticalLineSegment != null) {
						if(currentVerticalLineSegment.getEndY() - currentVerticalLineSegment.getStartY() >= minimumLength) {
							verticalLineSegments.add(currentVerticalLineSegment);
						}
						
						currentVerticalLineSegment = null;
					} else if(z > 0 && currentVerticalLineSegment != null) {
						if(z != currentVerticalLineSegment.getStartZ() || material != currentVerticalLineSegment.getMaterial()) {
							if(currentVerticalLineSegment.getEndY() - currentVerticalLineSegment.getStartY() >= minimumLength) {
								verticalLineSegments.add(currentVerticalLineSegment);
							}
							
							currentVerticalLineSegment = new LineSegment3I(x, y, z);
							currentVerticalLineSegment.setMaterial(material);
						} else {
							currentVerticalLineSegment.setEnd(x, y, z);
						}
					} else if(z > 0) {
						currentVerticalLineSegment = new LineSegment3I(x, y, z);
						currentVerticalLineSegment.setMaterial(material);
					}
				}
			}
			
//			Add copies of the horizontal line segments:
			for(int i = 0; i < horizontalLineSegments.size(); i++) {
				final LineSegment3I oldHorizontalLineSegment = horizontalLineSegments.get(i);
				final LineSegment3I newHorizontalLineSegment = oldHorizontalLineSegment.copy();
				
				horizontalLineSegmentsOuter.add(newHorizontalLineSegment);
			}
			
//			Add copies of the vertical line segments:
			for(int i = 0; i < verticalLineSegments.size(); i++) {
				final LineSegment3I oldVerticalLineSegment = verticalLineSegments.get(i);
				final LineSegment3I newVerticalLineSegment = oldVerticalLineSegment.copy();
				
				verticalLineSegmentsOuter.add(newVerticalLineSegment);
			}
			
//			Add all connections for each horizontal line segment:
			for(int i = 0; i < horizontalLineSegmentsOuter.size(); i++) {
				final LineSegment3I horizontalLineSegment = horizontalLineSegmentsOuter.get(i);
				
				for(int j = 0; j < verticalLineSegmentsOuter.size(); j++) {
					final LineSegment3I verticalLineSegment = verticalLineSegmentsOuter.get(j);
					
					if(horizontalLineSegment.getStartX() <= verticalLineSegment.getStartX() && horizontalLineSegment.getEndX() >= verticalLineSegment.getStartX()) {
						if(horizontalLineSegment.getStartY() >= verticalLineSegment.getStartY() && horizontalLineSegment.getStartY() <= verticalLineSegment.getEndY()) {
							horizontalLineSegment.addConnection(new Point2I(verticalLineSegment.getStartX(), horizontalLineSegment.getStartY()), verticalLineSegment);
						}
					}
				}
			}
			
//			Add all connections for each vertical line segment:
			for(int i = 0; i < verticalLineSegmentsOuter.size(); i++) {
				final LineSegment3I verticalLineSegment = verticalLineSegmentsOuter.get(i);
				
				for(int j = 0; j < horizontalLineSegmentsOuter.size(); j++) {
					final LineSegment3I horizontalLineSegment = horizontalLineSegmentsOuter.get(j);
					
					if(verticalLineSegment.getStartY() <= horizontalLineSegment.getStartY() && verticalLineSegment.getEndY() >= horizontalLineSegment.getStartY()) {
						if(verticalLineSegment.getStartX() >= horizontalLineSegment.getStartX() && verticalLineSegment.getStartX() <= horizontalLineSegment.getEndX()) {
							verticalLineSegment.addConnection(new Point2I(verticalLineSegment.getStartX(), horizontalLineSegment.getStartY()), horizontalLineSegment);
						}
					}
				}
			}
			
			this.horizontalLineSegments.addAll(horizontalLineSegments);
			this.horizontalLineSegmentsOuter.addAll(horizontalLineSegmentsOuter);
			
			this.verticalLineSegments.addAll(verticalLineSegments);
			this.verticalLineSegmentsOuter.addAll(verticalLineSegmentsOuter);
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static final class LineSegment3I {
		private Map<Point2I, List<LineSegment3I>> connections;
		private int endX;
		private int endY;
		private int endZ;
		private int material;
		private int startX;
		private int startY;
		private int startZ;
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		public LineSegment3I(final int startX, final int startY, final int startZ) {
			this(startX, startY, startZ, startX, startY, startZ);
		}
		
		public LineSegment3I(final int startX, final int startY, final int startZ, final int endX, final int endY, final int endZ) {
			this(startX, startY, startZ, endX, endY, endZ, 0);
		}
		
		public LineSegment3I(final int startX, final int startY, final int startZ, final int endX, final int endY, final int endZ, final int material) {
			this.startX = startX;
			this.startY = startY;
			this.startZ = startZ;
			this.endX = endX;
			this.endY = endY;
			this.endZ = endZ;
			this.material = material;
			this.connections = new HashMap<>();
		}
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		public LineSegment3I copy() {
			return new LineSegment3I(this.startX, this.startY, this.startZ, this.endX, this.endY, this.endZ, this.material);
		}
		
		public List<LineSegment3I> getConnectionList() {
			return getConnectionList(new ArrayList<>(), 0);
		}
		
		public List<LineSegment3I> getConnectionList(final List<LineSegment3I> connectionList, final int depth) {
			connectionList.add(this);
			
			boolean isDirectionSwitchNeeded = false;
			
			int x = this.startX;
			int y = this.endY;
			
			List<LineSegment3I> lineSegments = this.connections.get(new Point2I(x, y));
			
			if(lineSegments != null && lineSegments.size() == 1) {
				final LineSegment3I lineSegment = lineSegments.get(0);
				
				boolean contains = false;
				
				for(int i = 0; i < connectionList.size(); i++) {
					final LineSegment3I currentLineSegment = connectionList.get(i);
					
					if(currentLineSegment.getStartX() == lineSegment.getStartX() && currentLineSegment.getStartY() == lineSegment.getStartY() && currentLineSegment.getEndX() == lineSegment.getEndX() && currentLineSegment.getEndY() == lineSegment.getEndY()) {
						contains = true;
						
						isDirectionSwitchNeeded = true;
						
						break;
					}
				}
				
				if(!contains) {
					lineSegment.getConnectionList(connectionList, depth + 1);
				}
			}
			
			if(isDirectionSwitchNeeded) {
				isDirectionSwitchNeeded = false;
				
				x = this.endX;
				y = this.endY;
				
				lineSegments = this.connections.get(new Point2I(x, y));
				
				if(lineSegments != null && lineSegments.size() == 1) {
					final LineSegment3I lineSegment = lineSegments.get(0);
					
					boolean contains = false;
					
					for(int i = 0; i < connectionList.size(); i++) {
						final LineSegment3I currentLineSegment = connectionList.get(i);
						
						if(currentLineSegment.getStartX() == lineSegment.getStartX() && currentLineSegment.getStartY() == lineSegment.getStartY() && currentLineSegment.getEndX() == lineSegment.getEndX() && currentLineSegment.getEndY() == lineSegment.getEndY()) {
							contains = true;
							
							isDirectionSwitchNeeded = true;
							
							break;
						}
					}
					
					if(!contains) {
						lineSegment.getConnectionList(connectionList, depth + 1);
					}
				}
			}
			
			if(isDirectionSwitchNeeded) {
				isDirectionSwitchNeeded = false;
				
				x = this.endX;
				y = this.startY;
				
				lineSegments = this.connections.get(new Point2I(x, y));
				
				if(lineSegments != null && lineSegments.size() == 1) {
					final LineSegment3I lineSegment = lineSegments.get(0);
					
					boolean contains = false;
					
					for(int i = 0; i < connectionList.size(); i++) {
						final LineSegment3I currentLineSegment = connectionList.get(i);
						
						if(currentLineSegment.getStartX() == lineSegment.getStartX() && currentLineSegment.getStartY() == lineSegment.getStartY() && currentLineSegment.getEndX() == lineSegment.getEndX() && currentLineSegment.getEndY() == lineSegment.getEndY()) {
							contains = true;
							
							break;
						}
					}
					
					if(!contains) {
						lineSegment.getConnectionList(connectionList, depth + 1);
					}
				}
			}
			
			return connectionList;
		}
		
		public Map<Point2I, List<LineSegment3I>> getConnections() {
			return new HashMap<>(this.connections);
		}
		
		public boolean isConnected() {
			return this.connections.size() > 0;
		}
		
		public boolean isConnectedAtEnd() {
			return this.connections.containsKey(new Point2I(this.endX, this.endY));
		}
		
		public boolean isConnectedAtStart() {
			return this.connections.containsKey(new Point2I(this.startX, this.startY));
		}
		
		public int getEndX() {
			return this.endX;
		}
		
		public int getEndY() {
			return this.endY;
		}
		
		public int getEndZ() {
			return this.endZ;
		}
		
		public int getMaterial() {
			return this.material;
		}
		
		public int getStartX() {
			return this.startX;
		}
		
		public int getStartY() {
			return this.startY;
		}
		
		public int getStartZ() {
			return this.startZ;
		}
		
		public void addConnection(final Point2I point, final LineSegment3I lineSegment) {
			final List<LineSegment3I> lineSegments = this.connections.get(point);
			
			if(lineSegments != null) {
				lineSegments.add(lineSegment);
			} else {
				final List<LineSegment3I> newLineSegments = new ArrayList<>();
				
				newLineSegments.add(lineSegment);
				
				this.connections.put(point, newLineSegments);
			}
		}
		
		public void removeConnections(final Point2I point) {
			this.connections.remove(point);
		}
		
		public void setEnd(final int endX, final int endY, final int endZ) {
			this.endX = endX;
			this.endY = endY;
			this.endZ = endZ;
		}
		
		public void setMaterial(final int material) {
			this.material = material;
		}
		
		public void setStart(final int startX, final int startY, final int startZ) {
			this.startX = startX;
			this.startY = startY;
			this.startZ = startZ;
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static final class TriangleInfo {
		private static final float EPSILON = 0.001F;
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		private final Point3F a;
		private final Point3F b;
		private final Point3F c;
		private final Vector3F n;
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		public TriangleInfo(final Point3F a, final Point3F b, final Point3F c, final Vector3F n) {
			this.a = Objects.requireNonNull(a, "a == null");
			this.b = Objects.requireNonNull(b, "b == null");
			this.c = Objects.requireNonNull(c, "c == null");
			this.n = Objects.requireNonNull(n, "n == null");
		}
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		public Point3F getA() {
			return this.a;
		}
		
		public Point3F getB() {
			return this.b;
		}
		
		public Point3F getC() {
			return this.c;
		}
		
		public Vector3F getN() {
			return this.n;
		}
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		public static int triangulate(final int vertexCount, final List<Point3F> vertices, final Vector3F n, final List<TriangleInfo> triangleInfos) {
			final boolean[] active = new boolean[vertexCount];
			
			for(int i = 0; i < vertexCount; i++) {
				active[i] = true;
			}
			
			int triangleCount = 0;
			
			int start = 0;
			
			int p1 = 0;
			int p2 = 1;
			
			int m1 = vertexCount - 1;
			int m2 = vertexCount - 2;
			
			boolean lastPositive = false;
			
			while(true) {
				if(p2 == m2) {
					final Point3F a = vertices.get(m1);
					final Point3F b = vertices.get(p1);
					final Point3F c = vertices.get(p2);
					
					triangleInfos.add(new TriangleInfo(a, b, c, n));
					
					triangleCount++;
					
					break;
				}
				
				final Point3F vp1 = vertices.get(p1);
				final Point3F vp2 = vertices.get(p2);
				final Point3F vm1 = vertices.get(m1);
				final Point3F vm2 = vertices.get(m2);
				
				boolean positive = false;
				boolean negative = false;
				
				Vector3F n1 = Vector3F.crossProduct(n, Vector3F.normalize(Vector3F.subtract(vm1, vp2)));
				
				if(Vector3F.dotProduct(n1, Vector3F.subtract(vp1, vp2)) > EPSILON) {
					positive = true;
					
					final Vector3F n2 = Vector3F.crossProduct(n, Vector3F.normalize(Vector3F.subtract(vp1, vm1)));
					final Vector3F n3 = Vector3F.crossProduct(n, Vector3F.normalize(Vector3F.subtract(vp2, vp1)));
					
					for(int i = 0; i < vertexCount; i++) {
						if(active[i] && i != p1 && i != p2 && i != m1) {
							final Point3F v = vertices.get(i);
							
							if(Vector3F.dotProduct(n1, Vector3F.normalize(Vector3F.subtract(v, vp2))) > -EPSILON && Vector3F.dotProduct(n2, Vector3F.normalize(Vector3F.subtract(v, vm1))) > -EPSILON && Vector3F.dotProduct(n3, Vector3F.normalize(Vector3F.subtract(v, vp1))) > -EPSILON) {
								positive = false;
								
								break;
							}
						}
					}
				}
				
				n1 = Vector3F.crossProduct(n, Vector3F.normalize(Vector3F.subtract(vm2, vp1)));
				
				if(Vector3F.dotProduct(n1, Vector3F.subtract(vm1, vp1)) > EPSILON) {
					negative = true;
					
					final Vector3F n2 = Vector3F.crossProduct(n, Vector3F.normalize(Vector3F.subtract(vm1, vm2)));
					final Vector3F n3 = Vector3F.crossProduct(n, Vector3F.normalize(Vector3F.subtract(vp1, vm1)));
					
					for(int i = 0; i < vertexCount; i++) {
						if(active[i] && i != m1 && i != m2 && i != p1) {
							final Point3F v = vertices.get(i);
							
							if(Vector3F.dotProduct(n1, Vector3F.normalize(Vector3F.subtract(v, vp1))) > -EPSILON && Vector3F.dotProduct(n2, Vector3F.normalize(Vector3F.subtract(v, vm2))) > -EPSILON && Vector3F.dotProduct(n3, Vector3F.normalize(Vector3F.subtract(v, vm1))) > -EPSILON) {
								negative = false;
								
								break;
							}
						}
					}
				}
				
				if(positive && negative) {
					final float pd = Vector3F.dotProduct(Vector3F.normalize(Vector3F.subtract(vp2, vm1)), Vector3F.normalize(Vector3F.subtract(vm2, vm1)));
					final float md = Vector3F.dotProduct(Vector3F.normalize(Vector3F.subtract(vm2, vp1)), Vector3F.normalize(Vector3F.subtract(vp2, vp1)));
					
					if(Floats.abs(pd - md) < EPSILON) {
						if(lastPositive) {
							positive = false;
						} else {
							negative = false;
						}
					} else {
						if(pd < md) {
							negative = false;
						} else {
							positive = false;
						}
					}
				}
				
				if(positive) {
					active[p1] = false;
					
					final Point3F a = vertices.get(m1);
					final Point3F b = vertices.get(p1);
					final Point3F c = vertices.get(p2);
					
					triangleInfos.add(new TriangleInfo(a, b, c, n));
					
					triangleCount++;
					
					p1 = doGetNextActive(p1, vertexCount, active);
					p2 = doGetNextActive(p2, vertexCount, active);
					
					lastPositive = true;
					
					start = -1;
				} else if(negative) {
					active[m1] = false;
					
					final Point3F a = vertices.get(m2);
					final Point3F b = vertices.get(m1);
					final Point3F c = vertices.get(p1);
					
					triangleInfos.add(new TriangleInfo(a, b, c, n));
					
					triangleCount++;
					
					m1 = doGetPreviousActive(m1, vertexCount, active);
					m2 = doGetPreviousActive(m2, vertexCount, active);
					
					lastPositive = false;
					
					start = -1;
				} else {
					if(start == -1) {
						start = p2;
					} else if(p2 == start) {
						break;
					}
					
					m2 = m1;
					m1 = p1;
					p1 = p2;
					p2 = doGetNextActive(p2, vertexCount, active);
				}
			}
			
			return triangleCount;
		}
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		private static int doGetNextActive(final int index, final int vertexCount, final boolean[] active) {
			int currentIndex = index;
			
			while(true) {
				if(++currentIndex == vertexCount) {
					currentIndex = 0;
				}
				
				if(active[currentIndex]) {
					break;
				}
			}
			
			return currentIndex;
		}
		
		private static int doGetPreviousActive(final int index, final int vertexCount, final boolean[] active) {
			int currentIndex = index;
			
			while(true) {
				if(--currentIndex == -1) {
					currentIndex = vertexCount - 1;
				}
				
				if(active[currentIndex]) {
					break;
				}
			}
			
			return currentIndex;
		}
	}
}