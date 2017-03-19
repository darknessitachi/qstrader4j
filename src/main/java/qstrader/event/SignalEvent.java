package qstrader.event;

/**
 * Handles the event of sending a Signal from a Strategy object.
    This is received by a Portfolio object and acted upon.
 * @author Administrator
 *
 */
public class SignalEvent extends Event {
	
	private String ticker;
	private String action;
	private int suggested_quantity;

	public SignalEvent(String ticker,String action){
		this(ticker, action, 0);
	}
	
	/**
	 * Initialises the SignalEvent.
	 * @param ticker The ticker symbol, e.g. 'GOOG'.
	 * @param action 'BOT' (for long) or 'SLD' (for short).
	 * @param suggested_quantity Optional positively valued integer
            representing a suggested absolute quantity of units
            of an asset to transact in, which is used by the
            PositionSizer and RiskManager.
	 */
    public SignalEvent(String ticker,String action,int suggested_quantity){
        this.type = EventType.SIGNAL;
        this.ticker = ticker;
        this.action = action;
        this.suggested_quantity = suggested_quantity;
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

	public int getSuggested_quantity() {
		return suggested_quantity;
	}

	public void setSuggested_quantity(int suggested_quantity) {
		this.suggested_quantity = suggested_quantity;
	}
}