package qstrader.event;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 
 * @author Darkness
 * @date 2016年12月13日 下午4:24:44
 * @version V1.0
 */
public class TickerEvent extends Event {

	 String ticker;
	 LocalDateTime time;
	
	public TickerEvent(String ticker, LocalDateTime time) {
		this.ticker = ticker;
		this.time = time;
	}

	public String getTicker() {
		return ticker;
	}

	public LocalDateTime getTime() {
		return time;
	}
	
	public static void main(String[] args) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM.dd.yyyy HH:mm:ss.SSS");
		DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
		LocalDateTime time = LocalDateTime.parse("01.02.2016 00:00:01.358", formatter);
		System.out.println(time.format(formatter2));
		LocalDateTime now = LocalDateTime.now();
		System.out.println(now);
		System.out.println(now.getNano());
	}
}
