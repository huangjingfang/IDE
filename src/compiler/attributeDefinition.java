package compiler;

import java.util.Vector;

public class attributeDefinition 
{
	public Vector<String> code;
	static Vector<String> data = new Vector<String>();
	static int temp = 0;
	static int seg = 0;
	static String next = "";
	static boolean judge = false;
	
	public attributeDefinition()
	{
		code = new Vector<String>();
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

class type_spec extends attributeDefinition
{
	public String type;
	public int width;
	
	public type_spec(String type)
	{
		super();
		this.type = type;
		if(type.equals("INT"))
			this.width = 4;
		else if(type.equals("VOID"))
			this.width = 0;
	}
}

class int_literal extends attributeDefinition
{
	public int lexval;
	
	public int_literal(int lexval)
	{
		super();
		this.lexval = lexval;
		this.code.add(Integer.toString(lexval));
	}
	
	public int_literal(DECNUM dec)
	{
		super();
		this.lexval = dec.number;
		this.code.add(String.valueOf(this.lexval));
	}
	
	public int_literal(HEXNUM hex)
	{
		super();
		this.lexval = hex.number;
		this.code.add(Integer.toString(lexval));
	}
}

class expr extends attributeDefinition
{
	public String place;
	public String True;
	public String False;
	
	public expr(String place)
	{
		super();
		this.place = place;
	}
	
	public expr(String True,String False)
	{
		super();
		this.True = True;
		this.False = False;
	}
	
	public void changeTrue(String true1)
	{
		for(int i = 0 ; i < this.code.size() ; i++)
			this.code.set(i, this.code.get(i).replaceAll(True, true1));
		
		True = true1;
	}
	
	public void changeFalse(String false1)
	{
		for(int i = 0 ; i < this.code.size() ; i++)
			this.code.set(i, this.code.get(i).replaceAll(False, false1));
		
		False = false1;
	}
	
	public String getTrue() {
		return True;
	}

	public void setTrue(String true1) {
		for(int i = 0 ; i < this.code.size() ; i++)
			this.code.set(i, this.code.get(i).replaceAll("offsetTrue", true1));
		
		True = true1;
	}

	public String getFalse() {
		return False;
	}

	public void setFalse(String false1) {
		for(int i = 0 ; i < this.code.size() ; i++)
			this.code.set(i, this.code.get(i).replaceAll("offsetFalse", false1));
		
		False = false1;
	}
	
	public void setPlace(String place)
	{
		this.place = place;
		String []ss = this.code.firstElement().split(":");
		ss[0] = place;
		this.code.set(0, ss[0]+":"+ss[1]);
	}

	public expr(expr ex2,expr ex1,String op)
	{
		super();
		if(op.equals("OR"))
		{
			this.place = ex1.place;
			ex1.setFalse(ex2.place);
			for(int i = 0 ; i < ex1.code.size() ; i++)
				this.code.add(ex1.code.get(i));
			for(int i = 0 ; i < ex2.code.size() ; i++)
				this.code.add(ex2.code.get(i));
		}
		else if(op.equals("AND"))
		{
			this.place = ex1.place;
			ex1.setTrue(ex2.place);
			for(int i = 0 ; i < ex1.code.size() ; i++)
				this.code.add(ex1.code.get(i));
			for(int i = 0 ; i < ex2.code.size() ; i++)
				this.code.add(ex2.code.get(i));
		}
		else if(op.equals("EQ"))
		{
			String now = "s"+Integer.toString(seg++);
			this.place = now;
			if(ex1.code.size() > 1)
			{
				this.code.add(now+":"+ex1.code.get(0));
				for(int i = 1 ; i < ex1.code.size()-1 ; i++)
					this.code.add(ex1.code.get(i));
			}
			for(int i = 0 ; i < ex2.code.size()-1 ; i++)
				this.code.add(ex2.code.get(i));
			this.code.add("\tBEQ "+ex1.code.lastElement()+","+ex2.code.lastElement()+","+"offsetTrue");
			this.code.add("\tJ offsetFalse,-,-");
		}
		else if(op.equals("NE"))
		{
			String now = "s"+Integer.toString(seg++);
			this.place = now;
			if(ex1.code.size() > 1)
			{
				this.code.add(now+":"+ex1.code.get(0));
				for(int i = 1 ; i < ex1.code.size()-1 ; i++)
					this.code.add(ex1.code.get(i));
			}
			for(int i = 0 ; i < ex2.code.size()-1 ; i++)
				this.code.add(ex2.code.get(i));
			this.code.add("\tBNE "+ex1.code.lastElement()+","+ex2.code.lastElement()+","+"offsetTrue");
			this.code.add("\tJ offsetFalse,-,-");
		}
		else if(op.equals("LE"))
		{
			String now = "s"+Integer.toString(seg++);
			this.place = now;
			if(ex1.code.size() > 1)
			{
				this.code.add(now+":"+ex1.code.get(0));
				for(int i = 1 ; i < ex1.code.size()-1 ; i++)
					this.code.add(ex1.code.get(i));
			}
			for(int i = 0 ; i < ex2.code.size()-1 ; i++)
				this.code.add(ex2.code.get(i));
			this.code.add("\t- "+"t"+Integer.toString(temp)+","+ex1.code.lastElement()+","+ex2.code.lastElement());
			this.code.add("\tBLEZ "+"t"+Integer.toString(temp++)+",offsetTrue,-");
			this.code.add("\tJ offsetFalse,-,-");
		}
		else if(op.equals("GE"))
		{
			String now = "s"+Integer.toString(seg++);
			this.place = now;
			if(ex1.code.size() > 1)
			{
				this.code.add(now+":"+ex1.code.get(0));
				for(int i = 1 ; i < ex1.code.size()-1 ; i++)
					this.code.add(ex1.code.get(i));
			}
			for(int i = 0 ; i < ex2.code.size()-1 ; i++)
				this.code.add(ex2.code.get(i));
			this.code.add("\t- "+"t"+Integer.toString(temp)+","+ex1.code.lastElement()+","+ex2.code.lastElement());
			this.code.add("\tBGEZ "+"t"+Integer.toString(temp++)+",offsetTrue,-");
			this.code.add("\tJ offsetFalse,-,-");
		}
		else if(op.equals("<"))
		{
			String now = "s"+Integer.toString(seg++);
			this.place = now;
			if(ex1.code.size() > 1)
			{
				this.code.add(now+":"+ex1.code.get(0));
				for(int i = 1 ; i < ex1.code.size()-1 ; i++)
					this.code.add(ex1.code.get(i));
			}
			for(int i = 0 ; i < ex2.code.size()-1 ; i++)
				this.code.add(ex2.code.get(i));
			this.code.add("\t- "+"t"+Integer.toString(temp)+","+ex1.code.lastElement()+","+ex2.code.lastElement());
			this.code.add("\tBLTZ "+"t"+Integer.toString(temp++)+",offsetTrue,-");
			this.code.add("\tJ offsetFalse,-,-");
		}
		else if(op.equals(">"))
		{
			String now = "s"+Integer.toString(seg++);
			this.place = now;
			if(ex1.code.size() > 1)
			{
				this.code.add(now+":"+ex1.code.get(0));
				for(int i = 1 ; i < ex1.code.size()-1 ; i++)
					this.code.add(ex1.code.get(i));
			}
			for(int i = 0 ; i < ex2.code.size()-1 ; i++)
				this.code.add(ex2.code.get(i));
			this.code.add("\t- "+"t"+Integer.toString(temp)+","+ex1.code.lastElement()+","+ex2.code.lastElement());
			this.code.add("\tBGTZ "+"t"+Integer.toString(temp++)+",offsetTrue,-");
			this.code.add("\tJ offsetFalse,-,-");
		}
		else
		{
			for(int i = 0 ; i < ex1.code.size()-1 ; i++)
				this.code.add(new String(ex1.code.get(i)));
			for(int i = 0 ; i < ex2.code.size()-1 ; i++)
				this.code.add(new String(ex2.code.get(i)));
			this.code.add("\t"+op+" t"+temp+","+ex1.code.lastElement()+","+ex2.code.lastElement());
			this.code.add("t"+(temp++));
		}
	}
	
	public expr(expr ex,String op)
	{
		super();
		if(op.equals("+") || op.equals("-"))
		{
			for(int i = 0 ; i < ex.code.size()-1 ; i++)
				this.code.add(new String(ex.code.get(i)));
			this.code.add("\t"+op+" t"+temp+",0,"+ex.code.lastElement());
			this.code.add("t"+(temp++));
		}
		else if(op.equals("$") || op.equals("~"))
		{
			for(int i = 0 ; i < ex.code.size()-1 ; i++)
				this.code.add(new String(ex.code.get(i)));
			this.code.add("\t"+op+" t"+temp+",-,"+ex.code.lastElement());
			this.code.add("t"+(temp++));
		}
		else if(op.equals("!"))
		{
			String trueTemp = ex.False;
			String falseTemp = ex.True;
			ex.changeTrue(trueTemp);
			ex.changeFalse(falseTemp);
		}
	}
	
	public expr(expr ex)
	{
		super();
		for(int i = 0 ; i < ex.code.size() ; i++)
			this.code.add(new String(ex.code.get(i)));
		this.True = ex.True;
		this.False = ex.False;
		this.place = ex.place;
	}
	
	public expr(int_literal inl)
	{
		super();
		this.code.add("\t= t"+temp+",-,"+inl.lexval);
		this.code.add("t"+(temp++));
	}
	
	public expr(IDENT id)
	{
		super();
		this.code.add("\t= t"+temp+",-,"+id.name);
		this.code.add("t"+(temp++));
	}
	
	public expr(expr ex,IDENT id)
	{
		super();
		for(int i = 0 ; i < ex.code.size()-1 ; i++)
			this.code.add(ex.code.get(i));
		this.code.add("\t=[] t"+temp+","+ex.code.lastElement()+","+id.name);
		this.code.add("t"+(temp++));
	}
	
	public expr(args arg,IDENT id)
	{
		super();
		for(int i = 0 ; i < arg.code.size() ; i++)
			this.code.add(arg.code.get(i));
		this.code.add("\tCALL -,-,"+id.name);
		this.code.add("\tPOP -,-,t"+temp++);
		for(int i = 0 ; i < arg.code.size() ; i++)
			this.code.add("\tPOP -,-,t"+temp);
		this.code.add("\t= t"+temp+",-,t"+(temp-1));
		this.code.add("t"+(temp++));
	}
}

class expr_stmt extends attributeDefinition
{
	public expr_stmt(expr ex,IDENT id)
	{
		super();
		for(int i = 0 ; i < ex.code.size()-1 ; i++)
			this.code.add(ex.code.get(i));
		this.code.add("\t= "+id.name+",-,"+ex.code.lastElement());
	}
	
	public expr_stmt(expr ex2,expr ex1,IDENT id)
	{
		super();
		for(int i = 0 ; i < ex1.code.size()-1 ; i++)
			this.code.add(ex1.code.get(i));
		for(int i = 0 ; i < ex2.code.size()-1 ; i++)
			this.code.add(ex2.code.get(i));
		this.code.add("\t=[] "+id.name+","+ex1.code.lastElement()+","+ex2.code.lastElement());
	}
	
	public expr_stmt(expr ex2,expr ex1)
	{
		super();
		for(int i = 0 ; i < ex1.code.size()-1 ; i++)
			this.code.add(ex1.code.get(i));
		for(int i = 0 ; i < ex2.code.size()-1 ; i++)
			this.code.add(ex2.code.get(i));
		this.code.add("\t$= -,"+ex1.code.lastElement()+","+ex2.code.lastElement());
	}
	
	public expr_stmt(args arg,IDENT id)
	{
		super();
		for(int i = 0 ; i < arg.code.size() ; i++)
			this.code.add(arg.code.get(i));
		this.code.add("\tCALL -,-,"+id.name);
		for(int i = 0 ; i < arg.code.size() ; i++)
		{
			if(i == 0)
				temp = temp + 1;
			this.code.add("\tPOP -,-,t"+temp);
		}
	}
}

class if_stmt extends attributeDefinition
{
	public String next;
	
	public if_stmt(stmt ifstmt,expr temp)
	{
		super();
		String next = "s"+Integer.toString(seg++);
		String begin = "s"+Integer.toString(seg++);
		this.next = next;
		
		if(judge == true)
		{
			temp.setPlace(next);
			judge = false;
		}
		else
			temp.code.set(0, temp.code.get(0).replaceAll(temp.place+":", ""));
		
		if(ifstmt.code.size() > 0)
		{
			String []ss = ifstmt.code.firstElement().split(":");
			if(ss.length == 2)
			{
				temp.code.set(0, ss[0]+":"+temp.code.get(0));
				ifstmt.code.set(0, ss[1]);
			}
		}
		
		temp.setTrue(begin);
		temp.setFalse(next);;
		ifstmt.next = next;
		if(ifstmt.code.size() > 0)
		{
			for(int i = 0 ; i < temp.code.size() ; i++)
				this.code.add(new String(temp.code.get(i)));
			this.code.add(begin+":"+ifstmt.code.get(0));
		}
		else 
		{
			temp.changeTrue(next);
			for(int i = 0 ; i < temp.code.size() ; i++)
				this.code.add(new String(temp.code.get(i)));
		}
		for(int i = 1 ; i < ifstmt.code.size() ; i++)
			this.code.add(new String(ifstmt.code.get(i)));
		
		judge = true;
		attributeDefinition.next = this.next;
	}
	
	public if_stmt(stmt ifstmt2,stmt ifstmt1,expr temp)
	{
		super();
		String begin = "s"+Integer.toString(seg++);
		String begin2 = "s"+Integer.toString(seg++);
		String next = "s"+Integer.toString(seg++);
		this.next = next;
		
		temp.code.set(0, temp.code.get(0).replaceAll(temp.place+":", ""));
		
		if(judge == true)
		{
			temp.setPlace(next);
			judge = false;
		}
		
		if(ifstmt1.code.size() > 0)
		{
			String []ss = ifstmt1.code.firstElement().split(":");
			if(ss.length == 2)
			{
				temp.code.set(0, ss[0]+":"+temp.code.get(0));
				ifstmt1.code.set(0, ss[1]);
			}
		}
		
		temp.setTrue(begin);
		temp.setFalse(begin2);
		ifstmt1.next = next;
		ifstmt2.next = next;
		
		if(ifstmt2.code.size() <= 0)
			temp.changeFalse(next);
		if(ifstmt1.code.size() > 0)
		{
			for(int i = 0 ; i < temp.code.size() ; i++)
				this.code.add(new String(temp.code.get(i)));
			this.code.add(begin+":"+ifstmt1.code.get(0));
			for(int i = 1 ; i < ifstmt1.code.size() ; i++)
				this.code.add(new String(ifstmt1.code.get(i)));
			this.code.add("\tJ "+this.next+",-,-");
		}
		else 
		{
			temp.changeTrue(next);
			for(int i = 0 ; i < temp.code.size() ; i++)
				this.code.add(new String(temp.code.get(i)));
		}
		
		if(ifstmt2.code.size() > 0)
		{
			this.code.add(begin2+": "+ifstmt2.code.get(0));
			for(int i = 1 ; i < ifstmt2.code.size() ; i++)
				this.code.add(new String(ifstmt2.code.get(i)));
			this.code.add("\tJ "+this.next+",-,-");
		}
		
		judge = true;
		attributeDefinition.next = this.next;
		
	}
}

class while_stmt extends attributeDefinition
{
	public String begin;
	public String next;
	
	public while_stmt(stmt whilestmt,expr temp)
	{
		super();
		String next = "s"+Integer.toString(seg++);
		String begin = "s"+Integer.toString(seg++);
		this.next = next;
		
		if(judge == true)
		{
			judge = false;
			
			for(int i = 0 ; i < whilestmt.code.size() ; i++)
				whilestmt.code.set(i, whilestmt.code.get(i).replaceAll(whilestmt.next, temp.place));
		}
		
		if(whilestmt.code.size() > 0)
		{
			String []ss = whilestmt.code.firstElement().split(":");
			if(ss.length == 2)
			{
				temp.setPlace(ss[0]);
				whilestmt.code.set(0, ss[1]);
			}
		}
		
		for(int i = 0 ; i < whilestmt.code.size() ; i++)
		{
			whilestmt.code.set(i, whilestmt.code.get(i).replaceAll("offsetBreak", next));
			whilestmt.code.set(i, whilestmt.code.get(i).replaceAll("offsetContinue", temp.place));
		}
		
		temp.setTrue(begin);
		temp.setFalse(next);;
		whilestmt.next = next;
		if(whilestmt.code.size() > 0)
		{
			for(int i = 0 ; i < temp.code.size() ; i++)
				this.code.add(new String(temp.code.get(i)));
			this.code.add(begin+": "+whilestmt.code.get(0));
			for(int i = 1 ; i < whilestmt.code.size() ; i++)
				this.code.add(new String(whilestmt.code.get(i)));
			this.code.add("\tJ "+temp.place+",-,-");
		}
		else 
		{
			temp.changeTrue(temp.place);
			for(int i = 0 ; i < temp.code.size() ; i++)
				this.code.add(new String(temp.code.get(i)));
		}
		
		judge = true;
		attributeDefinition.next = this.next;
	}
}

class break_stmt extends attributeDefinition
{
	public String next;
	
	public break_stmt()
	{
		super();
		this.code.add("\tJ offsetBreak,-,-");
	}
}

class continue_stmt extends attributeDefinition
{
	public String next;
	
	public continue_stmt()
	{
		super();
		this.code.add("\tJ offsetContinue,-,-");
	}
}

class args extends attributeDefinition
{
	public args()
	{
	}
	
	public args(arg_list list)
	{
		super();
		for(int i = 0 ; i < list.code.size() ; i++)
			this.code.add(new String(list.code.get(i)));
	}
}

class arg_list extends attributeDefinition
{
	public arg_list(expr ex)
	{
		super();
		for(int i = 0 ; i < ex.code.size()-1 ; i++)
			this.code.add(new String(ex.code.get(i)));
		this.code.add("\tPUSH " + ex.code.lastElement() + ",-,-");
	}
	
	public arg_list(expr ex,arg_list list)
	{
		super();
		for(int i = 0 ; i < list.code.size() ; i++)
			this.code.add(new String(list.code.get(i)));
		for(int i = 0 ; i < ex.code.size()-1 ; i++)
			this.code.add(new String(ex.code.get(i)));
		this.code.add("\tPUSH " + ex.code.lastElement() + ",-,-");
	}
}

class return_stmt extends attributeDefinition
{
	public String next;
	
	public return_stmt()
	{
		super();
		this.code.add("\tJal -,-,-");
	}
	
	public return_stmt(expr ex)
	{
		super();
		for(int i = 0 ; i < ex.code.size()-1 ; i++)
			this.code.add(new String(ex.code.get(i)));
		this.code.add("\tPUSH"+ex.code.lastElement()+",-,-");
		this.code.add("\tJal -,-,-");
	}
}

class stmt extends attributeDefinition
{
	public String next;
	
	public stmt(expr_stmt estmt)
	{
		super();
		if(judge == true)
		{
			if(estmt.code.firstElement().split(":").length == 1)
			{
				this.code.add(attributeDefinition.next+":"+estmt.code.firstElement());
				judge = false;
			}
			else
				this.code.add(estmt.code.firstElement().replaceAll(estmt.code.firstElement().split(":")[0], attributeDefinition.next));
			
			for(int i = 1 ; i < estmt.code.size() ; i++)
				this.code.add(new String(estmt.code.get(i)));
		}
		else
		{
			for(int i = 0 ; i < estmt.code.size() ; i++)
				this.code.add(new String(estmt.code.get(i)));
		}
	}
	
	public stmt(if_stmt istmt)
	{
		super();
		this.next = istmt.next;
		for(int i = 0 ; i < istmt.code.size() ; i++)
			this.code.add(new String(istmt.code.get(i)));
	}
	
	public stmt(while_stmt wstmt)
	{
		super();
		this.next = wstmt.next;
		for(int i = 0 ; i < wstmt.code.size() ; i++)
			this.code.add(new String(wstmt.code.get(i)));
	}
	
	public stmt(return_stmt rstmt)
	{
		super();
		if(judge == true)
		{
			this.code.add(attributeDefinition.next+":"+rstmt.code.firstElement());
			judge = false;
			for(int i = 1 ; i < rstmt.code.size() ; i++)
				this.code.add(new String(rstmt.code.get(i)));
		}
		else
		{
			for(int i = 0 ; i < rstmt.code.size() ; i++)
				this.code.add(new String(rstmt.code.get(i)));
		}
	}
	
	public stmt(continue_stmt cstmt)
	{
		super();
		if(judge == true)
		{
			this.code.add(attributeDefinition.next+":"+cstmt.code.firstElement());
			judge = false;
			for(int i = 1 ; i < cstmt.code.size() ; i++)
				this.code.add(new String(cstmt.code.get(i)));
		}
		else
		{
			for(int i = 0 ; i < cstmt.code.size() ; i++)
				this.code.add(new String(cstmt.code.get(i)));
		}
	}
	
	public stmt(break_stmt bstmt)
	{
		super();
		if(judge == true)
		{
			this.code.add(attributeDefinition.next+":"+bstmt.code.firstElement());
			judge = false;
			for(int i = 1 ; i < bstmt.code.size() ; i++)
				this.code.add(new String(bstmt.code.get(i)));
		}
		else
		{
			for(int i = 0 ; i < bstmt.code.size() ; i++)
				this.code.add(new String(bstmt.code.get(i)));
		}
	}
	
	public stmt(block_stmt bstmt)
	{
		super();
		this.next = bstmt.next;
		for(int i = 0 ; i < bstmt.code.size() ; i++)
			this.code.add(new String(bstmt.code.get(i)));
	}
}

class block_stmt extends attributeDefinition
{
	public String next;
	public block_stmt(stmt_list sstmt)
	{
		super();
		this.next = sstmt.next;
		for(int i = 0 ; i < sstmt.code.size() ; i++)
			this.code.add(new String(sstmt.code.get(i)));
	}
}

class stmt_list extends attributeDefinition
{
	public String next;
	public stmt_list()
	{
		super();
	}
	
	public stmt_list(stmt sstmt)
	{
		super();
		this.next = sstmt.next;
		for(int i = 0 ; i < sstmt.code.size() ; i++)
			this.code.add(new String(sstmt.code.get(i)));
	}
	
	public stmt_list(stmt sstmt,stmt_list stmtl)
	{
		super();
		this.next = sstmt.next;
		for(int i = 0 ; i < stmtl.code.size() ; i++)
			this.code.add(new String(stmtl.code.get(i)));
		
		String []ss = stmtl.code.lastElement().split(" ");
		if(ss[ss.length-1].equals(":"))
		{
			this.code.set(this.code.size()-1, this.code.lastElement()+" "+sstmt.code.get(0));
			for(int i = 1 ; i < sstmt.code.size() ; i++)
				this.code.add(new String(sstmt.code.get(i)));
		}
		else
		{
			for(int i = 0 ; i < sstmt.code.size() ; i++)
				this.code.add(new String(sstmt.code.get(i)));
		}
	}
}

class local_decl extends attributeDefinition
{
	public local_decl(IDENT id,type_spec type)
	{
		super();
		data.add("\t"+id.name+":\t.BYTE\t?");
	}
	
	public local_decl(int_literal intl,IDENT id,type_spec type)
	{
		super();
		String temp = "\t"+id.name +":\t.BYTE";
		for(int i = 0 ; i < intl.lexval ; i++)
			temp = temp + "\t?";
		data.add(temp);
	}
}

class compound extends attributeDefinition
{
	public compound(stmt_list sstmt)
	{
		super();
		for(int i = 0 ; i < sstmt.code.size() ; i++)
			this.code.add(new String(sstmt.code.get(i)));
	}
	
	public compound()
	{
		super();
	}
}

class compound_stmt extends attributeDefinition
{
	public compound_stmt(compound com)
	{
		super();
		for(int i = 0 ; i < com.code.size() ; i++)
			this.code.add(new String(com.code.get(i)));
	}
}

class param extends attributeDefinition
{
	
}

class param_list extends attributeDefinition
{
	
}

class params extends attributeDefinition
{
	
}

class FUNCTION_IDENT extends attributeDefinition
{
	public FUNCTION_IDENT(IDENT id)
	{
		super();
		this.code.add(id.name);
	}
}

class fun_decl extends attributeDefinition
{
	public fun_decl(compound_stmt cstmt,params pparam,FUNCTION_IDENT id,type_spec type)
	{
		super();
		this.code.add(id.code.get(0)+":");
		for(int i = 0 ; i < cstmt.code.size() ; i++)
			this.code.add(new String(cstmt.code.get(i)));
	}
	
	public fun_decl()
	{
		super();
	}
}

class var_decl extends attributeDefinition
{
	public var_decl(IDENT id,type_spec type)
	{
		super();
		data.add("\t"+id.name+":\t.BYTE\t?");
	}
	
	public var_decl(int_literal intl,IDENT id,type_spec type)
	{
		super();
		String temp = "\t"+id.name +":\t.BYTE";
		for(int i = 0 ; i < intl.lexval ; i++)
			temp = temp + "\t?";
		data.add(temp);
	}
}

class decl extends attributeDefinition
{
	public decl()
	{
		super();
	}
	
	public decl(fun_decl fun)
	{
		super();
		for(int i = 0 ; i < fun.code.size() ; i++)
			this.code.add(new String(fun.code.get(i)));
	}
}

class decl_list extends attributeDefinition
{
	public decl_list(decl dec)
	{
		super();
		for(int i = 0 ; i < dec.code.size() ; i++)
			this.code.add(new String(dec.code.get(i)));
	}
	
	public decl_list(decl dec,decl_list decll)
	{
		super();
		for(int i = 0 ; i < decll.code.size() ; i++)
			this.code.add(new String(decll.code.get(i)));
		for(int i = 0 ; i < dec.code.size() ; i++)
			this.code.add(new String(dec.code.get(i)));
	}
}

class program extends attributeDefinition
{
	public program(decl_list decll)
	{
		super();
		this.code.add(".DATA");
		for(int i = 0 ; i < data.size() ; i++)
			this.code.add(new String(data.get(i)));
		this.code.add(".CODE");
		for(int i = 0 ; i < decll.code.size() ; i++)
			this.code.add(new String(decll.code.get(i)));
	}
}