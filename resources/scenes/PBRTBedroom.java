List<TriangleMesh3F> triangleMeshes = TriangleMesh3F.readWavefrontObject("./resources/models/Bedroom.obj", true);

Material material = new MattePBRTMaterial(new ConstantTexture(new Color3F(20.0F)), new ConstantTexture(Color3F.WHITE));
Material materialLaminate = new SubstratePBRTMaterial(ImageTexture.load("./resources/textures/laminate.jpg"), new ConstantTexture(new Color3F(0.2F)), new ConstantTexture(new Color3F(0.2F)), new ConstantTexture(new Color3F(0.2F)), true);
Material materialWall = new MattePBRTMaterial(new ConstantTexture(new Color3F(20.0F)), ImageTexture.load("./resources/textures/Wall.jpg"));

Map<String, Material> materials = new HashMap<>();

materials.put("Ceiling", materialWall);
materials.put("Floor", materialLaminate);
materials.put("Walls", materialWall);

Transform transform1 = new Transform(new Point3F(0.0F,  0.0F, 20.0F));
Transform transform2 = new Transform(new Point3F(0.0F, 40.0F, 20.0F));

Shape3F shape = new Sphere3F();

AreaLight areaLight = new DiffuseAreaLight(transform2.getObjectToWorld(), 1, new Color3F(500.0F), shape, false);

scene.addLight(areaLight);
scene.addPrimitive(new Primitive(new MattePBRTMaterial(new ConstantTexture(new Color3F(0.0F)), new ConstantTexture(Color3F.WHITE)), shape, transform2, areaLight));

for(final TriangleMesh3F triangleMesh : triangleMeshes) {
	scene.addPrimitive(new Primitive(materials.getOrDefault(triangleMesh.getGroupName(), material), triangleMesh, transform1));
}

scene.setCamera(new Camera(new Point3F(0.0F, 30.0F, 0.0F), AngleF.degrees(70.0F)));
scene.setName("PBRTBedroom");
scene.buildAccelerationStructure();