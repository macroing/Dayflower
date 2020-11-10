Material material = new LambertianMaterial();

Shape3F shape = TriangleMesh3F.readWavefrontObject("./resources/models/smoothMonkey2.obj", true, 100.0F).get(0);

Texture texture1 = new ConstantTexture(Color3F.GRAY);
Texture texture2 = new ConstantTexture();

Matrix44F matrix = Matrix44F.multiply(Matrix44F.multiply(Matrix44F.translate(0.0F, 1.0F, 5.0F), Matrix44F.rotateY(AngleF.degrees(180.0F))), Matrix44F.scale(0.01F));

scene.addLight(new PerezLight());
scene.addPrimitive(new Primitive(material, shape, texture1, texture2, matrix));
scene.setCamera(new Camera(new Point3F(0.0F, 2.0F, 0.0F)));
scene.setName("RayitoShowcaseShape3FTriangleMesh3F");