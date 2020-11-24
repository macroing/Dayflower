boolean isInBox = false;

List<TriangleMesh3F> triangleMeshes = TriangleMesh3F.readWavefrontObject("./resources/models/aphroditegirl.obj", true, 100.0F);

Texture textureLaminate = ImageTexture.load("./resources/textures/laminate.jpg", AngleF.degrees(0.0F), new Vector2F(0.5F, 0.5F));

Material material11 = new MatteMaterial(new ConstantTexture(new Color3F(20.0F)), textureLaminate);
Material material12 = new MatteMaterial(new ConstantTexture(new Color3F(20.0F)), textureLaminate);
Material material13 = new MatteMaterial(new ConstantTexture(new Color3F(20.0F)), textureLaminate);
Material material14 = new MatteMaterial(new ConstantTexture(new Color3F(20.0F)), textureLaminate);
Material material15 = new MatteMaterial(new ConstantTexture(new Color3F(20.0F)), textureLaminate);
Material material16 = new MatteMaterial(new ConstantTexture(new Color3F(20.0F)), textureLaminate);
Material material21 = new PlasticMaterial(new ConstantTexture(new Color3F(227, 161, 115)), new ConstantTexture(new Color3F(0.01F)), new ConstantTexture(Color3F.WHITE), true);
Material material22 = new PlasticMaterial(new ConstantTexture(new Color3F(227, 161, 115)), new ConstantTexture(new Color3F(0.01F)), new ConstantTexture(Color3F.WHITE), true);
Material material23 = new PlasticMaterial(new ConstantTexture(new Color3F(0.2F, 0.2F, 1.0F)), new ConstantTexture(new Color3F(0.001F)), new ConstantTexture(Color3F.WHITE), true);
Material material24 = new PlasticMaterial(new ConstantTexture(new Color3F(216, 192, 120)), new ConstantTexture(new Color3F(0.01F)), new ConstantTexture(Color3F.WHITE), true);
Material material25 = new PlasticMaterial(new ConstantTexture(new Color3F(0.2F, 0.2F, 1.0F)), new ConstantTexture(new Color3F(0.001F)), new ConstantTexture(Color3F.WHITE), true);
Material material26 = new PlasticMaterial(new ConstantTexture(new Color3F(227, 161, 115)), new ConstantTexture(new Color3F(0.01F)), new ConstantTexture(Color3F.WHITE), true);
Material material27 = new PlasticMaterial(new ConstantTexture(new Color3F(1.0F, 1.0F, 1.0F)), new ConstantTexture(new Color3F(0.01F)), new ConstantTexture(Color3F.WHITE), true);
Material material28 = new PlasticMaterial(new ConstantTexture(new Color3F(227, 161, 115)), new ConstantTexture(new Color3F(0.01F)), new ConstantTexture(Color3F.WHITE), true);
Material material31 = new MatteMaterial(new ConstantTexture(new Color3F(0.0F)), new ConstantTexture(Color3F.WHITE));

Shape3F shape11 = new Plane3F(new Point3F(0.0F, 0.0F, 0.0F), new Point3F(0.0F, +0.0F, +1.0F), new Point3F(1.0F, 0.0F, +0.0F));//B
Shape3F shape12 = new Plane3F(new Point3F(0.0F, 0.0F, 0.0F), new Point3F(0.0F, +0.0F, -1.0F), new Point3F(1.0F, 0.0F, +0.0F));//T
Shape3F shape13 = new Plane3F(new Point3F(0.0F, 0.0F, 0.0F), new Point3F(0.0F, +1.0F, +0.0F), new Point3F(1.0F, 0.0F, +0.0F));//F
Shape3F shape14 = new Plane3F(new Point3F(0.0F, 0.0F, 0.0F), new Point3F(0.0F, -1.0F, +0.0F), new Point3F(1.0F, 0.0F, +0.0F));//B
Shape3F shape15 = new Plane3F(new Point3F(0.0F, 0.0F, 0.0F), new Point3F(0.0F, +1.0F, +0.0F), new Point3F(0.0F, 0.0F, -1.0F));//R
Shape3F shape16 = new Plane3F(new Point3F(0.0F, 0.0F, 0.0F), new Point3F(0.0F, +1.0F, +0.0F), new Point3F(0.0F, 0.0F,  1.0F));//L
Shape3F shape21 = triangleMeshes.get(0);
Shape3F shape22 = triangleMeshes.get(1);
Shape3F shape23 = triangleMeshes.get(2);
Shape3F shape24 = triangleMeshes.get(3);
Shape3F shape25 = triangleMeshes.get(4);
Shape3F shape26 = triangleMeshes.get(5);
Shape3F shape27 = triangleMeshes.get(6);
Shape3F shape28 = triangleMeshes.get(7);
Shape3F shape31 = new Sphere3F();

Matrix44F matrix11 = Matrix44F.translate(+0.0F, 0.00F, + 0.0F);
Matrix44F matrix12 = Matrix44F.translate(+0.0F, 8.00F, + 0.0F);
Matrix44F matrix13 = Matrix44F.translate(+0.0F, 0.00F, + 7.5F);
Matrix44F matrix14 = Matrix44F.translate(+0.0F, 0.00F, -10.0F);
Matrix44F matrix15 = Matrix44F.translate(+5.0F, 0.00F, + 0.0F);
Matrix44F matrix16 = Matrix44F.translate(-5.0F, 0.00F, + 0.0F);
Matrix44F matrix21 = Matrix44F.multiply(Matrix44F.multiply(Matrix44F.translate(0.0F, 0.0F, 4.0F), Matrix44F.rotateY(AngleF.degrees(0.0F))), Matrix44F.scale(0.02F));
Matrix44F matrix31 = Matrix44F.translate(0.0F, 8.0F, 0.0F);

AreaLight areaLight31 = new DiffuseAreaLight(matrix31, 1, new Color3F(20.0F), shape31, false);

if(isInBox) {
	scene.addLight(areaLight31);
	scene.addPrimitive(new Primitive(material11, shape11, new ConstantTexture(), new ConstantTexture(), matrix11));
	scene.addPrimitive(new Primitive(material12, shape12, new ConstantTexture(), new ConstantTexture(), matrix12));
	scene.addPrimitive(new Primitive(material13, shape13, new ConstantTexture(), new ConstantTexture(), matrix13));
	scene.addPrimitive(new Primitive(material14, shape14, new ConstantTexture(), new ConstantTexture(), matrix14));
	scene.addPrimitive(new Primitive(material15, shape15, new ConstantTexture(), new ConstantTexture(), matrix15));
	scene.addPrimitive(new Primitive(material16, shape16, new ConstantTexture(), new ConstantTexture(), matrix16));
	scene.addPrimitive(new Primitive(material21, shape21, new ConstantTexture(), new ConstantTexture(), matrix21));
	scene.addPrimitive(new Primitive(material22, shape22, new ConstantTexture(), new ConstantTexture(), matrix21));
	scene.addPrimitive(new Primitive(material23, shape23, new ConstantTexture(), new ConstantTexture(), matrix21));
	scene.addPrimitive(new Primitive(material24, shape24, new ConstantTexture(), new ConstantTexture(), matrix21));
	scene.addPrimitive(new Primitive(material25, shape25, new ConstantTexture(), new ConstantTexture(), matrix21));
	scene.addPrimitive(new Primitive(material26, shape26, new ConstantTexture(), new ConstantTexture(), matrix21));
	scene.addPrimitive(new Primitive(material27, shape27, new ConstantTexture(), new ConstantTexture(), matrix21));
	scene.addPrimitive(new Primitive(material28, shape28, new ConstantTexture(), new ConstantTexture(), matrix21));
	scene.addPrimitive(new Primitive(material31, shape31, new ConstantTexture(), new ConstantTexture(), matrix31, areaLight31));
	scene.setCamera(new Camera(new Point3F(0.0F, 4.0F, -10.0F), AngleF.degrees(40.0F)));
	scene.setName("PBRTAphroditeGirl");
} else {
	scene.addLight(new PerezLight());
	scene.addPrimitive(new Primitive(material11, shape11, new ConstantTexture(), new ConstantTexture(), matrix11));
	scene.addPrimitive(new Primitive(material21, shape21, new ConstantTexture(), new ConstantTexture(), matrix21));
	scene.addPrimitive(new Primitive(material22, shape22, new ConstantTexture(), new ConstantTexture(), matrix21));
	scene.addPrimitive(new Primitive(material23, shape23, new ConstantTexture(), new ConstantTexture(), matrix21));
	scene.addPrimitive(new Primitive(material24, shape24, new ConstantTexture(), new ConstantTexture(), matrix21));
	scene.addPrimitive(new Primitive(material25, shape25, new ConstantTexture(), new ConstantTexture(), matrix21));
	scene.addPrimitive(new Primitive(material26, shape26, new ConstantTexture(), new ConstantTexture(), matrix21));
	scene.addPrimitive(new Primitive(material27, shape27, new ConstantTexture(), new ConstantTexture(), matrix21));
	scene.addPrimitive(new Primitive(material28, shape28, new ConstantTexture(), new ConstantTexture(), matrix21));
	scene.setCamera(new Camera(new Point3F(0.0F, 4.0F, -10.0F), AngleF.degrees(40.0F)));
	scene.setName("PBRTAphroditeGirl");
}