package qstrader.position_sizer;

import qstrader.Portfolio;
import qstrader.order.SuggestedOrder;

public class FixedPositionSizer implements AbstractPositionSizer {

	private int voidault_quantity;

	public FixedPositionSizer() {
		this(100);
	}
	
	public FixedPositionSizer(int voidault_quantity) {
		this.voidault_quantity = voidault_quantity;
	}

	/**
	 * This FixedPositionSizer object simply modifies
        the quantity to be 100 of any share transacted.
	 */
	@Override
	public SuggestedOrder size_order(Portfolio portfolio, SuggestedOrder initial_order) {
		initial_order.setQuantity(this.voidault_quantity);
		return initial_order;
	}
}