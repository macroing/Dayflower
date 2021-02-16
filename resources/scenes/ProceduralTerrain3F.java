Material material1 = new MatteMaterial(new CheckerboardTexture());

Shape3F shape1 = ProceduralTerrain3F.sin();

Transform transform1 = new Transform(new Point3F(0.0F, 0.0F, 0.0F));

scene.addLight(new PerezLight());
scene.addPrimitive(new Primitive(material1, shape1, transform1));
scene.setCamera(new Camera(new Point3F(10.0F, 2.0F, 10.0F), AngleF.degrees(40.0F)));
scene.setName("ProceduralTerrain3F");