package qstrader.position_sizer;

import qstrader.Portfolio;
import qstrader.order.SuggestedOrder;

public class NaivePositionSizer implements AbstractPositionSizer {
	
	public NaivePositionSizer( ){
    }
    
	/**
	 *  This NaivePositionSizer object follows all
        suggestions from the initial order without
        modification. Useful for testing simpler
        strategies that do not reside in a larger
        risk-managed portfolio.
	 */
	@Override
	public SuggestedOrder size_order(Portfolio portfolio, SuggestedOrder initial_order) {
        return initial_order;
	}
}
