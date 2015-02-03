
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

public class Graph extends JPanel {

	private static final long serialVersionUID = 1L;
	private JFreeChart chart;
	private TimeSeriesCollection dataset;
	private String outputPath;
	private String date;
	private JComboBox<String> startP;
	private JComboBox<String> endP;
	private Calendar startC;
	private Calendar endC;
	private XYPlot plot;
	private ArrayList<GraphPoint> points;
	
	public Graph(String o, String d, NyueumaiersGUI p, Calendar start, Calendar end) {
		outputPath = o;
		date = d;
		Calendar s = (Calendar) start.clone();
		Calendar e = (Calendar) end.clone();
		startP = createDropDown(start, end, false);
		startP.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		try {
        			@SuppressWarnings("unchecked")
					JComboBox<String> j = (JComboBox<String>) e.getSource();
            		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        			Calendar date = Calendar.getInstance();
        			date.setTime(sdf.parse((String) j.getSelectedItem()));
        			if (endC == null || (endC != null && date.getTime().before(endC.getTime()))) {
        				startC = Calendar.getInstance();
            			startC.setTime(sdf.parse((String) j.getSelectedItem()));
            			setChart();
                		plot.setDataset(dataset);
        			}
        		} catch (ParseException e1) {
        			e1.printStackTrace();
        		}
            }
        });
		endP = createDropDown(s, e, true);
		endP.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		try {
            		@SuppressWarnings("unchecked")
					JComboBox<String> j = (JComboBox<String>) e.getSource();
            		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        			Calendar date = Calendar.getInstance();
        			date.setTime(sdf.parse((String) j.getSelectedItem()));
        			if (startC == null || (startC != null && date.getTime().after(startC.getTime()))) {
        				endC = Calendar.getInstance();
            			endC.setTime(sdf.parse((String) j.getSelectedItem()));
            			setChart();
                		plot.setDataset(dataset);
        			}
        		} catch (ParseException e1) {
        			e1.printStackTrace();
        		}
            }
        });
		getPoints();
		
		JPanel buttons = new JPanel();
		buttons.setLayout((new BoxLayout(buttons, BoxLayout.X_AXIS)));
		buttons.add(createSaveButton());
		buttons.add(startP);
		buttons.add(endP);
		
		setLayout(new BoxLayout(this, 1));
		add(createGraph());
		add(buttons);
	}
	
	private ChartPanel createGraph() {
		
		setChart();
	   	
	   	plot = (XYPlot) chart.getPlot();
	   	plot.setDomainAxis(new TmxDateAxis());
	   	
	   	DateAxis axis = (DateAxis) plot.getDomainAxis();
        axis.setDateFormatOverride(new SimpleDateFormat("dd-MM-yyyy, hh:mm"));
        axis.setLabelAngle(-45);
        axis.setVerticalTickLabels(true);
        ChartPanel c = new ChartPanel(chart);
        c.setDomainZoomable(true);
        c.setRangeZoomable(true);
        return c;
	}
	
	private SubmitButton createSaveButton() {
		final SubmitButton save = new SubmitButton("Save Graph", 60, 20);
		
        save.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
                if (e.getSource() == save) {
                	try {
            			ChartUtilities.saveChartAsJPEG(new File(outputPath + "/Graph" + date + ".jpg"), chart, 700, 500);
            		} catch (IOException f) {
            			f.printStackTrace();
            		}
                }
            }
        });
        
        return save;
	}
	
	private void getPoints() {
		points = new ArrayList<GraphPoint>();
		
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(outputPath + "/OrderBook" + date + ".csv"));
			String l = br.readLine();
			while ((l = br.readLine()) != null) {
				String[] b = l.replace("\"", "").split(",");
				if (b[9].equals("B")) {
					if ((l = br.readLine()) != null) {
						String[] s = l.replace("\"", "").split(",");
						if (s[9].equals("A")) {
							
							SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHH:mm:ss");
							Calendar date = Calendar.getInstance();
							date.setTime(formatter.parse(s[1] + s[2]));
							boolean dateIn = false;
							
							for (GraphPoint p : points) {
								if (date.getTime().equals(p.getDate().getTime())) {
									dateIn = true;
									p.addToValue((Double.parseDouble(s[3]) - Double.parseDouble(b[3])) / Double.parseDouble(b[3]));
								}
							}
							if (!dateIn) {
								points.add(new GraphPoint(s[1] + s[2], ((Double.parseDouble(s[3]) - Double.parseDouble(b[3])) / Double.parseDouble(b[3]))));
							}
						}
					}
				}				
			}
 
		} catch (IOException | ParseException e) {
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
	
	private JComboBox<String> createDropDown(Calendar s, Calendar e, boolean end) {
		JComboBox<String> j = new JComboBox<String>();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		for (Calendar i = s; !i.getTime().after(e.getTime()); i.add(Calendar.HOUR_OF_DAY, 1)) {
			j.addItem(sdf.format(i.getTime()));
		}
		if (end) {
			j.setSelectedItem(j.getItemAt(j.getItemCount() - 1));
		}
		return j;
	}
	
	protected JPanel updateGraph() {
		JPanel p = new JPanel();
		JPanel buttons = new JPanel();
		buttons.setLayout((new BoxLayout(buttons, BoxLayout.X_AXIS)));
		buttons.add(createSaveButton());
		buttons.add(startP);
		buttons.add(endP);
		buttons.add(new JLabel("HELLO"));
		
		p.setLayout(new BoxLayout(this, 1));
		p.add(createGraph());
		p.add(buttons);
		return p;
	}
	/*
	private String getLatestOrderbook() {
		File[] listOfFiles = new File(outputPath).listFiles(); 
		long lastMod = Long.MIN_VALUE;
		File latest = null;
		
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				String files = listOfFiles[i].getName();
				
				if ((files.endsWith(".csv") || files.endsWith(".CSV")) && files.contains("OrderBook")) {
					if (listOfFiles[i].lastModified() > lastMod) {
						latest = listOfFiles[i];
						lastMod = listOfFiles[i].lastModified();
					}
				}
			}
		}
		String[] name = latest.getName().split("\\.")[0].split("-");
		date = "-"+name[1]+"-"+name[2];
		return latest.getAbsolutePath();
	}

	public static void main(String[] args) {
		getPoints();
		JFrame f = new JFrame();
		JPanel er = new JPanel();
		ArrayList<GraphPoint> points = new ArrayList<GraphPoint>();
		for (double i = 0; i < 101; i++) {
			points.add(new GraphPoint("2011030101:01:0"+i, i));
		}
		er.add(new Graph(points));
		JScrollPane jsp = new JScrollPane(er);
        f.add(jsp);
        f.setSize(400,270);
        f.setVisible(true);
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        f.setLocation((d.width / 2 - 260), (d.height / 2 - 260));
	}*/
	
	@SuppressWarnings("deprecation")
	private void setChart() {
		TimeSeries pop = new TimeSeries("Loss/Gain", Second.class);
		pop.setNotify(false);
		for (GraphPoint p : points) {
			Calendar date = p.getDate();
			if ((startC == null && endC == null) ||
				(startC != null && endC != null && startC.getTime().before(endC.getTime()) && 
				(startC.getTime().before(date.getTime()) || startC.getTime().equals(date.getTime())) && 
				(endC.getTime().after(date.getTime()) || endC.getTime().equals(date.getTime())) ) ||
			    (startC != null && endC == null && 
			    (startC.getTime().before(date.getTime()) || startC.getTime().equals(date.getTime()))) ||
			    (startC == null && endC != null && 
			    (endC.getTime().after(date.getTime()) || endC.getTime().equals(date.getTime())))
			){
				pop.add(new Second(date.get(Calendar.SECOND), date.get(Calendar.MINUTE), date.get(Calendar.HOUR_OF_DAY), 
							   	   date.get(Calendar.DAY_OF_MONTH), date.get(Calendar.MONTH), date.get(Calendar.YEAR)), p.getValue());
			}
		}
		pop.setNotify(true);
		
	   	dataset = new TimeSeriesCollection(pop);
	   	dataset.setDomainIsPointsInTime(true);
	   	//chart = ChartFactory.createXYLineChart("Gains/Loss of Trades", "Date", "Loss/Gain", dataset);
	   	chart = ChartFactory.createTimeSeriesChart(
	   			"Gains/Loss of Trades", //Graph Title
			    "Date", 				//x-axis label
			    "Loss/Gain",			//y-axis label
			   	dataset,				//dataset containing data to graph
			   	false,					//show legend
			   	false,					//generate tooltips
			   	false);					//generate urls
	}
}
