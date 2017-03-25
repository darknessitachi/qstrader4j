package qstrader;

import java.util.List;
import java.util.Queue;

import qstrader.event.Event;
import qstrader.event.FillEvent;
import qstrader.event.OrderEvent;
import qstrader.event.SignalEvent;
import qstrader.order.SuggestedOrder;
import qstrader.position_sizer.AbstractPositionSizer;
import qstrader.price_handler.AbstractPriceHandler;
import qstrader.risk_manager.AbstractRiskManager;

public class PortfolioHandler {
	
	private long initial_cash;
	private Queue<Event> events_queue;
	private AbstractPriceHandler price_handler;
	private AbstractPositionSizer position_sizer;
	private AbstractRiskManager risk_manager;
	private Portfolio portfolio;

	/**
	 *  The PortfolioHandler is designed to interact with the
        backtesting or live trading overall event-driven
        architecture. It exposes two methods, on_signal and
        on_fill, which handle how SignalEvent and FillEvent
        objects are dealt with.

        Each PortfolioHandler contains a Portfolio object,
        which stores the actual Position objects.

        The PortfolioHandler takes a handle to a PositionSizer
        object which determines a mechanism, based on the current
        Portfolio, as to how to size a new Order.

        The PortfolioHandler also takes a handle to the
        RiskManager, which is used to modify any generated
        Orders to remain in line with risk parameters.
	 * @param initial_cash
	 * @param events_queue
	 * @param price_handler
	 * @param position_sizer
	 * @param risk_manager
	 */
	public PortfolioHandler(long initial_cash, Queue<Event> events_queue, AbstractPriceHandler price_handler, AbstractPositionSizer position_sizer,
			AbstractRiskManager risk_manager) {
		this.initial_cash = initial_cash;
		this.events_queue = events_queue;
		this.price_handler = price_handler;
		this.position_sizer = position_sizer;
		this.risk_manager = risk_manager;
		this.portfolio = new Portfolio(price_handler, initial_cash);
	}
	
	public Portfolio getPortfolio() {
		return portfolio;
	}

	/**
	 * Take a SignalEvent object and use it to form a
        SuggestedOrder object. These are not OrderEvent objects,
        as they have yet to be sent to the RiskManager object.
        At this stage they are simply "suggestions" that the
        RiskManager will either verify, modify or eliminate.
	 * @param signal_event
	 * @return
	 */
	public SuggestedOrder _create_order_from_signal(SignalEvent signal_event) {
		int quantity = signal_event.getSuggested_quantity();
		SuggestedOrder order = new SuggestedOrder(signal_event.getTicker(), signal_event.getAction(), quantity);
		return order;
	}
    
	/**
	 *  Once the RiskManager has verified, modified or eliminated
        any order objects, they are placed onto the events queue,
        to ultimately be executed by the ExecutionHandler.
	 *  
	 * @author Darkness
	 * @date 2016年12月16日 下午3:38:36
	 * @version V1.0
	 */
    public void _place_orders_onto_queue(List<OrderEvent> order_list){
        for (OrderEvent order_event : order_list){
            this.events_queue.offer(order_event);
        }
    }
    
    /**
     *  Upon receipt of a FillEvent, the PortfolioHandler converts
        the event into a transaction that gets stored in the Portfolio
        object. This ensures that the broker and the local portfolio
        are "in sync".

        In addition, for backtesting purposes, the portfolio value can
        be reasonably estimated in a realistic manner, simply by
        modifying how the ExecutionHandler object handles slippage,
        transaction costs, liquidity and market impact.
     *  
     * @author Darkness
     * @date 2016年12月16日 下午3:38:51
     * @version V1.0
     */
    public void _convert_fill_to_portfolio_update(FillEvent fill_event){
        String action = fill_event.getAction();
        String ticker = fill_event.getTicker();
        long quantity = fill_event.getQuantity();
        long price = fill_event.getPrice();
        long commission = fill_event.getCommission();
		// Create or modify the position from the fill info
		this.portfolio.transact_position(action, ticker, quantity, price, commission);
    }
    
    /**
     *  This is called by the backtester or live trading architecture
        to form the initial orders from the SignalEvent.

        These orders are sized by the PositionSizer object and then
        sent to the RiskManager to verify, modify or eliminate.

        Once received from the RiskManager they are converted into
        full OrderEvent objects and sent back to the events queue.
     *  
     * @author Darkness
     * @date 2016年12月16日 下午3:39:47
     * @version V1.0
     */
    public void on_signal(SignalEvent signal_event){
		// Create the initial order list from a signal event
		SuggestedOrder initial_order = this._create_order_from_signal(signal_event);
		// Size the quantity of the initial order
		SuggestedOrder sized_order = this.position_sizer.size_order(this.portfolio, initial_order);
		// Refine or eliminate the order via the risk manager overlay
		List<OrderEvent> order_events = this.risk_manager.refine_orders(this.portfolio, sized_order);
		// Place orders onto events queue
		this._place_orders_onto_queue(order_events);
    }
    
    /**
     *  This is called by the backtester or live trading architecture
        to take a FillEvent and update the Portfolio object with new
        or modified Positions.

        In a backtesting environment these FillEvents will be simulated
        by a model representing the execution, whereas in live trading
        they will come directly from a brokerage (such as Interactive
        Brokers).
     *  
     * @author Darkness
     * @date 2016年12月16日 下午3:41:30
     * @version V1.0
     */
    public void on_fill(FillEvent fill_event){
        this._convert_fill_to_portfolio_update(fill_event);
    }
    
    /**
     *  Update the portfolio to reflect current market value as
        based on last bid/ask of each ticker.
     *  
     * @author Darkness
     * @date 2016年12月16日 下午3:41:46
     * @version V1.0
     */
    public void update_portfolio_value(){
        this.portfolio._update_portfolio();
    }
}