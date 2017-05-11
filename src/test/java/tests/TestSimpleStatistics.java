package tests;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.abigdreamer.ark.commons.collection.Mapx;
import com.abigdreamer.ark.commons.collection.TwoTuple;

import qstrader.Config;
import qstrader.Portfolio;
import qstrader.PriceParser;
import qstrader.Settings;
import qstrader.price_handler.AbstractTickPriceHandler;
import qstrader.statistics.SimpleStatistics;

/**
 * Test the statistics on a portfolio consisting of
    AMZN and GOOG with various orders to create
    round-trips for both.

    We run a simple and short backtest, and check
    arithmetic for equity, return and drawdown
    calculations along the way.
    
 * @author Administrator
 */
public class TestSimpleStatistics {

	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	
	double Delta = 0.0001;
	
	private Config config;
	private Portfolio portfolio;

	@Before
	public void setUp() {
		this.config = Settings.TEST;
	}
	
	/**
	 * Purchase/sell multiple lots of AMZN, GOOG
        at various prices/commissions to ensure
        the arithmetic in calculating equity, drawdowns
        and sharpe ratio is correct.
	 */
	@Test
	public void test_calculating_statistics() {
		// Create Statistics object
        PriceHandlerMock price_handler = new PriceHandlerMock();
		this.portfolio = new Portfolio(price_handler, PriceParser.parse(500000.00));

		// PortfolioHandler portfolio_handler = new
		// PortfolioHandler(this.portfolio);
		SimpleStatistics statistics = new SimpleStatistics(this.config, this.portfolio);

        // Check initialization was correct
        Assert.assertEquals(PriceParser.display(statistics.getEquity().get(0)), 500000.00, Delta);
        Assert.assertEquals(PriceParser.display(statistics.getDrawdowns().get(0)), 00, Delta);
        Assert.assertEquals(statistics.getEquity_returns().get(0).doubleValue(), 0.0, Delta);

        // Perform transaction and test statistics at this tick
        this.portfolio.transact_position(
            "BOT", "AMZN", 100,
            PriceParser.parse(566.56), PriceParser.parse(1.00)
        );
        LocalDateTime t = LocalDateTime.parse("2000-01-01 00:00:00", formatter);
        statistics.update(t, portfolio);
        Assert.assertEquals(PriceParser.display(statistics.getEquity().get(1)), 499807.00, Delta);
        Assert.assertEquals(PriceParser.display(statistics.getDrawdowns().get(1)), 193.00, Delta);
        Assert.assertEquals(statistics.getEquity_returns().get(1).doubleValue(), -0.0386, Delta);

        // Perform transaction and test statistics at this tick
        this.portfolio.transact_position(
            "BOT", "AMZN", 200,
            PriceParser.parse(566.395), PriceParser.parse(1.00)
        );
        t =  LocalDateTime.parse("2000-01-02 00:00:00", formatter);
        statistics.update(t, portfolio);
        Assert.assertEquals(PriceParser.display(statistics.getEquity().get(2)), 499455.00, Delta);
        Assert.assertEquals(PriceParser.display(statistics.getDrawdowns().get(2)), 545.00, Delta);
        Assert.assertEquals(statistics.getEquity_returns().get(2).doubleValue(), -0.0705, Delta);

        // Perform transaction and test statistics at this tick
        this.portfolio.transact_position(
            "BOT", "GOOG", 200,
            PriceParser.parse(707.50), PriceParser.parse(1.00)
        );
        t = LocalDateTime.parse("2000-01-03 00:00:00", formatter);
        statistics.update(t, portfolio);
        Assert.assertEquals(PriceParser.display(statistics.getEquity().get(3)), 499046.00, Delta);
        Assert.assertEquals(PriceParser.display(statistics.getDrawdowns().get(3)), 954.00, Delta);
        Assert.assertEquals(statistics.getEquity_returns().get(3).doubleValue(), -0.0820, Delta);

        // Perform transaction and test statistics at this tick
        this.portfolio.transact_position(
            "SLD", "AMZN", 100,
            PriceParser.parse(565.83), PriceParser.parse(1.00)
        );
        t = LocalDateTime.parse("2000-01-04 00:00:00", formatter);
        statistics.update(t, portfolio);
        Assert.assertEquals(PriceParser.display(statistics.getEquity().get(4)), 499164.00, Delta);
        Assert.assertEquals(PriceParser.display(statistics.getDrawdowns().get(4)), 836.00, Delta);
        Assert.assertEquals(statistics.getEquity_returns().get(4).doubleValue(), 0.0236, Delta);

        // Perform transaction and test statistics at this tick
        this.portfolio.transact_position(
            "BOT", "GOOG", 200,
            PriceParser.parse(705.545), PriceParser.parse(1.00)
        );
        t = LocalDateTime.parse("2000-01-05 00:00:00", formatter);
        statistics.update(t, portfolio);
        Assert.assertEquals(PriceParser.display(statistics.getEquity().get(5)), 499146.00, Delta);
        Assert.assertEquals(PriceParser.display(statistics.getDrawdowns().get(5)), 854.00, Delta);
        Assert.assertEquals(statistics.getEquity_returns().get(5).doubleValue(), -0.0036, Delta);

        // Perform transaction and test statistics at this tick
        this.portfolio.transact_position(
            "SLD", "AMZN", 200,
            PriceParser.parse(565.59), PriceParser.parse(1.00)
        );
        t = LocalDateTime.parse("2000-01-06 00:00:00", formatter);
        statistics.update(t, portfolio);
        Assert.assertEquals(PriceParser.display(statistics.getEquity().get(6)), 499335.00, Delta);
        Assert.assertEquals(PriceParser.display(statistics.getDrawdowns().get(6)), 665.00, Delta);
        Assert.assertEquals(statistics.getEquity_returns().get(6).doubleValue(), 0.0379, Delta);

        // Perform transaction and test statistics at this tick
        this.portfolio.transact_position(
            "SLD", "GOOG", 100,
            PriceParser.parse(707.92), PriceParser.parse(1.00)
        );
        t = LocalDateTime.parse("2000-01-07 00:00:00", formatter);
        statistics.update(t, portfolio);
        Assert.assertEquals(PriceParser.display(statistics.getEquity().get(7)), 499580.00, Delta);
        Assert.assertEquals(PriceParser.display(statistics.getDrawdowns().get(7)), 420.00, Delta);
        Assert.assertEquals(statistics.getEquity_returns().get(7).doubleValue(), 0.0490, Delta);

        // Perform transaction and test statistics at this tick
        this.portfolio.transact_position(
            "SLD", "GOOG", 100,
            PriceParser.parse(707.90), PriceParser.parse(0.00)
        );
        t = LocalDateTime.parse("2000-01-08 00:00:00", formatter);
        statistics.update(t, portfolio);
        Assert.assertEquals(PriceParser.display(statistics.getEquity().get(8)), 499824.00, Delta);
        Assert.assertEquals(PriceParser.display(statistics.getDrawdowns().get(8)), 176.00, Delta);
        Assert.assertEquals(statistics.getEquity_returns().get(8).doubleValue(), 0.0488, Delta);

        // Perform transaction and test statistics at this tick
        this.portfolio.transact_position(
            "SLD", "GOOG", 100,
            PriceParser.parse(707.92), PriceParser.parse(0.50)
        );
        t = LocalDateTime.parse("2000-01-09 00:00:00", formatter);
        statistics.update(t, portfolio);
        Assert.assertEquals(PriceParser.display(statistics.getEquity().get(9)), 500069.50, Delta);
        Assert.assertEquals(PriceParser.display(statistics.getDrawdowns().get(9)), 00.00, Delta);
        Assert.assertEquals(statistics.getEquity_returns().get(9).doubleValue(), 0.0491, Delta);

        // Perform transaction and test statistics at this tick
        this.portfolio.transact_position(
            "SLD", "GOOG", 100,
            PriceParser.parse(707.78), PriceParser.parse(1.00)
        );
        t = LocalDateTime.parse("2000-01-10 00:00:00", formatter);
        statistics.update(t, portfolio);
        Assert.assertEquals(PriceParser.display(statistics.getEquity().get(10)), 500300.50, Delta);
        Assert.assertEquals(PriceParser.display(statistics.getDrawdowns().get(10)), 00.00, Delta);
        Assert.assertEquals(statistics.getEquity_returns().get(10).doubleValue(), 0.0462, Delta);

        // Test that results are calculated correctly.
        Mapx<String, Object> results = statistics.get_results();
        Assert.assertEquals(results.getDouble("max_drawdown"), 954.00, Delta);
        Assert.assertEquals(results.getDouble("max_drawdown_pct"), 0.1908, Delta);
        Assert.assertEquals(results.getDouble("sharpe"), 1.7575, Delta);
	}
	
	class PriceHandlerMock extends AbstractTickPriceHandler {

		@Override
		public void stream_next() {
		}
		
		@Override
		public TwoTuple<Long, Long> get_best_bid_ask(String ticker) {
			Map<String, TwoTuple<Long, Long>> prices = new HashMap<>();
			prices.put("GOOG", new TwoTuple<>(PriceParser.parse(705.46), PriceParser.parse(705.46)));
			prices.put("AMZN", new TwoTuple<>(PriceParser.parse(564.14), PriceParser.parse(565.14)));
			
			return prices.get(ticker);
		}
	}
}
