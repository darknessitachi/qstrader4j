package examples;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.abigdreamer.ark.commons.collection.Mapx;

import qstrader.Config;
import qstrader.PriceParser;
import qstrader.Settings;
import qstrader.TradingSession;
import qstrader.TradingSession.TradingSessionBuilder;
import qstrader.event.Event;
import qstrader.strategy.AbstractStrategy;
import qstrader.strategy.BuyAndHoldStrategy;
import qstrader.strategy.DisplayStrategy;
import qstrader.strategy.Strategies;

public class buy_and_hold_backtest {

	public static Mapx<String, Object> run(Config config, boolean testing, List<String> tickers, String filename) {
		String title = String.format("Buy and Hold Example on %s", tickers);
		
		// Set up variables needed for backtest
		Queue<Event> events_queue = new LinkedBlockingQueue<>();
		String csv_dir = config.CSV_DATA_DIR;
	  
		/**
		 * 资产（assets）=负债(liability)+权益(equity) 这些资源中有一部分向债权人借的，叫负债（liability）。
		 * 还有一部分是企业所有者自有的，就是equity：权益
		 */
		long initial_equity = PriceParser.parse(1_0000.00);

		LocalDate start_date = LocalDate.of(2000, 1, 1);
		LocalDate end_date = LocalDate.of(2014, 1, 1);
			    
		// Use the Buy and Hold Strategy
		AbstractStrategy strategy = new BuyAndHoldStrategy(tickers, events_queue);
		strategy = new Strategies(strategy, new DisplayStrategy());
		
		
		// Set up the backtest
//		backtest = TradingSession(
//		        config, strategy, tickers,
//		        initial_equity, start_date, end_date,
//		        events_queue, title=title
//		    )
		TradingSession backtest = TradingSessionBuilder.create()
			.setConfig(config)
			.setStrategy(strategy)
			.setTickers(tickers)
			.setEquity(initial_equity)
			.setStart_date(start_date)
			.setEnd_date(end_date)
			.setEvents_queue(events_queue)
			.setTitle(title)
			.build();
	
	    Mapx<String, Object> results = backtest.start_trading(testing);
//	    statistics.save(filename);
	    return results;
	}

	public static void main(String[] args) throws ParseException {
		CommandLineParser parser = new DefaultParser();
		Options options = new Options();
		options.addOption("testing", "testing", false, "Enable testing mode");
		options.addOption("config", "config", true, "Config filename");//settings.DEFAULT_CONFIG_FILENAME
		options.addOption("tickers", "tickers", true, "Tickers (use comma)");//SP500TR
		options.addOption("filename", "filename", true, "Pickle (.pkl) statistics filename");
		
		// Parse the program arguments
		CommandLine commandLine = parser.parse(options, args);

		String[] tickers = new String[]{"SPY"};
		
		boolean testing = false;

		String filename = commandLine.getOptionValue("filename");
		run(Settings.TEST, testing, Arrays.asList(tickers), filename);
	}
}
