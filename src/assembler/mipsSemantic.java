package assembler;

import java.util.Stack;
import java.util.Vector;

public class mipsSemantic {
	public Vector<producer> producers;
	private Stack<Object> nonter;
	public Vector<segment> dataAndCode;
	
	public mipsSemantic(Vector<producer> producers)
	{
		this.producers = producers;
		this.nonter = new Stack<Object>();
		dataAndCode = new Vector<segment>();
	}
	
	public void Analysis(String []code,Vector<Integer> action,String []lex)
	{
		int index = 0;
		for(int i = 0 ; i < action.size() ; i++)
		{
			if(action.get(i) == -1)
			{
				if(lex[index].equals("IDENT"))
					nonter.push(new IDENT(code[index]));
				else if(lex[index].equals("DECNUM"))
					nonter.push(new DECNUM(code[index]));
				else if(lex[index].equals("HEXNUM"))
					nonter.push(new HEXNUM(code[index]));
				else if(lex[index].equals("REGISTER"))
					nonter.push(new Register(code[index]));
				else if(lex[index].equals("ROP_3") || lex[index].equals("ROP_2") || lex[index].equals("ROP_1") || lex[index].equals("ROP_0") || lex[index].equals("IOP_3") || lex[index].equals("IOP_2") || lex[index].equals("JOP_2") || lex[index].equals("JOP_1") || lex[index].equals("HOP_1"))
					nonter.push(new Operation(lex[index],code[index]));
				index = index + 1;
			}
			else
				regulation(action.get(i));
		}
	}
	
	public void regulation(int Serial)
	{
		switch(Serial)
		{
		case 0:
			break;
		case 1:
			break;
		case 2:
			break;
		case 3:
			break;
		case 4:
			break;
		case 5:
			break;
		case 6:
			code_seg cos = new code_seg((codes)nonter.pop(),(IDENT)nonter.pop());
			findSegment(cos.name).code.addAll(cos.codes);
			break;
		case 7:
			code_seg cos2 = new code_seg((codes)nonter.pop());
			findSegment("Global").code.addAll(cos2.codes);
			break;
		case 8:
			codes co = new codes((code)nonter.pop(),(codes)nonter.pop());
			nonter.push(co);
			break;
		case 9:
			codes co2 = new codes((code)nonter.pop());
			nonter.push(co2);
			break;
		case 10:
			code c = new code((code_body)nonter.pop(),(IDENT)nonter.pop());
			nonter.push(c);
			break;
		case 11:
			code c2 = new code((code_body)nonter.pop());
			nonter.push(c2);
			break;
		case 12:
			code_body cb = new code_body((Register)nonter.pop(),(Register)nonter.pop(),(Register)nonter.pop(),(Operation)nonter.pop());
			nonter.push(cb);
			break;
		case 13:
			code_body cb2 = new code_body(new Register(),(Register)nonter.pop(),(Register)nonter.pop(),(Operation)nonter.pop());
			nonter.push(cb2);
			break;
		case 14:
			code_body cb3 = new code_body(new Register(),new Register(),(Register)nonter.pop(),(Operation)nonter.pop());
			nonter.push(cb3);
			break;
		case 15:
			code_body cb4 = new code_body(new Register(),new Register(),new Register(),(Operation)nonter.pop());
			nonter.push(cb4);
			break;
		case 16:
			code_body cb5 = new code_body((int_literal)nonter.pop(),(Register)nonter.pop(),(Register)nonter.pop(),(Operation)nonter.pop());
			nonter.push(cb5);
			break;
		case 17:
			code_body cb6 = new code_body((IDENT)nonter.pop(),(Register)nonter.pop(),(Register)nonter.pop(),(Operation)nonter.pop());
			nonter.push(cb6);
			break;
		case 18:
			code_body cb7 = new code_body((IDENT)nonter.pop(),new Register(),(Register)nonter.pop(),(Operation)nonter.pop());
			nonter.push(cb7);
			break;
		case 19:
			code_body cb8 = new code_body(new Register(),(Register)nonter.pop(),(Register)nonter.pop(),(Operation)nonter.pop());
			nonter.push(cb8);
			break;
		case 20:
			code_body cb9 = new code_body(new Register(),new Register(),(Register)nonter.pop(),(Operation)nonter.pop());
			nonter.push(cb9);
			break;
		case 21:
			code_body cb10 = new code_body((int_literal)nonter.pop(),new Register(),new Register(),(Operation)nonter.pop());
			nonter.push(cb10);
			break;
		case 22:
			code_body cb11 = new code_body((IDENT)nonter.pop(),new Register(),new Register(),(Operation)nonter.pop());
			nonter.push(cb11);
			break;
		case 23:
			code_body cb12 = new code_body((IDENT)nonter.pop(),(Register)nonter.pop(),(Operation)nonter.pop());
			nonter.push(cb12);
			break;
		case 24:
			code_body cb13 = new code_body((Register)nonter.pop(),(int_literal)nonter.pop(),(Register)nonter.pop(),(Operation)nonter.pop());
			nonter.push(cb13);
			break;
		case 25:
			code_body cb14 = new code_body((IDENT)nonter.pop(),(Register)nonter.pop(),(Operation)nonter.pop());
			nonter.push(cb14);
			break;
		case 26:
			code_body cb15 = new code_body((Register)nonter.pop(),(int_literal)nonter.pop(),(Register)nonter.pop(),(Operation)nonter.pop());
			nonter.push(cb15);
			break;
		case 27:
			code_body cb16 = new code_body(new Register(),new Register(),(Register)nonter.pop(),(Operation)nonter.pop());
			nonter.push(cb16);
			break;
		case 28:
			data_seg das = new data_seg((datas)nonter.pop(),(IDENT)nonter.pop());
			findSegment(das.name).data.addAll(das.datas);
			break;
		case 29:
			data_seg das2 = new data_seg((datas)nonter.pop());
			findSegment("Global").data.addAll(das2.datas);
			break;
		case 30:
			datas da = new datas((data)nonter.pop(),(datas)nonter.pop());
			nonter.push(da);
			break;
		case 31:
			datas da2 = new datas((data)nonter.pop());
			nonter.push(da2);
			break;
		case 32:
			data d = new data((type_spec)nonter.pop(),(IDENT)nonter.pop());
			nonter.push(d);
			break;
		case 33:
			type_spec ts = new type_spec((values)nonter.pop(),".BYTE");
			nonter.push(ts);
			break;
		case 34:
			type_spec ts2 = new type_spec((values)nonter.pop(),".WORD");
			nonter.push(ts2);
			break;
		case 35:
			type_spec ts3 = new type_spec((values)nonter.pop(),".HALF");
			nonter.push(ts3);
			break;
		case 36:
			type_spec ts4 = new type_spec((values)nonter.pop(),".SPACE");
			nonter.push(ts4);
			break;
		case 37:
			values va = new values((value)nonter.pop(),(values)nonter.pop());
			nonter.push(va);
			break;
		case 38:
			values va2 = new values((value)nonter.pop());
			nonter.push(va2);
			break;
		case 39:
			value v = new value((int_literal)nonter.pop());
			nonter.push(v);
			break;
		case 40:
			value v2 = new value();
			nonter.push(v2);
			break;
		case 41:
			int_literal intl = new int_literal((DECNUM)nonter.pop());
			nonter.push(intl);
			break;
		case 42:
			int_literal intl2 = new int_literal((HEXNUM)nonter.pop());
			nonter.push(intl2);
			break;
		default:
			break;
		}
	}
	
	private segment findSegment(String name)
	{
		for(int i = 0 ; i < dataAndCode.size() ; i++)
		{
			if(dataAndCode.get(i).name.equals(name))
				return dataAndCode.get(i);
		}
		dataAndCode.add(new segment(name));
		return dataAndCode.lastElement();
	}
}

class segment
{
	public String name;
	public Vector<dataSeq> data;
	public Vector<codeSeq> code;
	
	public segment(String name)
	{
		this.data = new Vector<dataSeq>();
		this.code = new Vector<codeSeq>();
		this.name = name;
	}
}

class dataSeq
{
	public String name;
	public String type;
	public int size;
	public Vector<Integer> value;
	
	public dataSeq(String name,String type,int size,Vector<Integer> value)
	{
		value = new Vector<Integer>();
		this.name = name;
		this.type = type;
		this.size = size;
		for(int i = 0 ; i < value.size() ; i++)
			this.value.add(value.get(i));
	}
}

class codeSeq
{
	private String address;
	private String code;
	
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	public codeSeq()
	{
		address = "";
		code = "";
	}
}

class Operation
{
	public String type;
	public String name;
	
	public Operation(String type,String name)
	{
		this.type = type;
		this.name = name;
	}
}

class Register
{
	public String name;
	
	public Register()
	{
		
	}
	
	public Register(String name)
	{
		this.name = name;
	}
}
