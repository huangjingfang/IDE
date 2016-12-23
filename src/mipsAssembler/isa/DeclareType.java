package mipsAssembler.isa;

public enum DeclareType {
	DATA("data"),
	TEXT("text"),
	BYTE("byte"),
	HALF("half"),
	WORD("word"),
	FLOAT("float"),
	DOUBLE("double"),
	ASCII("ascii"),
	ASCIIZ("asciiz"),
	SPACE("space");
	
	private String name;
	private DeclareType(String name) {
		// TODO Auto-generated constructor stub
		this.name = name;
	}
	
	public static DeclareType getDeclareTypeByName(String name){
		System.out.println(name);
		for(DeclareType t:values()){
			if(name.equalsIgnoreCase(t.name)){
				return t;
			}
		}
		return null;
	}
}
