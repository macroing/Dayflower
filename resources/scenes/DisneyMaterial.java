Material material1 = new MatteMaterial(new CheckerboardTexture());
Material material2 = new DisneyMaterial(new Color3F(255, 127, 80), Color3F.BLACK, Color3F.BLACK, 0.4F, 1.75F, 1.0F, 1.0F, 1.2F, 0.0F, 0.39F, 0.475F, 0.1F, 0.5F, 0.5F, 0.0F);

Shape3F shape1 = new Plane3F();
Shape3F shape2 = new Sphere3F();

Transform transform1 = new Transform(new Point3F(0.0F, 0.0F, 0.0F));
Transform transform2 = new Transform(new Point3F(0.0F, 1.0F, 0.0F));

scene.addLight(new PerezLight());
scene.addPrimitive(new Primitive(material1, shape1, transform1));
scene.addPrimitive(new Primitive(material2, shape2, transform2));
scene.setCamera(new Camera(new Point3F(0.0F, 2.0F, -10.0F), AngleF.degrees(40.0F)));
scene.setName("DisneyMaterial");