List<TriangleMesh3F> triangleMeshes = TriangleMesh3F.readWavefrontObject("./resources/models/bil.obj", true);

Texture textureHjul = ImageTexture.load("./resources/textures/hjul.png");
Texture textureLaminate = ImageTexture.load("./resources/textures/laminate.jpg", AngleF.degrees(0.0F), new Vector2F(0.5F, 0.5F));
Texture textureTextur = ImageTexture.load("./resources/textures/textur.png");

Material material11 = new MatteMaterial(new ConstantTexture(new Color3F(20.0F)), textureLaminate);
Material material12 = new MatteMaterial(new ConstantTexture(new Color3F(20.0F)), textureLaminate);
Material material13 = new MatteMaterial(new ConstantTexture(new Color3F(20.0F)), textureLaminate);
Material material14 = new MatteMaterial(new ConstantTexture(new Color3F(20.0F)), textureLaminate);
Material material15 = new MatteMaterial(new ConstantTexture(new Color3F(20.0F)), textureLaminate);
Material material16 = new MatteMaterial(new ConstantTexture(new Color3F(20.0F)), textureLaminate);
Material material21 = new PlasticMaterial(textureTextur, new ConstantTexture(new Color3F(0.01F)), new ConstantTexture(Color3F.WHITE), true);
Material material22 = new SubstrateMaterial(textureHjul, new ConstantTexture(new Color3F(0.01F)), new ConstantTexture(new Color3F(0.01F)), new ConstantTexture(new Color3F(0.2F)), true);
Material material23 = new SubstrateMaterial(textureHjul, new ConstantTexture(new Color3F(0.01F)), new ConstantTexture(new Color3F(0.01F)), new ConstantTexture(new Color3F(0.2F)), true);
Material material24 = new SubstrateMaterial(textureHjul, new ConstantTexture(new Color3F(0.01F)), new ConstantTexture(new Color3F(0.01F)), new ConstantTexture(new Color3F(0.2F)), true);
Material material25 = new SubstrateMaterial(textureHjul, new ConstantTexture(new Color3F(0.01F)), new ConstantTexture(new Color3F(0.01F)), new ConstantTexture(new Color3F(0.2F)), true);
Material material31 = new MatteMaterial(new ConstantTexture(new Color3F(0.0F)), new ConstantTexture(Color3F.WHITE));

Shape3F shape11 = new Plane3F(new Point3F(0.0F, 0.0F, 0.0F), new Point3F(0.0F, +0.0F, +1.0F), new Point3F(1.0F, 0.0F, +0.0F));//Bottom
Shape3F shape12 = new Plane3F(new Point3F(0.0F, 0.0F, 0.0F), new Point3F(0.0F, +0.0F, -1.0F), new Point3F(1.0F, 0.0F, +0.0F));//Top
Shape3F shape13 = new Plane3F(new Point3F(0.0F, 0.0F, 0.0F), new Point3F(0.0F, +1.0F, +0.0F), new Point3F(1.0F, 0.0F, +0.0F));//Front
Shape3F shape14 = new Plane3F(new Point3F(0.0F, 0.0F, 0.0F), new Point3F(0.0F, -1.0F, +0.0F), new Point3F(1.0F, 0.0F, +0.0F));//Back
Shape3F shape15 = new Plane3F(new Point3F(0.0F, 0.0F, 0.0F), new Point3F(0.0F, +1.0F, +0.0F), new Point3F(0.0F, 0.0F, -1.0F));//Right
Shape3F shape16 = new Plane3F(new Point3F(0.0F, 0.0F, 0.0F), new Point3F(0.0F, +1.0F, +0.0F), new Point3F(0.0F, 0.0F,  1.0F));//Left
Shape3F shape21 = triangleMeshes.get(0);
Shape3F shape22 = triangleMeshes.get(1);
Shape3F shape23 = triangleMeshes.get(2);
Shape3F shape24 = triangleMeshes.get(3);
Shape3F shape25 = triangleMeshes.get(4);
Shape3F shape31 = new Sphere3F();

Matrix44F matrix11 = Matrix44F.translate(+0.0F, 0.00F, + 0.0F);
Matrix44F matrix12 = Matrix44F.translate(+0.0F, 8.00F, + 0.0F);
Matrix44F matrix13 = Matrix44F.translate(+0.0F, 0.00F, + 7.5F);
Matrix44F matrix14 = Matrix44F.translate(+0.0F, 0.00F, -10.0F);
Matrix44F matrix15 = Matrix44F.translate(+5.0F, 0.00F, + 0.0F);
Matrix44F matrix16 = Matrix44F.translate(-5.0F, 0.00F, + 0.0F);
Matrix44F matrix21 = Matrix44F.multiply(Matrix44F.multiply(Matrix44F.translate(+0.0F, 0.25F, 3.0F), Matrix44F.rotateY(AngleF.degrees(90.0F))), Matrix44F.scale(1.0F));
Matrix44F matrix22 = Matrix44F.multiply(Matrix44F.multiply(Matrix44F.translate(+0.0F, 0.25F, 3.0F), Matrix44F.rotateY(AngleF.degrees(90.0F))), Matrix44F.scale(1.0F));
Matrix44F matrix23 = Matrix44F.multiply(Matrix44F.multiply(Matrix44F.translate(+0.0F, 0.25F, 3.0F), Matrix44F.rotateY(AngleF.degrees(90.0F))), Matrix44F.scale(1.0F));
Matrix44F matrix24 = Matrix44F.multiply(Matrix44F.multiply(Matrix44F.translate(+0.0F, 0.25F, 3.0F), Matrix44F.rotateY(AngleF.degrees(90.0F))), Matrix44F.scale(1.0F));
Matrix44F matrix25 = Matrix44F.multiply(Matrix44F.multiply(Matrix44F.translate(+0.0F, 0.25F, 3.0F), Matrix44F.rotateY(AngleF.degrees(90.0F))), Matrix44F.scale(1.0F));
Matrix44F matrix31 = Matrix44F.translate(0.0F, 5.0F, 0.0F);

AreaLight areaLight31 = new DiffuseAreaLight(matrix31, 1, new Color3F(20.0F), shape31, true);

scene.addLight(areaLight31);
scene.addPrimitive(new Primitive(material11, shape11, new ConstantTexture(Color3F.GRAY), new ConstantTexture(), matrix11));
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
scene.addPrimitive(new Primitive(material31, shape31, new ConstantTexture(), new ConstantTexture(), matrix31, areaLight31));
scene.setCamera(new Camera(new Point3F(0.0F, 4.0F, -10.0F), AngleF.degrees(40.0F)));
scene.setName("PBRTBil");