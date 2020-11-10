List<TriangleMesh3F> triangleMeshes = TriangleMesh3F.readWavefrontObject("./resources/models/SL500.obj", false);

Color3F colorA = new Color3F(0.3F, 0.7F, 0.3F);
Color3F colorB = new Color3F(0.1F, 0.5F, 0.1F);
Color3F colorC = Color3F.GRAY;

Material material01 = new LambertianMaterial();
Material material02 = new RefractionMaterial();
Material material03 = new AshikhminShirleyMaterial(0.05F);
Material material04 = new OrenNayarMaterial(AngleF.degrees(20.0F));
Material material05 = new OrenNayarMaterial(AngleF.degrees(20.0F));
Material material06 = new OrenNayarMaterial(AngleF.degrees(20.0F));
Material material07 = new AshikhminShirleyMaterial(0.05F);
Material material08 = new OrenNayarMaterial(AngleF.degrees(20.0F));
Material material09 = new RefractionMaterial();
Material material10 = new RefractionMaterial();
Material material11 = new RefractionMaterial();
Material material12 = new RefractionMaterial();
Material material13 = new RefractionMaterial();
Material material14 = new AshikhminShirleyMaterial(0.05F);
Material material15 = new AshikhminShirleyMaterial(0.05F);
Material material16 = new AshikhminShirleyMaterial(0.02F);
Material material17 = new AshikhminShirleyMaterial(0.02F);
Material material18 = new AshikhminShirleyMaterial(0.02F);
Material material19 = new AshikhminShirleyMaterial(0.02F);
Material material20 = new OrenNayarMaterial(AngleF.degrees(20.0F));
Material material21 = new AshikhminShirleyMaterial(0.05F);
Material material22 = new AshikhminShirleyMaterial(0.02F);
Material material23 = new AshikhminShirleyMaterial(0.02F);
Material material24 = new AshikhminShirleyMaterial(0.02F);
Material material25 = new AshikhminShirleyMaterial(0.02F);
Material material26 = new AshikhminShirleyMaterial(0.02F);
Material material27 = new AshikhminShirleyMaterial(0.02F);
Material material28 = new AshikhminShirleyMaterial(0.02F);
Material material29 = new AshikhminShirleyMaterial(0.02F);
Material material30 = new AshikhminShirleyMaterial(0.02F);
Material material31 = new OrenNayarMaterial(AngleF.degrees(20.0F));
Material material32 = new OrenNayarMaterial(AngleF.degrees(20.0F));
Material material33 = new AshikhminShirleyMaterial(0.05F);
Material material34 = new RefractionMaterial();
Material material35 = new OrenNayarMaterial(AngleF.degrees(20.0F));
Material material36 = new AshikhminShirleyMaterial(0.05F);
Material material37 = new RefractionMaterial();
Material material38 = new AshikhminShirleyMaterial(0.02F);
Material material39 = new AshikhminShirleyMaterial(0.02F);
Material material40 = new AshikhminShirleyMaterial(0.05F);
Material material41 = new AshikhminShirleyMaterial(0.02F);
Material material42 = new AshikhminShirleyMaterial(0.02F);
Material material43 = new AshikhminShirleyMaterial(0.05F);
Material material44 = new OrenNayarMaterial(AngleF.degrees(20.0F));
Material material45 = new ReflectionMaterial();
Material material46 = new OrenNayarMaterial(AngleF.degrees(20.0F));
Material material47 = new AshikhminShirleyMaterial(0.02F);
Material material48 = new ReflectionMaterial();
Material material49 = new AshikhminShirleyMaterial(0.05F);
Material material50 = new AshikhminShirleyMaterial(0.02F);
Material material51 = new ReflectionMaterial();
Material material52 = new AshikhminShirleyMaterial(0.05F);
Material material53 = new OrenNayarMaterial(AngleF.degrees(20.0F));
Material material54 = new OrenNayarMaterial(AngleF.degrees(20.0F));
Material material55 = new OrenNayarMaterial(AngleF.degrees(20.0F));
Material material56 = new AshikhminShirleyMaterial(0.02F);
Material material57 = new OrenNayarMaterial(AngleF.degrees(20.0F));
Material material58 = new OrenNayarMaterial(AngleF.degrees(20.0F));
Material material59 = new OrenNayarMaterial(AngleF.degrees(20.0F));
Material material60 = new AshikhminShirleyMaterial(0.02F);
Material material61 = new OrenNayarMaterial(AngleF.degrees(20.0F));
Material material62 = new OrenNayarMaterial(AngleF.degrees(20.0F));
Material material63 = new OrenNayarMaterial(AngleF.degrees(20.0F));
Material material64 = new AshikhminShirleyMaterial(0.02F);
Material material65 = new OrenNayarMaterial(AngleF.degrees(20.0F));
Material material66 = new OrenNayarMaterial(AngleF.degrees(20.0F));
Material material67 = new OrenNayarMaterial(AngleF.degrees(20.0F));
Material material68 = new AshikhminShirleyMaterial(0.02F);
Material material69 = new OrenNayarMaterial(AngleF.degrees(20.0F));

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

Texture texture011 = new ConstantTexture(Color3F.GRAY);
Texture texture012 = new ConstantTexture();
Texture texture021 = new ConstantTexture(Color3F.WHITE);
Texture texture022 = new ConstantTexture();
Texture texture031 = new ConstantTexture(colorA);
Texture texture032 = new ConstantTexture();
Texture texture041 = new ConstantTexture(new Color3F(0.1F));
Texture texture042 = new ConstantTexture();
Texture texture051 = new ConstantTexture(new Color3F(0.1F));
Texture texture052 = new ConstantTexture();
Texture texture061 = new ConstantTexture(new Color3F(0.1F));
Texture texture062 = new ConstantTexture();
Texture texture071 = new ConstantTexture(colorA);
Texture texture072 = new ConstantTexture();
Texture texture081 = new ConstantTexture(Color3F.WHITE);
Texture texture082 = new ConstantTexture();
Texture texture091 = new ConstantTexture(Color3F.WHITE);
Texture texture092 = new ConstantTexture();
Texture texture101 = new ConstantTexture(new Color3F(1.0F, 0.01F, 0.01F));
Texture texture102 = new ConstantTexture();
Texture texture111 = new ConstantTexture(Color3F.WHITE);
Texture texture112 = new ConstantTexture();
Texture texture121 = new ConstantTexture(new Color3F(1.0F, 0.01F, 0.01F));
Texture texture122 = new ConstantTexture();
Texture texture131 = new ConstantTexture(new Color3F(1.0F, 0.01F, 0.01F));
Texture texture132 = new ConstantTexture();
Texture texture141 = new ConstantTexture(colorB);
Texture texture142 = new ConstantTexture();
Texture texture151 = new ConstantTexture(colorA);
Texture texture152 = new ConstantTexture();
Texture texture161 = new ConstantTexture(colorC);
Texture texture162 = new ConstantTexture();
Texture texture171 = new ConstantTexture(colorC);
Texture texture172 = new ConstantTexture();
Texture texture181 = new ConstantTexture(colorC);
Texture texture182 = new ConstantTexture();
Texture texture191 = new ConstantTexture(colorC);
Texture texture192 = new ConstantTexture();
Texture texture201 = new ConstantTexture(new Color3F(0.2F));
Texture texture202 = new ConstantTexture();
Texture texture211 = new ConstantTexture(colorA);
Texture texture212 = new ConstantTexture();
Texture texture221 = new ConstantTexture(colorC);
Texture texture222 = new ConstantTexture();
Texture texture231 = new ConstantTexture(colorC);
Texture texture232 = new ConstantTexture();
Texture texture241 = new ConstantTexture(colorC);
Texture texture242 = new ConstantTexture();
Texture texture251 = new ConstantTexture(colorC);
Texture texture252 = new ConstantTexture();
Texture texture261 = new ConstantTexture(colorC);
Texture texture262 = new ConstantTexture();
Texture texture271 = new ConstantTexture(colorC);
Texture texture272 = new ConstantTexture();
Texture texture281 = new ConstantTexture(colorC);
Texture texture282 = new ConstantTexture();
Texture texture291 = new ConstantTexture(colorC);
Texture texture292 = new ConstantTexture();
Texture texture301 = new ConstantTexture(colorC);
Texture texture302 = new ConstantTexture();
Texture texture311 = new ConstantTexture(new Color3F(227, 161, 115));
Texture texture312 = new ConstantTexture();
Texture texture321 = new ConstantTexture(Color3F.WHITE);
Texture texture322 = new ConstantTexture(new Color3F(12.0F));
Texture texture331 = new ConstantTexture(colorB);
Texture texture332 = new ConstantTexture();
Texture texture341 = new ConstantTexture(Color3F.WHITE);
Texture texture342 = new ConstantTexture();
Texture texture351 = new ConstantTexture(Color3F.WHITE);
Texture texture352 = new ConstantTexture(new Color3F(12.0F));
Texture texture361 = new ConstantTexture(colorB);
Texture texture362 = new ConstantTexture();
Texture texture371 = new ConstantTexture(Color3F.WHITE);
Texture texture372 = new ConstantTexture();
Texture texture381 = new ConstantTexture(colorC);
Texture texture382 = new ConstantTexture();
Texture texture391 = new ConstantTexture(colorC);
Texture texture392 = new ConstantTexture();
Texture texture401 = new ConstantTexture(colorA);
Texture texture402 = new ConstantTexture();
Texture texture411 = new ConstantTexture(colorC);
Texture texture412 = new ConstantTexture();
Texture texture421 = new ConstantTexture(colorC);
Texture texture422 = new ConstantTexture();
Texture texture431 = new ConstantTexture(colorA);
Texture texture432 = new ConstantTexture();
Texture texture441 = new ConstantTexture(new Color3F(98, 74, 46));
Texture texture442 = new ConstantTexture();
Texture texture451 = new ConstantTexture(Color3F.WHITE);
Texture texture452 = new ConstantTexture();
Texture texture461 = new ConstantTexture(new Color3F(98, 74, 46));
Texture texture462 = new ConstantTexture();
Texture texture471 = new ConstantTexture(colorC);
Texture texture472 = new ConstantTexture();
Texture texture481 = new ConstantTexture(Color3F.WHITE);
Texture texture482 = new ConstantTexture();
Texture texture491 = new ConstantTexture(colorA);
Texture texture492 = new ConstantTexture();
Texture texture501 = new ConstantTexture(colorC);
Texture texture502 = new ConstantTexture();
Texture texture511 = new ConstantTexture(Color3F.WHITE);
Texture texture512 = new ConstantTexture();
Texture texture521 = new ConstantTexture(colorA);
Texture texture522 = new ConstantTexture();
Texture texture531 = new ConstantTexture(new Color3F(0.1F));
Texture texture532 = new ConstantTexture();
Texture texture541 = new ConstantTexture(new Color3F(0.1F));
Texture texture542 = new ConstantTexture();
Texture texture551 = new ConstantTexture(new Color3F(0.1F));
Texture texture552 = new ConstantTexture();
Texture texture561 = new ConstantTexture(colorC);
Texture texture562 = new ConstantTexture();
Texture texture571 = new ConstantTexture(new Color3F(0.1F));
Texture texture572 = new ConstantTexture();
Texture texture581 = new ConstantTexture(new Color3F(0.1F));
Texture texture582 = new ConstantTexture();
Texture texture591 = new ConstantTexture(new Color3F(0.1F));
Texture texture592 = new ConstantTexture();
Texture texture601 = new ConstantTexture(colorC);
Texture texture602 = new ConstantTexture();
Texture texture611 = new ConstantTexture(new Color3F(0.1F));
Texture texture612 = new ConstantTexture();
Texture texture621 = new ConstantTexture(new Color3F(0.1F));
Texture texture622 = new ConstantTexture();
Texture texture631 = new ConstantTexture(new Color3F(0.1F));
Texture texture632 = new ConstantTexture();
Texture texture641 = new ConstantTexture(colorC);
Texture texture642 = new ConstantTexture();
Texture texture651 = new ConstantTexture(new Color3F(0.1F));
Texture texture652 = new ConstantTexture();
Texture texture661 = new ConstantTexture(new Color3F(0.1F));
Texture texture662 = new ConstantTexture();
Texture texture671 = new ConstantTexture(new Color3F(0.1F));
Texture texture672 = new ConstantTexture();
Texture texture681 = new ConstantTexture(colorC);
Texture texture682 = new ConstantTexture();
Texture texture691 = new ConstantTexture(Color3F.WHITE);
Texture texture692 = new ConstantTexture();

Matrix44F matrix01 = Matrix44F.identity();
Matrix44F matrixNM = Matrix44F.multiply(Matrix44F.translate(0.0F, 0.0F, 5.0F), Matrix44F.multiply(Matrix44F.rotateY(AngleF.degrees(90.0F)), Matrix44F.rotateX(AngleF.degrees(270.0F))));

scene.addLight(new PerezLight());
scene.addPrimitive(new Primitive(material01, shape01, texture011, texture012, matrix01));
scene.addPrimitive(new Primitive(material02, shape02, texture021, texture022, matrixNM));//Base			wind_glass
scene.addPrimitive(new Primitive(material03, shape03, texture031, texture032, matrixNM));//Base			Body_paint
scene.addPrimitive(new Primitive(material04, shape04, texture041, texture042, matrixNM));//Base			Misc
scene.addPrimitive(new Primitive(material05, shape05, texture051, texture052, matrixNM));//Base			Misc0
scene.addPrimitive(new Primitive(material06, shape06, texture061, texture062, matrixNM));//Base			Material__583
scene.addPrimitive(new Primitive(material07, shape07, texture071, texture072, matrixNM));//Base			Body_paint
scene.addPrimitive(new Primitive(material08, shape08, texture081, texture082, matrixNM));//Base			License
scene.addPrimitive(new Primitive(material09, shape09, texture091, texture092, matrixNM));//Base			wind_glass
scene.addPrimitive(new Primitive(material10, shape10, texture101, texture102, matrixNM));//Base			Material__586
scene.addPrimitive(new Primitive(material11, shape11, texture111, texture112, matrixNM));//Base			wind_glass
scene.addPrimitive(new Primitive(material12, shape12, texture121, texture122, matrixNM));//BLightL		Material__589
scene.addPrimitive(new Primitive(material13, shape13, texture131, texture132, matrixNM));//BLightR		Material__589
scene.addPrimitive(new Primitive(material14, shape14, texture141, texture142, matrixNM));//Body			DoorLine
scene.addPrimitive(new Primitive(material15, shape15, texture151, texture152, matrixNM));//Body			Badging_Chrome
scene.addPrimitive(new Primitive(material16, shape16, texture161, texture162, matrixNM));//Body			Misc1
scene.addPrimitive(new Primitive(material17, shape17, texture171, texture172, matrixNM));//Body			Misc_Chrome
scene.addPrimitive(new Primitive(material18, shape18, texture181, texture182, matrixNM));//Body			Misc_Chrome0
scene.addPrimitive(new Primitive(material19, shape19, texture191, texture192, matrixNM));//Body			Misc_Chrome1
scene.addPrimitive(new Primitive(material20, shape20, texture201, texture202, matrixNM));//Body			Black
scene.addPrimitive(new Primitive(material21, shape21, texture211, texture212, matrixNM));//Body			Body_paint0
scene.addPrimitive(new Primitive(material22, shape22, texture221, texture222, matrixNM));//Body			Bottom
scene.addPrimitive(new Primitive(material23, shape23, texture231, texture232, matrixNM));//BrakeFL		Brake_Pads
scene.addPrimitive(new Primitive(material24, shape24, texture241, texture242, matrixNM));//BrakeFL		Brake_Disc
scene.addPrimitive(new Primitive(material25, shape25, texture251, texture252, matrixNM));//BrakeFR		Brake_Pads0
scene.addPrimitive(new Primitive(material26, shape26, texture261, texture262, matrixNM));//BrakeFR		Brake_Disc0
scene.addPrimitive(new Primitive(material27, shape27, texture271, texture272, matrixNM));//BrakeRL		Brake_Pads
scene.addPrimitive(new Primitive(material28, shape28, texture281, texture282, matrixNM));//BrakeRL		Brake_Disc
scene.addPrimitive(new Primitive(material29, shape29, texture291, texture292, matrixNM));//BrakeRR		Brake_Pads0
scene.addPrimitive(new Primitive(material30, shape30, texture301, texture302, matrixNM));//BrakeRR		Brake_Disc0
scene.addPrimitive(new Primitive(material31, shape31, texture311, texture312, matrixNM));//Driver		Driver
scene.addPrimitive(new Primitive(material32, shape32, texture321, texture322, matrixNM));//HLightL		Material__593
scene.addPrimitive(new Primitive(material33, shape33, texture331, texture332, matrixNM));//HLightL		Misc2
scene.addPrimitive(new Primitive(material34, shape34, texture341, texture342, matrixNM));//HLightLG		Material__594
scene.addPrimitive(new Primitive(material35, shape35, texture351, texture352, matrixNM));//HLightR		Material__593
scene.addPrimitive(new Primitive(material36, shape36, texture361, texture362, matrixNM));//HLightR		Misc2
scene.addPrimitive(new Primitive(material37, shape37, texture371, texture372, matrixNM));//HLightRG		Material__594
scene.addPrimitive(new Primitive(material38, shape38, texture381, texture382, matrixNM));//Hood			Misc3
scene.addPrimitive(new Primitive(material39, shape39, texture391, texture392, matrixNM));//Hood			Misc_Chrome2
scene.addPrimitive(new Primitive(material40, shape40, texture401, texture402, matrixNM));//Hood			Body_paint1
scene.addPrimitive(new Primitive(material41, shape41, texture411, texture412, matrixNM));//Hood_Carbon	Misc4
scene.addPrimitive(new Primitive(material42, shape42, texture421, texture422, matrixNM));//Hood_Carbon	Misc_Chrome3
scene.addPrimitive(new Primitive(material43, shape43, texture431, texture432, matrixNM));//Hood_Carbon	Body_paint2
scene.addPrimitive(new Primitive(material44, shape44, texture441, texture442, matrixNM));//Interior		Interior
scene.addPrimitive(new Primitive(material45, shape45, texture451, texture452, matrixNM));//Interior		Material__597
scene.addPrimitive(new Primitive(material46, shape46, texture461, texture462, matrixNM));//Interior		Interior0
scene.addPrimitive(new Primitive(material47, shape47, texture471, texture472, matrixNM));//MirrorL		Misc_Chrome4
scene.addPrimitive(new Primitive(material48, shape48, texture481, texture482, matrixNM));//MirrorL		Material__598
scene.addPrimitive(new Primitive(material49, shape49, texture491, texture492, matrixNM));//MirrorL		Body_paint3
scene.addPrimitive(new Primitive(material50, shape50, texture501, texture502, matrixNM));//MirrorR		Misc_Chrome4
scene.addPrimitive(new Primitive(material51, shape51, texture511, texture512, matrixNM));//MirrorR		Material__598
scene.addPrimitive(new Primitive(material52, shape52, texture521, texture522, matrixNM));//MirrorR		Body_paint3
scene.addPrimitive(new Primitive(material53, shape53, texture531, texture532, matrixNM));//TireFL		Tire_Back
scene.addPrimitive(new Primitive(material54, shape54, texture541, texture542, matrixNM));//TireFL		Tire_Tread
scene.addPrimitive(new Primitive(material55, shape55, texture551, texture552, matrixNM));//TireFL		Tire_Sidewall
scene.addPrimitive(new Primitive(material56, shape56, texture561, texture562, matrixNM));//TireFL		Material__600
scene.addPrimitive(new Primitive(material57, shape57, texture571, texture572, matrixNM));//TireFR		Tire_Back
scene.addPrimitive(new Primitive(material58, shape58, texture581, texture582, matrixNM));//TireFR		Tire_Tread
scene.addPrimitive(new Primitive(material59, shape59, texture591, texture592, matrixNM));//TireFR		Tire_Sidewall
scene.addPrimitive(new Primitive(material60, shape60, texture601, texture602, matrixNM));//TireFR		Material__600
scene.addPrimitive(new Primitive(material61, shape61, texture611, texture612, matrixNM));//TireRL		Tire_Back
scene.addPrimitive(new Primitive(material62, shape62, texture621, texture622, matrixNM));//TireRL		Tire_Tread
scene.addPrimitive(new Primitive(material63, shape63, texture631, texture632, matrixNM));//TireRL		Tire_Sidewall
scene.addPrimitive(new Primitive(material64, shape64, texture641, texture642, matrixNM));//TireRL		Material__600
scene.addPrimitive(new Primitive(material65, shape65, texture651, texture652, matrixNM));//TireRR		Tire_Back
scene.addPrimitive(new Primitive(material66, shape66, texture661, texture662, matrixNM));//TireRR		Tire_Tread
scene.addPrimitive(new Primitive(material67, shape67, texture671, texture672, matrixNM));//TireRR		Tire_Sidewall
scene.addPrimitive(new Primitive(material68, shape68, texture681, texture682, matrixNM));//TireRR		Material__600
scene.addPrimitive(new Primitive(material69, shape69, texture691, texture692, matrixNM));//License		License0
scene.setCamera(new Camera(new Point3F(0.0F, 2.0F, 0.0F)));
scene.setName("RayitoSL500");