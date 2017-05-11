package qstrader.sentiment_handler;

import java.io.File;
import java.time.LocalDate;
import java.util.List;

import com.abigdreamer.ark.commons.collection.DataTable;

/**
 * SentdexSentimentHandler is designed to provide a backtesting
    sentiment analysis handler for the Sentdex sentiment analysis
    provider (http://sentdex.com/financial-analysis/).

    It uses a CSV file with date-ticker-sentiment tuples/rows.
    Hence in order to avoid implicit lookahead bias a specific
    method is provided "stream_sentiment_events_on_date" that only
    allows sentiment signals to be retrieved for a particular date.
    
 * @author Administrator
 *
 */
public class SentdexSentimentHandler implements AbstractSentimentHandler {

	
	private String csv_dir;
	private String filename;
	private List<?> events_queue;
	private List<String> tickers;
	private LocalDate start_date;
	private LocalDate end_date;
	private DataTable sent_df;

	public SentdexSentimentHandler(String csv_dir, String filename,
	        List<?> events_queue, List<String>tickers,
	        LocalDate start_date,LocalDate end_date) {
		this.csv_dir = csv_dir;
		this.filename = filename;
		this.events_queue = events_queue;
		this.tickers = tickers;
		this.start_date = start_date;
		this.end_date = end_date;
		this.sent_df = this._open_sentiment_csv();
	}
	
	/**
	 * Opens the CSV file containing the sentiment analysis
        information for all represented stocks and places
        it into a pandas DataFrame.
	 */
	private DataTable _open_sentiment_csv() {
		String sentiment_path = this.csv_dir + File.separator + this.filename;
		
//		        sent_df = pd.read_csv(
//		            sentiment_path, parse_dates=True,
//		            header=0, index_col=0,
//		            names=("Date", "Ticker", "Sentiment")
//		        )
//		        if self.start_date is not None:
//		            sent_df = sent_df[self.start_date.strftime("%Y-%m-%d"):]
//		        if self.end_date is not None:
//		            sent_df = sent_df[:self.end_date.strftime("%Y-%m-%d")]
//		        if self.tickers is not None:
//		            sent_df = sent_df[sent_df["Ticker"].isin(self.tickers)]
//		        return sent_df
		return null;
	}
	
	/**
	 * Stream the next set of ticker sentiment values into
        SentimentEvent objects.
	 */
	@Override
	public void stream_next(LocalDate stream_date) {
		if(stream_date == null) {
			System.out.println("No stream_date provided for stream_next sentiment event!");
			return ;
		}
		
//		stream_date_str = stream_date.strftime("%Y-%m-%d")
//        date_df = self.sent_df.ix[stream_date_str:stream_date_str]
//        for row in date_df.iterrows():
//            sev = SentimentEvent(
//                stream_date, row[1]["Ticker"],
//                row[1]["Sentiment"]
//            )
//            self.events_queue.put(sev)
	}

}
