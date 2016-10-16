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
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class MainFrame extends JFrame implements ActionListener {
	/**
	 * 
	 */
	JTabbedPane tb;
	ArrayList<ContentPane> array = new ArrayList<>();
	JMenu[] menus;
	JMenuItem[] fileItems;
	JMenuItem[] editItems;
	JMenuItem[] compileItems;
	JMenuItem[] helpItems;
	LinkedList<ContentPane> contentlist = new LinkedList<>();
	JFileChooser chooser;

	private static final long serialVersionUID = 1L;
	private JTextField search;

	public MainFrame() {
		// setFont(new Font("华文宋体", Font.PLAIN, 15));
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

		menus = new JMenu[] { new JMenu("文件"), new JMenu("编辑"), new JMenu("编译"), new JMenu("帮助") };
		fileItems = new JMenuItem[] { new JMenuItem("新建"), new JMenuItem("打开"), new JMenuItem("保存"),
				new JMenuItem("另存为"), new JMenuItem("退出") };
		editItems = new JMenuItem[] { new JMenuItem("复制"), new JMenuItem("粘贴"), new JMenuItem("撤销") };
		compileItems = new JMenuItem[] { new JMenuItem("编译"), new JMenuItem("添加词法"), new JMenuItem("添加文法"),
				new JMenuItem("make") };
		helpItems = new JMenuItem[] { new JMenuItem("关于") };
		chooser = new JFileChooser();
		setAccelerator();
		init();

		getContentPane().setLayout(new BorderLayout(0, 0));
		tb = new JTabbedPane();
		getContentPane().add(tb, BorderLayout.CENTER);

		ContentPane content = createContentPane();
		tb.addTab("New tab", null, content, null);

		JMenuBar menuBar_1 = new JMenuBar();
		menuBar_1.setLayout(new GridLayout(1, 3));
		getContentPane().add(menuBar_1, BorderLayout.SOUTH);

		search = new JTextField();
		menuBar_1.add(search);
		search.setColumns(10);

		JMenuItem status = new JMenuItem("status:");
		menuBar_1.add(status);

	}

	public static void main(String[] args) {
		new MainFrame();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		String commond = e.getActionCommand();
		System.out.println("commond:" + commond);

		switch (commond) {
		case "新建":
			ContentPane pane = createContentPane();
			tb.addTab("New tab", null, pane, null);
			break;
		case "打开":
			chooser.showOpenDialog(this);
			File f = chooser.getSelectedFile();
			OPenFile(f);
			tb.setSelectedIndex(contentlist.size() - 1);
			break;
		case "保存":
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
		case "另存为":

			break;
		case "退出":

			break;
		case "编译":

			break;

		default:
			break;
		}
	}

	private void init() {

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		for (JMenuItem jMItem : fileItems) {
			if (jMItem.getText().equals("退出")) {
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

		// JMenu file = new JMenu("文件");
		// menuBar.add(file);
		// JMenuItem newFile = new JMenuItem("新建");
		// newFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,
		// ActionEvent.CTRL_MASK));
		// newFile.setActionCommand("newFile");
		// newFile.addActionListener(this);
		// file.add(newFile);
		// JMenuItem openFile = new JMenuItem("打开");
		// openFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,
		// ActionEvent.CTRL_MASK));
		// openFile.setActionCommand("openFile");
		// openFile.addActionListener(this);
		// file.add(openFile);
		// JMenuItem save = new JMenuItem("保存");
		// save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
		// ActionEvent.CTRL_MASK));
		// save.setActionCommand("save");
		// save.addActionListener(this);
		// file.add(save);
		// JMenuItem saveAs = new JMenuItem("另存为");
		// saveAs.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
		// ActionEvent.ALT_MASK + ActionEvent.CTRL_MASK));
		// saveAs.setActionCommand("saveAs");
		// saveAs.addActionListener(this);
		// file.add(saveAs);
		// file.addSeparator();
		// JMenuItem exit = new JMenuItem("退出");
		// exit.setActionCommand("exit");
		// exit.addActionListener(this);
		// file.add(exit);
		//
		// JMenu edit = new JMenu("编辑");
		// menuBar.add(edit);
		//
		// JMenu tools = new JMenu("工具");
		// menuBar.add(tools);
		// JMenuItem compileFile = new JMenuItem("编译");
		// compileFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5,
		// ActionEvent.CTRL_MASK));
		// tools.add(compileFile);
		// JMenuItem addLex = new JMenuItem("添加词法");
		// tools.add(addLex);
		// JMenuItem addGrammer = new JMenuItem("添加文法");
		// tools.add(addGrammer);
		// JMenuItem make = new JMenuItem("make");
		// tools.add(make);
		//
		// JMenu help = new JMenu("帮助");
		// menuBar.add(help);
		// JMenuItem about = new JMenuItem("关于");
		// help.add(about);

	}

	private void setAccelerator() {
		fileItems[0].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
		fileItems[1].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
		fileItems[2].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		fileItems[3]
				.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.ALT_MASK + ActionEvent.CTRL_MASK));
		compileItems[0].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5, ActionEvent.CTRL_MASK));
	}

	private ContentPane createContentPane() {
		ContentPane pane = new ContentPane();
		contentlist.add(pane);
		return pane;
	}

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
}
