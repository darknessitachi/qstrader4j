package qstrader.event;

/**
 * Handles the event of sending an Order to an execution system.
    The order contains a ticker (e.g. GOOG), action (BOT or SLD)
    and quantity.
 * @author Administrator
 *
 */
public class OrderEvent extends Event {
	
	private String ticker;
	private String action;
	private long quantity;

	/**
	 * Initialises the OrderEvent.
	 * @param ticker The ticker symbol, e.g. 'GOOG'.
	 * @param action 'BOT' (for long) or 'SLD' (for short).
	 * @param quantity The quantity of shares to transact.
	 */
    public OrderEvent(String ticker,String action,long quantity) {
        this.type = EventType.ORDER;
        this.ticker = ticker;
        this.action = action;
        this.quantity = quantity;
}

    /**
     * Outputs the values within the OrderEvent.
     */
    public void print_order() {
        String string = String.format("Order: Ticker=%s, Action=%s, Quantity=%s" ,
                this.ticker, this.action, this.quantity
            );
        System.out.println(string);
    }

	public String getTicker() {
		return ticker;
	}

	public void setTicker(String ticker) {
		this.ticker = ticker;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public long getQuantity() {
		return quantity;
	}

	public void setQuantity(long quantity) {
		this.quantity = quantity;
	}
}

