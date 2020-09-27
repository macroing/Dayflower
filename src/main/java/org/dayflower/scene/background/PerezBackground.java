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
package org.dayflower.scene.background;

import static org.dayflower.util.Doubles.acos;
import static org.dayflower.util.Doubles.cos;
import static org.dayflower.util.Doubles.exp;
import static org.dayflower.util.Doubles.pow;
import static org.dayflower.util.Doubles.saturate;
import static org.dayflower.util.Doubles.tan;
import static org.dayflower.util.Floats.PI;
import static org.dayflower.util.Floats.acos;
import static org.dayflower.util.Floats.max;
import static org.dayflower.util.Floats.random;
import static org.dayflower.util.Floats.saturate;
import static org.dayflower.util.Floats.sin;

import java.util.ArrayList;
import java.util.List;

import org.dayflower.geometry.OrthonormalBasis33F;
import org.dayflower.geometry.Point3F;
import org.dayflower.geometry.Ray3F;
import org.dayflower.geometry.SurfaceIntersection3F;
import org.dayflower.geometry.Vector3F;
import org.dayflower.image.ChromaticSpectralCurve;
import org.dayflower.image.Color3F;
import org.dayflower.image.ConstantSpectralCurve;
import org.dayflower.image.IrregularSpectralCurve;
import org.dayflower.image.RegularSpectralCurve;
import org.dayflower.image.SpectralCurve;
import org.dayflower.scene.Background;
import org.dayflower.scene.BackgroundSample;
import org.dayflower.scene.Intersection;

/**
 * A {@code PerezBackground} is a {@link Background} implementation of the Perez algorithm.
 * <p>
 * This class is mutable and therefore not thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class PerezBackground implements Background {
	private static final SpectralCurve K_GAS_ABSORPTION_ATTENUATION_SPECTRAL_CURVE;
	private static final SpectralCurve K_OZONE_ABSORPTION_ATTENUATION_SPECTRAL_CURVE;
	private static final SpectralCurve K_WATER_VAPOR_ABSORPTION_ATTENUATION_SPECTRAL_CURVE;
	private static final SpectralCurve SOL_SPECTRAL_CURVE;
	private static final float[] K_GAS_ABSORPTION_ATTENUATION_AMPLITUDES;
	private static final float[] K_GAS_ABSORPTION_ATTENUATION_WAVELENGTHS;
	private static final float[] K_OZONE_ABSORPTION_ATTENUATION_AMPLITUDES;
	private static final float[] K_OZONE_ABSORPTION_ATTENUATION_WAVELENGTHS;
	private static final float[] K_WATER_VAPOR_ABSORPTION_ATTENUATION_AMPLITUDES;
	private static final float[] K_WATER_VAPOR_ABSORPTION_ATTENUATION_WAVELENGTHS;
	private static final float[] SOL_AMPLITUDES;
	private static final int HISTOGRAM_RESOLUTION_X;
	private static final int HISTOGRAM_RESOLUTION_Y;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	static {
		K_GAS_ABSORPTION_ATTENUATION_AMPLITUDES = new float[] {0.0F, 3.0F, 0.210F, 0.0F};
		K_GAS_ABSORPTION_ATTENUATION_WAVELENGTHS = new float[] {759.0F, 760.0F, 770.0F, 771.0F};
		K_OZONE_ABSORPTION_ATTENUATION_AMPLITUDES = new float[] {10.0F, 4.8F, 2.7F, 1.35F, 0.8F, 0.380F, 0.160F, 0.075F, 0.04F, 0.019F, 0.007F, 0.0F, 0.003F, 0.003F, 0.004F, 0.006F, 0.008F, 0.009F, 0.012F, 0.014F, 0.017F, 0.021F, 0.025F, 0.03F, 0.035F, 0.04F, 0.045F, 0.048F, 0.057F, 0.063F, 0.07F, 0.075F, 0.08F, 0.085F, 0.095F, 0.103F, 0.110F, 0.12F, 0.122F, 0.12F, 0.118F, 0.115F, 0.12F, 0.125F, 0.130F, 0.12F, 0.105F, 0.09F, 0.079F, 0.067F, 0.057F, 0.048F, 0.036F, 0.028F, 0.023F, 0.018F, 0.014F, 0.011F, 0.010F, 0.009F, 0.007F, 0.004F, 0.0F, 0.0F};
		K_OZONE_ABSORPTION_ATTENUATION_WAVELENGTHS = new float[] {300.0F, 305.0F, 310.0F, 315.0F, 320.0F, 325.0F, 330.0F, 335.0F, 340.0F, 345.0F, 350.0F, 355.0F, 445.0F, 450.0F, 455.0F, 460.0F, 465.0F, 470.0F, 475.0F, 480.0F, 485.0F, 490.0F, 495.0F, 500.0F, 505.0F, 510.0F, 515.0F, 520.0F, 525.0F, 530.0F, 535.0F, 540.0F, 545.0F, 550.0F, 555.0F, 560.0F, 565.0F, 570.0F, 575.0F, 580.0F, 585.0F, 590.0F, 595.0F, 600.0F, 605.0F, 610.0F, 620.0F, 630.0F, 640.0F, 650.0F, 660.0F, 670.0F, 680.0F, 690.0F, 700.0F, 710.0F, 720.0F, 730.0F, 740.0F, 750.0F, 760.0F, 770.0F, 780.0F, 790.0F};
		K_WATER_VAPOR_ABSORPTION_ATTENUATION_AMPLITUDES = new float[] {0.0F, 0.160e-1F, 0.240e-1F, 0.125e-1F, 0.100e+1F, 0.870F, 0.610e-1F, 0.100e-2F, 0.100e-4F, 0.100e-4F, 0.600e-3F, 0.175e-1F, 0.360e-1F};
		K_WATER_VAPOR_ABSORPTION_ATTENUATION_WAVELENGTHS = new float[] {689.0F, 690.0F, 700.0F, 710.0F, 720.0F, 730.0F, 740.0F, 750.0F, 760.0F, 770.0F, 780.0F, 790.0F, 800.0F};
		
		SOL_AMPLITUDES = new float[] {165.5F, 162.3F, 211.2F, 258.8F, 258.2F, 242.3F, 267.6F, 296.6F, 305.4F, 300.6F, 306.6F, 288.3F, 287.1F, 278.2F, 271.0F, 272.3F, 263.6F, 255.0F, 250.6F, 253.1F, 253.5F, 251.3F, 246.3F, 241.7F, 236.8F, 232.1F, 228.2F, 223.4F, 219.7F, 215.3F, 211.0F, 207.3F, 202.4F, 198.7F, 194.3F, 190.7F, 186.3F, 182.6F};
		
		K_GAS_ABSORPTION_ATTENUATION_SPECTRAL_CURVE = new IrregularSpectralCurve(K_GAS_ABSORPTION_ATTENUATION_AMPLITUDES, K_GAS_ABSORPTION_ATTENUATION_WAVELENGTHS);
		K_OZONE_ABSORPTION_ATTENUATION_SPECTRAL_CURVE = new IrregularSpectralCurve(K_OZONE_ABSORPTION_ATTENUATION_AMPLITUDES, K_OZONE_ABSORPTION_ATTENUATION_WAVELENGTHS);
		K_WATER_VAPOR_ABSORPTION_ATTENUATION_SPECTRAL_CURVE = new IrregularSpectralCurve(K_WATER_VAPOR_ABSORPTION_ATTENUATION_AMPLITUDES, K_WATER_VAPOR_ABSORPTION_ATTENUATION_WAVELENGTHS);
		
		SOL_SPECTRAL_CURVE = new RegularSpectralCurve(380.0F, 750.0F, SOL_AMPLITUDES);
		
		HISTOGRAM_RESOLUTION_X = 32;
		HISTOGRAM_RESOLUTION_Y = 32;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private Color3F sunColor;
	private OrthonormalBasis33F orthonormalBasis;
	private SpectralCurve sunSpectralRadiance;
	private Vector3F sunDirection;
	private Vector3F sunDirectionWorldSpace;
	private double[] perezRelativeLuminance;
	private double[] perezX;
	private double[] perezY;
	private double[] zenith;
	private float jacobian;
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
	 * Samples this {@code PerezBackground} instance from {@code intersection}.
	 * <p>
	 * Returns a {@code List} of {@link BackgroundSample} instances.
	 * <p>
	 * If {@code intersection} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param intersection the {@link Intersection} instance to sample from
	 * @return a {@code List} of {@code BackgroundSample} instances
	 * @throws NullPointerException thrown if, and only if, {@code intersection} is {@code null}
	 */
	@Override
	public List<BackgroundSample> sample(final Intersection intersection) {
		final List<BackgroundSample> backgroundSamples = new ArrayList<>();
		
		final SurfaceIntersection3F surfaceIntersection = intersection.getSurfaceIntersectionWorldSpace();
		
		final Point3F surfaceIntersectionPoint = surfaceIntersection.getSurfaceIntersectionPoint();
		
		final Vector3F surfaceNormalG = surfaceIntersection.getSurfaceNormalG();
		final Vector3F surfaceNormalS = surfaceIntersection.getSurfaceNormalS();
		
		if(Vector3F.dotProduct(this.sunDirectionWorldSpace, surfaceNormalG) > 0.0F && Vector3F.dotProduct(this.sunDirectionWorldSpace, surfaceNormalS) > 0.0F) {
			backgroundSamples.add(new BackgroundSample(this.sunColor, new Ray3F(surfaceIntersectionPoint, this.sunDirectionWorldSpace)));
		}
		
		final int samples = 1;
		
		final float[] histogramCol = this.histogramColumn;
		final float[][] histogramImage = this.histogramImage;
		
		for(int i = 0; i < samples; i++) {
			final float randomX = random();
			final float randomY = random();
			
			int x = 0;
			int y = 0;
			
			while(randomX >= histogramCol[x] && x < histogramCol.length - 1) {
				x++;
			}
			
			final float[] histogramRow = histogramImage[x];
			
			while(randomY >= histogramRow[y] && y < histogramRow.length - 1) {
				y++;
			}
			
			final float u = x == 0 ? randomX / histogramCol[0] : (randomX - histogramCol[x - 1]) / (histogramCol[x] - histogramCol[x - 1]);
			final float v = y == 0 ? randomY / histogramRow[0] : (randomY - histogramRow[y - 1]) / (histogramRow[y] - histogramRow[y - 1]);
			
			final float px = x == 0 ? histogramCol[0] : histogramCol[x] - histogramCol[x - 1];
			final float py = y == 0 ? histogramRow[0] : histogramRow[y] - histogramRow[y - 1];
			
			final float su = (x + u) / histogramCol.length;
			final float sv = (y + v) / histogramRow.length;
			
			final float probabilityReciprocal = sin(sv * PI) * this.jacobian / (samples * px * py);
			
			final Vector3F directionLocal = Vector3F.directionSpherical(su, sv);
			final Vector3F direction = Vector3F.transform(directionLocal, this.orthonormalBasis);
			
			if(Vector3F.dotProduct(direction, surfaceNormalG) > 0.0F && Vector3F.dotProduct(direction, surfaceNormalS) > 0.0F) {
				backgroundSamples.add(new BackgroundSample(Color3F.multiply(doRadiance(directionLocal), probabilityReciprocal), new Ray3F(surfaceIntersectionPoint, direction)));
			}
		}
		
		return backgroundSamples;
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
		doSetSunColorAndSunSpectralRadiance();
		doSetZenith();
		doInitializeJacobian();
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
	
	private void doInitializeJacobian() {
		this.jacobian = (2.0F * PI * PI) / (HISTOGRAM_RESOLUTION_X * HISTOGRAM_RESOLUTION_Y);
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
		this.orthonormalBasis = new OrthonormalBasis33F(Vector3F.y(), Vector3F.x());
	}
	
	private void doSetSunColorAndSunSpectralRadiance() {
		if(this.sunDirection.getZ() > 0.0F) {
			this.sunSpectralRadiance = doCalculateAttenuatedSunlight(this.theta, this.turbidity);
			this.sunColor = Color3F.minimumTo0(Color3F.convertXYZToRGBUsingSRGB(Color3F.multiply(this.sunSpectralRadiance.toColorXYZ(), 1.0e-4F)));
		} else {
			this.sunSpectralRadiance = new ConstantSpectralCurve(0.0F);
			this.sunColor = Color3F.BLACK;
		}
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
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static SpectralCurve doCalculateAttenuatedSunlight(final float theta, final float turbidity) {
		final float[] spectrum = new float[91];
		
		final double alpha = 1.3D;
		final double lozone = 0.35D;
		final double w = 2.0D;
		final double beta = 0.04608365822050D * turbidity - 0.04586025928522D;
		final double relativeOpticalMass = 1.0D / (cos(theta) + 0.000940D * pow(1.6386D - theta, -1.253D));
		
		for(int i = 0, lambda = 350; lambda <= 800; i++, lambda += 5) {
			final double tauRayleighScattering = exp(-relativeOpticalMass * 0.008735D * pow(lambda / 1000.0D, -4.08D));
			final double tauAerosolAttenuation = exp(-relativeOpticalMass * beta * pow(lambda / 1000.0D, -alpha));
			final double tauOzoneAbsorptionAttenuation = exp(-relativeOpticalMass * K_OZONE_ABSORPTION_ATTENUATION_SPECTRAL_CURVE.sample(lambda) * lozone);
			final double tauGasAbsorptionAttenuation = exp(-1.41D * K_GAS_ABSORPTION_ATTENUATION_SPECTRAL_CURVE.sample(lambda) * relativeOpticalMass / pow(1.0D + 118.93D * K_GAS_ABSORPTION_ATTENUATION_SPECTRAL_CURVE.sample(lambda) * relativeOpticalMass, 0.45D));
			final double tauWaterVaporAbsorptionAttenuation = exp(-0.2385D * K_WATER_VAPOR_ABSORPTION_ATTENUATION_SPECTRAL_CURVE.sample(lambda) * w * relativeOpticalMass / pow(1.0D + 20.07D * K_WATER_VAPOR_ABSORPTION_ATTENUATION_SPECTRAL_CURVE.sample(lambda) * w * relativeOpticalMass, 0.45D));
			final double amplitude = SOL_SPECTRAL_CURVE.sample(lambda) * tauRayleighScattering * tauAerosolAttenuation * tauOzoneAbsorptionAttenuation * tauGasAbsorptionAttenuation * tauWaterVaporAbsorptionAttenuation;
			
			spectrum[i] = (float)(amplitude);
		}
		
		return new RegularSpectralCurve(350.0F, 800.0F, spectrum);
	}
}