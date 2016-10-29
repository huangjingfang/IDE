package GUI;

import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleConstants.FontConstants;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

import utilities.DataUtil;

// ����༭��panel
public class CodePane extends JTextPane {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	UndoManager undoManager;// ���������Ĺ�����
	SimpleAttributeSet purple;// �ؼ��ֵ����Լ�
	SimpleAttributeSet blue;// �����ڵ����Լ�
	SimpleAttributeSet lightGreen;// ע�͵����Լ�
	SimpleAttributeSet normal;// ������Ĭ�����Լ�
	public static SimpleAttributeSet find;//�������
	DefaultStyledDocument defaultStyle;// Ĭ�ϵ��ĵ�
	String keywords;// �ؼ��ֵ�������ʽ
	String isnote;// ע�͵�������ʽ
	String insideQuotation;// ���������ݵ�������ʽ

	public CodePane() {
		isnote = "(\\W)*\"(.)*\"";
		insideQuotation = "(\\W)*//";
		
		setKeyword();
		initAttributeSet();

		setDocument(defaultStyle);

		addListner();
	}

	// �����ؼ��ֵ�������ʽ����ɣ�
	private void setKeyword() {
		StringBuffer buffer = new StringBuffer("(\\W)*(");
		for (String s : DataUtil.keywords) {
			buffer.append(s);
			if(!s.equals(DataUtil.keywords[DataUtil.keywords.length-1])){
				buffer.append('|');
			}
		}
		buffer.append(")");
		keywords = buffer.toString();
		System.out.println(keywords);
	}

	// ��ʼ�������Լ��Լ�Ĭ���ı��ĵ�
	private void initAttributeSet() {
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

		defaultStyle = new DefaultStyledDocument() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void insertString(int offset, String str, AttributeSet a) throws BadLocationException {
				// TODO Auto-generated method stub
				switch (str) {
				case "\t":
					str = "   ";
					break;
				case "\"":
					str = "\"\"";
					break;
				case "(":
					str = "()";
					break;
				case "{":
					str = "{}";
					break;
				default:
					break;
				}
				super.insertString(offset, str, a);
				if (str.equals("\"\"") || str.equals("()") || str.equals("{}")) {
					setCaretPosition(getCaretPosition() - 1);
				}

				String text = CodePane.this.getText();

				int before = findLastNonWordChar(text, offset);
				if (before < 0)
					before = 0;
				int after = findFirstNonWordChar(text, offset + str.length());
				int wordL = before;
				int wordR = before;

				while (wordR <= after) {
					if (wordR == after || String.valueOf(text.charAt(wordR)).matches("\\W")) {
						if (text.substring(wordL, wordR).matches(keywords)){
							setCharacterAttributes(wordL, wordR - wordL, purple, false);
							System.out.println("Match keywords @ "+ wordL+"\tkeyword:"+text.substring(wordL, wordR));
						}
						else{
							setCharacterAttributes(wordL, wordR - wordL, normal, false);
						}
						wordL = wordR;
					}
					wordR++;

				}
				int begin=0;
				for(int i=0;i<text.length();i++){
					if(text.charAt(i)=='"'){
						if(begin==0){
							begin = i;
						}else{
							setCharacterAttributes(begin, i-begin, blue, true);
							begin = 0;
						}
					}
				}
			}

			@Override
			public void remove(int offs, int len) throws BadLocationException {
				// TODO Auto-generated method stub
				super.remove(offs, len);
				String text = getText(0, getLength());
				int before = findLastNonWordChar(text, offs);
				if (before < 0)
					before = 0;
				int after = findFirstNonWordChar(text, offs);

				if (text.substring(before, after).matches(keywords)) {
					setCharacterAttributes(before, after - before, purple, false);
				} else {
					setCharacterAttributes(before, after - before, normal, false);
				}
				
				int begin=0;
				for(int i=0;i<text.length();i++){
					if(text.charAt(i)=='"'){
						if(begin==0){
							begin = i;
						}else{
							setCharacterAttributes(begin, i-begin, blue, true);
							begin = 0;
						}
					}
				}
			}

		};
	}

	// ��ӳ����������ļ����Լ�����
	private void addListner() {
		// TODO Auto-generated method stub
		undoManager = new UndoManager();
		defaultStyle.addUndoableEditListener(new UndoableEditListener() {

			@Override
			public void undoableEditHappened(UndoableEditEvent e) {
				// TODO Auto-generated method stub
				undoManager.addEdit(e.getEdit());
			}
		});
		InputMap inp = getInputMap(JComponent.WHEN_FOCUSED);
		ActionMap aMap = getActionMap();

		inp.put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()), "Undo");
		inp.put(KeyStroke.getKeyStroke(KeyEvent.VK_Y, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()), "Redo");

		aMap.put("Undo", new AbstractAction() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					if (undoManager.canUndo())
						undoManager.undo();
				} catch (CannotUndoException e1) {
					e1.printStackTrace();
				}
			}
		});

		aMap.put("Redo", new AbstractAction() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					if (undoManager.canRedo())
						undoManager.redo();
				} catch (CannotRedoException e2) {
					e2.printStackTrace();
				}
			}
		});

		inp.put(KeyStroke.getKeyStroke(KeyEvent.VK_F, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()), "Find");

		aMap.put("Find", new AbstractAction() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// doSearch();
			}
		});
	}

	// �ҵ��ı������һ�����ַ�������
	private int findLastNonWordChar(String text, int index) {
		while (--index >= 0) {
			if (String.valueOf(text.charAt(index)).matches("\\W")) {
				break;
			}
		}
		return index;
	}

	// �ҵ��ı��д�index��ʼ�ĵ�һ�����ַ�������
	private int findFirstNonWordChar(String text, int index) {
		while (index < text.length()) {
			if (String.valueOf(text.charAt(index)).matches("\\W")) {
				break;
			}
			index++;
		}
		return index;
	}

}
