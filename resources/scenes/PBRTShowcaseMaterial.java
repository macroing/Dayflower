boolean isInBox = false;
boolean isTorus = false;

Texture textureLaminate = ImageTexture.load("./resources/textures/laminate.jpg", AngleF.degrees(0.0F), new Vector2F(0.5F, 0.5F));

Material material11 = new MatteMaterial(new ConstantTexture(new Color3F(20.0F)), textureLaminate);
Material material12 = new MatteMaterial(new ConstantTexture(new Color3F(20.0F)), textureLaminate);
Material material13 = new MatteMaterial(new ConstantTexture(new Color3F(20.0F)), textureLaminate);
Material material14 = new MatteMaterial(new ConstantTexture(new Color3F(20.0F)), textureLaminate);
Material material15 = new MatteMaterial(new ConstantTexture(new Color3F(20.0F)), textureLaminate);
Material material16 = new MatteMaterial(new ConstantTexture(new Color3F(20.0F)), textureLaminate);
Material material21 = new PlasticMaterial(new ConstantTexture(new Color3F(0.2F, 0.2F, 1.0F)), new ConstantTexture(new Color3F(0.1F)), new ConstantTexture(new Color3F(0.25F)), true);
Material material22 = new MetalMaterial(new ConstantTexture(Color3F.GOLD_ETA_MAXIMUM_TO_1), new ConstantTexture(Color3F.GOLD_K_MAXIMUM_TO_1), new ConstantTexture(new Color3F(0.05F)), new ConstantTexture(new Color3F(0.05F)), true);
Material material23 = new SubstrateMaterial(new ConstantTexture(new Color3F(1.0F, 0.2F, 0.2F)), new ConstantTexture(new Color3F(0.1F)), new ConstantTexture(new Color3F(0.1F)), new ConstantTexture(new Color3F(0.5F)), true);
Material material24 = new GlassMaterial(new ConstantTexture(new Color3F(1.5F)), new ConstantTexture(new Color3F(1.0F, 1.0F, 0.5F)), new ConstantTexture(new Color3F(1.0F, 1.0F, 0.5F)), new ConstantTexture(), new ConstantTexture(), false);
Material material25 = new MatteMaterial(new ConstantTexture(new Color3F(90.0F)), new ConstantTexture(new Color3F(0.2F, 1.0F, 0.2F)));
Material material26 = new MirrorMaterial();
Material material27 = new UberMaterial();
Material material31 = new MatteMaterial(new ConstantTexture(new Color3F(0.0F)), new ConstantTexture(Color3F.WHITE));

Shape3F shape11 = new Plane3F(new Point3F(0.0F, 0.0F, 0.0F), new Point3F(0.0F, +0.0F, +1.0F), new Point3F(1.0F, 0.0F, +0.0F));//Bottom
Shape3F shape12 = new Plane3F(new Point3F(0.0F, 0.0F, 0.0F), new Point3F(0.0F, +0.0F, -1.0F), new Point3F(1.0F, 0.0F, +0.0F));//Top
Shape3F shape13 = new Plane3F(new Point3F(0.0F, 0.0F, 0.0F), new Point3F(0.0F, +1.0F, +0.0F), new Point3F(1.0F, 0.0F, +0.0F));//Front
Shape3F shape14 = new Plane3F(new Point3F(0.0F, 0.0F, 0.0F), new Point3F(0.0F, -1.0F, +0.0F), new Point3F(1.0F, 0.0F, +0.0F));//Back
Shape3F shape15 = new Plane3F(new Point3F(0.0F, 0.0F, 0.0F), new Point3F(0.0F, +1.0F, +0.0F), new Point3F(0.0F, 0.0F, -1.0F));//Right
Shape3F shape16 = new Plane3F(new Point3F(0.0F, 0.0F, 0.0F), new Point3F(0.0F, +1.0F, +0.0F), new Point3F(0.0F, 0.0F,  1.0F));//Left
Shape3F shape21 = isTorus ? new Torus3F() : new Sphere3F();
Shape3F shape22 = isTorus ? new Torus3F() : new Sphere3F();
Shape3F shape23 = isTorus ? new Torus3F() : new Sphere3F();
Shape3F shape24 = isTorus ? new Torus3F() : new Sphere3F();
Shape3F shape25 = isTorus ? new Torus3F() : new Sphere3F();
Shape3F shape26 = isTorus ? new Torus3F() : new Sphere3F();
Shape3F shape27 = isTorus ? new Torus3F() : new Sphere3F();
Shape3F shape31 = new Sphere3F();

Transform transform11 = new Transform(new Point3F(+0.0F, 0.00F, + 0.0F));
Transform transform12 = new Transform(new Point3F(+0.0F, 8.00F, + 0.0F));
Transform transform13 = new Transform(new Point3F(+0.0F, 0.00F, + 7.5F));
Transform transform14 = new Transform(new Point3F(+0.0F, 0.00F, -10.0F));
Transform transform15 = new Transform(new Point3F(+5.0F, 0.00F, + 0.0F));
Transform transform16 = new Transform(new Point3F(-5.0F, 0.00F, + 0.0F));
Transform transform21 = new Transform(new Point3F(-3.0F, 1.25F, + 5.0F));
Transform transform22 = new Transform(new Point3F(+0.0F, 1.25F, + 5.0F));
Transform transform23 = new Transform(new Point3F(+3.0F, 1.25F, + 5.0F));
Transform transform24 = new Transform(new Point3F(-3.0F, 3.75F, + 5.0F));
Transform transform25 = new Transform(new Point3F(+0.0F, 3.75F, + 5.0F));
Transform transform26 = new Transform(new Point3F(+3.0F, 3.75F, + 5.0F));
Transform transform27 = new Transform(new Point3F(+0.0F, 6.25F, + 5.0F));
Transform transform31 = new Transform(new Point3F(+0.0F, 0.00F, + 0.0F));

AreaLight areaLight31 = new DiffuseAreaLight(transform31.getObjectToWorld(), 1, new Color3F(20.0F), shape31, true);

if(isInBox) {
	scene.addLight(areaLight31);
	scene.addPrimitive(new Primitive(material11, shape11, transform11));
	scene.addPrimitive(new Primitive(material12, shape12, transform12));
	scene.addPrimitive(new Primitive(material13, shape13, transform13));
	scene.addPrimitive(new Primitive(material14, shape14, transform14));
	scene.addPrimitive(new Primitive(material15, shape15, transform15));
	scene.addPrimitive(new Primitive(material16, shape16, transform16));
	scene.addPrimitive(new Primitive(material21, shape21, transform21));
	scene.addPrimitive(new Primitive(material22, shape22, transform22));
	scene.addPrimitive(new Primitive(material23, shape23, transform23));
	scene.addPrimitive(new Primitive(material24, shape24, transform24));
	scene.addPrimitive(new Primitive(material25, shape25, transform25));
	scene.addPrimitive(new Primitive(material26, shape26, transform26));
	scene.addPrimitive(new Primitive(material27, shape27, transform27));
	scene.addPrimitive(new Primitive(material31, shape31, transform31, areaLight31));
	scene.setCamera(new Camera(new Point3F(0.0F, 4.0F, -10.0F), AngleF.degrees(40.0F)));
	scene.setName("PBRTShowcaseMaterial");
} else {
	scene.addLight(new PerezLight());
	scene.addPrimitive(new Primitive(material11, shape11, transform11));
	scene.addPrimitive(new Primitive(material21, shape21, transform21));
	scene.addPrimitive(new Primitive(material22, shape22, transform22));
	scene.addPrimitive(new Primitive(material23, shape23, transform23));
	scene.addPrimitive(new Primitive(material24, shape24, transform24));
	scene.addPrimitive(new Primitive(material25, shape25, transform25));
	scene.addPrimitive(new Primitive(material26, shape26, transform26));
	scene.addPrimitive(new Primitive(material27, shape27, transform27));
	scene.setCamera(new Camera(new Point3F(0.0F, 4.0F, -10.0F), AngleF.degrees(40.0F)));
	scene.setName("PBRTShowcaseMaterial");
}