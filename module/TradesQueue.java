import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;

public class TradesQueue {
	private ArrayList<String> orders;
	private ArrayList<String> signals;
	private double[] details;
	private String[] c_details;
	private Date startDate;
	private String outputDir;
			
	public TradesQueue(String filename, String output, String start, String end, 
					   double moving_avg, double threshold, Date startD) {
		
		startDate = startD;
		outputDir = output;
		
		orders = new ArrayList<String>();
		signals = new ArrayList<String>();
		
		c_details = new String[3];
		details = new double[7];
		details[0] = 0;		//lines
		details[1] = 0;		//trades
		details[2] = 0;		//buys
		details[3] = 0;		//sells
		details[4] = 0;		//undefined
		details[5] = 0;		//total gain/loss
		details[6] = 0;		//total gain/loss short selling
		
		Deque<String> trades_queue = new LinkedList<String>();
		double prev_price = 0;
		double prev_SMA = 0;
		int prev_decision = -1;
		String prev_buy = "";
		int first_SMA = 1;

		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));
			String line = br.readLine();
			c_details[1] = "";
			
			while ((line = br.readLine()) != null) {
				if (c_details[1].isEmpty()) {
					String[] f = line.split(",");
					c_details[0] = f[0];
					c_details[1] = parseDate(f[1] + f[2]);
				}
				
				c_details[2] = line;
				
				if (line.contains(",TRADE,") && withinWindow(start, end, line)) {
					//Add to trade count
					details[1]++;
					
					//Add to queue
					trades_queue.addLast(line);
					
					//When queue is size of the moving_avg then analyse trades
					if (trades_queue.size() == moving_avg) {
						
						//Get Returns for all trades in queue
						ArrayList<Double> returns = new ArrayList<Double>();
						for (String trade: trades_queue) {
							String[] trade_details = trade.split(",");
							if (prev_price != 0) {
								returns.add(Calculations.returns(prev_price, Double.parseDouble(trade_details[4])));
							}
							prev_price = Double.parseDouble(trade_details[4]);
						}
						
						//Calculate SMA and then make decision
						//Need two SMAs so need to just calculate SMA when its the first SMA calculated
						if (first_SMA != 1) {
							int decision = Calculations.trading_signal(prev_SMA, Calculations.SMA(returns, moving_avg), threshold);
							
							if (decision != -1) {
								if (decision == 1) {
									String[] buy_details = trades_queue.getLast().split(",");
									signals.add(buy_details[0] + "," + buy_details[1] + "," + buy_details[2] + "," + 
											    buy_details[4] + "," + buy_details[5] + "," + buy_details[7] + "," + 
											    buy_details[9] + "," + buy_details[10] + ",,B");
								} else if (decision == 0) {
									String[] sell_details = trades_queue.getLast().split(",");
									signals.add(sell_details[0] + "," + sell_details[1] + "," + sell_details[2] + "," + 
											 sell_details[4] + "," + sell_details[5] + "," + sell_details[7] + "," + 
											 sell_details[9] + ",," + sell_details[11] + ",A,");
								}
							} else {
								details[4]++;
							}
							
							if (decision != prev_decision && decision != -1) {
								//If first decision made and its a buy or sell add to order book
								//or
								//If there is a previous decision and the new decision calculated doesn't equal the previous one
								//and the decision is buy or sell add to order book
								
								if (decision == 1) {
									details[2]++;
									prev_buy = trades_queue.getLast();
									String[] buy_details = prev_buy.split(",");
									orders.add(buy_details[0] + "," + buy_details[1] + "," + buy_details[2] + "," + 
											 buy_details[4] + "," + buy_details[5] + "," + buy_details[7] + "," + 
											 buy_details[9] + "," + buy_details[10] + ",,B");
								} else if (decision == 0) {
									if (!prev_buy.isEmpty()) {
										String[] buy_dets = prev_buy.split(",");
										String[] sell_dets = trades_queue.getLast().split(",");
										details[5] += ((Double.parseDouble(sell_dets[4]) - Double.parseDouble(buy_dets[4])) / Double.parseDouble(buy_dets[4]));
										details[6] += ((Double.parseDouble(sell_dets[4]) - Double.parseDouble(buy_dets[4])) / Double.parseDouble(sell_dets[4]));
										prev_buy = "";
									}
									
									details[3]++;
									String[] sell_details = trades_queue.getLast().split(",");
									orders.add(sell_details[0] + "," + sell_details[1] + "," + sell_details[2] + "," + 
											 sell_details[4] + "," + sell_details[5] + "," + sell_details[7] + "," + 
											 sell_details[9] + ",," + sell_details[11] + ",A,");
								}
								prev_decision = decision;
							}
						} else {
							//indicate we have calculated the first SMA
							first_SMA = 0;
							//First SMA is undefined, therefore decision is undefined
							details[4]++;
						}
						prev_SMA = Calculations.SMA(returns, moving_avg);
						//When done remove the first trade and then loop again to add another one
						trades_queue.removeFirst();
					}
				}
				//Add to line count
				details[0]++;
			}
			
			String[] f = c_details[2].split(",");
			c_details[2] = parseDate(f[1] + f[2]);
			
			br.close();
			printCSVs();
		} catch (FileNotFoundException e) {  
			e.printStackTrace();  
		} catch (IOException e) {  
			e.printStackTrace();  
		}
	}

	protected double[] getCSVDets () {
		return details;
	}
	
	private boolean withinWindow(String start, String end, String trade) {
		String[] trade_dets = trade.split(",");
		String time = trade_dets[1] + "," + trade_dets[2];
		
		if (start.isEmpty() && end.isEmpty()) {
			return true;
		} else if (start.isEmpty() && !end.isEmpty()) {
			try {
				DateFormat sdf = new SimpleDateFormat("yyyyMMdd,HH:mm:ss");
				sdf.setLenient(false);
				Date tend = sdf.parse(end);
				Date ttrade = sdf.parse(time);
				if (ttrade.equals(tend) || ttrade.before(tend)) {
					return true;
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		} else if (!start.isEmpty() && end.isEmpty()) {
			try {
				DateFormat sdf = new SimpleDateFormat("yyyyMMdd,HH:mm:ss");
				sdf.setLenient(false);
				Date tstart = sdf.parse(start);
				Date ttrade = sdf.parse(time);
				if (ttrade.equals(tstart) || ttrade.after(tstart)) {
					return true;
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		} else {
			try {
				DateFormat sdf = new SimpleDateFormat("yyyyMMdd,HH:mm:ss");
				sdf.setLenient(false);
				Date tstart = sdf.parse(start);
				Date tend = sdf.parse(end);
				Date ttrade = sdf.parse(time);
				if ((ttrade.equals(tstart) || ttrade.after(tstart)) && (ttrade.equals(tend) || ttrade.before(tend))) {
					return true;
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
		return false;
	}
	
	private void printCSVs() {
		if (!orders.isEmpty()) {
			DateFormat dateFileFormat = new SimpleDateFormat("ddMMyyyy-HHmmss");
			String filepath = outputDir + "/NyueumaiersOrderBook-" + dateFileFormat.format(startDate) + ".csv";
			PrintWriter orderWriter = null;
			try {
				orderWriter = new PrintWriter(filepath, "UTF-8");
				orderWriter.println("#Instrument,Date,Time,Price,Volume,Value,Trans ID,Bid ID,Ask ID,Bid/Ask");
			} catch (FileNotFoundException | UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			
			for (String order : orders) {
				orderWriter.println(order);
			}
			orderWriter.close();
		}
		
		if (!signals.isEmpty()) {
			DateFormat dateFileFormat = new SimpleDateFormat("ddMMyyyy-HHmmss");
			String filepath = outputDir + "/NyueumaiersTradeSignals-" + dateFileFormat.format(startDate) + ".csv";
			PrintWriter signalWriter = null;
			try {
				signalWriter = new PrintWriter(filepath, "UTF-8");
				signalWriter.println("#Instrument,Date,Time,Price,Volume,Value,Trans ID,Bid ID,Ask ID,Bid/Ask");
			} catch (FileNotFoundException | UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			
			for (String signal : signals) {
				signalWriter.println(signal);
			}
			signalWriter.close();
		}
	}
	
	protected String[] getComDets() {
		return c_details;
	}
	
	private String parseDate(String d) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHH:mm:ss");
		Calendar date = Calendar.getInstance();
		try {
			date.setTime(sdf.parse(d));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		sdf = new SimpleDateFormat("dd MMM yyyy, HH:mm");
		
		return sdf.format(date.getTime());
	}
}

