package assembler;

import java.util.Vector;

public class mipsTranslation {
	public mipsTranslation(String []code)
	{
		mipsLex ml = new mipsLex();
		mipsYacc my = new mipsYacc();
		Vector<Integer> regulation = new Vector<Integer>();
		String []strTemp = new String[code.length+1];
		for(int i = 0 ; i < code.length ; i++)
			strTemp[i] = code[i];
		strTemp[code.length] = "#";
		code = strTemp;
		
		String []lex = ml.LexAnalysis(code);
		for(String s:code){
			System.out.print(s+"\t");
		}
		my.YaccAnalysis(lex, regulation);
		mipsSemantic ms = new mipsSemantic(my.producers);
		ms.Analysis(code, regulation, lex);
	}
}
