package utilities;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleConstants.FontConstants;

import GUI.MainFrame;
import compiler.BackEndStruct;

public class DataUtil {
	public static int fontSize = 14;// �����С
	public static MainFrame mainframe;

	public static int STATE_TEXT = 1; // ��ͨ�ı�
	public static int STATE_DOUBLE_QUOTE = 2; // ˫����
	public static int STATE_SINGLE_QUOTE = 3; // ������
	public static int STATE_MULTI_LINE_COMMENT = 4; // ����ע��
	public static int STATE_LINE_COMMENT = 5; // ����ע��

	public static String label;
	private static int lineNumber; // �к�
	private static boolean enableLineNumber = true; // �����кű�־

	public boolean isEnableLineNumber() {
		return enableLineNumber;
	}

	// line numbers are printed by default.
	// but this behavior can be disabled by invoking this method and setting the
	// flag to false
	public void setEnableLineNumber(boolean enableLineNumber) {
		this.enableLineNumber = enableLineNumber;
	}

	static String[] literalArray = { "null", "true", "false" }; // ���泣��
	static String[] keywordArray = { "abstract", "break", "case", "catch", "class", "const", "continue", "default",
			"do", "else", "extends", "final", "finally", "for", "goto", "if", "implements", "import", "instanceof",
			"interface", "native", "new", "package", "private", "protected", "public", "return", "static", "strictfp",
			"super", "switch", "synchronized", "this", "throw", "throws", "transient", "try", "volatile", "while" }; // �ؼ���
	static String[] primitiveTypeArray = { "boolean", "char", "byte", "short", "int", "long", "float", "double",
			"void" }; // ԭʼ��������

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
		str = str.replaceAll("\\s\\)(\\s)*", "\\)");
		str = str.replaceAll("\\s\\[(\\s)*", "\\[");
		str = str.replaceAll("\\s\\](\\s)*", "\\]");
		String nonWord = "(;|\\(|\\)|\\{|\\}|=|!=|\\+|-|\\*|/|%|>|<|\\[|\\]|\\$|~|&|,|\\|)";
		//System.out.println(str);
		String regexpr = "(\\s)*(\\w)+(\\s|;|\\(|\\)|\\{|\\}|=|!=|\\+|-|\\*|/|%|>|<|\\[|\\]|\\$|~|&|,|\\|)";
		String insideregex = "(\\w)+(\\s)";
		String doubleop = "(\\w)+(!=)";
		for (int i = 0; i <= str.length(); i++) {
			String sub = str.substring(0, i);
			if (sub.matches(regexpr)) {//����һ������
				if (sub.matches(insideregex)) {//�ô����Կհ׷���β
					res.add(sub.trim());
				} else {//������;=+-�Ƚ�β
					if(sub.matches(doubleop)){
						if(sub.matches("(\\S)+\\s")){
							res.add(sub.substring(0, sub.length() - 2).trim());
							res.add(sub.substring(sub.length() - 2,sub.length() - 1).trim());
						}else{
							res.add(sub.substring(0, sub.length() - 2).trim());
							res.add(sub.substring(sub.length() - 2).trim());
						}
						
					}else{
						res.add(sub.substring(0, sub.length() - 1));
						res.add(sub.substring(sub.length() - 1));
					}
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
			}else if(i < res.size() - 1 && res.get(i).equals("<")){
				if(res.get(i + 1).equals("=")){
					result[i - offset] = "<=";
					offset++;
					i++;
				}else if(res.get(i + 1).equals("<")){
					result[i - offset] = "<<";
					offset++;
					i++;
				}else{
					result[i-offset] = "<";
				}
				
			}else if(i < res.size() - 1 && res.get(i).equals(">")){
				if(res.get(i + 1).equals("=")){
					result[i - offset] = ">=";
					offset++;
					i++;
				}else if(res.get(i + 1).equals(">")){
					result[i - offset] = ">>";
					offset++;
					i++;
				}else{
					result[i-offset] = ">";
				}
				
			}else
				result[i - offset] = res.get(i);
		}
		String[] updateRes = new String[res.size() - offset];
		for (int i = 0; i < updateRes.length; i++) {
			updateRes[i] = result[i];
			//System.out.println(updateRes[i]);
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

}
