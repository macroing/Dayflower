boolean isDiffuseAreaLight = true;
boolean isTriangleLight = false;

Material material01 = new MatteMaterial();
Material material02 = new MatteMaterial();
Material material03 = new MatteMaterial();
Material material04 = new MatteMaterial();
Material material05 = new MatteMaterial(new Color3F(0.25F, 0.25F, 0.75F));
Material material06 = new MatteMaterial(new Color3F(0.75F, 0.25F, 0.25F));
Material material07 = new MirrorMaterial(Color3F.GRAY_0_50);
Material material08 = new GlassMaterial();
Material material09 = new MatteMaterial(Color3F.WHITE, new Color3F(12.0F));

Shape3F shape01 = new Plane3F(new Point3F(0.0F, 0.0F, 0.0F), new Point3F(0.0F,  0.0F,  1.0F), new Point3F(1.0F, 0.0F,  0.0F));//Bottom
Shape3F shape02 = new Plane3F(new Point3F(0.0F, 0.0F, 0.0F), new Point3F(0.0F,  0.0F, -1.0F), new Point3F(1.0F, 0.0F,  0.0F));//Top
Shape3F shape03 = new Plane3F(new Point3F(0.0F, 0.0F, 0.0F), new Point3F(0.0F, -1.0F,  0.0F), new Point3F(1.0F, 0.0F,  0.0F));//Back
Shape3F shape04 = new Plane3F(new Point3F(0.0F, 0.0F, 0.0F), new Point3F(0.0F,  1.0F,  0.0F), new Point3F(1.0F, 0.0F,  0.0F));//Front
Shape3F shape05 = new Plane3F(new Point3F(0.0F, 0.0F, 0.0F), new Point3F(0.0F,  1.0F,  0.0F), new Point3F(0.0F, 0.0F, -1.0F));//Right
Shape3F shape06 = new Plane3F(new Point3F(0.0F, 0.0F, 0.0F), new Point3F(0.0F,  1.0F,  0.0F), new Point3F(0.0F, 0.0F,  1.0F));//Left
Shape3F shape07 = new RectangularCuboid3F(new Point3F(-1.0F, -1.0F, -1.0F), new Point3F(1.0F, 1.0F, 1.0F));//new Sphere3F(1.0F, new Point3F(0.0F, 0.0F, 0.0F));
Shape3F shape08 = new Sphere3F(1.0F, new Point3F(0.0F, 0.0F, 0.0F));
Shape3F shape09 = isTriangleLight ? new Triangle3F() : new Sphere3F();

Transform transform01 = new Transform(new Point3F( 0.0F, 0.0F,  0.0F));
Transform transform02 = new Transform(new Point3F( 0.0F, 5.0F,  0.0F));
Transform transform03 = new Transform(new Point3F( 0.0F, 0.0F,  0.0F));
Transform transform04 = new Transform(new Point3F( 0.0F, 0.0F, 10.0F));
Transform transform05 = new Transform(new Point3F( 3.0F, 0.0F,  0.0F));
Transform transform06 = new Transform(new Point3F(-3.0F, 0.0F,  0.0F));
Transform transform07 = new Transform(new Point3F(-1.5F, 1.0F,  8.0F));
Transform transform08 = new Transform(new Point3F( 1.5F, 1.0F,  7.0F));
Transform transform09 = new Transform(new Point3F( 0.0F, 4.99F, 9.0F), Quaternion4F.from(Matrix44F.rotateX(AngleF.degrees(270.0F))), new Vector3F(1.0F));

Primitive primitive01 = new Primitive(material01, shape01, transform01);
Primitive primitive02 = new Primitive(material02, shape02, transform02);
Primitive primitive03 = new Primitive(material03, shape03, transform03);
Primitive primitive04 = new Primitive(material04, shape04, transform04);
Primitive primitive05 = new Primitive(material05, shape05, transform05);
Primitive primitive06 = new Primitive(material06, shape06, transform06);
Primitive primitive07 = new Primitive(material07, shape07, transform07);
Primitive primitive08 = new Primitive(material08, shape08, transform08);
Primitive primitive09 = new Primitive(material09, shape09, transform09);

AreaLight areaLight09 = isDiffuseAreaLight ? new DiffuseAreaLight(transform09, 1, new Color3F(12.0F), shape09, false) : new PrimitiveAreaLight(primitive09);

//primitive09.setAreaLight(areaLight09);

Camera camera = new Camera(new Point3F(0.0F, 2.5F, 1.0F));
camera.setResolution(1024.0F, 768.0F);
camera.setFieldOfViewY(AngleF.degrees(58.0F));
camera.setFieldOfViewX();

//scene.addLight(areaLight09);
scene.addLight(new PointLight(new Color3F(12.0F), new Point3F(0.0F, 4.0F, 9.0F)));
//scene.addLight(new SpotLight(AngleF.degrees(50.0F), AngleF.degrees(5.0F), new Color3F(50.0F), new Point3F(0.0F, 4.9F, 9.0F), Vector3F.y(-1.0F)));
scene.addPrimitive(primitive01);
scene.addPrimitive(primitive02);
scene.addPrimitive(primitive03);
scene.addPrimitive(primitive04);
scene.addPrimitive(primitive05);
scene.addPrimitive(primitive06);
scene.addPrimitive(primitive07);
scene.addPrimitive(primitive08);
//scene.addPrimitive(primitive09);
scene.setCamera(camera);
scene.setName("CornellBox");