package tests.position;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import qstrader.Position;
import qstrader.PriceParser;

public class TestRoundTripPGPosition {
	
	double Delta = 0.00000001;
	private Position position;

	/**
	 *  Test a round-trip trade in Proctor & Gamble where the initial
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
	
	/**
	 * After the subsequent sale, carry out two more sells/shorts
        and then close the position out with two additional buys/longs.

        The following prices have been tested against those calculated
        via Interactive Brokers' Trader Workstation (TWS).
	 */
	@Test
	public void test_calculate_round_trip() {
		this.position.transact_shares("SLD", 100, PriceParser.parse(77.68), PriceParser.parse(1.00));
		this.position.transact_shares("SLD", 50, PriceParser.parse(77.70), PriceParser.parse(1.00));
		this.position.transact_shares("BOT", 100, PriceParser.parse(77.77), PriceParser.parse(1.00));
		this.position.transact_shares("BOT", 150, PriceParser.parse(77.73), PriceParser.parse(1.00));
		this.position.update_market_value(PriceParser.parse(77.72), PriceParser.parse(77.72));

        Assert.assertEquals(this.position.getAction(), "SLD");
        Assert.assertEquals(this.position.getTicker(), "PG");
        Assert.assertEquals(this.position.getQuantity(), 0);

        Assert.assertEquals(this.position.getBuys(), 250);
        Assert.assertEquals(this.position.getSells(), 250);
        Assert.assertEquals(this.position.getNet(), 0);
        
		Assert.assertEquals(PriceParser.display(this.position.getAvg_bot(), 3), 77.746, Delta);
		Assert.assertEquals(PriceParser.display(this.position.getAvg_sld(), 3), 77.688, Delta);
		Assert.assertEquals(PriceParser.display(this.position.getTotal_bot()), 19436.50, Delta);
		Assert.assertEquals(PriceParser.display(this.position.getTotal_sld()), 19422.00, Delta);
		Assert.assertEquals(PriceParser.display(this.position.getNet_total()), -14.50, Delta);
		Assert.assertEquals(PriceParser.display(this.position.getTotal_commission()), 5.00, Delta);
		Assert.assertEquals(PriceParser.display(this.position.getNet_incl_comm()), -19.50, Delta);

		Assert.assertEquals(PriceParser.display(this.position.getAvg_price(), 5), 77.67600, Delta);
		Assert.assertEquals(PriceParser.display(this.position.getCost_basis()), 0.00, Delta);
		Assert.assertEquals(PriceParser.display(this.position.getMarket_value()), 0.00, Delta);
		Assert.assertEquals(PriceParser.display(this.position.getUnrealised_pnl()), 0.00, Delta);
		Assert.assertEquals(PriceParser.display(this.position.getRealised_pnl()), -19.50, Delta);
	}
}