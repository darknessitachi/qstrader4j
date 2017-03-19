package qstrader.event;

import java.time.LocalDateTime;

import qstrader.PriceParser;

/**
 *  Handles the event of receiving a new market update tick,
    which is public voidined as a ticker symbol and associated best
    bid and ask from the top of the order book.
    
 * @author Administrator
 *
 */
public class TickEvent extends TickerEvent {
	
	private long bid;
	private long ask;

	/**
	 * Initialises the TickEvent.
	 * @param ticker The ticker symbol, e.g. 'GOOG'.
	 * @param time The timestamp of the tick
	 * @param bid The best bid price at the time of the tick.
	 * @param ask The best ask price at the time of the tick.
	 */
    public TickEvent(String ticker, LocalDateTime time, long bid, long ask) {
        super(ticker, time);
        
        this.type = EventType.TICK;
        this.bid = bid;
        this.ask = ask;
    }
    
    @Override
    public String toString() {
		double bid = PriceParser.display(this.bid);
		double ask = PriceParser.display(this.ask);

		String string = String.format("Type: %s, Ticker: %s, Time: %s, Bid: %s, Ask: %s", this.type, this.ticker,
				this.time, bid, ask);
    			
    	return string;
    }

	public long getBid() {
		return bid;
	}

	public void setBid(long bid) {
		this.bid = bid;
	}

	public long getAsk() {
		return ask;
	}

	public void setAsk(long ask) {
		this.ask = ask;
	}


}
