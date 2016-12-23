package mipsAssembler.i_type;

import mipsAssembler.utils.Utils;

public class InsType_I extends InsType{
	int op;
	int rs;
	int rt;
	int immediate;
	public InsType_I(int op) {
		// TODO Auto-generated constructor stub
		this.op = op;
	}
	
	public InsType_I(int op,int rt) {
		// TODO Auto-generated constructor stub
		this.op = op;
		this.rt = rt;
	}

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return I_TYPE;
	}

	public String getOp(){
		return Utils.format(Integer.toBinaryString(op), 6);
	}

	public String getRt(){
		return Utils.format(Integer.toBinaryString(rt), 5);
	}
}
