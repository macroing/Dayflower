List<Curve3F> curves = Curve3F.createCurvesByBSpline(Arrays.asList(new Point3F(0.0F, 0.0F, 0.0F), new Point3F(0.0F, 1.0F, 0.0F), new Point3F(0.0F, 2.0F, 0.0F), new Point3F(0.0F, 3.0F, 0.0F)), new ArrayList<>(), Type.FLAT, 5.0F, 5.0F, 3, 5);

Material material0 = new MatteMaterial(new ConstantTexture(new Color3F(0.0F)), new ConstantTexture(Color3F.GRAY));
Material material1 = new PlasticMaterial(new ConstantTexture(new Color3F(0.2F, 0.2F, 1.0F)), new ConstantTexture(new Color3F(0.01F)), new ConstantTexture(Color3F.WHITE), true);//new HairMaterial();
Material material2 = new MatteMaterial(new ConstantTexture(new Color3F(0.0F)), new ConstantTexture(Color3F.WHITE));

Shape3F shape0 = new Plane3F(new Point3F(0.0F, 0.0F, 0.0F), new Point3F(0.0F, +1.0F, +0.0F), new Point3F(1.0F, 0.0F, +0.0F));//F
Shape3F shape1 = new Curves3F(curves);
Shape3F shape2 = new Sphere3F();

Matrix44F matrix0 = Matrix44F.translate(0.0F, 0.0F, 7.5F);
Matrix44F matrix1 = Matrix44F.multiply(Matrix44F.translate(0.0F, 2.0F, 5.0F), Matrix44F.rotateZ(AngleF.degrees(45.0F)));
Matrix44F matrix2 = Matrix44F.translate(0.0F, 0.0F, 0.0F);

AreaLight areaLight2 = new DiffuseAreaLight(matrix2, 1, new Color3F(20.0F), shape2, false);

scene.addLight(areaLight2);
scene.addPrimitive(new Primitive(material0, shape0, new ConstantTexture(), new ConstantTexture(), matrix0));
scene.addPrimitive(new Primitive(material1, shape1, new ConstantTexture(), new ConstantTexture(), matrix1));
scene.addPrimitive(new Primitive(material2, shape2, new ConstantTexture(), new ConstantTexture(), matrix2, areaLight2));
scene.setCamera(new Camera(new Point3F(0.0F, 4.0F, -10.0F), AngleF.degrees(40.0F)));
scene.setName("PBRTShowcaseShape3FCurves3F");