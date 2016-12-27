package mipsAssembler;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.security.auth.DestroyFailedException;

import compiler.BackEndStruct;
import mipsAssembler.isa.Declare;
import mipsAssembler.isa.DeclareType;
import mipsAssembler.isa.Operation;
import mipsAssembler.isa.Oprand;
import mipsAssembler.isa.OprandMode;
import mipsAssembler.isa.OprandType;
import mipsAssembler.utils.Constants;
import mipsAssembler.utils.Utils;

public class LineParse {
	public static boolean currentSeg = true;//true代表代码段，false代表数据段
	
	private static final String COMMENT_REGEX = "(.+)?\\#.*";
	private static final String STATEMENT_REGEX = "(\\S+:)?(.+)?"; //每行除注释以外的内容，包括label和指令
	private static final String ELEMENT_REGEX = "(\\s+)?(\\S+)(\\s+)?(.*)?"; //每条指令,每个数据声明
	private static final String VARIABLE_DATAS = "(\\S+)\\s+(.+)";
	private static final String OPRAND_SPLITOR = "[: \t,\\(\\)]";
	private static final String DATA_SPLITOR ="," ;
	private static final String LABEL_IDENTIFIER = "([a-zA-Z._$][0-9a-zA-Z._$]*):";

	
	private static Matcher m_COMMENT_REGEX = Pattern.compile(COMMENT_REGEX).matcher("");
	private static Matcher m_STATEMENT_REGEX = Pattern.compile(STATEMENT_REGEX).matcher("");
	private static Matcher m_ELEMENT_REGEX = Pattern.compile(ELEMENT_REGEX).matcher("");
	private static Matcher m_LABEL_IDENTIFIER = Pattern.compile(LABEL_IDENTIFIER).matcher("");

	
	//解析读入的一行代码
	public static void parse(String line,AssembleContext context) throws Exception{
		m_COMMENT_REGEX.reset(line);
		if(m_COMMENT_REGEX.matches()){
			line = m_COMMENT_REGEX.group(1);//去注释
		}
		if(line==null)
			return;
		//System.out.println("Line:"+line);
		line =line.trim();
		if(line.isEmpty()){
			return;
		}
		m_STATEMENT_REGEX.reset(line);
		if(!m_STATEMENT_REGEX.matches()){
			throw new Exception("Illeagel Statement:"+line);
		}else{
			String labelName = m_STATEMENT_REGEX.group(1);
			if(labelName!=null)
				parseLabel(labelName, context);//解析label
			String instruction = m_STATEMENT_REGEX.group(2);
			if(instruction!=null)
				parseLine(instruction, context);//解析指令
		}
		
	}

	//解析一行代码中的label
	private static void parseLabel(String label, AssembleContext context) throws Exception {
		
		if(label!=null){
			m_LABEL_IDENTIFIER.reset(label);
			if(m_LABEL_IDENTIFIER.matches()){
				String labelName = m_LABEL_IDENTIFIER.group(1);
				if(currentSeg){//代码段
					context.setLabel(labelName.trim());
					//System.out.println("laebl:"+label);
				}else{
					context.setVariable(labelName.trim());
					//System.out.println("vari:"+label);
				}
				
			}else{
				throw new Exception("Illeagel Label Name:"+label);
			}
		}
	}

	//解析一行代码的指令部分
	private static void parseLine(String instruction, AssembleContext context) throws Exception {
		//System.out.println("指令部分："+instruction);
		if(instruction.trim().startsWith(".")){
			//段的声明，数据段变量的声明
			String data = instruction.substring(instruction.indexOf(".")+1);
			//System.out.println(data);
			m_ELEMENT_REGEX.reset(data);
			if(!m_ELEMENT_REGEX.matches()){
				throw new Exception("Data declare wrong:"+data);
			}else{
				String type = m_ELEMENT_REGEX.group(2);
				String values = m_ELEMENT_REGEX.group(4);
				parseDataSeg(type,values, context);//解析数据声名
			}
		}else{
			//57条指令
			if(instruction.isEmpty())
				return;
			m_ELEMENT_REGEX.reset(instruction);
			if(!m_ELEMENT_REGEX.matches()){
				throw new Exception("Illegal Instruction:"+instruction);
			}else{
				//System.out.println(instruction);
				String op = m_ELEMENT_REGEX.group(2);
				//System.out.println(op);
				String oprands = m_ELEMENT_REGEX.group(4).trim();
				parseInstruction(op,oprands, context); //解析操作
			}
		}
	}
	
	//段的声明，数据段变量的声明
	private static void parseDataSeg(String type,String value,AssembleContext context) throws Exception{
		String[] values = splitDataSeg(value);
		DeclareType d_type = DeclareType.getDeclareTypeByName(type);
		if(d_type==null){
			throw new Exception("No such type:"+type);
		}else{
			Declare d = new Declare(d_type, values);
			String bin = d.getBinary();
			//System.out.println("binarys:"+bin);
			if(bin==null)
				return;
			String[] bins = bin.split(",");
			int length = bins.length;
			for(int i=0;i<length;i++){
				context.binaryData.add(bins[i]);
				context.dataInc(1);
			}
		}
		
	}
	
	//57条指令的解析
	private static void parseInstruction(String op,String oprand,AssembleContext context) throws Exception{
		String[] new_instruction = pre_process(op,oprand,context);
		if(new_instruction[0].equals("mult-ins")){
			String[] inss = new_instruction[1].split("\n");
			for(String s:inss){
				parseLine(s, context);
			}
		}else{
			Operation operation = Operation.getOperationByName(new_instruction[0]);
			String[] ops = splitOprand(new_instruction[1]);
			List<Oprand> oprands = Oprand.parse(ops,operation.mode.types);
			Instruction ins = Instruction.genInstruction(operation, oprands);
			context.binaryIns.add(ins.genBinary(context));
			//System.out.println("指令："+op+"\t"+oprand+";指令码："+Utils.Bin2Hex(ins.genBinary(context), 8));
			context.addrInc(Constants.BYTES_PER_INSTRUCTION);
		}
	}
	
	private static String[] pre_process(String op,String oprand,AssembleContext context) throws Exception{
		//System.out.println("伪指令："+op+"\t"+oprand);
		String[] toReturn = new String[2];
		switch (op.trim()) {
		case "lb":
		case "lbu":
		case "lh":
		case "lhu":
		case "lw":
		case "sb":
		case "sh":
		case "sw":
			String[] ops = splitOprand(oprand);
			OprandType[] types = new OprandType[ops.length];
			for(int i=0;i<ops.length;i++){
				types[i] = OprandType.identifyOprand(ops[i]);
			}
			
			if(OprandMode.isInstanceOf(types,OprandMode.I_RS_RT_OFFSET)){
				toReturn[0] = op;
				toReturn[1] = oprand;
			}
			else if(OprandMode.isInstanceOf(types,OprandMode.I_RS_RT_VARI)){
				//将vari-offset形式的指令转化为offset-base类型的指令
				toReturn[0] = "mult-ins";
				String[] oprands = splitOprand(oprand);
				long adr = context.getVariableAddress(oprands[1]);
				long high16 = (adr&0XFFFF0000)>>16;
				long low16 = adr&0X0000FFFF;
				if(low16>0X00008000){
					low16+= high16;
				}
				StringBuilder builder = new StringBuilder();
				builder.append("lui").append("\t").append("$at").append(",").append(high16).append(",").append("\n");
				builder.append("addu").append("\t").append("$at").append(",").append("$at").append(",").append(oprands[2]).append("\n");
				builder.append(op).append("\t").append(oprands[0]).append(",").append(low16).append(",").append("$at").append("\n");
				toReturn[1] = builder.toString();
				//System.out.println(toReturn[1]);
			}else throw new Exception("指令操作数有误："+op+"\t"+oprand);
			
			break;
		case "push":
			//System.out.println("栈指针位置："+context.getSp());
			toReturn[0] = "sw";
			toReturn[1] = oprand + ","+context.getSp()+"($sp)";
			context.incSp(4);
			break;
		case "pop":
			//System.out.println("栈指针位置："+context.getSp());
			toReturn[0] = "lw";
			toReturn[1] = oprand + ","+context.getSp()+"($sp)";
			context.incSp(-4);
			break;
		case "adr":
			String[] oprands = splitOprand(oprand);
			long adr = context.getVariableAddress(oprands[1]);
			toReturn[0] = "ori";
			toReturn[1] = oprands[0]+",$zero,"+adr;
			break;
		default:
			toReturn[0] = op;
			toReturn[1] = oprand;
			break;
		}
		//System.out.println("伪指令转换结果："+toReturn[0]+"\t"+toReturn[1]);
		return toReturn;
		
	}
	//分裂字符串
	private static String[] splitDataSeg(String content){
		String[] segs = content.split(DATA_SPLITOR);
		for(int i=0;i<segs.length;i++){
			if(segs[i].trim().equals("?"))
				segs[i] = "0";
		}
		return segs;
	}
	private static String[] splitOprand(String content){
		String[] oprands = content.split(OPRAND_SPLITOR);
		int nonEmptyNum = 0;
		for(String s:oprands){
			if(!s.isEmpty())
				nonEmptyNum ++;
		}
		String[] toReturn = new String[nonEmptyNum];
		int count = 0;
		for(int i=0;i<oprands.length;i++){
			if(!oprands[i].isEmpty()){
				toReturn[count] = oprands[i];
				count++;
			}else continue;
		}
		return toReturn;
	}
	
	public static void main(String[] args) throws Exception {
		String test = "sw $1,10($3)";
		AssembleContext context = new AssembleContext();
		parse(test, context);
		for(String code:context.binaryIns){
			System.out.println(code);
			System.out.println(Utils.Bin2Hex(code, code.length()/4).toUpperCase());
		}
		
	}
}


