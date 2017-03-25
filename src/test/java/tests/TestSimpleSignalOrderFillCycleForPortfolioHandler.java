package tests;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.abigdreamer.ark.framework.collection.TwoTuple;

import qstrader.Portfolio;
import qstrader.PortfolioHandler;
import qstrader.PriceParser;
import qstrader.event.Event;
import qstrader.event.FillEvent;
import qstrader.event.OrderEvent;
import qstrader.event.SignalEvent;
import qstrader.order.SuggestedOrder;
import qstrader.position_sizer.AbstractPositionSizer;
import qstrader.price_handler.AbstractTickPriceHandler;
import qstrader.risk_manager.AbstractRiskManager;

/**
 * Tests a simple Signal, Order and Fill cycle for the
    PortfolioHandler. This is, in effect, a sanity check.
 * @author Administrator
 */
public class TestSimpleSignalOrderFillCycleForPortfolioHandler {

	long initial_cash;
	Queue<Event> events_queue;
	private PriceHandlerMock price_handler;
	private PositionSizerMock position_sizer;
	private RiskManagerMock risk_manager;
	private PortfolioHandler portfolio_handler;
	
	/**
	 * Set up the PortfolioHandler object supplying it with
        $500,000.00 USD in initial cash.
	 */
	@Before
	public void setUp() {
		this.initial_cash = PriceParser.parse("500000.00");
		this.events_queue = new LinkedBlockingQueue<>();

		this.price_handler = new PriceHandlerMock();
		this.position_sizer = new PositionSizerMock();
		this.risk_manager = new RiskManagerMock();
		// // Create the PortfolioHandler object from the rest
		this.portfolio_handler = new PortfolioHandler(initial_cash, events_queue, price_handler, position_sizer,
				risk_manager);
	}
	
	/**
	 * Tests the "_create_order_from_signal" method
        as a basic sanity check.
	 */
	@Test
	public void test_create_order_from_signal_basic_check() {
		SignalEvent signal_event = new SignalEvent("MSFT", "BOT");
		SuggestedOrder order = this.portfolio_handler._create_order_from_signal(signal_event);
		Assert.assertEquals(order.getTicker(), "MSFT");
		Assert.assertEquals(order.getAction(), "BOT");
		Assert.assertEquals(order.getQuantity(), 0);
	}
	
	/**
	 * Tests the "_place_orders_onto_queue" method
        as a basic sanity check.
	 */
	@Test
	public void test_place_orders_onto_queue_basic_check() {
		OrderEvent order = new OrderEvent("MSFT", "BOT", 100);
		List<OrderEvent> order_list = Arrays.asList(order);
		this.portfolio_handler._place_orders_onto_queue(order_list);
		OrderEvent ret_order = (OrderEvent) this.events_queue.poll();
		Assert.assertEquals(ret_order.getTicker(), "MSFT");
		Assert.assertEquals(ret_order.getAction(), "BOT");
		Assert.assertEquals(ret_order.getQuantity(), 100);
	}
	
	/**
	 * Tests the "_convert_fill_to_portfolio_update" method
        as a basic sanity check.
	 */
	@Test
	public void test_convert_fill_to_portfolio_update_basic_check() {
		FillEvent fill_event_buy = new FillEvent(LocalDateTime.now(), "MSFT", "BOT", 100, "ARCA",
				PriceParser.parse("50.25"), PriceParser.parse("1.00"));
		this.portfolio_handler._convert_fill_to_portfolio_update(fill_event_buy);
		// Check the Portfolio values within the PortfolioHandler
		Portfolio port = this.portfolio_handler.getPortfolio();
		Assert.assertEquals(port.getCur_cash(), PriceParser.parse("494974.00"));

		// TODO: Finish this off and check it works via Interactive Brokers
		FillEvent fill_event_sell = new FillEvent(LocalDateTime.now(), "MSFT", "SLD", 100, "ARCA",
				PriceParser.parse("50.25"), PriceParser.parse("1.00"));
		this.portfolio_handler._convert_fill_to_portfolio_update(fill_event_sell);
	}
	
	/**
	 * Tests the "on_signal" method as a basic sanity check.
	 */
	@Test
	public void test_on_signal_basic_check() {
		SignalEvent signal_event = new SignalEvent("MSFT", "BOT");
		this.portfolio_handler.on_signal(signal_event);
		OrderEvent ret_order = (OrderEvent) this.events_queue.poll();
		Assert.assertEquals(ret_order.getTicker(), "MSFT");
		Assert.assertEquals(ret_order.getAction(), "BOT");
		Assert.assertEquals(ret_order.getQuantity(), 100);
	}
	
	class PriceHandlerMock extends AbstractTickPriceHandler {

		@Override
		public void stream_next() {
		}
		
		@Override
		public TwoTuple<Long, Long> get_best_bid_ask(String ticker) {
			Map<String, TwoTuple<Long, Long>> prices = new HashMap<>();
			prices.put("MSFT", new TwoTuple<>(PriceParser.parse(50.28), PriceParser.parse(50.31)));
			prices.put("GOOG", new TwoTuple<>(PriceParser.parse(705.46), PriceParser.parse(705.46)));
			prices.put("AMZN", new TwoTuple<>(PriceParser.parse(564.14), PriceParser.parse(565.14)));
			
			
			return prices.get(ticker);
		}
		
	}
	
	class PositionSizerMock implements AbstractPositionSizer {

		/**
		 * This PositionSizerMock object simply modifies
        	the quantity to be 100 of any share transacted.
		 */
		@Override
		public SuggestedOrder size_order(Portfolio portfolio, SuggestedOrder initial_order) {
			initial_order.setQuantity(100);
			return initial_order;
		}
		
	}
	
	class RiskManagerMock implements AbstractRiskManager {

		/**
		 * This RiskManagerMock object simply lets the
        	sized order through, creates the corresponding
        	OrderEvent object and adds it to a list.
		 */
		@Override
		public List<OrderEvent> refine_orders(Portfolio portfolio, SuggestedOrder sized_order) {
			OrderEvent order_event = new OrderEvent(
		            sized_order.getTicker(),
		            sized_order.getAction(),
		            sized_order.getQuantity()
		        );
			return Arrays.asList(order_event);
		}
		
	}
}
