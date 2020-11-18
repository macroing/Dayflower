List<TriangleMesh3F> triangleMeshes = TriangleMesh3F.readWavefrontObject("./resources/models/Bedroom.obj", true);

Material material = new MatteMaterial(new ConstantTexture(new Color3F(20.0F)), new ConstantTexture(Color3F.WHITE));
Material materialLaminate = new SubstrateMaterial(ImageTexture.load("./resources/textures/laminate.jpg"), new ConstantTexture(new Color3F(0.2F)), new ConstantTexture(new Color3F(0.2F)), new ConstantTexture(new Color3F(0.2F)), true);
Material materialWall = new MatteMaterial(new ConstantTexture(new Color3F(20.0F)), ImageTexture.load("./resources/textures/Wall.jpg"));

Map<String, Material> materials = new HashMap<>();

materials.put("Ceiling", materialWall);
materials.put("Floor", materialLaminate);
materials.put("Walls", materialWall);

Texture textureAlbedo = new ConstantTexture(Color3F.GRAY);
Texture textureEmittance = new ConstantTexture();

Matrix44F matrix1 = Matrix44F.translate(0.0F,  0.0F, 20.0F);
Matrix44F matrix2 = Matrix44F.translate(0.0F, 40.0F, 20.0F);

Shape3F shape = new Sphere3F();

AreaLight areaLight = new DiffuseAreaLight(matrix2, 1, new Color3F(500.0F), shape, false);

scene.addLight(areaLight);
scene.addPrimitive(new Primitive(new MatteMaterial(new ConstantTexture(new Color3F(0.0F)), new ConstantTexture(Color3F.WHITE)), shape, new ConstantTexture(), new ConstantTexture(), matrix2, areaLight));

for(final TriangleMesh3F triangleMesh : triangleMeshes) {
	scene.addPrimitive(new Primitive(materials.getOrDefault(triangleMesh.getGroupName(), material), triangleMesh, textureAlbedo, textureEmittance, matrix1));
}

scene.setCamera(new Camera(new Point3F(0.0F, 30.0F, 0.0F), AngleF.degrees(70.0F)));
scene.setName("PBRTBedroom");
scene.buildAccelerationStructure();