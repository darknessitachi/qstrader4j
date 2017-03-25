package qstrader.event;

import java.time.LocalDateTime;

/**
 * Handles the event of streaming a "Sentiment" value associated
    with a ticker. Can be used for a generic "date-ticker-sentiment"
    service, often provided by many data vendors.
    
 * @author Administrator
 */
public class SentimentEvent extends TickerEvent {

	private String sentiment;
	
	/**
	 * Initialises the SentimentEvent.
	 * @param timestamp - The timestamp when the sentiment was generated.
	 * @param ticker - The ticker symbol, e.g. 'GOOG'.
	 * @param sentiment - A string, float or integer value of "sentiment",
            e.g. "bullish", -1, 5.4, etc.
	 */
	public SentimentEvent(LocalDateTime timestamp,String  ticker,String  sentiment) {
		super(ticker, timestamp);
		this.type = EventType.SENTIMENT;
		this.sentiment = sentiment;
	}

	public String getSentiment() {
		return sentiment;
	}

	public void setSentiment(String sentiment) {
		this.sentiment = sentiment;
	}
}
