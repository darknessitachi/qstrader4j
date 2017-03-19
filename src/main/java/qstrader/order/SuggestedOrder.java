package qstrader.order;

/**
 *  A SuggestedOrder object is generated by the PortfolioHandler
    to be sent to the PositionSizer object and subsequently the
    RiskManager object. Creating a separate object type for
    suggested orders and final orders (OrderEvent objects) ensures
    that a suggested order is never transacted unless it has been
    scrutinised by the position sizing and risk management layers.
 *  
 * @author Darkness
 * @date 2016年12月16日 下午2:46:08
 * @version V1.0
 */
public class SuggestedOrder {
	private String ticker;
	private String action;
	private long quantity;

	/**
	 *  Initialises the SuggestedOrder. The quantity public voidaults
        to zero as the PortfolioHandler creates these objects
        prior to any position sizing.

        The PositionSizer object will "fill in" the correct
        value prior to sending the SuggestedOrder to the
        RiskManager.
            
	 * @param ticker The ticker symbol, e.g. 'GOOG'.
	 * @param action 'BOT' (for long) or 'SLD' (for short) or 'EXIT' (for liquidation).
	 * @param quantity The quantity of shares to transact.
	 */
    public SuggestedOrder(String ticker,String action,long quantity) {
        this.ticker = ticker;
        this.action = action;
        this.quantity = quantity;
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