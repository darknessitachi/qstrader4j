package qstrader.price_handler;

import java.util.HashMap;
import java.util.Map;

import qstrader.event.BarEvent;
import qstrader.event.Event;

/**
 *  
 * @author Darkness
 * @date 2016年12月13日 上午11:10:09
 * @version V1.0
 */
public abstract class AbstractBarPriceHandler extends AbstractPriceHandler {
	
    public boolean istick() {
        return false;
    }

    public boolean isbar() {
        return true;
    }
    
    /**
     * Store price event for closing price and adjusted closing price
     *  
     * @author Darkness
     * @date 2016年12月14日 下午3:52:19
     * @version V1.0
     */
    public void _store_event(BarEvent event){
        String ticker = event.getTicker();
        this.tickers.put(ticker, event);
    }
    
    /**
     * Returns the most recent actual (unadjusted) closing price.
     *  
     * @author Darkness
     * @date 2016年12月14日 下午4:03:22
     * @version V1.0
     */
    public long get_last_close(String ticker){
        if(this.tickers.containsKey(ticker)) {
        	BarEvent barEvent = (BarEvent)this.tickers.get(ticker);
        	long close_price = barEvent.getClose_price();
            return close_price;
        }else{
           System.out.println(String.format("Close price for ticker %s is not available from the YahooDailyBarPriceHandler.", ticker));
            return 0;
        }
    }
}