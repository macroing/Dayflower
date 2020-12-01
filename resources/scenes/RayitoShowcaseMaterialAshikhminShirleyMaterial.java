Material material1 = new LambertianMaterial();
Material material2 = new AshikhminShirleyMaterial();

Shape3F shape1 = new Plane3F();
Shape3F shape2 = new Sphere3F(10.0F);

Transform transform1 = new Transform(new Point3F(0.0F, 0.0F,  0.0F));
Transform transform2 = new Transform(new Point3F(0.0F, 2.0F, 30.0F));

scene.addLight(new PerezLight());
scene.addPrimitive(new Primitive(material1, shape1, transform1));
scene.addPrimitive(new Primitive(material2, shape2, transform2));
scene.setCamera(new Camera(new Point3F(0.0F, 2.0F, 0.0F)));
scene.setName("RayitoShowcaseMaterialAshikhminShirleyMaterial");