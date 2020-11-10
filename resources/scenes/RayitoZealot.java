Material material1 = new LambertianMaterial();
Material material2 = new AshikhminShirleyMaterial(0.02F);

Shape3F shape1 = new Plane3F();
Shape3F shape2 = TriangleMesh3F.readWavefrontObject("./resources/models/Zealot.obj", true).get(0);

Texture texture11 = new CheckerboardTexture(new Color3F(0.1F), new Color3F(1.0F), AngleF.degrees(90.0F), new Vector2F(0.5F, 0.5F));
Texture texture12 = new ConstantTexture();
Texture texture21 = ImageTexture.load("./resources/textures/Zealot_albedo.png");
Texture texture22 = ImageTexture.load("./resources/textures/Zealot_emissive.png");

Matrix44F matrix1 = Matrix44F.identity();
Matrix44F matrix2 = Matrix44F.multiply(Matrix44F.multiply(Matrix44F.translate(0.0F, 0.0F, 5.0F), Matrix44F.rotateY(AngleF.degrees(180.0F))), Matrix44F.scale(0.05F));

scene.addLight(new PerezLight());
scene.addPrimitive(new Primitive(material1, shape1, texture11, texture12, matrix1));
scene.addPrimitive(new Primitive(material2, shape2, texture21, texture22, matrix2));
scene.setCamera(new Camera(new Point3F(0.0F, 2.0F, 0.0F)));
scene.setName("RayitoZealot");