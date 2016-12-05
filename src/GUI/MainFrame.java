package GUI;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;
import org.fife.ui.rtextarea.SearchContext;
import org.fife.ui.rtextarea.SearchEngine;

import compiler.BackEndStruct;
import compiler.translation;
import utilities.DataUtil;

public class MainFrame extends JFrame implements ActionListener {
	/**
	 * 
	 */
	JTabbedPane tb;// tab��
	JMenu[] menus;// �ܲ˵�
	JMenuItem[] fileItems;// �ļ��˵��еĲ˵���
	JMenuItem[] editItems;// �༭�˵��еĲ˵���
	JMenuItem[] compileItems;// �༭�˵��еĲ˵���
	JMenuItem[] helpItems;// �����˵��еĲ˵���
	LinkedList<RTextScrollPane> contentlist = new LinkedList<>();// tab���е�����
	HashMap<RTextScrollPane, String> pathDic = new HashMap<>();// �洢ÿ��tab��·��;
	JFileChooser chooser;// �򿪻򱣴��ļ����ļ�ѡ����
	String searchContent;
	ArrayList<Integer> searchIndex;
	int currentIndex;
	translation tr;
	

	private static final long serialVersionUID = 1L;
	private JTextField search;

	// ���캯������������Ԫ��
	public MainFrame() {
		// setFont(new Font("��������", Font.PLAIN, 15));
		setIconImage(Toolkit.getDefaultToolkit()
				.getImage(MainFrame.class.getResource("/com/sun/javafx/scene/web/skin/Paste_16x16_JFX.png")));
		setTitle("Mini C IDE");

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		setSize(800, 600);

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		menus = new JMenu[] { new JMenu("�ļ�"), new JMenu("�༭"), new JMenu("����"), new JMenu("����") };
		fileItems = new JMenuItem[] { new JMenuItem("�½�"), new JMenuItem("��"), new JMenuItem("����"),
				new JMenuItem("���Ϊ"), new JMenuItem("����"), new JMenuItem("�ر�"), new JMenuItem("����"),
				new JMenuItem("�˳�") };
		editItems = new JMenuItem[] { new JMenuItem("����"), new JMenuItem("ճ��"), new JMenuItem("����") };
		compileItems = new JMenuItem[] { new JMenuItem("����"), new JMenuItem("��Ӵʷ�"), new JMenuItem("����ķ�"),
				new JMenuItem("make") };
		helpItems = new JMenuItem[] { new JMenuItem("����") };
		chooser = new JFileChooser();
		setAccelerator();
		init();

		getContentPane().setLayout(new BorderLayout(0, 0));
		tb = new JTabbedPane();
		getContentPane().add(tb, BorderLayout.CENTER);

		RTextScrollPane content = createContentPane(SyntaxConstants.SYNTAX_STYLE_NONE);
		pathDic.put(content, null);
		tb.addTab("New tab", null, content, null);

		JMenuBar menuBar_1 = new JMenuBar();
		menuBar_1.setLayout(new GridLayout(0, 5));
		getContentPane().add(menuBar_1, BorderLayout.SOUTH);

		search = new JTextField();
		search.setHorizontalAlignment(SwingConstants.LEFT);
		menuBar_1.add(search);
		search.setColumns(10);

		JMenuItem status = new JMenuItem("status:");
		menuBar_1.add(status);

	}

	public static void main(String[] args) {
		new MainFrame();
	}

	// ���Ƿ�������������˲˵��е���Щѡ�ʵ�ּ������¼�
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		String commond = e.getActionCommand();
		System.out.println("commond:" + commond);
		switch (commond) {
		case "�½�":
			RTextScrollPane pane = createContentPane(SyntaxConstants.SYNTAX_STYLE_NONE);
			pathDic.put(pane, null);
			tb.addTab("New tab", null, pane, null);
			tb.setSelectedIndex(contentlist.size() - 1);
			break;
		case "��":
			chooser.showOpenDialog(this);
			File f = chooser.getSelectedFile();
			String name = f.getName();
			String type = name.substring(name.lastIndexOf(".")+1,name.length());
			OPenFile(f,type);
			tb.setSelectedIndex(contentlist.size() - 1);
			break;
		case "����":
			String str = contentlist.get(tb.getSelectedIndex()).getTextArea().getText();
			String path = pathDic.get(contentlist.get(tb.getSelectedIndex()));
			File sfile;
			if (path == null) {
				chooser.showSaveDialog(this);
				sfile = chooser.getSelectedFile();
				if(sfile==null)
					break;
				path = sfile.getAbsolutePath();
				pathDic.put(contentlist.get(tb.getSelectedIndex()), path);
				tb.setTitleAt(tb.getSelectedIndex(), sfile.getName());
				saveFile(sfile, str);
			} else {
				sfile = new File(path);
				saveFile(sfile, str);
			}
			String t = sfile.getName().substring(sfile.getName().lastIndexOf(".")+1,sfile.getName().length());
			((RSyntaxTextArea)contentlist.get(tb.getSelectedIndex()).getTextArea()).setSyntaxEditingStyle("text/" + t);
			break;
		case "���Ϊ":
			String s = contentlist.get(tb.getSelectedIndex()).getTextArea().getText();
			chooser.showSaveDialog(this);
			File file = chooser.getSelectedFile();
			if(file==null)
				break;
			path = file.getAbsolutePath();
			pathDic.put(contentlist.get(tb.getSelectedIndex()), path);
			tb.setTitleAt(tb.getSelectedIndex(), file.getName());
			saveFile(file, s);
			String st = file.getName().substring(file.getName().lastIndexOf(".")+1,file.getName().length());
			((RSyntaxTextArea)contentlist.get(tb.getSelectedIndex()).getTextArea()).setSyntaxEditingStyle("text/" + st);
			//OPenFile(file, t);
			break;
		case "����":
			searchContent = JOptionPane.showInputDialog(this, "������Ҫ����������:");
			System.out.println(searchContent);
			if(searchContent==null)
				break;
			SearchContext searchContext = new SearchContext(searchContent);
			SearchEngine.find(contentlist.get(tb.getSelectedIndex()).getTextArea(), searchContext);
			searchIndex = search(searchContent);
			break;
		case "�˳�":
			System.exit(0);
			break;
		case "����":
			SwingUtilities.invokeLater(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					String text = contentlist.get(tb.getSelectedIndex()).getTextArea().getText();
					String[] lexs = DataUtil.divide(text);
//					for(String st:lexs){
//						System.out.println(st);
//					}
					tr = new translation(lexs);
					try {
						BackEndStruct bct = new BackEndStruct();
						bct.setLex(tr.getLex());
						System.out.println("is variTable in bct null?"+ (bct.variTable==null));
						bct.genCode("IntermediateCode.data", "instructions.data");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
			break;
		case "�ر�":
			contentlist.remove(tb.getSelectedComponent());
			pathDic.remove(tb.getSelectedComponent());
			tb.remove(tb.getSelectedIndex());

			break;
		default:
			break;
		}
	}

	// ��ʼ�����������˵���Ԫ���뵽�˵�������ʾ
	private void init() {

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		for (JMenuItem jMItem : fileItems) {
			if (jMItem.getText().equals("�˳�")) {
				menus[0].addSeparator();
			}
			menus[0].add(jMItem);
			jMItem.setActionCommand(jMItem.getText());
			jMItem.addActionListener(this);
		}
		for (JMenuItem jItem : editItems) {
			menus[1].add(jItem);
			jItem.setActionCommand(jItem.getText());
			jItem.addActionListener(this);
		}
		for (JMenuItem jItem : compileItems) {
			menus[2].add(jItem);
			jItem.setActionCommand(jItem.getText());
			jItem.addActionListener(this);
		}
		for (JMenuItem jItem : helpItems) {
			menus[3].add(jItem);
			jItem.setActionCommand(jItem.getName());
			jItem.addActionListener(this);
		}
		for (JMenu jMenu : menus) {
			menuBar.add(jMenu);
		}

	}

	// ���ÿ�ݼ�
	private void setAccelerator() {
		fileItems[0].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
		fileItems[1].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
		fileItems[2].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		fileItems[3]
				.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.ALT_MASK + ActionEvent.CTRL_MASK));
		fileItems[4].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, ActionEvent.CTRL_MASK));
		fileItems[5].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.CTRL_MASK));
		compileItems[0].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5, ActionEvent.CTRL_MASK));
	}

	// ����һ��tab������tab���뵽һ��list�б��ڹ���
//	private RTextScrollPane createContentPane() {
//		RSyntaxTextArea pane = new RSyntaxTextArea();
//		pane.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
//		pane.setCodeFoldingEnabled(true);
//		RTextScrollPane sp = new RTextScrollPane(pane);
//		contentlist.add(sp);
//		return sp;
//	}

	// ����һ��tab�����ָ�tab�ļ��ĺ�׺�Ӷ��򿪶�Ӧ���﷨�������ܣ�����tab���뵽һ��list�б��ڹ���
	private RTextScrollPane createContentPane(String language) {
		RSyntaxTextArea pane = new RSyntaxTextArea();
		System.out.println("text/" + language);
		pane.setSyntaxEditingStyle("text/" + language);
		pane.setCodeFoldingEnabled(true);
		RTextScrollPane sp = new RTextScrollPane(pane);
		contentlist.add(sp);
		return sp;
	}

	// ���ļ������ļ�f�е�������ʾ��һ���µ�tab��
	private void OPenFile(File f,String type) {
		try {
			@SuppressWarnings("resource")
			BufferedReader reader = new BufferedReader(new FileReader(f));
			StringBuilder strb = new StringBuilder();
			String title = f.getName();
			String path = f.getCanonicalPath();

			while (reader.ready()) {
				strb.append(reader.readLine()).append("\n");
			}

			RTextScrollPane pane = createContentPane(type);
			pathDic.put(pane, path);
			tb.addTab(title, pane);
			pane.getTextArea().setText(strb.toString());
			// pane.setText(strb.toString());
			// pane.setPath(path);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// ��strд���ļ�f
	private void saveFile(File f, String str) {
		
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(f));
			writer.write(str);
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// ����ָ������
	public ArrayList<Integer> search(String s) {
		ArrayList<Integer> index = new ArrayList<>();
		String rex = "(\\w|\\W)*" + s;
		int length = s.length();
		// String text = contentlist.get(tb.getSelectedIndex()).getText();
		String text = "default";
		for (int i = 0; i <= text.length(); i++) {
			if (text.substring(0, i).matches(rex)) {
				index.add(i);
				// contentlist.get(tb.getSelectedIndex()).codePane.defaultStyle.setCharacterAttributes(i-length,
				// length,DataUtil.find, false);
			}
		}
		return index;
	}
}
