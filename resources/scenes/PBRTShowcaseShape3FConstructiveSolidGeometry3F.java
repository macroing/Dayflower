Material material0 = new MatteMaterial(new ConstantTexture(new Color3F(0.0F)), new ConstantTexture(Color3F.GRAY));
Material material1 = new PlasticMaterial(new ConstantTexture(new Color3F(0.2F, 0.2F, 1.0F)), new ConstantTexture(new Color3F(0.01F)), new ConstantTexture(Color3F.WHITE), true);

Shape3F shape0 = new Plane3F();
Shape3F shape1 = new ConstructiveSolidGeometry3F(Operation.DIFFERENCE, new Sphere3F(1.0F, new Point3F(0.0F, 0.0F, 0.0F)), new Sphere3F(1.0F, new Point3F(0.75F, 0.0F, 0.0F)));

Matrix44F matrix0 = Matrix44F.translate(0.0F, 0.0F, 7.5F);
Matrix44F matrix1 = Matrix44F.translate(0.0F, 2.0F, 2.0F);

scene.addLight(new PerezLight());
scene.addPrimitive(new Primitive(material0, shape0, new ConstantTexture(), new ConstantTexture(), matrix0));
scene.addPrimitive(new Primitive(material1, shape1, new ConstantTexture(), new ConstantTexture(), matrix1));
scene.setCamera(new Camera(new Point3F(0.0F, 4.0F, -10.0F), AngleF.degrees(40.0F)));
scene.setName("PBRTShowcaseShape3FConstructiveSolidGeometry3F");