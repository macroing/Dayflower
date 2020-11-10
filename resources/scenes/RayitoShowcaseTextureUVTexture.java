Material material1 = new LambertianMaterial();
Material material2 = new LambertianMaterial();

Shape3F shape1 = new Plane3F();
Shape3F shape2 = new Sphere3F(10.0F);

Texture texture11 = new ConstantTexture(Color3F.GRAY);
Texture texture12 = new ConstantTexture();
Texture texture21 = new UVTexture();
Texture texture22 = new ConstantTexture();

Matrix44F matrix1 = Matrix44F.identity();
Matrix44F matrix2 = Matrix44F.translate(0.0F, 2.0F, 20.0F);

scene.addLight(new PerezLight());
scene.addPrimitive(new Primitive(material1, shape1, texture11, texture12, matrix1));
scene.addPrimitive(new Primitive(material2, shape2, texture21, texture22, matrix2));
scene.setCamera(new Camera(new Point3F(0.0F, 2.0F, 0.0F)));
scene.setName("RayitoShowcaseTextureUVTexture");