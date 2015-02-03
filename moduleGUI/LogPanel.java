import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTextArea;


public class LogPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private String outputPath;
	private String dateFile;
	private String company;
	private String date;
	private String amount;
	private boolean error;
	
	public LogPanel(String o, String d) {
		outputPath = o;
		dateFile = d;
		error = false;
		
		setLayout(new BoxLayout(this, 1));
		
		BufferedReader br = null;
		JTextArea text = new JTextArea();
		try {
			br = new BufferedReader(new FileReader(outputPath + "/log" + dateFile + ".txt"));
			String l = null;
			while ((l = br.readLine()) != null) {
				if (l.contains("Company: ")) {
					company = l;
				}
				
				if (l.contains("Date: ")) {
					date = l;
				}
				
				if (l.contains("Total Gain/Loss: ")) {
					amount = l;
				}
				
				if (l.contains("ERROR")) {
					error = true;
				}
				text.setText(text.getText() + l + "\n");
			}
 
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null) {
					br.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		text.setEditable(false);
		add(text);
		/*JScrollPane scrollPane = new JScrollPane(text); 
		scrollPane.add(text);
		setSize(430, 630);
		add(scrollPane);*/
	}
	
	protected boolean error() {
		return error;
	}
	
	protected String company() {
		return company;
	}
	
	protected String date() {
		return date;
	}
	
	protected String amount() {
		return amount;
	}

}
