package mipsAssembler.isa;

public enum OprandsMatcher {
	R_RS_RT_RD(),
	R_RS_RT(),
	R_RD(),
	R_RS(),
	R_CP0(),
	R_RT_RD_SHAMT(),
	R_RS_RD(),
	R_CODE(),
	I_RS_RT_IMME(),
	I_RT_IMME(),
	I_RS_RT_OFFSET(),//offset����Ϊlabel
	I_RS_OFFSET(),//offset����Ϊlabel
	J_ADDR(); //addr����Ϊlabel

}
