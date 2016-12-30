package mipsAssembler;

import java.util.List;

import mipsAssembler.i_type.InsType_I;
import mipsAssembler.i_type.InsType_J;
import mipsAssembler.i_type.InsType_R;
import mipsAssembler.isa.Operation;
import mipsAssembler.isa.Oprand;

public class Instruction {
	private static final String VARI_REGEX = "([a-zA-Z._$][0-9a-zA-Z._$]*)";
	private Operation op;
	private List<Oprand> oprands;
	
	public Instruction(Operation op,List<Oprand> oprands) {
		this.op = op;
		this.oprands = oprands;
	}
	
	private Instruction() {
		// TODO Auto-generated constructor stub
	}

	public static Instruction genInstruction(Operation operation,List<Oprand> oprands) throws Exception{
		return new Instruction(operation, oprands);
		
	}

	public String genBinary(AssembleContext context) throws Exception {
		// TODO Auto-generated method stub
		InsType_R type_R;
		InsType_I type_I;
		InsType_J type_J;
		StringBuilder builder = new StringBuilder();
		switch (op.getOpName()) {
		case "add":
		case "addu":
		case "sub":
		case "subu":
		case "and":
		case "or":
		case "xor":
		case "nor":
		case "slt":
		case "sltu":
		case "sllv":
		case "srlv":
		case "srav":
			type_R = (InsType_R)op.type;
			builder.append(type_R.getOp()).append(oprands.get(1).genBinary(5,context))
				.append(oprands.get(2).genBinary(5,context)).append(oprands.get(0).genBinary(5,context)).append("00000")
				.append(type_R.getFunc());
			break;
		case "mult":
		case "multu":
		case "div":
		case "divu":
			type_R = (InsType_R)op.type;
			builder.append(type_R.getOp()).append(oprands.get(0).genBinary(5,context))
				.append(oprands.get(1).genBinary(5,context)).append("0000000000").append(type_R.getFunc());
			break;
		case "mfhi":
		case "mflo":
			type_R = (InsType_R)op.type;
			builder.append(type_R.getOp()).append("0000000000").append(oprands.get(0).genBinary(5,context))
				.append("00000").append(type_R.getFunc());
			break;
		case "mthi":
		case "mtlo":
			type_R = (InsType_R)op.type;
			builder.append(type_R.getOp()).append(oprands.get(0).genBinary(5,context)).append("0000000000")
				.append("00000").append(type_R.getFunc());
			break;
		case "mfc0":
		case "mtc0":
			type_R = (InsType_R)op.type;
			builder.append(type_R.getOp()).append(type_R.getRs()).append(oprands.get(0).genBinary(5,context))
				.append(oprands.get(1).genBinary(5,context)).append("00000").append(oprands.get(2).genBinary(6,context));
			break;
		case "sll":
		case "srl":
		case "sra":
			type_R = (InsType_R)op.type;
			builder.append(type_R.getOp()).append("00000").append(oprands.get(1).genBinary(5,context))
				.append(oprands.get(0).genBinary(5,context)).append(oprands.get(2).genBinary(5,context)).append(type_R.getFunc());
			break;
		case "jr":
			type_R = (InsType_R)op.type;
			builder.append(type_R.getOp()).append(oprands.get(0).genBinary(5,context)).append("000000000000000")
				.append(type_R.getFunc());
			break;
		case "jalr":
			type_R = (InsType_R)op.type;
			builder.append(type_R.getOp()).append(oprands.get(1).genBinary(5,context)).append("00000").append(oprands.get(0).genBinary(5,context))
				.append("00000").append(type_R.getFunc());
			break;
		case "break":
		case "syscall":
		case "eret":
			type_R = (InsType_R)op.type;
			builder.append(type_R.getOp()).append("11111111111111111111")
				.append(type_R.getFunc());
			break;
		case "addi":
		case "addiu":
		case "andi":
		case "ori":
		case "xori":
			type_I = (InsType_I)op.type;
			builder.append(type_I.getOp()).append(oprands.get(1).genBinary(5,context))
				.append(oprands.get(0).genBinary(5,context)).append(oprands.get(2).genBinary(16,context));
			break;
		case "lui":
			type_I = (InsType_I)op.type;
			builder.append(type_I.getOp()).append("00000")
				.append(oprands.get(0).genBinary(5,context)).append(oprands.get(1).genBinary(16,context));
			break;
		case "lb":
		case "lbu":
		case "lh":
		case "lhu":
		case "sb":
		case "sh":
		case "lw":
		case "sw":
			type_I = (InsType_I)op.type;
			builder.append(type_I.getOp()).append(oprands.get(2).genBinary(5,context))
				.append(oprands.get(0).genBinary(5,context)).append(oprands.get(1).genBinary(16,context));
			break;
		case "beq":
		case "bne":
			type_I = (InsType_I)op.type;
			builder.append(type_I.getOp()).append(oprands.get(0).genBinary(5,context))
				.append(oprands.get(1).genBinary(5,context)).append(oprands.get(2).genBinary(16,context));
			break;
		case "bgez":
		case "bgtz":
		case "blez":
		case "bltz":
		case "bgezal":
		case "bltzal":
			type_I = (InsType_I)op.type;
			builder.append(type_I.getOp()).append(oprands.get(0).genBinary(5,context))
				.append(type_I.getRt()).append(oprands.get(1).genBinary(16,context));
			break;
		case "slti":
		case "sltiu":
			type_I = (InsType_I)op.type;
			builder.append(type_I.getOp()).append(oprands.get(1).genBinary(5,context))
				.append(oprands.get(0).genBinary(5,context)).append(oprands.get(2).genBinary(16,context));
			break;
		case "j":
		case "jal":
			type_J = (InsType_J)op.type;
			builder.append(type_J.getOp()).append(oprands.get(0).genBinary(26,context));			
			break;
		default:
			break;
		}
		return builder.toString();
	}
	
	
//	public static Instruction macro(Instruction instruction,AssembleContext context){
//		if(instruction.op==Operation.ORI){
//			List<Oprand> ops = instruction.oprands;
//			String vari_name = ops.get(2).getName();
//			if(vari_name.matches(VARI_REGEX)){
//				Long addr = context.getVariableAddress(vari_name);
//				ops.get(2).setName(addr+"");
//			}
//		}
//		return instruction;
//	}

	
	
	
}
