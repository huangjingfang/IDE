package compiler;

import java.util.Stack;
import java.util.Vector;

public class semanticAnalysis 
{
	public Vector<producer> producers;
	private Stack<Object> nonter;
	
	public semanticAnalysis(Vector<producer> producers)
	{
		this.producers = producers;
		this.nonter = new Stack<Object>();
		attributeDefinition.data.clear();
	}
	
	public Vector<String> Analysis(String []code,Vector<Integer> action,String []lex)
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
				index = index + 1;
			}
			else
				regulation(action.get(i));
		}
		
		return ((attributeDefinition)nonter.pop()).code;
	}
	
	public void regulation(int Serial)
	{
		switch(Serial)
		{
		case 0:
			break;
		case 1:
			program pro = new program((decl_list)nonter.pop());
			nonter.push(pro);
			break;
		case 2:
			decl_list decll = new decl_list((decl)nonter.pop(),(decl_list)nonter.pop());
			nonter.push(decll);
			break;
		case 3:
			decl_list decll2 = new decl_list((decl)nonter.pop());
			nonter.push(decll2);
			break;
		case 4:
			decl ddecl = new decl();
			nonter.push(ddecl);
			break;
		case 5:
			decl ddecl2 = new decl((fun_decl)nonter.pop());
			nonter.push(ddecl2);
			break;
		case 6:
			new var_decl((IDENT)nonter.pop(),(type_spec)nonter.pop());
			break;
		case 7:
			new var_decl((int_literal)nonter.pop(),(IDENT)nonter.pop(),(type_spec)nonter.pop());
			break;
		case 8:
			type_spec type = new type_spec("INT");
			nonter.push(type);
			break;
		case 9:
			type_spec type2 = new type_spec("VOID");
			nonter.push(type2);
			break;
		case 10:
			fun_decl fun = new fun_decl((compound_stmt)nonter.pop(),(params)nonter.pop(),(FUNCTION_IDENT)nonter.pop(),(type_spec)nonter.pop());
			nonter.push(fun);
			break;
		case 11:
			nonter.pop();
			nonter.pop();
			nonter.pop();
			nonter.push(new fun_decl());
			break;
		case 12:
			FUNCTION_IDENT id = new FUNCTION_IDENT((IDENT)nonter.pop());
			nonter.push(id);
			break;
		case 13:
			nonter.push(new params());
			break;
		case 14:
			nonter.push(new params());
			break;
		case 15:
			break;
		case 16:
			break;
		case 17:
			nonter.pop();
			nonter.pop();
			break;
		case 18:
			nonter.pop();
			nonter.pop();
			nonter.pop();
			break;
		case 19:
			compound_stmt cstmt = new compound_stmt((compound)nonter.pop());
			nonter.push(cstmt);
			break;
		case 20:
		case 21:
			compound com = new compound((stmt_list)nonter.pop());
			nonter.push(com);
			break;
		case 22:
			compound com2 = new compound();
			nonter.push(com2);
			break;
		case 23:
			break;
		case 24:
			break;
		case 25:
			new local_decl((IDENT)nonter.pop(),(type_spec)nonter.pop());
			break;
		case 26:
			new local_decl((int_literal)nonter.pop(),(IDENT)nonter.pop(),(type_spec)nonter.pop());
			break;
		case 27:
			stmt_list stmtl = new stmt_list((stmt)nonter.pop(),(stmt_list)nonter.pop());
			nonter.push(stmtl);
			break;
		case 28:
			stmt_list stmtl2 = new stmt_list((stmt)nonter.pop());
			nonter.push(stmtl2);
			break;
		case 29:
			stmt_list stmtl3 = new stmt_list();
			nonter.push(stmtl3);
			break;
		case 30:
			stmt sstmt = new stmt((expr_stmt)nonter.pop());
			nonter.push(sstmt);
			break;
		case 31:
			stmt sstmt2 = new stmt((block_stmt)nonter.pop());
			nonter.push(sstmt2);
			break;
		case 32:
			stmt sstmt3 = new stmt((if_stmt)nonter.pop());
			nonter.push(sstmt3);
			break;
		case 33:
			stmt sstmt4 = new stmt((while_stmt)nonter.pop());
			nonter.push(sstmt4);
			break;
		case 34:
			stmt sstmt5 = new stmt((return_stmt)nonter.pop());
			nonter.push(sstmt5);
			break;
		case 35:
			stmt sstmt6 = new stmt((continue_stmt)nonter.pop());
			nonter.push(sstmt6);
			break;
		case 36:
			stmt sstmt7 = new stmt((break_stmt)nonter.pop());
			nonter.push(sstmt7);
			break;
		case 37:
			expr_stmt estmt = new expr_stmt((expr)nonter.pop(),(IDENT)nonter.pop());
			nonter.push(estmt);
			break;
		case 38:
			expr_stmt estmt2 = new expr_stmt((expr)nonter.pop(),(expr)nonter.pop(),(IDENT)nonter.pop());
			nonter.push(estmt2);
			break;
		case 39:
			expr_stmt estmt3 = new expr_stmt((expr)nonter.pop(),(expr)nonter.pop());
			nonter.push(estmt3);
			break;
		case 40:
			expr_stmt estmt4 = new expr_stmt((args)nonter.pop(),(IDENT)nonter.pop());
			nonter.push(estmt4);
			break;
		case 41:
			while_stmt wstmt = new while_stmt((stmt)nonter.pop(),(expr)nonter.pop());
			nonter.push(wstmt);
			break;
		case 42:
			break;
		case 43:
			block_stmt bstmt = new block_stmt((stmt_list)nonter.pop());
			nonter.push(bstmt);
			break;
		case 44:
			if_stmt istmt = new if_stmt((stmt)nonter.pop(),(expr)nonter.pop());
			nonter.push(istmt);
			break;
		case 45:
			if_stmt istmt2 = new if_stmt((stmt)nonter.pop(),(stmt)nonter.pop(),(expr)nonter.pop());
			nonter.push(istmt2);
			break;
		case 46:
			return_stmt rstmt = new return_stmt();
			nonter.push(rstmt);
			break;
		case 47:
			return_stmt rstmt2 = new return_stmt((expr)nonter.pop());
			nonter.push(rstmt2);
			break;
		case 48:
			expr ex = new expr((expr)nonter.pop(),(expr)nonter.pop(),"OR");
			nonter.push(ex);
			break;
		case 49:
			expr ex2 = new expr((expr)nonter.pop(),(expr)nonter.pop(),"EQ");
			nonter.push(ex2);
			break;
		case 50:
			expr ex3 = new expr((expr)nonter.pop(),(expr)nonter.pop(),"NE");
			nonter.push(ex3);
			break;
		case 51:
			expr ex4 = new expr((expr)nonter.pop(),(expr)nonter.pop(),"LE");
			nonter.push(ex4);
			break;
		case 52:
			expr ex5 = new expr((expr)nonter.pop(),(expr)nonter.pop(),"<");
			nonter.push(ex5);
			break;
		case 53:
			expr ex6 = new expr((expr)nonter.pop(),(expr)nonter.pop(),"GE");
			nonter.push(ex6);
			break;
		case 54:
			expr ex7 = new expr((expr)nonter.pop(),(expr)nonter.pop(),">");
			nonter.push(ex7);
			break;
		case 55:
			expr ex8 = new expr((expr)nonter.pop(),(expr)nonter.pop(),"AND");
			nonter.push(ex8);
			break;
		case 56:
			expr ex9 = new expr((expr)nonter.pop(),(expr)nonter.pop(),"+");
			nonter.push(ex9);
			break;
		case 57:
			expr ex10 = new expr((expr)nonter.pop(),(expr)nonter.pop(),"-");
			nonter.push(ex10);
			break;
		case 58:
			expr ex11 = new expr((expr)nonter.pop(),(expr)nonter.pop(),"*");
			nonter.push(ex11);
			break;
		case 59:
			expr ex12 = new expr((expr)nonter.pop(),(expr)nonter.pop(),"/");
			nonter.push(ex12);
			break;
		case 60:
			expr ex13 = new expr((expr)nonter.pop(),(expr)nonter.pop(),"%");
			nonter.push(ex13);
			break;
		case 61:
			expr ex14 = new expr((expr)nonter.pop(),"!");
			nonter.push(ex14);
			break;
		case 62:
			expr ex15 = new expr((expr)nonter.pop(),"-");
			nonter.push(ex15);
			break;
		case 63:
			expr ex16 = new expr((expr)nonter.pop(),"+");
			nonter.push(ex16);
			break;
		case 64:
			expr ex17 = new expr((expr)nonter.pop(),"$");
			nonter.push(ex17);
			break;
		case 65:
			expr ex18 = new expr((expr)nonter.pop());
			nonter.push(ex18);
			break;
		case 66:
			expr ex19 = new expr((IDENT)nonter.pop());
			nonter.push(ex19);
			break;
		case 67:
			expr ex20 = new expr((expr)nonter.pop(),(IDENT)nonter.pop());
			nonter.push(ex20);
			break;
		case 68:
			expr ex21 = new expr((args)nonter.pop(),(IDENT)nonter.pop());
			nonter.push(ex21);
			break;
		case 69:
			expr ex22 = new expr((int_literal)nonter.pop());
			nonter.push(ex22);
			break;
		case 70:
			expr ex23 = new expr((expr)nonter.pop(),(expr)nonter.pop(),"&");
			nonter.push(ex23);
			break;
		case 71:
			expr ex24 = new expr((expr)nonter.pop(),(expr)nonter.pop(),"^");
			nonter.push(ex24);
			break;
		case 72:
			expr ex25 = new expr((expr)nonter.pop(),"~");
			nonter.push(ex25);
			break;
		case 73:
			expr ex26 = new expr((expr)nonter.pop(),(expr)nonter.pop(),"LSHIFT");
			nonter.push(ex26);
			break;
		case 74:
			expr ex27 = new expr((expr)nonter.pop(),(expr)nonter.pop(),"RSHIFT");
			nonter.push(ex27);
			break;
		case 75:
			expr ex28 = new expr((expr)nonter.pop(),(expr)nonter.pop(),"|");
			nonter.push(ex28);
			break;
		case 76:
			int_literal intl = new int_literal((DECNUM)nonter.pop());
			nonter.push(intl);
			break;
		case 77:
			int_literal intl2 = new int_literal((HEXNUM)nonter.pop());
			nonter.push(intl2);
			break;
		case 78:
			arg_list argl = new arg_list((expr)nonter.pop(),(arg_list)nonter.pop());
			nonter.push(argl);
			break;
		case 79:
			arg_list argl2 = new arg_list((expr)nonter.pop());
			nonter.push(argl2);
			break;
		case 80:
			args ar = new args((arg_list)nonter.pop());
			nonter.push(ar);
			break;
		case 81:
			args ar2 = new args();
			nonter.push(ar2);
			break;
		case 82:
			continue_stmt constmt = new continue_stmt();
			nonter.push(constmt);
			break;
		case 83:
			break_stmt brestmt = new break_stmt();
			nonter.push(brestmt);
			break;
		default:
			break;
		}
	}
}
