package qstrader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import com.abigdreamer.ark.commons.collection.TwoTuple;

import qstrader.price_handler.AbstractBarPriceHandler;
import qstrader.price_handler.AbstractPriceHandler;
import qstrader.price_handler.AbstractTickPriceHandler;

public class Portfolio {
        
    private long unrealised_pnl;
	public AbstractPriceHandler price_handler;
	private long init_cash;
	private long equity;
	private long cur_cash;
	public HashMap<String, Position> positions;
	private ArrayList<Position> closed_positions;
	private long realised_pnl;
	
	/**
	 *  On creation, the Portfolio object contains no
        positions and all values are "reset" to the initial
        cash, with no PnL - realised or unrealised.

        Note that realised_pnl is the running tally pnl from closed
        positions (closed_pnl), as well as realised_pnl
        from currently open positions.
	 * @param price_handler
	 * @param cash
	 */
	public Portfolio(AbstractPriceHandler price_handler, long cash) {
		this.price_handler = price_handler;
		this.init_cash = cash;
		this.equity = cash;
		this.cur_cash = cash;
		this.positions = new HashMap<>();
		this.closed_positions = new ArrayList<>();

		this.realised_pnl = 0;
	}
              
	/**
	 *  Updates the value of all positions that are currently open.
        Value of closed positions is tallied as this.realised_pnl.
	 *  
	 * @author Darkness
	 * @date 2016年12月16日 下午3:25:25
	 * @version V1.0
	 */
	public void _update_portfolio() {
        this.unrealised_pnl = 0;
        this.equity = this.realised_pnl;
        this.equity += this.init_cash;
        
        for (Entry<String, Position> positionEntry : this.positions.entrySet()) {
        	String ticker= positionEntry.getKey();
        	Position pt = positionEntry.getValue();
        	
        	long bid, ask;
            if (this.price_handler.istick()) {
            	AbstractTickPriceHandler tickPriceHandler = (AbstractTickPriceHandler)this.price_handler;
            	TwoTuple<Long, Long> bidAsk = tickPriceHandler.get_best_bid_ask(ticker);
            	bid = bidAsk.first;
            	ask = bidAsk.second;
            }else{
            	AbstractBarPriceHandler tickPriceHandler = (AbstractBarPriceHandler)this.price_handler;
                long close_price = tickPriceHandler.get_last_close(ticker);
                bid = close_price;
                ask = close_price;
            }
            
            pt.update_market_value(bid, ask);
            this.unrealised_pnl += pt.unrealised_pnl;
			this.equity += (pt.market_value - pt.cost_basis + pt.realised_pnl);
        }
    }
    
    /**
     *  Adds a new Position object to the Portfolio. This
        requires getting the best bid/ask price from the
        price handler in order to calculate a reasonable
        "market value".

        Once the Position is added, the Portfolio values
        are updated.
     *  
     * @author Darkness
     * @date 2016年12月14日 下午4:34:52
     * @version V1.0
     */
	public void _add_position(String action, String ticker, long quantity, long price, long commission) {
        if (this.positions.containsKey(ticker)) {
        	System.out.println(String.format("Ticker %s is already in the positions list. Could not add a new position.", ticker));
        	return;
        }
		long bid, ask;
		if (this.price_handler.istick()) {
			AbstractTickPriceHandler tickPriceHandler = (AbstractTickPriceHandler) this.price_handler;
			TwoTuple<Long, Long> bidAndAsk = tickPriceHandler.get_best_bid_ask(ticker);
			bid = bidAndAsk.first;
			ask = bidAndAsk.second;
		} else {
			AbstractBarPriceHandler barPriceHandler = (AbstractBarPriceHandler) this.price_handler;
			long close_price = barPriceHandler.get_last_close(ticker);
			bid = close_price;
			ask = close_price;
		}
		Position position = new Position(action, ticker, quantity, price, commission, bid, ask);
		this.positions.put(ticker, position);
		this._update_portfolio();
    }
    
	/**
	 * Modifies a current Position object to the Portfolio.
        This requires getting the best bid/ask price from the
        price handler in order to calculate a reasonable
        "market value".

        Once the Position is modified, the Portfolio values
        are updated.
	 *  
	 * @author Darkness
	 * @date 2016年12月14日 下午4:39:15
	 * @version V1.0
	 */
	private void _modify_position(String action, String ticker, long quantity, long price, long commission) {
    	if(this.positions.containsKey(ticker)) {
    		Position position = this.positions.get(ticker);
			position.transact_shares(action, quantity, price, commission);
            long bid, ask;
            if( this.price_handler.istick()) {
            	AbstractTickPriceHandler tickPriceHandler = (AbstractTickPriceHandler)this.price_handler;
                TwoTuple<Long, Long> bidAndAsk = tickPriceHandler.get_best_bid_ask(ticker);
                bid = bidAndAsk.first;
                ask = bidAndAsk.second;
            } else {
                AbstractBarPriceHandler barPriceHandler = (AbstractBarPriceHandler)this.price_handler;
                long close_price = barPriceHandler.get_last_close(ticker);
                bid = close_price;
                ask = close_price;
            }
            position.update_market_value(bid, ask);

            if (position.quantity == 0) {
                Position closed = this.positions.remove(ticker);
                this.realised_pnl += closed.realised_pnl;
                System.out.println(this.realised_pnl);
                this.closed_positions.add(closed);
            }
            this._update_portfolio();
    	} else {
    		System.out.println(String.format("Ticker %s not in the current position list. Could not modify a current position." , ticker));
    	}
    }
    
    /**
     * Handles any new position or modification to
        a current position, by calling the respective
        _add_position and _modify_position methods.

        Hence, this single method will be called by the
        PortfolioHandler to update the Portfolio itself.
     *  
     * @author Darkness
     * @date 2016年12月14日 下午4:43:28
     * @version V1.0
     */
	public void transact_position(String action, String ticker, long quantity, long price, long commission) {
		if (action.equals("BOT")) {
			this.cur_cash -= quantity * price + commission;
		} else if (action.equals("SLD")) {
			this.cur_cash += quantity * price - commission;
		}
        if(this.positions.containsKey(ticker)) {
			this._modify_position(action, ticker, quantity, price, commission);
        } else {
			this._add_position(action, ticker, quantity, price, commission);
        }
    }
	
	public long getEquity() {
		return equity;
	}

	public long getUnrealised_pnl() {
		return unrealised_pnl;
	}

	public AbstractPriceHandler getPrice_handler() {
		return price_handler;
	}

	public long getInit_cash() {
		return init_cash;
	}

	public long getCur_cash() {
		return cur_cash;
	}

	public HashMap<String, Position> getPositions() {
		return positions;
	}

	public ArrayList<Position> getClosed_positions() {
		return closed_positions;
	}

	public long getRealised_pnl() {
		return realised_pnl;
	}
}