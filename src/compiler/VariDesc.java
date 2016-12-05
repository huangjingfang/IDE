package compiler;

public class VariDesc {
	public String name; //������
	public String morpheme; //���ͣ������������
	public String type; //���ͣ�����������
	public String property; //���ʣ�������������ֵ��
	public int size; //��С��������СΪ1���ֽڣ������СΪn���ֽ�
	public String actionScope; //������
	
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
