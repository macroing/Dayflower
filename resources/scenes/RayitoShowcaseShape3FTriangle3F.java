Material material = new LambertianMaterial();

Point3F positionA = new Point3F(+0.0F, +5.0F, 0.0F);
Point3F positionB = new Point3F(+5.0F, -5.0F, 0.0F);
Point3F positionC = new Point3F(-5.0F, -5.0F, 0.0F);

Vector3F surfaceNormal = Vector3F.normalNormalized(positionA, positionB, positionC);

Vertex3F a = new Vertex3F(new Point2F(0.5F, 0.0F), positionA, surfaceNormal);
Vertex3F b = new Vertex3F(new Point2F(1.0F, 1.0F), positionB, surfaceNormal);
Vertex3F c = new Vertex3F(new Point2F(0.0F, 1.0F), positionC, surfaceNormal);

Shape3F shape = new Triangle3F(a, b, c);

Texture texture1 = new ConstantTexture(Color3F.GRAY);
Texture texture2 = new ConstantTexture();

Matrix44F matrix = Matrix44F.translate(0.0F, 2.0F, 20.0F);

scene.addLight(new PerezLight());
scene.addPrimitive(new Primitive(material, shape, texture1, texture2, matrix));
scene.setCamera(new Camera(new Point3F(0.0F, 2.0F, 0.0F)));
scene.setName("RayitoShowcaseShape3FTriangle3F");