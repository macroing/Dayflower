Material material1 = new MatteMaterial(new CheckerboardTexture());
Material material2 = new SubsurfaceMaterial(50.0F, ConstantTexture.WHITE, ConstantTexture.WHITE, ConstantTexture.BLACK, new ConstantTexture(new Color3F(0.032F, 0.17F, 0.48F)), new ConstantTexture(new Color3F(0.74F, 0.88F, 1.01F)), 0.0F, 1.5F, ConstantTexture.BLACK, ConstantTexture.BLACK, false, new NoOpModifier());
Material material3 = new SubsurfaceMaterial(50.0F, ConstantTexture.WHITE, ConstantTexture.WHITE, ConstantTexture.BLACK, new ConstantTexture(new Color3F(0.032F, 0.17F, 0.48F)), new ConstantTexture(new Color3F(0.74F, 0.88F, 1.01F)), 0.0F, 1.5F, ConstantTexture.BLACK, ConstantTexture.BLACK, false, new NoOpModifier());
Material material4 = new SubsurfaceMaterial(50.0F, ConstantTexture.WHITE, ConstantTexture.WHITE, ConstantTexture.BLACK, new ConstantTexture(new Color3F(0.032F, 0.17F, 0.48F)), new ConstantTexture(new Color3F(0.74F, 0.88F, 1.01F)), 0.0F, 1.5F, ConstantTexture.BLACK, ConstantTexture.BLACK, false, new NoOpModifier());

Shape3F shape1 = new Plane3F();
Shape3F shape2 = new Torus3F();
Shape3F shape3 = new Cone3F();
Shape3F shape4 = new Sphere3F();

Transform transform1 = new Transform(new Point3F(0.0F, 0.0F, 0.0F), Quaternion4F.from(Matrix44F.rotateX(AngleF.degrees(90.0F))));
Transform transform2 = new Transform(new Point3F(0.0F, 0.75F, 0.0F), Quaternion4F.from(Matrix44F.rotateX(AngleF.degrees(90.0F))));
Transform transform3 = new Transform(new Point3F(0.0F, 0.5F, 0.0F), Quaternion4F.from(Matrix44F.rotateX(AngleF.degrees(270.0F))));
Transform transform4 = new Transform(new Point3F(0.0F, 0.5F, 0.0F), new Quaternion4F(), new Vector3F(0.5F, 0.5F, 0.5F));

scene.addLight(new PerezLight());
scene.addPrimitive(new Primitive(material1, shape1, transform1));
scene.addPrimitive(new Primitive(material2, shape2, transform2));
scene.addPrimitive(new Primitive(material3, shape3, transform3));
scene.addPrimitive(new Primitive(material4, shape4, transform4));
scene.setCamera(new Camera(new Point3F(0.0F, 2.0F, -10.0F), AngleF.degrees(40.0F)));
scene.setName("SubsurfaceMaterial");