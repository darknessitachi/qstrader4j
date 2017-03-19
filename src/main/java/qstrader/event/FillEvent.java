package qstrader.event;

/**
 *  Encapsulates the notion of a filled order, as returned
    from a brokerage. Stores the quantity of an instrument
    actually filled and at what price. In addition, stores
    the commission of the trade from the brokerage.

    TODO: Currently does not support filling positions at
    different prices. This will be simulated by averaging
    the cost.
 * @author Administrator
 *
 */
public class FillEvent extends Event {

    private String timestamp;
	private String ticker;
	private String action;
	private long quantity;
	private String exchange;
	private double price;
	private double commission;

	/**
	 * Initialises the FillEvent object.
	 * @param timestamp - The timestamp when the order was filled.
	 * @param ticker - The ticker symbol, e.g. 'GOOG'.
	 * @param action - 'BOT' (for long) or 'SLD' (for short).
	 * @param quantity - The filled quantity.
	 * @param exchange - The exchange where the order was filled.
	 * @param price - The price at which the trade was filled
	 * @param commission - The brokerage commission for carrying out the trade.
	 */
	public  FillEvent(
        String timestamp,String ticker,
        String   action,long quantity,
        String exchange, double price,
        double commission
    ) {
        this.type = EventType.FILL;
        this.timestamp = timestamp;
        this.ticker = ticker;
        this.action = action;
        this.quantity = quantity;
        this.exchange = exchange;
        this.price = price;
        this.commission = commission;
    }

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
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

	public String getExchange() {
		return exchange;
	}

	public void setExchange(String exchange) {
		this.exchange = exchange;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getCommission() {
		return commission;
	}

	public void setCommission(double commission) {
		this.commission = commission;
	}
}
