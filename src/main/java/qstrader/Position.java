package qstrader;

public class Position {
	
	String  action;
	Object ticker;
	long quantity;
	double init_price;
	double init_commission;
	double realised_pnl;
	double unrealised_pnl;
	long buys;
	long sells;
	double avg_bot;
	double avg_sld;
	double total_bot;
	double total_sld;
	double total_commission;
	double market_value;
	double cost_basis;
	double net_incl_comm;
	long net;
	double net_total;
	double avg_price;
	
	/**
	 *  Set up the initial "account" of the Position to be
        zero for most items, with the exception of the initial
        purchase/sale.

        Then calculate the initial values and finally update the
        market value of the transaction.
	 * @param action
	 * @param ticker
	 * @param init_quantity
	 * @param init_price
	 * @param init_commission
	 * @param bid
	 * @param ask
	 */
	public Position(String action, String ticker, long init_quantity, double init_price, double init_commission,
			double bid, double ask) {
        this.action = action;
        this.ticker = ticker;
        this.quantity = init_quantity;
        this.init_price = init_price;
        this.init_commission = init_commission;

        this.realised_pnl = 0;
        this.unrealised_pnl = 0;

        this.buys = 0;
        this.sells = 0;
        this.avg_bot = 0;
        this.avg_sld = 0;
        this.total_bot = 0;
        this.total_sld = 0;
        this.total_commission = init_commission;

        this._calculate_initial_value();
        this.update_market_value(bid, ask);
    }
    
	/**
	 *  Depending upon whether the action was a buy or sell ("BOT"
        or "SLD") calculate the average bought cost, the total bought
        cost, the average price and the cost basis.

        Finally, calculate the net total with and without commission.
	 *  
	 * @author Darkness
	 * @date 2016年12月16日 下午3:42:43
	 * @version V1.0
	 */
    public void _calculate_initial_value() {
        if (this.action == "BOT") {
            this.buys = this.quantity;
            this.avg_bot = this.init_price;
            this.total_bot = this.buys * this.avg_bot;
            this.avg_price = (this.init_price * this.quantity + this.init_commission) / this.quantity;
            this.cost_basis = this.quantity * this.avg_price;
        } else {// action == "SLD"
            this.sells = this.quantity;
            this.avg_sld = this.init_price;
            this.total_sld = this.sells * this.avg_sld;
            this.avg_price = (this.init_price * this.quantity - this.init_commission) / this.quantity;
            this.cost_basis = -this.quantity * this.avg_price;
        }
        this.net = this.buys - this.sells;
        this.net_total = this.total_sld - this.total_bot;
        this.net_incl_comm = this.net_total - this.init_commission;
    }
    
    /**
     *  The market value is tricky to calculate as we only have
        access to the top of the order book through Interactive
        Brokers, which means that the true redemption price is
        unknown until executed.

        However, it can be estimated via the mid-price of the
        bid-ask spread. Once the market value is calculated it
        allows calculation of the unrealised and realised profit
        and loss of any transactions.
     *  
     * @author Darkness
     * @date 2016年12月16日 下午3:43:53
     * @version V1.0
     */
    public void update_market_value(double bid, double ask) {
        double midpoint = (bid + ask) / 2;
        this.market_value = this.quantity * midpoint * sign(this.net);
        this.unrealised_pnl = this.market_value - this.cost_basis;
        //this.realised_pnl = this.market_value + this.net_incl_comm;
    }

    //计算各元素的正负号：1（正数）、0（零）、-1（负数）
	private int sign(double value) {
		if (value > 0) {
			return 1;
		}
		if (value < 0) {
			return -1;
		}
		return 0;
	}

	/**
	 *  Calculates the adjustments to the Position that occur
        once new shares are bought and sold.

        Takes care to update the average bought/sold, total
        bought/sold, the cost basis and PnL calculations,
        as carried out through Interactive Brokers TWS.
	 *  
	 * @author Darkness
	 * @date 2016年12月16日 下午3:45:29
	 * @version V1.0
	 */
    public void transact_shares(String action,long quantity,double price,double commission){
        this.total_commission += commission;

		// Adjust total bought and sold
		if (action == "BOT") {
			this.avg_bot = (this.avg_bot * this.buys + price * quantity) / (this.buys + quantity);
			if (this.action != "SLD") {// Increasing long position
				this.avg_price = (this.avg_price * this.buys + price * quantity + commission) / (this.buys + quantity);
			} else if(this.action == "SLD") {// Closed partial positions out
				// Adjust realised PNL
				this.realised_pnl += quantity * (this.avg_price - price) - commission;
			}
			this.buys += quantity;
			this.total_bot = this.buys * this.avg_bot;
		} else {// action == "SLD"
			this.avg_sld = (this.avg_sld * this.sells + price * quantity) / (this.sells + quantity);
			if (this.action != "BOT") {// Increasing short position
				this.avg_price = (this.avg_price * this.sells + price * quantity - commission)
						/ (this.sells + quantity);
				this.unrealised_pnl -= commission;
			} else if (this.action == "BOT") {//Closed partial positions out
				 this.realised_pnl += quantity * (
		                    price - this.avg_price
		                ) - commission;
			}
			this.sells += quantity;
			this.total_sld = this.sells * this.avg_sld;
		}
		// Adjust net values, including commissions
		this.net = this.buys - this.sells;
		this.quantity = this.net;
		this.net_total = this.total_sld - this.total_bot;
		this.net_incl_comm = this.net_total - this.total_commission;

		// Adjust average price and cost basis
		this.cost_basis = this.quantity * this.avg_price;
    }
    
    public long getQuantity() {
		return quantity;
	}
}