Texture textureLaminate = ImageTexture.load("./resources/textures/laminate.jpg", AngleF.degrees(0.0F), new Vector2F(0.5F, 0.5F));

Material material11 = new MatteMaterial(new ConstantTexture(new Color3F(20.0F)), textureLaminate);
Material material12 = new MatteMaterial(new ConstantTexture(new Color3F(20.0F)), textureLaminate);
Material material13 = new MatteMaterial(new ConstantTexture(new Color3F(20.0F)), textureLaminate);
Material material14 = new MatteMaterial(new ConstantTexture(new Color3F(20.0F)), textureLaminate);
Material material15 = new MatteMaterial(new ConstantTexture(new Color3F(20.0F)), textureLaminate);
Material material16 = new MatteMaterial(new ConstantTexture(new Color3F(20.0F)), textureLaminate);
Material material21 = new PlasticMaterial(new ConstantTexture(new Color3F(0.2F, 0.2F, 1.0F)), new ConstantTexture(new Color3F(0.01F)), new ConstantTexture(Color3F.WHITE), true);
Material material22 = new MetalMaterial(new ConstantTexture(Color3F.maximumTo1(IrregularSpectralCurve.GOLD_ETA.toColorXYZ())), new ConstantTexture(Color3F.maximumTo1(IrregularSpectralCurve.GOLD_K.toColorXYZ())), new ConstantTexture(new Color3F(0.05F)), new ConstantTexture(new Color3F(0.05F)), true);
Material material23 = new SubstrateMaterial(new ConstantTexture(new Color3F(1.0F, 0.2F, 0.2F)), new ConstantTexture(new Color3F(0.01F)), new ConstantTexture(new Color3F(0.01F)), new ConstantTexture(new Color3F(0.2F)), true);
Material material24 = new GlassMaterial(new ConstantTexture(new Color3F(1.5F)), new ConstantTexture(new Color3F(1.0F, 1.0F, 0.5F)), new ConstantTexture(new Color3F(1.0F, 1.0F, 0.5F)), new ConstantTexture(), new ConstantTexture(), true);
Material material25 = new MatteMaterial(new ConstantTexture(new Color3F(90.0F)), new ConstantTexture(new Color3F(0.2F, 1.0F, 0.2F)));
Material material26 = new MirrorMaterial();
Material material27 = new UberMaterial();
Material material31 = new MatteMaterial(new ConstantTexture(new Color3F(0.0F)), new ConstantTexture(Color3F.WHITE));

Shape3F shape11 = new Plane3F(new Point3F(0.0F, 0.0F, 0.0F), new Point3F(0.0F, +0.0F, +1.0F), new Point3F(1.0F, 0.0F, +0.0F));//Bottom
Shape3F shape12 = new Plane3F(new Point3F(0.0F, 0.0F, 0.0F), new Point3F(0.0F, +0.0F, -1.0F), new Point3F(1.0F, 0.0F, +0.0F));//Top
Shape3F shape13 = new Plane3F(new Point3F(0.0F, 0.0F, 0.0F), new Point3F(0.0F, +1.0F, +0.0F), new Point3F(1.0F, 0.0F, +0.0F));//Front
Shape3F shape14 = new Plane3F(new Point3F(0.0F, 0.0F, 0.0F), new Point3F(0.0F, -1.0F, +0.0F), new Point3F(1.0F, 0.0F, +0.0F));//Back
Shape3F shape15 = new Plane3F(new Point3F(0.0F, 0.0F, 0.0F), new Point3F(0.0F, +1.0F, +0.0F), new Point3F(0.0F, 0.0F, -1.0F));//Right
Shape3F shape16 = new Plane3F(new Point3F(0.0F, 0.0F, 0.0F), new Point3F(0.0F, +1.0F, +0.0F), new Point3F(0.0F, 0.0F,  1.0F));//Left
Shape3F shape21 = new Sphere3F();
Shape3F shape22 = new Sphere3F();
Shape3F shape23 = new Sphere3F();
Shape3F shape24 = new Sphere3F();
Shape3F shape25 = new Sphere3F();
Shape3F shape26 = new Sphere3F();
Shape3F shape27 = new Sphere3F();
Shape3F shape31 = new Sphere3F();

Matrix44F matrix11 = Matrix44F.translate(+0.0F, 0.00F, + 0.0F);
Matrix44F matrix12 = Matrix44F.translate(+0.0F, 8.00F, + 0.0F);
Matrix44F matrix13 = Matrix44F.translate(+0.0F, 0.00F, + 7.5F);
Matrix44F matrix14 = Matrix44F.translate(+0.0F, 0.00F, -10.0F);
Matrix44F matrix15 = Matrix44F.translate(+5.0F, 0.00F, + 0.0F);
Matrix44F matrix16 = Matrix44F.translate(-5.0F, 0.00F, + 0.0F);
Matrix44F matrix21 = Matrix44F.translate(-3.0F, 1.25F, + 5.0F);
Matrix44F matrix22 = Matrix44F.translate(+0.0F, 1.25F, + 5.0F);
Matrix44F matrix23 = Matrix44F.translate(+3.0F, 1.25F, + 5.0F);
Matrix44F matrix24 = Matrix44F.translate(-3.0F, 3.75F, + 5.0F);
Matrix44F matrix25 = Matrix44F.translate(+0.0F, 3.75F, + 5.0F);
Matrix44F matrix26 = Matrix44F.translate(+3.0F, 3.75F, + 5.0F);
Matrix44F matrix27 = Matrix44F.translate(+0.0F, 6.25F, + 5.0F);
Matrix44F matrix31 = Matrix44F.translate(+0.0F, 0.00F, + 0.0F);

AreaLight areaLight31 = new DiffuseAreaLight(matrix31, 1, new Color3F(20.0F), shape31, true);

scene.addLight(areaLight31);
scene.addPrimitive(new Primitive(material11, shape11, new ConstantTexture(), new ConstantTexture(), matrix11));
scene.addPrimitive(new Primitive(material12, shape12, new ConstantTexture(), new ConstantTexture(), matrix12));
scene.addPrimitive(new Primitive(material13, shape13, new ConstantTexture(), new ConstantTexture(), matrix13));
scene.addPrimitive(new Primitive(material14, shape14, new ConstantTexture(), new ConstantTexture(), matrix14));
scene.addPrimitive(new Primitive(material15, shape15, new ConstantTexture(), new ConstantTexture(), matrix15));
scene.addPrimitive(new Primitive(material16, shape16, new ConstantTexture(), new ConstantTexture(), matrix16));
scene.addPrimitive(new Primitive(material21, shape21, new ConstantTexture(), new ConstantTexture(), matrix21));
scene.addPrimitive(new Primitive(material22, shape22, new ConstantTexture(), new ConstantTexture(), matrix22));
scene.addPrimitive(new Primitive(material23, shape23, new ConstantTexture(), new ConstantTexture(), matrix23));
scene.addPrimitive(new Primitive(material24, shape24, new ConstantTexture(), new ConstantTexture(), matrix24));
scene.addPrimitive(new Primitive(material25, shape25, new ConstantTexture(), new ConstantTexture(), matrix25));
scene.addPrimitive(new Primitive(material26, shape26, new ConstantTexture(), new ConstantTexture(), matrix26));
scene.addPrimitive(new Primitive(material27, shape27, new ConstantTexture(), new ConstantTexture(), matrix27));
scene.addPrimitive(new Primitive(material31, shape31, new ConstantTexture(), new ConstantTexture(), matrix31, areaLight31));
scene.setCamera(new Camera(new Point3F(0.0F, 4.0F, -10.0F), AngleF.degrees(40.0F)));
scene.setName("PBRTShowcaseMaterial");