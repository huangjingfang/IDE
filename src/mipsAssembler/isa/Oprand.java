package mipsAssembler.isa;

import java.util.ArrayList;
import java.util.List;

import mipsAssembler.AssembleContext;
import mipsAssembler.utils.Utils;

public class Oprand {
	String name;
	OprandType mode;

	public Oprand(){
		
	}
	public Oprand(String name,OprandType type){
		this.name = name;
		this.mode = type;
	}
	public OprandType getType(){
		return mode;
	}
	public String getName(){
		return name;
	}
	public void setName(String name){
		this.name = name;
	}
	
	private static final String REG_MATCH = "\\$(\\S+)";
	public static List<Oprand> parse(String[] splitDataSeg,OprandType[] types) {
		// TODO Auto-generated method stub
		ArrayList<Oprand> oprands = new ArrayList<>();
		int count = 0;
		for(int i=0;i<splitDataSeg.length;i++){
			if(splitDataSeg[i].isEmpty()){
				continue;
			}else{
				Oprand op = new Oprand(splitDataSeg[i].trim(),types[count]);
				count++;
				oprands.add(op);
			}
		}
		return oprands;
	}
	
	public String genBinary(int length,AssembleContext context) throws Exception{
		switch (mode) {
		case REGISTER:
			if(!name.matches(REG_MATCH)){
				throw new Exception("No REGISTER:"+name);
			}else{
				switch(name)
				{
				case "$0":
				case "$zero":
					return "00000";
				case "$1":
				case "$at":
					return "00001";
				case "$2":
				case "$v0":
					return "00010";
				case "$3":
				case "$v1":
					return "00011";
				case "$4":
				case "$a0":
					return "00100";
				case "$5":
				case "$a1":
					return "00101";
				case "$6":
				case "$a2":
					return "00110";
				case "$7":
				case "$a3":
					return "00111";
				case "$8":
				case "$t0":
					return "01000";
				case "$9":
				case "$t1":
					return "01001";
				case "$10":
				case "$t2":
					return "01010";
				case "$11":
				case "$t3":
					return "01011";
				case "$12":
				case "$t4":
					return "01100";
				case "$13":
				case "$t5":
					return "01101";
				case "$14":
				case "$t6":
					return "01110";
				case "$15":
				case "$t7":
					return "01111";
				case "$16":
				case "$s0":
					return "10000";
				case "$17":
				case "$s1":
					return "10001";
				case "$18":
				case "$s2":
					return "10010";
				case "$19":
				case "$s3":
					return "10011";
				case "$20":
				case "$s4":
					return "10100";
				case "$21":
				case "$s5":
					return "10101";
				case "$22":
				case "$s6":
					return "10110";
				case "$23":
				case "$s7":
					return "10111";
				case "$24":
				case "$t8":
					return "11000";
				case "$25":
				case "$t9":
					return "11001";
				case "$26":
				case "$k0":
					return "11010";
				case "$27":
				case "$k1":
					return "11011";
				case "$28":
				case "$gp":
					return "11100";
				case "$29":
				case "$sp":
					return "11101";
				case "$30":
				case "$s8":
				case "$fp":
					return "11110";
				case "$31":
				case "$ra":
					return "11111";
				default: throw new Exception("No REGISTER:"+name);
				}
			}
		case IMMEDIATE:
			if(!name.matches("-?\\d+")){
				throw new Exception("Invalid Number:"+name);
			}else {
				String temp = Integer.toBinaryString(Integer.parseInt(name));
				if(Math.pow(2, length)<Long.parseLong(name)){
					throw new Exception("Invalid Number:"+name);
				}else{
					return Utils.format(Long.parseLong(name), length);
				}
			}
		case OFFSET:
			if(!name.matches("-?\\d+")){
				return name;
			}else{
				//String temp = Integer.toBinaryString(Integer.parseInt(name));
				if(Math.pow(2, length)<Long.parseLong(name)){
					throw new Exception("Invalid Number:"+name);
				}else{
					return Utils.format(Long.parseLong(name), length);
				}
			}
		case SHIFT_AMOUNT:
			if(!name.matches("\\d+")){
				throw new Exception("Invalid Number:"+name);
			}{
				String temp = Integer.toBinaryString(Integer.parseInt(name));
				if(length<temp.length()){
					throw new Exception("Invalid Number:"+name);
				}else{
					return Utils.format(temp, length);
				}
			}
		case ADDRESS:
			if(name.matches("\\d+")){
				String temp = Long.toBinaryString(Long.parseLong(name)/4);
				return Utils.format(temp, length);
			}else if(name.matches("([a-zA-Z][0-9a-zA-Z._$]*)")){
				return name;
//				long label_addr = context.getLabelAddress(name);
//				if(label_addr<0){
//					throw new Exception("No label:"+name);
//				}else{
//					String temp = Long.toBinaryString(label_addr);
//					return Utils.format(temp, length);
//				}
			}else throw new Exception("·Ç·¨µØÖ·£º"+name);
		case CODE:
			if(!name.matches("\\d+")){
				throw new Exception("Invalid Number:"+name);
			}else{
				String temp = Integer.toBinaryString(Integer.parseInt(name));
				if(length<temp.length()){
					throw new Exception("Invalid Number:"+name);
				}else{
					return Utils.format(temp, length);
				}
			}
			
		case VARIABLE:
			if(!name.matches("([a-zA-Z][0-9a-zA-Z._$]*)")){
				throw new Exception("Invalid Variable :"+name);
			}else{
				long label_addr = context.getVariableAddress(name);
				if(label_addr<0){
					throw new Exception("No Variable:"+name);
				}else{
					String temp = Long.toBinaryString(label_addr);
					return Utils.format(temp, length);
				}
			}
		default:
			throw new Exception("Unable to parse oprand:"+name);
		}
	}

}
