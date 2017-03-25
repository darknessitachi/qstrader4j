//import click
package examples;

import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.abigdreamer.ark.framework.collection.Mapx;

import qstrader.PriceParser;
import qstrader.event.Event;

//
//from qstrader import settings
//from qstrader.compat import queue
//from qstrader.price_parser import PriceParser
//from qstrader.price_handler.ig import IGTickPriceHandler
//from qstrader.strategy import DisplayStrategy
//from qstrader.position_sizer.fixed import FixedPositionSizer
//from qstrader.risk_manager.example import ExampleRiskManager
//from qstrader.portfolio_handler import PortfolioHandler
//from qstrader.compliance.example import ExampleCompliance
//from qstrader.execution_handler.ib_simulated import IBSimulatedExecutionHandler
//from qstrader.statistics.simple import SimpleStatistics
//from qstrader.trading_session.backtest import Backtest
//
//from trading_ig import (IGService, IGStreamService)
//
//
public class display_prices_ig{
	public static Mapx<String, Object> run(Mapx<String, Object> config, boolean testing, List<String> tickers,
			String filename, int n, int n_window) {
//
//    # Set up variables needed for backtest
		Queue<Event> events_queue = new LinkedBlockingQueue<>();
		String csv_dir = config.getString("CSV_DATA_DIR");
		long initial_equity = PriceParser.parse(50_0000.00);
//
//    ig_service = IGService(config.IG.USERNAME, config.IG.PASSWORD, config.IG.API_KEY, config.IG.ACCOUNT.TYPE)
//
//    ig_stream_service = IGStreamService(ig_service)
//    ig_session = ig_stream_service.create_session()
//    accountId = ig_session[u'accounts'][0][u'accountId']
//
//    ig_stream_service.connect(accountId)
//
//
//    # Use IG Tick Price Handler
//    price_handler = IGTickPriceHandler(
//        events_queue, ig_stream_service, tickers
//    )
//
//    # Use the Display Strategy
//    strategy = DisplayStrategy(n=n, n_window=n_window)
//
//    # Use an example Position Sizer
//    position_sizer = FixedPositionSizer()
//
//    # Use an example Risk Manager
//    risk_manager = ExampleRiskManager()
//
//    # Use the default Portfolio Handler
//    portfolio_handler = PortfolioHandler(
//        initial_equity, events_queue, price_handler,
//        position_sizer, risk_manager
//    )
//
//    # Use the ExampleCompliance component
//    compliance = ExampleCompliance(config)
//
//    # Use a simulated IB Execution Handler
//    execution_handler = IBSimulatedExecutionHandler(
//        events_queue, price_handler, compliance
//    )
//
//    # Use the default Statistics
//    statistics = SimpleStatistics(config, portfolio_handler)
//
//    # Set up the backtest
//    backtest = Backtest(
//        price_handler, strategy,
//        portfolio_handler, execution_handler,
//        position_sizer, risk_manager,
//        statistics, initial_equity
//    )
//    results = backtest.simulate_trading(testing=testing)
//    statistics.save(filename)
//    return results
//
		return null;
	}
//
	public static void main(String[] args) throws ParseException {
		CommandLineParser parser = new DefaultParser();
		Options options = new Options();
		options.addOption("config", "config", true, "Config filename");// settings.DEFAULT_CONFIG_FILENAME
		options.addOption("testing", "testing", true, "Enable testing mode");
		options.addOption("tickers", "tickers", true, "Tickers (use comma)");// L1:CS.D.GBPUSD.CFD.IP,L1:CS.D.USDJPY.CFD.IP
		options.addOption("filename", "filename", true, "Pickle (.pkl) statistics filename");
		options.addOption("n", "n", true, "Display prices every n price events");// 1
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
