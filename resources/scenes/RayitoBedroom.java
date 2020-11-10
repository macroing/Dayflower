List<TriangleMesh3F> triangleMeshes = TriangleMesh3F.readWavefrontObject("./resources/models/Bedroom.obj", true);

Material material = new LambertianMaterial();

Texture textureAlbedo = new ConstantTexture(Color3F.GRAY);
Texture textureEmittance = new ConstantTexture();

Texture textureLaminate = ImageTexture.load("./resources/textures/laminate.jpg");
Texture textureWall = ImageTexture.load("./resources/textures/Wall.jpg");

Map<String, Texture> textures = new HashMap<>();

textures.put("Ceiling", textureWall);
textures.put("Floor", textureLaminate);
textures.put("Walls", textureWall);

Matrix44F matrix = Matrix44F.translate(0.0F, 0.0F, 20.0F);

Primitive primitive = new Primitive(new LambertianMaterial(), new Sphere3F(), new ConstantTexture(Color3F.WHITE), new ConstantTexture(Color3F.WHITE), Matrix44F.translate(0.0F, 30.0F, 20.0F));

scene.addLight(new PointLight(new Point3F(0.0F, 30.0F, 20.0F)));
scene.addLight(new PrimitiveLight(primitive));
scene.addPrimitive(primitive);

for(final TriangleMesh3F triangleMesh : triangleMeshes) {
	scene.addPrimitive(new Primitive(material, triangleMesh, textures.getOrDefault(triangleMesh.getGroupName(), textureAlbedo), textureEmittance, matrix));
}

scene.setCamera(new Camera(new Point3F(0.0F, 30.0F, 0.0F)));
scene.setName("RayitoBedroom");