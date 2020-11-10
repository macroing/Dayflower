Material material1 = new LambertianMaterial();
Material material2 = new AshikhminShirleyMaterial(0.05F);
Material material3 = new AshikhminShirleyMaterial(0.05F);
Material material4 = new AshikhminShirleyMaterial(0.05F);
Material material5 = new RefractionMaterial();

Shape3F shape1 = new Plane3F();
Shape3F shape2 = new Sphere3F(7.5F);
Shape3F shape3 = new RectangularCuboid3F(new Point3F(-1.0F), new Point3F(1.0F));
Shape3F shape4 = TriangleMesh3F.readWavefrontObject("./resources/models/smoothMonkey2.obj", true, 100.0F).get(0);
Shape3F shape5 = new Torus3F();

Texture texture11 = new CheckerboardTexture(new Color3F(0.1F), new Color3F(1.0F), AngleF.degrees(90.0F), new Vector2F(0.5F, 0.5F));
Texture texture12 = new ConstantTexture();
Texture texture21 = new BullseyeTexture(new Color3F(1.0F, 0.1F, 0.1F), new Color3F(0.1F, 1.0F, 0.1F), new Point3F(0.0F, 10.0F, 0.0F), 0.5F);
Texture texture22 = new ConstantTexture();
Texture texture31 = new ConstantTexture(new Color3F(1.0F, 1.0F, 0.1F));
Texture texture32 = new ConstantTexture();
Texture texture41 = new ConstantTexture(new Color3F(0.5F));
Texture texture42 = new ConstantTexture();
Texture texture51 = new ConstantTexture(Color3F.WHITE);
Texture texture52 = new ConstantTexture();

Matrix44F matrix1 = Matrix44F.identity();
Matrix44F matrix2 = Matrix44F.translate(0.0F, 2.0F, 20.0F);
Matrix44F matrix3 = Matrix44F.translate(-3.0F, 1.0F, 5.0F);
Matrix44F matrix4 = Matrix44F.multiply(Matrix44F.multiply(Matrix44F.translate(0.0F, 1.0F, 5.0F), Matrix44F.rotateY(AngleF.degrees(180.0F))), Matrix44F.scale(0.01F));
Matrix44F matrix5 = Matrix44F.translate(3.0F, 1.25F, 5.0F);

scene.addLight(new PerezLight());
scene.addLight(new PointLight(new Point3F(0.0F, 2.0F, 5.0F), new Color3F(1.0F, 1.0F, 1.0F)));
scene.addLight(new PointLight(new Point3F(0.0F, 1.0F, 0.0F), new Color3F(1.0F, 1.0F, 1.0F)));
scene.addPrimitive(new Primitive(material1, shape1, texture11, texture12, matrix1));
scene.addPrimitive(new Primitive(material2, shape2, texture21, texture22, matrix2));
scene.addPrimitive(new Primitive(material3, shape3, texture31, texture32, matrix3));
scene.addPrimitive(new Primitive(material4, shape4, texture41, texture42, matrix4));
scene.addPrimitive(new Primitive(material5, shape5, texture51, texture52, matrix5));
scene.setCamera(new Camera(new Point3F(0.0F, 2.0F, -10.0F), AngleF.degrees(40.0F)));
scene.setName("RayitoDefault");