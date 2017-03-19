//#!/usr/bin/env/python
//
//import time
//
//from .base import AbstractStrategy
//from ..profiling import s_speed
//from ..event import EventType
//from ..price_parser import PriceParser

package qstrader.strategy;

import qstrader.profiling;
import qstrader.event.TickerEvent;

public class DisplayStrategy extends AbstractStrategy {
	
	/**
	 * A strategy which display ticks / bars
	 * @param n = 10000
	 * @param n_window = 5
	 */
	public DisplayStrategy(){
		this(100, 5);
    }
	
	int i;
	int n;
	int n_window;
	long t0;
	
	public DisplayStrategy(int n, int n_window){
        this.n = n;
        this.n_window = n_window;
        this.t0 = System.currentTimeMillis();
        this.i = 0;
    }
    
	@Override
    public void calculate_signals(TickerEvent event){
		if (this.i % this.n == 0 && this.i != 0) {
			System.out.println(profiling.s_speed(event, this.i, this.t0));
		}
		int temp = this.i % this.n;
		if (temp > 0 && temp < this.n_window) {
			System.out.println(String.format("%d %s", this.i, event));
		}

		this.i += 1;
    }
}