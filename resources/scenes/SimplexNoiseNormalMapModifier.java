Material material1 = new MatteMaterial(Color3F.GRAY, Color3F.BLACK, 0.0F, new SimplexNoiseNormalMapModifier(4.0F, 1.0F));
Material material2 = new GlassMaterial(Color3F.WHITE, Color3F.WHITE, Color3F.BLACK, 1.5F, 0.0F, 0.0F, true, new SimplexNoiseNormalMapModifier(16.0F, 1.0F));

Shape3F shape1 = new Plane3F();
Shape3F shape2 = new Sphere3F();

Transform transform1 = new Transform(new Point3F(0.0F, 0.0F, 0.0F), Quaternion4F.from(Matrix44F.rotateX(AngleF.degrees(90.0F))));
Transform transform2 = new Transform(new Point3F(0.0F, 1.0F, 0.0F), Quaternion4F.from(Matrix44F.rotateX(AngleF.degrees(270.0F))));

scene.addLight(new PerezLight());
scene.addPrimitive(new Primitive(material1, shape1, transform1));
scene.addPrimitive(new Primitive(material2, shape2, transform2));
scene.setCamera(new Camera(new Point3F(0.0F, 2.0F, -10.0F), AngleF.degrees(40.0F)));
scene.setName("SimplexNoiseNormalMapModifier");