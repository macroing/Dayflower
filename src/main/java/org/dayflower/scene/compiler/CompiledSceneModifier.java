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
package org.dayflower.scene.compiler;

import java.util.Objects;
import java.util.Optional;

import org.dayflower.geometry.BoundingVolume3F;
import org.dayflower.geometry.Shape3F;
import org.dayflower.geometry.boundingvolume.AxisAlignedBoundingBox3F;
import org.dayflower.geometry.boundingvolume.BoundingSphere3F;
import org.dayflower.geometry.boundingvolume.InfiniteBoundingVolume3F;
import org.dayflower.geometry.shape.Cone3F;
import org.dayflower.geometry.shape.Cylinder3F;
import org.dayflower.geometry.shape.Disk3F;
import org.dayflower.geometry.shape.Hyperboloid3F;
import org.dayflower.geometry.shape.Paraboloid3F;
import org.dayflower.geometry.shape.Plane3F;
import org.dayflower.geometry.shape.Rectangle3F;
import org.dayflower.geometry.shape.RectangularCuboid3F;
import org.dayflower.geometry.shape.Sphere3F;
import org.dayflower.geometry.shape.Torus3F;
import org.dayflower.geometry.shape.Triangle3F;
import org.dayflower.geometry.shape.TriangleMesh3F;
import org.dayflower.scene.AreaLight;
import org.dayflower.scene.Light;
import org.dayflower.scene.Material;
import org.dayflower.scene.Primitive;
import org.dayflower.scene.light.DiffuseAreaLight;
import org.dayflower.scene.light.DirectionalLight;
import org.dayflower.scene.light.ImageLight;
import org.dayflower.scene.light.PerezLight;
import org.dayflower.scene.light.PointLight;
import org.dayflower.scene.light.SpotLight;
import org.dayflower.scene.material.BullseyeMaterial;
import org.dayflower.scene.material.CheckerboardMaterial;
import org.dayflower.scene.material.ClearCoatMaterial;
import org.dayflower.scene.material.DisneyMaterial;
import org.dayflower.scene.material.GlassMaterial;
import org.dayflower.scene.material.GlossyMaterial;
import org.dayflower.scene.material.MatteMaterial;
import org.dayflower.scene.material.MetalMaterial;
import org.dayflower.scene.material.MirrorMaterial;
import org.dayflower.scene.material.PlasticMaterial;
import org.dayflower.scene.material.PolkaDotMaterial;
import org.dayflower.scene.material.SubstrateMaterial;
import org.dayflower.scene.modifier.Modifier;
import org.dayflower.scene.modifier.NoOpModifier;
import org.dayflower.scene.modifier.NormalMapLDRImageModifier;
import org.dayflower.scene.modifier.SimplexNoiseNormalMapModifier;
import org.dayflower.scene.texture.BlendTexture;
import org.dayflower.scene.texture.BullseyeTexture;
import org.dayflower.scene.texture.CheckerboardTexture;
import org.dayflower.scene.texture.ConstantTexture;
import org.dayflower.scene.texture.DotProductTexture;
import org.dayflower.scene.texture.LDRImageTexture;
import org.dayflower.scene.texture.MarbleTexture;
import org.dayflower.scene.texture.PolkaDotTexture;
import org.dayflower.scene.texture.SimplexFractionalBrownianMotionTexture;
import org.dayflower.scene.texture.SurfaceNormalTexture;
import org.dayflower.scene.texture.Texture;
import org.dayflower.scene.texture.UVTexture;

/**
 * A {@code CompiledSceneModifier} is used to modify a {@link CompiledScene} instance.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class CompiledSceneModifier {
	private final CompiledScene compiledScene;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code CompiledSceneModifier} instance.
	 * <p>
	 * If {@code compiledScene} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param compiledScene a {@link CompiledScene} instance
	 * @throws NullPointerException thrown if, and only if, {@code compiledScene} is {@code null}
	 */
	public CompiledSceneModifier(final CompiledScene compiledScene) {
		this.compiledScene = Objects.requireNonNull(compiledScene, "compiledScene == null");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the {@link CompiledScene} instance that is associated with this {@code CompiledSceneModifier} instance.
	 * 
	 * @return the {@code CompiledScene} instance that is associated with this {@code CompiledSceneModifier} instance
	 */
	public CompiledScene getCompiledScene() {
		return this.compiledScene;
	}
	
	/**
	 * Adds {@code light} to the associated {@link CompiledScene} instance, if absent.
	 * <p>
	 * Returns {@code true} if, and only if, {@code light} was added, {@code false} otherwise.
	 * <p>
	 * If {@code light} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param light the {@link Light} instance to add
	 * @return {@code true} if, and only if, {@code light} was added, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, {@code light} is {@code null}
	 */
	@SuppressWarnings("static-method")
	public boolean addLight(final Light light) {
//		TODO: Implement!
		Objects.requireNonNull(light, "light == null");
		
		if(!CompiledLightCache.isSupported(light)) {
			return false;
		}
		
		return false;
	}
	
	/**
	 * Adds {@code primitive} to the associated {@link CompiledScene} instance, if absent.
	 * <p>
	 * Returns {@code true} if, and only if, {@code primitive} was added, {@code false} otherwise.
	 * <p>
	 * If {@code primitive} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param primitive the {@link Primitive} instance to add
	 * @return {@code true} if, and only if, {@code primitive} was added, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, {@code primitive} is {@code null}
	 */
	public boolean addPrimitive(final Primitive primitive) {
		Objects.requireNonNull(primitive, "primitive == null");
		
		if(!CompiledPrimitiveCache.isSupported(primitive)) {
			return false;
		}
		
		final int areaLightOffset = doAddOptionalAreaLight(primitive.getAreaLight());
		final int boundingVolumeOffset = doAddBoundingVolume3F(primitive.getBoundingVolume());
		final int materialOffset = doAddMaterial(primitive.getMaterial());
		final int shapeOffset = doAddShape3F(primitive.getShape());
		
		final int primitiveCount = this.compiledScene.getCompiledPrimitiveCache().getPrimitiveCount();
		final int primitiveOffset = this.compiledScene.getCompiledPrimitiveCache().addPrimitive(CompiledPrimitiveCache.toPrimitive(primitive, areaLight -> areaLightOffset, boundingVolume3F -> boundingVolumeOffset, material -> materialOffset, shape3F -> shapeOffset), CompiledPrimitiveCache.toMatrix44Fs(primitive.getTransform()));
		
		return primitiveOffset == primitiveCount;
	}
	
	/**
	 * Removes {@code light} from the associated {@link CompiledScene} instance, if present.
	 * <p>
	 * Returns {@code true} if, and only if, {@code light} was removed, {@code false} otherwise.
	 * <p>
	 * If {@code light} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param light the {@link Light} instance to remove
	 * @return {@code true} if, and only if, {@code light} was removed, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, {@code light} is {@code null}
	 */
	@SuppressWarnings("static-method")
	public boolean removeLight(final Light light) {
//		TODO: Implement!
		Objects.requireNonNull(light, "light == null");
		
		if(!CompiledLightCache.isSupported(light)) {
			return false;
		}
		
		return false;
	}
	
	/**
	 * Removes {@code primitive} from the associated {@link CompiledScene} instance, if present.
	 * <p>
	 * Returns {@code true} if, and only if, {@code primitive} was removed, {@code false} otherwise.
	 * <p>
	 * If {@code primitive} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param primitive the {@link Primitive} instance to remove
	 * @return {@code true} if, and only if, {@code primitive} was removed, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, {@code primitive} is {@code null}
	 */
	public boolean removePrimitive(final Primitive primitive) {
		Objects.requireNonNull(primitive, "primitive == null");
		
		if(!CompiledPrimitiveCache.isSupported(primitive)) {
			return false;
		}
		
		final int areaLightOffset = doAddOptionalAreaLight(primitive.getAreaLight());
		final int boundingVolumeOffset = doAddBoundingVolume3F(primitive.getBoundingVolume());
		final int materialOffset = doAddMaterial(primitive.getMaterial());
		final int shapeOffset = doAddShape3F(primitive.getShape());
		
		return this.compiledScene.getCompiledPrimitiveCache().removePrimitive(CompiledPrimitiveCache.toPrimitive(primitive, areaLight -> areaLightOffset, boundingVolume3F -> boundingVolumeOffset, material -> materialOffset, shape3F -> shapeOffset));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private int doAddBoundingVolume3F(final BoundingVolume3F boundingVolume3F) {
		final CompiledBoundingVolume3FCache compiledBoundingVolume3FCache = this.compiledScene.getCompiledBoundingVolume3FCache();
		
		if(boundingVolume3F instanceof AxisAlignedBoundingBox3F) {
			final AxisAlignedBoundingBox3F axisAlignedBoundingBox3F = AxisAlignedBoundingBox3F.class.cast(boundingVolume3F);
			
			final int offsetRelative = compiledBoundingVolume3FCache.addAxisAlignedBoundingBox3F(CompiledBoundingVolume3FCache.toAxisAlignedBoundingBox3F(axisAlignedBoundingBox3F));
			
			return offsetRelative;
		} else if(boundingVolume3F instanceof BoundingSphere3F) {
			final BoundingSphere3F boundingSphere3F = BoundingSphere3F.class.cast(boundingVolume3F);
			
			final int offsetRelative = compiledBoundingVolume3FCache.addBoundingSphere3F(CompiledBoundingVolume3FCache.toBoundingSphere3F(boundingSphere3F));
			
			return offsetRelative;
		} else if(boundingVolume3F instanceof InfiniteBoundingVolume3F) {
			return 0;
		} else {
			throw new IllegalArgumentException();
		}
	}
	
	private int doAddLight(final Light light) {
		final CompiledLightCache compiledLightCache = this.compiledScene.getCompiledLightCache();
		
		if(light instanceof DiffuseAreaLight) {
			final DiffuseAreaLight diffuseAreaLight = DiffuseAreaLight.class.cast(light);
			
			final int offsetRelative = compiledLightCache.addDiffuseAreaLight(CompiledLightCache.toDiffuseAreaLight(diffuseAreaLight, this::doAddShape3F));
			
			return offsetRelative;
		} else if(light instanceof DirectionalLight) {
			final DirectionalLight directionalLight = DirectionalLight.class.cast(light);
			
			final int offsetRelative = compiledLightCache.addDirectionalLight(CompiledLightCache.toDirectionalLight(directionalLight));
			
			return offsetRelative;
		} else if(light instanceof ImageLight) {
			final ImageLight imageLight = ImageLight.class.cast(light);
			
			final int offsetRelative = compiledLightCache.addImageLight(CompiledLightCache.toImageLight(imageLight));
			
			return offsetRelative;
		} else if(light instanceof PerezLight) {
			final PerezLight perezLight = PerezLight.class.cast(light);
			
			final int offsetRelative = compiledLightCache.addPerezLight(CompiledLightCache.toPerezLight(perezLight));
			
			return offsetRelative;
		} else if(light instanceof PointLight) {
			final PointLight pointLight = PointLight.class.cast(light);
			
			final int offsetRelative = compiledLightCache.addPointLight(CompiledLightCache.toPointLight(pointLight));
			
			return offsetRelative;
		} else if(light instanceof SpotLight) {
			final SpotLight spotLight = SpotLight.class.cast(light);
			
			final int offsetRelative = compiledLightCache.addSpotLight(CompiledLightCache.toSpotLight(spotLight));
			
			return offsetRelative;
		} else {
			throw new IllegalArgumentException();
		}
	}
	
	private int doAddMaterial(final Material material) {
		final CompiledMaterialCache compiledMaterialCache = this.compiledScene.getCompiledMaterialCache();
		
		if(material instanceof BullseyeMaterial) {
			final BullseyeMaterial bullseyeMaterial = BullseyeMaterial.class.cast(material);
			
			final int offsetRelative = compiledMaterialCache.addBullseyeMaterial(CompiledMaterialCache.toBullseyeMaterial(bullseyeMaterial, this::doAddMaterial));
			final int offsetAbsolute = offsetRelative * CompiledMaterialCache.BULLSEYE_MATERIAL_LENGTH;
			
			return offsetAbsolute;
		} else if(material instanceof CheckerboardMaterial) {
			final CheckerboardMaterial checkerboardMaterial = CheckerboardMaterial.class.cast(material);
			
			final int offsetRelative = compiledMaterialCache.addCheckerboardMaterial(CompiledMaterialCache.toCheckerboardMaterial(checkerboardMaterial, this::doAddMaterial));
			final int offsetAbsolute = offsetRelative * CompiledMaterialCache.CHECKERBOARD_MATERIAL_LENGTH;
			
			return offsetAbsolute;
		} else if(material instanceof ClearCoatMaterial) {
			final ClearCoatMaterial clearCoatMaterial = ClearCoatMaterial.class.cast(material);
			
			final int offsetRelative = compiledMaterialCache.addClearCoatMaterial(CompiledMaterialCache.toClearCoatMaterial(clearCoatMaterial, this::doAddModifier, this::doAddTexture));
			final int offsetAbsolute = offsetRelative * CompiledMaterialCache.CLEAR_COAT_MATERIAL_LENGTH;
			
			return offsetAbsolute;
		} else if(material instanceof DisneyMaterial) {
			final DisneyMaterial disneyMaterial = DisneyMaterial.class.cast(material);
			
			final int offsetRelative = compiledMaterialCache.addDisneyMaterial(CompiledMaterialCache.toDisneyMaterial(disneyMaterial, this::doAddModifier, this::doAddTexture));
			final int offsetAbsolute = offsetRelative * CompiledMaterialCache.DISNEY_MATERIAL_LENGTH;
			
			return offsetAbsolute;
		} else if(material instanceof GlassMaterial) {
			final GlassMaterial glassMaterial = GlassMaterial.class.cast(material);
			
			final int offsetRelative = compiledMaterialCache.addGlassMaterial(CompiledMaterialCache.toGlassMaterial(glassMaterial, this::doAddModifier, this::doAddTexture));
			final int offsetAbsolute = offsetRelative * CompiledMaterialCache.GLASS_MATERIAL_LENGTH;
			
			return offsetAbsolute;
		} else if(material instanceof GlossyMaterial) {
			final GlossyMaterial glossyMaterial = GlossyMaterial.class.cast(material);
			
			final int offsetRelative = compiledMaterialCache.addGlossyMaterial(CompiledMaterialCache.toGlossyMaterial(glossyMaterial, this::doAddModifier, this::doAddTexture));
			final int offsetAbsolute = offsetRelative * CompiledMaterialCache.GLOSSY_MATERIAL_LENGTH;
			
			return offsetAbsolute;
		} else if(material instanceof MatteMaterial) {
			final MatteMaterial matteMaterial = MatteMaterial.class.cast(material);
			
			final int offsetRelative = compiledMaterialCache.addMatteMaterial(CompiledMaterialCache.toMatteMaterial(matteMaterial, this::doAddModifier, this::doAddTexture));
			final int offsetAbsolute = offsetRelative * CompiledMaterialCache.MATTE_MATERIAL_LENGTH;
			
			return offsetAbsolute;
		} else if(material instanceof MetalMaterial) {
			final MetalMaterial metalMaterial = MetalMaterial.class.cast(material);
			
			final int offsetRelative = compiledMaterialCache.addMetalMaterial(CompiledMaterialCache.toMetalMaterial(metalMaterial, this::doAddModifier, this::doAddTexture));
			final int offsetAbsolute = offsetRelative * CompiledMaterialCache.METAL_MATERIAL_LENGTH;
			
			return offsetAbsolute;
		} else if(material instanceof MirrorMaterial) {
			final MirrorMaterial mirrorMaterial = MirrorMaterial.class.cast(material);
			
			final int offsetRelative = compiledMaterialCache.addMirrorMaterial(CompiledMaterialCache.toMirrorMaterial(mirrorMaterial, this::doAddModifier, this::doAddTexture));
			final int offsetAbsolute = offsetRelative * CompiledMaterialCache.MIRROR_MATERIAL_LENGTH;
			
			return offsetAbsolute;
		} else if(material instanceof PlasticMaterial) {
			final PlasticMaterial plasticMaterial = PlasticMaterial.class.cast(material);
			
			final int offsetRelative = compiledMaterialCache.addPlasticMaterial(CompiledMaterialCache.toPlasticMaterial(plasticMaterial, this::doAddModifier, this::doAddTexture));
			final int offsetAbsolute = offsetRelative * CompiledMaterialCache.PLASTIC_MATERIAL_LENGTH;
			
			return offsetAbsolute;
		} else if(material instanceof PolkaDotMaterial) {
			final PolkaDotMaterial polkaDotMaterial = PolkaDotMaterial.class.cast(material);
			
			final int offsetRelative = compiledMaterialCache.addPolkaDotMaterial(CompiledMaterialCache.toPolkaDotMaterial(polkaDotMaterial, this::doAddMaterial));
			final int offsetAbsolute = offsetRelative * CompiledMaterialCache.POLKA_DOT_MATERIAL_LENGTH;
			
			return offsetAbsolute;
		} else if(material instanceof SubstrateMaterial) {
			final SubstrateMaterial substrateMaterial = SubstrateMaterial.class.cast(material);
			
			final int offsetRelative = compiledMaterialCache.addSubstrateMaterial(CompiledMaterialCache.toSubstrateMaterial(substrateMaterial, this::doAddModifier, this::doAddTexture));
			final int offsetAbsolute = offsetRelative * CompiledMaterialCache.SUBSTRATE_MATERIAL_LENGTH;
			
			return offsetAbsolute;
		} else {
			throw new IllegalArgumentException();
		}
	}
	
	private int doAddModifier(final Modifier modifier) {
		final CompiledModifierCache compiledModifierCache = this.compiledScene.getCompiledModifierCache();
		
		if(modifier instanceof NormalMapLDRImageModifier) {
			final NormalMapLDRImageModifier normalMapLDRImageModifier = NormalMapLDRImageModifier.class.cast(modifier);
			
			final int offsetRelative = compiledModifierCache.addNormalMapLDRImageModifier(CompiledModifierCache.toNormalMapLDRImageModifier(normalMapLDRImageModifier));
			
			return offsetRelative;
		} else if(modifier instanceof NoOpModifier) {
			return 0;
		} else if(modifier instanceof SimplexNoiseNormalMapModifier) {
			final SimplexNoiseNormalMapModifier simplexNoiseNormalMapModifier = SimplexNoiseNormalMapModifier.class.cast(modifier);
			
			final int offsetRelative = compiledModifierCache.addSimplexNoiseNormalMapModifier(CompiledModifierCache.toSimplexNoiseNormalMapModifier(simplexNoiseNormalMapModifier));
			
			return offsetRelative;
		} else {
			throw new IllegalArgumentException();
		}
	}
	
	private int doAddOptionalAreaLight(final Optional<AreaLight> optionalAreaLight) {
		return optionalAreaLight.isPresent() ? doAddLight(optionalAreaLight.get()) : 0;
	}
	
	private int doAddShape3F(final Shape3F shape3F) {
		final CompiledShape3FCache compiledShape3FCache = this.compiledScene.getCompiledShape3FCache();
		
		if(shape3F instanceof Cone3F) {
			final Cone3F cone3F = Cone3F.class.cast(shape3F);
			
			final int offsetRelative = compiledShape3FCache.addCone3F(CompiledShape3FCache.toCone3F(cone3F));
			final int offsetAbsolute = offsetRelative * CompiledShape3FCache.CONE_3_F_LENGTH;
			
			return offsetAbsolute;
		} else if(shape3F instanceof Cylinder3F) {
			final Cylinder3F cylinder3F = Cylinder3F.class.cast(shape3F);
			
			final int offsetRelative = compiledShape3FCache.addCylinder3F(CompiledShape3FCache.toCylinder3F(cylinder3F));
			final int offsetAbsolute = offsetRelative * CompiledShape3FCache.CYLINDER_3_F_LENGTH;
			
			return offsetAbsolute;
		} else if(shape3F instanceof Disk3F) {
			final Disk3F disk3F = Disk3F.class.cast(shape3F);
			
			final int offsetRelative = compiledShape3FCache.addDisk3F(CompiledShape3FCache.toDisk3F(disk3F));
			final int offsetAbsolute = offsetRelative * CompiledShape3FCache.DISK_3_F_LENGTH;
			
			return offsetAbsolute;
		} else if(shape3F instanceof Hyperboloid3F) {
			final Hyperboloid3F hyperboloid3F = Hyperboloid3F.class.cast(shape3F);
			
			final int offsetRelative = compiledShape3FCache.addHyperboloid3F(CompiledShape3FCache.toHyperboloid3F(hyperboloid3F));
			final int offsetAbsolute = offsetRelative * CompiledShape3FCache.HYPERBOLOID_3_F_LENGTH;
			
			return offsetAbsolute;
		} else if(shape3F instanceof Paraboloid3F) {
			final Paraboloid3F paraboloid3F = Paraboloid3F.class.cast(shape3F);
			
			final int offsetRelative = compiledShape3FCache.addParaboloid3F(CompiledShape3FCache.toParaboloid3F(paraboloid3F));
			final int offsetAbsolute = offsetRelative * CompiledShape3FCache.PARABOLOID_3_F_LENGTH;
			
			return offsetAbsolute;
		} else if(shape3F instanceof Plane3F) {
			return 0;
		} else if(shape3F instanceof Rectangle3F) {
			final Rectangle3F rectangle3F = Rectangle3F.class.cast(shape3F);
			
			final int offsetRelative = compiledShape3FCache.addRectangle3F(CompiledShape3FCache.toRectangle3F(rectangle3F));
			final int offsetAbsolute = offsetRelative * CompiledShape3FCache.RECTANGLE_3_F_LENGTH;
			
			return offsetAbsolute;
		} else if(shape3F instanceof RectangularCuboid3F) {
			final RectangularCuboid3F rectangularCuboid3F = RectangularCuboid3F.class.cast(shape3F);
			
			final int offsetRelative = compiledShape3FCache.addRectangularCuboid3F(CompiledShape3FCache.toRectangularCuboid3F(rectangularCuboid3F));
			final int offsetAbsolute = offsetRelative * CompiledShape3FCache.RECTANGULAR_CUBOID_3_F_LENGTH;
			
			return offsetAbsolute;
		} else if(shape3F instanceof Sphere3F) {
			return 0;
		} else if(shape3F instanceof Torus3F) {
			final Torus3F torus3F = Torus3F.class.cast(shape3F);
			
			final int offsetRelative = compiledShape3FCache.addTorus3F(CompiledShape3FCache.toTorus3F(torus3F));
			final int offsetAbsolute = offsetRelative * CompiledShape3FCache.TORUS_3_F_LENGTH;
			
			return offsetAbsolute;
		} else if(shape3F instanceof Triangle3F) {
			final Triangle3F triangle3F = Triangle3F.class.cast(shape3F);
			
			final int offsetRelative = compiledShape3FCache.addTriangle3F(CompiledShape3FCache.toTriangle3F(triangle3F));
			final int offsetAbsolute = offsetRelative * CompiledShape3FCache.TRIANGLE_3_F_LENGTH;
			
			return offsetAbsolute;
		} else if(shape3F instanceof TriangleMesh3F) {
			final TriangleMesh3F triangleMesh3F = TriangleMesh3F.class.cast(shape3F);
			
			final int offsetRelative = compiledShape3FCache.addTriangleMesh3F(CompiledShape3FCache.toTriangleMesh3F(triangleMesh3F, this::doAddBoundingVolume3F, this::doAddShape3F));
			final int offsetAbsolute = compiledShape3FCache.getTriangleMesh3FOffsets()[offsetRelative];
			
			return offsetAbsolute;
		} else {
			throw new IllegalArgumentException();
		}
	}
	
	private int doAddTexture(final Texture texture) {
		final CompiledTextureCache compiledTextureCache = this.compiledScene.getCompiledTextureCache();
		
		if(texture instanceof BlendTexture) {
			final BlendTexture blendTexture = BlendTexture.class.cast(texture);
			
			final int offsetRelative = compiledTextureCache.addBlendTexture(CompiledTextureCache.toBlendTexture(blendTexture, this::doAddTexture));
			
			return offsetRelative;
		} else if(texture instanceof BullseyeTexture) {
			final BullseyeTexture bullseyeTexture = BullseyeTexture.class.cast(texture);
			
			final int offsetRelative = compiledTextureCache.addBullseyeTexture(CompiledTextureCache.toBullseyeTexture(bullseyeTexture, this::doAddTexture));
			
			return offsetRelative;
		} else if(texture instanceof CheckerboardTexture) {
			final CheckerboardTexture checkerboardTexture = CheckerboardTexture.class.cast(texture);
			
			final int offsetRelative = compiledTextureCache.addCheckerboardTexture(CompiledTextureCache.toCheckerboardTexture(checkerboardTexture, this::doAddTexture));
			
			return offsetRelative;
		} else if(texture instanceof ConstantTexture) {
			final ConstantTexture constantTexture = ConstantTexture.class.cast(texture);
			
			final int offsetRelative = compiledTextureCache.addConstantTexture(CompiledTextureCache.toConstantTexture(constantTexture));
			
			return offsetRelative;
		} else if(texture instanceof DotProductTexture) {
			return 0;
		} else if(texture instanceof LDRImageTexture) {
			final LDRImageTexture lDRImageTexture = LDRImageTexture.class.cast(texture);
			
			final int offsetRelative = compiledTextureCache.addLDRImageTexture(CompiledTextureCache.toLDRImageTexture(lDRImageTexture));
			
			return offsetRelative;
		} else if(texture instanceof MarbleTexture) {
			final MarbleTexture marbleTexture = MarbleTexture.class.cast(texture);
			
			final int offsetRelative = compiledTextureCache.addMarbleTexture(CompiledTextureCache.toMarbleTexture(marbleTexture));
			
			return offsetRelative;
		} else if(texture instanceof PolkaDotTexture) {
			final PolkaDotTexture polkaDotTexture = PolkaDotTexture.class.cast(texture);
			
			final int offsetRelative = compiledTextureCache.addPolkaDotTexture(CompiledTextureCache.toPolkaDotTexture(polkaDotTexture, this::doAddTexture));
			
			return offsetRelative;
		} else if(texture instanceof SimplexFractionalBrownianMotionTexture) {
			final SimplexFractionalBrownianMotionTexture simplexFractionalBrownianMotionTexture = SimplexFractionalBrownianMotionTexture.class.cast(texture);
			
			final int offsetRelative = compiledTextureCache.addSimplexFractionalBrownianMotionTexture(CompiledTextureCache.toSimplexFractionalBrownianMotionTexture(simplexFractionalBrownianMotionTexture));
			
			return offsetRelative;
		} else if(texture instanceof SurfaceNormalTexture) {
			return 0;
		} else if(texture instanceof UVTexture) {
			return 0;
		} else {
			throw new IllegalArgumentException();
		}
	}
}