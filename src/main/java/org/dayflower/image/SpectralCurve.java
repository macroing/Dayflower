/**
 * Copyright 2020 J&#246;rgen Lundgren
 * 
 * This file is part of Dayflower.
 * 
 * Dayflower is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Dayflower is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with Dayflower. If not, see <http://www.gnu.org/licenses/>.
 */
package org.dayflower.image;

/**
 * A {@code SpectralCurve} is used for sampled or analytic spectral data.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public abstract class SpectralCurve {
//	private static final double[] CIE_X_BAR = {0.000129900000D, 0.000232100000D, 0.000414900000D, 0.000741600000D, 0.001368000000D, 0.002236000000D, 0.004243000000D, 0.007650000000D, 0.014310000000D, 0.023190000000D, 0.043510000000D, 0.077630000000D, 0.134380000000D, 0.214770000000D, 0.283900000000D, 0.328500000000D, 0.348280000000D, 0.348060000000D, 0.336200000000D, 0.318700000000D, 0.290800000000D, 0.251100000000D, 0.195360000000D, 0.142100000000D, 0.095640000000D, 0.057950010000D, 0.032010000000D, 0.014700000000D, 0.004900000000D, 0.002400000000D, 0.009300000000D, 0.029100000000D, 0.063270000000D, 0.109600000000D, 0.165500000000D, 0.225749900000D, 0.290400000000D, 0.359700000000D, 0.433449900000D, 0.512050100000D, 0.594500000000D, 0.678400000000D, 0.762100000000D, 0.842500000000D, 0.916300000000D, 0.978600000000D, 1.026300000000D, 1.056700000000D, 1.062200000000D, 1.045600000000D, 1.002600000000D, 0.938400000000D, 0.854449900000D, 0.751400000000D, 0.642400000000D, 0.541900000000D, 0.447900000000D, 0.360800000000D, 0.283500000000D, 0.218700000000D, 0.164900000000D, 0.121200000000D, 0.087400000000D, 0.063600000000D, 0.046770000000D, 0.032900000000D, 0.022700000000D, 0.015840000000D, 0.011359160000D, 0.008110916000D, 0.005790346000D, 0.004106457000D, 0.002899327000D, 0.002049190000D, 0.001439971000D, 0.000999949300D, 0.000690078600D, 0.000476021300D, 0.000332301100D, 0.000234826100D, 0.000166150500D, 0.000117413000D, 0.000083075270D, 0.000058706520D, 0.000041509940D, 0.000029353260D, 0.000020673830D, 0.000014559770D, 0.000010253980D, 0.000007221456D, 0.000005085868D, 0.000003581652D, 0.000002522525D, 0.000001776509D, 0.000001251141D};
//	private static final double[] CIE_Y_BAR = {0.000003917000D, 0.000006965000D, 0.000012390000D, 0.000022020000D, 0.000039000000D, 0.000064000000D, 0.000120000000D, 0.000217000000D, 0.000396000000D, 0.000640000000D, 0.001210000000D, 0.002180000000D, 0.004000000000D, 0.007300000000D, 0.011600000000D, 0.016840000000D, 0.023000000000D, 0.029800000000D, 0.038000000000D, 0.048000000000D, 0.060000000000D, 0.073900000000D, 0.090980000000D, 0.112600000000D, 0.139020000000D, 0.169300000000D, 0.208020000000D, 0.258600000000D, 0.323000000000D, 0.407300000000D, 0.503000000000D, 0.608200000000D, 0.710000000000D, 0.793200000000D, 0.862000000000D, 0.914850100000D, 0.954000000000D, 0.980300000000D, 0.994950100000D, 1.000000000000D, 0.995000000000D, 0.978600000000D, 0.952000000000D, 0.915400000000D, 0.870000000000D, 0.816300000000D, 0.757000000000D, 0.694900000000D, 0.631000000000D, 0.566800000000D, 0.503000000000D, 0.441200000000D, 0.381000000000D, 0.321000000000D, 0.265000000000D, 0.217000000000D, 0.175000000000D, 0.138200000000D, 0.107000000000D, 0.081600000000D, 0.061000000000D, 0.044580000000D, 0.032000000000D, 0.023200000000D, 0.017000000000D, 0.011920000000D, 0.008210000000D, 0.005723000000D, 0.004102000000D, 0.002929000000D, 0.002091000000D, 0.001484000000D, 0.001047000000D, 0.000740000000D, 0.000520000000D, 0.000361100000D, 0.000249200000D, 0.000171900000D, 0.000120000000D, 0.000084800000D, 0.000060000000D, 0.000042400000D, 0.000030000000D, 0.000021200000D, 0.000014990000D, 0.000010600000D, 0.000007465700D, 0.000005257800D, 0.000003702900D, 0.000002607800D, 0.000001836600D, 0.000001293400D, 0.000000910930D, 0.000000641530D, 0.000000451810D};
//	private static final double[] CIE_Z_BAR = {0.000606100000D, 0.001086000000D, 0.001946000000D, 0.003486000000D, 0.006450001000D, 0.010549990000D, 0.020050010000D, 0.036210000000D, 0.067850010000D, 0.110200000000D, 0.207400000000D, 0.371300000000D, 0.645600000000D, 1.039050100000D, 1.385600000000D, 1.622960000000D, 1.747060000000D, 1.782600000000D, 1.772110000000D, 1.744100000000D, 1.669200000000D, 1.528100000000D, 1.287640000000D, 1.041900000000D, 0.812950100000D, 0.616200000000D, 0.465180000000D, 0.353300000000D, 0.272000000000D, 0.212300000000D, 0.158200000000D, 0.111700000000D, 0.078249990000D, 0.057250010000D, 0.042160000000D, 0.029840000000D, 0.020300000000D, 0.013400000000D, 0.008749999000D, 0.005749999000D, 0.003900000000D, 0.002749999000D, 0.002100000000D, 0.001800000000D, 0.001650001000D, 0.001400000000D, 0.001100000000D, 0.001000000000D, 0.000800000000D, 0.000600000000D, 0.000340000000D, 0.000240000000D, 0.000190000000D, 0.000100000000D, 0.000049999990D, 0.000030000000D, 0.000020000000D, 0.000010000000D, 0.000000000000D, 0.000000000000D, 0.000000000000D, 0.000000000000D, 0.000000000000D, 0.000000000000D, 0.000000000000D, 0.000000000000D, 0.000000000000D, 0.000000000000D, 0.000000000000D, 0.000000000000D, 0.000000000000D, 0.000000000000D, 0.000000000000D, 0.000000000000D, 0.000000000000D, 0.000000000000D, 0.000000000000D, 0.000000000000D, 0.000000000000D, 0.000000000000D, 0.000000000000D, 0.000000000000D, 0.000000000000D, 0.000000000000D, 0.000000000000D, 0.000000000000D, 0.000000000000D, 0.000000000000D, 0.000000000000D, 0.000000000000D, 0.000000000000D, 0.000000000000D, 0.000000000000D, 0.000000000000D, 0.000000000000D};
	private static final double[] CIE_X_BAR = {0.000129900000D, 0.000145847000D, 0.000163802100D, 0.000184003700D, 0.000206690200D, 0.000232100000D, 0.000260728000D, 0.000293075000D, 0.000329388000D, 0.00036991400D, 0.00041490000D, 0.00046415870D, 0.00051898600D, 0.00058185400D, 0.00065523470D, 0.00074160000D, 0.00084502960D, 0.00096452680D, 0.00109494900D, 0.001231154D, 0.001368000D, 0.001502050D, 0.001642328D, 0.001802382D, 0.001995757D, 0.002236000D, 0.002535385D, 0.002892603D, 0.003300829D, 0.003753236D, 0.004243000D, 0.004762389D, 0.005330048D, 0.005978712D, 0.006741117D, 0.007650000D, 0.008751373D, 0.01002888D, 0.01142170D, 0.01286901D, 0.01431000D, 0.01570443D, 0.01714744D, 0.01878122D, 0.02074801D, 0.02319000D, 0.02620736D, 0.02978248D, 0.03388092D, 0.03846824D, 0.04351000D, 0.04899560D, 0.05502260D, 0.06171880D, 0.06921200D, 0.07763000D, 0.08695811D, 0.09717672D, 0.1084063D, 0.1207672D, 0.1343800D, 0.1493582D, 0.1653957D, 0.1819831D, 0.1986110D, 0.2147700D, 0.2301868D, 0.2448797D, 0.2587773D, 0.2718079D, 0.2839000D, 0.2949438D, 0.3048965D, 0.3137873D, 0.3216454D, 0.3285000D, 0.3343513D, 0.3392101D, 0.3431213D, 0.3461296D, 0.3482800D, 0.3495999D, 0.3501474D, 0.3500130D, 0.3492870D, 0.3480600D, 0.3463733D, 0.3442624D, 0.3418088D, 0.3390941D, 0.3362000D, 0.3331977D, 0.3300411D, 0.3266357D, 0.3228868D, 0.3187000D, 0.3140251D, 0.3088840D, 0.3032904D, 0.2972579D, 0.2908000D, 0.2839701D, 0.2767214D, 0.2689178D, 0.2604227D, 0.2511000D, 0.2408475D, 0.2298512D, 0.2184072D, 0.2068115D, 0.1953600D, 0.1842136D, 0.1733273D, 0.1626881D, 0.1522833D, 0.1421000D, 0.1321786D, 0.1225696D, 0.1132752D, 0.1042979D, 0.09564000D, 0.08729955D, 0.07930804D, 0.07171776D, 0.06458099D, 0.05795001D, 0.05186211D, 0.04628152D, 0.04115088D, 0.03641283D, 0.03201000D, 0.02791720D, 0.02414440D, 0.02068700D, 0.01754040D, 0.01470000D, 0.01216179D, 0.009919960D, 0.007967240D, 0.006296346D, 0.004900000D, 0.003777173D, 0.002945320D, 0.002424880D, 0.002236293D, 0.002400000D, 0.002925520D, 0.003836560D, 0.005174840D, 0.006982080D, 0.009300000D, 0.01214949D, 0.01553588D, 0.01947752D, 0.02399277D, 0.02910000D, 0.03481485D, 0.04112016D, 0.04798504D, 0.05537861D, 0.06327000D, 0.07163501D, 0.08046224D, 0.08973996D, 0.09945645D, 0.1096000D, 0.1201674D, 0.1311145D, 0.1423679D, 0.1538542D, 0.1655000D, 0.1772571D, 0.1891400D, 0.2011694D, 0.2133658D, 0.2257499D, 0.2383209D, 0.2510668D, 0.2639922D, 0.2771017D, 0.2904000D, 0.3038912D, 0.3175726D, 0.3314384D, 0.3454828D, 0.3597000D, 0.3740839D, 0.3886396D, 0.4033784D, 0.4183115D, 0.4334499D, 0.4487953D, 0.4643360D, 0.4800640D, 0.4959713D, 0.5120501D, 0.5282959D, 0.5446916D, 0.5612094D, 0.5778215D, 0.5945000D, 0.6112209D, 0.6279758D, 0.6447602D, 0.6615697D, 0.6784000D, 0.6952392D, 0.7120586D, 0.7288284D, 0.7455188D, 0.7621000D, 0.7785432D, 0.7948256D, 0.8109264D, 0.8268248D, 0.8425000D, 0.8579325D, 0.8730816D, 0.8878944D, 0.9023181D, 0.9163000D, 0.9297995D, 0.9427984D, 0.9552776D, 0.9672179D, 0.9786000D, 0.9893856D, 0.9995488D, 1.0090892D, 1.0180064D, 1.0263000D, 1.0339827D, 1.0409860D, 1.0471880D, 1.0524667D, 1.0567000D, 1.0597944D, 1.0617992D, 1.0628068D, 1.0629096D, 1.0622000D, 1.0607352D, 1.0584436D, 1.0552244D, 1.0509768D, 1.0456000D, 1.0390369D, 1.0313608D, 1.0226662D, 1.0130477D, 1.0026000D, 0.9913675D, 0.9793314D, 0.9664916D, 0.9528479D, 0.9384000D, 0.9231940D, 0.9072440D, 0.8905020D, 0.8729200D, 0.8544499D, 0.8350840D, 0.8149460D, 0.7941860D, 0.7729540D, 0.7514000D, 0.7295836D, 0.7075888D, 0.6856022D, 0.6638104D, 0.6424000D, 0.6215149D, 0.6011138D, 0.5811052D, 0.5613977D, 0.5419000D, 0.5225995D, 0.5035464D, 0.4847436D, 0.4661939D, 0.4479000D, 0.4298613D, 0.4120980D, 0.3946440D, 0.3775333D, 0.3608000D, 0.3444563D, 0.3285168D, 0.3130192D, 0.2980011D, 0.2835000D, 0.2695448D, 0.2561184D, 0.2431896D, 0.2307272D, 0.2187000D, 0.2070971D, 0.1959232D, 0.1851708D, 0.1748323D, 0.1649000D, 0.1553667D, 0.1462300D, 0.1374900D, 0.1291467D, 0.1212000D, 0.1136397D, 0.1064650D, 0.09969044D, 0.09333061D, 0.08740000D, 0.08190096D, 0.07680428D, 0.07207712D, 0.06768664D, 0.06360000D, 0.05980685D, 0.05628216D, 0.05297104D, 0.04981861D, 0.04677000D, 0.04378405D, 0.04087536D, 0.03807264D, 0.03540461D, 0.03290000D, 0.03056419D, 0.02838056D, 0.02634484D, 0.02445275D, 0.02270000D, 0.02108429D, 0.01959988D, 0.01823732D, 0.01698717D, 0.01584000D, 0.01479064D, 0.01383132D, 0.01294868D, 0.01212920D, 0.01135916D, 0.01062935D, 0.009938846D, 0.009288422D, 0.008678854D, 0.008110916D, 0.007582388D, 0.007088746D, 0.006627313D, 0.006195408D, 0.005790346D, 0.005409826D, 0.005052583D, 0.004717512D, 0.004403507D, 0.004109457D, 0.003833913D, 0.003575748D, 0.003334342D, 0.003109075D, 0.002899327D, 0.002704348D, 0.002523020D, 0.002354168D, 0.002196616D, 0.002049190D, 0.001910960D, 0.001781438D, 0.001660110D, 0.001546459D, 0.001439971D, 0.001340042D, 0.001246275D, 0.001158471D, 0.001076430D, 0.0009999493D, 0.0009287358D, 0.0008624332D, 0.0008007503D, 0.0007433960D, 0.0006900786D, 0.0006405156D, 0.0005945021D, 0.0005518646D, 0.0005124290D, 0.0004760213D, 0.0004424536D, 0.0004115117D, 0.0003829814D, 0.0003566491D, 0.0003323011D, 0.0003097586D, 0.0002888871D, 0.0002695394D, 0.0002515682D, 0.0002348261D, 0.0002191710D, 0.0002045258D, 0.0001908405D, 0.0001780654D, 0.0001661505D, 0.0001550236D, 0.0001446219D, 0.0001349098D, 0.0001258520D, 0.0001174130D, 0.0001095515D, 0.0001022245D, 0.00009539445D, 0.00008902390D, 0.00008307527D, 0.00007751269D, 0.00007231304D, 0.00006745778D, 0.00006292844D, 0.00005870652D, 0.00005477028D, 0.00005109918D, 0.00004767654D, 0.00004448567D, 0.00004150994D, 0.00003873324D, 0.00003614203D, 0.00003372352D, 0.00003146487D, 0.00002935326D, 0.00002737573D, 0.00002552433D, 0.00002379376D, 0.00002217870D, 0.00002067383D, 0.00001927226D, 0.00001796640D, 0.00001674991D, 0.00001561648D, 0.00001455977D, 0.00001357387D, 0.00001265436D, 0.00001179723D, 0.00001099844D, 0.00001025398D, 0.000009559646D, 0.000008912044D, 0.000008308358D, 0.000007745769D, 0.000007221456D, 0.000006732475D, 0.000006276423D, 0.000005851304D, 0.000005455118D, 0.000005085868D, 0.000004741466D, 0.000004420236D, 0.000004120783D, 0.000003841716D, 0.000003581652D, 0.000003339127D, 0.000003112949D, 0.000002902121D, 0.000002705645D, 0.000002522525D, 0.000002351726D, 0.000002192415D, 0.000002043902D, 0.000001905497D, 0.000001776509D, 0.000001656215D, 0.000001544022D, 0.000001439440D, 0.000001341977D, 0.000001251141D};
	private static final double[] CIE_Y_BAR = {0.000003917000D, 0.000004393581D, 0.000004929604D, 0.000005532136D, 0.000006208245D, 0.000006965000D, 0.000007813219D, 0.000008767336D, 0.000009839844D, 0.00001104323D, 0.00001239000D, 0.00001388641D, 0.00001555728D, 0.00001744296D, 0.00001958375D, 0.00002202000D, 0.00002483965D, 0.00002804126D, 0.00003153104D, 0.00003521521D, 0.00003900000D, 0.00004282640D, 0.00004691460D, 0.00005158960D, 0.00005717640D, 0.00006400000D, 0.00007234421D, 0.00008221224D, 0.00009350816D, 0.0001061361D, 0.0001200000D, 0.0001349840D, 0.0001514920D, 0.0001702080D, 0.0001918160D, 0.0002170000D, 0.0002469067D, 0.0002812400D, 0.0003185200D, 0.0003572667D, 0.0003960000D, 0.0004337147D, 0.0004730240D, 0.0005178760D, 0.0005722187D, 0.0006400000D, 0.0007245600D, 0.0008255000D, 0.0009411600D, 0.001069880D, 0.001210000D, 0.001362091D, 0.001530752D, 0.001720368D, 0.001935323D, 0.002180000D, 0.002454800D, 0.002764000D, 0.003117800D, 0.003526400D, 0.004000000D, 0.004546240D, 0.005159320D, 0.005829280D, 0.006546160D, 0.007300000D, 0.008086507D, 0.008908720D, 0.009767680D, 0.01066443D, 0.01160000D, 0.01257317D, 0.01358272D, 0.01462968D, 0.01571509D, 0.01684000D, 0.01800736D, 0.01921448D, 0.02045392D, 0.02171824D, 0.02300000D, 0.02429461D, 0.02561024D, 0.02695857D, 0.02835125D, 0.02980000D, 0.03131083D, 0.03288368D, 0.03452112D, 0.03622571D, 0.03800000D, 0.03984667D, 0.04176800D, 0.04376600D, 0.04584267D, 0.04800000D, 0.05024368D, 0.05257304D, 0.05498056D, 0.05745872D, 0.06000000D, 0.06260197D, 0.06527752D, 0.06804208D, 0.07091109D, 0.07390000D, 0.07701600D, 0.08026640D, 0.08366680D, 0.08723280D, 0.09098000D, 0.09491755D, 0.09904584D, 0.1033674D, 0.1078846D, 0.1126000D, 0.1175320D, 0.1226744D, 0.1279928D, 0.1334528D, 0.1390200D, 0.1446764D, 0.1504693D, 0.1564619D, 0.1627177D, 0.1693000D, 0.1762431D, 0.1835581D, 0.1912735D, 0.1994180D, 0.2080200D, 0.2171199D, 0.2267345D, 0.2368571D, 0.2474812D, 0.2586000D, 0.2701849D, 0.2822939D, 0.2950505D, 0.3085780D, 0.3230000D, 0.3384021D, 0.3546858D, 0.3716986D, 0.3892875D, 0.4073000D, 0.4256299D, 0.4443096D, 0.4633944D, 0.4829395D, 0.5030000D, 0.5235693D, 0.5445120D, 0.5656900D, 0.5869653D, 0.6082000D, 0.6293456D, 0.6503068D, 0.6708752D, 0.6908424D, 0.7100000D, 0.7281852D, 0.7454636D, 0.7619694D, 0.7778368D, 0.7932000D, 0.8081104D, 0.8224962D, 0.8363068D, 0.8494916D, 0.8620000D, 0.8738108D, 0.8849624D, 0.8954936D, 0.9054432D, 0.9148501D, 0.9237348D, 0.9320924D, 0.9399226D, 0.9472252D, 0.9540000D, 0.9602561D, 0.9660074D, 0.9712606D, 0.9760225D, 0.9803000D, 0.9840924D, 0.9874812D, 0.9903128D, 0.9928116D, 0.9949501D, 0.9967108D, 0.9980983D, 0.9991120D, 0.9997482D, 1.0000000D, 0.9998567D, 0.9993046D, 0.9983255D, 0.9968987D, 0.9950000D, 0.9926005D, 0.9897426D, 0.9864444D, 0.9827241D, 0.9786000D, 0.9740837D, 0.9691712D, 0.9638568D, 0.9581349D, 0.9520000D, 0.9454504D, 0.9384992D, 0.9311628D, 0.9234576D, 0.9154000D, 0.9070064D, 0.8982772D, 0.8892048D, 0.8797816D, 0.8700000D, 0.8598613D, 0.8493920D, 0.8386220D, 0.8275813D, 0.8163000D, 0.8047947D, 0.7930820D, 0.7811920D, 0.7691547D, 0.7570000D, 0.7447541D, 0.7324224D, 0.7200036D, 0.7074965D, 0.6949000D, 0.6822192D, 0.6694716D, 0.6566744D, 0.6438448D, 0.6310000D, 0.6181555D, 0.6053144D, 0.5924756D, 0.5796379D, 0.5668000D, 0.5539611D, 0.5411372D, 0.5283528D, 0.5156323D, 0.5030000D, 0.4904688D, 0.4780304D, 0.4656776D, 0.4534032D, 0.4412000D, 0.4290800D, 0.4170360D, 0.4050320D, 0.3930320D, 0.3810000D, 0.3689184D, 0.3568272D, 0.3447768D, 0.3328176D, 0.3210000D, 0.3093381D, 0.2978504D, 0.2865936D, 0.2756245D, 0.2650000D, 0.2547632D, 0.2448896D, 0.2353344D, 0.2260528D, 0.2170000D, 0.2081616D, 0.1995488D, 0.1911552D, 0.1829744D, 0.1750000D, 0.1672235D, 0.1596464D, 0.1522776D, 0.1451259D, 0.1382000D, 0.1315003D, 0.1250248D, 0.1187792D, 0.1127691D, 0.1070000D, 0.1014762D, 0.09618864D, 0.09112296D, 0.08626485D, 0.08160000D, 0.07712064D, 0.07282552D, 0.06871008D, 0.06476976D, 0.06100000D, 0.05739621D, 0.05395504D, 0.05067376D, 0.04754965D, 0.04458000D, 0.04175872D, 0.03908496D, 0.03656384D, 0.03420048D, 0.03200000D, 0.02996261D, 0.02807664D, 0.02632936D, 0.02470805D, 0.02320000D, 0.02180077D, 0.02050112D, 0.01928108D, 0.01812069D, 0.01700000D, 0.01590379D, 0.01483718D, 0.01381068D, 0.01283478D, 0.01192000D, 0.01106831D, 0.01027339D, 0.009533311D, 0.008846157D, 0.008210000D, 0.007623781D, 0.007085424D, 0.006591476D, 0.006138485D, 0.005723000D, 0.005343059D, 0.004995796D, 0.004676404D, 0.004380075D, 0.004102000D, 0.003838453D, 0.003589099D, 0.003354219D, 0.003134093D, 0.002929000D, 0.002738139D, 0.002559876D, 0.002393244D, 0.002237275D, 0.002091000D, 0.001953587D, 0.001824580D, 0.001703580D, 0.001590187D, 0.001484000D, 0.001384496D, 0.001291268D, 0.001204092D, 0.001122744D, 0.001047000D, 0.0009765896D, 0.0009111088D, 0.0008501332D, 0.0007932384D, 0.0007400000D, 0.0006900827D, 0.0006433100D, 0.0005994960D, 0.0005584547D, 0.0005200000D, 0.0004839136D, 0.0004500528D, 0.0004183452D, 0.0003887184D, 0.0003611000D, 0.0003353835D, 0.0003114404D, 0.0002891656D, 0.0002684539D, 0.0002492000D, 0.0002313019D, 0.0002146856D, 0.0001992884D, 0.0001850475D, 0.0001719000D, 0.0001597781D, 0.0001486044D, 0.0001383016D, 0.0001287925D, 0.0001200000D, 0.0001118595D, 0.0001043224D, 0.00009733560D, 0.00009084587D, 0.00008480000D, 0.00007914667D, 0.00007385800D, 0.00006891600D, 0.00006430267D, 0.00006000000D, 0.00005598187D, 0.00005222560D, 0.00004871840D, 0.00004544747D, 0.00004240000D, 0.00003956104D, 0.00003691512D, 0.00003444868D, 0.00003214816D, 0.00003000000D, 0.00002799125D, 0.00002611356D, 0.00002436024D, 0.00002272461D, 0.00002120000D, 0.00001977855D, 0.00001845285D, 0.00001721687D, 0.00001606459D, 0.00001499000D, 0.00001398728D, 0.00001305155D, 0.00001217818D, 0.00001136254D, 0.00001060000D, 0.000009885877D, 0.000009217304D, 0.000008592362D, 0.000008009133D, 0.000007465700D, 0.000006959567D, 0.000006487995D, 0.000006048699D, 0.000005639396D, 0.000005257800D, 0.000004901771D, 0.000004569720D, 0.000004260194D, 0.000003971739D, 0.000003702900D, 0.000003452163D, 0.000003218302D, 0.000003000300D, 0.000002797139D, 0.000002607800D, 0.000002431220D, 0.000002266531D, 0.000002113013D, 0.000001969943D, 0.000001836600D, 0.000001712230D, 0.000001596228D, 0.000001488090D, 0.000001387314D, 0.000001293400D, 0.000001205820D, 0.000001124143D, 0.000001048009D, 0.0000009770578D, 0.0000009109300D, 0.0000008492513D, 0.0000007917212D, 0.0000007380904D, 0.0000006881098D, 0.0000006415300D, 0.0000005980895D, 0.0000005575746D, 0.0000005198080D, 0.0000004846123D, 0.0000004518100D};
	private static final double[] CIE_Z_BAR = {0.000606100000D, 0.000680879200D, 0.000765145600D, 0.000860012400D, 0.000966592800D, 0.001086000000D, 0.001220586000D, 0.001372729000D, 0.001543579000D, 0.00173428600D, 0.00194600000D, 0.00217777700D, 0.00243580900D, 0.00273195300D, 0.00307806400D, 0.00348600000D, 0.00397522700D, 0.00454088000D, 0.00515832000D, 0.005802907D, 0.006450001D, 0.007083216D, 0.007745488D, 0.008501152D, 0.009414544D, 0.01054999D, 0.01196580D, 0.01365587D, 0.01558805D, 0.01773015D, 0.02005001D, 0.02251136D, 0.02520288D, 0.02827972D, 0.03189704D, 0.03621000D, 0.04143771D, 0.04750372D, 0.05411988D, 0.06099803D, 0.06785001D, 0.07448632D, 0.08136156D, 0.08915364D, 0.09854048D, 0.1102000D, 0.1246133D, 0.1417017D, 0.1613035D, 0.1832568D, 0.2074000D, 0.2336921D, 0.2626114D, 0.2947746D, 0.3307985D, 0.3713000D, 0.4162091D, 0.4654642D, 0.5196948D, 0.5795303D, 0.6456000D, 0.7184838D, 0.7967133D, 0.8778459D, 0.9594390D, 1.0390501D, 1.1153673D, 1.1884971D, 1.2581233D, 1.3239296D, 1.3856000D, 1.4426352D, 1.4948035D, 1.5421903D, 1.5848807D, 1.6229600D, 1.6564048D, 1.6852959D, 1.7098745D, 1.7303821D, 1.7470600D, 1.7600446D, 1.7696233D, 1.7762637D, 1.7804334D, 1.7826000D, 1.7829682D, 1.7816998D, 1.7791982D, 1.7758671D, 1.7721100D, 1.7682589D, 1.7640390D, 1.7589438D, 1.7524663D, 1.7441000D, 1.7335595D, 1.7208581D, 1.7059369D, 1.6887372D, 1.6692000D, 1.6475287D, 1.6234127D, 1.5960223D, 1.5645280D, 1.5281000D, 1.4861114D, 1.4395215D, 1.3898799D, 1.3387362D, 1.2876400D, 1.2374223D, 1.1878243D, 1.1387611D, 1.0901480D, 1.0419000D, 0.9941976D, 0.9473473D, 0.9014531D, 0.8566193D, 0.8129501D, 0.7705173D, 0.7294448D, 0.6899136D, 0.6521049D, 0.6162000D, 0.5823286D, 0.5504162D, 0.5203376D, 0.4919673D, 0.4651800D, 0.4399246D, 0.4161836D, 0.3938822D, 0.3729459D, 0.3533000D, 0.3348578D, 0.3175521D, 0.3013375D, 0.2861686D, 0.2720000D, 0.2588171D, 0.2464838D, 0.2347718D, 0.2234533D, 0.2123000D, 0.2011692D, 0.1901196D, 0.1792254D, 0.1685608D, 0.1582000D, 0.1481383D, 0.1383758D, 0.1289942D, 0.1200751D, 0.1117000D, 0.1039048D, 0.09666748D, 0.08998272D, 0.08384531D, 0.07824999D, 0.07320899D, 0.06867816D, 0.06456784D, 0.06078835D, 0.05725001D, 0.05390435D, 0.05074664D, 0.04775276D, 0.04489859D, 0.04216000D, 0.03950728D, 0.03693564D, 0.03445836D, 0.03208872D, 0.02984000D, 0.02771181D, 0.02569444D, 0.02378716D, 0.02198925D, 0.02030000D, 0.01871805D, 0.01724036D, 0.01586364D, 0.01458461D, 0.01340000D, 0.01230723D, 0.01130188D, 0.01037792D, 0.009529306D, 0.008749999D, 0.008035200D, 0.007381600D, 0.006785400D, 0.006242800D, 0.005749999D, 0.005303600D, 0.004899800D, 0.004534200D, 0.004202400D, 0.003900000D, 0.003623200D, 0.003370600D, 0.003141400D, 0.002934800D, 0.002749999D, 0.002585200D, 0.002438600D, 0.002309400D, 0.002196800D, 0.002100000D, 0.002017733D, 0.001948200D, 0.001889800D, 0.001840933D, 0.001800000D, 0.001766267D, 0.001737800D, 0.001711200D, 0.001683067D, 0.001650001D, 0.001610133D, 0.001564400D, 0.001513600D, 0.001458533D, 0.001400000D, 0.001336667D, 0.001270000D, 0.001205000D, 0.001146667D, 0.001100000D, 0.001068800D, 0.001049400D, 0.001035600D, 0.001021200D, 0.001000000D, 0.0009686400D, 0.0009299200D, 0.0008868800D, 0.0008425600D, 0.0008000000D, 0.0007609600D, 0.0007236800D, 0.0006859200D, 0.0006454400D, 0.0006000000D, 0.0005478667D, 0.0004916000D, 0.0004354000D, 0.0003834667D, 0.0003400000D, 0.0003072533D, 0.0002831600D, 0.0002654400D, 0.0002518133D, 0.0002400000D, 0.0002295467D, 0.0002206400D, 0.0002119600D, 0.0002021867D, 0.0001900000D, 0.0001742133D, 0.0001556400D, 0.0001359600D, 0.0001168533D, 0.0001000000D, 0.00008613333D, 0.00007460000D, 0.00006500000D, 0.00005693333D, 0.00004999999D, 0.00004416000D, 0.00003948000D, 0.00003572000D, 0.00003264000D, 0.00003000000D, 0.00002765333D, 0.00002556000D, 0.00002364000D, 0.00002181333D, 0.00002000000D, 0.00001813333D, 0.00001620000D, 0.00001420000D, 0.00001213333D, 0.00001000000D, 0.000007733333D, 0.000005400000D, 0.000003200000D, 0.000001333333D, 0.000000000000D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D};
	private static final int WAVELENGTH_MAX = 830;
	private static final int WAVELENGTH_MIN = 360;
	private static final int WAVELENGTH_STEP = (WAVELENGTH_MAX - WAVELENGTH_MIN) / (CIE_X_BAR.length - 1);
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code SpectralCurve} instance.
	 */
	protected SpectralCurve() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link Color3F} instance in XYZ-color space.
	 * 
	 * @return a {@code Color3F} instance in XYZ-color space
	 */
	public final Color3F toColorXYZ() {
		float x = 0.0F;
		float y = 0.0F;
		float z = 0.0F;
		
		for(int i = 0, j = WAVELENGTH_MIN; i < CIE_X_BAR.length; i++, j += WAVELENGTH_STEP) {
			final float s = sample(j);
			
			x += s * CIE_X_BAR[i];
			y += s * CIE_Y_BAR[i];
			z += s * CIE_Z_BAR[i];
		}
		
		x *= WAVELENGTH_STEP;
		y *= WAVELENGTH_STEP;
		z *= WAVELENGTH_STEP;
		
		return new Color3F(x, y, z);
	}
	
	/**
	 * Returns a sample based on the wavelength {@code lambda} in nanometers.
	 * 
	 * @param lambda the wavelength in nanometers
	 * @return a sample based on the wavelength {@code lambda} in nanometers
	 */
	public abstract float sample(final float lambda);
}