package mipsAssembler.i_type;

import mipsAssembler.utils.Utils;

public class InsType_J extends InsType {
	int op;
	int address;
	
	public InsType_J(int op) {
		// TODO Auto-generated constructor stub
		this.op = op;
	}

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return J_TYPE;
	}

	public String getOp(){
		return Utils.format(Integer.toBinaryString(op), 6);
	}
}
