package tests;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.abigdreamer.ark.framework.collection.TwoTuple;

import qstrader.Portfolio;
import qstrader.Position;
import qstrader.PriceParser;
import qstrader.price_handler.AbstractTickPriceHandler;

/**
 * Test a portfolio consisting of Amazon and
    Google/Alphabet with various orders to create
    round-trips for both.

    These orders were carried out in the Interactive Brokers
    demo account and checked for cash, equity and PnL
    equality.
 * @author Administrator
 *
 */
public class TestAmazonGooglePortfolio {

	double Delta = 0.00000001;
	private Portfolio portfolio;

	/**
	 * Set up the Portfolio object that will store the
        collection of Position objects, supplying it with
        $500,000.00 USD in initial cash.
	 */
	@Before
	public void setUp() {
		PriceHandlerMock ph = new PriceHandlerMock();
		long cash = PriceParser.parse(50_0000.00);
		this.portfolio = new Portfolio(ph, cash);
	}
	
	/**
	 * Purchase/sell multiple lots of AMZN and GOOG
        at various prices/commissions to check the
        arithmetic and cost handling.
	 */
	@Test
	public void test_calculate_round_trip() {
		// Buy 300 of AMZN over two transactions
		this.portfolio.transact_position("BOT", "AMZN", 100, PriceParser.parse(566.56), PriceParser.parse(1.00));
		this.portfolio.transact_position("BOT", "AMZN", 200, PriceParser.parse(566.395), PriceParser.parse(1.00));
		// Buy 200 GOOG over one transaction
		this.portfolio.transact_position("BOT", "GOOG", 200, PriceParser.parse(707.50), PriceParser.parse(1.00));
		// Add to the AMZN position by 100 shares
		this.portfolio.transact_position("SLD", "AMZN", 100, PriceParser.parse(565.83), PriceParser.parse(1.00));
		// Add to the GOOG position by 200 shares
		this.portfolio.transact_position("BOT", "GOOG", 200, PriceParser.parse(705.545), PriceParser.parse(1.00));
		// Sell 200 of the AMZN shares
		this.portfolio.transact_position("SLD", "AMZN", 200, PriceParser.parse(565.59), PriceParser.parse(1.00));
		
		// Multiple transactions bundled into one (in IB)
		// Sell 300 GOOG from the portfolio
		this.portfolio.transact_position("SLD", "GOOG", 100, PriceParser.parse(704.92), PriceParser.parse(1.00));
		this.portfolio.transact_position("SLD", "GOOG", 100, PriceParser.parse(704.90), PriceParser.parse(0.00));
		this.portfolio.transact_position("SLD", "GOOG", 100, PriceParser.parse(704.92), PriceParser.parse(0.50));
		
		// Finally, sell the remaining GOOG 100 shares
		this.portfolio.transact_position("SLD", "GOOG", 100, PriceParser.parse(704.78), PriceParser.parse(1.00));

        // The figures below are derived from Interactive Brokers
        // demo account using the above trades with prices provided
        // by their demo feed.
        Assert.assertEquals(this.portfolio.positions.size(), 0);
        Assert.assertEquals(this.portfolio.getClosed_positions().size(), 2);
        
        Assert.assertEquals(PriceParser.display(this.portfolio.getClosed_positions().get(0).getRealised_pnl()), -238.0, Delta);
        Assert.assertEquals(PriceParser.display(this.portfolio.getClosed_positions().get(1).getRealised_pnl()), -661.5, Delta);
        
        Assert.assertEquals(PriceParser.display(this.portfolio.getCur_cash()), 499100.50, Delta);
        Assert.assertEquals(PriceParser.display(this.portfolio.getEquity()), 499100.50, Delta);
        Assert.assertEquals(PriceParser.display(this.portfolio.getUnrealised_pnl()), 0.00, Delta);
        Assert.assertEquals(PriceParser.display(this.portfolio.getRealised_pnl()), -899.50, Delta);

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
