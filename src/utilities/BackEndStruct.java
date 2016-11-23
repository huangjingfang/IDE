package utilities;

import java.util.HashMap;

public class BackEndStruct {
	HashMap<String, String> registerDesc;
	HashMap<String, String[]> addressDesc;
	
	public BackEndStruct() {
		// TODO Auto-generated constructor stub
		//寄存器描述符应该初始化，每个寄存器都要放入registerDesc这个map中。暂时还需要考虑的问题就是有哪些寄存器要放入这个map
		registerDesc = new HashMap<>();
		addressDesc = new HashMap<>();
	}

	// 获取某条指令需要使用的寄存器
	public static int[] getReg(Quaternary q) {
		return null;
	}
}
