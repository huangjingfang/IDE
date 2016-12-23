package mipsAssembler.i_type;

public abstract class InsType {
	public static final int OP0 = 0b000000;
	public static final int BZ = 0b000001;
	public static final String CPO = "CP0";
	public static final String ERET = "ERET";
	public static final String R_TYPE = "R_TYPE";
	public static final String I_TYPE = "I_TYPE";
	public static final String J_TYPE = "J_TYPE";
	
	abstract public String getType();
}
