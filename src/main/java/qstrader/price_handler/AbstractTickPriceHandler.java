package qstrader.price_handler;

import com.abigdreamer.ark.framework.collection.TwoTuple;

import qstrader.event.TickEvent;

/**
 *  
 * @author Darkness
 * @date 2016年12月13日 上午11:10:28
 * @version V1.0
 */
public abstract class AbstractTickPriceHandler extends AbstractPriceHandler {
	
    public boolean istick() {
        return true;
    }

	public boolean isbar(){
        return false;
	}

	/**
	 * Store price event for bid/ask
	 *  
	 * @author Darkness
	 * @date 2016年12月14日 下午4:04:27
	 * @version V1.0
	 */
    public void _store_event(TickEvent event) {
        this.tickers.put(event.getTicker(), event);
    }
    
    /**
     * Returns the most recent bid/ask price for a ticker.
     *  
     * @author Darkness
     * @date 2016年12月14日 下午4:17:13
     * @version V1.0
     */
    public TwoTuple<Long, Long> get_best_bid_ask(String ticker) {
    	if(this.tickers.containsKey(ticker)) {
    		TickEvent event = (TickEvent)this.tickers.get(ticker);
    		return new TwoTuple<>(event.getBid(), event.getAsk());
    	}
    	System.out.println(String.format("Bid/ask values for ticker %s are not available from the PriceHandler." , ticker));
    	return new TwoTuple<>(0L, 0L);
    }
}