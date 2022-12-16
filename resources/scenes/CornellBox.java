final Light light1 = new PointLight(new Color3F(12.0F), new Point3F(0.0F, 4.0F, 9.0F));

final Material material1 = new MatteMaterial();
final Material material2 = new MatteMaterial();
final Material material3 = new MatteMaterial();
final Material material4 = new MatteMaterial();
final Material material5 = new MatteMaterial(new Color3F(0.25F, 0.25F, 0.75F));
final Material material6 = new MatteMaterial(new Color3F(0.75F, 0.25F, 0.25F));
final Material material7 = new MirrorMaterial(Color3F.GRAY);
final Material material8 = new GlassMaterial();

final Shape3F shape1 = new Plane3F();
final Shape3F shape2 = new Plane3F();
final Shape3F shape3 = new Plane3F();
final Shape3F shape4 = new Plane3F();
final Shape3F shape5 = new Plane3F();
final Shape3F shape6 = new Plane3F();
final Shape3F shape7 = new RectangularCuboid3F(new Point3F(-1.0F, -1.0F, -1.0F), new Point3F(1.0F, 1.0F, 1.0F));
final Shape3F shape8 = new Sphere3F();

final Transform transform1 = new Transform(new Point3F( 0.0F, 0.0F,  0.0F), Quaternion4F.from(Matrix44F.rotateX(AngleF.degrees(90.0F))));
final Transform transform2 = new Transform(new Point3F( 0.0F, 5.0F,  0.0F), Quaternion4F.from(Matrix44F.multiply(Matrix44F.rotateX(AngleF.degrees(90.0F)), Matrix44F.rotateY(AngleF.degrees(180.0F)))));
final Transform transform3 = new Transform(new Point3F( 0.0F, 0.0F,  0.0F), Quaternion4F.from(Matrix44F.rotateX(AngleF.degrees(180.0F))));
final Transform transform4 = new Transform(new Point3F( 0.0F, 0.0F, 10.0F));
final Transform transform5 = new Transform(new Point3F( 3.0F, 0.0F,  0.0F), Quaternion4F.from(Matrix44F.rotateY(AngleF.degrees(90.0F))));
final Transform transform6 = new Transform(new Point3F(-3.0F, 0.0F,  0.0F), Quaternion4F.from(Matrix44F.multiply(Matrix44F.rotateY(AngleF.degrees(90.0F)), Matrix44F.rotateX(AngleF.degrees(180.0F)))));
final Transform transform7 = new Transform(new Point3F(-1.5F, 1.0F,  8.0F));
final Transform transform8 = new Transform(new Point3F( 1.5F, 1.0F,  7.0F));

final AreaLight areaLight8 = new DiffuseAreaLight(transform8, 1, new Color3F(2.0F), shape8, true);

final Primitive primitive1 = new Primitive(material1, shape1, transform1);
final Primitive primitive2 = new Primitive(material2, shape2, transform2);
final Primitive primitive3 = new Primitive(material3, shape3, transform3);
final Primitive primitive4 = new Primitive(material4, shape4, transform4);
final Primitive primitive5 = new Primitive(material5, shape5, transform5);
final Primitive primitive6 = new Primitive(material6, shape6, transform6);
final Primitive primitive7 = new Primitive(material7, shape7, transform7);
final Primitive primitive8 = new Primitive(material8, shape8, transform8, areaLight8);

final
Camera camera = new Camera(new Point3F(0.0F, 2.5F, 1.0F));
camera.setResolution(1024.0F, 768.0F);
camera.setFieldOfViewY(AngleF.degrees(58.0F));
camera.setFieldOfViewX();

scene.addLight(areaLight8);
//scene.addLight(light1);
scene.addPrimitive(primitive1);
scene.addPrimitive(primitive2);
scene.addPrimitive(primitive3);
scene.addPrimitive(primitive4);
scene.addPrimitive(primitive5);
scene.addPrimitive(primitive6);
scene.addPrimitive(primitive7);
scene.addPrimitive(primitive8);
scene.setCamera(camera);
scene.setName("CornellBox");