package mipsAssembler.utils;

public class Utils {
	/**
	 * 将字符串二进制码格式化为规定长度
	 * @param str
	 * @param length
	 * @return
	 */
	public static String format(String str, int length) {
		int cur_len = str.length();
		StringBuilder b = new StringBuilder();
		for (int i = cur_len; i < length; i++) {
			b.append("0");
		}
		return b.append(str).toString();
	}

	/**
	 * 将long类型的十进制数格式化为指定长度的二进制码
	 * @param num
	 * @param length
	 * @return
	 */
	public static String format(long num, int length) {
		if(num>=0){
			StringBuilder b = new StringBuilder();
			String cur = Long.toBinaryString(num);
			for (int i = cur.length(); i < length; i++) {
				b.append("0");
			}
			return b.append(cur).toString();
		}else{
			String cur = Long.toBinaryString(num);
			System.out.println(num+":\t"+cur.substring(cur.length()-length));
			return cur.substring(cur.length()-length);
		}
		
	}

	/**
	 * 将二进制的字符串格式化为十六进制的字符串
	 * @param bin
	 * @param length
	 * @return
	 */
	public static String Bin2Hex(String bin, int length) {
		long d = 0;
		for (int i = bin.length() - 1; i >= 0; i--) {
			if (bin.charAt(i) == '1') {
				d += Math.pow(2, 31 - i);
			}

		}
		return format(Long.toHexString(d), length);
	}


	/**
	 * 将地址描述字符串转换为十进制数
	 * @param addr
	 * @return
	 * @throws Exception
	 */
	
	public static long parseAddr(String addr) throws Exception {
		if (addr.startsWith("0x") | addr.startsWith("0X")) {
			String value = addr.substring(2);
			System.out.println(value);
			long toReturn = 0;
			for (int i = value.length()-1; i >= 0; i--) {
				switch (value.charAt(i)) {
				case '0':
					break;
				case '1':
					toReturn+=Math.pow(16, value.length()-i-1);
					break;
				case '2':
					toReturn+=(2*Math.pow(16, value.length()-i-1));
					break;
				case '3':
					toReturn+=(3*Math.pow(16, value.length()-i-1));
					break;
				case '4':
					toReturn+=(4*Math.pow(16, value.length()-i)-1);
					break;
				case '5':
					toReturn+=(5*Math.pow(16, value.length()-i-1));
					break;
				case '6':
					toReturn+=(6*Math.pow(16, value.length()-i-1));
					break;
				case '7':
					toReturn+=(7*Math.pow(16, value.length()-i-1));
					break;
				case '8':
					toReturn+=(8*Math.pow(16, value.length()-i-1));
					break;
				case '9':
					toReturn+=(9*Math.pow(16, value.length()-i-1));
					break;
				case 'a':
				case 'A':
					toReturn+=(10*Math.pow(16, value.length()-i-1));
					break;
				case 'b':
				case 'B':
					toReturn+=(11*Math.pow(16, value.length()-i-1));
					break;
				case 'c':
				case 'C':
					toReturn+=(12*Math.pow(16, value.length()-i-1));
					break;
				case 'd':
				case 'D':
					toReturn+=(13*Math.pow(16, value.length()-i-1));
					break;
				case 'e':
				case 'E':
					toReturn+=(14*Math.pow(16, value.length()-i-1));
					break;
				case 'f':
				case 'F':
					toReturn+=(15*Math.pow(16, value.length()-i-1));
					break;
				default:
					throw new Exception("Not Hex Number");
				}
			}
			return toReturn;

		} else if (addr.startsWith("0b") | addr.startsWith("0B")) {
			String value = addr.substring(2);
			long toReturn = 0;
			for(int i=value.length()-1;i>=0;i--){
				switch (value.charAt(i)) {
				case '0':
					break;
				case '1':
					toReturn+=Math.pow(2, value.length()-i-1);
					break;
				default:
					throw new Exception("Not Binary Number!");
				}
			}
			return toReturn;
		} else {
			return Long.parseLong(addr);
		}
	}
	
	public static void main(String[] args) throws Exception {
		long a = 4;
		System.out.println(format(a, 14));
	}
}
