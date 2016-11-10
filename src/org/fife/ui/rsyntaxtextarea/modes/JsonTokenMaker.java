/* The following code was generated by JFlex 1.4.1 on 8/22/15 3:14 PM */

/*
 * 12/23/2012
 *
 * JsonTokenMaker.java - Scanner for JSON.
 *
 * This library is distributed under a modified BSD license.  See the included
 * RSyntaxTextArea.License.txt file for details.
 */
package org.fife.ui.rsyntaxtextarea.modes;

import java.io.*;
import javax.swing.text.Segment;

import org.fife.ui.rsyntaxtextarea.*;


/**
 * Scanner for JSON.<p>
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
 *   <li>The generated <code>JsonTokenMaker.java</code> file will contain two
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
 * @version 0.6
 *
 */

public class JsonTokenMaker extends AbstractJFlexTokenMaker {

  /** This character denotes the end of file */
  public static final int YYEOF = -1;

  /** lexical states */
  public static final int EOL_COMMENT = 1;
  public static final int YYINITIAL = 0;

  /**
   * Translates characters to character classes
   */
  private static final String ZZ_CMAP_PACKED =
    "\11\0\1\1\1\10\1\0\1\1\23\0\1\1\1\3\1\11\1\3"+
    "\1\2\1\3\5\3\1\21\1\6\1\7\1\17\1\16\12\4\1\32"+
    "\1\3\1\0\1\3\1\0\2\3\4\5\1\20\1\5\24\2\1\33"+
    "\1\13\1\33\1\0\1\3\1\0\1\26\1\15\2\5\1\24\1\25"+
    "\1\2\1\34\1\36\2\2\1\27\1\2\1\14\1\2\1\35\1\2"+
    "\1\23\1\30\1\22\1\12\1\2\1\37\3\2\1\31\1\0\1\31"+
    "\1\3\uff81\0";

  /**
   * Translates characters to character classes
   */
  private static final char [] ZZ_CMAP = zzUnpackCMap(ZZ_CMAP_PACKED);

  /**
   * Translates DFA states to action switch labels.
   */
  private static final int [] ZZ_ACTION = zzUnpackAction();

  private static final String ZZ_ACTION_PACKED_0 =
    "\2\0\1\1\1\2\1\3\2\1\1\4\4\1\1\5"+
    "\1\6\1\7\3\6\2\0\1\4\1\10\1\4\1\1"+
    "\1\11\2\1\4\0\2\12\1\0\1\13\1\4\1\14"+
    "\1\4\3\1\4\0\1\4\1\15\1\16\2\0\1\17"+
    "\1\4\2\0\1\4";

  private static int [] zzUnpackAction() {
    int [] result = new int[55];
    int offset = 0;
    offset = zzUnpackAction(ZZ_ACTION_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackAction(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /**
   * Translates a state to a row index in the transition table
   */
  private static final int [] ZZ_ROWMAP = zzUnpackRowMap();

  private static final String ZZ_ROWMAP_PACKED_0 =
    "\0\0\0\40\0\100\0\140\0\200\0\240\0\300\0\340"+
    "\0\u0100\0\u0120\0\u0140\0\u0160\0\240\0\u0180\0\240\0\u01a0"+
    "\0\u01c0\0\u01e0\0\u0200\0\u0220\0\u0240\0\u0260\0\u0280\0\u02a0"+
    "\0\240\0\u02c0\0\u02e0\0\u0300\0\u0320\0\u0340\0\u0360\0\u0380"+
    "\0\u03a0\0\u03a0\0\240\0\u03c0\0\240\0\u03e0\0\u0400\0\u0420"+
    "\0\u0440\0\u0460\0\u0480\0\u04a0\0\u04c0\0\u04e0\0\100\0\100"+
    "\0\u0500\0\u0520\0\u0540\0\u0560\0\u0580\0\u0540\0\u05a0";

  private static int [] zzUnpackRowMap() {
    int [] result = new int[55];
    int offset = 0;
    offset = zzUnpackRowMap(ZZ_ROWMAP_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackRowMap(String packed, int offset, int [] result) {
    int i = 0;  /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int high = packed.charAt(i++) << 16;
      result[j++] = high | packed.charAt(i++);
    }
    return j;
  }

  /**
   * The transition table of the DFA
   */
  private static final int [] ZZ_TRANS = zzUnpackTrans();

  private static final String ZZ_TRANS_PACKED_0 =
    "\1\3\1\4\2\3\1\5\1\3\1\6\1\7\1\3"+
    "\1\10\2\3\1\11\1\3\1\12\3\3\1\13\2\3"+
    "\1\14\3\3\1\15\1\6\1\15\4\3\10\16\1\17"+
    "\14\16\1\20\6\16\1\21\2\16\1\22\1\3\1\0"+
    "\4\3\1\0\2\3\1\0\4\3\1\0\12\3\3\0"+
    "\4\3\1\0\1\4\42\0\1\5\12\0\1\23\1\24"+
    "\3\0\1\24\57\0\1\5\33\0\10\10\1\25\1\26"+
    "\1\10\1\27\24\10\1\3\1\0\4\3\1\0\2\3"+
    "\1\0\1\30\3\3\1\0\12\3\3\0\4\3\16\0"+
    "\1\31\21\0\1\3\1\0\4\3\1\0\2\3\1\0"+
    "\4\3\1\0\4\3\1\32\5\3\3\0\5\3\1\0"+
    "\4\3\1\0\2\3\1\0\4\3\1\0\7\3\1\33"+
    "\2\3\3\0\4\3\10\16\1\0\14\16\1\0\6\16"+
    "\1\0\2\16\23\0\1\34\13\0\1\35\23\0\1\36"+
    "\54\0\1\37\4\0\1\40\37\0\1\41\2\0\1\42"+
    "\11\0\1\42\16\0\11\25\1\43\1\25\1\44\24\25"+
    "\32\0\1\45\5\0\10\25\1\0\1\10\1\46\4\10"+
    "\3\25\2\10\1\25\1\10\12\25\1\3\1\0\4\3"+
    "\1\0\2\3\1\0\4\3\1\0\10\3\1\47\1\3"+
    "\3\0\5\3\1\0\4\3\1\0\2\3\1\0\1\50"+
    "\3\3\1\0\12\3\3\0\5\3\1\0\4\3\1\0"+
    "\2\3\1\0\4\3\1\0\10\3\1\51\1\3\3\0"+
    "\4\3\35\0\1\52\31\0\1\53\32\0\1\54\54\0"+
    "\1\55\4\0\1\40\13\0\1\24\3\0\1\24\17\0"+
    "\1\41\33\0\10\25\1\0\33\25\2\56\3\25\1\43"+
    "\1\25\1\44\1\25\1\56\2\25\1\56\3\25\3\56"+
    "\11\25\1\3\1\0\4\3\1\0\2\3\1\0\4\3"+
    "\1\0\10\3\1\57\1\3\3\0\5\3\1\0\4\3"+
    "\1\0\2\3\1\0\4\3\1\0\5\3\1\60\4\3"+
    "\3\0\5\3\1\0\4\3\1\0\2\3\1\0\4\3"+
    "\1\0\11\3\1\50\3\0\4\3\32\0\1\61\31\0"+
    "\1\52\50\0\1\62\21\0\1\63\20\0\4\25\2\64"+
    "\3\25\1\43\1\25\1\44\1\25\1\64\2\25\1\64"+
    "\3\25\3\64\11\25\16\0\1\65\51\0\1\52\1\0"+
    "\1\61\7\0\1\63\1\66\2\63\2\66\2\0\1\63"+
    "\1\0\3\63\1\66\1\63\1\66\7\63\1\0\2\66"+
    "\4\63\4\25\2\67\3\25\1\43\1\25\1\44\1\25"+
    "\1\67\2\25\1\67\3\25\3\67\11\25\16\0\1\63"+
    "\21\0\4\25\2\10\3\25\1\43\1\25\1\44\1\25"+
    "\1\10\2\25\1\10\3\25\3\10\11\25";

  private static int [] zzUnpackTrans() {
    int [] result = new int[1472];
    int offset = 0;
    offset = zzUnpackTrans(ZZ_TRANS_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackTrans(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      value--;
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /* error codes */
  private static final int ZZ_UNKNOWN_ERROR = 0;
  private static final int ZZ_NO_MATCH = 1;
  private static final int ZZ_PUSHBACK_2BIG = 2;

  /* error messages for the codes above */
  private static final String ZZ_ERROR_MSG[] = {
    "Unkown internal scanner error",
    "Error: could not match input",
    "Error: pushback value was too large"
  };

  /**
   * ZZ_ATTRIBUTE[aState] contains the attributes of state <code>aState</code>
   */
  private static final int [] ZZ_ATTRIBUTE = zzUnpackAttribute();

  private static final String ZZ_ATTRIBUTE_PACKED_0 =
    "\2\0\3\1\1\11\6\1\1\11\1\1\1\11\3\1"+
    "\2\0\1\1\1\3\2\1\1\11\2\1\4\0\2\1"+
    "\1\0\1\11\1\1\1\15\4\1\4\0\3\1\2\0"+
    "\2\1\2\0\1\1";

  private static int [] zzUnpackAttribute() {
    int [] result = new int[55];
    int offset = 0;
    offset = zzUnpackAttribute(ZZ_ATTRIBUTE_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackAttribute(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }

  /** the input device */
  private java.io.Reader zzReader;

  /** the current state of the DFA */
  private int zzState;

  /** the current lexical state */
  private int zzLexicalState = YYINITIAL;

  /** this buffer contains the current text to be matched and is
      the source of the yytext() string */
  private char zzBuffer[];

  /** the textposition at the last accepting state */
  private int zzMarkedPos;

  /** the textposition at the last state to be included in yytext */
  private int zzPushbackPos;

  /** the current text position in the buffer */
  private int zzCurrentPos;

  /** startRead marks the beginning of the yytext() string in the buffer */
  private int zzStartRead;

  /** endRead marks the last character in the buffer, that has been read
      from input */
  private int zzEndRead;

  /** zzAtEOF == true <=> the scanner is at the EOF */
  private boolean zzAtEOF;

  /* user code: */

	private boolean highlightEolComments;


	/**
	 * Constructor.  This must be here because JFlex does not generate a
	 * no-parameter constructor.
	 */
	public JsonTokenMaker() {
	}


	/**
	 * Adds the token specified to the current linked list of tokens.
	 *
	 * @param tokenType The token's type.
	 * @see #addToken(int, int, int)
	 */
	private void addHyperlinkToken(int start, int end, int tokenType) {
		int so = start + offsetShift;
		addToken(zzBuffer, start,end, tokenType, so, true);
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
		addToken(zzBuffer, start,end, tokenType, so, false);
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
	 * @param hyperlink Whether this token is a hyperlink.
	 */
	@Override
	public void addToken(char[] array, int start, int end, int tokenType,
						int startOffset, boolean hyperlink) {
		super.addToken(array, start,end, tokenType, startOffset, hyperlink);
		zzStartRead = zzMarkedPos;
	}


	/**
	 * Returns <code>true</code> always as C-style languages use curly braces
	 * to denote code blocks.
	 *
	 * @return <code>true</code> always.
	 */
	public boolean getCurlyBracesDenoteCodeBlocks() {
		return true;
	}


	@Override
	public boolean getMarkOccurrencesOfTokenType(int type) {
		return false;
	}


	@Override
	public boolean getShouldIndentNextLineAfter(Token t) {
		if (t!=null && t.length()==1) {
			char ch = t.charAt(0);
			return ch=='{' || ch=='[';
		}
		return false;
	}


	/**
	 * Returns the first token in the linked list of tokens generated
	 * from <code>text</code>.  This method must be implemented by
	 * subclasses so they can correctly implement syntax highlighting.
	 *
	 * @param text The text from which to get tokens.
	 * @param initialTokenType The token type we should start with.
	 * @param startOffset The offset into the document at which
	 *        <code>text</code> starts.
	 * @return The first <code>Token</code> in a linked list representing
	 *         the syntax highlighted text.
	 */
	@Override
	public Token getTokenList(Segment text, int initialTokenType, int startOffset) {

		resetTokenList();
		this.offsetShift = -text.offset + startOffset;

		// Start off in the proper state.
		int state = YYINITIAL;
		start = text.offset;

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


	protected void setHighlightEolComments(boolean highlightEolComments) {
		this.highlightEolComments = highlightEolComments;
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
		zzCurrentPos = zzMarkedPos  = s.offset;
		zzLexicalState = YYINITIAL;
		zzReader = reader;
		zzAtEOF  = false;
	}




  /**
   * Creates a new scanner
   * There is also a java.io.InputStream version of this constructor.
   *
   * @param   in  the java.io.Reader to read input from.
   */
  public JsonTokenMaker(java.io.Reader in) {
    this.zzReader = in;
  }

  /**
   * Creates a new scanner.
   * There is also java.io.Reader version of this constructor.
   *
   * @param   in  the java.io.Inputstream to read input from.
   */
  public JsonTokenMaker(java.io.InputStream in) {
    this(new java.io.InputStreamReader(in));
  }

  /**
   * Unpacks the compressed character translation table.
   *
   * @param packed   the packed character translation table
   * @return         the unpacked character translation table
   */
  private static char [] zzUnpackCMap(String packed) {
    char [] map = new char[0x10000];
    int i = 0;  /* index in packed string  */
    int j = 0;  /* index in unpacked array */
    while (i < 124) {
      int  count = packed.charAt(i++);
      char value = packed.charAt(i++);
      do map[j++] = value; while (--count > 0);
    }
    return map;
  }


  /**
   * Closes the input stream.
   */
  public final void yyclose() throws java.io.IOException {
    zzAtEOF = true;            /* indicate end of file */
    zzEndRead = zzStartRead;  /* invalidate buffer    */

    if (zzReader != null)
      zzReader.close();
  }


  /**
   * Returns the current lexical state.
   */
  public final int yystate() {
    return zzLexicalState;
  }


  /**
   * Enters a new lexical state
   *
   * @param newState the new lexical state
   */
  @Override
  public final void yybegin(int newState) {
    zzLexicalState = newState;
  }


  /**
   * Returns the text matched by the current regular expression.
   */
  public final String yytext() {
    return new String( zzBuffer, zzStartRead, zzMarkedPos-zzStartRead );
  }


  /**
   * Returns the character at position <tt>pos</tt> from the
   * matched text.
   *
   * It is equivalent to yytext().charAt(pos), but faster
   *
   * @param pos the position of the character to fetch.
   *            A value from 0 to yylength()-1.
   *
   * @return the character at position pos
   */
  public final char yycharat(int pos) {
    return zzBuffer[zzStartRead+pos];
  }


  /**
   * Returns the length of the matched text region.
   */
  public final int yylength() {
    return zzMarkedPos-zzStartRead;
  }


  /**
   * Reports an error that occured while scanning.
   *
   * In a wellformed scanner (no or only correct usage of
   * yypushback(int) and a match-all fallback rule) this method
   * will only be called with things that "Can't Possibly Happen".
   * If this method is called, something is seriously wrong
   * (e.g. a JFlex bug producing a faulty scanner etc.).
   *
   * Usual syntax/scanner level error handling should be done
   * in error fallback rules.
   *
   * @param   errorCode  the code of the errormessage to display
   */
  private void zzScanError(int errorCode) {
    String message;
    try {
      message = ZZ_ERROR_MSG[errorCode];
    }
    catch (ArrayIndexOutOfBoundsException e) {
      message = ZZ_ERROR_MSG[ZZ_UNKNOWN_ERROR];
    }

    throw new Error(message);
  }


  /**
   * Pushes the specified amount of characters back into the input stream.
   *
   * They will be read again by then next call of the scanning method
   *
   * @param number  the number of characters to be read again.
   *                This number must not be greater than yylength()!
   */
  public void yypushback(int number)  {
    if ( number > yylength() )
      zzScanError(ZZ_PUSHBACK_2BIG);

    zzMarkedPos -= number;
  }


  /**
   * Resumes scanning until the next regular expression is matched,
   * the end of input is encountered or an I/O-Error occurs.
   *
   * @return      the next token
   * @exception   java.io.IOException  if any I/O-Error occurs
   */
  public org.fife.ui.rsyntaxtextarea.Token yylex() throws java.io.IOException {
    int zzInput;
    int zzAction;

    // cached fields:
    int zzCurrentPosL;
    int zzMarkedPosL;
    int zzEndReadL = zzEndRead;
    char [] zzBufferL = zzBuffer;
    char [] zzCMapL = ZZ_CMAP;

    int [] zzTransL = ZZ_TRANS;
    int [] zzRowMapL = ZZ_ROWMAP;
    int [] zzAttrL = ZZ_ATTRIBUTE;
    int zzPushbackPosL = zzPushbackPos = -1;
    boolean zzWasPushback;

    while (true) {
      zzMarkedPosL = zzMarkedPos;

      zzAction = -1;

      zzCurrentPosL = zzCurrentPos = zzStartRead = zzMarkedPosL;

      zzState = zzLexicalState;

      zzWasPushback = false;

      zzForAction: {
        while (true) {

          if (zzCurrentPosL < zzEndReadL)
            zzInput = zzBufferL[zzCurrentPosL++];
          else if (zzAtEOF) {
            zzInput = YYEOF;
            break zzForAction;
          }
          else {
            // store back cached positions
            zzCurrentPos  = zzCurrentPosL;
            zzMarkedPos   = zzMarkedPosL;
            zzPushbackPos = zzPushbackPosL;
            boolean eof = zzRefill();
            // get translated positions and possibly new buffer
            zzCurrentPosL  = zzCurrentPos;
            zzMarkedPosL   = zzMarkedPos;
            zzBufferL      = zzBuffer;
            zzEndReadL     = zzEndRead;
            zzPushbackPosL = zzPushbackPos;
            if (eof) {
              zzInput = YYEOF;
              break zzForAction;
            }
            else {
              zzInput = zzBufferL[zzCurrentPosL++];
            }
          }
          int zzNext = zzTransL[ zzRowMapL[zzState] + zzCMapL[zzInput] ];
          if (zzNext == -1) break zzForAction;
          zzState = zzNext;

          int zzAttributes = zzAttrL[zzState];
          if ( (zzAttributes & 2) == 2 )
            zzPushbackPosL = zzCurrentPosL;

          if ( (zzAttributes & 1) == 1 ) {
            zzWasPushback = (zzAttributes & 4) == 4;
            zzAction = zzState;
            zzMarkedPosL = zzCurrentPosL;
            if ( (zzAttributes & 8) == 8 ) break zzForAction;
          }

        }
      }

      // store back cached position
      zzMarkedPos = zzMarkedPosL;
      if (zzWasPushback)
        zzMarkedPos = zzPushbackPosL;

      switch (zzAction < 0 ? zzAction : ZZ_ACTION[zzAction]) {
        case 13:
          { addToken(Token.RESERVED_WORD);
          }
        case 16: break;
        case 1:
          { addToken(Token.IDENTIFIER);
          }
        case 17: break;
        case 10:
          { addToken(Token.LITERAL_NUMBER_FLOAT);
          }
        case 18: break;
        case 8:
          { addToken(Token.LITERAL_STRING_DOUBLE_QUOTE);
          }
        case 19: break;
        case 12:
          { addToken(Token.VARIABLE);
          }
        case 20: break;
        case 9:
          { if (highlightEolComments) {
										start = zzMarkedPos-2; yybegin(EOL_COMMENT);
									}
									else {
										addToken(Token.IDENTIFIER);
									}
          }
        case 21: break;
        case 2:
          { addToken(Token.WHITESPACE);
          }
        case 22: break;
        case 15:
          { int temp=zzStartRead; addToken(start,zzStartRead-1, Token.COMMENT_EOL); addHyperlinkToken(temp,zzMarkedPos-1, Token.COMMENT_EOL); start = zzMarkedPos;
          }
        case 23: break;
        case 3:
          { addToken(Token.LITERAL_NUMBER_DECIMAL_INT);
          }
        case 24: break;
        case 14:
          { addToken(Token.LITERAL_BOOLEAN);
          }
        case 25: break;
        case 4:
          { addToken(Token.ERROR_STRING_DOUBLE); addNullToken(); return firstToken;
          }
        case 26: break;
        case 7:
          { addToken(start,zzStartRead-1, Token.COMMENT_EOL); addNullToken(); return firstToken;
          }
        case 27: break;
        case 11:
          { addToken(Token.ERROR_STRING_DOUBLE);
          }
        case 28: break;
        case 6:
          {
          }
        case 29: break;
        case 5:
          { addToken(Token.SEPARATOR);
          }
        case 30: break;
        default:
          if (zzInput == YYEOF && zzStartRead == zzCurrentPos) {
            zzAtEOF = true;
            switch (zzLexicalState) {
            case EOL_COMMENT: {
              addToken(start,zzStartRead-1, Token.COMMENT_EOL); addNullToken(); return firstToken;
            }
            case 56: break;
            case YYINITIAL: {
              addNullToken(); return firstToken;
            }
            case 57: break;
            default:
            return null;
            }
          }
          else {
            zzScanError(ZZ_NO_MATCH);
          }
      }
    }
  }


}
