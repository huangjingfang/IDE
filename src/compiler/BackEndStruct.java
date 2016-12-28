package compiler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BackEndStruct {
	private static final String FUNCTION_NAME_REGEX = "\\.text\\s+\\[(\\w+)\\]";
	private static final String DATASEG_REGEX = "\\.data\\s+\\[(\\w+)\\]";
	
	private static final Matcher m_FUNCTION_NAME_REGEX = Pattern.compile(FUNCTION_NAME_REGEX).matcher("");
	private static final Matcher m_DATASEG_REGEX = Pattern.compile(DATASEG_REGEX).matcher("");
	
	HashMap<String, String> registerDesc;// 寄存器描述符（寄存器2临时变量）
	HashMap<String, String> variDesc;// 变量描述符（变量2临时变量）
	HashMap<String, Long> seg2index;// label对应的位置
	HashMap<String, Long> func2index;// 函数代码对应的位置
	HashMap<String, String> vari2addr;// 临时变量的地址
	HashMap<String, String> tempVDesc;//临时变量对应的常量值
	int start;// 程序的起始地址
	long currentIndex;// 目前的程序地址偏移量
	LinkedList<String> regTeble;// 能用的寄存器列表
	int regIndex;
	String writePath;// 输出文件地址
	myLex lex;
	public HashMap<String, VariDesc> variTable;//变量（名称和域）2变量描述
	Random rand = new Random();

	public BackEndStruct() {
		// TODO Auto-generated constructor stub
		// 寄存器描述符应该初始化，每个寄存器都要放入registerDesc这个map中。暂时还需要考虑的问题就是有哪些寄存器要放入这个map
		registerDesc = new HashMap<>();
		variDesc = new HashMap<>();
		regTeble = new LinkedList<>();
		String[] regName = new String[] { "$t0", "$t1", "$t2", "$t3", "$t4", "$t5", "$t6", "$t7", "$t8", "$t9"//};
				,"$s0","$s1","$s2","$s2","$s3","$s4","$s5","$s6","$s7","$a0","$a1","$a2","$a3","$v0","$v1"};
		for (String s : regName)
			regTeble.add(s);
		seg2index = new HashMap<>();
		func2index = new HashMap<>();
		vari2addr = new HashMap<>();
		tempVDesc = new HashMap<>();
		start = 0;
		currentIndex = 0;
	}

	// 获取某条指令需要使用的寄存器
	private String[] getReg(String arg) {
		String[] toReturn = new String[1];
		if (registerDesc.containsValue(arg)) {
			Iterator<String> it = registerDesc.keySet().iterator();
			while (it.hasNext()) {
				String key = it.next();
				if (registerDesc.get(key).equals(arg)){
					toReturn[0] = key;
					//System.out.println("Matches!! "+key+" matches "+registerDesc.get(key));
				}
			}
		}
		if (toReturn[0] == null) {
			toReturn[0] = regTeble.get(regIndex);
			regIndex = (regIndex+1)%regTeble.size();
		}
		System.out.println(arg+" 分配到寄存器 "+toReturn[0]);
		return toReturn;
	}

	private String getReg(){
		String str =  regTeble.get(regIndex);
		regIndex = (regIndex+1)%regTeble.size();
		return str;
	}
	
	private String[] getReg(String arg, String arg1) {
		String[] toReturn = new String[2];
		String[] paras = { arg, arg1 };
		Iterator<String> it = registerDesc.keySet().iterator();
		for (int i = 0; i < 2; i++) {
			// 如果寄存器中已经存在该变量的值，直接返回该寄存器
			if (registerDesc.containsValue(paras[i])) {
				while (it.hasNext()) {
					String key = it.next();
					if (registerDesc.get(key).equals(paras[i])){
						toReturn[i] = key;
						break;
					}
				}
			}
		}

		if (toReturn[0] != null && toReturn[1] != null) {
			System.out.println(arg+" 分配到寄存器 "+toReturn[0]+"\t"+arg1+" 分配到寄存器 "+toReturn[1] );
			return toReturn;
		} else if (toReturn[0] == null && toReturn[1] == null) {
			toReturn[0] = regTeble.get(regIndex);
			toReturn[1] = regTeble.get((regIndex+1)%regTeble.size());
			regIndex = (regIndex+2)%regTeble.size();
			System.out.println(arg+" 分配到寄存器 "+toReturn[0]+"\t"+arg1+" 分配到寄存器 "+toReturn[1] );
			return toReturn;
		} else {
			if (toReturn[0] == null) {
				LinkedList<String> temp = new LinkedList<>(regTeble);
				temp.remove(toReturn[1]);
				toReturn[0] = temp.get(regIndex%temp.size());
				regIndex = (regIndex+1)%regTeble.size();
			} else {
				LinkedList<String> temp = new LinkedList<>(regTeble);
				temp.remove(toReturn[0]);
				toReturn[1] = temp.get(regIndex%temp.size());
				regIndex = (regIndex+1)%regTeble.size();
			}
			System.out.println(arg+" 分配到寄存器 "+toReturn[0]+"\t"+arg1+" 分配到寄存器 "+toReturn[1] );
			return toReturn;
		}

	}

	public String[] getReg(String arg0, String arg1, String result) {
		String[] toReturn = new String[3];
		String[] qContent = new String[]{arg0,arg1,result};
		Iterator<String> it = registerDesc.keySet().iterator();
		for (int i = 0; i < 3; i++) {
			// 如果寄存器中已经存在该变量的值，直接返回该寄存器
			if (registerDesc.containsValue(qContent[i])) {
				while (it.hasNext()) {
					String key = it.next();
					if (registerDesc.get(key).equals(qContent[i])){
						toReturn[i] = key;
						break;
					}
				}
			}
		}
		int count = 0;
		int[] indexs = new int[3];
		for (int i = 0; i < toReturn.length; i++) {
			if (toReturn[i] == null) {
				indexs[count] = i;
				count++;
			}
		}
		if (count == 0) {
			System.out.println(arg0+" 分配到寄存器 "+toReturn[0]+"\t"+arg1+" 分配到寄存器 "+toReturn[1]+"\t"+result+" 分配到寄存器 "+toReturn[2] );
			return toReturn;
		} else if (count == 3) {
			toReturn[0] = regTeble.get(regIndex);
			toReturn[1] = regTeble.get((regIndex+1)%regTeble.size());
			toReturn[2] = regTeble.get((regIndex+2)%regTeble.size());
			regIndex = (regIndex+3)%regTeble.size();
			System.out.println(arg0+" 分配到寄存器 "+toReturn[0]+"\t"+arg1+" 分配到寄存器 "+toReturn[1]+"\t"+result+" 分配到寄存器 "+toReturn[2] );
			return toReturn;
		} else if (count == 1) {
			LinkedList<String> temp = new LinkedList<>(regTeble);
			temp.remove(toReturn[indexs[2]]);
			temp.remove(toReturn[indexs[1]]);
			toReturn[indexs[0]] = temp.get(regIndex%temp.size());
			regIndex = (regIndex+1)%regTeble.size();
			System.out.println(arg0+" 分配到寄存器 "+toReturn[0]+"\t"+arg1+" 分配到寄存器 "+toReturn[1]+"\t"+result+" 分配到寄存器 "+toReturn[2] );
			return toReturn;
		} else {
			LinkedList<String> temp = new LinkedList<>(regTeble);
			temp.remove(toReturn[indexs[2]]);
			toReturn[indexs[0]] = temp.get(regIndex%temp.size());
			temp.remove(toReturn[indexs[0]]);
			toReturn[indexs[1]] = temp.get((regIndex+1)%temp.size());
			regIndex = (regIndex+2)%regTeble.size();
			System.out.println(arg0+" 分配到寄存器 "+toReturn[0]+"\t"+arg1+" 分配到寄存器 "+toReturn[1]+"\t"+result+" 分配到寄存器 "+toReturn[2] );
			return toReturn;
		}

	}

	public void genCode(String input, String path) throws Exception {
		// TODO Auto-generated method stub
		writePath = path;
		BufferedReader reader = new BufferedReader(new FileReader(input));
		PrintWriter writer = new PrintWriter(new File(writePath));
		StringBuilder text = new StringBuilder();
		while (reader.ready()) {
			String temp = reader.readLine();
			text.append(temp).append("\n");
		}
		// 将程序分段处理
		ArrayList<String> fragments = new ArrayList<>();
		String content = text.toString();
		String[] fs = content.split(".DATA");
		for (String s : fs) {
			if (s.length() == 0)
				continue;
			String temp = ".data" + s;
			fragments.add(temp);
			// System.out.println(temp);
			// System.out.println("-----------------------------------------------------");
		}
		System.out.println("分段完毕");
		// 处理每一段程序
		StringBuilder dataSeg = new StringBuilder();
		StringBuilder textSeg = new StringBuilder();
		dataSeg.append(".data\n");
		textSeg.append(".text\n").append("start:\n").append("\tori\t$sp,$zero,36863\n")
			.append("\tjal\tmain\n");
		for (String s : fragments) {
			String[] parts = s.split(".CODE");
			if (parts.length == 1) {
				// String code = processHeader(parts[0]);
				String[] varis = parseDataSeg(parts[0]);
				for(String str:varis)
					dataSeg.append("\t").append(str).append("\n");
			} else {
				// String header = processHeader(parts[0]);
				String[] varis = parseDataSeg(parts[0]);
				for(String str:varis)
					dataSeg.append("\t").append(str).append("\n");
				String code = processBody(".text" + parts[1]);
				textSeg.append(code);
			}
		}
		writer.append(dataSeg);
		writer.append(textSeg);
		reader.close();
		writer.close();
		System.out.println("指令代码生成结束");
	}
	private String[] parseDataSeg(String dataseg) throws Exception{
		String[] decs = dataseg.split("\n");
		String[] toReturn = new String[decs.length-1];
		String funName;
		m_DATASEG_REGEX.reset(decs[0]);
		if(m_DATASEG_REGEX.matches()){
			funName = m_DATASEG_REGEX.group(1)+"_";
		}else{
			funName = "Global_";
		}
		for(int i=1;i<decs.length;i++){
			String[] var_val = decs[i].trim().split("\\s+");
			StringBuilder temp = new StringBuilder();
			String f_vari = funName+var_val[0].substring(0, var_val[0].length()-1);
			String g_vari = "Global_"+var_val[0].substring(0, var_val[0].length()-1);
			System.out.println(f_vari+"\t"+g_vari);
			if(variTable.containsKey(f_vari.trim())){
				temp.append(f_vari).append(":").append("\t");
			}else if(variTable.containsKey(g_vari.trim())){
				temp.append(g_vari).append(":").append("\t");
			}else{
				throw new Exception("不存在变量："+var_val[0]);
			}
			for(int j=1;j<var_val.length;j++){
				temp.append(var_val[j]).append("\t");
			}
			toReturn[i-1] = temp.toString().trim();
		}
		return toReturn;
	}

	private String processBody(String body) throws Exception {
		String[] lexs = body.split("\n");
		String funName;
		StringBuilder toReturn = new StringBuilder();
		// 处理.Code [FNAME] 和函数名
		//toReturn.append(lexs[0] + "\n");
		m_FUNCTION_NAME_REGEX.reset(lexs[0].trim());
		if(m_FUNCTION_NAME_REGEX.matches()){
			funName = m_FUNCTION_NAME_REGEX.group(1);
			toReturn.append(funName + ":\n");
			func2index.put(funName, currentIndex);
		}else{
			throw new Exception("函数不存在："+lexs[0]);
		}
		//System.out.println(funName);
		// 处理函数内部指令
		for (int i = 2; i < lexs.length; i++) {
			String[] devide = lexs[i].split(":");
			if (devide.length == 1) {
				// 没有label
				String[] split = devide[0].split(" ");
				String operator = split[0];
				String args[] = split[1].split(",");
				if(operator.trim().equals("=")){
					for(int j=0;j<args.length;j++){
						if(args[j].trim().matches("[a-zA-Z]\\w*")){
							if(variTable.containsKey(funName+"_"+args[j]))
								args[j] = funName+"_"+args[j];
							else if(variTable.containsKey("Global_"+args[j]))
								args[j] = "Global_"+args[j];
							else throw new Exception("不存在变量："+args[j]);
						}
							
					}
				}
				Quaternary q = new Quaternary(operator, args[1], args[2], args[0]);
				//System.out.println("变换变量后的四元式值："+q.op + "\t" + q.arg0 + "\t" + q.arg1 + "\t" + q.result);
				String code = genCode(q);
				//System.out.println(code);
				toReturn.append(code);
			} else {
				// 带有label
				String label = devide[0];
				seg2index.put(label, currentIndex);
				toReturn.append(label+":");
				String[] split = devide[1].split(" ");
				String operator = split[0];
				String args[] = split[1].split(",");
				if(operator.trim().equals("=")){
					for(int j=0;j<args.length;j++){
						if(args[j].trim().matches("[a-zA-Z]\\w*"))
							if(variTable.containsKey(funName+"_"+args[j]))
								args[j] = funName+"_"+args[j];
							else if(variTable.containsKey("Global_"+args[j]))
								args[j] = "Global_"+args[j];
							else throw new Exception("不存在变量："+args[j]);
					}
				}
				Quaternary q = new Quaternary(operator, args[1], args[2], args[0]);
				//System.out.println(q.op + "\t" + q.arg0 + "\t" + q.arg1 + "\t" + q.result);
				String code = genCode(q);
				//System.out.println(code);
				toReturn.append(code);
			}

		}
		return toReturn.toString();
	}

	/**
	 * 事先检查中间变量是否在寄存器中，如果中间在，直接跳过，否则将寄存中的内容写回到内存中
	 * 
	 * 
	 * @param addr 寄存器的地址
	 * @param qValues 需要存入该寄存器的临时变量
	 * @return
	 */
	private String pre_process(String[] addr, String[] qValues) {
		StringBuilder builder = new StringBuilder();
		System.out.println();
		for(int i=0;i<addr.length;i++){
			registerDesc.put(addr[i], qValues[i]);
			System.out.print(addr[i]+":"+qValues[i]+"\t\t\t");
			
		}
		System.out.println();
//		for (int i = 0; i < addr.length; i++) {
//			//寄存器中的变量就是该临时变量的值或为空。
//			if(registerDesc.get(addr[i])==null||registerDesc.get(addr[i]).equals(qValues[i])){
//				continue;
//			}else{
//				//两种情况：1、寄存器中的值是一个暂时变量；2、寄存器中的值是一个真实变量
//				String tempVar = registerDesc.get(addr[i]);
//				String realVar = getKey(variDesc, tempVar);//获取寄存器中的值对应的真实变量
//				System.out.println("addr:"+addr[i]+"\toldTV:"+tempVar+"\tnewTV:"+qValues[i]+"\toldRV:"+realVar+"\tnewRV:"+getKey(variDesc, qValues[i]));
//				if(realVar==null){
//					//开辟新的一块空间存储该临时变量
//					builder.append("\tsw "+ addr[i]+",tempVari\n");
//				}else{
//					builder.append("\tsw "+ addr[i]+","+realVar+"\n");
//					//新临时变量并没有对应一个实际变量
//
//				}
//				String newVar = getKey(variDesc, qValues[i]);
//				if(newVar==null){
//					continue;
//				}else {
//					builder.append("\tlw " + addr[i] + "," + newVar + "\n");
//				}
//			}
//		}
		return builder.toString();
	}

	private String genCode(Quaternary q) {
		String[] addr;
		String[] qValues;
		StringBuilder builder = new StringBuilder();
		System.out.println("四元式："+q.op.trim()+" "+q.result+","+q.arg0+","+q.arg1);
		switch (q.op.trim()) {
		case "+":
			if (q.arg1.matches("(\\d)+")) {
				// 立即数加，arg1为立即数，arg0为变量或寄存器名，result为寄存器名
				//System.out.println(getKey(variDesc, q.arg0)+variTable.get(getKey(variDesc, q.arg0)));
				if (variTable.get(getKey(variDesc, q.arg0)).type.equalsIgnoreCase("unsigned")) {
					// 无符号数加
					addr = getReg(q.arg0, q.result);
					qValues = new String[]{q.arg0,q.result};
					builder.append(pre_process(addr, qValues));
					builder.append("\t" + "addiu " + addr[1] + "," + addr[0] + "," + q.arg1 + "\n");
				} else if (variTable.get(getKey(variDesc, q.arg0)).type.equalsIgnoreCase("Integer")) {
					// 整数加
					addr = getReg(q.arg0, q.result);
					qValues = new String[]{q.arg0,q.result};
					builder.append(pre_process(addr, qValues));
					builder.append("\t" + "addi " + addr[1] + "," + addr[0] + "," + q.arg1 + "\n");
				}
			} else {
				if (variTable.get(getKey(variDesc, q.arg1))!=null&&variTable.get(getKey(variDesc, q.arg1)).type.equalsIgnoreCase("unsigned")) {
					// 无符号数加
					addr = getReg(q.arg0, q.arg1, q.result);
					qValues = new String[] { q.arg0, q.arg1, q.result };
					builder.append(pre_process(addr, qValues));
					builder.append("\t" + "addu " + addr[2] + "," + addr[0] + "," + addr[1] + "\n");
				} else{// if (variTable.get(getKey(variDesc, q.arg1)).type.equalsIgnoreCase("Integer")) {
					// 整数加
					addr = getReg(q.arg0, q.arg1, q.result);
					qValues = new String[] { q.arg0, q.arg1, q.result };
					builder.append(pre_process(addr, qValues));
					builder.append("\t" + "add " + addr[2] + "," + addr[0] + "," + addr[1] + "\n");
				}
			}

			// return builder.toString();
			break;
		case "-":
			if (variTable.get(getKey(variDesc, q.arg1))!=null&&variTable.get(getKey(variDesc, q.arg1)).type.equalsIgnoreCase("unsigned")) {
				// 无符号数-
				addr = getReg(q.arg0, q.arg1, q.result);
				qValues = new String[] { q.arg0, q.arg1, q.result };
				builder.append(pre_process(addr, qValues));
				builder.append("\t" + "subu " + addr[2] + "," + addr[0] + "," + addr[1] + "\n");
			} else{// if (variTable.get(getKey(variDesc, q.arg1)).type.equalsIgnoreCase("Integer")) {
				// 整数-
				addr = getReg(q.arg0, q.arg1, q.result);
				qValues = new String[] { q.arg0, q.arg1, q.result };
				builder.append(pre_process(addr, qValues));
				builder.append("\t" + "sub " + addr[2] + "," + addr[0] + "," + addr[1] + "\n");
			}
			break;
		case "*":
			if (variTable.get(getKey(variDesc, q.arg1))!=null&&variTable.get(getKey(variDesc, q.arg1)).type.equalsIgnoreCase("unsigned")) {
				// 无符号数*
				addr = getReg(q.arg0, q.arg1, q.result);
				qValues = new String[] { q.arg0, q.arg1, q.result };
				builder.append(pre_process(addr, qValues));
				builder.append("\t" + "multu " + addr[0] + "," + addr[1] + "\n");
				builder.append("\t" + "mflo " + addr[2] + "\n");
			} else{// if (variTable.get(getKey(variDesc, q.arg1)).type.equalsIgnoreCase("Integer")) {
				// 整数*
				addr = getReg(q.arg0, q.arg1, q.result);
				qValues = new String[] { q.arg0, q.arg1, q.result };
				builder.append(pre_process(addr, qValues));
				builder.append("\t" + "mult " + addr[0] + "," + addr[1] + "\n");
				builder.append("\t" + "mflo " + addr[2] + "\n");
			}
			break;
		case "%":
			if (variTable.get(getKey(variDesc, q.arg1))!=null&&variTable.get(getKey(variDesc, q.arg1)).type.equalsIgnoreCase("unsigned")) {
				// 无符号数*
				addr = getReg(q.arg0, q.arg1, q.result);
				qValues = new String[] { q.arg0, q.arg1, q.result };
				builder.append(pre_process(addr, qValues));
				builder.append("\t" + "multu " + addr[0] + "," + addr[1] + "\n");
				builder.append("\t" + "mflo " + addr[2] + "\n");
			} else{// if (variTable.get(getKey(variDesc, q.arg1)).type.equalsIgnoreCase("Integer")) {
				// 整数*
				addr = getReg(q.arg0, q.arg1, q.result);
				qValues = new String[] { q.arg0, q.arg1, q.result };
				builder.append(pre_process(addr, qValues));
				builder.append("\t" + "mult " + addr[0] + "," + addr[1] + "\n");
				builder.append("\t" + "mflo " + addr[2] + "\n");
			}
			break;
		case "/":
			if (variTable.get(getKey(variDesc, q.arg1))!=null&&variTable.get(getKey(variDesc, q.arg1)).type.equalsIgnoreCase("unsigned")) {
				// 无符号数*
				addr = getReg(q.arg0, q.arg1, q.result);
				qValues = new String[] { q.arg0, q.arg1, q.result };
				builder.append(pre_process(addr, qValues));
				builder.append("\t" + "divu " + addr[0] + "," + addr[1] + "\n");
				builder.append("\t" + "mflo " + addr[2] + "\n");
			} else{// if (variTable.get(getKey(variDesc, q.arg1)).type.equalsIgnoreCase("Integer")) {
				// 整数*
				addr = getReg(q.arg0, q.arg1, q.result);
				qValues = new String[] { q.arg0, q.arg1, q.result };
				builder.append(pre_process(addr, qValues));
				builder.append("\t" + "div " + addr[0] + "," + addr[1] + "\n");
				builder.append("\t" + "mflo " + addr[2] + "\n");
			}
			break;
		case "=":
			if (q.arg1.matches("(\\d)+")) {
				// 将常量存入寄存器
				addr = getReg(q.result);
				if (registerDesc.containsKey(addr[0]) &&
						(registerDesc.get(addr[0]) != null)|(!registerDesc.get(addr[0]).equals(q.result))) {
					// 将原变量转出到内存
					String var = getKey(variDesc, registerDesc.get(addr[0]));
					//builder.append("\tsw "+ addr[0]+","+var+"\n");
				}
				//builder.append(pre_process(addr, new String[]{q.result}));
				builder.append("\t" + "ori " + addr[0] +",$zero," + q.arg1 + "\n");
				registerDesc.put(addr[0], q.result);
				tempVDesc.put(q.result, q.arg1);
			} else if (q.arg1.matches("@t(\\d)+")) {
				// 将寄存器中的值写回变量save
				addr = getReg(q.arg1);
				//System.out.println(q.arg1+"得到的寄存器为"+addr[0]);
				//System.out.println(addr[0]);
				builder.append("\tsw "+addr[0]+ ","+q.result+ " ($zero)\n");
				variDesc.put(q.result, q.arg1);
				registerDesc.put(addr[0],q.arg1);
				System.out.println(q.result+":\t"+q.arg1);
			} else {
				//将变量存入寄存器load
				//如果variDesc中有一个临时变量对应一个临时变量，并且该临时变量在寄存器中，
				String tempVar = variDesc.get(q.arg1);
				String varAddr = getKey(registerDesc, tempVar);
				System.out.println(registerDesc);
				if(tempVar == null||varAddr==null){
					addr = getReg(q.result);
					String oldVar = registerDesc.get(addr[0]);
					String vari = getKey(variDesc, oldVar);
					System.out.println("OldValue:"+oldVar+"\tVari:"+vari);
					if(vari!=null){
						variDesc.put(vari, null);
					}
					registerDesc.put(addr[0], q.result);
					variDesc.put(q.arg1, q.result);
					builder.append(pre_process(addr, new String[]{q.result}));
					builder.append("\tlw "+addr[0]+","+q.arg1+ " ($zero)\n");
				}else{
					variDesc.put(q.arg1, q.result);
					registerDesc.put(varAddr, q.result);
					System.out.println(q.arg1+":"+q.result+"\t"+varAddr+":"+q.result);
				}
			}
			
			
			break;
		case "=[]":
			//将数组中的变量导入到寄存器中
			if(q.result.matches("@t(\\d)+")){
				addr = getReg(q.result);
				qValues = new String[]{q.result};
				builder.append(pre_process(addr, qValues));
				int offset = Integer.parseInt(tempVDesc.get(q.arg0))*8;
				builder.append("\tlw "+addr[0]+","+offset+"("+q.arg1+")"+"\n");
			}else{
				//将寄存器中的内容导入到变量内存中
				addr = getReg(q.arg1);
				qValues = new String[]{q.arg1};
				builder.append(pre_process(addr, qValues));
				int offset = Integer.parseInt(tempVDesc.get(q.arg0))*8;
				builder.append("\tsw "+addr[0]+","+offset+"("+q.result+")"+"\n");
			}
			break;
		case "&":
			if(q.arg1.matches("(\\d)+")){
				//andi
				addr = getReg(q.arg0, q.result);
				qValues = new String[] { q.arg0, q.result };
				builder.append(pre_process(addr, qValues));
				builder.append("\t" + "andi " + addr[1] + "," + addr[0] + "," + q.arg1 + "\n");
			}else{
				//and
				addr = getReg(q.arg0, q.arg1, q.result);
				qValues = new String[] { q.arg0, q.arg1, q.result };
				builder.append(pre_process(addr, qValues));
				builder.append("\t" + "and " + addr[2] + "," + addr[0] + "," + addr[1] + "\n");
			}
			break;
		case "|":
			if(q.arg1.matches("(\\d)+")){
				//ori
				addr = getReg(q.arg0, q.result);
				qValues = new String[] { q.arg0, q.result };
				builder.append(pre_process(addr, qValues));
				builder.append("\t" + "ori " + addr[1] + "," + addr[0] + "," + q.arg1 + "\n");
			}else{
				//or
				addr = getReg(q.arg0, q.arg1, q.result);
				qValues = new String[] { q.arg0, q.arg1, q.result };
				builder.append(pre_process(addr, qValues));
				builder.append("\t" + "or " + addr[2] + "," + addr[0] + "," + addr[1] + "\n");
			}
			break;
		case "^":
			if(q.arg1.matches("(\\d)+")){
				//xori
				addr = getReg(q.arg0, q.result);
				qValues = new String[] { q.arg0, q.result };
				builder.append(pre_process(addr, qValues));
				builder.append("\t" + "xori " + addr[1] + "," + addr[0] + "," + q.arg1 + "\n");
			}else{
				//xor
				addr = getReg(q.arg0, q.arg1, q.result);
				qValues = new String[] { q.arg0, q.arg1, q.result };
				builder.append(pre_process(addr, qValues));
				builder.append("\t" + "xor " + addr[2] + "," + addr[0] + "," + addr[1] + "\n");
			}
			break;
		case "~":
			if(q.arg0.trim().equals("-")){
				addr = getReg(q.arg1,q.result);
				qValues = new String[]{q.arg1,q.result};
				builder.append(pre_process(addr, qValues));
				builder.append("\t" + "nor " + addr[1] + ",$zero," + addr[0] + "\n");
			}else{
				addr = getReg(q.arg0, q.arg1, q.result);
				qValues = new String[] { q.arg0, q.arg1, q.result };
				builder.append(pre_process(addr, qValues));
				builder.append("\t" + "nor " + addr[2] + "," + addr[0] + "," + addr[1] + "\n");
			}
			break;
		case "$":
			addr = getReg(q.arg1,q.result);
			qValues = new String[]{q.arg1,q.result};
			builder.append(pre_process(addr, qValues));
			String vari = getKey(variDesc, q.arg1);
			builder.append("\t"+addr[1]+" address of "+vari+"\n");
			break;	
		case "LSHIFT":
			if(!q.arg1.matches("@t(\\d)+")){
				//立即数位移
				addr = getReg(q.arg0, q.result);
				qValues = new String[]{q.arg0,q.result};
				builder.append(pre_process(addr, qValues));
				builder.append("\tsll "+addr[1]+","+addr[0]+","+q.arg1+"\n");
			}else{
				//非立即数位移
				addr = getReg(q.arg0,q.arg1,q.result);
				qValues = new String[]{q.arg0,q.arg1,q.result};
				builder.append(pre_process(addr, qValues));
				builder.append("\tsllv "+addr[2]+","+addr[0]+","+addr[1]+"\n");
			}
			break;
		case "RSHIFT":
			if(!q.arg1.matches("@t(\\d)+")){
				//立即数位移
				addr = getReg(q.arg0, q.result);
				qValues = new String[]{q.arg0,q.result};
				builder.append(pre_process(addr, qValues));
				builder.append("\tsrl "+addr[1]+","+addr[0]+","+q.arg1+"\n");
			}else{
				//非立即数位移
				addr = getReg(q.arg0,q.arg1,q.result);
				qValues = new String[]{q.arg0,q.arg1,q.result};
				builder.append(pre_process(addr, qValues));
				builder.append("\tsrlv "+addr[2]+","+addr[0]+","+addr[1]+"\n");
			}
			break;
		case "BEQ":
			addr = getReg(q.arg0, q.result);
			qValues = new String[] { q.arg0, q.result };
			builder.append(pre_process(addr, qValues));
			builder.append("\t" + "beq " + addr[0] + "," + addr[1] + ","+q.arg0+ "\n");

			break;
		case "BNE":
			addr = getReg(q.arg0, q.result);
			qValues = new String[] { q.arg0, q.result };
			builder.append(pre_process(addr, qValues));
			builder.append("\t" + "bne " + addr[0] + "," + addr[1] + ","+q.arg0+ "\n");
			break;
		case "BLEZ":
			addr = getReg(q.arg0);
			qValues = new String[] { q.arg0 };
			builder.append(pre_process(addr, qValues));
			builder.append("\t" + "blez " + addr[0] + "," + ","+q.arg0+ "\n");
			break;
		case "BGEZ":
			addr = getReg(q.arg0);
			qValues = new String[] { q.arg0 };
			builder.append(pre_process(addr, qValues));
			builder.append("\t" + "bgez " + addr[0] + "," + ","+q.arg0+ "\n");
			break;
		case "BLTZ":
			addr = getReg(q.arg0);
			qValues = new String[] { q.arg0 };
			builder.append(pre_process(addr, qValues));
			builder.append("\t" + "bltz " + addr[0] + ","+q.arg0+ "\n");
			break;
		case "BGTZ":
			addr = getReg(q.arg0);
			qValues = new String[] { q.arg0 };
			builder.append(pre_process(addr, qValues));
			builder.append("\t" + "bgtz " + addr[0] + ","+q.arg0+ "\n");
			break;
		case "POP":
			if(q.result.trim().equals("$ra")){
				builder.append("\tpop $ra\n");
			}else{
				addr = getReg(q.result);
				qValues = new String[]{q.result};
				pre_process(addr, qValues);
				builder.append("\tpop "+addr[0]+"\n");
			}
			break;
		case "PUSH":
			if(q.result.trim().equals("$ra")){
				builder.append("\tpush $ra\n");
			}else{
				addr = getReg(q.result);
				qValues = new String[]{q.result};
				pre_process(addr, qValues);
				builder.append("\tpush "+addr[0]+"\n");
			}
			break;
		case "J":
			builder.append("\tj "+q.result+ "\n");
			break;
		case "JAL":
			builder.append("\tjal "+q.result+ "\n");
			break;
		case "JR":
			addr = getReg(q.result);
			qValues = new String[]{q.result};
			pre_process(addr, qValues);
			builder.append("\tjr "+addr[0]+ "\n");
			break;
		// 其他指令

		default:
			break;
		}
		System.out.println("该四元式产生的代码为："+builder.toString());
		return builder.toString();
	}

	public void setLex(myLex l) {
		lex = l;
		variTable = new HashMap<>();
		for (sNode node : l.VariSignary) {
			variTable.put(node.actionScope+"_"+node.name, new VariDesc(node));
			System.out.println(node.actionScope+"\t"+node.morpheme+"\t"+
					node.name+"\t"+node.property+"\t"+node.size+"\t"+node.type);
		}
		
		Iterator<String> it = variTable.keySet().iterator();
		while(it.hasNext()){
			String key = it.next();
			VariDesc value = variTable.get(key);
			System.out.println(key+"\t"+value.actionScope+"\t"+value.type);
		}
	}
	
	private String getKey(Map<String,String> m,Object value){
		System.out.println(m);
		Iterator<String> it = m.keySet().iterator();
		String toReturn = null;
		while(it.hasNext()){
			String key = it.next();
			if(m.get(key)!=null&&m.get(key).equals(value)){
				toReturn = key;
			}else continue;
				
		}
		return toReturn;
	}
}
