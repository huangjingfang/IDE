package mipsAssembler.isa;

public enum OprandType {
	REGISTER,
	IMMEDIATE,
	OFFSET,
	SHIFT_AMOUNT,
	ADDRESS,
	CODE,
	VARIABLE,
	DEFAULT,//非寄存器、非变量的所有数字
	
	NULL;//空，所有可能性
	
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
		}else throw new Exception("无法识别操作数类型："+oprand);
	}
	
	public static void main(String[] args) {
		System.out.println("main_b".matches(VARIABLE_REGEX));
	}
}
