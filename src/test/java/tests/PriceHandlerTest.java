package tests;

import java.io.File;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import oracle.net.aso.i;
import qstrader.Config;
import qstrader.PriceParser;
import qstrader.Settings;
import qstrader.event.Event;
import qstrader.event.TickEvent;
import qstrader.price_handler.AbstractPriceHandler;
import qstrader.price_handler.HistoricCSVTickPriceHandler;

/**
 * Test the initialisation of a PriceHandler object with
    a small list of tickers. Concatenate the ticker data (
    pre-generated and stored as a fixture) and stream the
    subsequent ticks, checking that the correct bid-ask
    values are returned.
 *  
 * @author Darkness
 * @date 2016年12月16日 下午4:31:04
 * @version V1.0
 */
public class PriceHandlerTest {
	
	AbstractPriceHandler price_handler;
	Config config;
	
	double Delta = 0.000000001;
	
	/**
	 * Set up the PriceHandler object with a small set of initial tickers.
	 */
	@Before
	public void setup() {
		config = Settings.TEST;
		String fixtures_path = config.CSV_DATA_DIR;
		
        Queue<Event> events_queue = new LinkedBlockingQueue<>();
        List<String> init_tickers = Arrays.asList("GOOG", "AMZN", "MSFT");
		price_handler = new HistoricCSVTickPriceHandler(fixtures_path, events_queue, init_tickers);
	}
	
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss.SSS");
	
	private void assertTick(TickEvent tickers, String time, double bid, double ask) {
		 Assert.assertEquals(time,tickers.getTime().format(formatter));
        Assert.assertEquals(bid, PriceParser.display(tickers.getBid(), 5), Delta);
        Assert.assertEquals(ask, PriceParser.display(tickers.getAsk(), 5), Delta);
	}
	
	/**
	 * The initialisation of the class will open the three
        test CSV files, then merge and sort them. They will
        then be stored in a member "tick_stream". This will
        be used for streaming the ticks.
	 *  
	 * @author Darkness
	 * @date 2016年12月16日 下午4:36:05
	 * @version V1.0
	 */
	@Test
	public void  test_stream_all_ticks() {
        // Stream to Tick #1 (GOOG)
        price_handler.stream_next();
        
        TickEvent tickers = (TickEvent)price_handler.tickers.get("GOOG");
        assertTick(tickers, "01-02-2016 00:00:01.358", 683.56000, 683.58000);

        // Stream to Tick #2 (AMZN)
        price_handler.stream_next();
        tickers = (TickEvent)price_handler.tickers.get("AMZN");
        assertTick(tickers, "01-02-2016 00:00:01.562", 502.10001, 502.11999);

        // Stream to Tick #3 (MSFT)
        price_handler.stream_next();
        tickers = (TickEvent)price_handler.tickers.get("MSFT");
        assertTick(tickers, "01-02-2016 00:00:01.578", 50.14999, 50.17001);

        // Stream to Tick #10 (GOOG)
        for (int i = 4; i < 11; i++) {
        	 this.price_handler.stream_next();
		}
        tickers = (TickEvent)price_handler.tickers.get("GOOG");
        assertTick(tickers, "01-02-2016 00:00:05.215", 683.56001, 683.57999);

        // Stream to Tick #20 (GOOG)
        for (int i = 11; i < 21; i++) {
       	 this.price_handler.stream_next();
		}
        tickers = (TickEvent)price_handler.tickers.get("MSFT");
        assertTick(tickers, "01-02-2016 00:00:09.904", 50.15000, 50.17000);

        // Stream to Tick #30 (final tick, AMZN)
        for (int i = 21; i < 31; i++) {
          	 this.price_handler.stream_next();
   		}
        tickers = (TickEvent)price_handler.tickers.get("AMZN");
        assertTick(tickers, "01-02-2016 00:00:14.616", 502.10015, 502.11985);
	}
	
	/**
	 * Tests the 'subscribe_ticker' and 'unsubscribe_ticker'
        methods, and check that they raise exceptions when
        appropriate.
	 */
    public void  test_subscribe_unsubscribe(){
//        # Check unsubscribing a ticker that isn't
//        # in the price handler list
//        # ToFix: https://github.com/mhallsmoore/qstrader/issues/46
//        # self.assertRaises(
//        #     KeyError, lambda: self.price_handler.unsubscribe_ticker("PG")
//        # )
//
//        # Check a ticker that is already subscribed
//        # to make sure that it doesn't raise an exception
//        try {
//            this.price_handler.subscribe_ticker("GOOG");
//        } catch (Exception e) {
//			System.out.println("subscribe_ticker() raised %s unexpectedly" + e.getMessage());
//		}

//        # Subscribe a new ticker, without CSV
//        # ToFix: https://github.com/mhallsmoore/qstrader/issues/46
//        # self.assertRaises(
//        #     IOError, lambda: self.price_handler.subscribe_ticker("XOM")
//        # )

//        # Unsubscribe a current ticker
//        self.assertTrue("GOOG" in self.price_handler.tickers)
//        self.assertTrue("GOOG" in self.price_handler.tickers_data)
//        
//        self.price_handler.unsubscribe_ticker("GOOG")
//        self.assertTrue("GOOG" not in self.price_handler.tickers)
//        self.assertTrue("GOOG" not in self.price_handler.tickers_data)
    }

     public void  test_get_best_bid_ask(){
//        	"""
//            Tests that the 'get_best_bid_ask' method produces the
//            correct values depending upon validity of ticker.
//            """
//            bid, ask = self.price_handler.get_best_bid_ask("AMZN")
//            self.assertEqual(PriceParser.display(bid, 5), 502.10001)
//            self.assertEqual(PriceParser.display(ask, 5), 502.11999)
//
//            bid, ask = self.price_handler.get_best_bid_ask("C")
//            # TODO WHAT TO DO HERE?.
//            # self.assertEqual(PriceParser.display(bid, 5), None)
//            # self.assertEqual(PriceParser.display(ask, 5), None)
      
        }

}