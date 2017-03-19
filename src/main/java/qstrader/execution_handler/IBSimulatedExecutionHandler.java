package qstrader.execution_handler;

import java.util.Queue;

import com.github.rapidark.framework.collection.TwoTuple;

import qstrader.PriceParser;
import qstrader.compliance.AbstractCompliance;
import qstrader.event.Event;
import qstrader.event.FillEvent;
import qstrader.event.OrderEvent;
import qstrader.price_handler.AbstractBarPriceHandler;
import qstrader.price_handler.AbstractPriceHandler;
import qstrader.price_handler.AbstractTickPriceHandler;

/**
 * The simulated execution handler for Interactive Brokers
	converts all order objects into their equivalent fill
	objects automatically without latency, slippage or
	fill-ratio issues.
	
	This allows a straightforward "first go" test of any strategy,
	before implementation with a more sophisticated execution
	handler.
 *  
 * @author Darkness
 * @date 2016年12月16日 下午2:37:33
 * @version V1.0
 */
public class IBSimulatedExecutionHandler implements AbstractExecutionHandler {

	Queue<Event> events_queue;
	AbstractPriceHandler price_handler;
	AbstractCompliance compliance;

	public IBSimulatedExecutionHandler(Queue<Event> events_queue, AbstractPriceHandler price_handler) {
		this(events_queue, price_handler, null);
	}
	 
	/**
	 *  Initialises the handler, setting the event queue
        as well as access to local pricing.

	 * @param events_queue The Queue of Event objects.
	 * @param price_handler
	 * @param compliance
	 */
    public IBSimulatedExecutionHandler(Queue<Event>  events_queue, AbstractPriceHandler price_handler, 
    		AbstractCompliance compliance){
        this.events_queue = events_queue;
        this.price_handler = price_handler;
        this.compliance = compliance;
    }
    
    /**
     *  Calculate the Interactive Brokers commission for
        a transaction. This is based on the US Fixed pricing,
        the details of which can be found here:
        https://www.interactivebrokers.co.uk/en/index.php?f=1590&p=stocks1
     *  
     * @author Darkness
     * @date 2016年12月16日 下午2:39:41
     * @version V1.0
     */
	public long calculate_ib_commission(long quantity, double fill_price) {
		double commission = Math.min(0.5 * fill_price * quantity, Math.max(1.0, 0.005 * quantity));
		return PriceParser.parse(commission);
	}
    
	/**
	 *  Converts OrderEvents into FillEvents "naively",
        i.e. without any latency, slippage or fill ratio problems.

        @param event - An Event object with order information.
	 */
	@Override
    public void execute_order(OrderEvent orderEvent){
		// # Obtain values from the OrderEvent
		String timestamp = this.price_handler.get_last_timestamp(orderEvent.getTicker());
		String ticker = orderEvent.getTicker();
		String action = orderEvent.getAction();
		long quantity = orderEvent.getQuantity();

		// # Obtain the fill price
		double fill_price;
		if (this.price_handler.istick()) {
			AbstractTickPriceHandler tickPriceHandler = (AbstractTickPriceHandler) this.price_handler;
			TwoTuple<Double, Double> bidAndAsk = tickPriceHandler.get_best_bid_ask(ticker);
			double bid, ask;
			bid = bidAndAsk.first;
			ask = bidAndAsk.second;
			if (orderEvent.getAction().equals("BOT")) {
				fill_price = ask;
			} else {
				fill_price = bid;
			}
		} else {
			AbstractBarPriceHandler barPriceHandler = (AbstractBarPriceHandler) this.price_handler;
			double close_price = barPriceHandler.get_last_close(ticker);
			fill_price = close_price;
		}
            
		// # Set a dummy exchange and calculate trade commission
		String exchange = "ARCA";
		double commission = this.calculate_ib_commission(quantity, fill_price);

		// # Create the FillEvent and place on the events queue
		FillEvent fill_event = new FillEvent(timestamp, ticker, action, quantity, exchange, fill_price, commission);
		this.events_queue.offer(fill_event);

		if (this.compliance != null) {
			this.compliance.record_trade(fill_event);
		}
    }
}