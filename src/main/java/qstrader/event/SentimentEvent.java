package qstrader.event;

/**
 * Handles the event of streaming a "Sentiment" value associated
    with a ticker. Can be used for a generic "date-ticker-sentiment"
    service, often provided by many data vendors.
    
 * @author Administrator
 */
public class SentimentEvent extends TickerEvent {

	private String timestamp;
	private String ticker;
	private String sentiment;
	
	/**
	 * Initialises the SentimentEvent.
	 * @param timestamp - The timestamp when the sentiment was generated.
	 * @param ticker - The ticker symbol, e.g. 'GOOG'.
	 * @param sentiment - A string, float or integer value of "sentiment",
            e.g. "bullish", -1, 5.4, etc.
	 */
	public SentimentEvent(String timestamp,String  ticker,String  sentiment) {
		this.type = EventType.SENTIMENT;
		this.timestamp = timestamp;
		this.ticker = ticker;
		this.sentiment = sentiment;
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

	public String getSentiment() {
		return sentiment;
	}

	public void setSentiment(String sentiment) {
		this.sentiment = sentiment;
	}
}
