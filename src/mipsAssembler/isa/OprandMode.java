package mipsAssembler.isa;

public enum OprandMode {
	R_RS_RT_RD(OprandType.REGISTER,OprandType.REGISTER,OprandType.REGISTER),
	R_RS_RT(OprandType.REGISTER,OprandType.REGISTER),
	R_RD(OprandType.REGISTER),
	R_RS(OprandType.REGISTER),
	R_CP0(OprandType.REGISTER,OprandType.REGISTER,OprandType.IMMEDIATE),
	R_RT_RD_SHAMT(OprandType.REGISTER,OprandType.REGISTER,OprandType.SHIFT_AMOUNT),
	R_RS_RD(OprandType.REGISTER,OprandType.REGISTER),
	R_CODE(OprandType.CODE),
	I_RS_RT_IMME(OprandType.REGISTER,OprandType.REGISTER,OprandType.IMMEDIATE),
	I_RT_IMME(OprandType.REGISTER,OprandType.IMMEDIATE),
	I_RS_RT_VARI(OprandType.REGISTER,OprandType.VARIABLE,OprandType.REGISTER),
	I_RS_RT_OFFSET(OprandType.REGISTER,OprandType.OFFSET,OprandType.REGISTER),
	I_RT_RS_OFFSET(OprandType.REGISTER,OprandType.REGISTER,OprandType.OFFSET),
	I_RS_OFFSET(OprandType.REGISTER,OprandType.OFFSET),
	J_ADDR(OprandType.ADDRESS);
	


	
	public OprandType[] types;
	OprandMode(OprandType op0) {
		// TODO Auto-generated constructor stub
		types = new OprandType[]{op0};
	}
	OprandMode(OprandType op0,OprandType op1) {
		// TODO Auto-generated constructor stub
		types = new OprandType[]{op0,op1};
	}
	OprandMode(OprandType op0,OprandType op1,OprandType op2) {
		// TODO Auto-generated constructor stub
		types = new OprandType[]{op0,op1,op2};
	}
	
	public static OprandMode getOprandModeByTypes(OprandType[] types) throws Exception{
		for(OprandMode mode:values()){
			OprandType[] defaultType = mode.types;
			if(defaultType.length!=types.length){
				continue;
			}else{
				boolean flag = false;
				for(int i=0;i<types.length;i++){
					if(types[i]==defaultType[i]||types[i]==OprandType.DEFAULT){
						if(i==types.length-1)
							flag = true;
						else continue;
					}else break;
				}
				if(flag)
					return mode;
				else continue;
			}
		}
		throw new Exception("操作数类型有错");
	}
	public static boolean isInstanceOf(OprandType[] types, OprandMode mode) {
		// TODO Auto-generated method stub
		if(types.length!=mode.types.length)
			return false;
		OprandType[] defaultType = mode.types;
		for(int i=0;i<types.length;i++){
			if(types[i]==defaultType[i]||types[i]==OprandType.DEFAULT){
				continue;
			}else return false;
		}
		return true;
	}
}
