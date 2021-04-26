package sourceKDAR;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.enterprise.inject.Stereotype;

import org.jdom.Comment;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Pair;
import presenters.conflicts.ConflictWithDirection;
import presenters.detector.Detector;
import presenters.detector.RoadDetectorHBoxCell;
import presenters.detector.RoadDetectorModel;
import presenters.directions.ControlledChanelHBoxCell;
import presenters.directions.GroupControlHBoxCell;
import presenters.directions.RoadDirection;
import presenters.directions.RoadDirectionsModel;
import presenters.object.RoadObjectModel;
import presenters.object.TypeKDK;
import presenters.phase.OpenDirectionInCurrentPhaseHBoxCell;
import presenters.phase.RoadPhase;
import presenters.phase.RoadPhaseModel;
import presenters.programs.PhaseInProgram;
import presenters.programs.RoadProgram;
import presenters.programs.RoadProgramsModel;
import presenters.programs.ScheduleCalendarDateHBoxCell;
import presenters.programs.ScheduleCalendarWeekDayHBoxCell;
import presenters.programs.ScheduleProgram;
import presenters.programs.SwitchPhase;
import presenters.promtactu.InterphaseTransitionsHBoxCell;
import presenters.promtactu.PromtactData;
import presenters.promtactu.RoadPromtactuModel;
import roadModel.IRoadModel;
import roadModel.RoadModel;

public class SourceKDAR {
	
	String object;
	RoadModel roadModel;
	
	public SourceKDAR(RoadModel roadModel, String object) {
		this.roadModel = roadModel;
		this.object = object;
	}
	
	static byte[] setBits4byteBlinkingTmain(List<OpenDirectionInCurrentPhaseHBoxCell> openDir) {
		byte[] b = new byte[4];
		for(OpenDirectionInCurrentPhaseHBoxCell openDirection : openDir) {
			if(openDirection.getCheckBox().isSelected()) {
				String str = openDirection.getComboBox().getValue();
				
				int value = Integer.parseInt(str);
				if(value > 0 && value < 9) b[0] = (byte)(b[0] | (1 << value - 1));
				else if(value > 8 && value < 17) b[1] = (byte)(b[1] | (1 << value - 9));
				else if(value > 16 && value < 25) b[2] = (byte)(b[2] | (1 << value - 17));
				else if(value > 24 && value < 33) b[3] = (byte)(b[3] | (1 << value - 24));
				else {
					System.out.println("Method setBits4byteBlinkingTmain anknown value direction: " + value);
				}
			}
		}
		return b;
	}
	
	static byte[] setBits4byteOpenDir(List<OpenDirectionInCurrentPhaseHBoxCell> openDir){
		byte[] b = new byte[4];
		for(OpenDirectionInCurrentPhaseHBoxCell openDirection : openDir) {
			String str = openDirection.getComboBox().getValue();
			
			int value = Integer.parseInt(str);
			if(value > 0 && value < 9) b[0] = (byte)(b[0] | (1 << value - 1));
			else if(value > 8 && value < 17) b[1] = (byte)(b[1] | (1 << value - 9));
			else if(value > 16 && value < 25) b[2] = (byte)(b[2] | (1 << value - 17));
			else if(value > 24 && value < 33) b[3] = (byte)(b[3] | (1 << value - 24));
			else {
				System.out.println("Method setBits4byteOpenDir anknown value direction: " + value);
			}
		}
		return b;
	}
	
	static int returnTypeDirection(String type) {
		int number_of_type = -1;
		if(type.equals("Транспортное направление")) number_of_type = 0;
		else if(type.equals("Поворотная стрелка")) number_of_type = 1;
		else if(type.equals("Пешеходное")) number_of_type = 2;
		else if(type.equals("Трамвайное налево")){
			number_of_type = 4;
		}
		else if(type.equals("Трамвайное прямо")) number_of_type = 5;
		else if(type.equals("Трамвайное направо")) number_of_type = 6;
		else if(type.equals("Транспортное с одним красным")) number_of_type = 12;
		else if(type.equals("Транспортное красное и желтое")) number_of_type = 13;
		else if(type.equals("Транспортное два красных")) number_of_type = 17;
		
		if(number_of_type == -1) {
			System.out.println("Неутсновленно тип направления");
		}
		
		return number_of_type;
	}
	
	static int returnTypeProgram(String type) {
		int number_of_type = -1;
		if(type.equals("Циклическая")) number_of_type = 1;
		else if(type.equals("Циклическая с ТВП - 1")) number_of_type = 5;
		else if(type.equals("Циклическая с ТВП - 2")) number_of_type = 6;
		else if(type.equals("Циклическая с ТВП - 1 и ТВП - 2")) number_of_type = 7;
		else if(type.equals("Пешеходная с ТВП - 1")) number_of_type = 2;
		else if(type.equals("Пешеходная с ТВП - 2")) number_of_type = 3;
		else if(type.equals("Пешеходная с ТВП - 1 и ТВП - 2")) number_of_type = 4;
		else if(type.equals("Желтое мигание")) number_of_type = 8;
		else if(type.equals("Отключение светофора")) number_of_type = 9;
		
		if(number_of_type == -1) {
			System.out.println("Неутсновленно тип программы");
		}
		
		return number_of_type;
	}
	
	static int returnBackupTypeProgram(String type) {
		int number_of_type = 0;
		if(type.equals("Неисправность таймера")) number_of_type = 1;
		else if(type.equals("Неисправность ТВП - 1")) number_of_type = 2;
		else if(type.equals("Неисправность ТВП - 2")) number_of_type = 3;
		else if(type.equals("Неисправность ТВП - 1 и ТВП - 2")) number_of_type = 4;
		else if(type.equals("Неисправность детектора")) number_of_type = 5;
		
		if(number_of_type == 0) {
			System.out.println("Неутсновленно тип резервной программы");
		}
		
		return number_of_type;
	}
	
	static void setBit(int start_addr, int bit_number, byte[] stitch) {
		
		int byte_displacement;
		int bit_displacement;
		
		byte_displacement = (bit_number - 1)/8;
		bit_displacement = bit_number - (byte_displacement * 8)-1;
		start_addr = start_addr + byte_displacement;
		
		if(start_addr < stitch.length) {
			stitch[start_addr] |= (1 << bit_displacement);
		}else {
			System.out.println("Method setBit was fault");
		}
	}
	
	static int byteToInt(byte value) {
		int i = 0;
		
		i = value & 0xFF;
		
		return i;
	}

	static byte convert(int value){
		byte b = 0;
		
		if(value >= 0 && value < 256){
			if(value > 127){
				b = (byte)-(256-value);
			}else{
				b = (byte)value;
			}
		}
		return b;
	}
	
	static void bin(byte b){
		byte n;		
		n =(byte)((b & 0b10000000) >> 7);
		System.out.print(n);
		n =(byte)((b & 0b01000000) >> 6);
		System.out.print(n);
		n =(byte)((b & 0b00100000) >> 5);
		System.out.print(n);
		n =(byte)((b & 0b00010000) >> 4);
		System.out.print(n);
		n =(byte)((b & 0b00001000) >> 3);
		System.out.print(n);
		n =(byte)((b & 0b00000100) >> 2);
		System.out.print(n);
		n =(byte)((b & 0b00000010) >> 1);
		System.out.print(n);
		n =(byte)((b & 0b00000001));
		System.out.print(n);
		
	}
	
	static byte intTo_BCD(int time){
		byte b = 0;
		
		if(time <= 59){
			b = (byte) (time % 10);
			b |= (byte) ((time / 10) << 4);
		}
		
		return b;
	}
	
	static int charToASCII(char ch){
		int i = ch;
		if(i < 32 || i > 127){
			if(ch == 'Ґ') i = 165;
			else if(ch == 'Ё') i = 165;
			else if(ch == 'Є') i = 170;
			else if(ch == 'Ї') i = 175;
			else if(ch == 'І') i = 178;
			else if(ch == 'і') i = 179;
			else if(ch == 'ґ') i = 180;
			else if(ch == 'ё') i = 184;
			else if(ch == 'є') i = 186;
			else if(ch == 'ї') i = 191;
			
			else if(ch == 'А') i = 192;
			else if(ch == 'Б') i = 193;
			else if(ch == 'В') i = 194;
			else if(ch == 'Г') i = 195;
			else if(ch == 'Д') i = 196;
			else if(ch == 'Е') i = 197;
			else if(ch == 'Ж') i = 198;
			else if(ch == 'З') i = 199;
			else if(ch == 'И') i = 200;
			else if(ch == 'Й') i = 201;
			else if(ch == 'К') i = 202;
			else if(ch == 'Л') i = 203;
			else if(ch == 'М') i = 204;
			else if(ch == 'Н') i = 205;
			else if(ch == 'О') i = 206;
			else if(ch == 'П') i = 207;
			else if(ch == 'Р') i = 208;
			else if(ch == 'С') i = 209;
			else if(ch == 'Т') i = 210;
			else if(ch == 'У') i = 211;
			else if(ch == 'Ф') i = 212;
			else if(ch == 'Х') i = 213;
			else if(ch == 'Ц') i = 214;
			else if(ch == 'Ч') i = 215;
			else if(ch == 'Ш') i = 216;
			else if(ch == 'Щ') i = 217;
			else if(ch == 'Ъ') i = 218;
			else if(ch == 'Ы') i = 219;
			else if(ch == 'Ь') i = 220;
			else if(ch == 'Э') i = 221;
			else if(ch == 'Ю') i = 222;
			else if(ch == 'Я') i = 223;
			
			else if(ch == 'а') i = 224;
			else if(ch == 'б') i = 225;
			else if(ch == 'в') i = 226;
			else if(ch == 'г') i = 227;
			else if(ch == 'д') i = 228;
			else if(ch == 'е') i = 229;
			else if(ch == 'ж') i = 230;
			else if(ch == 'з') i = 231;
			else if(ch == 'и') i = 232;
			else if(ch == 'й') i = 233;
			else if(ch == 'к') i = 234;
			else if(ch == 'л') i = 235;
			else if(ch == 'м') i = 236;
			else if(ch == 'н') i = 237;
			else if(ch == 'о') i = 238;
			else if(ch == 'п') i = 239;
			else if(ch == 'р') i = 240;
			else if(ch == 'с') i = 241;
			else if(ch == 'т') i = 242;
			else if(ch == 'у') i = 243;
			else if(ch == 'ф') i = 244;
			else if(ch == 'х') i = 245;
			else if(ch == 'ц') i = 246;
			else if(ch == 'ч') i = 247;
			else if(ch == 'ш') i = 248;
			else if(ch == 'щ') i = 249;
			else if(ch == 'ъ') i = 250;
			else if(ch == 'ы') i = 251;
			else if(ch == 'ь') i = 252;
			else if(ch == 'э') i = 253;
			else if(ch == 'ю') i = 254;
			else if(ch == 'я') i = 255;
			else i = 88;
		
		}
		
		return i; 
	}
	
	static int getState(PromtactData promtactData) {
		int i = 0;
		
		if(!promtactData.getRoadPromtactu_endGreenAddit().equals("") && !promtactData.getRoadPromtactu_durationGreenBlink().equals("")
				&& !promtactData.getRoadPromtactu_durationYellow().equals("")) {
			i = 3;
		}else {
			i = 2;
		}
		
		return i;
	}
	
	
	public static String transliterate(String message){
	    char[] abcCyr =   {' ','а','б','в','г','д','е','ё', 'ж','з','и','й','к','л','м','н','о','п','р','с','т','у','ф','х', 'ц','ч', 'ш','щ','ъ','ы','ь','э', 'ю','я',
				'А','Б','В','Г','Д','Е','Ё', 'Ж','З','И','Й','К','Л','М','Н','О','П','Р','С','Т','У','Ф','Х', 'Ц', 'Ч','Ш', 'Щ','Ъ','Ы','Ь','Э','Ю','Я',
				'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z',
				'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
	    String[] abcLat = {" ","a","b","v","g","d","e","e","zh","z","i","y","k","l","m","n","o","p","r","s","t","u","f","h","ts","ch","sh","sch",
				"","i", "","e","ju","ja","A","B","V","G","D","E","E","Zh","Z","I","Y","K","L","M","N","O","P","R","S","T","U","F","H","Ts","Ch","Sh","Sch",
				"","I", "","E","Ju","Ja","a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z",
				"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
	    StringBuilder builder = new StringBuilder();
	    for (int i = 0; i < message.length(); i++) {
	        for (int x = 0; x < abcCyr.length; x++ ) {
	            if (message.charAt(i) == abcCyr[x]) {
	                builder.append(abcLat[x]);
	            }
	        }
	    }
	    return builder.toString();
	}
	
	
	/*static String toLatin(String string) {
		String name = null;
		
		char[] ch_string = string.toCharArray();
		
		
		
		
		return name;
	}*/
	
	
	/**
	 * @param path
	 */
	public void createBinary(File path) {
		RoadObjectModel roadObjectModel = roadModel.getRoadObjectModel();
		RoadDirectionsModel roadDirectionsModel = roadModel.getRoadDirectionModel();
		RoadPhaseModel roadPhaseModel = roadModel.getRoadPhaseModel();
		RoadProgramsModel roadProgramsModel = roadModel.getRoadProgramsModel();
		RoadPromtactuModel roadPromtactuModel = roadModel.getRoadPromtactuModel();
		
		byte[] stitch = new byte[4096];
		
		final int DEFINE_VALUE_RED_CHANEL_CONTROL = 1;
		
		int max_channel = 0;
		int max_phase = 0;
		int count_direction;
		int max_directions = 0;
		
		
		List<RoadDirection> allDirections = roadDirectionsModel.getRoadDirectionList();		// get max value of channels
		for(RoadDirection direction : allDirections) {
			if(!direction.getRoadDirections_chanal_1().equals("")) {
				max_channel = Integer.parseInt(direction.getRoadDirections_chanal_1());
			}
			if(!direction.getRoadDirections_chanal_2().equals("")) {
				max_channel = Integer.parseInt(direction.getRoadDirections_chanal_2());
			}
			if(!direction.getRoadDirections_chanal_3().equals("")) {
				max_channel = Integer.parseInt(direction.getRoadDirections_chanal_3());
			}
			if(!direction.getRoadDirections_chanal_4().equals("")) {
				max_channel = Integer.parseInt(direction.getRoadDirections_chanal_4());
			}
			
		}
		
		List<RoadPhase> allPhases = roadPhaseModel.getRoadPhaseList();		// get max number of phase
		for(RoadPhase phase : allPhases) {
			max_phase = Integer.parseInt(phase.getRoadPhase_number());
		}
		
		
		List<RoadDirection> allDirection = roadDirectionsModel.getRoadDirectionList();	// get max number of direction
		for(RoadDirection direction : allDirection) {
			max_directions = Integer.parseInt(direction.getRoadDirections_number());
		}		
		
		count_direction = roadDirectionsModel.getRoadDirectionList().size();	// count directions of object
		
		byte max_chan = convert(max_channel);														//$0000
		byte max_phases = convert(max_phase);														//$0001
		byte count_direc = convert(count_direction);												//$0002
		byte max_direc = convert(max_directions);													//$0003
		
		stitch[0] = max_chan;
		stitch[1] = max_phases;
		stitch[2] = count_direc;
		stitch[3] = max_direc;
		
		int magisrtal = 0x00;	// magistral
		if(!roadObjectModel.getRoadObjectMagistral().equals("")) {
			magisrtal = Integer.parseInt(roadObjectModel.getRoadObjectMagistral());
		}
		byte magistral_value = convert(magisrtal);
		stitch[6] = magistral_value;
		
		//int magisrtal = 0x00;	// magistral														$0006
		
		
		int net_address = 0;		// network address												// $0007-$0008
		if(!roadObjectModel.getRoadObjectNetworkAddress().equals("")) {
			net_address = Integer.parseInt(roadObjectModel.getRoadObjectNetworkAddress());
			int i_1 = 0;
			int i_2 = 0;
			i_1 = net_address & 0x0000ff00;
			i_1 >>= 8;
			i_2 = net_address & 0x000000ff;	
			stitch[7] = convert(i_1);
			stitch[8] = convert(i_2);
		}
		
		
		String protocol_string = roadObjectModel.getRoadObjectProtocol();		// protocol			$0009
		if(protocol_string.equals("Радио 'КОМКОН' - 1200")) {
			int protocol_int = 0x03;
			byte protocol = convert(protocol_int);
			stitch[9] = protocol;
		}
		if(protocol_string.equals("Ethernet 'КОМКОН' - 115200")) {
			int protocol_int = 0x0D;
			byte protocol = convert(protocol_int);
			stitch[9] = protocol;
		}
		if(protocol_string.equals("Радио 'Росток'")) {
			int protocol_int = 0x04;
			byte protocol = convert(protocol_int);
			stitch[9] = protocol;
		}
		if(protocol_string.equals("Харьков(1 линия)")) {
			int protocol_int = 0x06;
			byte protocol = convert(protocol_int);
			stitch[9] = protocol;
		}
		if(protocol_string.equals("Харьков(2 линия)")) {
			int protocol_int = 0x07;
			byte protocol = convert(protocol_int);
			stitch[9] = protocol;
		}
		if(protocol_string.equals("Житомир(1 линия)")) {
			int protocol_int = 0x0A;
			byte protocol = convert(protocol_int);
			stitch[9] = protocol;
		}
		if(protocol_string.equals("Житомир(2 линия)")) {
			int protocol_int = 0x0B;
			byte protocol = convert(protocol_int);
			stitch[9] = protocol;
		}
		if(protocol_string.equals("Луганск(1 линия)")) {
			int protocol_int = 0x08;
			byte protocol = convert(protocol_int);
			stitch[9] = protocol;
		}
		if(protocol_string.equals("Луганск(2 линия)")) {
			int protocol_int = 0x09;
			byte protocol = convert(protocol_int);
			stitch[9] = protocol;
		}
		
		int default_value_kodeOfStitch = 11;	// default value kode of stitch(use Mega 128)			// $000A
		byte stitch_kode = (byte)default_value_kodeOfStitch;
		stitch[10] = stitch_kode;		
		
		List<RoadPhase> roadPhasesList1 = roadPhaseModel.getRoadPhaseList();		// TVP phase and TVP table
		
		for(RoadPhase roadPhase : roadPhasesList1) {
			
			int number_value = Integer.parseInt(roadPhase.getRoadPhase_number());
			
			
			if(roadPhase.getRoadPhase_phaseTVP().getTvp().equals("ТВП - 1")) {							// $000B
				byte number = convert(number_value);
				stitch[11] = number;
				if(!roadPhase.getRoadPhase_panelTVP_1().equals("")) {
					int ch = Integer.parseInt(roadPhase.getRoadPhase_panelTVP_1());						// $000D
					byte channel = convert(ch);
					stitch[13] = channel;
				}
				
				
				
			}
			
			if(roadPhase.getRoadPhase_phaseTVP().getTvp().equals("ТВП - 2")) {							// $000C
				byte number = convert(number_value);
				stitch[12] = number;
				if(!roadPhase.getRoadPhase_panelTVP_2().equals("")) {
					int ch = Integer.parseInt(roadPhase.getRoadPhase_panelTVP_2());						// $000E
					byte channel = convert(ch);
					stitch[14] = channel;
				}
			}
			
			
						
			if(roadPhase.getRoadPhase_phaseTVP().getTvp().equals("ТВП - 1 и ТВП - 2")) {				// $000B-$000C
				byte number = convert(number_value);
				stitch[11] = number;
				stitch[12] = number;
				stitch[15] |= 0x04;
				if(!roadPhase.getRoadPhase_panelTVP_1().equals("")) {
					int ch = Integer.parseInt(roadPhase.getRoadPhase_panelTVP_1());						// $000D
					byte channel = convert(ch);
					stitch[13] = channel;
				}
				if(!roadPhase.getRoadPhase_panelTVP_2().equals("")) {
					int ch = Integer.parseInt(roadPhase.getRoadPhase_panelTVP_2());						// $000E
					byte channel = convert(ch);
					stitch[14] = channel;
				}
			}
			
			if(roadPhase.getRoadPhase_phaseTVP().getTvp().equals("ТВП - 1 или ТВП - 2")) {				// $000B-$000C
				byte number = convert(number_value);
				stitch[11] = number;
				stitch[12] = number;
				stitch[15] |= 0x02;
				if(!roadPhase.getRoadPhase_panelTVP_1().equals("")) {
					int ch = Integer.parseInt(roadPhase.getRoadPhase_panelTVP_1());						// $000D
					byte channel = convert(ch);
					stitch[13] = channel;
				}
				if(!roadPhase.getRoadPhase_panelTVP_2().equals("")) {
					int ch = Integer.parseInt(roadPhase.getRoadPhase_panelTVP_2());						// $000E
					byte channel = convert(ch);
					stitch[14] = channel;
				}
			}
		} 
		
		
		String dirCharge = roadObjectModel.getRoadObjectCharge();	// loading of direction				$000F
		if(dirCharge.equals("Желтый")) {
			stitch[15] |= 0x01;
		}
		
		int define_mega128_1 = 0x45;				// version for the processor program              	$0012-$0013
		int define_mega128_2 = 0x02;
		byte version_stitch_for_procesor_1 = convert(define_mega128_1);
		byte version_stitch_for_procesor_2 = convert(define_mega128_2);
		stitch[18] = version_stitch_for_procesor_1;
		stitch[19] = version_stitch_for_procesor_2;
		
		
		String str_country = roadObjectModel.getRoadObjectCountry();	// country						$0037-$003A
		if(!str_country.equals("")) {
			char[] country_array = str_country.toCharArray();
			for(int i = 0; i < 4 && i < country_array.length; i++) {
				int later = charToASCII(country_array[i]);
				byte country = convert(later);
				stitch[55+i] = country;
			}
		}
		
		String str_trough = roadObjectModel.getRoadObjectRegion();		// trough						$003B-$004A
		if(!str_trough.equals("")) {
			char[] trough_array = str_trough.toCharArray();
			for(int i = 0; i < 16 && i < trough_array.length; i++) {
				int later = charToASCII(trough_array[i]);
				byte trough = convert(later);
				stitch[59+i] = trough;
			}
		}
		
		String str_city = roadObjectModel.getRoadObjectCity();			// city							$004B-$005A
		if(!str_city.equals("")) {
			char[] city_array = str_city.toCharArray();
			for(int i = 0; i < 16 && i < city_array.length; i++) {
				int later = charToASCII(city_array[i]);
				byte city = convert(later);
				stitch[75+i] = city;
			}
		}
		
		String str_name = roadObjectModel.getRoadObjectName();			// object name					$005B-$007A
		if(!str_name.equals("")) {
			char[] name_array = str_name.toCharArray();
			for(int i = 0; i < 32 && i < name_array.length; i++) {
				int later = charToASCII(name_array[i]);
				byte name = convert(later);
				stitch[91+i] = name;
			}
		}
		
		String str_date_create = roadObjectModel.getRoadObjectDateOfCreation();	// date of creating		$007B-$007F
		if(!str_date_create.equals("")) {
			int day_value = Integer.parseInt(str_date_create.substring(0, 2));
			int mounth_value = Integer.parseInt(str_date_create.substring(3, 5));
			int year_value = Integer.parseInt(str_date_create.substring(8));
			byte day = convert(day_value);
			byte mounth = convert(mounth_value);
			byte year = convert(year_value);
			stitch[125] = day;
			stitch[126] = mounth;
			stitch[127] = year;
		}
		
		
		
		
		List<RoadDirection> roadDirectionsList = roadDirectionsModel.getRoadDirectionList();	// directions, red control	$0080-$017F
		for(RoadDirection roadDirection : roadDirectionsList) {
			int channel1red_value = 0;
			int channel2yellow_value = 0;
			int channel3green_value = 0;
			int channel4edditred_value = 0;
			
			if(!roadDirection.getRoadDirections_chanal_1().equals("")) {	// red channel
				byte b1 = 0;
				byte b2 = 0;
				byte b3 = 0;
				byte b4 = 0;
				
				channel1red_value = Integer.parseInt(roadDirection.getRoadDirections_chanal_1());
				b1 = (byte)(1 << 5);
				int dir_number = Integer.parseInt(roadDirection.getRoadDirections_number());
				b1 = (byte)(b1 | (dir_number - 1));
				
				int dir_type = returnTypeDirection(roadDirection.getRoadDirections_typeOfDirection().getTypDirection());
				b2 = convert(dir_type);
				
				int control1 = 0;
				int control2 = 0;
				
				if(!roadDirection.getRoadDirections_control_1().equals("")) {
					control1 = Integer.parseInt(roadDirection.getRoadDirections_control_1());
				}
				if(!roadDirection.getRoadDirections_control_2().equals("")) {
					control2 = Integer.parseInt(roadDirection.getRoadDirections_control_2());
				}
				
				if(channel1red_value == control1 || channel1red_value == control2) {	// if is control
					
					b3 = convert(DEFINE_VALUE_RED_CHANEL_CONTROL);	// define
					b4 = convert(DEFINE_VALUE_RED_CHANEL_CONTROL);	// define
				}
				
				stitch[128 + (channel1red_value - 1)*4] = b1;
				stitch[128 + (channel1red_value - 1)*4 + 1] = b2;
				stitch[128 + (channel1red_value - 1)*4 + 2] = b3;
				stitch[128 + (channel1red_value - 1)*4 + 3] = b4;
				
			}
			
			if(!roadDirection.getRoadDirections_chanal_2().equals("")) {	// yellow channel
				byte b1 = 0;
				byte b2 = 0;
				byte b3 = 0;
				byte b4 = 0;
				
				channel2yellow_value = Integer.parseInt(roadDirection.getRoadDirections_chanal_2());
				b1 = (byte)(3 << 5);
				int dir_number = Integer.parseInt(roadDirection.getRoadDirections_number());
				b1 = (byte)(b1 | (dir_number - 1));
				
				int dir_type = returnTypeDirection(roadDirection.getRoadDirections_typeOfDirection().getTypDirection());
				b2 = convert(dir_type);
				
				stitch[128 + (channel2yellow_value - 1)*4] = b1;
				stitch[128 + (channel2yellow_value - 1)*4 + 1] = b2;
				stitch[128 + (channel2yellow_value - 1)*4 + 2] = b3;
				stitch[128 + (channel2yellow_value - 1)*4 + 3] = b4;
				
			}
			
			if(!roadDirection.getRoadDirections_chanal_3().equals("")) {	// green channel
				byte b1 = 0;
				byte b2 = 0;
				byte b3 = 0;
				byte b4 = 0;
				
				channel3green_value = Integer.parseInt(roadDirection.getRoadDirections_chanal_3());
				b1 = (byte)(5 << 5);
				int dir_number = Integer.parseInt(roadDirection.getRoadDirections_number());
				b1 = (byte)(b1 | (dir_number - 1));
				
				int dir_type = returnTypeDirection(roadDirection.getRoadDirections_typeOfDirection().getTypDirection());
				b2 = convert(dir_type);
				
				stitch[128 + (channel3green_value - 1)*4] = b1;
				stitch[128 + (channel3green_value - 1)*4 + 1] = b2;
				stitch[128 + (channel3green_value - 1)*4 + 2] = b3;
				stitch[128 + (channel3green_value - 1)*4 + 3] = b4;
				
			}
			
			if(!roadDirection.getRoadDirections_chanal_4().equals("")) {	// additional red channel
				byte b1 = 0;
				byte b2 = 0;
				byte b3 = 0;
				byte b4 = 0;
				
				channel4edditred_value = Integer.parseInt(roadDirection.getRoadDirections_chanal_4());
				b1 = (byte)(2 << 5);
				int dir_number = Integer.parseInt(roadDirection.getRoadDirections_number());
				b1 = (byte)(b1 | (dir_number - 1));
				
				int dir_type = returnTypeDirection(roadDirection.getRoadDirections_typeOfDirection().getTypDirection());
				b2 = convert(dir_type);
				
				int control1 = Integer.parseInt(roadDirection.getRoadDirections_control_1());
				int control2 = Integer.parseInt(roadDirection.getRoadDirections_control_2());
				if(channel4edditred_value == control1 || channel4edditred_value == control2) {	// if is control
					
					b3 = convert(DEFINE_VALUE_RED_CHANEL_CONTROL);	// define
					b4 = convert(DEFINE_VALUE_RED_CHANEL_CONTROL);	// define
				}
				
				stitch[128 + (channel4edditred_value - 1)*4] = b1;
				stitch[128 + (channel4edditred_value - 1)*4 + 1] = b2;
				stitch[128 + (channel4edditred_value - 1)*4 + 2] = b3;
				stitch[128 + (channel4edditred_value - 1)*4 + 3] = b4;
				
			}
		}
		
		
		List<RoadPhase> roadPhasesList = roadPhaseModel.getRoadPhaseList();		// phases, Tmin, openDirections, blinking Tmain		
		Map<String, List<OpenDirectionInCurrentPhaseHBoxCell>> mapOpenDirectionInPhase = roadPhaseModel.getMapOpenDirectionInPhase();
		for(RoadPhase roadPhase : roadPhasesList) {
			int phase_number = Integer.parseInt(roadPhase.getRoadPhase_number());
			int tmin_value = Integer.parseInt(roadPhase.getRoadPhase_Tmin());																// $0180-$018F
			byte tMin = convert(tmin_value);
			stitch[384 + phase_number - 1] = tMin;
			
			String str_number = roadPhase.getRoadPhase_number();
			List<OpenDirectionInCurrentPhaseHBoxCell> openDirectionsList = mapOpenDirectionInPhase.get(str_number);
			
			byte[] openDirection = setBits4byteOpenDir(openDirectionsList);																	// $0190-$01CF
			int start_index_openDir = 400 + ((phase_number - 1) * 4);
			for(int i = 0; i < 4 && i < openDirection.length; i++) {
				stitch[start_index_openDir + i] = openDirection[i];
			}
			
			byte[] blinkingTmain = setBits4byteBlinkingTmain(openDirectionsList);															// $01D0-$020F
			int start_index_blinking = 464 + ((phase_number - 1) * 4);
			for(int i = 0; i < 4 && i < blinkingTmain.length; i++) {
				stitch[start_index_blinking + i] = blinkingTmain[i];
			}
			
			
		}
		
		
		Map<String, List<ConflictWithDirection>> mapOfConflict = roadModel.getRoadConflictsModel().getMapOfConflict();	// table conflicts 		$0210-$028F						
		for(Map.Entry<String, List<ConflictWithDirection>> entry : mapOfConflict.entrySet()) {
			int start_dir_number = Integer.parseInt(entry.getKey());
			start_dir_number = 528 + ((start_dir_number - 1)*4);
			
			List<ConflictWithDirection> conflictList = entry.getValue();
			for(ConflictWithDirection conflictDirection : conflictList) {
				int conflict_dir_number = Integer.parseInt(conflictDirection.getConflictWithDirection());
				
				setBit(start_dir_number, conflict_dir_number, stitch);
				
			}
		}
		
		
		Map<GroupControlHBoxCell, List<ControlledChanelHBoxCell>> groupControlHBoxCellListMap = roadDirectionsModel.getGroupControlHBoxCellListMap();	// group control 	$0290-$02CF
		for(Map.Entry<GroupControlHBoxCell, List<ControlledChanelHBoxCell>> entry : groupControlHBoxCellListMap.entrySet()) {
			int group_number = Integer.parseInt(entry.getKey().getNumberOfControl().getText());
			int start_index = 656 + ((group_number - 1)*4);
			
			List<ControlledChanelHBoxCell> controledChannelList = entry.getValue();
			for(ControlledChanelHBoxCell chennal : controledChannelList) {
				int chennal_value = Integer.parseInt(chennal.getTextFieldChanel().getText());
				
				setBit(start_index, chennal_value, stitch);
				
			}
		}
		
		
		List<RoadProgram> programList = roadProgramsModel.getRoadProgramList();																					// Time program array  $02D0-$0527
		Map<RoadProgram, List<PhaseInProgram>> mapOfPhasesInProgram = roadProgramsModel.getMapOfPhasesInProgram();
		Map<InterphaseTransitionsHBoxCell, Map<String, PromtactData>> mapOfInterphaseSpecificPromtact = roadPromtactuModel.getMapOfInterphaseSpecificPromtact();
		Map<String, PromtactData> mapOfBasePromtact = roadDirectionsModel.getMapOfBasePromtact();
		
		for(RoadProgram mainProgram : programList) {
			
			int start_number = Integer.parseInt(mainProgram.getRoadProgram_number());
			int start_index = 720 + ((start_number - 1)*25);
			int type = returnTypeProgram(mainProgram.getRoadProgram_programMode().getMode());
			
			if(mapOfPhasesInProgram.containsKey(mainProgram)) {
				
				List<PhaseInProgram> phaseInProgramsList = mapOfPhasesInProgram.get(mainProgram);
				
				int phases_count = phaseInProgramsList.size();
				
				List<Integer> allPromtactValues;
				//Map<Integer, Integer> mapOfTmain = new LinkedHashMap<Integer, Integer>();
				List<Pair<Integer,Integer>> pairsOfTmain = new ArrayList<Pair<Integer,Integer>>();
				for (int i = 0; i < phaseInProgramsList.size(); i++) {							// generate T main phases
					PhaseInProgram phaseInProgramFrom = phaseInProgramsList.get(i);
					PhaseInProgram phaseInProgramTo;
					
					if (i == phaseInProgramsList.size() - 1) {
						phaseInProgramTo = phaseInProgramsList.get(0);
					}else{
						phaseInProgramTo = phaseInProgramsList.get(i + 1);
					}
					
					int duration = 0;
					int promtact = 0;
					boolean isSpecPromtact = false;
					
					String phaseInProgramNumberFrom = phaseInProgramFrom.getPhaseInProgramNumber().getPhaseNumber();
					String phaseInProgramNumberTo = phaseInProgramTo.getPhaseInProgramNumber().getPhaseNumber();
					
					String phaseDurationFrom = phaseInProgramFrom.getDurationPhaseInProgram(); // duration phase in program
					duration = Integer.parseInt(phaseDurationFrom);
					
					for (Map.Entry<InterphaseTransitionsHBoxCell, Map<String, PromtactData>> entryPromtact : mapOfInterphaseSpecificPromtact.entrySet()) {
						String specPromFrom = entryPromtact.getKey().getComboBoxFromPhase().getValue();
						String specPromTo = entryPromtact.getKey().getComboBoxToPhase().getValue();
	
						if (phaseInProgramNumberFrom.equals(specPromFrom) && phaseInProgramNumberTo.equals(specPromTo)) {
							isSpecPromtact = true;
							Map<String, PromtactData> specPromMap = entryPromtact.getValue();
							allPromtactValues = new ArrayList<>();
	
							for (Map.Entry<String, PromtactData> entrySpecPromtact : specPromMap.entrySet()) {
								PromtactData promtactData = entrySpecPromtact.getValue();
								String greenBlink = promtactData.getRoadPromtactu_durationGreenBlink();
								String greenAddit = promtactData.getRoadPromtactu_endGreenAddit();
								String yellow = promtactData.getRoadPromtactu_durationYellow();
								String red = promtactData.getRoadPromtactu_endRed();
								String redYellow = promtactData.getRoadPromtactu_durationRedYellow();
	
								if (!greenBlink.equals("")) {
									allPromtactValues.add(Integer.parseInt(greenBlink));
								}
								if (!greenAddit.equals("")) {
									allPromtactValues.add(Integer.parseInt(greenAddit));
								}
								if (!yellow.equals("")) {
									allPromtactValues.add(Integer.parseInt(yellow));
								}
								if (!red.equals("")) {
									allPromtactValues.add(Integer.parseInt(red));
								}
								if (!redYellow.equals("")) {
									allPromtactValues.add(Integer.parseInt(redYellow));
								}
							}
							promtact = Collections.max(allPromtactValues);
							break;
						}
					}
					
					if (isSpecPromtact == false) {
						Map<String, List<OpenDirectionInCurrentPhaseHBoxCell>> openDirectionsInPhase = roadModel.getRoadPhaseModel().getMapOpenDirectionInPhase();
						List<OpenDirectionInCurrentPhaseHBoxCell> diferent = new ArrayList<>();
						allPromtactValues = new ArrayList<>();
	
						List<OpenDirectionInCurrentPhaseHBoxCell> openDirectionsFromList = openDirectionsInPhase.get(phaseInProgramNumberFrom);
						List<OpenDirectionInCurrentPhaseHBoxCell> openDirectionsToList = openDirectionsInPhase.get(phaseInProgramNumberTo);
	
						for (OpenDirectionInCurrentPhaseHBoxCell existed : openDirectionsFromList) {
							if (!openDirectionsToList.contains(existed)) {
								diferent.add(existed);
							}
						}
						for (OpenDirectionInCurrentPhaseHBoxCell difDirection : diferent) {
							if (mapOfBasePromtact.containsKey(difDirection.getComboBox().getValue())) {
								PromtactData promtactData = mapOfBasePromtact.get(difDirection.getComboBox().getValue());
								String value = promtactData.getRoadPromtactu_endGreenAddit(); //
	
								allPromtactValues.add(Integer.parseInt(value));
							}
						}
						
						diferent.clear();
						
						for (OpenDirectionInCurrentPhaseHBoxCell existed : openDirectionsToList) {
							if (!openDirectionsFromList.contains(existed)) {
								diferent.add(existed);
							}
						}
						for (OpenDirectionInCurrentPhaseHBoxCell difDirection : diferent) {
							if (mapOfBasePromtact.containsKey(difDirection.getComboBox().getValue())) {
								PromtactData promtactData = mapOfBasePromtact.get(difDirection.getComboBox().getValue());
								String value = promtactData.getRoadPromtactu_endRed();
	
								allPromtactValues.add(Integer.parseInt(value));
							}
						}
						
						
						promtact = Collections.max(allPromtactValues);
						
						
					}
					
					int tMain = duration - promtact;
					int phaseNumber = Integer.parseInt(phaseInProgramsList.get(i).getPhaseInProgramNumber().getPhaseNumber());
					Pair<Integer, Integer> durationPair = new Pair<Integer, Integer>(phaseNumber, tMain);
					pairsOfTmain.add(durationPair);
					//mapOfTmain.put(phaseNumber, tMain);
					
				}
				
				byte b_type = (byte)((type << 4) | (phases_count));
				stitch[start_index] = b_type;
				
				int temp_index = 0;
				
				for(Pair<Integer, Integer> entryPairs: pairsOfTmain) {
					temp_index++;
					int phase_number = entryPairs.getKey();
					int T_main = entryPairs.getValue();
					
					if(temp_index % 2 == 0) {
						stitch[start_index + (temp_index / 2)] |= ((phase_number) << 4);
					}else {
						stitch[start_index + ((temp_index / 2) + 1)] |= (phase_number);
					}
					
					stitch[start_index + 8 + temp_index] = convert(T_main);
				}
				
				/*for(Map.Entry<Integer, Integer> entryPhase : mapOfTmain.entrySet()) {
					temp_index++;
					int phase_number = entryPhase.getKey();
					int T_main = entryPhase.getValue();
					
					if(temp_index % 2 == 0) {
						stitch[start_index + (temp_index / 2)] |= ((phase_number) << 4);
					}else {
						stitch[start_index + ((temp_index / 2) + 1)] |= (phase_number);
					}
					
					stitch[start_index + 8 + temp_index] = convert(T_main);
					
				}*/
				
			}else {
				
				byte b_type = (byte)(type << 4);
				stitch[start_index] = b_type;
				
			}
		}
		
		
		for(RoadProgram mainProgram : programList) {														// BACKUP PROGRAM 		$0528-$052C
			
			int program_number = Integer.parseInt(mainProgram.getRoadProgram_number());
			int backup_type = returnBackupTypeProgram(mainProgram.getRoadProgram_backupProgram().getBackupProgram());
			
			byte prog_number = convert(program_number);
			
			if(backup_type > 0) {
				stitch[1319 + backup_type] = prog_number;
			}
			
		}
		
		
		
		Map<ScheduleCalendarWeekDayHBoxCell, List<ScheduleProgram>> mapOfWeekCalendar = roadProgramsModel.getMapOfWeekCalendar();		//schedule program    $0665-$0904
		//Map<Integer, Integer> pointerMap = new LinkedHashMap<>();
		Map<String, Integer> pointerMap = new LinkedHashMap<>();
		
		int prog_index = 0;
		int start_index = 1637;
		
		int weekDay_index = 0;
		String weekDay;
		for(Map.Entry<ScheduleCalendarWeekDayHBoxCell, List<ScheduleProgram>> entrySchedule : mapOfWeekCalendar.entrySet()) {
			prog_index = 0;
			weekDay = entrySchedule.getKey().getWeekDay().getText();
			
			if(weekDay_index != 0) {
				//prog_index++;
				
				//stitch[start_index + (prog_index - 1) *4] = (byte)0;	// end day array
			
				start_index = 1637 + (weekDay_index * 96); 
			}
			
			pointerMap.put(weekDay, start_index );//+ (prog_index)*4);
			
			List<ScheduleProgram> scheduleList = entrySchedule.getValue();
			for(ScheduleProgram scheduleProgram : scheduleList) {
				
				prog_index++;
				
				int schedule_prog_number = Integer.parseInt(scheduleProgram.getNumberOfScheduleProgram().getScheduleNumber());
				String time = scheduleProgram.getTimeONOfScheduleProgram();
				int start_time_hour = Integer.parseInt(time.substring(0, 2));
				int start_time_minute = Integer.parseInt(time.substring(3, 5));
				int offset_time = Integer.parseInt(scheduleProgram.getDisplacementTimeOfScheduleProgram());
				
				for(RoadProgram mainProgram : programList) {
					int mainProgram_number = Integer.parseInt(mainProgram.getRoadProgram_number());
					
					if(schedule_prog_number == mainProgram_number) {
						
						if(mainProgram.getRoadProgram_programMode().getMode().equals("Желтое мигание")) {
							stitch[start_index + (prog_index - 1)*4] = (byte)schedule_prog_number; // 0xFE
							
						}
						else if(mainProgram.getRoadProgram_programMode().getMode().equals("Отключение светофора")) {
							stitch[start_index + (prog_index - 1)*4] = (byte)schedule_prog_number; // 0xFF
						}
						else {
							stitch[start_index + (prog_index - 1)*4] = (byte)schedule_prog_number;
						}
						
						stitch[start_index + (prog_index - 1)*4 + 1] = intTo_BCD(start_time_hour);
						stitch[start_index + (prog_index - 1)*4 + 2] = intTo_BCD(start_time_minute);
						stitch[start_index + (prog_index - 1)*4 + 3] = intTo_BCD(offset_time);
						
						
					}
					
				}

			}
			if(prog_index < 24)			{
				prog_index++;
				stitch[start_index + (prog_index - 1) *4] = (byte)0;	// end day array
			}
			else {
				System.out.println("Day have more of 24 program");
			}
			
			
			weekDay_index++;
		}
		
		System.out.println(pointerMap);
		
		Map<ScheduleCalendarDateHBoxCell, List<ScheduleProgram>> mapOfDateCalendar = roadProgramsModel.getMapOfDateCalendar();		// Calendar by date 		$052D-05A4
		int start_date_index = 1325;
		int index_date = 0;
		for(Map.Entry<ScheduleCalendarDateHBoxCell, List<ScheduleProgram>> entryCalendarByDate : mapOfDateCalendar.entrySet()) {
			
			ScheduleCalendarDateHBoxCell scheduleByDate = entryCalendarByDate.getKey();
			String weekDayKey = scheduleByDate.getChoiceBox().getValue();
			
			String strDate = scheduleByDate.getDatePicker().getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			
			int schedule_date = Integer.parseInt(strDate.substring(8, 10));
			int schedule_mounth = Integer.parseInt(strDate.substring(5, 7));
			
			int absolute_address = pointerMap.get(weekDayKey);
			
			byte first_byte = (byte)intTo_BCD(schedule_date);
			byte second_byte = (byte)intTo_BCD(schedule_mounth);
			
			byte third_byte = (byte)(absolute_address & 0x0f);
			byte fourth_byte = (byte)((absolute_address & 0xf0) >> 8);
			
			stitch[start_date_index + (index_date)*4] = first_byte;
			stitch[start_date_index + (index_date)*4 + 1] = second_byte;
			stitch[start_date_index + (index_date)*4 + 2] = third_byte;
			stitch[start_date_index + (index_date)*4 + 3] = fourth_byte;
			
			index_date += 4;
		}
		
		
		
		int index = 2309;
		
		
		
		for(Map.Entry<InterphaseTransitionsHBoxCell, Map<String, PromtactData>> specificEntry : mapOfInterphaseSpecificPromtact.entrySet()) {	// specific promtact 	$0905-MAX address $0FFD
			
			InterphaseTransitionsHBoxCell specific = specificEntry.getKey();
			Map<String, PromtactData> specificPromtactMap = specificEntry.getValue();
			
			int from_phase_number = Integer.parseInt(specific.getComboBoxFromPhase().getValue());
			int to_phase_number = Integer.parseInt(specific.getComboBoxToPhase().getValue());			
			
			/*byte first_byte = (byte)((from_phase_number << 4) | to_phase_number);
			stitch[index] = first_byte;
			index++;*/
			
			
			Map<String, PromtactData> inequalityPromtactMap = new LinkedHashMap<String, PromtactData>();
			
			for(Map.Entry<String, PromtactData> specificPromtactEntry : specificPromtactMap.entrySet()) {
				
				String specificDirection = specificPromtactEntry.getKey();
				PromtactData specPromtact = specificPromtactEntry.getValue();
				
				if(specPromtact.isFullPromtact() == false) {	// if promtact not full going to base promtact map
					
					int state = getState(specPromtact);	// state 3 - open, state 2 - close
					
					PromtactData basePromtactData = mapOfBasePromtact.get(specificDirection);
					
					if(state == 3) {
						
						String KZD = basePromtactData.getRoadPromtactu_endGreenAddit();
						String KZM = basePromtactData.getRoadPromtactu_durationGreenBlink();
						String KG = basePromtactData.getRoadPromtactu_durationYellow();
						
						if(!specPromtact.getRoadPromtactu_endGreenAddit().equals(KZD) || !specPromtact.getRoadPromtactu_durationGreenBlink().equals(KZM) 
								|| !specPromtact.getRoadPromtactu_durationYellow().equals(KG)) {	// if spec not equals base - put in map
							
							inequalityPromtactMap.put(specificDirection, specPromtact);
							
						}
						
					}else {
						
						String KK = basePromtactData.getRoadPromtactu_endRed();
						String KKG = basePromtactData.getRoadPromtactu_durationRedYellow();
						
						if(!specPromtact.getRoadPromtactu_endRed().equals(KK) || !specPromtact.getRoadPromtactu_durationRedYellow().equals(KKG)) {	// if base not equals base - put in map
							inequalityPromtactMap.put(specificDirection, specPromtact);
						}
						
					}
					
				}else {	// full promtact automatically become specific 
					
					inequalityPromtactMap.put(specificDirection, specPromtact);
					
				}
				
			}
			
			int direction_count = inequalityPromtactMap.size();
			
			if(direction_count != 0) {
			
				byte first_byte = (byte)((from_phase_number << 4) | to_phase_number);
				stitch[index] = first_byte;
				index++;
				
				byte second_byte = convert(direction_count);
				stitch[index] = second_byte;
				index++;
				
				for(Map.Entry<String, PromtactData> promtactEntry : inequalityPromtactMap.entrySet()) {
					int dir_number = Integer.parseInt(promtactEntry.getKey());
					
					stitch[index] = convert(dir_number);
					index++;
					
					PromtactData promtactData = promtactEntry.getValue();
					
					if(!promtactData.getRoadPromtactu_endGreenAddit().equals("")) {
						int KZD = Integer.parseInt(promtactData.getRoadPromtactu_endGreenAddit());
						stitch[index] = convert(KZD);
						index++;
					}else {
						int KZD = 0;
						stitch[index] = convert(KZD);
						index++;
					}
					if(!promtactData.getRoadPromtactu_durationGreenBlink().equals("")) {
						int KZM = Integer.parseInt(promtactData.getRoadPromtactu_durationGreenBlink());
						stitch[index] = convert(KZM);
						index++;
					}else {
						int KZM = 0;
						stitch[index] = convert(KZM);
						index++;
					}
					if(!promtactData.getRoadPromtactu_durationYellow().equals("")) {
						int KG = Integer.parseInt(promtactData.getRoadPromtactu_durationYellow());
						stitch[index] = convert(KG);
						index++;
					}else {
						int KG = 0;
						stitch[index] = convert(KG);
						index++;
					}
					if(!promtactData.getRoadPromtactu_endRed().equals("")) {
						int KK = Integer.parseInt(promtactData.getRoadPromtactu_endRed());
						stitch[index] = convert(KK);
						index++;
					}else {
						int KK = 0;
						stitch[index] = convert(KK);
						index++;
					}
					if(!promtactData.getRoadPromtactu_durationRedYellow().equals("")) {
						int KKG = Integer.parseInt(promtactData.getRoadPromtactu_durationRedYellow());
						stitch[index] = convert(KKG);
						index++;
					}else {
						int KKG = 0;
						stitch[index] = convert(KKG);
						index++;
					}
				}
			
			}
			
			/*int direction_count = specificPromtactMap.size();
			
			byte second_byte = convert(direction_count);
			stitch[index] = second_byte;
			index++;*/
			
			/*for(Map.Entry<String, PromtactData> promtactEntry : specificPromtactMap.entrySet()) {
				
				int dir_number = Integer.parseInt(promtactEntry.getKey());
				
				stitch[index] = convert(dir_number);
				index++;
				
				PromtactData promtactData = promtactEntry.getValue();
				
				if(!promtactData.getRoadPromtactu_endGreenAddit().equals("")) {
					int KZD = Integer.parseInt(promtactData.getRoadPromtactu_endGreenAddit());
					stitch[index] = convert(KZD);
					index++;
				}else {
					int KZD = 0;
					stitch[index] = convert(KZD);
					index++;
				}
				if(!promtactData.getRoadPromtactu_durationGreenBlink().equals("")) {
					int KZM = Integer.parseInt(promtactData.getRoadPromtactu_durationGreenBlink());
					stitch[index] = convert(KZM);
					index++;
				}else {
					int KZM = 0;
					stitch[index] = convert(KZM);
					index++;
				}
				if(!promtactData.getRoadPromtactu_durationYellow().equals("")) {
					int KG = Integer.parseInt(promtactData.getRoadPromtactu_durationYellow());
					stitch[index] = convert(KG);
					index++;
				}else {
					int KG = 0;
					stitch[index] = convert(KG);
					index++;
				}
				if(!promtactData.getRoadPromtactu_endRed().equals("")) {
					int KK = Integer.parseInt(promtactData.getRoadPromtactu_endRed());
					stitch[index] = convert(KK);
					index++;
				}else {
					int KK = 0;
					stitch[index] = convert(KK);
					index++;
				}
				if(!promtactData.getRoadPromtactu_durationRedYellow().equals("")) {
					int KKG = Integer.parseInt(promtactData.getRoadPromtactu_durationRedYellow());
					stitch[index] = convert(KKG);
					index++;
				}else {
					int KKG = 0;
					stitch[index] = convert(KKG);
					index++;
				}
				
			}*/
			
		}
		
		int base_promtact_code = 0;
		byte first_byte = (byte)base_promtact_code;
		stitch[index] = first_byte;
		index++;
		
		int all_dir_count = roadDirectionsList.size();
		byte second_byte = (byte)all_dir_count;
		stitch[index] = second_byte;
		index++;
		
		for(Map.Entry<String, PromtactData> basePromtactEntry : mapOfBasePromtact.entrySet()) {		// base promtact 
			
			
			int dir_number = Integer.parseInt(basePromtactEntry.getKey());
			
			stitch[index] = convert(dir_number);
			index++;
			
			PromtactData promtactData = basePromtactEntry.getValue();
			
			if(!promtactData.getRoadPromtactu_endGreenAddit().equals("")) {
				int KZD = Integer.parseInt(promtactData.getRoadPromtactu_endGreenAddit());
				stitch[index] = convert(KZD);
				index++;
			}else {
				int KZD = 0;
				stitch[index] = convert(KZD);
				index++;
			}
			if(!promtactData.getRoadPromtactu_durationGreenBlink().equals("")) {
				int KZM = Integer.parseInt(promtactData.getRoadPromtactu_durationGreenBlink());
				stitch[index] = convert(KZM);
				index++;
			}else {
				int KZM = 0;
				stitch[index] = convert(KZM);
				index++;
			}
			if(!promtactData.getRoadPromtactu_durationYellow().equals("")) {
				int KG = Integer.parseInt(promtactData.getRoadPromtactu_durationYellow());
				stitch[index] = convert(KG);
				index++;
			}else {
				int KG = 0;
				stitch[index] = convert(KG);
				index++;
			}
			if(!promtactData.getRoadPromtactu_endRed().equals("")) {
				int KK = Integer.parseInt(promtactData.getRoadPromtactu_endRed());
				stitch[index] = convert(KK);
				index++;
			}else {
				int KK = 0;
				stitch[index] = convert(KK);
				index++;
			}
			if(!promtactData.getRoadPromtactu_durationRedYellow().equals("")) {
				int KKG = Integer.parseInt(promtactData.getRoadPromtactu_durationRedYellow());
				stitch[index] = convert(KKG);
				index++;
			}else {
				int KKG = 0;
				stitch[index] = convert(KKG);
				index++;
			}
			
		}
		
		System.out.println("Final index " + index);
		
		int check_sum = 0;
		for(int i = 0; i < 4096; i++) {
			check_sum += byteToInt(stitch[i]);
		}
		
		stitch[index] = (byte)(check_sum >> 8);
		index++;
		stitch[index] = (byte)(check_sum & 0xff);
		
		stitch[4] = (byte)(check_sum >> 8);
		stitch[5] = (byte)(check_sum & 0xff);
		
		stitch[16] = (byte)((index & 0xff00) >> 8);
		stitch[17] = (byte)(index & 0xff);
		
		
		
		File file = new File(path + File.separator + "binary.bin");
		try {
			FileOutputStream fos = new FileOutputStream(file);
			
			fos.write(stitch);
			fos.close();
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void createCycle(File path) {
		if(roadModel != null) {
			RoadObjectModel roadObjectModel = roadModel.getRoadObjectModel();
			RoadDirectionsModel roadDirectionsModel = roadModel.getRoadDirectionModel();
			RoadPhaseModel roadPhaseModel = roadModel.getRoadPhaseModel();
			RoadProgramsModel roadProgramsModel = roadModel.getRoadProgramsModel();
			RoadPromtactuModel roadPromtactuModel = roadModel.getRoadPromtactuModel();
	
			Map<RoadProgram, List<PhaseInProgram>> mapOfPhasesInProgram = roadProgramsModel.getMapOfPhasesInProgram();
			Map<RoadProgram, List<SwitchPhase>> mapOfSwichPhase = roadProgramsModel.getMapOfSwichPhase();
	
			Map<String, PromtactData> mapOfBasicPromtact = roadDirectionsModel.getMapOfBasePromtact();
	
			List<RoadPhase> roadPhasesList = roadPhaseModel.getRoadPhaseList();
			Map<InterphaseTransitionsHBoxCell, Map<String, PromtactData>> mapOfDirectionSpecificPromtact = roadPromtactuModel.getMapOfInterphaseSpecificPromtact();
	
			Map<String, List<OpenDirectionInCurrentPhaseHBoxCell>> mapOfOpenDirInPhase = roadPhaseModel.getMapOpenDirectionInPhase();
	
			
			// set base promtact if is absent in specific			
			ObservableList<String> observableListDirectionsNumbers = FXCollections.observableArrayList();	// directions which change state
			ObservableList<String> observableListDirectionNotChangeState = FXCollections.observableArrayList();	// directions which not change state			
			
			for(Map.Entry<InterphaseTransitionsHBoxCell, Map<String, PromtactData>> entry : mapOfDirectionSpecificPromtact.entrySet()) {	// add base promtact in absented specific
				
				String fromPhase = entry.getKey().getComboBoxFromPhase().getValue();
				String toPhase = entry.getKey().getComboBoxToPhase().getValue();
				
				List<OpenDirectionInCurrentPhaseHBoxCell> fromPhaseOpenDirList = mapOfOpenDirInPhase.get(fromPhase);	// get open directions list from phase
				List<OpenDirectionInCurrentPhaseHBoxCell> toPhaseOpenDirList = mapOfOpenDirInPhase.get(toPhase);		// get open directions list to phase
				
				List<String> fromPhaseList = new ArrayList<String>();
				List<String> toPhaseList = new ArrayList<String>();
				
				for(OpenDirectionInCurrentPhaseHBoxCell openDirectionFrom : fromPhaseOpenDirList) {	// add open direction from
					fromPhaseList.add(openDirectionFrom.getComboBox().getValue());
				}
				
				for(OpenDirectionInCurrentPhaseHBoxCell openDirectionTo : toPhaseOpenDirList) {		// add open direction to
					toPhaseList.add(openDirectionTo.getComboBox().getValue());
				}
				
				observableListDirectionsNumbers.clear();
				
				for(String directionFrom : fromPhaseList) {		// go to the list direction from	
					if(toPhaseList.contains(directionFrom)) {	
						observableListDirectionNotChangeState.add(directionFrom);	// add if not change state
					}else {
						observableListDirectionsNumbers.add(directionFrom);			// add if change state
					}
				}
				
				
				for(String directionTo : toPhaseList) {			// go to the list direction to
					if(fromPhaseList.contains(directionTo)) {
						observableListDirectionNotChangeState.add(directionTo);		// add if not change state
					}else {
						observableListDirectionsNumbers.add(directionTo);			// add if change state
					}
				}
				
				
				
				Map<String, PromtactData> specificPromtact = entry.getValue();
				
				for(String changeState : observableListDirectionsNumbers) {
					
					if(!specificPromtact.containsKey(changeState)) {
												
						PromtactData isAbsent = mapOfBasicPromtact.get(changeState);
						PromtactData promtactData = new PromtactData();
						
						if(fromPhaseList.contains(changeState)) {
							promtactData.setRoadPromtactu_endGreenAddit(isAbsent.getRoadPromtactu_endGreenAddit());
							promtactData.setRoadPromtactu_durationGreenBlink(isAbsent.getRoadPromtactu_durationGreenBlink());
							promtactData.setRoadPromtactu_durationYellow(isAbsent.getRoadPromtactu_durationYellow());
							promtactData.setRoadPromtactu_endRed("");
							promtactData.setRoadPromtactu_durationRedYellow("");
						}else {
							promtactData.setRoadPromtactu_endGreenAddit("");
							promtactData.setRoadPromtactu_durationGreenBlink("");
							promtactData.setRoadPromtactu_durationYellow("");
							promtactData.setRoadPromtactu_endRed(isAbsent.getRoadPromtactu_endRed());
							promtactData.setRoadPromtactu_durationRedYellow(isAbsent.getRoadPromtactu_durationRedYellow());
						}
						
						mapOfDirectionSpecificPromtact.get(entry.getKey()).put(changeState, promtactData);
						
					}
					
				}
				
			}
			
			
			
			//////////////////////////////////////////////////////////////////////////////////////////
			///////////////////////////// sort phase in program
			////////////////////////////////////////////////////////////////////////////////////////// //////////////////////////////////////
			//////////////////////////////////////////////////////////////////////////////////////////
			/*
			 * for(Map.Entry<RoadProgram, List<PhaseInProgram>> entry :
			 * mapOfPhasesInProgram.entrySet()){ sortedPhaseInProgramList = new
			 * ArrayList<>(); RoadProgram roadProgram = entry.getKey(); sortedList.clear();
			 * List<PhaseInProgram> phaseInProgram = mapOfPhasesInProgram.get(roadProgram);
			 * for(PhaseInProgram phase : phaseInProgram){ String number =
			 * phase.getPhaseInProgramNumber().getPhaseNumber(); int sortNumber =
			 * Integer.parseInt(number); sortedList.add(sortNumber); }
			 * Collections.sort(sortedList); for(Integer integer : sortedList){ String
			 * numberAfterSort = integer.toString(); for(PhaseInProgram phase :
			 * phaseInProgram){
			 * if(numberAfterSort.equals(phase.getPhaseInProgramNumber().getPhaseNumber())){
			 * sortedPhaseInProgramList.add(phase); } }
			 * sortMapOfPhasesInProgram.put(roadProgram, sortedPhaseInProgramList); } }
			 */
			///////////////////////////////////////////////////////////////////////////////////////////
	
			try {
	
				/*
				 * org.jdom.Element objectNameElement = new org.jdom.Element(" ");
				 * objectNameElement.setText("!-- " + roadObjectModel.getRoadObjectName() +
				 * " --");
				 */
	
				org.jdom.Element root = new org.jdom.Element("root");
				org.jdom.Document doc = new org.jdom.Document();
	
				String latin = transliterate(roadObjectModel.getRoadObjectName());
				
				doc.addContent(new Comment(latin));
				doc.addContent(new Comment(roadObjectModel.getRoadObjectDateOfCreation()));
	
				org.jdom.Element number_of_directions = new org.jdom.Element("number_of_directions");
				String totalDirections = Integer.toString(roadDirectionsModel.getRoadDirectionList().size());
				number_of_directions.setText(totalDirections);
	
				org.jdom.Element on_correction_when_program_startingElement = new org.jdom.Element("on_correction_when_program_starting");
				on_correction_when_program_startingElement.setText("false");
	
				org.jdom.Element on_correction_between_programsElement = new org.jdom.Element("on_correction_between_programs");
				on_correction_between_programsElement.setText("false");
				////////////////////////////////////////////////////////////////////////////////////
				//////////////////////// tvp_groups element ////////////////////////////
				////////////////////////////////////////////////////////////////////////////////////
				
				org.jdom.Element tvp_groupsElement = new org.jdom.Element("tvp_groups");
				
				org.jdom.Element orElement = new org.jdom.Element("or");
				
				org.jdom.Element tvp_groupElement = new org.jdom.Element("tvp_group");
				tvp_groupElement.setAttribute("tvp_group_id", "1");
				
				org.jdom.Element tvpElement5 = new org.jdom.Element("tvp");
				tvpElement5.setText("5");
				
				org.jdom.Element tvpElement32 = new org.jdom.Element("tvp");
				tvpElement32.setText("32");
				
				tvp_groupElement.addContent(tvpElement5);
				tvp_groupElement.addContent(tvpElement32);
				
				orElement.addContent(tvp_groupElement);
				
				tvp_groupsElement.addContent(orElement);
				
				////////////////////////////////////////////////////////////////////////////////////
	
				////////////////////////////////////////////////////////////////////////////////////
				///////////////////// replace_phase_tvp_groups element /////////////////////////
				////////////////////////////////////////////////////////////////////////////////////
				/*org.jdom.Element replace_phase_tvp_groupsElement = new org.jdom.Element("replace_phase_tvp_groups");
				org.jdom.Element rep_phase_tvp_group = new org.jdom.Element("tvp_group");
				rep_phase_tvp_group.setAttribute("id", "");
				org.jdom.Element rep_phase_switch_phase = new org.jdom.Element("switch_phase");
				rep_phase_switch_phase.setAttribute("from", "");
				rep_phase_switch_phase.setAttribute("to", "");*/
				////////////////////////////////////////////////////////////////////////////////////
	
				////////////////////////////////////////////////////////////////////////////////////
				//////////////////////// recommended_speed element
				//////////////////////////////////////////////////////////////////////////////////// ///////////////////////////////
				////////////////////////////////////////////////////////////////////////////////////
				org.jdom.Element recommended_speedElement = new org.jdom.Element("recommended_speed");
				org.jdom.Element speed_idElement = new org.jdom.Element("speed_id");
				speed_idElement.setAttribute("id", "");
				speed_idElement.setAttribute("led_id", "");
				////////////////////////////////////////////////////////////////////////////////////
	
				////////////////////////////////////////////////////////////////////////////////////
				/////////////////////////// calendar element ///////////////////////////////////
				////////////////////////////////////////////////////////////////////////////////////
				org.jdom.Element calendarElement = new org.jdom.Element("calendar");
	
				// by_weekday element
				org.jdom.Element by_weekdayElement = new org.jdom.Element("by_weekday");
				// day by weekday element
				org.jdom.Element dayByWeekDayElement = null;
				org.jdom.Element programByWeekDayElement = null;
	
				Map<ScheduleCalendarWeekDayHBoxCell, List<ScheduleProgram>> mapOfWeekDayCalendar = roadProgramsModel.getMapOfWeekCalendar();
				String day = null;
				for (Map.Entry<ScheduleCalendarWeekDayHBoxCell, List<ScheduleProgram>> entry : mapOfWeekDayCalendar.entrySet()) {
					ScheduleCalendarWeekDayHBoxCell scheduleCalendarWeekDayHBoxCell = entry.getKey();
	
					if (scheduleCalendarWeekDayHBoxCell.getWeekDay().getText().equals("Понедельник")) {
						day = "Mon";
						dayByWeekDayElement = new org.jdom.Element("day");
						dayByWeekDayElement.setAttribute("id", day);
						List<ScheduleProgram> scheduleProgramsList = entry.getValue();
						for (ScheduleProgram scheduleProgram : scheduleProgramsList) {
							String progNumber = scheduleProgram.getNumberOfScheduleProgram().getScheduleNumber();
							String startTime = scheduleProgram.getTimeONOfScheduleProgram();
							String offset = scheduleProgram.getDisplacementTimeOfScheduleProgram();
	
							programByWeekDayElement = new org.jdom.Element("program");
							programByWeekDayElement.setAttribute("id", progNumber);
							programByWeekDayElement.setAttribute("startTime", startTime);
							programByWeekDayElement.setAttribute("offset", offset);
							// programByWeekDayElement.setAttribute("tvp_group", "");
	
							dayByWeekDayElement.addContent(programByWeekDayElement);
						}
	
					}
					if (scheduleCalendarWeekDayHBoxCell.getWeekDay().getText().equals("Вторник")) {
						day = "Tue";
						dayByWeekDayElement = new org.jdom.Element("day");
						dayByWeekDayElement.setAttribute("id", day);
						List<ScheduleProgram> scheduleProgramsList = entry.getValue();
						for (ScheduleProgram scheduleProgram : scheduleProgramsList) {
							String progNumber = scheduleProgram.getNumberOfScheduleProgram().getScheduleNumber();
							String startTime = scheduleProgram.getTimeONOfScheduleProgram();
							String offset = scheduleProgram.getDisplacementTimeOfScheduleProgram();
	
							programByWeekDayElement = new org.jdom.Element("program");
							programByWeekDayElement.setAttribute("id", progNumber);
							programByWeekDayElement.setAttribute("startTime", startTime);
							programByWeekDayElement.setAttribute("offset", offset);
							// programByWeekDayElement.setAttribute("tvp_group", "");
	
							dayByWeekDayElement.addContent(programByWeekDayElement);
						}
					}
					if (scheduleCalendarWeekDayHBoxCell.getWeekDay().getText().equals("Среда")) {
						day = "Wed";
						dayByWeekDayElement = new org.jdom.Element("day");
						dayByWeekDayElement.setAttribute("id", day);
						List<ScheduleProgram> scheduleProgramsList = entry.getValue();
						for (ScheduleProgram scheduleProgram : scheduleProgramsList) {
							String progNumber = scheduleProgram.getNumberOfScheduleProgram().getScheduleNumber();
							String startTime = scheduleProgram.getTimeONOfScheduleProgram();
							String offset = scheduleProgram.getDisplacementTimeOfScheduleProgram();
	
							programByWeekDayElement = new org.jdom.Element("program");
							programByWeekDayElement.setAttribute("id", progNumber);
							programByWeekDayElement.setAttribute("startTime", startTime);
							programByWeekDayElement.setAttribute("offset", offset);
							// programByWeekDayElement.setAttribute("tvp_group", "");
	
							dayByWeekDayElement.addContent(programByWeekDayElement);
						}
					}
					if (scheduleCalendarWeekDayHBoxCell.getWeekDay().getText().equals("Четверг")) {
						day = "Thu";
						dayByWeekDayElement = new org.jdom.Element("day");
						dayByWeekDayElement.setAttribute("id", day);
						List<ScheduleProgram> scheduleProgramsList = entry.getValue();
						for (ScheduleProgram scheduleProgram : scheduleProgramsList) {
							String progNumber = scheduleProgram.getNumberOfScheduleProgram().getScheduleNumber();
							String startTime = scheduleProgram.getTimeONOfScheduleProgram();
							String offset = scheduleProgram.getDisplacementTimeOfScheduleProgram();
	
							programByWeekDayElement = new org.jdom.Element("program");
							programByWeekDayElement.setAttribute("id", progNumber);
							programByWeekDayElement.setAttribute("startTime", startTime);
							programByWeekDayElement.setAttribute("offset", offset);
							// programByWeekDayElement.setAttribute("tvp_group", "");
	
							dayByWeekDayElement.addContent(programByWeekDayElement);
						}
					}
					if (scheduleCalendarWeekDayHBoxCell.getWeekDay().getText().equals("Пятница")) {
						day = "Fri";
						dayByWeekDayElement = new org.jdom.Element("day");
						dayByWeekDayElement.setAttribute("id", day);
						List<ScheduleProgram> scheduleProgramsList = entry.getValue();
						for (ScheduleProgram scheduleProgram : scheduleProgramsList) {
							String progNumber = scheduleProgram.getNumberOfScheduleProgram().getScheduleNumber();
							String startTime = scheduleProgram.getTimeONOfScheduleProgram();
							String offset = scheduleProgram.getDisplacementTimeOfScheduleProgram();
	
							programByWeekDayElement = new org.jdom.Element("program");
							programByWeekDayElement.setAttribute("id", progNumber);
							programByWeekDayElement.setAttribute("startTime", startTime);
							programByWeekDayElement.setAttribute("offset", offset);
							// programByWeekDayElement.setAttribute("tvp_group", "");
	
							dayByWeekDayElement.addContent(programByWeekDayElement);
						}
					}
					if (scheduleCalendarWeekDayHBoxCell.getWeekDay().getText().equals("Суббота")) {
						day = "Sat";
						dayByWeekDayElement = new org.jdom.Element("day");
						dayByWeekDayElement.setAttribute("id", day);
						List<ScheduleProgram> scheduleProgramsList = entry.getValue();
						for (ScheduleProgram scheduleProgram : scheduleProgramsList) {
							String progNumber = scheduleProgram.getNumberOfScheduleProgram().getScheduleNumber();
							String startTime = scheduleProgram.getTimeONOfScheduleProgram();
							String offset = scheduleProgram.getDisplacementTimeOfScheduleProgram();
	
							programByWeekDayElement = new org.jdom.Element("program");
							programByWeekDayElement.setAttribute("id", progNumber);
							programByWeekDayElement.setAttribute("startTime", startTime);
							programByWeekDayElement.setAttribute("offset", offset);
							// programByWeekDayElement.setAttribute("tvp_group", "");
	
							dayByWeekDayElement.addContent(programByWeekDayElement);
						}
					}
					if (scheduleCalendarWeekDayHBoxCell.getWeekDay().getText().equals("Воскресенье")) {
						day = "Sun";
						dayByWeekDayElement = new org.jdom.Element("day");
						dayByWeekDayElement.setAttribute("id", day);
						List<ScheduleProgram> scheduleProgramsList = entry.getValue();
						for (ScheduleProgram scheduleProgram : scheduleProgramsList) {
							String progNumber = scheduleProgram.getNumberOfScheduleProgram().getScheduleNumber();
							String startTime = scheduleProgram.getTimeONOfScheduleProgram();
							String offset = scheduleProgram.getDisplacementTimeOfScheduleProgram();
	
							programByWeekDayElement = new org.jdom.Element("program");
							programByWeekDayElement.setAttribute("id", progNumber);
							programByWeekDayElement.setAttribute("startTime", startTime);
							programByWeekDayElement.setAttribute("offset", offset);
							// programByWeekDayElement.setAttribute("tvp_group", "");
	
							dayByWeekDayElement.addContent(programByWeekDayElement);
						}
					}
					by_weekdayElement.addContent(dayByWeekDayElement);
	
				}
	
				// by_date element
				org.jdom.Element by_dateElement = new org.jdom.Element("by_date");
				org.jdom.Element programByDateElement = null;
				// day by date element
				org.jdom.Element dayElement = null;
				Map<ScheduleCalendarDateHBoxCell, List<ScheduleProgram>> mapOfDateCalendar = roadProgramsModel.getMapOfDateCalendar();
				for(Map.Entry<ScheduleCalendarDateHBoxCell, List<ScheduleProgram>> entry : mapOfDateCalendar.entrySet()) {
					dayElement = new org.jdom.Element("day");
					String date = entry.getKey().getDatePicker().getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
					dayElement.setAttribute("day", date);
					
					
					List<ScheduleProgram> scheduleProgramsList = entry.getValue();
					for(ScheduleProgram scheduleProgram : scheduleProgramsList) {
						programByDateElement = new org.jdom.Element("program");
						programByDateElement.setAttribute("id", scheduleProgram.getNumberOfScheduleProgram().getScheduleNumber());
						programByDateElement.setAttribute("startTime", scheduleProgram.getTimeONOfScheduleProgram());
						programByDateElement.setAttribute("offset", scheduleProgram.getDisplacementTimeOfScheduleProgram());
						dayElement.addContent(programByDateElement);
					}
					by_dateElement.addContent(dayElement);
				}
				
				////////////////////////////////////////////////////////////////////////////////////
	
				////////////////////////////////////////////////////////////////////////////////////
				/////////////////////////// programs element ///////////////////////////////////
				////////////////////////////////////////////////////////////////////////////////////
				org.jdom.Element programsElement = new org.jdom.Element("programs");
				org.jdom.Element programElement = null;
				org.jdom.Element phaseInProgramElement = null;
				org.jdom.Element swichPhaseElement = null;
				
				List<RoadProgram> roadProgramsList = roadProgramsModel.getRoadProgramList();
				for(RoadProgram existRoadProgram : roadProgramsList) {
					String roadProgramId = existRoadProgram.getRoadProgram_number();
					String roadProgramType = existRoadProgram.getRoadProgram_programMode().getMode();
					
					String sub_type = null;
					
					roadProgramType = "daily_program";
					
					if(existRoadProgram.getRoadProgram_programMode().getMode().equals("Желтое мигание")) {
						roadProgramType = "GM";
					}
					if (existRoadProgram.getRoadProgram_programMode().getMode().equals("Отключение светофора")) {
						roadProgramType = "OC";
					}
					
					if(existRoadProgram.getRoadProgram_programMode().getMode().equals("Циклическая с ТВП - 1") || 
							existRoadProgram.getRoadProgram_programMode().getMode().equals("Циклическая с ТВП - 2") || 
							existRoadProgram.getRoadProgram_programMode().getMode().equals("Циклическая с ТВП - 1 и ТВП - 2") ||
							existRoadProgram.getRoadProgram_programMode().getMode().equals("Пешеходная с ТВП - 1") || 
							existRoadProgram.getRoadProgram_programMode().getMode().equals("Пешеходная с ТВП - 2") || 
							existRoadProgram.getRoadProgram_programMode().getMode().equals("Пешеходная с ТВП - 1 и ТВП - 2")) {
						
						sub_type = "call_phase";
						
					}
					
					if(existRoadProgram.getRoadProgram_programMode().getMode().equals("Замена фаз")) {
						sub_type = "switch_phase";
					}
					
					programElement = new org.jdom.Element("program");
					programElement.setAttribute("id", roadProgramId);
					programElement.setAttribute("type", roadProgramType);
					
					if(sub_type != null) {
						programElement.setAttribute("sub_type", sub_type);
					}					
	
					programsElement.addContent(programElement);
					
					if(!existRoadProgram.getRoadProgram_programMode().getMode().equals("Замена фаз")) {
						List<PhaseInProgram> phaseInProgramsList = mapOfPhasesInProgram.get(existRoadProgram);
						
						if(phaseInProgramsList.size() > 0) {
							for (PhaseInProgram phaseInProgram : phaseInProgramsList) {
								String phaseInProgramId = phaseInProgram.getPhaseInProgramNumber().getPhaseNumber();
								String durationPhase = phaseInProgram.getDurationPhaseInProgram();
			
								phaseInProgramElement = new org.jdom.Element("phase");
								phaseInProgramElement.setAttribute("id", phaseInProgramId);
								phaseInProgramElement.setText(durationPhase);
			
								programElement.addContent(phaseInProgramElement);
								
							}
						}else {
							phaseInProgramElement = new org.jdom.Element("phase");
							phaseInProgramElement.setAttribute("id", "100");
							phaseInProgramElement.setText("20");
							
							programElement.addContent(phaseInProgramElement);
							
						}
						
					}else {
						List<SwitchPhase> swichPhasesList = mapOfSwichPhase.get(existRoadProgram);
						if(!swichPhasesList.isEmpty()) {
							for(SwitchPhase existSwichPhase : swichPhasesList) {
								String phaseId = existSwichPhase.getPhase().getPhaseNumber();
								String swichPhase1 = existSwichPhase.getToPhase1().getPhaseNumber();
								String durationPhase = existSwichPhase.getDurationPhase();
								
								swichPhaseElement = new org.jdom.Element("phase");
								swichPhaseElement.setAttribute("id", phaseId);
								swichPhaseElement.setText(durationPhase);
								
								programElement.addContent(swichPhaseElement);
								
								swichPhaseElement = new org.jdom.Element("phase");
								swichPhaseElement.setAttribute("id", swichPhase1);
								swichPhaseElement.setText(durationPhase);
								
								programElement.addContent(swichPhaseElement);
							}
						}
						
					}
					
				}
				
				
				/*for (Map.Entry<RoadProgram, List<PhaseInProgram>> entry : mapOfPhasesInProgram.entrySet()) {
					String roadProgramId = entry.getKey().getRoadProgram_number();
					String roadProgramType = entry.getKey().getRoadProgram_programMode().getMode();
					
					String sub_type = null;
					
					roadProgramType = "daily_program";
					
					if(entry.getKey().getRoadProgram_programMode().getMode().equals("Желтое мигание")) {
						roadProgramType = "GM";
					}
					if (entry.getKey().getRoadProgram_programMode().getMode().equals("Отключение светофора")) {
						roadProgramType = "OC";
					}
					
					if(entry.getKey().getRoadProgram_programMode().getMode().equals("Циклическая с ТВП - 1") || 
							entry.getKey().getRoadProgram_programMode().getMode().equals("Циклическая с ТВП - 2") || 
							entry.getKey().getRoadProgram_programMode().getMode().equals("Циклическая с ТВП - 1 и ТВП - 2") ||
							entry.getKey().getRoadProgram_programMode().getMode().equals("Пешеходная с ТВП - 1") || 
							entry.getKey().getRoadProgram_programMode().getMode().equals("Пешеходная с ТВП - 2") || 
							entry.getKey().getRoadProgram_programMode().getMode().equals("Пешеходная с ТВП - 1 и ТВП - 2")) {
						
						sub_type = "call_phase";
						
					}
					
					////////////////////////////////////////////////
					if (roadProgramType.equals("Циклическая")) {
						roadProgramType = "daily_program";
					} else if (roadProgramType.equals("Желтое мигание")) {
						roadProgramType = "GM";
					} else if (roadProgramType.equals("Отключение светофора")) {
						roadProgramType = "OC";
					} else {
						roadProgramType = "tvp_daily_program";
					}
					///////////////////////////////////////////////////
				
					programElement = new org.jdom.Element("program");
					programElement.setAttribute("id", roadProgramId);
					programElement.setAttribute("type", roadProgramType);
					
					if(sub_type != null) {
						programElement.setAttribute("sub_type", sub_type);
					}
					
	
					programsElement.addContent(programElement);
					
					if(entry.getValue().size() > 0) {
					
						for (PhaseInProgram phaseInProgram : entry.getValue()) {
							String phaseInProgramId = phaseInProgram.getPhaseInProgramNumber().getPhaseNumber();
							String durationPhase = phaseInProgram.getDurationPhaseInProgram();
		
							phaseInProgramElement = new org.jdom.Element("phase");
							phaseInProgramElement.setAttribute("id", phaseInProgramId);
							phaseInProgramElement.setText(durationPhase);
		
							programElement.addContent(phaseInProgramElement);
						}
					
					}else {
						phaseInProgramElement = new org.jdom.Element("phase");
						phaseInProgramElement.setAttribute("id", "100");
						phaseInProgramElement.setText("20");
						
						programElement.addContent(phaseInProgramElement);
					}
				}*/
				////////////////////////////////////////////////////////////////////////////////////
	
				////////////////////////////////////////////////////////////////////////////////////
				///////////////////////// base promtact element
				//////////////////////////////////////////////////////////////////////////////////// /////////////////////////////////
				////////////////////////////////////////////////////////////////////////////////////
				////////////////////////////////////////////////////////////////////////////////////
				org.jdom.Element base_promtactsElement = new org.jdom.Element("base_promtacts");
				org.jdom.Element basePromtactDirectionElement = null;
	
				List<RoadDirection> allDirections = roadDirectionsModel.getRoadDirectionList();
				for (RoadDirection roadDirection : allDirections) {
					String directionNumber = roadDirection.getRoadDirections_number();
					String directionType = roadDirection.getRoadDirections_typeOfDirection().getTypDirection();
	
					for (Map.Entry<String, PromtactData> entry : mapOfBasicPromtact.entrySet()) {
						String directionId = entry.getKey();
	
						if (directionId.equals(directionNumber)) {
							// get type of direction
							if (!directionType.equals("Пешеходное")) {
								directionType = "traffic";
							} else {
								directionType = "pedestrian";
							}
	
							PromtactData promtactData = entry.getValue();
							String kzdValue = promtactData.getRoadPromtactu_endGreenAddit();
							String kzmValue = promtactData.getRoadPromtactu_durationGreenBlink();
							String kgValue = promtactData.getRoadPromtactu_durationYellow();
							String kkValue = promtactData.getRoadPromtactu_endRed();
							String kkgValue = promtactData.getRoadPromtactu_durationRedYellow();
	
							// direction element
							basePromtactDirectionElement = new org.jdom.Element("direction");
							basePromtactDirectionElement.setAttribute("id", directionId);
							basePromtactDirectionElement.setAttribute("type", directionType);
							basePromtactDirectionElement.setAttribute("KZD", kzdValue);
							basePromtactDirectionElement.setAttribute("KZM", kzmValue);
							basePromtactDirectionElement.setAttribute("KG", kgValue);
							basePromtactDirectionElement.setAttribute("KK", kkValue);
							basePromtactDirectionElement.setAttribute("KKG", kkgValue);
	
							base_promtactsElement.addContent(basePromtactDirectionElement);
	
						}
					}
				}
				////////////////////////////////////////////////////////////////////////////////////
	
				////////////////////////////////////////////////////////////////////////////////////
				//////////////////////////// phases element
				//////////////////////////////////////////////////////////////////////////////////// /////////////////////////////////////
				////////////////////////////////////////////////////////////////////////////////////
				org.jdom.Element phasesElement = new org.jdom.Element("phases");
				org.jdom.Element phaseElement = null;
				org.jdom.Element tMinPhaseElement = null;
				org.jdom.Element tMaxPhaseElement = null;
				org.jdom.Element specificElement = null;
				org.jdom.Element to_phaseElement = null;
				org.jdom.Element directionsSpecElement = null;
				org.jdom.Element directionSpecElement = null;
				org.jdom.Element promtactElement = null;
				org.jdom.Element defaultElement = null;
				org.jdom.Element directionsDefaultElement = null;
				org.jdom.Element directionDefaultElement = null;
				org.jdom.Element defaultPromtactElement = null;
				org.jdom.Element TVPElement = null;
	
				// get all phases
				for (RoadPhase roadPhase : roadPhasesList) {
					String phaseNumber = roadPhase.getRoadPhase_number();
					String tMinValue = roadPhase.getRoadPhase_Tmin();
					String tMaxValue = "0";
					List<OpenDirectionInCurrentPhaseHBoxCell> openDirInPhaseList = null;
	
					phaseElement = new org.jdom.Element("phase");
					phaseElement.setAttribute("id", phaseNumber);
	
					tMinPhaseElement = new org.jdom.Element("Tmin");
					tMinPhaseElement.setText(tMinValue);
	
					tMaxPhaseElement = new org.jdom.Element("Tmax");
					tMaxPhaseElement.setText(tMaxValue);
	
					specificElement = new org.jdom.Element("specific");
					
					TVPElement = new org.jdom.Element("tvp");
					TVPElement.setText("0");
					
					if(roadPhase.getRoadPhase_phaseTVP().getTvp().equals("ТВП - 1")) {
						TVPElement.setText("1");
					}
					if(roadPhase.getRoadPhase_phaseTVP().getTvp().equals("ТВП - 2")) {
						TVPElement.setText("2");
					}
					if(roadPhase.getRoadPhase_phaseTVP().getTvp().equals("ТВП - 1 и ТВП - 2") || roadPhase.getRoadPhase_phaseTVP().getTvp().equals("ТВП - 1 или ТВП - 2")) {
						TVPElement.setText("102");
					}
					
	
					phasesElement.addContent(phaseElement);
					phaseElement.addContent(TVPElement);
					phaseElement.addContent(tMinPhaseElement);
					phaseElement.addContent(tMaxPhaseElement);
					phaseElement.addContent(specificElement);
	
					// get state of direction
					for (Map.Entry<String, List<OpenDirectionInCurrentPhaseHBoxCell>> entryOpenDir : mapOfOpenDirInPhase.entrySet()) {
						String phase = entryOpenDir.getKey();
						if (phase.equals(phaseNumber)) {
							openDirInPhaseList = entryOpenDir.getValue();
						}
					}
	
					for (Map.Entry<InterphaseTransitionsHBoxCell, Map<String, PromtactData>> entry : mapOfDirectionSpecificPromtact.entrySet()) {
						InterphaseTransitionsHBoxCell interphaseTransitionsHBoxCell = entry.getKey();
						Map<String, PromtactData> mapPromtactData = entry.getValue();
						Map<String, PromtactData> mapPromtactDataCopy = new LinkedHashMap<>(mapPromtactData);
						String phaseFrom = interphaseTransitionsHBoxCell.getComboBoxFromPhase().getValue();
						String toPhase = interphaseTransitionsHBoxCell.getComboBoxToPhase().getValue();
	
						if (phaseNumber.equals(phaseFrom)) {
							List<OpenDirectionInCurrentPhaseHBoxCell> openDirectionListCopy = new ArrayList<>(openDirInPhaseList);
							List<Integer> allPromtactValueList = new ArrayList<>();
							to_phaseElement = new org.jdom.Element("to_phase");
							to_phaseElement.setAttribute("id", toPhase);
	
							specificElement.addContent(to_phaseElement);
	
							directionsSpecElement = new org.jdom.Element("directions");
							to_phaseElement.addContent(directionsSpecElement);
	
							List<RoadDirection> allDirectionList = roadDirectionsModel.getRoadDirectionList();
							for (RoadDirection roadDirection : allDirectionList) {
								String directionNumber = roadDirection.getRoadDirections_number();
								String directionType = roadDirection.getRoadDirections_typeOfDirection().getTypDirection();
								String state = "close";
	
								for (OpenDirectionInCurrentPhaseHBoxCell openDirection : openDirectionListCopy) {
									if (openDirection.getComboBox().getValue().equals(directionNumber)) {
										state = "open";
										openDirectionListCopy.remove(openDirection);
										break;
									}
								}
	
								// get type of direction
								if (!directionType.equals("Пешеходное")) {
									directionType = "traffic";
								} else {
									directionType = "pedestrian";
								}
	
								if (!mapPromtactDataCopy.isEmpty()) {
									if (mapPromtactDataCopy.containsKey(directionNumber)) {
										PromtactData promtactData = mapPromtactDataCopy.get(directionNumber);
										String greenBlink = promtactData.getRoadPromtactu_durationGreenBlink();
										String greenAddit = promtactData.getRoadPromtactu_endGreenAddit();
										String yellow = promtactData.getRoadPromtactu_durationYellow();
										String red = promtactData.getRoadPromtactu_endRed();
										String redYellow = promtactData.getRoadPromtactu_durationRedYellow();
	
										if (!greenBlink.equals("")) {
											allPromtactValueList.add(Integer.parseInt(greenBlink));
										} else {
											greenBlink = "0";
										}
										if (!greenAddit.equals("")) {
											allPromtactValueList.add(Integer.parseInt(greenAddit));
										} else {
											greenAddit = "0";
										}
										if (!yellow.equals("")) {
											allPromtactValueList.add(Integer.parseInt(yellow));
										} else {
											yellow = "0";
										}
										if (!red.equals("")) {
											allPromtactValueList.add(Integer.parseInt(red));
										} else {
											red = "0";
										}
										if (!redYellow.equals("")) {
											allPromtactValueList.add(Integer.parseInt(redYellow));
										} else {
											redYellow = "0";
										}
	
										directionSpecElement = new org.jdom.Element("direction");
										directionSpecElement.setAttribute("id", directionNumber);
										directionSpecElement.setAttribute("type", directionType);
										directionSpecElement.setAttribute("state", state);
										directionSpecElement.setAttribute("KZD", greenAddit);
										directionSpecElement.setAttribute("KZM", greenBlink);
										directionSpecElement.setAttribute("KG", yellow);
										directionSpecElement.setAttribute("KK", red);
										directionSpecElement.setAttribute("KKG", redYellow);
	
										directionsSpecElement.addContent(directionSpecElement);
									} else {
										directionSpecElement = new org.jdom.Element("direction");
										directionSpecElement.setAttribute("id", directionNumber);
										directionSpecElement.setAttribute("type", directionType);
										directionSpecElement.setAttribute("state", state);
										directionSpecElement.setAttribute("KZD", new Integer(0).toString());
										directionSpecElement.setAttribute("KZM", new Integer(0).toString());
										directionSpecElement.setAttribute("KG", new Integer(0).toString());
										directionSpecElement.setAttribute("KK", new Integer(0).toString());
										directionSpecElement.setAttribute("KKG", new Integer(0).toString());
	
										directionsSpecElement.addContent(directionSpecElement);
									}
								} else {
									directionSpecElement = new org.jdom.Element("direction");
									directionSpecElement.setAttribute("id", directionNumber);
									directionSpecElement.setAttribute("type", directionType);
									directionSpecElement.setAttribute("state", state);
									directionSpecElement.setAttribute("KZD", new Integer(0).toString());
									directionSpecElement.setAttribute("KZM", new Integer(0).toString());
									directionSpecElement.setAttribute("KG", new Integer(0).toString());
									directionSpecElement.setAttribute("KK", new Integer(0).toString());
									directionSpecElement.setAttribute("KKG", new Integer(0).toString());
	
									directionsSpecElement.addContent(directionSpecElement);
								}
							}
							// promtact element
							promtactElement = new org.jdom.Element("promtact");
							String promtact = Integer.toString(Collections.max(allPromtactValueList));
							promtactElement.setText(promtact);
							to_phaseElement.addContent(promtactElement);
						}
					}
	
					defaultElement = new org.jdom.Element("default");
					phaseElement.addContent(defaultElement);
	
					directionsDefaultElement = new org.jdom.Element("directions");
					defaultElement.addContent(directionsDefaultElement);
	
					for (Map.Entry<String, List<OpenDirectionInCurrentPhaseHBoxCell>> entryOpenDir : mapOfOpenDirInPhase.entrySet()) {
						String phase = entryOpenDir.getKey();
						if (phase.equals(phaseNumber)) {
							openDirInPhaseList = entryOpenDir.getValue();
						}
					}
					List<OpenDirectionInCurrentPhaseHBoxCell> openDirectionList = new ArrayList<>(openDirInPhaseList);
					List<RoadDirection> allDirectionList = roadDirectionsModel.getRoadDirectionList();
					for (RoadDirection roadDirection : allDirectionList) {
						String directionId = roadDirection.getRoadDirections_number();
						String directionType = roadDirection.getRoadDirections_typeOfDirection().getTypDirection();
						String state = "";
	
						// get type of direction
						if (!directionType.equals("Пешеходное")) {
							directionType = "traffic";
						} else {
							directionType = "pedestrian";
						}
	
						if (!openDirectionList.isEmpty()) {
							for (OpenDirectionInCurrentPhaseHBoxCell openDirection : openDirectionList) {
								if (openDirection.getComboBox().getValue().equals(directionId)) {
									directionDefaultElement = new org.jdom.Element("direction");
									state = "open";
	
									directionDefaultElement.setAttribute("id", directionId);
									directionDefaultElement.setAttribute("type", directionType);
									directionDefaultElement.setAttribute("state", state);
									directionDefaultElement.setAttribute("KZD", new Integer(0).toString());
									directionDefaultElement.setAttribute("KZM", new Integer(0).toString());
									directionDefaultElement.setAttribute("KG", new Integer(0).toString());
									directionDefaultElement.setAttribute("KK", new Integer(0).toString());
									directionDefaultElement.setAttribute("KKG", new Integer(0).toString());
	
									directionsDefaultElement.addContent(directionDefaultElement);
	
									openDirectionList.remove(openDirection);
									break;
								} else {
									directionDefaultElement = new org.jdom.Element("direction");
									state = "close";
	
									directionDefaultElement.setAttribute("id", directionId);
									directionDefaultElement.setAttribute("type", directionType);
									directionDefaultElement.setAttribute("state", state);
									directionDefaultElement.setAttribute("KZD", new Integer(0).toString());
									directionDefaultElement.setAttribute("KZM", new Integer(0).toString());
									directionDefaultElement.setAttribute("KG", new Integer(0).toString());
									directionDefaultElement.setAttribute("KK", new Integer(0).toString());
									directionDefaultElement.setAttribute("KKG", new Integer(0).toString());
	
									directionsDefaultElement.addContent(directionDefaultElement);
									break;
								}
	
							}
						} else {
							directionDefaultElement = new org.jdom.Element("direction");
							state = "close";
	
							directionDefaultElement.setAttribute("id", directionId);
							directionDefaultElement.setAttribute("type", directionType);
							directionDefaultElement.setAttribute("state", state);
							directionDefaultElement.setAttribute("KZD", new Integer(0).toString());
							directionDefaultElement.setAttribute("KZM", new Integer(0).toString());
							directionDefaultElement.setAttribute("KG", new Integer(0).toString());
							directionDefaultElement.setAttribute("KK", new Integer(0).toString());
							directionDefaultElement.setAttribute("KKG", new Integer(0).toString());
	
							directionsDefaultElement.addContent(directionDefaultElement);
						}
	
					}
	
					defaultPromtactElement = new org.jdom.Element("promtact");
					defaultPromtactElement.setText(new Integer(0).toString());
	
					defaultElement.addContent(defaultPromtactElement);
	
				}
	
				// hundredth phase
				org.jdom.Element hundredthPhase = new org.jdom.Element("phase");
				hundredthPhase.setAttribute("id", "100");
				phasesElement.addContent(hundredthPhase);
	
				org.jdom.Element tminElement = new org.jdom.Element("Tmin");
				tminElement.setText("12");
				hundredthPhase.addContent(tminElement);
	
				org.jdom.Element tmaxElement = new org.jdom.Element("Tmax");
				tmaxElement.setText("100000");
				hundredthPhase.addContent(tmaxElement);
	
				org.jdom.Element defElement = new org.jdom.Element("default");
				hundredthPhase.addContent(defElement);
	
				org.jdom.Element directionsDefElement = new org.jdom.Element("directions");
				defElement.addContent(directionsDefElement);
	
				List<RoadDirection> roadDirectionsList = roadDirectionsModel.getRoadDirectionList();
				org.jdom.Element dirDefElement = null;
				for (RoadDirection roadDirection : roadDirectionsList) {
					dirDefElement = new org.jdom.Element("direction");
					String dirId = roadDirection.getRoadDirections_number();
					String dirType = roadDirection.getRoadDirections_typeOfDirection().getTypDirection();
	
					if (!dirType.equals("Пешеходное")) {
						dirType = "traffic";
					} else {
						dirType = "pedestrian";
					}
	
					dirDefElement.setAttribute("id", dirId);
					dirDefElement.setAttribute("type", dirType);
					dirDefElement.setAttribute("state", "close");
					dirDefElement.setAttribute("KZD", "0");
					dirDefElement.setAttribute("KZM", "0");
					dirDefElement.setAttribute("KG", "0");
					dirDefElement.setAttribute("KK", "0");
					dirDefElement.setAttribute("KKG", "0");
	
					directionsDefElement.addContent(dirDefElement);
	
				}
	
				org.jdom.Element promtactHundrethPhaseElement = new org.jdom.Element("promtact");
				promtactHundrethPhaseElement.setText("12");
				defElement.addContent(promtactHundrethPhaseElement);
	
				////////////////////////////////////////////////////////////////////////////////////
	
				root.addContent(number_of_directions);
				root.addContent(on_correction_when_program_startingElement);
				root.addContent(on_correction_between_programsElement);
				root.addContent(tvp_groupsElement);
				//root.addContent(replace_phase_tvp_groupsElement);
				root.addContent(recommended_speedElement);
				root.addContent(calendarElement);
				root.addContent(programsElement);
				root.addContent(base_promtactsElement);
				root.addContent(phasesElement);
	
				/*tvp_groupsElement.addContent(orElement);
				tvp_groupsElement.addContent(tvp_groupElement);
				tvp_groupElement.addContent(tvpElement);*/
	
				//replace_phase_tvp_groupsElement.addContent(rep_phase_tvp_group);
	
				//rep_phase_tvp_group.addContent(rep_phase_switch_phase);
				recommended_speedElement.addContent(speed_idElement);
				
				calendarElement.addContent(by_weekdayElement);
				calendarElement.addContent(by_dateElement);
	
				doc.setRootElement(root);
	
				XMLOutputter outter = new XMLOutputter();
				outter.setFormat(Format.getPrettyFormat());
				outter.output(doc, new FileWriter(new File(path + File.separator + "cycle.xml")));
	
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void createConfig(File path) {
		if(roadModel != null) {
			RoadObjectModel roadObjectModel = roadModel.getRoadObjectModel();
			RoadDetectorModel roadDetectorModel = roadModel.getRoadDetectorModel();
	
			try {
				org.jdom.Element root = new org.jdom.Element("root");
				org.jdom.Document doc = new org.jdom.Document();
	
				String latin = transliterate(roadObjectModel.getRoadObjectName());
				
				doc.addContent(new Comment(latin));
				doc.addContent(new Comment(roadObjectModel.getRoadObjectDateOfCreation()));
	
				///////////////////////////////////////////////////////////////////////////////////////////////////
				///////////////////// TRAFFIC CONTROLLER //////////////////////////
				///////////////////////////////////////////////////////////////////////////////////////////////////
				org.jdom.Element trafficController = new org.jdom.Element("trafficController");
				root.addContent(trafficController);
	
				org.jdom.Element logElement = new org.jdom.Element("log_level");
				logElement.setText("info");
				trafficController.addContent(logElement);
	
				org.jdom.Element protocolElement = new org.jdom.Element("protocol");
				String protocol = roadObjectModel.getRoadObjectProtocol();
				if (protocol.equals("Ethernet 'КОМКОН' - 115200")) {
					protocol = "komkon";
				}
				if (protocol.equals("Радио 'КОМКОН' - 1200")) {
					protocol = "komkon";
				}
				protocolElement.setText(protocol);
				trafficController.addContent(protocolElement);
	
				org.jdom.Element modeElement = new org.jdom.Element("mode");
				modeElement.setText("ARM");
				trafficController.addContent(modeElement);
	
				org.jdom.Element isAlwaysShowLedsElement = new org.jdom.Element("isAlwaysShowLeds");
				isAlwaysShowLedsElement.setText("false");
				trafficController.addContent(isAlwaysShowLedsElement);
	
				org.jdom.Element showLogLedsToPortElement = new org.jdom.Element("showLogLedsToPort");
				showLogLedsToPortElement.setText("true");
				trafficController.addContent(showLogLedsToPortElement);
	
				org.jdom.Element timeoutRequset_externalElement = new org.jdom.Element("timeoutRequset_external");
				timeoutRequset_externalElement.setText("400");
				trafficController.addContent(timeoutRequset_externalElement);
	
				org.jdom.Element timeoutBetweenByte_externalElement = new org.jdom.Element("timeoutBetweenByte_external");
				timeoutBetweenByte_externalElement.setText("20");
				trafficController.addContent(timeoutBetweenByte_externalElement);
	
				org.jdom.Element timeoutRequset_internalElement = new org.jdom.Element("timeoutRequset_internal");
				timeoutRequset_internalElement.setText("400");
				trafficController.addContent(timeoutRequset_internalElement);
	
				org.jdom.Element timeoutBetweenByte_internalElement = new org.jdom.Element("timeoutBetweenByte_internal");
				timeoutBetweenByte_internalElement.setText("20");
				trafficController.addContent(timeoutBetweenByte_internalElement);
	
				org.jdom.Element external_portsElement = new org.jdom.Element("external_ports");
				trafficController.addContent(external_portsElement);
	
				org.jdom.Element external_portsTypeElement = new org.jdom.Element("type");
				
				String external_porsType = roadObjectModel.getRoadObjectConnectType();
				
				if(external_porsType.equals("Ethernet")) {
					
					external_portsTypeElement.setText("ethernet");
					external_portsElement.addContent(external_portsTypeElement);
					
					org.jdom.Element ethernetElement = new org.jdom.Element("ethernet");
					external_portsElement.addContent(ethernetElement);
	
					org.jdom.Element ethernetPortElement = new org.jdom.Element("port");
					String ethernetPort = roadObjectModel.getRoadObjectConnectPort();
					ethernetPortElement.setText(ethernetPort);
					ethernetElement.addContent(ethernetPortElement);
					
					org.jdom.Element serialElement = new org.jdom.Element("serial");
					
					external_portsElement.addContent(serialElement);
					
					org.jdom.Element isEnabledElement = new org.jdom.Element("isEnabled");
					isEnabledElement.setText("false");
					serialElement.addContent(isEnabledElement);
					
					org.jdom.Element portElement = new org.jdom.Element("port");
					String gprsPort = "/dev/ttyUSB0";
					portElement.setText(gprsPort);
					serialElement.addContent(portElement);
	
					org.jdom.Element speedElement = new org.jdom.Element("speed");
					String gprsSpeed = roadObjectModel.getRoadObjectConnectSpeed();
					speedElement.setText(gprsSpeed);
					serialElement.addContent(speedElement);
					
				}else {
					
					external_portsTypeElement.setText("serial");
					external_portsElement.addContent(external_portsTypeElement);
					
					org.jdom.Element ethernetElement = new org.jdom.Element("ethernet");
					external_portsElement.addContent(ethernetElement);
	
					org.jdom.Element ethernetPortElement = new org.jdom.Element("port");
					String ethernetPort = "4002";
					ethernetPortElement.setText(ethernetPort);
					ethernetElement.addContent(ethernetPortElement);
					
					org.jdom.Element serialElement = new org.jdom.Element("serial");
					
					external_portsElement.addContent(serialElement);
					
					org.jdom.Element isEnabledElement = new org.jdom.Element("isEnabled");
					isEnabledElement.setText("true");
					serialElement.addContent(isEnabledElement);
					
					org.jdom.Element portElement = new org.jdom.Element("port");
					String gprsPort = "/dev/" + roadObjectModel.getRoadObjectConnectPort();
					portElement.setText(gprsPort);
					serialElement.addContent(portElement);
	
					org.jdom.Element speedElement = new org.jdom.Element("speed");
					String gprsSpeed = roadObjectModel.getRoadObjectConnectSpeed();
					speedElement.setText(gprsSpeed);
					serialElement.addContent(speedElement);
					
				}
	
				org.jdom.Element kdk_portElement = new org.jdom.Element("kdk_port");
				trafficController.addContent(kdk_portElement);
	
				org.jdom.Element isEnabledKDKElement = new org.jdom.Element("isEnabled");
				isEnabledKDKElement.setText("true");
				kdk_portElement.addContent(isEnabledKDKElement);
	
				org.jdom.Element kdkPortElement = new org.jdom.Element("port");
				String kdkPort = "/dev/" + roadObjectModel.getRoadObjectKDPPort();
				kdkPortElement.setText(kdkPort);
				kdk_portElement.addContent(kdkPortElement);
	
				org.jdom.Element kdkSpeedElement = new org.jdom.Element("speed");
				String kdkSpeed = roadObjectModel.getRoadObjectKDPSpeed();
				kdkSpeedElement.setText(kdkSpeed);
				kdk_portElement.addContent(kdkSpeedElement);
	
				org.jdom.Element led_portElement = new org.jdom.Element("leds_port");
				trafficController.addContent(led_portElement);
	
				org.jdom.Element isEnabledLedsElement = new org.jdom.Element("isEnabled");
				isEnabledLedsElement.setText("true");
				led_portElement.addContent(isEnabledLedsElement);
	
				org.jdom.Element ledsPortElement = new org.jdom.Element("port");
				String ledPort = "/dev/" + roadObjectModel.getRoadObjectLEDPort();
				ledsPortElement.setText(ledPort);
				led_portElement.addContent(ledsPortElement);
	
				org.jdom.Element ledsSpeedElement = new org.jdom.Element("speed");
				String ledSpeed = roadObjectModel.getRoadObjectKDPSpeed();
				ledsSpeedElement.setText(ledSpeed);
				led_portElement.addContent(ledsSpeedElement);
	
				org.jdom.Element tvpElement = new org.jdom.Element("tvp");
				trafficController.addContent(tvpElement);
	
				for (int i = 1; i < 5; i++) {
					org.jdom.Element tvpMaskElement = new org.jdom.Element("mask");
					String id = Integer.toString(i);
					tvpMaskElement.setAttribute("id", id);
					if (i == 1) {
						tvpMaskElement.setText("0x0200");
					}
					if (i == 2) {
						tvpMaskElement.setText("0x0400");
					}
					if (i == 3) {
						tvpMaskElement.setText("0x0800");
					}
					if (i == 4) {
						tvpMaskElement.setText("0x1000");
					}
					tvpElement.addContent(tvpMaskElement);
	
				}
	
				org.jdom.Element cycleElement = new org.jdom.Element("cycle");
				cycleElement.setText("/etc/komkon/kdk/config/cycle.xml");
				trafficController.addContent(cycleElement);
	
				org.jdom.Element addrFromElement = new org.jdom.Element("addrFrom");
				addrFromElement.setText("2");
				trafficController.addContent(addrFromElement);
	
				org.jdom.Element addrToElement = new org.jdom.Element("addrTo");
				String addrTo = roadObjectModel.getRoadObjectNetworkAddress();
				addrToElement.setText(addrTo);
				trafficController.addContent(addrToElement);
	
				org.jdom.Element timerElement = new org.jdom.Element("timer");
				timerElement.setText("200");
				trafficController.addContent(timerElement);
	
				org.jdom.Element timeoutReadTvpElement = new org.jdom.Element("timeoutReadTvp");
				timeoutReadTvpElement.setText("10");
				trafficController.addContent(timeoutReadTvpElement);
	
				///////////////////////////////////////////////////////////////////////////////////////////////////
				///////////////////////////////////////////////////////////////////////////////////////////////////
	
				///////////////////////////////////////////////////////////////////////////////////////////////////
				//////////////////////// SENSOR CONTROLLER /////////////////////////////////////
				///////////////////////////////////////////////////////////////////////////////////////////////////
	
				org.jdom.Element sensorControllerElement = new org.jdom.Element("sensorController");
				root.addContent(sensorControllerElement);
	
				org.jdom.Element progModeElement = new org.jdom.Element("progMode");
				progModeElement.setText("ARM");
				sensorControllerElement.addContent(progModeElement);
	
				org.jdom.Element sensorPortElement = new org.jdom.Element("port");
				sensorPortElement.setText("8090");
				sensorControllerElement.addContent(sensorPortElement);
	
				org.jdom.Element dbPathElement = new org.jdom.Element("dbPath");
				dbPathElement.setText("/etc/komkon/kdk/db");
				sensorControllerElement.addContent(dbPathElement);
	
				org.jdom.Element spiElement = new org.jdom.Element("spi");
				sensorControllerElement.addContent(spiElement);
	
				org.jdom.Element devElement = new org.jdom.Element("dev");
				devElement.setText("/dev/spidev0.0");
				spiElement.addContent(devElement);
	
				org.jdom.Element modeSpiElement = new org.jdom.Element("mode");
				modeSpiElement.setText("SPI_MODE_1");
				spiElement.addContent(modeSpiElement);
	
				org.jdom.Element spedSpiElement = new org.jdom.Element("speed");
				spedSpiElement.setText("1000000");
				spiElement.addContent(spedSpiElement);
	
				org.jdom.Element bitsPerWordElement = new org.jdom.Element("bitsPerWord");
				bitsPerWordElement.setText("8");
				spiElement.addContent(bitsPerWordElement);
	
				org.jdom.Element periodElement = new org.jdom.Element("period");
				periodElement.setText("0.1");
				spiElement.addContent(periodElement);
	
				org.jdom.Element write_to_dbSpiElement = new org.jdom.Element("write_to_db");
				write_to_dbSpiElement.setText("false");
				spiElement.addContent(write_to_dbSpiElement);
	
				org.jdom.Element collectTimeElement = new org.jdom.Element("collectTime");
				collectTimeElement.setText("60");
				spiElement.addContent(collectTimeElement);
	
				org.jdom.Element masksElement = new org.jdom.Element("masks");
				spiElement.addContent(masksElement);
	
				for (int i = 1; i < 17; i++) {
					org.jdom.Element spiMaskElement = new org.jdom.Element("mask");
					String id = Integer.toString(i);
					spiMaskElement.setAttribute("id", id);
	
					if (i == 1) {
						spiMaskElement.setText("0x0001");
					}
					if (i == 2) {
						spiMaskElement.setText("0x0002");
					}
					if (i == 3) {
						spiMaskElement.setText("0x0004");
					}
					if (i == 4) {
						spiMaskElement.setText("0x0008");
					}
					if (i == 5) {
						spiMaskElement.setText("0x0010");
					}
					if (i == 6) {
						spiMaskElement.setText("0x0020");
					}
					if (i == 7) {
						spiMaskElement.setText("0x0040");
					}
					if (i == 8) {
						spiMaskElement.setText("0x0080");
					}
					if (i == 9) {
						spiMaskElement.setText("0x0100");
					}
					if (i == 10) {
						spiMaskElement.setText("0x0200");
					}
					if (i == 11) {
						spiMaskElement.setText("0x0400");
					}
					if (i == 12) {
						spiMaskElement.setText("0x0800");
					}
					if (i == 13) {
						spiMaskElement.setText("0x1000");
					}
					if (i == 14) {
						spiMaskElement.setText("0x2000");
					}
					if (i == 15) {
						spiMaskElement.setText("0x4000");
					}
					if (i == 16) {
						spiMaskElement.setText("0x8000");
					}
	
					masksElement.addContent(spiMaskElement);
	
				}
	
				org.jdom.Element camerasElement = new org.jdom.Element("cameras");
				sensorControllerElement.addContent(camerasElement);
	
				org.jdom.Element connect_wating_timeElement = new org.jdom.Element("connect_wating_time");
				connect_wating_timeElement.setText("10");
				camerasElement.addContent(connect_wating_timeElement);
	
				org.jdom.Element number_attemts_to_reconectElement = new org.jdom.Element("number_attemts_to_reconect");
				number_attemts_to_reconectElement.setText("2");
				camerasElement.addContent(number_attemts_to_reconectElement);
	
				Map<RoadDetectorHBoxCell, List<Detector>> mapOfMultiZonesDetector = roadDetectorModel.getMapOfMultiZonesDetector();
				int idCam = 0;
				for (Map.Entry<RoadDetectorHBoxCell, List<Detector>> entry : mapOfMultiZonesDetector.entrySet()) {
					String typeDetector = entry.getKey().getChoiceBoxTypeOfDetector().getValue();
					if (typeDetector.equals("Камера")) {
						Detector detector = entry.getValue().get(0);
						String zones = Integer.toString(entry.getValue().size());
						idCam++;
						String id = Integer.toString(idCam);
						String period = detector.getPeriodInterrogation();
						String collectTime = detector.getPeriodSaving();
						String ip = detector.getIPDetector();
						String port = detector.getPort();
						String portXML = detector.getPortXML();
						String portHTTP = detector.getPortHTTP();
						String write_to_db = "true";
	
						org.jdom.Element camElement = new org.jdom.Element("cam");
						camElement.setAttribute("id", id);
	
						org.jdom.Element periodCamElement = new org.jdom.Element("period");
						periodCamElement.setText(period);
						camElement.addContent(periodCamElement);
	
						org.jdom.Element collectTimeCamElement = new org.jdom.Element("collectTime");
						collectTimeCamElement.setText(collectTime);
						camElement.addContent(collectTimeCamElement);
	
						org.jdom.Element ipCam = new org.jdom.Element("ip");
						ipCam.setText(ip);
						camElement.addContent(ipCam);
	
						org.jdom.Element portCam = new org.jdom.Element("port");
						portCam.setText(port);
						camElement.addContent(portCam);
	
						org.jdom.Element portXmlCam = new org.jdom.Element("portXml");
						portXmlCam.setText(portXML);
						camElement.addContent(portXmlCam);
	
						org.jdom.Element httpPortCam = new org.jdom.Element("httpPort");
						httpPortCam.setText(portHTTP);
						camElement.addContent(httpPortCam);
	
						org.jdom.Element write_to_dbCam = new org.jdom.Element("write_to_db");
						write_to_dbCam.setText(write_to_db);
						camElement.addContent(write_to_dbCam);
	
						org.jdom.Element zonesNumber = new org.jdom.Element("zones");
						zonesNumber.setText(zones);
						camElement.addContent(zonesNumber);
	
						camerasElement.addContent(camElement);
	
					}
				}
	
				/*
				 * mapRoadCameraSettings =
				 * iRoadModel.getModel().getRoadCameraModel().getMapRoadCameraSettings(); for
				 * (Map.Entry<String, RoadCameraSettings> entry :
				 * mapRoadCameraSettings.entrySet()) { String camId = entry.getKey();
				 * 
				 * RoadCameraSettings roadCameraSettings = entry.getValue(); String id =
				 * roadCameraSettings.getCameraID(); String period =
				 * roadCameraSettings.getCameraPeriod(); String collectTime =
				 * roadCameraSettings.getCameraCollectTime(); String camIP =
				 * roadCameraSettings.getCameraIP(); String camPort =
				 * roadCameraSettings.getCameraPort(); String portXML =
				 * roadCameraSettings.getCameraPortXML(); String portHTTP =
				 * roadCameraSettings.getCameraHTTPport(); boolean writeDB =
				 * roadCameraSettings.isWriteDB(); String camZone =
				 * roadCameraSettings.getCameraZonesAmount();
				 * 
				 * org.jdom.Element camElement = new org.jdom.Element("cam");
				 * camElement.setAttribute("id", id);
				 * 
				 * org.jdom.Element periodCamElement = new org.jdom.Element("period");
				 * periodCamElement.setText(period); camElement.addContent(periodCamElement);
				 * 
				 * org.jdom.Element collectTimeCamElement = new org.jdom.Element("collectTime");
				 * collectTimeCamElement.setText(collectTime);
				 * camElement.addContent(collectTimeCamElement);
				 * 
				 * org.jdom.Element ipCam = new org.jdom.Element("ip"); ipCam.setText(camIP);
				 * camElement.addContent(ipCam);
				 * 
				 * org.jdom.Element portCam = new org.jdom.Element("port");
				 * portCam.setText(camPort); camElement.addContent(portCam);
				 * 
				 * org.jdom.Element portXmlCam = new org.jdom.Element("portXml");
				 * portXmlCam.setText(portXML); camElement.addContent(portXmlCam);
				 * 
				 * org.jdom.Element httpPortCam = new org.jdom.Element("httpPort");
				 * httpPortCam.setText(portHTTP); camElement.addContent(httpPortCam);
				 * 
				 * org.jdom.Element write_to_dbCam = new org.jdom.Element("write_to_db");
				 * write_to_dbCam.setText(String.valueOf(writeDB));
				 * camElement.addContent(write_to_dbCam);
				 * 
				 * org.jdom.Element zonesCam = new org.jdom.Element("zones");
				 * zonesCam.setText(camZone); camElement.addContent(zonesCam);
				 * 
				 * camerasElement.addContent(camElement);
				 * 
				 * }
				 */
	
				///////////////////////////////////////////////////////////////////////////////////////////////////
				///////////////////////////////////////////////////////////////////////////////////////////////////
	
				// external_portsTypeElement.setText(external_porsType);
				// external_portsElement.addContent(external_portsTypeElement);
	
				// org.jdom.Element
	
				doc.setRootElement(root);
	
				XMLOutputter outter = new XMLOutputter();
				outter.setFormat(Format.getPrettyFormat());
				outter.output(doc, new FileWriter(new File(path + File.separator + "config.xml")));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void createConnectSettingFile(File path) {
		File connectSettingsFile = new File(path + File.separator + "net");
		char quotes = '"';
		
		String networkAddr = roadModel.getRoadObjectModel().getRoadObjectNetworkAddress();		
		int addr = Integer.parseInt(networkAddr);		
		networkAddr = Integer.toHexString(addr).toUpperCase();
		
		try {
			connectSettingsFile.createNewFile();
			
			FileWriter fileWriter = new FileWriter(connectSettingsFile);
		    PrintWriter printWriter = new PrintWriter(fileWriter);
		    printWriter.print("config_eth0=" + quotes + roadModel.getRoadObjectModel().getRoadObjectIP() + " " + "netmask " + roadModel.getRoadObjectModel().getRoadObjectMASK() + quotes + "\n");
		    printWriter.print("mac_eth0=" + quotes + "00:1F:F2:00:" + networkAddr + ":" +networkAddr + quotes + "\n");
		    printWriter.print("routes_eth0=" + quotes + "default via " + roadModel.getRoadObjectModel().getRoadObjectDNS() + quotes);
		    printWriter.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
}
