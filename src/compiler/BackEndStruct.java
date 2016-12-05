package compiler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class BackEndStruct {
	HashMap<String, String> registerDesc;// �Ĵ������������Ĵ���2��ʱ������
	HashMap<String, String> variDesc;// ����������������2��ʱ������
	HashMap<String, Integer> seg2index;// label��Ӧ��λ��
	HashMap<String, Integer> func2index;// ���������Ӧ��λ��
	HashMap<String, String> vari2addr;// ��ʱ�����ĵ�ַ
	int start;// �������ʼ��ַ
	int currentIndex;// Ŀǰ�ĳ����ַƫ����
	LinkedList<String> regTeble;// ���õļĴ����б�
	int regIndex;
	String writePath;// ����ļ���ַ
	myLex lex;
	public HashMap<String, VariDesc> variTable;//���������ƺ���2��������
	Random rand = new Random();

	public BackEndStruct() {
		// TODO Auto-generated constructor stub
		// �Ĵ���������Ӧ�ó�ʼ����ÿ���Ĵ�����Ҫ����registerDesc���map�С���ʱ����Ҫ���ǵ������������Щ�Ĵ���Ҫ�������map
		registerDesc = new HashMap<>();
		variDesc = new HashMap<>();
		regTeble = new LinkedList<>();
		String[] regName = new String[] { "$t0", "$t1", "$t2", "$t3", "$t4", "$t5", "$t6", "$t7", "$t8", "$t9" };
		for (String s : regName)
			regTeble.add(s);
		seg2index = new HashMap<>();
		func2index = new HashMap<>();
		vari2addr = new HashMap<>();
		start = 0;
		currentIndex = 0;
	}

	// ��ȡĳ��ָ����Ҫʹ�õļĴ���
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
		return toReturn;
	}

	private String[] getReg(String arg, String arg1) {
		String[] toReturn = new String[2];
		String[] paras = { arg, arg1 };
		Iterator<String> it = registerDesc.keySet().iterator();
		for (int i = 0; i < 2; i++) {
			// ����Ĵ������Ѿ����ڸñ�����ֵ��ֱ�ӷ��ظüĴ���
			if (registerDesc.containsValue(paras[i])) {
				while (it.hasNext()) {
					String key = it.next();
					if (registerDesc.get(key).equals(paras[i]))
						toReturn[i] = key;
				}
			}
		}

		if (toReturn[0] != null && toReturn[1] != null) {
			return toReturn;
		} else if (toReturn[0] == null && toReturn[1] == null) {
			toReturn[0] = regTeble.get(regIndex);
			toReturn[1] = regTeble.get((regIndex+1)%regTeble.size());
			regIndex = (regIndex+2)%regTeble.size();
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
			return toReturn;
		}

	}

	public String[] getReg(String arg0, String arg1, String result) {
		String[] toReturn = new String[3];
		String[] qContent = new String[3];
		qContent[0] = arg0;
		qContent[1] = arg1;
		qContent[2] = result;
		Iterator<String> it = registerDesc.keySet().iterator();
		for (int i = 0; i < 3; i++) {
			// ����Ĵ������Ѿ����ڸñ�����ֵ��ֱ�ӷ��ظüĴ���
			if (registerDesc.containsValue(qContent[i])) {
				while (it.hasNext()) {
					String key = it.next();
					if (registerDesc.get(key).equals(qContent[i]))
						toReturn[i] = key;
					else
						continue;
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
			return toReturn;
		} else if (count == 3) {
			toReturn[0] = regTeble.get(regIndex);
			toReturn[1] = regTeble.get((regIndex+1)%regTeble.size());
			toReturn[2] = regTeble.get((regIndex+2)%regTeble.size());
			regIndex = (regIndex+3)%regTeble.size();
			return toReturn;
		} else if (count == 1) {
			LinkedList<String> temp = new LinkedList<>(regTeble);
			temp.remove(toReturn[indexs[2]]);
			temp.remove(toReturn[indexs[1]]);
			toReturn[indexs[0]] = temp.get(regIndex%temp.size());
			regIndex = (regIndex+1)%regTeble.size();
			return toReturn;
		} else {
			LinkedList<String> temp = new LinkedList<>(regTeble);
			temp.remove(toReturn[indexs[2]]);
			toReturn[indexs[0]] = temp.get(regIndex%temp.size());
			temp.remove(toReturn[indexs[0]]);
			toReturn[indexs[1]] = temp.get((regIndex+1)%temp.size());
			regIndex = (regIndex+2)%regTeble.size();
			return toReturn;
		}

	}

	public void genCode(String input, String path) throws IOException {
		// TODO Auto-generated method stub
		writePath = path;
		BufferedReader reader = new BufferedReader(new FileReader(input));
		PrintWriter writer = new PrintWriter(writePath);
		StringBuilder text = new StringBuilder();
		while (reader.ready()) {
			String temp = reader.readLine();
			text.append(temp).append("\n");
		}
		// ������ֶδ���
		ArrayList<String> fragments = new ArrayList<>();
		String content = text.toString();
		String[] fs = content.split(".DATA");
		for (String s : fs) {
			if (s.length() == 0)
				continue;
			String temp = ".DATA" + s;
			fragments.add(temp);
			// System.out.println(temp);
			// System.out.println("-----------------------------------------------------");
		}
		System.out.println("�ֶ����");
		// ����ÿһ�γ���
		for (String s : fragments) {
			String[] parts = s.split(".CODE");
			if (parts.length == 1) {
				// String code = processHeader(parts[0]);
				writer.append(parts[0]);
			} else {
				// String header = processHeader(parts[0]);
				String code = processBody(".CODE" + parts[1]);
				writer.append(parts[0]);
				writer.append(code);
			}
		}
		reader.close();
		writer.close();
		System.out.println("ָ��������ɽ���");
	}



	private String processHeader(String header) {
		StringBuilder toReturn = new StringBuilder();

		return toReturn.toString();
	}

	private String processBody(String body) {
		String[] lexs = body.split("\n");
		StringBuilder toReturn = new StringBuilder();
		// ����.Code [FNAME] �ͺ�����
		toReturn.append(lexs[0] + "\n");
		String funName = lexs[1].split(":")[0];
		toReturn.append(funName + ":\n");
		func2index.put(funName, currentIndex);
		System.out.println(funName);
		// �������ڲ�ָ��
		for (int i = 2; i < lexs.length; i++) {
			String[] devide = lexs[i].split(":");
			if (devide.length == 1) {
				// û��label
				String[] split = devide[0].split(" ");
				String operator = split[0];
				String args[] = split[1].split(",");
				Quaternary q = new Quaternary(operator, args[1], args[2], args[0]);
				//System.out.println(q.op + "\t" + q.arg0 + "\t" + q.arg1 + "\t" + q.result);
				String code = genCode(q);
				//System.out.println(code);
				toReturn.append(code);
			} else {
				// ����label
				String label = devide[0];
				seg2index.put(label, currentIndex);
				toReturn.append(label+":");
				String[] split = devide[1].split(" ");
				String operator = split[0];
				String args[] = split[1].split(",");
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
	 * ���ȼ�����б����Ƿ��ڼĴ����У������ֱ�����������򽫱���װ�ؽ���Ĵ���
	 * @param addr �Ĵ����ĵ�ַ
	 * @param qValues ��Ҫ����üĴ�������ʱ����
	 * @return
	 */
	private String pre_process(String[] addr, String[] qValues) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < addr.length; i++) {
			//�Ĵ����еı������Ǹ���ʱ������ֵ
			if(registerDesc.get(addr[i])==null||registerDesc.get(addr[i]).equals(qValues[i])){
				continue;
			}else{
				//�Ĵ����еı������Ǹ���ʱ������ֵ,���Ĵ����е�ֵд��variDesc
				String tempVar = registerDesc.get(addr[i]);
				String realVar = getKey(variDesc, tempVar);
				if(realVar==null){
					//�����µ�һ��ռ�洢����ʱ����
					builder.append("\tsw "+ addr[i]+",tempVari\n");
				}else{
					builder.append("\tsw "+ addr[i]+","+realVar+"\n");
				}
				//����ʱ������û�ж�Ӧһ��ʵ�ʱ���
				String newVar = getKey(variDesc, qValues[i]);
				if(newVar==null){
					continue;
				}else {
					builder.append("\t" + "lw " + addr[i] + "," + newVar + "\n");
				}
			}
			
//			//����Ĵ����д���һ��ֵ�������ֵ����Ŀ�����ֵ���򽫼Ĵ����е�ֵ���浽������
//			if (registerDesc.containsKey(addr[i]) &&
//					(registerDesc.get(addr[i]) != null)|(!registerDesc.get(addr[i]).equals(qValues[i]))) {
//				// ��ԭ����ת�����ڴ棬�����±���д��Ĵ���
//				builder.append(changeValue(addr[i], registerDesc.get(addr[i]), qValues[i]));
//			} else{
//				//�����жϸ���ʱ��������������ʱ������û�ж�Ӧһ��ʵ�ʱ�������ôֱ��������һ��������ʵ�ʱ���load������Ĵ�����
//				if()
//				builder.append("\t" + "lw " + addr[i] + "," + qValues[i] + "\n");
//				registerDesc.put(addr[i], qValues[i]);
//			}
		}
		return builder.toString();
	}

	/**
	 * ���Ĵ�����ԭ����ֵ�滻Ϊ�µ�ֵ
	 * @param addr �Ĵ�����ַ
	 * @param oldVar ԭ���Ĵ����е���ʱ����
	 * @param newVar ��Ҫload���Ĵ����е��µ���ʱ����
	 * @return
	 */
//	private String changeValue(String addr, String oldVar, String newVar) {
//		StringBuilder strb = new StringBuilder();
//		String tempAddr = registerDesc.get(addr);
//		String vari = getKey(variDesc, tempAddr);
//		strb.append("\t" + "sw " + addr + "," + vari + "\n");
//		strb.append("\t" + "lw " + addr + "," + newVar + "\n");
//		registerDesc.put(addr, newVar);
//		return strb.toString();
//	}

	private String genCode(Quaternary q) {
		String[] addr;
		String[] qValues;
		StringBuilder builder = new StringBuilder();
		// System.out.println(q.op.length());
		switch (q.op.trim()) {
		case "+":
			//System.out.println(variTable==null);
			if (q.arg0.matches("(\\d)+")) {
				// �������ӣ�arg0Ϊ��������arg1Ϊ������Ĵ�������resultΪ�Ĵ�����
				if (variTable.get(getKey(variDesc, q.arg1)).type.equalsIgnoreCase("unsigned")) {
					// �޷�������
					addr = getReg(q.arg1, q.result);
					qValues = new String[]{q.arg1,q.result};
					builder.append(pre_process(addr, qValues));
					builder.append("\t" + "addiu " + addr[1] + "," + q.arg0 + "," + addr[0] + "\n");
				} else if (variTable.get(getKey(variDesc, q.arg1)).type.equalsIgnoreCase("Integer")) {
					// ������
					addr = getReg(q.arg1, q.result);
					qValues = new String[]{q.arg1,q.result};
					builder.append(pre_process(addr, qValues));
					builder.append("\t" + "addi " + addr[1] + "," + q.arg0 + "," + addr[0] + "\n");
				}
			} else {
				if (variTable.get(getKey(variDesc, q.arg1))!=null&&variTable.get(getKey(variDesc, q.arg1)).type.equalsIgnoreCase("unsigned")) {
					// �޷�������
					addr = getReg(q.arg0, q.arg1, q.result);
					qValues = new String[] { q.arg0, q.arg1, q.result };
					builder.append(pre_process(addr, qValues));
					builder.append("\t" + "addu " + addr[2] + "," + addr[0] + "," + addr[1] + "\n");
				} else{// if (variTable.get(getKey(variDesc, q.arg1)).type.equalsIgnoreCase("Integer")) {
					// ������
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
				// �޷�����-
				addr = getReg(q.arg0, q.arg1, q.result);
				qValues = new String[] { q.arg0, q.arg1, q.result };
				builder.append(pre_process(addr, qValues));
				builder.append("\t" + "subu " + addr[2] + "," + addr[0] + "," + addr[1] + "\n");
			} else{// if (variTable.get(getKey(variDesc, q.arg1)).type.equalsIgnoreCase("Integer")) {
				// ����-
				addr = getReg(q.arg0, q.arg1, q.result);
				qValues = new String[] { q.arg0, q.arg1, q.result };
				builder.append(pre_process(addr, qValues));
				builder.append("\t" + "sub " + addr[2] + "," + addr[0] + "," + addr[1] + "\n");
			}
			break;
		case "*":
			if (variTable.get(getKey(variDesc, q.arg1))!=null&&variTable.get(getKey(variDesc, q.arg1)).type.equalsIgnoreCase("unsigned")) {
				// �޷�����*
				addr = getReg(q.arg0, q.arg1, q.result);
				qValues = new String[] { q.arg0, q.arg1, q.result };
				builder.append(pre_process(addr, qValues));
				builder.append("\t" + "multu " + addr[0] + "," + addr[1] + "\n");
				builder.append("\t" + "mflo " + addr[2] + "\n");
			} else{// if (variTable.get(getKey(variDesc, q.arg1)).type.equalsIgnoreCase("Integer")) {
				// ����*
				addr = getReg(q.arg0, q.arg1, q.result);
				qValues = new String[] { q.arg0, q.arg1, q.result };
				builder.append(pre_process(addr, qValues));
				builder.append("\t" + "mult " + addr[0] + "," + addr[1] + "\n");
				builder.append("\t" + "mflo " + addr[2] + "\n");
			}
			break;
		case "%":
			if (variTable.get(getKey(variDesc, q.arg1))!=null&&variTable.get(getKey(variDesc, q.arg1)).type.equalsIgnoreCase("unsigned")) {
				// �޷�����*
				addr = getReg(q.arg0, q.arg1, q.result);
				qValues = new String[] { q.arg0, q.arg1, q.result };
				builder.append(pre_process(addr, qValues));
				builder.append("\t" + "multu " + addr[0] + "," + addr[1] + "\n");
				builder.append("\t" + "mflo " + addr[2] + "\n");
			} else{// if (variTable.get(getKey(variDesc, q.arg1)).type.equalsIgnoreCase("Integer")) {
				// ����*
				addr = getReg(q.arg0, q.arg1, q.result);
				qValues = new String[] { q.arg0, q.arg1, q.result };
				builder.append(pre_process(addr, qValues));
				builder.append("\t" + "mult " + addr[0] + "," + addr[1] + "\n");
				builder.append("\t" + "mflo " + addr[2] + "\n");
			}
			break;
		case "/":
			if (variTable.get(getKey(variDesc, q.arg1))!=null&&variTable.get(getKey(variDesc, q.arg1)).type.equalsIgnoreCase("unsigned")) {
				// �޷�����*
				addr = getReg(q.arg0, q.arg1, q.result);
				qValues = new String[] { q.arg0, q.arg1, q.result };
				builder.append(pre_process(addr, qValues));
				builder.append("\t" + "divu " + addr[0] + "," + addr[1] + "\n");
				builder.append("\t" + "mflo " + addr[2] + "\n");
			} else{// if (variTable.get(getKey(variDesc, q.arg1)).type.equalsIgnoreCase("Integer")) {
				// ����*
				addr = getReg(q.arg0, q.arg1, q.result);
				qValues = new String[] { q.arg0, q.arg1, q.result };
				builder.append(pre_process(addr, qValues));
				builder.append("\t" + "div " + addr[0] + "," + addr[1] + "\n");
				builder.append("\t" + "mflo " + addr[2] + "\n");
			}
			break;
		case "=":
			if (q.arg1.matches("(\\d)+")) {
				// ����������Ĵ���
				addr = getReg(q.result);
				if (registerDesc.containsKey(addr[0]) &&
						(registerDesc.get(addr[0]) != null)|(!registerDesc.get(addr[0]).equals(q.result))) {
					// ��ԭ����ת�����ڴ�
					String var = getKey(variDesc, registerDesc.get(addr[0]));
					builder.append("\tsw "+ addr[0]+","+var+"\n");
				}
				//builder.append(pre_process(addr, new String[]{q.result}));
				builder.append("\t" + "ori " + addr[0] +",$zero," + q.arg1 + "\n");
				registerDesc.put(addr[0], q.result);
			} else if (q.arg1.matches("@t(\\d)+")) {
				// ���Ĵ����е�ֵд�ر���save
				Iterator<String> it = registerDesc.keySet().iterator();
				while(it.hasNext()){
					String item = it.next();
					System.out.println(item+"\t\t"+registerDesc.get(item));
				}
				addr = getReg(q.arg1);
				System.out.println(q.arg1+"�õ��ļĴ���Ϊ"+addr[0]);
				System.out.println(addr[0]);
				builder.append("\tsw "+addr[0]+ ","+q.result+ "\n");
				variDesc.put(q.result, q.arg1);
			} else {
				//����������Ĵ���load
				if(variDesc.get(q.arg1)!=null){
					variDesc.put(q.arg1, q.result);
					String address = getKey(registerDesc, q.result);
					registerDesc.put(address, q.result);
				}else{
					addr = getReg(q.result);
					builder.append("\tlw "+addr[0]+","+q.arg1+ "\n");
					String oldVar = registerDesc.get(addr[0]);
					String vari = getKey(variDesc, oldVar);
					if(vari!=null){
						variDesc.put(vari, null);
					}
					registerDesc.put(addr[0], q.result);
					variDesc.put(q.arg1, q.result);
				}
			}
			break;
		case "=[]":

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
			addr = getReg(q.arg0, q.arg1, q.result);
			qValues = new String[] { q.arg0, q.arg1, q.result };
			builder.append(pre_process(addr, qValues));
			builder.append("\t" + "nor " + addr[2] + "," + addr[0] + "," + addr[1] + "\n");
			break;
		case "<<":
			
			break;
		case ">>":
			
			break;
		case "BEQ":
			addr = getReg(q.arg0, q.arg1);
			qValues = new String[] { q.arg0, q.arg1 };
			builder.append(pre_process(addr, qValues));
			builder.append("\t" + "beq " + addr[0] + "," + addr[1] + ",/offset" + "\n");

			break;
		case "BNE":
			addr = getReg(q.arg0, q.arg1);
			qValues = new String[] { q.arg0, q.arg1 };
			builder.append(pre_process(addr, qValues));
			builder.append("\t" + "bne " + addr[0] + "," + addr[1] + "/offset"+ "\n");
			break;
		case "BLEZ":
			addr = getReg(q.arg0);
			qValues = new String[] { q.arg0 };
			builder.append(pre_process(addr, qValues));
			builder.append("\t" + "blez " + addr[0] + "," + "/offset"+ "\n");
			break;
		case "BGEZ":
			addr = getReg(q.arg0);
			qValues = new String[] { q.arg0 };
			builder.append(pre_process(addr, qValues));
			builder.append("\t" + "bgez " + addr[0] + "," + "/offset"+ "\n");
			break;
		case "BLTZ":
			addr = getReg(q.arg0);
			qValues = new String[] { q.arg0 };
			builder.append(pre_process(addr, qValues));
			builder.append("\t" + "bltz " + addr[0] + "," + "/offset"+ "\n");
			break;
		case "BGTZ":
			addr = getReg(q.arg0);
			qValues = new String[] { q.arg0 };
			builder.append(pre_process(addr, qValues));
			builder.append("\t" + "bgtz " + addr[0] + "," + "/offset"+ "\n");
			break;
		case "CALL":

			break;
		case "POP":
//			builder.append("\tlw $a1, 0($sp)"+ "\n");
//			builder.append("\tadd $v0,$v0,$a1"+ "\n");
//			builder.append("\tlw $ra, 4($sp)"+ "\n");
//			builder.append("\taddi $sp,$sp,8"+ "\n");
//			builder.append("\tjr $ra"+ "\n");
			break;
		case "PUSH":
//			builder.append("\taddi $sp,$sp,-8"+ "\n");
//			builder.append("\tsw $ra, 4($sp)"+ "\n");
//			builder.append("\tsw $a1, 0($sp)"+ "\n");
//			builder.append("\tor $a1,$a0,$zero"+ "\n");
//			builder.append("\tjal mult"+ "\n");
			break;
		case "J":
			builder.append("\tj "+q.result+ "\n");
			break;
		case "JAL":
			builder.append("\tjal "+q.result+ "\n");
			break;
		case "JR":
			builder.append("\tjr "+q.result+ "\n");
			break;
		// ����ָ��

		default:
			break;
		}

		return builder.toString();
	}

	public void setLex(myLex l) {
		lex = l;
		variTable = new HashMap<>();
		for (sNode node : l.VariSignary) {
			variTable.put(node.name, new VariDesc(node));
			System.out.println(node.actionScope+"\t"+node.morpheme+"\t"+
					node.name+"\t"+node.property+"\t"+node.size+"\t"+node.type);
		}
	}
	
	private String getKey(Map<String,String> m,Object value){
		Iterator<String> it = m.keySet().iterator();
		String o = null;
		while(it.hasNext()){
			o = it.next();
			if(m.get(o)!=null&&m.get(o).equals(value))
				return o;
		}
		return o;
	}
}
