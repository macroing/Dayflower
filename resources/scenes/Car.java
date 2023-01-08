List<TriangleMesh3F> triangleMeshes = TriangleMesh3F.readWavefrontObject(new File(directory, "../models/car.obj"), true, 1.0F, true);

Texture textureCar   = LDRImageTexture.undoGammaCorrectionSRGB(LDRImageTexture.load(new File(directory, "../textures/car.png")));
Texture textureWheel = LDRImageTexture.undoGammaCorrectionSRGB(LDRImageTexture.load(new File(directory, "../textures/wheel.png")));

Material material1 = new CheckerboardMaterial();
Material material2 = new ClearCoatMaterial(textureCar);
Material material3 = new MatteMaterial(textureWheel);
Material material4 = new MatteMaterial(textureWheel);
Material material5 = new MatteMaterial(textureWheel);
Material material6 = new MatteMaterial(textureWheel);

Shape3F shape1 = new Plane3F();
Shape3F shape2 = triangleMeshes.get(0);
Shape3F shape3 = triangleMeshes.get(1);
Shape3F shape4 = triangleMeshes.get(2);
Shape3F shape5 = triangleMeshes.get(3);
Shape3F shape6 = triangleMeshes.get(4);

Transform transform1 = new Transform(new Point3F(0.0F, 0.00F, 0.0F), Quaternion4F.from(Matrix44F.rotateX(AngleF.degrees(90.0F))));
Transform transform2 = new Transform(new Point3F(0.0F, 0.25F, 3.0F), Quaternion4F.from(Matrix44F.rotateY(AngleF.degrees(90.0F))));
Transform transform3 = new Transform(new Point3F(0.0F, 0.25F, 3.0F), Quaternion4F.from(Matrix44F.rotateY(AngleF.degrees(90.0F))));
Transform transform4 = new Transform(new Point3F(0.0F, 0.25F, 3.0F), Quaternion4F.from(Matrix44F.rotateY(AngleF.degrees(90.0F))));
Transform transform5 = new Transform(new Point3F(0.0F, 0.25F, 3.0F), Quaternion4F.from(Matrix44F.rotateY(AngleF.degrees(90.0F))));
Transform transform6 = new Transform(new Point3F(0.0F, 0.25F, 3.0F), Quaternion4F.from(Matrix44F.rotateY(AngleF.degrees(90.0F))));

final
Camera camera = new Camera(new Point3F(0.0F, 2.0F, -10.0F));
camera.setResolution(2560.0F / 3.0F, 1440.0F / 3.0F);
camera.setFieldOfViewX(AngleF.degrees(40.0F));
camera.setFieldOfViewY();

scene.addLight(new PerezLight());
scene.addPrimitive(new Primitive(material1, shape1, transform1));
scene.addPrimitive(new Primitive(material2, shape2, transform2));
scene.addPrimitive(new Primitive(material3, shape3, transform3));
scene.addPrimitive(new Primitive(material4, shape4, transform4));
scene.addPrimitive(new Primitive(material5, shape5, transform5));
scene.addPrimitive(new Primitive(material6, shape6, transform6));
scene.setCamera(camera);
scene.setName("Car");
scene.buildAccelerationStructure();