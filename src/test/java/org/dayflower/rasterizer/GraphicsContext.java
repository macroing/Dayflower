/**
 * Copyright 2014 - 2022 J&#246;rgen Lundgren
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
package org.dayflower.rasterizer;

import static org.dayflower.utility.Floats.ceil;
import static org.dayflower.utility.Ints.toInt;

import java.util.ArrayList;
import java.util.List;

import org.dayflower.geometry.Matrix44F;
import org.dayflower.geometry.Point4F;
import org.dayflower.geometry.Vector3F;
import org.dayflower.geometry.shape.Triangle3F;
import org.dayflower.geometry.shape.Triangle3F.Vertex3F;
import org.dayflower.geometry.shape.TriangleMesh3F;
import org.dayflower.utility.Floats;

public final class GraphicsContext extends Bitmap {
	private final Matrix44F identity;
	private final Matrix44F screenSpaceTransform;
	private final float[] zBuffer;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public GraphicsContext(final int width, final int height) {
		super(width, height);
		
		this.identity = Matrix44F.identity();
		this.screenSpaceTransform = doScreenSpaceTransform(width, height);
		this.zBuffer = new float[width * height];
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void clearZBuffer() {
		for(int i = 0; i < this.zBuffer.length; i++) {
			this.zBuffer[i] = Float.MAX_VALUE;
		}
	}
	
	public void drawTriangle(final Triangle3F triangle, final Bitmap bitmap) {
		final Vertex3F a = triangle.getA();
		final Vertex3F b = triangle.getB();
		final Vertex3F c = triangle.getC();
		
		if(a.isInsideViewFrustum() && b.isInsideViewFrustum() && c.isInsideViewFrustum()) {
			doFillTriangle(a, b, c, bitmap);
			
			return;
		}
		
		final List<Vertex3F> vertices0 = new ArrayList<>(3);
		final List<Vertex3F> vertices1 = new ArrayList<>();
		
		vertices0.add(a);
		vertices0.add(b);
		vertices0.add(c);
		
		if(doClipPolygonAxis(vertices0, vertices1, 0) && doClipPolygonAxis(vertices0, vertices1, 1) && doClipPolygonAxis(vertices0, vertices1, 2)) {
			final Vertex3F initialVertex = vertices0.get(0);
			
			for(int i = 1; i < vertices0.size() - 1; i++) {
				doFillTriangle(initialVertex, vertices0.get(i), vertices0.get(i + 1), bitmap);
			}
		}
	}
	
	public void drawTriangleMesh(final TriangleMesh3F triangleMesh, final Matrix44F viewProjection, final Matrix44F model, final Matrix44F modelInverse, final Bitmap bitmap) {
		final Matrix44F modelViewProjection = Matrix44F.multiply(viewProjection, model);
		
		for(final Triangle3F triangle : triangleMesh.getTriangles()) {
			drawTriangle(Triangle3F.transform(triangle, modelViewProjection, modelInverse), bitmap);
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private void doDrawScanLine(final Gradients gradients, final Edge left, final Edge right, final int j, final Bitmap bitmap) {
		final int xMinimum = toInt(ceil(left.getX()));
		final int xMaximum = toInt(ceil(right.getX()));
		
		final float xPrestep = xMinimum - left.getX();
		
		final float depthXStep = gradients.getDepthXStep();
		final float lightIntensityXStep = gradients.getLightIntensityXStep();
		final float textureCoordinateXXStep = gradients.getTextureCoordinateXXStep();
		final float textureCoordinateYXStep = gradients.getTextureCoordinateYXStep();
		final float zReciprocalXStep = gradients.getZReciprocalXStep();
		
		float depth = left.getDepth() + depthXStep * xPrestep;
		float lightIntensity = left.getLightIntensity() + lightIntensityXStep * xPrestep;
		float textureCoordinateX = left.getTextureCoordinateX() + textureCoordinateXXStep * xPrestep;
		float textureCoordinateY = left.getTextureCoordinateY() + textureCoordinateYXStep * xPrestep;
		float zReciprocal = left.getZReciprocal() + zReciprocalXStep * xPrestep;
		
		for(int i = xMinimum; i < xMaximum; i++) {
			final int index = i + j * getWidth();
			
			if(depth < this.zBuffer[index]) {
				this.zBuffer[index] = depth;
				
				final float z = 1.0F / zReciprocal;
				
				final int sourceX = (int)((textureCoordinateX * z) * (bitmap.getWidth() - 1) + 0.5F);
				final int sourceY = (int)((textureCoordinateY * z) * (bitmap.getHeight() - 1) + 0.5F);
				
				copyPixel(i, j, sourceX, sourceY, bitmap, Math.min(lightIntensity, 1.0F));
			}
			
			depth += depthXStep;
			lightIntensity += lightIntensityXStep;
			textureCoordinateX += textureCoordinateXXStep;
			textureCoordinateY += textureCoordinateYXStep;
			zReciprocal += zReciprocalXStep;
		}
	}
	
	private void doFillTriangle(final Vertex3F a, final Vertex3F b, final Vertex3F c, final Bitmap bitmap) {
		Vertex3F minimumY = Vertex3F.transformAndDivide(a, this.screenSpaceTransform, this.identity);
		Vertex3F middleY = Vertex3F.transformAndDivide(b, this.screenSpaceTransform, this.identity);
		Vertex3F maximumY = Vertex3F.transformAndDivide(c, this.screenSpaceTransform, this.identity);
		
		if(doComputeTriangleAreaMultipliedByTwo(minimumY, maximumY, middleY) >= 0.0F) {
			return;
		}
		
		if(maximumY.getPosition().y < middleY.getPosition().y) {
			final Vertex3F temporary = maximumY;
			
			maximumY = middleY;
			middleY = temporary;
		}
		
		if(middleY.getPosition().y < minimumY.getPosition().y) {
			final Vertex3F temporary = middleY;
			
			middleY = minimumY;
			minimumY = temporary;
		}
		
		if(maximumY.getPosition().y < middleY.getPosition().y) {
			final Vertex3F temporary = maximumY;
			
			maximumY = middleY;
			middleY = temporary;
		}
		
		doScanTriangle(minimumY, middleY, maximumY, doComputeTriangleAreaMultipliedByTwo(minimumY, maximumY, middleY) >= 0.0F, bitmap);
	}
	
	private void doScanEdges(final Gradients gradients, final Edge a, final Edge b, final boolean handedness, final Bitmap bitmap) {
		Edge left = a;
		Edge right = b;
		
		if(handedness) {
			final Edge temporary = left;
			
			left = right;
			right = temporary;
		}
		
		final int yStart = b.getYStart();
		final int yEnd = b.getYEnd();
		
		for(int i = yStart; i < yEnd; i++) {
			doDrawScanLine(gradients, left, right, i, bitmap);
			
			left.step();
			right.step();
		}
	}
	
	private void doScanTriangle(final Vertex3F minimumY, final Vertex3F middleY, final Vertex3F maximumY, final boolean handedness, final Bitmap bitmap) {
		final Gradients gradients = new Gradients(minimumY, middleY, maximumY);
		
		final Edge topToBottom = new Edge(gradients, minimumY, maximumY, 0);
		final Edge topToMiddle = new Edge(gradients, minimumY, middleY, 0);
		final Edge middleToBottom = new Edge(gradients, middleY, maximumY, 1);
		
		doScanEdges(gradients, topToBottom, topToMiddle, handedness, bitmap);
		doScanEdges(gradients, topToBottom, middleToBottom, handedness, bitmap);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static Matrix44F doScreenSpaceTransform(final float width, final float height) {
		final float halfWidth = width * 0.5F;
		final float halfHeight = height * 0.5F;
		
		return new Matrix44F(halfWidth, 0.0F, 0.0F, halfWidth - 0.5F, 0.0F, -halfHeight, 0.0F, halfHeight - 0.5F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F);
	}
	
	private static boolean doClipPolygonAxis(final List<Vertex3F> vertices0, final List<Vertex3F> vertices1, final int componentIndex) {
		doClipPolygonComponent(vertices0, componentIndex, 1.0F, vertices1);
		
		vertices0.clear();
		
		if(vertices1.isEmpty()) {
			return false;
		}
		
		doClipPolygonComponent(vertices1, componentIndex, -1.0F, vertices0);
		
		vertices1.clear();
		
		return !vertices0.isEmpty();
	}
	
	private static float doComputeTriangleAreaMultipliedByTwo(final Vertex3F a, final Vertex3F b, final Vertex3F c) {
		final Point4F aP = a.getPosition();
		final Point4F bP = b.getPosition();
		final Point4F cP = c.getPosition();
		
		final float x1 = bP.x - aP.x;
		final float y1 = bP.y - aP.y;
		
		final float x2 = cP.x - aP.x;
		final float y2 = cP.y - aP.y;
		
		return x1 * y2 - x2 * y1;
	}
	
	private static void doClipPolygonComponent(final List<Vertex3F> vertices, final int componentIndex, final float componentFactor, final List<Vertex3F> result) {
		Vertex3F previousVertex = vertices.get(vertices.size() - 1);
		
		Point4F previousPosition = previousVertex.getPosition();
		
		float previousComponent = previousPosition.getComponent(componentIndex) * componentFactor;
		
		boolean previousInside = previousComponent <= previousPosition.w;
		
		for(final Vertex3F currentVertex : vertices) {
			final Point4F currentPosition = currentVertex.getPosition();
			
			final float currentComponent = currentPosition.getComponent(componentIndex) * componentFactor;
			
			final boolean currentInside = currentComponent <= currentPosition.w;
			
			if(currentInside ^ previousInside) {
				final float fraction = (previousPosition.w - previousComponent) / ((previousPosition.w - previousComponent) - (currentPosition.w - currentComponent));
				
				result.add(Vertex3F.lerp(previousVertex, currentVertex, fraction));
			}
			
			if(currentInside) {
				result.add(currentVertex);
			}
			
			previousVertex = currentVertex;
			previousPosition = currentPosition;
			previousComponent = currentComponent;
			previousInside = currentInside;
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static final class Edge {
		private float depth;
		private float depthStep;
		private float lightIntensity;
		private float lightIntensityStep;
		private float textureCoordinateX;
		private float textureCoordinateXStep;
		private float textureCoordinateY;
		private float textureCoordinateYStep;
		private float x;
		private float xStep;
		private float zReciprocal;
		private float zReciprocalStep;
		private int yEnd;
		private int yStart;
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		public Edge(final Gradients gradients, final Vertex3F a, final Vertex3F b, final int minimumYIndex) {
			final Point4F aP = a.getPosition();
			final Point4F bP = b.getPosition();
			
			this.yStart = toInt(ceil(aP.y));
			this.yEnd = toInt(ceil(bP.y));
			
			final float xDistance = bP.x - aP.x;
			final float yDistance = bP.y - aP.y;
			
			final float yPrestep = this.yStart - aP.y;
			
			this.xStep = xDistance / yDistance;
			this.x = aP.x + yPrestep * this.xStep;
			
			final float xPrestep = this.x - aP.x;
			
			this.textureCoordinateX = gradients.getTextureCoordinateX(minimumYIndex) + gradients.getTextureCoordinateXXStep() * xPrestep + gradients.getTextureCoordinateXYStep() * yPrestep;
			this.textureCoordinateXStep = gradients.getTextureCoordinateXYStep() + gradients.getTextureCoordinateXXStep() * this.xStep;
			
			this.textureCoordinateY = gradients.getTextureCoordinateY(minimumYIndex) + gradients.getTextureCoordinateYXStep() * xPrestep + gradients.getTextureCoordinateYYStep() * yPrestep;
			this.textureCoordinateYStep = gradients.getTextureCoordinateYYStep() + gradients.getTextureCoordinateYXStep() * this.xStep;
			
			this.zReciprocal = gradients.getZReciprocal(minimumYIndex) + gradients.getZReciprocalXStep() * xPrestep + gradients.getZReciprocalYStep() * yPrestep;
			this.zReciprocalStep = gradients.getZReciprocalYStep() + gradients.getZReciprocalXStep() * this.xStep;
			
			this.depth = gradients.getDepth(minimumYIndex) + gradients.getDepthXStep() * xPrestep + gradients.getDepthYStep() * yPrestep;
			this.depthStep = gradients.getDepthYStep() + gradients.getDepthXStep() * this.xStep;
			
			this.lightIntensity = gradients.getLightIntensity(minimumYIndex) + gradients.getLightIntensityXStep() * xPrestep + gradients.getLightIntensityYStep() * yPrestep;
			this.lightIntensityStep = gradients.getLightIntensityYStep() + gradients.getLightIntensityXStep() * this.xStep;
		}
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		public float getDepth() {
			return this.depth;
		}
		
		public float getLightIntensity() {
			return this.lightIntensity;
		}
		
		public float getTextureCoordinateX() {
			return this.textureCoordinateX;
		}
		
		public float getTextureCoordinateY() {
			return this.textureCoordinateY;
		}
		
		public float getX() {
			return this.x;
		}
		
		public float getZReciprocal() {
			return this.zReciprocal;
		}
		
		public int getYEnd() {
			return this.yEnd;
		}
		
		public int getYStart() {
			return this.yStart;
		}
		
		public void step() {
			this.depth += this.depthStep;
			this.lightIntensity += this.lightIntensityStep;
			this.textureCoordinateX += this.textureCoordinateXStep;
			this.textureCoordinateY += this.textureCoordinateYStep;
			this.x += this.xStep;
			this.zReciprocal += this.zReciprocalStep;
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static final class Gradients {
		private static final Vector3F LIGHT_DIRECTION = Vector3F.normalize(new Vector3F(0.0F, 0.0F, 1.0F));
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		private final float depthXStep;
		private final float depthYStep;
		private final float lightIntensityXStep;
		private final float lightIntensityYStep;
		private final float textureCoordinateXXStep;
		private final float textureCoordinateXYStep;
		private final float textureCoordinateYXStep;
		private final float textureCoordinateYYStep;
		private final float zReciprocalXStep;
		private final float zReciprocalYStep;
		private final float[] depth = new float[3];
		private final float[] lightIntensity = new float[3];
		private final float[] textureCoordinateX = new float[3];
		private final float[] textureCoordinateY = new float[3];
		private final float[] zReciprocals = new float[3];
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		public Gradients(final Vertex3F a, final Vertex3F b, final Vertex3F c) {
			final Point4F aP = a.getPosition();
			final Point4F bP = b.getPosition();
			final Point4F cP = c.getPosition();
			
			final float dXReciprocal = 1.0F / (((bP.x - cP.x) * (aP.y - cP.y)) - ((aP.x - cP.x) * (bP.y - cP.y)));
			final float dYReciprocal = -dXReciprocal;
			
			this.depth[0] = aP.z;
			this.depth[1] = bP.z;
			this.depth[2] = cP.z;
			
			this.lightIntensity[0] = Floats.saturate(Vector3F.dotProduct(a.getNormal(), LIGHT_DIRECTION)) * 0.9F + 0.1F;
			this.lightIntensity[1] = Floats.saturate(Vector3F.dotProduct(b.getNormal(), LIGHT_DIRECTION)) * 0.9F + 0.1F;
			this.lightIntensity[2] = Floats.saturate(Vector3F.dotProduct(c.getNormal(), LIGHT_DIRECTION)) * 0.9F + 0.1F;
			
			this.zReciprocals[0] = 1.0F / aP.w;
			this.zReciprocals[1] = 1.0F / bP.w;
			this.zReciprocals[2] = 1.0F / cP.w;
			
			this.textureCoordinateX[0] = a.getTextureCoordinates().x * this.zReciprocals[0];
			this.textureCoordinateX[1] = b.getTextureCoordinates().x * this.zReciprocals[1];
			this.textureCoordinateX[2] = c.getTextureCoordinates().x * this.zReciprocals[2];
			
			this.textureCoordinateY[0] = a.getTextureCoordinates().y * this.zReciprocals[0];
			this.textureCoordinateY[1] = b.getTextureCoordinates().y * this.zReciprocals[1];
			this.textureCoordinateY[2] = c.getTextureCoordinates().y * this.zReciprocals[2];
			
			this.textureCoordinateXXStep = doCalculateXStep(this.textureCoordinateX, aP, bP, cP, dXReciprocal);
			this.textureCoordinateXYStep = doCalculateYStep(this.textureCoordinateX, aP, bP, cP, dYReciprocal);
			this.textureCoordinateYXStep = doCalculateXStep(this.textureCoordinateY, aP, bP, cP, dXReciprocal);
			this.textureCoordinateYYStep = doCalculateYStep(this.textureCoordinateY, aP, bP, cP, dYReciprocal);
			this.zReciprocalXStep = doCalculateXStep(this.zReciprocals, aP, bP, cP, dXReciprocal);
			this.zReciprocalYStep = doCalculateYStep(this.zReciprocals, aP, bP, cP, dYReciprocal);
			this.depthXStep = doCalculateXStep(this.depth, aP, bP, cP, dXReciprocal);
			this.depthYStep = doCalculateYStep(this.depth, aP, bP, cP, dYReciprocal);
			this.lightIntensityXStep = doCalculateXStep(this.lightIntensity, aP, bP, cP, dXReciprocal);
			this.lightIntensityYStep = doCalculateYStep(this.lightIntensity, aP, bP, cP, dYReciprocal);
		}
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		public float getDepth(final int index) {
			return this.depth[index];
		}
		
		public float getDepthXStep() {
			return this.depthXStep;
		}
		
		public float getDepthYStep() {
			return this.depthYStep;
		}
		
		public float getLightIntensity(final int index) {
			return this.lightIntensity[index];
		}
		
		public float getLightIntensityXStep() {
			return this.lightIntensityXStep;
		}
		
		public float getLightIntensityYStep() {
			return this.lightIntensityYStep;
		}
		
		public float getTextureCoordinateX(final int index) {
			return this.textureCoordinateX[index];
		}
		
		public float getTextureCoordinateXXStep() {
			return this.textureCoordinateXXStep;
		}
		
		public float getTextureCoordinateXYStep() {
			return this.textureCoordinateXYStep;
		}
		
		public float getTextureCoordinateY(final int index) {
			return this.textureCoordinateY[index];
		}
		
		public float getTextureCoordinateYXStep() {
			return this.textureCoordinateYXStep;
		}
		
		public float getTextureCoordinateYYStep() {
			return this.textureCoordinateYYStep;
		}
		
		public float getZReciprocal(final int index) {
			return this.zReciprocals[index];
		}
		
		public float getZReciprocalXStep() {
			return this.zReciprocalXStep;
		}
		
		public float getZReciprocalYStep() {
			return this.zReciprocalYStep;
		}
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		private static float doCalculateXStep(final float[] values, final Point4F a, final Point4F b, final Point4F c, final float dXReciprocal) {
			return (((values[1] - values[2]) * (a.y - c.y)) - ((values[0] - values[2]) * (b.y - c.y))) * dXReciprocal;
		}
		
		private static float doCalculateYStep(final float[] values, final Point4F a, final Point4F b, final Point4F c, final float dYReciprocal) {
			return (((values[1] - values[2]) * (a.x - c.x)) - ((values[0] - values[2]) * (b.x - c.x))) * dYReciprocal;
		}
	}
}