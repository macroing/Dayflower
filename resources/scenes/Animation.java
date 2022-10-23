Material materialPlane = new MatteMaterial(Color3F.GRAY);
Material materialTorus = new MatteMaterial(new Color3F(1.0F, 0.1F, 0.1F));

Shape3F shapePlane = new Plane3F();
Shape3F shapeTorus = new Torus3F();

Transform transformPlane = new Transform(new Point3F(0.0F, 0.0F, 0.0F), Quaternion4F.from(Matrix44F.rotateX(AngleF.degrees(90.0F))));
Transform transformTorus = new Transform(new Point3F(0.0F, 1.25F, 0.0F));

Camera camera = new Camera(new Point3F(0.0F, 1.0F, -5.0F));
camera.setResolution(2560.0F / 3.0F, 1440.0F / 3.0F);
camera.setFieldOfViewX(AngleF.degrees(40.0F));
camera.setFieldOfViewY();
camera.setSamplingCenter(true);

SceneObserver sceneObserver = new AbstractSceneObserver() {
	@Override
	public boolean onUpdate(Scene scene, float delta) {
		transformTorus.rotate(Quaternion4F.from(AngleF.radians(delta), Vector3F.y()));
		
		return true;
	}
};

scene.addLight(new PerezLight());
scene.addPrimitive(new Primitive(materialPlane, shapePlane, transformPlane));
scene.addPrimitive(new Primitive(materialTorus, shapeTorus, transformTorus));
scene.addSceneObserver(sceneObserver);
scene.setCamera(camera);
scene.setName("Animation");