package tests.position;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import qstrader.Position;
import qstrader.PriceParser;

public class TestShortPosition {
	
	double Delta = 0.00000001;
	private Position position;

	/**
	 *  Test a short position in Proctor & Gamble where the initial
	    trade is a sell/short of 100 shares of PG, at a price of
	    $77.69, with $1.00 commission.
	 */
	@Before
	public void setUp() {
        this.position = new Position(
            "SLD", "PG", 100,
            PriceParser.parse(77.69), PriceParser.parse(1.00),
            PriceParser.parse(77.68), PriceParser.parse(77.70)
        );
	}
	
	@Test
	public void test_open_short_position() {
		Assert.assertEquals(PriceParser.display(this.position.getCost_basis()), -7768.00, Delta);
		Assert.assertEquals(PriceParser.display(this.position.getMarket_value()), -7769.00, Delta);
		Assert.assertEquals(PriceParser.display(this.position.getUnrealised_pnl()), -1.00, Delta);
		Assert.assertEquals(PriceParser.display(this.position.getRealised_pnl()), 0.00, Delta);

		this.position.update_market_value(PriceParser.parse(77.72), PriceParser.parse(77.72));

		Assert.assertEquals(PriceParser.display(this.position.getCost_basis()), -7768.00, Delta);
		Assert.assertEquals(PriceParser.display(this.position.getMarket_value()), -7772.00, Delta);
		Assert.assertEquals(PriceParser.display(this.position.getUnrealised_pnl()), -4.00, Delta);
		Assert.assertEquals(PriceParser.display(this.position.getRealised_pnl()), 0.00, Delta);
	}
}