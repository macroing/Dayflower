Texture textureBullseye = new BullseyeTexture(new Color3F(1.0F, 0.01F, 0.01F), Color3F.WHITE, new Point3F(0.0F, 10.0F, 0.0F), 4.0F);
Texture textureCheckerboard = new CheckerboardTexture(new Color3F(1.0F, 0.01F, 0.01F), Color3F.WHITE, AngleF.degrees(0.0F), new Vector2F(2.0F, 2.0F));
Texture textureImage = ImageTexture.load("./resources/textures/laminate.jpg", AngleF.degrees(0.0F), new Vector2F(0.5F, 0.5F));

Material materialClearCoatBullseye = new ClearCoatSmallPTMaterial(textureBullseye);
Material materialClearCoatCheckerboard = new ClearCoatSmallPTMaterial(textureCheckerboard);
Material materialClearCoatImage = new ClearCoatSmallPTMaterial(textureImage);
Material materialClearCoatConstantRed = new ClearCoatSmallPTMaterial(new Color3F(1.0F, 0.01F, 0.01F));
Material materialGlassConstantYellow = new GlassSmallPTMaterial(Color3F.WHITE, new Color3F(1.0F, 1.0F, 0.5F));
Material materialMatteImage = new MatteSmallPTMaterial(textureImage);

Material material1 = /*materialMatteImage*/materialGlassConstantYellow/*materialClearCoatBullseye*/;
Material material2 = /*materialClearCoatConstantRed*/materialClearCoatCheckerboard;
Material material3 = new MirrorSmallPTMaterial(new Color3F(0.01F, 1.0F, 0.01F));
Material material4 = materialMatteImage/*new MetalSmallPTMaterial()*/;
Material material5 = new MatteSmallPTMaterial(new Color3F(0.01F, 0.01F, 1.0F));
Material material6 = new MatteSmallPTMaterial();

Shape3F shape1 = new Sphere3F();
Shape3F shape2 = new Torus3F();
Shape3F shape3 = new RectangularCuboid3F(new Point3F(-1.0F, -1.0F, -1.0F), new Point3F(+1.0F, +1.0F, +1.0F));
Shape3F shape4 = new Plane3F();
Shape3F shape5 = new Triangle3F();
Shape3F shape6 = new Sphere3F(10.0F);

Transform transform1 = new Transform(new Point3F(-2.5F,  1.00F, 5.0F));
Transform transform2 = new Transform(new Point3F(+0.0F,  1.25F, 5.0F));
Transform transform3 = new Transform(new Point3F(+2.5F,  1.00F, 5.0F));
Transform transform4 = new Transform(new Point3F(+0.0F,  0.00F, 0.0F));
Transform transform5 = new Transform(new Point3F(+0.0F,  3.50F, 5.0F));
Transform transform6 = new Transform(new Point3F(+0.0F, 20.00F, 5.0F));

scene.addPrimitive(new Primitive(material1, shape1, transform1));
scene.addPrimitive(new Primitive(material2, shape2, transform2));
scene.addPrimitive(new Primitive(material3, shape3, transform3));
scene.addPrimitive(new Primitive(material4, shape4, transform4));
scene.addPrimitive(new Primitive(material5, shape5, transform5));
//scene.addPrimitive(new Primitive(material6, shape6, transform6));
scene.setCamera(new Camera(new Point3F(0.0F, 1.0F, -5.0F), AngleF.degrees(40.0F)));
scene.setName("GPUTest");