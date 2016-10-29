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
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

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
	LinkedList<ContentPane> contentlist = new LinkedList<>();// tab���е�����
	JFileChooser chooser;// �򿪻򱣴��ļ����ļ�ѡ����
	String searchContent;
	ArrayList<Integer> searchIndex;
	int currentIndex;

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
				new JMenuItem("���Ϊ"),new JMenuItem("����"), new JMenuItem("�ر�"), new JMenuItem("����"), new JMenuItem("�˳�") };
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

		ContentPane content = createContentPane();
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
			ContentPane pane = createContentPane();
			tb.addTab("New tab", null, pane, null);
			tb.setSelectedIndex(contentlist.size() - 1);
			break;
		case "��":
			chooser.showOpenDialog(this);
			File f = chooser.getSelectedFile();
			OPenFile(f);
			tb.setSelectedIndex(contentlist.size() - 1);
			break;
		case "����":
			String str = contentlist.get(tb.getSelectedIndex()).getText();
			String path = contentlist.get(tb.getSelectedIndex()).getPath();
			if (path == null) {
				chooser.showOpenDialog(this);
				File file = chooser.getSelectedFile();
				saveFile(file, str);
			} else {
				File file = new File(path);
				saveFile(file, str);
			}
			break;
		case "���Ϊ":
			String s = contentlist.get(tb.getSelectedIndex()).getText();
			chooser.showOpenDialog(this);
			File file = chooser.getSelectedFile();
			saveFile(file, s);
			break;
		case "����":
			//Search search = Search.getInstance();
			searchContent = JOptionPane.showInputDialog(this, "������Ҫ����������:");
			searchIndex = search(searchContent);
			int index = searchIndex.get(0);
			for(int a:searchIndex){
				System.out.print(a+"\t");
			}
			contentlist.get(tb.getSelectedIndex()).codePane.setCaretPosition(index);
			break;
			//search.setVisible(true);
		case "�˳�":
			System.exit(0);
			break;
		case "����":

			break;
		case "�ر�":
			contentlist.remove(tb.getSelectedComponent());
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
	private ContentPane createContentPane() {
		ContentPane pane = new ContentPane();
		contentlist.add(pane);
		return pane;
	}

	// ���ļ������ļ�f�е�������ʾ��һ���µ�tab��
	private void OPenFile(File f) {
		try {
			@SuppressWarnings("resource")
			BufferedReader reader = new BufferedReader(new FileReader(f));
			StringBuilder strb = new StringBuilder();
			String title = f.getName();
			String path = f.getCanonicalPath();

			while (reader.ready()) {
				strb.append(reader.readLine()).append("\n");
			}

			ContentPane pane = createContentPane();
			tb.addTab(title, pane);
			pane.setText(strb.toString());
			pane.setPath(path);
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
	//����ָ������
	public ArrayList<Integer> search(String s){
		ArrayList<Integer> index = new ArrayList<>();
		String rex = "(\\w|\\W)*"+s;
		int length = s.length();
		String text = contentlist.get(tb.getSelectedIndex()).getText();
		for(int i=0;i<=text.length();i++){
			if(text.substring(0, i).matches(rex)){
				index.add(i);
				contentlist.get(tb.getSelectedIndex()).codePane.defaultStyle.setCharacterAttributes(i-length, length,CodePane.find, false);
			}
		}
		return index;
	}
}
