import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JButton;


public class SubmitButton extends JButton {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SubmitButton(String text, int x, int y) {
		
		this.setText(text);
		Dimension d = new Dimension(x,y);
		this.setSize(d);
		this.setBounds(250, 250, 40, 10);
		this.setBackground(new Color(0, 201, 87));
	}

}

