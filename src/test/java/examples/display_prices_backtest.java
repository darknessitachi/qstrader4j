package examples;
//import click

//
//from qstrader import settings
//from qstrader.compat import queue
//from qstrader.price_parser import PriceParser
//from qstrader.price_handler.yahoo_daily_csv_bar import YahooDailyCsvBarPriceHandler
//from qstrader.strategy import DisplayStrategy
//from qstrader.position_sizer.fixed import FixedPositionSizer
//from qstrader.risk_manager.example import ExampleRiskManager
//from qstrader.portfolio_handler import PortfolioHandler
//from qstrader.compliance.example import ExampleCompliance
//from qstrader.execution_handler.ib_simulated import IBSimulatedExecutionHandler
//from qstrader.statistics.simple import SimpleStatistics
//from qstrader.trading_session.backtest import Backtest

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
import qstrader.strategy.DisplayStrategy;

public class display_prices_backtest {
	
	public static Mapx<String, Object> run(Mapx<String, Object> config, boolean testing, List<String> tickers,
			String filename, int n, int n_window) {
		//
		// # Set up variables needed for backtest
		Queue<Event> events_queue = new LinkedBlockingQueue<>();
		String csv_dir = config.getString("CSV_DATA_DIR");
		long initial_equity = PriceParser.parse(50_0000.00);
		//
		// # Use Yahoo Daily Price Handler
		AbstractPriceHandler price_handler = new YahooDailyCsvBarPriceHandler(csv_dir, events_queue, tickers);
		//
		// # Use the Display Strategy
		AbstractStrategy strategy = new DisplayStrategy(n, n_window);
		//
		// # Use an example Position Sizer
		AbstractPositionSizer position_sizer = new FixedPositionSizer();
		//
		// # Use an example Risk Manager
		AbstractRiskManager risk_manager = new ExampleRiskManager();
		//
		// # Use the default Portfolio Handler
		PortfolioHandler portfolio_handler = new PortfolioHandler(initial_equity, events_queue, price_handler,
				position_sizer, risk_manager);
		//
		// # Use the ExampleCompliance component
		AbstractCompliance compliance = new ExampleCompliance(
				System.getProperty("user.dir") + File.separator + "compliance-data");
		//
		// # Use a simulated IB Execution Handler
		AbstractExecutionHandler execution_handler = new IBSimulatedExecutionHandler(events_queue, price_handler,
				compliance);
		//
		// # Use the default Statistics
		AbstractStatistics statistics = new SimpleStatistics(config, portfolio_handler);
		//
		// # Set up the backtest
		TradingSession backtest = new TradingSession(price_handler, strategy, portfolio_handler, execution_handler, position_sizer,
				risk_manager, statistics, initial_equity);
		Mapx<String, Object> results = backtest.start_trading(testing);
		statistics.save(filename);
		return results;
		//
	}

	public static void main(String[] args) throws ParseException {
		CommandLineParser parser = new DefaultParser();
		Options options = new Options();
		options.addOption("config", "config", true, "Config filename");// settings.DEFAULT_CONFIG_FILENAME
		options.addOption("testing", "testing", true, "Enable testing mode");
		options.addOption("tickers", "tickers", true, "Tickers (use comma)");// SP500TR
		options.addOption("filename", "filename", true, "Pickle (.pkl) statistics filename");
		options.addOption("n", "n", true, "Display prices every n price events");// 10000
		options.addOption("n_window", "n_window", true, "Display n_window prices");// 5

		// Parse the program arguments
		CommandLine commandLine = parser.parse(options, args);

		String tickersString = commandLine.getOptionValue("tickers");
		String[] tickers = tickersString.split(",");

		boolean testing = false;
		if (commandLine.hasOption("testing")) {
			testing = Boolean.valueOf(commandLine.getOptionValue("testing"));
		}
		// config = settings.from_file(config, testing);
		Mapx<String, Object> config = null;
		String filename = commandLine.getOptionValue("filename");
		int n = 1_0000;
		if (commandLine.hasOption("n")) {
			n = Integer.parseInt(commandLine.getOptionValue("n"));
		}
		int n_window = 5;
		if (commandLine.hasOption("n_window")) {
			n_window = Integer.parseInt(commandLine.getOptionValue("n_window"));
		}
		run(config, testing, Arrays.asList(tickers), filename, n, n_window);
	}

}
