final Light light1 = new PerezLight();

final Material material1 = new MatteMaterial();
final Material material2 = new MatteMaterial(new CheckerboardTexture(Color3F.GRAY_0_50, Color3F.WHITE, AngleF.degrees(0.0F), new Vector2F(4.0F, 4.0F)));

final Shape3F shape1 = new Plane3F();
final Shape3F shape2 = new Rectangle3F();

final Transform transform1 = new Transform(new Point3F(+0.0F, 0.0F, 0.0F));
final Transform transform2 = new Transform(new Point3F(-1.0F, 0.0F, 0.0F));

final Primitive primitive1 = new Primitive(material1, shape1, transform1);
final Primitive primitive2 = new Primitive(material2, shape2, transform2);

final Camera camera = new Camera(new Point3F(0.0F, 2.0F, -10.0F), AngleF.degrees(40.0F));

scene.addLight(light1);
scene.addPrimitive(primitive1);
scene.addPrimitive(primitive2);
scene.setCamera(camera);
scene.setName("Rectangle3F");