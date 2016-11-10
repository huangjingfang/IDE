package GUI;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

public class Setting extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static Setting instance = new Setting();
	private Setting() {
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		getContentPane().add(tabbedPane, BorderLayout.CENTER);
		String[] menus = new String[]{"×ÖÌå",""};
		
	}
	
	synchronized public static Setting getInstance() {
		return instance;
	}
	

}
