package utilities;

public class Quaternary {

	public String op;
	public String arg0;
	public String arg1;
	public String result;
	
	public Quaternary(String op,String arg0,String arg1,String result) {
		// TODO Auto-generated constructor stub
		this.op = op;
		this.arg0 = arg0;
		this.arg1 = arg1;
		this.result = result;
	}
	
	//将该条指令翻译成指令码
	public String genCode(){
		int[] addr = BackEndStruct.getReg(this);
		
		return null;
	}
	
	
}
