package org.dayflower.scene.background;

import static org.dayflower.util.Floats.PI_MULTIPLIED_BY_2_RECIPROCAL;
import static org.dayflower.util.Floats.abs;
import static org.dayflower.util.Floats.asinpi;
import static org.dayflower.util.Floats.atan2;
import static org.dayflower.util.Floats.ceil;
import static org.dayflower.util.Floats.cos;
import static org.dayflower.util.Floats.floor;
import static org.dayflower.util.Floats.remainder;
import static org.dayflower.util.Floats.sin;
import static org.dayflower.util.Ints.modulo;
import static org.dayflower.util.Ints.toInt;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import javax.imageio.ImageIO;

import org.dayflower.geometry.AngleF;
import org.dayflower.geometry.Point2F;
import org.dayflower.geometry.Ray3F;
import org.dayflower.geometry.Vector2F;
import org.dayflower.geometry.Vector3F;
import org.dayflower.image.Color3F;
import org.dayflower.image.Image;
import org.dayflower.scene.Background;
import org.dayflower.scene.BackgroundSample;
import org.dayflower.scene.Intersection;
import org.dayflower.util.BufferedImages;
import org.dayflower.util.ParameterArguments;

public class ImageBackground implements Background {
	private final AngleF angle;
	private final Vector2F scale;
	private final int resolution;
	private final int resolutionX;
	private final int resolutionY;
	private final int[] image;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code ImageBackground} instance.
	 * <p>
	 * If {@code image} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new ImageBackground(image, AngleF.degrees(0.0F));
	 * }
	 * </pre>
	 * 
	 * @param image an {@link Image} instance
	 * @throws NullPointerException thrown if, and only if, {@code image} is {@code null}
	 */
	public ImageBackground(final Image image) {
		this(image, AngleF.degrees(0.0F));
	}
	
	/**
	 * Constructs a new {@code ImageBackground} instance.
	 * <p>
	 * If either {@code image} or {@code angle} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new ImageBackground(image, angle, new Vector2F(1.0F, 1.0F));
	 * }
	 * </pre>
	 * 
	 * @param image an {@link Image} instance
	 * @param angle the {@link AngleF} instance to use
	 * @throws NullPointerException thrown if, and only if, either {@code image} or {@code angle} are {@code null}
	 */
	public ImageBackground(final Image image, final AngleF angle) {
		this(image, angle, new Vector2F(1.0F, 1.0F));
	}
	
	/**
	 * Constructs a new {@code ImageBackground} instance.
	 * <p>
	 * If either {@code image}, {@code angle} or {@code scale} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new ImageBackground(image.getResolutionX(), image.getResolutionY(), image.toIntArrayPackedForm(), angle, scale);
	 * }
	 * </pre>
	 * 
	 * @param image an {@link Image} instance
	 * @param angle the {@link AngleF} instance to use
	 * @param scale the {@link Vector2F} instance to use as the scale factor
	 * @throws NullPointerException thrown if, and only if, either {@code image}, {@code angle} or {@code scale} are {@code null}
	 */
	public ImageBackground(final Image image, final AngleF angle, final Vector2F scale) {
		this(image.getResolutionX(), image.getResolutionY(), image.toIntArrayPackedForm(), angle, scale);
	}
	
	/**
	 * Constructs a new {@code ImageBackground} instance.
	 * <p>
	 * If {@code image} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}, or {@code resolutionX * resolutionY != image.length}, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new ImageBackground(resolutionX, resolutionY, image, AngleF.degrees(0.0F));
	 * }
	 * </pre>
	 * 
	 * @param resolutionX the resolution of the X-axis
	 * @param resolutionY the resolution of the Y-axis
	 * @param image the image to clone and use
	 * @throws IllegalArgumentException thrown if, and only if, either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}, or {@code resolutionX * resolutionY != image.length}
	 * @throws NullPointerException thrown if, and only if, {@code image} is {@code null}
	 */
	public ImageBackground(final int resolutionX, final int resolutionY, final int[] image) {
		this(resolutionX, resolutionY, image, AngleF.degrees(0.0F));
	}
	
	/**
	 * Constructs a new {@code ImageBackground} instance.
	 * <p>
	 * If either {@code image} or {@code angle} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}, or {@code resolutionX * resolutionY != image.length}, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new ImageBackground(resolutionX, resolutionY, image, angle, new Vector2F(1.0F, 1.0F));
	 * }
	 * </pre>
	 * 
	 * @param resolutionX the resolution of the X-axis
	 * @param resolutionY the resolution of the Y-axis
	 * @param image the image to clone and use
	 * @param angle the {@link AngleF} instance to use
	 * @throws IllegalArgumentException thrown if, and only if, either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}, or {@code resolutionX * resolutionY != image.length}
	 * @throws NullPointerException thrown if, and only if, either {@code image} or {@code angle} are {@code null}
	 */
	public ImageBackground(final int resolutionX, final int resolutionY, final int[] image, final AngleF angle) {
		this(resolutionX, resolutionY, image, angle, new Vector2F(1.0F, 1.0F));
	}
	
	/**
	 * Constructs a new {@code ImageBackground} instance.
	 * <p>
	 * If either {@code image}, {@code angle} or {@code scale} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}, or {@code resolutionX * resolutionY != image.length}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param resolutionX the resolution of the X-axis
	 * @param resolutionY the resolution of the Y-axis
	 * @param image the image to clone and use
	 * @param angle the {@link AngleF} instance to use
	 * @param scale the {@link Vector2F} instance to use as the scale factor
	 * @throws IllegalArgumentException thrown if, and only if, either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}, or {@code resolutionX * resolutionY != image.length}
	 * @throws NullPointerException thrown if, and only if, either {@code image}, {@code angle} or {@code scale} are {@code null}
	 */
	public ImageBackground(final int resolutionX, final int resolutionY, final int[] image, final AngleF angle, final Vector2F scale) {
		this.resolutionX = ParameterArguments.requireRange(resolutionX, 0, Integer.MAX_VALUE, "resolutionX");
		this.resolutionY = ParameterArguments.requireRange(resolutionY, 0, Integer.MAX_VALUE, "resolutionY");
		this.resolution = ParameterArguments.requireRange(resolutionX * resolutionY, 0, Integer.MAX_VALUE, "resolutionX * resolutionY");
		this.image = ParameterArguments.requireExactArrayLength(Objects.requireNonNull(image, "image == null"), resolutionX * resolutionY, "image").clone();
		this.angle = Objects.requireNonNull(angle, "angle == null");
		this.scale = Objects.requireNonNull(scale, "scale == null");
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
		final Vector3F direction = ray.getDirection();
		
		final Point2F textureCoordinates = new Point2F(0.5F + atan2(direction.getZ(), direction.getX()) * PI_MULTIPLIED_BY_2_RECIPROCAL, 0.5F - asinpi(direction.getY()));
		
		final float cosAngleRadians = cos(this.angle.getRadians());
		final float sinAngleRadians = sin(this.angle.getRadians());
		
		final float resolutionX = this.resolutionX;
		final float resolutionY = this.resolutionY;
		
		final float u = remainder((textureCoordinates.getU() * cosAngleRadians - textureCoordinates.getV() * sinAngleRadians) * this.scale.getU() * resolutionX, resolutionX);
		final float v = remainder((textureCoordinates.getV() * cosAngleRadians + textureCoordinates.getU() * sinAngleRadians) * this.scale.getV() * resolutionY, resolutionY);
		
		final float x = u >= 0.0F ? u : resolutionX - abs(u);
		final float y = v >= 0.0F ? v : resolutionY - abs(v);
		
		return doGetColorRGB(x, y);
	}
	
	/**
	 * Samples this {@code ImageBackground} instance from {@code intersection}.
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
		Objects.requireNonNull(intersection, "intersection == null");
		
		return new ArrayList<>();
	}
	
	/**
	 * Returns a {@code String} representation of this {@code ImageBackground} instance.
	 * 
	 * @return a {@code String} representation of this {@code ImageBackground} instance
	 */
	@Override
	public String toString() {
		return String.format("new ImageBackground(%d, %d, %s, %s, %s)", Integer.valueOf(this.resolutionX), Integer.valueOf(this.resolutionY), "new int[] {...}", this.angle, this.scale);
	}
	
	/**
	 * Compares {@code object} to this {@code ImageBackground} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code ImageBackground}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code ImageBackground} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code ImageBackground}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof ImageBackground)) {
			return false;
		} else if(!Objects.equals(this.angle, ImageBackground.class.cast(object).angle)) {
			return false;
		} else if(!Objects.equals(this.scale, ImageBackground.class.cast(object).scale)) {
			return false;
		} else if(this.resolution != ImageBackground.class.cast(object).resolution) {
			return false;
		} else if(this.resolutionX != ImageBackground.class.cast(object).resolutionX) {
			return false;
		} else if(this.resolutionY != ImageBackground.class.cast(object).resolutionY) {
			return false;
		} else if(!Arrays.equals(this.image, ImageBackground.class.cast(object).image)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns a hash code for this {@code ImageBackground} instance.
	 * 
	 * @return a hash code for this {@code ImageBackground} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.angle, this.scale, Integer.valueOf(this.resolution), Integer.valueOf(this.resolutionX), Integer.valueOf(this.resolutionY), Integer.valueOf(Arrays.hashCode(this.image)));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Loads an {@code ImageBackground} from the file represented by {@code file}.
	 * <p>
	 * Returns a new {@code ImageBackground} instance.
	 * <p>
	 * If {@code file} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * ImageBackground.load(file, AngleF.degrees(0.0F));
	 * }
	 * </pre>
	 * 
	 * @param file a {@code File} that represents the file to load from
	 * @return a new {@code ImageBackground} instance
	 * @throws NullPointerException thrown if, and only if, {@code file} is {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
	public static ImageBackground load(final File file) {
		return load(file, AngleF.degrees(0.0F));
	}
	
	/**
	 * Loads an {@code ImageBackground} from the file represented by {@code file}.
	 * <p>
	 * Returns a new {@code ImageBackground} instance.
	 * <p>
	 * If either {@code file} or {@code angle} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * ImageBackground.load(file, angle, new Vector2F(1.0F, 1.0F));
	 * }
	 * </pre>
	 * 
	 * @param file a {@code File} that represents the file to load from
	 * @param angle the {@link AngleF} instance to use
	 * @return a new {@code ImageBackground} instance
	 * @throws NullPointerException thrown if, and only if, either {@code file} or {@code angle} are {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
	public static ImageBackground load(final File file, final AngleF angle) {
		return load(file, angle, new Vector2F(1.0F, 1.0F));
	}
	
	/**
	 * Loads an {@code ImageBackground} from the file represented by {@code file}.
	 * <p>
	 * Returns a new {@code ImageBackground} instance.
	 * <p>
	 * If either {@code file}, {@code angle} or {@code scale} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * 
	 * @param file a {@code File} that represents the file to load from
	 * @param angle the {@link AngleF} instance to use
	 * @param scale the {@link Vector2F} instance to use as the scale factor
	 * @return a new {@code ImageBackground} instance
	 * @throws NullPointerException thrown if, and only if, either {@code file}, {@code angle} or {@code scale} are {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
	public static ImageBackground load(final File file, final AngleF angle, final Vector2F scale) {
		try {
			final BufferedImage bufferedImage = BufferedImages.getCompatibleBufferedImage(ImageIO.read(Objects.requireNonNull(file, "file == null")));
			
			final int resolutionX = bufferedImage.getWidth();
			final int resolutionY = bufferedImage.getHeight();
			
			final int[] image = DataBufferInt.class.cast(bufferedImage.getRaster().getDataBuffer()).getData();
			
			return new ImageBackground(resolutionX, resolutionY, image, angle, scale);
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private Color3F doGetColorRGB(final float x, final float y) {
		final int minimumX = toInt(floor(x));
		final int maximumX = toInt(ceil(x));
		
		final int minimumY = toInt(floor(y));
		final int maximumY = toInt(ceil(y));
		
		if(minimumX == maximumX && minimumY == maximumY) {
			return doGetColorRGB(minimumX, minimumY);
		}
		
		final Color3F color00 = doGetColorRGB(minimumX, minimumY);
		final Color3F color01 = doGetColorRGB(maximumX, minimumY);
		final Color3F color10 = doGetColorRGB(minimumX, maximumY);
		final Color3F color11 = doGetColorRGB(maximumX, maximumY);
		
		final float xFactor = x - minimumX;
		final float yFactor = y - minimumY;
		
		final Color3F color = Color3F.blend(Color3F.blend(color00, color01, xFactor), Color3F.blend(color10, color11, xFactor), yFactor);
		
		return color;
	}
	
	private Color3F doGetColorRGB(final int x, final int y) {
		return Color3F.unpack(this.image[modulo(y, this.resolutionY) * this.resolutionX + modulo(x, this.resolutionX)]);
	}
}