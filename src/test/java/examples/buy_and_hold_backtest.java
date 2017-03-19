package examples;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.github.rapidark.framework.collection.Mapx;

import qstrader.PortfolioHandler;
import qstrader.PriceParser;
import qstrader.TradingSession;
import qstrader.compliance.AbstractCompliance;
import qstrader.compliance.ExampleCompliance;
import qstrader.event.Event;
import qstrader.execution_handler.AbstractExecutionHandler;
import qstrader.execution_handler.IBSimulatedExecutionHandler;
import qstrader.position_sizer.AbstractPositionSizer;
import qstrader.position_sizer.FixedPositionSizer;
import qstrader.price_handler.AbstractPriceHandler;
import qstrader.price_handler.YahooDailyCsvBarPriceHandler;
import qstrader.risk_manager.AbstractRiskManager;
import qstrader.risk_manager.ExampleRiskManager;
import qstrader.statistics.AbstractStatistics;
import qstrader.statistics.SimpleStatistics;
import qstrader.strategy.AbstractStrategy;
import qstrader.strategy.BuyAndHoldStrategy;
import qstrader.strategy.DisplayStrategy;
import qstrader.strategy.Strategies;

public class buy_and_hold_backtest {

	public static Mapx<String, Object> run(Mapx<String, Object> config, boolean testing, List<String> tickers, String filename) {
		// Set up variables needed for backtest
		Queue<Event> events_queue = new LinkedBlockingQueue<>();
		String csv_dir = config.getString("CSV_DATA_DIR");
	  
		/**
		 * 资产（assets）=负债(liability)+权益(equity) 这些资源中有一部分向债权人借的，叫负债（liability）。
		 * 还有一部分是企业所有者自有的，就是equity：权益
		 */
		long initial_equity = PriceParser.parse(50_0000.00);

		// Use Yahoo Daily Price Handler
		AbstractPriceHandler price_handler = new YahooDailyCsvBarPriceHandler(csv_dir, events_queue, tickers);
		
		// Use the Buy and Hold Strategy
		AbstractStrategy strategy = new BuyAndHoldStrategy(tickers, events_queue);
		strategy = new Strategies(strategy, new DisplayStrategy());
		
		// Use an example Position Sizer
		AbstractPositionSizer position_sizer = new FixedPositionSizer();
	
		// Use an example Risk Manager
		AbstractRiskManager risk_manager = new ExampleRiskManager();
	
		// Use the default Portfolio Handler
		PortfolioHandler portfolio_handler = new PortfolioHandler(initial_equity, events_queue, price_handler, position_sizer, risk_manager);
	
		// Use the ExampleCompliance component
		AbstractCompliance compliance = new ExampleCompliance(System.getProperty("user.dir") + File.separator + "compliance-data");
	
		// Use a simulated IB Execution Handler
		AbstractExecutionHandler execution_handler = new IBSimulatedExecutionHandler(events_queue, price_handler, compliance);
	
		// Use the default Statistics
		AbstractStatistics statistics = new SimpleStatistics(config, portfolio_handler);
	
		// Set up the backtest
		TradingSession backtest = new TradingSession(price_handler, strategy, portfolio_handler, execution_handler, position_sizer, risk_manager, statistics, initial_equity);
	    Mapx<String, Object> results = backtest.start_trading(testing);
	    statistics.save(filename);
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

		String tickersString= commandLine.getOptionValue("tickers");
		String[] tickers = tickersString.split(",");
		
		boolean testing = false;
		if(commandLine.hasOption("testing")) {
			testing = Boolean.valueOf(commandLine.getOptionValue("testing"));
		}
		// config = settings.from_file(config, testing);
		Mapx<String, Object> config = null;
		String filename = commandLine.getOptionValue("filename");
		run(config, testing, Arrays.asList(tickers), filename);
	}
}
