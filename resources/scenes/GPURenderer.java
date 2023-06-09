parameterList.addFloat("Turbidity", 2.0F);
parameterList.addFloat("Sun Direction X", +1.0F);
parameterList.addFloat("Sun Direction Y", +1.0F);
parameterList.addFloat("Sun Direction Z", -1.0F);
parameterList.load();

final float turbidity = parameterList.getFloat("Turbidity", 2.0F);

final float sunDirectionWorldSpaceX = parameterList.getFloat("Sun Direction X", +1.0F);
final float sunDirectionWorldSpaceY = parameterList.getFloat("Sun Direction Y", +1.0F);
final float sunDirectionWorldSpaceZ = parameterList.getFloat("Sun Direction Z", -1.0F);

Texture textureCheckerboardGray = new CheckerboardTexture();
Texture textureCheckerboardRed = new CheckerboardTexture(new Color3F(1.0F, 0.01F, 0.01F), Color3F.WHITE, AngleF.degrees(0.0F), new Vector2F(4.0F, 4.0F));

Material materialPlane = new MatteMaterial(new CheckerboardTexture());
Material materialSphereA = new ClearCoatMaterial(new Color3F(1.0F, 0.01F, 0.01F));
Material materialSphereB = new DisneyMaterial(new Color3F(255, 127, 80), Color3F.BLACK, Color3F.BLACK, 0.4F, 1.75F, 1.0F, 1.0F, 1.2F, 0.0F, 0.39F, 0.475F, 0.1F, 0.5F, 0.5F, 0.0F);
Material materialSphereC = new GlassMaterial(Color3F.WHITE, new Color3F(1.0F, 1.0F, 0.5F));
Material materialSphereD = new GlossyMaterial(Color3F.GRAY, Color3F.BLACK, 0.2F);
Material materialSphereE = new MatteMaterial(new Color3F(0.1F, 1.0F, 0.1F));
Material materialSphereF = new MetalMaterial();
Material materialSphereG = new MirrorMaterial(Color3F.GRAY);
Material materialSphereH = new PlasticMaterial();
Material materialSphereI = new SubstrateMaterial(new Color3F(1.0F, 0.2F, 0.2F));
Material materialSphereJ = new TranslucentMaterial(new Color3F(0.75F, 0.25F, 0.25F), new Color3F(0.75F), Color3F.BLACK, 0.01F, Color3F.GRAY, Color3F.GRAY, true, new NoOpModifier());
Material materialSphereK = new UberMaterial(new Color3F(0.25F, 0.75F, 0.25F));

Shape3F shapePlane = new Plane3F();
Shape3F shapeSphereA = new Sphere3F();
Shape3F shapeSphereB = new Sphere3F();
Shape3F shapeSphereC = new Sphere3F();
Shape3F shapeSphereD = new Sphere3F();
Shape3F shapeSphereE = new Sphere3F();
Shape3F shapeSphereF = new Sphere3F();
Shape3F shapeSphereG = new Sphere3F();
Shape3F shapeSphereH = new Sphere3F();
Shape3F shapeSphereI = new Sphere3F();
Shape3F shapeSphereJ = new Sphere3F();
Shape3F shapeSphereK = new Sphere3F();

Transform transformPlane = new Transform(new Point3F(0.0F, 0.0F, 0.0F), Quaternion4F.from(Matrix44F.rotateX(AngleF.degrees(90.0F))));
Transform transformSphereA = new Transform(new Point3F(- 7.5F,  1.00F, 5.0F), Quaternion4F.from(Matrix44F.rotateX(AngleF.degrees(270.0F))));
Transform transformSphereB = new Transform(new Point3F(- 5.0F,  1.00F, 5.0F), Quaternion4F.from(Matrix44F.rotateX(AngleF.degrees(270.0F))));
Transform transformSphereC = new Transform(new Point3F(- 2.5F,  1.00F, 5.0F), Quaternion4F.from(Matrix44F.rotateX(AngleF.degrees(270.0F))));
Transform transformSphereD = new Transform(new Point3F(+ 0.0F,  1.00F, 5.0F), Quaternion4F.from(Matrix44F.rotateX(AngleF.degrees(270.0F))));
Transform transformSphereE = new Transform(new Point3F(+ 2.5F,  1.00F, 5.0F), Quaternion4F.from(Matrix44F.rotateX(AngleF.degrees(270.0F))));
Transform transformSphereF = new Transform(new Point3F(+ 5.0F,  1.00F, 5.0F), Quaternion4F.from(Matrix44F.rotateX(AngleF.degrees(270.0F))));
Transform transformSphereG = new Transform(new Point3F(+ 7.5F,  1.00F, 5.0F), Quaternion4F.from(Matrix44F.rotateX(AngleF.degrees(270.0F))));
Transform transformSphereH = new Transform(new Point3F(+10.0F,  1.00F, 5.0F), Quaternion4F.from(Matrix44F.rotateX(AngleF.degrees(270.0F))));
Transform transformSphereI = new Transform(new Point3F(+12.5F,  1.00F, 5.0F), Quaternion4F.from(Matrix44F.rotateX(AngleF.degrees(270.0F))));
Transform transformSphereJ = new Transform(new Point3F(+15.0F,  1.00F, 5.0F), Quaternion4F.from(Matrix44F.rotateX(AngleF.degrees(270.0F))));
Transform transformSphereK = new Transform(new Point3F(+17.5F,  1.00F, 5.0F), Quaternion4F.from(Matrix44F.rotateX(AngleF.degrees(270.0F))));

final
Camera camera = new Camera(new Point3F(0.0F, 1.0F, -5.0F));
camera.setResolution(2560.0F / 3.0F, 1440.0F / 3.0F);
camera.setFieldOfViewX(AngleF.degrees(40.0F));
camera.setFieldOfViewY();

//scene.addLight(new DirectionalLight());
//scene.addLight(new LDRImageLight(new PerezLight().toImage(800, 800)));
scene.addLight(new PerezLight(turbidity, new Vector3F(sunDirectionWorldSpaceX, sunDirectionWorldSpaceY, sunDirectionWorldSpaceZ)));
//scene.addLight(new PointLight(new Color3F(50.0F), new Point3F(0.0F, 5.0F, 5.0F)));
//scene.addLight(new SpotLight(AngleF.degrees(50.0F), AngleF.degrees(10.0F), new Color3F(50.0F), new Point3F(0.0F, 2.0F, -5.0F), Vector3F.z()));
scene.addPrimitive(new Primitive(materialPlane, shapePlane, transformPlane));
scene.addPrimitive(new Primitive(materialSphereA, shapeSphereA, transformSphereA));
scene.addPrimitive(new Primitive(materialSphereB, shapeSphereB, transformSphereB));
scene.addPrimitive(new Primitive(materialSphereC, shapeSphereC, transformSphereC));
scene.addPrimitive(new Primitive(materialSphereD, shapeSphereD, transformSphereD));
scene.addPrimitive(new Primitive(materialSphereE, shapeSphereE, transformSphereE));
scene.addPrimitive(new Primitive(materialSphereF, shapeSphereF, transformSphereF));
scene.addPrimitive(new Primitive(materialSphereG, shapeSphereG, transformSphereG));
scene.addPrimitive(new Primitive(materialSphereH, shapeSphereH, transformSphereH));
scene.addPrimitive(new Primitive(materialSphereI, shapeSphereI, transformSphereI));
scene.addPrimitive(new Primitive(materialSphereJ, shapeSphereJ, transformSphereJ));
scene.addPrimitive(new Primitive(materialSphereK, shapeSphereK, transformSphereK));
scene.setCamera(camera);
scene.setName("GPURenderer");