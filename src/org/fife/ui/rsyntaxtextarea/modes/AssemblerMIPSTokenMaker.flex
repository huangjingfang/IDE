/*
 * 12/06/2004
 *
 * AssemblerMIPSTokenMaker.java - An object that can take a chunk of text and
 * return a linked list of tokens representing MIPS assembler.
 * 
 * This library is distributed under a modified BSD license.  See the included
 * RSyntaxTextArea.License.txt file for details.
 */
package org.fife.ui.rsyntaxtextarea.modes;

import java.io.*;
import javax.swing.text.Segment;

import org.fife.ui.rsyntaxtextarea.*;


/**
 * This class takes plain text and returns tokens representing MIPS
 * assembler.<p>
 *
 * This implementation was created using
 * <a href="http://www.jflex.de/">JFlex</a> 1.4.1; however, the generated file
 * was modified for performance.  Memory allocation needs to be almost
 * completely removed to be competitive with the handwritten lexers (subclasses
 * of <code>AbstractTokenMaker</code>, so this class has been modified so that
 * Strings are never allocated (via yytext()), and the scanner never has to
 * worry about refilling its buffer (needlessly copying chars around).
 * We can achieve this because RText always scans exactly 1 line of tokens at a
 * time, and hands the scanner this line as an array of characters (a Segment
 * really).  Since tokens contain pointers to char arrays instead of Strings
 * holding their contents, there is no need for allocating new memory for
 * Strings.<p>
 *
 * The actual algorithm generated for scanning has, of course, not been
 * modified.<p>
 *
 * If you wish to regenerate this file yourself, keep in mind the following:
 * <ul>
 *   <li>The generated <code>AssemblerMIPSTokenMaker.java</code> file will contain two
 *       definitions of both <code>zzRefill</code> and <code>yyreset</code>.
 *       You should hand-delete the second of each definition (the ones
 *       generated by the lexer), as these generated methods modify the input
 *       buffer, which we'll never have to do.</li>
 *   <li>You should also change the declaration/definition of zzBuffer to NOT
 *       be initialized.  This is a needless memory allocation for us since we
 *       will be pointing the array somewhere else anyway.</li>
 *   <li>You should NOT call <code>yylex()</code> on the generated scanner
 *       directly; rather, you should use <code>getTokenList</code> as you would
 *       with any other <code>TokenMaker</code> instance.</li>
 * </ul>
 *
 * @author Robert Futrell
 * @version 0.2
 *
 */
%%

%public
%class AssemblerMIPSTokenMaker
%extends AbstractJFlexTokenMaker
%unicode
%ignorecase
%type org.fife.ui.rsyntaxtextarea.Token


%{


	/**
	 * Constructor.  We must have this here as JFLex does not generate a
	 * no parameter constructor.
	 */
	public AssemblerMIPSTokenMaker() {
		super();
	}


	/**
	 * Adds the token specified to the current linked list of tokens.
	 *
	 * @param tokenType The token's type.
	 */
	private void addToken(int tokenType) {
		addToken(zzStartRead, zzMarkedPos-1, tokenType);
	}


	/**
	 * Adds the token specified to the current linked list of tokens.
	 *
	 * @param tokenType The token's type.
	 */
	private void addToken(int start, int end, int tokenType) {
		int so = start + offsetShift;
		addToken(zzBuffer, start,end, tokenType, so);
	}


	/**
	 * Adds the token specified to the current linked list of tokens.
	 *
	 * @param array The character array.
	 * @param start The starting offset in the array.
	 * @param end The ending offset in the array.
	 * @param tokenType The token's type.
	 * @param startOffset The offset in the document at which this token
	 *                    occurs.
	 */
	@Override
	public void addToken(char[] array, int start, int end, int tokenType, int startOffset) {
		super.addToken(array, start,end, tokenType, startOffset);
		zzStartRead = zzMarkedPos;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public String[] getLineCommentStartAndEnd(int languageIndex) {
		return new String[] { ";", null };
	}


	/**
	 * Returns the first token in the linked list of tokens generated
	 * from <code>text</code>.  This method must be implemented by
	 * subclasses so they can correctly implement syntax highlighting.
	 *
	 * @param text The text from which to get tokens.
	 * @param initialTokenType The token type we should start with.
	 * @param startOffset The offset into the document at which
	 *                    <code>text</code> starts.
	 * @return The first <code>Token</code> in a linked list representing
	 *         the syntax highlighted text.
	 */
	public Token getTokenList(Segment text, int initialTokenType, int startOffset) {

		resetTokenList();
		this.offsetShift = -text.offset + startOffset;

		// Start off in the proper state.
		int state = Token.NULL;
		switch (initialTokenType) {
			default:
				state = Token.NULL;
		}

		s = text;
		try {
			yyreset(zzReader);
			yybegin(state);
			return yylex();
		} catch (IOException ioe) {
			ioe.printStackTrace();
			return new TokenImpl();
		}

	}


	/**
	 * Refills the input buffer.
	 *
	 * @return      <code>true</code> if EOF was reached, otherwise
	 *              <code>false</code>.
	 */
	private boolean zzRefill() {
		return zzCurrentPos>=s.offset+s.count;
	}


	/**
	 * Resets the scanner to read from a new input stream.
	 * Does not close the old reader.
	 *
	 * All internal variables are reset, the old input stream 
	 * <b>cannot</b> be reused (internal buffer is discarded and lost).
	 * Lexical state is set to <tt>YY_INITIAL</tt>.
	 *
	 * @param reader   the new input stream 
	 */
	public final void yyreset(Reader reader) {
		// 's' has been updated.
		zzBuffer = s.array;
		/*
		 * We replaced the line below with the two below it because zzRefill
		 * no longer "refills" the buffer (since the way we do it, it's always
		 * "full" the first time through, since it points to the segment's
		 * array).  So, we assign zzEndRead here.
		 */
		//zzStartRead = zzEndRead = s.offset;
		zzStartRead = s.offset;
		zzEndRead = zzStartRead + s.count - 1;
		zzCurrentPos = zzMarkedPos = zzPushbackPos = s.offset;
		zzLexicalState = YYINITIAL;
		zzReader = reader;
		zzAtBOL  = true;
		zzAtEOF  = false;
	}


%}

Letter				= ([A-Za-z_])
Digit				= ([0-9])
Number				= ({Digit}+)

Identifier			= (({Letter}|{Digit})[^ \t\f\n\,\.\+\-\*\/\%\[\]]+)

UnclosedStringLiteral	= ([\"][^\"]*)
StringLiteral			= ({UnclosedStringLiteral}[\"])
UnclosedCharLiteral		= ([\'][^\']*)
CharLiteral			= ({UnclosedCharLiteral}[\'])

CommentBegin			= ([;])

LineTerminator			= (\n)
WhiteSpace			= ([ \t\f])

Label				= (({Letter}|{Digit})+[\:])

Operator				= ("+"|"-"|"*"|"/"|"%"|"^"|"|"|"&"|"~"|"!"|"="|"<"|">")

%%

<YYINITIAL> {

	/* Keywords */
	".186" |
	".286" |
	".286P" |
	".287" |
	".386" |
	".386P" |
	".387" |
	".486" |
	".486P" |
	".586" |
	".586P" |
	".686" |
	".686P" |
	".8086" |
	".8087" |
	".ALPHA" |
	".BREAK" |
	".BSS" |
	".CODE" |
	".CONST" |
	".CONTINUE" |
	".CREF" |
	".DATA" |
	".DATA?" |
	".DOSSEG" |
	".ELSE" |
	".ELSEIF" |
	".ENDIF" |
	".ENDW" |
	".ERR" |
	".ERR1" |
	".ERR2" |
	".ERRB" |
	".ERRDEF" |
	".ERRDIF" |
	".ERRDIFI" |
	".ERRE" |
	".ERRIDN" |
	".ERRIDNI" |
	".ERRNB" |
	".ERRNDEF" |
	".ERRNZ" |
	".EXIT" |
	".FARDATA" |
	".FARDATA?" |
	".IF" |
	".K3D" |
	".LALL" |
	".LFCOND" |
	".LIST" |
	".LISTALL" |
	".LISTIF" |
	".LISTMACRO" |
	".LISTMACROALL" |
	".MMX" |
	".MODEL" |
	".MSFLOAT" |
	".NO87" |
	".NOCREF" |
	".NOLIST" |
	".NOLISTIF" |
	".NOLISTMACRO" |
	".RADIX" |
	".REPEAT" |
	".SALL" |
	".SEQ" |
	".SFCOND" |
	".STACK" |
	".STARTUP" |
	".TEXT" |
	".TFCOND" |
	".UNTIL" |
	".UNTILCXZ" |
	".WHILE" |
	".XALL" |
	".XCREF" |
	".XLIST" |
	".XMM" |
	"__FILE__" |
	"__LINE__" |
	"A16" |
	"A32" |
	"ADDR" |
	"ALIGN" |
	"ALIGNB" |
	"ASSUME" |
	"BITS" |
	"CARRY?" |
	"CATSTR" |
	"CODESEG" |
	"COMM" |
	"COMMENT" |
	"COMMON" |
	"DATASEG" |
	"DOSSEG" |
	"ECHO" |
	"ELSE" |
	"ELSEIF" |
	"ELSEIF1" |
	"ELSEIF2" |
	"ELSEIFB" |
	"ELSEIFDEF" |
	"ELSEIFE" |
	"ELSEIFIDN" |
	"ELSEIFNB" |
	"ELSEIFNDEF" |
	"END" |
	"ENDIF" |
	"ENDM" |
	"ENDP" |
	"ENDS" |
	"ENDSTRUC" |
	"EVEN" |
	"EXITM" |
	"EXPORT" |
	"EXTERN" |
	"EXTERNDEF" |
	"EXTRN" |
	"FAR" |
	"FOR" |
	"FORC" |
	"GLOBAL" |
	"GOTO" |
	"GROUP" |
	"HIGH" |
	"HIGHWORD" |
	"IEND" |
	"IF" |
	"IF1" |
	"IF2" |
	"IFB" |
	"IFDEF" |
	"IFDIF" |
	"IFDIFI" |
	"IFE" |
	"IFIDN" |
	"IFIDNI" |
	"IFNB" |
	"IFNDEF" |
	"IMPORT" |
	"INCBIN" |
	"INCLUDE" |
	"INCLUDELIB" |
	"INSTR" |
	"INVOKE" |
	"IRP" |
	"IRPC" |
	"ISTRUC" |
	"LABEL" |
	"LENGTH" |
	"LENGTHOF" |
	"LOCAL" |
	"LOW" |
	"LOWWORD" |
	"LROFFSET" |
	"MACRO" |
	"NAME" |
	"NEAR" |
	"NOSPLIT" |
	"O16" |
	"O32" |
	"OFFSET" |
	"OPATTR" |
	"OPTION" |
	"ORG" |
	"OVERFLOW?" |
	"PAGE" |
	"PARITY?" |
	"POPCONTEXT" |
	"PRIVATE" |
	"PROC" |
	"PROTO" |
	"PTR" |
	"PUBLIC" |
	"PURGE" |
	"PUSHCONTEXT" |
	"RECORD" |
	"REPEAT" |
	"REPT" |
	"SECTION" |
	"SEG" |
	"SEGMENT" |
	"SHORT" |
	"SIGN?" |
	"SIZE" |
	"SIZEOF" |
	"SIZESTR" |
	"STACK" |
	"STRUC" |
	"STRUCT" |
	"SUBSTR" |
	"SUBTITLE" |
	"SUBTTL" |
	"THIS" |
	"TITLE" |
	"TYPE" |
	"TYPEDEF" |
	"UNION" |
	"USE16" |
	"USE32" |
	"USES" |
	"WHILE" |
	"WRT" |
	"ZERO?"		{ addToken(Token.PREPROCESSOR); }

	"DB" |
	"DW" |
	"DD" |
	"DF" |
	"DQ" |
	"DT" |
	"RESB" |
	"RESW" |
	"RESD" |
	"RESQ" |
	"REST" |
	"EQU" |
	"TEXTEQU" |
	"TIMES" |
	"DUP"		{ addToken(Token.FUNCTION); }

	"BYTE" |
	"HALF" |
	"WORD" |
	"FLOAT" |
	"DOUBLE" |
	"ASCII" |
	"ASCIIZ" |
	"SPACE"		{ addToken(Token.DATA_TYPE); }

	/* Registers */
	"$0" |
	"$1" |
	"$2" |
	"$3" |
	"$4" |
	"$5" |
	"$6" |
	"$7" |
	"$8" |
	"$9" |
	"$10" |
	"$11" |
	"$12" |
	"$13" |
	"$14" |
	"$15" |
	"$16" |
	"$17" |
	"$18" |
	"$19" |
	"$20" |
	"$21" |
	"$22" |
	"$23" |
	"$24" |	
	"$25" |
	"$26" |
	"$27" |
	"$28" |
	"$29" |
	"$30" |
	"$31" |
	"$zero" |
	"$at" |
	"$v0" |
	"$v1" |
	"$a0" |
	"$a1" |
	"$a2" |
	"$a3" |
	"$t0" |
	"$t1" |
	"$t2" |
	"$t3" |
	"$t4" |
	"$t5" |
	"$t6" |
	"$t7" |
	"$s0" |
	"$s1" |
	"$s2" |
	"$s3" |
	"$s4" |
	"$s5" |
	"$s6" |
	"$s7" |
	"$t8" |
	"$t9" |
	"$k0" |
	"$k1" |
	"$gp" |
	"$sp" |
	"$fp" |
	"$ra"		{ addToken(Token.VARIABLE); }

	/* MIPS Instructions. */
	"add" |
	"addu" |
	"sub" |
	"subu" |
	"and" |
	"mult" |
	"multu" |
	"div" |
	"divu" |
	"mfhi" |
	"mflo" |
	"mthi" |
	"mtlo" |
	"mfc0" |
	"mtc0" |
	"or" |
	"xor" |
	"nor" |
	"slt" |
	"sltu" |
	"sll" |
	"srl" |
	"sra" |
	"sllv" |
	"srlv" |
	"srav" |
	"jr" |
	"jalr" |
	"break" |
	"syscall" |
	"eret" |
	"addi" |
	"addiu" |
	"andi" |
	"ori" |
	"xori" |
	"lui" |
	"lb" |
	"lbu" |
	"lh" |
	"lhu" |
	"sb" |
	"sh" |
	"lw" |
	"sw" |
	"beq" |
	"bne" |
	"bgez" |
	"bgtz" |
	"blez" |
	"bltz" |
	"bgezal" |
	"slti" |
	"sltiu" |
	"j" |
	"jal" |
	"push" |
	"pop" |
	"adr"		{ addToken(Token.RESERVED_WORD); }

}

<YYINITIAL> {

	{LineTerminator}				{ addNullToken(); return firstToken; }

	{WhiteSpace}+					{ addToken(Token.WHITESPACE); }

	/* String/Character Literals. */
	{CharLiteral}					{ addToken(Token.LITERAL_CHAR); }
	{UnclosedCharLiteral}			{ addToken(Token.ERROR_CHAR); /*addNullToken(); return firstToken;*/ }
	{StringLiteral}				{ addToken(Token.LITERAL_STRING_DOUBLE_QUOTE); }
	{UnclosedStringLiteral}			{ addToken(Token.ERROR_STRING_DOUBLE); addNullToken(); return firstToken; }

	/* Labels. */
	{Label}						{ addToken(Token.PREPROCESSOR); }

	^%({Letter}|{Digit})*			{ addToken(Token.FUNCTION); }

	/* Comment Literals. */
	{CommentBegin}.*				{ addToken(Token.COMMENT_EOL); addNullToken(); return firstToken; }

	/* Operators. */
	{Operator}					{ addToken(Token.OPERATOR); }

	/* Numbers */
	{Number}						{ addToken(Token.LITERAL_NUMBER_DECIMAL_INT); }

	/* Ended with a line not in a string or comment. */
	<<EOF>>						{ addNullToken(); return firstToken; }

	/* Catch any other (unhandled) characters. */
	{Identifier}					{ addToken(Token.IDENTIFIER); }
	.							{ addToken(Token.IDENTIFIER); }

}
