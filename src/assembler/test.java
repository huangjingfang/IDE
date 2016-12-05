package assembler;

public class test {
	public static void main(String[] args) {
		String temp = ".DATA NUMBER : .BYTE ? c : .WORD ? ? 4 .CODE [ main ] main : DIV $1 , $2 MFLO $at PUSH $t0 POP $31 JR $ra J main";
		String []ss = temp.split(" ");
		new mipsTranslation(ss);
	}
}
