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
package org.dayflower.image;

/**
 * An {@code IrregularSpectralCurve} is an implementation of {@link SpectralCurve} that contains irregular spectral data.
 * <p>
 * This class is immutable and therefore suitable for concurrent use without external synchronization.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class IrregularSpectralCurve extends SpectralCurve {
	/**
	 * An {@code IrregularSpectralCurve} instance for copper.
	 */
	public static final IrregularSpectralCurve COPPER_ETA = new IrregularSpectralCurve(doCreateCopperAmplitudesEta(), doCreateCopperWavelengths());
	
	/**
	 * An {@code IrregularSpectralCurve} instance for copper.
	 */
	public static final IrregularSpectralCurve COPPER_K = new IrregularSpectralCurve(doCreateCopperAmplitudesK(), doCreateCopperWavelengths());
	
	/**
	 * An {@code IrregularSpectralCurve} instance for gold.
	 */
	public static final IrregularSpectralCurve GOLD_ETA = new IrregularSpectralCurve(doCreateGoldAmplitudesEta(), doCreateGoldWavelengths());
	
	/**
	 * An {@code IrregularSpectralCurve} instance for gold.
	 */
	public static final IrregularSpectralCurve GOLD_K = new IrregularSpectralCurve(doCreateGoldAmplitudesK(), doCreateGoldWavelengths());
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final float[] amplitudes;
	private final float[] wavelengths;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code IrregularSpectralCurve} instance.
	 * <p>
	 * If either {@code amplitudes} or {@code wavelengths} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Both arrays {@code amplitudes} and {@code wavelengths} will be cloned.
	 * 
	 * @param amplitudes an array with amplitudes
	 * @param wavelengths an array with wavelengths in nanometers
	 * @throws NullPointerException thrown if, and only if, either {@code amplitudes} or {@code wavelengths} are {@code null}
	 */
	public IrregularSpectralCurve(final float[] amplitudes, final float[] wavelengths) {
		this.amplitudes = amplitudes.clone();
		this.wavelengths = wavelengths.clone();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a sample based on the wavelength {@code lambda} in nanometers.
	 * 
	 * @param lambda the wavelength in nanometers
	 * @return a sample based on the wavelength {@code lambda} in nanometers
	 */
	@Override
	public float sample(final float lambda) {
		if(this.wavelengths.length == 0) {
			return 0.0F;
		} else if(this.wavelengths.length == 1 || lambda <= this.wavelengths[0]) {
			return this.wavelengths[0];
		} else if(lambda >= this.wavelengths[this.wavelengths.length - 1]) {
			return this.wavelengths[this.wavelengths.length - 1];
		} else {
			for(int i = 1; i < this.wavelengths.length; i++) {
				if(lambda < this.wavelengths[i]) {
					final float deltaX = (lambda - this.wavelengths[i - 1]) / (this.wavelengths[i] - this.wavelengths[i - 1]);
					final float sample = (1.0F - deltaX) * this.amplitudes[i - 1] + deltaX * this.amplitudes[i];
					
					return sample;
				}
			}
			
			return this.amplitudes[this.wavelengths.length - 1];
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static float[] doCreateCopperAmplitudesEta() {
		return new float[] {
			1.400313F, 1.380F, 1.358438F, 1.340F, 1.329063F, 1.325F, 1.332500F, 1.340F,
			1.334375F, 1.325F, 1.317812F, 1.310F, 1.300313F, 1.290F, 1.281563F, 1.270F,
			1.249062F, 1.225F, 1.200000F, 1.180F, 1.174375F, 1.175F, 1.177500F, 1.180F,
			1.178125F, 1.175F, 1.172812F, 1.170F, 1.165312F, 1.160F, 1.155312F, 1.150F,
			1.142812F, 1.135F, 1.131562F, 1.120F, 1.092437F, 1.040F, 0.950375F, 0.826F,
			0.645875F, 0.468F, 0.351250F, 0.272F, 0.230813F, 0.214F, 0.209250F, 0.213F,
			0.216250F, 0.223F, 0.236500F, 0.250F, 0.254188F, 0.260F, 0.280000F, 0.300F
		};
	}
	
	private static float[] doCreateCopperAmplitudesK() {
		return new float[] {
			1.662125F, 1.687F, 1.703313F, 1.720F, 1.744563F, 1.770F, 1.791625F, 1.810F,
			1.822125F, 1.834F, 1.851750F, 1.872F, 1.894250F, 1.916F, 1.931688F, 1.950F,
			1.972438F, 2.015F, 2.121562F, 2.210F, 2.177188F, 2.130F, 2.160063F, 2.210F,
			2.249938F, 2.289F, 2.326000F, 2.362F, 2.397625F, 2.433F, 2.469187F, 2.504F,
			2.535875F, 2.564F, 2.589625F, 2.605F, 2.595562F, 2.583F, 2.576500F, 2.599F,
			2.678062F, 2.809F, 3.010750F, 3.240F, 3.458187F, 3.670F, 3.863125F, 4.050F,
			4.239563F, 4.430F, 4.619563F, 4.817F, 5.034125F, 5.260F, 5.485625F, 5.717F
		};
	}
	
	private static float[] doCreateCopperWavelengths() {
		return new float[] {
			298.7570554F, 302.4004341F, 306.13377280F, 309.9604450F, 313.8839949F,
			317.9081487F, 322.0368260F, 326.27415260F, 330.6244747F, 335.0923730F,
			339.6826795F, 344.4004944F, 349.25120560F, 354.2405086F, 359.3744290F,
			364.6593471F, 370.1020239F, 375.70963030F, 381.4897785F, 387.4505563F,
			393.6005651F, 399.9489613F, 406.50550160F, 413.2805933F, 420.2853492F,
			427.5316483F, 435.0322035F, 442.80063570F, 450.8515564F, 459.2006593F,
			467.8648226F, 476.8622231F, 486.21246270F, 495.9367120F, 506.0578694F,
			516.6007417F, 527.5922468F, 539.06164350F, 551.0407911F, 563.5644455F,
			576.6705953F, 590.4008476F, 604.80086830F, 619.9208900F, 635.8162974F,
			652.5483053F, 670.1847459F, 688.80098890F, 708.4810171F, 729.3186941F,
			751.4192606F, 774.9011125F, 799.89792260F, 826.5611867F, 855.0632966F,
			885.6012714F
		};
	}
	
	private static float[] doCreateGoldAmplitudesEta() {
		return new float[] {
			1.795000F, 1.812000F, 1.822625F, 1.830000F, 1.837125F, 1.840000F, 1.834250F, 1.824000F,
			1.812000F, 1.798000F, 1.782000F, 1.766000F, 1.752500F, 1.740000F, 1.727625F, 1.716000F,
			1.705875F, 1.696000F, 1.684750F, 1.674000F, 1.666000F, 1.658000F, 1.647250F, 1.636000F,
			1.628000F, 1.616000F, 1.596250F, 1.562000F, 1.502125F, 1.426000F, 1.345875F, 1.242000F,
			1.086750F, 0.916000F, 0.754500F, 0.608000F, 0.491750F, 0.402000F, 0.345500F, 0.306000F,
			0.267625F, 0.236000F, 0.212375F, 0.194000F, 0.177750F, 0.166000F, 0.161000F, 0.160000F,
			0.160875F, 0.164000F, 0.169500F, 0.176000F, 0.181375F, 0.188000F, 0.198125F, 0.210000F
		};
	}
	
	private static float[] doCreateGoldAmplitudesK() {
		return new float[] {
			1.920375F, 1.920000F, 1.918875F, 1.916000F, 1.911375F, 1.904000F, 1.891375F, 1.878000F,
			1.868250F, 1.860000F, 1.851750F, 1.846000F, 1.845250F, 1.848000F, 1.852375F, 1.862000F,
			1.883000F, 1.906000F, 1.922500F, 1.936000F, 1.947750F, 1.956000F, 1.959375F, 1.958000F,
			1.951375F, 1.940000F, 1.924500F, 1.904000F, 1.875875F, 1.846000F, 1.814625F, 1.796000F,
			1.797375F, 1.840000F, 1.956500F, 2.120000F, 2.326250F, 2.540000F, 2.730625F, 2.880000F,
			2.940625F, 2.970000F, 3.015000F, 3.060000F, 3.070000F, 3.150000F, 3.445812F, 3.800000F,
			4.087687F, 4.357000F, 4.610188F, 4.860000F, 5.125813F, 5.390000F, 5.631250F, 5.880000F
		};
	}
	
	private static float[] doCreateGoldWavelengths() {
		return new float[] {
			298.757050F, 302.400421F, 306.133759F, 309.960449F, 313.884003F,
			317.908142F, 322.036835F, 326.274139F, 330.624481F, 335.092377F,
			339.682678F, 344.400482F, 349.251221F, 354.240509F, 359.374420F,
			364.659332F, 370.102020F, 375.709625F, 381.489777F, 387.450562F,
			393.600555F, 399.948975F, 406.505493F, 413.280579F, 420.285339F,
			427.531647F, 435.032196F, 442.800629F, 450.851562F, 459.200653F,
			467.864838F, 476.862213F, 486.212463F, 495.936707F, 506.057861F,
			516.600769F, 527.592224F, 539.061646F, 551.040771F, 563.564453F,
			576.670593F, 590.400818F, 604.800842F, 619.920898F, 635.816284F,
			652.548279F, 670.184753F, 688.800964F, 708.481018F, 729.318665F,
			751.419250F, 774.901123F, 799.897949F, 826.561157F, 855.063293F,
			885.601257F
		};
	}
}