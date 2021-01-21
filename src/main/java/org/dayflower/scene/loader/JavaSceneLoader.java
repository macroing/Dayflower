/**
 * Copyright 2014 - 2021 J&#246;rgen Lundgren
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import javax.tools.JavaCompiler;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;
import javax.tools.JavaCompiler.CompilationTask;

import org.dayflower.scene.Scene;
import org.dayflower.scene.SceneLoader;

/**
 * A {@code JavaSceneLoader} is a {@link SceneLoader} implementation that loads {@link Scene} instances by compiling Java source code and executing it.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class JavaSceneLoader implements SceneLoader {
	private static final AtomicInteger IDENTIFIER = new AtomicInteger();
	private static final String LINE_SEPARATOR = System.getProperty("line.separator");
	private static final String TMP_DIRECTORY = System.getProperty("java.io.tmpdir");
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final Map<String, Object> objects;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code JavaSceneLoader} instance.
	 */
	public JavaSceneLoader() {
		this.objects = new HashMap<>();
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
	 * javaSceneLoader.load(file, new Scene());
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
	 * 
	 * @param file a {@code File} instance that represents a file
	 * @param scene the {@code Scene} instance to load into
	 * @return the loaded {@code Scene} instance, {@code scene}
	 * @throws NullPointerException thrown if, and only if, either {@code file} or {@code scene} are {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
	@Override
	public Scene load(final File file, final Scene scene) {
		Objects.requireNonNull(file, "file == null");
		Objects.requireNonNull(scene, "scene == null");
		
		doLoad(doLoadObject(file), scene);
		
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
	 * javaSceneLoader.load(pathname, new Scene());
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
	 * 
	 * @param pathname a {@code String} instance that represents a pathname to a file
	 * @param scene the {@code Scene} instance to load into
	 * @return the loaded {@code Scene} instance, {@code scene}
	 * @throws NullPointerException thrown if, and only if, either {@code pathname} or {@code scene} are {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
	@Override
	public Scene load(final String pathname, final Scene scene) {
		return load(new File(Objects.requireNonNull(pathname, "pathname == null")), scene);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private Object doLoadObject(final File file) {
		final String sourceCode = doReadFrom(file);
		
		if(this.objects.containsKey(sourceCode)) {
			return this.objects.get(sourceCode);
		}
		
		final Object object = doCompileSourceCode(sourceCode);
		
		if(object != null) {
			this.objects.put(sourceCode, object);
			
			return object;
		}
		
		return null;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static File doGetBinaryDirectory() {
		final
		File file = new File(TMP_DIRECTORY, "dayflower/bin");
		file.mkdirs();
		
		return file;
	}
	
	private static File doGetSourceDirectory() {
		final
		File file = new File(TMP_DIRECTORY, "dayflower/src");
		file.mkdirs();
		
		return file;
	}
	
	private static File doGetSourceFile(final String directory, final String className) {
		final
		File file = new File(doGetSourceDirectory(), directory + "/" + className + ".java");
		file.getParentFile().mkdirs();
		
		return file;
	}
	
	private static List<File> doGetFiles(final URL[] uRLs) {
		final List<File> files = new ArrayList<>();
		
		for(final URL uRL : uRLs) {
			try {
				files.add(new File(uRL.toURI()));
			} catch(final URISyntaxException e) {
				files.add(new File(uRL.getPath()));
			}
		}
		
		return files;
	}
	
	private static Object doCompileSourceCode(final String sourceCode) {
		final String className = "JavaSceneLoaderProgram" + IDENTIFIER.incrementAndGet();
		final String packageName = "org.dayflower.scene.loader";
		final String directory = packageName.replace(".", "/");
		
		final File binaryDirectory = doGetBinaryDirectory();
		final File sourceDirectory = doGetSourceDirectory();
		final File sourceFile = doGetSourceFile(directory, className);
		
		doAddToClassPath(binaryDirectory);
		doGenerateSourceCode(className, sourceCode, sourceFile);
		
		try {
			final JavaCompiler javaCompiler = ToolProvider.getSystemJavaCompiler();
			
			final List<File> files = doGetFiles(URLClassLoader.class.cast(ClassLoader.getSystemClassLoader()).getURLs());
			
			try(final StandardJavaFileManager standardJavaFileManager = javaCompiler.getStandardFileManager(null, null, null)) {
				standardJavaFileManager.setLocation(StandardLocation.CLASS_OUTPUT, Arrays.asList(binaryDirectory));
				standardJavaFileManager.setLocation(StandardLocation.CLASS_PATH, files);
				standardJavaFileManager.setLocation(StandardLocation.SOURCE_PATH, Arrays.asList(sourceDirectory));
				
				final CompilationTask compilationTask = javaCompiler.getTask(null, standardJavaFileManager, null, null, null, standardJavaFileManager.getJavaFileObjectsFromFiles(Arrays.asList(sourceFile)));
				
				final boolean isCompiled = compilationTask.call().booleanValue();
				
				if(!isCompiled) {
					return null;
				}
			}
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
		
		try {
			return Class.forName("org.dayflower.scene.loader." + className).getConstructor().newInstance();
		} catch(final Exception e) {
			return null;
		}
	}
	
	private static String doFormatSourceCode(final String sourceCode) {
		final String[] lines = sourceCode.trim().split("\r\n|\n|\r");
		
		final StringBuilder stringBuilder = new StringBuilder();
		
		for(int i = 0; i < lines.length; i++) {
			final String line = lines[i];
			
			stringBuilder.append(i > 0 ? "			" : "");
			stringBuilder.append(line);
			stringBuilder.append(i + 1 < lines.length ? LINE_SEPARATOR : "");
		}
		
		return stringBuilder.toString();
	}
	
	private static String doGenerateSourceCode(final String className, final String sourceCode) {
		final StringBuilder stringBuilder = new StringBuilder();
		
		doAppendLinef(stringBuilder, "package org.dayflower.scene.loader;");
		doAppendLinef(stringBuilder, "");
		doAppendLinef(stringBuilder, "import java.io.*;");
		doAppendLinef(stringBuilder, "import java.util.*;");
		doAppendLinef(stringBuilder, "");
		doAppendLinef(stringBuilder, "import org.dayflower.color.*;");
		doAppendLinef(stringBuilder, "import org.dayflower.filter.*;");
		doAppendLinef(stringBuilder, "import org.dayflower.geometry.*;");
		doAppendLinef(stringBuilder, "import org.dayflower.geometry.boundingvolume.*;");
		doAppendLinef(stringBuilder, "import org.dayflower.geometry.rasterizer.*;");
		doAppendLinef(stringBuilder, "import org.dayflower.geometry.shape.*;");
		doAppendLinef(stringBuilder, "import org.dayflower.geometry.shape.ConstructiveSolidGeometry3F.*;");
		doAppendLinef(stringBuilder, "import org.dayflower.geometry.shape.Curve3F.*;");
		doAppendLinef(stringBuilder, "import org.dayflower.geometry.shape.Triangle3F.*;");
		doAppendLinef(stringBuilder, "import org.dayflower.image.*;");
		doAppendLinef(stringBuilder, "import org.dayflower.scene.*;");
		doAppendLinef(stringBuilder, "import org.dayflower.scene.bssrdf.*;");
		doAppendLinef(stringBuilder, "import org.dayflower.scene.bxdf.pbrt.*;");
		doAppendLinef(stringBuilder, "import org.dayflower.scene.bxdf.rayito.*;");
		doAppendLinef(stringBuilder, "import org.dayflower.scene.fresnel.*;");
		doAppendLinef(stringBuilder, "import org.dayflower.scene.light.*;");
		doAppendLinef(stringBuilder, "import org.dayflower.scene.material.pbrt.*;");
		doAppendLinef(stringBuilder, "import org.dayflower.scene.material.rayito.*;");
		doAppendLinef(stringBuilder, "import org.dayflower.scene.material.smallpt.*;");
		doAppendLinef(stringBuilder, "import org.dayflower.scene.microfacet.*;");
		doAppendLinef(stringBuilder, "import org.dayflower.scene.modifier.*;");
		doAppendLinef(stringBuilder, "import org.dayflower.scene.texture.*;");
		doAppendLinef(stringBuilder, "");
		doAppendLinef(stringBuilder, "public final class %s {", className);
		doAppendLinef(stringBuilder, "	public %s() {", className);
		doAppendLinef(stringBuilder, "		");
		doAppendLinef(stringBuilder, "	}");
		doAppendLinef(stringBuilder, "	");
		doAppendLinef(stringBuilder, "	public Scene load(final Scene scene) {");
		doAppendLinef(stringBuilder, "		try {");
		doAppendLinef(stringBuilder, "			%s", doFormatSourceCode(sourceCode));
		doAppendLinef(stringBuilder, "		} catch(final Exception e) {");
		doAppendLinef(stringBuilder, "			");
		doAppendLinef(stringBuilder, "		} finally {");
		doAppendLinef(stringBuilder, "			return scene;");
		doAppendLinef(stringBuilder, "		}");
		doAppendLinef(stringBuilder, "	}");
		doAppendLinef(stringBuilder, "}");
		
		return stringBuilder.toString();
	}
	
	private static String doReadFrom(final File file) {
		try(final BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
			final StringBuilder stringBuilder = new StringBuilder();
			
			for(String line = bufferedReader.readLine(); line != null; line = bufferedReader.readLine()) {
				stringBuilder.append(line);
				stringBuilder.append(LINE_SEPARATOR);
			}
			
			return stringBuilder.toString();
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	private static void doAddToClassPath(final File file) {
		try {
			final Method method = URLClassLoader.class.getDeclaredMethod("addURL", new Class<?>[]{URL.class});
			
			final boolean isAccessible = method.isAccessible();
			
			method.setAccessible(true);
			method.invoke(URLClassLoader.class.cast(ClassLoader.getSystemClassLoader()), new Object[]{file.toURI().toURL()});
			method.setAccessible(isAccessible);
		} catch(final IllegalAccessException | InvocationTargetException | MalformedURLException | NoSuchMethodException e) {
			throw new UnsupportedOperationException(e);
		}
	}
	
	private static void doAppendLinef(final StringBuilder stringBuilder, final String lineFormat, final Object... lineArguments) {
		stringBuilder.append(String.format(lineFormat, lineArguments) + LINE_SEPARATOR);
	}
	
	private static void doGenerateSourceCode(final String className, final String sourceCode, final File sourceFile) {
		try(final FileWriter fileWriter = new FileWriter(sourceFile)) {
			fileWriter.write(doGenerateSourceCode(className, sourceCode));
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	private static void doLoad(final Object object, final Scene scene) {
		if(object != null) {
			try {
				final Class<?> clazz = object.getClass();
				
				final Method method = clazz.getMethod("load", Scene.class);
				
				method.invoke(object, scene);
			} catch(final IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
//				Do nothing for now!
			}
		}
	}
}