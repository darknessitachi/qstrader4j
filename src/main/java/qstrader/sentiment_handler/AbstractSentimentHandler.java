package qstrader.sentiment_handler;

import java.time.LocalDate;

/**
 * AbstractSentimentHandler is an abstract base class providing
    an interface for all inherited sentiment analysis event handlers.

    Its goal is to allow subclassing for objects that read in file-based
    sentiment data (such as CSV files of date-asset-sentiment tuples), or
    streamed sentiment data from an API, and produce an event-driven output
    that sends SentimentEvent objects to the events queue.
    
 * @author Administrator
 *
 */
public interface AbstractSentimentHandler {

	/**
	 * Interface method for streaming the next SentimentEvent
        object to the events queue.
	 * @param stream_date
	 */
	void stream_next(LocalDate stream_date);
}
