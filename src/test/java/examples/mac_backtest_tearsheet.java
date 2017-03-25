////import click
//package examples;
//
//import java.io.File;
//import java.util.List;
//import java.util.Queue;
//import java.util.concurrent.LinkedBlockingQueue;
//
//import org.apache.commons.cli.CommandLine;
//import org.apache.commons.cli.CommandLineParser;
//import org.apache.commons.cli.DefaultParser;
//import org.apache.commons.cli.Options;
//import org.apache.commons.cli.ParseException;
//
//import com.github.rapidark.framework.collection.Mapx;
//
//import qstrader.PortfolioHandler;
//import qstrader.PriceParser;
//import qstrader.TradingSession;
//import qstrader.compliance.AbstractCompliance;
//import qstrader.compliance.ExampleCompliance;
//import qstrader.event.Event;
//import qstrader.execution_handler.AbstractExecutionHandler;
//import qstrader.execution_handler.IBSimulatedExecutionHandler;
//import qstrader.position_sizer.AbstractPositionSizer;
//import qstrader.position_sizer.FixedPositionSizer;
//import qstrader.price_handler.AbstractPriceHandler;
//import qstrader.price_handler.YahooDailyCsvBarPriceHandler;
//import qstrader.risk_manager.AbstractRiskManager;
//import qstrader.risk_manager.ExampleRiskManager;
//import qstrader.statistics.AbstractStatistics;
//import qstrader.strategy.AbstractStrategy;
//import qstrader.strategy.MovingAverageCrossStrategy;
//
////
////from qstrader import settings
////from qstrader.compat import queue
////from qstrader.price_parser import PriceParser
////from qstrader.price_handler.yahoo_daily_csv_bar import YahooDailyCsvBarPriceHandler
////from qstrader.strategy.moving_average_cross_strategy import MovingAverageCrossStrategy
////from qstrader.position_sizer.fixed import FixedPositionSizer
////from qstrader.risk_manager.example import ExampleRiskManager
////from qstrader.portfolio_handler import PortfolioHandler
////from qstrader.compliance.example import ExampleCompliance
////from qstrader.execution_handler.ib_simulated import IBSimulatedExecutionHandler
////from qstrader.statistics.tearsheet import TearsheetStatistics
////from qstrader.trading_session.backtest import Backtest
////
//public class mac_backtest_tearsheet {
//
//	public static Mapx<String, Object> run(Mapx<String, Object> config, boolean testing, List<String> tickers, String filename) {
////
////    # Benchmark ticker
////    benchmark = 'SP500TR'
////
////    # Set up variables needed for backtest
////    title = [
////        'Moving Average Crossover Example',
////        __file__,
////        ','.join(tickers) + ': 100x400'
////    ]
//		
//		Queue<Event> events_queue = new LinkedBlockingQueue<>();
//		String csv_dir = config.getString("CSV_DATA_DIR");
//	  
//		/**
//		 * 资产（assets）=负债(liability)+权益(equity) 这些资源中有一部分向债权人借的，叫负债（liability）。
//		 * 还有一部分是企业所有者自有的，就是equity：权益
//		 */
//		long initial_equity = PriceParser.parse(50_0000.00);
//		
////
////    # Use Yahoo Daily Price Handler
//		AbstractPriceHandler price_handler =new YahooDailyCsvBarPriceHandler(
//        csv_dir, events_queue, tickers
//    );
////
////    # Use the MAC Strategy
//		AbstractStrategy strategy =new MovingAverageCrossStrategy(tickers, events_queue);
////
////    # Use an example Position Sizer,
//		AbstractPositionSizer position_sizer =new FixedPositionSizer();
////
////    # Use an example Risk Manager,
//    AbstractRiskManager  risk_manager =new ExampleRiskManager();
////
////    # Use the default Portfolio Handler
//    PortfolioHandler portfolio_handler =new PortfolioHandler(
//        initial_equity, events_queue, price_handler,
//        position_sizer, risk_manager
//    );
////
////    # Use the ExampleCompliance component
//    AbstractCompliance compliance =new ExampleCompliance(System.getProperty("user.dir") + File.separator + "compliance-data");;
////
////    # Use a simulated IB Execution Handler
//    AbstractExecutionHandler execution_handler =new IBSimulatedExecutionHandler(
//        events_queue, price_handler, compliance
//    );
////
////    # Use the default Statistics
//    AbstractStatistics statistics = null;//new TearsheetStatistics(config, portfolio_handler, title, benchmark);
////
////    # Set up the backtest
//    TradingSession backtest =new TradingSession(
//        price_handler, strategy,
//        portfolio_handler, execution_handler,
//        position_sizer, risk_manager,
//        statistics, initial_equity
//    );
//    Mapx<String, Object>  results = backtest.start_trading(testing);
//    statistics.save(filename);
//    return results;
////
////
//	}
//	public static void main(String[] args) throws ParseException {
//		CommandLineParser parser = new DefaultParser();
//		Options options = new Options();
//		options.addOption("config", "config", true, "Config filename");// settings.DEFAULT_CONFIG_FILENAME
//		options.addOption("testing", "testing", true, "Enable testing mode");
//		options.addOption("tickers", "tickers", true, "Tickers (use comma)");// SP500TR
//		options.addOption("filename", "filename", true, "Pickle (.pkl) statistics filename");
//
//		// Parse the program arguments
//		CommandLine commandLine = parser.parse(options, args);
//
//		String tickersString = commandLine.getOptionValue("tickers");
//		String[] tickers = tickersString.split(",");
//
//		boolean testing = false;
//		if (commandLine.hasOption("testing")) {
//			testing = Boolean.valueOf(commandLine.getOptionValue("testing"));
//		}
//		// config = settings.from_file(config, testing);
//		Mapx<String, Object> config = null;
//		String filename = commandLine.getOptionValue("filename");
//		int n = 1_0000;
//		if (commandLine.hasOption("n")) {
//			n = Integer.parseInt(commandLine.getOptionValue("n"));
//		}
//		int n_window = 5;
//		if (commandLine.hasOption("n_window")) {
//			n_window = Integer.parseInt(commandLine.getOptionValue("n_window"));
//		}
//		//run(config, testing, Arrays.asList(tickers), filename, n, n_window);
//	}
//}