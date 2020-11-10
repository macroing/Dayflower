Image image = new Image(800, 800, Color3F.WHITE);
image.fillRectangle(new Rectangle2I(new Point2I(300, 300), new Point2I(500, 500)), pixel -> Color3F.simplexFractionalBrownianMotion(new Color3F(0.75F, 0.5F, 0.75F), new Point2F(pixel.getX(), pixel.getY()), new Point2F(), new Point2F(800.0F, 800.0F)));

Material material1 = new LambertianMaterial();
Material material2 = new LambertianMaterial();

Shape3F shape1 = new Plane3F();
Shape3F shape2 = new Sphere3F(10.0F);

Texture texture11 = new ConstantTexture(Color3F.GRAY);
Texture texture12 = new ConstantTexture();
Texture texture21 = new MarbleTexture();
Texture texture22 = new ConstantTexture();

Matrix44F matrix1 = Matrix44F.identity();
Matrix44F matrix2 = Matrix44F.multiply(Matrix44F.translate(0.0F, 2.0F, 20.0F), Matrix44F.rotateY(AngleF.degrees(90.0F)));

scene.addLight(new PerezLight());
scene.addPrimitive(new Primitive(material1, shape1, texture11, texture12, matrix1));
scene.addPrimitive(new Primitive(material2, shape2, texture21, texture22, matrix2));
scene.setCamera(new Camera(new Point3F(0.0F, 2.0F, 0.0F)));
scene.setName("RayitoShowcaseTextureMarbleTexture");