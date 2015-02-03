import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.jdesktop.xswingx.PromptSupport;
import org.jfree.chart.ChartPanel;

public class NyueumaiersGUI extends JPanel {

	private static final long serialVersionUID = 1L;
	private String start;
	private String end;
	private String path;
    private JFileChooser fc;
    private JFileChooser dc;
    
    //the following houses the parameter
    private NTextField CSVPath;
    private NTextField outputPath;
    
    //split up the start and end dates to make input easier.
    //START date fields
    private NTextField startDD;
    private NTextField startMM;
    private NTextField startYYYY;
    private NTextField startHH;
    private NTextField startmm;
    private NTextField startss;
    
    // END date fields
    private NTextField endDD;
    private NTextField endMM;
    private NTextField endYYYY;
    private NTextField endHH;
    private NTextField endmm;
    private NTextField endss;
    
    private NTextField movingAvg;
    private NTextField threshold;
    private moduleGUI frame;
    private Graph g;
    
    //private JSeparator s;
    
	public NyueumaiersGUI (String p, moduleGUI f) {
		path = p;
		frame = f;
		
		setLayout(new BoxLayout(this, 1));
        
        add(filePanel());
        add(datePanel());
        add(movingAndThresPanel());
        add(submitPanel());
	}	
	
	private BoxPanel filePanel() {
		//Create a file chooser
		fc = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV files", "csv");
        fc.setFileFilter(filter);
        
        JLabel fileLabel = new JLabel(new ImageIcon(getClass().getResource("csv.png")));
        CSVPath = new NTextField("", 8);
        CSVPath.setEditable(false);
        CSVPath.addKeyListener(new KeyListener() {
	    	public void keyTyped(KeyEvent e) {
	        	if (e.getKeyChar() == KeyEvent.VK_BACK_SPACE) {  
	        		CSVPath.setText("");
	        	}
	        	
	        	if (e.getKeyChar() == KeyEvent.VK_ENTER) {  
	        		run();
	        	}
	    	}

			@Override
			public void keyReleased(KeyEvent arg0) {				
			}

			@Override
			public void keyPressed(KeyEvent arg0) {				
			}
		});
        final SubmitButton openButton = new SubmitButton("Open CSV File...", 60, 20);
        openButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {

                //Handle open button action.
                if (e.getSource() == openButton) {
                    if (fc.showOpenDialog(NyueumaiersGUI.this) == JFileChooser.APPROVE_OPTION) {
                        CSVPath.setText(fc.getSelectedFile().getAbsolutePath());
                        defaultTime();
                    }
                }
            }
        });
        
        JPanel file = new JPanel();
        file.setBackground(Color.WHITE);
        file.setLayout(new BoxLayout(file, 1));
        file.add(fileLabel);
        //file.add(s);
        PromptSupport.setPrompt("Choose a .csv file!", CSVPath);
        file.add(CSVPath);
        file.add(openButton);
        
        JLabel outputLabel = new JLabel(new ImageIcon(getClass().getResource("output.png")));
        outputPath = new NTextField("", 8);
        outputPath.setEditable(false);
        outputPath.addKeyListener(new KeyListener() {
	    	public void keyTyped(KeyEvent e) {
	        	if (e.getKeyChar() == KeyEvent.VK_BACK_SPACE) {  
	        		outputPath.setText("");
	        	}
	        	
	        	if (e.getKeyChar() == KeyEvent.VK_ENTER) {  
	        		run();
	        	}
	    	}

			@Override
			public void keyReleased(KeyEvent arg0) {				
			}

			@Override
			public void keyPressed(KeyEvent arg0) {				
			}
		});
        dc = new JFileChooser();
        dc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        final SubmitButton outputButton = new SubmitButton("Find Directory...", 60, 20);
        outputButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {

                //Handle open button action.
                if (e.getSource() == outputButton) {
                    if (dc.showOpenDialog(NyueumaiersGUI.this) == JFileChooser.APPROVE_OPTION) {
                        outputPath.setText(dc.getSelectedFile().getAbsolutePath());
                    }
                }
            }
        });
        JPanel output = new JPanel();
        output.setBackground(Color.WHITE);
        output.setLayout(new BoxLayout(output, 1));
        output.add(outputLabel);
        PromptSupport.setPrompt("Result will go here.", outputPath);
        output.add(outputPath);
        output.add(outputButton);
        
        BoxPanel filePanel = new BoxPanel(new Dimension(200,150)); //use FlowLayout
        filePanel.setLayout(new BoxLayout(filePanel, BoxLayout.Y_AXIS));
        filePanel.add(file);
        filePanel.add(output);
        
        return filePanel;
	}
	
	private BoxPanel datePanel() {
		JLabel startdLabel = new JLabel(new ImageIcon(getClass().getResource("startdate.png")));
		JLabel starttLabel = new JLabel(new ImageIcon(getClass().getResource("starttime.png")));
		JLabel enddLabel = new JLabel(new ImageIcon(getClass().getResource("enddate.png")));
		JLabel endtLabel = new JLabel(new ImageIcon(getClass().getResource("endtime.png")));
		JLabel colon = new JLabel(new ImageIcon(getClass().getResource("colon.png")));
		
		// starting dates
        startDD = new NTextField("", 2, 2);
        startDD.addKeyListener(enterListener());
        startMM = new NTextField("", 2, 2);
        startMM.addKeyListener(enterListener());
        startYYYY = new NTextField("", 3, 4);
        startYYYY.addKeyListener(enterListener());
        startHH = new NTextField("", 2, 2);
        startHH.addKeyListener(enterListener());
        startmm = new NTextField("", 2, 2);
        startmm.addKeyListener(enterListener());
        startss = new NTextField("", 2, 2);
        startss.addKeyListener(enterListener());
        PromptSupport.setPrompt("DD - Date", startDD);
        PromptSupport.setPrompt("MM - Month", startMM);
        PromptSupport.setPrompt("YYYY - Year", startYYYY);
        PromptSupport.setPrompt("HH - Hour (24 hr)", startHH);
        PromptSupport.setPrompt("mm - Minutes (24 hr)", startmm);
        PromptSupport.setPrompt("ss - Seconds (24 hr)", startss);
        
        JPanel start = new JPanel();
        start.setBackground(Color.WHITE);
        start.add(startdLabel);
        
        start.add(startDD);
        start.add(startMM);
        start.add(startYYYY);
        start.add(starttLabel);
        start.add(startHH);
        colon = new JLabel(new ImageIcon(getClass().getResource("colon.png")));
        start.add(colon);
        start.add(startmm);
        colon = new JLabel(new ImageIcon(getClass().getResource("colon.png")));
        start.add(colon);
        start.add(startss);
        
        // ending dates
        
        endDD = new NTextField("", 2, 2);
        endDD.addKeyListener(enterListener());
        endMM = new NTextField("", 2, 2);
        endMM.addKeyListener(enterListener());
        endYYYY = new NTextField("", 3, 4);
        endYYYY.addKeyListener(enterListener());
        endHH= new NTextField("", 2, 2);
        endHH.addKeyListener(enterListener());
        endmm = new NTextField("", 2, 2);
        endmm.addKeyListener(enterListener());
        endss = new NTextField("", 2, 2);
        endss.addKeyListener(enterListener());
        PromptSupport.setPrompt("DD - Day", endDD);
        PromptSupport.setPrompt("MM - Month", endMM);
        PromptSupport.setPrompt("YYYY - Year", endYYYY);
        PromptSupport.setPrompt("HH - Hour (24 hr)", endHH);
        PromptSupport.setPrompt("mm - Minutes (24 hr)", endmm);
        PromptSupport.setPrompt("ss - Seconds (24 hr)", endss);
        
        JPanel end = new JPanel();
        end.setBackground(Color.WHITE);
        end.add(enddLabel);
        
        end.add(endDD);
        end.add(endMM);
        end.add(endYYYY);
        end.add(endtLabel);
        end.add(endHH);
        colon = new JLabel(new ImageIcon(getClass().getResource("colon.png")));
        end.add(colon);
        end.add(endmm);
        colon = new JLabel(new ImageIcon(getClass().getResource("colon.png")));
        end.add(colon);
        end.add(endss);
        
        BoxPanel datePanel = new BoxPanel(new Dimension(200,150));
        datePanel.setLayout((new BoxLayout(datePanel, BoxLayout.Y_AXIS)));
        datePanel.add(start);
        datePanel.add(end);
        
        return datePanel;
	}
	
	private BoxPanel movingAndThresPanel() {
		JLabel movingLabel = new JLabel(new ImageIcon(getClass().getResource("avg.png")));
        movingAvg = new NTextField("", 18, -1);
        movingAvg.addKeyListener(enterListener());
        
        JRadioButton defaultm1 = new JRadioButton("2");
        defaultm1.setActionCommand("2");
        defaultm1.addActionListener(getDefaultM());
        JRadioButton defaultm2 = new JRadioButton("4");
        defaultm2.setActionCommand("4");
        defaultm2.addActionListener(getDefaultM());
        JRadioButton defaultm3 = new JRadioButton("6");
        defaultm3.setActionCommand("6");
        defaultm3.addActionListener(getDefaultM());
        
        JPanel defaultm = new JPanel();
        defaultm.setLayout(new BoxLayout(defaultm, BoxLayout.X_AXIS));
        defaultm.add(defaultm1);
        defaultm.add(defaultm2);
        defaultm.add(defaultm3);
        ButtonGroup defm = new ButtonGroup();
        defm.add(defaultm1);
        defm.add(defaultm2);
        defm.add(defaultm3);
        
        JLabel thresLabel = new JLabel(new ImageIcon(getClass().getResource("thresh.png")));
        threshold = new NTextField("", 18, -1);
        threshold.addKeyListener(enterListener());
        
        
        JRadioButton defaultt1 = new JRadioButton("0.001");
        defaultt1.setActionCommand("0.001");
        defaultt1.addActionListener(getDefaultT());
        JRadioButton defaultt2 = new JRadioButton("0.0001");
        defaultt2.setActionCommand("0.0001");
        defaultt2.addActionListener(getDefaultT());
        JRadioButton defaultt3 = new JRadioButton("0.000000001");
        defaultt3.setActionCommand("0.000000001");
        defaultt3.addActionListener(getDefaultT());
        defaultt3.setSelected(false);
        
        JPanel defaultt = new JPanel();
        defaultt.setLayout(new BoxLayout(defaultt, BoxLayout.X_AXIS));
        defaultt.add(defaultt1);
        defaultt.add(defaultt2);
        defaultt.add(defaultt3);
        ButtonGroup  deft = new ButtonGroup();
        deft.add(defaultt1);
        deft.add(defaultt2);
        deft.add(defaultt3);
        
        BoxPanel mAndTPanel = new BoxPanel(new Dimension(200, 200)); //use FlowLayout
        mAndTPanel.setLayout(new BoxLayout(mAndTPanel, BoxLayout.Y_AXIS));
        mAndTPanel.add(movingLabel);
        PromptSupport.setPrompt("A Moving Average is a number.", movingAvg);
        mAndTPanel.add(movingAvg);
        mAndTPanel.add(defaultm);
        PromptSupport.setPrompt("A typical threshold is 0.001.", threshold);
        mAndTPanel.add(thresLabel);
        mAndTPanel.add(threshold);
        mAndTPanel.add(defaultt);
        
        return mAndTPanel;
	}
	
	
	private BoxPanel submitPanel() {
		
        BoxPanel subPanel = new BoxPanel(new Dimension(200,50));
        final SubmitButton submit = new SubmitButton("Proceed", 60, 20);
        submit.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
                if (e.getSource() == submit) {
                	run();
                }
            }
        });
        subPanel.add(submit);
        
        return subPanel;
	}
	
	private void run() {
		try {
    		//Extract required files
    		//Write to parameter file
    		//Run module and wait for it
    		//then delete files
    		
    		Runtime.getRuntime().exec("unzip " + path + "/moduleGUI.jar module.jar parameters.properties -d " + path + "/tmp_resources").waitFor();
    		try {
    			
    			PrintWriter writer = new PrintWriter(path + "/tmp_resources/parameters.properties", "UTF-8");
    			writer.println("file=" + CSVPath.getText());
    			writer.println("outputDir=" + outputPath.getText());
    			if (startYYYY.getText().isEmpty() && startMM.getText().isEmpty() && startDD.getText().isEmpty() &&
    				startHH.getText().isEmpty() && startmm.getText().isEmpty() && startss.getText().isEmpty() ) {
    				writer.println("timeFrameStart=");
    			} else {
    				writer.println("timeFrameStart=" + startYYYY.getText() + startMM.getText() + startDD.getText() + "," + startHH.getText() + ":" + startmm.getText() + ":" + startss.getText());
    			}
    			if (endYYYY.getText().isEmpty() && endMM.getText().isEmpty() && endDD.getText().isEmpty() &&
    				endHH.getText().isEmpty() && endmm.getText().isEmpty() && endss.getText().isEmpty() ) {
        			writer.println("timeFrameEnd=");
        		} else {
        			writer.println("timeFrameEnd=" + endYYYY.getText() + endMM.getText() + endDD.getText() + "," + endHH.getText() + ":" + endmm.getText() + ":" + endss.getText());
        		}
    			
    			writer.println("movingAvg=" + movingAvg.getText());
    			writer.println("threshold=" + threshold.getText());
    			writer.close();
    			
    		} catch (FileNotFoundException | UnsupportedEncodingException f) {
    			f.printStackTrace();
    		}
    		
			Runtime.getRuntime().exec("java -jar " + path + "/tmp_resources/module.jar " + path + "/tmp_resources/parameters.properties").waitFor();
    		Runtime.getRuntime().exec("rm -r " + path + "/tmp_resources");
    		
    		JPanel e = new JPanel();
			e.setLayout((new BoxLayout(e, BoxLayout.Y_AXIS)));
    		JPanel parameters = new JPanel();
			parameters.setLayout((new BoxLayout(parameters, BoxLayout.X_AXIS)));
			
    		parameters.add(frame.getPP());
    		Dimension d = new Dimension(700, 680);
    		
    		String p = path;
			if (!outputPath.getText().isEmpty() && new File(outputPath.getText()).isDirectory()) {
				p = outputPath.getText();
			} 
			
			LogPanel log = new LogPanel(p, getLatestDate(p));
			g = new Graph(p + "/OrderBook" + getLatestDate(p) + ".csv", p + "/Graph" + getLatestDate(p) + ".jpg", null, null, null);
			if (produceGraph(p)) {
				parameters.add(g);
				d.setSize(1500, 680);
			}
			parameters.add(log);
			
			if (!log.error()) {
				e.add(banner(log.company(), log.date(), log.amount()));
			}
			e.add(parameters);
			
			frame.remove(frame.getPG());
			frame.setPG(e);
			frame.add(e);
			frame.setSize(d);
			frame.paintComponents(frame.getGraphics());
    	} catch (IOException | InterruptedException e1) {
			e1.printStackTrace();
		}
	}
	
	protected void updateGUI() {
		JPanel e = new JPanel();
		e.setLayout((new BoxLayout(e, BoxLayout.Y_AXIS)));
		JPanel parameters = new JPanel();
		parameters.setLayout((new BoxLayout(parameters, BoxLayout.X_AXIS)));
		
		parameters.add(frame.getPP());
		Dimension d = new Dimension(700, 680);
		
		String p = path;
		if (!outputPath.getText().isEmpty() && new File(outputPath.getText()).isDirectory()) {
			p = outputPath.getText();
		} 
		
		LogPanel log = new LogPanel(p, getLatestDate(p));
		if (produceGraph(p)) {
			parameters.add(g.updateGraph());
			d.setSize(1500, 680);
		}
		parameters.add(log);
		
		if (!log.error()) {
			e.add(banner(log.company(), log.date(), log.amount()));
		}
		e.add(parameters);
		
		frame.remove(frame.getPG());
		frame.setPG(e);
		frame.add(e);
		frame.setSize(d);
		frame.paintAll(frame.getGraphics());
	}
	
	private String getLatestDate(String p) {
		File[] listOfFiles = new File(p).listFiles(); 
		
		long lastMod = Long.MIN_VALUE;
		File latest = null;
		
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				String files = listOfFiles[i].getName();
				
				if ((files.endsWith(".txt") || files.endsWith(".TXT")) && files.contains("log")) {
					if (listOfFiles[i].lastModified() > lastMod) {
						latest = listOfFiles[i];
						lastMod = listOfFiles[i].lastModified();
					}
				}
			}
		}
		String[] name = latest.getName().split("\\.")[0].split("-");
		return "-"+name[1]+"-"+name[2];
	}
	
	private boolean produceGraph(String p) {
		File[] listOfFiles = new File(p).listFiles(); 
		
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				String files = listOfFiles[i].getName();
				
				if ((files.endsWith(".csv") || files.endsWith(".CSV")) && 
					 files.contains("OrderBook" + getLatestDate(p))) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	private KeyListener enterListener() {
		return new KeyListener() {
	    	public void keyTyped(KeyEvent e) {
	        	if (e.getKeyChar() == KeyEvent.VK_ENTER) {  
	        		run();
	        	}
	    	}

			@Override
			public void keyReleased(KeyEvent arg0) {				
			}

			@Override
			public void keyPressed(KeyEvent arg0) {				
			}
		};
	}
	
	private ActionListener getDefaultM() {
		return new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				movingAvg.setText(arg0.getActionCommand());
			}
			
		};
	}
	
	private ActionListener getDefaultT() {
		return new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				threshold.setText(arg0.getActionCommand());
			}
		};
	}
	
	private void defaultTime() {
		String csv = CSVPath.getText();

		if (csv.endsWith(".csv")) {
			BufferedReader br = null;
			try {
				br = new BufferedReader(new FileReader(csv));
				String l = br.readLine();
				l = br.readLine().replace("\"", "");
				
				String[] b = l.split(",");
				start = b[1] + b[2];
				setTimeLabel(start, startDD, startMM, startYYYY, startHH, startmm, startss);
								
				String last = null;
				while ((l = br.readLine()) != null) {
					last = l;
				}
				
				b = last.split(",");
				end = b[1] + b[2];
				setTimeLabel(end, endDD, endMM, endYYYY, endHH, endmm, endss);
	 
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
		}
	}
	
	private void setTimeLabel(String time, NTextField d, NTextField M, NTextField y, NTextField h, NTextField m, NTextField s) {
		Calendar date = parseDate(time, false);
		
		d.setText(String.format("%02d", date.get(Calendar.DAY_OF_MONTH)));
	    M.setText(String.format("%02d", date.get(Calendar.MONTH) + 1));
	    y.setText("" + date.get(Calendar.YEAR));
	    h.setText(String.format("%02d", date.get(Calendar.HOUR_OF_DAY)));
	    m.setText(String.format("%02d", date.get(Calendar.MINUTE)));
	    s.setText(String.format("%02d", date.get(Calendar.SECOND)));
	}
	
	private JPanel banner(String c, String d, String a) {
		JPanel p = new JPanel();
		p.add(new JLabel(c));
		p.add(new JLabel(d));
		p.add(new JLabel(a));
		
		return p;
	}
	
	private Calendar parseDate(String d, boolean round) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHH:mm:ss");
		Calendar date = Calendar.getInstance();
		try {
			date.setTime(sdf.parse(d));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		if (round) {
			date.add(Calendar.MINUTE, 30); 
			date.set(Calendar.MINUTE, 0);
			date.set(Calendar.SECOND, 0);
		}
		
		return date;
	}
	
	protected void change(ChartPanel chartPanel) {
		
		frame.removeAll();
		frame.paintAll(frame.getGraphics());
		frame.add(new JLabel("HELLO"));
		frame.paintAll(frame.getGraphics());
	}
}