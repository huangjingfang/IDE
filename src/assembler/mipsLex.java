package assembler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayDeque;
import java.util.Deque;

public class mipsLex {
	int [][]TABLE;
	int []STATE_PATTERN = {-1,-1,61,61,61,61,61,61,61,61,61,61,59,59,59,59,18,59,59,59,59,59,59,59,61,59,55,56,39,40,59,59,59,53,59,59,59,59,59,54,51,52,62,47,49,50,48,41,43,44,46,45,42,59,59,59,57,58,37,38,60,59,36,59,59,59,59,34,35,59,59,59,32,31,33,59,59,59,30,29,28,23,25,59,27,22,26,24,59,21,19,20,59,59,17,59,15,16,59,59,59,59,59,59,59,14,13,59,59,11,59,12,10,59,59,9,7,59,8,6,59,59,4,5,0,1,3,2,-1,61,59};
	
	public mipsLex()
	{
		TABLE = new int[131][];
		for(int i = 0 ; i < 131 ; i++)
			TABLE[i] = new int[128];
	
		File f = new File("TABLE.data");
		
		try {
			RandomAccessFile raf = new RandomAccessFile(f, "r");
			
			long ptr = 0;
			String str = "";
			int index = 0;
			while (ptr < f.length() && index < 131) 
			{
				str = raf.readLine();
				ptr = raf.getFilePointer();
				
				if(!str.equals(""))
				{
					String[] ss = str.split(",");
					for(int i = 0 ; i < ss.length ; i++)
						TABLE[index][i] = Integer.parseInt(ss[i]);
					
					++index;
				}
			}
			raf.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String LexSynthesis(String temp)
	{
		if(temp.equals(".DATA") || temp.equals(".CODE") || temp.equals(".SPACE") || temp.equals(".BYTE") || temp.equals(".WORD") || temp.equals(".HALF") || temp.equals("[") || temp.equals("]") || temp.equals("?") || temp.equals(":") || temp.equals("(") || temp.equals(")") || temp.equals(",") || temp.equals("#"))
			return temp;
		else
			return seuLex(temp);
	}
	
	public String seuLex(String temp)
	{
		String seuLexLastLex;
		int currentState = 0;
		int matchedState = 0;
		int currentLength = 0;
		int matchedLength = 0;
		seuLexLastLex = temp;
		int index = 0;
		char c;
		Deque<Character> q = new ArrayDeque<Character>();
		while ( currentState!=-1 && index<seuLexLastLex.length() )
		{
			c = temp.charAt(index++);
			q.push(c);
			currentLength++;
			currentState = TABLE[currentState][c];
			if ( STATE_PATTERN[currentState] != -1 )
			{
				matchedState = currentState;
				matchedLength = currentLength;
			}
		}
		if ( matchedLength>0 )
		{
			while ( currentLength>matchedLength )
			{
				q.removeLast();
				currentLength--;
			}
			while ( !q.isEmpty() )
			{
				seuLexLastLex += q.getFirst();
				q.pop();
			}
			switch ( STATE_PATTERN[matchedState] )
			{
			    case 0:
				return "ROP_3"; 
				case 1:
				return "IOP_3"; 
				case 2:
				return "IOP_3";  
				case 3:
				return "ROP_3";
				case 4:
				return "ROP_3";
				case 5:
				return "IOP_3";
				case 6:
				return "IOP_3";
				case 7:
				return "IOP_2";
				case 8:
				return "IOP_2"; 
				case 9:
				return "IOP_2";
				case 10:
				return "IOP_2";
				case 11:
				return "IOP_2";
				case 12:
				return "IOP_2";
				case 13:
				return "IOP_3";
				case 14:
				return "ROP_0"; 
				case 15:
				return "ROP_2"; 
				case 16:
				return "ROP_2";  
				case 17:
				return "ROP_0";
				case 18:
				return "JOP_1";
				case 19:
				return "JOP_1";
				case 20:
				return "JOP_2";
				case 21:
				return "JOP_1";
				case 22:
				return "IOP_2"; 
				case 23:
				return "LOAD";
				case 24:
				return "LOAD";
				case 25:
				return "LOAD";
				case 26:
				return "LOAD";
				case 27:
				return "LOAD";
				case 28:
				return "IOP_3"; 
				case 29:
				return "ROP_1"; 
				case 30:
				return "ROP_1";  
				case 31:
				return "ROP_1";
				case 32:
				return "ROP_1";
				case 33:
				return "IOP_3";
				case 34:
				return "ROP_2";
				case 35:
				return "ROP_2";
				case 36:
				return "ROP_3"; 
				case 37:
				return "ROP_3";
				case 38:
				return "IOP_3";
				case 39:
				return "STORE";
				case 40:
				return "STORE";
				case 41:
				return "IOP_3";
				case 42:
				return "ROP_3"; 
				case 43:
				return "ROP_3"; 
				case 44:
				return "IOP_3";  
				case 45:
				return "IOP_3";
				case 46:
				return "ROP_3";
				case 47:
				return "IOP_3";
				case 48:
				return "ROP_3";
				case 49:
				return "IOP_3";
				case 50:
				return "ROP_3"; 
				case 51:
				return "ROP_3";
				case 52:
				return "ROP_3";
				case 53:
				return "STORE";
				case 54:
				return "ROP_0";
				case 55:
				return "ROP_3";
				case 56:
				return "IOP_3";
				case 57:
				return "HOP_1";
				case 58:
				return "HOP_1";
				case 59:
				return "IDENT";
				case 60:
				return "HEXNUM"; 
				case 61:
				return "DECNUM";
				case 62:
				return "REGISTER";
				default:
				assert(false);
			}
		}
		else 
		{
			return "";
		}
		return "";
	}
	
	public String[] LexAnalysis(String []ss)
	{
		String []result = new String[ss.length];
		for(int i = 0 ; i < ss.length ; i++)
			result[i] = LexSynthesis(ss[i]);
		return result;
	}
}
