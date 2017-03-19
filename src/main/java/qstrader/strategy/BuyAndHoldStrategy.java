//from .base import AbstractStrategy
//
//from ..event import (SignalEvent, EventType)

package qstrader.strategy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.apache.xpath.operations.Bool;

import qstrader.event.BarEvent;
import qstrader.event.Event;
import qstrader.event.EventType;
import qstrader.event.SignalEvent;
import qstrader.event.TickerEvent;

/**
 *  A testing strategy that simply purchases (longs) a set of
    assets upon first receipt of the relevant bar event and
    then holds until the completion of a backtest.
 *  
 * @author Darkness
 * @date 2016年12月14日 下午3:57:23
 * @version V1.0
 */
public class BuyAndHoldStrategy extends AbstractStrategy {
	
	List<String>  tickers;
	Queue<Event> events_queue;
	Map<String, Boolean> invested = new HashMap<>();
	
    public BuyAndHoldStrategy(List<String>  tickers, Queue<Event> events_queue){
        this.tickers = tickers;
        this.events_queue = events_queue;
    }
    
    @Override
    public void calculate_signals(TickerEvent tickEvent) {
        String ticker = tickEvent.getTicker();
		if (tickers.contains(ticker)) {
			if (!invested.getOrDefault(ticker, false)) {
				SignalEvent signal = new SignalEvent(ticker, "BOT");
				this.events_queue.offer(signal);
				invested.put(ticker, true);
			}
		}
    }
}