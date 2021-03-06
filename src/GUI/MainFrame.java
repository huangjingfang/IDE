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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
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
import mipsAssembler.AssembleContext;
import utilities.DataUtil;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextPane;
import java.awt.Color;

public class MainFrame extends JFrame implements ActionListener {
	/**
	 * 
	 */
	JTabbedPane tb;// tab项
	JMenu[] menus;// 总菜单
	JMenuItem[] fileItems;// 文件菜单中的菜单项
	JMenuItem[] editItems;// 编辑菜单中的菜单项
	JMenuItem[] compileItems;// 编辑菜单中的菜单项
	JMenuItem[] helpItems;// 帮助菜单中的菜单项
	LinkedList<RTextScrollPane> contentlist = new LinkedList<>();// tab项中的内容
	HashMap<RTextScrollPane, String> pathDic = new HashMap<>();// 存储每个tab的路径;
	JFileChooser chooser;// 打开或保存文件的文件选择器
	String searchContent;
	ArrayList<Integer> searchIndex;
	int currentIndex;
	translation tr;
//	public JLabel status;
	public JTextPane textPane;
	

	private static final long serialVersionUID = 1L;
//	private JTextField search;

	// 构造函数，创建各种元素
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
				new JMenuItem("另存为"), new JMenuItem("搜索"), new JMenuItem("关闭"), new JMenuItem("设置"),
				new JMenuItem("退出") };
		editItems = new JMenuItem[] { new JMenuItem("复制"), new JMenuItem("粘贴"), new JMenuItem("撤销") };
		compileItems = new JMenuItem[] { new JMenuItem("编译"), new JMenuItem("汇编(MIPS)")};
		helpItems = new JMenuItem[] { new JMenuItem("关于") };
		chooser = new JFileChooser();
		setAccelerator();
		init();

		getContentPane().setLayout(new BorderLayout(5, 0));
		tb = new JTabbedPane();
		getContentPane().add(tb, BorderLayout.CENTER);

		RTextScrollPane content = createContentPane(SyntaxConstants.SYNTAX_STYLE_NONE);
		pathDic.put(content, null);
		tb.addTab("New tab", null, content, null);
		
		textPane = new JTextPane();
		textPane.setForeground(Color.RED);
		textPane.setBackground(Color.WHITE);
		textPane.setEditable(false);
		//textPane.setText("Wrong\n\n\n\n\n");
		getContentPane().add(textPane, BorderLayout.SOUTH);
		DataUtil.mainframe = this;
	}

	public static void main(String[] args) {
		new MainFrame();
	}

	// 覆盖方法，监听点击了菜单中的哪些选项并实现监听的事件
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		String commond = e.getActionCommand();
		System.out.println("commond:" + commond);
		switch (commond) {
		case "新建":
			RTextScrollPane pane = createContentPane(SyntaxConstants.SYNTAX_STYLE_NONE);
			pathDic.put(pane, null);
			tb.addTab("New tab", null, pane, null);
			tb.setSelectedIndex(contentlist.size() - 1);
			break;
		case "打开":
			chooser.showOpenDialog(this);
			File f = chooser.getSelectedFile();
			String name = f.getName();
			String type = name.substring(name.lastIndexOf(".")+1,name.length());
			OPenFile(f,type);
			tb.setSelectedIndex(contentlist.size() - 1);
			break;
		case "保存":
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
		case "另存为":
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
		case "搜索":
			searchContent = JOptionPane.showInputDialog(this, "请输入要搜索的内容:");
			System.out.println(searchContent);
			if(searchContent==null)
				break;
			SearchContext searchContext = new SearchContext(searchContent);
			SearchEngine.find(contentlist.get(tb.getSelectedIndex()).getTextArea(), searchContext);
			searchIndex = search(searchContent);
			break;
		case "退出":
			System.exit(0);
			break;
		case "编译":
			if(pathDic.get(contentlist.get(tb.getSelectedIndex()))==null){
				JOptionPane.showMessageDialog(this, "请先保存文件！");
				break;
			}else{
				DataUtil.currentFileName = pathDic.get(contentlist.get(tb.getSelectedIndex()));
			}
			
			SwingUtilities.invokeLater(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					String text = contentlist.get(tb.getSelectedIndex()).getTextArea().getText().trim();
					String[] lexs = DataUtil.divide(text);
					for(int i=0;i<lexs.length;i++){
						//System.out.println(lexs[i]);
						if(lexs[i].trim().equals("+")){
							if(lexs[i-1].trim().matches("(\\d)+")){
								String temp = lexs[i-1];
								lexs[i-1] = lexs[i+1];
								lexs[i+1] = temp;
							}
						}
					}
//					for(String st:lexs){
//						System.out.println(st);
//					}
					try {
						tr = new translation(lexs);
						int tips = tr.getTips();
						
						BackEndStruct bct = new BackEndStruct();
						bct.setLex(tr.getLex());
						System.out.println("is variTable in bct null?"+ (bct.variTable==null));
						new File(DataUtil.currentFileName.replace(".c", ".mips"));
						bct.genCode( DataUtil.currentFileName.replace(".c", ".temp"), DataUtil.currentFileName.replace(".c", ".mips"));
						textPane.setText("编译成功！\n\n\n");
						
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						StackTraceElement[] trace = e1.getStackTrace();
						StringBuilder builder = new StringBuilder();
						for(StackTraceElement e:trace){
							builder.append(e.toString());
						}
						e1.printStackTrace();
						textPane.setText(e1.getMessage()+"\n\n\n");
					}
					
					
				}
			});
			
			break;
		case "汇编(MIPS)":
			if(pathDic.get(contentlist.get(tb.getSelectedIndex()))==null){
				JOptionPane.showMessageDialog(this, "请先保存文件！");
				break;
			}else{
				DataUtil.currentFileName = pathDic.get(contentlist.get(tb.getSelectedIndex()));
			}
			//String text = contentlist.get(tb.getSelectedIndex()).getTextArea().getText();
			//String[] lines = text.split("\n");
			String filename = tb.getTitleAt(tb.getSelectedIndex());
			System.out.println(filename);
			String postfix = filename.substring(filename.lastIndexOf(".")+1,filename.length());
			if(postfix.equals("mips")||postfix.equals("MIPS")){
				File current = new File(pathDic.get(contentlist.get(tb.getSelectedIndex()))); 
				if(!current.exists()){
					JOptionPane.showMessageDialog(this, "请先保存文件");
				}else{
					try {
						InputStream ins = new FileInputStream(current);
						AssembleContext content = AssembleContext.parseContext(ins);
						content.Parse();
						ins.close();
						textPane.setText("汇编成功\n\n\n");
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						textPane.setText(e1.getMessage()+"\n\n\n");
						e1.printStackTrace();
					}	
				}
			}else{
				JOptionPane.showMessageDialog(MainFrame.this, "该文件不是.mips文件,无法进行汇编");
			}
			
			
			break;
		case "关闭":
			contentlist.remove(tb.getSelectedComponent());
			pathDic.remove(tb.getSelectedComponent());
			tb.remove(tb.getSelectedIndex());

			break;
		case "关于":
			JOptionPane.showMessageDialog(this, "Mini C IDE\n\t东南大学计算机科学与工程学院计算机综合设计作品！");
		default:
			break;
		}
	}

	// 初始化，将各个菜单单元加入到菜单项中显示
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

	}

	// 设置快捷键
	private void setAccelerator() {
		fileItems[0].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
		fileItems[1].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
		fileItems[2].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		fileItems[3]
				.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.ALT_MASK + ActionEvent.CTRL_MASK));
		fileItems[4].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, ActionEvent.CTRL_MASK));
		fileItems[5].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.CTRL_MASK));
		compileItems[0].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5, ActionEvent.CTRL_MASK));
		compileItems[1].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F7, ActionEvent.CTRL_MASK));
	}

	// 创建一个tab，发现该tab文件的后缀从而打开对应的语法高亮功能，并将tab加入到一个list中便于管理
	private RTextScrollPane createContentPane(String language) {
		RSyntaxTextArea pane = new RSyntaxTextArea();
		System.out.println("text/" + language);
		pane.setSyntaxEditingStyle("text/" + language);
		pane.setCodeFoldingEnabled(true);
		RTextScrollPane sp = new RTextScrollPane(pane);
		contentlist.add(sp);
		return sp;
	}

	// 打开文件，将文件f中的内容显示在一个新的tab中
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

	// 将str写入文件f
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

	// 搜索指定内容
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
