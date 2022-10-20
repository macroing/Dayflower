Texture texture = new FunctionTexture(intersection -> {
	Point3F eye = intersection.getRay().getOrigin();
	Point3F lookAt = intersection.getSurfaceIntersectionPoint();
	
	float distanceSquared = Point3F.distanceSquared(eye, lookAt);
	float scale = 0.5F;
	float intensity = 1.0F / (distanceSquared * scale);
	
	return Color3F.maxTo1(Color3F.minTo0(new Color3F(intensity)));
});

Material material = new MatteMaterial(texture);

Shape3F shapePlane = new Plane3F();
Shape3F shapeSphere = new Sphere3F();

Transform transformPlane = new Transform(new Point3F(0.0F, 0.0F, 0.0F), Quaternion4F.from(Matrix44F.rotateX(AngleF.degrees(90.0F))));
Transform transformSphere = new Transform(new Point3F(- 7.5F,  1.00F, 5.0F), Quaternion4F.from(Matrix44F.rotateX(AngleF.degrees(270.0F))));

Camera camera = new Camera(new Point3F(0.0F, 1.0F, -5.0F));
camera.setResolution(2560.0F / 3.0F, 1440.0F / 3.0F);
camera.setFieldOfViewX(AngleF.degrees(40.0F));
camera.setFieldOfViewY();
camera.setSamplingCenter(true);

ImageF imageF = new IntImageF();
imageF.clear(Color3F.WHITE);

scene.addLight(new ImageLight(imageF));
scene.addPrimitive(new Primitive(material, shapePlane, transformPlane));
scene.addPrimitive(new Primitive(material, shapeSphere, transformSphere));
scene.setCamera(camera);
scene.setName("DepthCamera");