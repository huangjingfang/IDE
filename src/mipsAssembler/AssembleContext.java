package mipsAssembler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import mipsAssembler.utils.Constants;
import mipsAssembler.utils.Utils;
import utilities.DataUtil;

public class AssembleContext {
	public static final long DEFAULT_DATA_SEG_ADDRESS = 0X00000000;
	public static final long DEFAULT_CODE_SEG_ADDRESS = 0X00000000;
	
	private static long dsa;
	private static long csa;
	private HashMap<String, Long> label2Addr;
	private HashMap<String, Long> vari2Addr;
	public ArrayList<String> binaryData;
	public ArrayList<String> binaryIns;
	private long stackPoint;
	private int codeSegLength;
	private int dataSegLength;

	public AssembleContext() {
		label2Addr = new HashMap<>();
		binaryData = new ArrayList<>();
		binaryIns = new ArrayList<>();
		vari2Addr = new HashMap<>();
		stackPoint = 0X00000000;
		codeSegLength = 0;
		dataSegLength = 0;
	}

	public static AssembleContext parseContext(InputStream ins) throws Exception {
		AssembleContext context = new AssembleContext();

		BufferedReader reader = new BufferedReader(new InputStreamReader(ins, StandardCharsets.UTF_8));
		int lineNum = 0;
		String lineContent = new String();
		try {

			while (reader.ready()) {
				lineNum++;
				lineContent = reader.readLine();
				LineParse.parse(lineContent, context);

			}
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			String info = e.getMessage();
			throw new Exception("Wrong:Line" + lineNum + "\t" + info);
		}

		return context;
	}

	public void setLabel(String label) throws Exception {
		// TODO Auto-generated method stub
		if (label2Addr.containsKey(label)) {
			throw new Exception("Label Already Exist:" + label);
		}
		label2Addr.put(label, csa);
		System.out.println("新建标签："+label+"\t地址："+csa);
	}
	
	public void setVariable(String variable) throws Exception{
		if (vari2Addr.containsKey(variable)) {
			throw new Exception("Variable Already Exist:" + variable);
		}
		vari2Addr.put(variable, dsa);
		System.out.println("新建变量："+variable+"\t地址："+dsa);
	}

//	public void writeBytes(String bits) {
//		int bytes = bits.length()/Constants.BYTES_PER_WORD;
//		csa += bytes;
//		binaryIns.add(bits);
//	}
//	
//	public void Parse(String[] content) throws Exception{
//		PrintWriter writer = new PrintWriter("outInstruction.coe");
//		for(String s:content){
//			LineParse.parse(s, this);
//		}
//		int count = 1;
//		for(String s:binaryIns){
//			String hex = Utils.Bin2Hex(s, s.length()/Constants.BYTES_PER_WORD).toUpperCase();
//			System.out.println(hex);
//			if(count%8 == 0){
//				writer.write(hex);
//				writer.write(",\n");
//			}else{
//				writer.write(hex);
//				writer.write(",");
//			}
//			
//		}
//		writer.close();
//	}
	
	private void pre_parse(){
		Iterator<String> it = label2Addr.keySet().iterator();
		while(it.hasNext()){
			String key = it.next();
			long codeAddr = csa-codeSegLength;
			for(int i=0;i<binaryIns.size();i++){
				//System.out.print("原指令码："+binaryIns.get(i)+"\tkey:"+key);
				if(binaryIns.get(i).contains(key.trim())){
					//System.out.print(";包含标签："+key+"\t标签地址："+label2Addr.get(key));
					if(binaryIns.get(i).startsWith("000010")||binaryIns.get(i).startsWith("000011")){
						//j和jal
						//System.out.println("J型指令");
						String labelAddrB = Long.toBinaryString(label2Addr.get(key));
						System.out.println("J型指令：当前指令地址 :"+codeAddr+";标签"+key+"地址:"+label2Addr.get(key));
						binaryIns.set(i, binaryIns.get(i).replace(key,Utils.format(labelAddrB, 26) ));
					}else{
						//offset
						//System.out.println("I型指令");
						long labelAddr = label2Addr.get(key);
						System.out.println("R型指令：当前指令地址 :"+codeAddr+";标签"+key+"地址:"+labelAddr);
						long offset = labelAddr-codeAddr;
						String offsetB = Long.toBinaryString(offset);
						System.out.println("偏移值："+offset);
						if(offsetB.length()==64)
							offsetB = offsetB.substring(48, 64);
						else offsetB = Utils.format(offsetB, 16);
						//binaryIns.set(i, binaryIns.get(i).replace(key, Utils.format(offsetB, 16)));
						binaryIns.set(i, binaryIns.get(i).replace(key, offsetB));
					}
				}
				//System.out.println("\t更新指令码："+binaryIns.get(i));
				codeAddr+=Constants.BYTES_PER_INSTRUCTION;
			}
		}
		
		for(String s:binaryIns){
			System.out.println(s);
		}
	}
	public void Parse() throws Exception{
		pre_parse();
		PrintWriter writer_Ins = new PrintWriter(new File(DataUtil.currentFileName.replace(".mips", "ins.coe")));
		PrintWriter writer_Data = new PrintWriter(new File(DataUtil.currentFileName.replace(".mips", "data.coe")));
		writer_Ins.write("memory_initialization_radix=16;\n");
		writer_Ins.write("memory_initialization_vector=\n");
		for(int i=0;i<binaryIns.size();i++){
			String code = binaryIns.get(i);
			String hex = Utils.Bin2Hex(code, code.length()/Constants.BYTES_PER_WORD).toUpperCase();
			System.out.println(hex);
			if(i!=binaryIns.size()-1){
				if((i+1)%8 == 0){
					writer_Ins.write(hex);
					writer_Ins.write(",\n");
				}else{
					writer_Ins.write(hex);
					writer_Ins.write(",");
				}
			}else{
				writer_Ins.write(hex);
				writer_Ins.write(";");
			}
		}
		for(int i=0;i<binaryData.size();i++){
			String code = binaryData.get(i);
			String hex = Utils.Bin2Hex(code, code.length()/Constants.BYTES_PER_WORD).toUpperCase();
			System.out.println("binary code:"+code+"\thex code:"+hex);
			if(i!=binaryData.size()-1){
				if((i+1)%8 == 0){
					writer_Data.write(hex);
					writer_Data.write(",\n");
				}else{
					writer_Data.write(hex);
					writer_Data.write(",");
				}
			}else{
				writer_Data.write(hex);
				writer_Data.write(";");
			}
		}
		writer_Ins.close();
		writer_Data.close();
	}
	
	public void addrInc(int length){
		csa+=length;
		codeSegLength+=length;
	}
	public void dataInc(int length){
		dsa+=length;
		dataSegLength+=length;
	}

	public long getLabelAddress(String name) {
		// TODO Auto-generated method stub
		return label2Addr.get(name);
	}

	public long getVariableAddress(String name) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("Variable Name:"+name);
		Iterator<String> it = vari2Addr.keySet().iterator();
		while(it.hasNext()){
			String key = it.next();
			System.out.println(key+":"+vari2Addr.get(key));
		}
		if(vari2Addr.get(name)==null){
			String global = "Global"+name.substring(name.indexOf("_"));
			System.out.println(global+vari2Addr.get(global));
			if(vari2Addr.get(global)!=null){
				return vari2Addr.get(global);
			}else throw new Exception("不存在变量："+name+"或者"+global);
		}
		return vari2Addr.get(name);
	}

	public long getSp(){
		return stackPoint;
	}
	public void incSp(long inscrement){
		stackPoint+=inscrement;
	}
	public static void setDsa(long dataseg){
		dsa = dataseg;
	}
	public static void setCsa(long codeseg){
		csa = codeseg;
	}
}
