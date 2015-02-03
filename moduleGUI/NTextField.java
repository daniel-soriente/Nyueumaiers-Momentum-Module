import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class NTextField extends JTextField {
	
	private static final long serialVersionUID = 1L;
	
	public NTextField (String e, Integer n) {
		Font font = new Font("Droid Sans", 10, 20);
		this.setFont(font);
		this.setColumns(n);
	}
	
	public NTextField (String e, Integer n, int limit) {
		Font font = new Font("Droid Sans", 10, 20);
		this.setFont(font);
		this.setColumns(n);
		if (limit == -1) {
			this.addKeyListener(numbersCommaOnly());
		} else {
			this.setDocument(new JTextFieldLimit(limit));
			this.addKeyListener(numbersOnly());
		}
	}

	public class JTextFieldLimit extends PlainDocument {
		private static final long serialVersionUID = 1L;
		private int limit;

		JTextFieldLimit(int limit) {
			super();
			this.limit = limit;
		}

		public void insertString( int offset, String  str, AttributeSet attr ) throws BadLocationException {
			if (str == null) return;

			if ((getLength() + str.length()) <= limit) {
				super.insertString(offset, str, attr);
			}
		}
	}
	
	protected KeyListener numbersOnly() {
		KeyListener key = new KeyListener() {
	    	public void keyTyped(KeyEvent e) {
	        	if ((e.getKeyChar() < 48 || e.getKeyChar() > 57) && e.getKeyChar() != KeyEvent.VK_BACK_SPACE) {  
	        		e.consume();
	        	}
	    	}

			@Override
			public void keyReleased(KeyEvent arg0) {				
			}

			@Override
			public void keyPressed(KeyEvent arg0) {				
			}
		};
		return key;
	}
	
	protected KeyListener numbersCommaOnly() {
		KeyListener key = new KeyListener() {
	    	public void keyTyped(KeyEvent e) {
	        	if ((e.getKeyChar() < 48 || e.getKeyChar() > 57) && e.getKeyChar() != KeyEvent.VK_BACK_SPACE && e.getKeyChar() != KeyEvent.VK_PERIOD) {  
	        		e.consume();
	        	}
	    	}

			@Override
			public void keyReleased(KeyEvent arg0) {				
			}

			@Override
			public void keyPressed(KeyEvent arg0) {				
			}
		};
		return key;
	}
}