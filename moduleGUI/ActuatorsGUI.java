import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javax.swing.BoxLayout;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

//input csv file selector
// threshold,  number
//moving avg number
//log path directory
//log name text
//output csv path directory
// output csv name text
public class ActuatorsGUI extends JPanel {
	private static final long serialVersionUID = 1L;
	private String path;
	
    private JFileChooser fc;
    private JFileChooser dc;
    
    private JTextField inputCSV;
    private JTextField outputCSVPath;
    private JTextField outputCSV;
    private JTextField movingAvg;
    private JTextField threshold;
    private JTextField logPath;
    private JTextField log;
	
	public ActuatorsGUI(String p) {
		path = p;
		
		setLayout(new BoxLayout(this, 1));
		
        add(csvPanel());
        add(parameterPanel());
        add(logPanel());
  	}

	private BoxPanel csvPanel() {
		fc = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV files", "csv");
        fc.setFileFilter(filter);
        
        JLabel inputCSVLabel = new JLabel("Input CSV Filepath*: ");
        inputCSV = new JTextField("", 30);
        final SubmitButton openIButton = new SubmitButton("Open CSV File...", 60, 20);
        openIButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
                //Handle open button action.
                if (e.getSource() == openIButton) {
                    if (fc.showOpenDialog(ActuatorsGUI.this) == JFileChooser.APPROVE_OPTION) {
                        File csvPath = fc.getSelectedFile();
                        inputCSV.setText(csvPath.getAbsolutePath());
                    }
                }
            }
        });
        
        JPanel inputCSVPanel = new JPanel();
        inputCSVPanel.setLayout(new BoxLayout(inputCSVPanel, 1));
        inputCSVPanel.add(inputCSVLabel);
        inputCSVPanel.add(inputCSV);
        inputCSVPanel.add(openIButton);
        
        JLabel outputCSVPLabel = new JLabel("Output CSV Filepath*: ");
        outputCSVPath = new JTextField("", 30);
        dc = new JFileChooser();
        dc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        final SubmitButton openOButton = new SubmitButton("Select Output CSV Directory*: ", 60, 20);
        openOButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
                //Handle open button action.
                if (e.getSource() == openOButton) {
                    if (dc.showOpenDialog(ActuatorsGUI.this) == JFileChooser.APPROVE_OPTION) {
                        outputCSVPath.setText(dc.getSelectedFile().getAbsolutePath());
                    }
                }
            }
        });
        
        JLabel outputCSVFLabel = new JLabel("Output CSV Filename*: ");
        outputCSV = new JTextField("", 30);
        
        JPanel outputCSVPanel = new JPanel();
        outputCSVPanel.setLayout(new BoxLayout(outputCSVPanel, 1));
        outputCSVPanel.add(outputCSVPLabel);
        outputCSVPanel.add(outputCSVPath);
        outputCSVPanel.add(openOButton);
        outputCSVPanel.add(outputCSVFLabel);
        outputCSVPanel.add(outputCSV);
        
        BoxPanel csvPanel = new BoxPanel(new Dimension(500,200)); //use FlowLayout
        csvPanel.add(inputCSVPanel);
        csvPanel.add(outputCSVPanel);
        
        return csvPanel;
	}
	
	private BoxPanel parameterPanel() {
		JLabel movingLabel = new JLabel("Moving Average*: ");
		movingAvg = new JTextField("", 30);
		movingAvg.addKeyListener(new KeyListener() {
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
		});
		
		JLabel thresLabel = new JLabel("Threshold*: ");
		threshold = new JTextField("", 30);
		threshold.addKeyListener(new KeyListener() {
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
		});
		
		BoxPanel parameterPanel = new BoxPanel(new Dimension(500,150)); //use FlowLayout
        parameterPanel.add(movingLabel);
        parameterPanel.add(movingAvg);
        parameterPanel.add(thresLabel);
        parameterPanel.add(threshold);
        
        return parameterPanel;
	}
	
	private BoxPanel logPanel() {
		BoxPanel logPanel = new BoxPanel(new Dimension(500,150)); //use FlowLayout
        
		JLabel logLabel = new JLabel("Log Directory*: ");
		logPath = new JTextField("", 30);
		dc = new JFileChooser();
        dc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        final SubmitButton openLButton = new SubmitButton("Select Output CSV Directory*: ", 60, 20);
        openLButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
                //Handle open button action.
                if (e.getSource() == openLButton) {
                    if (dc.showOpenDialog(ActuatorsGUI.this) == JFileChooser.APPROVE_OPTION) {
                        logPath.setText(dc.getSelectedFile().getAbsolutePath());
                    }
                }
            }
        });
        
        JLabel logPLabel = new JLabel("Log Filename*: ");
        log = new JTextField("", 30);
		
        JPanel l = new JPanel();
        l.setLayout(new BoxLayout(l, 1));
        l.add(logLabel);
        l.add(logPath);
        l.add(openLButton);
        l.add(logPLabel);
        l.add(log);
		l.add(submitButton());
		logPanel.add(l);
        return logPanel;
	}
	
	private SubmitButton submitButton() {
		final SubmitButton submit = new SubmitButton("Submit", 60, 20);
		submit.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
                if (e.getSource() == submit) {
                	try {
                		//Extract required files
                		//Write to parameter file
                		//Run module and wait for it
                		//then delete files
                		
                		Runtime.getRuntime().exec("unzip " + path + "/moduleGUI.jar msm-0.4.1.jar params.txt -d " + path + "/tmp_resources").waitFor();
                		try {
                			
                			PrintWriter writer = new PrintWriter(path + "/tmp_resources/params.txt", "UTF-8");
                			writer.println(threshold.getText());
                			writer.println(movingAvg.getText());
                			writer.close();
                		} catch (FileNotFoundException | UnsupportedEncodingException f) {
                			f.printStackTrace();
                		}
						Runtime.getRuntime().exec("java -jar " + path + "/tmp_resources/msm-0.4.1.jar " + 
												  inputCSV.getText() + " " + 
												  path + "/tmp_resources/params.txt " + 
												  logPath.getText() + "/" + log.getText() + " "+ 
								                  outputCSVPath.getText() + "/" + outputCSV.getText()).waitFor();
                		Runtime.getRuntime().exec("rm -r " + path + "/tmp_resources");
                	} catch (IOException | InterruptedException e1) {
						e1.printStackTrace();
					}
                }
            }
        });
		return submit;
	}
}
