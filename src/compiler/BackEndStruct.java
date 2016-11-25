package compiler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import compiler.attributeDefinition;

public class BackEndStruct {
	HashMap<String, String> registerDesc;
	HashMap<String, String[]> addressDesc;
	String writePath;
	
	public BackEndStruct() {
		// TODO Auto-generated constructor stub
		//�Ĵ���������Ӧ�ó�ʼ����ÿ���Ĵ�����Ҫ����registerDesc���map�С���ʱ����Ҫ���ǵ������������Щ�Ĵ���Ҫ�������map
		registerDesc = new HashMap<>();
		addressDesc = new HashMap<>();
	}

	// ��ȡĳ��ָ����Ҫʹ�õļĴ���
	public String[] getReg(Quaternary q) {
		return null;
	}
	
	public String checkReg(String s){
		return null;
	}
	
	public boolean isInRegisterDesc(String addr){
		if(registerDesc.containsKey(addr)){
			return true;
		}else return false;
	}
	
	public void genCode(String input,String path) throws IOException {
		// TODO Auto-generated method stub
		writePath = path;
		BufferedReader reader = new BufferedReader(new FileReader(input));
		PrintWriter writer = new PrintWriter(writePath);
		while(reader.ready()){
			String temp = reader.readLine();
			if(temp.split(",").length==3){
				//�õ�ַΪ��Ԫʽ
				String[] split = temp.split(" ");
				String operator = split[0];
				String args[] = split[1].split(",");
				Quaternary q = new Quaternary(operator, args[0], args[1], args[2]);
				String code = genCode(q);
				writer.append(code);
			}else{
				writer.append(temp);
			}
		}
		
	}
	
	private String genCode(Quaternary q){
		String[] addr = getReg(q);
		StringBuilder builder = new StringBuilder();
		switch (q.op) {
		case "+":
			if(isInRegisterDesc(addr[0])){
				builder.append("LA "+ addr[0]+","+ q.arg0+"\n");
			}else if(isInRegisterDesc(addr[1])){
				builder.append("LA "+ addr[1]+","+ q.arg1+"\n");
			}else if(isInRegisterDesc(addr[2])){
				builder.append("LA "+ addr[2]+","+ q.result+"\n");
			}
			builder.append("ADD "+addr[2]+","+addr[0]+","+addr[1]+"\n");
			break;
		case "-":
			if(isInRegisterDesc(addr[0])){
				builder.append("LA "+ addr[0]+","+ q.arg0+"\n");
			}else if(isInRegisterDesc(addr[1])){
				builder.append("LA "+ addr[1]+","+ q.arg1+"\n");
			}else if(isInRegisterDesc(addr[2])){
				builder.append("LA "+ addr[2]+","+ q.result+"\n");
			}
			builder.append("SUB "+addr[2]+","+addr[0]+","+addr[1]+"\n");
			break;
		case "*":
			
			break;
		case "/":
			
			break;
		case "=":
			
			break;
		case "BEQ":
			
			break;
		case "BNE":
			
			break;
		case "CALL":
			
			break;
		case "POP":
			
			break;
		case "J":
			
			break;
		case "JAL":
			
			break;
		//����ָ��

		default:
			break;
		}
		return builder.toString();
	}
}
