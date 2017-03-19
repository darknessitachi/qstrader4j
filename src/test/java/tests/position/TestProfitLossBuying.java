package tests.position;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import qstrader.Position;
import qstrader.PriceParser;

public class TestProfitLossBuying {
	
	double Delta = 0.00000001;
	private Position position;

	/**
	 *  Tests that the unrealised and realised pnls are
	    working after position initialization, every
	    transaction, and every price update
	 */
	@Before
	public void setUp() {
        this.position = new Position(
            "BOT", "XOM", 100,
            PriceParser.parse(74.78), PriceParser.parse(1.00),
            PriceParser.parse(74.77), PriceParser.parse(74.79)
        );
	}
	
	@Test
	public void test_realised_unrealised_calcs() {
		Assert.assertEquals(PriceParser.display(this.position.getUnrealised_pnl()), -1.00, Delta);
		Assert.assertEquals(PriceParser.display(this.position.getRealised_pnl()), 0.00, Delta);

		this.position.update_market_value(PriceParser.parse(75.77), PriceParser.parse(75.79));
		Assert.assertEquals(PriceParser.display(this.position.getUnrealised_pnl()), 99.00, Delta);
		this.position.transact_shares("SLD", 100, PriceParser.parse(75.78), PriceParser.parse(1.00));

		// still high
		Assert.assertEquals(PriceParser.display(this.position.getUnrealised_pnl()), 99.00, Delta);
		Assert.assertEquals(PriceParser.display(this.position.getRealised_pnl()), 98.00, Delta);

		this.position.update_market_value(PriceParser.parse(75.77), PriceParser.parse(75.79));
		Assert.assertEquals(PriceParser.display(this.position.getUnrealised_pnl()), 0.00, Delta);
    }
}