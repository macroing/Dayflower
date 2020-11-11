Material material = new PlasticMaterial(new ConstantTexture(new Color3F(0.2F, 0.2F, 1.0F)), new ConstantTexture(new Color3F(0.01F)), new ConstantTexture(Color3F.WHITE), true);

Shape3F shape = new Sphere3F(10.0F);

Texture texture1 = new ConstantTexture();
Texture texture2 = new ConstantTexture();

Matrix44F matrix = Matrix44F.translate(0.0F, 2.0F, 20.0F);

scene.addLight(new SpotLight(AngleF.degrees(50.0F), AngleF.degrees(10.0F), new Color3F(100.0F), Matrix44F.translate(0.0F, 2.0F, 0.0F), new Point3F(0.0F, 2.0F, 0.0F), new Point3F(0.0F, 2.0F, 20.0F)));
scene.addPrimitive(new Primitive(material, shape, texture1, texture2, matrix));
scene.setCamera(new Camera(new Point3F(0.0F, 2.0F, 0.0F)));
scene.setName("PBRTShowcaseLightSpotLight");