package compiler;

public class test {
	public static void main(String[] args) {
		String temp = "int number ; int c ; void main ( void ) { int a ; int b ; a = 1 ; b = 2 ; c = a + b ; if ( a == b ) a = debug ( ) ; else { a = 4 ; b = 5 ; } return ; }";
		String []ss = temp.split(" ");
		new translation(ss);
	}
}
