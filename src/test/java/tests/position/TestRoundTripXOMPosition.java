package tests.position;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import qstrader.Position;
import qstrader.PriceParser;

/**
 *  Test a round-trip trade in Exxon-Mobil where the initial
    trade is a buy/long of 100 shares of XOM, at a price of
    $74.78, with $1.00 commission.
    
 * @author Administrator
 *
 */
public class TestRoundTripXOMPosition {

	double Delta = 0.00000001;
	
	private Position position;

	/**
	 * Set up the Position object that will store the PnL.
	 */
	@Before
	public void setUp() {
		this.position = new Position("BOT", "XOM", 100, 
				PriceParser.parse(74.78), PriceParser.parse(1.00),
				PriceParser.parse(74.78), PriceParser.parse(74.80));
	}
	
	/**
	 * After the subsequent purchase, carry out two more buys/longs
        and then close the position out with two additional sells/shorts.

        The following prices have been tested against those calculated
        via Interactive Brokers' Trader Workstation (TWS).
	 */
	@Test
	public void test_calculate_round_trip() {
		this.position.transact_shares("BOT", 100, PriceParser.parse(74.63), PriceParser.parse(1.00));
		this.position.transact_shares("BOT", 250, PriceParser.parse(74.620), PriceParser.parse(1.25));
		
		this.position.transact_shares("SLD", 200, PriceParser.parse(74.58), PriceParser.parse(1.00));
		this.position.transact_shares("SLD", 250, PriceParser.parse(75.26), PriceParser.parse(1.25));
		
		this.position.update_market_value(PriceParser.parse(77.75), PriceParser.parse(77.77));
		
		Assert.assertEquals(this.position.getAction(), "BOT");
		Assert.assertEquals(this.position.getTicker(), "XOM");
		Assert.assertEquals(this.position.getQuantity(), 0);

		Assert.assertEquals(this.position.getBuys(), 450);
		Assert.assertEquals(this.position.getSells(), 450);
		Assert.assertEquals(this.position.getNet(), 0);
		Assert.assertEquals(PriceParser.display(this.position.getAvg_bot(), 5), 74.65778, Delta);
		Assert.assertEquals(PriceParser.display(this.position.getAvg_sld(), 5), 74.95778, Delta);
		Assert.assertEquals(PriceParser.display(this.position.getTotal_bot()), 33596.00, Delta);
		Assert.assertEquals(PriceParser.display(this.position.getTotal_sld()), 33731.00, Delta);
		Assert.assertEquals(PriceParser.display(this.position.getNet_total()), 135.00, Delta);
		Assert.assertEquals(PriceParser.display(this.position.getTotal_commission()), 5.50, Delta);
		Assert.assertEquals(PriceParser.display(this.position.getNet_incl_comm()), 129.50, Delta);

		Assert.assertEquals(PriceParser.display(this.position.getAvg_price(), 3), 74.665, Delta);
		Assert.assertEquals(PriceParser.display(this.position.getCost_basis()), 0.00, Delta);
		Assert.assertEquals(PriceParser.display(this.position.getMarket_value()), 0.00, Delta);
		Assert.assertEquals(PriceParser.display(this.position.getUnrealised_pnl()), 0.00, Delta);
		Assert.assertEquals(PriceParser.display(this.position.getRealised_pnl()), 129.50, Delta);
	}
}