List<Curve3F> curves = Curve3F.createCurvesByBSpline(Arrays.asList(new Point3F(0.0F, 0.0F, 0.0F), new Point3F(0.0F, 1.0F, 0.0F), new Point3F(0.0F, 2.0F, 0.0F), new Point3F(0.0F, 3.0F, 0.0F)), new ArrayList<>(), Type.FLAT, 5.0F, 5.0F, 3, 5);

Material material0 = new MattePBRTMaterial(new ConstantTexture(new Color3F(0.0F)), new ConstantTexture(Color3F.GRAY));
Material material1 = new PlasticPBRTMaterial(new ConstantTexture(new Color3F(0.2F, 0.2F, 1.0F)), new ConstantTexture(new Color3F(0.01F)), new ConstantTexture(Color3F.WHITE), true);//new HairMaterial();
Material material2 = new MattePBRTMaterial(new ConstantTexture(new Color3F(0.0F)), new ConstantTexture(Color3F.WHITE));

Shape3F shape0 = new Plane3F(new Point3F(0.0F, 0.0F, 0.0F), new Point3F(0.0F, +1.0F, +0.0F), new Point3F(1.0F, 0.0F, +0.0F));//F
Shape3F shape1 = new Curves3F(curves);
Shape3F shape2 = new Sphere3F();

Transform transform0 = new Transform(new Point3F(0.0F, 0.0F, 7.5F));
Transform transform1 = new Transform(new Point3F(0.0F, 2.0F, 5.0F), Quaternion4F.from(Matrix44F.rotateZ(AngleF.degrees(45.0F))));
Transform transform2 = new Transform(new Point3F(0.0F, 0.0F, 0.0F));

AreaLight areaLight2 = new DiffuseAreaLight(transform2.getObjectToWorld(), 1, new Color3F(20.0F), shape2, false);

scene.addLight(areaLight2);
scene.addPrimitive(new Primitive(material0, shape0, transform0));
scene.addPrimitive(new Primitive(material1, shape1, transform1));
scene.addPrimitive(new Primitive(material2, shape2, transform2, areaLight2));
scene.setCamera(new Camera(new Point3F(0.0F, 4.0F, -10.0F), AngleF.degrees(40.0F)));
scene.setName("PBRTShowcaseShape3FCurves3F");