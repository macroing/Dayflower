/**
 * Copyright 2020 J&#246;rgen Lundgren
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
package org.dayflower.scene;

import static org.dayflower.util.Doubles.acos;
import static org.dayflower.util.Doubles.cos;
import static org.dayflower.util.Doubles.exp;
import static org.dayflower.util.Doubles.saturate;
import static org.dayflower.util.Doubles.tan;
import static org.dayflower.util.Floats.PI;
import static org.dayflower.util.Floats.acos;
import static org.dayflower.util.Floats.max;
import static org.dayflower.util.Floats.saturate;
import static org.dayflower.util.Floats.sin;

import org.dayflower.geometry.OrthonormalBasis33F;
import org.dayflower.geometry.Ray3F;
import org.dayflower.geometry.Vector3F;
import org.dayflower.image.ChromaticSpectralCurve;
import org.dayflower.image.Color3F;

/**
 * A {@code PerezBackground} is a {@link Background} implementation of the Perez algorithm.
 * <p>
 * This class is mutable and therefore not thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class PerezBackground implements Background {
	private static final int HISTOGRAM_RESOLUTION_X = 32;
	private static final int HISTOGRAM_RESOLUTION_Y = 32;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private OrthonormalBasis33F orthonormalBasis;
	private Vector3F sunDirection;
	private Vector3F sunDirectionWorldSpace;
	private double[] perezRelativeLuminance;
	private double[] perezX;
	private double[] perezY;
	private double[] zenith;
	private float theta;
	private float turbidity;
	private float[] histogramColumn;
	private float[][] histogramImage;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code PerezBackground} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new PerezBackground(2.0F);
	 * }
	 * </pre>
	 */
	public PerezBackground() {
		this(2.0F);
	}
	
	/**
	 * Constructs a new {@code PerezBackground} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new PerezBackground(turbidity, new Vector3F(1.0F, 1.0F, -1.0F));
	 * }
	 * </pre>
	 * 
	 * @param turbidity the turbidity associated with this {@code PerezBackground} instance
	 */
	public PerezBackground(final float turbidity) {
		this(turbidity, new Vector3F(1.0F, 1.0F, -1.0F));
	}
	
	/**
	 * Constructs a new {@code PerezBackground} instance.
	 * <p>
	 * If {@code sunDirectionWorldSpace} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param turbidity the turbidity associated with this {@code PerezBackground} instance
	 * @param sunDirectionWorldSpace the sun direction in world space associated with this {@code PerezBackground} instance
	 * @throws NullPointerException thrown if, and only if, {@code sunDirectionWorldSpace} is {@code null}
	 */
	public PerezBackground(final float turbidity, final Vector3F sunDirectionWorldSpace) {
		set(turbidity, sunDirectionWorldSpace);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link Color3F} instance with the radiance along {@code ray}.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray a {@link Ray3F} instance
	 * @return a {@code Color3F} instance with the radiance along {@code ray}
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	@Override
	public Color3F radiance(final Ray3F ray) {
		return Color3F.minimumTo0(doRadiance(Vector3F.normalize(Vector3F.transformReverse(ray.getDirection(), this.orthonormalBasis))));
	}
	
	/**
	 * Sets the parameters for this {@code PerezBackground} instance.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * perezBackground.set(2.0F);
	 * }
	 * </pre>
	 */
	public void set() {
		set(2.0F);
	}
	
	/**
	 * Sets the parameters for this {@code PerezBackground} instance.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * perezBackground.set(turbidity, new Vector3F(1.0F, 1.0F, -1.0F));
	 * }
	 * </pre>
	 * 
	 * @param turbidity the turbidity associated with this {@code PerezBackground} instance
	 */
	public void set(final float turbidity) {
		set(turbidity, new Vector3F(1.0F, 1.0F, -1.0F));
	}
	
	/**
	 * Sets the parameters for this {@code PerezBackground} instance.
	 * <p>
	 * If {@code sunDirectionWorldSpace} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param turbidity the turbidity associated with this {@code PerezBackground} instance
	 * @param sunDirectionWorldSpace the sun direction in world space associated with this {@code PerezBackground} instance
	 * @throws NullPointerException thrown if, and only if, {@code sunDirectionWorldSpace} is {@code null}
	 */
	public void set(final float turbidity, final Vector3F sunDirectionWorldSpace) {
		doSetOrthonormalBasis();
		doSetTurbidity(turbidity);
		doSetSunDirectionWorldSpace(sunDirectionWorldSpace);
		doSetSunDirection();
		doSetTheta();
		doSetZenith();
		doInitializePerezRelativeLuminance();
		doInitializePerezX();
		doInitializePerezY();
		doInitializeHistogram();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private Color3F doRadiance(final Vector3F direction) {
		if(direction.getZ() < 0.0F) {
			return Color3F.BLACK;
		}
		
		final Vector3F directionSaturated = Vector3F.normalize(new Vector3F(direction.getX(), direction.getY(), max(direction.getZ(), 0.001F)));
		
		final double theta = acos(saturate(directionSaturated.getZ(), -1.0D, 1.0D));
		final double gamma = acos(saturate(Vector3F.dotProduct(directionSaturated, this.sunDirection), -1.0D, 1.0D));
		final double relativeLuminance = doCalculatePerezFunction(this.perezRelativeLuminance, theta, gamma, this.zenith[0]) * 1.0e-4D;
		final double x = doCalculatePerezFunction(this.perezX, theta, gamma, this.zenith[1]);
		final double y = doCalculatePerezFunction(this.perezY, theta, gamma, this.zenith[2]);
		
		final Color3F colorXYZ = ChromaticSpectralCurve.getColorXYZ((float)(x), (float)(y));
		
		final float x0 = (float)(colorXYZ.getX() * relativeLuminance / colorXYZ.getY());
		final float y0 = (float)(relativeLuminance);
		final float z0 = (float)(colorXYZ.getZ() * relativeLuminance / colorXYZ.getY());
		
		return Color3F.convertXYZToRGBUsingSRGB(new Color3F(x0, y0, z0));
	}
	
	private double doCalculatePerezFunction(final double[] lam, final double theta, final double gamma, final double lvz) {
		final double den = ((1.0D + lam[0] * exp(lam[1])) * (1.0D + lam[2] * exp(lam[3] * this.theta) + lam[4] * cos(this.theta) * cos(this.theta)));
		final double num = ((1.0D + lam[0] * exp(lam[1] / cos(theta))) * (1.0D + lam[2] * exp(lam[3] * gamma) + lam[4] * cos(gamma) * cos(gamma)));
		
		return lvz * num / den;
	}
	
	private void doInitializeHistogram() {
		final float deltaU = 1.0F / HISTOGRAM_RESOLUTION_X;
		final float deltaV = 1.0F / HISTOGRAM_RESOLUTION_Y;
		
		this.histogramColumn = new float[HISTOGRAM_RESOLUTION_X];
		this.histogramImage  = new float[HISTOGRAM_RESOLUTION_X][HISTOGRAM_RESOLUTION_Y];
		
		for(int x = 0; x < HISTOGRAM_RESOLUTION_X; x++) {
			for(int y = 0; y < HISTOGRAM_RESOLUTION_Y; y++) {
				final float u = (x + 0.5F) * deltaU;
				final float v = (y + 0.5F) * deltaV;
				
				final Color3F colorRGB = doRadiance(Vector3F.directionSpherical(u, v));
				
				this.histogramImage[x][y] = colorRGB.luminance() * sin(PI * v);
				
				if(y > 0) {
					this.histogramImage[x][y] += this.histogramImage[x][y - 1];
				}
			}
			
			this.histogramColumn[x] = this.histogramImage[x][HISTOGRAM_RESOLUTION_Y - 1];
			
			if(x > 0) {
				this.histogramColumn[x] += this.histogramColumn[x - 1];
			}
			
			for(int y = 0; y < HISTOGRAM_RESOLUTION_Y; y++) {
				this.histogramImage[x][y] /= this.histogramImage[x][HISTOGRAM_RESOLUTION_Y - 1];
			}
		}
		
		for(int x = 0; x < HISTOGRAM_RESOLUTION_X; x++) {
			this.histogramColumn[x] /= this.histogramColumn[HISTOGRAM_RESOLUTION_X - 1];
		}
	}
	
	private void doInitializePerezRelativeLuminance() {
		this.perezRelativeLuminance    = new double[5];
		this.perezRelativeLuminance[0] = +0.17872D * this.turbidity - 1.46303D;
		this.perezRelativeLuminance[1] = -0.35540D * this.turbidity + 0.42749D;
		this.perezRelativeLuminance[2] = -0.02266D * this.turbidity + 5.32505D;
		this.perezRelativeLuminance[3] = +0.12064D * this.turbidity - 2.57705D;
		this.perezRelativeLuminance[4] = -0.06696D * this.turbidity + 0.37027D;
	}
	
	private void doInitializePerezX() {
		this.perezX    = new double[5];
		this.perezX[0] = -0.01925D * this.turbidity - 0.25922D;
		this.perezX[1] = -0.06651D * this.turbidity + 0.00081D;
		this.perezX[2] = -0.00041D * this.turbidity + 0.21247D;
		this.perezX[3] = -0.06409D * this.turbidity - 0.89887D;
		this.perezX[4] = -0.00325D * this.turbidity + 0.04517D;
	}
	
	private void doInitializePerezY() {
		this.perezY    = new double[5];
		this.perezY[0] = -0.01669D * this.turbidity - 0.26078D;
		this.perezY[1] = -0.09495D * this.turbidity + 0.00921D;
		this.perezY[2] = -0.00792D * this.turbidity + 0.21023D;
		this.perezY[3] = -0.04405D * this.turbidity - 1.65369D;
		this.perezY[4] = -0.01092D * this.turbidity + 0.05291D;
	}
	
	private void doSetOrthonormalBasis() {
		this.orthonormalBasis = new OrthonormalBasis33F(Vector3F.y(), Vector3F.z(), Vector3F.x());
	}
	
	private void doSetSunDirection() {
		this.sunDirection = Vector3F.normalize(Vector3F.transformReverse(this.sunDirectionWorldSpace, this.orthonormalBasis));
	}
	
	private void doSetSunDirectionWorldSpace(final Vector3F sunDirectionWorldSpace) {
		this.sunDirectionWorldSpace = Vector3F.normalize(sunDirectionWorldSpace);
	}
	
	private void doSetTheta() {
		this.theta = acos(saturate(this.sunDirection.getZ(), -1.0F, 1.0F));
	}
	
	private void doSetTurbidity(final float turbidity) {
		this.turbidity = turbidity;
	}
	
	private void doSetZenith() {
		this.zenith    = new double[3];
		this.zenith[0] = ((4.0453D * this.turbidity - 4.9710D) * tan((4.0D / 9.0D - this.turbidity / 120.0D) * (Math.PI - 2.0D * this.theta)) - 0.2155D * this.turbidity + 2.4192D) * 1000.0D;
		this.zenith[1] = (0.00165D * (this.theta * this.theta * this.theta) - 0.00374D * (this.theta * this.theta) + 0.00208D * this.theta + 0.0D) * (this.turbidity * this.turbidity) + (-0.02902D * (this.theta * this.theta * this.theta) + 0.06377D * (this.theta * this.theta) - 0.03202D * this.theta + 0.00394D) * this.turbidity + (0.11693D * (this.theta * this.theta * this.theta) - 0.21196D * (this.theta * this.theta) + 0.06052D * this.theta + 0.25885D);
		this.zenith[2] = (0.00275D * (this.theta * this.theta * this.theta) - 0.00610D * (this.theta * this.theta) + 0.00316D * this.theta + 0.0D) * (this.turbidity * this.turbidity) + (-0.04212D * (this.theta * this.theta * this.theta) + 0.08970D * (this.theta * this.theta) - 0.04153D * this.theta + 0.00515D) * this.turbidity + (0.15346D * (this.theta * this.theta * this.theta) - 0.26756D * (this.theta * this.theta) + 0.06669D * this.theta + 0.26688D);
	}
}