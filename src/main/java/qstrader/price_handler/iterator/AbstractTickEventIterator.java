package qstrader.price_handler.iterator;

import com.github.rapidark.framework.collection.DataRow;

import qstrader.PriceParser;
import qstrader.event.TickEvent;
import qstrader.exception.EmptyTickEvent;

/**
 *  
 * @author Darkness
 * @date 2016年12月13日 下午4:03:44
 * @version V1.0
 */
public abstract class AbstractTickEventIterator extends AbstractPriceEventIterator {
	
	/**
	 *  Obtain all elements of the bar a row of dataframe
        and return a TickEvent
	 *  
	 * @author Darkness
	 * @date 2016年12月14日 下午5:15:19
	 * @version V1.0
	 */
    public TickEvent _create_event(String time,String ticker,DataRow row) {
		if (row == null) {
			throw new EmptyTickEvent(String.format("row %s %s %s can't be convert to TickEvent", time, ticker, row));
		}
		double bid = PriceParser.parse(row.getString("Bid"));
		double ask = PriceParser.parse(row.getString("Ask"));
		TickEvent tev = new TickEvent(ticker, time, bid, ask);
		return tev;
    }
}