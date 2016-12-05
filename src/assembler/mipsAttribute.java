package assembler;

import java.util.Vector;

public class mipsAttribute {
	static public String registerCalculation(String name)
	{
		switch(name)
		{
		case "$0":
		case "$zero":
			return "00000";
		case "$1":
		case "$at":
			return "00001";
		case "$2":
		case "$v0":
			return "00010";
		case "$3":
		case "$v1":
			return "00011";
		case "$4":
		case "$a0":
			return "00100";
		case "$5":
		case "$a1":
			return "00101";
		case "$6":
		case "$a2":
			return "00110";
		case "$7":
		case "$a3":
			return "00111";
		case "$8":
		case "$t0":
			return "01000";
		case "$9":
		case "$t1":
			return "01001";
		case "$10":
		case "$t2":
			return "01010";
		case "$11":
		case "$t3":
			return "01011";
		case "$12":
		case "$t4":
			return "01100";
		case "$13":
		case "$t5":
			return "01101";
		case "$14":
		case "$t6":
			return "01110";
		case "$15":
		case "$t7":
			return "01111";
		case "$16":
		case "$s0":
			return "10000";
		case "$17":
		case "$s1":
			return "10001";
		case "$18":
		case "$s2":
			return "10010";
		case "$19":
		case "$s3":
			return "10011";
		case "$20":
		case "$s4":
			return "10100";
		case "$21":
		case "$s5":
			return "10101";
		case "$22":
		case "$s6":
			return "10110";
		case "$23":
		case "$s7":
			return "10111";
		case "$24":
		case "$t8":
			return "11000";
		case "$25":
		case "$t9":
			return "11001";
		case "$26":
		case "$k0":
			return "11010";
		case "$27":
		case "$k1":
			return "11011";
		case "$28":
		case "$gp":
			return "11100";
		case "$29":
		case "$sp":
			return "11101";
		case "$30":
		case "$s8":
		case "$fp":
			return "11110";
		case "$31":
		case "$ra":
			return "11111";
		default:
			System.out.println("不合法的寄存器标识符");
			System.exit(0);
		}
		return "";
	}
	
	static public String ROP_func(String name)
	{
		switch(name)
		{
		case "ADD":
			return "100000";
		case "ADDU":
			return "100001";
		case "SUB":
			return "100010";
		case "SUBU":
			return "100011";
		case "AND":
			return "100100";
		case "MULT":
			return "011000";
		case "MULTU":
			return "011001";
		case "DIV":
			return "011010";
		case "DIVU":
			return "011011";
		case "MFHI":
			return "010000";
		case "MFLO":
			return "010010";
		case "MTHI":
			return "010001";
		case "MTLO":
			return "010011";
		case "MFC0":
			return "000sel";
		case "MTC0":
			return "000sel";
		case "OR":
			return "100101";
		case "XOR":
			return "100110";
		case "NOR":
			return "100111";
		case "SLT":
			return "101010";
		case "SLTU":
			return "101011";
		case "SLL":
			return "000000";
		case "SRL":
			return "000010";
		case "SRA":
			return "000011";
		case "SLLV":
			return "000100";
		case "SRLV":
			return "000110";
		case "SRAV":
			return "000111";
		case "JR":
			return "001000";
		case "JALR":
			return "001001";
		case "BREAK":
			return "001101";
		case "SYSCALL":
			return "001100";
		case "ERET":
			return "011000";
		}
		return "";
	}
	
	static public String OP_op(String name)
	{
		switch(name)
		{
		case "ADD":
		case "ADDU":
		case "SUB":
		case "SUBU":
		case "AND":
		case "MULT":
		case "MULTU":
		case "DIV":
		case "DIVU":
		case "MFHI":
		case "MFLO":
		case "MTHI":
		case "MTLO":
		case "OR":
		case "XOR":
		case "NOR":
		case "SLT":
		case "SLTU":
		case "SLL":
		case "SRL":
		case "SRA":
		case "SLLV":
		case "SRLV":
		case "SRAV":
		case "JR":
		case "JALR":
		case "BREAK":
		case "SYSCALL":
			return "000000";
		case "MFC0":
		case "MTC0":
		case "ERET":
			return "010000";
		case "ADDI":
			return "001000";
		case "ADDIU":
			return "001001";
		case "ANDI":
			return "001100";
		case "ORI":
			return "001101";
		case "XORI":
			return "001110";
		case "LUI":
			return "001111";
		case "LB":
			return "100000";
		case "LBU":
			return "100100";
		case "LH":
			return "100001";
		case "LHU":
			return "100101";
		case "SB":
			return "101000";
		case "SH":
			return "101001";
		case "LW":
			return "100011";
		case "SW":
			return "101011";
		case "BEQ":
			return "000100";
		case "BNE":
			return "000101";
		case "BGEZ":
		case "BLTZ":
		case "BGEZAL":
		case "BLTZAL":
			return "000001";
		case "BGTZ":
			return "000111";
		case "BLEZ":
			return "000110";
		case "SLTI":
			return "001010";
		case "SLTIU":
			return "001011";
		case "J":
			return "000010";
		case "JAL":
			return "000011";
		}
		return "";
	}
	
	static String IOP_2_rt(String name)
	{
		switch(name)
		{
		case "BGEZ":
			return "00001";
		case "BGTZ":
		case "BLEZ":
		case "BLTZ":
			return "00000";
		case "BGEZAL":
			return "10001";
		case "BLTZAL":
			return "10000";
		}
		return "";
	}
	
	static String toBinaryString(int number,int bit)
	{
		if(number >= 0)
		{
			String temp = Integer.toBinaryString(number);
			int size = bit-temp.length();
			for(int i = 0 ; i < size ; i++)
				temp = "0"+temp;
			return temp;
		}
		else
			return Integer.toBinaryString(number).substring(32-bit);
	}
}

class IDENT
{
	public String name;
	
	public IDENT(String name)
	{
		this.name = name;
	}
}

class DECNUM
{
	public int number;
	
	public DECNUM(String number)
	{
		this.number = Integer.parseInt(number);
	}
}

class HEXNUM
{
	public int number;
	
	public HEXNUM(String number)
	{
		this.number = parse(number);
	}
	
	private int parse(String s) throws NumberFormatException
	{
		if(!(s.startsWith("0x") || s.startsWith("0X")))
			throw new NumberFormatException();
		int number=0,n=0;
		for(int i=2;i<s.length();i++)
		{
			char c=s.charAt(i);
			switch(c)
			{
				case '1':
					n=1;
					break;
				case '2':
					n=2;
					break;
				case '3':
					n=3;
					break;
				case '4':
					n=4;
					break;
				case '5':
					n=5;
					break;
				case '6':
					n=6;
					break;
				case '7':
					n=7;
					break;
				case '8':
					n=8;
					break;
				case '9':
					n=9;
					break;
				case '0':
					n=0;
					break;
				case 'a':
				case 'A':
					n=10;
					break;
				case 'b':
				case 'B':
					n=11;
					break;
				case 'c':
				case 'C':
					n=12;
					break;
				case 'd':
				case 'D':
					n=13;
					break;
				case 'e':
				case 'E':
					n=14;
					break;
				case 'f':
				case 'F':
					n=15;
					break;
				default:
					throw new NumberFormatException();
			}
			number=number*16+n;
		}
		return number;
	}
}

class int_literal
{
	public int lexval;
	
	public int_literal(DECNUM dec)
	{
		this.lexval = dec.number;
	}
	
	public int_literal(HEXNUM hex)
	{
		this.lexval = hex.number;
	}
}

class value
{
	public int lexval;
	
	public value(int_literal intl)
	{
		this.lexval = intl.lexval;
	}
	
	public value()
	{
		this.lexval = 0;
	}
}

class values
{
	public Vector<Integer> lexval;
	
	public values(value v)
	{
		this.lexval = new Vector<Integer>();
		this.lexval.add(v.lexval);
	}
	
	public values(value v,values va)
	{
		this.lexval = new Vector<Integer>();
		for(int i = 0 ; i < va.lexval.size() ; i++)
			this.lexval.add(va.lexval.get(i));
		this.lexval.add(v.lexval);
	}
}

class type_spec
{
	public String type;
	public int size;
	public Vector<Integer> lexval;
	
	public type_spec(values va,String type)
	{
		this.lexval = new Vector<Integer>();
		this.type = type;
		this.size = va.lexval.size();
		for(int i = 0 ; i < va.lexval.size() ; i++)
			this.lexval.add(va.lexval.get(i));
	}
}

class data
{
	public dataSeq data;
	
	public data(type_spec type,IDENT id)
	{
		data = new dataSeq(id.name,type.type,type.size,type.lexval);
	}
}

class datas
{
	Vector<dataSeq> datas;
	
	public datas(data d)
	{
		this.datas = new Vector<dataSeq>();
		this.datas.add(d.data);
	}
	
	public datas(data d,datas da)
	{
		this.datas = new Vector<dataSeq>();
		for(int i = 0 ; i < da.datas.size() ; i++)
			this.datas.add(da.datas.get(i));
		this.datas.add(d.data);
	}
}

class data_seg
{
	String name;
	Vector<dataSeq> datas;
	
	public data_seg(datas da)
	{
		datas = new Vector<dataSeq>();
		this.name = "";
		this.datas.addAll(da.datas);
	}
	
	public data_seg(datas da,IDENT id)
	{
		datas = new Vector<dataSeq>();
		this.name = id.name;
		this.datas.addAll(da.datas);
	}
}

class code_body
{
	public Vector<String> code;
	
	public code_body(Register reg3,Register reg2,Register reg1,Operation op)
	{
		code = new Vector<String>();
		if(op.type.equals("ROP_3"))
		{
			String temp = mipsAttribute.OP_op(op.name)+mipsAttribute.registerCalculation(reg2.name)+mipsAttribute.registerCalculation(reg3.name)+mipsAttribute.registerCalculation(reg1.name)+"00000"+mipsAttribute.ROP_func(op.name);
			code.add(temp);
		}
		else if(op.type.equals("ROP_2"))
		{
			String temp = mipsAttribute.OP_op(op.name)+mipsAttribute.registerCalculation(reg1.name)+mipsAttribute.registerCalculation(reg2.name)+"0000000000"+mipsAttribute.ROP_func(op.name);
			code.add(temp);
		}
		else if(op.type.equals("ROP_1"))
		{
			if(op.name.equals("MTHI") || op.name.equals("MTLO"))
			{
				String temp = mipsAttribute.OP_op(op.name)+mipsAttribute.registerCalculation(reg1.name)+"000000000000000"+mipsAttribute.ROP_func(op.name);
				code.add(temp);
			}
			else
			{
				String temp = mipsAttribute.OP_op(op.name)+"0000000000"+mipsAttribute.registerCalculation(reg1.name)+"00000"+mipsAttribute.ROP_func(op.name);
				code.add(temp);
			}
		}
		else if(op.type.equals("ROP_0"))
		{
			if(op.name.equals("ERET"))
			{
				String temp = mipsAttribute.OP_op(op.name)+"10000000000000000000"+mipsAttribute.ROP_func(op.name);
				code.add(temp);
			}
			else
			{
				String temp = mipsAttribute.OP_op(op.name)+"code"+mipsAttribute.ROP_func(op.name);
				code.add(temp);
			}
		}
		else if(op.type.equals("JOP_2"))
		{
			String temp = mipsAttribute.OP_op(op.name)+mipsAttribute.registerCalculation(reg2.name)+"00000"+mipsAttribute.registerCalculation(reg1.name)+"00000"+mipsAttribute.ROP_func(op.name);
			code.add(temp);
		}
		else if(op.type.equals("JOP_1"))
		{
			String temp = mipsAttribute.OP_op(op.name)+mipsAttribute.registerCalculation(reg1.name)+"000000000000000"+mipsAttribute.ROP_func(op.name);
			code.add(temp);
		}
		else if(op.type.equals("HOP_1"))
		{
			if(op.name.equals("PUSH"))
			{
				String temp = mipsAttribute.OP_op("SW")+mipsAttribute.registerCalculation(reg1.name)+mipsAttribute.registerCalculation("$sp")+"0000000000000000";
				code.add(new String(temp));
				temp = mipsAttribute.OP_op("ADDI")+mipsAttribute.registerCalculation("$sp")+mipsAttribute.registerCalculation("$sp")+mipsAttribute.toBinaryString(4,16);
				code.add(temp);
			}
			else
			{
				String temp = mipsAttribute.OP_op("ADDI")+mipsAttribute.registerCalculation("$sp")+mipsAttribute.registerCalculation("$sp")+mipsAttribute.toBinaryString(-4,16);
				code.add(new String(temp));
				temp = mipsAttribute.OP_op("LW")+mipsAttribute.registerCalculation(reg1.name)+mipsAttribute.registerCalculation("$sp")+"0000000000000000";
				code.add(temp);
			}
		}
	}
	
	public code_body(int_literal intl,Register reg2,Register reg1,Operation op)
	{
		code = new Vector<String>();
		if(op.type.equals("IOP_3"))
		{
			if(op.name.equals("MFC0") || op.name.equals("MTC0"))
			{
				if(intl.lexval < 4)
				{
					if(op.name.equals("MFC0"))
					{
						String temp = mipsAttribute.OP_op(op.name)+"00000"+mipsAttribute.registerCalculation(reg1.name)+mipsAttribute.registerCalculation(reg2.name)+"00000000"+mipsAttribute.toBinaryString(intl.lexval,2);
						code.add(temp);
					}
					else
					{
						String temp = mipsAttribute.OP_op(op.name)+"00100"+mipsAttribute.registerCalculation(reg1.name)+mipsAttribute.registerCalculation(reg2.name)+"00000000"+mipsAttribute.toBinaryString(intl.lexval,2);
						code.add(temp);
					}
				}
				else
				{
					System.out.println("不合法的中断选择");
					System.exit(0);
				}
			}
			else if(op.name.equals("SLL") || op.name.equals("SRL") || op.name.equals("SRA"))
			{
				if(intl.lexval < 32 && intl.lexval >= -32)
				{
					String temp = mipsAttribute.OP_op(op.name)+"00000"+mipsAttribute.registerCalculation(reg1.name)+mipsAttribute.registerCalculation(reg2.name)+"00000"+mipsAttribute.ROP_func(op.name);
					code.add(temp);
				}
				else
				{
					System.out.println("移位数值超出范围");
					System.exit(0);
				}
			}
			else
			{
				if(intl.lexval >= -32768 && intl.lexval < 32768)
				{
					String temp = mipsAttribute.OP_op(op.name)+mipsAttribute.registerCalculation(reg2.name)+mipsAttribute.registerCalculation(reg1.name)+mipsAttribute.toBinaryString(intl.lexval,16);
					code.add(temp);
				}
				else
				{
					System.out.println("立即数数值超出范围");
					System.exit(0);
				}
			}
		}
		else if(op.type.equals("IOP_2"))
		{
			if(intl.lexval < -32768 || intl.lexval >= 32768)
			{
				System.out.println("立即数数值超出范围");
				System.exit(0);
			}
			
			if(op.name.equals("LUI"))
			{
				String temp = mipsAttribute.OP_op(op.name)+"00000"+mipsAttribute.registerCalculation(reg1.name)+mipsAttribute.toBinaryString(intl.lexval,16);
				code.add(temp);
			}
			else
			{
				String temp = mipsAttribute.OP_op(op.name)+mipsAttribute.registerCalculation(reg1.name)+mipsAttribute.IOP_2_rt(op.name)+mipsAttribute.toBinaryString(intl.lexval,16);
				code.add(temp);
			}
		}
		else if(op.type.equals("JOP_1"))
		{
			String temp = mipsAttribute.OP_op(op.name)+mipsAttribute.toBinaryString(intl.lexval, 26);
			code.add(temp);
		}
	}
	
	public code_body(Register reg2,int_literal intl,Register reg1,Operation op)
	{
		code = new Vector<String>();
		String temp = mipsAttribute.OP_op(op.name)+mipsAttribute.registerCalculation(reg2.name)+mipsAttribute.registerCalculation(reg1.name)+mipsAttribute.toBinaryString(intl.lexval,16);
		code.add(temp);
	}
	
	public code_body(IDENT id,Register reg,Operation op)
	{
		code = new Vector<String>();
		String temp = mipsAttribute.OP_op(op.name)+mipsAttribute.registerCalculation(reg.name)+id.name;
		code.add(temp);
	}
	
	public code_body(IDENT id,Register reg2,Register reg1,Operation op)
	{
		code = new Vector<String>();
		if(op.type.equals("JOP_1"))
		{
			String temp = mipsAttribute.OP_op(op.name)+id.name;
			code.add(temp);
		}
		else
		{
			String temp = mipsAttribute.OP_op(op.name)+mipsAttribute.registerCalculation(reg2.name)+mipsAttribute.registerCalculation(reg1.name)+id.name;
			code.add(temp);
		}
	}
}

class code
{
	public Vector<codeSeq> code;
	
	public code(code_body cb)
	{
		code = new Vector<codeSeq>();
		for(int i = 0 ; i < cb.code.size() ; i++)
		{
			codeSeq newCode = new codeSeq();
			newCode.setCode(cb.code.get(i));
			code.add(newCode);
		}
		
	}
	
	public code(code_body cb,IDENT id)
	{
		code = new Vector<codeSeq>();
		for(int i = 0 ; i < cb.code.size() ; i++)
		{
			codeSeq newCode = new codeSeq();
			newCode.setCode(cb.code.get(i));
			if(i == 0)
				newCode.setAddress(id.name);
			code.add(newCode);
		}
	}
}

class codes
{
	public Vector<codeSeq> code;
	
	public codes(code c)
	{
		code = new Vector<codeSeq>();
		code.addAll(c.code);
	}
	
	public codes(code c,codes co)
	{
		code = new Vector<codeSeq>();
		this.code.addAll(co.code);
		this.code.addAll(c.code);
	}
}

class code_seg
{
	public String name;
	public Vector<codeSeq> codes;
	
	public code_seg(codes co)
	{
		codes = new Vector<codeSeq>();
		this.name = "";
		this.codes.addAll(co.code);
	}
	
	public code_seg(codes co,IDENT id)
	{
		codes = new Vector<codeSeq>();
		this.name = id.name;
		this.codes.addAll(co.code);
	}
}
