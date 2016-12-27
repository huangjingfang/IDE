package mipsAssembler.isa;

import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mipsAssembler.AssembleContext;
import mipsAssembler.LineParse;
import mipsAssembler.utils.Constants;
import mipsAssembler.utils.Utils;

public class Declare {
	private static final String ZERO_BYTE = "00000000";
	private static final String ZERO_HALF = "0000000000000000";
	private static final String ASCII_STRING_REGEX = "(\\s*\"([\\S\\s]+)\"\\s*)+";
	
	private static final Matcher m_ASCII_STRING_REGEX = Pattern.compile(ASCII_STRING_REGEX).matcher("");
	DeclareType type;
	String[] values;

	public Declare(DeclareType type, String[] value) {
		// TODO Auto-generated constructor stub
		this.type = type;
		this.values = value;
	}
	public Declare(DeclareType type, String value) {
		// TODO Auto-generated constructor stub
		this.type = type;
		this.values = new String[]{value};
	}
	public String getBinary() throws Exception {
		StringBuilder builder = new StringBuilder();
		int dataLength = 0;
		switch (type) {
		case DATA:
			if(values.length==0||values[0].isEmpty()){
				AssembleContext.setDsa(AssembleContext.DEFAULT_DATA_SEG_ADDRESS);
			}else{
				AssembleContext.setDsa(Utils.parseAddr(values[0]));
			}
			LineParse.currentSeg = false;
			return null;
		case TEXT:
			if(values.length==0||values[0].isEmpty()){
				AssembleContext.setCsa(AssembleContext.DEFAULT_CODE_SEG_ADDRESS);
			}else{
				System.out.println("values:"+values[0]);
				AssembleContext.setCsa(Utils.parseAddr(values[0]));
			}
			LineParse.currentSeg = true;
			return null;
		case BYTE:
			if(values.length==0){
				throw new Exception("Variable has no data");
			}else{
				int length = values.length;
				dataLength = length;
				int index = 0;
				String v0,v1,v2,v3;
				while(length>0){
					if(length>4){
						v0 = Utils.format(Integer.toBinaryString(Integer.parseInt(values[index])), Constants.BYTE_LENGTH);
						v1 = Utils.format(Integer.toBinaryString(Integer.parseInt(values[index+1])), Constants.BYTE_LENGTH);
						v2 = Utils.format(Integer.toBinaryString(Integer.parseInt(values[index+2])), Constants.BYTE_LENGTH);
						v3 = Utils.format(Integer.toBinaryString(Integer.parseInt(values[index+3])), Constants.BYTE_LENGTH);
						builder.append(v0).append(v1).append(v2).append(v3).append(",");
						index +=4;
						length-=4;
					}else{
						switch (length%4) {
						case 0:
							v0 = Utils.format(Integer.toBinaryString(Integer.parseInt(values[index])), Constants.BYTE_LENGTH);
							v1 = Utils.format(Integer.toBinaryString(Integer.parseInt(values[index+1])), Constants.BYTE_LENGTH);
							v2 = Utils.format(Integer.toBinaryString(Integer.parseInt(values[index+2])), Constants.BYTE_LENGTH);
							v3 = Utils.format(Integer.toBinaryString(Integer.parseInt(values[index+3])), Constants.BYTE_LENGTH);
							builder.append(v0).append(v1).append(v2).append(v3).append(",");
							index +=4;
							length-=4;
							break;
						case 1:
							v0 = Utils.format(Integer.toBinaryString(Integer.parseInt(values[index])), Constants.BYTE_LENGTH);
							builder.append(v0).append(ZERO_BYTE).append(ZERO_BYTE).append(ZERO_BYTE).append(",");
							index +=1;
							length-=1;
							break;
						case 2:
							v0 = Utils.format(Integer.toBinaryString(Integer.parseInt(values[index])), Constants.BYTE_LENGTH);
							v1 = Utils.format(Integer.toBinaryString(Integer.parseInt(values[index+1])), Constants.BYTE_LENGTH);
							builder.append(v0).append(v1).append(ZERO_BYTE).append(ZERO_BYTE).append(",");
							index +=2;
							length-=2;
							break;
						case 3:
							v0 = Utils.format(Integer.toBinaryString(Integer.parseInt(values[index])), Constants.BYTE_LENGTH);
							v1 = Utils.format(Integer.toBinaryString(Integer.parseInt(values[index+1])), Constants.BYTE_LENGTH);
							v2 = Utils.format(Integer.toBinaryString(Integer.parseInt(values[index+2])), Constants.BYTE_LENGTH);
							builder.append(v0).append(v1).append(v2).append(ZERO_BYTE).append(",");
							index +=3;
							length-=3;
							break;
						default:
							break;
						}
					}
				}
			}
			break;
		case HALF:
			if(values.length==0){
				throw new Exception("Variable has no data");
			}else{
				int length = values.length;
				dataLength = 2*length;
				int index = 0;
				String v0,v1;
				while(length>0){
					if(length>2){
						v0 = Utils.format(Integer.toBinaryString(Integer.parseInt(values[index])), Constants.HALF_WORD_LENGTH);
						v1 = Utils.format(Integer.toBinaryString(Integer.parseInt(values[index+1])), Constants.HALF_WORD_LENGTH);
						builder.append(v0).append(v1).append(",");
						index +=2;
						length-=2;
					}else{
						switch (length%2) {
						case 0:
							v0 = Utils.format(Integer.toBinaryString(Integer.parseInt(values[index])), Constants.HALF_WORD_LENGTH);
							v1 = Utils.format(Integer.toBinaryString(Integer.parseInt(values[index+1])), Constants.HALF_WORD_LENGTH);
							builder.append(v0).append(v1).append(",");
							index +=2;
							length-=2;
							break;
						case 1:
							v0 = Utils.format(Integer.toBinaryString(Integer.parseInt(values[index])), Constants.HALF_WORD_LENGTH);
							builder.append(v0).append(ZERO_HALF).append(",");
							index +=1;
							length-=1;
							break;
						default:
							break;
						}
					}
				}
			}
			break;
		case WORD:
			if(values.length==0){
				throw new Exception("Variable has no data");
			}else{
				dataLength = 4*values.length;
				int index = 0;
				String v;
				while(index<values.length){
					v = Utils.format(Integer.toBinaryString(Integer.parseInt(values[index])), Constants.WORD_LENGTH);
					builder.append(v).append(",");
					index ++;
				}
			}
			break;
		case FLOAT:
			
			break;
		case DOUBLE:

			break;
		case ASCII:
			if(values.length==0){
				throw new Exception("Variable has no data");
			}else{
				dataLength = 0;
				for(String s:values){
					m_ASCII_STRING_REGEX.reset(s);
					if(!m_ASCII_STRING_REGEX.matches()){
						throw new Exception("data:"+s+" is not a String");
					}else{
						s = m_ASCII_STRING_REGEX.group(2);
						byte[] bytes = s.getBytes(StandardCharsets.US_ASCII);
						for(byte b:bytes){
							builder.append(Utils.format(b, Constants.BYTE_LENGTH));
							dataLength++;
						}
					}
				}
				int offset = builder.length()%32;
				for(int i=0;i<32-offset;i++){
					builder.append("0");
					dataLength++;
				}
			}
				
			break;
		case ASCIIZ:
			
			break;
		case SPACE:
			if(values.length==0){
				throw new Exception("No data after .space");
			}else{
				int count = Integer.parseInt(values[0]);
				dataLength = count;
				for(int i=0;i<count;i++){
					builder.append(ZERO_BYTE);
				}
			}
			break;
		default:
			break;
		}
		return Utils.format(builder.toString(), builder.length());
	}

}
