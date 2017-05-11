package qstrader.price_handler;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import com.abigdreamer.ark.commons.collection.DataRow;
import com.abigdreamer.ark.commons.collection.DataTable;
import com.abigdreamer.ark.commons.collection.Filter;

import qstrader.PriceParser;
import qstrader.event.BarEvent;
import qstrader.event.Event;

public class YahooDailyCsvBarPriceHandler extends AbstractBarPriceHandler {
	
	String csv_dir;
	
	LocalDate start_date;
	LocalDate end_date;
	
	Iterator<DataRow> bar_stream;
	
	boolean calc_adj_returns;
	List<String> adj_close_returns;
	
	/**
	 * YahooDailyBarPriceHandler is designed to read CSV files of
	    Yahoo Finance daily Open-High-Low-Close-Volume (OHLCV) data
	    for each requested financial instrument and stream those to
	    the provided events queue as BarEvents.
	 * @param csv_dir
	 * @param events_queue
	 * @param init_tickers
	 */
	public YahooDailyCsvBarPriceHandler(String csv_dir, Queue<Event> events_queue, List<String> init_tickers) {
		this(csv_dir, events_queue, init_tickers, LocalDate.MIN, LocalDate.MAX, false);
	}
	
	public YahooDailyCsvBarPriceHandler(String csv_dir, Queue<Event> events_queue, List<String> init_tickers,
			LocalDate start_date, LocalDate end_date) {
		this(csv_dir, events_queue, init_tickers, start_date, end_date, false);
	}

	/**
	 * Takes the CSV directory, the events queue and a possible
        list of initial ticker symbols then creates an (optional)
        list of ticker subscriptions and associated prices.
	 * @param csv_dir
	 * @param events_queue
	 * @param init_tickers
	 * @param start_date
	 * @param end_date
	 */
	public YahooDailyCsvBarPriceHandler(String csv_dir, Queue<Event> events_queue, List<String> init_tickers,
			LocalDate start_date, LocalDate end_date, boolean calc_adj_returns) {
        this.csv_dir = csv_dir;
        this.events_queue = events_queue;
        this.continue_backtest = true;
        this.tickers = new HashMap<>();
        this.tickers_data = new HashMap<>();
        
        this.start_date = start_date;
        this.end_date = end_date;
        
        if(!init_tickers.isEmpty()) {
            for(String ticker : init_tickers) {
                this.subscribe_ticker(ticker);
            }
        }
        
        this.bar_stream = this._merge_sort_ticker_data().iterator();
        this.calc_adj_returns = calc_adj_returns;
		if (this.calc_adj_returns) {
			this.adj_close_returns = new ArrayList<>();
		}
    }
    
	/**
	 *  Opens the CSV files containing the equities ticks from
        the specified CSV data directory, converting them into
        them into a pandas DataFrame, stored in a dictionary.
	 *  
	 * @author Darkness
	 * @date 2016年12月16日 下午4:24:10
	 * @version V1.0
	 */
    public boolean _open_ticker_price_csv(String ticker){
        String ticker_path = this.csv_dir + ticker + ".csv";
        DataTable dataTable = YahooFinance.getHistoricalPrices(ticker, this.start_date, this.end_date); 
//        		YahooFinance.DEFAULT_FROM.toInstant(), Instant.now());
        dataTable.insertColumn("Ticker", ticker);
        
        this.tickers_data.put(ticker, dataTable);
//        this.tickers_data[ticker]["Ticker"] = ticker
        return true;
    }
    
    /**
     *  Concatenates all of the separate equities DataFrames
        into a single DataFrame that is time ordered, allowing tick
        data events to be added to the queue in a chronological fashion.

        Note that this is an idealised situation, utilised solely for
        backtesting. In live trading ticks may arrive "out of order".
     *  
     * @author Darkness
     * @date 2016年12月14日 下午3:39:12
     * @version V1.0
     */
    public DataTable _merge_sort_ticker_data(){
    	DataTable dataTable = new DataTable(this.tickers_data.values().iterator().next().getDataColumns());
    	
    	for (Map.Entry<String,DataTable> entry : this.tickers_data.entrySet()) {
			String ticker = entry.getKey();
			DataTable singleDataTable = entry.getValue();
			dataTable.union(singleDataTable);
		}
    	dataTable.sort("Date", "asc");
    	
    	LocalDate startDate = this.start_date;
    	LocalDate endDate = this.end_date;
    	return dataTable.filter(new Filter<DataRow>() {
			@Override
			public boolean filter(DataRow dataRow) {
				if(dataRow.getLocalDate("Date").isBefore(startDate)) {
					return false;
				}
				if(dataRow.getLocalDate("Date").isAfter(endDate)) {
					return false;
				}
				return true;
			}
		});
    }

    /**
     * Subscribes the price handler to a new ticker symbol.
     * @param ticker
     */
    public void subscribe_ticker(String ticker){
        if(!this.tickers.containsKey(ticker)) {
			boolean hasData = this._open_ticker_price_csv(ticker);
			if (hasData) {

			} else {
				System.out.println(String.format("Could not subscribe ticker %s as no data CSV found for pricing.", ticker));
			}
			DataTable dft = this.tickers_data.get(ticker);
			DataRow dataRow = dft.get(0);
			long close = PriceParser.parse(dataRow.getString("Close"));
			long adj_close = PriceParser.parse(dataRow.getString("Adj Close"));
			String timestamp = dataRow.getString("Date");

//                this.tickers[ticker] = new TickerPrices(close, adj_close, timestamp);
        } else {
           System.out.println(String.format("Could not subscribe ticker %s as is already subscribed." , ticker));
        }
    }
    
    /**
     *  Obtain all elements of the bar from a row of dataframe
        and return a BarEvent
     *  
     * @author Darkness
     * @date 2016年12月16日 下午4:24:36
     * @version V1.0
     */
    public BarEvent _create_event(LocalDateTime time,int period,String ticker,DataRow row){
        long open_price = PriceParser.parse(row.getString("Open"));
        long high_price = PriceParser.parse(row.getString("High"));
        long low_price = PriceParser.parse(row.getString("Low"));
        long close_price = PriceParser.parse(row.getString("Close"));
        long adj_close_price = PriceParser.parse(row.getString("Adj Close"));
        long volume = row.getLong("Volume");
		BarEvent bev = new BarEvent(ticker, time, period, open_price, high_price, low_price, close_price, volume, adj_close_price);
        return bev;
    }
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    /**
     * Place the next BarEvent onto the event queue.
     */
    public void stream_next(){
    	if(!bar_stream.hasNext()) {
    		 this.continue_backtest = false;
    		 return;
    	}
    	
    	DataRow row = bar_stream.next();
           
		// Obtain all elements of the bar from the dataframe
		String ticker = row.getString("Ticker");
		int period = 86400; // Seconds in a day
		// Create the tick event for the queue
		String time = row.getString("Date");
		
		BarEvent bev = this._create_event(LocalDateTime.parse(time + " 00:00:00", formatter), period, ticker, row);
		// Store event
		this._store_event(bev);
		// Send event to queue
		this.events_queue.offer(bev);
    }
}