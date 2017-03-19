//import calendar
//
//from .base import AbstractStrategy
//
//from ..event import (SignalEvent, EventType)
package qstrader.strategy;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import qstrader.event.Event;
import qstrader.event.SignalEvent;
import qstrader.event.TickerEvent;

/**
 * A generic strategy that allows monthly rebalancing of a
    set of tickers, via full liquidation and dollar-weighting
    of new positions.

    Must be used in conjunction with the
    LiquidateRebalancePositionSizer object to work correctly.
 * @author darkness
 *
 */
public class MonthlyLiquidateRebalanceStrategy extends AbstractStrategy {
    
	List<String> tickers;
	Queue<Event> events_queue;
	Map<String, Boolean> tickers_invested;
	
	public MonthlyLiquidateRebalanceStrategy(List<String> tickers, Queue<Event> events_queue) {
		this.tickers = tickers;
		this.events_queue = events_queue;
		this.tickers_invested = this._create_invested_list();
	}
    
    /**
     * Create a dictionary with each ticker as a key, with
        a boolean value depending upon whether the ticker has
        been "invested" yet. This is necessary to avoid sending
        a liquidation signal on the first allocation.
     *  
     * @author Darkness
     * @date 2016年12月14日 下午6:13:08
     * @version V1.0
     */
    public Map<String, Boolean> _create_invested_list(){
    	Map<String, Boolean> tickers_invested = new HashMap<>();
    	for (String ticker : this.tickers) {
    		tickers_invested.put(ticker, false);
		}
        return tickers_invested;
    }
    
    /**
     * Determine if the current day is at the end of the month.
     *  
     * @author Darkness
     * @date 2016年12月14日 下午6:13:14
     * @version V1.0
     */
    public boolean _end_of_month(String cur_time){
    	LocalDate current = LocalDate.parse(cur_time);
    	LocalDate lastDayOfMonth =current .with(TemporalAdjusters.lastDayOfMonth());
    	return current.equals(lastDayOfMonth);
    }
    
    /**
     * For a particular received BarEvent, determine whether
        it is the end of the month (for that bar) and generate
        a liquidation signal, as well as a purchase signal,
        for each ticker.
     *  
     * @author Darkness
     * @date 2016年12月14日 下午6:13:01
     * @version V1.0
     */
    @Override
    public void calculate_signals(TickerEvent event){
		if (this._end_of_month(event.getTime())) {
            String ticker = event.getTicker();
            if (this.tickers_invested.getOrDefault(ticker, false)) {
            	SignalEvent liquidate_signal = new SignalEvent(ticker, "EXIT");
                this.events_queue.offer(liquidate_signal);
            }
            SignalEvent long_signal = new SignalEvent(ticker, "BOT");
            this.events_queue.offer(long_signal);
            this.tickers_invested.put(ticker, true);
        }
    }
}