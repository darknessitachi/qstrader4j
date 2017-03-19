package qstrader.price_handler;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Queue;

import joinery.DataFrame;
import qstrader.PriceParser;
import qstrader.TickerPrice;
import qstrader.event.Event;
import qstrader.event.TickEvent;

/**
 * HistoricCSVPriceHandler is designed to read CSV files of
  tick data for each requested financial instrument and
  stream those to the provided events queue as TickEvents.
  
 * @author Administrator
 */
public class HistoricCSVTickPriceHandler extends AbstractTickPriceHandler {
	
	private String csv_dir;
	private Queue<Event> events_queue;
	private boolean continue_backtest;
	private Map<String, TickerPrice> tickers;
	private Map<String, DataFrame<Object>> tickers_data;
	private ListIterator<List<Object>> tick_stream;
	
	/**
	 *  Takes the CSV directory, the events queue and a possible
        list of initial ticker symbols, then creates an (optional)
        list of ticker subscriptions and associated prices.
	 * @param csv_dir
	 * @param events_queue
	 * @param init_tickers
	 */
    public HistoricCSVTickPriceHandler(String csv_dir,Queue<Event> events_queue, List<String> init_tickers){
        this.csv_dir = csv_dir;
        this.events_queue = events_queue;
        this.continue_backtest = true;
        this.tickers = new HashMap<>();
        this.tickers_data = new HashMap<>();
        if(init_tickers != null)  {
        	for (String ticker : init_tickers) {
        		 this.subscribe_ticker(ticker);
			}
        }
        
        this.tick_stream = this._merge_sort_ticker_data().iterrows();
    }
    
    /**
     *  Opens the CSV files containing the equities ticks from
  		the specified CSV data directory, converting them into
  		them into a pandas DataFrame, stored in a dictionary.
     * @param ticker
     */
    public void _open_ticker_price_csv(String ticker){
        String ticker_path = this.csv_dir + File.separator + String.format("%s.csv", ticker);
        try {
        	DataFrame<Object> df = DataFrame.readCsv(ticker_path);
			this.tickers_data.put(ticker, df);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    /**
     * Concatenates all of the separate equities DataFrames
        into a single DataFrame that is time ordered, allowing tick
        data events to be added to the queue in a chronological fashion.

        Note that this is an idealised situation, utilised solely for
        backtesting. In live trading ticks may arrive "out of order".
     */
    public DataFrame<Object> _merge_sort_ticker_data(){
    	Collection<DataFrame<Object>> dfs = this.tickers_data.values();
    	
    	DataFrame<Object> all = new DataFrame<>(dfs.iterator().next().columns());
    	for (DataFrame<Object> df : dfs) {
    		ListIterator<List<Object>> rows = df.iterrows();
    		while(rows.hasNext()) {
    			all.append(rows.next());
    		}
		}
    	
    	all = all.sortBy("Time");
        return all;
    }
    
    /**
     * Subscribes the price handler to a new ticker symbol.
     * @param ticker
     */
    public void subscribe_ticker(String ticker){
    	if(this.tickers.containsKey(ticker)) {
    		System.out.println(String.format( "Could not subscribe ticker %s as is already subscribed.", ticker));
    		return;
    	}
    	
    	try {
                this._open_ticker_price_csv(ticker);
                DataFrame<Object> df = this.tickers_data.get(ticker);
                List<Object> row = df.row(0);
                TickerPrice tickerPrice = new TickerPrice();
                tickerPrice.setBid(PriceParser.parse(row.get(2).toString()));
                tickerPrice.setAsk(PriceParser.parse(row.get(3).toString()));
                tickerPrice.setTimestamp(row.get(1).toString());
                this.tickers.put(ticker, tickerPrice);
    	} catch (Exception e) {
    		System.out.println(String.format("Could not subscribe ticker %s as no data CSV found for pricing.", ticker));
		}
    }
    
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM.dd.yyyy HH:mm:ss.SSS");
	
    /**
     * Obtain all elements of the bar a row of dataframe and return a TickEvent
     * @param index
     * @param ticker
     * @param row
     */
    public TickEvent _create_event(String time,String ticker, List<Object> row){
        long bid = PriceParser.parse(row.get(2).toString());//["Bid"])
        long ask = PriceParser.parse(row.get(3).toString());//row["Ask"])
        TickEvent tev = new TickEvent(ticker, LocalDateTime.parse(time, formatter), bid, ask);
        return tev;
    }
    
    /**
     * Place the next TickEvent onto the event queue.
     */
	public void stream_next() {
    	if(!this.tick_stream.hasNext()) {
    		this.continue_backtest = false;
    		return;
    	}
    	
    	List<Object> row = this.tick_stream.next();
		String ticker = row.get(0).toString();
		String time = row.get(1).toString();
        TickEvent tev = this._create_event(time, ticker, row);
        this._store_event(tev);
        this.events_queue.offer(tev);
    }
	
	public boolean isContinue_backtest() {
		return continue_backtest;
	}
}