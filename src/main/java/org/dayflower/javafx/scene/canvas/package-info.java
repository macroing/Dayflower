/**
 * Provides the JavaFX Scene Canvas API.
 * <p>
 * The JavaFX Scene Canvas API is an extension to {@code javafx.scene.canvas}.
 * <h3>Overview</h3>
 * <p>
 * The following list contains information about the classes in this API.
 * <ul>
 * <li>{@link org.dayflower.javafx.scene.canvas.ConcurrentImageCanvas ConcurrentImageCanvas} is a {@code Canvas} that performs rendering to an {@code Image} using an {@code ExecutorService} and updates the {@code Canvas} on the {@code FX Application Thread}.</li>
 * <li>{@link org.dayflower.javafx.scene.canvas.ConcurrentImageCanvasPane ConcurrentImageCanvasPane} is a {@code Pane} that can resize a {@code ConcurrentImageCanvas} instance.</li>
 * </ul>
 * <h3>Dependencies</h3>
 * <p>
 * The following list shows all dependencies for this API.
 * <ul>
 * <li>The Change API</li>
 * <li>The Color API</li>
 * <li>The Filter API</li>
 * <li>The Geometry API</li>
 * <li>The Geometry Rasterizer API</li>
 * <li>The Geometry Shape API</li>
 * <li>The JavaFX Concurrent API</li>
 * <li>The Node API</li>
 * <li>The Noise API</li>
 * <li>The Utility API</li>
 * </ul>
 */
package org.dayflower.javafx.scene.canvas;