//from .base import AbstractStrategy
//
//from collections import deque
//
//import numpy as np
//
//from ..event import (SignalEvent, EventType)
package qstrader.strategy;

import java.util.List;
import java.util.Queue;

import com.github.rapidark.framework.collection.Queuex;

import qstrader.event.BarEvent;
import qstrader.event.Event;
import qstrader.event.EventType;
import qstrader.event.SignalEvent;
import qstrader.event.TickerEvent;

/**
 * Requires:
    tickers - The list of ticker symbols
    events_queue - A handle to the system events queue
    short_window - Lookback period for short moving average
    long_window - Lookback period for long moving average
 * @author darkness
 *
 */
public class MovingAverageCrossStrategy extends AbstractStrategy {
	
	List<String>tickers;
	Queue<Event> events_queue;
	private int short_window;
	private int long_window;
	private int bars;
	private boolean invested;
	private Queuex<Long> sw_bars;
	private Queuex <Long>lw_bars;
	
	public MovingAverageCrossStrategy(List<String> tickers, Queue<Event> events_queue) {
		this(tickers, events_queue, 100, 400);
	}
	
	public MovingAverageCrossStrategy(List<String> tickers, Queue<Event> events_queue, int short_window, int long_window) {
		this.tickers = tickers;
		this.events_queue = events_queue;
		this.short_window = short_window;
		this.long_window = long_window;
		this.bars = 0;
		this.invested = false;
		this.sw_bars = new Queuex<>(this.short_window);
		this.lw_bars = new Queuex<>(this.long_window);
	}
    
	@Override
    public void calculate_signals(TickerEvent event){
		// TODO: Only applies SMA to first ticker
		String ticker = this.tickers.get(0);
        if (event.type == EventType.BAR ) {
        	BarEvent barEvent = (BarEvent)event;
			if (!barEvent.getTicker().equals(ticker)) {
				return;
			}

			// Add latest adjusted closing price to the
			// short and long window bars
			this.lw_bars.push(barEvent.getAdj_close_price());
			if (this.bars > this.long_window - this.short_window) {
				this.sw_bars.push(barEvent.getAdj_close_price());
			}
			// Enough bars are present for trading
			if (this.bars > this.long_window) {
				// Calculate the simple moving averages
				long short_sma = avg(this.sw_bars);
				long long_sma = avg(this.lw_bars);
				// Trading signals based on moving average cross
				if (short_sma > long_sma && !this.invested) {
					System.out.println(String.format("LONG: %s", barEvent.getTime()));
					SignalEvent signal = new SignalEvent(ticker, "BOT");
					this.events_queue.offer(signal);
					this.invested = true;
				} else if (short_sma < long_sma && this.invested) {
					System.out.println(String.format("SHORT: %s", barEvent.getTime()));
					SignalEvent signal = new SignalEvent(ticker, "SLD");
					this.events_queue.offer(signal);
					this.invested = false;
				}
            }
            this.bars += 1;
        }
    }
    
    private long avg(Queuex<Long> datas) {
    	long total = 0;
    	for (int i=0;i<datas.size();i++) {
			total += datas.get(i);
		}
    	return total/datas.size();
    }
}