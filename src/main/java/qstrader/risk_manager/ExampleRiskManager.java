package qstrader.risk_manager;

import java.util.Arrays;
import java.util.List;

import qstrader.Portfolio;
import qstrader.event.OrderEvent;
import qstrader.order.SuggestedOrder;

public class ExampleRiskManager implements AbstractRiskManager {
	
	/**
	 *  This ExampleRiskManager object simply lets the
        sized order through, creates the corresponding
        OrderEvent object and adds it to a list.
	 */
	@Override
	public List<OrderEvent> refine_orders(Portfolio portfolio, SuggestedOrder sized_order) {
		OrderEvent order_event = new OrderEvent(sized_order.getTicker(), sized_order.getAction(), sized_order.getQuantity());
		return Arrays.asList(order_event);
	}
}
