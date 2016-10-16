package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.TextArea;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

public class ContentPane extends JScrollPane {

	/**
	 * 
	 */
	JTextPane textPane;
	StringBuilder columnNumber;
	JPanel columnNum;
	JTextPane txtpnn;
	int count = 1;
	private static final long serialVersionUID = 1L;
	String path;
	
	public ContentPane() {
		path = null;
		columnNumber = new StringBuilder(0+"\n");
		
		textPane = new CodePane();
		setViewportView(textPane);
		
		columnNum = new JPanel();
		columnNum.setBackground(Color.LIGHT_GRAY);
		setRowHeaderView(columnNum);
		columnNum.setLayout(new BorderLayout(0, 0));
		
		txtpnn = new JTextPane();
		txtpnn.setBackground(Color.LIGHT_GRAY);
		txtpnn.setText(columnNumber.toString());
		columnNum.add(txtpnn, BorderLayout.NORTH);
		textPane.addKeyListener(new KeyListener() {
	
			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				if(e.getKeyCode() == KeyEvent.VK_ENTER){
					count++;
					columnNumber.append(count+"\n");
					txtpnn.setText(columnNumber.toString());
				}
				
			}

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	public void setText(String str){
		textPane.setText(str);
		int a = str.split("\n").length-1;
		System.out.println(a);
		int count = 1;
		columnNumber = new StringBuilder();
		while(count<a){
			columnNumber.append(count+"\n");
			count++;
		}
		txtpnn.setText(columnNumber.toString());
	}
	
	public String getText(){
		return textPane.getText();
	}
	public void setPath(String path){
		this.path = path;
	}
	public String getPath(){
		return path;
	}
	
}
