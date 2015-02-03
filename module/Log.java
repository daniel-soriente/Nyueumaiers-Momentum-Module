import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Log {
	
	//Method to generate a successful log file
	public Log(String output, String file, String timeStart, String timeEnd, String movingavg,
			   String threshold, Date startDate, long startTime, ArrayList<String> details) {
		DateFormat dateLogFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date start = startDate;
		
		Date endDate = new Date();
		final long endTime = System.currentTimeMillis();
		
		DateFormat dateFileFormat = new SimpleDateFormat("ddMMyyyy-HHmmss");
		String filepath = output + "/NyueumaiersLog-" + dateFileFormat.format(start) + ".txt";
		
		try {
			PrintWriter writer = new PrintWriter(filepath, "UTF-8");
			
			writer.println("\n-----ERROR/S------\n");
			
			ArrayList<String> e = (ArrayList<String>) details;
			for (String error : e) {
				writer.println(error);
			}
			
			writer.println("----PARAMETERS----\n");
			
			if (file.isEmpty()) {
				writer.println("Filename: N/A");
			} else {
				writer.println("Filename: " + file);
			}
			
			writer.println("Output Filepath: " + output);
			
			if (timeStart.isEmpty()) {
				writer.println("Time Frame Start: N/A");
			} else {
				writer.println("Time Frame Start: " + timeStart);
			}
			
			if (timeEnd.isEmpty()) {
				writer.println("Time Frame End: N/A");
			} else {
				writer.println("Time Frame End: " + timeEnd);
			}

			if (movingavg.isEmpty()) {
				writer.println("Moving Average: N/A");
			} else {
				writer.println("Moving Average: " + movingavg);
			}
			
			if (threshold.isEmpty()) {
				writer.println("Threshold: N/A");
			} else {
				writer.println("Threshold: " + threshold);
			}
			
			writer.println("\n------------------\n\n");	
			
			writer.println("Developer Team: P.Raju D.Soriente");
			writer.println("Start Date/Time: " + dateLogFormat.format(startDate));
			writer.println("End Date/Time: " + dateLogFormat.format(endDate));
			writer.println("Execution Time: " + (endTime - startTime) + " milliseconds" + "\n");
			
			writer.println("------------------");
			writer.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}	
	}
	
	public Log(String output, String file, String timeStart, String timeEnd, String movingavg,
			   String threshold, Date startDate, long startTime, double[] details, String[] c_details) {
		DateFormat dateLogFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date start = startDate;
		
		Date endDate = new Date();
		final long endTime = System.currentTimeMillis();
		
		DateFormat dateFileFormat = new SimpleDateFormat("ddMMyyyy-HHmmss");
		String filepath = output + "/NyueumaiersLog-" + dateFileFormat.format(start) + ".txt";
		
		try {
			PrintWriter writer = new PrintWriter(filepath, "UTF-8");
			
			writer.println("----PARAMETERS----\n");
			
			if (file.isEmpty()) {
				writer.println("Filename: N/A");
			} else {
				writer.println("Filename: " + file);
			}
			
			writer.println("Output Filepath: " + output);
			
			if (timeStart.isEmpty()) {
				writer.println("Time Frame Start: N/A");
			} else {
				writer.println("Time Frame Start: " + timeStart);
			}
			
			if (timeEnd.isEmpty()) {
				writer.println("Time Frame End: N/A");
			} else {
				writer.println("Time Frame End: " + timeEnd);
			}

			if (movingavg.isEmpty()) {
				writer.println("Moving Average: N/A");
			} else {
				writer.println("Moving Average: " + movingavg);
			}
			
			if (threshold.isEmpty()) {
				writer.println("Threshold: N/A");
			} else {
				writer.println("Threshold: " + threshold);
			}
			
			writer.println("\n-----SUCCESS------\n");
			
			writer.println("Company: " + c_details[0]);
			writer.println("Date: " + c_details[1] + " - " + c_details[2]);
			writer.println("Lines: " + details[0]);
			writer.println("Trades: " + details[1]);
			writer.println("Buys: " + details[2]);
			writer.println("Sells: " + details[3]);
			writer.println("Undefined: " + details[4]);
			DecimalFormat df = new DecimalFormat("###.##########");
			writer.println("Total Gain/Loss: " + df.format(details[5]));
			writer.println("Total Gain/Loss (Short Selling): " + df.format(details[5]));				
			
			writer.println("\n------------------\n");	
			
			writer.println("Developer Team: Nyueumaiers Team (T. Bhat, Y. Jeong, M. John, P. Raju, D. Soriente)");
			writer.println("Start Date/Time: " + dateLogFormat.format(startDate));
			writer.println("End Date/Time: " + dateLogFormat.format(endDate));
			writer.println("Execution Time: " + (endTime - startTime) + " milliseconds" + "\n");
			
			writer.println("------------------\n");
			writer.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}	
	}
}

