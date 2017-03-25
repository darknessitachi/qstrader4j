package tests;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import qstrader.Portfolio;
import qstrader.PriceParser;
import qstrader.event.BarEvent;
import qstrader.order.SuggestedOrder;
import qstrader.position_sizer.LiquidateRebalancePositionSizer;
import qstrader.price_handler.AbstractBarPriceHandler;

public class TestLiquidateRebalancePositionSizer {

	private Map<String, Double> ticker_weights;
	private PriceHandlerMock price_handler_mock;
	private LiquidateRebalancePositionSizer position_sizer;
	private Portfolio portfolio;
	
	@Before
	public void setUp() {
		price_handler_mock = new PriceHandlerMock();
		
		ticker_weights = new HashMap<>();
		ticker_weights.put("AAA", 0.3);
		ticker_weights.put("BBB", 0.7);
		
		this.position_sizer = new LiquidateRebalancePositionSizer(ticker_weights);
		this.portfolio = new Portfolio(price_handler_mock, PriceParser.parse(10000.00));
	}
	
	/**
	 * Tests that the position sizer will open up new positions with
        the correct weights.
	 */
	@Test
	public void test_will_add_positions() {
		SuggestedOrder order_a = new SuggestedOrder("AAA", "BOT", 0);
		SuggestedOrder order_b = new SuggestedOrder("BBB", "BOT", 0);
		SuggestedOrder sized_a = this.position_sizer.size_order(this.portfolio, order_a);
		SuggestedOrder sized_b = this.position_sizer.size_order(this.portfolio, order_b);

		Assert.assertEquals(sized_a.getAction(), "BOT");
		Assert.assertEquals(sized_b.getAction(), "BOT");
		Assert.assertEquals(sized_a.getQuantity(), 60);
		Assert.assertEquals(sized_b.getQuantity(), 70);
	}
	
	/**
	 * Ensure positions will be liquidated completely when asked.
        Include a long & a short.
	 */
	@Test
	public void test_will_liquidate_positions() {
		this.portfolio._add_position("BOT", "AAA", 100, PriceParser.parse(60.00), 0);
		this.portfolio._add_position("BOT", "BBB", -100, PriceParser.parse(60.00), 0);

		SuggestedOrder exit_a = new SuggestedOrder("AAA", "EXIT", 0);
		SuggestedOrder exit_b = new SuggestedOrder("BBB", "EXIT", 0);
		SuggestedOrder sized_a = this.position_sizer.size_order(this.portfolio, exit_a);
		SuggestedOrder sized_b = this.position_sizer.size_order(this.portfolio, exit_b);

		Assert.assertEquals(sized_a.getAction(), "SLD");
		Assert.assertEquals(sized_b.getAction(), "BOT");
		Assert.assertEquals(sized_a.getQuantity(), 100);
		Assert.assertEquals(sized_a.getQuantity(), 100);
	}
	
	class PriceHandlerMock extends AbstractBarPriceHandler {
	    
		public PriceHandlerMock() {
			tickers.put("AAA", new BarEvent("AAA", LocalDateTime.MIN, 0, 0, 0, 0, 0, 0, PriceParser.parse(50.00)));
			tickers.put("BBB", new BarEvent("AAA", LocalDateTime.MIN, 0, 0, 0, 0, 0, 0, PriceParser.parse(100.00)));
			tickers.put("CCC", new BarEvent("AAA", LocalDateTime.MIN, 0, 0, 0, 0, 0, 0, PriceParser.parse(1.00)));
	    }

		@Override
		public long get_last_close(String ticker) {
			BarEvent event = (BarEvent)tickers.get(ticker);
			return event.getAdj_close_price();
		}

		@Override
		public void stream_next() {
		}
		
	}
}
