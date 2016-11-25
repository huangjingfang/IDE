package utilities;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleConstants.FontConstants;

public class DataUtil {
	public static String[] keywords;
	public static int fontSize = 14;// 字体大小
	public static SimpleAttributeSet purple;// 关键字的属性集
	public static SimpleAttributeSet blue;// 引号内的属性集
	public static SimpleAttributeSet lightGreen;// 注释的属性集
	public static SimpleAttributeSet normal;// 其他的默认属性集
	public static SimpleAttributeSet find;// 搜索结果

	static {
		keywords = new String[] { "abstract", "do", "import", "public", "throws", "boolean", "double", "instanceof",
				"return", "transient", "break", "else", "int", "short", "try", "byte", "extends", "interface", "static",
				"void", "case", "final", "long", "strictfp", "volatile", "catch", "finally", "native", "super", "while",
				"char", "float", "new", "switch", "class", "for", "package", "synchronized", "continue", "if",
				"private", "this", "default", "implements", "protected", "const", "goto", "null", "true", "false" };

		purple = new SimpleAttributeSet();
		StyleConstants.setForeground(purple, new Color(135, 38, 87));
		FontConstants.setFontFamily(purple, "Consolas");
		FontConstants.setFontSize(purple, DataUtil.fontSize);

		blue = new SimpleAttributeSet();
		StyleConstants.setForeground(blue, Color.BLUE);
		FontConstants.setFontFamily(blue, "Consolas");
		FontConstants.setFontSize(blue, DataUtil.fontSize);

		lightGreen = new SimpleAttributeSet();
		StyleConstants.setForeground(lightGreen, new Color(0, 201, 87));
		FontConstants.setFontFamily(lightGreen, "Microsoft YaHei UI");
		FontConstants.setFontSize(lightGreen, DataUtil.fontSize);

		normal = new SimpleAttributeSet();
		StyleConstants.setForeground(normal, Color.BLACK);
		FontConstants.setFontFamily(normal, "Consolas");
		FontConstants.setFontSize(normal, DataUtil.fontSize);

		find = new SimpleAttributeSet();
		StyleConstants.setForeground(find, Color.WHITE);
		StyleConstants.setBackground(find, Color.RED);
		FontConstants.setFontFamily(find, "Consolas");
		FontConstants.setFontSize(find, DataUtil.fontSize);
	};

	public static int STATE_TEXT = 1; // 普通文本
	public static int STATE_DOUBLE_QUOTE = 2; // 双引号
	public static int STATE_SINGLE_QUOTE = 3; // 单引号
	public static int STATE_MULTI_LINE_COMMENT = 4; // 多行注释
	public static int STATE_LINE_COMMENT = 5; // 单行注释

	private static int lineNumber; // 行号
	private static boolean enableLineNumber = true; // 开启行号标志

	public boolean isEnableLineNumber() {
		return enableLineNumber;
	}

	// line numbers are printed by default.
	// but this behavior can be disabled by invoking this method and setting the
	// flag to false
	public void setEnableLineNumber(boolean enableLineNumber) {
		this.enableLineNumber = enableLineNumber;
	}

	static String[] literalArray = { "null", "true", "false" }; // 字面常量
	static String[] keywordArray = { "abstract", "break", "case", "catch", "class", "const", "continue", "default",
			"do", "else", "extends", "final", "finally", "for", "goto", "if", "implements", "import", "instanceof",
			"interface", "native", "new", "package", "private", "protected", "public", "return", "static", "strictfp",
			"super", "switch", "synchronized", "this", "throw", "throws", "transient", "try", "volatile", "while" }; // 关键词
	static String[] primitiveTypeArray = { "boolean", "char", "byte", "short", "int", "long", "float", "double",
			"void" }; // 原始数据类型

	static Set<String> literalSet = new HashSet<String>(Arrays.asList(literalArray));
	static Set<String> keywordSet = new HashSet<String>(Arrays.asList(keywordArray));
	static Set<String> primitiveTypeSet = new HashSet<String>(Arrays.asList(primitiveTypeArray));

	public static String process(String src) {
		int currentState = STATE_TEXT;
		int identifierLength = 0;
		int currentIndex = -1;
		char currentChar = 0;
		String identifier = "";
		StringBuffer out = new StringBuffer();

		while (++currentIndex != src.length() - 1) {
			currentChar = src.charAt(currentIndex);
			if (Character.isJavaIdentifierPart(currentChar)) {
				out.append(currentChar);
				++identifierLength;
				continue;
			}
			if (identifierLength > 0) {
				identifier = out.substring(out.length() - identifierLength);
				if (currentState == STATE_TEXT) {
					if (literalSet.contains(identifier)) { // identifier is a
															// literal
						out.insert(out.length() - identifierLength, "<div class=\"literalStyle\">");
						out.append("</div>");
					} else if (keywordSet.contains(identifier)) { // identifier
																	// is a
																	// keyword
						out.insert(out.length() - identifierLength, "<div class=\"keywordStyle\">");
						out.append("</div>");
					} else if (primitiveTypeSet.contains(identifier)) { // identifier
																		// is a
																		// primitive
																		// type
						out.insert(out.length() - identifierLength, "<div class=\"primitiveTypeStyle\">");
						out.append("</div>");
					} else if (identifier.equals(identifier.toUpperCase())
							&& !Character.isDigit(identifier.charAt(0))) { // identifier
																			// is
																			// a
																			// constant
						out.insert(out.length() - identifierLength, "<div class=\"constantStyle\">");
						out.append("</div>");
					} else if (Character.isUpperCase(identifier.charAt(0))) { // identifier
																				// is
																				// non-primitive
																				// type
						out.insert(out.length() - identifierLength, "<div class=\"nonPrimitiveTypeStyle\">");
						out.append("</div>");
					}
				}
			}

			switch (currentChar) {
			// because I handle the "greater than" and "less than" marks
			// somewhere else, I comment them out here
			// case '<':
			// out.append("&lt;");
			// break;
			// case '>':
			// out.append("&gt;");
			// break;
			case '\"':
				out.append('\"');
				if (currentState == STATE_TEXT) {
					currentState = STATE_DOUBLE_QUOTE;
					out.insert(out.length() - ("\"").length(), "<div class=\"doubleQuoteStyle\">");
				} else if (currentState == STATE_DOUBLE_QUOTE) {
					currentState = STATE_TEXT;
					out.append("</div>");
				}
				break;
			case '\'':
				out.append("\'");
				if (currentState == STATE_TEXT) {
					currentState = STATE_SINGLE_QUOTE;
					out.insert(out.length() - ("\'").length(), "<div class=\"singleQuoteStyle\">");
				} else if (currentState == STATE_SINGLE_QUOTE) {
					currentState = STATE_TEXT;
					out.append("</div>");
				}
				break;
			case '\\':
				out.append("\\");
				if (currentState == STATE_DOUBLE_QUOTE || currentState == STATE_SINGLE_QUOTE) {
					// treat as a character escape sequence
					out.append(src.charAt(++currentIndex));
				}
				break;
			// if you want to translate tabs into spaces, uncomment the
			// following lines
			// case '\t':
			// // replace tabs with tabsize number of spaces
			// for (int i = 0; i < tabSize; i++)
			// out.append("&nbsp;&nbsp;&nbsp;&nbsp;");
			// break;
			case '*':
				out.append('*');
				if (currentState == STATE_TEXT && currentIndex > 0 && src.charAt(currentIndex - 1) == '/') {
					out.insert(out.length() - ("/*").length(), "<div class=\"multiLineCommentStyle\">");
					currentState = STATE_MULTI_LINE_COMMENT;
				}
				break;
			case '/':
				out.append("/");
				if (currentState == STATE_TEXT && currentIndex > 0 && src.charAt(currentIndex - 1) == '/') {
					out.insert(out.length() - ("//").length(), "<div class=\"singleLineCommentStyle\">");
					currentState = STATE_LINE_COMMENT;
				} else if (currentState == STATE_MULTI_LINE_COMMENT) {
					out.append("</div>");
					currentState = STATE_TEXT;
				}
				break;
			case '\r':
			case '\n':
				// end single line comments
				if (currentState == STATE_LINE_COMMENT) {
					out.insert(out.length() - 1, "</div>");
					currentState = STATE_TEXT;
				}
				if (currentChar == '\r' && currentIndex < src.length() - 1) {
					out.append("\r\n");
					++currentIndex;
				} else
					out.append('\n');

				if (enableLineNumber)
					out.append("<div class=\"lineNumberStyle\">" + (++lineNumber) + ".</div>");
				break;
			case 0:
				if (currentState == STATE_LINE_COMMENT && currentIndex == (src.length() - 1))
					out.append("</div>");
				break;
			default: // everything else
				out.append(currentChar);
			}
			identifierLength = 0;
		}
		return out.toString();
	}

	public static String[] divide(String str) {
		ArrayList<String> res = new ArrayList<>();
		str = str.replaceAll("\\s\\((\\s)*", "\\(");
		String nonWord = "(;|\\(|\\)|\\{|\\}|=|\\+|-|\\*|/|>|<)";
		// System.out.println(str);
		String regexpr = "(\\s)*(\\w)+(\\s|;|\\(|\\)|\\{|\\}|=|\\+|-|\\*|/|>|<)";
		String insideregex = "(\\w)+(\\s)";
		for (int i = 0; i <= str.length(); i++) {
			String sub = str.substring(0, i);
			if (sub.matches(regexpr)) {
				if (sub.matches(insideregex)) {
					res.add(sub.trim());
				} else {
					res.add(sub.substring(0, sub.length() - 1));
					res.add(sub.substring(sub.length() - 1));
				}
				str = str.substring(i).trim();
				i = 0;
				// System.out.println(str);
			} else if (sub.matches(nonWord)) {
				res.add(sub);
				str = str.substring(i).trim();
				i = 0;
			}
		}
		String[] result = new String[res.size()];
		int offset = 0;
		for (int i = 0; i < res.size(); i++) {
			if (i < res.size() - 1 && res.get(i).equals("=") && res.get(i + 1).equals("=")) {
				result[i - offset] = "==";
				offset++;
				i++;
			} else
				result[i - offset] = res.get(i);
		}
		String[] updateRes = new String[res.size() - offset];
		for (int i = 0; i < updateRes.length; i++) {
			updateRes[i] = result[i];
		}
		return updateRes;
	}
/*
	public static void main(String[] args) {
		String s = "int number;\nint c;\nvoid main (void){\n int a;\n int b;\n a= 1;"
				+ "\n b = 2;\n c = a * b;\n if(a>b)\n a = debug();\n else{ \n"
				+ " a = 4;\n b = 5;\n } \n return ;\n}\n";
		String[] strs = divide(s);
		for (String str : strs) {
			System.out.println(str);
		}
	}*/

	static String[] instructionsSet = new String[] { "ADD", "ADDU", "ADDI", "ADDIU", "SUB", "SUBU", "MULT", "MULTU",
			"DIV", "DIVU", "AND", "ANDI", "OR", "ORI", "XOR", "XORI", "NOR", "LUI", "SLL", "SRL", "SRA", "SLLV", "SRLV",
			"SRAV", "LB", "LBU", "LH", "LHU", "LW", "SB", "SH", "SW", "BEQ", "BNE", "SLT", "SLTI", "SLTU", "SLTIU",
			"BGEZ", "BGTZ", "BLEZ", "BLTZ", "BZEZAL", "BLTZAL", "J", "JR", "JAL", "JALR", "MFHI", "MFLO", "MTHI",
			"MTLO", "BREAK", "SYSCALL", "ERET", "MFC0", "MTC0" };
}
