Material material01 = new LambertianMaterial();
Material material02 = new LambertianMaterial();
Material material03 = new LambertianMaterial();
Material material04 = new LambertianMaterial();
Material material05 = new LambertianMaterial();
Material material06 = new LambertianMaterial();
Material material07 = new ReflectionMaterial();
Material material08 = new RefractionMaterial();
Material material09 = new LambertianMaterial();

Shape3F shape01 = new Plane3F(new Point3F(0.0F, 0.0F, 0.0F), new Point3F(0.0F,  0.0F,  1.0F), new Point3F(1.0F, 0.0F,  0.0F));//Bottom
Shape3F shape02 = new Plane3F(new Point3F(0.0F, 0.0F, 0.0F), new Point3F(0.0F,  0.0F, -1.0F), new Point3F(1.0F, 0.0F,  0.0F));//Top
Shape3F shape03 = new Plane3F(new Point3F(0.0F, 0.0F, 0.0F), new Point3F(0.0F, -1.0F,  0.0F), new Point3F(1.0F, 0.0F,  0.0F));//Back
Shape3F shape04 = new Plane3F(new Point3F(0.0F, 0.0F, 0.0F), new Point3F(0.0F,  1.0F,  0.0F), new Point3F(1.0F, 0.0F,  0.0F));//Front
Shape3F shape05 = new Plane3F(new Point3F(0.0F, 0.0F, 0.0F), new Point3F(0.0F,  1.0F,  0.0F), new Point3F(0.0F, 0.0F, -1.0F));//Right
Shape3F shape06 = new Plane3F(new Point3F(0.0F, 0.0F, 0.0F), new Point3F(0.0F,  1.0F,  0.0F), new Point3F(0.0F, 0.0F,  1.0F));//Left
Shape3F shape07 = new Sphere3F(1.0F, new Point3F(0.0F, 0.0F, 0.0F));
Shape3F shape08 = new Sphere3F(1.0F, new Point3F(0.0F, 0.0F, 0.0F));
Shape3F shape09 = new Triangle3F();

Texture texture011 = new ConstantTexture(Color3F.GRAY);
Texture texture012 = new ConstantTexture();
Texture texture021 = new ConstantTexture(Color3F.GRAY);
Texture texture022 = new ConstantTexture();
Texture texture031 = new ConstantTexture(Color3F.GRAY);
Texture texture032 = new ConstantTexture();
Texture texture041 = new ConstantTexture(Color3F.GRAY);
Texture texture042 = new ConstantTexture();
Texture texture051 = new ConstantTexture(new Color3F(0.25F, 0.25F, 0.75F));
Texture texture052 = new ConstantTexture();
Texture texture061 = new ConstantTexture(new Color3F(0.75F, 0.25F, 0.25F));
Texture texture062 = new ConstantTexture();
Texture texture071 = new ConstantTexture(Color3F.WHITE);
Texture texture072 = new ConstantTexture();
Texture texture081 = new ConstantTexture(Color3F.WHITE);
Texture texture082 = new ConstantTexture();
Texture texture091 = new ConstantTexture(Color3F.WHITE);
Texture texture092 = new ConstantTexture(new Color3F(12.0F));

Matrix44F matrix01 = Matrix44F.translate( 0.0F, 0.0F,  0.0F);
Matrix44F matrix02 = Matrix44F.translate( 0.0F, 5.0F,  0.0F);
Matrix44F matrix03 = Matrix44F.translate( 0.0F, 0.0F,  0.0F);
Matrix44F matrix04 = Matrix44F.translate( 0.0F, 0.0F, 10.0F);
Matrix44F matrix05 = Matrix44F.translate( 3.0F, 0.0F,  0.0F);
Matrix44F matrix06 = Matrix44F.translate(-3.0F, 0.0F,  0.0F);
Matrix44F matrix07 = Matrix44F.translate(-1.5F, 1.0F,  8.0F);
Matrix44F matrix08 = Matrix44F.translate( 1.5F, 1.0F,  7.0F);
Matrix44F matrix09 = Matrix44F.multiply(Matrix44F.multiply(Matrix44F.translate(0.0F, 4.99F, 9.0F), Matrix44F.rotateX(AngleF.degrees(270.0F))), Matrix44F.scale(0.25F));

Primitive primitive01 = new Primitive(material01, shape01, texture011, texture012, matrix01);
Primitive primitive02 = new Primitive(material02, shape02, texture021, texture022, matrix02);
Primitive primitive03 = new Primitive(material03, shape03, texture031, texture032, matrix03);
Primitive primitive04 = new Primitive(material04, shape04, texture041, texture042, matrix04);
Primitive primitive05 = new Primitive(material05, shape05, texture051, texture052, matrix05);
Primitive primitive06 = new Primitive(material06, shape06, texture061, texture062, matrix06);
Primitive primitive07 = new Primitive(material07, shape07, texture071, texture072, matrix07);
Primitive primitive08 = new Primitive(material08, shape08, texture081, texture082, matrix08);
Primitive primitive09 = new Primitive(material09, shape09, texture091, texture092, matrix09);

Camera camera = new Camera(new Point3F(0.0F, 2.5F, 1.0F));
camera.setResolution(1024.0F, 768.0F);
camera.setFieldOfViewY(AngleF.degrees(58.0F));
camera.setFieldOfViewX();

scene.addLight(new PrimitiveLight(primitive09));
scene.addPrimitive(primitive01);
scene.addPrimitive(primitive02);
scene.addPrimitive(primitive03);
scene.addPrimitive(primitive04);
scene.addPrimitive(primitive05);
scene.addPrimitive(primitive06);
scene.addPrimitive(primitive07);
scene.addPrimitive(primitive08);
scene.addPrimitive(primitive09);
scene.setCamera(camera);
scene.setName("RayitoCornellBox");