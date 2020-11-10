Material material = new LambertianMaterial();

Shape3F shape = ProceduralTerrain3F.sin();

Texture texture1 = new ConstantTexture(Color3F.GRAY);
Texture texture2 = new ConstantTexture();

Matrix44F matrix = Matrix44F.scale(1.0F);

scene.addLight(new PerezLight());
scene.addPrimitive(new Primitive(material, shape, texture1, texture2, matrix));
scene.setCamera(new Camera(new Point3F(10.0F, 4.0F, 10.0F)));
scene.setName("RayitoShowcaseShape3FProceduralTerrain3F");