Material material1 = new MattePBRTMaterial();

Shape3F shape1 = new Sphere3F();

Transform transform1 = new Transform(new Point3F(0.0F, 0.0F, 0.0F));

scene.addPrimitive(new Primitive(material1, shape1, transform1));
scene.setCamera(new Camera(new Point3F(0.0F, 0.0F, -5.0F), AngleF.degrees(40.0F)));
scene.setName("GPUTest");