package mipsAssembler.isa;

public enum OprandType {
	REGISTER,
	IMMEDIATE,
	OFFSET,
	SHIFT_AMOUNT,
	ADDRESS,
	CODE,
	VARIABLE,
	DEFAULT,//�ǼĴ������Ǳ�������������
	
	NULL;//�գ����п�����
	
	private static final String REGISTER_REGEX = "\\$\\w+";
	private static final String NUMBER_REGEX = "\\d+";
	private static final String VARIABLE_REGEX = "([a-zA-Z][0-9a-zA-Z\\.\\_\\$]*)";
	public static OprandType identifyOprand(String oprand) throws Exception{
		if(oprand.matches(REGISTER_REGEX)){
			return REGISTER;
		}else if(oprand.matches(VARIABLE_REGEX)){
			return VARIABLE;
		}else if(oprand.matches(NUMBER_REGEX)){
			return DEFAULT;
		}else throw new Exception("�޷�ʶ����������ͣ�"+oprand);
	}
	
	public static void main(String[] args) {
		System.out.println("main_b".matches(VARIABLE_REGEX));
	}
}
