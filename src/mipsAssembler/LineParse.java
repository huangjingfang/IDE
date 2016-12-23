package mipsAssembler;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.security.auth.DestroyFailedException;

import mipsAssembler.isa.Declare;
import mipsAssembler.isa.DeclareType;
import mipsAssembler.isa.Operation;
import mipsAssembler.isa.Oprand;
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
	private static Matcher m_VARIABLE_DATAS = Pattern.compile(VARIABLE_DATAS).matcher("");
	
	


	//解析读入的一行代码
	public static void parse(String line,AssembleContext context) throws Exception{
		m_COMMENT_REGEX.reset(line);
		if(m_COMMENT_REGEX.matches()){
			line = m_COMMENT_REGEX.group(1);//去注释
		}
		if(line==null)
			return;
		System.out.println("Line:"+line);
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
					System.out.println("laebl:"+label);
				}else{
					context.setVariable(labelName.trim());
					System.out.println("vari:"+label);
				}
				
			}else{
				throw new Exception("Illeagel Label Name:"+label);
			}
		}
	}

	//解析一行代码的指令部分
	private static void parseLine(String instruction, AssembleContext context) throws Exception {
		System.out.println("指令部分："+instruction);
		if(instruction.trim().startsWith(".")){
			//段的声明，数据段变量的声明
			String data = instruction.substring(instruction.indexOf(".")+1);
			System.out.println(data);
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
			System.out.println("binarys:"+bin);
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
		Operation operation = Operation.getOperationByName(op);
		String[] ops = splitOprand(oprand);
		List<Oprand> oprands = Oprand.parse(ops,operation.mode.types);
		Instruction ins = Instruction.genInstruction(operation, oprands);
		ins = Instruction.macro(ins,context);
		context.binaryIns.add(ins.genBinary(context));
		context.addrInc(Constants.BYTES_PER_INSTRUCTION);
	}
	
	
	//分裂字符串
	private static String[] splitDataSeg(String content){
		String[] segs = content.split(DATA_SPLITOR);
		return segs;
	}
	private static String[] splitOprand(String content){
		String[] oprands = content.split(OPRAND_SPLITOR);
		return oprands;
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


