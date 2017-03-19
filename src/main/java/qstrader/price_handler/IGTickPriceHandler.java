//import pandas as pd
//
//from trading_ig.lightstreamer import Subscription
//
//from ..price_parser import PriceParser
//from ..event import TickEvent
//from .base import AbstractTickPriceHandler

package qstrader.price_handler;

import java.util.List;
import java.util.Queue;

import qstrader.event.Event;

public class IGTickPriceHandler extends AbstractTickPriceHandler {
	
    public IGTickPriceHandler(Queue<Event>events_queue, Object ig_stream_service,List<String> tickers){
//        this.price_event = None
//        this.events_queue = events_queue
//        this.continue_backtest = True
//        this.ig_stream_service = ig_stream_service
//        this.tickers_lst = tickers
//        this.tickers = {}
//        for ticker in this.tickers_lst:
//            this.tickers[ticker] = {}
//
//        # Making a new Subscription in MERGE mode
//        subcription_prices = Subscription(
//            mode="MERGE",
//            items=tickers,
//            fields=["UPDATE_TIME", "BID", "OFFER", "CHANGE", "MARKET_STATE"],
//            # adapter="QUOTE_ADAPTER",
//        )
//
//        # Adding the "on_price_update" function to Subscription
//        subcription_prices.addlistener(this.on_prices_update)
//
//        # Registering the Subscription
//        this.ig_stream_service.ls_client.subscribe(subcription_prices)
    }
    
//    public void on_prices_update( data){
//        tev = this._create_event(data)
//        if this.price_event is not None:
//            print("losing %s" % this.price_event)
//        this.price_event = tev
//    }
//    
//    public void _create_event( data){
//        ticker = data["name"]
//        index = pd.to_datetime(data["values"]["UPDATE_TIME"])
//        bid = PriceParser.parse(data["values"]["BID"])
//        ask = PriceParser.parse(data["values"]["OFFER"])
//        return TickEvent(ticker, index, bid, ask)
//    }
    
    public void stream_next(){
//        """
//        Place the next PriceEvent (BarEvent or TickEvent) onto the event queue.
//        """
//        if this.price_event is not None:
//            this._store_event(this.price_event)
//            this.events_queue.put(this.price_event)
//            this.price_event = None
    }
}