import java.awt.*;
import java.awt.event.*;
import java.io.File;

import javax.swing.*;

/*
 * Creates window and dropdown menu for strategies
 */
public class moduleGUI extends JFrame {

	private static final long serialVersionUID = 1L;
    private String strategy;
    private JPanel parameters;
    private JPanel pg;

    public moduleGUI() {
    	setLayout(new BorderLayout());
       	add(new JLabel(new ImageIcon(getClass().getResource("smaller-banner.png"))));
    	setLayout(new FlowLayout(FlowLayout.CENTER));
    	add(createDropdown());
    	
        //Set JFrame Properties
        setTitle("Module");
    	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	pack();
		setSize(430, 120);
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((d.width / 2 - 700), (d.height / 2 - 400));
		setResizable(true);
		setVisible(true);
    }

    //Create dropdown menu
	private JComboBox<String> createDropdown() {
		String[] stratList = {"Select Strategy...", "Nyueumaiers Momentum Strategy", "Actuators Momentum Strategy"};
		
    	JComboBox<String> strategies = new JComboBox<String>(stratList);
    	strategies.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		@SuppressWarnings("unchecked")
				JComboBox<String> cb = (JComboBox<String>) e.getSource();
        		//If you want to add another strategy, just add else if statement
        		//remove the parameters already on view
        		//then change the size of the window
        		//create the parameter gui and add
        		if (((String) cb.getSelectedItem()).equals("Select Strategy...")  && !((String) cb.getSelectedItem()).equals(strategy)) {
        			strategy = "";
        			if (pg != null) {
        				moduleGUI.this.remove(pg);
        				setSize(430, 120);
        			}
        		} else if (((String) cb.getSelectedItem()).equals("Nyueumaiers Momentum Strategy")  && !((String) cb.getSelectedItem()).equals(strategy)) {
        			strategy = (String) cb.getSelectedItem();
        			if (pg != null) {
        				moduleGUI.this.remove(pg);
        			}
        			moduleGUI.this.setSize(430, 670);

        			pg = new JPanel();
        			pg.setLayout((new BoxLayout(pg, BoxLayout.X_AXIS)));
        			parameters = new JPanel();
        			parameters.add(new NyueumaiersGUI(getPath(), moduleGUI.this));
        			pg.add(parameters);
        			moduleGUI.this.add(pg);
        			moduleGUI.this.paintComponents(moduleGUI.this.getGraphics());
        		} else if (((String) cb.getSelectedItem()).equals("Actuators Momentum Strategy")  && !((String) cb.getSelectedItem()).equals(strategy)) {
        			strategy = (String) cb.getSelectedItem();
        			if (pg != null) {
        				moduleGUI.this.remove(pg);
        			}
        			moduleGUI.this.setSize(430, 630);
        			
        			pg = new JPanel();
        			pg.setLayout(new BoxLayout(pg, BoxLayout.X_AXIS));
        			parameters = new JPanel();
        			pg.add(new ActuatorsGUI(getPath()));
        			moduleGUI.this.add(pg);
        			moduleGUI.this.paintComponents(moduleGUI.this.getGraphics());
        		}
        	}
        });
    	
    	return strategies;
	}
	
	protected JPanel getPP() {
		return parameters;
	}
	
	protected void setPP(JPanel p) {
		parameters = p;
	}
	
	protected JPanel getPG() {
		return pg;
	}
	
	protected void setPG(JPanel p) {
		pg = p;
	}
	
	protected static String getPath() {
		if (!System.getProperty("java.class.path").contains("/")) {
			return System.getProperty("user.dir");
		} else {
			return new File(System.getProperty("java.class.path")).getParent();
		}
	}

	//Run module and create gui
	public static void main(String[] args) {
    	new moduleGUI();
    }
}

