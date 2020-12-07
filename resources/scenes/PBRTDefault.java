Color3F colorGoldEta = Color3F.maximumTo1(Color3F.convertXYZToRGBUsingPBRT(IrregularSpectralCurve.GOLD_ETA.toColorXYZ()));
Color3F colorGoldK = Color3F.maximumTo1(Color3F.convertXYZToRGBUsingPBRT(IrregularSpectralCurve.GOLD_K.toColorXYZ()));

Material material1 = new MattePBRTMaterial(new ConstantTexture(new Color3F(20.0F)), new SimplexFractionalBrownianMotionTexture(new Color3F(0.8F, 0.5F, 0.0F), 5.0F, 0.5F, 16));
Material material2 = new PlasticPBRTMaterial(new ConstantTexture(new Color3F(0.2F, 0.2F, 1.0F)), new ConstantTexture(new Color3F(0.01F)), new ConstantTexture(Color3F.WHITE), true);
Material material3 = new MirrorPBRTMaterial();
Material material4 = new MetalPBRTMaterial(new ConstantTexture(colorGoldEta), new ConstantTexture(colorGoldK), new ConstantTexture(new Color3F(0.05F)), new ConstantTexture(new Color3F(0.05F)), true);
Material material5 = new SubstratePBRTMaterial(new ConstantTexture(new Color3F(1.0F, 0.2F, 0.2F)), new ConstantTexture(new Color3F(0.01F)), new ConstantTexture(new Color3F(0.01F)), new ConstantTexture(new Color3F(0.2F)), true);
Material material6 = new MattePBRTMaterial(new ConstantTexture(new Color3F(0.0F)), new ConstantTexture(Color3F.WHITE));

Shape3F shape1 = new Plane3F();
Shape3F shape2 = new Sphere3F(10.0F);
Shape3F shape3 = new RectangularCuboid3F(new Point3F(-1.0F), new Point3F(1.0F));
Shape3F shape4 = TriangleMesh3F.readWavefrontObject("./resources/models/smoothMonkey2.obj", true, 100.0F).get(0);
Shape3F shape5 = new Torus3F();
Shape3F shape6 = new Sphere3F();

Transform transform1 = new Transform();
Transform transform2 = new Transform(new Point3F( 0.0F,  2.00F, 20.0F));
Transform transform3 = new Transform(new Point3F(-3.0F,  1.00F,  5.0F));
Transform transform4 = new Transform(new Point3F( 0.0F,  1.00F,  5.0F), Quaternion4F.from(Matrix44F.rotateY(AngleF.degrees(180.0F))), new Vector3F(0.01F));
Transform transform5 = new Transform(new Point3F( 3.0F,  1.25F,  5.0F));
Transform transform6 = new Transform(new Point3F( 0.0F, 10.00F,  0.0F));

AreaLight areaLight = new DiffuseAreaLight(transform6.getObjectToWorld(), 1, new Color3F(50.0F), shape6, false);

//scene.addLight(areaLight);
scene.addLight(new PerezLight());
scene.addPrimitive(new Primitive(material1, shape1, transform1));
scene.addPrimitive(new Primitive(material2, shape2, transform2));
scene.addPrimitive(new Primitive(material3, shape3, transform3));
scene.addPrimitive(new Primitive(material4, shape4, transform4));
scene.addPrimitive(new Primitive(material5, shape5, transform5));
//scene.addPrimitive(new Primitive(material6, shape6, transform6, areaLight));
scene.setCamera(new Camera(new Point3F(0.0F, 2.0F, -10.0F), AngleF.degrees(40.0F)));
scene.setName("PBRTDefault");