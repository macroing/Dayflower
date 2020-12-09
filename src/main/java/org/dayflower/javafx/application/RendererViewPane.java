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
package org.dayflower.javafx.application;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

import org.dayflower.geometry.Point2I;
import org.dayflower.geometry.Point3F;
import org.dayflower.geometry.Quaternion4F;
import org.dayflower.geometry.Shape3F;
import org.dayflower.geometry.Vector3F;
import org.dayflower.geometry.shape.Plane3F;
import org.dayflower.geometry.shape.Rectangle2I;
import org.dayflower.geometry.shape.RectangularCuboid3F;
import org.dayflower.geometry.shape.Sphere3F;
import org.dayflower.geometry.shape.Torus3F;
import org.dayflower.geometry.shape.Triangle3F;
import org.dayflower.image.BoxFilter;
import org.dayflower.image.Color3F;
import org.dayflower.image.Image;
import org.dayflower.javafx.canvas.ConcurrentImageCanvas;
import org.dayflower.javafx.scene.control.Labels;
import org.dayflower.javafx.scene.control.ObjectTreeView;
import org.dayflower.javafx.scene.image.WritableImageCache;
import org.dayflower.javafx.scene.layout.Regions;
import org.dayflower.renderer.Renderer;
import org.dayflower.renderer.RendererConfiguration;
import org.dayflower.renderer.RendererObserver;
import org.dayflower.renderer.RenderingAlgorithm;
import org.dayflower.renderer.cpu.CPURenderer;
import org.dayflower.renderer.observer.NoOpRendererObserver;
import org.dayflower.sampler.RandomSampler;
import org.dayflower.scene.Camera;
import org.dayflower.scene.Material;
import org.dayflower.scene.Primitive;
import org.dayflower.scene.Scene;
import org.dayflower.scene.Transform;
import org.dayflower.scene.material.pbrt.GlassPBRTMaterial;
import org.dayflower.scene.material.pbrt.HairPBRTMaterial;
import org.dayflower.scene.material.pbrt.MattePBRTMaterial;
import org.dayflower.scene.material.pbrt.MetalPBRTMaterial;
import org.dayflower.scene.material.pbrt.MirrorPBRTMaterial;
import org.dayflower.scene.material.pbrt.PBRTMaterial;
import org.dayflower.scene.material.pbrt.PlasticPBRTMaterial;
import org.dayflower.scene.material.pbrt.SubstratePBRTMaterial;
import org.dayflower.scene.material.pbrt.UberPBRTMaterial;
import org.dayflower.scene.material.rayito.GlassRayitoMaterial;
import org.dayflower.scene.material.rayito.MatteRayitoMaterial;
import org.dayflower.scene.material.rayito.MetalRayitoMaterial;
import org.dayflower.scene.material.rayito.MirrorRayitoMaterial;
import org.dayflower.scene.material.rayito.RayitoMaterial;
import org.dayflower.scene.preview.Previews;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Separator;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
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
	
	private final AtomicReference<File> file;
	private final ConcurrentImageCanvas concurrentImageCanvas;
	private final ExecutorService executorService;
	private final HBox hBox;
	private final Label labelRenderPass;
	private final Label labelRenderTime;
	private final Label labelRenderTimePerPass;
	private final ObjectTreeView<String, Object> objectTreeView;
	private final ProgressBar progressBar;
	private final Renderer renderer;
	private final RendererBox rendererBox;
	private final SceneBox sceneBox;
	private final VBox vBoxL;
	private final VBox vBoxR;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code RendererViewPane} instance.
	 * <p>
	 * If either {@code renderer} or {@code executorService} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param renderer the {@link Renderer} instance associated with this {@code RendererViewPane} instance
	 * @param executorService the {@code ExecutorService} associated with this {@code RendererViewPane} instance
	 * @throws NullPointerException thrown if, and only if, either {@code renderer} or {@code executorService} are {@code null}
	 */
	public RendererViewPane(final Renderer renderer, final ExecutorService executorService) {
		this.file = new AtomicReference<>();
		this.concurrentImageCanvas = new ConcurrentImageCanvas(executorService, renderer.getRendererConfiguration().getImage(), this::doRender);
		this.executorService = Objects.requireNonNull(executorService, "executorService == null");
		this.hBox = new HBox();
		this.labelRenderPass = new Label();
		this.labelRenderTime = new Label();
		this.labelRenderTimePerPass = new Label();
		this.objectTreeView = doCreateObjectTreeView(renderer.getRendererConfiguration().getScene());
		this.progressBar = new ProgressBar();
		this.renderer = renderer;
		this.rendererBox = new RendererBox(renderer);
		this.sceneBox = new SceneBox(renderer);
		this.vBoxL = new VBox();
		this.vBoxR = new VBox();
		
		doConfigure();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
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
	 * Returns the {@link Renderer} instance associated with this {@code RendererViewPane} instance.
	 * 
	 * @return the {@code Renderer} instance associated with this {@code RendererViewPane} instance
	 */
	public Renderer getRenderer() {
		return this.renderer;
	}
	
	/**
	 * This method is called when it's time to render.
	 */
	public void render() {
		this.concurrentImageCanvas.render();
	}
	
	/**
	 * This method is called when it's time to save the {@link Image}.
	 */
	public void save() {
		final Optional<File> optionalFile = getFile();
		
		if(optionalFile.isPresent()) {
			final File file = optionalFile.get();
			
			final Renderer renderer = this.renderer;
			
			final RendererConfiguration rendererConfiguration = renderer.getRendererConfiguration();
			
			final
			Image image = rendererConfiguration.getImage();
			image.save(file);
		}
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
	private boolean doRender(final Image image) {
		return this.renderer.render();
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
		for(final Primitive primitive : this.renderer.getRendererConfiguration().getScene().getPrimitives()) {
			this.objectTreeView.add(primitive);
		}
		
//		Configure the ProgressBar:
		this.progressBar.setProgress(0.0D);
		
//		Configure the Renderer:
		this.renderer.setRendererObserver(new RendererObserverImpl(this.labelRenderPass, this.labelRenderTime, this.labelRenderTimePerPass, this.progressBar));
		
//		Configure the VBox for L:
		this.vBoxL.getChildren().add(this.rendererBox);
		this.vBoxL.getChildren().add(this.sceneBox);
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
		this.renderer.getRendererConfiguration().getScene().removePrimitive(primitive);
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
	
	private static RendererConfiguration doCreateRendererConfiguration(final RenderingAlgorithm renderingAlgorithm, final Scene scene) {
		final
		RendererConfiguration rendererConfiguration = new RendererConfiguration();
		rendererConfiguration.setImage(new Image(32, 32, Color3F.BLACK, new BoxFilter()));
		rendererConfiguration.setPreviewMode(true);
		rendererConfiguration.setRenderingAlgorithm(renderingAlgorithm);
		rendererConfiguration.setRenderPasses(10);
		rendererConfiguration.setSampler(new RandomSampler());
		rendererConfiguration.setScene(scene);
		
		return rendererConfiguration;
	}
	
	private static RenderingAlgorithm doCreateRenderingAlgorithm(final Material material) {
		if(material instanceof PBRTMaterial) {
			return RenderingAlgorithm.PATH_TRACING;
		} else if(material instanceof RayitoMaterial) {
			return RenderingAlgorithm.PATH_TRACING;
		} else {
			return RenderingAlgorithm.PATH_TRACING;
		}
	}
	
	private static WritableImage doCreateWritableImageMaterial(final Material material) {
		final
		Renderer renderer = new CPURenderer(doCreateRendererConfiguration(doCreateRenderingAlgorithm(material), Previews.createMaterialPreviewScene(material)), new NoOpRendererObserver());
		renderer.render();
		
		final
		Image image = renderer.getRendererConfiguration().getImage();
		image.drawRectangle(new Rectangle2I(new Point2I(0, 0), new Point2I(image.getResolutionX() - 1, image.getResolutionY() - 1)), new Color3F(181, 181, 181));
		
		return image.toWritableImage();
	}
	
	@SuppressWarnings("unused")
	private static WritableImage doCreateWritableImageShape(final Shape3F shape) {
		final
		Image image = new Image(32, 32, Color3F.WHITE);
		image.drawRectangle(new Rectangle2I(new Point2I(0, 0), new Point2I(image.getResolutionX() - 1, image.getResolutionY() - 1)), new Color3F(181, 181, 181));
		
		return image.toWritableImage();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static final class RendererBox extends VBox {
		private final Button buttonUpdateRenderer;
		private final ComboBox<RenderingAlgorithm> comboBoxRenderingAlgorithm;
		private final Renderer renderer;
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		public RendererBox(final Renderer renderer) {
			this.buttonUpdateRenderer = new Button();
			this.comboBoxRenderingAlgorithm = new ComboBox<>();
			this.renderer = Objects.requireNonNull(renderer, "renderer == null");
			
			doConfigure();
		}
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		private void doConfigure() {
//			Configure the Button for Update Renderer:
			this.buttonUpdateRenderer.setMaxWidth(Double.MAX_VALUE);
			this.buttonUpdateRenderer.setOnAction(this::doOnActionButtonUpdateRenderer);
			this.buttonUpdateRenderer.setText("Update Renderer");
			
//			Configure the ComboBox for Rendering Algorithm:
			this.comboBoxRenderingAlgorithm.getItems().add(RenderingAlgorithm.AMBIENT_OCCLUSION);
			this.comboBoxRenderingAlgorithm.getItems().add(RenderingAlgorithm.PATH_TRACING);
			this.comboBoxRenderingAlgorithm.getItems().add(RenderingAlgorithm.PATH_TRACING_P_B_R_T);
			this.comboBoxRenderingAlgorithm.getItems().add(RenderingAlgorithm.PATH_TRACING_RAYITO);
			this.comboBoxRenderingAlgorithm.getItems().add(RenderingAlgorithm.PATH_TRACING_SMALL_P_T_ITERATIVE);
			this.comboBoxRenderingAlgorithm.getItems().add(RenderingAlgorithm.PATH_TRACING_SMALL_P_T_RECURSIVE);
			this.comboBoxRenderingAlgorithm.getItems().add(RenderingAlgorithm.RAY_CASTING);
			this.comboBoxRenderingAlgorithm.setValue(this.renderer.getRendererConfiguration().getRenderingAlgorithm());
			
//			Configure the RendererBox:
			getChildren().add(Labels.createLabel("Renderer Configuration", 16.0D));
			getChildren().add(this.comboBoxRenderingAlgorithm);
			getChildren().add(this.buttonUpdateRenderer);
			setAlignment(Pos.CENTER);
			setFillWidth(true);
			setSpacing(10.0D);
		}
		
		@SuppressWarnings("unused")
		private void doOnActionButtonUpdateRenderer(final ActionEvent actionEvent) {
			final RenderingAlgorithm renderingAlgorithm = this.comboBoxRenderingAlgorithm.getValue();
			
			if(renderingAlgorithm != null) {
				this.renderer.getRendererConfiguration().setRenderingAlgorithm(renderingAlgorithm);
				this.renderer.renderShutdown();
				this.renderer.clear();
			}
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static final class RendererObserverImpl implements RendererObserver {
		private final Label labelRenderPass;
		private final Label labelRenderTime;
		private final Label labelRenderTimePerPass;
		private final ProgressBar progressBar;
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		public RendererObserverImpl(final Label labelRenderPass, final Label labelRenderTime, final Label labelRenderTimePerPass, final ProgressBar progressBar) {
			this.labelRenderPass = Objects.requireNonNull(labelRenderPass, "labelRenderPass == null");
			this.labelRenderTime = Objects.requireNonNull(labelRenderTime, "labelRenderTime == null");
			this.labelRenderTimePerPass = Objects.requireNonNull(labelRenderTimePerPass, "labelRenderTimePerPass == null");
			this.progressBar = Objects.requireNonNull(progressBar, "progressBar == null");
		}
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		@Override
		public void onRenderDisplay(final Renderer renderer, final Image image) {
//			Do nothing.
		}
		
		@Override
		public void onRenderPassComplete(final Renderer renderer, final int renderPass, final int renderPasses, final long elapsedTimeMillis) {
			Platform.runLater(() -> {
				this.labelRenderPass.setText("Render Pass: " + renderer.getRendererConfiguration().getRenderPass());
				this.labelRenderTime.setText("Render Time: " + renderer.getRendererConfiguration().getTimer().getTime());
				this.labelRenderTimePerPass.setText("Render Time Per Pass: " + elapsedTimeMillis);
				
				this.progressBar.setProgress(1.0D);
			});
		}
		
		@Override
		public void onRenderPassProgress(final Renderer renderer, final int renderPass, final int renderPasses, final double percent) {
			Platform.runLater(() -> {
				this.labelRenderPass.setText("Render Pass: " + renderer.getRendererConfiguration().getRenderPass());
				this.labelRenderTime.setText("Render Time: " + renderer.getRendererConfiguration().getTimer().getTime());
				
				this.progressBar.setProgress(percent);
			});
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static final class SceneBox extends VBox {
		private final Button buttonBuildAccelerationStructure;
		private final Button buttonClearAccelerationStructure;
		private final Button buttonPrimitiveAdd;
		private final ComboBox<String> comboBoxPrimitiveAddMaterial;
		private final ComboBox<String> comboBoxPrimitiveAddShape;
		private final Renderer renderer;
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		public SceneBox(final Renderer renderer) {
			this.buttonBuildAccelerationStructure = new Button();
			this.buttonClearAccelerationStructure = new Button();
			this.buttonPrimitiveAdd = new Button();
			this.comboBoxPrimitiveAddMaterial = new ComboBox<>();
			this.comboBoxPrimitiveAddShape = new ComboBox<>();
			this.renderer = Objects.requireNonNull(renderer, "renderer == null");
			
			doConfigure();
		}
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		public Renderer getRenderer() {
			return this.renderer;
		}
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		private Camera doGetCamera() {
			return doGetScene().getCamera();
		}
		
		private Material doCreateMaterial() {
			final String selectedItem = this.comboBoxPrimitiveAddMaterial.getSelectionModel().getSelectedItem();
			
			if(selectedItem != null) {
				switch(selectedItem) {
					case GlassPBRTMaterial.NAME:
						return new GlassPBRTMaterial();
					case HairPBRTMaterial.NAME:
						return new HairPBRTMaterial();
					case MattePBRTMaterial.NAME:
						return new MattePBRTMaterial();
					case MetalPBRTMaterial.NAME:
						return new MetalPBRTMaterial();
					case MirrorPBRTMaterial.NAME:
						return new MirrorPBRTMaterial();
					case PlasticPBRTMaterial.NAME:
						return new PlasticPBRTMaterial();
					case SubstratePBRTMaterial.NAME:
						return new SubstratePBRTMaterial();
					case UberPBRTMaterial.NAME:
						return new UberPBRTMaterial();
					case GlassRayitoMaterial.NAME:
						return new GlassRayitoMaterial();
					case MatteRayitoMaterial.NAME:
						return new MatteRayitoMaterial();
					case MetalRayitoMaterial.NAME:
						return new MetalRayitoMaterial();
					case MirrorRayitoMaterial.NAME:
						return new MirrorRayitoMaterial();
					default:
						return null;
				}
			}
			
			return null;
		}
		
		private Point3F doGetPointByShape(final Shape3F shape) {
			if(shape instanceof Plane3F) {
				return doGetCamera().getPointBelowEye(1.0F);
			} else if(shape instanceof RectangularCuboid3F) {
				return doGetCamera().getPointInfrontOfEye(7.5F);
			} else if(shape instanceof Sphere3F) {
				return doGetCamera().getPointInfrontOfEye(7.5F);
			} else if(shape instanceof Torus3F) {
				return doGetCamera().getPointInfrontOfEye(7.5F);
			} else if(shape instanceof Triangle3F) {
				return doGetCamera().getPointInfrontOfEye(7.5F);
			} else {
				return new Point3F();
			}
		}
		
		private Scene doGetScene() {
			return getRenderer().getRendererConfiguration().getScene();
		}
		
		private Shape3F doCreateShape() {
			final String selectedItem = this.comboBoxPrimitiveAddShape.getSelectionModel().getSelectedItem();
			
			if(selectedItem != null) {
				switch(selectedItem) {
					case Plane3F.NAME:
						return new Plane3F();
					case RectangularCuboid3F.NAME:
						return new RectangularCuboid3F();
					case Sphere3F.NAME:
						return new Sphere3F();
					case Torus3F.NAME:
						return new Torus3F();
					case Triangle3F.NAME:
						return new Triangle3F();
					default:
						return null;
				}
			}
			
			return null;
		}
		
		private void doAddPrimitiveByMaterialAndShape(final Material material, final Shape3F shape) {
			final Transform transform = new Transform(doGetPointByShape(shape));
			
			final Primitive primitive = new Primitive(material, shape, transform);
			
			final
			Scene scene = doGetScene();
			scene.addPrimitive(primitive);
		}
		
		private void doConfigure() {
//			Configure the Button for Build Acceleration Structure:
			this.buttonBuildAccelerationStructure.setMaxWidth(Double.MAX_VALUE);
			this.buttonBuildAccelerationStructure.setOnAction(this::doOnActionButtonBuildAccelerationStructure);
			this.buttonBuildAccelerationStructure.setText("Build Acceleration Structure");
			
//			Configure the Button for Clear Acceleration Structure:
			this.buttonClearAccelerationStructure.setMaxWidth(Double.MAX_VALUE);
			this.buttonClearAccelerationStructure.setOnAction(this::doOnActionButtonClearAccelerationStructure);
			this.buttonClearAccelerationStructure.setText("Clear Acceleration Structure");
			
//			Configure the Button for Primitive Add:
			this.buttonPrimitiveAdd.setMaxWidth(Double.MAX_VALUE);
			this.buttonPrimitiveAdd.setOnAction(this::doOnActionButtonPrimitiveAdd);
			this.buttonPrimitiveAdd.setText("Add Primitive");
			
//			Configure the ComboBox for Primitive Add Material:
			this.comboBoxPrimitiveAddMaterial.getItems().add(GlassPBRTMaterial.NAME);
			this.comboBoxPrimitiveAddMaterial.getItems().add(HairPBRTMaterial.NAME);
			this.comboBoxPrimitiveAddMaterial.getItems().add(MattePBRTMaterial.NAME);
			this.comboBoxPrimitiveAddMaterial.getItems().add(MetalPBRTMaterial.NAME);
			this.comboBoxPrimitiveAddMaterial.getItems().add(MirrorPBRTMaterial.NAME);
			this.comboBoxPrimitiveAddMaterial.getItems().add(PlasticPBRTMaterial.NAME);
			this.comboBoxPrimitiveAddMaterial.getItems().add(SubstratePBRTMaterial.NAME);
			this.comboBoxPrimitiveAddMaterial.getItems().add(UberPBRTMaterial.NAME);
			this.comboBoxPrimitiveAddMaterial.getItems().add(GlassRayitoMaterial.NAME);
			this.comboBoxPrimitiveAddMaterial.getItems().add(MatteRayitoMaterial.NAME);
			this.comboBoxPrimitiveAddMaterial.getItems().add(MetalRayitoMaterial.NAME);
			this.comboBoxPrimitiveAddMaterial.getItems().add(MirrorRayitoMaterial.NAME);
			this.comboBoxPrimitiveAddMaterial.setMaxWidth(Double.MAX_VALUE);
			this.comboBoxPrimitiveAddMaterial.setValue(MattePBRTMaterial.NAME);
			
//			Configure the ComboBox for Primitive Add Shape:
			this.comboBoxPrimitiveAddShape.getItems().add(Plane3F.NAME);
			this.comboBoxPrimitiveAddShape.getItems().add(RectangularCuboid3F.NAME);
			this.comboBoxPrimitiveAddShape.getItems().add(Sphere3F.NAME);
			this.comboBoxPrimitiveAddShape.getItems().add(Torus3F.NAME);
			this.comboBoxPrimitiveAddShape.getItems().add(Triangle3F.NAME);
			this.comboBoxPrimitiveAddShape.setMaxWidth(Double.MAX_VALUE);
			this.comboBoxPrimitiveAddShape.setValue(Plane3F.NAME);
			
//			Configure the SceneBox:
			getChildren().add(Labels.createLabel("Scene Configuration", 16.0D));
			getChildren().add(this.comboBoxPrimitiveAddMaterial);
			getChildren().add(this.comboBoxPrimitiveAddShape);
			getChildren().add(this.buttonPrimitiveAdd);
			getChildren().add(new Separator());
			getChildren().add(this.buttonBuildAccelerationStructure);
			getChildren().add(this.buttonClearAccelerationStructure);
			setAlignment(Pos.CENTER);
			setFillWidth(true);
			setSpacing(10.0D);
		}
		
		@SuppressWarnings("unused")
		private void doOnActionButtonBuildAccelerationStructure(final ActionEvent actionEvent) {
			final
			Scene scene = doGetScene();
			scene.buildAccelerationStructure();
			
			this.renderer.renderShutdown();
			this.renderer.clear();
		}
		
		@SuppressWarnings("unused")
		private void doOnActionButtonClearAccelerationStructure(final ActionEvent actionEvent) {
			final
			Scene scene = doGetScene();
			scene.clearAccelerationStructure();
			
			this.renderer.renderShutdown();
			this.renderer.clear();
		}
		
		@SuppressWarnings("unused")
		private void doOnActionButtonPrimitiveAdd(final ActionEvent actionEvent) {
			final Material material = doCreateMaterial();
			
			final Shape3F shape = doCreateShape();
			
			if(material != null && shape != null) {
				doAddPrimitiveByMaterialAndShape(material, shape);
			}
		}
	}
}