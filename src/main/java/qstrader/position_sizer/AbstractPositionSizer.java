package qstrader.position_sizer;

import qstrader.Portfolio;
import qstrader.order.SuggestedOrder;

/**
 *  The AbstractPositionSizer abstract class modifies 
 *  the quantity (or not) of any share transacted
 *  
 * @author Darkness
 * @date 2016年12月16日 下午2:57:53
 * @version V1.0
 */
public interface AbstractPositionSizer {

	/**
	 *  This TestPositionSizer object simply modifies
        the quantity to be 100 of any share transacted.
	 *  
	 * @author Darkness
	 * @date 2016年12月16日 下午2:58:21
	 * @version V1.0
	 */
	SuggestedOrder size_order(Portfolio portfolio, SuggestedOrder initial_order);
}