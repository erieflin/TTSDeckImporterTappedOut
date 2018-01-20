package core;
import java.awt.Button;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Panel;
import java.awt.TextArea;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ColorDisplay {
	Button barr[];
	Button colorButtons[];
	Random r = new Random();
	Button fg, bg;
	TextArea ta;
	int val1, val2, val3;
	Panel p1, p2, p3;
	CardLayout cr;
	public static final String[] colorArray = new String[]
			{"#1CE6FF" ,"#FF34FF" ,"#008941" ,"#006FA6" ,"#A30059" ,"#FFDBE5" ,"#7A4900" ,"#63FFAC" ,"#B79762" ,"#004D43" ,"#8FB0FF" ,"#997D87" ,"#5A0007" ,"#809693" ,"#FEFFE6" ,"#1B4400" ,"#4FC601" ,"#3B5DFF" ,"#FF2F80" ,"#61615A" ,"#6B7900" ,"#00C2A0" ,"#FFAA92" ,"#FF90C9" ,"#B903AA" ,"#D16100" ,"#7B4F4B" ,"#A1C299" ,"#0AA6D8" ,"#00846F" ,"#FFB500" ,"#C2FFED" ,"#A079BF" ,"#CC0744" ,"#C0B9B2" ,"#C2FF99" ,"#00489C" ,"#6F0062" ,"#0CBD66" ,"#EEC3FF" ,"#456D75" ,"#B77B68" ,"#7A87A1" ,"#788D66" ,"#885578" ,"#FAD09F" ,"#FF8A9A" ,"#D157A0" ,"#BEC459" ,"#456648" ,"#0086ED" ,"#886F4C" ,"#B4A8BD" ,"#00A6AA" ,"#636375" ,"#A3C8C9" ,"#FF913F" ,"#938A81" ,"#575329" ,"#00FECF" ,"#B05B6F" ,"#8CD0FF" ,"#3B9700" ,"#04F757" ,"#C8A1A1" ,"#1E6E00" ,"#A77500" ,"#6367A9" ,"#A05837" ,"#6B002C" ,"#772600" ,"#D790FF" ,"#9B9700" ,"#549E79" ,"#72418F" ,"#BC23FF" ,"#99ADC0" ,"#3A2465" ,"#922329" ,"#0089A3" ,"#CB7E98" ,"#324E72" ,"#6A3A4C" ,"#83AB58" ,"#D1F7CE" ,"#004B28" ,"#C8D0F6" ,"#A3A489" ,"#806C66" ,"#66796D" ,"#DA007C" ,"#FF1A59" ,"#8ADBB4" ,"#C895C5" ,"#66E1D3" ,"#CFCDAC" ,"#D0AC94" ,"#7ED379" ,"#7A7BFF" ,"#D68E01" ,"#78AFA1" ,"#FEB2C6" ,"#75797C" ,"#837393" ,"#943A4D" ,"#B5F4FF" ,"#D2DCD5" ,"#9556BD" ,"#6A714A" ,"#02525F" ,"#0AA3F7" ,"#E98176" ,"#DBD5DD" ,"#5EBCD1" ,"#7E6405" ,"#02684E" ,"#962B75" ,"#8D8546" ,"#9695C5" ,"#E773CE" ,"#D86A78" ,"#3E89BE" ,"#CA834E" ,"#518A87" ,"#5B113C" ,"#55813B" ,"#E704C4" ,"#00005F" ,"#A97399" ,"#4B8160" ,"#59738A" ,"#FF5DA7" ,"#F7C9BF" ,"#643127" ,"#513A01" ,"#6B94AA" ,"#51A058" ,"#A45B02" ,"#E7AB63" ,"#4C6001" ,"#9C6966" ,"#64547B" ,"#97979E" ,"#006A66" ,"#006C31" ,"#DDB6D0" ,"#7C6571" ,"#9FB2A4" ,"#00D891" ,"#15A08A" ,"#BC65E9" ,"#C6DC99" ,"#6B3A64" ,"#F5E1FF" ,"#FFA0F2" ,"#CCAA35" ,"#8BB400" ,"#797868" ,"#C6005A" ,"#29607C" ,"#7D5A44" ,"#CCB87C" ,"#B88183" ,"#AA5199" ,"#B5D6C3" ,"#A38469" ,"#9F94F0" ,"#A74571" ,"#B894A6" ,"#71BB8C" ,"#00B433" ,"#789EC9" ,"#6D80BA" ,"#953F00" ,"#5EFF03" ,"#E4FFFC" ,"#1BE177" ,"#BCB1E5" ,"#76912F" ,"#0060CD" ,"#D20096" ,"#895563" ,"#5B3213" ,"#A76F42" ,"#89412E" ,"#A88C85" ,"#F4ABAA" ,"#A3F3AB" ,"#00C6C8" ,"#EA8B66" ,"#958A9F" ,"#BDC9D2" ,"#9FA064" ,"#658188" ,"#83A485" ,"#47675D" ,"#3A3F00" ,"#868E7E" ,"#98D058" ,"#6C8F7D" ,"#D7BFC2" ,"#3C3E6E" ,"#D83D66" ,"#2F5D9B" ,"#6C5E46" ,"#D25B88" ,"#5B656C" ,"#00B57F" ,"#545C46" ,"#866097" ,"#365D25" ,"#00CCFF" ,"#674E60" ,"#FC009C" ,"#92896B" ,"#DEC9B2" ,"#9D4948" ,"#85ABB4" ,"#D09685" ,"#A4ACAC" ,"#00FFFF" ,"#AE9C86" ,"#742A33" ,"#0E72C5" ,"#AFD8EC" ,"#C064B9" ,"#91028C" ,"#FEEDBF" ,"#FFB789" ,"#9CB8E4" ,"#AFFFD1" ,"#647095" ,"#34BBFF" ,"#807781" ,"#920003" ,"#B3A5A7" ,"#018615" ,"#F1FFC8" ,"#976F5C" ,"#FF3BC1" ,"#FF5F6B" ,"#077D84" ,"#F56D93" ,"#5771DA" ,"#830055" ,"#02D346" ,"#00905E" ,"#BE0028" ,"#6E96E3" ,"#007699" ,"#FEC96D" ,"#9C6A7D" ,"#3FA1B8" ,"#79B4D6" ,"#7FD4D9" ,"#B28D2D" ,"#E27A05" ,"#DD9CB8" ,"#AABC7A" ,"#980034" ,"#561A02" ,"#8F7F00" ,"#635000" ,"#CD7DAE" ,"#8A5E2D" ,"#FFB3E1" ,"#6B6466" ,"#88EC69" ,"#8FCCBE" ,"#511F4D" ,"#E3F6E3" ,"#FF8EB1" ,"#6B4F29" ,"#A37F46" ,"#6A5950" ,"#04784D" ,"#FF74FE" ,"#00A45F" ,"#8F5DF8" ,"#4B0059" ,"#D8939E" ,"#DB9D72" ,"#604143" ,"#B5BACE" ,"#989EB7" ,"#D2C4DB" ,"#A587AF" ,"#77D796" ,"#7F8C94" ,"#FF9B03" ,"#555196" ,"#31DDAE" ,"#74B671" ,"#802647" ,"#014A68" ,"#696628" ,"#4C7B6D" ,"#7A4522" ,"#3B5859" ,"#E5D381" ,"#679FA0" ,"#2C5742" ,"#9131AF" ,"#AF5D88" ,"#C7706A" ,"#61AB1F" ,"#8CF2D4" ,"#C5D9B8" ,"#9FFFFB" ,"#BF45CC" ,"#863B60" ,"#B90076" ,"#003177" ,"#C582D2" ,"#C1B394" ,"#602B70" ,"#887868" ,"#BABFB0" ,"#D1ACFE" ,"#7FDEFE" ,"#4B5C71" ,"#A3A097" ,"#637B5D" ,"#92BEA5" ,"#00F8B3" ,"#BEDDFF" ,"#3DB5A7" ,"#B6E4DE" ,"#427745" ,"#598C5A" ,"#B94C59" ,"#8181D5" ,"#94888B" ,"#FED6BD" ,"#536D31" ,"#6EFF92" ,"#E4E8FF" ,"#20E200" ,"#FFD0F2" ,"#4C83A1" ,"#BD7322" ,"#915C4E" ,"#8C4787" ,"#025117" ,"#A2AA45" ,"#A9DDB0" ,"#FF4F78" ,"#528500" ,"#009A2E" ,"#17FCE4" ,"#71555A" ,"#525D82" ,"#967874" ,"#EFBFC4" ,"#6F9755" ,"#6F7586" ,"#741D16" ,"#5EB393" ,"#B5B400" ,"#AD6552" ,"#836BBA" ,"#98AA7F" ,"#7CB9BA" ,"#5B6965" ,"#707D3D" ,"#7A001D" ,"#6E4636" ,"#AE81FF" ,"#489079" ,"#897334" ,"#009087" ,"#FF6F01" ,"#006679" ,"#370E77" ,"#4B3A83" ,"#C9E2E6" ,"#C44170" ,"#73BE54" ,"#ADFF60" ,"#00447D" ,"#DCCEC9" ,"#BD9479" ,"#656E5B" ,"#FF6EC2" ,"#7A617E" ,"#DDAEA2" ,"#77837F" ,"#A53327" ,"#608EFF" ,"#B599D7" ,"#A50149" ,"#C9B1A9" ,"#03919A" ,"#E500F1" ,"#982E0B" ,"#B67180" ,"#006039" ,"#578F9B" ,"#305230" ,"#CE934C" ,"#B3C2BE" ,"#C0BAC0" ,"#B506D3" ,"#78726D" ,"#B6602B" ,"#DDB588" ,"#497200" ,"#C5AAB6" ,"#71B2F5" ,"#A9E088" ,"#4979B0" ,"#A2C3DF" ,"#784149" ,"#0091BE" ,"#E451D1" ,"#4B4B6A" ,"#5C011A" ,"#7C8060" ,"#FF9491" ,"#4C325D" ,"#005C8B" ,"#E5FDA4" ,"#68D1B6" ,"#8683A9" ,"#A72C3E" ,"#B1BB9A" ,"#B4A04F" ,"#8D918E" ,"#A168A6" ,"#813D3A" ,"#425218" ,"#DA8386" ,"#776133" ,"#8498AE" ,"#90C1D3" ,"#B5666B" ,"#9B585E" ,"#856465" ,"#AD7C90" ,"#E2BC00" ,"#E3AAE0" ,"#B2C2FE" ,"#009B75" ,"#E87EAC" ,"#848590" ,"#AA9297" ,"#83A193" ,"#577977" ,"#3E7158" ,"#C64289" ,"#EA0072" ,"#C4A8CB" ,"#55C899" ,"#E78FCF" ,"#966716" ,"#378FDB" ,"#435E6A" ,"#5B9C8F" ,"#6E2B52" ,"#E3E8C4" ,"#AE3B85" ,"#EA1CA9" ,"#FF9E6B" ,"#457D8B" ,"#92678B" ,"#00CDBB" ,"#9CCC04" ,"#96C57F" ,"#CFF6B4" ,"#766E52" ,"#E3D19F" ,"#B2EACE" ,"#F3BDA4" ,"#A24E3D" ,"#976FD9" ,"#8C9FA8" ,"#7C2B73" ,"#4E5F37" ,"#5D5462" ,"#90956F" ,"#6AA776" ,"#DBCBF6" ,"#DA71FF" ,"#987C95" ,"#BB3C42" ,"#4FC15F" ,"#A2B9C1" ,"#79DB21" ,"#1D5958" ,"#BD744E" ,"#6B8295" ,"#00E0E4" ,"#1B782A" ,"#DAA9B5" ,"#B0415D" ,"#859253" ,"#97A094" ,"#06E3C4" ,"#47688C" ,"#7C6755" ,"#075C00" ,"#7560D5" ,"#7D9F00" ,"#C36D96" ,"#4D913E" ,"#5F4276" ,"#FCE4C8" ,"#E5A532" ,"#706690" ,"#AA9A92" ,"#237363" ,"#73013E" ,"#FF9079" ,"#A79A74" ,"#029BDB" ,"#FF0169" ,"#C7D2E7" ,"#CA8869" ,"#80FFCD" ,"#BB1F69" ,"#90B0AB" ,"#7D74A9" ,"#FCC7DB" ,"#99375B" ,"#00AB4D" ,"#ABAED1" ,"#BE9D91" ,"#E6E5A7" ,"#DD587B" ,"#6D3800" ,"#B57BB3" ,"#D7FFE6" ,"#C535A9" ,"#6A8781" ,"#A8ABB4" ,"#D45262" ,"#794B61" ,"#8DA4DB" ,"#C7C890" ,"#6FE9AD" ,"#A243A7" ,"#B2B081" ,"#286154" ,"#4CA43B" ,"#6A9573" ,"#A8441D" ,"#5C727B" ,"#738671" ,"#D0CFCB" ,"#897B77" ,"#DA9894" ,"#A1757A" ,"#63243C" ,"#ADAAFF" ,"#00CDE2" ,"#DDBC62" ,"#698EB1" ,"#208462" ,"#00B7E0" ,"#614A44" ,"#9BBB57" ,"#7A5C54" ,"#857A50" ,"#766B7E" ,"#014833" ,"#FF8347" ,"#7A8EBA" ,"#946444" ,"#EBD8E6" ,"#646241" ,"#6AD450" ,"#81817B" ,"#D499E3" ,"#979440" ,"#526554" ,"#B5885C" ,"#A499A5" ,"#03AD89" ,"#B3008B" ,"#E3C4B5" ,"#96531F" ,"#867175" ,"#74569E" ,"#617D9F" ,"#E70452" ,"#067EAF" ,"#A697B6" ,"#B787A8" ,"#9CFF93" ,"#3A9459" ,"#6E746E" ,"#B0C5AE" ,"#84EDF7" ,"#ED3488" ,"#754C78" ,"#C7847B" ,"#00B6C5" ,"#7FA670" ,"#C1AF9E" ,"#2A7FFF" ,"#72A58C" ,"#FFC07F" ,"#9DEBDD" ,"#D97C8E" ,"#7E7C93" ,"#62E674" ,"#B5639E" ,"#FFA861" ,"#C2A580" ,"#8D9C83" ,"#B70546" ,"#0098FF" ,"#985975" ,"#445083" ,"#72361F" ,"#9676A3" ,"#CED6C2" ,"#CCA763" ,"#2C7F77" ,"#02227B" ,"#A37E6F" ,"#CDE6DC" ,"#CDFFFB" ,"#BE811A" ,"#F77183" ,"#CDC6B4" ,"#FFE09E" ,"#3A7271" ,"#4E4E01" ,"#4AC684" ,"#8BC891" ,"#BC8A96" ,"#5EAADD" ,"#F6A0AD" ,"#E269AA" ,"#A3DAE4" ,"#436E83" ,"#A1C2B6" ,"#50003F" ,"#71695B" ,"#67C4BB" ,"#536EFF" ,"#5D5A48" ,"#890039" ,"#969381" ,"#5E4665" ,"#AA62C3" ,"#8D6F81" ,"#2C6135" ,"#564620" ,"#E69034" ,"#6DA6BD" ,"#E58E56" ,"#E3A68B" ,"#48B176" ,"#D27D67" ,"#B5B268" ,"#7F8427" ,"#FF84E6" ,"#435740" ,"#325800" ,"#4B6BA5" ,"#ADCEFF" ,"#9B8ACC" ,"#885138" ,"#5875C1" ,"#7E7311" ,"#FEA5CA" ,"#9F8B5B" ,"#A55B54" ,"#89006A" ,"#AF756F" ,"#7499A1" ,"#FFB550" ,"#688151" ,"#BC908A" ,"#78C8EB" ,"#5EA7FF" ,"#785715" ,"#0CEA91" ,"#B3AF9D" ,"#5A9BC2" ,"#9C2F90" ,"#8D5700" ,"#ADD79C" ,"#00768B" ,"#337D00" ,"#C59700" ,"#944575" ,"#ECFFDC" ,"#D24CB2" ,"#97703C" ,"#4C257F" ,"#9E0366" ,"#88FFEC" ,"#B56481" ,"#396D2B" ,"#56735F" ,"#988376" ,"#9BB195" ,"#A9795C" ,"#E4C5D3" ,"#9F4F67" ,"#664327" ,"#AFCE78" ,"#86B487" ,"#ABE86B" ,"#96656D" ,"#A60019" ,"#0080CF" ,"#CAEFFF" ,"#A449DC" ,"#6A9D3B" ,"#FF5AE4" ,"#636A01" ,"#D16CDA" ,"#736060" ,"#FFBAAD" ,"#D369B4" ,"#FFDED6" ,"#6C6D74" ,"#927D5E" ,"#845D70" ,"#5B62C1" ,"#AC84DD" ,"#762988" ,"#70EC98" ,"#408543" ,"#9B9EE2" ,"#58AFAD" ,"#7AC5A6" ,"#685D75" ,"#B9BCBD" ,"#834357" ,"#1A7B42" ,"#2E57AA" ,"#E55199" ,"#316E47" ,"#CD00C5" ,"#6A004D" ,"#7FBBEC" ,"#F35691" ,"#62ACB7" ,"#CBA1BC" ,"#A28A9A" ,"#6C3F3B" ,"#DCBAE3" ,"#5F816D" ,"#7DBF32" ,"#852C19" ,"#285366" ,"#B8CB9C" ,"#4B5D56" ,"#6B543F" ,"#E27172" ,"#0568EC" ,"#2EB500" ,"#D21656" ,"#EFAFFF" ,"#682021" ,"#DA4CFF" ,"#70968E" ,"#FF7B7D" ,"#E8C282" ,"#E7DBBC" ,"#A68486" ,"#36574E" ,"#52CE79" ,"#ADAAA9" ,"#8A9F45" ,"#00FB8C" ,"#5D697B" ,"#CCD27F" ,"#94A5A1" ,"#790229" ,"#E383E6" ,"#7EA4C1" ,"#620B70" ,"#314C1E" ,"#874AA6" ,"#E30091" ,"#66460A" ,"#EB9A8B" ,"#EAC3A3" ,"#98EAB3" ,"#AB9180" ,"#94DDC5" ,"#9D8C76" ,"#9C8333" ,"#94A9C9" ,"#8C675E" ,"#917100" ,"#01400B" ,"#449896" ,"#1CA370" ,"#E08DA7" ,"#8B4A4E" ,"#667776" ,"#4692AD" ,"#67BDA8" ,"#69255C" ,"#D3BFFF" ,"#4A5132" ,"#7E9285" ,"#77733C" ,"#E7A0CC" ,"#51A288" ,"#2C656A" ,"#4D5C5E" ,"#DDD7F3" ,"#005844" ,"#B4A200" ,"#488F69" ,"#858182" ,"#D4E9B9" ,"#3D7397" ,"#CAE8CE" ,"#AA6746" ,"#9E5585" ,"#BA6200"};
	List<String> colors = new ArrayList<String>();
	
	List<String> colorsRemoved = new ArrayList<String>();
	public static Color hex2Rgb(String colorStr) {
		return new Color(Integer.valueOf(colorStr.substring(1, 3), 16), Integer.valueOf(colorStr.substring(3, 5), 16),
				Integer.valueOf(colorStr.substring(5, 7), 16));
	}

	public void init() {
		JFrame frame = new JFrame("TappedOut->TTS Deck Importer");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel mainPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));

		for (int i=0; i < colors.size(); ++i) {
			String color = colors.get(i);
			JTextField colorButton = new JTextField();
			colorButton.setBorder(javax.swing.BorderFactory.createEmptyBorder());
			Dimension d = new Dimension(20, 20);
			colorButton.setSize(d);
			colorButton.setMinimumSize(d);
			colorButton.setMaximumSize(d);
			colorButton.setPreferredSize(d);
			Color c = hex2Rgb(color);
			colorButton.setBackground(c);
			colorButton.setName(i+"");
		/*	colorButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if(colors.contains(color)){
						colorsRemoved.add(color);
						colors.remove(color);	
						colorButton.setBackground(Color.BLACK);
					}else{
						colors.add(color);
						colorsRemoved.remove(color);
						colorButton.setBackground(c);
					}
				}	
			});*/
			mainPanel.add(colorButton);
		}

		frame.add(mainPanel);
		frame.setPreferredSize(new Dimension(1200, 400));
		//frame.setResizable(false);
		// Display the window.
		frame.pack();
		frame.setVisible(true);

	}

	public static void main(String[] args) {
		
		ColorDisplay test = new ColorDisplay();
		test.colors.addAll(Arrays.asList(test.colorArray));
		test.init();
	}

}