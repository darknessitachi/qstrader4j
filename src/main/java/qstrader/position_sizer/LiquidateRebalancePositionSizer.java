package qstrader.position_sizer;

import java.util.Map;

import qstrader.Portfolio;
import qstrader.PriceParser;
import qstrader.event.BarEvent;
import qstrader.order.SuggestedOrder;

/**
 *  Carries out a periodic full liquidation and rebalance of
    the Portfolio.

    This is achieved by determining whether an order type type
    is "EXIT" or "BOT/SLD".

    If the former, the current quantity of shares in the ticker
    is determined and then BOT or SLD to net the position to zero.

    If the latter, the current quantity of shares to obtain is
    determined by prespecified weights and adjusted to reflect
    current account equity.
 * @author darkness
 *
 */
public class LiquidateRebalancePositionSizer implements AbstractPositionSizer {

    private Map<String, Integer> ticker_weights;

	public LiquidateRebalancePositionSizer(Map<String, Integer> ticker_weights){
        this.ticker_weights = ticker_weights;
    }
    
	/**
	 * Size the order to reflect the dollar-weighting of the
        current equity account size based on pre-specified
        ticker weights.
	 */
    @Override
	public SuggestedOrder size_order(Portfolio portfolio, SuggestedOrder initial_order) {
		String ticker = initial_order.getTicker();
		if (initial_order.getAction().equals("EXIT")) {
			// Obtain current quantity and liquidate
			long cur_quantity = portfolio.positions.get(ticker).getQuantity();
			if (cur_quantity > 0) {
				initial_order.setAction("SLD");
				initial_order.setQuantity(cur_quantity);
			} else if (cur_quantity < 0) {
				initial_order.setAction("BOT");
				initial_order.setQuantity(cur_quantity);
			} else {
				initial_order.setQuantity(0);
			}
		} else {
			int weight = this.ticker_weights.get(ticker);
			// Determine total portfolio value, work out dollar weight
			// and finally determine integer quantity of shares to purchase
			BarEvent barEvent = (BarEvent) portfolio.price_handler.tickers.get(ticker);
			long price = barEvent.getAdj_close_price();
//			price = PriceParser.display(price);
			double equity = portfolio.getEquity();//PriceParser.display(portfolio.getEquity())
//			price /= PriceParser.PRICE_MULTIPLIER;
//			double equity = portfolio.getEquity() / PriceParser.PRICE_MULTIPLIER;
			double dollar_weight = weight * equity;
			double weighted_quantity = dollar_weight / price;
			// Update quantity
			initial_order.setQuantity((long) weighted_quantity);
		}
		return initial_order;
    }
}