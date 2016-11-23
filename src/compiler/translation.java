package compiler;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

public class translation {
	public translation(String []code)
	{
		myYacc my = new myYacc();
		myLex ml = new myLex();
		
		String []lexTranslation = ml.LexAnalysis(code);
		
		String []temp = new String[code.length+1];
		for(int i = 0 ; i < code.length ; i++)
			temp[i] = code[i];
		temp[code.length] = "#";
		code = temp;
		
		temp = new String[lexTranslation.length+1];
		for(int i = 0 ; i < lexTranslation.length ; i++)
			temp[i] = lexTranslation[i];
		
		temp[lexTranslation.length] = "#";
		lexTranslation = temp;
		
		Vector<Integer> regulation = new Vector<Integer>();
		
		for(int i = 0 ; i < lexTranslation.length-1 ; i++)
		{
			if((lexTranslation[i] == "{" && lexTranslation[i+1] == "}") || (lexTranslation[i] == "(" && lexTranslation[i+1] == ")"))
			{
				String []s = new String[lexTranslation.length+1];
				String []ss = new String[code.length+1];
				for(int j = 0 ; j <= i ; j++)
				{
					s[j] = lexTranslation[j];
					ss[j] = code[j];
				}
				s[i+1] = "#";
				ss[i+1] = "#";
				for(int j = i+1 ; j < lexTranslation.length ; j++)
				{
					s[j+1] = lexTranslation[j];
					ss[j+1] = code[j];
				}
				lexTranslation = s;
				code = ss;
			}
		}
		
		my.YaccAnalysis(lexTranslation,regulation);
		
		semanticAnalysis sem = new semanticAnalysis(my.producers);
		Vector<String> result = sem.Analysis(code, regulation, lexTranslation);
		printResult(result);
	}
	
	public void printResult(Vector<String> temp)
	{
		try {
			FileWriter fileWriter = new FileWriter("IntermediateCode.data");
			for(int i = 0 ; i < temp.size() ; i++)
			{
				if(temp.get(i).equals(".DATA") || temp.get(i).equals(".CODE"))
					fileWriter.write(temp.get(i));
				else
					fileWriter.write(temp.get(i));
				fileWriter.write("\r\n");
			}
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
