package qstrader.price_handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Queue;

import qstrader.event.Event;
import qstrader.price_handler.iterator.AbstractPriceEventIterator;

public class AbstractGenericHandler extends AbstractPriceHandler {
	
	AbstractPriceEventIterator price_event_iterator;
	
    public AbstractGenericHandler(Queue<Event> events_queue, AbstractPriceEventIterator price_event_iterator) {
        this.events_queue = events_queue;
        this.price_event_iterator = price_event_iterator;
        this.continue_backtest = true;
        this.tickers = new HashMap<>();
//        for ticker in this.tickers_lst:
//            this.tickers[ticker] = {};
    }
    
    public void stream_next(){
//        """
//        Place the next PriceEvent (BarEvent or TickEvent) onto the event queue.
//        """
//        try:
//            price_event = next(this.price_event_iterator)
//        except StopIteration:
//            this.continue_backtest = False
//            return
//        except (EmptyTickEvent, EmptyBarEvent):
//            return
//        this._store_event(price_event)
//        this.events_queue.put(price_event)
    }
    
//    @property
//    public void tickers_lst(self):
//        return this.price_event_iterator.tickers_lst

}
//class GenericBarHandler(AbstractGenericHandler, AbstractBarPriceHandler):
//    pass
//class GenericTickHandler(AbstractGenericHandler, AbstractTickPriceHandler):
//        pass
//        
//public void GenericPriceHandler(events_queue, price_event_iterator):
//    if isinstance(price_event_iterator, AbstractBarEventIterator):
//        return GenericBarHandler(events_queue, price_event_iterator)
//    elif isinstance(price_event_iterator, AbstractTickEventIterator):
//        return GenericTickHandler(events_queue, price_event_iterator)
//    else:
//        raise NotImplementedError("price_event_iterator must be instance of")
