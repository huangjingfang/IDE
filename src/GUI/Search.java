package GUI;

import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Search extends Dialog implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Search search = new Search();
	private JTextField textField;;
	
	public Search(){
		super(Entry.main, "����");
		JPanel panel = new JPanel();
		panel.setLayout(null);
		
		JLabel label = new JLabel("���ݣ�");
		label.setBounds(56, 64, 54, 15);
		panel.add(label);
		
		textField = new JTextField();
		textField.setBounds(142, 62, 176, 22);
		panel.add(textField);
		textField.setColumns(10);
		
		JButton button = new JButton("ȷ��");
		button.setBounds(52, 149, 93, 23);
		panel.add(button);
		
		JButton button_1 = new JButton("ȡ��");
		button_1.setBounds(211, 147, 93, 23);
		panel.add(button_1);
		
	}
	
	synchronized public static Search getInstance(){
		return search;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		String s = e.getActionCommand();
		if(s.equals("ȷ��")){
			
		}else if(s.equals("ȡ��")){
			
		}
		
	}
}
