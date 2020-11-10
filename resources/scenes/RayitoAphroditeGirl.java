List<TriangleMesh3F> triangleMeshes = TriangleMesh3F.readWavefrontObject("./resources/models/aphroditegirl.obj", true, 100.0F);

Material material1 = new LambertianMaterial();
Material material2 = new LambertianMaterial();
Material material3 = new LambertianMaterial();
Material material4 = new LambertianMaterial();
Material material5 = new LambertianMaterial();
Material material6 = new LambertianMaterial();
Material material7 = new LambertianMaterial();
Material material8 = new LambertianMaterial();
Material material9 = new LambertianMaterial();

Shape3F shape1 = new Plane3F();
Shape3F shape2 = triangleMeshes.get(0);
Shape3F shape3 = triangleMeshes.get(1);
Shape3F shape4 = triangleMeshes.get(2);
Shape3F shape5 = triangleMeshes.get(3);
Shape3F shape6 = triangleMeshes.get(4);
Shape3F shape7 = triangleMeshes.get(5);
Shape3F shape8 = triangleMeshes.get(6);
Shape3F shape9 = triangleMeshes.get(7);

Texture texture11 = new CheckerboardTexture(new Color3F(0.1F), new Color3F(1.0F), AngleF.degrees(90.0F), new Vector2F(0.5F, 0.5F));
Texture texture12 = new ConstantTexture();
Texture texture21 = new ConstantTexture(new Color3F(227, 161, 115));
Texture texture22 = new ConstantTexture();
Texture texture31 = new ConstantTexture(new Color3F(227, 161, 115));
Texture texture32 = new ConstantTexture();
Texture texture41 = new ConstantTexture(new Color3F(0.8F, 0.1F, 0.8F));
Texture texture42 = new ConstantTexture();
Texture texture51 = new ConstantTexture(new Color3F(216, 192, 120));
Texture texture52 = new ConstantTexture();
Texture texture61 = new ConstantTexture(new Color3F(0.8F, 0.1F, 0.8F));
Texture texture62 = new ConstantTexture();
Texture texture71 = new ConstantTexture(new Color3F(227, 161, 115));
Texture texture72 = new ConstantTexture();
Texture texture81 = new ConstantTexture(new Color3F(1.0F, 1.0F, 1.0F));
Texture texture82 = new ConstantTexture();
Texture texture91 = new ConstantTexture(new Color3F(227, 161, 115));
Texture texture92 = new ConstantTexture();

Matrix44F matrix1 = Matrix44F.identity();
Matrix44F matrix2 = Matrix44F.multiply(Matrix44F.multiply(Matrix44F.translate(0.0F, 1.0F, 2.0F), Matrix44F.rotateY(AngleF.degrees(0.0F))), Matrix44F.scale(0.01F));

scene.addLight(new PerezLight());
scene.addLight(new PointLight(new Point3F(0.0F, 2.0F, 5.0F), new Color3F(1.0F, 1.0F, 1.0F)));
scene.addLight(new PointLight(new Point3F(0.0F, 1.0F, 0.0F), new Color3F(1.0F, 1.0F, 1.0F)));
scene.addPrimitive(new Primitive(material1, shape1, texture11, texture12, matrix1));
scene.addPrimitive(new Primitive(material2, shape2, texture21, texture22, matrix2));
scene.addPrimitive(new Primitive(material3, shape3, texture31, texture32, matrix2));
scene.addPrimitive(new Primitive(material4, shape4, texture41, texture42, matrix2));
scene.addPrimitive(new Primitive(material5, shape5, texture51, texture52, matrix2));
scene.addPrimitive(new Primitive(material6, shape6, texture61, texture62, matrix2));
scene.addPrimitive(new Primitive(material7, shape7, texture71, texture72, matrix2));
scene.addPrimitive(new Primitive(material8, shape8, texture81, texture82, matrix2));
scene.addPrimitive(new Primitive(material9, shape9, texture91, texture92, matrix2));
scene.setCamera(new Camera(new Point3F(0.0F, 2.0F, 0.0F)));
scene.setName("RayitoAphroditeGirl");