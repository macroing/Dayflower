Shape3F shapeA = new RectangularCuboid3F(new Point3F(-1.0F, -1.0F, -1.0F), new Point3F(1.0F, 1.0F, 1.0F));
Shape3F shapeB = new Sphere3F(0.5F, new Point3F(0.0F, 0.0F, -1.0F));
//Shape3F shapeB = new Sphere3F(1.0F, new Point3F(1.0F, 1.0F, -1.0F));
Shape3F shape0 = new Plane3F();
Shape3F shape1 = new ConstructiveSolidGeometry3F(Operation.DIFFERENCE, shapeA, shapeB);

Material material0 = new MatteMaterial(new ConstantTexture(new Color3F(0.0F)), new ConstantTexture(Color3F.GRAY));
Material material1 = new PlasticMaterial(FunctionTexture.createShapeIdentity(shapeA, new ConstantTexture(new Color3F(1.0F, 0.1F, 0.1F)), shapeB, new ConstantTexture(new Color3F(0.1F, 1.0F, 0.1F))), new ConstantTexture(new Color3F(0.01F)), new ConstantTexture(Color3F.WHITE), true);

Matrix44F matrix0 = Matrix44F.translate(0.0F, 0.0F, 7.5F);
Matrix44F matrix1 = Matrix44F.multiply(Matrix44F.multiply(Matrix44F.translate(0.0F, 2.0F, 2.0F), Matrix44F.rotateX(AngleF.degrees(0.0F))), Matrix44F.rotateY(AngleF.degrees(60.0F)));

scene.addLight(new PerezLight());
scene.addPrimitive(new Primitive(material0, shape0, new ConstantTexture(), new ConstantTexture(), matrix0));
scene.addPrimitive(new Primitive(material1, shape1, new ConstantTexture(), new ConstantTexture(), matrix1));
scene.setCamera(new Camera(new Point3F(0.0F, 4.0F, -10.0F), AngleF.degrees(40.0F)));
scene.setName("PBRTShowcaseShape3FConstructiveSolidGeometry3F");