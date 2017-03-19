//from .base import AbstractStrategy
//
//from ..event import (SignalEvent, EventType)

package qstrader.strategy;

import java.util.List;
import java.util.Queue;

import qstrader.event.Event;
import qstrader.event.EventType;
import qstrader.event.SignalEvent;
import qstrader.event.TickEvent;
import qstrader.event.TickerEvent;

/**
 * A testing strategy that alternates between buying and selling
    a ticker on every 5th tick. This has the effect of continuously
    "crossing the spread" and so will be loss-making strategy.

    It is used to test that the backtester/live trading system is
    behaving as expected.
 *  
 * @author Darkness
 * @date 2016年12月14日 下午6:08:22
 * @version V1.0
 */
public class ExampleStrategy extends AbstractStrategy {
	
	List<String> tickers;
	Queue<Event> events_queue;
	int ticks;
	boolean invested;
	
    public ExampleStrategy(List<String> tickers,Queue<Event> events_queue){
        this.tickers = tickers;
        this.events_queue = events_queue;
        this.ticks = 0;
        this.invested = false;
    }
    
    @Override
    public void calculate_signals(TickerEvent event){
        String ticker = this.tickers.get(0);
        if (event.type == EventType.TICK) {
        	TickEvent tickEvent = (TickEvent)event;
        	if(tickEvent.getTicker().equals(ticker)) {
	            if (this.ticks % 5 == 0){
	                if (!this.invested) {
	                	SignalEvent  signal = new SignalEvent(ticker, "BOT");
	                    this.events_queue.offer(signal);
	                    this.invested = true;
	                }else{
	                	SignalEvent signal =new SignalEvent(ticker, "SLD");
	                    this.events_queue.offer(signal);
	                    this.invested = false;
	                }
	            }
	            this.ticks += 1;
        	}
        }
    }
}