package compiler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

import javax.swing.SwingUtilities;

import GUI.MainFrame;
import utilities.DataUtil;

public class translation {
	myYacc my;
	myLex ml;
	int tips = 0;
	public translation(String []code) throws Exception
	{
		my = new myYacc();
		ml = new myLex();
		
		String []lexTranslation = ml.LexAnalysis(code);
		tips =1;
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
		
		boolean judge = my.YaccAnalysis(lexTranslation,regulation);
		tips = 2;
		System.out.println("Judge:"+judge);
		if(judge == true)
		{
			semanticAnalysis sem = new semanticAnalysis(my.producers,ml.VariSignary,ml.ConsSignary);
			Vector<String> result = sem.Analysis(code, regulation, lexTranslation);
			tips = 3;
			printResult(result);
//			for(sNode node:attributeDefinition.VariSignary){
//				System.out.println(node.actionScope+"\t"+node.morpheme+"\t"+
//			node.name+"\t"+node.property+"\t"+node.size+"\t"+node.type);
//			}
		}else{
			DataUtil.mainframe.textPane.setText("”Ô∑®∑÷Œˆ”–ŒÛ");
		}
	}
	
	public void printResult(Vector<String> temp)
	{
		try {
			FileWriter fileWriter = new FileWriter(new File(DataUtil.currentFileName.replace(".c", ".temp")));
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
	public myLex getLex(){
		return ml;
	}
	public int getTips(){
		return tips;
	}
}
