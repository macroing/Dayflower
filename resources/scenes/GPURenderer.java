Texture textureCheckerboardGray = new CheckerboardTexture();
Texture textureCheckerboardRed = new CheckerboardTexture(new Color3F(1.0F, 0.01F, 0.01F), Color3F.WHITE, AngleF.degrees(0.0F), new Vector2F(4.0F, 4.0F));

Material materialPlane = new MatteMaterial(new CheckerboardTexture());
Material materialSphereA = new ClearCoatMaterial(textureCheckerboardRed);
Material materialSphereB = new GlassMaterial(Color3F.WHITE, new Color3F(1.0F, 1.0F, 0.5F));
Material materialSphereC = new GlossyMaterial(textureCheckerboardRed, ConstantTexture.BLACK, 0.05F);
Material materialSphereD = new MatteMaterial(new Color3F(0.1F, 1.0F, 0.1F));
Material materialSphereE = new MetalMaterial();
Material materialSphereF = new MirrorMaterial(Color3F.GRAY_0_50);
Material materialSphereG = new PlasticMaterial(new Color3F(0.2F, 0.2F, 1.0F), Color3F.GRAY_0_50, Color3F.BLACK, 0.01F);
Material materialSphereH = new SubstrateMaterial(new Color3F(1.0F, 0.2F, 0.2F));

Shape3F shapePlane = new Plane3F();
Shape3F shapeSphereA = new Sphere3F();
Shape3F shapeSphereB = new Sphere3F();
Shape3F shapeSphereC = new Sphere3F();
Shape3F shapeSphereD = new Sphere3F();
Shape3F shapeSphereE = new Sphere3F();
Shape3F shapeSphereF = new Sphere3F();
Shape3F shapeSphereG = new Sphere3F();
Shape3F shapeSphereH = new Sphere3F();

Transform transformPlane = new Transform(new Point3F(0.0F, 0.0F, 0.0F));
Transform transformSphereA = new Transform(new Point3F(- 7.5F,  1.00F, 5.0F), Quaternion4F.from(Matrix44F.rotateX(AngleF.degrees(270.0F))));
Transform transformSphereB = new Transform(new Point3F(- 5.0F,  1.00F, 5.0F), Quaternion4F.from(Matrix44F.rotateX(AngleF.degrees(270.0F))));
Transform transformSphereC = new Transform(new Point3F(- 2.5F,  1.00F, 5.0F), Quaternion4F.from(Matrix44F.rotateX(AngleF.degrees(270.0F))));
Transform transformSphereD = new Transform(new Point3F(+ 0.0F,  1.00F, 5.0F), Quaternion4F.from(Matrix44F.rotateX(AngleF.degrees(270.0F))));
Transform transformSphereE = new Transform(new Point3F(+ 2.5F,  1.00F, 5.0F), Quaternion4F.from(Matrix44F.rotateX(AngleF.degrees(270.0F))));
Transform transformSphereF = new Transform(new Point3F(+ 5.0F,  1.00F, 5.0F), Quaternion4F.from(Matrix44F.rotateX(AngleF.degrees(270.0F))));
Transform transformSphereG = new Transform(new Point3F(+ 7.5F,  1.00F, 5.0F), Quaternion4F.from(Matrix44F.rotateX(AngleF.degrees(270.0F))));
Transform transformSphereH = new Transform(new Point3F(+10.0F,  1.00F, 5.0F), Quaternion4F.from(Matrix44F.rotateX(AngleF.degrees(270.0F))));

scene.addLight(new LDRImageLight(new PerezLight().toImage(800, 800)));
scene.addPrimitive(new Primitive(materialPlane, shapePlane, transformPlane));
scene.addPrimitive(new Primitive(materialSphereA, shapeSphereA, transformSphereA));
scene.addPrimitive(new Primitive(materialSphereB, shapeSphereB, transformSphereB));
scene.addPrimitive(new Primitive(materialSphereC, shapeSphereC, transformSphereC));
scene.addPrimitive(new Primitive(materialSphereD, shapeSphereD, transformSphereD));
scene.addPrimitive(new Primitive(materialSphereE, shapeSphereE, transformSphereE));
scene.addPrimitive(new Primitive(materialSphereF, shapeSphereF, transformSphereF));
scene.addPrimitive(new Primitive(materialSphereG, shapeSphereG, transformSphereG));
scene.addPrimitive(new Primitive(materialSphereH, shapeSphereH, transformSphereH));
scene.setCamera(new Camera(new Point3F(0.0F, 1.0F, -5.0F), AngleF.degrees(40.0F)));
scene.setName("GPURenderer");