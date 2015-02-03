import java.util.ArrayList;

public class Calculations {

	protected static double returns(double p1, double p2) {
		if (p1 > 0 && p2 >= 0) {
			return (p2 - p1)/ p1;
		}
		return 0;
	}
	
	//Do not need to check moving_avg is zero since we already checked when reading in
	protected static double SMA(ArrayList<Double> returns, double moving_avg) {
		double sum = 0;
		for (double ret: returns) {
			sum += ret;
		}
		return (sum / moving_avg);
	}
	
	protected static int trading_signal(double sma1, double sma2, double threshold) {
		int answer = -1;
		if ((sma2 - sma1) > threshold) {
			answer = 1;
		} else if ((sma2 - sma1) < -threshold) {
			answer = 0;
		}
		return answer;
	}
}

