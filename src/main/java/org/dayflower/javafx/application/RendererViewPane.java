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
package org.dayflower.javafx.application;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

import org.dayflower.geometry.Point2I;
import org.dayflower.geometry.Point3F;
import org.dayflower.geometry.Quaternion4F;
import org.dayflower.geometry.Shape3F;
import org.dayflower.geometry.Vector3F;
import org.dayflower.geometry.shape.Rectangle2I;
import org.dayflower.image.BoxFilter2F;
import org.dayflower.image.Color3F;
import org.dayflower.image.Color4F;
import org.dayflower.image.ImageF;
import org.dayflower.image.PixelImageF;
import org.dayflower.javafx.canvas.ConcurrentImageCanvas;
import org.dayflower.javafx.scene.control.ObjectTreeView;
import org.dayflower.javafx.scene.image.WritableImageCache;
import org.dayflower.javafx.scene.layout.Regions;
import org.dayflower.renderer.CombinedProgressiveImageOrderRenderer;
import org.dayflower.renderer.RenderingAlgorithm;
import org.dayflower.renderer.cpu.CPURenderer;
import org.dayflower.renderer.gpu.AbstractGPURenderer;
import org.dayflower.renderer.observer.NoOpRendererObserver;
import org.dayflower.sampler.RandomSampler;
import org.dayflower.scene.Camera;
import org.dayflower.scene.Material;
import org.dayflower.scene.Primitive;
import org.dayflower.scene.Scene;
import org.dayflower.scene.Transform;
import org.dayflower.scene.material.pbrt.PBRTMaterial;
import org.dayflower.scene.material.rayito.RayitoMaterial;
import org.dayflower.scene.material.smallpt.SmallPTMaterial;
import org.dayflower.scene.preview.Previews;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

final class RendererViewPane extends BorderPane {
	private static final WritableImageCache<Material> WRITABLE_IMAGE_CACHE_MATERIAL = new WritableImageCache<>(RendererViewPane::doCreateWritableImageMaterial);
	private static final WritableImageCache<Shape3F> WRITABLE_IMAGE_CACHE_SHAPE = new WritableImageCache<>(RendererViewPane::doCreateWritableImageShape);
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final AtomicBoolean isCameraUpdateRequired;
	private final AtomicReference<File> file;
	private final CombinedProgressiveImageOrderRenderer combinedProgressiveImageOrderRenderer;
	private final ConcurrentImageCanvas concurrentImageCanvas;
	private final ExecutorService executorService;
	private final HBox hBox;
	private final Label labelRenderPass;
	private final Label labelRenderTime;
	private final Label labelRenderTimePerPass;
	private final ObjectTreeView<String, Object> objectTreeView;
	private final ProgressBar progressBar;
	private final VBox vBoxL;
	private final VBox vBoxR;
	private final VBox vBoxRenderer;
	private final VBox vBoxScene;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code RendererViewPane} instance.
	 * <p>
	 * If either {@code combinedProgressiveImageOrderRenderer} or {@code executorService} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param combinedProgressiveImageOrderRenderer the {@link CombinedProgressiveImageOrderRenderer} instance associated with this {@code RendererViewPane} instance
	 * @param executorService the {@code ExecutorService} associated with this {@code RendererViewPane} instance
	 * @throws NullPointerException thrown if, and only if, either {@code combinedProgressiveImageOrderRenderer} or {@code executorService} are {@code null}
	 */
	public RendererViewPane(final CombinedProgressiveImageOrderRenderer combinedProgressiveImageOrderRenderer, final ExecutorService executorService) {
		this.isCameraUpdateRequired = new AtomicBoolean();
		this.file = new AtomicReference<>();
		this.combinedProgressiveImageOrderRenderer = Objects.requireNonNull(combinedProgressiveImageOrderRenderer, "combinedProgressiveImageOrderRenderer == null");
		this.concurrentImageCanvas = new ConcurrentImageCanvas(executorService, combinedProgressiveImageOrderRenderer.getImage(), this::doRender, new ObserverImpl(combinedProgressiveImageOrderRenderer));
		this.executorService = Objects.requireNonNull(executorService, "executorService == null");
		this.hBox = new HBox();
		this.labelRenderPass = new Label();
		this.labelRenderTime = new Label();
		this.labelRenderTimePerPass = new Label();
		this.objectTreeView = doCreateObjectTreeView(combinedProgressiveImageOrderRenderer.getScene());
		this.progressBar = new ProgressBar();
		this.vBoxL = new VBox();
		this.vBoxR = new VBox();
		this.vBoxRenderer = CenteredVBoxes.createCenteredVBoxForCombinedProgressiveImageOrderRenderer(combinedProgressiveImageOrderRenderer);
		this.vBoxScene = CenteredVBoxes.createCenteredVBoxForScene(combinedProgressiveImageOrderRenderer);
		
		doConfigure();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the {@link CombinedProgressiveImageOrderRenderer} instance associated with this {@code RendererViewPane} instance.
	 * 
	 * @return the {@code CombinedProgressiveImageOrderRenderer} instance associated with this {@code RendererViewPane} instance
	 */
	public CombinedProgressiveImageOrderRenderer getCombinedProgressiveImageOrderRenderer() {
		return this.combinedProgressiveImageOrderRenderer;
	}
	
	/**
	 * Returns the {@code ExecutorService} instance associated with this {@code RendererViewPane} instance.
	 * 
	 * @return the {@code ExecutorService} instance associated with this {@code RendererViewPane} instance
	 */
	public ExecutorService getExecutorService() {
		return this.executorService;
	}
	
	/**
	 * Returns the {@code ObjectTreeView} instance associated with this {@code RendererViewPane} instance.
	 * 
	 * @return the {@code ObjectTreeView} instance associated with this {@code RendererViewPane} instance
	 */
	public ObjectTreeView<String, Object> getObjectTreeView() {
		return this.objectTreeView;
	}
	
	/**
	 * Returns the optional {@code File} for saving.
	 * 
	 * @return the optional {@code File} for saving
	 */
	public Optional<File> getFile() {
		return Optional.ofNullable(this.file.get());
	}
	
	/**
	 * This method is called when it's time to render.
	 */
	public void render() {
		this.concurrentImageCanvas.render();
	}
	
	/**
	 * This method is called when it's time to save the {@link ImageF}.
	 */
	public void save() {
		final Optional<File> optionalFile = getFile();
		
		if(optionalFile.isPresent()) {
			final
			ImageF imageF = this.combinedProgressiveImageOrderRenderer.getImage();
			imageF.save(optionalFile.get());
		}
	}
	
	/**
	 * Sets the camera update required state to {@code isCameraUpdateRequired}.
	 * 
	 * @param isCameraUpdateRequired {@code true} if, and only if, camera update is required, {@code false} otherwise
	 */
	public void setCameraUpdateRequired(final boolean isCameraUpdateRequired) {
		this.isCameraUpdateRequired.set(isCameraUpdateRequired);
	}
	
	/**
	 * Sets the {@code File} for saving to {@code file}.
	 * <p>
	 * If {@code file} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param file the {@code File} for saving
	 * @throws NullPointerException} thrown if, and only if, {@code file} is {@code null}
	 */
	public void setFile(final File file) {
		this.file.set(Objects.requireNonNull(file, "file == null"));
	}
	
	/**
	 * This method is called when it's time to update.
	 */
	public void update() {
		final Camera camera = this.combinedProgressiveImageOrderRenderer.getScene().getCamera();
		
		if(this.concurrentImageCanvas.isKeyPressed(KeyCode.A)) {
			camera.moveLeft(0.3F);
		}
		
		if(this.concurrentImageCanvas.isKeyPressed(KeyCode.D)) {
			camera.moveRight(0.3F);
		}
		
		if(this.concurrentImageCanvas.isKeyPressed(KeyCode.S)) {
			camera.moveBackward(0.3F);
		}
		
		if(this.concurrentImageCanvas.isKeyPressed(KeyCode.W)) {
			camera.moveForward(0.3F);
		}
		
		if(this.isCameraUpdateRequired.compareAndSet(true, false)) {
			if(this.combinedProgressiveImageOrderRenderer instanceof AbstractGPURenderer) {
				AbstractGPURenderer.class.cast(this.combinedProgressiveImageOrderRenderer).updateCamera();
			}
			
			this.combinedProgressiveImageOrderRenderer.renderShutdown();
			this.combinedProgressiveImageOrderRenderer.clear();
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private Function<Object, ContextMenu> doCreateMapperUToContextMenu() {
		return object -> {
			if(object instanceof Primitive) {
				final
				MenuItem menuItem = new MenuItem();
				menuItem.setOnAction(actionEvent -> doOnActionDelete(Primitive.class.cast(object)));
				menuItem.setText("Delete");
				
				final
				ContextMenu contextMenu = new ContextMenu();
				contextMenu.getItems().add(menuItem);
				
				return contextMenu;
			}
			
			return null;
		};
	}
	
	private ObjectTreeView<String, Object> doCreateObjectTreeView(final Scene scene) {
		return new ObjectTreeView<>(doCreateMapperUToContextMenu(), doCreateMapperUToListU(), doCreateMapperUToGraphic(), doCreateMapperUToT(), scene);
	}
	
	@SuppressWarnings("unused")
	private boolean doRender(final ImageF image) {
		return this.combinedProgressiveImageOrderRenderer.render();
	}
	
	private void doConfigure() {
//		Configure the HBox:
		this.hBox.getChildren().add(this.labelRenderPass);
		this.hBox.getChildren().add(this.labelRenderTime);
		this.hBox.getChildren().add(this.labelRenderTimePerPass);
		this.hBox.getChildren().add(Regions.createRegionHBoxHorizontalGrowAlways());
		this.hBox.getChildren().add(this.progressBar);
		this.hBox.setBorder(new Border(new BorderStroke(Color.rgb(181, 181, 181), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1.0D, 0.0D, 0.0D, 0.0D))));
		this.hBox.setPadding(new Insets(10.0D, 10.0D, 10.0D, 10.0D));
		this.hBox.setSpacing(20.0D);
		
//		Configure the Label for Render Pass:
		this.labelRenderPass.setText("Render Pass: 0");
		
//		Configure the Label for Render Time:
		this.labelRenderTime.setText("Render Time: 00:00:00");
		
//		Configure the Label for Render Time Per Pass:
		this.labelRenderTimePerPass.setText("Render Time Per Pass: 0");
		
//		Configure the ObjectTreeView:
		for(final Primitive primitive : this.combinedProgressiveImageOrderRenderer.getScene().getPrimitives()) {
			this.objectTreeView.add(primitive);
		}
		
//		Configure the ProgressBar:
		this.progressBar.setProgress(0.0D);
		
//		Configure the CombinedProgressiveImageOrderRenderer:
		this.combinedProgressiveImageOrderRenderer.setRendererObserver(new RendererObserverImpl(this.labelRenderPass, this.labelRenderTime, this.labelRenderTimePerPass, this.progressBar));
		
//		Configure the VBox for L:
		this.vBoxL.getChildren().add(this.vBoxRenderer);
		this.vBoxL.getChildren().add(this.vBoxScene);
		this.vBoxL.setBorder(new Border(new BorderStroke(Color.rgb(181, 181, 181), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(0.0D, 1.0D, 0.0D, 0.0D))));
		this.vBoxL.setFillWidth(true);
		this.vBoxL.setPadding(new Insets(10.0D, 10.0D, 10.0D, 10.0D));
		this.vBoxL.setSpacing(20.0D);
		
//		Configure the VBox for R:
		this.vBoxR.getChildren().add(this.objectTreeView);
		this.vBoxR.setBorder(new Border(new BorderStroke(Color.rgb(181, 181, 181), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(0.0D, 0.0D, 0.0D, 1.0D))));
		this.vBoxR.setFillWidth(true);
		this.vBoxR.setPadding(new Insets(10.0D, 10.0D, 10.0D, 10.0D));
		this.vBoxR.setSpacing(20.0D);
		
		VBox.setVgrow(this.objectTreeView, Priority.ALWAYS);
		
//		Configure the RendererViewPane:
		setBottom(this.hBox);
		setCenter(this.concurrentImageCanvas);
		setLeft(this.vBoxL);
		setRight(this.vBoxR);
	}
	
	private void doOnActionDelete(final Primitive primitive) {
		this.combinedProgressiveImageOrderRenderer.getScene().removePrimitive(primitive);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static Function<Object, List<Object>> doCreateMapperUToListU() {
		return object -> {
			final List<Object> list = new ArrayList<>();
			
			if(object instanceof Primitive) {
				final Primitive primitive = Primitive.class.cast(object);
				
				list.add(primitive.getMaterial());
				list.add(primitive.getShape());
//				list.add(primitive.getTransform());
			} else if(object instanceof Transform) {
				final Transform transform = Transform.class.cast(object);
				
				list.add(transform.getPosition());
				list.add(transform.getRotation());
				list.add(transform.getScale());
			}
			
			return list;
		};
	}
	
	private static Function<Object, Node> doCreateMapperUToGraphic() {
		return object -> {
			if(object instanceof Material) {
				return new ImageView(WRITABLE_IMAGE_CACHE_MATERIAL.get(Material.class.cast(object)));
			} else if(object instanceof Point3F) {
				return null;
			} else if(object instanceof Primitive) {
				return null;
			} else if(object instanceof Quaternion4F) {
				return null;
			} else if(object instanceof Scene) {
				return null;
			} else if(object instanceof Shape3F) {
				return new ImageView(WRITABLE_IMAGE_CACHE_SHAPE.get(Shape3F.class.cast(object)));
			} else if(object instanceof Transform) {
				return null;
			} else if(object instanceof Vector3F) {
				return null;
			} else {
				return null;
			}
		};
	}
	
	private static Function<Object, String> doCreateMapperUToT() {
		return object -> {
			if(object instanceof Material) {
				return Material.class.cast(object).getName();
			} else if(object instanceof Point3F) {
				final Point3F position = Point3F.class.cast(object);
				
				return String.format("[%+.10f, %+.10f, %+.10f]", Float.valueOf(position.getX()), Float.valueOf(position.getY()), Float.valueOf(position.getZ()));
			} else if(object instanceof Primitive) {
				return "Primitive";
			} else if(object instanceof Quaternion4F) {
				final Quaternion4F rotation = Quaternion4F.class.cast(object);
				
				return String.format("[%+.10f, %+.10f, %+.10f, %+.10f]", Float.valueOf(rotation.getX()), Float.valueOf(rotation.getY()), Float.valueOf(rotation.getZ()), Float.valueOf(rotation.getW()));
			} else if(object instanceof Scene) {
				return Scene.class.cast(object).getName();
			} else if(object instanceof Shape3F) {
				return Shape3F.class.cast(object).getName();
			} else if(object instanceof Transform) {
				return "Transform";
			} else if(object instanceof Vector3F) {
				final Vector3F scale = Vector3F.class.cast(object);
				
				return String.format("[%+.10f, %+.10f, %+.10f]", Float.valueOf(scale.getX()), Float.valueOf(scale.getY()), Float.valueOf(scale.getZ()));
			} else {
				return "";
			}
		};
	}
	
	private static RenderingAlgorithm doCreateRenderingAlgorithm(final Material material) {
		if(material instanceof PBRTMaterial) {
			return RenderingAlgorithm.PATH_TRACING;
		} else if(material instanceof RayitoMaterial) {
			return RenderingAlgorithm.PATH_TRACING;
		} else if(material instanceof SmallPTMaterial) {
			return RenderingAlgorithm.PATH_TRACING_SMALL_P_T_ITERATIVE;
		} else {
			return RenderingAlgorithm.PATH_TRACING;
		}
	}
	
	private static WritableImage doCreateWritableImageMaterial(final Material material) {
		final
		CombinedProgressiveImageOrderRenderer combinedProgressiveImageOrderRenderer = new CPURenderer(new NoOpRendererObserver());
		combinedProgressiveImageOrderRenderer.setImage(new PixelImageF(32, 32, Color4F.BLACK, new BoxFilter2F()));
		combinedProgressiveImageOrderRenderer.setPreviewMode(true);
		combinedProgressiveImageOrderRenderer.setRenderingAlgorithm(doCreateRenderingAlgorithm(material));
		combinedProgressiveImageOrderRenderer.setRenderPasses(10);
		combinedProgressiveImageOrderRenderer.setSampler(new RandomSampler());
		combinedProgressiveImageOrderRenderer.setScene(Previews.createMaterialPreviewScene(material));
		combinedProgressiveImageOrderRenderer.render();
		
		final
		PixelImageF pixelImageF = PixelImageF.class.cast(combinedProgressiveImageOrderRenderer.getImage());
		pixelImageF.drawRectangle(new Rectangle2I(new Point2I(0, 0), new Point2I(pixelImageF.getResolutionX() - 1, pixelImageF.getResolutionY() - 1)), new Color3F(181, 181, 181));
		
		return pixelImageF.toWritableImage();
	}
	
	@SuppressWarnings("unused")
	private static WritableImage doCreateWritableImageShape(final Shape3F shape) {
		final
		PixelImageF pixelImageF = new PixelImageF(32, 32, Color4F.WHITE);
		pixelImageF.drawRectangle(new Rectangle2I(new Point2I(0, 0), new Point2I(pixelImageF.getResolutionX() - 1, pixelImageF.getResolutionY() - 1)), new Color3F(181, 181, 181));
		
		return pixelImageF.toWritableImage();
	}
}