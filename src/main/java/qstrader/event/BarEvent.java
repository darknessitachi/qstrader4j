package qstrader.event;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.rapidark.framework.collection.Mapx;
import com.google.common.collect.Maps;

import qstrader.PriceParser;

/**
 * Handles the event of receiving a new market
    open-high-low-close-volume bar, as would be generated
    via common data providers such as Yahoo Finance.
    
 * @author Administrator
 */
public class BarEvent extends TickerEvent {
	
	
	private int period;
	private long open_price;
	private long high_price;
	private long low_price;
	private long close_price;
	private long volume;
	private long adj_close_price;
	private Object period_readable;
	
	/**
	 * Initialises the BarEvent.
	 * 
	 * @param ticker The ticker symbol, e.g. 'GOOG'.
	 * @param time The timestamp of the bar
	 * @param period The time period covered by the bar in seconds
	 * @param open_price The unadjusted opening price of the bar
	 * @param high_price The unadjusted high price of the bar
	 * @param low_price The unadjusted low price of the bar
	 * @param close_price The unadjusted close price of the bar
	 * @param volume The volume of trading within the bar
	 * @param adj_close_price The vendor adjusted closing price
//            (e.g. back-adjustment) of the bar
	 */
    public BarEvent(
        String ticker,String time,int period,
        long open_price, long high_price,long  low_price,
        long close_price,long  volume, long adj_close_price
    ) {
        this.type = EventType.BAR;
        this.ticker = ticker;
        this.time = time;
        this.period = period;
        this.open_price = open_price;
        this.high_price = high_price;
        this.low_price = low_price;
        this.close_price = close_price;
        this.volume = volume;
        this.adj_close_price = adj_close_price;
        this.period_readable = this._readable_period();
    }
    
    /**
     *  Creates a human-readable period from the number
        of seconds specified for 'period'.

        For instance, converts:
        * 1 -> '1sec'
        * 5 -> '5secs'
        * 60 -> '1min'
        * 300 -> '5min'

        If no period is found in the lookup table, the human
        readable period is simply passed through from period,
        in seconds.
        
     * @return
     */
    public String _readable_period(){
    	Mapx<String, Object> lut = new Mapx<>();
    	lut.put("1", "1sec");
    	lut.put("5", "5sec");
    	lut.put("10", "10sec");
    	lut.put("15", "15sec");
    	lut.put("30", "30sec");
    	lut.put("60", "1min");
    	lut.put("300", "5min");
    	lut.put("600", "10min");
    	lut.put("900", "15min");
    	lut.put("1800", "30min");
    	lut.put("3600", "1hr");
    	lut.put("86400", "1day");
    	lut.put("604800", "1wk");
    	
		if (lut.containsKey(String.valueOf(this.period))) {
			return lut.getString(this.period + "");
		} else {
			return String.format("%ssec", this.period);
		}
    }
    
    public Mapx<String, Object> map(String jsonText) {
    	JSONObject json = JSON.parseObject(jsonText);
    	Mapx<String, Object> params = new Mapx<>(json);	
    	return params;
    }
    
    @Override
    public String toString() {
		String open_price = PriceParser.display(this.open_price);
		String high_price = PriceParser.display(this.high_price);
		String low_price = PriceParser.display(this.low_price);
		String close_price = PriceParser.display(this.close_price);
		String adj_close_price = PriceParser.display(this.adj_close_price);
    	
	 String format_str = String.format("Type: %s, Ticker: %s, Time: %s, Period: %s, " +
	            "Open: %s, High: %s, Low: %s, Close: %s, " +
	            "Adj Close: %s, Volume: %s", 
	                this.type, this.ticker, this.time,
	                this.period_readable, this.open_price,
	                this.high_price, this.low_price,
	                this.close_price, this.adj_close_price,
	                this.volume
	            );
	        return format_str;
    }
    
	public String getTicker() {
		return ticker;
	}

	public void setTicker(String ticker) {
		this.ticker = ticker;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public int getPeriod() {
		return period;
	}

	public void setPeriod(int period) {
		this.period = period;
	}

	public long getOpen_price() {
		return open_price;
	}

	public void setOpen_price(long open_price) {
		this.open_price = open_price;
	}

	public long getHigh_price() {
		return high_price;
	}

	public void setHigh_price(long high_price) {
		this.high_price = high_price;
	}

	public long getLow_price() {
		return low_price;
	}

	public void setLow_price(long low_price) {
		this.low_price = low_price;
	}

	public long getClose_price() {
		return close_price;
	}

	public void setClose_price(long close_price) {
		this.close_price = close_price;
	}

	public long getVolume() {
		return volume;
	}

	public void setVolume(long volume) {
		this.volume = volume;
	}

	public long getAdj_close_price() {
		return adj_close_price;
	}

	public void setAdj_close_price(long adj_close_price) {
		this.adj_close_price = adj_close_price;
	}

	public Object getPeriod_readable() {
		return period_readable;
	}

	public void setPeriod_readable(Object period_readable) {
		this.period_readable = period_readable;
	}

}