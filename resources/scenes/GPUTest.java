Material material1 = new MattePBRTMaterial();
Material material2 = new MattePBRTMaterial();

Shape3F shape1 = new Sphere3F();
Shape3F shape2 = new Torus3F();

Transform transform1 = new Transform(new Point3F(0.0F, 1.0F, 5.0F));
Transform transform2 = new Transform(new Point3F(2.0F, 2.0F, 5.0F));

scene.addPrimitive(new Primitive(material1, shape1, transform1));
scene.addPrimitive(new Primitive(material2, shape2, transform2));
scene.setCamera(new Camera(new Point3F(0.0F, 1.0F, -5.0F), AngleF.degrees(40.0F)));
scene.setName("GPUTest");