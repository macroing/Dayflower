Material material1 = new MatteMaterial(new ConstantTexture(new Color3F(20.0F)), new SimplexFractionalBrownianMotionTexture());
Material material2 = new GlassMaterial(new ConstantTexture(new Color3F(1.5F)), new ConstantTexture(new Color3F(1.0F, 0.5F, 0.5F)), new ConstantTexture(new Color3F(1.0F, 0.5F, 0.5F)), new ConstantTexture(), new ConstantTexture(), false);

Shape3F shape1 = new Plane3F(new Point3F(0.0F, 0.0F, 0.0F), new Point3F(0.0F, 0.0F, 1.0F), new Point3F(1.0F, 0.0F, 0.0F));
Shape3F shape2 = new Sphere3F(3.0F);

Transform transform1 = new Transform(new Point3F(0.0F, 0.0F, 0.0F));
Transform transform2 = new Transform(new Point3F(0.0F, 3.5F, 3.0F));

scene.addLight(new PerezLight());
scene.addPrimitive(new Primitive(material1, shape1, new ConstantTexture(), new ConstantTexture(), transform1));
scene.addPrimitive(new Primitive(material2, shape2, new ConstantTexture(), new ConstantTexture(), transform2));
scene.setCamera(new Camera(new Point3F(0.0F, 4.0F, -10.0F), AngleF.degrees(40.0F)));
scene.setName("PBRTShowcaseMaterialGlassMaterial");