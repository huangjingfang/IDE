package compiler;

public class VariDesc {
	public String name; //参数名
	public String morpheme; //类型（常量或变量）
	public String type; //类型（数组或变量）
	public String property; //性质（参数、变量或值）
	public int size; //大小，变量大小为1个字节，数组大小为n个字节
	public String actionScope; //作用域
	
	public VariDesc(String name,String morpheme)
	{
		this.name = name;
		this.morpheme = morpheme;
	}
	
	public VariDesc(VariDesc node,String _name){
		name = _name;
		morpheme = node.morpheme;
		type = node.type;
		property = node.property;
		size = node.size;
		actionScope = node.actionScope;
	}
	public VariDesc(sNode node){
		name = node.name;
		morpheme = node.morpheme;
		type = node.type;
		property = node.property;
		size = node.size;
		actionScope = node.actionScope;
	}
	
	public VariDesc(String name,String morpheme,String type,String property,int size,String actionScope)
	{
		this.name = name;
		this.morpheme = morpheme;
		this.type = type;
		this.property = property;
		this.size = size;
		this.actionScope = actionScope;
	}
	
	public void set(String type,String property,int size,String actionScope)
	{
		this.type = type;
		this.property = property;
		this.size = size;
		this.actionScope = actionScope;
	}
}
