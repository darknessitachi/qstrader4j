package qstrader.risk_manager;

import java.util.List;

import qstrader.Portfolio;
import qstrader.event.OrderEvent;
import qstrader.order.SuggestedOrder;

/**
 *  The AbstractRiskManager abstract class lets the
    sized order through, creates the corresponding
    OrderEvent object and adds it to a list.
 *  
 * @author Darkness
 * @date 2016年12月16日 下午3:04:24
 * @version V1.0
 */
public interface AbstractRiskManager {
	List<OrderEvent> refine_orders(Portfolio portfolio, SuggestedOrder sized_order);
}