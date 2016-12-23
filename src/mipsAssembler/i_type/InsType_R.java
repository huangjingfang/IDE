package mipsAssembler.i_type;

import mipsAssembler.utils.Utils;

public class InsType_R extends InsType {
	int op;
	int func;
	int rs;
	int rt;
	int rd;
	int shamt;
	public InsType_R(int op,int func) {
		// TODO Auto-generated constructor stub
		this.op = op;
		this.func = func;
	}
	public InsType_R(int op,int data,String extra) {
		// TODO Auto-generated constructor stub
		this.op = op;
		switch (extra) {
		case CPO:
			rs = data;
			break;
		case ERET:
			func = data;
		default:
			break;
		}
	}
	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return R_TYPE;
	}
	public String getOp(){
		return Utils.format(Integer.toBinaryString(op), 6);
	}
	public String getFunc(){
		return Utils.format(Integer.toBinaryString(func), 6);
	}
	public String getRs(){
		return Utils.format(Integer.toBinaryString(rs), 5);
	}
	
}
