List<TriangleMesh3F> triangleMeshes = TriangleMesh3F.readWavefrontObject("./resources/models/SL500.obj", false, 100.0F, true);

Color3F colorA = new Color3F(0.8F);//new Color3F(0.3F, 0.7F, 0.3F);
Color3F colorB = new Color3F(0.5F);//new Color3F(0.1F, 0.5F, 0.1F);
Color3F colorC = new Color3F(0.5F);

Material material01 = new MatteMaterial(new ConstantTexture(new Color3F(20.0F)), new SimplexFractionalBrownianMotionTexture(new Color3F(0.25F, 0.75F, 0.25F))/*new ConstantTexture(Color3F.GRAY)*/);
Material material02 = new GlassMaterial(new ConstantTexture(new Color3F(1.5F)), new ConstantTexture(Color3F.WHITE), new ConstantTexture(Color3F.WHITE), new ConstantTexture(), new ConstantTexture(), false);
Material material03 = new SubstrateMaterial(new ConstantTexture(colorA), new ConstantTexture(new Color3F(0.5F)), new ConstantTexture(new Color3F(0.5F)), new ConstantTexture(new Color3F(0.5F)), true);
Material material04 = new MatteMaterial(new ConstantTexture(new Color3F(20.0F)), new ConstantTexture(new Color3F(0.1F)));
Material material05 = new MatteMaterial(new ConstantTexture(new Color3F(20.0F)), new ConstantTexture(new Color3F(0.1F)));
Material material06 = new MatteMaterial(new ConstantTexture(new Color3F(20.0F)), new ConstantTexture(new Color3F(0.1F)));
Material material07 = new SubstrateMaterial(new ConstantTexture(colorA), new ConstantTexture(new Color3F(0.5F)), new ConstantTexture(new Color3F(0.5F)), new ConstantTexture(new Color3F(0.5F)), true);
Material material08 = new MatteMaterial(new ConstantTexture(new Color3F(20.0F)), new ConstantTexture(Color3F.WHITE));
Material material09 = new GlassMaterial(new ConstantTexture(new Color3F(1.5F)), new ConstantTexture(Color3F.WHITE), new ConstantTexture(Color3F.WHITE), new ConstantTexture(), new ConstantTexture(), false);
Material material10 = new GlassMaterial(new ConstantTexture(new Color3F(1.5F)), new ConstantTexture(new Color3F(1.0F, 0.01F, 0.01F)), new ConstantTexture(new Color3F(1.0F, 0.01F, 0.01F)), new ConstantTexture(), new ConstantTexture(), false);
Material material11 = new GlassMaterial(new ConstantTexture(new Color3F(1.5F)), new ConstantTexture(Color3F.WHITE), new ConstantTexture(Color3F.WHITE), new ConstantTexture(), new ConstantTexture(), false);
Material material12 = new GlassMaterial(new ConstantTexture(new Color3F(1.5F)), new ConstantTexture(new Color3F(1.0F, 0.01F, 0.01F)), new ConstantTexture(new Color3F(1.0F, 0.01F, 0.01F)), new ConstantTexture(), new ConstantTexture(), false);
Material material13 = new GlassMaterial(new ConstantTexture(new Color3F(1.5F)), new ConstantTexture(new Color3F(1.0F, 0.01F, 0.01F)), new ConstantTexture(new Color3F(1.0F, 0.01F, 0.01F)), new ConstantTexture(), new ConstantTexture(), false);
Material material14 = new SubstrateMaterial(new ConstantTexture(colorB), new ConstantTexture(new Color3F(0.5F)), new ConstantTexture(new Color3F(0.5F)), new ConstantTexture(new Color3F(0.5F)), true);
Material material15 = new SubstrateMaterial(new ConstantTexture(colorA), new ConstantTexture(new Color3F(0.5F)), new ConstantTexture(new Color3F(0.5F)), new ConstantTexture(new Color3F(0.5F)), true);
Material material16 = new SubstrateMaterial(new ConstantTexture(colorC), new ConstantTexture(new Color3F(0.2F)), new ConstantTexture(new Color3F(0.2F)), new ConstantTexture(new Color3F(0.2F)), true);
Material material17 = new SubstrateMaterial(new ConstantTexture(colorC), new ConstantTexture(new Color3F(0.2F)), new ConstantTexture(new Color3F(0.2F)), new ConstantTexture(new Color3F(0.2F)), true);
Material material18 = new SubstrateMaterial(new ConstantTexture(colorC), new ConstantTexture(new Color3F(0.2F)), new ConstantTexture(new Color3F(0.2F)), new ConstantTexture(new Color3F(0.2F)), true);
Material material19 = new SubstrateMaterial(new ConstantTexture(colorC), new ConstantTexture(new Color3F(0.2F)), new ConstantTexture(new Color3F(0.2F)), new ConstantTexture(new Color3F(0.2F)), true);
Material material20 = new MatteMaterial(new ConstantTexture(new Color3F(20.0F)), new ConstantTexture(new Color3F(0.2F)));
Material material21 = new SubstrateMaterial(new ConstantTexture(colorA), new ConstantTexture(new Color3F(0.5F)), new ConstantTexture(new Color3F(0.5F)), new ConstantTexture(new Color3F(0.5F)), true);
Material material22 = new SubstrateMaterial(new ConstantTexture(colorC), new ConstantTexture(new Color3F(0.2F)), new ConstantTexture(new Color3F(0.2F)), new ConstantTexture(new Color3F(0.2F)), true);
Material material23 = new SubstrateMaterial(new ConstantTexture(colorC), new ConstantTexture(new Color3F(0.2F)), new ConstantTexture(new Color3F(0.2F)), new ConstantTexture(new Color3F(0.2F)), true);
Material material24 = new SubstrateMaterial(new ConstantTexture(colorC), new ConstantTexture(new Color3F(0.2F)), new ConstantTexture(new Color3F(0.2F)), new ConstantTexture(new Color3F(0.2F)), true);
Material material25 = new SubstrateMaterial(new ConstantTexture(colorC), new ConstantTexture(new Color3F(0.2F)), new ConstantTexture(new Color3F(0.2F)), new ConstantTexture(new Color3F(0.2F)), true);
Material material26 = new SubstrateMaterial(new ConstantTexture(colorC), new ConstantTexture(new Color3F(0.2F)), new ConstantTexture(new Color3F(0.2F)), new ConstantTexture(new Color3F(0.2F)), true);
Material material27 = new SubstrateMaterial(new ConstantTexture(colorC), new ConstantTexture(new Color3F(0.2F)), new ConstantTexture(new Color3F(0.2F)), new ConstantTexture(new Color3F(0.2F)), true);
Material material28 = new SubstrateMaterial(new ConstantTexture(colorC), new ConstantTexture(new Color3F(0.2F)), new ConstantTexture(new Color3F(0.2F)), new ConstantTexture(new Color3F(0.2F)), true);
Material material29 = new SubstrateMaterial(new ConstantTexture(colorC), new ConstantTexture(new Color3F(0.2F)), new ConstantTexture(new Color3F(0.2F)), new ConstantTexture(new Color3F(0.2F)), true);
Material material30 = new SubstrateMaterial(new ConstantTexture(colorC), new ConstantTexture(new Color3F(0.2F)), new ConstantTexture(new Color3F(0.2F)), new ConstantTexture(new Color3F(0.2F)), true);
Material material31 = new MatteMaterial(new ConstantTexture(new Color3F(20.0F)), new ConstantTexture(new Color3F(227, 161, 115)));
Material material32 = new GlassMaterial(new ConstantTexture(new Color3F(1.5F)), new ConstantTexture(Color3F.WHITE), new ConstantTexture(Color3F.WHITE), new ConstantTexture(), new ConstantTexture(), false);//new MatteMaterial(new ConstantTexture(new Color3F(20.0F)), new ConstantTexture(Color3F.WHITE));
Material material33 = new SubstrateMaterial(new ConstantTexture(colorB), new ConstantTexture(new Color3F(0.5F)), new ConstantTexture(new Color3F(0.5F)), new ConstantTexture(new Color3F(0.5F)), true);
Material material34 = new GlassMaterial(new ConstantTexture(new Color3F(1.5F)), new ConstantTexture(Color3F.WHITE), new ConstantTexture(Color3F.WHITE), new ConstantTexture(), new ConstantTexture(), false);
Material material35 = new GlassMaterial(new ConstantTexture(new Color3F(1.5F)), new ConstantTexture(Color3F.WHITE), new ConstantTexture(Color3F.WHITE), new ConstantTexture(), new ConstantTexture(), false);//new MatteMaterial(new ConstantTexture(new Color3F(20.0F)), new ConstantTexture(Color3F.WHITE));
Material material36 = new SubstrateMaterial(new ConstantTexture(colorB), new ConstantTexture(new Color3F(0.5F)), new ConstantTexture(new Color3F(0.5F)), new ConstantTexture(new Color3F(0.5F)), true);
Material material37 = new GlassMaterial(new ConstantTexture(new Color3F(1.5F)), new ConstantTexture(Color3F.WHITE), new ConstantTexture(Color3F.WHITE), new ConstantTexture(), new ConstantTexture(), false);
Material material38 = new SubstrateMaterial(new ConstantTexture(colorC), new ConstantTexture(new Color3F(0.2F)), new ConstantTexture(new Color3F(0.2F)), new ConstantTexture(new Color3F(0.2F)), true);
Material material39 = new SubstrateMaterial(new ConstantTexture(colorC), new ConstantTexture(new Color3F(0.2F)), new ConstantTexture(new Color3F(0.2F)), new ConstantTexture(new Color3F(0.2F)), true);
Material material40 = new SubstrateMaterial(new ConstantTexture(colorA), new ConstantTexture(new Color3F(0.5F)), new ConstantTexture(new Color3F(0.5F)), new ConstantTexture(new Color3F(0.5F)), true);
Material material41 = new SubstrateMaterial(new ConstantTexture(colorC), new ConstantTexture(new Color3F(0.2F)), new ConstantTexture(new Color3F(0.2F)), new ConstantTexture(new Color3F(0.2F)), true);
Material material42 = new SubstrateMaterial(new ConstantTexture(colorC), new ConstantTexture(new Color3F(0.2F)), new ConstantTexture(new Color3F(0.2F)), new ConstantTexture(new Color3F(0.2F)), true);
Material material43 = new SubstrateMaterial(new ConstantTexture(colorA), new ConstantTexture(new Color3F(0.5F)), new ConstantTexture(new Color3F(0.5F)), new ConstantTexture(new Color3F(0.5F)), true);
Material material44 = new MatteMaterial(new ConstantTexture(new Color3F(20.0F)), new ConstantTexture(new Color3F(98, 74, 46)));
Material material45 = new MirrorMaterial();
Material material46 = new MatteMaterial(new ConstantTexture(new Color3F(20.0F)), new ConstantTexture(new Color3F(98, 74, 46)));
Material material47 = new SubstrateMaterial(new ConstantTexture(colorA), new ConstantTexture(new Color3F(0.5F)), new ConstantTexture(new Color3F(0.5F)), new ConstantTexture(new Color3F(0.5F)), true);
Material material48 = new MirrorMaterial();
Material material49 = new SubstrateMaterial(new ConstantTexture(colorA), new ConstantTexture(new Color3F(0.5F)), new ConstantTexture(new Color3F(0.5F)), new ConstantTexture(new Color3F(0.5F)), true);
Material material50 = new SubstrateMaterial(new ConstantTexture(colorA), new ConstantTexture(new Color3F(0.5F)), new ConstantTexture(new Color3F(0.5F)), new ConstantTexture(new Color3F(0.5F)), true);
Material material51 = new MirrorMaterial();
Material material52 = new SubstrateMaterial(new ConstantTexture(colorA), new ConstantTexture(new Color3F(0.5F)), new ConstantTexture(new Color3F(0.5F)), new ConstantTexture(new Color3F(0.5F)), true);
Material material53 = new MatteMaterial(new ConstantTexture(new Color3F(20.0F)), new ConstantTexture(new Color3F(0.1F)));
Material material54 = new MatteMaterial(new ConstantTexture(new Color3F(20.0F)), new ConstantTexture(new Color3F(0.1F)));
Material material55 = new MatteMaterial(new ConstantTexture(new Color3F(20.0F)), new ConstantTexture(new Color3F(0.1F)));
Material material56 = new SubstrateMaterial(new ConstantTexture(colorC), new ConstantTexture(new Color3F(0.2F)), new ConstantTexture(new Color3F(0.2F)), new ConstantTexture(new Color3F(0.2F)), true);
Material material57 = new MatteMaterial(new ConstantTexture(new Color3F(20.0F)), new ConstantTexture(new Color3F(0.1F)));
Material material58 = new MatteMaterial(new ConstantTexture(new Color3F(20.0F)), new ConstantTexture(new Color3F(0.1F)));
Material material59 = new MatteMaterial(new ConstantTexture(new Color3F(20.0F)), new ConstantTexture(new Color3F(0.1F)));
Material material60 = new SubstrateMaterial(new ConstantTexture(colorC), new ConstantTexture(new Color3F(0.2F)), new ConstantTexture(new Color3F(0.2F)), new ConstantTexture(new Color3F(0.2F)), true);
Material material61 = new MatteMaterial(new ConstantTexture(new Color3F(20.0F)), new ConstantTexture(new Color3F(0.1F)));
Material material62 = new MatteMaterial(new ConstantTexture(new Color3F(20.0F)), new ConstantTexture(new Color3F(0.1F)));
Material material63 = new MatteMaterial(new ConstantTexture(new Color3F(20.0F)), new ConstantTexture(new Color3F(0.1F)));
Material material64 = new SubstrateMaterial(new ConstantTexture(colorC), new ConstantTexture(new Color3F(0.2F)), new ConstantTexture(new Color3F(0.2F)), new ConstantTexture(new Color3F(0.2F)), true);
Material material65 = new MatteMaterial(new ConstantTexture(new Color3F(20.0F)), new ConstantTexture(new Color3F(0.1F)));
Material material66 = new MatteMaterial(new ConstantTexture(new Color3F(20.0F)), new ConstantTexture(new Color3F(0.1F)));
Material material67 = new MatteMaterial(new ConstantTexture(new Color3F(20.0F)), new ConstantTexture(new Color3F(0.1F)));
Material material68 = new SubstrateMaterial(new ConstantTexture(colorC), new ConstantTexture(new Color3F(0.2F)), new ConstantTexture(new Color3F(0.2F)), new ConstantTexture(new Color3F(0.2F)), true);
Material material69 = new MatteMaterial(new ConstantTexture(new Color3F(20.0F)), new ConstantTexture(Color3F.WHITE));

Shape3F shape01 = new Plane3F();
Shape3F shape02 = triangleMeshes.get( 0);
Shape3F shape03 = triangleMeshes.get( 1);
Shape3F shape04 = triangleMeshes.get( 2);
Shape3F shape05 = triangleMeshes.get( 3);
Shape3F shape06 = triangleMeshes.get( 4);
Shape3F shape07 = triangleMeshes.get( 5);
Shape3F shape08 = triangleMeshes.get( 6);
Shape3F shape09 = triangleMeshes.get( 7);
Shape3F shape10 = triangleMeshes.get( 8);
Shape3F shape11 = triangleMeshes.get( 9);
Shape3F shape12 = triangleMeshes.get(10);
Shape3F shape13 = triangleMeshes.get(11);
Shape3F shape14 = triangleMeshes.get(12);
Shape3F shape15 = triangleMeshes.get(13);
Shape3F shape16 = triangleMeshes.get(14);
Shape3F shape17 = triangleMeshes.get(15);
Shape3F shape18 = triangleMeshes.get(16);
Shape3F shape19 = triangleMeshes.get(17);
Shape3F shape20 = triangleMeshes.get(18);
Shape3F shape21 = triangleMeshes.get(19);
Shape3F shape22 = triangleMeshes.get(20);
Shape3F shape23 = triangleMeshes.get(21);
Shape3F shape24 = triangleMeshes.get(22);
Shape3F shape25 = triangleMeshes.get(23);
Shape3F shape26 = triangleMeshes.get(24);
Shape3F shape27 = triangleMeshes.get(25);
Shape3F shape28 = triangleMeshes.get(26);
Shape3F shape29 = triangleMeshes.get(27);
Shape3F shape30 = triangleMeshes.get(28);
Shape3F shape31 = triangleMeshes.get(29);
Shape3F shape32 = triangleMeshes.get(30);
Shape3F shape33 = triangleMeshes.get(31);
Shape3F shape34 = triangleMeshes.get(32);
Shape3F shape35 = triangleMeshes.get(33);
Shape3F shape36 = triangleMeshes.get(34);
Shape3F shape37 = triangleMeshes.get(35);
Shape3F shape38 = triangleMeshes.get(36);
Shape3F shape39 = triangleMeshes.get(37);
Shape3F shape40 = triangleMeshes.get(38);
Shape3F shape41 = triangleMeshes.get(39);
Shape3F shape42 = triangleMeshes.get(40);
Shape3F shape43 = triangleMeshes.get(41);
Shape3F shape44 = triangleMeshes.get(42);
Shape3F shape45 = triangleMeshes.get(43);
Shape3F shape46 = triangleMeshes.get(44);
Shape3F shape47 = triangleMeshes.get(45);
Shape3F shape48 = triangleMeshes.get(46);
Shape3F shape49 = triangleMeshes.get(47);
Shape3F shape50 = triangleMeshes.get(48);
Shape3F shape51 = triangleMeshes.get(49);
Shape3F shape52 = triangleMeshes.get(50);
Shape3F shape53 = triangleMeshes.get(51);
Shape3F shape54 = triangleMeshes.get(52);
Shape3F shape55 = triangleMeshes.get(53);
Shape3F shape56 = triangleMeshes.get(54);
Shape3F shape57 = triangleMeshes.get(55);
Shape3F shape58 = triangleMeshes.get(56);
Shape3F shape59 = triangleMeshes.get(57);
Shape3F shape60 = triangleMeshes.get(58);
Shape3F shape61 = triangleMeshes.get(59);
Shape3F shape62 = triangleMeshes.get(60);
Shape3F shape63 = triangleMeshes.get(61);
Shape3F shape64 = triangleMeshes.get(62);
Shape3F shape65 = triangleMeshes.get(63);
Shape3F shape66 = triangleMeshes.get(64);
Shape3F shape67 = triangleMeshes.get(65);
Shape3F shape68 = triangleMeshes.get(66);
Shape3F shape69 = triangleMeshes.get(67);

Transform transform01 = new Transform();
Transform transformNM = new Transform(new Point3F(0.0F, 0.0F, 0.0F), Quaternion4F.from(Matrix44F.multiply(Matrix44F.rotateY(AngleF.degrees(90.0F)), Matrix44F.rotateX(AngleF.degrees(270.0F)))), new Vector3F(0.01F));

Camera camera = new Camera(new Point3F(0.0F, 2.0F, -10.0F), AngleF.degrees(40.0F));

scene.addLight(new PerezLight());
scene.addPrimitive(new Primitive(material01, shape01, new ConstantTexture(), new ConstantTexture(), transform01));
scene.addPrimitive(new Primitive(material02, shape02, new ConstantTexture(), new ConstantTexture(), transformNM));//Base			wind_glass
scene.addPrimitive(new Primitive(material03, shape03, new ConstantTexture(), new ConstantTexture(), transformNM));//Base			Body_paint
scene.addPrimitive(new Primitive(material04, shape04, new ConstantTexture(), new ConstantTexture(), transformNM));//Base			Misc
scene.addPrimitive(new Primitive(material05, shape05, new ConstantTexture(), new ConstantTexture(), transformNM));//Base			Misc0
scene.addPrimitive(new Primitive(material06, shape06, new ConstantTexture(), new ConstantTexture(), transformNM));//Base			Material__583
scene.addPrimitive(new Primitive(material07, shape07, new ConstantTexture(), new ConstantTexture(), transformNM));//Base			Body_paint
scene.addPrimitive(new Primitive(material08, shape08, new ConstantTexture(), new ConstantTexture(), transformNM));//Base			License
scene.addPrimitive(new Primitive(material09, shape09, new ConstantTexture(), new ConstantTexture(), transformNM));//Base			wind_glass
scene.addPrimitive(new Primitive(material10, shape10, new ConstantTexture(), new ConstantTexture(), transformNM));//Base			Material__586
scene.addPrimitive(new Primitive(material11, shape11, new ConstantTexture(), new ConstantTexture(), transformNM));//Base			wind_glass
scene.addPrimitive(new Primitive(material12, shape12, new ConstantTexture(), new ConstantTexture(), transformNM));//BLightL		Material__589
scene.addPrimitive(new Primitive(material13, shape13, new ConstantTexture(), new ConstantTexture(), transformNM));//BLightR		Material__589
scene.addPrimitive(new Primitive(material14, shape14, new ConstantTexture(), new ConstantTexture(), transformNM));//Body			DoorLine
scene.addPrimitive(new Primitive(material15, shape15, new ConstantTexture(), new ConstantTexture(), transformNM));//Body			Badging_Chrome
scene.addPrimitive(new Primitive(material16, shape16, new ConstantTexture(), new ConstantTexture(), transformNM));//Body			Misc1
scene.addPrimitive(new Primitive(material17, shape17, new ConstantTexture(), new ConstantTexture(), transformNM));//Body			Misc_Chrome
scene.addPrimitive(new Primitive(material18, shape18, new ConstantTexture(), new ConstantTexture(), transformNM));//Body			Misc_Chrome0
scene.addPrimitive(new Primitive(material19, shape19, new ConstantTexture(), new ConstantTexture(), transformNM));//Body			Misc_Chrome1
scene.addPrimitive(new Primitive(material20, shape20, new ConstantTexture(), new ConstantTexture(), transformNM));//Body			Black
scene.addPrimitive(new Primitive(material21, shape21, new ConstantTexture(), new ConstantTexture(), transformNM));//Body			Body_paint0
scene.addPrimitive(new Primitive(material22, shape22, new ConstantTexture(), new ConstantTexture(), transformNM));//Body			Bottom
scene.addPrimitive(new Primitive(material23, shape23, new ConstantTexture(), new ConstantTexture(), transformNM));//BrakeFL		Brake_Pads
scene.addPrimitive(new Primitive(material24, shape24, new ConstantTexture(), new ConstantTexture(), transformNM));//BrakeFL		Brake_Disc
scene.addPrimitive(new Primitive(material25, shape25, new ConstantTexture(), new ConstantTexture(), transformNM));//BrakeFR		Brake_Pads0
scene.addPrimitive(new Primitive(material26, shape26, new ConstantTexture(), new ConstantTexture(), transformNM));//BrakeFR		Brake_Disc0
scene.addPrimitive(new Primitive(material27, shape27, new ConstantTexture(), new ConstantTexture(), transformNM));//BrakeRL		Brake_Pads
scene.addPrimitive(new Primitive(material28, shape28, new ConstantTexture(), new ConstantTexture(), transformNM));//BrakeRL		Brake_Disc
scene.addPrimitive(new Primitive(material29, shape29, new ConstantTexture(), new ConstantTexture(), transformNM));//BrakeRR		Brake_Pads0
scene.addPrimitive(new Primitive(material30, shape30, new ConstantTexture(), new ConstantTexture(), transformNM));//BrakeRR		Brake_Disc0
scene.addPrimitive(new Primitive(material31, shape31, new ConstantTexture(), new ConstantTexture(), transformNM));//Driver			Driver
scene.addPrimitive(new Primitive(material32, shape32, new ConstantTexture(), new ConstantTexture(), transformNM));//HLightL		Material__593
scene.addPrimitive(new Primitive(material33, shape33, new ConstantTexture(), new ConstantTexture(), transformNM));//HLightL		Misc2
scene.addPrimitive(new Primitive(material34, shape34, new ConstantTexture(), new ConstantTexture(), transformNM));//HLightLG		Material__594
scene.addPrimitive(new Primitive(material35, shape35, new ConstantTexture(), new ConstantTexture(), transformNM));//HLightR		Material__593
scene.addPrimitive(new Primitive(material36, shape36, new ConstantTexture(), new ConstantTexture(), transformNM));//HLightR		Misc2
scene.addPrimitive(new Primitive(material37, shape37, new ConstantTexture(), new ConstantTexture(), transformNM));//HLightRG		Material__594
scene.addPrimitive(new Primitive(material38, shape38, new ConstantTexture(), new ConstantTexture(), transformNM));//Hood			Misc3
scene.addPrimitive(new Primitive(material39, shape39, new ConstantTexture(), new ConstantTexture(), transformNM));//Hood			Misc_Chrome2
scene.addPrimitive(new Primitive(material40, shape40, new ConstantTexture(), new ConstantTexture(), transformNM));//Hood			Body_paint1
scene.addPrimitive(new Primitive(material41, shape41, new ConstantTexture(), new ConstantTexture(), transformNM));//Hood_Carbon	Misc4
scene.addPrimitive(new Primitive(material42, shape42, new ConstantTexture(), new ConstantTexture(), transformNM));//Hood_Carbon	Misc_Chrome3
scene.addPrimitive(new Primitive(material43, shape43, new ConstantTexture(), new ConstantTexture(), transformNM));//Hood_Carbon	Body_paint2
scene.addPrimitive(new Primitive(material44, shape44, new ConstantTexture(), new ConstantTexture(), transformNM));//Interior		Interior
scene.addPrimitive(new Primitive(material45, shape45, new ConstantTexture(), new ConstantTexture(), transformNM));//Interior		Material__597
scene.addPrimitive(new Primitive(material46, shape46, new ConstantTexture(), new ConstantTexture(), transformNM));//Interior		Interior0
scene.addPrimitive(new Primitive(material47, shape47, new ConstantTexture(), new ConstantTexture(), transformNM));//MirrorL		Misc_Chrome4
scene.addPrimitive(new Primitive(material48, shape48, new ConstantTexture(), new ConstantTexture(), transformNM));//MirrorL		Material__598
scene.addPrimitive(new Primitive(material49, shape49, new ConstantTexture(), new ConstantTexture(), transformNM));//MirrorL		Body_paint3
scene.addPrimitive(new Primitive(material50, shape50, new ConstantTexture(), new ConstantTexture(), transformNM));//MirrorR		Misc_Chrome4
scene.addPrimitive(new Primitive(material51, shape51, new ConstantTexture(), new ConstantTexture(), transformNM));//MirrorR		Material__598
scene.addPrimitive(new Primitive(material52, shape52, new ConstantTexture(), new ConstantTexture(), transformNM));//MirrorR		Body_paint3
scene.addPrimitive(new Primitive(material53, shape53, new ConstantTexture(), new ConstantTexture(), transformNM));//TireFL			Tire_Back
scene.addPrimitive(new Primitive(material54, shape54, new ConstantTexture(), new ConstantTexture(), transformNM));//TireFL			Tire_Tread
scene.addPrimitive(new Primitive(material55, shape55, new ConstantTexture(), new ConstantTexture(), transformNM));//TireFL			Tire_Sidewall
scene.addPrimitive(new Primitive(material56, shape56, new ConstantTexture(), new ConstantTexture(), transformNM));//TireFL			Material__600
scene.addPrimitive(new Primitive(material57, shape57, new ConstantTexture(), new ConstantTexture(), transformNM));//TireFR			Tire_Back
scene.addPrimitive(new Primitive(material58, shape58, new ConstantTexture(), new ConstantTexture(), transformNM));//TireFR			Tire_Tread
scene.addPrimitive(new Primitive(material59, shape59, new ConstantTexture(), new ConstantTexture(), transformNM));//TireFR			Tire_Sidewall
scene.addPrimitive(new Primitive(material60, shape60, new ConstantTexture(), new ConstantTexture(), transformNM));//TireFR			Material__600
scene.addPrimitive(new Primitive(material61, shape61, new ConstantTexture(), new ConstantTexture(), transformNM));//TireRL			Tire_Back
scene.addPrimitive(new Primitive(material62, shape62, new ConstantTexture(), new ConstantTexture(), transformNM));//TireRL			Tire_Tread
scene.addPrimitive(new Primitive(material63, shape63, new ConstantTexture(), new ConstantTexture(), transformNM));//TireRL			Tire_Sidewall
scene.addPrimitive(new Primitive(material64, shape64, new ConstantTexture(), new ConstantTexture(), transformNM));//TireRL			Material__600
scene.addPrimitive(new Primitive(material65, shape65, new ConstantTexture(), new ConstantTexture(), transformNM));//TireRR			Tire_Back
scene.addPrimitive(new Primitive(material66, shape66, new ConstantTexture(), new ConstantTexture(), transformNM));//TireRR			Tire_Tread
scene.addPrimitive(new Primitive(material67, shape67, new ConstantTexture(), new ConstantTexture(), transformNM));//TireRR			Tire_Sidewall
scene.addPrimitive(new Primitive(material68, shape68, new ConstantTexture(), new ConstantTexture(), transformNM));//TireRR			Material__600
scene.addPrimitive(new Primitive(material69, shape69, new ConstantTexture(), new ConstantTexture(), transformNM));//License		License0
scene.setCamera(camera);
scene.setName("PBRTSL500");
scene.buildAccelerationStructure();