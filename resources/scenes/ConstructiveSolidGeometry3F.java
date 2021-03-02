Shape3F shapeL = new RectangularCuboid3F(new Point3F(-1.0F, -1.0F, -1.0F), new Point3F(1.0F, 1.0F, 1.0F));
Shape3F shapeR = new Sphere3F(0.5F, new Point3F(0.0F, 0.0F, -1.0F));

Material material1 = new MatteMaterial();
Material material2 = FunctionMaterial.createShapeIdentity(shapeL, new MetalMaterial(), shapeR, new PlasticMaterial(new CheckerboardTexture(Color3F.GRAY_0_50, Color3F.WHITE, AngleF.degrees(0.0F), new Vector2F(4.0F, 4.0F))));

Shape3F shape1 = new Plane3F();
Shape3F shape2 = new ConstructiveSolidGeometry3F(Operation.DIFFERENCE, shapeL, shapeR);

Transform transform1 = new Transform(new Point3F(0.0F, 0.0F, 0.0F));
Transform transform2 = new Transform(new Point3F(0.0F, 1.0F, 0.0F), Quaternion4F.from(Matrix44F.multiply(Matrix44F.rotateX(AngleF.degrees(0.0F)), Matrix44F.rotateY(AngleF.degrees(60.0F)))));

scene.addLight(new PerezLight());
scene.addPrimitive(new Primitive(material1, shape1, transform1));
scene.addPrimitive(new Primitive(material2, shape2, transform2));
scene.setCamera(new Camera(new Point3F(0.0F, 2.0F, -10.0F), AngleF.degrees(40.0F)));
scene.setName("ConstructiveSolidGeometry3F");