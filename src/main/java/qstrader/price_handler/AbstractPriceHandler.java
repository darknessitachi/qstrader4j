package qstrader.price_handler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

import com.github.rapidark.framework.collection.DataTable;

import qstrader.event.Event;
import qstrader.event.TickerEvent;

/**
 * PriceHandler is a base class providing an interface for
    all subsequent (inherited) data handlers (both live and historic).

    The goal of a (derived) PriceHandler object is to output a set of
    TickEvents or BarEvents for each financial instrument and place
    them into an event queue.

    This will replicate how a live strategy would function as current
    tick/bar data would be streamed via a brokerage. Thus a historic and live
    system will be treated identically by the rest of the QSTrader suite.
 *  
 * @author Darkness
 * @date 2016年12月14日 下午4:16:15
 * @version V1.0
 */
public abstract class AbstractPriceHandler {
	
	public Queue<Event> events_queue;
	public boolean continue_backtest;
	public Map<String, TickerEvent> tickers = new HashMap<>();
	public Map<String, DataTable> tickers_data = new HashMap<>();
	
	public boolean istick() {
        return false;
    }

    public boolean isbar() {
        return false;
    }
	
    /**
     *  Unsubscribes the price handler from a current ticker symbol.
     *  
     * @author Darkness
     * @date 2016年12月14日 下午4:13:28
     * @version V1.0
     */
    public void unsubscribe_ticker(String ticker){
    	if(this.tickers.containsKey(ticker)) {
            this.tickers.put(ticker, null);
            this.tickers_data.put(ticker, null);
    	} else {
    		System.out.println(String.format("Could not unsubscribe ticker %s as it was never subscribed.", ticker));
    	}
    }
    
    /**
     * Returns the most recent actual timestamp for a given ticker
     *  
     * @author Darkness
     * @date 2016年12月14日 下午4:16:00
     * @version V1.0
     */
    public LocalDateTime get_last_timestamp(String ticker){
        if(this.tickers.containsKey(ticker)){
            LocalDateTime time = this.tickers.get(ticker).getTime();
            return time;
        }else{
            System.out.println(String.format("Timestamp for ticker %s is not available from the %s." ,ticker));
            return LocalDateTime.MIN;
        }
    }

    public abstract void stream_next();
}
