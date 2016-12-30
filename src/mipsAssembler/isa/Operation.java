package mipsAssembler.isa;

import mipsAssembler.i_type.InsType;
import mipsAssembler.i_type.InsType_I;
import mipsAssembler.i_type.InsType_J;
import mipsAssembler.i_type.InsType_R;

public enum Operation {

	//R-type instruction
	ADD("add",new InsType_R(InsType.OP0,0b100000),OprandMode.R_RS_RT_RD),
	ADDU("addu",new InsType_R(InsType.OP0,0b100001),OprandMode.R_RS_RT_RD),
	SUB("sub",new InsType_R(InsType.OP0,0b100010),OprandMode.R_RS_RT_RD),
	SUBU("subu",new InsType_R(InsType.OP0,0b100011),OprandMode.R_RS_RT_RD),
	AND("and",new InsType_R(InsType.OP0,0b100100),OprandMode.R_RS_RT_RD),
	MULT("mult",new InsType_R(InsType.OP0,0b011000),OprandMode.R_RS_RT),
	MULTU("multu",new InsType_R(InsType.OP0,0b011001),OprandMode.R_RS_RT),
	DIV("div",new InsType_R(InsType.OP0,0b011010),OprandMode.R_RS_RT),
	DIVU("divu",new InsType_R(InsType.OP0,0b011011),OprandMode.R_RS_RT),
	MFHI("mfhi",new InsType_R(InsType.OP0,0b010000),OprandMode.R_RD),
	MFLO("mflo",new InsType_R(InsType.OP0,0b010010),OprandMode.R_RD),
	MTHI("mthi",new InsType_R(InsType.OP0,0b010001),OprandMode.R_RS),
	MTLO("mtlo",new InsType_R(InsType.OP0,0b010011),OprandMode.R_RS),
	MFC0("mfc0",new InsType_R(0b010000,0b00000,InsType.CPO),OprandMode.R_CP0),
	MTC0("mtc0",new InsType_R(0b010000,0b00100,InsType.CPO),OprandMode.R_CP0),
	OR("or",new InsType_R(InsType.OP0,0b100101),OprandMode.R_RS_RT_RD),
	XOR("xor",new InsType_R(InsType.OP0,0b100110),OprandMode.R_RS_RT_RD),
	NOR("nor",new InsType_R(InsType.OP0,0b100111),OprandMode.R_RS_RT_RD),
	SLT("slt",new InsType_R(InsType.OP0,0b101010),OprandMode.R_RS_RT_RD),
	SLTU("sltu",new InsType_R(InsType.OP0,0b101011),OprandMode.R_RS_RT_RD),
	SLL("sll",new InsType_R(InsType.OP0,0b000000),OprandMode.R_RT_RD_SHAMT),
	SRL("srl",new InsType_R(InsType.OP0,0b000010),OprandMode.R_RT_RD_SHAMT),
	SRA("sra",new InsType_R(InsType.OP0,0b000011),OprandMode.R_RT_RD_SHAMT),
	SLLV("sllv",new InsType_R(InsType.OP0,0b000100),OprandMode.R_RS_RT_RD),
	SRLV("srlv",new InsType_R(InsType.OP0,0b000110),OprandMode.R_RS_RT_RD),
	SRAV("srav",new InsType_R(InsType.OP0,0b000111),OprandMode.R_RS_RT_RD),
	JR("jr",new InsType_R(InsType.OP0,0b001000),OprandMode.R_RS),
	JALR("jalr",new InsType_R(InsType.OP0,0b001001),OprandMode.R_RS_RD),
	BREAK("break",new InsType_R(InsType.OP0,0b001101),OprandMode.R_CODE),
	SYSCALL("syscall",new InsType_R(InsType.OP0,0b001100),OprandMode.R_CODE),
	ERET("eret",new InsType_R(0b010000,0b011000,InsType.ERET),OprandMode.R_CODE),
	//i-type instruction
	ADDI("addi",new InsType_I(0b001000),OprandMode.I_RS_RT_IMME),
	ADDIU("addiu",new InsType_I(0b001001),OprandMode.I_RS_RT_IMME),
	ANDI("andi",new InsType_I(0b001100),OprandMode.I_RS_RT_IMME),
	ORI("ori",new InsType_I(0b001101),OprandMode.I_RS_RT_IMME),
	XORI("xori",new InsType_I(0b001110),OprandMode.I_RS_RT_IMME),
	LUI("lui",new InsType_I(0b001111),OprandMode.I_RT_IMME),
	
	LB("lb",new InsType_I(0b100000),OprandMode.I_RS_RT_OFFSET),
	LBU("lbu",new InsType_I(0b100100),OprandMode.I_RS_RT_OFFSET),
	LH("lh",new InsType_I(0b100001),OprandMode.I_RS_RT_OFFSET),
	LHU("lhu",new InsType_I(0b100101),OprandMode.I_RS_RT_OFFSET),
	SB("sb",new InsType_I(0b101000),OprandMode.I_RS_RT_OFFSET),
	SH("sh",new InsType_I(0b101001),OprandMode.I_RS_RT_OFFSET),
	LW("lw",new InsType_I(0b100011),OprandMode.I_RS_RT_OFFSET),
	SW("sw",new InsType_I(0b101011),OprandMode.I_RS_RT_OFFSET),
	
	BEQ("beq",new InsType_I(0b000100),OprandMode.I_RT_RS_OFFSET),
	BNE("bne",new InsType_I(0b000101),OprandMode.I_RT_RS_OFFSET),
	BGEZ("bgez",new InsType_I(0b000001,0b00001),OprandMode.I_RS_OFFSET),
	BGTZ("bgtz",new InsType_I(0b000111,0b00000),OprandMode.I_RS_OFFSET),
	BLEZ("blez",new InsType_I(0b000110,0b00000),OprandMode.I_RS_OFFSET),
	BLTZ("bltz",new InsType_I(0b000001,0b00000),OprandMode.I_RS_OFFSET),
	BGEZAL("bgezal",new InsType_I(0b000001,0b10001),OprandMode.I_RS_OFFSET),
	BLTZAL("bltzal",new InsType_I(0b000001,0b10000),OprandMode.I_RS_OFFSET),
	SLTI("slti",new InsType_I(0b001010),OprandMode.I_RS_RT_IMME),
	SLTIU("sltiu",new InsType_I(0b001011),OprandMode.I_RS_RT_IMME),//j-type instruction
	J("j",new InsType_J(0b000010),OprandMode.J_ADDR),
	JAL("jal",new InsType_J(0b000011),OprandMode.J_ADDR);

	
	
	private String opName;
	public InsType type;
	public OprandMode mode;
	Operation(String opName,InsType type,OprandMode mode){
		this.opName = opName;
		this.type = type;
		this.mode = mode;
	}
	
	public static Operation getOperationByName(String name){
		for(Operation operation:values()){
			if(operation.opName.equalsIgnoreCase(name)){
				return operation;
			}else continue;
		}
		return null;
	}
	public String getOpName(){
		return opName;
	}

}
