import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class GraphPoint {
	private Calendar date;
	private Double value;
	
	public GraphPoint(String d, Double v) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHH:mm:ss");
			date = Calendar.getInstance();
			date.setTime(sdf.parse(d));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		value = v;
	}
	
	protected void addToValue(Double v) {
		value += v;
	}
	
	protected Calendar getDate() {
		return date;
	}
	
	protected Double getValue() {
		return value;
	}
}
