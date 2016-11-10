package GUI;

import javax.swing.SwingUtilities;

public class Entry {
	public static MainFrame main;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				main = new MainFrame();
			}
		});
	}

}
