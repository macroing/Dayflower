final Light light1 = new PerezLight();

final Material material1 = new MatteMaterial(new CheckerboardTexture());
final Material material2 = new GlossyMaterial(new Color3F(0.01F, 0.5F, 0.01F), Color3F.BLACK, 0.1F);
final Material material3 = new GlossyMaterial(new Color3F(0.01F, 0.5F, 0.01F), Color3F.BLACK, 0.1F);
final Material material4 = new GlossyMaterial(new Color3F(0.01F, 0.5F, 0.01F), Color3F.BLACK, 0.1F);

final Shape3F shape1 = new Plane3F();
final Shape3F shape2 = new Torus3F();
final Shape3F shape3 = new Cone3F();
final Shape3F shape4 = new Sphere3F();

final Transform transform1 = new Transform(new Point3F(0.0F, 0.0F, 0.0F), Quaternion4F.from(Matrix44F.rotateX(AngleF.degrees(90.0F))));
final Transform transform2 = new Transform(new Point3F(0.0F, 0.75F, 0.0F), Quaternion4F.from(Matrix44F.rotateX(AngleF.degrees(90.0F))));
final Transform transform3 = new Transform(new Point3F(0.0F, 0.5F, 0.0F), Quaternion4F.from(Matrix44F.rotateX(AngleF.degrees(270.0F))));
final Transform transform4 = new Transform(new Point3F(0.0F, 0.5F, 0.0F), new Quaternion4F(), new Vector3F(0.5F, 0.5F, 0.5F));

final Primitive primitive1 = new Primitive(material1, shape1, transform1);
final Primitive primitive2 = new Primitive(material2, shape2, transform2);
final Primitive primitive3 = new Primitive(material3, shape3, transform3);
final Primitive primitive4 = new Primitive(material4, shape4, transform4);

final Camera camera = new Camera(new Point3F(0.0F, 2.0F, -10.0F), AngleF.degrees(40.0F));

scene.addLight(light1);
scene.addPrimitive(primitive1);
scene.addPrimitive(primitive2);
scene.addPrimitive(primitive3);
scene.addPrimitive(primitive4);
scene.setCamera(camera);
scene.setName("GlossyMaterial");