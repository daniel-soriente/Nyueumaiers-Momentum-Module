import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

public class StrategyMain {
	//Global variable to store what errors occur
	private static ArrayList<String> errors;
	private static Date startDate;
	private static long startTime;
	private static String outputDir;
	
	public static void main(String[] args) {
		startDate = new Date();
		startTime = System.currentTimeMillis();
		errors = new ArrayList<String>();
		outputDir = getPath();
		
		if (args.length != 0) {
			new StrategyMain(args[0]);
		} else {
			errors.add("No input for filepath to parameters.properties file *Output Dir set to current directory by default");
			new Log(outputDir, "" , "", "", "", "", startDate, startTime, errors);
		}
		
		System.gc();
		//valgrind
	}
	
	public StrategyMain(String paramFile) {
		Properties prop = new Properties();
		TradesQueue tradeQueue = null;
		
		if (new File(paramFile).exists()) {
			try {
				InputStream config = new FileInputStream(paramFile);
				prop.load(config);
				
				boolean valid_file = checkFileName(prop.getProperty("file"));
				boolean valid_outputDir = checkOutputDirectory(prop.getProperty("outputDir"));
				boolean valid_timeFrame = checkTimeFrameWindow(prop.getProperty("timeFrameStart"), prop.getProperty("timeFrameEnd"));
				boolean valid_avg = checkMovingAvg(prop.getProperty("movingAvg"));
				boolean valid_threshold = checkThreshold(prop.getProperty("threshold"));
				
				if (valid_file && valid_timeFrame && valid_avg && valid_threshold && valid_outputDir) {
					tradeQueue = new TradesQueue(prop.getProperty("file"), 
												 outputDir,
												 prop.getProperty("timeFrameStart"),
												 prop.getProperty("timeFrameEnd"),
												 Double.parseDouble(prop.getProperty("movingAvg")), 
												 Double.parseDouble(prop.getProperty("threshold")),
												 startDate);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			errors.add("invalid parameter.properties path *Output dir set to current directory by default");
		}
		
		if (prop.isEmpty()) {
			new Log(outputDir, "" , "", "", "", "", startDate, startTime, errors);
		} else if (!errors.isEmpty()) {
			new Log(outputDir, prop.getProperty("file"), prop.getProperty("timeFrameStart"),
					prop.getProperty("timeFrameEnd"), prop.getProperty("movingAvg"), 
					prop.getProperty("threshold"), startDate, startTime, errors);
		} else {
			new Log(outputDir, prop.getProperty("file"), prop.getProperty("timeFrameStart"),
					prop.getProperty("timeFrameEnd"), prop.getProperty("movingAvg"), 
					prop.getProperty("threshold"), startDate, startTime, tradeQueue.getCSVDets(), tradeQueue.getComDets());
		}
	}

	private boolean checkFileName(String filename) {
		if (!filename.isEmpty() && new File(filename).exists() && filename.endsWith(".csv")) {
			return true;
		}
		errors.add("Invalid file");
		return false;
	}
	
    private boolean checkOutputDirectory(String output) {
		if (output.isEmpty()) {
			return true;
		} else if (new File(output).isDirectory()) {
			outputDir = output;
			return true;
		}
		//If invalid set to current directory for log file.
		errors.add("Invalid output directory provided *Set to current directory by default");
		return false;
	}
    
    private boolean checkTimeFrameWindow(String start, String end) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd,HH:mm:ss");
		sdf.setLenient(false);
		
		if (start.isEmpty() && end.isEmpty()) {
			//both empty
			//So we analyse entire file
			return true;
		} else if (!start.isEmpty() && !end.isEmpty()) {
			//Time frame window specified
			//Check if they are dates
			//Also start is before end
			try {
				if (sdf.parse(start) != null && sdf.parse(end) != null && sdf.parse(start).before(sdf.parse(end))) {
					return true;
				}
			} catch (ParseException e) {
			}
			errors.add("Invalid Time Frame Window: Check start is before end");
		} else if (!start.isEmpty() && end.isEmpty()) {
			//Start of time frame specified
			//Check if its a date
			try {
				if (sdf.parse(start) != null) {
					return true;
				}
			} catch (ParseException e) {
			}
			errors.add("Invalid Start of time frame window");
		} else if (start.isEmpty() && !end.isEmpty()) {
			//End of time frame specified
			//Check if its a date
			try {
				if (sdf.parse(end) != null) {
					return true;
				}
			} catch (ParseException e) {
			}
			errors.add("Invalid end of time frame window");
		}
		
		return false;
	}
    
    //Check moving average is a number
  	private boolean checkMovingAvg(String mov) {
  		if (!mov.isEmpty() && isPosNumeric(mov)) {
  			return true;
  		}
  		errors.add("Invalid Moving Average");
  		return false;
  	}
  	
  	//Check threshold is a number
  	private boolean checkThreshold(String thres) {
  		if (!thres.isEmpty() && isPosNumeric(thres)) {
  			return true;
  		}
  		errors.add("Invalid Threshold");
  		return false;
  	}
  	
  	private boolean isPosNumeric(String str)  {  
		try  {  
			if (Double.parseDouble(str) >= 0) {
				return true;
			}
		}  catch(NumberFormatException nfe)  {   
			return false;
		}  
		return false; 
	}
	
	protected static String getPath() {
		if (!System.getProperty("java.class.path").contains("/")) {
			return System.getProperty("user.dir");
		} else {
			return new File(new File(System.getProperty("java.class.path")).getParent()).getParent();
		}
	}
}
