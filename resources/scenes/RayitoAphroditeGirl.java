List<TriangleMesh3F> triangleMeshes = TriangleMesh3F.readWavefrontObject("./resources/models/aphroditegirl.obj", true, 100.0F);

Material material1 = new LambertianMaterial(new CheckerboardTexture(new Color3F(0.1F), new Color3F(1.0F), AngleF.degrees(90.0F), new Vector2F(0.5F, 0.5F)));
Material material2 = new LambertianMaterial(new Color3F(227, 161, 115));
Material material3 = new LambertianMaterial(new Color3F(227, 161, 115));
Material material4 = new LambertianMaterial(new Color3F(0.8F, 0.1F, 0.8F));
Material material5 = new LambertianMaterial(new Color3F(216, 192, 120));
Material material6 = new LambertianMaterial(new Color3F(0.8F, 0.1F, 0.8F));
Material material7 = new LambertianMaterial(new Color3F(227, 161, 115));
Material material8 = new LambertianMaterial(new Color3F(1.0F, 1.0F, 1.0F));
Material material9 = new LambertianMaterial(new Color3F(227, 161, 115));

Shape3F shape1 = new Plane3F();
Shape3F shape2 = triangleMeshes.get(0);
Shape3F shape3 = triangleMeshes.get(1);
Shape3F shape4 = triangleMeshes.get(2);
Shape3F shape5 = triangleMeshes.get(3);
Shape3F shape6 = triangleMeshes.get(4);
Shape3F shape7 = triangleMeshes.get(5);
Shape3F shape8 = triangleMeshes.get(6);
Shape3F shape9 = triangleMeshes.get(7);

Transform transform1 = new Transform();
Transform transform2 = new Transform(new Point3F(0.0F, 1.0F, 5.0F), Quaternion4F.from(Matrix44F.rotateY(AngleF.degrees(0.0F))), new Vector3F(0.01F));

scene.addLight(new PerezLight());
scene.addLight(new PointLight(new Point3F(0.0F, 2.0F, 5.0F), new Color3F(1.0F, 1.0F, 1.0F)));
scene.addLight(new PointLight(new Point3F(0.0F, 1.0F, 0.0F), new Color3F(1.0F, 1.0F, 1.0F)));
scene.addPrimitive(new Primitive(material1, shape1, transform1));
scene.addPrimitive(new Primitive(material2, shape2, transform2));
scene.addPrimitive(new Primitive(material3, shape3, transform2));
scene.addPrimitive(new Primitive(material4, shape4, transform2));
scene.addPrimitive(new Primitive(material5, shape5, transform2));
scene.addPrimitive(new Primitive(material6, shape6, transform2));
scene.addPrimitive(new Primitive(material7, shape7, transform2));
scene.addPrimitive(new Primitive(material8, shape8, transform2));
scene.addPrimitive(new Primitive(material9, shape9, transform2));
scene.setCamera(new Camera(new Point3F(0.0F, 2.0F, 0.0F)));
scene.setName("RayitoAphroditeGirl");