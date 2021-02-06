Material material1 = new MatteMaterial(new SimplexFractionalBrownianMotionTexture());
Material material2 = new MetalMaterial(Color3F.AU_K, Color3F.AU_ETA, Color3F.BLACK, 0.01F, 0.7F, true);

Vertex3F vertexA = new Vertex3F(new Point2F(0.5F, 0.0F), new Point4F(+0.0F, +1.0F, 0.0F), Vector3F.normalize(new Vector3F(+0.0F, 0.5F, -1.0F)));
Vertex3F vertexB = new Vertex3F(new Point2F(1.0F, 1.0F), new Point4F(+1.0F, -1.0F, 0.0F), Vector3F.normalize(new Vector3F(-0.5F, 0.0F, -1.0F)));
Vertex3F vertexC = new Vertex3F(new Point2F(0.0F, 1.0F), new Point4F(-1.0F, -1.0F, 0.0F), Vector3F.normalize(new Vector3F(+0.5F, 0.0F, -1.0F)));

Shape3F shape1 = new Plane3F();
Shape3F shape2 = new Triangle3F(vertexA, vertexB, vertexC);

Transform transform1 = new Transform(new Point3F(0.0F, 0.0F, 0.0F));
Transform transform2 = new Transform(new Point3F(0.0F, 1.5F, 0.0F));

//scene.addLight(new DirectionalLight());
//scene.addLight(new PerezLight());
//scene.addLight(new PointLight(new Color3F(12.0F), new Point3F(0.0F, 4.0F, -5.0F)));
scene.addLight(new SpotLight(AngleF.degrees(50.0F), AngleF.degrees(5.0F), new Color3F(50.0F), new Point3F(0.0F, 2.0F, -5.0F), Vector3F.z()));
scene.addPrimitive(new Primitive(material1, shape1, transform1));
scene.addPrimitive(new Primitive(material2, shape2, transform2));
scene.setCamera(new Camera(new Point3F(0.0F, 2.0F, -10.0F), AngleF.degrees(40.0F)));
scene.setName("MetalMaterial");